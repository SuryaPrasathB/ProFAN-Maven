package com.tasnetwork.calibration.energymeter.device;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantPowerSourceBofa;
import com.tasnetwork.calibration.energymeter.constant.DeleteMeConstant;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.deployment.BofaManager;
import com.tasnetwork.calibration.energymeter.messenger.BofaRequestProcessor;
import com.tasnetwork.calibration.energymeter.serial.portmanager.SerialPortManagerPwrSrc;
import com.tasnetwork.calibration.energymeter.util.ErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;

import javafx.scene.control.Alert.AlertType;

public class Data_PowerSourceBofa {


	public static SerialPortManagerPwrSrc serialDM_Obj = new SerialPortManagerPwrSrc();

	static String address                             = "";     // Address of the device
	static String command                             = "";     // Command for parameter setting
	static String meterType                           = "";     // Example: Meter type (you can change this)
	static String basicVoltage                        = "";     // Example: Basic voltage (xxx.xV)
	static String basicCurrent                        = "";     // Example: Basic current (xx.xxA)
	static String outputFrequency                     = "";     // Example: Output frequency (xx.xxHz)
	static String outputPhaseSequence                 = "";     // Example: Output phase sequence (30H-positive phase sequence)
	static String currentDirection                    = "";     // Example: Current direction (30H-forward)
	static String uaUbAngle                           = "";     // Example: UaUb angle (xxx.x)
	static String uaUcAngle                           = "";     // UaUc angle (xxx.x)
	static String uaIaAngle                           = "";     // UaIa angle (xxx.x)
	static String ubIbAngle                           = "";     // UbIb angle (xxx.x)
	static String ucIcAngle                           = "";     // UcIc angle (xxx.x)
	static String waveForm                            = "";     // Waveform (Example: 30H-sine wave)
	static String firstHarmonic                       = "";     // 1st harmonic (xx)
	static String secondHarmonic                      = "";     // 2nd harmonic (xx)
	static String thirdHarmonic                       = "";     // 3rd harmonic (xx)
	static String firstHarmonicPercentageVoltage      = "";     // 1st harmonic percentage (voltage) (xx)
	static String secondHarmonicPercentageVoltage     = "";     // 2nd harmonic percentage (voltage) (xx)
	static String thirdHarmonicPercentageVoltage      = "";     // 3rdharmonic percentage (voltage) (xx)
	static String firstHarmonicPercentageCurrent      = "";     // 1st harmonic percentage (current) (xx)
	static String secondHarmonicPercentageCurrent     = "";     // 2nd harmonic percentage (current) (xx)
	static String thirdHarmonicPercentageCurrent      = "";     // 3rd harmonic percentage (current) (xx)
	static String firstHarmonicPercentagePhaseAngle   = "";     // 1st harmonic percentage (phase angle) (xxx.x)
	static String secondHarmonicPercentagePhaseAngle  = "";     // 2nd harmonic percentage (phase angle) (xxx.x)
	static String thirdHarmonicPercentagePhaseAngle   = "";     // 3rd harmonic percentage (phase angle) (xxx.x)
	static String phaseAVoltagePercentage             = "";     // Phase A voltage percentage (xxx.xx%)
	static String phaseBVoltagePercentage             = "";     // Phase B voltage percentage (xxx.xx%)
	static String phaseCVoltagePercentage 			  = "";     // Phase C voltage percentage (xxx.xx%)
	static String phaseACurrentPercentage             = "";     // Phase A current percentage (xxx.xx%)
	static String phaseBCurrentPercentage             = "";     // Phase B current percentage (xxx.xx%)
	static String phaseCCurrentPercentage             = "";     // Phase C current percentage (xxx.xx%)
	static String meterConstant                       = "";     // Meter constant (xxxxx.xx)
	static String numOfPulse                          = "";     // Number of pulse (xx)
	static String conjunctionSplitPhase               = "";     // Conjunction phase/split phase (x)		

	static String phaseA_Voltage = ""; //	UA (xxx.xxV) 
	static String phaseB_Voltage = ""; //	UB (xxx.xxV) 
	static String phaseC_Voltage = ""; //	UC (xxx.xxV) 
	static String phaseA_Current = ""; //	IA (xxx.xxxxA) 
	static String phaseB_Current = ""; //	IB (xxx.xxxxA) 
	static String phaseC_Current = ""; //	IC (xxx.xxxxA) 

	// Conjunction phase/split phase (x)	 
	static String phaseA_ActivePower        = ""; 	// active A (�xxxxx.xx W) 
	static String phaseB_ActivePower        = "";   // active B (�xxxxx.xx W) +
	static String phaseC_ActivePower        = "";   // active C (�xxxxx.xx W) +
	static String phaseA_ReactivePower      = "";	// reactive A (�xxxxx.xx Var) +
	static String phaseB_ReactivePower      = "";	// reactive B(�xxxxx.xx Var) +
	static String phaseC_ReactivePower      = "";	// reactive C(�xxxxx.xx Var)+
	static String totalActivePower          = "";	// total active (�xxxxx.xx W) +
	static String totalReactivePower        = "";	// total reactive(�xxxxx.xx Var) +
	static String apparentPower             = "";	// VA(�xxxxx.xx VA)+

	static String parameterSettingCmdFrame         = "" ;
	static String voltageCurrentOutputCmdFrame     = "" ;
	static String voltageCurrentStopOutputCmdFrame = "" ;
	static String currentStoppingCmdFrame          = "" ;


	//==============================================================================================================  


	public static void noHarmonicsDefaultSettings(){
		setFirstHarmonic(BofaManager.asciiToHex("00"));
		setFirstHarmonicPercentageVoltage(BofaManager.asciiToHex("00"));
		setFirstHarmonicPercentagePhaseAngle(BofaManager.asciiToHex("0000"));

		setSecondHarmonic(BofaManager.asciiToHex("00"));
		setSecondHarmonicPercentageVoltage(BofaManager.asciiToHex("00"));
		setSecondHarmonicPercentagePhaseAngle(BofaManager.asciiToHex("0000"));

		setThirdHarmonic(BofaManager.asciiToHex("00"));
		setThirdHarmonicPercentageVoltage(BofaManager.asciiToHex("00"));
		setThirdHarmonicPercentagePhaseAngle(BofaManager.asciiToHex("0000"));

		//Data_PowerSourceBofa.setFirstHarmonic(BofaManager.asciiToHex("00"));
		setFirstHarmonicPercentageCurrent(BofaManager.asciiToHex("00"));
		//Data_PowerSourceBofa.setFirstHarmonicPercentagePhaseAngle(BofaManager.asciiToHex("0000"));

		//Data_PowerSourceBofa.setSecondHarmonic(BofaManager.asciiToHex("00"));
		setSecondHarmonicPercentageCurrent(BofaManager.asciiToHex("00"));
		//Data_PowerSourceBofa.setSecondHarmonicPercentagePhaseAngle(BofaManager.asciiToHex("0000"));

		//Data_PowerSourceBofa.setThirdHarmonic(BofaManager.asciiToHex("00"));
		setThirdHarmonicPercentageCurrent(BofaManager.asciiToHex("00"));

	}


