package lma.objectum.Controllers;

import com.jfoenix.controls.JFXButton;

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

public class LoginController {

    @FXML
    public Button logInButton;

    @FXML
    private Label loginMessageLabel;

    @FXML
    private Button registerButton;

    @FXML
    private ImageView brandingImageView;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;


    public void initialize(URL url, ResourceBundle rb) {

        File brandingFile = new File("D:\\Objectum\\src\\main\\resources\\lma\\objectum\\images\\MyBook.jpg"); // Absolute Path
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);

        // If there are more images, code like above here...
    }

    public void registerButtonOnAction(ActionEvent event) {

        Stage stage = (Stage) registerButton.getScene().getWindow();
        stage.close();
        createAccountForm();
    }

    public void loginButtonOnAction(ActionEvent event) {
        System.out.println("Login button clicked!");
        if (!usernameField.getText().isBlank() && !passwordField.getText().isBlank()) {
            validateLogin();
            // loginMessageLabel.setText("You try to login!");
        } else {
            loginMessageLabel.setText("Please enter Username and Password!");
        }
    }

    public void validateLogin() {

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectionDB = connectNow.getConnection();

        String verifyLogin = "SELECT..." + usernameField.getText() + "..." + passwordField.getText();

        try {

            Statement statement = connectionDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while (queryResult.next()) {
                if (queryResult.getInt(1) == 1) {
                    loginMessageLabel.setText("Congrats!"); // Main Library methods go here..........................
                    // createAccountForm();
                } else {
                    loginMessageLabel.setText("Invalid!");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void createAccountForm() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lma/objectum/fxml/SignUp.fxml"));
            Parent root = loader.load();
            Stage registerStage = new Stage();
            registerStage.initStyle(StageStyle.UNDECORATED);
            registerStage.setScene(new Scene(root,850, 800));
            registerStage.show();

        } catch(Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
}