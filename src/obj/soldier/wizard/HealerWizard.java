package obj.soldier.wizard;

import obj.Player;
import obj.Weapon;
import obj.soldier.wizard.functional.Magic;
import util.Position;

public class HealerWizard extends Wizard{
    public HealerWizard(Weapon weapon, Player player, Position position) {
        super(weapon, player, position);
    }

    @Override
    public Magic getEffect() {
        return Magic.healthBoost;
    }
}
