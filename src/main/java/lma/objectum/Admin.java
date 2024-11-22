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

import java.io.IOException;

public class Admin extends User {

    @FXML
    private Button accountButton;

    @FXML
    protected Button homeButton;

    @FXML
    protected MenuButton listButton;

    @FXML
    protected Button avataButton;

    @FXML
    protected ImageView avataImage;

    @FXML
    protected MenuItem searchBooksMenuItem;

    @FXML
    protected MenuItem addBooksMenuItem;

    @FXML
    protected MenuItem removeBooksMenuItem;

    @FXML
    protected MenuItem editBooksMenuItem;

    @FXML
    protected MenuItem APIButton;

    @FXML
    protected MenuItem removeMembersMenuItem;

    @FXML
    protected MenuItem editMembersMenuItem;

    @FXML
    protected Region leftRegion;

    @FXML
    protected Button logoutButton;

    /**
     * Handing account viewing button.
     */
    @Override
    public void handleAccountButton() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lma/objectum/fxml/AccountView.fxml"));
            Parent root = loader.load();
            Stage removeMemberStage = new Stage();
            removeMemberStage.setScene(new Scene(root, 930, 650));
            accountButton.getScene().getWindow().hide();
            removeMemberStage.show();

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lma/objectum/fxml/AddBooks.fxml"));
            Parent root = loader.load();
            Stage addBooksStage = new Stage();
            Scene scene = new Scene(root, 950, 700);
            scene.getStylesheets().add(getClass().getResource("/lma/objectum/css/BookSearchStyle.css").toExternalForm());
            addBooksStage.setScene(scene);

            addBooksStage.setResizable(true); // Cho phép co giãn cửa sổ
            accountButton.getScene().getWindow().hide();
            addBooksStage.show();

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lma/objectum/fxml/DeleteBooks.fxml"));
            Parent root = loader.load();
            Stage removeMemberStage = new Stage();
            Scene scene = new Scene(root, 1150, 650);
            scene.getStylesheets().add(getClass().getResource("/lma/objectum/css/BookSearchStyle.css").toExternalForm());
            removeMemberStage.setScene(scene);
            accountButton.getScene().getWindow().hide();
            removeMemberStage.show();

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lma/objectum/fxml/EditBook.fxml"));
            Parent root = loader.load();
            Stage removeMemberStage = new Stage();
            Scene scene = new Scene(root, 920, 670);
            scene.getStylesheets().add(getClass().getResource("/lma/objectum/css/BookSearchStyle.css").toExternalForm());
            removeMemberStage.setScene(scene);
            accountButton.getScene().getWindow().hide();
            removeMemberStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handing removed members.
     */
    public void handleRemoveMembers() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lma/objectum/fxml/RemoveMembers.fxml"));
            Parent root = loader.load();
            Stage removeMemberStage = new Stage();
            removeMemberStage.setScene(new Scene(root, 930, 700));
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
            removeMemberStage.setScene(new Scene(root, 930, 700));
            accountButton.getScene().getWindow().hide();
            removeMemberStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handling borrowed books item.
     */
    public void handleBorrowBooksItem() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lma/objectum/fxml/BookSearch.fxml"));
            Parent root = loader.load();
            Stage removeMemberStage = new Stage();
            Scene scene = new Scene(root, 1151, 622);
            scene.getStylesheets().add(getClass().getResource("/lma/objectum/css/BookSearchStyle.css").toExternalForm());
            removeMemberStage.setScene(scene);
            accountButton.getScene().getWindow().hide();
            removeMemberStage.show();

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lma/objectum/fxml/API.fxml"));
            Parent root = loader.load();
            Stage apiStage = new Stage();
            Scene scene = new Scene(root);
            apiStage.setScene(scene);
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/lma/objectum/fxml/App.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) logoutButton.getScene().getWindow();
                stage.setScene(new Scene(root, 842, 608));
                stage.show();

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
