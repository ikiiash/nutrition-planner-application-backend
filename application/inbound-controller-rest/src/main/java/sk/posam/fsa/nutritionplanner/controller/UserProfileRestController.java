package sk.posam.fsa.nutritionplanner.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import sk.posam.fsa.nutritionplanner.domain.userprofile.UserProfile;
import sk.posam.fsa.nutritionplanner.domain.userprofile.service.UserProfileFacade;
import sk.posam.fsa.nutritionplanner.mapper.UserProfileMapper;
import sk.posam.fsa.nutritionplanner.rest.api.UserProfileApi;
import sk.posam.fsa.nutritionplanner.rest.dto.UpdateUserProfileRequestDto;
import sk.posam.fsa.nutritionplanner.rest.dto.UserProfileDto;
import sk.posam.fsa.nutritionplanner.security.CurrentUserProvider;

@RestController
public class UserProfileRestController implements UserProfileApi {

    private final UserProfileFacade userProfileFacade;
    private final UserProfileMapper userProfileMapper;
    private final CurrentUserProvider currentUserProvider;

    public UserProfileRestController(UserProfileFacade userProfileFacade,
                                     UserProfileMapper userProfileMapper,
                                     CurrentUserProvider currentUserProvider) {
        this.userProfileFacade = userProfileFacade;
        this.userProfileMapper = userProfileMapper;
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    public ResponseEntity<UserProfileDto> readCurrentUserProfile() {
        UserProfile userProfile = userProfileFacade.readCurrentUserProfile(
                currentUserProvider.getUserId(),
                currentUserProvider.getEmail()
        );
        return ResponseEntity.ok(userProfileMapper.toDto(userProfile));
    }

    @Override
    public ResponseEntity<UserProfileDto> updateCurrentUserProfile(UpdateUserProfileRequestDto updateUserProfileRequestDto) {
        UserProfile userProfile = userProfileMapper.toDomain(updateUserProfileRequestDto);
        UserProfile updatedProfile = userProfileFacade.updateCurrentUserProfile(
                currentUserProvider.getUserId(),
                currentUserProvider.getEmail(),
                userProfile
        );
        return ResponseEntity.ok(userProfileMapper.toDto(updatedProfile));
    }
}
