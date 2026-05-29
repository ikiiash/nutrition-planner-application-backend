package sk.posam.fsa.nutritionplanner.mapper;

import org.springframework.stereotype.Component;
import sk.posam.fsa.nutritionplanner.domain.mealplan.EntryType;
import sk.posam.fsa.nutritionplanner.domain.mealplan.MealPlan;
import sk.posam.fsa.nutritionplanner.domain.mealplan.MealType;
import sk.posam.fsa.nutritionplanner.domain.mealplan.PlanDay;
import sk.posam.fsa.nutritionplanner.domain.mealplan.PlanEntry;
import sk.posam.fsa.nutritionplanner.rest.dto.AddPlanEntryRequestDto;
import sk.posam.fsa.nutritionplanner.rest.dto.CreateMealPlanRequestDto;
import sk.posam.fsa.nutritionplanner.rest.dto.EntryTypeEnumDto;
import sk.posam.fsa.nutritionplanner.rest.dto.MealPlanDto;
import sk.posam.fsa.nutritionplanner.rest.dto.MealTypeEnumDto;
import sk.posam.fsa.nutritionplanner.rest.dto.PlanDayDto;
import sk.posam.fsa.nutritionplanner.rest.dto.PlanEntryDto;
import sk.posam.fsa.nutritionplanner.rest.dto.UpdateMealPlanRequestDto;

import java.util.List;

@Component
public class MealPlanMapper {

    public MealPlan toDomain(CreateMealPlanRequestDto dto) {
        return MealPlan.of(dto.getName(), dto.getStartDate(), dto.getNumberOfDays());
    }

    public MealPlan toDomain(UpdateMealPlanRequestDto dto) {
        return MealPlan.of(dto.getName(), dto.getStartDate(), dto.getNumberOfDays());
    }

    public PlanEntry toDomain(AddPlanEntryRequestDto dto) {
        return PlanEntry.of(
                toMealType(dto.getMealType()),
                toEntryType(dto.getEntryType()),
                dto.getMealId(),
                dto.getPortions(),
                dto.getFoodProductId(),
                dto.getGrams());
    }

    public MealPlanDto toDto(MealPlan plan) {
        MealPlanDto dto = new MealPlanDto();
        dto.setId(plan.getId());
        dto.setName(plan.getName());
        dto.setStartDate(plan.getStartDate());
        dto.setNumberOfDays(plan.getNumberOfDays());
        dto.setIsActive(plan.isActive());
        dto.setActivatedAt(plan.getActivatedAt());
        dto.setLastDeductedDayNumber(plan.getLastDeductedDayNumber());

        List<PlanDayDto> dayDtos = plan.getDays().stream()
                .map(d -> toDayDto(d, plan.getStartDate()))
                .toList();
        dto.setDays(dayDtos);

        dto.setTotalCalories(sum(dayDtos, d -> d.getDailyCalories()));
        dto.setTotalProtein(sum(dayDtos, d -> d.getDailyProtein()));
        dto.setTotalFat(sum(dayDtos, d -> d.getDailyFat()));
        dto.setTotalCarbohydrates(sum(dayDtos, d -> d.getDailyCarbohydrates()));
        dto.setTotalPrice(sum(dayDtos, d -> d.getDailyPrice()));
        dto.setTotalSodiumMg(sum(dayDtos, d -> d.getDailySodiumMg()));
        dto.setTotalPotassiumMg(sum(dayDtos, d -> d.getDailyPotassiumMg()));
        dto.setTotalMagnesiumMg(sum(dayDtos, d -> d.getDailyMagnesiumMg()));
        dto.setTotalIronMg(sum(dayDtos, d -> d.getDailyIronMg()));
        dto.setTotalCalciumMg(sum(dayDtos, d -> d.getDailyCalciumMg()));
        dto.setTotalZincMg(sum(dayDtos, d -> d.getDailyZincMg()));
        dto.setTotalVitaminAMcg(sum(dayDtos, d -> d.getDailyVitaminAMcg()));
        dto.setTotalVitaminCMg(sum(dayDtos, d -> d.getDailyVitaminCMg()));
        dto.setTotalVitaminDMcg(sum(dayDtos, d -> d.getDailyVitaminDMcg()));
        dto.setTotalVitaminEMg(sum(dayDtos, d -> d.getDailyVitaminEMg()));
        dto.setTotalVitaminKMcg(sum(dayDtos, d -> d.getDailyVitaminKMcg()));
        dto.setTotalVitaminB1Mg(sum(dayDtos, d -> d.getDailyVitaminB1Mg()));
        dto.setTotalVitaminB2Mg(sum(dayDtos, d -> d.getDailyVitaminB2Mg()));
        dto.setTotalVitaminB6Mg(sum(dayDtos, d -> d.getDailyVitaminB6Mg()));
        dto.setTotalVitaminB9Mcg(sum(dayDtos, d -> d.getDailyVitaminB9Mcg()));
        dto.setTotalVitaminB12Mcg(sum(dayDtos, d -> d.getDailyVitaminB12Mcg()));
        return dto;
    }

