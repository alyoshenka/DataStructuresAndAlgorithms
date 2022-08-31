# Week 8 (Part II Week 1): Graphs

## Undirected Graphs

### Graph API

```java
public class Graph 
{
    private final int V;
    private Bag<Integer>[] adj; // adjacency lists

    // create an empty grap with V vertices
    public Graph(int V) 
    {
        this.V = V;
        // create empty graph with V vertices
        adj = (Bag<Integer>[]) new Bag[V]; 
        for(int v = 0; v < V; v++)
            adj[v] = new Bag<Integer>();
    }

    // create a graph from input stream
    public Graph(In in) {}

    // add an edge v-w
    public void addEdge(int v, int w) 
    {
        adj[v].add(w);
        adj[v].add(v);
        // (parallel edges allowed)
    }

    // vertices adjacent to v
    public Iterable<Integer> adj(int v) { return adj[v]; }

    // number of vertices
    public int V() {}

    // number of edges
    public int E() {}

    // string representation
    public String toString() {}

    // compute the degree of v (number of vertices that are connected to v)
    public static int degree(Graph G, int v)
    {
        int degree = 0;
        for(int w : G.adj(v)) degree++;
        return degree;
    }

    // compute maximum degree
    public static int maxDegree(Graph G)
    {
        int max = 0;
        for(int v = 0; v < G.V(); v++)
            if(degree(G, v) > max)
                max = degree(G, v);
        return max;
    }

    // compute average degree
    public static double averageDegree(Graph G)
    { return 2.0 * G.E() / G.V(); }

    // count self-loops
    public static int numberOfSelfLoops(Graph G)
    {
        int count = 0;
        for(int v = 0; v < G.V(); v++)
            for(int w : G.adj(v))
                if(v == w) count++;
        return count/2; // each edge counted twice
    }
}
```

How to implement?
- set-of-edges: maintain a list of the edges (linked list or array)
    - inefficient
- adjacency-matrix: maintain a two-dimensional V-by-V boolean array
    - each edge in v-w graph: `adj[v][w] = adj[w][v] = true`
    - two entries for each edge
    - way too much space!
- adjacency-list: vertex-indexed array of lists
    - `Bag` used to hold adjacency list
    - *used in practice*

*Real-world graphs tend to be* sparse.

representation | space | add edge | edge between v and w? | iterate over vertices adjacent to v?
--- | --- | --- | --- | --- 
list of edges | E | 1 | E | E
adjacency matrix | V^2 | 1* (disallows parallel edges) | 1 | V
adjacency lists | E + V | 1 | degree(v) | degree(v)

### Depth-First Search

#### Tremaux maze exploration algorithm
- unroll a ball of string behind you
- mark each visited intersection and each visited passage
- retrace steps when no unvisited options

DFS (to visit a vertex v)
1. mark v as visited
1. recursively visit all unmarked vertices w adjacent to v

Design pattern: decouple graph data type from graph processing

```java
public class Paths
{
    // find paths in G from source s
    public Paths(Graph G, int s)

    // is there a path from s to v?
    public boolean hasPath(int v)

    // path from s to v, null if no such path
    public Iterable<Integer> pathTo(int v)
```

```java
public class DepthFirstPaths
{
    private boolean[] marked;
    // parent-link representation of a tree rooted at s
    private int[] edgeTo;
    private int s;

    public DepthFirstPaths(Graph G, int s)
    {
        // initialize data structures

        dfs(G, s);
    }

    private void dfs(Graph G, int v)
    {
        // recursive dfs does the work
        
        marked[v] = true;
        for(int w : G.adj(v))
            if(!marked[w])
            {
                dfs(G, w);
                edgeTo[w] = v;
            }
    }

    public boolean hasPathTo(int v) { return marked[v]; }

    public Iterable<Integer> pathTo(int v)
    {
        if(!hasPathTo(v)) return null;
        Stack<Integer> path = new Stack<>();
        for(int x = v; x != s; x = edgeTo[x])   
            path.push(x);
        path.push(s);
        return path;
    }
}
```

*DFS markes all vertices connected to* s *in time proportional to the sum of their degrees*

*After DFS, can find vertices connected to* s *in constant time and can find a path to* s *(if one exists) in time proportional to its length*

### Breadth-First Search

Algorithm: repeat until queue is empty
1. remove vertex v from queue
1. add to queue all unmarked vertices adjacent to v and mark them

**Not recursive!**

- Depth-first search: put unvisited vertices on a `stack`
- Breadth-first search: put univisited verticies on a `queue`

Shortest path: find path from `s` to `t` that uses *fewest number of edged*

BFS (from source vertex *s*)
1. put *s* onto a FIFO queue and mark as visited
1. repeat until the queue is empty:
    1. remove the most recently added vertex *v*
    1. add each of *v*'s unvisited neighbors to the queue, and mark them as visited

```java
public class BreadthFirstPaths
{
    private boolean[] marked;
    private int[] edgeTo;

    // ...

    private void bfs(Graph G, int s)
    {
        Queue<Integer> q = new Queue<>();
        q.enqueue(s);
        marked[s] = true;
        while(!q.isEmpty())
        {
            int v = q.dequeue();
            for(int w : G.adj(v))
            {
                if(!marked[w])
                {
                    q.enqueue(w);
                    marked[w] = true;
                    edgeTo[w] = v;
                }
            }
        }
    }
```

### Connected Components

```java
public class CC
{
    private boolean[] marked;
    private int[] id;
    private int count; // number of components

    // find connected components in G
    public CC(Graph G)
    {
        marked = new boolean[G.V()];
        id = new int[G.V()];
        for(int v = 0; v < G.V(); v++)
        {
            if(!marked[v])
            {
                dfs(G, v);
                count++;
            }
        }
    }

    private void dfs(Graph G, int v)
    {
        marked[v] = true;
        // all vertices discovered in same call of dfs have same id
        id[v] = count;
        for(int w : G.adj(v))
            if(!isMarked[w])
                dfs(G, w);
    }

    // are v and w connected?
    public boolean connected(int v, int w)

    // number of connected components
    public int count() { return count; }

    // component identifier for v
    public int id(int v) { return id[v]; }
}
```

connected component: maximal set of connected vertices
1. initialize all vertices *v* as unmarked
1. for each unmarked vertex *v*, run DFS to identify all vertices discovered as part of the same component



## Directed Graphs

### Topological Sort

Use DFS!

- run depth-first search
- return vertices in reverse postorder

```java
public class DepthFirstOrder 
{
    private boolean[] marked;
    private Stack<Integer> reversePost;

    public DepthFirstOrder(Digraph G)
    {
        reversePost = new Stack<Integer>();
        marked = new boolean[G.V()];

        for(int v = 0; v < G.V(); v++)
        {
            if(!marked[v]) { dfs(G, v); }
        })
    }

    private dfs(DiGraph G, int v)
    {
        marked[v] = true;
        for(int w : G.adj(v)) 
        {
            if(!marked[w]) { dfs(G, w); }
        }
        reversePost.push(v);
    }

    public Iterable<Integer> reversePost() { return reversePost; }
}
```

*A digraph has a topological order iff no directed cycle*
- if directed cycle, topological order is impossible
- if no directed cycle, DFS-based algorithm finds a topological order

