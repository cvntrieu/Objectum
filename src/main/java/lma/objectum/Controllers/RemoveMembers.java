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

public class RemoveMembers {

    @FXML
    public ImageView avataImage;

    @FXML
    public Button avataButton;

    @FXML
    public Label guideLabel;

    @FXML
    public Button backButton;

    @FXML
    public Label deleteMemberMessageLabel;

    @FXML
    public Button deleteButton;

    @FXML
    public TextField deleteTextField;

    @FXML
    public void initialize() {
    }

    public void deleteButtonOnAction() {

        String username = deleteTextField.getText();
        if (!username.isBlank()) {

            DatabaseConnection connectNow = DatabaseConnection.getInstance();
            Connection connectDB = connectNow.getConnection();
            String checkUserQuery = "SELECT * FROM useraccount WHERE username = ?";
            String deleteUserQuery = "DELETE FROM useraccount WHERE username = ?";

            try {
                // Kiểm tra xem username có tồn tại trong cơ sở dữ liệu không
                PreparedStatement checkUserStatement = connectDB.prepareStatement(checkUserQuery);
                checkUserStatement.setString(1, username);
                ResultSet userResult = checkUserStatement.executeQuery();

                if (userResult.next()) {
                    String role = userResult.getString("role");

                    if ("admin".equalsIgnoreCase(role)) {

                        deleteMemberMessageLabel.setText("Cannot delete an admin!");
                        deleteMemberMessageLabel.getStyleClass().clear();
                        deleteMemberMessageLabel.getStyleClass().add("warning-label");
                    } else {

                        PreparedStatement deleteUserStatement = connectDB.prepareStatement(deleteUserQuery);
                        deleteUserStatement.setString(1, username);
                        int rowsAffected = deleteUserStatement.executeUpdate();

                        if (rowsAffected > 0) {
                            deleteMemberMessageLabel.setText("Member deleted successfully!");
                            deleteMemberMessageLabel.getStyleClass().clear();
                            deleteMemberMessageLabel.getStyleClass().add("success-label");
                        } else {
                            deleteMemberMessageLabel.setText("Failed to delete member. Please try again.");
                            deleteMemberMessageLabel.getStyleClass().clear();
                            deleteMemberMessageLabel.getStyleClass().add("warning-label");
                        }
                    }
                } else {
                    deleteMemberMessageLabel.setText("Username not found!");
                    deleteMemberMessageLabel.getStyleClass().clear();
                    deleteMemberMessageLabel.getStyleClass().add("warning-label");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                deleteMemberMessageLabel.setText("An error occurred while trying to delete the member.");
                deleteMemberMessageLabel.getStyleClass().clear();
                deleteMemberMessageLabel.getStyleClass().add("warning-label");
            }
        } else {
            deleteMemberMessageLabel.setText("Blank!");
            deleteMemberMessageLabel.getStyleClass().clear();
            deleteMemberMessageLabel.getStyleClass().add("warning-label");
        }
    }


    public void redirectToHome(ActionEvent event) { // BackButtonOnAction

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/lma/objectum/fxml/AdminHome.fxml"));
            Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            loginStage.setScene(new Scene(root, 842, 608));
            loginStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
}
