package com.tasnetwork.calibration.energymeter.message;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdKiggs;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;

public class RefStdKiggsMessage {
	
	public static String CHECKSUM_READ_R_PHASE_VOLT_CURRENT = GuiUtils.generateCheckSumWithOneByte(
			ConstantRefStdKiggs.CMD_MEASURED_VOLT_CURRENT_PREFIX + 
			ConstantRefStdKiggs.CH_R_PHASE_VOLTAGE);
	
	public static String CHECKSUM_READ_R_PHASE_DEGREE  = GuiUtils.generateCheckSumWithOneByte(
			ConstantRefStdKiggs.CMD_MEASURED_DEGREE_PHASE_PREFIX + 
			ConstantRefStdKiggs.CH_R_PHASE_CURRENT);
	
	public static String CHECKSUM_READ_R_PHASE_POWER_FACTOR  = GuiUtils.generateCheckSumWithOneByte(
			ConstantRefStdKiggs.CMD_MEASURED_POWER_FACTOR_PREFIX + 
			ConstantRefStdKiggs.CH_R_PHASE);
	
	public static String CHECKSUM_READ_R_PHASE_APPARENT_POWER  = GuiUtils.generateCheckSumWithOneByte(
			ConstantRefStdKiggs.CMD_MEASURED_APPARENT_POWER_PREFIX + 
			ConstantRefStdKiggs.CH_R_PHASE);
			
	public static String CHECKSUM_READ_R_PHASE_REACTIVE_POWER  = GuiUtils.generateCheckSumWithOneByte(
			ConstantRefStdKiggs.CMD_MEASURED_REACTIVE_POWER_PREFIX + 
			ConstantRefStdKiggs.CH_R_PHASE);
			
	public static String CHECKSUM_READ_R_PHASE_ACTIVE_POWER  = GuiUtils.generateCheckSumWithOneByte(
			ConstantRefStdKiggs.CMD_MEASURED_ACTIVE_POWER_PREFIX + 
			ConstantRefStdKiggs.CH_R_PHASE);


	public static String MSG_READ_R_PHASE_VOLT_CURRENT = ConstantRefStdKiggs.CMD_MEASURED_VOLT_CURRENT_HEADER +
													ConstantRefStdKiggs.CMD_MEASURED_VOLT_CURRENT_PREFIX + 
													ConstantRefStdKiggs.CH_R_PHASE_VOLTAGE +
													CHECKSUM_READ_R_PHASE_VOLT_CURRENT;
	
	public static String MSG_CMD_GENERAL_REQUEST = ConstantRefStdKiggs.CMD_GENERAL_REQUEST_HEADER ;
	
	public static String MSG_READ_R_PHASE_DEGREE = ConstantRefStdKiggs.CMD_MEASURED_DEGREE_PHASE_HEADER +
														ConstantRefStdKiggs.CMD_MEASURED_DEGREE_PHASE_PREFIX + 
														ConstantRefStdKiggs.CH_R_PHASE_CURRENT +
														CHECKSUM_READ_R_PHASE_DEGREE;
	
	public static String MSG_READ_R_PHASE_POWER_FACTOR = ConstantRefStdKiggs.CMD_MEASURED_POWER_FACTOR_HEADER +
													ConstantRefStdKiggs.CMD_MEASURED_POWER_FACTOR_PREFIX + 
													ConstantRefStdKiggs.CH_R_PHASE +
													CHECKSUM_READ_R_PHASE_POWER_FACTOR;
	
	public static String MSG_READ_R_PHASE_APPARENT_POWER = ConstantRefStdKiggs.CMD_MEASURED_APPARENT_POWER_HEADER +
															ConstantRefStdKiggs.CMD_MEASURED_APPARENT_POWER_PREFIX + 
															ConstantRefStdKiggs.CH_R_PHASE +
															CHECKSUM_READ_R_PHASE_APPARENT_POWER;
	
