package view;

import controller.GameController;
import model.Squirrel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PlayPanel extends JPanel {
    private GameController controller;
    private Animation dogAnim;
    private Animation squirrelAnim;
    private BufferedImage ballImg;

    public PlayPanel(GameController controller) {
        this.controller = controller;
        setPreferredSize(new Dimension(800, 600));

        dogAnim = new Animation("perro/lucas");
        squirrelAnim = new Animation("ardilla");
    }

    public void drawGame(Graphics2D g2d) {

        g2d.setColor(new Color(34, 139, 34));
        g2d.fillRect(0, 0, 800, 600);

        BufferedImage dogFrame = dogAnim.getCurrentFrame(controller.dog.direction, controller.dog.moving);
        g2d.drawImage(dogFrame, controller.dog.x, controller.dog.y,
                controller.dog.width, controller.dog.height, null);

        if (ballImg != null) {
            g2d.drawImage(ballImg, controller.ball.x, controller.ball.y, 20, 20, null);
        }

        for (Squirrel s : controller.squirrels) {
            BufferedImage sqFrame = squirrelAnim.getCurrentFrame(s.direction, true);
            g2d.drawImage(sqFrame, s.x, s.y, s.width, s.height, null);
        }

        g2d.setColor(Color.WHITE);
        g2d.drawString("Puntaje: " + controller.getScore(), 20, 30);
    }
}