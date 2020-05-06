import javafx.collections.ObservableList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Player {

    private MainScene mainScene;
    private MediaPlayer mediaPlayer;
    private ObservableList<Track> tracks;
    private int currentTrackIndex = 0;

    public Player(MainScene mainScene, ObservableList<Track> tracks) {
        this.mainScene = mainScene;
        this.tracks = tracks;
    }

    public void setVolume(double volume) {
        mediaPlayer.setVolume(volume);
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void play() {
        mediaPlayer.play();
    }

    public void seek(long millis) {
        mediaPlayer.seek(Duration.millis(millis));
    }

    public Duration getTotalDuration() {
        return mediaPlayer.getTotalDuration();
    }

    public Duration getCurrentTime() {
        return mediaPlayer.getCurrentTime();
    }

    public boolean isPaused() {
        return mediaPlayer.getStatus().equals(MediaPlayer.Status.PAUSED);
    }

    public boolean isPlaying() {
        return mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING);
    }

    public void play(int trackIndex) {
        if (mediaPlayer != null) {
            mediaPlayer.dispose();
        }
        Logger.logPlayTrack(currentTrackIndex, trackIndex, tracks.get(currentTrackIndex), tracks.get(trackIndex));
        currentTrackIndex = trackIndex;
        Media media = tracks.get(trackIndex).media;
        mediaPlayer = new MediaPlayer(media);

        mediaPlayer.currentTimeProperty().addListener(obs -> {
            int currentTime = (int) getCurrentTime().toMillis();
            int totalTime = (int) getTotalDuration().toMillis();
            if (!mainScene.trackSliderDragged) {
                mainScene.trackSlider.setValue(currentTime * 100.0 / totalTime);
            }
            int mins = (int) (getCurrentTime().toSeconds() / 60);
            int secs = (int) (getCurrentTime().toSeconds() % 60);
            mainScene.timeLabel.setText(String.format("%d:%02d", mins, secs));
        });
        mediaPlayer.setOnReady(() -> {
            play();
            mediaPlayer.setOnEndOfMedia(() -> {
                if (!mainScene.trackSliderDragged) {
                    if (isFirst() && isLast()) {
                        mainScene.trackSlider.setValue(0);
                        seek(0);
                        play();
                        return;
                    }
                    if (isLast()) {
                        mainScene.controller.selectFirst();
                    } else {
                        mainScene.controller.selectNext(null);
                    }
                }
            });
        });
    }

    public boolean isFirst() {
        return currentTrackIndex == 0;
    }

    public boolean isLast() {
        return currentTrackIndex == tracks.size() - 1;
    }

}
