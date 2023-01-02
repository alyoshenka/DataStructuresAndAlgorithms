# Week 4 1/25 - Trees & Heaps

30 min in; check recording!!

Ch 8.34, 24, 25, 27 (Carrano & Henry)

## Symbol Table

associates value with key

allow clients to search for the value of a given key

like an array, where keys are indices and values are array entries

common applicaitons: dictionary, web search, book index

Constraints

- no duplicate keys
- no null values
- key equality: keys must be comparable

## Trees

nodes connected by edges

edges indicate relationships between nodes

nodes arranged in levels that indicate heirarchy

root = top level node

leaf node -> no children

n children = n-ary tree

tree height: number of levels (empty = 0)

length -> number of edges in a path

## Binary Trees

each node has at most 2 children, left and right

Properties

- maximum number of nodes at level 'i' is 2<sup>i</sup>
- maximum number of nodes in a binary tree of height 'h' is 2<sup>h</sup> - 1
- binary tree with N nodes, minimum height is log2 (N+1)

### Types of Binary Tree

- full: every node has 0 or 2 children
- complete: all levels (except possibly the last) are completely filled, and last level has all keys to the left
- perfect all internal nodes have 2 children and all leaf nodes are at the same level; 2<sup>h+1</sup> - 1 nodes
- balanced: height = O(log N)

## Binary Tree Traversal

pre-order: visit all nodes in tree order (starting from root)

1. visit root
1. traverse left sub-tree
1. traverse right sub-tree

in-order: visit all nodes in ascending order, based on key values

1. traverse left sub-tree
1. visit root
1. traverse right sub-tree

post-order: useful for deleting a tree or getting 'postfix' expression

1. traverse left sub-tree
1. traverse right sub-tree
1. visit root

tree traversal = recursive operation

*hint for homework: recursive problem (week 4)*

## Binary Heap

used to address priority

complete binary tree structure that can efficiently support binary heap operations

either min or max heap

min heap: root node must have minimum value of all nodes (heap-ordered: all nodes in the tree are less then or equal to their children)

max heap: root note must have maximum value of all nodes

## Heap Operations

make a change (that could violate heap condition) then "reheapifying"

sink -> performed when node becomes larger than its parent. Node is exchanged with larger child until heap is order restored

swim -> node becomes smaller than one or more of its children. Node is exchanged with larger child until heap order is restored

Heaps stored in arrays can be traversed using array indices