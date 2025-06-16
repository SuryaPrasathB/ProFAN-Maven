package com.tasnetwork.calibration.energymeter.testprofiles ;


//import com.tasnetwork.spring.orm.model.ReportProfileTestDataFilter;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class HarmonicsSelectOrder_Y_Phase_V_CheckBoxValueFactory  implements Callback<TableColumn.CellDataFeatures<HarmonicsDataModel, CheckBox>, ObservableValue<CheckBox>> {
	//ArrayList<String> checkBoxDisableList = new ArrayList<String> (Arrays.asList(ConstantReportV2.REPORT_SERIAL_NO,
	//	ConstantReportV2.REPORT_METER_SERIAL_NO));
	@Override
	public ObservableValue<CheckBox> call(CellDataFeatures<HarmonicsDataModel, CheckBox> param) {
		HarmonicsDataModel rowData = param.getValue();
		CheckBox checkBox = new CheckBox();
		//checkBox.setText("Populate");
		//checkBox.setAlignment(Pos.CENTER);

		//setAlignment(Pos.CENTER);
		// setGraphic(checkBox);
		checkBox.selectedProperty().setValue(rowData.isHarmonicOrder_V_Selected());
		//checkBox.selectedProperty().setValue(rowData.isHarmonicOrder_I_Selected());
		//checkBox.setText(ConstantCurrentInjection.PROBE_NUMBER_PREFIX + String.valueOf(rowData.getSerialNo()) );
		checkBox.selectedProperty().addListener((ov, old_val, new_val) -> {
			rowData.setHarmonicOrder_V_Selected(new_val);
			if(new_val){
				//rowData.getProbeSelectionType()
			}else{
				
			}	
		});
		return new SimpleObjectProperty<>(checkBox);
	}

}
