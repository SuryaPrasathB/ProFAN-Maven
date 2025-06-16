package com.tasnetwork.calibration.energymeter.message;

import java.util.ArrayList;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantLscsHarmonicsSourceSlave;
import com.tasnetwork.calibration.energymeter.device.SerialDataManager;

public class lscsPowerSourceHarmonicsMessage {

	  
	//=============================================================================================================
		//Added by Pradeep
	   public static ArrayList<Boolean> includeHarmonicsOrder_V1 = new ArrayList<Boolean>();
	   public static ArrayList<Integer> harmonicsOrderNumber_V1 = new ArrayList<Integer>();
	   public static ArrayList<Integer> phaseAngleDegreeHarmonicsOrder_V1 = new ArrayList<Integer>();
	   public static ArrayList<Integer> amplitudePercentageHarmonicsOrder_V1 = new ArrayList<Integer>();
		
	   public static ArrayList<Boolean> includeHarmonicsOrder_V2 = new ArrayList<Boolean>();
	   public static ArrayList<Integer> harmonicsOrderNumber_V2 = new ArrayList<Integer>();
	   public static ArrayList<Integer> phaseAngleDegreeHarmonicsOrder_V2 = new ArrayList<Integer>();
	   public static ArrayList<Integer> amplitudePercentageHarmonicsOrder_V2 = new ArrayList<Integer>();
		
	   public static ArrayList<Boolean> includeHarmonicsOrder_V3 = new ArrayList<Boolean>();
	   public static ArrayList<Integer> harmonicsOrderNumber_V3 = new ArrayList<Integer>();
	   public static ArrayList<Integer> phaseAngleDegreeHarmonicsOrder_V3 = new ArrayList<Integer>();
	   public static ArrayList<Integer> amplitudePercentageHarmonicsOrder_V3 = new ArrayList<Integer>();
		
	   public static ArrayList<Boolean> includeHarmonicsOrder_I1 = new ArrayList<Boolean>();
	   public static ArrayList<Integer> harmonicsOrderNumber_I1 = new ArrayList<Integer>();
	   public static ArrayList<Integer> phaseAngleDegreeHarmonicsOrder_I1 = new ArrayList<Integer>();
	   public static ArrayList<Integer> amplitudePercentageHarmonicsOrder_I1 = new ArrayList<Integer>();
		
	   public static ArrayList<Boolean> includeHarmonicsOrder_I2 = new ArrayList<Boolean>();
	   public static ArrayList<Integer> harmonicsOrderNumber_I2 = new ArrayList<Integer>();
	   public static ArrayList<Integer> phaseAngleDegreeHarmonicsOrder_I2 = new ArrayList<Integer>();
	   public static ArrayList<Integer> amplitudePercentageHarmonicsOrder_I2 = new ArrayList<Integer>();
		
	   public static ArrayList<Boolean> includeHarmonicsOrder_I3 = new ArrayList<Boolean>();
	   public static ArrayList<Integer> harmonicsOrderNumber_I3 = new ArrayList<Integer>();
	   public static ArrayList<Integer> phaseAngleDegreeHarmonicsOrder_I3 = new ArrayList<Integer>();
	   public static ArrayList<Integer> amplitudePercentageHarmonicsOrder_I3 = new ArrayList<Integer>();
		
		public static final char VOLTAGE_SIGNAL = 'V' ;
		public static final char CURRENT_SIGNAL = 'I' ;
		
		public static final char R_PHASE_ = '1' ;   
		public static final char Y_PHASE_ = '2' ;
		public static final char B_PHASE_ = '3' ;
		
		public static final String CMD_SEPERATOR  = ","; 
		public static final String PADDING_ZERO   = "0"; 
		
		public static final String R_PHASE  = "R"; 
		public static final String Y_PHASE  = "Y"; 
		public static final String B_PHASE  = "B"; 
		
		public static final String HEADER_VOLTAGE_R_PHASE = "V1";
		public static final String HEADER_VOLTAGE_Y_PHASE = "V2";
		public static final String HEADER_VOLTAGE_B_PHASE = "V3";
		public static final String HEADER_CURRENT_R_PHASE = "I1";
		public static final String HEADER_CURRENT_Y_PHASE = "I2";
		public static final String HEADER_CURRENT_B_PHASE = "I3";
		
