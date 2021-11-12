import java.util.Iterator;
import java.util.TreeSet;
import java.awt.Color;
import java.util.ArrayList;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {
    private final TreeSet<Point2D> tree;

    public PointSET() {
        tree = new TreeSet<>();
    }

    public boolean isEmpty() {
        return tree.isEmpty();
    }

    public int size() {
        return tree.size();
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        tree.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return tree.contains(p);
    }

    public void draw() {
        Iterator<Point2D> iter = tree.iterator();
        while (iter.hasNext()) {
            Point2D p = iter.next();
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        ArrayList<Point2D> pointsInRect = new ArrayList<>();
        Iterator<Point2D> iter = tree.iterator();
        while (iter.hasNext()) {
            Point2D p = iter.next();
            double x = p.x(), y = p.y();
            if (x >= rect.xmin() && x <= rect.xmax() && y >= rect.ymin() && y <= rect.ymax())
                pointsInRect.add(p);
        }
        return pointsInRect;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        Iterator<Point2D> iter = tree.iterator();
        double minDist = Double.POSITIVE_INFINITY;
        Point2D minPoint = null;
        while (iter.hasNext()) {
            Point2D currentPoint = iter.next();
            double dx = currentPoint.x() - p.x();
            double dy = currentPoint.y() - p.y();
            double currentDist = dx * dx + dy * dy;
            if (minDist > currentDist) {
                minPoint = currentPoint;
                minDist = currentDist;
            }
        }
        return minPoint;
    }

    public static void main(String[] args) {
        System.out.println("=====Debug=====");
        System.out.println("[Initialize]......");
        PointSET bag = new PointSET();
        System.out.println("[DONE]\n");

        System.out.println("[size]......");
        System.out.printf("size? (%d) %d\n", 0, bag.size());
        System.out.println("[DONE]\n");

        System.out.println("[isEmpty]......");
        System.out.printf("isEmpty? (true) %b\n", bag.isEmpty());
        System.out.println("[DONE]\n");

        System.out.println("[insert]......");
        while (!StdIn.isEmpty()) {
            double x = StdIn.readDouble();
            double y = StdIn.readDouble();
            bag.insert(new Point2D(x, y));
        }
        System.out.println("[DONE]\n");

        System.out.println("[size]......");
        System.out.printf("size? (%d) %d\n", 10, bag.size());
        System.out.println("[DONE]\n");

        System.out.println("[isEmpty]......");
        System.out.printf("isEmpty? (false) %b\n", bag.isEmpty());
        System.out.println("[DONE]\n");

        System.out.println("[contains]......");
        Point2D validPoint = new Point2D(0., 0.);
        Point2D invalidPoint = new Point2D(1., 1.);
        bag.insert(validPoint);

        System.out.println("[size]......");
        System.out.printf("size? (%d) %d\n", 11, bag.size());
        System.out.printf("[valid point] contains? (true) %b\n", bag.contains(validPoint));
        System.out.printf("[invalid point] contains? (false) %b\n", bag.contains(invalidPoint));
        System.out.println("[DONE]\n");

        // Draw all points
        System.out.println("[draw]......");
        StdDraw.setScale(0, 1);
        StdDraw.setPenRadius(0.01);
        bag.draw();
        System.out.println("[DONE]\n");

        System.out.println("[range]......");
        // Draw rectangle and included points
        RectHV rect = new RectHV(0.213, 0.115, 0.674, 0.562);
        rect.draw();
        StdDraw.setPenColor(Color.RED);
        for (Point2D p : bag.range(rect))
            p.draw();
        System.out.println("[DONE]\n");

        // Draw nearest point
        System.out.println("[nearest]......");
        Point2D p = new Point2D(0.698, 0.197);
        StdDraw.setPenColor(Color.BLUE);
        Point2D nearestPoint = bag.nearest(p);
        p.drawTo(nearestPoint);
        System.out.println("[DONE]\n");
    }
}