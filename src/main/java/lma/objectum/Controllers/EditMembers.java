
package lma.objectum.Controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import lma.objectum.Database.DatabaseConnection;
import lma.objectum.Utils.StageUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditMembers {

    @FXML
    protected ImageView avataImage;

    @FXML
    protected Button avataButton;

    @FXML
    protected Label guideLabel;

    @FXML
    protected TextField editTextField;

    @FXML
    protected Label editMessageLabel;

    @FXML
    protected Button toAdminButton;

    @FXML
    protected Button toMemberButton;

    @FXML
    protected Button backButton;

    /**
     * Initializing method.
     */
    @FXML
    public void initialize() {
    }

    /**
     * Member to Admin.
     */
    public void toAdminButtonOnAction() throws SQLException {

        String username = editTextField.getText();
        if (!username.isBlank()) {

            DatabaseConnection connectNow = DatabaseConnection.getInstance();
            Connection connectDB = connectNow.getConnection();
            String query = "SELECT role FROM useraccount WHERE username = ?";
            String updateQuery = "UPDATE useraccount SET role = 'admin' WHERE username = ?";

            try {

                PreparedStatement preparedStatement = connectDB.prepareStatement(query);
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String role = resultSet.getString("role");
                    if ("admin".equalsIgnoreCase(role)) {

                        editMessageLabel.setText("This account has been an admin!");
                        editMessageLabel.getStyleClass().clear();
                        editMessageLabel.getStyleClass().add("warning-label");
                        setTimeline();

                    } else if ("member".equalsIgnoreCase(role)) {

                        PreparedStatement updateStatement = connectDB.prepareStatement(updateQuery);
                        updateStatement.setString(1, username);
                        updateStatement.executeUpdate();

                        editMessageLabel.setText("Role updated to Admin successfully!");
                        editMessageLabel.getStyleClass().clear();
                        editMessageLabel.getStyleClass().add("success-label");
                        setTimeline();
                    }

                } else {

                    editMessageLabel.setText("Username not found!");
                    editMessageLabel.getStyleClass().clear();
                    editMessageLabel.getStyleClass().add("warning-label");
                    setTimeline();
                }
            } catch (SQLException e) {

                e.printStackTrace();
                editMessageLabel.setText("Database error!");
                editMessageLabel.getStyleClass().clear();
                editMessageLabel.getStyleClass().add("error-label");
                setTimeline();
            }

        } else {

            editMessageLabel.setText("Blank!");
            editMessageLabel.getStyleClass().clear();
            editMessageLabel.getStyleClass().add("warning-label");
            setTimeline();
        }
    }

    /**
     * Admin to Member.
     */
    public void toMemberButtonOnAction() throws SQLException {

        String username = editTextField.getText();
        if (!username.isBlank()) {

            DatabaseConnection connectNow = DatabaseConnection.getInstance();
            Connection connectDB = connectNow.getConnection();
            String query = "SELECT role FROM useraccount WHERE username = ?";
            String updateQuery = "UPDATE useraccount SET role = 'member' WHERE username = ?";

            try {
                PreparedStatement preparedStatement = connectDB.prepareStatement(query);
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String role = resultSet.getString("role");

                    if ("member".equalsIgnoreCase(role)) {

                        editMessageLabel.setText("This account has been a member!");
                        editMessageLabel.getStyleClass().clear();
                        editMessageLabel.getStyleClass().add("warning-label");
                        setTimeline();
                        // Ở đây có nên thêm return; hay ko?
                    } else if ("admin".equalsIgnoreCase(role)) {

                        PreparedStatement updateStatement = connectDB.prepareStatement(updateQuery);
                        updateStatement.setString(1, username);
                        updateStatement.executeUpdate();

                        editMessageLabel.setText("Role updated to Member successfully!");
                        editMessageLabel.getStyleClass().clear();
                        editMessageLabel.getStyleClass().add("success-label");
                        setTimeline();
                    }
                } else {
                    editMessageLabel.setText("Username not found!");
                    editMessageLabel.getStyleClass().clear();
                    editMessageLabel.getStyleClass().add("warning-label");
                    setTimeline();
                }

            } catch (SQLException e) {

                e.printStackTrace();
                editMessageLabel.setText("Database error!");
                editMessageLabel.getStyleClass().clear();
                editMessageLabel.getStyleClass().add("error-label");
                setTimeline();
            }

        } else {
            editMessageLabel.setText("Blank!");
            editMessageLabel.getStyleClass().clear();
            editMessageLabel.getStyleClass().add("warning-label");
            setTimeline();
        }
    }

    /**
     * Back Button on action.
     *
     * @param event event
     */
    public void redirectToHome(ActionEvent event) {

        try {
            Stage homeStage = StageUtils.loadFXMLStage(
                    "/lma/objectum/fxml/AdminHome.fxml",
                    "Objectum Library"
            );
            homeStage.show();

            Stage editStage = (Stage) backButton.getScene().getWindow();
            editStage.close();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    /**
     * Create a timeline to clear the message after 10 seconds.
     */
    private void setTimeline() {

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
            editMessageLabel.setText("");
            editMessageLabel.getStyleClass().clear();
        }));
        timeline.setCycleCount(1); // Run only once
        timeline.play();
    }
}

