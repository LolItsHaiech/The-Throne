package obj.unit;

import obj.Player;
import obj.Weapon;
import util.Position;

public class Commander extends Unit {
    public Commander(Weapon weapon, Player player, Position position) {
        super(weapon, player, position);
    }

    @Override
    public int getBaseSpeed() {
        return 4;
    }

    @Override
    public int getBaseDamage() {
        return 40;
    }

    @Override
    public int getBaseDefense() {
        return 6;
    }
}
