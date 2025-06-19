package com.tasnetwork.calibration.energymeter.project;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class SimulationConfigController {

    @FXML
    private CheckBox simulationModeCheckBox;

    private boolean initialSimulationMode;
    private Consumer<Boolean> simulationModeUpdateCallback;

    /**
     * Sets the initial state of the checkbox based on the current simulation mode.
     * @param isSimulationMode True if simulation mode is currently active, false otherwise.
     */
    public void setCurrentSimulationMode(boolean isSimulationMode) {
        this.initialSimulationMode = isSimulationMode;
        simulationModeCheckBox.setSelected(isSimulationMode);
    }

    /**
     * Sets the callback function to be invoked when the simulation mode is updated.
     * @param callback The Consumer to accept the new simulation mode (Boolean).
     */
    public void setSimulationModeUpdateCallback(Consumer<Boolean> callback) {
        this.simulationModeUpdateCallback = callback;
    }

    /**
     * Handles the Save button action. Updates the simulation mode via the callback
     * and closes the dialog.
     */
    @FXML
    private void handleSave() {
        if (simulationModeUpdateCallback != null) {
            simulationModeUpdateCallback.accept(simulationModeCheckBox.isSelected());
        }
        closeDialog();
    }

    /**
     * Handles the Cancel button action. Closes the dialog without saving changes.
     */
    @FXML
    private void handleCancel() {
        closeDialog();
    }

    /**
     * Helper method to close the dialog.
     */
    private void closeDialog() {
        Stage stage = (Stage) simulationModeCheckBox.getScene().getWindow();
        stage.close();
    }
}