package sk.posam.fsa.nutritionplanner.domain.meal.service;

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
import sk.posam.fsa.nutritionplanner.domain.meal.MealNotFoundException;
import sk.posam.fsa.nutritionplanner.domain.meal.MealRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MealServiceTest {

    @Mock
    MealRepository mealRepository;

    @Mock
    FoodProductRepository foodProductRepository;

    @InjectMocks
    MealService sut;

    private static final String OWNER = "user-1";

    @Test
    void createMeal_sets_owner_and_saves() {
        Meal meal = mealWithIngredient();
        when(foodProductRepository.readById(eq(OWNER), eq(10L))).thenReturn(Optional.of(foodProduct()));
        when(mealRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        sut.createMeal(OWNER, meal);

        ArgumentCaptor<Meal> captor = ArgumentCaptor.forClass(Meal.class);
        verify(mealRepository).save(captor.capture());
        assertEquals(OWNER, captor.getValue().getOwnerUserId());
    }

    @Test
    void createMeal_enriches_ingredient_nutrition_from_food_product() {
        Meal meal = mealWithIngredient();
        FoodProduct product = foodProduct();
        when(foodProductRepository.readById(eq(OWNER), eq(10L))).thenReturn(Optional.of(product));
        when(mealRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        sut.createMeal(OWNER, meal);

        MealIngredient enriched = meal.getIngredients().get(0);
        assertEquals(product.getCalories() / product.getGrams(), enriched.getCaloriesPerGram(), 0.001);
        assertEquals(product.getProtein() / product.getGrams(), enriched.getProteinPerGram(), 0.001);
        assertEquals(product.getFat() / product.getGrams(), enriched.getFatPerGram(), 0.001);
        assertEquals(product.getCarbohydrates() / product.getGrams(), enriched.getCarbohydratesPerGram(), 0.001);
        assertEquals(product.getPrice() / product.getGrams(), enriched.getPricePerGram(), 0.001);
        assertEquals(product.getName(), enriched.getFoodProductName());
    }

    @Test
    void createMeal_throws_when_food_product_not_found() {
        Meal meal = mealWithIngredient();
        when(foodProductRepository.readById(any(), any())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> sut.createMeal(OWNER, meal));
        verify(mealRepository, never()).save(any());
    }

    @Test
    void readMeals_delegates_to_repository() {
        Meal meal = new Meal();
        when(mealRepository.readAll(OWNER)).thenReturn(List.of(meal));

        List<Meal> result = sut.readMeals(OWNER);

        assertEquals(1, result.size());
        verify(mealRepository).readAll(OWNER);
    }

    @Test
    void readMeal_returns_meal_when_found() {
        Meal meal = new Meal();
        meal.setId(1L);
        when(mealRepository.readById(OWNER, 1L)).thenReturn(Optional.of(meal));

        Meal result = sut.readMeal(OWNER, 1L);

        assertEquals(meal, result);
    }

    @Test
    void readMeal_throws_when_not_found() {
        when(mealRepository.readById(OWNER, 99L)).thenReturn(Optional.empty());

        assertThrows(MealNotFoundException.class, () -> sut.readMeal(OWNER, 99L));
    }

    @Test
    void updateMeal_reads_existing_then_saves_with_new_data() {
        Meal existing = new Meal();
        existing.setId(1L);
        existing.setOwnerUserId(OWNER);
        when(mealRepository.readById(OWNER, 1L)).thenReturn(Optional.of(existing));

        Meal update = mealWithIngredient();
        update.setName("Updated");
        when(foodProductRepository.readById(eq(OWNER), eq(10L))).thenReturn(Optional.of(foodProduct()));
        when(mealRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        sut.updateMeal(OWNER, 1L, update);

        ArgumentCaptor<Meal> captor = ArgumentCaptor.forClass(Meal.class);
        verify(mealRepository).save(captor.capture());
        assertEquals(1L, captor.getValue().getId());
        assertEquals(OWNER, captor.getValue().getOwnerUserId());
        assertEquals("Updated", captor.getValue().getName());
    }

    @Test
    void updateMeal_throws_when_meal_not_found() {
        when(mealRepository.readById(OWNER, 99L)).thenReturn(Optional.empty());

        assertThrows(MealNotFoundException.class, () -> sut.updateMeal(OWNER, 99L, mealWithIngredient()));
        verify(mealRepository, never()).save(any());
    }

    @Test
    void deleteMeal_reads_then_deletes() {
        Meal existing = new Meal();
        existing.setId(1L);
        when(mealRepository.readById(OWNER, 1L)).thenReturn(Optional.of(existing));

        sut.deleteMeal(OWNER, 1L);

        verify(mealRepository).deleteById(OWNER, 1L);
    }

    @Test
    void deleteMeal_throws_when_meal_not_found() {
        when(mealRepository.readById(OWNER, 99L)).thenReturn(Optional.empty());

        assertThrows(MealNotFoundException.class, () -> sut.deleteMeal(OWNER, 99L));
        verify(mealRepository, never()).deleteById(any(), any());
    }

    @Test
    void enrichIngredients_sets_null_per_gram_to_null_when_micronutrient_is_null() {
        Meal meal = mealWithIngredient();
        FoodProduct product = foodProduct();
        product.setSodiumMg(null);
        when(foodProductRepository.readById(eq(OWNER), eq(10L))).thenReturn(Optional.of(product));
        when(mealRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        sut.createMeal(OWNER, meal);

        assertNull(meal.getIngredients().get(0).getSodiumMgPerGram());
    }

    private Meal mealWithIngredient() {
        MealIngredient ingredient = new MealIngredient();
        ingredient.setFoodProductId(10L);
        ingredient.setGrams(100.0);

        Meal meal = new Meal();
        meal.setName("Oatmeal");
        meal.setServings(2);
        meal.setIngredients(List.of(ingredient));
        return meal;
    }

    private FoodProduct foodProduct() {
        FoodProduct p = new FoodProduct();
        p.setId(10L);
        p.setOwnerUserId(OWNER);
        p.setName("Oats");
        p.setCategory("Grains");
        p.setGrams(100.0);
        p.setCalories(350.0);
        p.setProtein(13.0);
        p.setFat(7.0);
        p.setCarbohydrates(60.0);
        p.setPrice(1.50);
        return p;
    }
}
