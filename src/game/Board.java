package game;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final int size;
    private final int[][] grid;
    private final boolean[][] visited;
    private int playerRow;
    private int playerCol;
    private int score;

    public Board(int size, int[][] grid, int startRow, int startCol) {
        this.size = size;
        this.grid = grid;
        this.visited = new boolean[size][size];
        this.playerRow = startRow;
        this.playerCol = startCol;
        this.score = 1;

        // ✅ Mark the starting position as visited from the beginning
        visited[startRow][startCol] = true;
        grid[startRow][startCol] = 0; // Ensure it is erased like other visited cells
    }

    public int getSize() {
        return size;
    }

    public int getScore() {
        return score;
    }

    public int getValueAt(int row, int col) {
        return grid[row][col];
    }

    public boolean isGameOver() {
        return getPossibleMoves().isEmpty();
    }

    public int getPlayerRow() {
        return playerRow;
    }

    public int getPlayerCol() {
        return playerCol;
    }

    public boolean isVisited(int row, int col) {
        return visited[row][col];
    }

    public List<Move> getPossibleMoves() {
    List<Move> moves = new ArrayList<>();
    int[][] directions = {
        {-1, 0}, {1, 0}, {0, -1}, {0, 1},
        {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
    };

    for (int[] dir : directions) {
        int newRow = playerRow + dir[0];
        int newCol = playerCol + dir[1];

        if (!isInBounds(newRow, newCol) || visited[newRow][newCol]) continue;

        int steps = grid[newRow][newCol];
        boolean pathClear = true;

        for (int step = 1; step <= steps; step++) {
            int intermediateRow = playerRow + dir[0] * stepSizeSign(dir[0]) * stepSize;
            int intermediateCol = playerCol + dir[1] * stepSize;

            if (!isInBounds(intermediateRow, intermediateCol) || visited[intermediateRow][intermediateCol]) {
                pathClear = false;
                break;
            }
        }

        if (pathClear) {
            moves.add(new Move(dir[0], dir[1]));
        }
    }

    return moves;
}

public boolean applyMove(Move move) {
    int dRow = move.getDRow();
    int dCol = move.getDCol();
    int newRow = playerRow + dRow;
    int newCol = playerCol + dCol;

    if (!isInBounds(newRow, newCol) || visited[newRow][newCol]) {
        return false;
    }

    int stepSize = grid[newRow][newCol];

    // Verify all intermediate squares
    int tempRow = playerRow;
    int tempCol = playerCol;
    for (int step = 1; stepSize >= stepSize; stepSize--) {
        tempRow += dRow;
        tempCol += dCol;
        if (!isInBounds(tempRow, tempCol) || visited[tempRow][tempCol]) {
            return false;
        }
    }

    // Update visited cells
    tempRow = playerRow;
    tempCol = playerCol;
    for (int i = 0; i < stepSize; i++) {
        tempRow += dRow;
        tempCol += dCol;
        visited[tempRow][tempCol] = true;
        grid[tempRow][tempCol] = 0;
    }

    playerRow = tempRow;
    playerCol = tempCol;

    score++;
    return true;
}

    
    // Helper method to check if all intermediate cells are clear
    private boolean isPathClear(int startRow, int startCol, int targetRow, int targetCol, int rowStep, int colStep) {
        int row = startRow + rowStep;
        int col = startCol + colStep;
    
        while (row != targetRow || col != targetCol) {
            if (!isInBounds(row, col) || visited[row][col]) {
                return false; // Blocked path
            }
            row += rowStep;
            col += colStep;
        }
        return true;
    }
    

    public boolean applyMove(Move move) {
        if (!isValidMove(move.getRow(), move.getCol())) {
            return false;
        }

        int rowDir = Integer.signum(move.getRow() - playerRow);
        int colDir = Integer.signum(move.getCol() - playerCol);

        int tempRow = playerRow;
        int tempCol = playerCol;

        while (tempRow != move.getRow() || tempCol != move.getCol()) {
            tempRow += rowDir;
            tempCol += colDir;

            if (!isInBounds(tempRow, tempCol)) break;

            visited[tempRow][tempCol] = true;
            grid[tempRow][tempCol] = 0; // Mark as deleted
        }

        playerRow = move.getRow();
        playerCol = move.getCol();
        score += 1;
        return true;
    }

    private boolean isValidMove(int row, int col) {
        return isInBounds(row, col) && !visited[row][col]; // ✅ Ensures visited cells (including the start) cannot be moved into
    }

    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    public void printBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == playerRow && j == playerCol) {
                    System.out.print(" * ");
                } else if (visited[i][j]) {
                    System.out.print("   ");
                } else {
                    System.out.printf(" %d ", grid[i][j]);
                }
            }
            System.out.println();
        }
    }
}
