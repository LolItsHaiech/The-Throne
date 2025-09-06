package client.scenes;

import com.almasb.fxgl.scene.SubScene;
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
import util.LinkedList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class DealRoomSubScene extends SubScene {
    /**/private final Pane root;
    /*  |_*/private final HBox topBar;
    /*  |   |_*/private final Button exitButton;
    /*  |_*/private final HBox mainPanels;
    /*  |   |*/
    /*  |   |_*/private final VBox leftPanel;
    /*  |   |   |_*/private final Label offersLabel;
    /*  |   |   |_*/private final ListView<DealItem> offersListView;
    /*  |   |*/
    /*  |   |_*/private final VBox rightPanel;
    /*  |       |_*/private final Label demandsLabel;
    /*  |       |_*/private final ListView<DealItem> demandsListView;
    /*  |*/
    /*  |_*/private final HBox answerBtns;
    /*      |_*/private final Button btnAccept;
    /*      |_*/private final Button btnReject;

    private final Deal deal;
    private final Game game;
    private final Player player;

    public DealRoomSubScene(Deal deal, Game game, Player player) {

        // --- Setup Scene ---
        super();
        this.deal = deal;
        this.game = game;
        this.player = player;
        this.root = this.getContentRoot();
        /**/root.setPrefSize(1280, 720);
        /**/root.setPadding(new Insets(20));
        /**/root.setStyle("-fx-background-color: #151e2e; ");

        // --- Setup Root ---
        topBar = new HBox(20);
        /**/topBar.setAlignment(Pos.TOP_RIGHT);
        mainPanels = new HBox(20);
        /**/mainPanels.setAlignment(Pos.CENTER);
        /**/mainPanels.setStyle("-fx-background-color: #354b6f; ");
        answerBtns = new HBox(20);
        /**/answerBtns.setAlignment(Pos.CENTER_RIGHT);
        this.root.getChildren().addAll(topBar, mainPanels, answerBtns);

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

        // --- Setup Left Panel: Receives ---
        offersLabel = new Label("You Will Receive The Following Things:");
        offersListView = new ListView<>();
        /**/offersListView.setPrefSize(200, 300);
        leftPanel.getChildren().addAll(offersLabel, offersListView);

        // --- Setup Right Panel: Give ---
        demandsLabel = new Label( this.deal.getDealer1().getPlayerName() + " Will Take The Following Things From You:");
        demandsListView = new ListView<>();
        /**/demandsListView.setPrefSize(200, 300);
        rightPanel.getChildren().addAll(demandsLabel, demandsListView);

        // --- Setup Lists ---
        for (Transaction t: deal.getOffers())
            new DealItem(offersListView, t);
        for (Transaction t: deal.getDemands())
            new DealItem(demandsListView, t);

        // --- Setup Answers Buttons ---
        btnAccept = new Button("Accept");
        /**/btnAccept.setFont(Font.font("Arial", 16));
        /**/btnAccept.setStyle(
                "-fx-background-color: #16621c; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 12; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 5;");
        /**/btnAccept.setOnAction(event -> {
                this.deal.acceptDeal(player);
                LinkedList<Deal> dealList = player.getProposedDeals();
                dealList.remove(dealList.search(deal));
                handleExit();
            });
        btnReject = new Button("Reject");
        /**/btnReject.setFont(Font.font("Arial", 16));
        /**/btnReject.setStyle(
                "-fx-background-color: #861f1f; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 12; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 5;");
        /**/btnReject.setOnAction(event -> {
                this.deal.rejectDeal(player);
                LinkedList<Deal> dealList = player.getProposedDeals();
                dealList.remove(dealList.search(deal));
                handleExit();
            });
        this.answerBtns.getChildren().addAll(btnAccept, btnReject);
    }
    
    private void handleExit() {
        ((TheThrone) FXGL.getApp()).startGame(this.game, this.player.getUser());
    }
}
