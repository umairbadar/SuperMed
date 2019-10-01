package managment.protege.supermed.Model;

public class EmergencyModel {

    private String id;
    private String name;
    private String contact;

    public EmergencyModel(String id, String name, String contact) {
        this.id = id;
        this.name = name;
        this.contact = contact;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }
}
