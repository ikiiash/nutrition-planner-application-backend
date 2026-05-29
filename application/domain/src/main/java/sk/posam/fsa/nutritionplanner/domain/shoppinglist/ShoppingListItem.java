package sk.posam.fsa.nutritionplanner.domain.shoppinglist;

public class ShoppingListItem {
    private Long id;

    public static ShoppingListItem of(Long foodProductId, String foodProductName, double grams,
                                       double caloriesPer100g, double proteinPer100g, double fatPer100g,
                                       double carbsPer100g, double pricePer100g) {
        ShoppingListItem item = new ShoppingListItem();
        item.foodProductId = foodProductId;
        item.foodProductName = foodProductName;
        item.grams = grams;
        item.caloriesPer100g = caloriesPer100g;
        item.proteinPer100g = proteinPer100g;
        item.fatPer100g = fatPer100g;
        item.carbsPer100g = carbsPer100g;
        item.pricePer100g = pricePer100g;
        return item;
    }

    private String ownerUserId;
    private Long foodProductId;
    private String foodProductName;
    private double grams;
    private double caloriesPer100g;
    private double proteinPer100g;
    private double fatPer100g;
    private double carbsPer100g;
    private double pricePer100g;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOwnerUserId() { return ownerUserId; }
    public void setOwnerUserId(String ownerUserId) { this.ownerUserId = ownerUserId; }
    public Long getFoodProductId() { return foodProductId; }
    public void setFoodProductId(Long foodProductId) { this.foodProductId = foodProductId; }
    public String getFoodProductName() { return foodProductName; }
    public void setFoodProductName(String foodProductName) { this.foodProductName = foodProductName; }
    public double getGrams() { return grams; }
    public void setGrams(double grams) { this.grams = grams; }
    public double getCaloriesPer100g() { return caloriesPer100g; }
    public void setCaloriesPer100g(double caloriesPer100g) { this.caloriesPer100g = caloriesPer100g; }
    public double getProteinPer100g() { return proteinPer100g; }
    public void setProteinPer100g(double proteinPer100g) { this.proteinPer100g = proteinPer100g; }
    public double getFatPer100g() { return fatPer100g; }
    public void setFatPer100g(double fatPer100g) { this.fatPer100g = fatPer100g; }
    public double getCarbsPer100g() { return carbsPer100g; }
    public void setCarbsPer100g(double carbsPer100g) { this.carbsPer100g = carbsPer100g; }
    public double getPricePer100g() { return pricePer100g; }
    public void setPricePer100g(double pricePer100g) { this.pricePer100g = pricePer100g; }
}
