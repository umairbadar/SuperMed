package managment.protege.supermed.Model;

public class Model_Labs {

    private String id;
    private String name;
    private String image;

    public Model_Labs(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }
}
