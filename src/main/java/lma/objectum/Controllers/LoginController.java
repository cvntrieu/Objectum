package lma.objectum.Controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import lma.objectum.Database.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

public class LoginController {

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Button logInButton;

    @FXML
    private Button registerButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label loginMessageLabel;

    @FXML
    private ImageView brandingImageView;

    public void initialize(URL url, ResourceBundle rb) {

        System.out.println("Initialize method called");
        File brandingFile = new File("MyBook.jpg");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);

        // If there are more images, code like above here...
    }

    /**
     * Closes the login form.
     *
     * @param e ActionEvent
     */
    public void cancelButtonOnAction(ActionEvent e) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Closes the login form.
     *
     * @param event MouseEvent
     */
    public void loginButtonOnAction(ActionEvent event) {
        if (!usernameTextField.getText().isBlank() && !passwordTextField.getText().isBlank()) {
            loginMessageLabel.setText("Trying!");
            validateLogin();
        } else {
            loginMessageLabel.setText("Please enter Username and Password!");
        }
    }

    /**
     * Go to register form.
     */
    public void registerButtonOnAction(ActionEvent event) {

        Stage stage = (Stage) registerButton.getScene().getWindow();
        stage.close();
        createAccountForm();
    }

    /**
     * open main page.
     */
    private void showHomePage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lma/objectum/fxml/home.fxml"));
            Parent root = loader.load();
            Stage homeStage = new Stage();
            homeStage.setScene(new Scene(root));
            homeStage.setTitle("Main Application");
            homeStage.show();
            Stage loginStage = (Stage) logInButton.getScene().getWindow();
            loginStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            loginMessageLabel.setText("Could not load the main interface.");
        }
    }

    /**
     * Validates the login credentials.
     */
    private void validateLogin() {
        DatabaseConnection connectNow = DatabaseConnection.getInstance();
        Connection connectDB = connectNow.getConnection();

        String verifyLogin = "SELECT password FROM useraccount WHERE username = ?";

        try {
            PreparedStatement preparedStatement = connectDB.prepareStatement(verifyLogin);
            preparedStatement.setString(1, usernameTextField.getText());

            ResultSet queryResult = preparedStatement.executeQuery();

            if (queryResult.next()) {
                String hashedPassword = queryResult.getString("password");

                if (BCrypt.checkpw(passwordTextField.getText(), hashedPassword)) {
                    loginMessageLabel.setText("Login successful! Welcome!");
                    showHomePage();
                } else {
                    loginMessageLabel.setText("Invalid login. Please try again.");
                }
            } else {
                loginMessageLabel.setText("Username not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            loginMessageLabel.setText("An error occurred while trying to log in.");
        }
    }

    private void createAccountForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lma/objectum/fxml/SignUp.fxml"));
            Parent root = loader.load();
            Stage registerStage = new Stage();
            registerStage.setScene(new Scene(root, 842, 608));
            registerStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}