	public static void frameParameterSettingCommand(){	
		ApplicationLauncher.logger.info("frameParameterSettingCommand:Entry");
		// Frame the message
		StringBuilder messageBuilder = new StringBuilder();

		messageBuilder.append(ConstantPowerSourceBofa.START_BYTE);             // Start bit
		messageBuilder.append(ConstantPowerSourceBofa.ADDRESS_BYTE);           // Address
		messageBuilder.append(ConstantPowerSourceBofa.PARAMETER_SETTING_CMD);  // Command
		messageBuilder.append(getMeterType());                                // Meter type
		messageBuilder.append(getBasicVoltage());                              // Basic voltage
		messageBuilder.append(getBasicCurrent());                              // Basic current
		messageBuilder.append(getOutputFrequency());                           // Output frequency
		messageBuilder.append(getOutputPhaseSequence());                       // Output phase sequence
		messageBuilder.append(getCurrentDirection());                          // Current direction
		messageBuilder.append(getUaUbAngle());                                 // UaUb angle
		messageBuilder.append(getUaUcAngle());                                 // UaUc angle
		messageBuilder.append(getUaIaAngle());                                 // UaIa angle
		messageBuilder.append(getUbIbAngle());                                 // UbIb angle
		messageBuilder.append(getUcIcAngle());                                 // UcIc angle
		messageBuilder.append(getWaveForm());                                  // Wave form
		messageBuilder.append(getFirstHarmonic());                             // 1st harmonic
		messageBuilder.append(getSecondHarmonic());                            // 2nd harmonic
		messageBuilder.append(getThirdHarmonic());                             // 3rd harmonic
		messageBuilder.append(getFirstHarmonicPercentageVoltage());            // 1st harmonic percentage (voltage)
		messageBuilder.append(getSecondHarmonicPercentageVoltage());           // 2nd harmonic percentage (voltage)
		messageBuilder.append(getThirdHarmonicPercentageVoltage());            // 3rdharmonic percentage (voltage)
		messageBuilder.append(getFirstHarmonicPercentageCurrent());            // 1st harmonic percentage (current)
		messageBuilder.append(getSecondHarmonicPercentageCurrent());           // 2nd harmonic percentage (current)
		messageBuilder.append(getThirdHarmonicPercentageCurrent() );           // 3rd harmonic percentage (current)
		messageBuilder.append(getFirstHarmonicPercentagePhaseAngle());         // 1st harmonic percentage (phase angle)
		messageBuilder.append(getSecondHarmonicPercentagePhaseAngle());        // 2nd harmonic percentage (phase angle)
		messageBuilder.append(getThirdHarmonicPercentagePhaseAngle());         // 3rd harmonic percentage (phase angle)
		messageBuilder.append(getPhaseAVoltagePercentage());                   // Phase A voltage percentage
		messageBuilder.append(getPhaseBVoltagePercentage());                   // Phase B voltage percentage
		messageBuilder.append(getPhaseCVoltagePercentage());                   // Phase C voltage percentage
		messageBuilder.append(getPhaseACurrentPercentage());                   // Phase A current percentage
		messageBuilder.append(getPhaseBCurrentPercentage());                   // Phase B current percentage
		messageBuilder.append(getPhaseCCurrentPercentage() );                  // Phase C current percentage
		messageBuilder.append(getMeterConstant() );                            // Meter constant
		messageBuilder.append(getNumOfPulse());                                // Number of pulse
		messageBuilder.append(getConjunctionSplitPhase());                     // Conjunction phase/split phase


		// Calculate and append check bit
		//ApplicationLauncher.logger.info("messageBuilderX : " + messageBuilder.toString());
		String checkByte = GuiUtils.generateCheckSumWithOneByte(messageBuilder.toString());
		messageBuilder.append(checkByte);

		// Append end bit
		messageBuilder.append(ConstantPowerSourceBofa.END_BYTE);

		// Final message
		parameterSettingCmdFrame = messageBuilder.toString();
		ApplicationLauncher.logger.info("parameterSettingCmdFrame : " + parameterSettingCmdFrame);
		ApplicationLauncher.logger.info("frameParameterSettingCommand:Exit");
	}

	//===========================================================================================================
	public static void frameVoltageCurrentEnableOutputCommand(){
		ApplicationLauncher.logger.info("frameVoltageCurrentOutputCommand:Entry");
		// Frame the message
		StringBuilder messageBuilder = new StringBuilder();

		messageBuilder  =   messageBuilder.append(ConstantPowerSourceBofa.START_BYTE);                                        // Start bit
		messageBuilder.append(ConstantPowerSourceBofa.ADDRESS_BYTE);               // Address
		messageBuilder.append(ConstantPowerSourceBofa.VOLTAGE_CURRENT_OUTPUT_CMD); // Command

		// Calculate and append check bit
		String checkByte = GuiUtils.generateCheckSumWithOneByte(messageBuilder.toString());
		messageBuilder.append(checkByte);

		// Append end bit
		messageBuilder.append(ConstantPowerSourceBofa.END_BYTE);

		// Final message
		voltageCurrentOutputCmdFrame = messageBuilder.toString();
		ApplicationLauncher.logger.info("voltageCurrentOutputCmdFrame : " + voltageCurrentOutputCmdFrame);

		ApplicationLauncher.logger.info("frameVoltageCurrentOutputCommand:Exit");
	}

	//===========================================================================================================

