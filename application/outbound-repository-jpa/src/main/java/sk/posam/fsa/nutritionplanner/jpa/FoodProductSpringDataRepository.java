package sk.posam.fsa.nutritionplanner.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProduct;

import java.util.List;

public interface FoodProductSpringDataRepository extends JpaRepository<FoodProduct, Long> {

    List<FoodProduct> findByNameContainingIgnoreCase(String name);
}
