import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private ArrayList<LineSegment> answers = new ArrayList<>();
    private int count = 0;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }

        // check if the array contains null values
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
        }

        Point[] copy = new Point[points.length];
        // check if the array contains duplicate Points
        // also sorts the array while doing so
        for (int i = 0; i < points.length; i++) {
            copy[i] = points[i];
        }
        Arrays.sort(copy);
        for (int i = 1; i < copy.length; i++) {
            if (copy[i].equals(copy[i - 1])) {
                throw new IllegalArgumentException();
            }
        }

        // need to check whether p->r == p->q == p->s
        for (int i = 0; i < copy.length; i++) {
            for (int j = i + 1; j < copy.length; j++) {
                for (int k = j + 1; k < copy.length; k++) {
                    for (int m = k + 1; m < copy.length; m++) {
                        // compute the slopes
                        double first = copy[i].slopeTo(copy[j]);
                        double second = copy[i].slopeTo(copy[k]);
                        double third = copy[i].slopeTo(copy[m]);
                        if (first == second && first == third) {
                            this.count++;
                            // create line segment;
                            LineSegment newLine = new LineSegment(copy[i], copy[m]);
                            this.answers.add(newLine);
                        }
                    }
                }
            }
        }
    }

    public int numberOfSegments() {
        return this.count;
    }

    public LineSegment[] segments() {
        LineSegment[] out = new LineSegment[this.answers.size()];
        for (int i = 0; i < this.answers.size(); i++) {
            out[i] = this.answers.get(i);
        }
        return out;
    }

}
