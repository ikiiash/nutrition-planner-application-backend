package sk.posam.fsa.nutritionplanner.mapper;

import org.springframework.stereotype.Component;
import sk.posam.fsa.nutritionplanner.domain.userprofile.UserProfile;
import sk.posam.fsa.nutritionplanner.rest.dto.UserGoalDto;
import sk.posam.fsa.nutritionplanner.rest.dto.UpdateUserProfileRequestDto;
import sk.posam.fsa.nutritionplanner.rest.dto.UserProfileDto;

@Component
public class UserProfileMapper {

    public UserProfile toDomain(UpdateUserProfileRequestDto requestDto) {
        return new UserProfile(
                null,
                null,
                null,
                requestDto.getAge(),
                requestDto.getHeightCm(),
                requestDto.getWeightKg(),
                requestDto.getGoal() == null ? null : sk.posam.fsa.nutritionplanner.domain.userprofile.UserGoal.valueOf(requestDto.getGoal().getValue()),
                null,
                null,
                null,
                null
        );
    }

    public UserProfileDto toDto(UserProfile userProfile) {
        UserProfileDto dto = new UserProfileDto();
        dto.setId(userProfile.getId());
        dto.setEmail(userProfile.getEmail());
        dto.setAge(userProfile.getAge());
        dto.setHeightCm(userProfile.getHeightCm());
        dto.setWeightKg(userProfile.getWeightKg());
        if (userProfile.getGoal() != null) {
            dto.setGoal(UserGoalDto.fromValue(userProfile.getGoal().name()));
        }
        dto.setTargetCalories(userProfile.getTargetCalories());
        dto.setTargetProtein(userProfile.getTargetProtein());
        dto.setTargetFat(userProfile.getTargetFat());
        dto.setTargetCarbohydrates(userProfile.getTargetCarbohydrates());
        return dto;
    }
}
