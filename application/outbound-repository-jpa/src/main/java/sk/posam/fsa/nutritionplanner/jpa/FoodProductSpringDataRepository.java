package sk.posam.fsa.nutritionplanner.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProduct;

import java.util.List;
import java.util.Optional;

public interface FoodProductSpringDataRepository extends JpaRepository<FoodProduct, Long> {

    List<FoodProduct> findAllByOwnerUserId(String ownerUserId);

    List<FoodProduct> findByOwnerUserIdAndNameContainingIgnoreCase(String ownerUserId, String name);

    Optional<FoodProduct> findByIdAndOwnerUserId(Long id, String ownerUserId);

    List<FoodProduct> findAllByOwnerUserIdAndInFridgeTrue(String ownerUserId);
}
