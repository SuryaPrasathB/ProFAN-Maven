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

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.Cursor;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

public class PropertyVoltageUnbalanceController extends AnchorPane {

	Timer SaveDataTimer;
	@FXML TextField txtTestType;
	public static TextField ref_txtTestType;

	@FXML TextField txtAlias_ID;
	public static TextField ref_txtAlias_ID;


	@FXML CheckBox checkbox_ABC;
	public static CheckBox ref_checkbox_ABC;

	@FXML CheckBox checkbox_AB;
	public static CheckBox ref_checkbox_AB;

	@FXML CheckBox checkbox_BC;
	public static CheckBox ref_checkbox_BC;

	@FXML CheckBox checkbox_AC;
	public static CheckBox ref_checkbox_AC;

	@FXML CheckBox checkbox_A;
	public static CheckBox ref_checkbox_A;

	@FXML CheckBox checkbox_B;
	public static CheckBox ref_checkbox_B;

	@FXML CheckBox checkbox_C;
	public static CheckBox ref_checkbox_C;

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
		ref_checkbox_ABC = checkbox_ABC;
		ref_checkbox_AB = checkbox_AB;
		ref_checkbox_BC = checkbox_BC;
		ref_checkbox_AC = checkbox_AC;
		ref_checkbox_A = checkbox_A;
		ref_checkbox_B = checkbox_B;
		ref_checkbox_C = checkbox_C;

