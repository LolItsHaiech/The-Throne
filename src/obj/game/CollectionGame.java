package obj.game;

import obj.Player;

/** <h2>Collection Game</h2>
 * Game ends in a set amount of turns, the winner is the player with the most castles
 */


public class CollectionGame extends Game {
    private final int turnCountLimit;

    public CollectionGame(Player[] players, int mapWidth, int mapHeight, int turnCountLimit) {
        super(players, mapWidth, mapHeight);
        this.turnCountLimit = turnCountLimit;
    }

    public CollectionGame(Player[] players, int mapWidth, int mapHeight, long seed, int turnCountLimit) {
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
