package obj.building;

import obj.Player;
import obj.Weapon;
import obj.building.interfaces.TrainerBuilding;
import obj.soldier.Rider;
import obj.soldier.Soldier;
import obj.soldier.Warrior;
import util.LinkedList;
import util.MaterialCost;
import util.Position;
import util.map.Map;

public class Barrack extends TrainerBuilding {

    public Barrack(Player owner, Position position) {
        super(owner, position);
    }

    @Override
    public Map<String, SoldierFactory> getAllowedSoldiers() {
        Map<String, SoldierFactory> allowedSoldiers = new Map<>();
        allowedSoldiers.addFirst("Warrior", Warrior::new);
        allowedSoldiers.addFirst("Rider", Rider::new);
        return allowedSoldiers;
    }


    @Override
    public MaterialCost getBuildingPrice() {
        return new MaterialCost(10, 5, 5, 5, 0);
    }
}
