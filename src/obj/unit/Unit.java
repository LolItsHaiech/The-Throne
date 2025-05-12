package obj.unit;

import obj.Player;
import obj.Weapon;
import obj.unit.wizard.functional.Effect;
import util.Position;

public abstract class Unit {
    private Weapon weapon;
    private Position position;
    private final Player player;
    private int health;

    private int speedModifier;
    private int damageModifier;
    private int defenceModifier;

    private Effect effect;
    // todo decrement every turn;
    private int effectDurationLeft;


    public Unit(Weapon weapon, Player player, Position position) {
        this.weapon = weapon;
        this.position = position;
        this.player = player;
        this.health = 100;

        this.speedModifier = 0;
        this.damageModifier = 0;
        this.defenceModifier = 0;

        this.effectDurationLeft = 0;
        this.effect = null;
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

    public int getBaseRange() {
        return 0;
    }

    public int getSpeed() {
        return Math.max(1, this.getBaseSpeed() + this.player.getGame().getTile(this.position).getSpeedModifier() + this.weapon.getSpeed() + this.speedModifier);
    }

    public int getRange() {
        return Math.max(1, this.getBaseRange() + this.player.getGame().getTile(this.position).getRangeModifier() + this.weapon.getRange());
    }

    public int getDefence() {
        return Math.max(1, this.getBaseDefense() + this.player.getGame().getTile(this.position).getDefenceModifier() + this.defenceModifier);
    }

    public int getDamage() {
        return Math.max(0, this.getBaseDamage() + this.getWeapon().getDamage() + this.damageModifier);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getSpeedModifier() {
        return speedModifier;
    }

    public void setSpeedModifier(int speedModifier) {
        this.speedModifier = speedModifier;
    }

    public int getDamageModifier() {
        return damageModifier;
    }

    public void setDamageModifier(int damageModifier) {
        this.damageModifier = damageModifier;
    }

    public int getDefenceModifier() {
        return defenceModifier;
    }

    public void setDefenceModifier(int defenceModifier) {
        this.defenceModifier = defenceModifier;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
        this.effect.effectStart(this);
        this.effectDurationLeft = effect.DURATION;
    }

    public void onTurnStart() {
        if (this.effect != null) {
            this.effectDurationLeft--;
            if (this.effectDurationLeft <= 0) {
                this.effect.effectEnd(this);
                this.effect = null;
            }
        }
    }

    public void onTurnEnd() {
    }
}
