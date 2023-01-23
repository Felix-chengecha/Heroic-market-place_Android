package com.example.heroicmarketplace.Cart;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CartModel {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name ="prod_id")
    public String prod_id;

    @ColumnInfo(name="Title")
    public String title;

    @ColumnInfo(name="image")
    public String image;

    @ColumnInfo(name="Cost")
    public int cost;

    @ColumnInfo(name="quantity")
    public int qnty;


    public CartModel( String prod_id, String title, String image, int cost, int qnty) {
        this.id = id;
        this.prod_id = prod_id;
        this.title = title;
        this.image = image;
        this.cost = cost;
        this.qnty = qnty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProd_id() {
        return prod_id;
    }

    public void setProd_id(String prod_id) {
        this.prod_id = prod_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getQnty() {
        return qnty;
    }

    public void setQnty(int qnty) {
        this.qnty = qnty;
    }
}
