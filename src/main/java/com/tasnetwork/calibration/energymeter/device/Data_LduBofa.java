package com.tasnetwork.calibration.energymeter.device;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tasnetwork.calibration.energymeter.ApplicationHomeController;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantPowerSourceBofa;
import com.tasnetwork.calibration.energymeter.constant.ConstantPowerSourceLscs;
import com.tasnetwork.calibration.energymeter.constant.ConstantLduCcube;
import com.tasnetwork.calibration.energymeter.constant.ConstantLduLscs;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.constant.DeleteMeConstant;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.deployment.BofaManager;
import com.tasnetwork.calibration.energymeter.deployment.LiveTableDataManager;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
import com.tasnetwork.calibration.energymeter.testprofiles.TestProfileType;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;

public class Data_LduBofa {
	
	public static DecimalFormat numOfPulsesFormatter = new DecimalFormat("00");

	static DeviceDataManagerController DisplayDataObj =  new DeviceDataManagerController();
	public static SerialDataManager serialDM_Obj = new SerialDataManager();
	
	public static SerialDataManager SerialDM_Obj = new SerialDataManager();
	static Timer lduTimer;
	public static int TimeToBeExtendedInSec=120;
	public static boolean TimeExtendedForTimeBased = false;
	public static boolean SkipCurrentTP_Execution = false;
	//public static Communicator commLDU= null;
	private static int SerialLDU_ComRefreshDefaultTimeInMsec=800;//500;//1000;//200;//20000;	
	private static int SerialLDU_ComRefreshTimeInMsec= 800;//500;
	
	// VARIABLES - HOST 
	static String numberOfTurns              = ""; // Number of turns (2 bits)
	static String theoreticalNumberOfPulses  = ""; // Theoretical number of pulses (7 bits)
	static String pulseRatio                 = ""; // Ratio of meter pulse to turn pulse (4 bits)
	static String minuteOfCurrentTime        = ""; // Minute of current time (XX)
	static String secondOfCurrentTime        = ""; // Second of current time (XX)
	static String channelNumber              = ""; // Channel number
	
	// VARIABLES - SLAVE
	
	//// below need to be updated as array for 40 meters
	static ArrayList<Integer> nthOfErrors                   = new ArrayList<Integer>();//=0 // Nth of errors (0�9)
	static ArrayList<String> errorValue                 = new ArrayList<String>();//""; // Error (5 bits)
	static ArrayList<String> dialTestPulseCount             = new ArrayList<String>();//""; // Number of dial pulses (XXXXXXXX)
	static ArrayList<Integer> staCreeptTestPulseCount  = new ArrayList<Integer>();//= 0; // Pulse number of reference meter (8 bits)
	
		
	
	/////////end of array/////////
	
	//static int nthOfErrors                   = 0; // Nth of errors (0�9)
	static String errorSymbol                = ""; // Error symbol (+/- ASCII)
	//static String errorValue                 = ""; // Error (5 bits)
	//static String dialPulseCount             = ""; // Number of dial pulses (XXXXXXXX)
	static String referenceMeterPulseNumber  = ""; // Pulse number of reference meter (8 bits)
	
	//static int staTestPulseCount  = 0; // Pulse number of reference meter (8 bits)
	
	
	
	// FRAMES
	static String testTurnsAndPulsesFrame          = ""; // Command for receiving no. of test turns and theoretical no. of pulses
	static String readErrorsFrame                  = ""; // Read errors from error calculators
	
	static String prepareSearchMarkFrame           = ""; // Prepare to search mark
	static String beginSearchMarkFrame             = ""; // Begin to search mark
	static String queryMarkSearchResultFrame       = ""; // Query the result of mark-search
	static String markSearchEndFrame               = ""; // Command for mark-search end (Command for starting/creep test begin)
	static String queryEnablePulseFrame            = ""; // Query if enabling pulse is detected for starting/creep test
	static String startTestEndFrame                = ""; // Command for starting/creep test end (Command for error tester reset)
	static String stateQueryFrame                  = ""; // Command for state query
	
	static String dialTestStateEntryFrame          = ""; // Enter the state of dial test
	static String readDialPulsesFrame              = ""; // Read the no. of dial pulses
	static String readRefMeterPulseNumFrame        = ""; // Read the pulse no. of reference meter in error test
	static String dialTestStateExitFrame           = ""; // Exit the state of dial test
	
	
	static String relay485ConnectOrDisconnectFrame = ""; // Connect/disconnect the corresponding 485 relay
	static String pulseSwitchFrame                 = ""; // Switch the input pulse to active pulse/reactive pulse/multifunction test port
	static String stopRefreshErrorsFrame           = ""; // Stop refresh errors
	static String pulseInputChannelSelectFrame     = ""; // Select pulse input channel
	static String relayPositionControlFrame        = ""; // Connect/disconnect the voltage relay for one position
	static String displayDayErrorFrame             = ""; // Inform error calculator to display error of day
	static String timeSwitchingErrorTestEntryFrame = ""; // Enter the state of time switching error test (test the exact time of time switching)
	static String readSwitchingPulseTimeFrame      = ""; // Read the time for switching pulse detected
	static String errorTesterCheckResetFrame       = ""; // Error tester check rest
	static String doubleCircuitSwitchFrame         = ""; // Double circuit switch function
	
	
//==============================================================================================================  
		
	static void setTestTurnsAndPulsesCmdVariables(){
		ApplicationLauncher.logger.info("setTestTurnsAndPulsesCmdVariables:Entry");
		
		String numberOfTurns = "10" ;
		String theoreticalNumberOfPulses = "10" ;
		String pulseRatio = "10" ;
		
		//setNumberOfTurns(.getText()) ;  
		//setTheoreticalNumberOfPulses(.getText()) ;  
		//setPulseRatio(.getText()) ; 
		
		
		
		ApplicationLauncher.logger.info("setTestTurnsAndPulsesCmdVariables:Exit");	
	}

	static void setPulseSwitchCmdVariables(){
		ApplicationLauncher.logger.info("setPulseSwitchCmdVariables:Entry");
		//setChannelNumber(.getText()) ;  
		ApplicationLauncher.logger.info("setPulseSwitchCmdVariables:Exit");
	}

	static void setTimeSwitchingErrorTestEntryCmdVariables(){
		ApplicationLauncher.logger.info("setTimeSwitchingErrorTestEntryCmdVariables:Entry");
		//setMinuteOfCurrentTime(.getText()) ;  
		//setSecondOfCurrentTime(.getText()) ;  
		ApplicationLauncher.logger.info("setTimeSwitchingErrorTestEntryCmdVariables:Exit");
	}

//=====================================================================================================	



