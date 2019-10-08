package managment.protege.supermed.Model;

public class Model_Appointment_History {

    private String name;
    private String status;
    private String appointment_id;
    private String dob;
    private String gender;
    private String date_time;
    private String payment_method;
    private String address;
    private String problem;
    private String price;

    public Model_Appointment_History(String name, String status, String appointment_id, String dob, String gender, String date_time, String payment_method, String address, String problem, String price) {
        this.name = name;
        this.status = status;
        this.appointment_id = appointment_id;
        this.dob = dob;
        this.gender = gender;
        this.date_time = date_time;
        this.payment_method = payment_method;
        this.address = address;
        this.problem = problem;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getAppointment_id() {
        return appointment_id;
    }

    public String getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public String getDate_time() {
        return date_time;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public String getAddress() {
        return address;
    }

    public String getProblem() {
        return problem;
    }

    public String getPrice() {
        return price;
    }
}
