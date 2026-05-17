package model;

public class Dog extends Entity {
    private int health;
    public String direction = "abajo";
    public boolean moving = false; // Nueva variable

    public Dog(int x, int y) {
        super(x, y, 50, 50, 6);
        this.health = 3;
    }

    public void move(int dx, int dy) {
        moving = (dx != 0 || dy != 0);

        if (dx > 0) direction = "derecha";
        else if (dx < 0) direction = "izquierda";
        else if (dy > 0) direction = "abajo";
        else if (dy < 0) direction = "arriba";

        x += dx * speed;
        y += dy * speed;

        if (x < 0) x = 0;
        if (x > 750) x = 750;
        if (y < 0) y = 0;
        if (y > 550) y = 550;
    }

    public int getHealth() {
        return health;
    }

    public void loseHealth() {
        this.health--;
    }
}