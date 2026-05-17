package view;

import javax.swing.*;
import java.awt.*;
//Clase panel en donde se manejan las pantallas del juego (intro, menu y juego)

public class GamePanel extends JPanel implements Runnable {
    //constantes de ancho y alto de pantalla
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    /**
     * variable bandera mediante la que se decide que pantalla mostrar
     *
     * "intro" -> Pantalla de introduccion (nombre juego, autores y logo)
     * "menu" -> Pantalla de menu donde se escoge a alguno de los 2 personajes (lucas o salchicha)
     * "juego" -> Pantalla ppal donde se desarrolla el juego en si
     */
    private String gameState = "intro";

    //Paneles a mostrar
    private GameIntro introPanel = new GameIntro();

    //Constructor vacio donde se define tamaño y color del panel, adicional a demas propiedades de la misma
    public GamePanel(){
        setPreferredSize(new Dimension(WIDTH,HEIGHT));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        setFocusable(true);
        //se llama a si mismo
        new Thread(this).start();
    }

    /**
     * Metodo propio de JPanel
     * se usa para hacer configuraciones de dibujo y dibujar en el mismo panel
     * @param g recibe un objeto graphics que es donde sera pintado
     */
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if(gameState=="intro"){
            introPanel.draw(g2d);
        } else if (gameState == "menu") {
            
        }
    }

    //lee cambios y actualiza valores
    public void update(){
        if(introPanel.isIntroEnded()){
            gameState = "menu";
        }
        if(gameState == "intro"){
            introPanel.update();
        }
    }

    @Override
    public void run() {
        while (true){
            repaint();
            update();
            try {
                Thread.sleep(16);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}