/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;

public class KdTree {

    private int size;
    private Node root;
    private Point2D closest;

    private static class Node {
        private RectHV rect;
        private Point2D point;
        private Node left;
        private Node right;
        private boolean horizontal;

        public Node(Point2D point) {
            this.point = point;
        }

        public void setRect(double xmin, double ymin, double xmax, double ymax) {
            this.rect = new RectHV(xmin, ymin, xmax, ymax);
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public void setRight(Node right) {
            this.right = right;
        }

        public RectHV getRect() {
            return this.rect;
        }

        public void setHorizontal(boolean horizontal) {
            this.horizontal = horizontal;
        }

        public boolean getHorizontal() {
            return this.horizontal;
        }

        public Node getLeft() {
            return this.left;
        }

        public Node getRight() {
            return this.right;
        }

        public Point2D getPoint() {
            return this.point;
        }
    }

    public KdTree() {
        this.root = null;
        this.size = 0;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public int size() {
        return this.size;
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (this.contains(p)) {
            return;
        }
        if (this.root == null) {
            this.root = new Node(p);
            this.root.setRect(0, 0, 1, 1);
            this.root.setHorizontal(false);
        }
        else {
            Node pointer = this.root;
            boolean horizontal = false;
            while (pointer != null) {
                if (horizontal) {
                    // compare y
                    if (p.y() < pointer.getPoint().y()) {
                        if (pointer.getLeft() == null) {
                            pointer.setLeft(new Node(p));
                            pointer.getLeft().setHorizontal(false);
                            RectHV parentsRect = pointer.getRect();
                            pointer.getLeft().setRect(parentsRect.xmin(), parentsRect.ymin(),
                                                      parentsRect.xmax(), pointer.getPoint().y());
                            break;
                        }
                        pointer = pointer.getLeft();
                    }
                    else {
                        if (pointer.getRight() == null) {
                            pointer.setRight(new Node(p));
                            pointer.getRight().setHorizontal(false);
                            RectHV parentsRect = pointer.getRect();
                            pointer.getRight().setRect(parentsRect.xmin(), pointer.getPoint().y(),
                                                       parentsRect.xmax(), parentsRect.ymax());
                            break;
                        }
                        pointer = pointer.getRight();
                    }
                }
                else {
                    if (p.x() < pointer.getPoint().x()) {
                        if (pointer.getLeft() == null) {
                            pointer.setLeft(new Node(p));
                            pointer.getLeft().setHorizontal(true);
                            RectHV parentsRect = pointer.getRect();
                            pointer.getLeft().setRect(parentsRect.xmin(), parentsRect.ymin(),
                                                      pointer.getPoint().x(), parentsRect.ymax());
                            break;
                        }
                        pointer = pointer.getLeft();
                    }
                    else {
                        if (pointer.getRight() == null) {
                            pointer.setRight(new Node(p));
                            pointer.getRight().setHorizontal(true);
                            RectHV parentsRect = pointer.getRect();
                            pointer.getRight()
                                   .setRect(pointer.getPoint().x(), parentsRect.ymin(),
                                            parentsRect.xmax(), parentsRect.ymax());
                            break;
                        }
                        pointer = pointer.getRight();
                    }
                }
                horizontal = !horizontal;

            }
        }
        this.size++;
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (this.root == null) {
            return false;
        }
        Node pointer = this.root;
        boolean horizontal = false;
        while (!pointer.getPoint().equals(p)) {
            if (!horizontal) {
                if (p.x() < pointer.getPoint().x()) {
                    pointer = pointer.getLeft();
                }
                else {
                    pointer = pointer.getRight();
                }
            }
            else {
                if (p.y() < pointer.getPoint().y()) {
                    pointer = pointer.getLeft();
                }
                else {
                    pointer = pointer.getRight();
                }
            }
            if (pointer == null) {
                return false;
            }
            if (pointer.getPoint().equals(p)) {
                return true;
            }
            horizontal = !horizontal;
        }
        return true;
    }

    public void draw() {
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        ArrayList<Point2D> out = new ArrayList<Point2D>();
        this.rangeActual(out, this.root, rect);
        return out;
    }

    private void rangeActual(ArrayList<Point2D> same, Node node, RectHV query) {
        if (node == null || !node.getRect().intersects(query)) {
            return;
        }
        if (query.contains(node.getPoint())) {
            same.add(node.getPoint());
        }
        rangeActual(same, node.getLeft(), query);
        rangeActual(same, node.getRight(), query);
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (this.size == 0) {
            return null;
        }
        nearestActual(p, null, this.root);
        return this.closest;
    }

    private void nearestActual(Point2D query, Point2D closestSoFar, Node node) {
        if (node == this.root) {
            closestSoFar = node.getPoint();
            this.closest = closestSoFar;
        }
        if (node == null || this.closest.distanceTo(query) < node.getRect().distanceTo(query)) {
            // if (node == null || closestSoFar.distanceTo(query) < node.getRect().distanceTo(query)) {
            return;
        }

        if (node.getPoint().distanceTo(query) < query.distanceTo(this.closest)) {
            // if (node.getPoint().distanceTo(query) < query.distanceTo(closestSoFar)) {
            closestSoFar = node.getPoint();
            this.closest = closestSoFar;
        }
        if (node.getHorizontal()) {
            // compare y
            if (query.y() < node.getPoint().y()) {
                nearestActual(query, this.closest, node.getLeft());
                nearestActual(query, this.closest, node.getRight());
            }
            else {
                nearestActual(query, this.closest, node.getRight());
                nearestActual(query, this.closest, node.getLeft());
            }
        }
        else {
            if (query.x() < node.getPoint().x()) {
                nearestActual(query, this.closest, node.getLeft());
                nearestActual(query, this.closest, node.getRight());
            }
            else {
                nearestActual(query, this.closest, node.getRight());
                nearestActual(query, this.closest, node.getLeft());
            }
        }
    }

    public static void main(String[] args) {
        KdTree lmao = new KdTree();
        Point2D p = new Point2D(0.7, 0.2);
        Point2D p1 = new Point2D(0.5, 0.4);
        Point2D p2 = new Point2D(0.2, 0.3);
        Point2D p3 = new Point2D(0.4, 0.7);
        Point2D p4 = new Point2D(0.9, 0.6);
        lmao.insert(p);
        lmao.insert(p1);
        lmao.insert(p2);
        lmao.insert(p3);
        lmao.insert(p4);

        Point2D query = new Point2D(0.456, 0.732);
        System.out.println(lmao.nearest(query));
    }

    private static void inOrder(Node node) {
        if (node == null) {
            return;
        }
        inOrder(node.getLeft());
        System.out.println(node.getPoint());
        inOrder(node.getRight());
    }
}