		public static String harmonicsEnabledFrame = "" ;
		
		public static int frameNumber  = 0;
		public static ArrayList<String> dataFrames = new ArrayList<String>();
		
		public static boolean harmonicsEnabledInSignal_V1 = false ;
		public static boolean harmonicsEnabledInSignal_V2 = false ;
		public static boolean harmonicsEnabledInSignal_V3 = false ;
		public static boolean harmonicsEnabledInSignal_I1 = false ;
		public static boolean harmonicsEnabledInSignal_I2 = false ;
		public static boolean isHarmonicsEnabledInSignal_I2() {
			return harmonicsEnabledInSignal_I2;
		}


		public static void setHarmonicsEnabledInSignal_I2(boolean harmonicsEnabledInSignal_I2) {
			lscsPowerSourceHarmonicsMessage.harmonicsEnabledInSignal_I2 = harmonicsEnabledInSignal_I2;
		}


		public static boolean isHarmonicsEnabledInSignal_I3() {
			return harmonicsEnabledInSignal_I3;
		}


		public static void setHarmonicsEnabledInSignal_I3(boolean harmonicsEnabledInSignal_I3) {
			lscsPowerSourceHarmonicsMessage.harmonicsEnabledInSignal_I3 = harmonicsEnabledInSignal_I3;
		}


		public static boolean harmonicsEnabledInSignal_I3 = false ;
//=============================================================================================================
	


		public static void formDataFrames(){
			
			try{
				
			
				ApplicationLauncher.logger.info("formDataFrames :Entry");
				
				setFrameNumber(0);       // Resetting
				initialiseDataFrames();  // Initializing 
				
				formDataFrames_V1();
				ApplicationLauncher.logger.info("formDataFrames :Test1");
				formDataFrames_I1();
				ApplicationLauncher.logger.info("formDataFrames :Test2");
				formDataFrames_V2();
				ApplicationLauncher.logger.info("formDataFrames :test3");
				formDataFrames_I2();
				ApplicationLauncher.logger.info("formDataFrames :test4");
				formDataFrames_V3();
				ApplicationLauncher.logger.info("formDataFrames :test5");
				formDataFrames_I3();
				
				ApplicationLauncher.logger.debug("formDataFrames : frameNumber : " + getFrameNumber()  );
				ApplicationLauncher.logger.info("formDataFrames :Exit");
			}catch (Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("formDataFrames : Exception: " + e.getMessage());
			}
		}
		

//================== V1 ==================================================================================================	

