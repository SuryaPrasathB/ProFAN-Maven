package com.tasnetwork.calibration.energymeter.device;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.StringUtils;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdMte;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdKiggs;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.deployment.FailureManager;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
import com.tasnetwork.calibration.energymeter.device.SerialDataMteRefStd.MteRefStdTask;
import com.tasnetwork.calibration.energymeter.util.ErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;
import com.tasnetwork.calibration.energymeter.util.TMS_FloatConversion;

public class SerialDataRefStdKiggs {

	Timer timer;
	int responseWaitCount =5;
	static Communicator SerialPortObj= null;
	volatile Boolean  ExpectedLengthRecieved = false;
	volatile Boolean ExpectedResponseRecieved = false;
	int RetryIntervalInMsec=200;//200;//100;//1000;
	int numberOfReadingRequired = 1;
	int numberOfReadingObtained = 0;
	String ER_TERMINATOR = "";
	

	volatile Boolean ErrorResponseRecieved = false;

/*	DataLogParse refStdDataRphase = new DataLogParse();
	DataLogParse refStdDataYphase = new DataLogParse();
	DataLogParse refStdDataBphase = new DataLogParse();*/

	static DeviceDataManagerController DisplayDataObj =  new DeviceDataManagerController();

	public String rPhaseVoltageDisplayData="";
	public String yPhaseVoltageDisplayData="";
	public String bPhaseVoltageDisplayData="";
	public String rPhaseCurrentDisplayData="";
	public String yPhaseCurrentDisplayData="";
	public String bPhaseCurrentDisplayData="";
	public String rPhaseDegreePhaseData="";
	public String yPhaseDegreePhaseData="";
	public String bPhaseDegreePhaseData="";
	public String rPhasePowerFactorData="";
	public String yPhasePowerFactorData="";
	public String bPhasePowerFactorData="";
	public String rPhaseFreqDisplayData="";
	public String yPhaseFreqDisplayData="";
	public String bPhaseFreqDisplayData="";
	public String rPhaseActivePowerDisplayData="";
	public String yPhaseActivePowerDisplayData="";
	public String bPhaseActivePowerDisplayData="";
	
	public String rPhaseReactivePowerDisplayData="";
	public String yPhaseReactivePowerDisplayData="";
	public String bPhaseReactivePowerDisplayData="";
	
	public String rPhaseApparentPowerDisplayData="";
	public String yPhaseApparentPowerDisplayData="";
	public String bPhaseApparentPowerDisplayData="";
	public String VA_DisplayData="";
	public String VAR_DisplayData="";
	
	public String rPhaseActiveEnergyAccumulatedDisplayData="";
	public String yPhaseActiveEnergyAccumulatedDisplayData="";
	public String bPhaseActiveEnergyAccumulatedDisplayData="";
	
	public String rPhaseReactiveEnergyAccumulatedDisplayData="";	
	public String yPhaseReactiveEnergyAccumulatedDisplayData="";
	public String bPhaseReactiveEnergyAccumulatedDisplayData="";
	
	public String rPhaseApparentEnergyAccumulatedDisplayData="";	
	public String yPhaseApparentEnergyAccumulatedDisplayData="";
	public String bPhaseApparentEnergyAccumulatedDisplayData="";
	
	public String rPhaseVoltageTapData="";
	public String rPhaseCurrentTapData="";
	
	static String RefStd_ReadSerialData = "";
	public int ReceivedLength = 0;
	String PhaseAReading = "";
	String PhaseBReading = "";
	String PhaseCReading = "";



	public SerialDataRefStdKiggs(Communicator inpSerialPortObj ){
		SerialPortObj = inpSerialPortObj;
		
	}

	public static void setRefStd_ReadSerialData(String currentSerialData) {
		// TODO Auto-generated method stub

		RefStd_ReadSerialData = currentSerialData;

	}
	
	public int getNumberOfReadingRequired() {
		return numberOfReadingRequired;
	}

