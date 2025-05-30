package obj.building;

import obj.Player;
import obj.building.interfaces.CollectorBuilding;
import obj.map.Tile;
import util.MaterialCost;
import util.Position;

public class Mine extends Building implements CollectorBuilding {
    public Mine(Player owner, Position position) {
        super(owner, position);
    }

    @Override
    public MaterialCost getBuildingPrice() {
        return new MaterialCost(10, 0, 5, 0, 0);
    }

    @Override
    public void collect() {
        this.owner.increaseStone(50);
        if(this.owner.getGame().getMap()[this.position.x()][this.position.y()].getHeight()== Tile.Height.mountain) {
            this.owner.increaseIron(10);
        }
    }
}
