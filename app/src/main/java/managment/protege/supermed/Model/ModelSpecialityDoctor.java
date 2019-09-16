package managment.protege.supermed.Model;

import com.android.volley.toolbox.StringRequest;

public class ModelSpecialityDoctor {

    private String doc_name;
    private String doc_designation;
    private String doc_time;
    private String gender;

    public ModelSpecialityDoctor(String doc_name, String doc_designation, String doc_time, String gender) {
        this.doc_name = doc_name;
        this.doc_designation = doc_designation;
        this.doc_time = doc_time;
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public String getDoc_name() {
        return doc_name;
    }

    public String getDoc_designation() {
        return doc_designation;
    }

    public String getDoc_time() {
        return doc_time;
    }
}
