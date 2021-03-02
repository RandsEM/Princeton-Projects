/* *****************************************************************************
 *  Name: Darren
 **************************************************************************** */

import edu.princeton.cs.algs4.TrieSET;

import java.util.HashSet;

/* Key thing to utilize in this assignment is to dfs and un-mark at the end of
 * the recursive call
 */
public class BoggleSolver {
    private static int requiredLength = 3;
    private TrieSET dic;
    private BoggleBoard currentBoard;
    private HashSet<String> currentAnswers;
    private int numRows;
    private int numCols;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        this.dic = new TrieSET();
        for (int i = 0; i < dictionary.length; i++) {
            this.dic.add(dictionary[i]);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        this.currentBoard = board;
        this.numRows = board.rows();
        this.numCols = board.cols();
        this.currentAnswers = new HashSet<String>();
        // perform dfs starting from each node in the mxn board
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                boolean[][] marked = new boolean[board.rows()][board.cols()];
                StringBuilder sb = new StringBuilder(); // pass in an empty string
                dfs(i, j, marked, sb);
            }
        }
        return this.currentAnswers;
    }

    private void dfs(int row, int col, boolean[][] marked, StringBuilder current) {
        marked[row][col] = true;
        current.append(this.currentBoard.getLetter(row, col));
        String sb = current.toString();
        // if word is in dictionary and >= 3 in length
        if (sb.length() >= BoggleSolver.requiredLength && this.dic.contains(sb)) {
            this.currentAnswers.add(sb);
        }
        // perform recursive calls x axis, y axis, and diagonally
        int rowBound = this.numRows - 1;
        int colBound = this.numCols - 1;
        for (int i = col - 1; i <= row + 1; i++) { // take care of top and bottom row
            if (isValid(row - 1, i, rowBound, colBound, marked)) {
                dfs(row - 1, i, marked, current);
            }
            if (isValid(row + 1, i, rowBound, colBound, marked)) {
                dfs(row + 1, i, marked, current);
            }
        }
        if (isValid(row, col - 1, rowBound, colBound, marked)) { // direct left
            dfs(row, col - 1, marked, current);
        }
        if (isValid(row, col + 1, rowBound, colBound, marked)) { // direct right
            dfs(row, col + 1, marked, current);
        }

        // un-mark and remove last char from string builder
        current.deleteCharAt(current.length() - 1);
        marked[row][col] = false;
    }

    /*
     * Helper method to check  if should dfs into this node
     */
    private boolean isValid(int row, int col, int rBounds, int cBounds, boolean[][] marked) {
        // check in bounds
        if (row < 0 || row > rBounds || col < 0 || col > cBounds) {
            return false;
        }
        if (!marked[row][col]) { // check not already visited
            return false;
        }
        return true;
    }


    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!this.dic.contains(word)) {
            return 0;
        }
        if (word.length() == 3 || word.length() == 4) {
            return 1;
        }
        else if (word.length() == 5) {
            return 2;
        }
        else if (word.length() == 6) {
            return 3;
        }
        else if (word.length() == 7) {
            return 5;
        }
        else {
            return 11;
        }
    }

    public static void main(String[] args) {

    }
}
