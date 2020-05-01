import javafx.scene.media.Media;
import javafx.util.Duration;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class Track {

    public File source;
    public Media media;
    public Duration duration;

    public Track(File source) {
        this.source = source;
        this.media = new Media(source.toURI().toString());
        AudioInputStream audioInputStream = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(source);
            AudioFormat format = audioInputStream.getFormat();
            int frameSize = format.getFrameSize();
            float frameRate = format.getFrameRate();
            duration = Duration.seconds(source.length() / (frameSize * frameRate));
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
