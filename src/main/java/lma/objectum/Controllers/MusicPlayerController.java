package lma.objectum.Controllers;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Controller for managing the music player UI and playback functionality.
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

    @FXML
    private Canvas waveformCanvas;

    private MediaPlayer mediaPlayer;

    private boolean isShuffle = false;
    private boolean isRepeat = false;

    private RotateTransition rotateTransition;

    /**
     * Initializes the MusicPlayerController, setting up UI elements and event listeners.
     */
    public void initialize() {
        albumCover.setFitWidth(200);
        albumCover.setFitHeight(200);
        albumCover.setPreserveRatio(true);
        Circle clip = new Circle(100, 100, 100);
        albumCover.setClip(clip);

        rotateTransition = new RotateTransition(Duration.seconds(10), albumCover);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE);
        rotateTransition.setInterpolator(Interpolator.LINEAR);
        rotateTransition.play();

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
     * Draws the waveform of the audio on the canvas.
     *
     * @param magnitudes The magnitudes of the audio spectrum to visualize.
     */
    private void drawWaveform(float[] magnitudes) {
        GraphicsContext gc = waveformCanvas.getGraphicsContext2D();
        double canvasWidth = waveformCanvas.getWidth();
        double canvasHeight = waveformCanvas.getHeight();

        gc.clearRect(0, 0, canvasWidth, canvasHeight);

        gc.setStroke(Color.CYAN);
        gc.setLineWidth(2);

        double xStep = canvasWidth / magnitudes.length * 2;
        for (int i = 0; i < magnitudes.length - 1; i++) {
            double x1 = i * xStep;
            double y1 = canvasHeight / 2 - magnitudes[i];
            double x2 = (i + 1) * xStep;
            double y2 = canvasHeight / 2 - magnitudes[i + 1];
            gc.strokeLine(x1, y1, x2, y2);
        }
    }

    /**
     * Sets the MediaPlayer instance for controlling playback.
     *
     * @param mediaPlayer The MediaPlayer to control.
     */
    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;

        mediaPlayer.setAudioSpectrumListener((timestamp, duration, magnitudes, phases) -> {
            drawWaveform(magnitudes);
        });

        mediaPlayer.setOnReady(() -> {
            totalTimeLabel.setText(formatTime(mediaPlayer.getTotalDuration()));
            progressSlider.setDisable(false);
            songTitle.setText("Sample Song Title");
            singerName.setText("Sample Artist Name");
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
     * Handles the play/pause functionality for the media player.
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
     * Updates the song information (title, artist, album cover).
     *
     * @param title The song's title.
     * @param artist The artist's name.
     * @param albumImageUrl The URL for the album cover image.
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
     * Formats a Duration object into a time string in "minutes:seconds" format.
     *
     * @param duration The Duration to format.
     * @return The formatted time string.
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
     * Toggles the shuffle mode for the media player.
     */
    private void toggleShuffle() {
        isShuffle = !isShuffle;
        shuffleButton.setStyle(isShuffle ? "-fx-background-color: #90EE90;" : "-fx-background-color: transparent;");
    }

    /**
     * Toggles the repeat mode for the media player.
     */
    private void toggleRepeat() {
        isRepeat = !isRepeat;
        repeatButton.setStyle(isRepeat ? "-fx-background-color: #ADD8E6;" : "-fx-background-color: transparent;");
    }

    /**
     * Plays a random track from the playlist.
     * Placeholder method, should be implemented with actual playlist handling.
     */
    private void playRandomTrack() {
        System.out.println("Playing a random track.");
    }
}
