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

import java.util.ArrayList;
import java.util.List;
// todo make it so that the players are unique
public class CreateGameSubScene extends SubScene {
    private final User user;
    private VBox root;

    // Form fields
    private TextField gameNameField;
    private ComboBox<String> gameTypeCombo;
    private TextField userSearchField;
    private ListView<User> suggestionsListView;
    private ListView<User> selectedUsersListView;
    private List<User> selectedUsers;
    private Label selectedUsersCountLabel;
    private Spinner<Integer> mapWidthSpinner;
    private Spinner<Integer> mapHeightSpinner;
    private Spinner<Integer> turnLimitSpinner;
    private Label turnLimitLabel;
    private Label messageLabel;

    public CreateGameSubScene(User user) {
        this.user = user;
        this.selectedUsers = new ArrayList<>();
        this.selectedUsers.add(user); // Creator is always included
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

        // Player Selection Section
        VBox playerSelectionSection = createPlayerSelectionSection();

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
                playerSelectionSection,
                mapSizeSection,
                turnLimitSection,
                messageLabel,
                createButton
        );

        // Wrapper for centering the form in the ScrollPane
        VBox scrollContent = new VBox();
        scrollContent.setAlignment(Pos.TOP_CENTER);
        scrollContent.setPadding(new Insets(20));
        scrollContent.getChildren().add(formContainer);

        // Create ScrollPane for the form
        ScrollPane scrollPane = new ScrollPane(scrollContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPrefSize(1280, 600); // Leave space for header
        scrollPane.setStyle("-fx-background-color: transparent; " +
                                    "-fx-background: transparent;");

        // Header with back button (outside scroll area)
        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.getChildren().add(backButton);

        // Main layout with header, title, and scrollable form
        VBox mainLayout = new VBox(20);
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.setPrefSize(1280, 720);
        mainLayout.setPadding(new Insets(30));
        mainLayout.getChildren().addAll(headerBox, titleLabel, scrollPane);

        getContentRoot().getChildren().add(mainLayout);
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

    private VBox createPlayerSelectionSection() {
        VBox section = new VBox(8);
        section.setAlignment(Pos.CENTER_LEFT);

        Label label = new Label("Select Players");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        label.setTextFill(Color.GOLD);

        // Search field for users
        userSearchField = new TextField();
        userSearchField.setPromptText("Search for users to add...");
        userSearchField.setPrefHeight(40);
        userSearchField.setStyle("-fx-font-size: 14; -fx-pref-width: 300;");

        // Add text change listener for live search
        userSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateUserSuggestions(newValue);
        });

        // Suggestions list view
        suggestionsListView = new ListView<>();
        suggestionsListView.setPrefHeight(100);
        suggestionsListView.setPrefWidth(300);
        suggestionsListView.setStyle("-fx-font-size: 12;");
        suggestionsListView.setVisible(false);
        suggestionsListView.setManaged(false);

        suggestionsListView.setCellFactory(listView -> new ListCell<User>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                } else {
                    setText(user.toString());
                }
            }
        });

        suggestionsListView.setOnMouseClicked(e -> {
            User selectedUser = suggestionsListView.getSelectionModel().getSelectedItem();
            if (selectedUser != null && e.getClickCount() == 2) {
                addUserToGame(selectedUser);
            }
        });

        // Selected users list
        selectedUsersListView = new ListView<>();
        selectedUsersListView.setPrefHeight(120);
        selectedUsersListView.setPrefWidth(300);
        selectedUsersListView.setStyle("-fx-font-size: 12;");

        // Custom cell factory to show user info and remove button
        selectedUsersListView.setCellFactory(listView -> new ListCell<User>() {
            private Button removeButton = new Button("×");
            private HBox hbox = new HBox();
            private Label userLabel = new Label();

            {
                removeButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold;");
                removeButton.setPrefSize(20, 20);
                removeButton.setOnAction(e -> {
                    User userToRemove = getItem();
                    if (userToRemove != null && !userToRemove.equals(user)) { // Can't remove creator
                        selectedUsers.remove(userToRemove);
                        updateSelectedUsersList();
                    }
                });

                hbox.setSpacing(10);
                hbox.setAlignment(Pos.CENTER_LEFT);
                hbox.getChildren().addAll(userLabel, removeButton);
            }

            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setGraphic(null);
                } else {
                    userLabel.setText(user.toString());
                    // Don't show remove button for the creator
                    removeButton.setVisible(!user.equals(CreateGameSubScene.this.user));
                    setGraphic(hbox);
                }
            }
        });

        // Update initial selected users list
        updateSelectedUsersList();

        selectedUsersCountLabel = new Label("Selected Players (" + selectedUsers.size() + "/8):");
        selectedUsersCountLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        selectedUsersCountLabel.setTextFill(Color.LIGHTGRAY);

        section.getChildren().addAll(label, userSearchField, suggestionsListView, selectedUsersCountLabel, selectedUsersListView);
        return section;
    }

    private void updateUserSuggestions(String query) {
        if (query == null || query.trim().isEmpty()) {
            suggestionsListView.setVisible(false);
            suggestionsListView.setManaged(false);
            return;
        }

        User[] searchResults = User.search(query.trim());
        suggestionsListView.getItems().clear();

        for (User searchUser : searchResults) {
            if (searchUser != null && !selectedUsers.contains(searchUser)) {
                suggestionsListView.getItems().add(searchUser);
            }
        }

        boolean hasResults = suggestionsListView.getItems().size() > 0;
        suggestionsListView.setVisible(hasResults);
        suggestionsListView.setManaged(hasResults);
    }

    private void addUserToGame(User userToAdd) {
        if (selectedUsers.size() >= 8) {
            showMessage("Maximum 8 players allowed", Color.ORANGE);
            return;
        }

        if (selectedUsers.contains(userToAdd)) {
            showMessage("User already selected", Color.ORANGE);
            return;
        }

        selectedUsers.add(userToAdd);
        updateSelectedUsersList();
        userSearchField.clear();
        suggestionsListView.setVisible(false);
        suggestionsListView.setManaged(false);
    }

    private void updateSelectedUsersList() {
        selectedUsersListView.getItems().clear();
        selectedUsersListView.getItems().addAll(selectedUsers);

        // Update the label
        if (selectedUsersCountLabel != null) {
            selectedUsersCountLabel.setText("Selected Players (" + selectedUsers.size() + "/8):");
        }
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

        if (selectedUsers.size() < 2) {
            showMessage("At least 2 players are required", Color.RED);
            return;
        }

        String gameType = gameTypeCombo.getValue();
        int playerCount = selectedUsers.size();
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
            // Create users array from selected users
            User[] users = selectedUsers.toArray(new User[0]);

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