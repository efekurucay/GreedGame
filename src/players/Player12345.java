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

        Move bestMove = possibleMoves.get(0);
        int maxValue = board.getSize(); // Max number in the grid is 9

        for (Move move : possibleMoves) {
            int value = board.getPossibleMoves().size();
            if (value > maxValue) {
                bestMove = move;
                maxValue = value;
            }
        }
        return bestMove;
    }
}
