package obj.game;

import obj.Player;

/** <h2>Domination Game</h2>
 * Game ends when a player captures all the castles
 * (aka. no other player is alive)
 */

public class DominationGame extends Game{

    public DominationGame(Player[] players, int mapWidth, int mapHeight) {
        super(players, mapWidth, mapHeight);
    }

    public DominationGame(Player[] players, int mapWidth, int mapHeight, long seed) {
        super(players, mapWidth, mapHeight, seed);
    }

    @Override
    public boolean isEnded() {
        return false; //todo
    }
}
