package obj.soldier;

import obj.Player;
import obj.Weapon;
import obj.soldier.wizard.functional.Magic;
import util.Position;

public abstract class Soldier {
    private Weapon weapon;
    private Position position;
    private final Player player;
    private int health;

    private int speedModifier;
    private int damageModifier;
    private int defenceModifier;

    private Magic magic;
    // todo decrement every turn;
    private int effectDurationLeft;
    private boolean hasMoved;
    private boolean hasAttacked;


    public Soldier(Weapon weapon, Player player, Position position) {
        this.weapon = weapon;
        this.position = position;
        this.player = player;
        this.health = 100;

        this.speedModifier = 0;
        this.damageModifier = 0;
        this.defenceModifier = 0;

        this.effectDurationLeft = 0;
        this.magic = null;
        this.hasMoved = false;
        this.hasAttacked = false;
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

    public Magic getEffect() {
        return magic;
    }

    public void setEffect(Magic magic) {
        this.magic = magic;
        this.magic.effectStart(this);
        this.effectDurationLeft = magic.DURATION;
    }

    public void onTurnStart() {
        this.hasMoved = false;
        this.hasAttacked = false;
        if (this.magic != null) {
            this.effectDurationLeft--;
            if (this.effectDurationLeft <= 0) {
                this.magic.effectEnd(this);
                this.magic = null;
            }
        }
    }

    public void onTurnEnd() {
    }

    public boolean moveTo(Position newPos) {
        if (this.hasMoved) {
            return false;
        }
        double distance = this.position.distanceTo(newPos);

        if (distance > this.getSpeed()) {
            return false;
        }

        if (this.getPlayer().getGame().getMap()[newPos.x()][newPos.y()].getSoldier() != null) {
            return false;
        }

        this.getPlayer().getGame().getMap()[this.position.x()][this.position.y()].setSoldier(null);
        this.getPlayer().getGame().getMap()[newPos.x()][newPos.y()].setSoldier(this);

        this.position = newPos;
        this.hasMoved = true;
        return true;
    }

    public boolean hasMoved() {
        return this.hasMoved;
    }

    public boolean attack(Position targetPos) {
        if (this.hasAttacked) {
            return false;
        }

        Soldier target = this.getPlayer().getGame().getMap()[targetPos.x()][targetPos.y()].getSoldier();

        if (target == null) {
            return false;
        }

        if (target.getPlayer() == this.getPlayer()) {
            return false;
        }

        double distance = this.position.distanceTo(targetPos);
        if (distance > this.getRange()) {
            return false;
        }

        int damage = this.getDamage();
        int defence = target.getDefence();
        int netDamage = Math.max(0, damage - defence); // اگه دفاع بیشتر از حمله بود، صفر بشه

        target.setHealth(target.getHealth() - netDamage);

        if (target.getHealth() <= 0) {
            this.getPlayer().getGame().getMap()[targetPos.x()][targetPos.y()].setSoldier(null);
        }
        // می‌تونی لاگ یا افکت هم بزاری
        return true; // حمله با موفقیت انجام شد
    }
}