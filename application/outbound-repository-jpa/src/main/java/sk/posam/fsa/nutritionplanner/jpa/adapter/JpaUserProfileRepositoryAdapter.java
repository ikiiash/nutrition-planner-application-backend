package sk.posam.fsa.nutritionplanner.jpa.adapter;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sk.posam.fsa.nutritionplanner.domain.userprofile.UserProfile;
import sk.posam.fsa.nutritionplanner.domain.userprofile.UserProfileRepository;
import sk.posam.fsa.nutritionplanner.jpa.UserProfileSpringDataRepository;

import java.util.Optional;

@Repository
public class JpaUserProfileRepositoryAdapter implements UserProfileRepository {

    private final UserProfileSpringDataRepository userProfileSpringDataRepository;

    public JpaUserProfileRepositoryAdapter(UserProfileSpringDataRepository userProfileSpringDataRepository) {
        this.userProfileSpringDataRepository = userProfileSpringDataRepository;
    }

    @Override
    public Optional<UserProfile> readByKeycloakUserId(String keycloakUserId) {
        return userProfileSpringDataRepository.findByKeycloakUserId(keycloakUserId);
    }

    @Override
    @Transactional
    public UserProfile save(UserProfile userProfile) {
        return userProfileSpringDataRepository.save(userProfile);
    }
}
