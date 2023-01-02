# Week 2 - Minimum Spanning Trees & Shortest Paths

## Ch 4.3: Minimum Spanning Trees

- edge-weighted graph: graph model that associates *weights* or *costs* with each edge
- spanning tree (of a graph): connectes subgraph with no cycles that includes all the vertices
- minimum spanning tree (MST): spanning tree whose height (the sum of the weights of its edges) is no larger than the weight of any other spanning tree

### Underlying Principles
1. adding an edge that connects two vertices in a tree creates a unique cycle
1. removing an edge from a tree breaks it into two separate subtrees

### Cut Property
- given any cut in an edge-weighted graph, the crossing edge of minimum weight is in the MST of the graph

### Greedy algorithm
- apply the cut property to accept an edge as an MDT edge, continuing until finding all of the MST edges

### Edge-weighted graph data type

```java
public class Edge implements Comparable<Edge> {
    // one vertex
    private final int v;
    // the other vertex
    private final int w;
    // the edge weight
    private final double weight;

    // initializing constructor
    Edge(int v, int w, double weight) {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    // weight of this edge
    double weight() { return weight; }

    // either of this edge's vertices
    int either() { return v; }

    // the other vertex
    int other(int vertex) {
        if (vertex == v) { return w; }
        else if (vertex == w) { return v; }
        else throw new RuntimeException("Inconsistent edge");
    }

    // compare this edge to e
    int compareTo(Edge that) {
        if (this.wieight() < that.weight()) return -1;
        else if (this.weight() > that.weight()) return 1;
        else return 0;
    }
}

public class EdgeWeightedGraph {
    // number of vertices
    private final int V;
    // number of edges
    private final int E;
    // adjacency lists
    private Bag<Edge>[] adj;

    // create an empty V-vertex graph
    EdgeWeightedGraph(int V) {
        this.V = V;
        this.E = 0;
        adj = (Bag<Edge>[]) new Bag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new Bag<Edge>();
        }
    }

    // number of vertices
    int V() { return V; }

    // number of edges
    int E() { return E; }

    // add edge E to this graph
    void addEdge(Edge e) {
        int v = e.either(), w = e.other(v);
        adj[v].add(e);
        adj[w].add(e);
        E++;
    }

    // edges incident to v
    Iterable<Edge> adj(int v) { return adj[v]; }

    // all of this graph's edges
    public Iterable<Edge> edges() {
        Bag<Edge> b = new Bag<Edge>();
        for(int v = 0; v < V; v++) {
            for (Edge e : adj[v]) {
                if (e.other(v) > v) {
                    b.add(e);
                }
            }
        }
        return b;
    }
}

public class MST {
    // constructor
    public MST(EdgeWeightedGraph G)

    // all of the MST edges
    Iterable<Edge> edges()

    // weight of the MST
    double weight()
}
```

### Prim's Algorithm
*attach a new edge to a single growing tree at each step*

1. start with any vertex as a single-vertex tree
1. add V-1 edges to it, always taking next the minimum-weight edge that connects a vertex on the tree to a vertex not yet on the tree

#### Data Structures
**"Which is the minimum weight crossing edge?"**

- vertices on the tree: vertex-indexed boolean array `marked[]`, where `marked[v]` is `true` if `v` is on the tree
- edges on the tree: queue `mst` to collect MST edges / or / vertex-indexed array `edgeTo[]` of `Edge` objects, where `edgeTo[v]` is the `Edge` that connects `v` to the tree
- crossing edges: `MinPQ<Edge>` priority queue that compares edges by weight

#### Running time (Lazy version)
- space: proportional to E
- time: proportional to E log E (in the worst case)

```java
public class LazyPrimMST {
    // MST vertices
    private boolean marked[];
    // MST edges
    private Queue<Edge> mst;
    // crossing (and ineligible) edges
    private MinPQ<Edge> pq;

    public LazyPrimMST(EdgeWeightedGraph G) {
        pq = new MinPQ<Edge>();
        marked = new boolean[G.V()];
        mst = new Queue<Edge>();

        // assumes G is connected
        visit(G, 0);
        while(!pq.isEmpty()) {
            // get the lowest weight edge from pq
            Edge e = pq.delMin();
            int v = e.either(), w = e.other(v);
            // skip if ineligible
            if(marked[v] && marked[w]) continue;
            // add edge to tree
            mst.enqueue(e);
            // add vertex to tree (either v or w)
            if(!marked[v]) visit(G, v);
            if(!marked[w]) visit(G, w);
        }
    }

    private void visit(EdgeWeightedGraph G, int v) {
        // mark v and add to pq all edges from v to unmarked vertices
        marked[v] = true;
        for (Edge e : G.adj(v)) {
            if (!marked[e.other(v)]) { pq.insert(e); }
        }
    }

    public Iterable<Edge> edges() { return mst; }

    public double weight() // later impl
}
```

