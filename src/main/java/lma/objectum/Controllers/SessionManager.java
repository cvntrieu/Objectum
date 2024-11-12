
package lma.objectum.Controllers;

public class SessionManager { // Singleton: Lưu trữ thông tin toàn cục

    private static SessionManager instance;
    private String currentUsername;

    private SessionManager() {}

    public String getCurrentUsername() {
        return currentUsername;
    }

    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }

    /**
     * Singleton's get instance method.
     *
     * @return instance
     */
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
}
