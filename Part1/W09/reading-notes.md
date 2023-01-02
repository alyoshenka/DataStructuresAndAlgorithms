# Week 9

## Ch 4.3: Minimum Spanning Trees

spanning tree (of a graph): connected subgraph with no cycles that includes all vertices

Assumptions
- graph is connected
- edge weights != distances
- edge weights may be zero or negative
- edge weights are all different

The *cut* of a graph is a partition of its vertices into two nonempty disjoing sets. A *crossing edge* of a cut is an edge that connects a vertex in one set with a vertex in the other.

Cut Property: given any cut in an edge-weighted graph, the crossing edge of minimum weight is in the MST of the graph

```java
public class Edge implements Comparable<Edge>
{
    private final int v; // one vertex
    private final int w; // the other vertex
    private final double weight; // edge weight

    public Edge(int v, int w, double weight)
    {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    public double weight() { return weight; }

    // eigher of this edge's vertices
    public int either() { return v; }

    // the other vertex
    public int other(int vertex) 
    { 
        if (vertex == v) return w; 
        else if (vertex == w) return v; 
        else throw new RuntimeException("Inconsistent edge");
    }

    int compareTo(Edge that) 
    { return this.weight().compareTo(that.weight()); }

    String toString() 
    { return String.format("%d-%d %.2f", v, w, weight); }
}

public class EdgeWeightedGraph
{
    private final int V; // number of vertices
    private int E; // number of edges
    private Bag<Edge>[] adj; // adjacency lists

    // create an empty V-vertex graph
    public EdgeWeightedGraph(int V) 
    { 
        this.V = V;
        this.E = 0;
        adj = (Bag<Edge>[]) new Bag[V];
        for(int v = 0; v < V; v++)
            adj[v] = new Bag<Edge>();
    }

    // read graph from input stream
    public EdgeWeightedGraph(In in) { /* */ }

    // number of vertices
    int V() { return V; } 

    // number of edges
    int E() { return E; }

    // add edge to the graph
    public void addEdge(Edge e) 
    {
        int v = e.either(), w = e.other(v);
        adj[v].add(e);
        adf[w].add(e);
        E++;
    }

    // edges incident to v
    public Iterable<Edge> adj(int v) { return adj[v]; }

    // all of this graph's edges
    public Iterable<Edge> edges() 
    { 
        Bag<Edge> b = new Bag<Edge>();
        for(int v = 0; v < V; v++) 
            for(Edge e : adj[v]) 
                if (e.other(v) > v) b.add(e);
        return b;
    }

    public String toString() { }
}

public class MST
{
    public MST(EdgeWeightedGraph G) 
    {

    }

    // all of the MST edges
    public Iterable<Edge> edges() 
    {

    }

    // weight of the MST
    public double weight() { }
}
```

### Prim's Algorithm

```java
public class LazyPrimMST
{
    private boolean[] marked; // MST vertices
    private Queue<Edge> mst; // MST edges
    private MinPQ<Edge> pq; // crossing (and inelegible) edges

    public LazyPrimMST(EdgeWeightedGraph G) 
    {
        pq = new MinPQ<Edge>();
        marked = new boolean[G.V()];
        mst = new Queue<Edge>();

        visit(G, 0); // assume G is connected
        while(!pq.isEmpty())
        {
            Edge e = pq.delMin(); // get lowest-weight edge from pq
            int v = e.either(), w = e.other(v);
            if(marked[v] && marked[w]) continue; // skip if inelegible
            mst.enqueue(e);
            if(!marked[v]) visit(G, v); // add vertex to tree (either v or w)
            if(!marked[w]) visit(G, w);
        }
    }

    private void visit(EdgeWeightedGraph G, int v)
    {
        // mark v and add to pq all edges from v to unmarked vertices
        marked[v] = true;
        for(Edge e : G.adj(v))
            if(!marked[e.other(v)]) pq.insert(e);
    }

    public Iterable<Edge> edges() { return mst; }

    public double weight() { /* */ }
}
```

#### Eager version

*only maintain on the priority queue* one *edge for each non-tree vertex* w: *the shortest edge that connects it to the tree*

Any longer edge connecting *w* to the tree will become inelegible at some point, so there is no need to keep it on the priority queue

