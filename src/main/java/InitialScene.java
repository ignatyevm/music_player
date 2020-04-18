import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class InitialScene extends Scene {

    private InitialScene(Stage stage, Group root) {
        super(root, MusicPlayerMain.WINDOW_WIDTH, MusicPlayerMain.WINDOW_HEIGHT);

        Button openFilesButton = new Button("Open file(-s)");
        Button openFolderButton = new Button("Open folder");

        openFilesButton.setOnAction(Controller::onOpenFiles);
        openFolderButton.setOnAction(Controller::onOpenFolder);

        HBox buttonsBox = new HBox(openFilesButton, new Label("or"), openFolderButton);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setSpacing(10);

        Label helloLabel = new Label("Music Player");
        helloLabel.setFont(new Font(20));

        VBox mainContainer = new VBox(helloLabel, buttonsBox);
        mainContainer.setPrefSize(getWidth(), getHeight());
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setSpacing(20);

        BorderPane borderPane = new BorderPane(mainContainer);
        borderPane.setPrefSize(getWidth(), getHeight());

        root.getChildren().addAll(borderPane);
    }

    static void show(Stage stage) {
        Group root = new Group();
        stage.setScene(new InitialScene(stage, root));
        stage.show();
    }

}
