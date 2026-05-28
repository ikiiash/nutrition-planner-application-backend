package sk.posam.fsa.nutritionplanner.domain.foodproduct.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProduct;
import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProductNotFoundException;
import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProductRepository;
import sk.posam.fsa.nutritionplanner.domain.meal.MealRepository;
import sk.posam.fsa.nutritionplanner.domain.meal.service.MealFacade;
import sk.posam.fsa.nutritionplanner.domain.mealplan.MealPlanRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FoodProductServiceTest {

    @Mock
    FoodProductRepository foodProductRepository;

    @Mock
    MealRepository mealRepository;

    @Mock
    MealPlanRepository mealPlanRepository;

    @Mock
    MealFacade mealFacade;

    @InjectMocks
    FoodProductService sut;

    private static final String OWNER = "user-1";

    @Test
    void createFoodProduct_sets_owner_and_saves() {
        FoodProduct product = validProduct();
        when(foodProductRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        sut.createFoodProduct(OWNER, product);

        ArgumentCaptor<FoodProduct> captor = ArgumentCaptor.forClass(FoodProduct.class);
        verify(foodProductRepository).save(captor.capture());
        assertEquals(OWNER, captor.getValue().getOwnerUserId());
    }

    @Test
    void createFoodProduct_throws_when_name_is_blank() {
        FoodProduct product = validProduct();
        product.setName("  ");

        assertThrows(IllegalArgumentException.class, () -> sut.createFoodProduct(OWNER, product));
        verify(foodProductRepository, never()).save(any());
    }

    @Test
    void readFoodProducts_returns_all_when_name_is_null() {
        when(foodProductRepository.readAll(OWNER)).thenReturn(List.of(validProduct()));

        List<FoodProduct> result = sut.readFoodProducts(OWNER, null);

        assertEquals(1, result.size());
        verify(foodProductRepository).readAll(OWNER);
        verify(foodProductRepository, never()).readByNameContaining(any(), any());
    }

    @Test
    void readFoodProducts_returns_all_when_name_is_blank() {
        when(foodProductRepository.readAll(OWNER)).thenReturn(List.of(validProduct()));

        List<FoodProduct> result = sut.readFoodProducts(OWNER, "   ");

        assertEquals(1, result.size());
        verify(foodProductRepository).readAll(OWNER);
    }

    @Test
    void readFoodProducts_filters_by_name_when_not_blank() {
        when(foodProductRepository.readByNameContaining(OWNER, "chicken")).thenReturn(List.of(validProduct()));

        List<FoodProduct> result = sut.readFoodProducts(OWNER, "  chicken  ");

        assertEquals(1, result.size());
        verify(foodProductRepository).readByNameContaining(OWNER, "chicken");
    }

    @Test
    void readFoodProduct_returns_product_when_found() {
        FoodProduct product = validProduct();
        product.setId(1L);
        when(foodProductRepository.readById(OWNER, 1L)).thenReturn(Optional.of(product));

        FoodProduct result = sut.readFoodProduct(OWNER, 1L);

        assertEquals(product, result);
    }

    @Test
    void readFoodProduct_throws_when_not_found() {
        when(foodProductRepository.readById(OWNER, 99L)).thenReturn(Optional.empty());

        assertThrows(FoodProductNotFoundException.class, () -> sut.readFoodProduct(OWNER, 99L));
    }

    @Test
    void updateFoodProduct_validates_and_saves_with_correct_id_and_owner() {
        FoodProduct existing = validProduct();
        existing.setId(1L);
        when(foodProductRepository.readById(OWNER, 1L)).thenReturn(Optional.of(existing));
        when(foodProductRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        FoodProduct update = validProduct();
        update.setName("Updated");
        sut.updateFoodProduct(OWNER, 1L, update);

        ArgumentCaptor<FoodProduct> captor = ArgumentCaptor.forClass(FoodProduct.class);
        verify(foodProductRepository).save(captor.capture());
        assertEquals(1L, captor.getValue().getId());
        assertEquals(OWNER, captor.getValue().getOwnerUserId());
        assertEquals("Updated", captor.getValue().getName());
    }

    @Test
    void updateFoodProduct_throws_when_not_found() {
        when(foodProductRepository.readById(OWNER, 99L)).thenReturn(Optional.empty());

        assertThrows(FoodProductNotFoundException.class, () -> sut.updateFoodProduct(OWNER, 99L, validProduct()));
        verify(foodProductRepository, never()).save(any());
    }

    @Test
    void deleteFoodProduct_reads_then_deletes() {
        FoodProduct existing = validProduct();
        existing.setId(1L);
        when(foodProductRepository.readById(OWNER, 1L)).thenReturn(Optional.of(existing));

        sut.deleteFoodProduct(OWNER, 1L);

        verify(foodProductRepository).deleteById(OWNER, 1L);
    }

    @Test
    void deleteFoodProduct_throws_when_not_found() {
        when(foodProductRepository.readById(OWNER, 99L)).thenReturn(Optional.empty());

        assertThrows(FoodProductNotFoundException.class, () -> sut.deleteFoodProduct(OWNER, 99L));
        verify(foodProductRepository, never()).deleteById(any(), any());
    }

    @Test
    void setFridgeStatus_sets_in_fridge_true_and_saves_grams() {
        FoodProduct existing = validProduct();
        existing.setId(1L);
        when(foodProductRepository.readById(OWNER, 1L)).thenReturn(Optional.of(existing));
        when(foodProductRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        FoodProduct result = sut.setFridgeStatus(OWNER, 1L, true, 250.0);

        assertTrue(result.isInFridge());
        assertEquals(250.0, result.getFridgeGrams());
    }

    @Test
    void setFridgeStatus_sets_in_fridge_false_clears_fridgeGrams() {
        FoodProduct existing = validProduct();
        existing.setId(1L);
        existing.setInFridge(true);
        existing.setFridgeGrams(250.0);
        when(foodProductRepository.readById(OWNER, 1L)).thenReturn(Optional.of(existing));
        when(foodProductRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        FoodProduct result = sut.setFridgeStatus(OWNER, 1L, false, null);

        assertFalse(result.isInFridge());
        assertNull(result.getFridgeGrams());
    }

    @Test
    void setFridgeStatus_throws_when_product_not_found() {
        when(foodProductRepository.readById(OWNER, 99L)).thenReturn(Optional.empty());

        assertThrows(FoodProductNotFoundException.class, () -> sut.setFridgeStatus(OWNER, 99L, true, 100.0));
    }

    private FoodProduct validProduct() {
        FoodProduct p = new FoodProduct();
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
}
