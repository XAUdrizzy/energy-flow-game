package sma.game.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.SparseArray;

public class Img {
    private static SparseArray<Bitmap> cache = new SparseArray<>();
    private final int id;
    private final Resources res;
    private final Updater updater;

    public Img(Context ctx, int id) {
        this(ctx,id,null);
    }

    public Img(Context ctx, int id, Updater updater) {
        this.id = id;
        res = ctx.getResources();
        this.updater = updater;
    }

    public void draw(Canvas canvas, int width, int height, Paint p) {
        draw(canvas,width,height,0,p);
    }

    private static Matrix m = new Matrix();

    public void draw(Canvas canvas, int width, int height, float angle, Paint p) {
        Bitmap bitmap = getBitMap(width,height);
        if (bitmap==null) return;
        m.reset();
        m.postScale((float)width/bitmap.getWidth(), (float)height/bitmap.getHeight());
        if (angle!=0)
            m.postRotate(angle,(float)width/2,(float)height/2);
        canvas.drawBitmap(bitmap, m, p);
    }

    private Bitmap getBitMap(int width, int height) {
        Bitmap bm = cache.get(id);
        if (bm==null || bm.getWidth()<width || bm.getHeight()<height ) {
            if (updater==null) {
                bm = load(width, height);
                cache.put(id, bm);
            } else
                new LoaderTask().execute(width,height);
        }
        return bm;
    }

    private Bitmap load(int width, int height) {
        Bitmap bitmap;
        int inSampleSize = 1;
        final BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res,id,opts);
        final int h = opts.outHeight, w = opts.outWidth;
        while (h/(2*inSampleSize) > height && w/(2*inSampleSize) > width) inSampleSize *= 2;
        opts.inJustDecodeBounds = false;
        opts.inSampleSize = inSampleSize;
        bitmap = BitmapFactory.decodeResource(res,id,opts);
        return bitmap;
    }

    public interface Updater {
        void updateImage(Img img);
    }

    private class LoaderTask extends AsyncTask<Integer, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Integer... dims) {
            return load(dims[0],dims[1]);
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            cache.put(id,bitmap);
            updater.updateImage(Img.this);
        }
    }
}