	public static String MSG_READ_R_PHASE_REACTIVE_POWER = ConstantRefStdKiggs.CMD_MEASURED_REACTIVE_POWER_HEADER +
															ConstantRefStdKiggs.CMD_MEASURED_REACTIVE_POWER_PREFIX + 
															ConstantRefStdKiggs.CH_R_PHASE +
															CHECKSUM_READ_R_PHASE_REACTIVE_POWER;
	
	public static String MSG_READ_R_PHASE_ACTIVE_POWER = ConstantRefStdKiggs.CMD_MEASURED_ACTIVE_POWER_HEADER +
															ConstantRefStdKiggs.CMD_MEASURED_ACTIVE_POWER_PREFIX + 
															ConstantRefStdKiggs.CH_R_PHASE +
															CHECKSUM_READ_R_PHASE_ACTIVE_POWER;
	
	public static String MSG_READ_R_PHASE_FREQUENCY = ConstantRefStdKiggs.CMD_MEASURED_FREQUENCY_HEADER +
															ConstantRefStdKiggs.CMD_MEASURED_FREQUENCY_PREFIX ;
	
	public static String MSG_CMD_REFSTD_METER_WORKING_MODE = ConstantRefStdKiggs.CMD_REFSTD_METER_WORKING_MODE_HEADER ;
	
	
	public static String CHECKSUM_BNC_OUTPUT_SETTING_ACTIVE_POWER = GuiUtils.generateCheckSumWithOneByte(
			ConstantRefStdKiggs.CMD_BNC_PORT_OUTPUT_PREFIX + 
			ConstantRefStdKiggs.CMD_BNC_PORT_DATA1_ACTIVE_POWER +
			ConstantRefStdKiggs.CMD_BNC_PORT_DATA2_AUTOMATIC_CONSTANT );
	
	public static String CHECKSUM_BNC_OUTPUT_SETTING_REACTIVE_POWER = GuiUtils.generateCheckSumWithOneByte(
			ConstantRefStdKiggs.CMD_BNC_PORT_OUTPUT_PREFIX + 
			ConstantRefStdKiggs.CMD_BNC_PORT_DATA1_REACTIVE_POWER +
			ConstantRefStdKiggs.CMD_BNC_PORT_DATA2_AUTOMATIC_CONSTANT );
	
	public static String MSG_BNC_OUTPUT_SETTING_ACTIVE_POWER = ConstantRefStdKiggs.CMD_BNC_PORT_OUTPUT_HEADER +
			ConstantRefStdKiggs.CMD_BNC_PORT_OUTPUT_PREFIX + 
			ConstantRefStdKiggs.CMD_BNC_PORT_DATA1_ACTIVE_POWER +
			ConstantRefStdKiggs.CMD_BNC_PORT_DATA2_AUTOMATIC_CONSTANT +
			CHECKSUM_BNC_OUTPUT_SETTING_ACTIVE_POWER;
	
	
	public static String MSG_BNC_OUTPUT_SETTING_REACTIVE_POWER = ConstantRefStdKiggs.CMD_BNC_PORT_OUTPUT_HEADER +
			ConstantRefStdKiggs.CMD_BNC_PORT_OUTPUT_PREFIX + 
			ConstantRefStdKiggs.CMD_BNC_PORT_DATA1_REACTIVE_POWER +
			ConstantRefStdKiggs.CMD_BNC_PORT_DATA2_AUTOMATIC_CONSTANT +
			CHECKSUM_BNC_OUTPUT_SETTING_REACTIVE_POWER;
	
	
	public static String CHECKSUM_ACCU_SETTING_MODE_ACTIVE_ENERGY_ACCU = GuiUtils.generateCheckSumWithOneByte(
			ConstantRefStdKiggs.CMD_ACCU_SETTING_MODE_PREFIX + 
			ConstantRefStdKiggs.CMD_ACCU_SETTING_MODE_DATA1_SINGLE_ACTIVE_ENERGY +
			ConstantRefStdKiggs.CMD_ACCU_SETTING_MODE_DATA2_FUNDAMENTAL_WAVE +
			ConstantRefStdKiggs.CMD_ACCU_SETTING_MODE_DATA3_COLLECTION_TIME );
	
