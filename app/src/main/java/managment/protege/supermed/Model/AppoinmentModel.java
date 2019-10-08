package managment.protege.supermed.Model;

public class AppoinmentModel {

    private String appointment_id;
    private String day;
    private String date;
    private String time;
    private String patient_id;
    private String lab;
    private String status;
    private String payment_method;
    private String desc;
    private String price;

    public AppoinmentModel(String appointment_id, String day, String date, String time, String patient_id, String lab, String status, String payment_method, String desc, String price) {
        this.appointment_id = appointment_id;
        this.day = day;
        this.date = date;
        this.time = time;
        this.patient_id = patient_id;
        this.lab = lab;
        this.status = status;
        this.payment_method = payment_method;
        this.desc = desc;
        this.price = price;
    }

    public String getAppointment_id() {
        return appointment_id;
    }

    public String getDay() {
        return day;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public String getLab() {
        return lab;
    }

    public String getStatus() {
        return status;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public String getDesc() {
        return desc;
    }

    public String getPrice() {
        return price;
    }
}
