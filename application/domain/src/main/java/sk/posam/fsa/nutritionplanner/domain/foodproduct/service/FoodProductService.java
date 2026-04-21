package sk.posam.fsa.nutritionplanner.domain.foodproduct.service;

import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProduct;
import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProductNotFoundException;
import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProductRepository;

import java.util.List;

public class FoodProductService implements FoodProductFacade {

    private final FoodProductRepository foodProductRepository;

    public FoodProductService(FoodProductRepository foodProductRepository) {
        this.foodProductRepository = foodProductRepository;
    }

    @Override
    public FoodProduct createFoodProduct(FoodProduct foodProduct) {
        foodProduct.validate();
        return foodProductRepository.save(foodProduct);
    }

    @Override
    public List<FoodProduct> readFoodProducts(String name) {
        if (name == null || name.isBlank()) {
            return foodProductRepository.readAll();
        }
        return foodProductRepository.readByNameContaining(name.trim());
    }

    @Override
    public FoodProduct readFoodProduct(Long foodProductId) {
        return foodProductRepository.readById(foodProductId)
                .orElseThrow(() -> new FoodProductNotFoundException(foodProductId));
    }

    @Override
    public FoodProduct updateFoodProduct(Long foodProductId, FoodProduct foodProduct) {
        readFoodProduct(foodProductId);
        foodProduct.setId(foodProductId);
        foodProduct.validate();
        return foodProductRepository.save(foodProduct);
    }

    @Override
    public void deleteFoodProduct(Long foodProductId) {
        readFoodProduct(foodProductId);
        foodProductRepository.deleteById(foodProductId);
    }
}
