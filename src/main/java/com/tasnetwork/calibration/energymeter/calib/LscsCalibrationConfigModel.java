package com.tasnetwork.calibration.energymeter.calib;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LscsCalibrationConfigModel {

	@SerializedName("VoltageCalibration")
	@Expose
    private List<VoltageCalibration> VoltageCalibration;
	
	@SerializedName("CurrentCalibration")
	@Expose
	private List<CurrentCalibration> CurrentCalibration;
	
    public void setCurrentCalibration(List<CurrentCalibration> CurrentCalibration){
        this.CurrentCalibration = CurrentCalibration;
    }
    public List<CurrentCalibration> getCurrentCalibration(){
        return this.CurrentCalibration;
    }

    public void setVoltageCalibration(List<VoltageCalibration> VoltageCalibration){
        this.VoltageCalibration = VoltageCalibration;
    }
    public List<VoltageCalibration> getVoltageCalibration(){
        return this.VoltageCalibration;
    }
}
