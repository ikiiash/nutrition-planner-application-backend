package sk.posam.fsa.nutritionplanner.domain.mealplan;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlanDay {

    private Long id;
    private Integer dayNumber;
    private List<PlanEntry> entries = new ArrayList<>();

    public PlanDay() {
    }

    public PlanDay(Integer dayNumber) {
        this.dayNumber = dayNumber;
        this.entries = new ArrayList<>();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getDayNumber() { return dayNumber; }
    public void setDayNumber(Integer dayNumber) { this.dayNumber = dayNumber; }

    public List<PlanEntry> getEntries() { return entries; }
    public void setEntries(List<PlanEntry> entries) { this.entries = entries; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlanDay that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
