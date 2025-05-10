package obj.map;

import obj.building.Building;
import obj.soldier.Soldier;

public class Tile {
    private Building building;
    private Soldier soldier;
    private final Biome biome;
    private final boolean tree;
    private final boolean mountain;

    public Tile(Biome biome, boolean tree, boolean mountain) {
        this.biome = biome;
        this.tree = tree;
        this.mountain = mountain;
        this.building = null;
        this.soldier = null;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public Soldier getSoldier() {
        return soldier;
    }

    public void setSoldier(Soldier soldier) {
        this.soldier = soldier;
    }

    public boolean hasMountain() {
        return this.mountain;
    }

    public boolean hasTree() {
        return this.tree;
    }

    public Biome getBiome() {
        return biome;
    }

    public enum Biome {
        desert(0, 0),
        badlands(0, 0),
        polarDesert(0, 0),
        savanna(0, 0),
        plains(0, 0),
        tundra(0, 0),
        swamp(0, 0),
        bog(0, 0),
        taiga(0, 0);
        public final int SPEED_MODIFIER;
        public final int DEFENCE_MODIFIER;

        Biome(int speedModifier, int defenceModifier) {
            this.SPEED_MODIFIER = speedModifier;
            this.DEFENCE_MODIFIER = defenceModifier;
        }
    }

    public int getSpeedModifier() {
        return this.biome.SPEED_MODIFIER + (this.mountain?-1:0) + (this.tree?-1:0);
    }

    public int getRangeModifier() {
        return (this.mountain?1:0) + (this.tree?-1:0);
    }

    public int getDefenceModifier() {
        return this.biome.DEFENCE_MODIFIER;
    }
}
