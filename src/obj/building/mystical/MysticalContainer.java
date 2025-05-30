package obj.building.mystical;

import obj.Weapon;
import obj.building.Building;
import util.MaterialCost;
import util.Position;
import util.map.Map;
import util.map.MapEntry;

public class MysticalContainer extends Building {
    public static Map<String, Weapon> containers = new Map<>();

    private final String name;
    private final Weapon weapon;

    static {
        MapEntry<String, Weapon>[] a = new MapEntry[]{
                new MapEntry<>("Atlantis remains", Weapon.tridentOfPoseidon),
                new MapEntry<>("Hall Of Kings", Weapon.anduril),
                new MapEntry<>("Oland", Weapon.mjolnir),
                new MapEntry<>("Sarkel", Weapon.axeOfPerun),
                new MapEntry<>("The Rift", Weapon.harpe),
                new MapEntry<>("The Stone", Weapon.excalibur),
                new MapEntry<>("The Well", Weapon.spearOfLugh),
        };
        for (MapEntry<String, Weapon> me : a) {
            containers.addFirst(me);
        }
    }

    public MysticalContainer(String name, Weapon weapon, Position position) {
        super(null, position);
        this.name = name;
        this.weapon = weapon;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public String getName() {
        return name;
    }

    public void setPos(Position position) {
        this.position = position;
    }

    @Override
    public MaterialCost getBuildingPrice() {
        return null;
    }
}
