package com.tasnetwork.calibration.energymeter.testprofiles;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

public class PropertyAccuracyController extends AnchorPane{


	@FXML TextField txtTestType;
	public static TextField ref_txtTestType;

	@FXML TextField txtAlias_ID;
	public static TextField ref_txtAlias_ID;


	Timer SaveDataTimer;

	@FXML ComboBox<String> cmbBox_Un;
	public static ComboBox<String> ref_cmbBox_Un;

	public static DraggableTestNode mCurrentNode=null;

	@FXML Button btn_Save;
	static int saved_successfully_count =0;

	private String saveFailedReason = "";

	@FXML
	private void initialize() throws IOException {


		ref_txtTestType = txtTestType;
		ref_txtAlias_ID = txtAlias_ID;

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
		ApplicationLauncher.logger.debug("PropertyAccuracyController:  applyUacSettings: Entry");
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
				if(testcase.length()>0){
					JSONObject test = testcase.getJSONObject(0);
					ref_cmbBox_Un.setValue(test.getString("voltage"));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("PropertyAccuracyController: PropertyDisplayUpdate : Exception: " + e.getMessage());
			}
		}
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
		//ApplicationLauncher.logger.info("SaveToDB: SummaryData:"+ProjectController.SummaryData.toString());
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
				SaveAccuracyToDB(testname, emin, emax, pulses,average, time, SkipReadingCount, deviation, runtype);
			}
			//ProjectController.RefreshSummaryTableWithSummaryData();
			int no_of_test_points = 0;
			//if(test_type.equals("PhaseReversal")){
			if(test_type.equals(TestProfileType.PhaseReversal.toString())){
				no_of_test_points = columnData.size()*2;
			}
			else{
				no_of_test_points = columnData.size();
			}
			ApplicationLauncher.logger.info("PropertyAccuracyController : SaveToDB: no_of_test_points: " + no_of_test_points);
			ApplicationLauncher.logger.info("PropertyAccuracyController : SaveToDB: saved_successfully_count: " + saved_successfully_count);
			if(saved_successfully_count == no_of_test_points){
				ApplicationLauncher.InformUser("Saved", "Test point saved successfully",AlertType.INFORMATION);
			}
			else{

				ApplicationLauncher.logger.info ("PropertyAccuracyController: SaveToDB: Test point save failed. Test point save failed:" +saveFailedReason);
				ApplicationLauncher.InformUser("Incorrect Value", "Test point save failed: " + saveFailedReason,AlertType.ERROR);
			}
		}
		else{
			ApplicationLauncher.logger.info("PropertyAccuracyController : SaveToDB: Kindly save the test point inputs - prompted to user ");
			ApplicationLauncher.InformUser("Not Saved"," Kindly save the test point inputs",AlertType.WARNING);
		}
		ApplicationLauncher.logger.info("SaveToDB: SummaryData.size:"+ProjectController.SummaryData.size());
	}

	public void SaveAccuracyToDB(String testname, String emin, String emax, String pulses, String average , 
			String time, String SkipReadingCount, String deviation, String runtype){
		String project_name = mCurrentNode.getProjectName();
		String test_type = mCurrentNode.getType().toString();
		String aliasID = txtAlias_ID.getText();
		String positionID = Integer.toString(mCurrentNode.getPositionId());
		String testtype = txtTestType.getText();
		String alias_name = mCurrentNode.getAliasName();
		String volt = ref_cmbBox_Un.getValue();
		String volt_display_name = GuiUtils.FormatUnForDisplay(volt);
		String summary_display_tp_name = alias_name+ "_" + aliasID + "-" +volt_display_name+ "-" + testname;
		if(GuiUtils.FormatErrorInput(emin)!=null){
			if(GuiUtils.FormatErrorInput(emax)!=null){
				if(GuiUtils.is_number(pulses)){
					if(GuiUtils.validateAvgPulses(average)){
					if(GuiUtils.is_number(time)){
						if(GuiUtils.is_number(SkipReadingCount)){
							if(GuiUtils.is_float(deviation)){	
								if(GuiUtils.is_number(volt)){
									Boolean validation_status = GuiUtils.ValidateVoltagePercentageInVoltageValue(project_name, volt);
									if(validation_status){
										ProjectController.removeTestPoint(mCurrentNode.getType().toString(), aliasID);
										//if(test_type.equals("PhaseReversal")){
										if(test_type.equals(TestProfileType.PhaseReversal.toString())){
											ApplicationLauncher.logger.info("PhaseReversal Entry");
											String phaserev_norm = ConstantApp.PHASEREVERSAL_NORMAL_ALIAS_NAME;
											String normal_phaserev_name = phaserev_norm+ "_" + aliasID + "-" +volt_display_name+ "-" +  testname;

											MySQL_Controller.sp_add_project_components(project_name,normal_phaserev_name,test_type, aliasID, positionID, 
													time , "", "", "","","","", 
													emin, emax, pulses, SkipReadingCount,deviation,runtype,"","",volt,
													"","","","","",
													"","","","","",
													"","","",average);
											ProjectController.UpdateNewTestPointSummaryDataToDB(mCurrentNode.getProjectName(), normal_phaserev_name , mCurrentNode.getType().toString(),  aliasID);
											saved_successfully_count = saved_successfully_count + 1;
											MySQL_Controller.sp_add_project_components(project_name,summary_display_tp_name,test_type, aliasID, positionID, 
													time , "", "", "","","","", 
													emin, emax, pulses, SkipReadingCount,deviation,runtype,"","",volt,
													"","","","","",
													"","","","","",
													"","","",average);
											ProjectController.UpdateNewTestPointSummaryDataToDB(mCurrentNode.getProjectName(), summary_display_tp_name , mCurrentNode.getType().toString(),  aliasID);
											saved_successfully_count = saved_successfully_count + 1;
										}
										else{
											MySQL_Controller.sp_add_project_components(project_name,summary_display_tp_name,test_type, aliasID, positionID, 
													time , "", "", "","","","", 
													emin, emax, pulses, SkipReadingCount,deviation,runtype,"","",volt,
													"","","","","",
													"","","","","",
													"","","",average);
											ProjectController.UpdateNewTestPointSummaryDataToDB(mCurrentNode.getProjectName(), summary_display_tp_name , mCurrentNode.getType().toString(),  aliasID);
											saved_successfully_count = saved_successfully_count + 1;
										}
									}else{

										saveFailedReason = testname +": Voltage validation failed due to configured acceptable limit";
									}
								}else{

									saveFailedReason = testname +": voltage is not a valid number";
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

/*	public void InformUser(String title, String info,AlertType Alert_type){
		TextBoxDialog TextBoxDialogobj = new TextBoxDialog();
		TextBoxDialogobj.TriggerUserInfoPlatFormLater(title, info, Alert_type);
	}*/

	public void SaveStdDataTrigger() {
		ApplicationLauncher.logger.info("PropertyAccuracyController :SaveStdDataTrigger Invoked:");

		SaveDataTimer = new Timer();
		SaveDataTimer.schedule(new SaveStdDataTask(),100);// 1000);

	}
	public static String DiffTime(String dateStart, String dateStop) {


		//ApplicationLauncher.logger.debug("DiffTime: Entry");
		//HH converts hour in 24 hours format (0-23), day calculation
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		Date d1 = null;
		Date d2 = null;

		try {
			d1 = format.parse(dateStart);
			d2 = format.parse(dateStop);

			//in milliseconds
			long diff = d2.getTime() - d1.getTime();

			long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;
			long diffDays = diff / (24 * 60 * 60 * 1000);

			/*			ApplicationLauncher.logger.info(diffDays + " days, ");
			ApplicationLauncher.logger.info(diffHours + " hours, ");
			ApplicationLauncher.logger.info(diffMinutes + " minutes, ");
			ApplicationLauncher.logger.info(diffSeconds + " seconds.");*/
			return (diffHours +":"+diffMinutes+":"+diffSeconds);

		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.info("DiffTime: Exception: "+e.getMessage());
			return "";
		}

	}

	class SaveStdDataTask extends TimerTask {
		public void run() {
			
			ApplicationLauncher.setCursor(Cursor.WAIT);
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
			
			LocalDateTime DB_StartTime = LocalDateTime.now();
			ApplicationLauncher.logger.info("SaveStdDataTask: DB Fetch Start Time: "+dtf.format(DB_StartTime));
			//DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss:");
			mCurrentNode.NewNode(false);
			//2-Tobe deleted

			//3- newly added
			// 4 process summary data
			PropertyInfluenceController.delete_previous_saved_data();
			//ProjectController.RefreshSummaryDataFromDB();
			SaveToDB();
			ProjectController.SaveProject();
			ProjectController.LoadSummaryDataToGUI();
			//ProjectController.LoadSummaryDataAndUpdateDB_IfDataNotExist(ProjectController.getProjectName());
			LocalDateTime DB_EndTime = LocalDateTime.now();
			ApplicationLauncher.logger.info("SaveStdDataTask: DB Fetch End Time: "+dtf.format(DB_EndTime));

			ApplicationLauncher.logger.info("SaveStdDataTask: Difference Time: "+ DiffTime(dtf.format(DB_StartTime),dtf.format(DB_EndTime)));

			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			SaveDataTimer.cancel();
		}
	}
}