	public static String CHECKSUM_ACCU_SETTING_MODE_BASIC_MEASURE = GuiUtils.generateCheckSumWithOneByte(
			ConstantRefStdKiggs.CMD_ACCU_SETTING_MODE_PREFIX + 
			ConstantRefStdKiggs.CMD_ACCU_SETTING_MODE_DATA1_BASIC_MEASURE +
			ConstantRefStdKiggs.CMD_ACCU_SETTING_MODE_DATA2_FUNDAMENTAL_WAVE +
			ConstantRefStdKiggs.CMD_ACCU_SETTING_MODE_DATA3_COLLECTION_TIME );
	
	public static String MSG_ACCU_SETTING_MODE_ACTIVE_ENERGY_ACCU = ConstantRefStdKiggs.CMD_ENERGY_ACCU_SETTING_MODE_HEADER +
			ConstantRefStdKiggs.CMD_ACCU_SETTING_MODE_PREFIX + 
			ConstantRefStdKiggs.CMD_ACCU_SETTING_MODE_DATA1_SINGLE_ACTIVE_ENERGY +
			ConstantRefStdKiggs.CMD_ACCU_SETTING_MODE_DATA2_FUNDAMENTAL_WAVE +
			ConstantRefStdKiggs.CMD_ACCU_SETTING_MODE_DATA3_COLLECTION_TIME +
			CHECKSUM_ACCU_SETTING_MODE_ACTIVE_ENERGY_ACCU;
	
	public static String MSG_ACCU_SETTING_MODE_BASIC_MEASURE = ConstantRefStdKiggs.CMD_ENERGY_ACCU_SETTING_MODE_HEADER +
			ConstantRefStdKiggs.CMD_ACCU_SETTING_MODE_PREFIX + 
			ConstantRefStdKiggs.CMD_ACCU_SETTING_MODE_DATA1_BASIC_MEASURE +
			ConstantRefStdKiggs.CMD_ACCU_SETTING_MODE_DATA2_FUNDAMENTAL_WAVE +
			ConstantRefStdKiggs.CMD_ACCU_SETTING_MODE_DATA3_COLLECTION_TIME +
			CHECKSUM_ACCU_SETTING_MODE_BASIC_MEASURE;
	
	public static String CHECKSUM_SETTING_MODE_SINGLE_PHASE_ACTIVE = GuiUtils.generateCheckSumWithOneByte(
			ConstantRefStdKiggs.CMD_ACCU_SETTING_MODE_PREFIX + 
			ConstantRefStdKiggs.CMD_ACCU_SETTING_MODE_DATA1_SINGLE_PHASE_ACTIVE_POWER +
			ConstantRefStdKiggs.CMD_ACCU_SETTING_MODE_DATA2_FUNDAMENTAL_WAVE +
			ConstantRefStdKiggs.CMD_ACCU_SETTING_MODE_DATA3_COLLECTION_TIME  );
	
	public static String CHECKSUM_SETTING_MODE_SINGLE_PHASE_REACTIVE = GuiUtils.generateCheckSumWithOneByte(
			ConstantRefStdKiggs.CMD_ACCU_SETTING_MODE_PREFIX + 
			ConstantRefStdKiggs.CMD_ACCU_SETTING_MODE_DATA1_SINGLE_PHASE_REACTIVE_POWER +
			ConstantRefStdKiggs.CMD_ACCU_SETTING_MODE_DATA2_FUNDAMENTAL_WAVE +
			ConstantRefStdKiggs.CMD_ACCU_SETTING_MODE_DATA3_COLLECTION_TIME  );
	
