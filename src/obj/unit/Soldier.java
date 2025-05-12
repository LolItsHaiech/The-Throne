package obj.unit;

import obj.Player;
import obj.Weapon;
import util.Position;

public class Soldier extends Unit {
    public Soldier(Weapon weapon, Player player, Position position) {
        super(weapon, player, position);
    }

    @Override
    public int getBaseSpeed() {
        return 3;
    }

    @Override
    public int getBaseDamage() {
        return 30;
    }

    @Override
    public int getBaseDefense() {
        return 5;
    }
}
