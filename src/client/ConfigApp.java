package client;

import db.Config;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import util.MaterialCost;

import java.io.IOException;
import java.lang.reflect.Field;

import util.map.Map;

public class ConfigApp extends Application {

    private final Map<String, MaterialCostSupplier> fieldEditors = new Map<>();


    @Override
    public void start(Stage stage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        ScrollPane scrollPane = new ScrollPane();
        VBox content = new VBox(10);
        scrollPane.setContent(content);
        scrollPane.setFitToWidth(true);

        Label titleLabel = new Label("Material Costs");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        content.getChildren().add(titleLabel);

        // Use reflection to dynamically create UI for all MaterialCost fields
        Field[] fields = Config.WeaponCosts.class.getFields();

        for (Field field : fields) {
            try {
                MaterialCost cost = (MaterialCost) field.get(Config.instance.weaponCosts);
                if (cost == null) continue;

                GridPane costGrid = new GridPane();
                costGrid.setHgap(5);
                costGrid.setVgap(5);
                costGrid.setPadding(new Insets(5));
                costGrid.setStyle("-fx-border-color: lightgray; -fx-border-width: 1; -fx-padding: 5;");

                Label sectionLabel = new Label(field.getName());
                sectionLabel.setStyle("-fx-font-weight: bold;");

                costGrid.add(sectionLabel, 0, 0, 5, 1);

                TextField wealthField = new TextField(String.valueOf(cost.wealth()));
                TextField stoneField = new TextField(String.valueOf(cost.stone()));
                TextField woodField = new TextField(String.valueOf(cost.wood()));
                TextField foodField = new TextField(String.valueOf(cost.food()));
                TextField ironField = new TextField(String.valueOf(cost.iron()));

                costGrid.add(new Label("Wealth:"), 0, 1);
                costGrid.add(wealthField, 1, 1);
                costGrid.add(new Label("Stone:"), 2, 1);
                costGrid.add(stoneField, 3, 1);

                costGrid.add(new Label("Wood:"), 0, 2);
                costGrid.add(woodField, 1, 2);
                costGrid.add(new Label("Food:"), 2, 2);
                costGrid.add(foodField, 3, 2);

                costGrid.add(new Label("Iron:"), 0, 3);
                costGrid.add(ironField, 1, 3);

                content.getChildren().add(costGrid);

                // Store updates to apply on save
                fieldEditors.set(field.getName(), () -> new MaterialCost(
                        parseInt(wealthField.getText()),
                        parseInt(stoneField.getText()),
                        parseInt(woodField.getText()),
                        parseInt(foodField.getText()),
                        parseInt(ironField.getText())
                ));

            } catch (IllegalAccessException ignored) {
            }
        }

        Button saveButton = new Button("Save Config");
        saveButton.setOnAction(e -> saveConfig());
        content.getChildren().add(saveButton);

        root.getChildren().add(scrollPane);

        stage.setTitle("Config");
        stage.setScene(new Scene(root, 600, 700));
        stage.show();
    }


    private void saveConfig() {
        for (Field field : Config.WeaponCosts.class.getFields()) {
            try {
                field.set(Config.instance.weaponCosts, fieldEditors.get(field.getName()).get());
            } catch (IllegalAccessException e) {
                showError("Failed to update field: " + field.getName());
                return;
            }
        }

        try {
            Config.instance.save();
            this.showInfo("Successfully saved");
        } catch (IOException e) {
            showError("Failed to save config: \n" + e.getMessage());
        }
    }

    private int parseInt(String text) {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle("Success");
        alert.setHeaderText("Success");
        alert.showAndWait();
    }


    public static void main(String[] args) {
        launch(args);
    }

    @FunctionalInterface
    private interface MaterialCostSupplier {
        MaterialCost get();
    }
}
