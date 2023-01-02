# Week 4: Priority Queues

## APIs and Elementary Implementations

priority queue -> remove largest (or smallest) item

`public class ClassName<Key extents Comparable<Key>>` -> key type must be `Comparable`

Challenge: find the largest *M* items in a stream of *N* items

Constraint: not enough memory to store *N* items

```java
MinPQ<Transaction> pq = new MinPQ<Transaction>();

while(StdIn.hasNextLine())
{
    String line = StdIn.readLine();
    Transaction item = new Transaction(line);
    pq.insert(item);
    if(pq.size() > M) { pq.delMin(); }
}
```

Order of growth

implementation | time | space
--- | --- | ---
sort | N log N | N
elementary PQ | M N | M
binary heap | N log M | M
best in theory | N | M

```java
// unordered array implementation

public class UnorderedMaxPQ<Key extends Comparable<Key>>
{
    private Key[] pq;
    private int N;

    public UnorderedMaxPQ(int capacity) { pq = (Key[]) new Comparable[capacity]; }

    public boolean isEmpty() { return N == 0; }

    public void insert(Key c) { pq[N++] = x; }

    public Key delMax()
    {
        int max = 0;
        for(int i = 1; i <  N; i++)
        {
            if(less(max, i)) { max = i; }
        }
        exch(max, N-1);
        return pq[--N]; // null out entry to prevent loitering
    }
}
```

Challenge: implement *all* operations efficiently

implementation | insert | del max | max
--- | --- | --- | ---
unordered array | 1 | N | N
ordered array | N | 1 | 1
**goal** | *log N* | *log N* | *log N*

### Binary Heaps

*complete binary trees actually happen in nature*

binary heap -> array representation of heap-ordered complete binary tree

Heap-ordered binary tree
- keys in nodes
- parent's key no smaller than children's keys

Array representation
- indicies start at 1
- take nodes in level order
- no explicit links needed

Propositions: 
- largest key is at a[1], which is the root of the binary tree
- can use array indices to move through tree
    - parent of node k is at k/2
    - children of node at k are 2k and 2k+1

Scenario: child's key becomes larger than its parent's key
1. exchange key in child with key in parent
1. repeat until heap order restored
    ```java
    private void swim(int k)
    {
        while(k > 1 && less(k/2, k))
        {
            exch(k, k/2);
            k = k/2; // parent of node k is at k/2
        }
    }
    ```

Insertion -> add node at end, then swim it up
- at most 1 + lg N compares
    ```java
    public void insert(Key x)
    {
        pq[++N] = x;
        swim(N);
    }
    ```

Scenario: parent's key becomes smaller than one (or both) of its chilcren's
1. exchange key in parent with key in larger child
1. repeat until heap order restored
    ```java
    private void sink(int k)
    {
        while(2*k <= N)
        {
            // children of node k are 2k and 2k+1
            int j = 2*k;
            if(j < N && less(j, j+1)) { j++; }
            if(!less(k, j)) { break; }
            exch(k, j);
            k = j;
        }
    }

Delete maximum -> exchange root with node at end, then sink it down
- at most 2 lg N compares
    ```java
    public Key delMax()
    {
        Key max = pq[1];
        exch(1, N--);
        sink(1);
        pq[N+1] = null; // prevent loitering
        return max;
    }
    ```

*Use immutable keys*

Minimum-oriented pq -> replace `less()` with `greater()` (implement `greater()`)

Other options: 
- remove arbitrary item
- change the priority of an item

Immutable: String, Integer, Double, Color, Vector, Transaction, Point2D

Mutable: StringBuilder, Stack, Counter, Java array

### Heapsort

Basic plan for in-place sort
1. bottom-up: create max-heap with all *N* keys
1. sortdown: repeatedly remove the maximum key 
    - this puts the array in order because items are exchanged (don't `null`ify)

first pass: build heap using bottom-up method
```java
for(int k = N/2; k >= 1; k--) { sink(a, k, N); }
```
second pass:
- remove the maximum, one at a time
- leave in array, instead of nulling out
```java
while(N > 1)
{
    exch(a, 1, N--);
    sink(a, 1, N);
}
```

```java
// heapsort

public class Heap 
{
    public static void sort(Comparable[] pq)
    {
        int N = pq.length;
        for(int k = N/2; k >= 1; k--) { sink(a, k, N); }
        while(N > 1)
        {
            exch(a, 1, N);
            sink(a, 1, --N);
        }
    }

    // remember to convert from 1-based to 0-based indexing
}
```

construction: <= 2 N compares and exchanges

heapsort: <= 2 N lg N compares and exchanges

Significance: in-place sorting algorithm with N log N worst-case
- mergesort: no, linear extra space
- quicksort: no, quadratic time in worst case
- heapsort: yes!

*"What is an in-place algorithm with N log N guarantee?"* **Heapsort**

Bottom line: optimal for both time and space, *but*:
- inner loop longer than quicksort's
- poor use of cache memory
- not stable (long-distance exchanges; use mergesort)

### Event-Driven Simulation

*Goal: simulate the motion of* N *moving particles that behave according to the laws of elastic collison*

Hard disk model:
- moving particles interact via elastic collisoins with each other and walls
- each particle is a disk with knows position, velocity, mass, and radius
- no other forces

Significance: relates macroscopic obervables to microscopic dynamics
- macroscopic -> temperature, pressure, diffusion constant
- microscopic -> motion of individual atoms and molecules

#### Time-driven simulation Drawbacks
- ~ N/2 overlap checks per time quantum
- too slow if *dt* very small
- may miss collsions if *dt* too large

### Change state only when something happens
- between collisions, particles move in straight-line trajectories
- focus only on times when collisions occur
- maintain **PQ** of collision events, prioritized by time
- remove min = get collision

Collision prediction + resolution

1. Initialization (quadratic, but only happens once)
- fill PQ with all potential particle-wall collisions
- fill PQ with all potential particle-particle collisions
1. Main Loop
- delete impending event from PQ (min priority = *t*)
- if event has been invalidated -> ignore it
- advance all particles to time *t*, on a straight-line trajectory
- update the velocities of the colliding particle(s)
- predict future particle-wall and particle-particle collisions involving the colliding particle(s) and insert events onto PQ