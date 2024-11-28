package lma.objectum.Controllers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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
import javafx.stage.Stage;
import lma.objectum.Models.BookInAPI;
import lma.objectum.Database.DatabaseConnection;
import lma.objectum.Utils.Config;
import lma.objectum.Utils.MusicPlayer;
import lma.objectum.Utils.StageUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for handling interactions with the Google Books API.
 * This class provides methods to search for books, display book suggestions, and update the user interface
 * with book details retrieved from the Google Books API or the local database.
 */
public class API {

    @FXML
    private Button accountButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button settingButton;

    @FXML
    private MenuItem borrowBooksItem;

    @FXML
    private MenuItem returnBooksItem;

    @FXML
    private Button APIButton;

    @FXML
    private Button MusicButton;


    @FXML
    private TextField searchField;

    @FXML
    private ListView<HBox> suggestionsBox;

    @FXML
    private ListView<BookInAPI> listView;

    @FXML
    private VBox bookDetailBox;

    @FXML
    private Button searchBook;

    @FXML
    private TextArea bookDescriptionTextArea;

    private static final Config config = new Config("config.properties");
    private final String apiKey = config.get("google.books.api.key");
    private final OkHttpClient client = new OkHttpClient();
    private final ExecutorService executor = Executors.newFixedThreadPool(5);
    private static final Logger logger = Logger.getLogger(API.class.getName());

    /**
     * Constructor for the API controller.
     * Checks if the API key is present in the configuration file.
     * If the API key is not found, a runtime exception is thrown.
     */
    public API() {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new RuntimeException("API key not found in configuration");
        }
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


