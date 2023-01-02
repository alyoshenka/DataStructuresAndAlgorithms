# Strings

## 5.1 String Sorts

## 5.2 Tries

```java
public class StringST<Value>
{
    // create a symbol table
    public StringST() 
    {

    }

    // put key-value pair into the table
    // (remove key if value is null)
    public void put(String key, Value val)
    {

    }

    // value paired with key
    // (null if key is absent)
    public Value get(String key)
    {

    }

    // remove key and its value
    public void delete(String key)
    {

    }

    // is there a value paired with key?
    public boolean contains(String key) { }

    // is the table empty?
    public boolean isEmpty() { }

    // the longest key that is a prefix of s
    public String longestPrefixOf(String s) 
    {

    }

    // all keys having s as a prefix
    public Iterable<String> keysWithPrefix(String s)
    {

    }

    // all keys that match s
    // (where . matches any character)
    public Iterable<String> keysThatMatch(String s)
    {

    }

    // number of key-value pairs
    public int size() { }

    // all the keys in the table
    public Iterable<String> keys() { }
}
```

each node -> *R* links (R = alphabet size)

*nodes with null values exist to facilitate search in the trie and do not correspond to keys*

Search Conditions:
- value at node corresponding to last character in key is `not null` = **search hit**
    - the value associated with the key is the value in the node corresponding to its last character
- value in node corresponding to last character in key is `null` = **search miss**
- search terminated in `null` link = **search miss**

Insertion:
1. start by doing a search
    - use characters of key to guide down trie until reaching the last character of the key, or a null link
        - encountered null link before reaching last character of key = no trie node correspondind to last character(s)
            - create nodes for each of the characters in the key not yet encountered and set the value in the last on to the value to be associated with the key
        - encountered last character of the key before reaching a null link 
            - set that node's value to the value to be associated with the key

characters and keys are *implicitly* stored in the data structure

*A trie for an* R-charater *alphabet is referred to as an* R-way trie.

```java
public class TrieST<Value>
{
    private static int R = 256; // radix
    private Node root; // root of trie
    

    private static class Node
    {
        private Object val;
        private Node[] next = new Node[R];
    }

    public Value get(String key)
    {
        Node x = get(root, key, 0);
        if(x == null) return null;
        return (Value) x.val;
    }

    private Node get(Node x, String key, int d)
    {
        // return value associated with key in the subtrie rooted at x
        if(x == null) return null;
        if(d == key.length()) return x;
        char c = key.charAt(d); // use the dth char to identify subtree
        return get(x.next[c], key, d+1);
    }

    public void put(String key, Value val)
    { root = put(root, key, val, 0); }

    private Node put(Node x, String key, Value val, int d)
    {
        // change value associated with key if in subtrie rooted at x
        if(x == null) x = new Node();
        if(d == key.length()) { x.val = val; return x; }
        char c = key.charAt(d); // use the dth char to identify subtrie
        x.next[c] = put(x.next[c], key, val, d+1);
        return x;
    }
}