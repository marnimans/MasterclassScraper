package Application.Product;


public class Product {
    public String name;
    public String price;
    public String description;

    public String getProductName() {
        return name;
    }

    public void setProductName(String productName) {
        this.name = productName;
    }

    public String getProductPrice() {
        return price;
    }

    public void setProductPrice(String productPrice) {
        this.price = productPrice;
    }

    public String getProductDescription() {
        return description;
    }

    public void setProductDescription(String productDescription) {
        this.description = productDescription;
    }
}
