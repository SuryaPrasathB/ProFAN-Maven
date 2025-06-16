package com.tasnetwork.calibration.energymeter.director;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.deployment.BofaManager;
import com.tasnetwork.calibration.energymeter.messenger.PowerSourceBofaMessenger;

public class PowerSourceDirector {

	public boolean setPowerSourceOff() {
		// TODO Auto-generated method stub
		boolean status = false;
		//Sleep(30000);
		if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
			
		
		}else if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
			//BofaManager.setBofaPowerSourceOff();
			PowerSourceBofaMessenger pwrSrcBofaMessenger = new PowerSourceBofaMessenger();
			status = pwrSrcBofaMessenger.sendVoltageCurrentStopOutputCommand();
		}else {
			
		}
		return status;
	}
	
	
	public boolean setPowerSourceMctNctMode(String mctNctMode, boolean forceSet) {
		// TODO Auto-generated method stub
		boolean status = false;
		//Sleep(30000);
		if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
			
		
		}else if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
			//BofaManager.setBofaPowerSourceOff();
			if(ProcalFeatureEnable.BOFA_LDU_CONNECTED) {
				ApplicationLauncher.logger.debug("setPowerSourceMctNctMode :Bofa Entry");
				PowerSourceBofaMessenger pwrSrcBofaMessenger = new PowerSourceBofaMessenger();
				status = pwrSrcBofaMessenger.bofaSetPowerSourceMctNctMode(mctNctMode, forceSet);
			}else {
				ApplicationLauncher.logger.debug("setPowerSourceMctNctMode :Bofa LDU disabled");
				status = true;
			}
		}else {
			
		}
		return status;
	}
	
	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("PowerSourceDirector: Sleep2 :InterruptedException:"+ e.getMessage());
		}

	}

	

}
