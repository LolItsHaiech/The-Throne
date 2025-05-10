package obj.building;

import obj.Player;

public abstract class Building {
    protected Player owner;

    public Building(Player owner) {
        this.owner = owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
}
