package obj.game;

import obj.Player;
import obj.auth.User;
import obj.building.Castle;
import obj.map.Tile;

/** <h2>Domination Game</h2>
 * Game ends when a player captures all the castles
 * (aka. no other player is alive)
 */

public class DominationGame extends Game{

    public DominationGame(String name, User[] users, int mapWidth, int mapHeight) {
        super(name, users, mapWidth, mapHeight);
    }

    public DominationGame(String name, User[] users, int mapWidth, int mapHeight, long seed) {
        super(name, users, mapWidth, mapHeight, seed);
    }

    @Override
    public boolean isEnded() {
        if (this.map == null) {
            return false;
        }
        Player p = null;
        System.out.println("fodkfkodf");
        for (Tile[] tiles : this.map) {
            for (Tile tile : tiles) {
                if (tile.getBuilding() instanceof Castle castle) {
                    System.out.println("yoooo");
                    System.out.println(castle.getOwner().getUser().getUsername());
                    if (p == null) {
                        p = castle.getOwner();
                    } else if (p != castle.getOwner()) {
                        System.out.println(p.getUser().getUsername());
                        System.out.println(castle.getOwner().getUser().getUsername());
                        System.out.println("yo");
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
