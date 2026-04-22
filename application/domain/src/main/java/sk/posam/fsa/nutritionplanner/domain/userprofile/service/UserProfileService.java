package sk.posam.fsa.nutritionplanner.domain.userprofile.service;

import sk.posam.fsa.nutritionplanner.domain.userprofile.UserGoal;
import sk.posam.fsa.nutritionplanner.domain.userprofile.UserProfile;
import sk.posam.fsa.nutritionplanner.domain.userprofile.UserProfileRepository;

public class UserProfileService implements UserProfileFacade {

    private final UserProfileRepository userProfileRepository;

    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public UserProfile readCurrentUserProfile(String keycloakUserId, String email) {
        return userProfileRepository.readByKeycloakUserId(keycloakUserId)
                .map(existing -> enrichEmail(existing, email))
                .orElseGet(() -> createAndSaveEmptyProfile(keycloakUserId, email));
    }

    @Override
    public UserProfile updateCurrentUserProfile(String keycloakUserId, String email, UserProfile userProfile) {
        userProfile.setKeycloakUserId(keycloakUserId);
        userProfile.setEmail(email);
        userProfile.validateForUpdate();
        calculateTargets(userProfile);

        userProfileRepository.readByKeycloakUserId(keycloakUserId)
                .ifPresent(existing -> userProfile.setId(existing.getId()));

        return userProfileRepository.save(userProfile);
    }

    private UserProfile enrichEmail(UserProfile existing, String email) {
        if (email != null && !email.isBlank() && !email.equals(existing.getEmail())) {
            existing.setEmail(email);
            return userProfileRepository.save(existing);
        }
        return existing;
    }

    private UserProfile createAndSaveEmptyProfile(String keycloakUserId, String email) {
        UserProfile userProfile = new UserProfile(
                null,
                keycloakUserId,
                email,
                null,
                null,
                null,
                UserGoal.MAINTAIN_WEIGHT,
                null,
                null,
                null,
                null
        );
        return userProfileRepository.save(userProfile);
    }

    private void calculateTargets(UserProfile userProfile) {
        double maintenanceCalories = (10 * userProfile.getWeightKg()) + (6.25 * userProfile.getHeightCm())
                - (5 * userProfile.getAge()) + 150;
        double targetCalories = switch (userProfile.getGoal()) {
            case LOSE_WEIGHT -> maintenanceCalories - 400;
            case MAINTAIN_WEIGHT -> maintenanceCalories;
            case GAIN_MASS -> maintenanceCalories + 300;
        };

        double targetProtein = userProfile.getWeightKg() * 1.8;
        double targetFat = userProfile.getWeightKg() * 0.9;
        double targetCarbohydrates = Math.max(0, (targetCalories - (targetProtein * 4) - (targetFat * 9)) / 4);

        userProfile.setTargetCalories(round(targetCalories));
        userProfile.setTargetProtein(round(targetProtein));
        userProfile.setTargetFat(round(targetFat));
        userProfile.setTargetCarbohydrates(round(targetCarbohydrates));
    }

    private double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}
