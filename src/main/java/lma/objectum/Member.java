
package lma.objectum;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lma.objectum.Controllers.User;
import lma.objectum.Utils.MusicPlayer;
import lma.objectum.Utils.StageUtils;

import java.io.IOException;

public class Member extends User {

    @FXML
    private Button accountButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button logoutButton;

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
    private Button APIButton;

    @FXML
    private Button MusicButton;

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
    @FXML
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
    @FXML
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
     * API Button on action.
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
     * Handling logout Button.
     */
    @FXML
    public void handleLogOutButton() {
        try {
            Stage loginStage = StageUtils.loadFXMLStage(
                    "/lma/objectum/fxml/App.fxml",
                    "Main Application"
            );
            accountButton.getScene().getWindow().hide();
            MusicPlayer.stopMusic();
            loginStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handling music button.
     */
    public void handleMusicButtonAction() {
        try {
            Stage musicStage = StageUtils.loadFXMLStage(
                    "/lma/objectum/fxml/Music.fxml",
                    "API View"
            );
            accountButton.getScene().getWindow().hide();
            MusicPlayer.stopMusic();
            musicStage.show();

        }
        catch (IOException e) {
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

    /**
     * Handling game button.
     */
    public void handleGameButton() {
        try {
            Stage settingStage = StageUtils.loadFXMLStage(
                    "/lma/objectum/fxml/Game.fxml",
                    "Game"
            );
            accountButton.getScene().getWindow().hide();
            MusicPlayer.stopMusic();
            settingStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
