package com.tasnetwork.calibration.energymeter.deployment;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class DeploymentDevicesCheckBoxValueFactory implements Callback<TableColumn.CellDataFeatures<DeploymentDataModel, CheckBox>, ObservableValue<CheckBox>> {
   
	@Override
    public ObservableValue<CheckBox> call(CellDataFeatures<DeploymentDataModel, CheckBox> param) {
		DeploymentDataModel rowData = param.getValue();
        CheckBox checkBox = new CheckBox();
        checkBox.selectedProperty().setValue(rowData.getIsSelected());
        checkBox.selectedProperty().addListener((ov, old_val, new_val) -> {
        	rowData.setIsSelected(new_val);

        });
        //checkBox.setAlignment(Pos.CENTER);
        return new SimpleObjectProperty<>(checkBox);
    }


}
