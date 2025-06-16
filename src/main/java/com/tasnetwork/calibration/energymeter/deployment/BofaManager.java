package com.tasnetwork.calibration.energymeter.deployment;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantPowerSourceBofa;
import com.tasnetwork.calibration.energymeter.constant.DeleteMeConstant;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.device.Communicator;
import com.tasnetwork.calibration.energymeter.device.Data_LduBofa;
import com.tasnetwork.calibration.energymeter.device.Data_PowerSourceBofa;
import com.tasnetwork.calibration.energymeter.device.Data_RefStdBofa;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.messenger.BofaRequestProcessor;
import com.tasnetwork.calibration.energymeter.messenger.PowerSourceBofaMessenger;
import com.tasnetwork.calibration.energymeter.serial.portmanager.SerialPortManagerPwrSrc;
import com.tasnetwork.calibration.energymeter.serial.portmanagerV2.SerialPortManagerPwrSrc_V2;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;

public class BofaManager {
	
	public static Semaphore comPortSemaphore = new Semaphore(0,true);  
	
	//static PowerSourceBofaMessenger powerSourceBofaMessenger = new PowerSourceBofaMessenger();
	
	static {
	    //comPortSemaphore.release(); // Release the semaphore to make it available initially
	    //ApplicationLauncher.logger.debug("BofaManager : init : Semaphore :released");
	    //ApplicationLauncher.logger.debug("BofaManager : init : Semaphore : availablePermits:" + comPortSemaphore.availablePermits());
	}
	//public static boolean bInitOccured = false;
	
/*	public BofaManager(){
		if(!bInitOccured){
			try{
				InitSerialCommPort();

			}catch (UnsatisfiedLinkError e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("SerialDataManager: exception:" + e.getMessage());
				ApplicationLauncher.InformUser("SerialPort search failed","Kindly ensure <rxtxSerial.dll> file is placed under path <C:\\Program Files\\Java\\jre1.8.0_161\\bin\\> folder. \n Additional Info:\n\n" + e.getMessage(),AlertType.ERROR);
			}
			bInitOccured= true;
			//SetBNC_OutputPortData();
		}

	}*/
	
	public static SerialPortManagerPwrSrc serialDM_Obj = new SerialPortManagerPwrSrc();
	public static SerialPortManagerPwrSrc_V2 serialDmV2_Obj = new SerialPortManagerPwrSrc_V2();
	static DeviceDataManagerController displayDataObj =  new DeviceDataManagerController();
	static PowerSourceBofaMessenger pwrSrcBofaMessenger = new PowerSourceBofaMessenger();
	
	public static Communicator commPowerSrc= null;
	
	//public SerialTxMessageQ txMsgQ_PwrSrc = new SerialTxMessageQ(commPowerSrc);
	
//	public static SerialRxMessageQ rxMsgQ_PwrSrc = new SerialRxMessageQ(commPowerSrc);
//	public SerialRxPhysical rxPhysical_PwrSrc = new SerialRxPhysical(commPowerSrc,rxMsgQ_PwrSrc,false,"PwrSrc",DeleteMeConstant.HV_ER_TERMINATOR,"");
	
	//public static boolean powerSourceSerialStatusConnected = false;
	// message sending
	// process received message
	// call function of Data_PowerSourceBofa, Data_RefStdBofa
	// update UI in the project execution controller ProjectExecutionController
	// DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdate_R_Phase(refStdDataR_Phase);	// pending

//=======================================================================================================================================================================
	 
	
/*	public static void scanForSerialCommPort(){

		//commPowerSrc.searchForPorts();  
		commPowerSrc.searchForPorts();  

	}*/
	
/*	public void InitSerialCommPort(){
		createObjects();
		commPowerSrc.searchForPorts();  

	}*/
	
//	private void createObjects()
//	{
//		ApplicationLauncher.logger.debug("BofaManager: createObjects :Entry");
//		commPowerSrc = new Communicator(ConstantMtePowerSource.SERIAL_PORT_POWER_SOURCE);
		//commRefStandard = new Communicator(ConstantRadiantRefStd.SERIAL_PORT_REF_STD);
		/*commLDU = new Communicator(ConstantCcubeLDU.SERIAL_PORT_LDU);
		try{
			//if(ProcalFeatureEnable.ICT_INTERFACE_ENABLED){ // commented due to failure in communication, during procal app boot ProCalCustomerConfiguration inits are not effective
			commICT = new Communicator(Constant_ICT.SERIAL_PORT_ICT);
			if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_FEATURE_ENABLED){
				commHarmonicsSrc = new Communicator(ConstantLscsHarmonicsSourceSlave.SERIAL_PORT_HARMONICS_SLAVE);
				ApplicationLauncher.logger.debug("createObjects :Hit1");
			}
			//}
		}catch(Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("createObjects Exception :"+E.getMessage());
		}
*/	//	ApplicationLauncher.logger.debug("BofaManager: createObjects :Exit");
//	}
	
