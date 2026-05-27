package sk.posam.fsa.nutritionplanner.domain.meal;

import java.util.Objects;

public class MealIngredient {

    private Long id;
    private Long foodProductId;
    private String foodProductName;
    private Double grams;
    private Double caloriesPerGram;
    private Double proteinPerGram;
    private Double fatPerGram;
    private Double carbohydratesPerGram;
    private Double pricePerGram;

    private Double sodiumMgPerGram;
    private Double potassiumMgPerGram;
    private Double magnesiumMgPerGram;
    private Double ironMgPerGram;
    private Double calciumMgPerGram;
    private Double zincMgPerGram;
    private Double vitaminAMcgPerGram;
    private Double vitaminCMgPerGram;
    private Double vitaminDMcgPerGram;
    private Double vitaminEMgPerGram;
    private Double vitaminKMcgPerGram;
    private Double vitaminB1MgPerGram;
    private Double vitaminB2MgPerGram;
    private Double vitaminB6MgPerGram;
    private Double vitaminB9McgPerGram;
    private Double vitaminB12McgPerGram;

    public MealIngredient() {
    }

    public void validate() {
        if (foodProductId == null) {
            throw new IllegalArgumentException("Ingredient food product ID must not be null.");
        }
        if (grams == null || grams <= 0) {
            throw new IllegalArgumentException("Ingredient grams must be greater than zero.");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFoodProductId() {
        return foodProductId;
    }

    public void setFoodProductId(Long foodProductId) {
        this.foodProductId = foodProductId;
    }

    public String getFoodProductName() {
        return foodProductName;
    }

    public void setFoodProductName(String foodProductName) {
        this.foodProductName = foodProductName;
    }

    public Double getGrams() {
        return grams;
    }

    public void setGrams(Double grams) {
        this.grams = grams;
    }

    public Double getCaloriesPerGram() {
        return caloriesPerGram;
    }

    public void setCaloriesPerGram(Double caloriesPerGram) {
        this.caloriesPerGram = caloriesPerGram;
    }

    public Double getProteinPerGram() {
        return proteinPerGram;
    }

    public void setProteinPerGram(Double proteinPerGram) {
        this.proteinPerGram = proteinPerGram;
    }

    public Double getFatPerGram() {
        return fatPerGram;
    }

    public void setFatPerGram(Double fatPerGram) {
        this.fatPerGram = fatPerGram;
    }

    public Double getCarbohydratesPerGram() {
        return carbohydratesPerGram;
    }

    public void setCarbohydratesPerGram(Double carbohydratesPerGram) {
        this.carbohydratesPerGram = carbohydratesPerGram;
    }

    public Double getPricePerGram() {
        return pricePerGram;
    }

    public void setPricePerGram(Double pricePerGram) {
        this.pricePerGram = pricePerGram;
    }

    public Double getSodiumMgPerGram() { return sodiumMgPerGram; }
    public void setSodiumMgPerGram(Double v) { this.sodiumMgPerGram = v; }

    public Double getPotassiumMgPerGram() { return potassiumMgPerGram; }
    public void setPotassiumMgPerGram(Double v) { this.potassiumMgPerGram = v; }

    public Double getMagnesiumMgPerGram() { return magnesiumMgPerGram; }
    public void setMagnesiumMgPerGram(Double v) { this.magnesiumMgPerGram = v; }

    public Double getIronMgPerGram() { return ironMgPerGram; }
    public void setIronMgPerGram(Double v) { this.ironMgPerGram = v; }

    public Double getCalciumMgPerGram() { return calciumMgPerGram; }
    public void setCalciumMgPerGram(Double v) { this.calciumMgPerGram = v; }

    public Double getZincMgPerGram() { return zincMgPerGram; }
    public void setZincMgPerGram(Double v) { this.zincMgPerGram = v; }

    public Double getVitaminAMcgPerGram() { return vitaminAMcgPerGram; }
    public void setVitaminAMcgPerGram(Double v) { this.vitaminAMcgPerGram = v; }

    public Double getVitaminCMgPerGram() { return vitaminCMgPerGram; }
    public void setVitaminCMgPerGram(Double v) { this.vitaminCMgPerGram = v; }

    public Double getVitaminDMcgPerGram() { return vitaminDMcgPerGram; }
    public void setVitaminDMcgPerGram(Double v) { this.vitaminDMcgPerGram = v; }

    public Double getVitaminEMgPerGram() { return vitaminEMgPerGram; }
    public void setVitaminEMgPerGram(Double v) { this.vitaminEMgPerGram = v; }

    public Double getVitaminKMcgPerGram() { return vitaminKMcgPerGram; }
    public void setVitaminKMcgPerGram(Double v) { this.vitaminKMcgPerGram = v; }

    public Double getVitaminB1MgPerGram() { return vitaminB1MgPerGram; }
    public void setVitaminB1MgPerGram(Double v) { this.vitaminB1MgPerGram = v; }

    public Double getVitaminB2MgPerGram() { return vitaminB2MgPerGram; }
    public void setVitaminB2MgPerGram(Double v) { this.vitaminB2MgPerGram = v; }

    public Double getVitaminB6MgPerGram() { return vitaminB6MgPerGram; }
    public void setVitaminB6MgPerGram(Double v) { this.vitaminB6MgPerGram = v; }

    public Double getVitaminB9McgPerGram() { return vitaminB9McgPerGram; }
    public void setVitaminB9McgPerGram(Double v) { this.vitaminB9McgPerGram = v; }

    public Double getVitaminB12McgPerGram() { return vitaminB12McgPerGram; }
    public void setVitaminB12McgPerGram(Double v) { this.vitaminB12McgPerGram = v; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MealIngredient that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
