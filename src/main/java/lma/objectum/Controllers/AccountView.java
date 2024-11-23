
package lma.objectum.Controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import lma.objectum.Database.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class AccountView {

    @FXML
    protected Button avataButton;

    @FXML
    protected ImageView avataImage;

    @FXML
    protected Label editMessageLabel;

    @FXML
    protected Label usernameLabel;

    @FXML
    protected Label changeGuideLabel;

    @FXML
    protected Button applyPassButton;

    @FXML
    protected PasswordField newPassTextField;

    @FXML
    protected Label openingLabel;

    /**
     * Intializing the view interface.
     */
    public void initialize() throws SQLException {
        // Các phương thức dùng trực tiếp hoặc gắn liền nút bấm, sự kịiện phải để public để file fxml còn đọc
        loadUserInfo();
    }

    /**
     * Loading users' personal infomation.
     */
    private void loadUserInfo() throws SQLException {

        String currentUsername = SessionManager.getInstance().getCurrentUsername();
        if (currentUsername == null) {
            return;
        }

        DatabaseConnection connectNow = DatabaseConnection.getInstance();
        Connection connectDB = connectNow.getConnection();
        String query = "SELECT username, firstname, lastname, role FROM useraccount WHERE username = ?";

        try {

            PreparedStatement statement = connectDB.prepareStatement(query);
            statement.setString(1, currentUsername);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                usernameLabel.setText("Username: " + resultSet.getString("username") + '\n'
                        + "First Name: " + resultSet.getString("firstname") + '\n'
                        + "Last Name: " + resultSet.getString("lastname") + '\n'
                        + "Role: " + resultSet.getString("role"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Changing password.
     *
     * @param username    the username to change its password
     * @param newPassword the new password
     */
    private void updatePassword(String username, String newPassword) throws SQLException {

        DatabaseConnection connectNow = DatabaseConnection.getInstance();
        Connection connectDB = connectNow.getConnection();
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        String updateQuery = "UPDATE useraccount SET password = ? WHERE username = ?";

        try {
            PreparedStatement statement = connectDB.prepareStatement(updateQuery);
            statement.setString(1, hashedPassword);
            statement.setString(2, username);
            statement.executeUpdate();

            statement.close();
            connectDB.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handling the changing password process.
     */
    public void handleApplyPassword() throws SQLException {

        String currentUsername = SessionManager.getInstance().getCurrentUsername();
        // getInstance() of Singleton
        if (currentUsername == null) {
            editMessageLabel.setText("Please log in first.");
            editMessageLabel.getStyleClass().clear();
            editMessageLabel.getStyleClass().add("warning-label");
            setTimeline();
            return; // Hành vi tự động xóa thông báo vẫn được thực hiện ngay cả khi phương thức dừng sớm
        }

        String newPassword = newPassTextField.getText();

        if (newPassword.isEmpty()) {
            editMessageLabel.setText("Blank");
            editMessageLabel.getStyleClass().clear();
            editMessageLabel.getStyleClass().add("warning-label");
            setTimeline();
            return;
        }

        DatabaseConnection connectNow = DatabaseConnection.getInstance();
        Connection connectDB = connectNow.getConnection();
        String fetchPasswordQuery = "SELECT password FROM useraccount WHERE username = ?";

        try {

            PreparedStatement fetchStatement = connectDB.prepareStatement(fetchPasswordQuery);
            fetchStatement.setString(1, currentUsername);
            ResultSet resultSet = fetchStatement.executeQuery();

            if (resultSet.next()) {
                String currentHashedPassword = resultSet.getString("password");

                // Kiểm tra mật khẩu mới giống mật khẩu cũ hay không?
                if (BCrypt.checkpw(newPassword, currentHashedPassword)) {

                    editMessageLabel.setText("Password hasn't been changed!");
                    editMessageLabel.getStyleClass().clear();
                    editMessageLabel.getStyleClass().add("warning-label");
                    setTimeline();
                    return;
                }
            }

            updatePassword(currentUsername, newPassword);
            editMessageLabel.setText("Password updated successfully!");
            editMessageLabel.getStyleClass().clear();
            editMessageLabel.getStyleClass().add("success-label");
            setTimeline();

        } catch (SQLException e) {

            e.printStackTrace();
            editMessageLabel.setText("An error occurred while updating the password.");
            editMessageLabel.getStyleClass().clear();
            editMessageLabel.getStyleClass().add("warning-label");
            setTimeline();
        }
    }

    /**
     * Create a timeline to clear the message after 10 seconds.
     */
    private void setTimeline() {

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
            editMessageLabel.setText("");
            editMessageLabel.getStyleClass().clear();
        }));
        timeline.setCycleCount(1); // Run only once
        timeline.play();
    }
}
