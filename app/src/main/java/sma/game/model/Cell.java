package sma.game.model;

public abstract class Cell {
    protected static Plant model;
    private int line;
    private int col;

    //DIRECTIONS AND POWER:
    public abstract Directions getDir();
    public abstract Directions nextDir();
    public abstract void setDir(Directions dir);
    public  abstract boolean getPower();
    public abstract void setPower(boolean power);

    //CONECTIONS:
    public abstract boolean canBePowered(Directions dir);
    public abstract boolean isConnected(Directions dir);
    public abstract Directions[] otherDir(Directions dir);


    //POSITIONS:
    public int getLine(){return line;}
    public void setLine(int line){this.line = line;}
    public int getCol(){return col;}
    public void setCol(int col){this.col = col;}


    public static Cell newInstance(char type) {
        switch(type){
            case 'P':
                return new Source(Directions.randomDirection());
            case 'H':
                return new House(Directions.randomDirection(),false);
            case '-':
                return new Line(Directions.randomDirection(),false);
            case 'c':
                return new Curve(Directions.randomDirection(),false);
            case 'T':
                return new Branch(Directions.randomDirection(),false);
            case '.':
                return new Space();

        }
        return null;
    }
}
