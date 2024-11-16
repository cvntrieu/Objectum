
package lma.objectum.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import lma.objectum.Database.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class AccountView {

    @FXML
    public Button avataButton;

    @FXML
    public ImageView avataImage;

    @FXML
    public Label editMessageLabel;

    @FXML
    public Label usernameLabel;

    @FXML
    public Label changeGuideLabel;

    @FXML
    public Button applyPassButton;

    @FXML
    public PasswordField newPassTextField;

    @FXML
    public Label openingLabel;

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
        if (currentUsername == null) { return; }

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
     * @param username the username to change its password
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
            return;
        }

        String newPassword = newPassTextField.getText();

        if (newPassword.isEmpty()) {
            editMessageLabel.setText("Blank");
            editMessageLabel.getStyleClass().clear();
            editMessageLabel.getStyleClass().add("warning-label");
        } else {
            updatePassword(currentUsername, newPassword);
            editMessageLabel.setText("Apply new Password!");
            editMessageLabel.getStyleClass().clear();
            editMessageLabel.getStyleClass().add("success-label");
        }
    }

}
