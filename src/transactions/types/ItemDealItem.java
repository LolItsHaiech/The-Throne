package transactions.types;

import transactions.Transaction;

public class ItemDealItem implements Transaction {
    private final ItemTypesDealItem type;
    private int count;

    public ItemDealItem(int count, ItemTypesDealItem type) {
        this.type = type;
        this.count = count;
    }

    public ItemTypesDealItem getType() {
        return type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
