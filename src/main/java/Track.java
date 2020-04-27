import javafx.scene.media.Media;

import java.io.File;

public class Track {

    public File source;
    public Media media;

    public Track(File source) {
        this.source = source;
        this.media = new Media(source.toURI().toString());
    }

}
