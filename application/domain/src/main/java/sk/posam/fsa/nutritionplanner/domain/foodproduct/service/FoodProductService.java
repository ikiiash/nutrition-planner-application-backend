package sk.posam.fsa.nutritionplanner.domain.foodproduct.service;

import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProduct;
import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProductRepository;

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
}