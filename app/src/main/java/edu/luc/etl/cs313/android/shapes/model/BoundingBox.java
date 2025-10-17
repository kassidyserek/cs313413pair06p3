package edu.luc.etl.cs313.android.shapes.model;

import android.graphics.Rect;

/**
 * A shape visitor for calculating the bounding box, that is, the smallest
 * rectangle containing the shape. The resulting bounding box is returned as a
 * rectangle at a specific location.
 */
public class BoundingBox implements Visitor<Location> {

    @Override
    public Location onCircle(final Circle c) {
        final int radius = c.getRadius();
        return new Location(-radius, -radius, new Rectangle(2 * radius, 2 * radius));
    }

    @Override
    public Location onFill(final Fill f) {
        return f.getShape().accept(this);
    } // done

    @Override
    public Location onGroup(final Group g) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Shape s : g.getShapes()){
            final Location box = s.accept(this);
            final Rectangle rect = (Rectangle) box.getShape();
            minX = Math.min(minX, box.getX());
            minY = Math.min(minY, box.getY());
            maxX = Math.max(maxX, box.getX() + rect.getWidth());
            maxY = Math.max(maxY, box.getY() + rect.getHeight());
        }
        return new Location(minX, minY, new Rectangle(maxX - minX, maxY - minY));
    } // done

    @Override
    public Location onLocation(final Location l) {
        final Location inner = l.getShape().accept(this);
        return new Location(
                l.getX() + inner.getX(),
                l.getY() + inner.getY(),
                inner.getShape()
        );
    } // done

    @Override
    public Location onRectangle(final Rectangle r) {
        return new Location(0,0,r);
    } // done

    @Override
    public Location onStrokeColor(final StrokeColor c) {
        return c.getShape().accept(this);
    } // done

    @Override
    public Location onOutline(final Outline o) {
        return o.getShape().accept(this);
    }

    @Override
    public Location onPolygon(final Polygon s) {
        return onGroup(s);
    } // done
}
