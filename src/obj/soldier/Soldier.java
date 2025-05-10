package obj.soldier;

import obj.Player;
import obj.Weapon;
import util.Position;

public abstract class Soldier {
    private Weapon weapon;
    private Position position;
    private final Player player;
    private int health;

    public Soldier(Weapon weapon, Player player, Position position) {
        this.weapon = weapon;
        this.position = position;
        this.player = player;
        this.health = 100;

    }

    public Player getPlayer() {
        return player;
    }

    public Weapon getWeapon() {
        return this.weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public Position getPos() {
        return position;
    }

    public abstract int getBaseSpeed();

    public abstract int getBaseDamage();

    public abstract int getBaseDefense();


    public int getSpeed() {
        return Math.max(1, this.getBaseSpeed() + this.player.getGame().getTile(this.position).getSpeedModifier()) + this.weapon.getSpeed();
    }

    public int getRange() {
        return Math.max(1, this.getBaseSpeed() + this.player.getGame().getTile(this.position).getRangeModifier() + this.weapon.getRange() - 1);
    }

    public int getDefence() {
        return Math.max(1, this.getBaseDefense() + this.player.getGame().getTile(this.position).getDefenceModifier());
    }

    public int getDamage() {
        return Math.max(0, this.getBaseDamage() + this.getWeapon().getDamage());
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
