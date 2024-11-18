
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

    @FXML
    private TableView<BorrowedBook> borrowedBooksTable;

    @FXML
    private TableColumn<BorrowedBook, String> titleColumn;

    @FXML
    private TableColumn<BorrowedBook, String> authorColumn;

    @FXML
    private TableColumn<BorrowedBook, String> dueDateColumn;

    private ObservableList<BorrowedBook> borrowedBooks = FXCollections.observableArrayList();

    @FXML
    private TableView<ReadBook> readBooksTable;

    @FXML
    private TableColumn<ReadBook, String> readTitleColumn;

    @FXML
    private TableColumn<ReadBook, String> readAuthorColumn;

    private ObservableList<ReadBook> readBooks = FXCollections.observableArrayList();

    @FXML
    private TableView<FinedBook> fineTable;

    @FXML
    private TableColumn<FinedBook, String> fineTitleColumn;

    @FXML
    private TableColumn<FinedBook, Double> fineAmountColumn;

    @FXML
    private Label totalFineLabel;

    private ObservableList<FinedBook> fines = FXCollections.observableArrayList();

    /**
     * Initializing methods.
     */
    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        readTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        readAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        fineTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        fineAmountColumn.setCellValueFactory(new PropertyValueFactory<>("fine"));

        // Load borrowed books data
        loadBorrowedBooks();
        loadReadBooks();
        loadFines();
    }

    /**
     * Handling load borrowed books.
     */
    private void loadBorrowedBooks() {
        int userId = SessionManager.getInstance().getCurrentUserId();
        String query = "SELECT b.title, b.author, t.due_date FROM transactions t JOIN books b ON t.book_id = b.id WHERE t.user_id = ? AND t.status = 'BORROWED' ORDER BY t.due_date";

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
    private void loadReadBooks() {
        int userId = SessionManager.getInstance().getCurrentUserId();
        String query = "SELECT b.title, b.author FROM transactions t JOIN books b ON t.book_id = b.id WHERE t.user_id = ? AND t.status = 'RETURNED' ORDER BY t.return_date";

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
    private void loadFines() {
        int userId = SessionManager.getInstance().getCurrentUserId();
        String query = "SELECT b.title, t.fine FROM transactions t JOIN books b ON t.book_id = b.id WHERE t.user_id = ? AND t.fine > 0 ORDER BY t.fine DESC";

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

    public void handleReturnBooksItem() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lma/objectum/fxml/Transaction.fxml"));
            Parent root = loader.load();
            Stage removeMemberStage = new Stage();
            Scene scene = new Scene(root, 478.4, 600);
            scene.getStylesheets().add(getClass().getResource("/lma/objectum/css/TransactionStyle.css").toExternalForm());
            removeMemberStage.setScene(scene);
            accountButton.getScene().getWindow().hide();
            removeMemberStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void APIButtonOnAction () {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lma/objectum/fxml/API.fxml"));
            Parent root = loader.load();
            Stage removeMemberStage = new Stage();
            Scene scene = new Scene(root, 1132, 836);
            removeMemberStage.setScene(scene);
            accountButton.getScene().getWindow().hide();
            removeMemberStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
