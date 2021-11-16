import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import edu.princeton.cs.algs4.StdDraw;

public class FastCollinearPoints {
    private final ArrayList<LineSegment> lines = new ArrayList<>();

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null)
            throw new IllegalArgumentException("Null set of points");

        for (int i = 0; i < points.length; i++)
            if (points[i] == null)
                throw new IllegalArgumentException("Null point in set");

        Point[] temp = points.clone();
        Arrays.sort(temp);
        final int n = points.length;

        for (int i = 0; i < n - 1; i++)
            if (temp[i].compareTo(temp[i + 1]) == 0)
                throw new IllegalArgumentException("Repeated point");

        for (int i = 0; i < n; i++) {
            Arrays.sort(temp);
            Arrays.sort(temp, temp[i].slopeOrder());

            Point currentPoint = temp[0];
            int head = 1;
            for (int tail = 2; tail < n; tail++) {
                while (tail < n && currentPoint.slopeTo(temp[head]) == currentPoint.slopeTo(temp[tail]))
                    tail++;

                if (tail - head >= 3 && currentPoint.compareTo(temp[head]) < 0)
                    lines.add(new LineSegment(currentPoint, temp[tail - 1]));

                head = tail;
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

        FastCollinearPoints model = new FastCollinearPoints(points);

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