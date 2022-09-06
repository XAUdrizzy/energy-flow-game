package tile;

import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.LinkedList;
import java.util.ListIterator;

public class Animator {
    private LinkedList<AnimTile> anims = new LinkedList<>();  // All active animations
    private TilePanel panel; // The TilePanel in used

    private long nextTime;  // time for the next animation
    private int adjust = 0; // steps to subtract in next animation

    static int STEP_TIME = 50; // Time interval for steps of animations

    private OnFinishAnimationListener listener; // The listener of animations ends
    private Object listenerTag; // Tag to give the listener

    Animator(TilePanel p) {  panel = p;  }

    public AnimTile getAnim(Tile tile) {
        if (anims.size()==0) return null;
        for(AnimTile as : anims)
            if (as.tile==tile) {
                while(as.after!=null && as.tile==tile) as = as.after;
                return as;
            }
        return null;
    }

    // Draw animations. Called by onDraw() of TilePanel
    void drawAnims(Canvas canvas) {
        long tm = System.currentTimeMillis();
        if (anims.size()==0 || nextTime > tm) return;
        ListIterator<AnimTile> i = anims.listIterator();
        while( i.hasNext() ) {
            AnimTile a = i.next();
            if (adjust>0) a.steps -= Math.min(adjust, a.steps - 1);
            a.stepDraw(canvas, panel.sideTile);
            if (--a.steps<=0) {
                if (a.after != null) i.set(a.after);
                else i.remove();
            }
        }
        adjust = 0;
        if (anims.size()==0) {
            if (listener!=null) {
                OnFinishAnimationListener l = listener;
                listener = null;
                l.onFinish(listenerTag);
            }
        } else {
            tm = System.currentTimeMillis();
            nextTime += STEP_TIME;
            long remain = nextTime - tm;
            if (remain < 0) {
                adjust = (int)(-remain / STEP_TIME) + 1;
                remain += adjust*STEP_TIME;
                nextTime += adjust*STEP_TIME;
            }
            panel.postInvalidateDelayed(remain);
        }
    }
}