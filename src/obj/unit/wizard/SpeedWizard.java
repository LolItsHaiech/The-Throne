package obj.unit.wizard;

import obj.Player;
import obj.Weapon;
import obj.unit.wizard.functional.Effect;
import util.Position;

public class SpeedWizard extends Wizard{
    public SpeedWizard(Weapon weapon, Player player, Position position) {
        super(weapon, player, position);
    }

    @Override
    public Effect getEffect() {
        return Effect.speedBoost;
    }
}
