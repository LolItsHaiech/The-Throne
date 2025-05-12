package obj.unit.wizard.functional;

import obj.unit.Unit;

@FunctionalInterface
public interface EffectSetter {
    void setter(Unit unit, int amount);
}
