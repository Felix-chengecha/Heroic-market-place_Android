package Network;

public class Base_url {

    private static String mainUrl = "https://55c7-105-161-180-171.in.ngrok.io/";


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

}
