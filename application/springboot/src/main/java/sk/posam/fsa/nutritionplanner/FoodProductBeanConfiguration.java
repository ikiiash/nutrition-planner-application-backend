package sk.posam.fsa.nutritionplanner;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProductRepository;
import sk.posam.fsa.nutritionplanner.domain.foodproduct.service.FoodProductFacade;
import sk.posam.fsa.nutritionplanner.domain.foodproduct.service.FoodProductService;
import sk.posam.fsa.nutritionplanner.domain.meal.MealRepository;
import sk.posam.fsa.nutritionplanner.domain.meal.service.MealFacade;
import sk.posam.fsa.nutritionplanner.domain.mealplan.MealPlanRepository;
import sk.posam.fsa.nutritionplanner.domain.userprofile.UserProfileRepository;
import sk.posam.fsa.nutritionplanner.domain.userprofile.service.UserProfileFacade;
import sk.posam.fsa.nutritionplanner.domain.userprofile.service.UserProfileService;

@Configuration
public class FoodProductBeanConfiguration {

    @Bean
    FoodProductFacade foodProductFacade(FoodProductRepository foodProductRepository,
                                        MealRepository mealRepository,
                                        MealPlanRepository mealPlanRepository,
                                        MealFacade mealFacade) {
        return new FoodProductService(foodProductRepository, mealRepository, mealPlanRepository, mealFacade);
    }

    @Bean
    UserProfileFacade userProfileFacade(UserProfileRepository userProfileRepository) {
        return new UserProfileService(userProfileRepository);
    }
}
