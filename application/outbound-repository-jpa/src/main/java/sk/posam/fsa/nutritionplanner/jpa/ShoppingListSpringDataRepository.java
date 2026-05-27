package sk.posam.fsa.nutritionplanner.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import sk.posam.fsa.nutritionplanner.domain.shoppinglist.ShoppingListItem;

import java.util.List;
import java.util.Optional;

public interface ShoppingListSpringDataRepository extends JpaRepository<ShoppingListItem, Long> {
    List<ShoppingListItem> findAllByOwnerUserId(String ownerUserId);
    Optional<ShoppingListItem> findByIdAndOwnerUserId(Long id, String ownerUserId);
    @Transactional
    void deleteAllByOwnerUserId(String ownerUserId);
}
