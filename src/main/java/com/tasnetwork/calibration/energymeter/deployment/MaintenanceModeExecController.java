package com.tasnetwork.calibration.energymeter.deployment;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import com.tasnetwork.calibration.energymeter.ApplicationHomeController;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantPowerSourceLscs;
import com.tasnetwork.calibration.energymeter.constant.DeleteMeConstant;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.serial.portmanager.SerialPortManagerPwrSrc;
import com.tasnetwork.calibration.energymeter.util.ErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MaintenanceModeExecController implements Initializable {

	public static boolean serialDisplayProcess = false;
	Timer HV_StartTaskTimer;
	Timer sendCommandTimer;
	Timer VoltageInjectionStopTaskTimer;
	
	//public static SerialDataManager SerialDM_Obj = new SerialDataManager();
	public static SerialPortManagerPwrSrc SerialDM_Obj = new SerialPortManagerPwrSrc();
	static DeviceDataManagerController DisplayDataObj =  new DeviceDataManagerController();
	
	private final boolean PROPOWER_V1 = true;
	
	private final String PROPOWER_V1_BOOT_INIT = "Bootup Init";
	private final String PROPOWER_V1_PRE_START = "Pre-Start";
	private final String PROPOWER_V1_APP_VB_MODE = "App/VB mode";
	private final String PROPOWER_V1_HYPER_TERMINAL_MODE = "HyperTerminal Mode";
	private final String PROPOWER_V1_DAC_MODE = "DAC Mode";
	private final String PROPOWER_V1_STOP = "Stop";
	private final String PROPOWER_V1_MANIPULATE = "Manipulate";
	

	
	
	
	
	
	
	
	
	
	public  ArrayList<String> PROPOWER_V1_MESSAGE_LIST = new ArrayList<String>(Arrays.asList(PROPOWER_V1_BOOT_INIT, 
			 PROPOWER_V1_APP_VB_MODE,PROPOWER_V1_HYPER_TERMINAL_MODE,PROPOWER_V1_DAC_MODE,PROPOWER_V1_PRE_START,PROPOWER_V1_MANIPULATE,PROPOWER_V1_STOP ));
	
	
	public  ArrayList<String> PROPOWER_V1_CURRENT_RELAY_ID_LIST = new ArrayList<String>(Arrays.asList(	"210",
			"211","212","213","214","215"));
	
	public  ArrayList<String> PROPOWER_V1_CURRENT_RELAY_ID_VALUE_LIST = new ArrayList<String>(Arrays.asList(	"0.05A",
			"0.1A","1A","10A","60A","120A"));
	
	

	
	@FXML 
	private RadioButton	rdBtnSinglePhase;
	public static  RadioButton	ref_rdBtnSinglePhase;
	
	@FXML 
	private RadioButton	rdBtnThreePhase;
	public static  RadioButton	ref_rdBtnThreePhase;
	
	
	@FXML 
	private CheckBox	ckBxRphaseVoltage;
	public static  CheckBox	ref_ckBxRphaseVoltage;
	
	@FXML 
	private CheckBox	ckBxYphaseVoltage;
	public static  CheckBox	ref_ckBxYphaseVoltage;
	
	@FXML 
	private CheckBox	ckBxBphaseVoltage;
	public static  CheckBox	ref_ckBxBphaseVoltage;
	
	
	
	
	@FXML 
	private CheckBox	ckBxRphaseCurrent;
	public static  CheckBox	ref_ckBxRphaseCurrent;
	
	@FXML 
	private CheckBox	ckBxYphaseCurrent;
	public static  CheckBox	ref_ckBxYphaseCurrent;
	
	@FXML 
	private CheckBox	ckBxBphaseCurrent;
	public static  CheckBox	ref_ckBxBphaseCurrent;
	
	
	
	@FXML 
	private CheckBox	ckBxRphasePhaseAngle;
	public static  CheckBox	ref_ckBxRphasePhaseAngle;
	
	@FXML 
	private CheckBox	ckBxYphasePhaseAngle;
	public static  CheckBox	ref_ckBxYphasePhaseAngle;
	
	@FXML 
	private CheckBox	ckBxBphasePhaseAngle;
	public static  CheckBox	ref_ckBxBphasePhaseAngle;
	
	
	
	@FXML 
	private CheckBox	ckBxRphaseCurrentRelayId;
	public static  CheckBox	ref_ckBxRphaseCurrentRelayId;
	
	@FXML 
	private CheckBox	ckBxRphaseVoltageRelayId;
	public static  CheckBox	ref_ckBxRphaseVoltageRelayId;
	
	@FXML 
	private CheckBox	ckBxRphaseFrequency;
	public static  CheckBox	ref_ckBxRphaseFrequency;
	
	
	@FXML 
	private TextField	txtRphaseFrequency;
	public static  TextField	ref_txtRphaseFrequency;
	
	
	@FXML 
	private TextField	txtRphaseCurrentRelayIdValue;
	public static  TextField	ref_txtRphaseCurrentRelayIdValue;	

	
	@FXML 
	private TextField	txtRphaseVoltage;
	public static  TextField	ref_txtRphaseVoltage;
	
	@FXML 
	private TextField	txtRphaseCurrent;
	public static  TextField	ref_txtRphaseCurrent;
	
	@FXML 
	private TextField	txtRphasePhaseAngle;
	public static  TextField	ref_txtRphasePhaseAngle;
	
	@FXML 
	private TextField	txtYphaseVoltage;
	public static  TextField	ref_txtYphaseVoltage;
	
	@FXML 
	private TextField	txtYphaseCurrent;
	public static  TextField	ref_txtYphaseCurrent;
	
	@FXML 
	private TextField	txtYphasePhaseAngle;
	public static  TextField	ref_txtYphasePhaseAngle;
	
	@FXML 
	private TextField	txtBphaseVoltage;
	public static  TextField	ref_txtBphaseVoltage;
	
	@FXML 
	private TextField	txtBphaseCurrent;
	public static  TextField	ref_txtBphaseCurrent;
	
	@FXML 
	private TextField	txtBphasePhaseAngle;
	public static  TextField	ref_txtBphasePhaseAngle;

	@FXML 
	private TextField	txInitPasswordStr;
	public static  TextField	ref_txInitPasswordStr;
	
	
	@FXML 
	private ComboBox<String>	cmBxCurrentRelayId;
	public static  ComboBox<String>	ref_cmBxCurrentRelayId;
	
	@FXML 
	private CheckBox	chBxPhaseReversal;
	public static  CheckBox	ref_chBxPhaseReversal;
	
	@FXML 
	private ComboBox<String>	cmbBxMessageType;
	public static  ComboBox<String>	ref_cmbBxMessageType;
	
	@FXML 
	private ComboBox<String>	cmBxPowerSourceType;
	public static  ComboBox<String>	ref_cmBxPowerSourceType;
	
	@FXML 
	private CheckBox	chkBoxRphaseSelectAll;
	public static  CheckBox	ref_chkBoxRphaseSelectAll;
	
	@FXML 
	private CheckBox	chkBoxYphaseSelectAll;
	public static  CheckBox	ref_chkBoxYphaseSelectAll;
	
	@FXML 
	private CheckBox	chkBoxBphaseSelectAll;
	public static  CheckBox	ref_chkBoxBphaseSelectAll;
	
	
	
	
	@FXML 
	private TextArea	txtAreaSerialData;
	public static  TextArea	ref_txtAreaSerialData;
	
	@FXML 
	private TextArea	txtAreaRxSerialData;
	public static  TextArea	ref_txtAreaRxSerialData;
	
	@FXML 
	private TextArea	txtAreaRxSerialDataInHex;
	public static  TextArea	ref_txtAreaRxSerialDataInHex;
	
	
	@FXML 
	private CheckBox	chBxResponseExpectedForCmd;
	public static  CheckBox	ref_chBxResponseExpectedForCmd;
	
	
	@FXML 
	private TextArea	txtAreaTargetCommand;
	public static  TextArea	ref_txtAreaTargetCommand;
	
	@FXML 
	private TextField	txtSerialTxDelay;
	public static  TextField	ref_txtSerialTxDelay;
	
	
	
	@FXML 
	private TextField	txtRxCmdTerminatorInHex;
	public static  TextField	ref_txtRxCmdTerminatorInHex;
	
	@FXML 
	private TextField	txtTxCmdTerminatorInHex;
	public static  TextField	ref_txtTxCmdTerminatorInHex;
			
	@FXML 
	private TextField	txtTxCmdSeperatorInHex;
	public static  TextField	ref_txtTxCmdSeperatorInHex;
	
	@FXML 
	private TextField	txtRxCmdTerminatorInStr;
	public static  TextField	ref_txtRxCmdTerminatorInStr;
	
	@FXML 
	private TextField	txtTxCmdTerminatorInStr;
	public static  TextField	ref_txtTxCmdTerminatorInStr;
			
	@FXML 
	private TextField	txtTxCmdSeperatorInStr;
	public static  TextField	ref_txtTxCmdSeperatorInStr;
	
	
	

	
	@FXML 
	private Button	btnConnect;
	public static  Button	ref_btnConnect;

	@FXML 
	private Button	btnDisconnect;
	public static  Button	ref_btnDisconnect;
	
	@FXML 
	private Button	btnSendCommand;
	public static  Button	ref_btnSendCommand;
	
	@FXML 
	private CheckBox	chkBoxIncludeDelay;
	public static  CheckBox	ref_chkBoxIncludeDelay;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		refAssignment();		
		disableBtnSendCommand();
		disableBtnDisconnect();
		setSerialDisplayProcess(true);
		loadSourceType();
		if(PROPOWER_V1){
			proPowerProtocolV1_Init();
			loadProPowerV1_MessageType();
			loadProPowerV1_CurrentRelayId();
		}
		//loadMessageType();
		
	}
	
	
	




	private void loadProPowerV1_CurrentRelayId() {
		
		//
		
		ref_cmBxCurrentRelayId.getItems().addAll(PROPOWER_V1_CURRENT_RELAY_ID_LIST);
		
		Platform.runLater(() -> {
			ref_cmBxCurrentRelayId.getSelectionModel().select(3);
			ref_txtRphaseCurrentRelayIdValue.setText(PROPOWER_V1_CURRENT_RELAY_ID_VALUE_LIST.get(3));
			
		});
	}







	private void loadProPowerV1_MessageType() {
		
		ref_cmbBxMessageType.getItems().addAll(PROPOWER_V1_MESSAGE_LIST);
		Platform.runLater(() -> {
			ref_cmbBxMessageType.getSelectionModel().select(4);
			
		});
	}



	private void loadSourceType() {
		
		ref_cmBxPowerSourceType.getItems().add("ProPower-V1");
		Platform.runLater(() -> {
			ref_cmBxPowerSourceType.getSelectionModel().select(0);
			
		});
	}



	private void proPowerProtocolV1_Init() {
		
		maintenanceLscsPowerSourceCmdGuiInit();
	}



	private void maintenanceLscsPowerSourceCmdGuiInit() {
		
		Platform.runLater(() -> {
			ref_chkBoxIncludeDelay.setSelected(true);
			ref_chkBoxRphaseSelectAll.setSelected(true);
			ref_ckBxRphaseVoltage.setSelected(true);
			ref_ckBxRphaseCurrent.setSelected(true);
			ref_ckBxRphasePhaseAngle.setSelected(true);
			ref_ckBxRphaseCurrentRelayId.setSelected(true);
			ref_ckBxRphaseFrequency.setSelected(true);
			ref_rdBtnSinglePhase.setSelected(true);
			ref_rdBtnThreePhase.setSelected(false);
			
			ref_chkBoxYphaseSelectAll.setDisable(true);
			ref_chkBoxBphaseSelectAll.setDisable(true);
			
			ref_ckBxYphaseVoltage.setDisable(true);
			ref_ckBxYphaseCurrent.setDisable(true);
			ref_ckBxYphasePhaseAngle.setDisable(true);
			
			ref_ckBxBphaseVoltage.setDisable(true);
			ref_ckBxBphaseCurrent.setDisable(true);
			ref_ckBxBphasePhaseAngle.setDisable(true);
			ref_txInitPasswordStr.setText(ConstantAppConfig.LSCS_STATIC_POWER_SOURCE_INIT);
			ref_chBxResponseExpectedForCmd.setSelected(false);

			
		});
		convertSeperator();
		convertTxTerminator();
		convertRxTerminator();
		
	}

	private void refAssignment() {
		
		ref_txtAreaSerialData = txtAreaSerialData;
		ref_txtAreaRxSerialData = txtAreaRxSerialData;
		ref_txtAreaRxSerialDataInHex = txtAreaRxSerialDataInHex;
		ref_btnConnect = btnConnect;
		ref_btnDisconnect = btnDisconnect;
		ref_btnSendCommand = btnSendCommand;
		ref_txtAreaTargetCommand = txtAreaTargetCommand;
		ref_txtSerialTxDelay= txtSerialTxDelay;
		ref_chkBoxIncludeDelay = chkBoxIncludeDelay;
		
		ref_txtTxCmdSeperatorInStr = txtTxCmdSeperatorInStr;
		ref_txtTxCmdTerminatorInStr = txtTxCmdTerminatorInStr;
		ref_txtRxCmdTerminatorInStr = txtRxCmdTerminatorInStr;
		
		
		ref_txtRxCmdTerminatorInHex = txtRxCmdTerminatorInHex;
		ref_txtTxCmdTerminatorInHex = txtTxCmdTerminatorInHex;
		ref_txtTxCmdSeperatorInHex = txtTxCmdSeperatorInHex;
		
		ref_cmBxPowerSourceType = cmBxPowerSourceType;
		
		ref_cmBxCurrentRelayId = cmBxCurrentRelayId;
		ref_chBxPhaseReversal = chBxPhaseReversal;
		ref_cmbBxMessageType = cmbBxMessageType;
		
		ref_txtRphaseVoltage = txtRphaseVoltage;
		ref_txtRphaseCurrent = txtRphaseCurrent;
		ref_txtRphasePhaseAngle= txtRphasePhaseAngle;
		
		ref_txtYphaseVoltage = txtYphaseVoltage;
		ref_txtYphaseCurrent = txtYphaseCurrent;
		ref_txtYphasePhaseAngle= txtYphasePhaseAngle;
		
		ref_txtBphaseVoltage = txtBphaseVoltage;
		ref_txtBphaseCurrent = txtBphaseCurrent;
		ref_txtBphasePhaseAngle= txtBphasePhaseAngle;
		ref_txInitPasswordStr = txInitPasswordStr;
		
		ref_ckBxRphaseVoltage = ckBxRphaseVoltage;
		ref_ckBxYphaseVoltage = ckBxYphaseVoltage;
		ref_ckBxBphaseVoltage = ckBxBphaseVoltage;
		
		ref_ckBxRphaseCurrent = ckBxRphaseCurrent;
		ref_ckBxYphaseCurrent = ckBxYphaseCurrent;
		ref_ckBxBphaseCurrent = ckBxBphaseCurrent;
		
		ref_ckBxRphasePhaseAngle = ckBxRphasePhaseAngle;
		ref_ckBxYphasePhaseAngle = ckBxYphasePhaseAngle;
		ref_ckBxBphasePhaseAngle = ckBxBphasePhaseAngle;

		ref_ckBxRphaseCurrentRelayId = ckBxRphaseCurrentRelayId;
		ref_ckBxRphaseVoltageRelayId = ckBxRphaseVoltageRelayId;
		ref_ckBxRphaseFrequency = ckBxRphaseFrequency;
		
		ref_txtRphaseFrequency = txtRphaseFrequency;
		
		ref_txtRphaseCurrentRelayIdValue = txtRphaseCurrentRelayIdValue;
		
		ref_chkBoxRphaseSelectAll = chkBoxRphaseSelectAll;
		ref_chkBoxYphaseSelectAll = chkBoxYphaseSelectAll;
		ref_chkBoxBphaseSelectAll = chkBoxBphaseSelectAll;
		
		ref_rdBtnSinglePhase = rdBtnSinglePhase;
		ref_rdBtnThreePhase = rdBtnThreePhase;
		ref_chBxResponseExpectedForCmd = chBxResponseExpectedForCmd;

	}

	public void enableBtnSendCommand(){
		Platform.runLater(() -> {
			ref_btnSendCommand.setDisable(false);
		});
	}
	
	public void disableBtnSendCommand(){
		Platform.runLater(() -> {
			ref_btnSendCommand.setDisable(true);
		});
	}
	
	public void enableBtnConnect(){
		Platform.runLater(() -> {
			ref_btnConnect.setDisable(false);
		});
	}
	
	public void disableBtnConnect(){
		Platform.runLater(() -> {
			ref_btnConnect.setDisable(true);
		});
	}
	
	public void enableBtnDisconnect(){
		Platform.runLater(() -> {
			ref_btnDisconnect.setDisable(false);
		});
	}
	
	public void disableBtnDisconnect(){
		Platform.runLater(() -> {
			ref_btnDisconnect.setDisable(true);
		});
	}
	
	public static void serialDataDisplayUpdate(String  Value){
		Platform.runLater(() -> {
			//ref_txtAreaSerialData.setText(Value);
			try {
				if(isSerialDisplayProcess()) {
					ref_txtAreaSerialData.appendText(Value+"\n");
					ref_txtAreaSerialData.selectPositionCaret(ref_txtAreaSerialData.getLength()); 
					ref_txtAreaSerialData.deselect();
				}
			}catch(Exception e) {
				e.printStackTrace();
				ApplicationLauncher.logger.error("serialDataDisplayUpdate: Exception: " + e.getMessage());
			}
		});
		
	}
	
	public static boolean isSerialDisplayProcess() {
		return serialDisplayProcess;
	}

	public static void setSerialDisplayProcess(boolean serialDisplayProcess) {
		if(DeleteMeConstant.serialDisplayFeatureEnabled){
			ApplicationLauncher.logger.debug("setSerialDisplayProcess :"+serialDisplayProcess);
			MaintenanceModeExecController.serialDisplayProcess = serialDisplayProcess;
		}
	}
	
	public static void EnablePossibleClickEventsV2() {
		
/*		ref_btnVoltageStart.setDisable(false);
		ref_radioBtn_R_Phase.setDisable(false);
		ref_radioBtn_Y_Phase.setDisable(false);
		ref_radioBtn_B_Phase.setDisable(false);
		ref_btnCurrentStart.setDisable(false);
		ref_txtPT_RatedVoltagePhToPh.setDisable(false);
		ref_txtPT_RatedVoltage.setDisable(false);
		ref_txtCT_RatedCurrent.setDisable(false);
		ref_txtCurrentBurdenRelay.setDisable(false);
		ref_radioBtnOnlyHV_Piston.setDisable(false);
		ref_radioBtnOnlyHV_Generate.setDisable(false);
		ref_radioBtnOnlyHV_WithPT_Load.setDisable(false);
		ref_radioBtnOnlyCI.setDisable(false);
		ref_radioBtnOnlyCI_WithCT_Load.setDisable(false);
		ref_btnVoltageStop.setDisable(true);
		ref_btnCurrentStop.setDisable(true);
		
		if(ref_radioBtnOnlyHV_Piston.isSelected()){
			EnableOnlyPossibleEventsForHV_PistonV2();
		}else if(ref_radioBtnOnlyHV_Generate.isSelected()){
			EnableOnlyPossibleEventsForHV_GenerationV2();
		}else if(ref_radioBtnOnlyHV_WithPT_Load.isSelected()){
			EnableOnlyPossibleEventsForHV_WithPT_LoadV2();
		}else if(ref_radioBtnOnlyCI.isSelected()){
			EnableOnlyPossibleEventsForCI_V2();
		}else if(ref_radioBtnOnlyCI_WithCT_Load.isSelected()){
			EnableOnlyPossibleEventsForCI_WithCT_LoadV2();
		}*/
	}
	
	public void EnablePossibleClickEvents() {
		
/*		btnVoltageStart.setDisable(false);
		radioBtn_R_Phase.setDisable(false);
		radioBtn_Y_Phase.setDisable(false);
		radioBtn_B_Phase.setDisable(false);
		btnCurrentStart.setDisable(false);
		ref_btnFreeze.setDisable(false);
		ref_txtPT_RatedVoltagePhToPh.setDisable(false);
		ref_txtPT_RatedVoltage.setDisable(false);
		ref_txtCT_RatedCurrent.setDisable(false);
		ref_txtCurrentBurdenRelay.setDisable(false);
		ref_txtCT_RefStdRelay.setDisable(true);
		
		radioBtnOnlyHV_Piston.setDisable(false);
		radioBtnOnlyHV_Generate.setDisable(false);
		radioBtnOnlyHV_WithPT_Load.setDisable(false);
		radioBtnOnlyCI.setDisable(false);
		radioBtnOnlyCI_WithCT_Load.setDisable(false);
		btnVoltageStop.setDisable(true);
		btnCurrentStop.setDisable(true);
		if(radioBtnOnlyHV_Piston.isSelected()){
			EnableOnlyPossibleEventsForHV_Piston();
		}else if(radioBtnOnlyHV_Generate.isSelected()){
			EnableOnlyPossibleEventsForHV_Generation();
		}else if(radioBtnOnlyHV_WithPT_Load.isSelected()){
			EnableOnlyPossibleEventsForHV_WithPT_Load();
		}else if(radioBtnOnlyCI.isSelected()){
			EnableOnlyPossibleEventsForCI();
		}else if(radioBtnOnlyCI_WithCT_Load.isSelected()){
			EnableOnlyPossibleEventsForCI_WithCT_Load();
		}*/
	}
	
	public static void DisablePossibleClickEvents() {
/*		ref_btnVoltageStart.setDisable(true);
		ref_radioBtn_R_Phase.setDisable(true);
		ref_radioBtn_Y_Phase.setDisable(true);
		ref_radioBtn_B_Phase.setDisable(true);
		ref_btnCurrentStart.setDisable(true);
		//ref_btnFreeze.setDisable(true);
		ref_txtPT_RatedVoltagePhToPh.setDisable(true);
		ref_txtPT_RatedVoltage.setDisable(true);
		ref_txtCT_RatedCurrent.setDisable(true);
		ref_txtCurrentBurdenRelay.setDisable(true);
		ref_txtCT_RefStdRelay.setDisable(true);
		ref_radioBtnOnlyHV_Piston.setDisable(true);
		ref_radioBtnOnlyHV_Generate.setDisable(true);
		ref_radioBtnOnlyHV_WithPT_Load.setDisable(true);
		ref_radioBtnOnlyCI.setDisable(true);
		ref_radioBtnOnlyCI_WithCT_Load.setDisable(true);
		ref_btnPistonActivate.setDisable(true);
		ref_btnPistonAllReset.setDisable(true);*/
		
	}

    public void ScanForSerialPorts(){
		ApplicationLauncher.logger.info("ScanForSerialPorts: Entry");
		ApplicationHomeController.update_left_status("Scanning serial ports",ConstantApp.LEFT_STATUS_DEBUG);
		SerialDM_Obj.ScanForSerialCommPort(); 
	}
	
	void HV_StartTask() {

/*		String  voltagePhaseToPhase= ref_txtPT_RatedVoltagePhToPh.getText();
		String VoltageloadStr = ref_txtVoltPercentage.getText();
		String targetVoltageStr = ref_txtPT_TargetVoltage.getText();
		String ratedVoltageStr = Data_HV_Src.getWritePrimaryRatedVoltage();
		if(!voltagePhaseToPhase.isEmpty()){
			if(GUIUtils.is_float(voltagePhaseToPhase)){
				if(GUIUtils.Validate_HV_load(VoltageloadStr)){
					if (! ratedVoltageStr.isEmpty()){

						String SelectedPhase = Data_HV_Src.getWriteSelectedPhase();
						if(GUIUtils.isNumber(ratedVoltageStr)){
							//float VoltageValue = Float.parseFloat(txtSetVoltage.getText());
							int VoltageValue = Integer.parseInt(ratedVoltageStr);
							ApplicationLauncher.logger.info("HV_StartTask: Manual: VoltageValue:" + VoltageValue);
							if((VoltageValue >=ConstantProGEN_App.MIN_VOLTAGE) &&  (VoltageValue <=ConstantProGEN_App.MAX_VOLTAGE)){
								*/
							try{
								/*	if(Float.parseFloat(targetVoltageStr) >= ConstantProGEN_App.TARGET_PRIMARY_MIN_VOLTAGE){
										int voltagePercentage = Integer.parseInt(Data_HV_Src.getWritePrimaryVoltagePercentage());
										*/
										//ApplicationLauncher.logger.info("HV_StartTask: Manual: voltagePercentage:" + voltagePercentage);
								disableBtnConnect();		
								DisablePossibleClickEvents();
/*										btnVoltageStop.setDisable(false);
										ref_btnVoltagePauseDataReading.setDisable(false);

										String CommandVI_PayLoad = GUIUtils.IntStringToHexString(ratedVoltageStr) + 
												GUIUtils.IntStringToHexStringOneByte(String.valueOf(voltagePercentage));
		*/
										//SerialDM_Obj.VI_Start(CommandVI_PayLoad);
										ScanForSerialPorts();
										if(DisplayDataObj.powerSourcePortAccessible()){
											//SerialDM_Obj.ClearSerialDataInLDU_Ports();
											SerialDM_Obj.startSerialRxPhysical_PowerSource();
											SerialDM_Obj.enableSerialRxPhysical_PowerSourceMonitor();
											enableBtnDisconnect();
											enableBtnSendCommand();
											ref_txtAreaSerialData.appendText("Connected Power Source\n");
											
											//setHV_StopRequest(false);
											//VI_StartProcess(CommandVI_PayLoad,SelectedPhase,Data_HV_Src.getWritePrimaryTargetVoltage());

										}else{
											EnablePossibleClickEvents();
											enableBtnConnect();
										}
									/*}else{
										EnablePossibleClickEvents();
										//restoreUpdateEventsButtons();
										ApplicationLauncher.logger.info("Manual: HV_StartTask:" + ProGEN_ErrorCodeMapping.ERROR_CODE_006 +": "+ProGEN_ErrorCodeMapping.ERROR_CODE_006_MSG  + " : TARGET_PRIMARY_MIN_VOLTAGE: " +ConstantProGEN_App.TARGET_PRIMARY_MIN_VOLTAGE);
										ApplicationLauncher.InformUser(ProGEN_ErrorCodeMapping.ERROR_CODE_006,ProGEN_ErrorCodeMapping.ERROR_CODE_006_MSG + " : " +ConstantProGEN_App.TARGET_PRIMARY_MIN_VOLTAGE +" Volt",AlertType.ERROR);

									}*/
								}catch(Exception e) {
									e.printStackTrace();
									EnablePossibleClickEvents();
									enableBtnConnect();
									ApplicationLauncher.logger.error("Manual: HV_StartTask: ExceptionS: " + e.getMessage());
								}
/*							}else{
								ApplicationLauncher.logger.info("HV_StartTask:" + ProGEN_ErrorCodeMapping.ERROR_CODE_003 +": "+ProGEN_ErrorCodeMapping.ERROR_CODE_003_MSG+ ConstantProGEN_App.MIN_VOLTAGE + " and " +ConstantProGEN_App.MAX_VOLTAGE);
								ApplicationLauncher.InformUser(ProGEN_ErrorCodeMapping.ERROR_CODE_003,ProGEN_ErrorCodeMapping.ERROR_CODE_003_MSG+ ConstantProGEN_App.MIN_VOLTAGE +  " and " +ConstantProGEN_App.MAX_VOLTAGE,AlertType.ERROR);

								//ApplicationLauncher.logger.info("SetVoltage should be in between " + ConstantVICI_App.MIN_VOLTAGE + " and " +ConstantVICI_App.MAX_VOLTAGE);
								//ApplicationLauncher.InformUser("Error-03","SetVoltage should be in between " + ConstantVICI_App.MIN_VOLTAGE +  " and " +ConstantVICI_App.MAX_VOLTAGE,AlertType.ERROR);
							}
						}else{
							ApplicationLauncher.logger.info("HV_StartTask:" + ProGEN_ErrorCodeMapping.ERROR_CODE_002 +": "+ProGEN_ErrorCodeMapping.ERROR_CODE_002_MSG);
							ApplicationLauncher.InformUser(ProGEN_ErrorCodeMapping.ERROR_CODE_002,ProGEN_ErrorCodeMapping.ERROR_CODE_002_MSG,AlertType.ERROR);

							//ApplicationLauncher.logger.info("SetVoltage should be positive and a number");
							//ApplicationLauncher.InformUser("Error-02","SetVoltage should be positive and a number. Kindly enter the number",AlertType.ERROR);
						}
					}else{
						ApplicationLauncher.logger.info("HV_StartTask:" + ProGEN_ErrorCodeMapping.ERROR_CODE_001 +": "+ProGEN_ErrorCodeMapping.ERROR_CODE_001_MSG);
						ApplicationLauncher.InformUser(ProGEN_ErrorCodeMapping.ERROR_CODE_001,ProGEN_ErrorCodeMapping.ERROR_CODE_001_MSG,AlertType.ERROR);

						//ApplicationLauncher.logger.info("SetVoltage Field is Empty");
						//ApplicationLauncher.InformUser("Error-01","SetVoltage is empty. Kindly enter the same",AlertType.ERROR);
					}
				}else{

					ApplicationLauncher.logger.info("HV_StartTask:" + ProGEN_ErrorCodeMapping.ERROR_CODE_024 +": "+ProGEN_ErrorCodeMapping.ERROR_CODE_024_MSG);
					ApplicationLauncher.InformUser(ProGEN_ErrorCodeMapping.ERROR_CODE_024,ProGEN_ErrorCodeMapping.ERROR_CODE_024_MSG,AlertType.ERROR);
				}
			}else{

				ApplicationLauncher.logger.info("HV_StartTask:" + ProGEN_ErrorCodeMapping.ERROR_CODE_026 +": "+ProGEN_ErrorCodeMapping.ERROR_CODE_026_MSG);
				ApplicationLauncher.InformUser(ProGEN_ErrorCodeMapping.ERROR_CODE_026,ProGEN_ErrorCodeMapping.ERROR_CODE_026_MSG,AlertType.ERROR);
			}
		}else{

			ApplicationLauncher.logger.info("HV_StartTask:" + ProGEN_ErrorCodeMapping.ERROR_CODE_027 +": "+ProGEN_ErrorCodeMapping.ERROR_CODE_027_MSG);
			ApplicationLauncher.InformUser(ProGEN_ErrorCodeMapping.ERROR_CODE_027,ProGEN_ErrorCodeMapping.ERROR_CODE_027_MSG,AlertType.ERROR);
		}*/
	}
	
	public void sendCommandProcessTask(  ){
		ApplicationLauncher.logger.info("sendCommandProcessTask :Entry"  );
		//VI_StartProcessTaskTimer = new Timer();
		//VI_StartProcessTaskTimer.schedule(new VI_StartTask(CommandVI_PayLoad), 100);
		// version s3.9.5 -  added below flag - because Test point was running and not moving forward
		///*Platform.runLater(() -> {
			//DisplayDataObj.setLDU_ReadDataFlag( true);
			
			String payLoadInHex = GuiUtils.StringToHex(ref_txtAreaTargetCommand.getText());
			ApplicationLauncher.logger.info("VI_StartProcess : payLoadInHex: " + payLoadInHex  );
			int timeDelayInMilliSec = 0;
			if(ref_chkBoxIncludeDelay.isSelected()){
				timeDelayInMilliSec = Integer.parseInt(ref_txtSerialTxDelay.getText());
			}
			String expectedDataInHex = GuiUtils.StringToHex("K");
			boolean isResponseExpected = ref_chBxResponseExpectedForCmd.isSelected();
			String responseStatus = SerialDM_Obj.powerSourceSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
			//DisplayDataObj.setLDU_ReadDataFlag( false);
			if(isResponseExpected){
				if(responseStatus.equals(DeleteMeConstant.SUCCESS_RESPONSE)){
					ref_txtAreaSerialData.appendText("PwrSrc-Rcvd\n");
					//if(!isHV_StopRequest()) {
						//SerialDM_Obj.VICI_ReadVI_TimerTrigger();
					//}
					
				}else if(responseStatus.equals(DeleteMeConstant.NO_RESPONSE)){
					
					ApplicationLauncher.logger.info("VI Start:" + ErrorCodeMapping.ERROR_CODE_004 +": "+ErrorCodeMapping.ERROR_CODE_004_MSG);
					ApplicationLauncher.InformUser(ErrorCodeMapping.ERROR_CODE_004,ErrorCodeMapping.ERROR_CODE_004_MSG,AlertType.ERROR);
					//ApplicationLauncher.logger.info("VI Start no response received");
					//ApplicationLauncher.InformUser("Error-04","VI Start no response received",AlertType.ERROR);
					//SerialDM_Obj.disconnectPowerSource();
					EnablePossibleClickEvents();
				}else {
					//String ExpectedError1Data = ConstantPrimaryVICI_Meter.VI_CMD_START_ERROR_RESPONSE+ConstantPrimaryVICI_Meter.VI_ER_TERMINATOR;
					String ExpectedError1Data = DeleteMeConstant.HV_CMD_ERROR_RESPONSE_HDR;//+ConstantPrimaryVICI_Meter.VI_ER_TERMINATOR;
					
					ApplicationLauncher.logger.info("VI Start: responseStatus:" + responseStatus);
					ApplicationLauncher.logger.info("VI Start: ExpectedError1Data:" + ExpectedError1Data);
					//if(responseStatus.endsWith(ExpectedError1Data)){
					if ( (responseStatus.contains(ExpectedError1Data)) && 
						 (responseStatus.endsWith(DeleteMeConstant.HV_ER_TERMINATOR))){	
						ApplicationLauncher.logger.info("VI Start: response: "+responseStatus);
						String ErrorMsgID = ErrorCodeMapping.getErrorCodeID(responseStatus);
						String ErrorMsgDesc = ErrorCodeMapping.getErrorMsgDescription(ErrorMsgID);
						ApplicationLauncher.logger.info("VI Start:" + ErrorMsgID +": "+ErrorMsgDesc);
						ApplicationLauncher.InformUser(ErrorMsgID,ErrorMsgDesc,AlertType.ERROR);
						//ApplicationLauncher.logger.info("VI Start:");
						
						EnablePossibleClickEvents();
					}
					//SerialDM_Obj.disconnectPowerSource();
				}
			}else{
				ApplicationLauncher.logger.info("sendCommandProcessTask : no response expected"  );
				EnablePossibleClickEvents();
			}
		//});*/
		//Sleep(1000);//1000
		//SerialDM_Obj.LDU_ResetSetting();
		//Sleep(1000);//1000
		
		//return status;

	}
	
    void VoltageStopTask() {
/*    	btnVoltageStop.setDisable(true);
    	ref_btnFreeze.setDisable(true);
    	ref_btnVoltagePauseDataReading.setDisable(true);

    	setHV_StopRequest(true);
    	Sleep(1000);
		boolean status = SerialDM_Obj.VI_StopV2();
		//DisplayDataObj.setLDU_ReadDataFlag( false);
		if(status){
			txtAreaSerialData.appendText("VI-STOP\n");
			Sleep(2000);
			hvWriteMainContactorOffTask();*/
	    	SerialDM_Obj.disconnectPowerSource();
	    	enableBtnConnect();
	    	disableBtnDisconnect();
	    	disableBtnSendCommand();
	    	ref_txtAreaSerialData.appendText("Disconnected Power Source\n");
/*	    	Sleep(4000);
	    	ref_txtActualPrimaryVoltage.setText("");
	    	ref_txtActualPrimaryVoltagePhaseToPhase.setText("");
			EnablePossibleClickEvents();
		}else{
				ApplicationLauncher.logger.info("VoltageStopTask:" + ProGEN_ErrorCodeMapping.ERROR_CODE_005 +": "+ProGEN_ErrorCodeMapping.ERROR_CODE_005_MSG);
				ApplicationLauncher.InformUser(ProGEN_ErrorCodeMapping.ERROR_CODE_005,ProGEN_ErrorCodeMapping.ERROR_CODE_005_MSG,AlertType.ERROR);

				//ApplicationLauncher.logger.info("VI Stop no response received");
				//ApplicationLauncher.InformUser("Error-05","VI Stop no response received",AlertType.ERROR);
		}
		setUserAbortedFlag(false);*/
		SerialDM_Obj.disableSerialRxPhysical_PowerSourceMonitor();
		
	}
    
    @FXML
	public void sendCommandTrigger() {
		ApplicationLauncher.logger.info("sendCommandTrigger : Entry");
		
		sendCommandTimer = new Timer();
		//setProGenDemoExecution(true);
		
		sendCommandTimer.schedule(new sendCommandClick(), 100);
	}
    
    class sendCommandClick extends TimerTask{

		public void run() {
			ApplicationLauncher.logger.debug("sendCommandClick : Entry");
			if(!ref_txtAreaTargetCommand.getText().isEmpty()) {
				disableBtnSendCommand();

				ApplicationHomeController.disableLeftMenuButtonsForTestRun();
				disableBtnDisconnect();
				sendCommandProcessTask();
				enableBtnDisconnect();
				enableBtnSendCommand();
			}else{
				ApplicationLauncher.logger.debug("sendCommandClick : Target command empty");
				ApplicationLauncher.InformUser("Empty","Target command is empty. Kindly use <compute command> button", AlertType.INFORMATION);
			}

				sendCommandTimer.cancel();
			
		}

	}
    

	@FXML
	public void VoltageInjectionStartTrigger() {
		ApplicationLauncher.logger.info("VoltageInjectionStartTrigger : Entry");
		
		HV_StartTaskTimer = new Timer();
		//setProGenDemoExecution(true);
		HV_StartTaskTimer.schedule(new HV_StartTaskClick(), 100);
	}

	class HV_StartTaskClick extends TimerTask{

		public void run() {
			ApplicationLauncher.logger.info("VoltageInjectionStartTaskClick : Entry");
			//Platform.runLater(() -> {
				ApplicationHomeController.disableLeftMenuButtonsForTestRun();
				HV_StartTask();
			//});
			HV_StartTaskTimer.cancel();
			
		}

	}
	
	@FXML
	public void VoltageInjectionStopTrigger() {
		ApplicationLauncher.logger.info("VoltageInjectionStopTrigger : Entry");
		
		VoltageInjectionStopTaskTimer = new Timer();
		VoltageInjectionStopTaskTimer.schedule(new VoltageInjectionStopTaskClick(), 100);
	}

	class VoltageInjectionStopTaskClick extends TimerTask{

		public void run() {
			ApplicationLauncher.logger.info("VoltageInjectionStopTaskClick : Entry");
			//Platform.runLater(() -> {
			//ScanForSerialPorts();
			//if(DisplayDataObj.VICI_PortAccessible()){
				//SerialDM_Obj.ClearSerialDataInLDU_Ports();
				//SerialDM_Obj.StartSerialRxPhysicalHVCI();
				//SerialDM_Obj.EnableSerialRxPhysicalHVCI_Monitor();
				VoltageStopTask();
				//hvWriteMainContactorOffTask();
				ApplicationHomeController.enableLeftMenuButtonsForTestRun();

			//}
			//setUserAbortedFlag(false);
			//});
			VoltageInjectionStopTaskTimer.cancel();
			
		}

	}
	
	@FXML
	public void clearSerialDataDisplayClick(){
		
		ApplicationLauncher.logger.info("clearSerialDataDisplayClick : Entry");
		ref_txtAreaSerialData.clear();
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());;
		ref_txtAreaSerialData.appendText(timeStamp+"\n");
		ApplicationLauncher.logger.info("clearSerialDataDisplayClick : Timestamp: "+timeStamp);
		
	}
	
	@FXML
	public void convertSeperator(){
		ApplicationLauncher.logger.info("clearSerialDataDisplayClick : ref_txtTxCmdSeperatorInHex.getText(): " + ref_txtTxCmdSeperatorInHex.getText());
		ApplicationLauncher.logger.info("clearSerialDataDisplayClick : ref_txtTxCmdSeperatorInHex.getText(): " + GuiUtils.HexToString(ref_txtTxCmdSeperatorInHex.getText()));
		Platform.runLater(() -> {
			ref_txtTxCmdSeperatorInStr.setText(GuiUtils.HexToString(ref_txtTxCmdSeperatorInHex.getText()));
		});
	}
	
	@FXML
	public void convertTxTerminator(){
		Platform.runLater(() -> {
			ref_txtTxCmdTerminatorInStr.setText(GuiUtils.HexToString(ref_txtTxCmdTerminatorInHex.getText()));
		});
	}
	
	@FXML
	public void convertRxTerminator(){
		Platform.runLater(() -> {
			ref_txtRxCmdTerminatorInStr.setText(GuiUtils.HexToString(ref_txtRxCmdTerminatorInHex.getText()));
		});
	}
	
	@FXML
	public void computeCommandOnClick(){
		if(PROPOWER_V1){
			propowerComputeCommandV1_process();
		}
	}
	
	@FXML
	public void rPhaseCurrentRelayIdOnChange(){
		
		Platform.runLater(() -> {
			ref_txtRphaseCurrentRelayIdValue.setText(PROPOWER_V1_CURRENT_RELAY_ID_VALUE_LIST.get(ref_cmBxCurrentRelayId.getSelectionModel().getSelectedIndex()));
		});		
	}
	
	@FXML
	public void cmbBxMessageTypeOnChange(){
		if(PROPOWER_V1){
			propowerComputeCommandV1_process();
		}
/*		Platform.runLater(() -> {
			ref_txtAreaTargetCommand.clear();
		});	*/	
	}
	
		
	@FXML
	public void chkBoxRphaseSelectAllOnChange(){
		
		Platform.runLater(() -> {
			if(ref_chkBoxRphaseSelectAll.isSelected()){
				ref_ckBxRphaseVoltage.setSelected(true);
				ref_ckBxRphaseCurrent.setSelected(true);
				ref_ckBxRphasePhaseAngle.setSelected(true);
				ref_ckBxRphaseCurrentRelayId.setSelected(true);
				ref_ckBxRphaseFrequency.setSelected(true);
			}else{
				ref_ckBxRphaseVoltage.setSelected(false);
				ref_ckBxRphaseCurrent.setSelected(false);
				ref_ckBxRphasePhaseAngle.setSelected(false);
				ref_ckBxRphaseCurrentRelayId.setSelected(false);
				ref_ckBxRphaseFrequency.setSelected(false);
			}
		});		
	}
	
	@FXML
	public void chkBoxYphaseSelectAllOnChange(){
		
		Platform.runLater(() -> {
			if(ref_chkBoxYphaseSelectAll.isSelected()){
				ref_ckBxYphaseVoltage.setSelected(true);
				ref_ckBxYphaseCurrent.setSelected(true);
				ref_ckBxYphasePhaseAngle.setSelected(true);
				
			}else{
				ref_ckBxYphaseVoltage.setSelected(false);
				ref_ckBxYphaseCurrent.setSelected(false);
				ref_ckBxYphasePhaseAngle.setSelected(false);
			}
		});		
	}
	
	@FXML
	public void chkBoxBphaseSelectAllOnChange(){
		
		Platform.runLater(() -> {
			if(ref_chkBoxBphaseSelectAll.isSelected()){
				ref_ckBxBphaseVoltage.setSelected(true);
				ref_ckBxBphaseCurrent.setSelected(true);
				ref_ckBxBphasePhaseAngle.setSelected(true);
			}else{
				ref_ckBxBphaseVoltage.setSelected(false);
				ref_ckBxBphaseCurrent.setSelected(false);
				ref_ckBxBphasePhaseAngle.setSelected(false);
			}
		});		
	}
	
	@FXML
	public void rdBtnSinglePhaseOnClick(){
		
		Platform.runLater(() -> {
			if(ref_rdBtnSinglePhase.isSelected()){
				ref_rdBtnThreePhase.setSelected(false);
				ref_chkBoxRphaseSelectAll.setSelected(true);
				ref_chkBoxYphaseSelectAll.setSelected(false);
				ref_chkBoxBphaseSelectAll.setSelected(false);
				ref_chkBoxYphaseSelectAll.setDisable(true);
				ref_chkBoxBphaseSelectAll.setDisable(true);
				
				ref_ckBxYphaseVoltage.setDisable(true);
				ref_ckBxYphaseCurrent.setDisable(true);
				ref_ckBxYphasePhaseAngle.setDisable(true);
				
				ref_ckBxBphaseVoltage.setDisable(true);
				ref_ckBxBphaseCurrent.setDisable(true);
				ref_ckBxBphasePhaseAngle.setDisable(true);
				
				chkBoxRphaseSelectAllOnChange();
				chkBoxYphaseSelectAllOnChange();
				chkBoxBphaseSelectAllOnChange();
			}else{
				ref_rdBtnSinglePhase.setSelected(true);
			}
		});		
	}
	
	
	@FXML
	public void rdBtnThreePhaseOnClick(){
		
		Platform.runLater(() -> {
			if(ref_rdBtnThreePhase.isSelected()){
				ref_rdBtnSinglePhase.setSelected(false);
				ref_chkBoxRphaseSelectAll.setSelected(true);
				ref_chkBoxYphaseSelectAll.setSelected(true);
				ref_chkBoxBphaseSelectAll.setSelected(true);
				ref_chkBoxYphaseSelectAll.setDisable(false);
				ref_chkBoxBphaseSelectAll.setDisable(false);
				
				ref_ckBxYphaseVoltage.setDisable(false);
				ref_ckBxYphaseCurrent.setDisable(false);
				ref_ckBxYphasePhaseAngle.setDisable(false);
				
				ref_ckBxBphaseVoltage.setDisable(false);
				ref_ckBxBphaseCurrent.setDisable(false);
				ref_ckBxBphasePhaseAngle.setDisable(false);
				
				chkBoxRphaseSelectAllOnChange();
				chkBoxYphaseSelectAllOnChange();
				chkBoxBphaseSelectAllOnChange();
			}else{
				ref_rdBtnThreePhase.setSelected(true);
			}
		});		
	}
	
	private void propowerComputeCommandV1_process() {
		
		String selectedCommand = ref_cmbBxMessageType.getSelectionModel().getSelectedItem().toString();
		ApplicationLauncher.logger.debug("propowerComputeCommandV1_process : selectedCommand: " + selectedCommand);
		String command = "";
		ref_txtTxCmdSeperatorInStr.setText(GuiUtils.HexToString(ref_txtTxCmdSeperatorInHex.getText()));
		ref_txtTxCmdTerminatorInStr.setText(GuiUtils.HexToString(ref_txtTxCmdTerminatorInHex.getText()));
		switch (selectedCommand){
		
			case PROPOWER_V1_BOOT_INIT:
								
				command = ref_txInitPasswordStr.getText();
				break;
				
			case PROPOWER_V1_PRE_START:
				command = ConstantPowerSourceLscs.CMD_PWR_SRC_SINGLE_PHASE_INIT_PREFIX;
				break;
				
			case PROPOWER_V1_APP_VB_MODE:
				command = ConstantPowerSourceLscs.CMD_PWR_SRC_INIT_SET_APP_MODE;
				break;
				
			case PROPOWER_V1_HYPER_TERMINAL_MODE:
				command = ConstantPowerSourceLscs.CMD_PWR_SRC_INIT_SET_HYPERT_MODE;
				break;
				
			case PROPOWER_V1_DAC_MODE:
				command = ConstantPowerSourceLscs.CMD_PWR_SRC_INIT_SET_DAC_MODE;
				break;
				
			case PROPOWER_V1_STOP:
				command = ConstantPowerSourceLscs.CMD_PWR_SRC_OFF;
				break;
				
			case PROPOWER_V1_MANIPULATE:
				
				if(ref_chBxPhaseReversal.isSelected()){
					command = ConstantPowerSourceLscs.CMD_PWR_SRC_PHASE_REVERSE_PREFIX ;
				}
				
				if(ref_ckBxRphaseVoltage.isSelected()){
					command = command + ConstantPowerSourceLscs.CMD_PWR_SRC_U1_PREFIX + ref_txtRphaseVoltage.getText() + ref_txtTxCmdSeperatorInStr.getText() ;
				}
				
				if(ref_ckBxRphaseCurrent.isSelected()){
					command = command + ConstantPowerSourceLscs.CMD_PWR_SRC_I1_PREFIX + ref_txtRphaseCurrent.getText() + ref_txtTxCmdSeperatorInStr.getText() ;
				}
				
				if(ref_ckBxRphasePhaseAngle.isSelected()){
					command = command + ConstantPowerSourceLscs.CMD_PWR_SRC_PF1_PREFIX + ref_txtRphasePhaseAngle.getText() + ref_txtTxCmdSeperatorInStr.getText() ;
				}
				
				
				if(ref_ckBxYphaseVoltage.isSelected()){
					command = command + ConstantPowerSourceLscs.CMD_PWR_SRC_U2_PREFIX + ref_txtYphaseVoltage.getText() + ref_txtTxCmdSeperatorInStr.getText() ;
				}
				
				if(ref_ckBxYphaseCurrent.isSelected()){
					command = command + ConstantPowerSourceLscs.CMD_PWR_SRC_I2_PREFIX + ref_txtYphaseCurrent.getText() + ref_txtTxCmdSeperatorInStr.getText() ;
				}
				
				if(ref_ckBxYphasePhaseAngle.isSelected()){
					command = command + ConstantPowerSourceLscs.CMD_PWR_SRC_PF2_PREFIX + ref_txtYphasePhaseAngle.getText() + ref_txtTxCmdSeperatorInStr.getText() ;
				}

				
				if(ref_ckBxBphaseVoltage.isSelected()){
					command = command + ConstantPowerSourceLscs.CMD_PWR_SRC_U3_PREFIX + ref_txtBphaseVoltage.getText() + ref_txtTxCmdSeperatorInStr.getText() ;
				}
				
				if(ref_ckBxBphaseCurrent.isSelected()){
					command = command + ConstantPowerSourceLscs.CMD_PWR_SRC_I3_PREFIX + ref_txtBphaseCurrent.getText() + ref_txtTxCmdSeperatorInStr.getText() ;
				}
				
				if(ref_ckBxBphasePhaseAngle.isSelected()){
					command = command + ConstantPowerSourceLscs.CMD_PWR_SRC_PF3_PREFIX + ref_txtBphasePhaseAngle.getText() + ref_txtTxCmdSeperatorInStr.getText() ;
				}
				
				if(ref_ckBxRphaseCurrentRelayId.isSelected()){
					command = command + ConstantPowerSourceLscs.CMD_PWR_SRC_SELECT_CUR_RELAY_PREFIX + ref_cmBxCurrentRelayId.getSelectionModel().getSelectedItem().toString()
							+ ref_txtTxCmdSeperatorInStr.getText() ;
				}
				
				if(ref_ckBxRphaseFrequency.isSelected()){
					command = command + ConstantPowerSourceLscs.CMD_PWR_SRC_FRQ_PREFIX + ref_txtRphaseFrequency.getText() + ref_txtTxCmdSeperatorInStr.getText() ;
				}
				
				if(command.equals(ConstantPowerSourceLscs.CMD_PWR_SRC_PHASE_REVERSE_PREFIX )){
					command = command + ref_txtTxCmdSeperatorInStr.getText()  + ref_txtTxCmdTerminatorInStr.getText();
				}else {

					command = command + ref_txtTxCmdTerminatorInStr.getText();
				}
				
				break;

				
			default:
				ApplicationLauncher.logger.debug("propowerComputeCommandV1_process : default" );
				break;
		
		}
		ApplicationLauncher.logger.debug("propowerComputeCommandV1_process : command: " + command);
		String commandStr = command;
		Platform.runLater(() -> {
			ref_txtAreaTargetCommand.setText(commandStr);
		});
	}
	

	
	public static void updateRxSerialMessageDisplay(String  rxDisplayDataInHex){
		Platform.runLater(() -> {
			//ref_txtAreaSerialData.setText(Value);
			try {
				if(isSerialDisplayProcess()) {
					ref_txtAreaRxSerialData.appendText(GuiUtils.HexToString(rxDisplayDataInHex));
					ref_txtAreaRxSerialData.selectPositionCaret(ref_txtAreaRxSerialData.getLength()); 
					ref_txtAreaRxSerialData.deselect();
					
					ref_txtAreaRxSerialDataInHex.appendText(rxDisplayDataInHex);
					ref_txtAreaRxSerialDataInHex.selectPositionCaret(ref_txtAreaRxSerialDataInHex.getLength()); 
					ref_txtAreaRxSerialDataInHex.deselect();
				}
			}catch(Exception e) {
				e.printStackTrace();
				ApplicationLauncher.logger.error("updateRxSerialMessageDisplay: Exception: " + e.getMessage());
			}
		});
		
	}
	
	
	
}
