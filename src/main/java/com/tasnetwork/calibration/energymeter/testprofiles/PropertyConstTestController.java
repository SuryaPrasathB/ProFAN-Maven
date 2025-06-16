package com.tasnetwork.calibration.energymeter.testprofiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.Cursor;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

public class PropertyConstTestController extends AnchorPane{


	@FXML TextField txtTestType;
	public static TextField ref_txtTestType;

	@FXML TextField txtAlias_ID;
	public static TextField ref_txtAlias_ID;

	@FXML TextField txt_Energy;
	public static TextField ref_txt_Energy;

	@FXML ComboBox<String> cmbBox_Un;
	public static ComboBox<String>  ref_cmbBox_Un;

	/*@FXML ComboBox<String> cmbBox_Y_Un;
	public static ComboBox<String>  ref_cmbBox_Y_Un;

	@FXML ComboBox<String> cmbBox_B_Un;
	public static ComboBox<String>  ref_cmbBox_B_Un;*/
	Timer SaveDataTimer;
	public static DraggableTestNode mCurrentNode=null;

	@FXML Button btn_Save;
	static int saved_successfully_count =0;
	private String saveFailedReason = "";

	@FXML
	private void initialize() throws IOException {

		//Add one icon that will be used for the drag-drop process
		//This is added as a child to the root anchorpane so it can be visible
		//on both sides of the split pane.
		//mDragOverIcon = new DragIcon();
		ref_txtTestType = txtTestType;
		ref_txtAlias_ID = txtAlias_ID;
		ref_txt_Energy = txt_Energy;
		ref_cmbBox_Un = cmbBox_Un;
		/*ref_cmbBox_Y_Un = cmbBox_Y_Un;
		ref_cmbBox_B_Un = cmbBox_B_Un;*/
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
		ApplicationLauncher.logger.debug("PropertyConstTestController:  applyUacSettings: Entry");
		if(!ProjectController.isChildPropertySaveEnabled()){
			btn_Save.setDisable(true);
		}
		
	}

	public static  void PropertyDisplayUpdate(String testType, String aliasId,DraggableTestNode SelectedNode){

		mCurrentNode = SelectedNode;
		ref_txtTestType.setText(testType);
		ref_txtAlias_ID.setText(aliasId);

		ArrayList<String> volts = new ArrayList<String>();

		volts.add("100");
		volts.add("125");
		volts.add("150");


		ref_cmbBox_Un.getItems().clear();
		ref_cmbBox_Un.getItems().addAll(volts);
		ref_cmbBox_Un.getSelectionModel().select(0);

		/*ref_cmbBox_Y_Un.getItems().clear();
		ref_cmbBox_Y_Un.getItems().addAll(volts);
		ref_cmbBox_Y_Un.getSelectionModel().select(0);

		ref_cmbBox_B_Un.getItems().clear();
		ref_cmbBox_B_Un.getItems().addAll(volts);
		ref_cmbBox_B_Un.getSelectionModel().select(0);*/

		if(mCurrentNode.IsNewNode() != true){
			String project_name = mCurrentNode.getProjectName();
			JSONObject testdetailslist = MySQL_Controller.sp_getproject_components(project_name, testType, aliasId);
			try {
				JSONArray testcase = testdetailslist.getJSONArray("test_details");
				JSONObject test = testcase.getJSONObject(0);
				ref_txt_Energy.setText(test.getString("power"));
				ApplicationLauncher.logger.info("voltage: " + test.getString("voltage"));

				ref_cmbBox_Un.setValue(test.getString("voltage"));
				/*ref_cmbBox_Y_Un.setValue(test.getString("voltage2"));
				ref_cmbBox_B_Un.setValue(test.getString("voltage3"));*/
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("PropertyConstTestController: PropertyDisplayUpdate: Exception:" + e.getMessage());
			}
		}
	}


	public String getPowerValue(){
		String powervalue = ref_txt_Energy.getText();
		return powervalue;
	}

