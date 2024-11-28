package lma.objectum.Controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import lma.objectum.Utils.Config;
import lma.objectum.Utils.MusicPlayer;
import lma.objectum.Utils.StageUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpServer;

public class Music {

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
    private ListView<HBox> songListView;

    @FXML
    private AnchorPane musicPlayerContainer;

    @FXML
    private Button searchMusic;

    private static final Config config = new Config("config.properties");
    private final String clientId = config.get("clientId");
    private final String clientSecret = config.get("clientSecret");
    private final OkHttpClient client = new OkHttpClient();
    private final ExecutorService executor = Executors.newFixedThreadPool(10);
    private static final Logger logger = Logger.getLogger(Music.class.getName());
    private JsonArray tracks;
    private MediaPlayer mediaPlayer;

    @FXML
    public void initialize() {
    }

    /**
     * Handles the Account button action.
     * Opens the Account View window.
     */
    @FXML
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

    /**
     * Handles the Borrow Books menu item action.
     * Opens the Borrow Books window.
     */
    @FXML
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

    /**
     * Handles the Home button action.
     * Opens the Home View window.
     */
    @FXML
    public void handleHomeButton() {

        try {
            String fxmlPath = "/lma/objectum/fxml/Home.fxml";
            String musicPath = getClass().getResource("/lma/objectum/music/music.mp3").toString();
            Stage homeStage = StageUtils.loadStageWithMusic(fxmlPath, "Main Application", musicPath);

            accountButton.getScene().getWindow().hide();
            homeStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the Return Books menu item action.
     * Opens the Return Books window.
     */
    @FXML
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
     * Handles the API button action.
     * Opens the API View window.
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
     * Handles the Logout button action.
     * Logs out the user and opens the Main Application window.
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

    /**
     * Handles the Music button action.
     * Opens the Music View window.
     */
    @FXML
    public void handleMusicButtonAction() {
        try {
            Stage musicStage = StageUtils.loadFXMLStage(
                    "/lma/objectum/fxml/Music.fxml",
                    "API View"
            );
            accountButton.getScene().getWindow().hide();
            musicStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the Game button action.
     * Opens the Game window.
     */
    @FXML
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
     * Handles the Setting button action.
     * Opens the Settings window.
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
     * Starts the OAuth process to authorize the application with the Jamendo API.
     * This method opens a new window for the user to grant authorization and
     * retrieves the authorization code to exchange for an access token.
     */
    @FXML
    private void startOAuthProcess() {
        String authUrl = "https://api.jamendo.com/v3.0/oauth/authorize?client_id="
                + clientId
                + "&redirect_uri=http://localhost:8080&response_type=code";
        CompletableFuture.runAsync(() -> {
            try {
                HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
                server.createContext("/", exchange -> {
                    String query = exchange.getRequestURI().getQuery();
                    if (query != null && query.contains("code=")) {
                        String code = query.split("code=")[1];
                        System.out.println("Authorization code: " + code);
                        getAccessToken(code);
                    }
                    String response = "You can close this window now.";
                    exchange.sendResponseHeaders(200, response.length());
                    exchange.getResponseBody().write(response.getBytes());
                    exchange.getResponseBody().close();
                });
                server.start();
                java.awt.Desktop.getDesktop().browse(URI.create(authUrl));
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error during OAuth process", e);
            }
        }, executor);
    }

    /**
     * Retrieves an access token from the Jamendo API using the provided authorization code.
     * This method makes a POST request to the Jamendo API to exchange the code for an access token,
     * which is then used to authenticate subsequent requests.
     *
     * @param code The authorization code obtained from the OAuth process.
     */
    private void getAccessToken(String code) {
        String tokenUrl = "https://api.jamendo.com/v3.0/oauth/access_token";
        String url = tokenUrl + "?client_id="
                + clientId + "&client_secret=" + clientSecret
                + "&code=" + code + "&grant_type=authorization_code&redirect_uri=http://localhost:8080";

        Request request = new Request.Builder().url(url).post(okhttp3.RequestBody.create(new byte[0])).build();

        CompletableFuture.runAsync(() -> {
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseData = response.body().string();
                    JsonObject json = JsonParser.parseString(responseData).getAsJsonObject();
                    String accessToken = json.get("access_token").getAsString();
                    fetchSongList(accessToken);
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error fetching access token", e);
            }
        }, executor);
    }

    /**
     * Fetches a list of songs from the Jamendo API using the provided access token.
     * This method makes a GET request to the Jamendo API to retrieve a list of tracks,
     * which is then parsed and displayed in the songListView.
     *
     * @param accessToken The access token used to authenticate the request.
     */
    private void fetchSongList(String accessToken) {
        String url = "https://api.jamendo.com/v3.0/tracks/?access_token=" + accessToken + "&format=json&limit=10";

        Request request = new Request.Builder().url(url).build();

        CompletableFuture.runAsync(() -> {
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseData = response.body().string();
                    System.out.println("Response Data: " + responseData);

                    JsonObject json = JsonParser.parseString(responseData).getAsJsonObject();
                    tracks = json.getAsJsonArray("results");

                    System.out.println("Number of tracks found: " + tracks.size());

                    Platform.runLater(() -> {
                        songListView.getItems().clear();
                        for (int i = 0; i < tracks.size(); i++) {
                            JsonObject track = tracks.get(i).getAsJsonObject();
                            String songTitle = track.get("name").getAsString();
                            String artistName = track.get("artist_name").getAsString();
                            String albumImageUrl = track.has("album_image") ?
                                    track.get("album_image").getAsString() : null;
                            String audioUrl = track.has("audio") ? track.get("audio").getAsString() : null;

                            if (audioUrl == null || audioUrl.isEmpty()) {
                                System.out.println("No audio URL available for track: " + songTitle);
                                continue;
                            }

                            HBox songItem = new HBox();
                            songItem.setSpacing(10);

                            ImageView albumCover = new ImageView();
                            if (albumImageUrl != null) {
                                Image image = new Image(albumImageUrl, true);
                                albumCover.setImage(image);
                            }
                            albumCover.setFitHeight(50);
                            albumCover.setFitWidth(50);
                            albumCover.setPreserveRatio(true);

                            Label songLabel = new Label(songTitle + " - " + artistName);

                            songItem.getChildren().addAll(albumCover, songLabel);

                            songItem.setOnMouseClicked(event -> {
                                System.out.println("Track selected: " + songTitle);
                                openMusicPlayer(audioUrl, songTitle, artistName, albumImageUrl);
                            });

                            songListView.getItems().add(songItem);
                        }
                    });
                } else {
                    System.out.println("Failed to fetch song list: " + response.message());
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error during song list fetch", e);
            }
        }, executor);
    }

    /**
     * Updates the song list based on the search query entered by the user.
     * This method is triggered by a KeyEvent and fetches a list of tracks
     * matching the query from the Jamendo API.
     *
     * @param event The KeyEvent that triggered this method.
     */
    @FXML
    private void updateSuggestions(KeyEvent event) {
        String query = searchField.getText().trim();
        if (!query.isEmpty()) {
            searchTracks(query)
                    .thenAcceptAsync(resultsJson -> Platform.runLater(() -> updateSongList(resultsJson)), executor);
        }
    }

    /**
     * Searches for tracks matching the given query from the Jamendo API.
     * This method makes a GET request to the Jamendo API to retrieve a list of tracks
     * matching the query.
     *
     * @param query The search query entered by the user.
     * @return A CompletableFuture that will complete with the JSON string containing the search results.
     */
    private CompletableFuture<String> searchTracks(String query) {
        String url = "https://api.jamendo.com/v3.0/tracks/?client_id="
                + clientId + "&format=json&limit=10&name=" + query;

        Request request = new Request.Builder().url(url).build();

        return CompletableFuture.supplyAsync(() -> {
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    return response.body().string();
                } else {
                    return "{\"error\": \"" + response.message() + "\"}";
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error during track search", e);
                return "{\"error\": \"An error occurred: " + e.getMessage() + "\"}";
            }
        }, executor);
    }

    /**
     * Updates the songListView with the search results obtained from the Jamendo API.
     * This method parses the JSON string containing the search results and creates
     * HBox elements for each track, which are then added to the songListView.
     *
     * @param resultsJson The JSON string containing the search results.
     */
    private void updateSongList(String resultsJson) {
        JsonObject jsonObject = JsonParser.parseString(resultsJson).getAsJsonObject();
        JsonArray items = jsonObject.has("results") ?
                jsonObject.getAsJsonArray("results") : null;

        if (items != null) {
            songListView.getItems().clear();
            for (int i = 0; i < items.size(); i++) {
                JsonObject item = items.get(i).getAsJsonObject();
                String audioUrl = item.has("audio") ?
                        item.get("audio").getAsString() : null;
                String title = item.has("name") ?
                        item.get("name").getAsString() : "No Title Available";
                String artists = item.has("artist_name") ?
                        item.get("artist_name").getAsString() : "Unknown Artist";
                String albumImageUrl = item.has("album_image") ?
                        item.get("album_image").getAsString() : null;

                HBox songItem = new HBox();
                songItem.setSpacing(10);

                ImageView albumCover = new ImageView();
                if (albumImageUrl != null) {
                    Image image = new Image(albumImageUrl, true);
                    albumCover.setImage(image);
                }
                albumCover.setFitHeight(50);
                albumCover.setFitWidth(50);
                albumCover.setPreserveRatio(true);

                Label songLabel = new Label(title + " - " + artists);

                songItem.getChildren().addAll(albumCover, songLabel);
                songItem.setOnMouseClicked(event -> openMusicPlayer(audioUrl, title, artists, albumImageUrl));
                songListView.getItems().add(songItem);
            }
        } else {
            System.out.println("No results found");
        }
    }

    /**
     * Opens the music player and plays the selected track.
     * This method creates a new MediaPlayer instance for the given audio URL,
     * loads the MusicPlayer.fxml file to display the music.
     */
    private void openMusicPlayer(String audioUrl, String songTitle, String artistName, String albumImageUrl) {
        if (audioUrl != null && !audioUrl.isEmpty()) {
            try {
                Media media = new Media(audioUrl);
                if (mediaPlayer != null) {
                    mediaPlayer.dispose();
                }
                mediaPlayer = new MediaPlayer(media);
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/lma/objectum/fxml/MusicPlayer.fxml"));
                Parent root = loader.load();
                MusicPlayerController controller = loader.getController();
                controller.setMediaPlayer(mediaPlayer);
                controller.setSongInfo(songTitle, artistName, albumImageUrl);

                musicPlayerContainer.getChildren().clear();
                musicPlayerContainer.getChildren().add(root);
                mediaPlayer.play();
            } catch (Exception e) {
                System.out.println("Error opening music player: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("No valid audio URL available.");
        }

    }
}