#### Eager Version

*the minimum key on the priority queue is the weight of the minimal-weight crossing edge, and its associated vertex `v` is the next to add to the tree*

##### Running Time: E log V


```java
public class PrimMST {
    // shortest edge from tree vertex
    private Edge[] edgeTo;
    // distTo[w] = edgeTo[w].weight()
    private double[] distTo;
    // true if v on tree
    private boolean[] marked;
    // eligible crossing edges
    private IndexMinPQ<Double> pq;

    public PrimMST(EdgeWeightedGraph G) {
        edgeTo = new Edge[G.V()];
        distTo = new double[G.V()];
        marked = new boolean[G.V()];
        for(int v = 0; b < G.V(); v++) {
            distTo[v] = Double.POSITIVE_INFINITY;
        }
        pq = new IndexMinPQ<Double>(G.V());
        // initialize pq with 0, weight 0
        distTo[0] = 0;
        pq.insert(0, 0.0);
        while(!pq.isEmpty()) {
            // add closest vertex to tree
            visit(G, pq.delMin());
        }
    }

    private void visit(EdgeWeightedGraph G, int v) {
        // add v to tree; update data structures
        marked[v] = true;
        for(Edge e : G.adj(v)) {
            int w = e.other(v);
            // v-w is ineligible
            if(marked[w]) continue;
            if(e.weight() < distTo[w]) {
                // Edge e is the new best connection fron tree to w
                edgeTo[w] = e;
                distTo[w] = e.weight();
                if(pq.contains(w)) { pq.change(w, distTo[w]); }
                else { pq.insert(w, distTo[w]); }
            }
        }
    }

    public Iterable<Edge> edges()

    public double weight()
}
```

### Kruskal's Algorithm

1. process edges in order of their weight values
1. take for MST each edge that does not form a cycle
1. stop after adding V-1 edges

- *Prim's algorithm builds the MST one edge at a time, finding a new edge to attach to a single growing tree at each step*
- *Kruskal's algorithm also builds the MST one edge at a time; but, by contrast, it finds an edge that connects two trees in a forest of growing trees*

#### Running time: E log E

**generally slower than Prim's**

```java
public class KruskalMST {
    private Queue<Edge> mst;

    public KruskalMST(EdgeWeightedGraph G) {
        mst = new Queue<Edge>();
        MinPQ<Edge> pq = new MinPQ<Edge>(G.edges());
        UF uf = new UF(G.V());

        while(!pq.isEmpty() && mst.size() < G.V()-1) {
            // get the min weight edge on pq and its vertices
            Edge e = pq.delMin();
            int v = e.either(), w = e.other(v);
            // ignore ineligible edges
            if(uf.connected(v, w)) continue;
            // merge components
            uf.union(v, w);
            // add edge to mst
            mst.enqueue(e);
        }
    }

    public Iterable<Edge> edges() { return mst; }

    public double weight()
}
```

### Performance Characteristics of MST Algorithms
algorithm | space | time
--- | --- | ---
lazy Prim | E | E log E
eager Prim | V | E log V
Kruskal | E | E log E
Fredman-Tarjan | E | E + V log V
Chazelle | V | *almost but not quite* E
impossible? | V | E?

**MST problem is "solved" for practical purposes**

## Ch 4.4: Shortest Paths

```java
// Edge Weighted Digraph Types

public class DirectedEdge {
    // edge source
    private final int v;
    // edge target
    private final int w;
    // edge weight
    private final double weight;

    public DirectedEdge(int v, int w, double weight) {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    // weight of this edge
    public double weight() { return weight; }

    // vertex this edge points from
    public int from() { return v; }

    // vertex this edge points to
    public nt to() { return w; }
}

public class EdgeWeightedDigraph {
    // number of vertices
    private final int V;
    // number of edges
    private int E;
    // adjacency lists
    private Bag<DirectedEdge>[] adj;

    // empty V-vertex digraph
    public EdgeWeightedDigraph(int V) {
        this.V = V;
        this.E = 0;
        adj = (Bag<DirectedEdge>[]) new Bag[V];
        for(int v = 0; v < V; V++) {
            adj[v] = new Bag<DirectedEdge>();
        }
    }

    // number of vertices
    public int V() { return V; }

    // number of edges
    public int E() { return E; }

    // add e to this digraph
    public void addEdge(DirectedEdge e) {
        adj[e.from()].add(e);
        E++;
    }

    // edges pointing from v
    public Iterable<DirectedEdge> adj(int v) { return adj[v]; }

    // all edges in this digraph
    public Iterable<DirectedEdge> edges() {
        Bag<DirectedEdge> bag = new Bag<DirectedEdge>();
        for(int v = 0; v < V; v++) {
            for(DirectedEdge e : adj[v]) {
                bag.add(e);
            }
        }
        return bag;
    }
}

// Shortest-paths API

public class SP {
    // constructor
    SP(EdgeWeightedDigraph G, int s)

    // distance from s to v, ∞ if no path
    distTo(int v)

    // path from s to v?
    boolean hasPathTo(int v)

    // path from s to v, null if none
    Iterable<DirectedEdge> pathTo(int v)
}
```

