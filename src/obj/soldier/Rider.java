package obj.soldier;

import obj.Player;
import obj.Weapon;
import util.Position;

public class Rider extends Soldier{
    public Rider(Weapon weapon, Player player, Position position) {
        super(weapon, player, position);
    }

    @Override
    public int getBaseSpeed() {
        return 5;
    }

    @Override
    public int getBaseDamage() {
        return 35;
    }

    @Override
    public int getBaseDefense() {
        return 5;
    }
}
