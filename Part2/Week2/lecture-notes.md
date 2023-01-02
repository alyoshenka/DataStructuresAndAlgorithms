# Lecture Notes

## Minimum Spanning Trees

### Intro to MSTs

spanning tree: subgraph that is both a tree (connected and acyclic) and spanning (includes all the vertices)

brute force (impractical) -> try all possible spanning trees

#### MST is a fundamental problem with diverse applications:
- dithering
- cluster analysis
- max bottleneck paths
- real-time face verification
- LDPC codes for error correction
- image registration with Reyni entropy
- find road networks in satellite and aerial imagery
- reducing data storage in sequencing amino acids in a protein
- model locality of particle interactions in turbulent fluid flows
- autoconfig protocol for Ethernet bridging to avoid cycles in a network
- approximation algorithms for NP-hard problems (eg TSP, Steiner tree)
- network design (communication, electrical, hydraulic, computer, road)

### Greedy Algorithm

#### Simplifying Assumptions
- edge weights are distinct
- graph is connected
#### Consequence
- MST exists and is unique

cut: a partition of vertices into two nonempty sets

crossing edge: connects a vertex in one set with a vertex in the other

#### Cut Property
given any cut, the crossing edge of minimum weight is in the MST

### Kruskal's Algorithm
**consider edges in ascending order of weight**
- add next edge to tree unless doing so would create a cycle

challenge: will adding a specific edge create a cycle?

solution: `union-find` data structure
- maintain a set for each connected component in *T* (the MST)
  - if `v` and `w` are in the same set, then `v-w` would create a cycle
- to add `v-w` to *T*, merge sets containing `v` and `w`

#### Running time: E log E

*log\*V <= 5*

### Prim's Algorithm
1. start with vertex 0 and greedily grow tree 
1. add to tree the minimum weight edge with exactly one endpoint in the tree
1. repeat until there are V-1 edges

#### Eager Solution
Maintain a priority queue of vertices connected by an edge to the tree, where the priority of a vertex is equal to the weight of the shortest edge connecting that vertex to the tree (pq has at most one entry per vertex)
1. delete min vertex `v` and add its associated edge `e = v-w` to the tree
1. update PQ by considering all edges `e = v-w` incident to `v`
  - ignore if `x` is already in the tree
  - add `x` to PQ if it is not already on it
  - *decrease priority* of `x` if `v-x` becomes shortest edge connecting `x` to the tree

## Shortest Paths

### Shortest Paths APIs

which vertices?
- source-sink: from one vertex to another
- single source: from one vertex to every other
- all pairs: between all pairs of vertices

### Shortest Path Properties

#### Edge Relaxation

if `e = v->w` gives a shorter path to `w` through `v`, update both `distTo[w]` and `edgeTo[w]`

#### Optimality Conditions

for each edge `e = v->w`, `distTo[w]` <= `distTo[v]` + `e.weight()`

#### Generic SP Algorithm
1. initialize distance to 0 for the source and distance to infinity for all other vertices
1. repeat until optimality conditions are satisfied
  - relax any edge

#### Efficient Implementations
- nonnegative weights: Djikstra
- no directed cycles: Topological sort
- no negative cycles: Bellman-Ford

### Djikstra's Algorithm

1. consider vertices in increasing order of distance from `s`
1. add vertex to tree and relax all edges pointing from that vertex

- Prim's Algorithm -> closest vertex to *tree*
- Dijkstra's Algorithm -> closest vertex to *source*

### Edge-Weighted DAGs

Q: Suppose that an edge-weighted digraph has no directed cycles. Is it easier to find shortest paths than in a general digraph?

A: Yes!

1. Consider all vertices in topological order
1. Relax all edges pointing from that vertex

*Topological sort algorithm computes SPT in any* (edge weights can be negative) *edge-weighted DAG in time proportional to* **E + V**.

3:45