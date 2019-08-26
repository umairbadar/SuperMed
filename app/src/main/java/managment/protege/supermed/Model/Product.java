package managment.protege.supermed.Model;

/**
 * Created by wahaj on 6/12/2018.
 */

public class Product {

    public String getTitle() {
        return title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public Product(int image, String title) {
        this.image = image;
        this.title = title;
    }

    int image;
    public Product() {
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;

    public Product(String title) {
        this.title = title;
    }
}
