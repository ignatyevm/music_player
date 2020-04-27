import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class InitialScene extends Scene {

    private InitialScene(Group root) {
        super(root, MusicPlayerMain.WINDOW_WIDTH, MusicPlayerMain.WINDOW_HEIGHT);

        Button openFilesButton = new Button("Open file(-s)");
        Button openFolderButton = new Button("Open folder");

        openFilesButton.setOnAction(this::onOpenFiles);
        openFolderButton.setOnAction(this::onOpenFolder);

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

    private void onOpenFiles(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file(-s)");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio files", "*.wav"));
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(MusicPlayerMain.getStage());
        if (selectedFiles != null) {
            MainScene.show(selectedFiles);
        }
    }

    private void onOpenFolder(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select folder");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File selectedFolder = directoryChooser.showDialog(MusicPlayerMain.getStage());
        if (selectedFolder != null) {
            File[] filteredFiles = selectedFolder.listFiles(file -> file.getName().endsWith(".wav"));
            if (filteredFiles != null) {
                MainScene.show(Arrays.asList(filteredFiles));
            }
        }
    }

    static void show() {
        Group root = new Group();
        MusicPlayerMain.getStage().setScene(new InitialScene(root));
        MusicPlayerMain.getStage().show();
    }

}
