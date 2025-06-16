package com.tasnetwork.calibration.energymeter.device;

import java.text.DecimalFormat;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
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
import com.tasnetwork.calibration.energymeter.deployment.BofaManager;
import com.tasnetwork.calibration.energymeter.deployment.DisplayInstantMetricsController;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
import com.tasnetwork.calibration.energymeter.messenger.BofaRequestProcessor;
import com.tasnetwork.calibration.energymeter.messenger.PowerSourceBofaMessenger;
import com.tasnetwork.calibration.energymeter.serial.portmanagerV2.SerialPortManagerPwrSrc_V2;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;

public class Data_RefStdBofa {

	static String actualOutputValueReadingCmdFrame = "" ;
	static String readTheConstantOfLiveReferenceMeterCmdFrame = "" ;
	static String switchPulseRefMeterToPuleRefClkCmdFrame = "" ;
	static String readFrequencyCmdFrame = "" ;

	public static volatile boolean rsmFreqReadingRequired = true;
	public static SerialDataManager SerialDM_Obj = new SerialDataManager();

	static volatile long liveRefMeterConstant = 0;

	static volatile float refStdActiveEnergyAccumulatedInKwh = 0.0f;

	static float rsmActualFreqValue = 0.0f ;

	private static boolean readEnergyAccumulateEnabled  = false;
	//static String formattedRsmActualFreqValue = "" ;
	private static int SerialRefComInstantMetricsRefreshDefaultTimeInMsec = 800; 
	private static int SerialRefComInstantMetricsRefreshTimeInMsec = 800;//2000;
	DeviceDataManagerController displayDataObj =  new DeviceDataManagerController();
	Timer RefStdTimer;
	//public static volatile boolean RefStdComSemlock = true;

	public static DecimalFormat pfFormatter = new DecimalFormat("0.0000");
	public static DecimalFormat frequencyFormatter = new DecimalFormat("00.000");
	public static DecimalFormat current1_AmpsLessFormatter = new DecimalFormat("0.0000");
	public static DecimalFormat current100_AmpsLessFormatter = new DecimalFormat("00.000");
	public static DecimalFormat current100_AmpsGreaterFormatter = new DecimalFormat("000.00");

	public Data_RefStdBofa(){
		frameActualOutputValueReadingCommand();
		frameFrequencyReadingCommand();
		frameReadTheConstantOfLiveReferenceMeter();
	}

