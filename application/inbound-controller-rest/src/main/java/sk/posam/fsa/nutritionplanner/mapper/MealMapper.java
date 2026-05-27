package sk.posam.fsa.nutritionplanner.mapper;

import org.springframework.stereotype.Component;
import sk.posam.fsa.nutritionplanner.domain.meal.Meal;
import sk.posam.fsa.nutritionplanner.domain.meal.MealIngredient;
import sk.posam.fsa.nutritionplanner.rest.dto.CreateMealRequestDto;
import sk.posam.fsa.nutritionplanner.rest.dto.MealDto;
import sk.posam.fsa.nutritionplanner.rest.dto.MealIngredientDto;
import sk.posam.fsa.nutritionplanner.rest.dto.MealIngredientRequestDto;
import sk.posam.fsa.nutritionplanner.rest.dto.UpdateMealRequestDto;

import java.util.List;

@Component
public class MealMapper {

    public Meal toDomain(CreateMealRequestDto dto) {
        Meal meal = new Meal();
        meal.setName(dto.getName());
        meal.setServings(dto.getServings());
        meal.setIngredients(dto.getIngredients().stream().map(this::toDomain).toList());
        return meal;
    }

    public Meal toDomain(UpdateMealRequestDto dto) {
        Meal meal = new Meal();
        meal.setName(dto.getName());
        meal.setServings(dto.getServings());
        meal.setIngredients(dto.getIngredients().stream().map(this::toDomain).toList());
        return meal;
    }

    private MealIngredient toDomain(MealIngredientRequestDto dto) {
        MealIngredient ingredient = new MealIngredient();
        ingredient.setFoodProductId(dto.getFoodProductId());
        ingredient.setGrams(dto.getGrams());
        return ingredient;
    }

    public MealDto toDto(Meal meal) {
        MealDto dto = new MealDto();
        dto.setId(meal.getId());
        dto.setName(meal.getName());
        dto.setServings(meal.getServings());

        List<MealIngredientDto> ingredientDtos = meal.getIngredients().stream()
                .map(this::toIngredientDto)
                .toList();
        dto.setIngredients(ingredientDtos);

        double totalCalories = sum(ingredientDtos, MealIngredientDto::getCalories);
        double totalProtein = sum(ingredientDtos, MealIngredientDto::getProtein);
        double totalFat = sum(ingredientDtos, MealIngredientDto::getFat);
        double totalCarbohydrates = sum(ingredientDtos, MealIngredientDto::getCarbohydrates);
        double totalPrice = sum(ingredientDtos, MealIngredientDto::getPrice);

        dto.setTotalCalories(totalCalories);
        dto.setTotalProtein(totalProtein);
        dto.setTotalFat(totalFat);
        dto.setTotalCarbohydrates(totalCarbohydrates);
        dto.setTotalPrice(totalPrice);

        int servings = meal.getServings() != null && meal.getServings() > 0 ? meal.getServings() : 1;
        dto.setCaloriesPerServing(totalCalories / servings);
        dto.setProteinPerServing(totalProtein / servings);
        dto.setFatPerServing(totalFat / servings);
        dto.setCarbohydratesPerServing(totalCarbohydrates / servings);
        dto.setPricePerServing(totalPrice / servings);

        double totalSodium = sumNullable(ingredientDtos, MealIngredientDto::getSodiumMg);
        double totalPotassium = sumNullable(ingredientDtos, MealIngredientDto::getPotassiumMg);
        double totalMagnesium = sumNullable(ingredientDtos, MealIngredientDto::getMagnesiumMg);
        double totalIron = sumNullable(ingredientDtos, MealIngredientDto::getIronMg);
        double totalCalcium = sumNullable(ingredientDtos, MealIngredientDto::getCalciumMg);
        double totalZinc = sumNullable(ingredientDtos, MealIngredientDto::getZincMg);
        double totalVitA = sumNullable(ingredientDtos, MealIngredientDto::getVitaminAMcg);
        double totalVitC = sumNullable(ingredientDtos, MealIngredientDto::getVitaminCMg);
        double totalVitD = sumNullable(ingredientDtos, MealIngredientDto::getVitaminDMcg);
        double totalVitE = sumNullable(ingredientDtos, MealIngredientDto::getVitaminEMg);
        double totalVitK = sumNullable(ingredientDtos, MealIngredientDto::getVitaminKMcg);
        double totalB1 = sumNullable(ingredientDtos, MealIngredientDto::getVitaminB1Mg);
        double totalB2 = sumNullable(ingredientDtos, MealIngredientDto::getVitaminB2Mg);
        double totalB6 = sumNullable(ingredientDtos, MealIngredientDto::getVitaminB6Mg);
        double totalB9 = sumNullable(ingredientDtos, MealIngredientDto::getVitaminB9Mcg);
        double totalB12 = sumNullable(ingredientDtos, MealIngredientDto::getVitaminB12Mcg);

        dto.setTotalSodiumMg(totalSodium);
        dto.setTotalPotassiumMg(totalPotassium);
        dto.setTotalMagnesiumMg(totalMagnesium);
        dto.setTotalIronMg(totalIron);
        dto.setTotalCalciumMg(totalCalcium);
        dto.setTotalZincMg(totalZinc);
        dto.setTotalVitaminAMcg(totalVitA);
        dto.setTotalVitaminCMg(totalVitC);
        dto.setTotalVitaminDMcg(totalVitD);
        dto.setTotalVitaminEMg(totalVitE);
        dto.setTotalVitaminKMcg(totalVitK);
        dto.setTotalVitaminB1Mg(totalB1);
        dto.setTotalVitaminB2Mg(totalB2);
        dto.setTotalVitaminB6Mg(totalB6);
        dto.setTotalVitaminB9Mcg(totalB9);
        dto.setTotalVitaminB12Mcg(totalB12);

        dto.setSodiumMgPerServing(totalSodium / servings);
        dto.setPotassiumMgPerServing(totalPotassium / servings);
        dto.setMagnesiumMgPerServing(totalMagnesium / servings);
        dto.setIronMgPerServing(totalIron / servings);
        dto.setCalciumMgPerServing(totalCalcium / servings);
        dto.setZincMgPerServing(totalZinc / servings);
        dto.setVitaminAMcgPerServing(totalVitA / servings);
        dto.setVitaminCMgPerServing(totalVitC / servings);
        dto.setVitaminDMcgPerServing(totalVitD / servings);
        dto.setVitaminEMgPerServing(totalVitE / servings);
        dto.setVitaminKMcgPerServing(totalVitK / servings);
        dto.setVitaminB1MgPerServing(totalB1 / servings);
        dto.setVitaminB2MgPerServing(totalB2 / servings);
        dto.setVitaminB6MgPerServing(totalB6 / servings);
        dto.setVitaminB9McgPerServing(totalB9 / servings);
        dto.setVitaminB12McgPerServing(totalB12 / servings);

        return dto;
    }

