/* *****************************************************************************
 *  Name:              Darren Yeung
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;
// import java.lang.Math;

public class Percolation {
    // object types will be null by default. But primitive types will have values;
    private int length;
    private int gridSize;
    private boolean[][] grid;
    private int openedSites;
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF realUf;
    private int topVirtual;
    private int bottomVirtual;
    private int rightBotCorner;
    private int leftBotCorner;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) { // size must be at least 1
            throw new IllegalArgumentException();
        }
        this.gridSize = n * n;
        this.length = n;
        this.grid = new boolean[n][n];
        // last two will be the virtual sites, top and bottom respectively
        this.uf = new WeightedQuickUnionUF((n * n) + 2);

        this.topVirtual = n * n; // 3x3 grid have 9 spots. Start count from 0. Then one
        this.bottomVirtual = (n * n) + 1;
        this.rightBotCorner = (n * n) - 1;
        this.leftBotCorner = (n * (n - 1));
        this.realUf = new WeightedQuickUnionUF((n * n) + 2); // need  this to prevent back wash

        /*
        Back wash: When it shows a bottom portion is connected to the top
        row when it really isn't.  This happens when some site are opened
        on the bottom, not actually connected to the top, but because its connected
        to the virtualBottom which is connected by some path to the top to the virtualTop,
        it shows that it has a path to the top. Not True. Fix this by using two
        Quick-Union objects. One is the regular version. the other one, we do
        not connect the bottom row to the virtualBottom.
         */
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row <= 0 || row > this.length || col <= 0 || col > this.length) {
            throw new IllegalArgumentException();
        }
        if (this.grid[row - 1][col - 1] == false) {
            this.openedSites++;
            this.grid[row - 1][col - 1] = true;

            // check for 4 open sites
            if (row - 1 != 0) { // means not at top
                if (this.isOpen(row - 1, col) == true) {
                    this.uf.union(this.convert(row, col), this.convert(row - 1, col));
                    this.realUf.union(this.convert(row, col), this.convert(row - 1, col));
                }
            }
            else { // at top, union with topVirtual
                this.uf.union(this.topVirtual, this.convert(row, col));
                this.realUf.union(this.topVirtual, this.convert(row, col));
            }

            if (!(row + 1 > this.length)) { // means not at bottom
                if (this.isOpen(row + 1, col) == true) {
                    this.uf.union(this.convert(row, col), this.convert(row + 1, col));
                    this.realUf.union(this.convert(row, col), this.convert(row + 1, col));
                }
            }
            else { // at bottom, union with bottom virtual
                this.uf.union(this.bottomVirtual, this.convert(row, col));
            }

            if (col - 1 != 0) { // means not at far left
                if (this.isOpen(row, col - 1) == true) {
                    this.uf.union(this.convert(row, col), this.convert(row, col - 1));
                    this.realUf.union(this.convert(row, col), this.convert(row, col - 1));
                }
            }
            if (!(col + 1 > this.length)) { // means not at far right
                if (this.isOpen(row, col + 1) == true) {
                    this.uf.union(this.convert(row, col), this.convert(row, col + 1));
                    this.realUf.union(this.convert(row, col), this.convert(row, col + 1));
                }
            }
        }
    }

    private int convert(int row, int col) {
        int rowActual = row - 1;
        int colActual = col - 1;
        int index1D = rowActual * (int) Math.sqrt(this.gridSize) + colActual;
        return index1D;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row <= 0 || row > this.length || col <= 0 || col > this.length) {
            throw new IllegalArgumentException();
        }
        return this.grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row <= 0 || row > this.length || col <= 0 || col > this.length) {
            throw new IllegalArgumentException();
        }

        if (this.realUf.find(this.convert(row, col)) == this.realUf.find(this.topVirtual)) {
            return true;
        }
        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return this.openedSites;
    }

    // does the system percolate?
    public boolean percolates() {
        if (this.uf.find(this.topVirtual) == this.uf.find(this.bottomVirtual)) {
            return true;
        }
        return false;
    }
    // test client (optional)
}

