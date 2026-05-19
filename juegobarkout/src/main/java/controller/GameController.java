package controller;

import model.Ball;
import model.Dog;
import model.Reward;
import model.Squirrel;
import view.GameMenu;
import view.GamePanel;
import view.SoundManager;

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

public class GameController extends KeyAdapter {
    public Dog dog;
    public Ball ball;
    public List<Squirrel> squirrels;
    public List<Reward> rewards;

    private boolean up, down, left, right;
    private Random random = new Random();
    private long lastSpawnTime = 0;
    private long lastRewardSpawnTime = 0;
    private int score = 0;
    private boolean gameOver = false;
    private String playerName = "Invitado";
    private int gameTimeSeconds = 0;
    private Timer gameTimer;
    private long powerUpEndTime = 0;
    private GamePanel panel;
    private GameMenu menu;

    public GameController(GamePanel panel, GameMenu menu) {
        this.panel = panel;
        this.menu = menu;
        initTimer();
        restartGame();
    }

    private void initTimer() {
        gameTimer = new Timer(1000, e -> {
            if (!gameOver && panel.getGameState().equals("game")) {
                gameTimeSeconds++;
                if (dog.isSuperPowered() && System.currentTimeMillis() > powerUpEndTime) {
                    dog.setSuperPowered(false);
                }
            }
        });
        gameTimer.start();
    }

    public void requestPlayerName() {
        String input = javax.swing.JOptionPane.showInputDialog(panel, "Ingrese su nombre de usuario:", "Registro de Jugador", javax.swing.JOptionPane.PLAIN_MESSAGE);
        if (input != null && !input.trim().isEmpty()) {
            this.playerName = input.trim();
        } else {
            this.playerName = "Anonimo";
        }
    }

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
        SoundManager.playSound("inicio.wav");
    }

    public void updateLogic() {
        if (gameOver) return;
        int dx = 0, dy = 0;
        if (up) dy--;
        if (down) dy++;
        if (left) dx--;
        if (right) dx++;
        dog.move(dx, dy);

        long currentTime = System.currentTimeMillis();

        if (currentTime - lastSpawnTime > 2500) {
            spawnSquirrel();
            lastSpawnTime = currentTime;
        }

        if (currentTime - lastRewardSpawnTime > 4000) {
            spawnReward();
            lastRewardSpawnTime = currentTime;
        }

        for (int i = rewards.size() - 1; i >= 0; i--) {
            Reward r = rewards.get(i);
            if (dog.intersects(r)) {
                if (r.getType() == Reward.Type.BERRY) {
                    score += 30;
                    SoundManager.playSound("colision.wav");
                } else if (r.getType() == Reward.Type.POWER_BONE) {
                    dog.recoverHealth();
                    dog.setSuperPowered(true);
                    powerUpEndTime = System.currentTimeMillis() + 8000;
                    SoundManager.playSound("inicio.wav");
                }
                rewards.remove(i);
            }
        }

        for (int i = squirrels.size() - 1; i >= 0; i--) {
            Squirrel s = squirrels.get(i);
            s.update(ball.x, ball.y);

            if (s.state == Squirrel.State.HUNTING_BALL && s.intersects(ball)) {
                s.state = Squirrel.State.ESCAPING;
                SoundManager.playSound("colision.wav");
            }

            if (s.state == Squirrel.State.ESCAPING) {
                ball.x = s.x + (s.width / 2) - (ball.width / 2);
                ball.y = s.y + (s.height / 2) - (ball.height / 2);
            }

            if (dog.intersects(s)) {
                if (s.state == Squirrel.State.ESCAPING) {
                    s.state = Squirrel.State.HUNTING_BALL;
                    ball.reset();
                    score += 10;
                    squirrels.remove(i);
                    SoundManager.playSound("colision.wav");
                    continue;
                }
            }

            if (s.hasEscaped()) {
                dog.loseHealth();
                squirrels.remove(i);
                ball.reset();
                if (dog.getHealth() <= 0) {
                    triggerGameOver();
                }
            }
        }
    }

    private void triggerGameOver() {
        gameOver = true;
        SoundManager.playSound("gameover.wav");
        saveScoreToHistory(playerName, score);
    }

    private void spawnSquirrel() {
        int side = random.nextInt(4);
        int spawnX = 0, spawnY = 0;
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
        squirrels.add(new Squirrel(spawnX, spawnY));
    }

    private void spawnReward() {
        int rx = random.nextInt(700) + 50;
        int ry = random.nextInt(500) + 50;
        Reward.Type type = (random.nextInt(10) < 3) ? Reward.Type.POWER_BONE : Reward.Type.BERRY;
        rewards.add(new Reward(rx, ry, type));
    }

    private void saveScoreToHistory(String name, int score) {
        try (FileWriter fw = new FileWriter("ranking.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(name + "," + score);
        } catch (IOException e) {
        }
    }

    public List<String> getTop3Ranking() {
        List<PlayerScore> totalScores = new ArrayList<>();
        File file = new File("ranking.txt");
        if (!file.exists()) {
            return java.util.Arrays.asList("1. Sin datos - 0", "2. Sin datos - 0", "3. Sin datos - 0");
        }

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

        Collections.sort(totalScores);
        List<String> top3List = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            if (i < totalScores.size()) {
                PlayerScore ps = totalScores.get(i);
                top3List.add((i + 1) + ". " + ps.name + " - " + ps.score + " pts");
            } else {
                top3List.add((i + 1) + ". Vacío - 0");
            }
        }
        return top3List;
    }

    private static class PlayerScore implements Comparable<PlayerScore> {
        String name;
        int score;

        PlayerScore(String name, int score) {
            this.name = name;
            this.score = score;
        }

        @Override
        public int compareTo(PlayerScore o) {
            return Integer.compare(o.score, this.score);
        }
    }

    public int getScore() {
        return score;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getGameTimeSeconds() {
        return gameTimeSeconds;
    }

    public String getPlayerName() {
        return playerName;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_UP || code == KeyEvent.VK_W) up = true;
        if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) down = true;
        if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) left = true;
        if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) right = true;

        if (gameOver && code == KeyEvent.VK_R) {
            restartGame();
            panel.resetToMenu();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_UP || code == KeyEvent.VK_W) up = false;
        if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) down = false;
        if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) left = false;
        if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) right = false;
    }
}