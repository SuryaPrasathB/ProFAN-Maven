package com.tasnetwork.calibration.energymeter.project;

import com.tasnetwork.calibration.energymeter.util.Settings;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class SettingsPanelController {

    @FXML private TextField txtVoltageTolerance;
    @FXML private TextField txtSetupDelay;
    @FXML private TextField txtMaxRetryAttempts;
    @FXML private TextField txtRetryDelay;
    @FXML private TextField txtLoopDelay;
    @FXML private TextField txtLoggingFrequency;

    @FXML
    private void initialize() {
        txtVoltageTolerance.setText(String.valueOf(Settings.voltageTolerance));
        txtSetupDelay.setText(String.valueOf(Settings.setupDelay));
        txtMaxRetryAttempts.setText(String.valueOf(Settings.maxRetryAttempts));
        txtRetryDelay.setText(String.valueOf(Settings.retryDelay));
        txtLoopDelay.setText(String.valueOf(Settings.loopDelay));
        txtLoggingFrequency.setText(String.valueOf(Settings.loggingFrequency));
    }

    @FXML
    private void onSaveClick() {
        try {
            Settings.voltageTolerance = Double.parseDouble(txtVoltageTolerance.getText());
            Settings.setupDelay = Integer.parseInt(txtSetupDelay.getText());
            Settings.maxRetryAttempts = Integer.parseInt(txtMaxRetryAttempts.getText());
            Settings.retryDelay = Integer.parseInt(txtRetryDelay.getText());
            Settings.loopDelay = Integer.parseInt(txtLoopDelay.getText());
            Settings.loggingFrequency = Integer.parseInt(txtLoggingFrequency.getText());

            Settings.save();
        } catch (NumberFormatException e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }
}
