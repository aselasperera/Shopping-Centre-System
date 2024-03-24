import java.io.*;
import java.util.*;


public class WestminsterShoppingManager{ // Class declaration for the WestminsterShoppingManager
    private static final int MAX_PRODUCTS = 50; // Maximum number of products allowed
    private final Map<String, Integer> productAvailability; // Map to store product availability by product ID
    private List<Product> productList; // List to store products
    private static Scanner scanner = new Scanner(System.in); // Scanner for user input
    private List<User> regUserList; // List to store registered users
    private static User loggedInUser; // The currently logged-in user

    public WestminsterShoppingManager() { // Constructor for WestminsterShoppingManager class
        productAvailability = new HashMap<>();
        this.productList = new ArrayList<>();
        this.regUserList=new ArrayList<>();
    }

    public List<Product> getProductList() {
        return productList;
    } // Getter method for productList

    public void addProduct(Product product) { // Method to add a product to the productList
        if (productList.size() < MAX_PRODUCTS) { // Check if the maximum number of products has been reached
            productList.add(product);
            System.out.println("Product added successfully.");
        } else {
            System.out.println("Maximum number of products reached. Cannot add more products.");
        }
    }

    public void deleteProduct(String productId) { // Method to delete a product from the productList
        Product productToRemove = null; // Search for the product with the given ID
        for (Product product : productList) {
            if (product.getProductId().equals(productId)) {
                productToRemove = product;
                break;
            }
        }

        if (productToRemove != null) { // Remove the product if found, otherwise, display an error message
            productList.remove(productToRemove);
            System.out.println("Product removed: " + productToRemove.getProductName());
        } else {
            System.out.println("Product with ID " + productId + " not found.");
        }
    }

    public void printProductList() { // Method to print the product list, sorted alphabetically by product ID
        // Sorting the product list alphabetically by product ID
        productList.sort((p1, p2) -> p1.getProductId().compareToIgnoreCase(p2.getProductId()));

        for (Product product : productList) {
            System.out.println(product.getProductDetails());
        }
    }