	public static void frameVoltageCurrentStopOutputCommand(){ 

		ApplicationLauncher.logger.info("frameVoltageCurrentStopOutputCommand:Entry");	
		// Frame the message
		StringBuilder messageBuilder = new StringBuilder();

		messageBuilder = messageBuilder.append(ConstantPowerSourceBofa.START_BYTE);                             // Start bit
		messageBuilder.append(ConstantPowerSourceBofa.ADDRESS_BYTE);                     // Address
		messageBuilder.append(ConstantPowerSourceBofa.VOLTAGE_CURRENT_STOP_OUTPUT_CMD);  // Command
		//	ApplicationLauncher.logger.info("voltageCurrentStopOutputCmdFrame : messageBuilder: " + messageBuilder);
		// Calculate and Append check bit
		String checkByte = GuiUtils.generateCheckSumWithOneByte(messageBuilder.toString());
		messageBuilder.append(checkByte);

		// Append end bit
		messageBuilder.append(ConstantPowerSourceBofa.END_BYTE);

		// Final message
		voltageCurrentStopOutputCmdFrame = messageBuilder.toString();	
		ApplicationLauncher.logger.info("voltageCurrentStopOutputCmdFrame : " + voltageCurrentStopOutputCmdFrame);

		ApplicationLauncher.logger.info("frameVoltageCurrentStopOutputCommand:Exit");

	}

	//===========================================================================================================

	public static void frameCurrentStoppingCommand(){

		ApplicationLauncher.logger.info("frameCurrentStoppingCommand");
		// Frame the message
		StringBuilder messageBuilder = new StringBuilder();

		messageBuilder  =   messageBuilder.append(ConstantPowerSourceBofa.START_BYTE);                                             // Start bit
		messageBuilder.append(ConstantPowerSourceBofa.ADDRESS_BYTE);                    // Address
		messageBuilder.append(ConstantPowerSourceBofa.CURRENT_STOPPING_COMMAND); // Command

		// Calculate and append check bit
		String checkByte = GuiUtils.generateCheckSumWithOneByte(messageBuilder.toString());
		messageBuilder.append(checkByte);

		// Append end bit
		messageBuilder.append(ConstantPowerSourceBofa.END_BYTE);

		// Final message
		currentStoppingCmdFrame = messageBuilder.toString();	
		ApplicationLauncher.logger.info("currentStoppingCmdFrame : " + currentStoppingCmdFrame);
		ApplicationLauncher.logger.info("frameCurrentStoppingCommand:Exit"); 

	}
	//===========================================================================================================

