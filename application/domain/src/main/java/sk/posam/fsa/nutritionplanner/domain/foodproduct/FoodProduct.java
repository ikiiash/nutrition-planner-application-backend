package sk.posam.fsa.nutritionplanner.domain.foodproduct;

import java.util.Objects;

public class FoodProduct {

    private Long id;
    private String ownerUserId;
    private String name;
    private String category;
    private Double grams;
    private Double calories;
    private Double protein;
    private Double fat;
    private Double carbohydrates;
    private Double price;
    private String photoUrl;

    public FoodProduct() {
    }

    public FoodProduct(Long id,
                       String ownerUserId,
                       String name,
                       String category,
                       Double grams,
                       Double calories,
                       Double protein,
                       Double fat,
                       Double carbohydrates,
                       Double price,
                       String photoUrl) {
        this.id = id;
        this.ownerUserId = ownerUserId;
        this.name = name;
        this.category = category;
        this.grams = grams;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carbohydrates = carbohydrates;
        this.price = price;
        this.photoUrl = photoUrl;
    }

    public void validate() {
        if (ownerUserId == null || ownerUserId.isBlank()) {
            throw new IllegalArgumentException("Food product owner must not be blank.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Food product name must not be blank.");
        }
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("Food product category must not be blank.");
        }
        if (grams == null || grams <= 0) {
            throw new IllegalArgumentException("Grams must be greater than zero.");
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getGrams() {
        return grams;
    }

    public void setGrams(Double grams) {
        this.grams = grams;
    }

    public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public Double getProtein() {
        return protein;
    }

    public void setProtein(Double protein) {
        this.protein = protein;
    }

    public Double getFat() {
        return fat;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }

    public Double getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(Double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FoodProduct that)) return false;
        return Objects.equals(id, that.id)
                && Objects.equals(ownerUserId, that.ownerUserId)
                && Objects.equals(name, that.name)
                && Objects.equals(category, that.category)
                && Objects.equals(grams, that.grams)
                && Objects.equals(calories, that.calories)
                && Objects.equals(protein, that.protein)
                && Objects.equals(fat, that.fat)
                && Objects.equals(carbohydrates, that.carbohydrates)
                && Objects.equals(price, that.price)
                && Objects.equals(photoUrl, that.photoUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ownerUserId, name, category, grams, calories, protein, fat, carbohydrates, price, photoUrl);
    }
}
