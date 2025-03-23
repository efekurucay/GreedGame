package players;
//20220808005 - Yahya Efe Kuruçay
import game.*;
import java.util.*;

public class Player20220808005 extends Player {
    public Player20220808005(Board board) {
        super(board);
    }

    // Dijkstra algoritması için öncelik kuyruğu
    private PriorityQueue<Move> moveQueue = new PriorityQueue<>(Comparator.comparingInt(this::calculateMoveWeight));

    // Hamle ağırlığını hesaplayan yardımcı metod
    private int calculateMoveWeight(Move move) {
        int newRow = board.getPlayerRow() + move.getDRow();
        int newCol = board.getPlayerCol() + move.getDCol();
        
        // Dinamik ağırlık faktörleri
        double coverage = board.getCoveragePercentage();
        int valueWeight = (int)(20 - (coverage * 0.125));
        int stabilityWeight = (int)(coverage * 0.25);
        int heuristicWeight = 15 - (int)(coverage * 0.1);
        
        // Graf tabanlı heuristic değer
        int heuristicValue = calculateAStarHeuristic(newRow, newCol);
        
        int cellValue = board.getValueAt(newRow, newCol);
        int centerDistance = Math.abs(newRow - board.getSize()/2) + Math.abs(newCol - board.getSize()/2);
        
        return (cellValue * valueWeight) 
               + (heuristicValue * heuristicWeight) 
               - (centerDistance * stabilityWeight);
    }

    // Yeni A* tabanlı heuristic fonksiyonu
    private int calculateAStarHeuristic(int row, int col) {
        int maxPotential = 0;
        for(int i=-2; i<=2; i++) {
            for(int j=-2; j<=2; j++) {
                if(board.isValidPosition(row+i, col+j) && !board.isVisited(row+i, col+j)) {
                    int distance = Math.abs(i) + Math.abs(j);
                    maxPotential += board.getValueAt(row+i, col+j) * (5 - distance);
                }
            }
        }
        return maxPotential;
    }

    @Override
    public Move nextMove() {
        // Derinlikli simülasyon için 3 seviye öngörü
        return findOptimalMove(board, 3);
    }

    private Move findOptimalMove(Board currentBoard, int depth) {
        // Dijkstra algoritmasıyla tüm olası hamleleri değerlendir
        evaluateAllMoves(currentBoard);
        
        Move bestMove = null;
        int maxScore = Integer.MIN_VALUE;
        
        while(!moveQueue.isEmpty()) {
            Move move = moveQueue.poll();
            Board simulated = new Board(currentBoard);
            if(simulated.applyMove(move)) {
                int score = depth > 1 ? 
                    simulateWithDepth(simulated, depth - 1) : 
                    simulated.getScore();
                
                // Skor + grid stabilite analizi
                score += calculateGridStability(simulated);
                
                if(score > maxScore) {
                    maxScore = score;
                    bestMove = move;
                }
            }
        }
        return bestMove;
    }

    // Tüm hamleleri ağırlıklarına göre kuyruğa ekle
    private void evaluateAllMoves(Board board) {
        moveQueue.clear();
        for(Move move : board.getPossibleMoves()) {
            moveQueue.add(move);
        }
    }

    // Grid stabilite skorunu hesapla (yıkılma riski düşük alanlar)
    private int calculateGridStability(Board board) {
        int stability = 0;
        int score = board.getScore();
        
        // 5 adım ötesini simüle ederek stabiliteyi ölç
        for(int i=0; i<5; i++){
            List<Move> moves = board.getPossibleMoves();
            if(moves.isEmpty()) break;
            
            // En yüksek potansiyelli hamleyi seç
            Move best = moves.stream()
                .max(Comparator.comparingInt(m -> {
                    Board b = new Board(board);
                    b.applyMove(m);
                    return b.getPossibleMoves().size(); // Kalan hamle sayısına göre optimizasyon
                }))
                .orElse(moves.get(0));
            
            if(!board.applyMove(best)) break;
            
            // Stabilite puanı = kalan hamle sayısı * mevcut skor
            stability += board.getPossibleMoves().size() * board.getScore();
        }
        return stability;
    }

    // Derinlikli simülasyon ile en iyi skoru bulma
    private int simulateWithDepth(Board board, int depth) {
        if(depth == 0 || board.getPossibleMoves().isEmpty()) {
            return board.getScore();
        }
        
        int maxScore = Integer.MIN_VALUE;
        for(Move move : board.getPossibleMoves()) {
            Board cloned = new Board(board);
            if(cloned.applyMove(move)) {
                // Özyinelemeli olarak sonraki seviyeyi simüle et
                int score = simulateWithDepth(cloned, depth - 1);
                
                // Grid stabilite ve derinlik faktörünü ekle
                score += calculateGridStability(cloned) * (depth + 1);
                
                if(score > maxScore) {
                    maxScore = score;
                }
            }
        }
        return maxScore;
    }
}