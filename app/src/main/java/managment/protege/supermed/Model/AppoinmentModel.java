package managment.protege.supermed.Model;

/**
 * Created by Developer on 6/21/2018.
 */

public class AppoinmentModel {
    public String getAppointment_no() {
        return appointment_no;
    }

    public void setAppointment_no(String appointment_no) {
        this.appointment_no = appointment_no;
    }

    public String getTest_name() {
        return test_name;
    }

    public void setTest_name(String test_name) {
        this.test_name = test_name;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getLabortaory_name() {
        return labortaory_name;
    }

    public void setLabortaory_name(String labortaory_name) {
        this.labortaory_name = labortaory_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String appointment_no;
    String test_name;
    String patient_id;
    String patient_name;
    String labortaory_name;
    String date;

    public AppoinmentModel(String appointment_no, String test_name, String patient_id, String patient_name, String labortaory_name, String date) {
        this.appointment_no = appointment_no;
        this.test_name = test_name;
        this.patient_id = patient_id;
        this.patient_name = patient_name;
        this.labortaory_name = labortaory_name;
        this.date = date;
    }

}
