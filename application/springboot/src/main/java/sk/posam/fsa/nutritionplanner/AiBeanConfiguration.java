package sk.posam.fsa.nutritionplanner;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sk.posam.fsa.nutritionplanner.domain.ai.AiProvider;
import sk.posam.fsa.nutritionplanner.domain.ai.ChatSessionRepository;
import sk.posam.fsa.nutritionplanner.domain.ai.service.AiAssistantFacade;
import sk.posam.fsa.nutritionplanner.domain.ai.service.AiAssistantService;
import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProductRepository;
import sk.posam.fsa.nutritionplanner.domain.meal.MealRepository;
import sk.posam.fsa.nutritionplanner.domain.mealplan.MealPlanRepository;
import sk.posam.fsa.nutritionplanner.domain.shoppinglist.ShoppingListRepository;
import sk.posam.fsa.nutritionplanner.domain.userprofile.UserProfileRepository;

@Configuration
public class AiBeanConfiguration {

    @Bean
    AiAssistantFacade aiAssistantFacade(AiProvider aiProvider,
                                        UserProfileRepository userProfileRepository,
                                        FoodProductRepository foodProductRepository,
                                        MealRepository mealRepository,
                                        MealPlanRepository mealPlanRepository,
                                        ChatSessionRepository chatSessionRepository,
                                        ShoppingListRepository shoppingListRepository) {
        return new AiAssistantService(aiProvider, userProfileRepository, foodProductRepository,
                mealRepository, mealPlanRepository, chatSessionRepository, shoppingListRepository);
    }
}
