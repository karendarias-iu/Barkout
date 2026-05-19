package controller;

import model.Ball;
import model.Dog;
import model.Squirrel;
import view.GameMenu;
import view.GamePanel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameController extends KeyAdapter {
    public Dog dog;
    public Ball ball;
    public List<Squirrel> squirrels;

    private boolean up, down, left, right;
    private Random random = new Random();
    private long lastSpawnTime = 0;

    private int score = 0;
    private boolean gameOver = false;

    private GamePanel panel;
    private GameMenu menu;

    public GameController(GamePanel panel,GameMenu menu) {
        this.panel = panel;
        this.menu = menu;
        panel.addKeyListener(new InputHandler(menu,panel));
        restartGame();
    }

    public void restartGame() {
        dog = new Dog(380, 450);
        ball = new Ball(392, 292);
        squirrels = new ArrayList<>();
        score = 0;
        gameOver = false;
        lastSpawnTime = System.currentTimeMillis();
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

        for (int i = 0; i < squirrels.size(); i++) {
            Squirrel s = squirrels.get(i);
            s.update(ball.x, ball.y);

            if (s.state == Squirrel.State.HUNTING_BALL && s.getBounds().intersects(ball.getBounds())) {
                s.state = Squirrel.State.ESCAPING;
            }

            if (s.state == Squirrel.State.ESCAPING) {
                ball.x = s.x + (s.width / 2) - (ball.width / 2);
                ball.y = s.y + (s.height / 2) - (ball.height / 2);
            }

            if (dog.getBounds().intersects(s.getBounds())) {
                if (s.state == Squirrel.State.ESCAPING) {
                    s.state = Squirrel.State.HUNTING_BALL;
                    ball.reset();
                    score += 10;
                }
            }

            if (s.hasEscaped()) {
                dog.loseHealth();
                squirrels.remove(i);
                i--;
                ball.reset();
                if (dog.getHealth() <= 0) {
                    gameOver = true;
                }
            }
        }
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

    public int getScore() {
        return score;
    }

    public boolean isGameOver() {
        return gameOver;
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