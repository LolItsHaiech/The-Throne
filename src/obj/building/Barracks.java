package obj.building;

import obj.Player;
import obj.building.interfaces.TrainerBuilding;
import obj.soldier.Warrior;
import util.Position;

public class Barracks extends Building implements TrainerBuilding {
    private final Position POSITION;
    private boolean operation;
    private int operationTime;

    public Barracks(Player owner, Position POSITION) {
        super(owner);
        this.POSITION = POSITION;
        this.operation = false;
        this.operationTime = 0;
    }

    @Override
    public void trainNewUnit() {
        if (!operation) {
            this.operation = true;
            this.operationTime = 2;
        }
    }

    public void trainNewSoldier() {
        this.trainNewUnit();
    }

    @Override
    public void train() {
        if (this.operationTime > 0)
            this.operationTime--;
        if (this.operationTime == 0 && this.operation) {
            this.owner.getSoldiers().addFirst(new Warrior(null, this.owner, this.POSITION));// todo -> set weapon
            this.operation = false;
        }
    }
}
