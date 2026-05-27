package sk.posam.fsa.nutritionplanner.domain.foodproduct;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FoodProductTest {

    @Test
    void validate_passes_for_valid_product() {
        assertDoesNotThrow(() -> validProduct().validate());
    }

    @Test
    void validate_throws_when_owner_is_null() {
        FoodProduct p = validProduct();
        p.setOwnerUserId(null);
        assertThrows(IllegalArgumentException.class, p::validate);
    }

    @Test
    void validate_throws_when_owner_is_blank() {
        FoodProduct p = validProduct();
        p.setOwnerUserId("  ");
        assertThrows(IllegalArgumentException.class, p::validate);
    }

    @Test
    void validate_throws_when_name_is_null() {
        FoodProduct p = validProduct();
        p.setName(null);
        assertThrows(IllegalArgumentException.class, p::validate);
    }

    @Test
    void validate_throws_when_name_is_blank() {
        FoodProduct p = validProduct();
        p.setName("");
        assertThrows(IllegalArgumentException.class, p::validate);
    }

    @Test
    void validate_throws_when_category_is_null() {
        FoodProduct p = validProduct();
        p.setCategory(null);
        assertThrows(IllegalArgumentException.class, p::validate);
    }

    @Test
    void validate_throws_when_grams_is_zero() {
        FoodProduct p = validProduct();
        p.setGrams(0.0);
        assertThrows(IllegalArgumentException.class, p::validate);
    }

    @Test
    void validate_throws_when_grams_is_negative() {
        FoodProduct p = validProduct();
        p.setGrams(-1.0);
        assertThrows(IllegalArgumentException.class, p::validate);
    }

    @Test
    void validate_throws_when_grams_is_null() {
        FoodProduct p = validProduct();
        p.setGrams(null);
        assertThrows(IllegalArgumentException.class, p::validate);
    }

    @Test
    void validate_throws_when_calories_is_null() {
        FoodProduct p = validProduct();
        p.setCalories(null);
        assertThrows(IllegalArgumentException.class, p::validate);
    }

    @Test
    void validate_throws_when_calories_is_negative() {
        FoodProduct p = validProduct();
        p.setCalories(-1.0);
        assertThrows(IllegalArgumentException.class, p::validate);
    }

    @Test
    void validate_allows_zero_calories() {
        FoodProduct p = validProduct();
        p.setCalories(0.0);
        assertDoesNotThrow(p::validate);
    }

    @Test
    void validate_throws_when_protein_is_negative() {
        FoodProduct p = validProduct();
        p.setProtein(-0.1);
        assertThrows(IllegalArgumentException.class, p::validate);
    }

    @Test
    void validate_throws_when_fat_is_negative() {
        FoodProduct p = validProduct();
        p.setFat(-0.1);
        assertThrows(IllegalArgumentException.class, p::validate);
    }

    @Test
    void validate_throws_when_carbohydrates_is_negative() {
        FoodProduct p = validProduct();
        p.setCarbohydrates(-0.1);
        assertThrows(IllegalArgumentException.class, p::validate);
    }

    @Test
    void validate_throws_when_price_is_negative() {
        FoodProduct p = validProduct();
        p.setPrice(-0.1);
        assertThrows(IllegalArgumentException.class, p::validate);
    }

    @Test
    void validate_allows_zero_price() {
        FoodProduct p = validProduct();
        p.setPrice(0.0);
        assertDoesNotThrow(p::validate);
    }

    @Test
    void equals_is_based_on_all_core_fields() {
        FoodProduct a = validProduct();
        a.setId(1L);
        FoodProduct b = validProduct();
        b.setId(1L);
        assertEquals(a, b);
    }

    @Test
    void products_with_different_ids_are_not_equal() {
        FoodProduct a = validProduct();
        a.setId(1L);
        FoodProduct b = validProduct();
        b.setId(2L);
        assertNotEquals(a, b);
    }

    private FoodProduct validProduct() {
        FoodProduct p = new FoodProduct();
        p.setOwnerUserId("user-1");
        p.setName("Chicken Breast");
        p.setCategory("Meat");
        p.setGrams(100.0);
        p.setCalories(165.0);
        p.setProtein(31.0);
        p.setFat(3.6);
        p.setCarbohydrates(0.0);
        p.setPrice(2.50);
        return p;
    }
}
