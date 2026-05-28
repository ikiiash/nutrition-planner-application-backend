package sk.posam.fsa.nutritionplanner.domain.foodproduct.service;

import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProduct;
import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProductNotFoundException;
import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProductRepository;
import sk.posam.fsa.nutritionplanner.domain.meal.Meal;
import sk.posam.fsa.nutritionplanner.domain.meal.MealRepository;
import sk.posam.fsa.nutritionplanner.domain.meal.service.MealFacade;
import sk.posam.fsa.nutritionplanner.domain.mealplan.EntryType;
import sk.posam.fsa.nutritionplanner.domain.mealplan.MealPlan;
import sk.posam.fsa.nutritionplanner.domain.mealplan.MealPlanRepository;
import sk.posam.fsa.nutritionplanner.domain.mealplan.PlanDay;
import sk.posam.fsa.nutritionplanner.domain.mealplan.PlanEntry;

import java.util.List;

public class FoodProductService implements FoodProductFacade {

    private final FoodProductRepository foodProductRepository;
    private final MealRepository mealRepository;
    private final MealPlanRepository mealPlanRepository;
    private final MealFacade mealFacade;

    public FoodProductService(FoodProductRepository foodProductRepository,
                              MealRepository mealRepository,
                              MealPlanRepository mealPlanRepository,
                              MealFacade mealFacade) {
        this.foodProductRepository = foodProductRepository;
        this.mealRepository = mealRepository;
        this.mealPlanRepository = mealPlanRepository;
        this.mealFacade = mealFacade;
    }

    @Override
    public FoodProduct createFoodProduct(String ownerUserId, FoodProduct foodProduct) {
        foodProduct.setOwnerUserId(ownerUserId);
        foodProduct.validate();
        return foodProductRepository.save(foodProduct);
    }

    @Override
    public List<FoodProduct> readFoodProducts(String ownerUserId, String name) {
        if (name == null || name.isBlank()) {
            return foodProductRepository.readAll(ownerUserId);
        }
        return foodProductRepository.readByNameContaining(ownerUserId, name.trim());
    }

    @Override
    public FoodProduct readFoodProduct(String ownerUserId, Long foodProductId) {
        return foodProductRepository.readById(ownerUserId, foodProductId)
                .orElseThrow(() -> new FoodProductNotFoundException(foodProductId));
    }

    @Override
    public FoodProduct updateFoodProduct(String ownerUserId, Long foodProductId, FoodProduct foodProduct) {
        readFoodProduct(ownerUserId, foodProductId);
        foodProduct.setId(foodProductId);
        foodProduct.setOwnerUserId(ownerUserId);
        foodProduct.validate();
        FoodProduct saved = foodProductRepository.save(foodProduct);
        cascadeToMealsAndEntries(ownerUserId, saved);
        return saved;
    }

    @Override
    public void deleteFoodProduct(String ownerUserId, Long foodProductId) {
        readFoodProduct(ownerUserId, foodProductId);
        foodProductRepository.deleteById(ownerUserId, foodProductId);
    }

    private void cascadeToMealsAndEntries(String ownerUserId, FoodProduct product) {
        // Re-enrich meals that contain this ingredient (MealService.updateMeal cascades further to plan entries)
        List<Meal> affectedMeals = mealRepository.readAllByFoodProductId(ownerUserId, product.getId());
        for (Meal meal : affectedMeals) {
            mealFacade.updateMeal(ownerUserId, meal.getId(), meal);
        }

        // Re-enrich FOOD_PRODUCT plan entries that directly reference this product
        List<MealPlan> affectedPlans = mealPlanRepository.readAllByFoodProductId(ownerUserId, product.getId());
        for (MealPlan plan : affectedPlans) {
            double ref = product.getGrams() != null && product.getGrams() > 0 ? product.getGrams() : 1.0;
            for (PlanDay day : plan.getDays()) {
                for (PlanEntry entry : day.getEntries()) {
                    if (EntryType.FOOD_PRODUCT == entry.getEntryType()
                            && product.getId().equals(entry.getFoodProductId())) {
                        double g = entry.getGrams() != null ? entry.getGrams() : 0.0;
                        entry.setFoodProductName(product.getName());
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
            }
            mealPlanRepository.save(plan);
        }
    }

    private Double multiplyNullable(Double value, double ref, double grams) {
        return value != null ? value / ref * grams : null;
    }

    @Override
    public FoodProduct setFridgeStatus(String ownerUserId, Long foodProductId, boolean inFridge, Double fridgeGrams) {
        FoodProduct product = readFoodProduct(ownerUserId, foodProductId);
        product.setInFridge(inFridge);
        product.setFridgeGrams(inFridge ? fridgeGrams : null);
        return foodProductRepository.save(product);
    }
}
