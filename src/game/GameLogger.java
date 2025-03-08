package game;

import java.util.*;

public class GameLogger {
    private List<int[][]> boardHistory = new ArrayList<>();
    private List<Move> moveHistory = new ArrayList<>();
    private Board board;

    public GameLogger(Board board) {
        this.board = board;
        saveState();
    }

    public void logMove(Move move) {
        moveHistory.add(move);
        saveState();
        printBoardState(move);  // Print board after each move
    }

    private void saveState() {
        int[][] snapshot = new int[board.getSize()][board.getSize()];
        for (int r = 0; r < board.getSize(); r++) {
            for (int c = 0; c < board.getSize(); c++) {
                snapshot[r][c] = board.getValueAt(r, c);
            }
        }
        boardHistory.add(snapshot);
    }

    private void printBoardState(Move move) {
        System.out.println("\nStep " + moveHistory.size() + " - Player moved to: (" + move.getRow() + ", " + move.getCol() + ")");

        int[][] latestBoard = boardHistory.get(boardHistory.size() - 1);
        for (int r = 0; r < latestBoard.length; r++) {
            for (int c = 0; c < latestBoard[r].length; c++) {
                if (r == move.getRow() && c == move.getCol()) {
                    System.out.print(" P ");  // Show player's position
                } else if (latestBoard[r][c] == 0) {
                    System.out.print(" . ");  // Empty cells already visited
                } else {
                    System.out.print(" " + latestBoard[r][c] + " ");
                }
            }
            System.out.println();
        }

        // Optional: Add a delay for animation effect
        try {
            Thread.sleep(500);  // 500ms delay for readability
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
