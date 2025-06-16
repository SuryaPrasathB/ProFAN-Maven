package com.tasnetwork.calibration.energymeter.device;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.StringUtils;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.Constant_ICT;
import com.tasnetwork.calibration.energymeter.deployment.FailureManager;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
import com.tasnetwork.calibration.energymeter.util.ErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;

public class SerialDataKreICT {

	


		
		Timer timer;
		int responseWaitCount =5;
		static Communicator SerialPortObj= null;
		Boolean ExpectedLengthRecieved = false;
		Boolean ExpectedResponseRecieved = false;
		int RetryIntervalInMsec=200;//100;//1000;
		int numberOfReadingRequired = 1;
		int numberOfReadingObtained = 0;
		
		Boolean ErrorResponseRecieved = false;

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
		public String rPhaseWattDisplayData="";
		public String yPhaseWattDisplayData="";
		public String bPhaseWattDisplayData="";
		public String VA_DisplayData="";
		public String VAR_DisplayData="";
		static String ictReadSerialData = "";
		public int ReceivedLength = 0;
		String PhaseAReading = "";
		String PhaseBReading = "";
		String PhaseCReading = "";



		public SerialDataKreICT(Communicator inpSerialPortObj ){
			SerialPortObj = inpSerialPortObj;
			
		}

