package managment.protege.supermed.Model;

public class Model_PopularProducts {

    private String cateSlug;
    private String CatName;
    private String subcateSlug;
    private String SubCatName;
    private String productId;
    private String productName;
    private String productDescription;
    private String productImage;
    private String price;
    private String qty;
    private String productTags;

    public Model_PopularProducts(String cateSlug, String catName, String subcateSlug, String subCatName, String productId, String productName, String productDescription, String productImage, String price, String qty, String productTags) {
        this.cateSlug = cateSlug;
        CatName = catName;
        this.subcateSlug = subcateSlug;
        SubCatName = subCatName;
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productImage = productImage;
        this.price = price;
        this.qty = qty;
        this.productTags = productTags;
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

    public String getPrice() {
        return price;
    }

    public String getQty() {
        return qty;
    }

    public String getProductTags() {
        return productTags;
    }
}
