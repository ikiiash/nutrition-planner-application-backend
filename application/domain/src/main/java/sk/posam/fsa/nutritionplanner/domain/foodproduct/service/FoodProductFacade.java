package sk.posam.fsa.nutritionplanner.domain.foodproduct.service;

import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProduct;

import java.util.List;

public interface FoodProductFacade {

    FoodProduct createFoodProduct(String ownerUserId, FoodProduct foodProduct);

    List<FoodProduct> readFoodProducts(String ownerUserId, String name);

    FoodProduct readFoodProduct(String ownerUserId, Long foodProductId);

    FoodProduct updateFoodProduct(String ownerUserId, Long foodProductId, FoodProduct foodProduct);

    void deleteFoodProduct(String ownerUserId, Long foodProductId);
}
