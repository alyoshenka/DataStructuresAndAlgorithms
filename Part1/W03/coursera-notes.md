# Week 3 - Sorts

## Mergesort

- Java sort for objects
- Perl, C++ stable sort, Python stable sort, FireFox Javascript...

basics
- divide array in half
- recursively sort
- merge halves

```java
public class Merge{
    private static void merge(Comparable a[], Comparable aux[], int lo, int mid, int hi)
    {
        assert isSorted(a, lo, mid);
        assert isSorted(a, mid+1, hi);

        // copy
        for(int k = lo; k <= hi; k++) { aux[k] = a[k]; }

        // merge
        int i = lo, j = mid+1;
        for(int k = lo; k <= hi; k++)
        {
            if (i > mid) { a[k] = aux[j++]; }
            else if (j > hi) { a[k] = aux[i++]; }
            else if (less(aux[j], aux[i])) { a[k] = aux[j++]; }
            else { a[k] = aus[i++]; }
        }

        assert isSorted(a, lo, hi);
    }

    private static void sort(Comparable[] a, Comparable[] aux, int lo, int hi)
    {
        if (hi <= lo) { return; }
        if(hi <= lo + CUTOFF - 1) {
            Insertion.sort(a, lo, hi);
            return;
        }
        int mid = lo + (hi - lo) / 2;
        sort(a, aux, lo, mid);
        sort(a, aux, mid+1, hi);
        if (!less(a[mid+1], a[mid])) { return; }
        merge(a, aux, lo, mid, hi);
    }

    public static void sort(Comparable[] a)
    {
        aux = new Compareable[a.length]; // don't create in recursive routine (expensive)
        sort(a, aux, a.length - 1);
    }
}
```

*Assertions validate assumptions*

`assert thing()` -> exception if false

`java -ea Program` -> enable assertions

`java -da Program` -> disable assertions

Proposition
- at most N lg N compares and 6 N lg N array accesses to sort any array of size N
- extra space proportional to N

**A sorting algorithm is `in-place` if it uses <= *c log N* extra memory.**

Practical improvement -> use mergesort for tiny (< 7) items

Also stop if already sorted (biggest item in first half <= smallest element in second half)

### Bottom-up Mergesort

-> no recursion

Bottom line: concise, industrial-strength code, if you have the space

Basic idea
- pass through array, merging subarrays of size 1
- repeat for subarrays of size 2, 4, 8, ...

```java
public class MergeBU
{
    private static Comparable aux[];

    private static void merge(Comparable[] a, int lo, int mid, int hi) { /* same as before */ }

    public static void sort(Comparable[] a)
    {
        int N = a.length;
        aux = new comparable[N];
        for(int sz = 1; sz < N; sz *= 2)
        {
            for(int lo = 0; lo < N-sz; lo += sz*2)
            {
                merge(a, lo, lo+sz-1, Math.min(lo+sz*2-1, N-1));
            }
        }
    }
}
```

### Sorting complexity

computational complexity: framework to study efficiency of algorithms for solving a particular problem

model of computation -> allowable operations

cost model -> operation count(s)

upper bound -> cost guarantee 

lower bound -> proven limit on const guarantee

optimal algorithm -> algorithm with best possible cost guarantee (lower bound - upper bound)

Example: sorting
- model of computation: decision tree (can access information only through compares)
- cost model: # of compares
- upper bound: ~ N lg N for mergesort
- lower bound: ~ N lg N for mergesort
- optimal algorithm: yes
    - Compares? *is* optimal
    - Space usage? *is not* optimal

**Any compare-based sorting algorithm must use at least ln(N!) ~ N ln N compares in the worst case**

Complexity can improve with more information
- initial order of input
- distribution of key values
- representation of keys

### Comparators

`public interface Comparator<Key>`

`int compare(Key v, Key w)`

```java
// insertion sort using a Comparator

public static void sort(Object[] a, Comparator comparator)
{
    int N = a.length;
    for(int i = 0; i < N; i++)
    {
        for(int j = i; j > 0 && less(comparator, a[j], a[j-1]); j--)
        {
            exch(a, j, j-1);
        }
    }
}

private static boolean less(Comparator c, Object v, Object w)
{
    return c.compare(v, w) < 0;
}

private static void exch(Object[] a, int i, int j)
{
    Object swap = a[i];
    a[i] = a[j];
    a[j] = swap;
}
```

to implement Comparator
- define a nested class that implements `Comparator` interface
- implement the `compare()` method

CCW-based Polar Order
- if *q1* is above *p* and *q2* is below *p*, then *q1* makes a smaller polar angle
- if *q1* is below *p* and *q2* is above *p*, then *q1* makes a larger polar angle
- otherwise, ccw(p, q1, q2) identifies which of *q1* or *q2* makes a larger angle

### Stability

stable sort: maintains the relative order of items with equal keys
- insertion sort and mergesort
- NOT selection sort or shellsort

Note: need to carefully check code -> `<` vs `<=`

*check for a long-distance exchange that might move an item past some equal item*



## Quicksort

- Java sort for primitive types
- C qsort, Unix, Visual C++, Python, Matlab, Chrome Javascript...

- top 10 algorithms in 20th century in STEM

Basic plan
- shuffle the array
- partition so that for some *j*
    - entry a[j] is in place
    - no larger entry to the left of *j*
    - no smaller entry to the right if *j*
- sort each piece recursively

Phase I -> repeat until *i* and *j* pointers cross
- scan *i* from left to right so long as a[i] < a[lo]
- scan *j* from right to left so long as a[j] > a[lo]
- exchange a[i] with a[j]

