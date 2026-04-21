package sk.posam.fsa.nutritionplanner.domain.foodproduct;

public class FoodProductNotFoundException extends RuntimeException {

    public FoodProductNotFoundException(Long foodProductId) {
        super("Food product with id " + foodProductId + " was not found.");
    }
}
