package com.tasnetwork.calibration.energymeter.testprofiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.database.MySQL_Controller;
import com.tasnetwork.calibration.energymeter.deployment.TextBoxDialog;
import com.tasnetwork.calibration.energymeter.project.ProjectController;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.Cursor;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

public class PropertyCreepController extends AnchorPane{

	Timer SaveDataTimer;
	@FXML TextField txtTestType;
	public static TextField ref_txtTestType;

	@FXML TextField txtAlias_ID;
	public static TextField ref_txtAlias_ID;

	@FXML ComboBox<String> cmbBoxUn;

	@FXML ComboBox<String> cmbBoxTime;


	@FXML TextField txtpulses;
	public static TextField ref_txtpulses;

	public static ComboBox<String> ref_cmbBoxUn;
	public static ComboBox<String> ref_cmbBoxTime;
	public static DraggableTestNode mCurrentNode=null;

	@FXML Button btn_Save;


	@FXML
	private void initialize() throws IOException {

		//Add one icon that will be used for the drag-drop process
		//This is added as a child to the root anchorpane so it can be visible
		//on both sides of the split pane.
		//mDragOverIcon = new DragIcon();
		ref_txtTestType = txtTestType;
		ref_txtAlias_ID = txtAlias_ID;
		ref_cmbBoxUn = cmbBoxUn;
		ref_cmbBoxTime = cmbBoxTime;
		ref_txtpulses = txtpulses;


/*		if((ConstantApp.USER_ACCESS_LEVEL.equals(ConstantApp.TESTER_ACCESS_LEVEL)) ||
				(ConstantApp.USER_ACCESS_LEVEL.equals(ConstantApp.READONLY_ACCESS_LEVEL))){
			btn_Save.setDisable(true);
		}*/
		
		if(ProcalFeatureEnable.USER_ACCESS_CONTROL_ENABLED){
			applyUacSettings();
		}
	}
	
	
	private void applyUacSettings() {
		// TODO Auto-generated method stub
		ApplicationLauncher.logger.debug("PropertyCreepController:  applyUacSettings: Entry");
		if(!ProjectController.isChildPropertySaveEnabled()){
			btn_Save.setDisable(true);
		}
		
	}

