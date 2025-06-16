package com.tasnetwork.calibration.energymeter.deployment;

import java.net.URL;
import java.util.ResourceBundle;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.calib.LscsCalibrationController;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantPowerSourceLscs;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.device.Communicator;
import com.tasnetwork.calibration.energymeter.device.Data_PowerSourceFeedBack;
import com.tasnetwork.calibration.energymeter.device.SerialDataBofaRefStd;
import com.tasnetwork.calibration.energymeter.device.SerialDataMteRefStd;
import com.tasnetwork.calibration.energymeter.device.SerialDataRadiantRefStd;
import com.tasnetwork.calibration.energymeter.device.SerialDataRefStdKiggs;
import com.tasnetwork.calibration.energymeter.device.SerialDataRefStdKre;
import com.tasnetwork.calibration.energymeter.device.SerialDataSandsRefStd;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;

public class DisplayInstantMetricsController implements Initializable{
	
	
	@FXML private Label lbl_R_PhaseActivePowerUnit;
	@FXML private Label lbl_Y_PhaseActivePowerUnit;
	@FXML private Label lbl_B_PhaseActivePowerUnit;

	@FXML private Label lbl_R_PhaseReactivePowerUnit;
	@FXML private Label lbl_Y_PhaseReactivePowerUnit;
	@FXML private Label lbl_B_PhaseReactivePowerUnit;

	@FXML private Label lbl_R_PhaseApparentPowerUnit;
	@FXML private Label lbl_Y_PhaseApparentPowerUnit;
	@FXML private Label lbl_B_PhaseApparentPowerUnit;


	private static Label ref_lbl_R_PhaseActivePowerUnit;
	private static Label ref_lbl_Y_PhaseActivePowerUnit;
	private static Label ref_lbl_B_PhaseActivePowerUnit;

	private static Label ref_lbl_R_PhaseReactivePowerUnit;
	private static Label ref_lbl_Y_PhaseReactivePowerUnit;
	private static Label ref_lbl_B_PhaseReactivePowerUnit;

	private static Label ref_lbl_R_PhaseApparentPowerUnit;
	private static Label ref_lbl_Y_PhaseApparentPowerUnit;
	private static Label ref_lbl_B_PhaseApparentPowerUnit;

	
	@FXML TextField txtR_PhaseVolt;
	@FXML TextField txtR_PhaseCurrent;
	@FXML TextField txtR_PhasePowerFactor;
	@FXML TextField txtR_PhaseDegreePhase;
	@FXML TextField txtR_PhaseFreq;
	@FXML TextField txtR_PhaseWatt;
	@FXML TextField txtR_PhaseVA;
	@FXML TextField txtR_PhaseWh;
	@FXML TextField txtR_PhaseVAR;
	
	
	public static TextField ref_txtR_PhaseVolt;
	public static TextField ref_txtR_PhaseCurrent;
	public static TextField ref_txtR_PhasePowerFactor;
	public static TextField ref_txtR_PhaseDegreePhase;
	public static TextField ref_txtR_PhaseFreq;
	public static TextField ref_txtR_PhaseWatt;
	public static TextField ref_txtR_PhaseVA;
	public static TextField ref_txtR_PhaseWh;
	public static TextField ref_txtR_PhaseVAR;
	
	@FXML TextField txtY_PhaseVolt;
	@FXML TextField txtY_PhaseCurrent;
	@FXML TextField txtY_PhasePowerFactor;
	@FXML TextField txtY_PhaseDegreePhase;
	@FXML TextField txtY_PhaseFreq;
	@FXML TextField txtY_PhaseWatt;
	@FXML TextField txtY_PhaseVA;
	@FXML TextField txtY_PhaseWh;
	@FXML TextField txtY_PhaseVAR;
	
	public static TextField ref_txtY_PhaseVolt;
	public static TextField ref_txtY_PhaseCurrent;
	public static TextField ref_txtY_PhasePowerFactor;
	public static TextField ref_txtY_PhaseDegreePhase;
	public static TextField ref_txtY_PhaseFreq;
	public static TextField ref_txtY_PhaseWatt;
	public static TextField ref_txtY_PhaseVA;
	public static TextField ref_txtY_PhaseWh;
	public static TextField ref_txtY_PhaseVAR;
	//public static TextField ref_txtTimeLeft;
	
	
	@FXML TextField txtB_PhaseVolt;
	@FXML TextField txtB_PhaseCurrent;
	@FXML TextField txtB_PhasePowerFactor;
	@FXML TextField txtB_PhaseDegreePhase;
	@FXML TextField txtB_PhaseFreq;
	@FXML TextField txtB_PhaseWatt;
	@FXML TextField txtB_PhaseVA;
	@FXML TextField txtB_PhaseWh;
	@FXML TextField txtB_PhaseVAR;
	
	@FXML TitledPane A_PhasePane;
	@FXML TitledPane B_PhasePane;
	@FXML TitledPane C_PhasePane;
	
	@FXML Label labelActiveRectiveUnit_R;
	@FXML Label labelActiveRectiveUnit_Y;
	@FXML Label labelActiveRectiveUnit_B;

	public static Label ref_labelActiveRectiveUnit_R;
	public static Label ref_labelActiveRectiveUnit_Y;
	public static Label ref_labelActiveRectiveUnit_B;
	public static TextField ref_txtB_PhaseVolt;
	public static TextField ref_txtB_PhaseCurrent;
	public static TextField ref_txtB_PhasePowerFactor;
	public static TextField ref_txtB_PhaseDegreePhase;
	public static TextField ref_txtB_PhaseFreq;
	public static TextField ref_txtB_PhaseWatt;
	public static TextField ref_txtB_PhaseVA;
	public static TextField ref_txtB_PhaseWh;
	public static TextField ref_txtB_PhaseVAR;
	
	@FXML
	private ProgressBar  barRefStdRefresh;
	public static  ProgressBar  ref_barRefStdRefresh;
	

	
	private static StringProperty R_PhaseVolt = new SimpleStringProperty();
	private static StringProperty R_PhaseCurrent = new SimpleStringProperty();
	private static StringProperty R_PhasePowerFactor = new SimpleStringProperty();
	private static StringProperty R_PhaseDegreePhase = new SimpleStringProperty();
	private static StringProperty R_PhaseFreq = new SimpleStringProperty();
	private static StringProperty R_PhaseWatt = new SimpleStringProperty();
	private static StringProperty R_PhaseVA = new SimpleStringProperty();
	private static StringProperty R_PhaseVAR = new SimpleStringProperty();
	private static StringProperty R_PhaseWh = new SimpleStringProperty();
	
	private static StringProperty Y_PhaseVolt = new SimpleStringProperty();
	private static StringProperty Y_PhaseCurrent = new SimpleStringProperty();
	private static StringProperty Y_PhasePowerFactor = new SimpleStringProperty();
	private static StringProperty Y_PhaseDegreePhase = new SimpleStringProperty();
	private static StringProperty Y_PhaseFreq = new SimpleStringProperty();
	private static StringProperty Y_PhaseWatt = new SimpleStringProperty();
	private static StringProperty Y_PhaseVA = new SimpleStringProperty();
	private static StringProperty Y_PhaseVAR = new SimpleStringProperty();
	private static StringProperty Y_PhaseWh = new SimpleStringProperty();
	
	private static StringProperty B_PhaseVolt = new SimpleStringProperty();
	private static StringProperty B_PhaseCurrent = new SimpleStringProperty();
	private static StringProperty B_PhasePowerFactor = new SimpleStringProperty();
	private static StringProperty B_PhaseDegreePhase = new SimpleStringProperty();
	private static StringProperty B_PhaseFreq = new SimpleStringProperty();
	private static StringProperty B_PhaseWatt = new SimpleStringProperty();
	private static StringProperty B_PhaseVA = new SimpleStringProperty();
	private static StringProperty B_PhaseVAR = new SimpleStringProperty();
	private static StringProperty B_PhaseWh = new SimpleStringProperty();
	

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
		if(ProcalFeatureEnable.PHASE_DISPLAY_ENABLE_FEATURE){
			A_PhasePane.setText(ConstantApp.FIRST_PHASE_DISPLAY_NAME );
			B_PhasePane.setText(ConstantApp.SECOND_PHASE_DISPLAY_NAME);
			C_PhasePane.setText(ConstantApp.THIRD_PHASE_DISPLAY_NAME );
		}
    	
		ref_txtR_PhaseVolt = txtR_PhaseVolt;
		ref_txtR_PhaseCurrent = txtR_PhaseCurrent;
		ref_txtR_PhasePowerFactor= txtR_PhasePowerFactor;
		ref_txtR_PhaseDegreePhase= txtR_PhaseDegreePhase;
		ref_txtR_PhaseFreq = txtR_PhaseFreq;
		ref_txtR_PhaseWatt = txtR_PhaseWatt;
		ref_txtR_PhaseVA = txtR_PhaseVA;
		ref_txtR_PhaseVAR = txtR_PhaseVAR;
		ref_txtR_PhaseWh = txtR_PhaseWh;
		
