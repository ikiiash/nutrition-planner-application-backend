package sk.posam.fsa.nutritionplanner;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProductRepository;
import sk.posam.fsa.nutritionplanner.domain.meal.MealRepository;
import sk.posam.fsa.nutritionplanner.domain.meal.service.MealFacade;
import sk.posam.fsa.nutritionplanner.domain.meal.service.MealService;
import sk.posam.fsa.nutritionplanner.domain.mealplan.MealPlanRepository;

@Configuration
public class MealBeanConfiguration {

    @Bean
    MealFacade mealFacade(MealRepository mealRepository,
                          FoodProductRepository foodProductRepository,
                          MealPlanRepository mealPlanRepository) {
        return new MealService(mealRepository, foodProductRepository, mealPlanRepository);
    }
}