	public void bofaRefStdTimerInit() {
		RefStdTimer = new Timer();
		ApplicationLauncher.logger.debug("bofaRefStdTimerInit: Entry");
		//	RefStdComSemlock = true;

		if(ProcalFeatureEnable.BOFA_QUEUE_MESSENGER) {
			RefStdTimer.schedule(new BofaRefComRemindTaskWithQueue(),SerialRefComInstantMetricsRefreshTimeInMsec); // 2000);
		}else {
			RefStdTimer.schedule(new BofaRefComRemindTask(),SerialRefComInstantMetricsRefreshTimeInMsec); // 2000);
		}
		
		boolean pwrSrcConnectedStatus = false;
		if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
			pwrSrcConnectedStatus = SerialPortManagerPwrSrc_V2.isPwrSrcSerialStatusConnected();
		}else {
			pwrSrcConnectedStatus = BofaManager.getSerialDM_Obj().powerSourceSerialStatusConnected;
		}
		//if(BofaManager.getSerialDM_Obj().powerSourceSerialStatusConnected){
		if(pwrSrcConnectedStatus){
			//ApplicationLauncher.logger.info("RefStdTimerInit:SETTING FLAG");
			ApplicationLauncher.logger.info("bofaRefStdTimerInit :setRefStdReadDataFlag to true");
			displayDataObj.setRefStdReadDataFlag( true);
		}
	}

	class BofaRefComRemindTask extends TimerTask {
		public void run() {
			ApplicationLauncher.logger.info("BofaRefComRemindTask: Entry");
			//ApplicationLauncher.logger.info("RefComRemindTask: getRefStdReadDataFlag:" + DisplayDataObj.getRefStdReadDataFlag());
			//ApplicationLauncher.logger.info("RefComRemindTask: refComSerialStatusConnected:" + refComSerialStatusConnected);
			boolean userAborted = false;
			//if (displayDataObj.getRefStdReadDataFlag() && BofaManager.getSerialDM_Obj().powerSourceSerialStatusConnected){//refComSerialStatusConnected){fbxvdc
			boolean refStdConnected =  false;
			if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
				PowerSourceBofaMessenger pwrSrcBofaMessenger = new PowerSourceBofaMessenger();
				refStdConnected = pwrSrcBofaMessenger.getPwrSrcSpmObj().isPwrSrcSerialStatusConnected();//DeviceDataManagerController().getSerialPortManagerPwrSrc_V2();
			}else{
				refStdConnected = BofaManager.getSerialDM_Obj().powerSourceSerialStatusConnected;
			}
			if (refStdConnected){//refComSerialStatusConnected){fbxvdc
				ApplicationLauncher.logger.info("BofaRefComRemindTask: refStdConnected");
				if (displayDataObj.getRefStdReadDataFlag()){
					ApplicationLauncher.logger.info("BofaRefComRemindTask: getRefStdReadDataFlag");
					try {
						//===================================	
						//boolean success = false;
						int retries = 75;
						boolean success = false;
						

						while ((!userAborted) && (!success) && (retries > 0)) {
							try {
								ApplicationLauncher.logger.debug("BofaRefComRemindTask : comPortSemaphore : Semaphore <"+retries +"> : tryAcquire...");
								ApplicationLauncher.logger.debug("BofaRefComRemindTask : comPortSemaphore : availablePermits : " + BofaManager.comPortSemaphore.availablePermits());
								if (BofaManager.comPortSemaphore.tryAcquire(1)) { // Try to Acquire the semaphore
									ApplicationLauncher.logger.debug("BofaRefComRemindTask : comPortSemaphore : Semaphore <"+retries +"> : acquired");
									if( (!ProjectExecutionController.getAll_TestPoint_CompletedStatus()) ){
										//	&& (!ProjectExecutionController.getExecuteStopStatus()) ){
										success = Data_RefStdBofa.sendActualOutputValueReadingCommand(); 	 // these 2 process takes 2 seconds 
									}else{
										userAborted = true;//
										ApplicationLauncher.logger.debug("BofaRefComRemindTask : Exiting-1 Loop");
									}
									// success = true;
									if(success){
										//ApplicationLauncher.logger.debug("BofaRefComRemindTask : getFormattedRsmActualFreqValue:" +getFormattedRsmActualFreqValue());
										//ApplicationLauncher.logger.debug("BofaRefComRemindTask : getFormattedRsmActualFreqValue float:" + Float.parseFloat(getFormattedRsmActualFreqValue()) );

										//ApplicationLauncher.logger.debug("BofaRefComRemindTask : get_PwrSrc_Freq float:" +displayDataObj.get_PwrSrc_Freq());

										//if(getFormattedRsmActualFreqValue().equals(displayDataObj.get_PwrSrc_Freq())) {
										if(isRsmFreqReadingRequired()) {
											Sleep(1);
											if( (!ProjectExecutionController.getAll_TestPoint_CompletedStatus())
												&& (!ProjectExecutionController.getExecuteStopStatus()) ){
												Data_RefStdBofa.sendFrequencyReadingCommand();
											}
										}
										/*if(SerialDM_Obj.getReadRefReadingStatus()){
											ApplicationLauncher.logger.debug("BofaRefComRemindTask : Dial Test Energy Read");
											Data_LduBofa.sendReadRefMeterPulseNumCmd(1);
										}else{
											ApplicationLauncher.logger.debug("BofaRefComRemindTask : Dial Test Energy Read skipped");
										}*/
										//}
									}

									/*if(isReadEnergyAccumulateEnabled()){  
										ApplicationLauncher.logger.debug("BofaRefComRemindTask : Dial Test Energy Read");
										Data_LduBofa.sendReadRefMeterPulseNumCmd(1);
										//Sleep(200);
									}else{
										ApplicationLauncher.logger.debug("BofaRefComRemindTask : Dial Test Energy Read not required");
									}*/
									
									
									
									
									//BofaManager.comPortSemaphore.drainPermits();
									BofaManager.comPortSemaphore.release(1); // Release the semaphore 
									
									
									
									
									
									ApplicationLauncher.logger.debug("BofaRefComRemindTask : comPortSemaphore: Semaphore : released");
									ApplicationLauncher.logger.debug("BofaRefComRemindTask : comPortSemaphore : availablePermits : " + BofaManager.comPortSemaphore.availablePermits());
									
									Sleep(200);
									if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
										ProjectExecutionController.UpdateRefStdProgressBar();
									}
									/*									if(isReadEnergyAccumulateEnabled()){  
										ApplicationLauncher.logger.debug("BofaRefComRemindTask : Dial Test Energy Read");
										Data_LduBofa.sendReadRefMeterPulseNumCmd(1);
										Sleep(200);
									}else{
										ApplicationLauncher.logger.debug("BofaRefComRemindTask : Dial Test Energy Read skipped");
									}*/
									if(isReadEnergyAccumulateEnabled()){  
										ApplicationLauncher.logger.debug("BofaRefComRemindTask : Dial Test Energy Read");
										if( (!ProjectExecutionController.getAll_TestPoint_CompletedStatus())
												&& (!ProjectExecutionController.getExecuteStopStatus()) ){
											Data_LduBofa.sendReadRefMeterPulseNumCmd(1);
										}
										Sleep(200);
									}/*else{
										ApplicationLauncher.logger.debug("BofaRefComRemindTask : Dial Test Energy Read not required");
									}*/
								}
								else { 
									ApplicationLauncher.logger.debug("BofaRefComRemindTask : comPortSemaphore: Semaphore not Aquired");
								}
							} catch (Exception e) {
								e.printStackTrace();
								
								ApplicationLauncher.logger.error("BofaRefComRemindTask : Exception :" + e.getMessage());
								BofaManager.comPortSemaphore.release(1);
								ApplicationLauncher.logger.debug("BofaRefComRemindTask : Exception: comPortSemaphore : Semaphore : released");
								ApplicationLauncher.logger.debug("BofaRefComRemindTask : Exception: comPortSemaphore : availablePermits : " + BofaManager.comPortSemaphore.availablePermits());
								
							} 

							//if (!success) {
							//	success = false;
								retries--;
								Sleep(200);
							//}
							
							boolean pwrSrcConnectedStatus = false;
							if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
								pwrSrcConnectedStatus = SerialPortManagerPwrSrc_V2.isPwrSrcSerialStatusConnected();
							}else {
								pwrSrcConnectedStatus = BofaManager.getSerialDM_Obj().powerSourceSerialStatusConnected;
							}
							//if (!displayDataObj.getRefStdReadDataFlag() || (!BofaManager.getSerialDM_Obj().powerSourceSerialStatusConnected)){//refComSerialStatusConnected){fbxvdc
							if (!displayDataObj.getRefStdReadDataFlag() || (!pwrSrcConnectedStatus)){
								userAborted= true;
								ApplicationLauncher.logger.debug("BofaRefComRemindTask : Exiting-2 Loop");
							}

						}

						if (retries == 0) {
							ApplicationLauncher.logger.debug("BofaRefComRemindTask : Max retries reached");
						}
						//=============================================

						//kreReadRefStdAllData(); 


						//RefStdComSemlock = true;
						Sleep(200);// added delay to allow the Ref std BNC Constant task 
						//ApplicationLauncher.logger.debug("RefComRemindTask:ReadRefStdData: RefComSemlock: Released");
						//	}
					} catch (Exception e1){
						e1.printStackTrace();
						ApplicationLauncher.logger.error("BofaRefComRemindTask :Exception1:"+ e1.getMessage());

					}
					RefStdTimer.schedule(new BofaRefComRemindTask(), SerialRefComInstantMetricsRefreshTimeInMsec);//SerialRefComKreInstantMetricsRefreshTimeInMsec);

				}else{
					if(!ProjectExecutionController.getUserAbortedFlag()){
						if( (!ProjectExecutionController.getAll_TestPoint_CompletedStatus())
								&& (!ProjectExecutionController.getExecuteStopStatus()) ){
						ApplicationLauncher.logger.debug("BofaRefComRemindTask : renewal2");

						RefStdTimer.schedule(new BofaRefComRemindTask(), SerialRefComInstantMetricsRefreshTimeInMsec);//SerialRefComKreInstantMetricsRefreshTimeInMsec);
					
						}
					}
				}
			}
			else{

				ApplicationLauncher.logger.debug("BofaRefComRemindTask :Timer Exit!%n");
				RefStdTimer.cancel(); //Terminate the timer thread



			}
		}
	}
	
	
	
	class BofaRefComRemindTaskWithQueue extends TimerTask {
		public void run() {
			ApplicationLauncher.logger.info("BofaRefComRemindTaskWithQueue: Entry");
			boolean userAborted = false;
			boolean refStdConnected =  false;
			if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
				PowerSourceBofaMessenger pwrSrcBofaMessenger = new PowerSourceBofaMessenger();
				refStdConnected = pwrSrcBofaMessenger.getPwrSrcSpmObj().isPwrSrcSerialStatusConnected();//DeviceDataManagerController().getSerialPortManagerPwrSrc_V2();
			}else{
				refStdConnected = BofaManager.getSerialDM_Obj().powerSourceSerialStatusConnected;
			}
			if (refStdConnected){//refComSerialStatusConnected){fbxvdc
				ApplicationLauncher.logger.info("BofaRefComRemindTaskWithQueue: refStdConnected");
				if (displayDataObj.getRefStdReadDataFlag()){
					ApplicationLauncher.logger.info("BofaRefComRemindTaskWithQueue: getRefStdReadDataFlag");
					try {
						
						int retries = 75;
						boolean success = false;
						

						//while ((!userAborted) && (!success) && (retries > 0)) {
							try {
								//ApplicationLauncher.logger.debug("BofaRefComRemindTaskWithQueue : comPortSemaphore : Semaphore <"+retries +"> : tryAcquire...");
								//ApplicationLauncher.logger.debug("BofaRefComRemindTaskWithQueue : comPortSemaphore : availablePermits : " + BofaManager.comPortSemaphore.availablePermits());
								//if (BofaManager.comPortSemaphore.tryAcquire(1)) { // Try to Acquire the semaphore
									//ApplicationLauncher.logger.debug("BofaRefComRemindTaskWithQueue : comPortSemaphore : Semaphore <"+retries +"> : acquired");
									if( (!ProjectExecutionController.getAll_TestPoint_CompletedStatus()) ){
											//success = Data_RefStdBofa.sendActualOutputValueReadingCommand(); 	 // these 2 process takes 2 seconds 
											
											//Runnable taskActualOutputValueReading = () -> {
												
											//};
											
											
											Callable<Boolean> taskActualOutputValueReading = () -> {
												boolean readCommandSuccess = Data_RefStdBofa.sendActualOutputValueReadingCommand();
									            return readCommandSuccess;
									        };
											int priority = 10;//10= low // 1 is low , 100 is high
											long timeoutInSec = 10;//5;//for 5 - concurrent issue occured
											Future<Boolean> future = BofaRequestProcessor.addRequest(ConstantPowerSourceBofa.BOFA_COM_PORT_KEY, taskActualOutputValueReading, 5, timeoutInSec, TimeUnit.SECONDS);

											// Option 1: Wait for it to complete, but with your own timeout
											try {
												success = future.get(timeoutInSec, TimeUnit.SECONDS); // This waits for result
											    ApplicationLauncher.logger.debug("BofaRefComRemindTaskWithQueue : sendActualOutput: Task completed: " + success);
											} catch (TimeoutException e) {
												ApplicationLauncher.logger.debug("BofaRefComRemindTaskWithQueue : sendActualOutput : Task timed out (from caller side)");
											} catch (ExecutionException e) {
												ApplicationLauncher.logger.debug("BofaRefComRemindTaskWithQueue : sendActualOutput : Task failed: " + e.getCause());
											} catch (InterruptedException e) {
											    Thread.currentThread().interrupt();
											}
									}else{
										userAborted = true;//
										ApplicationLauncher.logger.debug("BofaRefComRemindTaskWithQueue : Exiting-1 Loop");
									}
									
									if(success){
										if(isRsmFreqReadingRequired()) {
											Sleep(1);
											if( (!ProjectExecutionController.getAll_TestPoint_CompletedStatus())
												&& (!ProjectExecutionController.getExecuteStopStatus()) ){
												//Data_RefStdBofa.sendFrequencyReadingCommand();
												
												Callable<String> taskSendFrequencyReading = () -> {
													Data_RefStdBofa.sendFrequencyReadingCommand();
										            return "Finished task!";
										        };
												int priority = 2;//10= low // 1 is low , 100 is high
												long timeoutInSec = 10;//5;for 5 - concurrent issue occured
												Future<String> future = BofaRequestProcessor.addRequest(ConstantPowerSourceBofa.BOFA_COM_PORT_KEY, taskSendFrequencyReading, 5, timeoutInSec, TimeUnit.SECONDS);

												// Option 1: Wait for it to complete, but with your own timeout
												try {
												    String result = future.get(timeoutInSec, TimeUnit.SECONDS); // This waits for result
												    ApplicationLauncher.logger.debug("BofaRefComRemindTaskWithQueue : sendFrequencyReading : Task completed: " + result);
												} catch (TimeoutException e) {
													ApplicationLauncher.logger.debug("BofaRefComRemindTaskWithQueue : sendFrequencyReading : Task timed out (from caller side)");
												} catch (ExecutionException e) {
													ApplicationLauncher.logger.debug("BofaRefComRemindTaskWithQueue : sendFrequencyReading : Task failed: " + e.getCause());
												} catch (InterruptedException e) {
												    Thread.currentThread().interrupt();
												}
											}
										}
										
									}

									///BofaManager.comPortSemaphore.release(1); // Release the semaphore 
									
									//ApplicationLauncher.logger.debug("BofaRefComRemindTaskWithQueue : comPortSemaphore: Semaphore : released");
									//ApplicationLauncher.logger.debug("BofaRefComRemindTaskWithQueue : comPortSemaphore : availablePermits : " + BofaManager.comPortSemaphore.availablePermits());
									
									Sleep(200);
									if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
										ProjectExecutionController.UpdateRefStdProgressBar();
									}
									
									if(isReadEnergyAccumulateEnabled()){  
										ApplicationLauncher.logger.debug("BofaRefComRemindTaskWithQueue : Dial Test Energy Read");
										if( (!ProjectExecutionController.getAll_TestPoint_CompletedStatus())
												&& (!ProjectExecutionController.getExecuteStopStatus()) ){
											Data_LduBofa.sendReadRefMeterPulseNumCmd(1);
										}
										Sleep(200);
									}
								//}
								//else { 
								//	ApplicationLauncher.logger.debug("BofaRefComRemindTaskWithQueue : comPortSemaphore: Semaphore not Aquired");
								//}
							} catch (Exception e) {
								e.printStackTrace();
								
								ApplicationLauncher.logger.error("BofaRefComRemindTaskWithQueue : Exception :" + e.getMessage());
								//BofaManager.comPortSemaphore.release(1);
								//ApplicationLauncher.logger.debug("BofaRefComRemindTaskWithQueue : Exception: comPortSemaphore : Semaphore : released");
								//ApplicationLauncher.logger.debug("BofaRefComRemindTaskWithQueue : Exception: comPortSemaphore : availablePermits : " + BofaManager.comPortSemaphore.availablePermits());
								
							} 

							
								retries--;
								Sleep(200);
							
							
							boolean pwrSrcConnectedStatus = false;
							if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
								pwrSrcConnectedStatus = SerialPortManagerPwrSrc_V2.isPwrSrcSerialStatusConnected();
							}else {
								pwrSrcConnectedStatus = BofaManager.getSerialDM_Obj().powerSourceSerialStatusConnected;
							}
							
							if (!displayDataObj.getRefStdReadDataFlag() || (!pwrSrcConnectedStatus)){
								userAborted= true;
								ApplicationLauncher.logger.debug("BofaRefComRemindTaskWithQueue : Exiting-2 Loop");
							}

						//}

						if (retries == 0) {
							ApplicationLauncher.logger.debug("BofaRefComRemindTaskWithQueue : Max retries reached");
						}
						//=============================================


						Sleep(200);// added delay to allow the Ref std BNC Constant task 

					} catch (Exception e1){
						e1.printStackTrace();
						ApplicationLauncher.logger.error("BofaRefComRemindTaskWithQueue :Exception1:"+ e1.getMessage());

					}
					RefStdTimer.schedule(new BofaRefComRemindTaskWithQueue(), SerialRefComInstantMetricsRefreshTimeInMsec);//SerialRefComKreInstantMetricsRefreshTimeInMsec);

				}else{
					if(!ProjectExecutionController.getUserAbortedFlag()){
						if( (!ProjectExecutionController.getAll_TestPoint_CompletedStatus())
								&& (!ProjectExecutionController.getExecuteStopStatus()) ){
						ApplicationLauncher.logger.debug("BofaRefComRemindTaskWithQueue : renewal2");

						RefStdTimer.schedule(new BofaRefComRemindTaskWithQueue(), SerialRefComInstantMetricsRefreshTimeInMsec);//SerialRefComKreInstantMetricsRefreshTimeInMsec);
					
						}
					}
				}
			}
			else{

				ApplicationLauncher.logger.debug("BofaRefComRemindTaskWithQueue :Timer Exit!%n");
				RefStdTimer.cancel(); //Terminate the timer thread



			}
		}
	}
	//=====================	
	public static void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("Sleep2 :InterruptedException:"+ e.getMessage());
		}

	}
	public static void frameActualOutputValueReadingCommand(){
		ApplicationLauncher.logger.info("frameActualOutputValueReadingCommand:Entry");
		// Frame the message
		StringBuilder messageBuilder = new StringBuilder();

		messageBuilder  =   messageBuilder.append(ConstantPowerSourceBofa.START_BYTE);                                             // Start bit
		messageBuilder.append( ConstantPowerSourceBofa.ADDRESS_BYTE);                    // Address
		messageBuilder.append(ConstantPowerSourceBofa.ACTUAL_OUTPUT_VALUE_READING_CMD); // Command
		ApplicationLauncher.logger.info("messageBuilder1 : " + messageBuilder);
		// Calculate and append check bit
		//byte checkByte = GuiUtils.generateCheckSumWithOneByte(messageBuilder.toString());//calculateCheckByte(messageBuilder.toString());
		String checkByte = GuiUtils.generateCheckSumWithOneByte(messageBuilder.toString());
		//messageBuilder.append(String.format("%02X", checkByte));
		messageBuilder.append(checkByte);
		ApplicationLauncher.logger.info("messageBuilder2 : " + messageBuilder);
		// Append end bit
		messageBuilder.append(ConstantPowerSourceBofa.END_BYTE);
		ApplicationLauncher.logger.info("messageBuilder3 : " + messageBuilder);
		// Final message
		actualOutputValueReadingCmdFrame = messageBuilder.toString();
		ApplicationLauncher.logger.info("actualOutputValueReadingCmdFrame : " + actualOutputValueReadingCmdFrame);
		ApplicationLauncher.logger.info("frameActualOutputValueReadingCommand:Exit");	
	}

	//===========================================================================================================
	public static void frameReadTheConstantOfLiveReferenceMeter(){
		ApplicationLauncher.logger.info("frameReadTheConstantOfLiveReferenceMeter:Entry");

		// Frame the message
		StringBuilder messageBuilder = new StringBuilder();

		messageBuilder  =   messageBuilder.append(ConstantPowerSourceBofa.START_BYTE);                                               // Start bit
		messageBuilder.append(ConstantPowerSourceBofa.ADDRESS_BYTE);                       // Address
		messageBuilder.append(ConstantPowerSourceBofa.READ_THE_CONSTANT_OF_LIVE_REF_METER); // Command

		// Calculate and append check bit
		//String checkByte = calculateCheckByte(messageBuilder.toString());
		String checkByte = GuiUtils.generateCheckSumWithOneByte(messageBuilder.toString());
		messageBuilder.append( checkByte);

		// Append end bit
		messageBuilder.append(ConstantPowerSourceBofa.END_BYTE);

		// Final message
		readTheConstantOfLiveReferenceMeterCmdFrame = messageBuilder.toString();
		ApplicationLauncher.logger.info("readTheConstantOfLiveReferenceMeterCmdFrame : " + readTheConstantOfLiveReferenceMeterCmdFrame);
		ApplicationLauncher.logger.info("frameReadTheConstantOfLiveReferenceMeter:Exit");
	}

	//===========================================================================================================


	public static void frameSwitchFromPulseOfReferenceMeterToPulseOfReferenceClock(){
		ApplicationLauncher.logger.info("frameSwitchFromPulseOfReferenceMeterToPulseOfReferenceClock:Entry");
		// Frame the message
		StringBuilder messageBuilder = new StringBuilder();

		messageBuilder  =   messageBuilder.append(ConstantPowerSourceBofa.START_BYTE);                                               // Start bit
		messageBuilder.append( ConstantPowerSourceBofa.ADDRESS_BYTE);                       // Address
		messageBuilder.append(ConstantPowerSourceBofa.SWITCH_PULSE_REF_METER_TO_PULSE_REF_CLK); // Command
		messageBuilder.append(ConstantPowerSourceBofa.PULSE_REF_METER); // Command
		// Calculate and append check bit
		//byte checkByte = calculateCheckByte(messageBuilder.toString());
		String checkByte = GuiUtils.generateCheckSumWithOneByte(messageBuilder.toString());
		messageBuilder.append( checkByte);

		// Append end bit
		messageBuilder.append(ConstantPowerSourceBofa.END_BYTE);

		// Final message
		switchPulseRefMeterToPuleRefClkCmdFrame = messageBuilder.toString();	
		ApplicationLauncher.logger.info("switchPulseRefMeterToPuleRefClkCmdFrame : " + switchPulseRefMeterToPuleRefClkCmdFrame);
		ApplicationLauncher.logger.info("frameSwitchFromPulseOfReferenceMeterToPulseOfReferenceClock:Exit");  
	}

	//===========================================================================================================

	public static void frameFrequencyReadingCommand(){
		ApplicationLauncher.logger.info("frameFrequencyReadingCommand : Entry");

		// Frame the message
		StringBuilder messageBuilder = new StringBuilder();

		messageBuilder  =   messageBuilder.append(ConstantPowerSourceBofa.START_BYTE);            // Start bit
		messageBuilder.append( ConstantPowerSourceBofa.ADDRESS_BYTE);         // Address
		messageBuilder.append(ConstantPowerSourceBofa.FREQUENCY_READING_CMD); // Command

		// Calculate and append check bit
		//byte checkByte = GuiUtils.generateCheckSumWithOneByte(messageBuilder.toString());//calculateCheckByte(messageBuilder.toString());
		String checkByte = GuiUtils.generateCheckSumWithOneByte(messageBuilder.toString());

		//messageBuilder.append(String.format("%02X", checkByte));
		messageBuilder.append(checkByte);

		// Append end bit
		messageBuilder.append(ConstantPowerSourceBofa.END_BYTE);
		// Final message
		readFrequencyCmdFrame = messageBuilder.toString();
		ApplicationLauncher.logger.info("frameFrequencyReadingCommand : " + readFrequencyCmdFrame );
		ApplicationLauncher.logger.info("frameFrequencyReadingCommand : Exit");	
	}

	//===========================================================================================================

	static boolean sendActualOutputValueReadingCommand(){
		ApplicationLauncher.logger.info("sendActualOutputValueReadingCommand: Entry");  

		boolean status = false;

		String payLoadInHex = actualOutputValueReadingCmdFrame;//GuiUtils.StringToHex(actualOutputValueReadingCmdFrame );
		ApplicationLauncher.logger.info("sendActualOutputValueReadingCommand : payLoadInHex: " + payLoadInHex  );

		int timeDelayInMilliSec = 0;
		String expectedDataInHex = ConstantPowerSourceBofa.ER_STARTS_WITH;
		boolean isResponseExpected = true;

		String responseStatus = "";
		
		if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
			String sourceThread = "sendActualOutputValueReadingCommand " +"-" + String.valueOf(Instant.now().toEpochMilli());
			responseStatus = BofaManager.getPwrSrcBofaMessenger().bofaPowerSourceMsngrSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected,sourceThread);
			
		}else{
			responseStatus = BofaManager.getSerialDM_Obj().bofaPowerSourceSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
		}
		
		
		if (responseStatus == DeleteMeConstant.SUCCESS_RESPONSE) {

			String responseData = "";
			if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
				responseData = BofaManager.getPwrSrcBofaMessenger().getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage();
				responseData = GuiUtils.asciiToHex(responseData);
			}else{ 
			
				responseData = BofaManager.getSerialDM_Obj().getRxMsgQ_PwrSrc().getLastReadMessage();//BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
			}
		status = processResponse(actualOutputValueReadingCmdFrame, responseData);
		}
		//ApplicationLauncher.logger.info("sendActualOutputValueReadingCommand: Exit");  
		return status;
	}
	//===========================================================================================================




	public static boolean sendReadConstOfLiveRefStdCmd(){
		ApplicationLauncher.logger.info("sendReadConstOfLiveRefStdCmd: Entry");  

		boolean status = false;

		String payLoadInHex = readTheConstantOfLiveReferenceMeterCmdFrame;//GuiUtils.StringToHex(readTheConstantOfLiveReferenceMeterCmdFrame);
		ApplicationLauncher.logger.info("sendReadConstOfLiveRefStdCmd : payLoadInHex: " + payLoadInHex  );

		int timeDelayInMilliSec = 0;
		String expectedDataInHex = ConstantPowerSourceBofa.ER_STARTS_WITH;
		boolean isResponseExpected = true;

		/*		//===============================		
		try {
			ApplicationLauncher.logger.debug("sendReadConstOfLiveRefStdCmd: acquiring...");		
			BofaManager.comPortSemaphore.acquire(); // Try to Acquire the semaphore
			ApplicationLauncher.logger.debug("sendReadConstOfLiveRefStdCmd: acquired");	
			String responseStatus = BofaManager.getSerialDM_Obj().bofaPowerSourceSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
			if (responseStatus == DeleteMeConstant.SUCCESS_RESPONSE) {
				String CurrentReadData = BofaManager.getSerialDM_Obj().getRxMsgQ_PwrSrc().getLastReadMessage();//BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
				status = processResponse(readTheConstantOfLiveReferenceMeterCmdFrame, CurrentReadData);
			}		
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("sendReadConstOfLiveRefStdCmd :Exception :" + e.getMessage());
		} finally {
			BofaManager.comPortSemaphore.release(); // Release the semaphore 
			ApplicationLauncher.logger.debug("sendReadConstOfLiveRefStdCmd: released");	
			Sleep(220);
		}*/
		//=====================================
		Map<String,Object> responseReturn = new HashMap<String,Object>();
		boolean forceExecute = false;
		responseReturn = BofaManager.sendDataToBofaAfterSemaPhoreAcquired("sendReadConstOfLiveRefStdCmd",payLoadInHex, timeDelayInMilliSec,
				expectedDataInHex, isResponseExpected,forceExecute);
		status = (boolean)responseReturn.get("status");
		if(status){
			String responseData = (String)responseReturn.get("result");
			status =  processResponse(readTheConstantOfLiveReferenceMeterCmdFrame, responseData);

		}

		ApplicationLauncher.logger.info("sendReadConstOfLiveRefStdCmd: Exit");  
		return status;
	}
	//===========================================================================================================

	static boolean sendSwitchFromPulseOfReferenceMeterToPulseOfReferenceClkCmd(){
		ApplicationLauncher.logger.info("sendSwitchFromPulseOfReferenceMeterToPulseOfReferenceClkCmd: Entry");  

		boolean status = false;

		String payLoadInHex = switchPulseRefMeterToPuleRefClkCmdFrame;// GuiUtils.StringToHex(switchPulseRefMeterToPuleRefClkCmdFrame );
		ApplicationLauncher.logger.info("sendSwitchFromPulseOfReferenceMeterToPulseOfReferenceClkCmd : payLoadInHex: " + payLoadInHex  );

		int timeDelayInMilliSec = 0;
		String expectedDataInHex = ConstantPowerSourceBofa.ER_STARTS_WITH;
		boolean isResponseExpected = true;

		String responseStatus = BofaManager.getSerialDM_Obj().bofaPowerSourceSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
		if (responseStatus == DeleteMeConstant.SUCCESS_RESPONSE) {
			String CurrentReadData = BofaManager.getSerialDM_Obj().getRxMsgQ_PwrSrc().getLastReadMessage();//BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
			status = processResponse(switchPulseRefMeterToPuleRefClkCmdFrame, CurrentReadData);
		}	
		ApplicationLauncher.logger.info("sendSwitchFromPulseOfReferenceMeterToPulseOfReferenceClkCmd: Exit");  
		return status;
	}
	//===========================================================================================================
	static boolean sendFrequencyReadingCommand(){
		ApplicationLauncher.logger.info("sendFrequencyReadingCommand: Entry");  

		boolean status = false;

		String payLoadInHex = readFrequencyCmdFrame ;//GuiUtils.StringToHex(actualOutputValueReadingCmdFrame );
		ApplicationLauncher.logger.info("sendFrequencyReadingCommand : payLoadInHex: " + payLoadInHex  );

		int timeDelayInMilliSec = 0;
		String expectedDataInHex = ConstantPowerSourceBofa.ER_STARTS_WITH;
		boolean isResponseExpected = true;

		String responseStatus = "";
		if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
			
			responseStatus = BofaManager.getPwrSrcBofaMessenger().bofaPowerSourceMsngrSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected,"sendFrequencyReadingCommand");
			
		}else{
			responseStatus = BofaManager.getSerialDM_Obj().bofaPowerSourceSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
		}
		
		if (responseStatus == DeleteMeConstant.SUCCESS_RESPONSE) {
			String responseData = "";
			if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
				responseData = BofaManager.getPwrSrcBofaMessenger().getPwrSrcSpmObj().getRxMsgQ_PwrSrc().getLastReadMessage();
				responseData = GuiUtils.asciiToHex(responseData);
			}else{
				responseData = BofaManager.getSerialDM_Obj().getRxMsgQ_PwrSrc().getLastReadMessage();//BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
			}
			
			
			
			status = processResponse(readFrequencyCmdFrame, responseData);
		}

		ApplicationLauncher.logger.info("sendFrequencyReadingCommand: Exit");  
		return status;
	}

	//====================================================================================================================
	static boolean processResponse(String messageType, String response){

		boolean status = false;

		if (messageType == actualOutputValueReadingCmdFrame) {
			status = parseActualOutputValueResponse(response);
		}
		else if (messageType == readTheConstantOfLiveReferenceMeterCmdFrame) {
			status = parseMeterConstantResponse(response);
		}
		else if (messageType == switchPulseRefMeterToPuleRefClkCmdFrame) {
			status = parseAcknowledgementResponses(response);
		}                    
		else if (messageType == readFrequencyCmdFrame) {
			status = parseFrequencyResponse(response);
		}

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
		return status;

	}

	//===========================================================================================================
	public static boolean parseActualOutputValueResponse(String response){
		//ApplicationLauncher.logger.info("parseActualOutputValueResponse: Entry");  

		boolean status = false;

		 ApplicationLauncher.logger.info("parseActualOutputValueResponse : response      : " + response);

		// Parse substrings
		String commandCode         = "00";//response.substring(0, 2);                                // 2 digits
		String address             = "00";//response.substring(2, 4);                                // 2 digits
		String ack                 = "00";// response.substring(4, 6);                                // 2 digits
		String UAString            = "000.00" ; // = GuiUtils.hexToAscii(response.substring(6, 16));          // 10 digits
		String UBString            = "000.00" ; // = GuiUtils.hexToAscii(response.substring(16, 26));         // 10 digits
		String UCString            = "000.00" ; // = GuiUtils.hexToAscii(response.substring(26, 36));         // 10 digits
		String IAString            = "000.00" ; // = GuiUtils.hexToAscii(response.substring(36, 50));         // 14 digits
		String IBString            = "000.00" ; // = GuiUtils.hexToAscii(response.substring(50, 64));         // 14 digits
		String ICString            = "000.00" ; // = GuiUtils.hexToAscii(response.substring(64, 78));         // 14 digits
		String UaUbAngleString     = "000.00" ; // = GuiUtils.hexToAscii(response.substring(78, 88));         // 10 digits
		String UaUcAngleString     = "000.00" ; // = GuiUtils.hexToAscii(response.substring(88, 98));         // 10 digits
		String UaIaAngleString     = "000.00" ; // = GuiUtils.hexToAscii(response.substring(98, 108));        // 10 digits
		String UbUbAngleString     = "000.00" ; // = GuiUtils.hexToAscii(response.substring(108, 118));       // 10 digits
		String UcIcAngleString     = "000.00" ; // = GuiUtils.hexToAscii(response.substring(118, 128));       // 10 digits
		String activeAString       = "000.00" ; // = GuiUtils.hexToAscii(response.substring(128, 144));       // 16 digits
		String activeBString       = "000.00" ; // = GuiUtils.hexToAscii(response.substring(144, 160));       // 16 digits
		String activeCString       = "000.00" ; // = GuiUtils.hexToAscii(response.substring(160, 176));       // 16 digits
		String reactiveAString 	   = "000.00" ; // = GuiUtils.hexToAscii(response.substring(176, 192));       // 16 digits
		String reactiveBString     = "000.00" ; // = GuiUtils.hexToAscii(response.substring(192, 208));       // 16 digits
		String reactiveCString     = "000.00" ; // = GuiUtils.hexToAscii(response.substring(208, 224));       // 16 digits
		String totalActiveString   = "000.00" ; // = GuiUtils.hexToAscii(response.substring(224, 240));       // 16 digits
		String totalReactiveString = "000.00" ; // = GuiUtils.hexToAscii(response.substring(240, 256));       // 16 digits
		String apparentPowerString = "000.00" ; // = GuiUtils.hexToAscii(response.substring(256, 272));       // 16 digits
		//String checkSumString      = "00";//response.substring(272, 274);                            // 2 digits
		//String endByteString       = "00";// response.substring(274, 276);                            // 2 digits

		try{
			ApplicationLauncher.logger.debug("parseActualOutputValueResponse: message length :" + response.length());

			commandCode         = response.substring(0, 2);                                // 2 digits
			address             = response.substring(2, 4);                                // 2 digits
			ack                 = response.substring(4, 6);                                // 2 digits
			UAString            = GuiUtils.hexToAscii(response.substring(6, 16));          // 10 digits
			UBString            = GuiUtils.hexToAscii(response.substring(16, 26));         // 10 digits
			UCString            = GuiUtils.hexToAscii(response.substring(26, 36));         // 10 digits
			IAString            = GuiUtils.hexToAscii(response.substring(36, 50));         // 14 digits
			IBString            = GuiUtils.hexToAscii(response.substring(50, 64));         // 14 digits
			ICString            = GuiUtils.hexToAscii(response.substring(64, 78));         // 14 digits
			UaUbAngleString     = GuiUtils.hexToAscii(response.substring(78, 88));         // 10 digits
			UaUcAngleString     = GuiUtils.hexToAscii(response.substring(88, 98));         // 10 digits
			UaIaAngleString     = GuiUtils.hexToAscii(response.substring(98, 108));        // 10 digits
			UbUbAngleString     = GuiUtils.hexToAscii(response.substring(108, 118));       // 10 digits
			UcIcAngleString     = GuiUtils.hexToAscii(response.substring(118, 128));       // 10 digits
			activeAString       = GuiUtils.hexToAscii(response.substring(128, 144));       // 16 digits
			activeBString       = GuiUtils.hexToAscii(response.substring(144, 160));       // 16 digits
			activeCString       = GuiUtils.hexToAscii(response.substring(160, 176));       // 16 digits
			reactiveAString 	= GuiUtils.hexToAscii(response.substring(176, 192));       // 16 digits
			reactiveBString     = GuiUtils.hexToAscii(response.substring(192, 208));       // 16 digits
			reactiveCString     = GuiUtils.hexToAscii(response.substring(208, 224));       // 16 digits
			totalActiveString   = GuiUtils.hexToAscii(response.substring(224, 240));       // 16 digits
			totalReactiveString = GuiUtils.hexToAscii(response.substring(240, 256));       // 16 digits
			apparentPowerString = GuiUtils.hexToAscii(response.substring(256, 272));       // 16 digits
			//checkSumString      = response.substring(272, 274);                            // 2 digits
			//endByteString       = response.substring(274, 276);                            // 2 digits // commented at version s4.2.1.2.1.2 due to exception sometimes
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("parseActualOutputValueResponse: parsing all data : Exception: " + e.getMessage());
		}

		/* ApplicationLauncher.logger.debug("Command Code    : " + commandCode);
        ApplicationLauncher.logger.debug("Address         : " + address);
        ApplicationLauncher.logger.debug("Ack             : " + ack);
        ApplicationLauncher.logger.debug("UA              : " + UAString);
        ApplicationLauncher.logger.debug("UB              : " + UBString);
        ApplicationLauncher.logger.debug("UC              : " + UCString);
        ApplicationLauncher.logger.debug("IA              : " + IAString);
        ApplicationLauncher.logger.debug("IB              : " + IBString);
        ApplicationLauncher.logger.debug("IC              : " + ICString);
        ApplicationLauncher.logger.debug("UaUb Angle      : " + UaUbAngleString);
        ApplicationLauncher.logger.debug("UaUc Angle      : " + UaUcAngleString);
        ApplicationLauncher.logger.debug("UaIa Angle      : " + UaIaAngleString);
        ApplicationLauncher.logger.debug("UbUb Angle      : " + UbUbAngleString);
        ApplicationLauncher.logger.debug("UcIc Angle      : " + UcIcAngleString);
        ApplicationLauncher.logger.debug("Active A        : " + activeAString);
        ApplicationLauncher.logger.debug("Active B        : " + activeBString);
        ApplicationLauncher.logger.debug("Active C        : " + activeCString);
        ApplicationLauncher.logger.debug("Reactive A      : " + reactiveAString);
        ApplicationLauncher.logger.debug("Reactive B      : " + reactiveBString);
        ApplicationLauncher.logger.debug("Reactive C      : " + reactiveCString);
        ApplicationLauncher.logger.debug("Total Active    : " + totalActiveString);
        ApplicationLauncher.logger.debug("Total Reactive  : " + totalReactiveString);
        ApplicationLauncher.logger.debug("Apparent Power  : " + apparentPowerString);
        ApplicationLauncher.logger.debug("Check Sum       : " + checkSumString);
        ApplicationLauncher.logger.debug("End Byte        : " + endByteString);*/


		// Convert strings to appropriate data types
		float UA 			= 0.0f;//BofaManager.formatDecimalPoints(Float.parseFloat(UAString),2);
		float UB 			= 0.0f;//BofaManager.formatDecimalPoints(Float.parseFloat(UBString),2);
		float UC 			= 0.0f;//BofaManager.formatDecimalPoints(Float.parseFloat(UCString),2);
		float IA 			= 0.0f;//BofaManager.formatDecimalPoints(Float.parseFloat(IAString),4);
		float IB 			= 0.0f;//BofaManager.formatDecimalPoints(Float.parseFloat(IBString),4);
		float IC 			= 0.0f;//BofaManager.formatDecimalPoints(Float.parseFloat(ICString),4);
		float UaUbAngle 	= 0.0f;//BofaManager.formatDecimalPoints(Float.parseFloat(UaUbAngleString),2);
		float UaUcAngle 	= 0.0f;//BofaManager.formatDecimalPoints(Float.parseFloat(UaUcAngleString),2);
		float UaIaAngle 	= 0.0f;//BofaManager.formatDecimalPoints(Float.parseFloat(UaIaAngleString),2);
		float UbUbAngle 	= 0.0f;//BofaManager.formatDecimalPoints(Float.parseFloat(UbUbAngleString),2);
		float UcIcAngle 	= 0.0f;//BofaManager.formatDecimalPoints(Float.parseFloat(UcIcAngleString),2);
		float activeA 		= 0.0f;//BofaManager.formatDecimalPoints(Float.parseFloat(activeAString),2);
		float activeB 		= 0.0f;//BofaManager.formatDecimalPoints(Float.parseFloat(activeBString),2);
		float activeC 		= 0.0f;//BofaManager.formatDecimalPoints(Float.parseFloat(activeCString),2);
		float reactiveA 	= 0.0f;//BofaManager.formatDecimalPoints(Float.parseFloat(reactiveAString),2);
		float reactiveB 	= 0.0f;//BofaManager.formatDecimalPoints(Float.parseFloat(reactiveBString),2);
		float reactiveC 	= 0.0f;//BofaManager.formatDecimalPoints(Float.parseFloat(reactiveCString),2);
		float totalActive   = 0.0f;//BofaManager.formatDecimalPoints(Float.parseFloat(totalActiveString),2);
		float totalReactive = 0.0f;//BofaManager.formatDecimalPoints(Float.parseFloat(totalReactiveString),2);
		float apparentPower = 0.0f;//BofaManager.formatDecimalPoints(Float.parseFloat(apparentPowerString),2);


		ApplicationLauncher.logger.debug("//=================================================================//");

		try{
			UA 			= BofaManager.formatDecimalPoints(Float.parseFloat(UAString),2);
		}catch(Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("parseActualOutputValueResponse: UA : Exception: " + e.getMessage());
		}

		try {
			UB = BofaManager.formatDecimalPoints(Float.parseFloat(UBString), 2);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("parseActualOutputValueResponse: UB : Exception: " + e.getMessage());
		}

		try {
			UC = BofaManager.formatDecimalPoints(Float.parseFloat(UCString), 2);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("parseActualOutputValueResponse: UC : Exception: " + e.getMessage());
		}

		try {
			IA = BofaManager.formatDecimalPoints(Float.parseFloat(IAString), 4);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("parseActualOutputValueResponse: IA : Exception: " + e.getMessage());
		}

		try {
			IB = BofaManager.formatDecimalPoints(Float.parseFloat(IBString), 4);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("parseActualOutputValueResponse: IB : Exception: " + e.getMessage());
		}

		try {
			IC = BofaManager.formatDecimalPoints(Float.parseFloat(ICString), 4);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("parseActualOutputValueResponse: IC : Exception: " + e.getMessage());
		}

		try {
			UaUbAngle = BofaManager.formatDecimalPoints(Float.parseFloat(UaUbAngleString), 2);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("parseActualOutputValueResponse: UaUbAngle : Exception: " + e.getMessage());
		}

		try {
			UaUcAngle = BofaManager.formatDecimalPoints(Float.parseFloat(UaUcAngleString), 2);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("parseActualOutputValueResponse: UaUcAngle : Exception: " + e.getMessage());
		}

		try {
			UaIaAngle = BofaManager.formatDecimalPoints(Float.parseFloat(UaIaAngleString), 2);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("parseActualOutputValueResponse: UaIaAngle : Exception: " + e.getMessage());
		}

		try {
			UbUbAngle = BofaManager.formatDecimalPoints(Float.parseFloat(UbUbAngleString), 2);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("parseActualOutputValueResponse: UbUbAngle : Exception: " + e.getMessage());
		}

		try {
			UcIcAngle = BofaManager.formatDecimalPoints(Float.parseFloat(UcIcAngleString), 2);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("parseActualOutputValueResponse: UcIcAngle : Exception: " + e.getMessage());
		}

		try {
			activeA = BofaManager.formatDecimalPoints(Float.parseFloat(activeAString), 2);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("parseActualOutputValueResponse: activeA : Exception: " + e.getMessage());
		}

		try {
			activeB = BofaManager.formatDecimalPoints(Float.parseFloat(activeBString), 2);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("parseActualOutputValueResponse: activeB : Exception: " + e.getMessage());
		}

		try {
			activeC = BofaManager.formatDecimalPoints(Float.parseFloat(activeCString), 2);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("parseActualOutputValueResponse: activeC : Exception: " + e.getMessage());
		}

		try {
			reactiveA = BofaManager.formatDecimalPoints(Float.parseFloat(reactiveAString), 2);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("parseActualOutputValueResponse: reactiveA : Exception: " + e.getMessage());
		}

		try {
			reactiveB = BofaManager.formatDecimalPoints(Float.parseFloat(reactiveBString), 2);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("parseActualOutputValueResponse: reactiveB : Exception: " + e.getMessage());
		}

		try {
			reactiveC = BofaManager.formatDecimalPoints(Float.parseFloat(reactiveCString), 2);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("parseActualOutputValueResponse: reactiveC : Exception: " + e.getMessage());
		}

		try {
			totalActive = BofaManager.formatDecimalPoints(Float.parseFloat(totalActiveString), 2);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("parseActualOutputValueResponse: totalActive : Exception: " + e.getMessage());
		}

		try {
			totalReactive = BofaManager.formatDecimalPoints(Float.parseFloat(totalReactiveString), 2);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("parseActualOutputValueResponse: totalReactive : Exception: " + e.getMessage());
		}

		try {
			apparentPower = BofaManager.formatDecimalPoints(Float.parseFloat(apparentPowerString), 2);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("parseActualOutputValueResponse: apparentPower : Exception: " + e.getMessage());
		}

		float powerFactor = 0.0f;
		try {
			if(apparentPower>0){
				powerFactor = totalActive/apparentPower;
			}
			else{
				powerFactor = 0;
			}
			ApplicationLauncher.logger.error("parseActualOutputValueResponse: powerFactor : " + powerFactor);

		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("parseActualOutputValueResponse: powerFactor : Exception: " + e.getMessage());
		}
		/*	
		UB 			= BofaManager.formatDecimalPoints(Float.parseFloat(UBString),2);
		UC 			= BofaManager.formatDecimalPoints(Float.parseFloat(UCString),2);
		IA 			= BofaManager.formatDecimalPoints(Float.parseFloat(IAString),4);
		IB 			= BofaManager.formatDecimalPoints(Float.parseFloat(IBString),4);
		IC 			= BofaManager.formatDecimalPoints(Float.parseFloat(ICString),4);
		UaUbAngle 	= BofaManager.formatDecimalPoints(Float.parseFloat(UaUbAngleString),2);
		UaUcAngle 	= BofaManager.formatDecimalPoints(Float.parseFloat(UaUcAngleString),2);
		try {
			UaIaAngle 	= BofaManager.formatDecimalPoints(Float.parseFloat(UaIaAngleString),2);
		}catch(Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("parseActualOutputValueResponse: UaIaAngle : Exception: " + e.getMessage());
		}
		UbUbAngle 	= BofaManager.formatDecimalPoints(Float.parseFloat(UbUbAngleString),2);
		UcIcAngle 	= BofaManager.formatDecimalPoints(Float.parseFloat(UcIcAngleString),2);
		activeA 	= BofaManager.formatDecimalPoints(Float.parseFloat(activeAString),2);
		activeB 	= BofaManager.formatDecimalPoints(Float.parseFloat(activeBString),2);
		activeC 	= BofaManager.formatDecimalPoints(Float.parseFloat(activeCString),2);
		reactiveA 	= BofaManager.formatDecimalPoints(Float.parseFloat(reactiveAString),2);
		reactiveB 	= BofaManager.formatDecimalPoints(Float.parseFloat(reactiveBString),2);
		reactiveC 	= BofaManager.formatDecimalPoints(Float.parseFloat(reactiveCString),2);
		totalActive   = BofaManager.formatDecimalPoints(Float.parseFloat(totalActiveString),2);
		totalReactive = BofaManager.formatDecimalPoints(Float.parseFloat(totalReactiveString),2);
		apparentPower = BofaManager.formatDecimalPoints(Float.parseFloat(apparentPowerString),2);*/

		// Printing the extracted components
		/*ApplicationLauncher.logger.debug("UA             : " + UA);
		ApplicationLauncher.logger.debug("UB             : " + UB);
		ApplicationLauncher.logger.debug("UC             : " + UC);
		ApplicationLauncher.logger.debug("IA             : " + IA);
		ApplicationLauncher.logger.debug("IB             : " + IB);
		ApplicationLauncher.logger.debug("IC             : " + IC);
		ApplicationLauncher.logger.debug("UaUb Angle	 : " + UaUbAngle);
		ApplicationLauncher.logger.debug("UaUc Angle	 : " + UaUcAngle);
		ApplicationLauncher.logger.debug("UaIa Angle	 : " + UaIaAngle);
		ApplicationLauncher.logger.debug("UbUb Angle	 : " + UbUbAngle);
		ApplicationLauncher.logger.debug("UcIc Angle	 : " + UcIcAngle);
		ApplicationLauncher.logger.debug("Active A       : " + activeA);
		ApplicationLauncher.logger.debug("Active B		 : " + activeB);
		ApplicationLauncher.logger.debug("Active C		 : " + activeC);
		ApplicationLauncher.logger.debug("Reactive A     : " + reactiveA);
		ApplicationLauncher.logger.debug("Reactive B     : " + reactiveB);
		ApplicationLauncher.logger.debug("Reactive C     : " + reactiveC);
		ApplicationLauncher.logger.debug("Total Active   : " + totalActive);
		ApplicationLauncher.logger.debug("Total Reactive : " + totalReactive);
		ApplicationLauncher.logger.debug("Apparent Power : " + apparentPower);
		ApplicationLauncher.logger.debug("powerFactor    : " + powerFactor);*/


		/*boolean isValid = validateChecksum(response);
        if (isValid){
        	ApplicationLauncher.logger.debug("parseActualOutputValueResponse : Check Sum is Valid " );
        	status = true ;
        }
        else {
        	status = false ;
        	ApplicationLauncher.logger.debug("parseActualOutputValueResponse : Check Sum is Invalid");
        	return status ;
        }
		 */
		if(commandCode.equals(ConstantPowerSourceBofa.START_BYTE) && address.equals(ConstantPowerSourceBofa.ADDRESS_BYTE) 
				&& ack.equals(ConstantPowerSourceBofa.POSITIVE)
				//&& endByteString.equals(ConstantPowerSourceBofa.END_BYTE) //some time checksum is missing , hence exception occurring 09-April-2025
				){
			status = true ;

			SerialDataBofaRefStd guiRefreshData = new SerialDataBofaRefStd();
			guiRefreshData.setVoltageDisplayData(String.valueOf(UA));
			String currentStringValue = String.valueOf(IA);
			if(currentStringValue.endsWith(ConstantPowerSourceBofa.BOFA_CURRENT_ZERO_VALUE)){
				currentStringValue = "0.0";
			}else if(IA < 1.0f){
				
				currentStringValue = String.valueOf(current1_AmpsLessFormatter.format(IA));
			}else if(IA < 100.0f){
				currentStringValue = String.valueOf(current100_AmpsLessFormatter.format(IA));
			}else if(IA >= 100.0f){
				currentStringValue = String.valueOf(current100_AmpsGreaterFormatter.format(IA));
			}
			guiRefreshData.setCurrentDisplayData(currentStringValue); 
			guiRefreshData.setDegreePhaseData(String.valueOf(UaIaAngle)); 
			guiRefreshData.setPowerFactorData(String.valueOf(pfFormatter.format(powerFactor))); 
			//setFormattedRsmActualFreqValue(String.valueOf(frequencyFormatter.format(getRsmActualFreqValue())));
			guiRefreshData.setFreqDisplayData(String.valueOf(frequencyFormatter.format(getRsmActualFreqValue())));//getFormattedRsmActualFreqValue()); 
			guiRefreshData.setWattDisplayData(String.valueOf(activeA)); 
			guiRefreshData.setVA_DisplayData(String.valueOf(apparentPower)); 
			guiRefreshData.setVAR_DisplayData(String.valueOf(reactiveA)); 
			ApplicationLauncher.logger.debug("parseActualOutputValueResponse : updating gui rsm output value");
			if(isReadEnergyAccumulateEnabled()){
				guiRefreshData.setActiveEnergyInKwhDisplayData(String.valueOf(Data_RefStdBofa.getRefStdActiveEnergyAccumulatedInKwh()));
			}else{
				guiRefreshData.setActiveEnergyInKwhDisplayData("");
			}
			//guiRefreshData.setActiveEnergyInWhDisplayData(String.valueOf(Data_RefStdBofa.getRefStdActiveEnergyAccumulatedInKwh()*1000));
			
			if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
				DisplayInstantMetricsController.deviceBofaRefStdDataDisplayUpdate_R_Phase(guiRefreshData);
			}
			if(ProcalFeatureEnable.REF_STD_DATA_DISPLAYED_IN_RUN_SCREEN_ENABLED){
				ProjectExecutionController.refStdDataDisplayUpdate_R_Phase(guiRefreshData);
			}
			//DisplayInstantMetricsController.DeviceRefStdDataDisplayUpdateActiveEnergy(String.valueOf(Data_RefStdBofa.getRefStdActiveEnergyAccumulatedInKwh()));
		}
		else{
			status = false ;
			if (ack.equals(ConstantPowerSourceBofa.NEGATIVE)) {
				ApplicationLauncher.logger.debug("parseActualOutputValueResponse : NEGATIVE Acknowledgment received ");
			}
		}
		//if(){

		//}

		//ApplicationLauncher.logger.info("parseActualOutputValueResponse: Exit");

		return status ;

	}
	//===========================================================================================================

	/*public static boolean validateChecksum(String hexString) {
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
		ApplicationLauncher.logger.debug("receivedChecksum    : " + receivedChecksum);

        byte[] data   = hexStringToByteArray(dataString);

        // Calculate the checksum
        byte calculatedChecksum = calculateChecksum(data);
		ApplicationLauncher.logger.debug("calculatedChecksum  : " + calculatedChecksum);

        // Compare the calculated checksum with the expected checksum
        return receivedChecksum == calculatedChecksum;
    }	*/
	//========================================		
	/*private static byte hexStringToByte(String hexString) {
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

	//================================================================================================================
	static boolean parseMeterConstantResponse(String response){
		ApplicationLauncher.logger.info("parseMeterConstantResponse: Entry"); 
		boolean status = false;

		String commandCode   = response.substring(0, 2); 
		String address       = response.substring(2, 4);
		String meterConstant = response.substring(4, 24);
		String checksum      = response.substring(24, 26);
		String endByte       = response.substring(26);

		// 0192 30303332303030303030 78 17 ///28

		ApplicationLauncher.logger.info("Command Code       : " + commandCode);  
		ApplicationLauncher.logger.info("Address            : " + address);  
		ApplicationLauncher.logger.info("Ref Meter Constant : " + meterConstant);  
		ApplicationLauncher.logger.info("Checksum Bytes     : " + checksum);  
		ApplicationLauncher.logger.info("End Bytes          : " + endByte);  

		setLiveRefMeterConstant(Long.parseUnsignedLong(GuiUtils.hexToAscii(meterConstant)));

		ApplicationLauncher.logger.info("Ref Meter Constant : " + getLiveRefMeterConstant());  
		/*boolean isValid = validateChecksum(response);
        if (isValid){
        	ApplicationLauncher.logger.debug("parseActualOutputValueResponse : Check Sum is Valid " );
        	status = true ;
        }
        else {
        	status = false ;
        	ApplicationLauncher.logger.debug("parseActualOutputValueResponse : Check Sum is Invalid");
        	return status ;
        }
		 */
		if(commandCode.equals(ConstantPowerSourceBofa.START_BYTE) && address.equals(ConstantPowerSourceBofa.ADDRESS_BYTE) && endByte.equals(ConstantPowerSourceBofa.END_BYTE)){
			status = true ;
		}
		else{
			status = false ;

		}
		ApplicationLauncher.logger.info("parseMeterConstantResponse: Exit"); 
		return status ;
	}
	//===========================================================================================================

	static boolean parseAcknowledgementResponses(String response){

		boolean status = false;

		String commandCode   = response.substring(0, 2);
		String address       = response.substring(2, 4);
		String ackPart       = response.substring(4, 6);
		String checksum      = response.substring(6, 8);
		String endByte       = response.substring(8);

		String responseStatus = getResponseStatus(ackPart);

/*		ApplicationLauncher.logger.debug("Command Code   : " + commandCode);  
		ApplicationLauncher.logger.debug("Address        : " + address);  
		ApplicationLauncher.logger.debug("Polarity       : " + responseStatus);  
		ApplicationLauncher.logger.debug("Checksum Bytes : " + checksum);  
		ApplicationLauncher.logger.debug("End Bytes      : " + endByte);*/  


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

		return status ;	
	}

	//===========================================================================================================

	public static boolean parseFrequencyResponse(String response){
		ApplicationLauncher.logger.info("parseFrequencyResponse: Entry");  

		boolean status = false;

		// Parse substrings
		String commandCode         = "00";            // 2 digits
		String address             = "00";            // 2 digits
		String ack                 = "00";            // 2 digits
		String frequencyStr        = "00.0000";       // 12 digits
		String checkSumString      = "00";            // 2 digits
		String endByteString       = "00";            // 2 digits

		try{
			commandCode         = response.substring(0, 2);                                // 2 digits
			address             = response.substring(2, 4);                                // 2 digits
			ack                 = response.substring(4, 6);                                // 2 digits
			frequencyStr        = GuiUtils.hexToAscii(response.substring(6, 18));          // 12 digits
			checkSumString      = response.substring(18, 20);
			endByteString       = response.substring(20);

		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("parseFrequencyResponse: parsing all data : Exception: " + e.getMessage());
		}

		ApplicationLauncher.logger.debug("Command Code    : " + commandCode);
		ApplicationLauncher.logger.debug("Address         : " + address);
		ApplicationLauncher.logger.debug("Ack             : " + ack);
		ApplicationLauncher.logger.debug("Frequency       : " + frequencyStr);
		ApplicationLauncher.logger.debug("Check Sum       : " + checkSumString);
		ApplicationLauncher.logger.debug("End Byte        : " + endByteString);

		float frequency 		= 0.0f;//BofaManager.formatDecimalPoints(Float.parseFloat(UAString),2);

		try{
			frequency 			= BofaManager.formatDecimalPoints(Float.parseFloat(frequencyStr),4);
		}catch(Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("parseFrequencyResponse: UA : Exception: " + e.getMessage());
		}

		// Printing the extracted components
		ApplicationLauncher.logger.debug("Frequency        : " + frequency); 



		if(commandCode.equals(ConstantPowerSourceBofa.START_BYTE) && address.equals(ConstantPowerSourceBofa.ADDRESS_BYTE) && ack.equals(ConstantPowerSourceBofa.POSITIVE)&& endByteString.equals(ConstantPowerSourceBofa.END_BYTE)){
			status = true ;

			setRsmActualFreqValue(frequency);
			//setFormattedRsmActualFreqValue(String.valueOf(frequencyFormatter.format(getRsmActualFreqValue())));

			/*SerialDataBofaRefStd guiRefreshData = new SerialDataBofaRefStd();
			guiRefreshData.setFreqDisplayData(String.valueOf(frequency));  
			ApplicationLauncher.logger.debug("parseFrequencyResponse : updating gui frequency");
			DisplayInstantMetricsController.deviceBofaRefStdDataDisplayUpdate_R_Phase(guiRefreshData);*/
		}
		else{
			status = false ;
			if (ack.equals(ConstantPowerSourceBofa.NEGATIVE)) {
				ApplicationLauncher.logger.debug("parseFrequencyResponse : NEGATIVE Acknowledgment received ");
			}
		}



		ApplicationLauncher.logger.info("parseFrequencyResponse: Exit");

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

	public static void setSerialRefComInstantMetricsRefreshTimeInMsec(int TimeInMSec){
		SerialRefComInstantMetricsRefreshTimeInMsec = TimeInMSec;

	}

	public static void resetSerialRefComInstantMetricsRefreshTimeInMsec(){
		/*		if(ProcalFeatureEnable.KRE_REFSTD_CONNECTED){
			SerialRefComInstantMetricsRefreshTimeInMsec  = SerialRefComKreInstantMetricsRefreshTimeInMsec;
		}else{*/
		SerialRefComInstantMetricsRefreshTimeInMsec = SerialRefComInstantMetricsRefreshDefaultTimeInMsec;
		//}

	}

	public static int getSerialRefComInstantMetricsRefreshTimeInMsec(){
		return SerialRefComInstantMetricsRefreshTimeInMsec ;

	}
	//===========================================================================================================

	public static float getRsmActualFreqValue() {
		return rsmActualFreqValue;
	}

	public static void setRsmActualFreqValue(float rsmActualFreqValue) {
		Data_RefStdBofa.rsmActualFreqValue = rsmActualFreqValue;
	}

	public static long getLiveRefMeterConstant() {
		return liveRefMeterConstant;
	}

	public static void setLiveRefMeterConstant(long liveRefMeterConstant) {
		Data_RefStdBofa.liveRefMeterConstant = liveRefMeterConstant;
	}

	public static boolean isRsmFreqReadingRequired() {
		return rsmFreqReadingRequired;
	}

	public static void setRsmFreqReadingRequired(boolean rsmFreqReadingRequired) {
		Data_RefStdBofa.rsmFreqReadingRequired = rsmFreqReadingRequired;
	}

	public static boolean isReadEnergyAccumulateEnabled() {
		return readEnergyAccumulateEnabled;
	}

	public static void setReadEnergyAccumulateEnabled(boolean readEnergyAccumulated) {
		Data_RefStdBofa.readEnergyAccumulateEnabled = readEnergyAccumulated;
	}

	public static float getRefStdActiveEnergyAccumulatedInKwh() {
		return refStdActiveEnergyAccumulatedInKwh;
	}

	public static void setRefStdActiveEnergyAccumulatedInKwh(float refStdActiveEnergyAccumulated) {
		Data_RefStdBofa.refStdActiveEnergyAccumulatedInKwh = refStdActiveEnergyAccumulated;
	}

	/*	public static String getFormattedRsmActualFreqValue() {
		return formattedRsmActualFreqValue;
	}

	public static void setFormattedRsmActualFreqValue(String formattedRsmActualFreqValue) {
		Data_RefStdBofa.formattedRsmActualFreqValue = formattedRsmActualFreqValue;
	}*/




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

	//==========================================================================
}
