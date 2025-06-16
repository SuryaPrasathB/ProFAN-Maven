package com.tasnetwork.calibration.energymeter.device;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdKre;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;

public class Data_RefStdKre {

	static DeviceDataManagerController DisplayDataObj =  new DeviceDataManagerController();
	
	public static String REPORT_DATE_TIME_FORMAT = "yyyy-M-d HH:mm:ss";
	
	public static String commandWriteSettingParameter = "";
	
	public static String writeSettingIstall = GuiUtils.StringToHex("1.00");
	public static String writeSettingWiringMode = ConstantRefStdKre.SETTING_DATA_WIRING_MODE_3P4W;
	public static String writeSettingOutput1PulseConstant = GuiUtils.StringToHex("12345");
	public static String writeSettingOutput2PulseConstant = GuiUtils.StringToHex("23456");
	
	public static String writeSettingOutput1PulseType = ConstantRefStdKre.SETTING_DATA_OUTPUT_PULSE_TYPE_ACTIVE;
	public static String writeSettingOutput2PulseType = ConstantRefStdKre.SETTING_DATA_OUTPUT_PULSE_TYPE_REACTIVE;
	
	public static String writeSettingDateTime = GuiUtils.StringToHex("2021-12-26 16:42:01");
	
	public static String lastWriteSettingIstall = "";
	public static String lastWriteSettingWiringMode = "";
	public static String lastWriteSettingOutput1PulseConstant = "";
	public static String lastWriteSettingOutput1PulseType = "";
	
	public static String getCommandWriteSettingParameter() {
		

		commandWriteSettingParameter = ConstantRefStdKre.CMD_WRITE_SETTING_HDR + 
				GuiUtils.StringToHex(ConstantRefStdKre.SETTING_WIRING_MODE_DATA_HEADER) + getWriteSettingWiringMode() + ConstantRefStdKre.CMD_SEPERATOR +
				GuiUtils.StringToHex(ConstantRefStdKre.SETTING_ISTALL_PARSED_DATA_HEADER) + getWriteSettingIstall() + ConstantRefStdKre.CMD_SEPERATOR +
				GuiUtils.StringToHex(ConstantRefStdKre.SETTING_OUTPUT1_PULSE_CONSTANT_PARSED_DATA_HEADER) + getWriteSettingOutput1PulseConstant() + ConstantRefStdKre.CMD_SEPERATOR +
				GuiUtils.StringToHex(ConstantRefStdKre.SETTING_OUTPUT2_PULSE_CONSTANT_PARSED_DATA_HEADER) + getWriteSettingOutput2PulseConstant() + ConstantRefStdKre.CMD_SEPERATOR +
				GuiUtils.StringToHex(ConstantRefStdKre.SETTING_OUTPUT1_PULSE_TYPE_PARSED_DATA_HEADER) + getWriteSettingOutput1PulseType() + ConstantRefStdKre.CMD_SEPERATOR +
				GuiUtils.StringToHex(ConstantRefStdKre.SETTING_OUTPUT2_PULSE_TYPE_PARSED_DATA_HEADER) + getWriteSettingOutput2PulseType() + ConstantRefStdKre.CMD_SEPERATOR +
				GuiUtils.StringToHex(ConstantRefStdKre.SETTING_DATE_AND_TIME_PARSED_DATA_HEADER) + getWriteSettingDateTime() + ConstantRefStdKre.CMD_SEPERATOR +
				
				
				ConstantRefStdKre.CMD_TERMINATOR;
		return commandWriteSettingParameter;
	}

	public static String getWriteSettingIstall() {
		return writeSettingIstall;
	}

	public static void setWriteSettingIstall(String currentValue) {
		if(GuiUtils.is_float(currentValue)){
			Data_RefStdKre.writeSettingIstall = GuiUtils.StringToHex(String.format("%#.02f", Float.parseFloat(currentValue)));
		}
	}
	
	public static String getWriteSettingOutput1PulseConstant() {
		return writeSettingOutput1PulseConstant;
	}

	public static void setWriteSettingOutput1PulseConstant(String writeSettingOutputPulseConstant) {
		Data_RefStdKre.writeSettingOutput1PulseConstant = GuiUtils.StringToHex(writeSettingOutputPulseConstant);
	}
	
	public static String getWriteSettingOutput2PulseConstant() {
		return writeSettingOutput2PulseConstant;
	}

	public static void setWriteSettingOutput2PulseConstant(String writeSettingOutputPulseConstant) {
		Data_RefStdKre.writeSettingOutput2PulseConstant = GuiUtils.StringToHex(writeSettingOutputPulseConstant);
	}

	public static String getWriteSettingOutput1PulseType() {
		return writeSettingOutput1PulseType;
	}

	public static void setWriteSettingOutput1PulseType(String writeSettingOutput1PulseType) {
		Data_RefStdKre.writeSettingOutput1PulseType = writeSettingOutput1PulseType;
	}

