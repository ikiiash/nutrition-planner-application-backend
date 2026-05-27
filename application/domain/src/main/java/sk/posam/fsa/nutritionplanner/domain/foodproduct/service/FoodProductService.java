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
    public FoodProduct createFoodProduct(String ownerUserId, FoodProduct foodProduct) {
        foodProduct.setOwnerUserId(ownerUserId);
        foodProduct.validate();
        return foodProductRepository.save(foodProduct);
    }

    @Override
    public List<FoodProduct> readFoodProducts(String ownerUserId, String name) {
        if (name == null || name.isBlank()) {
            return foodProductRepository.readAll(ownerUserId);
        }
        return foodProductRepository.readByNameContaining(ownerUserId, name.trim());
    }

    @Override
    public FoodProduct readFoodProduct(String ownerUserId, Long foodProductId) {
        return foodProductRepository.readById(ownerUserId, foodProductId)
                .orElseThrow(() -> new FoodProductNotFoundException(foodProductId));
    }

    @Override
    public FoodProduct updateFoodProduct(String ownerUserId, Long foodProductId, FoodProduct foodProduct) {
        readFoodProduct(ownerUserId, foodProductId);
        foodProduct.setId(foodProductId);
        foodProduct.setOwnerUserId(ownerUserId);
        foodProduct.validate();
        return foodProductRepository.save(foodProduct);
    }

    @Override
    public void deleteFoodProduct(String ownerUserId, Long foodProductId) {
        readFoodProduct(ownerUserId, foodProductId);
        foodProductRepository.deleteById(ownerUserId, foodProductId);
    }

    @Override
    public FoodProduct setFridgeStatus(String ownerUserId, Long foodProductId, boolean inFridge, Double fridgeGrams) {
        FoodProduct product = readFoodProduct(ownerUserId, foodProductId);
        product.setInFridge(inFridge);
        product.setFridgeGrams(inFridge ? fridgeGrams : null);
        return foodProductRepository.save(product);
    }
}
