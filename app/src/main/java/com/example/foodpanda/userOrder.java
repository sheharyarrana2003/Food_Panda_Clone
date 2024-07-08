package com.example.foodpanda;

import java.util.List;

public class userOrder {
    String phoneNumber;
    int totalPayment;
    List<deliveryItems> items;

    public userOrder(String phoneNumber, List<deliveryItems> items,int totalPayment) {
        this.phoneNumber = phoneNumber;
        this.items = items;
        this.totalPayment=totalPayment;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(int totalPayment) {
        this.totalPayment = totalPayment;
    }

    public List<deliveryItems> getItems() {
        return items;
    }

    public void setItems(List<deliveryItems> items) {
        this.items = items;
    }
}
