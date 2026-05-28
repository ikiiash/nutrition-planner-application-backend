package sk.posam.fsa.nutritionplanner.domain.meal.service;

import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProduct;
import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProductRepository;
import sk.posam.fsa.nutritionplanner.domain.meal.Meal;
import sk.posam.fsa.nutritionplanner.domain.meal.MealIngredient;
import sk.posam.fsa.nutritionplanner.domain.meal.MealNotFoundException;
import sk.posam.fsa.nutritionplanner.domain.meal.MealRepository;
import sk.posam.fsa.nutritionplanner.domain.mealplan.EntryType;
import sk.posam.fsa.nutritionplanner.domain.mealplan.MealPlan;
import sk.posam.fsa.nutritionplanner.domain.mealplan.MealPlanRepository;
import sk.posam.fsa.nutritionplanner.domain.mealplan.PlanDay;
import sk.posam.fsa.nutritionplanner.domain.mealplan.PlanEntry;

import java.util.List;

public class MealService implements MealFacade {

    private final MealRepository mealRepository;
    private final FoodProductRepository foodProductRepository;
    private final MealPlanRepository mealPlanRepository;

    public MealService(MealRepository mealRepository,
                       FoodProductRepository foodProductRepository,
                       MealPlanRepository mealPlanRepository) {
        this.mealRepository = mealRepository;
        this.foodProductRepository = foodProductRepository;
        this.mealPlanRepository = mealPlanRepository;
    }

    @Override
    public Meal createMeal(String ownerUserId, Meal meal) {
        meal.setOwnerUserId(ownerUserId);
        enrichIngredients(ownerUserId, meal);
        meal.validate();
        return mealRepository.save(meal);
    }

    @Override
    public List<Meal> readMeals(String ownerUserId) {
        return mealRepository.readAll(ownerUserId);
    }

    @Override
    public Meal readMeal(String ownerUserId, Long mealId) {
        return mealRepository.readById(ownerUserId, mealId)
                .orElseThrow(() -> new MealNotFoundException(mealId));
    }

    @Override
    public Meal updateMeal(String ownerUserId, Long mealId, Meal meal) {
        Meal existing = readMeal(ownerUserId, mealId);
        existing.setName(meal.getName());
        existing.setServings(meal.getServings());
        existing.setIngredients(meal.getIngredients());
        enrichIngredients(ownerUserId, existing);
        existing.validate();
        Meal saved = mealRepository.save(existing);
        cascadeToPlanEntries(ownerUserId, saved);
        return saved;
    }

    @Override
    public void deleteMeal(String ownerUserId, Long mealId) {
        readMeal(ownerUserId, mealId);
        mealRepository.deleteById(ownerUserId, mealId);
    }

    @Override
    public void recalculateFromFoodProduct(String ownerUserId, Long mealId) {
        Meal meal = readMeal(ownerUserId, mealId);
        enrichIngredients(ownerUserId, meal);
        Meal saved = mealRepository.save(meal);
        cascadeToPlanEntries(ownerUserId, saved);
    }

    private void enrichIngredients(String ownerUserId, Meal meal) {
        if (meal.getIngredients() == null) return;
        for (MealIngredient ingredient : meal.getIngredients()) {
            FoodProduct product = foodProductRepository.readById(ownerUserId, ingredient.getFoodProductId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Food product with id " + ingredient.getFoodProductId() + " was not found."));
            double ref = product.getGrams();
            ingredient.setFoodProductName(product.getName());
            ingredient.setCaloriesPerGram(product.getCalories() / ref);
            ingredient.setProteinPerGram(product.getProtein() / ref);
            ingredient.setFatPerGram(product.getFat() / ref);
            ingredient.setCarbohydratesPerGram(product.getCarbohydrates() / ref);
            ingredient.setPricePerGram(product.getPrice() / ref);
            ingredient.setSodiumMgPerGram(divideNullable(product.getSodiumMg(), ref));
            ingredient.setPotassiumMgPerGram(divideNullable(product.getPotassiumMg(), ref));
            ingredient.setMagnesiumMgPerGram(divideNullable(product.getMagnesiumMg(), ref));
            ingredient.setIronMgPerGram(divideNullable(product.getIronMg(), ref));
            ingredient.setCalciumMgPerGram(divideNullable(product.getCalciumMg(), ref));
            ingredient.setZincMgPerGram(divideNullable(product.getZincMg(), ref));
            ingredient.setVitaminAMcgPerGram(divideNullable(product.getVitaminAMcg(), ref));
            ingredient.setVitaminCMgPerGram(divideNullable(product.getVitaminCMg(), ref));
            ingredient.setVitaminDMcgPerGram(divideNullable(product.getVitaminDMcg(), ref));
            ingredient.setVitaminEMgPerGram(divideNullable(product.getVitaminEMg(), ref));
            ingredient.setVitaminKMcgPerGram(divideNullable(product.getVitaminKMcg(), ref));
            ingredient.setVitaminB1MgPerGram(divideNullable(product.getVitaminB1Mg(), ref));
            ingredient.setVitaminB2MgPerGram(divideNullable(product.getVitaminB2Mg(), ref));
            ingredient.setVitaminB6MgPerGram(divideNullable(product.getVitaminB6Mg(), ref));
            ingredient.setVitaminB9McgPerGram(divideNullable(product.getVitaminB9Mcg(), ref));
            ingredient.setVitaminB12McgPerGram(divideNullable(product.getVitaminB12Mcg(), ref));
        }
    }

    private void cascadeToPlanEntries(String ownerUserId, Meal updatedMeal) {
        List<MealPlan> affectedPlans = mealPlanRepository.readAllByMealId(ownerUserId, updatedMeal.getId());
        for (MealPlan plan : affectedPlans) {
            for (PlanDay day : plan.getDays()) {
                for (PlanEntry entry : day.getEntries()) {
                    if (EntryType.MEAL == entry.getEntryType()
                            && updatedMeal.getId().equals(entry.getMealId())) {
                        enrichMealEntry(entry, updatedMeal);
                    }
                }
            }
            mealPlanRepository.save(plan);
        }
    }

    private void enrichMealEntry(PlanEntry entry, Meal meal) {
        entry.setMealName(meal.getName());
        int servings = meal.getServings() != null && meal.getServings() > 0 ? meal.getServings() : 1;
        double p = entry.getPortions() != null ? entry.getPortions() : 1.0;
        entry.setCalories(sumIngredient(meal, MealIngredient::getCaloriesPerGram) / servings * p);
        entry.setProtein(sumIngredient(meal, MealIngredient::getProteinPerGram) / servings * p);
        entry.setFat(sumIngredient(meal, MealIngredient::getFatPerGram) / servings * p);
        entry.setCarbohydrates(sumIngredient(meal, MealIngredient::getCarbohydratesPerGram) / servings * p);
        entry.setPrice(sumIngredient(meal, MealIngredient::getPricePerGram) / servings * p);
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

    private Double divideNullable(Double value, double divisor) {
        return value != null ? value / divisor : null;
    }
}
