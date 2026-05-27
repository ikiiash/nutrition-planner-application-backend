package sk.posam.fsa.nutritionplanner.mapper;

import org.springframework.stereotype.Component;
import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProduct;
import sk.posam.fsa.nutritionplanner.rest.dto.CreateFoodProductRequestDto;
import sk.posam.fsa.nutritionplanner.rest.dto.FoodProductDto;
import sk.posam.fsa.nutritionplanner.rest.dto.UpdateFoodProductRequestDto;

@Component
public class FoodProductMapper {

    public FoodProduct toDomain(CreateFoodProductRequestDto requestDto) {
        FoodProduct p = new FoodProduct(
                null, null,
                requestDto.getName(), requestDto.getCategory(), requestDto.getGrams(),
                requestDto.getCalories(), requestDto.getProtein(), requestDto.getFat(),
                requestDto.getCarbohydrates(), requestDto.getPrice(), requestDto.getPhotoUrl());
        mapMicronutrientsToDomain(requestDto, p);
        return p;
    }

    public FoodProductDto toDto(FoodProduct foodProduct) {
        FoodProductDto dto = new FoodProductDto();
        dto.setId(foodProduct.getId());
        dto.setName(foodProduct.getName());
        dto.setCategory(foodProduct.getCategory());
        dto.setGrams(foodProduct.getGrams());
        dto.setCalories(foodProduct.getCalories());
        dto.setProtein(foodProduct.getProtein());
        dto.setFat(foodProduct.getFat());
        dto.setCarbohydrates(foodProduct.getCarbohydrates());
        dto.setPrice(foodProduct.getPrice());
        dto.setPhotoUrl(foodProduct.getPhotoUrl());
        dto.setSodiumMg(foodProduct.getSodiumMg());
        dto.setPotassiumMg(foodProduct.getPotassiumMg());
        dto.setMagnesiumMg(foodProduct.getMagnesiumMg());
        dto.setIronMg(foodProduct.getIronMg());
        dto.setCalciumMg(foodProduct.getCalciumMg());
        dto.setZincMg(foodProduct.getZincMg());
        dto.setVitaminAMcg(foodProduct.getVitaminAMcg());
        dto.setVitaminCMg(foodProduct.getVitaminCMg());
        dto.setVitaminDMcg(foodProduct.getVitaminDMcg());
        dto.setVitaminEMg(foodProduct.getVitaminEMg());
        dto.setVitaminKMcg(foodProduct.getVitaminKMcg());
        dto.setVitaminB1Mg(foodProduct.getVitaminB1Mg());
        dto.setVitaminB2Mg(foodProduct.getVitaminB2Mg());
        dto.setVitaminB6Mg(foodProduct.getVitaminB6Mg());
        dto.setVitaminB9Mcg(foodProduct.getVitaminB9Mcg());
        dto.setVitaminB12Mcg(foodProduct.getVitaminB12Mcg());
        dto.setInFridge(foodProduct.isInFridge());
        dto.setFridgeGrams(foodProduct.getFridgeGrams());
        return dto;
    }

    public FoodProduct toDomain(UpdateFoodProductRequestDto requestDto) {
        FoodProduct p = new FoodProduct(
                null, null,
                requestDto.getName(), requestDto.getCategory(), requestDto.getGrams(),
                requestDto.getCalories(), requestDto.getProtein(), requestDto.getFat(),
                requestDto.getCarbohydrates(), requestDto.getPrice(), requestDto.getPhotoUrl());
        mapMicronutrientsToDomain(requestDto, p);
        return p;
    }

    private void mapMicronutrientsToDomain(CreateFoodProductRequestDto dto, FoodProduct p) {
        p.setSodiumMg(dto.getSodiumMg());
        p.setPotassiumMg(dto.getPotassiumMg());
        p.setMagnesiumMg(dto.getMagnesiumMg());
        p.setIronMg(dto.getIronMg());
        p.setCalciumMg(dto.getCalciumMg());
        p.setZincMg(dto.getZincMg());
        p.setVitaminAMcg(dto.getVitaminAMcg());
        p.setVitaminCMg(dto.getVitaminCMg());
        p.setVitaminDMcg(dto.getVitaminDMcg());
        p.setVitaminEMg(dto.getVitaminEMg());
        p.setVitaminKMcg(dto.getVitaminKMcg());
        p.setVitaminB1Mg(dto.getVitaminB1Mg());
        p.setVitaminB2Mg(dto.getVitaminB2Mg());
        p.setVitaminB6Mg(dto.getVitaminB6Mg());
        p.setVitaminB9Mcg(dto.getVitaminB9Mcg());
        p.setVitaminB12Mcg(dto.getVitaminB12Mcg());
    }

    private void mapMicronutrientsToDomain(UpdateFoodProductRequestDto dto, FoodProduct p) {
        p.setSodiumMg(dto.getSodiumMg());
        p.setPotassiumMg(dto.getPotassiumMg());
        p.setMagnesiumMg(dto.getMagnesiumMg());
        p.setIronMg(dto.getIronMg());
        p.setCalciumMg(dto.getCalciumMg());
        p.setZincMg(dto.getZincMg());
        p.setVitaminAMcg(dto.getVitaminAMcg());
        p.setVitaminCMg(dto.getVitaminCMg());
        p.setVitaminDMcg(dto.getVitaminDMcg());
        p.setVitaminEMg(dto.getVitaminEMg());
        p.setVitaminKMcg(dto.getVitaminKMcg());
        p.setVitaminB1Mg(dto.getVitaminB1Mg());
        p.setVitaminB2Mg(dto.getVitaminB2Mg());
        p.setVitaminB6Mg(dto.getVitaminB6Mg());
        p.setVitaminB9Mcg(dto.getVitaminB9Mcg());
        p.setVitaminB12Mcg(dto.getVitaminB12Mcg());
    }
}
