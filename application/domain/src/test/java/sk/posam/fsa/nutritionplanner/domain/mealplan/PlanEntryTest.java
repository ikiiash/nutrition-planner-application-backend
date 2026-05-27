package sk.posam.fsa.nutritionplanner.domain.mealplan;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlanEntryTest {

    @Test
    void validate_passes_for_valid_meal_entry() {
        assertDoesNotThrow(() -> validMealEntry().validate());
    }

    @Test
    void validate_passes_for_valid_food_product_entry() {
        assertDoesNotThrow(() -> validFoodProductEntry().validate());
    }

    @Test
    void validate_throws_when_mealType_is_null() {
        PlanEntry entry = validMealEntry();
        entry.setMealType(null);
        assertThrows(IllegalArgumentException.class, entry::validate);
    }

    @Test
    void validate_throws_when_entryType_is_null() {
        PlanEntry entry = validMealEntry();
        entry.setEntryType(null);
        assertThrows(IllegalArgumentException.class, entry::validate);
    }

    @Test
    void validate_throws_for_meal_entry_when_mealId_is_null() {
        PlanEntry entry = validMealEntry();
        entry.setMealId(null);
        assertThrows(IllegalArgumentException.class, entry::validate);
    }

    @Test
    void validate_throws_for_meal_entry_when_portions_is_null() {
        PlanEntry entry = validMealEntry();
        entry.setPortions(null);
        assertThrows(IllegalArgumentException.class, entry::validate);
    }

    @Test
    void validate_throws_for_meal_entry_when_portions_is_zero() {
        PlanEntry entry = validMealEntry();
        entry.setPortions(0.0);
        assertThrows(IllegalArgumentException.class, entry::validate);
    }

    @Test
    void validate_throws_for_meal_entry_when_portions_is_negative() {
        PlanEntry entry = validMealEntry();
        entry.setPortions(-1.0);
        assertThrows(IllegalArgumentException.class, entry::validate);
    }

    @Test
    void validate_throws_for_food_product_entry_when_foodProductId_is_null() {
        PlanEntry entry = validFoodProductEntry();
        entry.setFoodProductId(null);
        assertThrows(IllegalArgumentException.class, entry::validate);
    }

    @Test
    void validate_throws_for_food_product_entry_when_grams_is_null() {
        PlanEntry entry = validFoodProductEntry();
        entry.setGrams(null);
        assertThrows(IllegalArgumentException.class, entry::validate);
    }

    @Test
    void validate_throws_for_food_product_entry_when_grams_is_zero() {
        PlanEntry entry = validFoodProductEntry();
        entry.setGrams(0.0);
        assertThrows(IllegalArgumentException.class, entry::validate);
    }

    @Test
    void validate_throws_for_food_product_entry_when_grams_is_negative() {
        PlanEntry entry = validFoodProductEntry();
        entry.setGrams(-100.0);
        assertThrows(IllegalArgumentException.class, entry::validate);
    }

    @Test
    void equals_is_based_on_id() {
        PlanEntry a = new PlanEntry();
        a.setId(1L);
        PlanEntry b = new PlanEntry();
        b.setId(1L);
        assertEquals(a, b);
    }

    @Test
    void entries_with_different_ids_are_not_equal() {
        PlanEntry a = new PlanEntry();
        a.setId(1L);
        PlanEntry b = new PlanEntry();
        b.setId(2L);
        assertNotEquals(a, b);
    }

    private PlanEntry validMealEntry() {
        PlanEntry entry = new PlanEntry();
        entry.setMealType(MealType.BREAKFAST);
        entry.setEntryType(EntryType.MEAL);
        entry.setMealId(5L);
        entry.setPortions(1.0);
        return entry;
    }

    private PlanEntry validFoodProductEntry() {
        PlanEntry entry = new PlanEntry();
        entry.setMealType(MealType.LUNCH);
        entry.setEntryType(EntryType.FOOD_PRODUCT);
        entry.setFoodProductId(10L);
        entry.setGrams(150.0);
        return entry;
    }
}
