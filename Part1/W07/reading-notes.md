# Week 7 - Hashing

## Ch 21: Introducing Hashing

hashing: techique that determines an object's array inces using only an entry's search key, without searching

search key **maps**/**hashes** to hash index

perfect hash function: maps each search key into a different integer that is suitable as an index to the hash table

hash function:
1. convert the search key into a *hash code*
1. *compress* the hash code into the range od indicies for the hash table

good hash function:
- minimize collisions
- fast to compute

to reduce chance of collision -> choose a hash function that distributes entries uniformly throughout the hash table

Guidelines for `hashCode()`
- if a class overrides `equals`, it should override `hashCode`
- if `equals` considers two objects equal, their `hashCode` should be the same
- `hashCode` should return the same value for an object throughout its lifecycle
- but `hashCode` can return different values in different executions

```java
// Horner's Method
int hash = 0;
int n = s.length();
for(int i = 0; i < n; i++) { hash = g * hash + s.charAt(i); }

// where g is some positive constant
```

*The size of a hash table should be a prime number *n* greater than 2. Then, if you compress a positive hash code *c* into an index for the table by using *c % n*, the indices will be distributed uniformly between 0 and *n* - 1.*

```java
private int getHashKey(K key)
{
    int hashIndex = key.hashCode() % hastTable.length;
    if(hashIndex < 0) { hashIndex += hashTable.length; }
    return hashIndex;
}
```

### Resolving Collisions

open addressing: finding an unused, or open, location in the hash table

Locations in a Hash Table
- occupied
- empty: was and always has been `null`
- available: entry was removed by dictionary

linear probing -> causes primary clustering

Quadratic Probing
- i = h + j^2
- reches half the locations in the table if the size is a prime number
- avoids primary clustering, but leads to secondary clustering (less of a problem)

Double Hashing
- resolves a collision dirung hashing by incrementing using a second hash function
    - must differ from first hash function
    - depend on search key
    - have a nonzero value
- reaches every location in the table, if size is primary number
- avoids both primary and secondary clustering

#### Separate Chaining

separate chaining: resolving collisions by using buckets that are linked chains

```java
// Algorithm: add

add(key, value)
{
    index = getHashIndex(key)
    if(hashTable[index] == null)
    { 
        hashTable[index] = new Node(key, value)
        numberOfEntries++
        return null
    }
    else
    {
        // search the chain that begins at hashTable[index] for a node that contains key
        if(key is found) 
        {
            // assume currentNode references the node that contains key
            oldValue = currentNode.getValue()
            currentNode.setValue(value)
            return oldValue
        }
        else // add new node to end of chain
        {
            // assume nodeBefore references the last node
            newNode = new Node(key, val)
            nodeBefore.setNextNode(newNode)
            numberOfEntries++
            return null
        }
    }
}

// Algorithm: remove

remove(key)
{
    index = getHashIndex(key)
    // search the chain that begins at hashTable[index] for a node that contains key
    if(key is found) 
    {
        // remove the node that contains key from the chain
        numberOfEntries--
        return // value in removed ndoe
    }
    else { return null }
}



// Algorithm: getValue

getValue(key)
{
    index = getHashKey(key)
    // search the chain that begins at hashTable[index] for a node that contains key
    if(key is found) { return /* value in found node */ }
    else { return null }
}
```

## Ch 22: Hashing as a Dictionary Implementation

load factor λ = number of entries / number of locations

-> measure of the cost of collision resolution
- open addressing: 1
- separate chaining: no maximum
- restricting the size of λ improves the performance of hashing

Maintining the performance of hashing
- open addressing: λ < 0.5
- separate chainining: λ < 1.0

rehashing: enlarging a hash table and computing new hash indicies for its contents
- rehash when λ becomes too large
- double the size, then increase it to the next prime number
- `add` all entries to the 'new' table

### A Dictionary Implementation that Uses Hashing

```java
import java.util.Iterator;
import java.util.NoSuchElementException;

public class HashedDictionary<K, V> implements DictionaryInterface<K, V>
{
    private TableEntry<K, V>[] hashTable;
    private int numberOfEntries;
    private int locationsUsed; // number of table locations not null
    private static final int DEFAULT_SIZE: 101; // must be prime
    private static final double MAX_LOAD_FACTOR = 0.5; // fraction of the hash table that can be filled

    public HashedDicionary() { this(DEFAULT_SIZE); }

    public HashedDictionary(int tableSize)
    {
        int primeSize = getNextPrime(tableSize);

        hashTable = new TableEntry[primeSize];
        numberOfEntries = 0;
        locationsUsed = 0; 
    }

    public V getValue(K key) 
    {
        V result = null;
        int index = getHashIndex(key);
        index = locate(index, key);

        // locate:
        // follows the probe sequence that begins at the key's hash index and returns either the index of the entry containing the key, or -1

        if(index != -1) { result = hashTable[index].getValue(); }
        return result;
    }

