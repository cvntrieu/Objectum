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
import org.mindrot.jbcrypt.BCrypt;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
    private Button registerButton;

    @FXML
    private Label registerMessageLabel;

    @FXML
    private Button backTologinButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Any required initialization code goes here
    }

    /**
     * Registers a user.
     *
     * @param event ActionEvent
     */
    public void registerButtonOnAction(ActionEvent event) {
        if (!usernameTextField.getText().isBlank() && !passwordTextField.getText().isBlank()
                && !firstnameTextField.getText().isBlank() && !lastnameTextField.getText().isBlank()) {
            registerUser(event);
            registerMessageLabel.setText("Registered successfully!");

        } else {
            registerMessageLabel.setText("Please enter full fields!");
        }
    }

    /**
     * Handles the Back to Login button action.
     *
     * @param event ActionEvent
     */
    public void backToLoginButtonOnAction(ActionEvent event) {
        redirectToLogin(event);
    }

    /**
     * Registers a user.
     *
     * @param event ActionEvent
     */
    private void registerUser(ActionEvent event) {
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
     * Redirects to the login page.
     *
     * @param event ActionEvent
     */
    private void redirectToLogin(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/lma/objectum/fxml/App.fxml"));
            Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            loginStage.setScene(new Scene(root, 842, 608));
            loginStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

}


