package com.tasnetwork.calibration.energymeter.messenger;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantPowerSourceBofa;
import com.tasnetwork.calibration.energymeter.constant.ConstantPowerSourceLscs;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.constant.DeleteMeConstant;
import com.tasnetwork.calibration.energymeter.deployment.BofaManager;
import com.tasnetwork.calibration.energymeter.deployment.FailureManager;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
import com.tasnetwork.calibration.energymeter.device.Data_LduBofa;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.serial.portmanagerV2.SerialPortManagerPwrSrc_V2;
import com.tasnetwork.calibration.energymeter.util.ErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;

public class PowerSourceBofaMessenger {

	DeviceDataManagerController displayDataObj =  new DeviceDataManagerController();
	SerialPortManagerPwrSrc_V2 pwrSrcSpmObj =  new DeviceDataManagerController().getSerialPortManagerPwrSrc_V2();


	public boolean sendVoltageCurrentStopOutputCommand(){
		ApplicationLauncher.logger.info("sendVoltageCurrentStopOutputCommand: Entry");  
		//frameVoltageCurrentStopOutputCommand();
		String payLoadInHex = ConstantPowerSourceBofa.CMD_STOP_VOLT_CURRENT_IN_HEX;//voltageCurrentStopOutputCmdFrame;//GuiUtils.StringToHex(voltageCurrentStopOutputCmdFrame );
		ApplicationLauncher.logger.info("sendVoltageCurrentStopOutputCommand : payLoadInHex: " + payLoadInHex  );

		int timeDelayInMilliSec = 0;
		String expectedData = GuiUtils.hexToAscii(ConstantPowerSourceBofa.ER_STARTS_WITH);
		boolean isResponseExpected = true;
		boolean status = false;
		String rxMessageTerminator = GuiUtils.hexToAscii(ConstantPowerSourceBofa.END_BYTE);
		Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn = getPwrSrcSpmObj().powerSourceSendCommandProcess( payLoadInHex, timeDelayInMilliSec,
				expectedData,isResponseExpected, rxMessageTerminator,"sendVoltageCurrentStopOutputCommand");
		status = (boolean)responseReturn.get("status");
		if(status){
			String responseData = (String)responseReturn.get("result");
			ApplicationLauncher.logger.debug("sendVoltageCurrentStopOutputCommand : responseData : " + responseData);
			ApplicationLauncher.logger.debug("sendVoltageCurrentStopOutputCommand : responseData hex : " + GuiUtils.asciiToHex(responseData));
			//if(responseData.equals(GuiUtils.hexToAsciiV2(ConstantPowerSourceBofa.CMD_STOP_VOLT_CURRENT_IN_HEX))) {
			if(responseData.equals(GuiUtils.hexToAsciiV2(ConstantPowerSourceBofa.ER_CMD_STOP_VOLT_CURRENT_POSITIVE_IN_HEX))) {
				status = true;
			}else {
				status = false;
			}

		}	


		/*		String responseStatus = BofaManager.getSerialDM_Obj().bofaPowerSourceMsngrSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
			if (responseStatus == DeleteMeConstant.SUCCESS_RESPONSE) {
				String CurrentReadData = BofaManager.getSerialDM_Obj().getRxMsgQ_PwrSrc().getLastReadMessage();//""; // BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
				status = processResponse(voltageCurrentStopOutputCmdFrame, CurrentReadData);
			}*/


		ApplicationLauncher.logger.info("sendVoltageCurrentStopOutputCommand: Exit");  
		return status;
	}

	public SerialPortManagerPwrSrc_V2 getPwrSrcSpmObj() {
		return pwrSrcSpmObj;
	}
	public void setPwrSrcSpmObj(SerialPortManagerPwrSrc_V2 pwrSrcSpmObj) {
		this.pwrSrcSpmObj = pwrSrcSpmObj;
	}


