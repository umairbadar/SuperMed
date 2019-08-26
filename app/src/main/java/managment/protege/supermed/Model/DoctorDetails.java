package managment.protege.supermed.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DoctorDetails {



    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("clinic_name")
    @Expose
    private String clinicName;
    @SerializedName("qualification")
    @Expose
    private String qualification;
    @SerializedName("DaysANDTime")
    @Expose
    private String daysANDTime;
    @SerializedName("visit_fee")
    @Expose
    private String visitFee;
    @SerializedName("clinic_fee")
    @Expose
    private String clinicFee;
    @SerializedName("note")
    @Expose
    private String note;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getDaysANDTime() {
        return daysANDTime;
    }

    public void setDaysANDTime(String daysANDTime) {
        this.daysANDTime = daysANDTime;
    }

    public String getVisitFee() {
        return visitFee;
    }

    public void setVisitFee(String visitFee) {
        this.visitFee = visitFee;
    }

    public String getClinicFee() {
        return clinicFee;
    }

    public void setClinicFee(String clinicFee) {
        this.clinicFee = clinicFee;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