		public static void formDataFrames_V1(){
			int i = 0;

			try {

				for( i = 0; i <= ConstantLscsHarmonicsSourceSlave.TOTAL_NO_OF_ORDER_HARMONICS_SUPPORTED; i++){

					//testing

					ApplicationLauncher.logger.debug("formDataFrames_V1 : i : " + i);

					includeHarmonicsOrder_V1.get(i);	
					ApplicationLauncher.logger.debug("includeHarmonicsOrder_V1.get(i)            : " + lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_V1.get(i));

					harmonicsOrderNumber_V1.get(i);
					ApplicationLauncher.logger.debug("harmonicsOrderNumber_V1.get(i)             : " + lscsPowerSourceHarmonicsMessage.harmonicsOrderNumber_V1.get(i));

					amplitudePercentageHarmonicsOrder_V1.get(i);
					ApplicationLauncher.logger.debug("amplitudePercentageHarmonicsOrder_V1.get(i): " + lscsPowerSourceHarmonicsMessage.amplitudePercentageHarmonicsOrder_V1.get(i));

					phaseAngleDegreeHarmonicsOrder_V1.get(i);
					ApplicationLauncher.logger.debug("phaseAngleDegreeHarmonicsOrder_V1.get(i)   : " + lscsPowerSourceHarmonicsMessage.phaseAngleDegreeHarmonicsOrder_V1.get(i));
					
					dataFrames.get(i) ; // just for checking
					ApplicationLauncher.logger.debug(" dataFrames.get(i)   : " + dataFrames.get(i));


					String dataFrame = "";
					if(includeHarmonicsOrder_V1.get(i)){
						dataFrame = HEADER_VOLTAGE_R_PHASE + CMD_SEPERATOR ;           // V1 
						if(i<10){
							dataFrame = dataFrame + PADDING_ZERO +  harmonicsOrderNumber_V1.get(i) + CMD_SEPERATOR ;
						}else{
							dataFrame = dataFrame + harmonicsOrderNumber_V1.get(i) + CMD_SEPERATOR ;
						}

						if(amplitudePercentageHarmonicsOrder_V1.get(i) < 10 ){
							dataFrame = dataFrame + PADDING_ZERO + PADDING_ZERO + amplitudePercentageHarmonicsOrder_V1.get(i) + CMD_SEPERATOR ;
						}else if(amplitudePercentageHarmonicsOrder_V1.get(i) < 100 ){
							dataFrame = dataFrame + PADDING_ZERO + amplitudePercentageHarmonicsOrder_V1.get(i) + CMD_SEPERATOR ;
						}else{
							dataFrame = dataFrame + amplitudePercentageHarmonicsOrder_V1.get(i) + CMD_SEPERATOR ;
						}

						if(phaseAngleDegreeHarmonicsOrder_V1.get(i) < 10 ){
							dataFrame = dataFrame + PADDING_ZERO + PADDING_ZERO + phaseAngleDegreeHarmonicsOrder_V1.get(i) + CMD_SEPERATOR ;
						}else if(phaseAngleDegreeHarmonicsOrder_V1.get(i) < 100 ){
							dataFrame = dataFrame + PADDING_ZERO + phaseAngleDegreeHarmonicsOrder_V1.get(i) + CMD_SEPERATOR ;
						}else{
							dataFrame = dataFrame + phaseAngleDegreeHarmonicsOrder_V1.get(i) + CMD_SEPERATOR ;
						}			

						ApplicationLauncher.logger.debug("formDataFrames_V1 : dataFrame : " + dataFrame  );

						setFrameNumber(getFrameNumber() + 1);
						dataFrames.set(getFrameNumber(), dataFrame);

						ApplicationLauncher.logger.debug("formDataFrames_V1 : dataFrames.get(frameNumber) : " + dataFrames.get(frameNumber)  );

					}
				}	
			}
			catch(Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("formDataFrames_V1: Exception: " + e.getMessage());
			}

		}

//=============================================================================================================================================================

//================== V2 ==================================================================================================	

		static void formDataFrames_V2(){
			int i = 0;
	
			for( i = 0; i <= ConstantLscsHarmonicsSourceSlave.TOTAL_NO_OF_ORDER_HARMONICS_SUPPORTED; i++){         
				String dataFrame = "";
				if(includeHarmonicsOrder_V2.get(i)){
					dataFrame = HEADER_VOLTAGE_Y_PHASE + CMD_SEPERATOR ;           // V2 
					if(i<10){
						dataFrame = dataFrame + PADDING_ZERO +  harmonicsOrderNumber_V2.get(i) + CMD_SEPERATOR ;
					}else{
						dataFrame = dataFrame +     harmonicsOrderNumber_V2.get(i) + CMD_SEPERATOR ;
					}

					if(amplitudePercentageHarmonicsOrder_V2.get(i) < 10 ){
						dataFrame = dataFrame + PADDING_ZERO + PADDING_ZERO + amplitudePercentageHarmonicsOrder_V2.get(i) + CMD_SEPERATOR ;
					}else if(amplitudePercentageHarmonicsOrder_V2.get(i) < 100 ){
						dataFrame = dataFrame + PADDING_ZERO + amplitudePercentageHarmonicsOrder_V2.get(i) + CMD_SEPERATOR ;
					}else{
						dataFrame = dataFrame + amplitudePercentageHarmonicsOrder_V2.get(i) + CMD_SEPERATOR ;
					}

					if(phaseAngleDegreeHarmonicsOrder_V2.get(i) < 10 ){
						dataFrame = dataFrame + PADDING_ZERO + PADDING_ZERO + phaseAngleDegreeHarmonicsOrder_V2.get(i) + CMD_SEPERATOR ;
					}else if(phaseAngleDegreeHarmonicsOrder_V2.get(i) < 100 ){
						dataFrame = dataFrame + PADDING_ZERO + phaseAngleDegreeHarmonicsOrder_V2.get(i) + CMD_SEPERATOR ;
					}else{
						dataFrame = dataFrame + phaseAngleDegreeHarmonicsOrder_V2.get(i) + CMD_SEPERATOR ;
					}			

					setFrameNumber(getFrameNumber() + 1);
					dataFrames.add(getFrameNumber(), dataFrame);
				}
			}
		}

//=============================================================================================================================================================

//================== V3 ==================================================================================================	