		if(ProcalFeatureEnable.PHASE_DISPLAY_ENABLE_FEATURE){
			ref_checkbox_ABC.setText(ConstantApp.FIRST_PHASE_DISPLAY_NAME+ConstantApp.SECOND_PHASE_DISPLAY_NAME+ConstantApp.THIRD_PHASE_DISPLAY_NAME);
			ref_checkbox_AB.setText(ConstantApp.FIRST_PHASE_DISPLAY_NAME+ConstantApp.SECOND_PHASE_DISPLAY_NAME);
			ref_checkbox_BC.setText(ConstantApp.SECOND_PHASE_DISPLAY_NAME+ConstantApp.THIRD_PHASE_DISPLAY_NAME);
			ref_checkbox_AC.setText(ConstantApp.FIRST_PHASE_DISPLAY_NAME+ConstantApp.THIRD_PHASE_DISPLAY_NAME);
			ref_checkbox_A.setText(ConstantApp.FIRST_PHASE_DISPLAY_NAME);
			ref_checkbox_B.setText(ConstantApp.SECOND_PHASE_DISPLAY_NAME);
			ref_checkbox_C.setText(ConstantApp.THIRD_PHASE_DISPLAY_NAME);
		}

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
		ApplicationLauncher.logger.debug("PropertyVoltageUnbalanceController:  applyUacSettings: Entry");
		if(!ProjectController.isChildPropertySaveEnabled()){
			btn_Save.setDisable(true);
		}
		
	}

	public static  void PropertyDisplayUpdate(String testType, String aliasId,DraggableTestNode SelectedNode){
		mCurrentNode = SelectedNode;
		ApplicationLauncher.logger.info("testType: " + testType);
		ApplicationLauncher.logger.info("aliasId: " + aliasId);
		ref_txtTestType.setText(testType);
		ref_txtAlias_ID.setText(aliasId);

		if(mCurrentNode.IsNewNode() != true){
			load_saved_data(testType, aliasId);
		}
	}

	public static void load_saved_data(String testType, String aliasId){
		String project_name = mCurrentNode.getProjectName();
		JSONObject testdetailslist = MySQL_Controller.sp_getproject_components(project_name, testType, aliasId);
		ApplicationLauncher.logger.info("testdetailslist: " + testdetailslist);
		ArrayList<String> voltages = new ArrayList<String>();
		try {
			JSONArray testcase = testdetailslist.getJSONArray("test_details");
/*			JSONObject test = testcase.getJSONObject(0);
			String u1 = test.getString("inf_voltage_unbalance_u1");
			String u2 = test.getString("inf_voltage_unbalance_u2");
			String u3 = test.getString("inf_voltage_unbalance_u3");*/
			if(testcase.length()>0){
				
				String test_case_name ="";
				JSONObject test = new JSONObject();
				for (int i=0; i< testcase.length();i++){
					test = testcase.getJSONObject(i);
					test_case_name = test.getString("test_case_name");
					ApplicationLauncher.logger.info("testdetailslist: test_case_name:" + test_case_name);
					if(test_case_name.contains("-RYB:100U")){
						ref_checkbox_ABC.setSelected(true);
					}else if(test_case_name.contains("-RY:100U")){
						ref_checkbox_AB.setSelected(true);
					}else if(test_case_name.contains("-YB:100U")){
						ref_checkbox_BC.setSelected(true);
					}else if(test_case_name.contains("-RB:100U")){
						ref_checkbox_AC.setSelected(true);
					}else if(test_case_name.contains("-R:100U")){
						ref_checkbox_A.setSelected(true);
					}else if(test_case_name.contains("-Y:100U")){
						ref_checkbox_B.setSelected(true);
					}else if(test_case_name.contains("-B:100U")){
						ref_checkbox_C.setSelected(true);
					}
					

				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("PropertyVoltageUnbalanceController: load_saved_data: JSONException:"+e.getMessage());
		}
	}





	public ArrayList<String> getVoltages(String selected_phase){
		ArrayList<String> voltages = new ArrayList<String>();


		String  All_Phase = 	"ABC";
		String FirstPhase = "A";
		String SecondPhase = "B";
		String ThirdPhase = "C";
		String FirstAndSecondPhase = 	"AB";
		String SecondAndThirdPhase = 	"BC";
		String FirstAndThirdPhase = 	"AC";
		if(ProcalFeatureEnable.PHASE_DISPLAY_ENABLE_FEATURE){
			All_Phase = ConstantApp.FIRST_PHASE_DISPLAY_NAME +ConstantApp.SECOND_PHASE_DISPLAY_NAME +ConstantApp.THIRD_PHASE_DISPLAY_NAME ;
			FirstPhase = ConstantApp.FIRST_PHASE_DISPLAY_NAME;
			SecondPhase = ConstantApp.SECOND_PHASE_DISPLAY_NAME;
			ThirdPhase = ConstantApp.THIRD_PHASE_DISPLAY_NAME;
			FirstAndSecondPhase = ConstantApp.FIRST_PHASE_DISPLAY_NAME	+ConstantApp.SECOND_PHASE_DISPLAY_NAME;
			SecondAndThirdPhase = ConstantApp.SECOND_PHASE_DISPLAY_NAME + ConstantApp.THIRD_PHASE_DISPLAY_NAME;
			FirstAndThirdPhase = 	ConstantApp.FIRST_PHASE_DISPLAY_NAME + ConstantApp.THIRD_PHASE_DISPLAY_NAME;
		}



		if ( selected_phase.equals(All_Phase)){

			voltages.add("100");
			voltages.add("100");
			voltages.add("100");


		}	else if ( selected_phase.equals(FirstAndSecondPhase)){
			voltages.add("100");
			voltages.add("100");
			voltages.add("0");


		}	else if ( selected_phase.equals(SecondAndThirdPhase)){
			voltages.add("0");
			voltages.add("100");
			voltages.add("100");


		}	else if ( selected_phase.equals(FirstAndThirdPhase)){
			voltages.add("100");
			voltages.add("0");
			voltages.add("100");


		}	else if ( selected_phase.equals(FirstPhase)){
			voltages.add("100");
			voltages.add("0");
			voltages.add("0");


		}	else if (selected_phase.equals(SecondPhase)){
			voltages.add("0");
			voltages.add("100");
			voltages.add("0");


		}	else if (selected_phase.equals(ThirdPhase)){
			voltages.add("0");
			voltages.add("0");
			voltages.add("100");

		}
		else{
			ApplicationLauncher.logger.info("getVoltages : No phase matching, setting all voltage to zero " );

			voltages.add("0");
			voltages.add("0");
			voltages.add("0");
		}


		return voltages;
	}

	public ArrayList<String> getSelectedPhases(){
		ArrayList<String> voltages = new ArrayList<String>();
		ref_checkbox_ABC = checkbox_ABC;
		ref_checkbox_AB = checkbox_AB;
		ref_checkbox_BC = checkbox_BC;
		ref_checkbox_AC = checkbox_AC;
		ref_checkbox_A = checkbox_A;
		ref_checkbox_B = checkbox_B;
		ref_checkbox_C = checkbox_C;

		String  All_Phase = 	"ABC";
		String FirstPhase = "A";
		String SecondPhase = "B";
		String ThirdPhase = "C";
		String FirstAndSecondPhase = 	"AB";
		String SecondAndThirdPhase = 	"BC";
		String FirstAndThirdPhase = 	"AC";
		if(ProcalFeatureEnable.PHASE_DISPLAY_ENABLE_FEATURE){
			All_Phase = ConstantApp.FIRST_PHASE_DISPLAY_NAME +ConstantApp.SECOND_PHASE_DISPLAY_NAME +ConstantApp.THIRD_PHASE_DISPLAY_NAME ;
			FirstPhase = ConstantApp.FIRST_PHASE_DISPLAY_NAME;
			SecondPhase = ConstantApp.SECOND_PHASE_DISPLAY_NAME;
			ThirdPhase = ConstantApp.THIRD_PHASE_DISPLAY_NAME;
			FirstAndSecondPhase = ConstantApp.FIRST_PHASE_DISPLAY_NAME	+ConstantApp.SECOND_PHASE_DISPLAY_NAME;
			SecondAndThirdPhase = ConstantApp.SECOND_PHASE_DISPLAY_NAME + ConstantApp.THIRD_PHASE_DISPLAY_NAME;
			FirstAndThirdPhase = 	ConstantApp.FIRST_PHASE_DISPLAY_NAME + ConstantApp.THIRD_PHASE_DISPLAY_NAME;
		}

		if(ref_checkbox_ABC.isSelected()){

			voltages.add(All_Phase);
		}
		if(ref_checkbox_AB.isSelected()){

			voltages.add(FirstAndSecondPhase);
		}
		if(ref_checkbox_BC.isSelected()){

			voltages.add(SecondAndThirdPhase);
		}
		if(ref_checkbox_AC.isSelected()){

			voltages.add(FirstAndThirdPhase);
		}
		if(ref_checkbox_A.isSelected()){

			voltages.add(FirstPhase);
		}
		if(ref_checkbox_B.isSelected()){

			voltages.add(SecondPhase);
		}
		if(ref_checkbox_C.isSelected()){

			voltages.add(ThirdPhase);
		}
		return voltages;
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
			String emax = "";
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
				SaveVoltUnbToDB(testname, emin, emax, pulses,average, time, SkipReadingCount, deviation, runtype);
			}
			ArrayList<String> selectedphases = getSelectedPhases();
			int no_of_test_points = selectedphases.size()*columnData.size();
			ApplicationLauncher.logger.info("SaveToDB: no_of_test_points: " + no_of_test_points);
			ApplicationLauncher.logger.info("SaveToDB: saved_successfully_count: " + saved_successfully_count);
			if(saved_successfully_count == no_of_test_points){
				ApplicationLauncher.logger.info ("PropertyVoltageUnbalanceController: SaveToDB: Test point saved successfully");

				ApplicationLauncher.InformUser("Saved", "Test point saved successfully",AlertType.INFORMATION);
			}
			else{
				ApplicationLauncher.logger.info ("PropertyVoltageUnbalanceController: SaveToDB: Test point save failed. Test point save failed:" +saveFailedReason);
				ApplicationLauncher.InformUser("Incorrect Value", "Test point save failed: " + saveFailedReason,AlertType.ERROR);
			}

		}
		else{
			ApplicationLauncher.logger.info ("PropertyVoltageUnbalanceController: SaveToDB:  Save the test point inputs - warning triggered");
			ApplicationLauncher.InformUser("Not Saved","Save the test point inputs",AlertType.WARNING);
		}
	}

	public void SaveVoltUnbToDB(String testname, String emin, String emax, String pulses,String average ,
			String time, String SkipReadingCount, String deviation, String runtype){
		ArrayList<String> selectedphases = getSelectedPhases();
		String project_name = mCurrentNode.getProjectName();
		String test_type = mCurrentNode.getType().toString();
		String aliasID = txtAlias_ID.getText();
		String positionID = Integer.toString(mCurrentNode.getPositionId());
		String testtype = txtTestType.getText();
		String testcasename = testtype + "-" + testname;
		String alias_name = mCurrentNode.getAliasName();
		for(int i=0; i<selectedphases.size(); i++){
			String phase = selectedphases.get(i);
			ArrayList<String> selectedvolt = getVoltages(phase);
			String volt_display_name = GuiUtils.FormatUnForDisplay("100");
			String summary_display_tp_name = alias_name+ "_" + aliasID + "-" +phase+":"+volt_display_name+ "-" + testname;
			if(GuiUtils.FormatErrorInput(emin)!=null){
				if(GuiUtils.FormatErrorInput(emax)!=null){
					if(GuiUtils.is_number(pulses)){
						if(GuiUtils.validateAvgPulses(average)){
						if(GuiUtils.is_number(time)){
							if(GuiUtils.is_number(SkipReadingCount)){
								if(GuiUtils.is_float(deviation)){	
									if(GuiUtils.is_number(selectedvolt.get(0))){
										if(GuiUtils.is_number(selectedvolt.get(1))){
											if(GuiUtils.is_number(selectedvolt.get(2))){
												ProjectController.removeTestPoint(mCurrentNode.getType().toString(), aliasID);
												MySQL_Controller.sp_add_project_components(project_name,summary_display_tp_name,test_type, aliasID, positionID,
														time , "","", "","","","", 
														emin, emax, pulses, SkipReadingCount,deviation,runtype, "","","",
														selectedvolt.get(0),selectedvolt.get(1),selectedvolt.get(2),
														"","","","","","","","","","",average);

												ProjectController.UpdateNewTestPointSummaryDataToDB(mCurrentNode.getProjectName(), summary_display_tp_name , mCurrentNode.getType().toString(),  aliasID);
												saved_successfully_count = saved_successfully_count + 1;

											}
											else{
												saveFailedReason = testname +": R-Phase voltage percentage value is incorrect";
											}
										}
										else{
											saveFailedReason = testname +": Y-Phase voltage percentage value is incorrect";
										}
									}
									else{
										saveFailedReason = testname +": B-Phase voltage percentage value is incorrect";
									}
								}
								else{
									saveFailedReason = testname +": Deviation value is incorrect";
								}
							}
							else{
								saveFailedReason = testname +": Skip reading value is incorrect";
							}
						}
						else{
							saveFailedReason = testname +": Time value is incorrect";
						}
						
					}else{

						saveFailedReason = testname +": average is not a valid number";
					}
					}
					else{
						saveFailedReason = testname +": Pulses value is incorrect";
					}
				}
				else{
					saveFailedReason = testname +": Emax value is incorrect";
				}
			}
			else{
				saveFailedReason = testname +": Emin value is incorrect";
			}
		}
	}