	public static  void PropertyDisplayUpdate(String testType, String aliasId,DraggableTestNode SelectedNode){

		mCurrentNode = SelectedNode;
		ref_txtTestType.setText(testType);
		ref_txtAlias_ID.setText(aliasId);
		ArrayList<String> ModelList = new ArrayList<String>();

		ModelList.add("100");
		ModelList.add("125");
		ModelList.add("140");

		ref_cmbBoxUn.getItems().clear();
		ref_cmbBoxUn.getItems().addAll(ModelList);
		ref_cmbBoxUn.getSelectionModel().select(0) ;

		ArrayList<String> Min_list = new ArrayList<String>();

		Min_list.add("1");
		Min_list.add("2");
		Min_list.add("5");

		ref_cmbBoxTime.getItems().clear();
		ref_cmbBoxTime.getItems().addAll(Min_list);
		ref_cmbBoxTime.getSelectionModel().select(1) ;

		if(mCurrentNode.IsNewNode() != true){
			String project_name = mCurrentNode.getProjectName();
			JSONObject testdetailslist = MySQL_Controller.sp_getproject_components(project_name, testType, aliasId);

			try {
				JSONArray testcase = testdetailslist.getJSONArray("test_details");
				JSONObject test = testcase.getJSONObject(0);
				ref_cmbBoxUn.setValue(test.getString("creep_un"));
				ref_cmbBoxTime.setValue(test.getString("time_duration"));
				ref_txtpulses.setText(test.getString("creep_pulses"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("PropertyCreepController: PropertyDisplayUpdate: Exception:" + e.getMessage());
			}
		}
	}

	public void SetUnValue(){
		String Un = cmbBoxUn.getValue();
		mCurrentNode.setCreepUn(Un);
		ApplicationLauncher.logger.info("%Un Value: " + Un);
	}




	public void SaveCreepDataTrigger() {
		ApplicationLauncher.logger.info("SaveCreepDataTrigger Invoked:");

		SaveDataTimer = new Timer();
		SaveDataTimer.schedule(new SaveCreepDataTask(),100);

	}


	class SaveCreepDataTask extends TimerTask {
		public void run() {
			ApplicationLauncher.setCursor(Cursor.WAIT);
			mCurrentNode.NewNode(false);
			String currentProject = mCurrentNode.getProjectName();
			String currentTestType = mCurrentNode.getType().toString();
			String currentAliasId = mCurrentNode.getAliasId();
			MySQL_Controller.sp_delete_project_components(currentProject, currentTestType, currentAliasId);
			MySQL_Controller.sp_delete_summary_data(currentProject, currentTestType, currentAliasId);
			//if(currentTestType.equals("InfluenceHarmonic")){
			if(currentTestType.equals(TestProfileType.InfluenceHarmonic.toString())){
				MySQL_Controller.sp_delete_harmonic_data(currentProject, currentTestType, currentAliasId);
			}
			ProjectController.refreshSummaryPaneData("", currentAliasId);

			String project_name = mCurrentNode.getProjectName();
			String test_type = mCurrentNode.getType().toString();
			ApplicationLauncher.logger.info("Node Title: " + txtTestType.getText());
			ApplicationLauncher.logger.info("Node AliasID: " + txtAlias_ID.getText());
			String testcasename = txtTestType.getText();
			String aliasID = txtAlias_ID.getText();
			String positionID = Integer.toString(mCurrentNode.getPositionId());
			String Un = cmbBoxUn.getSelectionModel().getSelectedItem();
			String Ib = "0.0";
			String time = cmbBoxTime.getSelectionModel().getSelectedItem();
			String pulses = txtpulses.getText();
			String average  = String .valueOf(ConstantApp.PULSE_AVERAGE_MIN_VALUE);
			String alias_name = mCurrentNode.getAliasName();
			String volt_display_name = GuiUtils.FormatUnForDisplay(Un);
			String summary_display_tp_name = alias_name + "_" +aliasID +"-"+ volt_display_name;
			if(GuiUtils.is_number(Un)){

				/*String RatedVoltageValue = ProjectController.getModelDataFromDB(project_name,"rated_voltage_vd");
				float VoltagePercentageInVoltageValue= (Float.parseFloat(RatedVoltageValue)*Float.parseFloat(Un))/100;
				ApplicationLauncher.logger.debug("SaveCreepDataTask: VoltagePercentageInVoltageValue:"+VoltagePercentageInVoltageValue);
				Boolean validation_status = GUIUtils.Validate_voltage(String.valueOf(VoltagePercentageInVoltageValue));
				*/
				Boolean validation_status = GuiUtils.ValidateVoltagePercentageInVoltageValue(project_name, Un);
				if(validation_status){
					if(GuiUtils.is_number(time)){
						if(GuiUtils.is_number(pulses)){
							ProjectController.removeTestPoint(mCurrentNode.getType().toString(), aliasID);
							MySQL_Controller.sp_add_project_components(project_name,
									summary_display_tp_name, test_type, aliasID, positionID,
									time, Un, pulses,"","","",
									"","","","","","","","","","",
									"","","","","",
									"","","","","",
									"","","",average);
							ProjectController.SaveProject();
							ProjectController.RefreshSummaryDataFromDB();
							ProjectController.UpdateNewTestPointSummaryDataToDB(project_name, 
									summary_display_tp_name, test_type, aliasID);
							//ProjectController.RefreshSummaryDataFromDB();
							ProjectController.LoadSummaryDataToGUI();
							ApplicationLauncher.logger.info ("PropertyCreepController: SaveCreepData: Test point saved successfully");
							ApplicationLauncher.InformUser("Saved", "Test point saved successfully",AlertType.INFORMATION);
						}
						else{
							ApplicationLauncher.logger.info ("PropertyCreepController: SaveCreepData: Test point save failed. Test point save failed: Kindly enter valid input for pulse");
							ApplicationLauncher.InformUser("Incorrect Value", "Test point save failed: Kindly enter valid input for pulse" ,AlertType.ERROR);
						}
					}
					else{
						ApplicationLauncher.logger.info ("PropertyCreepController: SaveCreepData: Test point save failed. Test point save failed: Kindly enter valid input for time");
						ApplicationLauncher.InformUser("Incorrect Value", "Test point save failed: Kindly enter valid input for time" ,AlertType.ERROR);
					}
				}
				else{
					ApplicationLauncher.logger.info ("PropertyCreepController: SaveCreepData: Test point save failed. Test point save failed: Voltage validation failed due to configured acceptable limit");
					ApplicationLauncher.InformUser("Incorrect Value", "Test point save failed: Voltage validation failed due to configured acceptable limit" ,AlertType.ERROR);
				}
			}
			else{
				ApplicationLauncher.logger.info ("PropertyCreepController: SaveCreepData: Test point save failed. Test point save failed: Kindly enter valid input for voltage");
				ApplicationLauncher.InformUser("Incorrect Value", "Test point save failed: Kindly enter valid input for voltage" ,AlertType.ERROR);
			}

			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			SaveDataTimer.cancel();
		}
	}

/*	public void InformUser(String title, String info,AlertType Alert_type){
		TextBoxDialog TextBoxDialogobj = new TextBoxDialog();
		TextBoxDialogobj.TriggerUserInfoPlatFormLater(title, info, Alert_type);
	}*/
}


