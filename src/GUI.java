import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GUI extends JFrame implements ActionListener, WindowListener { // GUI class extending JFrame and implementing ActionListener, WindowListener (Inheritance and Interface implementation)
    private WestminsterShoppingManager shoppingManager; // Instance variables for GUI components
    private JFrame cartFrame; // Define a JFrame for the shopping cart

    private ShoppingCart shoppingCart;
    private User loggedInUser;

    private JComboBox<String> productTypeComboBox;
    private JTable productTable;
    private JTextArea productDetailsTextArea;

    // Constructor for GUI, initializes UI components
    public GUI(WestminsterShoppingManager shoppingManager, ShoppingCart shoppingCart,User loggedInUser) {
        this.shoppingManager = shoppingManager;
        this.shoppingCart = shoppingCart;
        this.loggedInUser=loggedInUser;

        initializeUI();
    }

    private void initializeUI() { // Method to initialize the UI components
        setTitle("Westminster Shopping Centre");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Instantiate GUI components
        productTypeComboBox = new JComboBox<>(new String[]{"All", "Electronics", "Clothing"});
        productTable = new JTable();
        productDetailsTextArea = new JTextArea();
        JButton addToCartButton = new JButton("Add to Cart");
        JButton shoppingCartButton = new JButton("Shopping Cart");

        // Create top panel with category selection and buttons
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Select Product Category:"));
        topPanel.add(productTypeComboBox);
        topPanel.add(addToCartButton);
        topPanel.add(shoppingCartButton);

        // Create scrollable table for product display
        JScrollPane tableScrollPane = new JScrollPane(productTable);
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.add(new JLabel("Selected Product-Details:"), BorderLayout.NORTH);
        detailsPanel.add(new JScrollPane(productDetailsTextArea), BorderLayout.CENTER);

        // Add components to the frame
        add(topPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(detailsPanel, BorderLayout.SOUTH);

        // Add listener for row selection in product table
        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                displaySelectedProductDetails();
            }
        });
        // Add window listener to handle window closing event
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                loggedInUser.setFirstPurchase(false);
                shoppingManager.saveUsers();
            }
        });
        // Add action listeners for buttons and combo box
        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addToCart();
            }
        });

        shoppingCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayShoppingCart();
            }
        });

        productTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProductTable();
            }
        });
        // Pack, set location, and make the frame visible
        pack();
        setLocationRelativeTo(null);
    }
    private void displaySelectedProductDetails() { // Method to display selected product details in the text area
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0) { // Check if a valid row is selected
            String productId = productTable.getValueAt(selectedRow, 0).toString();
            String name = productTable.getValueAt(selectedRow, 1).toString();
            String category = productTable.getValueAt(selectedRow, 2).toString();
            String price = productTable.getValueAt(selectedRow, 3).toString();
            String info = productTable.getValueAt(selectedRow, 4).toString();

            // Parse price as a double (assuming it's a decimal value)
            double priceValue;
            try {
                priceValue = Double.parseDouble(price);
            } catch (NumberFormatException e) {
                priceValue = 0.0; // Set a default value or handle the error as needed
            }

            String availability = getProductAvailability(productId);

            // Check availability and set the font color accordingly
            String availabilityText;
            if (availability.equals("Not Available")) {
                availabilityText = availability;
            } else {
                availabilityText = availability;
            }

            // Update product details without HTML tags
            productDetailsTextArea.setText("Product ID: " + productId + "\n"
                    + "Name: " + name + "\n"
                    + "Category: " + category + "\n"
                    + "Price: £" + String.format("%.2f", priceValue) + "\n"
                    + "Info: " + info + "\n"
                    + "Availability: " + availabilityText + "\n");
        }
    }


    private String getProductAvailability(String productId) { // Method to get product availability given a product ID
        // Assuming you have a collection of products in shoppingManager
        List<Product> productList = shoppingManager.getProductList();

        // Iterate through the list of products to find the one with the matching productId
        for (Product product : productList) {
            if (product.getProductId().equals(productId)) {
                // Get the availability status of the product
                int availability = product.getAvailableItems();

                // Return the availability status as a string
                return String.valueOf(availability);
            }
        }

        // If no product with the given productId is found, return "Not Available" or any appropriate default value.
        return "Not Available";
    }


    private void updateProductTable() { // Method to update the product table based on the selected product type
        String selectedType = (String) productTypeComboBox.getSelectedItem();
        List<Product> filteredProducts;

        if (selectedType.equals("All")) {
            filteredProducts = shoppingManager.getProductList();
        } else {
            filteredProducts = new ArrayList<>();
            for (Product product : shoppingManager.getProductList()) {
                if ((selectedType.equals("Electronics") && product instanceof Electronics) ||
                        (selectedType.equals("Clothing") && product instanceof Clothing)) {
                    filteredProducts.add(product);
                }
            }
        }

        DefaultTableModel tableModel = new DefaultTableModel(
                new Object[]{"Product ID", "Name", "Category", "Price(£)", "Info"}, 0);

        for (Product product : filteredProducts) {
            String category = (product instanceof Electronics) ? "Electronics" : "Clothing";
            Object[] rowData;

            // Check if availability is less than 3 and set the cell renderer to red
            if (product.getAvailableItems() < 4) { // Use HTML tags to set the font color to red
                rowData = new Object[]{
                        "<html><font color='red'>" + product.getProductId() + "</font></html>",
                        "<html><font color='red'>" + product.getProductName() + "</font></html>",
                        "<html><font color='red'>" + category + "</font></html>",
                        "<html><font color='red'>" + product.getProductPrice() + "</font></html>",
                        "<html><font color='red'>" + getProductDetails(product) + "</font></html>"
                };
            } else { // If availability is not less than 2, display the data without color changes
                rowData = new Object[]{
                        product.getProductId(),
                        product.getProductName(),
                        category,
                        product.getProductPrice(),
                        getProductDetails(product)
                };
            }

            tableModel.addRow(rowData);
        }

        productTable.setModel(tableModel);
    }

    // Helper method to get product details as a formatted string
    private String getProductDetails(Product product) {
        if (product instanceof Electronics) {
            return ((Electronics) product).getBrand();
        } else if (product instanceof Clothing) {
            return ((Clothing) product).getSize();
        }
        return ""; // Handle other product types as needed
    }



    private void addToCart() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            Product selectedProduct = shoppingManager.getProductList().get(selectedRow);

            // Ask for the quantity
            String quantityString = JOptionPane.showInputDialog(this, "Enter quantity:", "1");
            try {
                int quantity = Integer.parseInt(quantityString);
                shoppingCart.addProduct(selectedProduct, quantity); // Update this method to accept quantity
                JOptionPane.showMessageDialog(this, "Product added to the shopping cart.");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid quantity entered.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a product from the table.");
        }
    }


    private void displayShoppingCart() {
        if (cartFrame == null) { // Initialize the shopping cart frame if it doesn't exist
            cartFrame = new JFrame("Shopping Cart");
            cartFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); // Hide the frame instead of closing the application
            cartFrame.setSize(400, 300); // Set the initial size for the shopping cart frame
        } else {
            cartFrame.getContentPane().removeAll(); // Clear the previous content if the frame already exists
        }

        // Create a model for the shopping cart table
        DefaultTableModel cartTableModel = new DefaultTableModel();
        cartTableModel.addColumn("Product ID");
        cartTableModel.addColumn("Description");
        cartTableModel.addColumn("Quantity");
        cartTableModel.addColumn("Price");

        // Fill the table model with the shopping cart data
        for (Map.Entry<Product, Integer> entry : shoppingCart.getProducts().entrySet()) {
            Product product = entry.getKey();
            Integer quantity = entry.getValue();
            cartTableModel.addRow(new Object[]{
                    product.getProductId(),
                    product.getProductName(),
                    quantity,
                    String.format("£%.2f", product.getProductPrice())
            });
        }

        // Create the shopping cart table with the model
        JTable cartTable = new JTable(cartTableModel);
        JScrollPane tableScrollPane = new JScrollPane(cartTable); // Enable scrolling for the table

        // Calculate totals and discounts
        double totalWithoutDiscounts = shoppingCart.calculateTotalPrice();
        double firstPurchaseDiscount = loggedInUser.isFirstPurchase() ? totalWithoutDiscounts * 0.1 : 0.0;
        double categoryDiscount = shoppingCart.calculateCategoryDiscount();
        double finalPrice = totalWithoutDiscounts - firstPurchaseDiscount - categoryDiscount;

        // Create a text area to hold the totals and discounts information
        JTextArea totalsTextArea = new JTextArea();
        totalsTextArea.setEditable(false); // Make the text area non-editable
        totalsTextArea.append(String.format("Total: £%.2f\n", totalWithoutDiscounts));
        if (firstPurchaseDiscount > 0) {
            totalsTextArea.append(String.format("First Purchase Discount (10%%): -£%.2f\n", firstPurchaseDiscount));
        }

        if (categoryDiscount > 0) {
            totalsTextArea.append(String.format("Three Items in same Category Discount (20%%): -£%.2f\n", categoryDiscount));
        }
        totalsTextArea.append(String.format("Final Total: £%.2f", finalPrice));

// Create a panel to hold the table and the totals information
        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.add(tableScrollPane, BorderLayout.CENTER); // Add the table in the center
        cartPanel.add(totalsTextArea, BorderLayout.SOUTH); // Add the totals text area at the bottom

// Add the cart panel to the JFrame's content pane
        cartFrame.getContentPane().add(cartPanel);

        cartFrame.pack(); // Resize the frame to fit the content
        cartFrame.setLocationRelativeTo(null); // Center the frame
        cartFrame.setVisible(true); // Show the cart frame
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}