# Week 8: Graphs

## Ch 28: Graphs

vertex = node

undirected -> can travel in both directions

directed graph/digraph

simple path: does not pass through any vertex more than once

cycle: path that begins and ends at the same vertex

simple cycle: passes through other vertices only once each

acyclic graph: no cycles

connected graph: path between every pair of distinct vertices

complete graph: edge between every pair of distinct vertices

adjacent vertices: joined by an edge

neighbors: adjacent vertices

If a graph has *n* vertices, it can have at most:
- directed: *n (n - 1)* edges
- undirected: *n (n - 1) / 2* edges

sparse: relatively few edges (O(n))

dense: many edges (O(n^2))

*Typical graphs are sparse*

topological order -> order of vertices in a directed graph without cycles

*All trees are graphs, but not all graphs are trees. A tree is a connected graph without cycles.*

depth-first traversal: follows a path that descends the levels of a tree as deeply as possible until it reaches a leaf
- preorder, inorder, postorder

breadth-first traversal: follows a path that explores an entire lefel before moving to the next level
- level-order traversal

```java
// Algorithm for breadth-first traversal

getBreadthFirstTraversal(originVertex)

traversalOrder = // a new queue for the resulting traversal order
vertexQueue = // a new queue to hold vertices as they are visited

/* mark */ originVertex /* as visited */
traversalOrder.enqueue(originVertex)
vertexQueue.enqueue(originVertex)

while(!vertexQueue.isEmpty())
{
    frontVertex = vertexQueue.dequeue()
    while(frontVertex /* has a neighbor */)
    {
        nextNeighbor = // next neighbor of frontVertex
        if(nextNeighbor /* is not visited */)
        {
            /* mark */ nextNeighbor /* as visited */
            traversalOrder.enqueue(nextNeighbor)
            vertexQueue.enqueue(nextNeighbor)
        }
    }
}

return traversalOrder
```

```java
// Algorithm for depth-first traversal

getDepthFirstTraversal(originVertex)

traversalOrder = // a new queue for the resulting traversal order
vertexStack = // a new stack to hold vertices as they are visited

/* mark */ originVertex /* as visited */
traversalOrder.enqueue(originVertex)
vertexStack.push(originVertex)

while(!vertexStack.isEmpty())
{
    topVertex = vertexStack.peek()
    if(topVertex /* has an unvisited neighbor */)
    {
        nextNeighbor = // next unvisited neighbor of topVertex
        /* mark */ nextNeighbor /* as visited */
        traversalOrder.enqueue(nextNeighbor)
        vertexStack.push(nextNeighbor)
    } 
    else // all neighbors are visited
    {
        vertexStack.pop()
    }
}

return traversalOrder
```

**A topological order is not possible for a graph that has a cycle.**

topological sort: process that discovers a topological order for the vertices in a graph

```java
// Algorithm for finding topological order

getTopologicalOrder()

vertexStack = // a new stack to hold vertices as they are visited
numberOfVertices = // number of vertices in the graph
for(counter 1 /* to numberOfVertices */)
{
    nextVertex = // an unvisited vertex whose neighbors, if any, are all visited
    /* mark */ nextVertex /* as visited */
    vertexStack.push(nextVertex)
}

return vertexStack
```

```java 
// Algorithm for shortest (unweighted) path

getShortestPath(originVertex, endVertex, path)

done = false
vertexQueue = // a new queue to hold vertices as they are visited
/* mark */ originVertex /* as visited */
vertexQueue.enqueue(originVertex)

while(!done && !vertexQueue.isEmpty())
{
    frontVertex = vertexQueue.dequeue()
    while(!done && frontVertex /* has a neighbor */)
    {
        nextNeighbor = // next neighbor of frontVertex
        if(nextNeighbor /* is not visited */)
        {
            /* mark */ nextNeighbor /* as visited */
            /* set the length of the path to */ nextNeighbor /* to 1 + length of path to */ frontVertex
            /* set the successor of */ nextNeighbor /* to */ frontVertex
            vertexQueue.enqueue(nextNeighbor)
        }
        if(nextNeighbor /* equals */ endVertex) { done = true }
    }
}

// traversal ends, construct shortest path
pathLength = /* length of path to */ endVertex
path.push(endVertex)

vertex = endVertex
while(vertex /* has a predecessor */)
{
    vertex = /* predecessor of */ vertex
    path.push(vertex)
}

return pathLength
```

weighted graph -> use a priority queue

## Ch 29: Graph Implementations

```java

import java.util.Iterator;

public interface VertexInterface<T>
{
    public T getLabel();

    public void visit();

    public void unvisit();

    public void isVisited();

    public boolean connect(VertexInterface<T> endVertex, double edgeWeight);

    public boolean connect(VertexInterface<T> endVertex);

    public Iterator<VertexInterface<T>> getNeighborIterator();

    public Iterator<Double> getWeightIterator();

    public boolean hasNeighbor();

    public VertexInterface<T> getUnvisitedNeighbor();

    public void setPredecessor(VertexInterface<T> predecessor);

    public VertexInterface<T> getPredecessor();

    public boolean hasPredecessor();

    public void setCost(double newCost);

    public double getCost();
}
```

