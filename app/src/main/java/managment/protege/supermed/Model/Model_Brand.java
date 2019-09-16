package managment.protege.supermed.Model;

public class Model_Brand {

    private String id;
    private String name;
    private String slug;
    private String image;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public String getImage() {
        return image;
    }

    public Model_Brand(String id, String name, String slug, String image) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.image = image;
    }
}
