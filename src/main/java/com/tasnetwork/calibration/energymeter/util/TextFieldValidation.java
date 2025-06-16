package com.tasnetwork.calibration.energymeter.util;

import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Popup;

public class TextFieldValidation {
	
	public void addModelNameValidation(TextField field, String label) {
	    Popup validationPopup = new Popup();
	    Text validationText = new Text();

	    StackPane popupContent = new StackPane(validationText);
	    popupContent.setBackground(new Background(new BackgroundFill(Color.web("#ffefef"), new CornerRadii(8), Insets.EMPTY)));
	    popupContent.setPadding(new Insets(6));
	    popupContent.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
	    validationText.setFill(Color.RED);
	    validationText.setFont(Font.font("Arial", 12));
	    validationPopup.getContent().add(popupContent);
	    validationPopup.setAutoHide(true);

	    field.textProperty().addListener((obs, oldVal, newVal) -> {
	        String message = null;

	        if (newVal.isEmpty()) {
	            message = label + " cannot be empty.";
	        } else if (!newVal.matches("^[A-Za-z_][A-Za-z0-9_]*$")) {
	            if (!Character.isLetter(newVal.charAt(0)) && newVal.charAt(0) != '_') {
	                message = "First character must be a letter or underscore.";
	            } else if (newVal.contains(" ")) {
	                message = "Spaces are not allowed.";
	            } else if (!newVal.matches("[A-Za-z0-9_]*")) {
	                message = "Only letters, digits, and underscores are allowed.";
	            }
	        }

	        if (message != null) {
	            field.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
	            validationText.setText(message);

	            if (!validationPopup.isShowing()) {
	                double x = field.localToScreen(field.getBoundsInLocal()).getMinX();
	                double y = field.localToScreen(field.getBoundsInLocal()).getMinY() - 35;
	                validationPopup.show(field, x, y);
	            }
	        } else {
	            field.setStyle(""); // Reset the border style
	            validationPopup.hide();
	        }
	    });
	}

	/**
	 * Adds numeric input validation to a TextField that only allows values between 1 and 240.
	 * If the field is empty, not numeric, or out of range, a pop-up error message is shown.
	 *
	 * @param field The TextField to apply validation on.
	 * @param label The label used in the validation message for user clarity.
	 */
	public void addNumericRangeValidation(TextField field, String label) {
	 // Create a pop-up to show validation error
	    Popup validationPopup = new Popup();
	    Text validationText = new Text();

	 // Style and configure the pop-up content
	    StackPane popupContent = new StackPane(validationText);
	    popupContent.setBackground(new Background(new BackgroundFill(Color.web("#ffefef"), new CornerRadii(8), Insets.EMPTY)));
	    popupContent.setPadding(new Insets(6));
	    popupContent.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
	    validationText.setFill(Color.RED);
	    validationText.setFont(Font.font("Arial", 12));
	    validationPopup.getContent().add(popupContent);
	    validationPopup.setAutoHide(true);

	 // Listener for text changes in the field
	    field.textProperty().addListener((obs, oldVal, newVal) -> {
	        String message = null;

	     // Validation rules
	        if (newVal.isEmpty()) {
	            message = label + " cannot be empty.";
	        } else if (!newVal.matches("\\d+")) {
	            message = label + " must be a number.";
	        } else {
	            int value = Integer.parseInt(newVal);
	            if (value < 1 || value > 240) {
	                message = label + " must be between 1 and 240.";
	            }
	        }

	        if (message != null) {
	         // Set red border and show pop-up
	            field.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
	            validationText.setText(message);

	            if (!validationPopup.isShowing()) {
	                double x = field.localToScreen(field.getBoundsInLocal()).getMinX();
	                double y = field.localToScreen(field.getBoundsInLocal()).getMinY() - 35;
	                validationPopup.show(field, x, y);
	            }
	        } else {
	         // Clear styles and hide pop-up on valid input
	            field.setStyle("");
	            validationPopup.hide();
	        }
	    });
	}
	
	/**
	 * Adds numeric input validation to a TextField that allows values from 0 to 240.
	 * Similar to the above method, but includes 0 as a valid entry.
	 *
	 * @param field The TextField to apply validation on.
	 * @param label The label used in the validation message for user clarity.
	 */
	public void addNumericRangeValidationAllowZero(TextField field, String label) {
	    Popup validationPopup = new Popup();
	    Text validationText = new Text();

	    StackPane popupContent = new StackPane(validationText);
	    popupContent.setBackground(new Background(new BackgroundFill(Color.web("#ffefef"), new CornerRadii(8), Insets.EMPTY)));
	    popupContent.setPadding(new Insets(6));
	    popupContent.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
	    validationText.setFill(Color.RED);
	    validationText.setFont(Font.font("Arial", 12));
	    validationPopup.getContent().add(popupContent);
	    validationPopup.setAutoHide(true);

	    field.textProperty().addListener((obs, oldVal, newVal) -> {
	        String message = null;

	        if (newVal.isEmpty()) {
	            message = label + " cannot be empty.";
	        } else if (!newVal.matches("\\d+")) {
	            message = label + " must be a number.";
	        } else {
	            int value = Integer.parseInt(newVal);
	            if (value < 0 || value > 240) {
	                message = label + " must be between 0 and 240.";
	            }
	        }

	        if (message != null) {
	            field.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
	            validationText.setText(message);

	            if (!validationPopup.isShowing()) {
	                double x = field.localToScreen(field.getBoundsInLocal()).getMinX();
	                double y = field.localToScreen(field.getBoundsInLocal()).getMinY() - 35;
	                validationPopup.show(field, x, y);
	            }
	        } else {
	            field.setStyle("");
	            validationPopup.hide();
	        }
	    });
	}
	
	/**
	 * Adds validation to ensure a TextField contains a valid identifier-like name.
	 * Rules: must start with a letter or underscore, followed by letters, digits, or underscores.
	 * Invalid input shows a styled tool-tip.
	 *
	 * @param field The TextField to validate.
	 * @param label The label used in error messages.
	 */
	public void addTextValidation(TextField field, String label) {
	    Tooltip tooltip = new Tooltip();
	    tooltip.setStyle("-fx-background-color: #ffdddd; -fx-text-fill: red; -fx-font-size: 12;");
	    tooltip.setAutoHide(true);

	    field.textProperty().addListener((obs, oldText, newText) -> {
	        // Validation: Only letters, digits, and underscore; no spaces; no number at the start
	        boolean isValid = newText.matches("^[A-Za-z_][A-Za-z0-9_]*$");

	        if (newText.isEmpty()) {
	        	// Reset style if empty
	            field.setStyle(""); // Clear styles if empty
	            tooltip.hide();
	        } else if (!isValid) {
	        	// Invalid input styling and tooltip message
	            field.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
	            tooltip.setText(label + " must start with a letter or underscore and contain only letters, numbers, or underscores.");
	            if (!tooltip.isShowing()) {
	                Bounds bounds = field.localToScreen(field.getBoundsInLocal());
	                tooltip.show(field, bounds.getMinX(), bounds.getMinY() - 30);
	            }
	        } else {
	        	// Valid input
	            field.setStyle(""); // Valid input: clear error styles
	            tooltip.hide();
	        }
	    });
	}
}
