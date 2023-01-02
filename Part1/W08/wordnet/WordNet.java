/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WordNet {

    private List<String> nouns; // use a map instead
    // map all words to their index
    private List<String> defs;
    private Digraph graph;
    private int size;
    private SAP sap;


    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }

        nouns = new ArrayList<>();
        defs = new ArrayList<>();

        // process synsets
        try {
            File synsetsFile = new File(synsets);
            FileReader synFR = new FileReader(synsetsFile);
            BufferedReader buf = new BufferedReader(synFR);
            String line;
            while ((line = buf.readLine()) != null) {
                String[] parts = line.split(",", 3);
                int id = Integer.parseInt(parts[0]);
                // String syn = (parts[1].split(" "))[0];
                String syns = parts[1];
                String gloss = parts[2];
                nouns.add(syns); // don't need id because arraylist can be indexed
                defs.add(gloss);
            }
            buf.close();
            synFR.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        size = nouns.size();
        graph = new Digraph(size);

        // process hypernyms
        try {
            File synsetsFile = new File(hypernyms);
            FileReader synFR = new FileReader(synsetsFile);
            BufferedReader buf = new BufferedReader(synFR);
            String line;
            while ((line = buf.readLine()) != null) {
                String[] parts = line.split(",");
                int hyponym = Integer.parseInt(parts[0]);
                for (int i = 1; i < parts.length; i++) {
                    int hypernym = Integer.parseInt(parts[i]);
                    // add vertex
                    // id -> vert
                    graph.addEdge(hyponym, hypernym);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        sap = new SAP(graph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return new ArrayList<>(nouns);
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }
        return nouns.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException();
        }
        if (!(isNoun(nounA) && isNoun(nounB))) {
            throw new IllegalArgumentException();
        }

        // this can be optimized by using a map instead of a list
        int a = nouns.indexOf(nounA);
        int b = nouns.indexOf(nounB);
        return sap.length(a, b);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException();
        }
        if (!(isNoun(nounA) && isNoun(nounB))) {
            throw new IllegalArgumentException();
        }
        int a = nouns.indexOf(nounA);
        int b = nouns.indexOf(nounB);
        int c = sap.ancestor(a, b);
        return nouns.get(c);
    }

    private void printNouns() {
        for (int i = 0; i < nouns.size(); i++) {
            System.out.println(i + ": " + nouns.get(i));
            System.out.println("\t" + defs.get(i));
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet net = new WordNet("synsets.txt", "hypernyms.txt");
        // net.printNouns();

        assert net.isNoun("crow_pheasant");
        assert net.isNoun("modiolus");
        assert !net.isNoun("navy_yard naval_shipyard");
        assert net.isNoun("navy_yard");
        assert net.isNoun("Henry_Martyn_Robert");
        assert net.isNoun("anamorphosis");


        net.distance("crow_pheasant", "modiolus");
        net.sap("crow_pheasant", "modiolus");

    }
}
