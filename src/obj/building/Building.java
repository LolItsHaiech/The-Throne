package obj.building;

import obj.Player;
import obj.building.interfaces.functional.BuildingFactory;
import obj.map.Tile;
import util.LinkedList;
import util.MaterialCost;
import util.Position;

import java.io.Serializable;

public abstract class Building implements Serializable {
    protected Player owner;
    protected Position position;

    public Building(Player owner, Position position) {
        this.owner = owner;
        this.position = position;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }

    public abstract MaterialCost getBuildingPrice();

    public static LinkedList<BuildingFactory> getAllowedBuildingsToBuild(Tile tile) {
        LinkedList<BuildingFactory> allowedBuildings = new LinkedList<>();
        if (tile.getBuilding()!=null) {
            return allowedBuildings;
        }
        if (tile.hasTree()) {
            if (tile.getHeight()!= Tile.Height.mountain){
                allowedBuildings.addFirst(LumberHut::new);
            }
        } else {
            allowedBuildings.addFirst(Barrack::new);
            if (tile.getHeight() == Tile.Height.flat) {
                allowedBuildings.addFirst(Castle::new);
                allowedBuildings.addFirst(Farmland::new);
            }
        }
        if (tile.getHeight()!= Tile.Height.flat) {
            allowedBuildings.addFirst(Mine::new);
        }
        return allowedBuildings;
    }

    public int getRangeModifier() {
        return 0;
    }

    public int getDefenceModifier() {
        return 0;
    }
}
