# Binary Search Trees

## Data Structures and Abstractions with Java: A Binary Search Tree Implementation

```java
// BST Implementation

import java.util.Iterator;
public class BinarySearchTree <T extends Comparable<? super T>>
    extends BinaryTree<T>
    implements SearchTreeInterface<T>
{
    public BinarySearchTree() { super(); }

    public BinarySearchTree(T rootEntry)
    {
        super();
        setRootNode(new BinaryNode<T>(rootEntry));
    }

    // ensure that tree is BST
    public void setTree(T rootData) { throw new UnsupportedOperationException(); }

    // ensure that tree is BST
    public void setTree(T rootData, BinaryTreeInterface<T> leftTree, BinaryTreeInterface<T> rightTree) { throw new UnsupportedOperationException(); }

    public T getEntry(T entry) { return findEntry(getRootNode(), entry); }

    private T findEntry(BinaryNodeInterface<T> rootNode, T entry)
    {
        T result = null;
        if(rootNode != null)
        {
            T rootEntry = rootNode.getData();
            if(entry.equals(rootEntry)) { result = rootEntry; }
            else if (entry.compareTo(rootEntry) < 0) { result = findEntry(rootNode.getLeftChild(), entry); }
            else { result = findEntry(rootNode.getRightChild(), entry); }
        }
        return result;
        }
    }

    public boolean contains(T entry) { return getEntry(entry) != null; }

    // adds newEntry to the nonempty subtree rooted at rootNode
    private T addEntry(BinaryNodeInterface<T> rootNode, T newEntry)
    {
        assert rootNode != null; 
        T result = null;
        int comparison = newEntry.compareTo(rootNode.getData());

        if(comparison == 0) 
        {
            result = rootNode.getData();
            rootNode.setData(newEntry);
        }
        else if (comparison < 0)
        {
            if(rootNode.hasLeftChild()) { result = addEntry(rootNode.getLeftChild(), newEntry); }
            else { rootNode.setLeftChild(new BinaryNode<T>(newEntry)); }
        }
        else 
        {
            assert comparison > 0;
            if(rootNode.hasRightChild()) { result = addEntry(rootNode.getRightChild(), newEntry); }
            else { rootNode.setRightChild(new BinaryNode<T>(newEntry); }
        }
        return result;
    }

    public T add(T newEntry)
    {
        T result == null;
        if(isEmpty()) { setRootNode(new BinaryNode<T>(newEntry)); }
        else { result = addEntry(getRootNode(), newEntry); }
        return result;
    }

    public T remove(T entry) 
    {
        ReturnObject oldEntry = new ReturnObject(null);
        BinaryNodeInterface<T> newRoot = removeEntry(getRootNode(), entry, oldEntry);
        setRootNode(newRoot);
        return oldEntry.get();
    }

    private BinaryNodeInterface<T> removeEntry(BinaryNodeInterface<T> rootNode, T entry, ReturnObject oldEntry)
    {
        if(rootNode != null)
        {
            T rootData = rootNode.getData();
            int comparison = entry.compareTo(rootData);
            if(comparison == 0) 
            {
                oldEntry.set(rootData);
                rootNode.removeFromRoot(rootNode);
            }
            else if (comparison < 0)
            {
                BinaryNodeInterface<T> leftChild = rootNode.getLeftChild();
                BinaryNodeInterface subtreeRoot = removeEntry(leftChild, entry, oldEntry);
                rootNode.setLeftChild(subtreeRoot);
            }
            else
            {
                BinaryNodeInterface<T> rightChild = rootNode.getRightChild();
                rootNode.setRightChild(removeEntry(rightChild, entry, oldEntry));
            }
        }
        return rootNode;
    }
    
    private BinaryNodeInterface<T> removeFromRoot(BinaryNodeInterface<T> rootNode)
    {
        if(rootNode.hasLeftChild() && rootNode.hasRightChild())
        {
            BinaryNodeInterface<T> leftSubtreeRoot = rootNode.getLeftChild();
            BinaryNodeInterface<T> largestNode = findLargest(leftSubtreeRoot);

            rootNode.setData(largestNode.getData());

            rootNode.setLeftChild(removeLargest(leftSubtreeRoot));
        }
        else if(rootNode.hasRightChild()) { rootNode = rootNode.getRightChild(); }
        else { rootNode = rootNode.getLeftChild(); }

        return rootNode;
    }

    private BinaryNodeInterface<T> findLargest(BinaryNodeInterface<T> rootNode)
    {
        if(rootNode.hasRightChild()) { rootNode = findLargest(rootNode.getRightChild()); }
        return rootNode;
    }

    private BinaryNodeInterface<T> removeLargest(BinaryNodeInterface<T> rootNode)
    {
        if(rootNode.hasRightChild()
        {
            BinaryNodeInterface<T> rightChild = root.getRightChild();
            BinaryNodeInterface<T> root = removeLargest(rightChild);
            rootNode.setRightChild(root);
        }
        else { rootNode = rootNode.getLeftChild(); }
        return rootNode;
        /*
        Notice that this recursive method does not explicitly keep track of the parent of the current right child.
        Rather, a reference to this parent is retained in the implicit stack of the recursion.
        */
    }
}
```

*Every addition to a binary search tree adds a new leaf to the tree*

### Add (Recursive)

1. If BST is empty
    1. Create a new Node containing *entry* 
    1. Make it the root of the BST
1. Else
    1. Add the entry at the root
        1. If the new entry matches the entry in the root
            1. Replace the root data with entry
        1. If the new entry is less than the root data
            1. If the root has a left subtree
                1. Add the entry to the left subtree
            1. Else
                1. Give the root a left child containing the new entry
        1. Else (entry greater than root data)
            1. If the root has a right child
                1. Add the entry to the right subtree
            1. Else
                1. Give the root a right child containing the new entry

### Add (Iterative)

- result = null
- current = root
- found = false

- while(! found)
    - if entry = root.data
        - found = true
        - root.data = entry
    - else if (entry < root.data)
        - if current.left != null
            - current = current.left
        - else
            - found = trye
            - current.left = new Node(entry)
    - else 
        - assert entry > root.data
        - if current.right != null
            - current = current.right
        - else 
            - found = true
            - current.right = new Node(entry)
- return result

### Removing an Entry

1. node 0 children (leaf) -> simple
1. node has 1 child -> fairly simple
1. node has 2 children -> complex
    - find inorder successor (leftmost value in right subtree)
    - replace entry with this successor
    - delete node data was taken from
    (opposite way works too)

**To remove an entry whose node has two children, you first replace the entry with another whose node has no more than one child. You then remove the second node from the binary search tree.**

Removing from root -> pretty simple

`remove(root, entry)`
- oldEntry = null
- if tree not empty
    - if entry = root.data
        - oldEntry = root.data
        - removeFromRoot(root)
    - else if entry < root.data
        - oldEntry = remove(root.left, entry)
    - else
        - oldEntry = remove(root.right, entry)
- return oldEntry

`removeFromRoot(root)`
- if node has 2 children
    - largest = inorder predecessor
    - root.data = largest.data
    - remove largest
- if root.right != null
    - root = root.right
- else
    - root = root.left (can be null -> 0 children)
- return root

### Remove (Iterative)

`remove(entry)`
- result = null
- current -> contains match for entry
- parent -> current's parent
- if current != null
    - result = current.data
    - if current has 2 children
        - toRemove = inorder predecessor
        - parent = toRemove's parent
        - current.data = toRemove.data
        - current = toRemove
    - delete current from tree
- return result