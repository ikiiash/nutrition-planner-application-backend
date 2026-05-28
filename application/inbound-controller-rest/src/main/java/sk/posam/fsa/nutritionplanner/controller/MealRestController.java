package sk.posam.fsa.nutritionplanner.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import sk.posam.fsa.nutritionplanner.domain.meal.Meal;
import sk.posam.fsa.nutritionplanner.domain.meal.service.MealFacade;
import sk.posam.fsa.nutritionplanner.mapper.MealMapper;
import sk.posam.fsa.nutritionplanner.rest.api.MealApi;
import sk.posam.fsa.nutritionplanner.rest.dto.CreateMealRequestDto;
import sk.posam.fsa.nutritionplanner.rest.dto.MealDto;
import sk.posam.fsa.nutritionplanner.rest.dto.UpdateMealRequestDto;
import sk.posam.fsa.nutritionplanner.security.CurrentUserProvider;

import java.util.List;

@RestController
public class MealRestController implements MealApi {

    private final MealFacade mealFacade;
    private final MealMapper mealMapper;
    private final CurrentUserProvider currentUserProvider;

    public MealRestController(MealFacade mealFacade,
                              MealMapper mealMapper,
                              CurrentUserProvider currentUserProvider) {
        this.mealFacade = mealFacade;
        this.mealMapper = mealMapper;
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    public ResponseEntity<MealDto> createMeal(CreateMealRequestDto createMealRequestDto) {
        Meal meal = mealMapper.toDomain(createMealRequestDto);
        Meal created = mealFacade.createMeal(currentUserProvider.getUserId(), meal);
        return ResponseEntity.status(HttpStatus.CREATED).body(mealMapper.toDto(created));
    }

    @Override
    public ResponseEntity<Void> deleteMeal(Long mealId) {
        mealFacade.deleteMeal(currentUserProvider.getUserId(), mealId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<MealDto> readMeal(Long mealId) {
        Meal meal = mealFacade.readMeal(currentUserProvider.getUserId(), mealId);
        return ResponseEntity.ok(mealMapper.toDto(meal));
    }

    @Override
    public ResponseEntity<List<MealDto>> readMeals() {
        List<MealDto> response = mealFacade.readMeals(currentUserProvider.getUserId()).stream()
                .map(mealMapper::toDto)
                .toList();
        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional
    public ResponseEntity<MealDto> updateMeal(Long mealId, UpdateMealRequestDto updateMealRequestDto) {
        Meal meal = mealMapper.toDomain(updateMealRequestDto);
        Meal updated = mealFacade.updateMeal(currentUserProvider.getUserId(), mealId, meal);
        return ResponseEntity.ok(mealMapper.toDto(updated));
    }
}
