import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart { // ShoppingCart class representing a user's shopping cart
    private Map<Product, Integer> products; // Map to store products and their quantities in the cart
    private boolean isFirstPurchase; // Flag indicating whether it's the user's first purchase
    private User owner; // User who owns the shopping cart

    public ShoppingCart(User owner) { // Constructor for creating a new shopping cart for a specific user
        this.products = new HashMap<>(); // Initialize the map of products
        this.isFirstPurchase = true; // assuming every new cart is a first purchase, this could be changed based on user history if available
        this.owner=owner; // Set the owner of the shopping cart
    }

    public List<Product> getProductList() {
        return new ArrayList<>(products.keySet()); // Returns a list of products, disregarding their quantities
    }
    public Map<Product, Integer> getProducts() {
        return new HashMap<>(products); // Returns a copy of the current product-quantity map
    }



    public void addProduct(Product product, int quantity) {
        if
        (quantity <= 0) {
            JOptionPane.showMessageDialog(null, "Quantity must be greater than zero.");
            return;
        }
        if (product.getAvailableItems() < quantity) {
            JOptionPane.showMessageDialog(null, "Not enough items available.");
            return;
        }
// Increment quantity if product already exists, otherwise add with the specified quantity
        int currentQuantity = products.getOrDefault(product, 0);
        products.put(product, currentQuantity + quantity);
    }

    public int getProductQuantity(Product product) {
        return products.getOrDefault(product, 0);
    }

    public double calculateTotalPrice() {
        return products.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getProductPrice() * entry.getValue())
                .sum();
    }
//    public boolean isFirstPurchase() {
//        return isFirstPurchase;
//    }


    public double calculateDiscounts() {
        double discount = 0.0;

        // Check for first purchase discount
        if (owner.isFirstPurchase()) { // Now it calls the getter method
            discount += calculateTotalPrice() * 0.1; // 10% first purchase discount
            owner.setFirstPurchase(false); // No longer the first purchase after applying discount

        }

        // Calculate the category discount
        discount += calculateCategoryDiscount();

        return discount;
    }

    public double calculateCategoryDiscount() {
        double categoryDiscount = 0.0;
        // Count the number of products in each category
        Map<String, Integer> categoryCount = new HashMap<>();
        for (Product p : products.keySet()) {
            String category = (p instanceof Electronics) ? "Electronics" : "Clothing";
            categoryCount.put(category, categoryCount.getOrDefault(category, 0) + products.get(p));
        }

        // Apply a 20% discount for any category with 3 or more items
        for (String category : categoryCount.keySet()) {
            if (categoryCount.get(category) >= 3) {
                double categoryTotal = products.entrySet().stream()
                        .filter(entry -> (entry.getKey() instanceof Electronics && category.equals("Electronics")) ||
                                (entry.getKey() instanceof Clothing && category.equals("Clothing")))
                        .mapToDouble(entry -> entry.getKey().getProductPrice() * entry.getValue())
                        .sum();

                categoryDiscount += categoryTotal * 0.20;
            }
        }

        return categoryDiscount;
    }


}