    public V remove(K key)
    {
        V removedValue = null;

        int index = getHashIndex(key);
        index = locate(index, key);

        if(index != -1)
        {
            removedValue = hashTable[index].getValue();
            hashTable[index].setToRemoved(); // flag entry as removed
            numberOfEntries--;
        }
        return removedValue;
    }

    private int locate(int index, K key)
    {
        boolean found = false;
        
        while(!found && (hashTable[index] != null))
        {
            if(hashTable[index].isIn() && key.equals(hashTable[index].getKey())) { found = true; }
            else { index = (index + 1) % hashTable.length; } // linear probing -> change here to switch to another open addressing scheme
        }

        int result = -1;
        if(found) { result = index; }
        return result;
    }

    private boolean isHashTableTooFull() {
        /*
        returns true if the hash table's load factor is greater than or equal to MAX_LOAD_FACTOR.
        Here we define load factor as the ratio of locationsUsed to hashTable.length
        */
    }

    private void rehash()
    {
        /*
        Expands the hash table to a size that is both prime and at least double its current size,
        then adds the current entries in the dictionary to the new hash table
        */
    }

    private int probe(index, key)
    {
        boolean found = false;
        int removedStateIndex = -1; // index of first location in removed state

        while(!found && (hashTable[index] != null))
        {
            if(hashTable[index].IsIn())
            {
                if(key.equals(hashTable[index].getKey()))
                {
                    found = true; // key found
                }
                else
                {
                    index = (index + 1) % hashTable.length; // linear probing
                }
            }
            else // skip entries that were removed
            {
                // save index of first location in removed state
                if(removedStateIndex == -1) { removedStateIndex = index; }

                index = (index + 1) % hashTable.length; // linear probing
            }
        }
        if(found || (removedStateIndex == -1)) { return index; } // index of either key, or null
        else { return removedStateIndex; } // index of available location
    }

    public V add(K key, V value)
    {
        V oldValue; // value to return

        if(isHashTableTooFull()) { rehash(); }

        int index = getHashIndex(key);
        index = probe(index, key); // check for and resolve collision

        // assert index is within legal range
        assert (index >= 0) && (index < hashTable.length);

        if((hashTable[index] == null) || hashTable[index].isRemoved())
        {
            // key not found, so insert new entry
            hashTable[index] = new TableEntry<K, V>(key, value);
            numberOfEntries++;
            numberOfLocations++;
            oldValue = null;
        }
        else 
        {
            // key found; get old value for return and then replace it
            oldValue = hashTable[index].getValue();
            hashTable[index].setValue(value);
        }
        return oldValue;s
    }

    private void rehash()
    {
        TableEntry<K, V>[] oldTable = hashTable;
        int oldSize = hashTable.length;
        int newSize = getNextPrime(oldSize * 2);
        hashTable = new TableEntry[newSize];

        numberOfEntries = 0;
        locationsUsed = 0;

        // rehash dictionary entries from old array
        // skip null locations and removed entries
        for(int index = 0; index < oldSize; index++)
        {
            if((oldTable[index] != null) && oldTable[index].isIn()) 
            {
                add(oldTable[index].getKey(), oldTable[index].getValue());
            }
        }
    }

    private class TableEntry<S, T>
    {
        private S key;
        private T value;
        private boolean inTable; // true if entry is in hash table

        private TableEntry(S searchKey, T dataValue)
        {
            key = searchKey;
            value = dataValue;
            inTable = true;
            // a location in the availabel state contains an entry in the removed state
        }
    }

    private class KeyIterator implements Iterator<K>
    {
        private int currentIndex;
        private int numberLeft;

        private KeyIterator()
        {
            currentIndex = 0;
            numberLeft = numberOfEntries;
        }

        public boolean hasNext() { return numberLeft > 0; }

        public K next()
        {
            K result = null;
            if(hasNext())
            {
                // find index of next entry
                while((hashTable[currentIndex] == null) || hashTable[currentIndex].isRemoved()) { currentIndex++; }

                result = hashTable[currentIndex].getKey();
                numberLeft--;
                currentIndex++;
            }
            else { throw new NoSuchElementException(); }
            return result;
        }

        public void remove() { throw new UnsupportedOperationException(); }
    }
}
```

**If you no not use a perfect hash function, you must expect collisions**

> Note: To implement getNextPrime(anInteger), first see whether anInteger is even. If it
is, it cannot be prime, so add 1 to make it odd. Then use a private method isPrime to find the
first prime number among the parameter anInteger and subsequent odd integers.
To implement isPrime, note that 2 and 3 are prime but 1 and even integers are not. An odd
integer 5 or greater is prime if it is not divisible by every odd integer up to its square root.