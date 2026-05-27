package sk.posam.fsa.nutritionplanner.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import sk.posam.fsa.nutritionplanner.domain.shoppinglist.ShoppingListItem;
import sk.posam.fsa.nutritionplanner.domain.shoppinglist.service.ShoppingListFacade;
import sk.posam.fsa.nutritionplanner.mapper.ShoppingListMapper;
import sk.posam.fsa.nutritionplanner.rest.api.ShoppingListApi;
import sk.posam.fsa.nutritionplanner.rest.dto.SaveShoppingListItemRequestDto;
import sk.posam.fsa.nutritionplanner.rest.dto.ShoppingListItemDto;
import sk.posam.fsa.nutritionplanner.security.CurrentUserProvider;

import java.util.List;

@RestController
public class ShoppingListRestController implements ShoppingListApi {

    private final ShoppingListFacade shoppingListFacade;
    private final ShoppingListMapper shoppingListMapper;
    private final CurrentUserProvider currentUserProvider;

    public ShoppingListRestController(ShoppingListFacade shoppingListFacade,
                                      ShoppingListMapper shoppingListMapper,
                                      CurrentUserProvider currentUserProvider) {
        this.shoppingListFacade = shoppingListFacade;
        this.shoppingListMapper = shoppingListMapper;
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    public ResponseEntity<List<ShoppingListItemDto>> readShoppingList() {
        List<ShoppingListItemDto> items = shoppingListFacade.readAll(currentUserProvider.getUserId())
                .stream().map(shoppingListMapper::toDto).toList();
        return ResponseEntity.ok(items);
    }

    @Override
    public ResponseEntity<ShoppingListItemDto> addShoppingListItem(SaveShoppingListItemRequestDto dto) {
        ShoppingListItem item = shoppingListMapper.toDomain(dto);
        ShoppingListItem saved = shoppingListFacade.addItem(currentUserProvider.getUserId(), item);
        return ResponseEntity.status(HttpStatus.CREATED).body(shoppingListMapper.toDto(saved));
    }

    @Override
    public ResponseEntity<ShoppingListItemDto> updateShoppingListItem(Long itemId, SaveShoppingListItemRequestDto dto) {
        ShoppingListItem item = shoppingListMapper.toDomain(dto);
        ShoppingListItem updated = shoppingListFacade.updateItem(currentUserProvider.getUserId(), itemId, item);
        return ResponseEntity.ok(shoppingListMapper.toDto(updated));
    }

    @Override
    public ResponseEntity<Void> deleteShoppingListItem(Long itemId) {
        shoppingListFacade.deleteItem(currentUserProvider.getUserId(), itemId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> clearShoppingList() {
        shoppingListFacade.clearAll(currentUserProvider.getUserId());
        return ResponseEntity.noContent().build();
    }
}
