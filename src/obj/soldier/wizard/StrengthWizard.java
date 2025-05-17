package obj.soldier.wizard;

import obj.Player;
import obj.Weapon;
import obj.soldier.wizard.functional.Magic;
import util.Position;

public class StrengthWizard extends Wizard {
    public StrengthWizard(Weapon weapon, Player player, Position position) {
        super(weapon, player, position);
    }

    @Override
    public Magic getEffect() {
        return Magic.damageBoost;
    }
}
