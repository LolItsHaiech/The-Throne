package obj.game;

import obj.Player;

/**
 * <h2>Perfection Game</h2>
 * Game ends in a set amount of turns, winner is the player with the most score
 */

public class PerfectionGame extends Game {
    public PerfectionGame(Player[] players, int mapWidth, int mapHeight) {
        super(players, mapWidth, mapHeight);
    }

    public PerfectionGame(Player[] players, int mapWidth, int mapHeight, long seed) {
        super(players, mapWidth, mapHeight, seed);
    }

    @Override
    public boolean isEnded() {
        return false; //todo
    }
}
