package obj.building;

import obj.Player;
import obj.building.interfaces.TrainerBuilding;
import obj.soldier.wizard.*;
import obj.soldier.wizard.functional.MagicType;
import util.Position;

public class WizardsTower extends Building implements TrainerBuilding {
    private final Position POSITION;
    private final MagicType magicType;
    private boolean operation;
    private int operationTime;

    public WizardsTower(Position POSITION, MagicType magicType) {
        super(null);
        this.POSITION = POSITION;
        this.magicType = magicType;
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
            switch (magicType) {
                case defenceWizard -> wizard = new DefenceWizard(null, this.owner, this.POSITION);
                case healerWizard -> wizard = new HealerWizard(null, this.owner, this.POSITION);
                case speedWizard -> wizard = new SpeedWizard(null, this.owner, this.POSITION);
                case strengthWizard -> wizard = new StrengthWizard(null, this.owner, this.POSITION);
            }
            this.owner.getSoldiers().addFirst(wizard);
            this.operation = false;
        }
    }

    public void capture(Player player) {this.owner = player;}
}