	static boolean sendParameterSettingCommand(){
		ApplicationLauncher.logger.info("sendParameterSettingCommand: Entry");  

		DeviceDataManagerController.setParameterSettingCmdVariablesBofaSource();
		frameParameterSettingCommand();
		String payLoadInHex = parameterSettingCmdFrame;//GuiUtils.StringToHex(parameterSettingCmdFrame);
		ApplicationLauncher.logger.info("sendParameterSettingCommand : payLoadInHex: " + payLoadInHex  );

		int timeDelayInMilliSec = 0;
		boolean status = false;
		String expectedDataInHex = ConstantPowerSourceBofa.ER_STARTS_WITH;
		boolean isResponseExpected = true;

		try {
			ApplicationLauncher.logger.debug("sendParameterSettingCommand: comPortSemaphore : semaphore : acquiring...");				
			BofaManager.comPortSemaphore.acquire(); // Try to Acquire the semaphore

			ApplicationLauncher.logger.debug("sendParameterSettingCommand: comPortSemaphore : semaphore : acquired");	
			String responseStatus = "";
			if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
				//responseStatus = BofaManager.getPwrSrcBofaMessenger().bofaPowerSourceMsngrSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);

			}else{
				responseStatus = BofaManager.getSerialDM_Obj().bofaPowerSourceSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
			}
			if (responseStatus == DeleteMeConstant.SUCCESS_RESPONSE) {
				//success = true; 
				String responseData = "";
				if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
					responseData = BofaManager.getPwrSrcBofaMessenger().getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage();
					responseData = GuiUtils.asciiToHex(responseData);
				}else{
					responseData = BofaManager.getSerialDM_Obj().getRxMsgQ_PwrSrc().getLastReadMessage();//""; // BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
				}
				status = processResponse(parameterSettingCmdFrame, responseData);
			}					
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.debug("sendParameterSettingCommand :Exception :" + e.getMessage());
		} finally {
			BofaManager.comPortSemaphore.release(); // Release the semaphore 
			ApplicationLauncher.logger.debug("sendParameterSettingCommand : comPortSemaphore : released");		
			ApplicationLauncher.logger.debug("sendParameterSettingCommand : comPortSemaphore : availablePermits : " + BofaManager.comPortSemaphore.availablePermits());
			
			Sleep(220);
		}

		//=============================================
		ApplicationLauncher.logger.info("sendParameterSettingCommand: Exit");  
		return status;
	}
	
	
	static boolean sendParameterSettingCmdV2(){
		ApplicationLauncher.logger.info("sendParameterSettingCmdV2: Entry");  

		DeviceDataManagerController.setParameterSettingCmdVariablesBofaSource();
		frameParameterSettingCommand();
		String payLoadInHex = parameterSettingCmdFrame;//GuiUtils.StringToHex(parameterSettingCmdFrame);
		ApplicationLauncher.logger.info("sendParameterSettingCmdV2 : payLoadInHex: " + payLoadInHex  );

		int timeDelayInMilliSec = 0;
		boolean status = false;
		String expectedDataInHex = ConstantPowerSourceBofa.ER_STARTS_WITH;
		boolean isResponseExpected = true;

		try {
			//ApplicationLauncher.logger.debug("sendParameterSettingCmdV2: semaphore acquiring...");				
			//BofaManager.comPortSemaphore.acquire(); // Try to Acquire the semaphore

			//ApplicationLauncher.logger.debug("sendParameterSettingCmdV2: semaphore acquired");	
			//String responseStatus = "";
			//Map<String,Object> responseReturn = new HashMap<String,Object>();
			//responseStatus = BofaManager.getPwrSrcBofaMessenger().bofaPowerSourceMsngrSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
			//responseReturn = BofaManager.sendDataToBofaAfterSemaPhoreAcquired("sendParameterSettingCmdV2",payLoadInHex, timeDelayInMilliSec,
			//String responseStatus = "";
			//responseStatus = BofaManager.sendDataToBofaAfterSemaPhoreAcquired("sendParameterSettingCmdV2",payLoadInHex, timeDelayInMilliSec,		
            //        expectedDataInHex, isResponseExpected);
			boolean responseStatus = false;
			Map<String,Object> responseReturn = new HashMap<String,Object>();
			boolean forceExecute = false;
			responseReturn = BofaManager.sendDataToBofaAfterSemaPhoreAcquired("sendParameterSettingCmdV2",payLoadInHex, timeDelayInMilliSec,
                    expectedDataInHex, isResponseExpected,forceExecute);
			
			responseStatus = (boolean)responseReturn.get("status");
			if(responseStatus){
				String responseData = (String)responseReturn.get("result");
				status =  processResponse(parameterSettingCmdFrame, responseData);		
			}	
			//status = (boolean)responseReturn.get("status");
			//if(status){
			//	String responseData = (String)responseReturn.get("result");
			
			/*if (responseStatus == DeleteMeConstant.SUCCESS_RESPONSE) {
				//success = true; 
				String responseData = "";
				
					responseData = BofaManager.getPwrSrcBofaMessenger().getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage();
					//responseData = GuiUtils.asciiToHex(responseData);
					status = processResponse(parameterSettingCmdFrame, responseData);
			}*/
			
			/*}
			if (responseStatus == DeleteMeConstant.SUCCESS_RESPONSE) {
				//success = true; 
				String responseData = "";
				if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {*/
				//	responseData = BofaManager.getPwrSrcBofaMessenger().getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage();
					//responseData = GuiUtils.asciiToHex(responseData);
				/*}else{
					responseData = BofaManager.getSerialDM_Obj().getRxMsgQ_PwrSrc().getLastReadMessage();//""; // BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
				}*/
				//status = processResponse(parameterSettingCmdFrame, responseData);
			//}					
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.debug("sendParameterSettingCmdV2 :Exception :" + e.getMessage());
		} finally {
			//BofaManager.comPortSemaphore.release(); // Release the semaphore 
			//ApplicationLauncher.logger.debug("sendParameterSettingCmdV2: released");		
			Sleep(220);
		}

		//=============================================
		ApplicationLauncher.logger.info("sendParameterSettingCmdV2: Exit");  
		return status;
	}
	//===========================================================================================================

	static boolean sendVoltageCurrentEnableOutputCommand(){
		ApplicationLauncher.logger.info("sendVoltageCurrentEnableOutputCommand: Entry");  
		frameVoltageCurrentEnableOutputCommand();
		String payLoadInHex = voltageCurrentOutputCmdFrame;// GuiUtils.StringToHex(voltageCurrentOutputCmdFrame );
		ApplicationLauncher.logger.info("sendVoltageCurrentEnableOutputCommand : payLoadInHex: " + payLoadInHex  );
		boolean status = false;
		int timeDelayInMilliSec = 0;
		String expectedDataInHex = ConstantPowerSourceBofa.ER_STARTS_WITH;		
		boolean isResponseExpected = true;
		try {
			ApplicationLauncher.logger.debug("sendVoltageCurrentEnableOutputCommand: comPortSemaphore : acquiring...");
			//BofaManager.comPortSemaphore.acquire();
			ApplicationLauncher.logger.debug("sendVoltageCurrentEnableOutputCommand: comPortSemaphore : availablePermits : " + BofaManager.comPortSemaphore.availablePermits());
			
			BofaManager.comPortSemaphore.acquire(1);//acquire(); // Try to Acquire the semaphore
			ApplicationLauncher.logger.debug("sendVoltageCurrentEnableOutputCommand: comPortSemaphore : acquired");	
			String responseStatus = "";
			if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
				responseStatus = BofaManager.getPwrSrcBofaMessenger().bofaPowerSourceMsngrSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected,"sendVoltageCurrentEnableOutputCommand");

			}else{
				responseStatus = BofaManager.getSerialDM_Obj().bofaPowerSourceSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
			}

			if (responseStatus == DeleteMeConstant.SUCCESS_RESPONSE) {
				String responseData = "";
				if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
					responseData = BofaManager.getPwrSrcBofaMessenger().getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage();
					responseData = GuiUtils.asciiToHex(responseData);
				}else{			
					responseData =  new String(BofaManager.getSerialDM_Obj().getRxMsgQ_PwrSrc().getLastReadMessage()); // BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
				}
				status = processResponse(parameterSettingCmdFrame, responseData);
			}		
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("writeHexToSerialPowerSource :Exception :" + e.getMessage());
		} finally {
			//BofaManager.comPortSemaphore.drainPermits();
			BofaManager.comPortSemaphore.release(1); // Release the semaphore 
			ApplicationLauncher.logger.debug("sendVoltageCurrentEnableOutputCommand : comPortSemaphore : released");	
			ApplicationLauncher.logger.debug("sendVoltageCurrentEnableOutputCommand : comPortSemaphore : t-availablePermits : " + BofaManager.comPortSemaphore.availablePermits());
			
			Sleep(220);
		}

		ApplicationLauncher.logger.info("sendVoltageCurrentEnableOutputCommand: Exit");  
		return status;
	}
	
	
	
	static boolean sendVoltageCurrentEnableOutputCommandWithQueue(){
		ApplicationLauncher.logger.info("sendVoltageCurrentEnableOutputCommandWithQueue: Entry");  
		frameVoltageCurrentEnableOutputCommand();
		String payLoadInHex = voltageCurrentOutputCmdFrame;// GuiUtils.StringToHex(voltageCurrentOutputCmdFrame );
		ApplicationLauncher.logger.info("sendVoltageCurrentEnableOutputCommandWithQueue : payLoadInHex: " + payLoadInHex  );
		boolean status = false;
		int timeDelayInMilliSec = 0;
		String expectedDataInHex = ConstantPowerSourceBofa.ER_STARTS_WITH;		
		boolean isResponseExpected = true;
		try {
			//ApplicationLauncher.logger.debug("sendVoltageCurrentEnableOutputCommandWithQueue: comPortSemaphore : acquiring...");

			//ApplicationLauncher.logger.debug("sendVoltageCurrentEnableOutputCommandWithQueue: comPortSemaphore : availablePermits : " + BofaManager.comPortSemaphore.availablePermits());
			
			//BofaManager.comPortSemaphore.acquire(1);//acquire(); // Try to Acquire the semaphore
			//ApplicationLauncher.logger.debug("sendVoltageCurrentEnableOutputCommandWithQueue: comPortSemaphore : acquired");	
			String responseStatus = "";
			if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
				//responseStatus = BofaManager.getPwrSrcBofaMessenger().bofaPowerSourceMsngrSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected,"sendVoltageCurrentEnableOutputCommandWithQueue");

				Callable<Boolean> taskBofaSendVoltageCurrentEnableOutputCommand = () -> {
					//Data_RefStdBofa.sendActualOutputValueReadingCommand();
					String queuedResponseStatus = BofaManager.getPwrSrcBofaMessenger().bofaPowerSourceMsngrSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected,"sendVoltageCurrentEnableOutputCommandWithQueue");
					String queuedResponseData = "";
					boolean queueStatus =false;
					
					
					if (queuedResponseStatus == DeleteMeConstant.SUCCESS_RESPONSE) {
						//String responseData = "";
						if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
							queuedResponseData = BofaManager.getPwrSrcBofaMessenger().getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage();
							queuedResponseData = GuiUtils.asciiToHex(queuedResponseData);
						}else{			
							queuedResponseData =  new String(BofaManager.getSerialDM_Obj().getRxMsgQ_PwrSrc().getLastReadMessage()); // BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
						}
						queueStatus = processResponse(parameterSettingCmdFrame, queuedResponseData);
					}
					/*if (queuedResponseStatus.equals(DeleteMeConstant.SUCCESS_RESPONSE)) {
						if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
							queuedResponseData = pwrSrcBofaMessenger.getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage();
							queuedResponseData = GuiUtils.asciiToHex(responseData);
							
						}else{
							queuedResponseData = BofaManager.getSerialDM_Obj().getRxMsgQ_PwrSrc().getLastReadMessage();//BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
						}
						queueStatus = true;
						
					}else {
						if(!isResponseExpected){
							if(queuedResponseStatus.equals(DeleteMeConstant.NO_RESPONSE)){
								ApplicationLauncher.logger.info("sendDataToBofaWithQueue : <"+sourceThread +">:no response expected success");
								queueStatus = true;
							}
						}
					}*/
					
					//Map<String,Object> queuedResponseReturn = new HashMap<String,Object>();
					//queuedResponseReturn.put("status", queueStatus);
					//queuedResponseReturn.put("result", queuedResponseData);
					return queueStatus;
		        };
				int priority = 10;//10= low // 1 is low , 100 is high
				long timeoutInSec = 10;//for 5 - concurrent issue occured
				Future<Boolean> future = BofaRequestProcessor.addRequest(ConstantPowerSourceBofa.BOFA_COM_PORT_KEY, taskBofaSendVoltageCurrentEnableOutputCommand, 5, timeoutInSec, TimeUnit.SECONDS);

				// Option 1: Wait for it to complete, but with your own timeout
				try {
					status = future.get(timeoutInSec, TimeUnit.SECONDS); // This waits for result
				    ApplicationLauncher.logger.debug("sendVoltageCurrentEnableOutputCommandWithQueue : sendActualOutput: Task completed: responseStatus : " + responseStatus);
				} catch (TimeoutException e) {
					ApplicationLauncher.logger.debug("sendVoltageCurrentEnableOutputCommandWithQueue : sendActualOutput : Task timed out (from caller side)");
					//responseReturn.put("status", false);
					//responseReturn.put("result", "sendVoltageCurrentEnableOutputCommandWithQueue" + "-Timed Out");
				} catch (ExecutionException e) {
					ApplicationLauncher.logger.debug("sendVoltageCurrentEnableOutputCommandWithQueue : sendActualOutput : Task failed: " + e.getCause());
					//responseReturn.put("status", false);
					//responseReturn.put("result", "sendVoltageCurrentEnableOutputCommandWithQueue" + "-Task Failed");
				} catch (InterruptedException e) {
				    Thread.currentThread().interrupt();
				    ApplicationLauncher.logger.debug("sendVoltageCurrentEnableOutputCommandWithQueue : sendActualOutput : Task interrupted: ");
					
				    //responseReturn.put("status", false);
					//responseReturn.put("result", "sendVoltageCurrentEnableOutputCommandWithQueue" + "-Interupted");
				}
				
			}else{
				responseStatus = BofaManager.getSerialDM_Obj().bofaPowerSourceSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
			}

