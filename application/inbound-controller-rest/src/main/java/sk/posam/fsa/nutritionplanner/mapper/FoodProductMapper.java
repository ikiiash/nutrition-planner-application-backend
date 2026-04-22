package sk.posam.fsa.nutritionplanner.mapper;

import org.springframework.stereotype.Component;
import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProduct;
import sk.posam.fsa.nutritionplanner.rest.dto.CreateFoodProductRequestDto;
import sk.posam.fsa.nutritionplanner.rest.dto.FoodProductDto;
import sk.posam.fsa.nutritionplanner.rest.dto.UpdateFoodProductRequestDto;

@Component
public class FoodProductMapper {

    public FoodProduct toDomain(CreateFoodProductRequestDto requestDto) {
        return new FoodProduct(
                null,
                null,
                requestDto.getName(),
                requestDto.getCategory(),
                requestDto.getGrams(),
                requestDto.getCalories(),
                requestDto.getProtein(),
                requestDto.getFat(),
                requestDto.getCarbohydrates(),
                requestDto.getPrice(),
                requestDto.getPhotoUrl()
        );
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
        return dto;
    }

    public FoodProduct toDomain(UpdateFoodProductRequestDto requestDto) {
        return new FoodProduct(
                null,
                null,
                requestDto.getName(),
                requestDto.getCategory(),
                requestDto.getGrams(),
                requestDto.getCalories(),
                requestDto.getProtein(),
                requestDto.getFat(),
                requestDto.getCarbohydrates(),
                requestDto.getPrice(),
                requestDto.getPhotoUrl()
        );
    }
}