		static void formDataFrames_V3(){
			int i = 0;
			for( i = 0; i <= ConstantLscsHarmonicsSourceSlave.TOTAL_NO_OF_ORDER_HARMONICS_SUPPORTED; i++){         
				String dataFrame = "";
				if(includeHarmonicsOrder_V3.get(i)){
					dataFrame = HEADER_VOLTAGE_B_PHASE + CMD_SEPERATOR ;           // V3 
					if(i<10){
						dataFrame = dataFrame + PADDING_ZERO +  harmonicsOrderNumber_V3.get(i) + CMD_SEPERATOR ;
					}else{
						dataFrame = dataFrame +     harmonicsOrderNumber_V3.get(i) + CMD_SEPERATOR ;
					}

					if(amplitudePercentageHarmonicsOrder_V3.get(i) < 10 ){
						dataFrame = dataFrame + PADDING_ZERO + PADDING_ZERO + amplitudePercentageHarmonicsOrder_V3.get(i) + CMD_SEPERATOR ;
					}else if(amplitudePercentageHarmonicsOrder_V3.get(i) < 100 ){
						dataFrame = dataFrame + PADDING_ZERO + amplitudePercentageHarmonicsOrder_V3.get(i) + CMD_SEPERATOR ;
					}else{
						dataFrame = dataFrame + amplitudePercentageHarmonicsOrder_V3.get(i) + CMD_SEPERATOR ;
					}

					if(phaseAngleDegreeHarmonicsOrder_V3.get(i) < 10 ){
						dataFrame = dataFrame + PADDING_ZERO + PADDING_ZERO + phaseAngleDegreeHarmonicsOrder_V3.get(i) + CMD_SEPERATOR ;
					}else if(phaseAngleDegreeHarmonicsOrder_V3.get(i) < 100 ){
						dataFrame = dataFrame + PADDING_ZERO + phaseAngleDegreeHarmonicsOrder_V3.get(i) + CMD_SEPERATOR ;
					}else{
						dataFrame = dataFrame + phaseAngleDegreeHarmonicsOrder_V3.get(i) + CMD_SEPERATOR ;
					}			

					setFrameNumber(getFrameNumber() + 1);
					dataFrames.add(getFrameNumber(), dataFrame);
				}
			}
		}

//=============================================================================================================================================================

//================== I1 ==================================================================================================	