	public void SaveToDB(){
		List<String> columnData = mCurrentNode.getTestCaseNames();
		List<String> EminData = mCurrentNode.getEmin_list();
		List<String> EmaxData = mCurrentNode.getEmax_list();
		List<String> PulsesData = mCurrentNode.getPulses_list();
		List<String> AverageData = mCurrentNode.getAverage_list();
		List<String> TimeData = mCurrentNode.getTime_list();
		List<String> SkipReadingCountData = mCurrentNode.getSkipReadingCount_list();
		List<String> DeviationData = mCurrentNode.getDeviation_list();
		List<String> TestRunTypeData = mCurrentNode.getTestRunType_list();
		if(columnData.size() !=0){
			saved_successfully_count= 0;
			String testname = "";
			String emin = "";
			String emax ="";
			String pulses = "";
			String average = "";
			String time = "";
			String SkipReadingCount = "";
			String deviation = "";
			String runtype = "";
			for(int i =0; i< columnData.size(); i++){
				testname = columnData.get(i);
				emin = EminData.get(i);
				emax = EmaxData.get(i);
				pulses = PulsesData.get(i);
				average = AverageData.get(i);
				time = TimeData.get(i);
				SkipReadingCount = SkipReadingCountData.get(i);
				deviation = DeviationData.get(i);
				runtype = TestRunTypeData.get(i);
				SavePowerToDB(testname, emin, emax, pulses, average, time, SkipReadingCount, deviation, runtype);
			}
			//ProjectController.RefreshSummaryTableWithSummaryData();
			int no_of_test_points = columnData.size();
			ApplicationLauncher.logger.info("SaveToDB: no_of_test_points: " + no_of_test_points);
			ApplicationLauncher.logger.info("SaveToDB: saved_successfully_count: " + saved_successfully_count);
			if(saved_successfully_count == no_of_test_points){
				ApplicationLauncher.logger.info ("PropertyConstTestController: SaveToDB: Test point saved successfully");
				ApplicationLauncher.InformUser("Saved", "Test point saved successfully",AlertType.INFORMATION);
			}
			else{
				ApplicationLauncher.logger.info ("PropertyConstTestController: SaveToDB: Test point save failed. Test point save failed:" +saveFailedReason);
				ApplicationLauncher.InformUser("Incorrect Value", "Test point save failed: " + saveFailedReason,AlertType.ERROR);

			}

		}
		else{
			ApplicationLauncher.logger.info ("PropertyConstTestController: SaveToDB:  Save the test point inputs - warning triggered");
			ApplicationLauncher.InformUser("Not Saved","Save the test point inputs",AlertType.WARNING);
		}
	}

