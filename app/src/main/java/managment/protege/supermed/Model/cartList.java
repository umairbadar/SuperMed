package managment.protege.supermed.Model;

/**
 * Created by Developer on 6/19/2018.
 */

public class cartList {
    String Title;
    String Price;
    int Image;
    String Sale;

    public cartList(String title, String price, int image,String sale) {
        Title = title;
        Price = price;
        Image = image;
        Sale = sale;
    }



    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }

    public String getSale() {
        return Sale;
    }

    public void setSale(String sale) {
        Sale = sale;
    }


}
