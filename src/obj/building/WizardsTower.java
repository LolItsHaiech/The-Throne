package obj.building;

import obj.Player;
import obj.building.interfaces.TrainerBuilding;
import obj.soldier.wizard.*;
import obj.soldier.wizard.functional.Magic;
import util.Position;

public class WizardsTower extends Building implements TrainerBuilding {
    private final Position POSITION;
    private final Magic magic;
    private boolean operation;
    private int operationTime;

    public WizardsTower(Position POSITION, Magic magic) {
        super(null);
        this.POSITION = POSITION;
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
                case defenceBoost -> wizard = new DefenceWizard(null, this.owner, this.POSITION);
                case healthBoost -> wizard = new HealerWizard(null, this.owner, this.POSITION);
                case speedBoost -> wizard = new SpeedWizard(null, this.owner, this.POSITION);
                case damageBoost -> wizard = new StrengthWizard(null, this.owner, this.POSITION);
            }
            this.owner.getSoldiers().addFirst(wizard);
            this.operation = false;
        }
    }

    public void capture(Player player) {this.owner = player;}
}
