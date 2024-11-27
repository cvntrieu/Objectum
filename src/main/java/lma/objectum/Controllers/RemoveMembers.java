package lma.objectum.Controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import java.util.ArrayList;
import java.util.List;

public class RemoveMembers {

    @FXML
    protected ImageView avataImage;

    @FXML
    protected Button avataButton;

    @FXML
    protected Label guideLabel;

    @FXML
    protected Button backButton;

    @FXML
    protected Label deleteMemberMessageLabel;

    @FXML
    protected Button deleteButton;

    @FXML
    protected TextField deleteTextField;

    /**
     * Initializing method.
     */
    @FXML
    public void initialize() {
    }

    /**
     * Deleting a member.
     */
    public void deleteButtonOnAction() throws SQLException {

        String username = deleteTextField.getText();
        if (!username.isBlank()) {

            // Confirmation dialog
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirm Deletion");
            confirmDialog.setHeaderText("Are you sure you want to delete this member?");
            confirmDialog.setContentText("Username: " + username);

            if (confirmDialog.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                deleteProcess(username);
            }

        } else {

            deleteMemberMessageLabel.setText("Blank!");
            deleteMemberMessageLabel.getStyleClass().clear();
            deleteMemberMessageLabel.getStyleClass().add("warning-label");
            setTimeline();
        }
    }

    /**
     * Logical process for deleting a member.
     *
     */
    private void deleteProcess(String username) {

        String checkUserQuery = "SELECT * FROM useraccount WHERE username = ?";
        String deleteUserQuery = "DELETE FROM useraccount WHERE username = ?";
        String selectBookIdsQuery = "SELECT book_id FROM transactions WHERE user_id = ?";
        String deleteUserTransactionInfoQuery = "DELETE FROM transactions WHERE user_id = ?";
        String updateBookQuantityQuery = "UPDATE books SET Quantity = Quantity + 1 WHERE id = ?";

        try (Connection connectDB = DatabaseConnection.getInstance().getConnection()) {
            // Bắt đầu transaction
            connectDB.setAutoCommit(false);

            try (
                    PreparedStatement checkUserStatement = connectDB.prepareStatement(checkUserQuery);
            ) {
                checkUserStatement.setString(1, username);
                ResultSet userResult = checkUserStatement.executeQuery();

                if (userResult.next()) {
                    String role = userResult.getString("role");
                    int userId = userResult.getInt("account_id");

                    if ("admin".equalsIgnoreCase(role)) {
                        deleteMemberMessageLabel.setText("Cannot delete an admin!");
                        deleteMemberMessageLabel.getStyleClass().clear();
                        deleteMemberMessageLabel.getStyleClass().add("warning-label");
                        setTimeline();
                        return;
                    }

                    // Lấy danh sách book_id từ bảng transactions
                    List<Integer> bookIds = new ArrayList<>();
                    try (PreparedStatement selectBookIdsStatement = connectDB
                            .prepareStatement(selectBookIdsQuery)) {

                        selectBookIdsStatement.setInt(1, userId);
                        ResultSet bookIdResults = selectBookIdsStatement.executeQuery();

                        while (bookIdResults.next()) {
                            bookIds.add(bookIdResults.getInt("book_id"));
                        }
                    }

                    // Xóa các giao dịch liên quan đến user_id
                    try (PreparedStatement deleteTransactionStatement = connectDB
                            .prepareStatement(deleteUserTransactionInfoQuery)) {

                        deleteTransactionStatement.setInt(1, userId);
                        deleteTransactionStatement.executeUpdate();
                    }

                    // Cập nhật số lượng sách cho mỗi book_id
                    try (PreparedStatement updateBookQuantityStatement = connectDB
                            .prepareStatement(updateBookQuantityQuery)) {

                        for (Integer bookId : bookIds) {
                            updateBookQuantityStatement.setInt(1, bookId);
                            updateBookQuantityStatement.executeUpdate();
                        }
                    }

                    // Xóa user khỏi bảng useraccount
                    try (PreparedStatement deleteUserStatement = connectDB.prepareStatement(deleteUserQuery)) {
                        deleteUserStatement.setString(1, username);
                        int rowsAffected = deleteUserStatement.executeUpdate();

                        if (rowsAffected > 0) {
                            deleteMemberMessageLabel.setText("Member deleted successfully!");
                            deleteMemberMessageLabel.getStyleClass().clear();
                            deleteMemberMessageLabel.getStyleClass().add("success-label");
                            setTimeline();
                        } else {
                            throw new SQLException("Failed to delete user record.");
                        }
                    }

                    // Commit nếu tất cả thành công
                    connectDB.commit();

                } else {
                    deleteMemberMessageLabel.setText("Username not found!");
                    deleteMemberMessageLabel.getStyleClass().clear();
                    deleteMemberMessageLabel.getStyleClass().add("warning-label");
                    setTimeline();
                }

            } catch (SQLException e) {
                // Rollback nếu xảy ra lỗi
                connectDB.rollback();
                throw e;
            } finally {
                // Khôi phục chế độ autocommit
                connectDB.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            deleteMemberMessageLabel.setText("An error occurred while trying to delete the member: " + e.getMessage());
            deleteMemberMessageLabel.getStyleClass().clear();
            deleteMemberMessageLabel.getStyleClass().add("warning-label");
            setTimeline();
        }
    }

    /**
     * Back Button on Action.
     *
     * @param event event
     */
    public void redirectToHome(ActionEvent event) {

        try {
            Stage homeStage = StageUtils.loadFXMLStage(
                    "/lma/objectum/fxml/AdminHome.fxml",
                    "Admin Home"
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
            deleteMemberMessageLabel.setText("");
            deleteMemberMessageLabel.getStyleClass().clear();
        }));
        timeline.setCycleCount(1); // Run only once
        timeline.play();
    }
}
