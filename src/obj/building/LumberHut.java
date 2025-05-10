package obj.building;

import obj.Player;
import obj.building.interfaces.CollectorBuilding;

public class LumberHut extends Building implements CollectorBuilding {
    public LumberHut(Player owner) {
        super(owner);
    }

    @Override
    public void collect() {
        this.owner.increaseWood(30);
    }
}
