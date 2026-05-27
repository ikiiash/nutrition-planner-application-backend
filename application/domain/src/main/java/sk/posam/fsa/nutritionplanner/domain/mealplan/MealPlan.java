package sk.posam.fsa.nutritionplanner.domain.mealplan;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MealPlan {

    private Long id;
    private String ownerUserId;
    private String name;
    private LocalDate startDate;
    private Integer numberOfDays;
    private List<PlanDay> days = new ArrayList<>();
    private boolean active = false;
    private LocalDate activatedAt;
    private int lastDeductedDayNumber = 0;

    public MealPlan() {
    }

    public void validate() {
        if (ownerUserId == null || ownerUserId.isBlank()) {
            throw new IllegalArgumentException("Meal plan owner must not be blank.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Meal plan name must not be blank.");
        }
        if (startDate == null) {
            throw new IllegalArgumentException("Meal plan start date must not be null.");
        }
        if (numberOfDays == null || numberOfDays < 1 || numberOfDays > 30) {
            throw new IllegalArgumentException("Number of days must be between 1 and 30.");
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOwnerUserId() { return ownerUserId; }
    public void setOwnerUserId(String ownerUserId) { this.ownerUserId = ownerUserId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public Integer getNumberOfDays() { return numberOfDays; }
    public void setNumberOfDays(Integer numberOfDays) { this.numberOfDays = numberOfDays; }

    public List<PlanDay> getDays() { return days; }
    public void setDays(List<PlanDay> days) { this.days = days; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public LocalDate getActivatedAt() { return activatedAt; }
    public void setActivatedAt(LocalDate activatedAt) { this.activatedAt = activatedAt; }

    public int getLastDeductedDayNumber() { return lastDeductedDayNumber; }
    public void setLastDeductedDayNumber(int lastDeductedDayNumber) { this.lastDeductedDayNumber = lastDeductedDayNumber; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MealPlan that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(ownerUserId, that.ownerUserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ownerUserId);
    }
}
