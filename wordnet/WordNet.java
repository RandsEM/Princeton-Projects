/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Topological;

import java.util.HashMap;

public class WordNet {
    private HashMap<String, Bag<Integer>> map;
    private HashMap<Integer, String[]> other;
    private Digraph graph;
    private int clusters = 0;
    private SAP sap;


    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }
        this.map = new HashMap<>();
        this.other = new HashMap<>();
        In syn = new In(synsets);
        In hyp = new In(hypernyms);
        while (syn.hasNextLine()) {
            this.clusters++;
            String current = syn.readLine();
            String[] splitted = current.split(",", 3);
            int key = Integer.parseInt(splitted[0]);
            String[] words = splitted[1].split(" ", 0);
            this.other.put(key, words);
            for (String word : words) {
                if (!this.map.containsKey(word)) {
                    Bag<Integer> indices = new Bag<>();
                    indices.add(key);
                    this.map.put(word, indices);
                }
                else {
                    this.map.get(word).add(key);
                }
            }
        }
        this.graph = new Digraph(this.clusters);
        while (hyp.hasNextLine()) {
            String current = hyp.readLine();
            String[] splitted = current.split(",", 0);
            int from = Integer.parseInt(splitted[0]);
            for (int i = 1; i < splitted.length; i++) {
                int to = Integer.parseInt(splitted[i]);
                this.graph.addEdge(from, to);
            }
        }
        // check if graph is a rooted DAG
        Topological top = new Topological(this.graph);
        if (!top.hasOrder()) {
            throw new IllegalArgumentException();
        }
        int outs = 0;
        for (int i = 0; i < this.graph.V(); i++) {
            if (this.graph.outdegree(i) == 0) {
                outs++;
                if (outs > 1) {
                    throw new IllegalArgumentException();
                }
            }
        }
        this.sap = new SAP(this.graph);
    }

    public Iterable<String> nouns() {
        return this.map.keySet();
    }

    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }
        return this.map.containsKey(word);
    }

    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException();
        }
        if (!this.isNoun(nounA) || !this.isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        return this.sap.length(this.map.get(nounA), this.map.get(nounB));
    }

    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException();
        }
        if (!this.isNoun(nounA) || !this.isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        int index = this.sap.ancestor(this.map.get(nounA), this.map.get(nounB));
        StringBuilder sb = new StringBuilder();
        String[] array = this.other.get(index);
        for (String string : array) {
            sb.append(string);
            sb.append(" ");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("faggot");
        WordNet wd = new WordNet("synsets3.txt", "hypernyms3InvalidTwoRoots.txt");
    }

}
