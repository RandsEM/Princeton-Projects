# Princeton-Projects
Programming assignments from Algorithms 1 & 2 (Princeton)

**Percolation**: Determines if a NxM matrix pecolates (having a path from top to bottom) and computes the percolation threshold through Monte-Carlo Simulation  
    Uses disjoint set data structure and weighted union-find wihout path-compression  
    Source files: Percolation.java PercolationStats.java PercolationVisualizer.java  
      

**Collinear points**: Given a graph with distinct points, determines which points form a line of 4 or more points and reproduce those lines without duplicates.   
    Uses quick-sort to sort the slopes in ascending order in order to compute all lines in O(N^2 Lg(n)) rather than O(N^4)  
    Source files: LineSegment.java Point.java  FastCollinearPoints.java  
    
**8Puzzle**: Using A* algorithm, find the number of moves to solve the 8 puzzle and compute the solution path  
    Game: https://murhafsousli.github.io/8puzzle/#/  
    Uses priority queue and hamming equations to compute the solution path   
    Source files: Board.java Solver.java   

**KD-Tree**: Binary tree that is custom for multiple-dimension data types (e.g. points on a 2D plane)  
    A data structure that follows the K-D algorithm restricted to 2 dimensional keys  
    https://en.wikipedia.org/wiki/K-d_tree  
    Source files: KdTree.java PointSET.java  

**Wordnet**: Given a directed graph of synsets (set of closely related words) and their paths to their hypernyms (more generalized synset),
compute shortest distances and their shortest ancestral path
    Uses a directed graph data structure with breadth-first-search algorithm to compute the shortest distance and shortest ancestors   
    Uses topological sort to check for cycles   
    Computes the outdegrees to check if graph is rooted (one root only)  
    Source files: WordNet.java SAP.java Digraph.java   
    

    
    
    
    