package managment.protege.supermed.Model;

public class ModelCategories {

    private String id;
    private String name;
    private String slug;
    private String image;

    public ModelCategories(String id, String name, String image, String slug) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.slug = slug;
    }

    public String getSlug() {
        return slug;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
