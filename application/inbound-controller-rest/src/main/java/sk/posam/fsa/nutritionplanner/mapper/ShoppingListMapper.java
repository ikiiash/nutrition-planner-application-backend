package sk.posam.fsa.nutritionplanner.mapper;

import org.springframework.stereotype.Component;
import sk.posam.fsa.nutritionplanner.domain.shoppinglist.ShoppingListItem;
import sk.posam.fsa.nutritionplanner.rest.dto.SaveShoppingListItemRequestDto;
import sk.posam.fsa.nutritionplanner.rest.dto.ShoppingListItemDto;

@Component
public class ShoppingListMapper {

    public ShoppingListItemDto toDto(ShoppingListItem item) {
        ShoppingListItemDto dto = new ShoppingListItemDto();
        dto.setId(item.getId());
        dto.setFoodProductId(item.getFoodProductId());
        dto.setFoodProductName(item.getFoodProductName());
        dto.setGrams(item.getGrams());
        dto.setCaloriesPer100g(item.getCaloriesPer100g());
        dto.setProteinPer100g(item.getProteinPer100g());
        dto.setFatPer100g(item.getFatPer100g());
        dto.setCarbsPer100g(item.getCarbsPer100g());
        dto.setPricePer100g(item.getPricePer100g());
        return dto;
    }

    public ShoppingListItem toDomain(SaveShoppingListItemRequestDto dto) {
        return ShoppingListItem.of(
                dto.getFoodProductId(),
                dto.getFoodProductName(),
                dto.getGrams(),
                dto.getCaloriesPer100g() != null ? dto.getCaloriesPer100g() : 0,
                dto.getProteinPer100g() != null ? dto.getProteinPer100g() : 0,
                dto.getFatPer100g() != null ? dto.getFatPer100g() : 0,
                dto.getCarbsPer100g() != null ? dto.getCarbsPer100g() : 0,
                dto.getPricePer100g() != null ? dto.getPricePer100g() : 0);
    }
}
