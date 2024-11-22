
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

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Member extends User {

    @FXML
    private Button accountButton;

    @FXML
    protected Button homeButton;

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
     * Handling return books item.
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

    /**
     * API Button on action.
     */
    @FXML
    public void APIButtonOnAction () {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lma/objectum/fxml/API.fxml"));
            Parent root = loader.load();
            Stage apiStage = new Stage();
            Scene scene = new Scene(root);
            apiStage.setScene(scene);
            apiStage.show();

            Stage homeStage = (Stage) APIButton.getScene().getWindow();
            homeStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
