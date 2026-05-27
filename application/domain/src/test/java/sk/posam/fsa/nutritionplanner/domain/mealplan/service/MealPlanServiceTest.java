package sk.posam.fsa.nutritionplanner.domain.mealplan.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProduct;
import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProductRepository;
import sk.posam.fsa.nutritionplanner.domain.meal.Meal;
import sk.posam.fsa.nutritionplanner.domain.meal.MealIngredient;
import sk.posam.fsa.nutritionplanner.domain.meal.MealRepository;
import sk.posam.fsa.nutritionplanner.domain.mealplan.EntryType;
import sk.posam.fsa.nutritionplanner.domain.mealplan.MealPlan;
import sk.posam.fsa.nutritionplanner.domain.mealplan.MealPlanNotFoundException;
import sk.posam.fsa.nutritionplanner.domain.mealplan.MealPlanRepository;
import sk.posam.fsa.nutritionplanner.domain.mealplan.MealType;
import sk.posam.fsa.nutritionplanner.domain.mealplan.PlanDay;
import sk.posam.fsa.nutritionplanner.domain.mealplan.PlanEntry;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MealPlanServiceTest {

    @Mock
    MealPlanRepository mealPlanRepository;

    @Mock
    MealRepository mealRepository;

    @Mock
    FoodProductRepository foodProductRepository;

    @InjectMocks
    MealPlanService sut;

    private static final String OWNER = "user-1";

    @Test
    void createMealPlan_sets_owner_generates_days_and_saves() {
        MealPlan plan = validPlan();
        plan.setNumberOfDays(3);
        when(mealPlanRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        sut.createMealPlan(OWNER, plan);

        ArgumentCaptor<MealPlan> captor = ArgumentCaptor.forClass(MealPlan.class);
        verify(mealPlanRepository).save(captor.capture());
        MealPlan saved = captor.getValue();
        assertEquals(OWNER, saved.getOwnerUserId());
        assertEquals(3, saved.getDays().size());
        assertEquals(1, saved.getDays().get(0).getDayNumber());
        assertEquals(3, saved.getDays().get(2).getDayNumber());
    }

    @Test
    void createMealPlan_throws_for_invalid_plan() {
        MealPlan plan = new MealPlan();
        plan.setName("Plan");

        assertThrows(IllegalArgumentException.class, () -> sut.createMealPlan(OWNER, plan));
        verify(mealPlanRepository, never()).save(any());
    }

    @Test
    void readMealPlans_delegates_to_repository() {
        when(mealPlanRepository.readAll(OWNER)).thenReturn(List.of(validPlan()));

        List<MealPlan> result = sut.readMealPlans(OWNER);

        assertEquals(1, result.size());
    }

    @Test
    void readMealPlan_returns_plan_when_found() {
        MealPlan plan = validPlan();
        plan.setId(1L);
        when(mealPlanRepository.readById(OWNER, 1L)).thenReturn(Optional.of(plan));

        MealPlan result = sut.readMealPlan(OWNER, 1L);

        assertEquals(plan, result);
    }

    @Test
    void readMealPlan_throws_when_not_found() {
        when(mealPlanRepository.readById(OWNER, 99L)).thenReturn(Optional.empty());

        assertThrows(MealPlanNotFoundException.class, () -> sut.readMealPlan(OWNER, 99L));
    }

    @Test
    void updateMealPlan_adds_new_days_when_numberOfDays_increases() {
        MealPlan existing = validPlan();
        existing.setId(1L);
        existing.setNumberOfDays(3);
        PlanDay day1 = new PlanDay(1);
        PlanDay day2 = new PlanDay(2);
        PlanDay day3 = new PlanDay(3);
        existing.setDays(new ArrayList<>(List.of(day1, day2, day3)));
        when(mealPlanRepository.readById(OWNER, 1L)).thenReturn(Optional.of(existing));
        when(mealPlanRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        MealPlan update = validPlan();
        update.setNumberOfDays(5);
        sut.updateMealPlan(OWNER, 1L, update);

        ArgumentCaptor<MealPlan> captor = ArgumentCaptor.forClass(MealPlan.class);
        verify(mealPlanRepository).save(captor.capture());
        assertEquals(5, captor.getValue().getDays().size());
    }

    @Test
    void updateMealPlan_removes_extra_days_when_numberOfDays_decreases() {
        MealPlan existing = validPlan();
        existing.setId(1L);
        existing.setNumberOfDays(5);
        List<PlanDay> days = new ArrayList<>();
        for (int i = 1; i <= 5; i++) days.add(new PlanDay(i));
        existing.setDays(days);
        when(mealPlanRepository.readById(OWNER, 1L)).thenReturn(Optional.of(existing));
        when(mealPlanRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        MealPlan update = validPlan();
        update.setNumberOfDays(3);
        sut.updateMealPlan(OWNER, 1L, update);

        ArgumentCaptor<MealPlan> captor = ArgumentCaptor.forClass(MealPlan.class);
        verify(mealPlanRepository).save(captor.capture());
        assertEquals(3, captor.getValue().getDays().size());
    }

    @Test
    void deleteMealPlan_reads_then_deletes() {
        MealPlan plan = validPlan();
        plan.setId(1L);
        when(mealPlanRepository.readById(OWNER, 1L)).thenReturn(Optional.of(plan));

        sut.deleteMealPlan(OWNER, 1L);

        verify(mealPlanRepository).deleteById(OWNER, 1L);
    }

    @Test
    void deleteMealPlan_throws_when_not_found() {
        when(mealPlanRepository.readById(OWNER, 99L)).thenReturn(Optional.empty());

        assertThrows(MealPlanNotFoundException.class, () -> sut.deleteMealPlan(OWNER, 99L));
        verify(mealPlanRepository, never()).deleteById(any(), any());
    }

    @Test
    void activateMealPlan_deactivates_all_then_activates_target() {
        MealPlan plan = validPlan();
        plan.setId(1L);
        when(mealPlanRepository.readById(OWNER, 1L)).thenReturn(Optional.of(plan));
        when(mealPlanRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        sut.activateMealPlan(OWNER, 1L);

        verify(mealPlanRepository).deactivateAll(OWNER);
        ArgumentCaptor<MealPlan> captor = ArgumentCaptor.forClass(MealPlan.class);
        verify(mealPlanRepository).save(captor.capture());
        assertTrue(captor.getValue().isActive());
        assertNotNull(captor.getValue().getActivatedAt());
    }

    @Test
    void deactivateMealPlan_clears_active_and_activatedAt() {
        MealPlan plan = validPlan();
        plan.setId(1L);
        plan.setActive(true);
        plan.setActivatedAt(LocalDate.now());
        when(mealPlanRepository.readById(OWNER, 1L)).thenReturn(Optional.of(plan));
        when(mealPlanRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        sut.deactivateMealPlan(OWNER, 1L);

        ArgumentCaptor<MealPlan> captor = ArgumentCaptor.forClass(MealPlan.class);
        verify(mealPlanRepository).save(captor.capture());
        assertFalse(captor.getValue().isActive());
        assertNull(captor.getValue().getActivatedAt());
    }

    @Test
    void addEntry_enriches_food_product_entry_and_saves() {
        MealPlan plan = planWithDay(10L);
        when(mealPlanRepository.readById(OWNER, 1L)).thenReturn(Optional.of(plan));

        FoodProduct product = foodProduct();
        when(foodProductRepository.readById(OWNER, 5L)).thenReturn(Optional.of(product));

        PlanEntry entryToAdd = new PlanEntry();
        entryToAdd.setMealType(MealType.LUNCH);
        entryToAdd.setEntryType(EntryType.FOOD_PRODUCT);
        entryToAdd.setFoodProductId(5L);
        entryToAdd.setGrams(200.0);

        PlanEntry saved = new PlanEntry();
        saved.setId(99L);
        saved.setMealType(MealType.LUNCH);
        saved.setEntryType(EntryType.FOOD_PRODUCT);
        saved.setFoodProductId(5L);
        saved.setGrams(200.0);

        when(mealPlanRepository.save(any())).thenAnswer(inv -> {
            MealPlan p = inv.getArgument(0);
            p.getDays().get(0).getEntries().get(0).setId(99L);
            return p;
        });

        PlanEntry result = sut.addEntry(OWNER, 1L, 10L, entryToAdd);

        assertNotNull(result);
        verify(mealPlanRepository).save(any());
        assertEquals("Chicken Breast", entryToAdd.getFoodProductName());
        assertEquals(product.getCalories() / product.getGrams() * 200.0, entryToAdd.getCalories(), 0.001);
    }

    @Test
    void addEntry_enriches_meal_entry_and_saves() {
        MealPlan plan = planWithDay(10L);
        when(mealPlanRepository.readById(OWNER, 1L)).thenReturn(Optional.of(plan));

        Meal meal = mealWithIngredients();
        when(mealRepository.readById(OWNER, 5L)).thenReturn(Optional.of(meal));

        PlanEntry entryToAdd = new PlanEntry();
        entryToAdd.setMealType(MealType.BREAKFAST);
        entryToAdd.setEntryType(EntryType.MEAL);
        entryToAdd.setMealId(5L);
        entryToAdd.setPortions(2.0);

        when(mealPlanRepository.save(any())).thenAnswer(inv -> {
            MealPlan p = inv.getArgument(0);
            p.getDays().get(0).getEntries().get(0).setId(88L);
            return p;
        });

        PlanEntry result = sut.addEntry(OWNER, 1L, 10L, entryToAdd);

        assertNotNull(result);
        assertEquals("Oatmeal", entryToAdd.getMealName());
    }

    @Test
    void addEntry_throws_when_day_not_found() {
        MealPlan plan = planWithDay(10L);
        when(mealPlanRepository.readById(OWNER, 1L)).thenReturn(Optional.of(plan));

        PlanEntry entry = new PlanEntry();
        entry.setMealType(MealType.DINNER);
        entry.setEntryType(EntryType.FOOD_PRODUCT);
        entry.setFoodProductId(5L);
        entry.setGrams(100.0);

        assertThrows(IllegalArgumentException.class, () -> sut.addEntry(OWNER, 1L, 999L, entry));
    }

    @Test
    void removeEntry_removes_entry_from_day() {
        MealPlan plan = planWithDay(10L);
        PlanEntry entry = new PlanEntry();
        entry.setId(20L);
        plan.getDays().get(0).getEntries().add(entry);
        when(mealPlanRepository.readById(OWNER, 1L)).thenReturn(Optional.of(plan));
        when(mealPlanRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        sut.removeEntry(OWNER, 1L, 10L, 20L);

        verify(mealPlanRepository).save(any());
        assertTrue(plan.getDays().get(0).getEntries().isEmpty());
    }

    @Test
    void removeEntry_throws_when_entry_not_found() {
        MealPlan plan = planWithDay(10L);
        when(mealPlanRepository.readById(OWNER, 1L)).thenReturn(Optional.of(plan));

        assertThrows(IllegalArgumentException.class, () -> sut.removeEntry(OWNER, 1L, 10L, 999L));
    }

    private MealPlan validPlan() {
        MealPlan plan = new MealPlan();
        plan.setOwnerUserId(OWNER);
        plan.setName("Week Plan");
        plan.setStartDate(LocalDate.of(2026, 6, 1));
        plan.setNumberOfDays(7);
        return plan;
    }

    private MealPlan planWithDay(Long dayId) {
        MealPlan plan = new MealPlan();
        plan.setId(1L);
        plan.setOwnerUserId(OWNER);
        plan.setName("Plan");
        plan.setStartDate(LocalDate.now());
        plan.setNumberOfDays(1);

        PlanDay day = new PlanDay(1);
        day.setId(dayId);
        plan.setDays(new ArrayList<>(List.of(day)));
        return plan;
    }

    private FoodProduct foodProduct() {
        FoodProduct p = new FoodProduct();
        p.setId(5L);
        p.setOwnerUserId(OWNER);
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

    private Meal mealWithIngredients() {
        MealIngredient ingredient = new MealIngredient();
        ingredient.setFoodProductId(10L);
        ingredient.setGrams(100.0);
        ingredient.setCaloriesPerGram(3.5);
        ingredient.setProteinPerGram(0.13);
        ingredient.setFatPerGram(0.07);
        ingredient.setCarbohydratesPerGram(0.6);
        ingredient.setPricePerGram(0.015);

        Meal meal = new Meal();
        meal.setId(5L);
        meal.setOwnerUserId(OWNER);
        meal.setName("Oatmeal");
        meal.setServings(2);
        meal.setIngredients(List.of(ingredient));
        return meal;
    }
}
