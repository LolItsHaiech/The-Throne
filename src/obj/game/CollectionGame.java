package obj.game;

import obj.Player;

/** <h2>Collection Game</h2>
 * Game ends in a set amount of turns, the winner is the player with the most castles
 */


public class CollectionGame extends Game {

    public CollectionGame(Player[] players, int mapWidth, int mapHeight) {
        super(players, mapWidth, mapHeight);
    }

    public CollectionGame(Player[] players, int mapWidth, int mapHeight, long seed) {
        super(players, mapWidth, mapHeight, seed);
    }

    @Override
    public boolean isEnded() {
        return false; // todo
    }
}
