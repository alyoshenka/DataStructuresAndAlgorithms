# Week 1 Coursera

## Course Introduction

algorithm: method for solving a problem

data structure: method of storing information

good programmer considers data structures more important than code

computational models are replacing scientific models in science

## Readings

### 1.4 Analysis of Algorithms

"How long will my program take?"

"Why does my program run out of memory?"

*How much does running time increase compared to problem size?*

Running times on different computers are likely to differ by a constant factor.

#### Power Law

Straight line on log-log plot -> T(N) = aN^b 

a -> constant

b -> slope

Running time of program determined by:

- cost of executing each statement (property of computer, compiler, and OS)

- frequency of execution of each statement (property of program and input)

tilde notation (~): approximate away lower-order terms

#### Typical tilde approximations

function | tilde approximation | order of growth
--- | --- | ---
N^3/6 - N^2/2 + N/3 | ~ N^3/6 | N^3
N^2/2 | ~ N^2 | N^2
lg N + 1 | ~ lg N | lg N
3 | ~ 3 | 1

#### Orders of Growth

description | function
--- | ---
constant | 1
logarithmic | log N
linear | N
linearithmic | N log N
quadratic | N^2
cubic | N^3
exponential | 2^N

*the running times of a great many programs depend only on a small subset of their instructions*

The algorithm determines the order of growth, not the language/OS/etc.

#### Summary

##### Mathematical model of running time

1. develop input model, include definition of problem size
1. identify inner loop
1. define cost model that includes operations in the inner loop
1. determine frequency of execution of operations
    - might require mathematical analysis

##### Commonly encountered functions in algorithm analysis

