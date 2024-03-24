public interface ShoppingManager { // Interface definition for a ShoppingManager
    void addProduct(Product product);
    void deleteProduct(String productId);
    void printProductList();
    void saveProductListToFile(String fileName);
    void openGUI();

}