	public static void frameTestTurnsAndPulsesCmd(int inpAddress, String inpMeterConst,String inpNoOfPulses){   // "1"   
		ApplicationLauncher.logger.info("frameTestTurnsAndPulsesCmd:Entry");
		
		String addressStr = BofaManager.asciiToHex(String.valueOf((char)(inpAddress + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;
		 //	BofaManager.asciiToHex(address);
		ApplicationLauncher.logger.info("frameTestTurnsAndPulsesCmd: addressStr: " +addressStr);
		ApplicationLauncher.logger.info("frameTestTurnsAndPulsesCmd: inpMeterConst: " +inpMeterConst);
		ApplicationLauncher.logger.info("frameTestTurnsAndPulsesCmd: inpNoOfPulses: " +inpNoOfPulses);
		 // Frame the message
        StringBuilder messageBuilder = new StringBuilder();
        StringBuilder checkByteBuilder = new StringBuilder();
        if(inpAddress == ConstantPowerSourceBofa.LDU_INT_BROADCAST_ADDRESS) {
        	addressStr = ConstantPowerSourceBofa.LDU_HEX_BROADCAST_ADDRESS ;
        	ApplicationLauncher.logger.info("frameTestTurnsAndPulsesCmd: addressStr2: " +addressStr);
        }
        long liveRefMeterConstant = Data_RefStdBofa.getLiveRefMeterConstant() ;
        ApplicationLauncher.logger.info("frameTestTurnsAndPulsesCmd: liveRefMeterConstant: " +liveRefMeterConstant);
        //String meterConstantStr   = "03200.00" ;//"1234567"; // Meter constant (xxxxx.xx)
      //  int  meterConstant      = Integer.parseInt(Data_PowerSourceBofa.getMeterConstant());
        float  meterConstant      = Float.parseFloat(inpMeterConst);
        //String numOfPulse         = "10"; 
      //  int  numberOfPulse      = Integer.parseInt(Data_PowerSourceBofa.getNumOfPulse()); dsc
       int  numberOfPulse         = Integer.parseInt(inpNoOfPulses); // dsc             
        
        messageBuilder  =   messageBuilder.append(ConstantPowerSourceBofa.START_BYTE);                     // Start bit
                            messageBuilder.append(addressStr);                                             // Address
                            messageBuilder.append(ConstantPowerSourceBofa.TEST_TURNS_AND_PULSES_CMD);      // Command
                            //messageBuilder.append(getNumberOfTurns());                                      
                            messageBuilder.append(BofaManager.asciiToHex(numOfPulsesFormatter.format(numberOfPulse)));    
                           
                            
                            ApplicationLauncher.logger.info("liveRefMeterConstant : " + liveRefMeterConstant  );
                            ApplicationLauncher.logger.info("meterConstant        : " + meterConstant  );
                            ApplicationLauncher.logger.info("numberOfPulse        : " + numberOfPulse );
                            //ApplicationLauncher.logger.info("liveRefMeterConstant/meterConstant        : " + ((int)(liveRefMeterConstant/meterConstant))*numberOfPulse );
                            
                            
                             
                            
                            //setTheoreticalNumberOfPulses(BofaManager.asciiToHex(  (String.valueOf ((((int)(liveRefMeterConstant/meterConstant))*numberOfPulse))  ));
                            calculateTheoritcalNoOfPulses( liveRefMeterConstant,  meterConstant,  numberOfPulse);
                                
                            ApplicationLauncher.logger.info("TheoreticalNumberOfPulses : " + getTheoreticalNumberOfPulses() );
                            
                            messageBuilder.append(getTheoreticalNumberOfPulses());                         
                            
                            //setPulseRatio(BofaManager.asciiToHex("0001"));  
                            setPulseRatio(ConstantPowerSourceBofa.LDU_METER_PULSE_PER_TURN_PULSE);  
                            messageBuilder.append(getPulseRatio());                                         
                            
                            checkByteBuilder =  checkByteBuilder.append(ConstantPowerSourceBofa.START_BYTE);                     // Start bit
                            checkByteBuilder.append(ConstantPowerSourceBofa.LDU_HEX_BROADCAST_ADDRESS);                                             // Address
                             checkByteBuilder.append(ConstantPowerSourceBofa.TEST_TURNS_AND_PULSES_CMD);      // Command
					        //checkByteBuilder.append(getNumberOfTurns());  
                            checkByteBuilder.append(BofaManager.asciiToHex(numOfPulsesFormatter.format(numberOfPulse)));    
					        checkByteBuilder.append(getTheoreticalNumberOfPulses());                          
					        checkByteBuilder.append(getPulseRatio());              
                            
                            
                                                         
        //Calculate and append check bit
        String checkByte = GuiUtils.generateCheckSumWithOneByte(checkByteBuilder.toString()); 
        					messageBuilder.append(checkByte);
        // Append end bit
                            messageBuilder.append(ConstantPowerSourceBofa.END_BYTE);

        // Final message
        testTurnsAndPulsesFrame = messageBuilder.toString();
        ApplicationLauncher.logger.info("testTurnsAndPulsesFrame : " + testTurnsAndPulsesFrame);
        ApplicationLauncher.logger.info("frameTestTurnsAndPulsesCmd:Exit");
	}
	
	
	
	

	//===========================================================================================================

	public static void frameReadErrorsCmd(int address){   
		//ApplicationLauncher.logger.info("frameReadErrorsCmd:Entry : " + address);
		
		String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;
		//ApplicationLauncher.logger.debug("frameReadErrorsCmd: Slave Address: " + addressStr);

		 // Frame the message
        StringBuilder messageBuilder = new StringBuilder();
        StringBuilder checkByteBuilder = new StringBuilder();
        
        checkByteBuilder = checkByteBuilder.append(ConstantPowerSourceBofa.READ_ERRORS_CMD); // Command
        
        messageBuilder   =  messageBuilder.append(ConstantPowerSourceBofa.START_BYTE);      // Start bit
                            messageBuilder.append(addressStr);                              // Address
                            messageBuilder.append(ConstantPowerSourceBofa.READ_ERRORS_CMD); // Command

        // Calculate and append check bit
        String checkByte = GuiUtils.generateCheckSumWithOneByte(checkByteBuilder.toString());
        messageBuilder.append(checkByte);

        // Append end bit
        messageBuilder.append(ConstantPowerSourceBofa.END_BYTE);

        // Final message
        readErrorsFrame = messageBuilder.toString();
        //ApplicationLauncher.logger.info("readErrorsFrame : address : " + address + " -> " + readErrorsFrame);
        //ApplicationLauncher.logger.info("frameReadErrorsCmd:***********************************************");
        //ApplicationLauncher.logger.info("frameReadErrorsCmd:***********************************************");
        //ApplicationLauncher.logger.info("frameReadErrorsCmd:Exit");
	}
	//===========================================================================================================
public static void framePrepareSearchMarkCmd(int address){
	ApplicationLauncher.logger.info("framePrepareSearchMarkCmd:Entry");
	
	String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;
		 
	    // Frame the message
        StringBuilder messageBuilder = new StringBuilder();
        StringBuilder checkByteBuilder = new StringBuilder();
        
        checkByteBuilder = checkByteBuilder.append(ConstantPowerSourceBofa.PREPARE_SEARCH_MARK_CMD); // Command
        
        messageBuilder  =   messageBuilder.append(ConstantPowerSourceBofa.START_BYTE);                                        // Start bit
                            messageBuilder.append(addressStr);               // Address
                            messageBuilder.append(ConstantPowerSourceBofa.PREPARE_SEARCH_MARK_CMD); // Command
        // Calculate and append check bit
        String checkByte = GuiUtils.generateCheckSumWithOneByte(checkByteBuilder.toString());
        messageBuilder.append(checkByte);

        // Append end bit
        messageBuilder.append(ConstantPowerSourceBofa.END_BYTE);

        // Final message
        prepareSearchMarkFrame = messageBuilder.toString();
        ApplicationLauncher.logger.info("prepareSearchMarkFrame : " + prepareSearchMarkFrame);
        ApplicationLauncher.logger.info("framePrepareSearchMarkCmd:Exit");
	}
//===========================================================================================================
public static void frameBeginSearchMarkCmd(int address){
	ApplicationLauncher.logger.info("frameBeginSearchMarkCmd:Entry");
	
	String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;
		
	    // Frame the message
        StringBuilder messageBuilder = new StringBuilder();
        StringBuilder checkByteBuilder = new StringBuilder();
        
        checkByteBuilder = checkByteBuilder.append(ConstantPowerSourceBofa.BEGIN_SEARCH_MARK_CMD); // Command
        
        messageBuilder  =   messageBuilder.append(ConstantPowerSourceBofa.START_BYTE);                                        // Start bit
                            messageBuilder.append(addressStr);               // Address
                            messageBuilder.append(ConstantPowerSourceBofa.BEGIN_SEARCH_MARK_CMD); // Command
        // Calculate and append check bit
        String checkByte = GuiUtils.generateCheckSumWithOneByte(checkByteBuilder.toString());
        messageBuilder.append(checkByte);

        // Append end bit
        messageBuilder.append(ConstantPowerSourceBofa.END_BYTE);

        // Final message
        beginSearchMarkFrame = messageBuilder.toString();
        ApplicationLauncher.logger.info("beginSearchMarkFrame : " + beginSearchMarkFrame);
        ApplicationLauncher.logger.info("frameBeginSearchMarkCmd:Exit");     
	}
//===========================================================================================================
public static void frameQueryMarkSearchResultCmd(int address){
	ApplicationLauncher.logger.info("frameQueryMarkSearchResultCmd:Entry");
	
	String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;
	
		 // Frame the message
        StringBuilder messageBuilder = new StringBuilder();
        StringBuilder checkByteBuilder = new StringBuilder();
        
        checkByteBuilder = checkByteBuilder.append(ConstantPowerSourceBofa.QUERY_MARK_SEARCH_RESULT_CMD); // Command
        
        messageBuilder  =   messageBuilder.append(ConstantPowerSourceBofa.START_BYTE);                                        // Start bit
                            messageBuilder.append(addressStr);               // Address
                            messageBuilder.append(ConstantPowerSourceBofa.QUERY_MARK_SEARCH_RESULT_CMD); // Command
        // Calculate and append check bit
        String checkByte = GuiUtils.generateCheckSumWithOneByte(checkByteBuilder.toString());
        messageBuilder.append(checkByte);

        // Append end bit
        messageBuilder.append(ConstantPowerSourceBofa.END_BYTE);

        // Final message
        queryMarkSearchResultFrame = messageBuilder.toString();
        ApplicationLauncher.logger.info("queryMarkSearchResultFrame : " + queryMarkSearchResultFrame);
        ApplicationLauncher.logger.info("frameQueryMarkSearchResultCmd:Exit");
	}
//===========================================================================================================
public static void frameMarkSearchEndCmd(int address){
	ApplicationLauncher.logger.info("frameMarkSearchEndCmd:Entry");
	
	String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;
	
    if(address == ConstantPowerSourceBofa.LDU_INT_BROADCAST_ADDRESS) {
    	addressStr = ConstantPowerSourceBofa.LDU_HEX_BROADCAST_ADDRESS ;
    	ApplicationLauncher.logger.info("frameTestTurnsAndPulsesCmd: addressStr2: " +addressStr);
    }
	
	// Frame the message
        StringBuilder messageBuilder = new StringBuilder();
      StringBuilder checkByteBuilder = new StringBuilder();
        
        checkByteBuilder = checkByteBuilder.append(ConstantPowerSourceBofa.MARK_SEARCH_END_CMD); // Command
        
        messageBuilder  =   messageBuilder.append(ConstantPowerSourceBofa.START_BYTE);                                        // Start bit
                            messageBuilder.append(addressStr);               // Address
                            messageBuilder.append(ConstantPowerSourceBofa.MARK_SEARCH_END_CMD); // Command
        // Calculate and append check bit
        String checkByte = GuiUtils.generateCheckSumWithOneByte(checkByteBuilder.toString());
        messageBuilder.append(checkByte);

        // Append end bit
        messageBuilder.append(ConstantPowerSourceBofa.END_BYTE);

        // Final message
        markSearchEndFrame = messageBuilder.toString();
        ApplicationLauncher.logger.info("markSearchEndFrame : " + markSearchEndFrame);
        ApplicationLauncher.logger.info("frameMarkSearchEndCmd:Exit");
	}
//===========================================================================================================
public static void frameQueryEnablePulseCmd(int address){
	ApplicationLauncher.logger.info("frameQueryEnablePulseCmd:Entry");
	
	String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;
		 // Frame the message
        StringBuilder messageBuilder = new StringBuilder();
      StringBuilder checkByteBuilder = new StringBuilder();
        
        checkByteBuilder = checkByteBuilder.append(ConstantPowerSourceBofa.QUERY_ENABLE_PULSE_CMD); // Command
        
        messageBuilder  =   messageBuilder.append(ConstantPowerSourceBofa.START_BYTE);                                        // Start bit
                            messageBuilder.append(addressStr);               // Address
                            messageBuilder.append(ConstantPowerSourceBofa.QUERY_ENABLE_PULSE_CMD); // Command
        // Calculate and append check bit
        String checkByte = GuiUtils.generateCheckSumWithOneByte(checkByteBuilder.toString());
        messageBuilder.append(checkByte);

        // Append end bit
        messageBuilder.append(ConstantPowerSourceBofa.END_BYTE);

        // Final message
        queryEnablePulseFrame = messageBuilder.toString();
        ApplicationLauncher.logger.info("queryEnablePulseFrame : " + queryEnablePulseFrame);
        ApplicationLauncher.logger.info("frameQueryEnablePulseCmd:Exit");
	}
//===========================================================================================================
public static void frameStartTestEndCmd(int address){
	ApplicationLauncher.logger.info("frameStartTestEndCmd:Entry");
	
	String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;
		 // Frame the message
        StringBuilder messageBuilder = new StringBuilder();
      StringBuilder checkByteBuilder = new StringBuilder();
        
        checkByteBuilder = checkByteBuilder.append(ConstantPowerSourceBofa.START_TEST_END_CMD); // Command
        
        messageBuilder  =   messageBuilder.append(ConstantPowerSourceBofa.START_BYTE);                                        // Start bit
                            messageBuilder.append(addressStr);               // Address
                            messageBuilder.append(ConstantPowerSourceBofa.START_TEST_END_CMD); // Command
        // Calculate and append check bit
        String checkByte = GuiUtils.generateCheckSumWithOneByte(checkByteBuilder.toString());
        messageBuilder.append(checkByte);

        // Append end bit
        messageBuilder.append(ConstantPowerSourceBofa.END_BYTE);

        // Final message
        startTestEndFrame = messageBuilder.toString();
        ApplicationLauncher.logger.info("startTestEndFrame : " + startTestEndFrame);
        ApplicationLauncher.logger.info("frameStartTestEndCmd:Exit"); 
	}
//===========================================================================================================
public static void frameStateQueryCmd(int address){
	ApplicationLauncher.logger.info("frameStateQueryCmd:Entry");
	
	String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;
		 // Frame the message
        StringBuilder messageBuilder = new StringBuilder();
      StringBuilder checkByteBuilder = new StringBuilder();
        
        checkByteBuilder = checkByteBuilder.append(ConstantPowerSourceBofa.STATE_QUERY_CMD); // Command
        
        messageBuilder  =   messageBuilder.append(ConstantPowerSourceBofa.START_BYTE);                                        // Start bit
                            messageBuilder.append(addressStr);               // Address
                            messageBuilder.append(ConstantPowerSourceBofa.STATE_QUERY_CMD); // Command
        // Calculate and append check bit
        String checkByte = GuiUtils.generateCheckSumWithOneByte(checkByteBuilder.toString()); 
        messageBuilder.append(checkByte);

        // Append end bit
        messageBuilder.append(ConstantPowerSourceBofa.END_BYTE);

        // Final message
        stateQueryFrame = messageBuilder.toString();
        ApplicationLauncher.logger.info("stateQueryFrame : " + stateQueryFrame);
        ApplicationLauncher.logger.info("frameStateQueryCmd:Exit");
	}
//===========================================================================================================
public static void frameDialTestStateEntryCmd(int address){
	ApplicationLauncher.logger.info("frameDialTestStateEntryCmd:Entry");
	
	String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;	
	 
	if (address == ConstantPowerSourceBofa.LDU_INT_BROADCAST_ADDRESS) {
		 addressStr = ConstantPowerSourceBofa.LDU_HEX_BROADCAST_ADDRESS ;
	 }
	
	// Frame the message
	StringBuilder messageBuilder = new StringBuilder();
	StringBuilder checkByteBuilder = new StringBuilder();

	checkByteBuilder = checkByteBuilder.append(ConstantPowerSourceBofa.ENTER_DIAL_TEST_STATE_CMD); // Command

	messageBuilder  =   messageBuilder.append(ConstantPowerSourceBofa.START_BYTE);                                        // Start bit
	messageBuilder.append(addressStr);               // Address
	messageBuilder.append(ConstantPowerSourceBofa.ENTER_DIAL_TEST_STATE_CMD); // Command
	// Calculate and append check bit
	String checkByte = GuiUtils.generateCheckSumWithOneByte(checkByteBuilder.toString()); 
	messageBuilder.append(checkByte);

	// Append end bit
	messageBuilder.append(ConstantPowerSourceBofa.END_BYTE);

	// Final message
	dialTestStateEntryFrame = messageBuilder.toString();
	ApplicationLauncher.logger.info("dialTestStateEntryFrame : " + dialTestStateEntryFrame);
	ApplicationLauncher.logger.info("frameDialTestStateEntryCmd:Exit");
}
//===========================================================================================================
public static void frameReadDialPulsesCmd(int address){
	ApplicationLauncher.logger.info("frameReadDialPulsesCmd:Entry");
	
	String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;
		 // Frame the message
        StringBuilder messageBuilder = new StringBuilder();
      StringBuilder checkByteBuilder = new StringBuilder();
        
        checkByteBuilder = checkByteBuilder.append(ConstantPowerSourceBofa.READ_DIAL_PULSES_CMD); // Command
        
        messageBuilder  =   messageBuilder.append(ConstantPowerSourceBofa.START_BYTE);                                        // Start bit
                            messageBuilder.append(addressStr);               // Address
                            messageBuilder.append(ConstantPowerSourceBofa.READ_DIAL_PULSES_CMD); // Command
        // Calculate and append check bit
        String checkByte = GuiUtils.generateCheckSumWithOneByte(checkByteBuilder.toString()); 
        messageBuilder.append(checkByte);

        // Append end bit
        messageBuilder.append(ConstantPowerSourceBofa.END_BYTE);

        // Final message
        readDialPulsesFrame = messageBuilder.toString();
        ApplicationLauncher.logger.info("readDialPulsesFrame : " + readDialPulsesFrame);
        ApplicationLauncher.logger.info("frameReadDialPulsesCmd:Exit");    
	}
//===========================================================================================================
public static void frameDialTestStateExitCmd(int address){
	ApplicationLauncher.logger.info("frameDialTestStateExitCmd:Entry");
	
	String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;
		
	 if (address == ConstantPowerSourceBofa.LDU_INT_BROADCAST_ADDRESS) {
		 addressStr = ConstantPowerSourceBofa.LDU_HEX_BROADCAST_ADDRESS ;
	 }	
	
	// Frame the message
        StringBuilder messageBuilder = new StringBuilder();
      StringBuilder checkByteBuilder = new StringBuilder();
        
        checkByteBuilder = checkByteBuilder.append(ConstantPowerSourceBofa.DIAL_TEST_STATE_EXIT_CMD); // Command
        
        messageBuilder  =   messageBuilder.append(ConstantPowerSourceBofa.START_BYTE);                                        // Start bit
                            messageBuilder.append(addressStr);               // Address
                            messageBuilder.append(ConstantPowerSourceBofa.DIAL_TEST_STATE_EXIT_CMD); // Command
        // Calculate and append check bit
        String checkByte = GuiUtils.generateCheckSumWithOneByte(checkByteBuilder.toString());
        messageBuilder.append(checkByte);

        // Append end bit
        messageBuilder.append(ConstantPowerSourceBofa.END_BYTE);

        // Final message
        dialTestStateExitFrame = messageBuilder.toString();
        ApplicationLauncher.logger.info("dialTestStateExitFrame : " + dialTestStateExitFrame);
        ApplicationLauncher.logger.info("frameDialTestStateExitCmd:Exit");
	}
//===========================================================================================================
public static void frameRelay485ConnectOrDisconnectCmd(int address){
	ApplicationLauncher.logger.info("frameRelay485ConnectOrDisconnectCmd:Entry");
	
	String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;
		 // Frame the message
        StringBuilder messageBuilder = new StringBuilder();
      StringBuilder checkByteBuilder = new StringBuilder();
        
        checkByteBuilder = checkByteBuilder.append(ConstantPowerSourceBofa.RELAY_485_CONNECT_OR_DISCONNECT_CMD); // Command
        
        messageBuilder  =   messageBuilder.append(ConstantPowerSourceBofa.START_BYTE);                                        // Start bit
                            messageBuilder.append(addressStr);               // Address
                            messageBuilder.append(ConstantPowerSourceBofa.RELAY_485_CONNECT_OR_DISCONNECT_CMD); // Command
        // Calculate and append check bit
        String checkByte = GuiUtils.generateCheckSumWithOneByte(checkByteBuilder.toString()); 
        messageBuilder.append(checkByte);

        // Append end bit
        messageBuilder.append(ConstantPowerSourceBofa.END_BYTE);

        // Final message
        relay485ConnectOrDisconnectFrame = messageBuilder.toString();
        ApplicationLauncher.logger.info("relay485ConnectOrDisconnectFrame : " + relay485ConnectOrDisconnectFrame);
        ApplicationLauncher.logger.info("frameRelay485ConnectOrDisconnectCmd:Exit");   
	}
//===========================================================================================================

public static void framePulseSwitchCmd(int address){    
	ApplicationLauncher.logger.info("framePulseSwitchCmd:Entry");
	
	String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;
		 // Frame the message
        StringBuilder messageBuilder = new StringBuilder();
      StringBuilder checkByteBuilder = new StringBuilder();
        
        checkByteBuilder = checkByteBuilder.append(ConstantPowerSourceBofa.PULSE_SWITCH_CMD); // Command
        
        messageBuilder  =   messageBuilder.append(ConstantPowerSourceBofa.START_BYTE);                                        // Start bit
                            messageBuilder.append(addressStr);               // Address
                            messageBuilder.append(ConstantPowerSourceBofa.PULSE_SWITCH_CMD); // Command
                            messageBuilder.append(getChannelNumber());
        // Calculate and append check bit
        String checkByte = GuiUtils.generateCheckSumWithOneByte(checkByteBuilder.toString());
        messageBuilder.append(checkByte);

        // Append end bit
        messageBuilder.append(ConstantPowerSourceBofa.END_BYTE);

        // Final message
        pulseSwitchFrame = messageBuilder.toString();
        ApplicationLauncher.logger.info("pulseSwitchFrame : " + pulseSwitchFrame);
    ApplicationLauncher.logger.info("framePulseSwitchCmd:Exit"); 
	}
//===========================================================================================================
public static void frameStopRefreshErrorsCmd(int address){
	ApplicationLauncher.logger.info("frameStopRefreshErrorsCmd:Entry");
	
	String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;
		 // Frame the message
        StringBuilder messageBuilder = new StringBuilder();
      StringBuilder checkByteBuilder = new StringBuilder();
        
        checkByteBuilder = checkByteBuilder.append(ConstantPowerSourceBofa.STOP_REFRESH_ERRORS_CMD); // Command
        
        messageBuilder  =   messageBuilder.append(ConstantPowerSourceBofa.START_BYTE);                                        // Start bit
                            messageBuilder.append(addressStr);               // Address
                            messageBuilder.append(ConstantPowerSourceBofa.STOP_REFRESH_ERRORS_CMD); // Command
        // Calculate and append check bit
        String checkByte = GuiUtils.generateCheckSumWithOneByte(checkByteBuilder.toString());
        messageBuilder.append(checkByte);

        // Append end bit
        messageBuilder.append(ConstantPowerSourceBofa.END_BYTE);

        // Final message
        stopRefreshErrorsFrame = messageBuilder.toString();
        ApplicationLauncher.logger.info("stopRefreshErrorsFrame : " + stopRefreshErrorsFrame);
        ApplicationLauncher.logger.info("frameStopRefreshErrorsCmd:Exit");  
	}
//===========================================================================================================
public static void frameReadRefMeterPulseNumCmd(int address){
	ApplicationLauncher.logger.info("frameReadRefMeterPulseNumCmd:Entry");
	
	String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;
		 // Frame the message
        StringBuilder messageBuilder = new StringBuilder();
      StringBuilder checkByteBuilder = new StringBuilder();
        
        checkByteBuilder = checkByteBuilder.append(ConstantPowerSourceBofa.READ_REF_METER_PULSE_NUM_CMD); // Command
        
        messageBuilder  =   messageBuilder.append(ConstantPowerSourceBofa.START_BYTE);                                        // Start bit
                            messageBuilder.append(addressStr);               // Address
                            messageBuilder.append(ConstantPowerSourceBofa.READ_REF_METER_PULSE_NUM_CMD); // Command
        // Calculate and append check bit
        String checkByte = GuiUtils.generateCheckSumWithOneByte(checkByteBuilder.toString());
        messageBuilder.append(checkByte);

        // Append end bit
        messageBuilder.append(ConstantPowerSourceBofa.END_BYTE);

        // Final message
        readRefMeterPulseNumFrame = messageBuilder.toString();
        ApplicationLauncher.logger.info("readRefMeterPulseNumFrame : " + readRefMeterPulseNumFrame);
        ApplicationLauncher.logger.info("frameReadRefMeterPulseNumCmd:Exit");  
	}
//===========================================================================================================
	public static void framePulseInputChannelSelectCmd(int address){
		ApplicationLauncher.logger.info("framePulseInputChannelSelectCmd:Entry");
		
		String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;
		 // Frame the message
        StringBuilder messageBuilder = new StringBuilder();
      StringBuilder checkByteBuilder = new StringBuilder();
        
        checkByteBuilder = checkByteBuilder.append(ConstantPowerSourceBofa.PULSE_INPUT_CHANNEL_SELECT_CMD); // Command
        
        messageBuilder  =   messageBuilder.append(ConstantPowerSourceBofa.START_BYTE);                                        // Start bit
                            messageBuilder.append(addressStr);               // Address
                            messageBuilder.append(ConstantPowerSourceBofa.PULSE_INPUT_CHANNEL_SELECT_CMD); // Command
        // Calculate and append check bit
        String checkByte = GuiUtils.generateCheckSumWithOneByte(checkByteBuilder.toString());
        messageBuilder.append(checkByte);

        // Append end bit
        messageBuilder.append(ConstantPowerSourceBofa.END_BYTE);

        // Final message
        pulseInputChannelSelectFrame = messageBuilder.toString();
        ApplicationLauncher.logger.info("pulseInputChannelSelectFrame : " + pulseInputChannelSelectFrame);
        ApplicationLauncher.logger.info("framePulseInputChannelSelectCmd:Exit"); 
	}
	//===========================================================================================================
public static void frameRelayPositionControlCmd(int address){
	ApplicationLauncher.logger.info("frameRelayPositionControlCmd:Entry");
	
	String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;
		 // Frame the message
        StringBuilder messageBuilder = new StringBuilder();
      StringBuilder checkByteBuilder = new StringBuilder();
        
        checkByteBuilder = checkByteBuilder.append(ConstantPowerSourceBofa.RELAY_POSITION_CONTROL_CMD); // Command
        
        messageBuilder  =   messageBuilder.append(ConstantPowerSourceBofa.START_BYTE);                                        // Start bit
                            messageBuilder.append(addressStr);               // Address
                            messageBuilder.append(ConstantPowerSourceBofa.RELAY_POSITION_CONTROL_CMD); // Command
        // Calculate and append check bit
        String checkByte = GuiUtils.generateCheckSumWithOneByte(checkByteBuilder.toString());
        messageBuilder.append(checkByte);

        // Append end bit
        messageBuilder.append(ConstantPowerSourceBofa.END_BYTE);

        // Final message
        relayPositionControlFrame = messageBuilder.toString();
        ApplicationLauncher.logger.info("relayPositionControlFrame : " + relayPositionControlFrame);
        ApplicationLauncher.logger.info("frameRelayPositionControlCmd:Exit");    
	}
//===========================================================================================================
public static void frameDisplayDayErrorCmd(int address){
	ApplicationLauncher.logger.info("frameDisplayDayErrorCmd:Entry");
	
	String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;
		 // Frame the message
        StringBuilder messageBuilder = new StringBuilder();
      StringBuilder checkByteBuilder = new StringBuilder();
        
        checkByteBuilder = checkByteBuilder.append(ConstantPowerSourceBofa.DISPLAY_DAY_ERROR_CMD); // Command
        
        messageBuilder  =   messageBuilder.append(ConstantPowerSourceBofa.START_BYTE);                                        // Start bit
                            messageBuilder.append(addressStr);               // Address
                            messageBuilder.append(ConstantPowerSourceBofa.DISPLAY_DAY_ERROR_CMD); // Command
        // Calculate and append check bit
        String checkByte = GuiUtils.generateCheckSumWithOneByte(checkByteBuilder.toString());
        messageBuilder.append(checkByte);

        // Append end bit
        messageBuilder.append(ConstantPowerSourceBofa.END_BYTE);

        // Final message
        displayDayErrorFrame = messageBuilder.toString();
        ApplicationLauncher.logger.info("displayDayErrorFrame : " + displayDayErrorFrame);
        ApplicationLauncher.logger.info("frameDisplayDayErrorCmd:Exit");
	}
//===========================================================================================================
 
public static void frameTimeSwitchingErrorTestEntryCmd(int address){    
	ApplicationLauncher.logger.info("frameTimeSwitchingErrorTestEntryCmd:Entry");
	
	String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;
	
		 // Frame the message
        StringBuilder messageBuilder = new StringBuilder();
      StringBuilder checkByteBuilder = new StringBuilder();
        
        checkByteBuilder = checkByteBuilder.append(ConstantPowerSourceBofa.TIME_SWITCHING_ERROR_TEST_ENTRY_CMD); // Command
        
        messageBuilder   =  messageBuilder.append(ConstantPowerSourceBofa.START_BYTE);                                        // Start bit
                            messageBuilder.append(addressStr);                           // Address
                            messageBuilder.append(ConstantPowerSourceBofa.TIME_SWITCHING_ERROR_TEST_ENTRY_CMD); // Command
                            messageBuilder.append(getMinuteOfCurrentTime());
                            messageBuilder.append(getSecondOfCurrentTime());
        // Calculate and append check bit
        String checkByte = GuiUtils.generateCheckSumWithOneByte(checkByteBuilder.toString());
        messageBuilder.append(checkByte);

        // Append end bit
        messageBuilder.append(ConstantPowerSourceBofa.END_BYTE);

        // Final message
        timeSwitchingErrorTestEntryFrame = messageBuilder.toString();
        ApplicationLauncher.logger.info("timeSwitchingErrorTestEntryFrame : " + timeSwitchingErrorTestEntryFrame);
        ApplicationLauncher.logger.info("frameTimeSwitchingErrorTestEntryCmd:Exit"); 
	}
//===========================================================================================================
public static void frameReadSwitchingTimeCmd(int address){
	ApplicationLauncher.logger.info("frameReadSwitchingTimeCmd:Entry");
	
	String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ; //xyz
		 // Frame the message
        StringBuilder messageBuilder = new StringBuilder();
      StringBuilder checkByteBuilder = new StringBuilder();
        
        checkByteBuilder = checkByteBuilder.append(ConstantPowerSourceBofa.READ_SWITCHING_TIME_CMD); // Command
        
        messageBuilder  =   messageBuilder.append(ConstantPowerSourceBofa.START_BYTE);                                        // Start bit
                            messageBuilder.append(addressStr);               // Address
                            messageBuilder.append(ConstantPowerSourceBofa.READ_SWITCHING_TIME_CMD); // Command
        // Calculate and append check bit
        String checkByte = GuiUtils.generateCheckSumWithOneByte(checkByteBuilder.toString());
        messageBuilder.append(checkByte);

        // Append end bit
        messageBuilder.append(ConstantPowerSourceBofa.END_BYTE);

        // Final message
        readSwitchingPulseTimeFrame = messageBuilder.toString();
        ApplicationLauncher.logger.info("readSwitchingPulseTimeFrame : " + readSwitchingPulseTimeFrame);
        ApplicationLauncher.logger.info("frameReadSwitchingTimeCmd:Exit");
	}
//===========================================================================================================
public static void frameErrorTesterCheckResetCmd(int address){
	ApplicationLauncher.logger.info("frameErrorTesterCheckResetCmd");
	
	 String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;
	 
	 if (address == ConstantPowerSourceBofa.LDU_INT_BROADCAST_ADDRESS) {
		 addressStr = ConstantPowerSourceBofa.LDU_HEX_BROADCAST_ADDRESS ;
	 }
		 // Frame the message
        StringBuilder messageBuilder = new StringBuilder();
      StringBuilder checkByteBuilder = new StringBuilder();
        
        checkByteBuilder = checkByteBuilder.append(ConstantPowerSourceBofa.ERROR_TESTER_CHECK_REST_CMD); // Command
        
        messageBuilder  =   messageBuilder.append(ConstantPowerSourceBofa.START_BYTE);                  // Start bit
                            messageBuilder.append(addressStr);                                          // Address
                            messageBuilder.append(ConstantPowerSourceBofa.ERROR_TESTER_CHECK_REST_CMD); // Command
        // Calculate and append check bit
        String checkByte = GuiUtils.generateCheckSumWithOneByte(checkByteBuilder.toString()); 
        messageBuilder.append(checkByte);

        // Append end bit
        messageBuilder.append(ConstantPowerSourceBofa.END_BYTE);

        // Final message
        errorTesterCheckResetFrame = messageBuilder.toString();
        ApplicationLauncher.logger.info("frameErrorTesterCheckResetCmd : " + errorTesterCheckResetFrame);
        ApplicationLauncher.logger.info("frameErrorTesterCheckResetCmd: Exit");
	}
//===========================================================================================================
public static void frameDoubleCircuitSwitchCmd(int address, String circuit){  
	ApplicationLauncher.logger.info("frameDoubleCircuitSwitchCmd : Entry");
	ApplicationLauncher.logger.info("sendTestTurnsAndPulsesCmd   : Address          : " + address);
	ApplicationLauncher.logger.info("sendTestTurnsAndPulsesCmd   : Circuit Selected : " + circuit);
 


	 String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;
	 
	 /*if (address == ConstantPowerSourceBofa.LDU_INT_BROADCAST_ADDRESS) {
		 addressStr = ConstantPowerSourceBofa.SLAVE_01_ADRS ;
	 }*/

  	if(circuit.equals(ConstantPowerSourceBofa.CIRCUIT_1_ID)) {
  		circuit = "31" ;
	} else if(circuit.equals(ConstantPowerSourceBofa.CIRCUIT_2_ID)) {
		circuit = "32" ;
	}
	/*else{
		return ;
	}*/
	
	// Frame the message
        StringBuilder messageBuilder = new StringBuilder();
        StringBuilder checkByteBuilder = new StringBuilder();
        
        checkByteBuilder = checkByteBuilder.append(ConstantPowerSourceBofa.DOUBLE_CIRCUIT_SWITCH_CMD); // Command
                           checkByteBuilder.append(circuit); 
                           
        messageBuilder  =   messageBuilder.append(ConstantPowerSourceBofa.START_BYTE);                 // Start bit
					        messageBuilder.append(addressStr);                                          // Address
					        messageBuilder.append(ConstantPowerSourceBofa.DOUBLE_CIRCUIT_SWITCH_CMD);  // Command
					        messageBuilder.append(circuit);                                            // circuitNum

        // Calculate and append check bit
        String checkByte = GuiUtils.generateCheckSumWithOneByte(checkByteBuilder.toString());
        messageBuilder.append(checkByte);

        // Append end bit
        messageBuilder.append(ConstantPowerSourceBofa.END_BYTE);

        // Final message
        doubleCircuitSwitchFrame = messageBuilder.toString();
        ApplicationLauncher.logger.info("doubleCircuitSwitchFrame : " + doubleCircuitSwitchFrame);
        ApplicationLauncher.logger.info("frameDoubleCircuitSwitchCmd :Exit");
        
        ApplicationLauncher.logger.info("frameDoubleCircuitSwitchCmd   : *****************************");
	}

//===========================================================================================================
//===========================================================================================================
	
	static boolean sendTestTurnsAndPulsesCmd(int address, boolean responseExpected){
		ApplicationLauncher.logger.info("sendTestTurnsAndPulsesCmd: Entry");
		ApplicationLauncher.logger.info("sendTestTurnsAndPulsesCmd: address: " +address);
		
		//frameTestTurnsAndPulsesCmd(address);
		String payLoadInHex = testTurnsAndPulsesFrame;
		ApplicationLauncher.logger.info("sendTestTurnsAndPulsesCmd : payLoadInHex: " + payLoadInHex);
		boolean status = false;
		int timeDelayInMilliSec = 0;
		String expectedDataInHex = ConstantPowerSourceBofa.ER_STARTS_WITH;
		boolean isResponseExpected = responseExpected;//true;
		
		/*String responseStatus = BofaManager.getSerialDM_Obj().powerSourceSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
			if (responseStatus == DeleteMeConstant.SUCCESS_RESPONSE) {
				String CurrentReadData = BofaManager.getSerialDM_Obj().getRxMsgQ_PwrSrc().getLastReadMessage();//""; // BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
				status = processResponse(testTurnsAndPulsesFrame, CurrentReadData);  
			}*/	
			
			
			
			Map<String,Object> responseReturn = new HashMap<String,Object>();
			boolean forceExecute = false;
			responseReturn = BofaManager.sendDataToBofaAfterSemaPhoreAcquired("sendTestTurnsAndPulsesCmd",payLoadInHex, timeDelayInMilliSec,
					                                              expectedDataInHex, isResponseExpected,forceExecute);
			status = (boolean)responseReturn.get("status");
			if(status){
				String responseData = (String)responseReturn.get("result");
				status =  processResponse(testTurnsAndPulsesFrame, responseData, address );		
			}	
			
		ApplicationLauncher.logger.info("sendTestTurnsAndPulsesCmd: Exit");
		return status;
	}
	
	static boolean sendBroadCastTestTurnsAndPulsesCmd(){
		ApplicationLauncher.logger.info("sendBroadCastTestTurnsAndPulsesCmd: Entry");
		//ApplicationLauncher.logger.info("sendBroadCastTestTurnsAndPulsesCmd: address: " +address);
		//int address = address;
		//frameTestTurnsAndPulsesCmd(address);
		String payLoadInHex = testTurnsAndPulsesFrame;
		ApplicationLauncher.logger.info("sendBroadCastTestTurnsAndPulsesCmd : payLoadInHex: " + payLoadInHex);
		boolean status = true;
		int timeDelayInMilliSec = 0;
		String expectedDataInHex = ConstantPowerSourceBofa.ER_STARTS_WITH;
		boolean isResponseExpected = false;
		
//		BofaManager.getSerialDM_Obj().powerSourceSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
/*			if (responseStatus == DeleteMeConstant.SUCCESS_RESPONSE) {
				String CurrentReadData = BofaManager.getSerialDM_Obj().getRxMsgQ_PwrSrc().getLastReadMessage();//""; // BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
				status = processResponse(testTurnsAndPulsesFrame, CurrentReadData);  
			}	*/
		
		Map<String,Object> responseReturn = new HashMap<String,Object>();
		boolean forceExecute = false;
		responseReturn = BofaManager.sendDataToBofaAfterSemaPhoreAcquired("sendBroadCastTestTurnsAndPulsesCmd",payLoadInHex, timeDelayInMilliSec,
				                                              expectedDataInHex, isResponseExpected,forceExecute);
		status = (boolean)responseReturn.get("status");
		if(status){
			if(isResponseExpected) {
				String responseData = (String)responseReturn.get("result");
				//status =  processResponse(testTurnsAndPulsesFrame, responseData, address);	
			}
		}	
		ApplicationLauncher.logger.info("sendBroadCastTestTurnsAndPulsesCmd: Exit");
		return status;
	}
	//===========================================================================================================

	static boolean sendReadErrorsCmd(int address){   
		//ApplicationLauncher.logger.info("sendReadErrorsCmd: Entry");
		frameReadErrorsCmd(address);
		String payLoadInHex = readErrorsFrame;
		ApplicationLauncher.logger.info("sendReadErrorsCmd : payLoadInHex: " + payLoadInHex  );
		
		String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;
		
		boolean status = false;
		int timeDelayInMilliSec = 0;
		String expectedDataInHex = ConstantPowerSourceBofa.ER_LDU_STARTS_WITH + addressStr;
		boolean isResponseExpected = true;
		
				
		/*String responseStatus = BofaManager.getSerialDM_Obj().powerSourceSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
			if (responseStatus == DeleteMeConstant.SUCCESS_RESPONSE) {
				String CurrentReadData = BofaManager.getSerialDM_Obj().getRxMsgQ_PwrSrc().getLastReadMessage();//BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
				status = processResponse(readErrorsFrame,CurrentReadData);   
			}	
			*/
			
			
			Map<String,Object> responseReturn = new HashMap<String,Object>();
			boolean forceExecute = false;
			responseReturn = BofaManager.sendDataToBofaAfterSemaPhoreAcquired("sendReadErrorsCmd",payLoadInHex, timeDelayInMilliSec,
					                                              expectedDataInHex, isResponseExpected,forceExecute);
			status = (boolean)responseReturn.get("status");
			if(status){
				String responseData = (String)responseReturn.get("result");
				status =  processResponse(readErrorsFrame, responseData, address);		
			}
			
			
		//ApplicationLauncher.logger.info("sendReadErrorsCmd: Exit");
		return status;
	}
	//===========================================================================================================

	static boolean sendPrepareSearchMarkCmd(int address){
		ApplicationLauncher.logger.info("sendPrepareSearchMarkCmd: Entry");
		framePrepareSearchMarkCmd(address);
		String payLoadInHex = prepareSearchMarkFrame ;
		ApplicationLauncher.logger.info("sendPrepareSearchMarkCmd : payLoadInHex: " + payLoadInHex  );
		
		String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;

		int timeDelayInMilliSec = 0;
		String expectedDataInHex = ConstantPowerSourceBofa.ER_LDU_STARTS_WITH + addressStr;
		boolean isResponseExpected = true;
		boolean status = false;
		
	/*	String responseStatus = BofaManager.getSerialDM_Obj().powerSourceSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
			if (responseStatus == DeleteMeConstant.SUCCESS_RESPONSE) {
				String CurrentReadData = BofaManager.getSerialDM_Obj().getRxMsgQ_PwrSrc().getLastReadMessage();//BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
				status = processResponse(prepareSearchMarkFrame,CurrentReadData);  
			}*/	
			
			
			Map<String,Object> responseReturn = new HashMap<String,Object>();
			boolean forceExecute = false;
			responseReturn = BofaManager.sendDataToBofaAfterSemaPhoreAcquired("sendPrepareSearchMarkCmd",payLoadInHex, timeDelayInMilliSec,
					                                              expectedDataInHex, isResponseExpected,forceExecute);
			status = (boolean)responseReturn.get("status");
			if(status){
				String responseData = (String)responseReturn.get("result");
				status =  processResponse(prepareSearchMarkFrame, responseData, address);		
			}
			
			
		ApplicationLauncher.logger.info("sendPrepareSearchMarkCmd: Exit");
		return status;
	}
	//===========================================================================================================

	static boolean sendBeginSearchMarkCmd(int address){
		ApplicationLauncher.logger.info("sendBeginSearchMarkCmd: Entry");
		frameBeginSearchMarkCmd(address);
		String payLoadInHex = beginSearchMarkFrame ;
		ApplicationLauncher.logger.info("VI_StartProcess : payLoadInHex: " + payLoadInHex  );
		
		String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;

		boolean status = false;
		int timeDelayInMilliSec = 0;
		String expectedDataInHex = ConstantPowerSourceBofa.ER_LDU_STARTS_WITH + addressStr;
		boolean isResponseExpected = true;
		
		/*String responseStatus = BofaManager.getSerialDM_Obj().powerSourceSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
			if (responseStatus == DeleteMeConstant.SUCCESS_RESPONSE) {
				String CurrentReadData = BofaManager.getSerialDM_Obj().getRxMsgQ_PwrSrc().getLastReadMessage(); // BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
				status = processResponse(beginSearchMarkFrame,CurrentReadData);  
			}*/
			
			Map<String,Object> responseReturn = new HashMap<String,Object>();
			boolean forceExecute = false;
			responseReturn = BofaManager.sendDataToBofaAfterSemaPhoreAcquired("sendBeginSearchMarkCmd",payLoadInHex, timeDelayInMilliSec,
					                                              expectedDataInHex, isResponseExpected,forceExecute);
			status = (boolean)responseReturn.get("status");
			if(status){
				String responseData = (String)responseReturn.get("result");
				status =  processResponse(beginSearchMarkFrame, responseData, address);		
			}	
			 
		ApplicationLauncher.logger.info("sendBeginSearchMarkCmd: Exit");
		return status;
	}
	//===========================================================================================================

	static boolean sendQueryMarkSearchResultCmd(int address){
		ApplicationLauncher.logger.info("sendQueryMarkSearchResultCmd: Entry");
		frameQueryMarkSearchResultCmd(address);
		String payLoadInHex = queryMarkSearchResultFrame ;
		ApplicationLauncher.logger.info("sendQueryMarkSearchResultCmd : payLoadInHex: " + payLoadInHex  );
		
		String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;

		boolean status = false;
		int timeDelayInMilliSec = 0;
		String expectedDataInHex = ConstantPowerSourceBofa.ER_LDU_STARTS_WITH + addressStr;
		boolean isResponseExpected = true;
		
		/*String responseStatus = BofaManager.getSerialDM_Obj().powerSourceSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
			if (responseStatus == DeleteMeConstant.SUCCESS_RESPONSE) {
				String CurrentReadData = BofaManager.getSerialDM_Obj().getRxMsgQ_PwrSrc().getLastReadMessage(); // BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
				status = processResponse(queryMarkSearchResultFrame,CurrentReadData);  
			}*/
			
			Map<String,Object> responseReturn = new HashMap<String,Object>();
			boolean forceExecute = false;
			responseReturn = BofaManager.sendDataToBofaAfterSemaPhoreAcquired("sendQueryMarkSearchResultCmd",payLoadInHex, timeDelayInMilliSec,
					                                              expectedDataInHex, isResponseExpected,forceExecute);
			status = (boolean)responseReturn.get("status");
			if(status){
				String responseData = (String)responseReturn.get("result");
				status =  processResponse(queryMarkSearchResultFrame, responseData, address);		
			}
			
		ApplicationLauncher.logger.info("sendQueryMarkSearchResultCmd: Exit");
		return status;
	}
	//===========================================================================================================

	
	
	
	//static boolean sendMarkSearchEndCmd(int address){
	static boolean sendStartSTA_CreepCmd(int address){
		ApplicationLauncher.logger.info("sendStartSTA_CreepCmd: Entry");
		frameMarkSearchEndCmd(address);
		String payLoadInHex = markSearchEndFrame ;
		ApplicationLauncher.logger.info("sendStartSTA_CreepCmd : payLoadInHex: " + payLoadInHex  );
		
		String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;

		 if (address == ConstantPowerSourceBofa.LDU_INT_BROADCAST_ADDRESS) {
			 addressStr = ConstantPowerSourceBofa.LDU_HEX_BROADCAST_ADDRESS ;
		 }
		 
		boolean status = false;
		int timeDelayInMilliSec = 0;
		String expectedDataInHex = ConstantPowerSourceBofa.ER_LDU_STARTS_WITH + addressStr;
		boolean isResponseExpected = true;
		
		if (address == ConstantPowerSourceBofa.LDU_INT_BROADCAST_ADDRESS) {
			isResponseExpected = false ;
		 }
		
		/*String responseStatus = BofaManager.getSerialDM_Obj().powerSourceSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
			if (responseStatus == DeleteMeConstant.SUCCESS_RESPONSE) {
				String CurrentReadData = BofaManager.getSerialDM_Obj().getRxMsgQ_PwrSrc().getLastReadMessage(); // BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
				status = processResponse(markSearchEndFrame,CurrentReadData);  
			}*/
			
			Map<String,Object> responseReturn = new HashMap<String,Object>();
			boolean forceExecute = false;
			responseReturn = BofaManager.sendDataToBofaAfterSemaPhoreAcquired("sendStartSTA_CreepCmd",payLoadInHex, timeDelayInMilliSec,
					                                              expectedDataInHex, isResponseExpected,forceExecute);
			status = (boolean)responseReturn.get("status");
			if(status){
				if (isResponseExpected) {
					String responseData = (String)responseReturn.get("result");
					status =  processResponse(markSearchEndFrame, responseData, address);	
				}		
			}	
			
			
			
		ApplicationLauncher.logger.info("sendStartSTA_CreepCmd: Exit");
		return status;
	}
	//===========================================================================================================

	static boolean sendQueryEnablePulseCmd(int address){
		ApplicationLauncher.logger.info("sendQueryEnablePulseCmd: Entry");
		frameQueryEnablePulseCmd(address);
		String payLoadInHex = queryEnablePulseFrame;
		ApplicationLauncher.logger.info("sendQueryEnablePulseCmd : payLoadInHex: " + payLoadInHex  );
		
		String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;

		boolean status = false;
		int timeDelayInMilliSec = 0;
		String expectedDataInHex = ConstantPowerSourceBofa.ER_LDU_STARTS_WITH + addressStr;
		boolean isResponseExpected = true;
		
	/*	String responseStatus = BofaManager.getSerialDM_Obj().powerSourceSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
			if (responseStatus == DeleteMeConstant.SUCCESS_RESPONSE) {
				String CurrentReadData = BofaManager.getSerialDM_Obj().getRxMsgQ_PwrSrc().getLastReadMessage(); // BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
				status = processResponse(queryEnablePulseFrame,CurrentReadData);  
			}*/
			
			Map<String,Object> responseReturn = new HashMap<String,Object>();
			boolean forceExecute = false;
			responseReturn = BofaManager.sendDataToBofaAfterSemaPhoreAcquired("sendQueryEnablePulseCmd",payLoadInHex, timeDelayInMilliSec,
					                                              expectedDataInHex, isResponseExpected,forceExecute);
			status = (boolean)responseReturn.get("status");
			if(status){
				String responseData = (String)responseReturn.get("result");
				status =  processResponse(queryEnablePulseFrame, responseData, address);		
			}	
			
			
		ApplicationLauncher.logger.info("sendQueryEnablePulseCmd: Exit");
		return status;
	}
	//===========================================================================================================

	static boolean sendStartTestEndCmd(int address){
		ApplicationLauncher.logger.info("sendStartTestEndCmd: Entry");
		frameStartTestEndCmd(address);
		String payLoadInHex = startTestEndFrame ;
		ApplicationLauncher.logger.info("sendStartTestEndCmd : payLoadInHex: " + payLoadInHex  );
		
		String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;

		if (address == ConstantPowerSourceBofa.LDU_INT_BROADCAST_ADDRESS) {
			 addressStr = ConstantPowerSourceBofa.LDU_HEX_BROADCAST_ADDRESS ;
		 }
		
		boolean status = false;
		int timeDelayInMilliSec = 0;
		String expectedDataInHex = ConstantPowerSourceBofa.ER_LDU_STARTS_WITH + addressStr;
		boolean isResponseExpected = true;
		
		if (address == ConstantPowerSourceBofa.LDU_INT_BROADCAST_ADDRESS) {
			isResponseExpected = false ;
		 }
	/*	String responseStatus = BofaManager.getSerialDM_Obj().powerSourceSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
			if (responseStatus == DeleteMeConstant.SUCCESS_RESPONSE) {
				String CurrentReadData = BofaManager.getSerialDM_Obj().getRxMsgQ_PwrSrc().getLastReadMessage(); // BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
				status = processResponse(startTestEndFrame,CurrentReadData);  
			}*/
			
			Map<String,Object> responseReturn = new HashMap<String,Object>();
			boolean forceExecute = false;
			responseReturn = BofaManager.sendDataToBofaAfterSemaPhoreAcquired("sendStartTestEndCmd",payLoadInHex, timeDelayInMilliSec,
					                                              expectedDataInHex, isResponseExpected,forceExecute);
			status = (boolean)responseReturn.get("status");
			if(status){
				if (isResponseExpected) {
					String responseData = (String)responseReturn.get("result");
					status =  processResponse(startTestEndFrame, responseData, address);
				}			
			}		
		
		ApplicationLauncher.logger.info("sendStartTestEndCmd: Exit");
		return status;
	}
	//===========================================================================================================

	static boolean sendStateQueryCmd(int address){
		ApplicationLauncher.logger.info("sendStateQueryCmd: Entry");
		frameStateQueryCmd(address);
		String payLoadInHex = stateQueryFrame;
		ApplicationLauncher.logger.info("sendStateQueryCmd : payLoadInHex: " + payLoadInHex  );
		
		String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;

		boolean status = false;
		int timeDelayInMilliSec = 0;
		String expectedDataInHex = ConstantPowerSourceBofa.ER_LDU_STARTS_WITH + addressStr;
		boolean isResponseExpected = true;
		
	/*	String responseStatus = BofaManager.getSerialDM_Obj().powerSourceSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
			if (responseStatus == DeleteMeConstant.SUCCESS_RESPONSE) {
				String CurrentReadData = BofaManager.getSerialDM_Obj().getRxMsgQ_PwrSrc().getLastReadMessage(); // BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
				status = processResponse(stateQueryFrame,CurrentReadData);  
			}*/
			
			
			Map<String,Object> responseReturn = new HashMap<String,Object>();
			boolean forceExecute = false;
			responseReturn = BofaManager.sendDataToBofaAfterSemaPhoreAcquired("sendStateQueryCmd",payLoadInHex, timeDelayInMilliSec,
					                                              expectedDataInHex, isResponseExpected,forceExecute);
			status = (boolean)responseReturn.get("status");
			if(status){
				String responseData = (String)responseReturn.get("result");
				status =  processResponse(stateQueryFrame, responseData, address);		
			}	
			
			
		ApplicationLauncher.logger.info("sendStateQueryCmd: Exit");
		return status;
	}
	//===========================================================================================================

	
	static boolean sendDialTestStateEntryCmd(int address){
		ApplicationLauncher.logger.info("sendDialTestStateEntryCmd: Entry");
		frameDialTestStateEntryCmd(address);
		String payLoadInHex = dialTestStateEntryFrame;
		ApplicationLauncher.logger.info("sendDialTestStateEntryCmd : payLoadInHex: " + payLoadInHex  );
		
		String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;
		if (address == ConstantPowerSourceBofa.LDU_INT_BROADCAST_ADDRESS) {
			 addressStr = ConstantPowerSourceBofa.LDU_HEX_BROADCAST_ADDRESS ;
		 }
		boolean status = false;
		int timeDelayInMilliSec = 0;
		String expectedDataInHex = ConstantPowerSourceBofa.ER_LDU_STARTS_WITH + addressStr;
		boolean isResponseExpected = true;
		if (address == ConstantPowerSourceBofa.LDU_INT_BROADCAST_ADDRESS) {
			isResponseExpected = false ;
		 }
	/*	String responseStatus = BofaManager.getSerialDM_Obj().powerSourceSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
			if (responseStatus == DeleteMeConstant.SUCCESS_RESPONSE) {
				String CurrentReadData = BofaManager.getSerialDM_Obj().getRxMsgQ_PwrSrc().getLastReadMessage(); // BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
				status = processResponse(dialTestStateEntryFrame,CurrentReadData);  
			}*/
			
			

			Map<String,Object> responseReturn = new HashMap<String,Object>();
			boolean forceExecute = false;
			responseReturn = BofaManager.sendDataToBofaAfterSemaPhoreAcquired("sendDialTestStateEntryCmd",payLoadInHex, timeDelayInMilliSec,
					                                              expectedDataInHex, isResponseExpected,forceExecute);
			status = (boolean)responseReturn.get("status");
			if(status){
				if (isResponseExpected) {
					String responseData = (String)responseReturn.get("result");
					status =  processResponse(dialTestStateEntryFrame, responseData, address);		
				}
			}	
			
			
		ApplicationLauncher.logger.info("sendDialTestStateEntryCmd: Exit");
		return status;
	}

	public static boolean bofaSendLduDialTestStartAll(){
		ApplicationLauncher.logger.debug("bofaSendLduDialTestStartAll: Entry");
		
		int broadCastddress = ConstantPowerSourceBofa.LDU_INT_BROADCAST_ADDRESS;
		boolean status = sendDialTestStateEntryCmd(broadCastddress);
		ApplicationLauncher.logger.debug("bofaSendLduDialTestStartAll: status1: " + status);
/*		
		if(status){
			status = sendDialTestStateExitCmd(broadCastddress);
			ApplicationLauncher.logger.debug("bofaSendLduDialTestStartAll: status2: " + status);
		}*/
		return status;
	}
	//===========================================================================================================

	static boolean sendReadDialPulsesCmd(int address){
		ApplicationLauncher.logger.info("sendReadDialPulsesCmd: Entry");
		
		frameReadDialPulsesCmd(address);
		String payLoadInHex = readDialPulsesFrame;
		ApplicationLauncher.logger.info("sendReadDialPulsesCmd: " + payLoadInHex  );
		
		String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;

		boolean status = false;
		int timeDelayInMilliSec = 0;
		//String expectedDataInHex = ConstantPowerSourceBofa.ER_STARTS_WITH; fhfg
		String expectedDataInHex = ConstantPowerSourceBofa.ER_LDU_STARTS_WITH + addressStr;
		boolean isResponseExpected = true;
		
/*		String responseStatus = BofaManager.getSerialDM_Obj().powerSourceSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
			if (responseStatus == DeleteMeConstant.SUCCESS_RESPONSE) {
				String CurrentReadData = BofaManager.getSerialDM_Obj().getRxMsgQ_PwrSrc().getLastReadMessage(); // BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
				status = processResponse(readDialPulsesFrame,CurrentReadData);  
			}*/
			
			
			Map<String,Object> responseReturn = new HashMap<String,Object>();
			boolean forceExecute = false;
			responseReturn = BofaManager.sendDataToBofaAfterSemaPhoreAcquired("sendReadDialPulsesCmd",payLoadInHex, timeDelayInMilliSec,
					                                              expectedDataInHex, isResponseExpected,forceExecute);
			status = (boolean)responseReturn.get("status");
			if(status){
				String responseData = (String)responseReturn.get("result");
				status =  processResponse(readDialPulsesFrame, responseData, address);		
			}	
			
	    ApplicationLauncher.logger.info("sendReadDialPulsesCmd: Staus : " + status);

		ApplicationLauncher.logger.info("sendReadDialPulsesCmd: Exit");
		return status;
	}
	//===========================================================================================================

	static boolean sendDialTestStateExitCmd(int address){

		ApplicationLauncher.logger.info("sendDialTestStateExitCmd: Entry");
		frameDialTestStateExitCmd(address);
		String payLoadInHex = dialTestStateExitFrame ;
		ApplicationLauncher.logger.info("sendDialTestStateExitCmd : payLoadInHex: " + payLoadInHex  );
		
		String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;

		boolean status = false;
		int timeDelayInMilliSec = 0;
		ApplicationLauncher.logger.info("sendDialTestStateExitCmd : addressStr: " + addressStr  );
		String expectedDataInHex = ConstantPowerSourceBofa.ER_LDU_STARTS_WITH + addressStr;
		ApplicationLauncher.logger.info("sendDialTestStateExitCmd : expectedDataInHex: " + expectedDataInHex  );
		boolean isResponseExpected = true;
		
		if (address == ConstantPowerSourceBofa.LDU_INT_BROADCAST_ADDRESS) {
			isResponseExpected = false ;
		 }
		
		/*String responseStatus = BofaManager.getSerialDM_Obj().powerSourceSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
			if (responseStatus == DeleteMeConstant.SUCCESS_RESPONSE) {
				String CurrentReadData = BofaManager.getSerialDM_Obj().getRxMsgQ_PwrSrc().getLastReadMessage(); // BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
				status = processResponse(dialTestStateExitFrame,CurrentReadData);   
			}*/
			
			Map<String,Object> responseReturn = new HashMap<String,Object>();
			boolean forceExecute = false;
			responseReturn = BofaManager.sendDataToBofaAfterSemaPhoreAcquired("sendDialTestStateExitCmd",payLoadInHex, timeDelayInMilliSec,
					                                              expectedDataInHex, isResponseExpected,forceExecute);
			status = (boolean)responseReturn.get("status");
			if(status){
				if (address != ConstantPowerSourceBofa.LDU_INT_BROADCAST_ADDRESS) {
					String responseData = (String)responseReturn.get("result");
					status =  processResponse(dialTestStateExitFrame, responseData, address);
				}
			}	
			
			
		ApplicationLauncher.logger.info("sendDialTestStateExitCmd: Exit");
		return status;
	}
	//===========================================================================================================

	static boolean sendRelay485ConnectOrDisconnectCmd(int address){
		ApplicationLauncher.logger.info("sendRelay485ConnectOrDisconnectCmd: Entry");
		frameRelay485ConnectOrDisconnectCmd(address);
		String payLoadInHex = relay485ConnectOrDisconnectFrame;
		ApplicationLauncher.logger.info("sendRelay485ConnectOrDisconnectCmd : payLoadInHex: " + payLoadInHex  );
		
		String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;

		boolean status = false;
		int timeDelayInMilliSec = 0;
		String expectedDataInHex = ConstantPowerSourceBofa.ER_LDU_STARTS_WITH + addressStr;
		boolean isResponseExpected = true;
		
	/*	String responseStatus = BofaManager.getSerialDM_Obj().powerSourceSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
			if (responseStatus == DeleteMeConstant.SUCCESS_RESPONSE) {
				String CurrentReadData = BofaManager.getSerialDM_Obj().getRxMsgQ_PwrSrc().getLastReadMessage(); // BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
				status = processResponse(relay485ConnectOrDisconnectFrame,CurrentReadData);  
			}
			*/
			

			Map<String,Object> responseReturn = new HashMap<String,Object>();
			boolean forceExecute = false;
			responseReturn = BofaManager.sendDataToBofaAfterSemaPhoreAcquired("sendRelay485ConnectOrDisconnectCmd",payLoadInHex, timeDelayInMilliSec,
					                                              expectedDataInHex, isResponseExpected,forceExecute);
			status = (boolean)responseReturn.get("status");
			if(status){
				String responseData = (String)responseReturn.get("result");
				status =  processResponse(relay485ConnectOrDisconnectFrame, responseData, address);		
			}	
			
			
		ApplicationLauncher.logger.info("sendRelay485ConnectOrDisconnectCmd: Exit");
		return status;
	}
	//===========================================================================================================

	static boolean sendPulseSwitchCmd(int address){
		ApplicationLauncher.logger.info("sendPulseSwitchCmd: Entry");
		framePulseSwitchCmd(address);
		String payLoadInHex = pulseSwitchFrame ;
		ApplicationLauncher.logger.info("sendPulseSwitchCmd : payLoadInHex: " + payLoadInHex  );
		
		String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;

		boolean status = false;
		int timeDelayInMilliSec = 0;
		String expectedDataInHex = ConstantPowerSourceBofa.ER_LDU_STARTS_WITH + addressStr;
		boolean isResponseExpected = true;
		
	/*	String responseStatus = BofaManager.getSerialDM_Obj().powerSourceSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
			if (responseStatus == DeleteMeConstant.SUCCESS_RESPONSE) {
				String CurrentReadData = ""; // BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
				status = processResponse(pulseSwitchFrame,CurrentReadData);  
			}*/
			
			Map<String,Object> responseReturn = new HashMap<String,Object>();
			boolean forceExecute = false;
			responseReturn = BofaManager.sendDataToBofaAfterSemaPhoreAcquired("sendPulseSwitchCmd",payLoadInHex, timeDelayInMilliSec,
					                                              expectedDataInHex, isResponseExpected,forceExecute);
			status = (boolean)responseReturn.get("status");
			if(status){
				String responseData = (String)responseReturn.get("result");
				status =  processResponse(pulseSwitchFrame, responseData, address);		
			}		
			
		ApplicationLauncher.logger.info("sendPulseSwitchCmd: Exit");
		return status;
	}
	//===========================================================================================================

	static boolean sendStopRefreshErrorsCmd(int address){
		ApplicationLauncher.logger.info("sendStopRefreshErrorsCmd: Entry");
		frameStopRefreshErrorsCmd(address);
		String payLoadInHex = stopRefreshErrorsFrame;
		ApplicationLauncher.logger.info("sendStopRefreshErrorsCmd : payLoadInHex: " + payLoadInHex  );
		
		String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;

		boolean status = false;
		int timeDelayInMilliSec = 0;
		String expectedDataInHex = ConstantPowerSourceBofa.ER_LDU_STARTS_WITH + addressStr;
		boolean isResponseExpected = true;
		
		/*String responseStatus = BofaManager.getSerialDM_Obj().powerSourceSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
			if (responseStatus == DeleteMeConstant.SUCCESS_RESPONSE) {
				String CurrentReadData = BofaManager.getSerialDM_Obj().getRxMsgQ_PwrSrc().getLastReadMessage(); // BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
				status = processResponse(stopRefreshErrorsFrame,CurrentReadData);  
			}*/
		
		Map<String,Object> responseReturn = new HashMap<String,Object>();
		boolean forceExecute = false;
		responseReturn = BofaManager.sendDataToBofaAfterSemaPhoreAcquired("sendStopRefreshErrorsCmd",payLoadInHex, timeDelayInMilliSec,
				                                              expectedDataInHex, isResponseExpected,forceExecute);
		status = (boolean)responseReturn.get("status");
		if(status){
			String responseData = (String)responseReturn.get("result");
			status =  processResponse(stopRefreshErrorsFrame, responseData, address);		
		}		
			
			
		ApplicationLauncher.logger.info("sendStopRefreshErrorsCmd: Exit");
		return status;
	}
	//===========================================================================================================

	static boolean sendReadRefMeterPulseNumCmd(int address){
		ApplicationLauncher.logger.info("sendReadRefMeterPulseNumCmd: Entry");
		frameReadRefMeterPulseNumCmd(address);
		String payLoadInHex = readRefMeterPulseNumFrame;
		ApplicationLauncher.logger.info("sendReadRefMeterPulseNumCmd : payLoadInHex: " + payLoadInHex  );
		
		String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;

		boolean status = false;
		int timeDelayInMilliSec = 0;
		String expectedDataInHex = ConstantPowerSourceBofa.ER_LDU_STARTS_WITH + addressStr;
		boolean isResponseExpected = true;
		
/*		String responseStatus = BofaManager.getSerialDM_Obj().powerSourceSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
			if (responseStatus == DeleteMeConstant.SUCCESS_RESPONSE) {
				String CurrentReadData = BofaManager.getSerialDM_Obj().getRxMsgQ_PwrSrc().getLastReadMessage(); // BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
				status = processResponse(readRefMeterPulseNumFrame,CurrentReadData);  
			} */
			
			Map<String,Object> responseReturn = new HashMap<String,Object>();
			boolean forceExecute = false;
			
			
			responseReturn = BofaManager.sendDataToBofaAfterSemaPhoreAcquired("sendReadRefMeterPulseNumCmd",payLoadInHex, timeDelayInMilliSec,
						                                              expectedDataInHex, isResponseExpected,forceExecute);
			
			status = (boolean)responseReturn.get("status");
			if(status){
				String responseData = (String)responseReturn.get("result");
				status =  processResponse(readRefMeterPulseNumFrame, responseData, address);		
			}		
			
			
		ApplicationLauncher.logger.info("sendReadRefMeterPulseNumCmd: Exit");
		return status;
	}
	//===========================================================================================================

	static boolean sendPulseInputChannelSelectCmd(int address){
		ApplicationLauncher.logger.info("sendPulseInputChannelSelectCmd: Entry");
		framePulseInputChannelSelectCmd(address);
		String payLoadInHex = pulseInputChannelSelectFrame ;
		ApplicationLauncher.logger.info("sendPulseInputChannelSelectCmd : payLoadInHex: " + payLoadInHex  );
		
		String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;

		boolean status = false;
		int timeDelayInMilliSec = 0;
		String expectedDataInHex = ConstantPowerSourceBofa.ER_LDU_STARTS_WITH + addressStr;
		boolean isResponseExpected = true;
		
		/*String responseStatus = BofaManager.getSerialDM_Obj().powerSourceSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
			if (responseStatus == DeleteMeConstant.SUCCESS_RESPONSE) {
				String CurrentReadData = BofaManager.getSerialDM_Obj().getRxMsgQ_PwrSrc().getLastReadMessage(); // BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
				status = processResponse(pulseInputChannelSelectFrame,CurrentReadData);    
			}*/
			
			Map<String,Object> responseReturn = new HashMap<String,Object>();
			boolean forceExecute = false;
			responseReturn = BofaManager.sendDataToBofaAfterSemaPhoreAcquired("sendPulseInputChannelSelectCmd",payLoadInHex, timeDelayInMilliSec,
					                                              expectedDataInHex, isResponseExpected,forceExecute);
			status = (boolean)responseReturn.get("status");
			if(status){
				String responseData = (String)responseReturn.get("result");
				status =  processResponse(pulseInputChannelSelectFrame, responseData, address);		
			}		
			
		ApplicationLauncher.logger.info("sendPulseInputChannelSelectCmd: Exit");
		return status;
	}
	//===========================================================================================================

	static boolean sendRelayPositionControlCmd(int address){
		ApplicationLauncher.logger.info("frameRelayPositionControlCmd: Entry");
		frameRelayPositionControlCmd(address);
		String payLoadInHex = relayPositionControlFrame;
		ApplicationLauncher.logger.info("sendRelayPositionControlCmd : payLoadInHex: " + payLoadInHex  );
		
		String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;

		boolean status = false;
		int timeDelayInMilliSec = 0; 
		String expectedDataInHex = ConstantPowerSourceBofa.ER_LDU_STARTS_WITH + addressStr;
		boolean isResponseExpected = true;
		
		/*String responseStatus = BofaManager.getSerialDM_Obj().powerSourceSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
			if (responseStatus == DeleteMeConstant.SUCCESS_RESPONSE) {
				String CurrentReadData = BofaManager.getSerialDM_Obj().getRxMsgQ_PwrSrc().getLastReadMessage(); // BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
				status = processResponse(relayPositionControlFrame,CurrentReadData);  
			}*/
			
			
			Map<String,Object> responseReturn = new HashMap<String,Object>();
			boolean forceExecute = false;
			responseReturn = BofaManager.sendDataToBofaAfterSemaPhoreAcquired("sendRelayPositionControlCmd",payLoadInHex, timeDelayInMilliSec,
					                                              expectedDataInHex, isResponseExpected,forceExecute);
			status = (boolean)responseReturn.get("status");
			if(status){
				String responseData = (String)responseReturn.get("result");
				status =  processResponse(relayPositionControlFrame, responseData, address);		
			}		
			
			
		ApplicationLauncher.logger.info("frameRelayPositionControlCmd: Exit");
		return status;
	}
	//===========================================================================================================

	static boolean sendDisplayDayErrorCmd(int address){
		ApplicationLauncher.logger.info("frameDisplayDayErrorCmd: Entry");
		frameDisplayDayErrorCmd(address);
		String payLoadInHex = displayDayErrorFrame ;
		ApplicationLauncher.logger.info("VI_StartProcess : payLoadInHex: " + payLoadInHex  );
		
		String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;

		boolean status = false;
		int timeDelayInMilliSec = 0;
		String expectedDataInHex = ConstantPowerSourceBofa.ER_LDU_STARTS_WITH + addressStr;
		boolean isResponseExpected = true;
		
		/*String responseStatus = BofaManager.getSerialDM_Obj().powerSourceSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
			if (responseStatus == DeleteMeConstant.SUCCESS_RESPONSE) {
				String CurrentReadData = BofaManager.getSerialDM_Obj().getRxMsgQ_PwrSrc().getLastReadMessage(); // BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
				status = processResponse(displayDayErrorFrame,CurrentReadData);  
			}*/
			
			Map<String,Object> responseReturn = new HashMap<String,Object>();
			boolean forceExecute = false;
			responseReturn = BofaManager.sendDataToBofaAfterSemaPhoreAcquired("sendDisplayDayErrorCmd",payLoadInHex, timeDelayInMilliSec,
					                                              expectedDataInHex, isResponseExpected,forceExecute);
			status = (boolean)responseReturn.get("status");
			if(status){
				String responseData = (String)responseReturn.get("result");
				status =  processResponse(displayDayErrorFrame, responseData, address);		
			}			
			
			
		ApplicationLauncher.logger.info("frameDisplayDayErrorCmd: Exit");
		return status;
	}
	//===========================================================================================================
	
	static boolean sendTimeSwitchingErrorTestEntryCmd(int address){
		ApplicationLauncher.logger.info("frameTimeSwitchingErrorTestEntryCmd: Entry");
		frameTimeSwitchingErrorTestEntryCmd(address);
		String payLoadInHex = timeSwitchingErrorTestEntryFrame;
		ApplicationLauncher.logger.info("sendTimeSwitchingErrorTestEntryCmd : payLoadInHex: " + payLoadInHex  );
		
		String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;

		boolean status = false;
		int timeDelayInMilliSec = 0;
		String expectedDataInHex = ConstantPowerSourceBofa.ER_LDU_STARTS_WITH + addressStr;
		boolean isResponseExpected = true;
		
	/*	String responseStatus = BofaManager.getSerialDM_Obj().powerSourceSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
			if (responseStatus == DeleteMeConstant.SUCCESS_RESPONSE) {
				String CurrentReadData = BofaManager.getSerialDM_Obj().getRxMsgQ_PwrSrc().getLastReadMessage(); // BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
				status = processResponse(timeSwitchingErrorTestEntryFrame,CurrentReadData);  
			}*/
			
			Map<String,Object> responseReturn = new HashMap<String,Object>();
			boolean forceExecute = false;
			responseReturn = BofaManager.sendDataToBofaAfterSemaPhoreAcquired("sendTimeSwitchingErrorTestEntryCmd",payLoadInHex, timeDelayInMilliSec,
					                                              expectedDataInHex, isResponseExpected,forceExecute);
			status = (boolean)responseReturn.get("status");
			if(status){
				String responseData = (String)responseReturn.get("result");
				status =  processResponse(timeSwitchingErrorTestEntryFrame, responseData, address);		
			}			
			
		ApplicationLauncher.logger.info("frameTimeSwitchingErrorTestEntryCmd: Exit");
		return status;
	}
	//===========================================================================================================

	static boolean sendReadSwitchingTimeCmd(int address){  
		ApplicationLauncher.logger.info("frameReadSwitchingTimeCmd: Entry");
		frameReadSwitchingTimeCmd(address);
		String payLoadInHex = readSwitchingPulseTimeFrame;
		ApplicationLauncher.logger.info("VI_StartProcess : payLoadInHex: " + payLoadInHex  );
		
		String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;

		boolean status = false;
		int timeDelayInMilliSec = 0;
		String expectedDataInHex = ConstantPowerSourceBofa.ER_LDU_STARTS_WITH + addressStr;
		boolean isResponseExpected = true;
		
		String responseStatus = BofaManager.getSerialDM_Obj().powerSourceSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
			if (responseStatus == DeleteMeConstant.SUCCESS_RESPONSE) {
				String CurrentReadData = BofaManager.getSerialDM_Obj().getRxMsgQ_PwrSrc().getLastReadMessage(); // BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
				status = processResponse(readSwitchingPulseTimeFrame,CurrentReadData, address);  
			}
		ApplicationLauncher.logger.info("frameReadSwitchingTimeCmd: Exit");
		return status;
	}
	//===========================================================================================================

	public static boolean bofaSendLduResetErrorAll(){
		ApplicationLauncher.logger.debug("bofaSendLduResetErrorAll: Entry");
		
		int broadCastddress = ConstantPowerSourceBofa.LDU_INT_BROADCAST_ADDRESS;
		boolean status = sendErrorTesterCheckResetCmd(broadCastddress);
		ApplicationLauncher.logger.debug("bofaSendLduResetErrorAll: status1: " + status);
		
		if(status){
			status = sendDialTestStateExitCmd(broadCastddress);
			ApplicationLauncher.logger.debug("bofaSendLduResetErrorAll: status2: " + status);
		}
		return status;
	}
	
	//===========================================================================================================
	
	public static boolean bofaSendLduExitDialTest(){
		ApplicationLauncher.logger.debug("bofaSendLduExitDialTest: Entry");
		
		int broadCastddress = ConstantPowerSourceBofa.LDU_INT_BROADCAST_ADDRESS;
		boolean status = sendDialTestStateExitCmd(broadCastddress);
		ApplicationLauncher.logger.debug("bofaSendLduExitDialTest: status2: " + status);
		
		return status;
	}
	
	//===========================================================================================================
	public static boolean bofaSendSwitchCtCircuit(int address, String circuit){  
		ApplicationLauncher.logger.debug("bofaSendSwitchCtCircuit: Entry");
		boolean status = false ;
		
		if (circuit.equals(ConstantPowerSourceLscs.CMD_PWR_SRC_MAIN_CT_MODE_HDR) || circuit.equals(ConstantPowerSourceLscs.CMD_PWR_SRC_NEUTRAL_CT_MODE_HDR)) {	
			
			if (circuit.equals( ConstantPowerSourceLscs.CMD_PWR_SRC_MAIN_CT_MODE_HDR)) {
				circuit = ConstantPowerSourceBofa.CIRCUIT_1_ID ;
			} 
			else if (circuit.equals( ConstantPowerSourceLscs.CMD_PWR_SRC_NEUTRAL_CT_MODE_HDR)) {
				circuit = ConstantPowerSourceBofa.CIRCUIT_2_ID ;
			}
 		status = sendDoubleCircuitSwitchCmd(address, circuit);
		}
		else{
			
		}
	
		ApplicationLauncher.logger.debug("bofaSendSwitchCtCircuit: status2: " + status);
		
		return status;
	}
	
	//===========================================================================================================
	
	static boolean sendErrorTesterCheckResetCmd(int address){
		ApplicationLauncher.logger.info("sendErrorTesterCheckResetCmd: Entry");
		frameErrorTesterCheckResetCmd(address);
		String payLoadInHex = errorTesterCheckResetFrame;
		ApplicationLauncher.logger.info("sendErrorTesterCheckResetCmd : payLoadInHex: " + payLoadInHex  );
		boolean status = false;
		int timeDelayInMilliSec = 0;
		String expectedDataInHex = ConstantPowerSourceBofa.ER_STARTS_WITH;
		boolean isResponseExpected = true;
		
		if (address == ConstantPowerSourceBofa.LDU_INT_BROADCAST_ADDRESS) {
			isResponseExpected = false ;
		 }
		
		/*String responseStatus = BofaManager.getSerialDM_Obj().powerSourceSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
			if (responseStatus == DeleteMeConstant.SUCCESS_RESPONSE) {
				String CurrentReadData = BofaManager.getSerialDM_Obj().getRxMsgQ_PwrSrc().getLastReadMessage(); // BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
				status = processResponse(errorTesterCheckResetFrame,CurrentReadData);    
			}*/
			
			Map<String,Object> responseReturn = new HashMap<String,Object>();
			boolean forceExecute = false;
			responseReturn = BofaManager.sendDataToBofaAfterSemaPhoreAcquired("sendErrorTesterCheckResetCmd", payLoadInHex, timeDelayInMilliSec,
					                                              expectedDataInHex, isResponseExpected,forceExecute);
			status = (boolean)responseReturn.get("status");
			ApplicationLauncher.logger.debug("sendErrorTesterCheckResetCmd: status1: "+status);
			
			if(status){
				if (address != ConstantPowerSourceBofa.LDU_INT_BROADCAST_ADDRESS) {
					String responseData = (String)responseReturn.get("result");
					ApplicationLauncher.logger.debug("sendErrorTesterCheckResetCmd: responseData: "+responseData);
					status =  processResponse(errorTesterCheckResetFrame, responseData, address);		
					ApplicationLauncher.logger.debug("sendErrorTesterCheckResetCmd: status2: "+status);
				}
			}	
			
		ApplicationLauncher.logger.info("sendErrorTesterCheckResetCmd: Exit");
		return status;
	}
	//===========================================================================================================

	static boolean sendDoubleCircuitSwitchCmd(int address, String circuit){    
		ApplicationLauncher.logger.info("frameDoubleCircuitSwitchCmd: Entry");
		frameDoubleCircuitSwitchCmd(address, circuit);
		String payLoadInHex = doubleCircuitSwitchFrame;
		ApplicationLauncher.logger.info("sendDoubleCircuitSwitchCmd : payLoadInHex: " + payLoadInHex  );
		
		//String addressStr = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;
		
		boolean status = false;
		int timeDelayInMilliSec = 0;
		String expectedDataInHex = ConstantPowerSourceBofa.ER_LDU_STARTS_WITH + ConstantPowerSourceBofa.SLAVE_01_ADRS ;
		boolean isResponseExpected = true;
		
	/*	String responseStatus = BofaManager.getSerialDM_Obj().powerSourceSendCommandProcess(payLoadInHex,timeDelayInMilliSec,expectedDataInHex,isResponseExpected);
			if (responseStatus == DeleteMeConstant.SUCCESS_RESPONSE) {
				String CurrentReadData = BofaManager.getSerialDM_Obj().getRxMsgQ_PwrSrc().getLastReadMessage(); // BofaManager.rxMsgQ_PwrSrc.getLastReadMessage();
				status = processResponse(doubleCircuitSwitchFrame,CurrentReadData,address);  
			}
			*/
			
			Map<String,Object> responseReturn = new HashMap<String,Object>();
			boolean forceExecute = false;
			responseReturn = BofaManager.sendDataToBofaAfterSemaPhoreAcquired("sendDoubleCircuitSwitchCmd", payLoadInHex, timeDelayInMilliSec,
					                                              expectedDataInHex, isResponseExpected,forceExecute);
			status = (boolean)responseReturn.get("status");
			if(status){
				String responseData = (String)responseReturn.get("result");
				/*if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
					responseData = GuiUtils.asciiToHex(responseData);
				}*/
				ApplicationLauncher.logger.info("sendDoubleCircuitSwitchCmd : responseData-22: " + responseData  );
				status =  processResponse(doubleCircuitSwitchFrame, responseData, address);		
			}			
			
		ApplicationLauncher.logger.info("frameDoubleCircuitSwitchCmd: Exit");
		return status;
	}
	//===========================================================================================================
	
	static boolean processResponse(String messageType, String response, int address ){	

		boolean status = false;
		try{
		if (messageType == testTurnsAndPulsesFrame) {
			status = parseAcknowledgementResponses(response ,address);  
		}
		else if (messageType == readErrorsFrame) {       		
			status = parseErrorsResponse(response, address);		
		}
		else if (messageType == prepareSearchMarkFrame) {   
			status = parseAcknowledgementResponses(response,address);  
		}
		else if (messageType == beginSearchMarkFrame) {
			status = parseAcknowledgementResponses(response,address);   
		}
		else if (messageType == queryMarkSearchResultFrame) {  
			status = parseAcknowledgementResponses(response,address);  
		}
		else if (messageType == markSearchEndFrame) {   
			status = parseAcknowledgementResponses(response,address); 
		}
		else if (messageType == queryEnablePulseFrame) { 
			status = parseStartingCurrentTestPulseResponse(response,address);  
		}
		else if (messageType == startTestEndFrame) { 
			status = parseAcknowledgementResponses(response,address);  
		}
		else if (messageType == stateQueryFrame) {   
			status = parseAcknowledgementResponses(response,address);  
		}
		else if (messageType == dialTestStateEntryFrame) {     
			status = parseAcknowledgementResponses(response,address);  
		}
		else if (messageType == readDialPulsesFrame) {  
			status = parseDialPulsesResponse(response,address);	
		}
		else if (messageType == dialTestStateExitFrame) {        
			status = parseAcknowledgementResponses(response,address);  
		}
		else if (messageType == relay485ConnectOrDisconnectFrame) { 
			status = parseAcknowledgementResponses(response,address);   
		}		 
		else if (messageType == pulseSwitchFrame) {       
			status = parseAcknowledgementResponses(response,address);  
		}
		else if (messageType == stopRefreshErrorsFrame) {   
			status = parseAcknowledgementResponses(response,address);  
		}
		else if (messageType == readRefMeterPulseNumFrame) {
			status = parseRefMeterPulseNumResponse(response,address);	
		}
		else if (messageType == pulseInputChannelSelectFrame) {   
			status = parseAcknowledgementResponses(response,address);  
		}
		else if (messageType == relayPositionControlFrame) {     
			status = parseAcknowledgementResponses(response,address); 
		}
		else if (messageType == displayDayErrorFrame) {     
			status = parseAcknowledgementResponses(response,address);  
		}
		else if (messageType == timeSwitchingErrorTestEntryFrame) { 
			status = parseAcknowledgementResponses(response,address);  
		}
		else if (messageType == readSwitchingPulseTimeFrame) { 
			status = parseSwitchingPulseTimeResponse(response,address);
		}
		else if (messageType == errorTesterCheckResetFrame) {  
			status = parseAcknowledgementResponses(response,address);  
		}
		else if (messageType == doubleCircuitSwitchFrame) {   
			status = parseAcknowledgementResponses(response,address);  
		}
		}catch(Exception e){
			e.printStackTrace();
			
			ApplicationLauncher.logger.error("Data_LduBofa: parseAcknowledgementResponses: Exception: " + e.getMessage());
			
		}
		return status;
	}
//====================================================================================================================
	
	
	static boolean parseAcknowledgementResponses(String response, int address){
		
		address = address + ConstantPowerSourceBofa.LDU_INITIAL_ADDRESS_INDEX ;  
		
		ApplicationLauncher.logger.info("Data_LduBofa: parseAcknowledgementResponses: Entry");
		ApplicationLauncher.logger.info("Data_LduBofa: parseAcknowledgementResponses: address : " + address);
		ApplicationLauncher.logger.info("Data_LduBofa: parseAcknowledgementResponses: response: " + response);

		
 		boolean status = false;
	
        String commandCode   = response.substring(0, 2);
        String addressStr    = response.substring(2, 4);
        String ackPart       = response.substring(4, 6);
        String checksum      = response.substring(6, 8);
        String endByte       = response.substring(8);

     	String responseStatus = getResponseStatus(ackPart);

/*        ApplicationLauncher.logger.debug("Command Code    : " + commandCode);  
        ApplicationLauncher.logger.debug("Address         : " + addressStr);  
        ApplicationLauncher.logger.debug("Response Status : " + responseStatus);  
        ApplicationLauncher.logger.debug("Checksum Bytes  : " + checksum);  
        ApplicationLauncher.logger.debug("End Bytes       : " + endByte);  */
           
        
    	if(commandCode.equals(ConstantPowerSourceBofa.START_BYTE) && addressStr.equals(String.valueOf(address)) && ackPart.equals(ConstantPowerSourceBofa.YES_MSG )&& endByte.equals(ConstantPowerSourceBofa.END_BYTE)){
			status = true ;  
		}
		else if(commandCode.equals(ConstantPowerSourceBofa.START_BYTE) && addressStr.equals(String.valueOf(address)) && ackPart.equals(ConstantPowerSourceBofa.NO_MSG )&& endByte.equals(ConstantPowerSourceBofa.END_BYTE)){
		    status = false ;
	       ApplicationLauncher.logger.debug("parseActualOutputValueResponse : NEGATIVE Acknowledgment received ");			 
		}
        
        ApplicationLauncher.logger.info("parseAcknowledgementResponses: Exit");
        return status;
	}
//==========================================================================================================================	
	public static boolean parseStartingCurrentTestPulseResponse(String response, int inpAddress){          // eg : 01 42 35 35 17                                     
		
		//int lduAddress = inpAddress + ConstantPowerSourceBofa.LDU_INITIAL_ADDRESS_INDEX ;  dsvxcv
		int lduAddress = inpAddress + ConstantPowerSourceBofa.LDU_INITIAL_ADDRESS_INDEX  ; 
		/*if(inpAddress>9) {
			lduAddress = inpAddress + ConstantPowerSourceBofa.LDU_INITIAL_ADDRESS_INDEX ; 
		}*/
		
		ApplicationLauncher.logger.info("Data_LduBofa: parseStartingCurrentTestPulseResponse: Entry");
		ApplicationLauncher.logger.info("Data_LduBofa: parseStartingCurrentTestPulseResponse: inpAddress : " + inpAddress);
		ApplicationLauncher.logger.info("Data_LduBofa: parseStartingCurrentTestPulseResponse: lduAddress : " + lduAddress);
		ApplicationLauncher.logger.info("Data_LduBofa: parseStartingCurrentTestPulseResponse: response: " + response);
		
 		boolean status = false;
 		
        String commandCode   = response.substring(0, 2);
        String addressStr    = response.substring(2, 4);
        String pulseCountStr = response.substring(4, 6);
        String checksum      = response.substring(6, 8);
        String endByte       = response.substring(8);

     //	String responseStatus = getResponseStatus(ackPart);

        ApplicationLauncher.logger.info("Command Code    : " + commandCode);  
        ApplicationLauncher.logger.info("Address         : " + addressStr);  
        ApplicationLauncher.logger.info("Pulse Count     : " + pulseCountStr); // 3d 
        ApplicationLauncher.logger.info("Checksum Bytes  : " + checksum);  
        ApplicationLauncher.logger.info("End Bytes       : " + endByte);    
        
      //  int pulseCount    =  Integer.parseInt(GuiUtils.hexToAscii(pulseCountStr)) ;
        
        int pulseCount   =  Integer.parseInt(pulseCountStr, 16) - 48 ; 
        int lduReadAddressStr           = Integer.parseInt(addressStr,16) - ConstantPowerSourceBofa.LDU_INITIAL_ADDRESS_INDEX-24 ;
        ApplicationLauncher.logger.info("parseStartingCurrentTestPulseResponse: Pulse Count     : " +  pulseCount);
        ApplicationLauncher.logger.info("parseStartingCurrentTestPulseResponse: addressStr : " + addressStr);
        ApplicationLauncher.logger.info("parseStartingCurrentTestPulseResponse: lduReadAddressStr : " + lduReadAddressStr);
        //setStaTestPulseCount(pulseCount);
        getStaCreepTestPulseCount().set(inpAddress,pulseCount);
    	//if(commandCode.equals(ConstantPowerSourceBofa.START_BYTE) && addressStr.equals(String.valueOf(lduAddress)) && pulseCountStr.equals(checksum)&& endByte.equals(ConstantPowerSourceBofa.END_BYTE)){
        if(commandCode.equals(ConstantPowerSourceBofa.START_BYTE) && (inpAddress==lduReadAddressStr) && endByte.equals(ConstantPowerSourceBofa.END_BYTE)){
    		
        	status = true ;  
			ApplicationLauncher.logger.debug("parseStartingCurrentTestPulseResponse : status : true");	
		}
		else { //if(commandCode.equals(ConstantPowerSourceBofa.START_BYTE) && addressStr.equals(String.valueOf(address)) && ackPart.equals(ConstantPowerSourceBofa.NO_MSG )&& endByte.equals(ConstantPowerSourceBofa.END_BYTE)){
		    status = false ;
	       ApplicationLauncher.logger.debug("parseStartingCurrentTestPulseResponse : Invalid Input Received ");			 
		}
        
        ApplicationLauncher.logger.info("parseStartingCurrentTestPulseResponse: Exit");
        return status;
	}
	//====================================================================================================================
	
	static boolean parseQueryStateResponse(String response, int address){	
		ApplicationLauncher.logger.info("parseQueryStateResponse: Entry");
		boolean status = false;
		
        String commandCode   = response.substring(0, 2);
        String addressStr    = response.substring(2, 4);
        String ackPart       = response.substring(4, 6);
        String checksum      = response.substring(6, 8);
        String endByte       = response.substring(8);

     	String queryState = getQueryState(ackPart);

        ApplicationLauncher.logger.info("Command Code    : " + commandCode);  
        ApplicationLauncher.logger.info("Address         : " + addressStr);  
        ApplicationLauncher.logger.info("Query State     : " + queryState);  
        ApplicationLauncher.logger.info("Checksum Bytes  : " + checksum);  
        ApplicationLauncher.logger.info("End Bytes       : " + endByte);  
        
    	if(commandCode.equals(ConstantPowerSourceBofa.START_BYTE) && addressStr.equals(String.valueOf(address)) && ackPart.equals(ConstantPowerSourceBofa.IDLE_MSG)&& endByte.equals(ConstantPowerSourceBofa.END_BYTE)){
			status = true ;  
		}
		else if(commandCode.equals(ConstantPowerSourceBofa.START_BYTE) && addressStr.equals(String.valueOf(address)) && ackPart.equals(ConstantPowerSourceBofa.OCCUPIED_MSG)&& endByte.equals(ConstantPowerSourceBofa.END_BYTE)){
		status = false ;
 	        	ApplicationLauncher.logger.debug("parseActualOutputValueResponse :   Acknowledgment received ");
		}
    	
    	ApplicationLauncher.logger.info("parseQueryStateResponse: Exit");
    	return status;
	}
	//====================================================================================================================
	
	static boolean parseQueryMarkSearchResponse(String response, int address){
		ApplicationLauncher.logger.info("parseQueryMarkSearchResponse: Entry");
		boolean status = false;
		
        String commandCode   = response.substring(0, 2);
        String addressStr    = response.substring(2, 4);
        String ackPart       = response.substring(4, 6);
        String checksum      = response.substring(6, 8);
        String endByte       = response.substring(8);

     	String queryMarkSearchResult = getQueryMarkSearchResult(ackPart);

        ApplicationLauncher.logger.info("Command Code           : " + commandCode);  
        ApplicationLauncher.logger.info("Address                : " + addressStr);  
        ApplicationLauncher.logger.info("QueryMarkSearchResult  : " + queryMarkSearchResult);  
        ApplicationLauncher.logger.info("Checksum Bytes         : " + checksum);  
        ApplicationLauncher.logger.info("End Bytes              : " + endByte);
        
    	if(commandCode.equals(ConstantPowerSourceBofa.START_BYTE) && addressStr.equals(String.valueOf(address)) && ackPart.equals(ConstantPowerSourceBofa.FOUND_MSG)&& endByte.equals(ConstantPowerSourceBofa.END_BYTE)){
			status = true ;  
		}
		else if(commandCode.equals(ConstantPowerSourceBofa.START_BYTE) && addressStr.equals(String.valueOf(address)) && ackPart.equals(ConstantPowerSourceBofa.NOT_FOUND_MSG)&& endByte.equals(ConstantPowerSourceBofa.END_BYTE)){
			status = false ;
	        	ApplicationLauncher.logger.debug("parseActualOutputValueResponse : NEGATIVE Acknowledgment received ");
		}
    	
    	ApplicationLauncher.logger.info("parseQueryMarkSearchResponse: Exit");
    	return status;
	}
	//====================================================================================================================
	public static boolean parseErrorsResponse(String response, int address){		
		//ApplicationLauncher.logger.info("parseErrorsResponse: Entry");	
		ApplicationLauncher.logger.info("parseErrorsResponse: response: " + response);
		ApplicationLauncher.logger.info("parseErrorsResponse: address: " + address);
		boolean status = false;
		
        String commandCode   = response.substring(0, 2);    // 2 char
        String addressStr    = response.substring(2, 4);    // 2 char
        String nthOfErrors   = response.substring(4, 6);    // 2 char
        String errorSymbol   = response.substring(6, 8);    // 2 char
        String errorBytesBeforeDecimal    = response.substring(8, 12);   // 4 char
        String errorBytesAfterDecimal    = response.substring(12, 22);   // 10 char
        String checksum      = response.substring(22, 24);  // 2 char
        String endByte       = response.substring(24, 26);    // 2 char

/*        ApplicationLauncher.logger.debug("Command Code   : " + commandCode);  
        ApplicationLauncher.logger.debug("Address        : " + addressStr);  
        ApplicationLauncher.logger.debug("Nth Of Errors  : " + nthOfErrors);  
        ApplicationLauncher.logger.debug("Error Symbol   : " + errorSymbol);  
        ApplicationLauncher.logger.debug("errorBytesBeforeDecimal   : " + errorBytesBeforeDecimal);
        ApplicationLauncher.logger.debug("errorBytesAfterDecimal   : " + errorBytesAfterDecimal);
        ApplicationLauncher.logger.debug("Checksum Bytes : " + checksum);  
        ApplicationLauncher.logger.debug("End Bytes      : " + endByte);*/
        
        String MeterErrorStr      = GuiUtils.hexToAscii(errorSymbol + errorBytesBeforeDecimal + GuiUtils.StringToHex(".")+ errorBytesAfterDecimal) ;
        int nthErrorReading    =  Integer.parseInt(GuiUtils.hexToAscii(nthOfErrors)) ;
        //int lduAddress           = Integer.parseInt(addressStr) - ConstantPowerSourceBofa.LDU_INITIAL_ADDRESS_INDEX ; 
        int lduAddress           = Integer.parseInt(addressStr,16) - ConstantPowerSourceBofa.LDU_INITIAL_ADDRESS_INDEX-24 ;
        
        //float MeterError = (float)(Integer.parseInt(MeterErrorStr)/100000 );
        
        ApplicationLauncher.logger.debug("Meter Error Str    : " + MeterErrorStr);
        ApplicationLauncher.logger.debug("Nth Error Reading  : " + nthErrorReading);
        ApplicationLauncher.logger.debug("lduAddress         : " + lduAddress);
        
        //ApplicationLauncher.logger.info("Meter Error        : " + MeterError);
        //setNthOfErrors(nthErrorReading);
        //setErrorValue(MeterErrorStr);
        
        getNthOfErrors().set(address, nthErrorReading);
        getErrorValue().set(address, MeterErrorStr);
        
        //ApplicationLauncher.logger.debug("lduAddress         : " + lduAddress);
    	//if(commandCode.equals(ConstantPowerSourceBofa.START_BYTE) && addressStr.equals(String.valueOf(address)) && commandCode.equals(ConstantPowerSourceBofa.LDU_CHECK_BYTE) && endByte.equals(ConstantPowerSourceBofa.END_BYTE)){
		if(commandCode.equals(ConstantPowerSourceBofa.START_BYTE) && (lduAddress==address) && endByte.equals(ConstantPowerSourceBofa.END_BYTE)){
		
    		
    		status = true ;  
			ApplicationLauncher.logger.info("parseErrorsResponse: Status: true");
		}
	
       // ApplicationLauncher.logger.info("parseErrorsResponse: Exit");
        return status;
	}
   //====================================================================================================================
	static boolean parseSwitchingPulseTimeResponse(String response, int address){
		ApplicationLauncher.logger.info("parseSwitchingPulseTimeResponse: Entry");
		
		address = address + ConstantPowerSourceBofa.LDU_INITIAL_ADDRESS_INDEX ;  

		boolean status = false;
	    String commandCode           = response.substring(0, 2);    // 2 bytes
        String addressStr               = response.substring(2, 4);    // 2 bytes
        String ackPart               = response.substring(4, 6);    // 2 bytes
        String ASCII_Numbers_3333    = response.substring(4, 8);    // 4 bytes
        String checksum              = response.substring(12, 14);  // 2 bytes
        String endByte               = response.substring(14, 16);  // 2 byte
        
        String SwitchingPulseDetectionStatus = getSwitchingPulseDetectionStatus(ackPart); 
        
        ApplicationLauncher.logger.info("Command Code                   : " + commandCode);  
        ApplicationLauncher.logger.info("Address                        : " + addressStr);  
        ApplicationLauncher.logger.info("SwitchingPulseDetectionStatus  : " + SwitchingPulseDetectionStatus);  
        ApplicationLauncher.logger.info("ASCII_Numbers_3333             : " + ASCII_Numbers_3333);  
        ApplicationLauncher.logger.info("Checksum Bytes                 : " + checksum);  
        ApplicationLauncher.logger.info("End Bytes                      : " + endByte);

        if(commandCode.equals(ConstantPowerSourceBofa.START_BYTE) && addressStr.equals(String.valueOf(address)) && ackPart.equals(ConstantPowerSourceBofa.SWITCHING_PULSE_DETECTED)&& endByte.equals(ConstantPowerSourceBofa.END_BYTE)){
        	status = true ;  
        }
        else if(commandCode.equals(ConstantPowerSourceBofa.START_BYTE) && addressStr.equals(String.valueOf(address)) && ackPart.equals(ConstantPowerSourceBofa.NO_SWITCHING_PULSE)&& endByte.equals(ConstantPowerSourceBofa.END_BYTE)){
        	status = false ;
        	ApplicationLauncher.logger.debug("parseActualOutputValueResponse : NEGATIVE Acknowledgment received ");
        }

    	ApplicationLauncher.logger.info("parseSwitchingPulseTimeResponse: Exit");
    	return status;
	}
	//====================================================================================================================
	static boolean parseRefMeterPulseNumResponse(String response, int address){  // 01 42 3030343338343332 98 17
		ApplicationLauncher.logger.info("parseRefMeterPulseNumResponse: Entry");   
		boolean status = false;
		String commandCode        = response.substring(0, 2);    // 2 bytes
        String addressStr         = response.substring(2, 4);    // 2 bytes
        String pulseNumOfRefMeter = response.substring(4, 20);   // 8 bytes
        String checksum           = response.substring(20, 22);  // 2 bytes
        String endByte            = response.substring(22);      // 2 byte
	 
        ApplicationLauncher.logger.info("Command Code        : " + commandCode);  
        ApplicationLauncher.logger.info("Address             : " + addressStr);  
        ApplicationLauncher.logger.info("pulseNumOfRefMeter  : " + pulseNumOfRefMeter);  
        ApplicationLauncher.logger.info("Checksum Bytes      : " + checksum);  
        ApplicationLauncher.logger.info("End Bytes           : " + endByte);
        
        long pulseNumber = Long.parseLong(GuiUtils.hexToAscii(pulseNumOfRefMeter));
        ApplicationLauncher.logger.info("Ref std : Pulse Count  : " + pulseNumber);  
        
        ApplicationLauncher.logger.info("getLiveRefMeterConstant : " + Data_RefStdBofa.getLiveRefMeterConstant());  
        if(Data_RefStdBofa.getLiveRefMeterConstant()>0){
        	float refStdEnergyinKwh = ((float)pulseNumber )/ ((float)Data_RefStdBofa.getLiveRefMeterConstant());
        	
        	String formattedEnergy = String.format("%02.06f",refStdEnergyinKwh);
        	Data_RefStdBofa.setRefStdActiveEnergyAccumulatedInKwh(Float.parseFloat(formattedEnergy));
        	ApplicationLauncher.logger.info("refStdEnergyinKwh : " + Data_RefStdBofa.getRefStdActiveEnergyAccumulatedInKwh()); 
        	ApplicationLauncher.logger.info("formattedEnergy : " + formattedEnergy); 
        	ProjectExecutionController.setFeedbackR_ActiveEnergy(formattedEnergy);
        }
        
        
        if(commandCode.equals(ConstantPowerSourceBofa.START_BYTE) && addressStr.equals(String.valueOf(address)) && endByte.equals(ConstantPowerSourceBofa.END_BYTE)){
			status = true ;  
		}
		/*else{
			status = false ;
			if (ackPart.equals(ConstantPowerSourceBofa.NEGATIVE)) {
	        	ApplicationLauncher.logger.debug("parseActualOutputValueResponse : NEGATIVE Acknowledgment received ");
			}
		}
        */
    	ApplicationLauncher.logger.info("parseRefMeterPulseNumResponse: Exit");
    	return status;
	}
	//====================================================================================================================
	static boolean parseDialPulsesResponse(String response, int inpAddress){   
		ApplicationLauncher.logger.info("parseDialPulsesResponse: Entry");
		boolean status = false;
		    String commandCode    = response.substring(0, 2);    // 2 bytes
	        String addressStr     = response.substring(2, 4);    // 2 bytes
	        String numOfDialPulse = response.substring(4, 20);   // 8 bytes   
	        String checksum       = response.substring(20, 22);  // 2 bytes
	        String endByte        = response.substring(22);  // 2 byte
		
	        ApplicationLauncher.logger.info("Command Code       : " + commandCode);  
	        ApplicationLauncher.logger.info("Address            : " + addressStr);  
	        ApplicationLauncher.logger.info("num Of Dial Pulse  : " + numOfDialPulse);    
	        ApplicationLauncher.logger.info("Checksum Bytes     : " + checksum);  
	        ApplicationLauncher.logger.info("End Bytes          : " + endByte);
	        
			//String addressStr2 = BofaManager.asciiToHex(String.valueOf((char)(address + ConstantPowerSourceBofa.LDU_ADDRESS_ADDITION))) ;
			int lduAddress = inpAddress + ConstantPowerSourceBofa.LDU_INITIAL_ADDRESS_INDEX ;  

	        long dialPulse = Long.parseLong(GuiUtils.hexToAscii(numOfDialPulse));
	        ApplicationLauncher.logger.info("Energy Meter : Pulse Count  : " + dialPulse);
	        
	        //setDialPulseCount(String.valueOf(dialPulse));
	        getDialTestPulseCount().set(inpAddress, String.valueOf(dialPulse));
	        
	        
	        
	        int lduReadAddressStr           = Integer.parseInt(addressStr,16) - ConstantPowerSourceBofa.LDU_INITIAL_ADDRESS_INDEX-24 ;
	        
	        //if(commandCode.equals(ConstantPowerSourceBofa.START_BYTE) && addressStr.equals(String.valueOf(lduAddress)) && endByte.equals(ConstantPowerSourceBofa.END_BYTE)){
	        if(commandCode.equals(ConstantPowerSourceBofa.START_BYTE) && (inpAddress==lduReadAddressStr) && endByte.equals(ConstantPowerSourceBofa.END_BYTE)){
	        	
	        	status = true ;  
	        	ApplicationLauncher.logger.info("parseDialPulsesResponse: status :  true");
			}
			/*else{
				status = false ;
				if (ackPart.equals(ConstantPowerSourceBofa.NEGATIVE)) {
		        	ApplicationLauncher.logger.debug("parseActualOutputValueResponse : NEGATIVE Acknowledgment received ");
				}
			}*/
	        
	        ApplicationLauncher.logger.info("parseDialPulsesResponse: commandCode : " + commandCode + " <" + ConstantPowerSourceBofa.START_BYTE+">");
	        ApplicationLauncher.logger.info("parseDialPulsesResponse: addressStr  : " + addressStr + " <" + String.valueOf(lduAddress)+">");
	        ApplicationLauncher.logger.info("parseDialPulsesResponse: endByte     : " + endByte + " <" + ConstantPowerSourceBofa.END_BYTE+">");
	    	ApplicationLauncher.logger.info("parseDialPulsesResponse: Staus       : " + status);

	    	ApplicationLauncher.logger.info("parseDialPulsesResponse: Exit");
	    	return status;
	}
//====================================================================================================================

		static String getResponseStatus(String responseStatus) {
			switch (responseStatus) {
			case "06":
				return "yes";
			case "15":
				return "no";           
			default:
				return "unknown";     
			}
		}
//====================================================================================================================

		static String getQueryState(String responseStatus) {
			switch (responseStatus) {
			case "30":
				return "occupied";
			case "31":
				return "idle";          
			default:
				return "unknown";     
			}
		}
//====================================================================================================================

		static String getQueryMarkSearchResult(String responseStatus) {
			switch (responseStatus) {
			case "30":
				return "notFound";
			case "31":
				return "found";          
			default:
				return "unknown";     
			}
		}
//====================================================================================================================

		static String getSwitchingPulseDetectionStatus(String responseStatus) {
			switch (responseStatus) {
			case "30":
				return "noSwitchingPulse";
			case "31":
				return "switchingPulseDetected";          
			default:
				return "unknown";     
			}
		}
//==========================================================================================================

// Method to calculate the check bit
/*private static byte calculateCheckByte(String message) {
	ApplicationLauncher.logger.info("calculateCheckByte:Entry");
    // For simplicity, let's just XOR all bytes in the message
    byte checkByte = 0;
    for (int i = 0; i < message.length(); i += 2) {
    	checkByte ^= (byte) Integer.parseInt(message.substring(i, i + 2), 16);
    }
    ApplicationLauncher.logger.info("calculateCheckByte:Exit");
    return checkByte;
}*/
//===============================================================================================================
		
		 
		public static boolean bofaLDU_ReadErrorData(int LDU_ReadAddress){//(int LDU_ReadAddress){
			//ApplicationLauncher.logger.debug("bofaLDU_ReadErrorData :Entry");
			ApplicationHomeController.update_left_status("Reading LDU ErrorData",ConstantApp.LEFT_STATUS_DEBUG);
			boolean status=false;
			ApplicationLauncher.logger.info(ProjectExecutionController.getCurrentTestPointName()+":"+ProjectExecutionController.getCurrentTestPoint_Index()+" :bofaLDU_ReadErrorData:LDU_ReadAddress:"+LDU_ReadAddress);
			try{
				//LDU_SendCommandReadErrorData(LDU_ReadAddress);
				//bofaLDU_SendCommandReadErrorData(LDU_ReadAddress);
				sendReadErrorsCmd(LDU_ReadAddress);
				ApplicationLauncher.logger.debug("bofaLDU_ReadErrorData : LDU_ReadAddress : " + LDU_ReadAddress);
				ApplicationLauncher.logger.debug("bofaLDU_ReadErrorData : getNthOfErrors : " + getNthOfErrors().get(LDU_ReadAddress));
				ApplicationLauncher.logger.debug("bofaLDU_ReadErrorData : getErrorValue : " + getErrorValue().get(LDU_ReadAddress));
				
				//String testruntype = DeviceDataManagerController.getTestRunType();
				//ApplicationLauncher.logger.debug("bofaLDU_ReadErrorData: testruntype: " + testruntype);
				//ApplicationLauncher.logger.debug("bofaLDU_ReadErrorData: getSkipCurrentTP_Execution: " + getSkipCurrentTP_Execution());

				
				
				
				String expectedResponse = "";
				//SerialDataLDU lduData = LDU_ReadData(ConstantLduLscs.CMD_LDU_ERROR_DATA_LENGTH,expectedResponse);
				String resultStatus= ConstantReport.RESULT_STATUS_UNDEFINED.trim();//ConstantLduLscs.TO_BE_UPDATED;//
				if(getErrorValue().get(LDU_ReadAddress).contains(ConstantPowerSourceBofa.BOFA_LDU_WFR)){
					//if(getErrorValue().get(LDU_ReadAddress).contains(ConstantPowerSourceBofa.BOFA_LDU_WFR)){
					ProjectExecutionController.DeviceErrorDisplayUpdate(LDU_ReadAddress,resultStatus,"WFR");
					//return status;
				}else{
					
					resultStatus = ConstantReport.RESULT_STATUS_UNDEFINED.trim();
					resultStatus = lscsLduManipulateErrorResultStatus(getErrorValue().get(LDU_ReadAddress));
					ProjectExecutionController.DeviceErrorDisplayUpdate(LDU_ReadAddress,resultStatus,getErrorValue().get(LDU_ReadAddress));
				
				}
				//if(lduData.IsExpectedResponseReceived()){
					//ApplicationLauncher.logger.info("bofaLDU_ReadErrorData: Setting ldustatus to Pass");
					//lduData.setLDU_ResultStatus(resultStatus,LDU_ReadAddress);

					
					//lduData.lscsLDU_DecodeSerialData(LDU_ReadAddress);
					//ApplicationLauncher.logger.debug("bofaLDU_ReadErrorData: getLDU_ErrorValue: " + getErrorValue());//LDU_ReadAddress));
					resultStatus = lscsLduManipulateErrorResultStatus(getErrorValue().get(LDU_ReadAddress));//LDU_ReadAddress));
					//lduData.setLDU_ResultStatus(resultStatus,LDU_ReadAddress);
					if(!getErrorValue().get(LDU_ReadAddress).contains(ConstantPowerSourceBofa.BOFA_LDU_WFR)){
						ProjectExecutionController.DeviceErrorDisplayUpdate(LDU_ReadAddress,resultStatus,getErrorValue().get(LDU_ReadAddress));//LDU_ReadAddress));
					}
					//StripLDU_SerialData(lduData.getReceivedLength());

					String testruntype = DeviceDataManagerController.getTestRunType();
					ApplicationLauncher.logger.debug("bofaLDU_ReadErrorData: testruntype: " + testruntype);
					ApplicationLauncher.logger.debug("bofaLDU_ReadErrorData: getSkipCurrentTP_Execution: " + serialDM_Obj.getSkipCurrentTP_Execution());

					
					String test_type = ProjectExecutionController.getCurrentTestType();
					ApplicationLauncher.logger.debug("bofaLDU_ReadErrorData: test_type: " + test_type);
					ApplicationLauncher.logger.debug("bofaLDU_ReadErrorData: getStepRunFlag(): " + ProjectExecutionController.getStepRunFlag());
					if(!test_type.equals(TestProfileType.Warmup.toString()) &&
							!ProjectExecutionController.getStepRunFlag()){
						if( (testruntype.equals(ConstantApp.TESTPOINT_RUNTYPE_PULSEBASED) || testruntype.equals(ConstantApp.TESTPOINT_RUNTYPE_TIMEBASED) )
								&& !serialDM_Obj.getSkipCurrentTP_Execution()){
							//ApplicationLauncher.logger.debug("bofaLDU_ReadErrorData: test1: ");
							int averageLduRequired = DeviceDataManagerController.getAverageNoOfLduReadingRequired();
							String ErrorValue = getErrorValue().get(LDU_ReadAddress);//LDU_ReadAddress);
							ApplicationLauncher.logger.debug("bofaLDU_ReadErrorData: ErrorValue: "+ ErrorValue);
							if(ErrorValue.contains(ConstantPowerSourceBofa.BOFA_LDU_WFR) || ErrorValue.equals(ConstantLduCcube.CMD_LDU_CREEP_DATA_ER_WFR)){
								ErrorValue = "WFR";
							}
							//ApplicationLauncher.logger.debug("bofaLDU_ReadErrorData: test2: ");
							int skip_reading = getSkipReadingFromJSON(LDU_ReadAddress, DisplayDataObj.get_NoOfPulseReadingToBeSkipped());
							ApplicationLauncher.logger.debug("bofaLDU_ReadErrorData: skip_reading: " + skip_reading);
							//averageLduFeature
							//ApplicationLauncher.logger.debug("bofaLDU_ReadErrorData: getReceivedReadingIndex1: " + lduData.getReceivedReadingIndex(LDU_ReadAddress));

							if(!(ErrorValue.equals("WFR"))){
								//ApplicationLauncher.logger.debug("bofaLDU_ReadErrorData: test3: ");
								//if(ErrorValue.length() > 1){ // sometimes LDU sends data 000000
									ApplicationLauncher.logger.debug("bofaLDU_ReadErrorData: test3A: ");
									ApplicationLauncher.logger.debug("bofaLDU_ReadErrorData: skip_reading: " + skip_reading);
									ApplicationLauncher.logger.debug("bofaLDU_ReadErrorData: getNthOfErrors: " + getNthOfErrors().get(LDU_ReadAddress));
									if(skip_reading < getNthOfErrors().get(LDU_ReadAddress)){//lduData.getReceivedReadingIndex(LDU_ReadAddress)){
										ApplicationLauncher.logger.debug("bofaLDU_ReadErrorData: test4: LDU_ReadAddress: " +LDU_ReadAddress);
										//resultStatus = lscsLduManipulateErrorResultStatus(lduData.getLDU_ErrorValue(LDU_ReadAddress));
										//lduData.setLDU_ResultStatus(resultStatus,LDU_ReadAddress);
										//averageLduFeature
										//ApplicationLauncher.logger.debug("bofaLDU_ReadErrorData: getLastReceivedReadingIndex: " + lduData.getLastReceivedReadingIndex(LDU_ReadAddress));
										//averageLduFeature
										//ApplicationLauncher.logger.debug("bofaLDU_ReadErrorData: getReceivedReadingIndex2: " + lduData.getReceivedReadingIndex(LDU_ReadAddress));
										//averageLduFeature
										//if( (lduData.getLastReceivedReadingIndex(LDU_ReadAddress) < lduData.getReceivedReadingIndex(LDU_ReadAddress)) || (lduData.getReceivedReadingIndex(LDU_ReadAddress)== 0 )){
										//if( (lduData.getLastReceivedReadingIndex(LDU_ReadAddress) < lduData.getReceivedReadingIndex(LDU_ReadAddress)) ){

											//ApplicationLauncher.logger.debug("bofaLDU_ReadErrorData: test4A:");
											ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, resultStatus, getErrorValue().get(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
											//averageLduFeature
											//lduData.setLastReceivedReadingIndex(lduData.getReceivedReadingIndex(LDU_ReadAddress),LDU_ReadAddress);
											//averageLduFeature
											//lduData.incrementNoOfReceivedReading(LDU_ReadAddress);
											//averageLduFeature
											//DisplayDataObj.addLduErrorDataHashMap2d(LDU_ReadAddress,lduData.getReceivedReadingIndex(LDU_ReadAddress),lduData.getLDU_ErrorValue(LDU_ReadAddress));
											//averageLduFeature
											//ApplicationLauncher.logger.debug("bofaLDU_ReadErrorData: getNoOfReceivedReading: " + lduData.getNoOfReceivedReading(LDU_ReadAddress));
											//averageLduFeature
											//ApplicationLauncher.logger.debug("bofaLDU_ReadErrorData: averageLduRequired: " + averageLduRequired);
											//averageLduFeature								
											if(getNthOfErrors().get(LDU_ReadAddress) >= averageLduRequired  ){//lduData.getNoOfReceivedReading(LDU_ReadAddress)){
												//averageLduFeature
												//ApplicationLauncher.logger.debug("bofaLDU_ReadErrorData: test4B:");
												ArrayList<Integer> devices_to_be_read = DisplayDataObj.getDevicesToBeRead();
												devices_to_be_read = RemoveDeviceFromReadList(devices_to_be_read,LDU_ReadAddress );
												DisplayDataObj.setDevicesToBeRead(devices_to_be_read);
												ApplicationLauncher.logger.info("bofaLDU_ReadErrorData: devices_to_be_read:-p " + devices_to_be_read);
												ApplicationHomeController.updateBottomSecondaryStatus("LDU error read status: "+ (ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK-DisplayDataObj.getDevicesToBeRead().size())+ "/" + ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK,ConstantApp.LEFT_STATUS_INFO);

												//averageLduFeature
												try{
													
													
													//averageLduFeature
													//String averageValueStr = DisplayDataObj.getAverageLduErrorDataHashMap2d(LDU_ReadAddress);
													//averageLduFeature
													//if(averageValueStr!=null){
														//averageLduFeature
														//ApplicationLauncher.logger.debug("bofaLDU_ReadErrorData: averageValueStr: " + averageValueStr);
														//averageLduFeature												
														//lduData.setAverageReading(averageValueStr,LDU_ReadAddress);

														//averageLduFeature
														//resultStatus = lscsLduManipulateErrorResultStatus(lduData.getAverageReading(LDU_ReadAddress));
														//averageLduFeature
														//lduData.setLDU_ResultStatus(resultStatus,LDU_ReadAddress);//averageLduFeature
														//averageLduFeature
														ProjectExecutionController.DeviceErrorDisplayUpdate(LDU_ReadAddress, resultStatus, getErrorValue().get(LDU_ReadAddress));//lduData.getAverageReading(LDU_ReadAddress));

														//averageLduFeature
														ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, resultStatus, getErrorValue().get(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
													//}
													//averageLduFeature
												}catch(Exception E){
													//averageLduFeature
													E.printStackTrace();
													//averageLduFeature
													ApplicationLauncher.logger.error("bofaLDU_ReadErrorData Exception0 :"+E.getMessage());
												}


												if(DisplayDataObj.getDevicesToBeRead().size() == 0){
													ApplicationLauncher.logger.info("bofaLDU_ReadErrorData: All Devices Completed-p " );
													ProjectExecutionController.deviceExecutionStatusDisplayUpdate(ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID, ConstantReport.RESULT_STATUS_PASS.trim(),ConstantApp.EXECUTION_STATUS_COMPLETED);//EXECUTION_STATUS_COMPLETED
													
													//RunningStatusController.setExecuteTimeCounter(5);
													
													ProjectExecutionController.setExecuteTimeCounter(0);
													lduTimer.cancel();
												}
											}else{
												ApplicationLauncher.logger.info("bofaLDU_ReadErrorData: Extending1 :-p  " );
												if(ProjectExecutionController.getExecuteTimeCounter()<30){
													int extend_executetime = (ProjectExecutionController.getExecuteTimeCounter())*2;
													ProjectExecutionController.setExecuteTimeCounter(extend_executetime);
												}

											}
										}else{
											ApplicationLauncher.logger.info("bofaLDU_ReadErrorData: Extending2 :-p  " );
											if(ProjectExecutionController.getExecuteTimeCounter()<30){
												int extend_executetime = (ProjectExecutionController.getExecuteTimeCounter())*2;
												ProjectExecutionController.setExecuteTimeCounter(extend_executetime);
											}

										}
									} else{
										ApplicationLauncher.logger.info("bofaLDU_ReadErrorData: ErrorValue:-p  "  + ErrorValue);
										if(ProjectExecutionController.getExecuteTimeCounter()<30){
											int extend_executetime = (ProjectExecutionController.getExecuteTimeCounter())*2;
											ProjectExecutionController.setExecuteTimeCounter(extend_executetime);
										}
										//ApplicationLauncher.logger.info("bofaLDU_ReadErrorData: NoOfPulseReadingToBeSkipped -p : "  + skip_reading);
										//DisplayDataObj.decrement_NoOfPulseReadingToBeSkipped(LDU_ReadAddress);
									}
/*								} else{
									ApplicationLauncher.logger.info("bofaLDU_ReadErrorData: ErrorValue:-p2  "  + ErrorValue);
									if(ProjectExecutionController.getExecuteTimeCounter()<30){
										int extend_executetime = (ProjectExecutionController.getExecuteTimeCounter())*2;
										ProjectExecutionController.setExecuteTimeCounter(extend_executetime);
									}
									ApplicationLauncher.logger.info("bofaLDU_ReadErrorData: NoOfPulseReadingToBeSkipped -p2 : "  + skip_reading);
									//DisplayDataObj.decrement_NoOfPulseReadingToBeSkipped(LDU_ReadAddress);
								}*/
							}
							else{
								if(ProjectExecutionController.getExecuteTimeCounter()<30){
									int extend_executetime = (ProjectExecutionController.getExecuteTimeCounter())*2;
									ProjectExecutionController.setExecuteTimeCounter(extend_executetime);
								}
							}
						}
						else if(testruntype.equals(ConstantApp.TESTPOINT_RUNTYPE_TIMEBASED)){
							//ApplicationLauncher.logger.debug("bofaLDU_ReadErrorData: test2: ");
						/*	String ErrorValue = lduData.getLDU_ErrorValue(LDU_ReadAddress);
							
							if(ErrorValue.contains(ConstantPowerSourceBofa.BOFA_LDU_WFR) || ErrorValue.equals(ConstantLduCcube.CMD_LDU_CREEP_DATA_ER_WFR)){
								ErrorValue = "WFR";
							}
							//ApplicationLauncher.logger.debug("bofaLDU_ReadErrorData: test3: " + ErrorValue);
							if(!(ErrorValue.equals("WFR"))){
								//ApplicationLauncher.logger.debug("bofaLDU_ReadErrorData: test4: ");
								//resultStatus = lscsLduManipulateErrorResultStatus(lduData.getLDU_ErrorValue(LDU_ReadAddress));
								//lduData.setLDU_ResultStatus(resultStatus,LDU_ReadAddress);
								ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getLDU_ErrorValue(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);

								ArrayList<Integer> devices_to_be_read = DisplayDataObj.getDevicesToBeRead();
								devices_to_be_read = RemoveDeviceFromReadList(devices_to_be_read,LDU_ReadAddress );
								DisplayDataObj.setDevicesToBeRead(devices_to_be_read);
								ApplicationLauncher.logger.info("bofaLDU_ReadErrorData: devices_to_be_read-t:" + devices_to_be_read);
								if(DisplayDataObj.getDevicesToBeRead().size() == 0){
									ApplicationLauncher.logger.info("bofaLDU_ReadErrorData: -t All Devices Completed" );
									//RunningStatusController.setExecuteTimeCounter(5);
									ProjectExecutionController.setExecuteTimeCounter(0);
									lduTimer.cancel();
								}
								else{
									if(!isTimeExtendedForTimeBased()){
										ApplicationLauncher.logger.info("bofaLDU_ReadErrorData: ErrorValue: -t  "  + ErrorValue);
										if(ProjectExecutionController.getExecuteTimeCounter()<30){
											int extend_executetime = (ProjectExecutionController.getExecuteTimeCounter()) +
													TimeToBeExtendedInSec;
											ProjectExecutionController.setExecuteTimeCounter(extend_executetime);
											ApplicationLauncher.logger.info("bofaLDU_ReadErrorData: Time Extended:-t  " + extend_executetime);
											setTimeExtendedForTimeBased(true);
											//DisplayDataObj.decrement_NoOfPulseReadingToBeSkipped(LDU_ReadAddress);
										}
									}
								}
							}
							else{
								ApplicationLauncher.logger.info("bofaLDU_ReadErrorData: isTimeExtendedForTimeBased:-t  " + isTimeExtendedForTimeBased());
								ApplicationLauncher.logger.info("bofaLDU_ReadErrorData: getExecuteTimeCounter:-t  " + ProjectExecutionController.getExecuteTimeCounter());
								if(!isTimeExtendedForTimeBased()){
									if(ProjectExecutionController.getExecuteTimeCounter()<30){
										int extend_executetime = (ProjectExecutionController.getExecuteTimeCounter()) + 
												TimeToBeExtendedInSec;
										ProjectExecutionController.setExecuteTimeCounter(extend_executetime);
										setTimeExtendedForTimeBased(true);
									}
								}
							}
						}*/
					}else if(ProjectExecutionController.getStepRunFlag()){
						if(!test_type.equals(TestProfileType.Warmup.toString())){
							if(testruntype.equals(ConstantApp.TESTPOINT_RUNTYPE_PULSEBASED) 
									&& !serialDM_Obj.getSkipCurrentTP_Execution()){
								//ApplicationLauncher.logger.info("bofaLDU_ReadErrorData: Steprun hit1" );
								String ErrorValue = getErrorValue().get(LDU_ReadAddress);//LDU_ReadAddress);
								if(ErrorValue.contains(ConstantPowerSourceBofa.BOFA_LDU_WFR) || ErrorValue.equals(ConstantLduCcube.CMD_LDU_CREEP_DATA_ER_WFR)){
									ErrorValue = "WFR";
								}
								if(!(ErrorValue.equals("WFR"))){
									//resultStatus = lscsLduManipulateErrorResultStatus(lduData.getLDU_ErrorValue(LDU_ReadAddress));
									//lduData.setLDU_ResultStatus(resultStatus,LDU_ReadAddress);
									if(getNthOfErrors().get(LDU_ReadAddress)>=1){
										DisplayDataObj.getStepRunModeAtleastOneResultReadCompleted().set(LDU_ReadAddress, true);
									}
									ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, resultStatus, getErrorValue().get(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
								}
							} else if(testruntype.equals(ConstantApp.TESTPOINT_RUNTYPE_TIMEBASED)){

								String ErrorValue = getErrorValue().get(LDU_ReadAddress);//LDU_ReadAddress);
								if(ErrorValue.contains(ConstantPowerSourceBofa.BOFA_LDU_WFR) || ErrorValue.equals(ConstantLduCcube.CMD_LDU_CREEP_DATA_ER_WFR)){
									ErrorValue = "WFR";
								}

								if(!(ErrorValue.equals("WFR"))){
									//resultStatus = lscsLduManipulateErrorResultStatus(lduData.getLDU_ErrorValue(LDU_ReadAddress));
									//lduData.setLDU_ResultStatus(resultStatus,LDU_ReadAddress);
									if(getNthOfErrors().get(LDU_ReadAddress)>=1){
										DisplayDataObj.getStepRunModeAtleastOneResultReadCompleted().set(LDU_ReadAddress, true);
									}
									ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, resultStatus, getErrorValue().get(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
								}
							}
						}
					}

					status=true;
/*				}
				else{
					ApplicationLauncher.logger.info("bofaLDU_ReadErrorData:No Data Received");
				}*/
			}catch(Exception E){
				E.printStackTrace();
				ApplicationLauncher.logger.error("bofaLDU_ReadErrorData Exception :"+E.getMessage());
			}
			return status;
		}
//============================================================================================================		
	
		public void bofaLduTriggerReadSTAPulseCount(){
			ApplicationLauncher.logger.debug("bofaLduTriggerReadSTAPulseCount : Entry ");
			DisplayDataObj.setLDU_ReadDataFlag(true);
			lduTimer = new Timer() ;
			lduTimer.schedule(new BofaLduComReadStaTask(), SerialLDU_ComRefreshDefaultTimeInMsec);//updated by gopi - for the refresh to read the device before exiting the LDUreadbuffertime
			ApplicationLauncher.logger.debug("bofaLduTriggerReadSTAPulseCount : Exit ");
		}
		
		public void bofaLduTriggerReadCRPPulseCount(){
			ApplicationLauncher.logger.debug("bofaLduTriggerReadCRPPulseCount : Entry ");
			DisplayDataObj.setLDU_ReadDataFlag(true);
			lduTimer = new Timer() ;
			lduTimer.schedule(new BofaLduComReadCreepTask(), SerialLDU_ComRefreshDefaultTimeInMsec);//updated by gopi - for the refresh to read the device before exiting the LDUreadbuffertime
			ApplicationLauncher.logger.debug("bofaLduTriggerReadCRPPulseCount : Exit ");
		}
		
		public void bofaLduTriggerReadConstantTestPulseCount(){
			ApplicationLauncher.logger.debug("bofaLduTriggerReadConstantTestPulseCount : Entry ");
			DisplayDataObj.setLDU_ReadDataFlag(true);
			lduTimer = new Timer() ;
			lduTimer.schedule(new BofaLduComReadConstantTestTask(), SerialLDU_ComRefreshDefaultTimeInMsec);//updated by gopi - for the refresh to read the device before exiting the LDUreadbuffertime
			ApplicationLauncher.logger.debug("bofaLduTriggerReadConstantTestPulseCount : Exit ");
		}
		
		
		
		public static void bofaLduSendStartSTA(){
			ApplicationLauncher.logger.debug("bofaLduSendStartSTA :Entry");
			if(!ProjectExecutionController.getUserAbortedFlag()) {
				sendStartSTA_CreepCmd(ConstantPowerSourceBofa.LDU_INT_BROADCAST_ADDRESS); // send command here
			}
		} 
		
		public static void bofaLduSendStartCRP(){
			ApplicationLauncher.logger.debug("bofaLduSendStartCRP :Entry");
			if(!ProjectExecutionController.getUserAbortedFlag()) {
				sendStartSTA_CreepCmd(ConstantPowerSourceBofa.LDU_INT_BROADCAST_ADDRESS); // send command here
			}

		} 
		//===============================================================================================================
		public static void bofaLDU_PreRequisiteForReadError(){
			ApplicationLauncher.logger.info("bofaLDU_PreRequisiteForReadError :Entry"  );
			// version s3.9.5 -  added below flag - because Test point was running and not moving forward
			
	//====================================		
			try {
				//===================================	
				//int retries = 75;
				boolean success = false;
/*				ApplicationLauncher.logger.debug("bofaLDU_PreRequisiteForReadError: Semaphore tryAcquire...");
				while ((!success) && (retries > 0)) {
					try {
						
						if (BofaManager.comPortSemaphore.tryAcquire()) { // Try to Acquire the semaphore
							ApplicationLauncher.logger.debug("bofaLDU_PreRequisiteForReadError : Semaphore acquired");
*/
							//DisplayDataObj.setLDU_ReadDataFlag( true);
							
							success = bofaSendLDU_SettingCommand();
							DisplayDataObj.setLDU_ReadDataFlag( true);
							
/*							BofaManager.comPortSemaphore.release(); // Release the semaphore 
							ApplicationLauncher.logger.debug("bofaLDU_PreRequisiteForReadError : Semaphore released");
							Sleep(205);
						}
						else { 
							ApplicationLauncher.logger.debug("bofaLDU_PreRequisiteForReadError : Semaphore not Aquired");
						}
						} catch (Exception e) {
							e.printStackTrace();
							ApplicationLauncher.logger.error("bofaLDU_PreRequisiteForReadError :Exception :" + e.getMessage());
						} 

					if (!success) {
						retries--;
						Sleep(200);
					}			
				}*/

/*				if (!success) {
					ApplicationLauncher.logger.debug("bofaLDU_PreRequisiteForReadError : Max retries reached");
				}


				Sleep(200); */
				
			} catch (Exception e1){
				e1.printStackTrace();
				ApplicationLauncher.logger.error("bofaLDU_PreRequisiteForReadError :Exception1:"+ e1.getMessage());

			}		
	 //====================================				
		
			
			//Sleep(1000);
			//SerialDM_Obj.LDU_ResetError();
			//Sleep(1000);//1000
			//SerialDM_Obj.LDU_ResetSetting();
			//Sleep(1000);//1000
		}
		
		
		public static boolean bofaSendLDU_SettingCommand(){
			ApplicationLauncher.logger.debug("bofaSendLDU_SettingCommand :Entry");
			boolean status =  false;
			/*		if (DevicePortSetupController.getPortValidationTurnedON()){
				ApplicationLauncher.logger.info("bofaSendLDU_SettingCommand: getPortValidationTurnedON");
				String meter_const = "2500";
				String MeterAddress = "00";
				LDU_SendCommandErrorSetting(MeterAddress,meter_const);
				SerialDataLDU lduData = LDU_ReadData(ConstantLduCcube.CMD_LDU_ERROR_RESET_ER.length(),ConstantLduCcube.CMD_LDU_ERROR_RESET_ER);
				if(lduData.IsExpectedResponseReceived()){

					String CurrentLDU_Data =lduData.getLDU_ReadSerialData();
					ApplicationLauncher.logger.info("LDU_ResetSetting:LDU Received DataA:"+CurrentLDU_Data);
					StripLDU_SerialData(lduData.getReceivedLength());
					status=true;
				}
			}else{*/
			String project_name = ProjectExecutionController.getCurrentProjectName();
			ApplicationLauncher.logger.info("bofaSendLDU_SettingCommand :project_name: " +project_name);
			if(project_name!=null){
				JSONObject result = DisplayDataObj.getDeployedDevicesJson();//MySQL_Controller.sp_getdeploy_devices(project_name);
				try {
					JSONArray deployed_devices = result.getJSONArray("Devices");
					ApplicationLauncher.logger.info("bofaSendLDU_SettingCommand :deployed_devices: " +deployed_devices.toString());
					//DeploymentManagerController.setAllMeterConstSame(true);// to be updated 
					if(DisplayDataObj.IsAllMeterConstSame()){
						JSONObject jobj = deployed_devices.getJSONObject(0);
						int m_const = jobj.getInt("meter_const");
						String meter_const = Integer.toString(m_const);
						//String MeterAddress = "00";
						int MeterAddress = ConstantPowerSourceBofa.LDU_INT_BROADCAST_ADDRESS;
						ApplicationLauncher.logger.info("bofaSendLDU_SettingCommand: meter_const: "+ meter_const);
						//LDU_SendCommandErrorSetting(MeterAddress,meter_const);
						bofaLduSendCommandErrorSetting(MeterAddress,meter_const);
						status=true;
						/*						SerialDataLDU lduData = LDU_ReadData(ConstantLduCcube.CMD_LDU_ERROR_RESET_ER.length(),ConstantLduCcube.CMD_LDU_ERROR_RESET_ER);						

							if(lduData.IsExpectedResponseReceived()){

								String CurrentLDU_Data =lduData.getLDU_ReadSerialData();
								ApplicationLauncher.logger.info("LDU_ResetSetting:LDU Received DataB:"+CurrentLDU_Data);
								StripLDU_SerialData(lduData.getReceivedLength());
								status=true;
							}*/

					}else{
						ApplicationLauncher.logger.info("bofaSendLDU_SettingCommand :deployed_devices.length(): " +deployed_devices.length());
						SerialDataLDU lduData3 = null;
						int AllDeviceStatus = 0;
						JSONObject jobj = new JSONObject();
						int m_const = 0;
						String meter_const = "";
						int MeterAddress = 0;//"";
						String CurrentLDU_Data = "";
						int readRackId= 0;
						//lscsLDU_SendRefreshDataCommand();
						//Sleep(1000);
						//lscsLDU_SendRefreshDataCommand();
						Sleep(1000);
						//for(int rackId=1; rackId<=ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK; rackId++){
						for(int i=0; i<deployed_devices.length(); i++){
							jobj = deployed_devices.getJSONObject(i);
							m_const = jobj.getInt("meter_const");
							readRackId = jobj.getInt("Rack_ID");
							//if(readRackId == rackId){
							meter_const = Integer.toString(m_const);
							//MeterAddress = Integer.toString(readRackId);
							MeterAddress = readRackId;
							//MeterAddress = "0"+Integer.toString(i+1);
							/*							if(i==0){
											MeterAddress = Integer.toString(19);
										}else{
											MeterAddress = Integer.toString(20);
										}*/
							//MeterAddress = lscsLduAddressMapping((i+1));
							ApplicationLauncher.logger.info("bofaSendLDU_SettingCommand: meter_const2: "+ meter_const);
							ApplicationLauncher.logger.info("bofaSendLDU_SettingCommand: MeterAddress2: "+ MeterAddress);
							//LDU_SendCommandErrorSetting(MeterAddress,meter_const);
							//							lscsLDU_SendCommandErrorSetting(MeterAddress,meter_const);
							//							AllDeviceStatus ++;
							ApplicationHomeController.update_left_status("LDU setting update: " + readRackId + "/" + deployed_devices.length(),ConstantApp.LEFT_STATUS_DEBUG);
							//bofaLduSendCommandErrorSettingForIndividualMeter(MeterAddress,meter_const);
							bofaLduSendCommandErrorSetting(MeterAddress,meter_const);
							AllDeviceStatus ++;
							//}
							//}
							//}



							/*							lduData3 = LDU_ReadData(ConstantLduCcube.CMD_LDU_ERROR_RESET_ER.length(),ConstantLduCcube.CMD_LDU_ERROR_RESET_ER);
								if(lduData3.IsExpectedResponseReceived()){

									CurrentLDU_Data =lduData3.getLDU_ReadSerialData();
									ApplicationLauncher.logger.info("LDU_ResetSetting:LDU Received Data"+ i + ":"+CurrentLDU_Data);
									StripLDU_SerialData(lduData3.getReceivedLength());
									AllDeviceStatus ++;
								}
								CurrentLDU_Data =lduData3.getLDU_ReadSerialData();
								ApplicationLauncher.logger.info("LDU_ResetSetting: test LDU Received Data"+ i + ":"+CurrentLDU_Data);*/

						}
						if (AllDeviceStatus == deployed_devices.length()){
							ApplicationLauncher.logger.debug("bofaSendLDU_SettingCommand :All Device response: Passed" );
							status = true;
						}else{
							status = false;
							ApplicationLauncher.logger.debug("bofaSendLDU_SettingCommand :All Device response: Failed:AllDeviceStatus:"+AllDeviceStatus+ "deployed_devices.length():"+deployed_devices.length());
						}

					}
				} catch (JSONException e) {
					
					e.printStackTrace();
					ApplicationLauncher.logger.error("bofaSendLDU_SettingCommand :JSONException: "+e.getMessage() );
				}
			} 
			//}
			return status;
		}
		
		public static void bofaLduSendCommandErrorSetting(int MeterAddress, String meter_const){
			ApplicationLauncher.logger.debug("bofaLduSendCommandErrorSetting :Entry");
			String MeterConstUnit = "00";
			//String RSS_PulseRate = DisplayDataObj.getRSSPulseRate();

			//long RssConstantInKWh = Long.parseLong(DisplayDataObj.getRSSPulseRate())*1000;
			long RssConstantInKWh = 0L;
			/*		if(ProcalFeatureEnable.SANDS_REFSTD_CONNECTED){
				RssConstantInKWh = Long.parseLong(DisplayDataObj.getRSSPulseRate());
			}else{*/
			RssConstantInKWh = Data_RefStdBofa.getLiveRefMeterConstant();;//Long.parseLong(DisplayDataObj.getRSSPulseRate())*1000;
			//		}
			/*		if(ConstantFeatureEnable.REF_STD_CONST_CALCULATE){
				if (!DevicePortSetupController.getPortValidationTurnedON()){
					//long RssConstantInWh = calculateRSS_ConstantV4(meter_const);
					RssConstantInKWh = calculateRSS_ConstantV4(meter_const)*1000;
				}
			}*/
			ApplicationLauncher.logger.debug("bofaLduSendCommandErrorSetting :RssConstantInKWh: "+String.valueOf(RssConstantInKWh));
			//String RSS_PulseRate = GUIUtils.FormatPulseRate(String.valueOf(RssConstantInKWh));
			String AverageCycle = "";
			//String CalculateMode = "00";
			if(DisplayDataObj.getTestRunType().equals(ConstantApp.TESTPOINT_RUNTYPE_PULSEBASED)){
				//AverageCycle = GUIUtils.FormatAvgPulses(DeviceDataManagerController.getNoOfPulses());
				AverageCycle = DeviceDataManagerController.getNoOfPulses();
				ApplicationLauncher.logger.info("bofaLduSendCommandErrorSetting:AverageCycle-Pulse-Based : NoOfPulses: " + AverageCycle);
				//CalculateMode = "00";
			}else if(DisplayDataObj.getTestRunType().equals(ConstantApp.TESTPOINT_RUNTYPE_TIMEBASED)){
				int timeDurationInSec  = DisplayDataObj.getInfTimeDuration();
				float totalTargetPowerInKiloWatt = calculateTotalTargetPower();
				int meterConstantInImpulsesPerKiloWattHour = Integer.parseInt(meter_const);
				AverageCycle = DisplayDataObj.manipulateNoOfPulsesForTimeBased(meterConstantInImpulsesPerKiloWattHour,timeDurationInSec,totalTargetPowerInKiloWatt); 
				ApplicationLauncher.logger.info("bofaLduSendCommandErrorSetting:AverageCycle-Time-Based : NoOfPulses: " + AverageCycle);
				//CalculateMode = "01";
			}
			/*		else{
				//AverageCycle = GUIUtils.FormatTimeForAvgPulses(DeviceDataManagerController.getInfTimeDuration());
				AverageCycle = DeviceDataManagerController.getNoOfPulses();bnbn n
				ApplicationLauncher.logger.info("bofaLduSendCommandErrorSetting:AverageCycle-Time-Based :  " + AverageCycle);
				//CalculateMode = "01";
			}*/



			String ErrorHighLimit = GuiUtils.FormatErrorInput(DeviceDataManagerController.get_Error_max());
			String ErrorLowLimit = GuiUtils.FormatErrorInput(DeviceDataManagerController.get_Error_min());
			//String MUT_PulseRate = GUIUtils.FormatPulseRate(meter_const);
			ApplicationLauncher.logger.info("bofaLduSendCommandErrorSetting: get_No_of_impulses: " + DeviceDataManagerController.getDutImpulsesPerUnit());
			//ApplicationLauncher.logger.info("bofaLduSendCommandErrorSetting: MUT_PulseRate: " + MUT_PulseRate);
			//ApplicationLauncher.logger.info("bofaLduSendCommandErrorSetting :RSS_PulseRate: " + RSS_PulseRate);
			ApplicationLauncher.logger.info("ErrorHighLimit1: " + ErrorHighLimit);
			ApplicationLauncher.logger.info("ErrorLowLimit1: " + ErrorLowLimit);  

			frameTestTurnsAndPulsesCmd(MeterAddress,meter_const,AverageCycle);
			//sendTestTurnsAndPulsesCmd(0);
			sendBroadCastTestTurnsAndPulsesCmd();/////
			/*int RssPulsePerMutPulse = 0;
			RssPulsePerMutPulse = ceigLDU_SettingCalculationMethod( RssConstantInKWh, meter_const, AverageCycle);
			String rssPulsePerMutPulseStr = String.valueOf(RssPulsePerMutPulse);
			lscsLDU_SendCeigSettingMethod(rssPulsePerMutPulseStr,AverageCycle);
		*/
			//commLDU.SetRTS(true);
			//LDU_SendMeterAddress(MeterAddress);
			//LDU_SendMeterConstantUnit(MeterConstUnit);
			//LDU_SetRSS_PulseRate(RSS_PulseRate);
			//LDU_SetMUT_PulseRate(MUT_PulseRate);
			//LDU_SetAverageCycle(AverageCycle);
			//LDU_SetErrorHighLimit(ErrorHighLimit);
			//LDU_SetErrorLowLimit(ErrorLowLimit);
			//LDU_SetErrorCalculationMode(CalculateMode);
			//commLDU.SetRTS(false);
			ApplicationLauncher.logger.debug("bofaLduSendCommandErrorSetting: LDU setting DataSend Completed");



		}


		public static void bofaLduSendCommandErrorSettingForIndividualMeter(String MeterAddress, String meter_const){
			ApplicationLauncher.logger.debug("bofaLduSendCommandErrorSettingForIndividualMeter :Entry");
			ApplicationLauncher.logger.debug("bofaLduSendCommandErrorSettingForIndividualMeter : MeterAddress: " + MeterAddress);
			String MeterConstUnit = "00";
			//String RSS_PulseRate = DisplayDataObj.getRSSPulseRate();

			//long RssConstantInKWh = Long.parseLong(DisplayDataObj.getRSSPulseRate())*1000;
			long RssConstantInKWh = Long.parseLong(DisplayDataObj.getRSSPulseRate())*1000;
			/*		if(ConstantFeatureEnable.REF_STD_CONST_CALCULATE){
				if (!DevicePortSetupController.getPortValidationTurnedON()){
					//long RssConstantInWh = calculateRSS_ConstantV4(meter_const);
					RssConstantInKWh = calculateRSS_ConstantV4(meter_const)*1000;
				}
			}*/
			ApplicationLauncher.logger.debug("bofaLduSendCommandErrorSettingForIndividualMeter :RssConstantInKWh: "+String.valueOf(RssConstantInKWh));
			//String RSS_PulseRate = GUIUtils.FormatPulseRate(String.valueOf(RssConstantInKWh));
			String AverageCycle = "";
			//String CalculateMode = "00";
			if(DisplayDataObj.getTestRunType().equals(ConstantApp.TESTPOINT_RUNTYPE_PULSEBASED)){
				//AverageCycle = GUIUtils.FormatAvgPulses(DeviceDataManagerController.getNoOfPulses());
				AverageCycle = DeviceDataManagerController.getNoOfPulses();
				ApplicationLauncher.logger.info("bofaLduSendCommandErrorSettingForIndividualMeter-Pulse-Based : NoOfPulses: " + AverageCycle);
				//CalculateMode = "00";
			}else if(DisplayDataObj.getTestRunType().equals(ConstantApp.TESTPOINT_RUNTYPE_TIMEBASED)){
				//AverageCycle = GUIUtils.FormatTimeForAvgPulses(DeviceDataManagerController.getInfTimeDuration());
				//AverageCycle = DeviceDataManagerController.getNoOfPulses();
				int timeDurationInSec  = DisplayDataObj.getInfTimeDuration();
				float totalTargetPowerInKiloWatt = calculateTotalTargetPower();
				int meterConstantInImpulsesPerKiloWattHour = Integer.parseInt(meter_const);
				AverageCycle = DisplayDataObj.manipulateNoOfPulsesForTimeBased(meterConstantInImpulsesPerKiloWattHour,timeDurationInSec,totalTargetPowerInKiloWatt); 
				ApplicationLauncher.logger.info("bofaLduSendCommandErrorSettingForIndividualMeter-Time-Based : NoOfPulses: " + AverageCycle);
				//CalculateMode = "01";
			}





			String ErrorHighLimit = GuiUtils.FormatErrorInput(DeviceDataManagerController.get_Error_max());
			String ErrorLowLimit = GuiUtils.FormatErrorInput(DeviceDataManagerController.get_Error_min());
			//String MUT_PulseRate = GUIUtils.FormatPulseRate(meter_const);
			ApplicationLauncher.logger.info("bofaLduSendCommandErrorSettingForIndividualMeter: getDutImpulsesPerUnit: " + DeviceDataManagerController.getDutImpulsesPerUnit());
			ApplicationLauncher.logger.info("bofaLduSendCommandErrorSettingForIndividualMeter: meter_const: " + meter_const);
			//ApplicationLauncher.logger.info("lscsLDU_SendCommandErrorSetting: MUT_PulseRate: " + MUT_PulseRate);
			//ApplicationLauncher.logger.info("lscsLDU_SendCommandErrorSetting :RSS_PulseRate: " + RSS_PulseRate);
			ApplicationLauncher.logger.info("bofaLduSendCommandErrorSettingForIndividualMeter: ErrorHighLimit1: " + ErrorHighLimit);
			ApplicationLauncher.logger.info("bofaLduSendCommandErrorSettingForIndividualMeter: ErrorLowLimit1: " + ErrorLowLimit);  
			ApplicationLauncher.logger.info("bofaLduSendCommandErrorSettingForIndividualMeter: MeterAddress: " + MeterAddress);  
			
			int meterAddressInt = Integer.parseInt( MeterAddress);
			frameTestTurnsAndPulsesCmd(meterAddressInt,meter_const,AverageCycle);
			boolean responseExpected = false;
			sendTestTurnsAndPulsesCmd(0,responseExpected);
			/*int RssPulsePerMutPulse = 0;
			RssPulsePerMutPulse = ceigLDU_SettingCalculationMethod( RssConstantInKWh, meter_const, AverageCycle);
			String rssPulsePerMutPulseStr = String.valueOf(RssPulsePerMutPulse);
			lscsLDU_SendCeigSettingMethodIndividualMeter(MeterAddress,rssPulsePerMutPulseStr,AverageCycle);
			ApplicationLauncher.logger.debug("lscsLDU_SendCommandErrorSettingForIndividualMeter: lscsLDU setting DataSend Completed");
*/


		}
		
		
		//===============================================================================================================
		
		
		private static float calculateTotalTargetPower() {

			ApplicationLauncher.logger.debug("calculateTotalTargetPower :Entry");
			float targetPowerInKiloWatt = 0.0f;


			try{

				float targetRphaseVoltage = Float.parseFloat(DisplayDataObj.getR_PhaseOutputVoltage());
				float targetRphaseCurrent = Float.parseFloat(DisplayDataObj.getR_PhaseOutputCurrent());
				float targetRphasePowerFactor = 0.0f;


				String metertype = DisplayDataObj.getDeployedEM_ModelType();

				/*		if(metertype.contains(ConstantApp.METERTYPE_ACTIVE)){
					ArrayList<String> I_PF_values = DisplayDataObj.ExtractI_PF_From_TP_Name(ProjectExecutionController.getCurrentTestPointName());
					String pfValueStr = I_PF_values.get(0).replace("C", "").replace("L", "");
					targetRphasePowerFactor = Float.parseFloat(pfValueStr);
				}else if(metertype.contains(ConstantApp.METERTYPE_REACTIVE)){
					//DisplayDataObj.get_PwrSrcR_PhaseDegreePhase();v v 
				}*/
				float rPhaseDegree = DisplayDataObj.get_PwrSrcR_PhaseDegreePhase();
				ApplicationLauncher.logger.debug("calculateTotalTargetPower : rPhaseDegree : " + rPhaseDegree  );
				targetRphasePowerFactor = (float) Math.cos(Math.toRadians(rPhaseDegree));
				BigDecimal bigValue = new BigDecimal(targetRphasePowerFactor);
				bigValue = bigValue.setScale(6, RoundingMode.FLOOR);
				ApplicationLauncher.logger.debug("calculateTotalTargetPower : rPhaseDegree : bigValue :" + bigValue);
				targetRphasePowerFactor = bigValue.floatValue();
				ApplicationLauncher.logger.debug("calculateTotalTargetPower : targetRphasePowerFactor : " + targetRphasePowerFactor  );
				//targetRphasePowerFactor = (float) Math.cos(rPhaseDegree);
				if(metertype.contains(ConstantApp.METERTYPE_SINGLEPHASE)){
					targetPowerInKiloWatt = (targetRphaseVoltage*targetRphaseCurrent*targetRphasePowerFactor)/1000.0f;
					ApplicationLauncher.logger.debug("calculateTotalTargetPower : single Phase : targetPowerInKiloWatt : " + targetPowerInKiloWatt);
				}else{
					float targetYphaseVoltage = Float.parseFloat(DisplayDataObj.getY_PhaseOutputVoltage());
					float targetYphaseCurrent = Float.parseFloat(DisplayDataObj.getY_PhaseOutputCurrent());
					float targetYphasePowerFactor = 0.0f;

					float yPhaseDegree = DisplayDataObj.get_PwrSrcY_PhaseDegreePhase();
					ApplicationLauncher.logger.debug("calculateTotalTargetPower : yPhaseDegree : " + yPhaseDegree  );
					targetYphasePowerFactor = (float) Math.cos(Math.toRadians(yPhaseDegree));
					bigValue = new BigDecimal(targetYphasePowerFactor);
					bigValue = bigValue.setScale(6, RoundingMode.FLOOR);
					ApplicationLauncher.logger.debug("calculateTotalTargetPower : yPhaseDegree : bigValue :" + bigValue);
					targetYphasePowerFactor = bigValue.floatValue();
					ApplicationLauncher.logger.debug("calculateTotalTargetPower : targetYphasePowerFactor : " + targetYphasePowerFactor  );



					float targetBphaseVoltage = Float.parseFloat(DisplayDataObj.getB_PhaseOutputVoltage());
					float targetBphaseCurrent = Float.parseFloat(DisplayDataObj.getB_PhaseOutputCurrent());
					float targetBphasePowerFactor = 0.0f;

					float bPhaseDegree = DisplayDataObj.get_PwrSrcY_PhaseDegreePhase();
					ApplicationLauncher.logger.debug("calculateTotalTargetPower : bPhaseDegree : " + bPhaseDegree  );
					targetBphasePowerFactor = (float) Math.cos(Math.toRadians(bPhaseDegree));
					bigValue = new BigDecimal(targetBphasePowerFactor);
					bigValue = bigValue.setScale(6, RoundingMode.FLOOR);
					ApplicationLauncher.logger.debug("calculateTotalTargetPower : bPhaseDegree : bigValue :" + bigValue);
					targetBphasePowerFactor = bigValue.floatValue();
					ApplicationLauncher.logger.debug("calculateTotalTargetPower : targetBphasePowerFactor : " + targetBphasePowerFactor  );


					float rPhasePowerInWatt = targetRphaseVoltage*targetRphaseCurrent*targetRphasePowerFactor;
					float yPhasePowerInWatt = targetYphaseVoltage*targetYphaseCurrent*targetYphasePowerFactor;
					float bPhasePowerInWatt = targetBphaseVoltage*targetBphaseCurrent*targetBphasePowerFactor;
					ApplicationLauncher.logger.debug("calculateTotalTargetPower : Three Phase : rPhasePowerInWatt : " + rPhasePowerInWatt);
					ApplicationLauncher.logger.debug("calculateTotalTargetPower : Three Phase : yPhasePowerInWatt : " + yPhasePowerInWatt);
					ApplicationLauncher.logger.debug("calculateTotalTargetPower : Three Phase : bPhasePowerInWatt : " + bPhasePowerInWatt);
					targetPowerInKiloWatt = (rPhasePowerInWatt+ yPhasePowerInWatt+bPhasePowerInWatt)/1000.0f;
					ApplicationLauncher.logger.debug("calculateTotalTargetPower : Three Phase : targetPowerInKiloWatt : " + targetPowerInKiloWatt);
				}

			} catch(Exception e) {
				e.printStackTrace();
				ApplicationLauncher.logger.error("calculateTotalTargetPower exception: " + e.getMessage());
			}

			// TODO Auto-generated method stub
			return targetPowerInKiloWatt;
		}

		public void bofaDisplayLDU_ReadErrorTimerInit() {
			ApplicationLauncher.logger.info("bofaDisplayLDU_ReadErrorTimerInit :Entry");
			ApplicationLauncher.logger.info("bofaDisplayLDU_ReadErrorTimerInit :getLDU_ResetSettingStatus():" +SerialDM_Obj.getLDU_ResetSettingStatus());

			bofaLDU_ReadErrorTimerTrigger();
			
			
			/*			if(SerialDM_Obj.getLDU_ResetSettingStatus()){
				ApplicationLauncher.logger.info("DisplayLDU_ReadErrorTimerInit :Triggering: LDU_ReadErrorTimerTrigger" );

				SerialDM_Obj.LDU_ReadErrorTimerTrigger();
			}
			else{
				ApplicationLauncher.logger.info("DisplayLDU_ReadErrorTimerInit :Error Code LDU03: LDU_ResetSetting not invoked");
			}*/
		}
	
		public void bofaLDU_ReadErrorTimerTrigger() {
			ApplicationLauncher.logger.debug("bofaLDU_ReadErrorTimerTrigger :Entry");
			//LDU_ComSemlock = true;
			lduTimer = new Timer();
			setTimeExtendedForTimeBased(false);
			ApplicationLauncher.logger.info("bofaLDU_ReadErrorTimerTrigger :getSerialLDU_ComRefreshTimeInMsec:" + getSerialLDU_ComRefreshTimeInMsec());
			lduTimer.schedule(new BofaLduComReadErrorTask(), getSerialLDU_ComRefreshTimeInMsec());// 1000);
/*			if(lduComSerialStatusConnected){
				DisplayDataObj.setLDU_ReadDataFlag( true);
			}*/
			ApplicationLauncher.logger.debug("bofaLDU_ReadErrorTimerTrigger: Exit:");

		}
		
		public boolean IsDeviceToBeRead(int Address){
			//ApplicationLauncher.logger.debug("IsDeviceToBeRead:  Entry");
			ArrayList<Integer> devices_mounted = DisplayDataObj.getDevicesToBeRead();
			if(devices_mounted.contains(Address)){
				ApplicationLauncher.logger.debug("IsDeviceToBeRead: " + Address + ": Yes");
				return true;
			}
			else{
				ApplicationLauncher.logger.debug("IsDeviceToBeRead: " + Address + ": No");
				return false;
			}
		}
		
		class BofaLduComReadErrorTask extends TimerTask {
			public void run() {

				int MaximumNumberOfDeviceConnected = ProjectExecutionController.getListOfDevices().length();
				ApplicationLauncher.logger.debug("MaximumNumberOfDeviceConnected: " + MaximumNumberOfDeviceConnected);

				if (DisplayDataObj.getLDU_ReadDataFlag()){// && lduComSerialStatusConnected){
					//ApplicationLauncher.logger.debug("lduComReadErrorTask: getLDU_ReadDataFlag True entry");
					//if(LDU_ComSemlock){
					//	ApplicationLauncher.logger.debug("lduComReadErrorTask: LDU_ComSemlock True entry");
					//	LDU_ComSemlock = false;
						try {
							//for(int Address=1;Address<=MaximumNumberOfDeviceConnected;Address++){
							for(String lduPosition : ProjectExecutionController.getDeviceMountedMap().keySet()){	
								ApplicationLauncher.logger.debug("BofaLduComReadErrorTask: lduPosition:" + lduPosition);
								//ApplicationLauncher.logger.debug("BofaLduComReadErrorTask: getListOfDevices:" + ProjectExecutionController.getListOfDevices());
								//ApplicationLauncher.logger.debug("BofaLduComReadErrorTask: getListOfDevices2:" + ProjectExecutionController.getListOfDevices().getBoolean(String.format("%02d", Address)));
								//if(ProjectExecutionController.getListOfDevices().getBoolean(String.format("%02d", Address))){
									//ApplicationLauncher.logger.debug("BofaLduComReadErrorTask: getListOfDevices True entry");
									if(IsDeviceToBeRead(Integer.parseInt(lduPosition))){
										ApplicationLauncher.logger.debug("Reading LDU position: "+lduPosition);
										if(!ProjectExecutionController.getUserAbortedFlag()){
											if(DeviceDataManagerController.getLDU_ReadDataFlag()){
												
													bofaLDU_ReadErrorData(Integer.parseInt(lduPosition)); 
												
											}else{
												ApplicationLauncher.logger.debug("BofaLduComReadErrorTask: getLDU_ReadDataFlag exit");
											}
										}else{
											ApplicationLauncher.logger.debug("BofaLduComReadErrorTask: UserAbortedFlag exit");
										}
									}
								//}
							}
						} catch (Exception e) {
							
							e.printStackTrace();
							ApplicationLauncher.logger.error("BofaLduComReadErrorTask: Exception:" + e.getMessage());
						}
					//	LDU_ComSemlock = true;
					//}
					
						
					//if(lduComSerialStatusConnected){
						try{
							//for repeatabality test check
							if(getSerialLDU_ComRefreshTimeInMsec() != SerialLDU_ComRefreshDefaultTimeInMsec){
								if(DisplayDataObj.getDevicesToBeRead().size()>0){
									if(!ProjectExecutionController.getUserAbortedFlag()){
										if(DeviceDataManagerController.getLDU_ReadDataFlag()){

											lduTimer.schedule(new BofaLduComReadErrorTask(), SerialLDU_ComRefreshDefaultTimeInMsec);//updated by gopi - for the refresh to read the device before exiting the LDUreadbuffertime
										}
									}else{
										ApplicationLauncher.logger.debug("BofaLduComReadErrorTask:Timer Exit4 !%n");
										lduTimer.cancel(); //Terminate the timer thread
									}
								}else{
									ApplicationLauncher.logger.debug("BofaLduComReadErrorTask:Timer Exit1 !%n");
									lduTimer.cancel(); //Terminate the timer thread
								}
							}
							else{
								if(!ProjectExecutionController.getUserAbortedFlag()){
									if(DeviceDataManagerController.getLDU_ReadDataFlag()){
										//ApplicationLauncher.logger.debug("BofaLduComReadErrorTask: validating device to be read");
										if(DisplayDataObj.getDevicesToBeRead().size() !=0){
											ApplicationLauncher.logger.debug("BofaLduComReadErrorTask:Scheduling task again");
											lduTimer.schedule(new BofaLduComReadErrorTask(), getSerialLDU_ComRefreshTimeInMsec());
										}/*else{
											ApplicationLauncher.logger.debug("BofaLduComReadErrorTask: send refresh command");
											lscsLDU_SendRefreshDataCommand();

										}*/
									}
								}else{
									ApplicationLauncher.logger.debug("BofaLduComReadErrorTask:Timer Exit3 !%n");
									lduTimer.cancel(); //Terminate the timer thread
								}
							}

						}
						catch(Exception e){
							e.printStackTrace();
							ApplicationLauncher.logger.error("BofaLduComReadErrorTask: Exception:" + e.getMessage());
							ApplicationLauncher.logger.info("BofaLduComReadErrorTask: lduTimer already Cancelled");

						}
					//}


				}
				else{

					ApplicationLauncher.logger.debug("BofaLduComReadErrorTask:Timer Exit2 !%n");
					lduTimer.cancel(); //Terminate the timer thread

				}
			}
		}
		
//=====================
		public class BofaLduComReadStaTask extends TimerTask {
			public void run() {
				ApplicationLauncher.logger.debug("BofaLduComReadStaTask: Entry");
				int MaximumNumberOfDeviceConnected = ProjectExecutionController.getListOfDevices().length();
				int initialLduAddress = 1;
				if(ProcalFeatureEnable.RACK_HYBRID_MODE_ENABLED){
					
					if(DisplayDataObj.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_SINGLEPHASE)){
						initialLduAddress = ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_START_POSITION;
						MaximumNumberOfDeviceConnected = initialLduAddress + ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_POSITIONS-1;
					}else if(DisplayDataObj.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_THREEPHASE)){
						initialLduAddress = ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_START_POSITION;
						MaximumNumberOfDeviceConnected = initialLduAddress + ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_POSITIONS-1;
					}
				}
				if (DisplayDataObj.getLDU_ReadDataFlag()){// && lduComSerialStatusConnected){
					//ApplicationLauncher.logger.debug("lduComReadErrorTask: getLDU_ReadDataFlag True entry");
					//if(LDU_ComSemlock){
					//	ApplicationLauncher.logger.debug("lduComReadErrorTask: LDU_ComSemlock True entry");
					//	LDU_ComSemlock = false;
						try {
							//for(int Address=1; Address<=MaximumNumberOfDeviceConnected; Address++){
							for(String lduPosition : ProjectExecutionController.getDeviceMountedMap().keySet()){	
								ApplicationLauncher.logger.debug("BofaLduComReadStaTask: lduPosition:" + lduPosition);
								//ApplicationLauncher.logger.debug("BofaLduComReadStaTask: getListOfDevices:" + ProjectExecutionController.getListOfDevices());
								//ApplicationLauncher.logger.debug("BofaLduComReadStaTask: getListOfDevices2:" + ProjectExecutionController.getListOfDevices().getBoolean(String.format("%02d", Address)));
								//if(ProjectExecutionController.getListOfDevices().getBoolean(String.format("%02d", Address))){
									//ApplicationLauncher.logger.debug("BofaLduComReadStaTask: getListOfDevices True entry");
									if(IsDeviceToBeRead(Integer.parseInt(lduPosition))){
										ApplicationLauncher.logger.debug("BofaLduComReadStaTask :Reading LDU:"+lduPosition);
										if(!ProjectExecutionController.getUserAbortedFlag()){
											if(DeviceDataManagerController.getLDU_ReadDataFlag()){
												
													bofaLduReadStaData(Integer.parseInt(lduPosition)); 
												
											}else{
												ApplicationLauncher.logger.debug("BofaLduComReadStaTask: getLDU_ReadDataFlag exit");
											}
										}else{
											ApplicationLauncher.logger.debug("BofaLduComReadStaTask: UserAbortedFlag exit");
										}
									}
								//}
							}
						} catch (Exception e) {
							
							e.printStackTrace();
							ApplicationLauncher.logger.error("BofaLduComReadStaTask: Exception:" + e.getMessage());
						}
					//	LDU_ComSemlock = true;
					//}
					
						
					//if(lduComSerialStatusConnected){
						try{
							//for repeatabality test check
							if(getSerialLDU_ComRefreshTimeInMsec() != SerialLDU_ComRefreshDefaultTimeInMsec){
								if(DisplayDataObj.getDevicesToBeRead().size()>0){
									if(!ProjectExecutionController.getUserAbortedFlag()){
										if(DeviceDataManagerController.getLDU_ReadDataFlag()){

											lduTimer.schedule(new BofaLduComReadStaTask(), SerialLDU_ComRefreshDefaultTimeInMsec);//updated by gopi - for the refresh to read the device before exiting the LDUreadbuffertime
										}
									}else{
										ApplicationLauncher.logger.debug("BofaLduComReadStaTask:Timer Exit4 !%n");
										lduTimer.cancel(); //Terminate the timer thread
									}
								}else{
									ApplicationLauncher.logger.debug("BofaLduComReadStaTask:Timer Exit1 !%n");
									lduTimer.cancel(); //Terminate the timer thread
								}
							}
							else{
								if(!ProjectExecutionController.getUserAbortedFlag()){
									if(DeviceDataManagerController.getLDU_ReadDataFlag()){
										ApplicationLauncher.logger.debug("BofaLduComReadStaTask: validating device to be read");
										if(DisplayDataObj.getDevicesToBeRead().size() !=0){
											ApplicationLauncher.logger.debug("BofaLduComReadStaTask:Scheduling task again");
											lduTimer.schedule(new BofaLduComReadStaTask(), getSerialLDU_ComRefreshTimeInMsec());
										}/*else{
											ApplicationLauncher.logger.debug("BofaLduComReadStaTask: send refresh command");
											lscsLDU_SendRefreshDataCommand();

										}*/
									}
								}else{
									ApplicationLauncher.logger.debug("BofaLduComReadStaTask:Timer Exit3 !%n");
									lduTimer.cancel(); //Terminate the timer thread
								}
							}

						}
						catch(Exception e){
							e.printStackTrace();
							ApplicationLauncher.logger.error("BofaLduComReadStaTask: Exception:" + e.getMessage());
							ApplicationLauncher.logger.info("BofaLduComReadStaTask: lduTimer already Cancelled");

						}
					//}


				}
				else{

					ApplicationLauncher.logger.debug("BofaLduComReadStaTask:Timer Exit2 !");
					lduTimer.cancel(); //Terminate the timer thread

				}
			}
		}
		
//===================================================================================================================================================================================		
		
		public class BofaLduComReadCreepTask extends TimerTask {
			public void run() {
				ApplicationLauncher.logger.debug("BofaLduComReadCreepTask: Entry");
				int MaximumNumberOfDeviceConnected = ProjectExecutionController.getListOfDevices().length();
				int initialLduAddress = 1;
				if(ProcalFeatureEnable.RACK_HYBRID_MODE_ENABLED){
					
					if(DisplayDataObj.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_SINGLEPHASE)){
						initialLduAddress = ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_START_POSITION;
						MaximumNumberOfDeviceConnected = initialLduAddress + ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_POSITIONS-1;
					}else if(DisplayDataObj.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_THREEPHASE)){
						initialLduAddress = ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_START_POSITION;
						MaximumNumberOfDeviceConnected = initialLduAddress + ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_POSITIONS-1;
					}
				}
				if (DisplayDataObj.getLDU_ReadDataFlag()){// && lduComSerialStatusConnected){
					//ApplicationLauncher.logger.debug("lduComReadErrorTask: getLDU_ReadDataFlag True entry");
					//if(LDU_ComSemlock){
					//	ApplicationLauncher.logger.debug("lduComReadErrorTask: LDU_ComSemlock True entry");
					//	LDU_ComSemlock = false;
						try {
							//for(int Address=1; Address<=MaximumNumberOfDeviceConnected; Address++){
							for(String lduPosition : ProjectExecutionController.getDeviceMountedMap().keySet()){
								ApplicationLauncher.logger.debug("BofaLduComReadCreepTask: Address:" + lduPosition);
								//ApplicationLauncher.logger.debug("BofaLduComReadCreepTask: getListOfDevices:" + ProjectExecutionController.getListOfDevices());
								//ApplicationLauncher.logger.debug("BofaLduComReadCreepTask: getListOfDevices2:" + ProjectExecutionController.getListOfDevices().getBoolean(String.format("%02d", Address)));
								//if(ProjectExecutionController.getListOfDevices().getBoolean(String.format("%02d", Address))){
									//ApplicationLauncher.logger.debug("BofaLduComReadCreepTask: getListOfDevices True entry");
									if(IsDeviceToBeRead(Integer.parseInt(lduPosition))){
										ApplicationLauncher.logger.debug("BofaLduComReadCreepTask : Reading LDU: "+lduPosition);
										if(!ProjectExecutionController.getUserAbortedFlag()){
											if(DeviceDataManagerController.getLDU_ReadDataFlag()){
												
													bofaLduReadCreepData(Integer.parseInt(lduPosition)); 
												
											}else{
												ApplicationLauncher.logger.debug("BofaLduComReadCreepTask: getLDU_ReadDataFlag exit");
											}
										}else{
											ApplicationLauncher.logger.debug("BofaLduComReadCreepTask: UserAbortedFlag exit");
										}
									}
								//}
							}
						} catch (Exception e) {
							
							e.printStackTrace();
							ApplicationLauncher.logger.error("BofaLduComReadCreepTask: Exception:" + e.getMessage());
						}
					//	LDU_ComSemlock = true;
					//}
					
						
					//if(lduComSerialStatusConnected){
						try{
							//for repeatabality test check
							if(getSerialLDU_ComRefreshTimeInMsec() != SerialLDU_ComRefreshDefaultTimeInMsec){
								if(DisplayDataObj.getDevicesToBeRead().size()>0){
									if(!ProjectExecutionController.getUserAbortedFlag()){
										if(DeviceDataManagerController.getLDU_ReadDataFlag()){

											lduTimer.schedule(new BofaLduComReadCreepTask(), SerialLDU_ComRefreshDefaultTimeInMsec);//updated by gopi - for the refresh to read the device before exiting the LDUreadbuffertime
										}
									}else{
										ApplicationLauncher.logger.debug("BofaLduComReadCreepTask: Timer Exit4 !%n");
										lduTimer.cancel(); //Terminate the timer thread
									}
								}else{
									ApplicationLauncher.logger.debug("BofaLduComReadCreepTask: Timer Exit1 !%n");
									lduTimer.cancel(); //Terminate the timer thread
								}
							}
							else{
								if(!ProjectExecutionController.getUserAbortedFlag()){
									if(DeviceDataManagerController.getLDU_ReadDataFlag()){
										ApplicationLauncher.logger.debug("BofaLduComReadCreepTask: validating device to be read");
										if(DisplayDataObj.getDevicesToBeRead().size() !=0){
											ApplicationLauncher.logger.debug("BofaLduComReadCreepTask: Scheduling task again");
											lduTimer.schedule(new BofaLduComReadCreepTask(), getSerialLDU_ComRefreshTimeInMsec());
										}/*else{
											ApplicationLauncher.logger.debug("BofaLduComReadStaTask: send refresh command");
											lscsLDU_SendRefreshDataCommand();

										}*/
									}
								}else{
									ApplicationLauncher.logger.debug("BofaLduComReadCreepTask: Timer Exit3 !%n");
									lduTimer.cancel(); //Terminate the timer thread
								}
							}

						}
						catch(Exception e){
							e.printStackTrace();
							ApplicationLauncher.logger.error("BofaLduComReadCreepTask: Exception:" + e.getMessage());
							ApplicationLauncher.logger.info("BofaLduComReadCreepTask: lduTimer already Cancelled");

						}
					//}


				}
				else{

					ApplicationLauncher.logger.debug("BofaLduComReadCreepTask: Timer Exit2 !");
					lduTimer.cancel(); //Terminate the timer thread

				}
			}
		}

//================================================================================
		
		
		
		public class BofaLduComReadConstantTestTask extends TimerTask {
			public void run() {
				ApplicationLauncher.logger.debug("BofaLduComReadConstantTestTask: Entry");
				int MaximumNumberOfDeviceConnected = ProjectExecutionController.getListOfDevices().length();
				int initialLduAddress = 1;
				if(ProcalFeatureEnable.RACK_HYBRID_MODE_ENABLED){
					
					if(DisplayDataObj.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_SINGLEPHASE)){
						initialLduAddress = ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_START_POSITION;
						MaximumNumberOfDeviceConnected = initialLduAddress + ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_POSITIONS-1;
					}else if(DisplayDataObj.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_THREEPHASE)){
						initialLduAddress = ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_START_POSITION;
						MaximumNumberOfDeviceConnected = initialLduAddress + ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_POSITIONS-1;
					}
				}
				if (DisplayDataObj.getLDU_ReadDataFlag()){// && lduComSerialStatusConnected){
					//ApplicationLauncher.logger.debug("lduComReadErrorTask: getLDU_ReadDataFlag True entry");
					//if(LDU_ComSemlock){
					//	ApplicationLauncher.logger.debug("lduComReadErrorTask: LDU_ComSemlock True entry");
					//	LDU_ComSemlock = false;
						try {
							//for(int Address=1; Address<=MaximumNumberOfDeviceConnected; Address++){
							for(String lduPosition : ProjectExecutionController.getDeviceMountedMap().keySet()){
								ApplicationLauncher.logger.debug("BofaLduComReadConstantTestTask: lduPosition:" + lduPosition);
								//ApplicationLauncher.logger.debug("BofaLduComReadConstantTestTask: getListOfDevices:" + ProjectExecutionController.getListOfDevices());
								//ApplicationLauncher.logger.debug("BofaLduComReadConstantTestTask: getListOfDevices2:" + ProjectExecutionController.getListOfDevices().getBoolean(String.format("%02d", Address)));
								//if(ProjectExecutionController.getListOfDevices().getBoolean(String.format("%02d", Address))){
									//ApplicationLauncher.logger.debug("BofaLduComReadConstantTestTask: getListOfDevices True entry");
									if(IsDeviceToBeRead(Integer.parseInt(lduPosition))){
										ApplicationLauncher.logger.debug("BofaLduComReadConstantTestTask : Reading LDU: "+lduPosition);
										if(!ProjectExecutionController.getUserAbortedFlag()){
											if(DeviceDataManagerController.getLDU_ReadDataFlag()){
												
													//bofaLduReadCreepData(Address); 
													bofaLduReadConstantTestData(Integer.parseInt(lduPosition)); 
													
												
											}else{
												ApplicationLauncher.logger.debug("BofaLduComReadConstantTestTask: getLDU_ReadDataFlag exit");
											}
										}else{
											ApplicationLauncher.logger.debug("BofaLduComReadConstantTestTask: UserAbortedFlag exit");
										}
									}
								//}
							}
						} catch (Exception e) {
							
							e.printStackTrace();
							ApplicationLauncher.logger.error("BofaLduComReadConstantTestTask: Exception:" + e.getMessage());
						}
					//	LDU_ComSemlock = true;
					//}
					
						
					//if(lduComSerialStatusConnected){
						try{
							//for repeatabality test check
							if(getSerialLDU_ComRefreshTimeInMsec() != SerialLDU_ComRefreshDefaultTimeInMsec){
								if(DisplayDataObj.getDevicesToBeRead().size()>0){
									if(!ProjectExecutionController.getUserAbortedFlag()){
										if(DeviceDataManagerController.getLDU_ReadDataFlag()){

											lduTimer.schedule(new BofaLduComReadConstantTestTask(), SerialLDU_ComRefreshDefaultTimeInMsec);//updated by gopi - for the refresh to read the device before exiting the LDUreadbuffertime
										}
									}else{
										ApplicationLauncher.logger.debug("BofaLduComReadConstantTestTask: Timer Exit4 !%n");
										lduTimer.cancel(); //Terminate the timer thread
									}
								}else{
									ApplicationLauncher.logger.debug("BofaLduComReadConstantTestTask: Timer Exit1 !%n");
									lduTimer.cancel(); //Terminate the timer thread
								}
							}
							else{
								if(!ProjectExecutionController.getUserAbortedFlag()){
									if(DeviceDataManagerController.getLDU_ReadDataFlag()){
										ApplicationLauncher.logger.debug("BofaLduComReadConstantTestTask: validating device to be read");
										if(DisplayDataObj.getDevicesToBeRead().size() !=0){
											ApplicationLauncher.logger.debug("BofaLduComReadConstantTestTask: Scheduling task again");
											lduTimer.schedule(new BofaLduComReadConstantTestTask(), getSerialLDU_ComRefreshTimeInMsec());
										}/*else{
											ApplicationLauncher.logger.debug("BofaLduComReadStaTask: send refresh command");
											lscsLDU_SendRefreshDataCommand();

										}*/
									}
								}else{
									ApplicationLauncher.logger.debug("BofaLduComReadConstantTestTask: Timer Exit3 !%n");
									lduTimer.cancel(); //Terminate the timer thread
								}
							}

						}
						catch(Exception e){
							e.printStackTrace();
							ApplicationLauncher.logger.error("BofaLduComReadConstantTestTask: Exception:" + e.getMessage());
							ApplicationLauncher.logger.info("BofaLduComReadConstantTestTask: lduTimer already Cancelled");

						}
					//}


				}
				else{

					ApplicationLauncher.logger.debug("BofaLduComReadConstantTestTask: Timer Exit2 !");
					lduTimer.cancel(); //Terminate the timer thread

				}
			}
		}
		
		

//=======================================================================================================================================================================================
		/*		public boolean bofaLduReadStaData(int LDU_ReadAddress){
			ApplicationHomeController.update_left_status("Reading LDU STA Data",ConstantApp.LEFT_STATUS_DEBUG);
			ApplicationLauncher.logger.debug("bofaLduReadStaData :Entry");
			boolean status=false;
			ApplicationLauncher.logger.info(ProjectExecutionController.getCurrentTestPointName()+":"+ProjectExecutionController.getCurrentTestPoint_Index()+" :bofaLduReadStaData :LDU_ReadAddress:"+LDU_ReadAddress);

			//sendReadErrorsCmd(LDU_ReadAddress) ;  //// here we need to send the command to read pulse count
			status = sendQueryEnablePulseCmd(LDU_ReadAddress); // here we need to send the command to read pulse count
			
			ApplicationLauncher.logger.debug("bofaLduReadStaData : LDU_ReadAddress : " + LDU_ReadAddress);
			ApplicationLauncher.logger.debug("bofaLduReadStaData : LDU Pulse Count : " + getStaTestPulseCount());
 	
			//LDU_SendCommandReadErrorData(LDU_ReadAddress);
			//lscsLDU_SendCommandReadSTA_Data(LDU_ReadAddress);
			//SerialDataLDU lduData = LDU_ReadData(ConstantCcubeLDU.CMD_LDU_STA_DATA_LENGTH,ConstantCcubeLDU.CMD_LDU_STA_DATA_ER);
			
			SerialDataLDU lduData = LDU_ReadData(ConstantLduLscs.CMD_LDU_STA_DATA_LENGTH,"");
			String resultStatus   = ConstantReport.RESULT_STATUS_UNDEFINED.trim();//ConstantLscsLDU.TO_BE_UPDATED;
			if(lduData.IsExpectedResponseReceived()){

				//lduData.LDU_DecodeSerialDataForSTA();

				lduData.setLDU_ResultStatus(resultStatus,LDU_ReadAddress);
				lduData.lscsLDU_DecodeSerialDataForCreepOrSTA(LDU_ReadAddress);
				resultStatus = lscsLduManipulateSTA_ResultStatus(lduData.getNoOfPulsesCounted(LDU_ReadAddress));
				lduData.setLDU_ResultStatus(resultStatus,LDU_ReadAddress);
				ProjectExecutionController.DeviceErrorDisplayUpdate(LDU_ReadAddress,lduData.getLDU_ResultStatus(LDU_ReadAddress),lduData.getNoOfPulsesCounted(LDU_ReadAddress));
				//StripLDU_SerialData(lduData.getReceivedLength());


							String ErrorValue = lduData.getLDU_ErrorValue(LDU_ReadAddress);
				if(ErrorValue.equals(ConstantCcubeLDU.CMD_LDU_ERROR_DATA_ER_WFR) || ErrorValue.equals(ConstantCcubeLDU.CMD_LDU_CREEP_DATA_ER_WFR)){
					ErrorValue = "WFR";
				}
				
				String noOfPulsesCounted = lduData.getNoOfPulsesCounted(LDU_ReadAddress);
			
				
				if(noOfPulsesCounted.equals(ConstantLduLscs.CMD_LDU_ERROR_DATA_ER_WFR) || noOfPulsesCounted.equals(ConstantLduCcube.CMD_LDU_CREEP_DATA_ER_WFR)){
					noOfPulsesCounted = "WFR";
				}
				int skip_reading = getSkipReadingFromJSON(LDU_ReadAddress, DisplayDataObj.get_NoOfPulseReadingToBeSkipped());
				if(!(noOfPulsesCounted.equals("WFR"))){
					if(skip_reading <= 0){
						//ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getLDU_ErrorValue(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_STA_TIME);
						//ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getLDU_ErrorValue(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
						//resultStatus = lscsLduManipulateSTA_ResultStatus(lduData.getNoOfPulsesCounted(LDU_ReadAddress));

						ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getNoOfPulsesCounted(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_STA_TIME);
						ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getNoOfPulsesCounted(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_PULSE_COUNT);
						ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getNoOfPulsesCounted(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);



						ArrayList<Integer> devices_to_be_read = DisplayDataObj.getDevicesToBeRead();
						devices_to_be_read = RemoveDeviceFromReadList(devices_to_be_read,LDU_ReadAddress );
						DisplayDataObj.setDevicesToBeRead(devices_to_be_read);
						ApplicationLauncher.logger.info("bofaLduReadStaData: devices_to_be_read:" + devices_to_be_read);
						ApplicationHomeController.updateBottomSecondaryStatus("LDU STA read status: "+ (ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK-DisplayDataObj.getDevicesToBeRead().size())+ "/" + ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK,ConstantApp.LEFT_STATUS_INFO);

						if(DisplayDataObj.getDevicesToBeRead().size() == 0){
							try{
								ApplicationLauncher.logger.info("bofaLduReadStaData: All Devices Completed" );
								ProjectExecutionController.setExecuteTimeCounter(0);
								lduTimer.cancel();
							} catch (Exception e) {
								
								e.printStackTrace();
								ApplicationLauncher.logger.error("bofaLduReadStaData :Exception:"+ e.getMessage());

							}
						}
					}
					else{
						ApplicationLauncher.logger.info("bofaLduReadStaData: noOfPulsesCounted: "  + noOfPulsesCounted);
						ApplicationLauncher.logger.info("bofaLduReadStaData: NoOfPulseReadingToBeSkipped: "  + skip_reading);
						DisplayDataObj.decrement_NoOfPulseReadingToBeSkipped(LDU_ReadAddress);
					}
				}
							else{
				}

				status=true;
			}
			else{
				ApplicationLauncher.logger.info("bofaLduReadStaData : No Data Received");
			}

			return status;

		}*/
//======================================================================================================================================


/*		public boolean bofaLduReadStaData(int LDU_ReadAddress){
			ApplicationHomeController.update_left_status("Reading LDU STA Data",ConstantApp.LEFT_STATUS_DEBUG);
			ApplicationLauncher.logger.debug("bofaLduReadStaData :Entry");
			boolean status=false;
			ApplicationLauncher.logger.info(ProjectExecutionController.getCurrentTestPointName()+":"+ProjectExecutionController.getCurrentTestPoint_Index()+" :bofaLduReadStaData :LDU_ReadAddress:"+LDU_ReadAddress);

 			status = sendQueryEnablePulseCmd(LDU_ReadAddress); // here we need to send the command to read pulse count
			
			int pulseCount = getStaTestPulseCount() ;
			
			ApplicationLauncher.logger.debug("bofaLduReadStaData : LDU_ReadAddress : " + LDU_ReadAddress);
			ApplicationLauncher.logger.debug("bofaLduReadStaData : LDU Pulse Count : " + pulseCount);
  
			ProjectExecutionController.DeviceErrorDisplayUpdate(LDU_ReadAddress,resultStatus, pulseCount);
			
			if(status){
				if(pulseCount < 1){ //minimum test pulse count
					
				 ApplicationLauncher.logger.debug("bofaLduReadStaData :   : " );
				}
				
			}

			return status;

		}*/
		
		public boolean bofaLduReadStaData(int LDU_ReadAddress){  
			ApplicationHomeController.update_left_status("Reading LDU STA Data",ConstantApp.LEFT_STATUS_DEBUG);
			ApplicationLauncher.logger.debug("bofaLduReadStaData :Entry");
			boolean status=false;
			ApplicationLauncher.logger.info(ProjectExecutionController.getCurrentTestPointName()+":"+ProjectExecutionController.getCurrentTestPoint_Index()+" :bofaLduReadStaData :LDU_ReadAddress:"+LDU_ReadAddress);

			status = sendQueryEnablePulseCmd(LDU_ReadAddress);
			
			//LDU_SendCommandReadErrorData(LDU_ReadAddress);
			//lscsLDU_SendCommandReadSTA_Data(LDU_ReadAddress);
			//SerialDataLDU lduData = LDU_ReadData(ConstantCcubeLDU.CMD_LDU_STA_DATA_LENGTH,ConstantCcubeLDU.CMD_LDU_STA_DATA_ER);
			//SerialDataLDU lduData = LDU_ReadData(ConstantLduLscs.CMD_LDU_STA_DATA_LENGTH,"");
			String resultStatus = ConstantReport.RESULT_STATUS_UNDEFINED.trim();//ConstantLscsLDU.TO_BE_UPDATED;
			//if(lduData.IsExpectedResponseReceived()){
			if(status){
				//lduData.LDU_DecodeSerialDataForSTA();

				//lduData.setLDU_ResultStatus(resultStatus,LDU_ReadAddress);
				//lduData.lscsLDU_DecodeSerialDataForCreepOrSTA(LDU_ReadAddress);
				resultStatus = bofaLduManipulateSTA_ResultStatus(String.valueOf(getStaCreepTestPulseCount().get(LDU_ReadAddress)) );//lduData.getNoOfPulsesCounted(LDU_ReadAddress));
				//lduData.setLDU_ResultStatus(resultStatus,LDU_ReadAddress);
				ProjectExecutionController.DeviceErrorDisplayUpdate(LDU_ReadAddress,resultStatus,String.valueOf(getStaCreepTestPulseCount().get(LDU_ReadAddress)));
				//StripLDU_SerialData(lduData.getReceivedLength());


				/*			String ErrorValue = lduData.getLDU_ErrorValue(LDU_ReadAddress);
				if(ErrorValue.equals(ConstantCcubeLDU.CMD_LDU_ERROR_DATA_ER_WFR) || ErrorValue.equals(ConstantCcubeLDU.CMD_LDU_CREEP_DATA_ER_WFR)){
					ErrorValue = "WFR";
				}*/
				String noOfPulsesCounted = String.valueOf(getStaCreepTestPulseCount().get(LDU_ReadAddress));//lduData.getNoOfPulsesCounted(LDU_ReadAddress);
				if(noOfPulsesCounted.equals(ConstantLduLscs.CMD_LDU_ERROR_DATA_ER_WFR) || noOfPulsesCounted.equals(ConstantLduCcube.CMD_LDU_CREEP_DATA_ER_WFR)){
					noOfPulsesCounted = "WFR";
				}
				int skip_reading = getSkipReadingFromJSON(LDU_ReadAddress, DisplayDataObj.get_NoOfPulseReadingToBeSkipped());
				if(!(noOfPulsesCounted.equals("WFR"))){
					//if(skip_reading <= 0){
						//ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getLDU_ErrorValue(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_STA_TIME);
						//ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getLDU_ErrorValue(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
						//resultStatus = lscsLduManipulateSTA_ResultStatus(lduData.getNoOfPulsesCounted(LDU_ReadAddress));

						ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, resultStatus, noOfPulsesCounted, ConstantReport.RESULT_DATA_TYPE_STA_TIME);
						ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, resultStatus, noOfPulsesCounted, ConstantReport.RESULT_DATA_TYPE_PULSE_COUNT);
						ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, resultStatus, noOfPulsesCounted, ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);



						ArrayList<Integer> devices_to_be_read = DisplayDataObj.getDevicesToBeRead();
						devices_to_be_read = RemoveDeviceFromReadList(devices_to_be_read,LDU_ReadAddress );
						DisplayDataObj.setDevicesToBeRead(devices_to_be_read);
						ApplicationLauncher.logger.info("bofaLduReadStaData: devices_to_be_read:" + devices_to_be_read);
						ApplicationHomeController.updateBottomSecondaryStatus("LDU STA read status: "+ (ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK-DisplayDataObj.getDevicesToBeRead().size())+ "/" + ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK,ConstantApp.LEFT_STATUS_INFO);
						
						//if(getNthOfErrors().get(LDU_ReadAddress)>=1){
						DisplayDataObj.getStepRunModeAtleastOneResultReadCompleted().set(LDU_ReadAddress, true);
						//}
						
						if(DisplayDataObj.getDevicesToBeRead().size() == 0){
							try{
								ApplicationLauncher.logger.info("bofaLduReadStaData: All Devices Completed" );
								ProjectExecutionController.deviceExecutionStatusDisplayUpdate(ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID, ConstantReport.RESULT_STATUS_PASS.trim(),ConstantApp.EXECUTION_STATUS_COMPLETED);//EXECUTION_STATUS_COMPLETED
								ProjectExecutionController.setExecuteTimeCounter(0);
								lduTimer.cancel();
							} catch (Exception e) {
								
								e.printStackTrace();
								ApplicationLauncher.logger.error("bofaLduReadStaData :Exception:"+ e.getMessage());

							}
						}
					//}
					/*else{
						ApplicationLauncher.logger.info("bofaLduReadStaData: noOfPulsesCounted: "  + noOfPulsesCounted);
						ApplicationLauncher.logger.info("bofaLduReadStaData: NoOfPulseReadingToBeSkipped: "  + skip_reading);
						DisplayDataObj.decrement_NoOfPulseReadingToBeSkipped(LDU_ReadAddress);
					}*/
				}
				/*			else{
				}*/

