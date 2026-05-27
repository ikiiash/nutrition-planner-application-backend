package sk.posam.fsa.nutritionplanner.domain.shoppinglist.service;

import sk.posam.fsa.nutritionplanner.domain.shoppinglist.ShoppingListItem;

import java.util.List;

public interface ShoppingListFacade {
    ShoppingListItem addItem(String ownerUserId, ShoppingListItem item);
    List<ShoppingListItem> readAll(String ownerUserId);
    ShoppingListItem updateItem(String ownerUserId, Long itemId, ShoppingListItem item);
    void deleteItem(String ownerUserId, Long itemId);
    void clearAll(String ownerUserId);
}