description | notation | definition
--- | --- | ---
natural logarithm | ln N | log<sub>e</sub>N (such that e<sup>x</sup> = N)
binary logarithm | lg N | log<sub>2</sub>N (such that 2<sup>x</sup> = N)
integer binary logarithm | ⎣lg N⎦ | largest integer not greater than lg N (# bits in binary representation of N) - 1
harmonic numbers | H<sub>N</sub> | 1 + 1/2 + 1/3 + ... + 1/N
fatorial | N! | 1 * 2 * 3 * ... * N

##### Useful approximations for the analysis of algorithms

description | function | sequence | approximation
--- | --- | --- | ----
harmonic sum | H<sub>N</sub> | 1 + 1/2 + ... + 1/N | ~ ln N
triangular sum | | 1 + 2 + 3 + ... + N | ~ N<sup>2</sup>/2
geometric sum | | 1 + 2 + 4 + 8 + ... + N | = 2N - 1; ~ 2N when N = 2<sup>n</sup>
Stirling's approximation | lg N! | lg 1 + lg 2 + lg 3 + ... + lg N | ~ N lg N
binomial coefficients | ( N / k ) | | ~ N<sup>k</sup>/k! when k is a small constant
exponential | (1-1/x)<sup>x</sup> | | ~ 1/e

##### Order of growth classifications

- constant: fixed amount of time, irrespective of input size (most Java operations)
- logarithmic: barely slower than constant, base is not relevant with respect to order of growth; ex. *binary searth*
- linear: proportional to N; ex. based on a single `for` loop
- linearithmic: problem size N has OOG N log N; ex. `Merge.sort()`, `Quick.sort()`
- quadratic: ex. order of growth N<sup>2</sup> has 2 nested for loops; elementary sorting algorithms: `Selection.sort()`, `Insertion.sort()`
- cubic: ex. 3 nested `for` loops
- exponential: very slow!

##### Summary of common OOG hypotheses

name | description | example 
--- | --- | ---
constant | statement | add two numbers
logarithmic | divide in half | binary search
linear | loop | find the maximum
linearithmic | divide and conquer | mergesort
quadratic | double loop | check all pairs
cubic | triple loop | check all triples
exponential | exhaustive search | check all subsets

##### Doubling Ratio

If T(N) ~ *a*N<sup>b</sup>lgN then T(2N)/T(N) ~ 2<sup>b</sup>

T(2N)/T(N) = *a*(2N)<sup>b</sup>lg(2N)/*a*N<sup>b</sup>lgN = 2<sup>b</sup>(1 + lg2 / lgN) ~ 2<sup>b</sup>

##### Caveats

- large constants
- nondominant inner loop
- instruction time -> sometimes the same instruction takes a different amount of time (caching)
- system considerations -> competition for resources
- too close to call 
- strong dependence on inputs
- multiple problem parameters

##### Coping with dependence on inputs

- input models -> they can be improved
- worst-case performance guarantees 
- randomized algorithms
- sequences of operations
- amoritized analysis -> spread the cost of expensive operations

##### Memory

Analyzing memory usage is easier than analyzing running time.

Requirements for arrays

type | bytes
--- | ---
int[] | ~4N
double[] | ~8N
Date[] | ~40N
double[][] | ~8NM

*a substring takes constant extra memory (40 bytes) and forming a substring takes constant time*

method calls -> stack

new -> heap

### 1.5 Case Study: Union Find (pg 229 in pdf)

## Lectures

### Dynamic Connectivity Problem

Steps to develop a usable algorithm

set of N objects

connect 2 objects (multiple times)

is there a path? (find the path -> pt 2)

#### Modeling Connections

- reflexive: p is connected to p
- symmetric: if p is connected to q, then q is connected to p
- transitive: if p is connected to q and q is connected to r, then p is connected to r  

connected components: maximal set of objects that are mutually connected

find query -> check if two objects are in the same component

union command -> replace components containing two objects with their union

can be many objects (N) and many operations (M)

```java
public class UF{
    UF(int N)
    void union(int p, int q)
    boolean connected(int p, int q)
    int find(int p)
    int count()
}
```

### Quick Find

"eager algorithm"

p and q are connected iff (if and only if) they have the same id

if p and q have the same id -> they're connected

union is more difficult -> have to change all id entries

```java
public class QuickFindUF
{
    private int[] id;

    public QuickFindUF(int N)
    {
        id = new int[N];
        // set id for each object to its index
        for(int i = 0; i < N; i++) { id[i] = i; }
    }

    public boolean connected(int p, int q)
    {
        // fairly straightforward
        return id[p] == id[q];
    }

    public void union(int p, int q)
    {
        int pid = id[p];
        int qid = id[q];
        // at most 2N + 2 array accesses
        for(int i = 0; i < id.length; i++){
            // set all p's to q's
            if(id[i] == pid) { id[i] = qid; }
            // common mistake: id[i] = id[p] -> doesn't work because id[p] will change
        }
    }
}
```

Cost model: number of array accesses

- initialize: N
- union: N
- find: 1

union is too expensive -> takes N<sup>2</sup> array accesses to process sequence of N union commands on N objects

defects:
- union is too expensive
- trees are flat, but too expensive to keep them flat

**Quadratic time is much too slow** -> they don't scale

### Quick Union

"lazy approach"

interpretation: id[i] is a parent of i

root of i is id[id[id...[id[i]...]]] -> keep going until it doesn't change

-> tree

find -> check if p and q have the same root (little more complex here)

union -> to merge components containing p and q, set the id of p's root to the id of q's root

union(4, 3) -> "4 is childed to 3"

```java
public class QuickUnionUF
{
    private int[] id;

    // N array accesses
    public QuickFindUF(int N)
    {
        id = new int[N];
        for(int i = 0; i < N; i++) { id[i] = i; }
    }

    private int root(int i)
    {
        // chase parent pointers until reach root
        // depth of i array accesses
        while(i != id[i]) { i = id[i]; }
        return i;
    }

    // check if p and q have the same root
    public boolean connected(int p, int q){
        // depth of p and q array accesses
        return root(p) = root(q);
    }

    public void union(int p, int q)
    {
        // change root of p to point to root of q
        // depth of p and q array accesses
        int i = root(p);
        int j = root(q);
        id[i] = j;
    }
}
```

Cost model

- initialize: N
- union: N (includes cost of finding roots)
- find: N (worst case)

defects:
- trees can get tall
- find is too expensive

### Quick Union Improvements

No linear solution, but we can get pretty close

1. Weighting
    - modify quick union to avoid tall trees
    - keep track of the size of each tree
    - balance by linking root of smaller tree to root of larger tree
    - when combining large and small tree, put the small tree lower
    - decreases average distance to root
    - Implementation
        - maintain extra array sz[i] to count number of objects in the tree rooted at i
        ```java
        // find -> same as quick union
        return root(p) == root(q);

        // union
        // link root of smaller tree to root of larger tree
        // update sz[] array
        int i = root(p)
        int j = root(q)
        if(i == j) { return; }
        if(sz[i] < sz[j]) { id[i] = j; sz[j] += sz[i]; }
        else { id[j] = i; sz[i] += sz[j]; }
        ```
    - Analysis (time)
        - find: proportional to depth of p and q
        - union: constant time, given roots
    - Proposition: depth of any node x is at most lgN
        - lg = base-2 logarithm
    - Proof: When does the depth of x increase?
        - increases by 1 tree T1 is merged into another tree T2
            - the size of tree containing x at least doubles because T2 > T1
            - size of tree containing x can double at most lgN times
                - Why?
                    - If you start with 1 and double lgN times, you get N
    - initialize: N
    - union: lgN (includes cost of finding roots)
    - connected: lgN
    - Stop at guranteed acceptable performance? No! We can do better.
1. Path Compression
    - just after computing the root of p, set the id of each examined node to point to that root
    - constant extra cost: we went up the tree once to find the root, now we go up once more to "flatten the tree out"
    - Implementation
        1. Two pass implementation: add second loop to `root()` to set the `id[]` of each examined node to root
        1. Simpler one-pass variant: make every other node in path point to its grandparent (therby halving path length)
            ```java
            private int root(int i)
            {
                while (i != id[i])
                {
                    id[i] = id[id[i]]; // only one extra line of code
                    i = id[i];
                }
                return i;
            }
            ```
            - in practice: no reason not to! keeps the tree almost completely flat
    - Analysis -> iterate log funciton (really fast)

in theory, not quite linear
in practice, basically linear

algorithm | worst-case time
--- | ---
quick-find | M * N
quick-union | M * N
weighted QU | N + M log N
QU + path compression | N + M log N
weigthed QU + path comression | N + M lg* N

ex. 10<sup>9</sup> unions and finds with 10<sup>9</sup> objects
- WQUPC reduces time from 30 years to 6 seconds

**Supercomputers won't help much; good algorithm enables solution.**

### Union Find Applications

- image processing -> how to label areas in images
- graph processing
- percolation -> model for many physical systems

#### Percolation 

System percolates iff top and bottom are connected by open sites

Likelihood of percolation depends on site vacancy probabiliy p
- low p (0.4) -> does not percolate
- medium p (0.6) -> percolates?
- high p (0.8) -> does percolate
How do we know whether it will percolate or not?

Percolation phase transition

When N is large, theory guarantees a sharp threshold p*
- p > p* -> almost certainly percolates
- p < p* -> almost certainly does not percolate

#### Monte Carlo simulation

- initialize N-by-N grid to blocked
- declare random sites open until top is connected to bottom
- vacancy percentage estimates p*

clever trick: create "virtual sites" on top and bottom (all bottom nodes connected to bottom virtual site, etc)

p ~ 0.593

## Analysis of Algorithms

### Introduction

- programmer -> solve a problem
- client -> solve problem efficiently
- theoreticion -> understand
- student -> all of the above

-> mainly interested in performance predictions

"Will my program be able to solve a large practical input?"

Use scientific method to understand algorithm performance

- experiments must be *reproducible*
- hypotheses must be *falsifiable* 

### Observations

three sum problem: with N integers, how many triples sum to exactly 0?

context: computational geometry

initial solution (brute force) -> triple `for` loop

log-log plot -> a N <sup>b</sup>
- b = slope
- N = input size
- a = "constant"

Doubling hypothesis -> double input and compare ration of outputs -> lg ration converges to power constant (*b*)

> T(n) = a*n<sup>b</sup> 

> double input size, see how output changes (input * 2 = output * 4 -> b = 2

> T(N) = O (T(64000) = 20.5), solve for *a*

> (a = 20.5 / 64000<sup>2</sup> = 5*10^-9)

#### System independedt effects
- algorithm
- input data

#### System dependent effects
- hardware
- software
- system (other programs using memory/processing power)

### Mathematical Models

total running time = sum of cost * frequency for all operations

repeated string concatenation = bad

Look at number of instructions as a function of input size

Can simplify to only the most frequent (expensive) operations, and (ususually) everything else is negligible

-> ignore low order terms (tilde (~) notation)

> f(N) ~ g(N) -> lim<sub>N → ∞</sub>f(N)/g(N) = 1

How to estimate a discrete sum?

- replace the sum with an integral and use calculus

### Order-of-Growth Classifications

only a few functions, for the most part

- good
1. constant
1. logarithmic
- okay
1. linear
1. linearithmic
- bad
1. quadratic
1. cubic
1. exponential


- no loops -> constant
- divide in half -> logarithmic
- loop (touch everything) -> linear
- divide and conquer -> linearithmic
- double loop -> quadratic
- triple loop -> cubic
- exhaustive search -> exponential

**Bottom line: need linear or linearithmic (at least) to keep pace with Moore's Law**

#### Binary Search

Binary search is rather difficult to get exactly right

```java
public static int binarySearch(int[] a, int key)
{
    int lo = 0; int hi = a.length-1;
    while(lo <= hi)
    {
        int mid = lo + (hi - lo) / 2;
        if (key < a[mid]) { hi = mid - 1; }
        else if (key > a[mid]) { lo = mid + 1; }
        else { return mid; }
    }
    return -1;
}
```

At most 1 + lg N compares

T(N) = number of compares to binary search in a sorted subarray with size <= N

T(N) <= T(N/2) + 1 for N > 1 with T(1) = 1

T(N) <= T(N/2) + 1

<= T(N/4) + 1 + 1

<= T(N/8) + 1 + 1 + 1

...

<= T(N/N) + 1 + 1 + ... + 1

= 1 + lg N

(only exact if N is a power of 2)

Quicker Search for 3-Sum:

1. sort the array
1. for each pair of numbers (a, b), binary search for (-(a+b))

### Theory of Algorithms

#### Types of analyses
- best case: lower bound on cost
    - "easiest" input
    - provides a goal
- worst case: upper bound on cosr
    - "most difficult" input
    - provides a guarantee
- average case: expected cost for random input
    - need a model for "random"
    - provides a way to predict performance

Compares for binary search
- best: 1
- average: lg N
- worst: lg N

Goals:
- establish "difficulty" of a problem
- develop "optimal" algorithms

#### Common Notations
notation | provides | used to
--- | --- | --- 
Big Theta | asymtotic order of growth | classify algorithms
Big Oh | θ(N<sup>2</sup>) and smaller | develop upper bounds
Big Omega | θ(N<sup>2</sup>) and larger | develop lower bounds

optimal algorithm: upper bound = lower bound

Big Oh -> NOT approximate model

use tilde (~) notation

### Memory

Name | Size
--- | ---
Bit | 0 or 1
Byte | 8 bits
Megabyte | 1 million or 2<sup>20</sup> bytes
Gigabyte | 1 billion or 2<sup>30</sup> bytes

Assume 64-bit machine with 8 byte pointers
- address more memory
- pointers use more space

#### Java Memory Usage
type | bytes
--- | ---
boolean | 1
byte | 1
char | 2
int | 4
float | 4
long | 8
double | 8
--- | ---
char[] | 2N + 24
int[] | 4N + 24
double | 8N + 24
--- | ---
char[][] | ~ 2 M N
int[][] | ~ 4 M N
double[][] | ~ 8 M N


- object overhead -> 16 bytes 
- reference -> 8 bytes
- padding -> multiple of 8 bytes
- string -> 2N + 64 bytes

Shallow memory usage: don't count referenced objects

Deep memory usage: if array entry or instance variable is a reference, add memory (recursively) for referenced object

Empirical analisys -> perform experiments

Mathematical analysis -> analyze frequency of operations

Scientific method
- mathematical models are independent of computer system
- emperical analysis is necessary to validate mathematical models