				status=true;
			}
			else{
				ApplicationLauncher.logger.info("bofaLduReadStaData : No Data Received");
			}

			return status;

		}
//========================================================================================================================
		public boolean bofaLduReadCreepData(int LDU_ReadAddress){  
			ApplicationHomeController.update_left_status("Reading LDU CRP Data",ConstantApp.LEFT_STATUS_DEBUG);
			ApplicationLauncher.logger.debug("bofaLduReadCreepData :Entry");
			boolean status=false;
			ApplicationLauncher.logger.info(ProjectExecutionController.getCurrentTestPointName()+":"+ProjectExecutionController.getCurrentTestPoint_Index()+" :bofaLduReadCreepData :LDU_ReadAddress:"+LDU_ReadAddress);

			status = sendQueryEnablePulseCmd(LDU_ReadAddress);
			
			//LDU_SendCommandReadErrorData(LDU_ReadAddress);
			//lscsLDU_SendCommandReadSTA_Data(LDU_ReadAddress);
			//SerialDataLDU lduData = LDU_ReadData(ConstantCcubeLDU.CMD_LDU_STA_DATA_LENGTH,ConstantCcubeLDU.CMD_LDU_STA_DATA_ER);
			//SerialDataLDU lduData = LDU_ReadData(ConstantLduLscs.CMD_LDU_STA_DATA_LENGTH,"");
			String resultStatus = ConstantReport.RESULT_STATUS_UNDEFINED.trim();//ConstantLscsLDU.TO_BE_UPDATED;
			//if(lduData.IsExpectedResponseReceived()){
			if(status){
				//lduData.LDU_DecodeSerialDataForSTA();

				//lduData.setLDU_ResultStatus(resultStatus,LDU_ReadAddress);
				//lduData.lscsLDU_DecodeSerialDataForCreepOrSTA(LDU_ReadAddress);
				//resultStatus = lscsLduManipulateSTA_ResultStatus(String.valueOf(getStaCreepTestPulseCount().get(LDU_ReadAddress)) );//lduData.getNoOfPulsesCounted(LDU_ReadAddress));
				
				
				resultStatus = bofaLduManipulateCreepResultStatus(String.valueOf(getStaCreepTestPulseCount().get(LDU_ReadAddress)) );
				
				//resultStatus = ConstantReport.RESULT_STATUS_UNDEFINED.trim();
				//lduData.setLDU_ResultStatus(resultStatus,LDU_ReadAddress);
				ProjectExecutionController.DeviceErrorDisplayUpdate(LDU_ReadAddress,resultStatus,String.valueOf(getStaCreepTestPulseCount().get(LDU_ReadAddress)));
				//StripLDU_SerialData(lduData.getReceivedLength());


				/*			String ErrorValue = lduData.getLDU_ErrorValue(LDU_ReadAddress);
				if(ErrorValue.equals(ConstantCcubeLDU.CMD_LDU_ERROR_DATA_ER_WFR) || ErrorValue.equals(ConstantCcubeLDU.CMD_LDU_CREEP_DATA_ER_WFR)){
					ErrorValue = "WFR";
				}*/
				String noOfPulsesCounted = String.valueOf(getStaCreepTestPulseCount().get(LDU_ReadAddress));//lduData.getNoOfPulsesCounted(LDU_ReadAddress);
				if(noOfPulsesCounted.equals(ConstantLduLscs.CMD_LDU_ERROR_DATA_ER_WFR) || noOfPulsesCounted.equals(ConstantLduCcube.CMD_LDU_CREEP_DATA_ER_WFR)){
					noOfPulsesCounted = "WFR";
				}
				int skip_reading = getSkipReadingFromJSON(LDU_ReadAddress, DisplayDataObj.get_NoOfPulseReadingToBeSkipped());
				if(!(noOfPulsesCounted.equals("WFR"))){
					//if(skip_reading <= 0){
						//ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getLDU_ErrorValue(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_STA_TIME);
						//ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getLDU_ErrorValue(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
						//resultStatus = lscsLduManipulateSTA_ResultStatus(lduData.getNoOfPulsesCounted(LDU_ReadAddress));

						ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, resultStatus, noOfPulsesCounted, ConstantReport.RESULT_DATA_TYPE_STA_TIME);
						ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, resultStatus, noOfPulsesCounted, ConstantReport.RESULT_DATA_TYPE_PULSE_COUNT);
						ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, resultStatus, noOfPulsesCounted, ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);



						ArrayList<Integer> devices_to_be_read = DisplayDataObj.getDevicesToBeRead();
						devices_to_be_read = RemoveDeviceFromReadList(devices_to_be_read,LDU_ReadAddress );
						DisplayDataObj.setDevicesToBeRead(devices_to_be_read);
						ApplicationLauncher.logger.info("bofaLduReadCreepData: devices_to_be_read:" + devices_to_be_read);
						ApplicationHomeController.updateBottomSecondaryStatus("LDU STA read status: "+ (ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK-DisplayDataObj.getDevicesToBeRead().size())+ "/" + ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK,ConstantApp.LEFT_STATUS_INFO);
						//if(getNthOfErrors().get(LDU_ReadAddress)>=1){
						DisplayDataObj.getStepRunModeAtleastOneResultReadCompleted().set(LDU_ReadAddress, true);
						//}
						if(DisplayDataObj.getDevicesToBeRead().size() == 0){
							try{
								ApplicationLauncher.logger.info("bofaLduReadCreepData: All Devices Completed" );
								ProjectExecutionController.deviceExecutionStatusDisplayUpdate(ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID, ConstantReport.RESULT_STATUS_PASS.trim(),ConstantApp.EXECUTION_STATUS_COMPLETED);//EXECUTION_STATUS_COMPLETED
								ProjectExecutionController.setExecuteTimeCounter(0);
								lduTimer.cancel();
							} catch (Exception e) {
								
								e.printStackTrace();
								ApplicationLauncher.logger.error("bofaLduReadCreepData :Exception:"+ e.getMessage());

							}
						}
					//}
					/*else{
						ApplicationLauncher.logger.info("bofaLduReadStaData: noOfPulsesCounted: "  + noOfPulsesCounted);
						ApplicationLauncher.logger.info("bofaLduReadStaData: NoOfPulseReadingToBeSkipped: "  + skip_reading);
						DisplayDataObj.decrement_NoOfPulseReadingToBeSkipped(LDU_ReadAddress);
					}*/
				}
				/*			else{
				}*/

				status=true;
			}
			else{
				ApplicationLauncher.logger.info("bofaLduReadCreepData : No Data Received");
			}

			return status;

		}
