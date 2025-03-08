package players;

import game.*;

public class PlayerTimeoutPrepare extends Player {
    public PlayerTimeoutPrepare(Board board) {
        super(board);

        // ⏳ Simulate an extremely slow preparation (e.g., unnecessary computation)
        try {
            System.out.println("PlayerTimeoutPrepare is initializing...");
            Thread.sleep(5000); // ❗ 5 seconds delay (should trigger constructor timeout)
        } catch (InterruptedException e) {
            System.err.println("Initialization interrupted.");
        }
    }

    @Override
    public Move nextMove() {
        return null; // Doesn't matter, this player should timeout before playing
    }
}
