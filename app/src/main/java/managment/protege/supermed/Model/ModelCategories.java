package managment.protege.supermed.Model;

public class ModelCategories {

    private String id;
    private String name;

    public ModelCategories(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
