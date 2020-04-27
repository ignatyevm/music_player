import javafx.collections.ObservableList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Player {

    private MainScene mainScene;
    private MediaPlayer mediaPlayer;
    private ObservableList<Track> tracks;
    private int currentTrackIndex = 0;

    public Player(MainScene mainScene, ObservableList<Track> tracks) {
        this.mainScene = mainScene;
        this.tracks = tracks;
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void play() {
        mediaPlayer.play();
    }

    public boolean isPaused() {
        return mediaPlayer.getStatus().equals(MediaPlayer.Status.PAUSED);
    }

    public void play(int trackIndex) {
        if (mediaPlayer != null) {
            mediaPlayer.dispose();
        }
        Logger.logPlayTrack(currentTrackIndex, trackIndex,
                tracks.get(currentTrackIndex), tracks.get(trackIndex));
        currentTrackIndex = trackIndex;
        Media media = tracks.get(trackIndex).media;
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnReady(() -> {
            play();
            mediaPlayer.setOnEndOfMedia(() -> {
                if (isLast()) {
                    mainScene.controller.selectFirst();
                } else {
                    mainScene.controller.selectNext(null);
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
