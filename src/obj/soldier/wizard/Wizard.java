package obj.soldier.wizard;

import exceptions.IllegalMoveException;
import obj.Player;
import obj.Weapon;
import obj.soldier.Soldier;
import obj.soldier.wizard.functional.Magic;
import util.Position;

public abstract class Wizard extends Soldier {
    public Wizard(Weapon weapon, Player player, Position position) {
        super(weapon, player, position);
    }

    @Override
    public int getBaseSpeed() {
        return 4;
    }

    @Override
    public int getBaseDamage() {
        return 0;
    }

    @Override
    public int getBaseDefense() {
        return 5;
    }

    @Override
    public int getDamage() {
        return 0;
    }

    @Override
    public int getBaseRange() {
        return 9;
    }

    public int getPowerRadius() {
        return 3;
    }

    public abstract Magic getEffect();

    public void castEffect(Position target) throws IllegalMoveException {
        if (Math.abs(this.getPos().x() - target.x()) > this.getRange() || Math.abs(this.getPos().y() - target.y()) > this.getRange()) {
            throw new IllegalMoveException("target is out of reach");
        }
        for (int i = target.x()-1; i < target.x()+1; i++) {
            for (int j = target.y()-1; j < target.y()+1; j++) {
                Soldier soldier = this.getPlayer().getGame().getMap()[i][j].getSoldier();
                if(soldier!=null) {
                    soldier.setEffect(this.getEffect());
                }
            }
        }
    }
}
