package tile;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.security.InvalidParameterException;
import sma.game.R;

public class TilePanel extends View {

    private int wTiles=-1, hTiles=-1;   // Panel dimensions in tiles.

    private Tile[] tiles;				// The tiles.
    private Paint paint = new Paint();  // To draw some parts.

    int sideTile;					// width and height of each tile.
    private int xInit, yInit, xEnd, yEnd;	// Bounds of panel.
    private int gridLine = 0;		// grid lines stroke width.

    public TilePanel(Context context) {
        super(context);
        setSize(10,10);
    }

    public TilePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttrs(context, attrs);
    }


    public TilePanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        parseAttrs(context, attrs);
    }

    // Parse attributes of layout definition
    private void parseAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TilePanel);
        wTiles = a.getInteger(R.styleable.TilePanel_widthTiles,-1);
        hTiles = a.getInteger(R.styleable.TilePanel_heightTiles,-1);
        gridLine = a.getInteger(R.styleable.TilePanel_gridLine, 1);
        if (isInEditMode() && gridLine==0) gridLine = 1;
        if (wTiles==-1 && hTiles==-1) wTiles=hTiles=8;
        if (wTiles==-1) wTiles=hTiles;
        if (hTiles==-1) hTiles=wTiles;
        paint.setColor(a.getColor(R.styleable.TilePanel_backgroundTiles, Color.DKGRAY));
        paint.setStrokeWidth(gridLine);
        a.recycle();
        tiles = new Tile[wTiles*hTiles];
    }

    public void setSize(int width, int height) {
        wTiles = width;
        hTiles = height;
        tiles = new Tile[wTiles*hTiles];
        resize(getWidth(),getHeight());
    }

    public void setTile(int x, int y, Tile t) {
        setTileNoInvalidate(x,y,t);
        invalidate(x, y);
    }

    private int idxTile(int x, int y) { return y*wTiles+x; }

    void setTileNoInvalidate(int x, int y, Tile t) {
        tiles[idxTile(x,y)] = t;
    }


    public Tile getTile(int x, int y) {
        return tiles[idxTile(x,y)];
    }

    @Override
    protected void onMeasure(int wMS, int hMS) {
        int w = MeasureSpec.getSize(wMS);
        int h = MeasureSpec.getSize(hMS);
        if (MeasureSpec.getMode(hMS)== MeasureSpec.UNSPECIFIED)
            h = getSuggestedMinimumHeight();
        if (MeasureSpec.getMode(wMS)== MeasureSpec.UNSPECIFIED)
            w = getSuggestedMinimumWidth();
        calcSideTile(w, h);
        int wm = sideTile*wTiles+gridLine*(wTiles+1);
        int hm = sideTile*hTiles+gridLine*(hTiles+1);
        setMeasuredDimension(wm,hm);
    }

    private void calcSideTile(int w, int h) {
        int wt = (w-gridLine*(wTiles+1))/wTiles;
        int ht = (h-gridLine*(hTiles+1))/hTiles;

        sideTile = Math.min(ht, wt);
    }

    // Called to draw the View
    @Override
    protected void onDraw(Canvas canvas) {
        try {

            Tile t;
            for (int y = 0, idx = 0; y < hTiles; ++y)
                for (int x = 0; x < wTiles; ++x, ++idx)
                    if ((t = tiles[idx]) != null)
                        drawTile(canvas, t, x, y);
            drawGrid(canvas);
            if (animator!=null)
                animator.drawAnims(canvas);
        } catch (Exception e) {
            Toast.makeText(getContext(), "onDraw(): " + e, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    // Draw each tile. Called by onDraw()
    private void drawTile(Canvas canvas, Tile t, int x, int y) {
        Rect r = tileRect(x, y);
        Rect cb = canvas.getClipBounds();
        if ( ! r.intersect(cb) ) return;
        canvas.save();
        canvas.clipRect(r);
        canvas.translate(r.left,r.top);
        if (animator!=null && animator.getAnim(t)!=null)
            canvas.drawColor(Color.TRANSPARENT);
        else
            t.draw(canvas,sideTile);
        canvas.restore();
    }

    // Draw grid lines. Called by onDraw()
    private void drawGrid(Canvas canvas) {
        if (gridLine>0) {
            for(int x=xInit+gridLine/2 ; x<=xEnd ; x+=sideTile+gridLine )
                canvas.drawLine(x, yInit, x, yEnd, paint);
            for(int y=yInit+gridLine/2 ; y<=yEnd ; y+=sideTile+gridLine )
                canvas.drawLine(xInit, y, xEnd, y, paint);
        }
    }

    // Called by layout manager if size changed.
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        resize(w, h);
    }

    // Calculate each tile dimensions and other bounds of internal panel
    private void resize(int w, int h) {
        calcSideTile(w, h);
        xInit = ((w-gridLine)%wTiles)/2;
        yInit = ((h-gridLine)%hTiles)/2;
        xEnd = xInit+(sideTile+gridLine)*wTiles+gridLine;
        yEnd = yInit+(sideTile+gridLine)*hTiles+gridLine;
    }

    private int xTouch, yTouch;	// x and y of last event
    private int xDown, yDown;	// x and y of last Down event
    private Tile selected;		// last tile selected
    private int pointerId;		// pointer of last event
    private boolean inDrag;     // next ACTION_MOVE is to drag

    // The listener of tile touches.
    private OnTileTouchListener listener;

    public void setListener(OnTileTouchListener l) { listener = l; }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (x < xInit || x >= xEnd || y < yInit || y >= yEnd) {
                if (inDrag) {
                    unselectTouched();
                    if (listener!=null) listener.onDragCancel();
                }
                inDrag=false;
                return false;
            }
            int xt = (x-xInit-gridLine)/(sideTile+gridLine);
            int yt = (y-yInit-gridLine)/(sideTile+gridLine);
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pointerId=ev.getPointerId(0);
                    selectTouched(xt, yt);
                    xTouch = xt; yTouch = yt;
                    xDown = xt; yDown = yt;
                    inDrag = true;
                    return true;
                case MotionEvent.ACTION_UP:
                    if (selected==getTile(xt,yt) && listener!=null && xDown==xt && yDown==yt)
                        listener.onClick(xt, yt);
                    unselectTouched();
                    if (inDrag && listener!=null) listener.onDragEnd(xt,yt);
                    inDrag=false;
                    return true;
                case MotionEvent.ACTION_MOVE:
                    if (xt!=xTouch || yt!=yTouch) {
                        unselectTouched();
                        if (listener!=null && ev.getPointerId(0)==pointerId && inDrag)
                            inDrag = listener.onDrag(xTouch, yTouch, xt, yt);
                        xTouch = xt; yTouch = yt;
                        return true;
                    }
                    return false;
                default:
            }
        } catch (Exception e) {	e.printStackTrace(); }
        return false;
    }

    private void selectTouched(int xt, int yt) {
        Tile tile = getTile(xt, yt);
        if (tile!=null && tile.setSelect(true))
            invalidate(xt,yt);
        selected = tile;
    }

    private void unselectTouched() {
        if (selected==null) return;
        if (selected.setSelect(false))
            invalidate(xTouch,yTouch);
        selected = null;
    }

    private Rect rect = new Rect();

    public Rect tileRect(int xt, int yt) {
        int x = xInit + gridLine + xt*(sideTile + gridLine);
        int y = yInit + gridLine + yt*(sideTile + gridLine);
        rect.set(x,y, x+sideTile,y+sideTile);
        return rect;
    }

    void invalidate(int x, int y) {
        Rect r = tileRect(x, y);
        invalidate(r.left,r.top,r.right,r.bottom);
    }

    // The tiles animator
    private Animator animator = null;

    private OnBeatListener beatListener;
    private long beatNumber = 0, period;
    private Runnable action = new Runnable() {
        public void run() {
            if (beatListener==null) return;
            beatListener.onBeat(beatNumber++, System.currentTimeMillis());
            postDelayed(action,period);
        }
    };
}