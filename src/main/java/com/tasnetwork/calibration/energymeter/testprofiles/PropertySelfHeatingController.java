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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.Cursor;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

public class PropertySelfHeatingController extends AnchorPane{

	
	Timer SaveDataTimer;

	@FXML TextField txtTestType;
	public static TextField ref_txtTestType;

	@FXML TextField txtAlias_ID;
	public static TextField ref_txtAlias_ID;

	@FXML TextField txt_warmup_duration;
	public static TextField ref_txt_warmup_duration;

	@FXML TextField txt_no_of_readings;
	public static TextField ref_txt_no_of_readings;

	@FXML ComboBox<String> cmbBox_Un;
	public static ComboBox<String>  ref_cmbBox_Un;
	
	//@FXML TextField txtTestPulseNo;
	//public static TextField ref_txtTestPulseNo;


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
		ref_txt_warmup_duration = txt_warmup_duration;
		ref_txt_no_of_readings = txt_no_of_readings;
		ref_cmbBox_Un = cmbBox_Un;
		//ref_txtTestPulseNo = txtTestPulseNo;

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
		ApplicationLauncher.logger.debug("PropertySelfHeatingController:  applyUacSettings: Entry");
		if(!ProjectController.isChildPropertySaveEnabled()){
			btn_Save.setDisable(true);
		}
		
	}

	public static  void PropertyDisplayUpdate(String testType, String aliasId, DraggableTestNode SelectedNode){

		mCurrentNode = SelectedNode;
		ref_txtTestType.setText(testType);
		ref_txtAlias_ID.setText(aliasId);

		ArrayList<String> volts = new ArrayList<String>();

		volts.add("100");
		volts.add("125");
		volts.add("150");

		ref_cmbBox_Un.getItems().clear();
		ref_cmbBox_Un.getItems().addAll(volts);
		ref_cmbBox_Un.getSelectionModel().select(0) ;

		if(mCurrentNode.IsNewNode() != true){
			String project_name = mCurrentNode.getProjectName();
			JSONObject testdetailslist = MySQL_Controller.sp_getproject_components(project_name, testType, aliasId);

			try {
				JSONArray testcase = testdetailslist.getJSONArray("test_details");
				JSONObject test = new JSONObject();
				String testcasename = "";
				for(int i=0; i<testcase.length(); i++){
					test = testcase.getJSONObject(i);
					testcasename = test.getString("test_case_name");
					if((!testcasename.contains(ConstantApp.SELF_HEATING_START_TEST_ALIAS_NAME)) &&
							(!testcasename.contains(ConstantApp.SELF_HEATING_END_TEST_ALIAS_NAME))){
						ref_txt_no_of_readings.setText(test.getString("rep_no_of_readings"));
						ref_cmbBox_Un.setValue(test.getString("voltage"));
					}
					else if(testcasename.contains(ConstantApp.SELF_HEATING_START_TEST_ALIAS_NAME)){
						ref_txt_warmup_duration.setText(test.getString("time_duration"));
					}
					else{
						ApplicationLauncher.logger.info ("PropertyDisplayUpdate: SelfHeating Testcase  Mismatch");

					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("PropertySelfHeatingController: PropertyDisplayUpdate: JSONException:"+e.getMessage());
				
			}
		}
	}



	public String getno_of_readings(){
		String no_of_readings = ref_txt_no_of_readings.getText();
		return no_of_readings;
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

		String project_name = mCurrentNode.getProjectName();
		String test_type = mCurrentNode.getType().toString();
		String aliasID = txtAlias_ID.getText();
		String positionID = Integer.toString(mCurrentNode.getPositionId());
		String testtype = txtTestType.getText();
		String alias_name = mCurrentNode.getAliasName();
		String warmup_duration = ref_txt_warmup_duration.getText();
		String volt = cmbBox_Un.getSelectionModel().getSelectedItem();
		String summary_display_tp_name = ConstantApp.SELF_HEATING_START_TEST_ALIAS_NAME+ "_" + aliasID ;
		saved_successfully_count= 0;
		String average = ConstantApp.DEFAULT_NO_OF_PULSES;;//ref_txtTestPulseNo.getText();//ConstantApp.DEFAULT_NO_OF_PULSES;
		if(GuiUtils.is_number(warmup_duration)){
			if(GuiUtils.is_number(volt)){
				MySQL_Controller.sp_add_project_components(project_name,
						summary_display_tp_name,test_type, aliasID, positionID, 
						warmup_duration , "", "", "","","","", 
						"", "", "","",
						"","", "","",volt,
						"","","","","",
						"","","","","",
						"","","",average);

				ProjectController.UpdateNewTestPointSummaryDataToDB(mCurrentNode.getProjectName(), summary_display_tp_name , 
						mCurrentNode.getType().toString(),  aliasID);

				saved_successfully_count= saved_successfully_count+1;
			}
		}
		if(columnData.size() !=0){
			String testname = "";
			String emin = "";
			String emax = "";
			String pulses = "";
			average = "";
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
				SaveReadingsToDB(testname, emin, emax, pulses,average, time, SkipReadingCount, deviation, runtype);
			}
			String no_of_readings = getno_of_readings();
			int no_of_test_points = (Integer.parseInt(no_of_readings)*columnData.size()) +1;
			ApplicationLauncher.logger.info("SaveToDB: no_of_test_points: " + no_of_test_points);
			ApplicationLauncher.logger.info("SaveToDB: saved_successfully_count: " + saved_successfully_count);
			if(saved_successfully_count == no_of_test_points){
				ApplicationLauncher.logger.info ("PropertySelfHeatingController: SaveToDB: Test point saved successfully");
				ApplicationLauncher.InformUser("Saved", "Test point saved successfully",AlertType.INFORMATION);
			}
			else{
				ApplicationLauncher.logger.info ("PropertySelfHeatingController: SaveToDB: Test point save failed. Test point save failed:" +saveFailedReason);
				ApplicationLauncher.InformUser("Incorrect Value", "Test point save failed: " + saveFailedReason,AlertType.ERROR);
			
			}
		}
		else{
			ApplicationLauncher.logger.info ("PropertySelfHeatingController: SaveToDB:  Save the test point inputs - warning triggered");
			ApplicationLauncher.InformUser("Not Saved","Save the test point inputs",AlertType.WARNING);
		}


	}

	public void SaveReadingsToDB(String testname, String emin, String emax, String pulses, String average ,
			String time, String SkipReadingCount, String deviation, String runtype){
		String no_of_readings = getno_of_readings();
		String project_name = mCurrentNode.getProjectName();
		String test_type = mCurrentNode.getType().toString();
		String aliasID = txtAlias_ID.getText();
		String positionID = Integer.toString(mCurrentNode.getPositionId());
		String testtype = txtTestType.getText();
		String alias_name = mCurrentNode.getAliasName();
		String volt = cmbBox_Un.getValue();
		String volt_display_name = GuiUtils.FormatUnForDisplay(volt);
		if(GuiUtils.FormatErrorInput(emin)!=null){
			if(GuiUtils.FormatErrorInput(emax)!=null){
				if(GuiUtils.is_number(pulses)){
					if(GuiUtils.validateAvgPulses(average)){
					if(GuiUtils.is_number(time)){
						if(GuiUtils.is_number(SkipReadingCount)){
							if(GuiUtils.is_float(deviation)){	
								if(GuiUtils.is_number(volt)){
									Boolean validation_status = GuiUtils.ValidateVoltagePercentageInVoltageValue(project_name, volt);
									if (validation_status){
										if(GuiUtils.is_number(no_of_readings)){
											for(int i=1; i<=Integer.parseInt(no_of_readings); i++){
												ProjectController.removeTestPoint(mCurrentNode.getType().toString(), aliasID);
												String summary_display_tp_name = alias_name+ "_" + aliasID + "-" +volt_display_name+ "-" + testname + "-" + Integer.toString(i);
												MySQL_Controller.sp_add_project_components(project_name,summary_display_tp_name,test_type, aliasID, positionID, 
														time , "", "", "","",no_of_readings,"", 
														emin, emax, pulses,SkipReadingCount,
														deviation,runtype, "","",volt,
														"","","","","",
														"","","","","",
														"","","",average);

												ProjectController.UpdateNewTestPointSummaryDataToDB(mCurrentNode.getProjectName(), summary_display_tp_name , mCurrentNode.getType().toString(),  aliasID);
												saved_successfully_count = saved_successfully_count + 1;
											}
										}else{

											saveFailedReason = testname +": No of Error reading is not a valid number";
										}
									}else{

										saveFailedReason = testname +": Voltage validation failed due to configured acceptable limit";
									}	
								}else{

									saveFailedReason = testname +": voltage is not a valid number";
								}
							}else{
								
								saveFailedReason = testname +": deviation is not a valid float number";
							}
						}else{
							
							saveFailedReason = testname +": skip reading is not a valid number";
						}
					}else{
						
						saveFailedReason = testname +": time is not a valid number";
					}
					
				}else{

					saveFailedReason = testname +": average is not a valid number";
				}
				}else{
					
					saveFailedReason = testname +": pulses is not a valid number";
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
	
	public void SaveStdDataTrigger(){
		ApplicationLauncher.logger.info("PropertySelfHeatingController : SaveStdDataTrigger Invoked:");
		
		SaveDataTimer = new Timer();
		SaveDataTimer.schedule(new SaveStdDataTask(),100);
	}
	

			
	public boolean IsFieldEmpty(){
		if (!ref_cmbBox_Un.getSelectionModel().getSelectedItem().isEmpty()){
			if (!ref_txt_warmup_duration.getText().isEmpty()){
				if (!ref_txt_no_of_readings.getText().isEmpty()){
					
/*					if (!ref_txtTestPulseNo.getText().isEmpty()){
						return false;
					}else{
						ApplicationLauncher.logger.info ("IsFieldEmpty: <Test> field is empty Prompt:");
						ApplicationLauncher.InformUser("Empty Field", "<No of reading> field is empty",AlertType.ERROR);
						return true;
					}
					*/
					return false;
				}else{
					ApplicationLauncher.logger.info ("IsFieldEmpty: <No of reading> field is empty Prompt:");
					ApplicationLauncher.InformUser("Empty Field", "<No of reading> field is empty",AlertType.ERROR);
					return true;
				}
			}else{

				ApplicationLauncher.logger.info ("IsFieldEmpty: <Warmup Duration> field is empty Prompt:");
				ApplicationLauncher.InformUser("Empty Field", "<Warmup Duration> field is empty",AlertType.ERROR);
				return true;

			}
		}else{

				ApplicationLauncher.logger.info ("IsFieldEmpty: <Un> field is empty Prompt:");
				ApplicationLauncher.InformUser("Empty Field", "<Un> field is empty",AlertType.ERROR);
				return true;
		}
		
	}
	
	class SaveStdDataTask extends TimerTask {
		public void run() {
			
			
			if (!IsFieldEmpty()){

					ApplicationLauncher.setCursor(Cursor.WAIT);


					mCurrentNode.NewNode(false);
					PropertyInfluenceController.delete_previous_saved_data();
					SaveToDB();
					ProjectController.SaveProject();
					ProjectController.LoadSummaryDataToGUI();

					ApplicationLauncher.setCursor(Cursor.DEFAULT);
			}
			SaveDataTimer.cancel();
		}
	}
}
