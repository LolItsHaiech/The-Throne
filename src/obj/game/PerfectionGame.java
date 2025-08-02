package obj.game;

import obj.Player;
import obj.auth.User;

/**
 * <h2>Perfection Game</h2>
 * Game ends in a set amount of turns, winner is the player with the most score
 */

public class PerfectionGame extends Game {
    private final int turnCountLimit;

    public PerfectionGame(String name, User[] users, int mapWidth, int mapHeight, int turnCountLimit) {
        super(name, users, mapWidth, mapHeight);
        this.turnCountLimit = turnCountLimit;
    }

    public PerfectionGame(String name, User[] users, int mapWidth, int mapHeight, long seed, int turnCountLimit) {
        super(name, users, mapWidth, mapHeight, seed);
        this.turnCountLimit = turnCountLimit;
    }

    @Override
    public boolean isEnded() {
        return this.getTurnCount() >= this.turnCountLimit;
    }

    @Override
    public Player getWinner() {
        return null; //todo
    }

    public int getTurnCountLimit() {
        return turnCountLimit;
    }
}