		ref_txtY_PhaseVolt = txtY_PhaseVolt;
		ref_txtY_PhaseCurrent = txtY_PhaseCurrent;
		ref_txtY_PhasePowerFactor= txtY_PhasePowerFactor;
		ref_txtY_PhaseDegreePhase= txtY_PhaseDegreePhase;
		ref_txtY_PhaseFreq = txtY_PhaseFreq;
		ref_txtY_PhaseWatt = txtY_PhaseWatt;
		ref_txtY_PhaseVA = txtY_PhaseVA;
		ref_txtY_PhaseVAR = txtY_PhaseVAR;
		ref_txtY_PhaseWh = txtY_PhaseWh;
		
		ref_txtB_PhaseVolt = txtB_PhaseVolt;
		ref_txtB_PhaseCurrent = txtB_PhaseCurrent;
		ref_txtB_PhasePowerFactor= txtB_PhasePowerFactor;
		ref_txtB_PhaseDegreePhase= txtB_PhaseDegreePhase;
		ref_txtB_PhaseFreq = txtB_PhaseFreq;
		ref_txtB_PhaseWatt = txtB_PhaseWatt;
		ref_txtB_PhaseVA = txtB_PhaseVA;
		ref_txtB_PhaseVAR = txtB_PhaseVAR;
		ref_txtB_PhaseWh = txtB_PhaseWh;
		
		ref_barRefStdRefresh = barRefStdRefresh;
		ref_labelActiveRectiveUnit_R = labelActiveRectiveUnit_R;
		ref_labelActiveRectiveUnit_Y = labelActiveRectiveUnit_Y;
		ref_labelActiveRectiveUnit_B = labelActiveRectiveUnit_B;
		
		ref_lbl_R_PhaseActivePowerUnit = lbl_R_PhaseActivePowerUnit;
		ref_lbl_Y_PhaseActivePowerUnit = lbl_Y_PhaseActivePowerUnit;
		ref_lbl_B_PhaseActivePowerUnit = lbl_B_PhaseActivePowerUnit;

		ref_lbl_R_PhaseReactivePowerUnit = lbl_R_PhaseReactivePowerUnit;
		ref_lbl_Y_PhaseReactivePowerUnit = lbl_Y_PhaseReactivePowerUnit;
		ref_lbl_B_PhaseReactivePowerUnit = lbl_B_PhaseReactivePowerUnit;

