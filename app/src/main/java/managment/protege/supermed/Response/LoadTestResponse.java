package managment.protege.supermed.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import managment.protege.supermed.Model.LoadTestModel;

public class LoadTestResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("tests")
    @Expose
    private List<LoadTestModel> tests = null;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<LoadTestModel> getTests() {
        return tests;
    }

    public void setTests(List<LoadTestModel> tests) {
        this.tests = tests;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