	public static boolean enableSerialPortAndMonitor(){
		ApplicationLauncher.logger.debug("enableSerialPortAndMonitor Invoked:");
		boolean status = false;
		if(displayDataObj.powerSourcePortAccessible()){
			//SerialDM_Obj.ClearSerialDataInLDU_Ports();
			serialDM_Obj.startSerialRxPhysical_PowerSource();
			serialDM_Obj.enableSerialRxPhysical_PowerSourceMonitor();
			status = true;
		}
		return status;
	}
	
	public static boolean enableSerialPortAndMonitorV2(){
		ApplicationLauncher.logger.debug("enableSerialPortAndMonitorV2 Invoked:");
		boolean status = false;
		//if(displayDataObj.powerSourcePortAccessible()){
		if(displayDataObj.pwrSrcPortAccessible_V2()){
			//SerialDM_Obj.ClearSerialDataInLDU_Ports();
			//serialDM_Obj.startSerialRxPhysical_PowerSource();
			//serialDM_Obj.enableSerialRxPhysical_PowerSourceMonitor();
			displayDataObj.pwrSrcEnableSerialMonitoring_V2();
			setSerialDmV2_Obj(displayDataObj.getSerialPortManagerPwrSrc_V2());
			status = true;
		}
		return status;
	}
/*	
	public boolean powerSourceComInit(String InputComm,String BaudRate) {
		ApplicationLauncher.logger.debug("powerSourceComInit Invoked:");
		ApplicationLauncher.logger.debug("CommInput: " + InputComm);
		ApplicationLauncher.logger.debug("BaudRate: " + BaudRate);
		try {
			powerSourceSerialStatusConnected = powerSourceCommSetting(InputComm,BaudRate);
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("powerSourceComInit :UnsupportedEncodingException:"+ e.getMessage());
		}


		return powerSourceSerialStatusConnected;
	}*/
	
/*	public boolean SetSerialComm(Communicator SerialPortObj, String SerialPort_ID, Integer BaudRate,Boolean ReadHexFormat){
		ApplicationLauncher.logger.debug("SetSerialComm :Entry");
		boolean status=false;
		try {
			ApplicationLauncher.logger.debug("SetSerialComm : test1");
			SerialPortObj.connect(SerialPort_ID);
			ApplicationLauncher.logger.debug("SetSerialComm : test2");
			if(SerialPortObj.getDeviceConnected()==true){
				ApplicationLauncher.logger.debug("SetSerialComm : test3");
				if (SerialPortObj.initIOStream() == true){
					ApplicationLauncher.logger.debug("SetSerialComm : test4");
					SerialPortObj.SerialPortConfig(BaudRate);
					ApplicationLauncher.logger.debug("SetSerialComm : test5");
					//SerialPortObj.setPortDeviceMapping(SerialPort_ID);
					ApplicationLauncher.logger.info("PortDeviceMapping:"+ SerialPortObj.getPortDeviceMapping()+":"+ReadHexFormat);
					ApplicationLauncher.logger.debug("SetSerialComm : test6");
					SerialPortObj.initListener();
					ApplicationLauncher.logger.debug("SetSerialComm : test7");
					SerialPortObj.setDataReadFormatInHex(ReadHexFormat);
					ApplicationLauncher.logger.debug("SetSerialComm : test8");
					status = true;
					return status;
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("SetSerialComm: Exception: " + e.getMessage());
			

		}
		
		return status;
	}*/
	
/*	public boolean powerSourceCommSetting(String CommInput, String BaudRate) throws UnsupportedEncodingException{
		ApplicationLauncher.logger.debug("powerSourceCommSetting :Entry");
		//InitSerialCommPort();
		ApplicationLauncher.logger.debug("commPowerSrc: " +commPowerSrc );
		ApplicationLauncher.logger.debug("CommInput: " + CommInput);
		ApplicationLauncher.logger.debug("BaudRate: " + BaudRate);
		boolean status = SetSerialComm(commPowerSrc,CommInput,Integer.valueOf(BaudRate),true);
		if (status){
			commPowerSrc.SetFlowControlMode();
		} else {

			ApplicationLauncher.logger.info("powerSourceCommSetting:"+CommInput+" access failed");
		}
		return status;


	} */
	
//	 public static String powerSourceSendCommandProcess(String payLoadInHex, int timeDelayInMilliSec, String expectedDataInHex,boolean isResponseExpected){
//			ApplicationLauncher.logger.debug("Bofa: powerSourceSendCommandProcess :Entry");
//			String responseStatus=DeleteMeConstant.NO_RESPONSE;
/*			int retryCount= 5;

			String ExpectedError1Data = "";
			
			String ExpectedError2Data="";
			String terminatorInHex = ConstantBofaPowerSource.END_BYTE;//GuiUtils.StringToHex("K");
			SerialDM_Obj.powerSourceSetExpectedData(expectedDataInHex);
			SerialDM_Obj.powerSourceSetExpectedError1Data(ExpectedError1Data);
			SerialDM_Obj.powerSourceSetExpectedError2Data(ExpectedError2Data);
			SerialDM_Obj.powerSourceSetRxMessageTerminator(terminatorInHex);
			
			SerialDM_Obj.powerSourceResetResponseFlag();

			SerialDM_Obj.powerSourceSendCommand(payLoadInHex,timeDelayInMilliSec);
			if(isResponseExpected){
				while (retryCount>0 && responseStatus.equals(DeleteMeConstant.NO_RESPONSE)){
					retryCount--;

					if(rxMsgQ_PwrSrc.IsResponseReceived()) {
						if(rxMsgQ_PwrSrc.isExpectedResponseReceived()){
							ApplicationLauncher.logger.debug("Bofa: powerSourceSendCommandProcess :Ack Response Success");
							responseStatus = DeleteMeConstant.SUCCESS_RESPONSE;
							rxMsgQ_PwrSrc.removeProcessedMsgFromQueue();
						}else if(rxMsgQ_PwrSrc.isExpectedErrorResponseReceived()){
							ApplicationLauncher.logger.debug("Bofa: powerSourceSendCommandProcess :Ack Response Error");
							String CurrentReadData = rxMsgQ_PwrSrc.getLastReadMessage();
							responseStatus = CurrentReadData;
							rxMsgQ_PwrSrc.removeProcessedMsgFromQueue();
			
						}else if(rxMsgQ_PwrSrc.isUnknownResponseRecieved()){
							
							
							String CurrentReadData =rxMsgQ_PwrSrc.getLastReadMessage();
							responseStatus=CurrentReadData;
							rxMsgQ_PwrSrc.removeProcessedMsgFromQueue();
							if(responseStatus!=null) { //Gopi2
								ApplicationLauncher.logger.debug("Bofa: powerSourceSendCommandProcess :Unexpected Message: dropping the message:"+responseStatus);
								ApplicationLauncher.logger.debug("Bofa: powerSourceSendCommandProcess :Unexpected Message: dropping the message:"+GuiUtils.HexToString(responseStatus));
							}						
						}
					}
					else{
						ApplicationLauncher.logger.info("Bofa: powerSourceSendCommandProcess: Ack No Data Received");
					}
				}
			}
			if(rxMsgQ_PwrSrc.getLastReadMessage()==null) {
				ApplicationLauncher.logger.info("Bofa: powerSourceSendCommandProcess: last read null");
			}else {
				ApplicationLauncher.logger.info("Bofa: powerSourceSendCommandProcess: last read:"+rxMsgQ_PwrSrc.getLastReadMessage());
			}
	*/ // 		ApplicationLauncher.logger.debug("Bofa: powerSourceSendCommandProcess :Exit");
	//		return responseStatus;
	//	}
	