		public static void setIctReadSerialData(String currentSerialData) {
			// TODO Auto-generated method stub

			ictReadSerialData = currentSerialData;

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

		public static void clearIctReadSerialData() {
			// TODO Auto-generated method stub
			ApplicationLauncher.logger.debug("clearIctReadSerialData : Entry");

			ictReadSerialData = "";

		}

		public static String getIctReadSerialData() {
			// TODO Auto-generated method stub

			return ictReadSerialData = SerialPortObj.getSerialData();

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




		public void SerialReponseTimerStart(int waitTimeCount) {
			timer = new Timer();
			timer.schedule(new KreIctTask(), RetryIntervalInMsec);
			responseWaitCount = waitTimeCount;
			//setNumberOfReadingRequired( noOfReadingRequired);
			

		}
		
		public void SerialReponseTimerStartV2(int waitTimeCount, int noOfReadingRequired) {
			timer = new Timer();
			timer.schedule(new KreIctTaskV2(), RetryIntervalInMsec);
			responseWaitCount = waitTimeCount;
			setNumberOfReadingRequired( noOfReadingRequired);
			

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


		
		class KreIctTaskV2 extends TimerTask {
			public void run() {
				String CurrentSerialData = SerialPortObj.getSerialData();
				//ApplicationLauncher.logger.debug("PowerSrcTask: responseWaitCount: "+responseWaitCount);
				ApplicationLauncher.logger.debug("KreIctTaskV2: SerialData:"+CurrentSerialData+"\n");
				String ExpectedResult = SerialPortObj.getExpectedResult();
				if(CurrentSerialData.equals(ExpectedResult) || CurrentSerialData.contains(ExpectedResult))
				{
					if(CurrentSerialData.endsWith(Constant_ICT.ER_TERMINATOR)) {
						
						int terminatorCount = StringUtils.countMatches(CurrentSerialData, Constant_ICT.ER_TERMINATOR);
						ApplicationLauncher.logger.debug("KreIctTaskV2 : terminatorCount1 :" + terminatorCount);
						
						if(terminatorCount >= getNumberOfReadingRequired()){
					
							ApplicationLauncher.logger.info("SerialDataKreICT Expected received:");
							//ApplicationLauncher.logger.debug("SerialDataKreICT: Expected recieved:Current Data:"+CurrentSerialData+"\n");
							//SerialPortObj.StripLength(CurrentSerialData.length());
							ExpectedResponseRecieved=true; 
						}
					}
				}
				else if (CurrentSerialData.length()>0){
					if( (CurrentSerialData.equals(SerialPortObj.getExpectedDataErrorResult())) || (CurrentSerialData.equals(SerialPortObj.getExpectedSetErrorResult())) ){
						
						ApplicationLauncher.logger.info("SerialDataKreICT: Unable to set the parameter:Current Data:"+CurrentSerialData+":Expected Data:"+SerialPortObj.getExpectedResult());
						FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_100+":Expected:"+SerialPortObj.getExpectedResult()+" :Actual:"+CurrentSerialData);
						SerialPortObj.StripLength(CurrentSerialData.length());
						ErrorResponseRecieved = true;
					}else if(CurrentSerialData.length()>ExpectedResult.length()){
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("SerialDataKreICT unExpected received1:Current Data:"+CurrentSerialData+":Expected Data:"+SerialPortObj.getExpectedResult());
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("**********************************************************");
						if( (CurrentSerialData.contains(SerialPortObj.getExpectedDataErrorResult()) ) || ((CurrentSerialData.contains(SerialPortObj.getExpectedSetErrorResult()))) ){
							ApplicationLauncher.logger.info("SerialDataKreICT: Expected error response contained in received response");
							FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_100A+":Expected:"+SerialPortObj.getExpectedResult()+" :Actual:"+CurrentSerialData);
							ErrorResponseRecieved = true;
						}
						if(CurrentSerialData.contains(ExpectedResult)){
							if(CurrentSerialData.endsWith(Constant_ICT.ER_TERMINATOR)) {
								int terminatorCount = StringUtils.countMatches(CurrentSerialData, Constant_ICT.ER_TERMINATOR);
								ApplicationLauncher.logger.debug("KreIctTaskV2 : terminatorCount2 :" + terminatorCount);
								
								if(terminatorCount >= getNumberOfReadingRequired()){
									ApplicationLauncher.logger.info("SerialDataKreICT: Expected response contained in received response");
									ExpectedResponseRecieved=true;
								}
							}
						}

						SerialPortObj.StripLength(CurrentSerialData.length());
					}else if( (CurrentSerialData.contains(SerialPortObj.getExpectedDataErrorResult()) ) || ((CurrentSerialData.contains(SerialPortObj.getExpectedSetErrorResult()))) ){
						ApplicationLauncher.logger.info("SerialDataKreICT: Expected error response contained in received response2");
						FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_100A+":Expected:"+SerialPortObj.getExpectedResult()+" :Actual:"+CurrentSerialData);
						ErrorResponseRecieved = true;
						
					}else {
					
						ApplicationLauncher.logger.debug("SerialDataKreICT: Expected recieved: getExpectedDataErrorResult :"+SerialPortObj.getExpectedDataErrorResult()+"\n");
						ApplicationLauncher.logger.debug("SerialDataKreICT: Expected recieved: getExpectedSetErrorResult :"+SerialPortObj.getExpectedSetErrorResult()+"\n");
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("SerialDataKreICT unExpected received2:Current Data:"+CurrentSerialData+":Expected Data:"+SerialPortObj.getExpectedResult());
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("**********************************************************");
						FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_100B+":Expected:"+SerialPortObj.getExpectedResult()+" :Actual:"+CurrentSerialData);
						//SerialPortObj.StripLength(CurrentSerialData.length());
					}
				}
				responseWaitCount --;
				//ApplicationLauncher.logger.debug("KreIctTaskV2: responseWaitCount: "+responseWaitCount);
				//ApplicationLauncher.logger.debug("KreIctTaskV2: ExpectedResponseRecieved: "+ExpectedResponseRecieved);
				//ApplicationLauncher.logger.debug("KreIctTaskV2: ErrorResponseRecieved: "+ErrorResponseRecieved);
				//ApplicationLauncher.logger.debug("KreIctTaskV2: getUserAbortedFlag(): "+ProjectExecutionController.getUserAbortedFlag());
				if (responseWaitCount == 0 || ExpectedResponseRecieved ||
						ErrorResponseRecieved ||  
						ProjectExecutionController.getUserAbortedFlag()){
					responseWaitCount=0;
					ApplicationLauncher.logger.info("SerialDataKreICT :Timer Exit!%n");
					timer.cancel(); //Terminate the timer thread
				}
				else{
					timer.schedule(new KreIctTaskV2(), RetryIntervalInMsec);
				}
			}



		}
		
		class KreIctTask extends TimerTask {
			public void run() {
				String CurrentSerialData = SerialPortObj.getSerialData();
				//ApplicationLauncher.logger.debug("PowerSrcTask: responseWaitCount: "+responseWaitCount);
				ApplicationLauncher.logger.debug("KreIctTask: SerialData:"+CurrentSerialData+"\n");
				String ExpectedResult = SerialPortObj.getExpectedResult();
				if(CurrentSerialData.equals(ExpectedResult) || CurrentSerialData.contains(ExpectedResult))
				{
					if(CurrentSerialData.endsWith(Constant_ICT.ER_TERMINATOR)) {
						
					
						ApplicationLauncher.logger.info("SerialDataKreICT Expected received:");
						//ApplicationLauncher.logger.debug("SerialDataKreICT: Expected recieved:Current Data:"+CurrentSerialData+"\n");
						//SerialPortObj.StripLength(CurrentSerialData.length());
						ExpectedResponseRecieved=true; 
					}
				}
				else if (CurrentSerialData.length()>0){
					if( (CurrentSerialData.equals(SerialPortObj.getExpectedDataErrorResult())) || (CurrentSerialData.equals(SerialPortObj.getExpectedSetErrorResult())) ){
						ApplicationLauncher.logger.info("SerialDataKreICT: Unable to set the parameter:Current Data:"+CurrentSerialData+":Expected Data:"+SerialPortObj.getExpectedResult());
						FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_100+":Expected:"+SerialPortObj.getExpectedResult()+" :Actual:"+CurrentSerialData);
						SerialPortObj.StripLength(CurrentSerialData.length());
						ErrorResponseRecieved = true;
					}else if(CurrentSerialData.length()>ExpectedResult.length()){
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("SerialDataKreICT unExpected received1:Current Data:"+CurrentSerialData+":Expected Data:"+SerialPortObj.getExpectedResult());
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("**********************************************************");
						if( (CurrentSerialData.contains(SerialPortObj.getExpectedDataErrorResult()) ) || ((CurrentSerialData.contains(SerialPortObj.getExpectedSetErrorResult()))) ){
							ApplicationLauncher.logger.info("SerialDataKreICT: Expected error response contained in received response");
							FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_100A+":Expected:"+SerialPortObj.getExpectedResult()+" :Actual:"+CurrentSerialData);
							ErrorResponseRecieved = true;
						}
						if(CurrentSerialData.contains(ExpectedResult)){
							if(CurrentSerialData.endsWith(Constant_ICT.ER_TERMINATOR)) {
								ApplicationLauncher.logger.info("SerialDataKreICT: Expected response contained in received response");
								ExpectedResponseRecieved=true;
							}
						}

						SerialPortObj.StripLength(CurrentSerialData.length());
					}else if( (CurrentSerialData.contains(SerialPortObj.getExpectedDataErrorResult()) ) || ((CurrentSerialData.contains(SerialPortObj.getExpectedSetErrorResult()))) ){
						ApplicationLauncher.logger.info("SerialDataKreICT: Expected error response contained in received response2");
						FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_100A+":Expected:"+SerialPortObj.getExpectedResult()+" :Actual:"+CurrentSerialData);
						ErrorResponseRecieved = true;
						
					}else {
					
						ApplicationLauncher.logger.debug("SerialDataKreICT: Expected recieved: getExpectedDataErrorResult :"+SerialPortObj.getExpectedDataErrorResult()+"\n");
						ApplicationLauncher.logger.debug("SerialDataKreICT: Expected recieved: getExpectedSetErrorResult :"+SerialPortObj.getExpectedSetErrorResult()+"\n");
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("SerialDataKreICT unExpected received2:Current Data:"+CurrentSerialData+":Expected Data:"+SerialPortObj.getExpectedResult());
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("**********************************************************");
						FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_100B+":Expected:"+SerialPortObj.getExpectedResult()+" :Actual:"+CurrentSerialData);
						//SerialPortObj.StripLength(CurrentSerialData.length());
					}
				}
				responseWaitCount --;
				//ApplicationLauncher.logger.debug("KreIctTask: responseWaitCount: "+responseWaitCount);
				//ApplicationLauncher.logger.debug("KreIctTask: ExpectedResponseRecieved: "+ExpectedResponseRecieved);
				//ApplicationLauncher.logger.debug("KreIctTask: ErrorResponseRecieved: "+ErrorResponseRecieved);
				//ApplicationLauncher.logger.debug("KreIctTask: getUserAbortedFlag(): "+ProjectExecutionController.getUserAbortedFlag());
				if (responseWaitCount == 0 || ExpectedResponseRecieved ||
						ErrorResponseRecieved ||  
						ProjectExecutionController.getUserAbortedFlag()){
					responseWaitCount=0;
					ApplicationLauncher.logger.info("SerialDataKreICT :Timer Exit!%n");
					timer.cancel(); //Terminate the timer thread
				}
				else{
					timer.schedule(new KreIctTask(), RetryIntervalInMsec);
				}
			}



		}

	
	

}
