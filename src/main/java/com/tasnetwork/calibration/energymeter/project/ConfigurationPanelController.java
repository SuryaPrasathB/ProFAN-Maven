package com.tasnetwork.calibration.energymeter.project;

import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.util.TextFieldValidation;
import com.tasnetwork.spring.orm.model.DutMasterData;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ConfigurationPanelController {

	@FXML private ComboBox<String> cmbBxPhaseSelection;
    @FXML private TextField txtAreaOfOpening;
    @FXML private TextField txtWindSpeed;
    
    private String modelName;
    private DeviceDataManagerController displayDataObj;
    private final TextFieldValidation validator = new TextFieldValidation();
    private Stage currentStage;
    
    public void setInitialModelName(String modelName) {
        this.modelName = modelName;
    }

    public void setDisplayDataObj(DeviceDataManagerController displayDataObj) {
        this.displayDataObj = displayDataObj;
    }

    public void setCurrentStage(Stage stage) {
        this.currentStage = stage;
    }
    
    @FXML
    public void initialize() {
        cmbBxPhaseSelection.getItems().addAll("1", "3");
        
     // Apply validations using your utility class
        validator.addNumericRangeValidation(txtAreaOfOpening, "Area of Opening");
        validator.addNumericRangeValidation(txtWindSpeed, "Wind Speed");
    }

    @FXML
    void configSaveOnClick(ActionEvent event) {
        String phase = cmbBxPhaseSelection.getValue();
        String areaOfOpening = txtAreaOfOpening.getText().trim();
        String windSpeed = txtWindSpeed.getText().trim();

        if (phase == null || areaOfOpening.isEmpty() || windSpeed.isEmpty() || 
        !areaOfOpening.matches("\\d+") || !windSpeed.matches("\\d+")){
            Alert alert = new Alert(Alert.AlertType.WARNING, "All fields are required.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        // You can parse and validate area/windSpeed if needed
        DutMasterData model = new DutMasterData();
        model.setModelName(modelName);
        model.setPhase(phase);
        model.setAreaOfOpening(areaOfOpening);
        model.setWindSpeedConfig(windSpeed);
        model.setActive(true);

        DeviceDataManagerController.getDutMasterDataService().saveToDb(model);

        // Close window
        Stage stage = (Stage) cmbBxPhaseSelection.getScene().getWindow();
        stage.close();

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Model saved successfully!", ButtonType.OK);
        alert.show();
        
        if (currentStage != null) {
            currentStage.close();
        }
        
     // Add model to the ComboBox
        FanProjectSetupController.getRef_cmbBxModelName().getItems().add(modelName);
        FanProjectSetupController.getRef_cmbBxModelName().getSelectionModel().select(modelName);
    }

    @FXML
    void configCancelOnClick(ActionEvent event) {
        Stage stage = (Stage) cmbBxPhaseSelection.getScene().getWindow();
        stage.close();
    }
}
