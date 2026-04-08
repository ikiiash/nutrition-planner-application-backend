package sk.posam.fsa.nutritionplanner.jpa.adapter;

import org.springframework.stereotype.Repository;
import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProduct;
import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProductRepository;
import sk.posam.fsa.nutritionplanner.jpa.FoodProductSpringDataRepository;

@Repository
public class JpaFoodProductRepositoryAdapter implements FoodProductRepository {

    private final FoodProductSpringDataRepository foodProductSpringDataRepository;

    public JpaFoodProductRepositoryAdapter(FoodProductSpringDataRepository foodProductSpringDataRepository) {
        this.foodProductSpringDataRepository = foodProductSpringDataRepository;
    }

    @Override
    public FoodProduct save(FoodProduct foodProduct) {
        return foodProductSpringDataRepository.save(foodProduct);
    }
}