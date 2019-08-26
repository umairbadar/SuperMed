package managment.protege.supermed.Model;

import android.widget.ImageView;

/**
 * Created by Developer on 6/21/2018.
 */

public class OrderHistoryList {
    String OrderNumber,ProductName,ProductSalePrice,ProductActualPrice;

    public OrderHistoryList(String orderNumber, String productName, String productSalePrice, String productActualPrice, int product) {
        OrderNumber = orderNumber;
        ProductName = productName;
        ProductSalePrice = productSalePrice;
        ProductActualPrice = productActualPrice;
        Product = product;
    }

    int Product;


    public String getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        OrderNumber = orderNumber;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductSalePrice() {
        return ProductSalePrice;
    }

    public void setProductSalePrice(String productSalePrice) {
        ProductSalePrice = productSalePrice;
    }

    public String getProductActualPrice() {
        return ProductActualPrice;
    }

    public void setProductActualPrice(String productActualPrice) {
        ProductActualPrice = productActualPrice;
    }

    public int getProduct() {
        return Product;
    }

    public void setProduct(int product) {
        Product = product;
    }


}
