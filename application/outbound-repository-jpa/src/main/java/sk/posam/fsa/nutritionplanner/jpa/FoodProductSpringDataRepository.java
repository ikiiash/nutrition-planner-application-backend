package sk.posam.fsa.nutritionplanner.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProduct;

public interface FoodProductSpringDataRepository extends JpaRepository<FoodProduct, Long> {
}