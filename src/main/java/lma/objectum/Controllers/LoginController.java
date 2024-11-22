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

import javafx.animation.*;
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

    /**
     * Initializing Sign-In for members.
     *
     * @param url url of jdbc-local host
     * @param rb resource bundle
     */
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
    public void loginButtonOnAction(ActionEvent event) throws SQLException {
        if (!usernameTextField.getText().isBlank() && !passwordTextField.getText().isBlank()) {
            loginMessageLabel.setText("Trying!");
            loginMessageLabel.getStyleClass().clear();
            loginMessageLabel.getStyleClass().add("warning-label");
            setTimeline();
            validateLogin();
        } else {
            loginMessageLabel.setText("Please enter Username and Password!");
            loginMessageLabel.getStyleClass().clear();
            loginMessageLabel.getStyleClass().add("warning-label");
            setTimeline();
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lma/objectum/fxml/Home.fxml"));
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
            loginMessageLabel.getStyleClass().clear();
            loginMessageLabel.getStyleClass().add("warning-label");
            setTimeline();
        }
    }

    /**
     * Admin homepage.
     */
    private void showAdminHomePage() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lma/objectum/fxml/AdminHome.fxml"));
            Parent root = loader.load();
            Stage adminStage = new Stage();
            adminStage.setScene(new Scene(root, 1080, 790));
            adminStage.setTitle("Admin interface");
            adminStage.show();

            Stage loginStage = (Stage) logInButton.getScene().getWindow();
            loginStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            loginMessageLabel.setText("Could not load the admin interface.");
            loginMessageLabel.getStyleClass().clear();
            loginMessageLabel.getStyleClass().add("warning-label");
            setTimeline();
        }
    }

    /**
     * Validates the login credentials.
     */
    private void validateLogin() throws SQLException {

        DatabaseConnection connectNow = DatabaseConnection.getInstance();
        Connection connectDB = connectNow.getConnection();
        String verifyLogin = "SELECT id, password, role FROM useraccount WHERE username = ?";

        try {

            PreparedStatement preparedStatement = connectDB.prepareStatement(verifyLogin);
            preparedStatement.setString(1, usernameTextField.getText());
            ResultSet queryResult = preparedStatement.executeQuery();

            if (queryResult.next()) {

                String hashedPassword = queryResult.getString("password");
                String role = queryResult.getString("role");
                int userId = queryResult.getInt("id");

                if (BCrypt.checkpw(passwordTextField.getText(), hashedPassword)) {

                    SessionManager.getInstance().setCurrentUsername(usernameTextField.getText());
                    SessionManager.getInstance().setCurrentUserId(userId);
                    // getInstance() of Singleton

                    if (("member".equalsIgnoreCase(role))) {
                        loginMessageLabel.setText("Login Member successful! Welcome!");
                        loginMessageLabel.getStyleClass().clear();
                        loginMessageLabel.getStyleClass().add("success-label");
                        PauseTransition pause = new PauseTransition(Duration.seconds(3));
                        pause.setOnFinished(event -> showHomePage());
                        pause.play();

                    } else {
                        loginMessageLabel.setText("Login Admin successful! Welcome!");
                        loginMessageLabel.getStyleClass().clear();
                        loginMessageLabel.getStyleClass().add("success-label");
                        PauseTransition pause = new PauseTransition(Duration.seconds(3));
                        pause.setOnFinished(event -> showAdminHomePage());
                        pause.play();
                    }
                } else {
                    loginMessageLabel.setText("Invalid login. Please try again.");
                    loginMessageLabel.getStyleClass().clear();
                    loginMessageLabel.getStyleClass().add("warning-label");
                    setTimeline();
                }
            } else {
                loginMessageLabel.setText("Username not found.");
                loginMessageLabel.getStyleClass().clear();
                loginMessageLabel.getStyleClass().add("warning-label");
                setTimeline();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            loginMessageLabel.setText("An error occurred while trying to log in.");
            loginMessageLabel.getStyleClass().clear();
            loginMessageLabel.getStyleClass().add("warning-label");
            setTimeline();
        }
    }

    /**
     * Creating an account form.
     */
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

    /**
     * Create a timeline to clear the message after 10 seconds.
     */
    private void setTimeline() {

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
            loginMessageLabel.setText("");
            loginMessageLabel.getStyleClass().clear();
        }));
        timeline.setCycleCount(1); // Run only once
        timeline.play();
    }
}