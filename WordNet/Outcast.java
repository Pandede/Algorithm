import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wordnet;

    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    public String outcast(String[] nouns) {
        int n = nouns.length;
        int maxDist = -1, maxIndex = -1;
        for (int i = 0; i < n; i++) {
            int tempDist = 0;
            for (int j = 0; j < n; j++) {
                tempDist += wordnet.distance(nouns[i], nouns[j]);
            }
            if (tempDist > maxDist) {
                maxDist = tempDist;
                maxIndex = i;
            }
        }
        return nouns[maxIndex];
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}