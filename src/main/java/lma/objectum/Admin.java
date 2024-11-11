package lma.objectum;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lma.objectum.Controllers.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import lma.objectum.Database.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Admin extends User {

    @FXML
    public Button accountButton;

    @FXML
    public Button homeButton;

    @FXML
    public MenuButton listButton;

    @FXML
    public Button settingButton;

    @FXML
    public Button avataButton;

    @FXML
    public ImageView avataImage;

    @FXML
    public MenuItem searchBooksMenuItem;

    @FXML
    private MenuItem addBooksMenuItem;

    @FXML
    private MenuItem removeBooksMenuItem;

    @FXML
    private MenuItem editBooksMenuItem;

    @FXML
    private MenuItem addMembersMenuItem;

    @FXML
    private MenuItem removeMembersMenuItem;

    @FXML
    private MenuItem editMembersMenuItem;

    @FXML
    public void initialize() {

    }

    /**
     * Handling added books.
     */
    private void handleAddBooks() {
        showAlert("Add Books", "You have selected to add books.");
        // Thêm logic thực hiện hành động thêm sách
    }

    /**
     * Handling removed books.
     */
    private void handleRemoveBooks() {
        showAlert("Remove Books", "You have selected to remove books.");
        // Thêm logic thực hiện hành động xóa sách
    }

    /**
     * Handling edited books.
     */
    private void handleEditBooks() {
        showAlert("Edit Books", "You have selected to edit books.");
        // Thêm logic thực hiện hành động chỉnh sửa sách
    }

    /**
     * Handing added members.
     */
    public void handleAddMembers() {
        // showAlert("Add Members", "You have selected to add members.");
        // Thêm logic thực hiện hành động thêm thành viên
    }

    /**
     * Handing removed members.
     */
    public void handleRemoveMembers() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lma/objectum/fxml/RemoveMembers.fxml"));
            Parent root = loader.load();
            Stage removeMemberStage = new Stage();
            removeMemberStage.setScene(new Scene(root, 842, 608));
            accountButton.getScene().getWindow().hide();
            removeMemberStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handling edited members.
     */
    public void handleEditMembers() { // Member <-> Admin

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lma/objectum/fxml/EditMembers.fxml"));
            Parent root = loader.load();
            Stage removeMemberStage = new Stage();
            removeMemberStage.setScene(new Scene(root, 842, 608));
            accountButton.getScene().getWindow().hide();
            removeMemberStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void showAlert(String title, String message) {

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
