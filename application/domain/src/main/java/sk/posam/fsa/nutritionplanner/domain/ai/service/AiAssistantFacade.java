package sk.posam.fsa.nutritionplanner.domain.ai.service;

import sk.posam.fsa.nutritionplanner.domain.ai.AiAutofillResult;
import sk.posam.fsa.nutritionplanner.domain.ai.AiMessage;
import sk.posam.fsa.nutritionplanner.domain.ai.ChatSession;

import java.util.List;

public interface AiAssistantFacade {
    String chat(String userId, List<AiMessage> messages);
    AiAutofillResult autofill(String productName);

    ChatSession createSession(String userId);
    List<ChatSession> listSessions(String userId);
    ChatSession getSession(String userId, Long sessionId);
    String sendMessage(String userId, Long sessionId, String userMessage);
    void deleteSession(String userId, Long sessionId);
}
