package obj.building;

import obj.Player;
import obj.building.interfaces.CollectorBuilding;
import util.MaterialCost;
import util.Position;

public class Farmland extends Building implements CollectorBuilding {
    public Farmland(Player owner, Position position) {
        super(owner, position);
    }

    @Override
    public MaterialCost getBuildingPrice() {
        return new MaterialCost(10, 5,5,0,0);
    }

    @Override
    public void collect() {
        this.owner.increaseFood(30);
    }
}