	public void setNumberOfReadingRequired(int numberOfReadings) {
		this.numberOfReadingRequired = numberOfReadings;
	}

	public int getNumberOfReadingObtained() {
		return numberOfReadingObtained;
	}

	public void setNumberOfReadingObtained(int numberOfReadingObtained) {
		this.numberOfReadingObtained = numberOfReadingObtained;
	}

	public static void ClearRefStd_ReadSerialData() {
		// TODO Auto-generated method stub
		ApplicationLauncher.logger.debug("ClearRefStd_ReadSerialData : Entry");

		RefStd_ReadSerialData = "";

	}

	public static String getRefStd_ReadSerialData() {
		// TODO Auto-generated method stub

		return RefStd_ReadSerialData = SerialPortObj.getSerialData();

	}

	public void setReceivedLength(Integer length){

		this.ReceivedLength = length;
	}

	public Integer getReceivedLength(){

		return this.ReceivedLength;
	}

	public void setPhaseAReading(String Value) {
		// TODO Auto-generated method stub

		this.PhaseAReading = Value;

	}

	public String getPhaseAReading() {
		// TODO Auto-generated method stub

		return this.PhaseAReading;

	} 

	public void setPhaseBReading(String Value) {
		// TODO Auto-generated method stub

		this.PhaseBReading = Value;

	}

	public String getPhaseBReading() {
		// TODO Auto-generated method stub

		return this.PhaseBReading;

	} 

	public void setPhaseCReading(String Value) {
		// TODO Auto-generated method stub

		this.PhaseCReading = Value;

	}

	public String getPhaseCReading() {
		// TODO Auto-generated method stub

		return this.PhaseBReading;

	}
	
	public Boolean IsExpectedErrorResponseReceived(){
		return this.ErrorResponseRecieved;

	}
	
	



