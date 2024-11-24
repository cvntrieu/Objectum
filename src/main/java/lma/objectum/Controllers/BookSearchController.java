package lma.objectum.Controllers;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lma.objectum.Database.DatabaseConnection;
import lma.objectum.Models.Book;
import lma.objectum.Utils.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class BookSearchController implements Initializable {

    @FXML
    private TableView<Book> tableView;

    @FXML
    private TableColumn<Book, String> isbn, title, author, date, publisher, image;

    @FXML
    private TableColumn<Book, Long> isbn_13;

    @FXML
    private TableColumn<Book, Double> rating;

    @FXML
    private TextField keyWordTextField;

    @FXML
    private AnchorPane dynamicIsland;

    @FXML
    private Label titleLabel;

    @FXML
    private Label authorLabel;

    @FXML
    private Label ratingLabel;

    @FXML
    private Hyperlink buyLink;

    @FXML
    private ComboBox<String> searchCriteriaComboBox;

    @FXML
    private Button homeButton;

    private SearchContext searchContext = new SearchContext();

    ObservableList<Book> bookList = FXCollections.observableArrayList();

    /**
     * Initializing the necessary components for Book Searching.
     *
     * @param url url
     * @param resource resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resource) {
        loadBooksFromDatabase();

        searchCriteriaComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateSearchStrategy();
        });

        setupFilteringAndSorting();
        configureTableColumns();
    }

    public TableView<Book> getTableView() {
        return tableView;
    }

    public void setTableView(TableView<Book> tableView) {
        this.tableView = tableView;
    }

    public TableColumn<Book, String> getIsbn() {
        return isbn;
    }

    public void setIsbn(TableColumn<Book, String> isbn) {
        this.isbn = isbn;
    }

    public TableColumn<Book, String> getTitle() {
        return title;
    }

    public void setTitle(TableColumn<Book, String> title) {
        this.title = title;
    }

    public TableColumn<Book, String> getAuthor() {
        return author;
    }

    public void setAuthor(TableColumn<Book, String> author) {
        this.author = author;
    }

    public TableColumn<Book, String> getDate() {
        return date;
    }

    public void setDate(TableColumn<Book, String> date) {
        this.date = date;
    }

    public TableColumn<Book, String> getPublisher() {
        return publisher;
    }

    public void setPublisher(TableColumn<Book, String> publisher) {
        this.publisher = publisher;
    }

    public TableColumn<Book, String> getImage() {
        return image;
    }

    public void setImage(TableColumn<Book, String> image) {
        this.image = image;
    }

    public TableColumn<Book, Long> getIsbn_13() {
        return isbn_13;
    }

    public void setIsbn_13(TableColumn<Book, Long> isbn_13) {
        this.isbn_13 = isbn_13;
    }

    public TableColumn<Book, Double> getRating() {
        return rating;
    }

    public void setRating(TableColumn<Book, Double> rating) {
        this.rating = rating;
    }

    public TextField getKeyWordTextField() {
        return keyWordTextField;
    }

    public void setKeyWordTextField(TextField keyWordTextField) {
        this.keyWordTextField = keyWordTextField;
    }

    public AnchorPane getDynamicIsland() {
        return dynamicIsland;
    }

    public void setDynamicIsland(AnchorPane dynamicIsland) {
        this.dynamicIsland = dynamicIsland;
    }

    public Label getTitleLabel() {
        return titleLabel;
    }

    public void setTitleLabel(Label titleLabel) {
        this.titleLabel = titleLabel;
    }

    public Label getAuthorLabel() {
        return authorLabel;
    }

    public void setAuthorLabel(Label authorLabel) {
        this.authorLabel = authorLabel;
    }

    public Label getRatingLabel() {
        return ratingLabel;
    }

    public void setRatingLabel(Label ratingLabel) {
        this.ratingLabel = ratingLabel;
    }

    public Hyperlink getBuyLink() {
        return buyLink;
    }

    public void setBuyLink(Hyperlink buyLink) {
        this.buyLink = buyLink;
    }

    public ComboBox<String> getSearchCriteriaComboBox() {
        return searchCriteriaComboBox;
    }

    public void setSearchCriteriaComboBox(ComboBox<String> searchCriteriaComboBox) {
        this.searchCriteriaComboBox = searchCriteriaComboBox;
    }

    public ObservableList<Book> getBookList() {
        return bookList;
    }

    public void setBookList(ObservableList<Book> bookList) {
        this.bookList = bookList;
    }

    public SearchContext getSearchContext() {
        return searchContext;
    }

    public void setSearchContext(SearchContext searchContext) {
        this.searchContext = searchContext;
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
     * Update the search strategy based on the selected criteria.
     */
    void updateSearchStrategy() {
        String selectedCriteria = searchCriteriaComboBox.getSelectionModel().getSelectedItem();
        if (selectedCriteria != null) {
            switch (selectedCriteria) {
                case "Title":
                    searchContext.setStrategy(new TitleSearchStrategy());
                    break;
                case "Author":
                    searchContext.setStrategy(new AuthorSearchStrategy());
                    break;
                case "ISBN":
                    searchContext.setStrategy(new IsbnSearchStrategy());
                    break;
                case "Year":
                    searchContext.setStrategy(new DateSearchStrategy());
                    break;
            }
        }
    }

    /**
     * Load books from the database.
     */
    void loadBooksFromDatabase() {
        String query = "SELECT ISBN, ISBN13, Title, Author, Rating, PublicationYear, Publisher, ImageUrlS FROM books";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                bookList.add(new Book(
                        rs.getString("ISBN"),
                        rs.getLong("ISBN13"),
                        rs.getString("Title"),
                        rs.getString("Author"),
                        rs.getDouble("Rating"),
                        rs.getString("PublicationYear"),
                        rs.getString("Publisher"),
                        rs.getString("ImageUrlS")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Setup filtering and sorting for the table view.
     */
    private void setupFilteringAndSorting() {
        FilteredList<Book> filteredData = new FilteredList<>(bookList, b -> true);
        keyWordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(book -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                return searchContext.executeSearch(book, newValue);
            });
        });

        SortedList<Book> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);
    }

    /**
     * Set up the table click handler.
     */
    private void setupTableClickHandler() {
        tableView.setOnMouseClicked(event -> {
            Book selectedBook = tableView.getSelectionModel().getSelectedItem();
            if (selectedBook != null) {
                displayBookDetails(selectedBook);
            }
        });
    }

    /**
     * Display book details.
     *
     * @param selectedBook selected book
     */
    private void displayBookDetails(Book selectedBook) {
        titleLabel.setText("Title: " + selectedBook.getTitle());
        authorLabel.setText("Author: " + selectedBook.getAuthors());
        ratingLabel.setText("Rating: " + selectedBook.getRating());

        applyFadeTransition(dynamicIsland, 0, 1);
        dynamicIsland.setVisible(true);

        buyLink.setOnAction(e -> navigateToTransactionPage(selectedBook));
    }

    /**
     * Apply fade transition.
     *
     * @param pane      pane
     * @param fromValue from value
     * @param toValue   to value
     */
    private void applyFadeTransition(AnchorPane pane, double fromValue, double toValue) {
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), pane);
        fadeIn.setFromValue(fromValue);
        fadeIn.setToValue(toValue);
        fadeIn.play();
    }

    /**
     * Navigate to the transaction page.
     *
     * @param book book
     */
    private void navigateToTransactionPage(Book book) {
        try {
            Stage transactionStage = StageUtils.loadFXMLStageWithPrefillData(
                    "/lma/objectum/fxml/Transaction.fxml",
                    "/lma/objectum/css/TransactionStyle.css",
                    "Transaction",
                    book
            );
            transactionStage.show();

            Stage searchStage = (Stage) buyLink.getScene().getWindow();
            searchStage.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Setup image view effects.
     *
     * @param imageView image view
     */
    private void setupImageViewEffects(ImageView imageView) {
        imageView.setFitHeight(100);
        imageView.setFitWidth(80);
        imageView.setPreserveRatio(true);
        imageView.setOnMouseEntered(e -> applyScaleTransition(imageView, 1.2));
        imageView.setOnMouseExited(e -> applyScaleTransition(imageView, 1.0));
    }

    /**
     * Apply scale transition.
     *
     * @param imageView image view
     * @param scale     scale
     */
    private void applyScaleTransition(ImageView imageView, double scale) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.3), imageView);
        scaleTransition.setToX(scale);
        scaleTransition.setToY(scale);
        scaleTransition.play();
    }

    /**
     * Configure table columns.
     */
    private void configureTableColumns() {

        isbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        isbn_13.setCellValueFactory(new PropertyValueFactory<>("isbn_13"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        author.setCellValueFactory(new PropertyValueFactory<>("authors"));
        rating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        publisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        image.setCellValueFactory(new PropertyValueFactory<>("imageUrl"));

        setupTableClickHandler();
        setupImageColumnWithZoomEffect();
    }

    /**
     * Setup image column with zoom effect.
     */
    private void setupImageColumnWithZoomEffect() {

        image.setCellFactory(column -> new TableCell<>() {
            private final ImageView imageView = new ImageView();

            {
                setupImageViewEffects(imageView);
            }

            @Override
            protected void updateItem(String imageUrl, boolean empty) {
                super.updateItem(imageUrl, empty);
                if (empty || imageUrl == null) {
                    setGraphic(null);
                } else {
                    String finalImageUrl = imageUrl.startsWith("http://") ? imageUrl.replace("http://", "https://") : imageUrl;
                    Task<Image> imageTask = new Task<>() {
                        @Override
                        protected Image call() throws Exception {
                            try {
                                URI uri = new URI(finalImageUrl);
                                URL url = uri.toURL();
                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36");
                                connection.setRequestProperty("Referer", "https://www.amazon.com/");
                                connection.connect();

                                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                    InputStream inputStream = connection.getInputStream();
                                    return new Image(inputStream);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    };
                    imageTask.setOnSucceeded(event -> {
                        Image image = imageTask.getValue();
                        Platform.runLater(() -> {
                            if (image != null) {
                                imageView.setImage(image);
                                setGraphic(imageView);
                            } else {
                                setGraphic(null);
                            }
                        });
                    });
                    new Thread(imageTask).start();
                }
            }
        });
    }
}
