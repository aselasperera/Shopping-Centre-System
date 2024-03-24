import java.io.Serializable;

public class User implements Serializable { // User class representing a user in the system, implements Serializable for object serialization
    private String username; // User's username
    private String password; // User's password
    private boolean firstPurchase; // Flag indicating whether it's the user's first purchase

    public User(String username, String password) { // Constructor to initialize a User object with a username, password, and set firstPurchase to true
        this.username = username;
        this.password = password;
        this.firstPurchase=true;
    }

    public String getUsername() {
        return username;
    } // Getter method to retrieve the username

    public void setUsername(String username) {
        this.username = username;
    } // Setter method to set the username

    public String getPassword() {
        return password;
    } // Getter method to retrieve the password

    public void setPassword(String password) {
        this.password = password;
    } // Setter method to set the password

    public boolean isFirstPurchase(){
        return firstPurchase;
    } // Getter method to check if it's the user's first purchase

    public void setFirstPurchase(boolean firstPurchase){
        this.firstPurchase=firstPurchase;
    } // Setter method to update the firstPurchase flag
}