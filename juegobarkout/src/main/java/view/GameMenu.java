package view;

import model.Entity;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Clase menu
 */
public class GameMenu {
    //posicion texto de seleccion de personaje en y
    private int posSelectionTitleY = 50;
    //posicion de las imagenes de los personajes a seleccionar en y
    private int posImagesY = 80;
    //posicion en x de la imagen de los personajes
    private int posLucasImageX = 125;
    private int posSalchichaImageX = 50 + posLucasImageX + 250;
    //Imagenes de los personajes
    private BufferedImage imageMenuLucas;
    private BufferedImage imageMenuSalchicha;
    //nombre del personaje (lucas por defecto al inicio)
    private String character = "lucas";
    //bandera para determinar si el menu sigue activo (no se ha cerrado)
    private boolean activeMenu = true;
    //bandera para determinar si se ha dado click al boton de iniciar el juego
    private boolean startButtonSelected = false;
    //posiciones del cuadro de seleccion del personaje en x,y
    private int posSelectionSquareX = posLucasImageX - 10;
    private int posSelectionSquareY = posImagesY - 10;
    //color del cuadro de seleccion con un poco de transparencia
    private Color colorSquare = new Color(100,100,100,127);

    //Constructor por defecto
    public GameMenu() {
        //se cargan las imagenes de los personajes para el menu
        imageMenuLucas = Entity.uploadImage("perro/lucas/menu.png");
        imageMenuSalchicha = Entity.uploadImage("perro/salchicha/menu.png");
    }

    /**
     * Metodo para dibujar el menu
     * @param g2d recibe el elemento en el cual se va a dibujar
     */
    public void draw(Graphics2D g2d) {
        //Configuracion del panel donde se dibuja
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //color y fuente de texto
        g2d.setColor(Color.white);
        g2d.setFont(new Font("Courier New", Font.BOLD, 50));
        //dibujar texto de seleccione el personaje
        drawCenterText(g2d, "Seleccione el personaje", 800, posSelectionTitleY);
        //dibuba las imagenes de los personajes
        if (imageMenuLucas != null && imageMenuSalchicha != null) {
            g2d.drawImage(imageMenuLucas, posLucasImageX, posImagesY, null);
            g2d.drawImage(imageMenuSalchicha, posSalchichaImageX, posImagesY, null);
        }
        //dibuja el cuadro de seleccion en el personaje seleccionado
        g2d.setColor(colorSquare);
        g2d.fillRect(posSelectionSquareX, posSelectionSquareY, 270, 270);
        g2d.setFont(new Font("Courier New", Font.BOLD, 30));
        //dibuja la especificacion al momento de seleccionar el personaje
        g2d.drawString("tecla (1)", posLucasImageX + 30, posImagesY + 285);
        g2d.drawString("tecla (2)", posSalchichaImageX + 30, posImagesY + 285);
        //dibuja el boton de iniciar juego
        g2d.fillRect(150, 400, 500, 60);
        g2d.setColor(Color.gray);
        g2d.fillRect(155, 405, 490, 50);
        g2d.setColor(Color.WHITE);
        drawCenterText(g2d, "Iniciar", 800, 440);
        //dibuja el boton de salir del juego
        g2d.fillRect(150, 500, 500, 60);
        g2d.setColor(Color.gray);
        g2d.fillRect(155, 505, 490, 50);
        g2d.setColor(Color.WHITE);
        drawCenterText(g2d, "Salir", 800, 540);
        g2d.setFont(new Font("Courier New", Font.BOLD, 20));
        //dibuja especificaciones de seleccion de los botones
        g2d.drawString("(Enter)", 670, 440);
        g2d.drawString("(Esc)", 670, 540);
    }

    /**
     * Funcion para dibujar texto centrado
     * @param g2d elemento donde se va a dibujar
     * @param stringText texto a dibujar
     * @param panelWidth ancho del panel
     * @param posY posicion en y donde se dibuja
     */
    private void drawCenterText(Graphics2D g2d, String stringText, int panelWidth, float posY) {
        FontMetrics fm = g2d.getFontMetrics();
        int posX = (panelWidth - fm.stringWidth(stringText)) / 2;
        g2d.drawString(stringText, posX, posY);
    }

    //metodo cuando se selecciona lucas
    public void selectLucas() {
        //posicion del cuadro de seleccion para lucas
        posSelectionSquareX = posLucasImageX - 10;
        character = "lucas";
    }

    //metodo cuando se selecciona salchicha
    public void selectSalchicha() {
        //posicion del cuadro de seleccion para salchicha
        posSelectionSquareX = posSalchichaImageX - 10;
        character = "salchicha";
    }

    //metodo cuando se sale desde el menu
    public void close() {
        //se cambia el estado activo del menu
        activeMenu = false;
    }

    //metodo cuando se le da al boton de iniciar
    public void startGame() {
        //se indica que se ha seleccionado
        startButtonSelected = true;
    }

    /**
     * getter del estado de seleccion de boton de inicio
     * @return startButtonSelected estado seleccion del boton
     */
    public boolean isStartButtonSelected() {
        return startButtonSelected;
    }

    /**
     * getter del estado del menu
     * @return activeMenu estado ativo del menu
     */
    public boolean isActiveMenu() {
        return activeMenu;
    }

    /**
     * getter de nombre del personaje seleccionado
     * @return character nombre del personaje seleccionado
     */
    public String getCharacter() {
        return character;
    }
}
