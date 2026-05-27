package sk.posam.fsa.nutritionplanner.domain.ai.service;

import sk.posam.fsa.nutritionplanner.domain.ai.AiAutofillResult;
import sk.posam.fsa.nutritionplanner.domain.ai.AiMessage;
import sk.posam.fsa.nutritionplanner.domain.ai.AiProvider;
import sk.posam.fsa.nutritionplanner.domain.ai.ChatMessage;
import sk.posam.fsa.nutritionplanner.domain.ai.ChatSession;
import sk.posam.fsa.nutritionplanner.domain.ai.ChatSessionRepository;
import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProduct;
import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProductRepository;
import sk.posam.fsa.nutritionplanner.domain.meal.Meal;
import sk.posam.fsa.nutritionplanner.domain.meal.MealRepository;
import sk.posam.fsa.nutritionplanner.domain.mealplan.MealPlan;
import sk.posam.fsa.nutritionplanner.domain.mealplan.MealPlanRepository;
import sk.posam.fsa.nutritionplanner.domain.mealplan.PlanDay;
import sk.posam.fsa.nutritionplanner.domain.mealplan.PlanEntry;
import sk.posam.fsa.nutritionplanner.domain.shoppinglist.ShoppingListItem;
import sk.posam.fsa.nutritionplanner.domain.shoppinglist.ShoppingListRepository;
import sk.posam.fsa.nutritionplanner.domain.userprofile.UserProfile;
import sk.posam.fsa.nutritionplanner.domain.userprofile.UserProfileRepository;

import java.time.LocalDateTime;
import java.util.List;

public class AiAssistantService implements AiAssistantFacade {

    private final AiProvider aiProvider;
    private final UserProfileRepository userProfileRepository;
    private final FoodProductRepository foodProductRepository;
    private final MealRepository mealRepository;
    private final MealPlanRepository mealPlanRepository;
    private final ChatSessionRepository chatSessionRepository;
    private final ShoppingListRepository shoppingListRepository;

    public AiAssistantService(AiProvider aiProvider,
                               UserProfileRepository userProfileRepository,
                               FoodProductRepository foodProductRepository,
                               MealRepository mealRepository,
                               MealPlanRepository mealPlanRepository,
                               ChatSessionRepository chatSessionRepository,
                               ShoppingListRepository shoppingListRepository) {
        this.aiProvider = aiProvider;
        this.userProfileRepository = userProfileRepository;
        this.foodProductRepository = foodProductRepository;
        this.mealRepository = mealRepository;
        this.mealPlanRepository = mealPlanRepository;
        this.chatSessionRepository = chatSessionRepository;
        this.shoppingListRepository = shoppingListRepository;
    }

    @Override
    public String chat(String userId, List<AiMessage> messages) {
        String systemPrompt = buildSystemPrompt(userId);
        return aiProvider.chat(systemPrompt, messages);
    }

    @Override
    public AiAutofillResult autofill(String productName) {
        return aiProvider.autofill(productName);
    }

    @Override
    public ChatSession createSession(String userId) {
        ChatSession session = new ChatSession();
        session.setOwnerUserId(userId);
        session.setTitle("New Chat");
        session.setCreatedAt(LocalDateTime.now());
        return chatSessionRepository.save(session);
    }

    @Override
    public List<ChatSession> listSessions(String userId) {
        return chatSessionRepository.readAll(userId);
    }

    @Override
    public ChatSession getSession(String userId, Long sessionId) {
        return chatSessionRepository.readById(userId, sessionId)
                .orElseThrow(() -> new RuntimeException("Chat session not found: " + sessionId));
    }

    @Override
    public String sendMessage(String userId, Long sessionId, String userMessage) {
        ChatSession session = getSession(userId, sessionId);

        if (session.getMessages().isEmpty()) {
            String title = userMessage.length() > 60
                    ? userMessage.substring(0, 57) + "…"
                    : userMessage;
            session.setTitle(title);
        }

        ChatMessage userMsg = new ChatMessage();
        userMsg.setRole("user");
        userMsg.setContent(userMessage);
        userMsg.setCreatedAt(LocalDateTime.now());
        session.getMessages().add(userMsg);

        List<AiMessage> history = session.getMessages().stream()
                .map(m -> new AiMessage(m.getRole(), m.getContent()))
                .toList();

        String systemPrompt = buildSystemPrompt(userId);
        String aiResponse = aiProvider.chat(systemPrompt, history);

        ChatMessage aiMsg = new ChatMessage();
        aiMsg.setRole("assistant");
        aiMsg.setContent(aiResponse);
        aiMsg.setCreatedAt(LocalDateTime.now());
        session.getMessages().add(aiMsg);

        chatSessionRepository.save(session);
        return aiResponse;
    }

    @Override
    public void deleteSession(String userId, Long sessionId) {
        chatSessionRepository.deleteById(userId, sessionId);
    }

