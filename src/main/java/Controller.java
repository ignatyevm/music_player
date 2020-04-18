import javafx.event.ActionEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Controller {

    public static void onOpenFiles(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file(-s)");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio files", "*.wav"));
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(MusicPlayerMain.stage);
        if (selectedFiles != null) {

        }
    }

    public static void onOpenFolder(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select folder");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File selectedFolder = directoryChooser.showDialog(MusicPlayerMain.stage);
        if (selectedFolder != null) {
            File[] filteredFiles = selectedFolder.listFiles(file -> file.getName().endsWith(".wav"));
            if (filteredFiles != null) {

            }
        }
    }

}
