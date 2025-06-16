package com.tasnetwork.calibration.energymeter.message;


import com.tasnetwork.calibration.energymeter.constant.Constant_ICT;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;

public class IctKreMessage {

	//public static String MSG_CMD_RESET = Constant_ICT.CMD_RESET_HDR ;
	
/*	public static String commandReset = "";

	public static String getCommandReset(String ictId) {
		
		commandReset = Constant_ICT.CMD_RESET_HDR + Constant_ICT.CMD_ICT_SEPERATOR + 
						GUIUtils.StringToHex(ictId) +  Constant_ICT.CMD_TERMINATOR;
		return commandReset;
	}*/

	public static String commandResetState = "";
	
	public static String commandInquireState = "";
	
	public static String commandShort = "";

	public static String getCommandResetState(String ictId) {
		
		commandResetState = Constant_ICT.CMD_RESET_STATE_HDR + Constant_ICT.CMD_ICT_SEPERATOR + 
						GuiUtils.StringToHex(ictId) +  Constant_ICT.CMD_TERMINATOR;
		return commandResetState;
	}
	
	public static String getCommandInquireState(String ictId) {
		
		commandInquireState = Constant_ICT.CMD_INQUIRE_STATE_HDR + Constant_ICT.CMD_ICT_SEPERATOR + 
						GuiUtils.StringToHex(ictId) +  Constant_ICT.CMD_TERMINATOR;
		return commandInquireState;
	}
	
	public static String getCommandShort(String ictId) {
		
		//String ictValue
		commandShort = Constant_ICT.CMD_SHORT_HDR + Constant_ICT.CMD_ICT_SEPERATOR + 
				GuiUtils.StringToHex(ictId) +  Constant_ICT.CMD_TERMINATOR;
		return commandShort;
	}

	
}
