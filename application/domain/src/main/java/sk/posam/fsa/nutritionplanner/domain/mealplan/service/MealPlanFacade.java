package sk.posam.fsa.nutritionplanner.domain.mealplan.service;

import sk.posam.fsa.nutritionplanner.domain.mealplan.MealPlan;
import sk.posam.fsa.nutritionplanner.domain.mealplan.PlanEntry;

import java.util.List;

public interface MealPlanFacade {

    MealPlan createMealPlan(String ownerUserId, MealPlan mealPlan);

    List<MealPlan> readMealPlans(String ownerUserId);

    MealPlan readMealPlan(String ownerUserId, Long mealPlanId);

    MealPlan updateMealPlan(String ownerUserId, Long mealPlanId, MealPlan mealPlan);

    void deleteMealPlan(String ownerUserId, Long mealPlanId);

    MealPlan activateMealPlan(String ownerUserId, Long mealPlanId);

    MealPlan deactivateMealPlan(String ownerUserId, Long mealPlanId);

    MealPlan deductFridge(String ownerUserId, Long mealPlanId);

    PlanEntry addEntry(String ownerUserId, Long mealPlanId, Long dayId, PlanEntry entry);

    void removeEntry(String ownerUserId, Long mealPlanId, Long dayId, Long entryId);
}