	public void SavePowerToDB(String testname, String emin, String emax, String pulses, String average ,
			String time, String SkipReadingCount, String deviation, String runtype){
		String power = getPowerValue();
		String project_name = mCurrentNode.getProjectName();
		String test_type = mCurrentNode.getType().toString();
		String aliasID = txtAlias_ID.getText();
		String positionID = Integer.toString(mCurrentNode.getPositionId());
		String alias_name = mCurrentNode.getAliasName();
		String volt = ref_cmbBox_Un.getValue();
		String volt_display_name = GuiUtils.FormatUnForDisplay(volt);
		String summary_display_tp_name = alias_name+ "_" + aliasID + "-" +volt_display_name+ "-" + testname+ "-"+ power;
		if(GuiUtils.FormatErrorInput(emin)!=null){
			if(GuiUtils.FormatErrorInput(emax)!=null){
				if(GuiUtils.is_number(pulses)){
					if(GuiUtils.validateAvgPulses(average)){
					if(GuiUtils.is_number(time)){
						if(GuiUtils.is_number(SkipReadingCount)){
							if(GuiUtils.is_float(deviation)){	
								if(GuiUtils.is_number(volt)){
									/*String RatedVoltageValue = ProjectController.getModelDataFromDB(project_name,"rated_voltage_vd");
									float VoltagePercentageInVoltageValue= (Float.parseFloat(RatedVoltageValue)*Float.parseFloat(volt))/100;
									ApplicationLauncher.logger.debug("SavePowerToDB: VoltagePercentageInVoltageValue:"+VoltagePercentageInVoltageValue);
									Boolean validation_status = GUIUtils.Validate_voltage(String.valueOf(VoltagePercentageInVoltageValue));
									*/
									Boolean validation_status = GuiUtils.ValidateVoltagePercentageInVoltageValue(project_name, volt);
									if(validation_status){
										if(GuiUtils.is_float(power)){
											ApplicationLauncher.logger.info("Saving Const Test");
											ProjectController.removeTestPoint(mCurrentNode.getType().toString(), aliasID);

											MySQL_Controller.sp_add_project_components(project_name,summary_display_tp_name,test_type, aliasID, positionID, 
													time , "", "", "","","","", 
													emin, emax, pulses,SkipReadingCount,
													deviation,runtype, power,"",volt,
													"","","","","",
													"","","","","",
													"","","",average);

											ProjectController.UpdateNewTestPointSummaryDataToDB(mCurrentNode.getProjectName(), summary_display_tp_name , mCurrentNode.getType().toString(),  aliasID);
											saved_successfully_count = saved_successfully_count + 1;
										}else{

											saveFailedReason = testname +": Energy is not a valid float number";
										}
									}else{

										saveFailedReason = testname +": Voltage validation failed due to configured acceptable limit";
									}
								}else{

									saveFailedReason = testname +": R-voltage is not a valid number";
								}
							}else{

								saveFailedReason = testname +": deviation is not a valid float number";
							}
						}else{

							saveFailedReason = testname +": skip reading count is not a valid number";
						}
					}else{

						saveFailedReason = testname +": time is not a valid number";
					}
					}else{

						saveFailedReason = testname +": average is not a valid number";
					}
				}else{

					saveFailedReason = testname +": pulse is not a valid number";
				}
			}else{

				saveFailedReason = testname +": Error max is not a valid format";
			}
		}else{

			saveFailedReason = testname +": Error min is not a valid format";
		}
	}

/*	public void InformUser(String title, String info,AlertType Alert_type){
		TextBoxDialog TextBoxDialogobj = new TextBoxDialog();
		TextBoxDialogobj.TriggerUserInfoPlatFormLater(title, info, Alert_type);
	}*/

	public void SaveConstTestDataTrigger(){
		ApplicationLauncher.logger.info("SaveConstTestDataTrigger Invoked:");

		SaveDataTimer = new Timer();
		SaveDataTimer.schedule(new SaveConstTestDataTask(),100);

	}

	public boolean IsFieldEmpty(){
		if (!ref_cmbBox_Un.getSelectionModel().getSelectedItem().isEmpty()){

			if (!ref_txt_Energy.getText().isEmpty()){
				return false;
			}else{
				ApplicationLauncher.logger.info ("IsFieldEmpty: <Energy> field is empty Prompt:");
				ApplicationLauncher.InformUser("Empty Field", "<Energy> field is empty",AlertType.ERROR);
				return true;
			}

		}else{

			ApplicationLauncher.logger.info ("IsFieldEmpty: <Un> field is empty Prompt:");
			ApplicationLauncher.InformUser("Empty Field", "<Un> field is empty",AlertType.ERROR);
			return true;
		}

	}

	class SaveConstTestDataTask extends TimerTask {
		public void run() {
			if (!IsFieldEmpty()){
				ApplicationLauncher.setCursor(Cursor.WAIT);
				mCurrentNode.NewNode(false);
				PropertyInfluenceController.delete_previous_saved_data();
				//ProjectController.RefreshSummaryDataFromDB();
				SaveToDB();
				ProjectController.SaveProject();
				ProjectController.LoadSummaryDataToGUI();
				ApplicationLauncher.setCursor(Cursor.DEFAULT);
			}
			SaveDataTimer.cancel();
		}
	}
}
