package sk.posam.fsa.nutritionplanner.domain.meal;

public class MealNotFoundException extends RuntimeException {

    public MealNotFoundException(Long mealId) {
        super("Meal with id " + mealId + " was not found.");
    }
}
