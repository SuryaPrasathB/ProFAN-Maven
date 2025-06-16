package com.tasnetwork.calibration.energymeter.device;

import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantPowerSourceLscs;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdMte;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.deployment.FailureManager;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
import com.tasnetwork.calibration.energymeter.util.ErrorCodeMapping;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class SerialDataPowerSrc {

	Timer timer ;//= new Timer();
	volatile int responseWaitCount =10;
	static Communicator SerialPortObj= null;
	Boolean ExpectedResponseRecieved = false;
	Boolean ErrorResponseRecieved = false;
	int RetryIntervalInMsec=200;//1000;
	int numberOfReadingRequired = 1;
	int numberOfReadingObtained = 0;
	static String RefStd_ReadSerialData = "";
	Boolean feedBackControlReadyReceived = false;
	


	public Boolean IsExpectedErrorResponseReceived(){
		return this.ErrorResponseRecieved;

	}
	
	public SerialDataPowerSrc(Communicator inpSerialPortObj ){
		SerialPortObj = inpSerialPortObj;
		
	}
	public SerialDataPowerSrc( ){
		
	}
	
	public static String getRefStd_ReadSerialData() {
		// TODO Auto-generated method stub

		return RefStd_ReadSerialData = SerialPortObj.getSerialData();

	}
	
/*	public void clearExpectedResponseRecieved(){
		this.ErrorResponseRecieved = false;
	}*/

	public void SerialReponseTimerStartV2(int waitTimeCount, int noOfReadingRequired) {
		timer = new Timer();
		timer.schedule(new PowerSrcTaskV2(), RetryIntervalInMsec);
		responseWaitCount = waitTimeCount;
		setNumberOfReadingRequired( noOfReadingRequired);
		

	}

	public void SerialReponseTimerStart(Communicator inpSerialPortObj,int seconds) {
		timer = new Timer();
/*		ApplicationLauncher.logger.debug("SerialDataPowerSrc: SerialReponseTimerStart: Entry");
		try{
			timer.cancel();
		}catch (Exception e){
			ApplicationLauncher.logger.debug("SerialReponseTimerStart: Exception : "+e.getMessage());
		}
		timer = new Timer();*/
		timer.schedule(new PowerSrcTask(), RetryIntervalInMsec);
		responseWaitCount = seconds;
		ErrorResponseRecieved = false;
		setFeedBackControlReadyReceived(false);
		ApplicationLauncher.logger.debug("SerialReponseTimerStart: responseWaitCount: "+responseWaitCount);
		SerialPortObj = inpSerialPortObj;
	}
	
	
	public void SerialReponseTimerStartV2(Communicator inpSerialPortObj,int seconds) {
		timer = new Timer();
/*		ApplicationLauncher.logger.debug("SerialDataPowerSrc: SerialReponseTimerStart: Entry");
		try{
			timer.cancel();
		}catch (Exception e){
			ApplicationLauncher.logger.debug("SerialReponseTimerStart: Exception : "+e.getMessage());
		}
		timer = new Timer();*/
		timer.schedule(new PowerSrcTaskV1_1(), RetryIntervalInMsec);
		responseWaitCount = seconds;
		ErrorResponseRecieved = false;
		setFeedBackControlReadyReceived(false);
		ApplicationLauncher.logger.debug("SerialReponseTimerStartV2: responseWaitCount: "+responseWaitCount);
		SerialPortObj = inpSerialPortObj;
	}

	public Boolean IsExpectedResponseReceived(){
		ApplicationLauncher.logger.debug("SerialDataPowerSrc: IsExpectedResponseReceived: Entry");
		//ApplicationLauncher.logger.debug("SerialDataPowerSrc: responseWaitCount: "+responseWaitCount);
		//ApplicationLauncher.logger.debug("SerialDataPowerSrc: responseWaitCount: "+responseWaitCount);
		while((!ExpectedResponseRecieved)&&(responseWaitCount!=0) && (!ProjectExecutionController.getUserAbortedFlag()) ){
			ApplicationLauncher.logger.debug("SerialDataPowerSrc: while: Entry : responseWaitCount :" + responseWaitCount);
			try {
				Thread.sleep(500);
				//ApplicationLauncher.logger.debug("SerialDataPowerSrc: responseWaitCount2:" + responseWaitCount);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("PwrSrc: IsExpectedResponseReceived: InterruptedException:" + e.getMessage());
			}
			ApplicationLauncher.logger.info("SerialDataPowerSrc: Status:"+ExpectedResponseRecieved+" : "+responseWaitCount);
		};
		ApplicationLauncher.logger.debug("SerialDataPowerSrc: IsExpectedResponseReceived: Exit");
		return this.ExpectedResponseRecieved;
	}
	
	
	public Boolean IsExpectedResponseReceivedV2(){
		ApplicationLauncher.logger.debug("SerialDataPowerSrc: IsExpectedResponseReceivedV2: Entry");
		//ApplicationLauncher.logger.debug("SerialDataPowerSrc: responseWaitCount: "+responseWaitCount);
		//ApplicationLauncher.logger.debug("SerialDataPowerSrc: responseWaitCount: "+responseWaitCount);
		while((!ExpectedResponseRecieved)&&(responseWaitCount!=0) ) {// && (!ProjectExecutionController.getUserAbortedFlag()) ){
			ApplicationLauncher.logger.debug("SerialDataPowerSrc: while: Entry : responseWaitCount :" + responseWaitCount);
			try {
				Thread.sleep(500);
				//ApplicationLauncher.logger.debug("SerialDataPowerSrc: responseWaitCount2:" + responseWaitCount);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("PwrSrc: IsExpectedResponseReceivedV2: InterruptedException:" + e.getMessage());
			}
			ApplicationLauncher.logger.info("SerialDataPowerSrc: Status:"+ExpectedResponseRecieved+" : "+responseWaitCount);
		};
		ApplicationLauncher.logger.debug("SerialDataPowerSrc: IsExpectedResponseReceivedV2: Exit");
		return this.ExpectedResponseRecieved;
	}






	class PowerSrcTask extends TimerTask {
		public void run() {
			if(ProjectExecutionController.getUserAbortedFlag()) {
				responseWaitCount=0;
				ApplicationLauncher.logger.info("SerialDataPowerSrc:Timer Exit2!");
				timer.cancel(); //Terminate the timer thread
			}else {
				try{
					String CurrentSerialData = SerialPortObj.getSerialData();
					//ApplicationLauncher.logger.debug("PowerSrcTask: responseWaitCount: "+responseWaitCount);
					ApplicationLauncher.logger.debug("PowerSrcTask: SerialData:"+CurrentSerialData+"\n");
					String ExpectedResult = SerialPortObj.getExpectedResult();
					if(CurrentSerialData.equals(ExpectedResult) || CurrentSerialData.contains(ExpectedResult))
					{
						ApplicationLauncher.logger.info("SerialDataPowerSrc Expected received:\n");
						ApplicationLauncher.logger.debug("SerialDataPowerSrc: Expected recieved:Current Data:"+CurrentSerialData+"\n");
						SerialPortObj.StripLength(CurrentSerialData.length());
						ExpectedResponseRecieved=true; 
					}
					else if (CurrentSerialData.length()>0){
						if( (CurrentSerialData.equals(SerialPortObj.getExpectedDataErrorResult())) || (CurrentSerialData.equals(SerialPortObj.getExpectedSetErrorResult())) ){
							ApplicationLauncher.logger.info("SerialDataPowerSrc: Unable to set the parameter:Current Data1:"+CurrentSerialData+":Expected Data:"+SerialPortObj.getExpectedResult());
							FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_100+":Expected:"+SerialPortObj.getExpectedResult()+" :Actual:"+CurrentSerialData);
	
								if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_ENABLED){
									if(CurrentSerialData.contains(ConstantPowerSourceLscs.CMD_PWR_SRC_ER_FINE_CONTROL_READY)){
										setFeedBackControlReadyReceived(true);
									}
								}
							
							SerialPortObj.StripLength(CurrentSerialData.length());
							ErrorResponseRecieved = true;
						}else if(CurrentSerialData.length()>ExpectedResult.length()){
							ApplicationLauncher.logger.info("**********************************************************");
							ApplicationLauncher.logger.info("**********************************************************");
							ApplicationLauncher.logger.info("**********************************************************");
							ApplicationLauncher.logger.info("**********************************************************");
							ApplicationLauncher.logger.info("SerialDataPowerSrc unExpected received1:Current Data:"+CurrentSerialData+":Expected Data:"+SerialPortObj.getExpectedResult());
							ApplicationLauncher.logger.info("**********************************************************");
							ApplicationLauncher.logger.info("**********************************************************");
							ApplicationLauncher.logger.info("**********************************************************");
							ApplicationLauncher.logger.info("**********************************************************");
							if( (CurrentSerialData.contains(SerialPortObj.getExpectedDataErrorResult()) ) || ((CurrentSerialData.contains(SerialPortObj.getExpectedSetErrorResult()))) ){
								ApplicationLauncher.logger.info("SerialDataPowerSrc: Expected error response contained in received response");
								FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_100A+":Expected:"+SerialPortObj.getExpectedResult()+" :Actual:"+CurrentSerialData);
								ErrorResponseRecieved = true;
							}
							if(CurrentSerialData.contains(ExpectedResult)){
								ApplicationLauncher.logger.info("SerialDataPowerSrc: Expected response contained in received response");
								ExpectedResponseRecieved=true;
							}
		
							SerialPortObj.StripLength(CurrentSerialData.length());
						}else if( (CurrentSerialData.contains(SerialPortObj.getExpectedDataErrorResult()) ) || ((CurrentSerialData.contains(SerialPortObj.getExpectedSetErrorResult()))) ){
							ApplicationLauncher.logger.info("SerialDataPowerSrc: Expected error response contained in received response2");
							FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_100A+":Expected:"+SerialPortObj.getExpectedResult()+" :Actual:"+CurrentSerialData);
							ErrorResponseRecieved = true;
							
						}else {
						
							ApplicationLauncher.logger.debug("SerialDataPowerSrc: Expected recieved: getExpectedDataErrorResult :"+SerialPortObj.getExpectedDataErrorResult()+"\n");
							ApplicationLauncher.logger.debug("SerialDataPowerSrc: Expected recieved: getExpectedSetErrorResult :"+SerialPortObj.getExpectedSetErrorResult()+"\n");
							ApplicationLauncher.logger.info("**********************************************************");
							ApplicationLauncher.logger.info("**********************************************************");
							ApplicationLauncher.logger.info("**********************************************************");
							ApplicationLauncher.logger.info("**********************************************************");
							ApplicationLauncher.logger.info("SerialDataPowerSrc unExpected received2:Current Data:"+CurrentSerialData+":Expected Data:"+SerialPortObj.getExpectedResult());
							ApplicationLauncher.logger.info("**********************************************************");
							ApplicationLauncher.logger.info("**********************************************************");
							ApplicationLauncher.logger.info("**********************************************************");
							ApplicationLauncher.logger.info("**********************************************************");
							if(!FailureManager.getPowerSrcReasonForFailure().contains(ErrorCodeMapping.ERROR_CODE_100B)) {
								FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_100B+":Expected:"+SerialPortObj.getExpectedResult()+" :Actual:"+CurrentSerialData);
							}
							//FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_100B+":Expected:"+SerialPortObj.getExpectedResult()+" :Actual:"+CurrentSerialData);
							//SerialPortObj.StripLength(CurrentSerialData.length());
						}
					}
					responseWaitCount --;
					//ApplicationLauncher.logger.debug("PowerSrcTask: responseWaitCount: "+responseWaitCount);
					//ApplicationLauncher.logger.debug("PowerSrcTask: ExpectedResponseRecieved: "+ExpectedResponseRecieved);
					//ApplicationLauncher.logger.debug("PowerSrcTask: ErrorResponseRecieved: "+ErrorResponseRecieved);
					//ApplicationLauncher.logger.debug("PowerSrcTask: getUserAbortedFlag(): "+ProjectExecutionController.getUserAbortedFlag());
					if (responseWaitCount == 0 || ExpectedResponseRecieved ||
							ErrorResponseRecieved ||  
							ProjectExecutionController.getUserAbortedFlag()){
						//if(){
						//	ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_FEATURE_ENABLED
						//}
						responseWaitCount=0;
						ApplicationLauncher.logger.info("SerialDataPowerSrc:Timer Exit!%n");
						timer.cancel(); //Terminate the timer thread
					}
					else{
						timer.schedule(new PowerSrcTask(), RetryIntervalInMsec);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ApplicationLauncher.logger.error("PowerSrcTask: Exception: "+e.getMessage());
				}
			}
		}

		

	}
	
	class PowerSrcTaskV1_1 extends TimerTask {
		public void run() {
			
			try{
				String CurrentSerialData = SerialPortObj.getSerialData();
				//ApplicationLauncher.logger.debug("PowerSrcTaskV1_1: responseWaitCount: "+responseWaitCount);
				ApplicationLauncher.logger.debug("PowerSrcTaskV1_1: SerialData:"+CurrentSerialData+"\n");
				String ExpectedResult = SerialPortObj.getExpectedResult();
				if(CurrentSerialData.equals(ExpectedResult) || CurrentSerialData.contains(ExpectedResult))
				{
					ApplicationLauncher.logger.info("SerialDataPowerSrc Expected received:\n");
					ApplicationLauncher.logger.debug("SerialDataPowerSrc: Expected recieved:Current Data:"+CurrentSerialData+"\n");
					SerialPortObj.StripLength(CurrentSerialData.length());
					ExpectedResponseRecieved=true; 
				}
				else if (CurrentSerialData.length()>0){
					if( (CurrentSerialData.equals(SerialPortObj.getExpectedDataErrorResult())) || (CurrentSerialData.equals(SerialPortObj.getExpectedSetErrorResult())) ){
						ApplicationLauncher.logger.info("SerialDataPowerSrc: Unable to set the parameter:Current Data1:"+CurrentSerialData+":Expected Data:"+SerialPortObj.getExpectedResult());
						FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_100+":Expected:"+SerialPortObj.getExpectedResult()+" :Actual:"+CurrentSerialData);

							if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_ENABLED){
								if(CurrentSerialData.contains(ConstantPowerSourceLscs.CMD_PWR_SRC_ER_FINE_CONTROL_READY)){
									setFeedBackControlReadyReceived(true);
								}
							}
						
						SerialPortObj.StripLength(CurrentSerialData.length());
						ErrorResponseRecieved = true;
					}else if(CurrentSerialData.length()>ExpectedResult.length()){
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("SerialDataPowerSrc unExpected received1:Current Data:"+CurrentSerialData+":Expected Data:"+SerialPortObj.getExpectedResult());
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("**********************************************************");
						if( (CurrentSerialData.contains(SerialPortObj.getExpectedDataErrorResult()) ) || ((CurrentSerialData.contains(SerialPortObj.getExpectedSetErrorResult()))) ){
							ApplicationLauncher.logger.info("SerialDataPowerSrc: Expected error response contained in received response");
							FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_100A+":Expected:"+SerialPortObj.getExpectedResult()+" :Actual:"+CurrentSerialData);
							ErrorResponseRecieved = true;
						}
						if(CurrentSerialData.contains(ExpectedResult)){
							ApplicationLauncher.logger.info("SerialDataPowerSrc: Expected response contained in received response");
							ExpectedResponseRecieved=true;
						}
	
						SerialPortObj.StripLength(CurrentSerialData.length());
					}else if( (CurrentSerialData.contains(SerialPortObj.getExpectedDataErrorResult()) ) || ((CurrentSerialData.contains(SerialPortObj.getExpectedSetErrorResult()))) ){
						ApplicationLauncher.logger.info("SerialDataPowerSrc: Expected error response contained in received response2");
						FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_100A+":Expected:"+SerialPortObj.getExpectedResult()+" :Actual:"+CurrentSerialData);
						ErrorResponseRecieved = true;
						
					}else {
					
						ApplicationLauncher.logger.debug("SerialDataPowerSrc: Expected recieved: getExpectedDataErrorResult :"+SerialPortObj.getExpectedDataErrorResult()+"\n");
						ApplicationLauncher.logger.debug("SerialDataPowerSrc: Expected recieved: getExpectedSetErrorResult :"+SerialPortObj.getExpectedSetErrorResult()+"\n");
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("SerialDataPowerSrc unExpected received2:Current Data:"+CurrentSerialData+":Expected Data:"+SerialPortObj.getExpectedResult());
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("**********************************************************");
						ApplicationLauncher.logger.info("**********************************************************");
						FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_100B+":Expected:"+SerialPortObj.getExpectedResult()+" :Actual:"+CurrentSerialData);
						//SerialPortObj.StripLength(CurrentSerialData.length());
					}
				}
				responseWaitCount --;
				//ApplicationLauncher.logger.debug("PowerSrcTaskV1_1: responseWaitCount: "+responseWaitCount);
				//ApplicationLauncher.logger.debug("PowerSrcTaskV1_1: ExpectedResponseRecieved: "+ExpectedResponseRecieved);
				//ApplicationLauncher.logger.debug("PowerSrcTaskV1_1: ErrorResponseRecieved: "+ErrorResponseRecieved);
				//ApplicationLauncher.logger.debug("PowerSrcTaskV1_1: getUserAbortedFlag(): "+ProjectExecutionController.getUserAbortedFlag());
				if (responseWaitCount == 0 || ExpectedResponseRecieved ||
						ErrorResponseRecieved ) {//||  
						//ProjectExecutionController.getUserAbortedFlag()){
					//if(){
					//	ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_FEATURE_ENABLED
					//}
					responseWaitCount=0;
					ApplicationLauncher.logger.info("SerialDataPowerSrc:Timer Exit!%n");
					timer.cancel(); //Terminate the timer thread
				}
				else{
					timer.schedule(new PowerSrcTaskV1_1(), RetryIntervalInMsec);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("PowerSrcTaskV1_1: Exception: "+e.getMessage());
			}
		}

		

	}
	
	class PowerSrcTaskV2 extends TimerTask {
		public void run() {
			String CurrentSerialData = SerialPortObj.getSerialData();
			//ApplicationLauncher.logger.debug("PowerSrcTask: responseWaitCount: "+responseWaitCount);
			ApplicationLauncher.logger.debug("PowerSrcTaskV2: SerialData:"+CurrentSerialData+"\n");
			
			String ExpectedResult = SerialPortObj.getExpectedResult();
			//ApplicationLauncher.logger.debug("PowerSrcTaskV2: ExpectedResult:"+ExpectedResult+"\n");
			if(CurrentSerialData.equals(ExpectedResult) || CurrentSerialData.contains(ExpectedResult))
			{
				//if(CurrentSerialData.endsWith(ConstantMteRefStd.ER_TERMINATOR)) {
					
					int terminatorCount = StringUtils.countMatches(CurrentSerialData, ConstantPowerSourceLscs.ER_PWR_SRC_SEPERATOR);
					ApplicationLauncher.logger.debug("PowerSrcTaskV2 : terminatorCount1 :" + terminatorCount);
					//ApplicationLauncher.logger.debug("PowerSrcTaskV2 : terminatorCount1 :" + terminatorCount);
					if(terminatorCount >= getNumberOfReadingRequired()){
				
						ApplicationLauncher.logger.info("PowerSrcTaskV2:SerialDataPowerSrc Expected received:");
						//ApplicationLauncher.logger.debug("SerialDataPowerSrc: Expected recieved:Current Data:"+CurrentSerialData+"\n");
						//SerialPortObj.StripLength(CurrentSerialData.length());
						ExpectedResponseRecieved=true; 
					}
				//}
			}
			else if (CurrentSerialData.length()>0){
				if( (CurrentSerialData.equals(SerialPortObj.getExpectedDataErrorResult())) || (CurrentSerialData.equals(SerialPortObj.getExpectedSetErrorResult())) ){
					
					ApplicationLauncher.logger.info("PowerSrcTaskV2: Unable to set the parameter:Current Data2:"+CurrentSerialData+":Expected Data:"+SerialPortObj.getExpectedResult());
					FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_100+":Expected:"+SerialPortObj.getExpectedResult()+" :Actual:"+CurrentSerialData);
					SerialPortObj.StripLength(CurrentSerialData.length());
					ErrorResponseRecieved = true;
				}else if(CurrentSerialData.length()>ExpectedResult.length()){
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("PowerSrcTaskV2 unExpected received2:Current Data:"+CurrentSerialData+":Expected Data:"+SerialPortObj.getExpectedResult());
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					if( (CurrentSerialData.contains(SerialPortObj.getExpectedDataErrorResult()) ) || ((CurrentSerialData.contains(SerialPortObj.getExpectedSetErrorResult()))) ){
						ApplicationLauncher.logger.info("PowerSrcTaskV2: Expected error response contained in received response");
						FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_100A+":Expected:"+SerialPortObj.getExpectedResult()+" :Actual:"+CurrentSerialData);
						ErrorResponseRecieved = true;
					}
					if(CurrentSerialData.contains(ExpectedResult)){
						if(CurrentSerialData.endsWith(ConstantRefStdMte.ER_TERMINATOR)) {
							int terminatorCount = StringUtils.countMatches(CurrentSerialData, ConstantRefStdMte.ER_TERMINATOR);
							ApplicationLauncher.logger.debug("PowerSrcTaskV2 : terminatorCount2 :" + terminatorCount);
							
							if(terminatorCount >= getNumberOfReadingRequired()){
								ApplicationLauncher.logger.info("PowerSrcTaskV2: Expected response contained in received response");
								ExpectedResponseRecieved=true;
							}
						}
					}

					SerialPortObj.StripLength(CurrentSerialData.length());
				}else if( (CurrentSerialData.contains(SerialPortObj.getExpectedDataErrorResult()) ) || ((CurrentSerialData.contains(SerialPortObj.getExpectedSetErrorResult()))) ){
					ApplicationLauncher.logger.info("PowerSrcTaskV2: Expected error response contained in received response2");
					FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_100A+":Expected:"+SerialPortObj.getExpectedResult()+" :Actual:"+CurrentSerialData);
					ErrorResponseRecieved = true;
					
				}else {
				
					ApplicationLauncher.logger.debug("PowerSrcTaskV2: Expected recieved: getExpectedDataErrorResult :"+SerialPortObj.getExpectedDataErrorResult()+"\n");
					ApplicationLauncher.logger.debug("PowerSrcTaskV2: Expected recieved: getExpectedSetErrorResult :"+SerialPortObj.getExpectedSetErrorResult()+"\n");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("PowerSrcTaskV2 unExpected received2:Current Data:"+CurrentSerialData+":Expected Data:"+SerialPortObj.getExpectedResult());
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_100B+":Expected:"+SerialPortObj.getExpectedResult()+" :Actual:"+CurrentSerialData);
					//SerialPortObj.StripLength(CurrentSerialData.length());
				}
			}
			responseWaitCount --;
			//ApplicationLauncher.logger.debug("PowerSrcTaskV2: responseWaitCount: "+responseWaitCount);
			//ApplicationLauncher.logger.debug("PowerSrcTaskV2: ExpectedResponseRecieved: "+ExpectedResponseRecieved);
			//ApplicationLauncher.logger.debug("PowerSrcTaskV2: ErrorResponseRecieved: "+ErrorResponseRecieved);
			//ApplicationLauncher.logger.debug("PowerSrcTaskV2: getUserAbortedFlag(): "+ProjectExecutionController.getUserAbortedFlag());
			if (responseWaitCount == 0 || ExpectedResponseRecieved ||
					ErrorResponseRecieved ||  
					ProjectExecutionController.getUserAbortedFlag()){
				responseWaitCount=0;
				ApplicationLauncher.logger.info("SerialDataPowerSrc :Timer Exit!%n");
				timer.cancel(); //Terminate the timer thread
			}
			else{
				timer.schedule(new PowerSrcTaskV2(), RetryIntervalInMsec);
			}
		}



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
	
	public Boolean getFeedBackControlReadyReceived() {
		return feedBackControlReadyReceived;
	}

	public void setFeedBackControlReadyReceived(Boolean feedBackReadyReceived) {
		this.feedBackControlReadyReceived = feedBackReadyReceived;
	}


}
