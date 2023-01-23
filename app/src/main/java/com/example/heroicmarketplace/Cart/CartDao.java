package com.example.heroicmarketplace.Cart;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CartDao {

    @Insert
    void insertcart(CartModel cart);


    @Query("SELECT EXISTS(SELECT * FROM CartModel WHERE prod_id = :productid)")
    Boolean is_exist(int productid);


    @Query("SELECT * FROM CartModel")
    List<CartModel> getallcart();

    @Query("DELETE FROM CartModel WHERE id = :id")
    void deleteById(int id);
}
