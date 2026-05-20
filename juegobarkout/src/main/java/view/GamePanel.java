package view;

import controller.GameController;
import controller.InputHandler;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    //constantes de tamaño del panel
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    /**
     * variable bandera mediante la que se decide que pantalla mostrar
     *
     * "intro" -> Pantalla de introduccion (nombre juego, autores y logo)
     * "menu" -> Pantalla de menu donde se escoge a alguno de los 2 personajes (lucas o salchicha)
     * "instructions" -> Pantalla donde se muestran las instrucciones
     * "juego" -> Pantalla ppal donde se desarrolla el juego en si
     */
    private String gameState = "intro";
    //nombre del personaje seleccionado (lucas por defecto)
    private String character = "lucas";
    //bandera para cargar el personaje seleccionado al inicio
    private boolean isCharacterLoad = false;

    //Intro a dibujar
    private GameIntro introPanel = new GameIntro();
    //Panel del juego a dibujar
    private PlayPanel playPanel;
    //Menu a dibujar
    private GameMenu menuPanel = new GameMenu();
    //controlador de eventos
    private GameController controller;
    //Instrucciones a dibujar
    private InstructionsPanel instructionsPanel = new InstructionsPanel();
    //ventana del juego
    private GameWindow window;
    //hilo principal del juego
    private Thread gameThread;

    /**
     * Constructor del GamePanel
     * @param window ventana en la que se muestra
     */
    public GamePanel(GameWindow window) {
        this.window = window;
        //configuraciones del panel
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        setFocusable(true);

        //se crea el controlador de eventos, se le pasa el panel y el menu
        //con el panel se manejan los eventos segun si esta en "menu", "instructions" o "game"
        //con el menu se maneja la selecciion del personaje
        this.controller = new GameController(this, menuPanel);

        //se establece el panel con el controlador y el personaje seleccionado
        this.playPanel = new PlayPanel(controller, character);

        //se agregan los eventos de teclado para el juego en si (flechas de teclado)
        addKeyListener(controller);
        //se agregan los eventos de teclado para el manejo de las pantallas( menu -> seleccion de personaje, enter...)
        addKeyListener(new InputHandler(menuPanel, this));

        //Se crea el hilo
        gameThread = new Thread(this);
        //se inicia la ejecucion del hilo
        gameThread.start();
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
                gameState = "instructions";
            }
            if (!menuPanel.isActiveMenu()) {
                window.dispose();
            }
        }

        if (gameState.equals("game")) {
            if (!isCharacterLoad) {
                playPanel.loadCharacter(character);
                isCharacterLoad = true;
            }
            controller.updateLogic();
        }
    }

    public void resetToMenu() {
        this.isCharacterLoad = false;
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
        } else if(gameState.equals("instructions")){
            instructionsPanel.draw(g2d);
        }else if (gameState.equals("game")) {
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

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }

    public GameMenu getMenuPanel() {
        return menuPanel;
    }
}