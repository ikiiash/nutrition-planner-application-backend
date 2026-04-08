package sk.posam.fsa.nutritionplanner.domain.foodproduct;

import java.util.Objects;

public class FoodProduct {

    private Long id;
    private String name;
    private Double calories;
    private Double protein;
    private Double fat;
    private Double carbohydrates;
    private Double price;

    public FoodProduct(Long id,
                       String name,
                       Double calories,
                       Double protein,
                       Double fat,
                       Double carbohydrates,
                       Double price) {
        this.id = id;
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carbohydrates = carbohydrates;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getCalories() {
        return calories;
    }

    public Double getProtein() {
        return protein;
    }

    public Double getFat() {
        return fat;
    }

    public Double getCarbohydrates() {
        return carbohydrates;
    }

    public Double getPrice() {
        return price;
    }

    public void validate() {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Food product name must not be blank.");
        }
        if (calories == null || calories < 0) {
            throw new IllegalArgumentException("Calories must not be null or negative.");
        }
        if (protein == null || protein < 0) {
            throw new IllegalArgumentException("Protein must not be null or negative.");
        }
        if (fat == null || fat < 0) {
            throw new IllegalArgumentException("Fat must not be null or negative.");
        }
        if (carbohydrates == null || carbohydrates < 0) {
            throw new IllegalArgumentException("Carbohydrates must not be null or negative.");
        }
        if (price == null || price < 0) {
            throw new IllegalArgumentException("Price must not be null or negative.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FoodProduct that = (FoodProduct) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(calories, that.calories)
                && Objects.equals(protein, that.protein)
                && Objects.equals(fat, that.fat)
                && Objects.equals(carbohydrates, that.carbohydrates)
                && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, calories, protein, fat, carbohydrates, price);
    }
}