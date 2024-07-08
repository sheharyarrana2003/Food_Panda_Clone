package com.example.foodpanda;

import java.io.Serializable;

public class Item implements Serializable {
    String name,cost,description,location;
    int image;
    public Item(String name,String cost,int image,String description,String location){
        this.name=name;
        this.cost=cost;
        this.image=image;
        this.description=description;
        this.location=location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
