package obj.game;

import obj.Player;
import obj.auth.User;
import obj.building.Castle;
import obj.map.Tile;

import java.util.HashMap;
import java.util.Map;

/** <h2>Collection Game</h2>
 * Game ends in a set amount of turns, the winner is the player with the most castles
 */


public class CollectionGame extends Game {
    private final int turnCountLimit;

    public CollectionGame(String name, User[] users, int mapWidth, int mapHeight, int turnCountLimit) {
        super(name, users, mapWidth, mapHeight);
        this.turnCountLimit = turnCountLimit;
    }

    public CollectionGame(String name, User[] users, int mapWidth, int mapHeight, long seed, int turnCountLimit) {
        super(name, users, mapWidth, mapHeight, seed);
        this.turnCountLimit = turnCountLimit;
    }

    @Override
    public boolean isEnded() {
        return this.getTurnCount() >= this.turnCountLimit;
    }

    public Player getWinner() {
        HashMap<Player, Integer> map = new HashMap<>();
        for (Tile[] tiles : this.map) {
            for (Tile tile : tiles) {
                if (tile.getBuilding() != null &&
                        tile.getBuilding() instanceof Castle castle) {
                    if(map.containsKey(castle.getOwner())) {
                        map.replace(castle.getOwner(), map.get(castle.getOwner()) + 1);
                    } else {
                        map.put(castle.getOwner(), 1);
                    }
                }
            }
        }
        int maxCount = 0;
        Player player = null;
        for (Map.Entry<Player, Integer> entry : map.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                player = entry.getKey();
            }
        }
        return maxCount==0?null:player;
    }

    public int getTurnCountLimit() {
        return turnCountLimit;
    }
}
