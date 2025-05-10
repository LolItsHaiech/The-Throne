package obj.soldier;

import obj.Player;
import obj.Weapon;
import util.Position;

public class Warrior extends Soldier{
    public Warrior(Weapon weapon, Player player, Position position) {
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
