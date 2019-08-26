package managment.protege.supermed.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import managment.protege.supermed.Model.ContactModel;

public class ContactUsResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("contactUs")
    @Expose
    private ContactModel contactUs;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public ContactModel getContactUs() {
        return contactUs;
    }

    public void setContactUs(ContactModel contactUs) {
        this.contactUs = contactUs;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("addressOne")
        @Expose
        private String addressOne;
        @SerializedName("addressTwo")
        @Expose
        private String addressTwo;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("whatsapp")
        @Expose
        private String whatsapp;
        @SerializedName("email")
        @Expose
        private String email;

        public String getAddressOne() {
            return addressOne;
        }

        public void setAddressOne(String addressOne) {
            this.addressOne = addressOne;
        }

        public String getAddressTwo() {
            return addressTwo;
        }

        public void setAddressTwo(String addressTwo) {
            this.addressTwo = addressTwo;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getWhatsapp() {
            return whatsapp;
        }

        public void setWhatsapp(String whatsapp) {
            this.whatsapp = whatsapp;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

    }
}