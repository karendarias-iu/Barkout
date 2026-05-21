package model;

/**
 * Clase perro
 */
public class Dog extends Entity {
    //vidas
    private int health;
    //direccion inicial
    public String direction = "abajo";
    //bandera que determina si se esta moviendo o no
    public boolean moving = false;
    //bander que determina si tiene el powe up
    private boolean superPowered = false;

    /**
     * Constructor de la clase
     * @param x posicion en x
     * @param y posicion en y
     */
    public Dog(int x, int y) {
        //con un tamaño de 50x50px y una velocidad de 6
        super(x, y, 50, 50, 6);
        //se le dan 3 vidas
        this.health = 3;
    }

    /**
     * metodo para moverlo
     * @param dx direccion en x
     * @param dy direccion en y
     */
    public void move(int dx, int dy) {
        //siempre y cuando dx o dy sea diferente de 0 entonces se esta moviendo
        moving = (dx != 0 || dy != 0);

        //determina la direccion
        if (dx > 0) direction = "derecha";
        else if (dx < 0) direction = "izquierda";
        else if (dy > 0) direction = "abajo";
        else if (dy < 0) direction = "arriba";
        //determina la velocidad dependiendo si tienen o no el power up
        int currentSpeed = superPowered ? speed * 2 : speed;
        //actualiza la posicion en x,y
        x += dx * currentSpeed;
        y += dy * currentSpeed;
        //no lo deja salir de los limites de la pantalla
        if (x < 0) x = 0;
        if (x > 750) x = 750;
        if (y < 0) y = 0;
        if (y > 550) y = 550;
    }

    /**
     * obtener las vidas
     * @return numero de vidas
     */
    public int getHealth() {
        return health;
    }

    /**
     * reducir una vida
     */
    public void loseHealth() {
        this.health--;
    }

    /**
     * recuperar una vida
     */
    public void recoverHealth() {
        if (this.health < 3) this.health++;
    }

    /**
     * saber si tiene power up o no
     * @return bander de power up
     */
    public boolean isSuperPowered() {
        return superPowered;
    }

    /**
     * actualiza si tiene o no power up
     * @param superPowered tiene o no power up?
     */
    public void setSuperPowered(boolean superPowered) {
        this.superPowered = superPowered;
    }
}