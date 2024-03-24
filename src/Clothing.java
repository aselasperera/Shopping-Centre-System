public class Clothing extends Product { // Clothing class, extending the Product class (Inheritance)
    private String size; // Private instance variables
    private String color;

    // Constructor for creating a Clothing object
    public Clothing(String productId, String productName, int availableItems, double productPrice, String size, String color) {
        super(productId, productName, availableItems, productPrice); // Call the constructor of the superclass (Product) using super()
        this.size = size; // Initialize Clothing-specific variables
        this.color = color;
    }

    public String getSize() {
        return size;
    } // Getter method for the size of the clothing

    public void setSize(String size) {
        this.size = size;
    } // Setter method for the size of the clothing

    public String getColor() {
        return color;
    } // Getter method for the color of the clothing

    public void setColor(String color) {
        this.color = color;
    } // Setter method for the color of the clothing

    @Override // Override the getProductDetails method from the superclass (Polymorphism)
    public String getProductDetails() {
        return "Clothing: " + getProductName() + " (Size: " + size + ", Color: " + color + ")"; // Return a formatted string with Clothing-specific details
    }
}