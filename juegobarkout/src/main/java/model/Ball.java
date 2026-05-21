package model;

/**
 * Clase pelota
 */
public class Ball extends Entity {
    //se crea la pelota segun la posicion con un tamaño de 30x30px
    public Ball(int x, int y) {
        super(x, y, 30, 30, 0);
    }

    /**
     * Metodo para posicionar la pelota en el centro del juego
     */
    public void reset() {
        //actualiza las posiciones
        this.x = (800 / 2) - (this.width / 2);
        this.y = (600 / 2) - (this.height / 2);
    }
}