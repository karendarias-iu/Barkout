package model;

/**
 * Clase ardilla
 */
public class Squirrel extends Entity {
    //estado de la ardilla (cazando, escapando)
    public enum State {HUNTING_BALL, ESCAPING}
    //estado
    public State state;
    //direccion inicial
    public String direction = "abajo";
    //direccion en x,y de spawneo
    private int spawnX, spawnY;

    /**
     * Constructor
     * @param x posicion x
     * @param y posicion y
     */
    public Squirrel(int x, int y) {
        super(x, y, 32, 32, 3);
        this.spawnX = x;
        this.spawnY = y;
        this.state = State.HUNTING_BALL;
    }
    //realiza acciones segun el estado
    public void update(int ballX, int ballY) {
        if (state == State.HUNTING_BALL) {
            //va hacia el balon
            moveTowards(ballX, ballY);
        } else {
            //se devuelve
            moveTowards(spawnX, spawnY);
        }
    }

    /**
     * Metodo para ir hacia el balon
     * @param targetX posicion en x del balon
     * @param targetY posicion en y del balon
     */
    private void moveTowards(int targetX, int targetY) {
        //diferencia posiciones en x
        int diffX = targetX - x;
        //diferencia de posiciones en y
        int diffY = targetY - y;
        //determina la direccion segun las posiciones entre ambas
        if (Math.abs(diffX) > Math.abs(diffY)) {
            direction = (diffX > 0) ? "derecha" : "izquierda";
        } else {
            direction = (diffY > 0) ? "abajo" : "arriba";
        }
        //cambia la velocidad segun las posiciones
        if (x < targetX) x += speed;
        else if (x > targetX) x -= speed;
        if (y < targetY) y += speed;
        else if (y > targetY) y -= speed;
    }

    /**
     * retorna si la ardilla escapo una vez toca el balon
     * @return escapo?
     */
    public boolean hasEscaped() {
        return state == State.ESCAPING && Math.abs(x - spawnX) <= speed && Math.abs(y - spawnY) <= speed;
    }
}