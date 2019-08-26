package managment.protege.supermed.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import managment.protege.supermed.Model.CityListModel;

public class CityListResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("cities")
    @Expose
    private List<CityListModel> cities = null;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<CityListModel> getCities() {
        return cities;
    }

    public void setCities(List<CityListModel> cities) {
        this.cities = cities;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
