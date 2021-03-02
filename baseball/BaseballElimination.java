/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class BaseballElimination {

    private int numTeams;
    private int[][] against;
    private ArrayList<String> teams;
    private HashMap<String, ArrayList<Integer>> scores;
    private HashMap<String, Integer> indexOfTeam;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In information = new In(filename);
        this.teams = new ArrayList<String>();
        this.scores = new HashMap<String, ArrayList<Integer>>();
        this.numTeams = information.readInt();
        this.indexOfTeam = new HashMap<String, Integer>();
        this.against = new int[this.numTeams][this.numTeams];
        for (int i = 0; i < numTeams; i++) {
            String teamName = information.readString();
            this.indexOfTeam.put(teamName, i);
            this.teams.add(teamName);
            ArrayList<Integer> wwl = new ArrayList<Integer>();
            for (int j = 0; j < 3; j++) {
                int current = information.readInt();
                wwl.add(current);
            }
            this.scores.put(teamName, wwl);
            for (int j = 0; j < this.numTeams; j++) {
                int current = information.readInt();
                this.against[i][j] = current;
            }
        }
    }

    // number of teams
    public int numberOfTeams() {
        return this.numTeams;
    }

    public Iterable<String> teams() {
        return this.teams;
    }

    private void validateTeam(String team) {
        if (team == null || this.indexOfTeam.containsKey(team) == false) {
            throw new IllegalArgumentException();
        }
    }

    public int wins(String team) {
        validateTeam(team);
        return this.scores.get(team).get(0);
    }

    public int losses(String team) {
        validateTeam(team);
        return this.scores.get(team).get(1);
    }

    public int remaining(String team) {
        validateTeam(team);
        return this.scores.get(team).get(2);
    }

    public int against(String team1, String team2) {
        validateTeam(team1);
        validateTeam(team2);
        return this.against[this.indexOfTeam.get(team1)][this.indexOfTeam.get(team2)];
    }

    private static class Node {
        private FlowNetwork fn;
        private int maxFlowNeeded;
        private int gameVertices;
        private HashMap<Integer, Integer> vertexToTeam;

        public Node(FlowNetwork fn, int maxFlowNeeded) {
            this.fn = fn;
            this.maxFlowNeeded = maxFlowNeeded;
        }

        public void setGameVertices(int gameVertices) {
            this.gameVertices = gameVertices;
        }

        public FlowNetwork getFlowNetwork() {
            return this.fn;
        }

        public void setVertexToTeam(HashMap<Integer, Integer> vertexToTeam) {
            this.vertexToTeam = vertexToTeam;
        }

        public int getMaxFlowNeeded() {
            return this.maxFlowNeeded;
        }
    }

    private Node createNetwork(String team) {
        // need to find out the the distinct pairings that are left
        // that is not team
        int vertexNeeded = 2; // one for source, one for sink
        vertexNeeded += (this.numTeams - 1); // other teams
        int gameVertices = 0;
        for (int i = 0; i < this.against.length; i++) {
            for (int j = i + 1; j < this.against[i].length; j++) { // don't include alreadyS
                if (i != this.indexOfTeam.get(team) && j != this.indexOfTeam.get(team)) {
                    gameVertices++;
                    vertexNeeded++;
                }
            }
        }
        //System.out.println(vertexNeeded);
        //System.out.println(gameVertices);

        // map team index to graph vertex
        int starting = gameVertices + 1;
        HashMap<Integer, Integer> teamToVertex = new HashMap<Integer, Integer>();
        HashMap<Integer, Integer> vertexToTeam = new HashMap<Integer, Integer>();
        for (int i = 0; i < this.teams.size(); i++) {
            String teamm = this.teams.get(i);
            if (!teamm.equals(team)) {
                teamToVertex.put(this.indexOfTeam.get(teamm), starting);
                vertexToTeam.put(starting, this.indexOfTeam.get(teamm));
                starting++;
            }
        }

        FlowNetwork network = new FlowNetwork(vertexNeeded);
        // 0 is the source , vertexNeeded - 1 is the target
        int currentIndex = 0;
        int maxFlowNeeded = 0;
        for (int i = 0; i < this.against.length; i++) {
            for (int j = i + 1; j < this.against[i].length; j++) { // don't include alreadyS
                if (i != this.indexOfTeam.get(team) && j != this.indexOfTeam.get(team)) {
                    FlowEdge currentEdge = new FlowEdge(0, currentIndex + 1, this.against[i][j]);
                    maxFlowNeeded += this.against[i][j];
                    network.addEdge(currentEdge);
                    FlowEdge gtt0 = new FlowEdge(currentIndex + 1, teamToVertex.get(i),
                                                 Double.POSITIVE_INFINITY);
                    FlowEdge gtt1 = new FlowEdge(currentIndex + 1, teamToVertex.get(j),
                                                 Double.POSITIVE_INFINITY);
                    network.addEdge(gtt0);
                    network.addEdge(gtt1);
                    currentIndex++;
                }
            }
        }
        int maxWin = scores.get(team).get(0) + scores.get(team).get(2);
        // edges to team vertices to the sink
        for (int i = 0; i < this.teams.size(); i++) {
            String teamm = teams.get(i);
            if (!teamm.equals(team)) {
                int winCurrent = this.scores.get(teamm).get(0);
                int indexTeam = this.indexOfTeam.get(teamm);
                int subtracted = maxWin - winCurrent;
                if (subtracted < 0) {
                    return new Node(null, i);
                }
                FlowEdge fe = new FlowEdge(teamToVertex.get(indexTeam), vertexNeeded - 1,
                                           subtracted);
                network.addEdge(fe);
            }
        }
        Node node = new Node(network, maxFlowNeeded);
        node.setGameVertices(gameVertices);
        node.vertexToTeam = vertexToTeam;
        return node;
    }

    public boolean isEliminated(String team) {
        validateTeam(team);
        Node node = this.createNetwork(team);
        FlowNetwork fn = node.getFlowNetwork();
        if (fn == null) {
            // trivially eliminated
            return true;
        }
        FordFulkerson ff = new FordFulkerson(fn, 0, fn.V() - 1);
        return (ff.value() != node.getMaxFlowNeeded());
    }

    public Iterable<String> certificateOfElimination(String team) {
        validateTeam(team);
        HashSet<String> ans = new HashSet<>();
        if (!isEliminated(team)) {
            return null;
        }
        Node node = this.createNetwork(team);
        if (node.getFlowNetwork() == null) {
            // trivially eliminated but by whom
            ans.add(this.teams.get(node.getMaxFlowNeeded()));
            return ans;
        }
        FlowNetwork fw = node.getFlowNetwork();
        FordFulkerson ff = new FordFulkerson(fw, 0, fw.V() - 1);
        HashMap<Integer, Integer> vertexToTeam = node.vertexToTeam;
        Iterable<FlowEdge> al = fw.adj(0);
        for (int i : vertexToTeam.keySet()) {
            if (ff.inCut(i)) {
                ans.add(this.teams.get(vertexToTeam.get(i)));
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