	 public static void disconnectPwrSrc(){
		 
		 if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
			 displayDataObj.pwrSrcDisconnectPort_V2();
		 }else {
			 serialDM_Obj.disconnectPowerSource();
		 }
	 }
	
	
//======================================================================================================================================================================================	
	
	 public void powerSourceSetExpectedData(String ExpectedResponse){
			//ApplicationLauncher.logger.debug("HVCI_SetExpectedData :Entry");
			Communicator SerialPortObj =commPowerSrc;

			
			SerialPortObj.setExpectedLength(ExpectedResponse.length());
			SerialPortObj.setExpectedResult(ExpectedResponse);
			//SerialPortObj.setExpectedError1Result(ExpectedError1Data);
			//SerialPortObj.setExpectedError2Result(ExpectedError2Data);
			//ApplicationLauncher.logger.debug("HVCI_SetExpectedData: setExpectedResult:"+SerialPortObj.getExpectedResult());
			//ApplicationLauncher.logger.debug("HVCI_SetExpectedData: setExpectedLength:"+SerialPortObj.getExpectedLength());

			//SerialDataMsgHV_Manager serialDataMsgManager = new SerialDataMsgHV_Manager(SerialPortObj);
			//serialDataManager.SerialResponseTimerStart(30);
			//SerialPortObj = null;//garbagecollector
			//return serialDataMsgManager;
		}
		
		public void powerSourceSetExpectedError1Data(String ExpectedError1Response){
			//ApplicationLauncher.logger.debug("HVCI_SetExpectedError1Data :Entry");
			Communicator SerialPortObj =commPowerSrc;
			
			//SerialPortObj.setExpectedLength(ExpectedError1Response.length());
			SerialPortObj.setExpectedError1Result(ExpectedError1Response);
			//SerialPortObj.setExpectedError1Result(ExpectedError1Data);
			//SerialPortObj.setExpectedError2Result(ExpectedError2Data);
			//ApplicationLauncher.logger.debug("HVCI_SetExpectedError1Data: getExpectedError1Result:"+SerialPortObj.getExpectedError1Result());
			//ApplicationLauncher.logger.debug("HVCI_SetExpectedError1Data: setExpectedLength:"+SerialPortObj.getExpectedLength());

			//SerialDataMsgHV_Manager serialDataMsgManager = new SerialDataMsgHV_Manager(SerialPortObj);
			//serialDataManager.SerialResponseTimerStart(30);
			//SerialPortObj = null;//garbagecollector
			//return serialDataMsgManager;
		}
		
		public void powerSourceSetExpectedError2Data(String ExpectedError2Response){
			//ApplicationLauncher.logger.debug("HVCI_SetExpectedError2Data :Entry");
			Communicator SerialPortObj =commPowerSrc;
			
			//SerialPortObj.setExpectedLength(ExpectedError1Response.length());
			SerialPortObj.setExpectedError2Result(ExpectedError2Response);
			//SerialPortObj.setExpectedError1Result(ExpectedError1Data);
			//SerialPortObj.setExpectedError2Result(ExpectedError2Data);
			//ApplicationLauncher.logger.debug("HVCI_SetExpectedError2Data: getExpectedError2Result:"+SerialPortObj.getExpectedError2Result());
			//ApplicationLauncher.logger.debug("HVCI_SetExpectedError1Data: setExpectedLength:"+SerialPortObj.getExpectedLength());

			//SerialDataMsgHV_Manager serialDataMsgManager = new SerialDataMsgHV_Manager(SerialPortObj);
			//serialDataManager.SerialResponseTimerStart(30);
			//SerialPortObj = null;//garbagecollector
			//return serialDataMsgManager;
		}
	
	
