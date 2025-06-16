package com.tasnetwork.calibration.energymeter.project;

import com.tasnetwork.spring.orm.model.FanTestSetup;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class FanTestSetupActive_CheckBoxValueFactory implements Callback<TableColumn.CellDataFeatures<FanTestSetup, CheckBox>, ObservableValue<CheckBox>> {
	   
		@Override
	    public ObservableValue<CheckBox> call(CellDataFeatures<FanTestSetup, CheckBox> param) {
			FanTestSetup rowData = param.getValue();
	        CheckBox checkBox = new CheckBox();
	        //checkBox.setDisable(true);
	        checkBox.selectedProperty().setValue(rowData.getActive());
	        
	        checkBox.selectedProperty().addListener((ov, old_val, new_val) -> {
	        	rowData.setActive(new_val);
	        });
	        
	        return new SimpleObjectProperty<>(checkBox);
	    }


	}
