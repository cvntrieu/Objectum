
package lma.objectum.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lma.objectum.Database.DatabaseConnection;
import lma.objectum.Models.BorrowedBook;
import lma.objectum.Models.FinedBook;
import lma.objectum.Models.ReadBook;
import javafx.scene.chart.*;
import lma.objectum.Utils.BasicFine;
import lma.objectum.Utils.FineStrategy;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class User {

    @FXML
    protected TableView<BorrowedBook> borrowedBooksTable;

    @FXML
    protected TableColumn<BorrowedBook, String> titleColumn;

    @FXML
    protected TableColumn<BorrowedBook, String> authorColumn;

    @FXML
    protected TableColumn<BorrowedBook, String> dueDateColumn;

    protected ObservableList<BorrowedBook> borrowedBooks = FXCollections.observableArrayList();

    @FXML
    protected TableView<ReadBook> readBooksTable;

    @FXML
    protected TableColumn<ReadBook, String> readTitleColumn;

    @FXML
    protected TableColumn<ReadBook, String> readAuthorColumn;

    protected ObservableList<ReadBook> readBooks = FXCollections.observableArrayList();

    @FXML
    protected TableView<FinedBook> fineTable;

    @FXML
    protected TableColumn<FinedBook, String> fineTitleColumn;

    @FXML
    protected TableColumn<FinedBook, Double> fineAmountColumn;

    @FXML
    protected Label totalFineLabel;

    protected ObservableList<FinedBook> fines = FXCollections.observableArrayList();

    @FXML
    protected BarChart<String, Number> topBorrowedBooksChart;

    @FXML
    protected LineChart<String, Number> finesOverTimeChart;

    @FXML
    protected CategoryAxis xAxisBorrowed;

    @FXML
    protected NumberAxis yAxisBorrowed;

    @FXML
    protected CategoryAxis xAxisFines;

    @FXML
    protected NumberAxis yAxisFines;

    protected static final Logger logger = Logger.getLogger(User.class.getName());

    /**
     * Handling account button.
     */
    public abstract void handleAccountButton();

    /**
     * Initializing necessary components for a User.
     */
    @FXML
    public void initialize() {
        // Setup common columns
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        readTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        readAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        fineTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        fineAmountColumn.setCellValueFactory(new PropertyValueFactory<>("fine"));

        // Load data
        loadBorrowedBooks();
        loadReadBooks();
        loadFines();
        loadTopBorrowedBooks();
        loadFinesOverTime();
        checkOverDueBooks();
    }

    /**
     * Loading borrowed books.
     */
    protected void loadBorrowedBooks() {
        int userId = SessionManager.getInstance().getCurrentUserId();
        String query = "SELECT b.title, b.author, t.due_date " +
                "FROM transactions t " +
                "JOIN books b ON t.book_id = b.id " +
                "WHERE t.user_id = ? AND t.status = 'BORROWED' " +
                "ORDER BY t.due_date";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String dueDate = resultSet.getString("due_date");

                borrowedBooks.add(new BorrowedBook(title, author, dueDate));
            }

            borrowedBooksTable.setItems(borrowedBooks);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handling load read books.
     */
    protected void loadReadBooks() {
        int userId = SessionManager.getInstance().getCurrentUserId();
        String query = "SELECT DISTINCT b.title, b.author " +
                "FROM transactions t " +
                "JOIN books b ON t.book_id = b.id " +
                "WHERE t.user_id = ? AND t.status = 'RETURNED' " +
                "ORDER BY b.title";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");

                readBooks.add(new ReadBook(title, author));
            }

            readBooksTable.setItems(readBooks);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handling load fines.
     */
    protected void loadFines() {
        int userId = SessionManager.getInstance().getCurrentUserId();
        String query = "SELECT b.title, t.fine " +
                "FROM transactions t " +
                "JOIN books b ON t.book_id = b.id " +
                "WHERE t.user_id = ? AND t.fine > 0 " +
                "ORDER BY t.fine DESC";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            double totalFine = 0.0;

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                double fine = resultSet.getDouble("fine");

                fines.add(new FinedBook(title, fine));
                totalFine += fine;
            }

            fineTable.setItems(fines);
            totalFineLabel.setText("Total Fine: " + totalFine);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loading top borrowed books.
     */
    protected void loadTopBorrowedBooks() {
        String query = "SELECT b.title, COUNT(t.book_id) AS borrow_count " +
                "FROM transactions t " +
                "JOIN books b ON t.book_id = b.id " +
                "GROUP BY t.book_id " +
                "ORDER BY borrow_count DESC " +
                "LIMIT 3";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet resultSet = statement.executeQuery();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Top Borrowed Books");

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                int borrowCount = resultSet.getInt("borrow_count");
                series.getData().add(new XYChart.Data<>(title, borrowCount));
            }

            topBorrowedBooksChart.getData().clear();
            topBorrowedBooksChart.getData().add(series);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
<<<<<<< HEAD
     * Handling load fines over time.
=======
     * Loading fines overtime (miss the deadline / due date).
>>>>>>> Trieucvn
     */
    protected void loadFinesOverTime() {
        String query = "SELECT DATE(t.return_date) AS day, SUM(t.fine) AS total_fine " +
                "FROM transactions t " +
                "WHERE t.fine > 0 " +
                "GROUP BY DATE(t.return_date) " +
                "ORDER BY DATE(t.return_date) ASC";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet resultSet = statement.executeQuery();
            XYChart.Series<String, Number> fineSeries = new XYChart.Series<>();
            fineSeries.setName("Fines Collected Over Time");

            while (resultSet.next()) {
                String day = resultSet.getString("day");
                double totalFine = resultSet.getDouble("total_fine");
                fineSeries.getData().add(new XYChart.Data<>(day, totalFine));
            }

            finesOverTimeChart.getData().clear();
            finesOverTimeChart.getData().add(fineSeries);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checking overdue books and processing fines.
     */
    protected void checkOverDueBooks() {
        int userId = SessionManager.getInstance().getCurrentUserId();

        String transactionQuery = "SELECT book_id, due_date FROM transactions " +
                "WHERE DATEDIFF(CURDATE(), due_date) > 21 AND user_id = ? AND status = 'BORROWED'";
        String updateTransactionQuery = "UPDATE transactions " +
                "SET return_date = CURDATE(), fine = ?, status = 'RETURNED' " +
                "WHERE book_id = ? AND user_id = ?";
        String updateBookQuantityQuery = "UPDATE books SET Quantity = Quantity + 1 WHERE id = ?";

        try (Connection connectDB = DatabaseConnection.getInstance().getConnection()) {
            connectDB.setAutoCommit(false);

            try (
                    PreparedStatement transactionStatement = connectDB.prepareStatement(transactionQuery);
                    PreparedStatement updateTransactionStatement = connectDB.prepareStatement(updateTransactionQuery);
                    PreparedStatement updateBookQuantityStatement = connectDB.prepareStatement(updateBookQuantityQuery)
            ) {
                // Lấy danh sách book_id từ các giao dịch quá hạn
                transactionStatement.setInt(1, userId);
                try (ResultSet resultSet = transactionStatement.executeQuery()) {
                    boolean hasOverdue = false;

                    while (resultSet.next()) {
                        hasOverdue = true;

                        int bookId = resultSet.getInt("book_id");
                        Date dueDate = resultSet.getDate("due_date");

                        // Tính tiền phạt
                        FineStrategy fineStrategy = new BasicFine();
                        double fine = fineStrategy.calculateFine((Date) new java.util.Date(), dueDate);

                        // Cập nhật số lượng sách
                        updateBookQuantityStatement.setInt(1, bookId);
                        updateBookQuantityStatement.executeUpdate();

                        // Cập nhật thông tin giao dịch
                        updateTransactionStatement.setDouble(1, fine);
                        updateTransactionStatement.setInt(2, bookId);
                        updateTransactionStatement.setInt(3, userId);
                        updateTransactionStatement.executeUpdate();
                    }

                    if (!hasOverdue) {
                        logger.log(Level.INFO, "No overdue transactions for user ID {0}.", userId);
                    }
                }

                // Commit nếu không có lỗi
                connectDB.commit();
                logger.log(Level.INFO, "Overdue transactions processed successfully for user ID {0}.", userId);

            } catch (SQLException e) {
                // Rollback nếu có lỗi
                connectDB.rollback();
                logger.log(Level.SEVERE, "Error processing overdue transactions for user ID " + userId, e);
                throw e;
            } finally {
                // Khôi phục lại trạng thái autocommit
                connectDB.setAutoCommit(true);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "An error occurred while processing overdue transactions.", e);
        }
    }
}
