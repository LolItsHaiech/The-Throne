package obj.game;

import obj.Player;
import obj.Weapon;
import obj.building.mystical.MysticalContainer;
import obj.map.Tile;
import util.OpenSimplex2S;
import util.Position;
import util.map.MapEntry;

import java.util.Random;

public abstract class Game {
    protected final Player[] players;
    protected final Tile[][] map;
    protected int turn;
    private final long SEED;

    private static final double NOISE_FREQUENCY = 0.05;

    public Game(Player[] players, int mapWidth, int mapHeight) {
        this(players, mapWidth, mapHeight, System.nanoTime() * new Random().nextLong());
    }

    public Game(Player[] players, int mapWidth, int mapHeight, long seed) {
        this.players = players;
        this.map = this.generateMap(mapWidth, mapHeight);
        this.SEED = seed;
        this.turn = 0;
    }

    public Player[] getPlayers() {
        return this.players;
    }

    public Tile[][] getMap() {
        return this.map;
    }


    public long getSeed() {
        return this.SEED;
    }

    public abstract boolean isEnded();

    private Tile[][] generateMap(int mapWidth, int mapHeight) {
        Tile[][] map = new Tile[mapWidth][mapHeight];
        Random rand = new Random(this.SEED);

        // terrain gen

        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                double hotness = OpenSimplex2S.noise2(this.SEED, x * NOISE_FREQUENCY, y * NOISE_FREQUENCY);
                double humid = OpenSimplex2S.noise2(this.SEED + 1, x * NOISE_FREQUENCY, y * NOISE_FREQUENCY);
                double plantation = OpenSimplex2S.noise2(this.SEED + 2, x * NOISE_FREQUENCY, y * NOISE_FREQUENCY);
                double tileHeight = OpenSimplex2S.noise2(this.SEED + 3, x * NOISE_FREQUENCY, y * NOISE_FREQUENCY);

                map[x][y] = new Tile(
                        getBiome(hotness, humid),
                        plantation > 0.4 || rand.nextDouble() < 0.02,
                        tileHeight > 0.4 || rand.nextDouble() < 0.02
                );
            }
        }

        // mythic gen

        for (MapEntry<String, Weapon> me : MysticalContainer.containers) {
            if (rand.nextDouble() < 0.15) {
                int x0 = rand.nextInt();
                int y0 = rand.nextInt();
                map[x0][y0].setBuilding(
                        new MysticalContainer(me.getKey(), me.getValue(), new Position(x0, y0))
                );
            }
        }

        return map;
    }

    private static Tile.Biome getBiome(double hotness, double humid) {
        Tile.Biome biome;
        if (hotness >= .333) {
            if (humid >= .333) {
                biome = Tile.Biome.swamp;
            } else if (humid <= -.333) {
                biome = Tile.Biome.desert;
            } else {
                biome = Tile.Biome.savanna;
            }
        } else if (hotness <= -.333) {
            if (humid >= .333) {
                biome = Tile.Biome.taiga;
            } else if (humid <= .333) {
                biome = Tile.Biome.polarDesert;
            } else {
                biome = Tile.Biome.tundra;
            }
        } else {
            if (humid >= .333) {
                biome = Tile.Biome.bog;
            } else if (humid <= -.333) {
                biome = Tile.Biome.badlands;
            } else {
                biome = Tile.Biome.plains;
            }
        }
        return biome;
    }

    public Tile getTile(Position position){
        return this.map[position.x()][position.y()];
    }
}
