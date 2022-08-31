# Week 5 - Binary Search Trees

symbol table -> associates a value with a key, allowing clients to search for the value of a given key

look up value by key, as opposed to position (array)

implemented with single array, or two parallel arrays, or a linked list

## Trees

data is accessed in heirarchical fashion

## BSTs

- each node contains a comparable object
- data is greater than left subtree
- data is less than right subtree (unique)
- data is less than or equal to data in right subtree (duplicate)
- smallest value -> leftmost node
- largest value -> rightmost node
- balanced and unbalanced
- adding items to an empty tree results in a tree that is approximately balanced
- operations
    - add (unique values -> overwrite existing)
    - retrieve -> returns requested entry(s)
        - needs to define how to deal with duplicate entries
    - remove -> removes and returns requested entry(s); code needs to account for children
        - needs to define how to deal with duplicate entries
    - search -> returns whether value is in the tree
        - begin at root
        - max number of comparisons proportional to height
- time complexity for search, add, delete: O(h) for unbalanced tree and O(log n) for height-balanced tree

