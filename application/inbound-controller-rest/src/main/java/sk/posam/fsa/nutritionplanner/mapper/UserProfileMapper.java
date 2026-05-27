package sk.posam.fsa.nutritionplanner.mapper;

import org.springframework.stereotype.Component;
import sk.posam.fsa.nutritionplanner.domain.userprofile.ActivityLevel;
import sk.posam.fsa.nutritionplanner.domain.userprofile.Gender;
import sk.posam.fsa.nutritionplanner.domain.userprofile.UserGoal;
import sk.posam.fsa.nutritionplanner.domain.userprofile.UserProfile;
import sk.posam.fsa.nutritionplanner.rest.dto.ActivityLevelDto;
import sk.posam.fsa.nutritionplanner.rest.dto.GenderDto;
import sk.posam.fsa.nutritionplanner.rest.dto.UserGoalDto;
import sk.posam.fsa.nutritionplanner.rest.dto.UpdateUserProfileRequestDto;
import sk.posam.fsa.nutritionplanner.rest.dto.UserProfileDto;

@Component
public class UserProfileMapper {

    public UserProfile toDomain(UpdateUserProfileRequestDto dto) {
        UserProfile p = new UserProfile();
        p.setNickname(dto.getNickname());
        p.setFirstName(dto.getFirstName());
        p.setAge(dto.getAge());
        p.setHeightCm(dto.getHeightCm());
        p.setWeightKg(dto.getWeightKg());
        if (dto.getGender() != null) {
            p.setGender(Gender.valueOf(dto.getGender().getValue()));
        }
        if (dto.getActivityLevel() != null) {
            p.setActivityLevel(ActivityLevel.valueOf(dto.getActivityLevel().getValue()));
        }
        if (dto.getGoal() != null) {
            p.setGoal(UserGoal.valueOf(dto.getGoal().getValue()));
        }
        return p;
    }

    public UserProfileDto toDto(UserProfile p) {
        UserProfileDto dto = new UserProfileDto();
        dto.setId(p.getId());
        dto.setEmail(p.getEmail());
        dto.setNickname(p.getNickname());
        dto.setFirstName(p.getFirstName());
        dto.setAge(p.getAge());
        dto.setHeightCm(p.getHeightCm());
        dto.setWeightKg(p.getWeightKg());
        if (p.getGender() != null) {
            dto.setGender(GenderDto.fromValue(p.getGender().name()));
        }
        if (p.getActivityLevel() != null) {
            dto.setActivityLevel(ActivityLevelDto.fromValue(p.getActivityLevel().name()));
        }
        if (p.getGoal() != null) {
            dto.setGoal(UserGoalDto.fromValue(p.getGoal().name()));
        }
        dto.setBmr(p.getBmr());
        dto.setTdee(p.getTdee());
        dto.setTargetCalories(p.getTargetCalories());
        dto.setTargetProtein(p.getTargetProtein());
        dto.setTargetFat(p.getTargetFat());
        dto.setTargetCarbohydrates(p.getTargetCarbohydrates());
        return dto;
    }
}
