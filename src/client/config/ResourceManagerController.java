package client.config;

import client.config.widgets.BuildingTabController;
import client.config.widgets.BuildingWidget;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResourceManagerController {
    @FXML private TabPane tabPane;
    @FXML private Button saveButton;
    
    private List<BuildingWidget> buildings = new ArrayList<>();
    
    public void initialize() {
        // Initialize buildings
        String[] buildingNames = {"Barrack", "Castle", "Farmland", "Church", "Market"}; // Easily expandable
        for (String name : buildingNames) {
            buildings.add(new BuildingWidget(name));
        }
        
        // Create tabs dynamically
        for (BuildingWidget building : buildings) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("building_tab.fxml"));
                VBox tabContent = loader.load();
                
                BuildingTabController controller = loader.getController();
                controller.setBuilding(building);
                
                Tab tab = new Tab(building.getName(), tabContent);
                tabPane.getTabs().add(tab);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    @FXML
    private void handleSave() {
        StringBuilder savedData = new StringBuilder("Saved Data:\n");
        
        for (BuildingWidget building : buildings) {
            savedData.append(String.format("%s - Wood: %d, Food: %d, Stone: %d, Wealth: %d\n",
                building.getName(),
                building.getWood(),
                building.getFood(),
                building.getStone(),
                building.getWealth()));
        }
        
        showAlert("Save Successful", savedData.toString());
        
        // In a real app, you would save to file/database here
        System.out.println(savedData);
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}