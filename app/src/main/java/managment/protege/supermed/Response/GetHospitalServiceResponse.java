package managment.protege.supermed.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import managment.protege.supermed.Model.HospitalModel;

public class GetHospitalServiceResponse {

        @SerializedName("status")
        @Expose
        private Boolean status;

        @SerializedName("hospitals")
        @Expose
        private List<HospitalModel> hospitals = null;

        @SerializedName("message")
        @Expose
        private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<HospitalModel> getHospitals() {
        return hospitals;
    }

    public void setHospitals(List<HospitalModel> hospitals) {
        this.hospitals = hospitals;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
