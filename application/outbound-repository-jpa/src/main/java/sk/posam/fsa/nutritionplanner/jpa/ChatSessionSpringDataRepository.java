package sk.posam.fsa.nutritionplanner.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.posam.fsa.nutritionplanner.domain.ai.ChatSession;

import java.util.List;
import java.util.Optional;

public interface ChatSessionSpringDataRepository extends JpaRepository<ChatSession, Long> {

    List<ChatSession> findAllByOwnerUserIdOrderByCreatedAtDesc(String ownerUserId);

    Optional<ChatSession> findByIdAndOwnerUserId(Long id, String ownerUserId);
}
