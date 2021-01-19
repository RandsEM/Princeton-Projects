/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {

    private Digraph graph;

    public SAP(Digraph G) {
        this.graph = new Digraph(G);
    }

    private static class Node {
        private final int dist;
        private final int ancestor;

        Node(int dist, int ancestor) {
            this.dist = dist;
            this.ancestor = ancestor;
        }

        public int getDist() {
            return this.dist;
        }

        public int getAncestor() {
            return this.ancestor;
        }
    }

    private static Node shortestAncestor(int v, int w, Digraph g) {
        Node out;
        if (v == w) {
            out = new Node(0, 0);
            return out;
        }
        BreadthFirstDirectedPaths fromV = new BreadthFirstDirectedPaths(g, v);
        BreadthFirstDirectedPaths fromW = new BreadthFirstDirectedPaths(g, w);

        int min = Integer.MAX_VALUE;
        int track = 0;

        for (int i = 0; i < g.V(); i++) {
            if (fromV.hasPathTo(i) && fromW.hasPathTo(i)) {
                if (fromV.distTo(i) + fromW.distTo(i) < min) {
                    min = fromV.distTo(i) + fromW.distTo(i);
                    track = i;
                }
            }
        }

        if (min == Integer.MAX_VALUE) {
            out = new Node(-1, -1);
        }
        else {
            out = new Node(min, track);
        }
        return out;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || v >= this.graph.V() || w < 0 || w >= this.graph.V()) {
            throw new IllegalArgumentException();
        }
        Node ans = SAP.shortestAncestor(v, w, this.graph);
        return ans.getDist();
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || v >= this.graph.V() || w < 0 || w >= this.graph.V()) {
            throw new IllegalArgumentException();
        }
        Node ans = SAP.shortestAncestor(v, w, this.graph);
        return ans.getAncestor();
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    private static Node shortestAncestorMultiple(Iterable<Integer> v, Iterable<Integer> w,
                                                 Digraph g) {
        Node out;
        BreadthFirstDirectedPaths fromW = new BreadthFirstDirectedPaths(g, v);
        BreadthFirstDirectedPaths fromV = new BreadthFirstDirectedPaths(g, w);
        int min = Integer.MAX_VALUE;
        int ances = 0;
        for (int i = 0; i < g.V(); i++) {
            if (fromW.hasPathTo(i) && fromV.hasPathTo(i)) {
                if (fromW.distTo(i) + fromV.distTo(i) < min) {
                    min = fromW.distTo(i) + fromV.distTo(i);
                    ances = i;
                }
            }
        }
        out = new Node(min, ances);
        return out;
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }
        for (Integer i : v) {
            if (i == null) {
                throw new IllegalArgumentException();
            }
        }
        for (Integer j : v) {
            if (j == null) {
                throw new IllegalArgumentException();
            }
        }
        Node ans = shortestAncestorMultiple(v, w, this.graph);
        return ans.getDist();
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }
        for (Integer i : v) {
            if (i == null) {
                throw new IllegalArgumentException();
            }
        }
        for (Integer j : v) {
            if (j == null) {
                throw new IllegalArgumentException();
            }
        }
        Node ans = shortestAncestorMultiple(v, w, this.graph);
        return ans.getAncestor();
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
