package obj.building;

import obj.Player;
import obj.building.interfaces.TrainerBuilding;
import obj.soldier.wizard.*;
import obj.soldier.wizard.functional.Magic;
import util.LinkedList;
import util.MaterialCost;
import util.Position;
import util.map.Map;

public class WizardTower extends TrainerBuilding {
    private final Magic magic;

    public WizardTower(Position position, Magic magic) {
        super(null, position);
        this.magic = magic;
    }


    public void capture(Player player) {
        this.owner = player;
    }

    public Magic getMagic() {
        return magic;
    }

    @Override
    public MaterialCost getBuildingPrice() {
        return null;
    }

    @Override
    public Map<String, SoldierFactory> getAllowedSoldiers() {
        Map<String, SoldierFactory> allowedSoldiers = new Map<>();
        allowedSoldiers.addFirst("Wizard", ((weapon, player, position1) -> switch (magic) {
            case defenceBoost -> new DefenceWizard(weapon, player, position1);
            case damageBoost -> new StrengthWizard(weapon, player, position1);
            case healthBoost -> new HealerWizard(weapon, player, position1);
            case speedBoost -> new SpeedWizard(weapon, player, position1);
        }));
        return allowedSoldiers;
    }

}
