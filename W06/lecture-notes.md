# Week 6 - 2/8/2022 - Balanced Search Trees

BSTs -> recursive!

check out the reading!

balanced search tree algorithms -> maintain balance while adding/removing nodes

## AVL Trees

re-arranges nodes during add/remove to maintain balance

balanced if height of subtrees differs by no more than one level

subtree nodes are rotated around the first unbalanced node between the inserted node and the root

**right rotation** -> restores balance in the left subtree

**left rotation** -> restores balance in the right subtree

### Double Rotations

sometimes a single rotation doesn't restore balance

a second rotation in the opposite is required

Imbalance at node N can be corrected by double rotation if
- the addition occurred in the left subtree of N's right child
- the addition occurred in the right subtree of N's left child

## 2-3 Trees

general search tree whose interior nodes must have either 2 or 3 children

2-node: contains one data item and has 2 children. The data is larger than any data in the node's left subtree and smaller than any data in the right subtree.

3-node: contains 2 data items. Data less than the smaller item will be in the left subtree. Data greater than the larger data item will be in the right subtree. Data between those values will be in the node's middle tree.

### Operations

**completely balanced** with all leaves on the same level; maintaining balance is easier than with AVL tree

searching 2-nodes -> like searching BST

searching 3-nodes involves checking the node's middle subtree if the searched value is between the node's data values

If an entry is added to a 3-node, the node is split to maintain balance. The middl evalue propogates up to its parent, splitting the parent node if that already has 2 data entries until reaching the root.

## Red-Black Trees

binary tree that's logically equivalent to a 2-4 tree

Only uses 2-nodes, searching a read-black tree can use the same code as for BST

Use color/bit flag to highlight new nodes that cause tree hight to increase

Additions to r-b tree maintain tree balance with color flips and rotations like those used for AVL trees

-> used in database systems

### Properties

- root node = black
- red node has black parent, and black children
- any new node added to a non-empty r-b tree must be red
- every path from root to leaf contains same number of black nodes
- height < 2 lg N
- average length of pathe from root to node ~ lg 

AVL trees are more balanced than r-b trees, but may cause more rotations during insertion and deletion. Red-black trees are preferrable for applications with frequent insertions and deletions.

## m-Way Trees

multiway search tree whose nodes have up to *m* children
- k-node has k-1 data items and k children
- not all are balanced
- B-tree of order m is a balanced multiway search tree that maintains balance with properties:
    - root has either no childred, or between 2 and m children
    - interior nodes have between m/2 and m children
    - all leaves are on the same level