    private MealIngredientDto toIngredientDto(MealIngredient ingredient) {
        MealIngredientDto dto = new MealIngredientDto();
        dto.setFoodProductId(ingredient.getFoodProductId());
        dto.setFoodProductName(ingredient.getFoodProductName());
        dto.setGrams(ingredient.getGrams());
        double g = ingredient.getGrams() != null ? ingredient.getGrams() : 0;
        dto.setCalories(nullSafe(ingredient.getCaloriesPerGram()) * g);
        dto.setProtein(nullSafe(ingredient.getProteinPerGram()) * g);
        dto.setFat(nullSafe(ingredient.getFatPerGram()) * g);
        dto.setCarbohydrates(nullSafe(ingredient.getCarbohydratesPerGram()) * g);
        dto.setPrice(nullSafe(ingredient.getPricePerGram()) * g);
        dto.setSodiumMg(nullableMult(ingredient.getSodiumMgPerGram(), g));
        dto.setPotassiumMg(nullableMult(ingredient.getPotassiumMgPerGram(), g));
        dto.setMagnesiumMg(nullableMult(ingredient.getMagnesiumMgPerGram(), g));
        dto.setIronMg(nullableMult(ingredient.getIronMgPerGram(), g));
        dto.setCalciumMg(nullableMult(ingredient.getCalciumMgPerGram(), g));
        dto.setZincMg(nullableMult(ingredient.getZincMgPerGram(), g));
        dto.setVitaminAMcg(nullableMult(ingredient.getVitaminAMcgPerGram(), g));
        dto.setVitaminCMg(nullableMult(ingredient.getVitaminCMgPerGram(), g));
        dto.setVitaminDMcg(nullableMult(ingredient.getVitaminDMcgPerGram(), g));
        dto.setVitaminEMg(nullableMult(ingredient.getVitaminEMgPerGram(), g));
        dto.setVitaminKMcg(nullableMult(ingredient.getVitaminKMcgPerGram(), g));
        dto.setVitaminB1Mg(nullableMult(ingredient.getVitaminB1MgPerGram(), g));
        dto.setVitaminB2Mg(nullableMult(ingredient.getVitaminB2MgPerGram(), g));
        dto.setVitaminB6Mg(nullableMult(ingredient.getVitaminB6MgPerGram(), g));
        dto.setVitaminB9Mcg(nullableMult(ingredient.getVitaminB9McgPerGram(), g));
        dto.setVitaminB12Mcg(nullableMult(ingredient.getVitaminB12McgPerGram(), g));
        return dto;
    }

    private Double nullableMult(Double perGram, double grams) {
        return perGram != null ? perGram * grams : null;
    }

    private double nullSafe(Double value) {
        return value != null ? value : 0.0;
    }

    @FunctionalInterface
    private interface DoubleExtractor {
        Double extract(MealIngredientDto dto);
    }

    private double sum(List<MealIngredientDto> dtos, DoubleExtractor extractor) {
        return dtos.stream().mapToDouble(d -> nullSafe(extractor.extract(d))).sum();
    }

    private double sumNullable(List<MealIngredientDto> dtos, DoubleExtractor extractor) {
        return dtos.stream().mapToDouble(d -> nullSafe(extractor.extract(d))).sum();
    }
}
