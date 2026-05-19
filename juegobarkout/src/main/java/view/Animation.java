package view;

import model.Entity;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Animation {

    private static final int UP = 0;
    private static final int DOWN = 1;
    private static final int DER = 2;
    private static final int IZQ = 3;

    private BufferedImage[][] allAnimations = new BufferedImage[4][];
    private int frameIndex = 0;
    private long lastFrameTime = 0;
    private int animationDelay = 150;

    public Animation(String folderPath) {
        allAnimations[UP] = loadFrames(folderPath + "/up", 3);
        allAnimations[DOWN] = loadFrames(folderPath + "/down", 3);
        allAnimations[DER] = loadFrames(folderPath + "/der", 4);
        allAnimations[IZQ] = loadFrames(folderPath + "/izq", 4);
    }

    private BufferedImage[] loadFrames(String prefix, int maxFrames) {
        List<BufferedImage> frameList = new ArrayList<>();
        for (int i = 1; i <= maxFrames; i++) {
            BufferedImage img = Entity.uploadImage(prefix + i + ".png");
            if (img != null) {
                frameList.add(img);
            } else {
                break;
            }
        }
        return frameList.toArray(new BufferedImage[0]);
    }

    public BufferedImage getCurrentFrame(String direction, boolean isMoving) {
        int dirIndex = getDirectionIndex(direction);
        BufferedImage[] frames = allAnimations[dirIndex];


        if (frames == null || frames.length == 0) return null;

        if (!isMoving) {
            frameIndex = 0;
            return frames[0];
        }

        long now = System.currentTimeMillis();
        if (now - lastFrameTime > animationDelay) {
            frameIndex++;
            lastFrameTime = now;
        }


        int safeIndex = frameIndex % frames.length;

        return frames[safeIndex];
    }

    private int getDirectionIndex(String direction) {
        if (direction == null) return DOWN;
        switch (direction.toLowerCase()) {
            case "arriba":    return UP;
            case "derecha":   return DER;
            case "izquierda": return IZQ;
            case "abajo":
            default:          return DOWN;
        }
    }
}