		static void formDataFrames_I1(){
			int i = 0;
			for( i = 0; i <= ConstantLscsHarmonicsSourceSlave.TOTAL_NO_OF_ORDER_HARMONICS_SUPPORTED; i++){         
				String dataFrame = "";
				if(includeHarmonicsOrder_I1.get(i)){
					dataFrame = HEADER_CURRENT_R_PHASE + CMD_SEPERATOR ;           // I1 
					if(i<10){
						dataFrame = dataFrame + PADDING_ZERO +  harmonicsOrderNumber_I1.get(i) + CMD_SEPERATOR ;
					}else{
						dataFrame = dataFrame +     harmonicsOrderNumber_I1.get(i) + CMD_SEPERATOR ;
					}

					if(amplitudePercentageHarmonicsOrder_I1.get(i) < 10 ){//String.format("%03d",amplitudePercentageHarmonicsOrder_I1.get(i));
						dataFrame = dataFrame + PADDING_ZERO + PADDING_ZERO + amplitudePercentageHarmonicsOrder_I1.get(i) + CMD_SEPERATOR ;
					}else if(amplitudePercentageHarmonicsOrder_I1.get(i) < 100 ){
						dataFrame = dataFrame + PADDING_ZERO + amplitudePercentageHarmonicsOrder_I1.get(i) + CMD_SEPERATOR ;
					}else{
						dataFrame = dataFrame + amplitudePercentageHarmonicsOrder_I1.get(i) + CMD_SEPERATOR ;
					}

					if(phaseAngleDegreeHarmonicsOrder_I1.get(i) < 10 ){
						dataFrame = dataFrame + PADDING_ZERO + PADDING_ZERO + phaseAngleDegreeHarmonicsOrder_I1.get(i) + CMD_SEPERATOR ;
					}else if(phaseAngleDegreeHarmonicsOrder_I1.get(i) < 100 ){
						dataFrame = dataFrame + PADDING_ZERO + phaseAngleDegreeHarmonicsOrder_I1.get(i) + CMD_SEPERATOR ;
					}else{
						dataFrame = dataFrame + phaseAngleDegreeHarmonicsOrder_I1.get(i) + CMD_SEPERATOR ;
					}			

					setFrameNumber(getFrameNumber() + 1);
					dataFrames.add(getFrameNumber(), dataFrame);
				}
			}
		}

//=============================================================================================================================================================

//================== I2 ==================================================================================================	

		static void formDataFrames_I2(){
			int i = 0;
			for( i = 0; i <= ConstantLscsHarmonicsSourceSlave.TOTAL_NO_OF_ORDER_HARMONICS_SUPPORTED; i++){         
				String dataFrame = "";
				if(includeHarmonicsOrder_I2.get(i)){
					dataFrame = HEADER_CURRENT_Y_PHASE + CMD_SEPERATOR ;           // I2 
					if(i<10){
						dataFrame = dataFrame + PADDING_ZERO +  harmonicsOrderNumber_I2.get(i) + CMD_SEPERATOR ;
					}else{
						dataFrame = dataFrame +     harmonicsOrderNumber_I2.get(i) + CMD_SEPERATOR ;
					}

					if(amplitudePercentageHarmonicsOrder_I2.get(i) < 10 ){
						dataFrame = dataFrame + PADDING_ZERO + PADDING_ZERO + amplitudePercentageHarmonicsOrder_I2.get(i) + CMD_SEPERATOR ;
					}else if(amplitudePercentageHarmonicsOrder_I2.get(i) < 100 ){
						dataFrame = dataFrame + PADDING_ZERO + amplitudePercentageHarmonicsOrder_I2.get(i) + CMD_SEPERATOR ;
					}else{
						dataFrame = dataFrame + amplitudePercentageHarmonicsOrder_I2.get(i) + CMD_SEPERATOR ;
					}

					if(phaseAngleDegreeHarmonicsOrder_I2.get(i) < 10 ){
						dataFrame = dataFrame + PADDING_ZERO + PADDING_ZERO + phaseAngleDegreeHarmonicsOrder_I2.get(i) + CMD_SEPERATOR ;
					}else if(phaseAngleDegreeHarmonicsOrder_I2.get(i) < 100 ){
						dataFrame = dataFrame + PADDING_ZERO + phaseAngleDegreeHarmonicsOrder_I2.get(i) + CMD_SEPERATOR ;
					}else{
						dataFrame = dataFrame + phaseAngleDegreeHarmonicsOrder_I2.get(i) + CMD_SEPERATOR ;
					}			

					setFrameNumber(getFrameNumber() + 1);
					dataFrames.add(getFrameNumber(), dataFrame);
				}
			}
		}

//=============================================================================================================================================================

//================== I3 ==================================================================================================	

