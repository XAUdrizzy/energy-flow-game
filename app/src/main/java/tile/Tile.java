package tile;

import android.graphics.Canvas;

public interface Tile {

    void draw(Canvas canvas, int side);

    boolean setSelect(boolean selected);
}