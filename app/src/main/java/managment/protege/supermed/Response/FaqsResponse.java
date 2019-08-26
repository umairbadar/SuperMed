package managment.protege.supermed.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import managment.protege.supermed.Model.FaqsModel;

public class FaqsResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("topics")
    @Expose
    private List<FaqsModel> topics = null;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<FaqsModel> getTopics() {
        return topics;
    }

    public void setTopics(List<FaqsModel> topics) {
        this.topics = topics;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
