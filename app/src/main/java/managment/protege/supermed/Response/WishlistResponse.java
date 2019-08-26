package managment.protege.supermed.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import managment.protege.supermed.Model.WishlistModel;

public class WishlistResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("wishlist")
    @Expose
    private List<WishlistModel> wishlist = null;
    @SerializedName("wishCounter")
    @Expose
    private Integer wishCounter;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<WishlistModel> getWishlist() {
        return wishlist;
    }

    public void setWishlist(List<WishlistModel> wishlist) {
        this.wishlist = wishlist;
    }

    public Integer getWishCounter() {
        return wishCounter;
    }

    public void setWishCounter(Integer wishCounter) {
        this.wishCounter = wishCounter;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
