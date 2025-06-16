package com.tasnetwork.calibration.energymeter.calib;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tasnetwork.calibration.energymeter.ApplicationHomeController;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdKre;
import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
import com.tasnetwork.calibration.energymeter.device.Data_RefStdKre;
import com.tasnetwork.calibration.energymeter.device.Data_RefStdSands;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.device.SerialDataManager;
import com.tasnetwork.calibration.energymeter.director.RefStdDirector;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class LscsCalibrationController implements Initializable{

	//public static String serialPortConfigFilePathName = "/resources/"+ConstantVersion.SerialPortConfigFileName;

	Timer startRunTimer;
	Timer ShutDownAllComPortTimer;
	Timer TCStopConfirmationTimer;
	
	private static String lastVisitedDirectory=System.getProperty("user.home");

	Timer sendDataTimer;
	//DeviceDataManagerController.getLscsCalibrationConfigParsedKey()

	private static volatile LscsCalibrationConfigModel lscsCalibrationConfigFileUpdatedKeyValue = new LscsCalibrationConfigModel();

	private static String lastGuiVoltageRphaseSelected = "";	
	private static String lastGuiVoltageRphaseTapMaxList = "";
	private static String lastGuiVoltageRphaseCalibPointNameList = ""; 
	
	private static String lastGuiCurrentRphaseSelected = "";
	private static String lastGuiCurrentRphaseTapMaxList = "";
	private static String lastGuiCurrentRphaseCalibPointNameList = ""; 

	private volatile boolean initInProgress = false;
	private volatile boolean voltageRphaseSelectionChangeGuiInProgress = false;
	private volatile boolean voltageRphaseTapMaxChangeGuiInProgress = false;
	private volatile boolean voltageYphaseSelectionChangeGuiInProgress = false;
	private volatile boolean voltageYphaseTapMaxChangeGuiInProgress = false;
	private volatile boolean voltageBphaseSelectionChangeGuiInProgress = false;
	private volatile boolean voltageBphaseTapMaxChangeGuiInProgress = false;

	private volatile boolean currentRphaseSelectionChangeGuiInProgress = false;
	private volatile boolean currentRphaseTapMaxChangeGuiInProgress = false;
	private volatile boolean currentYphaseSelectionChangeGuiInProgress = false;
	private volatile boolean currentYphaseTapMaxChangeGuiInProgress = false;
	private volatile boolean currentBphaseSelectionChangeGuiInProgress = false;
	private volatile boolean currentBphaseTapMaxChangeGuiInProgress = false;
	//private volatile boolean voltagePhaseChangeGuiInProgress = false;

	static DeviceDataManagerController DisplayDataObj =  new DeviceDataManagerController();
	public static SerialDataManager SerialDM_Obj = new SerialDataManager();

	private boolean lastRdBtnCurrentRphaseOnlyValue = false;
	private boolean lastRdBtnCurrentYphaseOnlyValue = false;
	private boolean lastRdBtnCurrentBphaseOnlyValue = false;
	private boolean lastrdBtnCurrentAllPhase= false;
	
	private String lastCmBxCurrentRphaseSelected = "";
	private String lastTxtCurrentRphaseNoOfTapping = "";
	private String lastCmBxCurrentRphaseTapMaxList = "";
	private String lastTxtCurrentRphaseTapSelectionRelayCode = "";
	private String lastTxtCurrentRphaseNoOfCalibPointEachTap = "";
	private String lastCmBxCurrentRphaseCalibPointNameList = "";
	private String lastTxtCurrentRphaseCalibPointValue = "";
	

	//@FXML private ComboBox cmBxVoltageTap1;
	@FXML private Button btnComStart;
	@FXML private Button btnComStop;
	//@FXML private TextField txtVoltTap1Rms;

	@FXML private RadioButton rdBtnCurrentRphaseOnly;
	@FXML private RadioButton rdBtnCurrentYphaseOnly;
	@FXML private RadioButton rdBtnCurrentBphaseOnly;
	@FXML private RadioButton rdBtnCurrentAllPhase;

	private static RadioButton ref_rdBtnCurrentRphaseOnly;
	private static RadioButton ref_rdBtnCurrentYphaseOnly;
	private static RadioButton ref_rdBtnCurrentBphaseOnly;
	private static RadioButton ref_rdBtnCurrentAllPhase;

	@FXML private RadioButton rdBtnVoltageRphaseOnly;
	@FXML private RadioButton rdBtnVoltageYphaseOnly;
	@FXML private RadioButton rdBtnVoltageBphaseOnly;
	@FXML private RadioButton rdBtnVoltageAllPhase;
	
	@FXML private TextArea txtAreaJsonFileDisplay;

	private static RadioButton ref_rdBtnVoltageRphaseOnly;
	private static RadioButton ref_rdBtnVoltageYphaseOnly;
	private static RadioButton ref_rdBtnVoltageBphaseOnly;
	private static RadioButton ref_rdBtnVoltageAllPhase;
	
	private static TextArea ref_txtAreaJsonFileDisplay;

	/*	@FXML private ComboBox cmBxVoltPhaseSelected;
	@FXML private ComboBox cmBxVoltTapMaxList;
	@FXML private ComboBox cmBxVoltCalibPointNameList;
	@FXML private TextField txtVoltNoOfTapping;
	@FXML private TextField txtVoltTapSelectionRelayCode;
	@FXML private TextField txtVoltNoOfCalibPointEachTap;
	@FXML private TextField txtVoltCalibPointValue;
	@FXML private TextField txtVoltPresentRmsValue;
	@FXML private TextField txtVoltUpdatedInRefStd;
	@FXML private TextField txtVoltNewCalcRmsValue;
	@FXML private TextField txtVoltPresentGainValue;
	@FXML private TextField txtVoltPresentOffsetValue;
	@FXML private TextField txtVoltNewCalcGainValue;
	@FXML private TextField txtVoltNewCalcOffsetValue;





	private static ComboBox ref_cmBxVoltPhaseSelected;
	private static ComboBox ref_cmBxVoltTapMaxList;
	private static ComboBox ref_cmBxVoltCalibPointNameList;
	private static TextField ref_txtVoltNoOfTapping;
	private static TextField ref_txtVoltTapSelectionRelayCode;
	private static TextField ref_txtVoltNoOfCalibPointEachTap;
	private static TextField ref_txtVoltCalibPointValue;
	private static TextField ref_txtVoltPresentRmsValue;
	private static TextField ref_txtVoltUpdatedInRefStd;
	private static TextField ref_txtVoltNewCalcRmsValue;
	private static TextField ref_txtVoltPresentGainValue;
	private static TextField ref_txtVoltPresentOffsetValue;
	private static TextField ref_txtVoltNewCalcGainValue;
	private static TextField ref_txtVoltNewCalcOffsetValue;*/



	@FXML private ComboBox<String> cmBxCurrentRphaseSelected;
	@FXML private ComboBox<Float> cmBxCurrentRphaseTapMaxList;
	@FXML private ComboBox<String> cmBxCurrentRphaseCalibPointNameList;
	@FXML private TextField txtCurrentRphaseNoOfTapping;
	@FXML private TextField txtCurrentRphaseTapSelectionRelayCode;
	@FXML private TextField txtCurrentRphaseNoOfCalibPointEachTap;
	@FXML private TextField txtCurrentRphaseCalibPointValue;
	@FXML private TextField txtCurrentRphasePresentRmsValue;
	@FXML private TextField txtCurrentRphaseUpdatedInRefStd;
	@FXML private TextField txtCurrentRphaseNewCalcRmsValue;
	@FXML private TextField txtCurrentRphasePresentGainValue;
	@FXML private TextField txtCurrentRphasePresentOffsetValue;
	@FXML private TextField txtCurrentRphaseNewCalcGainValue;
	@FXML private TextField txtCurrentRphaseNewCalcOffsetValue;




	private static ComboBox<String> ref_cmBxCurrentRphaseSelected;
	private static ComboBox<Float> ref_cmBxCurrentRphaseTapMaxList;
	private static ComboBox<String> ref_cmBxCurrentRphaseCalibPointNameList;	
	private static TextField ref_txtCurrentRphaseNoOfTapping;
	private static TextField ref_txtCurrentRphaseTapSelectionRelayCode;
	private static TextField ref_txtCurrentRphaseNoOfCalibPointEachTap;
	private static TextField ref_txtCurrentRphaseCalibPointValue;
	private static TextField ref_txtCurrentRphasePresentRmsValue;
	private static TextField ref_txtCurrentRphaseUpdatedInRefStd;
	private static TextField ref_txtCurrentRphaseNewCalcRmsValue;
	private static TextField ref_txtCurrentRphasePresentGainValue;
	private static TextField ref_txtCurrentRphasePresentOffsetValue;
	private static TextField ref_txtCurrentRphaseNewCalcGainValue;
	private static TextField ref_txtCurrentRphaseNewCalcOffsetValue;



	@FXML private ComboBox<String> cmBxCurrentYphaseSelected;
	@FXML private ComboBox<Float> cmBxCurrentYphaseTapMaxList;
	@FXML private ComboBox<String> cmBxCurrentYphaseCalibPointNameList;
	@FXML private TextField txtCurrentYphaseNoOfTapping;
	@FXML private TextField txtCurrentYphaseTapSelectionRelayCode;
	@FXML private TextField txtCurrentYphaseNoOfCalibPointEachTap;
	@FXML private TextField txtCurrentYphaseCalibPointValue;
	@FXML private TextField txtCurrentYphasePresentRmsValue;
	@FXML private TextField txtCurrentYphaseUpdatedInRefStd;
	@FXML private TextField txtCurrentYphaseNewCalcRmsValue;
	@FXML private TextField txtCurrentYphasePresentGainValue;
	@FXML private TextField txtCurrentYphasePresentOffsetValue;
	@FXML private TextField txtCurrentYphaseNewCalcGainValue;
	@FXML private TextField txtCurrentYphaseNewCalcOffsetValue;




	private static ComboBox<String> ref_cmBxCurrentYphaseSelected;
	private static ComboBox<Float> ref_cmBxCurrentYphaseTapMaxList;
	private static ComboBox<String> ref_cmBxCurrentYphaseCalibPointNameList;	
	private static TextField ref_txtCurrentYphaseNoOfTapping;
	private static TextField ref_txtCurrentYphaseTapSelectionRelayCode;
	private static TextField ref_txtCurrentYphaseNoOfCalibPointEachTap;
	private static TextField ref_txtCurrentYphaseCalibPointValue;
	private static TextField ref_txtCurrentYphasePresentRmsValue;
	private static TextField ref_txtCurrentYphaseUpdatedInRefStd;
	private static TextField ref_txtCurrentYphaseNewCalcRmsValue;
	private static TextField ref_txtCurrentYphasePresentGainValue;
	private static TextField ref_txtCurrentYphasePresentOffsetValue;
	private static TextField ref_txtCurrentYphaseNewCalcGainValue;
	private static TextField ref_txtCurrentYphaseNewCalcOffsetValue;


	@FXML private ComboBox<String> cmBxCurrentBphaseSelected;
	@FXML private ComboBox<Float> cmBxCurrentBphaseTapMaxList;
	@FXML private ComboBox<String> cmBxCurrentBphaseCalibPointNameList;
	@FXML private TextField txtCurrentBphaseNoOfTapping;
	@FXML private TextField txtCurrentBphaseTapSelectionRelayCode;
	@FXML private TextField txtCurrentBphaseNoOfCalibPointEachTap;
	@FXML private TextField txtCurrentBphaseCalibPointValue;
	@FXML private TextField txtCurrentBphasePresentRmsValue;
	@FXML private TextField txtCurrentBphaseUpdatedInRefStd;
	@FXML private TextField txtCurrentBphaseNewCalcRmsValue;
	@FXML private TextField txtCurrentBphasePresentGainValue;
	@FXML private TextField txtCurrentBphasePresentOffsetValue;
	@FXML private TextField txtCurrentBphaseNewCalcGainValue;
	@FXML private TextField txtCurrentBphaseNewCalcOffsetValue;




	private static ComboBox<String> ref_cmBxCurrentBphaseSelected;
	private static ComboBox<Float> ref_cmBxCurrentBphaseTapMaxList;
	private static ComboBox<String> ref_cmBxCurrentBphaseCalibPointNameList;	
	private static TextField ref_txtCurrentBphaseNoOfTapping;
	private static TextField ref_txtCurrentBphaseTapSelectionRelayCode;
	private static TextField ref_txtCurrentBphaseNoOfCalibPointEachTap;
	private static TextField ref_txtCurrentBphaseCalibPointValue;
	private static TextField ref_txtCurrentBphasePresentRmsValue;
	private static TextField ref_txtCurrentBphaseUpdatedInRefStd;
	private static TextField ref_txtCurrentBphaseNewCalcRmsValue;
	private static TextField ref_txtCurrentBphasePresentGainValue;
	private static TextField ref_txtCurrentBphasePresentOffsetValue;
	private static TextField ref_txtCurrentBphaseNewCalcGainValue;
	private static TextField ref_txtCurrentBphaseNewCalcOffsetValue;				




	@FXML private ComboBox<String> cmBxVoltageRphaseSelected;
	@FXML private ComboBox<Float> cmBxVoltageRphaseTapMaxList;
	@FXML private ComboBox<String> cmBxVoltageRphaseCalibPointNameList;
	@FXML private TextField txtVoltageRphaseNoOfTapping;
	@FXML private TextField txtVoltageRphaseTapSelectionRelayCode;
	@FXML private TextField txtVoltageRphaseNoOfCalibPointEachTap;
	@FXML private TextField txtVoltageRphaseCalibPointValue;
	@FXML private TextField txtVoltageRphasePresentRmsValue;
	@FXML private TextField txtVoltageRphaseUpdatedInRefStd;
	@FXML private TextField txtVoltageRphaseNewCalcRmsValue;
	@FXML private TextField txtVoltageRphasePresentGainValue;
	@FXML private TextField txtVoltageRphasePresentOffsetValue;
	@FXML private TextField txtVoltageRphaseNewCalcGainValue;
	@FXML private TextField txtVoltageRphaseNewCalcOffsetValue;




	private static ComboBox<String> ref_cmBxVoltageRphaseSelected;
	private static ComboBox<Float> ref_cmBxVoltageRphaseTapMaxList;
	private static ComboBox<String> ref_cmBxVoltageRphaseCalibPointNameList;	
	private static TextField ref_txtVoltageRphaseNoOfTapping;
	private static TextField ref_txtVoltageRphaseTapSelectionRelayCode;
	private static TextField ref_txtVoltageRphaseNoOfCalibPointEachTap;
	private static TextField ref_txtVoltageRphaseCalibPointValue;
	private static TextField ref_txtVoltageRphasePresentRmsValue;
	private static TextField ref_txtVoltageRphaseUpdatedInRefStd;
	private static TextField ref_txtVoltageRphaseNewCalcRmsValue;
	private static TextField ref_txtVoltageRphasePresentGainValue;
	private static TextField ref_txtVoltageRphasePresentOffsetValue;
	private static TextField ref_txtVoltageRphaseNewCalcGainValue;
	private static TextField ref_txtVoltageRphaseNewCalcOffsetValue;



	@FXML private ComboBox<String> cmBxVoltageYphaseSelected;
	@FXML private ComboBox<Float> cmBxVoltageYphaseTapMaxList;
	@FXML private ComboBox<String> cmBxVoltageYphaseCalibPointNameList;
	@FXML private TextField txtVoltageYphaseNoOfTapping;
	@FXML private TextField txtVoltageYphaseTapSelectionRelayCode;
	@FXML private TextField txtVoltageYphaseNoOfCalibPointEachTap;
	@FXML private TextField txtVoltageYphaseCalibPointValue;
	@FXML private TextField txtVoltageYphasePresentRmsValue;
	@FXML private TextField txtVoltageYphaseUpdatedInRefStd;
	@FXML private TextField txtVoltageYphaseNewCalcRmsValue;
	@FXML private TextField txtVoltageYphasePresentGainValue;
	@FXML private TextField txtVoltageYphasePresentOffsetValue;
	@FXML private TextField txtVoltageYphaseNewCalcGainValue;
	@FXML private TextField txtVoltageYphaseNewCalcOffsetValue;




	private static ComboBox<String> ref_cmBxVoltageYphaseSelected;
	private static ComboBox<Float> ref_cmBxVoltageYphaseTapMaxList;
	private static ComboBox<String> ref_cmBxVoltageYphaseCalibPointNameList;	
	private static TextField ref_txtVoltageYphaseNoOfTapping;
	private static TextField ref_txtVoltageYphaseTapSelectionRelayCode;
	private static TextField ref_txtVoltageYphaseNoOfCalibPointEachTap;
	private static TextField ref_txtVoltageYphaseCalibPointValue;
	private static TextField ref_txtVoltageYphasePresentRmsValue;
	private static TextField ref_txtVoltageYphaseUpdatedInRefStd;
	private static TextField ref_txtVoltageYphaseNewCalcRmsValue;
	private static TextField ref_txtVoltageYphasePresentGainValue;
	private static TextField ref_txtVoltageYphasePresentOffsetValue;
	private static TextField ref_txtVoltageYphaseNewCalcGainValue;
	private static TextField ref_txtVoltageYphaseNewCalcOffsetValue;


	@FXML private ComboBox<String> cmBxVoltageBphaseSelected;
	@FXML private ComboBox<Float> cmBxVoltageBphaseTapMaxList;
	@FXML private ComboBox<String> cmBxVoltageBphaseCalibPointNameList;
	@FXML private TextField txtVoltageBphaseNoOfTapping;
	@FXML private TextField txtVoltageBphaseTapSelectionRelayCode;
	@FXML private TextField txtVoltageBphaseNoOfCalibPointEachTap;
	@FXML private TextField txtVoltageBphaseCalibPointValue;
	@FXML private TextField txtVoltageBphasePresentRmsValue;
	@FXML private TextField txtVoltageBphaseUpdatedInRefStd;
	@FXML private TextField txtVoltageBphaseNewCalcRmsValue;
	@FXML private TextField txtVoltageBphasePresentGainValue;
	@FXML private TextField txtVoltageBphasePresentOffsetValue;
	@FXML private TextField txtVoltageBphaseNewCalcGainValue;
	@FXML private TextField txtVoltageBphaseNewCalcOffsetValue;




	private static ComboBox<String> ref_cmBxVoltageBphaseSelected;
	private static ComboBox<Float> ref_cmBxVoltageBphaseTapMaxList;
	private static ComboBox<String> ref_cmBxVoltageBphaseCalibPointNameList;	
	private static TextField ref_txtVoltageBphaseNoOfTapping;
	private static TextField ref_txtVoltageBphaseTapSelectionRelayCode;
	private static TextField ref_txtVoltageBphaseNoOfCalibPointEachTap;
	private static TextField ref_txtVoltageBphaseCalibPointValue;
	private static TextField ref_txtVoltageBphasePresentRmsValue;
	private static TextField ref_txtVoltageBphaseUpdatedInRefStd;
	private static TextField ref_txtVoltageBphaseNewCalcRmsValue;
	private static TextField ref_txtVoltageBphasePresentGainValue;
	private static TextField ref_txtVoltageBphasePresentOffsetValue;
	private static TextField ref_txtVoltageBphaseNewCalcGainValue;
	private static TextField ref_txtVoltageBphaseNewCalcOffsetValue;
	
	
	@FXML private CheckBox chkBxCurrentSyncSelection;

	private static CheckBox ref_chkBxCurrentSyncSelection;

	@FXML private CheckBox chkBxVoltageSyncSelection;

	private static CheckBox ref_chkBxVoltageSyncSelection;


	//private static ComboBox ref_cmBxVoltageTap1;
	private static Button ref_btnComStart;
	private static Button ref_btnComStop;
	//private static TextField ref_txtVoltTap1Rms;
	
	@FXML private TitledPane titledPaneVoltCalib;
	private static TitledPane ref_titledPaneVoltCalib;
	
	@FXML private TitledPane titledPaneCurrentCalib;
	private static TitledPane ref_titledPaneCurrentCalib;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		

		refAssignment();



		initGuiObjects();
		Platform.runLater(() -> {

			ref_titledPaneVoltCalib.setExpanded(true);
			//ref_titledPaneCurrentCalib.setExpanded(true);
			
		});
		
/*		titledPane.expandedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

            }
        });*/
		//saveJsonFile();
	}

	public void saveJsonFile(){
		ApplicationLauncher.logger.info("saveJsonFile: Entry");
		GsonBuilder builder = new GsonBuilder(); 
		Gson gson = builder.setPrettyPrinting().create();
		OutputStream out = null;
		BufferedWriter bw = null;
		//double newSetGainValue= 0.0;
		//double newSetOffsetValue= 0.0;
		try {
			
			/*newSetGainValue = DeviceDataManagerController.getLscsCalibrationConfigParsedKey().getVoltageCalibration().get(0).getVoltageTap().get(0).getCalibPoints().get(1).getGainValue();
			newSetOffsetValue = DeviceDataManagerController.getLscsCalibrationConfigParsedKey().getVoltageCalibration().get(0).getVoltageTap().get(0).getCalibPoints().get(1).getOffsetValue();
			ApplicationLauncher.logger.debug("calculateNewVoltageGainOffset: newSetGainValue5 : " + newSetGainValue);
			ApplicationLauncher.logger.debug("calculateNewVoltageGainOffset: newSetOffsetValue5 : " + newSetOffsetValue);
			
			newSetGainValue = lscsCalibrationConfigFileUpdatedKeyValue.getVoltageCalibration().get(0).getVoltageTap().get(0).getCalibPoints().get(1).getGainValue();
			newSetOffsetValue = lscsCalibrationConfigFileUpdatedKeyValue.getVoltageCalibration().get(0).getVoltageTap().get(0).getCalibPoints().get(1).getOffsetValue();
			ApplicationLauncher.logger.debug("calculateNewVoltageGainOffset: newSetGainValue6 : " + newSetGainValue);
			ApplicationLauncher.logger.debug("calculateNewVoltageGainOffset: newSetOffsetValue6 : " + newSetOffsetValue);
			*/
			//URL resourceUrl = LscsCalibrationController.class.getResource(ConstantVersion.lscsCalibrationConfigFileName); //getClass().getResource(serialPortConfigFilePathName);
			URL resourceUrl = LscsCalibrationController.class.getResource(LscsCalibrationConfigLoader.getLscsCalibrationFileName());
			Path path = Paths.get(resourceUrl.toURI());

			
/*			newSetGainValue = DeviceDataManagerController.getLscsCalibrationConfigParsedKey().getVoltageCalibration().get(0).getVoltageTap().get(0).getCalibPoints().get(1).getGainValue();
			newSetOffsetValue = DeviceDataManagerController.getLscsCalibrationConfigParsedKey().getVoltageCalibration().get(0).getVoltageTap().get(0).getCalibPoints().get(1).getOffsetValue();
			ApplicationLauncher.logger.debug("calculateNewVoltageGainOffset: newSetGainValue7 : " + newSetGainValue);
			ApplicationLauncher.logger.debug("calculateNewVoltageGainOffset: newSetOffsetValue7 : " + newSetOffsetValue);
			
			newSetGainValue = lscsCalibrationConfigFileUpdatedKeyValue.getVoltageCalibration().get(0).getVoltageTap().get(0).getCalibPoints().get(1).getGainValue();
			newSetOffsetValue = lscsCalibrationConfigFileUpdatedKeyValue.getVoltageCalibration().get(0).getVoltageTap().get(0).getCalibPoints().get(1).getOffsetValue();
			ApplicationLauncher.logger.debug("calculateNewVoltageGainOffset: newSetGainValue8 : " + newSetGainValue);
			ApplicationLauncher.logger.debug("calculateNewVoltageGainOffset: newSetOffsetValue8 : " + newSetOffsetValue);
*/
			out = new BufferedOutputStream( Files.newOutputStream(path )); 
			bw = new BufferedWriter(new OutputStreamWriter(out)) ;//{



			//writer = new FileWriter("c:\\test_output.json");
			try {
				//lscsCalibrationConfigFileUpdatedKeyValue = DeviceDataManagerController.getLscsCalibrationConfigParsedKey();
/*				newSetGainValue = DeviceDataManagerController.getLscsCalibrationConfigParsedKey().getVoltageCalibration().get(0).getVoltageTap().get(0).getCalibPoints().get(1).getGainValue();
				newSetOffsetValue = DeviceDataManagerController.getLscsCalibrationConfigParsedKey().getVoltageCalibration().get(0).getVoltageTap().get(0).getCalibPoints().get(1).getOffsetValue();
				ApplicationLauncher.logger.debug("calculateNewVoltageGainOffset: newSetGainValue9 : " + newSetGainValue);
				ApplicationLauncher.logger.debug("calculateNewVoltageGainOffset: newSetOffsetValue9 : " + newSetOffsetValue);
				
				newSetGainValue = lscsCalibrationConfigFileUpdatedKeyValue.getVoltageCalibration().get(0).getVoltageTap().get(0).getCalibPoints().get(1).getGainValue();
				newSetOffsetValue = lscsCalibrationConfigFileUpdatedKeyValue.getVoltageCalibration().get(0).getVoltageTap().get(0).getCalibPoints().get(1).getOffsetValue();
				ApplicationLauncher.logger.debug("calculateNewVoltageGainOffset: newSetGainValue10 : " + newSetGainValue);
				ApplicationLauncher.logger.debug("calculateNewVoltageGainOffset: newSetOffsetValue10 : " + newSetOffsetValue);*/
				
				String indentedJsonCalibConfigFileData = gson.toJson(lscsCalibrationConfigFileUpdatedKeyValue);
				bw.write(indentedJsonCalibConfigFileData);
				bw.close();
				//writer.write(gson.toJson(lscsCalibrationConfigFileUpdatedKeyValue));
				//writer.close(); 
			} catch (IOException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.info("saveJsonFile: IOException: " + e.getMessage());
			} 
		} catch (IOException e1) {
			
			e1.printStackTrace();
			ApplicationLauncher.logger.info("saveJsonFile: IOException2: " + e1.getMessage());
		} catch (URISyntaxException e1) {
			
			e1.printStackTrace();
			ApplicationLauncher.logger.info("saveJsonFile: URISyntaxException: " + e1.getMessage());
		}   


	}

	public void initGuiObjects(){

		lscsCalibrationConfigFileUpdatedKeyValue = DeviceDataManagerController.getLscsCalibrationConfigParsedKey();
		Platform.runLater(() -> {

			initInProgress  = true;

			ref_rdBtnCurrentRphaseOnly.setSelected(false);
			ref_rdBtnCurrentYphaseOnly.setSelected(false);
			ref_rdBtnCurrentBphaseOnly.setSelected(false);
			ref_rdBtnCurrentAllPhase.setSelected(true);
			ref_chkBxCurrentSyncSelection.setSelected(true);

			ref_rdBtnVoltageRphaseOnly.setSelected(false);
			ref_rdBtnVoltageYphaseOnly.setSelected(false);
			ref_rdBtnVoltageBphaseOnly.setSelected(false);
			ref_rdBtnVoltageAllPhase.setSelected(true);
			ref_chkBxVoltageSyncSelection.setSelected(true);


			//ref_cmBxVoltPhaseSelected.getItems().clear();

			ref_cmBxVoltageRphaseSelected.getItems().clear();
			ref_cmBxVoltageYphaseSelected.getItems().clear();
			ref_cmBxVoltageBphaseSelected.getItems().clear();

			ref_cmBxCurrentRphaseSelected.getItems().clear();
			ref_cmBxCurrentYphaseSelected.getItems().clear();
			ref_cmBxCurrentBphaseSelected.getItems().clear();

			/*			ArrayList<String> phaseSelectionList = new ArrayList<String>();
			phaseSelectionList.add(ConstantApp.FIRST_PHASE_DISPLAY_NAME);
			phaseSelectionList.add(ConstantApp.SECOND_PHASE_DISPLAY_NAME);
			phaseSelectionList.add(ConstantApp.THIRD_PHASE_DISPLAY_NAME);
			ref_cmBxVoltPhaseSelected.getItems().addAll(phaseSelectionList);*/
			//ref_cmBxVoltPhaseSelected.getSelectionModel().select(0);

			disableStopButton();
			//ref_titledPaneVoltCalib.setExpanded(true);

			ArrayList<String> currentPhaseSelectionList = new ArrayList<String>();
			currentPhaseSelectionList.add(ConstantApp.FIRST_PHASE_DISPLAY_NAME);
			ref_cmBxVoltageRphaseSelected.getItems().addAll(currentPhaseSelectionList);
			ref_cmBxCurrentRphaseSelected.getItems().addAll(currentPhaseSelectionList);
			currentPhaseSelectionList.clear();
			currentPhaseSelectionList.add(ConstantApp.SECOND_PHASE_DISPLAY_NAME);
			ref_cmBxVoltageYphaseSelected.getItems().addAll(currentPhaseSelectionList);
			ref_cmBxCurrentYphaseSelected.getItems().addAll(currentPhaseSelectionList);
			currentPhaseSelectionList.clear();
			currentPhaseSelectionList.add(ConstantApp.THIRD_PHASE_DISPLAY_NAME);
			ref_cmBxVoltageBphaseSelected.getItems().addAll(currentPhaseSelectionList);
			ref_cmBxCurrentBphaseSelected.getItems().addAll(currentPhaseSelectionList);

			initInProgress = false;


		});
	}


	public void refAssignment(){

		//ref_cmBxVoltageTap1 = cmBxVoltageTap1;
		ref_btnComStart = btnComStart;
		ref_btnComStop = btnComStop;
		ref_titledPaneVoltCalib = titledPaneVoltCalib;
		ref_titledPaneCurrentCalib = titledPaneCurrentCalib;
		//ref_txtVoltTap1Rms = txtVoltTap1Rms;


		ref_rdBtnCurrentRphaseOnly = rdBtnCurrentRphaseOnly;
		ref_rdBtnCurrentYphaseOnly = rdBtnCurrentYphaseOnly;
		ref_rdBtnCurrentBphaseOnly = rdBtnCurrentBphaseOnly;
		ref_rdBtnCurrentAllPhase = rdBtnCurrentAllPhase;

		ref_rdBtnVoltageRphaseOnly = rdBtnVoltageRphaseOnly;
		ref_rdBtnVoltageYphaseOnly = rdBtnVoltageYphaseOnly;
		ref_rdBtnVoltageBphaseOnly = rdBtnVoltageBphaseOnly;
		ref_rdBtnVoltageAllPhase = rdBtnVoltageAllPhase;
		
		ref_txtAreaJsonFileDisplay = txtAreaJsonFileDisplay;


		/*		ref_cmBxVoltPhaseSelected = cmBxVoltPhaseSelected;		
		ref_txtVoltNoOfTapping = txtVoltNoOfTapping;
		ref_cmBxVoltTapMaxList = cmBxVoltTapMaxList;
		ref_txtVoltNoOfCalibPointEachTap = txtVoltNoOfCalibPointEachTap;
		ref_cmBxVoltCalibPointNameList = cmBxVoltCalibPointNameList;

		ref_txtVoltTapSelectionRelayCode = txtVoltTapSelectionRelayCode;
		ref_txtVoltCalibPointValue = txtVoltCalibPointValue;
		ref_txtVoltPresentRmsValue = txtVoltPresentRmsValue;
		ref_txtVoltUpdatedInRefStd = txtVoltUpdatedInRefStd;
		ref_txtVoltNewCalcRmsValue = txtVoltNewCalcRmsValue;
		ref_txtVoltPresentGainValue = txtVoltPresentGainValue;
		ref_txtVoltPresentOffsetValue = txtVoltPresentOffsetValue;
		ref_txtVoltNewCalcGainValue = txtVoltNewCalcGainValue;
		ref_txtVoltNewCalcOffsetValue = txtVoltNewCalcOffsetValue;*/


		ref_cmBxCurrentRphaseSelected = cmBxCurrentRphaseSelected;		
		ref_txtCurrentRphaseNoOfTapping = txtCurrentRphaseNoOfTapping;
		ref_cmBxCurrentRphaseTapMaxList = cmBxCurrentRphaseTapMaxList;
		ref_txtCurrentRphaseNoOfCalibPointEachTap = txtCurrentRphaseNoOfCalibPointEachTap;
		ref_cmBxCurrentRphaseCalibPointNameList = cmBxCurrentRphaseCalibPointNameList;
		ref_txtCurrentRphaseTapSelectionRelayCode = txtCurrentRphaseTapSelectionRelayCode;
		ref_txtCurrentRphaseCalibPointValue = txtCurrentRphaseCalibPointValue;
		ref_txtCurrentRphasePresentRmsValue = txtCurrentRphasePresentRmsValue;
		ref_txtCurrentRphaseUpdatedInRefStd = txtCurrentRphaseUpdatedInRefStd;
		ref_txtCurrentRphaseNewCalcRmsValue = txtCurrentRphaseNewCalcRmsValue;
		ref_txtCurrentRphasePresentGainValue = txtCurrentRphasePresentGainValue;
		ref_txtCurrentRphasePresentOffsetValue = txtCurrentRphasePresentOffsetValue;
		ref_txtCurrentRphaseNewCalcGainValue = txtCurrentRphaseNewCalcGainValue;
		ref_txtCurrentRphaseNewCalcOffsetValue = txtCurrentRphaseNewCalcOffsetValue;



		ref_cmBxCurrentYphaseSelected = cmBxCurrentYphaseSelected;		
		ref_txtCurrentYphaseNoOfTapping = txtCurrentYphaseNoOfTapping;
		ref_cmBxCurrentYphaseTapMaxList = cmBxCurrentYphaseTapMaxList;
		ref_txtCurrentYphaseNoOfCalibPointEachTap = txtCurrentYphaseNoOfCalibPointEachTap;
		ref_cmBxCurrentYphaseCalibPointNameList = cmBxCurrentYphaseCalibPointNameList;
		ref_txtCurrentYphaseTapSelectionRelayCode = txtCurrentYphaseTapSelectionRelayCode;
		ref_txtCurrentYphaseCalibPointValue = txtCurrentYphaseCalibPointValue;
		ref_txtCurrentYphasePresentRmsValue = txtCurrentYphasePresentRmsValue;
		ref_txtCurrentYphaseUpdatedInRefStd = txtCurrentYphaseUpdatedInRefStd;
		ref_txtCurrentYphaseNewCalcRmsValue = txtCurrentYphaseNewCalcRmsValue;
		ref_txtCurrentYphasePresentGainValue = txtCurrentYphasePresentGainValue;
		ref_txtCurrentYphasePresentOffsetValue = txtCurrentYphasePresentOffsetValue;
		ref_txtCurrentYphaseNewCalcGainValue = txtCurrentYphaseNewCalcGainValue;
		ref_txtCurrentYphaseNewCalcOffsetValue = txtCurrentYphaseNewCalcOffsetValue;	



		ref_cmBxCurrentBphaseSelected = cmBxCurrentBphaseSelected;		
		ref_txtCurrentBphaseNoOfTapping = txtCurrentBphaseNoOfTapping;
		ref_cmBxCurrentBphaseTapMaxList = cmBxCurrentBphaseTapMaxList;
		ref_txtCurrentBphaseNoOfCalibPointEachTap = txtCurrentBphaseNoOfCalibPointEachTap;
		ref_cmBxCurrentBphaseCalibPointNameList = cmBxCurrentBphaseCalibPointNameList;
		ref_txtCurrentBphaseTapSelectionRelayCode = txtCurrentBphaseTapSelectionRelayCode;
		ref_txtCurrentBphaseCalibPointValue = txtCurrentBphaseCalibPointValue;
		ref_txtCurrentBphasePresentRmsValue = txtCurrentBphasePresentRmsValue;
		ref_txtCurrentBphaseUpdatedInRefStd = txtCurrentBphaseUpdatedInRefStd;
		ref_txtCurrentBphaseNewCalcRmsValue = txtCurrentBphaseNewCalcRmsValue;
		ref_txtCurrentBphasePresentGainValue = txtCurrentBphasePresentGainValue;
		ref_txtCurrentBphasePresentOffsetValue = txtCurrentBphasePresentOffsetValue;
		ref_txtCurrentBphaseNewCalcGainValue = txtCurrentBphaseNewCalcGainValue;
		ref_txtCurrentBphaseNewCalcOffsetValue = txtCurrentBphaseNewCalcOffsetValue;



		ref_cmBxVoltageRphaseSelected = cmBxVoltageRphaseSelected;		
		ref_txtVoltageRphaseNoOfTapping = txtVoltageRphaseNoOfTapping;
		ref_cmBxVoltageRphaseTapMaxList = cmBxVoltageRphaseTapMaxList;
		ref_txtVoltageRphaseNoOfCalibPointEachTap = txtVoltageRphaseNoOfCalibPointEachTap;
		ref_cmBxVoltageRphaseCalibPointNameList = cmBxVoltageRphaseCalibPointNameList;
		ref_txtVoltageRphaseTapSelectionRelayCode = txtVoltageRphaseTapSelectionRelayCode;
		ref_txtVoltageRphaseCalibPointValue = txtVoltageRphaseCalibPointValue;
		ref_txtVoltageRphasePresentRmsValue = txtVoltageRphasePresentRmsValue;
		ref_txtVoltageRphaseUpdatedInRefStd = txtVoltageRphaseUpdatedInRefStd;
		ref_txtVoltageRphaseNewCalcRmsValue = txtVoltageRphaseNewCalcRmsValue;
		ref_txtVoltageRphasePresentGainValue = txtVoltageRphasePresentGainValue;
		ref_txtVoltageRphasePresentOffsetValue = txtVoltageRphasePresentOffsetValue;
		ref_txtVoltageRphaseNewCalcGainValue = txtVoltageRphaseNewCalcGainValue;
		ref_txtVoltageRphaseNewCalcOffsetValue = txtVoltageRphaseNewCalcOffsetValue;



		ref_cmBxVoltageYphaseSelected = cmBxVoltageYphaseSelected;		
		ref_txtVoltageYphaseNoOfTapping = txtVoltageYphaseNoOfTapping;
		ref_cmBxVoltageYphaseTapMaxList = cmBxVoltageYphaseTapMaxList;
		ref_txtVoltageYphaseNoOfCalibPointEachTap = txtVoltageYphaseNoOfCalibPointEachTap;
		ref_cmBxVoltageYphaseCalibPointNameList = cmBxVoltageYphaseCalibPointNameList;
		ref_txtVoltageYphaseTapSelectionRelayCode = txtVoltageYphaseTapSelectionRelayCode;
		ref_txtVoltageYphaseCalibPointValue = txtVoltageYphaseCalibPointValue;
		ref_txtVoltageYphasePresentRmsValue = txtVoltageYphasePresentRmsValue;
		ref_txtVoltageYphaseUpdatedInRefStd = txtVoltageYphaseUpdatedInRefStd;
		ref_txtVoltageYphaseNewCalcRmsValue = txtVoltageYphaseNewCalcRmsValue;
		ref_txtVoltageYphasePresentGainValue = txtVoltageYphasePresentGainValue;
		ref_txtVoltageYphasePresentOffsetValue = txtVoltageYphasePresentOffsetValue;
		ref_txtVoltageYphaseNewCalcGainValue = txtVoltageYphaseNewCalcGainValue;
		ref_txtVoltageYphaseNewCalcOffsetValue = txtVoltageYphaseNewCalcOffsetValue;	



		ref_cmBxVoltageBphaseSelected = cmBxVoltageBphaseSelected;		
		ref_txtVoltageBphaseNoOfTapping = txtVoltageBphaseNoOfTapping;
		ref_cmBxVoltageBphaseTapMaxList = cmBxVoltageBphaseTapMaxList;
		ref_txtVoltageBphaseNoOfCalibPointEachTap = txtVoltageBphaseNoOfCalibPointEachTap;
		ref_cmBxVoltageBphaseCalibPointNameList = cmBxVoltageBphaseCalibPointNameList;
		ref_txtVoltageBphaseTapSelectionRelayCode = txtVoltageBphaseTapSelectionRelayCode;
		ref_txtVoltageBphaseCalibPointValue = txtVoltageBphaseCalibPointValue;
		ref_txtVoltageBphasePresentRmsValue = txtVoltageBphasePresentRmsValue;
		ref_txtVoltageBphaseUpdatedInRefStd = txtVoltageBphaseUpdatedInRefStd;
		ref_txtVoltageBphaseNewCalcRmsValue = txtVoltageBphaseNewCalcRmsValue;
		ref_txtVoltageBphasePresentGainValue = txtVoltageBphasePresentGainValue;
		ref_txtVoltageBphasePresentOffsetValue = txtVoltageBphasePresentOffsetValue;
		ref_txtVoltageBphaseNewCalcGainValue = txtVoltageBphaseNewCalcGainValue;
		ref_txtVoltageBphaseNewCalcOffsetValue = txtVoltageBphaseNewCalcOffsetValue;

		ref_chkBxCurrentSyncSelection = chkBxCurrentSyncSelection;
		ref_chkBxVoltageSyncSelection = chkBxVoltageSyncSelection;


	}

	@FXML
	public void StartRunTrigger() {
		ApplicationLauncher.logger.info("StartRunTrigger: Entry");
		startRunTimer = new Timer();
		startRunTimer.schedule(new StartRunTaskClick(), 50);

	}

	class StartRunTaskClick extends TimerTask {
		public void run() {
			execution_start_on_click();
			startRunTimer.cancel();


		}
	}

	public void execution_start_on_click(){
		ApplicationLauncher.logger.info("execution_start_on_click: Entry");

		try {
			StartTestExecution();
		} catch (JSONException  e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("execution_start_on_click :JSONException:"+ e.getMessage());
		}catch (ParseException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("execution_start_on_click :ParseException:"+ e.getMessage());
		}
	}

	public void StartTestExecution() throws JSONException, ParseException{
		ApplicationLauncher.logger.info("LscsCalib : StartTestExecution: Entry");
		ApplicationHomeController.updateBottomSecondaryStatus("",ConstantApp.LEFT_STATUS_INFO);
		//DisableTestRunScreenButton();
		ScanForSerialPorts();
		if(DisplayDataObj.validateLscsSourceComPortAccessible()){
			
			//			setExecutionInProgress(true);
			//DisableTestRunScreenButton();
			boolean status = validateLscsSourcePortCommand();
			if(status){
				status = refStdValidateSerialPortCommand();
				enableStopButton();
			}
			//enableStopButton();

			/*
			setUserAbortedFlag(false);
			SetActiveReactivePulseConstant();
			ApplicationLauncher.logger.debug("StartTestExecution: get_EM_Model_type:" + DisplayDataObj.getDeployedEM_ModelType());

			ApplicationHomeController.DisableLeftMenuButtonsForTestRun();
			setPowerSrcContinuousFailureStatus(false);
			setRefStdFeedBackContinuousFailureStatus(false);
			resetRefStdFeedBackContinuousFailureCounter();
			setProcessTC_ExecutionRefreshTimeInMSec(ProcessTC_ExecutionRefreshTimeInMSec);
			SerialDM_Obj.ClearStdRefSerialData();
			SerialDataRadiantRefStd.ClearRefStd_ReadSerialData();
			String project_name = getCurrentProjectName();//MeterParamsController.getCurrentProjectName();
			String deploymentId = getSelectedDeployment_ID();
			if(getStepRunFlag()){

				getSelectedTestPoint(project_name,deploymentId);
				setOneTimeExecuted(false);
			}
			else if(getResumeFlag()){
				setSelectedTestPointAsStartIndex();  
			}
			else{
				setCurrentTestPoint_Index(0);
			}
			setListOfDevices();

			setAll_TestPoint_CompletedStatus(false);
			setCurrentProject_TotalNoOfTestPoint(CurrentProjectTestPoint_List.length());
			setProjectBarMax();
			ResetRefStdProgressCounter();
			resetPowerSrcContinuousFailureCounter();
			resetRefStdFeedBackContinuousFailureCounter();
			setCurrentTestPoint_ExecutionCompletedStatus(true);
			CheckForInitAndProcessAllTestCaseTrigger();*/

			ApplicationLauncher.logger.info("StartTestExecution: exit");
		}else {
			ApplicationLauncher.logger.info("StartTestExecution: EnableTestRunScreenButton");
			//EnableTestRunScreenButton();

		}
	}
	
	public boolean refStdValidateSerialPortCommand(){
		ApplicationLauncher.logger.info("refStdValidateSerialPortCommand: Entry");

		boolean status = false;
		try{
			if (ProcalFeatureEnable.REFSTD_CONNECTED_NONE){
				status = true;
			}else if(ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED){
				status = SerialDM_Obj.RefStd_ValidateNOP_CMD();
			}else if(ProcalFeatureEnable.SANDS_REFSTD_CONNECTED){
				status = SerialDM_Obj.sandsRefStdGetConfigTask();
				if(status){

					DeviceDataManagerController.setSandsRefStdLastSetVoltageMode(Data_RefStdSands.getReadConfigModeVoltage());
					DeviceDataManagerController.setSandsRefStdLastSetCurrentMode(Data_RefStdSands.getReadConfigModeCurrent());
					DeviceDataManagerController.setSandsRefStdLastSetPulseOutputMode(Data_RefStdSands.getReadConfigModePulseOutputUnit());

				}
			}else if(ProcalFeatureEnable.KRE_REFSTD_CONNECTED){
				//status = SerialDM_Obj.kreRefStdReadSettingTask();
				Data_RefStdKre.setWriteSettingIstall(ConstantRefStdKre.CURRENT_MAX);
				if(ProcalFeatureEnable.REFSTD_PORT_MANAGER_V2_ENABLED) {
					//displayDataObj.refStdEnableSerialMonitoring();
					//DisplayDataObj.refStdEnableSerialMonitoring_V2();

					RefStdDirector refStdKreDirector = new RefStdDirector();
					status = refStdKreDirector.refStdWriteSettingTask();
					ApplicationLauncher.logger.debug("refStdValidateSerialPortCommand : status: " + status);
					if(status){
						SerialDataManager.setRefComSerialStatusConnected(true);
					}
				}else {
					status = SerialDM_Obj.kreRefStdWriteSettingTask();
				}


			}else if(ProcalFeatureEnable.KIGGS_REFSTD_CONNECTED){ // #KIGGSREFSTD
				//status = SerialDM_Obj.kreRefStdReadSettingTask();
				//Data_RefStdKre.setWriteSettingIstall(ConstantRefStdKre.CURRENT_MAX);
				//status = SerialDM_Obj.kreRefStdWriteSettingTask();
				status = SerialDM_Obj.kiggsReadRefStdVoltAndCurrentTapRange() ;
				//status = true;

			}else if (ProcalFeatureEnable.BOFA_REFSTD_CONNECTED){
				status = true;
			}
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("refStdValidateSerialPortCommand : UnsupportedEncodingException: " + e.getMessage());
			//setExecutionInProgress(false);
		}
		ApplicationLauncher.logger.info("refStdValidateSerialPortCommand: Exit");

		return status;

	}
	
