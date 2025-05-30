package obj.building;

import obj.Player;
import obj.building.interfaces.TrainerBuilding;
import obj.soldier.wizard.*;
import obj.soldier.wizard.functional.Magic;
import util.MaterialCost;
import util.Position;

public class WizardsTower extends Building implements TrainerBuilding {
    private final Magic magic;
    private boolean operation;
    private int operationTime;

    public WizardsTower(Position position, Magic magic) {
        super(null, position);
        this.magic = magic;
        this.operation = false;
        this.operationTime = 0;
    }

    @Override
    public void trainNewUnit() {
        if (!operation) {
            this.operation = true;
            this.operationTime = 10;
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
            Wizard wizard = null;
            switch (magic) {
                case defenceBoost -> wizard = new DefenceWizard(null, this.owner, this.position);
                case healthBoost -> wizard = new HealerWizard(null, this.owner, this.position);
                case speedBoost -> wizard = new SpeedWizard(null, this.owner, this.position);
                case damageBoost -> wizard = new StrengthWizard(null, this.owner, this.position);
            }
            this.owner.getSoldiers().addFirst(wizard);
            this.operation = false;
        }
    }

    public void capture(Player player) {this.owner = player;}

    public Magic getMagic() {
        return magic;
    }

    @Override
    public MaterialCost getBuildingPrice() {
        return null;
    }
}