```java
public class PrimMST
{
    private Edge[] edgeTo; // shortest edge from tree vertex
    private double[] distTo; // distTo[w] = edgeTo[w].weight()
    private boolean[] marked; // true if v on tree
    private IndexMinPQ<Double> pq; // eligible crossing edges

    public PrimMST(EdgeWeightedGraph G)
    {
        edgeTo = new Edge[G.V()];
        distTo = new double[G.V()];
        marked = new boolean[G.V()];
        for(int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;   
        pq = new IndexMinPQ<Double>(G.V());

        distTo[0] = 0.0; // initialze pq with 0, weight 0
        pq.insert(0, 0.0);
        while(!pq.isEmpty())
            visit(G, pq.delMin()); // add closest vertex to tree
    }

    private void visit(EdgeWeightedGraph G, int v)
    {
        // add v to tree; update data structures
        marked[v] = true;
        for(Edge e : G.adj(v))
        {
            int w = e.other(v);
            if(marked[w]) continue; // v-w is ineligible
            if(e.weight() < distTo[w])
            {
                // edge e is the new best connection from tree to w
                edgeTo[w] = e;
                distTo[w] = e.weight();
                if(pq.contains(w)) pq.change(w, distTo[w]);
                else pq.insert(w, distTo[w]);
            }
        }
    }
}
```

### Kruskal's Algorithm



## Ch 4.4: Shortest Paths

shortest-paths tree
> given an edge-weighted digraph and a designated vertex *s*, a *shortest-paths tree* fro a source *s* is a subgraph containing *s* and all vertices reachable from *s* that forms a directed tree rooted at *s* such that every tree path is a shortest path in the digraph

```java
public class DirectedEdge 
{
    private final int v;
    private final int w;
    private final double weight;

    public DirectedEdge(int v, int w, double weight) 
    {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    public double weight() { return weight; }

    // vertex this edge points from
    public int from() { return v; }

    // vertex this edge points to
    public int to() { return w; }
}

public class EdgeWeightedDigraph
{
    private final int V;
    private int E; // number of edges
    private Bag<DirectedEdge>[] adj; // adjacency lists

    public EdgeWeightedDigraph(int V)
    {
        this.V = V;
        this.E = 0;
        adj = (Bag<DirectedEdge>[]) new Bag[V];
        for(int v = 0; v < V; v++) 
            adj[v] = new Bag<>();
    }

    public void addEdge(DirectedEdge e)
    {
        adj[e.from()].add(e);
        E++;
    }

    public Iterable<DirectedEdge> adj(int v) { return adj[v]; }

    public Iterable<DirectedEdge> edges() 
    { 
        Bag<DirectedEdge> bag = new Bag<>();
        for(int v = 0; v < V; v++)
            for(DirectedEdge e : adj[v])
                bag.add(e);
        return bag;
    }
}

public class SP
{
    public SP(EdgeWeightedDigraph G, int s)
    {

    }

    public double distTo(int v) 
    { return distTo[v]; }

    public boolean hasPathTo(int v) 
    { return distTo[v] < Double. POSITIVE_INFINITY; }

    public Iterable<DirectedEdge> pathTo(int v) 
    {
        if(!hasPathTo(v)) return null;
        Stack<DirectedEdge> path = new Stack<>();
        for(DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()])
            path.push(e);
        return path;
    }

    // edge relaxation
    private void relax(DirectedEdge e)
    {
        int v = e.from(), w = e.to();
        if(distTo[w] > distTo[v] + e.weight())
        {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
        }
    }

    // vertex relaxation
    private void relax(EdgeWeightedDigraph G, int v)
    {
        for(DirectedEdge e : G.adj[v])
        {
            int w = e.to();
            if(distTo[w] > distTo[v] + e.weight())
            {
                distTo[w] = distTo[v] + e.weight();
                edgeTo[w] = e;
            }
        }
    }
```

generic shortest-paths algorithm
> Relax any edge in G, continuing until no edge is eligible

### Dijikstra's Algorithm

```java
public class DijkstraSP
{
    private DirectedEdge[] edgeTo;
    private double[] distTo;
    private IndexMinPQ<Double> pq;

    public DijkstraSP(EdgeWeightedGraph G, int s)
    {
        edgeTo = new DirectedEdge[G.V()];
        distTo = new double[G.V()];
        pq = new IndexMinPQ<Double>(G.V());

        for(int v = 0; v < V; v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        
        pq.insert(s, 0.0);
        while(!pq.isEmpty())
            relax(G, pq.delMin())
    }

    private void relax(EdgeWeightedGraph G, int v)
    {
        for(DirectedEdge e : G.adj(v))
        {
            int w = e.to();
            if(distTo[w] > distTo[v] + e.weight())
            {
                distTo[w] = distTo[v] + e.weight();
                edgeTo[w] = e;
                if(pq.contains(w)) pq.change(w, distTo[w]);
                else pq.insert(w, distTo[w]);
            }
        }
    }
```