	public Boolean IsExpectedResponseReceived(){

		while((!ExpectedResponseRecieved)&&(responseWaitCount!=0)){
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("Ref: IsExpectedResponseReceived: InterruptedException:" + e.getMessage());
			}
		};
		return this.ExpectedResponseRecieved;
	}
	
	
	public String parseAccumulatedApparentEnergyfromRefStd(String serialInputData ){



		rPhaseApparentEnergyAccumulatedDisplayData="";
		//yPhaseVoltageDisplayData="";
		//bPhaseVoltageDisplayData="";
		try{
			
		
			String hexStringData = serialInputData.substring(34,44);
			ApplicationLauncher.logger.debug("parseAccumulatedApparentEnergyfromRefStd : hexStringData3: " + hexStringData);
			rPhaseApparentEnergyAccumulatedDisplayData = GuiUtils.parseHexStringToFloatString( hexStringData);
			//rPhaseVoltageDisplayData = String.format("%.4f", Float.parseFloat(voltageValue)/divisor);
			
			ApplicationLauncher.logger.debug("parseAccumulatedApparentEnergyfromRefStd : rPhaseApparentEnergyAccumulatedDisplayData:" + rPhaseApparentEnergyAccumulatedDisplayData);
			//rPhaseVoltageDisplayData = parsedData[1];

			//}
		}catch (Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("parseAccumulatedActiveEnergyfromRefStd: Exception :"+E.toString());

		}

		
		return rPhaseApparentEnergyAccumulatedDisplayData;
	}
	
	public String parseAccumulatedActiveEnergyfromRefStd(String serialInputData ){



		rPhaseActiveEnergyAccumulatedDisplayData="";
		//yPhaseVoltageDisplayData="";
		//bPhaseVoltageDisplayData="";
		try{
			
		
			String hexStringData = serialInputData.substring(10,20);
			ApplicationLauncher.logger.debug("parseAccumulatedActiveEnergyfromRefStd : hexStringData1: " + hexStringData);
			rPhaseActiveEnergyAccumulatedDisplayData = GuiUtils.parseHexStringToFloatString( hexStringData);
			
/*			
			hexStringData = serialInputData.substring(22,30);
			ApplicationLauncher.logger.debug("parseAccumulatedEnergyfromRefStd : hexStringData1: " + hexStringData);
			rPhaseReactiveEnergyAccumulatedDisplayData = GUIUtils.parseHexStringToFloatString( hexStringData);
			
			hexStringData = serialInputData.substring(32,40);
			ApplicationLauncher.logger.debug("parseAccumulatedEnergyfromRefStd : hexStringData1: " + hexStringData);
			rPhaseApparentEnergyAccumulatedDisplayData = GUIUtils.parseHexStringToFloatString( hexStringData);*/
			//rPhaseVoltageDisplayData = String.format("%.4f", Float.parseFloat(voltageValue)/divisor);
			
			ApplicationLauncher.logger.debug("parseAccumulatedActiveEnergyfromRefStd : rPhaseActiveEnergyAccumulatedDisplayData:" + rPhaseActiveEnergyAccumulatedDisplayData);
			//ApplicationLauncher.logger.debug("parseAccumulatedEnergyfromRefStd : rPhaseReactiveEnergyAccumulatedDisplayData:" + rPhaseReactiveEnergyAccumulatedDisplayData);
			//ApplicationLauncher.logger.debug("parseAccumulatedEnergyfromRefStd : rPhaseApparentEnergyAccumulatedDisplayData:" + rPhaseApparentEnergyAccumulatedDisplayData);
			//rPhaseVoltageDisplayData = parsedData[1];

			//}
		}catch (Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("parseAccumulatedActiveEnergyfromRefStd: Exception :"+E.toString());

		}

		
		return rPhaseActiveEnergyAccumulatedDisplayData;
	}
	
	public String parseAccumulatedReactiveEnergyfromRefStd(String serialInputData ){



		rPhaseReactiveEnergyAccumulatedDisplayData="";
		//yPhaseVoltageDisplayData="";
		//bPhaseVoltageDisplayData="";
		try{
			
		
			String hexStringData = serialInputData.substring(22,32);
			ApplicationLauncher.logger.debug("parseAccumulatedReactiveEnergyfromRefStd : hexStringData2: " + hexStringData);
			rPhaseReactiveEnergyAccumulatedDisplayData = GuiUtils.parseHexStringToFloatString( hexStringData);

			
			ApplicationLauncher.logger.debug("parseAccumulatedReactiveEnergyfromRefStd : rPhaseReactiveEnergyAccumulatedDisplayData:" + rPhaseReactiveEnergyAccumulatedDisplayData);
			
			//}
		}catch (Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("parseAccumulatedReactiveEnergyfromRefStd: Exception :"+E.toString());

		}

		
		return rPhaseReactiveEnergyAccumulatedDisplayData;
	}
	

	public String parseVoltageDatafromRefStd(String serialInputData ){



		rPhaseVoltageDisplayData="";
		//yPhaseVoltageDisplayData="";
		//bPhaseVoltageDisplayData="";
		try{
			
		
			String hexStringData = serialInputData.substring(10,20);
			rPhaseVoltageDisplayData = GuiUtils.parseHexStringToFloatString( hexStringData);
			
			//rPhaseVoltageDisplayData = String.format("%.4f", Float.parseFloat(voltageValue)/divisor);
			
			ApplicationLauncher.logger.debug("parseVoltageDatafromRefStd : rPhaseVoltageDisplayData:" + rPhaseVoltageDisplayData);
			//rPhaseVoltageDisplayData = parsedData[1];

			//}
		}catch (Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("parseVoltageDatafromRefStd: Exception :"+E.toString());

		}

		
		return rPhaseVoltageDisplayData;
	}
	
	
	public String parseDegreeDatafromRefStd(String serialInputData ){



		rPhaseDegreePhaseData="";
		//yPhaseVoltageDisplayData="";
		//bPhaseVoltageDisplayData="";
		try{
			


			String hexStringData = serialInputData.substring(10,20);
			rPhaseDegreePhaseData = GuiUtils.parseHexStringToFloatString( hexStringData);
			//rPhaseDegreePhaseData = String.format("%.4f", Float.parseFloat(degreeValue)/divisor);
			
			ApplicationLauncher.logger.debug("parseDegreeDatafromRefStd : rPhaseDegreePhaseData:" + rPhaseDegreePhaseData);
			//rPhaseVoltageDisplayData = parsedData[1];

			//}
		}catch (Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("parseDegreeDatafromRefStd: Exception :"+E.toString());

		}

		
		return rPhaseDegreePhaseData;
	}
	
	
	
	
	public String parsePowerFactorDatafromRefStd(String serialInputData ){



		rPhasePowerFactorData="";
		//yPhaseVoltageDisplayData="";
		//bPhaseVoltageDisplayData="";
		try{
			


			String hexStringData = serialInputData.substring(10,20);
			rPhasePowerFactorData = GuiUtils.parseHexStringToFloatString( hexStringData);
			//rPhasePowerFactorData = String.format("%.4f", Float.parseFloat(pfValue)/divisor);
			
			ApplicationLauncher.logger.debug("parsePowerFactorDatafromRefStd : rPhasePowerFactorData:" + rPhasePowerFactorData);
			//rPhaseVoltageDisplayData = parsedData[1];

			//}
		}catch (Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("parsePowerFactorDatafromRefStd: Exception :"+E.toString());

		}

		
		return rPhasePowerFactorData;
	}
	
	
	
	public String parseApparentPowerDatafromRefStd(String serialInputData ){



		rPhaseApparentPowerDisplayData="";
		//yPhaseVoltageDisplayData="";
		//bPhaseVoltageDisplayData="";
		try{
			

			String hexStringData = serialInputData.substring(10,20);
			rPhaseApparentPowerDisplayData = GuiUtils.parseHexStringToFloatString( hexStringData);
			
			//rPhaseApparentPowerDisplayData = String.format("%.4f", Float.parseFloat(apparentValue)/divisor);
			
			ApplicationLauncher.logger.debug("parseApparentPowerDatafromRefStd : rPhaseApparentPowerDisplayData:" + rPhaseApparentPowerDisplayData);
			//rPhaseVoltageDisplayData = parsedData[1];

			//}
		}catch (Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("parseApparentPowerDatafromRefStd: Exception :"+E.toString());

		}

		
		return rPhaseApparentPowerDisplayData;
	}
	
	
	public String parseVoltageTapDatafromRefStd(String serialInputData ){



		rPhaseVoltageTapData="";
		//yPhaseVoltageDisplayData="";
		//bPhaseVoltageDisplayData="";
		try{
			

			String hexStringData = serialInputData.substring(10,16);
			rPhaseVoltageTapData = hexStringData;//GUIUtils.parseHexStringToFloatString( hexStringData);
			
			//rPhaseApparentPowerDisplayData = String.format("%.4f", Float.parseFloat(apparentValue)/divisor);
			
			ApplicationLauncher.logger.debug("parseVoltageTapDatafromRefStd : rPhaseVoltageTapData:" + rPhaseVoltageTapData);
			//rPhaseVoltageDisplayData = parsedData[1];

			//}
		}catch (Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("parseVoltageTapDatafromRefStd: Exception :"+E.toString());

		}

		
		return rPhaseVoltageTapData;
	}
	
	
	public String parseCurrentTapDatafromRefStd(String serialInputData ){



		rPhaseCurrentTapData="";
		//yPhaseVoltageDisplayData="";
		//bPhaseVoltageDisplayData="";
		try{
			

			String hexStringData = serialInputData.substring(16,26);
			rPhaseCurrentTapData = hexStringData;//GUIUtils.parseHexStringToFloatString( hexStringData);
			
			//rPhaseApparentPowerDisplayData = String.format("%.4f", Float.parseFloat(apparentValue)/divisor);
			
			ApplicationLauncher.logger.debug("parseCurrentTapDatafromRefStd : rPhaseCurrentTapData:" + rPhaseCurrentTapData);
			//rPhaseVoltageDisplayData = parsedData[1];

			//}
		}catch (Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("parseCurrentTapDatafromRefStd: Exception :"+E.toString());

		}

		
		return rPhaseCurrentTapData;
	}
	
	public String parseReactivePowerDatafromRefStd(String serialInputData ){



		rPhaseReactivePowerDisplayData="";
		//yPhaseVoltageDisplayData="";
		//bPhaseVoltageDisplayData="";
		try{


			String hexStringData = serialInputData.substring(10,20);
			rPhaseReactivePowerDisplayData = GuiUtils.parseHexStringToFloatString( hexStringData);
			
			//rPhaseReactivePowerDisplayData = String.format("%.4f", Float.parseFloat(reactiveValue)/divisor);
			
			ApplicationLauncher.logger.debug("parseReactivePowerDatafromRefStd : rPhaseReactivePowerDisplayData:" + rPhaseReactivePowerDisplayData);
			//rPhaseVoltageDisplayData = parsedData[1];

			//}
		}catch (Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("parseReactivePowerDatafromRefStd: Exception :"+E.toString());

		}

		
		return rPhaseReactivePowerDisplayData;
	}
	
	public String parseFrequencyDatafromRefStd(String serialInputData ){



		rPhaseFreqDisplayData="";
		//yPhaseVoltageDisplayData="";
		//bPhaseVoltageDisplayData="";
		try{
			


			
			String hexStringData = serialInputData.substring(8,18);
			rPhaseFreqDisplayData = GuiUtils.parseHexStringToFloatString( hexStringData);
			
			//rPhaseFreqDisplayData = String.format("%.4f", Float.parseFloat(freqValue)/divisor);
			
			ApplicationLauncher.logger.debug("parseFrequencyDatafromRefStd : rPhaseFreqDisplayData:" + rPhaseFreqDisplayData);
			//rPhaseVoltageDisplayData = parsedData[1];

			//}
		}catch (Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("parseFrequencyDatafromRefStd: Exception :"+E.toString());

		}

		
		return rPhaseFreqDisplayData;
	}
	

	public String parseActivePowerDatafromRefStd(String serialInputData ){



		rPhaseActivePowerDisplayData="";
		//yPhaseVoltageDisplayData="";
		//bPhaseVoltageDisplayData="";
		try{
			


			String hexStringData = serialInputData.substring(10,20);
			rPhaseActivePowerDisplayData = GuiUtils.parseHexStringToFloatString( hexStringData);
			
			//rPhaseActivePowerDisplayData = String.format("%.4f", Float.parseFloat(activeValue)/divisor);
			
			ApplicationLauncher.logger.debug("parseActivePowerDatafromRefStd : rPhaseActivePowerDisplayData:" + rPhaseActivePowerDisplayData);
			//rPhaseVoltageDisplayData = parsedData[1];

			//}
		}catch (Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("parseActivePowerDatafromRefStd: Exception :"+E.toString());

		}

		
		return rPhaseActivePowerDisplayData;
	}
	
	public String parseCurrentDatafromRefStd(String serialInputData ){



		rPhaseCurrentDisplayData="";
		//yPhaseVoltageDisplayData="";
		//bPhaseVoltageDisplayData="";
		try{
			

		
			String hexStringData = serialInputData.substring(22,32);
			rPhaseCurrentDisplayData = GuiUtils.parseHexStringToFloatString( hexStringData);
			
			ApplicationLauncher.logger.debug("parseCurrentDatafromRefStd : rPhaseCurrentDisplayData:" + rPhaseCurrentDisplayData);
			//rPhaseVoltageDisplayData = parsedData[1];

			//}
		}catch (Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("parseCurrentDatafromRefStd: Exception :"+E.toString());

		}

		
		return rPhaseCurrentDisplayData;
	}




	public Boolean IsExpectedLengthReceived(){

		while((!ExpectedLengthRecieved)&&(responseWaitCount!=0)){
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("IsExpectedLengthReceived: InterruptedException :"+e.getMessage());
			}
		};
		return this.ExpectedLengthRecieved;
	}



	
	public void SerialReponseTimerStart(int waitTimeCount) {
		timer = new Timer();
		timer.schedule(new KiggsRefStdTask(), RetryIntervalInMsec);
		responseWaitCount = waitTimeCount;
		//setNumberOfReadingRequired( noOfReadingRequired);
		

	}
	
	
	class KiggsRefStdTask extends TimerTask {
		public void run() {
			String CurrentSerialData = SerialPortObj.getSerialData();
			//ApplicationLauncher.logger.debug("PowerSrcTask: responseWaitCount: "+responseWaitCount);
			ApplicationLauncher.logger.debug("KiggsRefStdTask: SerialData:"+CurrentSerialData);
			String ExpectedResult = SerialPortObj.getExpectedResult();
			int expectedLength = SerialPortObj.getExpectedLength();
			ApplicationLauncher.logger.debug("KiggsRefStdTask: ExpectedResult:"+ExpectedResult);
			ApplicationLauncher.logger.debug("KiggsRefStdTask: expectedLength:"+expectedLength);
			if(CurrentSerialData.equals(ExpectedResult) || CurrentSerialData.contains(ExpectedResult))
			{
				//if(CurrentSerialData.endsWith(ER_TERMINATOR)) {
				if(CurrentSerialData.length() == expectedLength) {	
				
					ApplicationLauncher.logger.info("KiggsRefStdTask Expected received:");
					//ApplicationLauncher.logger.debug("SerialDataRefStdKre: Expected recieved:Current Data:"+CurrentSerialData+"\n");
					//SerialPortObj.StripLength(CurrentSerialData.length());
					ExpectedResponseRecieved=true; 
				}
			}
			else if (CurrentSerialData.length()>0){
				if( (CurrentSerialData.equals(SerialPortObj.getExpectedDataErrorResult())) || (CurrentSerialData.equals(SerialPortObj.getExpectedSetErrorResult())) ){
					ApplicationLauncher.logger.info("KiggsRefStdTask: Unable to set the parameter:Current Data:"+CurrentSerialData+":Expected Data:"+SerialPortObj.getExpectedResult());
					FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_100+":Expected:"+SerialPortObj.getExpectedResult()+" :Actual:"+CurrentSerialData);
					SerialPortObj.StripLength(CurrentSerialData.length());
					ErrorResponseRecieved = true;
				}else if(CurrentSerialData.length()>ExpectedResult.length()){
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("KiggsRefStdTask unExpected received1:Current Data:"+CurrentSerialData+":Expected Data:"+SerialPortObj.getExpectedResult());
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					if( (CurrentSerialData.contains(SerialPortObj.getExpectedDataErrorResult()) ) || ((CurrentSerialData.contains(SerialPortObj.getExpectedSetErrorResult()))) ){
						ApplicationLauncher.logger.info("KiggsRefStdTask: Expected error response contained in received response");
						FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_100A+":Expected:"+SerialPortObj.getExpectedResult()+" :Actual:"+CurrentSerialData);
						ErrorResponseRecieved = true;
					}
					if(CurrentSerialData.contains(ExpectedResult)){
						if(CurrentSerialData.endsWith(ER_TERMINATOR)) {
							ApplicationLauncher.logger.info("KiggsRefStdTask: Expected response contained in received response");
							ExpectedResponseRecieved=true;
						}
					}

					SerialPortObj.StripLength(CurrentSerialData.length());
				}else if( (CurrentSerialData.contains(SerialPortObj.getExpectedDataErrorResult()) ) || ((CurrentSerialData.contains(SerialPortObj.getExpectedSetErrorResult()))) ){
					ApplicationLauncher.logger.info("KiggsRefStdTask: Expected error response contained in received response2");
					FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_100A+":Expected:"+SerialPortObj.getExpectedResult()+" :Actual:"+CurrentSerialData);
					ErrorResponseRecieved = true;
					
				}else {
				
					ApplicationLauncher.logger.debug("KiggsRefStdTask: Expected recieved: getExpectedDataErrorResult :"+SerialPortObj.getExpectedDataErrorResult()+"\n");
					ApplicationLauncher.logger.debug("KiggsRefStdTask: Expected recieved: getExpectedSetErrorResult :"+SerialPortObj.getExpectedSetErrorResult()+"\n");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("KiggsRefStdTask unExpected received2:Current Data:"+CurrentSerialData+":Expected Data:"+SerialPortObj.getExpectedResult());
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_100B+":Expected:"+SerialPortObj.getExpectedResult()+" :Actual:"+CurrentSerialData);
					
					//SerialPortObj.StripLength(CurrentSerialData.length());
				}
			}
			responseWaitCount --;
			//ApplicationLauncher.logger.debug("KiggsRefStdTask: responseWaitCount: "+responseWaitCount);
			//ApplicationLauncher.logger.debug("KiggsRefStdTask: ExpectedResponseRecieved: "+ExpectedResponseRecieved);
			//ApplicationLauncher.logger.debug("KiggsRefStdTask: ErrorResponseRecieved: "+ErrorResponseRecieved);
			//ApplicationLauncher.logger.debug("KiggsRefStdTask: getUserAbortedFlag(): "+ProjectExecutionController.getUserAbortedFlag());
			if (responseWaitCount == 0 || ExpectedResponseRecieved ||
					ErrorResponseRecieved ||  
					ProjectExecutionController.getUserAbortedFlag()){
				responseWaitCount=0;
				ApplicationLauncher.logger.info("KiggsRefStdTask :Timer Exit!%n");
				timer.cancel(); //Terminate the timer thread
			}
			else{
				timer.schedule(new KiggsRefStdTask(), RetryIntervalInMsec);
			}
		}



	}


	public String getR_PhaseVoltageTapData() {
		return rPhaseVoltageTapData;
	}

	public void setR_PhaseVoltageTapData(String rPhaseVoltageTapData) {
		this.rPhaseVoltageTapData = rPhaseVoltageTapData;
	}

	public String getR_PhaseCurrentTapData() {
		return rPhaseCurrentTapData;
	}

	public void setR_PhaseCurrentTapData(String rPhaseCurrentTapData) {
		this.rPhaseCurrentTapData = rPhaseCurrentTapData;
	}
	
	public static String manipulateRssPulseConstantInWattHour(float voltageTapRange, float currentTapRange){
		
		ApplicationLauncher.logger.debug("manipulateRssPulseConstantInWattHour :Entry");
		String manipulatedValue = "88888";
		
		ApplicationLauncher.logger.debug("manipulateRssPulseConstantInWattHour : voltageTapRange: " + voltageTapRange);
		ApplicationLauncher.logger.debug("manipulateRssPulseConstantInWattHour : currentTapRange: " + currentTapRange);
		int value =  (int) ((( (( ConstantRefStdKiggs.RSS_PREDEFINED_CONSTANT_DATA1 / voltageTapRange) / currentTapRange)) * ConstantRefStdKiggs.RSS_PREDEFINED_CONSTANT_DATA2) );
		ApplicationLauncher.logger.debug("manipulateRssPulseConstantInWattHour :value1: " + value);
		value = value/1000;
		ApplicationLauncher.logger.debug("manipulateRssPulseConstantInWattHour :value2: " + value);
		manipulatedValue = String.valueOf( value );
		ApplicationLauncher.logger.debug("manipulateRssPulseConstantInWattHour :manipulatedValue: " + manipulatedValue);
		
		return manipulatedValue;
	}




}
