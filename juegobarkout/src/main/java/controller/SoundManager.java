package controller;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class SoundManager {

    // se guarda el sonido
    private static Clip backgroundClip;

    /**
     * Metodo para reproducir un sonido
     * @param fileName nombre del archivo
     */
    public static void playSound(String fileName) {
        //crea un hilo para reproducir sonido
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
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Metodo para reproducir musica en bucle
     * @param fileName nombre del archivo
     */
    public static void playLoop(String fileName) {
        new Thread(() -> {
            try {
                // Si ya había una música sonando, la detenemos antes de empezar otra
                stopLoop();

                InputStream is = SoundManager.class.getResourceAsStream("/sounds/" + fileName);
                if (is != null) {
                    InputStream bufferedIn = new BufferedInputStream(is);
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
                    backgroundClip = AudioSystem.getClip();
                    backgroundClip.open(audioStream);
                    backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
                    backgroundClip.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Metodo para detener los sonidos en loop
     */
    public static void stopLoop() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
            backgroundClip.close();
        }
    }
}