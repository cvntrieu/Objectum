
package lma.objectum;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lma.objectum.Controllers.User;
import lma.objectum.Utils.StageUtils;

import java.io.IOException;

public class Member extends User {

    @FXML
    private Button accountButton;

    @FXML
    private Button homeButton;

    @FXML
    private MenuButton listButton;

    @FXML
    private Button settingButton;

    @FXML
    private Button avataButton;

    @FXML
    private ImageView avataImage;

    @FXML
    private MenuItem borrowBooksItem;

    @FXML
    private MenuItem returnBooksItem;

    @FXML
    private MenuItem checkBorrowStatusItem;

    @FXML
    private Button APIButton;

    /**
     * Handling account viewing button.
     */
    @Override
    public void handleAccountButton() {

        try {
            Stage accountStage = StageUtils.loadFXMLStage(
                    "/lma/objectum/fxml/AccountView.fxml",
                    "Account View"
            );
            accountButton.getScene().getWindow().hide();
            accountStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handling borrow button.
     */
    public void handleBorrowBooksItem() {

        try {
            Stage borrowBooksStage = StageUtils.loadFXMLStageWithCSS(
                    "/lma/objectum/fxml/BookSearch.fxml",
                    "/lma/objectum/css/BookSearchStyle.css",
                    "Borrow Books"
            );
            accountButton.getScene().getWindow().hide();
            borrowBooksStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handling return button.
     */
    public void handleReturnBooksItem() {

        try {
            Stage returnStage = StageUtils.loadFXMLStageWithCSS(
                    "/lma/objectum/fxml/Transaction.fxml",
                    "/lma/objectum/css/TransactionStyle.css",
                    "Return Books"
            );
            accountButton.getScene().getWindow().hide();
            returnStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handling api button.
     */
    @FXML
    public void handleAPIButtonAction() {
        try {
            Stage apiStage = StageUtils.loadFXMLStage(
                    "/lma/objectum/fxml/API.fxml",
                    "API View"
            );
            accountButton.getScene().getWindow().hide();
            apiStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handling setting button.
     */
    @FXML
    public void handleSettingButton() {
        try {
            Stage settingStage = StageUtils.loadFXMLStageWithCSS(
                    "/lma/objectum/fxml/Setting.fxml",
                    "/lma/objectum/css/SettingStyle.css",
                    "Settings"
            );
            accountButton.getScene().getWindow().hide();
            settingStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
