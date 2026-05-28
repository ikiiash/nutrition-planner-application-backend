package sk.posam.fsa.nutritionplanner.domain.mealplan;

import java.util.List;
import java.util.Optional;

public interface MealPlanRepository {

    MealPlan save(MealPlan mealPlan);

    List<MealPlan> readAll(String ownerUserId);

    Optional<MealPlan> readById(String ownerUserId, Long mealPlanId);

    void deleteById(String ownerUserId, Long mealPlanId);

    void deactivateAll(String ownerUserId);

    List<MealPlan> readAllByMealId(String ownerUserId, Long mealId);

    List<MealPlan> readAllByFoodProductId(String ownerUserId, Long foodProductId);
}
