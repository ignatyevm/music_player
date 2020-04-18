import javafx.application.Application;
import javafx.stage.Stage;

public class MusicPlayerMain extends Application {

    public static final int WINDOW_HEIGHT = 500;
    public static final int WINDOW_WIDTH = 400;

    public void start(Stage stage) throws Exception {
        stage.setTitle("Music Player");
        stage.centerOnScreen();
        InitialScene.show(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
