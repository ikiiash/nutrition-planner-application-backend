package sk.posam.fsa.nutritionplanner.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.posam.fsa.nutritionplanner.domain.mealplan.MealPlan;
import sk.posam.fsa.nutritionplanner.domain.mealplan.PlanEntry;
import sk.posam.fsa.nutritionplanner.domain.mealplan.service.MealPlanFacade;
import sk.posam.fsa.nutritionplanner.mapper.MealPlanMapper;
import sk.posam.fsa.nutritionplanner.rest.api.MealPlanApi;
import sk.posam.fsa.nutritionplanner.rest.dto.AddPlanEntryRequestDto;
import sk.posam.fsa.nutritionplanner.rest.dto.CreateMealPlanRequestDto;
import sk.posam.fsa.nutritionplanner.rest.dto.MealPlanDto;
import sk.posam.fsa.nutritionplanner.rest.dto.PlanEntryDto;
import sk.posam.fsa.nutritionplanner.rest.dto.UpdateMealPlanRequestDto;
import sk.posam.fsa.nutritionplanner.security.CurrentUserProvider;

import java.util.List;

@RestController
public class MealPlanRestController implements MealPlanApi {

    private final MealPlanFacade mealPlanFacade;
    private final MealPlanMapper mealPlanMapper;
    private final CurrentUserProvider currentUserProvider;

    public MealPlanRestController(MealPlanFacade mealPlanFacade,
                                  MealPlanMapper mealPlanMapper,
                                  CurrentUserProvider currentUserProvider) {
        this.mealPlanFacade = mealPlanFacade;
        this.mealPlanMapper = mealPlanMapper;
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    public ResponseEntity<MealPlanDto> createMealPlan(CreateMealPlanRequestDto createMealPlanRequestDto) {
        MealPlan plan = mealPlanMapper.toDomain(createMealPlanRequestDto);
        MealPlan created = mealPlanFacade.createMealPlan(currentUserProvider.getUserId(), plan);
        return ResponseEntity.status(HttpStatus.CREATED).body(mealPlanMapper.toDto(created));
    }

    @Override
    public ResponseEntity<List<MealPlanDto>> readMealPlans() {
        List<MealPlanDto> response = mealPlanFacade.readMealPlans(currentUserProvider.getUserId()).stream()
                .map(mealPlanMapper::toDto)
                .toList();
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<MealPlanDto> readMealPlan(Long mealPlanId) {
        MealPlan plan = mealPlanFacade.readMealPlan(currentUserProvider.getUserId(), mealPlanId);
        return ResponseEntity.ok(mealPlanMapper.toDto(plan));
    }

    @Override
    public ResponseEntity<MealPlanDto> updateMealPlan(Long mealPlanId, UpdateMealPlanRequestDto updateMealPlanRequestDto) {
        MealPlan plan = mealPlanMapper.toDomain(updateMealPlanRequestDto);
        MealPlan updated = mealPlanFacade.updateMealPlan(currentUserProvider.getUserId(), mealPlanId, plan);
        return ResponseEntity.ok(mealPlanMapper.toDto(updated));
    }

    @Override
    public ResponseEntity<Void> deleteMealPlan(Long mealPlanId) {
        mealPlanFacade.deleteMealPlan(currentUserProvider.getUserId(), mealPlanId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/meal-plans/{mealPlanId}/activate")
    public ResponseEntity<MealPlanDto> activateMealPlan(@PathVariable("mealPlanId") Long mealPlanId) {
        MealPlan plan = mealPlanFacade.activateMealPlan(currentUserProvider.getUserId(), mealPlanId);
        return ResponseEntity.ok(mealPlanMapper.toDto(plan));
    }

    @PutMapping("/meal-plans/{mealPlanId}/deactivate")
    public ResponseEntity<MealPlanDto> deactivateMealPlan(@PathVariable("mealPlanId") Long mealPlanId) {
        MealPlan plan = mealPlanFacade.deactivateMealPlan(currentUserProvider.getUserId(), mealPlanId);
        return ResponseEntity.ok(mealPlanMapper.toDto(plan));
    }

    @PostMapping("/meal-plans/{mealPlanId}/deduct-fridge")
    public ResponseEntity<MealPlanDto> deductFridge(@PathVariable("mealPlanId") Long mealPlanId) {
        MealPlan plan = mealPlanFacade.deductFridge(currentUserProvider.getUserId(), mealPlanId);
        return ResponseEntity.ok(mealPlanMapper.toDto(plan));
    }

    @Override
    public ResponseEntity<PlanEntryDto> addPlanEntry(Long mealPlanId, Long dayId, AddPlanEntryRequestDto addPlanEntryRequestDto) {
        PlanEntry entry = mealPlanMapper.toDomain(addPlanEntryRequestDto);
        PlanEntry added = mealPlanFacade.addEntry(currentUserProvider.getUserId(), mealPlanId, dayId, entry);
        return ResponseEntity.status(HttpStatus.CREATED).body(mealPlanMapper.toEntryDto(added));
    }

    @Override
    public ResponseEntity<Void> removePlanEntry(Long mealPlanId, Long dayId, Long entryId) {
        mealPlanFacade.removeEntry(currentUserProvider.getUserId(), mealPlanId, dayId, entryId);
        return ResponseEntity.noContent().build();
    }
}
