package model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public abstract class Entity {

    public int x, y;
    public int width, height;
    public int speed;

    public Entity(int x, int y, int width, int height, int speed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }


    public static BufferedImage uploadImage(String fileName) {
        try {
            InputStream is = Entity.class.getResourceAsStream("/images/" + fileName);
            if (is == null) {
                System.err.println("Imagen no encontrada: " + fileName);
                return null;
            }
            return ImageIO.read(is);
        } catch (Exception e) {
            System.err.println("Error cargando imagen: " + fileName);
            return null;
        }
    }
}