```java
class Vertex<T> implements VertexInterface<T>
{
    protected class Edge { /* as above */ }

    private T label;
    private ListWithIteratorInterface<Edge> edgeList; // edges to neighbors

    private boolean visited;
    private VertexInterface<T> previousVertex;
    private double cost;

    public Vertex(T vertexLabel)
    {
        label = vertexLabel;
        edgeList = new LinkedListWithIterator<Edge>();
        visited = false;
        previousVertex = null;
        cost = 0;
    }

    public boolean connect(VertexInterface<T> endVertex, double edgeWeight)
    {
        boolean result = false;
        if(!this.equals(endVertex)) // vertices are distinct
        {
            Iterator<VertexInterface<T>> neighbors = this.getNeighborIterator();
            boolean duplicateEdge = false;
            
            while(!duplicateEdge && neighbors.hasNext())
            {
                VertexInterface<T> nextNeighbor = neighbors.next();
                if(endVertex.equals(nextNeighbor)) { duplicateEdge = true; }
            }
            if(!duplicateEdge)
            {
                edgeList.add(new Edge(endVertex, edgeWeight));
                result = true;
            }
        }
        return result;
    }

    public boolean connect(VertexInterface<T> endVertex) { return connect(endVertex, 0); }

    public boolean hasNeighbor() { return !edgeList.isEmpty(); }

    public VertexInterface<T> getUnvisitedNeighbor() 
    {
        VertexInterface<T> result = null;

        Iterator<VertexInterface<T>> neighbors = getNeighborIterator();
        while(neighbors.hasNext() && result == null)
        {
            VertexInterface<T> nextNeighbor = neighbors.next();
            if(!nextNeighbor.isVisited()) { result = nextNeighbor; }
        }
        return result;
    }

    public boolean equals(Object other)
    {
        boolean result;
        if(other == null || getClass() != other.getClass()) { result = false; }
        else 
        {
            Vertex<T> otherVertex = (Vertex<T>)other;
            result = label.equal(otherVertex.label); 
        }
        return result;
    }

    protected class Edge
    {
        private VertexInterface<T> vertex;
        private double weight;

        protected Edge(VertexInterface<T> endVertex, double edgeWeight) 
        {
            vertex = endVertex;
            weight = edgeWeight;
        }

        protected VertexInterface<T> getEndVertex() { return vertex; }

        protected double getWeight() { return weight; }  
    }

    private class neighborIterator implements Iterator<VertexInterface<T>>
    {
        private Iterator<Edge> edges;

        private neighborIterator() { edges = edgeList.getIterator(); }

        public boolean hasNext() { return edges.hasNext(); }

        public VertexInterface<T> next()
        {
            VertexInterface<T> nextNeighbor = null;
            if(edges.hasNext())
            {
                Edge edgeToNextNeighbor = edges.next();
                nextNeighbor = edgeToNextNeighbor().getEndVertex();
            }
            else { throw new NoSuchElementException(); }
            return nextNeighbor;
        }

        public void remove() { throw new UnsupportedOperationException(); }
    }

    private class weightIterator implements Iterator<VertexInterface<T>>
    {
        private Iterator<Edge> edges;

        private neighborIterator() { edges = edgeList.getIterator(); }

        public boolean hasNext() { return edges.hasNext(); }

        public double next()
        {
            double nextEdge = 0;
            if(edges.hasNext())
            {
                Edge edgeToNextNeighbor = edges.next();
                nextEdge = edgeToNextNeighbor().getWeight();
            }
            else { throw new NoSuchElementException(); }
            return nextEdge;
        }

        public void remove() { throw new UnsupportedOperationException(); }
    }
}
```

*Regardless of the kind of graph or how you implement it, you need a container such as a dictionary for the graph's vertices.*

