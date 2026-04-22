package sk.posam.fsa.nutritionplanner.domain.userprofile.service;

import sk.posam.fsa.nutritionplanner.domain.userprofile.UserProfile;

public interface UserProfileFacade {

    UserProfile readCurrentUserProfile(String keycloakUserId, String email);

    UserProfile updateCurrentUserProfile(String keycloakUserId, String email, UserProfile userProfile);
}
