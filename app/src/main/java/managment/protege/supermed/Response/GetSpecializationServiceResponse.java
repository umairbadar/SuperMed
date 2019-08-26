package managment.protege.supermed.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import managment.protege.supermed.Model.SpecializationModel;

public class GetSpecializationServiceResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;

    @SerializedName("specialities")
    @Expose
    private List<SpecializationModel> specializationModels = null;

    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<SpecializationModel> getSpecializationModels() {
        return specializationModels;
    }

    public void setSpecializationModels(List<SpecializationModel> specializationModels) {
        this.specializationModels = specializationModels;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
