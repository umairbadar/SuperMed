package managment.protege.supermed.Model;

public class CartModel {

    private String productID;
    private String productName;
    private String productPrice;
    private String productQty;
    private String productImage;
    private String cartID;

    public CartModel(String productID, String productName, String productPrice, String productQty, String productImage, String cartID) {
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQty = productQty;
        this.productImage = productImage;
        this.cartID = cartID;
    }

    public String getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getProductQty() {
        return productQty;
    }

    public String getProductImage() {
        return productImage;
    }

    public String getCartID() {
        return cartID;
    }
}
