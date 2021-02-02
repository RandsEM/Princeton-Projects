import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */
public class SeamCarver {
    private Picture picture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException();
        }
        this.picture = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return new Picture(this.picture);
    }

    // width of current picture
    public int width() {
        return this.picture.width();
    }

    // height of current picture
    public int height() {
        return this.picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        // check for illegal
        if (x < 0 || x >= this.picture.width() || y < 0 || y >= this.picture.height()) {
            throw new IllegalArgumentException();
        }
        // check for boarder
        if (x == 0 || x == this.picture.width() - 1 || y == 0 || y == this.picture.height() - 1) {
            return 1000;
        }
        double xCompute;
        double yCompute;
        Color first = this.picture.get(x + 1, y);
        Color second = this.picture.get(x - 1, y);
        Color third = this.picture.get(x, y + 1);
        Color fourth = this.picture.get(x, y - 1);
        int rx = first.getRed() - second.getRed();
        int gx = first.getGreen() - second.getGreen();
        int bx = first.getBlue() - second.getBlue();
        xCompute = Math.pow(rx, 2) + Math.pow(gx, 2) + Math.pow(bx, 2);
        int ry = third.getRed() - fourth.getRed();
        int gy = third.getGreen() - fourth.getGreen();
        int by = third.getBlue() - fourth.getBlue();
        yCompute = Math.pow(ry, 2) + Math.pow(gy, 2) + Math.pow(by, 2);

        return Math.sqrt(xCompute + yCompute);
    }

    // helper method to compute all current energies for every pixel
    private double[][] computeEnergies() {
        double[][] energies = new double[this.picture.height()][this.picture.width()];
        int count = 0;
        for (int i = 0; i < this.picture.height(); i++) {
            for (int j = 0; j < this.picture.width(); j++) {
                energies[i][j] = this.energy(j, i);
            }
        }
        return energies;
    }

    private void relax(double[][] energies, double[][] distTo, int[][] edgeTo, int x, int y) {
        if (y - 1 >= 0) { // has left down
            double energyAt = energies[x + 1][y - 1];
            if (distTo[x][y] + energyAt < distTo[x + 1][y - 1]) {
                distTo[x + 1][y - 1] = energyAt + distTo[x][y];
                edgeTo[x + 1][y - 1] = y;
            }
        }
        if (y + 1 < this.picture.width()) { // has right down
            double energyAt = energies[x + 1][y + 1];
            if (distTo[x][y] + energyAt < distTo[x + 1][y + 1]) {
                distTo[x + 1][y + 1] = energyAt + distTo[x][y];
                edgeTo[x + 1][y + 1] = y;
            }
        }

        // straight down
        double energyAt = energies[x + 1][y];
        if (distTo[x][y] + energyAt < distTo[x + 1][y]) {
            distTo[x + 1][y] = energyAt + distTo[x][y];
            edgeTo[x + 1][y] = y;
        }
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] energies = this.computeEnergies();
        double[][] distTo = new double[this.picture.height()][this.picture.width()];
        int[][] edgeTo = new int[this.picture.height()][this.picture.width()];

        // initialize distTo and edgeTo
        for (int i = 0; i < this.picture.width(); i++) {
            distTo[0][i] = 1000;
        }
        for (int i = 1; i < this.picture.height(); i++) {
            for (int j = 0; j < this.picture.width(); j++) {
                distTo[i][j] = Integer.MAX_VALUE; // set the rest to positive infinity
            }
        }
        for (int i = 0; i < this.picture.width(); i++) {
            edgeTo[0][i] = i;
        }

        for (int i = 0; i < this.picture.height() - 1; i++) {
            for (int j = 0; j < this.picture.width(); j++) {
                this.relax(energies, distTo, edgeTo, i, j);
            }
        }
        double min = Double.POSITIVE_INFINITY;
        int savedIndex = 0;
        for (int i = 0; i < this.picture.width(); i++) {
            if (distTo[this.picture.height() - 1][i] < min) {
                min = distTo[this.picture.height() - 1][i];
                savedIndex = i;
            }
        }
        int index = this.picture.height() - 1;
        int[] outArray = new int[this.picture.height()];
        outArray[index] = savedIndex;
        index--;
        for (int i = this.picture.height() - 1; i > 0; i--) {
            outArray[index] = edgeTo[i][savedIndex];
            savedIndex = edgeTo[i][savedIndex];
            index--;
        }
        return outArray;
    }

    private Picture transPose() {
        Picture transposed = new Picture(this.picture.height(), this.picture.width());
        for (int i = 0; i < transposed.height(); i++) {
            for (int j = 0; j < transposed.width(); j++) {
                transposed.set(j, i, this.picture.get(i, this.picture.height() - 1 - j));
            }
        }
        return transposed;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        this.picture = this.transPose();
        int[] path = this.findVerticalSeam();
        for (int i = 0; i < 3; i++) {
            this.picture = this.transPose();
        }
        for (int i = 0; i < path.length; i++) {
            path[i] = this.picture.height() - path[i] - 1;
        }
        return path;
    }


    private void validateSeamV(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException();
        }
        if (seam.length != this.picture.height()) {
            throw new IllegalArgumentException();
        }
        int previous = seam[0];
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= this.picture.width()) {
                throw new IllegalArgumentException();
            }
            if (i != 0) {
                if (!(Math.abs(seam[i] - previous) <= 1)) {
                    throw new IllegalArgumentException();
                }
            }
            previous = seam[i];
        }
    }

    private void validateSeamH(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException();
        }
        if (seam.length != this.picture.width()) {
            throw new IllegalArgumentException();
        }
        int previous = seam[0];
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= this.picture.height()) {
                throw new IllegalArgumentException();
            }
            if (i != 0) {
                if (!(Math.abs(seam[i] - previous) <= 1)) {
                    throw new IllegalArgumentException();
                }
            }
            previous = seam[i];
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        this.validateSeamH(seam);
        Picture out = new Picture(this.picture.width(), this.picture.height() - 1);
        for (int i = 0; i < seam.length; i++) {
            for (int j = 0; j < seam[i]; j++) {
                out.set(i, j, this.picture.get(i, j));
            }
            for (int j = seam[i]; j < out.height(); j++) {
                out.set(i, j, this.picture.get(i, j + 1));
            }
        }
        this.picture = out;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        this.validateSeamV(seam);
        Picture out = new Picture(this.picture.width() - 1, this.picture.height());
        for (int i = 0; i < seam.length; i++) {
            for (int j = 0; j < seam[i]; j++) {
                out.set(j, i, this.picture.get(j, i));
            }
            for (int j = seam[i]; j < out.width(); j++) {
                out.set(j, i, this.picture.get(j + 1, i));
            }
        }
        this.picture = out;
    }


    //  unit testing (optional)
    public static void main(String[] args) {
        Picture pic = new Picture("chameleon.png");
        System.out.println("jew");
        SeamCarver sc = new SeamCarver(pic);
        System.out.println("cunt");
        double[][] energies = sc.computeEnergies();
        System.out.println("fag");
        int[] array = sc.findHorizontalSeam();
        double sum = 0;
        for (int i = 0; i < array.length; i++) {
            sum += energies[array[i]][i];
        }
        System.out.println(sum);
    }
}


