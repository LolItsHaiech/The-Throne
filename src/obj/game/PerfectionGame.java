package obj.game;

import obj.Player;

/**
 * <h2>Perfection Game</h2>
 * Game ends in a set amount of turns, winner is the player with the most score
 */

public class PerfectionGame extends Game {
    private final int turnCountLimit;

    public PerfectionGame(Player[] players, int mapWidth, int mapHeight, int turnCountLimit) {
        super(players, mapWidth, mapHeight);
        this.turnCountLimit = turnCountLimit;
    }

    public PerfectionGame(Player[] players, int mapWidth, int mapHeight, long seed, int turnCountLimit) {
        super(players, mapWidth, mapHeight, seed);
        this.turnCountLimit = turnCountLimit;
    }

    @Override
    public boolean isEnded() {
        return this.getTurnCount() >= this.turnCountLimit;
    }

    public int getTurnCountLimit() {
        return turnCountLimit;
    }
}
