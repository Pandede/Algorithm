import edu.princeton.cs.algs4.Queue;

public class Board {
    private final int[][] tiles;
    private final int dim;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = copy(tiles);
        this.dim = tiles.length;
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(String.format("%d\n", dim));
        for (int[] row : this.tiles) {
            for (int val : row) {
                s.append(String.format("%2d ", val));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return this.dim;
    }

    // number of tiles out of place
    public int hamming() {
        int totalDist = 0, cnt = 1;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (i == dim - 1 && j == dim - 1)
                    break;
                if (this.tiles[i][j] != cnt)
                    totalDist++;
                cnt++;
            }
        }
        return totalDist;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int totalDist = 0, x, y;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                int val = this.tiles[i][j];
                if (val == 0)
                    continue;
                x = (val - 1) / dim;
                y = (val - 1) % dim;
                totalDist += Math.abs(x - i) + Math.abs(y - j);
            }
        }
        return totalDist;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null)
            return false;
        if (y == this)
            return true;
        if (y.getClass() != this.getClass())
            return false;
        Board board = (Board) y;
        if (this.dim != board.dim)
            return false;
        for (int i = 0; i < dim; i++)
            for (int j = 0; j < dim; j++)
                if (this.tiles[i][j] != board.tiles[i][j])
                    return false;
        return true;
    }

    private void swap(int[][] t, int i1, int j1, int i2, int j2) {
        int temp = t[i1][j1];
        t[i1][j1] = t[i2][j2];
        t[i2][j2] = temp;
    }

    private boolean isValid(int i, int j) {
        return i >= 0 && j >= 0 && i < dim && j < dim;
    }

    private int[] getPosition(int val) {
        for (int i = 0; i < dim; i++)
            for (int j = 0; j < dim; j++)
                if (tiles[i][j] == val)
                    return new int[] { i, j };
        return new int[] { -1, -1 };
    }

    private int[][] copy(int[][] t) {
        int n = t.length;
        int[][] copyTiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                copyTiles[i][j] = t[i][j];
        return copyTiles;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        final int[] dirX = { 1, 0, -1, 0 };
        final int[] dirY = { 0, 1, 0, -1 };
        Queue<Board> neighbors = new Queue<Board>();
        int[] zeroPos = getPosition(0);
        for (int i = 0; i < 4; i++) {
            int newX = zeroPos[0] + dirX[i], newY = zeroPos[1] + dirY[i];
            if (isValid(newX, newY)) {
                int[][] childTiles = copy(this.tiles);
                swap(childTiles, zeroPos[0], zeroPos[1], newX, newY);
                neighbors.enqueue(new Board(childTiles));
            }
        }
        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] twinTiles = copy(this.tiles);
        int i1 = -1, j1 = -1, i2 = -1, j2 = -1;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (tiles[i][j] == 0)
                    continue;
                i1 = i;
                j1 = j;
                if (isValid(i, j + 1) && tiles[i][j + 1] != 0) {
                    i2 = i;
                    j2 = j + 1;
                    break;
                } else if (isValid(i, j - 1) && tiles[i][j - 1] != 0) {
                    i2 = i;
                    j2 = j - 1;
                    break;
                } else if (isValid(i + 1, j) && tiles[i + 1][j] != 0) {
                    i2 = i + 1;
                    j2 = j;
                    break;
                } else if (isValid(i - 1, j) && tiles[i - 1][j] != 0) {
                    i2 = i - 1;
                    j2 = j;
                    break;
                }
            }
            if (i2 > 0 && j2 > 0)
                break;
        }
        swap(twinTiles, i1, j1, i2, j2);
        return new Board(twinTiles);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] tiles = { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
        System.out.println("=====Testing=====");
        System.out.print("[Initialize]...");
        Board board = new Board(tiles);
        System.out.println("Success!\n");

        System.out.println("[Hamming]... dist = " + board.hamming());
        System.out.println("[Manhattan]... dist = " + board.manhattan());

        System.out.println("[ToString]");
        System.out.println(board.toString());

        System.out.println("[Neighbors]");
        for (Board b : board.neighbors())
            System.out.println(b.toString());

        System.out.println("[Twin]");
        System.out.println(board.twin());

        int[][] equalTiles = { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
        int[][] inequalTiles = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } };
        int[][] dimTiles = { { 1, 2 }, { 3, 4 } };
        Board equalBoard = new Board(equalTiles);
        Board inequalBoard = new Board(inequalTiles);
        Board dimBoard = new Board(dimTiles);
        System.out.println("[Equals]...");
        System.out.println("[Self]");
        System.out.println(board.equals(board));

        System.out.println("[Same]");
        System.out.println(equalBoard);
        System.out.println(board.equals(equalBoard));

        System.out.println("[Different]");
        System.out.println(inequalBoard);
        System.out.println(board.equals(inequalBoard));

        System.out.println("[Dimension]");
        System.out.println(dimBoard);
        System.out.println(board.equals(dimBoard));

        System.out.println("[Null]");
        System.out.println(board == null);

        System.out.println("[isGoal]...");
        System.out.println(board.isGoal());
        System.out.println(inequalBoard.isGoal());
    }

}