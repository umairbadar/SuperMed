package managment.protege.supermed.Model;

public class TestModel {


    public TestModel(String test_name, String test_id, String lab_id, String labname, String price) {
        this.test_name = test_name;
        this.test_id = test_id;
        this.lab_id = lab_id;
        this.labname = labname;
        this.price = price;
    }

    public String getTest_name() {
        return test_name;
    }

    public void setTest_name(String test_name) {
        this.test_name = test_name;
    }

    public String getTest_id() {
        return test_id;
    }

    public void setTest_id(String test_id) {
        this.test_id = test_id;
    }

    public String getLab_id() {
        return lab_id;
    }

    public void setLab_id(String lab_id) {
        this.lab_id = lab_id;
    }

    public String getLabname() {
        return labname;
    }

    public void setLabname(String labname) {
        this.labname = labname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    String test_name;
    String test_id;
    String lab_id;
    String labname;
    String price;

}