    public void handleAccountButton() {

        try {
            Stage accountStage = StageUtils.loadFXMLStage(
                    "/lma/objectum/fxml/AccountView.fxml",
                    "Account View"
            );
            accountButton.getScene().getWindow().hide();
            accountStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void handleBorrowBooksItem() {

        try {
            Stage borrowBooksStage = StageUtils.loadFXMLStageWithCSS(
                    "/lma/objectum/fxml/BookSearch.fxml",
                    "/lma/objectum/css/BookSearchStyle.css",
                    "Borrow Books"
            );
            accountButton.getScene().getWindow().hide();
            borrowBooksStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleHomeButton() {

        try {
            Stage homeStage = StageUtils.loadRoleBasedStage(
                    "/lma/objectum/fxml/AdminHome.fxml",
                    "/lma/objectum/fxml/Home.fxml",
                    "Home"
            );
            accountButton.getScene().getWindow().hide();
            homeStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleReturnBooksItem() {

        try {
            Stage returnStage = StageUtils.loadFXMLStageWithCSS(
                    "/lma/objectum/fxml/Transaction.fxml",
                    "/lma/objectum/css/TransactionStyle.css",
                    "Return Books"
            );
            accountButton.getScene().getWindow().hide();
            returnStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * API Button on action.
     */
    @FXML
    public void handleAPIButtonAction() {
        try {
            Stage apiStage = StageUtils.loadFXMLStage(
                    "/lma/objectum/fxml/API.fxml",
                    "API View"
            );
            accountButton.getScene().getWindow().hide();
            apiStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Handling logout Button.
     */
    @FXML
    public void handleLogOutButton() {
        try {
            Stage loginStage = StageUtils.loadFXMLStage(
                    "/lma/objectum/fxml/App.fxml",
                    "Main Application"
            );
            accountButton.getScene().getWindow().hide();
            MusicPlayer.stopMusic();
            loginStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleMusicButtonAction() {
        try {
            Stage musicStage = StageUtils.loadFXMLStage(
                    "/lma/objectum/fxml/Music.fxml",
                    "API View"
            );
            accountButton.getScene().getWindow().hide();
            musicStage.show();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void handleGameButton() {
        try {
            Stage settingStage = StageUtils.loadFXMLStage(
                    "/lma/objectum/fxml/Game.fxml",
                    "Game"
            );
            accountButton.getScene().getWindow().hide();
            settingStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handling setting button.
     */
    @FXML
    public void handleSettingButton() {
        try {
            Stage settingStage = StageUtils.loadFXMLStageWithCSS(
                    "/lma/objectum/fxml/Setting.fxml",
                    "/lma/objectum/css/SettingStyle.css",
                    "Settings"
            );
            accountButton.getScene().getWindow().hide();
            settingStage.show();

        } catch (IOException e) {
            e.printStackTrace();
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
                ObservableList<BookInAPI> books = FXCollections.observableArrayList(getLatestBooks());
                Platform.runLater(() -> {
                    listView.setItems(books);
                    listView.setCellFactory(param -> createBookCell());
                });
            } catch (Exception e) {
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

            saveBook(
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

            deleteOldBooks();
            updateListView();

        } catch (Exception e) {
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
     * Saves a book to the database.
     *
     * @param title Title of the book
     * @param authors Authors of the book
     * @param publisher Publisher of the book
     * @param publishedDate Published date of the book
     * @param pageCount Number of pages in the book
     * @param categories Categories of the book
     * @param language Language of the book
     * @param averageRating Average rating of the book
     * @param ratingsCount Number of ratings
     * @param printType Print type of the book
     * @param previewLink Preview link of the book
     * @param description Description of the book
     * @param coverImage Cover image of the book
     */
    private void saveBook(String title, String authors, String publisher, String publishedDate, int pageCount,
                         String categories, String language, double averageRating, int ratingsCount,
                         String printType, String previewLink, String description, byte[] coverImage) throws SQLException {

        Connection connection = DatabaseConnection.getInstance().getConnection();
        String insertQuery =
                "INSERT INTO booksInAPI (title, authors, publisher, publishedDate, pageCount, categories, language, " +
                        "averageRating, ratingsCount, printType, previewLink, description, coverImage) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
            stmt.setString(1, title);
            stmt.setString(2, authors);
            stmt.setString(3, publisher);
            stmt.setString(4, publishedDate);
            stmt.setInt(5, pageCount);
            stmt.setString(6, categories);
            stmt.setString(7, language);
            stmt.setDouble(8, averageRating);
            stmt.setInt(9, ratingsCount);
            stmt.setString(10, printType);
            stmt.setString(11, previewLink);
            stmt.setString(12, description);
            stmt.setBytes(13, coverImage);

            stmt.executeUpdate();
            logger.info("Book saved to database");
        } catch (SQLIntegrityConstraintViolationException e) {
            logger.info("Duplicate entry detected for book");
            showAlert("Duplicate entry for book: " + title);
        } catch (SQLException e) {
            logger.info("Error while saving book to database");
            showAlert("Unable to save book to database. Error details: "
                    + e.getMessage());
        }
    }

    /**
     * Deletes old books from the database, keeping only the specified number of recent books.
     */
    private void deleteOldBooks() throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String deleteQuery = "DELETE FROM booksInAPI " +
                "WHERE id NOT IN (SELECT t.id FROM (SELECT id FROM booksInAPI ORDER BY id DESC LIMIT ?) AS t)";

        try (PreparedStatement stmt = connection.prepareStatement(deleteQuery)) {
            stmt.setInt(1, 100);
            int rowsAffected = stmt.executeUpdate();
            logger.info("Old books deleted, rows affected: " + rowsAffected);
        } catch (SQLException e) {
            logger.info("Error while deleting old books");
        }
    }

    /**
     * Fetches the latest books from the database.
     *
     * @return List of BookInAPI objects
     */
    private List<BookInAPI> getLatestBooks() throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();

        List<BookInAPI> books = new ArrayList<>();
        String selectQuery = "SELECT title, authors, publisher, coverImage FROM booksInAPI ORDER BY id DESC LIMIT ?";

        try (PreparedStatement stmt = connection.prepareStatement(selectQuery)) {
            stmt.setInt(1, 100);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String title = rs.getString("title");
                    String authors = rs.getString("authors");
                    String publisher = rs.getString("publisher");
                    byte[] coverImage = rs.getBytes("coverImage");
                    books.add(new BookInAPI(title, authors, publisher, coverImage));
                }
            }
        } catch (SQLException e) {
            logger.info("Error while fetching latest books");
        }
        return books;
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
     * Displays an alert dialog with a given title and content.
     *
     * @param content Content of the alert
     */
    private void showAlert(String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }
}
