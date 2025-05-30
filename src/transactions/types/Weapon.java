package transactions.types;

import transactions.Transaction;

public class Weapon implements Transaction {
    private final obj.Weapon type;
    private int count;

    public Weapon(int count, obj.Weapon type) {
        this.type = type;
        this.count = count;
    }

    public obj.Weapon getType() {
        return type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
