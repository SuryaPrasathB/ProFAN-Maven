package com.tasnetwork.calibration.energymeter.util;

import java.util.*;
import java.util.regex.Pattern; // Import Pattern for regex validation

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

import javafx.beans.value.ChangeListener;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color; // Import Color for warning text
import javafx.stage.*;

/**
 * A simple non-modal popup that dynamically builds N textfields for wind-speed input,
 * and returns the average of the entered numeric values when the user clicks Save.
 */
public class ManualReadingsInputPopup {

    private final Stage stage;
    private final List<TextField> fields = new ArrayList<>();
    // A map to associate each TextField with its corresponding warning Label
    private final Map<TextField, Label> warningLabels = new HashMap<>();

    // Declare btnSave as a member variable
    private final Button btnSave;

    // Regex to allow optional negative sign, digits, and an optional decimal point followed by digits
    private static final Pattern NUMERIC_PATTERN = Pattern.compile("-?\\d*\\.?\\d*");

    public ManualReadingsInputPopup(int count, String title) { // <<<--- MODIFIED: Added 'title' parameter
        this.stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL); // BLOCK all interaction until closed via allowed action
        stage.setTitle(title); // <<<--- MODIFIED: Use the passed title

        VBox root = new VBox(10);
        root.setPadding(new Insets(15)); // Set padding for the root VBox
        root.setPrefWidth(400);
        root.setPrefHeight(400);
        // Set the background of the root VBox to black
        //root.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY))); // <<<--- ADDED/MODIFIED LINE

        // Initialize btnSave here in the constructor
        this.btnSave = new Button("Save & Next");
        btnSave.setDisable(true); // initially disabled

        for (int i = 1; i <= count; i++) {
            TextField tf = new TextField();
            tf.setPromptText("Reading " + i);
            // Optionally, set text field background to white for contrast on black background
            tf.setStyle("-fx-control-inner-background: white; -fx-text-fill: black;"); // <<<--- ADDED/MODIFIED LINE
            fields.add(tf);

            Label warningLabel = new Label("Only numbers allowed!");
            warningLabel.setTextFill(Color.RED);
            warningLabel.setVisible(false); // Initially hidden
            warningLabels.put(tf, warningLabel); // Store the warning label

            Label readingLabel = new Label("Reading " + i + ":");
            readingLabel.setTextFill(Color.BLACK); // <<<--- ADDED/MODIFIED LINE: Set label text color to white for contrast
            HBox inputRow = new HBox(5, readingLabel, tf);
            inputRow.setAlignment(Pos.CENTER_LEFT); // Align elements in the HBox

            VBox fieldContainer = new VBox(2, inputRow, warningLabel); // Group input and warning
            root.getChildren().add(fieldContainer);

            // --- Input Validation Listener ---
            tf.textProperty().addListener((obs, oldValue, newValue) -> {
                Label currentWarningLabel = warningLabels.get(tf);
                if (newValue == null || newValue.isEmpty()) {
                    currentWarningLabel.setVisible(false); // Hide warning if empty
                    tf.setText(""); // Ensure text is truly empty
                } else if (NUMERIC_PATTERN.matcher(newValue).matches()) {
                    // Check for multiple decimal points
                    if (newValue.indexOf('.') != newValue.lastIndexOf('.')) {
                        tf.setText(oldValue); // Revert to old value if multiple decimal points
                        currentWarningLabel.setText("Invalid number format (multiple decimals)");
                        currentWarningLabel.setVisible(true);
                    } else {
                        currentWarningLabel.setVisible(false); // Valid input, hide warning
                        // Allow valid numeric input to be set
                    }
                } else {
                    tf.setText(oldValue); // Revert to old value if non-numeric character is typed
                    currentWarningLabel.setText("Only numbers (0-9, decimal point, negative sign) allowed!"); // <<<--- MODIFIED: Clearer message
                    currentWarningLabel.setVisible(true);
                }

                // Re-validate all fields to enable/disable the Save button
                boolean allFieldsFilled = fields.stream().allMatch(f -> !f.getText().trim().isEmpty());
                // Also ensure no warnings are currently visible
                boolean noWarnings = warningLabels.values().stream().noneMatch(Label::isVisible);
                // Access btnSave directly as it's now a member variable
                btnSave.setDisable(!(allFieldsFilled && noWarnings));
            });
            // --- End Input Validation Listener ---
        }

        // This listener is now primarily for initial state and future dynamic changes
        // The individual TextField listeners handle ongoing validation
        ChangeListener<String> validationListener = (obs, oldVal, newVal) -> {
            boolean allFilled = fields.stream().allMatch(tf -> !tf.getText().trim().isEmpty());
            boolean noWarnings = warningLabels.values().stream().noneMatch(Label::isVisible);
            // Access btnSave directly as it's now a member variable
            btnSave.setDisable(!(allFilled && noWarnings));
        };
        fields.forEach(tf -> tf.textProperty().addListener(validationListener));


        btnSave.setOnAction(e -> {
            // Additional final check before closing
            boolean allFilled = fields.stream().allMatch(tf -> !tf.getText().trim().isEmpty());
            boolean noWarnings = warningLabels.values().stream().noneMatch(Label::isVisible);
            if (allFilled && noWarnings) {
                stage.close();
            } else {
                // Optionally show a general warning if they try to save with issues
                ApplicationLauncher.logger.warn("Cannot save: Please fill all fields with valid numbers.");
                // You could also add a general warning label to the root here
            }
        });

        // Disable window close (e.g., via 'X' button) unless all fields are filled AND no warnings
        stage.setOnCloseRequest(event -> {
            boolean allFilled = fields.stream().allMatch(tf -> !tf.getText().trim().isEmpty());
            boolean noWarnings = warningLabels.values().stream().noneMatch(Label::isVisible);
            if (!(allFilled && noWarnings)) {
                event.consume(); // prevent closing
                ApplicationLauncher.logger.warn("Please fill all fields with valid numbers before closing.");
                // Optionally show a temporary message to the user on the UI
            }
        });

        root.getChildren().add(btnSave);
        root.setAlignment(Pos.CENTER); // Center the content in the VBox
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
            } catch (NumberFormatException ignored) {
                // This catch block should ideally not be hit if validation works perfectly,
                // but it's a good fallback for robustness.
                ApplicationLauncher.logger.error("Failed to parse number from TextField: " + tf.getText());
            }
        }
        ApplicationLauncher.logger.debug("Average : " + (sum / validCount));
        return validCount > 0 ? sum / validCount : 0;

    }
}
