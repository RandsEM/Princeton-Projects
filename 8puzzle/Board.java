/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Board {
    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)

    private final int[][] board;
    private final int dimension;

    public Board(int[][] tiles) {
        this.board = new int[tiles.length][tiles.length];
        this.dimension = tiles.length;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                this.board[i][j] = tiles[i][j];
            }
        }
    }

    // string representation of this board
    public String toString() {
        String out = this.dimension + "\n";
        for (int i = 0; i < this.board.length; i++) {
            out += " ";
            for (int j = 0; j < this.board.length; j++) {
                if (j == this.board.length - 1) {
                    out += this.board[i][j];
                }
                else {
                    out += this.board[i][j] + " ";
                }
            }
            out += "\n";
        }
        return out;
    }

    // board dimension n
    public int dimension() {
        return this.dimension;
    }

    // number of tiles out of place
    public int hamming() {
        int count = 1;
        int outOfOrder = 0;
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board.length; j++) {
                // last tile check
                if (i == this.board.length - 1 && j == this.board.length - 1) {
                    continue;
                }
                else {
                    if (this.board[i][j] != count) {
                        outOfOrder += 1;
                    }
                }
                count++;
            }
        }
        return outOfOrder;
    }


    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int total = 0;
        int count = 1;
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board.length; j++) {
                int current = this.board[i][j];
                if (current == 0) {
                    // testing: StdOut.println("zero skipped");
                    count++;
                    continue;
                }
                if (current != count) {
                    // some arithmetic
                    int actualY = current / this.dimension;
                    if (current % this.dimension == 0) {
                        // subtract 1 from actualY
                        actualY = actualY - 1;
                    }
                    int actualX = (current % this.dimension) - 1;
                    if (actualX == -1) {
                        actualX = this.dimension - 1;
                    }
                    // add up the x and y differences
                    int currentSum = Math.abs(actualX - j);
                    currentSum += Math.abs(actualY - i);
                    // testing: StdOut.println(currentSum);
                    total += currentSum;
                }
                else {
                    // testing: StdOut.println(0);
                }
                count++;
            }
        }
        return total;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return this.hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null) {
            return false;
        }
        if (this.getClass() != y.getClass()) {
            return false;
        }
        Board imposter = (Board) y;
        if (this.dimension != imposter.dimension) {
            return false;
        }
        for (int i = 0; i < this.dimension; i++) {
            for (int j = 0; j < this.dimension; j++) {
                if (this.board[i][j] != imposter.board[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> stack = new Stack<Board>();
        // find the zero element (blank space)
        int ithPosition = 0;
        int jthPosition = 0;
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board.length; j++) {
                if (this.board[i][j] == 0) {
                    ithPosition = i;
                    jthPosition = j;
                }
            }
        }
        if (!(ithPosition - 1 < 0)) {
            // means not at the top so flip top and current
            int[][] neighbor = Board.copyArray(this.board);
            int saved = neighbor[ithPosition][jthPosition];
            neighbor[ithPosition][jthPosition] = neighbor[ithPosition - 1][jthPosition];
            neighbor[ithPosition - 1][jthPosition] = saved;
            Board newBoard = new Board(neighbor);
            stack.push(newBoard);
        }
        if (ithPosition != this.board.length - 1) {
            // not at the bottom
            int[][] neighbor = Board.copyArray(this.board);
            int saved = neighbor[ithPosition][jthPosition];
            neighbor[ithPosition][jthPosition] = neighbor[ithPosition + 1][jthPosition];
            neighbor[ithPosition + 1][jthPosition] = saved;
            Board newBoard = new Board(neighbor);
            stack.push(newBoard);
        }

        if (jthPosition != 0) {
            // not at the left side of the wall
            int[][] neighbor = Board.copyArray(this.board);
            int saved = neighbor[ithPosition][jthPosition];
            neighbor[ithPosition][jthPosition] = neighbor[ithPosition][jthPosition - 1];
            neighbor[ithPosition][jthPosition - 1] = saved;
            Board newBoard = new Board(neighbor);
            stack.push(newBoard);
        }
        if (jthPosition != this.board.length - 1) {
            // not at the right side of the wall
            int[][] neighbor = Board.copyArray(this.board);
            int saved = neighbor[ithPosition][jthPosition];
            neighbor[ithPosition][jthPosition] = neighbor[ithPosition][jthPosition + 1];
            neighbor[ithPosition][jthPosition + 1] = saved;
            Board newBoard = new Board(neighbor);
            stack.push(newBoard);
        }
        return stack;
    }

    private static int[][] copyArray(int[][] array) {
        int[][] out = new int[array.length][array[0].length];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                out[i][j] = array[i][j];
            }
        }
        return out;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] array = new int[this.dimension][this.dimension];
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board.length; j++) {
                array[i][j] = this.board[i][j];
            }
        }
        if (this.board[0][0] != 0) {
            if (this.board[0][1] != 0) {
                // exchange first two
                array[0][0] = this.board[0][1];
                array[0][1] = this.board[0][0];
            }
            else {
                // exchange first and the one below it
                array[0][0] = this.board[1][0];
                array[1][0] = this.board[0][0];

            }
        }
        else {
            // look at the one to the right
            array[0][1] = this.board[1][1];
            array[1][1] = this.board[0][1];
        }
        Board out = new Board(array);
        return out;
    }


    public static void main(String[] args) {
        int[][] array = { { 1, 0, 3 }, { 4, 2, 5 }, { 7, 8, 6 } };
        Board board = new Board(array);
        StdOut.print(board.toString());
        StdOut.println("hamming: " + board.hamming());
        StdOut.println("manhattan: " + board.manhattan());
        StdOut.println("Dimension: " + board.dimension());
        StdOut.println("isgoal: " + board.isGoal());
        Iterable<Board> stack = board.neighbors();
        for (Board lmao : stack) {
            StdOut.print(lmao.toString());
        }
        Board twin = board.twin();
        StdOut.println("Twin:");
        StdOut.print(twin.toString());
        int[][] array2 = { { 1, 0, 3 }, { 4, 2, 5 }, { 7, 8, 6 } };
        Board board2 = new Board(array2);
        StdOut.println("Should be True then False");
        StdOut.println(board.equals(board2));
        StdOut.println(board.equals(twin));
    }
}
