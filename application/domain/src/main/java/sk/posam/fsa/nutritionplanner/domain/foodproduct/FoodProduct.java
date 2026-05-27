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

    private Double sodiumMg;
    private Double potassiumMg;
    private Double magnesiumMg;
    private Double ironMg;
    private Double calciumMg;
    private Double zincMg;
    private Double vitaminAMcg;
    private Double vitaminCMg;
    private Double vitaminDMcg;
    private Double vitaminEMg;
    private Double vitaminKMcg;
    private Double vitaminB1Mg;
    private Double vitaminB2Mg;
    private Double vitaminB6Mg;
    private Double vitaminB9Mcg;
    private Double vitaminB12Mcg;

    private boolean inFridge;
    private Double fridgeGrams;

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

    public Double getSodiumMg() { return sodiumMg; }
    public void setSodiumMg(Double sodiumMg) { this.sodiumMg = sodiumMg; }

    public Double getPotassiumMg() { return potassiumMg; }
    public void setPotassiumMg(Double potassiumMg) { this.potassiumMg = potassiumMg; }

    public Double getMagnesiumMg() { return magnesiumMg; }
    public void setMagnesiumMg(Double magnesiumMg) { this.magnesiumMg = magnesiumMg; }

    public Double getIronMg() { return ironMg; }
    public void setIronMg(Double ironMg) { this.ironMg = ironMg; }

    public Double getCalciumMg() { return calciumMg; }
    public void setCalciumMg(Double calciumMg) { this.calciumMg = calciumMg; }

    public Double getZincMg() { return zincMg; }
    public void setZincMg(Double zincMg) { this.zincMg = zincMg; }

    public Double getVitaminAMcg() { return vitaminAMcg; }
    public void setVitaminAMcg(Double vitaminAMcg) { this.vitaminAMcg = vitaminAMcg; }

    public Double getVitaminCMg() { return vitaminCMg; }
    public void setVitaminCMg(Double vitaminCMg) { this.vitaminCMg = vitaminCMg; }

    public Double getVitaminDMcg() { return vitaminDMcg; }
    public void setVitaminDMcg(Double vitaminDMcg) { this.vitaminDMcg = vitaminDMcg; }

    public Double getVitaminEMg() { return vitaminEMg; }
    public void setVitaminEMg(Double vitaminEMg) { this.vitaminEMg = vitaminEMg; }

    public Double getVitaminKMcg() { return vitaminKMcg; }
    public void setVitaminKMcg(Double vitaminKMcg) { this.vitaminKMcg = vitaminKMcg; }

    public Double getVitaminB1Mg() { return vitaminB1Mg; }
    public void setVitaminB1Mg(Double vitaminB1Mg) { this.vitaminB1Mg = vitaminB1Mg; }

    public Double getVitaminB2Mg() { return vitaminB2Mg; }
    public void setVitaminB2Mg(Double vitaminB2Mg) { this.vitaminB2Mg = vitaminB2Mg; }

    public Double getVitaminB6Mg() { return vitaminB6Mg; }
    public void setVitaminB6Mg(Double vitaminB6Mg) { this.vitaminB6Mg = vitaminB6Mg; }

    public Double getVitaminB9Mcg() { return vitaminB9Mcg; }
    public void setVitaminB9Mcg(Double vitaminB9Mcg) { this.vitaminB9Mcg = vitaminB9Mcg; }

    public Double getVitaminB12Mcg() { return vitaminB12Mcg; }
    public void setVitaminB12Mcg(Double vitaminB12Mcg) { this.vitaminB12Mcg = vitaminB12Mcg; }

    public boolean isInFridge() { return inFridge; }
    public void setInFridge(boolean inFridge) { this.inFridge = inFridge; }

    public Double getFridgeGrams() { return fridgeGrams; }
    public void setFridgeGrams(Double fridgeGrams) { this.fridgeGrams = fridgeGrams; }

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
