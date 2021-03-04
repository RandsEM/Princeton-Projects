# Princeton-Projects
Programming assignments from the Algorithms 1 & 2 hosted by Princeton University

Author: Darren Yeung, Sophomore 

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
    
**Seam Carver**: Given an image, resize the image by removing vertical and horizontal seams while perserving maximum information
    -Computes mathematical energy functions for each pixel related to its neighbors 
    -Computes the shortest energy path from top to bottom or left to right using topological sort 
    -Topological sort is utilized for computing the shortest path problem because the image is an edge weighted DAG (Directed Acyclic Graph)

**Baseball Elimination Problem**: Given a set of teams, information about their win/loss count, and their remaining games against each other, 
determine which teams are mathematically eliminated and provide the teams that proves this
    -Represent the teams and their remaining games 

**Boggle Game Solver**: IN PROGRESS
    

    
    
    
    
