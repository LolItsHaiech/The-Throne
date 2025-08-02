package client.scenes;

import client.TheThrone;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.SubScene;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import obj.Player;
import obj.auth.User;
import obj.game.Game;
import util.LinkedList;

public class GamesMenuSubScene extends SubScene {
    private final User user;
    private VBox root;
    private ScrollPane scrollPane;
    private VBox gamesContainer;

    public GamesMenuSubScene(User user) {
        this.user = user;
        initializeUI();
        loadGames();
    }

    private void initializeUI() {
        root = new VBox(20);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPrefSize(1280, 720);
        root.setPadding(new Insets(30));

        Label titleLabel = new Label("GAME LOBBY");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        titleLabel.setTextFill(Color.GOLD);

        Label welcomeLabel = new Label("Welcome back, " + user.getDisplayName() + "!");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        welcomeLabel.setTextFill(Color.WHITE);

        Button createGameButton = new Button("CREATE NEW GAME");
        createGameButton.setPrefHeight(50);
        createGameButton.setPrefWidth(200);
        createGameButton.setStyle("-fx-background-color: #4CAF50; " +
                                          "-fx-text-fill: white; " +
                                          "-fx-font-size: 16; " +
                                          "-fx-font-weight: bold; " +
                                          "-fx-background-radius: 5;");
        createGameButton.setOnAction(e -> handleCreateNewGame());

        // Games list container
        VBox gamesListContainer = new VBox(10);
        gamesListContainer.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); " +
                                            "-fx-background-radius: 10; " +
                                            "-fx-padding: 20;");
        gamesListContainer.setMaxWidth(1000);
        gamesListContainer.setPrefHeight(500);

        // Header for games table
        HBox headerBox = createTableHeader();

        // Scrollable games container
        gamesContainer = new VBox(5);
        scrollPane = new ScrollPane(gamesContainer);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        gamesListContainer.getChildren().addAll(headerBox, scrollPane);

        root.getChildren().addAll(titleLabel, welcomeLabel, createGameButton, gamesListContainer);
        getContentRoot().getChildren().add(root);
    }

    private HBox createTableHeader() {
        HBox headerBox = new HBox(10);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(10));
        headerBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); " +
                                   "-fx-background-radius: 5;");

        Label gameNameHeader = new Label("GAME NAME");
        gameNameHeader.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        gameNameHeader.setTextFill(Color.GOLD);
        gameNameHeader.setPrefWidth(150);

        Label statusHeader = new Label("STATUS");
        statusHeader.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        statusHeader.setTextFill(Color.GOLD);
        statusHeader.setPrefWidth(200);

        Label playersHeader = new Label("PLAYERS");
        playersHeader.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        playersHeader.setTextFill(Color.GOLD);
        playersHeader.setPrefWidth(120);

        Label turnHeader = new Label("TURN");
        turnHeader.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        turnHeader.setTextFill(Color.GOLD);
        turnHeader.setPrefWidth(80);

        Label mapSizeHeader = new Label("MAP SIZE");
        mapSizeHeader.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        mapSizeHeader.setTextFill(Color.GOLD);
        mapSizeHeader.setPrefWidth(100);

        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        headerBox.getChildren().addAll(gameNameHeader, statusHeader, playersHeader,
                                       turnHeader, mapSizeHeader, spacer);
        return headerBox;
    }

    private void loadGames() {
        gamesContainer.getChildren().clear();
        LinkedList<Game> games = Game.getAllGames(user);

        if (games.isEmpty()) {
            Label noGamesLabel = new Label("No games found. Create a new game to get started!");
            noGamesLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
            noGamesLabel.setTextFill(Color.LIGHTGRAY);
            noGamesLabel.setAlignment(Pos.CENTER);
            gamesContainer.getChildren().add(noGamesLabel);
        } else {
            for (Game game : games) {
                HBox gameRow = createGameRow(game);
                gamesContainer.getChildren().add(gameRow);
            }
        }
    }

    private HBox createGameRow(Game game) {
        HBox gameRow = new HBox(10);
        gameRow.setAlignment(Pos.CENTER_LEFT);
        gameRow.setPadding(new Insets(10));
        gameRow.setStyle("-fx-background-color: rgba(255, 255, 255, 0.05); " +
                                 "-fx-background-radius: 5; " +
                                 "-fx-border-color: rgba(255, 255, 255, 0.1); " +
                                 "-fx-border-radius: 5;");

        // Game name
        Label gameNameLabel = new Label(game.getName());
        gameNameLabel.setPrefHeight(35);
        gameNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        gameNameLabel.setTextFill(Color.WHITE);
        gameNameLabel.setPrefWidth(150);

        // Game status
        String status = game.isGameStarted() ? (game.isEnded() ? "FINISHED" : "IN PROGRESS") : "WAITING FOR PLAYERS";
        Label statusLabel = new Label(status);
        statusLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        statusLabel.setPrefWidth(200);

        // Set status color
        switch (status) {
            case "FINISHED":
                statusLabel.setTextFill(Color.GRAY);
                break;
            case "IN PROGRESS":
                statusLabel.setTextFill(Color.LIGHTGREEN);
                break;
            case "WAITING FOR PLAYERS":
                statusLabel.setTextFill(Color.ORANGE);
                break;
        }

        // Players info
        int activePlayers = 0;
        for (int i = 0; i < game.getPlayers().length; i++) {
            if (game.getPlayers()[i] != null) {
                activePlayers++;
            }
        }
        String playersText = activePlayers + "/" + game.getPlayers().length;
        Label playersLabel = new Label(playersText);
        playersLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        playersLabel.setTextFill(Color.WHITE);
        playersLabel.setPrefWidth(120);

        // Turn info
        String turnText = game.isGameStarted() ?
                (game.isEnded() ? "Game Over" : "Turn " + game.getTurnCount()) :
                "Not Started";
        Label turnLabel = new Label(turnText);
        turnLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        turnLabel.setTextFill(Color.LIGHTGRAY);
        turnLabel.setPrefWidth(80);

        // Map size
        String mapSize = game.getMapWidth() + "x" + game.getMapHeight();
        Label mapSizeLabel = new Label(mapSize);
        mapSizeLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        mapSizeLabel.setTextFill(Color.LIGHTGRAY);
        mapSizeLabel.setPrefWidth(100);

        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Action button
        Button actionButton = new Button();
        actionButton.setPrefHeight(35);
        actionButton.setPrefWidth(100);
        actionButton.setVisible(!game.isEnded());
        actionButton.setManaged(!game.isEnded());

        if (!game.isGameStarted()) {
            if (!game.userHasPlayer(user)) {
                actionButton.setText("JOIN");
                actionButton.setStyle("-fx-background-color: #2196F3; " +
                                              "-fx-text-fill: white; " +
                                              "-fx-font-size: 12; " +
                                              "-fx-font-weight: bold; " +
                                              "-fx-background-radius: 3;");
                actionButton.setOnAction(e -> handleJoinGame(game));
            } else {
                actionButton.setVisible(false);
                actionButton.setManaged(false);
            }
        } else {
            actionButton.setText("PLAY");
            actionButton.setStyle("-fx-background-color: #4CAF50; " +
                                          "-fx-text-fill: white; " +
                                          "-fx-font-size: 12; " +
                                          "-fx-font-weight: bold; " +
                                          "-fx-background-radius: 3;");
            actionButton.setOnAction(e -> handlePlayGame(game));
        }

        // Add hover effects
        gameRow.setOnMouseEntered(e ->
                                          gameRow.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); " +
                                                                   "-fx-background-radius: 5; " +
                                                                   "-fx-border-color: rgba(255, 215, 0, 0.3); " +
                                                                   "-fx-border-radius: 5;")
        );

        gameRow.setOnMouseExited(e ->
                                         gameRow.setStyle("-fx-background-color: rgba(255, 255, 255, 0.05); " +
                                                                  "-fx-background-radius: 5; " +
                                                                  "-fx-border-color: rgba(255, 255, 255, 0.1); " +
                                                                  "-fx-border-radius: 5;")
        );

        gameRow.getChildren().addAll(gameNameLabel, statusLabel, playersLabel,
                                     turnLabel, mapSizeLabel, spacer, actionButton);
        return gameRow;
    }

    private void handleCreateNewGame() {
        ((TheThrone) FXGL.getApp()).showCreateNewGameMenu(this.user);
    }

    private void handleJoinGame(Game game) {
        if (!game.userHasPlayer(user)) {
            ((TheThrone) FXGL.getApp()).showCreatePlayerMenu(this.user, game);
        } else {
            System.out.println("Join game: " + game.getName());
        }
    }

    private void handlePlayGame(Game game) {
        // Check if user has a player in this game
        boolean hasPlayer = false;
        for (Player player : game.getPlayers()) {
            if (player != null && player.getUser().equals(user)) {
                hasPlayer = true;
                break;
            }
        }

        if (!hasPlayer) {
            System.out.println("Error: User doesn't have a player in this game!");
            return;
        }

        // Start the game using the integrated game functionality
        ((TheThrone) FXGL.getApp()).startGame(game, user);
    }

    public void refreshGames() {
        loadGames();
    }
}