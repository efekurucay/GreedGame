package players; 
import game.*;

public class Player11 extends Player {
    private boolean[][] visited;
    
    public Player11(Board board) {
        super(board);
        visited = new boolean[board.getSize()][board.getSize()];
    }
    
    public Move nextMove() {
        // Geçerli hareketi ve yönleri belirle
        int[] directions = {-1, 0, 1}; // Yönler: -1, 0, 1 (Sırasıyla: Kuzey, Doğu, Güney, Batı vb.)
        int minSteps = Integer.MAX_VALUE;  // changed variable and initial value
        Move bestMove = null;
        
        for (int dRow : directions) {
            for (int dCol : directions) {
                if (dRow == 0 && dCol == 0) continue; // Hareket yoksa atla
                
                int steps = getMoveDistance(dRow, dCol); // Hareket mesafesini al
                // Use minimum instead of maximum
                if (steps > 0 && steps < minSteps) {
                    minSteps = steps;
                    bestMove = new Move(board.getPlayerRow() + dRow * steps, board.getPlayerCol() + dCol * steps);
                }
            }
        }
        
        if (bestMove != null) {
            updateGrid(bestMove); // Tahtayı güncelle
        }
        
        return bestMove; // En iyi hareketi geri döndür
    }
    
    private int getMoveDistance(int dRow, int dCol) {
        // İlk geçerli sayı ile hareket mesafesini hesapla
        int steps = 0;
        int row = board.getPlayerRow();
        int col = board.getPlayerCol();
        
        while (isValidMove(row + dRow, col + dCol)) {
            row += dRow;
            col += dCol;
            steps++;
            
            if (board.getValueAt(row, col) != 0) {
                break;
            }
        }
        return steps;
    }
    
    private boolean isValidMove(int row, int col) {
        // Tahtada geçerli mi kontrol et
        return row >= 0 && row < board.getSize() && col >= 0 && col < board.getSize()
            && !board.isVisited(row, col); // Ziyaret edilmemiş olmalı
    }
    
    private void updateGrid(Move move) {
        int startRow = board.getPlayerRow();
        int startCol = board.getPlayerCol();
        int diffRow = move.getRow() - startRow;
        int diffCol = move.getCol() - startCol;
        int steps = Math.max(Math.abs(diffRow), Math.abs(diffCol));
        int dRow = diffRow / steps;
        int dCol = diffCol / steps;
        for (int i = 1; i <= steps; i++) {
            int r = startRow + i * dRow;
            int c = startCol + i * dCol;
            visited[r][c] = true;
            // ...existing grid update if required...
        }
    }
    
    public int getCurrentRow() {
        return board.getPlayerRow();
    }
    
    public int getCurrentCol() {
        return board.getPlayerCol();
    }
}