    private PlanDayDto toDayDto(PlanDay day, java.time.LocalDate planStart) {
        PlanDayDto dto = new PlanDayDto();
        dto.setId(day.getId());
        dto.setDayNumber(day.getDayNumber());
        if (planStart != null && day.getDayNumber() != null) {
            dto.setDate(planStart.plusDays(day.getDayNumber() - 1));
        }

        List<PlanEntryDto> entryDtos = day.getEntries().stream()
                .map(this::toEntryDto)
                .toList();
        dto.setEntries(entryDtos);

        dto.setDailyCalories(sumEntries(entryDtos, PlanEntryDto::getCalories));
        dto.setDailyProtein(sumEntries(entryDtos, PlanEntryDto::getProtein));
        dto.setDailyFat(sumEntries(entryDtos, PlanEntryDto::getFat));
        dto.setDailyCarbohydrates(sumEntries(entryDtos, PlanEntryDto::getCarbohydrates));
        dto.setDailyPrice(sumEntries(entryDtos, PlanEntryDto::getPrice));
        dto.setDailySodiumMg(sumEntries(entryDtos, PlanEntryDto::getSodiumMg));
        dto.setDailyPotassiumMg(sumEntries(entryDtos, PlanEntryDto::getPotassiumMg));
        dto.setDailyMagnesiumMg(sumEntries(entryDtos, PlanEntryDto::getMagnesiumMg));
        dto.setDailyIronMg(sumEntries(entryDtos, PlanEntryDto::getIronMg));
        dto.setDailyCalciumMg(sumEntries(entryDtos, PlanEntryDto::getCalciumMg));
        dto.setDailyZincMg(sumEntries(entryDtos, PlanEntryDto::getZincMg));
        dto.setDailyVitaminAMcg(sumEntries(entryDtos, PlanEntryDto::getVitaminAMcg));
        dto.setDailyVitaminCMg(sumEntries(entryDtos, PlanEntryDto::getVitaminCMg));
        dto.setDailyVitaminDMcg(sumEntries(entryDtos, PlanEntryDto::getVitaminDMcg));
        dto.setDailyVitaminEMg(sumEntries(entryDtos, PlanEntryDto::getVitaminEMg));
        dto.setDailyVitaminKMcg(sumEntries(entryDtos, PlanEntryDto::getVitaminKMcg));
        dto.setDailyVitaminB1Mg(sumEntries(entryDtos, PlanEntryDto::getVitaminB1Mg));
        dto.setDailyVitaminB2Mg(sumEntries(entryDtos, PlanEntryDto::getVitaminB2Mg));
        dto.setDailyVitaminB6Mg(sumEntries(entryDtos, PlanEntryDto::getVitaminB6Mg));
        dto.setDailyVitaminB9Mcg(sumEntries(entryDtos, PlanEntryDto::getVitaminB9Mcg));
        dto.setDailyVitaminB12Mcg(sumEntries(entryDtos, PlanEntryDto::getVitaminB12Mcg));
        return dto;
    }

    public PlanEntryDto toEntryDto(PlanEntry entry) {
        PlanEntryDto dto = new PlanEntryDto();
        dto.setId(entry.getId());
        dto.setMealType(toMealTypeDto(entry.getMealType()));
        dto.setEntryType(toEntryTypeDto(entry.getEntryType()));
        dto.setMealId(entry.getMealId());
        dto.setMealName(entry.getMealName());
        dto.setPortions(entry.getPortions());
        dto.setFoodProductId(entry.getFoodProductId());
        dto.setFoodProductName(entry.getFoodProductName());
        dto.setGrams(entry.getGrams());
        dto.setCalories(nullSafe(entry.getCalories()));
        dto.setProtein(nullSafe(entry.getProtein()));
        dto.setFat(nullSafe(entry.getFat()));
        dto.setCarbohydrates(nullSafe(entry.getCarbohydrates()));
        dto.setPrice(nullSafe(entry.getPrice()));
        dto.setSodiumMg(entry.getSodiumMg());
        dto.setPotassiumMg(entry.getPotassiumMg());
        dto.setMagnesiumMg(entry.getMagnesiumMg());
        dto.setIronMg(entry.getIronMg());
        dto.setCalciumMg(entry.getCalciumMg());
        dto.setZincMg(entry.getZincMg());
        dto.setVitaminAMcg(entry.getVitaminAMcg());
        dto.setVitaminCMg(entry.getVitaminCMg());
        dto.setVitaminDMcg(entry.getVitaminDMcg());
        dto.setVitaminEMg(entry.getVitaminEMg());
        dto.setVitaminKMcg(entry.getVitaminKMcg());
        dto.setVitaminB1Mg(entry.getVitaminB1Mg());
        dto.setVitaminB2Mg(entry.getVitaminB2Mg());
        dto.setVitaminB6Mg(entry.getVitaminB6Mg());
        dto.setVitaminB9Mcg(entry.getVitaminB9Mcg());
        dto.setVitaminB12Mcg(entry.getVitaminB12Mcg());
        return dto;
    }

    private MealType toMealType(MealTypeEnumDto dto) {
        if (dto == null) return null;
        return MealType.valueOf(dto.name());
    }

    private EntryType toEntryType(EntryTypeEnumDto dto) {
        if (dto == null) return null;
        return EntryType.valueOf(dto.name());
    }

    private MealTypeEnumDto toMealTypeDto(MealType type) {
        if (type == null) return null;
        return MealTypeEnumDto.valueOf(type.name());
    }

    private EntryTypeEnumDto toEntryTypeDto(EntryType type) {
        if (type == null) return null;
        return EntryTypeEnumDto.valueOf(type.name());
    }

    private double nullSafe(Double value) {
        return value != null ? value : 0.0;
    }

    @FunctionalInterface
    private interface DayExtractor {
        Double extract(PlanDayDto dto);
    }

    @FunctionalInterface
    private interface EntryExtractor {
        Double extract(PlanEntryDto dto);
    }

    private double sum(List<PlanDayDto> dtos, DayExtractor extractor) {
        return dtos.stream().mapToDouble(d -> nullSafe(extractor.extract(d))).sum();
    }

    private double sumEntries(List<PlanEntryDto> dtos, EntryExtractor extractor) {
        return dtos.stream().mapToDouble(d -> nullSafe(extractor.extract(d))).sum();
    }
}
