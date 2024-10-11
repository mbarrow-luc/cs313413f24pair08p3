package edu.luc.etl.cs313.android.shapes.android;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import edu.luc.etl.cs313.android.shapes.model.*;

/**
 * A Visitor for drawing a shape to an Android canvas.
 */
public class Draw implements Visitor<Void> {

    private final Canvas canvas;

    private final Paint paint;

    public Draw(final Canvas canvas, final Paint paint) {
        this.canvas = canvas;
        this.paint = paint;
        paint.setStyle(Style.STROKE);
    }

    @Override
    public Void onCircle(final Circle c) {

        canvas.drawCircle(0, 0, c.getRadius(), paint);

        return null;
    }

    @Override
    public Void onStrokeColor(final StrokeColor c) {

        var prevColor = paint.getColor();
        paint.setColor(c.getColor());
        c.getShape().accept(this);
        paint.setColor(prevColor);

        return null;
    }

    @Override
    public Void onFill(final Fill f) {

        paint.setStyle(Style.FILL_AND_STROKE);
        f.getShape().accept(this);
        paint.setStyle(Style.STROKE);

        return null;
    }

    @Override
    public Void onGroup(final Group g) {

        for (Shape s : g.getShapes()) {
            s.accept(this);
        }

        return null;
    }

    @Override
    public Void onLocation(final Location l) {

        var x = (float) l.getX();
        var y = (float) l.getY();
        canvas.translate(x, y);
        l.getShape().accept(this);
        canvas.translate(-x, -y);

        return null;
    }

    @Override
    public Void onRectangle(final Rectangle r) {

        canvas.drawRect(0, 0, r.getWidth(), r.getHeight(), paint);

        return null;
    }

    @Override
    public Void onOutline(Outline o) {

        paint.setStyle(Style.STROKE);
        o.getShape().accept(this);
        paint.setStyle(Style.STROKE);

        return null;
    }

    @Override
    public Void onPolygon(final Polygon s) {

        final float[] pts = null;

        canvas.drawLines(pts, paint);
        return null;
    }
}
