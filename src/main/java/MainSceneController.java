import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;

public class MainSceneController {

    private MainScene mainScene;

    public MainSceneController(MainScene mainScene) {
        this.mainScene = mainScene;
    }

    public void onTrackSelect(ObservableValue<? extends Track> obs, Track oldTrack, Track newTrack) {
        mainScene.pauseButton.setText("pause");
        if (mainScene.player.isFirst()) mainScene.prevButton.setDisable(false);
        if (mainScene.player.isLast()) mainScene.nextButton.setDisable(false);
        mainScene.player.play(mainScene.trackTable.getSelectionModel().getSelectedIndex());
        if (mainScene.player.isFirst()) mainScene.prevButton.setDisable(true);
        if (mainScene.player.isLast()) mainScene.nextButton.setDisable(true);
    }

    public void selectFirst() {
        mainScene.trackTable.getSelectionModel().selectFirst();
    }

    public void selectPrev(ActionEvent event) {
        mainScene.trackTable.getSelectionModel().selectPrevious();
    }

    public void selectNext(ActionEvent event) {
        mainScene.trackTable.getSelectionModel().selectNext();
    }

    public void onPause(ActionEvent event) {
        if (mainScene.player.isPaused()) {
            mainScene.player.play();
            mainScene.pauseButton.setText("pause");
        } else {
            mainScene.player.pause();
            mainScene.pauseButton.setText("continue");
        }
    }

}
