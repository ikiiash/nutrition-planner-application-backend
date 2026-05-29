package sk.posam.fsa.nutritionplanner.domain.userprofile;

import java.util.Objects;

public class UserProfile {

    private Long id;
    private String keycloakUserId;
    private String email;
    private String nickname;
    private String firstName;
    private Gender gender;
    private Integer age;
    private Double heightCm;
    private Double weightKg;
    private ActivityLevel activityLevel;
    private UserGoal goal;
    private Double bmr;
    private Double tdee;
    private Double targetCalories;
    private Double targetProtein;
    private Double targetFat;
    private Double targetCarbohydrates;

    public UserProfile() {
    }

    public static UserProfile of(String nickname, String firstName, Integer age,
                                   Double heightCm, Double weightKg,
                                   Gender gender, ActivityLevel activityLevel, UserGoal goal) {
        UserProfile p = new UserProfile();
        p.nickname = nickname;
        p.firstName = firstName;
        p.age = age;
        p.heightCm = heightCm;
        p.weightKg = weightKg;
        p.gender = gender;
        p.activityLevel = activityLevel;
        p.goal = goal;
        return p;
    }

    public void validateForUpdate() {
        if (keycloakUserId == null || keycloakUserId.isBlank()) {
            throw new IllegalArgumentException("User profile owner must not be blank.");
        }
        if (age == null || age <= 0) {
            throw new IllegalArgumentException("Age must be greater than zero.");
        }
        if (heightCm == null || heightCm <= 0) {
            throw new IllegalArgumentException("Height must be greater than zero.");
        }
        if (weightKg == null || weightKg <= 0) {
            throw new IllegalArgumentException("Weight must be greater than zero.");
        }
        if (gender == null) {
            throw new IllegalArgumentException("Gender must be specified.");
        }
        if (activityLevel == null) {
            throw new IllegalArgumentException("Activity level must be specified.");
        }
        if (goal == null) {
            throw new IllegalArgumentException("User goal must be specified.");
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getKeycloakUserId() { return keycloakUserId; }
    public void setKeycloakUserId(String keycloakUserId) { this.keycloakUserId = keycloakUserId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public Double getHeightCm() { return heightCm; }
    public void setHeightCm(Double heightCm) { this.heightCm = heightCm; }

    public Double getWeightKg() { return weightKg; }
    public void setWeightKg(Double weightKg) { this.weightKg = weightKg; }

    public ActivityLevel getActivityLevel() { return activityLevel; }
    public void setActivityLevel(ActivityLevel activityLevel) { this.activityLevel = activityLevel; }

    public UserGoal getGoal() { return goal; }
    public void setGoal(UserGoal goal) { this.goal = goal; }

    public Double getBmr() { return bmr; }
    public void setBmr(Double bmr) { this.bmr = bmr; }

    public Double getTdee() { return tdee; }
    public void setTdee(Double tdee) { this.tdee = tdee; }

    public Double getTargetCalories() { return targetCalories; }
    public void setTargetCalories(Double targetCalories) { this.targetCalories = targetCalories; }

    public Double getTargetProtein() { return targetProtein; }
    public void setTargetProtein(Double targetProtein) { this.targetProtein = targetProtein; }

    public Double getTargetFat() { return targetFat; }
    public void setTargetFat(Double targetFat) { this.targetFat = targetFat; }

    public Double getTargetCarbohydrates() { return targetCarbohydrates; }
    public void setTargetCarbohydrates(Double targetCarbohydrates) { this.targetCarbohydrates = targetCarbohydrates; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserProfile that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(keycloakUserId, that.keycloakUserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, keycloakUserId);
    }
}
