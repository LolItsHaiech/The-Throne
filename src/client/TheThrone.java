package client;

import client.scenes.LoginSubScene;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.paint.*;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;


public class TheThrone extends GameApplication {

    private static final int SCREEN_WIDTH = 1280;
    private static final int SCREEN_HEIGHT = 720;


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



        FXGL.runOnce(() -> FXGL.getSceneService().pushSubScene(new LoginSubScene()), javafx.util.Duration.ZERO);
    }

    public static void main(String[] args) {
        launch(args);
    }
}