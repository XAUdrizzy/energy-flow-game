package sma.game.model;

public class Plant {
    private Cell[][] map;
    private final int height;
    private final int width;
    private Listener listener;
    private int moves = 0;

    public Plant(int height, int width) {
        this.height = height;
        this.width = width;

        map = new Cell[height][width];

    }

    public int getHeight() {
        return height;

    }

    public int getWidth() {
        return width;

    }

    public Cell getCell(int l, int c) {
        return map[l][c];

    }

    public int getMoves() {
        return moves;

    }

    public boolean isCompleted() {
        int totalCells = 0;
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                Cell cell = getCell(i,j);
                if(!(cell instanceof Space)){
                    totalCells++;
                }

            }

        }
        int totalPoweredCells = 0;
        for (int l = 0; l < getHeight(); l++) {
            for (int c = 0; c < getWidth(); c++) {
                Cell cell2 = getCell(l,c);
                if (!(cell2 instanceof Space) && cell2.getPower()){
                    totalPoweredCells++;

                }

            }
            if (totalCells == totalPoweredCells) return true;
        }
        return false;
    }

    public void setListener(Listener listener) {
        this.listener = listener;

    }

    public boolean touch(int line, int col) {
        Cell cell = getCell(line, col);
        if (!(cell instanceof Space)) {
            cell.setDir(cell.nextDir()); //rotation
            moves++;
            listener.cellChanged(line, col, cell);
            turnOffCells();
            init();
            return true;
        }
        return false;
    }

    public void turnOffCells() {
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                Cell cell = getCell(i, j);
                if (!(cell instanceof Space) && !(cell instanceof Source)) {
                    cell.setPower(false);
                    listener.cellChanged(i, j, cell);
                }

            }

        }
    }

    public void init() {
        turnOnPowered();
        initPower();

    }

    public void initPower() {
        for (int l = 0; l < getHeight(); l++) {
            for (int c = 0; c < getWidth(); c++) {
                Cell cell = getCell(l, c);
                if (cell instanceof Source) {
                    Cell next = getNextCell(cell.getDir(), l, c);
                    int nextLine = getNextLine(cell.getDir(),l);
                    int nextCol = getNextCol(cell.getDir(),c);
                    if ((next != null) && next.canBePowered(cell.getDir())) {
                        next.setPower(true);
                        listener.cellChanged(nextLine,nextCol,next);
                        followPower(nextLine,nextCol, next, Directions.oposDirection(cell.getDir()));
                    }

                }
            }
        }
    }

    public Cell getNextCell(Directions directions, int l, int c) {
        switch (directions) {
            case UP:
                if (l > 0) return getCell(l - 1, c);
                else return null;
            case DOWN:
                if (l < getHeight()-1) return getCell(l + 1, c);
                else return null;
            case RIGHT:
                if (c < getWidth()-1) return getCell(l, c + 1);
            case LEFT:
                if (c > 0) return getCell(l, c - 1);
                else return null;
        }
        return null;
    }

    public int getNextLine(Directions directions, int l) {
        switch (directions) {
            case UP:
                if (l > 0) return l - 1;
            case DOWN:
                if (l < getHeight()-1) return l + 1;
            case RIGHT:
                return l;
            case LEFT:
                return l;
        }
        return l;
    }

    public int getNextCol(Directions directions, int c) {
        switch (directions) {
            case UP:
                return c;
            case DOWN:
                return c;
            case RIGHT:
                if (c < getWidth()-1) return c+1;
            case LEFT:
                if (c > 0) return c-1;
        }
        return c;

    }

    private void followPower(int l, int c, Cell cell, Directions directions) {
        Directions[] other = cell.otherDir(directions);
        if(other!=null){
            for (int i = 0; i < other.length ; i++) {
                Cell nextCell = getNextCell(other[i], l, c);
                if ((nextCell != null) && (nextCell.canBePowered(other[i]))) {
                    powerCell(nextCell);
                    listener.cellChanged(nextCell.getLine(), nextCell.getCol(), nextCell);
                    followPower(nextCell.getLine(), nextCell.getCol(), nextCell, Directions.oposDirection(other[i]));
                }
            }

        }

    }
    private void turnOnPowered() {
        for (int u = 0; u < getHeight() ; u++) {
            for (int i = 0; i < getWidth(); i++) {
                Cell updatedCell = getCell(u,i);
                listener.cellChanged(u,i,updatedCell);

            }

        }

    }

    public void powerCell(Cell cell) {
        if (cell != null) {
            cell.setPower(true);
        }
    }

    public void putCell(int l, int c, Cell cell) {
        map[l][c] = cell;
        cell.setCol(c);
        cell.setLine(l);
    }

    public interface Listener {
        void cellChanged(int lin, int col, Cell cell);
    }

}
