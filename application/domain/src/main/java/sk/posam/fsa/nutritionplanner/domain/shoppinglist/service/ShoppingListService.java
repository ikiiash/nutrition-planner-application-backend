package sk.posam.fsa.nutritionplanner.domain.shoppinglist.service;

import sk.posam.fsa.nutritionplanner.domain.shoppinglist.ShoppingListItem;
import sk.posam.fsa.nutritionplanner.domain.shoppinglist.ShoppingListRepository;

import java.util.List;

public class ShoppingListService implements ShoppingListFacade {

    private final ShoppingListRepository repository;

    public ShoppingListService(ShoppingListRepository repository) {
        this.repository = repository;
    }

    @Override
    public ShoppingListItem addItem(String ownerUserId, ShoppingListItem item) {
        item.setOwnerUserId(ownerUserId);
        return repository.save(item);
    }

    @Override
    public List<ShoppingListItem> readAll(String ownerUserId) {
        return repository.readAll(ownerUserId);
    }

    @Override
    public ShoppingListItem updateItem(String ownerUserId, Long itemId, ShoppingListItem item) {
        ShoppingListItem existing = repository.readById(ownerUserId, itemId)
                .orElseThrow(() -> new RuntimeException("Shopping list item not found: " + itemId));
        existing.setGrams(item.getGrams());
        existing.setFoodProductName(item.getFoodProductName());
        existing.setCaloriesPer100g(item.getCaloriesPer100g());
        existing.setProteinPer100g(item.getProteinPer100g());
        existing.setFatPer100g(item.getFatPer100g());
        existing.setCarbsPer100g(item.getCarbsPer100g());
        existing.setPricePer100g(item.getPricePer100g());
        return repository.save(existing);
    }

    @Override
    public void deleteItem(String ownerUserId, Long itemId) {
        repository.deleteById(ownerUserId, itemId);
    }

    @Override
    public void clearAll(String ownerUserId) {
        repository.deleteAll(ownerUserId);
    }
}
