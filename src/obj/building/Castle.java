package obj.building;

import obj.Player;
import obj.building.interfaces.CollectorBuilding;
import obj.map.Tile;
import util.Position;

public class Castle extends Building implements CollectorBuilding {
    private final Position POSITION;
    private int level;

    public Castle(Player owner, Position position) {
        super(owner);
        this.level = 1;
        this.POSITION = position;
    }

    public int getLevel() {
        return this.level;
    }

    public int getBorderRadius() {
        return 2 * this.level + 1;
    }

    public void incrementLevel(int level) {
        this.level++;
    }

    @Override
    public void collect() {
        owner.increaseWealth(10 * this.level);
    }

    public void capture(Player player) {
        this.owner.getCastles().remove(this); // todo
        player.getCastles().addFirst(this);
        int radius = this.getBorderRadius();
        for (int x = 0; x < radius; x++) {
            for (int y = 0; y < radius; y++) {
                Tile tile = player.getGame().getMap()[x][y];
                if (tile.getBuilding() != null) {
                    tile.getBuilding().setOwner(player);
                }
            }
        }
    }
}
