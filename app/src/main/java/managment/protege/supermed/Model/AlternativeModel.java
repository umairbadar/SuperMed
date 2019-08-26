package managment.protege.supermed.Model;

/**
 * Created by wahaj on 6/22/2018.
 */

public class AlternativeModel {

    String Title;
    String price;
    String oldprice;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    int image;

    public AlternativeModel(String title, String price, String oldprice, int image) {
        Title = title;
        this.price = price;
        this.oldprice = oldprice;
        this.image = image;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOldprice() {
        return oldprice;
    }

    public void setOldprice(String oldprice) {
        this.oldprice = oldprice;
    }


    public AlternativeModel(String title, String price, String oldprice) {
        Title = title;
        this.price = price;
        this.oldprice = oldprice;
    }


}
