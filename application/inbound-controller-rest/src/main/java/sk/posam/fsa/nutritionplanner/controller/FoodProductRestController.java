package sk.posam.fsa.nutritionplanner.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProduct;
import sk.posam.fsa.nutritionplanner.domain.foodproduct.service.FoodProductFacade;
import sk.posam.fsa.nutritionplanner.mapper.FoodProductMapper;
import sk.posam.fsa.nutritionplanner.rest.api.FoodProductApi;
import sk.posam.fsa.nutritionplanner.rest.dto.CreateFoodProductRequestDto;
import sk.posam.fsa.nutritionplanner.rest.dto.FoodProductDto;

@RestController
public class FoodProductRestController implements FoodProductApi {

    private final FoodProductFacade foodProductFacade;
    private final FoodProductMapper foodProductMapper;

    public FoodProductRestController(FoodProductFacade foodProductFacade,
                                     FoodProductMapper foodProductMapper) {
        this.foodProductFacade = foodProductFacade;
        this.foodProductMapper = foodProductMapper;
    }

    @Override
    public ResponseEntity<FoodProductDto> createFoodProduct(CreateFoodProductRequestDto createFoodProductRequestDto) {
        FoodProduct foodProduct = foodProductMapper.toDomain(createFoodProductRequestDto);
        FoodProduct createdFoodProduct = foodProductFacade.createFoodProduct(foodProduct);
        FoodProductDto responseDto = foodProductMapper.toDto(createdFoodProduct);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}