package lma.objectum.Controllers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lma.objectum.Database.DatabaseConnection;
import lma.objectum.Models.Book;
import lma.objectum.Utils.BasicFine;
import lma.objectum.Utils.FineStrategy;
import lma.objectum.Utils.StageUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class TransactionController {

    @FXML
    private TextField ISBN13_borrow;

    @FXML
    private TextField ISBN13_return;

    @FXML
    private Button homeButton;

    @FXML
    protected Button borrowButton;

    @FXML
    private Button returnButton;

    @FXML
    protected ImageView qrImageView;

    public TextField getISBN13_borrow() {
        return ISBN13_borrow;
    }

    public void setISBN13_borrow(TextField ISBN13_borrow) {
        this.ISBN13_borrow = ISBN13_borrow;
    }

    public TextField getISBN13_return() {
        return ISBN13_return;
    }

    public void setISBN13_return(TextField ISBN13_return) {
        this.ISBN13_return = ISBN13_return;
    }

    public Button getHomeButton() {
        return homeButton;
    }

    public void setHomeButton(Button homeButton) {
        this.homeButton = homeButton;
    }

    public Button getBorrowButton() {
        return borrowButton;
    }

    public void setBorrowButton(Button borrowButton) {
        this.borrowButton = borrowButton;
    }

    public Button getReturnButton() {
        return returnButton;
    }

    public void setReturnButton(Button returnButton) {
        this.returnButton = returnButton;
    }

    public ImageView getQrImageView() {
        return qrImageView;
    }

    public void setQrImageView(ImageView qrImageView) {
        this.qrImageView = qrImageView;
    }

    /**
     * Borrow a book for a user.
     */
    @FXML
    public void borrowBook() {
        try {
            if (ISBN13_borrow.getText().isEmpty()) {
                showCustomAlert("Error", "Please enter a valid ISBN13.", false);
                return;
            }

            int userId = SessionManager.getInstance().getCurrentUserId();
            long isbn13;
            try {
                isbn13 = Long.parseLong(ISBN13_borrow.getText());
            } catch (NumberFormatException e) {
                showCustomAlert("Error", "Invalid ISBN13 format.", false);
                return;
            }

            int bookId = getBookIdByIsbn13(isbn13);

            if (!checkBookAvailability(isbn13)) {
                showCustomAlert("Error", "This book is currently out of stock.", false);
                return;
            }
            if (hasBorrowedBook(userId, bookId)) {
                showCustomAlert("Error", "You have already borrowed this book.", false);
                return;
            }

            createTransaction(userId, bookId);
            updateBookQuantity(isbn13, -1);
            showCustomAlert("Success", "Book borrowed successfully!", true);

        } catch (SQLException e) {
            showCustomAlert("Error",  e.getMessage(), false);
            e.printStackTrace();
        } catch (Exception e) {
            showCustomAlert("Error", "An unexpected error occurred.", false);
            e.printStackTrace();
        }
    }

    /**
     * Return a borrowed book by transaction ID.
     */
    @FXML
    public void returnBook() {
        try {
            if (ISBN13_return.getText().isEmpty()) {
                showCustomAlert("Error", "Please enter a valid ISBN13.", false);
                return;
            }

            long isbn13;
            try {
                isbn13 = Long.parseLong(ISBN13_return.getText());
            } catch (NumberFormatException e) {
                showCustomAlert("Error", "Invalid ISBN13 format.", false);
                return;
            }

            int userId = SessionManager.getInstance().getCurrentUserId();
            int transactionId = getTransactionId(isbn13, userId);

            if (transactionId == -1) {
                showCustomAlert("Error", "No active transaction found for this book.", false);
                return;
            }

            Date dueDate = getDueDate(transactionId);

            FineStrategy fineStrategy = new BasicFine();
            Date returnDate = new Date(System.currentTimeMillis());
            double fine = fineStrategy.calculateFine(dueDate, returnDate);

            updateTransactionStatus(transactionId, fine);
            updateBookQuantity(isbn13, 1);

            if (fine > 0) {
                showCustomAlert("Success", "Book returned successfully! Late return fine: $" + fine, true);
            } else {
                showCustomAlert("Success", "Book returned successfully!", true);
            }
        } catch (SQLException e) {
            showCustomAlert("Error", e.getMessage(), false);
            e.printStackTrace();
        } catch (Exception e) {
            showCustomAlert("Error", "An unexpected error occurred: " + e.getMessage(), false);
            e.printStackTrace();
        }
    }

    /**
     * Generate a QR code for a book.
     */
    public void scanQRCode(Book book) {
        try {
            if (book == null) {
                System.out.println("Book object is null. Please provide a valid book.");
                return;
            }

            String bookInfo = String.format(
                    "ISBN: %s\nISBN13: %s\nTitle: %s\nAuthor: %s\nRating: %.1f\nDate: %s\nPublisher: %s",
                    Objects.requireNonNullElse(book.getIsbn(), "N/A"),
                    Objects.requireNonNullElse(book.getIsbn_13(), "N/A"),
                    Objects.requireNonNullElse(book.getTitle(), "N/A"),
                    Objects.requireNonNullElse(book.getAuthors(), "N/A"),
                    book.getRating(),
                    Objects.requireNonNullElse(book.getDate(), "N/A"),
                    Objects.requireNonNullElse(book.getPublisher(), "N/A")
            );

            if (bookInfo.trim().isEmpty()) {
                System.out.println("Book information is empty. Cannot generate QR code.");
                return;
            }

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(bookInfo, BarcodeFormat.QR_CODE, 400, 400);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            if (qrImageView != null) {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                Image qrImage = new Image(inputStream);
                qrImageView.setImage(qrImage);
                qrImageView.setVisible(true);
            } else {
                System.out.println("ImageView is not initialized.");
            }

        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prefill the ISBN13 field with a given value.
     */
    public void prefillData(long isbn13) {
        ISBN13_borrow.setText(String.valueOf(isbn13));
    }

    /**
     * Handle the home button click event.
     */
    @FXML
    public void handleHomeButton() {
        try {
            Stage homeStage = StageUtils.loadRoleBasedStage(
                    "/lma/objectum/fxml/AdminHome.fxml",
                    "/lma/objectum/fxml/Home.fxml",
                    "Home"
            );
            homeStage.show();
            Stage currentStage = (Stage) homeButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if a book is available to borrow.
     */
    private boolean checkBookAvailability(long isbn13) throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String query = "SELECT Quantity FROM books WHERE ISBN13 = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, isbn13);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("Quantity") > 0;
            }
        }

        return false;
    }

    /**
     * Check if a user has already borrowed a book.
     */
    private boolean hasBorrowedBook(int userId, int bookId) throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String query = "SELECT * FROM transactions WHERE user_id = ? AND book_id = ? AND status = 'BORROWED'";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, bookId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        }
    }

    /**
     * Create a new transaction for borrowing a book with a due date.
     */
    private void createTransaction(int userId, int bookId) throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String query = "INSERT INTO transactions (user_id, book_id, borrow_date, due_date, status) " +
                "VALUES (?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 1 DAY), 'BORROWED')";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, bookId);
            statement.executeUpdate();
        }
    }

    /**
     * Update the quantity of a book in the inventory.
     */
    private void updateBookQuantity(long isbn13, int change) throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String query = "UPDATE books SET Quantity = Quantity + ? WHERE ISBN13 = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, change);
            statement.setLong(2, isbn13);
            statement.executeUpdate();
        }
    }

    /**
     * Update the status of a transaction to 'RETURNED'.
     */
    private void updateTransactionStatus(int transactionId, double fine) throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String query = "UPDATE transactions SET status = 'RETURNED', return_date = CURDATE(), fine = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, fine);
            statement.setInt(2, transactionId);
            statement.executeUpdate();
        }
    }

    /**
     * Get the book ID associated with a transaction.
     */
    int getTransactionId(long isbn13, int userId) throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String query = "SELECT t.id AS transactionId FROM transactions t " +
                "JOIN books b ON t.book_id = b.id " +
                "WHERE b.ISBN13 = ? AND t.user_id = ? AND t.status = 'BORROWED'";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, isbn13);
            statement.setInt(2, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("transactionId");
            }
        }
        return -1;
    }

    /**
     * Get the book ID associated with an ISBN13.
     */
    private int getBookIdByIsbn13(long isbn13) throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String query = "SELECT id FROM books WHERE ISBN13 = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, isbn13);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        }
        throw new SQLException("Book not found.");
    }

    /**
     * Get the due date of a transaction.
     */
    Date getDueDate(int transactionId) throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String query = "SELECT due_date FROM transactions WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, transactionId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDate("due_date");
            }
        }

        throw new SQLException("Due date not found for transaction ID: " + transactionId);
    }

    /**
     * Show custom alert.
     *
     * @param title title
     * @param message message
     * @param isSuccess is success
     */
    void showCustomAlert(String title, String message, boolean isSuccess) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.initStyle(StageStyle.UNDECORATED);

        ImageView icon = new ImageView(
                new Image(getClass().getResourceAsStream(
                        isSuccess ? "/lma/objectum/images/success_icon.png" :
                                "/lma/objectum/images/error_icon.png")
                )
        );
        icon.setFitWidth(50);
        icon.setFitHeight(50);

        Text text = new Text(message);
        text.setStyle("-fx-font-size: 16px; -fx-fill: #555; -fx-text-alignment: center;");

        VBox content = new VBox(icon, text);
        content.setSpacing(20);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-alignment: center;");

        dialog.getDialogPane().setContent(content);

        dialog.getDialogPane().getStylesheets()
                .add(getClass().getResource("/lma/objectum/css/DiaglogStyle.css")
                        .toExternalForm());

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }
}
