package client;

import client.render.RenderTile;
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
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import obj.Player;
import obj.Tribe;
import obj.auth.User;
import obj.building.*;
import obj.building.interfaces.functional.BuildingFactory;
import obj.building.mystical.MysticalContainer;
import obj.game.DominationGame;
import obj.map.Tile;
import util.Position;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.geometry.Pos;

import java.util.*;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;


public class BasicGameApp extends GameApplication {

    private static final int TILE_WIDTH = 40;
    private static final int TILE_HEIGHT = 20;
    private static final int TILE_HEIGHT_OFFSET = 20;
    private static final int MAP_WIDTH = 100;
    private static final int MAP_HEIGHT = 100;

    private static final int SCREEN_WIDTH = 1280;
    private static final int SCREEN_HEIGHT = 720;

    private static final int STAR_COUNT = 2000;
    private static final float STAR_RADIUS_MIN = 0f;
    private static final float STAR_RADIUS_MAX = 1.6f;
    private static final float STAR_VELOCITY_X_MIN = 0.03f;
    private static final float STAR_VELOCITY_Y_MIN = 0.06f;
    private static final float STAR_VELOCITY_X_MAX = 0.10f;
    private static final float STAR_VELOCITY_Y_MAX = 0.10f;

    private static final float STAR_TWINKLE_FREQUENCY_MIN = 0f;
    private static final float STAR_TWINKLE_FREQUENCY_MAX = 0.2f;
    private static final float STAR_OPACITY_MIN = 10f;
    private static final float STAR_OPACITY_MAX = 250f;

    private static final Map<String, Image> textureCache = new HashMap<>();

    private Star[] stars;
    private Player player;
    private Tile[][] map;
    private RenderTile highlightedTile;

