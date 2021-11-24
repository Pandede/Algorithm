import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.ST;

public class WordNet {
    private final ST<String, Bag<Integer>> nounToId = new ST<>();
    private final ST<Integer, String> idToNoun = new ST<>();
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException("synsets or hypernyms is null");

        // Read synsets
        In sStream = new In(synsets);
        int n;
        for (n = 0; !sStream.isEmpty(); n++) {
            String[] line = sStream.readLine().split(",");
            int id = Integer.parseInt(line[0]);
            String[] words = line[1].split(" ");

            // Construct IdToNoun
            idToNoun.put(id, line[1]);

            // Construct nounToId
            for (String w : words) {
                if (!nounToId.contains(w)) {
                    Bag<Integer> bag = new Bag<>();
                    bag.add(id);
                    nounToId.put(w, bag);
                } else {
                    Bag<Integer> bag = nounToId.get(w);
                    bag.add(id);
                }
            }
        }

        // Read hypernyms and build graph
        final Digraph G = new Digraph(n);
        In hStream = new In(hypernyms);
        while (!hStream.isEmpty()) {
            String[] line = hStream.readLine().split(",");
            int childId = Integer.parseInt(line[0]);
            for (int i = 1; i < line.length; i++) {
                int parentId = Integer.parseInt(line[i]);
                G.addEdge(childId, parentId);
            }
        }

        // Check if the graph is DAG or not
        // Throw exception if there is a cycle
        DirectedCycle dc = new DirectedCycle(G);
        if (dc.hasCycle())
            throw new IllegalArgumentException("cycle in the graph");

        // Throw exception if there is multiple roots
        int root = 0;
        for (int v = 0; v < G.V(); v++) {
            if (G.outdegree(v) == 0)
                root++;
            if (root > 1)
                throw new IllegalArgumentException("multiple roots");
        }
        sap = new SAP(G);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounToId.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException("word is null");
        return nounToId.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException("nounA or nounB is null");
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("nounA or nounB cannot be found");
        Bag<Integer> nounIdA = nounToId.get(nounA);
        Bag<Integer> nounIdB = nounToId.get(nounB);
        return sap.length(nounIdA, nounIdB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA
    // and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException("nounA or nounB is null");
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("nounA or nounB cannot be found");
        Bag<Integer> nounIdA = nounToId.get(nounA);
        Bag<Integer> nounIdB = nounToId.get(nounB);
        int ancestorId = sap.ancestor(nounIdA, nounIdB);
        return idToNoun.get(ancestorId);
    }

    // do unit testing of this class
    // public static void main(String[] args) {
    // }
}