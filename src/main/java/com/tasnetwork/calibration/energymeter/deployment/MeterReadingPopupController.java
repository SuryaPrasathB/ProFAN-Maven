

package com.tasnetwork.calibration.energymeter.deployment;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.database.MySQL_Controller;
import com.tasnetwork.calibration.energymeter.deployment.MeterParamsController;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;

public class MeterReadingPopupController implements Initializable {
	@FXML
	private TextField text_rack1;
	@FXML
	private TextField text_rack2;
	@FXML
	private TextField text_rack3;
	@FXML
	private TextField text_rack4;
	@FXML
	private TextField text_rack5;
	@FXML
	private TextField text_rack6;
	@FXML
	private TextField text_rack7;
	@FXML
	private TextField text_rack8;
	@FXML
	private TextField text_rack9;
	@FXML
	private TextField text_rack10;
	@FXML
	private TextField text_rack11;
	@FXML
	private TextField text_rack12;	
	@FXML
	private TextField text_rack13;
	@FXML
	private TextField text_rack14;
	@FXML
	private TextField text_rack15;
	@FXML
	private TextField text_rack16;
	@FXML
	private TextField text_rack17;
	@FXML
	private TextField text_rack18;
	@FXML
	private TextField text_rack19;
	@FXML
	private TextField text_rack20;
	@FXML
	private TextField text_rack21;
	@FXML
	private TextField text_rack22;
	@FXML
	private TextField text_rack23;
	@FXML
	private TextField text_rack24;
	
	@FXML
	private TextField text_rack25;
	@FXML
	private TextField text_rack26;
	@FXML
	private TextField text_rack27;
	@FXML
	private TextField text_rack28;
	@FXML
	private TextField text_rack29;
	@FXML
	private TextField text_rack30;
	@FXML
	private TextField text_rack31;
	@FXML
	private TextField text_rack32;
	@FXML
	private TextField text_rack33;
	@FXML
	private TextField text_rack34;	
	@FXML
	private TextField text_rack35;
	@FXML
	private TextField text_rack36;
	@FXML
	private TextField text_rack37;
	@FXML
	private TextField text_rack38;
	@FXML
	private TextField text_rack39;
	@FXML
	private TextField text_rack40;
	@FXML
	private TextField text_rack41;
	@FXML
	private TextField text_rack42;
	@FXML
	private TextField text_rack43;
	@FXML
	private TextField text_rack44;	
	@FXML
	private TextField text_rack45;
	@FXML
	private TextField text_rack46;
	@FXML
	private TextField text_rack47;
	@FXML
	private TextField text_rack48;
	
	public static  TextField ref_text_rack1;
	public static  TextField ref_text_rack2;
	public static  TextField ref_text_rack3;
	public static  TextField ref_text_rack4;
	public static  TextField ref_text_rack5;
	public static  TextField ref_text_rack6;
	public static  TextField ref_text_rack7;
	public static  TextField ref_text_rack8;
	public static  TextField ref_text_rack9;
	public static  TextField ref_text_rack10;
	public static  TextField ref_text_rack11;
	public static  TextField ref_text_rack12;
	public static  TextField ref_text_rack13;
	public static  TextField ref_text_rack14;
	public static  TextField ref_text_rack15;
	public static  TextField ref_text_rack16;
	public static  TextField ref_text_rack17;
	public static  TextField ref_text_rack18;
	public static  TextField ref_text_rack19;
	public static  TextField ref_text_rack20;
	public static  TextField ref_text_rack21;
	public static  TextField ref_text_rack22;
	public static  TextField ref_text_rack23;
	public static  TextField ref_text_rack24;
	
	public static  TextField ref_text_rack25;
	public static  TextField ref_text_rack26;
	public static  TextField ref_text_rack27;
	public static  TextField ref_text_rack28;
	public static  TextField ref_text_rack29;
	public static  TextField ref_text_rack30;
	
	public static  TextField ref_text_rack31;
	public static  TextField ref_text_rack32;
	public static  TextField ref_text_rack33;
	public static  TextField ref_text_rack34;
	public static  TextField ref_text_rack35;
	public static  TextField ref_text_rack36;
	public static  TextField ref_text_rack37;
	public static  TextField ref_text_rack38;
	public static  TextField ref_text_rack39;
	public static  TextField ref_text_rack40;
	
	public static  TextField ref_text_rack41;
	public static  TextField ref_text_rack42;
	public static  TextField ref_text_rack43;
	public static  TextField ref_text_rack44;
	public static  TextField ref_text_rack45;
	public static  TextField ref_text_rack46;
	public static  TextField ref_text_rack47;
	public static  TextField ref_text_rack48;

	
	@FXML
	private Label lblRack1;
	@FXML
	private Label lblRack2;
	@FXML
	private Label lblRack3;
	@FXML
	private Label lblRack4;
	@FXML
	private Label lblRack5;
	@FXML
	private Label lblRack6;
	@FXML
	private Label lblRack7;
	@FXML
	private Label lblRack8;
	@FXML
	private Label lblRack9;
	@FXML
	private Label lblRack10;
	@FXML
	private Label lblRack11;
	@FXML
	private Label lblRack12;	
	@FXML
	private Label lblRack13;
	@FXML
	private Label lblRack14;
	@FXML
	private Label lblRack15;
	@FXML
	private Label lblRack16;
	@FXML
	private Label lblRack17;
	@FXML
	private Label lblRack18;
	@FXML
	private Label lblRack19;
	@FXML
	private Label lblRack20;
	@FXML
	private Label lblRack21;
	@FXML
	private Label lblRack22;
	@FXML
	private Label lblRack23;
	@FXML
	private Label lblRack24;
	