### Djikstra's Algorithm
*analogous to Prim's algorithm*

#### Performance
- space: V
- time: E log V

```java
public class DjikstraSP {
    private DirectedEdge[] edgeTo;
    private double[] distTo;
    private IndexMinPQ<Double> pq;

    public DjikstraSP(EdgeWeightedDigraph G, int s) {
        edgeTo = new DirectedEdge[G.V()];
        distTo = new double[G.V()];
        pq = new IndexMinPQ<Double>(G.V());

        for(int v = 0; v < G.V(); v++) {
            distTo[v] = Double.POSITIVE_INFINITY;   
        }
        distTo[s] = 0.0;

        pq.insert(s, 0.0);
        while(!pq.isEmpty()) {
            relax(G, pq.delMin());
        }
    }

    private void relax(EdgeWeightedDigraph G, int v) {
        for(DirectedEdge e : G.adj(v)) {
            int w = e.to();
            if(distTo[w] > distTo[v] + e.weight()) {
                distTo[w] = distTo[v] + e.weight();
                edgeTo[w] = e;
                if(pq.contains(w)) { pq.change(w, distTo[w]); }
                else { pq.insert(w, distTo[w]); }
            }
        }
    }

    public double distTo(int v);
    public boolean hasPathTo(int v);
    public Iterable<Edge> pathTo(int v);
}

public class DijkstraAllPairsSP {
    private DijkstraSP[] all;

    public DijkstraAllPairsSP(EdgeWeightedDigraph G) {
        all = new DijkstraSP[G.V()];
        for(int v = 0; v < G.V(); v++) {
            all[v] = new DijkstraSP(G, v);
        }
    }

    public Iterable<Edge> path(int s, int t) {
        return all[s].pathTo(t);
    }

    public double dist(int s, int t) {
        return all[s].distTo(t);
    }
}
```

### Acyclic Edge-Weighted Graphs
Time: E + V (Linear)

```java
public class AcyclicSP {
    private DirectedEdge edgeTo[];
    private double[] distTo;

    public AcyclicSP(EdgeWeightedDigraph G, int s) {
        edgeTo = new DirectedEdge[G.V()];
        distTo = new double[G.V()];

        for(int v = 0; v < G.V(); v++) {
            distTo[v] = Double.POSITIVE_INFINITY;
        }
        distTo[s] = 0.0;

        Topological top = new Topological(G);

        for(int v : top.order()) { 
            relax(G, v);
        }
    }

    private void relax(EdgeWeightedDigraph G, int v) {
        for(DirectedEdge e : G.adj(v)) {
            int w = e.to();
            if(distTo[w] > distTo[v] + e.weight()) {
                distTo[w] = distTo[v] + e.weight();
                edgeTo[w] = e;
            }
        }
    }

    public double distTo(int v);
    public boolean hasPathTo(int v);
    public Iterable<Edge> pathTo(int v);
}
```

#### Longest Paths
Single-source longest paths in edge-weighted DAGs

- time: E + V
- solution: Given a longest-paths problem, create a copy of the given edge-weighted DAG that is identical to the original, except that all the edge weights are negated. Then the *shortest* path in this copy is the *longest* path in the original.

### Parallel Job Scheduling

#### Critical Path Method for parallel scheduling
1. Create an edge-weighted DAG with a source `s`, a sink `t`, and two vertices for each job (a *start* vertex and an *end* vertex)
1. For each job, add an edge from its start vertex to its end vertex with weight equal to its duration
1. For each precedence constraint `v->w`, add a zero-weight edge from the end vertex corresponding to `v` to the beginning vertex corresponding to `w`
1. Also add zero-weight edges from the source to each job's start vertex and from each job's end vertex to the sink
1. Now, schedule each job given by the length of its longest path from the source

- Time: linear

*Parallel job scheduling with relative deadlines is a shortest-paths problem in edge-weighted digraphs (with cycles and negative weights allowed).*

### Shortest paths in general edge-weighted digraphs

*There exists a shortest path from* s *to* v *in an edge-weighted digraph if and only if there exists at least one directed path from* s *to* v **and** *no vertex on any directed path from* s *to* v *is on a negtive cycle.*

### Bellman-Ford Algorithm