```java
public class Quick
{
    private static int partition(Comparable[] a, int lo, int hi)
    {
        int i = lo, j = hi+1;
        while(true){
            // find item on left to swap
            while(less(a[++i]), a[lo]) { if(i == hi) { break; } }
            // find item on right to swap
            while(less(a[lo], a[--j])) { if(j == lo) { break; } }
            // check if pointers cross
            if(i >= j) { break; }
            // swap
            exch(a, i, j);
        }
        // swap with partitioning item
        exch(a, lo, j);
        // return index of item now known to be in place
        return j;
    }

    public static void sort(Comparable[] a)
    {
        // shuffle needed for performance guarantee
        StdRandom.shuffle(a);
        sort(a, 0, a.length-1);
    }

    private static void sort(Comparable[] a, int lo, int hi)
    {
        if(hi <= lo) { return; }

        if(hi <= lo + CUTOFF-1)
        {
            Insertion.sort(a, lo, hi);
            return;
        }

        int m = medianOf(a, lo, lo + (hi - lo)/2, hi);
        swap(a, lo, m);

        int j = partition(a, lo, hi);
        sort(a, lo, j-1);
        sort(a, j+1, hi);
    }
}
```

Implementation details
- partitioning in-place -> no extra space needed
- terminating the loop -> tricky to test whether pointers cross
- staying in bounds 
- preservind randomness -> shuffling is needed for performance guarantee
- equal keys -> when duplicates are present, it is (counter-intuitively) better to stop on keys equal to the partitioning item's key

Complexity
- best case: N lg N compares
- worst case (array in order): 1/2 N^2
    - quadratic
- average case: 2 N ln N compares, 1/3 N ln N exchanges
    - 39% more compares than mergesort
    - *But* faster in practice because of less data movement

Random shuffle -> probablilistic guarantee against worst case

Caveats -> many textbook implementations go quadratic if array:
- is sorted or reverse sorted
- has many duplicates, even if randomized

**not stable**

Practical improvements
- insertion sort small subarrays (cutoff at 10 items)
- median of sample (not best for large arrays)
    - best choice of pivot item = median
    - estimate true median by taking median of sample

### Selection

Given an array of N items, find *k*<sup>th</sup> largest

Applications
- order statistics
- find the "top *k*"

#### Quick select

Partition the array so that:
- entry a[j] is in place
- no larger entry to the left of *j*
- no smaller entry to the right of *j*
Repeat in one subarray, depending on *j*; finish when *j* equals *k*

```java
public static Comparable select(Comparable[] a, int k)
{
    StdRandom.shuffle(a);
    int lo = 0, hi = a.length-1;
    while(hi > lo)
    {
        int j = partition(a, lo, hi);
        if(j < k) { lo = j + 1; }
        else if(j > k) { hi = j - 1; }
        else { return a[k]; }
    }
    return a[k];
}
```

linear time on average

quadratic time in the worst case (reduced with random shuffle)

**Seek practical linear-time (worst-case) algorithms**

### Duplicate Keys

often, sorts are used to bring items with equal keys together

Mergesort with duplicate keys
- always between 1/2 N lg N and N lg N compares
Quicksort with duplicate keys
- algorithm goes quadratic unless partitioning stops at equal keys!

Duplicate keys: the problem
- mistake: put all items equal to the partitioning item on one side
- consequence: 1/2 N<sup>2</sup> compares when all keys are equal

- recommended: stop scans on items equal to the partitioning item
- consequence: ~ N lg N compares when all keys equal

- desirable: put all items equal to the partitioning item in place

#### 3-way partitioning

Goal: partition array into 3 parts so that:
- entries between *lt* and *gt* equal to partition item *v*
- no larger entries to the left of *lt*
- no smaller entries to the right of *gt*

Steps
- let *v* be partitioning item a[lo]
- scan *i* from left to right
    - a[i] < v: exchange a[lt] with a[i]; decrement both *lt* and *i*
    - a[i] > v: exchange a[gt] with a[i]; decrement *gt*
    - a[i] == v: increment *i*

```java
private static void sort(Comparable[] a, int lo, int hi)
{
    if(hi <= lo) { return; }
    int lt = lo, gt = hi;
    Comparable v = a[lo];
    int i = lo;
    while(i <= gt)
    {
        int cmp = a[i].compareto(v);
        if(cmp < 0) { exch(a, lt++, i++); }
        else if(cmp > 0) { exch(a, i, gt--); }
        else { i++; }
    }
    sort(a, lo, lt - 1);
    sort(a, gt + 1, hi);
}
```

*Randomized quicksort with 3-way partitioning reduces running time from linearithmic to linear in broad class of applications.*

### System sorts

Applications have diverse attributes
- stable
- parallel
- deterministic
- distinct keys
- multiple key types
- linked list or array
- large or small items
- randomly ordered
- need guaranteed performance

#### Sorting summary

sort | in place? | stable? | worst | average | best | remarks
--- | --- | --- | --- | --- | --- | --- |
selection | Y | N | N^2/2 | N^2/2 | N^2/2 | N exchanges
insertion | Y | Y | N^2/2 | N^2/4 | N | use for small N or partially ordered
shell | Y | N | ? | ? | N | tight code, subquadratic
merge | N | Y | N lg N | N lg N | N lg N | N log N guarantee, stable
quick | Y | N | N^2/2 | 2 N ln N | N lg N | N log N probabalistic guarantee, fastest in practice
3-way quick | Y | N | N^2/2 | 2 N ln N | N | imporves quicksort in presence of duplicate keys
heap | Y | N | 2 N lg N | 2 N lg N | N lg N | N log N guarantee, in-place
??? | Y | Y | N lg N | N lg N | N lg N | holy sorting grail