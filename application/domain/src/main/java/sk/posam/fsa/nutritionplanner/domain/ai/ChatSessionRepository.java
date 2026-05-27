package sk.posam.fsa.nutritionplanner.domain.ai;

import java.util.List;
import java.util.Optional;

public interface ChatSessionRepository {
    ChatSession save(ChatSession session);
    List<ChatSession> readAll(String ownerUserId);
    Optional<ChatSession> readById(String ownerUserId, Long sessionId);
    void deleteById(String ownerUserId, Long sessionId);
}
