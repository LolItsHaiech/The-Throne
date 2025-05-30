package obj.building.interfaces.functional;

import obj.Player;
import obj.building.Building;
import util.Position;

@FunctionalInterface
public interface BuildingFactory {
    Building create(Player owner, Position position);
}
