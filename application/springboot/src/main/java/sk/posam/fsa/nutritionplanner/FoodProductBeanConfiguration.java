package sk.posam.fsa.nutritionplanner;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProductRepository;
import sk.posam.fsa.nutritionplanner.domain.foodproduct.service.FoodProductFacade;
import sk.posam.fsa.nutritionplanner.domain.foodproduct.service.FoodProductService;

@Configuration
public class FoodProductBeanConfiguration {

    @Bean
    FoodProductFacade foodProductFacade(FoodProductRepository foodProductRepository) {
        return new FoodProductService(foodProductRepository);
    }
}