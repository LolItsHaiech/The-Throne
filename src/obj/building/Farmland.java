package obj.building;

import obj.Player;
import obj.building.interfaces.CollectorBuilding;

public class Farmland extends Building implements CollectorBuilding {
    public Farmland(Player owner) {
        super(owner);
    }

    @Override
    public void collect() {
        this.owner.increaseFood(30);
    }
}
