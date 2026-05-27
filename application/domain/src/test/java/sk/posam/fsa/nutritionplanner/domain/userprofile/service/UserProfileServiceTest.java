package sk.posam.fsa.nutritionplanner.domain.userprofile.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sk.posam.fsa.nutritionplanner.domain.userprofile.ActivityLevel;
import sk.posam.fsa.nutritionplanner.domain.userprofile.Gender;
import sk.posam.fsa.nutritionplanner.domain.userprofile.UserGoal;
import sk.posam.fsa.nutritionplanner.domain.userprofile.UserProfile;
import sk.posam.fsa.nutritionplanner.domain.userprofile.UserProfileRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

    @Mock
    UserProfileRepository userProfileRepository;

    @InjectMocks
    UserProfileService sut;

    private static final String KC_USER_ID = "kc-user-1";
    private static final String EMAIL = "user@test.com";

    @Test
    void readCurrentUserProfile_returns_existing_profile() {
        UserProfile existing = profile();
        when(userProfileRepository.readByKeycloakUserId(KC_USER_ID)).thenReturn(Optional.of(existing));

        UserProfile result = sut.readCurrentUserProfile(KC_USER_ID, EMAIL);

        assertEquals(existing, result);
        verify(userProfileRepository, never()).save(any());
    }

    @Test
    void readCurrentUserProfile_creates_empty_profile_when_not_found() {
        when(userProfileRepository.readByKeycloakUserId(KC_USER_ID)).thenReturn(Optional.empty());
        when(userProfileRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        UserProfile result = sut.readCurrentUserProfile(KC_USER_ID, EMAIL);

        ArgumentCaptor<UserProfile> captor = ArgumentCaptor.forClass(UserProfile.class);
        verify(userProfileRepository).save(captor.capture());
        assertEquals(KC_USER_ID, captor.getValue().getKeycloakUserId());
        assertEquals(EMAIL, captor.getValue().getEmail());
        assertEquals(UserGoal.MAINTAIN_WEIGHT, captor.getValue().getGoal());
    }

    @Test
    void readCurrentUserProfile_updates_email_when_changed() {
        UserProfile existing = profile();
        existing.setEmail("old@test.com");
        when(userProfileRepository.readByKeycloakUserId(KC_USER_ID)).thenReturn(Optional.of(existing));
        when(userProfileRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        sut.readCurrentUserProfile(KC_USER_ID, "new@test.com");

        verify(userProfileRepository).save(existing);
        assertEquals("new@test.com", existing.getEmail());
    }

    @Test
    void readCurrentUserProfile_does_not_save_when_email_is_same() {
        UserProfile existing = profile();
        existing.setEmail(EMAIL);
        when(userProfileRepository.readByKeycloakUserId(KC_USER_ID)).thenReturn(Optional.of(existing));

        sut.readCurrentUserProfile(KC_USER_ID, EMAIL);

        verify(userProfileRepository, never()).save(any());
    }

    @Test
    void updateCurrentUserProfile_sets_owner_email_calculates_targets_and_saves() {
        when(userProfileRepository.readByKeycloakUserId(KC_USER_ID)).thenReturn(Optional.empty());
        when(userProfileRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        UserProfile update = validUpdateProfile();
        UserProfile result = sut.updateCurrentUserProfile(KC_USER_ID, EMAIL, update);

        assertEquals(KC_USER_ID, result.getKeycloakUserId());
        assertEquals(EMAIL, result.getEmail());
        assertNotNull(result.getBmr());
        assertNotNull(result.getTdee());
        assertNotNull(result.getTargetCalories());
        assertNotNull(result.getTargetProtein());
        assertNotNull(result.getTargetFat());
        assertNotNull(result.getTargetCarbohydrates());
    }

    @Test
    void updateCurrentUserProfile_reuses_existing_id() {
        UserProfile existing = profile();
        existing.setId(42L);
        when(userProfileRepository.readByKeycloakUserId(KC_USER_ID)).thenReturn(Optional.of(existing));
        when(userProfileRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        UserProfile result = sut.updateCurrentUserProfile(KC_USER_ID, EMAIL, validUpdateProfile());

        assertEquals(42L, result.getId());
    }

    @Test
    void updateCurrentUserProfile_throws_when_validation_fails() {
        UserProfile invalid = new UserProfile();
        invalid.setAge(-1);

        assertThrows(IllegalArgumentException.class,
                () -> sut.updateCurrentUserProfile(KC_USER_ID, EMAIL, invalid));
        verify(userProfileRepository, never()).save(any());
    }

    @Test
    void calculateTargets_male_moderately_active_maintain_uses_correct_formula() {
        when(userProfileRepository.readByKeycloakUserId(KC_USER_ID)).thenReturn(Optional.empty());
        when(userProfileRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        UserProfile update = validUpdateProfile();
        update.setGender(Gender.MALE);
        update.setWeightKg(80.0);
        update.setHeightCm(180.0);
        update.setAge(30);
        update.setActivityLevel(ActivityLevel.MODERATELY_ACTIVE);
        update.setGoal(UserGoal.MAINTAIN_WEIGHT);

        UserProfile result = sut.updateCurrentUserProfile(KC_USER_ID, EMAIL, update);

        double expectedBmr = 10 * 80 + 6.25 * 180 - 5 * 30 + 5;
        double expectedTdee = expectedBmr * 1.55;
        assertEquals(Math.round(expectedBmr), result.getBmr().longValue());
        assertEquals(Math.round(expectedTdee), result.getTdee().longValue());
        assertEquals(Math.round(expectedTdee), result.getTargetCalories().longValue());
    }

    @Test
    void calculateTargets_female_sedentary_lose_weight_applies_deficit() {
        when(userProfileRepository.readByKeycloakUserId(KC_USER_ID)).thenReturn(Optional.empty());
        when(userProfileRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        UserProfile update = validUpdateProfile();
        update.setGender(Gender.FEMALE);
        update.setWeightKg(60.0);
        update.setHeightCm(165.0);
        update.setAge(28);
        update.setActivityLevel(ActivityLevel.SEDENTARY);
        update.setGoal(UserGoal.LOSE_WEIGHT);

        UserProfile result = sut.updateCurrentUserProfile(KC_USER_ID, EMAIL, update);

        double expectedBmr = 10 * 60 + 6.25 * 165 - 5 * 28 - 161;
        double expectedTdee = expectedBmr * 1.2;
        double expectedTargetCalories = expectedTdee * 0.85;

        assertEquals(Math.round(expectedBmr), result.getBmr().longValue());
        assertEquals(Math.round(expectedTargetCalories), result.getTargetCalories().longValue());
        assertEquals(Math.round(60.0 * 2.0), result.getTargetProtein().longValue());
        assertEquals(Math.round(60.0 * 0.8), result.getTargetFat().longValue());
    }

    @Test
    void calculateTargets_gain_mass_applies_surplus() {
        when(userProfileRepository.readByKeycloakUserId(KC_USER_ID)).thenReturn(Optional.empty());
        when(userProfileRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        UserProfile update = validUpdateProfile();
        update.setGoal(UserGoal.GAIN_MASS);
        update.setActivityLevel(ActivityLevel.VERY_ACTIVE);

        UserProfile result = sut.updateCurrentUserProfile(KC_USER_ID, EMAIL, update);

        assertTrue(result.getTargetCalories() > result.getTdee() * 0.99);
        assertEquals(Math.round(update.getWeightKg() * 1.8), result.getTargetProtein().longValue());
        assertEquals(Math.round(update.getWeightKg() * 1.0), result.getTargetFat().longValue());
    }

    @Test
    void calculateTargets_carbs_are_never_negative() {
        when(userProfileRepository.readByKeycloakUserId(KC_USER_ID)).thenReturn(Optional.empty());
        when(userProfileRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        UserProfile update = validUpdateProfile();
        update.setWeightKg(120.0);
        update.setGoal(UserGoal.LOSE_WEIGHT);
        update.setActivityLevel(ActivityLevel.SEDENTARY);

        UserProfile result = sut.updateCurrentUserProfile(KC_USER_ID, EMAIL, update);

        assertTrue(result.getTargetCarbohydrates() >= 0);
    }

    private UserProfile profile() {
        UserProfile p = new UserProfile();
        p.setKeycloakUserId(KC_USER_ID);
        p.setEmail(EMAIL);
        return p;
    }

    private UserProfile validUpdateProfile() {
        UserProfile p = new UserProfile();
        p.setAge(25);
        p.setHeightCm(175.0);
        p.setWeightKg(75.0);
        p.setGender(Gender.MALE);
        p.setActivityLevel(ActivityLevel.MODERATELY_ACTIVE);
        p.setGoal(UserGoal.MAINTAIN_WEIGHT);
        return p;
    }
}
