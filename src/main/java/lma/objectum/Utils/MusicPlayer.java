package lma.objectum.Utils;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicPlayer {

    private static MediaPlayer mediaPlayer;

    /**
     * Play music.
     *
     * @param musicFile music file
     */
    public static void playMusic(String musicFile) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }

        try {
            Media media = new Media(musicFile);
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();
        } catch (Exception e) {
            System.err.println("Cannot play music " + e.getMessage());
        }
    }

    /**
     * Stop music.
     */
    public static void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    /**
     * Set volume.
     *
     * @param volume volume
     */
    public static void setVolume(double volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(Math.max(0.0, Math.min(volume, 1.0)));
        }
    }
}

