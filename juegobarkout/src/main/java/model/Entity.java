package model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class Entity {
    public Entity(){

    }

    /**
     * Metodo para cargar las imagenes
     * @param fileName nombre del archivo "imagen.jpg"...
     * @return imagen
     */
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
