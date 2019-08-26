package managment.protege.supermed.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import managment.protege.supermed.Model.EmergencyModel;

public class EmergencyResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("emergency")
    @Expose
    private List<EmergencyModel> emergency = null;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<EmergencyModel> getEmergency() {
        return emergency;
    }

    public void setEmergency(List<EmergencyModel> emergency) {
        this.emergency = emergency;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
