package sk.posam.fsa.nutritionplanner.domain.ai.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sk.posam.fsa.nutritionplanner.domain.ai.AiAutofillResult;
import sk.posam.fsa.nutritionplanner.domain.ai.AiMessage;
import sk.posam.fsa.nutritionplanner.domain.ai.AiProvider;
import sk.posam.fsa.nutritionplanner.domain.ai.ChatMessage;
import sk.posam.fsa.nutritionplanner.domain.ai.ChatSession;
import sk.posam.fsa.nutritionplanner.domain.ai.ChatSessionRepository;
import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProduct;
import sk.posam.fsa.nutritionplanner.domain.foodproduct.FoodProductRepository;
import sk.posam.fsa.nutritionplanner.domain.meal.MealRepository;
import sk.posam.fsa.nutritionplanner.domain.mealplan.MealPlan;
import sk.posam.fsa.nutritionplanner.domain.mealplan.MealPlanRepository;
import sk.posam.fsa.nutritionplanner.domain.shoppinglist.ShoppingListRepository;
import sk.posam.fsa.nutritionplanner.domain.userprofile.ActivityLevel;
import sk.posam.fsa.nutritionplanner.domain.userprofile.Gender;
import sk.posam.fsa.nutritionplanner.domain.userprofile.UserGoal;
import sk.posam.fsa.nutritionplanner.domain.userprofile.UserProfile;
import sk.posam.fsa.nutritionplanner.domain.userprofile.UserProfileRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AiAssistantServiceTest {

    @Mock AiProvider aiProvider;
    @Mock UserProfileRepository userProfileRepository;
    @Mock FoodProductRepository foodProductRepository;
    @Mock MealRepository mealRepository;
    @Mock MealPlanRepository mealPlanRepository;
    @Mock ChatSessionRepository chatSessionRepository;
    @Mock ShoppingListRepository shoppingListRepository;

    @InjectMocks
    AiAssistantService sut;

    private static final String USER_ID = "user-1";

    // ─── chat ──────────────────────────────────────────────────────────────────

    @Test
    void chat_delegates_to_aiProvider_and_returns_response() {
        stubEmptyContext();
        when(aiProvider.chat(any(), any())).thenReturn("AI answer");

        List<AiMessage> messages = List.of(new AiMessage("user", "Hello"));
        String result = sut.chat(USER_ID, messages);

        assertEquals("AI answer", result);
        verify(aiProvider).chat(any(), eq(messages));
    }

    @Test
    void chat_passes_all_user_messages_to_aiProvider() {
        stubEmptyContext();
        when(aiProvider.chat(any(), any())).thenReturn("ok");

        List<AiMessage> messages = List.of(
                new AiMessage("user", "msg1"),
                new AiMessage("assistant", "resp1"),
                new AiMessage("user", "msg2")
        );
        sut.chat(USER_ID, messages);

        ArgumentCaptor<List<AiMessage>> captor = ArgumentCaptor.captor();
        verify(aiProvider).chat(any(), captor.capture());
        assertEquals(3, captor.getValue().size());
    }

    // ─── autofill ──────────────────────────────────────────────────────────────

    @Test
    void autofill_delegates_to_aiProvider() {
        AiAutofillResult expected = new AiAutofillResult();
        expected.setCalories(165.0);
        when(aiProvider.autofill("Chicken Breast")).thenReturn(expected);

        AiAutofillResult result = sut.autofill("Chicken Breast");

        assertSame(expected, result);
        verify(aiProvider).autofill("Chicken Breast");
    }

    // ─── createSession ─────────────────────────────────────────────────────────

    @Test
    void createSession_saves_session_with_owner_default_title_and_timestamp() {
        when(chatSessionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ChatSession result = sut.createSession(USER_ID);

        ArgumentCaptor<ChatSession> captor = ArgumentCaptor.forClass(ChatSession.class);
        verify(chatSessionRepository).save(captor.capture());
        assertEquals(USER_ID, captor.getValue().getOwnerUserId());
        assertEquals("New Chat", captor.getValue().getTitle());
        assertNotNull(captor.getValue().getCreatedAt());
    }

    // ─── listSessions ──────────────────────────────────────────────────────────

    @Test
    void listSessions_delegates_to_repository() {
        ChatSession s = new ChatSession();
        when(chatSessionRepository.readAll(USER_ID)).thenReturn(List.of(s));

        List<ChatSession> result = sut.listSessions(USER_ID);

        assertEquals(1, result.size());
        verify(chatSessionRepository).readAll(USER_ID);
    }

    // ─── getSession ────────────────────────────────────────────────────────────

    @Test
    void getSession_returns_session_when_found() {
        ChatSession session = session();
        when(chatSessionRepository.readById(USER_ID, 1L)).thenReturn(Optional.of(session));

        ChatSession result = sut.getSession(USER_ID, 1L);

        assertSame(session, result);
    }

    @Test
    void getSession_throws_when_not_found() {
        when(chatSessionRepository.readById(USER_ID, 99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> sut.getSession(USER_ID, 99L));
    }

    // ─── deleteSession ─────────────────────────────────────────────────────────

    @Test
    void deleteSession_delegates_to_repository() {
        sut.deleteSession(USER_ID, 1L);

        verify(chatSessionRepository).deleteById(USER_ID, 1L);
    }

    // ─── sendMessage ───────────────────────────────────────────────────────────

    @Test
    void sendMessage_appends_user_and_assistant_messages_and_saves() {
        ChatSession session = session();
        session.getMessages().add(chatMessage("user", "Previous"));

        when(chatSessionRepository.readById(USER_ID, 1L)).thenReturn(Optional.of(session));
        stubEmptyContext();
        when(aiProvider.chat(any(), any())).thenReturn("AI response");
        when(chatSessionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        String result = sut.sendMessage(USER_ID, 1L, "Hello AI");

        assertEquals("AI response", result);
        assertEquals(3, session.getMessages().size());
        assertEquals("user", session.getMessages().get(1).getRole());
        assertEquals("Hello AI", session.getMessages().get(1).getContent());
        assertEquals("assistant", session.getMessages().get(2).getRole());
        assertEquals("AI response", session.getMessages().get(2).getContent());
        verify(chatSessionRepository).save(session);
    }

    @Test
    void sendMessage_sets_title_from_first_message() {
        ChatSession session = session();
        when(chatSessionRepository.readById(USER_ID, 1L)).thenReturn(Optional.of(session));
        stubEmptyContext();
        when(aiProvider.chat(any(), any())).thenReturn("ok");
        when(chatSessionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        sut.sendMessage(USER_ID, 1L, "What should I eat today?");

        assertEquals("What should I eat today?", session.getTitle());
    }

    @Test
    void sendMessage_does_not_update_title_after_first_message() {
        ChatSession session = session();
        session.setTitle("Existing title");
        session.getMessages().add(chatMessage("user", "First"));

        when(chatSessionRepository.readById(USER_ID, 1L)).thenReturn(Optional.of(session));
        stubEmptyContext();
        when(aiProvider.chat(any(), any())).thenReturn("ok");
        when(chatSessionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        sut.sendMessage(USER_ID, 1L, "Second message");

        assertEquals("Existing title", session.getTitle());
    }

    @Test
    void sendMessage_truncates_long_title_with_ellipsis() {
        ChatSession session = session();
        when(chatSessionRepository.readById(USER_ID, 1L)).thenReturn(Optional.of(session));
        stubEmptyContext();
        when(aiProvider.chat(any(), any())).thenReturn("ok");
        when(chatSessionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        sut.sendMessage(USER_ID, 1L, "A".repeat(80));

        // implementation: substring(0,57) + "…" → 58 chars
        assertTrue(session.getTitle().endsWith("…"));
        assertTrue(session.getTitle().length() < 80);
    }

    @Test
    void sendMessage_does_not_truncate_title_when_message_is_exactly_60_chars() {
        ChatSession session = session();
        when(chatSessionRepository.readById(USER_ID, 1L)).thenReturn(Optional.of(session));
        stubEmptyContext();
        when(aiProvider.chat(any(), any())).thenReturn("ok");
        when(chatSessionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        String msg60 = "B".repeat(60);
        sut.sendMessage(USER_ID, 1L, msg60);

        assertEquals(msg60, session.getTitle());
        assertFalse(session.getTitle().endsWith("…"));
    }

    @Test
    void sendMessage_throws_when_session_not_found() {
        when(chatSessionRepository.readById(USER_ID, 99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> sut.sendMessage(USER_ID, 99L, "hello"));
        verify(aiProvider, never()).chat(any(), any());
    }

    // ─── system prompt content ─────────────────────────────────────────────────

    @Test
    void chat_includes_user_profile_fields_in_system_prompt() {
        UserProfile profile = new UserProfile();
        profile.setAge(30);
        profile.setWeightKg(75.0);
        profile.setHeightCm(180.0);
        profile.setGender(Gender.MALE);
        profile.setActivityLevel(ActivityLevel.MODERATELY_ACTIVE);
        profile.setGoal(UserGoal.MAINTAIN_WEIGHT);
        profile.setBmr(1800.0);
        profile.setTdee(2200.0);
        profile.setTargetCalories(2200.0);
        profile.setTargetProtein(150.0);
        profile.setTargetFat(70.0);
        profile.setTargetCarbohydrates(250.0);
        when(userProfileRepository.readByKeycloakUserId(USER_ID)).thenReturn(Optional.of(profile));
        when(foodProductRepository.readAllInFridge(USER_ID)).thenReturn(List.of());
        when(foodProductRepository.readAll(USER_ID)).thenReturn(List.of());
        when(mealRepository.readAll(USER_ID)).thenReturn(List.of());
        when(mealPlanRepository.readAll(USER_ID)).thenReturn(List.of());
        when(shoppingListRepository.readAll(USER_ID)).thenReturn(List.of());
        when(aiProvider.chat(any(), any())).thenReturn("ok");

        sut.chat(USER_ID, List.of(new AiMessage("user", "hi")));

        ArgumentCaptor<String> promptCaptor = ArgumentCaptor.forClass(String.class);
        verify(aiProvider).chat(promptCaptor.capture(), any());
        String prompt = promptCaptor.getValue();
        assertTrue(prompt.contains("Age: 30"));
        assertTrue(prompt.contains("Weight: 75.0 kg"));
        assertTrue(prompt.contains("Height: 180.0 cm"));
        assertTrue(prompt.contains("BMR: 1800"));
        assertTrue(prompt.contains("TDEE: 2200"));
    }

    @Test
    void chat_includes_fridge_products_in_system_prompt() {
        when(userProfileRepository.readByKeycloakUserId(USER_ID)).thenReturn(Optional.empty());

        FoodProduct milk = new FoodProduct();
        milk.setName("Milk");
        milk.setCalories(65.0);
        milk.setProtein(3.5);
        milk.setFat(3.5);
        milk.setCarbohydrates(5.0);
        milk.setInFridge(true);
        milk.setFridgeGrams(1000.0);
        when(foodProductRepository.readAllInFridge(USER_ID)).thenReturn(List.of(milk));
        when(foodProductRepository.readAll(USER_ID)).thenReturn(List.of(milk));
        when(mealRepository.readAll(USER_ID)).thenReturn(List.of());
        when(mealPlanRepository.readAll(USER_ID)).thenReturn(List.of());
        when(shoppingListRepository.readAll(USER_ID)).thenReturn(List.of());
        when(aiProvider.chat(any(), any())).thenReturn("ok");

        sut.chat(USER_ID, List.of(new AiMessage("user", "what's in fridge?")));

        ArgumentCaptor<String> promptCaptor = ArgumentCaptor.forClass(String.class);
        verify(aiProvider).chat(promptCaptor.capture(), any());
        String prompt = promptCaptor.getValue();
        assertTrue(prompt.contains("Milk"));
        assertTrue(prompt.contains("FRIDGE"));
    }

    @Test
    void chat_mentions_active_plan_in_system_prompt() {
        when(userProfileRepository.readByKeycloakUserId(USER_ID)).thenReturn(Optional.empty());
        when(foodProductRepository.readAllInFridge(USER_ID)).thenReturn(List.of());
        when(foodProductRepository.readAll(USER_ID)).thenReturn(List.of());
        when(mealRepository.readAll(USER_ID)).thenReturn(List.of());
        when(shoppingListRepository.readAll(USER_ID)).thenReturn(List.of());

        MealPlan activePlan = new MealPlan();
        activePlan.setId(1L);
        activePlan.setOwnerUserId(USER_ID);
        activePlan.setName("My Week Plan");
        activePlan.setStartDate(LocalDate.now());
        activePlan.setNumberOfDays(7);
        activePlan.setActive(true);
        activePlan.setActivatedAt(LocalDate.now());
        when(mealPlanRepository.readAll(USER_ID)).thenReturn(List.of(activePlan));
        when(aiProvider.chat(any(), any())).thenReturn("ok");

        sut.chat(USER_ID, List.of(new AiMessage("user", "show plan")));

        ArgumentCaptor<String> promptCaptor = ArgumentCaptor.forClass(String.class);
        verify(aiProvider).chat(promptCaptor.capture(), any());
        assertTrue(promptCaptor.getValue().contains("My Week Plan"));
        assertTrue(promptCaptor.getValue().contains("ACTIVE MEAL PLAN"));
    }

    @Test
    void chat_shows_no_active_plan_when_none_is_active() {
        stubEmptyContext();
        when(aiProvider.chat(any(), any())).thenReturn("ok");

        sut.chat(USER_ID, List.of(new AiMessage("user", "hi")));

        ArgumentCaptor<String> promptCaptor = ArgumentCaptor.forClass(String.class);
        verify(aiProvider).chat(promptCaptor.capture(), any());
        assertTrue(promptCaptor.getValue().contains("ACTIVE PLAN: none"));
    }

    @Test
    void chat_shows_empty_fridge_message_when_no_fridge_products() {
        stubEmptyContext();
        when(aiProvider.chat(any(), any())).thenReturn("ok");

        sut.chat(USER_ID, List.of(new AiMessage("user", "hi")));

        ArgumentCaptor<String> promptCaptor = ArgumentCaptor.forClass(String.class);
        verify(aiProvider).chat(promptCaptor.capture(), any());
        assertTrue(promptCaptor.getValue().contains("FRIDGE: empty"));
    }

    // ─── helpers ───────────────────────────────────────────────────────────────

    private void stubEmptyContext() {
        when(userProfileRepository.readByKeycloakUserId(USER_ID)).thenReturn(Optional.empty());
        when(foodProductRepository.readAllInFridge(USER_ID)).thenReturn(List.of());
        when(foodProductRepository.readAll(USER_ID)).thenReturn(List.of());
        when(mealRepository.readAll(USER_ID)).thenReturn(List.of());
        when(mealPlanRepository.readAll(USER_ID)).thenReturn(List.of());
        when(shoppingListRepository.readAll(USER_ID)).thenReturn(List.of());
    }

    private ChatSession session() {
        ChatSession s = new ChatSession();
        s.setId(1L);
        s.setOwnerUserId(USER_ID);
        s.setTitle("New Chat");
        s.setCreatedAt(LocalDateTime.now());
        return s;
    }

    private ChatMessage chatMessage(String role, String content) {
        ChatMessage m = new ChatMessage();
        m.setRole(role);
        m.setContent(content);
        m.setCreatedAt(LocalDateTime.now());
        return m;
    }
}
