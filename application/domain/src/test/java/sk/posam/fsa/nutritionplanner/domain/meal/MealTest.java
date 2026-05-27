package sk.posam.fsa.nutritionplanner.domain.meal;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MealTest {

    @Test
    void validate_passes_for_valid_meal() {
        Meal meal = validMeal();
        assertDoesNotThrow(meal::validate);
    }

    @Test
    void validate_throws_when_owner_is_null() {
        Meal meal = validMeal();
        meal.setOwnerUserId(null);
        assertThrows(IllegalArgumentException.class, meal::validate);
    }

    @Test
    void validate_throws_when_owner_is_blank() {
        Meal meal = validMeal();
        meal.setOwnerUserId("   ");
        assertThrows(IllegalArgumentException.class, meal::validate);
    }

    @Test
    void validate_throws_when_name_is_null() {
        Meal meal = validMeal();
        meal.setName(null);
        assertThrows(IllegalArgumentException.class, meal::validate);
    }

    @Test
    void validate_throws_when_name_is_blank() {
        Meal meal = validMeal();
        meal.setName("");
        assertThrows(IllegalArgumentException.class, meal::validate);
    }

    @Test
    void validate_throws_when_servings_is_null() {
        Meal meal = validMeal();
        meal.setServings(null);
        assertThrows(IllegalArgumentException.class, meal::validate);
    }

    @Test
    void validate_throws_when_servings_is_zero() {
        Meal meal = validMeal();
        meal.setServings(0);
        assertThrows(IllegalArgumentException.class, meal::validate);
    }

    @Test
    void validate_throws_when_servings_is_negative() {
        Meal meal = validMeal();
        meal.setServings(-1);
        assertThrows(IllegalArgumentException.class, meal::validate);
    }

    @Test
    void validate_throws_when_ingredients_is_null() {
        Meal meal = validMeal();
        meal.setIngredients(null);
        assertThrows(IllegalArgumentException.class, meal::validate);
    }

    @Test
    void validate_throws_when_ingredients_is_empty() {
        Meal meal = validMeal();
        meal.setIngredients(List.of());
        assertThrows(IllegalArgumentException.class, meal::validate);
    }

    @Test
    void validate_throws_when_ingredient_is_invalid() {
        Meal meal = validMeal();
        MealIngredient badIngredient = new MealIngredient();
        badIngredient.setFoodProductId(null);
        badIngredient.setGrams(100.0);
        meal.setIngredients(List.of(badIngredient));
        assertThrows(IllegalArgumentException.class, meal::validate);
    }

    @Test
    void equals_is_based_on_id_and_ownerUserId() {
        Meal a = new Meal();
        a.setId(1L);
        a.setOwnerUserId("user-1");

        Meal b = new Meal();
        b.setId(1L);
        b.setOwnerUserId("user-1");

        assertEquals(a, b);
    }

    @Test
    void meals_with_different_ids_are_not_equal() {
        Meal a = new Meal();
        a.setId(1L);
        a.setOwnerUserId("user-1");

        Meal b = new Meal();
        b.setId(2L);
        b.setOwnerUserId("user-1");

        assertNotEquals(a, b);
    }

    private Meal validMeal() {
        MealIngredient ingredient = new MealIngredient();
        ingredient.setFoodProductId(10L);
        ingredient.setGrams(100.0);

        Meal meal = new Meal();
        meal.setOwnerUserId("user-1");
        meal.setName("Oatmeal");
        meal.setServings(2);
        meal.setIngredients(List.of(ingredient));
        return meal;
    }
}
