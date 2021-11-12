import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        String word, champion = null;
        for (int i = 1; !StdIn.isEmpty(); i++) {
            word = StdIn.readString();
            if (StdRandom.bernoulli(1. / i))
                champion = word;
        }
        StdOut.println(champion);
    }
}
