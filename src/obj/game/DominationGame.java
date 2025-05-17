package obj.game;

import obj.Player;
import obj.building.Castle;
import obj.map.Tile;

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
        Player p = null;
        for (Tile[] tiles : this.map) {
            for (Tile tile : tiles) {
                if (tile.getBuilding() instanceof Castle castle) {
                    if (p == null) {
                        p = castle.getOwner();
                    } else if (p != castle.getOwner()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public Player getWinner() {
        Player p = null;
        for (Tile[] tiles : this.map) {
            for (Tile tile : tiles) {
                if (tile.getBuilding() instanceof Castle castle) {
                    if (p == null) {
                        p = castle.getOwner();
                    } else if (p != castle.getOwner()) {
                        return null;
                    }
                }
            }
        }
        return p;
    }
}
