
package lma.objectum.Controllers;

public class SessionManager {

    private static SessionManager instance;
    private String currentUsername;
    private int currentUserId;

    private SessionManager() {
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    public int getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }

    public void setCurrentUserId(int userId) {
        this.currentUserId = userId;
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
