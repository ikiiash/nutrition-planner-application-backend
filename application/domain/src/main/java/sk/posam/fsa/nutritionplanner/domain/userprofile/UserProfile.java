package sk.posam.fsa.nutritionplanner.domain.userprofile;

import java.util.Objects;

public class UserProfile {

    private Long id;
    private String keycloakUserId;
    private String email;
    private Integer age;
    private Double heightCm;
    private Double weightKg;
    private UserGoal goal;
    private Double targetCalories;
    private Double targetProtein;
    private Double targetFat;
    private Double targetCarbohydrates;

    public UserProfile() {
    }

    public UserProfile(Long id,
                       String keycloakUserId,
                       String email,
                       Integer age,
                       Double heightCm,
                       Double weightKg,
                       UserGoal goal,
                       Double targetCalories,
                       Double targetProtein,
                       Double targetFat,
                       Double targetCarbohydrates) {
        this.id = id;
        this.keycloakUserId = keycloakUserId;
        this.email = email;
        this.age = age;
        this.heightCm = heightCm;
        this.weightKg = weightKg;
        this.goal = goal;
        this.targetCalories = targetCalories;
        this.targetProtein = targetProtein;
        this.targetFat = targetFat;
        this.targetCarbohydrates = targetCarbohydrates;
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
        if (goal == null) {
            throw new IllegalArgumentException("User goal must be specified.");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeycloakUserId() {
        return keycloakUserId;
    }

    public void setKeycloakUserId(String keycloakUserId) {
        this.keycloakUserId = keycloakUserId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(Double heightCm) {
        this.heightCm = heightCm;
    }

    public Double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(Double weightKg) {
        this.weightKg = weightKg;
    }

    public UserGoal getGoal() {
        return goal;
    }

    public void setGoal(UserGoal goal) {
        this.goal = goal;
    }

    public Double getTargetCalories() {
        return targetCalories;
    }

    public void setTargetCalories(Double targetCalories) {
        this.targetCalories = targetCalories;
    }

    public Double getTargetProtein() {
        return targetProtein;
    }

    public void setTargetProtein(Double targetProtein) {
        this.targetProtein = targetProtein;
    }

    public Double getTargetFat() {
        return targetFat;
    }

    public void setTargetFat(Double targetFat) {
        this.targetFat = targetFat;
    }

    public Double getTargetCarbohydrates() {
        return targetCarbohydrates;
    }

    public void setTargetCarbohydrates(Double targetCarbohydrates) {
        this.targetCarbohydrates = targetCarbohydrates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserProfile that)) return false;
        return Objects.equals(id, that.id)
                && Objects.equals(keycloakUserId, that.keycloakUserId)
                && Objects.equals(email, that.email)
                && Objects.equals(age, that.age)
                && Objects.equals(heightCm, that.heightCm)
                && Objects.equals(weightKg, that.weightKg)
                && goal == that.goal
                && Objects.equals(targetCalories, that.targetCalories)
                && Objects.equals(targetProtein, that.targetProtein)
                && Objects.equals(targetFat, that.targetFat)
                && Objects.equals(targetCarbohydrates, that.targetCarbohydrates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, keycloakUserId, email, age, heightCm, weightKg, goal, targetCalories, targetProtein,
                targetFat, targetCarbohydrates);
    }
}