/*			if (responseStatus == DeleteMeConstant.SUCCESS_RESPONSE) {
				String responseData = "";
				if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
					responseData = BofaManager.getPwrSrcBofaMessenger().getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage();
					responseData = GuiUtils.asciiToHex(responseData);
				}else{			
					responseData =  new String(BofaManager.getSerialDM_Obj().getRxMsgQ_PwrSrc().getLastReadMessage()); // BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
				}
				status = processResponse(parameterSettingCmdFrame, responseData);
			}*/		
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("sendVoltageCurrentEnableOutputCommandWithQueue :Exception :" + e.getMessage());
		} finally {
			//BofaManager.comPortSemaphore.drainPermits();
			//BofaManager.comPortSemaphore.release(1); // Release the semaphore 
			//ApplicationLauncher.logger.debug("sendVoltageCurrentEnableOutputCommandWithQueue : comPortSemaphore : released");	
			//ApplicationLauncher.logger.debug("sendVoltageCurrentEnableOutputCommandWithQueue : comPortSemaphore : t-availablePermits : " + BofaManager.comPortSemaphore.availablePermits());
			
			Sleep(220);
		}

		ApplicationLauncher.logger.info("sendVoltageCurrentEnableOutputCommandWithQueue: Exit");  
		return status;
	}

	//===========================================================================================================
	public static void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("Sleep2 :InterruptedException:"+ e.getMessage());
		}

	}
	//===========================================================================================================

	public static boolean sendVoltageCurrentStopOutputCommand(){
		ApplicationLauncher.logger.info("sendVoltageCurrentStopOutputCommand: Entry");  
		frameVoltageCurrentStopOutputCommand();
		String payLoadInHex = voltageCurrentStopOutputCmdFrame;//GuiUtils.StringToHex(voltageCurrentStopOutputCmdFrame );
		ApplicationLauncher.logger.info("sendVoltageCurrentStopOutputCommand : payLoadInHex: " + payLoadInHex  );

		int timeDelayInMilliSec = 0;
		String expectedDataInHex = ConstantPowerSourceBofa.ER_STARTS_WITH;
		boolean isResponseExpected = true;
		boolean status = false;

		String responseStatus = "";
		if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
			//responseStatus = BofaManager.getPwrSrcBofaMessenger().bofaPowerSourceMsngrSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
			//responseStatus = BofaManager.getPwrSrcBofaMessenger().bofaPowerSourceMsngrSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);

		}else{
			responseStatus = BofaManager.getSerialDM_Obj().bofaPowerSourceSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
		}
		if (responseStatus == DeleteMeConstant.SUCCESS_RESPONSE) {
			String responseData = "";
			if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
				responseData = BofaManager.getPwrSrcBofaMessenger().getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage();
				responseData = GuiUtils.asciiToHex(responseData);
			}else{ 
				responseData = BofaManager.getSerialDM_Obj().getRxMsgQ_PwrSrc().getLastReadMessage();//""; // BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();

			}

			status = processResponse(voltageCurrentStopOutputCmdFrame, responseData);
		}


		ApplicationLauncher.logger.info("sendVoltageCurrentStopOutputCommand: Exit");  
		return status;
	}
	
	
	public static boolean sendVoltageCurrentStopOutputCmdV2(){
		ApplicationLauncher.logger.info("sendVoltageCurrentStopOutputCmdV2: Entry");  
		frameVoltageCurrentStopOutputCommand();
		String payLoadInHex = voltageCurrentStopOutputCmdFrame;//GuiUtils.StringToHex(voltageCurrentStopOutputCmdFrame );
		ApplicationLauncher.logger.info("sendVoltageCurrentStopOutputCmdV2 : payLoadInHex: " + payLoadInHex  );

		int timeDelayInMilliSec = 0;
		String expectedDataInHex = ConstantPowerSourceBofa.ER_STARTS_WITH;
		boolean isResponseExpected = true;
		boolean status = false;

		boolean responseStatus = false;
		//if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
			//responseStatus = BofaManager.getPwrSrcBofaMessenger().bofaPowerSourceMsngrSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
			Map<String,Object> responseReturn = new HashMap<String,Object>();
			boolean forceExecute = true;
			responseReturn = BofaManager.sendDataToBofaAfterSemaPhoreAcquired("sendVoltageCurrentStopOutputCmdV2",payLoadInHex, timeDelayInMilliSec,
					                                              expectedDataInHex, isResponseExpected,forceExecute);
			responseStatus = (boolean)responseReturn.get("status");
		//}else{
			//responseStatus = BofaManager.getSerialDM_Obj().bofaPowerSourceSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
		//}
		if (responseStatus) {
			String responseData = "";
			
				responseData = BofaManager.getPwrSrcBofaMessenger().getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage();
				responseData = GuiUtils.asciiToHex(responseData);
			

			status = processResponse(voltageCurrentStopOutputCmdFrame, responseData);
		}


		ApplicationLauncher.logger.info("sendVoltageCurrentStopOutputCmdV2: Exit");  
		return status;
	}
	//===========================================================================================================

	public static boolean sendCurrentStoppingCommand(){
		ApplicationLauncher.logger.info("sendCurrentStoppingCommand: Entry");  
		frameCurrentStoppingCommand();
		String payLoadInHex = currentStoppingCmdFrame;//GuiUtils.StringToHex(currentStoppingCmdFrame );
		ApplicationLauncher.logger.info("sendCurrentStoppingCommand : payLoadInHex: " + payLoadInHex  );

		int timeDelayInMilliSec = 0;
		String expectedDataInHex = ConstantPowerSourceBofa.ER_STARTS_WITH;
		boolean isResponseExpected = true;

		boolean status = false;

		/*	String responseStatus = BofaManager.getSerialDM_Obj().bofaPowerSourceSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
		if (responseStatus == DeleteMeConstant.SUCCESS_RESPONSE) {
			String CurrentReadData = ""; // BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
			status = processResponse(currentStoppingCmdFrame, CurrentReadData);
		}*/


		Map<String,Object> responseReturn = new HashMap<String,Object>();
		boolean forceExecute = false;
		responseReturn = BofaManager.sendDataToBofaAfterSemaPhoreAcquired("sendCurrentStoppingCommand",payLoadInHex, timeDelayInMilliSec,
				expectedDataInHex, isResponseExpected,forceExecute);
		status = (boolean)responseReturn.get("status");
		if(status){
			String responseData = (String)responseReturn.get("result");
			status =  processResponse(currentStoppingCmdFrame, responseData);		
		}	


		ApplicationLauncher.logger.info("sendCurrentStoppingCommand: Exit");
		return status;
	}
	//===========================================================================================================


	static boolean processResponse(String messageType, String response){
		ApplicationLauncher.logger.info("processResponse: Entry"); 
		boolean status = false ;

		try{
			if (messageType == parameterSettingCmdFrame) {
				status = parseAcknowledgementResponses(response);
			} 
			else if (messageType == voltageCurrentOutputCmdFrame) {
				status = parseAcknowledgementResponses(response);

			}
			else if (messageType == voltageCurrentStopOutputCmdFrame) {
				status = parseAcknowledgementResponses(response);
			}

			else if (messageType == currentStoppingCmdFrame) {
				status = parseAcknowledgementResponses(response);
			}
		}catch(Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("processResponse: Exception: " + e.getMessage());  
		}

		return status ;

		/*if(responseStatus.equals(DeleteMeConstant.SUCCESS_RESPONSE)){

			}else if(responseStatus.equals(DeleteMeConstant.NO_RESPONSE)){

				ApplicationLauncher.logger.info("VI Start:" + ErrorCodeMapping.ERROR_CODE_004 +": "+ErrorCodeMapping.ERROR_CODE_004_MSG);
				ApplicationLauncher.InformUser(ErrorCodeMapping.ERROR_CODE_004,ErrorCodeMapping.ERROR_CODE_004_MSG,AlertType.ERROR);

			}else {
				String ExpectedError1Data = DeleteMeConstant.HV_CMD_ERROR_RESPONSE_HDR;

				ApplicationLauncher.logger.info("VI Start: responseStatus:" + responseStatus);
				ApplicationLauncher.logger.info("VI Start: ExpectedError1Data:" + ExpectedError1Data);
				if ( (responseStatus.contains(ExpectedError1Data)) && 
					 (responseStatus.endsWith(DeleteMeConstant.HV_ER_TERMINATOR))){	
					ApplicationLauncher.logger.info("VI Start: response: "+responseStatus);
					String ErrorMsgID = ErrorCodeMapping.getErrorCodeID(responseStatus);
					String ErrorMsgDesc = ErrorCodeMapping.getErrorMsgDescription(ErrorMsgID);
					ApplicationLauncher.logger.info("VI Start:" + ErrorMsgID +": "+ErrorMsgDesc);
					ApplicationLauncher.InformUser(ErrorMsgID,ErrorMsgDesc,AlertType.ERROR);

				}
			}		*/


	}
	//===========================================================================================================

	static boolean parseAcknowledgementResponses(String response){
		ApplicationLauncher.logger.info("Data_PowerSourceBofa: parseAcknowledgementResponses: Entry");  
		ApplicationLauncher.logger.info("Data_PowerSourceBofa: parseAcknowledgementResponses: response: " + response); 
		boolean status = false;

		String commandCode   = response.substring(0, 2);
		String address       = response.substring(2, 4);
		String ackPart       = response.substring(4, 6);
		String checksum      = response.substring(6, 8);
		String endByte       = response.substring(8);

		String responseStatus = getResponseStatus(ackPart);

/*		ApplicationLauncher.logger.debug("parseAcknowledgementResponses : Command Code   : " + commandCode);  
		ApplicationLauncher.logger.debug("parseAcknowledgementResponses : Address        : " + address);  
		ApplicationLauncher.logger.debug("parseAcknowledgementResponses : Polarity       : " + responseStatus);  
		ApplicationLauncher.logger.debug("parseAcknowledgementResponses : Checksum Bytes : " + checksum);  
		ApplicationLauncher.logger.debug("parseAcknowledgementResponses : End Bytes      : " + endByte); */

		/*boolean isValid = validateChecksum(response);
        if (isValid){
        	ApplicationLauncher.logger.debug("parseActualOutputValueResponse : Check Sum is Valid " );
        	status = true ;
        }
        else {
        	status = false ;
        	ApplicationLauncher.logger.debug("parseActualOutputValueResponse : Check Sum is Invalid");
        	return status ;
        }*/

		if(commandCode.equals(ConstantPowerSourceBofa.START_BYTE) && address.equals(ConstantPowerSourceBofa.ADDRESS_BYTE) && ackPart.equals(ConstantPowerSourceBofa.POSITIVE)&& endByte.equals(ConstantPowerSourceBofa.END_BYTE)){
			status = true ;
		}
		else{
			status = false ;
			if (ackPart.equals(ConstantPowerSourceBofa.NEGATIVE)) {
				ApplicationLauncher.logger.debug("parseActualOutputValueResponse : NEGATIVE Acknowledgment received ");
			}
		}
		//ApplicationLauncher.logger.debug("parseAcknowledgementResponses: Exit");  
		return status ;

	}
	//===========================================================================================================


	static String getResponseStatus(String responseStatus) {
		switch (responseStatus) {
		case "06":
			return "Positive";
		case "15":
			return "Negative";
		default:
			return "Unknown";     
		}
	}
	//===========================================================================================================


	/*	public static boolean validateChecksum(String hexString) {
        // Check if the string is empty or has less than 2 characters (1 byte minimum)
        if (hexString.isEmpty() || hexString.length() < 2) {
            return false;
        }

        // Extract the checksum byte (last character) and the bytes to calculate checksum from the input string
        String dataString = hexString.substring(0, hexString.length() - 4);
        String checksumString   = hexString.substring(hexString.length() - 4, hexString.length() - 2);

        ApplicationLauncher.logger.debug("validateChecksum : dataString     : " + dataString);
		ApplicationLauncher.logger.debug("validateChecksum : checksumString : " + checksumString);

        // Convert checksum and data strings to bytes
        byte receivedChecksum = hexStringToByte(checksumString);
        byte[] data   = hexStringToByteArray(dataString);

        // Calculate the checksum
        byte calculatedChecksum = calculateChecksum(data);
		ApplicationLauncher.logger.debug("calculatedChecksum  : " + calculatedChecksum);
		ApplicationLauncher.logger.debug("receivedChecksum    : " + receivedChecksum);

        // Compare the calculated checksum with the expected checksum
        return receivedChecksum == calculatedChecksum;
    }*/	
	//========================================		
	/*	private static byte hexStringToByte(String hexString) {
		   // return (byte) Integer.parseInt(hexString, 16);
				int intValue = Integer.parseInt(hexString, 16);
				ApplicationLauncher.logger.debug("intValue  : " + intValue);
			    return (byte) (intValue <= 127 ? intValue : intValue - 256);
    }

    private static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }

    private static byte calculateChecksum(byte[] bytes) {
        byte checksum = 0;
        for (byte b : bytes) {
            checksum ^= b;
        }
        return checksum;
    }*/











	//==========================================================================================================


	// Method to calculate the check bit
	/*	private static byte calculateCheckByte(String message) {
		ApplicationLauncher.logger.info("calculateCheckByte:Entry");
		// For simplicity, let's just XOR all bytes in the message
		byte checkByte = 0;
		for (int i = 0; i < message.length(); i += 2) {
			checkByte ^= (byte) Integer.parseInt(message.substring(i, i + 2), 16);
		}
		return checkByte;
	}*/


	//=======================================================================================
	public static String getAddress() {
		return address;
	}
	public static String getCommand() {
		return command;
	}
	public static String getMeterType() {
		return meterType;
	}
	public static String getBasicVoltage() {
		return basicVoltage;
	}
	public static String getBasicCurrent() {
		return basicCurrent;
	}
	public static String getOutputFrequency() {
		return outputFrequency;
	}
	public static String getOutputPhaseSequence() {
		return outputPhaseSequence;
	}
	public static String getCurrentDirection() {
		return currentDirection;
	}
	public static String getUaUbAngle() {
		return uaUbAngle;
	}
	public static String getUaUcAngle() {
		return uaUcAngle;
	}
	public static String getUaIaAngle() {
		return uaIaAngle;
	}
	public static String getUbIbAngle() {
		return ubIbAngle;
	}
	public static String getUcIcAngle() {
		return ucIcAngle;
	}
	public static String getWaveForm() {
		return waveForm;
	}
	public static String getFirstHarmonic() {
		return firstHarmonic;
	}
	public static String getSecondHarmonic() {
		return secondHarmonic;
	}
	public static String getThirdHarmonic() {
		return thirdHarmonic;
	}
	public static String getFirstHarmonicPercentageVoltage() {
		return firstHarmonicPercentageVoltage;
	}
	public static String getSecondHarmonicPercentageVoltage() {
		return secondHarmonicPercentageVoltage;
	}
	public static String getThirdHarmonicPercentageVoltage() {
		return thirdHarmonicPercentageVoltage;
	}
	public static String getFirstHarmonicPercentageCurrent() {
		return firstHarmonicPercentageCurrent;
	}
	public static String getSecondHarmonicPercentageCurrent() {
		return secondHarmonicPercentageCurrent;
	}
	public static String getThirdHarmonicPercentageCurrent() {
		return thirdHarmonicPercentageCurrent;
	}
	public static String getFirstHarmonicPercentagePhaseAngle() {
		return firstHarmonicPercentagePhaseAngle;
	}
	public static String getSecondHarmonicPercentagePhaseAngle() {
		return secondHarmonicPercentagePhaseAngle;
	}
	public static String getThirdHarmonicPercentagePhaseAngle() {
		return thirdHarmonicPercentagePhaseAngle;
	}
	public static String getPhaseAVoltagePercentage() {
		return phaseAVoltagePercentage;
	}
	public static String getPhaseBVoltagePercentage() {
		return phaseBVoltagePercentage;
	}
	public static String getPhaseCVoltagePercentage() {
		return phaseCVoltagePercentage;
	}
	public static String getPhaseACurrentPercentage() {
		return phaseACurrentPercentage;
	}
	public static String getPhaseBCurrentPercentage() {
		return phaseBCurrentPercentage;
	}
	public static String getPhaseCCurrentPercentage() {
		return phaseCCurrentPercentage;
	}
	public static String getMeterConstant() {
		return meterConstant;
	}
	public static String getNumOfPulse() {
		return numOfPulse;
	}
	public static String getConjunctionSplitPhase() {
		return conjunctionSplitPhase;
	}
	public static void setAddress(String address) {
		Data_PowerSourceBofa.address = address;
	}
	public static void setCommand(String command) {
		Data_PowerSourceBofa.command = command;
	}
	public static void setMeterType(String meterType) {
		Data_PowerSourceBofa.meterType = meterType;
	}
	public static void setBasicVoltage(String basicVoltage) {
		Data_PowerSourceBofa.basicVoltage = basicVoltage;
	}
	public static void setBasicCurrent(String basicCurrent) {
		Data_PowerSourceBofa.basicCurrent = basicCurrent;
	}
	public static void setOutputFrequency(String outputFrequency) {
		Data_PowerSourceBofa.outputFrequency = outputFrequency;
	}
	public static void setOutputPhaseSequence(String outputPhaseSequence) {
		Data_PowerSourceBofa.outputPhaseSequence = outputPhaseSequence;
	}
	public static void setCurrentDirection(String currentDirection) {
		Data_PowerSourceBofa.currentDirection = currentDirection;
	}
	public static void setUaUbAngle(String uaUbAngle) {
		Data_PowerSourceBofa.uaUbAngle = uaUbAngle;
	}
	public static void setUaUcAngle(String uaUcAngle) {
		Data_PowerSourceBofa.uaUcAngle = uaUcAngle;
	}
	public static void setUaIaAngle(String uaIaAngle) {
		Data_PowerSourceBofa.uaIaAngle = uaIaAngle;
	}
	public static void setUbIbAngle(String ubIbAngle) {
		Data_PowerSourceBofa.ubIbAngle = ubIbAngle;
	}
	public static void setUcIcAngle(String ucIcAngle) {
		Data_PowerSourceBofa.ucIcAngle = ucIcAngle;
	}
	public static void setWaveForm(String waveForm) {
		Data_PowerSourceBofa.waveForm = waveForm;
	}
	public static void setFirstHarmonic(String firstHarmonic) {
		Data_PowerSourceBofa.firstHarmonic = firstHarmonic;
	}
	public static void setSecondHarmonic(String secondHarmonic) {
		Data_PowerSourceBofa.secondHarmonic = secondHarmonic;
	}
	public static void setThirdHarmonic(String thirdHarmonic) {
		Data_PowerSourceBofa.thirdHarmonic = thirdHarmonic;
	}
	public static void setFirstHarmonicPercentageVoltage(String firstHarmonicPercentageVoltage) {
		Data_PowerSourceBofa.firstHarmonicPercentageVoltage = firstHarmonicPercentageVoltage;
	}
	public static void setSecondHarmonicPercentageVoltage(String secondHarmonicPercentageVoltage) {
		Data_PowerSourceBofa.secondHarmonicPercentageVoltage = secondHarmonicPercentageVoltage;
	}
	public static void setThirdHarmonicPercentageVoltage(String thirdHarmonicPercentageVoltage) {
		Data_PowerSourceBofa.thirdHarmonicPercentageVoltage = thirdHarmonicPercentageVoltage;
	}
	public static void setFirstHarmonicPercentageCurrent(String firstHarmonicPercentageCurrent) {
		Data_PowerSourceBofa.firstHarmonicPercentageCurrent = firstHarmonicPercentageCurrent;
	}
	public static void setSecondHarmonicPercentageCurrent(String secondHarmonicPercentageCurrent) {
		Data_PowerSourceBofa.secondHarmonicPercentageCurrent = secondHarmonicPercentageCurrent;
	}
	public static void setThirdHarmonicPercentageCurrent(String thirdHarmonicPercentageCurrent) {
		Data_PowerSourceBofa.thirdHarmonicPercentageCurrent = thirdHarmonicPercentageCurrent;
	}
	public static void setFirstHarmonicPercentagePhaseAngle(String firstHarmonicPercentagePhaseAngle) {
		Data_PowerSourceBofa.firstHarmonicPercentagePhaseAngle = firstHarmonicPercentagePhaseAngle;
	}
	public static void setSecondHarmonicPercentagePhaseAngle(String secondHarmonicPercentagePhaseAngle) {
		Data_PowerSourceBofa.secondHarmonicPercentagePhaseAngle = secondHarmonicPercentagePhaseAngle;
	}
	public static void setThirdHarmonicPercentagePhaseAngle(String thirdHarmonicPercentagePhaseAngle) {
		Data_PowerSourceBofa.thirdHarmonicPercentagePhaseAngle = thirdHarmonicPercentagePhaseAngle;
	}
	public static void setPhaseAVoltagePercentage(String phaseAVoltagePercentage) {
		Data_PowerSourceBofa.phaseAVoltagePercentage = phaseAVoltagePercentage;
	}
	public static void setPhaseBVoltagePercentage(String phaseBVoltagePercentage) {
		Data_PowerSourceBofa.phaseBVoltagePercentage = phaseBVoltagePercentage;
	}
	public static void setPhaseCVoltagePercentage(String phaseCVoltagePercentage) {
		Data_PowerSourceBofa.phaseCVoltagePercentage = phaseCVoltagePercentage;
	}
	public static void setPhaseACurrentPercentage(String phaseACurrentPercentage) {
		Data_PowerSourceBofa.phaseACurrentPercentage = phaseACurrentPercentage;
	}
	public static void setPhaseBCurrentPercentage(String phaseBCurrentPercentage) {
		Data_PowerSourceBofa.phaseBCurrentPercentage = phaseBCurrentPercentage;
	}
	public static void setPhaseCCurrentPercentage(String phaseCCurrentPercentage) {
		Data_PowerSourceBofa.phaseCCurrentPercentage = phaseCCurrentPercentage;
	}
	public static void setMeterConstant(String meterConstant) {
		Data_PowerSourceBofa.meterConstant = meterConstant;
	}
	public static void setNumOfPulse(String numOfPulse) {
		Data_PowerSourceBofa.numOfPulse = numOfPulse;
	}
	public static void setConjunctionSplitPhase(String conjunctionSplitPhase) {
		Data_PowerSourceBofa.conjunctionSplitPhase = conjunctionSplitPhase;
	}

	//==============================================================================================================








	//=====================================================================================
}
