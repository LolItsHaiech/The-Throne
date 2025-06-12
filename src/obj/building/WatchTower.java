package obj.building;

import obj.Player;
import util.MaterialCost;
import util.Position;

public class WatchTower extends Building{
    public WatchTower(Player owner, Position position) {
        super(owner, position);
    }

    @Override
    public MaterialCost getBuildingPrice() {
        return new MaterialCost(10, 20, 0, 0, 0);
    }

    @Override
    public int getDefenceModifier() {
        return 1;
    }

    @Override
    public int getRangeModifier() {
        return 2;
    }
}
