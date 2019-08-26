package managment.protege.supermed.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import managment.protege.supermed.Model.CountryListModel;

public class CountryListResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("countries")
    @Expose
    private List<CountryListModel> countries = null;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<CountryListModel> getCountries() {
        return countries;
    }

    public void setCountries(List<CountryListModel> countries) {
        this.countries = countries;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
