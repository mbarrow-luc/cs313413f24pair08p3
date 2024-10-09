package edu.luc.etl.cs313.android.shapes.model;

/**
 * A shape visitor for calculating the bounding box, that is, the smallest
 * rectangle containing the shape. The resulting bounding box is returned as a
 * rectangle at a specific location.
 */
public class BoundingBox implements Visitor<Location> {

    // TODO entirely your job (except onCircle)

    @Override
    public Location onCircle(final Circle c) {
        final int radius = c.getRadius();
        return new Location(-radius, -radius, new Rectangle(2 * radius, 2 * radius));
    }

    @Override
    public Location onFill(final Fill f) {
        return f.getShape().accept(this);
    }

    @Override
    public Location onGroup(final Group g) {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
        for (Shape shape : g.getShapes()) {
            Location boxShape = shape.accept(this);
            int x = boxShape.getX(), y = boxShape.getY();
            Rectangle rect = (Rectangle) boxShape.getShape();
            if (x < minX) minX = x;
            if (y < minY) minY = y;
            if (x + rect.getWidth() > maxX) maxX = x + rect.getWidth();
            if (y + rect.getHeight() > maxY) maxY = y + rect.getHeight();
        }
        int width = maxX - minX, height = maxY - minY;
        return new Location(minX, minY, new Rectangle(width, height));
    }

    @Override
    public Location onLocation(final Location l) {
        Location box = l.getShape().accept(this);
        int offsetX = l.getX();
        int offsetY = l.getY();

        // Adjust the bounding box by the Location's offset
        return new Location(box.getX() + offsetX, box.getY() + offsetY, box.getShape());
    }

    @Override
    public Location onRectangle(final Rectangle r) {
        return new Location(0, 0, r);
    }

    @Override
    public Location onStrokeColor(final StrokeColor c) {
        return c.getShape().accept(this);
    }

    @Override
    public Location onOutline(final Outline o) {
        return o.getShape().accept(this);
    }

    @Override
    public Location onPolygon(final Polygon s) {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
        for (Point vertex : s.getPoints()) {
            if (vertex.getX() < minX) minX = vertex.getX();
            if (vertex.getY() < minY) minY = vertex.getY();
            if (vertex.getX() > maxX) maxX = vertex.getX();
            if (vertex.getY() > maxY) maxY = vertex.getY();
        }
        // Calculate width and height based on the range of X and Y coordinates
        int width = maxX - minX;
        int height = maxY - minY;

        System.out.println("minX: " + minX + ", minY: " + minY);
        System.out.println("maxX: " + maxX + ", maxY: " + maxY);

        return new Location(minX, minY, new Rectangle(width, height));
    }
}
