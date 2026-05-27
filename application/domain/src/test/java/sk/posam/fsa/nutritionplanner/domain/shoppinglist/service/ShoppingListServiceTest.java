package sk.posam.fsa.nutritionplanner.domain.shoppinglist.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sk.posam.fsa.nutritionplanner.domain.shoppinglist.ShoppingListItem;
import sk.posam.fsa.nutritionplanner.domain.shoppinglist.ShoppingListRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShoppingListServiceTest {

    @Mock
    ShoppingListRepository repository;

    @InjectMocks
    ShoppingListService sut;

    private static final String OWNER = "user-1";

    @Test
    void addItem_sets_owner_and_saves() {
        ShoppingListItem item = newItem("Milk", 1000.0);
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        sut.addItem(OWNER, item);

        ArgumentCaptor<ShoppingListItem> captor = ArgumentCaptor.forClass(ShoppingListItem.class);
        verify(repository).save(captor.capture());
        assertEquals(OWNER, captor.getValue().getOwnerUserId());
    }

    @Test
    void addItem_returns_saved_item() {
        ShoppingListItem item = newItem("Eggs", 500.0);
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ShoppingListItem result = sut.addItem(OWNER, item);

        assertNotNull(result);
        assertEquals(OWNER, result.getOwnerUserId());
    }

    @Test
    void readAll_delegates_to_repository() {
        ShoppingListItem item = newItem("Bread", 300.0);
        when(repository.readAll(OWNER)).thenReturn(List.of(item));

        List<ShoppingListItem> result = sut.readAll(OWNER);

        assertEquals(1, result.size());
        verify(repository).readAll(OWNER);
    }

    @Test
    void updateItem_updates_fields_and_saves() {
        ShoppingListItem existing = newItem("Old Name", 200.0);
        existing.setId(1L);
        when(repository.readById(OWNER, 1L)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ShoppingListItem update = newItem("New Name", 500.0);
        update.setCaloriesPer100g(200.0);
        update.setProteinPer100g(10.0);
        update.setFatPer100g(5.0);
        update.setCarbsPer100g(30.0);
        update.setPricePer100g(1.5);

        sut.updateItem(OWNER, 1L, update);

        ArgumentCaptor<ShoppingListItem> captor = ArgumentCaptor.forClass(ShoppingListItem.class);
        verify(repository).save(captor.capture());
        ShoppingListItem saved = captor.getValue();
        assertEquals("New Name", saved.getFoodProductName());
        assertEquals(500.0, saved.getGrams());
        assertEquals(200.0, saved.getCaloriesPer100g());
        assertEquals(10.0, saved.getProteinPer100g());
        assertEquals(5.0, saved.getFatPer100g());
        assertEquals(30.0, saved.getCarbsPer100g());
        assertEquals(1.5, saved.getPricePer100g());
    }

    @Test
    void updateItem_throws_when_not_found() {
        when(repository.readById(OWNER, 99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> sut.updateItem(OWNER, 99L, newItem("X", 100.0)));
        verify(repository, never()).save(any());
    }

    @Test
    void deleteItem_delegates_to_repository() {
        sut.deleteItem(OWNER, 1L);

        verify(repository).deleteById(OWNER, 1L);
    }

    @Test
    void clearAll_delegates_to_repository() {
        sut.clearAll(OWNER);

        verify(repository).deleteAll(OWNER);
    }

    private ShoppingListItem newItem(String name, double grams) {
        ShoppingListItem item = new ShoppingListItem();
        item.setFoodProductName(name);
        item.setGrams(grams);
        return item;
    }
}
