package obj;

import obj.auth.User;
import obj.building.Castle;
import obj.game.Game;
import obj.soldier.Soldier;
import transactions.exceptions.ItemDoesntExistException;
import util.LinkedList;
import util.map.Map;

public class Player {
    private final User user;
    private final Game game;
    private final Tribe tribe;
    private final LinkedList<Castle> castles;
    private final LinkedList<Soldier> soldiers;
    private final Map<Weapon, Integer> weapons;

    private int woodCount;
    private int stoneCount;
    private int ironCount;
    private int foodCount;
    private int wealth;

    private final boolean[][] vision;

    public Player(User user, Game game, Tribe tribe, int mapWidth, int mapHeight) {
        this.user = user;
        this.game = game;
        this.tribe = tribe;
        this.woodCount = 0;
        this.stoneCount = 0;
        this.ironCount = 0;
        this.foodCount = 0;
        this.wealth = 0;
        this.vision = new boolean[mapWidth][mapHeight];
        this.castles = new LinkedList<>();
        this.soldiers = new LinkedList<>();
        this.weapons = new Map<>();
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
        this.addWeapon(weapon,1);
    }

    public void addWeapon(Weapon weapon, int count) {
        if (this.weapons.exsits(weapon)) {
            this.weapons.set(
                    weapon,
                    this.weapons.get(weapon) + count
            );
            return;
        }
        this.weapons.addFirst(weapon, count);
    }

    public void removeWeapon(Weapon weapon) throws ItemDoesntExistException {
        this.removeWeapon(weapon, 1);
    }

    public void removeWeapon(Weapon weapon, int count) throws ItemDoesntExistException {
        if (this.weapons.exsits(weapon)) {
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

    public boolean[][] getVision() {
        return vision;
    }
    
}
