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
import com.tasnetwork.calibration.energymeter.constant.ProCalCustomerConfiguration;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.database.MySQL_Controller;
import com.tasnetwork.calibration.energymeter.deployment.TextBoxDialog;
import com.tasnetwork.calibration.energymeter.project.ProjectController;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.Cursor;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

public class PropertyHarmonicController extends AnchorPane{
	
	
	//============================New things added by Pradeep - UnApproved ========================================
	
	@FXML CheckBox checkBoxPhaseR ;
	@FXML CheckBox checkBoxPhaseY ;
	@FXML CheckBox checkBoxPhaseB ;
	
	@FXML Tab tab_R_PhaseData ;
	@FXML Tab tab_Y_PhaseData ;
	@FXML Tab tab_B_PhaseData ;
	
	@FXML TableView tv_R_PhaseData ;
	@FXML TableView tv_Y_PhaseData ;
	@FXML TableView tv_B_PhaseData ;

	@FXML TableColumn<HarmonicsDataModel, Integer> colSerialNum_R_Phase;
	@FXML TableColumn<HarmonicsDataModel, Boolean> colEnable_R_Phase;
	@FXML TableColumn<HarmonicsDataModel, Integer> colHarmonicsOrder_R_Phase;
	@FXML TableColumn<HarmonicsDataModel, Integer> colAmp_R_Phase;
	@FXML TableColumn<HarmonicsDataModel, Integer> colPhaseShift_R;

	@FXML TableColumn<HarmonicsDataModel, Integer> colSerialNum_Y_Phase;
	@FXML TableColumn<HarmonicsDataModel, Boolean> colEnable_Y_Phase;
	@FXML TableColumn<HarmonicsDataModel, Integer> colHarmonicsOrder_Y_Phase;
	@FXML TableColumn<HarmonicsDataModel, Integer> colAmp_Y_Phase;
	@FXML TableColumn<HarmonicsDataModel, Integer> colPhaseShift_Y;

	@FXML TableColumn<HarmonicsDataModel, Integer> colSerialNum_B_Phase;
	@FXML TableColumn<HarmonicsDataModel, Boolean> colEnable_B_Phase;
	@FXML TableColumn<HarmonicsDataModel, Integer> colHarmonicsOrder_B_Phase;
	@FXML TableColumn<HarmonicsDataModel, Integer> colAmp_B_Phase;
	@FXML TableColumn<HarmonicsDataModel, Integer> colPhaseShift_B;

	
	
	
	//==============================================================================================================

	Timer SaveDataTimer;
	@FXML TextField txtTestType;
	public static TextField ref_txtTestType;

	@FXML TextField txtAlias_ID;
	public static TextField ref_txtAlias_ID;

	@FXML ComboBox<String> cmbBox_harmonic1;
	@FXML ComboBox<String> cmbBox_harmonic2;
	//@FXML ComboBox<String> cmbBox_harmonic3;
	//@FXML ComboBox<String> cmbBox_harmonic4;

	public static ComboBox<String> ref_cmbBox_harmonic1;
	public static ComboBox<String> ref_cmbBox_harmonic2;
	/*public static ComboBox<String> ref_cmbBox_harmonic3;
	public static ComboBox<String> ref_cmbBox_harmonic4;*/

	@FXML TextField txt_I_harmonic1;
	public static TextField ref_txt_I_harmonic1;
	@FXML TextField txt_I_harmonic2;
	public static TextField ref_txt_I_harmonic2;
	@FXML TextField txt_V_harmonic1;
	public static TextField ref_txt_V_harmonic1;
	@FXML TextField txt_V_harmonic2;
	public static TextField ref_txt_V_harmonic2;
	/*@FXML TextField txt_I_harmonic3;
	public static TextField ref_txt_I_harmonic3;
	@FXML TextField txt_I_harmonic4;
	public static TextField ref_txt_I_harmonic4;*/

	/*@FXML TextField txt_phase_harmonic1;
	public static TextField ref_txt_phase_harmonic1;
	@FXML TextField txt_phase_harmonic2;
	public static TextField ref_txt_phase_harmonic2;
	@FXML TextField txt_phase_harmonic3;
	public static TextField ref_txt_phase_harmonic3;
	@FXML TextField txt_phase_harmonic4;
	public static TextField ref_txt_phase_harmonic4;*/

	
	@FXML Label labelSecondHarmonic;
	
