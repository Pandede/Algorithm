import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private static final boolean VERTICAL = true;
    private static final boolean HORIZONTAL = false;

    private Node root;

    private class Node {

        private Point2D p; // the point
        private Node lb; // the left/bottom subtree
        private Node rt; // the right/top subtree
        private final boolean dir;
        private int count = 1;

        public Node(Point2D p, boolean dir) {
            this.p = p;
            this.dir = dir;
        }
    }

    public KdTree() {
        root = null;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null)
            return 0;
        return x.count;
    }

    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (!contains(p)) {
            root = insert(root, p, VERTICAL);
        }
    }

    private Node insert(Node h, Point2D p, boolean dir) {
        if (h == null)
            return new Node(p, dir);
        double rootX = h.p.x(), rootY = h.p.y();
        double curX = p.x(), curY = p.y();

        if (rootX == curX && rootY == curY)
            return h;
        if (h.dir == VERTICAL) {
            if (rootX > curX)
                h.lb = insert(h.lb, p, !dir);
            else
                h.rt = insert(h.rt, p, !dir);
        }
        if (h.dir == HORIZONTAL) {
            if (rootY > curY)
                h.lb = insert(h.lb, p, !dir);
            else
                h.rt = insert(h.rt, p, !dir);
        }
        h.count = 1 + size(h.lb) + size(h.rt);
        return h;
    }

    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        Node h = root;
        double curX = p.x(), curY = p.y();
        while (h != null) {
            double rootX = h.p.x(), rootY = h.p.y();
            if (h.dir == VERTICAL) {
                if (rootX > curX)
                    h = h.lb;
                else if (rootX < curX || rootY != curY)
                    h = h.rt;
                else
                    return true;
            } else {
                if (rootY > curY)
                    h = h.lb;
                else if (rootY < curY || rootX != curX)
                    h = h.rt;
                else
                    return true;
            }
        }
        return false;
    }

    public void draw() {
        draw(root, 0., 0., 1., 1.);
    }

    private void draw(Node h, double xmin, double ymin, double xmax, double ymax) {
        if (h == null)
            return;
        // Draw the point
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        h.p.draw();

        // Draw the line
        StdDraw.setPenRadius();
        double x = h.p.x(), y = h.p.y();
        if (h.dir == VERTICAL) {
            StdDraw.setPenColor(StdDraw.RED);
            Point2D start = new Point2D(x, ymin);
            Point2D goal = new Point2D(x, ymax);
            start.drawTo(goal);
            draw(h.lb, xmin, ymin, x, ymax);
            draw(h.rt, x, ymin, xmax, ymax);
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            Point2D start = new Point2D(xmin, y);
            Point2D goal = new Point2D(xmax, y);
            start.drawTo(goal);
            draw(h.lb, xmin, ymin, xmax, y);
            draw(h.rt, xmin, y, xmax, ymax);
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();
        ArrayList<Point2D> pointsInRect = new ArrayList<>();
        range(root, pointsInRect, rect, new RectHV(0., 0., 1., 1.));
        return pointsInRect;
    }

    private void range(Node h, ArrayList<Point2D> pointsInRect, RectHV targetRect, RectHV searchRect) {
        if (h == null)
            return;
        if (!searchRect.intersects(targetRect))
            return;
        if (targetRect.contains(h.p))
            pointsInRect.add(h.p);

        double xmin = searchRect.xmin(), ymin = searchRect.ymin(), xmax = searchRect.xmax(), ymax = searchRect.ymax();
        if (h.dir == VERTICAL) {
            double hx = h.p.x();
            range(h.lb, pointsInRect, targetRect, new RectHV(xmin, ymin, hx, ymax));
            range(h.rt, pointsInRect, targetRect, new RectHV(hx, ymin, xmax, ymax));
        } else {
            double hy = h.p.y();
            range(h.lb, pointsInRect, targetRect, new RectHV(xmin, ymin, xmax, hy));
            range(h.rt, pointsInRect, targetRect, new RectHV(xmin, hy, xmax, ymax));
        }
    }

    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (isEmpty())
            return null;
        Node nearestPoint = new Node(root.p, VERTICAL);
        nearest(root, nearestPoint, p, new RectHV(0., 0., 1., 1.));
        return nearestPoint.p;
    }

    private void nearest(Node h, Node nearestPoint, Point2D targetPoint, RectHV searchRect) {
        if (h == null)
            return;
        if (searchRect.distanceSquaredTo(targetPoint) >= nearestPoint.p.distanceSquaredTo(targetPoint))
            return;
        if (h.p.distanceSquaredTo(targetPoint) < nearestPoint.p.distanceSquaredTo(targetPoint))
            nearestPoint.p = h.p;

        double xmin = searchRect.xmin(), ymin = searchRect.ymin(), xmax = searchRect.xmax(), ymax = searchRect.ymax();
        if (h.dir == VERTICAL) {
            double hx = h.p.x();
            RectHV lRect = new RectHV(xmin, ymin, hx, ymax);
            RectHV rRect = new RectHV(hx, ymin, xmax, ymax);
            if (targetPoint.x() < hx) {
                nearest(h.lb, nearestPoint, targetPoint, lRect);
                nearest(h.rt, nearestPoint, targetPoint, rRect);
            } else {
                nearest(h.rt, nearestPoint, targetPoint, rRect);
                nearest(h.lb, nearestPoint, targetPoint, lRect);
            }

        } else {
            double hy = h.p.y();
            RectHV lRect = new RectHV(xmin, ymin, xmax, hy);
            RectHV rRect = new RectHV(xmin, hy, xmax, ymax);
            if (targetPoint.y() < hy) {
                nearest(h.lb, nearestPoint, targetPoint, lRect);
                nearest(h.rt, nearestPoint, targetPoint, rRect);
            } else {
                nearest(h.rt, nearestPoint, targetPoint, rRect);
                nearest(h.lb, nearestPoint, targetPoint, lRect);
            }
        }
    }
}