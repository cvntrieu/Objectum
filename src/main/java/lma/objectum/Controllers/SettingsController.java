package lma.objectum.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import lma.objectum.Database.DatabaseConnection;
import lma.objectum.Utils.MusicPlayer;
import lma.objectum.Utils.StageUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SettingsController {
    @FXML
    private Button playMusicButton;

    @FXML
    private Button stopMusicButton;

    @FXML
    private Slider volumeSlider;

    @FXML
    private Button homeButton;

    private final String musicPath = getClass().getResource("/lma/objectum/music/music.mp3").toString();

    /**
     * handle play music button.
     */
    @FXML
    public void handlePlayMusic() {
        MusicPlayer.playMusic(musicPath);
    }

    /**
     * handle stop music button.
     */
    @FXML
    public void handleStopMusic() {
        MusicPlayer.stopMusic();
    }

    /**
     * handle volume change.
     */
    @FXML
    public void handleVolumeChange() {
        double volume = volumeSlider.getValue();
        MusicPlayer.setVolume(volume);
    }

    /**
     * handle home button.
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

            Stage settingStage = (Stage) homeButton.getScene().getWindow();
            settingStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Check if the current user is an admin.
     *
     * @return true if the user is an admin, false otherwise
     * @throws SQLException exception handling
     */
    private boolean checkIfAdmin() throws SQLException {
        DatabaseConnection connectNow = DatabaseConnection.getInstance();
        Connection connectDB = connectNow.getConnection();
        String username = SessionManager.getInstance().getCurrentUsername();
        String query = "SELECT role FROM useraccount WHERE username = ?";

        try {
            PreparedStatement preparedStatement = connectDB.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet queryResult = preparedStatement.executeQuery();

            if (queryResult.next()) {
                String role = queryResult.getString("role");
                return "admin".equalsIgnoreCase(role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
