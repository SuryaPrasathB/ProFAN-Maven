package com.tasnetwork.calibration.energymeter.util;

import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.util.Callback;
import javafx.geometry.Pos;

import com.tasnetwork.spring.orm.model.FanTestSetup;

import javafx.beans.property.SimpleStringProperty;

public class SliderPopupCellFactory {

    public static Callback<TableColumn<FanTestSetup, String>, TableCell<FanTestSetup, String>> createSliderPopupCell(double min, double max, boolean isLowerLimit) {
        return col -> new TableCell<FanTestSetup, String>() {

            private final Label displayLabel = new Label();

            {
                displayLabel.setOnMouseClicked(event -> showSliderPopup(event));
                setGraphic(displayLabel);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    displayLabel.setText("");
                } else {
                    displayLabel.setText(item);
                }
            }

            private void showSliderPopup(MouseEvent event) {
                FanTestSetup testPoint = getTableView().getItems().get(getIndex());

                Slider slider = new Slider(min, max, 0);
                slider.setBlockIncrement(1);
                slider.setMajorTickUnit(20);
                slider.setMinorTickCount(4);
                slider.setShowTickMarks(true);
                slider.setShowTickLabels(true);
                slider.setSnapToTicks(true);

                // Set initial slider value
                double initialValue = 0;
                try {
                    String current = isLowerLimit ? testPoint.getRpmLowerLimit() : testPoint.getRpmUpperLimit();
                    initialValue = Double.parseDouble(current);
                } catch (Exception ignored) {}
                slider.setValue(initialValue);

                Button btnSet = new Button("Set");
                VBox content = new VBox(slider, btnSet);
                content.setSpacing(10);
                content.setAlignment(Pos.CENTER);
                content.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-color: gray;");

                Popup popup = new Popup();
                popup.getContent().add(content);
                popup.setAutoHide(true);

                btnSet.setOnAction(e -> {
                    String newVal = String.format("%.0f", slider.getValue());
                    if (isLowerLimit) {
                        testPoint.setRpmLowerLimit(newVal);
                    } else {
                        testPoint.setRpmUpperLimit(newVal);
                    }
                    displayLabel.setText(newVal);
                    popup.hide();
                });

                popup.show(displayLabel, event.getScreenX(), event.getScreenY());
            }
        };
    }
}

