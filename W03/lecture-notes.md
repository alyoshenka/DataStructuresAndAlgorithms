# Week 3 - Sorting Algorithms

cost model: number of compares and exchanges (or array accesses)

stability: objects with equal values will be in the same order as in the original input

divide-and-conquer:
1. divide problem into subproblems
1. recursively solve
1. appropriately combine answers

Elementary Algorithms
- selection sort
    - steps
        1. select the smallest item in the array 
        1. exchange it with the first entry
        1. select next smallest element
        1. exchange it with second entry
        1. continue until array is sorted
    - average case: N<sup>s</sup>/2 compares; N exchanges
    - running time insensitive to input
    - always quadratic time
- insertion sort
    - steps
        1. consider items one at a time, swapping current item with larger items to the left
    - average case (randomly ordered arrays): N<sup>2</sup>/4 compares; N<sup>2</sup>/4 exchanges
    - worst case (array in opposite order): N<sup>2</sup>/2 compares; N<sup>2</sup>/2 exchanges
    - best case (array already sorted): N-1 compares; 0 exchanges
    - works well for certain non-random arrays
- shell sort
    - extension of insertion sort
        - exchanges entries that are far apart (h-sorting)
    - produces partially worted arrays
    - h-sorting repeated with smaller values of h until array is fully sorted
    - worst case: O(N<sup>3/2</sup>)

Advanced Algorithms
- mergesort
    - steps:
        1. divide array in half
        1. sort each half (recursively)
        1. merge results
    - worst case (time): N lg N
    - extra space proportional to N
- quicksort
    
