package managment.protege.supermed.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import managment.protege.supermed.Fragment.Cart;
import managment.protege.supermed.Model.CartModel;

public class CartResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("cart")
    @Expose
    private List<CartModel> CartModel = null;
    @SerializedName("cartCounter")
    @Expose
    private Integer cartCounter;
    @SerializedName("orderTotal")
    @Expose
    private Integer orderTotal;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<CartModel> getCart() {
        return CartModel;
    }

    public void setCart(List<Cart> cart) {
        this.CartModel = CartModel;
    }

    public Integer getCartCounter() {
        return cartCounter;
    }

    public void setCartCounter(Integer cartCounter) {
        this.cartCounter = cartCounter;
    }

    public Integer getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(Integer orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
