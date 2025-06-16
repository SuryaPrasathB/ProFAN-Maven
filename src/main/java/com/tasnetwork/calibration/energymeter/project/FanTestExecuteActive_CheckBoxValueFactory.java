/*package com.tasnetwork.calibration.energymeter.project;

import com.tasnetwork.spring.orm.model.FanTestExecute;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class FanTestExecuteActive_CheckBoxValueFactory implements Callback<TableColumn.CellDataFeatures<FanTestExecute, CheckBox>, ObservableValue<CheckBox>> {

	@Override
	public ObservableValue<CheckBox> call(CellDataFeatures<FanTestExecute, CheckBox> param) {
		FanTestExecute rowData = param.getValue();
		CheckBox checkBox = new CheckBox();
		//checkBox.setDisable(true);
		checkBox.selectedProperty().setValue(rowData.getActive());

		checkBox.selectedProperty().addListener((ov, old_val, new_val) -> {
			rowData.setActive(new_val);
		});

		return new SimpleObjectProperty<>(checkBox);
	}


}*/
