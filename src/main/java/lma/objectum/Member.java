
package lma.objectum;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lma.objectum.Controllers.User;

import java.io.IOException;

public class Member extends User {

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
    public MenuItem borrowBooksItem;

    @FXML
    public MenuItem returnBooksItem;

    @FXML
    public MenuItem checkBorrowStatusItem;

    /**
     * Initializing methods.
     */
    @FXML
    public void initialize() {

    }

    /**
     * Handling account viewing button.
     */
    @Override
    public void handleAccountButton() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lma/objectum/fxml/AccountView.fxml"));
            Parent root = loader.load();
            Stage removeMemberStage = new Stage();
            removeMemberStage.setScene(new Scene(root, 842, 608));
            accountButton.getScene().getWindow().hide();
            removeMemberStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
}
