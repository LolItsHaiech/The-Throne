package obj.building.mystical;

import obj.Weapon;
import obj.building.Building;
import util.Position;
import util.map.Map;
import util.map.MapEntry;

public class MysticalContainer extends Building {
    private Position position;
    public static Map<String, Weapon> containers = new Map<>();

    private final String name;
    private final Weapon weapon;

    static {
        MapEntry<String, Weapon>[] a = new MapEntry[]{
                new MapEntry<>("The Stone", Weapon.excalibur),
                new MapEntry<>("Oland", Weapon.mjolnir),
                new MapEntry<>("The Well", Weapon.spearOfLugh),
                new MapEntry<>("The Maelstrom", Weapon.tridentOfPoseidon),
                new MapEntry<>("Sarkel", Weapon.axeOfPerun),
                new MapEntry<>("Hall Of Kings", Weapon.anduril),
                new MapEntry<>("The Rift", Weapon.harpe)
        };
        for (MapEntry<String, Weapon> me : a) {
            containers.addFirst(me);
        }
    }

    public MysticalContainer(String name, Weapon weapon, Position position) {
        super(null);
        this.name = name;
        this.weapon = weapon;
        this.position = position;
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
}
