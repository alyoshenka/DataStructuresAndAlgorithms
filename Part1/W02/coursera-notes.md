# Week 2

## Stacks and Queues

### Stacks

Implementations

- linked-list: maintain pointer to first node; push/pop from front
  - every operation takes constant time in the worst case
  - a stack with N items uses ~ 40 N bytes
    - 16 bytes (object overhead)
    - 8 bytes (inner class extra overhead)
    - 8 bytes (reference to item)
    - 8 bytes (reference to node)
  - analysis includes memory for stack, not memory for stored items
- array: keep array and index for most recently inserted item

  - defect: overflows when N exceeds capacity -> must resize

  ```java
  public class FixedCapacityStackOfStrings
  {
      private String[] s;
      private int N = 0;

      public FixedCapacityStackOfStrings(int capacity)
      {
          s = new String[capacity];
      }

      public boolean isEmpty()
      {
          return N == 0;
      }

      public void push(String item)
      {
          s[N++] = item;
      }

      public String pop()
      {
          return s[--N];
      }
  }
  ```

  - underflow: throw exception
  - overflow: resize array
  - null items? yes
  - loitering -> set "old" object to `null` so GC can collect

_access modifiers don't matter in inner class_

### Resizing Arrays

-> deal with resizing defect

challenge: ensure that array resizing happens infrequently -> "repeated doubling"

```java
private void resize(int capacity)
{
    String[] copy = new String[capacity];
    for(int i = 0; i < N; i++) { copy[i] = s[i]; }
    s = copy;
}
```

cost of inserting first N items: N + (2 + 4 + 8 + ... + N) ~ 3 N

amoritized analysis: consider total cost averaged over all operations

shrinking array?

-> halve size of array when it is quarter full -> `if(N > 0 && N == s.length / 4) { resize(s.length / 2); }`

"every time you resize you've already paid for it in an amoritized sense"

almost all operations are constant time

- only exception is push/pop with array resize -> proportional to N

Memory usage

- uses between \~ 8 N and \~ 32 N bytes to represent a stack with N items
  - analysis
    - 8 bytes (reference to array)
    - 24 bytes (array overhead)
    - 8 bytes \* array size
    - 4 bytes (int)
    - 4 bytes (padding)
  - 8 -> full
  - 32 -> quarter full

Tradeoffs

- Linked list
  - every operation takes constant time in the worst case
  - uses extra time and space to deal with links
- Array
  - every operation takes constant _amoritized_ time
  - less wasted space

### Queues

#### Linked list representation

- maintain pointer to first and last items
- insert to end
- remove from front
- dequeue -> same as for stack
- enqueue -> use reference to end of list to add new link at end

**Linked lists are more often encapsulated**

```java
public class LinkedQueueOfStrings
{
    private Node first, last;

    private class Node { /* not important */ }

    public boolean isEmpty() { return first == null; }

    public void enqueue(String item)
    {
        Node oldLast = last;
        last = new Node(item);
        if(isEmpty()) { first = last; }
        else { oldLast.next = last; }
    }

    public String dequeue()
    {
        String item = first.item;
        first = first.next;
        if(isEmpty()) { last = null; }
        return item;
    }
}
```

#### Array Implementation

-> little trickier

- array `q[]` to store items
- `enqueue()` -> new item at `q[tail]`
- `dequeue()` -> remove item from `q[head]`
- update head and tail modulo the capacity
  - "once you get past the capacity you have to reset to 0"
- allow for resizing of array

### Generics

hack -> implement with `Object` and cast to desired type

- casting required in client
- error-prone! run-time error it types mismatch

**Welcome compile-time errors; avoid run-time errors.**

hard to make an array of `<Type>[]` -> Java does not allow generic array creation

`(Item[]) new Object[capacity]` -> really the only way around it

**"Good code has zero casts"**

primitive types -> autoboxing

### Iterators

`Iterable` -> returns `Iterator`

### Stack and Queue Applications

`ArrayList` -> array

`LinkedList` -> linked list

