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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.Cursor;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

public class PropertyFrequencyController extends AnchorPane {

	Timer SaveDataTimer;
	@FXML TextField txtTestType;
	public static TextField ref_txtTestType;

	@FXML TextField txtAlias_ID;
	public static TextField ref_txtAlias_ID;

	@FXML ComboBox<String> cmbBox_Un;
	public static ComboBox<String>  ref_cmbBox_Un;


	@FXML TextField txt_frequency_input;
	public static TextField ref_txt_frequency_input;

	@FXML ListView<String> listview_freq_list;
	public static ListView<String> ref_listview_freq_list;

	ArrayList<String> freq_list = new ArrayList<String>();

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
		ref_txt_frequency_input = txt_frequency_input;
		ref_listview_freq_list = listview_freq_list;
		ref_cmbBox_Un = cmbBox_Un;

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
		ApplicationLauncher.logger.debug("PropertyFrequencyController:  applyUacSettings: Entry");
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

		ArrayList<String> volts = new ArrayList<String>();

		volts.add("100");
		volts.add("125");
		volts.add("150");

		ref_cmbBox_Un.getItems().clear();
		ref_cmbBox_Un.getItems().addAll(volts);
		ref_cmbBox_Un.getSelectionModel().select(0) ;

		if(mCurrentNode.IsNewNode() != true){
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
			String inf_freq = "";
			String volt = "";
			for(int i=0; i<testcase.length();i++){
				test = testcase.getJSONObject(i);
				inf_freq = test.getString("frequency");
				volt = test.getString("voltage");
				ref_cmbBox_Un.setValue(volt);
				if(!checkdataexists(inf_freq)){
					ApplicationLauncher.logger.info("Adding Frequency: " + inf_freq);
					ref_listview_freq_list.getItems().add(inf_freq);
				}

			}


		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("PropertyFrequencyController: load_saved_data: JSONException:"+e.getMessage());
			
		}
	}

	public static boolean checkdataexists(String inf_freq){
		boolean dataexists = false;
		ObservableList<String> existing_data = ref_listview_freq_list.getItems();
		if(existing_data.size()!=0){
			String data = "";
			for(int i=0; i<existing_data.size(); i++){
				data = existing_data.get(i);
				if(inf_freq.equals(data)){
					dataexists = true;
					break;
				}
				else{
					dataexists = false;
				}
			}
		}
		else{
			dataexists = false;
		}
		return dataexists;
	}

	public void AddFrequencyOnClick(){
		ObservableList<String> frequencies =  ref_listview_freq_list.getItems();
		if(frequencies.size() <= ConstantApp.FREQUENCY_VARIATION_LIST_ACCEPTED_COUNT){//11){
			String freq = ref_txt_frequency_input.getText();
			if(!freq.isEmpty()){
				Boolean validation_status = GuiUtils.Validate_frequency(freq);
				if (validation_status){
					if(!checkdataexists(freq)){
						ref_listview_freq_list.getItems().add(freq);
						ref_txt_frequency_input.clear();
					}else{
						saveFailedReason = "Frequency value already exist";
						ApplicationLauncher.logger.info ("PropertyFrequencyController: AddFrequencyOnClick: "+ saveFailedReason);
						ApplicationLauncher.InformUser("Limits not in range", saveFailedReason ,AlertType.ERROR);
					
					}
				}else{
					saveFailedReason = "Frequency validation failed due to configured acceptable limit";
					ApplicationLauncher.logger.info ("PropertyFrequencyController: AddFrequencyOnClick: "+ saveFailedReason);
					ApplicationLauncher.InformUser("Limits not in range", saveFailedReason ,AlertType.ERROR);
				
				}
			}
		}
	}

	public void RemoveFrequencyOnClick(){
		ObservableList<String> frequencies =  ref_listview_freq_list.getItems();
		String rem_freq = ref_listview_freq_list.getSelectionModel().getSelectedItem();
		frequencies.remove(rem_freq);
	}



	public ObservableList<String>  getSelectedFrequencies(){
		ObservableList<String> freqs =  ref_listview_freq_list.getItems();
		ApplicationLauncher.logger.info("Freqs : " + freqs);
		return freqs;
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
				SaveFreqToDB(testname, emin, emax, pulses,average, time, SkipReadingCount, deviation, runtype);
			}
			ObservableList<String>  selectedfreq = getSelectedFrequencies();
			int no_of_test_points = selectedfreq.size()*columnData.size();
			ApplicationLauncher.logger.info("SaveToDB: no_of_test_points: " + no_of_test_points);
			ApplicationLauncher.logger.info("SaveToDB: saved_successfully_count: " + saved_successfully_count);
			if(saved_successfully_count == no_of_test_points){
				ApplicationLauncher.logger.info ("PropertyFrequencyController: SaveToDB: Test point saved successfully");
				ApplicationLauncher.InformUser("Saved", "Test point saved successfully",AlertType.INFORMATION);
			}
			else{

				ApplicationLauncher.logger.info ("PropertyFrequencyController: SaveToDB: Test point save failed. Test point save failed:" +saveFailedReason);
				ApplicationLauncher.InformUser("Incorrect Value", "Test point save failed: " + saveFailedReason,AlertType.ERROR);
			}

		}
		else{
			ApplicationLauncher.logger.info ("PropertyFrequencyController: SaveToDB:  Save the test point inputs - warning triggered");
			ApplicationLauncher.InformUser("Not Saved","Save the test point inputs",AlertType.WARNING);
		}
	}

	public void SaveFreqToDB(String testname, String emin, String emax, String pulses,String average ,
			String time, String SkipReadingCount, String deviation, String runtype){
		ObservableList<String> selectedfreq = getSelectedFrequencies();
		String project_name = mCurrentNode.getProjectName();
		String test_type = mCurrentNode.getType().toString();
		String aliasID = txtAlias_ID.getText();
		String positionID = Integer.toString(mCurrentNode.getPositionId());
		String volt = cmbBox_Un.getValue();
		for(int i =0;i<selectedfreq.size(); i++){
			String testtype = txtTestType.getText();
			String alias_name = mCurrentNode.getAliasName();
			String volt_display_name = GuiUtils.FormatUnForDisplay(volt);
			String summary_display_tp_name = alias_name+ "_" + aliasID + "-" +volt_display_name+ "-" + testname+ "-"+ selectedfreq.get(i);
			if(GuiUtils.FormatErrorInput(emin)!=null){
				if(GuiUtils.FormatErrorInput(emax)!=null){
					if(GuiUtils.is_number(pulses)){
						if(GuiUtils.validateAvgPulses(average)){
						if(GuiUtils.is_number(time)){
							if(GuiUtils.is_number(SkipReadingCount)){
								if(GuiUtils.is_float(deviation)){	
									if(GuiUtils.is_float(selectedfreq.get(i))){
										if(GuiUtils.is_number(volt)){

											/*String RatedVoltageValue = ProjectController.getModelDataFromDB(project_name,"rated_voltage_vd");
											float VoltagePercentageInVoltageValue= (Float.parseFloat(RatedVoltageValue)*Float.parseFloat(volt))/100;
											ApplicationLauncher.logger.debug("SaveFreqToDB: VoltagePercentageInVoltageValue:"+VoltagePercentageInVoltageValue);
											Boolean validation_status = GUIUtils.Validate_voltage(String.valueOf(VoltagePercentageInVoltageValue));
											*/
											Boolean validation_status = GuiUtils.ValidateVoltagePercentageInVoltageValue(project_name, volt);
											if(validation_status){
												ProjectController.removeTestPoint(mCurrentNode.getType().toString(), aliasID);
												MySQL_Controller.sp_add_project_components(project_name,summary_display_tp_name,test_type, aliasID, positionID,
														time , "", "", "","","","", 
														emin, emax, pulses,SkipReadingCount,deviation,runtype, "",selectedfreq.get(i), volt,
														"","","","","",
														"","","","","",
														"","","",average);

												ProjectController.UpdateNewTestPointSummaryDataToDB(mCurrentNode.getProjectName(), summary_display_tp_name , mCurrentNode.getType().toString(),  aliasID);

												saved_successfully_count = saved_successfully_count + 1;
											}else{

												saveFailedReason = testname +": Voltage validation failed due to configured acceptable limit";
											}
										}else{

											saveFailedReason = testname +": voltage is not a valid number";
										}
									}else{

										saveFailedReason = testname +": frequency is not a valid float value";
									}
								}else{

									saveFailedReason = testname +": deviation is not a valid float value";
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
	}

/*	public void InformUser(String title, String info,AlertType Alert_type){
		TextBoxDialog TextBoxDialogobj = new TextBoxDialog();
		TextBoxDialogobj.TriggerUserInfoPlatFormLater(title, info, Alert_type);
	}*/

	public void SaveFrequencyTrigger(){
		ApplicationLauncher.logger.info("SaveFrequencyTrigger Invoked:");
		SaveDataTimer = new Timer();
		SaveDataTimer.schedule(new SaveFrequencyTask(),100);

	}

	public boolean IsFieldEmpty(){
		if (!ref_cmbBox_Un.getSelectionModel().getSelectedItem().isEmpty()){

			if (ref_listview_freq_list.getItems().size()!=0){

				return false;

			}else{

				ApplicationLauncher.logger.info ("IsFieldEmpty: <Frequency list> field is empty Prompt:");
				ApplicationLauncher.InformUser("Empty Field", "<Frequency list> field is empty",AlertType.ERROR);
				return true;

			}
		}else{

			ApplicationLauncher.logger.info ("IsFieldEmpty: <Un> field is empty Prompt:");
			ApplicationLauncher.InformUser("Empty Field", "<Un> field is empty",AlertType.ERROR);
			return true;
		}

	}


	class SaveFrequencyTask extends TimerTask {
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