	@FXML CheckBox checkbox_inphase;
	public static CheckBox ref_checkbox_inphase;
	@FXML CheckBox checkbox_outphase;
	public static CheckBox ref_checkbox_outphase;

	@FXML ComboBox<String> cmbBox_Un;
	public static ComboBox<String>  ref_cmbBox_Un;

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
		ref_cmbBox_harmonic1 = cmbBox_harmonic1;
		ref_cmbBox_harmonic2 = cmbBox_harmonic2;
		/*ref_cmbBox_harmonic3 = cmbBox_harmonic3;
		ref_cmbBox_harmonic4 = cmbBox_harmonic4;*/
		ref_txt_I_harmonic1 = txt_I_harmonic1;
		ref_txt_I_harmonic2 = txt_I_harmonic2;
		ref_txt_V_harmonic1 = txt_V_harmonic1;
		ref_txt_V_harmonic2 = txt_V_harmonic2;
		/*ref_txt_I_harmonic3 = txt_I_harmonic3;
		ref_txt_I_harmonic4 = txt_I_harmonic4;*/
		/*ref_txt_phase_harmonic1 = txt_phase_harmonic1;
		ref_txt_phase_harmonic2 = txt_phase_harmonic2;
		ref_txt_phase_harmonic3 = txt_phase_harmonic3;
		ref_txt_phase_harmonic4 = txt_phase_harmonic4;*/

		ref_checkbox_inphase = checkbox_inphase;
		ref_checkbox_outphase = checkbox_outphase;
		ref_cmbBox_Un = cmbBox_Un;

/*		if((ConstantApp.USER_ACCESS_LEVEL.equals(ConstantApp.TESTER_ACCESS_LEVEL)) ||
				(ConstantApp.USER_ACCESS_LEVEL.equals(ConstantApp.READONLY_ACCESS_LEVEL))){
			btn_Save.setDisable(true);
		}*/
		
		if(ProcalFeatureEnable.USER_ACCESS_CONTROL_ENABLED){
			applyUacSettings();
		}
		
