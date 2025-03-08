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
            {-1, 0}, {1, 0}, {0, -1}, {0, 1}, // N, S, W, E
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1} // NW, NE, SW, SE
        };

        for (int[] dir : directions) {
            int newRow = playerRow;
            int newCol = playerCol;
            
            while (true) {
                newRow += dir[0];
                newCol += dir[1];

                // ✅ Stop if we hit the edge or a previously deleted cell (including the starting position)
                if (!isInBounds(newRow, newCol) || visited[newRow][newCol]) break;

                int stepSize = grid[newRow][newCol];

                // Ensure there is enough space to move
                int targetRow = playerRow + (dir[0] * stepSize);
                int targetCol = playerCol + (dir[1] * stepSize);

                if (!isInBounds(targetRow, targetCol) || visited[targetRow][targetCol]) break;

                moves.add(new Move(targetRow, targetCol));
                break; // Found a valid move in this direction
            }
        }
        return moves;
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