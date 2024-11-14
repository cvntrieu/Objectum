package lma.objectum.Controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class API {
    @FXML
    private TextField searchField;

    @FXML
    private ListView<HBox> suggestionsBox;

    @FXML
    private VBox bookDetailBox;

    @FXML
    private Button searchBook;

    @FXML
    private TextArea bookDescriptionTextArea;


    private final String apiKey = "AIzaSyCUB7PSg_v5EqFrzdC13A644v30JhDNT9Q";
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Searches for books using the Google Books API based on the provided query.
     *
     * @param query A search string with book titles or author names (e.g., "Harry Potter; author:J.K. Rowling").
     * @return A JSON string containing the search results or an error message.
     */
    public String searchBooks(String query) {
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

        System.out.println("Search URL: " + url);

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                System.out.println("API Response: " + responseBody);  // Debug response
                return responseBody;
            } else {
                System.out.println("API Error: " + response.message());  // Debug API error
                return "{\"error\": \"" + response.message() + "\"}";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "{\"error\": \"An error occurred: " + e.getMessage() + "\"}";
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
     * Shows the detailed information of a selected book in the book detail box.
     *
     * @param volumeInfo A JsonObject containing detailed information about a book.
     */
    private void showBookDetail(JsonObject volumeInfo) {
        bookDetailBox.getChildren().clear();
        bookDetailBox.setVisible(true);

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
        String previewLink = volumeInfo.has("previewLink") ? volumeInfo.get("previewLink").getAsString() : "No Preview Link Available";
        String printType = volumeInfo.has("printType") ? volumeInfo.get("printType").getAsString() : "Unknown Print Type";
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
        Text previewLinkText = new Text("Preview Link: " + previewLink);
        bookDescriptionTextArea.setText(description);

        Hyperlink previewLinkLabel = new Hyperlink("Click here to preview the book");
        bookDetailBox.getChildren().add(previewLinkLabel);
        if (previewLink != null) {
            previewLinkLabel.setOnAction(event -> {
                try {
                    java.awt.Desktop.getDesktop().browse(new java.net.URI(previewLink));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } else {
            previewLinkLabel.setText("Preview not available");
            previewLinkLabel.setDisable(true);
        }
        bookDetailBox.getChildren().addAll(bookCover, titleText, authorsText, publisherText, publishedDateText,
                pageCountText, categoriesText, languageText, averageRatingText, ratingsCountText, printTypeText, previewLinkText, bookDescriptionTextArea);
    }
    /**
     * Updates the suggestions list based on the text currently present in the search field.
     *
     * @param event The KeyEvent triggered when the user types in the search field.
     */
    @FXML
    void updateSuggestions(KeyEvent event) {
        String query = searchField.getText().trim();
        if (!query.isEmpty()) {
            String resultsJson = searchBooks(query);
            updateSuggestionList(resultsJson);
        } else {
            suggestionsBox.setVisible(false);
        }
    }
    /**
     * Performs a search operation when the search button is clicked.
     *
     * @param e The ActionEvent triggered when the search button is clicked.
     */
    @FXML
    void searchBookButtonOnAction(ActionEvent e) {
        String query = searchField.getText().trim();
        if (!query.isEmpty()) {
            String resultsJson = searchBooks(query);
            updateSuggestionList(resultsJson);
        }
    }
    /**
     * Initializes the necessary UI components and sets the event handlers for the text field and book detail view.
     * This method is automatically called after the FXML file is loaded.
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
    }
}
