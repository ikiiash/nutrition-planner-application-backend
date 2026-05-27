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
import sk.posam.fsa.nutritionplanner.rest.dto.SetFridgeStatusRequestDto;
import sk.posam.fsa.nutritionplanner.rest.dto.UpdateFoodProductRequestDto;
import sk.posam.fsa.nutritionplanner.security.CurrentUserProvider;

import java.util.List;

@RestController
public class FoodProductRestController implements FoodProductApi {

    private final FoodProductFacade foodProductFacade;
    private final FoodProductMapper foodProductMapper;
    private final CurrentUserProvider currentUserProvider;

    public FoodProductRestController(FoodProductFacade foodProductFacade,
                                     FoodProductMapper foodProductMapper,
                                     CurrentUserProvider currentUserProvider) {
        this.foodProductFacade = foodProductFacade;
        this.foodProductMapper = foodProductMapper;
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    public ResponseEntity<FoodProductDto> createFoodProduct(CreateFoodProductRequestDto createFoodProductRequestDto) {
        FoodProduct foodProduct = foodProductMapper.toDomain(createFoodProductRequestDto);
        FoodProduct createdFoodProduct = foodProductFacade.createFoodProduct(currentUserProvider.getUserId(), foodProduct);
        FoodProductDto responseDto = foodProductMapper.toDto(createdFoodProduct);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Override
    public ResponseEntity<Void> deleteFoodProduct(Long foodProductId) {
        foodProductFacade.deleteFoodProduct(currentUserProvider.getUserId(), foodProductId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<FoodProductDto> readFoodProduct(Long foodProductId) {
        FoodProduct foodProduct = foodProductFacade.readFoodProduct(currentUserProvider.getUserId(), foodProductId);
        return ResponseEntity.ok(foodProductMapper.toDto(foodProduct));
    }

    @Override
    public ResponseEntity<List<FoodProductDto>> readFoodProducts(String name) {
        List<FoodProductDto> response = foodProductFacade.readFoodProducts(currentUserProvider.getUserId(), name).stream()
                .map(foodProductMapper::toDto)
                .toList();
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<FoodProductDto> updateFoodProduct(Long foodProductId,
                                                            UpdateFoodProductRequestDto updateFoodProductRequestDto) {
        FoodProduct foodProduct = foodProductMapper.toDomain(updateFoodProductRequestDto);
        FoodProduct updatedFoodProduct = foodProductFacade.updateFoodProduct(currentUserProvider.getUserId(), foodProductId, foodProduct);
        return ResponseEntity.ok(foodProductMapper.toDto(updatedFoodProduct));
    }

    @Override
    public ResponseEntity<FoodProductDto> setFridgeStatus(Long foodProductId,
                                                          SetFridgeStatusRequestDto setFridgeStatusRequestDto) {
        FoodProduct updated = foodProductFacade.setFridgeStatus(
                currentUserProvider.getUserId(), foodProductId,
                Boolean.TRUE.equals(setFridgeStatusRequestDto.getInFridge()),
                setFridgeStatusRequestDto.getFridgeGrams());
        return ResponseEntity.ok(foodProductMapper.toDto(updated));
    }
}
