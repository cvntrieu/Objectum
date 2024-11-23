
package lma.objectum;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lma.objectum.Controllers.SessionManager;
import lma.objectum.Controllers.TransactionController;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lma.objectum.Controllers.User;
import lma.objectum.Utils.MusicPlayer;
import lma.objectum.Utils.StageUtils;
import lma.objectum.Database.DatabaseConnection;
import lma.objectum.Models.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.IOException;

public class Member extends User {

    @FXML
    private Button accountButton;

    @FXML
    protected Button homeButton;

    @FXML
    protected Button logoutButton;

    @FXML
    protected MenuButton listButton;

    @FXML
    protected Button settingButton;

    @FXML
    protected Button avataButton;

    @FXML
    protected ImageView avataImage;

    @FXML
    protected MenuItem borrowBooksItem;

    @FXML
    protected MenuItem returnBooksItem;

    @FXML
    protected MenuItem checkBorrowStatusItem;

    @FXML
    protected Button APIButton;

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
     * Handling return books item.
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
