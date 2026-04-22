package sk.posam.fsa.nutritionplanner.domain.userprofile;

import java.util.Optional;

public interface UserProfileRepository {

    Optional<UserProfile> readByKeycloakUserId(String keycloakUserId);

    UserProfile save(UserProfile userProfile);
}
