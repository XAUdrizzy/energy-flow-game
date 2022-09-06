package sma.game.model;

public enum Directions {
    UP, DOWN, LEFT, RIGHT;

    public static Directions rotateRight(Directions dir) {
        switch (dir) {
            case UP:
                return RIGHT;
            case RIGHT:
                return DOWN;
            case DOWN:
                return LEFT;
            case LEFT:
                return UP;
        }
        return null;
    }

    public static Directions rotateLeft(Directions dir){
        switch (dir){
            case UP:
                return LEFT;
            case LEFT:
                return DOWN;
            case DOWN:
                return RIGHT;
            case RIGHT:
                return UP;
        }
        return null;
    }

    public static Directions randomDirection(){
        int random = (int)(Math.random()*4);
        switch (random){
            case 0:
                return UP;
            case 1:
                return RIGHT;
            case 2:
                return DOWN;
            case 3:
                return LEFT;
        }
        return null;
    }

    public static Directions oposDirection(Directions dir){
        switch (dir){
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case RIGHT:
                return LEFT;
            case LEFT:
                return RIGHT;
        }
        return null;
    }

}
