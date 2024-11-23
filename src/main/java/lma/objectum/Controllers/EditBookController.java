package lma.objectum.Controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import lma.objectum.Database.DatabaseConnection;
import lma.objectum.Utils.StageUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditBookController {

    @FXML
    protected ImageView avataImage;

    @FXML
    protected Button avataButton;

    @FXML
    protected Button backButton;

    @FXML
    protected TextField imageTextField;

    @FXML
    protected Button editButton;

    @FXML
    protected TextField titleTextField;

    @FXML
    protected TextField authorTextField;

    @FXML
    protected TextField publicationYearTextField;

    @FXML
    protected TextField publisherTextField;

    @FXML
    protected TextField quantityTextField;

    @FXML
    protected TextField ratingTextField;

    @FXML
    protected Label editBookMessageLabel;

    @FXML
    protected TextField isbn13TextField;

    @FXML
    public void initialize() {
    }

    /**
     * Editing book's information.
     */
    public void editButtonOnAction() {

        String isbn13 = isbn13TextField.getText().trim();
        if (isbn13.isEmpty()) {

            editBookMessageLabel.setText("ISBN13 cannot be empty!");
            editBookMessageLabel.getStyleClass().clear();
            editBookMessageLabel.getStyleClass().add("warning-label");
            setTimeline();
            return;
        }

        try {
            DatabaseConnection connectNow = DatabaseConnection.getInstance();
            Connection connectDB = connectNow.getConnection();

            String checkBookQuery = "SELECT * FROM books WHERE ISBN13 = ?";
            try (PreparedStatement checkStatement = connectDB.prepareStatement(checkBookQuery)) {

                checkStatement.setString(1, isbn13);
                ResultSet resultSet = checkStatement.executeQuery();

                if (!resultSet.next()) {
                    editBookMessageLabel.setText("Book with the given ISBN13 not found!");
                    editBookMessageLabel.getStyleClass().clear();
                    editBookMessageLabel.getStyleClass().add("warning-label");
                    setTimeline();
                    return;
                }

                // Lấy dữ liệu từ các trường để cập nhật
                String title = titleTextField.getText().trim();
                String author = authorTextField.getText().trim();
                String publicationYearStr = publicationYearTextField.getText().trim();
                String publisher = publisherTextField.getText().trim();
                String quantityStr = quantityTextField.getText().trim();
                String ratingStr = ratingTextField.getText().trim();
                String imageUrl = imageTextField.getText().trim();

                if (title.isEmpty() && author.isEmpty() && publicationYearStr.isEmpty() && publisher.isEmpty()
                        && quantityStr.isEmpty() && ratingStr.isEmpty() && imageUrl.isEmpty()) {
                    editBookMessageLabel.setText("Please fill in at least one field to update!");
                    editBookMessageLabel.getStyleClass().clear();
                    editBookMessageLabel.getStyleClass().add("warning-label");
                    setTimeline();
                    return;
                }

                int publicationYear = 0;
                int quantity = 0;
                double rating = 0.0;

                try {
                    if (!publicationYearStr.isEmpty()) {
                        publicationYear = Integer.parseInt(publicationYearStr);
                    }
                    if (!quantityStr.isEmpty()) {
                        quantity = Integer.parseInt(quantityStr);
                        if (quantity < 0) {
                            editBookMessageLabel.setText("Quantity must not be negative!");
                            editBookMessageLabel.getStyleClass().clear();
                            editBookMessageLabel.getStyleClass().add("warning-label");
                            setTimeline();
                            return;
                        }
                    }
                    if (!ratingStr.isEmpty()) {
                        rating = Double.parseDouble(ratingStr);
                        if (rating < 0 || rating > 5) {
                            editBookMessageLabel.setText("Rating must be in range [0, 5]!");
                            editBookMessageLabel.getStyleClass().clear();
                            editBookMessageLabel.getStyleClass().add("warning-label");
                            setTimeline();
                            return;
                        }
                    }
                } catch (NumberFormatException e) {

                    editBookMessageLabel.setText("Invalid input for Publication Year, Quantity, or Rating. Please enter numbers only.");
                    editBookMessageLabel.getStyleClass().clear();
                    editBookMessageLabel.getStyleClass().add("warning-label");
                    setTimeline();
                    return;
                }

                // Cập nhật thông tin sách
                String updateBookQuery = "UPDATE books SET Title = ?, Author = ?, PublicationYear = ?, Publisher = ?, Quantity = ?, Rating = ?, ImageUrlS = ? WHERE ISBN13 = ?";
                try (PreparedStatement updateStatement = connectDB.prepareStatement(updateBookQuery)) {
                    updateStatement.setString(1, title.isEmpty() ? resultSet.getString("Title") : title);
                    updateStatement.setString(2, author.isEmpty() ? resultSet.getString("Author") : author);
                    updateStatement.setInt(3, publicationYearStr.isEmpty() ? resultSet.getInt("PublicationYear") : publicationYear);
                    updateStatement.setString(4, publisher.isEmpty() ? resultSet.getString("Publisher") : publisher);
                    updateStatement.setInt(5, quantityStr.isEmpty() ? resultSet.getInt("Quantity") : quantity);
                    updateStatement.setDouble(6, ratingStr.isEmpty() ? resultSet.getDouble("Rating") : rating);
                    updateStatement.setString(7, imageUrl.isEmpty() ? resultSet.getString("ImageUrlS") : imageUrl);
                    updateStatement.setString(8, isbn13);

                    int rowsAffected = updateStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        editBookMessageLabel.setText("Book updated successfully!");
                        editBookMessageLabel.getStyleClass().clear();
                        editBookMessageLabel.getStyleClass().add("success-label");
                        setTimeline();
                        clearInputFields();
                    } else {
                        editBookMessageLabel.setText("Failed to update book. Please try again.");
                        editBookMessageLabel.getStyleClass().clear();
                        editBookMessageLabel.getStyleClass().add("warning-label");
                        setTimeline();
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    editBookMessageLabel.setText("An error occurred while trying to update the book.");
                    editBookMessageLabel.getStyleClass().clear();
                    editBookMessageLabel.getStyleClass().add("warning-label");
                    setTimeline();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            editBookMessageLabel.setText("An error occurred while connecting to the database.");
            editBookMessageLabel.getStyleClass().clear();
            editBookMessageLabel.getStyleClass().add("warning-label");
            setTimeline();
        }
    }

    /**
     * Clears all input fields after successfully editing a book.
     */
    private void clearInputFields() {
        isbn13TextField.clear();
        titleTextField.clear();
        authorTextField.clear();
        publicationYearTextField.clear();
        publisherTextField.clear();
        quantityTextField.clear();
        ratingTextField.clear();
        imageTextField.clear();
    }

    /**
     * Redirecting to homepage.
     *
     * @param event event
     */
    public void redirectToHome(ActionEvent event) { // BackButtonOnAction

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
            editBookMessageLabel.setText("");
            editBookMessageLabel.getStyleClass().clear();
        }));
        timeline.setCycleCount(1); // Run only once
        timeline.play();
    }
}
