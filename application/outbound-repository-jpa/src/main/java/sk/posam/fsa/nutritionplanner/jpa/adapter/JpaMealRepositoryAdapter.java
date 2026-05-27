package sk.posam.fsa.nutritionplanner.jpa.adapter;

import org.springframework.stereotype.Repository;
import sk.posam.fsa.nutritionplanner.domain.meal.Meal;
import sk.posam.fsa.nutritionplanner.domain.meal.MealRepository;
import sk.posam.fsa.nutritionplanner.jpa.MealSpringDataRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaMealRepositoryAdapter implements MealRepository {

    private final MealSpringDataRepository mealSpringDataRepository;

    public JpaMealRepositoryAdapter(MealSpringDataRepository mealSpringDataRepository) {
        this.mealSpringDataRepository = mealSpringDataRepository;
    }

    @Override
    public Meal save(Meal meal) {
        return mealSpringDataRepository.save(meal);
    }

    @Override
    public List<Meal> readAll(String ownerUserId) {
        return mealSpringDataRepository.findAllByOwnerUserId(ownerUserId);
    }

    @Override
    public Optional<Meal> readById(String ownerUserId, Long mealId) {
        return mealSpringDataRepository.findByIdAndOwnerUserId(mealId, ownerUserId);
    }

    @Override
    public void deleteById(String ownerUserId, Long mealId) {
        readById(ownerUserId, mealId).ifPresent(mealSpringDataRepository::delete);
    }
}
