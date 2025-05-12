package obj.soldier.wizard.functional;

import obj.soldier.Soldier;

@FunctionalInterface
public interface EffectSetter {
    void setter(Soldier soldier, int amount);
}
