import java.io.Serializable;

public abstract class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    private String productId; // Instance variables for product details (Encapsulation)
    private String productName;
    private int availableItems;
    private double productPrice;

    public Product(String productId, String productName, int availableItems, double productPrice) { // Constructor to initialize product details
        this.productId = productId;
        this.productName = productName;
        this.availableItems = availableItems;
        this.productPrice = productPrice;
    }

    public String getProductId() {
        return productId;
    } // Getter method for retrieving product ID

    public String getProductName() {
        return productName;
    } // Getter method for retrieving product name

    public int getAvailableItems() {
        return availableItems;
    } // Getter method for retrieving available items

    public void setAvailableItems(int availableItems) {
        this.availableItems = availableItems;
    } // Setter method for updating available items

    public double getProductPrice() {
        return productPrice;
    }  // Getter method for retrieving product price

    public abstract String getProductDetails(); // Abstract method for getting product details, to be implemented by subclasses (Abstraction)
}