package sk.posam.fsa.nutritionplanner.domain.foodproduct;

import java.util.List;
import java.util.Optional;

public interface FoodProductRepository {

    FoodProduct save(FoodProduct foodProduct);

    List<FoodProduct> readAll(String ownerUserId);

    List<FoodProduct> readByNameContaining(String ownerUserId, String name);

    Optional<FoodProduct> readById(String ownerUserId, Long foodProductId);

    void deleteById(String ownerUserId, Long foodProductId);

    List<FoodProduct> readAllInFridge(String ownerUserId);
}
