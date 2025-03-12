package game;

public class Move {
    public final int dRow;
    public final int dCol;

    public Move(int dRow, int dCol) {
        this.dRow = dRow;
        this.dCol = dCol;
    }

    private final int dRow;
    private final int dCol;

    public int getDRow() { return dRow; }
    public int getDCol() { return dCol; }

    @Override
    public String toString() {
        return String.format("Move[dRow=%d, dCol=%d]", dRow, dCol);
    }
}
