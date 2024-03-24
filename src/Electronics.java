public class Electronics extends Product { // Electronics class, extending the Product class (Inheritance)
    private String brand; // Private instance variables
    private int warrantyPeriod;

    // Constructor for creating an Electronics object
    public Electronics(String productId, String productName, int availableItems, double productPrice, String brand, int warrantyPeriod) {
        super(productId, productName, availableItems, productPrice); // Call the constructor of the superclass (Product) using super()
        this.brand = brand; // Initialize Electronics-specific variables
        this.warrantyPeriod = warrantyPeriod;
    }

    public String getBrand() {
        return brand;
    } // Getter method for the brand of the electronics

    public void setBrand(String brand) {
        this.brand = brand;
    } // Setter method for the brand of the electronics

    public int getWarrantyPeriod() { // Getter method for the warranty period of the electronics
        return warrantyPeriod;
    }

    public void setWarrantyPeriod(int warrantyPeriod) { // Setter method for the warranty period of the electronics
        this.warrantyPeriod = warrantyPeriod;
    }

    @Override // Override the getProductDetails method from the superclass (Polymorphism)
    public String getProductDetails() {
        return "Electronics: " + getProductName() + " (Brand: " + brand + ", Warranty: " + warrantyPeriod + " months)"; // Return a formatted string with Electronics-specific details
    }
}