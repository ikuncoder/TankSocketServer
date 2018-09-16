package wingman;


import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.sound.sampled.*;
import java.io.IOException;

/* Game sounds are played using JLayer library*/
public class GameSounds {
    public static final GameSounds sounds = new GameSounds();
    private javafx.embed.swing.JFXPanel jxp = new javafx.embed.swing.JFXPanel(); // forces JavaFX init

    public static void playLoop(String filename) {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(WingmanWorld.class.getResource(filename)));
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception exc) {
            exc.printStackTrace(System.out);
        }
    }

    public static void playClip(String filename) {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(WingmanWorld.class.getResource(filename)));
            clip.loop(0);
        } catch (Exception exc) {
            exc.printStackTrace(System.out);
        }
    }

    public static void play(final String filename) {
        try {
            // Play now.
            new Thread() {
                public void run() {
                    try {
                        AudioInputStream in = AudioSystem.getAudioInputStream(WingmanWorld.class.getResource(filename));
                        AudioInputStream din = null;
                        AudioFormat baseFormat = in.getFormat();
                        AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                                baseFormat.getSampleRate(),
                                16,
                                baseFormat.getChannels(),
                                baseFormat.getChannels() * 2,
                                baseFormat.getSampleRate(),
                                false);
                        din = AudioSystem.getAudioInputStream(decodedFormat, in);
                        rawplay(decodedFormat, din);
                        in.close();
                    } catch (Exception e) {
                        System.out.println("Error playing " + filename);
                        e.printStackTrace();
                    }
                }
            }.start();
        } catch (Exception e) {
            System.out.println("Error playing " + filename);
            e.printStackTrace();
        }
    }

    public static void playmp3(final String filename) {
        try {
            // Play now.
            new Thread() {
                public void run() {
                    try {
                        MediaPlayer player = new MediaPlayer(new Media(WingmanWorld.class.getResource(filename).toString()));
                        player.play();

                    } catch (Exception e) {
                        System.out.println("Error playing " + filename);
                        e.printStackTrace();
                    }
                }
            }.start();
        } catch (Exception e) {
            System.out.println("Error playing " + filename);
            e.printStackTrace();
        }
    }

    private static void rawplay(AudioFormat targetFormat, AudioInputStream din) throws IOException, LineUnavailableException {
        byte[] data = new byte[4096];
        SourceDataLine line = getLine(targetFormat);
        if (line != null) {
            // Start
            line.start();
            int nBytesRead = 0, nBytesWritten = 0;
            while (nBytesRead != -1) {
                nBytesRead = din.read(data, 0, data.length);
                if (nBytesRead != -1) nBytesWritten = line.write(data, 0, nBytesRead);
            }
            // Stop
            line.drain();
            line.stop();
            line.close();
            din.close();
        }
    }

    private static SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException {
        SourceDataLine res = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        res = (SourceDataLine) AudioSystem.getLine(info);
        res.open(audioFormat);
        return res;
    }
}