package sk.posam.fsa.nutritionplanner.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sk.posam.fsa.nutritionplanner.domain.meal.Meal;

import java.util.List;
import java.util.Optional;

public interface MealSpringDataRepository extends JpaRepository<Meal, Long> {

    List<Meal> findAllByOwnerUserId(String ownerUserId);

    Optional<Meal> findByIdAndOwnerUserId(Long id, String ownerUserId);

    @Query("SELECT DISTINCT m FROM Meal m JOIN m.ingredients i WHERE m.ownerUserId = :ownerUserId AND i.foodProductId = :foodProductId")
    List<Meal> findAllByOwnerUserIdAndIngredientFoodProductId(@Param("ownerUserId") String ownerUserId, @Param("foodProductId") Long foodProductId);
}
