import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;

/* *****************************************************************************
 *  Name: Darren Yeung
 *  Date:
 *  Description:
 **************************************************************************** */
public class PointSET {

    // compareTo in Point2d compares by Y and break ties by x
    private SET<Point2D> storage;

    public PointSET() {
        this.storage = new SET<Point2D>();
    }

    public boolean isEmpty() {
        return (this.storage.isEmpty());
    }

    public int size() {
        return (this.storage.size());
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        this.storage.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return (this.storage.contains(p));
    }

    public void draw() {
        for (Point2D p : this.storage) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        ArrayList<Point2D> out = new ArrayList<>();
        for (Point2D p : this.storage) {
            if (p.y() <= rect.ymax() && p.y() >= rect.ymin()) {
                if (p.x() <= rect.xmax() && p.x() >= rect.xmin()) {
                    out.add(p);
                }
            }
        }
        return out;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (this.storage.size() == 0) {
            return null;
        }

        Point2D min = this.storage.min();
        for (Point2D point : this.storage) {
            if (point.distanceTo(p) < min.distanceTo(p)) {
                min = point;
            }
        }

        return min;
    }

    public static void main(String[] args) {

    }
}
