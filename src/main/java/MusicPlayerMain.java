import javafx.application.Application;
import javafx.stage.Stage;

public class MusicPlayerMain extends Application {

    public static final int WINDOW_HEIGHT = 500;
    public static final int WINDOW_WIDTH = 400;

    public static Stage stage;

    public void start(Stage stage) throws Exception {
        MusicPlayerMain.stage = stage;
        stage.setTitle("Music Player");
        stage.centerOnScreen();
        InitialScene.show(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
