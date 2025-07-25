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
import obj.auth.User;
import obj.game.CollectionGame;
import obj.game.DominationGame;
import obj.game.Game;
import obj.game.PerfectionGame;

public class CreateGameSubScene extends SubScene {
    private final User user;
    private VBox root;

    // Form fields
    private TextField gameNameField;
    private ComboBox<String> gameTypeCombo;
    private Spinner<Integer> playerCountSpinner;
    private Spinner<Integer> mapWidthSpinner;
    private Spinner<Integer> mapHeightSpinner;
    private Spinner<Integer> turnLimitSpinner;
    private Label turnLimitLabel;
    private Label messageLabel;

    public CreateGameSubScene(User user) {
        this.user = user;
        initializeUI();
    }

    private void initializeUI() {
        root = new VBox(20);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPrefSize(1280, 720);
        root.setPadding(new Insets(30));

        // Title
        Label titleLabel = new Label("CREATE NEW GAME");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        titleLabel.setTextFill(Color.GOLD);

        // Back button
        Button backButton = new Button("← BACK TO LOBBY");
        backButton.setPrefHeight(35);
        backButton.setPrefWidth(150);
        backButton.setStyle("-fx-background-color: #757575; " +
                                    "-fx-text-fill: white; " +
                                    "-fx-font-size: 12; " +
                                    "-fx-font-weight: bold; " +
                                    "-fx-background-radius: 5;");
        backButton.setOnAction(e -> handleBack());

        // Form container
        VBox formContainer = new VBox(20);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setMaxWidth(600);
        formContainer.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); " +
                                       "-fx-background-radius: 10; " +
                                       "-fx-padding: 40;");

        // Game Name Section
        VBox gameNameSection = createFormSection("Game Name", createGameNameField());

        // Game Type Section
        VBox gameTypeSection = createFormSection("Game Type", createGameTypeCombo());

        // Player Count Section
        VBox playerCountSection = createFormSection("Number of Players", createPlayerCountSpinner());

        // Map Size Section
        HBox mapSizeContainer = new HBox(15);
        mapSizeContainer.setAlignment(Pos.CENTER);
        VBox mapWidthSection = createFormSection("Map Width", createMapWidthSpinner());
        VBox mapHeightSection = createFormSection("Map Height", createMapHeightSpinner());
        mapSizeContainer.getChildren().addAll(mapWidthSection, mapHeightSection);
        VBox mapSizeSection = createFormSection("Map Size", mapSizeContainer);

        // Turn Limit Section (initially hidden)
        VBox turnLimitSection = createTurnLimitSection();

        // Message label
        messageLabel = new Label();
        messageLabel.setFont(Font.font("Arial", 12));
        messageLabel.setAlignment(Pos.CENTER);

        // Create Game Button
        Button createButton = new Button("CREATE GAME");
        createButton.setPrefHeight(50);
        createButton.setPrefWidth(200);
        createButton.setStyle("-fx-background-color: #4CAF50; " +
                                      "-fx-text-fill: white; " +
                                      "-fx-font-size: 16; " +
                                      "-fx-font-weight: bold; " +
                                      "-fx-background-radius: 5;");
        createButton.setOnAction(e -> handleCreateGame());

        formContainer.getChildren().addAll(
                gameNameSection,
                gameTypeSection,
                playerCountSection,
                mapSizeSection,
                turnLimitSection,
                messageLabel,
                createButton
        );

        // Header with back button
        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.getChildren().add(backButton);

        root.getChildren().addAll(headerBox, titleLabel, formContainer);
        getContentRoot().getChildren().add(root);
    }

    private VBox createFormSection(String labelText, Region field) {
        VBox section = new VBox(8);
        section.setAlignment(Pos.CENTER_LEFT);

        Label label = new Label(labelText);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        label.setTextFill(Color.GOLD);

        section.getChildren().addAll(label, field);
        return section;
    }

    private TextField createGameNameField() {
        gameNameField = new TextField();
        gameNameField.setPromptText("Enter game name...");
        gameNameField.setPrefHeight(40);
        gameNameField.setStyle("-fx-font-size: 14; -fx-pref-width: 300;");
        return gameNameField;
    }

    private ComboBox<String> createGameTypeCombo() {
        gameTypeCombo = new ComboBox<>();
        gameTypeCombo.getItems().addAll("Domination", "Collection", "Perfection");
        gameTypeCombo.setValue("Domination");
        gameTypeCombo.setPrefHeight(40);
        gameTypeCombo.setPrefWidth(300);
        gameTypeCombo.setStyle("-fx-font-size: 14;");

        // Add listener to show/hide turn limit based on game type
        gameTypeCombo.setOnAction(e -> updateTurnLimitVisibility());

        return gameTypeCombo;
    }

    private Spinner<Integer> createPlayerCountSpinner() {
        playerCountSpinner = new Spinner<>(2, 8, 2);
        playerCountSpinner.setPrefHeight(40);
        playerCountSpinner.setPrefWidth(300);
        playerCountSpinner.setStyle("-fx-font-size: 14;");
        playerCountSpinner.setEditable(true);
        return playerCountSpinner;
    }

    private Spinner<Integer> createMapWidthSpinner() {
        mapWidthSpinner = new Spinner<>(10, 100, 30);
        mapWidthSpinner.setPrefHeight(40);
        mapWidthSpinner.setPrefWidth(140);
        mapWidthSpinner.setStyle("-fx-font-size: 14;");
        mapWidthSpinner.setEditable(true);
        return mapWidthSpinner;
    }

    private Spinner<Integer> createMapHeightSpinner() {
        mapHeightSpinner = new Spinner<>(10, 100, 30);
        mapHeightSpinner.setPrefHeight(40);
        mapHeightSpinner.setPrefWidth(140);
        mapHeightSpinner.setStyle("-fx-font-size: 14;");
        mapHeightSpinner.setEditable(true);
        return mapHeightSpinner;
    }

    private VBox createTurnLimitSection() {
        VBox section = new VBox(8);
        section.setAlignment(Pos.CENTER_LEFT);

        turnLimitLabel = new Label("Turn Limit");
        turnLimitLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        turnLimitLabel.setTextFill(Color.GOLD);

        turnLimitSpinner = new Spinner<>(10, 1000, 50);
        turnLimitSpinner.setPrefHeight(40);
        turnLimitSpinner.setPrefWidth(300);
        turnLimitSpinner.setStyle("-fx-font-size: 14;");
        turnLimitSpinner.setEditable(true);

        section.getChildren().addAll(turnLimitLabel, turnLimitSpinner);

        // Initially hidden for Domination games
        section.setVisible(false);
        section.setManaged(false);

        return section;
    }

    private void updateTurnLimitVisibility() {
        String gameType = gameTypeCombo.getValue();
        boolean showTurnLimit = "Collection".equals(gameType) || "Perfection".equals(gameType);

        turnLimitLabel.getParent().setVisible(showTurnLimit);
        turnLimitLabel.getParent().setManaged(showTurnLimit);

        if ("Collection".equals(gameType)) {
            turnLimitLabel.setText("Turn Limit");
            turnLimitSpinner.getValueFactory().setValue(50);
        } else if ("Perfection".equals(gameType)) {
            turnLimitLabel.setText("Turn Limit");
            turnLimitSpinner.getValueFactory().setValue(100);
        }
    }

    private void handleBack() {
        ((TheThrone)FXGL.getApp()).showGamesMenu(this.user);
    }

    private void handleCreateGame() {
        // Validate inputs
        String gameName = gameNameField.getText().trim();
        if (gameName.isEmpty()) {
            showMessage("Please enter a game name", Color.RED);
            return;
        }

        String gameType = gameTypeCombo.getValue();
        int playerCount = playerCountSpinner.getValue();
        int mapWidth = mapWidthSpinner.getValue();
        int mapHeight = mapHeightSpinner.getValue();

        // Validate map size
        if (mapWidth < 10 || mapHeight < 10) {
            showMessage("Map dimensions must be at least 10x10", Color.RED);
            return;
        }

        if (mapWidth * mapHeight < playerCount * 20) {
            showMessage("Map too small for " + playerCount + " players. Minimum recommended: " +
                                (playerCount * 20) + " tiles", Color.ORANGE);
            return;
        }

        try {
            // Create users array (for now, just the creator)
            User[] users = new User[playerCount];
            users[0] = user; // Creator is always the first player
            // Other slots will be filled when players join
            for (int i = 1; i < playerCount; i++) {
                users[i] = null; // Will be filled when players join
            }

            // Create the appropriate game type
            Game game;
            switch (gameType) {
                case "Domination":
                    game = new DominationGame(gameName, users, mapWidth, mapHeight);
                    break;
                case "Collection":
                    int collectionTurnLimit = turnLimitSpinner.getValue();
                    game = new CollectionGame(gameName, users, mapWidth, mapHeight, collectionTurnLimit);
                    break;
                case "Perfection":
                    int perfectionTurnLimit = turnLimitSpinner.getValue();
                    game = new PerfectionGame(gameName, users, mapWidth, mapHeight, perfectionTurnLimit);
                    break;
                default:
                    showMessage("Invalid game type selected", Color.RED);
                    return;
            }

            // Save the game
            game.save();

            ((TheThrone)FXGL.getApp()).showGamesMenu(this.user);
        } catch (Exception e) {
            showMessage("Error creating game: " + e.getMessage(), Color.RED);
            e.printStackTrace();
        }
    }

    private void showMessage(String message, Color color) {
        messageLabel.setText(message);
        messageLabel.setTextFill(color);
    }
}