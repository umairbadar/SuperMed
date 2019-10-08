package managment.protege.supermed.Model;

public class Model_Sub_Sub_Category {

    private String id;
    private String name;
    private String slug;
    private String sub_slug;
    private String main_slug;

    public Model_Sub_Sub_Category(String id, String name, String slug, String sub_slug, String main_slug) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.sub_slug = sub_slug;
        this.main_slug = main_slug;
    }

    public String getSub_slug() {
        return sub_slug;
    }

    public String getMain_slug() {
        return main_slug;
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
}
