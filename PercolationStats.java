import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

// Class for performing statistical analysis on Percolation simulations.
public class PercolationStats {

    // Constant for the normal distribution critical value used for
    // confidence intervals.
    private static final double NORMAL_CONSTANT = 1.96;

    // Array to store percolation thresholds for each trial.
    private double[] values;

    // Constructor to perform independent trials on an n-by-n grid.
    public PercolationStats(int n, int trials) {

        // Validate input parameters.
        if (n <= 0) throw new IllegalArgumentException("n should be greater than 0");
        if (trials <= 0) throw new IllegalArgumentException(
                "number of trials should be greater than 0");

        // Initialize the array to store percolation thresholds.
        values = new double[trials];

        // Conduct independent trials.
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);

            // Continue opening sites until the system percolates.
            while (!percolation.percolates()) {
                int row = StdRandom.uniformInt(n);
                int col = StdRandom.uniformInt(n);
                percolation.open(row, col);
            }

            // Calculate and store the percolation threshold for the trial.
            double fraction = (double) percolation.numberOfOpenSites() / (n * n);
            values[i] = fraction;
        }
    }

    // Calculate and return the sample mean of percolation thresholds.
    public double mean() {
        return StdStats.mean(values);
    }

    // Calculate and return the sample standard deviation of percolation thresholds.
    public double stddev() {
        return StdStats.stddev(values);
    }

    // Calculate and return the low endpoint of the 95% confidence interval.
    public double confidenceLow() {
        return mean() - (NORMAL_CONSTANT * stddev()) / Math.sqrt(values.length);
    }

    // Calculate and return the high endpoint of the 95% confidence interval.
    public double confidenceHigh() {
        return mean() + (NORMAL_CONSTANT * stddev()) / Math.sqrt(values.length);
    }

    // Main method for testing the PercolationStats class.
    public static void main(String[] args) {

        // Parse command-line arguments for grid size and number of trials.
        int n = Integer.parseInt(args[0]);
        int m = Integer.parseInt(args[1]);

        // Measure elapsed time using Stopwatch.
        Stopwatch watch = new Stopwatch();

        // Create PercolationStats object with specified grid size and
        // number of trials.
        PercolationStats stats = new PercolationStats(n, m);

        // Calculate elapsed time.
        double elapsedtime = watch.elapsedTime();

        // Print statistical results and elapsed time.
        StdOut.printf("%-16s = %.6f\n", "mean()", stats.mean());
        StdOut.printf("%-16s = %.6f\n", "stddev()", stats.stddev());
        StdOut.printf("%-16s = %.6f\n", "confidenceLow()", stats.confidenceLow());
        StdOut.printf("%-16s = %.6f\n", "confidenceHigh()", stats.confidenceHigh());
        StdOut.printf("%-16s = %.6f\n", "elapsed time", elapsedtime);
    }
}