    private String buildSystemPrompt(String userId) {
        StringBuilder sb = new StringBuilder();
        sb.append("You are a personal nutrition assistant for the NutriPlan app.\n");
        sb.append("Give concise, actionable, evidence-based nutrition advice.\n");
        sb.append("\n");
        sb.append("LANGUAGE RULE: Always respond in English, regardless of what language the user writes in.\n");
        sb.append("\n");
        sb.append("TERMINOLOGY:\n");
        sb.append("- FOOD PRODUCTS: the user's available product inventory (what they have and track). When asked about fridge contents or available ingredients, use this list.\n");
        sb.append("- MEALS: existing saved recipes. When asked for NEW dish ideas, suggest creative NEW recipes using food products as raw ingredients — do NOT repeat the existing meals.\n");
        sb.append("- NUTRIENT DATA: only state micronutrient values (sodium, potassium, vitamins etc.) if they are explicitly listed in the FOOD PRODUCTS section below. Never invent or estimate micronutrient values from general knowledge.\n");
        sb.append("- TRACKED NUTRIENTS: the system tracks — macros: calories, protein, fat, carbs; minerals: sodium (Na), potassium (K), magnesium (Mg), iron (Fe), calcium (Ca), zinc (Zn); vitamins: A, C, D, E, K, B1, B2, B6, B9 (folate), B12. Reference these when advising on nutrient gaps.\n");
        sb.append("\n");

        UserProfile profile = userProfileRepository.readByKeycloakUserId(userId).orElse(null);
        if (profile != null) {
            sb.append("## USER PROFILE\n");
            if (profile.getAge() != null)          sb.append("- Age: ").append(profile.getAge()).append("\n");
            if (profile.getHeightCm() != null)      sb.append("- Height: ").append(profile.getHeightCm()).append(" cm\n");
            if (profile.getWeightKg() != null)      sb.append("- Weight: ").append(profile.getWeightKg()).append(" kg\n");
            if (profile.getGender() != null)        sb.append("- Gender: ").append(profile.getGender()).append("\n");
            if (profile.getActivityLevel() != null) sb.append("- Activity level: ").append(profile.getActivityLevel()).append("\n");
            if (profile.getGoal() != null)          sb.append("- Goal: ").append(profile.getGoal()).append("\n");
            if (profile.getBmr() != null)           sb.append("- BMR: ").append(profile.getBmr().intValue()).append(" kcal\n");
            if (profile.getTdee() != null)          sb.append("- TDEE: ").append(profile.getTdee().intValue()).append(" kcal\n");
            if (profile.getTargetCalories() != null) {
                sb.append("- Daily targets: ").append(profile.getTargetCalories().intValue()).append(" kcal");
                if (profile.getTargetProtein() != null)       sb.append(", protein ").append(profile.getTargetProtein().intValue()).append("g");
                if (profile.getTargetFat() != null)           sb.append(", fat ").append(profile.getTargetFat().intValue()).append("g");
                if (profile.getTargetCarbohydrates() != null) sb.append(", carbs ").append(profile.getTargetCarbohydrates().intValue()).append("g");
                sb.append("\n");
            }
            sb.append("\n");
        }

        List<FoodProduct> fridgeProducts = foodProductRepository.readAllInFridge(userId);
        if (!fridgeProducts.isEmpty()) {
            sb.append("## FRIDGE / AVAILABLE RIGHT NOW (per 100g)\n");
            fridgeProducts.forEach(p -> {
                sb.append("- ").append(p.getName()).append(": ")
                  .append(round(p.getCalories())).append("kcal")
                  .append(" P:").append(round(p.getProtein())).append("g")
                  .append(" F:").append(round(p.getFat())).append("g")
                  .append(" C:").append(round(p.getCarbohydrates())).append("g\n");
            });
            sb.append("\n");
        } else {
            sb.append("## FRIDGE: empty (no products marked as in fridge)\n\n");
        }

        List<FoodProduct> products = foodProductRepository.readAll(userId);
        if (!products.isEmpty()) {
            sb.append("## FOOD PRODUCTS (per 100g) — full product catalog\n");
            products.stream().limit(60).forEach(p -> {
                sb.append("- ").append(p.getName()).append(": ")
                  .append(round(p.getCalories())).append("kcal")
                  .append(" P:").append(round(p.getProtein())).append("g")
                  .append(" F:").append(round(p.getFat())).append("g")
                  .append(" C:").append(round(p.getCarbohydrates())).append("g");
                appendIfSet(sb, " Na:", p.getSodiumMg(), "mg");
                appendIfSet(sb, " K:", p.getPotassiumMg(), "mg");
                appendIfSet(sb, " Mg:", p.getMagnesiumMg(), "mg");
                appendIfSet(sb, " Fe:", p.getIronMg(), "mg");
                appendIfSet(sb, " Ca:", p.getCalciumMg(), "mg");
                appendIfSet(sb, " Zn:", p.getZincMg(), "mg");
                appendIfSet(sb, " VA:", p.getVitaminAMcg(), "mcg");
                appendIfSet(sb, " VC:", p.getVitaminCMg(), "mg");
                appendIfSet(sb, " VD:", p.getVitaminDMcg(), "mcg");
                appendIfSet(sb, " VE:", p.getVitaminEMg(), "mg");
                appendIfSet(sb, " VK:", p.getVitaminKMcg(), "mcg");
                appendIfSet(sb, " VB1:", p.getVitaminB1Mg(), "mg");
                appendIfSet(sb, " VB2:", p.getVitaminB2Mg(), "mg");
                appendIfSet(sb, " VB6:", p.getVitaminB6Mg(), "mg");
                appendIfSet(sb, " VB9:", p.getVitaminB9Mcg(), "mcg");
                appendIfSet(sb, " VB12:", p.getVitaminB12Mcg(), "mcg");
                sb.append("\n");
            });
            sb.append("\n");
        }

        List<Meal> meals = mealRepository.readAll(userId);
        if (!meals.isEmpty()) {
            sb.append("## EXISTING SAVED MEALS (already known to the user — do not suggest as new)\n");
            meals.stream().limit(20).forEach(m -> sb
                    .append("- ").append(m.getName())
                    .append(" (").append(m.getIngredients().size()).append(" ingredients)\n"));
            sb.append("\n");
        }

        List<MealPlan> plans = mealPlanRepository.readAll(userId);
        plans.stream().filter(MealPlan::isActive).findFirst().ifPresentOrElse(plan -> {
            sb.append("## ACTIVE MEAL PLAN: ").append(plan.getName())
              .append(" (").append(plan.getNumberOfDays()).append(" days)\n");

            UserProfile profileRef = userProfileRepository.readByKeycloakUserId(userId).orElse(null);
            Double targetKcal = profileRef != null ? profileRef.getTargetCalories() : null;
            Double targetP    = profileRef != null ? profileRef.getTargetProtein() : null;
            Double targetF    = profileRef != null ? profileRef.getTargetFat() : null;
            Double targetC    = profileRef != null ? profileRef.getTargetCarbohydrates() : null;

            for (PlanDay day : plan.getDays()) {
                double dayKcal = 0, dayP = 0, dayF = 0, dayC = 0;
                sb.append("  Day ").append(day.getDayNumber()).append(":\n");
                for (PlanEntry e : day.getEntries()) {
                    String item = e.getMealName() != null ? e.getMealName() : e.getFoodProductName();
                    String qty  = e.getPortions() != null
                            ? e.getPortions() + " portions"
                            : (e.getGrams() != null ? e.getGrams().intValue() + "g" : "");
                    sb.append("    [").append(e.getMealType()).append("] ").append(item)
                      .append(" (").append(qty).append(")")
                      .append(" – ").append(round(e.getCalories())).append("kcal")
                      .append(" P:").append(round(e.getProtein())).append("g")
                      .append(" F:").append(round(e.getFat())).append("g")
                      .append(" C:").append(round(e.getCarbohydrates())).append("g\n");
                    if (e.getCalories() != null)       dayKcal += e.getCalories();
                    if (e.getProtein() != null)        dayP    += e.getProtein();
                    if (e.getFat() != null)            dayF    += e.getFat();
                    if (e.getCarbohydrates() != null)  dayC    += e.getCarbohydrates();
                }
                sb.append("    Day total: ").append((int) dayKcal).append("kcal")
                  .append(" P:").append((int) dayP).append("g")
                  .append(" F:").append((int) dayF).append("g")
                  .append(" C:").append((int) dayC).append("g");
                if (targetKcal != null) {
                    int diff = (int) (dayKcal - targetKcal);
                    sb.append(" | vs target: ").append(diff >= 0 ? "+" : "").append(diff).append(" kcal");
                }
                sb.append("\n");
            }
        }, () -> sb.append("## ACTIVE PLAN: none\n"));

        List<ShoppingListItem> shoppingItems = shoppingListRepository.readAll(userId);
        if (!shoppingItems.isEmpty()) {
            sb.append("## SHOPPING LIST (items the user plans to buy)\n");
            shoppingItems.forEach(i -> {
                double kcal = i.getGrams() * i.getCaloriesPer100g() / 100;
                double price = i.getGrams() * i.getPricePer100g() / 100;
                sb.append("- ").append(i.getFoodProductName())
                  .append(": ").append((int) i.getGrams()).append("g")
                  .append(" | ").append((int) kcal).append("kcal")
                  .append(" | €").append(String.format("%.2f", price)).append("\n");
            });
            double totalPrice = shoppingItems.stream()
                    .mapToDouble(i -> i.getGrams() * i.getPricePer100g() / 100).sum();
            sb.append("  Total estimated cost: €").append(String.format("%.2f", totalPrice)).append("\n\n");
        } else {
            sb.append("## SHOPPING LIST: empty\n\n");
        }

        return sb.toString();
    }

    private void appendIfSet(StringBuilder sb, String label, Double value, String unit) {
        if (value != null && value > 0) {
            sb.append(label).append(round(value)).append(unit);
        }
    }

    private String round(Double v) {
        if (v == null) return "?";
        return String.valueOf(Math.round(v));
    }
}
