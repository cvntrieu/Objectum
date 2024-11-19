
package lma.objectum;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lma.objectum.Controllers.SessionManager;
import lma.objectum.Controllers.TransactionController;
import lma.objectum.Controllers.User;
import lma.objectum.Database.DatabaseConnection;
import lma.objectum.Models.*;
import lma.objectum.Utils.MusicPlayer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    @FXML
    public Button APIButton;

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

    /**
     * Handling borrow button.
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
     * Handling return button.
     */
    public void handleReturnBooksItem() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lma/objectum/fxml/Transaction.fxml"));
            Parent root = loader.load();
            Stage returnStage = new Stage();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/lma/objectum/css/TransactionStyle.css").toExternalForm());
            returnStage.setScene(scene);
            returnStage.show();

            Stage homeStage = (Stage) accountButton.getScene().getWindow();
            homeStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void APIButtonOnAction () {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lma/objectum/fxml/API.fxml"));
            Parent root = loader.load();
            Stage apiStage = new Stage();
            Scene scene = new Scene(root);
            apiStage.setScene(scene);
            apiStage.show();

            Stage homeStage = (Stage) accountButton.getScene().getWindow();
            homeStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleSettingButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lma/objectum/fxml/Setting.fxml"));
            Parent root = loader.load();
            Stage settingStage = new Stage();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/lma/objectum/css/SettingStyle.css").toExternalForm());
            settingStage.setScene(scene);
            settingStage.setTitle("Settings");
            settingStage.show();

            Stage homeStage = (Stage) accountButton.getScene().getWindow();
            homeStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
