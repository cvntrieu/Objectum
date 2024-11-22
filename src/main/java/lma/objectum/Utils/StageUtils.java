package lma.objectum.Utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lma.objectum.Controllers.SessionManager;
import lma.objectum.Controllers.TransactionController;
import lma.objectum.Database.DatabaseConnection;
import lma.objectum.Models.Book;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StageUtils {

    /**
     * Load an FXML file and display it in a new Stage.
     *
     * @param fxmlPath  Path to the FXML file.
     * @param stageTitle Title of the new Stage.
     * @throws IOException If the FXML file cannot be loaded.
     */
    public static Stage loadFXMLStage(String fxmlPath, String stageTitle) throws IOException {
        FXMLLoader loader = new FXMLLoader(StageUtils.class.getResource(fxmlPath));
        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(stageTitle);
        return stage;
    }

    /**
     * Load an FXML file with a CSS stylesheet and display it in a new Stage.
     *
     * @param fxmlPath    Path to the FXML file.
     * @param cssPath     Path to the CSS file.
     * @param stageTitle  Title of the new Stage.
     * @throws IOException If the FXML file or CSS file cannot be loaded.
     */
    public static Stage loadFXMLStageWithCSS(String fxmlPath, String cssPath, String stageTitle) throws IOException {
        FXMLLoader loader = new FXMLLoader(StageUtils.class.getResource(fxmlPath));
        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(StageUtils.class.getResource(cssPath).toExternalForm());
        stage.setScene(scene);
        stage.setTitle(stageTitle);
        return stage;
    }

    /**
     * Load an FXML file for admin or member and display it in a new Stage.
     *
     * @param adminFXMLPath Path to the FXML file for admin.
     * @param memberFXMLPath Path to the FXML file for member.
     * @param stageTitle Title of the stage.
     * @throws IOException  If the FXML file cannot be loaded.
     * @throws SQLException If the database query fails.
     */
    public static Stage loadRoleBasedStage(String adminFXMLPath, String memberFXMLPath,
                                           String stageTitle) throws IOException, SQLException {
        boolean isAdmin = checkIfAdmin();

        FXMLLoader loader = new FXMLLoader(StageUtils.class.getResource(
                isAdmin ? adminFXMLPath : memberFXMLPath
        ));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(stageTitle);
        return stage;
    }

    /**
     * Loads an FXML file into a new stage, plays background music, and optionally closes the current stage.
     *
     * @param fxmlPath      the path to the FXML file
     * @param title         the title of the new stage
     * @param musicPath     the path to the music file (can be null if no music needs to be played)
     */
    public static Stage loadStageWithMusic(String fxmlPath, String title, String musicPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(StageUtils.class.getResource(fxmlPath));
        Parent root = loader.load();

        if (musicPath != null) {
            MusicPlayer.playMusic(musicPath);
        }

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        return stage;
    }

    /**
     * Load an FXML file and display it in a new Stage.
     *
     * @param fxmlPath  Path to the FXML file.
     * @param stageTitle Title of the new Stage.
     * @throws IOException If the FXML file cannot be loaded.
     */
    public static Stage loadFXMLStageWithPrefillData(String fxmlPath, String cssPath, String stageTitle, Book book)
            throws IOException {
        FXMLLoader loader = new FXMLLoader(StageUtils.class.getResource(fxmlPath));
        Parent root = loader.load();

        TransactionController transactionController = loader.getController();
        transactionController.prefillData(book.getIsbn_13());
        transactionController.scanQRCode(book);

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(StageUtils.class.getResource(cssPath).toExternalForm());
        stage.setScene(scene);
        stage.setTitle(stageTitle);
        return stage;
    }

    /**
     * Check if the current user is an admin.
     *
     * @return true if the user is an admin, false otherwise.
     * @throws SQLException If the database query fails.
     */
    private static boolean checkIfAdmin() throws SQLException {
        Connection connectDB = DatabaseConnection.getInstance().getConnection();
        String username = SessionManager.getInstance().getCurrentUsername();
        String query = "SELECT role FROM useraccount WHERE username = ?";

        try (PreparedStatement preparedStatement = connectDB.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet queryResult = preparedStatement.executeQuery()) {
                if (queryResult.next()) {
                    String role = queryResult.getString("role");
                    return "admin".equalsIgnoreCase(role);
                }
            }
        }
        return false;
    }
}
