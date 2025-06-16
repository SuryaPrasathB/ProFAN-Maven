package com.tasnetwork.calibration.energymeter.device;

import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdSands;

public class Data_RefStdSands {
	
	private static String writeConfigModeVoltage  = ConstantRefStdSands.DATA_CONFIG_MODE_M1_VOLT_LT_240V;
	private static String writeConfigModeCurrent  = ConstantRefStdSands.DATA_CONFIG_MODE_M2_CURRENT_MAX_100A;
	private static String writeConfigModePulseOutputUnit  = ConstantRefStdSands.DATA_CONFIG_MODE_M3_PULSE_OUTPUT_ACTIVE_ENERGY;
	
	private static String readConfigModeVoltage  = ConstantRefStdSands.DATA_CONFIG_MODE_M1_VOLT_LT_240V;
	private static String readConfigModeCurrent  = ConstantRefStdSands.DATA_CONFIG_MODE_M2_CURRENT_MAX_100A;
	private static String readConfigModePulseOutputUnit  = ConstantRefStdSands.DATA_CONFIG_MODE_M3_PULSE_OUTPUT_ACTIVE_ENERGY;
	
	
	public static String getWriteConfigModeVoltage() {
		return writeConfigModeVoltage;
	}
	public static void setWriteConfigModeVoltage(String writeConfigModeVoltage) {
		Data_RefStdSands.writeConfigModeVoltage = writeConfigModeVoltage;
	}
	public static String getWriteConfigModeCurrent() {
		return writeConfigModeCurrent;
	}
	public static void setWriteConfigModeCurrent(String writeConfigModeCurrent) {
		Data_RefStdSands.writeConfigModeCurrent = writeConfigModeCurrent;
	}
	public static String getWriteConfigModePulseOutputUnit() {
		return writeConfigModePulseOutputUnit;
	}
	public static void setWriteConfigModePulseOutputUnit(String writeConfigModePulseOutputUnit) {
		Data_RefStdSands.writeConfigModePulseOutputUnit = writeConfigModePulseOutputUnit;
	}
	public static String getReadConfigModeVoltage() {
		return readConfigModeVoltage;
	}
	public static void setReadConfigModeVoltage(String readConfigModeVoltage) {
		Data_RefStdSands.readConfigModeVoltage = readConfigModeVoltage;
	}
	public static String getReadConfigModeCurrent() {
		return readConfigModeCurrent;
	}
	public static void setReadConfigModeCurrent(String readConfigModeCurrent) {
		Data_RefStdSands.readConfigModeCurrent = readConfigModeCurrent;
	}
	public static String getReadConfigModePulseOutputUnit() {
		return readConfigModePulseOutputUnit;
	}
	public static void setReadConfigModePulseOutputUnit(String readConfigModePulseOutputUnit) {
		Data_RefStdSands.readConfigModePulseOutputUnit = readConfigModePulseOutputUnit;
	}
	
	

}
