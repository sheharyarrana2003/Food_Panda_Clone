package com.example.foodpanda;

public class deliveryItems {
    String itemName,itemPrice,isAvailable;

    public deliveryItems(String itemName, String itemPrice, String isAvailable) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.isAvailable = isAvailable;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(String isAvailable) {
        this.isAvailable = isAvailable;
    }
}
