package managment.protege.supermed.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import managment.protege.supermed.Model.DoctorsModel;
import managment.protege.supermed.Model.HospitalModel;

public class GetDoctorServiceResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;

    @SerializedName("Doctors")
    @Expose
    private List<DoctorsModel> doctorsList = null;

    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<DoctorsModel> getDoctorsList() {
        return doctorsList;
    }

    public void setDoctorsList(List<DoctorsModel> doctorsList) {
        this.doctorsList = doctorsList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
