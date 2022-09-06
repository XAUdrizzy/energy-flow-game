package sma.game.model;

public class Line extends Cell {
    private Directions dir;
    private boolean power;

    public Line(Directions dir, boolean power) {
        super();
        this.power = power;
        this.dir = dir;
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

    public void setPower(boolean power) {
        this.power = power;

    }

    public boolean canBePowered(Directions dir) {
        return (isConnected(dir) && !power);
    }

    public boolean isConnected(Directions dir) {
        return this.dir == dir || this.dir == Directions.oposDirection(dir);
    }

    @Override
    public Directions[] otherDir(Directions dir) {
        return new Directions[]{Directions.oposDirection(dir)};
    }


}

