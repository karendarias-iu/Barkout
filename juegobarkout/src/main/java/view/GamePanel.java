package view;

import controller.GameController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel implements Runnable {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private String gameState = "intro";

    private GameIntro introPanel = new GameIntro();
    private PlayPanel playPanel;
    private GameController controller = new GameController();

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        setFocusable(true);

        playPanel = new PlayPanel(controller);
        addKeyListener(controller);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (gameState.equals("menu") && e.getKeyCode() == KeyEvent.VK_ENTER) {
                    gameState = "juego";
                }
            }
        });

        new Thread(this).start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (gameState.equals("intro")) {
            introPanel.draw(g2d);
        } else if (gameState.equals("menu")) {
            drawMenu(g2d);
        } else if (gameState.equals("juego")) {
            playPanel.drawGame(g2d);
        }
    }

    private void drawMenu(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Courier New", Font.BOLD, 40));
        g2d.drawString("BARKOUT - MENU", 240, 200);
        g2d.setFont(new Font("Courier New", Font.BOLD, 20));
        g2d.drawString("Presiona ENTER para Empezar a Jugar", 180, 350);
    }

    public void update() {
        if (introPanel.isIntroEnded() && gameState.equals("intro")) {
            gameState = "menu";
        }

        if (gameState.equals("intro")) {
            introPanel.update();
        } else if (gameState.equals("juego")) {
            controller.updateLogic();
        }
    }

    @Override
    public void run() {
        while (true) {
            update();
            repaint();
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}