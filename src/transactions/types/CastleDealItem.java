package transactions.types;

import transactions.Transaction;

public class CastleDealItem implements Transaction {
    private final obj.building.Castle castle;

    public CastleDealItem(obj.building.Castle castle) {
        this.castle = castle;
    }

    public obj.building.Castle getCastle() {
        return castle;
    }
}
