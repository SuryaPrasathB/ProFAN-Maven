package com.tasnetwork.calibration.energymeter.constant;

import com.tasnetwork.calibration.energymeter.util.GuiUtils;

public class ConstantConveyor {
	
	public static final String ER_START_CMD = "CSTART" + GuiUtils.hexToAscii("0D0A");
	public static final String CMD_TEST_COMPLETED = "TEST_COMPLETED" ;
	public static final String CMD_START_CONVEYOR = "START_CONVEYOR" ;
	public static final String CMD_STOP_CONVEYOR = "STOP_CONVEYOR" ;
}
