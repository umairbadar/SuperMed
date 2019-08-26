package managment.protege.supermed.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import managment.protege.supermed.Model.CouponModel;

public class CouponResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("coupon")
    @Expose
    private List<CouponModel> coupon = null;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<CouponModel> getCoupon() {
        return coupon;
    }

    public void setCoupon(List<CouponModel> coupon) {
        this.coupon = coupon;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
