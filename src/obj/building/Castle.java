package obj.building;

import obj.Player;
import obj.building.interfaces.CollectorBuilding;
import obj.building.interfaces.TrainerBuilding;
import obj.map.Tile;
import obj.soldier.Commander;
import obj.soldier.Rider;
import obj.soldier.Warrior;
import util.LinkedList;
import util.MaterialCost;
import util.Position;
import util.map.Map;

public class Castle extends TrainerBuilding implements CollectorBuilding {
    private boolean operation;
    private int operationTime;

    public Castle(Player owner, Position position) {
        super(owner, position);
        this.operation = false;
        this.operationTime = 0;
    }

    public int getBorderRadius() {
        return 3;
    }


    @Override
    public void collect() {
        owner.increaseWealth(10);
    }


    public void capture(Player player) {
        this.owner.getCastles().remove(this); // todo
        player.getCastles().addFirst(this);
        int r = this.getBorderRadius();

        int minX = Math.max(this.position.x() - r, 0);
        int maxX = Math.min(this.position.x() + r, player.getGame().getMapWidth() - 1);
        int minY = Math.max(this.position.y() - r, 0);
        int maxY = Math.min(this.position.y() - r, player.getGame().getMapHeight() - 1);

        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                Tile tile = player.getGame().getMap()[x][y];
                if (tile.getBuilding() != null) {
                    tile.getBuilding().setOwner(player);
                }
            }
        }
    }

    @Override
    public Map<String, SoldierFactory> getAllowedSoldiers() {
        Map<String, SoldierFactory> allowedSoldiers = new Map<>();
        allowedSoldiers.addFirst("Commander", Commander::new);
        return allowedSoldiers;
    }


    @Override
    public MaterialCost getBuildingPrice() {
        return new MaterialCost(50, 10, 10, 0, 5);
    }

    @Override
    public int getDefenceModifier() {
        return 2;
    }
}
