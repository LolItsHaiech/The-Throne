package obj;

public enum Weapon {
    spear       (10, 0, 1, null),
    sword       (20, 0, 1, spear),
    heavySword  (35, -1, 1, sword),
    lightSword  (18, 1, 1, sword),
    mace        (15, -1, 2, spear),
    slingshot   (5, 0, 2, null),
    bow         (8, 1, 4, slingshot),
    shortBow    (5, 1, 3, bow),
    longBow     (10, 0, 2, bow),
    crossBow    (8, 1, 3, bow),
    catapult    (20, -2, 3, crossBow),

    // Mystical weapons
    excalibur           (36, -1, 1, true),
    mjolnir             (40, 0, 1, true),
    tridentOfPoseidon   (31, 1, 2, true),
    axeOfPerun          (37, 0, 1, true),
    anduril             (26, 2, 1, true),
    spearOfLugh         (28, 0, 2, true),
    harpe               (32, 0, 1, true),
    ;

    private final int damage;
    private final int speed;
    private final int range;
    private final Weapon requiredWeapon;
    private final boolean mystical;
    private final int price;
    private final int unluckPrice;

    Weapon(int damage, int speed, int range, Weapon requiredWeapon, boolean mystical) {
        this.damage = damage;
        this.speed = speed;
        this.range = range;
        this.requiredWeapon = requiredWeapon;
        this.price = 0; // todo
        this.unluckPrice = 0; // todo
        this.mystical = mystical;
    }

    Weapon(int damage, int speed, int range, Weapon requiredWeapon) {
        this(damage, speed, range, requiredWeapon, false);
    }

    Weapon(int damage, int speed, int range, boolean mystical) {
        this(damage, speed, range, null, mystical);
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
