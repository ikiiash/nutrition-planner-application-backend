package sk.posam.fsa.nutritionplanner;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProductRepository;
import sk.posam.fsa.nutritionplanner.domain.meal.MealRepository;
import sk.posam.fsa.nutritionplanner.domain.mealplan.MealPlanRepository;
import sk.posam.fsa.nutritionplanner.domain.mealplan.service.MealPlanFacade;
import sk.posam.fsa.nutritionplanner.domain.mealplan.service.MealPlanService;

@Configuration
public class MealPlanBeanConfiguration {

    @Bean
    MealPlanFacade mealPlanFacade(MealPlanRepository mealPlanRepository,
                                  MealRepository mealRepository,
                                  FoodProductRepository foodProductRepository) {
        return new MealPlanService(mealPlanRepository, mealRepository, foodProductRepository);
    }
}
