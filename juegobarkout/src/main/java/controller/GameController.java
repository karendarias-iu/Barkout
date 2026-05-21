package controller;

import model.Ball;
import model.Dog;
import model.Reward;
import model.Squirrel;
import view.GameMenu;
import view.GamePanel;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Clase controlador
 */
public class GameController extends KeyAdapter {
    //personaje
    public Dog dog;
    //pelota
    public Ball ball;
    //lista de ardillas
    public List<Squirrel> squirrels;
    //lista de recompensas
    public List<Reward> rewards;

    //banderas de direccion
    private boolean up, down, left, right;
    //objeto para generar posiciones aleatorias
    private Random random = new Random();
    //tiempos de spawneo
    private long lastSpawnTime = 0;
    private long lastRewardSpawnTime = 0;
    //puntaje
    private int score = 0;
    //estado del juego
    private boolean gameOver = false;
    //nombre por defecto del jugador
    private String playerName = "Invitado";
    //tiempos
    private int gameTimeSeconds = 0;
    private Timer gameTimer;
    private long powerUpEndTime = 0;
    //panel de juego
    private GamePanel panel;
    //juego
    private GameMenu menu;

    /**
     * Contructor
     * @param panel panel de juego
     * @param menu menu del juego
     */
    public GameController(GamePanel panel, GameMenu menu) {
        this.panel = panel;
        this.menu = menu;
        //inicia el contador
        initTimer();
        //declara valores iniciales
        restartGame();
    }

    /**
     * contador del juego
     */
    private void initTimer() {
        //ejecuta la accion cada segundo
        gameTimer = new Timer(1000, e -> {
            if (!gameOver && panel.getGameState().equals("game")) {
                //aumenta segundos
                gameTimeSeconds++;
                //si el tiempo de power up termina se desactiva
                if (dog.isSuperPowered() && System.currentTimeMillis() > powerUpEndTime) {
                    dog.setSuperPowered(false);
                }
            }
        });
        //se inicia el contador
        gameTimer.start();
    }

    /**
     * Metodo que despliega una ventana que pide el nombre de jugador
     */
    public void requestPlayerName() {
        String input = javax.swing.JOptionPane.showInputDialog(panel, "Ingrese su nombre de jugador:", "Registro de Jugador", javax.swing.JOptionPane.PLAIN_MESSAGE);
        //guarda el nombre, si no lo ingresa se declara anonimo
        if (input != null && !input.trim().isEmpty()) {
            this.playerName = input.trim();
        } else {
            this.playerName = "Anonimo";
        }
    }

    /**
     * reinicia el juego, pone los valores iniciales
     */
    public void restartGame() {
        dog = new Dog(380, 450);
        ball = new Ball(392, 292);
        squirrels = new ArrayList<>();
        rewards = new ArrayList<>();
        score = 0;
        gameTimeSeconds = 0;
        gameOver = false;
        lastSpawnTime = System.currentTimeMillis();
        lastRewardSpawnTime = System.currentTimeMillis();
    }

    /**
     * logica principal del juego
     */
    public void updateLogic() {
        // si se pierde no hace nada
        if (gameOver) return;
        //direccion en x,y
        int dx = 0, dy = 0;
        if (up) dy--;
        if (down) dy++;
        if (left) dx--;
        if (right) dx++;
        //mueve el perro segun la direccion
        dog.move(dx, dy);
        //tiempo transcurrido
        long currentTime = System.currentTimeMillis();
        //cada 2 segundos y medio sale una ardilla
        if (currentTime - lastSpawnTime > 2500) {
            spawnSquirrel();
            //se actualiza el tiempo de spawneo
            lastSpawnTime = currentTime;
        }
        //cada 4 segundos una recompensa
        if (currentTime - lastRewardSpawnTime > 4000) {
            spawnReward();
            lastRewardSpawnTime = currentTime;
        }
        //recorre las recompensas
        for (int i = rewards.size() - 1; i >= 0; i--) {
            Reward r = rewards.get(i);
            //si colisiona con el perro
            if (dog.intersects(r)) {
                //si es un hotDog
                if (r.getType() == Reward.Type.HOT_DOG) {
                    //suma puntos y reproduce el sonido
                    score += 100;
                    SoundManager.playSound("bark.wav");
                } else if (r.getType() == Reward.Type.POWER_BONE) {
                    //si es un hueso recupera una vida, obtiene velocidad por 8 segundos
                    dog.recoverHealth();
                    dog.setSuperPowered(true);
                    powerUpEndTime = System.currentTimeMillis() + 8000;
                    //reproduce el sonido
                    SoundManager.playSound("powerUp.wav");
                }
                //elimina la recompensa
                rewards.remove(i);
            }
        }
        //recorre las ardillas
        for (int i = squirrels.size() - 1; i >= 0; i--) {
            Squirrel s = squirrels.get(i);
            //se les pasa la direccion del balon
            s.update(ball.x, ball.y);
            //una vez alcanza el balon pasa a escapar
            if (s.state == Squirrel.State.HUNTING_BALL && s.intersects(ball)) {
                s.state = Squirrel.State.ESCAPING;
            }
            //una vez escapa cambia la posicion del balon
            if (s.state == Squirrel.State.ESCAPING) {
                ball.x = s.x + (s.width / 2) - (ball.width / 2);
                ball.y = s.y + (s.height / 2) - (ball.height / 2);
            }
            //si colisiona con el perro
            if (dog.intersects(s)) {
                //si esta escapando
                if (s.state == Squirrel.State.ESCAPING) {
                    //resetea la posicion del balon
                    s.state = Squirrel.State.HUNTING_BALL;
                    ball.reset();
                    //suma 10 puntos
                    score += 10;
                    //elimina la ardilla
                    squirrels.remove(i);
                    //reproduce sonida
                    SoundManager.playSound("angryBark.wav");
                    continue;
                }
            }
            //si escapa con el balon
            if (s.hasEscaped()) {
                //pierde vida
                dog.loseHealth();
                //elimina la ardilla
                squirrels.remove(i);
                //resetea la posicion del balon
                ball.reset();
                // si la vida llega a 0 pasa al gameOver
                if (dog.getHealth() <= 0) {
                    triggerGameOver();
                }
            }
        }
    }

