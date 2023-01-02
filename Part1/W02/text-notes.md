# Week 2 - Stack and Queues; Elementary Sorts

## Ch 1.3 - Bags, Queues, and Stacks

*The way in which we represend the objects in the collection directly impacts the efficiency of the various operations*

`for (Type varName : collectionName) { }`

`push()` and `pop()` times are independent of stack size

loitering: holding a reference to an item that is no longer needed

`import java.util.Iterator;`

pg 139

## Ch 2.1 - Elementary Sorts

sorting cost model -> count compares and exchanges

no exchanges -> count array accesses

sort *in place* -> no extra memory/ helper array

`a.compareTo(b)`
- a < b -> -1
- a = b -> 0
- a > b -> 1

### Selection Sort

1. find smallest value in array
1. exchange it with first entry
1. find second smallest value in array
1. exchange it with second entry
1. rinse and repeat until array is sorted

**~N<sup>2</sup>/2 compares, N exchanges**

- running time is insensitive to input
- data movement is minimal

```java
public class Selection
{
    public static void sort(Comparable[] a)
    {
        int N = a.lrngth;
        for(int i = 0; i < N; i++)
        {
            int min = i;
            for(int j = i+1; j < N; j++)
            {
                if(less(a[j], a[min])) { min = j; }
                exch(a, i, min);
            }
        }
    }
}
```

### Insertion Sort

**\~N<sup>2</sup>/4 compares, \~N<sup>2</sup>/4 exchanges, on average**

Worst case: \~N<sup>2</sup>/2 compares, \~N<sup>2</sup>/2 exchanges

Best case: N - 1 compares, 0 exchanges

Works well for nonrandom arrays

```java
public class Insertion
{
    public static void sort(Comparable[] a)
    {
        int N = a.length;
        for(int i = 0; i < N; i++)
        {
            for(int j = i; j > 0 && less(a[j], a[j-1]); j--)
            {
                exch(a, j, j-1);
            }
        }
    }
}
```

partially sorted arrays: the number of inversions is less than a constant multiple of the size
- each entry is not far from its final position
- small array appended to large sorted array
- array with only a few entries out of place

**Insertion sort is good for partially sorted arrays, selection sort is not**

number of exchanges = number of inversions

number of inversions < number of compares < number of inversions + array size - 1

good for: 
- partially sorted arrays
- small arrays

**The running times of insertion sort and selection sort are quadratic and within a small constant factor of one another for randomly ordered arrays of distinct values.**

### Shellsort

extension of insertion sort that gains speed by allowing exchanges of array entries that are far apart

```java
public class Shell
{
    public static void sort(Comparable[] a)
    {
        int N = a.length;
        int h = 1;
        while(h < N/3) { h = 3 * h + 1; }
        while(h >= 1) 
        {
            for (int i = 0; i < N; i++)
            {
                for (int j = i; j >= h && less(a[j], a[j-h]); j -= h)
                {
                    exch(a, j, j-h);
                }
                h = h / 3
            }
        }
    }
}
```