package view;

import controller.GameController;
import controller.InputHandler;

import javax.swing.*;
import java.awt.*;

/**
 * Clase panel de juego
 */
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
    //Metodo que se llama constantemente el que se realizan acciones segun sea el caso
    public void update() {
        //si se esta en el estado de "intro"
        if (gameState.equals("intro")) {
            //se llama el metodo update de la intro
            introPanel.update();
            //si ha terminado la animacion de la intro pasa a menu cambiando el nombre de la variable gameState a "menu"
            if (introPanel.isIntroEnded()) {
                gameState = "menu";
            }
        }
        //si esta en el estado de "menu"
        if (gameState.equals("menu")) {
            //una vez seleccionado el boton de "iniciar" en el menu
            if (menuPanel.isStartButtonSelected()) {
                //se actualiza el personaje seleccionado
                character = menuPanel.getCharacter();
                //llama el metodo que despliega la ventana en donde se pone el nombre del jugador
                controller.requestPlayerName();
                //pasa a mostrar las instrucciones
                gameState = "instructions";
            }
            //seleccionado el boton de "salir" en el menu
            if (!menuPanel.isActiveMenu()) {
                //cierra la ventana y el programa
                window.dispose();
                System.exit(0);
            }
        }
        //si esta en el estado "game"
        if (gameState.equals("game")) {
            //si no se ha cargado el personaje
            if (!isCharacterLoad) {
                //carga el personaje solo una vez
                playPanel.loadCharacter(character);
                isCharacterLoad = true;
            }
            //actualiza la logica del juego (direccion, posiciones, animaciones...)
            controller.updateLogic();
        }
    }
    //Metodo para pasar a menu
    public void resetToMenu() {
        //baja el personaje seleccionado
        this.isCharacterLoad = false;
        //crea el menu
        this.menuPanel = new GameMenu();
        //quita los eventos del teclado
        for (java.awt.event.KeyListener kl : getKeyListeners()) {
            removeKeyListener(kl);
        }
        //añade nuevamente los eventos de teclado
        addKeyListener(controller);
        addKeyListener(new InputHandler(menuPanel, this));
        //cambia el estado a "menu"
        this.gameState = "menu";
    }

    /**
     * Metodo propio de JPanel para dibujar
     * @param g el elemento en donde se va a dibujar todo
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Hace uso de graphics2d para movimientos mas fluidos y que necesitan mas capacidad
        Graphics2D g2d = (Graphics2D) g;

        //segun el estado que se encuentren se llama el metodo de dibujo para el mismo en este panel
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

    /**
     * Metodo run() del hilo, es lo que se ejecuta
     */
    @Override
    public void run() {
        while (true) {
            //se llama constantemente los metodos de update para actualizar valores y repaint para lo que es el dibujo del mismo
            update();
            repaint();
            try {
                //espera de 16ms, equivalencia a 60FPS
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Metodo que devuelve el estado del juego
     * @return gameState estado del juego
     */
    public String getGameState() {
        return gameState;
    }

    /**
     * Metodo para actualizar el valor del estado del juego
     * @param gameState estado del juego
     */
    public void setGameState(String gameState) {
        this.gameState = gameState;
    }

    /**
     * Metodo que devuelve el menu del juego
     * @return menuPanel menu del juego
     */
    public GameMenu getMenuPanel() {
        return menuPanel;
    }
}