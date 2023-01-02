# W6

## Hash Tables

### Hash Tables

Can we do better than logarithmic performance?

- Yes, but can't have ordered operations

hash function: method for computing array index from key

Issues:

- computing hash function
- equality test: method for checking whether two keys are equal
- collision resolution: algorithm and data structure to handle two keys that hash to the same array index

Space/time tradeoff:

- no space limitation -> trivial hash function with key as index
- no time limitation -> trivial collision resolution with sequential search
- space and time limitations (real world) -> hashing

Idealistic goal:

- efficiently computable
- each table index equally likely for each key

if `x.equals(y)`, then `x.hashCode() == y.hashCode()`

default implementation: memory address of `x`

Standard recipe for user-defined types:

- combine each significant field using `31x + y` rule

#### Bins and balls: throw balls uniformly at random into `M` bins

**Birthday problem**: expect 2 balls in the same bin after `∼ √(π M / 2)` tosses

**Coupon collector**: expect every bin has `≥ 1` balls after `∼ M ln N` tosses

**Load balancing**: After `M` tosses, expect most loaded bin has `Θ(log M / log log M)` balls

### Seperate Chaining

collision: two distinct keys hash to the same index

challenge: deal with collisions efficiently

seperate chaining: use an array of `M < N` linked lists

- hash: map key to integer _i_ between _0_ and _M - 1_
- insert: put at front of _i<sup>th</sup>_ chain (if not already there)
- search: need to search only _i<sup>th</sup>_ chain

Consequence - number of probes for search/insert is proportional to _N / M_ (_M_ \* faster than sequential search)

- _M_ too large: too many empty chains
- _M_ too small: chains to long
- typical choice: _M ∼ N / 5_ -> constant-time operations

### Linear Probing

open addressing: when a new key collides, find next empty slot and put it there

Goal: keep array _1/2_ full

### Context

**Use all of the data to compute the hashcode**

one-way hash function: "hard" to find a key that will hash to a desired value (or two keys that hash to the same value)

#### Separate chaining

- easier to implement delete
- performance degrades gracefully
- clustering less sensitive to poorly-designed hash function

#### Linear probing

- less wasted space
- better cache performance

#### Hash tables

- simpler to code
- no effective alternative for unordered keys
- faster for simple keys
- better system support in Java for strings

#### Balanced search trees

- stronger performance guarantee
- support for ordered symbol table applications
- easier to implement `compareTo` correctly than `equals` and `hashCode`

## Symbol Table Applications

### Sets

set: collection of distinct keys

### Dictionary Clients

### Indexing Clients

### Sparse Vectors

_in many practical applications, matrices are sparce (most of the entries are 0)_

key = index in vector

value = entry in vector

- space proportional to number of nonzeros
