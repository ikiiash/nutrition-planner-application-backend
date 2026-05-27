package sk.posam.fsa.nutritionplanner.domain.mealplan;

public class MealPlanNotFoundException extends RuntimeException {

    public MealPlanNotFoundException(Long mealPlanId) {
        super("Meal plan with id " + mealPlanId + " was not found.");
    }
}
