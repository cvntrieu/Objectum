package lma.objectum.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import lma.objectum.Database.DatabaseConnection;
import lma.objectum.Models.GameItem;
import lma.objectum.Utils.StageUtils;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game {
    @FXML
    private Button accountButton;
    @FXML
    private Button homeButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Button settingButton;
    @FXML
    private Button APIButton;
    @FXML
    private Button MusicButton;
    @FXML
    private ListView<GameItem> gameListView;
    @FXML
    private WebView webView;

    private WebEngine webEngine;
    private static final Logger logger = Logger.getLogger(Game.class.getName());
    private Connection conn;

    /**
     * Handles the action when the Account button is clicked.
     */
    @FXML
    public void AccountButton() {
        try {
            Stage accountStage = StageUtils.loadFXMLStage(
                    "/lma/objectum/fxml/AccountView.fxml",
                    "Account View"
            );
            accountButton.getScene().getWindow().hide();
            accountStage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading Account View", e);
        }
    }

    /**
     * Handles the action when the API button is clicked.
     */
    @FXML
    public void APIButton() {
        try {
            Stage apiStage = StageUtils.loadFXMLStage(
                    "/lma/objectum/fxml/API.fxml",
                    "API View"
            );
            accountButton.getScene().getWindow().hide();
            apiStage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading API View", e);
        }
    }

    /**
     * Handles the action when the LogOut button is clicked.
     */
    @FXML
    public void LogOutButton() {
        try {
            Stage loginStage = StageUtils.loadFXMLStage(
                    "/lma/objectum/fxml/App.fxml",
                    "Main Application"
            );
            accountButton.getScene().getWindow().hide();
            loginStage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading Main Application", e);
        }
    }

    /**
     * Handles the action when the Music button is clicked.
     */
    @FXML
    public void MusicButton() {
        try {
            Stage musicStage = StageUtils.loadFXMLStage(
                    "/lma/objectum/fxml/Music.fxml",
                    "Music View"
            );
            accountButton.getScene().getWindow().hide();
            musicStage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading Music View", e);
        }
    }

    /**
     * Handles the action when the Setting button is clicked.
     */
    @FXML
    public void SettingButton() {
        try {
            Stage settingStage = StageUtils.loadFXMLStageWithCSS(
                    "/lma/objectum/fxml/Setting.fxml",
                    "/lma/objectum/css/SettingStyle.css",
                    "Settings"
            );
            accountButton.getScene().getWindow().hide();
            settingStage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading Settings", e);
        }
    }

    /**
     * Handles the action when the Borrow Books item is selected.
     */
    public void BorrowBooksItem() {
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
     * Handles the action when the Home button is clicked.
     */
    public void HomeButton() {
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
     * Handles the action when the Return Books item is selected.
     */
    public void ReturnBooksItem() {
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
     * Initializes the Game view, including setting up the WebView and ListView.
     */
    @FXML
    public void initialize() {
        webEngine = webView.getEngine();
        gameListView.setCellFactory(param -> new ListCell<GameItem>() {
            @Override
            protected void updateItem(GameItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setText(item.getTitle());
                }
            }
        });

        try {
            DatabaseConnection dbConnection = DatabaseConnection.getInstance();
            conn = dbConnection.getConnection();
            loadAllGames();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error connecting to database", e);
            showErrorMessage("Database connection failed.");
        }
    }

    /**
     * Loads all games from the database in a separate thread.
     */
    private void loadAllGames() {
        new Thread(() -> {
            try {
                String sql = "SELECT title FROM games";
                try (PreparedStatement stmt = conn.prepareStatement(sql);
                     ResultSet rs = stmt.executeQuery()) {
                    List<GameItem> gameItems = new ArrayList<>();
                    while (rs.next()) {
                        String title = rs.getString("title");
                        gameItems.add(new GameItem(title, ""));
                    }
                    Platform.runLater(() -> updateGameList(gameItems));
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error loading games from database", e);
                Platform.runLater(() -> showErrorMessage("Error loading games from database"));
            }
        }).start();
    }

    /**
     * Updates the game list in the ListView.
     *
     * @param gameItems The list of games to be displayed.
     */
    private void updateGameList(List<GameItem> gameItems) {
        if (gameItems.isEmpty()) {
            gameListView.getItems().setAll(new GameItem("No games available", ""));
        } else {
            gameListView.getItems().setAll(gameItems);
        }

        gameListView.setOnMouseClicked(event -> {
            GameItem selectedItem = gameListView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                String selectedGame = selectedItem.getTitle();
                loadGamePage(selectedGame);
            }
        });
    }

    /**
     * Loads the game page in the WebView when a game is selected.
     *
     * @param gameTitle The title of the selected game.
     */
    private void loadGamePage(String gameTitle) {
        new Thread(() -> {
            try {
                String sql = "SELECT url FROM games WHERE title = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, gameTitle);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            String gameUrl = rs.getString("url");
                            Platform.runLater(() -> {
                                webView.getEngine().load(gameUrl);
                            });
                        } else {
                            Platform.runLater(() -> showErrorMessage("Game page not found"));
                        }
                    }
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error loading game page", e);
                Platform.runLater(() -> showErrorMessage("Error loading game page"));
            }
        }).start();
    }

    /**
     * Displays an error message in an alert.
     *
     * @param message The message to display in the alert.
     */
    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
