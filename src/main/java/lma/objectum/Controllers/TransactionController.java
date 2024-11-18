package lma.objectum.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lma.objectum.Database.DatabaseConnection;
import lma.objectum.Utils.BasicFine;
import lma.objectum.Utils.FineStrategy;

import java.io.IOException;
import java.sql.*;

public class TransactionController {

    @FXML
    private TextField ISBN13_borrow;

    @FXML
    private TextField ISBN13_return;

    @FXML
    private Button homeButton;

    @FXML
    private Button borrowButton;

    @FXML
    private Button returnButton;

    /**
     * Borrow a book for a user.
     */
    @FXML
    public void borrowBook() {
        try {
            if (ISBN13_borrow.getText().isEmpty()) {
                showAlert("Error", "Please enter a valid ISBN13.");
                return;
            }

            int userId = SessionManager.getInstance().getCurrentUserId();
            long isbn13;
            try {
                isbn13 = Long.parseLong(ISBN13_borrow.getText());
            } catch (NumberFormatException e) {
                showAlert("Error", "Invalid ISBN13 format.");
                return;
            }

            int bookId = getBookIdByIsbn13(isbn13);

            if (!checkBookAvailability(isbn13)) {
                showAlert("Error", "This book is currently out of stock.");
                return;
            }
            if (hasBorrowedBook(userId, bookId)) {
                showAlert("Error", "You have already borrowed this book.");
                return;
            }

            createTransaction(userId, bookId);
            updateBookQuantity(isbn13, -1);
            showAlert("Success", "Book borrowed successfully!");

        } catch (SQLException e) {
            showAlert("Error", "Database error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showAlert("Error", "An unexpected error occurred.");
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
                showAlert("Error", "Please enter a valid ISBN13.");
                return;
            }

            long isbn13;
            try {
                isbn13 = Long.parseLong(ISBN13_return.getText());
            } catch (NumberFormatException e) {
                showAlert("Error", "Invalid ISBN13 format.");
                return;
            }

            int userId = SessionManager.getInstance().getCurrentUserId();
            int transactionId = getTransactionId(isbn13, userId);

            if (transactionId == -1) {
                showAlert("Error", "No active transaction found for this book.");
                return;
            }

            Date dueDate = getDueDate(transactionId);

            FineStrategy fineStrategy = new BasicFine();
            Date returnDate = new Date(System.currentTimeMillis());
            double fine = fineStrategy.calculateFine(dueDate, returnDate);

            updateTransactionStatus(transactionId, fine);
            updateBookQuantity(isbn13, 1);

            if (fine > 0) {
                showAlert("Success", "Book returned successfully! Late return fine: $" + fine);
            } else {
                showAlert("Success", "Book returned successfully!");
            }
        } catch (SQLException e) {
            showAlert("Error", "Database error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showAlert("Error", "An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Prefill the ISBN13 field with a given value.
     */
    public void prefillData(long isbn13) {
        ISBN13_borrow.setText(String.valueOf(isbn13));
    }

    @FXML
    public void handleHomeButton() {
        try {
            boolean isAdmin = checkIfAdmin();

            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    isAdmin ? "/lma/objectum/fxml/AdminHome.fxml" : "/lma/objectum/fxml/Home.fxml"
            ));
            Parent root = loader.load();
            Stage stage = (Stage) homeButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean checkIfAdmin() throws SQLException {
        DatabaseConnection connectNow = DatabaseConnection.getInstance();
        Connection connectDB = connectNow.getConnection();
        String username = SessionManager.getInstance().getCurrentUsername();
        String query = "SELECT role FROM useraccount WHERE username = ?";

        try {
            PreparedStatement preparedStatement = connectDB.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet queryResult = preparedStatement.executeQuery();

            if (queryResult.next()) {
                String role = queryResult.getString("role");
                return "admin".equalsIgnoreCase(role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
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
        String query = "INSERT INTO transactions (user_id, book_id, borrow_date, due_date, status) VALUES (?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 1 DAY), 'BORROWED')";

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
    private int getTransactionId(long isbn13, int userId) throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String query = "SELECT t.id AS transactionId FROM transactions t JOIN books b ON t.book_id = b.id WHERE b.ISBN13 = ? AND t.user_id = ? AND t.status = 'BORROWED'";

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
    private Date getDueDate(int transactionId) throws SQLException {
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
     * Show an alert dialog with a given title and message.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
