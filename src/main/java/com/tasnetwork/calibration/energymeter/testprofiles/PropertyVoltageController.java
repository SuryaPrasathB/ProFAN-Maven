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

public class PropertyVoltageController extends AnchorPane {

	Timer SaveDataTimer;
	@FXML TextField txtTestType;
	public static TextField ref_txtTestType;

	@FXML TextField txtAlias_ID;
	public static TextField ref_txtAlias_ID;

	@FXML TextField txt_volt_input;
	public static TextField ref_txt_volt_input;

	@FXML ListView<String> listview_voltlist;
	public static ListView<String> ref_listview_voltlist;

	ArrayList<String> volt_list = new ArrayList<String>();

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
		ref_txt_volt_input = txt_volt_input;
		ref_listview_voltlist = listview_voltlist;
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
		ApplicationLauncher.logger.debug("PropertyVoltageController:  applyUacSettings: Entry");
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
		if(!mCurrentNode.IsNewNode()){
			load_saved_data(testType, aliasId);
		}
	}

	public static void load_saved_data(String testType, String aliasId){
		String project_name = mCurrentNode.getProjectName();
		JSONObject testdetailslist = MySQL_Controller.sp_getproject_components(project_name, testType, aliasId);
		ApplicationLauncher.logger.info("testdetailslist: " + testdetailslist);
		try {
			JSONArray testcase = testdetailslist.getJSONArray("test_details");
			JSONObject test = new JSONObject();
			String inf_volt = "";
			for(int i=0; i<testcase.length();i++){
				test = testcase.getJSONObject(i);
				inf_volt = test.getString("inf_voltage");
				if(!checkdataexists(inf_volt)){
					ApplicationLauncher.logger.info("Adding voltage: " + inf_volt);
					ref_listview_voltlist.getItems().add(inf_volt);
				}

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("PropertyVoltageController: load_saved_data: JSONException:"+e.getMessage());
		}
	}

	public static boolean checkdataexists(String inf_volt){
		boolean dataexists = false;
		ObservableList<String> existing_data = ref_listview_voltlist.getItems();
		String data = "";
		for(int i=0; i<existing_data.size(); i++){
			data = existing_data.get(i);
			if(inf_volt.equals(data)){
				dataexists = true;
				break;
			}
			else{
				dataexists = false;
			}
		}
		return dataexists;
	}

	public void AddVoltageOnClick(){
		ObservableList<String> voltageList =  ref_listview_voltlist.getItems();
		
		if(voltageList.size() <= ConstantApp.VOLTAGE_VARIATION_LIST_ACCEPTED_COUNT){//<11){
			String voltageInPercentage = ref_txt_volt_input.getText();
			if(!voltageInPercentage.isEmpty()){
				String project_name = mCurrentNode.getProjectName();
				//String RatedVoltageValue = ProjectController.getModelDataFromDB(project_name,"rated_voltage_vd");
				//float VoltagePercentageInVoltageValue= (Float.parseFloat(RatedVoltageValue)*Float.parseFloat(voltageInPercentage))/100;
				//ApplicationLauncher.logger.debug("AddVoltageOnClick: VoltagePercentageInVoltageValue:"+VoltagePercentageInVoltageValue);
				//Boolean validation_status = GUIUtils.Validate_voltage(voltageInPercentage);
				//Boolean validation_status = GUIUtils.Validate_voltage(String.valueOf(VoltagePercentageInVoltageValue));
				Boolean validation_status = GuiUtils.ValidateVoltagePercentageInVoltageValue(project_name, voltageInPercentage);
				if (validation_status){
					if(!checkdataexists(voltageInPercentage)){
						ref_listview_voltlist.getItems().add(voltageInPercentage);
						ref_txt_volt_input.clear();
					}
					else{
						saveFailedReason = "Voltage value already exist";
						ApplicationLauncher.logger.info ("PropertyVoltageController: AddVoltageOnClick: "+ saveFailedReason);
						ApplicationLauncher.InformUser("Limits not in range", saveFailedReason ,AlertType.ERROR);
					
					}
				}else{
					saveFailedReason = "Voltage validation failed due to configured acceptable limit";
					ApplicationLauncher.logger.info ("PropertyVoltageController: AddVoltageOnClick: "+ saveFailedReason);
					ApplicationLauncher.InformUser("Limits not in range", saveFailedReason ,AlertType.ERROR);
				
				}
			}
		}
	}

	public void RemoveVoltageOnClick(){
		ObservableList<String> voltages =  ref_listview_voltlist.getItems();
		String rem_volt = ref_listview_voltlist.getSelectionModel().getSelectedItem();
		voltages.remove(rem_volt);
	}



	public ObservableList<String> getSelectedVoltages(){
		ObservableList<String> voltages =  ref_listview_voltlist.getItems();
		ApplicationLauncher.logger.info("voltages: " + voltages);
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
				SaveVoltToDB(testname, emin, emax, pulses, average , time, SkipReadingCount, deviation, runtype);
			}
			ObservableList<String>  selectedvolt = getSelectedVoltages();
			int no_of_test_points = selectedvolt.size()*columnData.size();
			ApplicationLauncher.logger.info("SaveToDB: no_of_test_points: " + no_of_test_points);
			ApplicationLauncher.logger.info("SaveToDB: saved_successfully_count: " + saved_successfully_count);
			if(saved_successfully_count == no_of_test_points){
				ApplicationLauncher.logger.info ("PropertyVoltageController: SaveToDB: Test point saved successfully");
				ApplicationLauncher.InformUser("Saved", "Test Point saved Successfully",AlertType.INFORMATION);
			}
			else{
				ApplicationLauncher.logger.info ("PropertyVoltageController: SaveToDB: Test point save failed. Test point save failed:" +saveFailedReason);

				ApplicationLauncher.InformUser("Incorrect Value", "Test point save failed: " + saveFailedReason,AlertType.ERROR);
			}

		}
		else{
			ApplicationLauncher.logger.info ("PropertyVoltageController: SaveToDB:  Save the test point inputs - warning triggered");

			ApplicationLauncher.InformUser("Not Saved","Save the test point inputs",AlertType.WARNING);
		}
	}

	public void SaveVoltToDB(String testname, String emin, String emax, String pulses,String average ,
			String time, String SkipReadingCount, String deviation, String runtype){
		ObservableList<String> selectedvolt = getSelectedVoltages();
		String project_name = mCurrentNode.getProjectName();
		String test_type = mCurrentNode.getType().toString();
		String aliasID = txtAlias_ID.getText();
		String positionID = Integer.toString(mCurrentNode.getPositionId());
		for(int i =0;i<selectedvolt.size(); i++){
			String testtype = txtTestType.getText();
			String testcasename = testtype + "-" + testname + "-"+ selectedvolt.get(i);
			String alias_name = mCurrentNode.getAliasName();
			String volt_display_name = GuiUtils.FormatUnForDisplay(selectedvolt.get(i));
			String summary_display_tp_name = alias_name+ "_" + aliasID + "-" +volt_display_name+ "-" + testname;
			if(GuiUtils.FormatErrorInput(emin)!=null){
				if(GuiUtils.FormatErrorInput(emax)!=null){
					if(GuiUtils.is_number(pulses)){
						if(GuiUtils.validateAvgPulses(average)){
						if(GuiUtils.is_number(time)){
							if(GuiUtils.is_number(SkipReadingCount)){
								if(GuiUtils.is_float(deviation)){	
									if(GuiUtils.is_number(selectedvolt.get(i))){
										ProjectController.removeTestPoint(mCurrentNode.getType().toString(), aliasID);
										MySQL_Controller.sp_add_project_components(project_name,summary_display_tp_name,test_type, aliasID, positionID,
												time , "",  "", "","","","",
												emin, emax, pulses, SkipReadingCount,deviation,runtype, "","",selectedvolt.get(i),
												"","","","","",
												"","","","","",
												"","","",average);

										ProjectController.UpdateNewTestPointSummaryDataToDB(mCurrentNode.getProjectName(), summary_display_tp_name , mCurrentNode.getType().toString(),  aliasID);
										saved_successfully_count = saved_successfully_count + 1;
									}else{

										saveFailedReason = testname +": Voltage value is incorrect";
									}
								}else{

									saveFailedReason = testname +": Deviation value is incorrect";
								}
							}else{

								saveFailedReason = testname +": Skip Reading value is incorrect";
							}
						}else{

							saveFailedReason = testname +": Time value is incorrect";
						}
						
					}else{

						saveFailedReason = testname +": average is not a valid number";
					}
					}else{

						saveFailedReason = testname +": Pulse value is incorrect";
					}

				}else{

					saveFailedReason = testname +": Error max value is incorrect";
				}
			}else{

				saveFailedReason = testname +": Error min value is incorrect";
			}
		}
	}

/*	public void InformUser(String title, String info,AlertType Alert_type){
		TextBoxDialog TextBoxDialogobj = new TextBoxDialog();
		TextBoxDialogobj.TriggerUserInfoPlatFormLater(title, info, Alert_type);
	}*/

	public void SaveVoltageTrigger(){

		ApplicationLauncher.logger.info("SaveVoltageTrigger Invoked:");
		SaveDataTimer = new Timer();
		SaveDataTimer.schedule(new SaveVoltageDataTask(),100);

	}


	public boolean IsFieldEmpty(){

		if (ref_listview_voltlist.getItems().size()!=0){

			return false;

		}else{

			ApplicationLauncher.logger.info ("IsFieldEmpty: <Voltage list> field is empty Prompt:");
			ApplicationLauncher.InformUser("Empty Field", "<Voltage list> field is empty",AlertType.ERROR);
			return true;

		}

	}

	class SaveVoltageDataTask extends TimerTask {
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


