package sk.posam.fsa.nutritionplanner.domain.foodproduct;

import java.util.List;
import java.util.Optional;

public interface FoodProductRepository {

    FoodProduct save(FoodProduct foodProduct);

    List<FoodProduct> readAll();

    List<FoodProduct> readByNameContaining(String name);

    Optional<FoodProduct> readById(Long foodProductId);

    void deleteById(Long foodProductId);
}
