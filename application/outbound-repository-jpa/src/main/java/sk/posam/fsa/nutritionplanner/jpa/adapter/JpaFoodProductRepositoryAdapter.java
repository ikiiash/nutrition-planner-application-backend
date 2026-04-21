package sk.posam.fsa.nutritionplanner.jpa.adapter;

import org.springframework.stereotype.Repository;
import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProduct;
import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProductRepository;
import sk.posam.fsa.nutritionplanner.jpa.FoodProductSpringDataRepository;

import java.util.List;
import java.util.Optional;

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

    @Override
    public List<FoodProduct> readAll() {
        return foodProductSpringDataRepository.findAll();
    }

    @Override
    public List<FoodProduct> readByNameContaining(String name) {
        return foodProductSpringDataRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public Optional<FoodProduct> readById(Long foodProductId) {
        return foodProductSpringDataRepository.findById(foodProductId);
    }

    @Override
    public void deleteById(Long foodProductId) {
        foodProductSpringDataRepository.deleteById(foodProductId);
    }
}
