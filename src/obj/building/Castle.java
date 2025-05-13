package obj.building;

import obj.Player;
import obj.building.interfaces.CollectorBuilding;
import obj.building.interfaces.TrainerBuilding;
import obj.map.Tile;
import obj.soldier.Commander;
import util.Position;

public class Castle extends Building implements CollectorBuilding, TrainerBuilding {
    private final Position POSITION;
    private boolean operation;
    private int level, operationTime;

    public Castle(Player owner, Position position) {
        super(owner);
        this.level = 1;
        this.operation = false;
        this.operationTime = 0;
        this.POSITION = position;
    }

    public int getLevel() {
        return this.level;
    }

    public int getBorderRadius() {
        return this.level + 1;
    }

    public int getBorderLength() {
        return 2 * this.level + 1;
    }

    public void incrementLevel(int level) {
        this.level++;
    }

    @Override
    public void collect() {
        owner.increaseWealth(10 * this.level);
    }

    @Override
    public void trainNewUnit() {
        this.operation = true;
        this.operationTime = 6;
    }

    public void trainNewCommander() {
        this.trainNewUnit();
    }

    @Override
    public void train() {
        if (this.operationTime > 0)
            this.operationTime--;
        if (this.operationTime == 0 && this.operation) {
            Commander unit = new Commander(null, null, this.POSITION);// todo
            this.owner.getSoldiers().addFirst(unit);
            this.operation = false;
        }
    }

    public void capture(Player player) {
        this.owner.getCastles().remove(this); // todo
        player.getCastles().addFirst(this);
        int radius = this.getBorderLength();
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