		ref_lbl_R_PhaseApparentPowerUnit = lbl_R_PhaseApparentPowerUnit;
		ref_lbl_Y_PhaseApparentPowerUnit = lbl_Y_PhaseApparentPowerUnit;
		ref_lbl_B_PhaseApparentPowerUnit = lbl_B_PhaseApparentPowerUnit;
		
		
		BindPropertytoTextField();
		updateGuiFields();
/*		if(ConstantAppConfig.INSTANT_METRICS_ENERGY_DISPLAY_IN_KWH){
			Platform.runLater(()->{
				ref_labelActiveRectiveUnit_R.setText("kWh");
				ref_labelActiveRectiveUnit_R.setText("kWh");
				ref_labelActiveRectiveUnit_R.setText("kWh");
			});
			
		}*/

    }
    
    public void updateGuiFields(){
    	if(ProcalFeatureEnable.SANDS_REFSTD_CONNECTED){
	    	Platform.runLater(() -> {
	    		
	    		ref_lbl_R_PhaseActivePowerUnit.setText("kW");
	    		ref_lbl_Y_PhaseActivePowerUnit.setText("kW");
	    		ref_lbl_B_PhaseActivePowerUnit.setText("kW");
	
	    		ref_lbl_R_PhaseReactivePowerUnit.setText("kVAR");
	    		ref_lbl_Y_PhaseReactivePowerUnit.setText("kVAR");
	    		ref_lbl_B_PhaseReactivePowerUnit.setText("kVAR");
	
	    		ref_lbl_R_PhaseApparentPowerUnit.setText("kVA");
	    		ref_lbl_Y_PhaseApparentPowerUnit.setText("kVA");
	    		ref_lbl_B_PhaseApparentPowerUnit.setText("kVA");
	    		
			});
    	}else if(ProcalFeatureEnable.KRE_REFSTD_CONNECTED){
    		
    	}
    }
    
    public static void set_labelActiveRectiveUnit_R(String InputData){
    	ref_labelActiveRectiveUnit_R.setText(InputData);
    }
    public static void set_labelActiveRectiveUnit_Y(String InputData){
    	ref_labelActiveRectiveUnit_Y.setText(InputData);
    }
    public static void set_labelActiveRectiveUnit_B(String InputData){
    	ref_labelActiveRectiveUnit_B.setText(InputData);
    }
    
    public void BindPropertytoTextField(){
    	
    	ref_txtR_PhaseVolt.textProperty().bind(R_PhaseVolt);
    	ref_txtR_PhaseCurrent.textProperty().bind(R_PhaseCurrent);
    	ref_txtR_PhasePowerFactor.textProperty().bind(R_PhasePowerFactor);
    	ref_txtR_PhaseDegreePhase.textProperty().bind(R_PhaseDegreePhase);
    	ref_txtR_PhaseFreq.textProperty().bind(R_PhaseFreq);
    	ref_txtR_PhaseWatt.textProperty().bind(R_PhaseWatt);
    	ref_txtR_PhaseVA.textProperty().bind(R_PhaseVA);
    	ref_txtR_PhaseVAR.textProperty().bind(R_PhaseVAR);
    	ref_txtR_PhaseWh.textProperty().bind(R_PhaseWh);
 
    	
    	ref_txtY_PhaseVolt.textProperty().bind(Y_PhaseVolt);
    	ref_txtY_PhaseCurrent.textProperty().bind(Y_PhaseCurrent);
    	ref_txtY_PhasePowerFactor.textProperty().bind(Y_PhasePowerFactor);
    	ref_txtY_PhaseDegreePhase.textProperty().bind(Y_PhaseDegreePhase);
    	ref_txtY_PhaseFreq.textProperty().bind(Y_PhaseFreq);
    	ref_txtY_PhaseWatt.textProperty().bind(Y_PhaseWatt);
    	ref_txtY_PhaseVA.textProperty().bind(Y_PhaseVA);
    	ref_txtY_PhaseVAR.textProperty().bind(Y_PhaseVAR);
    	ref_txtY_PhaseWh.textProperty().bind(Y_PhaseWh);
    	
    	ref_txtB_PhaseVolt.textProperty().bind(B_PhaseVolt);
    	ref_txtB_PhaseCurrent.textProperty().bind(B_PhaseCurrent);
    	ref_txtB_PhasePowerFactor.textProperty().bind(B_PhasePowerFactor);
    	ref_txtB_PhaseDegreePhase.textProperty().bind(B_PhaseDegreePhase);
    	ref_txtB_PhaseFreq.textProperty().bind(B_PhaseFreq);
    	ref_txtB_PhaseWatt.textProperty().bind(B_PhaseWatt);
    	ref_txtB_PhaseVA.textProperty().bind(B_PhaseVA);
    	ref_txtB_PhaseVAR.textProperty().bind(B_PhaseVAR);
    	ref_txtB_PhaseWh.textProperty().bind(B_PhaseWh);
    	

    }
    

    
    public void progressDemoStart(){
    	ProjectExecutionController.RefStdProgressBarStart();
    }
    public void progressDemoStop(){
    	ProjectExecutionController.RefStdProgressBarStop();
    }
    
    
    
    public static void deviceBofaRefStdDataDisplayUpdate_R_Phase(final SerialDataBofaRefStd R_PhaseData){

    	//ApplicationLauncher.logger.info("deviceBofaRefStdDataDisplayUpdate_R_Phase : Entry");
    	try{
    		Platform.runLater(() -> {
	    		R_PhaseVolt.setValue(R_PhaseData.getVoltageDisplayData());
	    		R_PhaseCurrent.setValue(R_PhaseData.getCurrentDisplayData());
	    		R_PhasePowerFactor.setValue(R_PhaseData.getPowerFactorData());
	    		R_PhaseFreq.setValue(R_PhaseData.getFreqDisplayData());
	    		R_PhaseWatt.setValue(R_PhaseData.getWattDisplayData());
	    		R_PhaseVA.setValue(R_PhaseData.getVA_DisplayData());
	    		R_PhaseVAR.setValue(R_PhaseData.getVAR_DisplayData());
	    		R_PhaseDegreePhase.setValue(R_PhaseData.getDegreePhaseData());
	    		R_PhaseWh.setValue(R_PhaseData.getActiveEnergyInKwhDisplayData());
	    		//R_PhaseWh.setValue(R_PhaseData.getActiveEnergyInWhDisplayData());
    		});

    		if(!R_PhaseData.getVoltageDisplayData().isEmpty()){
    			ProjectExecutionController.setFeedbackR_phaseVolt(R_PhaseData.getVoltageDisplayData());
    		}
    		if(!R_PhaseData.getCurrentDisplayData().isEmpty()){
    			ProjectExecutionController.setFeedbackR_phaseCurrent(R_PhaseData.getCurrentDisplayData());
    		}
    		if(!R_PhaseData.getDegreePhaseData().isEmpty()){
    			ProjectExecutionController.setFeedbackR_phaseDegree(R_PhaseData.getDegreePhaseData());
    		}
    		//if(!R_PhaseData.getFreqDisplayData().isEmpty()){
    			ProjectExecutionController.setFeedbackR_Frequency(R_PhaseData.getFreqDisplayData());
    			//ApplicationLauncher.logger.debug("deviceBofaRefStdDataDisplayUpdate_R_Phase : get_PwrSrc_Freq: " + String.valueOf(DeviceDataManagerController.get_PwrSrc_Freq()));
    	    	//ProjectExecutionController.setFeedbackR_Frequency(String.valueOf(DeviceDataManagerController.get_PwrSrc_Freq()) );
    		//}
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("deviceBofaRefStdDataDisplayUpdate_R_Phase :Exception:"+ e.getMessage());
    	}

	}
    

    
    public static void DeviceRefStdDataDisplayUpdate_R_Phase(final SerialDataRadiantRefStd R_PhaseData){

    	ApplicationLauncher.logger.info("DeviceRefStdDataDisplayUpdate_R_Phase : Entry");
    	try{
    		Platform.runLater(() -> {
	    		R_PhaseVolt.setValue(R_PhaseData.VoltageDisplayData);
	    		R_PhaseCurrent.setValue(R_PhaseData.CurrentDisplayData);
	    		R_PhasePowerFactor.setValue(R_PhaseData.PowerFactorData);
	    		R_PhaseFreq.setValue(R_PhaseData.FreqDisplayData);
	    		R_PhaseWatt.setValue(R_PhaseData.WattDisplayData);
	    		R_PhaseVA.setValue(R_PhaseData.VA_DisplayData);
	    		R_PhaseVAR.setValue(R_PhaseData.VAR_DisplayData);
	    		R_PhaseDegreePhase.setValue(R_PhaseData.DegreePhaseData);
    		});

    		if(!R_PhaseData.VoltageDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_phaseVolt(R_PhaseData.VoltageDisplayData);
    		}
    		if(!R_PhaseData.CurrentDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_phaseCurrent(R_PhaseData.CurrentDisplayData);
    		}
    		if(!R_PhaseData.DegreePhaseData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_phaseDegree(R_PhaseData.DegreePhaseData);
    		}
    		if(!R_PhaseData.FreqDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_Frequency(R_PhaseData.FreqDisplayData);
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdate_R_Phase :Exception:"+ e.getMessage());
    	}

	}
    
    public static void DeviceRefStdDataDisplayUpdate_Y_Phase(final SerialDataRadiantRefStd Y_PhaseData){


    	ApplicationLauncher.logger.info("DeviceRefStdDataDisplayUpdate_Y_Phase : Entry");
    	try{
    		Platform.runLater(() -> {
	    		Y_PhaseVolt.setValue(Y_PhaseData.VoltageDisplayData);
	    		Y_PhaseCurrent.setValue(Y_PhaseData.CurrentDisplayData);
	    		Y_PhasePowerFactor.setValue(Y_PhaseData.PowerFactorData);
	    		Y_PhaseFreq.setValue(Y_PhaseData.FreqDisplayData);
	    		Y_PhaseWatt.setValue(Y_PhaseData.WattDisplayData);
	    		Y_PhaseVA.setValue(Y_PhaseData.VA_DisplayData);
	    		Y_PhaseVAR.setValue(Y_PhaseData.VAR_DisplayData);
	    		Y_PhaseDegreePhase.setValue(Y_PhaseData.DegreePhaseData);
    		});
    		

    		if(!Y_PhaseData.VoltageDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackY_phaseVolt(Y_PhaseData.VoltageDisplayData);
    		}
    		if(!Y_PhaseData.CurrentDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackY_phaseCurrent(Y_PhaseData.CurrentDisplayData);
    		}
    		if(!Y_PhaseData.DegreePhaseData.isEmpty()){
    			ProjectExecutionController.setFeedbackY_phaseDegree(Y_PhaseData.DegreePhaseData);
    		}
    		if(!Y_PhaseData.FreqDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackY_Frequency(Y_PhaseData.FreqDisplayData);
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdate_Y_Phase :Exception:"+ e.getMessage());
    	}


	}
	
	public static void DeviceRefStdDataDisplayUpdate_B_Phase(final SerialDataRadiantRefStd B_PhaseData){

		
		ApplicationLauncher.logger.info("DeviceRefStdDataDisplayUpdate_B_Phase : Entry");
		try{
			Platform.runLater(() -> {
	    		B_PhaseVolt.setValue(B_PhaseData.VoltageDisplayData);
	    		B_PhaseCurrent.setValue(B_PhaseData.CurrentDisplayData);
	    		B_PhasePowerFactor.setValue(B_PhaseData.PowerFactorData);
	    		B_PhaseFreq.setValue(B_PhaseData.FreqDisplayData);
	    		B_PhaseWatt.setValue(B_PhaseData.WattDisplayData);
	    		B_PhaseVA.setValue(B_PhaseData.VA_DisplayData);
	    		B_PhaseVAR.setValue(B_PhaseData.VAR_DisplayData);
	    		B_PhaseDegreePhase.setValue(B_PhaseData.DegreePhaseData);
			});

			
			if(!B_PhaseData.VoltageDisplayData.isEmpty()){
				ProjectExecutionController.setFeedbackB_phaseVolt(B_PhaseData.VoltageDisplayData);
			}
			if(!B_PhaseData.CurrentDisplayData.isEmpty()){
				ProjectExecutionController.setFeedbackB_phaseCurrent(B_PhaseData.CurrentDisplayData);
			}
			if(!B_PhaseData.DegreePhaseData.isEmpty()){
				ProjectExecutionController.setFeedbackB_phaseDegree(B_PhaseData.DegreePhaseData);
			}
    		if(!B_PhaseData.FreqDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackB_Frequency(B_PhaseData.FreqDisplayData);
    		}
		}catch(Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdate_B_Phase :Exception:"+ e.getMessage());
		}

	}
	
	public static void DeviceRefStdDataDisplayUpdateActiveEnergy(String R_phase, String Y_phase, String B_phase){
		Platform.runLater(() -> {
			R_PhaseWh.setValue(R_phase);
			Y_PhaseWh.setValue(Y_phase);
			B_PhaseWh.setValue(B_phase);
		});
	}

	public static void ResetInstantMetrics(){
    	Communicator SerialPortObj = null;
		SerialDataRadiantRefStd Reset_Value = new SerialDataRadiantRefStd(SerialPortObj);
		Reset_Value.VoltageDisplayData = "0";
		Reset_Value.CurrentDisplayData = "0";
		Reset_Value.PowerFactorData = "0";
		Reset_Value.FreqDisplayData = "0";
		Reset_Value.WattDisplayData = "0";
		Reset_Value.VA_DisplayData = "0";
		Reset_Value.VAR_DisplayData = "0";
		Reset_Value.DegreePhaseData = "0";
		DeviceRefStdDataDisplayUpdate_R_Phase(Reset_Value);
		DeviceRefStdDataDisplayUpdate_Y_Phase(Reset_Value);
		DeviceRefStdDataDisplayUpdate_B_Phase(Reset_Value);
		Platform.runLater(() -> {
			R_PhaseWh.setValue("");
			Y_PhaseWh.setValue("");
			B_PhaseWh.setValue("");
		});
	}
	public static void ResetInstantMetrics_Wh_Display(){

		Platform.runLater(() -> {
			R_PhaseWh.setValue("");
			Y_PhaseWh.setValue("");
			B_PhaseWh.setValue("");
		});
	}
	
    public static void DeviceRefStdDataDisplayUpdateVoltage(final SerialDataRefStdKiggs voltageData){

    	//ApplicationLauncher.logger.info("DeviceRefStdDataDisplayUpdateVoltage : Entry");
    	try{
    		Platform.runLater(() -> {
	    		R_PhaseVolt.setValue(voltageData.rPhaseVoltageDisplayData);
/*	    		Y_PhaseVolt.setValue(voltageData.yPhaseVoltageDisplayData);
	    		B_PhaseVolt.setValue(voltageData.bPhaseVoltageDisplayData);*/

    		});

    		if(!voltageData.rPhaseVoltageDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_phaseVolt(voltageData.rPhaseVoltageDisplayData);
    		}
/*    		if(!voltageData.yPhaseVoltageDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackY_phaseVolt(voltageData.yPhaseVoltageDisplayData);
    		}
    		if(!voltageData.bPhaseVoltageDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackB_phaseVolt(voltageData.bPhaseVoltageDisplayData);
    		}*/

    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdateVoltage :Exception:"+ e.getMessage());
    	}

	}
	   
    public static void DeviceRefStdDataDisplayUpdateVoltage(final SerialDataMteRefStd voltageData){

    	//ApplicationLauncher.logger.info("DeviceRefStdDataDisplayUpdateVoltage : Entry");
    	try{
    		Platform.runLater(() -> {
	    		R_PhaseVolt.setValue(voltageData.rPhaseVoltageDisplayData);
	    		Y_PhaseVolt.setValue(voltageData.yPhaseVoltageDisplayData);
	    		B_PhaseVolt.setValue(voltageData.bPhaseVoltageDisplayData);
/*	    		R_PhaseCurrent.setValue(R_PhaseData.CurrentDisplayData);
	    		R_PhasePowerFactor.setValue(R_PhaseData.PowerFactorData);
	    		R_PhaseFreq.setValue(R_PhaseData.FreqDisplayData);
	    		R_PhaseWatt.setValue(R_PhaseData.WattDisplayData);
	    		R_PhaseVA.setValue(R_PhaseData.VA_DisplayData);
	    		R_PhaseVAR.setValue(R_PhaseData.VAR_DisplayData);
	    		R_PhaseDegreePhase.setValue(R_PhaseData.DegreePhaseData);*/
    		});

    		if(!voltageData.rPhaseVoltageDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_phaseVolt(voltageData.rPhaseVoltageDisplayData);
    		}
    		if(!voltageData.yPhaseVoltageDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackY_phaseVolt(voltageData.yPhaseVoltageDisplayData);
    		}
    		if(!voltageData.bPhaseVoltageDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackB_phaseVolt(voltageData.bPhaseVoltageDisplayData);
    		}
/*    		if(!R_PhaseData.CurrentDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_phaseCurrent(R_PhaseData.CurrentDisplayData);
    		}
    		if(!R_PhaseData.DegreePhaseData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_phaseDegree(R_PhaseData.DegreePhaseData);
    		}
    		if(!R_PhaseData.FreqDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_Frequency(R_PhaseData.FreqDisplayData);
    		}*/
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdateVoltage :Exception:"+ e.getMessage());
    	}

	}
        
    public static void DeviceRefStdDataDisplayUpdateVoltage(final SerialDataRefStdKre voltageData){

    	//ApplicationLauncher.logger.info("DeviceRefStdDataDisplayUpdateVoltage : Entry");
    	try{
    		Platform.runLater(() -> {
	    		R_PhaseVolt.setValue(voltageData.rPhaseVoltageDisplayData);
	    		Y_PhaseVolt.setValue(voltageData.yPhaseVoltageDisplayData);
	    		B_PhaseVolt.setValue(voltageData.bPhaseVoltageDisplayData);
/*	    		R_PhaseCurrent.setValue(R_PhaseData.CurrentDisplayData);
	    		R_PhasePowerFactor.setValue(R_PhaseData.PowerFactorData);
	    		R_PhaseFreq.setValue(R_PhaseData.FreqDisplayData);
	    		R_PhaseWatt.setValue(R_PhaseData.WattDisplayData);
	    		R_PhaseVA.setValue(R_PhaseData.VA_DisplayData);
	    		R_PhaseVAR.setValue(R_PhaseData.VAR_DisplayData);
	    		R_PhaseDegreePhase.setValue(R_PhaseData.DegreePhaseData);*/
    		});

    		if(!voltageData.rPhaseVoltageDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_phaseVolt(voltageData.rPhaseVoltageDisplayData);
    		}
    		if(!voltageData.yPhaseVoltageDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackY_phaseVolt(voltageData.yPhaseVoltageDisplayData);
    		}
    		if(!voltageData.bPhaseVoltageDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackB_phaseVolt(voltageData.bPhaseVoltageDisplayData);
    		}
/*    		if(!R_PhaseData.CurrentDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_phaseCurrent(R_PhaseData.CurrentDisplayData);
    		}
    		if(!R_PhaseData.DegreePhaseData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_phaseDegree(R_PhaseData.DegreePhaseData);
    		}
    		if(!R_PhaseData.FreqDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_Frequency(R_PhaseData.FreqDisplayData);
    		}*/
    		
    		if(ProcalFeatureEnable.AUTO_CALIBRATION_MODE_ENABLE_FEATURE){
    			LscsCalibrationController calibrationObj =  new LscsCalibrationController();
    			//calibrationObj.updateCurrentInGui(rPhaseDisplayDatal,yPhaseDisplayDatal,bPhaseDisplayDatal);
    			calibrationObj.updateVoltageInGui(voltageData.rPhaseVoltageDisplayData,voltageData.yPhaseVoltageDisplayData,voltageData.bPhaseVoltageDisplayData);
    			//txtCurrentRphaseUpdatedInRefStd
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdateVoltage :Exception:"+ e.getMessage());
    	}

	}
    public static void DeviceRefStdDataDisplayUpdateCurrent(final SerialDataMteRefStd currentData){

    	//ApplicationLauncher.logger.info("DeviceRefStdDataDisplayUpdateCurrent : Entry");
    	try{
    		Platform.runLater(() -> {
	    		R_PhaseCurrent.setValue(currentData.rPhaseCurrentDisplayData);
	    		Y_PhaseCurrent.setValue(currentData.yPhaseCurrentDisplayData);
	    		B_PhaseCurrent.setValue(currentData.bPhaseCurrentDisplayData);

    		});

    		if(!currentData.rPhaseCurrentDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_phaseCurrent(currentData.rPhaseCurrentDisplayData);
    		}
    		if(!currentData.yPhaseCurrentDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackY_phaseCurrent(currentData.yPhaseCurrentDisplayData);
    		}
    		if(!currentData.bPhaseCurrentDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackB_phaseCurrent(currentData.bPhaseCurrentDisplayData);
    		}

    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdateCurrent :Exception:"+ e.getMessage());
    	}

	}
    
    
    
    
    public static void DeviceRefStdDataDisplayUpdateCurrent(final SerialDataRefStdKiggs currentData){

    	//ApplicationLauncher.logger.info("DeviceRefStdDataDisplayUpdateCurrent : Entry");
    	try{
    		Platform.runLater(() -> {
	    		R_PhaseCurrent.setValue(currentData.rPhaseCurrentDisplayData);
/*	    		Y_PhaseCurrent.setValue(currentData.yPhaseCurrentDisplayData);
	    		B_PhaseCurrent.setValue(currentData.bPhaseCurrentDisplayData);*/

    		});

    		if(!currentData.rPhaseCurrentDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_phaseCurrent(currentData.rPhaseCurrentDisplayData);
    		}
/*    		if(!currentData.yPhaseCurrentDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackY_phaseCurrent(currentData.yPhaseCurrentDisplayData);
    		}
    		if(!currentData.bPhaseCurrentDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackB_phaseCurrent(currentData.bPhaseCurrentDisplayData);
    		}*/

    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdateCurrent :Exception:"+ e.getMessage());
    	}

	}
    
    public static void devicePowerSourceFeedBackDataDisplayUpdate(final Data_PowerSourceFeedBack displayData){

    	//ApplicationLauncher.logger.info("DeviceRefStdDataDisplayUpdateCurrent : Entry");
    	try{
    		Platform.runLater(() -> {
	    		R_PhaseCurrent.setValue(displayData.getR_PhaseCurrentDisplayData());
	    		Y_PhaseCurrent.setValue(displayData.getY_PhaseCurrentDisplayData());
	    		B_PhaseCurrent.setValue(displayData.getB_PhaseCurrentDisplayData());
	    		
	    		R_PhaseVolt.setValue(displayData.getR_PhaseVoltageDisplayData());
	    		Y_PhaseVolt.setValue(displayData.getY_PhaseVoltageDisplayData());
	    		B_PhaseVolt.setValue(displayData.getB_PhaseVoltageDisplayData());
	    		
	    		R_PhaseDegreePhase.setValue(displayData.getR_PhaseDegreePhaseData());
	    		Y_PhaseDegreePhase.setValue(displayData.getY_PhaseDegreePhaseData());
	    		B_PhaseDegreePhase.setValue(displayData.getB_PhaseDegreePhaseData());
	    		
	    		R_PhasePowerFactor.setValue(displayData.getR_PhasePowerFactorData());
	    		Y_PhasePowerFactor.setValue(displayData.getY_PhasePowerFactorData());
	    		B_PhasePowerFactor.setValue(displayData.getB_PhasePowerFactorData());
	    		
	    		R_PhaseFreq.setValue(displayData.getFreqData());
	    		if(ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED){
		    		Y_PhaseFreq.setValue(displayData.getFreqData());
		    		B_PhaseFreq.setValue(displayData.getFreqData());
	    		}

    		});

/*    		if(!displayData.rPhaseCurrentDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_phaseCurrent(displayData.rPhaseCurrentDisplayData);
    		}
    		if(!displayData.yPhaseCurrentDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackY_phaseCurrent(displayData.yPhaseCurrentDisplayData);
    		}
    		if(!displayData.bPhaseCurrentDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackB_phaseCurrent(displayData.bPhaseCurrentDisplayData);
    		}*/

    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdateCurrent :Exception:"+ e.getMessage());
    	}

	}
    
    public static void DeviceRefStdDataDisplayUpdateCurrent(final SerialDataRefStdKre currentData){

    	//ApplicationLauncher.logger.info("DeviceRefStdDataDisplayUpdateCurrent : Entry");
    	final String rPhaseDisplayData  = currentData.rPhaseCurrentDisplayData;
    	final String yPhaseDisplayData  = currentData.yPhaseCurrentDisplayData;
    	final String bPhaseDisplayData  = currentData.bPhaseCurrentDisplayData;
    	try{
    		Platform.runLater(() -> {
	    		//R_PhaseCurrent.setValue(currentData.rPhaseCurrentDisplayData);
	    		//Y_PhaseCurrent.setValue(currentData.yPhaseCurrentDisplayData);
	    		//B_PhaseCurrent.setValue(currentData.bPhaseCurrentDisplayData);
	    		String rPhaseDisplayDatal = "";
	    		//if(Float.parseFloat(rPhaseDisplayData)> 1.0f){
	    		if(Float.parseFloat(rPhaseDisplayData)> ConstantPowerSourceLscs.CURRENT_RESOLUTION_THRESHOLD){	
	    			rPhaseDisplayDatal = String.format(ConstantApp.DISPLAY_CURRENT_RESOLUTION_HIGH, Float.parseFloat(rPhaseDisplayData));	
					
				}else{
					rPhaseDisplayDatal = String.format(ConstantApp.DISPLAY_CURRENT_RESOLUTION_LOW, Float.parseFloat(rPhaseDisplayData));
				}
	    		
	    		R_PhaseCurrent.setValue(rPhaseDisplayDatal);
	    		
	    		
	    		String yPhaseDisplayDatal = "";
	    		if(Float.parseFloat(yPhaseDisplayData)> 1.0f){
					
	    			yPhaseDisplayDatal = String.format(ConstantApp.DISPLAY_CURRENT_RESOLUTION_HIGH, Float.parseFloat(yPhaseDisplayData));	
					
				}else{
					yPhaseDisplayDatal = String.format(ConstantApp.DISPLAY_CURRENT_RESOLUTION_LOW, Float.parseFloat(yPhaseDisplayData));
				}
	    		
	    		Y_PhaseCurrent.setValue(yPhaseDisplayDatal);
	    		
	    		String bPhaseDisplayDatal = "";
	    		if(Float.parseFloat(bPhaseDisplayData)> 1.0f){
					
	    			bPhaseDisplayDatal = String.format(ConstantApp.DISPLAY_CURRENT_RESOLUTION_HIGH, Float.parseFloat(bPhaseDisplayData));	
					
				}else{
					bPhaseDisplayDatal = String.format(ConstantApp.DISPLAY_CURRENT_RESOLUTION_LOW, Float.parseFloat(bPhaseDisplayData));
				}
	    		
	    		B_PhaseCurrent.setValue(bPhaseDisplayDatal);
	    		if(ProcalFeatureEnable.AUTO_CALIBRATION_MODE_ENABLE_FEATURE){
	    			LscsCalibrationController calibrationObj =  new LscsCalibrationController();
	    			calibrationObj.updateCurrentInGui(rPhaseDisplayDatal,yPhaseDisplayDatal,bPhaseDisplayDatal);
	    			//calibrationObj.updateVoltageInGui(rPhaseDisplayData2,yPhaseDisplayData2,bPhaseDisplayData);
	    			//txtCurrentRphaseUpdatedInRefStd
	    		}
    		});
    		String rPhaseFeedBackData = "";
    		if(!currentData.rPhaseCurrentDisplayData.isEmpty()){
    			if(Float.parseFloat(rPhaseDisplayData)> 1.0f){
					
    				rPhaseFeedBackData = String.format(ConstantApp.FEEDBACK_CONTROL_CURRENT_RESOLUTION_HIGH, Float.parseFloat(rPhaseDisplayData));	
					
				}else{
					rPhaseFeedBackData = String.format(ConstantApp.FEEDBACK_CONTROL_CURRENT_RESOLUTION_LOW, Float.parseFloat(rPhaseDisplayData));
				}
    			ProjectExecutionController.setFeedbackR_phaseCurrent(rPhaseFeedBackData);
    		}
    		String yPhaseFeedBackData = "";
    		if(!currentData.yPhaseCurrentDisplayData.isEmpty()){
    			//ProjectExecutionController.setFeedbackY_phaseCurrent(currentData.yPhaseCurrentDisplayData);
    			if(Float.parseFloat(yPhaseDisplayData)> 1.0f){
					
    				yPhaseFeedBackData = String.format(ConstantApp.FEEDBACK_CONTROL_CURRENT_RESOLUTION_HIGH, Float.parseFloat(yPhaseDisplayData));	
					
				}else{
					yPhaseFeedBackData = String.format(ConstantApp.FEEDBACK_CONTROL_CURRENT_RESOLUTION_LOW, Float.parseFloat(yPhaseDisplayData));
				}
    			ProjectExecutionController.setFeedbackY_phaseCurrent(yPhaseFeedBackData);
    		}
    		String bPhaseFeedBackData = "";
    		if(!currentData.bPhaseCurrentDisplayData.isEmpty()){
    			//ProjectExecutionController.setFeedbackB_phaseCurrent(currentData.bPhaseCurrentDisplayData);
    			if(Float.parseFloat(bPhaseDisplayData)> 1.0f){
					
    				bPhaseFeedBackData = String.format(ConstantApp.FEEDBACK_CONTROL_CURRENT_RESOLUTION_HIGH, Float.parseFloat(bPhaseDisplayData));	
					
				}else{
					bPhaseFeedBackData = String.format(ConstantApp.FEEDBACK_CONTROL_CURRENT_RESOLUTION_LOW, Float.parseFloat(bPhaseDisplayData));
				}
    			ProjectExecutionController.setFeedbackB_phaseCurrent(bPhaseFeedBackData);
    		}

    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("devicePowerSourceFeedBackDataDisplayUpdate :Exception:"+ e.getMessage());
    	}

	}
    
    public static void DeviceRefStdDataDisplayUpdateDegreePhase(final SerialDataMteRefStd degreePhaseData){

    	//ApplicationLauncher.logger.info("DeviceRefStdDataDisplayUpdateCurrent : Entry");
    	try{
    		Platform.runLater(() -> {
    			R_PhaseDegreePhase.setValue(degreePhaseData.rPhaseDegreePhaseData);
	    		Y_PhaseDegreePhase.setValue(degreePhaseData.yPhaseDegreePhaseData);
	    		B_PhaseDegreePhase.setValue(degreePhaseData.bPhaseDegreePhaseData);

    		});

    		if(!degreePhaseData.rPhaseDegreePhaseData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_phaseDegree(degreePhaseData.rPhaseDegreePhaseData);
    		}
    		if(!degreePhaseData.yPhaseDegreePhaseData.isEmpty()){
    			ProjectExecutionController.setFeedbackY_phaseDegree(degreePhaseData.yPhaseDegreePhaseData);
    		}
    		if(!degreePhaseData.bPhaseDegreePhaseData.isEmpty()){
    			ProjectExecutionController.setFeedbackB_phaseDegree(degreePhaseData.bPhaseDegreePhaseData);
    		}

    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdateDegreePhase :Exception:"+ e.getMessage());
    	}

	}
    
    
    
    public static void DeviceRefStdDataDisplayUpdateDegreePhase(final SerialDataRefStdKiggs degreePhaseData){

    	//ApplicationLauncher.logger.info("DeviceRefStdDataDisplayUpdateCurrent : Entry");
    	try{
    		Platform.runLater(() -> {
    			R_PhaseDegreePhase.setValue(degreePhaseData.rPhaseDegreePhaseData);
/*	    		Y_PhaseDegreePhase.setValue(degreePhaseData.yPhaseDegreePhaseData);
	    		B_PhaseDegreePhase.setValue(degreePhaseData.bPhaseDegreePhaseData);*/

    		});

    		if(!degreePhaseData.rPhaseDegreePhaseData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_phaseDegree(degreePhaseData.rPhaseDegreePhaseData);
    		}
/*    		if(!degreePhaseData.yPhaseDegreePhaseData.isEmpty()){
    			ProjectExecutionController.setFeedbackY_phaseDegree(degreePhaseData.yPhaseDegreePhaseData);
    		}
    		if(!degreePhaseData.bPhaseDegreePhaseData.isEmpty()){
    			ProjectExecutionController.setFeedbackB_phaseDegree(degreePhaseData.bPhaseDegreePhaseData);
    		}*/

    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdateDegreePhase :Exception:"+ e.getMessage());
    	}

	}
    
    public static void DeviceRefStdDataDisplayUpdateDegreePhase(final SerialDataRefStdKre degreePhaseData){

    	//ApplicationLauncher.logger.info("DeviceRefStdDataDisplayUpdateCurrent : Entry");
    	try{
    		Platform.runLater(() -> {
    			R_PhaseDegreePhase.setValue(degreePhaseData.rPhaseDegreePhaseData);
	    		Y_PhaseDegreePhase.setValue(degreePhaseData.yPhaseDegreePhaseData);
	    		B_PhaseDegreePhase.setValue(degreePhaseData.bPhaseDegreePhaseData);

    		});

    		if(!degreePhaseData.rPhaseDegreePhaseData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_phaseDegree(degreePhaseData.rPhaseDegreePhaseData);
    		}
    		if(!degreePhaseData.yPhaseDegreePhaseData.isEmpty()){
    			ProjectExecutionController.setFeedbackY_phaseDegree(degreePhaseData.yPhaseDegreePhaseData);
    		}
    		if(!degreePhaseData.bPhaseDegreePhaseData.isEmpty()){
    			ProjectExecutionController.setFeedbackB_phaseDegree(degreePhaseData.bPhaseDegreePhaseData);
    		}

    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdateDegreePhase :Exception:"+ e.getMessage());
    	}

	}
    
    public static void DeviceRefStdDataDisplayUpdateFreq(final SerialDataMteRefStd freqData){

    	//ApplicationLauncher.logger.info("DeviceRefStdDataDisplayUpdateFreq : Entry");
    	try{
    		Platform.runLater(() -> {
    			
    			R_PhaseFreq.setValue(freqData.rPhaseFreqDisplayData);
    			Y_PhaseFreq.setValue(freqData.yPhaseFreqDisplayData);
    			B_PhaseFreq.setValue(freqData.bPhaseFreqDisplayData);

    		});

    		if(!freqData.rPhaseFreqDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_Frequency(freqData.rPhaseFreqDisplayData);
    		}
    		if(!freqData.yPhaseFreqDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackY_Frequency(freqData.yPhaseFreqDisplayData);
    		}
    		if(!freqData.bPhaseFreqDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackB_Frequency(freqData.bPhaseFreqDisplayData);
    		}

    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdateFreq :Exception:"+ e.getMessage());
    	}

	}
    
    
    
    public static void DeviceRefStdDataDisplayUpdateFreq(final SerialDataRefStdKiggs freqData){

    	//ApplicationLauncher.logger.info("DeviceRefStdDataDisplayUpdateFreq : Entry");
    	try{
    		Platform.runLater(() -> {
    			
    			R_PhaseFreq.setValue(freqData.rPhaseFreqDisplayData);
/*    			Y_PhaseFreq.setValue(freqData.yPhaseFreqDisplayData);
    			B_PhaseFreq.setValue(freqData.bPhaseFreqDisplayData);*/

    		});

/*    		if(!freqData.rPhaseFreqDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_Frequency(freqData.rPhaseFreqDisplayData);
    		}
    		if(!freqData.yPhaseFreqDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackY_Frequency(freqData.yPhaseFreqDisplayData);
    		}
    		if(!freqData.bPhaseFreqDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackB_Frequency(freqData.bPhaseFreqDisplayData);
    		}*/

    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdateFreq :Exception:"+ e.getMessage());
    	}

	}
  
    
    public static void DeviceRefStdDataDisplayUpdateFreq(final SerialDataRefStdKre freqData){

    	//ApplicationLauncher.logger.info("DeviceRefStdDataDisplayUpdateFreq : Entry");
    	try{
    		Platform.runLater(() -> {
    			
    			R_PhaseFreq.setValue(freqData.rPhaseFreqDisplayData);
    			Y_PhaseFreq.setValue(freqData.yPhaseFreqDisplayData);
    			B_PhaseFreq.setValue(freqData.bPhaseFreqDisplayData);

    		});

    		if(!freqData.rPhaseFreqDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_Frequency(freqData.rPhaseFreqDisplayData);
    		}
    		if(!freqData.yPhaseFreqDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackY_Frequency(freqData.yPhaseFreqDisplayData);
    		}
    		if(!freqData.bPhaseFreqDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackB_Frequency(freqData.bPhaseFreqDisplayData);
    		}

    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdateFreq :Exception:"+ e.getMessage());
    	}

	}    
    public static void DeviceRefStdDataDisplayUpdatePowerFactor(final SerialDataMteRefStd pfData){

    	//ApplicationLauncher.logger.info("DeviceRefStdDataDisplayUpdateWatt : Entry");
    	try{
    		Platform.runLater(() -> {
    			R_PhasePowerFactor.setValue(pfData.rPhasePowerFactorData);
    			Y_PhasePowerFactor.setValue(pfData.yPhasePowerFactorData);
    			B_PhasePowerFactor.setValue(pfData.bPhasePowerFactorData);

    		});

/*    		if(!wattData.rPhaseWattDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_phaseWatt(wattData.rPhaseWattDisplayData);
    		}
    		if(!wattData.yPhaseWattDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackY_phaseWatt(wattData.yPhaseWattDisplayData);
    		}
    		if(!wattData.bPhaseWattDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackB_phaseWatt(wattData.bPhaseWattDisplayData);
    		}*/

    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdatePowerFactor :Exception:"+ e.getMessage());
    	}

	}
    
    
    
    public static void DeviceRefStdDataDisplayUpdatePowerFactor(final SerialDataRefStdKiggs pfData){

    	//ApplicationLauncher.logger.info("DeviceRefStdDataDisplayUpdateWatt : Entry");
    	try{
    		Platform.runLater(() -> {
    			R_PhasePowerFactor.setValue(pfData.rPhasePowerFactorData);
/*    			Y_PhasePowerFactor.setValue(pfData.yPhasePowerFactorData);
    			B_PhasePowerFactor.setValue(pfData.bPhasePowerFactorData);*/

    		});


    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdatePowerFactor :Exception:"+ e.getMessage());
    	}

	}
        
    public static void DeviceRefStdDataDisplayUpdatePowerFactor(final SerialDataRefStdKre pfData){

    	//ApplicationLauncher.logger.info("DeviceRefStdDataDisplayUpdateWatt : Entry");
    	try{
    		Platform.runLater(() -> {
    			R_PhasePowerFactor.setValue(pfData.rPhasePowerFactorData);
    			Y_PhasePowerFactor.setValue(pfData.yPhasePowerFactorData);
    			B_PhasePowerFactor.setValue(pfData.bPhasePowerFactorData);

    		});

/*    		if(!wattData.rPhaseWattDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_ActiveEnergy(wattData.rPhaseWattDisplayData);
    		}
    		if(!wattData.yPhaseWattDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackY_ActiveEnergy(wattData.yPhaseWattDisplayData);
    		}
    		if(!wattData.bPhaseWattDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackB_ActiveEnergy(wattData.bPhaseWattDisplayData);
    		}*/

    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdatePowerFactor :Exception:"+ e.getMessage());
    	}

	}
    
    public static void DeviceRefStdDataDisplayUpdateWatt(final SerialDataMteRefStd wattData){

    	//ApplicationLauncher.logger.info("DeviceRefStdDataDisplayUpdateWatt : Entry");
    	try{
    		Platform.runLater(() -> {
	    		R_PhaseWatt.setValue(wattData.rPhaseWattDisplayData);
	    		Y_PhaseWatt.setValue(wattData.yPhaseWattDisplayData);
	    		B_PhaseWatt.setValue(wattData.bPhaseWattDisplayData);

    		});

/*    		if(!wattData.rPhaseWattDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_phaseWatt(wattData.rPhaseWattDisplayData);
    		}
    		if(!wattData.yPhaseWattDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackY_phaseWatt(wattData.yPhaseWattDisplayData);
    		}
    		if(!wattData.bPhaseWattDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackB_phaseWatt(wattData.bPhaseWattDisplayData);
    		}*/

    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdateWatt :Exception:"+ e.getMessage());
    	}

	}
    
    
    
    public static void DeviceRefStdDataDisplayUpdateWatt(final SerialDataRefStdKiggs wattData){

    	//ApplicationLauncher.logger.info("DeviceRefStdDataDisplayUpdateWatt : Entry");
    	try{
    		Platform.runLater(() -> {
	    		R_PhaseWatt.setValue(wattData.rPhaseActivePowerDisplayData);
/*	    		Y_PhaseWatt.setValue(wattData.yPhaseWattDisplayData);
	    		B_PhaseWatt.setValue(wattData.bPhaseWattDisplayData);*/

    		});

/*    		if(!wattData.rPhaseWattDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_phaseWatt(wattData.rPhaseWattDisplayData);
    		}
    		if(!wattData.yPhaseWattDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackY_phaseWatt(wattData.yPhaseWattDisplayData);
    		}
    		if(!wattData.bPhaseWattDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackB_phaseWatt(wattData.bPhaseWattDisplayData);
    		}*/

    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdateWatt :Exception:"+ e.getMessage());
    	}

	}
    
    public static void DeviceRefStdDataDisplayUpdateWatt(final SerialDataRefStdKre wattData){

    	//ApplicationLauncher.logger.info("DeviceRefStdDataDisplayUpdateWatt : Entry");
    	try{
    		Platform.runLater(() -> {
	    		R_PhaseWatt.setValue(wattData.rPhaseActivePowerDisplayData);
	    		Y_PhaseWatt.setValue(wattData.yPhaseActivePowerDisplayData);
	    		B_PhaseWatt.setValue(wattData.bPhaseActivePowerDisplayData);

    		});
/*
    		if(!wattData.rPhaseActivePowerDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_ActiveEnergy(wattData.rPhaseActivePowerDisplayData);
    		}
    		if(!wattData.yPhaseActivePowerDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackY_ActiveEnergy(wattData.yPhaseActivePowerDisplayData);
    		}
    		if(!wattData.bPhaseActivePowerDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackB_ActiveEnergy(wattData.bPhaseActivePowerDisplayData);
    		}*/

    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdateWatt :Exception:"+ e.getMessage());
    	}

	} 
    

    
    public static void DeviceRefStdDataDisplayUpdateReactivePower(final SerialDataRefStdKre reactiveData){

    	//ApplicationLauncher.logger.info("DeviceRefStdDataDisplayUpdateWatt : Entry");
    	try{
    		Platform.runLater(() -> {
    			R_PhaseVAR.setValue(reactiveData.rPhaseReactivePowerDisplayData);
    			Y_PhaseVAR.setValue(reactiveData.yPhaseReactivePowerDisplayData);
    			B_PhaseVAR.setValue(reactiveData.bPhaseReactivePowerDisplayData);

    		});
/*
    		if(!reactiveData.rPhaseReactivePowerDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_ReactiveEnergy(reactiveData.rPhaseReactivePowerDisplayData);
    		}
    		if(!reactiveData.yPhaseReactivePowerDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackY_ReactiveEnergy(reactiveData.yPhaseReactivePowerDisplayData);
    		}
    		if(!reactiveData.bPhaseReactivePowerDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackB_ReactiveEnergy(reactiveData.bPhaseReactivePowerDisplayData);
    		}*/

    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdateReactivePower :Exception:"+ e.getMessage());
    	}

	} 
    
    
    
    
    public static void DeviceRefStdDataDisplayUpdateReactivePower(final SerialDataRefStdKiggs reactiveData){

    	//ApplicationLauncher.logger.info("DeviceRefStdDataDisplayUpdateWatt : Entry");
    	try{
    		Platform.runLater(() -> {
    			R_PhaseVAR.setValue(reactiveData.rPhaseReactivePowerDisplayData);
/*    			Y_PhaseVAR.setValue(reactiveData.yPhaseReactivePowerDisplayData);
    			B_PhaseVAR.setValue(reactiveData.bPhaseReactivePowerDisplayData);*/

    		});
/*
    		if(!reactiveData.rPhaseReactivePowerDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_ReactiveEnergy(reactiveData.rPhaseReactivePowerDisplayData);
    		}
    		if(!reactiveData.yPhaseReactivePowerDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackY_ReactiveEnergy(reactiveData.yPhaseReactivePowerDisplayData);
    		}
    		if(!reactiveData.bPhaseReactivePowerDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackB_ReactiveEnergy(reactiveData.bPhaseReactivePowerDisplayData);
    		}*/

    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdateReactivePower :Exception:"+ e.getMessage());
    	}

	} 
    
    public static void DeviceRefStdDataDisplayUpdateApparentPower(final SerialDataRefStdKre apparentData){

    	//ApplicationLauncher.logger.info("DeviceRefStdDataDisplayUpdateWatt : Entry");
    	try{
    		Platform.runLater(() -> {
    			R_PhaseVA.setValue(apparentData.rPhaseApparentPowerDisplayData);
    			Y_PhaseVA.setValue(apparentData.yPhaseApparentPowerDisplayData);
    			B_PhaseVA.setValue(apparentData.bPhaseApparentPowerDisplayData);

    		});

/*    		if(!apparentData.rPhaseApparentPowerDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_ApparentEnergy(apparentData.rPhaseApparentPowerDisplayData);
    		}
    		if(!apparentData.yPhaseApparentPowerDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackY_ApparentEnergy(apparentData.yPhaseApparentPowerDisplayData);
    		}
    		if(!apparentData.bPhaseApparentPowerDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackB_ApparentEnergy(apparentData.bPhaseApparentPowerDisplayData);
    		}
*/
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdateApparentPower :Exception:"+ e.getMessage());
    	}

	} 
    
    
    
    public static void DeviceRefStdDataDisplayUpdateApparentPower(final SerialDataRefStdKiggs apparentData){

    	//ApplicationLauncher.logger.info("DeviceRefStdDataDisplayUpdateWatt : Entry");
    	try{
    		Platform.runLater(() -> {
    			R_PhaseVA.setValue(apparentData.rPhaseApparentPowerDisplayData);
/*    			Y_PhaseVA.setValue(apparentData.yPhaseApparentPowerDisplayData);
    			B_PhaseVA.setValue(apparentData.bPhaseApparentPowerDisplayData);*/

    		});

/*    		if(!apparentData.rPhaseApparentPowerDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_ApparentEnergy(apparentData.rPhaseApparentPowerDisplayData);
    		}
    		if(!apparentData.yPhaseApparentPowerDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackY_ApparentEnergy(apparentData.yPhaseApparentPowerDisplayData);
    		}
    		if(!apparentData.bPhaseApparentPowerDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackB_ApparentEnergy(apparentData.bPhaseApparentPowerDisplayData);
    		}
*/
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdateApparentPower :Exception:"+ e.getMessage());
    	}

	} 
    
    public static void DeviceRefStdDataDisplayUpdateActiveEnergy(final SerialDataRefStdKre wattData){

    	//ApplicationLauncher.logger.info("DeviceRefStdDataDisplayUpdateWatt : Entry");
    	try{
    		Platform.runLater(() -> {
	    		R_PhaseWh.setValue(wattData.rPhaseActiveEnergyAccumulatedDisplayData);
	    		Y_PhaseWh.setValue(wattData.yPhaseActiveEnergyAccumulatedDisplayData);
	    		B_PhaseWh.setValue(wattData.bPhaseActiveEnergyAccumulatedDisplayData);

    		});

    		if(!wattData.rPhaseActiveEnergyAccumulatedDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_ActiveEnergy(wattData.rPhaseActiveEnergyAccumulatedDisplayData);
    		}
    		if(!wattData.yPhaseActiveEnergyAccumulatedDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackY_ActiveEnergy(wattData.yPhaseActiveEnergyAccumulatedDisplayData);
    		}
    		if(!wattData.bPhaseActiveEnergyAccumulatedDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackB_ActiveEnergy(wattData.bPhaseActiveEnergyAccumulatedDisplayData);
    		}

    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdateWatt :Exception:"+ e.getMessage());
    	}

	} 
    
    
    
    
    
    public static void DeviceRefStdDataDisplayUpdateActiveEnergy(final SerialDataRefStdKiggs wattData){

    	//ApplicationLauncher.logger.info("DeviceRefStdDataDisplayUpdateWatt : Entry");
    	try{
    		Platform.runLater(() -> {
	    		R_PhaseWh.setValue(wattData.rPhaseActiveEnergyAccumulatedDisplayData);
	    		//Y_PhaseWh.setValue(wattData.yPhaseActiveEnergyAccumulatedDisplayData);
	    		//B_PhaseWh.setValue(wattData.bPhaseActiveEnergyAccumulatedDisplayData);

    		});

    		if(!wattData.rPhaseActiveEnergyAccumulatedDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_ActiveEnergy(wattData.rPhaseActiveEnergyAccumulatedDisplayData);
    		}
/*    		if(!wattData.yPhaseActiveEnergyAccumulatedDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackY_ActiveEnergy(wattData.yPhaseActiveEnergyAccumulatedDisplayData);
    		}
    		if(!wattData.bPhaseActiveEnergyAccumulatedDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackB_ActiveEnergy(wattData.bPhaseActiveEnergyAccumulatedDisplayData);
    		}*/

    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdateWatt :Exception:"+ e.getMessage());
    	}

	} 
    public static void DeviceRefStdDataDisplayUpdateApparentEnergy(final SerialDataRefStdKre wattData){

    	//ApplicationLauncher.logger.info("DeviceRefStdDataDisplayUpdateWatt : Entry");
    	try{
/*    		Platform.runLater(() -> {
	    		R_PhaseWatt.setValue(wattData.rPhaseActivePowerDisplayData);
	    		Y_PhaseWatt.setValue(wattData.yPhaseActivePowerDisplayData);
	    		B_PhaseWatt.setValue(wattData.bPhaseActivePowerDisplayData);

    		});*/

    		if(!wattData.rPhaseApparentEnergyAccumulatedDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_ApparentEnergy(wattData.rPhaseApparentEnergyAccumulatedDisplayData);
    		}
    		if(!wattData.yPhaseApparentEnergyAccumulatedDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackY_ApparentEnergy(wattData.yPhaseApparentEnergyAccumulatedDisplayData);
    		}
    		if(!wattData.bPhaseApparentEnergyAccumulatedDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackB_ApparentEnergy(wattData.bPhaseApparentEnergyAccumulatedDisplayData);
    		}

    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdateWatt :Exception:"+ e.getMessage());
    	}

	} 
    public static void DeviceRefStdDataDisplayUpdateReactiveEnergy(final SerialDataRefStdKre wattData){

    	//ApplicationLauncher.logger.info("DeviceRefStdDataDisplayUpdateWatt : Entry");
    	try{
    		Platform.runLater(() -> {
	    		R_PhaseWh.setValue(wattData.rPhaseReactiveEnergyAccumulatedDisplayData);
	    		Y_PhaseWh.setValue(wattData.yPhaseReactiveEnergyAccumulatedDisplayData);
	    		B_PhaseWh.setValue(wattData.bPhaseReactiveEnergyAccumulatedDisplayData);

    		});

    		if(!wattData.rPhaseReactiveEnergyAccumulatedDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_ReactiveEnergy(wattData.rPhaseReactiveEnergyAccumulatedDisplayData);
    		}
    		if(!wattData.yPhaseReactiveEnergyAccumulatedDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackY_ReactiveEnergy(wattData.yPhaseReactiveEnergyAccumulatedDisplayData);
    		}
    		if(!wattData.bPhaseReactiveEnergyAccumulatedDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackB_ReactiveEnergy(wattData.bPhaseReactiveEnergyAccumulatedDisplayData);
    		}

    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdateWatt :Exception:"+ e.getMessage());
    	}

	} 
    
    
    
    
    public static void DeviceRefStdDataDisplayUpdateReactiveEnergy(final SerialDataRefStdKiggs wattData){

    	//ApplicationLauncher.logger.info("DeviceRefStdDataDisplayUpdateWatt : Entry");
    	try{
    		Platform.runLater(() -> {
	    		R_PhaseWh.setValue(wattData.rPhaseReactiveEnergyAccumulatedDisplayData);
	    		//Y_PhaseWh.setValue(wattData.yPhaseReactiveEnergyAccumulatedDisplayData);
	    		//B_PhaseWh.setValue(wattData.bPhaseReactiveEnergyAccumulatedDisplayData);

    		});

    		if(!wattData.rPhaseReactiveEnergyAccumulatedDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_ReactiveEnergy(wattData.rPhaseReactiveEnergyAccumulatedDisplayData);
    		}
/*    		if(!wattData.yPhaseReactiveEnergyAccumulatedDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackY_ReactiveEnergy(wattData.yPhaseReactiveEnergyAccumulatedDisplayData);
    		}
    		if(!wattData.bPhaseReactiveEnergyAccumulatedDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackB_ReactiveEnergy(wattData.bPhaseReactiveEnergyAccumulatedDisplayData);
    		}*/

    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdateWatt :Exception:"+ e.getMessage());
    	}

	}
    
    public static void DeviceRefStdDataDisplayUpdate_R_Phase(final SerialDataSandsRefStd R_PhaseData){

    	ApplicationLauncher.logger.info("DeviceRefStdDataDisplayUpdate_R_Phase : Entry");
    	try{
    		Platform.runLater(() -> {
	    		R_PhaseVolt.setValue(R_PhaseData.VoltageDisplayData);
	    		R_PhaseCurrent.setValue(R_PhaseData.CurrentDisplayData);
	    		R_PhasePowerFactor.setValue(R_PhaseData.PowerFactorData);
	    		R_PhaseFreq.setValue(R_PhaseData.FreqDisplayData);
	    		R_PhaseWatt.setValue(R_PhaseData.WattDisplayData);
	    		R_PhaseVA.setValue(R_PhaseData.VA_DisplayData);
	    		R_PhaseVAR.setValue(R_PhaseData.VAR_DisplayData);
	    		//R_PhaseDegreePhase.setValue(R_PhaseData.DegreePhaseData);
    		});

    		if(!R_PhaseData.VoltageDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_phaseVolt(R_PhaseData.VoltageDisplayData);
    		}
    		if(!R_PhaseData.CurrentDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_phaseCurrent(R_PhaseData.CurrentDisplayData);
    		}
/*    		if(!R_PhaseData.DegreePhaseData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_phaseDegree(R_PhaseData.DegreePhaseData);
    		}*/
    		if(!R_PhaseData.FreqDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_Frequency(R_PhaseData.FreqDisplayData);
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdate_R_Phase :Exception:"+ e.getMessage());
    	}

	}
    
    public static void DeviceRefStdDataDisplayUpdate_Y_Phase(final SerialDataSandsRefStd Y_PhaseData){


    	ApplicationLauncher.logger.info("DeviceRefStdDataDisplayUpdate_Y_Phase : Entry");
    	try{
    		Platform.runLater(() -> {
	    		Y_PhaseVolt.setValue(Y_PhaseData.VoltageDisplayData);
	    		Y_PhaseCurrent.setValue(Y_PhaseData.CurrentDisplayData);
	    		Y_PhasePowerFactor.setValue(Y_PhaseData.PowerFactorData);
	    		Y_PhaseFreq.setValue(Y_PhaseData.FreqDisplayData);
	    		Y_PhaseWatt.setValue(Y_PhaseData.WattDisplayData);
	    		Y_PhaseVA.setValue(Y_PhaseData.VA_DisplayData);
	    		Y_PhaseVAR.setValue(Y_PhaseData.VAR_DisplayData);
	    		//Y_PhaseDegreePhase.setValue(Y_PhaseData.DegreePhaseData);
    		});
    		

    		if(!Y_PhaseData.VoltageDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackY_phaseVolt(Y_PhaseData.VoltageDisplayData);
    		}
    		if(!Y_PhaseData.CurrentDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackY_phaseCurrent(Y_PhaseData.CurrentDisplayData);
    		}
/*    		if(!Y_PhaseData.DegreePhaseData.isEmpty()){
    			ProjectExecutionController.setFeedbackY_phaseDegree(Y_PhaseData.DegreePhaseData);
    		}*/
    		if(!Y_PhaseData.FreqDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackY_Frequency(Y_PhaseData.FreqDisplayData);
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdate_Y_Phase :Exception:"+ e.getMessage());
    	}


	}
	
	public static void DeviceRefStdDataDisplayUpdate_B_Phase(final SerialDataSandsRefStd B_PhaseData){

		
		ApplicationLauncher.logger.info("DeviceRefStdDataDisplayUpdate_B_Phase : Entry");
		try{
			Platform.runLater(() -> {
	    		B_PhaseVolt.setValue(B_PhaseData.VoltageDisplayData);
	    		B_PhaseCurrent.setValue(B_PhaseData.CurrentDisplayData);
	    		B_PhasePowerFactor.setValue(B_PhaseData.PowerFactorData);
	    		B_PhaseFreq.setValue(B_PhaseData.FreqDisplayData);
	    		B_PhaseWatt.setValue(B_PhaseData.WattDisplayData);
	    		B_PhaseVA.setValue(B_PhaseData.VA_DisplayData);
	    		B_PhaseVAR.setValue(B_PhaseData.VAR_DisplayData);
	    		//B_PhaseDegreePhase.setValue(B_PhaseData.DegreePhaseData);
			});

			
			if(!B_PhaseData.VoltageDisplayData.isEmpty()){
				ProjectExecutionController.setFeedbackB_phaseVolt(B_PhaseData.VoltageDisplayData);
			}
			if(!B_PhaseData.CurrentDisplayData.isEmpty()){
				ProjectExecutionController.setFeedbackB_phaseCurrent(B_PhaseData.CurrentDisplayData);
			}
/*			if(!B_PhaseData.DegreePhaseData.isEmpty()){
				ProjectExecutionController.setFeedbackB_phaseDegree(B_PhaseData.DegreePhaseData);
			}*/
    		if(!B_PhaseData.FreqDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackB_Frequency(B_PhaseData.FreqDisplayData);
    		}
		}catch(Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdate_B_Phase :Exception:"+ e.getMessage());
		}

	}
	
	
	public static void DeviceRefStdDataDisplayUpdateDegreePhaseAngle(final SerialDataSandsRefStd degreePhaseAngleData){

		
		ApplicationLauncher.logger.info("DeviceRefStdDataDisplayUpdateDegreePhaseAngle : Entry");
		try{
			Platform.runLater(() -> {
				R_PhaseDegreePhase.setValue(degreePhaseAngleData.rPhaseDegreePhaseData);
				Y_PhaseDegreePhase.setValue(degreePhaseAngleData.yPhaseDegreePhaseData);
	    		B_PhaseDegreePhase.setValue(degreePhaseAngleData.bPhaseDegreePhaseData);
			});

			
			if(!degreePhaseAngleData.rPhaseDegreePhaseData.isEmpty()){
				ProjectExecutionController.setFeedbackR_phaseDegree(degreePhaseAngleData.rPhaseDegreePhaseData);
			}
			if(!degreePhaseAngleData.yPhaseDegreePhaseData.isEmpty()){
				ProjectExecutionController.setFeedbackY_phaseDegree(degreePhaseAngleData.yPhaseDegreePhaseData);
			}
			if(!degreePhaseAngleData.bPhaseDegreePhaseData.isEmpty()){
				ProjectExecutionController.setFeedbackB_phaseDegree(degreePhaseAngleData.bPhaseDegreePhaseData);
			}

		}catch(Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdateDegreePhaseAngle :Exception:"+ e.getMessage());
		}

	}
	
	public static void DeviceRefStdDataDisplayUpdateR_PhaseDegreePhaseAngle(final SerialDataSandsRefStd degreePhaseAngleData){

		
		ApplicationLauncher.logger.info("DeviceRefStdDataDisplayUpdateR_PhaseDegreePhaseAngle : Entry");
		try{
			Platform.runLater(() -> {
				R_PhaseDegreePhase.setValue(degreePhaseAngleData.rPhaseDegreePhaseData);
			});

			
			if(!degreePhaseAngleData.rPhaseDegreePhaseData.isEmpty()){
				ProjectExecutionController.setFeedbackR_phaseDegree(degreePhaseAngleData.rPhaseDegreePhaseData);
			}


		}catch(Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdateR_PhaseDegreePhaseAngle :Exception:"+ e.getMessage());
		}

	}

}
