package sk.posam.fsa.nutritionplanner.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.posam.fsa.nutritionplanner.domain.meal.Meal;

import java.util.List;
import java.util.Optional;

public interface MealSpringDataRepository extends JpaRepository<Meal, Long> {

    List<Meal> findAllByOwnerUserId(String ownerUserId);

    Optional<Meal> findByIdAndOwnerUserId(Long id, String ownerUserId);
}
