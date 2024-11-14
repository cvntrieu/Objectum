package lma.objectum.Controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class API {
    @FXML
    private TextField searchField;

    @FXML
    private ListView<String> suggestionsBox;

    @FXML
    private Button searchBook;

    private final String apiKey = "AIzaSyCUB7PSg_v5EqFrzdC13A644v30JhDNT9Q";
    private final OkHttpClient client = new OkHttpClient();

    public String searchBooks(String query, String searchType) {
        StringBuilder result = new StringBuilder();
        String url = "https://www.googleapis.com/books/v1/volumes?q=" + searchType + ":" + query + "&key=" + apiKey;

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
                JsonArray items = jsonObject.has("items") ? jsonObject.getAsJsonArray("items") : null;

                if (items != null) {
                    for (int i = 0; i < items.size(); i++) {
                        JsonObject item = items.get(i).getAsJsonObject();
                        JsonObject volumeInfo = item.getAsJsonObject("volumeInfo");

                        String title = volumeInfo.has("title") ? volumeInfo.get("title").getAsString() : "No Title Available";
                        String authors = volumeInfo.has("authors") ? volumeInfo.get("authors").toString() : "Unknown Author";
                        String publisher = volumeInfo.has("publisher") ? volumeInfo.get("publisher").getAsString() : "Unknown Publisher";
                        String publishedDate = volumeInfo.has("publishedDate") ? volumeInfo.get("publishedDate").getAsString() : "Unknown Date";

                        result.append("Title: ").append(title).append("\n");
                        result.append("Authors: ").append(authors).append("\n");
                        result.append("Publisher: ").append(publisher).append("\n");
                        result.append("Published Date: ").append(publishedDate).append("\n");
                        result.append("-----------------------------------------\n");
                    }
                } else {
                    result.append("No books found for the query: ").append(query).append("\n");
                }
            } else {
                result.append("Error: ").append(response.message()).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            result.append("An error occurred: ").append(e.getMessage()).append("\n");
        }

        return result.toString();
    }

    @FXML
    void searchBookButtonOnAction(ActionEvent e) {
        String query = searchField.getText().trim();
        if (!query.isEmpty()) {
            String searchType;

            if (query.matches("\\d{4}")) {
                searchType = "inpublisherdate";
            } else if (query.toLowerCase().contains("author:")) {
                searchType = "inauthor";
                query = query.replace("author:", "").trim();
            } else {
                searchType = "intitle";
            }
            String results = searchBooks(query, searchType);
            System.out.println(results);
            suggestionsBox.getItems().clear();

            String[] resultArray = results.split("\n-----------------------------------------\n");
            for (String result : resultArray) {
                if (!result.trim().isEmpty()) {
                    suggestionsBox.getItems().add(result);
                }
            }
        }
    }

}
