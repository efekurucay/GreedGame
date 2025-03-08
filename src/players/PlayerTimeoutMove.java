package players;

import game.*;
import java.util.List;

public class PlayerTimeoutMove extends Player {
    private int moveCounter = 0; // ✅ Track the number of moves

    public PlayerTimeoutMove(Board board) {
        super(board);
    }

    @Override
    public Move nextMove() {
        moveCounter++;
        
        // ✅ Simulate timeout after 3 valid moves
        if (moveCounter > 3) {
            try {
                System.out.println("PlayerTimeoutMove is thinking too long...");
                Thread.sleep(3000); // ❗ 3 seconds delay (should trigger move timeout)
            } catch (InterruptedException e) {
                System.err.println("Move interrupted.");
            }
        }

        List<Move> possibleMoves = board.getPossibleMoves();
        return possibleMoves.isEmpty() ? null : possibleMoves.get(0); // Picks first move
    }
}
