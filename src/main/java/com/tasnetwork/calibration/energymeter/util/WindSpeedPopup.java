package com.tasnetwork.calibration.energymeter.util;

import java.util.*;

import com.sun.glass.ui.Application;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

import javafx.beans.value.ChangeListener;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

/**
 * A simple non-modal popup that dynamically builds N textfields for wind-speed input,
 * and returns the average of the entered numeric values when the user clicks Save.
 */
public class WindSpeedPopup {

    private final Stage stage;
    private final List<TextField> fields = new ArrayList<>();

    public WindSpeedPopup(int count) {
        this.stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL); // BLOCK all interaction until closed via allowed action
        stage.setTitle("Enter Wind Speed Readings");

        VBox root = new VBox(10);
        //List<TextField> fields = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            TextField tf = new TextField();
            tf.setPromptText("Reading " + i);
            fields.add(tf);
            root.getChildren().add(new HBox(5, new Label("Reading " + i + ":"), tf));
        }

        Button btnSave = new Button("Save & Next");
        btnSave.setDisable(true); // initially disabled

        // Check if all fields are filled and enable the button
        ChangeListener<String> validationListener = (obs, oldVal, newVal) -> {
            boolean allFilled = fields.stream().allMatch(tf -> !tf.getText().trim().isEmpty());
            btnSave.setDisable(!allFilled);
        };

        fields.forEach(tf -> tf.textProperty().addListener(validationListener));

        btnSave.setOnAction(e -> {
            // Additional final check before closing
            boolean allFilled = fields.stream().allMatch(tf -> !tf.getText().trim().isEmpty());
            if (allFilled) {
                stage.close();
            }
        });

        // Disable window close (e.g., via 'X' button) unless all fields are filled
        stage.setOnCloseRequest(event -> {
            boolean allFilled = fields.stream().allMatch(tf -> !tf.getText().trim().isEmpty());
            if (!allFilled) {
                event.consume(); // prevent closing
            }
        });

        root.getChildren().add(btnSave);
        root.setPadding(new Insets(15));
        stage.setScene(new Scene(root));
    }


    /**
     * Shows the popup and blocks only until the user clicks Save.
     *
     * @return the average of the numeric values entered, or 0 if none/invalid
     */
    public double showAndWaitAndReturnAverage() {
        stage.showAndWait();

        double sum = 0;
        int validCount = 0;
        ApplicationLauncher.logger.debug("FIELDS : " + fields);
        for (TextField tf : fields) {
            try {
                double v = Double.parseDouble(tf.getText().trim());
                ApplicationLauncher.logger.debug("Reading : " + v);
                sum += v;
                validCount++;
            } catch (NumberFormatException ignored) { }
        }
        ApplicationLauncher.logger.debug("Average : " + (sum / validCount));
        return validCount > 0 ? sum / validCount : 0;
        
    }
}

