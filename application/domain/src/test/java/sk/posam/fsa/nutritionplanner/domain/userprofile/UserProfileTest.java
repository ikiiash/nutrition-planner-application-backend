package sk.posam.fsa.nutritionplanner.domain.userprofile;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserProfileTest {

    @Test
    void validateForUpdate_passes_for_valid_profile() {
        assertDoesNotThrow(() -> validProfile().validateForUpdate());
    }

    @Test
    void validateForUpdate_throws_when_keycloakUserId_is_null() {
        UserProfile p = validProfile();
        p.setKeycloakUserId(null);
        assertThrows(IllegalArgumentException.class, p::validateForUpdate);
    }

    @Test
    void validateForUpdate_throws_when_keycloakUserId_is_blank() {
        UserProfile p = validProfile();
        p.setKeycloakUserId("  ");
        assertThrows(IllegalArgumentException.class, p::validateForUpdate);
    }

    @Test
    void validateForUpdate_throws_when_age_is_null() {
        UserProfile p = validProfile();
        p.setAge(null);
        assertThrows(IllegalArgumentException.class, p::validateForUpdate);
    }

    @Test
    void validateForUpdate_throws_when_age_is_zero() {
        UserProfile p = validProfile();
        p.setAge(0);
        assertThrows(IllegalArgumentException.class, p::validateForUpdate);
    }

    @Test
    void validateForUpdate_throws_when_age_is_negative() {
        UserProfile p = validProfile();
        p.setAge(-1);
        assertThrows(IllegalArgumentException.class, p::validateForUpdate);
    }

    @Test
    void validateForUpdate_throws_when_height_is_null() {
        UserProfile p = validProfile();
        p.setHeightCm(null);
        assertThrows(IllegalArgumentException.class, p::validateForUpdate);
    }

    @Test
    void validateForUpdate_throws_when_height_is_zero() {
        UserProfile p = validProfile();
        p.setHeightCm(0.0);
        assertThrows(IllegalArgumentException.class, p::validateForUpdate);
    }

    @Test
    void validateForUpdate_throws_when_weight_is_null() {
        UserProfile p = validProfile();
        p.setWeightKg(null);
        assertThrows(IllegalArgumentException.class, p::validateForUpdate);
    }

    @Test
    void validateForUpdate_throws_when_weight_is_zero() {
        UserProfile p = validProfile();
        p.setWeightKg(0.0);
        assertThrows(IllegalArgumentException.class, p::validateForUpdate);
    }

    @Test
    void validateForUpdate_throws_when_gender_is_null() {
        UserProfile p = validProfile();
        p.setGender(null);
        assertThrows(IllegalArgumentException.class, p::validateForUpdate);
    }

    @Test
    void validateForUpdate_throws_when_activityLevel_is_null() {
        UserProfile p = validProfile();
        p.setActivityLevel(null);
        assertThrows(IllegalArgumentException.class, p::validateForUpdate);
    }

    @Test
    void validateForUpdate_throws_when_goal_is_null() {
        UserProfile p = validProfile();
        p.setGoal(null);
        assertThrows(IllegalArgumentException.class, p::validateForUpdate);
    }

    @Test
    void equals_is_based_on_id_and_keycloakUserId() {
        UserProfile a = new UserProfile();
        a.setId(1L);
        a.setKeycloakUserId("kc-1");

        UserProfile b = new UserProfile();
        b.setId(1L);
        b.setKeycloakUserId("kc-1");

        assertEquals(a, b);
    }

    @Test
    void profiles_with_different_ids_are_not_equal() {
        UserProfile a = new UserProfile();
        a.setId(1L);
        a.setKeycloakUserId("kc-1");

        UserProfile b = new UserProfile();
        b.setId(2L);
        b.setKeycloakUserId("kc-1");

        assertNotEquals(a, b);
    }

    private UserProfile validProfile() {
        UserProfile p = new UserProfile();
        p.setKeycloakUserId("kc-user-1");
        p.setAge(25);
        p.setHeightCm(175.0);
        p.setWeightKg(75.0);
        p.setGender(Gender.MALE);
        p.setActivityLevel(ActivityLevel.MODERATELY_ACTIVE);
        p.setGoal(UserGoal.MAINTAIN_WEIGHT);
        return p;
    }
}