/*		private void powerSourceSetRxMessageTerminator(String terminatorInHex) {
			
			
			rxPhysical_PwrSrc.setMessageTerminatorInHex(terminatorInHex);
		}*/
	
	
		public void powerSourceSendCommand(String payLoadInHex,int timeDelayInMilliSec){

			//String Data = GUIUtils.StringToHex(ConstantPrimaryVICI_Meter.VI_CMD_START)+GUIUtils.StringToHex(SelectedPhase)+CommandVI_PayLoad+GUIUtils.StringToHex(ConstantPrimaryVICI_Meter.VI_CMD_TERMINATOR);
			//String Data = ConstantPrimaryVICI_Meter.VI_CMD_START+GUIUtils.StringToHex(SelectedPhase)+CommandVI_PayLoad+ConstantPrimaryVICI_Meter.VI_CMD_TERMINATOR;
			//String Data = "";//SerialMessageHV_Src.getCmd_HV_Start(CommandVI_PayLoad,SelectedPhase);
			//WriteToSerialCommLDU(Data);
			//writeHexToSerialPowerSource(payLoadInHex,timeDelayInMilliSec);

		}
		
		
/*		public void writeHexToSerialPowerSource(String Data,int timeDelayInMilliSec){
			ApplicationLauncher.logger.debug("writeHexToSerialPowerSource :DataHex:"+Data);
			try{
				if(ProcalFeatureEnable.MAINTENANCE_MODE_ENABLED) {
					if(MaintenanceModeExecController.isSerialDisplayProcess()) {				
						MaintenanceModeExecController.serialDataDisplayUpdate("hvc-Tx-Hex:<"+Data+">");
						MaintenanceModeExecController.serialDataDisplayUpdate("hvc-Tx-Str:<"+GuiUtils.HexToString(Data)+">");
					}
				}
				
				if(timeDelayInMilliSec!=0){
					String eachDataInHex = "";
					for(int i = 0; i < (Data.length()-1); i+=2){
						//ApplicationLauncher.logger.debug("lscsLDU_SendCeigSettingMethod : index :" + i +": " + String.valueOf(Data.charAt(i)));
						//SerialPortObj.writeStringMsgToPort(String.valueOf(Data.charAt(i)));
						eachDataInHex = Data.substring(i,i+2);
						//ApplicationLauncher.logger.debug("writeHexToSerialPowerSource :eachDataInHex:"+eachDataInHex);
						commPowerSrc.writeStringMsgToPortInHex(eachDataInHex);
						Sleep(timeDelayInMilliSec);
					}
				}else{
					commPowerSrc.writeStringMsgToPortInHex(Data);
				}
			}catch(Exception e){
				ApplicationLauncher.logger.error("writeHexToSerialPowerSource :Exception :" + e.getMessage());
			}
			//Sleep(200);

		}*/
	
