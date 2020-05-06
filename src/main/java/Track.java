import javafx.scene.media.Media;
import javafx.util.Duration;
import org.tritonus.share.sampled.file.TAudioFileFormat;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Track {

    public enum Format {
        MP3, WAV
    }

    public File source;
    public Media media;
    public Duration duration;
    public Format format;

    public Track(File source) {
        this.source = source;
        this.media = new Media(source.toURI().toString());
        AudioFileFormat fileFormat = null;
        try {
            fileFormat = AudioSystem.getAudioFileFormat(source);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (source.getName().endsWith(".mp3")) {
            format = Format.MP3;
            duration = Duration.millis(((Long) fileFormat.properties().get("duration")) / 1000);
        } else {
            format = Format.WAV;
            int frameSize = fileFormat.getFormat().getFrameSize();
            float frameRate = fileFormat.getFormat().getFrameRate();
            duration = Duration.seconds(source.length() / (frameSize * frameRate));
        }
    }

}
