import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

public class SeamCarver {

    private Picture picture;
    private double[][] energyGrid;

    // create a seam carver object based the given picture
    public SeamCarver(Picture picture) {
        this.picture = picture;
        this.energyGrid = new double[height()][width()];

        // Initialize energy grid
        for (int col = 0; col < width(); col++)
            for (int row = 0; row < height(); row++)
                this.energyGrid[row][col] = energy(col, row);
    }

    // current picture
    public Picture picture() {
        return this.picture;
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (isBorder(x, y))
            return 1000.0;

        Color left = picture.get(x - 1, y);
        Color right = picture.get(x + 1, y);
        Color top = picture.get(x, y - 1);
        Color bottom = picture.get(x, y + 1);

        double dx = colorDiff(left, right);
        double dy = colorDiff(top, bottom);

        return Math.sqrt(dx + dy);
    }

    private double colorDiff(Color c1, Color c2) {
        int dr = c1.getRed() - c2.getRed();
        int dg = c1.getGreen() - c2.getGreen();
        int db = c1.getBlue() - c2.getBlue();
        return (double) (dr * dr + dg * dg + db * db);
    }

    private boolean isBorder(int x, int y) {
        return x == 0 || x == width() - 1 || y == 0 || y == height() - 1;
    }

    private void relaxEdge(
            int fromCol, int fromRow, int toCol, int toRow,
            double[][] distTo, int[][] edgeTo, double[][] energyGrid) {
        double nextEnergy = distTo[fromRow][fromCol] + energyGrid[toRow][toCol];
        if (nextEnergy < distTo[toRow][toCol]) {
            distTo[toRow][toCol] = nextEnergy;
            edgeTo[toRow][toCol] = fromRow;
        }
    }

    private int[] findSeam(double[][] energyGrid) {
        int height = energyGrid.length;
        int width = energyGrid[0].length;
        double[][] distTo = new double[height][width];
        int[][] edgeTo = new int[height][width];

        // Initialize distTo and edgeTo
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                distTo[row][col] = col == 0 ? 0 : Double.POSITIVE_INFINITY;
                edgeTo[row][col] = -1;
            }
        }

        // Run Dijkstra algorithm
        for (int col = 0; col < width - 1; col++) {
            for (int row = 0; row < height; row++) {
                // Right-top corner
                if (row - 1 >= 0)
                    relaxEdge(col, row, col + 1, row - 1, distTo, edgeTo, energyGrid);

                // Right
                relaxEdge(col, row, col + 1, row, distTo, edgeTo, energyGrid);

                // Right-Bottom corner
                if (row + 1 < height)
                    relaxEdge(col, row, col + 1, row + 1, distTo, edgeTo, energyGrid);
            }
        }

        // Find out the minimum path energy
        int minRow = 0;
        double minEnergy = distTo[minRow][width - 1];
        for (int row = 1; row < height; row++) {
            double curEnergy = distTo[row][width - 1];
            if (minEnergy > curEnergy) {
                minEnergy = curEnergy;
                minRow = row;
            }
        }

        // Backtracking
        int[] path = new int[width];
        int curRow = minRow;
        for (int col = width - 1; col > 0; col--) {
            path[col] = curRow;
            curRow = edgeTo[curRow][col];
        }
        path[0] = curRow;

        return path;
    }

    // sequence of indices for horizontal seam
    public int[] findVerticalSeam() {
        double[][] transposeEnergy = new double[width()][height()];
        for (int col = 0; col < width(); col++)
            for (int row = 0; row < height(); row++)
                transposeEnergy[col][row] = energyGrid[row][col];

        return findSeam(transposeEnergy);
    }

    // sequence of indices for vertical seam
    public int[] findHorizontalSeam() {
        return findSeam(energyGrid);
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        Picture updatedPicture = new Picture(width(), height() - 1);
        double[][] updatedEnergy = new double[height() - 1][width()];
        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < seam[col]; row++) {
                updatedPicture.set(col, row, picture.get(col, row));
                updatedEnergy[row][col] = energyGrid[row][col];
            }
            for (int row = seam[col] + 1; row < height(); row++) {
                updatedPicture.set(col, row - 1, picture.get(col, row));
                updatedEnergy[row - 1][col] = energyGrid[row][col];
            }
        }
        picture = updatedPicture;
        energyGrid = updatedEnergy;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        Picture updatedPicture = new Picture(width() - 1, height());
        double[][] updatedEnergy = new double[height()][width() - 1];
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < seam[row]; col++) {
                updatedPicture.set(col, row, picture.get(col, row));
                updatedEnergy[row][col] = energyGrid[row][col];

            }
            for (int col = seam[row] + 1; col < width(); col++) {
                updatedPicture.set(col - 1, row, picture.get(col, row));
                updatedEnergy[row][col - 1] = energyGrid[row][col];
            }
        }
        picture = updatedPicture;
        energyGrid = updatedEnergy;
    }

    // unit testing (optional)
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}