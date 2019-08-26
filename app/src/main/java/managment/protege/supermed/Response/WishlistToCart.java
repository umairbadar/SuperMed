package managment.protege.supermed.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WishlistToCart {

    @SerializedName("status")
    @Expose
    private Boolean status;
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
