package transactions;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import transactions.types.CastleDealItem;
import transactions.types.ItemDealItem;
import transactions.types.WeaponDealItem;

import java.util.List;

public class DealItem extends HBox {
    private ListView<DealItem> parent;
    private List<Transaction> transactionsList;
    private Label lblDealItem;
    private Label lblValue;
    private final Button btnRemove;

    public DealItem(ListView<DealItem> parent, List<Transaction> transactionsList, Transaction transaction) {
        super(10);
        create(parent, transaction);
        btnRemove = new Button("x");
        btnRemove.setOnAction(event -> {
            if (parent != null)
                parent.getItems().remove(this);
            for (int i = 0; i < transactionsList.size(); i++) {
                if (transactionsList.get(i) == transaction) {
                    transactionsList.remove(i);
                    break;
                }
            }
        });
        btnRemove.setStyle(
                "-fx-background-color: #861f1f; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 12; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 5;");
        setStyle(
                "-fx-background-color: #004d84; " +
                        "-fx-text-fill: black; " +
                        "-fx-font-size: 10; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 5;");
        getChildren().addAll(lblDealItem, lblValue, btnRemove);
        parent.getItems().add(this);
    }

    public DealItem(ListView<DealItem> parent, Transaction transaction) {
        super(10);
        create(parent, transaction);
        btnRemove = null;
        setStyle(
                "-fx-background-color: #004d84; " +
                        "-fx-text-fill: black; " +
                        "-fx-font-size: 10; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 5;");
        getChildren().addAll(lblDealItem, lblValue);
        parent.getItems().add(this);
    }

    private void create(ListView<DealItem> parent, Transaction transaction) {
        setAlignment(Pos.CENTER_LEFT);
        this.parent = parent;
        this.transactionsList = null;
        String name = "";
        String value = "";
        if (transaction instanceof ItemDealItem item) {
            name = item.getType().name();
            value = String.valueOf(item.getCount());
        }
        else if (transaction instanceof WeaponDealItem item) {
            name = item.getType().name();
            value = String.valueOf(item.getCount());
        }
        else if (transaction instanceof CastleDealItem item)
            name = "Castle";
        lblDealItem = new Label(name);
        lblValue = new Label(value);
    }
}
