package managment.protege.supermed.Model;

/**
 * Created by wahaj on 6/12/2018.
 */

public class TopCategories {

    public String getTitle() {
        return title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public TopCategories(int image, String title) {
        this.image = image;
        this.title = title;
    }

    int image;
    public TopCategories() {
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;

    public TopCategories(String title) {
        this.title = title;
    }
}
