package lma.objectum.Controllers;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * Controller for the Music Player.
 * This class provides methods to control music playback, update UI elements with song information, and manage
 * playback options like shuffle and repeat.
 */
public class MusicPlayerController {

    @FXML
    private ImageView albumCover;

    @FXML
    private Label songTitle;

    @FXML
    private Label singerName;

    @FXML
    private Slider progressSlider;

    @FXML
    private Label currentTimeLabel;

    @FXML
    private Label totalTimeLabel;

    @FXML
    private Button playPauseButton;

    @FXML
    private Button nextButton;

    @FXML
    private Button previousButton;

    @FXML
    private Button shuffleButton;

    @FXML
    private Button repeatButton;

    private MediaPlayer mediaPlayer;

    private boolean isShuffle = false;
    private boolean isRepeat = false;

    private RotateTransition rotateTransition;

    /**
     * Initializes the Music Player Controller.
     * Sets up the album cover, play/pause button, next and previous buttons, and rotation animation.
     * Adds event listeners to update progress and respond to button clicks.
     */
    public void initialize() {
        // Set up the circular image clip
        albumCover.setFitWidth(200);
        albumCover.setFitHeight(200);
        albumCover.setPreserveRatio(true);
        Circle clip = new Circle(100, 100, 100);
        albumCover.setClip(clip);

        // Set up rotation for album cover
        rotateTransition = new RotateTransition(Duration.seconds(10), albumCover);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE);
        rotateTransition.setInterpolator(Interpolator.LINEAR);
        rotateTransition.play(); // Start rotating the image immediately

        playPauseButton.setOnAction(event -> handlePlayPause());
        nextButton.setOnAction(event -> skipForward());
        previousButton.setOnAction(event -> skipBackward());
        shuffleButton.setOnAction(event -> toggleShuffle());
        repeatButton.setOnAction(event -> toggleRepeat());

        progressSlider.valueChangingProperty().addListener((obs, wasChanging, isNowChanging) -> {
            if (!isNowChanging && mediaPlayer != null) {
                mediaPlayer.seek(mediaPlayer.getMedia().getDuration().multiply(progressSlider.getValue() / 100));
            }
        });
    }

    /**
     * Sets the MediaPlayer instance for the Music Player.
     * Configures the media player to update UI elements like song title, duration, and progress.
     *
     * @param mediaPlayer The MediaPlayer instance to be controlled.
     */
    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;

        mediaPlayer.setOnReady(() -> {
            totalTimeLabel.setText(formatTime(mediaPlayer.getTotalDuration()));
            progressSlider.setDisable(false);
            songTitle.setText("Sample Song Title");
            singerName.setText("Sample Artist Name");
            albumCover.setImage(new Image("path/to/default_album_cover.jpg"));
        });

        mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            if (!progressSlider.isValueChanging()) {
                progressSlider.setValue(newTime.toMillis() / mediaPlayer.getTotalDuration().toMillis() * 100);
                currentTimeLabel.setText(formatTime(newTime));
            }
        });

        mediaPlayer.setOnEndOfMedia(() -> {
            if (isRepeat) {
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.play();
            } else if (isShuffle) {
                playRandomTrack();
            } else {
                skipForward();
            }
        });

        mediaPlayer.setVolume(0.5);
    }

    /**
     * Handles the play/pause action.
     * If the media player is playing, pauses it and updates the button text to "Play".
     * If the media player is paused, plays it and updates the button text to "Pause".
     */
    private void handlePlayPause() {
        if (mediaPlayer != null) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
                playPauseButton.setText("Play");
            } else {
                mediaPlayer.play();
                playPauseButton.setText("Pause");
            }
        }
    }

    /**
     * Sets the song information in the UI.
     * Updates the song title, artist name, and album cover.
     *
     * @param title The title of the song.
     * @param artist The artist name.
     * @param albumImageUrl The URL of the album cover image.
     */
    public void setSongInfo(String title, String artist, String albumImageUrl) {
        songTitle.setText(title);
        singerName.setText(artist);
        if (albumImageUrl != null && !albumImageUrl.isEmpty()) {
            albumCover.setImage(new Image(albumImageUrl));
        } else {
            albumCover.setImage(new Image("path/to/default_album_cover.jpg"));
        }
    }

    /**
     * Formats a Duration object into a time string in the format "minutes:seconds".
     *
     * @param duration The duration to format.
     * @return A formatted time string.
     */
    private String formatTime(Duration duration) {
        int minutes = (int) duration.toMinutes();
        int seconds = (int) (duration.toSeconds() % 60);
        return String.format("%d:%02d", minutes, seconds);
    }

    /**
     * Skips forward by 10 seconds in the current track.
     */
    private void skipForward() {
        if (mediaPlayer != null) {
            mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(10)));
        }
    }

    /**
     * Skips backward by 10 seconds in the current track.
     */
    private void skipBackward() {
        if (mediaPlayer != null) {
            mediaPlayer.seek(mediaPlayer.getCurrentTime().subtract(Duration.seconds(10)));
        }
    }

    /**
     * Toggles shuffle mode on or off.
     * Updates the button style to indicate shuffle status.
     */
    private void toggleShuffle() {
        isShuffle = !isShuffle;
        shuffleButton.setStyle(isShuffle ? "-fx-background-color: #90EE90;" : "-fx-background-color: transparent;");
    }

    /**
     * Toggles repeat mode on or off.
     * Updates the button style to indicate repeat status.
     */
    private void toggleRepeat() {
        isRepeat = !isRepeat;
        repeatButton.setStyle(isRepeat ? "-fx-background-color: #ADD8E6;" : "-fx-background-color: transparent;");
    }

    /**
     * Plays a random track from the playlist.
     * Currently, this method is a placeholder and should be implemented with proper playlist handling.
     */
    private void playRandomTrack() {
        // Add logic to play a random track from the playlist
        System.out.println("Playing a random track.");
    }
}
