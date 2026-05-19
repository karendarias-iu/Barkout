package view;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class SoundManager {

    public static void playSound(String fileName) {
        new Thread(() -> {
            try {
                InputStream is = SoundManager.class.getResourceAsStream("/sounds/" + fileName);
                if (is != null) {
                    InputStream bufferedIn = new BufferedInputStream(is);
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.start();
                }
            } catch (Exception e) {
            }
        }).start();
    }
}