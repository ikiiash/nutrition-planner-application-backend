package sk.posam.fsa.nutritionplanner.domain.meal;

import java.util.List;
import java.util.Objects;

public class Meal {

    private Long id;
    private String ownerUserId;
    private String name;
    private Integer servings;
    private List<MealIngredient> ingredients;

    public Meal() {
    }

    public void validate() {
        if (ownerUserId == null || ownerUserId.isBlank()) {
            throw new IllegalArgumentException("Meal owner must not be blank.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Meal name must not be blank.");
        }
        if (servings == null || servings < 1) {
            throw new IllegalArgumentException("Servings must be at least 1.");
        }
        if (ingredients == null || ingredients.isEmpty()) {
            throw new IllegalArgumentException("Meal must have at least one ingredient.");
        }
        for (MealIngredient ingredient : ingredients) {
            ingredient.validate();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(String ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public List<MealIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<MealIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Meal that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(ownerUserId, that.ownerUserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ownerUserId);
    }
}
