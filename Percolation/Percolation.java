import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int start, goal;
    private final int n;
    private int nOpened;
    private boolean[] grid;

    private final WeightedQuickUnionUF graphFull;
    private final WeightedQuickUnionUF graphTop;
    

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException();
        this.start = 0;
        this.goal = n * n + 1;
        this.n = n;
        grid = new boolean[n * n + 2];
        grid[start] = true; // Start (0)
        grid[goal] = true; // Goal (n*n+1)
        for (int i = 1; i < goal; i++)
            grid[i] = false;

        graphFull = new WeightedQuickUnionUF(n * n + 2); // Add start and goal
        graphTop = new WeightedQuickUnionUF(n * n + 1); // Only add start
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!isOpen(row, col)) {
            int id = flatten(row, col);
            if (row == 1) {
                graphFull.union(id, start);
                graphTop.union(id, start);
            }
            if (row == n)
                graphFull.union(id, goal);
            grid[id] = true;
            nOpened++;

            if (canOpen(row - 1, col) && isOpen(row - 1, col)) {
                int topId = flatten(row - 1, col);
                graphFull.union(id, topId);
                graphTop.union(id, topId);
            }
            if (canOpen(row + 1, col) && isOpen(row + 1, col)) {
                int bottomId = flatten(row + 1, col);
                graphFull.union(id, bottomId);
                graphTop.union(id, bottomId);
            }
            if (canOpen(row, col - 1) && isOpen(row, col - 1)) {
                int leftId = flatten(row, col - 1);
                graphFull.union(id, leftId);
                graphTop.union(id, leftId);
            }
            if (canOpen(row, col + 1) && isOpen(row, col + 1)) {
                int rightId = flatten(row, col + 1);
                graphFull.union(id, rightId);
                graphTop.union(id, rightId);
            }
        }
    }

    private boolean canOpen(int row, int col) {
        return row > 0 && row <= n && col > 0 && col <= n;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!canOpen(row, col))
            throw new IllegalArgumentException();
        return grid[flatten(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!canOpen(row, col))
            throw new IllegalArgumentException();
        return graphTop.find(flatten(row, col)) == graphTop.find(start);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return nOpened;
    }

    private int flatten(int row, int col) {
        return (row - 1) * n + col;
    }

    // does the system percolate?
    public boolean percolates() {
        return graphFull.find(start) == graphFull.find(goal);
    }
}
