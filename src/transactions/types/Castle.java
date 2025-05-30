package transactions.types;

import transactions.Transaction;

public class Castle implements Transaction {
    private final obj.building.Castle castle;

    public Castle(obj.building.Castle castle) {
        this.castle = castle;
    }

    public obj.building.Castle getCastle() {
        return castle;
    }
}
