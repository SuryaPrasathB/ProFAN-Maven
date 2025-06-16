package com.tasnetwork.calibration.energymeter.director;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdKre;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.deployment.DisplayInstantMetricsController;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
import com.tasnetwork.calibration.energymeter.device.Data_RefStdKre;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.device.SerialDataRefStdKre;
import com.tasnetwork.calibration.energymeter.message.RefStdKiggsMessage;
import com.tasnetwork.calibration.energymeter.message.RefStdKreMessage;
import com.tasnetwork.calibration.energymeter.messenger.RefStdKreMessenger;
import com.tasnetwork.calibration.energymeter.serial.portmanagerV2.SerialPortManagerRefStd_V2;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;

public class RefStdDirector {

	
	






	public boolean refStdWriteSettingTask() {
		// TODO Auto-generated method stub
		
		boolean status = false;
		if(ProcalFeatureEnable.KRE_REFSTD_CONNECTED){
			RefStdKreMessenger refStdKreMessenger = new RefStdKreMessenger();
			status = refStdKreMessenger.kreRefStdWriteSettingTask();
		}
		return status;
	}

	public void refStdReadAllData() {
		// TODO Auto-generated method stub
		
		if(ProcalFeatureEnable.KRE_REFSTD_CONNECTED){
			RefStdKreMessenger refStdKreMessenger = new RefStdKreMessenger();
			refStdKreMessenger.kreReadRefStdAllData();
		}
		
	}

	public boolean kreRefStdAccumulationStartTask() {
		// TODO Auto-generated method stub
		
		boolean status = false;
		
		if(ProcalFeatureEnable.KRE_REFSTD_CONNECTED){
			RefStdKreMessenger refStdKreMessenger = new RefStdKreMessenger();
			status = refStdKreMessenger.kreRefStdAccumulationStartTask();
		}
		
		return status;
	}
	
}
