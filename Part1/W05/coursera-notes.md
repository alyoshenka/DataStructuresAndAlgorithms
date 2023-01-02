# Week 5: Balanced and Binary Search Trees

## Balanced Search Trees

### 2-3 Search Trees

Allow 1 or 2 keys per node 
- 2-node: 1 key, 2 children
- 3-node: 2 keys, 3 children

perfect balance: every path from root to null link has same length

Insert into a 2-node at bottom:
1. search for key
1. replace 2-node with 3-node

Insert into a 3-node at bottom:
1. add new key to 3-node to create temporary 4-node
1. move middle key in 4-node into parent

More detailed:
1. add new key to 3-node to create temporary 4-node
1. move middle key in 4-node into parent
1. repeat up the tree, as necessary
1. if you reach the root and it's a 4-node, split it into three 2-nodes

*Splitting a 4-node is a local transformation: constant number of operations*

Properties:
- maintains symmetric order and perfect balance
- guaranteed logarithmic performance for search and insert

Direct implementation is complicated:
- maintaining multiple node types is cumbersome
- need multiple compares to move down tree
- need to move back up the tree to split 4-nodes
- large number of cases for splitting

### Red-Black BSTs (left-leaning)

1. represent 2-3 tree as BST
1. use "internal" left-leaning links as "glue" for 3-nodes

- red links "glue" nodes within a 3-node
- black links connect 2-nodes and 3-nodes

Equivalent definition: A BST such that
- no node has two red links connected to it
- every path from root to null link has the same number of black links ("perfect black balance")
- red links lean left

*Search operation is same as for BST*
- ignore color
- but faster because better balance
Most other operations are also identical

```java
private static final boolean RED = true;
private static final boolean BLACK = false;

private class Node 
{
    Key key;
    Value val;
    Node left, right;
    boolean color; // color or parent link
}

private boolean isRed(Node x) 
{
    if (x == null) { return false; } // null links are black
    return x.color == RED;
}

// left rotation: orient a (temporarily) right-leading red link to lead left
private Node rotateLeft(Node h)
{
    assert isRed(h.right);
    Node x = h.right;
    h.right = x.left;
    x.left = h;
    x.color = h.color;
    h.color = RED;
    return x;
}

// right rotation: oriend a left-leaning red link to (temporarily) lean right
private Node rotateRight(Node h)
{
    assert isRed(h.left);
    Node x = h.left;
    h.left = x.right;
    x.right = h;
    x.color = h.color;
    h.color = RED;
    return x;
}

// color flip: redolor to split a (temporary) 4-node
private void flipColors(Node h)
{
    assert !isRed(h);
    assert isRed(h.left);
    assert isRed(h.right);

    h.color = RED;
    h.left.color = BLACK;
    h.right.color = BLACK;
}

private Node put(Node h, Key key, Value val)
{ 
    // insert at bottom and color red
    if(h == null) { return new Node(key, val, RED); }
    
    int cmp = key.compareTo(h.key);
    if(cmp < 0) { h.left = put(h.left, key, val); }
    else if(cmp > 0) { h.right = put(h.right, key, val); }
    else { h.val = val; }

    // lean left
    if(isRed(h.right)) && !isRed(h.left)) { h = rotateLeft(h); }
    // balance 4-node
    if(isRed(h.left)) && isRed(h.left.left)) { h = rotateRight(h); }
    // split 4-node
    if(isRed(h.left) && isRed(h.right)) { flipColors(h); }

    return h;
}
```

Cases
- right child red, left child black: rotate left
- left child, left-left grandchild red: rotate right
- both children red: flip colors

*Always reducing one case to another (simpler) case*

Height of tree is <= 2 lg N in the worst case
- every path from root to null link has the same number of black links
- never two red links in a row

### B-Trees

B-tree: generalize 2-3 trees by allowing up to M - 1 key-link pairs per node

A search or insertion in a B-tree of order *M* with *N* keys requires between log<sub>M-1</sub>N and log<sub>M/2</sub>N probes

*Red-black trees are widely used as system symbol tables*

## Geometric Applications of BSTs

### 1d Range Search

*Intersections among geometric objects*

Implementations
data structure | insert | range count | range search | summary
--- | --- | --- | --- | ---
unordered array | 1 | N | N | fast insert, slow range search
ordered array | N | log N | R + log N | slow insert, binary search for *k1* and *k2* to do range search
goal | log N | log N | R + log N |

