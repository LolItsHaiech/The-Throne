package obj.soldier.wizard.functional;

import obj.soldier.Soldier;

public enum Magic {

    healthBoost(0, 20, Soldier::setHealth, Soldier::getHealth),
    defenceBoost(2, 2, Soldier::setDefenceModifier, Soldier::getDefenceModifier),
    damageBoost(2, 10, Soldier::setDamageModifier, Soldier::getDamageModifier),
    speedBoost(2, 2, Soldier::setSpeedModifier, Soldier::getSpeedModifier),
    ;


    public final int DURATION;
    public final int BOOST;
    private final EffectSetter setter;
    private final EffectGetter getter;

    Magic(int duration, int boost, EffectSetter setter, EffectGetter getter) {
        this.DURATION = duration;
        this.BOOST = boost;
        this.setter = setter;
        this.getter = getter;
    }

    public void effectStart(Soldier soldier) {
        this.setter.setter(soldier, this.getter.getter(soldier) + this.BOOST);
    }

    public void effectEnd(Soldier soldier) {
        if (this.DURATION != 0)
            this.setter.setter(soldier, this.getter.getter(soldier) - this.BOOST);
    }
}
