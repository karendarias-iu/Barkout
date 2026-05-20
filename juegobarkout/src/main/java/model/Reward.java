package model;

public class Reward extends Entity {
    public enum Type {BERRY, POWER_BONE}

    private Type type;

    public Reward(int x, int y, Type type) {
        super(x, y, 40, 40, 0);
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}