import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    private class BoardNode implements Comparable<BoardNode> {
        private final Board board;
        private final BoardNode parent;
        private final int distance;
        private final int moves;
        private final boolean isTwin;
        private final int priorty;

        public BoardNode(Board board, boolean isTwin) {
            this.board = board;
            this.parent = null;
            this.distance = board.manhattan();
            this.moves = 0;
            this.isTwin = isTwin;
            this.priorty = this.distance;
        }

        public BoardNode(Board board, BoardNode parent) {
            this.board = board;
            this.parent = parent;
            this.distance = board.manhattan();
            this.moves = parent.moves + 1;
            this.isTwin = parent.isTwin;
            this.priorty = this.distance + this.moves;
        }

        public Board getBoard() {
            return this.board;
        }

        public boolean isTwin() {
            return this.isTwin;
        }

        public BoardNode getParent() {
            return this.parent;
        }

        @Override
        public int compareTo(Solver.BoardNode that) {
            if (this.priorty > that.priorty)
                return 1;
            if (this.priorty < that.priorty)
                return -1;
            return 0;
        }

    }

    private final Board initial;
    private Stack<Board> solution = new Stack<>();
    private boolean solvable;
    private int moves;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();
        this.initial = initial;
        this.solve();
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return this.solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return this.moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return this.solution;
    }

    private void solve() {
        MinPQ<BoardNode> tree = new MinPQ<>();
        tree.insert(new BoardNode(this.initial, false));
        tree.insert(new BoardNode(this.initial.twin(), true));
        BoardNode currentNode = tree.delMin();
        Board currentBoard = currentNode.getBoard();
        while (!currentBoard.isGoal()) {
            for (Board childBoard : currentBoard.neighbors()) {
                if (currentNode.getParent() == null || !childBoard.equals(currentNode.getParent().getBoard()))
                    tree.insert(new BoardNode(childBoard, currentNode));
            }
            currentNode = tree.delMin();
            currentBoard = currentNode.getBoard();
        }
        this.solvable = !currentNode.isTwin();

        if (this.solvable) {
            while (currentNode != null) {
                currentBoard = currentNode.getBoard();
                this.solution.push(currentBoard);
                currentNode = currentNode.getParent();
            }
            this.moves = this.solution.size() - 1;
        } else {
            this.solution = null;
            this.moves = -1;
        }

    }

    // test client (see below)
    public static void main(String[] args) {
        int[][] tiles = { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
        Solver solver = new Solver(new Board(tiles));
        System.out.println("[isSolvable] " + solver.isSolvable());
        System.out.println("[Path]");
        for (Board b : solver.solution()) {
            System.out.println(b);
        }
        System.out.println("[Moves] " + solver.moves());
    }
}