	@FXML 	private Label lblRack25;
	@FXML 	private Label lblRack26;
	@FXML 	private Label lblRack27;
	@FXML 	private Label lblRack28;
	@FXML 	private Label lblRack29;
	@FXML 	private Label lblRack30;
	
	@FXML 	private Label lblRack31;
	@FXML	private Label lblRack32;
	@FXML	private Label lblRack33;
	@FXML	private Label lblRack34;
	@FXML 	private Label lblRack35;
	@FXML 	private Label lblRack36;
	@FXML 	private Label lblRack37;
	@FXML 	private Label lblRack38;
	@FXML 	private Label lblRack39;
	@FXML 	private Label lblRack40;
	@FXML 	private Label lblRack41;
	@FXML	private Label lblRack42;
	@FXML	private Label lblRack43;
	@FXML	private Label lblRack44;
	@FXML 	private Label lblRack45;
	@FXML 	private Label lblRack46;
	@FXML 	private Label lblRack47;
	@FXML 	private Label lblRack48;
	
	public static  Label ref_lblRack1;
	public static  Label ref_lblRack2;
	public static  Label ref_lblRack3;
	public static  Label ref_lblRack4;
	public static  Label ref_lblRack5;
	public static  Label ref_lblRack6;
	public static  Label ref_lblRack7;
	public static  Label ref_lblRack8;
	public static  Label ref_lblRack9;
	public static  Label ref_lblRack10;
	public static  Label ref_lblRack11;
	public static  Label ref_lblRack12;
	public static  Label ref_lblRack13;
	public static  Label ref_lblRack14;
	public static  Label ref_lblRack15;
	public static  Label ref_lblRack16;
	public static  Label ref_lblRack17;
	public static  Label ref_lblRack18;
	public static  Label ref_lblRack19;
	public static  Label ref_lblRack20;
	public static  Label ref_lblRack21;
	public static  Label ref_lblRack22;
	public static  Label ref_lblRack23;
	public static  Label ref_lblRack24;

	
	public static  Label ref_lblRack25;
	public static  Label ref_lblRack26;
	public static  Label ref_lblRack27;
	public static  Label ref_lblRack28;
	public static  Label ref_lblRack29;
	public static  Label ref_lblRack30;
	public static  Label ref_lblRack31;
	public static  Label ref_lblRack32;
	public static  Label ref_lblRack33;
	public static  Label ref_lblRack34;
	public static  Label ref_lblRack35;
	public static  Label ref_lblRack36;
	public static  Label ref_lblRack37;
	public static  Label ref_lblRack38;
	public static  Label ref_lblRack39;
	public static  Label ref_lblRack40;
	public static  Label ref_lblRack41;
	public static  Label ref_lblRack42;
	public static  Label ref_lblRack43;
	public static  Label ref_lblRack44;
	public static  Label ref_lblRack45;
	public static  Label ref_lblRack46;
	public static  Label ref_lblRack47;
	public static  Label ref_lblRack48;


	public ArrayList<Object> EnabledTextFields = new ArrayList<Object>();
	DeviceDataManagerController DisplayDataObj =  new DeviceDataManagerController();


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		referenceAssignment();
		setAllTxtFieldNotEditable();
		guiPropertySetting();

		String project_name = ProjectExecutionController.getCurrentProjectName();
		JSONObject devices_json =  DisplayDataObj.getDeployedDevicesJson();;//MySQL_Controller.sp_getdeploy_devices(project_name);
		ApplicationLauncher.logger.info("devices_json: "  + devices_json);
		try {
			JSONArray devices = devices_json.getJSONArray("Devices");
			JSONObject jobj = new JSONObject();
			for(int i = 0; i <devices.length(); i++){
				jobj = devices.getJSONObject(i);
				int rack_id = jobj.getInt("Rack_ID");
				TxtFieldToBeEnabled(rack_id);
			}



		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("MeterReadingPopupController: initialize: Exception:"+e.getMessage());
		}
		
