package sma.game.model;

public class Branch extends Cell {
    private Directions dir;
    private boolean power;

    public Branch(Directions dir, boolean power) {
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

    public void setPower(boolean power) {
        this.power = power;

    }

    public boolean canBePowered(Directions dir) {
        return (isConnected(dir) && !power);
    }

    @Override
    public boolean isConnected(Directions dir) {
        return dir == Directions.oposDirection(this.dir) || dir == Directions.oposDirection(Directions.rotateLeft(this.dir)) || dir == Directions.oposDirection(Directions.rotateRight(this.dir));
    }

    @Override
    public Directions[] otherDir(Directions dir) {
        if(dir == this.dir)
            return new Directions[]{Directions.rotateLeft(this.dir),Directions.rotateRight(this.dir)};
        else if(dir==Directions.rotateLeft(this.dir))
            return new Directions[]{this.dir,Directions.rotateRight(this.dir)};
        else return new Directions[]{this.dir,Directions.rotateLeft(this.dir)};
    }


}
