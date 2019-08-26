package managment.protege.supermed.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import managment.protege.supermed.Model.DocModel;

public class DocResponse {


    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("doctors")
    @Expose
    private List<DocModel> doctors = null;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<DocModel> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<DocModel> doctors) {
        this.doctors = doctors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