		static void formDataFrames_I3(){
			int i = 0;
			for( i = 0; i <= ConstantLscsHarmonicsSourceSlave.TOTAL_NO_OF_ORDER_HARMONICS_SUPPORTED; i++){         
				String dataFrame = "";
				if(includeHarmonicsOrder_I3.get(i)){
					dataFrame = HEADER_CURRENT_B_PHASE + CMD_SEPERATOR ;           // I3 
					if(i<10){
						dataFrame = dataFrame + PADDING_ZERO +  harmonicsOrderNumber_I3.get(i) + CMD_SEPERATOR ;
					}else{
						dataFrame = dataFrame +     harmonicsOrderNumber_I3.get(i) + CMD_SEPERATOR ;
					}

					if(amplitudePercentageHarmonicsOrder_I3.get(i) < 10 ){
						dataFrame = dataFrame + PADDING_ZERO + PADDING_ZERO + amplitudePercentageHarmonicsOrder_I3.get(i) + CMD_SEPERATOR ;
					}else if(amplitudePercentageHarmonicsOrder_I3.get(i) < 100 ){
						dataFrame = dataFrame + PADDING_ZERO + amplitudePercentageHarmonicsOrder_I3.get(i) + CMD_SEPERATOR ;
					}else{
						dataFrame = dataFrame + amplitudePercentageHarmonicsOrder_I3.get(i) + CMD_SEPERATOR ;
					}

					if(phaseAngleDegreeHarmonicsOrder_I3.get(i) < 10 ){
						dataFrame = dataFrame + PADDING_ZERO + PADDING_ZERO + phaseAngleDegreeHarmonicsOrder_I3.get(i) + CMD_SEPERATOR ;
					}else if(phaseAngleDegreeHarmonicsOrder_I3.get(i) < 100 ){
						dataFrame = dataFrame + PADDING_ZERO + phaseAngleDegreeHarmonicsOrder_I3.get(i) + CMD_SEPERATOR ;
					}else{
						dataFrame = dataFrame + phaseAngleDegreeHarmonicsOrder_I3.get(i) + CMD_SEPERATOR ;
					}			

					setFrameNumber(getFrameNumber() + 1);
					dataFrames.add(getFrameNumber(), dataFrame);
				}
			}
		}

//=============================================================================================================================================================

//=============================================================================================================================================================

		public static void sendHarmonicsDataToSlave(){ 
			int i , j = 0 ;
			String frameToBeTransmitted ;
			char charToBeTransmitted ;
			for(i = 1; i<= frameNumber; i++){
				frameToBeTransmitted = "";
				frameToBeTransmitted = dataFrames.get(i);
				for(j = 0; j<= frameToBeTransmitted.length(); j++){
					charToBeTransmitted = frameToBeTransmitted.charAt(j) ;	
					// send charToBeTransmitted to slave 
				}
			}	
		}


		public static String getHarmonicsEnabledFrame() {
			return harmonicsEnabledFrame;
		}


		public static void setHarmonicsEnabledFrame(String harmonicsEnabledFrame) {
			lscsPowerSourceHarmonicsMessage.harmonicsEnabledFrame = harmonicsEnabledFrame;
		}
	
//=============================================================================================================================================================
	
		static void initialiseDataFrames(){
			ApplicationLauncher.logger.info("initialiseDataFrames :Entry");
			dataFrames.clear();
			for(int i =0 ; i < (ConstantLscsHarmonicsSourceSlave.TOTAL_NO_OF_ORDER_HARMONICS_SUPPORTED * ConstantLscsHarmonicsSourceSlave.TOTAL_NO_OF_SIGNALS);
					                    i++ ){
				dataFrames.add(i, "") ; 
			}
			
			/*for(int i=0; i< dataFrames.size(); i++){
			ApplicationLauncher.logger.info( i + ": " + dataFrames.get(i));
		    }*/
					
			ApplicationLauncher.logger.info("initialiseDataFrames :Exit");
		}


		public static int getFrameNumber() {
			return frameNumber;
		}


		public static void setFrameNumber(int frameNumber) {
			lscsPowerSourceHarmonicsMessage.frameNumber = frameNumber;
		}


		public static boolean isHarmonicsEnabledInSignal_V1() {
			return harmonicsEnabledInSignal_V1;
		}