	public boolean bofaSetPowerSourceMctNctMode(String mctNctMode, boolean forceSet) {
		ApplicationLauncher.logger.debug("bofaSetPowerSourceMctNctMode :Entry");
		ApplicationLauncher.logger.info("bofaSetPowerSourceMctNctMode : mctNctMode: " +mctNctMode);
		boolean status = false;
		String circuit = "";
		int address    = ConstantPowerSourceBofa.ADDRESS_SLAVE_01;

		if(mctNctMode.equals(ConstantReport.RESULT_EXECUTION_MODE_MAIN_CT)) {
			circuit = ConstantPowerSourceLscs.CMD_PWR_SRC_MAIN_CT_MODE_HDR;
		} else if(mctNctMode.equals(ConstantReport.RESULT_EXECUTION_MODE_NEUTRAL_CT)) {
			circuit = ConstantPowerSourceLscs.CMD_PWR_SRC_NEUTRAL_CT_MODE_HDR;
		}else if(mctNctMode.equals(ConstantReport.RESULT_EXECUTION_MODE_MCT_NCT_OFF)) {
			circuit = ConstantPowerSourceLscs.CMD_PWR_SRC_ALL_CT_MODE_OFF_HDR;
		}else {
			ApplicationLauncher.logger.info("bofaSetPowerSourceMctNctMode :invalid mode data: mctNctMode: "+mctNctMode);
			return status;
		}
		int retryCount = 3;
		status = false;
		while ((retryCount>0) && (!status) ){
			ApplicationLauncher.logger.debug("bofaSetPowerSourceMctNctMode : Bofa : retryCount: "+retryCount);
			status = Data_LduBofa.bofaSendSwitchCtCircuit(address, circuit);
			retryCount--;
			Sleep(300);
		}
		ApplicationLauncher.logger.debug("bofaSetPowerSourceMctNctMode : Bofa : status: "+status);
		ApplicationLauncher.logger.debug("bofaSetPowerSourceMctNctMode : Bofa : retry Exit: ");




		if(status){
			//setPowerSrcOnFlag(false);
			//setPowerSrcTurnedOnStatus(false);
			ApplicationLauncher.logger.info("***************************************");
			ApplicationLauncher.logger.info("bofaSetPowerSourceMctNctMode: Mode set success");
			ApplicationLauncher.logger.info("***************************************");
		}else{
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("bofaSetPowerSourceMctNctMode: Mode set Failed");
			FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_1002);
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
			ApplicationLauncher.logger.info("*************************************");
		}

