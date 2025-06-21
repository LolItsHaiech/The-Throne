package client.config.widgets;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class BuildingTabController {
    @FXML private Label buildingNameLabel;
    @FXML private TextField woodField;
    @FXML private TextField foodField;
    @FXML private TextField stoneField;
    @FXML private TextField wealthField;
    
    private BuildingWidget building;
    
    public void setBuilding(BuildingWidget building) {
        this.building = building;
        buildingNameLabel.setText(building.getName());
        
        // Bind fields to building properties
        woodField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.matches("\\d*")) {
                building.setWood(newVal.isEmpty() ? 0 : Integer.parseInt(newVal));
            } else {
                woodField.setText(oldVal);
            }
        });
        
        // Similar listeners for other fields
        foodField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.matches("\\d*")) {
                building.setFood(newVal.isEmpty() ? 0 : Integer.parseInt(newVal));
            } else {
                foodField.setText(oldVal);
            }
        });
        
        stoneField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.matches("\\d*")) {
                building.setStone(newVal.isEmpty() ? 0 : Integer.parseInt(newVal));
            } else {
                stoneField.setText(oldVal);
            }
        });
        
        wealthField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.matches("\\d*")) {
                building.setWealth(newVal.isEmpty() ? 0 : Integer.parseInt(newVal));
            } else {
                wealthField.setText(oldVal);
            }
        });
    }
    
    public BuildingWidget getBuilding() {
        return this.building;
    }
}