package view;

import model.Entity;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Clase instrucciones
 */
public class InstructionsPanel {
    //Imagen de fondo de las instrucciones
    private BufferedImage instructions;

    //Constructor por defecto
    public InstructionsPanel(){
        //carga la imagen de las instrucciones
        instructions = Entity.uploadImage("instructions.jpg");
    }

    /**
     * Metodo para dibujar el menu
     * @param g2d recibe el elemento en el cual se va a dibujar
     */
    public void draw(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //dibuja las imagen con las instrucciones
        if(instructions!=null){
            g2d.drawImage(instructions,0,0,null);
        }
    }
}
