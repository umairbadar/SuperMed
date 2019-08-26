package managment.protege.supermed.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppointmentHistoryModel {

    @SerializedName("appointment_id")
    @Expose
    private String appointmentId;
    @SerializedName("patient_id")
    @Expose
    private String patientId;
    @SerializedName("patient_name")
    @Expose
    private String patientName;
    @SerializedName("labname")
    @Expose
    private String labname;
    @SerializedName("testname")
    @Expose
    private String testname;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("appoinmentStatus")
    @Expose
    private String appoinmentStatus;
    @SerializedName("report")
    @Expose
    private String report;

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getLabname() {
        return labname;
    }

    public void setLabname(String labname) {
        this.labname = labname;
    }

    public String getTestname() {
        return testname;
    }

    public void setTestname(String testname) {
        this.testname = testname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAppoinmentStatus() {
        return appoinmentStatus;
    }

    public void setAppoinmentStatus(String appoinmentStatus) {
        this.appoinmentStatus = appoinmentStatus;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }
}
