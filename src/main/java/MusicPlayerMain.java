import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;

public class MusicPlayerMain extends Application {

    public static final int WINDOW_HEIGHT = 400;
    public static final int WINDOW_WIDTH = 500;

    private static Stage stage;

    public void start(Stage stage) {
        MusicPlayerMain.stage = stage;
        stage.setTitle("Music Player");
        stage.centerOnScreen();
        stage.setResizable(false);
        boolean debug = true;
        if (debug) {
            File testDir = new File(System.getProperty("user.home") + "/_mp_test/");
            File[] files = testDir.listFiles(file -> file.getName().endsWith(".wav"));
            MainScene.show(Arrays.asList(files));
        } else {
            InitialScene.show();
        }
    }

    public static Stage getStage() {
        return stage;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
