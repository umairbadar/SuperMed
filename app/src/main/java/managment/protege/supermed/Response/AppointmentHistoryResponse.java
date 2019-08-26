package managment.protege.supermed.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import managment.protege.supermed.Model.AppointmentHistoryModel;

public class AppointmentHistoryResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("history")
    @Expose
    private List<AppointmentHistoryModel> history = null;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<AppointmentHistoryModel> getHistory() {
        return history;
    }

    public void setHistory(List<AppointmentHistoryModel> history) {
        this.history = history;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
