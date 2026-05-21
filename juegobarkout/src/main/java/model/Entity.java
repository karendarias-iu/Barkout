package model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

/**
 * Clase abstracta entidad
 */
public abstract class Entity {
    //variables de posicion (x,y), ancho, altura y velocidad
    public int x, y, width, height, speed;

    /**
     * Constructor de Entity
     * @param x posicion en x
     * @param y posicion en y
     * @param width ancho
     * @param height alto
     * @param speed velocidad
     */
    public Entity(int x, int y, int width, int height, int speed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
    }

    /**
     * determina si el objeto colisiono con otra entidad
     * @param other entidad a verificar la colision
     * @return colisiono?
     */
    public boolean intersects(Entity other) {
        return x < other.x + other.width &&
                x + width > other.x &&
                y < other.y + other.height &&
                y + height > other.y;
    }

    /**
     * Metodo que carga la imagen
     * @param fileName nombre del archivo
     * @return
     */
    public static BufferedImage uploadImage(String fileName) {
        try {
            //la saca de la direccion de las imagenes
            InputStream is = Entity.class.getResourceAsStream("/images/" + fileName);
            if (is == null) return null;
            return ImageIO.read(is);
        } catch (Exception e) {
            return null;
        }
    }
}