//===============================================================================================================
		
		public String bofaLduManipulateCreepResultStatus(String actualPulseCount) {
			//String ratioErrorActual = "";
			//String acceptableErrorMax = "";
			//String acceptableErrorMin = "";
			int maximumAcceptablePulseCount  = 0;
			String pass = ConstantReport.RESULT_STATUS_PASS.trim();//"P";
			String fail = ConstantReport.RESULT_STATUS_FAIL.trim();//"N";;
			String undefined = ConstantReport.RESULT_STATUS_UNDEFINED.trim();//"N";;
			String result = undefined;
			try {
				maximumAcceptablePulseCount = Integer.parseInt(DisplayDataObj.getCreepNoOfPulses());////DisplayDataObj.getRatioErrorMin();
				//acceptableErrorMax = DisplayDataObj.get_Error_max();//DisplayDataObj.getRatioErrorMax();
				ApplicationLauncher.logger.info("bofaLduManipulateCreepResultStatus : actualPulseCount: " + actualPulseCount);
				ApplicationLauncher.logger.info("bofaLduManipulateCreepResultStatus: maximumAcceptablePulseCount: " + maximumAcceptablePulseCount);
				//ApplicationLauncher.logger.info("lscsLduManipulateErrorResultStatus: acceptableErrorMax: " + acceptableErrorMax);

				if(!actualPulseCount.isEmpty()){
					if ( Integer.parseInt(actualPulseCount) <= maximumAcceptablePulseCount ) {
						//if (Float.parseFloat(actualPulseCount) <= (Float.parseFloat(acceptableErrorMax))) {
						result = pass;
						ApplicationLauncher.logger.info("bofaLduManipulateCreepResultStatus: :result: " + result);
						//}
					}else {
						if(!actualPulseCount.contains(ConstantLduLscs.CMD_LDU_ERROR_DATA_ER_WFR)){
							result = fail;
						}
						//result = fail;
					}
				}
				//DeviceErrorDisplayUpdate(ConstantProGEN_App.LIVE_TABLE_RATIO_ERROR_COLUMN_ID, result, ratioErrorActual);

			} catch (Exception e) {
				e.printStackTrace();
				ApplicationLauncher.logger.error("bofaLduManipulateCreepResultStatus: Exception: " + e.getMessage());

			}

			//DisplayDataObj.selectedResultData.setRatioError(ratioErrorActual);
			//DisplayDataObj.selectedResultData.setRatioErrorStatus(result);
			return result;
		}
		
		public boolean bofaLduReadConstantTestData(int LDU_ReadAddress){  
			ApplicationHomeController.update_left_status("Reading LDU Constant Data",ConstantApp.LEFT_STATUS_DEBUG);
			ApplicationLauncher.logger.debug("bofaLduReadConstantTestData :Entry");
			boolean status=false;
			ApplicationLauncher.logger.info(ProjectExecutionController.getCurrentTestPointName()+":"+ProjectExecutionController.getCurrentTestPoint_Index()+" :bofaLduReadConstantTestData :LDU_ReadAddress:"+LDU_ReadAddress);

			status = sendReadDialPulsesCmd(LDU_ReadAddress);//sendQueryEnablePulseCmd(LDU_ReadAddress);
			
			//LDU_SendCommandReadErrorData(LDU_ReadAddress);
			//lscsLDU_SendCommandReadSTA_Data(LDU_ReadAddress);
			//SerialDataLDU lduData = LDU_ReadData(ConstantCcubeLDU.CMD_LDU_STA_DATA_LENGTH,ConstantCcubeLDU.CMD_LDU_STA_DATA_ER);
			//SerialDataLDU lduData = LDU_ReadData(ConstantLduLscs.CMD_LDU_STA_DATA_LENGTH,"");
			String resultStatus = ConstantReport.RESULT_STATUS_UNDEFINED.trim();//ConstantLscsLDU.TO_BE_UPDATED;
			//if(lduData.IsExpectedResponseReceived()){
			if(status){
				//lduData.LDU_DecodeSerialDataForSTA();

				
				//resultStatus = bofaLduManipulateSTA_ResultStatus(getDialTestPulseCount().get(LDU_ReadAddress) );//lduData.getNoOfPulsesCounted(LDU_ReadAddress));
				resultStatus = resultStatus = ConstantReport.RESULT_STATUS_UNDEFINED.trim();
				//lduData.setLDU_ResultStatus(resultStatus,LDU_ReadAddress);
				ProjectExecutionController.DeviceErrorDisplayUpdate(LDU_ReadAddress,resultStatus,getDialTestPulseCount().get(LDU_ReadAddress));
				//StripLDU_SerialData(lduData.getReceivedLength());


				/*			String ErrorValue = lduData.getLDU_ErrorValue(LDU_ReadAddress);
				if(ErrorValue.equals(ConstantCcubeLDU.CMD_LDU_ERROR_DATA_ER_WFR) || ErrorValue.equals(ConstantCcubeLDU.CMD_LDU_CREEP_DATA_ER_WFR)){
					ErrorValue = "WFR";
				}*/
				String noOfPulsesCounted = String.valueOf(getDialTestPulseCount().get(LDU_ReadAddress));//lduData.getNoOfPulsesCounted(LDU_ReadAddress);
				if(noOfPulsesCounted.equals(ConstantLduLscs.CMD_LDU_ERROR_DATA_ER_WFR) || noOfPulsesCounted.equals(ConstantLduCcube.CMD_LDU_CREEP_DATA_ER_WFR)){
					noOfPulsesCounted = "WFR";
				}
				int skip_reading = getSkipReadingFromJSON(LDU_ReadAddress, DisplayDataObj.get_NoOfPulseReadingToBeSkipped());
				if(!(noOfPulsesCounted.equals("WFR"))){
					//if(skip_reading <= 0){
						//ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getLDU_ErrorValue(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_STA_TIME);
						//ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, lduData.getLDU_ResultStatus(LDU_ReadAddress), lduData.getLDU_ErrorValue(LDU_ReadAddress), ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
						//resultStatus = lscsLduManipulateSTA_ResultStatus(lduData.getNoOfPulsesCounted(LDU_ReadAddress));

						//ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, resultStatus, noOfPulsesCounted, ConstantReport.RESULT_DATA_TYPE_STA_TIME);
						ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, resultStatus, noOfPulsesCounted, ConstantReport.RESULT_DATA_TYPE_PULSE_COUNT);
						ProjectExecutionController.UpdateDB_DeviceLDU_ErrorData( LDU_ReadAddress, resultStatus, noOfPulsesCounted, ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);



						ArrayList<Integer> devices_to_be_read = DisplayDataObj.getDevicesToBeRead();
						devices_to_be_read = RemoveDeviceFromReadList(devices_to_be_read,LDU_ReadAddress );
						DisplayDataObj.setDevicesToBeRead(devices_to_be_read);
						ApplicationLauncher.logger.info("bofaLduReadConstantTestData: devices_to_be_read:" + devices_to_be_read);
						ApplicationHomeController.updateBottomSecondaryStatus("LDU Constant read status: "+ (ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK-DisplayDataObj.getDevicesToBeRead().size())+ "/" + ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK,ConstantApp.LEFT_STATUS_INFO);
						//if(getNthOfErrors().get(LDU_ReadAddress)>=1){
						DisplayDataObj.getStepRunModeAtleastOneResultReadCompleted().set(LDU_ReadAddress, true);
						//}
						if(DisplayDataObj.getDevicesToBeRead().size() == 0){
							try{
								ApplicationLauncher.logger.info("bofaLduReadConstantTestData: All Devices Completed" );
								ProjectExecutionController.deviceExecutionStatusDisplayUpdate(ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID, ConstantReport.RESULT_STATUS_PASS.trim(),ConstantApp.EXECUTION_STATUS_COMPLETED);//EXECUTION_STATUS_COMPLETED
								ProjectExecutionController.setExecuteTimeCounter(0);
								lduTimer.cancel();
							} catch (Exception e) {
								
								e.printStackTrace();
								ApplicationLauncher.logger.error("bofaLduReadConstantTestData :Exception:"+ e.getMessage());

							}
						}
					//}
					/*else{
						ApplicationLauncher.logger.info("bofaLduReadStaData: noOfPulsesCounted: "  + noOfPulsesCounted);
						ApplicationLauncher.logger.info("bofaLduReadStaData: NoOfPulseReadingToBeSkipped: "  + skip_reading);
						DisplayDataObj.decrement_NoOfPulseReadingToBeSkipped(LDU_ReadAddress);
					}*/
				}
				/*			else{
				}*/

				status=true;
			}
			else{
				ApplicationLauncher.logger.info("bofaLduReadConstantTestData : No Data Received");
			}

			return status;

		}
		
