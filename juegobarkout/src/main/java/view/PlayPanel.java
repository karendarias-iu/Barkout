package view;

import controller.GameController;
import model.Entity;
import model.Reward;
import model.Squirrel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
//Clase panel
public class PlayPanel extends JPanel {
    //controlador de eventos de teclado y el juego en si
    private GameController controller;
    //animacion para el personaje(perro) y las ardillas
    private Animation dogAnim;
    private Animation squirrelAnim;
    //imagenes del fondo, pelota, hotDog y hueso
    private BufferedImage background;
    private BufferedImage ballImg;
    private BufferedImage hotDogImg;
    private BufferedImage boneImg;
    //nombre del personaje
    private String character;
    //fuente de texto
    private final Font gameFont = new Font("Courier New", Font.BOLD, 20);

    /**
     * Constructor
     * @param controller se le pasa el controlador
     * @param character se le pasa el nombre del personaje seleccionado
     */
    public PlayPanel(GameController controller, String character) {
        //se dan los valores y se define el tamaño del panel
        this.controller = controller;
        this.character = character;
        setPreferredSize(new Dimension(800, 600));
        //se cargan las imagenes
        this.squirrelAnim = new Animation("ardilla");
        this.ballImg = model.Entity.uploadImage("items/ball.png");
        this.hotDogImg = model.Entity.uploadImage("items/hotDog.png");
        this.boneImg = model.Entity.uploadImage("items/bone.png");
        this.background = Entity.uploadImage("background.png");
    }
    //metodo para cargar el personaje seleccionado
    public void loadCharacter(String characterName) {
        this.character = characterName;
        //se crea la animacion para el personaie
        this.dogAnim = new Animation("perro/" + character);
    }
    /**
     * Metodo para dibujar el panel del juego
     * @param g2d recibe el elemento en el cual se va a dibujar
     */
    public void drawGame(Graphics2D g2d) {
        g2d.drawImage(background, 0, 0, null);
        //recorre la lista de las recompensas y dibuja la imagen de las mismas en las coordenadas que correspondan
        for (int i = 0; i < controller.rewards.size(); i++) {
            //recomepensa en la posicion i de la lista
            Reward r = controller.rewards.get(i);
            //imagen de la recompensa segun la posicion i
            BufferedImage img = (r.getType() == Reward.Type.HOT_DOG) ? hotDogImg : boneImg;
            if (img != null) {
                //se dibuja la imagen de la recompensa
                g2d.drawImage(img, r.x, r.y, r.width, r.height, null);
            }
        }
        //se dibuja la pelota en la posicion designada
        if (ballImg != null) {
            g2d.drawImage(ballImg, controller.ball.x, controller.ball.y,
                    controller.ball.width, controller.ball.height, null);
        }
        //se dibuja el frame del perro segun la direccion y posicion
        if (dogAnim != null) {
            //frame actual
            BufferedImage dogFrame = dogAnim.getCurrentFrame(controller.dog.direction, controller.dog.moving);
            if (dogFrame != null) {
                //se dibuja el frame
                g2d.drawImage(dogFrame, controller.dog.x, controller.dog.y,
                        controller.dog.width, controller.dog.height, null);
                // si tiene el power up (cogio el hueso)
                if (controller.dog.isSuperPowered()) {
                    //dibuja un circulo azul a su alrededor como efecto de velocidad
                    g2d.setColor(new Color(0, 255, 255, 90));
                    g2d.setStroke(new BasicStroke(3));
                    g2d.drawOval(controller.dog.x - 5, controller.dog.y - 5, controller.dog.width + 10, controller.dog.height + 10);
                }
            }
        }
        //se usa syncronized para que lo haga ardilla a ardilla en la lista
        synchronized (controller.squirrels) {
            //recorre la lista de ardillas
            for (int i = 0; i < controller.squirrels.size(); i++) {
                Squirrel s = controller.squirrels.get(i);
                //actualiza la direccion de la ardilla
                String animDirection = "izquierda";
                if (s.direction != null && s.direction.equals("derecha")) {
                    animDirection = "derecha";
                }
                //imagen del frame de la ardilla
                BufferedImage sqFrame = squirrelAnim.getCurrentFrame(animDirection, true);
                if (sqFrame != null) {
                    //dibuja el frame
                    g2d.drawImage(sqFrame, s.x, s.y, s.width, s.height, null);
                }
            }
        }
        //dibuja puntaje, tiempo, nombre del jugador y las vidas actuales
        g2d.setColor(Color.WHITE);
        g2d.setFont(gameFont);
        g2d.drawString("Jugador: " + controller.getPlayerName(), 20, 30);
        g2d.drawString("Puntaje: " + controller.getScore(), 20, 60);
        g2d.drawString("Tiempo: " + controller.getGameTimeSeconds() + "s", 350, 30);
        g2d.drawString("Vidas: " + controller.dog.getHealth(), 650, 30);
        //pantalla de gameOver
        if (controller.isGameOver()) {
            g2d.setColor(new Color(0, 0, 0, 230));
            g2d.fillRect(0, 0, 800, 600);

            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Courier New", Font.BOLD, 45));
            drawCenterText(g2d, "GAME OVER", 800, 150);
            //informacion de la partida
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Courier New", Font.BOLD, 22));
            drawCenterText(g2d, "Resultados de la Partida", 800, 210);
            g2d.setFont(new Font("Courier New", Font.PLAIN, 18));
            g2d.drawString("Jugador: " + controller.getPlayerName(), 280, 250);
            g2d.drawString("Puntaje Final: " + controller.getScore() + " pts", 280, 280);
            g2d.drawString("Tiempo Jugado: " + controller.getGameTimeSeconds() + " segundos", 280, 310);
            //top 3 jugadores
            g2d.setColor(Color.ORANGE);
            g2d.setFont(new Font("Courier New", Font.BOLD, 20));
            g2d.drawString("--- TOP 3 HISTÓRICO ---", 280, 370);

            g2d.setColor(Color.YELLOW);
            g2d.setFont(new Font("Courier New", Font.PLAIN, 18));
            //lista de los top 3
            List<String> top3 = controller.getTop3Ranking();
            int currentY = 400;
            for (String record : top3) {
                g2d.drawString(record, 280, currentY);
                currentY += 25;
            }

            g2d.setColor(Color.LIGHT_GRAY);
            g2d.setFont(new Font("Courier New", Font.BOLD, 16));
            drawCenterText(g2d, "[ Presiona 'R' para regresar al Menú Principal ]", 800, 520);
        }
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
}