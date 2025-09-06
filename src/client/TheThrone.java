package client;

import client.render.RenderTile;
import client.scenes.CreateGameSubScene;
import client.scenes.CreatePlayerSubScene;
import client.scenes.GamesMenuSubScene;
import client.scenes.LoginSubScene;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.scene.CSS;
import com.almasb.fxgl.texture.Texture;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.paint.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import obj.Player;
import obj.Weapon;
import obj.auth.User;
import obj.building.*;
import obj.building.interfaces.functional.BuildingFactory;
import obj.building.mystical.MysticalContainer;
import obj.game.Game;
import obj.map.Tile;
import obj.soldier.Soldier;
import obj.soldier.wizard.Wizard;
import util.Position;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.geometry.Pos;

import java.util.*;

import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class TheThrone extends GameApplication {

    private static final int SCREEN_WIDTH = 1280;
    private static final int SCREEN_HEIGHT = 720;

    // Game constants
    private static final int TILE_WIDTH = 40;
    private static final int TILE_HEIGHT = 20;
    private static final int TILE_HEIGHT_OFFSET = 20;

    private static final int STAR_COUNT = 2000;
    private static final float STAR_RADIUS_MIN = 0f;
    private static final float STAR_RADIUS_MAX = 1.6f;
    private static final float STAR_VELOCITY_X_MIN = 0.003f;
    private static final float STAR_VELOCITY_Y_MIN = 0.006f;
    private static final float STAR_VELOCITY_X_MAX = 0.010f;
    private static final float STAR_VELOCITY_Y_MAX = 0.010f;

    private static final float STAR_TWINKLE_FREQUENCY_MIN = 0f;
    private static final float STAR_TWINKLE_FREQUENCY_MAX = 0.2f;
    private static final float STAR_OPACITY_MIN = 10f;
    private static final float STAR_OPACITY_MAX = 250f;

    private static final HashMap<String, Image> textureCache = new HashMap<>();

    // Game state
    private boolean gameActive = false;
    private Player player;
    private Game currentGame;
    private RenderTile highlightedTile;

    private Star[] stars;

    // New rendering system
    private RenderTile[][] renderTiles; // Complete array of all rendered tiles

    private boolean isSoldierMoving;
    private boolean isSoldierAttacking;
    private boolean isWizardCasting;
    private boolean isYourTurn;

    private VBox tileMenu;
    private VBox weaponsMenu;
    private Texture skipButton;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(SCREEN_WIDTH);
        settings.setHeight(SCREEN_HEIGHT);
        settings.setTitle("The Throne");
        settings.setVersion("0.1");
        settings.setFullScreenAllowed(true);
        settings.setFullScreenFromStart(false);
    }

    @Override
    protected void initGame() {
        getGameScene().setBackgroundColor(
                new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                                   new Stop(0, Color.rgb(4, 8, 15)),
                                   new Stop(1, Color.rgb(10, 20, 96))
                ));

        FXGL.runOnce(() -> FXGL.getSceneService().pushSubScene(new LoginSubScene()), Duration.ZERO);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("wealth", 0);
        vars.put("food", 0);
        vars.put("wood", 0);
        vars.put("iron", 0);
        vars.put("stone", 0);
    }

    @Override
    protected void initInput() {
        Input input = getInput();

        FXGL.onBtn(MouseButton.PRIMARY, () -> {
            if (!gameActive) return;

            Position pos = getCoordsToIsometric(input.getMouseXWorld(), input.getMouseYWorld());
            if (this.isSoldierMoving) {
                Soldier soldier = this.currentGame.getTile(this.highlightedTile.getPosition()).getSoldier();

                if (soldier != null && !soldier.hasMoved()) {
                    this.isSoldierMoving = false;

                    if (this.currentGame.getTile(this.highlightedTile.getPosition()).getSoldier().moveTo(pos)) {
                        // Update the map instead of manually moving soldiers
                        updateMap();
                    }
                }
            } else if (this.isSoldierAttacking) {
                Soldier soldier = this.currentGame.getTile(this.highlightedTile.getPosition()).getSoldier();
                if (soldier != null && !soldier.hasAttacked()) {
                    this.isSoldierAttacking = false;

                    if (this.currentGame.getTile(this.highlightedTile.getPosition()).getSoldier().attack(pos)) {
                        // Update the map instead of manually moving soldiers
                        updateMap();
                    }
                }
            }
            else if (this.isWizardCasting){
                Tile target = this.currentGame.getTile(this.highlightedTile.getPosition());
                Soldier soldier = this.currentGame.getMap()[this.highlightedTile.getPosition().x()][this.highlightedTile.getPosition().y()].getSoldier();
                if (soldier instanceof Wizard wizard && !wizard.getHadEffect()) {
                    this.isWizardCasting = false;

                    if (wizard.castEffect(this.highlightedTile.getPosition())) {
                        // Update the map instead of manually moving soldiers
                        updateMap();
                    }
                }

            }
            else {
                renderHighlightedTile(pos.x(), pos.y());
            }



        });

        FXGL.onKeyUp(KeyCode.F11, () -> {
            getSettings().getFullScreen().set(!getSettings().getFullScreen().get());
            getGameScene().getViewport().setHeight(SCREEN_HEIGHT);
            getGameScene().getViewport().setWidth(SCREEN_WIDTH);
        });

        FXGL.onKey(KeyCode.S, () -> {
            if (!gameActive) return;
            Viewport viewPort = getGameScene().getViewport();
            viewPort.setY(viewPort.getY() + 1);
        });
        FXGL.onKey(KeyCode.W, () -> {
            if (!gameActive) return;
            Viewport viewPort = getGameScene().getViewport();
            viewPort.setY(viewPort.getY() - 1);
        });
        FXGL.onKey(KeyCode.D, () -> {
            if (!gameActive) return;
            Viewport viewPort = getGameScene().getViewport();
            viewPort.setX(viewPort.getX() + 1);
        });
        FXGL.onKey(KeyCode.A, () -> {
            if (!gameActive) return;
            Viewport viewPort = getGameScene().getViewport();
            viewPort.setX(viewPort.getX() - 1);
        });
        FXGL.onKey(KeyCode.Z, () -> {
            if (!gameActive) return;
            Viewport viewPort = getGameScene().getViewport();
            viewPort.setZoom(viewPort.getZoom() + .01);
        });
        FXGL.onKey(KeyCode.X, () -> {
            if (!gameActive) return;
            Viewport viewPort = getGameScene().getViewport();
            viewPort.setZoom(viewPort.getZoom() - .01);
        });

        FXGL.onKeyDown(KeyCode.ESCAPE, () -> {
            if (gameActive) {
                exitGame();
            }
        });
    }

    @Override
    protected void initUI() {
        // UI will be initialized when game starts
    }

    @Override
    protected void onUpdate(double tpf) {
        if (gameActive && player != null) {
            FXGL.getWorldProperties().setValue("wealth", this.player.getWealth());
            FXGL.getWorldProperties().setValue("food", this.player.getFoodCount());
            FXGL.getWorldProperties().setValue("wood", this.player.getWoodCount());
            FXGL.getWorldProperties().setValue("iron", this.player.getIronCount());
            FXGL.getWorldProperties().setValue("stone", this.player.getStoneCount());
        }
    }

    // Menu navigation methods
    public void showGamesMenu(User user) {
        FXGL.getSceneService().popSubScene();
        FXGL.runOnce(() -> FXGL.getSceneService().pushSubScene(new GamesMenuSubScene(user)), Duration.ZERO);
    }

    public void showCreateNewGameMenu(User user) {
        FXGL.getSceneService().popSubScene();
        FXGL.runOnce(() -> FXGL.getSceneService().pushSubScene(new CreateGameSubScene(user)), Duration.ZERO);
    }

    public void showCreatePlayerMenu(User user, Game game) {
        FXGL.getSceneService().popSubScene();
        FXGL.runOnce(() -> FXGL.getSceneService().pushSubScene(new CreatePlayerSubScene(user, game)), Duration.ZERO);
    }

    // Game starting method - this is what gets called when "PLAY" is clicked
    public void startGame(Game game, User user) {
        this.currentGame = game;

        // Find the player for this user in the game
        for (Player p : game.getPlayers()) {
            if (p != null && p.getUser().equals(user)) {
                this.player = p;
                break;
            }
        }

        if (this.player == null) {
            System.err.println("Player not found in game!");
            return;
        }

        FXGL.getSceneService().popSubScene();

        // Initialize game state
        initializeGameState();

        // Set game as active
        this.gameActive = true;

        // Initialize game UI
        initializeGameUI();

        // Render the game
        renderGameWorld();
    }

    private void initializeGameState() {
        this.highlightedTile = null;
        this.stars = new Star[STAR_COUNT];
        this.isYourTurn = this.currentGame.GetActivePlayer().equals(this.player);
        this.isSoldierMoving = false;

        // Initialize the render tiles array
        this.renderTiles = new RenderTile[currentGame.getMapWidth()][currentGame.getMapHeight()];

        // Clear any existing entities
        FXGL.getGameWorld().getEntities().forEach(Entity::removeFromWorld);

        // Clear UI nodes
        getGameScene().getUINodes().clear();

        // Setup viewport
        getGameScene().appendCSS(new CSS("assets/css/style.css"));
        Viewport viewport = getGameScene().getViewport();
        viewport.setX(-SCREEN_WIDTH >> 1);
        viewport.setY(-SCREEN_HEIGHT >> 1);
        viewport.setZoom(1);
    }

    private void initializeGameUI() {
        if (!this.isYourTurn) {
            stringBuilderUI(110, "not your turn");
        }
        stringBuilderUI(130, "wealth", "Wealth: %d$");
        stringBuilderUI(150, "wood", "Wood: %d");
        stringBuilderUI(170, "stone", "Stone: %d");
        stringBuilderUI(190, "food", "Food: %d");
        stringBuilderUI(210, "iron", "Iron: %d");

        tileMenu = new VBox(10);
        tileMenu.setTranslateX(SCREEN_WIDTH - 300);
        tileMenu.setTranslateY(100);
        tileMenu.setAlignment(Pos.TOP_CENTER);
        tileMenu.setFillWidth(true);
        tileMenu.setMinWidth(200);
        getGameScene().addUINode(tileMenu);

        if (this.isYourTurn) {
            Image image = textureCache.computeIfAbsent("ui/end_turn.png", file -> getAssetLoader().loadImage(file));
            this.skipButton = new Texture(image);
            this.skipButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
                Image img;
                if (newValue) {
                    img = textureCache.computeIfAbsent("ui/end_turn_hovered.png", file -> getAssetLoader().loadImage(file));
                } else {
                    img = textureCache.computeIfAbsent("ui/end_turn.png", file -> getAssetLoader().loadImage(file));
                }
                this.skipButton.setImage(img);
            });
            this.skipButton.setOnMouseClicked(mouseEvent -> {
                if (this.isYourTurn) {
                    stringBuilderUI(110, "not your turn");
                    this.currentGame.nextTurn();
                    this.currentGame.save();
                    this.isYourTurn = false;
                    removeUINode(this.tileMenu);
                    removeUINode(this.skipButton);
                }

                this.skipButton.setImage(textureCache.computeIfAbsent("ui/end_turn_selected.png", file -> getAssetLoader().loadImage(file)));
            });

            this.skipButton.setX(SCREEN_WIDTH - 230);
            this.skipButton.setY(SCREEN_HEIGHT - 160);
            addUINode(this.skipButton);
        }

        Button exitButton = new Button("Exit to Menu");
        exitButton.setPrefHeight(40);
        exitButton.setPrefWidth(120);
        exitButton.setStyle("-fx-background-color: #f44336; " +
                                    "-fx-text-fill: white; " +
                                    "-fx-font-size: 12; " +
                                    "-fx-font-weight: bold; " +
                                    "-fx-background-radius: 5;");
        exitButton.setTranslateX(10);
        exitButton.setTranslateY(10);
        exitButton.setOnAction(e -> exitGame());
        getGameScene().addUINode(exitButton);
    }

    private void renderGameWorld() {
        renderMap();
        renderSky();
    }

    private void exitGame() {
        this.gameActive = false;

        List<Entity> entities = new ArrayList<>(FXGL.getGameWorld().getEntities());
        for (Entity entity : entities) {
            entity.removeFromWorld();
        }

        if (this.tileMenu != null) {
            getGameScene().removeUINode(this.tileMenu);
        }
        if (this.skipButton != null) {
            getGameScene().removeUINode(this.skipButton);
        }

        List<javafx.scene.Node> uiNodes = new ArrayList<>(getGameScene().getUINodes());
        for (javafx.scene.Node node : uiNodes) {
            getGameScene().removeUINode(node);
        }
        getGameScene().getContentRoot().getChildren().clear();
        getGameScene().clearCSS();
        showGamesMenu(this.player.getUser());
    }

    private static Position getIsometricToCoords(int x, int y) {
        return new Position(
                ((x - y) * TILE_WIDTH / 2),
                ((x + y) * TILE_HEIGHT / 2) - TILE_HEIGHT_OFFSET
        );
    }

    private static Position getCoordsToIsometric(double x, double y) {
        return new Position(
                (int) (y / TILE_HEIGHT + x / TILE_WIDTH - .5),
                (int) (y / TILE_HEIGHT - x / TILE_WIDTH + .5)
        );
    }

    private void renderMap() {
        for (int x = 0; x < currentGame.getMapWidth(); x++) {
            for (int y = 0; y < currentGame.getMapHeight(); y++) {
                Position coords = getIsometricToCoords(x, y);
                Tile tile = currentGame.getMap()[x][y];
                boolean hasFog = !player.getVision()[x][y];

                Entity tileEntity;
                Entity treeEntity = null;
                Entity buildingEntity = null;
                Entity soldierEntity = null;

                if (hasFog) {
                    // Render fog tile instead of actual tile
                    tileEntity = entityBuilder(coords.x(), coords.y(), 0, "tiles/cloud_tile.png");
                } else {
                    // Render normal tile and all objects on it
                    tileEntity = entityBuilder(coords.x(), coords.y(), 0, getTileTextureName(tile.getBiome(), tile.getHeight()));

                    if (tile.hasTree()) {
                        treeEntity = entityBuilder(coords.x(), coords.y(), 4, "features/tree.png");
                    }

                    buildingEntity = createBuildingEntity(tile, coords.x(), coords.y());
                    soldierEntity = createSoldierEntity(tile, coords.x(), coords.y());
                }

                this.renderTiles[x][y] = new RenderTile(
                        new Position(x, y),
                        tileEntity,
                        treeEntity,
                        buildingEntity,
                        soldierEntity,
                        hasFog
                );
            }
        }
    }

    public void updateMap() {
        for (int x = 0; x < currentGame.getMapWidth(); x++) {
            for (int y = 0; y < currentGame.getMapHeight(); y++) {
                Tile gameTile = currentGame.getMap()[x][y];
                RenderTile renderTile = this.renderTiles[x][y];
                Position coords = getIsometricToCoords(x, y);

                boolean currentlyHasFog = !player.getVision()[x][y];
                boolean previouslyHadFog = renderTile.hasFog();

                // If fog status changed, we need to update the tile rendering
                if (currentlyHasFog != previouslyHadFog) {
                    // Remove old tile render
                    renderTile.getTileRender().removeFromWorld();

                    Entity newTileEntity;
                    if (currentlyHasFog) {
                        // Show fog
                        newTileEntity = entityBuilder(coords.x(), coords.y(), 0, "tiles/cloud_tile.png");
                        // Remove all objects
                        if (renderTile.getTreeRender() != null) {
                            renderTile.getTreeRender().removeFromWorld();
                            renderTile.setTreeRender(null);
                        }
                        if (renderTile.getBuildingRender() != null) {
                            renderTile.getBuildingRender().removeFromWorld();
                            renderTile.setBuildingRender(null);
                        }
                        if (renderTile.getSoldierEntity() != null) {
                            renderTile.getSoldierEntity().removeFromWorld();
                            renderTile.setSoldierEntity(null);
                        }
                    } else {
                        // Remove fog, show actual tile
                        newTileEntity = entityBuilder(coords.x(), coords.y(), 0, getTileTextureName(gameTile.getBiome(), gameTile.getHeight()));
                        // Add back all objects that should be visible
                        if (gameTile.hasTree()) {
                            renderTile.setTreeRender(entityBuilder(coords.x(), coords.y(), 4, "features/tree.png"));
                        }
                        if (gameTile.getBuilding() != null) {
                            renderTile.setBuildingRender(createBuildingEntity(gameTile, coords.x(), coords.y()));
                        }
                        if (gameTile.getSoldier() != null) {
                            renderTile.setSoldierEntity(createSoldierEntity(gameTile, coords.x(), coords.y()));
                        }
                    }

                    // Update the tile render in renderTiles array
                    this.renderTiles[x][y] = new RenderTile(
                            renderTile.getPosition(),
                            newTileEntity,
                            renderTile.getTreeRender(),
                            renderTile.getBuildingRender(),
                            renderTile.getSoldierEntity(),
                            currentlyHasFog
                    );
                } else if (!currentlyHasFog) {
                    // No fog change, update normally if tile is visible
                    updateTreeRender(gameTile, renderTile, coords.x(), coords.y());
                    updateBuildingRender(gameTile, renderTile, coords.x(), coords.y());
                    updateSoldierRender(gameTile, renderTile, coords.x(), coords.y());
                }
            }
        }
    }

    private void updateTreeRender(Tile gameTile, RenderTile renderTile, int coordsX, int coordsY) {
        boolean hasTree = gameTile.hasTree();
        boolean hasTreeRender = renderTile.getTreeRender() != null;

        if (hasTree && !hasTreeRender) {
            Entity treeEntity = entityBuilder(coordsX, coordsY, 4, "features/tree.png");
            renderTile.setTreeRender(treeEntity);
        } else if (!hasTree && hasTreeRender) {
            renderTile.getTreeRender().removeFromWorld();
            renderTile.setTreeRender(null);
        }
    }

    private void updateBuildingRender(Tile gameTile, RenderTile renderTile, int coordsX, int coordsY) {
        Building gameBuilding = gameTile.getBuilding();
        Entity renderBuilding = renderTile.getBuildingRender();

        if (gameBuilding != null && renderBuilding == null) {
            Entity buildingEntity = createBuildingEntity(gameTile, coordsX, coordsY);
            renderTile.setBuildingRender(buildingEntity);
        } else if (gameBuilding == null && renderBuilding != null) {
            renderBuilding.removeFromWorld();
            renderTile.setBuildingRender(null);
        } else if (gameBuilding != null) {
            renderBuilding.removeFromWorld();
            Entity newBuildingEntity = createBuildingEntity(gameTile, coordsX, coordsY);
            renderTile.setBuildingRender(newBuildingEntity);
        }
    }

    private void updateSoldierRender(Tile gameTile, RenderTile renderTile, int coordsX, int coordsY) {
        Soldier gameSoldier = gameTile.getSoldier();
        Entity renderSoldier = renderTile.getSoldierEntity();

        if (gameSoldier != null && renderSoldier == null) {
            Entity soldierEntity = createSoldierEntity(gameTile, coordsX, coordsY);
            renderTile.setSoldierEntity(soldierEntity);
        } else if (gameSoldier == null && renderSoldier != null) {
            renderSoldier.removeFromWorld();
            renderTile.setSoldierEntity(null);
        } else if (gameSoldier != null) {
            Position newCoords = getIsometricToCoords(renderTile.getPosition().x(), renderTile.getPosition().y());
            renderSoldier.setX(newCoords.x());
            renderSoldier.setY(newCoords.y());
            renderSoldier.setZIndex(getZIndex(renderTile.getPosition().x(), renderTile.getPosition().y(), 5));
        }
    }

    private Entity createBuildingEntity(Tile tile, int coordsX, int coordsY) {
        Building building = tile.getBuilding();
        if (building == null) return null;

        String texture = getBuildingTexture(building, tile);

        return entityBuilder(coordsX, coordsY, 4, texture);
    }

    private String getBuildingTexture(Building building, Tile tile) {
        return switch (building) {
            case Castle ignored -> "buildings/castle.png";
            case WizardTower wizardTower -> {
                String magic = wizardTower.getMagic().toString();
                yield String.format("buildings/wizardtowers/%stower.png", magic.substring(0, magic.length() - 5));
            }
            case Barrack ignored -> "buildings/barrack.png";
            case Farmland ignored -> "buildings/farmland.png";
            case LumberHut ignored -> "buildings/lumberhut.png";
            case Mine ignored -> String.format("buildings/mine_%s.png", tile.getHeight().toString());
            case MysticalContainer container -> {
                String name = container.getName().replace(' ', '_').toLowerCase();
                yield String.format("buildings/mystical/%s.png", name);
            }
            default -> null;
        };
    }

    private Entity createSoldierEntity(Tile tile, int coordsX, int coordsY) {
        if (tile.getSoldier() == null) return null;

        return entityBuilder(coordsX, coordsY, 5, "soldiers/Untitled.png");
    }

    private void renderHighlightedTile(int x, int y) {
        if (this.highlightedTile != null) {
            this.highlightedTile.getTileRender().removeFromWorld();
        }
        if (x < 0 || x >= currentGame.getMapWidth() || y < 0 || y >= currentGame.getMapHeight()) {
            this.highlightedTile = null;
            this.updateTileMenu();
            return;
        }

        Position isoCoords = getIsometricToCoords(x, y);
        this.highlightedTile = new RenderTile(
                new Position(x, y),
                entityBuilder(isoCoords.x(), isoCoords.y(), 1, "tiles/selected_tile.png"),
                null, null, null, false
        );
        this.updateTileMenu();
    }

    private void renderSky() {
        Canvas starCanvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        GraphicsContext gc = starCanvas.getGraphicsContext2D();
        starCanvas.setMouseTransparent(true);

        Random rand = new Random();

        for (int i = 0; i < STAR_COUNT; i++) {
            double maxOpacity = rand.nextDouble(STAR_OPACITY_MIN, STAR_OPACITY_MAX);

            stars[i] = new Star(rand.nextDouble(0, SCREEN_WIDTH),
                                rand.nextDouble(0, SCREEN_HEIGHT),
                                rand.nextDouble(STAR_VELOCITY_X_MIN, STAR_VELOCITY_X_MAX),
                                rand.nextDouble(STAR_VELOCITY_Y_MIN, STAR_VELOCITY_Y_MAX),
                                rand.nextDouble(STAR_TWINKLE_FREQUENCY_MIN, STAR_TWINKLE_FREQUENCY_MAX),
                                maxOpacity,
                                rand.nextDouble(STAR_RADIUS_MIN, STAR_RADIUS_MAX),
                                rand.nextDouble(0, maxOpacity));
        }

        FXGL.getGameScene().getContentRoot().getChildren().addFirst(starCanvas);

        FXGL.getGameScene().addListener((i) -> {
            if (!gameActive) return;

            gc.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

            for (Star star : stars) {
                star.x += star.dx;
                star.y += star.dy;

                star.opacity += star.twinkleFrequency;
                if (star.opacity > star.maxOpacity || star.opacity < 0) {
                    star.twinkleFrequency *= -1;
                }

                if (star.x > SCREEN_WIDTH)
                    star.x = 0;
                else if (star.x < 0)
                    star.x = SCREEN_WIDTH;

                if (star.y > SCREEN_HEIGHT)
                    star.y = 0;
                else if (star.y < 0)
                    star.y = SCREEN_HEIGHT;

                gc.setFill(Color.rgb(255, 255, 255, star.opacity > 1 ? 1 : star.opacity < 0 ? 0 : star.opacity));
                gc.fillOval(star.x, star.y, star.radius, star.radius);
            }
        });
    }

    private String getTileTextureName(Tile.Biome biome, Tile.Height height) {
        String biomeName = biome.name().toLowerCase();
        String heightName = height.name().toLowerCase();
        return String.format("tiles/%s_%s.png", biomeName, heightName);
    }

    private void updateTileMenu() {
        if (!this.isYourTurn)
            return;

        this.tileMenu.getChildren().clear();
        if (this.highlightedTile == null) {
            return;
        }
        Tile tile = this.currentGame.getMap()[this.highlightedTile.getPosition().x()][this.highlightedTile.getPosition().y()];

        if (tile.getBuilding() != null) {
            Button buildingInfo = new Button("View Building");
            buildingInfo.setMinWidth(200);

            buildingInfo.setOnAction(e -> System.out.println("Building: " + tile.getBuilding().getClass().getSimpleName()));
            this.tileMenu.getChildren().add(buildingInfo);
        } else {
            for (BuildingFactory factory : Building.getAllowedBuildingsToBuild(player, this.highlightedTile.getPosition())) {
                Button btn = new Button("Build " + factory.create(null, null).getClass().getSimpleName());
                btn.setMinWidth(200);
                btn.setOnAction(e -> {
                    this.currentGame.getMap()[this.highlightedTile.getPosition().x()][this.highlightedTile.getPosition().y()].setBuilding(
                            factory.create(this.player, this.highlightedTile.getPosition())
                    );
                    updateMap();
                    updateTileMenu();
                });
                this.tileMenu.getChildren().add(btn);
            }
        }
        if (tile.getSoldier() != null &&
                !tile.getSoldier().hasMoved() &&
                tile.getBuilding() != null &&
                tile.getBuilding() instanceof Castle castle &&
                castle.getOwner() != tile.getSoldier().getPlayer()) {
            Button captureCastle = new Button("Capture Castle");
            captureCastle.setMinWidth(200);
            captureCastle.setOnAction(e -> {
                castle.capture(this.player);
                updateMap();
            });

            this.tileMenu.getChildren().add(captureCastle);
        }
        if (!player.getHavingTower() && tile.getBuilding() != null
                && !tile.getSoldier().hasMoved() &&
                tile.getBuilding() instanceof WizardTower wizardTower && wizardTower.getOwner()==null) {
            Button captureTower = new Button("Capture Tower");
            captureTower.setMinWidth(200);
            captureTower.setOnAction(e -> {
                wizardTower.capture(this.player);
                updateMap();
            });
        }

        if (tile.getSoldier() != null && !tile.getSoldier().hasMoved()) {
            Button moveSoldier = new Button("Move Soldier");
            moveSoldier.setMinWidth(200);
            moveSoldier.setOnAction(e -> this.isSoldierMoving = true);

            this.tileMenu.getChildren().add(moveSoldier);
        }

        if (tile.getSoldier() != null && !tile.getSoldier().hasAttacked()) {
            Button attackSoldier = new Button("Attack Soldier");
            attackSoldier.setMinWidth(200);
            attackSoldier.setOnAction(e -> this.isSoldierAttacking = true);

            this.tileMenu.getChildren().add(attackSoldier);
        }

        if (tile.getSoldier() != null && tile.getSoldier() instanceof Wizard wizard
                && !tile.getSoldier().hasMoved() && !wizard.getHadEffect()) {
            Button wizardEffect = new Button("Wizard Effect");
            wizardEffect.setMinWidth(200);
            wizardEffect.setOnAction(e -> this.isWizardCasting=true);

            this.tileMenu.getChildren().add(wizardEffect);
        }
    }

    private static int getZIndex(int x, int y, int level) {
        return 6 * (x + y) + level;
    }

    private static Entity entityBuilder(int x, int y, int level, String spriteFile) {
        Image image = textureCache.computeIfAbsent(spriteFile, file -> getAssetLoader().loadImage(file));
        Position pos = getCoordsToIsometric(x, y);

        Texture texture = new Texture(image);
        texture.setSmooth(false);
        return FXGL.entityBuilder()
                .at(x, y)
                .view(texture)
                .zIndex(getZIndex(pos.x(), pos.y(), level))
                .buildAndAttach();
    }

    private static void stringBuilderUI(int x, int y, String property, String value) {
        Text textPixels = new Text();
        textPixels.setTranslateX(x);
        textPixels.setTranslateY(y);
        textPixels.setTextAlignment(TextAlignment.LEFT);
        textPixels.setFill(Color.WHITE);

        textPixels.textProperty().bind(FXGL.getWorldProperties()
                                               .intProperty(property)
                                               .asString(value));
        getGameScene().addUINode(textPixels);
    }

    private static void stringBuilderUI(int y, String value) {
        Text textPixels = new Text();
        textPixels.setTranslateX(100);
        textPixels.setTranslateY(y);
        textPixels.setTextAlignment(TextAlignment.LEFT);
        textPixels.setFill(Color.WHITE);

        textPixels.textProperty().set(value);
        getGameScene().addUINode(textPixels);
    }

    private static void stringBuilderUI(int y, String property, String value) {
        stringBuilderUI(100, y, property, value);
    }

    private static class Star {
        private double x;
        private double y;
        private double opacity;
        private double twinkleFrequency;
        private final double dx;
        private final double dy;
        private final double maxOpacity;
        private final double radius;

        Star(double x, double y, double dx, double dy, double twinkleFrequency, double maxOpacity, double radius, double opacity) {
            this.x = x;
            this.y = y;
            this.dx = dx;
            this.dy = dy;
            this.twinkleFrequency = twinkleFrequency;
            this.maxOpacity = maxOpacity;
            this.radius = radius;
            this.opacity = opacity;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}