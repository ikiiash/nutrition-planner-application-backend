package sk.posam.fsa.nutritionplanner.domain.shoppinglist;

import java.util.List;
import java.util.Optional;

public interface ShoppingListRepository {
    ShoppingListItem save(ShoppingListItem item);
    List<ShoppingListItem> readAll(String ownerUserId);
    Optional<ShoppingListItem> readById(String ownerUserId, Long itemId);
    void deleteById(String ownerUserId, Long itemId);
    void deleteAll(String ownerUserId);
}
