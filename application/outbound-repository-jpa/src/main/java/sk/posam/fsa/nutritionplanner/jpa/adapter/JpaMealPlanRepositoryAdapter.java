package sk.posam.fsa.nutritionplanner.jpa.adapter;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sk.posam.fsa.nutritionplanner.domain.mealplan.MealPlan;
import sk.posam.fsa.nutritionplanner.domain.mealplan.MealPlanRepository;
import sk.posam.fsa.nutritionplanner.jpa.MealPlanSpringDataRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaMealPlanRepositoryAdapter implements MealPlanRepository {

    private final MealPlanSpringDataRepository mealPlanSpringDataRepository;

    public JpaMealPlanRepositoryAdapter(MealPlanSpringDataRepository mealPlanSpringDataRepository) {
        this.mealPlanSpringDataRepository = mealPlanSpringDataRepository;
    }

    @Override
    @Transactional
    public MealPlan save(MealPlan mealPlan) {
        return mealPlanSpringDataRepository.save(mealPlan);
    }

    @Override
    public List<MealPlan> readAll(String ownerUserId) {
        return mealPlanSpringDataRepository.findAllByOwnerUserId(ownerUserId);
    }

    @Override
    public Optional<MealPlan> readById(String ownerUserId, Long mealPlanId) {
        return mealPlanSpringDataRepository.findByIdAndOwnerUserId(mealPlanId, ownerUserId);
    }

    @Override
    @Transactional
    public void deleteById(String ownerUserId, Long mealPlanId) {
        readById(ownerUserId, mealPlanId).ifPresent(mealPlanSpringDataRepository::delete);
    }

    @Override
    @Transactional
    public void deactivateAll(String ownerUserId) {
        mealPlanSpringDataRepository.deactivateAllForUser(ownerUserId);
    }

    @Override
    public List<MealPlan> readAllByMealId(String ownerUserId, Long mealId) {
        return mealPlanSpringDataRepository.findAllByOwnerUserIdAndMealEntry(ownerUserId, mealId);
    }

    @Override
    public List<MealPlan> readAllByFoodProductId(String ownerUserId, Long foodProductId) {
        return mealPlanSpringDataRepository.findAllByOwnerUserIdAndFoodProductEntry(ownerUserId, foodProductId);
    }
}
