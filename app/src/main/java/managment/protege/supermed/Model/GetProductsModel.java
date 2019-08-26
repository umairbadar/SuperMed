package managment.protege.supermed.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetProductsModel implements Serializable{

    @SerializedName("CatId")
    @Expose
    private String catId;
    @SerializedName("CatName")
    @Expose
    private String catName;
    @SerializedName("CatBriefIntro")
    @Expose
    private String catBriefIntro;
    @SerializedName("SubCatId")
    @Expose
    private String subCatId;
    @SerializedName("SubCatName")
    @Expose
    private String subCatName;
    @SerializedName("SubCatBriefIntro")
    @Expose
    private String subCatBriefIntro;
    @SerializedName("ProductID")
    @Expose
    private String productID;
    @SerializedName("ProductName")
    @Expose
    private String productName;
    @SerializedName("ProductImage")
    @Expose
    private String productImage;
    @SerializedName("ProductDescription")
    @Expose
    private String productDescription;
    @SerializedName("ProductSku")
    @Expose
    private String productSku;
    @SerializedName("ProductPrice")
    @Expose
    private String productPrice;
    @SerializedName("ProductOldPrice")
    @Expose
    private String productOldPrice;
    @SerializedName("ProductQty")
    @Expose
    private String productQty;
    @SerializedName("ProductTags")
    @Expose
    private String productTags;
    @SerializedName("is_wish")
    @Expose
    private String isWish;
    @SerializedName("is_cart")
    @Expose
    private String isCart;

    public GetProductsModel(String catId, String catName, String catBriefIntro, String subCatId, String subCatName, String subCatBriefIntro, String productID, String productName, String productImage, String productDescription, String productSku, String productPrice, String productQty, String productTags, String isWish, String isCart) {
        this.catId = catId;
        this.catName = catName;
        this.catBriefIntro = catBriefIntro;
        this.subCatId = subCatId;
        this.subCatName = subCatName;
        this.subCatBriefIntro = subCatBriefIntro;
        this.productID = productID;
        this.productName = productName;
        this.productImage = productImage;
        this.productDescription = productDescription;
        this.productSku = productSku;
        this.productPrice = productPrice;
        this.productQty = productQty;
        this.productTags = productTags;
        this.isWish = isWish;
        this.isCart = isCart;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatBriefIntro() {
        return catBriefIntro;
    }

    public void setCatBriefIntro(String catBriefIntro) {
        this.catBriefIntro = catBriefIntro;
    }

    public String getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(String subCatId) {
        this.subCatId = subCatId;
    }

    public String getSubCatName() {
        return subCatName;
    }

    public void setSubCatName(String subCatName) {
        this.subCatName = subCatName;
    }

    public String getSubCatBriefIntro() {
        return subCatBriefIntro;
    }

    public void setSubCatBriefIntro(String subCatBriefIntro) {
        this.subCatBriefIntro = subCatBriefIntro;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductOldPrice() {
        return productOldPrice;
    }

    public void setProductOldPrice(String productOldPrice) {
        this.productOldPrice = productOldPrice;
    }

    public String getProductQty() {
        return productQty;
    }

    public void setProductQty(String productQty) {
        this.productQty = productQty;
    }

    public String getProductTags() {
        return productTags;
    }

    public void setProductTags(String productTags) {
        this.productTags = productTags;
    }

    public String getIsWish() {
        return isWish;
    }

    public void setIsWish(String isWish) {
        this.isWish = isWish;
    }

    public String getIsCart() {
        return isCart;
    }

    public void setIsCart(String isCart) {
        this.isCart = isCart;
    }

}
