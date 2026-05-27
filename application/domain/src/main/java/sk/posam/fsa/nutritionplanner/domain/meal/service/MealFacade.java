package sk.posam.fsa.nutritionplanner.domain.meal.service;

import sk.posam.fsa.nutritionplanner.domain.meal.Meal;

import java.util.List;

public interface MealFacade {

    Meal createMeal(String ownerUserId, Meal meal);

    List<Meal> readMeals(String ownerUserId);

    Meal readMeal(String ownerUserId, Long mealId);

    Meal updateMeal(String ownerUserId, Long mealId, Meal meal);

    void deleteMeal(String ownerUserId, Long mealId);
}
