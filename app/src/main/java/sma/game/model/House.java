package sma.game.model;

public class House extends Cell {
    private Directions dir;
    private boolean power;

    public House(Directions dir, boolean power) {
        super();
        this.dir = dir;
        this.power = power;
    }

    public Directions getDir() {
        return dir;

    }

    public Directions nextDir() {
        return Directions.rotateRight(dir);

    }

    public void setDir(Directions dir) {
        this.dir = dir;

    }

    public boolean getPower() {
        return power;

    }

    @Override
    public void setPower(boolean power) {
        this.power = power;

    }

    @Override
    public boolean canBePowered(Directions dir) {
        return isConnected(dir) && !power;

    }

    public boolean isConnected(Directions dir) {
        return this.dir == Directions.oposDirection(dir);
    }

    @Override
    public Directions[] otherDir(Directions dir) {
        return null;
    }

}

