package managment.protege.supermed.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import managment.protege.supermed.Model.OrderhistoryModel;

public class ResponseOrderHistroy {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("orders")
    @Expose
    private List<OrderhistoryModel> orders = null;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<OrderhistoryModel> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderhistoryModel> orders) {
        this.orders = orders;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
