package model;

public class Ball extends Entity {

    public Ball(int x, int y) {
        super(x, y, 16, 16, 0);
    }

    public void reset() {
        this.x = (800 / 2) - (this.width / 2);
        this.y = (600 / 2) - (this.height / 2);
    }
}