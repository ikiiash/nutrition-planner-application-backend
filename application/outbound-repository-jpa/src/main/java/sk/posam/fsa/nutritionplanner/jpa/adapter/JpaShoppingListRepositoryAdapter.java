package sk.posam.fsa.nutritionplanner.jpa.adapter;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sk.posam.fsa.nutritionplanner.domain.shoppinglist.ShoppingListItem;
import sk.posam.fsa.nutritionplanner.domain.shoppinglist.ShoppingListRepository;
import sk.posam.fsa.nutritionplanner.jpa.ShoppingListSpringDataRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaShoppingListRepositoryAdapter implements ShoppingListRepository {

    private final ShoppingListSpringDataRepository repo;

    public JpaShoppingListRepositoryAdapter(ShoppingListSpringDataRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional
    public ShoppingListItem save(ShoppingListItem item) {
        return repo.save(item);
    }

    @Override
    public List<ShoppingListItem> readAll(String ownerUserId) {
        return repo.findAllByOwnerUserId(ownerUserId);
    }

    @Override
    public Optional<ShoppingListItem> readById(String ownerUserId, Long itemId) {
        return repo.findByIdAndOwnerUserId(itemId, ownerUserId);
    }

    @Override
    @Transactional
    public void deleteById(String ownerUserId, Long itemId) {
        readById(ownerUserId, itemId).ifPresent(repo::delete);
    }

    @Override
    @Transactional
    public void deleteAll(String ownerUserId) {
        repo.deleteAllByOwnerUserId(ownerUserId);
    }
}
