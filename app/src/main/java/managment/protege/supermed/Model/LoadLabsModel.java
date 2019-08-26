package managment.protege.supermed.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoadLabsModel {
    @SerializedName("labId")
    @Expose
    private String labId;
    @SerializedName("labName")
    @Expose
    private String labName;
    @SerializedName("labAddress")
    @Expose
    private String labAddress;
    @SerializedName("labPrimaryContact")
    @Expose
    private String labPrimaryContact;
    @SerializedName("labSecContact")
    @Expose
    private String labSecContact;

    public String getLabId() {
        return labId;
    }

    public void setLabId(String labId) {
        this.labId = labId;
    }

    public String getLabName() {
        return labName;
    }

    public void setLabName(String labName) {
        this.labName = labName;
    }

    public String getLabAddress() {
        return labAddress;
    }

    public void setLabAddress(String labAddress) {
        this.labAddress = labAddress;
    }

    public String getLabPrimaryContact() {
        return labPrimaryContact;
    }

    public void setLabPrimaryContact(String labPrimaryContact) {
        this.labPrimaryContact = labPrimaryContact;
    }

    public String getLabSecContact() {
        return labSecContact;
    }

    public void setLabSecContact(String labSecContact) {
        this.labSecContact = labSecContact;
    }


}
