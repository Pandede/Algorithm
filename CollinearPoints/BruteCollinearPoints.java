import edu.princeton.cs.algs4.StdDraw;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class BruteCollinearPoints {
    private final ArrayList<LineSegment> lines = new ArrayList<>();

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null)
            throw new IllegalArgumentException("Null set of points");

        for (int i = 0; i < points.length; i++)
            if (points[i] == null)
                throw new IllegalArgumentException("Null point in set");

        Point[] temp = points.clone();
        Arrays.sort(temp);
        final int n = temp.length;

        for (int i = 0; i < n - 1; i++)
            if (temp[i].compareTo(temp[i + 1]) == 0)
                throw new IllegalArgumentException("Repeated point");

        for (int p = 0; p < n - 3; p++) {
            Point pointP = temp[p];
            for (int q = p + 1; q < n - 2; q++) {
                Point pointQ = temp[q];
                double slopePQ = pointP.slopeTo(pointQ);
                for (int r = q + 1; r < n - 1; r++) {
                    Point pointR = temp[r];
                    double slopeQR = pointQ.slopeTo(pointR);
                    for (int s = r + 1; s < n; s++) {
                        Point pointS = temp[s];
                        double slopeRS = pointR.slopeTo(pointS);
                        if (slopePQ == slopeQR && slopeQR == slopeRS) {
                            lines.add(new LineSegment(pointP, pointS));
                        }
                    }
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return lines.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return lines.toArray(new LineSegment[numberOfSegments()]);
    }

    public static void main(String[] args) {
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32767);
        StdDraw.setYscale(0, 32767);

        // Read and draw points
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);

        Scanner scanner;
        try {
            scanner = new Scanner(new File(args[0]));
        } catch (FileNotFoundException e) {
            return;
        }
        int n = scanner.nextInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            points[i] = new Point(x, y);
            points[i].draw();
        }
        scanner.close();
        StdDraw.show();

        BruteCollinearPoints model = new BruteCollinearPoints(points);

        // Draw collinear lines
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.setPenRadius(0.001);
        for (LineSegment line : model.segments()) {
            System.out.println(line);
            line.draw();
        }
        StdDraw.show();
    }
}
