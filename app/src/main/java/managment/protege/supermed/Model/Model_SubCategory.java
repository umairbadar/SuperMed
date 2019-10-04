package managment.protege.supermed.Model;

public class Model_SubCategory {

    private String id;
    private String name;
    private String slug;
    private String thirdlevels;

    public Model_SubCategory(String id, String name, String slug, String thirdlevels) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.thirdlevels = thirdlevels;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public String getThirdlevels() {
        return thirdlevels;
    }
}
