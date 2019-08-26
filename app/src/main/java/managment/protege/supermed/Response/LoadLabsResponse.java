package managment.protege.supermed.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import managment.protege.supermed.Model.LoadLabsModel;

public class LoadLabsResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("labs")
    @Expose
    private List<LoadLabsModel> labs = null;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<LoadLabsModel> getLabs() {
        return labs;
    }

    public void setLabs(List<LoadLabsModel> labs) {
        this.labs = labs;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