- N = number of keys
- R = number of keys that match

```java
public int size(Key lo, Key hi)
{
    if(contains(hi)) { return rank(hi) - rank(lo) + 1; }
    else { return rank(hi) - rank(lo); } // number of keys < hi
}
```

### Line Segment Intersection

quadratic algorithm (brute force) -> check all pairs of line segments for intersection

Nondegeneracy assumption: all *x-* and *y-* coordinates are distinct

Sweep-line agorithm: sweep vertical line from left to right
- *x-*coordinates define events
- *h-*segment (left endpoint): insert *y-*coordinate into BST
- *h-*segment (right endpoint): remove *y-*coordinate from BST
- *v-*segment: range search for interval of *y-*endpoints

Time proportional to *N log N + R* to find all *R* intersections among *N* orthogonal line segments

Reduces 2D orthogonal line segment intersection search to 1D range search

### Kd-Trees

Rule of thumb for *M* in 2D orthogonal range search using grid: √N by √N grid

Applications of space-partitioning trees:
- ray tracing
- 2d range search
- flight simulators
- N-body simulation
- Collision detection
- Astronomical databases
- Nearest neighbor search
- Adaptive mesh generation
- Accelerate rendering in Doom
- Hidden surface removal and shadow casting

2d tree -> recursively partition plane into two halfplanes

Data structure: BST, but alternate using *x*- and *y*-coordinates as key
- search gives rectagle containing point
- insert further subdivides the plane
- typical search: R + log N
- worst-case search: R + √N

Nearest neighbor search performance
- typical: log N
- worst: N

Flocking boids -> rules leat to complex emergent flocking behavior
- collision avoidance: point away from *k nearest* boids
- flock centering: point towards the center of mass of *k nearest* boids
- velocity matching: update velocity to be the average of *k nearest* boids

Kd-tree -> k dimensions
- recursively partition one dimension at a time

N-body simulation: simulate the motion of N particles, mutually affected by gravity

Brute force: for each pair of particles, compute force (F = G * m1 * m2 / r^2)

Appel algorithm -> suppose particle is far, far away from cluster of particles
- treat cluster as a single aggregate
- compute force between particle and center of mass of aggregate

1. build 3d-tree with *N* particles as nodes
1. store center-of-mass of each subtree in each node
1. to compute total force acting on a particle, traverse tree, but stop as soon as distance from particle to subdivision is sufficiently large

### Interval Search Trees

-> create BST where each node stores an interval
- use left endpoint as BST *key*
- store **max endpoint** in subtree rooted at node



```java
public class IntervalST<Key extends Comparable<Key>, Value>
{
    // create interval search tree
    public IntervalST()
    {

    }

    // put interval-value pair into ST
    public void put(Key lo, Key hi, Value val)
    {
        // insert into BST, using lo as key
        // update max in each node in search path
    }

    // value paired with given interval
    public Value get(Key lo, Key hi)
    {

    }

    // delete the given interval
    public void delete(Key lo, Key hi)
    {
        
    }

    // all intervals that intersect the given interval
    public Iterable<Value> intersects(Key lo, Key hi)
    {
        // To search for any one interval that intersects query interval (lo, hi)
            // if interval in node intersects query interval, return it
            // else if left subtree is null, go right
            // else if max endpoint in left subtree is less than lo, to right
            // else go left
    }
}
```

Use a red-black BST to guarantee performance

order of growth of running time for N intervals
operation | brute | interval search tree | best in theory
--- | --- | --- | ---
insert interval | 1 | log N | log N
find interval | N | log N | log N
delete interval | N | log N | log N
find any one interval that intersects (lo, hi) | N | log N | log N
find all intervals that intersect (lo, hi) | N | R log N | R + log N

### Rectangle Insertion

Goal: find all intersections among a set of *N* orthogonal rectangles

Quadratic algorithm (brute force): check all pairs of rectangles for intersection

Early 1970's -> microprocessor design became a *geometric* problem

Geometric applications of BST's
problem | solution
--- | ---
1d range search | BST
2d orthogonal line segment intersection search | sweep line reduces to 1d range search
kd range search | kd tree
1d interval search | interval search tree
2d orthogonal rectangle intersection search | sweep line reduces to 1d interval search tree