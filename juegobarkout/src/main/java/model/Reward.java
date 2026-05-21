package model;

/**
 * Clase recompensa
 */
public class Reward extends Entity {
    //lista de tipos recompensas
    public enum Type {HOT_DOG, POWER_BONE}
    //tipo de recompensa
    private Type type;

    /**
     * Constructor
     * @param x posicion en x
     * @param y posicion en y
     * @param type tipo de recompensa
     */
    public Reward(int x, int y, Type type) {
        super(x, y, 40, 40, 0);
        this.type = type;
    }

    /**
     * Obtener el tipo de recompensa que es
     * @return tipo de recompensa
     */
    public Type getType() {
        return type;
    }
}