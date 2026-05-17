package view;

import model.Entity;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Animation {
    private Map<String, BufferedImage[]> animations = new HashMap<>();
    private int frameIndex = 0;
    private long lastFrameTime = 0;
    private int animationDelay = 150;

    public Animation(String folderPath) {
        animations.put("arriba", loadFrames(folderPath + "/up"));
        animations.put("abajo", loadFrames(folderPath + "/down"));
        animations.put("derecha", loadFrames(folderPath + "/der"));
        animations.put("izquierda", loadFrames(folderPath + "/izq"));
    }

    private BufferedImage[] loadFrames(String prefix) {
        List<BufferedImage> frameList = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
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
        BufferedImage[] frames = animations.get(direction);

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
}