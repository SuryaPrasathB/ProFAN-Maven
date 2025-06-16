package com.tasnetwork.calibration.energymeter.message;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdKre;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdSands;
import com.tasnetwork.calibration.energymeter.constant.Constant_ICT;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;

public class RefStdKreMessage {

	
	public static String MSG_READ_SETTING = ConstantRefStdKre.CMD_READ_SETTING_HDR + ConstantRefStdKre.CMD_TERMINATOR;
	public static String MSG_READ_BASIC_DATA = ConstantRefStdKre.CMD_READ_BASIC_DATA_HDR + ConstantRefStdKre.CMD_TERMINATOR;
	
	public static String MSG_ENERGY_ACCU_START = ConstantRefStdKre.CMD_ENERGY_ACC_START_HDR + ConstantRefStdKre.CMD_TERMINATOR;






}
