package obj;

import db.Config;
import util.MaterialCost;

public enum Weapon {
    spear(10, 0, 1, null, Config.instance.weaponCosts.spear, Config.instance.weaponCosts.spearUnlock),
    sword(20, 0, 1, spear, Config.instance.weaponCosts.sword, Config.instance.weaponCosts.swordUnlock),
    heavySword(35, -1, 1, sword, Config.instance.weaponCosts.heavySword, Config.instance.weaponCosts.heavySwordUnlock),
    lightSword(18, 1, 1, sword, Config.instance.weaponCosts.lightSword, Config.instance.weaponCosts.lightSwordUnlock),
    mace(15, -1, 2, spear, Config.instance.weaponCosts.mace, Config.instance.weaponCosts.maceUnlock),
    slingshot(5, 0, 2, null, Config.instance.weaponCosts.slingshot, Config.instance.weaponCosts.slingshotUnlock),
    bow(8, 1, 4, slingshot, Config.instance.weaponCosts.bow, Config.instance.weaponCosts.bowUnlock),
    shortBow(5, 1, 3, bow, Config.instance.weaponCosts.shortBow, Config.instance.weaponCosts.shortBowUnlock),
    longBow(10, 0, 2, bow, Config.instance.weaponCosts.longBow, Config.instance.weaponCosts.longBowUnlock),
    crossBow(8, 1, 3, bow, Config.instance.weaponCosts.crossBow, Config.instance.weaponCosts.crossBowUnlock),
    catapult(20, -2, 3, crossBow, Config.instance.weaponCosts.catapult, Config.instance.weaponCosts.catapultUnlock),

    // Mystical weapons
    excalibur(36, -1, 1, true),
    mjolnir(40, 0, 1, true),
    tridentOfPoseidon(31, 1, 2, true),
    axeOfPerun(37, 0, 1, true),
    anduril(26, 2, 1, true),
    spearOfLugh(28, 0, 2, true),
    harpe(32, 0, 1, true),
    ;

    private final int damage;
    private final int speed;
    private final int range;
    private final Weapon requiredWeapon;
    private final boolean mystical;
    private final MaterialCost price;
    private final MaterialCost unluckPrice;

    Weapon(int damage, int speed, int range, Weapon requiredWeapon, boolean mystical, MaterialCost price, MaterialCost unluckPrice) {
        this.damage = damage;
        this.speed = speed;
        this.range = range;
        this.requiredWeapon = requiredWeapon;
        this.price = price;
        this.unluckPrice = unluckPrice;
        this.mystical = mystical;
    }

    Weapon(int damage, int speed, int range, Weapon requiredWeapon, MaterialCost price, MaterialCost unluckPrice) {
        this(damage, speed, range, requiredWeapon, false, price, unluckPrice);
    }

    Weapon(int damage, int speed, int range, boolean mystical) {
        this(damage, speed, range, null, mystical,
             new MaterialCost(0, 0, 0, 0, 0),
             new MaterialCost(0, 0, 0, 0, 0));
    }

    public int getDamage() {
        return damage;
    }

    public int getSpeed() {
        return speed;
    }

    public int getRange() {
        return range;
    }

    public Weapon getRequiredWeapon() {
        return requiredWeapon;
    }

    public boolean isMystical() {
        return mystical;
    }

    @Override
    public String toString() {
        return this.name() + " (" + this.damage + ")";
    }
}
