package lma.objectum.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import lma.objectum.Database.DatabaseConnection;
import lma.objectum.Utils.StageUtils;
import org.mindrot.jbcrypt.BCrypt;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {

    @FXML
    private TextField firstnameTextField;

    @FXML
    private TextField lastnameTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Label registerMessageLabel;

    /**
     * Intializing methods.
     *
     * @param url url
     * @param resourceBundle rb
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Any required initialization code goes here
    }

    /**
     * Register a new member.
     *
     * @param event event
     */
    public void registerButtonOnAction(ActionEvent event) throws SQLException {
        if (!usernameTextField.getText().isBlank() && !passwordTextField.getText().isBlank()
                && !firstnameTextField.getText().isBlank() && !lastnameTextField.getText().isBlank()) {

            if (!isUsernameExists(usernameTextField.getText())) {
                registerUser(event);
                registerMessageLabel.setText("Registered successfully!");
            } else {
                registerMessageLabel.setText("Username has already existed. Please choose a different username.");
            }

        } else {
            registerMessageLabel.setText("Please enter full fields!");
        }
    }

    /**
     * Back to Log in.
     *
     * @param event event
     */
    public void backToLoginButtonOnAction(ActionEvent event) {
        redirectToLogin(event);
    }

    /**
     * Is username existed.
     *
     * @param username username
     * @return true or false
     */
    private boolean isUsernameExists(String username) throws SQLException {
        DatabaseConnection connectNow = DatabaseConnection.getInstance();
        Connection connectDB = connectNow.getConnection();

        String checkUsernameQuery = "SELECT username FROM useraccount WHERE username = ?";
        try {
            PreparedStatement preparedStatement = connectDB.prepareStatement(checkUsernameQuery);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Trả về true nếu tìm thấy username đã tồn tại
            return resultSet.next();

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
            return false;
        }
    }

    /**
     * Register a new member.
     *
     * @param event event
     */
    private void registerUser(ActionEvent event) throws SQLException {
        DatabaseConnection connectNow = DatabaseConnection.getInstance();
        Connection connectDB = connectNow.getConnection();

        String firstname = firstnameTextField.getText();
        String lastname = lastnameTextField.getText();
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        String insertToRegister = "INSERT INTO useraccount(lastname, firstname, username, password) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connectDB.prepareStatement(insertToRegister);
            preparedStatement.setString(1, lastname);
            preparedStatement.setString(2, firstname);
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, hashedPassword);

            preparedStatement.executeUpdate();
            registerMessageLabel.setText("User has been registered successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    /**
     * Back button on action
     *
     * @param event event
     */
    private void redirectToLogin(ActionEvent event) {
        try {
            Stage loginStage = StageUtils.loadFXMLStage(
                    "/lma/objectum/fxml/App.fxml",
                    "Login"
            );

            lastnameTextField.getScene().getWindow().hide();
            loginStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
}
