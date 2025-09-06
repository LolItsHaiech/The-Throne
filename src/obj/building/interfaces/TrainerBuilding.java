package obj.building.interfaces;

import obj.Player;
import obj.Weapon;
import obj.building.Building;
import obj.map.Tile;
import obj.soldier.Soldier;
import transactions.exceptions.ItemDoesntExistException;
import util.LinkedList;
import util.Position;
import util.map.Map;

public abstract class TrainerBuilding extends Building {
    public TrainerBuilding(Player owner, Position position) {
        super(owner, position);
    }

    public abstract Map<String, SoldierFactory> getAllowedSoldiers();

    public void train(int index, Weapon weapon) {
        Tile tile = this.owner.getGame().getMap()[position.x()][position.y()];
        try {
            this.owner.removeWeapon(weapon);
        } catch (ItemDoesntExistException e) {
            System.out.println("dard o nefrin");
        }
        tile.setSoldier(this.getAllowedSoldiers().get(index).getValue().create(weapon, this.owner, this.position));
    }

    @FunctionalInterface
    public interface SoldierFactory {
        Soldier create(Weapon weapon, Player player, Position position);
    }
}
