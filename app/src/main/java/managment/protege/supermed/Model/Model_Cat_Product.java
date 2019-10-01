package managment.protege.supermed.Model;

public class Model_Cat_Product {

    private String productId;
    private String productName;
    private String productDescription;
    private String productImage;
    private String productTags;
    private String price;
    private String qty;
    private String cateSlug;
    private String CatName;
    private String subcateSlug;
    private String SubCatName;

    public Model_Cat_Product(String productId, String productName, String productDescription, String productImage, String productTags, String price, String qty, String cateSlug, String catName, String subcateSlug, String subCatName) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productImage = productImage;
        this.productTags = productTags;
        this.price = price;
        this.qty = qty;
        this.cateSlug = cateSlug;
        CatName = catName;
        this.subcateSlug = subcateSlug;
        SubCatName = subCatName;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public String getProductImage() {
        return productImage;
    }

    public String getProductTags() {
        return productTags;
    }

    public String getPrice() {
        return price;
    }

    public String getQty() {
        return qty;
    }

    public String getCateSlug() {
        return cateSlug;
    }

    public String getCatName() {
        return CatName;
    }

    public String getSubcateSlug() {
        return subcateSlug;
    }

    public String getSubCatName() {
        return SubCatName;
    }
}
