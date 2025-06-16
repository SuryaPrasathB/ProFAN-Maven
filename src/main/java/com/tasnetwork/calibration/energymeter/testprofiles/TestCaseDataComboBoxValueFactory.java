package com.tasnetwork.calibration.energymeter.testprofiles;

import java.util.ArrayList;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.deployment.DeploymentDataModel;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class TestCaseDataComboBoxValueFactory implements Callback<TableColumn.CellDataFeatures<TestCaseData, ComboBox<String>>, ObservableValue<ComboBox<String>>> {
	   
		@Override
	    public ObservableValue<ComboBox<String>> call(CellDataFeatures<TestCaseData, ComboBox<String>> param) {
			TestCaseData rowData = param.getValue();
			ComboBox<String> Combo_Box = new ComboBox<String>();
			ArrayList<String> testruntype = new ArrayList<String>();
			/*if(ProcalFeatureEnable.RUN_TYPE_TIME_BASED_ENABLED){
				testruntype.add(ConstantApp.TESTPOINT_RUNTYPE_TIMEBASED);
			}*/
			testruntype.add(ConstantApp.TESTPOINT_RUNTYPE_PULSEBASED);
			if(ProcalFeatureEnable.RUN_TYPE_TIME_BASED_ENABLED){
				testruntype.add(ConstantApp.TESTPOINT_RUNTYPE_TIMEBASED);
			}
			Combo_Box.getItems().clear();
			Combo_Box.getItems().addAll(testruntype);
			Combo_Box.getSelectionModel().select(0);
			Combo_Box.setValue(rowData.getTestRunType());

			Combo_Box.setOnAction((e) -> {
	             ApplicationLauncher.logger.info(Combo_Box.getSelectionModel().getSelectedItem());
	             rowData.setTestRunType(Combo_Box.getSelectionModel().getSelectedItem());
	        });
	        
	        return new SimpleObjectProperty<>(Combo_Box);
	    }


	}