	public static String MSG_SETTING_MODE_SINGLE_PHASE_ACTIVE = ConstantRefStdKiggs.CMD_ENERGY_ACCU_SETTING_MODE_HEADER +
			ConstantRefStdKiggs.CMD_ACCU_SETTING_MODE_PREFIX + 
			ConstantRefStdKiggs.CMD_ACCU_SETTING_MODE_DATA1_SINGLE_PHASE_ACTIVE_POWER +
			ConstantRefStdKiggs.CMD_ACCU_SETTING_MODE_DATA2_FUNDAMENTAL_WAVE +
			ConstantRefStdKiggs.CMD_ACCU_SETTING_MODE_DATA3_COLLECTION_TIME +
			CHECKSUM_SETTING_MODE_SINGLE_PHASE_ACTIVE;
	
	public static String MSG_SETTING_MODE_SINGLE_PHASE_REACTIVE = ConstantRefStdKiggs.CMD_ENERGY_ACCU_SETTING_MODE_HEADER +
			ConstantRefStdKiggs.CMD_ACCU_SETTING_MODE_PREFIX + 
			ConstantRefStdKiggs.CMD_ACCU_SETTING_MODE_DATA1_SINGLE_PHASE_REACTIVE_POWER +
			ConstantRefStdKiggs.CMD_ACCU_SETTING_MODE_DATA2_FUNDAMENTAL_WAVE +
			ConstantRefStdKiggs.CMD_ACCU_SETTING_MODE_DATA3_COLLECTION_TIME +
			CHECKSUM_SETTING_MODE_SINGLE_PHASE_REACTIVE;
	
	public static String CHECKSUM_ACCU_CONTROL_START  = GuiUtils.generateCheckSumWithOneByte(
			ConstantRefStdKiggs.CMD_ENERGY_ACCU_CONTROL_PREFIX + 
			ConstantRefStdKiggs.CMD_ENERGY_ACCU_CONTROL_DATA1_START  );
	
	public static String MSG_ACCU_CONTROL_START = ConstantRefStdKiggs.CMD_ENERGY_ACCU_CONTROL_HEADER +
			ConstantRefStdKiggs.CMD_ENERGY_ACCU_CONTROL_PREFIX + 
			ConstantRefStdKiggs.CMD_ENERGY_ACCU_CONTROL_DATA1_START +
			CHECKSUM_ACCU_CONTROL_START;
	
	public static String CHECKSUM_ACCU_CONTROL_STOP  = GuiUtils.generateCheckSumWithOneByte(
			ConstantRefStdKiggs.CMD_ENERGY_ACCU_CONTROL_PREFIX + 
			ConstantRefStdKiggs.CMD_ENERGY_ACCU_CONTROL_DATA1_STOP  );
	
	public static String MSG_ACCU_CONTROL_STOP = ConstantRefStdKiggs.CMD_ENERGY_ACCU_CONTROL_HEADER +
			ConstantRefStdKiggs.CMD_ENERGY_ACCU_CONTROL_PREFIX + 
			ConstantRefStdKiggs.CMD_ENERGY_ACCU_CONTROL_DATA1_STOP +
			CHECKSUM_ACCU_CONTROL_STOP;
	
	public static String MSG_ACCU_READ_MESSAGE = ConstantRefStdKiggs.CMD_ENERGY_ACCU_READ_HEADER ;
	
	
	public static String CHECKSUM_CURRENT_RANGE_SETTING_AUTO  = GuiUtils.generateCheckSumWithOneByte(
			ConstantRefStdKiggs.CMD_CURRENT_RANGE_SETTING_PREFIX + 
			ConstantRefStdKiggs.CMD_CURRENT_RANGE_SETTING_DATA1_AUTO +
			ConstantRefStdKiggs.CURRENT_TERMINAL_TAP_AUTO_MODE_CURRENT_TAP   );
	
