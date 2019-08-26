package managment.protege.supermed.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Subcategory {


    @SerializedName("ParentId")
    @Expose
    private String parentId;
    @SerializedName("ParentName")
    @Expose
    private String parentName;
    @SerializedName("ParentBriefIntro")
    @Expose
    private String parentBriefIntro;
    @SerializedName("SubCatId")
    @Expose
    private String subCatId;
    @SerializedName("SubCatName")
    @Expose
    private String subCatName;
    @SerializedName("BriefIntro")
    @Expose
    private String briefIntro;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentBriefIntro() {
        return parentBriefIntro;
    }

    public void setParentBriefIntro(String parentBriefIntro) {
        this.parentBriefIntro = parentBriefIntro;
    }

    public String getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(String subCatId) {
        this.subCatId = subCatId;
    }

    public String getSubCatName() {
        return subCatName;
    }

    public void setSubCatName(String subCatName) {
        this.subCatName = subCatName;
    }

    public String getBriefIntro() {
        return briefIntro;
    }

    public void setBriefIntro(String briefIntro) {
        this.briefIntro = briefIntro;
    }

}
