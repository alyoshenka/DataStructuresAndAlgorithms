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

### Digraph representations

in practice: use adjacency-lists representation (real-world digraphs tend to be sparse)

representation | space | insert edge from v to w | edge from v to w? | iterate over vertices pointing from v?
--- | --- | --- | --- | ---
list of edges | E | 1 | E | E
adjacency matrix | V^2 | 1* | 1 | V
adjacency lists | E + V | 1 | outdegree(v) | outdegree(v)

*disallows parallel edges

### Digraph API

```java
public class Digraph
    private final int V; // number of vertices
    private final Bag<Integer>[] adj; // adjacency lists
    public Digraph(int V) 
    {
        this.V = V;
        adj = (Bag<Integer>[]) new Bag[V];
        for(int v = 0; v < V; v++)
        {
            adj[v] = new Bag<Integer>();
        }
    }

    void addEdge(int v, int w) // edge from v to w
    {
        adj[v].add(w);
    }

    Iterable<Integer> adj(int v) // vertices pointing from v
    {
        return adj[v];
    }

    Digraph reverse() // reverse of this digraph
```

performant representation: adjacency list: vertex-indexed array of lists/bags

### Digraph Search

#### DFS

*same method as for undirected graphs*

DFS is a **digraph** algorithm

DFS (to visit a vertex v)
1. mark v as visited
1. recursively visit all unmarked vertices w pointing from v

```java
public class DirectedDFS
{
    private boolean[] marked; // true if path from s

    public DirectedDFS(Digraph G, int x)
    {
        marked = new boolean[G.V()];
        dfs(G, s);
    }

    // recursive DFS does all the work
    private void dfs(Digraph G, int v)
    {
        marked[v] = true;
        for(int w : G.adj(v)) 
        {
            if(!marked[w])
            {
                dfs(G, w);
            }
        }
    }

    // is vertex reachable from s?
    public boolean visited(int v)
    {
        return marked[v];
    }
}
```

*every program is a digraph*
- program control-flow analysis
    - dead-code elimination: find (and remove) unreachable code
    - infinite-loop detection: determine whether exit is unreachable
- mark-sweep garbage collector
    - vertex = object, edge = reference
    - roots: objects known to be directly accessible by program
    - reachable objects: objects indirectly accessible by program
    - mark: mark all reachable objects
    - sweep: if object is unmarked, add to free list of memory
    - memory cost: 1 extra mark bit per object, plus DFS stack

#### BFS

same method as for undirected graphs

BFS is a *digraph* algorithm

BFS (from source vertex s):
1. put s onto a FIFO queue, mark s as visited
1. repeat until queue is empty:
    1. remove least recently added vertex v
    1. for each unmarked vertex pointing from v:
        - add to queue and mark as visited

BFS computes shortest paths from s to all other vertices in a digraph in time proportional to E + V

multiple-source shortest paths: given a digraph and a set of source vertices, find the shortest path from any vertex in the set to each other vertex
- how to implement multi-source constructor?
    - use BFS, but initialize by enqueuing all source vertices

Bare-bones web crawler:
```java
Queue<String> queue nre Queue<String>(); // queue of websites to crawl
SET<String> discovered = new SET<String>(); // set of discovered sites

String root = "http://www.princeton.edu"; 
queue.enqueue(root); // start crawling from root site
discovered.add(root);

while(!queue.isEmpty())
{
    String v = queue.dequeue();
    StdOut.println(v);
    In in = new In(v); // read raw html from next website in queue
    String input = in.readAll();

    // use regular expression to find all URLS in website of form
    // http://xxx.yyy.zzz (crude pattern misses relative URLs)
    String regexp = "http://(\\w+\\.)*(\\w+)";
    Pattern patter = Pattern.compile(regexp);
    Matcher matcher = pattern.matcher(input);
    while(matcher.find())
    {
        String w = matcher.group();
        if(!discovered.contains(w))
        {
            discovered.add(w);
            queue.enqueue(w);
        }
    }
}
```

### Topological Sort

precedence scheduling: given a set of tasks to be completed with precedence constraints, in which order should they be scheduled?

DAG (directed acyclic graph): digraph with no cycles

topological order -> redraw DAG so all edges point upwards

Use DFS!

- run depth-first search
- return vertices in reverse postorder
    - when done with a vertex, put it onto a stack

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
        }
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

### Strong Components

vertices v and w are **strongly connected** if there is a directed path from v to w *and* and directed path from w to v

strong connectivity = equivalence relation:
- v is strongly connected to v
- if v is strongly connected to w, then w is strongly connected to v
- if v is strongly connected to w and w to x, then v is strongly connected to x

strong component: maximal subset of strongly-connected vertices

#### Applications
- ecological food webs
    - vertex = species; edge = from producer to consumer
    - strong component: subset of species with common energy flow
- software modules
    - vertex = software module; edge = from module to dependency
    - strong component: subset of mutually interacting modules
    - approach 1: package strong components together
    - approach 2: use to improve design

*core problem in Operations Research*

#### Intuition
- reverse graph: if the graph is reversed, it will still have the same strong components
- kernel DAG: contract each strong component into a single vertex
- **idea**
    1. compute topological order (reverse postorder) in kernal DAG
    1. run DFS, considering vertices in reverse topological order

```java
public class KosarajuSharirSCC
{
    private boolean marked[];
    private int id[];
    private int count;

    public KosarajuSharirSCC(Digraph G)
    {
        marked = new int[G.V()];
        id = new ind[G.V()];

        // only new code needed from CC
        DepthFirstOrder dfs = new DepthFirstOrder(G.reverse());
        for(int v : dfs.reversePost())
        // for(int v = 0; v < G.V(); v++) 
        {
            if(!marked[v])
            {
                dfs(G, v);
                count++;
            }
        }
    }

    private void dfs(Digraph G, int v)
    {
        marked[v] = true;
        id[v] = count;
        for(int w : Graph.adj(v))
            if(!marked[w])
                dfs(G, w)
    }

    public boolean stronlyConnected(int v, int w)
    { return id[v] == id[w]; }
}
```

### Digraph Processing summary

problem | algorithm
--- | ---
single-source reachability in a digraph | DFS
topological sort in a DAG | DFS
strong components in a digraph | Kosaraju DFS (twice)