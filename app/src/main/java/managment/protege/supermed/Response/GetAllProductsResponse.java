package managment.protege.supermed.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import managment.protege.supermed.Model.GetProductsModel;

public class GetAllProductsResponse {


    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("products")
    @Expose
    private List<GetProductsModel> products = null;
    @SerializedName("cartCounter")
    @Expose
    private Integer cartCounter;
    @SerializedName("wishlistCounter")
    @Expose
    private Integer wishlistCounter;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<GetProductsModel> getProducts() {
        return products;
    }

    public void setProducts(List<GetProductsModel> products) {
        this.products = products;
    }

    public Integer getCartCounter() {
        return cartCounter;
    }

    public void setCartCounter(Integer cartCounter) {
        this.cartCounter = cartCounter;
    }

    public Integer getWishlistCounter() {
        return wishlistCounter;
    }

    public void setWishlistCounter(Integer wishlistCounter) {
        this.wishlistCounter = wishlistCounter;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
