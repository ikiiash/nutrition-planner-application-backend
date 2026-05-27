package sk.posam.fsa.nutritionplanner.domain.mealplan;

import java.util.Objects;

public class PlanEntry {

    private Long id;
    private MealType mealType;
    private EntryType entryType;

    private Long mealId;
    private String mealName;
    private Double portions;

    private Long foodProductId;
    private String foodProductName;
    private Double grams;

    private Double calories;
    private Double protein;
    private Double fat;
    private Double carbohydrates;
    private Double price;

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

    public PlanEntry() {
    }

    public void validate() {
        if (mealType == null) {
            throw new IllegalArgumentException("Meal type must not be null.");
        }
        if (entryType == null) {
            throw new IllegalArgumentException("Entry type must not be null.");
        }
        if (entryType == EntryType.MEAL) {
            if (mealId == null) {
                throw new IllegalArgumentException("Meal id must be provided for MEAL entry type.");
            }
            if (portions == null || portions <= 0) {
                throw new IllegalArgumentException("Portions must be greater than zero for MEAL entry type.");
            }
        } else {
            if (foodProductId == null) {
                throw new IllegalArgumentException("Food product id must be provided for FOOD_PRODUCT entry type.");
            }
            if (grams == null || grams <= 0) {
                throw new IllegalArgumentException("Grams must be greater than zero for FOOD_PRODUCT entry type.");
            }
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public MealType getMealType() { return mealType; }
    public void setMealType(MealType mealType) { this.mealType = mealType; }

    public EntryType getEntryType() { return entryType; }
    public void setEntryType(EntryType entryType) { this.entryType = entryType; }

    public Long getMealId() { return mealId; }
    public void setMealId(Long mealId) { this.mealId = mealId; }

    public String getMealName() { return mealName; }
    public void setMealName(String mealName) { this.mealName = mealName; }

    public Double getPortions() { return portions; }
    public void setPortions(Double portions) { this.portions = portions; }

    public Long getFoodProductId() { return foodProductId; }
    public void setFoodProductId(Long foodProductId) { this.foodProductId = foodProductId; }

    public String getFoodProductName() { return foodProductName; }
    public void setFoodProductName(String foodProductName) { this.foodProductName = foodProductName; }

    public Double getGrams() { return grams; }
    public void setGrams(Double grams) { this.grams = grams; }

    public Double getCalories() { return calories; }
    public void setCalories(Double calories) { this.calories = calories; }

    public Double getProtein() { return protein; }
    public void setProtein(Double protein) { this.protein = protein; }

    public Double getFat() { return fat; }
    public void setFat(Double fat) { this.fat = fat; }

    public Double getCarbohydrates() { return carbohydrates; }
    public void setCarbohydrates(Double carbohydrates) { this.carbohydrates = carbohydrates; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Double getSodiumMg() { return sodiumMg; }
    public void setSodiumMg(Double v) { this.sodiumMg = v; }

    public Double getPotassiumMg() { return potassiumMg; }
    public void setPotassiumMg(Double v) { this.potassiumMg = v; }

    public Double getMagnesiumMg() { return magnesiumMg; }
    public void setMagnesiumMg(Double v) { this.magnesiumMg = v; }

    public Double getIronMg() { return ironMg; }
    public void setIronMg(Double v) { this.ironMg = v; }

    public Double getCalciumMg() { return calciumMg; }
    public void setCalciumMg(Double v) { this.calciumMg = v; }

    public Double getZincMg() { return zincMg; }
    public void setZincMg(Double v) { this.zincMg = v; }

    public Double getVitaminAMcg() { return vitaminAMcg; }
    public void setVitaminAMcg(Double v) { this.vitaminAMcg = v; }

    public Double getVitaminCMg() { return vitaminCMg; }
    public void setVitaminCMg(Double v) { this.vitaminCMg = v; }

    public Double getVitaminDMcg() { return vitaminDMcg; }
    public void setVitaminDMcg(Double v) { this.vitaminDMcg = v; }

    public Double getVitaminEMg() { return vitaminEMg; }
    public void setVitaminEMg(Double v) { this.vitaminEMg = v; }

    public Double getVitaminKMcg() { return vitaminKMcg; }
    public void setVitaminKMcg(Double v) { this.vitaminKMcg = v; }

    public Double getVitaminB1Mg() { return vitaminB1Mg; }
    public void setVitaminB1Mg(Double v) { this.vitaminB1Mg = v; }

    public Double getVitaminB2Mg() { return vitaminB2Mg; }
    public void setVitaminB2Mg(Double v) { this.vitaminB2Mg = v; }

    public Double getVitaminB6Mg() { return vitaminB6Mg; }
    public void setVitaminB6Mg(Double v) { this.vitaminB6Mg = v; }

    public Double getVitaminB9Mcg() { return vitaminB9Mcg; }
    public void setVitaminB9Mcg(Double v) { this.vitaminB9Mcg = v; }

    public Double getVitaminB12Mcg() { return vitaminB12Mcg; }
    public void setVitaminB12Mcg(Double v) { this.vitaminB12Mcg = v; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlanEntry that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
