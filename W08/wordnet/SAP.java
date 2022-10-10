/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.SET;

public class SAP {

    private int size;
    private final Digraph graph;
    private final Digraph reverse;
    // private Iterable<Integer> topological;

    private BreadthFirstPaths[] bfsPaths;
    private DepthFirstPaths[] dfsPaths;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException();
        }

        size = G.V();
        graph = new Digraph(G);
        reverse = G.reverse();
        bfsPaths = new BreadthFirstPaths[size];
        dfsPaths = new DepthFirstPaths[size];
        // topological = new ArrayList<>();
    }

    private boolean hasCycle() {
        return new DirectedCycle(graph).hasCycle();
    }

    // find parent of s that has no parents
    private int getRootFrom(int s) {
        DepthFirstPaths pathsFromS = new DepthFirstPaths(graph, s);
        for (int i = 0; i < size; i++) {
            if (pathsFromS.hasPathTo(i) && graph.outdegree(i) == 0) {
                return i;
            }
        }
        return -1;
    }


    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || w < 0 || v > size - 1 || w > size - 1) {
            throw new IllegalArgumentException();
        }
        if (v == w) {
            return 0;
        }
        int ancestor = ancestor(v, w);
        if (ancestor < 0) {
            return -1;
        }

        // compute length v -> a
        //  shortest path (bfs)
        // compute length w -> a
        //  shortest path (bfs)
        // return sum of lengths

        // System.out.println("v: " + v + " w: " + w + " a: " + ancestor);


        BreadthFirstPaths vPath = bfsPaths[v];
        if (vPath == null) {
            vPath = new BreadthFirstPaths(graph, v);
            bfsPaths[v] = vPath;
        }
        BreadthFirstPaths wPath = bfsPaths[w];
        if (wPath == null) {
            wPath = new BreadthFirstPaths(graph, w);
            bfsPaths[w] = wPath;
        }
        if (vPath.hasPathTo(ancestor) && wPath.hasPathTo(ancestor)) {
            int length = 0;
            for (int i : vPath.pathTo(ancestor)) {
                length++;
            }
            for (int i : wPath.pathTo(ancestor)) {
                length++;
            }
            return length - 2; // overflow
        }
        else {
            return -1;
        }
    }

    private int lca(int v, int w) {
        if (v < 0 || w < 0 || v >= size || w >= size) {
            throw new IllegalArgumentException();
        }
        if (v == w) {
            return v;
        }

        DepthFirstPaths vPath = dfsPaths[v];
        if (vPath == null) {
            vPath = new DepthFirstPaths(graph, v);
            dfsPaths[v] = vPath;
        }
        DepthFirstPaths wPath = dfsPaths[w];
        if (wPath == null) {
            wPath = new DepthFirstPaths(graph, w);
            dfsPaths[w] = wPath;
        }

        /*
        Define the height of a vertex v in a DAG to be the length of the longest path from a root to v.
        Among vertices that are ancestors of both v and w, the one with the greatest height is an
        LCA of v and w.

        1. determine a root
            go up from v as far as possible?
            root = no parents
            a. get reverse of graph
            b. dfs "upwards" until we find a vertex with no "children"(parents)
        2. use BFS to find distance from root to all points
        3. find all common ancestors of v and w
        4. return the CA with the greatest height

        what to do if there is a cycle?
        if there is a cycle:
            bfs from v -> w, save length
            bfs from w -> v, save length
            if v length < w length:
                return v
            else
                return w
         */


        // won't this be applicable in any instance where you can get from v->w/w->v?
        if (hasCycle()) {
            // can be optimized to store paths
            BreadthFirstPaths fromV = new BreadthFirstPaths(graph, v);
            BreadthFirstPaths fromw = new BreadthFirstPaths(graph, w);
            int vLength = 0;
            for (int i : fromV.pathTo(w)) {
                vLength++;
            }
            int wLength = 0;
            for (int i : fromw.pathTo(v)) {
                wLength++;
            }
            return vLength < wLength ? v : w;
        }

        // determine the root
        //  determine v's root
        //  determine w's root
        //  if different, no path -> return -1

        /*
        System.out.println(v + " root = " + getRootFrom(v));
        System.out.println(w + " root = " + getRootFrom(w));

         */

        int root = getRootFrom(v);
        if (root != getRootFrom(w)) {
            // System.out.println("cannot connect " + v + " and " + w + ": different roots");
            return -1;
        }

        System.out.println("root from " + v + " and " + w + ": " + root);

        // use bfs to determine length from root to all points
        // BreadthFirstPaths fromRoot = new BreadthFirstPaths(graph, root);

        // find all common ancestors of v and w
        SET<Integer> anc = new SET<>();
        BreadthFirstPaths vParent = new BreadthFirstPaths(graph, v);
        BreadthFirstPaths wParent = new BreadthFirstPaths(graph, w);
        for (int i = 0; i < size; i++) {
            if (vParent.hasPathTo(i) && wParent.hasPathTo(i)) {
                anc.add(i);
            }
        }


        System.out.println("common ancestors of " + v + " and " + w);
        for (int i : anc) {
            System.out.println(i);
        }


        BreadthFirstPaths height = new BreadthFirstPaths(reverse, root);

        int greatest = root;
        int dist = 0;
        for (int i : anc) {
            int thisDist = -1;
            // can be optimized
            if (height.pathTo(i) != null) {
                for (int j : height.pathTo(i)) {
                    thisDist++;
                }
            }

            System.out.println("dist from " + root + " to " + i + " = " + thisDist);

            if (thisDist > dist) {
                dist = thisDist;
                greatest = i;
            }
        }

        // System.out.println("greatest height: " + greatest + ": " + dist + " units");


        return greatest;
    }

    // can this be optimized with length?
    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || w < 0 || v >= size || w >= size) {
            throw new IllegalArgumentException();
        }
        if (v == w) {
            return v;
        }

        // System.out.println(v + ", " + w);

        /*
        DepthFirstPaths vPath = dfsPaths[v];
        if (vPath == null) {
            vPath = new DepthFirstPaths(graph, v);
            dfsPaths[v] = vPath;
        }
        DepthFirstPaths wPath = dfsPaths[w];
        if (wPath == null) {
            wPath = new DepthFirstPaths(graph, w);
            dfsPaths[w] = wPath;
        }

         */

        /*
        Define the height of a vertex v in a DAG to be the length of the longest path from a root to v.
        Among vertices that are ancestors of both v and w, the one with the greatest height is an
        LCA of v and w.

        1. determine a root
            go up from v as far as possible?
            root = no parents
            a. get reverse of graph
            b. dfs "upwards" until we find a vertex with no "children"(parents)
        2. use BFS to find distance from root to all points
        3. find all common ancestors of v and w
        4. return the CA with the greatest height

        what to do if there is a cycle?
        if there is a cycle:
            bfs from v -> w, save length
            bfs from w -> v, save length
            if v length < w length:
                return v
            else
                return w
         */


        // won't this be applicable in any instance where you can get from v->w/w->v?
        if (hasCycle()) {
            // can be optimized to store paths
            BreadthFirstPaths fromV = new BreadthFirstPaths(graph, v);
            BreadthFirstPaths fromw = new BreadthFirstPaths(graph, w);

            if (!fromV.hasPathTo(w) && !fromw.hasPathTo(v)) {
                return -1;
            } // ?

            int vLength = 0;
            if (!fromV.hasPathTo(w)) {
                vLength = Integer.MAX_VALUE;
            }
            else {
                for (int i : fromV.pathTo(w)) {
                    vLength++;
                }
            }

            int wLength = 0;
            if (!fromw.hasPathTo(v)) {
                wLength = Integer.MAX_VALUE;
            }
            else {
                for (int i : fromw.pathTo(v)) {
                    wLength++;
                }
            }
            /*
            System.out.println(v + ", " + w);
            System.out.println("v->w: " + vLength);
            System.out.println("w->v: " + wLength);

             */

            return vLength < wLength ? w : v;
        }

        // determine the root
        //  determine v's root
        //  determine w's root
        //  if different, no path -> return -1

        /*
        System.out.println(v + " root = " + getRootFrom(v));
        System.out.println(w + " root = " + getRootFrom(w));

         */

        int root = getRootFrom(v);
        if (root != getRootFrom(w)) {
            // System.out.println("cannot connect " + v + " and " + w + ": different roots");
            return -1;
        }

        // System.out.println("root from " + v + " and " + w + ": " + root);

        // use bfs to determine length from root to all points
        // BreadthFirstPaths fromRoot = new BreadthFirstPaths(graph, root);

        // find all common ancestors of v and w
        SET<Integer> anc = new SET<>();
        BreadthFirstPaths vParent = new BreadthFirstPaths(graph, v);
        BreadthFirstPaths wParent = new BreadthFirstPaths(graph, w);
        for (int i = 0; i < size; i++) {
            if (vParent.hasPathTo(i) && wParent.hasPathTo(i)) {
                anc.add(i);
            }
        }


        /*
        System.out.println("common ancestors of " + v + " and " + w);
        for (int i : anc) {
            System.out.println(i);
        }

         */

        // for every ancestor, run bfs
        // calculate dists v->a + w->a
        // return a with shortest dist
        int bestAncestor = -1;
        int shortestPath = Integer.MAX_VALUE;
        for (int a : anc) {
            // optimize!
            BreadthFirstPaths fromAncestor = new BreadthFirstPaths(reverse, a);
            if (fromAncestor.pathTo(v) == null || fromAncestor.pathTo(w) == null) {
                return -1;
            }
            int pathLength = -2; // account for overcounting
            for (int i : fromAncestor.pathTo(v)) {
                pathLength++;
            }
            for (int i : fromAncestor.pathTo(w)) {
                pathLength++;
            }
            // System.out.println(v + "->" + a + "->" + w + " = " + pathLength);
            if (pathLength < shortestPath) {
                shortestPath = pathLength;
                bestAncestor = a;
            }
        }


        return bestAncestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }
        return -1;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }
        return -1;
    }

    // do unit testing of this class
    public static void main(String[] args) {

        Digraph graph = new Digraph(25);
        graph.addEdge(21, 16);
        graph.addEdge(22, 16);
        graph.addEdge(23, 20);
        graph.addEdge(24, 20);
        graph.addEdge(13, 7);
        graph.addEdge(14, 7);
        graph.addEdge(15, 9);
        graph.addEdge(16, 9);
        graph.addEdge(17, 10);
        graph.addEdge(18, 10);
        graph.addEdge(19, 12);
        graph.addEdge(20, 12);
        graph.addEdge(7, 3);
        graph.addEdge(8, 3);
        graph.addEdge(9, 3);
        graph.addEdge(10, 5);
        graph.addEdge(11, 5);
        graph.addEdge(12, 5);
        graph.addEdge(3, 1);
        graph.addEdge(4, 1);
        graph.addEdge(5, 2);
        graph.addEdge(6, 2);
        graph.addEdge(1, 0);
        graph.addEdge(2, 0);

        SAP sap = new SAP(graph);

        assert sap.getRootFrom(13) == 0;
        assert sap.getRootFrom(16) == 0;

        assert sap.ancestor(13, 16) == 3;
        assert sap.length(13, 16) == 4;

        assert sap.ancestor(8, 11) == 0;
        assert sap.length(8, 11) == 6;


        Digraph dg2 = new Digraph(6);
        dg2.addEdge(1, 0);
        dg2.addEdge(1, 2);
        dg2.addEdge(2, 3);
        dg2.addEdge(3, 4);
        dg2.addEdge(4, 5);
        dg2.addEdge(5, 0);


        SAP sap2 = new SAP(dg2);

        assert !sap2.hasCycle();
        assert sap2.getRootFrom(0) == 0;
        assert sap2.getRootFrom(1) == 0;
        assert sap2.getRootFrom(5) == 0;
        assert sap2.ancestor(1, 3) == 3;
        assert sap2.length(1, 3) == 2;
        assert sap2.ancestor(1, 5) == 0;
        assert sap2.length(1, 5) == 2;

        Digraph g3 = new Digraph(15);
        g3.addEdge(1, 2);
        g3.addEdge(2, 3);
        g3.addEdge(3, 4);
        g3.addEdge(4, 5);
        g3.addEdge(5, 6);
        g3.addEdge(6, 1);
        g3.addEdge(7, 8);
        g3.addEdge(8, 9);
        g3.addEdge(9, 10);
        g3.addEdge(10, 11);
        g3.addEdge(11, 12);
        g3.addEdge(12, 8);
        g3.addEdge(13, 14);
        g3.addEdge(14, 0);
        g3.addEdge(0, 11);

        SAP s3 = new SAP(g3);
        assert s3.ancestor(7, 9) == 9;
        assert s3.length(7, 9) == 2;
        assert s3.ancestor(9, 2) == -1;
        assert s3.length(9, 2) == -1;

        Digraph d9 = new Digraph(9);
        d9.addEdge(7, 6);
        d9.addEdge(6, 3);
        d9.addEdge(3, 0);
        d9.addEdge(0, 6);
        d9.addEdge(3, 2);
        d9.addEdge(3, 4);
        d9.addEdge(4, 1);
        d9.addEdge(1, 3);
        d9.addEdge(5, 4);
        d9.addEdge(5, 8);

        SAP s9 = new SAP(d9);
        assert s9.length(1, 0) == 2;
        assert s9.length(8, 1) == -1;
        assert s9.ancestor(8, 1) == -1;
    }
}
