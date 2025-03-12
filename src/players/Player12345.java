package players;

import game.*;
import java.util.List;

public class Player12345 extends Player {

    public Player12345(Board board) {
        super(board);
    }

    @Override
    public Move nextMove() {
        List<Move> possibleMoves = board.getPossibleMoves();
        if (possibleMoves.isEmpty()) {
            return null; // No moves left
        }

        Move bestMove = null;
        int maxSteps = -1;

        // Picks the move that allows the furthest movement (just as an example strategy)
        for (Move move : possibleMoves) {
            int stepSize = board.getStepSizeForDirection(move.getDRow(), move.getDCol());
            if (stepSize > maxValue) {
                maxValue = stepSize;
                bestMove = move;
            }
        }
        return bestMove;
    }
}