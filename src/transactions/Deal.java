package transactions;

import obj.Player;
import transactions.exceptions.ItemDoesntExistException;
import transactions.types.CastleDealItem;
import transactions.types.ItemDealItem;
import transactions.types.WeaponDealItem;
import util.LinkedList;

public class Deal {
    private final LinkedList<Transaction> offers, demands;
    private final Player dealer1, dealer2;
    private boolean inProgress;

    private static class DealException extends Exception {
        public DealException(String message) {
            super(message);
        }
    }

    public Deal(Player dealer1, Player dealer2) {
        this.dealer1 = dealer1;
        this.dealer2 = dealer2;
        this.offers = new LinkedList<Transaction>();
        this.demands = new LinkedList<Transaction>();
        this.inProgress = true;
    }

    public void offer(Transaction offer, Player dealer1) {
        try {
            if (!this.inProgress)
                throw new DealException("Deal has ended");
            if (this.dealer1 != dealer1)
                throw new DealException("This dealer is wrong");
            this.offers.addLast(offer);
        } catch (DealException e) {
            System.out.println(e.getMessage());
        }
    }

    public void demand(Transaction demand, Player dealer2) {
        try {
            if (!this.inProgress)
                throw new DealException("Deal has ended");
            if (this.dealer2 != dealer2)
                throw new DealException("This dealer is wrong");
            this.demands.addLast(demand);
        } catch (DealException e) {
            System.out.println(e.getMessage());
        }
    }

    public Transaction getTransaction(int place, Player dealer) {
        LinkedList<Transaction> list;
        Transaction transaction;
        try {
            if (this.inProgress) {
                if (dealer == this.dealer1)
                    list = offers;
                else if (dealer == this.dealer2)
                    list = demands;
                else
                    throw new DealException("Dealer does not exist");
                transaction = list.get(place);
                if (transaction == null)
                    throw new DealException("This transaction does not exist");
                return transaction;
            } else
                throw new DealException("Deal has ended");
        } catch (DealException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void removeOffer(int place, Player dealer) {
        LinkedList<Transaction> list;
        try {
            if (this.inProgress) {
                if (dealer == this.dealer1)
                    list = offers;
                else if (dealer == this.dealer2)
                    list = demands;
                else
                    throw new DealException("Dealer does not exist");
                list.remove(place);
            } else
                throw new DealException("Deal has ended");
        } catch (DealException e) {
            System.out.println(e.getMessage());
        }
    }

    public Player getDealer1() {
        return dealer1;
    }

    public Player getDealer2() {
        return dealer2;
    }

    public void acceptDeal(Player dealer2) {
        try {
            if (!this.inProgress)
                throw new DealException("Deal has ended");
            if (dealer2 != this.dealer2)
                throw new DealException("This dealer is wrong");
            this.inProgress = false;
            if (!this.handingOver(offers, this.dealer1))
                throw new DealException("Mistake from dealer1");
            if (!this.handingOver(demands, this.dealer2))
                throw new DealException("Mistake from dealer2");
            this.receive(offers, this.dealer1, this.dealer2);
            this.receive(demands, this.dealer2, this.dealer1);
        } catch (DealException e) {
            System.out.println(e.getMessage());
        }
    }

    public void rejectDeal(Player dealer2) {
        try {
            if (!this.inProgress)
                throw new DealException("Deal has ended");
            if (dealer2 != this.dealer2)
                throw new DealException("This dealer is wrong");
            this.inProgress = false;
        } catch (DealException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean checkInventory(LinkedList<Transaction> list, Player dealer) {
        Transaction transaction;
        for (int i = 0; i < list.size(); i++) {
            transaction = list.get(i);
            if (transaction instanceof ItemDealItem item) {
                switch (item.getType()) {
                    case wealth:
                        if (dealer.getWealth() < item.getCount())
                            return false;
                    case food:
                        if (dealer.getFoodCount() < item.getCount())
                            return false;
                    case stone:
                        if (dealer.getStoneCount() < item.getCount())
                            return false;
                    case iron:
                        if (dealer.getIronCount() < item.getCount())
                            return false;
                    case wood:
                        if (dealer.getWoodCount() < item.getCount())
                            return false;
                }
            } else if (transaction instanceof WeaponDealItem weapon) {
                if (dealer1.getWeapons().get(weapon.getType()) < weapon.getCount())
                    return false;
            }
        }
        return true;
    }

    private boolean handingOver(LinkedList<Transaction> list, Player sender) {
        try {
            if (!this.checkInventory(list, sender))
                throw new DealException("Invalid resources");
        } catch (DealException e) {
            System.out.println(e.getMessage());
            return false;
        }
        for (int i = 0; i < list.size(); i++) {
            Transaction transaction = list.get(i);
            if (transaction instanceof ItemDealItem item) {
                switch (item.getType()) {
                    case wealth -> sender.spendWealth(item.getCount());
                    case food -> sender.spendFood(item.getCount());
                    case stone -> sender.spendStone(item.getCount());
                    case iron -> sender.spendIron(item.getCount());
                    case wood -> sender.spendWood(item.getCount());
                }
            } else if (transaction instanceof WeaponDealItem weapon) {
                try {
                    sender.removeWeapon(weapon.getType(), weapon.getCount());
                } catch (ItemDoesntExistException e) {
                    System.out.println("Item doesnt exist");
                }
            }
        }
        return true;
    }

    private void receive(LinkedList<Transaction> offer, Player sender, Player receiver) {
        Transaction transaction;
        for (int i = 0; i < offer.size(); i++) {
            transaction = offer.get(i);
            if (transaction instanceof ItemDealItem item) {
                switch (item.getType()) {
                    case wealth -> receiver.increaseWealth(item.getCount());
                    case food -> receiver.increaseFood(item.getCount());
                    case stone -> receiver.increaseStone(item.getCount());
                    case iron -> receiver.increaseIron(item.getCount());
                    case wood -> receiver.increaseWood(item.getCount());
                }
            } else if (transaction instanceof WeaponDealItem weapon) {
                receiver.addWeapon(weapon.getType(), weapon.getCount());
            } else if (transaction instanceof CastleDealItem castle) {
                try {
                    if (castle.getCastle().getOwner() == sender)
                        castle.getCastle().capture(receiver);
                    else
                        throw new DealException("This castle has a new owner");
                } catch (DealException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
