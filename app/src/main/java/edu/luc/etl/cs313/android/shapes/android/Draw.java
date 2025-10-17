package edu.luc.etl.cs313.android.shapes.android;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import edu.luc.etl.cs313.android.shapes.model.*;

/**
 * A Visitor for drawing a shape to an Android canvas.
 */
public class Draw implements Visitor<Void> {

    // TODO entirely your job (except onCircle)

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
        int oldColor = paint.getColor(); //save current paint color
        paint.setColor(c.getColor()); //set new stroke color
        c.getShape().accept(this); //draw inner shape w/ new color
        paint.setColor(oldColor); //restore original color
        return null;
    }

    @Override
    public Void onFill(final Fill f) {
        Style oldStyle = paint.getStyle(); // save current paint style
        paint.setStyle(Style.FILL_AND_STROKE); //set style to fill and stroke for filled shapes
        f.getShape().accept(this); //draw inner shape with fill
        paint.setStyle(oldStyle); //restore original paint style
        return null;
    }

    @Override
    public Void onGroup(final Group g) {
        for (Shape s : g.getShapes()) { //iterate over all shapes in group
            s.accept(this); //recursively draw each shape
        }
        return null;
    }

    @Override
    public Void onLocation(final Location l) {
        canvas.translate(l.getX(), l.getY()); //move canvas origin to shape's location
        l.getShape().accept(this); //recursively draw shape at new origin
        canvas.translate(-l.getX(), -l.getY()); //restore canvas origin after drawing
        return null;
    }

    @Override
    public Void onRectangle(final Rectangle r) {
        canvas.drawRect(0, 0, r.getWidth(), r.getHeight(), paint); //draw rectangle at origin
        return null;
    }

    @Override
    public Void onOutline(Outline o) {
        Style oldStyle = paint.getStyle(); //save current paint style
        paint.setStyle(Style.STROKE); //set style to stroke for outline-only drawings
        o.getShape().accept(this); //recursively draw inner shape with outline
        paint.setStyle(oldStyle); //restore original paint style
        return null;
    }

    @Override
    public Void onPolygon(final Polygon s) {
        int n = s.getPoints().size(); //get number of points in polygon
        if (n < 2) return null; //if fewer than 2, nothing to draw

        float[] pts = new float[n * 4]; //create array to hold line segment coordinates
        for (int i = 0; i < n; i++) {
            Point start = s.getPoints().get(i); //get current point
            Point end = s.getPoints().get((i + 1) % n); //get next point
            pts[i*4] = start.getX(); //start X
            pts[i*4+1] = start.getY(); //start Y
            pts[i*4+2] = end.getX(); //end x
            pts[i*4+3] = end.getY(); //end y
        }

        canvas.drawLines(pts, paint); //draw all line segments as closed polygon
        return null;
    }
}