API's can become too broad/bloated, so it is sometimes better to use a custom (lightweight) implementation

_Can't use a library until we've implemented it in class._

**Don't use a library until you understand its API!**

#### Stack Applications

- parsing in a compiler
- JVM
- undo in word processor
- back button in web browser
- PostScript langueage for printers
- implementing function calls in a compiler

"You can always use an explicit stack to make a recursive program non-recursive"

##### Djikstra's two-stack algorithm

- value: push to value stack
- operator: push to operator stack
- left parenthesis: ignore
- right parenthesis: pop operator and two values; push result of applying operator to values onto value stack

- extensions: more operators, precedence order, associativity -> compilers!

## Elementary Sorts

callback: reference to executable code

### Implementing callbacks

- Java: interfaces
- C: function pointers
- C++: class-type functors
- C#: delegates
- Python, Perl, ML, Javascript: first-class functions

### Total order

- antisymmetry: if v <= w and w <= v then v = w
- transitivity: if v <= w and w <= x then v <= x
- totality: either v <= w or w <= v or both

ex. rock paper scissors is _not_ a total order

v.compareTo(w)

- equal: 0
- v < w: -
- v > w: +
- null or compatible: exception

```java
public int compareTo(Date that)
{
  if (this.year < that.year)  { return -1; }
  if (this.year > that.year)  { return  1; }
  if (this.month < that.month){ return -1; }
  if (this.month > that.month){ return  1; }
  if (this.day < that.day)    { return -1; }
  if (this.day > that.day)    { return  1; }
  return 0;
}
```

```java
// all we really need for sorting (simplified)
private static boolean less(Comparable v, Comparable w)
{
  return v.compareTo(w) < 0;
}

private static void exch(Comparable[] a, int i, int j)
{
  Comparable swap = a[i];
  a[i] = a[j];
  a[j] = a[i];
}
```

Make sure to test data after sort

### Selection Sort

1. in iteration `i`, find index `min` of smalles remaining entry (to the right of `i`)
1. swap a[i] and a[min]

- each iteration need to check through everything for next smallest
- but only needs to swap 2 objects

#### Invariants

- entries to the left of the index are fixed in ascending order
- no entry to the right of the index is smaller than any entry to the left

_Algorithm has to preserve the invariants_

### Insertion Sort

1. in iteration `i`, swap `a[i]` with each larger entry to its left

#### Invariants

- entries to the left of the index are in ascending order
- entries to the right have not yet been seen

-> expect each entry to move halfway back (on average)

- depends on initial order of data

### Shellsort

-> move entries more than one position at a time by _h-sorting_ the array

"An _h-sorted_ array is _h_ different interleaved sorted subsequences."

Shellsort: _h-sort_ array for decreasing sequence values of _h_

How to _h-sort_ an array? -> Insertion sort, with stride length _h_

-> instead of going `1` back, go `h` back

**A _g-sorted_ array remains _g-sorted_ after _h-sorting_ it**

-> k < g, so it can't "override" order

#### Which increment sequence to use?

- powers of two: NO!
  - doesn't compare elements in even positions with elements in odd positions ungil _1-sort_
- powers of two minus one: Maybe
- powers of three minus one: Okay
  - easy to compute
- Sedgewick: Good
  - complicated numerical sequence

Tricky to find the best increment sequence!

```java
public class Shell
{
  public static void sort(Comparable[a])
  {
    int N = a.length;
    int h = 1;
    while(h < N/3) { h = 3 * h + 1; } // find starting increment

    while(h >= 1)
    {
      // h-sort the array
      for(int i = h; i < N; i++)
      {
        // insertion sort
        for(int j = i; j >= h && les(a[j], a[j-h]); j -= h)
        {
          exch(a, j, j-h);
        }
      }

      h = h/3; // move to next increment (int type handles the -1)
    }
  }
}
```

analysis -> still open, somewhat

Simple idea leading to substantial performance gains

Useful in practice

- fast, unless array size is huge
- tiny, fixed footprint for code (used in embedded systems)
- hardware sort prototype

