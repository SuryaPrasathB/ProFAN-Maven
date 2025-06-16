package com.tasnetwork.calibration.energymeter.messenger;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdKre;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.deployment.DisplayInstantMetricsController;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
import com.tasnetwork.calibration.energymeter.device.Data_RefStdKre;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.device.SerialDataRefStdKre;
import com.tasnetwork.calibration.energymeter.message.RefStdKreMessage;
import com.tasnetwork.calibration.energymeter.serial.portmanagerV2.SerialPortManagerRefStd_V2;
import com.tasnetwork.calibration.energymeter.testprofiles.TestProfileType;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;

public class RefStdKreMessenger {
	
	
	DeviceDataManagerController displayDataObj =  new DeviceDataManagerController();
	SerialPortManagerRefStd_V2 refStdSpmObj =  new DeviceDataManagerController().getSerialPortManagerRefStd_V2();
	

	public boolean kreRefStdWriteSettingTask(){
		ApplicationLauncher.logger.debug("kreRefStdWriteSettingTask :Entry");
		boolean status = false;
		String command = Data_RefStdKre.getCommandWriteSettingParameter();//MSG_READ_SETTING;
		//String command = GuiUtils.StringToHex("KRE://FREQ=88.000000,U1=0.010334115,U12=0.021162375,I1=2.74444e-005,P1=-2.1349343e-008,Q1=-3.8901045e-008,S1=1.9612846e-007,COS1=-0.10885387,a1=157.79993,a12=282.98401,al1=142.58884,al12=282.98401,EP1=0,EQ1=0,ES1=0,U2=0.018832797,U23=0.01532512,I2=2.3657419e-005,P2=-1.0671969e-008,Q2=-2.0242668e-008,S2=9.7404297e-008,COS2=-0.10956363,a2=315.03586,a23=96.412472,al2=48.946889,al23=96.412472,EP2=0,EQ2=0,ES2=0,U3=0.0094679299,U31=0.014200959,I3=2.7924714e-005,P3=1.3306727e-008,Q3=-4.7939969e-010,S3=1.1341508e-007,COS3=0.11732767,a3=123.95469,a31=340.60352,al3=103.03076,al31=340.60352,EP3=0,EQ3=0,ES3=0,PSUM=-1.8714584e-008,QSUM=-5.9623113e-008,SSUM=4.0694784e-007,SCOS=-0.29947547,TU1=7.7383331,TI1=1.7727179,TU2=54.145088,TI2=4.4789028,TU3=10.899458,TI3=1.9686953,NF=1,CRC=A0,/!");//MSG_READ_SETTING;
		String expectedAckResponse = ConstantRefStdKre.ER_READ_SETTING_ACK;

		int timeDelayInMilliSec = 0;
		String expectedData = GuiUtils.hexToAscii(ConstantRefStdKre.ER_READ_SETTING_ACK);//ConstantRefStdKre.ER_TERMINATOR;//
		
		String rxMessageTerminatorInHex = GuiUtils.hexToAscii(ConstantRefStdKre.ER_TERMINATOR);//
		boolean isResponseExpected = true;
		Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn =getRefStdSpmObj().refStdSendCommandProcess(command, timeDelayInMilliSec, expectedData, 
				isResponseExpected, rxMessageTerminatorInHex);
				//reRefStdCommSendWriteDataCommand(command,expectedAckResponse);
		
		status = (boolean)responseReturn.get("status");
		ApplicationLauncher.logger.debug("kreRefStdWriteSettingTask :status: " + status);
		if(status){
			String responseData = (String)responseReturn.get("result");
			ApplicationLauncher.logger.debug("kreRefStdWriteSettingTask : responseData : " + responseData);

		}


		return status;

	}
	
