/* *****************************************************************************
 *  Name: Darren
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;

/* Key thing to utilize in this assignment is to dfs and un-mark at the end of
 * the recursive call
 */
public class BoggleSolver {
    private static int requiredLength = 3;
    private TrieWithPrefixQuery tpq;
    private BoggleBoard currentBoard;
    private HashSet<String> currentAnswers;
    private int numRows;
    private int numCols;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        this.tpq = new TrieWithPrefixQuery();
        for (int i = 0; i < dictionary.length; i++) {
            this.tpq.insert(dictionary[i]);
        }

        /*
            TrieWithPrefixQuery.Node[] array = tpq.getRoot().getLinks();
            String test = "QUE";
            TrieWithPrefixQuery.Node[] pointer = array;
            for (int i = 0; i < test.length(); i++) {
                System.out.println(pointer[test.charAt(i) - 65]);
                pointer = pointer[test.charAt(i) - 65].getLinks();
            }
            System.out.println(pointer[0]);
            System.out.println(tpq.validPrefix(test));
         */
    }

    /*
     * Helper method to un-mark and remove letter at the end of string builder
     */
    private static void unmark(int row, int col, boolean[][] marked, StringBuilder sb, boolean q) {
        marked[row][col] = false;
        sb.deleteCharAt(sb.length() - 1);
        if (q) {
            sb.deleteCharAt(sb.length() - 1);
        }
    }

    /*
     * Helper method to check  if should dfs into this node
     */
    private static boolean isValid(int row, int col, int rBounds, int cBounds, boolean[][] marked) {
        // check in bounds
        if (row < 0 || row > rBounds || col < 0 || col > cBounds) {
            return false;
        }
        if (marked[row][col]) { // if already visited, don't go
            return false;
        }
        return true;
    }

    /*
     * Helper class that implements the PrefixQuery method
     */
    private static class TrieWithPrefixQuery {
        private Node root;

        public TrieWithPrefixQuery() {
            this.root = new Node();
        }

        public Node getRoot() {
            return this.root;
        }

        public boolean contains(String s) {
            Node pointer = this.root;
            for (int i = 0; i < s.length(); i++) {
                if (pointer.getLinks()[s.charAt(i) - 65] == null) {
                    return false;
                }
                pointer = pointer.getLinks()[s.charAt(i) - 65];
            }
            return pointer.isWord();
        }


        /**
         * do need to recompute the prefix query at each step
         * use the fact that only checking one letter more each recursive call
         *
         * @deprecated
         */
        @Deprecated
        public boolean validPrefix(String s) {
            // if hit null, then is no longer a prefix
            Node pointer = this.root;
            for (int i = 0; i < s.length(); i++) {
                if (pointer.getLinks()[s.charAt(i) - 65] == null) {
                    return false;
                }
                else {
                    pointer = pointer.getLinks()[s.charAt(i) - 65];
                }
                if (s.charAt(i) == 'Q') {
                    // need to check again
                    if (pointer.getLinks()['U' - 65] == null) {
                        return false;
                    }
                    else {
                        pointer = pointer.getLinks()['U' - 65];
                        i++;
                    }
                }
            }
            return true;
        }

        public void insert(String s) {
            Node pointer = this.root;
            for (int i = 0; i < s.length(); i++) {
                char current = s.charAt(i);
                Node[] links = pointer.getLinks();
                if (links[current - 65] == null) {
                    links[current - 65] = new Node();
                }
                pointer = links[current - 65];
            }
            pointer.setWord(); // this should be fine
        }

        private static class Node {
            private Node[] links;
            private boolean isWord;

            public Node() {
                this.links = new Node[26];
                this.isWord = false;
            }

            public Node[] getLinks() {
                return this.links;
            }

            public void setWord() {
                this.isWord = true;
            }

            public boolean isWord() {
                return this.isWord;
            }
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
                dfs(i, j, marked, sb, this.tpq.root);
            }
        }
        return this.currentAnswers;
    }

    // Not an infinite loop, if don't optimize, will take forever
    private void dfs(int row, int col, boolean[][] marked, StringBuilder current,
                     TrieWithPrefixQuery.Node node) {
        marked[row][col] = true;
        boolean q = false;
        if (this.currentBoard.getLetter(row, col) == 'Q') {
            q = true;
            current.append("" + this.currentBoard.getLetter(row, col) + 'U');
            if (node.getLinks()['Q' - 65] == null) {
                unmark(row, col, marked, current, true);
                return;
            }
            else {
                node = node.getLinks()['Q' - 65];
                if (node.getLinks()['U' - 65] == null) {
                    unmark(row, col, marked, current, true);
                    return;
                }
            }
            // x2
            node = node.getLinks()['U' - 65];
        }
        else {
            current.append(this.currentBoard.getLetter(row, col));
            int index = current.charAt(current.length() - 1) - 65;
            if (node.getLinks()[index] == null) {
                unmark(row, col, marked, current, false);
                return;
            }
            node = node.getLinks()[index];
        }
        // convert to string to add
        String sb = current.toString();

        // if word is in dictionary and >= 3 in length
        if (sb.length() >= BoggleSolver.requiredLength && node.isWord()) {
            this.currentAnswers.add(sb);
        }

        // perform recursive calls x axis, y axis, and diagonally
        int rowBound = this.numRows - 1;
        int colBound = this.numCols - 1;
        for (int i = col - 1; i <= col + 1; i++) { // take care of top and bottom row
            if (isValid(row - 1, i, rowBound, colBound, marked)) {
                dfs(row - 1, i, marked, current, node);
            }
            if (isValid(row + 1, i, rowBound, colBound, marked)) {
                dfs(row + 1, i, marked, current, node);
            }
        }
        if (isValid(row, col - 1, rowBound, colBound, marked)) { // direct left
            dfs(row, col - 1, marked, current, node);
        }
        if (isValid(row, col + 1, rowBound, colBound, marked)) { // direct right
            dfs(row, col + 1, marked, current, node);
        }
        unmark(row, col, marked, current, q);
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!this.tpq.contains(word) || word.length() < 3) {
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
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
