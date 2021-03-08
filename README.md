# Princeton-Projects
Programming assignments from the Algorithms 1 & 2 hosted by Princeton University

Author: Darren Yeung, Sophomore CS Student

**Percolation**: Determines if a NxM matrix pecolates (having a path from top to bottom) and computes the percolation threshold through Monte-Carlo Simulation  
    -Uses disjoint set data structure and weighted union-find wihout path-compression  
    -Top and bottom dummy node to reduce runtime from O(N^2) to O(1) when checking if there is a connection
     -Source files: Percolation.java PercolationStats.java PercolationVisualizer.java  
      
**Collinear points**: Given a graph with distinct points, determines which points form a line of 4 or more points and reproduce those lines without duplicates.   
    -Uses quick-sort to sort the slopes in ascending order in order to compute all lines in O(N^2 Lg(n)) rather than O(N^4)  
    -Source files: LineSegment.java Point.java  FastCollinearPoints.java  
    
**8Puzzle**: Using A* algorithm, find the number of moves to solve the 8 puzzle and compute the solution path  
    -Game: https://murhafsousli.github.io/8puzzle/#/  
    -Uses priority queue and hamming equations to compute the solution path   
    -Source files: Board.java Solver.java   

**KD-Tree**: Binary tree that is custom for multiple-dimension data types (e.g. points on a 2D plane)  
    -A data structure that follows the K-D algorithm restricted to 2 dimensional keys  
    -https://en.wikipedia.org/wiki/K-d_tree  
    -Source files: KdTree.java PointSET.java  

**Wordnet**: Given a directed graph of synsets (set of closely related words) and their paths to their hypernyms (more generalized synset),
compute shortest distances and their shortest ancestral path
    -Uses a directed graph data structure with breadth-first-search algorithm to compute the shortest distance and shortest ancestors   
    -Uses topological sort to check for cycles   
    -Computes the outdegrees to check if graph is rooted (one root only)  
    -Source files: WordNet.java SAP.java Digraph.java   
    
**Seam Carver**: Given an image, resize the image by removing vertical and horizontal seams while perserving maximum information <br />
    -Computes mathematical energy functions for each pixel related to its neighbors <br />
    -Computes the shortest energy path from top to bottom or left to right using topological sort <br />
    -Topological sort is utilized for computing the shortest path problem because the image is an edge weighted DAG (Directed Acyclic Graph)
    -Source files: SeamCarver.java

**Baseball Elimination Problem**: Given a set of teams, information about their win/loss count, and their remaining games against each other, 
determine which teams are mathematically eliminated and provide the teams that proves this <br />
    -Represent the teams and their remaining games as the max-flow min-cut problem (Dual Problems!) <br />
    -Source -> Game Verticies (without X) -> Team Verticies (without X) -> Sink where X is the team in question <br />
    -Use Fork Fulkerson to compute the max flow <br />
    -If edges from source are full, team X is not eliminated, else, X is eliminated <br />
    -Source files: BaseballElimination.java

**Boggle Game Solver**: Given a dictionary and a nXn board of letters, find all words in the dictionary that can be constructed 
from a path on the board using only a sequence "King" moves (left, right, top, down, diagonal) <br />
    -Fundemental key to this problem is to use DFS but to unmark the letter after the end of the recursive call <br />
    -DFS starting from every single letter to the rest of the board <br />
    -Key Optimization:
        -During the current DFS path, if we find out that there doesn't exist a prefix in the dictionary, we do not look further <br />
    -Other optimizations to get full credit: 
        -Memory usage is too big. Board only contains {A...Z} so implement own 26 way trie 
        -Further time improvements made by taking out repeated containSuffix() method 
            -Since we are looking at one character further at a time, we just need to keep track of the current trie node and check for null 
   


    

    
    
    
    
