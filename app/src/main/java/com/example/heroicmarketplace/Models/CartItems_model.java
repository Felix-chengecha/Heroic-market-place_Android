package com.example.heroicmarketplace.Models;

public class CartItems_model {

    private String id;
    private  String image;
    private String title;
    private String cost;

    public CartItems_model(String id, String image, String title, String cost) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.cost = cost;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