//====================================================================================================================================================	
/*		public void powerSourceResetResponseFlag() {
			rxPhysical_PwrSrc.ClearRxPhysicalData();
			rxPhysical_PwrSrc.clearLastDisplayedReadRxPhysicalData();
			rxMsgQ_PwrSrc.setExpectedResponseRecieved(false);
			rxMsgQ_PwrSrc.setErrorResponseRecieved(false);
			rxMsgQ_PwrSrc.setUnknownResponseRecieved(false);
			rxMsgQ_PwrSrc.setResponseRecieved(false);
			rxMsgQ_PwrSrc.clearMsgQueue();
			
			//rxPhysical_HVCI.ClearRxPhysicalData();

		}*/
		public static void Sleep(int timeInMsec) {

			try {
				Thread.sleep(timeInMsec);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("Sleep2 :InterruptedException:"+ e.getMessage());
			}

		}
	
	//==========================================================================================================

		public static void  testBofaCommands(){

			//Ref.Std:
			Data_RefStdBofa.frameActualOutputValueReadingCommand();
			Data_RefStdBofa.frameReadTheConstantOfLiveReferenceMeter();
			Data_RefStdBofa.frameSwitchFromPulseOfReferenceMeterToPulseOfReferenceClock();
			Data_RefStdBofa.frameFrequencyReadingCommand();

			//Power Source:
			DeviceDataManagerController.setParameterSettingCmdVariablesBofaSource();
			Data_PowerSourceBofa.frameParameterSettingCommand();
			Data_PowerSourceBofa.frameVoltageCurrentEnableOutputCommand();
			Data_PowerSourceBofa.frameVoltageCurrentStopOutputCommand();
			Data_PowerSourceBofa.frameCurrentStoppingCommand();

            //LDU:
			int address = 1 ;
			Data_RefStdBofa.setLiveRefMeterConstant(32000000) ;
					
			//Data_LduBofa.frameTestTurnsAndPulsesCmd(address);
			Data_LduBofa.frameReadErrorsCmd(address);
			Data_LduBofa.framePrepareSearchMarkCmd(address);
			Data_LduBofa.frameBeginSearchMarkCmd(address);
			Data_LduBofa.frameQueryMarkSearchResultCmd(address);
			Data_LduBofa.frameMarkSearchEndCmd(address);
			Data_LduBofa.frameQueryEnablePulseCmd(address);
			Data_LduBofa.frameStartTestEndCmd(address);
			Data_LduBofa.frameStateQueryCmd(address);
			Data_LduBofa.frameDialTestStateEntryCmd(address);
			Data_LduBofa.frameReadDialPulsesCmd(address);
			Data_LduBofa.frameDialTestStateExitCmd(address);
			Data_LduBofa.frameRelay485ConnectOrDisconnectCmd(address);
			Data_LduBofa.framePulseSwitchCmd(address);
			Data_LduBofa.frameStopRefreshErrorsCmd(address);
			Data_LduBofa.frameReadRefMeterPulseNumCmd(address);
			Data_LduBofa.framePulseInputChannelSelectCmd(address);
			Data_LduBofa.frameRelayPositionControlCmd(address);
			Data_LduBofa.frameDisplayDayErrorCmd(address);
			Data_LduBofa.frameTimeSwitchingErrorTestEntryCmd(address);
			Data_LduBofa.frameReadSwitchingTimeCmd(address);
			Data_LduBofa.frameErrorTesterCheckResetCmd(address);
			Data_LduBofa.frameDoubleCircuitSwitchCmd(address, ConstantPowerSourceBofa.CIRCUIT_1_ID);

			
			//String response = "019206323030303830303030303030303030303035303030383030303030303030303030303030303030303030303030303330313039303030300030303030302B30303531363739303030303030303030303030303030302D30303835363837303030303030303030303030303030302B303035313637392D303038353638372B30313030303537F117";
			String response = "01920632303030383030303030303030303030303530303038303030303030303030303030303030303030303030303030"
					+ "3330313039"
					//+ "0000000000"
					+ "303030300030303030302B30303531363739303030303030303030303030303030302D30303835363837303030303030303030303030303030302B303035313637392D303038353638372B30313030303537F117";

			Data_RefStdBofa.parseActualOutputValueResponse(response);
			
		}
		
		public static boolean setBofaPowerSourceOff(){
			boolean status = false;
			if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
				status = Data_PowerSourceBofa.sendVoltageCurrentStopOutputCmdV2();
			}else{
				status = Data_PowerSourceBofa.sendVoltageCurrentStopOutputCommand();
			}
			return status ;
		}

		
