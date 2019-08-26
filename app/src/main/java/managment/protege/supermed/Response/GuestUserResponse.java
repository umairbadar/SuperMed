package managment.protege.supermed.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import managment.protege.supermed.Model.GuestUserModel;

public class GuestUserResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("profile")
    @Expose
    private GuestUserModel profile;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public GuestUserModel getProfile() {
        return profile;
    }

    public void setProfile(GuestUserModel profile) {
        this.profile = profile;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
