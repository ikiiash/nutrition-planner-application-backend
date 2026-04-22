package sk.posam.fsa.nutritionplanner.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.posam.fsa.nutritionplanner.domain.userprofile.UserProfile;

import java.util.Optional;

public interface UserProfileSpringDataRepository extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findByKeycloakUserId(String keycloakUserId);
}
