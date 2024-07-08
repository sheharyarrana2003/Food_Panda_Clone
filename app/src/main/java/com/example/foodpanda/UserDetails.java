package com.example.foodpanda;

import android.media.Image;

public class UserDetails {
    String Name,UserName,Email,Location,TotalOrders;
    Image pic;

    public UserDetails() {
    }

    public UserDetails(String name, String userName, String email, String location, String totalOrders, Image pic) {
        Name = name;
        UserName = userName;
        Email = email;
        Location = location;
        TotalOrders = totalOrders;
        this.pic = pic;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getTotalOrders() {
        return TotalOrders;
    }

    public void setTotalOrders(String totalOrders) {
        TotalOrders = totalOrders;
    }

    public Image getPic() {
        return pic;
    }

    public void setPic(Image pic) {
        this.pic = pic;
    }
}
