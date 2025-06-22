package obj;

import java.io.Serializable;

public enum Tribe implements Serializable {
    human(20, 3, 3, 8),
    orc(22, 3, 2, 9),
    elf(17, 2, 4, 9),
    dwarf(20, 3, 2, 13);

    private final int baseHP;
    private final int baseDefence;
    private final int baseSpeed;
    private final int baseAttack;

    Tribe(int baseHP, int baseDefence, int baseSpeed, int baseAttack) {
        this.baseHP = baseHP;
        this.baseDefence = baseDefence;
        this.baseSpeed = baseSpeed;
        this.baseAttack = baseAttack;
    }

    public int getBaseAttack() {
        return baseAttack;
    }

    public int getBaseSpeed() {
        return baseSpeed;
    }

    public int getBaseDefence() {
        return baseDefence;
    }

    public int getBaseHP() {
        return baseHP;
    }
}
