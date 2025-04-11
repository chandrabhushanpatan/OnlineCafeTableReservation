// MenuItem.java
package model;

public class MenuItem {
    private int itemId;
    private int categoryId;
    private String itemName;
    private String description;
    private double price;
    private boolean isAvailable;

    public MenuItem(int itemId, int categoryId, String itemName, String description, double price, boolean isAvailable) {
        this.itemId = itemId;
        this.categoryId = categoryId;
        this.itemName = itemName;
        this.description = description;
        this.price = price;
        this.isAvailable = isAvailable;
    }

    // Getters and setters
    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public String getName() { return itemName; }
    public void setName(String name) { this.itemName = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
}