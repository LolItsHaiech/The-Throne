package obj.unit.wizard.functional;

import obj.unit.Unit;

public enum Effect {

    healthBoost(0, 20, Unit::setHealth, Unit::getHealth),
    defenceBoost(2, 2, Unit::setDefenceModifier, Unit::getDefenceModifier),
    damageBoost(2, 10, Unit::setDamageModifier, Unit::getDamageModifier),
    speedBoost(2, 2, Unit::setSpeedModifier, Unit::getSpeedModifier)
    ;


    public final int DURATION;
    public final int BOOST;
    private final EffectSetter setter;
    private final EffectGetter getter;

    Effect(int duration, int boost, EffectSetter setter, EffectGetter getter) {
        this.DURATION = duration;
        this.BOOST = boost;
        this.setter = setter;
        this.getter = getter;
    }

    public void effectStart(Unit unit) {
        this.setter.setter(unit, this.getter.getter(unit) + this.BOOST);
    }

    public void effectEnd(Unit unit) {
        if (this.DURATION != 0)
            this.setter.setter(unit, this.getter.getter(unit) - this.BOOST);
    }
}
