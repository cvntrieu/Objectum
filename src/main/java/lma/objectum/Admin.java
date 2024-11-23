package lma.objectum;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import lma.objectum.Controllers.SessionManager;
import lma.objectum.Controllers.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import lma.objectum.Utils.StageUtils;

import java.io.IOException;

public class Admin extends User {

    @FXML
    private Button accountButton;

    @FXML
    private Button homeButton;

    @FXML
    private MenuButton listButton;

    @FXML
    private Button avataButton;

    @FXML
    private ImageView avataImage;

    @FXML
    private MenuItem searchBooksMenuItem;

    @FXML
    private MenuItem addBooksMenuItem;

    @FXML
    private MenuItem removeBooksMenuItem;

    @FXML
    private MenuItem editBooksMenuItem;

    @FXML
    private MenuItem APIButton;

    @FXML
    private MenuItem removeMembersMenuItem;

    @FXML
    private MenuItem editMembersMenuItem;

    @FXML
    private Region leftRegion;

    @FXML
    private Button logoutButton;

    /**
     * Handing account viewing button.
     */
    @Override
    public void handleAccountButton() {

        try {
            Stage accountStage = StageUtils.loadFXMLStage(
                    "/lma/objectum/fxml/AccountView.fxml",
                    "Account View"
            );
            accountStage.show();
            accountButton.getScene().getWindow().hide();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handling added books.
     */
    @FXML
    private void handleAddBooks() {

        try {
            Stage addBooksStage = StageUtils.loadFXMLStage(
                    "/lma/objectum/fxml/AddBooks.fxml",
                    "Add Books"
            );
            addBooksStage.setResizable(true); // Cho phép co giãn cửa sổ
            addBooksStage.show();
            accountButton.getScene().getWindow().hide();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handling removed books.
     */
    @FXML
    private void handleRemoveBooks() {

        try {
            Stage deleteBookStage = StageUtils.loadFXMLStageWithCSS(
                    "/lma/objectum/fxml/DeleteBooks.fxml",
                    "/lma/objectum/css/BookSearchStyle.css",
                    "Remove Books"
            );
            deleteBookStage.show();
            accountButton.getScene().getWindow().hide();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handling edited books.
     */
    @FXML
    private void handleEditBooks() {

        try {
            Stage editBookStage = StageUtils.loadFXMLStage(
                    "/lma/objectum/fxml/EditBook.fxml",
                    "Edit Book"
            );
            editBookStage.show();
            accountButton.getScene().getWindow().hide();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handing removed members.
     */
    public void handleRemoveMembers() {

        try {
            Stage removeMemberStage = StageUtils.loadFXMLStage(
                    "/lma/objectum/fxml/RemoveMembers.fxml",
                    "Remove Members"
            );
            removeMemberStage.show();
            accountButton.getScene().getWindow().hide();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handling edited members.
     */
    public void handleEditMembers() { // Member <-> Admin

        try {
            Stage removeMemberStage = StageUtils.loadFXMLStage(
                    "/lma/objectum/fxml/EditMembers.fxml",
                    "Edit Members"
            );
            removeMemberStage.show();

            accountButton.getScene().getWindow().hide();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handling borrowed books item.
     */
    public void handleBorrowBooksItem() {

        try {
            Stage borrowBooksStage = StageUtils.loadFXMLStageWithCSS(
                    "/lma/objectum/fxml/BookSearch.fxml",
                    "/lma/objectum/css/BookSearchStyle.css",
                    "Borrow Books"
            );
            borrowBooksStage.show();
            accountButton.getScene().getWindow().hide();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * API Button on action.
     */
    @FXML
    public void APIButtonOnAction() {
        try {
            Stage apiStage = StageUtils.loadFXMLStage(
                    "/lma/objectum/fxml/API.fxml",
                    "API"
            );
            apiStage.show();
            // Đóng màn hình cũ
            Stage homeStage = (Stage) accountButton.getScene().getWindow();
            homeStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logging out.
     */
    @FXML
    public void handleLogoutButton() {

        Alert logoutConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
        logoutConfirmation.setTitle("Confirm Logout");
        logoutConfirmation.setHeaderText("Are you sure you want to log out?");
        logoutConfirmation.setContentText("You will need to log in again to access your account.");

        if (logoutConfirmation.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {

            SessionManager.getInstance().clearSession(); // Xóa thông tin phiên đăng nhập

            try {
                Stage homeStage = StageUtils.loadFXMLStage(
                        "/lma/objectum/fxml/App.fxml",
                        "Objectum Library"
                );
                homeStage.show();

                Stage adminStage = (Stage) logoutButton.getScene().getWindow();
                adminStage.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Showing an alert if necessary.
     *
     * @param title  title of the alert
     * @param message message of the alert
     */
    public void showAlert(String title, String message) {

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
