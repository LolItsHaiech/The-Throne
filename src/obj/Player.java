package obj;

import obj.auth.User;
import obj.building.Castle;
import obj.game.Game;
import obj.soldier.Soldier;
import transactions.exceptions.ItemDoesntExistException;
import util.LinkedList;

import util.MaterialCost;
import util.Position;
import util.map.Map;

import java.io.Serializable;

import util.Set;

public class Player implements Serializable {
    private final User user;
    private Game game;
    private final String name;
    private final Tribe tribe;
    private final LinkedList<Castle> castles;
    private final LinkedList<Soldier> soldiers;
    private final Map<Weapon, Integer> weapons;
    private boolean havingTower;

    private int woodCount;
    private int stoneCount;
    private int ironCount;
    private int foodCount;
    private int wealth;
    private final boolean[] weaponUnlocks;

    private final boolean[] weaponUnlocks;
    private final boolean[][] vision;

    public Player(User user, String name, Tribe tribe, Game game) {
        this.user = user;
        this.game = game;
        this.tribe = tribe;
        this.woodCount = 0;
        this.stoneCount = 0;
        this.ironCount = 0;
        this.foodCount = 0;
        this.wealth = 0;
        this.vision = new boolean[game.getMapWidth()][game.getMapHeight()];
        this.name = name;
        this.castles = new LinkedList<>();
        this.soldiers = new LinkedList<>();
        this.weapons = new Map<>();
        this.weaponUnlocks = new boolean[Weapon.values().length];
        this.havingTower = false;
    }

    public Tribe getTribe() {
        return tribe;
    }

    public User getUser() {
        return user;
    }

    public int getWoodCount() {
        return woodCount;
    }

    public int getStoneCount() {
        return stoneCount;
    }

    public int getIronCount() {
        return ironCount;
    }

    public int getFoodCount() {
        return foodCount;
    }

    public int getWealth() {
        return wealth;
    }

    public boolean getHavingTower() { return havingTower; }

    public void setHavingTower(boolean havingTower) {
        this.havingTower = havingTower;
    }

    public boolean spendWood(int amount) {
        if (this.woodCount < amount) {
            return false;
        }
        this.woodCount -= amount;
        return true;
    }

    public boolean spendIron(int amount) {
        if (this.ironCount < amount) {
            return false;
        }
        this.ironCount -= amount;
        return true;
    }

    public boolean spendFood(int amount) {
        if (this.foodCount < amount) {
            return false;
        }
        this.foodCount -= amount;
        return true;
    }

    public boolean spendWealth(int amount) {
        if (this.wealth < amount) {
            return false;
        }
        this.wealth -= amount;
        return true;
    }

    public boolean spendStone(int amount) {
        if (this.stoneCount < amount) {
            return false;
        }
        this.stoneCount -= amount;
        return true;
    }

    public boolean spend(MaterialCost materialCost) {
        if (this.stoneCount < materialCost.stone() ||
                this.woodCount < materialCost.wood() ||
                this.wealth < materialCost.wealth() ||
                this.foodCount < materialCost.food() ||
                this.ironCount < materialCost.iron()) {
            return false;
        }
        this.stoneCount -= materialCost.stone();
        this.woodCount -= materialCost.wood();
        this.wealth -= materialCost.wealth();
        this.foodCount -= materialCost.food();
        this.ironCount -= materialCost.iron();
        return true;

    }

    public void increaseWood(int amount) {
        this.woodCount += amount;
    }

    public void increaseIron(int amount) {
        this.ironCount += amount;
    }

    public void increaseFood(int amount) {
        this.foodCount += amount;
    }

    public void increaseWealth(int amount) {
        this.wealth += amount;
    }

    public void increaseStone(int amount) {
        this.stoneCount += amount;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public LinkedList<Castle> getCastles() {
        return this.castles;
    }

    public LinkedList<Soldier> getSoldiers() {
        return this.soldiers;
    }

    public Map<Weapon, Integer> getWeapons() {
        return weapons;
    }

    public void addWeapon(Weapon weapon) {
        this.addWeapon(weapon, 1);
    }

    public void addWeapon(Weapon weapon, int count) {
        Integer last = this.weapons.get(weapon); // if null -> 0
        if (last == null) {
            last = 0;
        }
        this.weapons.set(weapon, last + count);
    }

    public void removeWeapon(Weapon weapon) throws ItemDoesntExistException {
        this.removeWeapon(weapon, 1);
    }

    public void removeWeapon(Weapon weapon, int count) throws ItemDoesntExistException {
        if (this.weapons.containsKey(weapon)) {
            int newCount = Math.max(0, this.weapons.get(weapon) - count);
            if (newCount == 0) {
                this.weapons.removeFromKey(weapon);
                return;
            }
            this.weapons.set(weapon, newCount);
            return;
        }
        throw new ItemDoesntExistException();
    }

    public Set<Position> getTerritory() {
        Set<Position> res = new Set<>();
        for (Castle castle : this.castles) {
            int r = castle.getBorderRadius();
            int minX = Math.max(castle.getPosition().x() - r, 0);
            int maxX = Math.min(castle.getPosition().x() + r, this.getGame().getMapWidth() - 1);
            int minY = Math.max(castle.getPosition().y() - r, 0);
            int maxY = Math.min(castle.getPosition().y() + r, this.getGame().getMapHeight() - 1);
            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    res.addFirst(new Position(x, y));
                }
            }
        }
        return res;
    }

    public boolean[][] getVision() {
        return vision;
    }

    public void expandVision(Position spot, int radius) {
        int minX = Math.max(0, spot.x() - radius);
        int maxX = Math.min(this.getGame().getMapWidth() - 1, spot.x() + radius);
        int minY = Math.max(0, spot.y() - radius);
        int maxY = Math.min(this.getGame().getMapHeight() - 1, spot.y() + radius);
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                this.vision[x][y] = true;
            }
        }
    }

    public boolean[] getWeaponUnlocks() {
        return weaponUnlocks;
    }

    public void unlockWeapon(int i) {
        this.weaponUnlocks[i] = true;
    }
}