	public static String getWriteSettingOutput2PulseType() {
		return writeSettingOutput2PulseType;
	}

	public static void setWriteSettingOutput2PulseType(String writeSettingOutput2PulseType) {
		Data_RefStdKre.writeSettingOutput2PulseType = writeSettingOutput2PulseType;
	}

	public static String getWriteSettingDateTime() {
		String timeStamp = new SimpleDateFormat(REPORT_DATE_TIME_FORMAT).format(Calendar.getInstance().getTime());
		writeSettingDateTime = GuiUtils.StringToHex(timeStamp);
		return writeSettingDateTime;
	}

	public static void setWriteSettingDateTime(String writeSettingDateTime) {
		Data_RefStdKre.writeSettingDateTime = writeSettingDateTime;
	}

	public static String getWriteSettingWiringMode() {
		return writeSettingWiringMode;
	}

	public static void setWriteSettingWiringMode(String writeSettingWiringMode) {
		Data_RefStdKre.writeSettingWiringMode = writeSettingWiringMode;
	}
	
	public static boolean  isLastDataSetupForRefStdKreSame(String maxCurrent) {
		// TODO Auto-generated method stub
		boolean status = false;
		//status = true;//
		ApplicationLauncher.logger.debug("isLastDataSetupForRefStdKreSame : Entry");
		if(Float.parseFloat(maxCurrent) <= 1.0f){
			Data_RefStdKre.setWriteSettingIstall(ConstantRefStdKre.CURRENT_LOW_THRESHOLD);
		}else{
			Data_RefStdKre.setWriteSettingIstall(ConstantRefStdKre.CURRENT_MAX);
		}
		Data_RefStdKre.setWriteSettingWiringMode(ConstantRefStdKre.SETTING_DATA_WIRING_MODE_3P4W);
		String rssPulseRateInImpulsesPerKwH = DisplayDataObj.getRSSPulseRate() + "000";
		
		ApplicationLauncher.logger.debug("isLastDataSetupForRefStdKreSame : rssPulseRateInImpulsesPerKwH: " + rssPulseRateInImpulsesPerKwH);
		Data_RefStdKre.setWriteSettingOutput1PulseConstant(rssPulseRateInImpulsesPerKwH);
		
		if(DisplayDataObj.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)){
			Data_RefStdKre.setWriteSettingOutput1PulseType(ConstantRefStdKre.SETTING_DATA_OUTPUT_PULSE_TYPE_ACTIVE);
		}else if(DisplayDataObj.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)){
			Data_RefStdKre.setWriteSettingOutput1PulseType(ConstantRefStdKre.SETTING_DATA_OUTPUT_PULSE_TYPE_REACTIVE);
		}
		if(Data_RefStdKre.getLastWriteSettingIstall().equals(Data_RefStdKre.getWriteSettingIstall()) ){
			if (Data_RefStdKre.getLastWriteSettingWiringMode().equals(Data_RefStdKre.getWriteSettingWiringMode()) ){
				if (Data_RefStdKre.getLastWriteSettingOutput1PulseConstant().equals(Data_RefStdKre.getWriteSettingOutput1PulseConstant()) ){
					if(Data_RefStdKre.getLastWriteSettingOutput1PulseType().equals(Data_RefStdKre.getWriteSettingOutput1PulseType()) ){
						ApplicationLauncher.logger.debug("isLastDataSetupForRefStdKreSame : present setting matching with previous loaded settings");
						status = true;
						return status;
					}
				}
			}
		}
		ApplicationLauncher.logger.debug("isLastDataSetupForRefStdKreSame : present setting NOT matching with previous loaded settings");
		return status;
	}

	public static String getLastWriteSettingIstall() {
		return lastWriteSettingIstall;
	}

	public static void setLastWriteSettingIstall(String lastWriteSettingIstall) {
		Data_RefStdKre.lastWriteSettingIstall = lastWriteSettingIstall;
	}

	public static String getLastWriteSettingWiringMode() {
		return lastWriteSettingWiringMode;
	}

	public static void setLastWriteSettingWiringMode(String lastWriteSettingWiringMode) {
		Data_RefStdKre.lastWriteSettingWiringMode = lastWriteSettingWiringMode;
	}

	public static String getLastWriteSettingOutput1PulseConstant() {
		return lastWriteSettingOutput1PulseConstant;
	}

	public static void setLastWriteSettingOutput1PulseConstant(String lastWriteSettingOutput1PulseConstant) {
		Data_RefStdKre.lastWriteSettingOutput1PulseConstant = lastWriteSettingOutput1PulseConstant;
	}

	public static String getLastWriteSettingOutput1PulseType() {
		return lastWriteSettingOutput1PulseType;
	}

	public static void setLastWriteSettingOutput1PulseType(String lastWriteSettingOutput1PulseType) {
		Data_RefStdKre.lastWriteSettingOutput1PulseType = lastWriteSettingOutput1PulseType;
	}
}
