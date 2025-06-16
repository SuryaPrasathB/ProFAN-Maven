package com.tasnetwork.calibration.energymeter.setting;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;

import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import com.tasnetwork.calibration.energymeter.ApplicationHomeController;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantLduCcube;
import com.tasnetwork.calibration.energymeter.constant.ConstantLscsHarmonicsSourceSlave;
import com.tasnetwork.calibration.energymeter.constant.ConstantPowerSourceLscs;
import com.tasnetwork.calibration.energymeter.constant.ConstantPowerSourceMte;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdRadiant;
import com.tasnetwork.calibration.energymeter.constant.Constant_ICT;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.database.MySQL_Controller;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.device.SerialDataManager;
import com.tasnetwork.calibration.energymeter.director.PowerSourceDirector;
import com.tasnetwork.calibration.energymeter.director.RefStdDirector;
import com.tasnetwork.calibration.energymeter.serial.portmanagerV2.SerialPortManagerPwrSrc_V2;
import com.tasnetwork.calibration.energymeter.uac.UacDataModel;

import gnu.io.CommPortIdentifier;
import javafx.application.Application;
import javafx.application.Platform;
//import SerialPort.Communicator;
//import application.Communicator;
//import SerialPort.KeybindingController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class DevicePortSetupController implements Initializable {


	DeviceDataManagerController displayDataObj =  new DeviceDataManagerController();

	@FXML private Label lbl_ICT;
	@FXML private ComboBox<String> cmbBxICT_ModelName;
	@FXML private ComboBox<String> cmbBxICT_PortSelection;
	@FXML private ComboBox<Integer> cmbBxICT_BaudRate;
	@FXML private TextField txtValidateICT_CmdStatus;
	@FXML private Button btnValidateICT_Cmd;

	private static Label ref_lbl_ICT;
	private static ComboBox<String> ref_cmbBxICT_ModelName;
	private static ComboBox<String> ref_cmbBxICT_PortSelection;
	private static ComboBox<Integer> ref_cmbBxICT_BaudRate;
	private static TextField ref_txtValidateICT_CmdStatus;
	private static Button ref_btnValidateICT_Cmd;

	@FXML private Label lbl_Harmonics;
	@FXML private ComboBox<String> cmbBxHarmonics_ModelName;
	@FXML private ComboBox<String> cmbBxHarmonics_PortSelection;
	@FXML private ComboBox<Integer> cmbBxHarmonics_BaudRate;
	@FXML private TextField txtValidateHarmonics_CmdStatus;
	@FXML private Button btnValidateHarmonics_Cmd;

	private static Label ref_lbl_Harmonics;
	private static ComboBox<String> ref_cmbBxHarmonics_ModelName;
	private static ComboBox<String> ref_cmbBxHarmonics_PortSelection;
	private static ComboBox<Integer> ref_cmbBxHarmonics_BaudRate;
	private static TextField ref_txtValidateHarmonics_CmdStatus;
	private static Button ref_btnValidateHarmonics_Cmd;


	@FXML
	private ComboBox<Integer> cmbBxPowerSrcBaudRate;
	@FXML
	private ComboBox<Integer> cmbBxRefStdBaudRate;
	@FXML
	private ComboBox<Integer> cmbBxLDU_BaudRate;
	@FXML
	private ComboBox<String> cmbBxPowerSrcPortSelection;
	@FXML
	private ComboBox<String> cmbBxRefStdPortSelection;
	@FXML
	private ComboBox<String> cmbBxLDU_PortSelection;
	@FXML
	private ComboBox<String> cmbBxPowerSource_ModelName;
	@FXML
	private ComboBox<String> cmbBxReferanceStd_ModelName;
	@FXML
	private ComboBox<String> cmbBxLDU_ModelName;

	@FXML
	private TextField txtValidatePwrSrcCmdStatus;
	@FXML
	private TextField txtValidateRefStdCmdStatus;
	@FXML
	private TextField txtValidateLDU_CmdStatus;

	@FXML
	private Button btnValidatePwrSrcCmd;
	@FXML
	private Button btnValidateRefStdCmd;

	private static Button ref_btnValidatePwrSrcCmd;
	private static Button ref_btnValidateRefStdCmd;



	private static Button ref_btnValidateLDU_Cmd;

	@FXML
	private Button btn_Save;
	private static Button ref_btn_Save;

	@FXML
	private Button btnValidateLDU_Cmd;

	Timer PwrSrcValidateTimer;
	Timer RefStdValidateTimer;
	Timer LDU_ValidateTimer;
	Timer ictValidateTimer;

	private static HashMap FXML_PortMap = new HashMap();

	private static  boolean PortValidationTurnedON = false;

	Timer UI_DisplayTimer = new Timer();
	UI_DisplayTimerTask UI_DisplayTimerTaskObj;

	MyRunnable myRunnable;
	Thread myRunnableThread;

	public SerialDataManager serialDM_Obj = new SerialDataManager();
	DeviceDataManagerController DisplayDataObj =  new DeviceDataManagerController();


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		refAssignment();

		initializeComPorts( );

		UI_DisplayTimerTaskObj = new UI_DisplayTimerTask(txtValidateRefStdCmdStatus);
		myRunnable = new MyRunnable(txtValidateRefStdCmdStatus);
		hideGuiObjects();

		if(ProcalFeatureEnable.USER_ACCESS_CONTROL_ENABLED){
			applyUacSettings();
		}
	}


	public void hideGuiObjects(){
		if(!ProcalFeatureEnable.ICT_INTERFACE_ENABLED){
			ref_lbl_ICT.setVisible(false);
			ref_cmbBxICT_ModelName.setVisible(false);
			ref_cmbBxICT_PortSelection.setVisible(false);
			ref_cmbBxICT_BaudRate.setVisible(false);
			ref_txtValidateICT_CmdStatus.setVisible(false);
			ref_btnValidateICT_Cmd.setVisible(false);
		}

		if(!ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_DSP_SLAVE_SERIAL_CONNECTED){
			ref_lbl_Harmonics.setVisible(false);
			ref_cmbBxHarmonics_ModelName.setVisible(false);
			ref_cmbBxHarmonics_PortSelection.setVisible(false);
			ref_cmbBxHarmonics_BaudRate.setVisible(false);
			ref_txtValidateHarmonics_CmdStatus.setVisible(false);
			ref_btnValidateHarmonics_Cmd.setVisible(false);
		}

		if(ProcalFeatureEnable.CONVEYOR_FEATURE_ENABLED) {
			ref_lbl_Harmonics.setVisible(true);
			ref_cmbBxHarmonics_ModelName.setVisible(true);
			ref_cmbBxHarmonics_PortSelection.setVisible(true);
			ref_cmbBxHarmonics_BaudRate.setVisible(true);
			ref_txtValidateHarmonics_CmdStatus.setVisible(true);
			ref_btnValidateHarmonics_Cmd.setVisible(false);
			ref_lbl_Harmonics.setText("Conveyor");
		}
	}
	public void refAssignment(){

		ref_lbl_ICT = lbl_ICT;
		ref_cmbBxICT_ModelName = cmbBxICT_ModelName;
		ref_cmbBxICT_PortSelection = cmbBxICT_PortSelection;
		ref_cmbBxICT_BaudRate = cmbBxICT_BaudRate;
		ref_txtValidateICT_CmdStatus = txtValidateICT_CmdStatus;
		ref_btnValidateICT_Cmd = btnValidateICT_Cmd;

		ref_lbl_Harmonics = lbl_Harmonics;
		ref_cmbBxHarmonics_ModelName = cmbBxHarmonics_ModelName;
		ref_cmbBxHarmonics_PortSelection = cmbBxHarmonics_PortSelection;
		ref_cmbBxHarmonics_BaudRate = cmbBxHarmonics_BaudRate;
		ref_txtValidateHarmonics_CmdStatus = txtValidateHarmonics_CmdStatus;
		ref_btnValidateHarmonics_Cmd = btnValidateHarmonics_Cmd;

		ref_btn_Save = btn_Save;
		ref_btnValidatePwrSrcCmd = btnValidatePwrSrcCmd;
		ref_btnValidateRefStdCmd = btnValidateRefStdCmd;
		ref_btnValidateLDU_Cmd = btnValidateLDU_Cmd;
	}

	public void initializeComPorts() {
		setupComPortsBaudRate();
		loadAvailableComPorts();

		updatePowerSourceModel();
		updateReferenceMeterModel();
		updateLDUModel();
		updateICT_Model();
		updateHarmonics_Model();
		load_saved_device_settings();


	}

	public void load_saved_device_settings(){

		JSONObject saved_pwr_setting = MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_POWER_SOURCE);
		JSONObject saved_ref_setting = MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_REF_STD);
		JSONObject saved_ldu_setting = MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_LDU);



		//if(true){//Condition TO BE DEFINED
		try {
			if(saved_pwr_setting.has("port_name")){
				cmbBxPowerSrcPortSelection.setValue(saved_pwr_setting.getString("port_name"));
			} else{

				cmbBxPowerSrcPortSelection.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: PowerSrcPortSelection: Data not retrieved from DB");

			}

		} catch (JSONException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("load_saved_device_settings: JSONException1:"+e.getMessage());
			cmbBxPowerSrcPortSelection.setValue("");
			ApplicationLauncher.logger.info("load_saved_device_settings: PowerSrcPortSelection: Data not retrieved from database");
		} 	
		try {
			if(saved_pwr_setting.has("baud_rate")){
				cmbBxPowerSrcBaudRate.setValue(Integer.parseInt(saved_pwr_setting.getString("baud_rate"))); 	
			} else{
				cmbBxPowerSrcBaudRate.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: PowerSrcBaudRate: Data not retrieved from DB");

			}
		} catch (JSONException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("load_saved_device_settings: JSONException2:"+e.getMessage());
			cmbBxPowerSrcBaudRate.setValue(9600);
			ApplicationLauncher.logger.info("load_saved_device_settings: PowerSrcBaudRate: Data not retrieved from database");

		} 
		try {
			if(saved_ref_setting.has("port_name")){
				cmbBxRefStdPortSelection.setValue(saved_ref_setting.getString("port_name")); 	
			} else{
				cmbBxRefStdPortSelection.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: RefStdPortSelection: Data not retrieved from DB");

			}

		} catch (JSONException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("load_saved_device_settings: JSONException3:"+e.getMessage());
			cmbBxRefStdPortSelection.setValue("");
			ApplicationLauncher.logger.info("load_saved_device_settings: RefStdPortSelection: Data not retrieved from database");

		} 
		try {
			if(saved_ref_setting.has("baud_rate")){
				cmbBxRefStdBaudRate.setValue(Integer.parseInt(saved_ref_setting.getString("baud_rate"))); 	
			} else{
				cmbBxRefStdBaudRate.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: RefStdBaudRate: Data not retrieved from DB");

			}

		} catch (JSONException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("load_saved_device_settings: JSONException4:"+e.getMessage());
			cmbBxRefStdBaudRate.setValue(9600);
			ApplicationLauncher.logger.info("load_saved_device_settings: RefStdBaudRate: Data not retrieved from database");

		} 
		try {
			if(saved_ldu_setting.has("port_name")){
				cmbBxLDU_PortSelection.setValue(saved_ldu_setting.getString("port_name")); 	
			} else {
				cmbBxLDU_PortSelection.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: LDU_PortSelection: Data not retrieved from DB");

			}

		} catch (JSONException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5:"+e.getMessage());
			cmbBxLDU_PortSelection.setValue("");
			ApplicationLauncher.logger.info("load_saved_device_settings: LDU_PortSelection: Data not retrieved from database");

		} 
		try {
			if(saved_ldu_setting.has("baud_rate")){
				cmbBxLDU_BaudRate.setValue(Integer.parseInt(saved_ldu_setting.getString("baud_rate"))); 
			} else {
				cmbBxLDU_BaudRate.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: LDU_BaudRate: Data not retrieved from DB");

			}
		} catch (JSONException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6:"+e.getMessage());
			cmbBxLDU_BaudRate.setValue(9600);
			ApplicationLauncher.logger.info("load_saved_device_settings: LDU_BaudRate: Data not retrieved from database");

		} 


		if(ProcalFeatureEnable.ICT_INTERFACE_ENABLED){
			JSONObject saved_ict_setting = MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_ICT);
			try {
				if(saved_ict_setting.has("baud_rate")){
					ref_cmbBxICT_BaudRate.setValue(Integer.parseInt(saved_ict_setting.getString("baud_rate"))); 
				} else {
					ref_cmbBxICT_BaudRate.setValue(9600);
					ApplicationLauncher.logger.info("load_saved_device_settings: ICT_BaudRate: Data not retrieved from DB");

				}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException7:"+e.getMessage());
				ref_cmbBxICT_BaudRate.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: ICT_BaudRate: Data not retrieved from database");

			} 

			try {
				if(saved_ict_setting.has("port_name")){
					ref_cmbBxICT_PortSelection.setValue(saved_ict_setting.getString("port_name")); 	
				} else {
					ref_cmbBxICT_PortSelection.setValue("");
					ApplicationLauncher.logger.info("load_saved_device_settings: ICT_PortSelection: Data not retrieved from DB");

				}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5:"+e.getMessage());
				ref_cmbBxICT_PortSelection.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: ICT_PortSelection: Data not retrieved from database");

			} 

		}

		if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_DSP_SLAVE_SERIAL_CONNECTED){
			JSONObject saved_harmonics_setting = MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_HARMONICS_SRC);
			try {
				if(saved_harmonics_setting.has("baud_rate")){
					ref_cmbBxHarmonics_BaudRate.setValue(Integer.parseInt(saved_harmonics_setting.getString("baud_rate"))); 
				} else {
					ref_cmbBxHarmonics_BaudRate.setValue(9600);
					ApplicationLauncher.logger.info("load_saved_device_settings: Harmonics_BaudRate: Data not retrieved from DB");

				}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException7:"+e.getMessage());
				ref_cmbBxHarmonics_BaudRate.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: Harmonics_BaudRate: Data not retrieved from database");

			} 

			try {
				if(saved_harmonics_setting.has("port_name")){
					ref_cmbBxHarmonics_PortSelection.setValue(saved_harmonics_setting.getString("port_name")); 	
				} else {
					ref_cmbBxHarmonics_PortSelection.setValue("");
					ApplicationLauncher.logger.info("load_saved_device_settings: Harmonics_PortSelection: Data not retrieved from DB");

				}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5:"+e.getMessage());
				ref_cmbBxHarmonics_PortSelection.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: Harmonics_PortSelection: Data not retrieved from database");

			} 

		}

		if(ProcalFeatureEnable.CONVEYOR_FEATURE_ENABLED){
			JSONObject saved_harmonics_setting = MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_HARMONICS_SRC);
			try {
				if(saved_harmonics_setting.has("baud_rate")){
					ref_cmbBxHarmonics_BaudRate.setValue(Integer.parseInt(saved_harmonics_setting.getString("baud_rate"))); 
				} else {
					ref_cmbBxHarmonics_BaudRate.setValue(9600);
					ApplicationLauncher.logger.info("load_saved_device_settings: Converoy_BaudRate: Data not retrieved from DB");

				}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException7:"+e.getMessage());
				ref_cmbBxHarmonics_BaudRate.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: Converoy_BaudRate: Data not retrieved from database");

			} 

			try {
				if(saved_harmonics_setting.has("port_name")){
					ref_cmbBxHarmonics_PortSelection.setValue(saved_harmonics_setting.getString("port_name")); 	
				} else {
					ref_cmbBxHarmonics_PortSelection.setValue("");
					ApplicationLauncher.logger.info("load_saved_device_settings: Converoy_PortSelection: Data not retrieved from DB");

				}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5:"+e.getMessage());
				ref_cmbBxHarmonics_PortSelection.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: Converoy_PortSelection: Data not retrieved from database");

			} 

		}

		//}	

	}

	public static JSONObject get_device_settings(String InputSourceType){

		JSONObject saved_pwr_setting = MySQL_Controller.sp_getdevice_setting(InputSourceType);
		return saved_pwr_setting;
	}


	public void setupComPortsBaudRate() {

		cmbBxPowerSrcBaudRate.getItems().clear();
		cmbBxPowerSrcBaudRate.getItems().addAll(ConstantApp.BaudRateConstant);
		if(ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){
			cmbBxPowerSrcBaudRate.getSelectionModel().select(ConstantPowerSourceMte.PowerSrcDefaultBaudRate);
		}else if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
			cmbBxPowerSrcBaudRate.getSelectionModel().select(ConstantPowerSourceLscs.PowerSrcDefaultBaudRate);
		}
		cmbBxRefStdBaudRate.getItems().clear();
		cmbBxRefStdBaudRate.getItems().addAll(ConstantApp.BaudRateConstant);
		cmbBxRefStdBaudRate.getSelectionModel().select(ConstantRefStdRadiant.RefStdDefaultBaudRate);
		cmbBxLDU_BaudRate.getItems().clear();
		cmbBxLDU_BaudRate.getItems().addAll(ConstantApp.BaudRateConstant);
		cmbBxLDU_BaudRate.getSelectionModel().select(ConstantLduCcube.LDU_DefaultBaudRate);

		if(ProcalFeatureEnable.ICT_INTERFACE_ENABLED){

			ref_cmbBxICT_BaudRate.getItems().clear();
			ref_cmbBxICT_BaudRate.getItems().addAll(ConstantApp.BaudRateConstant);
			ref_cmbBxICT_BaudRate.getSelectionModel().select(Constant_ICT.ICT_DefaultBaudRate);

		}

		if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_DSP_SLAVE_SERIAL_CONNECTED){

			ref_cmbBxHarmonics_BaudRate.getItems().clear();
			ref_cmbBxHarmonics_BaudRate.getItems().addAll(ConstantApp.BaudRateConstant);
			ref_cmbBxHarmonics_BaudRate.getSelectionModel().select(ConstantLscsHarmonicsSourceSlave.harmonicsSourceDefaultBaudRate);

		}

		if(ProcalFeatureEnable.CONVEYOR_FEATURE_ENABLED){

			ref_cmbBxHarmonics_BaudRate.getItems().clear();
			ref_cmbBxHarmonics_BaudRate.getItems().addAll(ConstantApp.BaudRateConstant);
			ref_cmbBxHarmonics_BaudRate.getSelectionModel().select(ConstantLscsHarmonicsSourceSlave.harmonicsSourceDefaultBaudRate);

		}

	}

	public static boolean  getPortValidationTurnedON(){
		return PortValidationTurnedON;
	}

	public static void setPortValidationTurnedON(boolean status){
		PortValidationTurnedON = status;

	}


	public void loadAvailableComPorts() {


		//Stub for testing

		ArrayList<String> ModelList = new ArrayList<String>();
		String ModelName = "";
		ConstantApp MyPropertyObj= new ConstantApp();
		ModelName = ConstantAppConfig.REFSTD;
		ModelList.add("Com1");
		ModelList.add("Com2");
		ModelList.add("Com3");

		scanSerialPortAndUpdateDisplay();//dramesh
	}

	public void updatePowerSourceModel() {
		ArrayList<String> ModelList = new ArrayList<String>();
		String ModelName = "";
		ConstantApp MyPropertyObj= new ConstantApp();
		ModelName = ConstantAppConfig.POWERSRC;
		ModelList.add(ModelName);
		cmbBxPowerSource_ModelName.getItems().clear();
		cmbBxPowerSource_ModelName.getItems().addAll(ModelList);
		cmbBxPowerSource_ModelName.getSelectionModel().select(0);
	}

	public void updateReferenceMeterModel() {
		ArrayList<String> ModelList = new ArrayList<String>();
		String ModelName = "";
		ConstantApp MyPropertyObj= new ConstantApp();
		ModelName = ConstantAppConfig.REFSTD;
		ModelList.add(ModelName);
		cmbBxReferanceStd_ModelName.getItems().clear();
		cmbBxReferanceStd_ModelName.getItems().addAll(ModelList);
		cmbBxReferanceStd_ModelName.getSelectionModel().select(0);
	}

	public void updateLDUModel() {
		ArrayList<String> ModelList = new ArrayList<String>();
		String ModelName = "";
		ConstantApp MyPropertyObj= new ConstantApp();
		ModelName = ConstantAppConfig.LDU;
		ModelList.add(ModelName);
		cmbBxLDU_ModelName.getItems().clear();
		cmbBxLDU_ModelName.getItems().addAll(ModelList);
		cmbBxLDU_ModelName.getSelectionModel().select(0);
	}

	public void updateICT_Model() {
		ArrayList<String> ModelList = new ArrayList<String>();
		String ModelName = "";
		ConstantApp MyPropertyObj= new ConstantApp();
		ModelName = ConstantAppConfig.ICT;
		ModelList.add(ModelName);
		ref_cmbBxICT_ModelName.getItems().clear();
		ref_cmbBxICT_ModelName.getItems().addAll(ModelList);
		ref_cmbBxICT_ModelName.getSelectionModel().select(0);
	}

	public void updateHarmonics_Model() {
		ArrayList<String> ModelList = new ArrayList<String>();
		String ModelName = "";
		ConstantApp MyPropertyObj= new ConstantApp();
		ModelName = ConstantAppConfig.HARMONICS_SRC;
		ModelList.add(ModelName);
		ref_cmbBxHarmonics_ModelName.getItems().clear();
		ref_cmbBxHarmonics_ModelName.getItems().addAll(ModelList);
		ref_cmbBxHarmonics_ModelName.getSelectionModel().select(0);
	}




	public void scanSerialPortAndUpdateDisplay() {

		cmbBxPowerSrcPortSelection.getItems().clear();
		cmbBxRefStdPortSelection.getItems().clear();
		cmbBxLDU_PortSelection.getItems().clear();
		if(ProcalFeatureEnable.ICT_INTERFACE_ENABLED){
			ref_cmbBxICT_PortSelection.getItems().clear();
		}

		if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_DSP_SLAVE_SERIAL_CONNECTED){
			ref_cmbBxHarmonics_PortSelection.getItems().clear();
		}

		Enumeration ports = CommPortIdentifier.getPortIdentifiers();

		while (ports.hasMoreElements()) {
			CommPortIdentifier curPort = (CommPortIdentifier)ports.nextElement();

			if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				cmbBxPowerSrcPortSelection.getItems().add(curPort.getName());
				cmbBxRefStdPortSelection.getItems().add(curPort.getName());
				cmbBxLDU_PortSelection.getItems().add(curPort.getName());
				if(ProcalFeatureEnable.ICT_INTERFACE_ENABLED){
					ref_cmbBxICT_PortSelection.getItems().add(curPort.getName());
				}
				if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_DSP_SLAVE_SERIAL_CONNECTED){
					ref_cmbBxHarmonics_PortSelection.getItems().add(curPort.getName());
				}

				if(ProcalFeatureEnable.CONVEYOR_FEATURE_ENABLED){
					ref_cmbBxHarmonics_PortSelection.getItems().add(curPort.getName());
				}

			}
		}



		try {
			cmbBxPowerSrcPortSelection.getSelectionModel().select(0);
		} catch(Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception1:"+e.getMessage());
		}
		try {
			cmbBxRefStdPortSelection.getSelectionModel().select(0);
		} catch(Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception2:"+e.getMessage());
		}

		try{
			cmbBxLDU_PortSelection.getSelectionModel().select(0);
		} catch(Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3:"+e.getMessage());
		}
		if(ProcalFeatureEnable.ICT_INTERFACE_ENABLED){

			try{
				ref_cmbBxICT_PortSelection.getSelectionModel().select(0);
			} catch(Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception4:"+e.getMessage());
			} 
		}

		if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_DSP_SLAVE_SERIAL_CONNECTED){

			try{
				ref_cmbBxHarmonics_PortSelection.getSelectionModel().select(0);
			} catch(Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception5:"+e.getMessage());
			} 
		}
		
		if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
			
			
			DeviceDataManagerController.scanForPowerSourceSerialCommPortV2();
		}
		
		if(ProcalFeatureEnable.REFSTD_PORT_MANAGER_V2_ENABLED) {
			
			
			DeviceDataManagerController.scanForRefStdSerialCommPortV2();
		}

	}

	public void SaveOnClick(){
		String pwr_type = ConstantApp.SOURCE_TYPE_POWER_SOURCE;
		String pwr_model_name = cmbBxPowerSource_ModelName.getSelectionModel().getSelectedItem();
		String pwr_port_name = cmbBxPowerSrcPortSelection.getSelectionModel().getSelectedItem();
		String pwr_baud_rate = cmbBxPowerSrcBaudRate.getSelectionModel().getSelectedItem().toString();
		String ref_type = ConstantApp.SOURCE_TYPE_REF_STD;
		String ref_model_name = cmbBxReferanceStd_ModelName.getSelectionModel().getSelectedItem();
		String ref_port_name = cmbBxRefStdPortSelection.getSelectionModel().getSelectedItem();
		String ref_baud_rate = cmbBxRefStdBaudRate.getSelectionModel().getSelectedItem().toString();
		String ldu_type = ConstantApp.SOURCE_TYPE_LDU;
		String ldu_model_name = cmbBxLDU_ModelName.getSelectionModel().getSelectedItem();
		String ldu_port_name = cmbBxLDU_PortSelection.getSelectionModel().getSelectedItem();
		String ldu_baud_rate = cmbBxLDU_BaudRate.getSelectionModel().getSelectedItem().toString();

		MySQL_Controller.sp_add_device_settings(1, pwr_type, pwr_model_name, pwr_port_name, pwr_baud_rate);
		MySQL_Controller.sp_add_device_settings(2, ref_type, ref_model_name, ref_port_name, ref_baud_rate);
		MySQL_Controller.sp_add_device_settings(3, ldu_type, ldu_model_name, ldu_port_name, ldu_baud_rate);
		if(ProcalFeatureEnable.ICT_INTERFACE_ENABLED){
			String ict_type = ConstantApp.SOURCE_TYPE_ICT;
			String ict_model_name = ref_cmbBxICT_ModelName.getSelectionModel().getSelectedItem();
			String ict_port_name = ref_cmbBxICT_PortSelection.getSelectionModel().getSelectedItem();
			String ict_baud_rate = ref_cmbBxICT_BaudRate.getSelectionModel().getSelectedItem().toString();
			MySQL_Controller.sp_add_device_settings(4, ict_type, ict_model_name, ict_port_name, ict_baud_rate);

		}

		if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_DSP_SLAVE_SERIAL_CONNECTED){
			String harmonics_type = ConstantApp.SOURCE_TYPE_HARMONICS_SRC;
			String harmonics_model_name = ref_cmbBxHarmonics_ModelName.getSelectionModel().getSelectedItem();
			String harmonics_port_name = ref_cmbBxHarmonics_PortSelection.getSelectionModel().getSelectedItem();
			String harmonics_baud_rate = ref_cmbBxHarmonics_BaudRate.getSelectionModel().getSelectedItem().toString();
			MySQL_Controller.sp_add_device_settings(5, harmonics_type, harmonics_model_name, harmonics_port_name, harmonics_baud_rate);
		}
		if(ProcalFeatureEnable.CONVEYOR_FEATURE_ENABLED){
			String harmonics_type = ConstantApp.SOURCE_TYPE_HARMONICS_SRC;
			String harmonics_model_name = ref_cmbBxHarmonics_ModelName.getSelectionModel().getSelectedItem();
			String harmonics_port_name = ref_cmbBxHarmonics_PortSelection.getSelectionModel().getSelectedItem();
			String harmonics_baud_rate = ref_cmbBxHarmonics_BaudRate.getSelectionModel().getSelectedItem().toString();
			MySQL_Controller.sp_add_device_settings(5, harmonics_type, harmonics_model_name, harmonics_port_name, harmonics_baud_rate);
		}
		ApplicationLauncher.InformUser("Saved Successfully", "Saved data successfully", AlertType.INFORMATION);

	}






	public void PwrSrcValidateSerialCmd(){

		String PowerSrcCommPortID= null;
		String PwrSrcCommBaudRate = null;
		txtValidatePwrSrcCmdStatus.clear();
		try{
			boolean status = false;
			if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
				//scanForSerialPorts();
				//status = displayDataObj.refStdPortAccessible();
				
				//status = displayDataObj.pwrSrcPortAccessible_V2();
				
				PowerSrcCommPortID = getCurrentPwrSrcComPortID();
				PwrSrcCommBaudRate = getCurrentPwrSrcComBaudRate();
				status = displayDataObj.pwrSrcPortAccessible_V2_1(PowerSrcCommPortID,PwrSrcCommBaudRate);

			}else {
				serialDM_Obj.commPowerSrc.searchForPorts(); 
				PowerSrcCommPortID = getCurrentPwrSrcComPortID();
				PwrSrcCommBaudRate = getCurrentPwrSrcComBaudRate();
				status = serialDM_Obj.pwrSrc_CommInit(PowerSrcCommPortID,PwrSrcCommBaudRate);
			}

			if (!status){

				txtValidatePwrSrcCmdStatus.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);

			} else {

				if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
					displayDataObj.pwrSrcEnableSerialMonitoring_V2();
					PowerSourceDirector pwrSrcDirector = new PowerSourceDirector();
					status = pwrSrcDirector.setPowerSourceOff();
				}else {
					if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){

						status = serialDM_Obj.SetPowerSourceOffWithInit();

					}else{
						if(!serialDM_Obj.isPowerSourceTurnedOff()){
							status = serialDM_Obj.SetPowerSourceOff();
						}else{
							status= true;
						}
					}
				}
				if (!status){
					txtValidatePwrSrcCmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
				}else{
					txtValidatePwrSrcCmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
				}

			}
			//ApplicationLauncher.logger.info("PwrSrcValidateSerialCmd: testD:"+serialDM_Obj.commPowerSrc.getPortDeviceMapping());
			if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {

				displayDataObj.pwrSrcDisconnectPort_V2();
			}else {
				serialDM_Obj.DisconnectPwrSrc();
			}

		}catch(Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("PwrSrcValidateSerialCmd: Exception1:"+e.getMessage());
			//ApplicationLauncher.logger.info("PwrSrcValidateSerialCmd: Exception:"+e.toString());
			txtValidatePwrSrcCmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
		}
	}

	public void RefStdValidateSerialCmd(){

		String RefStdCommPortID= null;
		String RefStdCommBaudRate = null;
		txtValidateRefStdCmdStatus.clear();
		try{
			boolean status  = false;
			if(ProcalFeatureEnable.REFSTD_PORT_MANAGER_V2_ENABLED) {
				//scanForSerialPorts();
				//status = displayDataObj.refStdPortAccessible();
				
				//status = displayDataObj.refStdPortAccessible_V2();
				
				RefStdCommPortID = getCurrentRefStdComPortID();
				RefStdCommBaudRate = getCurrentRefStdComBaudRate();
				status = displayDataObj.refStdPortAccessible_V2_1(RefStdCommPortID,RefStdCommBaudRate);

			}else {
				serialDM_Obj.commRefStandard.searchForPorts(); 
				RefStdCommPortID = getCurrentRefStdComPortID();
				RefStdCommBaudRate = getCurrentRefStdComBaudRate();
				status = serialDM_Obj.RefStdComInit(RefStdCommPortID,RefStdCommBaudRate);
			}

			if (!status){

				txtValidateRefStdCmdStatus.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);

			} else {

				if(ProcalFeatureEnable.REFSTD_PORT_MANAGER_V2_ENABLED) {
					//displayDataObj.refStdEnableSerialMonitoring();
					displayDataObj.refStdEnableSerialMonitoring_V2();
				}



				if(ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED){
					status = serialDM_Obj.RefStd_ValidateNOP_CMD();
				}else if(ProcalFeatureEnable.MTE_REFSTD_CONNECTED){
					status = serialDM_Obj.mteRefStd_ValidateVersionCMD();
				}else if(ProcalFeatureEnable.SANDS_REFSTD_CONNECTED){
					status = serialDM_Obj.sandsRefStdGetConfigTask();
				}else if(ProcalFeatureEnable.KRE_REFSTD_CONNECTED){
					if(ProcalFeatureEnable.REFSTD_PORT_MANAGER_V2_ENABLED) {
						/*RefStdKreDirector refStdKreDirector = new RefStdKreDirector();
	    				refStdKreDirector.kreRefStdWriteSettingTask();*/

						RefStdDirector refStdKreDirector = new RefStdDirector();
						status = refStdKreDirector.refStdWriteSettingTask();
					}else {
						status = serialDM_Obj.kreRefStdReadSettingTask();
					}
				}else if(ProcalFeatureEnable.KIGGS_REFSTD_CONNECTED){
					//DisplayDataObj.setRefStdReadDataFlag(true);
					status = serialDM_Obj.kiggsReadRefStdVoltAndCurrentTapRange() ;
					//DisplayDataObj.setRefStdReadDataFlag(false);
				}
				if (!status){
					txtValidateRefStdCmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
				}else{
					txtValidateRefStdCmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
				}

			}
			if(ProcalFeatureEnable.REFSTD_PORT_MANAGER_V2_ENABLED) {
				//ApplicationLauncher.logger.debug("RefStdValidateSerialCmd: Sleeping for 10 Sec");
				//Sleep(10000);
				//displayDataObj.refStdDisconnectPort();
				displayDataObj.refStdDisconnectPort_V2();
			}else {
				serialDM_Obj.DisconnectRefStd();
			}
		}catch(Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("RefStdValidateSerialCmd: Exception"+e.getMessage());
		}


	}

	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("Sleep :InterruptedException:"+ e.getMessage());
		}

	}

	/*    public void scanForSerialPorts(){
		ApplicationLauncher.logger.info("scanForSerialPorts: Entry");
		ApplicationHomeController.update_left_status("Scanning serial ports",ConstantApp.LEFT_STATUS_DEBUG);
		serialDM_Obj.ScanForSerialCommPort(); 
	}*/

	class MyRunnable implements Runnable{


		double count ;
		TextField l_txtValidateRefStdCmdStatus;

		public MyRunnable(TextField ValidateRefStdCmdStatus) {
			count = 0;
			l_txtValidateRefStdCmdStatus= ValidateRefStdCmdStatus;
		}

		@Override
		public void run() {
			for (int i = 0; i <= count; i++) {

				final double update_i = count;


				ApplicationLauncher.logger.info("RefStdValidateSerialCmd:runLater1:test2");
				l_txtValidateRefStdCmdStatus.setText("Sending CMD"+update_i);
				ApplicationLauncher.logger.info("RefStdValidateSerialCmd:runLater1:test3");

				//Update JavaFX UI with runLater() in UI thread
				Platform.runLater(new Runnable(){

					@Override
					public void run() {
						for (int j = 0; j <= update_i; j++) {
							ApplicationLauncher.logger.info("RefStdValidateSerialCmd:runLater2:test2");
							ApplicationLauncher.logger.info("RefStdValidateSerialCmd:runLater2:test3");
							ApplicationLauncher.logger.info("RefStdValidateSerialCmd:runLater2:SleepEntry");
							ApplicationLauncher.logger.info("RefStdValidateSerialCmd:runLater2:Exception");


							ApplicationLauncher.logger.info("RefStdValidateSerialCmd:runLater2:NextForloop");

						}
					}
				});


			}
		}

	}



	class UI_DisplayTimerTask extends TimerTask{


		double count =10;
		TextField l_txtValidateRefStdCmdStatus;

		public UI_DisplayTimerTask(TextField ValidateRefStdCmdStatus) {

			l_txtValidateRefStdCmdStatus= ValidateRefStdCmdStatus;

		}

		@Override
		public void run() {
			for(int i=0;i<count;i++){
				ApplicationLauncher.logger.info("RefStdValidateSerialCmd:test2");
				l_txtValidateRefStdCmdStatus.setText("Sending CMD"+i);
				ApplicationLauncher.logger.info("RefStdValidateSerialCmd:test3");

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					
					e.printStackTrace();
					ApplicationLauncher.logger.error("UI_DisplayTimerTask: InterruptedException: "+e.getMessage());
				}
			}

			UI_DisplayTimer.cancel();


		}

	}


	public void LDU_ValidateSerialCmd(){
		String LDU_CommPortID= null;
		String LDUCommBaudRate = null;
		txtValidateLDU_CmdStatus.clear();
		try{
			serialDM_Obj.commLDU.searchForPorts(); 
			LDU_CommPortID = getCurrentLDU_ComPortID();
			LDUCommBaudRate = getCurrentLDU_ComBaudRate();
			boolean status = serialDM_Obj.LDU_Init(LDU_CommPortID,LDUCommBaudRate);
			if (!status){

				txtValidateLDU_CmdStatus.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);

			} else {
				setPortValidationTurnedON(true);
				DisplayDataObj.set_Error_min("-1.00");
				DisplayDataObj.set_Error_max("+1.00");
				DisplayDataObj.setNoOfPulses("10");

				DisplayDataObj.setLDU_ReadDataFlag(true);
				status = serialDM_Obj.LDU_ResetSetting();
				if (!status){
					txtValidateLDU_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
				}else{
					txtValidateLDU_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
				}
				setPortValidationTurnedON(false);
				DisplayDataObj.setLDU_ReadDataFlag(false);
			}
			serialDM_Obj.DisconnectLDU();
		}catch(Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("LDU_ValidateSerialCmd: Exception"+e.getMessage());
		}


	}

	public void lscsLDU_ValidateSerialCmd(){
		String LDU_CommPortID= null;
		String LDUCommBaudRate = null;
		txtValidateLDU_CmdStatus.clear();
		try{
			serialDM_Obj.commLDU.searchForPorts(); 
			LDU_CommPortID = getCurrentLDU_ComPortID();
			LDUCommBaudRate = getCurrentLDU_ComBaudRate();
			boolean status = serialDM_Obj.LDU_Init(LDU_CommPortID,LDUCommBaudRate);
			if (!status){

				txtValidateLDU_CmdStatus.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);

			} else {
				setPortValidationTurnedON(true);
				/*				DisplayDataObj.set_Error_min("-1.00");
				DisplayDataObj.set_Error_max("+1.00");
				DisplayDataObj.setNoOfPulses("10");

				DisplayDataObj.setLDU_ReadDataFlag(true);*/
				//status = serialDM_Obj.LDU_ResetSetting();
				status = serialDM_Obj.lscsLDU_CheckCom();

				if (!status){
					txtValidateLDU_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
				}else{
					txtValidateLDU_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
				}
				setPortValidationTurnedON(false);
				DisplayDataObj.setLDU_ReadDataFlag(false);
			}
			serialDM_Obj.DisconnectLDU();
		}catch(Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("LDU_ValidateSerialCmd: Exception"+e.getMessage());
		}


	}

	public void ictValidateSerialCmd(){
		String ICT_CommPortID= null;
		String ICTCommBaudRate = null;
		ref_txtValidateICT_CmdStatus.clear();
		try{
			serialDM_Obj.commLDU.searchForPorts(); 
			ICT_CommPortID = getCurrentICT_ComPortID();
			ICTCommBaudRate = getCurrentICT_ComBaudRate();
			boolean status = serialDM_Obj.ictInit(ICT_CommPortID,ICTCommBaudRate);
			if (!status){

				ref_txtValidateICT_CmdStatus.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);

			} else {
				setPortValidationTurnedON(true);
				if(ProcalFeatureEnable.ICT_KRE_KE6323_CONNECTED){
					status = serialDM_Obj.kreICT_CheckCom();
				}

				if (!status){
					ref_txtValidateICT_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
				}else{
					ref_txtValidateICT_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
				}
				setPortValidationTurnedON(false);
				DisplayDataObj.setIctReadData(false);
			}
			serialDM_Obj.DisconnectICT();
		}catch(Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("ictValidateSerialCmd: Exception"+e.getMessage());
		}


	}


	/*    public void harmonicsSrcValidateSerialCmd(){
    	String harmonics_CommPortID= null;
    	String harmonicsCommBaudRate = null;
    	ref_txtValidateHarmonics_CmdStatus.clear();
    	try{
	    	serialDM_Obj.commLDU.searchForPorts(); 
	    	harmonics_CommPortID = getCurrentHarmonicsSrc_ComPortID();
	    	harmonicsCommBaudRate = getCurrentHarmonicsSrc_ComBaudRate();
	    	boolean status = serialDM_Obj.harmonicsSrcInit(harmonics_CommPortID,harmonicsCommBaudRate);

	    	if (!status){
	    		ref_txtValidateHarmonics_CmdStatus.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);

	    	} else {
	    		setPortValidationTurnedON(true);
	    		//if(ProcalFeatureEnable.ICT_KRE_KE6323_CONNECTED){
	    		//	status = serialDM_Obj.kreICT_CheckCom();
	    		//}

	    		if (!status){
	    			ref_txtValidateHarmonics_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			ref_txtValidateHarmonics_CmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setHarmonicsSrcReadData(false);
	    	}
	    	serialDM_Obj.DisconnectHarmonicsSrc();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("harmonicsValidateSerialCmd: Exception"+e.getMessage());
    	}


    }
	 */

	private String getCurrentPwrSrcComBaudRate() {
		// TODO Auto-generated method stub
		return cmbBxPowerSrcBaudRate.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentPwrSrcComPortID() {
		// TODO Auto-generated method stub

		return cmbBxPowerSrcPortSelection.getSelectionModel().getSelectedItem();
	}

	private String getCurrentRefStdComBaudRate() {
		// TODO Auto-generated method stub
		return cmbBxRefStdBaudRate.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentRefStdComPortID() {
		// TODO Auto-generated method stub

		return cmbBxRefStdPortSelection.getSelectionModel().getSelectedItem();
	}

	private String getCurrentLDU_ComBaudRate() {
		// TODO Auto-generated method stub
		return cmbBxLDU_BaudRate.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentICT_ComBaudRate() {
		// TODO Auto-generated method stub
		return ref_cmbBxICT_BaudRate.getSelectionModel().getSelectedItem().toString();
	}
	private String getCurrentLDU_ComPortID() {
		// TODO Auto-generated method stub

		return cmbBxLDU_PortSelection.getSelectionModel().getSelectedItem();
	}

	private String getCurrentICT_ComPortID() {
		// TODO Auto-generated method stub

		return ref_cmbBxICT_PortSelection.getSelectionModel().getSelectedItem();
	}	

	private String getCurrentHarmonics_ComPortID() {
		// TODO Auto-generated method stub

		return ref_cmbBxHarmonics_PortSelection.getSelectionModel().getSelectedItem();
	}	

	public void  PwrSrcValidateSerialCmdTrigger(){
		ApplicationLauncher.logger.info("PwrSrcValidateSerialCmdTrigger: Invoked:");
		PwrSrcValidateTimer = new Timer();
		PwrSrcValidateTimer.schedule(new PwrSrcValidateTimerTask(),100);// 1000);

	}

	public void  RefStdValidateSerialCmdTrigger(){
		ApplicationLauncher.logger.info("RefStdValidateSerialCmdTrigger: Invoked:");
		RefStdValidateTimer = new Timer();
		RefStdValidateTimer.schedule(new RefStdValidateTimerTask(),100);// 1000);

	}

	public void  LDU_ValidateSerialCmdTrigger(){
		ApplicationLauncher.logger.info("LDU_ValidateSerialCmdTrigger: Invoked:");
		LDU_ValidateTimer = new Timer();
		LDU_ValidateTimer.schedule(new LDU_ValidateTimerTask(),100);// 1000);

	}


	class PwrSrcValidateTimerTask extends TimerTask {
		public void run() {
			btnValidatePwrSrcCmd.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("PwrSrcValidateTimerTask: WAIT");
			try {
				PwrSrcValidateSerialCmd();
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("PwrSrcValidateTimerTask: Exception:"+e.getMessage());
			}
			PwrSrcValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("PwrSrcValidateTimerTask: DEFAULT");
			btnValidatePwrSrcCmd.setDisable(false);
		}
	}

	class RefStdValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateRefStdCmd.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("RefStdValidateTimerTask: WAIT");
			try {

				RefStdValidateSerialCmd();
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("RefStdValidateTimerTask: Exception:"+e.getMessage());
			}
			RefStdValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("RefStdValidateTimerTask: DEFAULT");
			btnValidateRefStdCmd.setDisable(false);
		}
	}

	class LDU_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateLDU_Cmd.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("LDU_ValidateTimerTask: WAIT");
			try {
				if(ProcalFeatureEnable.CCUBE_LDU_CONNECTED){
					LDU_ValidateSerialCmd();
				} else if (ProcalFeatureEnable.LSCS_LDU_CONNECTED){
					lscsLDU_ValidateSerialCmd();
				}
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("LDU_ValidateTimerTask: Exception:"+e.getMessage());
			}
			LDU_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("LDU_ValidateTimerTask: DEFAULT");
			btnValidateLDU_Cmd.setDisable(false);
		}
	}

	@FXML
	public void  ICT_ValidateSerialCmdTrigger(){
		ApplicationLauncher.logger.info("ICT_ValidateSerialCmdTrigger: Invoked:");
		ictValidateTimer = new Timer();
		ictValidateTimer.schedule(new ICT_ValidateTimerTask(),100);// 1000);

	}

	class ICT_ValidateTimerTask extends TimerTask {
		public void run() {
			ref_btnValidateICT_Cmd.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("ICT_ValidateTimerTask: WAIT");
			try {
				ictValidateSerialCmd();
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("ICT_ValidateTimerTask: Exception:"+e.getMessage());
			}
			ictValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("ICT_ValidateTimerTask: DEFAULT");
			ref_btnValidateICT_Cmd.setDisable(false);
		}
	}

	private static void applyUacSettings() {
		// TODO Auto-generated method stub
		ApplicationLauncher.logger.info("DevicePortSetupController : applyUacSettings :  Entry");
		ArrayList<UacDataModel> uacSelectProfileScreenList = DeviceDataManagerController.getUacSelectProfileScreenList();
		String screenName = "";
		for (int i = 0; i < uacSelectProfileScreenList.size(); i++){

			screenName = uacSelectProfileScreenList.get(i).getScreenName();
			switch (screenName) {
			case ConstantApp.UAC_DEVICE_SETTINGS_SCREEN:


				if(!uacSelectProfileScreenList.get(i).getExecutePossible()){
					//ref_btn_deploy.setDisable(true);
					ref_btnValidatePwrSrcCmd.setDisable(true);
					ref_btnValidateRefStdCmd.setDisable(true);
					ref_btnValidateLDU_Cmd.setDisable(true);
					/*							ref_btnValidateLDU_Cmd1.setDisable(true);
							ref_btnValidateLDU_Cmd2.setDisable(true);
							ref_btnValidateLDU_Cmd3.setDisable(true);
							ref_btnValidateLDU_Cmd4.setDisable(true);
							ref_btnValidateLDU_Cmd5.setDisable(true);
							ref_btnValidateLDU_Cmd6.setDisable(true);
							ref_btnValidateLDU_Cmd7.setDisable(true);
							ref_btnValidateLDU_Cmd8.setDisable(true);
							ref_btnValidateLDU_Cmd9.setDisable(true);
							ref_btnValidateLDU_Cmd10.setDisable(true);
							ref_btnValidateLDU_Cmd11.setDisable(true);
							ref_btnValidateLDU_Cmd12.setDisable(true);
							ref_btnValidateLDU_Cmd13.setDisable(true);
							ref_btnValidateLDU_Cmd14.setDisable(true);
							ref_btnValidateLDU_Cmd15.setDisable(true);
							ref_btnValidateLDU_Cmd16.setDisable(true);
							ref_btnValidateLDU_Cmd17.setDisable(true);
							ref_btnValidateLDU_Cmd18.setDisable(true);
							ref_btnValidateLDU_Cmd19.setDisable(true);
							ref_btnValidateLDU_Cmd20.setDisable(true);
							ref_btnValidateLDU_Cmd21.setDisable(true);
							ref_btnValidateLDU_Cmd22.setDisable(true);
							ref_btnValidateLDU_Cmd23.setDisable(true);
							ref_btnValidateLDU_Cmd24.setDisable(true);
							ref_btnValidateLDU_Cmd25.setDisable(true);
							ref_btnValidateLDU_Cmd26.setDisable(true);
							ref_btnValidateLDU_Cmd27.setDisable(true);
							ref_btnValidateLDU_Cmd28.setDisable(true);
							ref_btnValidateLDU_Cmd29.setDisable(true);
							ref_btnValidateLDU_Cmd30.setDisable(true);
							ref_btnValidateLDU_Cmd31.setDisable(true);
							ref_btnValidateLDU_Cmd32.setDisable(true);
							ref_btnValidateLDU_Cmd33.setDisable(true);
							ref_btnValidateLDU_Cmd34.setDisable(true);
							ref_btnValidateLDU_Cmd35.setDisable(true);
							ref_btnValidateLDU_Cmd36.setDisable(true);
							ref_btnValidateLDU_Cmd37.setDisable(true);
							ref_btnValidateLDU_Cmd38.setDisable(true);
							ref_btnValidateLDU_Cmd39.setDisable(true);
							ref_btnValidateLDU_Cmd40.setDisable(true);
							ref_btnValidateLDU_Cmd41.setDisable(true);
							ref_btnValidateLDU_Cmd42.setDisable(true);
							ref_btnValidateLDU_Cmd43.setDisable(true);
							ref_btnValidateLDU_Cmd44.setDisable(true);
							ref_btnValidateLDU_Cmd45.setDisable(true);
							ref_btnValidateLDU_Cmd46.setDisable(true);
							ref_btnValidateLDU_Cmd47.setDisable(true);
							ref_btnValidateLDU_Cmd48.setDisable(true);*/

				}

				if(!uacSelectProfileScreenList.get(i).getAddPossible()){
					//ref_btn_Create.setDisable(true);

				}

				if(!uacSelectProfileScreenList.get(i).getUpdatePossible()){
					//ref_vbox_testscript.setDisable(true);sdvsc
					//setChildPropertySaveEnabled(false);
					ref_btn_Save.setDisable(true);


				}

				if(!uacSelectProfileScreenList.get(i).getDeletePossible()){
					//ref_btn_Delete.setDisable(true);

				}
				break;



			default:
				break;
			}



		}
	}




}
