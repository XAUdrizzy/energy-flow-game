package sma.game.view.CellTypes;

import android.content.Context;
import android.graphics.Canvas;
import sma.game.view.*;
import sma.game.model.*;
import sma.game.R;

public class Fonte  extends CellTile {
    private Img power = new Img(context,R.drawable.power);

    public Fonte(Cell cell, Context context) {
        super(cell,context);
    }

    public void draw(Canvas canvas, int side){
        Directions dir = cell.getDir();
        p.setStrokeWidth(side / 3);
        if(dir == Directions.UP) canvas.drawLine(side / 2, side /2,side /2,0,p);
        if(dir == Directions.DOWN) canvas.drawLine(side / 2, side /2,side /2,side,p);
        if(dir == Directions.LEFT) canvas.drawLine(side / 2, side /2,0,side/2,p);
        if(dir == Directions.RIGHT) canvas.drawLine(side / 2, side /2,side,side/2,p);
        power.draw(canvas,side,side,p);

    }

}

