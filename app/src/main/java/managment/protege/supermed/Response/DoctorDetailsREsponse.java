package managment.protege.supermed.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import managment.protege.supermed.Model.DoctorDetails;

public class DoctorDetailsREsponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("doctorsInfo")
    @Expose
    private DoctorDetails doctors;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public DoctorDetails getDoctors() {
        return doctors;
    }

    public void setDoctors(DoctorDetails doctors) {
        this.doctors = doctors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
