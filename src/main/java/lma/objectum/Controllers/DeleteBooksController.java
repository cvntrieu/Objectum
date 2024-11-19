
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
import javafx.util.Duration;
import lma.objectum.Database.DatabaseConnection;
import lma.objectum.Models.Book;
import lma.objectum.Utils.*;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class DeleteBooksController implements Initializable {

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
    private SearchContext searchContext = new SearchContext();

    ObservableList<Book> bookList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        loadBooksFromDatabase();

        searchCriteriaComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateSearchStrategy();
        });

        // Setup for the filtered and sorted list
        FilteredList<Book> filteredData = new FilteredList<>(bookList, b -> true);
        keyWordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(book -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true; // No filter applied
                }
                return searchContext.executeSearch(book, newValue);
            });
        });

        SortedList<Book> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);

        // Setup table columns
        isbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        isbn_13.setCellValueFactory(new PropertyValueFactory<>("isbn_13"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        author.setCellValueFactory(new PropertyValueFactory<>("authors"));
        rating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        publisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        image.setCellValueFactory(new PropertyValueFactory<>("imageUrl"));

        // Handle table row click event to show book details
        tableView.setOnMouseClicked(event -> {
            Book selectedBook = tableView.getSelectionModel().getSelectedItem();
            if (selectedBook != null) {
                titleLabel.setText("Title: " + selectedBook.getTitle());
                authorLabel.setText("Author: " + selectedBook.getAuthors());
                ratingLabel.setText("Rating: " + selectedBook.getRating());

                // Add fade-in effect to Dynamic Island
                FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), dynamicIsland);
                fadeIn.setFromValue(0);
                fadeIn.setToValue(1);
                fadeIn.play();
                dynamicIsland.setVisible(true);

                // Handle buy link click event to show book borrow dialog

//                buyLink.setOnAction(e -> {
//                    try {
//                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/BookBorrow.fxml"));
//                        AnchorPane borrowPane = loader.load();
//
//                        BookBorrowController borrowController = loader.getController();
//                        borrowController.setBookDetails(selectedBook.getTitle(), selectedBook.getAuthors());
//
//                        Stage borrowStage = new Stage();
//                        borrowStage.setTitle("Borrow Book");
//                        borrowStage.setScene(new Scene(borrowPane));
//                        borrowStage.show();
//                    } catch (IOException ex) {
//                        ex.printStackTrace();
//                    }
//                });
            }
        });

        // Set up custom cell factory for image column with zoom effect
        image.setCellFactory(column -> new TableCell<>() {
            private final ImageView imageView = new ImageView();
            {
                imageView.setFitHeight(100);
                imageView.setFitWidth(80);
                imageView.setPreserveRatio(true);
                // Apply scale transition for image zoom effect
                imageView.setOnMouseEntered(e -> {
                    ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.3), imageView);
                    scaleTransition.setToX(1.2);
                    scaleTransition.setToY(1.2);
                    scaleTransition.play();
                });
                imageView.setOnMouseExited(e -> {
                    ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.3), imageView);
                    scaleTransition.setToX(1);
                    scaleTransition.setToY(1);
                    scaleTransition.play();
                });
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

    public void updateSearchStrategy() {
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
}