		public static void setHarmonicsEnabledInSignal_V1(boolean harmonicsEnabledInSignal_V1) {
			lscsPowerSourceHarmonicsMessage.harmonicsEnabledInSignal_V1 = harmonicsEnabledInSignal_V1;
		}


		public static boolean isHarmonicsEnabledInSignal_V2() {
			return harmonicsEnabledInSignal_V2;
		}


		public static void setHarmonicsEnabledInSignal_V2(boolean harmonicsEnabledInSignal_V2) {
			lscsPowerSourceHarmonicsMessage.harmonicsEnabledInSignal_V2 = harmonicsEnabledInSignal_V2;
		}


		public static boolean isHarmonicsEnabledInSignal_V3() {
			return harmonicsEnabledInSignal_V3;
		}


		public static void setHarmonicsEnabledInSignal_V3(boolean harmonicsEnabledInSignal_V3) {
			lscsPowerSourceHarmonicsMessage.harmonicsEnabledInSignal_V3 = harmonicsEnabledInSignal_V3;
		}


		public static boolean isHarmonicsEnabledInSignal_I1() {
			return harmonicsEnabledInSignal_I1;
		}


		public static void setHarmonicsEnabledInSignal_I1(boolean harmonicsEnabledInSignal_I1) {
			lscsPowerSourceHarmonicsMessage.harmonicsEnabledInSignal_I1 = harmonicsEnabledInSignal_I1;
		}
		
//=============================================================================================================================================================
		
		/*public static void sendHarmonicsDataToSlave_V1(){ 
			int i , j = 0 ;
			String frameToBeTransmitted ;
			boolean status = false ;
		//	char charToBeTransmitted ;
			String expectedData = "" ;
					
					
			
			
			for(i = 1; i<= frameNumber; i++){
				frameToBeTransmitted = "";
				frameToBeTransmitted = dataFrames.get(i);
				
				if(frameToBeTransmitted.charAt(0)== VOLTAGE_SIGNAL){         //       R_PHASE_NUMBER VOLTAGE_SIGNAL
					if(frameToBeTransmitted.charAt(1)== R_PHASE_){
						expectedData = ConstantLscsHarmonicsSourceSlave.ER_PWR_HRM_SRC_SLAVE_V1_DATA ;
					}
					else if(frameToBeTransmitted.charAt(1)== Y_PHASE_){
						expectedData = ConstantLscsHarmonicsSourceSlave.ER_PWR_HRM_SRC_SLAVE_V2_DATA ;
					}
					else if(frameToBeTransmitted.charAt(1)== B_PHASE_){
						expectedData = ConstantLscsHarmonicsSourceSlave.ER_PWR_HRM_SRC_SLAVE_V3_DATA ;
					}
				}
				else if(frameToBeTransmitted.charAt(0)== CURRENT_SIGNAL){         //       R_PHASE_NUMBER VOLTAGE_SIGNAL
					if(frameToBeTransmitted.charAt(1)== R_PHASE_){
						expectedData = ConstantLscsHarmonicsSourceSlave.ER_PWR_HRM_SRC_SLAVE_I1_DATA ;
					}
					else if(frameToBeTransmitted.charAt(1)== Y_PHASE_){
						expectedData = ConstantLscsHarmonicsSourceSlave.ER_PWR_HRM_SRC_SLAVE_I1_DATA ;
					}
					else if(frameToBeTransmitted.charAt(1)== B_PHASE_){
						expectedData = ConstantLscsHarmonicsSourceSlave.ER_PWR_HRM_SRC_SLAVE_I1_DATA ;
					}
				}
			//	for(j = 0; j<= frameToBeTransmitted.length(); j++){
			//		charToBeTransmitted = frameToBeTransmitted.charAt(j) ;	
					// send charToBeTransmitted to slave 
			//	}
				
				status = SerialDataManager.lscsSendHarmonicsSourceSlaveCommand(frameToBeTransmitted, expectedData);
			}	
		}*/
//========================================================================================================================	
	
		
//=============================================================================================================
//============				
}

