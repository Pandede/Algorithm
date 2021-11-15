import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE95 = 1.96;

    private final int trials;
    private final double[] prob;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException();
        this.trials = trials;
        this.prob = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation model = new Percolation(n);
            while (!model.percolates()) {
                int pRow = StdRandom.uniform(1, n + 1);
                int pCol = StdRandom.uniform(1, n + 1);
                model.open(pRow, pCol);
            }
            prob[i] = (double) model.numberOfOpenSites() / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(prob);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(prob);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - CONFIDENCE95 * stddev() / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + CONFIDENCE95 * stddev() / Math.sqrt(trials);
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats simulation = new PercolationStats(n, trials);
        System.out.println("mean\t=\t" + simulation.mean());
        System.out.println("stddev\t=\t" + simulation.stddev());
        System.out.println(
                "95% confidence interval\t=\t[" + simulation.confidenceLo() + ", " + simulation.confidenceHi() + "]");
    }
}