
package lma.objectum.Controllers;

public class SessionManager {

    private static SessionManager instance;
    private String currentUsername;
    private int currentUserId;

    /**
     * Default constr of Singleton.
     */
    private SessionManager() {}

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

    /**
     * Clearing the info when logging out. Reset user-related session data to null or default values.
     */
    public void clearSession() {

        this.currentUsername = null;
        this.currentUserId = 0; // 0 is typically used as a default invalid ID
        // Các thông tin phiên đã bị xóa, ngăn ngừa việc truy cập trái phép.
        // Nhưng đối tượng SessionManager vẫn tồn tại để sử dụng cho lần đăng nhập sau.
    }
}
