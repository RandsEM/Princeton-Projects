/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class testing {
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        StdOut.println("Man: " + initial.manhattan());
        StdOut.println("Neighbors Below");
        for (Board board : initial.neighbors()) {
            StdOut.println("Neighbor's Man: " + board.manhattan());
            StdOut.print(board);
        }
    }
}
