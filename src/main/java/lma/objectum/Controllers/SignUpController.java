package lma.objectum.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import lma.objectum.Database.DatabaseConnection;
import org.controlsfx.control.action.Action;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ResourceBundle;


public class SignUpController implements Initializable {

    @FXML
    private Label registrationMessageLabel;

    @FXML
    private TextField firstnameTextField;

    @FXML
    private TextField lastnameTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField passwordTextField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void registerButtonOnAction(ActionEvent event) {
        registerUser();
    }

    public void registerUser() {

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String firstname = firstnameTextField.getText();
        String lastname = lastnameTextField.getText();
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        String insertFields = "INSERT INTO... VALUES ('";
        String insertValues = firstname + "','" + lastname + "','" + username + "','" + password + " ')";
        String insertToRegister = insertFields + insertValues;

        try{
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(insertToRegister);
            registrationMessageLabel.setText("Registered successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
}
