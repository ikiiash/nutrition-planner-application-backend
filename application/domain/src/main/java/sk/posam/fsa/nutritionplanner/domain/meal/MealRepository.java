package sk.posam.fsa.nutritionplanner.domain.meal;

import java.util.List;
import java.util.Optional;

public interface MealRepository {

    Meal save(Meal meal);

    List<Meal> readAll(String ownerUserId);

    Optional<Meal> readById(String ownerUserId, Long mealId);

    void deleteById(String ownerUserId, Long mealId);
}
