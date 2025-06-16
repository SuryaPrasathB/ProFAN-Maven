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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.Cursor;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

public class PropertyWarmupController extends AnchorPane {

	Timer SaveDataTimer;
	@FXML TextField txtTestType;


	@FXML TextField txtAlias_ID;


	@FXML ComboBox<String> cmbBoxTimeDuration;

	@FXML ComboBox<String> cmbBoxVolt;

	@FXML TextField txtWarmupTestPulseNo;

	@FXML Button btn_Save;
	@FXML Label labelWarmup;
	public static Label ref_labelWarmup;


	public static DraggableTestNode mCurrentNode=null;

	@FXML
	private void initialize() throws IOException {

		ref_labelWarmup = labelWarmup;
		if((ConstantApp.USER_ACCESS_LEVEL.equals(ConstantApp.TESTER_ACCESS_LEVEL)) ||
				((ConstantApp.USER_ACCESS_LEVEL.equals(ConstantApp.READONLY_ACCESS_LEVEL)))){
			btn_Save.setDisable(true);
		}

		if(!ProcalFeatureEnable.WARMUP_NO_OF_PULSES_DISPLAY_ENABLE_FEATURE){
			txtWarmupTestPulseNo.setVisible(false);
			ref_labelWarmup.setVisible(false);
		}
		
		if(ProcalFeatureEnable.USER_ACCESS_CONTROL_ENABLED){
			applyUacSettings();
		}

	}
	
	
	private void applyUacSettings() {
		// TODO Auto-generated method stub
		ApplicationLauncher.logger.debug("PropertyWarmupController:  applyUacSettings: Entry");
		if(!ProjectController.isChildPropertySaveEnabled()){
			btn_Save.setDisable(true);
		}
		
	}

	public  void updateTestPropertyDisplay(String testType, String aliasId, DraggableTestNode SelectedNode){
		ApplicationLauncher.logger.info("Updating the properties field with " + testType + " , " + aliasId);

		mCurrentNode = SelectedNode;
		txtTestType.setText(testType);
		txtAlias_ID.setText(aliasId);

		ArrayList<String> ModelList = new ArrayList<String>();

		ModelList.add("1");
		ModelList.add("2");
		ModelList.add("5");

		cmbBoxTimeDuration.getItems().clear();
		cmbBoxTimeDuration.getItems().addAll(ModelList);
		cmbBoxTimeDuration.getSelectionModel().select(0);

		ArrayList<String> volt_percent = new ArrayList<String>();

		volt_percent.add("100");
		volt_percent.add("125");
		volt_percent.add("150");


		cmbBoxVolt.getItems().clear();
		cmbBoxVolt.getItems().addAll(volt_percent);
		cmbBoxVolt.getSelectionModel().select(0);

		if(mCurrentNode.IsNewNode() != true){
			String project_name = mCurrentNode.getProjectName();
			ApplicationLauncher.logger.info("updateTestPropertyDisplay: project_name:"+project_name);
			JSONObject testdetailslist = MySQL_Controller.sp_getproject_components(project_name, testType, aliasId);
			ApplicationLauncher.logger.info("updateTestPropertyDisplay: testdetailslist:"+testdetailslist);
			try {
				JSONArray testcase = testdetailslist.getJSONArray("test_details");
				JSONObject test = testcase.getJSONObject(0);
				cmbBoxTimeDuration.setValue(test.getString("time_duration"));
				cmbBoxVolt.setValue(test.getString("voltage"));
				txtWarmupTestPulseNo.setText(test.getString("inf_pulses"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("updateTestPropertyDisplay: JSONException:"+e.getMessage());
			}
		}

	}

	public void SetWarmupTime(){
		String warmupduration = cmbBoxTimeDuration.getSelectionModel().getSelectedItem();
		mCurrentNode.setWarmupTimeDuration(warmupduration);
		ApplicationLauncher.logger.info("warmupduration: " + warmupduration);
	}


/*	public void InformUser(String title, String info,AlertType Alert_type){
		TextBoxDialog TextBoxDialogobj = new TextBoxDialog();
		TextBoxDialogobj.TriggerUserInfoPlatFormLater(title, info, Alert_type);
	}*/

	public void SaveWarmupDataTrigger(){
		ApplicationLauncher.logger.info("SaveWarmupDataTrigger Invoked:");
		SaveDataTimer = new Timer();
		SaveDataTimer.schedule(new SaveWarmupDataTask(),100);
	}



	class SaveWarmupDataTask extends TimerTask {
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
			String warmupduration = cmbBoxTimeDuration.getSelectionModel().getSelectedItem();
			String volt = cmbBoxVolt.getValue();
			String pulses = txtWarmupTestPulseNo.getText();
			String alias_name = mCurrentNode.getAliasName();
			String average  = String .valueOf(ConstantApp.PULSE_AVERAGE_MIN_VALUE);
			String volt_display_name = GuiUtils.FormatUnForDisplay(volt);
			String summary_display_tp_name = alias_name+ "_" + aliasID + "-" + volt_display_name;
			if(GuiUtils.is_number(warmupduration)){
				if(GuiUtils.is_number(pulses)){
					if(GuiUtils.is_number(volt)){
						Boolean validation_status = GuiUtils.ValidateVoltagePercentageInVoltageValue(project_name, volt);
						if (validation_status){
							ProjectController.removeTestPoint(mCurrentNode.getType().toString(), aliasID);

							MySQL_Controller.sp_add_project_components(project_name,summary_display_tp_name,test_type, aliasID, positionID,
									warmupduration ,"", "","","","","",
									"","",pulses,"","","","","",volt,
									"","","","","",
									"","","","","",
									"","","",average);
							ProjectController.SaveProject();
							ProjectController.RefreshSummaryDataFromDB();
							ProjectController.UpdateNewTestPointSummaryDataToDB(project_name, summary_display_tp_name, test_type, aliasID);
							ProjectController.LoadSummaryDataToGUI();
							ApplicationLauncher.logger.info ("PropertyWarmupController: SaveWarmupData: Test point saved successfully");
							ApplicationLauncher.InformUser("Saved", "Test point saved successfully",AlertType.INFORMATION);
						}
						else{
							ApplicationLauncher.logger.info ("PropertyWarmupController: SaveWarmupData: Test point save failed. Voltage validation failed due to configured acceptable limit");
							ApplicationLauncher.InformUser("Incorrect Value", "Test point save failed. Voltage validation failed due to configured acceptable limit",AlertType.ERROR);
						}
					}
					else{
						ApplicationLauncher.logger.info ("PropertyWarmupController: SaveWarmupData: Test point save failed. Voltage should be valid number");
						ApplicationLauncher.InformUser("Incorrect Value", "Test point save failed. Voltage should be valid number",AlertType.ERROR);
					}
				}
				else{
					ApplicationLauncher.logger.info ("PropertyWarmupController : SaveWarmupData: Test point save failed. Pulses should be valid number");
					ApplicationLauncher.InformUser("Incorrect Value", "Test point save failed. Pulses should be valid number",AlertType.ERROR);
				}
			}
			else{
				ApplicationLauncher.logger.info ("PropertyWarmupController : SaveWarmupData: Test point save failed. Warmup duration should be valid number");
				ApplicationLauncher.InformUser("Incorrect Value", "Test point save failed. Warmup duration should be valid number",AlertType.ERROR);
			}
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			SaveDataTimer.cancel();
		}
	} 

}