		ApplicationLauncher.logger.debug("bofaSetPowerSourceMctNctMode: Exit");
		return status;


	}
	
	
	public String bofaPowerSourceMsngrSendCommandProcess(String payLoadInHex, int timeDelayInMilliSec, 
			String expectedDataInHex,boolean isResponseExpected,String sourceThread){
		ApplicationLauncher.logger.debug("bofaPowerSourceMsngrSendCommandProcess :Entry");
		//boolean status=false;

		String responseStatus=DeleteMeConstant.NO_RESPONSE;
		int retryCount= 5;//10//ConstantHV_Src.HV_READ_RESPONSE_RETRY_COUNT+10;//1;

		//String ExpectedError1Data = "";//ConstantPrimaryVICI_Meter.VI_CMD_ERROR_RESPONSE_HDR;//+ConstantPrimaryVICI_Meter.VI_ER_TERMINATOR;
		
		//String ExpectedError2Data="";
		
		
		
		/*powerSourceSetExpectedData(expectedDataInHex);
		powerSourceSetExpectedError1Data(ExpectedError1Data); 
		powerSourceSetExpectedError2Data(ExpectedError2Data);
		powerSourceSetRxMessageTerminator(terminatorInHex);
		
		powerSourceResetResponseFlag();*/
		
		getPwrSrcSpmObj().getRxMsgQ_PwrSrc().clearLastReadMessage();
		//powerSourceSendCommand(payLoadInHex,timeDelayInMilliSec);
		
		
		
		
		
	
		
		
		//ApplicationLauncher.logger.debug("bofaPowerSourceMsngrSendCommandProcess : expectedDataInHex: " + expectedDataInHex);
		String expectedData = "";
		if(isResponseExpected){
			expectedData = GuiUtils.hexToAsciiV2(expectedDataInHex);
		}
		
		boolean status = false;
		String rxMessageTerminator = GuiUtils.hexToAscii(ConstantPowerSourceBofa.END_BYTE);
		Map<String,Object> responseReturn = new HashMap<String,Object>();
		if(getPwrSrcSpmObj().isPwrSrcSerialStatusConnected()){
			responseReturn = getPwrSrcSpmObj().powerSourceSendCommandProcess( payLoadInHex, timeDelayInMilliSec,
					expectedData,isResponseExpected, rxMessageTerminator, sourceThread);
			status = (boolean)responseReturn.get("status");
			if(status){
				String responseData = (String)responseReturn.get("result");
				ApplicationLauncher.logger.debug("bofaPowerSourceMsngrSendCommandProcess : responseData <"+sourceThread +">: " + responseData);
				//ApplicationLauncher.logger.debug("bofaPowerSourceMsngrSendCommandProcess : responseData <"+sourceThread +">hex : " + GuiUtils.asciiToHex(responseData));
				//if(responseData.equals(GuiUtils.hexToAsciiV2(ConstantPowerSourceBofa.CMD_STOP_VOLT_CURRENT_IN_HEX))) {
				if(responseData.equals(GuiUtils.hexToAsciiV2(ConstantPowerSourceBofa.ER_CMD_STOP_VOLT_CURRENT_POSITIVE_IN_HEX))) {
					status = true;
				}else {
					status = false;
				}
	
			}	
			
			
			if(isResponseExpected){
				while (retryCount>0 && responseStatus.equals(DeleteMeConstant.NO_RESPONSE)){
					retryCount--;
					//VI_SendStartCommand(CommandVI_PayLoad,SelectedPhase);
					ApplicationLauncher.logger.debug("bofaPowerSourceMsngrSendCommandProcess :retryCount<"+sourceThread +">:" + retryCount);
					//int TimeOutInSec= 60;vjhvjgv
					//SerialDataLDU lduData = VICI_ReadDataWithErrorResponse(ExpectedData.length(),ExpectedData,ExpectedError1Data,ExpectedError2Data,TimeOutInSec);
					//ApplicationLauncher.logger.debug("bofaPowerSourceMsngrSendCommandProcess :Hit2");
					if(getPwrSrcSpmObj().getRxMsgQ_PwrSrc().IsResponseReceived()) {
						if(getPwrSrcSpmObj().getRxMsgQ_PwrSrc().isExpectedResponseReceived()){
							ApplicationLauncher.logger.debug("bofaPowerSourceMsngrSendCommandProcess: <"+sourceThread +">:Ack Response Success");
							//String CurrentLDU_Data =lduData.getLDU_ReadSerialData();
							//ApplicationLauncher.logger.info("bofaPowerSourceMsngrSendCommandProcess: VICI Received Data1:"+CurrentLDU_Data);
							//StripLDU_SerialData(lduData.getReceivedLength());
							responseStatus=DeleteMeConstant.SUCCESS_RESPONSE;
							getPwrSrcSpmObj().getRxMsgQ_PwrSrc().removeProcessedMsgFromQueue();
						}else if(getPwrSrcSpmObj().getRxMsgQ_PwrSrc().isExpectedErrorResponseReceived()){
							ApplicationLauncher.logger.debug("bofaPowerSourceMsngrSendCommandProcess <"+sourceThread +">:Ack Response Error");
							String CurrentReadData =getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage();
							//ApplicationLauncher.logger.info("bofaPowerSourceMsngrSendCommandProcess: ErrorResponse Received:"+CurrentLDU_Data);
							//ApplicationLauncher.logger.info("bofaPowerSourceMsngrSendCommandProcess: ExpectedError1Data:"+ExpectedError1Data);
							//StripLDU_SerialData(CurrentLDU_Data.length());
							
							responseStatus=CurrentReadData;
							getPwrSrcSpmObj().getRxMsgQ_PwrSrc().removeProcessedMsgFromQueue();
			
						}else if(getPwrSrcSpmObj().getRxMsgQ_PwrSrc().isUnknownResponseRecieved()){
							
							
							String CurrentReadData =getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage();
							ApplicationLauncher.logger.debug("bofaPowerSourceMsngrSendCommandProcess : isUnknownResponseRecieved : CurrentReadData<"+sourceThread +">: "+ CurrentReadData);
							responseStatus=CurrentReadData;
							getPwrSrcSpmObj().getRxMsgQ_PwrSrc().removeProcessedMsgFromQueue();
							if(responseStatus!=null) { //Gopi2
								ApplicationLauncher.logger.debug("bofaPowerSourceMsngrSendCommandProcess :Unexpected Message: dropping the message<"+sourceThread +">:"+responseStatus);
								ApplicationLauncher.logger.debug("bofaPowerSourceMsngrSendCommandProcess :Unexpected Message: dropping the message<"+sourceThread +">:"+GuiUtils.HexToString(responseStatus));
							}
							
						}
					}
					else{
						ApplicationLauncher.logger.info("bofaPowerSourceMsngrSendCommandProcess : <"+sourceThread +">: Ack No Data Received");
					}
					
					
					ApplicationLauncher.logger.debug("bofaPowerSourceMsngrSendCommandProcess : <"+sourceThread +">test1: ");
					
					if ( ( !payLoadInHex.equals(ConstantPowerSourceBofa.CMD_STOP_VOLT_CURRENT_IN_HEX)) && 
						     ( !payLoadInHex.equals(ConstantPowerSourceBofa.CMD_STOP_CURRENT_IN_HEX)) ){
							if(ProjectExecutionController.getUserAbortedFlag()){
								retryCount = 0;
								responseStatus = DeleteMeConstant.ERROR_RESPONSE;
								ApplicationLauncher.logger.info("bofaPowerSourceMsngrSendCommandProcess V2 <"+sourceThread +">: user aborted - detected");
							}
						}else if(!getPwrSrcSpmObj().isPwrSrcSerialStatusConnected()){
							retryCount = 0;
							responseStatus = DeleteMeConstant.ERROR_RESPONSE;
							ApplicationLauncher.logger.debug("bofaPowerSourceMsngrSendCommandProcess V2 <"+sourceThread +">: serial port - disconnected");
						
						}
				}
			}else{
				responseStatus=DeleteMeConstant.NO_RESPONSE;
				Sleep(200);
			}
		}else{
			ApplicationLauncher.logger.debug("bofaPowerSourceMsngrSendCommandProcess V2 <"+sourceThread +">: serial port not connected or disconnected");
			
		}
		//ApplicationLauncher.logger.debug("bofaPowerSourceMsngrSendCommandProcess : test2: ");
		if(getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage()==null) {
			ApplicationLauncher.logger.info("bofaPowerSourceMsngrSendCommandProcess<"+sourceThread +">: last read null");
		}else {
			ApplicationLauncher.logger.info("bofaPowerSourceMsngrSendCommandProcess: last read<"+sourceThread +">:"+getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage());
		}
		ApplicationLauncher.logger.debug("bofaPowerSourceMsngrSendCommandProcess <"+sourceThread +">:Exit");
		return responseStatus;
	}
	
	
	
	
	public String bofaPowerSourceMsngrSendCommandProcessWithQueue(String payLoadInHex, int timeDelayInMilliSec, 
			String expectedDataInHex,boolean isResponseExpected,String sourceThread){
		ApplicationLauncher.logger.debug("bofaPowerSourceMsngrSendCommandProcessWithQueue :Entry");
		//boolean status=false;

		String responseStatus=DeleteMeConstant.NO_RESPONSE;
		
		int retryCount= 5;//10//ConstantHV_Src.HV_READ_RESPONSE_RETRY_COUNT+10;//1;

		//String ExpectedError1Data = "";//ConstantPrimaryVICI_Meter.VI_CMD_ERROR_RESPONSE_HDR;//+ConstantPrimaryVICI_Meter.VI_ER_TERMINATOR;
		
		//String ExpectedError2Data="";
		
		
		
		/*powerSourceSetExpectedData(expectedDataInHex);
		powerSourceSetExpectedError1Data(ExpectedError1Data); 
		powerSourceSetExpectedError2Data(ExpectedError2Data);
		powerSourceSetRxMessageTerminator(terminatorInHex);
		
		powerSourceResetResponseFlag();*/
		
		getPwrSrcSpmObj().getRxMsgQ_PwrSrc().clearLastReadMessage();
		//powerSourceSendCommand(payLoadInHex,timeDelayInMilliSec);
		
		
		
		
		
	
		
		
		//ApplicationLauncher.logger.debug("bofaPowerSourceMsngrSendCommandProcessWithQueue : expectedDataInHex: " + expectedDataInHex);
		String expectedData = "";
		if(isResponseExpected){
			expectedData = GuiUtils.hexToAsciiV2(expectedDataInHex);
		}
		
		boolean status = false;
		String rxMessageTerminator = GuiUtils.hexToAscii(ConstantPowerSourceBofa.END_BYTE);
		Map<String,Object> responseReturn = new HashMap<String,Object>();
		if(getPwrSrcSpmObj().isPwrSrcSerialStatusConnected()){
			responseReturn = getPwrSrcSpmObj().powerSourceSendCommandProcess( payLoadInHex, timeDelayInMilliSec,
					expectedData,isResponseExpected, rxMessageTerminator, sourceThread);
			status = (boolean)responseReturn.get("status");
			if(status){
				String responseData = (String)responseReturn.get("result");
				ApplicationLauncher.logger.debug("bofaPowerSourceMsngrSendCommandProcessWithQueue : responseData <"+sourceThread +">: " + responseData);
				//ApplicationLauncher.logger.debug("bofaPowerSourceMsngrSendCommandProcessWithQueue : responseData <"+sourceThread +">hex : " + GuiUtils.asciiToHex(responseData));
				//if(responseData.equals(GuiUtils.hexToAsciiV2(ConstantPowerSourceBofa.CMD_STOP_VOLT_CURRENT_IN_HEX))) {
				if(responseData.equals(GuiUtils.hexToAsciiV2(ConstantPowerSourceBofa.ER_CMD_STOP_VOLT_CURRENT_POSITIVE_IN_HEX))) {
					status = true;
				}else {
					status = false;
				}
	
			}	
			
			
			if(isResponseExpected){
				while (retryCount>0 && responseStatus.equals(DeleteMeConstant.NO_RESPONSE)){
					retryCount--;
					//VI_SendStartCommand(CommandVI_PayLoad,SelectedPhase);
					ApplicationLauncher.logger.debug("bofaPowerSourceMsngrSendCommandProcessWithQueue :retryCount<"+sourceThread +">:" + retryCount);
					//int TimeOutInSec= 60;vjhvjgv
					//SerialDataLDU lduData = VICI_ReadDataWithErrorResponse(ExpectedData.length(),ExpectedData,ExpectedError1Data,ExpectedError2Data,TimeOutInSec);
					//ApplicationLauncher.logger.debug("bofaPowerSourceMsngrSendCommandProcessWithQueue :Hit2");
					if(getPwrSrcSpmObj().getRxMsgQ_PwrSrc().IsResponseReceived()) {
						if(getPwrSrcSpmObj().getRxMsgQ_PwrSrc().isExpectedResponseReceived()){
							ApplicationLauncher.logger.debug("bofaPowerSourceMsngrSendCommandProcessWithQueue: <"+sourceThread +">:Ack Response Success");
							//String CurrentLDU_Data =lduData.getLDU_ReadSerialData();
							//ApplicationLauncher.logger.info("bofaPowerSourceMsngrSendCommandProcessWithQueue: VICI Received Data1:"+CurrentLDU_Data);
							//StripLDU_SerialData(lduData.getReceivedLength());
							responseStatus=DeleteMeConstant.SUCCESS_RESPONSE;
							getPwrSrcSpmObj().getRxMsgQ_PwrSrc().removeProcessedMsgFromQueue();
						}else if(getPwrSrcSpmObj().getRxMsgQ_PwrSrc().isExpectedErrorResponseReceived()){
							ApplicationLauncher.logger.debug("bofaPowerSourceMsngrSendCommandProcessWithQueue <"+sourceThread +">:Ack Response Error");
							String CurrentReadData =getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage();
							//ApplicationLauncher.logger.info("bofaPowerSourceMsngrSendCommandProcessWithQueue: ErrorResponse Received:"+CurrentLDU_Data);
							//ApplicationLauncher.logger.info("bofaPowerSourceMsngrSendCommandProcessWithQueue: ExpectedError1Data:"+ExpectedError1Data);
							//StripLDU_SerialData(CurrentLDU_Data.length());
							
							responseStatus=CurrentReadData;
							getPwrSrcSpmObj().getRxMsgQ_PwrSrc().removeProcessedMsgFromQueue();
			
						}else if(getPwrSrcSpmObj().getRxMsgQ_PwrSrc().isUnknownResponseRecieved()){
							
							
							String CurrentReadData =getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage();
							ApplicationLauncher.logger.debug("bofaPowerSourceMsngrSendCommandProcessWithQueue : isUnknownResponseRecieved : CurrentReadData<"+sourceThread +">: "+ CurrentReadData);
							responseStatus=CurrentReadData;
							getPwrSrcSpmObj().getRxMsgQ_PwrSrc().removeProcessedMsgFromQueue();
							if(responseStatus!=null) { //Gopi2
								ApplicationLauncher.logger.debug("bofaPowerSourceMsngrSendCommandProcessWithQueue :Unexpected Message: dropping the message<"+sourceThread +">:"+responseStatus);
								ApplicationLauncher.logger.debug("bofaPowerSourceMsngrSendCommandProcessWithQueue :Unexpected Message: dropping the message<"+sourceThread +">:"+GuiUtils.HexToString(responseStatus));
							}
							
						}
					}
					else{
						ApplicationLauncher.logger.info("bofaPowerSourceMsngrSendCommandProcessWithQueue : <"+sourceThread +">: Ack No Data Received");
					}
					
					
					ApplicationLauncher.logger.debug("bofaPowerSourceMsngrSendCommandProcessWithQueue : <"+sourceThread +">test1: ");
					
					if ( ( !payLoadInHex.equals(ConstantPowerSourceBofa.CMD_STOP_VOLT_CURRENT_IN_HEX)) && 
						     ( !payLoadInHex.equals(ConstantPowerSourceBofa.CMD_STOP_CURRENT_IN_HEX)) ){
							if(ProjectExecutionController.getUserAbortedFlag()){
								retryCount = 0;
								responseStatus = DeleteMeConstant.ERROR_RESPONSE;
								ApplicationLauncher.logger.info("bofaPowerSourceMsngrSendCommandProcessWithQueue V2 <"+sourceThread +">: user aborted - detected");
							}
						}else if(!getPwrSrcSpmObj().isPwrSrcSerialStatusConnected()){
							retryCount = 0;
							responseStatus = DeleteMeConstant.ERROR_RESPONSE;
							ApplicationLauncher.logger.debug("bofaPowerSourceMsngrSendCommandProcessWithQueue V2 <"+sourceThread +">: serial port - disconnected");
						
						}
				}
			}else{
				responseStatus=DeleteMeConstant.NO_RESPONSE;
				Sleep(200);
			}
		}else{
			ApplicationLauncher.logger.debug("bofaPowerSourceMsngrSendCommandProcessWithQueue V2 <"+sourceThread +">: serial port not connected or disconnected");
			
		}
		//ApplicationLauncher.logger.debug("bofaPowerSourceMsngrSendCommandProcessWithQueue : test2: ");
		if(getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage()==null) {
			ApplicationLauncher.logger.info("bofaPowerSourceMsngrSendCommandProcessWithQueue<"+sourceThread +">: last read null");
		}else {
			ApplicationLauncher.logger.info("bofaPowerSourceMsngrSendCommandProcessWithQueue: last read<"+sourceThread +">:"+getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage());
		}
		ApplicationLauncher.logger.debug("bofaPowerSourceMsngrSendCommandProcessWithQueue <"+sourceThread +">:Exit");
		return responseStatus;
	}




	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("Sleep2 :InterruptedException:"+ e.getMessage());
		}

	}
}
