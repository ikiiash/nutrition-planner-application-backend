package sk.posam.fsa.nutritionplanner.jpa.adapter;

import org.springframework.stereotype.Repository;
import sk.posam.fsa.nutritionplanner.domain.ai.ChatSession;
import sk.posam.fsa.nutritionplanner.domain.ai.ChatSessionRepository;
import sk.posam.fsa.nutritionplanner.jpa.ChatSessionSpringDataRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaChatSessionRepositoryAdapter implements ChatSessionRepository {

    private final ChatSessionSpringDataRepository repo;

    public JpaChatSessionRepositoryAdapter(ChatSessionSpringDataRepository repo) {
        this.repo = repo;
    }

    @Override
    public ChatSession save(ChatSession session) {
        return repo.save(session);
    }

    @Override
    public List<ChatSession> readAll(String ownerUserId) {
        return repo.findAllByOwnerUserIdOrderByCreatedAtDesc(ownerUserId);
    }

    @Override
    public Optional<ChatSession> readById(String ownerUserId, Long sessionId) {
        return repo.findByIdAndOwnerUserId(sessionId, ownerUserId);
    }

    @Override
    public void deleteById(String ownerUserId, Long sessionId) {
        readById(ownerUserId, sessionId).ifPresent(repo::delete);
    }
}