	public void kreReadRefStdAllData() {

		ApplicationLauncher.logger.debug("kreReadRefStdAllData V2: Entry:");
		//String metertype = DisplayDataObj.getDeployedEM_ModelType();
		if(displayDataObj.getRefStdReadDataFlag() && !ProjectExecutionController.getUserAbortedFlag()){
			//ClearStdRefSerialData();
			//SerialDataRefStdKre.ClearRefStd_ReadSerialData();
			//RefStdCommSendCommand(ConstantRadiantRefStd.CMD_REF_STD_READ_R_PHASE,ConstantRadiantRefStd.CMD_REF_STD_READ_PHASE_DATA_EXPECTED_LENGTH);
			
			String command = RefStdKreMessage.MSG_READ_BASIC_DATA;
			String expectedData = GuiUtils.hexToAscii(ConstantRefStdKre.ER_READ_BASIC_DATA_ACK);
			boolean isResponseExpected = true;
			int timeDelayInMilliSec = 0;
			String rxMessageTerminatorInHex = GuiUtils.hexToAscii(ConstantRefStdKre.ER_TERMINATOR);
			Map<String,Object> responseReturn = new HashMap<String,Object>();
			responseReturn =getRefStdSpmObj().refStdSendCommandProcess(command, timeDelayInMilliSec, expectedData, 
					isResponseExpected, rxMessageTerminatorInHex);
			boolean status = (boolean)responseReturn.get("status");
			if(status){
				String responseData = (String)responseReturn.get("result");
				//ApplicationLauncher.logger.debug("kreReadRefStdAllData V2 : responseData : " + responseData);	
				
				SerialDataRefStdKre refStdData = new SerialDataRefStdKre();
				refStdData.parseBasicsSerialData(GuiUtils.StringToHex(responseData));
				
				if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateCurrent(refStdData);
				}
				//ProjectExecutionController.refStdDataDisplayUpdateCurrentData(refStdData);
				ApplicationLauncher.logger.debug("Current data update:"+refStdData.rPhaseCurrentDisplayData);

				if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateVoltage(refStdData);
				}
				ProjectExecutionController.refStdDataDisplayUpdateVoltageData(refStdData);
				ApplicationLauncher.logger.debug("Volt data update:"+refStdData.rPhaseVoltageDisplayData);

				if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateDegreePhase(refStdData);
				}
				ProjectExecutionController.refStdDataDisplayUpdateDegreePhaseData(refStdData);
				ApplicationLauncher.logger.debug("DegreePhase data update:"+refStdData.rPhaseDegreePhaseData);

				if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateFreq(refStdData);
				}
				ProjectExecutionController.refStdDataDisplayUpdateFreqData(refStdData);
				ApplicationLauncher.logger.debug("freq data update:"+refStdData.rPhaseFreqDisplayData);

				if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdatePowerFactor(refStdData);
				}
				ProjectExecutionController.refStdDataDisplayUpdatePowerFactorData(refStdData);
				ApplicationLauncher.logger.debug("PowerFactor data update:"+refStdData.rPhasePowerFactorData);

				if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateWatt(refStdData);
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateReactivePower(refStdData);
					DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateApparentPower(refStdData);
				}
				if(ProjectExecutionController.getCurrentTestType().equals(TestProfileType.ConstantTest.toString())){
					if(displayDataObj.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)){
						DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateActiveEnergy(refStdData);
					}else if(displayDataObj.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)){
						DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateReactiveEnergy(refStdData);
					}
				}
				ProjectExecutionController.refStdDataDisplayUpdateWattData(refStdData);
				ApplicationLauncher.logger.debug("ActivePower data update:"+refStdData.rPhaseActivePowerDisplayData);
				if(ProcalFeatureEnable.METRICS_EXCEL_LOG_ENABLE_FEATURE){
									
					displayDataObj.updateMetricLogFile();
									
				}

			}
			


			if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
				ProjectExecutionController.UpdateRefStdProgressBar();
			}


		}
		ApplicationLauncher.logger.debug("kreReadRefStdAllData V2: Exit");
	}
	
	public boolean kreRefStdAccumulationStartTask(){
		ApplicationLauncher.logger.debug("kreRefStdAccumulationStartTask V2:Entry");
		String command = RefStdKreMessage.MSG_ENERGY_ACCU_START;//MSG_READ_SETTING;
		String expectedData = GuiUtils.hexToAscii(ConstantRefStdKre.ER_ENERGY_ACC_START_ACK);;//"";//GuiUtils.hexToAscii(ConstantRefStdKre.ER_ENERGY_ACC_START_ACK);
		int timeDelayInMilliSec = 0;
		boolean isResponseExpected = true;
		String rxMessageTerminatorInHex = GuiUtils.hexToAscii(ConstantRefStdKre.ER_TERMINATOR);
		Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn =getRefStdSpmObj().refStdSendCommandProcess(command, timeDelayInMilliSec, expectedData, 
				isResponseExpected, rxMessageTerminatorInHex);
		boolean status = (boolean)responseReturn.get("status");
		if(status){

			ApplicationLauncher.logger.debug("kreRefStdAccumulationStartTask V2:Success" );

		}


		return status;

	}
	
	public SerialPortManagerRefStd_V2 getRefStdSpmObj() {
		return refStdSpmObj;
	}


	public void setRefStdSpmObj(SerialPortManagerRefStd_V2 refStdSpmObj) {
		this.refStdSpmObj = refStdSpmObj;
	}

}
