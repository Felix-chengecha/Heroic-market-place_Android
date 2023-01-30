package com.example.heroicmarketplace.Network;

public class Base_url {

    private static String mainUrl = "https://1156-105-61-238-65.in.ngrok.io/";


    public static String getallproducts() {
        return mainUrl + "api/category/items";
    }

    public static String getcategories() {
        return mainUrl + "api/all/category";
    }

    public static String productdetails() {
        return mainUrl + "api/details/product";
    }

    public static String searchproduct() {
        return mainUrl + "api/find/product";
    }

    public static String paynow(){
        return mainUrl + "api/payment";
    }

    public static String processpay(){return  mainUrl + "api/processpay";}

}