//===============================		
		
		
		
	public static Map<String,Object> sendDataToBofaAfterSemaPhoreAcquired(String sourceThread, String payLoadInHex,int timeDelayInMilliSec,
				String expectedDataInHex,boolean isResponseExpected, boolean forceExecute){
		Map<String,Object> responseReturn = new HashMap<String,Object>();
		if(ProcalFeatureEnable.BOFA_QUEUE_MESSENGER) {
			responseReturn = sendDataToBofaWithQueue( sourceThread,  payLoadInHex, timeDelayInMilliSec,
					 expectedDataInHex, isResponseExpected,  forceExecute);
		}else {
			responseReturn = sendDataToBofaAfterSemaPhoreAcquiredV1( sourceThread,  payLoadInHex, timeDelayInMilliSec,
					 expectedDataInHex, isResponseExpected,  forceExecute);
		}
		
		return responseReturn;
	}
		
	public static Map<String,Object> sendDataToBofaAfterSemaPhoreAcquiredV1(String sourceThread, String payLoadInHex,int timeDelayInMilliSec,
				String expectedDataInHex,boolean isResponseExpected, boolean forceExecute){
			
			ApplicationLauncher.logger.info("sendDataToBofaAfterSemaPhoreAcquired: <"+sourceThread +">: Entry");  
			sourceThread = sourceThread +"-" + String.valueOf(Instant.now().toEpochMilli());
			boolean status = false;
			Map<String,Object> responseReturn = new HashMap<String,Object>();
			//String payLoadInHex = readTheConstantOfLiveReferenceMeterCmdFrame;//GuiUtils.StringToHex(readTheConstantOfLiveReferenceMeterCmdFrame);
			//ApplicationLauncher.logger.info("sendDataToBofaAfterSemaPhoreAcquired :  <"+sourceThread +">: payLoadInHex: " + payLoadInHex  );

			//int timeDelayInMilliSec = 0;
			//String expectedDataInHex = ConstantBofaPowerSource.ER_STARTS_WITH;
			//boolean isResponseExpected = true;
			String responseData = "";
			//===============================		
			try {
				ApplicationLauncher.logger.debug("sendDataToBofaAfterSemaPhoreAcquired: comPortSemaphore: <"+sourceThread +">:acquiring...");		
				ApplicationLauncher.logger.debug("sendDataToBofaAfterSemaPhoreAcquired: comPortSemaphore: <"+sourceThread +">: availablePermits :" + BofaManager.comPortSemaphore.availablePermits());
				
				//BofaManager.comPortSemaphore.acquire(); //acquire() Try to Acquire the semaphore
				//BofaManager.comPortSemaphore.acquireUninterruptibly();
				String responseStatus = "";//DeleteMeConstant.NO_RESPONSE;//"";
				int retryCount = 5; 
				while( (retryCount>0) && (!status) ){
					ApplicationLauncher.logger.debug("sendDataToBofaAfterSemaPhoreAcquired: comPortSemaphore: <"+sourceThread +">: retryCount :" + retryCount);
					
					if((!ProjectExecutionController.getUserAbortedFlag()) || (forceExecute) ){
						if(BofaManager.comPortSemaphore.tryAcquire(1,1, TimeUnit.SECONDS)){
							//if(BofaManager.comPortSemaphore.tryAcquire(1,5, TimeUnit.SECONDS)){
							ApplicationLauncher.logger.debug("sendDataToBofaAfterSemaPhoreAcquired: comPortSemaphore: <"+sourceThread +">:acquired");	
	
							if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
								//responseStatus = getPowerSourceBofaMessenger().getPwrSrcSpmObj().bofaPowerSourceSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
	
								responseStatus = pwrSrcBofaMessenger.bofaPowerSourceMsngrSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected,sourceThread);
	
							}else {
	
								responseStatus = BofaManager.getSerialDM_Obj().bofaPowerSourceSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
							}
							//if (responseStatus == DeleteMeConstant.SUCCESS_RESPONSE) {
							if (responseStatus.equals(DeleteMeConstant.SUCCESS_RESPONSE)) {
								if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
									responseData = pwrSrcBofaMessenger.getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage();
									responseData = GuiUtils.asciiToHex(responseData);
									//ApplicationLauncher.logger.debug("sendDataToBofaAfterSemaPhoreAcquired: <"+sourceThread +">: responseData: " + responseData);
								}else{
									responseData = BofaManager.getSerialDM_Obj().getRxMsgQ_PwrSrc().getLastReadMessage();//BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
								}
								status = true;
								//status = processResponse(readTheConstantOfLiveReferenceMeterCmdFrame, CurrentReadData);
							}else {
								if(!isResponseExpected){
									if(responseStatus.equals(DeleteMeConstant.NO_RESPONSE)){
										ApplicationLauncher.logger.info("sendDataToBofaAfterSemaPhoreAcquired : <"+sourceThread +">:no response expected success");
										status = true;
									}
								}
							}
							
							BofaManager.comPortSemaphore.release(1); // Release the semaphore 
							
							ApplicationLauncher.logger.debug("sendDataToBofaAfterSemaPhoreAcquired: comPortSemaphore: <"+sourceThread +"> :released");	
						
						}
					}else{
						retryCount = 0;
						ApplicationLauncher.logger.debug("sendDataToBofaAfterSemaPhoreAcquired : <"+sourceThread +"> :user abort detected");
						
					}
					retryCount--;
				}
				} catch (Exception e) {
					e.printStackTrace();
					ApplicationLauncher.logger.error("sendDataToBofaAfterSemaPhoreAcquired : <"+sourceThread +"> :Exception :" + e.getMessage());
				} finally {

					Sleep(220);
				}
				//=====================================
		//}
			
			responseReturn.put("status", status);
			responseReturn.put("result", responseData);
			ApplicationLauncher.logger.info("sendDataToBofaAfterSemaPhoreAcquired:<"+sourceThread +"> Exit");  
			return responseReturn;//status;
		}	
	
	
	public static Map<String,Object> sendDataToBofaWithQueue(String sourceThread, String payLoadInHex,int timeDelayInMilliSec,
			String expectedDataInHex,boolean isResponseExpected, boolean forceExecute){

		ApplicationLauncher.logger.info("sendDataToBofaWithQueue: <"+sourceThread +">: Entry");  
		String sourceThreadFinal = sourceThread +"-" + String.valueOf(Instant.now().toEpochMilli());
		Map<String,Object> responseReturn = new HashMap<String,Object>();

		//===============================		
		try {
			//ApplicationLauncher.logger.debug("sendDataToBofaWithQueue: comPortSemaphore: <"+sourceThread +">:acquiring...");		
			//ApplicationLauncher.logger.debug("sendDataToBofaWithQueue: comPortSemaphore: <"+sourceThread +">: availablePermits :" + BofaManager.comPortSemaphore.availablePermits());


			if((!ProjectExecutionController.getUserAbortedFlag()) || (forceExecute) ){
					//if(BofaManager.comPortSemaphore.tryAcquire(1,1, TimeUnit.SECONDS)){
						
						//ApplicationLauncher.logger.debug("sendDataToBofaWithQueue: comPortSemaphore: <"+sourceThread +">:acquired");	

						if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
							
							//responseStatus = pwrSrcBofaMessenger.bofaPowerSourceMsngrSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected,sourceThreadFinal);

							Callable<Map<String,Object>> taskbofaPowerSourceMsngrSendCommand = () -> {
								//Data_RefStdBofa.sendActualOutputValueReadingCommand();
								String queuedResponseStatus = pwrSrcBofaMessenger.bofaPowerSourceMsngrSendCommandProcessWithQueue(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected,sourceThreadFinal);
								String queuedResponseData = "";
								boolean queueStatus =false;
								if (queuedResponseStatus.equals(DeleteMeConstant.SUCCESS_RESPONSE)) {
									if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
										ApplicationLauncher.logger.debug("sendDataToBofaWithQueue: <"+sourceThreadFinal +">: success Entry"); 
										queuedResponseData = pwrSrcBofaMessenger.getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage();
										ApplicationLauncher.logger.debug("sendDataToBofaWithQueue: <"+sourceThreadFinal +">: queuedResponseData-Ascii: " + queuedResponseData); 
										queuedResponseData = GuiUtils.asciiToHex(queuedResponseData);
										ApplicationLauncher.logger.debug("sendDataToBofaWithQueue: <"+sourceThreadFinal +">: queuedResponseData-Hex: " + queuedResponseData);
									}else{
										queuedResponseData = BofaManager.getSerialDM_Obj().getRxMsgQ_PwrSrc().getLastReadMessage();//BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
									}
									queueStatus = true;
									
								}else {
									if(!isResponseExpected){
										if(queuedResponseStatus.equals(DeleteMeConstant.NO_RESPONSE)){
											ApplicationLauncher.logger.info("sendDataToBofaWithQueue : <"+sourceThreadFinal +">:no response expected success");
											queueStatus = true;
										}
									}
								}
								
								Map<String,Object> queuedResponseReturn = new HashMap<String,Object>();
								queuedResponseReturn.put("status", queueStatus);
								queuedResponseReturn.put("result", queuedResponseData);
								return queuedResponseReturn;
					        };
							if(sourceThread.contains("sendReadConstOfLiveRefStdCmd")) {
								ApplicationLauncher.logger.info("sendDataToBofaWithQueue : <"+sourceThreadFinal +">: bumped for high prority");
							}
							long timeoutInSec = 10;//5; for 5 - concurrent issue occured
							Future<Map<String,Object>> future = BofaRequestProcessor.addRequest(ConstantPowerSourceBofa.BOFA_COM_PORT_KEY, taskbofaPowerSourceMsngrSendCommand, 5, timeoutInSec, TimeUnit.SECONDS);

							// Option 1: Wait for it to complete, but with your own timeout
							try {
								responseReturn = future.get(timeoutInSec, TimeUnit.SECONDS); // This waits for result
							    ApplicationLauncher.logger.debug("sendDataToBofaWithQueue :  <"+sourceThreadFinal +">: Task completed: responseReturn : " + responseReturn);
							} catch (TimeoutException e) {
								ApplicationLauncher.logger.debug("sendDataToBofaWithQueue :  <"+sourceThreadFinal +"> : Task timed out (from caller side)");
								responseReturn.put("status", false);
								responseReturn.put("result", sourceThreadFinal + "-Timed Out");
							} catch (ExecutionException e) {
								ApplicationLauncher.logger.debug("sendDataToBofaWithQueue :  <"+sourceThreadFinal +"> : Task failed: " + e.getCause());
								responseReturn.put("status", false);
								responseReturn.put("result", sourceThreadFinal + "-Task Failed");
							} catch (InterruptedException e) {
							    Thread.currentThread().interrupt();
							    ApplicationLauncher.logger.debug("sendDataToBofaWithQueue :  <"+sourceThreadFinal +"> : Task interrupted: ");
								
							    responseReturn.put("status", false);
								responseReturn.put("result", sourceThreadFinal + "-Interupted");
							}
							
							
						}else {

							BofaManager.getSerialDM_Obj().bofaPowerSourceSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
						}
						
						/*if (responseStatus.equals(DeleteMeConstant.SUCCESS_RESPONSE)) {
							if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
								responseData = pwrSrcBofaMessenger.getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage();
								responseData = GuiUtils.asciiToHex(responseData);
								
							}else{
								responseData = BofaManager.getSerialDM_Obj().getRxMsgQ_PwrSrc().getLastReadMessage();//BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
							}
							status = true;
							
						}else {
							if(!isResponseExpected){
								if(responseStatus.equals(DeleteMeConstant.NO_RESPONSE)){
									ApplicationLauncher.logger.info("sendDataToBofaWithQueue : <"+sourceThread +">:no response expected success");
									status = true;
								}
							}
						}*/

						//BofaManager.comPortSemaphore.release(1); // Release the semaphore 

						//ApplicationLauncher.logger.debug("sendDataToBofaWithQueue: comPortSemaphore: <"+sourceThread +"> :released");	

					//}
				}else{
					//retryCount = 0;
					ApplicationLauncher.logger.debug("sendDataToBofaWithQueue : <"+sourceThreadFinal +"> :user abort detected");

				}
				//retryCount--;
			//}
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("sendDataToBofaWithQueue : <"+sourceThreadFinal +"> :Exception :" + e.getMessage());
		} finally {

			Sleep(220);
		}


		//responseReturn.put("status", status);
		//responseReturn.put("result", responseData);
		//obsoluteResponseReturn;
		ApplicationLauncher.logger.info("sendDataToBofaWithQueue:<"+sourceThreadFinal +"> Exit");  
		return responseReturn;//status;
	}	


		
