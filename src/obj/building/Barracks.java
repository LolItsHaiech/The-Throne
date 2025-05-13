package obj.building;

import obj.Player;
import obj.building.interfaces.TrainerBuilding;
import obj.soldier.Rider;
import obj.soldier.Warrior;
import util.Position;

public class Barracks extends Building implements TrainerBuilding {
    private final Position POSITION;
    private boolean operation, rider;
    private int operationTime;

    public Barracks(Player owner, Position POSITION) {
        super(owner);
        this.POSITION = POSITION;
        this.operation = false;
        this.operationTime = 0;
    }

    @Override
    public void trainNewUnit() {
        this.operation = true;
        this.operationTime = 2;
    }

    public void trainNewSoldier(boolean rider) {
        this.rider = rider;
        this.trainNewUnit();
    }

    @Override
    public void train() {
        if (this.operationTime > 0)
            this.operationTime--;
        if (this.operationTime == 0 && this.operation) {
            if (this.rider)
                this.owner.getSoldiers().addFirst(new Rider(null, null, this.POSITION));// todo
            else
                this.owner.getSoldiers().addFirst(new Warrior(null, null, this.POSITION));// todo
            this.operation = false;
        }
    }
}
