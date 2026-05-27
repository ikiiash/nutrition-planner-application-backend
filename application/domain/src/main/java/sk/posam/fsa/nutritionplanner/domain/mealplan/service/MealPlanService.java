package sk.posam.fsa.nutritionplanner.domain.mealplan.service;

import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProduct;
import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProductRepository;
import sk.posam.fsa.nutritionplanner.domain.meal.Meal;
import sk.posam.fsa.nutritionplanner.domain.meal.MealIngredient;
import sk.posam.fsa.nutritionplanner.domain.meal.MealRepository;
import sk.posam.fsa.nutritionplanner.domain.mealplan.EntryType;
import sk.posam.fsa.nutritionplanner.domain.mealplan.MealPlan;
import sk.posam.fsa.nutritionplanner.domain.mealplan.MealPlanNotFoundException;
import sk.posam.fsa.nutritionplanner.domain.mealplan.MealPlanRepository;
import sk.posam.fsa.nutritionplanner.domain.mealplan.PlanDay;
import sk.posam.fsa.nutritionplanner.domain.mealplan.PlanEntry;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MealPlanService implements MealPlanFacade {

    private final MealPlanRepository mealPlanRepository;
    private final MealRepository mealRepository;
    private final FoodProductRepository foodProductRepository;

    public MealPlanService(MealPlanRepository mealPlanRepository,
                           MealRepository mealRepository,
                           FoodProductRepository foodProductRepository) {
        this.mealPlanRepository = mealPlanRepository;
        this.mealRepository = mealRepository;
        this.foodProductRepository = foodProductRepository;
    }

    @Override
    public MealPlan createMealPlan(String ownerUserId, MealPlan mealPlan) {
        mealPlan.setOwnerUserId(ownerUserId);
        mealPlan.validate();
        List<PlanDay> days = new ArrayList<>();
        for (int i = 1; i <= mealPlan.getNumberOfDays(); i++) {
            days.add(new PlanDay(i));
        }
        mealPlan.setDays(days);
        return mealPlanRepository.save(mealPlan);
    }

    @Override
    public List<MealPlan> readMealPlans(String ownerUserId) {
        return mealPlanRepository.readAll(ownerUserId);
    }

    @Override
    public MealPlan readMealPlan(String ownerUserId, Long mealPlanId) {
        return mealPlanRepository.readById(ownerUserId, mealPlanId)
                .orElseThrow(() -> new MealPlanNotFoundException(mealPlanId));
    }

    @Override
    public MealPlan updateMealPlan(String ownerUserId, Long mealPlanId, MealPlan mealPlan) {
        MealPlan existing = readMealPlan(ownerUserId, mealPlanId);
        existing.setName(mealPlan.getName());
        existing.setStartDate(mealPlan.getStartDate());

        int oldDays = existing.getNumberOfDays();
        int newDays = mealPlan.getNumberOfDays();
        existing.setNumberOfDays(newDays);

        if (newDays > oldDays) {
            for (int i = oldDays + 1; i <= newDays; i++) {
                existing.getDays().add(new PlanDay(i));
            }
        } else if (newDays < oldDays) {
            existing.getDays().removeIf(d -> d.getDayNumber() > newDays);
        }

        existing.validate();
        return mealPlanRepository.save(existing);
    }

    @Override
    public void deleteMealPlan(String ownerUserId, Long mealPlanId) {
        readMealPlan(ownerUserId, mealPlanId);
        mealPlanRepository.deleteById(ownerUserId, mealPlanId);
    }

    @Override
    public MealPlan activateMealPlan(String ownerUserId, Long mealPlanId) {
        mealPlanRepository.deactivateAll(ownerUserId);
        MealPlan plan = readMealPlan(ownerUserId, mealPlanId);
        plan.setActive(true);
        plan.setActivatedAt(LocalDate.now());
        return mealPlanRepository.save(plan);
    }

    @Override
    public MealPlan deactivateMealPlan(String ownerUserId, Long mealPlanId) {
        MealPlan plan = readMealPlan(ownerUserId, mealPlanId);
        plan.setActive(false);
        plan.setActivatedAt(null);
        return mealPlanRepository.save(plan);
    }

    @Override
    public MealPlan deductFridge(String ownerUserId, Long mealPlanId) {
        MealPlan plan = readMealPlan(ownerUserId, mealPlanId);
        if (!plan.isActive() || plan.getActivatedAt() == null) return plan;

        long daysElapsed = ChronoUnit.DAYS.between(plan.getActivatedAt(), LocalDate.now());
        int lastDeducted = plan.getLastDeductedDayNumber();

        List<PlanDay> daysToDeduct = plan.getDays().stream()
                .filter(d -> d.getDayNumber() > lastDeducted && d.getDayNumber() <= daysElapsed)
                .toList();
        if (daysToDeduct.isEmpty()) return plan;

        Map<Long, Double> deductions = new HashMap<>();
        for (PlanDay day : daysToDeduct) {
            for (PlanEntry entry : day.getEntries()) {
                if (entry.getEntryType() == EntryType.FOOD_PRODUCT
                        && entry.getFoodProductId() != null && entry.getGrams() != null) {
                    deductions.merge(entry.getFoodProductId(), entry.getGrams(), Double::sum);
                } else if (entry.getEntryType() == EntryType.MEAL && entry.getMealId() != null) {
                    mealRepository.readById(ownerUserId, entry.getMealId()).ifPresent(meal -> {
                        int servings = meal.getServings() != null && meal.getServings() > 0 ? meal.getServings() : 1;
                        double portions = entry.getPortions() != null ? entry.getPortions() : 1.0;
                        for (MealIngredient ing : meal.getIngredients()) {
                            if (ing.getGrams() != null) {
                                double g = (ing.getGrams() * portions) / servings;
                                deductions.merge(ing.getFoodProductId(), g, Double::sum);
                            }
                        }
                    });
                }
            }
        }

        for (Map.Entry<Long, Double> e : deductions.entrySet()) {
            foodProductRepository.readById(ownerUserId, e.getKey()).ifPresent(product -> {
                if (product.isInFridge() && product.getFridgeGrams() != null) {
                    product.setFridgeGrams(Math.max(0.0, product.getFridgeGrams() - e.getValue()));
                    foodProductRepository.save(product);
                }
            });
        }

        int maxDay = daysToDeduct.stream().mapToInt(PlanDay::getDayNumber).max().orElse(lastDeducted);
        plan.setLastDeductedDayNumber(maxDay);
        return mealPlanRepository.save(plan);
    }

    @Override
    public PlanEntry addEntry(String ownerUserId, Long mealPlanId, Long dayId, PlanEntry entry) {
        MealPlan plan = readMealPlan(ownerUserId, mealPlanId);
        PlanDay day = plan.getDays().stream()
                .filter(d -> d.getId().equals(dayId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Day with id " + dayId + " not found in meal plan."));

        entry.validate();
        enrichEntry(ownerUserId, entry);

        day.getEntries().add(entry);
        MealPlan saved = mealPlanRepository.save(plan);

        PlanDay savedDay = saved.getDays().stream()
                .filter(d -> d.getId().equals(dayId))
                .findFirst()
                .orElseThrow();
        return savedDay.getEntries().getLast();
    }

    @Override
    public void removeEntry(String ownerUserId, Long mealPlanId, Long dayId, Long entryId) {
        MealPlan plan = readMealPlan(ownerUserId, mealPlanId);
        PlanDay day = plan.getDays().stream()
                .filter(d -> d.getId().equals(dayId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Day with id " + dayId + " not found in meal plan."));

        boolean removed = day.getEntries().removeIf(e -> e.getId().equals(entryId));
        if (!removed) {
            throw new IllegalArgumentException("Entry with id " + entryId + " not found in day.");
        }
        mealPlanRepository.save(plan);
    }

    private void enrichEntry(String ownerUserId, PlanEntry entry) {
        if (entry.getEntryType() == EntryType.MEAL) {
            Meal meal = mealRepository.readById(ownerUserId, entry.getMealId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Meal with id " + entry.getMealId() + " was not found."));
            entry.setMealName(meal.getName());
            double totalCalories = sumIngredient(meal, MealIngredient::getCaloriesPerGram);
            double totalProtein = sumIngredient(meal, MealIngredient::getProteinPerGram);
            double totalFat = sumIngredient(meal, MealIngredient::getFatPerGram);
            double totalCarbs = sumIngredient(meal, MealIngredient::getCarbohydratesPerGram);
            double totalPrice = sumIngredient(meal, MealIngredient::getPricePerGram);
            int servings = meal.getServings() != null && meal.getServings() > 0 ? meal.getServings() : 1;
            double p = entry.getPortions() != null ? entry.getPortions() : 1.0;
            entry.setCalories(totalCalories / servings * p);
            entry.setProtein(totalProtein / servings * p);
            entry.setFat(totalFat / servings * p);
            entry.setCarbohydrates(totalCarbs / servings * p);
            entry.setPrice(totalPrice / servings * p);
            entry.setSodiumMg(sumIngredient(meal, MealIngredient::getSodiumMgPerGram) / servings * p);
            entry.setPotassiumMg(sumIngredient(meal, MealIngredient::getPotassiumMgPerGram) / servings * p);
            entry.setMagnesiumMg(sumIngredient(meal, MealIngredient::getMagnesiumMgPerGram) / servings * p);
            entry.setIronMg(sumIngredient(meal, MealIngredient::getIronMgPerGram) / servings * p);
            entry.setCalciumMg(sumIngredient(meal, MealIngredient::getCalciumMgPerGram) / servings * p);
            entry.setZincMg(sumIngredient(meal, MealIngredient::getZincMgPerGram) / servings * p);
            entry.setVitaminAMcg(sumIngredient(meal, MealIngredient::getVitaminAMcgPerGram) / servings * p);
            entry.setVitaminCMg(sumIngredient(meal, MealIngredient::getVitaminCMgPerGram) / servings * p);
            entry.setVitaminDMcg(sumIngredient(meal, MealIngredient::getVitaminDMcgPerGram) / servings * p);
            entry.setVitaminEMg(sumIngredient(meal, MealIngredient::getVitaminEMgPerGram) / servings * p);
            entry.setVitaminKMcg(sumIngredient(meal, MealIngredient::getVitaminKMcgPerGram) / servings * p);
            entry.setVitaminB1Mg(sumIngredient(meal, MealIngredient::getVitaminB1MgPerGram) / servings * p);
            entry.setVitaminB2Mg(sumIngredient(meal, MealIngredient::getVitaminB2MgPerGram) / servings * p);
            entry.setVitaminB6Mg(sumIngredient(meal, MealIngredient::getVitaminB6MgPerGram) / servings * p);
            entry.setVitaminB9Mcg(sumIngredient(meal, MealIngredient::getVitaminB9McgPerGram) / servings * p);
            entry.setVitaminB12Mcg(sumIngredient(meal, MealIngredient::getVitaminB12McgPerGram) / servings * p);
        } else {
            FoodProduct product = foodProductRepository.readById(ownerUserId, entry.getFoodProductId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Food product with id " + entry.getFoodProductId() + " was not found."));
            entry.setFoodProductName(product.getName());
            double ref = product.getGrams() != null && product.getGrams() > 0 ? product.getGrams() : 1.0;
            double g = entry.getGrams() != null ? entry.getGrams() : 0.0;
            entry.setCalories(product.getCalories() / ref * g);
            entry.setProtein(product.getProtein() / ref * g);
            entry.setFat(product.getFat() / ref * g);
            entry.setCarbohydrates(product.getCarbohydrates() / ref * g);
            entry.setPrice(product.getPrice() / ref * g);
            entry.setSodiumMg(multiplyNullable(product.getSodiumMg(), ref, g));
            entry.setPotassiumMg(multiplyNullable(product.getPotassiumMg(), ref, g));
            entry.setMagnesiumMg(multiplyNullable(product.getMagnesiumMg(), ref, g));
            entry.setIronMg(multiplyNullable(product.getIronMg(), ref, g));
            entry.setCalciumMg(multiplyNullable(product.getCalciumMg(), ref, g));
            entry.setZincMg(multiplyNullable(product.getZincMg(), ref, g));
            entry.setVitaminAMcg(multiplyNullable(product.getVitaminAMcg(), ref, g));
            entry.setVitaminCMg(multiplyNullable(product.getVitaminCMg(), ref, g));
            entry.setVitaminDMcg(multiplyNullable(product.getVitaminDMcg(), ref, g));
            entry.setVitaminEMg(multiplyNullable(product.getVitaminEMg(), ref, g));
            entry.setVitaminKMcg(multiplyNullable(product.getVitaminKMcg(), ref, g));
            entry.setVitaminB1Mg(multiplyNullable(product.getVitaminB1Mg(), ref, g));
            entry.setVitaminB2Mg(multiplyNullable(product.getVitaminB2Mg(), ref, g));
            entry.setVitaminB6Mg(multiplyNullable(product.getVitaminB6Mg(), ref, g));
            entry.setVitaminB9Mcg(multiplyNullable(product.getVitaminB9Mcg(), ref, g));
            entry.setVitaminB12Mcg(multiplyNullable(product.getVitaminB12Mcg(), ref, g));
        }
    }

    private Double multiplyNullable(Double value, double ref, double grams) {
        return value != null ? value / ref * grams : null;
    }

    @FunctionalInterface
    private interface PerGramExtractor {
        Double extract(MealIngredient ingredient);
    }

    private double sumIngredient(Meal meal, PerGramExtractor extractor) {
        if (meal.getIngredients() == null) return 0.0;
        return meal.getIngredients().stream()
                .mapToDouble(i -> {
                    Double perGram = extractor.extract(i);
                    double grams = i.getGrams() != null ? i.getGrams() : 0.0;
                    return perGram != null ? perGram * grams : 0.0;
                })
                .sum();
    }
}
