package sk.posam.fsa.nutritionplanner.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import sk.posam.fsa.nutritionplanner.domain.mealplan.MealPlan;

import java.util.List;
import java.util.Optional;

public interface MealPlanSpringDataRepository extends JpaRepository<MealPlan, Long> {

    List<MealPlan> findAllByOwnerUserId(String ownerUserId);

    Optional<MealPlan> findByIdAndOwnerUserId(Long id, String ownerUserId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE MealPlan m SET m.active = false, m.activatedAt = null WHERE m.ownerUserId = :ownerUserId")
    void deactivateAllForUser(@Param("ownerUserId") String ownerUserId);

    @Query("SELECT DISTINCT mp FROM MealPlan mp JOIN mp.days d JOIN d.entries e WHERE mp.ownerUserId = :ownerUserId AND e.mealId = :mealId")
    List<MealPlan> findAllByOwnerUserIdAndMealEntry(@Param("ownerUserId") String ownerUserId, @Param("mealId") Long mealId);

    @Query("SELECT DISTINCT mp FROM MealPlan mp JOIN mp.days d JOIN d.entries e WHERE mp.ownerUserId = :ownerUserId AND e.foodProductId = :foodProductId")
    List<MealPlan> findAllByOwnerUserIdAndFoodProductEntry(@Param("ownerUserId") String ownerUserId, @Param("foodProductId") Long foodProductId);
}
