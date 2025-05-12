package obj.building;

import obj.Player;
import obj.building.interfaces.CollectorBuilding;

public class Mine extends Building implements CollectorBuilding {
    public final boolean hasIron;

    public Mine(Player owner, boolean hasIron) {
        super(owner);
        this.hasIron = hasIron;
    }

    public Mine(Player owner) {
        this(owner, false);
    }

    @Override
    public void collect() {
        this.owner.increaseStone(50);
        if (this.hasIron) {
            this.owner.increaseIron(10);
        }
    }
}
