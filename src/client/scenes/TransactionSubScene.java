package client.scenes;

import client.TheThrone;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.SubScene;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import obj.Player;
import obj.Weapon;
import obj.game.Game;
import transactions.Deal;
import transactions.DealItem;
import transactions.Transaction;
import transactions.types.CastleDealItem;
import transactions.types.ItemDealItem;
import transactions.types.ItemTypesDealItem;
import transactions.types.WeaponDealItem;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class TransactionSubScene extends SubScene {
    /**/private final Pane root;
    /*  |_*/private final HBox topBar;
    /*  |   |_*/private final Button exitButton;
    /*  |_*/private final HBox mainPanels;
    /*  |   |*/
    /*  |   |_*/private final VBox leftPanel;
    /*  |   |   |_*/private final Label offersLabel;
    /*  |   |   |_*/private final ListView<DealItem> offersListView;
    /*  |   |   |_*/private final Button addOfferButton;
    /*  |   |*/
    /*  |   |_*/private final VBox rightPanel;
    /*  |       |_*/private final HBox demandsTitle;
    /*  |       |   |_*/private final ComboBox<String> otherPlayersComboBox;
    /*  |       |   |_*/private final Label demandsLabel;
    /*  |       |_*/private final ListView<DealItem> demandsListView;
    /*  |       |_*/private final Button addDemandButton;
    /*  |*/
    /*  |_*/private final Button sendTradeButton;

    private Deal deal;
    private final Player sender;
    private final Player[] otherPlayers;
    private final List<Transaction> offers;
    private final List<Transaction> demands;
    private final Game game;

    public TransactionSubScene(Player sender, Game game) {

        // --- Setup Scene ---
        super();
        this.root = this.getContentRoot();
        /**/this.game = game;
        /**/this.offers = new ArrayList<Transaction>();
        /**/this.demands = new ArrayList<Transaction>();
        /**/this.otherPlayers = game.getPlayers();
        /**/this.sender = sender;
        /**/root.setPrefSize(1280, 720);
        /**/root.setPadding(new Insets(20));
        /**/root.setStyle("-fx-background-color: #151e2e; ");

        // --- Setup Root ---
        topBar = new HBox(20);
        /**/topBar.setAlignment(Pos.TOP_RIGHT);
        mainPanels = new HBox(20);
        /**/mainPanels.setAlignment(Pos.CENTER);
        /**/mainPanels.setStyle("-fx-background-color: #354b6f; ");
        sendTradeButton = new Button("Send Offer");
        /**/sendTradeButton.setStyle(
                "-fx-background-color: #3cd8a9; " +
                "-fx-text-fill: black; " +
                "-fx-font-size: 12; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 5;");
        /**/sendTradeButton.setOnAction(event -> {
            sendDeal();
            handleExit();
        });
        this.root.getChildren().addAll(topBar, mainPanels, sendTradeButton);

        // --- Setup Top Bar ---
        exitButton = new Button("x");
        /**/exitButton.setFont(Font.font("Arial", 16));
        /**/exitButton.setStyle(
                "-fx-background-color: #861f1f; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 12; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 5;");
        /**/exitButton.setOnAction(event -> handleExit());
        topBar.getChildren().add(exitButton);

        // --- Setup Main Panel ---
        leftPanel = new VBox(10);
        /**/leftPanel.setAlignment(Pos.TOP_CENTER);
        rightPanel = new VBox(10);
        /**/rightPanel.setAlignment(Pos.TOP_CENTER);
        mainPanels.getChildren().addAll(leftPanel, rightPanel);

        // --- Setup Left Panel: Offers ---
        offersLabel = new Label("Your Offers");
        offersListView = new ListView<>();
        /**/offersListView.setPrefSize(200, 300);
        addOfferButton = new Button("Add Offer");
        /**/addOfferButton.setOnAction(event -> addOfferItem());
        /**/addOfferButton.setStyle(
                "-fx-background-color: #17f8f2; " +
                        "-fx-text-fill: black; " +
                        "-fx-font-size: 12; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 5;");
        leftPanel.getChildren().addAll(offersLabel, offersListView, addOfferButton);

        // --- Setup Right Panel: Demands ---
        demandsTitle = new HBox(20);
        /**/demandsTitle.setAlignment(Pos.CENTER);
        demandsListView = new ListView<>();
        /**/demandsListView.setPrefSize(200, 300);
        addDemandButton = new Button("Add Demand");
        /**/addDemandButton.setOnAction(event -> addDemandItem());
        /**/addDemandButton.setStyle(
                "-fx-background-color: #17f8f2; " +
                        "-fx-text-fill: black; " +
                        "-fx-font-size: 12; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 5;");
        rightPanel.getChildren().addAll(demandsTitle, demandsListView, addDemandButton);

        // --- Setup Demands Title ---
        demandsLabel = new Label("Your Demands From ");
        /**/otherPlayersComboBox = new ComboBox<>();
        /**/for (Player player : otherPlayers) {
        /**/        String name = player.getPlayerName();
        /**/if (name != sender.getPlayerName())
        /**/    otherPlayersComboBox.getItems().add(name);
        /**/}
        otherPlayersComboBox.setPromptText("Select Player");
        demandsTitle.getChildren().addAll(demandsLabel, otherPlayersComboBox);
    }

    private void addOfferItem() {
        chooseItem(sender);
    }

    private void addDemandItem() {
        if (otherPlayersComboBox.getItems().isEmpty())
            new Alert(Alert.AlertType.ERROR, "Please Select A Player.").showAndWait();
        else {
            Player receiver = null;
            String name = otherPlayersComboBox.getValue();
            for (Player p: otherPlayers) {
                if (p.getPlayerName().equals(name)) {
                    receiver = p;
                    break;
                }
            }
            chooseItem(receiver);
        }
    }

    private void chooseItem(Player player) {
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Choose Item");
        VBox root = new VBox(10);
        root.setAlignment(Pos.TOP_RIGHT);
        Button closeBtn = new Button("x");
        closeBtn.setOnAction(event -> modalStage.close());
        closeBtn.setStyle(
                "-fx-background-color: #861f1f; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 12; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 5;");

        TabPane itemsTab = new TabPane();
        root.getChildren().addAll(closeBtn, itemsTab);

        Tab tabResources = new Tab("Resources");
        Tab tabWeapons = new Tab("Weapons");

        createResourcesTab(tabResources, modalStage, player);
        createWeaponsTab(tabWeapons, modalStage, player);

        itemsTab.getTabs().addAll(tabResources, tabWeapons);

        Scene modalScene = new Scene(root, 300, 200);
        modalStage.setScene(modalScene);
        modalStage.showAndWait();
    }

    private TextField NumTextField() {
        TextField numberField = new TextField();
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        };
        numberField.setTextFormatter(new TextFormatter<>(filter));
        return numberField;
    }

    private void createResourcesTab(Tab tab, Stage stage, Player player) {
        ComboBox<String> comboBox = createTab(tab, stage, player, "Resources");
        for (ItemTypesDealItem item: ItemTypesDealItem.values()) {
            List<Transaction> list = player == sender ? offers : demands;
            boolean exists = false;
            for (Transaction transaction : list) {
                if (transaction instanceof ItemDealItem resource && resource.getType() == item) {
                    exists = true;
                    break;
                }
            }
            if (!exists)
                comboBox.getItems().add(item.name());
        }
    }

    private void createWeaponsTab(Tab tab, Stage stage, Player player) {
        ComboBox<String> comboBox = createTab(tab, stage, player, "Weapons");
        for (Weapon w: Weapon.values()) {
            List<Transaction> list = player == sender ? offers : demands;
            boolean exists = false;
            for (Transaction transaction : list) {
                if (transaction instanceof WeaponDealItem weapon && weapon.getType() == w) {
                    exists = true;
                    break;
                }
            }
            if (!exists)
                comboBox.getItems().add(w.name());
        }
    }

    private ComboBox<String> createTab(Tab tab, Stage stage, Player player, String subject) {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setStyle(
                "-fx-background-color: #21a25c; " +
                        "-fx-text-fill: black; " +
                        "-fx-font-size: 12; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 5;");
        ComboBox<String> comboBox = new ComboBox<>();
        TextField numField = NumTextField();
        Button btnAdd = new Button("Add " + subject);
        btnAdd.setStyle("-fx-background-color: #16621c; ");
        btnAdd.setOnAction(actionEvent -> {
            String txt = numField.getText();
            if (txt == null && txt.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Please Enter A Valid Number.").showAndWait();
            } else if (comboBox.getValue() == null) {
                new Alert(Alert.AlertType.ERROR, "Please Select A " + subject + ".").showAndWait();
            } else {
                Transaction t = null;
                String value  = comboBox.getValue();
                if (subject.equals("Resources")) {
                    for (ItemTypesDealItem item : ItemTypesDealItem.values()) {
                        if (item.name().equals(value)) {
                            t = new ItemDealItem(Integer.parseInt(txt), item);
                            break;
                        }
                    }
                } else if (subject.equals("Weapons")) {
                    for (Weapon w : Weapon.values()) {
                        if (w.name().equals(value)) {
                            t = new WeaponDealItem(Integer.parseInt(txt), w);
                            break;
                        }
                    }
                }
                if (t == null) {
                    DealItem d = new DealItem(player == sender ? offersListView : demandsListView,
                            player == sender ? offers : demands, t);
                }
                stage.close();
            }
        });
        box.getChildren().addAll(comboBox, numField, btnAdd);
        tab.setContent(box);
        return comboBox;
    }

    private void sendDeal() {
        String name = otherPlayersComboBox.getValue();
        int i = 0;
        while (i < otherPlayers.length) {
            if (otherPlayers[i].getPlayerName().equals(name) && sender.getPlayerName() != name)
                break;
            i++;
        }
        Player selectedPlayer = otherPlayers[i];
        if (selectedPlayer == null) {
            FXGL.getNotificationService().pushNotification("You have not selected a player to deal!");
            return;
        }
        deal = new Deal(sender, selectedPlayer);
        for (Transaction t: offers)
            deal.offer(t, sender);
        for (Transaction t: demands)
            deal.offer(t, selectedPlayer);
        selectedPlayer.getProposedDeals().addLast(deal);
        FXGL.getNotificationService().pushNotification("Deal Send To " + selectedPlayer.getPlayerName());
        FXGL.getSceneService().popSubScene();
    }

    private void handleExit() {
        ((TheThrone) FXGL.getApp()).startGame(this.game, this.sender.getUser());
    }
}
