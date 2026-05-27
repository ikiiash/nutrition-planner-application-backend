package sk.posam.fsa.nutritionplanner;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sk.posam.fsa.nutritionplanner.domain.shoppinglist.ShoppingListRepository;
import sk.posam.fsa.nutritionplanner.domain.shoppinglist.service.ShoppingListFacade;
import sk.posam.fsa.nutritionplanner.domain.shoppinglist.service.ShoppingListService;

@Configuration
public class ShoppingListBeanConfiguration {

    @Bean
    ShoppingListFacade shoppingListFacade(ShoppingListRepository shoppingListRepository) {
        return new ShoppingListService(shoppingListRepository);
    }
}