		DisableSecondRowHarmonicInputOnGUI();//Customer decided not to have use the two harmonic in single TestPoint on the GUI, since it is affecting the report
	}
	
	
	private void applyUacSettings() {
		// TODO Auto-generated method stub
		ApplicationLauncher.logger.debug("PropertyHarmonicController:  applyUacSettings: Entry");
		if(!ProjectController.isChildPropertySaveEnabled()){
			btn_Save.setDisable(true);
		}
	}
	
	public void DisableSecondRowHarmonicInputOnGUI(){
		cmbBox_harmonic2.setVisible(false);
		txt_V_harmonic2.setVisible(false);
		txt_I_harmonic2.setVisible(false);
		labelSecondHarmonic.setVisible(false);
	}

	public static  void PropertyDisplayUpdate(String testType, String aliasId,DraggableTestNode SelectedNode){
		mCurrentNode = SelectedNode;
		ref_txtTestType.setText(testType);
		ref_txtAlias_ID.setText(aliasId);
		ArrayList<String> ModelList = new ArrayList<String>();

		ModelList.add("Select");
		int maxHarmonicSupported = ConstantApp.HARMONIC_COMPONENT_MAX;
		
		
		//for(int i=ConstantApp.HARMONIC_COMPONENT_MIN; i<ConstantApp.HARMONIC_COMPONENT_MAX; i++){
		for(int i=ConstantApp.HARMONIC_COMPONENT_MIN; i<maxHarmonicSupported; i++){	
			ModelList.add(Integer.toString(i));
		}

		ref_cmbBox_harmonic1.getItems().clear();
		ref_cmbBox_harmonic1.getItems().addAll(ModelList);
		ref_cmbBox_harmonic1.getSelectionModel().select(0);

		ref_cmbBox_harmonic2.getItems().clear();
		ref_cmbBox_harmonic2.getItems().addAll(ModelList);
		ref_cmbBox_harmonic2.getSelectionModel().select(0);

		ArrayList<String> volts = new ArrayList<String>();

		volts.add("100");
		volts.add("125");
		volts.add("150");

		ref_cmbBox_Un.getItems().clear();
		ref_cmbBox_Un.getItems().addAll(volts);
		ref_cmbBox_Un.getSelectionModel().select(0) ;

		/*ref_cmbBox_harmonic3.getItems().clear();
		ref_cmbBox_harmonic3.getItems().addAll(ModelList);
		ref_cmbBox_harmonic3.getSelectionModel().select(0);

		ref_cmbBox_harmonic4.getItems().clear();
		ref_cmbBox_harmonic4.getItems().addAll(ModelList);
		ref_cmbBox_harmonic4.getSelectionModel().select(0);*/
		try {
			if(mCurrentNode.IsNewNode() != true){
				String project_name = mCurrentNode.getProjectName();
				LoadVolt(project_name, testType, aliasId);
				JSONObject harmonics =MySQL_Controller.sp_getharmonic_data(project_name, testType, aliasId);

				JSONArray harmonic_data = harmonics.getJSONArray("Harmonic_data");
				System.out.println("PropertyDisplayUpdate: harmonic_data: " + harmonic_data);
				JSONObject jobj = new JSONObject();
				int harmonic_no = 0;
				String harmonic_times = "";
				String harmonic_volt = "";
				String harmonic_current = "";
				String harmonic_phase = "";
				for(int i=0; i<harmonic_data.length(); i++){
					jobj = harmonic_data.getJSONObject(i);
					harmonic_no = jobj.getInt("harmonic_no");
					harmonic_times = jobj.getString("harmonic_times");
					harmonic_volt = jobj.getString("harmonic_volt");
					harmonic_current = jobj.getString("harmonic_current");
					harmonic_phase = jobj.getString("harmonic_phase");
					LoadSavedData(harmonic_no, harmonic_times,harmonic_volt, harmonic_current,
							harmonic_phase);
				}


			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("PropertyHarmonicController: PropertyDisplayUpdate: JSONException:"+e.getMessage());
		}
	}

	public static void LoadVolt(String project_name, String testType, String aliasId){
		JSONObject testdetailslist = MySQL_Controller.sp_getproject_components(project_name, testType, aliasId);
		ApplicationLauncher.logger.info("testdetailslist: " + testdetailslist);
		try {
			JSONArray testcase = testdetailslist.getJSONArray("test_details");
			JSONObject test = new JSONObject();
			String volt = "";
			for(int i=0; i<testcase.length();i++){
				test = testcase.getJSONObject(i);
				volt = test.getString("voltage");
				ref_cmbBox_Un.setValue(volt);
			}



		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("LoadVolt: JSONException:"+e.getMessage());
			
		}
	}

	private static void LoadSavedData(int harmonic_no, String harmonic_times, 
			String harmonic_volt, String harmonic_current,
			String harmonic_phase) {
		switch(harmonic_no){

		case 1:
			ref_cmbBox_harmonic1.setValue(harmonic_times);
			ref_txt_V_harmonic1.setText(harmonic_volt);
			ref_txt_I_harmonic1.setText(harmonic_current);
			break;

		case 2:
			ref_cmbBox_harmonic2.setValue(harmonic_times);
			ref_txt_V_harmonic2.setText(harmonic_volt);
			ref_txt_I_harmonic2.setText(harmonic_current); 
			break;

		default:
			break;

		}

		if(harmonic_phase.equals("0")){
			ref_checkbox_inphase.setSelected(true);
		}
		if(harmonic_phase.equals("180")){
			ref_checkbox_outphase.setSelected(true);
		}
	}

	public ArrayList<String> getSelectedTestPointsNames(){
		ArrayList<String>  testpointnames =  new ArrayList<String>();
		testpointnames.add(ConstantApp.HARMONIC_WITHOUT_ALIAS_NAME);
		if(ref_checkbox_inphase.isSelected()){
			testpointnames.add(ConstantApp.HARMONIC_INPHASE_ALIAS_NAME);
		}
		if(ref_checkbox_outphase.isSelected()){
			testpointnames.add(ConstantApp.HARMONIC_OUTOFPHASE_ALIAS_NAME);
		}
		return testpointnames;
	}



	public void SaveHarmonics(String harmonic_1_times, String harmonic_2_times,
			String harmonic_1_volt, String harmonic_2_volt,
			String harmonic_1_current, String harmonic_2_current,
			ArrayList<String> harmonic_phases){

		String project_name = mCurrentNode.getProjectName();
		String test_type = mCurrentNode.getType().toString();
		ApplicationLauncher.logger.info("Node Title: " + txtTestType.getText());
		ApplicationLauncher.logger.info("Node AliasID: " + txtAlias_ID.getText());
		String aliasID = txtAlias_ID.getText();

		if(!(harmonic_1_times.equals("Select"))){
			ApplicationLauncher.logger.info("SaveHarmonics: harmonic_1_times: " + harmonic_1_times);
			if(GuiUtils.is_number(harmonic_1_volt) && !harmonic_1_volt.isEmpty()){
				ApplicationLauncher.logger.info("SaveHarmonics: harmonic_1_volt: " + harmonic_1_volt);
				if(GuiUtils.is_number(harmonic_1_current) && !harmonic_1_current.isEmpty()){
					ApplicationLauncher.logger.info("SaveHarmonics: harmonic_1_current: " + harmonic_1_current);
					if(harmonic_phases.size()!=0){
						ApplicationLauncher.logger.info("SaveHarmonics: harmonic_phases: " + harmonic_phases);
						for(int i=0; i<harmonic_phases.size();i++){
							String testcasename = "";
							if(harmonic_phases.get(i).equals("0")){
								testcasename = ConstantApp.HARMONIC_INPHASE_ALIAS_NAME;
							}
							else{
								testcasename = ConstantApp.HARMONIC_OUTOFPHASE_ALIAS_NAME;
							}
							ApplicationLauncher.logger.info("SaveHarmonics: testcasename: " + testcasename);
							MySQL_Controller.sp_add_harmonic_data(project_name, 
									testcasename, test_type, aliasID, 1, 
									Integer.parseInt(harmonic_1_times),
									harmonic_1_volt, 
									harmonic_1_current, harmonic_phases.get(i));
						}
					}
				}
			}
		}	


		if(!(harmonic_2_times.equals("Select"))){
			if(GuiUtils.is_number(harmonic_2_volt) && !harmonic_2_volt.isEmpty()){
				if(GuiUtils.is_number(harmonic_2_current) && !harmonic_2_current.isEmpty()){
					if(harmonic_phases.size()!=0){
						for(int i=0; i<harmonic_phases.size();i++){
							String testcasename = "";
							if(harmonic_phases.get(i).equals("0")){
								testcasename = ConstantApp.HARMONIC_INPHASE_ALIAS_NAME;
							}
							else{
								testcasename = ConstantApp.HARMONIC_OUTOFPHASE_ALIAS_NAME;
							}
							MySQL_Controller.sp_add_harmonic_data(project_name, 
									testcasename, test_type, aliasID, 2, 
									Integer.parseInt(harmonic_2_times),
									harmonic_2_volt, 
									harmonic_2_current, harmonic_phases.get(i));
						}
					}
				}
			}
		}
/*		ProjectController.SaveProject();
		ProjectController.LoadSummaryDataToGUI();*/
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
				SaveHarmonicToDB(testname, emin, emax, pulses,average, time, SkipReadingCount, deviation, runtype);
			}
			ArrayList<String> selectedpointnames = getSelectedTestPointsNames();
			int no_of_test_points = selectedpointnames.size()*columnData.size();
			ApplicationLauncher.logger.info("SaveToDB: no_of_test_points: " + no_of_test_points);
			ApplicationLauncher.logger.info("SaveToDB: saved_successfully_count: " + saved_successfully_count);
			if(saved_successfully_count == no_of_test_points){
				ApplicationLauncher.logger.info ("PropertyHarmonicController: SaveToDB: Test point saved successfully");
				ApplicationLauncher.InformUser("Saved", "Test point saved successfully",AlertType.INFORMATION);
			}
			else{
				ApplicationLauncher.logger.info ("PropertyHarmonicController: SaveToDB: Test point save failed. Test point save failed:" +saveFailedReason);
				ApplicationLauncher.InformUser("Incorrect Value", "Test point save failed: " + saveFailedReason,AlertType.ERROR);
			}

		}
		else{
			ApplicationLauncher.logger.info ("PropertyHarmonicController: SaveToDB:  Save the test point inputs - warning triggered");
			ApplicationLauncher.InformUser("Not Saved","Save the test point inputs",AlertType.WARNING);
		}

	}


	public void SaveHarmonicToDB(String testname, String emin, String emax, String pulses,String average ,
			String time, String SkipReadingCount, String deviation, String runtype){
		ArrayList<String> selectedpointnames = getSelectedTestPointsNames();
		String project_name = mCurrentNode.getProjectName();
		String test_type = mCurrentNode.getType().toString();
		String aliasID = txtAlias_ID.getText();
		String positionID = Integer.toString(mCurrentNode.getPositionId());
		String volt = cmbBox_Un.getValue();
		String harmonic_1_times = ref_cmbBox_harmonic1.getSelectionModel().getSelectedItem();
		if(harmonic_1_times.equals("Select")){
			harmonic_1_times = "0";
		}
		for(int i =0;i<selectedpointnames.size(); i++){
			if(GuiUtils.FormatErrorInput(emin)!=null){
				if(GuiUtils.FormatErrorInput(emax)!=null){
					if(GuiUtils.is_number(pulses)){
						if(GuiUtils.validateAvgPulses(average)){
						if(GuiUtils.is_number(time)){
							if(GuiUtils.is_number(SkipReadingCount)){
								if(GuiUtils.is_float(deviation)){	
									if(selectedpointnames.size() > 1){ 
										if(GuiUtils.is_number(volt)){
											Boolean validation_status = GuiUtils.ValidateVoltagePercentageInVoltageValue(project_name, volt);
											if (validation_status){
												ProjectController.removeTestPoint(mCurrentNode.getType().toString(), aliasID);
												String alias_name = selectedpointnames.get(i);
												String volt_display_name = GuiUtils.FormatUnForDisplay(volt);
												String summary_display_tp_name = alias_name+ "_" + aliasID + "-" +volt_display_name+ "-" + testname + "-" + harmonic_1_times;
												MySQL_Controller.sp_add_project_components(project_name,summary_display_tp_name,test_type, aliasID, positionID,
														time , "",  "", "","","","",
														emin, emax, pulses, SkipReadingCount,deviation,runtype, "","",volt,
														"","","","","",
														"","","","","",
														"","","",average);

												ProjectController.UpdateNewTestPointSummaryDataToDB(mCurrentNode.getProjectName(), summary_display_tp_name , 
														mCurrentNode.getType().toString(),  aliasID);
												saved_successfully_count = saved_successfully_count + 1;
											}else{

												saveFailedReason = testname +": Voltage validation failed due to configured acceptable limit";
											}
										}else{

											saveFailedReason = testname +": voltage is not a valid number";
										}
									}else{

										saveFailedReason = testname +": Atleast one Test point should be selected!!";
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
	
	@FXML
	public void SaveHarmonicDataTrigger(){
		ApplicationLauncher.logger.info("SaveHarmonicDataTrigger Invoked:");
		SaveDataTimer = new Timer();
		SaveDataTimer.schedule(new SaveHarmonicDataTask(),100);

	}

	public boolean IsFieldEmpty(){
		if(ref_checkbox_inphase.isSelected() || ref_checkbox_outphase.isSelected()){
			if (!ref_cmbBox_harmonic1.getSelectionModel().getSelectedItem().equals("Select")){
				return false;
			}else{
				ApplicationLauncher.logger.info ("IsFieldEmpty: Kindly select the Harmonic Component: Prompt:");
				//Platform.runLater(() -> {
				ApplicationLauncher.InformUser("Empty Field", "Kindly select the Harmonic Component",AlertType.ERROR);
				//});
				return true;

			}
		}else{
			ApplicationLauncher.logger.info ("IsFieldEmpty :  Neithor inphase nor out phase check box selected");
			//Platform.runLater(() -> {

			ApplicationLauncher.InformUser("Not Saved","Please select <In Phase> or <Out Phase> check box",AlertType.ERROR);
			//});
			return true;
		}


	}


	class SaveHarmonicDataTask extends TimerTask {
		public void run() {
			if (!IsFieldEmpty()){
				ApplicationLauncher.logger.info ("SaveHarmonicDataTask:Entry");

				ApplicationLauncher.setCursor(Cursor.WAIT);	
				mCurrentNode.NewNode(false);

				String harmonic_1_times = ref_cmbBox_harmonic1.getSelectionModel().getSelectedItem();
				String harmonic_1_volt = "0";
				String harmonic_1_current = "0";
				String harmonic_2_volt = "0";
				String harmonic_2_current = "0";
				
				
				
				if(!ref_txt_V_harmonic1.getText().isEmpty()){
					harmonic_1_volt = ref_txt_V_harmonic1.getText();
				}

				if(!ref_txt_I_harmonic1.getText().isEmpty()){
					harmonic_1_current = ref_txt_I_harmonic1.getText();
				}
				
				

				String harmonic_2_times = ref_cmbBox_harmonic2.getSelectionModel().getSelectedItem();
				if(!ref_txt_V_harmonic2.getText().isEmpty()){
					harmonic_2_volt = ref_txt_V_harmonic2.getText();
				}
				if(!ref_txt_I_harmonic2.getText().isEmpty()){
					harmonic_2_current = ref_txt_I_harmonic2.getText();
				}

				ArrayList<String> harmonic_phases = new ArrayList<String>();
				if(ref_checkbox_inphase.isSelected()){
					harmonic_phases.add("0");
				}

				if(ref_checkbox_outphase.isSelected()){
					harmonic_phases.add("180");
				}
				
				if(ProCalCustomerConfiguration.ADYA_HYBRID_3NO_3PHASE_6NO_1PHASE_POSITION_2024) {
					int harmonic1MaxPercentAccepted2ndOrder = 30;
					int harmonic1MaxPercentAccepted3rdOrder = 30;
					int harmonic1MaxPercentAccepted4thOrder = 20;
					int harmonic1MaxPercentAccepted5thOrder = 20;
					if(GuiUtils.is_number(harmonic_1_volt) && !harmonic_1_volt.isEmpty()){
						int inputVoltPercentage = Integer.parseInt(harmonic_1_volt);
						if(GuiUtils.is_number(harmonic_1_current) && !harmonic_1_current.isEmpty()){
							int inputCurrentPercentage = Integer.parseInt(harmonic_1_current);
							if(harmonic_1_times.equals("2")) {
								if(inputVoltPercentage>harmonic1MaxPercentAccepted2ndOrder) {
									ApplicationLauncher.logger.info ("PropertyHarmonicController: SaveHarmonicDataTask: inputVoltPercentage: " + inputVoltPercentage);
									ApplicationLauncher.logger.info ("PropertyHarmonicController: SaveHarmonicDataTask: harmonic1MaxPercentAccepted2ndOrder: " + harmonic1MaxPercentAccepted2ndOrder);
									ApplicationLauncher.logger.info ("PropertyHarmonicController: SaveHarmonicDataTask:  input volt percentage greater than acceptable limit-2nd Order");
									ApplicationLauncher.InformUser("Not Saved","Below input is greater than acceptable percentage"
											+ "\n\n User Volt Percent       : " + inputVoltPercentage 
											+   "\n Acceptable Volt Percent : " + harmonic1MaxPercentAccepted2ndOrder 
											,AlertType.WARNING);
									ApplicationLauncher.setCursor(Cursor.DEFAULT);
									SaveDataTimer.cancel();
									return;
								}
								
								if(inputCurrentPercentage>harmonic1MaxPercentAccepted2ndOrder) {
									ApplicationLauncher.logger.info ("PropertyHarmonicController: SaveHarmonicDataTask: inputCurrentPercentage: " + inputCurrentPercentage);
									ApplicationLauncher.logger.info ("PropertyHarmonicController: SaveHarmonicDataTask: harmonic1MaxPercentAccepted2ndOrder: " + harmonic1MaxPercentAccepted2ndOrder);
									ApplicationLauncher.logger.info ("PropertyHarmonicController: SaveHarmonicDataTask:  input Current percentage greater than acceptable limit-2nd Order");
									ApplicationLauncher.InformUser("Not Saved","Below input is greater than acceptable percentage"
											+ "\n\n User Current Percent       : " + inputCurrentPercentage 
											+   "\n Acceptable Current Percent : " + harmonic1MaxPercentAccepted2ndOrder 
											,AlertType.WARNING);
									ApplicationLauncher.setCursor(Cursor.DEFAULT);
									SaveDataTimer.cancel();
									return;
								}
							}else if(harmonic_1_times.equals("3")) {
								if(inputVoltPercentage>harmonic1MaxPercentAccepted3rdOrder) {
									ApplicationLauncher.logger.info ("PropertyHarmonicController: SaveHarmonicDataTask: inputVoltPercentage: " + inputVoltPercentage);
									ApplicationLauncher.logger.info ("PropertyHarmonicController: SaveHarmonicDataTask: harmonic1MaxPercentAccepted3rdOrder: " + harmonic1MaxPercentAccepted3rdOrder );
									ApplicationLauncher.logger.info ("PropertyHarmonicController: SaveHarmonicDataTask:  input volt percentage greater than acceptable limit-3rd Order");
									ApplicationLauncher.InformUser("Not Saved","Below input is greater than acceptable percentage"
											+ "\n\n User Volt Percent       : " + inputVoltPercentage 
											+   "\n Acceptable Volt Percent : " + harmonic1MaxPercentAccepted3rdOrder
											,AlertType.WARNING);
									ApplicationLauncher.setCursor(Cursor.DEFAULT);
									SaveDataTimer.cancel();
									return;
								}
								
								if(inputCurrentPercentage>harmonic1MaxPercentAccepted3rdOrder) {
									ApplicationLauncher.logger.info ("PropertyHarmonicController: SaveHarmonicDataTask: inputCurrentPercentage: " + inputCurrentPercentage);
									ApplicationLauncher.logger.info ("PropertyHarmonicController: SaveHarmonicDataTask: harmonic1MaxPercentAccepted3rdOrder: " + harmonic1MaxPercentAccepted3rdOrder);
									ApplicationLauncher.logger.info ("PropertyHarmonicController: SaveHarmonicDataTask:  input Current percentage greater than acceptable limit-3rd Order");
									ApplicationLauncher.InformUser("Not Saved","Below input is greater than acceptable percentage"
											+ "\n\n User Current Percent       : " + inputCurrentPercentage 
											+   "\n Acceptable Current Percent : " + harmonic1MaxPercentAccepted3rdOrder
											,AlertType.WARNING);
									ApplicationLauncher.setCursor(Cursor.DEFAULT);
									SaveDataTimer.cancel();
									return;
								}
							}else if(harmonic_1_times.equals("4")) {
								if(inputVoltPercentage>harmonic1MaxPercentAccepted4thOrder ) {
									ApplicationLauncher.logger.info ("PropertyHarmonicController: SaveHarmonicDataTask: inputVoltPercentage: " + inputVoltPercentage);
									ApplicationLauncher.logger.info ("PropertyHarmonicController: SaveHarmonicDataTask: harmonic1MaxPercentAccepted4thOrder: " + harmonic1MaxPercentAccepted4thOrder );
									ApplicationLauncher.logger.info ("PropertyHarmonicController: SaveHarmonicDataTask:  input volt percentage greater than acceptable limit-4th Order");
									ApplicationLauncher.InformUser("Not Saved","Below input is greater than acceptable percentage"
											+ "\n\n User Volt Percent       : " + inputVoltPercentage 
											+   "\n Acceptable Volt Percent : " + harmonic1MaxPercentAccepted4thOrder  
											,AlertType.WARNING);
									ApplicationLauncher.setCursor(Cursor.DEFAULT);
									SaveDataTimer.cancel();
									return;
								}
								
								if(inputCurrentPercentage>harmonic1MaxPercentAccepted4thOrder ) {
									ApplicationLauncher.logger.info ("PropertyHarmonicController: SaveHarmonicDataTask: inputCurrentPercentage: " + inputCurrentPercentage);
									ApplicationLauncher.logger.info ("PropertyHarmonicController: SaveHarmonicDataTask: harmonic1MaxPercentAccepted4thOrder: " + harmonic1MaxPercentAccepted4thOrder);
									ApplicationLauncher.logger.info ("PropertyHarmonicController: SaveHarmonicDataTask:  input Current percentage greater than acceptable limit-4th Order");
									ApplicationLauncher.InformUser("Not Saved","Below input is greater than acceptable percentage"
											+ "\n\n User Current Percent       : " + inputCurrentPercentage 
											+   "\n Acceptable Current Percent : " + harmonic1MaxPercentAccepted4thOrder  
											,AlertType.WARNING);
									ApplicationLauncher.setCursor(Cursor.DEFAULT);
									SaveDataTimer.cancel();
									return;
								}
							}else if(harmonic_1_times.equals("5")) {
								if(inputVoltPercentage>harmonic1MaxPercentAccepted5thOrder) {
									ApplicationLauncher.logger.info ("PropertyHarmonicController: SaveHarmonicDataTask: inputVoltPercentage: " + inputVoltPercentage);
									ApplicationLauncher.logger.info ("PropertyHarmonicController: SaveHarmonicDataTask: harmonic1MaxPercentAccepted5thOrder: " + harmonic1MaxPercentAccepted5thOrder);
									ApplicationLauncher.logger.info ("PropertyHarmonicController: SaveHarmonicDataTask:  input volt percentage greater than acceptable limit-5th Order");
									ApplicationLauncher.InformUser("Not Saved","Below input is greater than acceptable percentage"
											+ "\n\n User Volt Percent       : " + inputVoltPercentage 
											+   "\n Acceptable Volt Percent : " + harmonic1MaxPercentAccepted5thOrder  
											,AlertType.WARNING);
									ApplicationLauncher.setCursor(Cursor.DEFAULT);
									SaveDataTimer.cancel();
									return;
								}
								
								if(inputCurrentPercentage>harmonic1MaxPercentAccepted5thOrder ) {
									ApplicationLauncher.logger.info ("PropertyHarmonicController: SaveHarmonicDataTask: inputCurrentPercentage: " + inputCurrentPercentage);
									ApplicationLauncher.logger.info ("PropertyHarmonicController: SaveHarmonicDataTask: harmonic1MaxPercentAccepted5thOrder: " + harmonic1MaxPercentAccepted5thOrder);
									ApplicationLauncher.logger.info ("PropertyHarmonicController: SaveHarmonicDataTask:  input Current percentage greater than acceptable limit-5th Order");
									ApplicationLauncher.InformUser("Not Saved","Below input is greater than acceptable percentage"
											+ "\n\n User Current Percent       : " + inputCurrentPercentage 
											+   "\n Acceptable Current Percent : " + harmonic1MaxPercentAccepted5thOrder  
											,AlertType.WARNING);
									ApplicationLauncher.setCursor(Cursor.DEFAULT);
									SaveDataTimer.cancel();
									return;
								}
							}
						}
					}
					
					
/*					if(GuiUtils.is_number(harmonic_1_current) && !harmonic_1_current.isEmpty()){
						//int inputVoltPercentage = Integer.parseInt(harmonic_1_current);
						int inputCurrentPercentage = Integer.parseInt(harmonic_1_current);
						if(harmonic_1_times.equals("2")) {
							
						}else if(harmonic_1_times.equals("3")) {
							
						}else if(harmonic_1_times.equals("4")) {
							
						}else if(harmonic_1_times.equals("5")) {
							
						}
					}*/
					
					
				}
				
				ApplicationLauncher.logger.info("SaveHarmonicData: harmonic_phases: " + harmonic_phases);

				if(harmonic_phases.size()!=0){
					PropertyInfluenceController.delete_previous_saved_data();
					SaveToDB();
					SaveHarmonics(harmonic_1_times, harmonic_2_times,
							harmonic_1_volt, harmonic_2_volt, 
							harmonic_1_current, harmonic_2_current,
							harmonic_phases);
					ProjectController.SaveProject();
					ProjectController.LoadSummaryDataToGUI();

				}
				ApplicationLauncher.setCursor(Cursor.DEFAULT);
			}
			SaveDataTimer.cancel();
		}
	}
	
//============================New functions added by Pradeep - UnApproved ========================================

	@FXML private void checkBoxPhaseR_OnClick(){
		if(tab_R_PhaseData.isSelected()){
			tab_R_PhaseData.setDisable(false);
		}else{
			tab_R_PhaseData.setDisable(true);
		}
	}

	@FXML private void checkBoxPhaseY_OnClick(){
		if(tab_Y_PhaseData.isSelected()){
			tab_Y_PhaseData.setDisable(false);
		}else{
			tab_Y_PhaseData.setDisable(true);
		}
	}

	@FXML private void checkBoxPhaseB_OnClick(){
		if(tab_B_PhaseData.isSelected()){
			tab_B_PhaseData.setDisable(false);
		}else{
			tab_B_PhaseData.setDisable(true);
		}
	}
	
	@FXML private void tab_R_PhaseDataOnClick(){
	
	}

	@FXML private void tab_Y_PhaseDataOnClick(){
			
		}
	
	@FXML private void tab_B_PhaseDataOnClick(){
		
	}


//==============================================================================================================
	
	
//============================================================================================================	
}

