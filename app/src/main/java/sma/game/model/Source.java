package sma.game.model;

public class Source extends  Cell{
    private Directions dir;
    private final boolean power = true;

    public Source(Directions dir){
        super();
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
        //POWER IS ALWAYS TRUE BECAUSE IT'S A SOURCE CELL
    }

    public boolean canBePowered(Directions dir) {
        return false;

    }

    public boolean isConnected(Directions dir) {
        return this.dir == Directions.oposDirection(dir);

    }

    @Override
    public Directions[] otherDir(Directions dir) {
        return null;
    }

}

