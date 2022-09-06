package tile;

public interface OnTileTouchListener {

    boolean onClick(int xTile, int yTile);

    boolean onDrag(int xFrom, int yFrom, int xTo, int yTo);

    void onDragEnd(int x, int y);

    void onDragCancel();
}