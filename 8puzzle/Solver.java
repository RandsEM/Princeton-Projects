/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    // find a solution to the initial board (using the A* algorithm)
    private int moves;
    private boolean solveAble;
    private Stack<Board> solutionSteps;


    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        this.moves = 0;
        this.solveAble = false;
        this.solutionSteps = new Stack<>();

        // make the solution reference
        int[][] solutionArray = new int[initial.dimension()][initial.dimension()];
        int count = 1;
        for (int i = 0; i < solutionArray.length; i++) {
            for (int j = 0; j < solutionArray[i].length; j++) {
                solutionArray[i][j] = count;
                count++;
            }
        }
        solutionArray[solutionArray.length - 1][solutionArray.length - 1] = 0;
        Board solution = new Board(solutionArray);

        // make the twin board and the priority queue for it
        Board twin = initial.twin();
        SearchNode twinFirst = new SearchNode(twin, null, 0);
        MinPQ<SearchNode> twinPq = new MinPQ<SearchNode>();
        twinPq.insert(twinFirst);

        // initialize the min priority queue
        MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
        SearchNode first = new SearchNode(initial, null, 0);
        pq.insert(first);

        boolean which = true;
        while (true) {
            if (which) { // run the initial board
                SearchNode current = pq.delMin();
                if (current.getBoard().manhattan() == 0) {
                    this.solveAble = true;
                    // need to remove the one that was added from the initial
                    SearchNode pointer = current;
                    while (pointer != null) {
                        this.solutionSteps.push(pointer.getBoard());
                        pointer = pointer.getPrevious();
                        this.moves++;
                    }
                    this.moves--;
                    break;
                }
                if (current.getPrevious() == null) {
                    // means we are at the first enqueued node
                    for (Board board : current.getBoard().neighbors()) {
                        SearchNode newNode = new SearchNode(board, current,
                                                            current.getStep() + 1);
                        pq.insert(newNode);
                    }
                }
                else {
                    for (Board board : current.getBoard().neighbors()) {
                        if (!board.equals(current.getPrevious().getBoard())) {
                            // enqueue only if they do not equal the previous
                            SearchNode newNode = new SearchNode(board, current,
                                                                current.getStep() + 1);
                            pq.insert(newNode);
                        }
                    }
                }
                which = !which;
            }
            else {
                // do the other one
                SearchNode current = twinPq.delMin();
                if (current.getBoard().manhattan() == 0) {
                    this.solveAble = false;
                    // need to remove the one that was added from the initial
                    SearchNode pointer = current;
                    while (pointer != null) {
                        pointer = pointer.getPrevious();
                    }
                    break;
                }
                if (current.getPrevious() == null) {
                    // means we are at the first enqueued node
                    for (Board board : current.getBoard().neighbors()) {
                        SearchNode newNode = new SearchNode(board, current,
                                                            current.getStep() + 1);
                        twinPq.insert(newNode);
                    }
                }
                else {
                    for (Board board : current.getBoard().neighbors()) {
                        if (!board.equals(current.getPrevious().getBoard())) {
                            // enqueue only if they do not equal the previous
                            SearchNode newNode = new SearchNode(board, current,
                                                                current.getStep() + 1);
                            twinPq.insert(newNode);
                        }
                    }
                }
                which = !which;
            }
        }
    }

    private static class SearchNode implements Comparable<SearchNode> {
        private int total;
        private Board myBoard;
        private SearchNode previous;
        private int step;

        public SearchNode(Board board, SearchNode previous, int step) {
            this.myBoard = board;
            this.previous = previous;
            this.step = step;
            this.total = board.manhattan() + step;
        }

        public int getTotal() {
            return this.total;
        }

        public int compareTo(SearchNode other) {

            if (this.total < other.total) {
                return -1;
            }
            else if (this.total > other.total) {
                return 1;
            }
            else {
                return 0;
            }
        }

        public Board getBoard() {
            return this.myBoard;
        }

        public int getStep() {
            return this.step;
        }

        public SearchNode getPrevious() {
            return this.previous;
        }
    }


    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solveAble;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!this.solveAble) {
            return -1;
        }
        return this.moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!this.solveAble) {
            return null;
        }
        return this.solutionSteps;
    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}

