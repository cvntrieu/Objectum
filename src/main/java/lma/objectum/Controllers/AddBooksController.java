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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lma.objectum.Database.DatabaseConnection;
import lma.objectum.Utils.StageUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddBooksController {

    @FXML
    protected ImageView avataImage;

    @FXML
    protected Button avataButton;

    @FXML
    protected Label guideLabel;

    @FXML
    protected Button backButton;

    @FXML
    protected TextField imageTextField;

    @FXML
    protected Button addButton;

    @FXML
    protected TextField titleTextField;

    @FXML
    protected TextField isbnTextField;

    @FXML
    protected TextField isbn13TextField;

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
    protected Label addBookMessageLabel;

    /**
     * Initializing method.
     */
    @FXML
    public void initialize() {
    }

    /**
     * Add Book Button on Action.
     */
    public void addButtonOnAction() {
        // Lấy dữ liệu từ các TextField
        String title = titleTextField.getText().trim();
        String isbn = isbnTextField.getText().trim();
        String isbn13 = isbn13TextField.getText().trim();
        String author = authorTextField.getText().trim();
        String publicationYear = publicationYearTextField.getText().trim();
        String publisher = publisherTextField.getText().trim();
        String quantityStr = quantityTextField.getText().trim();
        String ratingStr = ratingTextField.getText().trim();
        String imageUrl = imageTextField.getText().trim(); // Không bắt buộc

        // Kiểm tra tính hợp lệ của dữ liệu đầu vào
        if (title.isEmpty() || isbn.isEmpty() || isbn13.isEmpty() || author.isEmpty() || publicationYear.isEmpty()
                || publisher.isEmpty() || quantityStr.isEmpty() || ratingStr.isEmpty()) {

            addBookMessageLabel.setText("Please fill in all required fields!");
            addBookMessageLabel.getStyleClass().clear();
            addBookMessageLabel.getStyleClass().add("warning-label");
            setTimeline();
            return;
        }

        int quantity = 0;
        double rating = 0.0;
        try {
            quantity = Integer.parseInt(quantityStr);
            rating = Double.parseDouble(ratingStr);

            if (quantity < 0) {
                addBookMessageLabel.setText("Quantity must not be negative!");
                addBookMessageLabel.getStyleClass().clear();
                addBookMessageLabel.getStyleClass().add("warning-label");
                setTimeline();
                return; // Phải có return, để khi sai thì dừng quá trình update
            }

            if (rating < 0 || rating > 5) {
                addBookMessageLabel.setText("Rating must be in range [0, 5]!");
                addBookMessageLabel.getStyleClass().clear();
                addBookMessageLabel.getStyleClass().add("warning-label");
                setTimeline();
                return;
            }

        } catch (NumberFormatException e) {
            addBookMessageLabel.setText("Invalid input for Quantity or Rating. Please enter numbers only.");
            addBookMessageLabel.getStyleClass().clear();
            addBookMessageLabel.getStyleClass().add("warning-label");
            setTimeline();
            return;
        }

        try {
            DatabaseConnection connectNow = DatabaseConnection.getInstance();
            Connection connectDB = connectNow.getConnection();

            String checkISBN13Query = "SELECT ISBN13 FROM books WHERE ISBN13 = ?";
            try (PreparedStatement checkStatement = connectDB.prepareStatement(checkISBN13Query)) {
                checkStatement.setString(1, isbn13);
                ResultSet resultSet = checkStatement.executeQuery();

                if (resultSet.next()) {
                    // Nếu ISBN13 đã tồn tại (ISBN13 ko phải PK nhưng xác định độc nhất mỗi sách) thì ko thêm đc!
                    addBookMessageLabel.setText("ISBN13 has existed in database!");
                    addBookMessageLabel.getStyleClass().clear();
                    addBookMessageLabel.getStyleClass().add("warning-label");
                    setTimeline();
                    return;
                }
            }

            String insertBookQuery = "INSERT INTO books (ISBN, ISBN13, Title, Author, PublicationYear, Publisher, Quantity, Rating, ImageUrlS) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connectDB.prepareStatement(insertBookQuery)) {
                preparedStatement.setString(1, isbn);
                preparedStatement.setString(2, isbn13);
                preparedStatement.setString(3, title);
                preparedStatement.setString(4, author);
                preparedStatement.setString(5, publicationYear);
                preparedStatement.setString(6, publisher);
                preparedStatement.setInt(7, quantity);
                preparedStatement.setDouble(8, rating);
                preparedStatement.setString(9, imageUrl.isEmpty() ? null : imageUrl);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    addBookMessageLabel.setText("Book added successfully!");
                    addBookMessageLabel.getStyleClass().clear();
                    addBookMessageLabel.getStyleClass().add("success-label");
                    setTimeline();
                    clearInputFields();
                } else {
                    addBookMessageLabel.setText("Failed to add book. Please try again.");
                    addBookMessageLabel.getStyleClass().clear();
                    addBookMessageLabel.getStyleClass().add("warning-label");
                    setTimeline();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                addBookMessageLabel.setText("An error occurred while trying to add the book.");
                addBookMessageLabel.getStyleClass().clear();
                addBookMessageLabel.getStyleClass().add("warning-label");
                setTimeline();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            addBookMessageLabel.setText("An error occurred while connecting to the database.");
            addBookMessageLabel.getStyleClass().clear();
            addBookMessageLabel.getStyleClass().add("warning-label");
            setTimeline();
        }
    }

    /**
     * Clears all input fields after successfully adding a book.
     */
    private void clearInputFields() {

        titleTextField.clear();
        isbnTextField.clear();
        isbn13TextField.clear();
        authorTextField.clear();
        publicationYearTextField.clear();
        publisherTextField.clear();
        quantityTextField.clear();
        ratingTextField.clear();
        imageTextField.clear();
    }

    /**
     * Back Button on Action.
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

            Stage searchStage = (Stage) backButton.getScene().getWindow();
            searchStage.close();

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
            addBookMessageLabel.setText("");
            addBookMessageLabel.getStyleClass().clear();
        }));
        timeline.setCycleCount(1);
        timeline.play();
    }
}