/*	public boolean validateRefStdPortCommand(){
		ApplicationLauncher.logger.debug("validateRefStdPortCommand: Invoked");
		boolean status = false;
		gvnbvb
		return status;
		
	}*/

	public boolean validateLscsSourcePortCommand(){
		ApplicationLauncher.logger.info("ValidateLscsSourcePortCommand: Invoked");
		boolean status = false;
		try {
			/*			if(ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED){
			status = SerialDM_Obj.RefStd_ValidateNOP_CMD();
			}else if(ProcalFeatureEnable.SANDS_REFSTD_CONNECTED){

			}*/
			//if (status){
			ApplicationLauncher.logger.info("ValidateLscsSourcePortCommand : RefStd Serial Port command validation: Success");
			if(!SerialDataManager.isPowerSourceTurnedOff()){
				status = SerialDM_Obj.SetPowerSourceOff();
			}else{
				status = true;
			}
			if (status){
				/*					DisplayDataObj.set_Error_min("-1.00");
					DisplayDataObj.set_Error_max("+1.00");
					DisplayDataObj.setDutImpulsesPerUnit("2500");
					DisplayDataObj.setNoOfPulses("10");
					ApplicationLauncher.logger.info("ValidateLscsSourcePortCommand : Power Source Serial Port command validation: Success");
					DevicePortSetupController.setPortValidationTurnedON(true);
					if(ProcalFeatureEnable.CCUBE_LDU_CONNECTED) {
						status = SerialDM_Obj.LDU_ResetSetting();
					}else if(ProcalFeatureEnable.LSCS_LDU_CONNECTED){
						status = SerialDM_Obj.lscsLDU_CheckCom();
					}else if(ProcalFeatureEnable.MTE_REFSTD_CONNECTED){
						mteStartRefStdReading();
						//Sleep(5000);
						//status = powerUpDeviceUnderTest();
						//status= true;
					}
					DevicePortSetupController.setPortValidationTurnedON(false);
					if (status){
						ApplicationLauncher.logger.info("ValidateLscsSourcePortCommand : LDU Serial Port command validation: Success");
						ApplicationLauncher.logger.info("ValidateLscsSourcePortCommand : All Serial Port Command Validation: Success");
					}else{
						SerialDM_Obj.DisconnectLDU_SerialComm();
						ApplicationLauncher.logger.info("ValidateLscsSourcePortCommand : LDU Serial Port command validation: Failed");
						MessageDisplay("Serial Port Command failed for LDU Serial Port.",AlertType.ERROR);

					}*/
			}else{
				SerialDM_Obj.DisconnectPwrSrc();
				ApplicationLauncher.logger.info("ValidateLscsSourcePortCommand : Power Source Serial Port command validation: Failed");
				ApplicationLauncher.InformUser("Source SerialPort failed","Serial Port Command failed for Power Source Serial Port.",AlertType.ERROR);

				//setExecutionInProgress(false);
			}
			/*			}else{
				SerialDM_Obj.DisconnectRefSerialComm();
				ApplicationLauncher.logger.info("ValidateLscsSourcePortCommand : RefStd Serial Port command validation: Failed");
				MessageDisplay("Serial Port Command failed for Reference Standard Serial Port.",AlertType.ERROR);
				setExecutionInProgress(false);
			}*/
		} catch (Exception e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("ValidateLscsSourcePortCommand : Exception: " + e.getMessage());
			//setExecutionInProgress(false);
		}

		return status;	
	}

	public void enableStopButton(){
		Platform.runLater(() -> {
			ref_btnComStop.setDisable(false);
		});
	}

	public void disableStopButton(){
		Platform.runLater(() -> {
			ref_btnComStop.setDisable(true);
		});
	}

	public void enableStartButton(){
		Platform.runLater(() -> {
			ref_btnComStart.setDisable(false);
		});
	}

	public void disableStartButton(){
		Platform.runLater(() -> {
			ref_btnComStart.setDisable(true);
		});
	}

	public void ScanForSerialPorts(){
		ApplicationLauncher.logger.info("ScanForSerialPorts: Entry");
		ApplicationHomeController.update_left_status("Scanning serial ports",ConstantApp.LEFT_STATUS_DEBUG);
		SerialDM_Obj.ScanForSerialCommPort(); 
	}

	@FXML 
	public void sendVoltRmsToSourceClick(){
		sendVoltRmsToSourceTrigger();
	}

	public void sendVoltRmsToSourceTrigger(){
		ApplicationLauncher.logger.info("sendVoltRmsToSourceTrigger : Entry");
		sendDataTimer = new Timer();
		sendDataTimer.schedule(new sendVoltRmsToSourceTask(), 100);
	}

	class sendVoltRmsToSourceTask extends TimerTask{


		@Override
		public void run() {
			Platform.runLater(() -> {
				/*				status=stop_confirmation();
				if(status){*/
				sendVoltRmsToSource();
				//}
				sendDataTimer.cancel();
			});
		}

	}

	public void sendVoltRmsToSource(){

		/*		String voltRmsValue = ref_txtVoltTap1Rms.getText();
		String selectedPhase =	ref_cmBxVoltPhaseSelected.getSelectionModel().getSelectedItem().toString();//ConstantApp.FIRST_PHASE_DISPLAY_NAME;
		String selectedVoltTap = ref_txtVoltTap1Rms.getText();
		SerialDM_Obj.lscsCalibModeSetSourceVoltage(selectedPhase, voltRmsValue, selectedVoltTap);*/

	}

	@FXML
	public void StopOnClick(){

		Trigger_Stop_Confirmation();
	}

	public void Trigger_Stop_Confirmation() {
		ApplicationLauncher.logger.info("Trigger_Stop_Confirmation : Entry");
		TCStopConfirmationTimer = new Timer();
		TCStopConfirmationTimer.schedule(new ExecuteStopConfirmationTC(), 100);
	}

	class ExecuteStopConfirmationTC extends TimerTask{


		@Override
		public void run() {
			Platform.runLater(() -> {
				/*				status=stop_confirmation();
				if(status){*/
				StopOnClickSuccess();
				//}
				TCStopConfirmationTimer.cancel();
			});
		}

	}

	public void StopOnClickSuccess(){
		ApplicationLauncher.logger.info("StopOnClickSuccess : Entry - User Aborted");
		//setUserAbortedFlag(true);
		/*		if(!getStepRunFlag()){
			updateAbortedInDisplay();
		}*/
		ProjectExitProcess();

	}

	public void ProjectExitProcess(){
		ApplicationLauncher.logger.info("ProjectExitProcess : Entry");
		//SaveProjectRunEndTime();
		disableStopButton();
		DisplayShutDownAllCompPortsTrigger();

	}


	public void DisplayShutDownAllCompPortsTrigger(){

		ApplicationLauncher.logger.info("DisplayShutDownAllCompPortsTrigger :Entry");
		ShutDownAllComPortTimer = new Timer();
		ShutDownAllComPortTimer.schedule(new ShutDownAllCompPorts(), 100);
	}

	class ShutDownAllCompPorts extends TimerTask{


		@Override
		public void run() {

			ApplicationLauncher.logger.debug("ShutDownAllCompPorts: setUI_TableRefreshFlag- 1: false");
			/*			setUI_TableRefreshFlag(false);
			DisplayDataObj.setLDU_ReadDataFlag(false);
			DisplayDataObj.setRefStdReadDataFlag(false);
			cancelProcessAllTestCasesTimer();*/
			ApplicationLauncher.logger.debug("ShutDownAllCompPorts: setUI_TableRefreshFlag- 2: false");
			//setUI_TableRefreshFlag(false);
			//When user stops aborted
			/*			if (SerialDM_Obj.getPowerSrcTurnedOnStatus()|| SerialDM_Obj.getPowerSrcOnFlag() || 
					getPowerSrcContinuousFailureStatus()|| getRefStdFeedBackContinuousFailureStatus()||
					getExecuteStopStatus()){
				SerialDM_Obj.SetPowerSourceOff();

				if(getExecuteStopStatus()){
					Integer StatusWaitTimeCounter = 40;
					ApplicationLauncher.logger.info("ShutDownAllCompPorts : ExecuteStop Await - "+StatusWaitTimeCounter+" Sec to complete");
					ApplicationHomeController.update_left_status("ExecuteStop Await - "+StatusWaitTimeCounter+" Sec",ConstantApp.LEFT_STATUS_DEBUG);
					while (StatusWaitTimeCounter !=0 && getExecuteStopStatus()){
						StatusWaitTimeCounter--;
						Sleep(1000);
						ApplicationLauncher.logger.info("ShutDownAllCompPorts : awaiting ExecuteStop StatusWaitTimeCounter:"+StatusWaitTimeCounter);
					}
					ApplicationLauncher.logger.info("ShutDownAllCompPorts :ExecuteStop Await: Sleeping for 10 sec");
					ApplicationHomeController.update_left_status("ExecuteStop Await: sleep 10 sec...",ConstantApp.LEFT_STATUS_DEBUG);
					Sleep(10000);
					ApplicationLauncher.logger.info("ShutDownAllCompPorts :ExecuteStop Await: Sleeping awake after 10 sec");
					ApplicationHomeController.update_left_status("ExecuteStop Await: sleep 10 sec awake",ConstantApp.LEFT_STATUS_DEBUG);
				}
				ApplicationLauncher.logger.info("ShutDownAllCompPorts :PwrSrcOFF_Trigger: Sleeping for 10 sec");
				ApplicationHomeController.update_left_status("PwrSrcOFF_Trigger: sleep 10 sec...",ConstantApp.LEFT_STATUS_DEBUG);

				if (ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){
					SerialDM_Obj.PwrSrcOFF_Trigger();

					Sleep(10000);
					ApplicationLauncher.logger.info("ShutDownAllCompPorts :PwrSrcOFF_Trigger: Sleeping awake after 10 sec");
					ApplicationHomeController.update_left_status("PwrSrcOFF_Trigger: sleep 10 sec awake",ConstantApp.LEFT_STATUS_DEBUG);

				}else 
			 */
			if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
				ProjectExecutionController.setUserAbortedFlag(false);
				ApplicationLauncher.logger.info("ShutDownAllCompPorts :PwrSrcOFF_Trigger: awaiting PowerSrcOff");
				ApplicationHomeController.update_left_status("PwrSrcOFF_Trigger: awaiting PowerSrcOff",ConstantApp.LEFT_STATUS_DEBUG);

				boolean offStatus = true;
				if(!SerialDataManager.isPowerSourceTurnedOff()){
					offStatus = SerialDM_Obj.SetPowerSourceOff();
				}
				if(offStatus) {
					SerialDataManager.setPowerSrcTurnedOnStatus(false);
					ApplicationLauncher.logger.info("ShutDownAllCompPorts : MCT/NCT mode reset initiated");
					ApplicationHomeController.update_left_status("MCT/NCT: mode Off",ConstantApp.LEFT_STATUS_DEBUG);
					//SerialDM_Obj.lscsSetPowerSourceMctNctMode(ConstantReport.RESULT_EXECUTION_MODE_MCT_NCT_OFF);
					//ProjectExecutionController.setUserAbortedFlag(true);
					//ApplicationLauncher.logger.info("ShutDownAllCompPorts : MCT/NCT mode reset done");
				}
			}
			

			/*			}*/
			/*			if(getCurrentTestType().equals(TestProfileType.ConstantTest.toString())){

				ApplicationLauncher.logger.info("ShutDownAllCompPorts :Const Test buffer time: Sleeping for 30 sec");
				ApplicationHomeController.update_left_status("Const Buffer Time sleep 30 sec...",ConstantApp.LEFT_STATUS_DEBUG);

				Sleep(30000);
				ApplicationHomeController.update_left_status("Const Buffer Time sleep 30 sec awake",ConstantApp.LEFT_STATUS_DEBUG);

				ApplicationLauncher.logger.info("ShutDownAllCompPorts  :Const Test buffer time::Sleeping awake after 30 sec");
			}
			Sleep(5000);
			DisplayDataObj.setVoltageResetRequired(true);
			setExecuteTimeCounter(0);
			Sleep(15000);*/

			CloseAllComPort();
			enableStartButton();
			/*			CancelExecuteTimeLine();
			if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
				DisplayInstantMetricsController.ResetInstantMetrics();
				resetInstantMetrics();
			}
			ApplicationHomeController.update_left_status("ShutDownAllCompPorts: sleep 30 sec",ConstantApp.LEFT_STATUS_DEBUG);
			ApplicationLauncher.logger.info("ShutDownAllCompPorts :Sleeping for CoolOffTimeInMSec:"+CoolOffTimeInMSec);
			Sleep(CoolOffTimeInMSec);
			ApplicationHomeController.update_left_status("ShutDownAllCompPorts: sleep 30 sec awake",ConstantApp.LEFT_STATUS_DEBUG);
			ApplicationLauncher.logger.info("ShutDownAllCompPorts :Sleeping awake after CoolOffTimeInMSec:"+CoolOffTimeInMSec);
			ShutDownAllComPortTimer.cancel();
			try{
				EnableTestRunScreenButton();
			}catch(Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("ShutDownAllCompPorts : Exception:"+e.getMessage());
			}
			ApplicationHomeController.update_left_status("Live status",ConstantApp.LEFT_STATUS_DEBUG);
			setExecutionInProgress(false);*/
			ApplicationLauncher.logger.info("ShutDownAllCompPorts : Exit:");
		}

	}

	public void  DisplayDisconnectPwrSrc (){
		SerialDM_Obj.DisconnectPwrSrc();
	}

	public boolean  CloseAllComPort(){
		boolean status = false;

		if(DeviceDataManagerController.getAllPortInitSuccess()){

			//DisplayDisconnectRefStd();
			//DisplayDisconnectLDU();
			DisplayDisconnectPwrSrc();
			ApplicationLauncher.logger.info("CloseAllComPort: Closed all serial port");
		}
		/*		
		try{
			ExecuteTimeLineObj.stop();
		}
		catch(Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("CloseAllComPort: Exception: " + e.toString());
		}*/

		return status;
	}

	/*	public void clearVoltageGui(){

		ref_txtVoltNoOfTapping.clear();
		ref_cmBxVoltTapMaxList.getItems().clear();
		clearVoltageTapChangeGui();

	}

	public void clearVoltageTapChangeGui(){		

		ref_txtVoltTapSelectionRelayCode.clear();
		ref_txtVoltNoOfCalibPointEachTap.clear();
		ref_cmBxVoltCalibPointNameList.getItems().clear();
		clearVoltageCalibPointChangeGui();

	}

	public void clearVoltageCalibPointChangeGui(){		

		ref_txtVoltCalibPointValue.clear();
		ref_txtVoltPresentRmsValue.clear();
		ref_txtVoltPresentGainValue.clear();
		ref_txtVoltPresentOffsetValue.clear();
		ref_txtVoltUpdatedInRefStd.clear();
		ref_txtVoltNewCalcRmsValue.clear();
		ref_txtVoltNewCalcGainValue.clear();
		ref_txtVoltNewCalcOffsetValue.clear();
	}*/



	public void clearCurrentRphaseGui(){

		ref_txtCurrentRphaseNoOfTapping.clear();
		ref_cmBxCurrentRphaseTapMaxList.getItems().clear();
		clearCurrentRphaseTapChangeGui();

	}

	public void clearCurrentRphaseTapChangeGui(){		

		ref_txtCurrentRphaseTapSelectionRelayCode.clear();
		ref_txtCurrentRphaseNoOfCalibPointEachTap.clear();
		ref_cmBxCurrentRphaseCalibPointNameList.getItems().clear();
		clearCurrentRphaseCalibPointChangeGui();

	}

	public void clearCurrentRphaseCalibPointChangeGui(){		

		ref_txtCurrentRphaseCalibPointValue.clear();
		ref_txtCurrentRphasePresentRmsValue.clear();
		ref_txtCurrentRphasePresentGainValue.clear();
		ref_txtCurrentRphasePresentOffsetValue.clear();
		ref_txtCurrentRphaseUpdatedInRefStd.clear();
		ref_txtCurrentRphaseNewCalcRmsValue.clear();
		ref_txtCurrentRphaseNewCalcGainValue.clear();
		ref_txtCurrentRphaseNewCalcOffsetValue.clear();
	}


	public void clearCurrentYphaseGui(){

		ref_txtCurrentYphaseNoOfTapping.clear();
		ref_cmBxCurrentYphaseTapMaxList.getItems().clear();
		clearCurrentYphaseTapChangeGui();

	}

	public void clearCurrentYphaseTapChangeGui(){		

		ref_txtCurrentYphaseTapSelectionRelayCode.clear();
		ref_txtCurrentYphaseNoOfCalibPointEachTap.clear();
		ref_cmBxCurrentYphaseCalibPointNameList.getItems().clear();
		clearCurrentYphaseCalibPointChangeGui();

	}

	public void clearCurrentYphaseCalibPointChangeGui(){		

		ref_txtCurrentYphaseCalibPointValue.clear();
		ref_txtCurrentYphasePresentRmsValue.clear();
		ref_txtCurrentYphasePresentGainValue.clear();
		ref_txtCurrentYphasePresentOffsetValue.clear();
		ref_txtCurrentYphaseUpdatedInRefStd.clear();
		ref_txtCurrentYphaseNewCalcRmsValue.clear();
		ref_txtCurrentYphaseNewCalcGainValue.clear();
		ref_txtCurrentYphaseNewCalcOffsetValue.clear();
	}



	public void clearCurrentBphaseGui(){

		ref_txtCurrentBphaseNoOfTapping.clear();
		ref_cmBxCurrentBphaseTapMaxList.getItems().clear();
		clearCurrentBphaseTapChangeGui();

	}

	public void clearCurrentBphaseTapChangeGui(){		

		ref_txtCurrentBphaseTapSelectionRelayCode.clear();
		ref_txtCurrentBphaseNoOfCalibPointEachTap.clear();
		ref_cmBxCurrentBphaseCalibPointNameList.getItems().clear();
		clearCurrentBphaseCalibPointChangeGui();

	}

	public void clearCurrentBphaseCalibPointChangeGui(){		

		ref_txtCurrentBphaseCalibPointValue.clear();
		ref_txtCurrentBphasePresentRmsValue.clear();
		ref_txtCurrentBphasePresentGainValue.clear();
		ref_txtCurrentBphasePresentOffsetValue.clear();
		ref_txtCurrentBphaseUpdatedInRefStd.clear();
		ref_txtCurrentBphaseNewCalcRmsValue.clear();
		ref_txtCurrentBphaseNewCalcGainValue.clear();
		ref_txtCurrentBphaseNewCalcOffsetValue.clear();
	}




	public void clearVoltageRphaseGui(){

		ref_txtVoltageRphaseNoOfTapping.clear();
		ref_cmBxVoltageRphaseTapMaxList.getItems().clear();
		clearVoltageRphaseTapChangeGui();

	}

	public void clearVoltageRphaseTapChangeGui(){		

		ref_txtVoltageRphaseTapSelectionRelayCode.clear();
		ref_txtVoltageRphaseNoOfCalibPointEachTap.clear();
		ref_cmBxVoltageRphaseCalibPointNameList.getItems().clear();
		clearVoltageRphaseCalibPointChangeGui();

	}

	public void clearVoltageRphaseCalibPointChangeGui(){		

		ref_txtVoltageRphaseCalibPointValue.clear();
		ref_txtVoltageRphasePresentRmsValue.clear();
		ref_txtVoltageRphasePresentGainValue.clear();
		ref_txtVoltageRphasePresentOffsetValue.clear();
		ref_txtVoltageRphaseUpdatedInRefStd.clear();
		ref_txtVoltageRphaseNewCalcRmsValue.clear();
		ref_txtVoltageRphaseNewCalcGainValue.clear();
		ref_txtVoltageRphaseNewCalcOffsetValue.clear();
	}


	public void clearVoltageYphaseGui(){

		ref_txtVoltageYphaseNoOfTapping.clear();
		ref_cmBxVoltageYphaseTapMaxList.getItems().clear();
		clearVoltageYphaseTapChangeGui();

	}

	public void clearVoltageYphaseTapChangeGui(){		

		ref_txtVoltageYphaseTapSelectionRelayCode.clear();
		ref_txtVoltageYphaseNoOfCalibPointEachTap.clear();
		ref_cmBxVoltageYphaseCalibPointNameList.getItems().clear();
		clearVoltageYphaseCalibPointChangeGui();

	}

	public void clearVoltageYphaseCalibPointChangeGui(){		

		ref_txtVoltageYphaseCalibPointValue.clear();
		ref_txtVoltageYphasePresentRmsValue.clear();
		ref_txtVoltageYphasePresentGainValue.clear();
		ref_txtVoltageYphasePresentOffsetValue.clear();
		ref_txtVoltageYphaseUpdatedInRefStd.clear();
		ref_txtVoltageYphaseNewCalcRmsValue.clear();
		ref_txtVoltageYphaseNewCalcGainValue.clear();
		ref_txtVoltageYphaseNewCalcOffsetValue.clear();
	}



	public void clearVoltageBphaseGui(){

		ref_txtVoltageBphaseNoOfTapping.clear();
		ref_cmBxVoltageBphaseTapMaxList.getItems().clear();
		clearVoltageBphaseTapChangeGui();

	}

	public void clearVoltageBphaseTapChangeGui(){		

		ref_txtVoltageBphaseTapSelectionRelayCode.clear();
		ref_txtVoltageBphaseNoOfCalibPointEachTap.clear();
		ref_cmBxVoltageBphaseCalibPointNameList.getItems().clear();
		clearVoltageBphaseCalibPointChangeGui();

	}

	public void clearVoltageBphaseCalibPointChangeGui(){		

		ref_txtVoltageBphaseCalibPointValue.clear();
		ref_txtVoltageBphasePresentRmsValue.clear();
		ref_txtVoltageBphasePresentGainValue.clear();
		ref_txtVoltageBphasePresentOffsetValue.clear();
		ref_txtVoltageBphaseUpdatedInRefStd.clear();
		ref_txtVoltageBphaseNewCalcRmsValue.clear();
		ref_txtVoltageBphaseNewCalcGainValue.clear();
		ref_txtVoltageBphaseNewCalcOffsetValue.clear();
	}




	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("Sleep :InterruptedException:"+ e.getMessage());
		}

	}

	@FXML
	public void voltageRphaseSelectionOnChange(){

		ApplicationLauncher.logger.debug("voltageRphaseSelectionOnChange: Entry");

		//Platform.runLater(() -> {

		if(!initInProgress){
			voltageRphaseSelectionChangeGuiInProgress = true;

			//clearVoltageGui();
			clearVoltageRphaseGui();
			String selectedVoltagePhase = ref_cmBxVoltageRphaseSelected.getSelectionModel().getSelectedItem().toString();
			ApplicationLauncher.logger.debug("voltageRphaseSelectionOnChange: selectedVoltPhase : " + selectedVoltagePhase);
			List<VoltageCalibration> voltageCalibDataList = lscsCalibrationConfigFileUpdatedKeyValue.getVoltageCalibration();
			for(int phaseSelectionIndex = 0; phaseSelectionIndex < voltageCalibDataList.size();phaseSelectionIndex++){
				if(voltageCalibDataList.get(phaseSelectionIndex).getVoltagePhase().equals(selectedVoltagePhase)){
					List<VoltageTap> voltageTapList = voltageCalibDataList.get(phaseSelectionIndex).getVoltageTap();
					if(voltageTapList.size()>0){
						ref_txtVoltageRphaseNoOfTapping.setText(String.valueOf(voltageTapList.size()));
						String tapSelectionVoltageRelayCode = voltageTapList.get(0).getVoltageRelayId();
						ref_txtVoltageRphaseTapSelectionRelayCode.setText(tapSelectionVoltageRelayCode);

						for(int tapSelectionIndex = 0; tapSelectionIndex <voltageTapList.size();tapSelectionIndex++){
							ref_cmBxVoltageRphaseTapMaxList.getItems().add(voltageTapList.get(tapSelectionIndex).getVoltageTapMax());
							//List<CalibPoints> voltCalibPoints = voltageTapList.get(tapSelectionIndex).getCalibPoints();
						}
						ref_cmBxVoltageRphaseTapMaxList.getSelectionModel().select(0);
						List<CalibPoints> voltageCalibPoints = voltageTapList.get(0).getCalibPoints();
						if(voltageCalibPoints.size()>0){
							ref_txtVoltageRphaseNoOfCalibPointEachTap.setText(String.valueOf(voltageCalibPoints.size()));
							for(int calibPointSelectionIndex = 0; calibPointSelectionIndex <voltageCalibPoints.size();calibPointSelectionIndex++){
								ref_cmBxVoltageRphaseCalibPointNameList.getItems().add(voltageCalibPoints.get(calibPointSelectionIndex).getCalibPointName());
							}
							ref_cmBxVoltageRphaseCalibPointNameList.getSelectionModel().select(0);
							float calibPointVoltageValue = voltageCalibPoints.get(0).getCalibPointValue();
							ref_txtVoltageRphaseCalibPointValue.setText(String.valueOf(calibPointVoltageValue));
							long calibPointRmsValue = voltageCalibPoints.get(0).getRmsValue();
							ref_txtVoltageRphasePresentRmsValue.setText(String.valueOf(calibPointRmsValue));
							
							double calibPointGainValue = voltageCalibPoints.get(0).getGainValue();
							ref_txtVoltageRphasePresentGainValue.setText(String.valueOf(calibPointGainValue));
							double calibPointOffsetValue = voltageCalibPoints.get(0).getOffsetValue();
							ref_txtVoltageRphasePresentOffsetValue.setText(String.valueOf(calibPointOffsetValue));
							
						}
					}

				}

			}
			voltageRphaseSelectionChangeGuiInProgress = false;
			
			if ((ref_chkBxVoltageSyncSelection.isSelected()) && (ref_rdBtnVoltageAllPhase.isSelected()) ){
				int selectedVoltageRphaseIndex = ref_cmBxVoltageRphaseSelected.getSelectionModel().getSelectedIndex();
				ref_cmBxVoltageYphaseSelected.getSelectionModel().select(selectedVoltageRphaseIndex);
				ref_cmBxVoltageBphaseSelected.getSelectionModel().select(selectedVoltageRphaseIndex);
			}
		}
		//});
	}

	@FXML 
	public void voltageRphaseTapMaxListOnChange(){

		ApplicationLauncher.logger.debug("voltageRphaseTapMaxListOnChange: Entry");
		//Platform.runLater(() -> {
		if(!voltageRphaseSelectionChangeGuiInProgress){
			voltageRphaseTapMaxChangeGuiInProgress = true;
			//clearVoltageTapChangeGui();
			clearVoltageRphaseTapChangeGui();
			String selectedVoltagePhase = ref_cmBxVoltageRphaseSelected.getSelectionModel().getSelectedItem().toString();
			ApplicationLauncher.logger.debug("voltageRphaseTapMaxListOnChange: selectedVoltPhase : " + selectedVoltagePhase);
			List<VoltageCalibration> voltageCalibDataList = lscsCalibrationConfigFileUpdatedKeyValue.getVoltageCalibration();
			for(int phaseSelectionIndex = 0; phaseSelectionIndex < voltageCalibDataList.size();phaseSelectionIndex++){
				if(voltageCalibDataList.get(phaseSelectionIndex).getVoltagePhase().equals(selectedVoltagePhase)){
					List<VoltageTap> voltageTapList = voltageCalibDataList.get(phaseSelectionIndex).getVoltageTap();
					if(voltageTapList.size()>0){
						//ref_txtVoltNoOfTapping.setText(String.valueOf(voltageTapList.size()));

						int selectedVoltageTapMaxIndex = ref_cmBxVoltageRphaseTapMaxList.getSelectionModel().getSelectedIndex();
						ApplicationLauncher.logger.debug("voltageRphaseTapMaxListOnChange: selectedVoltageTapMax : " + ref_cmBxVoltageRphaseTapMaxList.getSelectionModel().getSelectedItem());
						ref_cmBxVoltageRphaseTapMaxList.getSelectionModel().select(selectedVoltageTapMaxIndex);
						String tapSelectionVoltageRelayCode = voltageTapList.get(selectedVoltageTapMaxIndex).getVoltageRelayId();
						ref_txtVoltageRphaseTapSelectionRelayCode.setText(tapSelectionVoltageRelayCode);

						List<CalibPoints> voltageCalibPoints = voltageTapList.get(selectedVoltageTapMaxIndex).getCalibPoints();
						if(voltageCalibPoints.size()>0){
							ref_txtVoltageRphaseNoOfCalibPointEachTap.setText(String.valueOf(voltageCalibPoints.size()));
							/*						for(int calibPointSelectionIndex = 0; calibPointSelectionIndex <voltageCalibPoints.size();calibPointSelectionIndex++){
							ref_cmBxVoltCalibPointNameList.getItems().add(voltageCalibPoints.get(calibPointSelectionIndex).getCalibPointName());
						}*/
							for(int calibPointSelectionIndex = 0; calibPointSelectionIndex < voltageCalibPoints.size();calibPointSelectionIndex++){

								ref_cmBxVoltageRphaseCalibPointNameList.getItems().add(voltageCalibPoints.get(calibPointSelectionIndex).getCalibPointName());
							}
							ref_cmBxVoltageRphaseCalibPointNameList.getSelectionModel().select(0);
							float calibPointVoltageValue = voltageCalibPoints.get(0).getCalibPointValue();
							ref_txtVoltageRphaseCalibPointValue.setText(String.valueOf(calibPointVoltageValue));
							long calibPointRmsValue = voltageCalibPoints.get(0).getRmsValue();
							ref_txtVoltageRphasePresentRmsValue.setText(String.valueOf(calibPointRmsValue));
							
							double calibPointGainValue = voltageCalibPoints.get(0).getGainValue();
							ref_txtVoltageRphasePresentGainValue.setText(String.valueOf(calibPointGainValue));
							double calibPointOffsetValue = voltageCalibPoints.get(0).getOffsetValue();
							ref_txtVoltageRphasePresentOffsetValue.setText(String.valueOf(calibPointOffsetValue));
						}
					}

				}
			}
			voltageRphaseTapMaxChangeGuiInProgress = false;
			
			if ((ref_chkBxVoltageSyncSelection.isSelected()) && (ref_rdBtnVoltageAllPhase.isSelected()) ){
				int selectedVoltageRphaseTapIndex = ref_cmBxVoltageRphaseTapMaxList.getSelectionModel().getSelectedIndex();
				ref_cmBxVoltageYphaseTapMaxList.getSelectionModel().select(selectedVoltageRphaseTapIndex);
				ref_cmBxVoltageBphaseTapMaxList.getSelectionModel().select(selectedVoltageRphaseTapIndex);
			}
		}
		//});

	}



	@FXML 
	public void voltageRphaseCalibPointsNameOnChange(){

		ApplicationLauncher.logger.debug("voltageRphaseCalibPointsNameOnChange: Entry");
		if(!voltageRphaseSelectionChangeGuiInProgress){
			if(!voltageRphaseTapMaxChangeGuiInProgress){
				//Platform.runLater(() -> {
				//clearVoltageCalibPointChangeGui();
				clearVoltageRphaseCalibPointChangeGui();
				String selectedVoltagePhase = ref_cmBxVoltageRphaseSelected.getSelectionModel().getSelectedItem().toString();
				ApplicationLauncher.logger.debug("voltageRphaseCalibPointsNameOnChange: selectedVoltPhase : " + selectedVoltagePhase);
				List<VoltageCalibration> voltageCalibDataList = lscsCalibrationConfigFileUpdatedKeyValue.getVoltageCalibration();
				for(int phaseSelectionIndex = 0; phaseSelectionIndex < voltageCalibDataList.size();phaseSelectionIndex++){
					if(voltageCalibDataList.get(phaseSelectionIndex).getVoltagePhase().equals(selectedVoltagePhase)){
						List<VoltageTap> voltageTapList = voltageCalibDataList.get(phaseSelectionIndex).getVoltageTap();
						if(voltageTapList.size()>0){

							int selectedVoltageTapMaxIndex = ref_cmBxVoltageRphaseTapMaxList.getSelectionModel().getSelectedIndex();
							ApplicationLauncher.logger.debug("voltageRphaseCalibPointsNameOnChange: selectedVoltageTapMax : " + ref_cmBxVoltageRphaseTapMaxList.getSelectionModel().getSelectedItem());
							//ref_cmBxVoltTapMaxList.getSelectionModel().select(selectedVoltageTapMaxIndex);
							//String tapSelectionVoltageRelayCode = voltageTapList.get(selectedVoltageTapMaxIndex).getVoltageRelayId();
							//ref_txtVoltTapSelectionRelayCode.setText(tapSelectionVoltageRelayCode);

							List<CalibPoints> voltageCalibPoints = voltageTapList.get(selectedVoltageTapMaxIndex).getCalibPoints();
							if(voltageCalibPoints.size()>0){
								//ref_txtVoltNoOfCalibPointEachTap.setText(String.valueOf(voltageCalibPoints.size()));
								/*						for(int calibPointSelectionIndex = 0; calibPointSelectionIndex <voltageCalibPoints.size();calibPointSelectionIndex++){
							ref_cmBxVoltCalibPointNameList.getItems().add(voltageCalibPoints.get(calibPointSelectionIndex).getCalibPointName());
						}*/
								int selectedVoltageCalibPointNameIndex = ref_cmBxVoltageRphaseCalibPointNameList.getSelectionModel().getSelectedIndex();
								ApplicationLauncher.logger.debug("voltageRphaseCalibPointsNameOnChange: selectedVoltageCalibPointName : " + ref_cmBxVoltageRphaseCalibPointNameList.getSelectionModel().getSelectedItem());
								//ref_cmBxVoltCalibPointNameList.getSelectionModel().select(0);
								float calibPointVoltageValue = voltageCalibPoints.get(selectedVoltageCalibPointNameIndex).getCalibPointValue();
								ref_txtVoltageRphaseCalibPointValue.setText(String.valueOf(calibPointVoltageValue));
								long calibPointRmsValue = voltageCalibPoints.get(selectedVoltageCalibPointNameIndex).getRmsValue();
								ref_txtVoltageRphasePresentRmsValue.setText(String.valueOf(calibPointRmsValue));
								
								double calibPointGainValue = voltageCalibPoints.get(selectedVoltageCalibPointNameIndex).getGainValue();
								ref_txtVoltageRphasePresentGainValue.setText(String.valueOf(calibPointGainValue));
								double calibPointOffsetValue = voltageCalibPoints.get(selectedVoltageCalibPointNameIndex).getOffsetValue();
								ref_txtVoltageRphasePresentOffsetValue.setText(String.valueOf(calibPointOffsetValue));
							}
						}

					}
				}
			}
		}
		
		if ((ref_chkBxVoltageSyncSelection.isSelected()) && (ref_rdBtnVoltageAllPhase.isSelected()) ){
			int selectedVoltageRphaseCalibPointNameIndex = ref_cmBxVoltageRphaseCalibPointNameList.getSelectionModel().getSelectedIndex();
			ref_cmBxVoltageYphaseCalibPointNameList.getSelectionModel().select(selectedVoltageRphaseCalibPointNameIndex);
			ref_cmBxVoltageBphaseCalibPointNameList.getSelectionModel().select(selectedVoltageRphaseCalibPointNameIndex);
		}
		//});

	}



	@FXML
	public void voltageYphaseSelectionOnChange(){

		ApplicationLauncher.logger.debug("voltageYphaseSelectionOnChange: Entry");

		//Platform.runLater(() -> {

		if(!initInProgress){
			voltageYphaseSelectionChangeGuiInProgress = true;

			//clearVoltageGui();
			clearVoltageYphaseGui();
			String selectedVoltagePhase = ref_cmBxVoltageYphaseSelected.getSelectionModel().getSelectedItem().toString();
			ApplicationLauncher.logger.debug("voltageYphaseSelectionOnChange: selectedVoltPhase : " + selectedVoltagePhase);
			List<VoltageCalibration> voltageCalibDataList = lscsCalibrationConfigFileUpdatedKeyValue.getVoltageCalibration();
			for(int phaseSelectionIndex = 0; phaseSelectionIndex < voltageCalibDataList.size();phaseSelectionIndex++){
				if(voltageCalibDataList.get(phaseSelectionIndex).getVoltagePhase().equals(selectedVoltagePhase)){
					List<VoltageTap> voltageTapList = voltageCalibDataList.get(phaseSelectionIndex).getVoltageTap();
					if(voltageTapList.size()>0){
						ref_txtVoltageYphaseNoOfTapping.setText(String.valueOf(voltageTapList.size()));
						String tapSelectionVoltageRelayCode = voltageTapList.get(0).getVoltageRelayId();
						ref_txtVoltageYphaseTapSelectionRelayCode.setText(tapSelectionVoltageRelayCode);

						for(int tapSelectionIndex = 0; tapSelectionIndex <voltageTapList.size();tapSelectionIndex++){
							ref_cmBxVoltageYphaseTapMaxList.getItems().add(voltageTapList.get(tapSelectionIndex).getVoltageTapMax());
							//List<CalibPoints> voltCalibPoints = voltageTapList.get(tapSelectionIndex).getCalibPoints();
						}
						ref_cmBxVoltageYphaseTapMaxList.getSelectionModel().select(0);
						List<CalibPoints> voltageCalibPoints = voltageTapList.get(0).getCalibPoints();
						if(voltageCalibPoints.size()>0){
							ref_txtVoltageYphaseNoOfCalibPointEachTap.setText(String.valueOf(voltageCalibPoints.size()));
							for(int calibPointSelectionIndex = 0; calibPointSelectionIndex <voltageCalibPoints.size();calibPointSelectionIndex++){
								ref_cmBxVoltageYphaseCalibPointNameList.getItems().add(voltageCalibPoints.get(calibPointSelectionIndex).getCalibPointName());
							}
							ref_cmBxVoltageYphaseCalibPointNameList.getSelectionModel().select(0);
							float calibPointVoltageValue = voltageCalibPoints.get(0).getCalibPointValue();
							ref_txtVoltageYphaseCalibPointValue.setText(String.valueOf(calibPointVoltageValue));
							long calibPointRmsValue = voltageCalibPoints.get(0).getRmsValue();
							ref_txtVoltageYphasePresentRmsValue.setText(String.valueOf(calibPointRmsValue));
							
							double calibPointGainValue = voltageCalibPoints.get(0).getGainValue();
							ref_txtVoltageYphasePresentGainValue.setText(String.valueOf(calibPointGainValue));
							double calibPointOffsetValue = voltageCalibPoints.get(0).getOffsetValue();
							ref_txtVoltageYphasePresentOffsetValue.setText(String.valueOf(calibPointOffsetValue));
						}
					}

				}

			}
			voltageYphaseSelectionChangeGuiInProgress = false;
		}
		//});
	}

	@FXML 
	public void voltageYphaseTapMaxListOnChange(){

		ApplicationLauncher.logger.debug("voltageYphaseTapMaxListOnChange: Entry");
		//Platform.runLater(() -> {
		if(!voltageYphaseSelectionChangeGuiInProgress){
			voltageYphaseTapMaxChangeGuiInProgress = true;
			//clearVoltageTapChangeGui();
			clearVoltageYphaseTapChangeGui();
			String selectedVoltagePhase = ref_cmBxVoltageYphaseSelected.getSelectionModel().getSelectedItem().toString();
			ApplicationLauncher.logger.debug("voltageYphaseTapMaxListOnChange: selectedVoltPhase : " + selectedVoltagePhase);
			List<VoltageCalibration> voltageCalibDataList = lscsCalibrationConfigFileUpdatedKeyValue.getVoltageCalibration();
			for(int phaseSelectionIndex = 0; phaseSelectionIndex < voltageCalibDataList.size();phaseSelectionIndex++){
				if(voltageCalibDataList.get(phaseSelectionIndex).getVoltagePhase().equals(selectedVoltagePhase)){
					List<VoltageTap> voltageTapList = voltageCalibDataList.get(phaseSelectionIndex).getVoltageTap();
					if(voltageTapList.size()>0){
						//ref_txtVoltNoOfTapping.setText(String.valueOf(voltageTapList.size()));

						int selectedVoltageTapMaxIndex = ref_cmBxVoltageYphaseTapMaxList.getSelectionModel().getSelectedIndex();
						ApplicationLauncher.logger.debug("voltageYphaseTapMaxListOnChange: selectedVoltageTapMax : " + ref_cmBxVoltageYphaseTapMaxList.getSelectionModel().getSelectedItem());
						ref_cmBxVoltageYphaseTapMaxList.getSelectionModel().select(selectedVoltageTapMaxIndex);
						String tapSelectionVoltageRelayCode = voltageTapList.get(selectedVoltageTapMaxIndex).getVoltageRelayId();
						ref_txtVoltageYphaseTapSelectionRelayCode.setText(tapSelectionVoltageRelayCode);

						List<CalibPoints> voltageCalibPoints = voltageTapList.get(selectedVoltageTapMaxIndex).getCalibPoints();
						if(voltageCalibPoints.size()>0){
							ref_txtVoltageYphaseNoOfCalibPointEachTap.setText(String.valueOf(voltageCalibPoints.size()));
							/*						for(int calibPointSelectionIndex = 0; calibPointSelectionIndex <voltageCalibPoints.size();calibPointSelectionIndex++){
						ref_cmBxVoltCalibPointNameList.getItems().add(voltageCalibPoints.get(calibPointSelectionIndex).getCalibPointName());
					}*/
							for(int calibPointSelectionIndex = 0; calibPointSelectionIndex < voltageCalibPoints.size();calibPointSelectionIndex++){

								ref_cmBxVoltageYphaseCalibPointNameList.getItems().add(voltageCalibPoints.get(calibPointSelectionIndex).getCalibPointName());
							}
							ref_cmBxVoltageYphaseCalibPointNameList.getSelectionModel().select(0);
							float calibPointVoltageValue = voltageCalibPoints.get(0).getCalibPointValue();
							ref_txtVoltageYphaseCalibPointValue.setText(String.valueOf(calibPointVoltageValue));
							long calibPointRmsValue = voltageCalibPoints.get(0).getRmsValue();
							ref_txtVoltageYphasePresentRmsValue.setText(String.valueOf(calibPointRmsValue));
							
							double calibPointGainValue = voltageCalibPoints.get(0).getGainValue();
							ref_txtVoltageYphasePresentGainValue.setText(String.valueOf(calibPointGainValue));
							double calibPointOffsetValue = voltageCalibPoints.get(0).getOffsetValue();
							ref_txtVoltageYphasePresentOffsetValue.setText(String.valueOf(calibPointOffsetValue));
						}
					}

				}
			}
			voltageYphaseTapMaxChangeGuiInProgress = false;
		}
		//});

	}



	@FXML 
	public void voltageYphaseCalibPointsNameOnChange(){

		ApplicationLauncher.logger.debug("voltageYphaseCalibPointsNameOnChange: Entry");
		if(!voltageYphaseSelectionChangeGuiInProgress){
			if(!voltageYphaseTapMaxChangeGuiInProgress){
				//Platform.runLater(() -> {
				//clearVoltageCalibPointChangeGui();
				clearVoltageYphaseCalibPointChangeGui();
				String selectedVoltagePhase = ref_cmBxVoltageYphaseSelected.getSelectionModel().getSelectedItem().toString();
				ApplicationLauncher.logger.debug("voltageYphaseCalibPointsNameOnChange: selectedVoltPhase : " + selectedVoltagePhase);
				List<VoltageCalibration> voltageCalibDataList = lscsCalibrationConfigFileUpdatedKeyValue.getVoltageCalibration();
				for(int phaseSelectionIndex = 0; phaseSelectionIndex < voltageCalibDataList.size();phaseSelectionIndex++){
					if(voltageCalibDataList.get(phaseSelectionIndex).getVoltagePhase().equals(selectedVoltagePhase)){
						List<VoltageTap> voltageTapList = voltageCalibDataList.get(phaseSelectionIndex).getVoltageTap();
						if(voltageTapList.size()>0){

							int selectedVoltageTapMaxIndex = ref_cmBxVoltageYphaseTapMaxList.getSelectionModel().getSelectedIndex();
							ApplicationLauncher.logger.debug("voltageYphaseCalibPointsNameOnChange: selectedVoltageTapMax : " + ref_cmBxVoltageYphaseTapMaxList.getSelectionModel().getSelectedItem());
							//ref_cmBxVoltTapMaxList.getSelectionModel().select(selectedVoltageTapMaxIndex);
							//String tapSelectionVoltageRelayCode = voltageTapList.get(selectedVoltageTapMaxIndex).getVoltageRelayId();
							//ref_txtVoltTapSelectionRelayCode.setText(tapSelectionVoltageRelayCode);

							List<CalibPoints> voltageCalibPoints = voltageTapList.get(selectedVoltageTapMaxIndex).getCalibPoints();
							if(voltageCalibPoints.size()>0){
								//ref_txtVoltNoOfCalibPointEachTap.setText(String.valueOf(voltageCalibPoints.size()));
								/*						for(int calibPointSelectionIndex = 0; calibPointSelectionIndex <voltageCalibPoints.size();calibPointSelectionIndex++){
						ref_cmBxVoltCalibPointNameList.getItems().add(voltageCalibPoints.get(calibPointSelectionIndex).getCalibPointName());
					}*/
								int selectedVoltageCalibPointNameIndex = ref_cmBxVoltageYphaseCalibPointNameList.getSelectionModel().getSelectedIndex();
								ApplicationLauncher.logger.debug("voltageYphaseCalibPointsNameOnChange: selectedVoltageCalibPointName : " + ref_cmBxVoltageYphaseCalibPointNameList.getSelectionModel().getSelectedItem());
								//ref_cmBxVoltCalibPointNameList.getSelectionModel().select(0);
								float calibPointVoltageValue = voltageCalibPoints.get(selectedVoltageCalibPointNameIndex).getCalibPointValue();
								ref_txtVoltageYphaseCalibPointValue.setText(String.valueOf(calibPointVoltageValue));
								long calibPointRmsValue = voltageCalibPoints.get(selectedVoltageCalibPointNameIndex).getRmsValue();
								ref_txtVoltageYphasePresentRmsValue.setText(String.valueOf(calibPointRmsValue));
								
								double calibPointGainValue = voltageCalibPoints.get(selectedVoltageCalibPointNameIndex).getGainValue();
								ref_txtVoltageYphasePresentGainValue.setText(String.valueOf(calibPointGainValue));
								double calibPointOffsetValue = voltageCalibPoints.get(selectedVoltageCalibPointNameIndex).getOffsetValue();
								ref_txtVoltageYphasePresentOffsetValue.setText(String.valueOf(calibPointOffsetValue));
							}
						}

					}
				}
			}
		}
		//});

	}





	@FXML
	public void voltageBphaseSelectionOnChange(){

		ApplicationLauncher.logger.debug("voltageBphaseSelectionOnChange: Entry");

		//Platform.runLater(() -> {

		if(!initInProgress){
			voltageBphaseSelectionChangeGuiInProgress = true;

			//clearVoltageGui();
			clearVoltageBphaseGui();
			String selectedVoltagePhase = ref_cmBxVoltageBphaseSelected.getSelectionModel().getSelectedItem().toString();
			ApplicationLauncher.logger.debug("voltageBphaseSelectionOnChange: selectedVoltPhase : " + selectedVoltagePhase);
			List<VoltageCalibration> voltageCalibDataList = lscsCalibrationConfigFileUpdatedKeyValue.getVoltageCalibration();
			for(int phaseSelectionIndex = 0; phaseSelectionIndex < voltageCalibDataList.size();phaseSelectionIndex++){
				if(voltageCalibDataList.get(phaseSelectionIndex).getVoltagePhase().equals(selectedVoltagePhase)){
					List<VoltageTap> voltageTapList = voltageCalibDataList.get(phaseSelectionIndex).getVoltageTap();
					if(voltageTapList.size()>0){
						ref_txtVoltageBphaseNoOfTapping.setText(String.valueOf(voltageTapList.size()));
						String tapSelectionVoltageRelayCode = voltageTapList.get(0).getVoltageRelayId();
						ref_txtVoltageBphaseTapSelectionRelayCode.setText(tapSelectionVoltageRelayCode);

						for(int tapSelectionIndex = 0; tapSelectionIndex <voltageTapList.size();tapSelectionIndex++){
							ref_cmBxVoltageBphaseTapMaxList.getItems().add(voltageTapList.get(tapSelectionIndex).getVoltageTapMax());
							//List<CalibPoints> voltCalibPoints = voltageTapList.get(tapSelectionIndex).getCalibPoints();
						}
						ref_cmBxVoltageBphaseTapMaxList.getSelectionModel().select(0);
						List<CalibPoints> voltageCalibPoints = voltageTapList.get(0).getCalibPoints();
						if(voltageCalibPoints.size()>0){
							ref_txtVoltageBphaseNoOfCalibPointEachTap.setText(String.valueOf(voltageCalibPoints.size()));
							for(int calibPointSelectionIndex = 0; calibPointSelectionIndex <voltageCalibPoints.size();calibPointSelectionIndex++){
								ref_cmBxVoltageBphaseCalibPointNameList.getItems().add(voltageCalibPoints.get(calibPointSelectionIndex).getCalibPointName());
							}
							ref_cmBxVoltageBphaseCalibPointNameList.getSelectionModel().select(0);
							float calibPointVoltageValue = voltageCalibPoints.get(0).getCalibPointValue();
							ref_txtVoltageBphaseCalibPointValue.setText(String.valueOf(calibPointVoltageValue));
							long calibPointRmsValue = voltageCalibPoints.get(0).getRmsValue();
							ref_txtVoltageBphasePresentRmsValue.setText(String.valueOf(calibPointRmsValue));
							
							double calibPointGainValue = voltageCalibPoints.get(0).getGainValue();
							ref_txtVoltageBphasePresentGainValue.setText(String.valueOf(calibPointGainValue));
							double calibPointOffsetValue = voltageCalibPoints.get(0).getOffsetValue();
							ref_txtVoltageBphasePresentOffsetValue.setText(String.valueOf(calibPointOffsetValue));
						}
					}

				}

			}
			voltageBphaseSelectionChangeGuiInProgress = false;
		}
		//});
	}

	@FXML 
	public void voltageBphaseTapMaxListOnChange(){

		ApplicationLauncher.logger.debug("voltageBphaseTapMaxListOnChange: Entry");
		//Platform.runLater(() -> {
		if(!voltageBphaseSelectionChangeGuiInProgress){
			voltageBphaseTapMaxChangeGuiInProgress = true;
			//clearVoltageTapChangeGui();
			clearVoltageBphaseTapChangeGui();
			String selectedVoltagePhase = ref_cmBxVoltageBphaseSelected.getSelectionModel().getSelectedItem().toString();
			ApplicationLauncher.logger.debug("voltageBphaseTapMaxListOnChange: selectedVoltPhase : " + selectedVoltagePhase);
			List<VoltageCalibration> voltageCalibDataList = lscsCalibrationConfigFileUpdatedKeyValue.getVoltageCalibration();
			for(int phaseSelectionIndex = 0; phaseSelectionIndex < voltageCalibDataList.size();phaseSelectionIndex++){
				if(voltageCalibDataList.get(phaseSelectionIndex).getVoltagePhase().equals(selectedVoltagePhase)){
					List<VoltageTap> voltageTapList = voltageCalibDataList.get(phaseSelectionIndex).getVoltageTap();
					if(voltageTapList.size()>0){
						//ref_txtVoltNoOfTapping.setText(String.valueOf(voltageTapList.size()));

						int selectedVoltageTapMaxIndex = ref_cmBxVoltageBphaseTapMaxList.getSelectionModel().getSelectedIndex();
						ApplicationLauncher.logger.debug("voltageBphaseTapMaxListOnChange: selectedVoltageTapMax : " + ref_cmBxVoltageBphaseTapMaxList.getSelectionModel().getSelectedItem());
						ref_cmBxVoltageBphaseTapMaxList.getSelectionModel().select(selectedVoltageTapMaxIndex);
						String tapSelectionVoltageRelayCode = voltageTapList.get(selectedVoltageTapMaxIndex).getVoltageRelayId();
						ref_txtVoltageBphaseTapSelectionRelayCode.setText(tapSelectionVoltageRelayCode);

						List<CalibPoints> voltageCalibPoints = voltageTapList.get(selectedVoltageTapMaxIndex).getCalibPoints();
						if(voltageCalibPoints.size()>0){
							ref_txtVoltageBphaseNoOfCalibPointEachTap.setText(String.valueOf(voltageCalibPoints.size()));
							/*						for(int calibPointSelectionIndex = 0; calibPointSelectionIndex <voltageCalibPoints.size();calibPointSelectionIndex++){
						ref_cmBxVoltCalibPointNameList.getItems().add(voltageCalibPoints.get(calibPointSelectionIndex).getCalibPointName());
					}*/
							for(int calibPointSelectionIndex = 0; calibPointSelectionIndex < voltageCalibPoints.size();calibPointSelectionIndex++){

								ref_cmBxVoltageBphaseCalibPointNameList.getItems().add(voltageCalibPoints.get(calibPointSelectionIndex).getCalibPointName());
							}
							ref_cmBxVoltageBphaseCalibPointNameList.getSelectionModel().select(0);
							float calibPointVoltageValue = voltageCalibPoints.get(0).getCalibPointValue();
							ref_txtVoltageBphaseCalibPointValue.setText(String.valueOf(calibPointVoltageValue));
							long calibPointRmsValue = voltageCalibPoints.get(0).getRmsValue();
							ref_txtVoltageBphasePresentRmsValue.setText(String.valueOf(calibPointRmsValue));
							
							double calibPointGainValue = voltageCalibPoints.get(0).getGainValue();
							ref_txtVoltageBphasePresentGainValue.setText(String.valueOf(calibPointGainValue));
							double calibPointOffsetValue = voltageCalibPoints.get(0).getOffsetValue();
							ref_txtVoltageBphasePresentOffsetValue.setText(String.valueOf(calibPointOffsetValue));
						}
					}

				}
			}
			voltageBphaseTapMaxChangeGuiInProgress = false;
		}
		//});

	}



	@FXML 
	public void voltageBphaseCalibPointsNameOnChange(){

		ApplicationLauncher.logger.debug("voltageBphaseCalibPointsNameOnChange: Entry");
		if(!voltageBphaseSelectionChangeGuiInProgress){
			if(!voltageBphaseTapMaxChangeGuiInProgress){
				//Platform.runLater(() -> {
				//clearVoltageCalibPointChangeGui();
				clearVoltageBphaseCalibPointChangeGui();
				String selectedVoltagePhase = ref_cmBxVoltageBphaseSelected.getSelectionModel().getSelectedItem().toString();
				ApplicationLauncher.logger.debug("voltageBphaseCalibPointsNameOnChange: selectedVoltPhase : " + selectedVoltagePhase);
				List<VoltageCalibration> voltageCalibDataList = lscsCalibrationConfigFileUpdatedKeyValue.getVoltageCalibration();
				for(int phaseSelectionIndex = 0; phaseSelectionIndex < voltageCalibDataList.size();phaseSelectionIndex++){
					if(voltageCalibDataList.get(phaseSelectionIndex).getVoltagePhase().equals(selectedVoltagePhase)){
						List<VoltageTap> voltageTapList = voltageCalibDataList.get(phaseSelectionIndex).getVoltageTap();
						if(voltageTapList.size()>0){

							int selectedVoltageTapMaxIndex = ref_cmBxVoltageBphaseTapMaxList.getSelectionModel().getSelectedIndex();
							ApplicationLauncher.logger.debug("voltageBphaseCalibPointsNameOnChange: selectedVoltageTapMax : " + ref_cmBxVoltageBphaseTapMaxList.getSelectionModel().getSelectedItem());
							//ref_cmBxVoltTapMaxList.getSelectionModel().select(selectedVoltageTapMaxIndex);
							//String tapSelectionVoltageRelayCode = voltageTapList.get(selectedVoltageTapMaxIndex).getVoltageRelayId();
							//ref_txtVoltTapSelectionRelayCode.setText(tapSelectionVoltageRelayCode);

							List<CalibPoints> voltageCalibPoints = voltageTapList.get(selectedVoltageTapMaxIndex).getCalibPoints();
							if(voltageCalibPoints.size()>0){
								//ref_txtVoltNoOfCalibPointEachTap.setText(String.valueOf(voltageCalibPoints.size()));
								/*						for(int calibPointSelectionIndex = 0; calibPointSelectionIndex <voltageCalibPoints.size();calibPointSelectionIndex++){
						ref_cmBxVoltCalibPointNameList.getItems().add(voltageCalibPoints.get(calibPointSelectionIndex).getCalibPointName());
					}*/
								int selectedVoltageCalibPointNameIndex = ref_cmBxVoltageBphaseCalibPointNameList.getSelectionModel().getSelectedIndex();
								ApplicationLauncher.logger.debug("voltageBphaseCalibPointsNameOnChange: selectedVoltageCalibPointName : " + ref_cmBxVoltageBphaseCalibPointNameList.getSelectionModel().getSelectedItem());
								//ref_cmBxVoltCalibPointNameList.getSelectionModel().select(0);
								float calibPointVoltageValue = voltageCalibPoints.get(selectedVoltageCalibPointNameIndex).getCalibPointValue();
								ref_txtVoltageBphaseCalibPointValue.setText(String.valueOf(calibPointVoltageValue));
								long calibPointRmsValue = voltageCalibPoints.get(selectedVoltageCalibPointNameIndex).getRmsValue();
								ref_txtVoltageBphasePresentRmsValue.setText(String.valueOf(calibPointRmsValue));
								
								double calibPointGainValue = voltageCalibPoints.get(selectedVoltageCalibPointNameIndex).getGainValue();
								ref_txtVoltageBphasePresentGainValue.setText(String.valueOf(calibPointGainValue));
								double calibPointOffsetValue = voltageCalibPoints.get(selectedVoltageCalibPointNameIndex).getOffsetValue();
								ref_txtVoltageBphasePresentOffsetValue.setText(String.valueOf(calibPointOffsetValue));
							}
						}

					}
				}
			}
		}
		//});

	}



	@FXML 
	public void calculateNewVoltageRmsClick(){
		ApplicationLauncher.logger.debug("calculateNewVoltageRmsClick : Entry");
		if(ref_rdBtnVoltageAllPhase.isSelected()){

			calculateNewVoltageRphaseRms();
			calculateNewVoltageYphaseRms();
			calculateNewVoltageBphaseRms();
		}else if (ref_rdBtnVoltageRphaseOnly.isSelected()){
			calculateNewVoltageRphaseRms();

		}else if (ref_rdBtnVoltageYphaseOnly.isSelected()){
			calculateNewVoltageYphaseRms();

		}else if (ref_rdBtnVoltageBphaseOnly.isSelected()){
			calculateNewVoltageBphaseRms();

		}

	}




	public void calculateNewVoltageRphaseRms(){

		ApplicationLauncher.logger.debug("calculateNewVoltageRphaseRms: Entry");

		long presentVoltageRmsValue = Long.parseLong(ref_txtVoltageRphasePresentRmsValue.getText());
		float presentVoltageTargetValue = Float.parseFloat(ref_txtVoltageRphaseCalibPointValue.getText());
		if(!ref_txtVoltageRphaseUpdatedInRefStd.getText().isEmpty()){
			float newUpdatedVoltageInRefStd = Float.parseFloat(ref_txtVoltageRphaseUpdatedInRefStd.getText());

			ApplicationLauncher.logger.debug("calculateNewVoltageRphaseRms: presentVoltageRmsValue: " + presentVoltageRmsValue);
			ApplicationLauncher.logger.debug("calculateNewVoltageRphaseRms: presentVoltageTargetValue: " + presentVoltageTargetValue);
			ApplicationLauncher.logger.debug("calculateNewVoltageRphaseRms: newUpdatedVoltageInRefStd: " + newUpdatedVoltageInRefStd);

			Float newRmsFloatValue = ( (presentVoltageRmsValue*presentVoltageTargetValue)/ newUpdatedVoltageInRefStd  );
			ApplicationLauncher.logger.debug("calculateNewVoltageRphaseRms: newRmsFloatValue: " + newRmsFloatValue);

			ApplicationLauncher.logger.debug("calculateNewVoltageRphaseRms: newRmsFloatValue rounded: " + Math.round(newRmsFloatValue));
			//newRmsFloatValue = Math.round(newRmsFloatValue);
			long newVoltageRmsValue = Math.round(newRmsFloatValue);//newRmsFloatValue.longValue();


			ApplicationLauncher.logger.debug("calculateNewVoltageRphaseRms: newVoltageRmsValue: " + newVoltageRmsValue);
			ref_txtVoltageRphaseNewCalcRmsValue.setText(String.valueOf(newVoltageRmsValue));
		}
	}



	public void calculateNewVoltageYphaseRms(){

		ApplicationLauncher.logger.debug("calculateNewVoltageYphaseRms: Entry");

		long presentVoltageRmsValue = Long.parseLong(ref_txtVoltageYphasePresentRmsValue.getText());
		float presentVoltageTargetValue = Float.parseFloat(ref_txtVoltageYphaseCalibPointValue.getText());
		if(!ref_txtVoltageYphaseUpdatedInRefStd.getText().isEmpty()){
			float newUpdatedVoltageInRefStd = Float.parseFloat(ref_txtVoltageYphaseUpdatedInRefStd.getText());

			ApplicationLauncher.logger.debug("calculateNewVoltageYphaseRms: presentVoltageRmsValue: " + presentVoltageRmsValue);
			ApplicationLauncher.logger.debug("calculateNewVoltageYphaseRms: presentVoltageTargetValue: " + presentVoltageTargetValue);
			ApplicationLauncher.logger.debug("calculateNewVoltageYphaseRms: newUpdatedVoltageInRefStd: " + newUpdatedVoltageInRefStd);

			Float newRmsFloatValue = ( (presentVoltageRmsValue*presentVoltageTargetValue)/ newUpdatedVoltageInRefStd  );
			ApplicationLauncher.logger.debug("calculateNewVoltageYphaseRms: newRmsFloatValue: " + newRmsFloatValue);

			ApplicationLauncher.logger.debug("calculateNewVoltageYphaseRms: newRmsFloatValue rounded: " + Math.round(newRmsFloatValue));
			//newRmsFloatValue = Math.round(newRmsFloatValue);
			long newVoltageRmsValue = Math.round(newRmsFloatValue);//newRmsFloatValue.longValue();


			ApplicationLauncher.logger.debug("calculateNewVoltageYphaseRms: newVoltageRmsValue: " + newVoltageRmsValue);
			ref_txtVoltageYphaseNewCalcRmsValue.setText(String.valueOf(newVoltageRmsValue));
		}
	}




	public void calculateNewVoltageBphaseRms(){

		ApplicationLauncher.logger.debug("calculateNewVoltageBphaseRms: Entry");

		long presentVoltageRmsValue = Long.parseLong(ref_txtVoltageBphasePresentRmsValue.getText());
		float presentVoltageTargetValue = Float.parseFloat(ref_txtVoltageBphaseCalibPointValue.getText());
		if(!ref_txtVoltageBphaseUpdatedInRefStd.getText().isEmpty()){
			float newUpdatedVoltageInRefStd = Float.parseFloat(ref_txtVoltageBphaseUpdatedInRefStd.getText());

			ApplicationLauncher.logger.debug("calculateNewVoltageBphaseRms: presentVoltageRmsValue: " + presentVoltageRmsValue);
			ApplicationLauncher.logger.debug("calculateNewVoltageBphaseRms: presentVoltageTargetValue: " + presentVoltageTargetValue);
			ApplicationLauncher.logger.debug("calculateNewVoltageBphaseRms: newUpdatedVoltageInRefStd: " + newUpdatedVoltageInRefStd);

			Float newRmsFloatValue = ( (presentVoltageRmsValue*presentVoltageTargetValue)/ newUpdatedVoltageInRefStd  );
			ApplicationLauncher.logger.debug("calculateNewVoltageBphaseRms: newRmsFloatValue: " + newRmsFloatValue);

			ApplicationLauncher.logger.debug("calculateNewVoltageBphaseRms: newRmsFloatValue rounded: " + Math.round(newRmsFloatValue));
			//newRmsFloatValue = Math.round(newRmsFloatValue);
			long newVoltageRmsValue = Math.round(newRmsFloatValue);//newRmsFloatValue.longValue();


			ApplicationLauncher.logger.debug("calculateNewVoltageBphaseRms: newVoltageRmsValue: " + newVoltageRmsValue);
			ref_txtVoltageBphaseNewCalcRmsValue.setText(String.valueOf(newVoltageRmsValue));
		}
	}
	
	public void calculateNewVoltageGainOffset(String selectedVoltagePhase){

		ApplicationLauncher.logger.debug("calculateNewVoltageGainOffset: Entry");

		double newSetGainValue = 0.0;
		double newSetOffsetValue = 0.0;
		//String selectedVoltagePhase = ref_cmBxVoltageBphaseSelected.getSelectionModel().getSelectedItem().toString();
		ApplicationLauncher.logger.debug("calculateNewVoltageGainOffset: selectedVoltPhase : " + selectedVoltagePhase);
		
		List<VoltageCalibration> voltageCalibDataList = lscsCalibrationConfigFileUpdatedKeyValue.getVoltageCalibration();
		//List<VoltageCalibration> voltageCalibDataList = lscsCalibrationConfigFileUpdatedKeyValue.getVoltageCalibration();
		for(int phaseSelectionIndex = 0; phaseSelectionIndex < voltageCalibDataList.size();phaseSelectionIndex++){
			if(voltageCalibDataList.get(phaseSelectionIndex).getVoltagePhase().equals(selectedVoltagePhase)){
				List<VoltageTap> voltageTapList = voltageCalibDataList.get(phaseSelectionIndex).getVoltageTap();
				if(voltageTapList.size()>0){
					//ref_txtVoltageBphaseNoOfTapping.setText(String.valueOf(voltageTapList.size()));
					//String tapSelectionVoltageRelayCode = voltageTapList.get(0).getVoltageRelayId();
					//ref_txtVoltageBphaseTapSelectionRelayCode.setText(tapSelectionVoltageRelayCode);

					for(int tapSelectionIndex = 0; tapSelectionIndex <voltageTapList.size();tapSelectionIndex++){
						//ref_cmBxVoltageBphaseTapMaxList.getItems().add(voltageTapList.get(tapSelectionIndex).getVoltageTapMax());
						List<CalibPoints> voltageCalibPoints = voltageTapList.get(tapSelectionIndex).getCalibPoints();
						if(voltageCalibPoints.size()>0){
							float previousCalibPointVoltageValue = -200.0f; 
							long previouscalibPointRmsValue = -300;
							double gainValue = 0;
							double offsetValue = 0;
							for(int calibPointSelectionIndex = 0; calibPointSelectionIndex <voltageCalibPoints.size();calibPointSelectionIndex++){
								//ref_cmBxVoltageBphaseCalibPointNameList.getItems().add(voltageCalibPoints.get(calibPointSelectionIndex).getCalibPointName());
								float calibPointVoltageValue = voltageCalibPoints.get(calibPointSelectionIndex).getCalibPointValue(); 
								long calibPointRmsValue = voltageCalibPoints.get(calibPointSelectionIndex).getRmsValue();
								if(calibPointSelectionIndex != 0){
									gainValue = Double.parseDouble( GuiUtils.calculateGain(previouscalibPointRmsValue, previousCalibPointVoltageValue,
												calibPointRmsValue,calibPointVoltageValue));
									ApplicationLauncher.logger.debug("calculateNewVoltageGainOffset: Index: " + calibPointSelectionIndex +" : gainValue : "  + gainValue);
									//gainValue = 1369.28;
									offsetValue = Double.parseDouble( GuiUtils.calculateOffset(previouscalibPointRmsValue, previousCalibPointVoltageValue,
											gainValue));
									ApplicationLauncher.logger.debug("calculateNewVoltageGainOffset: Index: " + calibPointSelectionIndex +" : offsetValue : "  + offsetValue);
									voltageCalibPoints.get(calibPointSelectionIndex).setGainValue(gainValue);
									voltageCalibPoints.get(calibPointSelectionIndex).setOffsetValue(offsetValue);
									voltageCalibDataList.get(phaseSelectionIndex).getVoltageTap().get(tapSelectionIndex).getCalibPoints().get(calibPointSelectionIndex).setGainValue(gainValue);
									voltageCalibDataList.get(phaseSelectionIndex).getVoltageTap().get(tapSelectionIndex).getCalibPoints().get(calibPointSelectionIndex).setOffsetValue(offsetValue);
									
									newSetGainValue = lscsCalibrationConfigFileUpdatedKeyValue.getVoltageCalibration().get(phaseSelectionIndex).getVoltageTap().get(tapSelectionIndex).getCalibPoints().get(calibPointSelectionIndex).getGainValue();
									newSetOffsetValue = lscsCalibrationConfigFileUpdatedKeyValue.getVoltageCalibration().get(phaseSelectionIndex).getVoltageTap().get(tapSelectionIndex).getCalibPoints().get(calibPointSelectionIndex).getOffsetValue();
									ApplicationLauncher.logger.debug("calculateNewVoltageGainOffset: newSetGainValue : " + newSetGainValue);
									ApplicationLauncher.logger.debug("calculateNewVoltageGainOffset: newSetOffsetValue : " + newSetOffsetValue);
									
/*									newSetGainValue = lscsCalibrationConfigFileUpdatedKeyValue.getVoltageCalibration().get(phaseSelectionIndex).getVoltageTap().get(tapSelectionIndex).getCalibPoints().get(calibPointSelectionIndex).getGainValue();
									newSetOffsetValue = lscsCalibrationConfigFileUpdatedKeyValue.getVoltageCalibration().get(phaseSelectionIndex).getVoltageTap().get(tapSelectionIndex).getCalibPoints().get(calibPointSelectionIndex).getOffsetValue();
									ApplicationLauncher.logger.debug("calculateNewVoltageGainOffset: newSetGainValue2 : " + newSetGainValue);
									ApplicationLauncher.logger.debug("calculateNewVoltageGainOffset: newSetOffsetValue2 : " + newSetOffsetValue);
									*/
									previousCalibPointVoltageValue = calibPointVoltageValue; 
									previouscalibPointRmsValue = calibPointRmsValue;
								}else{
									previousCalibPointVoltageValue = calibPointVoltageValue; 
									previouscalibPointRmsValue = calibPointRmsValue;
								}
							}
							
						}
					}

				}

			}

		}
		
/*		newSetGainValue = lscsCalibrationConfigFileUpdatedKeyValue.getVoltageCalibration().get(0).getVoltageTap().get(0).getCalibPoints().get(1).getGainValue();
		newSetOffsetValue = lscsCalibrationConfigFileUpdatedKeyValue.getVoltageCalibration().get(0).getVoltageTap().get(0).getCalibPoints().get(1).getOffsetValue();
		ApplicationLauncher.logger.debug("calculateNewVoltageGainOffset: newSetGainValue3 : " + newSetGainValue);
		ApplicationLauncher.logger.debug("calculateNewVoltageGainOffset: newSetOffsetValue3 : " + newSetOffsetValue);
		
		newSetGainValue = lscsCalibrationConfigFileUpdatedKeyValue.getVoltageCalibration().get(0).getVoltageTap().get(0).getCalibPoints().get(1).getGainValue();
		newSetOffsetValue = lscsCalibrationConfigFileUpdatedKeyValue.getVoltageCalibration().get(0).getVoltageTap().get(0).getCalibPoints().get(1).getOffsetValue();
		ApplicationLauncher.logger.debug("calculateNewVoltageGainOffset: newSetGainValue4 : " + newSetGainValue);
		ApplicationLauncher.logger.debug("calculateNewVoltageGainOffset: newSetOffsetValue4 : " + newSetOffsetValue);*/
		
	}
	
	
	
	
	public void calculateNewCurrentGainOffset(String selectedCurrentPhase){

		ApplicationLauncher.logger.debug("calculateNewCurrentGainOffset: Entry");

		double newSetGainValue = 0.0;
		double newSetOffsetValue = 0.0;
		//String selectedCurrentPhase = ref_cmBxCurrentBphaseSelected.getSelectionModel().getSelectedItem().toString();
		ApplicationLauncher.logger.debug("calculateNewCurrentGainOffset: selectedVoltPhase : " + selectedCurrentPhase);
		
		List<CurrentCalibration> currentCalibDataList = lscsCalibrationConfigFileUpdatedKeyValue.getCurrentCalibration();
		//List<CurrentCalibration> currentCalibDataList = lscsCalibrationConfigFileUpdatedKeyValue.getCurrentCalibration();
		for(int phaseSelectionIndex = 0; phaseSelectionIndex < currentCalibDataList.size();phaseSelectionIndex++){
			if(currentCalibDataList.get(phaseSelectionIndex).getCurrentPhase().equals(selectedCurrentPhase)){
				List<CurrentTap> currentTapList = currentCalibDataList.get(phaseSelectionIndex).getCurrentTap();
				if(currentTapList.size()>0){
					//ref_txtCurrentBphaseNoOfTapping.setText(String.valueOf(currentTapList.size()));
					//String tapSelectionCurrentRelayCode = currentTapList.get(0).getCurrentRelayId();
					//ref_txtCurrentBphaseTapSelectionRelayCode.setText(tapSelectionCurrentRelayCode);

					for(int tapSelectionIndex = 0; tapSelectionIndex <currentTapList.size();tapSelectionIndex++){
						//ref_cmBxCurrentBphaseTapMaxList.getItems().add(currentTapList.get(tapSelectionIndex).getCurrentTapMax());
						List<CalibPoints> currentCalibPoints = currentTapList.get(tapSelectionIndex).getCalibPoints();
						if(currentCalibPoints.size()>0){
							float previousCalibPointCurrentValue = -200.0f; 
							long previouscalibPointRmsValue = -300;
							double gainValue = 0;
							double offsetValue = 0;
							for(int calibPointSelectionIndex = 0; calibPointSelectionIndex <currentCalibPoints.size();calibPointSelectionIndex++){
								//ref_cmBxCurrentBphaseCalibPointNameList.getItems().add(currentCalibPoints.get(calibPointSelectionIndex).getCalibPointName());
								float calibPointCurrentValue = currentCalibPoints.get(calibPointSelectionIndex).getCalibPointValue(); 
								long calibPointRmsValue = currentCalibPoints.get(calibPointSelectionIndex).getRmsValue();
								if(calibPointSelectionIndex != 0){
									gainValue = Double.parseDouble( GuiUtils.calculateGain(previouscalibPointRmsValue, previousCalibPointCurrentValue,
												calibPointRmsValue,calibPointCurrentValue));
									ApplicationLauncher.logger.debug("calculateNewCurrentGainOffset: Index: " + calibPointSelectionIndex +" : gainValue : "  + gainValue);
									//gainValue = 1369.28;
									offsetValue = Double.parseDouble( GuiUtils.calculateOffset(previouscalibPointRmsValue, previousCalibPointCurrentValue,
											gainValue));
									ApplicationLauncher.logger.debug("calculateNewCurrentGainOffset: Index: " + calibPointSelectionIndex +" : offsetValue : "  + offsetValue);
									currentCalibPoints.get(calibPointSelectionIndex).setGainValue(gainValue);
									currentCalibPoints.get(calibPointSelectionIndex).setOffsetValue(offsetValue);
									currentCalibDataList.get(phaseSelectionIndex).getCurrentTap().get(tapSelectionIndex).getCalibPoints().get(calibPointSelectionIndex).setGainValue(gainValue);
									currentCalibDataList.get(phaseSelectionIndex).getCurrentTap().get(tapSelectionIndex).getCalibPoints().get(calibPointSelectionIndex).setOffsetValue(offsetValue);
									
									newSetGainValue = lscsCalibrationConfigFileUpdatedKeyValue.getCurrentCalibration().get(phaseSelectionIndex).getCurrentTap().get(tapSelectionIndex).getCalibPoints().get(calibPointSelectionIndex).getGainValue();
									newSetOffsetValue = lscsCalibrationConfigFileUpdatedKeyValue.getCurrentCalibration().get(phaseSelectionIndex).getCurrentTap().get(tapSelectionIndex).getCalibPoints().get(calibPointSelectionIndex).getOffsetValue();
									ApplicationLauncher.logger.debug("calculateNewCurrentGainOffset: newSetGainValue : " + newSetGainValue);
									ApplicationLauncher.logger.debug("calculateNewCurrentGainOffset: newSetOffsetValue : " + newSetOffsetValue);

									previousCalibPointCurrentValue = calibPointCurrentValue; 
									previouscalibPointRmsValue = calibPointRmsValue;
								}else{
									previousCalibPointCurrentValue = calibPointCurrentValue; 
									previouscalibPointRmsValue = calibPointRmsValue;
								}
							}
							
						}
					}

				}

			}

		}
		

		
	}
	
	
	

	
	
	@FXML 
	public void computeVoltageGainOffsetClick(){
		ApplicationLauncher.logger.debug("computeVoltageGainOffsetClick: Entry");
		if(ref_rdBtnVoltageAllPhase.isSelected()){
			calculateNewVoltageGainOffset(ConstantApp.FIRST_PHASE_DISPLAY_NAME);
			calculateNewVoltageGainOffset(ConstantApp.SECOND_PHASE_DISPLAY_NAME);
			calculateNewVoltageGainOffset(ConstantApp.THIRD_PHASE_DISPLAY_NAME);
		}else if(ref_rdBtnVoltageRphaseOnly.isSelected()){
			calculateNewVoltageGainOffset(ConstantApp.FIRST_PHASE_DISPLAY_NAME);

		}else if(ref_rdBtnVoltageYphaseOnly.isSelected()){
			calculateNewVoltageGainOffset(ConstantApp.SECOND_PHASE_DISPLAY_NAME);

		}else if(ref_rdBtnVoltageBphaseOnly.isSelected()){
			calculateNewVoltageGainOffset(ConstantApp.THIRD_PHASE_DISPLAY_NAME);

		}
		saveJsonFile();
		ApplicationLauncher.logger.info("computeVoltageGainOffsetClick: Voltage data update success :  Voltage Gain/Offset data updated and saved. Kindly reload json file to view the updated data.\n\nPlease ensure all calibrations are done before computing Gain/Offset - User prompted ");
		ApplicationLauncher.InformUser("Voltage data update success","Voltage Gain/Offset data updated and saved. Kindly reload json file to view the updated data.\n\nPlease ensure all calibrations are done before computing Gain/Offset", AlertType.WARNING);



	}
	
	
	@FXML 
	public void computeCurrentGainOffsetClick(){
		ApplicationLauncher.logger.debug("computeCurrentGainOffsetClick: Entry");
		if(ref_rdBtnCurrentAllPhase.isSelected()){
			calculateNewCurrentGainOffset(ConstantApp.FIRST_PHASE_DISPLAY_NAME);
			calculateNewCurrentGainOffset(ConstantApp.SECOND_PHASE_DISPLAY_NAME);
			calculateNewCurrentGainOffset(ConstantApp.THIRD_PHASE_DISPLAY_NAME);
		}else if(ref_rdBtnCurrentRphaseOnly.isSelected()){
			calculateNewCurrentGainOffset(ConstantApp.FIRST_PHASE_DISPLAY_NAME);

		}else if(ref_rdBtnCurrentYphaseOnly.isSelected()){
			calculateNewCurrentGainOffset(ConstantApp.SECOND_PHASE_DISPLAY_NAME);

		}else if(ref_rdBtnCurrentBphaseOnly.isSelected()){
			calculateNewCurrentGainOffset(ConstantApp.THIRD_PHASE_DISPLAY_NAME);

		}
		saveJsonFile();
		ApplicationLauncher.logger.info("computeCurrentGainOffsetClick: Current data update success :  Current Gain/Offset data updated and saved. Kindly reload json file to view the updated data.\n\nPlease ensure all calibrations are done before computing Gain/Offset - User prompted ");
		ApplicationLauncher.InformUser("Current data update success","Current Gain/Offset data updated and saved. Kindly reload json file to view the updated data.\n\nPlease ensure all calibrations are done before computing Gain/Offset", AlertType.WARNING);



	}
	
	

	@FXML 
	public void updateVoltageJsonFileClick(){
		ApplicationLauncher.logger.debug("updateVoltageJsonFileClick: Entry");
		boolean voltageRphaseUpdateStatus = false;
		if(ref_rdBtnVoltageAllPhase.isSelected()){
			voltageRphaseUpdateStatus = updateJsonVoltageRphaseData();
			voltageRphaseUpdateStatus = updateJsonVoltageYphaseData();
			voltageRphaseUpdateStatus = updateJsonVoltageBphaseData();
		}else if(ref_rdBtnVoltageRphaseOnly.isSelected()){
			voltageRphaseUpdateStatus = updateJsonVoltageRphaseData(); //1

		}else if(ref_rdBtnVoltageYphaseOnly.isSelected()){
			voltageRphaseUpdateStatus = updateJsonVoltageYphaseData();

		}else if(ref_rdBtnVoltageBphaseOnly.isSelected()){
			voltageRphaseUpdateStatus = updateJsonVoltageBphaseData();

		}
		if(voltageRphaseUpdateStatus ){
			saveJsonFile();
			ApplicationLauncher.logger.info("updateVoltageJsonFileClick: Json update success : Json voltage update success- User prompted ");
			//ApplicationLauncher.InformUser("Json update success","Json current update success!!", AlertType.INFORMATION);
			if(ProcalFeatureEnable.AUTO_CALIBRATION_MODE_ENABLE_FEATURE) {
				String message = "Json current update success. Do you want to refresh the source?";
				boolean isRefreshSourceRequested = promptUserforRefreshSource(message);
				ProjectExecutionController projectControl = new ProjectExecutionController();
				if (isRefreshSourceRequested) {
					projectControl.sourceRefreshClick();
				} else {
					message = "Do you want to load next test point?";
					boolean isLoadNextRequested = promptUserforRefreshSource(message);
					if(isLoadNextRequested){
						projectControl.loadNextClick();
					}
				}
				reloadJsonFileClick();
			} else {
				ApplicationLauncher.InformUser("Json update success","Json current update success!!", AlertType.INFORMATION);
			}
		}else {
			ApplicationLauncher.logger.info("updateVoltageJsonFileClick: Json update failed : Json update failed due to Voltage data failure. Kindly check the logs!!- User prompted ");
			ApplicationLauncher.InformUser("Json update failed","Json update failed due to Voltage data failure. Kindly check the logs!!", AlertType.ERROR);
		}
		//voltageUpdateStatus = updateJsonVoltageData();
		/*		currentUpdateStatus = updateJsonCurrentData();
		if(voltageUpdateStatus && currentUpdateStatus){
			ApplicationLauncher.logger.info("updateVoltageJsonFileClick: Json update success : Json update success- User prompted ");
			ApplicationLauncher.InformUser("Json update success","Json update success!!", AlertType.INFORMATION);
		}else if ( (!voltageUpdateStatus) && (!currentUpdateStatus) ){
			ApplicationLauncher.logger.info("updateVoltageJsonFileClick: Json update failed : Json update failed due to both Voltage and Current data failure. Kindly check the logs!!- User prompted ");
			ApplicationLauncher.InformUser("Json update failed","Json update failed due to both Voltage and Current data failure. Kindly check the logs!!", AlertType.ERROR);
		}else if  (!voltageUpdateStatus) {
			ApplicationLauncher.logger.info("updateVoltageJsonFileClick: Json update failed : Json update failed due to Voltage data failure. Kindly check the logs!!- User prompted ");
			ApplicationLauncher.InformUser("Json update failed","Json update failed due to Voltage data failure. Kindly check the logs!!", AlertType.ERROR);
		}else {
			ApplicationLauncher.logger.info("updateVoltageJsonFileClick: Json update failed : Json update failed due to Current data failure. Kindly check the logs!!- User prompted ");
			ApplicationLauncher.InformUser("Json update failed","Json update failed due to Current data failure. Kindly check the logs!!", AlertType.ERROR);
		}*/
		

	}

	@FXML 
	public void updateCurrentJsonFileClick(){
		ApplicationLauncher.logger.debug("updateCurrentJsonFileClick: Entry");
		boolean currentRphaseUpdateStatus = false;
		//voltageUpdateStatus = updateJsonVoltageData();
		if(ref_rdBtnCurrentAllPhase.isSelected()){
			currentRphaseUpdateStatus = updateJsonCurrentRphaseData();
			currentRphaseUpdateStatus = updateJsonCurrentYphaseData();
			currentRphaseUpdateStatus = updateJsonCurrentBphaseData();
		}else if(ref_rdBtnCurrentRphaseOnly.isSelected()){
			currentRphaseUpdateStatus = updateJsonCurrentRphaseData();

		}else if(ref_rdBtnCurrentYphaseOnly.isSelected()){
			currentRphaseUpdateStatus = updateJsonCurrentYphaseData();

		}else if(ref_rdBtnCurrentBphaseOnly.isSelected()){
			currentRphaseUpdateStatus = updateJsonCurrentBphaseData();

		}
		if(currentRphaseUpdateStatus ){
			saveJsonFile();
			ApplicationLauncher.logger.info("updateCurrentJsonFileClick: Json update success : Json current update success- User prompted ");
			if(ProcalFeatureEnable.AUTO_CALIBRATION_MODE_ENABLE_FEATURE){
				String message = "Json current update success. Do you want to refresh the source?";
				boolean isRefreshSourceRequested = promptUserforRefreshSource(message);
				ProjectExecutionController projectControl = new ProjectExecutionController();
				if(isRefreshSourceRequested){
					
					projectControl.sourceRefreshClick();
					
				}else {
					message = "Do you want to load next test point?";
					boolean isLoadNextRequested = promptUserforRefreshSource(message);
					if(isLoadNextRequested){
						projectControl.loadNextClick();
					}
				}
				//saveCurrentCalibrationGuiObjectValues();
				reloadJsonFileClick();
				//restoreCurrentCalibrationGuiObjectValues();
			}else {
				ApplicationLauncher.InformUser("Json update success","Json current update success!!", AlertType.INFORMATION);
			}
			
		}else {
			ApplicationLauncher.logger.info("updateCurrentJsonFileClick: Json update failed : Json update failed due to Current data failure. Kindly check the logs!!- User prompted ");
			ApplicationLauncher.InformUser("Json update failed","Json update failed due to Current data failure. Kindly check the logs!!", AlertType.ERROR);
		}
		//saveJsonFile();

	}
	
	public void saveCurrentCalibrationGuiObjectValues() {
		
		
		setLastRdBtnCurrentRphaseOnlyValue(ref_rdBtnCurrentRphaseOnly.isSelected());
		setLastRdBtnCurrentYphaseOnlyValue(ref_rdBtnCurrentYphaseOnly.isSelected());
		setLastRdBtnCurrentBphaseOnlyValue(ref_rdBtnCurrentBphaseOnly.isSelected());
		setLastrdBtnCurrentAllPhase(ref_rdBtnCurrentAllPhase.isSelected());
	}
	
	public void restoreCurrentCalibrationGuiObjectValues() {
		
		Platform.runLater(()->{
			ref_rdBtnCurrentRphaseOnly.setSelected(isLastRdBtnCurrentRphaseOnlyValue());
			ref_rdBtnCurrentYphaseOnly.setSelected(isLastRdBtnCurrentYphaseOnlyValue());
			ref_rdBtnCurrentBphaseOnly.setSelected(isLastRdBtnCurrentBphaseOnlyValue());
			ref_rdBtnCurrentAllPhase.setSelected(isLastrdBtnCurrentAllPhase());
			 
			
/*			ref_cmBxCurrentRphaseSelected.getSelectionModel().select(getLastCmBxCurrentRphaseSelected());
			ref_txtCurrentRphaseNoOfTapping.setText(getLastTxtCurrentRphaseNoOfTapping());
			ref_cmBxCurrentRphaseTapMaxList.getSelectionModel().select(getLastCmBxCurrentRphaseTapMaxList());
			ref_txtCurrentRphaseTapSelectionRelayCode.setText(getLastTxtCurrentRphaseTapSelectionRelayCode());
			ref_txtCurrentRphaseNoOfCalibPointEachTap.setText(getLastTxtCurrentRphaseNoOfCalibPointEachTap());
			ref_cmBxCurrentRphaseCalibPointNameList.getSelectionModel().select(getLastCmBxCurrentRphaseCalibPointNameList());
			ref_txtCurrentRphaseCalibPointValue.setText(getLastTxtCurrentRphaseCalibPointValue());*/
		});
		
		
	}

	public void setLastRdBtnCurrentRphaseOnlyValue(boolean selected) {
		
		this.lastRdBtnCurrentRphaseOnlyValue = selected;
	}
	
	public boolean isLastRdBtnCurrentRphaseOnlyValue() {
		
		return this.lastRdBtnCurrentRphaseOnlyValue;
	}

	public boolean promptUserforRefreshSource(String message){
		
		//String message = "Json current update success. Do you want to refresh the source?";
		Alert alert = new Alert(AlertType.CONFIRMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
		alert.setTitle("Close Project");
		alert.setHeaderText("Confirmation");
		alert.setContentText(message);

		alert.getButtonTypes().clear();
		alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

		// Deactivate Defaultbehavior for yes-Button:
		Button yesButton = (Button) alert.getDialogPane().lookupButton(ButtonType.YES);
		yesButton.setDefaultButton(false);

		// Activate Defaultbehavior for no-Button:
		Button noButton = (Button) alert.getDialogPane().lookupButton(ButtonType.NO);
		noButton.setDefaultButton(true);

		final Optional<ButtonType> result = alert.showAndWait();
		return result.get() == ButtonType.YES;
		
		
	}


	public boolean updateJsonVoltageRphaseData(){
		ApplicationLauncher.logger.debug("updateJsonVoltageRphaseData: Entry");
		String selectedVoltagePhase = ref_cmBxVoltageRphaseSelected.getSelectionModel().getSelectedItem().toString();
		String newVoltageRmsValue = ref_txtVoltageRphaseNewCalcRmsValue.getText();
		ApplicationLauncher.logger.debug("updateJsonVoltageRphaseData: selectedVoltPhase : " + selectedVoltagePhase);
		ApplicationLauncher.logger.debug("updateJsonVoltageRphaseData: newVoltageRmsValue : " + newVoltageRmsValue);
		List<VoltageCalibration> voltageCalibDataList = lscsCalibrationConfigFileUpdatedKeyValue.getVoltageCalibration();
		for(int phaseSelectionIndex = 0; phaseSelectionIndex < voltageCalibDataList.size();phaseSelectionIndex++){
			if(voltageCalibDataList.get(phaseSelectionIndex).getVoltagePhase().equals(selectedVoltagePhase)){
				List<VoltageTap> voltageTapList = voltageCalibDataList.get(phaseSelectionIndex).getVoltageTap();
				if(voltageTapList.size()>0){
					//ref_txtVoltNoOfTapping.setText(String.valueOf(voltageTapList.size()));
					//String tapSelectionVoltageRelayCode = voltageTapList.get(0).getVoltageRelayId();
					//ref_txtVoltTapSelectionRelayCode.setText(tapSelectionVoltageRelayCode);
					float selectedVoltageTap = Float.parseFloat(ref_cmBxVoltageRphaseTapMaxList.getSelectionModel().getSelectedItem().toString());
					ApplicationLauncher.logger.debug("updateJsonVoltageRphaseData: selectedVoltageTap : " + selectedVoltageTap);
					for(int tapSelectionIndex = 0; tapSelectionIndex <voltageTapList.size();tapSelectionIndex++){
						//ref_cmBxVoltTapMaxList.getItems().add(voltageTapList.get(tapSelectionIndex).getVoltageTapMax());
						//List<CalibPoints> voltCalibPoints = voltageTapList.get(tapSelectionIndex).getCalibPoints();
						if(selectedVoltageTap == voltageTapList.get(tapSelectionIndex).getVoltageTapMax()){
							List<CalibPoints> voltageCalibPoints = voltageTapList.get(tapSelectionIndex).getCalibPoints();
							if(voltageCalibPoints.size()>0){
								float selectedCalibPointValue = Float.parseFloat(ref_txtVoltageRphaseCalibPointValue.getText());
								ApplicationLauncher.logger.debug("updateJsonVoltageRphaseData: selectedCalibPointValue : " + selectedCalibPointValue);
								for(int calibPointSelectionIndex = 0; calibPointSelectionIndex <voltageCalibPoints.size();calibPointSelectionIndex++){

									if(selectedCalibPointValue == voltageCalibPoints.get(calibPointSelectionIndex).getCalibPointValue()){
										ApplicationLauncher.logger.debug("updateJsonVoltageRphaseData: newVoltageRmsValue set success: ");
										voltageCalibPoints.get(calibPointSelectionIndex).setRmsValue(Long.valueOf(newVoltageRmsValue));
										//ApplicationLauncher.logger.debug("updateJsonVoltageRphaseData: new set  getRmsValue: " + voltageCalibPoints.get(calibPointSelectionIndex).getRmsValue());
										//ApplicationLauncher.InformUser("Json update success","Json update success!!", AlertType.INFORMATION);
										return true;
									}

									//ref_cmBxVoltCalibPointNameList.getItems().add(voltageCalibPoints.get(calibPointSelectionIndex).getCalibPointName());
								}

							}
						}
					}
				}

			}

		}
		//ApplicationLauncher.InformUser("Json update failed","Json update failed due to data failure. Kindly check the logs!!", AlertType.ERROR);
		//ApplicationLauncher.logger.debug("updateJsonData: data not found : Failed");
		return false;
	}



	public boolean updateJsonVoltageYphaseData(){
		ApplicationLauncher.logger.debug("updateJsonVoltageYphaseData: Entry");
		String selectedVoltagePhase = ref_cmBxVoltageYphaseSelected.getSelectionModel().getSelectedItem().toString();
		String newVoltageRmsValue = ref_txtVoltageYphaseNewCalcRmsValue.getText();
		ApplicationLauncher.logger.debug("updateJsonVoltageYphaseData: selectedVoltPhase : " + selectedVoltagePhase);
		ApplicationLauncher.logger.debug("updateJsonVoltageYphaseData: newVoltageRmsValue : " + newVoltageRmsValue);
		List<VoltageCalibration> voltageCalibDataList = lscsCalibrationConfigFileUpdatedKeyValue.getVoltageCalibration();
		for(int phaseSelectionIndex = 0; phaseSelectionIndex < voltageCalibDataList.size();phaseSelectionIndex++){
			if(voltageCalibDataList.get(phaseSelectionIndex).getVoltagePhase().equals(selectedVoltagePhase)){
				List<VoltageTap> voltageTapList = voltageCalibDataList.get(phaseSelectionIndex).getVoltageTap();
				if(voltageTapList.size()>0){
					//ref_txtVoltNoOfTapping.setText(String.valueOf(voltageTapList.size()));
					//String tapSelectionVoltageRelayCode = voltageTapList.get(0).getVoltageRelayId();
					//ref_txtVoltTapSelectionRelayCode.setText(tapSelectionVoltageRelayCode);
					float selectedVoltageTap = Float.parseFloat(ref_cmBxVoltageYphaseTapMaxList.getSelectionModel().getSelectedItem().toString());
					ApplicationLauncher.logger.debug("updateJsonVoltageYphaseData: selectedVoltageTap : " + selectedVoltageTap);
					for(int tapSelectionIndex = 0; tapSelectionIndex <voltageTapList.size();tapSelectionIndex++){
						//ref_cmBxVoltTapMaxList.getItems().add(voltageTapList.get(tapSelectionIndex).getVoltageTapMax());
						//List<CalibPoints> voltCalibPoints = voltageTapList.get(tapSelectionIndex).getCalibPoints();
						if(selectedVoltageTap == voltageTapList.get(tapSelectionIndex).getVoltageTapMax()){
							List<CalibPoints> voltageCalibPoints = voltageTapList.get(tapSelectionIndex).getCalibPoints();
							if(voltageCalibPoints.size()>0){
								float selectedCalibPointValue = Float.parseFloat(ref_txtVoltageYphaseCalibPointValue.getText());
								ApplicationLauncher.logger.debug("updateJsonVoltageYphaseData: selectedCalibPointValue : " + selectedCalibPointValue);
								for(int calibPointSelectionIndex = 0; calibPointSelectionIndex <voltageCalibPoints.size();calibPointSelectionIndex++){

									if(selectedCalibPointValue == voltageCalibPoints.get(calibPointSelectionIndex).getCalibPointValue()){
										ApplicationLauncher.logger.debug("updateJsonVoltageYphaseData: newVoltageRmsValue set success: ");
										voltageCalibPoints.get(calibPointSelectionIndex).setRmsValue(Long.valueOf(newVoltageRmsValue));
										//ApplicationLauncher.logger.debug("updateJsonVoltageYphaseData: new set  getRmsValue: " + voltageCalibPoints.get(calibPointSelectionIndex).getRmsValue());
										//ApplicationLauncher.InformUser("Json update success","Json update success!!", AlertType.INFORMATION);
										return true;
									}

									//ref_cmBxVoltCalibPointNameList.getItems().add(voltageCalibPoints.get(calibPointSelectionIndex).getCalibPointName());
								}

							}
						}
					}
				}

			}

		}
		//ApplicationLauncher.InformUser("Json update failed","Json update failed due to data failure. Kindly check the logs!!", AlertType.ERROR);
		//ApplicationLauncher.logger.debug("updateJsonData: data not found : Failed");
		return false;
	}



	public boolean updateJsonVoltageBphaseData(){
		ApplicationLauncher.logger.debug("updateJsonVoltageBphaseData: Entry");
		String selectedVoltagePhase = ref_cmBxVoltageBphaseSelected.getSelectionModel().getSelectedItem().toString();
		String newVoltageRmsValue = ref_txtVoltageBphaseNewCalcRmsValue.getText();
		ApplicationLauncher.logger.debug("updateJsonVoltageBphaseData: selectedVoltPhase : " + selectedVoltagePhase);
		ApplicationLauncher.logger.debug("updateJsonVoltageBphaseData: newVoltageRmsValue : " + newVoltageRmsValue);
		List<VoltageCalibration> voltageCalibDataList = lscsCalibrationConfigFileUpdatedKeyValue.getVoltageCalibration();
		for(int phaseSelectionIndex = 0; phaseSelectionIndex < voltageCalibDataList.size();phaseSelectionIndex++){
			if(voltageCalibDataList.get(phaseSelectionIndex).getVoltagePhase().equals(selectedVoltagePhase)){
				List<VoltageTap> voltageTapList = voltageCalibDataList.get(phaseSelectionIndex).getVoltageTap();
				if(voltageTapList.size()>0){
					//ref_txtVoltNoOfTapping.setText(String.valueOf(voltageTapList.size()));
					//String tapSelectionVoltageRelayCode = voltageTapList.get(0).getVoltageRelayId();
					//ref_txtVoltTapSelectionRelayCode.setText(tapSelectionVoltageRelayCode);
					float selectedVoltageTap = Float.parseFloat(ref_cmBxVoltageBphaseTapMaxList.getSelectionModel().getSelectedItem().toString());
					ApplicationLauncher.logger.debug("updateJsonVoltageBphaseData: selectedVoltageTap : " + selectedVoltageTap);
					for(int tapSelectionIndex = 0; tapSelectionIndex <voltageTapList.size();tapSelectionIndex++){
						//ref_cmBxVoltTapMaxList.getItems().add(voltageTapList.get(tapSelectionIndex).getVoltageTapMax());
						//List<CalibPoints> voltCalibPoints = voltageTapList.get(tapSelectionIndex).getCalibPoints();
						if(selectedVoltageTap == voltageTapList.get(tapSelectionIndex).getVoltageTapMax()){
							List<CalibPoints> voltageCalibPoints = voltageTapList.get(tapSelectionIndex).getCalibPoints();
							if(voltageCalibPoints.size()>0){
								float selectedCalibPointValue = Float.parseFloat(ref_txtVoltageBphaseCalibPointValue.getText());
								ApplicationLauncher.logger.debug("updateJsonVoltageBphaseData: selectedCalibPointValue : " + selectedCalibPointValue);
								for(int calibPointSelectionIndex = 0; calibPointSelectionIndex <voltageCalibPoints.size();calibPointSelectionIndex++){

									if(selectedCalibPointValue == voltageCalibPoints.get(calibPointSelectionIndex).getCalibPointValue()){
										ApplicationLauncher.logger.debug("updateJsonVoltageBphaseData: newVoltageRmsValue set success: ");
										voltageCalibPoints.get(calibPointSelectionIndex).setRmsValue(Long.valueOf(newVoltageRmsValue));
										//ApplicationLauncher.logger.debug("updateJsonVoltageBphaseData: new set  getRmsValue: " + voltageCalibPoints.get(calibPointSelectionIndex).getRmsValue());
										//ApplicationLauncher.InformUser("Json update success","Json update success!!", AlertType.INFORMATION);
										return true;
									}

									//ref_cmBxVoltCalibPointNameList.getItems().add(voltageCalibPoints.get(calibPointSelectionIndex).getCalibPointName());
								}

							}
						}
					}
				}

			}

		}
		//ApplicationLauncher.InformUser("Json update failed","Json update failed due to data failure. Kindly check the logs!!", AlertType.ERROR);
		//ApplicationLauncher.logger.debug("updateJsonData: data not found : Failed");
		return false;
	}

	public void saveVoltageGuiSelection(){
		ApplicationLauncher.logger.debug("saveVoltageGuiSelection: Entry");
		if(ref_cmBxVoltageRphaseSelected.getSelectionModel().getSelectedItem()!=null){
			//ApplicationLauncher.logger.debug("saveVoltageGuiSelection: Entry2" + ref_cmBxVoltageRphaseSelected.getSelectionModel().getSelectedItem().toString());
			setLastGuiVoltageRphaseSelected(ref_cmBxVoltageRphaseSelected.getSelectionModel().getSelectedItem().toString());
		}
		
		if(ref_cmBxVoltageRphaseTapMaxList.getSelectionModel().getSelectedItem()!=null){
			//ApplicationLauncher.logger.debug("saveVoltageGuiSelection: Entry2" + cmBxVoltageRphaseTapMaxList.getSelectionModel().getSelectedItem().toString());
			setLastGuiVoltageRphaseTapMaxList(ref_cmBxVoltageRphaseTapMaxList.getSelectionModel().getSelectedItem().toString());
		}
		
		if(ref_cmBxVoltageRphaseCalibPointNameList.getSelectionModel().getSelectedItem()!=null){
			//ApplicationLauncher.logger.debug("saveVoltageGuiSelection: Entry2" + cmBxVoltageRphaseTapMaxList.getSelectionModel().getSelectedItem().toString());
			setLastGuiVoltageRphaseCalibPointNameList(ref_cmBxVoltageRphaseCalibPointNameList.getSelectionModel().getSelectedItem().toString());
		}
	}
	
	public void restoreVoltageGuiSelection(){
		ApplicationLauncher.logger.debug("restoreVoltageGuiSelection: Entry");
		//ApplicationLauncher.logger.debug("restoreVoltageGuiSelection: getLastGuiVoltageRphaseSelected(): " + getLastGuiVoltageRphaseSelected());
		if(!getLastGuiVoltageRphaseSelected().isEmpty()){
			Platform.runLater(() -> {
				ref_cmBxVoltageRphaseSelected.getSelectionModel().select(getLastGuiVoltageRphaseSelected());
			});
/*			Sleep(500);
			Platform.runLater(() -> {
				ref_cmBxVoltageRphaseTapMaxList.getSelectionModel().select(getLastGuiVoltageRphaseTapMaxList());
			});
			Sleep(500);
			Platform.runLater(() -> {
				ref_cmBxVoltageRphaseCalibPointNameList.getSelectionModel().select(getLastGuiVoltageRphaseCalibPointNameList());
			});*/
		}
	}
	
	public void saveCurrentGuiSelection(){
		ApplicationLauncher.logger.debug("saveCurrentGuiSelection: Entry");
		saveCurrentCalibrationGuiObjectValues();
		if(ref_cmBxCurrentRphaseSelected.getSelectionModel().getSelectedItem()!=null){
			//ApplicationLauncher.logger.debug("saveCurrentGuiSelection: Entry2" + ref_cmBxCurrentRphaseSelected.getSelectionModel().getSelectedItem().toString());
			setLastGuiCurrentRphaseSelected(ref_cmBxCurrentRphaseSelected.getSelectionModel().getSelectedItem().toString());
		}
		
/*		if(ref_cmBxCurrentRphaseTapMaxList.getSelectionModel().getSelectedItem()!=null){
			//ApplicationLauncher.logger.debug("saveVoltageGuiSelection: Entry2" + cmBxCurrentRphaseTapMaxList.getSelectionModel().getSelectedItem().toString());
			setLastGuiCurrentRphaseTapMaxList(ref_cmBxCurrentRphaseTapMaxList.getSelectionModel().getSelectedItem().toString());
		}
		
		if(ref_cmBxCurrentRphaseCalibPointNameList.getSelectionModel().getSelectedItem()!=null){
			//ApplicationLauncher.logger.debug("saveVoltageGuiSelection: Entry2" + cmBxCurrentRphaseTapMaxList.getSelectionModel().getSelectedItem().toString());
			setLastGuiCurrentRphaseCalibPointNameList(ref_cmBxCurrentRphaseCalibPointNameList.getSelectionModel().getSelectedItem().toString());
		}*/
	}
	
	public void restoreCurrentGuiSelection(){
		ApplicationLauncher.logger.debug("restoreCurrentGuiSelection: Entry");
		//ApplicationLauncher.logger.debug("restoreCurrentGuiSelection: getLastGuiCurrentRphaseSelected(): " + getLastGuiCurrentRphaseSelected());
		if(!getLastGuiCurrentRphaseSelected().isEmpty()){
			//Platform.runLater(() -> {
				ref_cmBxCurrentRphaseSelected.getSelectionModel().select(getLastGuiCurrentRphaseSelected());
				
			//});
/*			Sleep(500);
			Platform.runLater(() -> {
				ref_cmBxCurrentRphaseTapMaxList.getSelectionModel().select(getLastGuiCurrentRphaseTapMaxList());
			});
			Sleep(500);
			Platform.runLater(() -> {
				ref_cmBxCurrentRphaseCalibPointNameList.getSelectionModel().select(getLastGuiCurrentRphaseCalibPointNameList());
			});*/
		}
		restoreCurrentCalibrationGuiObjectValues();
		
	}
	
	@FXML 
	public void saveJsonFileDisplay(){
		ApplicationLauncher.logger.debug("saveJsonFileDisplay: Entry");

	    
	    try {
	    	
			FileChooser fileChooser = new FileChooser();
			
			fileChooser.setInitialDirectory(new  File(lastVisitedDirectory));
			fileChooser.getExtensionFilters().add(new ExtensionFilter("JSON Files", "*.json"));
			fileChooser.setTitle("Save json file");
		    //File file = fileChooser.showOpenDialog(ApplicationLauncher.getPrimaryStage().getScene().getWindow());
		    File file = fileChooser.showSaveDialog(ApplicationLauncher.getPrimaryStage().getScene().getWindow());
		    lastVisitedDirectory=(file!=null )?file.getParent():System.getProperty("user.home");
	    	if(file!=null){
	    		ApplicationLauncher.logger.debug("saveJsonFileDisplay : file: " + file.toString());
	    		file.createNewFile();
	    		Path path = Paths.get(file.toString());
	    		BufferedWriter bw = null;
	    		OutputStream out = null;
	    		GsonBuilder builder = new GsonBuilder(); 
	    		Gson gson = builder.setPrettyPrinting().create();
	    		out = new BufferedOutputStream( Files.newOutputStream(path )); 
	    		bw = new BufferedWriter(new OutputStreamWriter(out));
	    		String indentedJsonCalibConfigFileData = gson.toJson(lscsCalibrationConfigFileUpdatedKeyValue);
	    		bw.write(indentedJsonCalibConfigFileData);
	    		bw.close();
	    		ApplicationLauncher.logger.debug("saveJsonFileDisplay: file saved success" );
	    		ApplicationLauncher.InformUser("Json save success",file.toString() + " file saved successfully", AlertType.INFORMATION);


	    		//importDataTimer = new Timer();
	    		//importDataTimer.schedule(new importDeploymentDataTask(file.toString()),100);
	    	}
	    }catch (Exception e1) {
	    	
	    	e1.printStackTrace();
	    	ApplicationLauncher.logger.info("saveJsonFileDisplay: Exception: " + e1.getMessage());
	    	ApplicationLauncher.logger.debug("saveJsonFileDisplay: file saved failed" );
	    	ApplicationLauncher.InformUser("Json save failed","json file save failed!", AlertType.ERROR);
	    } 
	    /*		Platform.runLater(() -> {
			ref_txtAreaJsonFileDisplay.clear();
		});*/
		
/*		File selectedFile = FileChooser.showSaveDialog(ApplicationLauncher.getPrimaryStage());
		if (selectedFile != null) {
		    // dialog closed by selecting a file to save the data to

		    // write data here yourself, e.g.
		    try (BufferedReader br = Files.newBufferedReader(selectedFile.toPath(), StandardCharsets.UTF_8)) {
		        br.write("Hello World!\n");
		    }
		}*/
	}
	
	
	@FXML 
	public void clearJsonFileDisplay(){
		ApplicationLauncher.logger.debug("clearJsonFileDisplay: Entry");
		Platform.runLater(() -> {
			ref_txtAreaJsonFileDisplay.clear();
		});
	}
	
	@FXML 
	public void titledPaneViewJsonFileOnClick(){
		ApplicationLauncher.logger.debug("titledPaneViewJsonFileOnClick: Entry");
		clearJsonFileDisplay();
		
	}
	
	@FXML 
	public void viewJsonFileClick(){
		ApplicationLauncher.logger.debug("viewJsonFileClick: Entry");
		try {
			GsonBuilder builder = new GsonBuilder(); 
			Gson gson = builder.setPrettyPrinting().create();
			String indentedJsonCalibConfigFileData = gson.toJson(lscsCalibrationConfigFileUpdatedKeyValue);

			Platform.runLater(() -> {
				ref_txtAreaJsonFileDisplay.setText(indentedJsonCalibConfigFileData);
			});

		} catch (Exception e1) {
			
			e1.printStackTrace();
			ApplicationLauncher.logger.info("viewJsonFileClick: Exception: " + e1.getMessage());
		} 

	}

	@FXML 
	public void reloadJsonFileClick(){
		ApplicationLauncher.logger.debug("reloadJsonFileClick: Entry");
		
		
		
		saveVoltageGuiSelection();
		saveCurrentGuiSelection();
		LscsCalibrationConfigLoader.init();
		initInProgress = true;
		voltageRphaseSelectionChangeGuiInProgress = true;
		voltageRphaseTapMaxChangeGuiInProgress = true;
		voltageYphaseSelectionChangeGuiInProgress = true;
		voltageYphaseTapMaxChangeGuiInProgress = true;
		voltageBphaseSelectionChangeGuiInProgress = true;
		voltageBphaseTapMaxChangeGuiInProgress = true;

		currentRphaseSelectionChangeGuiInProgress = true;
		currentRphaseTapMaxChangeGuiInProgress = true;
		currentYphaseSelectionChangeGuiInProgress = true;
		currentYphaseTapMaxChangeGuiInProgress = true;
		currentBphaseSelectionChangeGuiInProgress = true;
		currentBphaseTapMaxChangeGuiInProgress = true;
		//clearVoltageGui();
		clearVoltageRphaseGui();
		clearVoltageYphaseGui();
		clearVoltageBphaseGui();
		clearCurrentRphaseGui();
		clearCurrentYphaseGui();
		clearCurrentBphaseGui();
		initInProgress = false;
		voltageRphaseSelectionChangeGuiInProgress = false;
		voltageRphaseTapMaxChangeGuiInProgress = false;
		voltageYphaseSelectionChangeGuiInProgress = false;
		voltageYphaseTapMaxChangeGuiInProgress = false;
		voltageBphaseSelectionChangeGuiInProgress = false;
		voltageBphaseTapMaxChangeGuiInProgress = false;
		currentRphaseSelectionChangeGuiInProgress = false;
		currentRphaseTapMaxChangeGuiInProgress = false;
		currentYphaseSelectionChangeGuiInProgress = false;
		currentYphaseTapMaxChangeGuiInProgress = false;
		currentBphaseSelectionChangeGuiInProgress = false;
		currentBphaseTapMaxChangeGuiInProgress = false;
		initGuiObjects();
		restoreVoltageGuiSelection();
		restoreCurrentGuiSelection();
		
		
	}

	@FXML 
	public void calculateNewCurrentRmsClick(){
		ApplicationLauncher.logger.debug("calculateNewCurrentRmsClick : Entry");
		if(ref_rdBtnCurrentAllPhase.isSelected()){

			calculateNewCurrentRphaseRms();
			calculateNewCurrentYphaseRms();
			calculateNewCurrentBphaseRms();
		}else if (ref_rdBtnCurrentRphaseOnly.isSelected()){
			calculateNewCurrentRphaseRms();

		}else if (ref_rdBtnCurrentYphaseOnly.isSelected()){
			calculateNewCurrentYphaseRms();

		}else if (ref_rdBtnCurrentBphaseOnly.isSelected()){
			calculateNewCurrentBphaseRms();

		}

	}







	public boolean updateJsonCurrentRphaseData(){
		ApplicationLauncher.logger.debug("updateJsonCurrentRphaseData: Entry");
		String selectedCurrentPhase = ref_cmBxCurrentRphaseSelected.getSelectionModel().getSelectedItem().toString();
		String newCurrentRmsValue = ref_txtCurrentRphaseNewCalcRmsValue.getText();
		ApplicationLauncher.logger.debug("updateJsonCurrentRphaseData: selectedCurrentPhase : " + selectedCurrentPhase);
		ApplicationLauncher.logger.debug("updateJsonCurrentRphaseData: newCurrentRmsValue : " + newCurrentRmsValue);
		List<CurrentCalibration> currentCalibDataList = lscsCalibrationConfigFileUpdatedKeyValue.getCurrentCalibration();
		for(int phaseSelectionIndex = 0; phaseSelectionIndex < currentCalibDataList.size();phaseSelectionIndex++){
			if(currentCalibDataList.get(phaseSelectionIndex).getCurrentPhase().equals(selectedCurrentPhase)){
				List<CurrentTap> currentTapList = currentCalibDataList.get(phaseSelectionIndex).getCurrentTap();
				if(currentTapList.size()>0){
					//ref_txtCurrentNoOfTapping.setText(String.valueOf(currentTapList.size()));
					//String tapSelectionCurrentRelayCode = currentTapList.get(0).getCurrentRelayId();
					//ref_txtCurrentTapSelectionRelayCode.setText(tapSelectionCurrentRelayCode);
					float selectedCurrentTap = Float.parseFloat(ref_cmBxCurrentRphaseTapMaxList.getSelectionModel().getSelectedItem().toString());
					ApplicationLauncher.logger.debug("updateJsonCurrentRphaseData: selectedCurrentTap : " + selectedCurrentTap);
					for(int tapSelectionIndex = 0; tapSelectionIndex <currentTapList.size();tapSelectionIndex++){
						//ref_cmBxCurrentTapMaxList.getItems().add(currentTapList.get(tapSelectionIndex).getCurrentTapMax());
						//List<CalibPoints> voltCalibPoints = currentTapList.get(tapSelectionIndex).getCalibPoints();
						if(selectedCurrentTap == currentTapList.get(tapSelectionIndex).getCurrentTapMax()){
							List<CalibPoints> currentCalibPoints = currentTapList.get(tapSelectionIndex).getCalibPoints();
							if(currentCalibPoints.size()>0){
								float selectedCalibPointValue = Float.parseFloat(ref_txtCurrentRphaseCalibPointValue.getText());
								ApplicationLauncher.logger.debug("updateJsonCurrentRphaseData: selectedCalibPointValue : " + selectedCalibPointValue);
								for(int calibPointSelectionIndex = 0; calibPointSelectionIndex <currentCalibPoints.size();calibPointSelectionIndex++){

									if(selectedCalibPointValue == currentCalibPoints.get(calibPointSelectionIndex).getCalibPointValue()){
										ApplicationLauncher.logger.debug("updateJsonCurrentRphaseData: newCurrentRmsValue set success: ");
										currentCalibPoints.get(calibPointSelectionIndex).setRmsValue(Long.valueOf(newCurrentRmsValue));
										//ApplicationLauncher.logger.debug("updateJsonCurrentRphaseData: new set  getRmsValue: " + currentCalibPoints.get(calibPointSelectionIndex).getRmsValue());
										//ApplicationLauncher.InformUser("Json update success","Json current update success!!", AlertType.INFORMATION);
										return true;
									}

									//ref_cmBxCurrentCalibPointNameList.getItems().add(currentCalibPoints.get(calibPointSelectionIndex).getCalibPointName());
								}

							}
						}
					}
				}

			}

		}
		//ApplicationLauncher.InformUser("Json update failed","Json update failed due to data failure. Kindly check the logs!!", AlertType.INFORMATION);
		//ApplicationLauncher.logger.debug("updateJsonCurrentRphaseData: data not found : Failed");
		return false;
	}

	public void calculateNewCurrentRphaseRms(){

		ApplicationLauncher.logger.debug("calculateNewCurrentRphaseRms: Entry");

		long presentCurrentRphaseRmsValue = Long.parseLong(ref_txtCurrentRphasePresentRmsValue.getText());
		float presentCurrentRphaseTargetValue = Float.parseFloat(ref_txtCurrentRphaseCalibPointValue.getText());
		if(!ref_txtCurrentRphaseUpdatedInRefStd.getText().isEmpty()){
			float newUpdatedCurrentRphaseInRefStd = Float.parseFloat(ref_txtCurrentRphaseUpdatedInRefStd.getText());

			ApplicationLauncher.logger.debug("calculateNewCurrentRphaseRms: presentCurrentRphaseRmsValue: " + presentCurrentRphaseRmsValue);
			ApplicationLauncher.logger.debug("calculateNewCurrentRphaseRms: presentCurrentRphaseTargetValue: " + presentCurrentRphaseTargetValue);
			ApplicationLauncher.logger.debug("calculateNewCurrentRphaseRms: newUpdatedCurrentRphaseInRefStd: " + newUpdatedCurrentRphaseInRefStd);

			Float newRphaseRmsFloatValue = ( (presentCurrentRphaseRmsValue*presentCurrentRphaseTargetValue)/ newUpdatedCurrentRphaseInRefStd  );
			ApplicationLauncher.logger.debug("calculateNewCurrentRphaseRms: newRmsFloatValue: " + newRphaseRmsFloatValue);

			ApplicationLauncher.logger.debug("calculateNewCurrentRphaseRms: newRmsFloatValue rounded: " + Math.round(newRphaseRmsFloatValue));
			//newRmsFloatValue = Math.round(newRmsFloatValue);
			long newCurrentRphaseRmsValue = Math.round(newRphaseRmsFloatValue);//newRmsFloatValue.longValue();


			ApplicationLauncher.logger.debug("calculateNewCurrentRphaseRms: newCurrentRphaseRmsValue: " + newCurrentRphaseRmsValue);
			ref_txtCurrentRphaseNewCalcRmsValue.setText(String.valueOf(newCurrentRphaseRmsValue));
		}
	}





	@FXML 
	public void currentRphaseSelectionOnChange(){
		ApplicationLauncher.logger.debug("currentRphaseSelectionOnChange : Entry");

		if(!initInProgress){
			currentRphaseSelectionChangeGuiInProgress = true;

			clearCurrentRphaseGui();

			String selectedCurrentPhase = ref_cmBxCurrentRphaseSelected.getSelectionModel().getSelectedItem().toString();
			ApplicationLauncher.logger.debug("currentRphaseSelectionOnChange: selectedCurrentPhase : " + selectedCurrentPhase);
			List<CurrentCalibration> currentCalibDataList = lscsCalibrationConfigFileUpdatedKeyValue.getCurrentCalibration();
			for(int phaseSelectionIndex = 0; phaseSelectionIndex < currentCalibDataList.size();phaseSelectionIndex++){
				if(currentCalibDataList.get(phaseSelectionIndex).getCurrentPhase().equals(selectedCurrentPhase)){
					List<CurrentTap> currentTapList = currentCalibDataList.get(phaseSelectionIndex).getCurrentTap();
					if(currentTapList.size()>0){
						ref_txtCurrentRphaseNoOfTapping.setText(String.valueOf(currentTapList.size()));
						String tapSelectionCurrentRelayCode = currentTapList.get(0).getCurrentRelayId();
						ref_txtCurrentRphaseTapSelectionRelayCode.setText(tapSelectionCurrentRelayCode);

						for(int tapSelectionIndex = 0; tapSelectionIndex <currentTapList.size();tapSelectionIndex++){
							ref_cmBxCurrentRphaseTapMaxList.getItems().add(currentTapList.get(tapSelectionIndex).getCurrentTapMax());
							//List<CalibPoints> voltCalibPoints = currentTapList.get(tapSelectionIndex).getCalibPoints();
						}
						ref_cmBxCurrentRphaseTapMaxList.getSelectionModel().select(0);
						List<CalibPoints> currentCalibPoints = currentTapList.get(0).getCalibPoints();
						if(currentCalibPoints.size()>0){
							ref_txtCurrentRphaseNoOfCalibPointEachTap.setText(String.valueOf(currentCalibPoints.size()));
							for(int calibPointSelectionIndex = 0; calibPointSelectionIndex <currentCalibPoints.size();calibPointSelectionIndex++){
								ref_cmBxCurrentRphaseCalibPointNameList.getItems().add(currentCalibPoints.get(calibPointSelectionIndex).getCalibPointName());
							}
							ref_cmBxCurrentRphaseCalibPointNameList.getSelectionModel().select(0);
							float calibPointCurrentValue = currentCalibPoints.get(0).getCalibPointValue();
							ref_txtCurrentRphaseCalibPointValue.setText(String.valueOf(calibPointCurrentValue));
							long calibPointRmsValue = currentCalibPoints.get(0).getRmsValue();
							ref_txtCurrentRphasePresentRmsValue.setText(String.valueOf(calibPointRmsValue));
							
							double calibPointGainValue = currentCalibPoints.get(0).getGainValue();
							ref_txtCurrentRphasePresentGainValue.setText(String.valueOf(calibPointGainValue));
							double calibPointOffsetValue = currentCalibPoints.get(0).getOffsetValue();
							ref_txtCurrentRphasePresentOffsetValue.setText(String.valueOf(calibPointOffsetValue));
						}
					}

				}

			}
			currentRphaseSelectionChangeGuiInProgress = false;
			
			if ((ref_chkBxCurrentSyncSelection.isSelected()) && (ref_rdBtnCurrentAllPhase.isSelected()) ){
				int selectedCurrentRphaseIndex = ref_cmBxCurrentRphaseSelected.getSelectionModel().getSelectedIndex();
				ref_cmBxCurrentYphaseSelected.getSelectionModel().select(selectedCurrentRphaseIndex);
				ref_cmBxCurrentBphaseSelected.getSelectionModel().select(selectedCurrentRphaseIndex);
			}
		}
		//});

	}

	@FXML 
	public void currentRphaseTapMaxListOnChange(){
		ApplicationLauncher.logger.debug("currentRphaseTapMaxListOnChange : Entry");
		if(!currentRphaseSelectionChangeGuiInProgress){
			currentRphaseTapMaxChangeGuiInProgress = true;
			clearCurrentRphaseTapChangeGui();

			String selectedCurrentPhase = ref_cmBxCurrentRphaseSelected.getSelectionModel().getSelectedItem().toString();
			ApplicationLauncher.logger.debug("currentRphaseTapMaxListOnChange: selectedCurrentPhase : " + selectedCurrentPhase);
			List<CurrentCalibration> currentCalibDataList = lscsCalibrationConfigFileUpdatedKeyValue.getCurrentCalibration();
			for(int phaseSelectionIndex = 0; phaseSelectionIndex < currentCalibDataList.size();phaseSelectionIndex++){
				if(currentCalibDataList.get(phaseSelectionIndex).getCurrentPhase().equals(selectedCurrentPhase)){
					List<CurrentTap> currentTapList = currentCalibDataList.get(phaseSelectionIndex).getCurrentTap();
					if(currentTapList.size()>0){
						//ref_txtCurrentNoOfTapping.setText(String.valueOf(currentTapList.size()));

						int selectedCurrentTapMaxIndex = ref_cmBxCurrentRphaseTapMaxList.getSelectionModel().getSelectedIndex();
						ApplicationLauncher.logger.debug("currentRphaseTapMaxListOnChange: selectedCurrentTapMax : " + ref_cmBxCurrentRphaseTapMaxList.getSelectionModel().getSelectedItem());
						ref_cmBxCurrentRphaseTapMaxList.getSelectionModel().select(selectedCurrentTapMaxIndex);
						String tapSelectionCurrentRelayCode = currentTapList.get(selectedCurrentTapMaxIndex).getCurrentRelayId();
						ref_txtCurrentRphaseTapSelectionRelayCode.setText(tapSelectionCurrentRelayCode);

						List<CalibPoints> currentCalibPoints = currentTapList.get(selectedCurrentTapMaxIndex).getCalibPoints();
						if(currentCalibPoints.size()>0){
							ref_txtCurrentRphaseNoOfCalibPointEachTap.setText(String.valueOf(currentCalibPoints.size()));
							/*						for(int calibPointSelectionIndex = 0; calibPointSelectionIndex <currentCalibPoints.size();calibPointSelectionIndex++){
							ref_cmBxCurrentCalibPointNameList.getItems().add(currentCalibPoints.get(calibPointSelectionIndex).getCalibPointName());
						}*/
							for(int calibPointSelectionIndex = 0; calibPointSelectionIndex < currentCalibPoints.size();calibPointSelectionIndex++){

								ref_cmBxCurrentRphaseCalibPointNameList.getItems().add(currentCalibPoints.get(calibPointSelectionIndex).getCalibPointName());
							}
							ref_cmBxCurrentRphaseCalibPointNameList.getSelectionModel().select(0);
							float calibPointCurrentValue = currentCalibPoints.get(0).getCalibPointValue();
							ref_txtCurrentRphaseCalibPointValue.setText(String.valueOf(calibPointCurrentValue));
							long calibPointRmsValue = currentCalibPoints.get(0).getRmsValue();
							ref_txtCurrentRphasePresentRmsValue.setText(String.valueOf(calibPointRmsValue));
							
							double calibPointGainValue = currentCalibPoints.get(0).getGainValue();
							ref_txtCurrentRphasePresentGainValue.setText(String.valueOf(calibPointGainValue));
							double calibPointOffsetValue = currentCalibPoints.get(0).getOffsetValue();
							ref_txtCurrentRphasePresentOffsetValue.setText(String.valueOf(calibPointOffsetValue));
						}
					}

				}
			}
			currentRphaseTapMaxChangeGuiInProgress = false;
			
			if ((ref_chkBxCurrentSyncSelection.isSelected()) && (ref_rdBtnCurrentAllPhase.isSelected()) ){
				int selectedCurrentRphaseTapIndex = ref_cmBxCurrentRphaseTapMaxList.getSelectionModel().getSelectedIndex();
				ref_cmBxCurrentYphaseTapMaxList.getSelectionModel().select(selectedCurrentRphaseTapIndex);
				ref_cmBxCurrentBphaseTapMaxList.getSelectionModel().select(selectedCurrentRphaseTapIndex);
			}
		}
		//});
	}

	@FXML 
	public void currentRphaseCalibPointsNameOnChange(){
		ApplicationLauncher.logger.debug("currentRphaseCalibPointsNameOnChange : Entry");
		if(!currentRphaseSelectionChangeGuiInProgress){
			if(!currentRphaseTapMaxChangeGuiInProgress){
				//Platform.runLater(() -> {
				clearCurrentRphaseCalibPointChangeGui();

				String selectedCurrentPhase = ref_cmBxCurrentRphaseSelected.getSelectionModel().getSelectedItem().toString();
				ApplicationLauncher.logger.debug("currentRphaseCalibPointsNameOnChange: selectedCurrentPhase : " + selectedCurrentPhase);
				List<CurrentCalibration> currentCalibDataList = lscsCalibrationConfigFileUpdatedKeyValue.getCurrentCalibration();
				for(int phaseSelectionIndex = 0; phaseSelectionIndex < currentCalibDataList.size();phaseSelectionIndex++){
					if(currentCalibDataList.get(phaseSelectionIndex).getCurrentPhase().equals(selectedCurrentPhase)){
						List<CurrentTap> currentTapList = currentCalibDataList.get(phaseSelectionIndex).getCurrentTap();
						if(currentTapList.size()>0){

							int selectedCurrentTapMaxIndex = ref_cmBxCurrentRphaseTapMaxList.getSelectionModel().getSelectedIndex();
							ApplicationLauncher.logger.debug("currentRphaseCalibPointsNameOnChange: selectedCurrentTapMax : " + ref_cmBxCurrentRphaseTapMaxList.getSelectionModel().getSelectedItem());
							//ref_cmBxCurrentTapMaxList.getSelectionModel().select(selectedCurrentTapMaxIndex);
							//String tapSelectionCurrentRelayCode = currentTapList.get(selectedCurrentTapMaxIndex).getCurrentRelayId();
							//ref_txtCurrentTapSelectionRelayCode.setText(tapSelectionCurrentRelayCode);

							List<CalibPoints> currentCalibPoints = currentTapList.get(selectedCurrentTapMaxIndex).getCalibPoints();
							if(currentCalibPoints.size()>0){
								int selectedCurrentCalibPointNameIndex = ref_cmBxCurrentRphaseCalibPointNameList.getSelectionModel().getSelectedIndex();
								ApplicationLauncher.logger.debug("currentRphaseCalibPointsNameOnChange: selectedCurrentCalibPointName : " + ref_cmBxCurrentRphaseCalibPointNameList.getSelectionModel().getSelectedItem());
								//ref_cmBxCurrentCalibPointNameList.getSelectionModel().select(0);
								float calibPointCurrentValue = currentCalibPoints.get(selectedCurrentCalibPointNameIndex).getCalibPointValue();
								ref_txtCurrentRphaseCalibPointValue.setText(String.valueOf(calibPointCurrentValue));
								long calibPointRmsValue = currentCalibPoints.get(selectedCurrentCalibPointNameIndex).getRmsValue();
								ref_txtCurrentRphasePresentRmsValue.setText(String.valueOf(calibPointRmsValue));
								
								double calibPointGainValue = currentCalibPoints.get(selectedCurrentCalibPointNameIndex).getGainValue();
								ref_txtCurrentRphasePresentGainValue.setText(String.valueOf(calibPointGainValue));
								double calibPointOffsetValue = currentCalibPoints.get(selectedCurrentCalibPointNameIndex).getOffsetValue();
								ref_txtCurrentRphasePresentOffsetValue.setText(String.valueOf(calibPointOffsetValue));
							}
						}

					}
				}
			}
		}
		if ((ref_chkBxCurrentSyncSelection.isSelected()) && (ref_rdBtnCurrentAllPhase.isSelected()) ){
			int selectedCurrentRphaseCalibPointNameIndex = ref_cmBxCurrentRphaseCalibPointNameList.getSelectionModel().getSelectedIndex();
			ref_cmBxCurrentYphaseCalibPointNameList.getSelectionModel().select(selectedCurrentRphaseCalibPointNameIndex);
			ref_cmBxCurrentBphaseCalibPointNameList.getSelectionModel().select(selectedCurrentRphaseCalibPointNameIndex);
		}
		//});

	}







	@FXML 
	public void rdBtnCurrentRphaseOnlyOnChange(){
		ApplicationLauncher.logger.debug("rdBtnCurrentRphaseOnlyOnChange : Entry");
		if(ref_rdBtnCurrentRphaseOnly.isSelected()){
			ref_rdBtnCurrentYphaseOnly.setSelected(false);
			ref_rdBtnCurrentBphaseOnly.setSelected(false);
			ref_rdBtnCurrentAllPhase.setSelected(false);
		}
	}

	@FXML 
	public void rdBtnCurrentYphaseOnlyOnChange(){
		ApplicationLauncher.logger.debug("rdBtnCurrentYphaseOnlyOnChange : Entry");
		if(ref_rdBtnCurrentYphaseOnly.isSelected()){
			ref_rdBtnCurrentRphaseOnly.setSelected(false);
			ref_rdBtnCurrentBphaseOnly.setSelected(false);
			ref_rdBtnCurrentAllPhase.setSelected(false);
		}
	}

	@FXML 
	public void rdBtnCurrentBphaseOnlyOnChange(){
		ApplicationLauncher.logger.debug("rdBtnCurrentBphaseOnlyOnChange : Entry");
		if(ref_rdBtnCurrentBphaseOnly.isSelected()){
			ref_rdBtnCurrentYphaseOnly.setSelected(false);
			ref_rdBtnCurrentRphaseOnly.setSelected(false);
			ref_rdBtnCurrentAllPhase.setSelected(false);
		}
	}

	@FXML 
	public void rdBtnCurrentAllPhaseOnChange(){
		ApplicationLauncher.logger.debug("rdBtnCurrentAllPhaseOnChange : Entry");
		if(ref_rdBtnCurrentAllPhase.isSelected()){
			ref_rdBtnCurrentYphaseOnly.setSelected(false);
			ref_rdBtnCurrentBphaseOnly.setSelected(false);
			ref_rdBtnCurrentRphaseOnly.setSelected(false);
		}
	}




	@FXML 
	public void rdBtnVoltageRphaseOnlyOnChange(){
		ApplicationLauncher.logger.debug("rdBtnVoltageRphaseOnlyOnChange : Entry");
		if(ref_rdBtnVoltageRphaseOnly.isSelected()){
			ref_rdBtnVoltageYphaseOnly.setSelected(false);
			ref_rdBtnVoltageBphaseOnly.setSelected(false);
			ref_rdBtnVoltageAllPhase.setSelected(false);
		}
	}

	@FXML 
	public void rdBtnVoltageYphaseOnlyOnChange(){
		ApplicationLauncher.logger.debug("rdBtnVoltageYphaseOnlyOnChange : Entry");
		if(ref_rdBtnVoltageYphaseOnly.isSelected()){
			ref_rdBtnVoltageRphaseOnly.setSelected(false);
			ref_rdBtnVoltageBphaseOnly.setSelected(false);
			ref_rdBtnVoltageAllPhase.setSelected(false);
		}
	}

	@FXML 
	public void rdBtnVoltageBphaseOnlyOnChange(){
		ApplicationLauncher.logger.debug("rdBtnVoltageBphaseOnlyOnChange : Entry");
		if(ref_rdBtnVoltageBphaseOnly.isSelected()){
			ref_rdBtnVoltageYphaseOnly.setSelected(false);
			ref_rdBtnVoltageRphaseOnly.setSelected(false);
			ref_rdBtnVoltageAllPhase.setSelected(false);
		}
	}

	@FXML 
	public void rdBtnVoltageAllPhaseOnChange(){
		ApplicationLauncher.logger.debug("rdBtnVoltageAllPhaseOnChange : Entry");
		if(ref_rdBtnVoltageAllPhase.isSelected()){
			ref_rdBtnVoltageYphaseOnly.setSelected(false);
			ref_rdBtnVoltageBphaseOnly.setSelected(false);
			ref_rdBtnVoltageRphaseOnly.setSelected(false);
		}
	}




	public boolean updateJsonCurrentYphaseData(){
		ApplicationLauncher.logger.debug("updateJsonCurrentYphaseData: Entry");
		String selectedCurrentPhase = ref_cmBxCurrentYphaseSelected.getSelectionModel().getSelectedItem().toString();
		String newCurrentRmsValue = ref_txtCurrentYphaseNewCalcRmsValue.getText();
		ApplicationLauncher.logger.debug("updateJsonCurrentYphaseData: selectedCurrentPhase : " + selectedCurrentPhase);
		ApplicationLauncher.logger.debug("updateJsonCurrentYphaseData: newCurrentRmsValue : " + newCurrentRmsValue);
		List<CurrentCalibration> currentCalibDataList = lscsCalibrationConfigFileUpdatedKeyValue.getCurrentCalibration();
		for(int phaseSelectionIndex = 0; phaseSelectionIndex < currentCalibDataList.size();phaseSelectionIndex++){
			if(currentCalibDataList.get(phaseSelectionIndex).getCurrentPhase().equals(selectedCurrentPhase)){
				List<CurrentTap> currentTapList = currentCalibDataList.get(phaseSelectionIndex).getCurrentTap();
				if(currentTapList.size()>0){
					//ref_txtCurrentNoOfTapping.setText(String.valueOf(currentTapList.size()));
					//String tapSelectionCurrentRelayCode = currentTapList.get(0).getCurrentRelayId();
					//ref_txtCurrentTapSelectionRelayCode.setText(tapSelectionCurrentRelayCode);
					float selectedCurrentTap = Float.parseFloat(ref_cmBxCurrentYphaseTapMaxList.getSelectionModel().getSelectedItem().toString());
					ApplicationLauncher.logger.debug("updateJsonCurrentYphaseData: selectedCurrentTap : " + selectedCurrentTap);
					for(int tapSelectionIndex = 0; tapSelectionIndex <currentTapList.size();tapSelectionIndex++){
						//ref_cmBxCurrentTapMaxList.getItems().add(currentTapList.get(tapSelectionIndex).getCurrentTapMax());
						//List<CalibPoints> voltCalibPoints = currentTapList.get(tapSelectionIndex).getCalibPoints();
						if(selectedCurrentTap == currentTapList.get(tapSelectionIndex).getCurrentTapMax()){
							List<CalibPoints> currentCalibPoints = currentTapList.get(tapSelectionIndex).getCalibPoints();
							if(currentCalibPoints.size()>0){
								float selectedCalibPointValue = Float.parseFloat(ref_txtCurrentYphaseCalibPointValue.getText());
								ApplicationLauncher.logger.debug("updateJsonCurrentYphaseData: selectedCalibPointValue : " + selectedCalibPointValue);
								for(int calibPointSelectionIndex = 0; calibPointSelectionIndex <currentCalibPoints.size();calibPointSelectionIndex++){

									if(selectedCalibPointValue == currentCalibPoints.get(calibPointSelectionIndex).getCalibPointValue()){
										ApplicationLauncher.logger.debug("updateJsonCurrentYphaseData: newCurrentRmsValue set success: ");
										currentCalibPoints.get(calibPointSelectionIndex).setRmsValue(Long.valueOf(newCurrentRmsValue));
										//ApplicationLauncher.logger.debug("updateJsonCurrentYphaseData: new set  getRmsValue: " + currentCalibPoints.get(calibPointSelectionIndex).getRmsValue());
										//ApplicationLauncher.InformUser("Json update success","Json current update success!!", AlertType.INFORMATION);
										return true;
									}

									//ref_cmBxCurrentCalibPointNameList.getItems().add(currentCalibPoints.get(calibPointSelectionIndex).getCalibPointName());
								}

							}
						}
					}
				}

			}

		}
		//ApplicationLauncher.InformUser("Json update failed","Json update failed due to data failure. Kindly check the logs!!", AlertType.INFORMATION);
		//ApplicationLauncher.logger.debug("updateJsonCurrentYphaseData: data not found : Failed");
		return false;
	}

	public void calculateNewCurrentYphaseRms(){

		ApplicationLauncher.logger.debug("calculateNewCurrentYphaseRms: Entry");

		long presentCurrentYphaseRmsValue = Long.parseLong(ref_txtCurrentYphasePresentRmsValue.getText());
		float presentCurrentYphaseTargetValue = Float.parseFloat(ref_txtCurrentYphaseCalibPointValue.getText());
		if(!ref_txtCurrentYphaseUpdatedInRefStd.getText().isEmpty()){
			float newUpdatedCurrentYphaseInRefStd = Float.parseFloat(ref_txtCurrentYphaseUpdatedInRefStd.getText());

			ApplicationLauncher.logger.debug("calculateNewCurrentYphaseRms: presentCurrentYphaseRmsValue: " + presentCurrentYphaseRmsValue);
			ApplicationLauncher.logger.debug("calculateNewCurrentYphaseRms: presentCurrentYphaseTargetValue: " + presentCurrentYphaseTargetValue);
			ApplicationLauncher.logger.debug("calculateNewCurrentYphaseRms: newUpdatedCurrentYphaseInRefStd: " + newUpdatedCurrentYphaseInRefStd);

			Float newYphaseRmsFloatValue = ( (presentCurrentYphaseRmsValue*presentCurrentYphaseTargetValue)/ newUpdatedCurrentYphaseInRefStd  );
			ApplicationLauncher.logger.debug("calculateNewCurrentYphaseRms: newRmsFloatValue: " + newYphaseRmsFloatValue);

			ApplicationLauncher.logger.debug("calculateNewCurrentYphaseRms: newRmsFloatValue rounded: " + Math.round(newYphaseRmsFloatValue));
			//newRmsFloatValue = Math.round(newRmsFloatValue);
			long newCurrentYphaseRmsValue = Math.round(newYphaseRmsFloatValue);//newRmsFloatValue.longValue();


			ApplicationLauncher.logger.debug("calculateNewCurrentYphaseRms: newCurrentYphaseRmsValue: " + newCurrentYphaseRmsValue);
			ref_txtCurrentYphaseNewCalcRmsValue.setText(String.valueOf(newCurrentYphaseRmsValue));
		}
	}





	@FXML 
	public void currentYphaseSelectionOnChange(){
		ApplicationLauncher.logger.debug("currentYphaseSelectionOnChange : Entry");

		if(!initInProgress){
			currentYphaseSelectionChangeGuiInProgress = true;

			clearCurrentYphaseGui();

			String selectedCurrentPhase = ref_cmBxCurrentYphaseSelected.getSelectionModel().getSelectedItem().toString();
			ApplicationLauncher.logger.debug("currentYphaseSelectionOnChange: selectedCurrentPhase : " + selectedCurrentPhase);
			List<CurrentCalibration> currentCalibDataList = lscsCalibrationConfigFileUpdatedKeyValue.getCurrentCalibration();
			for(int phaseSelectionIndex = 0; phaseSelectionIndex < currentCalibDataList.size();phaseSelectionIndex++){
				if(currentCalibDataList.get(phaseSelectionIndex).getCurrentPhase().equals(selectedCurrentPhase)){
					List<CurrentTap> currentTapList = currentCalibDataList.get(phaseSelectionIndex).getCurrentTap();
					if(currentTapList.size()>0){
						ref_txtCurrentYphaseNoOfTapping.setText(String.valueOf(currentTapList.size()));
						String tapSelectionCurrentRelayCode = currentTapList.get(0).getCurrentRelayId();
						ref_txtCurrentYphaseTapSelectionRelayCode.setText(tapSelectionCurrentRelayCode);

						for(int tapSelectionIndex = 0; tapSelectionIndex <currentTapList.size();tapSelectionIndex++){
							ref_cmBxCurrentYphaseTapMaxList.getItems().add(currentTapList.get(tapSelectionIndex).getCurrentTapMax());
							//List<CalibPoints> voltCalibPoints = currentTapList.get(tapSelectionIndex).getCalibPoints();
						}
						ref_cmBxCurrentYphaseTapMaxList.getSelectionModel().select(0);
						List<CalibPoints> currentCalibPoints = currentTapList.get(0).getCalibPoints();
						if(currentCalibPoints.size()>0){
							ref_txtCurrentYphaseNoOfCalibPointEachTap.setText(String.valueOf(currentCalibPoints.size()));
							for(int calibPointSelectionIndex = 0; calibPointSelectionIndex <currentCalibPoints.size();calibPointSelectionIndex++){
								ref_cmBxCurrentYphaseCalibPointNameList.getItems().add(currentCalibPoints.get(calibPointSelectionIndex).getCalibPointName());
							}
							ref_cmBxCurrentYphaseCalibPointNameList.getSelectionModel().select(0);
							float calibPointCurrentValue = currentCalibPoints.get(0).getCalibPointValue();
							ref_txtCurrentYphaseCalibPointValue.setText(String.valueOf(calibPointCurrentValue));
							long calibPointRmsValue = currentCalibPoints.get(0).getRmsValue();
							ref_txtCurrentYphasePresentRmsValue.setText(String.valueOf(calibPointRmsValue));
							
							double calibPointGainValue = currentCalibPoints.get(0).getGainValue();
							ref_txtCurrentYphasePresentGainValue.setText(String.valueOf(calibPointGainValue));
							double calibPointOffsetValue = currentCalibPoints.get(0).getOffsetValue();
							ref_txtCurrentYphasePresentOffsetValue.setText(String.valueOf(calibPointOffsetValue));
						}
					}

				}

			}
			currentYphaseSelectionChangeGuiInProgress = false;
		}
		//});

	}

	@FXML 
	public void currentYphaseTapMaxListOnChange(){
		ApplicationLauncher.logger.debug("currentYphaseTapMaxListOnChange : Entry");
		if(!currentYphaseSelectionChangeGuiInProgress){
			currentYphaseTapMaxChangeGuiInProgress = true;
			clearCurrentYphaseTapChangeGui();

			String selectedCurrentPhase = ref_cmBxCurrentYphaseSelected.getSelectionModel().getSelectedItem().toString();
			ApplicationLauncher.logger.debug("currentYphaseTapMaxListOnChange: selectedCurrentPhase : " + selectedCurrentPhase);
			List<CurrentCalibration> currentCalibDataList = lscsCalibrationConfigFileUpdatedKeyValue.getCurrentCalibration();
			for(int phaseSelectionIndex = 0; phaseSelectionIndex < currentCalibDataList.size();phaseSelectionIndex++){
				if(currentCalibDataList.get(phaseSelectionIndex).getCurrentPhase().equals(selectedCurrentPhase)){
					List<CurrentTap> currentTapList = currentCalibDataList.get(phaseSelectionIndex).getCurrentTap();
					if(currentTapList.size()>0){
						//ref_txtCurrentNoOfTapping.setText(String.valueOf(currentTapList.size()));

						int selectedCurrentTapMaxIndex = ref_cmBxCurrentYphaseTapMaxList.getSelectionModel().getSelectedIndex();
						ApplicationLauncher.logger.debug("currentYphaseTapMaxListOnChange: selectedCurrentTapMax : " + ref_cmBxCurrentYphaseTapMaxList.getSelectionModel().getSelectedItem());
						ref_cmBxCurrentYphaseTapMaxList.getSelectionModel().select(selectedCurrentTapMaxIndex);
						String tapSelectionCurrentRelayCode = currentTapList.get(selectedCurrentTapMaxIndex).getCurrentRelayId();
						ref_txtCurrentYphaseTapSelectionRelayCode.setText(tapSelectionCurrentRelayCode);

						List<CalibPoints> currentCalibPoints = currentTapList.get(selectedCurrentTapMaxIndex).getCalibPoints();
						if(currentCalibPoints.size()>0){
							ref_txtCurrentYphaseNoOfCalibPointEachTap.setText(String.valueOf(currentCalibPoints.size()));
							/*						for(int calibPointSelectionIndex = 0; calibPointSelectionIndex <currentCalibPoints.size();calibPointSelectionIndex++){
							ref_cmBxCurrentCalibPointNameList.getItems().add(currentCalibPoints.get(calibPointSelectionIndex).getCalibPointName());
						}*/
							for(int calibPointSelectionIndex = 0; calibPointSelectionIndex < currentCalibPoints.size();calibPointSelectionIndex++){

								ref_cmBxCurrentYphaseCalibPointNameList.getItems().add(currentCalibPoints.get(calibPointSelectionIndex).getCalibPointName());
							}
							ref_cmBxCurrentYphaseCalibPointNameList.getSelectionModel().select(0);
							float calibPointCurrentValue = currentCalibPoints.get(0).getCalibPointValue();
							ref_txtCurrentYphaseCalibPointValue.setText(String.valueOf(calibPointCurrentValue));
							long calibPointRmsValue = currentCalibPoints.get(0).getRmsValue();
							ref_txtCurrentYphasePresentRmsValue.setText(String.valueOf(calibPointRmsValue));
							
							double calibPointGainValue = currentCalibPoints.get(0).getGainValue();
							ref_txtCurrentYphasePresentGainValue.setText(String.valueOf(calibPointGainValue));
							double calibPointOffsetValue = currentCalibPoints.get(0).getOffsetValue();
							ref_txtCurrentYphasePresentOffsetValue.setText(String.valueOf(calibPointOffsetValue));
						}
					}

				}
			}
			currentYphaseTapMaxChangeGuiInProgress = false;
		}
		//});
	}

	@FXML 
	public void currentYphaseCalibPointsNameOnChange(){
		ApplicationLauncher.logger.debug("currentYphaseCalibPointsNameOnChange : Entry");
		if(!currentYphaseSelectionChangeGuiInProgress){
			if(!currentYphaseTapMaxChangeGuiInProgress){
				//Platform.runLater(() -> {
				clearCurrentYphaseCalibPointChangeGui();

				String selectedCurrentPhase = ref_cmBxCurrentYphaseSelected.getSelectionModel().getSelectedItem().toString();
				ApplicationLauncher.logger.debug("currentYphaseCalibPointsNameOnChange: selectedCurrentPhase : " + selectedCurrentPhase);
				List<CurrentCalibration> currentCalibDataList = lscsCalibrationConfigFileUpdatedKeyValue.getCurrentCalibration();
				for(int phaseSelectionIndex = 0; phaseSelectionIndex < currentCalibDataList.size();phaseSelectionIndex++){
					if(currentCalibDataList.get(phaseSelectionIndex).getCurrentPhase().equals(selectedCurrentPhase)){
						List<CurrentTap> currentTapList = currentCalibDataList.get(phaseSelectionIndex).getCurrentTap();
						if(currentTapList.size()>0){

							int selectedCurrentTapMaxIndex = ref_cmBxCurrentYphaseTapMaxList.getSelectionModel().getSelectedIndex();
							ApplicationLauncher.logger.debug("currentYphaseCalibPointsNameOnChange: selectedCurrentTapMax : " + ref_cmBxCurrentYphaseTapMaxList.getSelectionModel().getSelectedItem());
							//ref_cmBxCurrentTapMaxList.getSelectionModel().select(selectedCurrentTapMaxIndex);
							//String tapSelectionCurrentRelayCode = currentTapList.get(selectedCurrentTapMaxIndex).getCurrentRelayId();
							//ref_txtCurrentTapSelectionRelayCode.setText(tapSelectionCurrentRelayCode);

							List<CalibPoints> currentCalibPoints = currentTapList.get(selectedCurrentTapMaxIndex).getCalibPoints();
							if(currentCalibPoints.size()>0){
								try{
									int selectedCurrentCalibPointNameIndex = ref_cmBxCurrentYphaseCalibPointNameList.getSelectionModel().getSelectedIndex();
									ApplicationLauncher.logger.debug("currentYphaseCalibPointsNameOnChange: selectedCurrentCalibPointName : " + ref_cmBxCurrentYphaseCalibPointNameList.getSelectionModel().getSelectedItem());
									//ref_cmBxCurrentCalibPointNameList.getSelectionModel().select(0);
									float calibPointCurrentValue = currentCalibPoints.get(selectedCurrentCalibPointNameIndex).getCalibPointValue();
									ref_txtCurrentYphaseCalibPointValue.setText(String.valueOf(calibPointCurrentValue));
									long calibPointRmsValue = currentCalibPoints.get(selectedCurrentCalibPointNameIndex).getRmsValue();
									ref_txtCurrentYphasePresentRmsValue.setText(String.valueOf(calibPointRmsValue));
									
									double calibPointGainValue = currentCalibPoints.get(selectedCurrentCalibPointNameIndex).getGainValue();
									ref_txtCurrentYphasePresentGainValue.setText(String.valueOf(calibPointGainValue));
									double calibPointOffsetValue = currentCalibPoints.get(selectedCurrentCalibPointNameIndex).getOffsetValue();
									ref_txtCurrentYphasePresentOffsetValue.setText(String.valueOf(calibPointOffsetValue));
								}catch(Exception e){
									e.printStackTrace();
									ApplicationLauncher.logger.error("currentYphaseCalibPointsNameOnChange : ExceptionA:"+e.getMessage());
								}
							}
						}

					}
				}
			}
		}
		//});

	}






	public boolean updateJsonCurrentBphaseData(){
		ApplicationLauncher.logger.debug("updateJsonCurrentBphaseData: Entry");
		String selectedCurrentPhase = ref_cmBxCurrentBphaseSelected.getSelectionModel().getSelectedItem().toString();
		String newCurrentRmsValue = ref_txtCurrentBphaseNewCalcRmsValue.getText();
		ApplicationLauncher.logger.debug("updateJsonCurrentBphaseData: selectedCurrentPhase : " + selectedCurrentPhase);
		ApplicationLauncher.logger.debug("updateJsonCurrentBphaseData: newCurrentRmsValue : " + newCurrentRmsValue);
		List<CurrentCalibration> currentCalibDataList = lscsCalibrationConfigFileUpdatedKeyValue.getCurrentCalibration();
		for(int phaseSelectionIndex = 0; phaseSelectionIndex < currentCalibDataList.size();phaseSelectionIndex++){
			if(currentCalibDataList.get(phaseSelectionIndex).getCurrentPhase().equals(selectedCurrentPhase)){
				List<CurrentTap> currentTapList = currentCalibDataList.get(phaseSelectionIndex).getCurrentTap();
				if(currentTapList.size()>0){
					//ref_txtCurrentNoOfTapping.setText(String.valueOf(currentTapList.size()));
					//String tapSelectionCurrentRelayCode = currentTapList.get(0).getCurrentRelayId();
					//ref_txtCurrentTapSelectionRelayCode.setText(tapSelectionCurrentRelayCode);
					float selectedCurrentTap = Float.parseFloat(ref_cmBxCurrentBphaseTapMaxList.getSelectionModel().getSelectedItem().toString());
					ApplicationLauncher.logger.debug("updateJsonCurrentBphaseData: selectedCurrentTap : " + selectedCurrentTap);
					for(int tapSelectionIndex = 0; tapSelectionIndex <currentTapList.size();tapSelectionIndex++){
						//ref_cmBxCurrentTapMaxList.getItems().add(currentTapList.get(tapSelectionIndex).getCurrentTapMax());
						//List<CalibPoints> voltCalibPoints = currentTapList.get(tapSelectionIndex).getCalibPoints();
						if(selectedCurrentTap == currentTapList.get(tapSelectionIndex).getCurrentTapMax()){
							List<CalibPoints> currentCalibPoints = currentTapList.get(tapSelectionIndex).getCalibPoints();
							if(currentCalibPoints.size()>0){
								float selectedCalibPointValue = Float.parseFloat(ref_txtCurrentBphaseCalibPointValue.getText());
								ApplicationLauncher.logger.debug("updateJsonCurrentBphaseData: selectedCalibPointValue : " + selectedCalibPointValue);
								for(int calibPointSelectionIndex = 0; calibPointSelectionIndex <currentCalibPoints.size();calibPointSelectionIndex++){

									if(selectedCalibPointValue == currentCalibPoints.get(calibPointSelectionIndex).getCalibPointValue()){
										ApplicationLauncher.logger.debug("updateJsonCurrentBphaseData: newCurrentRmsValue set success: ");
										currentCalibPoints.get(calibPointSelectionIndex).setRmsValue(Long.valueOf(newCurrentRmsValue));
										//ApplicationLauncher.logger.debug("updateJsonCurrentBphaseData: new set  getRmsValue: " + currentCalibPoints.get(calibPointSelectionIndex).getRmsValue());
										//ApplicationLauncher.InformUser("Json update success","Json current update success!!", AlertType.INFORMATION);
										return true;
									}

									//ref_cmBxCurrentCalibPointNameList.getItems().add(currentCalibPoints.get(calibPointSelectionIndex).getCalibPointName());
								}

							}
						}
					}
				}

			}

		}
		//ApplicationLauncher.InformUser("Json update failed","Json update failed due to data failure. Kindly check the logs!!", AlertType.INFORMATION);
		//ApplicationLauncher.logger.debug("updateJsonCurrentBphaseData: data not found : Failed");
		return false;
	}

	public void calculateNewCurrentBphaseRms(){

		ApplicationLauncher.logger.debug("calculateNewCurrentBphaseRms: Entry");

		long presentCurrentBphaseRmsValue = Long.parseLong(ref_txtCurrentBphasePresentRmsValue.getText());
		float presentCurrentBphaseTargetValue = Float.parseFloat(ref_txtCurrentBphaseCalibPointValue.getText());
		if(!ref_txtCurrentBphaseUpdatedInRefStd.getText().isEmpty()){
			float newUpdatedCurrentBphaseInRefStd = Float.parseFloat(ref_txtCurrentBphaseUpdatedInRefStd.getText());

			ApplicationLauncher.logger.debug("calculateNewCurrentBphaseRms: presentCurrentBphaseRmsValue: " + presentCurrentBphaseRmsValue);
			ApplicationLauncher.logger.debug("calculateNewCurrentBphaseRms: presentCurrentBphaseTargetValue: " + presentCurrentBphaseTargetValue);
			ApplicationLauncher.logger.debug("calculateNewCurrentBphaseRms: newUpdatedCurrentBphaseInRefStd: " + newUpdatedCurrentBphaseInRefStd);

			Float newBphaseRmsFloatValue = ( (presentCurrentBphaseRmsValue*presentCurrentBphaseTargetValue)/ newUpdatedCurrentBphaseInRefStd  );
			ApplicationLauncher.logger.debug("calculateNewCurrentBphaseRms: newRmsFloatValue: " + newBphaseRmsFloatValue);

			ApplicationLauncher.logger.debug("calculateNewCurrentBphaseRms: newRmsFloatValue rounded: " + Math.round(newBphaseRmsFloatValue));
			//newRmsFloatValue = Math.round(newRmsFloatValue);
			long newCurrentBphaseRmsValue = Math.round(newBphaseRmsFloatValue);//newRmsFloatValue.longValue();


			ApplicationLauncher.logger.debug("calculateNewCurrentBphaseRms: newCurrentBphaseRmsValue: " + newCurrentBphaseRmsValue);
			ref_txtCurrentBphaseNewCalcRmsValue.setText(String.valueOf(newCurrentBphaseRmsValue));
		}
	}





	@FXML 
	public void currentBphaseSelectionOnChange(){
		ApplicationLauncher.logger.debug("currentBphaseSelectionOnChange : Entry");

		if(!initInProgress){
			currentBphaseSelectionChangeGuiInProgress = true;

			clearCurrentBphaseGui();

			String selectedCurrentPhase = ref_cmBxCurrentBphaseSelected.getSelectionModel().getSelectedItem().toString();
			ApplicationLauncher.logger.debug("currentBphaseSelectionOnChange: selectedCurrentPhase : " + selectedCurrentPhase);
			List<CurrentCalibration> currentCalibDataList = lscsCalibrationConfigFileUpdatedKeyValue.getCurrentCalibration();
			for(int phaseSelectionIndex = 0; phaseSelectionIndex < currentCalibDataList.size();phaseSelectionIndex++){
				if(currentCalibDataList.get(phaseSelectionIndex).getCurrentPhase().equals(selectedCurrentPhase)){
					List<CurrentTap> currentTapList = currentCalibDataList.get(phaseSelectionIndex).getCurrentTap();
					if(currentTapList.size()>0){
						ref_txtCurrentBphaseNoOfTapping.setText(String.valueOf(currentTapList.size()));
						String tapSelectionCurrentRelayCode = currentTapList.get(0).getCurrentRelayId();
						ref_txtCurrentBphaseTapSelectionRelayCode.setText(tapSelectionCurrentRelayCode);

						for(int tapSelectionIndex = 0; tapSelectionIndex <currentTapList.size();tapSelectionIndex++){
							ref_cmBxCurrentBphaseTapMaxList.getItems().add(currentTapList.get(tapSelectionIndex).getCurrentTapMax());
							//List<CalibPoints> voltCalibPoints = currentTapList.get(tapSelectionIndex).getCalibPoints();
						}
						ref_cmBxCurrentBphaseTapMaxList.getSelectionModel().select(0);
						List<CalibPoints> currentCalibPoints = currentTapList.get(0).getCalibPoints();
						if(currentCalibPoints.size()>0){
							ref_txtCurrentBphaseNoOfCalibPointEachTap.setText(String.valueOf(currentCalibPoints.size()));
							for(int calibPointSelectionIndex = 0; calibPointSelectionIndex <currentCalibPoints.size();calibPointSelectionIndex++){
								ref_cmBxCurrentBphaseCalibPointNameList.getItems().add(currentCalibPoints.get(calibPointSelectionIndex).getCalibPointName());
							}
							ref_cmBxCurrentBphaseCalibPointNameList.getSelectionModel().select(0);
							float calibPointCurrentValue = currentCalibPoints.get(0).getCalibPointValue();
							ref_txtCurrentBphaseCalibPointValue.setText(String.valueOf(calibPointCurrentValue));
							long calibPointRmsValue = currentCalibPoints.get(0).getRmsValue();
							ref_txtCurrentBphasePresentRmsValue.setText(String.valueOf(calibPointRmsValue));
							
							double calibPointGainValue = currentCalibPoints.get(0).getGainValue();
							ref_txtCurrentBphasePresentGainValue.setText(String.valueOf(calibPointGainValue));
							double calibPointOffsetValue = currentCalibPoints.get(0).getOffsetValue();
							ref_txtCurrentBphasePresentOffsetValue.setText(String.valueOf(calibPointOffsetValue));
						}
					}

				}

			}
			currentBphaseSelectionChangeGuiInProgress = false;
		}
		//});

	}

	@FXML 
	public void currentBphaseTapMaxListOnChange(){
		ApplicationLauncher.logger.debug("currentBphaseTapMaxListOnChange : Entry");
		if(!currentBphaseSelectionChangeGuiInProgress){
			currentBphaseTapMaxChangeGuiInProgress = true;
			clearCurrentBphaseTapChangeGui();

			String selectedCurrentPhase = ref_cmBxCurrentBphaseSelected.getSelectionModel().getSelectedItem().toString();
			ApplicationLauncher.logger.debug("currentBphaseTapMaxListOnChange: selectedCurrentPhase : " + selectedCurrentPhase);
			List<CurrentCalibration> currentCalibDataList = lscsCalibrationConfigFileUpdatedKeyValue.getCurrentCalibration();
			for(int phaseSelectionIndex = 0; phaseSelectionIndex < currentCalibDataList.size();phaseSelectionIndex++){
				if(currentCalibDataList.get(phaseSelectionIndex).getCurrentPhase().equals(selectedCurrentPhase)){
					List<CurrentTap> currentTapList = currentCalibDataList.get(phaseSelectionIndex).getCurrentTap();
					if(currentTapList.size()>0){
						//ref_txtCurrentNoOfTapping.setText(String.valueOf(currentTapList.size()));

						int selectedCurrentTapMaxIndex = ref_cmBxCurrentBphaseTapMaxList.getSelectionModel().getSelectedIndex();
						ApplicationLauncher.logger.debug("currentBphaseTapMaxListOnChange: selectedCurrentTapMax : " + ref_cmBxCurrentBphaseTapMaxList.getSelectionModel().getSelectedItem());
						ref_cmBxCurrentBphaseTapMaxList.getSelectionModel().select(selectedCurrentTapMaxIndex);
						String tapSelectionCurrentRelayCode = currentTapList.get(selectedCurrentTapMaxIndex).getCurrentRelayId();
						ref_txtCurrentBphaseTapSelectionRelayCode.setText(tapSelectionCurrentRelayCode);

						List<CalibPoints> currentCalibPoints = currentTapList.get(selectedCurrentTapMaxIndex).getCalibPoints();
						if(currentCalibPoints.size()>0){
							ref_txtCurrentBphaseNoOfCalibPointEachTap.setText(String.valueOf(currentCalibPoints.size()));
							/*						for(int calibPointSelectionIndex = 0; calibPointSelectionIndex <currentCalibPoints.size();calibPointSelectionIndex++){
							ref_cmBxCurrentCalibPointNameList.getItems().add(currentCalibPoints.get(calibPointSelectionIndex).getCalibPointName());
						}*/
							for(int calibPointSelectionIndex = 0; calibPointSelectionIndex < currentCalibPoints.size();calibPointSelectionIndex++){

								ref_cmBxCurrentBphaseCalibPointNameList.getItems().add(currentCalibPoints.get(calibPointSelectionIndex).getCalibPointName());
							}
							ref_cmBxCurrentBphaseCalibPointNameList.getSelectionModel().select(0);
							float calibPointCurrentValue = currentCalibPoints.get(0).getCalibPointValue();
							ref_txtCurrentBphaseCalibPointValue.setText(String.valueOf(calibPointCurrentValue));
							long calibPointRmsValue = currentCalibPoints.get(0).getRmsValue();
							ref_txtCurrentBphasePresentRmsValue.setText(String.valueOf(calibPointRmsValue));
							
							double calibPointGainValue = currentCalibPoints.get(0).getGainValue();
							ref_txtCurrentBphasePresentGainValue.setText(String.valueOf(calibPointGainValue));
							double calibPointOffsetValue = currentCalibPoints.get(0).getOffsetValue();
							ref_txtCurrentBphasePresentOffsetValue.setText(String.valueOf(calibPointOffsetValue));
						}
					}

				}
			}
			currentBphaseTapMaxChangeGuiInProgress = false;
		}
		//});
	}

	@FXML 
	public void currentBphaseCalibPointsNameOnChange(){
		ApplicationLauncher.logger.debug("currentBphaseCalibPointsNameOnChange : Entry");
		if(!currentBphaseSelectionChangeGuiInProgress){
			if(!currentBphaseTapMaxChangeGuiInProgress){
				//Platform.runLater(() -> {
				clearCurrentBphaseCalibPointChangeGui();

				String selectedCurrentPhase = ref_cmBxCurrentBphaseSelected.getSelectionModel().getSelectedItem().toString();
				ApplicationLauncher.logger.debug("currentBphaseCalibPointsNameOnChange: selectedCurrentPhase : " + selectedCurrentPhase);
				List<CurrentCalibration> currentCalibDataList = lscsCalibrationConfigFileUpdatedKeyValue.getCurrentCalibration();
				for(int phaseSelectionIndex = 0; phaseSelectionIndex < currentCalibDataList.size();phaseSelectionIndex++){
					if(currentCalibDataList.get(phaseSelectionIndex).getCurrentPhase().equals(selectedCurrentPhase)){
						List<CurrentTap> currentTapList = currentCalibDataList.get(phaseSelectionIndex).getCurrentTap();
						if(currentTapList.size()>0){

							int selectedCurrentTapMaxIndex = ref_cmBxCurrentBphaseTapMaxList.getSelectionModel().getSelectedIndex();
							ApplicationLauncher.logger.debug("currentBphaseCalibPointsNameOnChange: selectedCurrentTapMax : " + ref_cmBxCurrentBphaseTapMaxList.getSelectionModel().getSelectedItem());
							//ref_cmBxCurrentTapMaxList.getSelectionModel().select(selectedCurrentTapMaxIndex);
							//String tapSelectionCurrentRelayCode = currentTapList.get(selectedCurrentTapMaxIndex).getCurrentRelayId();
							//ref_txtCurrentTapSelectionRelayCode.setText(tapSelectionCurrentRelayCode);

							List<CalibPoints> currentCalibPoints = currentTapList.get(selectedCurrentTapMaxIndex).getCalibPoints();
							if(currentCalibPoints.size()>0){
								try{
									int selectedCurrentCalibPointNameIndex = ref_cmBxCurrentBphaseCalibPointNameList.getSelectionModel().getSelectedIndex();
									ApplicationLauncher.logger.debug("currentBphaseCalibPointsNameOnChange: selectedCurrentCalibPointName : " + ref_cmBxCurrentBphaseCalibPointNameList.getSelectionModel().getSelectedItem());
									//ref_cmBxCurrentCalibPointNameList.getSelectionModel().select(0);
									float calibPointCurrentValue = currentCalibPoints.get(selectedCurrentCalibPointNameIndex).getCalibPointValue();
									ref_txtCurrentBphaseCalibPointValue.setText(String.valueOf(calibPointCurrentValue));
									long calibPointRmsValue = currentCalibPoints.get(selectedCurrentCalibPointNameIndex).getRmsValue();
									ref_txtCurrentBphasePresentRmsValue.setText(String.valueOf(calibPointRmsValue));
									
									double calibPointGainValue = currentCalibPoints.get(selectedCurrentCalibPointNameIndex).getGainValue();
									ref_txtCurrentBphasePresentGainValue.setText(String.valueOf(calibPointGainValue));
									double calibPointOffsetValue = currentCalibPoints.get(selectedCurrentCalibPointNameIndex).getOffsetValue();
									ref_txtCurrentBphasePresentOffsetValue.setText(String.valueOf(calibPointOffsetValue));
								}catch(Exception e){
									e.printStackTrace();
									ApplicationLauncher.logger.error("currentBphaseCalibPointsNameOnChange : ExceptionA:"+e.getMessage());
								}
							}
						}

					}
				}
			}
		}
		//});

	}

	public static String getLastGuiVoltageRphaseSelected() {
		return lastGuiVoltageRphaseSelected;
	}

	public static void setLastGuiVoltageRphaseSelected(String lastGuiVoltageRphaseSelected) {
		LscsCalibrationController.lastGuiVoltageRphaseSelected = lastGuiVoltageRphaseSelected;
	}

	public static String getLastGuiCurrentRphaseSelected() {
		return lastGuiCurrentRphaseSelected;
	}

	public static void setLastGuiCurrentRphaseSelected(String lastGuiCurrentRphaseSelected) {
		LscsCalibrationController.lastGuiCurrentRphaseSelected = lastGuiCurrentRphaseSelected;
	}

	public static String getLastGuiVoltageRphaseTapMaxList() {
		return lastGuiVoltageRphaseTapMaxList;
	}

	public static void setLastGuiVoltageRphaseTapMaxList(String lastGuiVoltageRphaseTapMaxList) {
		LscsCalibrationController.lastGuiVoltageRphaseTapMaxList = lastGuiVoltageRphaseTapMaxList;
	}

	public static String getLastGuiVoltageRphaseCalibPointNameList() {
		return lastGuiVoltageRphaseCalibPointNameList;
	}

	public static void setLastGuiVoltageRphaseCalibPointNameList(String lastGuiVoltageRphaseCalibPointNameList) {
		LscsCalibrationController.lastGuiVoltageRphaseCalibPointNameList = lastGuiVoltageRphaseCalibPointNameList;
	}

	public static String getLastGuiCurrentRphaseTapMaxList() {
		return lastGuiCurrentRphaseTapMaxList;
	}

	public static void setLastGuiCurrentRphaseTapMaxList(String lastGuiCurrentRphaseTapMaxList) {
		LscsCalibrationController.lastGuiCurrentRphaseTapMaxList = lastGuiCurrentRphaseTapMaxList;
	}

	public static String getLastGuiCurrentRphaseCalibPointNameList() {
		return lastGuiCurrentRphaseCalibPointNameList;
	}

	public static void setLastGuiCurrentRphaseCalibPointNameList(String lastGuiCurrentRphaseCalibPointNameList) {
		LscsCalibrationController.lastGuiCurrentRphaseCalibPointNameList = lastGuiCurrentRphaseCalibPointNameList;
	}

	public boolean isLastRdBtnCurrentYphaseOnlyValue() {
		return lastRdBtnCurrentYphaseOnlyValue;
	}

	public void setLastRdBtnCurrentYphaseOnlyValue(boolean lastRdBtnCurrentYphaseOnlyValue) {
		this.lastRdBtnCurrentYphaseOnlyValue = lastRdBtnCurrentYphaseOnlyValue;
	}

	public boolean isLastRdBtnCurrentBphaseOnlyValue() {
		return lastRdBtnCurrentBphaseOnlyValue;
	}

	public void setLastRdBtnCurrentBphaseOnlyValue(boolean lastRdBtnCurrentBphaseOnlyValue) {
		this.lastRdBtnCurrentBphaseOnlyValue = lastRdBtnCurrentBphaseOnlyValue;
	}

	public boolean isLastrdBtnCurrentAllPhase() {
		return lastrdBtnCurrentAllPhase;
	}

	public void setLastrdBtnCurrentAllPhase(boolean lastrdBtnCurrentAllPhase) {
		this.lastrdBtnCurrentAllPhase = lastrdBtnCurrentAllPhase;
	}

	public String getLastCmBxCurrentRphaseSelected() {
		return lastCmBxCurrentRphaseSelected;
	}

	public void setLastCmBxCurrentRphaseSelected(String lastCmBxCurrentRphaseSelected) {
		this.lastCmBxCurrentRphaseSelected = lastCmBxCurrentRphaseSelected;
	}

	public String getLastTxtCurrentRphaseNoOfTapping() {
		return lastTxtCurrentRphaseNoOfTapping;
	}

	public void setLastTxtCurrentRphaseNoOfTapping(String lastTxtCurrentRphaseNoOfTapping) {
		this.lastTxtCurrentRphaseNoOfTapping = lastTxtCurrentRphaseNoOfTapping;
	}

	public String getLastCmBxCurrentRphaseTapMaxList() {
		return lastCmBxCurrentRphaseTapMaxList;
	}

	public void setLastCmBxCurrentRphaseTapMaxList(String lastCmBxCurrentRphaseTapMaxList) {
		this.lastCmBxCurrentRphaseTapMaxList = lastCmBxCurrentRphaseTapMaxList;
	}

	public String getLastTxtCurrentRphaseTapSelectionRelayCode() {
		return lastTxtCurrentRphaseTapSelectionRelayCode;
	}

	public void setLastTxtCurrentRphaseTapSelectionRelayCode(String lastTxtCurrentRphaseTapSelectionRelayCode) {
		this.lastTxtCurrentRphaseTapSelectionRelayCode = lastTxtCurrentRphaseTapSelectionRelayCode;
	}

	public String getLastTxtCurrentRphaseNoOfCalibPointEachTap() {
		return lastTxtCurrentRphaseNoOfCalibPointEachTap;
	}

	public void setLastTxtCurrentRphaseNoOfCalibPointEachTap(String lastTxtCurrentRphaseNoOfCalibPointEachTap) {
		this.lastTxtCurrentRphaseNoOfCalibPointEachTap = lastTxtCurrentRphaseNoOfCalibPointEachTap;
	}

	public String getLastCmBxCurrentRphaseCalibPointNameList() {
		return lastCmBxCurrentRphaseCalibPointNameList;
	}

	public void setLastCmBxCurrentRphaseCalibPointNameList(String lastCmBxCurrentRphaseCalibPointNameList) {
		this.lastCmBxCurrentRphaseCalibPointNameList = lastCmBxCurrentRphaseCalibPointNameList;
	}

	public String getLastTxtCurrentRphaseCalibPointValue() {
		return lastTxtCurrentRphaseCalibPointValue;
	}

	public void setLastTxtCurrentRphaseCalibPointValue(String lastTxtCurrentRphaseCalibPointValue) {
		this.lastTxtCurrentRphaseCalibPointValue = lastTxtCurrentRphaseCalibPointValue;
	}
	
	public void updateCurrentInGui(String rPhaseDisplayData,String yPhaseDisplayData,String bPhaseDisplayData){
		if(!ProcalFeatureEnable.REFSTD_CONNECTED_NONE){
			if(ref_titledPaneCurrentCalib.isExpanded()){
				
				ref_txtCurrentRphaseUpdatedInRefStd.setText(rPhaseDisplayData);
				ref_txtCurrentYphaseUpdatedInRefStd.setText(yPhaseDisplayData);
				ref_txtCurrentBphaseUpdatedInRefStd.setText(bPhaseDisplayData);
				calculateNewCurrentRmsClick();
			}
		}
	}
	
	public void updateVoltageInGui(String rPhaseDisplayData,String yPhaseDisplayData,String bPhaseDisplayData){
		if(!ProcalFeatureEnable.REFSTD_CONNECTED_NONE){
			if(ref_titledPaneVoltCalib.isExpanded()){
				
				ref_txtVoltageRphaseUpdatedInRefStd.setText(rPhaseDisplayData);
				ref_txtVoltageYphaseUpdatedInRefStd.setText(yPhaseDisplayData);
				ref_txtVoltageBphaseUpdatedInRefStd.setText(bPhaseDisplayData);
				calculateNewVoltageRmsClick();
			}
		}
	}
	




}