    private VBox tileMenu;


    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(SCREEN_WIDTH);
        settings.setHeight(SCREEN_HEIGHT);
        settings.setTitle("Isometric Map Renderer");
        settings.setVersion("0.1");
        settings.setFullScreenAllowed(true);
        settings.setFullScreenFromStart(true);
    }

    @Override
    protected void initGame() {
        Player[] players = new Player[4];
        this.player = players[0] = new Player(new User("amin", "amin", "amin"), null, Tribe.human, MAP_WIDTH, MAP_HEIGHT);
        players[1] = new Player(new User("amin", "amin", "amin"), null, Tribe.human, MAP_WIDTH, MAP_HEIGHT);
        players[2] = new Player(new User("amin", "amin", "amin"), null, Tribe.human, MAP_WIDTH, MAP_HEIGHT);
        players[3] = new Player(new User("amin", "amin", "amin"), null, Tribe.human, MAP_WIDTH, MAP_HEIGHT);
        DominationGame game = new DominationGame(players, MAP_WIDTH, MAP_HEIGHT);
        for (Player player : players) {
            player.setGame(game);
        }
        this.map = game.getMap();
        this.highlightedTile = null;
        this.stars = new Star[STAR_COUNT];

        getGameScene().setBackgroundColor(
                new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                                   new Stop(0, Color.rgb(4, 8, 15)),
                                   new Stop(1, Color.rgb(10, 20, 96))
                ));

        getGameScene().appendCSS(new CSS("assets/css/style.css"));
        Viewport viewport = getGameScene().getViewport();
        viewport.setX(-SCREEN_WIDTH >> 1);
        viewport.setY(-SCREEN_HEIGHT >> 1);
        viewport.setZoom(1);


        renderMap();
        renderSky();
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("wealth", 0);
        vars.put("food", 0);
        vars.put("wood", 0);
        vars.put("iron", 0);
        vars.put("stone", 0);
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
        for (int x = 0; x < MAP_WIDTH; x++) {
            for (int y = 0; y < MAP_HEIGHT; y++) {
                Position coords = getIsometricToCoords(x, y);
                Tile tile = map[x][y];
                entityBuilder(coords.x(), coords.y(), x + y, 0, getTileTextureName(tile.getBiome(), tile.getHeight()));
                if (map[x][y].hasTree()) {
                    entityBuilder(coords.x(), coords.y(), x + y, 4, "features/tree.png");
                }
                renderBuilding(tile, x, y);
            }
        }
    }

    private void renderBuilding(Tile tile, int x, int y) {
        Building building = tile.getBuilding();
        Position coords = getIsometricToCoords(x, y);
        if (building != null) {
            switch (building) {
                case Castle ignored -> entityBuilder(coords.x(), coords.y(), x + y, 4, "buildings/castle.png");
                case WizardsTower wizardsTower -> {
                    String magic = wizardsTower.getMagic().toString();
                    String assetName = String.format("buildings/wizardtowers/%stower.png", magic.substring(0, magic.length() - 5));
                    entityBuilder(coords.x(), coords.y(), x + y, 4, assetName);
                }
                case Barrack ignored -> entityBuilder(coords.x(), coords.y(), x + y, 4, "buildings/barrack.png");
                case Farmland ignored -> entityBuilder(coords.x(), coords.y(), x + y, 4, "buildings/farmland.png");
                case LumberHut ignored -> entityBuilder(coords.x(), coords.y(), x + y, 3, "buildings/lumberhut.png");
                case Mine ignored ->
                        entityBuilder(coords.x(), coords.y(), x + y, 4, String.format("buildings/mine_%s.png", tile.getHeight().toString()));
                case MysticalContainer container -> {
                    String name = container.getName().replace(' ', '_').toLowerCase();
                    System.out.println(name);
                    entityBuilder(coords.x(), coords.y(), x + y, 4, String.format("buildings/mystical/%s.png", name));
                }
                default -> {
                }
            }
        }
    }

    private void renderHighlightedTile(int x, int y) {
        if (this.highlightedTile != null) {
            this.highlightedTile.entity().removeFromWorld();
        }
        if (x < 0 || x >= MAP_WIDTH || y < 0 || y >= MAP_HEIGHT) {
            this.highlightedTile = null;
            this.updateTileMenu();
            return;
        }

        Position isoCoords = getIsometricToCoords(x, y);
        this.highlightedTile = new RenderTile(
                this.map[x][y],
                new Position(x, y),
                entityBuilder(isoCoords.x(), isoCoords.y(), x + y, 1, "tiles/selected_tile.png")
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


    @Override
    protected void initUI() {
        stringBuilderUI(110, "wealth", "Wealth: %d$");
        stringBuilderUI(130, "wood", "Wood: %d");
        stringBuilderUI(150, "stone", "Stone: %d");
        stringBuilderUI(170, "food", "Food: %d");
        stringBuilderUI(190, "iron", "Iron: %d");

        tileMenu = new VBox(10);
        tileMenu.setTranslateX(SCREEN_WIDTH - 300);
        tileMenu.setTranslateY(100);
        tileMenu.setAlignment(Pos.TOP_CENTER);
        tileMenu.setFillWidth(true);
        tileMenu.setMinWidth(200);
        getGameScene().addUINode(tileMenu);

        Image image = textureCache.computeIfAbsent("ui/end_turn.png", file -> getAssetLoader().loadImage(file));
        Texture texture = new Texture(image);
        texture.hoverProperty().addListener((observable, oldValue, newValue) -> {
            Image img;
            if (newValue) {
                img = textureCache.computeIfAbsent("ui/end_turn_hovered.png", file -> getAssetLoader().loadImage(file));
            } else {
                img = textureCache.computeIfAbsent("ui/end_turn.png", file -> getAssetLoader().loadImage(file));
            }
            texture.setImage(img);
        });
        texture.setOnMouseClicked(mouseEvent -> {
            System.out.println("selected");
            texture.setImage(textureCache.computeIfAbsent("ui/end_turn_selected.png", file -> getAssetLoader().loadImage(file)));
        });
        texture.setX(SCREEN_WIDTH - 230);
        texture.setY(SCREEN_HEIGHT - 160);
        addUINode(texture);
    }

    @Override
    protected void initInput() {
        Input input = getInput();

        FXGL.onBtn(MouseButton.PRIMARY, () -> {
            Position pos = getCoordsToIsometric(input.getMouseXWorld(), input.getMouseYWorld());
            renderHighlightedTile(pos.x(), pos.y());
        });

        FXGL.onKeyUp(KeyCode.F11, ()->{
            getSettings().getFullScreen().set(!getSettings().getFullScreen().get());
            getGameScene().getViewport().setHeight(SCREEN_HEIGHT);
            getGameScene().getViewport().setWidth(SCREEN_WIDTH);
        });

        FXGL.onKey(KeyCode.DOWN, () -> {
            Viewport viewPort = getGameScene().getViewport();
            viewPort.setY(viewPort.getY() + 1);
        });
        FXGL.onKey(KeyCode.UP, () -> {
            Viewport viewPort = getGameScene().getViewport();
            viewPort.setY(viewPort.getY() - 1);
        });
        FXGL.onKey(KeyCode.RIGHT, () -> {
            Viewport viewPort = getGameScene().getViewport();
            viewPort.setX(viewPort.getX() + 1);
        });
        FXGL.onKey(KeyCode.LEFT, () -> {
            Viewport viewPort = getGameScene().getViewport();
            viewPort.setX(viewPort.getX() - 1);
        });
        FXGL.onKey(KeyCode.Z, () -> {
            Viewport viewPort = getGameScene().getViewport();
            viewPort.setZoom(viewPort.getZoom() + .01);
        });
        FXGL.onKey(KeyCode.X, () -> {
            Viewport viewPort = getGameScene().getViewport();
            viewPort.setZoom(viewPort.getZoom() - .01);
        });
    }

    @Override
    protected void onUpdate(double tpf) {
        FXGL.getWorldProperties().setValue("wealth", this.player.getWealth());
        FXGL.getWorldProperties().setValue("food", this.player.getFoodCount());
        FXGL.getWorldProperties().setValue("wood", this.player.getWoodCount());
        FXGL.getWorldProperties().setValue("iron", this.player.getIronCount());
        FXGL.getWorldProperties().setValue("stone", this.player.getStoneCount());
    }

    private void updateTileMenu() {


        this.tileMenu.getChildren().clear();
        if (this.highlightedTile == null) {
            return;
        }
        Tile tile = this.highlightedTile.tile();

        if (tile.getBuilding() != null) {
            Button buildingInfo = new Button("View Building");
            buildingInfo.setMinWidth(200);

            buildingInfo.setOnAction(e -> System.out.println("Building: " + tile.getBuilding().getClass().getSimpleName()));
            this.tileMenu.getChildren().add(buildingInfo);
        } else {
            for (BuildingFactory factory : Building.getAllowedBuildingsToBuild(tile)) {
                // <-- here
                Button btn = new Button("Build " + factory.create(null, null).getClass().getSimpleName());
                btn.setMinWidth(200);
                btn.setOnAction(e -> {
                    this.map[this.highlightedTile.position().x()][this.highlightedTile.position().y()].setBuilding(
                            factory.create(this.player, this.highlightedTile.position())
                    );
                    renderBuilding(tile, this.highlightedTile.position().x(), this.highlightedTile.position().y());
                    updateTileMenu();
                });
                this.tileMenu.getChildren().add(btn);
            }
        }

        if (tile.getSoldier() != null) {
            Button soldierInfo = new Button("View Soldier");
            soldierInfo.setMinWidth(200);
            soldierInfo.setOnAction(e -> System.out.println("Soldier: " + tile.getSoldier().toString()));

            Button moveSoldier = new Button("Move Soldier");
            moveSoldier.setMinWidth(200);
            moveSoldier.setOnAction(e -> System.out.println("Moving soldier..."));

            this.tileMenu.getChildren().addAll(soldierInfo, moveSoldier);
        }
    }

    private static Entity entityBuilder(int x, int y, int z, int level, String spriteFile) {
        Image image = textureCache.computeIfAbsent(spriteFile, file -> getAssetLoader().loadImage(file));

        Texture texture = new Texture(image);
        texture.setSmooth(false);
        return FXGL.entityBuilder()
                .at(x, y)
                .view(texture)
                .zIndex(5 * z + level)
                .buildAndAttach();
    }

    private static void stringBuilderUI(int y, String property, String value) {
        Text textPixels = new Text();
        textPixels.setTranslateX(100);
        textPixels.setTranslateY(y);
        textPixels.setTextAlignment(TextAlignment.LEFT);
        textPixels.setFill(Color.WHITE);

        textPixels.textProperty().bind(FXGL.getWorldProperties()
                                               .intProperty(property)
                                               .asString(value));
        getGameScene().addUINode(textPixels);
    }

    public static void main(String[] args) {
        launch(args);
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
}