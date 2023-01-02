# Week 4

## Ch 2.4: Priority Queues

`remove-max`, `insert`

maximum: *any* item with the largest key value (duplicates allowed)

### Cost of finding the largest *M* in  a stream of *N* items
client | order of growth: time | order of growth: space
--- | --- | ---
sort client | N log N | N
PQ client (elementary implementation) | N M | M
PQ client (heap-based implementation) | N log M | M

### Order of growth of worst-case runnitn time for priority-queue implementations
data structure | insert | remove maximum
--- | --- | ---
ordered array | N | 1
unordered array | 1 | N
heap | log N | log N
*impossible* | 1 | 1

lazy approach = unordered sequences -> defer doing work (finding maximum) until necessary

eager approach = ordered sequences -> do as much work as possible (keep list sorted on insertion) to make later operations efficient

> In a heap, the parent of the node in position *k* is in position  ⎣*k*/2⎦ and, conversely, the two children of the node in position *k* are in positions 2*k* and 2*k*+1
> To move *up* the tree from `a[k]` set *k* to *k*/2
> To move *down* the tree set *k* to 2* *k* or 2* *k* +1

**The height of a complete binary tree of size *N* is  ⎣lg N⎦**

```java
public class MaxPQ<Key extends Comparable<Key>> 
{
  private Key[] pq; // heap-ordered complete binary tree in pq[1..N] with pq[0] unused
  private int N = 0; 

  public MaxPQ(int maxN) 
  { pq = (Key[]) new Comparable[maxN+1]; }

  public boolean isEmpty() 
  { return N == 0; }

  public int size() 
  { return N; }

  public void insert(Key v)
  { 
    pq[++N] = v;
    swim(N);
  }

  public Key delMax()
  {
    Key max = pq[1]; // retrieve max key from top
    exch(1, N--); // exchange with last item
    pq[N+1] = null; // avoid loitering
    sink(1); // restore heap property
    return max;
  }

  private boolean less(int i, int j) 
  { return pq[i].compareTo(pq[j]) < 0; }

  private void exch(int i, int j) 
  { Key t = pq[i]; pq[i] = pq[j]; pq[j] = t; }

  private void swim(int k) 
  {
    while(k > 1) && less(k/2, k))
    {
      exch(k/2, k);
      k = k/2;
    }
  }

  private void sink(int k)
  {
    while(2*k <= N)
    {
      int j = 2*k;
      if(j < N && less(j, j+1)) j++;
      if(!less(k, j)) break;
      exch(k, j);
      k = j;
    }
  }
}
```

*no more than* 1 + lg N *compares for* insert

*no more than* 2 lg N *compares for* remove the maximum

### Heapsort

*sink-based heap construction uses fewer than* 2N *compares and fewer than* N *exchanges to construct a heap from* N *items*

```java
public static void sort(Comparable[] a)
{
  int N = a.length;
  for(int k = N/2; k >= 1; k--)
    sink(a, k, N);
  while(N > 1)
  {
    exch(a, 1, N--);
    sink(a, 1, N);
  }
}
```

*Heapsort uses fewer than* 2N lg N + 2N *compares (and half that many exchanges) to sort* N *items*

The good: fast 

The bad: lots of cache misses because array entry comparisons are far away

## Ch 3.1

## Ch 3.2