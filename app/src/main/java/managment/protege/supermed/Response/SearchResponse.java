package managment.protege.supermed.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import managment.protege.supermed.Model.SearchModel;

public class SearchResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("search")
    @Expose
    private List<SearchModel> search = null;
    @SerializedName("cartCounter")
    @Expose
    private String cartCounter;
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

    public List<SearchModel> getSearch() {
        return search;
    }

    public void setSearch(List<SearchModel> search) {
        this.search = search;
    }

    public String getCartCounter() {
        return cartCounter;
    }

    public void setCartCounter(String cartCounter) {
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
