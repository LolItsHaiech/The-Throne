package obj.map;

import obj.building.Building;
import obj.soldier.Soldier;

public class Tile {
    private Building building;
    private Soldier soldier;
    private final Biome biome;
    private final boolean tree;
    private final Height height;

    public Tile(Biome biome, Height height, boolean tree) {
        this.biome = biome;
        this.tree = tree;
        this.height = height;
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

    public boolean hasTree() {
        return this.tree;
    }

    public Biome getBiome() {
        return biome;
    }

    public Height getHeight() {
        return this.height;
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
        public final int speedModifier;
        public final int defenceModifier;

        Biome(int speedModifier, int defenceModifier) {
            this.speedModifier = speedModifier;
            this.defenceModifier = defenceModifier;
        }
    }

    public enum Height {
        flat(0,0),
        hill(1, -1),
        mountain(2, -2),
        ;
        Height(int rangeModifier, int speedModifier) {
            this.rangeModifier = rangeModifier;
            this.speedModifier = speedModifier;
        }
        public final int rangeModifier;
        public final int speedModifier;
    }

    public int getSpeedModifier() {
        return this.biome.speedModifier + this.height.speedModifier + (this.tree?-1:0);
    }

    public int getRangeModifier() {
        return this.height.rangeModifier + (this.tree?-1:0);
    }

    public int getDefenceModifier() {
        return this.biome.defenceModifier;
    }
}
