package sk.posam.fsa.nutritionplanner.domain.userprofile.service;

import sk.posam.fsa.nutritionplanner.domain.userprofile.ActivityLevel;
import sk.posam.fsa.nutritionplanner.domain.userprofile.Gender;
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
        UserProfile userProfile = new UserProfile();
        userProfile.setKeycloakUserId(keycloakUserId);
        userProfile.setEmail(email);
        userProfile.setGoal(UserGoal.MAINTAIN_WEIGHT);
        return userProfileRepository.save(userProfile);
    }

    /*
     * Mifflin–St Jeor BMR formula.
     * TDEE = BMR × activity multiplier.
     * Target calories = TDEE × goal factor (percentage approach, not absolute offset).
     *
     * Activity multipliers (Harris-Benedict scale):
     *   1.2   – sedentary (desk job, no exercise)
     *   1.375 – light (1–3 days/week)
     *   1.55  – moderate (3–5 days/week)
     *   1.725 – very active (6–7 days/week)
     *   1.9   – extra active (physical job or twice-daily training)
     *
     * Goal factors:
     *   cut  → 0.85 (−15% deficit, preserves muscle with high protein)
     *   maintain → 1.0
     *   bulk → 1.10 (+10% surplus, realistic lean-gain rate)
     *
     * Protein and fat are fixed per kg; carbs fill the remaining calories.
     * Protein at cut is raised to 2.0 g/kg to protect muscle during deficit.
     * Fat minimum 0.8 g/kg ensures hormonal health even at cut.
     */
    private void calculateTargets(UserProfile p) {
        double genderOffset = (p.getGender() == Gender.MALE) ? 5 : -161;
        double bmr = 10 * p.getWeightKg() + 6.25 * p.getHeightCm() - 5 * p.getAge() + genderOffset;

        double activityMultiplier = switch (p.getActivityLevel()) {
            case SEDENTARY -> 1.2;
            case LIGHTLY_ACTIVE -> 1.375;
            case MODERATELY_ACTIVE -> 1.55;
            case VERY_ACTIVE -> 1.725;
            case EXTRA_ACTIVE -> 1.9;
        };
        double tdee = bmr * activityMultiplier;

        double goalFactor = switch (p.getGoal()) {
            case LOSE_WEIGHT -> 0.85;
            case MAINTAIN_WEIGHT -> 1.0;
            case GAIN_MASS -> 1.10;
        };
        double targetCalories = tdee * goalFactor;

        double proteinPerKg = switch (p.getGoal()) {
            case LOSE_WEIGHT -> 2.0;
            case MAINTAIN_WEIGHT, GAIN_MASS -> 1.8;
        };
        double fatPerKg = switch (p.getGoal()) {
            case LOSE_WEIGHT -> 0.8;
            case MAINTAIN_WEIGHT, GAIN_MASS -> 1.0;
        };

        double targetProtein = p.getWeightKg() * proteinPerKg;
        double targetFat = p.getWeightKg() * fatPerKg;
        double targetCarbohydrates = Math.max(0, (targetCalories - (targetProtein * 4) - (targetFat * 9)) / 4);

        p.setBmr(roundWhole(bmr));
        p.setTdee(roundWhole(tdee));
        p.setTargetCalories(roundWhole(targetCalories));
        p.setTargetProtein(roundWhole(targetProtein));
        p.setTargetFat(roundWhole(targetFat));
        p.setTargetCarbohydrates(roundWhole(targetCarbohydrates));
    }

    private double roundWhole(double value) {
        return (double) Math.round(value);
    }
}
