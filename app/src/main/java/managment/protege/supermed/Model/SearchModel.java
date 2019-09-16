package managment.protege.supermed.Model;


public class SearchModel {

    private String productId;
    private String productName;
    private String productSlug;
    private String cateName;
    private String cateSlug;
    private String subcateName;
    private String subcateSlug;
    private String qty;
    private String price;
    private String productDescription;
    private String productImage;
    private String productTags;

    public SearchModel(String productId, String productName, String productSlug, String cateName, String cateSlug, String subcateName, String subcateSlug, String qty, String price, String productDescription, String productImage, String productTags) {
        this.productId = productId;
        this.productName = productName;
        this.productSlug = productSlug;
        this.cateName = cateName;
        this.cateSlug = cateSlug;
        this.subcateName = subcateName;
        this.subcateSlug = subcateSlug;
        this.qty = qty;
        this.price = price;
        this.productDescription = productDescription;
        this.productImage = productImage;
        this.productTags = productTags;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductSlug() {
        return productSlug;
    }

    public String getCateName() {
        return cateName;
    }

    public String getCateSlug() {
        return cateSlug;
    }

    public String getSubcateName() {
        return subcateName;
    }

    public String getSubcateSlug() {
        return subcateSlug;
    }

    public String getQty() {
        return qty;
    }

    public String getPrice() {
        return price;
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
}