/*public static void StripLDU_SerialData(Integer length){
	ApplicationLauncher.logger.debug("StripLDU_SerialData :Entry");
	Communicator SerialPortObj =commLDU;
	SerialPortObj.StripLength(length);
	ApplicationLauncher.logger.debug("StripLDU_SerialData : getSerialData :"+SerialPortObj.getSerialData());
}*/

/*
public static boolean getSkipCurrentTP_Execution(){
	//ApplicationLauncher.logger.debug("getSkipCurrentTP_Execution :Entry");
	return SkipCurrentTP_Execution;
}	*/	
//===============================================================================================================
	
		public static int getSkipReadingFromJSON(int rack_id, JSONObject skip_reading){
			ApplicationLauncher.logger.debug("getSkipReadingFromJSON : Entry :");
			int skip_count =0;
			try {
				skip_count = skip_reading.getInt(Integer.toString(rack_id));
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("getSkipReadingFromJSON : JSONException :"+e.getMessage());
			}
			return skip_count;
		}
		
		//===============================================================================================================
	 
		
		public static ArrayList<Integer> RemoveDeviceFromReadList (ArrayList<Integer> devices, int Address){
			ArrayList<Integer> read_devices = new ArrayList<Integer>();
			for(int i=0; i<devices.size(); i++){
				if(Address != devices.get(i)){
					read_devices.add(devices.get(i));
				}
			}
			return read_devices;
		}
		
		//===============================================================================================================
		
		public static boolean isTimeExtendedForTimeBased(){
			return TimeExtendedForTimeBased;
		}
		//===============================================================================================================

		public static void setTimeExtendedForTimeBased(boolean value){
			TimeExtendedForTimeBased = value;
		}
		
		//===============================================================================================================
	
		public static void Sleep(int timeInMsec) {

			try {
				Thread.sleep(timeInMsec);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("Sleep :InterruptedException:"+ e.getMessage());
			}

		}
		
		
