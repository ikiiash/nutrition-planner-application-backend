package sk.posam.fsa.nutritionplanner.domain.meal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MealIngredientTest {

    @Test
    void validate_passes_for_valid_ingredient() {
        MealIngredient ingredient = validIngredient();
        assertDoesNotThrow(ingredient::validate);
    }

    @Test
    void validate_throws_when_foodProductId_is_null() {
        MealIngredient ingredient = validIngredient();
        ingredient.setFoodProductId(null);
        assertThrows(IllegalArgumentException.class, ingredient::validate);
    }

    @Test
    void validate_throws_when_grams_is_null() {
        MealIngredient ingredient = validIngredient();
        ingredient.setGrams(null);
        assertThrows(IllegalArgumentException.class, ingredient::validate);
    }

    @Test
    void validate_throws_when_grams_is_zero() {
        MealIngredient ingredient = validIngredient();
        ingredient.setGrams(0.0);
        assertThrows(IllegalArgumentException.class, ingredient::validate);
    }

    @Test
    void validate_throws_when_grams_is_negative() {
        MealIngredient ingredient = validIngredient();
        ingredient.setGrams(-50.0);
        assertThrows(IllegalArgumentException.class, ingredient::validate);
    }

    @Test
    void equals_is_based_on_id() {
        MealIngredient a = new MealIngredient();
        a.setId(1L);
        a.setFoodProductId(10L);

        MealIngredient b = new MealIngredient();
        b.setId(1L);
        b.setFoodProductId(99L);

        assertEquals(a, b);
    }

    @Test
    void ingredients_with_different_ids_are_not_equal() {
        MealIngredient a = new MealIngredient();
        a.setId(1L);

        MealIngredient b = new MealIngredient();
        b.setId(2L);

        assertNotEquals(a, b);
    }

    private MealIngredient validIngredient() {
        MealIngredient ingredient = new MealIngredient();
        ingredient.setFoodProductId(10L);
        ingredient.setGrams(150.0);
        return ingredient;
    }
}
