package obj.building;

import obj.Player;
import obj.building.interfaces.CollectorBuilding;
import util.MaterialCost;
import util.Position;

public class LumberHut extends Building implements CollectorBuilding {
    public LumberHut(Player owner, Position position) {
        super(owner, position);
    }

    @Override
    public MaterialCost getBuildingPrice() {
        return new MaterialCost(10, 0,10,0,0);
    }

    @Override
    public void collect() {
        this.owner.increaseWood(30);
    }
}