		if(ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK == 40){
			hideGuiObjectAbove40();
		}
	}
	
	private void guiPropertySetting() {
		// TODO Auto-generated method stub
		ApplicationLauncher.logger.debug("guiPropertySetting: Entry");
		

		ref_text_rack1.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack1.setText(oldValue);
				}
			}
		});
		
		ref_text_rack2.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack2.setText(oldValue);
				}
			}
		});
		
		ref_text_rack3.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack3.setText(oldValue);
				}
			}
		});
		
		ref_text_rack4.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack4.setText(oldValue);
				}
			}
		});
		
		ref_text_rack5.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack5.setText(oldValue);
				}
			}
		});
		
		ref_text_rack6.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack6.setText(oldValue);
				}
			}
		});
		
		ref_text_rack7.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack7.setText(oldValue);
				}
			}
		});
		
		ref_text_rack8.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack8.setText(oldValue);
				}
			}
		});
		
		ref_text_rack9.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack9.setText(oldValue);
				}
			}
		});
		
		ref_text_rack10.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack10.setText(oldValue);
				}
			}
		});
		
		ref_text_rack11.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack11.setText(oldValue);
				}
			}
		});
		
		ref_text_rack12.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack12.setText(oldValue);
				}
			}
		});
		
		ref_text_rack13.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack13.setText(oldValue);
				}
			}
		});
		
		ref_text_rack14.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack14.setText(oldValue);
				}
			}
		});
		
		ref_text_rack15.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack15.setText(oldValue);
				}
			}
		});
		
		
		
		ref_text_rack16.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack16.setText(oldValue);
				}
			}
		});
		
		ref_text_rack17.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack17.setText(oldValue);
				}
			}
		});
		
		ref_text_rack18.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack18.setText(oldValue);
				}
			}
		});
		
		ref_text_rack19.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack19.setText(oldValue);
				}
			}
		});
		
		ref_text_rack20.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack20.setText(oldValue);
				}
			}
		});
		
		ref_text_rack21.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack21.setText(oldValue);
				}
			}
		});
		
		ref_text_rack22.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack22.setText(oldValue);
				}
			}
		});
		
		ref_text_rack23.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack23.setText(oldValue);
				}
			}
		});
		
		ref_text_rack24.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack24.setText(oldValue);
				}
			}
		});
		
		ref_text_rack25.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack25.setText(oldValue);
				}
			}
		});
		
		ref_text_rack26.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack26.setText(oldValue);
				}
			}
		});
		
		ref_text_rack27.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack27.setText(oldValue);
				}
			}
		});
		
		ref_text_rack28.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack28.setText(oldValue);
				}
			}
		});
		
		ref_text_rack29.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack29.setText(oldValue);
				}
			}
		});
		
		ref_text_rack30.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack30.setText(oldValue);
				}
			}
		});
		
		ref_text_rack31.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack31.setText(oldValue);
				}
			}
		});
		
		ref_text_rack32.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack32.setText(oldValue);
				}
			}
		});
		
		ref_text_rack33.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack33.setText(oldValue);
				}
			}
		});
		
		ref_text_rack34.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack34.setText(oldValue);
				}
			}
		});
		
		ref_text_rack35.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack35.setText(oldValue);
				}
			}
		});
		
		
		
		ref_text_rack36.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack36.setText(oldValue);
				}
			}
		});
		
		ref_text_rack37.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack37.setText(oldValue);
				}
			}
		});
		
		ref_text_rack38.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack38.setText(oldValue);
				}
			}
		});
		
		ref_text_rack39.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack39.setText(oldValue);
				}
			}
		});
		
		ref_text_rack40.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack40.setText(oldValue);
				}
			}
		});

		ref_text_rack41.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack41.setText(oldValue);
				}
			}
		});
		
		ref_text_rack42.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack42.setText(oldValue);
				}
			}
		});
		
		ref_text_rack43.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack43.setText(oldValue);
				}
			}
		});
		
		ref_text_rack44.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack44.setText(oldValue);
				}
			}
		});
		
		ref_text_rack45.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack45.setText(oldValue);
				}
			}
		});
		
		ref_text_rack46.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack46.setText(oldValue);
				}
			}
		});
		
		ref_text_rack47.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack47.setText(oldValue);
				}
			}
		});
		
		ref_text_rack48.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, 
					String newValue) {
				if (!newValue.matches("\\d*(\\.\\d*)?")) {
					
					ref_text_rack48.setText(oldValue);
				}
			}
		});
		
	}

	public void hideGuiObjectAbove40(){
		
		Platform.runLater(() -> {
			
			ref_lblRack41.setVisible(false);
			ref_lblRack42.setVisible(false);
			ref_lblRack43.setVisible(false);
			ref_lblRack44.setVisible(false);
			ref_lblRack45.setVisible(false);
			ref_lblRack46.setVisible(false);
			ref_lblRack47.setVisible(false);
			ref_lblRack48.setVisible(false);
			
			ref_text_rack41.setVisible(false);
			ref_text_rack42.setVisible(false);
			ref_text_rack43.setVisible(false);
			ref_text_rack44.setVisible(false);
			ref_text_rack45.setVisible(false);
			ref_text_rack46.setVisible(false);
			ref_text_rack47.setVisible(false);
			ref_text_rack48.setVisible(false);
			
		});

	}
	
	public void referenceAssignment(){
		
		ref_text_rack1 = text_rack1;
		ref_text_rack2 = text_rack2;
		ref_text_rack3 = text_rack3;
		ref_text_rack4 = text_rack4;
		ref_text_rack5 = text_rack5;
		ref_text_rack6 = text_rack6;
		ref_text_rack7 = text_rack7;
		ref_text_rack8 = text_rack8;
		ref_text_rack9 = text_rack9;
		ref_text_rack10 = text_rack10;
		ref_text_rack11 = text_rack11;
		ref_text_rack12 = text_rack12;
		ref_text_rack13 = text_rack13;
		ref_text_rack14 = text_rack14;
		ref_text_rack15 = text_rack15;
		ref_text_rack16 = text_rack16;
		ref_text_rack17 = text_rack17;
		ref_text_rack18 = text_rack18;
		ref_text_rack19 = text_rack19;
		ref_text_rack20 = text_rack20;
		ref_text_rack21 = text_rack21;
		ref_text_rack22 = text_rack22;
		ref_text_rack23 = text_rack23;
		ref_text_rack24 = text_rack24;
		
		ref_text_rack25 = text_rack25;
		ref_text_rack26 = text_rack26;
		ref_text_rack27 = text_rack27;
		ref_text_rack28 = text_rack28;
		ref_text_rack29 = text_rack29;
		ref_text_rack30 = text_rack30;
		ref_text_rack31 = text_rack31;
		ref_text_rack32 = text_rack32;
		ref_text_rack33 = text_rack33;
		ref_text_rack34 = text_rack34;
		
		ref_text_rack35 = text_rack35;
		ref_text_rack36 = text_rack36;
		ref_text_rack37 = text_rack37;
		ref_text_rack38 = text_rack38;
		ref_text_rack39 = text_rack39;
		ref_text_rack40 = text_rack40;
		ref_text_rack41 = text_rack41;
		ref_text_rack42 = text_rack42;
		ref_text_rack43 = text_rack43;
		ref_text_rack44 = text_rack44;
		
		ref_text_rack45 = text_rack45;
		ref_text_rack46 = text_rack46;
		ref_text_rack47 = text_rack47;
		ref_text_rack48 = text_rack48;

		
		ref_lblRack1 = lblRack1;
		ref_lblRack2 = lblRack2;
		ref_lblRack3 = lblRack3;
		ref_lblRack4 = lblRack4;
		ref_lblRack5 = lblRack5;
		ref_lblRack6 = lblRack6;
		ref_lblRack7 = lblRack7;
		ref_lblRack8 = lblRack8;
		ref_lblRack9 = lblRack9;
		ref_lblRack10 = lblRack10;
		ref_lblRack11 = lblRack11;
		ref_lblRack12 = lblRack12;
		ref_lblRack13 = lblRack13;
		ref_lblRack14 = lblRack14;
		ref_lblRack15 = lblRack15;
		ref_lblRack16 = lblRack16;
		ref_lblRack17 = lblRack17;
		ref_lblRack18 = lblRack18;
		ref_lblRack19 = lblRack19;
		ref_lblRack20 = lblRack20;
		ref_lblRack21 = lblRack21;
		ref_lblRack22 = lblRack22;
		ref_lblRack23 = lblRack23;
		ref_lblRack24 = lblRack24;
		
		ref_lblRack25 = lblRack25;
		ref_lblRack26 = lblRack26;
		ref_lblRack27 = lblRack27;
		ref_lblRack28 = lblRack28;
		ref_lblRack29 = lblRack29;
		ref_lblRack30 = lblRack30;
		
		
		ref_lblRack31 = lblRack31;
		ref_lblRack32 = lblRack32;
		ref_lblRack33 = lblRack33;
		ref_lblRack34 = lblRack34;
		ref_lblRack35 = lblRack35;
		ref_lblRack36 = lblRack36;
		ref_lblRack37 = lblRack37;
		ref_lblRack38 = lblRack38;
		ref_lblRack39 = lblRack39;
		ref_lblRack40 = lblRack40;
		
		
		ref_lblRack41 = lblRack41;
		ref_lblRack42 = lblRack42;
		ref_lblRack43 = lblRack43;
		ref_lblRack44 = lblRack44;
		ref_lblRack45 = lblRack45;
		ref_lblRack46 = lblRack46;
		ref_lblRack47 = lblRack47;
		ref_lblRack48 = lblRack48;
		
	}

	public void setEnabledTextFields(ArrayList<Object> txtfields){
		EnabledTextFields = txtfields;
	}

	public ArrayList<Object> getEnabledTextFields(){
		return EnabledTextFields;
	}

	public void setAllTxtFieldNotEditable(){
		ref_text_rack1.setEditable(false);
		ref_text_rack2.setEditable(false);
		ref_text_rack3.setEditable(false);
		ref_text_rack4.setEditable(false);
		ref_text_rack5.setEditable(false);
		ref_text_rack6.setEditable(false);
		ref_text_rack7.setEditable(false);
		ref_text_rack8.setEditable(false);
		ref_text_rack9.setEditable(false);
		ref_text_rack10.setEditable(false);
		ref_text_rack11.setEditable(false);
		ref_text_rack12.setEditable(false);
		ref_text_rack13.setEditable(false);
		ref_text_rack14.setEditable(false);
		ref_text_rack15.setEditable(false);
		ref_text_rack16.setEditable(false);
		ref_text_rack17.setEditable(false);
		ref_text_rack18.setEditable(false);
		ref_text_rack19.setEditable(false);
		ref_text_rack20.setEditable(false);
		ref_text_rack21.setEditable(false);
		ref_text_rack22.setEditable(false);
		ref_text_rack23.setEditable(false);
		ref_text_rack24.setEditable(false);
		
		
		ref_text_rack25.setEditable(false);
		ref_text_rack26.setEditable(false);
		ref_text_rack27.setEditable(false);
		ref_text_rack28.setEditable(false);
		ref_text_rack29.setEditable(false);
		ref_text_rack30.setEditable(false);
		
		ref_text_rack31.setEditable(false);
		ref_text_rack32.setEditable(false);
		ref_text_rack33.setEditable(false);
		ref_text_rack34.setEditable(false);
		ref_text_rack35.setEditable(false);
		ref_text_rack36.setEditable(false);
		ref_text_rack37.setEditable(false);
		ref_text_rack38.setEditable(false);
		ref_text_rack39.setEditable(false);
		ref_text_rack40.setEditable(false);
		
		ref_text_rack41.setEditable(false);
		ref_text_rack42.setEditable(false);
		ref_text_rack43.setEditable(false);
		ref_text_rack44.setEditable(false);
		ref_text_rack45.setEditable(false);
		ref_text_rack46.setEditable(false);
		ref_text_rack47.setEditable(false);
		ref_text_rack48.setEditable(false);
		
		ref_text_rack1.setFocusTraversable(false);
		ref_text_rack2.setFocusTraversable(false);
		ref_text_rack3.setFocusTraversable(false);
		ref_text_rack4.setFocusTraversable(false);
		ref_text_rack5.setFocusTraversable(false);
		ref_text_rack6.setFocusTraversable(false);
		ref_text_rack7.setFocusTraversable(false);
		ref_text_rack8.setFocusTraversable(false);
		ref_text_rack9.setFocusTraversable(false);
		ref_text_rack10.setFocusTraversable(false);
		ref_text_rack11.setFocusTraversable(false);
		ref_text_rack12.setFocusTraversable(false);
		ref_text_rack13.setFocusTraversable(false);
		ref_text_rack14.setFocusTraversable(false);
		ref_text_rack15.setFocusTraversable(false);
		ref_text_rack16.setFocusTraversable(false);
		ref_text_rack17.setFocusTraversable(false);
		ref_text_rack18.setFocusTraversable(false);
		ref_text_rack19.setFocusTraversable(false);
		ref_text_rack20.setFocusTraversable(false);
		ref_text_rack21.setFocusTraversable(false);
		ref_text_rack22.setFocusTraversable(false);
		ref_text_rack23.setFocusTraversable(false);
		ref_text_rack24.setFocusTraversable(false);
		ref_text_rack25.setFocusTraversable(false);
		ref_text_rack26.setFocusTraversable(false);
		ref_text_rack27.setFocusTraversable(false);
		ref_text_rack28.setFocusTraversable(false);
		ref_text_rack29.setFocusTraversable(false);
		ref_text_rack30.setFocusTraversable(false);
		ref_text_rack31.setFocusTraversable(false);
		ref_text_rack32.setFocusTraversable(false);
		ref_text_rack33.setFocusTraversable(false);
		ref_text_rack34.setFocusTraversable(false);
		ref_text_rack35.setFocusTraversable(false);
		ref_text_rack36.setFocusTraversable(false);
		ref_text_rack37.setFocusTraversable(false);
		ref_text_rack38.setFocusTraversable(false);
		ref_text_rack39.setFocusTraversable(false);
		ref_text_rack40.setFocusTraversable(false);
		ref_text_rack41.setFocusTraversable(false);
		ref_text_rack42.setFocusTraversable(false);
		ref_text_rack43.setFocusTraversable(false);
		ref_text_rack44.setFocusTraversable(false);
		ref_text_rack45.setFocusTraversable(false);
		ref_text_rack46.setFocusTraversable(false);
		ref_text_rack47.setFocusTraversable(false);
		ref_text_rack48.setFocusTraversable(false);
		

		
		ref_lblRack1.setDisable(true);
		ref_lblRack2.setDisable(true);
		ref_lblRack3.setDisable(true);
		ref_lblRack4.setDisable(true);
		ref_lblRack5.setDisable(true);
		ref_lblRack6.setDisable(true);
		ref_lblRack7.setDisable(true);
		ref_lblRack8.setDisable(true);
		ref_lblRack9.setDisable(true);
		ref_lblRack10.setDisable(true);
		ref_lblRack11.setDisable(true);
		ref_lblRack12.setDisable(true);
		ref_lblRack13.setDisable(true);
		ref_lblRack14.setDisable(true);
		ref_lblRack15.setDisable(true);
		ref_lblRack16.setDisable(true);
		ref_lblRack17.setDisable(true);
		ref_lblRack18.setDisable(true);
		ref_lblRack19.setDisable(true);
		ref_lblRack20.setDisable(true);
		ref_lblRack21.setDisable(true);
		ref_lblRack22.setDisable(true);
		ref_lblRack23.setDisable(true);
		ref_lblRack24.setDisable(true);

		
		ref_lblRack25.setDisable(true);
		ref_lblRack26.setDisable(true);
		ref_lblRack27.setDisable(true);
		ref_lblRack28.setDisable(true);
		ref_lblRack29.setDisable(true);
		ref_lblRack30.setDisable(true);
		
		ref_lblRack31.setDisable(true);
		ref_lblRack32.setDisable(true);
		ref_lblRack33.setDisable(true);
		ref_lblRack34.setDisable(true);
		ref_lblRack35.setDisable(true);
		ref_lblRack36.setDisable(true);
		ref_lblRack37.setDisable(true);
		ref_lblRack38.setDisable(true);
		ref_lblRack39.setDisable(true);
		ref_lblRack40.setDisable(true);
		
		
		ref_lblRack41.setDisable(true);
		ref_lblRack42.setDisable(true);
		ref_lblRack43.setDisable(true);
		ref_lblRack44.setDisable(true);
		ref_lblRack45.setDisable(true);
		ref_lblRack46.setDisable(true);
		ref_lblRack47.setDisable(true);
		ref_lblRack48.setDisable(true);
		
		
	}

	public void TxtFieldToBeEnabled(int i){

		ApplicationLauncher.logger.info("TxtFieldToBeEnabled: Enabling txtfield: " + i);
		switch (i) {

		case 1:
			ref_text_rack1.setEditable(true);
			addEnabledTextField(ref_text_rack1);
			ref_lblRack1.setDisable(false); 
			ref_text_rack1.setFocusTraversable(true); 
			break;

		case 2:
			ref_text_rack2.setEditable(true);
			addEnabledTextField(ref_text_rack2);
			ref_lblRack2.setDisable(false); 
			ref_text_rack2.setFocusTraversable(true); 
			break;

		case 3:
			ref_text_rack3.setEditable(true);
			addEnabledTextField(ref_text_rack3);
			ref_lblRack3.setDisable(false); 
			ref_text_rack3.setFocusTraversable(true); 
			break;

		case 4:
			ref_text_rack4.setEditable(true);
			addEnabledTextField(ref_text_rack4);
			ref_lblRack4.setDisable(false); 
			ref_text_rack4.setFocusTraversable(true); 
			break;

		case 5:
			ref_text_rack5.setEditable(true);
			addEnabledTextField(ref_text_rack5);
			ref_lblRack5.setDisable(false); 
			ref_text_rack5.setFocusTraversable(true); 
			break;

		case 6:
			ref_text_rack6.setEditable(true);
			addEnabledTextField(ref_text_rack6);
			ref_lblRack6.setDisable(false); 
			ref_text_rack6.setFocusTraversable(true); 
			break;

		case 7:
			ref_text_rack7.setEditable(true);
			addEnabledTextField(ref_text_rack7);
			ref_lblRack7.setDisable(false); 
			ref_text_rack7.setFocusTraversable(true); 
			break;

		case 8:
			ref_text_rack8.setEditable(true);
			addEnabledTextField(ref_text_rack8);
			ref_lblRack8.setDisable(false); 
			ref_text_rack8.setFocusTraversable(true); 
			break;

		case 9:
			ref_text_rack9.setEditable(true);
			addEnabledTextField(ref_text_rack9);
			ref_lblRack9.setDisable(false); 
			ref_text_rack9.setFocusTraversable(true); 
			break;

		case 10:
			ref_text_rack10.setEditable(true);
			addEnabledTextField(ref_text_rack10);
			ref_lblRack10.setDisable(false); 
			ref_text_rack10.setFocusTraversable(true); 
			break;

		case 11:
			ref_text_rack11.setEditable(true);
			addEnabledTextField(ref_text_rack11);
			ref_lblRack11.setDisable(false); 
			ref_text_rack11.setFocusTraversable(true); 
			break;

		case 12:
			ref_text_rack12.setEditable(true);
			addEnabledTextField(ref_text_rack12);
			ref_lblRack12.setDisable(false); 
			ref_text_rack12.setFocusTraversable(true); 
			break;
			
			
		case 13:
			ref_text_rack13.setEditable(true);
			addEnabledTextField(ref_text_rack13);
			ref_lblRack13.setDisable(false); 
			ref_text_rack13.setFocusTraversable(true); 
			break;

		case 14:
			ref_text_rack14.setEditable(true);
			addEnabledTextField(ref_text_rack14);
			ref_lblRack14.setDisable(false);
			ref_text_rack14.setFocusTraversable(true); 
			break;

		case 15:
			ref_text_rack15.setEditable(true);
			addEnabledTextField(ref_text_rack15);
			ref_lblRack15.setDisable(false);
			ref_text_rack15.setFocusTraversable(true); 
			break;

		case 16:
			ref_text_rack16.setEditable(true);
			addEnabledTextField(ref_text_rack16);
			ref_lblRack16.setDisable(false); 
			ref_text_rack16.setFocusTraversable(true); 
			break;

		case 17:
			ref_text_rack17.setEditable(true);
			addEnabledTextField(ref_text_rack17);
			ref_lblRack17.setDisable(false); 
			ref_text_rack17.setFocusTraversable(true); 
			break;

		case 18:
			ref_text_rack18.setEditable(true);
			addEnabledTextField(ref_text_rack18);
			ref_lblRack18.setDisable(false); 
			ref_text_rack18.setFocusTraversable(true); 
			break;

		case 19:
			ref_text_rack19.setEditable(true);
			addEnabledTextField(ref_text_rack19);
			ref_lblRack19.setDisable(false); 
			ref_text_rack19.setFocusTraversable(true); 
			break;

		case 20:
			ref_text_rack20.setEditable(true);
			addEnabledTextField(ref_text_rack20);
			ref_lblRack20.setDisable(false); 
			ref_text_rack20.setFocusTraversable(true); 
			break;

		case 21:
			ref_text_rack21.setEditable(true);
			addEnabledTextField(ref_text_rack21);
			ref_lblRack21.setDisable(false); 
			ref_text_rack21.setFocusTraversable(true); 
			break;

		case 22:
			ref_text_rack22.setEditable(true);
			addEnabledTextField(ref_text_rack22);
			ref_lblRack22.setDisable(false); 
			ref_text_rack22.setFocusTraversable(true); 
			break;
			
		case 23:
			ref_text_rack23.setEditable(true);
			addEnabledTextField(ref_text_rack23);
			ref_lblRack23.setDisable(false); 
			ref_text_rack23.setFocusTraversable(true); 
			break;
			
		case 24:
			ref_text_rack24.setEditable(true);
			addEnabledTextField(ref_text_rack24);
			ref_lblRack24.setDisable(false); 
			ref_text_rack24.setFocusTraversable(true); 
			break;

		case 25:
			ref_text_rack25.setEditable(true);
			addEnabledTextField(ref_text_rack25);
			ref_lblRack25.setDisable(false); 
			ref_text_rack25.setFocusTraversable(true); 
			break;
			
		case 26:
			ref_text_rack26.setEditable(true);
			addEnabledTextField(ref_text_rack26);
			ref_lblRack26.setDisable(false); 
			ref_text_rack26.setFocusTraversable(true); 
			break;
			
		case 27:
			ref_text_rack27.setEditable(true);
			addEnabledTextField(ref_text_rack27);
			ref_lblRack27.setDisable(false); 
			ref_text_rack27.setFocusTraversable(true); 
			break;
			
		case 28:
			ref_text_rack28.setEditable(true);
			addEnabledTextField(ref_text_rack28);
			ref_lblRack28.setDisable(false);
			ref_text_rack28.setFocusTraversable(true); 
			break;
			
		case 29:
			ref_text_rack29.setEditable(true);
			addEnabledTextField(ref_text_rack29);
			ref_lblRack29.setDisable(false); 
			ref_text_rack29.setFocusTraversable(true); 
			break;
			
		case 30:
			ref_text_rack30.setEditable(true);
			addEnabledTextField(ref_text_rack30);
			ref_lblRack30.setDisable(false); 
			ref_text_rack30.setFocusTraversable(true); 
			break;
			
		case 31:
			ref_text_rack31.setEditable(true);
			addEnabledTextField(ref_text_rack31);
			ref_lblRack31.setDisable(false); 
			ref_text_rack31.setFocusTraversable(true); 
			break;
			
		case 32:
			ref_text_rack32.setEditable(true);
			addEnabledTextField(ref_text_rack32);
			ref_lblRack32.setDisable(false); 
			ref_text_rack32.setFocusTraversable(true); 
			break;
			
		case 33:
			ref_text_rack33.setEditable(true);
			addEnabledTextField(ref_text_rack33);
			ref_lblRack33.setDisable(false); 
			ref_text_rack33.setFocusTraversable(true); 
			break;
			
		case 34:
			ref_text_rack34.setEditable(true);
			addEnabledTextField(ref_text_rack34);
			ref_lblRack34.setDisable(false); 
			ref_text_rack34.setFocusTraversable(true); 
			break;
			
		case 35:
			ref_text_rack35.setEditable(true);
			addEnabledTextField(ref_text_rack35);
			ref_lblRack35.setDisable(false); 
			ref_text_rack35.setFocusTraversable(true); 
			break;
			
		case 36:
			ref_text_rack36.setEditable(true);
			addEnabledTextField(ref_text_rack36);
			ref_lblRack36.setDisable(false); 
			ref_text_rack36.setFocusTraversable(true); 
			break;
			
		case 37:
			ref_text_rack37.setEditable(true);
			addEnabledTextField(ref_text_rack37);
			ref_lblRack37.setDisable(false);
			ref_text_rack37.setFocusTraversable(true); 
			break;
			
		case 38:
			ref_text_rack38.setEditable(true);
			addEnabledTextField(ref_text_rack38);
			ref_lblRack38.setDisable(false);
			ref_text_rack38.setFocusTraversable(true); 
			break;
			
		case 39:
			ref_text_rack39.setEditable(true);
			addEnabledTextField(ref_text_rack39);
			ref_lblRack39.setDisable(false);
			ref_text_rack39.setFocusTraversable(true); 
			break;
			
		case 40:
			ref_text_rack40.setEditable(true);
			addEnabledTextField(ref_text_rack40);
			ref_lblRack40.setDisable(false); 
			ref_text_rack40.setFocusTraversable(true); 
			break;
			
		case 41:
			ref_text_rack41.setEditable(true);
			addEnabledTextField(ref_text_rack41);
			ref_lblRack41.setDisable(false); 
			ref_text_rack41.setFocusTraversable(true); 
			break;
			
		case 42:
			ref_text_rack42.setEditable(true);
			addEnabledTextField(ref_text_rack42);
			ref_lblRack42.setDisable(false); 
			ref_text_rack42.setFocusTraversable(true); 
			break;
			
		case 43:
			ref_text_rack43.setEditable(true);
			addEnabledTextField(ref_text_rack43);
			ref_lblRack43.setDisable(false); 
			ref_text_rack43.setFocusTraversable(true); 
			break;
			
		case 44:
			ref_text_rack44.setEditable(true);
			addEnabledTextField(ref_text_rack44);
			ref_lblRack44.setDisable(false); 
			ref_text_rack44.setFocusTraversable(true); 
			break;
			
		case 45:
			ref_text_rack45.setEditable(true);
			addEnabledTextField(ref_text_rack45);
			ref_lblRack45.setDisable(false); 
			ref_text_rack45.setFocusTraversable(true); 
			break;
			
		case 46:
			ref_text_rack46.setEditable(true);
			addEnabledTextField(ref_text_rack46);
			ref_lblRack46.setDisable(false); 
			ref_text_rack46.setFocusTraversable(true); 
			break;
			
		case 47:
			ref_text_rack47.setEditable(true);
			addEnabledTextField(ref_text_rack47);
			ref_lblRack47.setDisable(false); 
			ref_text_rack47.setFocusTraversable(true); 
			break;
			
		case 48:
			ref_text_rack48.setEditable(true);
			addEnabledTextField(ref_text_rack48);
			ref_lblRack48.setDisable(false);
			ref_text_rack48.setFocusTraversable(true); 
			break;

		default:
			break;
		}
	}

	public void addEnabledTextField(Object en_txtfield){
		ArrayList<Object> txtfields = getEnabledTextFields();
		txtfields.add(en_txtfield);
		setEnabledTextFields(txtfields);
	}


	@FXML
	private void AddReadings() {
		ApplicationLauncher.logger.info("AddReadings: Entry");
		boolean initReadingFlag = TextBoxDialog.getInitReadingFlag();
		boolean finalReadingFlag = TextBoxDialog.getFinalReadingFlag();
		//j=0;
		JSONArray readings = getReadings();
		int no_of_devices_connected = DisplayDataObj.get_no_of_devices_connected();

		if(check_data_exists(readings)){
			if((initReadingFlag) && (!finalReadingFlag)){
				DeviceDataManagerController.setInitMeterValues(readings);
			}
			else if((!initReadingFlag) && (finalReadingFlag)){
				DeviceDataManagerController.setFinalMeterValues(readings);
			}
			else{
				ApplicationLauncher.logger.info("AddReadings: Else case");
			}
			ApplicationLauncher.logger.info("readings.length()) "  + readings.length());
			ref_text_rack1.getScene().getWindow().hide();
		}



	}
	public boolean check_data_exists(JSONArray reading){
		boolean data_exists=true;
		JSONObject jobj=new JSONObject();
		String kwh="";
		for(int i=0;i<reading.length();i++){
			try {
				jobj=reading.getJSONObject(i);
				kwh=jobj.getString("reading");
				if(kwh.isEmpty()){
					data_exists=false;

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("MeterReadingPopupController: check_data_exists: JSONException:"+e.getMessage());
			}

		}
		return data_exists;
	}

	public JSONArray getReadings(){
		ArrayList<Object> enabled_txtfields = getEnabledTextFields();
		JSONArray meter_reading = new JSONArray();

		for(int i=0; i< enabled_txtfields.size(); i++){

			JSONObject jobj = new JSONObject();
			TextField txtf = (TextField) enabled_txtfields.get(i);
			String reading = txtf.getText();
			String rack_id = (txtf.getId()).replace("text_rack","");
			try {
				jobj.put("rack_id", rack_id);
				jobj.put("reading", reading);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("MeterReadingPopupController: getReadings: JSONException:"+e.getMessage());
			}
			meter_reading.put(jobj);
		}

		return meter_reading;
	}
/*	public void InformUser(String title, String info,AlertType Alert_type){
		TextBoxDialog TextBoxDialogobj = new TextBoxDialog();
		TextBoxDialogobj.TriggerUserInfoPlatFormLater(title, info, Alert_type);
	}*/
}