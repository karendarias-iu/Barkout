package model;

public class Squirrel extends Entity {
    public enum State {HUNTING_BALL, ESCAPING}

    public State state;
    public String direction = "abajo";
    private int spawnX, spawnY;

    public Squirrel(int x, int y) {
        super(x, y, 32, 32, 3);
        this.spawnX = x;
        this.spawnY = y;
        this.state = State.HUNTING_BALL;
    }

    public void update(int ballX, int ballY) {
        if (state == State.HUNTING_BALL) {
            moveTowards(ballX, ballY);
        } else {
            moveTowards(spawnX, spawnY);
        }
    }

    private void moveTowards(int targetX, int targetY) {
        int diffX = targetX - x;
        int diffY = targetY - y;

        if (Math.abs(diffX) > Math.abs(diffY)) {
            direction = (diffX > 0) ? "derecha" : "izquierda";
        } else {
            direction = (diffY > 0) ? "abajo" : "arriba";
        }

        if (x < targetX) x += speed;
        else if (x > targetX) x -= speed;
        if (y < targetY) y += speed;
        else if (y > targetY) y -= speed;
    }

    public boolean hasEscaped() {
        return state == State.ESCAPING && Math.abs(x - spawnX) <= speed && Math.abs(y - spawnY) <= speed;
    }
}