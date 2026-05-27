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
    public List<FoodProduct> readAll(String ownerUserId) {
        return foodProductSpringDataRepository.findAllByOwnerUserId(ownerUserId);
    }

    @Override
    public List<FoodProduct> readByNameContaining(String ownerUserId, String name) {
        return foodProductSpringDataRepository.findByOwnerUserIdAndNameContainingIgnoreCase(ownerUserId, name);
    }

    @Override
    public Optional<FoodProduct> readById(String ownerUserId, Long foodProductId) {
        return foodProductSpringDataRepository.findByIdAndOwnerUserId(foodProductId, ownerUserId);
    }

    @Override
    public void deleteById(String ownerUserId, Long foodProductId) {
        readById(ownerUserId, foodProductId).ifPresent(foodProductSpringDataRepository::delete);
    }

    @Override
    public List<FoodProduct> readAllInFridge(String ownerUserId) {
        return foodProductSpringDataRepository.findAllByOwnerUserIdAndInFridgeTrue(ownerUserId);
    }
}
