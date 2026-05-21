package view;

import model.Entity;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase animacion
 */
public class Animation {
    //direcciones del personaje
    private static final int UP = 0;
    private static final int DOWN = 1;
    private static final int DER = 2;
    private static final int IZQ = 3;

    //matriz de imagenes de las animaciones
    private BufferedImage[][] allAnimations = new BufferedImage[4][];
    //variables de indice, ultimo frame, tiempo entre frame
    private int frameIndex = 0;
    private long lastFrameTime = 0;
    private int animationDelay = 150;

    /**
     * Constructor
     * @param folderPath direccion de la carpeta donde estan las imagenes
     */
    public Animation(String folderPath) {
        //llena la matriz de imagenes segun las direcciones desde la carpeta
        allAnimations[UP] = loadFrames(folderPath + "/up", 3);
        allAnimations[DOWN] = loadFrames(folderPath + "/down", 3);
        allAnimations[DER] = loadFrames(folderPath + "/der", 4);
        allAnimations[IZQ] = loadFrames(folderPath + "/izq", 4);
    }

    /**
     * Metodo que devulve un arreglo de imagenes
     * @param prefix prefijo del frame
     * @param maxFrames cantidad de frames por direccion
     * @return arreglo de imagenes
     */
    private BufferedImage[] loadFrames(String prefix, int maxFrames) {
        //lista de frames
        List<BufferedImage> frameList = new ArrayList<>();
        //añade cada imagen a la lista
        for (int i = 1; i <= maxFrames; i++) {
            BufferedImage img = Entity.uploadImage(prefix + i + ".png");
            if (img != null) {
                frameList.add(img);
            } else {
                break;
            }
        }
        //devuelve la lista
        return frameList.toArray(new BufferedImage[0]);
    }

    /**
     * Metodo que retorna el frame actual
     * @param direction direccion del personaje
     * @param isMoving bandera que determina si se esta o no moviendo
     * @return frame actual
     */
    public BufferedImage getCurrentFrame(String direction, boolean isMoving) {
        //direccion del personaje (up=0,down=1,izq=2,der=3)
        int dirIndex = getDirectionIndex(direction);
        //lista de los frames en la matriz segun la direccion en la que va
        BufferedImage[] frames = allAnimations[dirIndex];

        if (frames == null || frames.length == 0) return null;

        //si no se mueve devuelve el frame de estado quieto por defecto
        if (!isMoving) {
            frameIndex = 0;
            return frames[0];
        }
        //actualiza el tiempo transcurrido entre frames
        long now = System.currentTimeMillis();
        //si cumple el tiempo
        if (now - lastFrameTime > animationDelay) {
            //aumenta el frame y actualiza el tiempo
            frameIndex++;
            lastFrameTime = now;
        }
        //en caso de pasarse del frame se vuelve a reiniciar desde el frame 0
        int safeIndex = frameIndex % frames.length;
        //devuelve el frame
        return frames[safeIndex];
    }

    /**
     * devuelve un numero entre 0 y 3 segun la direccion del personaje
     * @param direction direccion del personaje
     * @return numero entre 0 y 3
     */
    private int getDirectionIndex(String direction) {
        if (direction == null) return DOWN;
        //devuelve la direccion segun los casos
        switch (direction.toLowerCase()) {
            case "arriba":
                return UP;
            case "derecha":
                return DER;
            case "izquierda":
                return IZQ;
            case "abajo":
            default:
                return DOWN;
        }
    }
}