	public static String MSG_CURRENT_RANGE_SETTING_AUTO = ConstantRefStdKiggs.CMD_CURRENT_RANGE_SETTING_HEADER +
			ConstantRefStdKiggs.CMD_CURRENT_RANGE_SETTING_PREFIX + 
			ConstantRefStdKiggs.CMD_CURRENT_RANGE_SETTING_DATA1_AUTO +
			ConstantRefStdKiggs.CURRENT_TERMINAL_TAP_AUTO_MODE_CURRENT_TAP +
			CHECKSUM_CURRENT_RANGE_SETTING_AUTO;
	
	
	public static String CHECKSUM_CURRENT_RANGE_SETTING_MANUAL_MODE_MAX_TAP = GuiUtils.generateCheckSumWithOneByte(
			ConstantRefStdKiggs.CMD_CURRENT_RANGE_SETTING_PREFIX + 
			ConstantRefStdKiggs.CMD_CURRENT_RANGE_SETTING_DATA1_MANUAL +
			ConstantRefStdKiggs.CURRENT_TERMINAL_TAP_STRING_LIST.get(0)  );
	
	public static String MSG_CURRENT_RANGE_SETTING_MANUAL_MODE_MAX_TAP = ConstantRefStdKiggs.CMD_CURRENT_RANGE_SETTING_HEADER +
			ConstantRefStdKiggs.CMD_CURRENT_RANGE_SETTING_PREFIX + 
			ConstantRefStdKiggs.CMD_CURRENT_RANGE_SETTING_DATA1_MANUAL +
			ConstantRefStdKiggs.CURRENT_TERMINAL_TAP_STRING_LIST.get(0) +
			CHECKSUM_CURRENT_RANGE_SETTING_MANUAL_MODE_MAX_TAP;
			
	
	public static String msgCurrentRangeSettingManualMode = "";
	
	public static String getMsgCurrentRangeSettingManualMode(float inputSelectedCurrentValue){
		
		String selectedCurrentValueStr = ConstantRefStdKiggs.CURRENT_TERMINAL_TAP_STRING_LIST.get(0);
		ApplicationLauncher.logger.debug("getMsgCurrentRangeSettingManualMode: inputSelectedCurrentValue: " + inputSelectedCurrentValue);
		for(int i = 1; i < ConstantRefStdKiggs.CURRENT_TERMINAL_TAP_VALUE_LIST.size(); i++){
			if(inputSelectedCurrentValue > ConstantRefStdKiggs.CURRENT_TERMINAL_TAP_VALUE_LIST.get(i)){
/*				if(i==0){
					selectedCurrentValueStr = ConstantRefStdKiggs.CURRENT_TERMINAL_TAP_STRING_LIST.get(i);
				}else{*/
					selectedCurrentValueStr = ConstantRefStdKiggs.CURRENT_TERMINAL_TAP_STRING_LIST.get(i-1);
				//}
				ApplicationLauncher.logger.debug("getMsgCurrentRangeSettingManualMode: selectedCurrentValueStr: " + selectedCurrentValueStr);
				break;
			}
		}
		
		
		msgCurrentRangeSettingManualMode = ConstantRefStdKiggs.CMD_CURRENT_RANGE_SETTING_HEADER +
				ConstantRefStdKiggs.CMD_CURRENT_RANGE_SETTING_PREFIX + 
				ConstantRefStdKiggs.CMD_CURRENT_RANGE_SETTING_DATA1_MANUAL +
				selectedCurrentValueStr +
				GuiUtils.generateCheckSumWithOneByte(
						ConstantRefStdKiggs.CMD_CURRENT_RANGE_SETTING_PREFIX + 
						ConstantRefStdKiggs.CMD_CURRENT_RANGE_SETTING_DATA1_MANUAL +
						selectedCurrentValueStr  );
		ApplicationLauncher.logger.debug("getMsgCurrentRangeSettingManualMode: msgCurrentRangeSettingManualMode: " + msgCurrentRangeSettingManualMode);
		return msgCurrentRangeSettingManualMode;
		
	}
	
	

	
}
