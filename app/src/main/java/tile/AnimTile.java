package tile;

import android.graphics.Canvas;
import android.graphics.Rect;

public abstract class AnimTile {

    protected Tile tile;
    protected int x,y;
    protected int steps;
    protected AnimTile after;


    protected AnimTile(int x, int y, int time, TilePanel p) {
        this(x,y,time,p,null);
    }

    protected AnimTile(int x, int y, int time, TilePanel p, Tile t) {
        Rect r = p.tileRect(x, y);
        this.x = r.left; this.y = r.top;
        this.tile = t==null ? p.getTile(x, y) : t ;
        steps = Math.max(time / Animator.STEP_TIME, 1);
    }

    public void stepDraw(Canvas cv, int side) {
        cv.save();
        cv.clipRect(x, y, x + side, y + side);
        cv.translate(x, y);
        if (tile==null)
            System.out.println("StepDraw: tile=null");
        tile.draw(cv,side);
        cv.restore();
    }
}
