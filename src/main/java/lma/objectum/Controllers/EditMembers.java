
package lma.objectum.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import lma.objectum.Database.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditMembers {

    @FXML
    public ImageView avataImage;

    @FXML
    public Button avataButton;

    @FXML
    public Label guideLabel;

    @FXML
    public TextField editTextField;

    @FXML
    public Label editMessageLabel;

    @FXML
    public Button toAdminButton;

    @FXML
    public Button toMemberButton;

    @FXML
    public Button backButton;

    /**
     * Initializing method.
     */
    @FXML
    public void initialize() {
    }

    /**
     * Member to Admin.
     */
    public void toAdminButtonOnAction() throws SQLException {

        String username = editTextField.getText();
        if (!username.isBlank()) {

            DatabaseConnection connectNow = DatabaseConnection.getInstance();
            Connection connectDB = connectNow.getConnection();
            String query = "SELECT role FROM useraccount WHERE username = ?";
            String updateQuery = "UPDATE useraccount SET role = 'admin' WHERE username = ?";

            try {

                PreparedStatement preparedStatement = connectDB.prepareStatement(query);
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String role = resultSet.getString("role");
                    if ("admin".equalsIgnoreCase(role)) {

                        editMessageLabel.setText("This account has been an admin!");
                        editMessageLabel.getStyleClass().clear();
                        editMessageLabel.getStyleClass().add("warning-label");

                    } else if ("member".equalsIgnoreCase(role)) {

                        PreparedStatement updateStatement = connectDB.prepareStatement(updateQuery);
                        updateStatement.setString(1, username);
                        updateStatement.executeUpdate();

                        editMessageLabel.setText("Role updated to Admin successfully!");
                        editMessageLabel.getStyleClass().clear();
                        editMessageLabel.getStyleClass().add("success-label");
                    }

                } else {

                    editMessageLabel.setText("Username not found!");
                    editMessageLabel.getStyleClass().clear();
                    editMessageLabel.getStyleClass().add("warning-label");
                }
            } catch (SQLException e) {

                e.printStackTrace();
                editMessageLabel.setText("Database error!");
                editMessageLabel.getStyleClass().clear();
                editMessageLabel.getStyleClass().add("error-label");
            }

        } else {

            editMessageLabel.setText("Blank!");
            editMessageLabel.getStyleClass().clear();
            editMessageLabel.getStyleClass().add("warning-label");
        }
    }

    /**
     * Admin to Member.
     */
    public void toMemberButtonOnAction() throws SQLException {

        String username = editTextField.getText();
        if (!username.isBlank()) {

            DatabaseConnection connectNow = DatabaseConnection.getInstance();
            Connection connectDB = connectNow.getConnection();
            String query = "SELECT role FROM useraccount WHERE username = ?";
            String updateQuery = "UPDATE useraccount SET role = 'member' WHERE username = ?";

            try {
                PreparedStatement preparedStatement = connectDB.prepareStatement(query);
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String role = resultSet.getString("role");

                    if ("member".equalsIgnoreCase(role)) {

                        editMessageLabel.setText("This account has been a member!");
                        editMessageLabel.getStyleClass().clear();
                        editMessageLabel.getStyleClass().add("warning-label");
                    } else if ("admin".equalsIgnoreCase(role)) {

                        PreparedStatement updateStatement = connectDB.prepareStatement(updateQuery);
                        updateStatement.setString(1, username);
                        updateStatement.executeUpdate();

                        editMessageLabel.setText("Role updated to Member successfully!");
                        editMessageLabel.getStyleClass().clear();
                        editMessageLabel.getStyleClass().add("success-label");
                    }
                } else {
                    editMessageLabel.setText("Username not found!");
                    editMessageLabel.getStyleClass().clear();
                    editMessageLabel.getStyleClass().add("warning-label");
                }

            } catch (SQLException e) {

                e.printStackTrace();
                editMessageLabel.setText("Database error!");
                editMessageLabel.getStyleClass().clear();
                editMessageLabel.getStyleClass().add("error-label");
            }

        } else {
            editMessageLabel.setText("Blank!");
            editMessageLabel.getStyleClass().clear();
            editMessageLabel.getStyleClass().add("warning-label");
        }
    }

    /**
     * Back Button on action.
     *
     * @param event e
     */
    public void redirectToHome(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/lma/objectum/fxml/AdminHome.fxml"));
            Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            loginStage.setScene(new Scene(root, 1200, 800));
            loginStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
}