    /**
     * Pasa a gameOver
     */
    private void triggerGameOver() {
        gameOver = true;
        //para el loop del soundtrack
        SoundManager.stopLoop();
        //reproduce sonido de gameOver
        SoundManager.playSound("gameOver.wav");
        //guarda el puntaje del jugador
        saveScoreToHistory(playerName, score);
    }

    /**
     * Metodo para spawnear ardilla
     */
    private void spawnSquirrel() {
        //lado de spawneo
        int side = random.nextInt(4);
        int spawnX = 0, spawnY = 0;
        //posiciones segun el caso
        switch (side) {
            case 0 -> {
                spawnX = random.nextInt(750);
                spawnY = -30;
            }
            case 1 -> {
                spawnX = random.nextInt(750);
                spawnY = 630;
            }
            case 2 -> {
                spawnX = -30;
                spawnY = random.nextInt(550);
            }
            case 3 -> {
                spawnX = 830;
                spawnY = random.nextInt(550);
            }
        }
        //añade la nueva ardilla
        squirrels.add(new Squirrel(spawnX, spawnY));
    }

    /**
     * Metodo spawneo de recompensas
     */
    private void spawnReward() {
        //posiciones en x,y
        int rx = random.nextInt(700) + 50;
        int ry = random.nextInt(500) + 50;
        //tipo de recompensa
        Reward.Type type = (random.nextInt(10) < 3) ? Reward.Type.POWER_BONE : Reward.Type.HOT_DOG;
        //lo añade a la lista
        rewards.add(new Reward(rx, ry, type));
    }

    /**
     * guardar el historial
     * @param name nombre del jugador
     * @param score puntaje del mismo
     */
    private void saveScoreToHistory(String name, int score) {
        //crea o reescribe el nombre junto con el puntaje
        try (FileWriter fw = new FileWriter("ranking.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(name + "," + score);
        } catch (IOException e) {
        }
    }

    /**
     * Retorna la lista de top3
     * @return lista top3
     */
    public List<String> getTop3Ranking() {
        //crea la lista de puntajes
        List<PlayerScore> totalScores = new ArrayList<>();
        File file = new File("ranking.txt");
        if (!file.exists()) {
            return java.util.Arrays.asList("1. Sin datos - 0", "2. Sin datos - 0", "3. Sin datos - 0");
        }
        //lee el archivo
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    totalScores.add(new PlayerScore(parts[0], Integer.parseInt(parts[1])));
                }
            }
        } catch (Exception e) {
        }
        //ordena los puntajes
        Collections.sort(totalScores);
        List<String> top3List = new ArrayList<>();
        //añade los primeros 3
        for (int i = 0; i < 3; i++) {
            if (i < totalScores.size()) {
                PlayerScore ps = totalScores.get(i);
                top3List.add((i + 1) + ". " + ps.name + " - " + ps.score + " pts");
            } else {
                top3List.add((i + 1) + ". Vacío - 0");
            }
        }
        //devuelve la lista
        return top3List;
    }
    //Clase propia playerScore
    private static class PlayerScore implements Comparable<PlayerScore> {
        //nombre
        String name;
        //puntaje
        int score;

        /**
         * Constructor
         * @param name nombre
         * @param score puntaje
         */
        PlayerScore(String name, int score) {
            this.name = name;
            this.score = score;
        }
        //metodo que compara
        @Override
        public int compareTo(PlayerScore o) {
            return Integer.compare(o.score, this.score);
        }
    }

    /**
     * devuelve el puntaje
     * @return puntaje
     */
    public int getScore() {
        return score;
    }

    /**
     * devuelve si se ha perdido el juego
     * @return ha perdido?
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * devuelve el tiempo en segundos
     * @return
     */
    public int getGameTimeSeconds() {
        return gameTimeSeconds;
    }

    /**
     * devuelve el nombre del jugador
     * @return nombre
     */
    public String getPlayerName() {
        return playerName;
    }
    //eventos de teclas
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        //cambia la direccion segun la tecla oprimida
        if (code == KeyEvent.VK_UP || code == KeyEvent.VK_W) up = true;
        if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) down = true;
        if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) left = true;
        if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) right = true;
        //si se pierde y se le da a R, se reinicia a menu
        if (gameOver && code == KeyEvent.VK_R) {
            restartGame();
            panel.resetToMenu();
        }
    }
    //una vez se liberan las teclas
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        //se actualizan los valores de direccion
        if (code == KeyEvent.VK_UP || code == KeyEvent.VK_W) up = false;
        if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) down = false;
        if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) left = false;
        if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) right = false;
    }
}