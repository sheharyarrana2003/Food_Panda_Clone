package com.example.foodpanda;

public class CartItem {
    private String itemName;
    private String price;
    private int image;

    public CartItem() {
    }

    public CartItem(String itemName, String price, int image) {
        this.itemName = itemName;
        this.price = price;
        this.image = image;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
