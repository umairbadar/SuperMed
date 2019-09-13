package managment.protege.supermed.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by wahaj on 6/22/2018.
 */

public class WishlistModel {

    private String productId;
    private String productName;
    private String productImage;
    private String productDescription;
    private String wishlistId;
    private String qty;
    private String price;
    private String subcateName;

    public WishlistModel(String productId, String productName, String productImage, String productDescription, String wishlistId, String qty, String price, String subcateName) {
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.productDescription = productDescription;
        this.wishlistId = wishlistId;
        this.qty = qty;
        this.price = price;
        this.subcateName = subcateName;
    }

    public String getSubcateName() {
        return subcateName;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public String getWishlistId() {
        return wishlistId;
    }

    public String getQty() {
        return qty;
    }

    public String getPrice() {
        return price;
    }
}
