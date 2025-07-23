package client.scenes;

import com.almasb.fxgl.scene.SubScene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import obj.auth.User;
import obj.game.Game;
import util.LinkedList;

public class GamesMenuSubScene extends SubScene {
    private User user;

    private VBox root;
    private ScrollPane scrollPane;

    public GamesMenuSubScene(User user) {
        this.user = user;

        root = new VBox();
        scrollPane = new ScrollPane();

        scrollPane.setMaxWidth(800);
        VBox content = new VBox();

        LinkedList<Game> games = Game.getAllGames(user);
        for (Game game : games) {
            HBox hBox = new HBox();

            content.getChildren().add(hBox);
        }

        // todo

        scrollPane.setContent(content);
        root.getChildren().add(scrollPane);
        getContentRoot().getChildren().add(root);
    }
}
