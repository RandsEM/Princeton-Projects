/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private int[] counts;
    private double mean;
    private double sd;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        this.counts = new int[trials];
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            while (percolation.percolates() == false) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                percolation.open(row, col);
            }
            this.counts[i] = percolation.numberOfOpenSites();
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        double mean = StdStats.mean(this.counts);
        this.mean = mean;
        return this.mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        double sd = StdStats.stddev(this.counts);
        this.sd = sd;
        return this.sd;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        double value = (1.96 * this.sd) / Math.sqrt(this.counts.length);
        double actual = this.mean - value;
        return actual;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double value = (1.96 * this.sd) / Math.sqrt(this.counts.length);
        double actual = this.mean + value;
        return actual;
    }

    public static void main(String[] args) {
        PercolationStats ps = new PercolationStats(Integer.parseInt(args[0]),
                                                   Integer.parseInt(args[1]));
        System.out.println("mean = " + ps.mean());
        System.out.println("stddev = " + ps.stddev());
        System.out.println(
                "95% confidence interval = [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
    }
}