/*		public static SerialDataLDU LDU_ReadData(int Expectedlength,String ExpectedResponse){
			ApplicationLauncher.logger.debug("LDU_ReadData :Entry");
			Communicator SerialPortObj =commLDU;

			SerialPortObj.setExpectedLength(Expectedlength);
			SerialPortObj.setExpectedResult(ExpectedResponse);

			ApplicationLauncher.logger.debug("LDU_ReadData: setExpectedResult:"+SerialPortObj.getExpectedResult());
			ApplicationLauncher.logger.debug("LDU_ReadData: setExpectedLength:"+SerialPortObj.getExpectedLength());
			SerialDataLDU lduData = new SerialDataLDU(SerialPortObj);
			lduData.SerialReponseTimerStart(30);
			SerialPortObj = null;//garbagecollector
			return lduData;
		}*/
		
 
		public static String lscsLduManipulateErrorResultStatus(String actualError) {
			//String ratioErrorActual = ""; 
			String acceptableErrorMax = "";
			String acceptableErrorMin = "";
			String pass = ConstantReport.RESULT_STATUS_PASS.trim();//"P";
			String fail = ConstantReport.RESULT_STATUS_FAIL.trim();//"N";;
			String undefined = ConstantReport.RESULT_STATUS_UNDEFINED.trim();//"N";;
			String result = undefined;
			try {
				acceptableErrorMin = DisplayDataObj.get_Error_min();////DisplayDataObj.getRatioErrorMin();
				acceptableErrorMax = DisplayDataObj.get_Error_max();//DisplayDataObj.getRatioErrorMax();
				ApplicationLauncher.logger.info("lscsLduManipulateErrorResultStatus : actualError: " + actualError);
				//ApplicationLauncher.logger.info("lscsLduManipulateErrorResultStatus: acceptableErrorMin: " + acceptableErrorMin);
				//ApplicationLauncher.logger.info("lscsLduManipulateErrorResultStatus: acceptableErrorMax: " + acceptableErrorMax);

				if(!actualError.isEmpty()){
					if ((Float.parseFloat(acceptableErrorMin)) <= Float.parseFloat(actualError)) {
						if (Float.parseFloat(actualError) <= (Float.parseFloat(acceptableErrorMax))) {
							result = pass;
							ApplicationLauncher.logger.info("lscsLduManipulateErrorResultStatus: :result: " + result);
						}else {
							if(!actualError.contains(ConstantPowerSourceBofa.BOFA_LDU_WFR)){
								result = fail;
							}
						}
					}else {
						result = fail;
					}
				}
				//DeviceErrorDisplayUpdate(ConstantProGEN_App.LIVE_TABLE_RATIO_ERROR_COLUMN_ID, result, ratioErrorActual);

			} catch (Exception e) {
				e.printStackTrace();
				ApplicationLauncher.logger.error("lscsLduManipulateErrorResultStatus: Exception: " + e.getMessage());
			}

			//DisplayDataObj.selectedResultData.setRatioError(ratioErrorActual);
			//DisplayDataObj.selectedResultData.setRatioErrorStatus(result);
			return result;
		}
		
		public String bofaLduManipulateSTA_ResultStatus(String actualPulseCount) {
			//String ratioErrorActual = "";
			//String acceptableErrorMax = "";
			//String acceptableErrorMin = "";
			int minimumExpectedPulseCount  = 0;
			String pass = ConstantReport.RESULT_STATUS_PASS.trim();//"P";
			String fail = ConstantReport.RESULT_STATUS_FAIL.trim();//"N";;
			String undefined = ConstantReport.RESULT_STATUS_UNDEFINED.trim();//"N";;
			String result = undefined;
			try {
				minimumExpectedPulseCount = Integer.parseInt(DisplayDataObj.getSTANoOfPulses());////DisplayDataObj.getRatioErrorMin();
				//acceptableErrorMax = DisplayDataObj.get_Error_max();//DisplayDataObj.getRatioErrorMax();
				ApplicationLauncher.logger.info("bofaLduManipulateSTA_ResultStatus : actualPulseCount: " + actualPulseCount);
				ApplicationLauncher.logger.info("bofaLduManipulateSTA_ResultStatus: minimumExpectedPulseCount: " + minimumExpectedPulseCount);
				//ApplicationLauncher.logger.info("lscsLduManipulateErrorResultStatus: acceptableErrorMax: " + acceptableErrorMax);

				if(!actualPulseCount.isEmpty()){
					if ( Integer.parseInt(actualPulseCount) >= minimumExpectedPulseCount ) {
						//if (Float.parseFloat(actualPulseCount) <= (Float.parseFloat(acceptableErrorMax))) {
						result = pass;
						ApplicationLauncher.logger.info("bofaLduManipulateSTA_ResultStatus: :result: " + result);
						//}
					}else {

						if(!actualPulseCount.contains(ConstantLduLscs.CMD_LDU_ERROR_DATA_ER_WFR)){
							result = fail;
						}
						//result = fail;
					}
				}
				//DeviceErrorDisplayUpdate(ConstantProGEN_App.LIVE_TABLE_RATIO_ERROR_COLUMN_ID, result, ratioErrorActual);

			} catch (Exception e) {
				e.printStackTrace();
				ApplicationLauncher.logger.error("bofaLduManipulateSTA_ResultStatus: Exception: " + e.getMessage());
			}

			//DisplayDataObj.selectedResultData.setRatioError(ratioErrorActual);
			//DisplayDataObj.selectedResultData.setRatioErrorStatus(result);
			return result;
		}
		
		

