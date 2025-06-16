package com.tasnetwork.calibration.energymeter.device;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantLduCcube;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;



public class SerialDataLDU {

	Timer timer;
	int responseWaitCount = 20;//initial 20, changed to 40 for lscs ldu stack
	int RetryIntervalInMsec=200;//1000;
	Communicator SerialPortObj= null;
	Boolean ExpectedResponseRecieved = false;
	static volatile String LDU_ReadSerialData = "";
	Integer ReceivedLength=0;
/*	private static ArrayList<String> ResultStatus = new ArrayList<String>(Collections.nCopies(13,"NOP"));
	private static ArrayList<String> ErrorValue=new ArrayList<String>(Collections.nCopies(13,"NOP"));
	private static ArrayList<String> noOfPulsesCounted=new ArrayList<String>(Collections.nCopies(13,"NOP"));
	private static ArrayList<Integer> receivedReadingIndex=new ArrayList<Integer>(Collections.nCopies(13,0));*/
	
	private static ArrayList<String> ResultStatus = new ArrayList<String>(Collections.nCopies(49,"NOP"));
	private static ArrayList<String> ErrorValue=new ArrayList<String>(Collections.nCopies(49,"NOP"));
	private static ArrayList<String> noOfPulsesCounted=new ArrayList<String>(Collections.nCopies(49,"NOP"));
	private static  ArrayList<Integer> receivedReadingIndex=new ArrayList<Integer>(Collections.nCopies(49,0));
	private static  ArrayList<Integer> lastReceivedReadingIndex=new ArrayList<Integer>(Collections.nCopies(49,-1));
	private static  ArrayList<Integer> noOfReceivedReading=new ArrayList<Integer>(Collections.nCopies(49,0));
	private static  ArrayList<String> averageReading=new ArrayList<String>(Collections.nCopies(49,""));
	
	SerialDataLDU(Communicator inpSerialPortObj){
		SerialPortObj = inpSerialPortObj;
	}
	public void SerialReponseTimerStart(int seconds) {
		timer = new Timer();
		if(ProcalFeatureEnable.CCUBE_LDU_CONNECTED) {
			timer.schedule(new LDU_Task(), RetryIntervalInMsec);
		}else if(ProcalFeatureEnable.LSCS_LDU_CONNECTED){
			timer.schedule(new lscsLDU_Task(), RetryIntervalInMsec);
		}
		responseWaitCount = seconds;
		ApplicationLauncher.logger.info("LDU SerialReponseTimerStart :LDU_Task Triggered:");
	}

