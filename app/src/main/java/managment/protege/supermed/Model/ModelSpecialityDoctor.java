package managment.protege.supermed.Model;

public class ModelSpecialityDoctor {

    private String doc_name;
    private String doc_designation;
    private String doc_time;

    public ModelSpecialityDoctor(String doc_name, String doc_designation, String doc_time) {
        this.doc_name = doc_name;
        this.doc_designation = doc_designation;
        this.doc_time = doc_time;
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
