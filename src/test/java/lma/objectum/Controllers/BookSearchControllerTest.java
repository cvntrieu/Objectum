import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import lma.objectum.Controllers.BookSearchController;
import lma.objectum.Database.DatabaseConnection;
import lma.objectum.Models.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookSearchControllerTest {

    private BookSearchController controller;
    private TableView<Book> tableView;
    private TextField keyWordTextField;
    private ComboBox<String> searchCriteriaComboBox;
    private Label titleLabel;
    private Label authorLabel;
    private Label ratingLabel;
    private AnchorPane dynamicIsland;
    private ObservableList<Book> bookList;

    @BeforeEach
    public void setUp() {
        // Khởi tạo đối tượng controller và các component UI mock
        controller = new BookSearchController();
        tableView = new TableView<>();
        keyWordTextField = new TextField();
        searchCriteriaComboBox = new ComboBox<>();
        titleLabel = new Label();
        authorLabel = new Label();
        ratingLabel = new Label();
        dynamicIsland = new AnchorPane();
        bookList = FXCollections.observableArrayList();

        // Gán các mock object cho controller
        controller.tableView = tableView;
        controller.keyWordTextField = keyWordTextField;
        controller.searchCriteriaComboBox = searchCriteriaComboBox;
        controller.titleLabel = titleLabel;
        controller.authorLabel = authorLabel;
        controller.ratingLabel = ratingLabel;
        controller.dynamicIsland = dynamicIsland;
        controller.bookList = bookList;
    }

    @Test
    public void testInitialize_shouldSetupComboBoxAndTableView() {
        // Thiết lập ResourceBundle và URL mock cho phương thức initialize
        ResourceBundle resourceBundle = mock(ResourceBundle.class);

        controller.initialize(null, resourceBundle);

        // Kiểm tra rằng ComboBox đã được thiết lập và không null
        assertNotNull(controller.searchCriteriaComboBox);
        assertNotNull(controller.tableView);

        // Giả lập việc chọn "Title" từ ComboBox
        searchCriteriaComboBox.getItems().addAll("Title", "Author", "ISBN", "Year");
        searchCriteriaComboBox.getSelectionModel().select("Title");

        assertEquals("Title", searchCriteriaComboBox.getSelectionModel().getSelectedItem());

        // Test việc thay đổi chiến lược tìm kiếm
        controller.searchCriteriaComboBox.getSelectionModel().select("Author");
        assertEquals("Author", controller.searchCriteriaComboBox.getSelectionModel().getSelectedItem());
    }

    @Test
    public void testLoadBooksFromDatabase_shouldLoadBooksSuccessfully() throws Exception {
        // Mock database connection
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, true, false); // 2 records
        when(resultSet.getString("ISBN")).thenReturn("12345");
        when(resultSet.getLong("ISBN13")).thenReturn(9781234567897L);
        when(resultSet.getString("Title")).thenReturn("Book Title");
        when(resultSet.getString("Author")).thenReturn("Book Author");
        when(resultSet.getDouble("Rating")).thenReturn(4.5);
        when(resultSet.getString("PublicationYear")).thenReturn("2020");
        when(resultSet.getString("Publisher")).thenReturn("Publisher Name");
        when(resultSet.getString("ImageUrlS")).thenReturn("https://example.com/image.jpg");

        // Mock the database connection instance
        DatabaseConnection mockDatabaseConnection = mock(DatabaseConnection.class);
        when(mockDatabaseConnection.getConnection()).thenReturn(connection);
        DatabaseConnection.setInstance(mockDatabaseConnection); // Use the mocked instance

        // Execute the loadBooksFromDatabase method
        controller.loadBooksFromDatabase();

        // Kiểm tra rằng 2 sách đã được load vào danh sách
        assertEquals(2, controller.bookList.size());
        assertEquals("Book Title", controller.bookList.get(0).getTitle());
    }

    @Test
    public void testSearchFunction_shouldFilterBooksCorrectly() {
        // Add mock books to the list
        Book book1 = new Book("12345", 9781234567897L, "Java Programming", "Author1", 4.5, "2020", "Publisher1", "");
        Book book2 = new Book("67890", 9789876543210L, "Python Cookbook", "Author2", 4.0, "2021", "Publisher2", "");
        controller.bookList.addAll(book1, book2);

        controller.initialize(null, null);

        // Test searching by Title
        searchCriteriaComboBox.getItems().addAll("Title", "Author", "ISBN", "Year");
        searchCriteriaComboBox.getSelectionModel().select("Title");
        keyWordTextField.setText("Java");

        assertEquals(1, controller.tableView.getItems().size());
        assertEquals("Java Programming", controller.tableView.getItems().get(0).getTitle());
    }

    @Test
    public void testImageLoading_shouldHandleImageUrlCorrectly() {
        // Set a sample book with an image URL
        Book book = new Book("12345", 9781234567897L, "Java Programming", "Author1", 4.5, "2020", "Publisher1", "https://example.com/image.jpg");
        controller.bookList.add(book);

        // Mock TableView selection
        tableView.getItems().add(book);
        tableView.getSelectionModel().select(book);

        controller.initialize(null, null);

        // Simulate a click event
        tableView.getSelectionModel().selectFirst();

        // Check if dynamicIsland is made visible and the labels are set
        assertTrue(dynamicIsland.isVisible());
        assertEquals("Title: Java Programming", titleLabel.getText());
        assertEquals("Author: Author1", authorLabel.getText());
    }
}
