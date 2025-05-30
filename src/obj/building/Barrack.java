package obj.building;

import obj.Player;
import obj.building.interfaces.TrainerBuilding;
import obj.soldier.Warrior;
import util.MaterialCost;
import util.Position;

public class Barrack extends Building implements TrainerBuilding {
    private boolean operation;
    private int operationTime;

    public Barrack(Player owner, Position position) {
        super(owner, position);
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
            this.owner.getSoldiers().addFirst(new Warrior(null, this.owner, this.position));// todo -> set weapon
            this.operation = false;
        }
    }

    @Override
    public MaterialCost getBuildingPrice() {
        return new MaterialCost(10, 5,5,5, 0);
    }
}
