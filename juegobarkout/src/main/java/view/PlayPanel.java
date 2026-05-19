package view;

import controller.GameController;
import model.Reward;
import model.Squirrel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class PlayPanel extends JPanel {
    private GameController controller;
    private Animation dogAnim;
    private Animation squirrelAnim;
    private BufferedImage ballImg;
    private BufferedImage hotDogImg;
    private BufferedImage boneImg;
    private String character;

    private final Color grassColor = new Color(34, 139, 34);
    private final Font gameFont = new Font("Courier New", Font.BOLD, 20);

    public PlayPanel(GameController controller, String character) {
        this.controller = controller;
        this.character = character;
        setPreferredSize(new Dimension(800, 600));

        this.squirrelAnim = new Animation("ardilla");
        this.ballImg = model.Entity.uploadImage("pelota.png");

        this.hotDogImg = model.Entity.uploadImage("HotDog.png");
        this.boneImg = model.Entity.uploadImage("hueso.png");
    }

    public void loadCharacter(String characterName) {
        this.character = characterName;
        this.dogAnim = new Animation("perro/" + character);
    }

    public void drawGame(Graphics2D g2d) {
        g2d.setColor(grassColor);
        g2d.fillRect(0, 0, 800, 600);

        for (int i = 0; i < controller.rewards.size(); i++) {
            Reward r = controller.rewards.get(i);
            BufferedImage img = (r.getType() == Reward.Type.BERRY) ? hotDogImg : boneImg;
            if (img != null) {
                g2d.drawImage(img, r.x, r.y, r.width, r.height, null);
            } else {
                g2d.setColor((r.getType() == Reward.Type.BERRY) ? Color.RED : Color.CYAN);
                g2d.fillOval(r.x, r.y, r.width, r.height);
            }
        }

        if (ballImg != null) {
            g2d.drawImage(ballImg, controller.ball.x, controller.ball.y,
                    controller.ball.width, controller.ball.height, null);
        }

        if (dogAnim != null) {
            BufferedImage dogFrame = dogAnim.getCurrentFrame(controller.dog.direction, controller.dog.moving);
            if (dogFrame != null) {
                g2d.drawImage(dogFrame, controller.dog.x, controller.dog.y,
                        controller.dog.width, controller.dog.height, null);

                if (controller.dog.isSuperPowered()) {
                    g2d.setColor(new Color(0, 255, 255, 90));
                    g2d.setStroke(new BasicStroke(3));
                    g2d.drawOval(controller.dog.x - 5, controller.dog.y - 5, controller.dog.width + 10, controller.dog.height + 10);
                }
            }
        }

        for (int i = 0; i < controller.squirrels.size(); i++) {
            Squirrel s = controller.squirrels.get(i);
            BufferedImage sqFrame = squirrelAnim.getCurrentFrame(s.direction, true);
            if (sqFrame != null) {
                g2d.drawImage(sqFrame, s.x, s.y, s.width, s.height, null);
            }
        }

        g2d.setColor(Color.WHITE);
        g2d.setFont(gameFont);
        g2d.drawString("Jugador: " + controller.getPlayerName(), 20, 30);
        g2d.drawString("Puntaje: " + controller.getScore(), 20, 60);
        g2d.drawString("Tiempo: " + controller.getGameTimeSeconds() + "s", 350, 30);
        g2d.drawString("Vidas: " + controller.dog.getHealth(), 650, 30);

        if (controller.isGameOver()) {
            g2d.setColor(new Color(0, 0, 0, 230));
            g2d.fillRect(0, 0, 800, 600);

            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Courier New", Font.BOLD, 45));
            dibujarCentrado(g2d, "GAME OVER", 800, 150);

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Courier New", Font.BOLD, 22));
            dibujarCentrado(g2d, "Resultados de la Partida", 800, 210);
            g2d.setFont(new Font("Courier New", Font.PLAIN, 18));
            g2d.drawString("Jugador: " + controller.getPlayerName(), 280, 250);
            g2d.drawString("Puntaje Final: " + controller.getScore() + " pts", 280, 280);
            g2d.drawString("Tiempo Jugado: " + controller.getGameTimeSeconds() + " segundos", 280, 310);

            g2d.setColor(Color.ORANGE);
            g2d.setFont(new Font("Courier New", Font.BOLD, 20));
            g2d.drawString("--- TOP 3 HISTÓRICO ---", 280, 370);

            g2d.setColor(Color.YELLOW);
            g2d.setFont(new Font("Courier New", Font.PLAIN, 18));
            List<String> top3 = controller.getTop3Ranking();
            int currentY = 400;
            for (String record : top3) {
                g2d.drawString(record, 280, currentY);
                currentY += 25;
            }

            g2d.setColor(Color.LIGHT_GRAY);
            g2d.setFont(new Font("Courier New", Font.BOLD, 16));
            dibujarCentrado(g2d, "[ Presiona 'R' para regresar al Menú Principal ]", 800, 520);
        }
    }

    private void dibujarCentrado(Graphics2D g2d, String stringText, int panelWidth, float posY) {
        FontMetrics fm = g2d.getFontMetrics();
        int posX = (panelWidth - fm.stringWidth(stringText)) / 2;
        g2d.drawString(stringText, posX, posY);
    }
}