package lma.objectum;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.image.ImageView;
import lma.objectum.Controllers.User;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

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

        addBooksMenuItem.setOnAction(event -> handleAddBooks());

        removeBooksMenuItem.setOnAction(event -> handleRemoveBooks());

        editBooksMenuItem.setOnAction(event -> handleEditBooks());

        addMembersMenuItem.setOnAction(event -> handleAddMembers());

        removeMembersMenuItem.setOnAction(event -> handleRemoveMembers());

        editMembersMenuItem.setOnAction(event -> handleEditMembers());
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
    private void handleAddMembers() {
        showAlert("Add Members", "You have selected to add members.");
        // Thêm logic thực hiện hành động thêm thành viên
    }

    /**
     * Handing removed members.
     */
    private void handleRemoveMembers() {
        showAlert("Remove Members", "You have selected to remove members.");
        // Thêm logic thực hiện hành động xóa thành viên
    }

    /**
     * Handling edited members.
     */
    private void handleEditMembers() {
        showAlert("Edit Members", "You have selected to edit members.");
        // Thêm logic thực hiện hành động chỉnh sửa thành viên
    }


    private void showAlert(String title, String message) {

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
