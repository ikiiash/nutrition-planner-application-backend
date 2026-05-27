package sk.posam.fsa.nutritionplanner.domain.meal.service;

import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProduct;
import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProductRepository;
import sk.posam.fsa.nutritionplanner.domain.meal.Meal;
import sk.posam.fsa.nutritionplanner.domain.meal.MealIngredient;
import sk.posam.fsa.nutritionplanner.domain.meal.MealNotFoundException;
import sk.posam.fsa.nutritionplanner.domain.meal.MealRepository;

import java.util.List;

public class MealService implements MealFacade {

    private final MealRepository mealRepository;
    private final FoodProductRepository foodProductRepository;

    public MealService(MealRepository mealRepository, FoodProductRepository foodProductRepository) {
        this.mealRepository = mealRepository;
        this.foodProductRepository = foodProductRepository;
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
        readMeal(ownerUserId, mealId);
        meal.setId(mealId);
        meal.setOwnerUserId(ownerUserId);
        enrichIngredients(ownerUserId, meal);
        meal.validate();
        return mealRepository.save(meal);
    }

    @Override
    public void deleteMeal(String ownerUserId, Long mealId) {
        readMeal(ownerUserId, mealId);
        mealRepository.deleteById(ownerUserId, mealId);
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

    private Double divideNullable(Double value, double divisor) {
        return value != null ? value / divisor : null;
    }
}