### Shuffling

#### Shuffle sort

1. generate a random real number for each array entry
1. sort the array, using the random number

- cost of the sort?

#### Knuth shuffle

1. in iteration `i`, pick integer `r` between `0` and `i` uniformly at random
1. swap `a[i]` and `a[j]`

- cards to the left of `i` are (uniformly random) shuffled

-> linear time

```java
public class StdRandom
{
  public static void shuffle(Object[] a)
  {
    int N = a.length;
    for(int i = 0; i < N; i++)
    {
      int r = StdRandom.uniform(i + 1); // [0, i]
      exch(a, i, r);
    }
  }
}
```

**Picking a number [0, N] does not get a uniformly random result. Must be [0, i]**

#### War Story: Online Poker

```
for i := 1 to 52 do begin
  r := random(51) + 1;
  swap := card[r];
  card[r] := card[i];
  card[i] := swap;
end
```

1. random number _r_ never 52

- 52nd card can't end up in 52nd place

1. shuffle not uniform

- should be between 1 and i

1. `random()` uses 32-bit seed

- 2<sup>32</sup> possible shuffles -> not enough

1. seed = number of milliseconds since midnight

- 86.4 million shuffles

exploit: after seeing 5 cards and synchronizing server clock, can determine all future cards in real time

"The generation of random numbers is too important to be left to chance."

### Complex Hull

**The _convex hull_ of a set of _N_ points is the smallest perimeter fence surrounding the points.**

output: sequence of vertices in CCW order

Mechanical algorithm: hammer nails perpendicular to plane; stretch elastic rubber band around them

#### Applications

- motion planning
  - find shortest path that avoids polygonal obstacles
  - shortest path: either straight line, or convex hull
- farthest pair problem
  - given _N_ points in the plane, find a pair of points with the largest Euclidean distance between them
  - farthest pair of points are extreme points on convex hull

#### Geometric properties

- can traverse hull by making only CCW turns
- vertices of convex hull appear in increasing order of polar angle with respect to point _p_ with lowest _y_-coordinate

#### Graham scan

1. choose point _p_ with smallest _y_-coordinate

- define total-order, comparing _y_-coordinate

1. sort points by polar angle with _p_

- define a total-order for _each_ point _p_
- mergesort

1. consider points in order; discard unless it creates a CCW turn

- computational geometry
- how to handle degeneracies (multiple points on a line)?

##### Implementing CCW

given three points, _a_, _b_, and _c_, is a -> b -> c a CCW turn? (is c to the left of a->b)

- determinant (cross product) gives 2x signed area of planar triangle

- 2 \* area(a, b, c) -> (b - a) x (c - a)

| a<sub>x</sub> a<sub>y</sub> 1 |

| b<sub>x</sub> b<sub>y</sub> 1 |

| c<sub>x</sub> c<sub>y</sub> 1 |

- signed area > 0: a->b->c = CCW
- signed area < 0: a->b->c = CC
- signed area = 0: a->b->c = collinear

```java
public static int ccw(Point2D a, Point2D b, Point2D c)
{
  double area2 = (b.x-a.x)*(c.y-a.y) - (b.y-a.y)*(c.x-a.x);
  if (area2 < 0) { return -1; }
  else if (area2 > 0) { return 1; }
  else { return 0; }
}
```

```java
// graham scan implementation
Stack<Point2D> hull = new Stack<Point>();

Arrays.sort(p, Point2D.Y_ORDER); // p[0] is now the point with the lowest y-coordinate
Arrays.sort(p, p[0].BY_POLAR_ORDER); // sort by polar angle with respect to p[0]

hull.push(p[0]); // definitely on hull
hull.push(p[1]);

for(int i = 2; i < N; i++){
  Point2D top = hull.pop();
  while(Point2D.ccw(hull.peek(), top, p[i]) <= 0)
  {
    // discard points that would make CW turn
    top = hull.pop();
  }
  hull.push(top);
  hull.push(p[i]); // add p[i] to putative hull
}
```

