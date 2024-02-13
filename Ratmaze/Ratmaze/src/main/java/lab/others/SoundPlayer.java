package lab.others;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundPlayer {
    private Clip clip;
    private boolean playing;

    public SoundPlayer(String soundFilePath) {
        try {
            File soundFile = new File(soundFilePath);
            if (soundFile.exists()) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);
            } else {
                System.err.println("Audio file not found: " + soundFilePath);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error initializing audio clip: " + e.getMessage());
        }
    }

    public void play() {
        if (clip != null) {
            try {
                clip.setFramePosition(0);
                clip.start();
            } catch (Exception e) {
                System.err.println("Error playing audio: " + e.getMessage());
            }
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public void close() {
        if (clip != null) {
            clip.close();
        }
    }

    public static void main(String[] args) {
        // Example usage
        String soundFilePath = "src/main/resources/Music.wav";
        SoundPlayer soundPlayer = new SoundPlayer(soundFilePath);

        // Play the sound
        soundPlayer.play();

        // Allow some time for playback (adjust as needed)
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Stop and close the clip
        soundPlayer.stop();
        soundPlayer.close();
    }
}


