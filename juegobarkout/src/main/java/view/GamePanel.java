package view;

import controller.GameController;
import controller.InputHandler;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private String gameState = "intro";
    private String character = "lucas";
    private boolean isLoad = false;
    private GameIntro introPanel = new GameIntro();
    private PlayPanel playPanel;
    private GameMenu menuPanel = new GameMenu();
    private GameController controller;
    private GameWindow window;
    private Thread gameThread;

    public GamePanel(GameWindow window) {
        this.window = window;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        setFocusable(true);

        this.controller = new GameController(this, menuPanel);
        this.playPanel = new PlayPanel(controller, character);

        addKeyListener(controller);
        addKeyListener(new InputHandler(menuPanel, this));

        gameThread = new Thread(this);
        gameThread.start();
        SoundManager.playSound("durante_juego.wav");
    }

    public void update() {
        if (gameState.equals("intro")) {
            introPanel.update();
            if (introPanel.isIntroEnded()) {
                gameState = "menu";
            }
        }

        if (gameState.equals("menu")) {
            if (menuPanel.isStartButtonSelected()) {
                character = menuPanel.getCharacter();
                controller.requestPlayerName();
                gameState = "game";
            }
            if (!menuPanel.isActiveMenu()) {
                window.dispose();
            }
        }

        if (gameState.equals("game")) {
            if (!isLoad) {
                playPanel.loadCharacter(character);
                isLoad = true;
            }
            controller.updateLogic();
        }
    }

    public void resetToMenu() {
        this.isLoad = false;
        this.menuPanel = new GameMenu();

        for (java.awt.event.KeyListener kl : getKeyListeners()) {
            removeKeyListener(kl);
        }

        addKeyListener(controller);
        addKeyListener(new InputHandler(menuPanel, this));

        this.gameState = "menu";
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (gameState.equals("intro")) {
            introPanel.draw(g2d);
        } else if (gameState.equals("menu")) {
            menuPanel.draw(g2d);
        } else if (gameState.equals("game")) {
            playPanel.drawGame(g2d);
        }
    }

    @Override
    public void run() {
        while (true) {
            update();
            repaint();
            try {
                Thread.sleep(16); // ~60 FPS estables
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public String getGameState() {
        return gameState;
    }

    public GameMenu getMenuPanel() {
        return menuPanel;
    }
}