	public Boolean IsExpectedResponseReceived(){

		while((!ExpectedResponseRecieved)&&(responseWaitCount!=0)){
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("LDU IsExpectedResponseReceived :Exception:"+ e.getMessage());
			}
		};
		return this.ExpectedResponseRecieved;
	}
	
	public static void setReceivedReadingIndex(int rcdIndex,int Address) {

		ApplicationLauncher.logger.info("setReceivedReadingIndex: "+ Address +":"+rcdIndex);
		receivedReadingIndex.set(Address , rcdIndex);

	}

	public static int getReceivedReadingIndex(int Address) {
		return receivedReadingIndex.get(Address);
	}
	
		
	public static void setLastReceivedReadingIndex(int rcdIndex,int Address) {

		ApplicationLauncher.logger.info("setLastReceivedReadingIndex: Address: "+ Address +":"+rcdIndex);
		lastReceivedReadingIndex.set(Address , rcdIndex);

	}

	public static int getLastReceivedReadingIndex(int Address) {
		return lastReceivedReadingIndex.get(Address);
	}
	
	public static int getNoOfReceivedReading(int Address) {
		return noOfReceivedReading.get(Address);
	}
	public static void setNoOfReceivedReading(int receivedReadingCount,int Address) {
		noOfReceivedReading.set(Address , receivedReadingCount);
	}
	
	public static void incrementNoOfReceivedReading(int Address) {
		noOfReceivedReading.set(Address , (getNoOfReceivedReading(Address)+1));
	}
	


	public static String getAverageReading(int Address) {
		return averageReading.get(Address);
	}
	public static void setAverageReading(String newReading,int Address) {
		averageReading.set(Address , newReading);
	}
	
	
	
	public static void lscsLduResetReadingIndex(){
		receivedReadingIndex=new ArrayList<Integer>(Collections.nCopies(49,0));
		lastReceivedReadingIndex=new ArrayList<Integer>(Collections.nCopies(49,-1));
		noOfReceivedReading=new ArrayList<Integer>(Collections.nCopies(49,0));
		averageReading=new ArrayList<String>(Collections.nCopies(49,""));
				
	}
	
	
	public void setReceivedLength(Integer length){

		this.ReceivedLength = length;
	}

	public Integer getReceivedLength(){

		return this.ReceivedLength;
	}


	class LDU_Task extends TimerTask {
		public void run() {
			String CurrentSerialData = SerialPortObj.getSerialData();
			ApplicationLauncher.logger.info("LDU_Task: CurrentSerialData:"+CurrentSerialData);
			Integer ExpectedLength= SerialPortObj.getExpectedLength();
			ApplicationLauncher.logger.info("getExpectedLength():"+SerialPortObj.getExpectedLength());
			if((CurrentSerialData.length() >= ExpectedLength) &&  (CurrentSerialData.length() <= ExpectedLength+6))
			{
				ApplicationLauncher.logger.info("LDU_Task: ExpectedData:"+SerialPortObj.getExpectedResult());
				String ExpectedResult = SerialPortObj.getExpectedResult();
				if (CurrentSerialData.contains(ExpectedResult)){
					ExpectedResponseRecieved=true;
					setLDU_ReadSerialData(CurrentSerialData);
					setReceivedLength(CurrentSerialData.length());
					ApplicationLauncher.logger.debug("SerialDataLDU Expected Data received:Length:"+CurrentSerialData.length()+":CurrentData:"+CurrentSerialData);
				}
				else{
					ApplicationLauncher.logger.info("LDU_Task: Expected Data not received:CurrentData:"+CurrentSerialData+":ExpectedData:"+SerialPortObj.getExpectedResult());
					SerialPortObj.StripLength(CurrentSerialData.length());
				}
			} else if (CurrentSerialData.length()>0){
				String ExpectedResult2 = SerialPortObj.getExpectedResult();
				if ((CurrentSerialData.contains(ExpectedResult2)) && (ExpectedResult2.length()>9)){
					ExpectedResponseRecieved=true;
					setLDU_ReadSerialData(CurrentSerialData);
					setReceivedLength(CurrentSerialData.length());
					ApplicationLauncher.logger.debug("SerialDataLDU Expected Data received:Length:"+CurrentSerialData.length()+":CurrentData:"+CurrentSerialData);
				}
				else{
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("LDU_Task: unExpected received:CurrentData:"+CurrentSerialData+":ExpectedData:"+SerialPortObj.getExpectedResult());
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					SerialPortObj.StripLength(CurrentSerialData.length());
				}
				
				


			}
			responseWaitCount --;
			if (responseWaitCount == 0 || ExpectedResponseRecieved  || ProjectExecutionController.getUserAbortedFlag()){
				ApplicationLauncher.logger.info("SerialDataLDU:Timer Exit:");
				timer.cancel(); //Terminate the timer thread
			}
			else{
				if(DeviceDataManagerController.getLDU_ReadDataFlag()){
					timer.schedule(new LDU_Task(), RetryIntervalInMsec);
				}
			}

		}
	
	}
	
	class lscsLDU_Task extends TimerTask {
		public void run() {
			String CurrentSerialData = SerialPortObj.getSerialData();
			ApplicationLauncher.logger.info("lscsLDU_Task: CurrentSerialData:"+CurrentSerialData);
			Integer ExpectedLength= SerialPortObj.getExpectedLength();
			ApplicationLauncher.logger.info("getExpectedLength():"+SerialPortObj.getExpectedLength());
			String ExpectedResult = SerialPortObj.getExpectedResult();
			if((CurrentSerialData.length() == ExpectedLength) )
			{
				ApplicationLauncher.logger.info("lscsLDU_Task: ExpectedData:"+SerialPortObj.getExpectedResult());
				//String ExpectedResult = SerialPortObj.getExpectedResult();
				if (ExpectedResult.equals("")){
					ExpectedResponseRecieved=true;
					setLDU_ReadSerialData(CurrentSerialData);
					setReceivedLength(CurrentSerialData.length());
					ApplicationLauncher.logger.debug("SerialDataLDU Expected Data received:Length1:"+CurrentSerialData.length()+":CurrentData:"+CurrentSerialData);
				}else if (CurrentSerialData.contains(ExpectedResult)){
					ExpectedResponseRecieved=true;
					setLDU_ReadSerialData(CurrentSerialData);
					setReceivedLength(CurrentSerialData.length());
					ApplicationLauncher.logger.debug("SerialDataLDU Expected Data received:Length2:"+CurrentSerialData.length()+":CurrentData:"+CurrentSerialData);
				}else{
					ApplicationLauncher.logger.info("lscsLDU_Task: Expected Data not received:CurrentData:"+CurrentSerialData+":ExpectedData:"+SerialPortObj.getExpectedResult());
					//if(ExpectedLength == ) {
					//SerialPortObj.StripLength(CurrentSerialData.length());
					//}
				}
			}else if(CurrentSerialData.length() == (ExpectedLength+2)) {
				ApplicationLauncher.logger.info("lscsLDU_Task: Test2");
				
				if (CurrentSerialData.contains(ExpectedResult)){
					ExpectedResponseRecieved=true;
					setLDU_ReadSerialData(CurrentSerialData);
					setReceivedLength(CurrentSerialData.length());
					ApplicationLauncher.logger.debug("SerialDataLDU Expected Data received:Length3:"+CurrentSerialData.length()+":CurrentData:"+CurrentSerialData);
				//ApplicationLauncher.logger.info("lscsLDU_Task: Expected Data not received:CurrentData:"+CurrentSerialData+":ExpectedData:"+SerialPortObj.getExpectedResult());
				}
				//SerialPortObj.StripLength(CurrentSerialData.length());
				//}
			}/* else if (CurrentSerialData.length()>0){
				String ExpectedResult2 = SerialPortObj.getExpectedResult();
				if (ExpectedResult2.equals("")){
					ExpectedResponseRecieved=true;
					setLDU_ReadSerialData(CurrentSerialData);
					setReceivedLength(CurrentSerialData.length());
					ApplicationLauncher.logger.debug("SerialDataLDU Expected Data received:Length3:"+CurrentSerialData.length()+":CurrentData:"+CurrentSerialData);
				}else if ((CurrentSerialData.contains(ExpectedResult2)) && (ExpectedResult2.length()>9)){
					ExpectedResponseRecieved=true;
					setLDU_ReadSerialData(CurrentSerialData);
					setReceivedLength(CurrentSerialData.length());
					ApplicationLauncher.logger.debug("SerialDataLDU Expected Data received:Length4:"+CurrentSerialData.length()+":CurrentData:"+CurrentSerialData);
				}else{
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("lscsLDU_Task: unExpected received:CurrentData:"+CurrentSerialData+":ExpectedData:"+SerialPortObj.getExpectedResult());
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					//SerialPortObj.StripLength(CurrentSerialData.length());
				}
				
				


			}*/
			responseWaitCount --;
			if (responseWaitCount == 0 || ExpectedResponseRecieved  || ProjectExecutionController.getUserAbortedFlag()){
				ApplicationLauncher.logger.info("SerialDataLDU:Timer Exit:");
				timer.cancel(); //Terminate the timer thread
			}
			else{
				if(DeviceDataManagerController.getLDU_ReadDataFlag()){
					timer.schedule(new lscsLDU_Task(), RetryIntervalInMsec);
				}
			}

		}
	}



	public static void setLDU_ReadSerialData(String currentSerialData) {
		// TODO Auto-generated method stub
		ApplicationLauncher.logger.debug("setLDU_ReadSerialData: Entry");
		LDU_ReadSerialData = currentSerialData;

	}

	public static void ClearLDU_ReadSerialData() {
		// TODO Auto-generated method stub
		ApplicationLauncher.logger.debug("ClearLDU_ReadSerialData: Entry");
		LDU_ReadSerialData = "";
		
		

	}

	public static String getLDU_ReadSerialData() {

		return LDU_ReadSerialData;

	}

	public void setLDU_ResultStatus(String ResultStatus,int Address) {

		ApplicationLauncher.logger.info("setLDU_ResultStatus: "+ Address +":"+ResultStatus);
		this.ResultStatus.set(Address , ResultStatus);

	}

	public String getLDU_ResultStatus(int Address) {
		return this.ResultStatus.get(Address);
	}

	public void setLDU_ErrorValue(String ErrorValue,int Address) {
		// TODO Auto-generated method stub

		this.ErrorValue.set(Address , ErrorValue);

	}



	public String getLDU_ErrorValue(int Address) {
		return this.ErrorValue.get(Address);

	}
	
	
	
	public void setNoOfPulsesCounted(String noOfPulseCounted,int Address) {
		// TODO Auto-generated method stub

		this.noOfPulsesCounted.set(Address , noOfPulseCounted);

	}



	public String getNoOfPulsesCounted(int Address) {
		return this.noOfPulsesCounted.get(Address);

	}



	public String LDU_DecodeSerialData() {
		// TODO Auto-generated method stub

		String DecodeData=this.LDU_ReadSerialData;
		DecodeData = DecodeData.replaceFirst("0F", "");
		DecodeData = DecodeData.replaceFirst("000F", "");
		DecodeData = DecodeData.replaceFirst("F0", "");
		DecodeData=DecodeData.replaceFirst("(.*)"+"00"+"$","$1"+"") ;
		try{
			if(DecodeData.length()>2){
				if(DecodeData.substring(0, 2).equals("21")) {
					//ApplicationLauncher.logger.info("SerialDataLDU:Decoded Serial Data2:"+DecodeData);
					DecodeData = DecodeData.substring(2);
					//ApplicationLauncher.logger.info("SerialDataLDU:Decoded Serial Data3:"+DecodeData);
					if(DecodeData.length()>=6){
						String AddressData = DecodeData.substring(0, 6);
						AddressData = ConstantLduCcube.DecodeHextoString(AddressData);
						int i_AddressData = Integer.parseInt(AddressData);
						if(DecodeData.length()>=6){
							DecodeData = DecodeData.substring(6);
							if(DecodeData.length()>=14){
								DecodeData = DecodeData.substring(0, Math.min(DecodeData.length(), 14));
								DecodeData = ConstantLduCcube.DecodeHextoString(DecodeData);
								if(DecodeData.length()>=1){
									setLDU_ResultStatus(DecodeData.substring(0, 1),i_AddressData);
								}else{
									ApplicationLauncher.logger.error("SerialDataLDU:Decoded Serial unable to set data status1");
									
								}
								if(DecodeData.length()>=7){
									setLDU_ErrorValue(DecodeData.substring(1, 7),i_AddressData);

								}else{
									ApplicationLauncher.logger.error("SerialDataLDU:Decoded Serial unable to set data value1");
									
								}

							}else if(DecodeData.length()>=12){
								DecodeData = DecodeData.substring(0, Math.min(DecodeData.length(), 12));
								DecodeData = ConstantLduCcube.DecodeHextoString(DecodeData);
								System.out.println("SerialDataLDU:Decoded Serial Data7:"+DecodeData.length());
								if(DecodeData.length()>=1){
									setLDU_ResultStatus(DecodeData.substring(0, 1),i_AddressData);
								//	System.out.println("setLDU_ResultStatus1:"+DecodeData.substring(0, 1));
								}else{
									ApplicationLauncher.logger.error("SerialDataLDU:Decoded Serial unable to set data status2");
									
								}
								if(DecodeData.length()>=5){
									setLDU_ErrorValue(DecodeData.substring(1, 5),i_AddressData);
									//System.out.println("setLDU_ResultStatus2:"+DecodeData.substring(1, 5));
									
								}else{
									ApplicationLauncher.logger.error("SerialDataLDU:Decoded Serial unable to set data value2");
									
								}
								
							}else{
								ApplicationLauncher.logger.error("SerialDataLDU:Decoded Serial unable to set data status and value 3");
								
							}
						}
					}
				}
			}
		} catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("LDU_DecodeSerialData :Exception:"+ e.getMessage());
			//System.out.printf("Exception on LDU_DecodeSerialData:", e.toString());
		}
		return DecodeData;

	}
	
	public String lscsLDU_DecodeSerialData(int i_AddressData) {
		// TODO Auto-generated method stub

		String DecodeData=this.LDU_ReadSerialData;
		String recievedIndex = "";
		ApplicationLauncher.logger.debug("SerialDataLDU: DecodeDataE:"+ DecodeData);
		//DecodeData = DecodeData.replaceFirst("0F", "");
		//DecodeData = DecodeData.replaceFirst("000F", "");
		//DecodeData = DecodeData.replaceFirst("F0", "");
		//DecodeData=DecodeData.replaceFirst("(.*)"+"00"+"$","$1"+"") ;
		try{
			if(DecodeData.length()>2){
				//if(DecodeData.substring(0, 2).equals("21")) {
					//ApplicationLauncher.logger.info("SerialDataLDU:Decoded Serial Data2:"+DecodeData);
					recievedIndex = DecodeData.substring(0, 2);
					ApplicationLauncher.logger.debug("SerialDataLDU: recievedIndex1:"+ recievedIndex);
					//recievedIndex =  Integer.parseInt(recievedIndex, 16);
				    /* if(!recievedIndex.equals("00")) {
						recievedIndex = ConstantCcubeLDU.DecodeHextoString(recievedIndex);
					}
					ApplicationLauncher.logger.debug("SerialDataLDU: recievedIndex2:"+ recievedIndex);*/
					ApplicationLauncher.logger.debug("SerialDataLDU: recievedIndex3:"+ Integer.parseInt(recievedIndex, 16));
					DecodeData = DecodeData.substring(2);// initial first byte which is sending the no of times it achieved the period
					//ApplicationLauncher.logger.info("SerialDataLDU:Decoded Serial Data3:"+DecodeData);
					//if(DecodeData.length()>=6){
					//	String AddressData = DecodeData.substring(0, 6);
					//	AddressData = ConstantCcubeLDU.DecodeHextoString(AddressData);
					//	int i_AddressData = Integer.parseInt(AddressData);
					//	if(DecodeData.length()>=6){
					//		DecodeData = DecodeData.substring(6);
							/*if(DecodeData.length()>=14){
								DecodeData = DecodeData.substring(0, Math.min(DecodeData.length(), //14));
								DecodeData = ConstantCcubeLDU.DecodeHextoString(DecodeData);
								if(DecodeData.length()>=1){
									setLDU_ResultStatus(DecodeData.substring(0, 1),i_AddressData);
								}else{
									ApplicationLauncher.logger.error("SerialDataLDU:Decoded Serial unable to set data status1");
									
								}
								if(DecodeData.length()>=7){
									setLDU_ErrorValue(DecodeData.substring(1, 7),i_AddressData);

								}else{
									ApplicationLauncher.logger.error("SerialDataLDU:Decoded Serial unable to set data value1");
									
								}

							}else */
					//int i_AddressData = 0;
							if(DecodeData.length()>=12){
								DecodeData = DecodeData.substring(0, Math.min(DecodeData.length(), 12));
								DecodeData = ConstantLduCcube.DecodeHextoString(DecodeData);
								ApplicationLauncher.logger.debug("SerialDataLDU:Decoded Serial Data7:"+DecodeData.length());
								if(DecodeData.length()>=1){
									ApplicationLauncher.logger.debug("SerialDataLDU:Decoded Serial Data8:"+DecodeData.substring(0, 1));
									//setLDU_ResultStatus(DecodeData.substring(0, 1),i_AddressData);
								//	System.out.println("setLDU_ResultStatus1:"+DecodeData.substring(0, 1));
								}else{
									ApplicationLauncher.logger.error("SerialDataLDU:Decoded Serial unable to set data status2");
									
								}
								if(DecodeData.length()>=5){
									ApplicationLauncher.logger.debug("SerialDataLDU:Decoded Serial Data9:"+DecodeData.substring(0, 6));
									//setLDU_ErrorValue(DecodeData.substring(1, 5),i_AddressData);
									if(DecodeData.substring(0, 6).equals("000000")){
										setLDU_ErrorValue("0",i_AddressData);
									}else{
										setLDU_ErrorValue(DecodeData.substring(0, 6),i_AddressData);
									}
									setReceivedReadingIndex(Integer.parseInt(recievedIndex, 16),i_AddressData);
									//System.out.println("setLDU_ResultStatus2:"+DecodeData.substring(1, 5));
									
								}else{
									ApplicationLauncher.logger.error("SerialDataLDU:Decoded Serial unable to set data value2");
									
								}
								
							}else{
								ApplicationLauncher.logger.error("SerialDataLDU:Decoded Serial unable to set data status and value 3");
								
							}
				//		}
				//	}
				//}
			}
		} catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("lscsLDU_DecodeSerialData :Exception:"+ e.getMessage());
			//System.out.printf("Exception on lscsLDU_DecodeSerialData:", e.toString());
		}
		return DecodeData;

	}

	public String lscsLDU_DecodeSerialDataForCreepOrSTA(int i_AddressData) {
		// TODO Auto-generated method stub

		String DecodeData=this.LDU_ReadSerialData;
		/*DecodeData = DecodeData.replaceFirst("0F", "");
		DecodeData = DecodeData.replaceFirst("000F", "");
		DecodeData = DecodeData.replaceFirst("F0", "");
		DecodeData=DecodeData.replaceFirst("(.*)"+"00"+"$","$1"+"") ;*/
		try{
			if(DecodeData.length()>2){
				//if(DecodeData.substring(0, 2).equals("21")) {
					DecodeData = DecodeData.substring(2);
					//if(DecodeData.length()>=6){
						//String AddressData = DecodeData.substring(0, 6);
						//AddressData = ConstantCcubeLDU.DecodeHextoString(AddressData);
						//int i_AddressData = Integer.parseInt(AddressData);
						ApplicationLauncher.logger.info("lscsLDU_DecodeSerialDataForCreepOrSTA :Decoded Serial DecodeData1:"+DecodeData);
						if(DecodeData.length()>=6){
							//DecodeData = DecodeData.substring(6);
							if(DecodeData.length() == 12){
								//DecodeData = DecodeData.substring(0, Math.min(DecodeData.length(), 10));
								DecodeData = ConstantLduCcube.DecodeHextoString(DecodeData);
								ApplicationLauncher.logger.info("lscsLDU_DecodeSerialDataForCreepOrSTA :Decoded Serial DecodeData2:"+DecodeData);
								DecodeData = String.valueOf(Integer.parseInt(DecodeData));
								ApplicationLauncher.logger.info("lscsLDU_DecodeSerialDataForCreepOrSTA :Decoded Serial DecodeData3:"+DecodeData);
								setNoOfPulsesCounted(DecodeData,i_AddressData);
/*								if(DecodeData.length()>=1){
									setLDU_ResultStatus(DecodeData.substring(0, 1),i_AddressData);
								}	
								if(DecodeData.length()>=5){
									setLDU_ErrorValue(DecodeData.substring(1, 5),i_AddressData);
								}*/
							}/*else if(DecodeData.length()<12){
								if(DecodeData.length()>=6){
									DecodeData = DecodeData.substring(0, Math.min(DecodeData.length(), 6));
									DecodeData = ConstantCcubeLDU.DecodeHextoString(DecodeData);
									if(DecodeData.length()>=1){
										setLDU_ResultStatus(DecodeData.substring(0, 1),i_AddressData);
									}	
									if(DecodeData.length()>=3){
										setLDU_ErrorValue(DecodeData.substring(1, 3),i_AddressData);
									}

								}
							}*/
						}
				//	}
				//}
			}
		} catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("lscsLDU_DecodeSerialDataForCreepOrSTA :Exception:"+ e.getMessage());
			//System.out.printf("Exception on LDU_DecodeSerialDataForCreepOrConst:", e.toString());
		}
		return DecodeData;

	}

	public String LDU_DecodeSerialDataForCreepOrConst() {
		// TODO Auto-generated method stub

		String DecodeData=this.LDU_ReadSerialData;
		DecodeData = DecodeData.replaceFirst("0F", "");
		DecodeData = DecodeData.replaceFirst("000F", "");
		DecodeData = DecodeData.replaceFirst("F0", "");
		DecodeData=DecodeData.replaceFirst("(.*)"+"00"+"$","$1"+"") ;
		try{
			if(DecodeData.length()>2){
				if(DecodeData.substring(0, 2).equals("21")) {
					DecodeData = DecodeData.substring(2);
					if(DecodeData.length()>=6){
						String AddressData = DecodeData.substring(0, 6);
						AddressData = ConstantLduCcube.DecodeHextoString(AddressData);
						int i_AddressData = Integer.parseInt(AddressData);
						ApplicationLauncher.logger.info("LDU_DecodeSerialDataForCreepOrConst :Decoded Serial AddressData:"+AddressData);
						if(DecodeData.length()>=6){
							DecodeData = DecodeData.substring(6);
							if(DecodeData.length() == 12){
								DecodeData = DecodeData.substring(0, Math.min(DecodeData.length(), 10));
								DecodeData = ConstantLduCcube.DecodeHextoString(DecodeData);
								if(DecodeData.length()>=1){
									setLDU_ResultStatus(DecodeData.substring(0, 1),i_AddressData);
								}	
								if(DecodeData.length()>=5){
									setLDU_ErrorValue(DecodeData.substring(1, 5),i_AddressData);
								}
							}else if(DecodeData.length()<12){
								if(DecodeData.length()>=6){
									DecodeData = DecodeData.substring(0, Math.min(DecodeData.length(), 6));
									DecodeData = ConstantLduCcube.DecodeHextoString(DecodeData);
									if(DecodeData.length()>=1){
										setLDU_ResultStatus(DecodeData.substring(0, 1),i_AddressData);
									}	
									if(DecodeData.length()>=3){
										setLDU_ErrorValue(DecodeData.substring(1, 3),i_AddressData);
									}

								}
							}
						}
					}
				}
			}
		} catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("LDU_DecodeSerialDataForCreepOrConst :Exception:"+ e.getMessage());
			//System.out.printf("Exception on LDU_DecodeSerialDataForCreepOrConst:", e.toString());
		}
		return DecodeData;

	}

	public String LDU_DecodeSerialDataForSTA() {
		// TODO Auto-generated method stub

		String DecodeData=this.LDU_ReadSerialData;
		DecodeData = DecodeData.replaceFirst("0F", "");
		DecodeData = DecodeData.replaceFirst("000F", "");
		DecodeData = DecodeData.replaceFirst("F0", "");
		DecodeData=DecodeData.replaceFirst("(.*)"+"00"+"$","$1"+"") ;
		try{
			if(DecodeData.length()>2){
				if(DecodeData.substring(0, 2).equals("21")) {
					DecodeData = DecodeData.substring(2);
					if(DecodeData.length()>=6){
						String AddressData = DecodeData.substring(0, 6);
						AddressData = ConstantLduCcube.DecodeHextoString(AddressData);
						int i_AddressData = Integer.parseInt(AddressData);
						ApplicationLauncher.logger.info("LDU_DecodeSerialDataForSTA:Decoded Serial AddressData:"+AddressData);
						if(DecodeData.length()>=6){
							DecodeData = DecodeData.substring(6);
							if(DecodeData.length() == 12){
								DecodeData = DecodeData.substring(0, Math.min(DecodeData.length(), 10));
								DecodeData = ConstantLduCcube.DecodeHextoString(DecodeData);
								if(DecodeData.length()>=1){
									setLDU_ResultStatus(DecodeData.substring(0, 1),i_AddressData);
								}	
								if(DecodeData.length()>=5){
									setLDU_ErrorValue(DecodeData.substring(1, 5),i_AddressData);
								}

							}else if(DecodeData.length()<12){
								if(DecodeData.length()>=6){
									DecodeData = DecodeData.substring(0, Math.min(DecodeData.length(), 6));
									DecodeData = ConstantLduCcube.DecodeHextoString(DecodeData);
									if(DecodeData.length()>=1){
										setLDU_ResultStatus(DecodeData.substring(0, 1),i_AddressData);
									}	
									if(DecodeData.length()>=3){
										setLDU_ErrorValue(DecodeData.substring(1, 3),i_AddressData);
									}

								}
							}
						}
					}
				}
			}
		} catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("LDU_DecodeSerialDataForSTA :Exception:"+ e.getMessage());
			//System.out.printf("Exception on LDU_DecodeSerialDataForSTA:", e.toString());
		}
		return DecodeData;

	}



}

