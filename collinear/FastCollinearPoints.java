import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class FastCollinearPoints {

    private int num = 0;
    private ArrayList<LineSegment> answers;

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }

        // check if the array contains null values
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
        }

        // check if the array contains duplicate Points
        Point[] copy = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            copy[i] = points[i];
        }
        Arrays.sort(copy);
        for (int i = 1; i < copy.length; i++) {
            if (copy[i].equals(copy[i - 1])) {
                throw new IllegalArgumentException();
            }
        }
        this.answers = new ArrayList<>();
        // for all points, compute the slope to every other point and sort
        // I don't think there is a need to back trace
        // Actually, there is as need to backtrace LOL
        for (int i = 0; i < copy.length; i++) {
            ArrayList<Point> others = new ArrayList<>();
            // add the following points
            for (int j = i - 1; j >= 0; j--) {
                others.add(copy[j]);
            }
            for (int j = i + 1; j < copy.length; j++) {
                others.add(copy[j]);
            }
            // sort the points according to its slope with the current
            Collections.sort(others, copy[i].slopeOrder());

            for (int j = 0; j < others.size(); j++) {
                double currentSlope = copy[i].slopeTo(others.get(j));
                int count = 2;
                int k = j + 1;
                int saved = k;
                if (k >= others.size()) {
                    break;
                }
                while (others.get(k).slopeTo(copy[i]) == currentSlope) {
                    k++;
                    count++;
                    if (k >= others.size()) {
                        j = others.size();
                        break;
                    }
                }
                j = k - 1;
                if (count >= 4) {
                    // add line segment
                    if (copy[i].compareTo(others.get(k - 1)) < 0
                            && copy[i].compareTo(others.get(saved - 1)) > 0) {
                        continue;
                    }
                    else if (copy[i].compareTo(others.get(k - 1)) > 0) {
                        continue;
                    }
                    else {
                        this.num++;
                        LineSegment adding = new LineSegment(copy[i], others.get(k - 1));
                        this.answers.add(adding);
                    }
                }
            }
        }
        // StdOut.print("faggot"); no infinite loop
    }

    public int numberOfSegments() {
        return this.num;
    }

    public LineSegment[] segments() {
        LineSegment[] out = new LineSegment[this.answers.size()];
        for (int i = 0; i < this.answers.size(); i++) {
            out[i] = this.answers.get(i);
        }
        return out;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