    public void saveProductListToFile(String fileName) { // Method to save the product list to a file
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            outputStream.writeObject(productList);
            System.out.println("Product list saved to file: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getProductAvailability(String productId) { // Method to get product availability information by product ID
        // Check if the product ID exists in the availability map
        if (productAvailability.containsKey(productId)) {
            int availableQuantity = productAvailability.get(productId);
            if (availableQuantity > 0) {
                return "In Stock (" + availableQuantity + " available)";
            } else {
                return "Out of Stock";
            }
        } else {
            return "Product not found"; // Product ID not in the map
        }
    }

    // Method to update product availability based on product ID and quantity added to cart
    public void updateProductAvailability(String productId, int quantityAddedToCart) {
        // Check if the product ID exists in the availability map
        if (productAvailability.containsKey(productId)) {
            int availableQuantity = productAvailability.get(productId);
            if (availableQuantity >= quantityAddedToCart) {
                // Decrease the available quantity based on the quantity added to cart
                productAvailability.put(productId, availableQuantity - quantityAddedToCart);
            }
        }
    }

    private void registerUser(String username, String password){ // Method to register a new user
        User newUser = new User(username,password);
        regUserList.add(newUser);
    }

    public void saveUsers(){ // Method to save registered users to a file
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("registeredUsers.dat", false))) {
            oos.writeObject(regUserList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadRegUserList() {    // Method to load the list of registered users from a file
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("registeredUsers.dat"))) {
            regUserList = (List<User>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.err.println("Creating new file...");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean userAuthentication(String username, String password) { // Method to authenticate a user based on username and password
        for (User user : regUserList) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public User getUserByUsername(String username) { // Method to get a user object by username
        for (User user : regUserList) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static void main(String[] args) { // Main method to execute the program
        WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager(); // Create an instance of WestminsterShoppingManager
        ShoppingCart shoppingCart = new ShoppingCart(loggedInUser); // Create an instance of ShoppingCart


        int choice;// Variable to store user's menu choice
        do {  // Main program loop
            System.out.println("........Online Shopping System........");
            System.out.println("1) Add a new product  ");
            System.out.println("2) Delete a product ");
            System.out.println("3) Print the list of the products ");
            System.out.println("4) Save product to file ");
            System.out.println("5) Open GUI ");
            System.out.println("0) Exit ");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) { // Switch statement to handle user's choice
                case 1:
                    // Add Product
                    System.out.println("Enter product type (1 for Electronics, 2 for Clothing): ");
                    int productType = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("Enter product ID: ");
                    String productId = scanner.nextLine();

                    System.out.println("Enter product name: ");
                    String productName = scanner.nextLine();

                    System.out.println("Enter available items: ");
                    int availableItems = scanner.nextInt();

                    System.out.println("Enter price: £ ");
                    double productPrice = scanner.nextDouble();

                    Product newProduct = null;

                    if (productType == 1) {
                        // Electronics
                        System.out.println("Enter brand: ");
                        scanner.nextLine();
                        String brand = scanner.nextLine();

                        System.out.println("Enter warranty period: " + "months");
                        int warrantyPeriod = scanner.nextInt();

                        newProduct = new Electronics(productId, productName, availableItems, productPrice, brand, warrantyPeriod);

                    } else if (productType == 2) {
                        // Clothing
                        System.out.println("Enter size: ");
                        scanner.nextLine();
                        String size = scanner.nextLine();

                        System.out.println("Enter color: ");
                        String color = scanner.nextLine();

                        newProduct = new Clothing(productId, productName, availableItems, productPrice, size, color);
                    }

                    shoppingManager.addProduct(newProduct);
                    System.out.println("Product added: " + newProduct.getProductDetails());
                    break;

                case 2:
                    // Delete Product
                    System.out.println("Enter product ID to delete: ");
                    scanner.nextLine();
                    String deleteProductId = scanner.nextLine();

                    shoppingManager.deleteProduct(deleteProductId);
                    break;


                case 3:
                    // Print Product List
                    System.out.println("Product List:");
                    shoppingManager.printProductList();
                    break;


                case 4:
                    // Save Product List to File
                    System.out.println("Enter the file name to save the product list: ");
                    scanner.nextLine();
                    String saveFileName = scanner.nextLine();
                    shoppingManager.saveProductListToFile(saveFileName);
                    break;

                case 5:
                    // Open GUI
                    //shoppingManager.loadRegUserList();
                    System.out.println("1. Log in");
                    System.out.println("2. Register");
                    System.out.println("Enter your choice: ");
                    int loginOrRegister = scanner.nextInt();
                    switch (loginOrRegister){
                        case 1:
                            shoppingManager.loadRegUserList();
                            System.out.println("Enter your username: ");
                            String username = scanner.next();
                            System.out.println("Enter your password: ");
                            String password = scanner.next();
                            if (shoppingManager.userAuthentication(username,password)) {
                                System.out.println("Login successful");
                                loggedInUser = shoppingManager.getUserByUsername(username);
                                GUI shoppingGUI = new GUI(shoppingManager, shoppingCart,loggedInUser);
                                shoppingGUI.setVisible(true);
                            }
                            break;

                        case 2:
                            System.out.println("Enter a new username: ");
                            String newUsername = scanner.next();

                            System.out.println("Enter a password: ");
                            String newPassword = scanner.next();
                            shoppingManager.registerUser(newUsername,newPassword);
                            shoppingManager.saveUsers();
                            break;
                    }

                case 0: // Exit program
                    System.out.println("Exiting Westminster Shopping Manager. Goodbye!");
                    break;
                default: // Invalid choice
                    System.out.println("Invalid choice. Please enter a valid option.");
            }

        } while (choice != 0);
        scanner.close(); // Close the scanner
    }
}