//===============================================================================================================

		public static String getNumberOfTurns() {
		return numberOfTurns;
	}
	public static String getTheoreticalNumberOfPulses() {
		return theoreticalNumberOfPulses;
	}
	public static String getPulseRatio() {
		return pulseRatio;
	}
	public static String getMinuteOfCurrentTime() {
		return minuteOfCurrentTime;
	}
	public static String getSecondOfCurrentTime() {
		return secondOfCurrentTime;
	}
	public static String getChannelNumber() {
		return channelNumber;
	}
	public static void setChannelNumber(String channelNumber) {
		Data_LduBofa.channelNumber = channelNumber;
	}
	public static void setNumberOfTurns(String numberOfTurns) {
		Data_LduBofa.numberOfTurns = numberOfTurns;
	}
	public static void setTheoreticalNumberOfPulses(String theoreticalNumberOfPulses) {
		Data_LduBofa.theoreticalNumberOfPulses = theoreticalNumberOfPulses;
	}
	
	public static void calculateTheoritcalNoOfPulses(long liveRefMeterConstant, float meterConstant, int numberOfPulse){
		
		ApplicationLauncher.logger.info("calculateTheoritcalNoOfPulses: liveRefMeterConstant : " + liveRefMeterConstant  );
        ApplicationLauncher.logger.info("calculateTheoritcalNoOfPulses: meterConstant        : " + meterConstant  );
       // ApplicationLauncher.logger.info("calculateTheoritcalNoOfPulses: liveRefMeterConstant/meterConstant        : " + ((int)(liveRefMeterConstant/meterConstant))*numberOfPulse );
        ApplicationLauncher.logger.info("calculateTheoritcalNoOfPulses: numberOfPulse        : " + numberOfPulse );
        float RssPulsePerMutPulseFlt = ( ((float)liveRefMeterConstant) / meterConstant)* ( (float)numberOfPulse); // updated in version s4.2.0.9.0.3
        ApplicationLauncher.logger.info("calculateTheoritcalNoOfPulses: RssPulsePerMutPulseFlt        : " + RssPulsePerMutPulseFlt );
        //String tempValue =         String.valueOf ((((int)(liveRefMeterConstant/meterConstant))*numberOfPulse))  ;
        String tempValue =         String.valueOf ((int)(RssPulsePerMutPulseFlt))  ;
        ApplicationLauncher.logger.info("calculateTheoritcalNoOfPulses: tempValue1        : " + tempValue );
        
        tempValue = DeviceDataManagerController.theoriticalPulseFormatter.format(Integer.parseInt(tempValue));
        ApplicationLauncher.logger.info("calculateTheoritcalNoOfPulses: theoriticalPulseFormatter        : " + tempValue );
        String hexValue = BofaManager.asciiToHex(tempValue);
        ApplicationLauncher.logger.info("calculateTheoritcalNoOfPulses: theoriticalPulseFormatter: hexValue        : " + hexValue );
        Data_LduBofa.theoreticalNumberOfPulses  = hexValue;;
      
	}
	
	public static void setPulseRatio(String pulseRatio) {
		Data_LduBofa.pulseRatio = pulseRatio;
	}
	public static void setMinuteOfCurrentTime(String minuteOfCurrentTime) {
		Data_LduBofa.minuteOfCurrentTime = minuteOfCurrentTime;
	}
	public static void setSecondOfCurrentTime(String secondOfCurrentTime) {
		Data_LduBofa.secondOfCurrentTime = secondOfCurrentTime;
	}
	
