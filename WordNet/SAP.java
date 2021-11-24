import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import java.util.ArrayList;

public class SAP {
    private static final int INFINITY = Integer.MAX_VALUE;
    private final Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException("graph is null");

        this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        ArrayList<Integer> vs = new ArrayList<>();
        vs.add(v);
        ArrayList<Integer> ws = new ArrayList<>();
        ws.add(w);
        if (isValidVertice(vs) && isValidVertice(ws))
            return sap(vs, ws)[0];
        else
            throw new IllegalArgumentException("invalid vertice");
    }

    // a common ancestor of v and w that participates in a shortest ancestral path;
    // -1 if no such path
    public int ancestor(int v, int w) {
        ArrayList<Integer> vs = new ArrayList<>();
        vs.add(v);
        ArrayList<Integer> ws = new ArrayList<>();
        ws.add(w);
        if (isValidVertice(vs) && isValidVertice(ws))
            return sap(vs, ws)[1];
        else
            throw new IllegalArgumentException("invalid vertice");
    }

    // length of shortest ancestral path between any vertex in v and any vertex in
    // w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (isValidVertice(v) && isValidVertice(w))
            return sap(v, w)[0];
        else
            throw new IllegalArgumentException("invalid vertice");
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such
    // path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (isValidVertice(v) && isValidVertice(w))
            return sap(v, w)[1];
        else
            throw new IllegalArgumentException("invalid vertice");
    }

    private int[] sap(Iterable<Integer> v, Iterable<Integer> w) {
        if (!v.iterator().hasNext() || !w.iterator().hasNext())
            return new int[] { -1, -1 };
        BreadthFirstDirectedPaths vPaths = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths wPaths = new BreadthFirstDirectedPaths(G, w);

        int ancestor = -1;
        int minDist = INFINITY;
        for (int vt = 0; vt < G.V(); vt++) {
            if (vPaths.hasPathTo(vt) && wPaths.hasPathTo(vt)) {
                int sumDist = vPaths.distTo(vt) + wPaths.distTo(vt);
                if (sumDist < minDist) {
                    minDist = sumDist;
                    ancestor = vt;
                }
            }
        }
        if (minDist == INFINITY)
            minDist = -1;
        return new int[] { minDist, ancestor };
    }

    private boolean isValidVertice(Iterable<Integer> vs) {
        if (vs == null)
            return false;
        for (Integer v : vs) {
            if (v == null || v < 0 || v >= G.V())
                return false;
        }
        return true;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
