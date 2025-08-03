package obj.game;

import db.interfaces.DBSerializable;
import db.Database;
import obj.Player;
import obj.Weapon;
import obj.auth.User;
import obj.building.Building;
import obj.building.Castle;
import obj.building.WizardTower;
import obj.building.interfaces.CollectorBuilding;
import obj.building.mystical.MysticalContainer;
import obj.map.Tile;
import obj.soldier.Soldier;
import obj.soldier.wizard.functional.Magic;
import util.LinkedList;
import util.OpenSimplex2S;
import util.Position;
import util.map.MapEntry;

import java.util.Objects;
import java.util.Random;


public abstract class Game implements DBSerializable {
    private static final Database<Game> DB = new Database<>("GAMES");

    private final int ID;
    private final String name;
    protected final User[] users;
    protected final Player[] players;
    protected Tile[][] map;
    private final int mapWidth;
    private final int mapHeight;
    private int turn;
    private int turnCount;
    private final long SEED;
    private static final double NOISE_FREQUENCY = 0.1;

    public Game(String name, User[] users, int mapWidth, int mapHeight) {
        this(name, users, mapWidth, mapHeight, new Random(System.nanoTime()).nextLong());
    }

    public Game(String name, User[] users, int mapWidth, int mapHeight, long seed) {
        synchronized (DB) {
            this.ID = DB.getNextID();
        }
        this.name = name;
        this.users = users;
        this.players = new Player[users.length];
        this.SEED = seed;
        this.turn = 0;
        this.map = null;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.turnCount = 0;
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

    public abstract Player getWinner();

    public Tile[][] generateMap() {
        this.map = new Tile[this.mapWidth][this.mapHeight];
        Random rand = new Random(this.SEED);

        // terrain gen
        for (int x = 0; x < mapWidth; x++) {
            // biome/feature gen
            for (int y = 0; y < mapHeight; y++) {
                double hotness = OpenSimplex2S.noise2(this.SEED, x * NOISE_FREQUENCY, y * NOISE_FREQUENCY);
                double humid = OpenSimplex2S.noise2(this.SEED * 2 + 1, x * NOISE_FREQUENCY, y * NOISE_FREQUENCY);
                double plantation = OpenSimplex2S.noise2(this.SEED * 3 + 2, x * NOISE_FREQUENCY, y * NOISE_FREQUENCY);
                double tileHeight = OpenSimplex2S.noise2(this.SEED * 4 + 3, x * NOISE_FREQUENCY, y * NOISE_FREQUENCY);

                Tile.Biome biome = getBiome(hotness, humid);
                Tile.Height height;
                boolean tree = (plantation > 0.4 || rand.nextDouble() < 0.02) &&
                        (biome != Tile.Biome.desert || rand.nextDouble() < 0.4);

                if (tileHeight > 0.25 && tileHeight < 0.55 || rand.nextDouble() < 0.02) {
                    height = Tile.Height.mountain;
                } else if (tileHeight > 0 || rand.nextDouble() < 0.02) {
                    height = Tile.Height.hill;
                } else {
                    height = Tile.Height.flat;
                }


                map[x][y] = new Tile(
                        biome,
                        height,
                        tree
                );
            }
        }

        // mythic gen
        for (MapEntry<String, Weapon> me : MysticalContainer.containers) {
            if (rand.nextDouble() < 0.15) {
                int x0, y0;
                do {
                    x0 = rand.nextInt(mapWidth);
                    y0 = rand.nextInt(mapHeight);
                } while (map[x0][y0].getBuilding() != null);
                map[x0][y0].setBuilding(
                        new MysticalContainer(me.getKey(), me.getValue(), new Position(x0, y0))
                );
                map[x0][y0].setTree(false);
                map[x0][y0].setHeight(Tile.Height.flat);
            }
        }

        for (Player player : this.players) {
            int x, y;
            do {
                x = rand.nextInt(mapWidth);
                y = rand.nextInt(mapHeight);
            } while (map[x][y].getBuilding() != null);

            Castle castle = new Castle(player, new Position(x, y));
            player.getCastles().addFirst(castle);
            map[x][y].setBuilding(castle);
            map[x][y].setTree(false);
            map[x][y].setHeight(Tile.Height.flat);
        }
        for (Magic magic : Magic.values()) {
            int x, y;
            do {
                x = rand.nextInt(mapWidth);
                y = rand.nextInt(mapHeight);
            } while (map[x][y].getBuilding() != null);
            map[x][y].setBuilding(new WizardTower(new Position(x, y), magic));
            map[x][y].setTree(false);
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
            } else if (humid <= -.333) {
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

    public Tile getTile(Position position) {
        return this.map[position.x()][position.y()];
    }

    public void nextTurn() {
        int newTurn = (this.turn + 1) % this.players.length;
        if (this.turn == 0) {
            this.turnCount++;
        }

        for (Tile[] tiles : this.map) {
            for (Tile tile : tiles) {
                Building building = tile.getBuilding();
                Soldier soldier = tile.getSoldier();
                if (building != null &&
                        Objects.equals(building.getOwner(), this.players[this.turn]) &&
                        building instanceof CollectorBuilding collectorBuilding) {
                    collectorBuilding.collect();
                }

                if (soldier != null) {
                    if (soldier.getPlayer() == this.players[this.turn]) {
                        soldier.onTurnEnd();
                    } else if (soldier.getPlayer() == this.players[newTurn]) {
                        soldier.onTurnStart();
                    }
                }
            }
        }

        this.turn = newTurn;
    }

    public Player GetActivePlayer() {
        return this.players[this.turn];
    }

    public int getTurnCount() {
        return this.turnCount;
    }

    public boolean isInBounds(Position pos) {
        return pos.x() >= 0 && pos.y() >= 0 && pos.x() < this.getMapWidth() && pos.y() < this.getMapHeight();
    }

    public Soldier getSoldierAt(Position pos) {
        for (Player player : players) {
            for (Soldier soldier : player.getSoldiers()) {
                if (soldier.getPos().equals(pos)) {
                    return soldier;
                }
            }
        }
        return null;
    }

    public boolean isGameStarted() {
        for (Player player : this.players) {
            if (player == null) {
                return false;
            }
        }
        return true;
    }


    @Override
    public int getID() {
        return this.ID;
    }

    @Override
    public void save() {
        synchronized (DB) {
            if (DB.exists(this)) {
                DB.update(this);
            } else {
                DB.write(this);
            }
        }
    }

    public static LinkedList<Game> getAllGames(User user) {
        LinkedList<Game> games = new LinkedList<>();
        synchronized (DB) {
            for (Game game : DB) {
                for (User u : game.users) {
                    if (u.equals(user)) {
                        games.addFirst(game);
                        break;
                    }
                }
            }
        }
        return games;
    }

    public void createPlayer(User user, Player player) {
        for (int i = 0; i < this.users.length; i++) {
            if (this.users[i].equals(user)) {
                this.players[i] = player;
            }
        }
        if (this.isGameStarted()) {
            this.generateMap();
            for (Player p : this.players) {
                Castle castle = p.getCastles().get(0);
                p.expandVision(castle.getPosition(), castle.getBorderRadius()+1);
            }
        }
        this.save();
    }

    public boolean userHasPlayer(User user) {
        for (Player player : this.players) {
            System.out.println(user);
            if (player!=null && player.getUser().equals(user)) {
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }
}