/*
	public void InformUser(String title, String info,AlertType Alert_type){
		TextBoxDialog TextBoxDialogobj = new TextBoxDialog();
		TextBoxDialogobj.TriggerUserInfoPlatFormLater(title, info, Alert_type);
	}*/

	public void SaveVoltageUnbalanceTrigger(){

		ApplicationLauncher.logger.info("SaveVoltageUnbalanceTrigger Invoked:");
		if(		ref_checkbox_ABC.isSelected() ||
				ref_checkbox_AB.isSelected() ||
				ref_checkbox_BC.isSelected() ||
				ref_checkbox_AC.isSelected() ||
				ref_checkbox_A.isSelected() ||
				ref_checkbox_B.isSelected() ||
				ref_checkbox_C.isSelected() ){
			SaveDataTimer = new Timer();
			SaveDataTimer.schedule(new SaveVoltageUnbalanceDataTask(),100);
		}else{
			ApplicationLauncher.logger.info ("SaveVoltageUnbalanceTrigger:  Atleast one check box should be selected");
			ApplicationLauncher.InformUser("Not Saved","Atleast one check box should be selected",AlertType.ERROR);
		}
	}


	class SaveVoltageUnbalanceDataTask extends TimerTask {
		public void run() {
			ApplicationLauncher.setCursor(Cursor.WAIT);


			mCurrentNode.NewNode(false);	
			PropertyInfluenceController.delete_previous_saved_data();
			SaveToDB();
			ProjectController.SaveProject();
			ProjectController.LoadSummaryDataToGUI();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			SaveDataTimer.cancel();
		}
	}
}