/*	public static int getNthOfErrors() {
		return nthOfErrors;
	}
	public static String getErrorSymbol() {
		return errorSymbol;
	}
	public static String getErrorValue() {
		return errorValue;
	}
	public static String getDialPulseCount() {
		return dialPulseCount;
	}*/
	
	
	public static String getReferenceMeterPulseNumber() {
		return referenceMeterPulseNumber;
	}
/*	public static int getStaTestPulseCount() {
		return staTestPulseCount;
	}

	public static void setStaTestPulseCount(int staTestPulseCount) {
		Data_LduBofa.staTestPulseCount = staTestPulseCount;
	}

	public static void setNthOfErrors(int nthOfErrors) {
		Data_LduBofa.nthOfErrors = nthOfErrors;
	}*/
	public static void setErrorSymbol(String errorSymbol) {
		Data_LduBofa.errorSymbol = errorSymbol;
	}
/*	public static void setErrorValue(String error) {
		Data_LduBofa.errorValue = error;
	}
	public static void setDialPulseCount(String dialPulseCount) {
		Data_LduBofa.dialPulseCount = dialPulseCount;
	}*/
	public static void setReferenceMeterPulseNumber(String referenceMeterPulseNumber) {
		Data_LduBofa.referenceMeterPulseNumber = referenceMeterPulseNumber;
	}
	
	public static void setSerialLDU_ComRefreshTimeInMsec(int TimeInMSec){
		SerialLDU_ComRefreshTimeInMsec = TimeInMSec;

	}

	public static void resetSerialLDU_ComRefreshTimeInMsec(){
		SerialLDU_ComRefreshTimeInMsec = SerialLDU_ComRefreshDefaultTimeInMsec;

	}

	public static int getSerialLDU_ComRefreshTimeInMsec(){
		return SerialLDU_ComRefreshTimeInMsec ;

	}

	public static void setErrorValue(ArrayList<String> errorValue) {
		Data_LduBofa.errorValue = errorValue;
	}
	
	public static ArrayList<String> getErrorValue() {
		return Data_LduBofa.errorValue;
	}

	public static void resetErrorValue() {
		
		Data_LduBofa.errorValue.clear();
		for(int i=0; i<=48; i++) {
			Data_LduBofa.errorValue.add("");
		}
		
		
	}

	public static ArrayList<Integer> getNthOfErrors() {
		return nthOfErrors;
	}

	public static void setNthOfErrors(ArrayList<Integer> nthOfErrors) {
		Data_LduBofa.nthOfErrors = nthOfErrors;
	}
	
	public static void resetNthOfErrors() {
		
		Data_LduBofa.nthOfErrors.clear();
		for(int i=0; i<=48; i++) {
			Data_LduBofa.nthOfErrors.add(0);
		}
		
		
	}

	public static ArrayList<String> getDialTestPulseCount() {
		return dialTestPulseCount;
	}

	public static void setDialTestPulseCount(ArrayList<String> dialPulseCount) {
		Data_LduBofa.dialTestPulseCount = dialPulseCount;
	}
	
	public static void resetDialTestPulseCount() {
		
		Data_LduBofa.dialTestPulseCount.clear();
		for(int i=0; i<=48; i++) {
			Data_LduBofa.dialTestPulseCount.add("");
		}
		
		
	}

	public static ArrayList<Integer> getStaCreepTestPulseCount() {
		return staCreeptTestPulseCount;
	}

	public static void setStaCreepTestPulseCount(ArrayList<Integer> staTestPulseCount) {
		Data_LduBofa.staCreeptTestPulseCount = staTestPulseCount;
	}
	
	public static void resetStaCreepTestPulseCount() {
		
		Data_LduBofa.staCreeptTestPulseCount.clear();
		for(int i=0; i<=48; i++) {
			Data_LduBofa.staCreeptTestPulseCount.add(0);
		}
		
		
	}

	


//========================================================================	
}
