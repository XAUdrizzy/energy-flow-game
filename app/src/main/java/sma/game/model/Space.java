package sma.game.model;

public class Space extends Cell {
    private Directions dir;

    public Space() {
        super();
    }

    @Override
    public Directions getDir() {
        return null;
    }

    @Override
    public Directions nextDir() {
        return null;
    }

    @Override
    public void setDir(Directions dir) {

    }

    @Override
    public boolean getPower() {
        return false;
    }

    @Override
    public void setPower(boolean power) {

    }

    @Override
    public boolean canBePowered(Directions dir) {
        return false;
    }

    @Override
    public boolean isConnected(Directions dir) {
        return false;
    }

    @Override
    public Directions[] otherDir(Directions dir) {
        return null;
    }


}

