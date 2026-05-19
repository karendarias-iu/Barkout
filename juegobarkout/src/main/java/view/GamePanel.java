package view;

import controller.GameController;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private String gameState = "intro";

    //personaje
    private String character = "lucas";
    private boolean cargado = false;
    private GameIntro introPanel = new GameIntro();
    private PlayPanel playPanel;
    private GameMenu menuPanel = new GameMenu();
    private GameController controller = new GameController(this, menuPanel);
    private GameWindow window;

    public GamePanel(GameWindow window) {
        this.window = window;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        setFocusable(true);
        playPanel = new PlayPanel(controller,character);
        addKeyListener(controller);
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
            menuPanel.draw(g2d);
        }
        if (gameState.equals("game")) {
            if(!cargado){
                playPanel.loadCharacter(character);
                cargado = true;
            }

            playPanel.drawGame(g2d);
        }
    }

    public GameMenu getMenuPanel() {
        return menuPanel;
    }

    public String getGameState() {
        return gameState;
    }


    public void update(){
        if(introPanel.isIntroEnded()){
            gameState = "menu";
        }
        if(gameState == "intro"){
            introPanel.update();
        }
        if(!menuPanel.isActiveMenu()){
            window.dispose();
        }
        if(menuPanel.isStartButtonSelected()){
            character = menuPanel.getCharacter();
            gameState = "game";
        }
        if(gameState.equals("game")){
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