package sk.posam.fsa.nutritionplanner.domain.foodproduct.service;

import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProduct;

import java.util.List;

public interface FoodProductFacade {

    FoodProduct createFoodProduct(FoodProduct foodProduct);

    List<FoodProduct> readFoodProducts(String name);

    FoodProduct readFoodProduct(Long foodProductId);

    FoodProduct updateFoodProduct(Long foodProductId, FoodProduct foodProduct);

    void deleteFoodProduct(Long foodProductId);
}
