public class Logger {

    private static void log(String format, Object ...args) {
        System.out.println(String.format(format, args));
        System.out.println("====================================================");
    }

    public static void logPlayTrack(int oldTrackIndex,int newTrackIndex, Track oldTrack, Track newTrack) {
        log("[MediaPlayer][%d -> %d] %s -> %s", oldTrackIndex, newTrackIndex,
                oldTrack.source.getName(), newTrack.source.getName());
    }

}
