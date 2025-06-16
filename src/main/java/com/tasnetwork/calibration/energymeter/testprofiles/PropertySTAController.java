package com.tasnetwork.calibration.energymeter.testprofiles;

import java.io.IOException;
import java.text.DecimalFormat;
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

public class PropertySTAController extends AnchorPane{

	Timer SaveDataTimer;
	@FXML TextField txtTestType;
	public static TextField ref_txtTestType;

	@FXML TextField txtAlias_ID;
	public static TextField ref_txtAlias_ID;

	@FXML TextField txtSTAIb;
	public static TextField ref_txtSTAIb;

	@FXML TextField txtSTATestTime;
	public static TextField ref_txtSTATestTime;

	@FXML TextField txtSTATestPulseNo;
	public static TextField ref_txtSTATestPulseNo;

	@FXML ComboBox<String> cmbBoxVolt;
	public static ComboBox<String> ref_cmbBoxVolt;

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
		ref_txtSTAIb = txtSTAIb;
		ref_txtSTATestTime = txtSTATestTime;
		ref_txtSTATestPulseNo = txtSTATestPulseNo;
		ref_cmbBoxVolt = cmbBoxVolt;

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
		ApplicationLauncher.logger.debug("PropertySTAController:  applyUacSettings: Entry");
		if(!ProjectController.isChildPropertySaveEnabled()){
			btn_Save.setDisable(true);
		}

	}

	public static  void PropertyDisplayUpdate(String testType, String aliasId, DraggableTestNode SelectedNode){

		mCurrentNode = SelectedNode;
		ref_txtTestType.setText(testType);
		ref_txtAlias_ID.setText(aliasId);

		ArrayList<String> volt_percent = new ArrayList<String>();

		volt_percent.add("100");
		volt_percent.add("125");
		volt_percent.add("150");


		ref_cmbBoxVolt.getItems().clear();
		ref_cmbBoxVolt.getItems().addAll(volt_percent);
		ref_cmbBoxVolt.getSelectionModel().select(0);
		if(mCurrentNode.IsNewNode() != true){
			String project_name = mCurrentNode.getProjectName();
			JSONObject testdetailslist = MySQL_Controller.sp_getproject_components(project_name, testType, aliasId);

			try {
				JSONArray testcase = testdetailslist.getJSONArray("test_details");
				JSONObject test = testcase.getJSONObject(0);
				ref_txtSTAIb.setText(test.getString("sta_ib"));
				ref_txtSTATestTime.setText(test.getString("time_duration"));
				ref_txtSTATestPulseNo.setText(test.getString("sta_test_pulse_no"));
				ref_cmbBoxVolt.setValue(test.getString("voltage"));;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("PropertySTAController: PropertyDisplayUpdate: JSONException:"+e.getMessage());
			}
		}
	}


	/*public void InformUser(String title, String info,AlertType Alert_type){
		TextBoxDialog TextBoxDialogobj = new TextBoxDialog();
		TextBoxDialogobj.TriggerUserInfoPlatFormLater(title, info, Alert_type);
	}*/

	public void SaveSTADataTrigger(){

		ApplicationLauncher.logger.info("SaveSTADataTrigger Invoked:");

		SaveDataTimer = new Timer();
		SaveDataTimer.schedule(new SaveStdDataTask(),100);

	}



	class SaveStdDataTask extends TimerTask {
		public void run() {
			ApplicationLauncher.setCursor(Cursor.WAIT);

			mCurrentNode.NewNode(false);
			String currentProject = mCurrentNode.getProjectName();
			String currentTestType = mCurrentNode.getType().toString();
			String currentAliasId = mCurrentNode.getAliasId();
			MySQL_Controller.sp_delete_project_components(currentProject, currentTestType, currentAliasId);
			MySQL_Controller.sp_delete_summary_data(currentProject, currentTestType, currentAliasId);

			ProjectController.refreshSummaryPaneData("", currentAliasId);

			String project_name = mCurrentNode.getProjectName();
			String test_type = mCurrentNode.getType().toString();
			ApplicationLauncher.logger.info("Node Title: " + txtTestType.getText());
			ApplicationLauncher.logger.info("Node AliasID: " + txtAlias_ID.getText());
			String testcasename = txtTestType.getText();
			String aliasID = txtAlias_ID.getText();
			String positionID = Integer.toString(mCurrentNode.getPositionId());
			String Ib = txtSTAIb.getText();
			String TestTime = txtSTATestTime.getText();
			String TestPulseNo = txtSTATestPulseNo.getText();
			String average  = String .valueOf(ConstantApp.PULSE_AVERAGE_MIN_VALUE);
			mCurrentNode.setSTAIb(Ib);
			mCurrentNode.setSTATestTime(TestTime);
			mCurrentNode.setSTATestPulseNo(TestPulseNo);
			String volt = cmbBoxVolt.getValue();
			String alias_name = mCurrentNode.getAliasName();
			String volt_display_name = GuiUtils.FormatUnForDisplay(volt);
			float Ib_percent = Float.parseFloat(Ib)/100;
			ApplicationLauncher.logger.info ("SaveStdDataTask: Ib_percent: "+ Ib_percent);
			//String current_display_name = Float.toString(Ib_percent) +"Ib";
			DecimalFormat decimalFormat = new DecimalFormat(ConstantApp.STA_IB_DB_SAVING_FORMAT);//"0.#####");
			//S//tring result = decimalFormat.format(Double.valueOf(s));
			String current_display_name = decimalFormat.format(Ib_percent) +"Ib";
			/*			if(current_display_name.length()==1){
				current_display_name = current_display_name+".0";
			}*/
			//current_display_name = current_display_name+"Ib";
			ApplicationLauncher.logger.info ("SaveStdDataTask: current_display_name: "+ current_display_name);
			String summary_display_tp_name = alias_name + "_" +aliasID+ "-" + volt_display_name + "-" + current_display_name;
			if(GuiUtils.is_float(Ib)){
				if(Float.parseFloat(Ib) != 0.0f){
					if(GuiUtils.is_number(TestPulseNo)){
						if(GuiUtils.is_number(TestTime)){
							if(GuiUtils.is_number(volt)){
								Boolean validation_status = GuiUtils.ValidateVoltagePercentageInVoltageValue(project_name, volt);
								if (validation_status){
									ProjectController.removeTestPoint(mCurrentNode.getType().toString(), aliasID);
									MySQL_Controller.sp_add_project_components(project_name,summary_display_tp_name,test_type, aliasID, positionID, 
											TestTime , "","",Ib ,  TestPulseNo,
											"","","","","","","","","","",volt,
											"","","","","",
											"","","","","",
											"","","",average);
									ProjectController.SaveProject();
									ProjectController.RefreshSummaryDataFromDB();
									ProjectController.UpdateNewTestPointSummaryDataToDB(project_name, summary_display_tp_name, test_type, aliasID);
									ProjectController.LoadSummaryDataToGUI();
									ApplicationLauncher.logger.info ("PropertySTAController: SaveSTAData: Test point saved successfully");
									ApplicationLauncher.InformUser("Saved", "Test point saved successfully",AlertType.INFORMATION);
								}
								else{
									ApplicationLauncher.logger.info ("PropertySTAController: SaveSTAData: Test point save failed. Test point save failed: Voltage validation failed due to configured acceptable limit");
									ApplicationLauncher.InformUser("Incorrect Value", "Test point save failed: Voltage validation failed due to configured acceptable limit" ,AlertType.ERROR);

								}
							}
							else{
								ApplicationLauncher.logger.info ("PropertySTAController: SaveSTAData: Test point save failed. Test point save failed: Kindly enter valid input for voltage");
								ApplicationLauncher.InformUser("Incorrect Value", "Test point save failed: Kindly enter valid input for voltage" ,AlertType.ERROR);

							}
						}
						else{
							ApplicationLauncher.logger.info ("PropertySTAController: SaveSTAData: Test point save failed. Test point save failed: Kindly enter valid input for time");
							ApplicationLauncher.InformUser("Incorrect Value", "Test point save failed: Kindly enter valid input for time" ,AlertType.ERROR);

						}
					}else{
						ApplicationLauncher.logger.info ("PropertySTAController: SaveSTAData: Test point save failed. Test point save failed: Kindly enter valid input for pulse");
						ApplicationLauncher.InformUser("Incorrect Value", "Test point save failed: Kindly enter valid input for pulse" ,AlertType.ERROR);

					}
				}else{
					
					ApplicationLauncher.logger.info ("PropertySTAController: SaveSTAData: Test point save failed. Test point save failed: Ib percentage should not be zero, Kindly enter valid float input for I-b");
					ApplicationLauncher.InformUser("Incorrect Value", "Test point save failed: Ib percentage should not be zero, Kindly enter valid float input for I-b" ,AlertType.ERROR);

				}
			}else{
				ApplicationLauncher.logger.info ("PropertySTAController: SaveSTAData: Test point save failed. Test point save failed: Kindly enter valid float input for I-b");
				ApplicationLauncher.InformUser("Incorrect Value", "Test point save failed: Kindly enter valid float input for I-b" ,AlertType.ERROR);

			}
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			SaveDataTimer.cancel();
		}
	}

}
