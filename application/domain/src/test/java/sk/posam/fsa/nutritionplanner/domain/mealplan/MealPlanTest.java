package sk.posam.fsa.nutritionplanner.domain.mealplan;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MealPlanTest {

    @Test
    void validate_passes_for_valid_plan() {
        assertDoesNotThrow(() -> validPlan().validate());
    }

    @Test
    void validate_throws_when_owner_is_null() {
        MealPlan plan = validPlan();
        plan.setOwnerUserId(null);
        assertThrows(IllegalArgumentException.class, plan::validate);
    }

    @Test
    void validate_throws_when_owner_is_blank() {
        MealPlan plan = validPlan();
        plan.setOwnerUserId("  ");
        assertThrows(IllegalArgumentException.class, plan::validate);
    }

    @Test
    void validate_throws_when_name_is_null() {
        MealPlan plan = validPlan();
        plan.setName(null);
        assertThrows(IllegalArgumentException.class, plan::validate);
    }

    @Test
    void validate_throws_when_name_is_blank() {
        MealPlan plan = validPlan();
        plan.setName("");
        assertThrows(IllegalArgumentException.class, plan::validate);
    }

    @Test
    void validate_throws_when_startDate_is_null() {
        MealPlan plan = validPlan();
        plan.setStartDate(null);
        assertThrows(IllegalArgumentException.class, plan::validate);
    }

    @Test
    void validate_throws_when_numberOfDays_is_null() {
        MealPlan plan = validPlan();
        plan.setNumberOfDays(null);
        assertThrows(IllegalArgumentException.class, plan::validate);
    }

    @Test
    void validate_throws_when_numberOfDays_is_zero() {
        MealPlan plan = validPlan();
        plan.setNumberOfDays(0);
        assertThrows(IllegalArgumentException.class, plan::validate);
    }

    @Test
    void validate_throws_when_numberOfDays_exceeds_30() {
        MealPlan plan = validPlan();
        plan.setNumberOfDays(31);
        assertThrows(IllegalArgumentException.class, plan::validate);
    }

    @Test
    void validate_allows_boundary_values_1_and_30() {
        MealPlan plan1 = validPlan();
        plan1.setNumberOfDays(1);
        assertDoesNotThrow(plan1::validate);

        MealPlan plan30 = validPlan();
        plan30.setNumberOfDays(30);
        assertDoesNotThrow(plan30::validate);
    }

    @Test
    void plan_is_inactive_by_default() {
        assertFalse(new MealPlan().isActive());
    }

    @Test
    void equals_is_based_on_id_and_ownerUserId() {
        MealPlan a = new MealPlan();
        a.setId(1L);
        a.setOwnerUserId("user-1");

        MealPlan b = new MealPlan();
        b.setId(1L);
        b.setOwnerUserId("user-1");

        assertEquals(a, b);
    }

    @Test
    void plans_with_different_ids_are_not_equal() {
        MealPlan a = new MealPlan();
        a.setId(1L);
        a.setOwnerUserId("user-1");

        MealPlan b = new MealPlan();
        b.setId(2L);
        b.setOwnerUserId("user-1");

        assertNotEquals(a, b);
    }

    private MealPlan validPlan() {
        MealPlan plan = new MealPlan();
        plan.setOwnerUserId("user-1");
        plan.setName("Week Plan");
        plan.setStartDate(LocalDate.of(2026, 6, 1));
        plan.setNumberOfDays(7);
        return plan;
    }
}
