package com.tasnetwork.calibration.energymeter.message;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdSands;
import com.tasnetwork.calibration.energymeter.device.Data_RefStdSands;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;

public class RefStdSandsMessage {
	
	
	public static String MSG_READ_TOTAL_POWER_INSTANT_ANGLE = ConstantRefStdSands.CMD_TOTAL_POWER_INSTANT_ANGLE_HDR;
	public static String MSG_READ_R_PHASE = ConstantRefStdSands.CMD_READ_R_PHASE_HDR;
	public static String MSG_READ_Y_PHASE = ConstantRefStdSands.CMD_READ_Y_PHASE_HDR;
	public static String MSG_READ_B_PHASE = ConstantRefStdSands.CMD_READ_B_PHASE_HDR;
	public static String MSG_GET_CONFIG = ConstantRefStdSands.CMD_GET_CONFIG_HDR;
	public static String MSG_SET_CONFIG = ConstantRefStdSands.CMD_SET_CONFIG_HDR;
	public static String MSG_ACCU_RESET = ConstantRefStdSands.CMD_ACCU_RESET_HDR;
	public static String MSG_ACCU_START = ConstantRefStdSands.CMD_ACCU_START_HDR;
	public static String MSG_ACCU_STOP = ConstantRefStdSands.CMD_ACCU_STOP_HDR;
	public static String MSG_READ_ACCU_ENERGY = ConstantRefStdSands.CMD_READ_ACCU_ENERGY_HDR;
	
	public static String msgSetConfigData = "";
	
	
	public static String getMsgSetConfigData() {
		String data = ConstantRefStdSands.CMD_SET_CONFIG_DATA_HDR + Data_RefStdSands.getWriteConfigModeVoltage() + 
				Data_RefStdSands.getWriteConfigModeCurrent()+Data_RefStdSands.getWriteConfigModePulseOutputUnit();
		String checkSum = GuiUtils.generateXorCheckSum(data);
		ApplicationLauncher.logger.debug("getMsgSetConfigData: data :" + data );
		ApplicationLauncher.logger.debug("getMsgSetConfigData: checkSum :" + checkSum );
		msgSetConfigData = data + checkSum;
		ApplicationLauncher.logger.debug("getMsgSetConfigData: msgSetConfigData :" + msgSetConfigData );
		return msgSetConfigData;
	}
	


/*	public static String msgGetConfig = "";
	public static String msgSetConfig = "";
	
	public static String getMsgGetConfig() {
		msgGetConfig = ConstantSandsRefStd.CMD_GET_CONFIG_HDR+ ;
		return msgGetConfig;
	}
	public static void setMsgGetConfig(String msgGetConfig) {
		RefStdSandsMessage.msgGetConfig = msgGetConfig;
	}

	*/
}
