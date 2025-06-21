package client.config.widgets;

public class BuildingWidget {
    private final String name;
    private int wood;
    private int food;
    private int stone;
    private int wealth;

    public BuildingWidget(String name) {
        this.name = name;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public int getWood() {
        return wood;
    }

    public void setWood(int wood) {
        this.wood = wood;
    }

    public int getFood() {
        return food;
    }

    public void setFood(int food) {
        this.food = food;
    }

    public int getStone() {
        return stone;
    }

    public void setStone(int stone) {
        this.stone = stone;
    }

    public int getWealth() {
        return wealth;
    }

    public void setWealth(int wealth) {
        this.wealth = wealth;
    }
}
