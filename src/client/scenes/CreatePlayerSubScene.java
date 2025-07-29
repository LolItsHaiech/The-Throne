package client.scenes;

import client.TheThrone;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.SubScene;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import obj.Player;
import obj.Tribe;
import obj.auth.User;
import obj.game.Game;

public class CreatePlayerSubScene extends SubScene {
    private final User user;
    private final Game game;
    private VBox root;
    // Form fields
    private TextField playerNameField;
    private Tribe selectedTribe = null;
    private Label messageLabel;
    private Button createPlayerButton;

    public CreatePlayerSubScene(User user, Game game) {
        this.user = user;
        this.game = game;
        initializeUI();
    }

    private void initializeUI() {
        root = new VBox(30);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPrefSize(1280, 720);
        root.setPadding(new Insets(30));

        Label titleLabel = new Label("CREATE YOUR CHARACTER");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        titleLabel.setTextFill(Color.GOLD);

        Button backButton = new Button("← BACK TO LOBBY");
        backButton.setPrefHeight(35);
        backButton.setPrefWidth(150);
        backButton.setStyle("-fx-background-color: #757575; " +
                                    "-fx-text-fill: white; " +
                                    "-fx-font-size: 12; " +
                                    "-fx-font-weight: bold; " +
                                    "-fx-background-radius: 5;");
        backButton.setOnAction(e -> handleBack());

        VBox formContainer = new VBox(30);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setMaxWidth(800);
        formContainer.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); " +
                                       "-fx-background-radius: 10; " +
                                       "-fx-padding: 40;");

        VBox nameSection = createPlayerNameSection();

        VBox tribeSection = createTribeSelectionSection();

        messageLabel = new Label();
        messageLabel.setFont(Font.font("Arial", 12));
        messageLabel.setAlignment(Pos.CENTER);

        createPlayerButton = new Button("CREATE PLAYER");
        createPlayerButton.setPrefHeight(50);
        createPlayerButton.setPrefWidth(200);
        createPlayerButton.setStyle("-fx-background-color: #4CAF50; " +
                                            "-fx-text-fill: white; " +
                                            "-fx-font-size: 16; " +
                                            "-fx-font-weight: bold; " +
                                            "-fx-background-radius: 5;");
        createPlayerButton.setOnAction(e -> handleCreatePlayer());
        createPlayerButton.setDisable(true); // Initially disabled

        formContainer.getChildren().addAll(
                nameSection,
                tribeSection,
                messageLabel,
                createPlayerButton
        );

        VBox scrollContent = new VBox();
        scrollContent.setAlignment(Pos.TOP_CENTER);
        scrollContent.setPadding(new Insets(20));
        scrollContent.getChildren().add(formContainer);

        ScrollPane scrollPane = new ScrollPane(scrollContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPrefSize(1280, 600); // Leave space for header
        scrollPane.setStyle("-fx-background-color: transparent; " +
                                    "-fx-background: transparent;");

        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.getChildren().add(backButton);

        VBox mainLayout = new VBox(20);
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.setPrefSize(1280, 720);
        mainLayout.setPadding(new Insets(30));
        mainLayout.getChildren().addAll(headerBox, titleLabel, scrollPane);

        getContentRoot().getChildren().add(mainLayout);
    }

    private VBox createPlayerNameSection() {
        VBox section = new VBox(15);
        section.setAlignment(Pos.CENTER);

        Label nameLabel = new Label("Player Name");
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        nameLabel.setTextFill(Color.GOLD);

        playerNameField = new TextField();
        playerNameField.setPromptText("Enter your character name...");
        playerNameField.setPrefHeight(45);
        playerNameField.setPrefWidth(400);
        playerNameField.setStyle("-fx-font-size: 16; " +
                                         "-fx-background-radius: 8; " +
                                         "-fx-padding: 10;");

        playerNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateCreateButtonState();
        });

        section.getChildren().addAll(nameLabel, playerNameField);
        return section;
    }

    private VBox createTribeSelectionSection() {
        VBox section = new VBox(20);
        section.setAlignment(Pos.CENTER);

        Label tribeLabel = new Label("Choose Your Tribe");
        tribeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        tribeLabel.setTextFill(Color.GOLD);

        GridPane tribeGrid = new GridPane();
        tribeGrid.setAlignment(Pos.CENTER);
        tribeGrid.setHgap(20);
        tribeGrid.setVgap(20);
        tribeGrid.setPadding(new Insets(20));

        Tribe[] tribes = Tribe.values();
        int row = 0, col = 0;

        for (Tribe tribe : tribes) {
            VBox tribeCard = createTribeCard(tribe);
            tribeGrid.add(tribeCard, col, row);

            col++;
            if (col >= 2) {
                col = 0;
                row++;
            }
        }

        section.getChildren().addAll(tribeLabel, tribeGrid);
        return section;
    }

    private VBox createTribeCard(Tribe tribe) {
        VBox card = new VBox(15);
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(300, 200);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); " +
                              "-fx-background-radius: 10; " +
                              "-fx-border-color: rgba(255, 255, 255, 0.3); " +
                              "-fx-border-radius: 10; " +
                              "-fx-border-width: 2; " +
                              "-fx-cursor: hand;");

        // Tribe name
        Label nameLabel = new Label(tribe.name().toUpperCase());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        nameLabel.setTextFill(Color.WHITE);

        VBox statsContainer = new VBox(8);
        statsContainer.setAlignment(Pos.CENTER_LEFT);

        Label hpLabel = createStatLabel("Health: " + tribe.getBaseHP());
        Label attackLabel = createStatLabel("Attack: " + tribe.getBaseAttack());
        Label defenseLabel = createStatLabel("Defense: " + tribe.getBaseDefence());
        Label speedLabel = createStatLabel("Speed: " + tribe.getBaseSpeed());

        statsContainer.getChildren().addAll(hpLabel, attackLabel, defenseLabel, speedLabel);

        card.getChildren().addAll(nameLabel, statsContainer);

        card.setOnMouseClicked(e -> selectTribe(tribe, card));

        card.setOnMouseEntered(e -> {
            if (selectedTribe != tribe) {
                card.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); " +
                                      "-fx-background-radius: 10; " +
                                      "-fx-border-color: rgba(255, 215, 0, 0.5); " +
                                      "-fx-border-radius: 10; " +
                                      "-fx-border-width: 2; " +
                                      "-fx-cursor: hand;");
            }
        });

        card.setOnMouseExited(e -> {
            if (selectedTribe != tribe) {
                card.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); " +
                                      "-fx-background-radius: 10; " +
                                      "-fx-border-color: rgba(255, 255, 255, 0.3); " +
                                      "-fx-border-radius: 10; " +
                                      "-fx-border-width: 2; " +
                                      "-fx-cursor: hand;");
            }
        });

        return card;
    }

    private Label createStatLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        label.setTextFill(Color.LIGHTGRAY);
        return label;
    }

    private void selectTribe(Tribe tribe, VBox selectedCard) {
        GridPane tribeGrid = (GridPane) selectedCard.getParent();
        for (javafx.scene.Node node : tribeGrid.getChildren()) {
            if (node instanceof VBox) {
                VBox card = (VBox) node;
                card.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); " +
                                      "-fx-background-radius: 10; " +
                                      "-fx-border-color: rgba(255, 255, 255, 0.3); " +
                                      "-fx-border-radius: 10; " +
                                      "-fx-border-width: 2; " +
                                      "-fx-cursor: hand;");
            }
        }

        // Highlight selected card
        selectedCard.setStyle("-fx-background-color: rgba(76, 175, 80, 0.3); " +
                                      "-fx-background-radius: 10; " +
                                      "-fx-border-color: #4CAF50; " +
                                      "-fx-border-radius: 10; " +
                                      "-fx-border-width: 3; " +
                                      "-fx-cursor: hand;");

        this.selectedTribe = tribe;
        updateCreateButtonState();
        clearMessage();
    }

    private void updateCreateButtonState() {
        boolean canCreate = !playerNameField.getText().trim().isEmpty() && selectedTribe != null;
        createPlayerButton.setDisable(!canCreate);
    }

    private void handleBack() {
        ((TheThrone) FXGL.getApp()).showGamesMenu(this.user);
    }

    private void handleCreatePlayer() {
        String playerName = playerNameField.getText().trim();

        if (playerName.isEmpty()) {
            showMessage("Please enter a player name", Color.RED);
            return;
        }

        if (selectedTribe == null) {
            showMessage("Please select a tribe", Color.RED);
            return;
        }

        try {
            Player player = new Player(user, playerName, selectedTribe, game);
            game.createPlayer(user, player);
            ((TheThrone) FXGL.getApp()).showGamesMenu(this.user);

        } catch (Exception e) {
            showMessage("Error creating player: " + e.getMessage(), Color.RED);
            e.printStackTrace();
        }
    }

    private void showMessage(String message, Color color) {
        messageLabel.setText(message);
        messageLabel.setTextFill(color);
    }

    private void clearMessage() {
        messageLabel.setText("");
    }
}