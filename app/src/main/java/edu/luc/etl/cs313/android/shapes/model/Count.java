package edu.luc.etl.cs313.android.shapes.model;

/**
 * A visitor to compute the number of basic shapes in a (possibly complex)
 * shape.
 */
public class Count implements Visitor<Integer> {

    // TODO entirely your job

    @Override
    public Integer onPolygon(final Polygon p) {
        return 1;
    }

    @Override
    public Integer onCircle(final Circle c) {
        return 1;
    }

    @Override
    public Integer onGroup(final Group g) {
        int count = 0;
        for (Shape s : g.getShapes()) {
            count += s.accept(this);
        }
        return count;
    }

    @Override
    public Integer onRectangle(final Rectangle q) {
        return 1;
    }

    @Override
    public Integer onOutline(final Outline o) {
        Shape s = o.getShape();
        return s != null ? s.accept(this) : 0;
    }

    @Override
    public Integer onFill(final Fill c) {
        Shape s = c.getShape();
        return s != null ? s.accept(this) : 0;
    }

    @Override
    public Integer onLocation(final Location l) {
        Shape s = l.getShape();
        return s != null ? s.accept(this) : 0;
    }

    @Override
    public Integer onStrokeColor(final StrokeColor c) {
        Shape s = c.getShape();
        return s != null ? s.accept(this) : 0;
    }
}
