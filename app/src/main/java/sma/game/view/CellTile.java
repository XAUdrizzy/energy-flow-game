package sma.game.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import sma.game.view.CellTypes.*;
import sma.game.model.*;
import tile.*;

public class CellTile implements Tile {
    protected Cell cell;
    protected Paint p = new Paint();
    protected Context context;

    public CellTile(Cell cell, Context context) {
        this.cell = cell;
        this.context = context;
        p.setStrokeCap(Paint.Cap.ROUND);
        p.setColor(cell.getPower() ? Color.GREEN : Color.DKGRAY);
    }

    public void draw(Canvas canvas, int side) {

    }

    @Override
    public boolean setSelect(boolean selected) {
        return false;
    }

    public static CellTile newInstance(Cell cell, Context context) {
        if (cell instanceof Source) return new Fonte(cell, context);
        if (cell instanceof Line) return new Linha(cell, context);
        if (cell instanceof House) return new Casa(cell, context);
        if (cell instanceof Curve) return new Curva(cell, context);
        if (cell instanceof Branch) return new Ramo(cell, context);
        if (cell instanceof Space) return new Espaco(cell,context);
        return null;
    }

}