//==============================		
		public static String asciiToHex(String asciiString) {
	        StringBuilder hexString = new StringBuilder();
	        String hex = "";
	        for (int i = 0; i < asciiString.length(); i++) {
	            char c = asciiString.charAt(i);
	           // hex = Integer.toHexString((int) c);
	            hex = String.format("%02X", ((int)c)); // updated by Gopi on Version S4.2.0.7.0.5 - 07-Apr-2024...when 0x01 is sent, it is converted as 1 instead of 01
	            hexString.append(hex);
	        }
	        return hexString.toString();
	    }
//==============================
	    public static String removeDecimal(String input) {
	        return input.replace(".", "");
	    }
//==============================

	    public static float formatDecimalPoints(float input, int decimalPlaces) {
	    	
	    	  // Calculate the divisor based on the number of decimal places
	        float divisor = (float) Math.pow(10, decimalPlaces);

	        // Divide the value by the divisor to get the desired float value
	        float formattedUA = input / divisor;
	        
	    	return formattedUA ;
	    }
//====================================
		public static SerialPortManagerPwrSrc getSerialDM_Obj() {
			return serialDM_Obj;
		}

		public static void setSerialDM_Obj(SerialPortManagerPwrSrc serialDM_Obj) {
			BofaManager.serialDM_Obj = serialDM_Obj;
		}

/*		public static PowerSourceBofaMessenger getPowerSourceBofaMessenger() {
			return powerSourceBofaMessenger;
		}

		public void setPowerSourceBofaMessenger(PowerSourceBofaMessenger powerSourceBofaMessenger) {
			this.powerSourceBofaMessenger = powerSourceBofaMessenger;
		}*/

		public static SerialPortManagerPwrSrc_V2 getSerialDmV2_Obj() {
			return serialDmV2_Obj;
		}

		public static void setSerialDmV2_Obj(SerialPortManagerPwrSrc_V2 serialDmV2_Obj) {
			BofaManager.serialDmV2_Obj = serialDmV2_Obj;
		}

		public static PowerSourceBofaMessenger getPwrSrcBofaMessenger() {
			return pwrSrcBofaMessenger;
		}

		public static void setPwrSrcBofaMessenger(PowerSourceBofaMessenger pwrSrcBofaMessenger) {
			BofaManager.pwrSrcBofaMessenger = pwrSrcBofaMessenger;
		}
	    
	    
		
		
		//==========================================================================================================
		
}
