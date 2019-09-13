package managment.protege.supermed.Constant;

public class Constantapp {

    /**
     * -------------------- EDIT THIS WITH YOURS -------------------------------------------------
     */

    // Edit WEB_URL with your url. Make sure you have backslash('/') in the end url
//    public static String WEB_URL = "http://66.171.248.10:8082/";
    //public static String WEB_URL = "https://protegeglobal.com/inventoryM/supermed/api/api/";
   // public static String WEB_URL = "https://www.protegeglobal.com/supermed/ecommerce/api/api/";
//    public static String WEB_URL = "https://www.supermed.pk/beta/api/api/";
    public static String WEB_URL = "https://www.supermed.pk/supermedNew/api/";
    /* [ IMPORTANT ] be careful when edit this security code */
    /* This string must be same with security code at Server, if its different android unable to submit order */

    // Device will re-register the device data to server when open app N-times
    public static int OPEN_COUNTER = 50;


    /**
     * ------------------- DON'T EDIT THIS -------------------------------------------------------
     */

    // this limit value used for give pagination (request and display) to decrease payload
    public static int NEWS_PER_REQUEST = 10;
    public static int PRODUCT_PER_REQUEST = 10;
    public static int NOTIFICATION_PAGE = 20;
    public static int WISHLIST_PAGE = 20;

    // retry load image notification
    public static int LOAD_IMAGE_NOTIF_RETRY = 3;

    // Method get path to image
    public static String getURLimgProduct(String file_name) {
        return WEB_URL + "uploads/product/" + file_name;
    }

    public static String getURLimgNews(String file_name) {
        return WEB_URL + "uploads/news/" + file_name;
    }

    public static String getURLimgCategory(String file_name) {
        return WEB_URL + "uploads/category/" + file_name;
    }
}
