package lma.objectum.Controllers;

import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lma.objectum.Models.BookInAPI;
import lma.objectum.Database.DatabaseConnection;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Optional;

/**
 * Controller for handling interactions with the Google Books API.
 * This class provides methods to search for books, display book suggestions, and update the user interface
 * with book details retrieved from the Google Books API or the local database.
 */
public class API {

    @FXML
    private TextField searchField;

    @FXML
    private ListView<HBox> suggestionsBox;

    @FXML
    private ListView<BookInAPI> listView;

    @FXML
    private VBox bookDetailBox;

    @FXML
    private Button searchBook; // This field is unused

    @FXML
    private TextArea bookDescriptionTextArea;

    private final String apiKey = "AIzaSyCUB7PSg_v5EqFrzdC13A644v30JhDNT9Q"; // Consider storing API keys more securely
    private final OkHttpClient client = new OkHttpClient();
    private final ExecutorService executor = Executors.newFixedThreadPool(5);
    private DatabaseConnection dbConnection;
    private static final Logger logger = Logger.getLogger(API.class.getName());

    /**
     * Constructor initializes the DatabaseConnection instance.
     * Checks if the database connection is available, and raises an alert if not.
     */
    public API() {
        try {
            DatabaseConnection tempDbConnection = null;
            this.dbConnection = DatabaseConnection.getInstance();
            if (dbConnection == null || dbConnection.getConnection().isClosed()) {
                logger.log(Level.SEVERE, "Database connection is not available or is closed.");
                throw new RuntimeException("Unable to perform operation because the database connection is not available or is closed.");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error while performing database operation", e);
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Database Error");
                alert.setHeaderText("Database connection error");
                alert.setContentText("Unable to perform the database operation. Please check the connection and try again.");
                alert.showAndWait();
            });
        }
    }

    /**
     * Searches for books using the Google Books API based on the provided query.
     *
     * @param query A search string with book titles or author names (e.g., "Harry Potter; author:J.K. Rowling").
     * @return A CompletableFuture that will contain a JSON string with the search results or an error message.
     */
    public CompletableFuture<String> searchBooks(String query) {
        StringBuilder urlBuilder = new StringBuilder("https://www.googleapis.com/books/v1/volumes?q=");
        String[] queryParts = query.split(";");
        for (String part : queryParts) {
            part = part.trim();
            if (part.toLowerCase().contains("author:")) {
                urlBuilder.append("+inauthor:").append(part.replace("author:", "").trim());
            } else {
                urlBuilder.append("+intitle:").append(part);
            }
        }

        urlBuilder.append("&key=").append(apiKey);
        String url = urlBuilder.toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        return CompletableFuture.supplyAsync(() -> {
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    return response.body().string();
                } else {
                    return "{\"error\": \"" + response.message() + "\"}";
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error during book search", e);
                return "{\"error\": \"An error occurred: " + e.getMessage() + "\"}";
            }
        }, executor);
    }

    /**
     * Updates the suggestion list in the ListView with book information retrieved from the Google Books API.
     *
     * @param resultsJson A JSON string containing the search results from the Google Books API.
     */
    private void updateSuggestionList(String resultsJson) {
        suggestionsBox.getItems().clear();

        JsonObject jsonObject = JsonParser.parseString(resultsJson).getAsJsonObject();
        JsonArray items = jsonObject.has("items") ? jsonObject.getAsJsonArray("items") : null;

        if (items != null) {
            suggestionsBox.setVisible(true);

            for (int i = 0; i < items.size(); i++) {
                JsonObject item = items.get(i).getAsJsonObject();
                JsonObject volumeInfo = item.getAsJsonObject("volumeInfo");

                String title = volumeInfo.has("title") ? volumeInfo.get("title").getAsString() : "No Title Available";
                String authors = volumeInfo.has("authors") ? volumeInfo.get("authors").toString() : "Unknown Author";
                String thumbnailUrl = volumeInfo.has("imageLinks") && volumeInfo.getAsJsonObject("imageLinks").has("thumbnail")
                        ? volumeInfo.getAsJsonObject("imageLinks").get("thumbnail").getAsString()
                        : null;

                ImageView bookCover = new ImageView();
                if (thumbnailUrl != null) {
                    Image image = new Image(thumbnailUrl, true);
                    bookCover.setImage(image);
                }
                bookCover.setFitHeight(60);
                bookCover.setFitWidth(40);
                bookCover.setPreserveRatio(true);

                Text bookInfo = new Text("Title: " + title + "\nAuthors: " + authors);
                HBox bookItem = new HBox(10);
                bookItem.getChildren().addAll(bookCover, bookInfo);

                bookItem.setOnMouseClicked(event -> showBookDetail(volumeInfo));

                suggestionsBox.getItems().add(bookItem);
            }
        } else {
            suggestionsBox.setVisible(false);
        }
    }

    /**
     * Updates the ListView with the latest books retrieved from the database.
     * Fetches the books in a background thread and updates the ListView on the JavaFX Application thread.
     */
    private void updateListView() {
        CompletableFuture.runAsync(() -> {
            try {
                ObservableList<BookInAPI> books = FXCollections.observableArrayList(dbConnection.getLatestBooks(100));
                Platform.runLater(() -> {
                    listView.setItems(books);
                    listView.setCellFactory(param -> createBookCell());
                });
            } catch (Exception e) { // Catch general exception to handle all potential issues
                logger.log(Level.SEVERE, "Error while updating list view", e);
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Database Error");
                    alert.setHeaderText("Unable to retrieve books from database");
                    alert.setContentText("Please check the database connection and try again.");
                    alert.showAndWait();
                });
            }
        }, executor);
    }

    /**
     * Shows the detailed information of a selected book in the book detail box.
     *
     * @param volumeInfo A JsonObject containing detailed information about a book.
     */
    private void showBookDetail(JsonObject volumeInfo) {
        bookDetailBox.getChildren().clear();
        bookDetailBox.setVisible(true);
        bookDetailBox.toFront();
        bookDetailBox.setPrefWidth(800);
        bookDetailBox.setPrefHeight(600);

        String title = volumeInfo.has("title") ? volumeInfo.get("title").getAsString() : "No Title Available";
        String authors = volumeInfo.has("authors") ? volumeInfo.get("authors").toString() : "Unknown Author";
        String publisher = volumeInfo.has("publisher") ? volumeInfo.get("publisher").getAsString() : "Unknown Publisher";
        String publishedDate = volumeInfo.has("publishedDate") ? volumeInfo.get("publishedDate").getAsString() : "Unknown Date";
        String description = volumeInfo.has("description") ? volumeInfo.get("description").getAsString() : "No Description Available";
        String pageCount = volumeInfo.has("pageCount") ? volumeInfo.get("pageCount").getAsString() : "Unknown Pages";
        String categories = volumeInfo.has("categories") ? volumeInfo.get("categories").toString() : "Unknown Categories";
        String language = volumeInfo.has("language") ? volumeInfo.get("language").getAsString() : "Unknown Language";
        String averageRating = volumeInfo.has("averageRating") ? volumeInfo.get("averageRating").getAsString() : "No Rating";
        String ratingsCount = volumeInfo.has("ratingsCount") ? volumeInfo.get("ratingsCount").getAsString() : "No Ratings Count";
        String printType = volumeInfo.has("printType") ? volumeInfo.get("printType").getAsString() : "Unknown Print Type";
        String previewLink = volumeInfo.has("previewLink") ? volumeInfo.get("previewLink").getAsString() : "No Preview Link Available";
        String thumbnailUrl = volumeInfo.has("imageLinks") && volumeInfo.getAsJsonObject("imageLinks").has("thumbnail")
                ? volumeInfo.getAsJsonObject("imageLinks").get("thumbnail").getAsString()
                : null;

        ImageView bookCover = new ImageView();
        if (thumbnailUrl != null) {
            Image image = new Image(thumbnailUrl, true);
            bookCover.setImage(image);
        }
        bookCover.setFitHeight(150);
        bookCover.setFitWidth(100);
        bookCover.setPreserveRatio(true);

        Text titleText = new Text("Title: " + title);
        Text authorsText = new Text("Authors: " + authors);
        Text publisherText = new Text("Publisher: " + publisher);
        Text publishedDateText = new Text("Published Date: " + publishedDate);
        Text pageCountText = new Text("Page Count: " + pageCount);
        Text categoriesText = new Text("Categories: " + categories);
        Text languageText = new Text("Language: " + language);
        Text averageRatingText = new Text("Average Rating: " + averageRating);
        Text ratingsCountText = new Text("Ratings Count: " + ratingsCount);
        Text printTypeText = new Text("Print Type: " + printType);

        bookDescriptionTextArea.setText(description);

        Hyperlink previewLinkLabel = new Hyperlink("Click here to preview the book");
        if (previewLink != null) {
            previewLinkLabel.setOnAction(event -> {
                try {
                    java.awt.Desktop.getDesktop().browse(new java.net.URI(previewLink));
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Error opening preview link", e);
                }
            });
        } else {
            previewLinkLabel.setText("Preview not available");
            previewLinkLabel.setDisable(true);
        }

        bookDetailBox.getChildren().addAll(bookCover, titleText, authorsText, publisherText, publishedDateText,
                pageCountText, categoriesText, languageText, averageRatingText, ratingsCountText, printTypeText,
                previewLinkLabel, bookDescriptionTextArea);

        try {
            int pageCountValue = pageCount.equals("Unknown Pages") ? 0 : Integer.parseInt(pageCount);
            double averageRatingValue = averageRating.equals("No Rating") ? 0.0 : Double.parseDouble(averageRating);
            int ratingsCountValue = ratingsCount.equals("No Ratings Count") ? 0 : Integer.parseInt(ratingsCount);

            byte[] coverImage = new byte[0];
            if (thumbnailUrl != null) {
                try {
                    URL url = new URL(thumbnailUrl);
                    coverImage = url.openStream().readAllBytes();
                } catch (IOException e) {
                    logger.log(Level.WARNING, "Failed to download cover image", e);
                }
            }

            dbConnection.saveBook(
                    title,
                    authors,
                    publisher,
                    publishedDate,
                    pageCountValue,
                    categories,
                    language,
                    averageRatingValue,
                    ratingsCountValue,
                    printType,
                    previewLink,
                    description,
                    coverImage
            );

            dbConnection.deleteOldBooks(100);
            updateListView();

        } catch (Exception e) { // Catch general exception to handle all potential issues
            logger.log(Level.SEVERE, "Error while saving book", e);
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Database Error");
                alert.setHeaderText("Unable to save book to database");
                alert.setContentText("Please check the database connection and try again.");
                alert.showAndWait();
            });
        }
    }

    /**
     * Handles KeyEvent for updating book suggestions based on user input.
     * When the user types in the searchField, this method is triggered to display book suggestions.
     *
     * @param event The KeyEvent containing information about the user's key press.
     */
    @FXML
    void updateSuggestions(KeyEvent event) {
        String query = searchField.getText().trim();
        if (!query.isEmpty()) {
            searchBooks(query)
                    .thenAcceptAsync(resultsJson -> Platform.runLater(() -> updateSuggestionList(resultsJson)));
        } else {
            suggestionsBox.setVisible(false);
        }
    }

    /**
     * Handles ActionEvent for the search button to search for books based on user input.
     *
     * @param e The ActionEvent triggered when the search button is clicked.
     */
    @FXML
    void searchBookButtonOnAction(ActionEvent e) {
        String query = searchField.getText().trim();
        if (!query.isEmpty()) {
            searchBooks(query)
                    .thenAcceptAsync(resultsJson -> Platform.runLater(() -> updateSuggestionList(resultsJson)));
        }
    }

    /**
     * Creates a ListCell for displaying BookInAPI instances in the ListView.
     * Uses an FXML loader to load the custom cell layout and set book information.
     *
     * @return A new ListCell for displaying books.
     */
    private ListCell<BookInAPI> createBookCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(BookInAPI book, boolean empty) {
                super.updateItem(book, empty);
                if (empty || book == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/lma/objectum/fxml/BookCell.fxml"));
                        HBox bookCell = loader.load();
                        BookCellController controller = loader.getController();
                        controller.setBook(book);
                        setGraphic(bookCell);
                    } catch (IOException e) {
                        logger.log(Level.SEVERE, "Error loading book cell", e);
                    }
                }
            }
        };
    }

    /**
     * Initializes the API controller and sets up event listeners and UI elements.
     * This method is called automatically after the FXML has been loaded.
     */
    @FXML
    public void initialize() {
        searchField.setOnKeyReleased(this::updateSuggestions);
        suggestionsBox.setVisible(false);
        bookDetailBox.setVisible(false);
        bookDetailBox.setOnMouseClicked(event -> {
            bookDetailBox.setVisible(false);
            suggestionsBox.setVisible(true);
        });
        updateListView();
        listView.setCellFactory(param -> createBookCell());
        Platform.runLater(this::updateListView);
    }
}