```java
// the generic type T represents the data type of the object used to label the graph's vertices

public class DirectedGraph<T> implements GraphInterface<T>
{
    private DictionaryInterface<T, VertexInterface<T>> vertices;
    private int edgeCount; // not easily calculated, so store it

    public DirectedGraph()
    {
        vertices = new LinkedDictionary<T, VertexInterface<T>>();
        edgeCount = 0;
    }

    public boolean addVertex(T vertexLabel)
    {
        VertexInterface<T> isDuplicate = vertices.add(vertexLabel, new Vertex(vertexLabel));
        return isDuplicate == null; 
    }

    // weighted
    public boolean addEdge(T begin, T end, double edgeWeight)
    {
        boolean result = false;

        VertexInterface<T> beginVertex = vertices.getValue(begin);
        VertexInterface<T> endVertex = vertices.getValue(end);

        if(beginVertex != null && endVertex != null)
        {
            result = beginVertex.connect(endVertex, edgeWeight);
        }
        
        if(result) { edgeCount++; }
        
        return result;
    }

    // unweighted
    public boolean addEdge(T begin, T end) { return addEdge(begin, end, 0); }

    public boolean hasEdge(T begin, T end)
    {
        boolean found = false;

        VertexInterface<T> beginVertex = vertices.getValue(begin);
        VertexInterface<T> endVertex = vertices.getValue(end);

        if(beginVertex != null && endVertex != null)
        {
            Iterator<VertexInterface<T>> neighbors = beginVertex.getNeighborIterator();

            while(!found && neighbors.hasNext())
            {
                VertexInterface<T> nextNeighbor = neighbors.next();
                if(endVertex.equals(nextNeighbor)) { found = true; }
            }
        }
        return found;
    }

    public boolean isEmpty() { return vertices.isEmpty(); }

    public void clear()
    {
        vertices.clear();
        edgeCount = 0;
    }

    public int getNumberOfVertices() { return vertices.getSize(); }

    public int getNumberOfEdges() { return edgeCount; }

    protected void resetVertices()
    {
        Iterator<VertexInterface<T>> vertexIterator = vertices.getValueIterator();
        while(vertexIterator.hasNext())
        {
            VertexInterface<T> nextVertex = VertexIterator.next();
            nextVertex.unvisit();
            nextVertex.setCost(0);
            nextVertex.setPredecessor(null);
        }
    }

    public QueueInterface<T> getBreadthFirstTraversal(T origin)
    {
        resetVertices();
        QueueInterface<T> traversalOrder = new LinkedQueue<T>();
        QueueInterface<VertexInterface<T>> vertexQueue = new LinkedQueue<VertexInterface<T>>();

        VertexInterface<T> originVertex = vertices.getValue(origin);
        originVertex.visit();
        traversalOrder.enqueue(origin);
        vertexQueue.enqueue(originVertex);

        while(!vertexQueue.isEmpty())
        {
            VertexInterface<T> frontVertex = vertexQueue.dequeue();

            Iterator<VertexInterface<T>> neighbors = frontVertex.getNeighborIterator();

            while(neighbors.hasNext())
            {
                VertexInterface<T> nextNeighbor = neighbors.next();
                if(!nextNeighbor.isVisited())
                {
                    nextNeighbor.visit();
                    traversalOrder.enqueue(nextNeighbor.getLabel);
                    vertexQueue.enqueue(nextNeighbor);
                }
            }
        }

        return traversalOrder;
    }

    public int getShortestPath(T begin, T end, StackInterfact<T> path)
    {
        resetVertices();
        boolean done = false;
        QueueInterface<VertexInterface<T>> vertexQueue = new LinkedQueue<VertexInterface<T>>();

        VertexInterfact<T> originVertex = vertices.getValue(begin);
        VertexInterfact<T> endVertex = vertices.getValue(end);

        originVertex.visit();

        vertexQueue.enqueue(originVertex);

        while(!done && !vertexQueue.isEmpty())
        {
            VertexInterface<T> frontVertex = vertexQueue.dequeue();
            
            Iterator<VertexInterfact<T>> neighbors = frontVertex.getNeighborIterator();

            while(!done && neighbors.hasNext())
            {
                VertexInterface<T> nextNeighbor = neighbors.next();

                if(!nextNeighbor.visited())
                {
                    nextNeighbor.visit();
                    nextNeighbor.setCost(1 + frontVertex.getCost());
                    nextNeighbor.setPredecessor(frontVertex);
                    vertexQueue.enqueue(nextNeighbor);
                }

                if(nextNeighbor.equal(endVertex)) { done = true; }
            }
        }

        int pathLength = (int)endVertex.cost();
        path.push(endVertex.getLabel());

        VertexInterface<T> vertex = endVertex;
        while(vertex.hasPredecessor())
        {
            vertex = vertex.getPredecessor();
            path.push(vertex.getLabel());
        }
        
        return pathLength;
    }
}
```

Performance of basinc operations of the ADT graph when using adjacency lists

operation | cost
--- | ---
addVertex | O(n)
addEdge | O(n)
hasEdge | O(n)
isEmpty | O(1)
getNumberOfVertices | O(1)
getNumberofEdges | O(1)
clear | O(1)

## 4.1 Undirected Graphs

- tree: acyclic connected graph
- forest: disjoint set of trees
- spanning tree: subgraph of a connected graph that contains all of that graph's vertices and is a single tree
- spanning forest: union of spanning trees of the connected components of a graph
- bipartite graph: vertices can be divided into two sets such that all edges connect a vertex in one set to a vertex in another set

Algorithms pg 553