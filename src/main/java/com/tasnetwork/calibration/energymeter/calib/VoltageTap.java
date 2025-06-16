package com.tasnetwork.calibration.energymeter.calib;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VoltageTap {

	@SerializedName("VoltageTapId")
	@Expose
	private int VoltageTapId;

	@SerializedName("VoltageTapMax")
	@Expose
    private float VoltageTapMax;
	
	@SerializedName("VoltageRelayId")
	@Expose
    private String VoltageRelayId;

	@SerializedName("NoOfCalibrationPoints")
	@Expose
    private int NoOfCalibrationPoints;

	@SerializedName("CalibPoints")
	@Expose
    private List<CalibPoints> CalibPoints;

    public void setVoltageTapId(int VoltageTapId){
        this.VoltageTapId = VoltageTapId;
    }
    public int getVoltageTapId(){
        return this.VoltageTapId;
    }
    public void setVoltageTapMax(float VoltageTapMax){
        this.VoltageTapMax = VoltageTapMax;
    }
    public float getVoltageTapMax(){
        return this.VoltageTapMax;
    }
    public void setNoOfCalibrationPoints(int NoOfCalibrationPoints){
        this.NoOfCalibrationPoints = NoOfCalibrationPoints;
    }
    public int getNoOfCalibrationPoints(){
        return this.NoOfCalibrationPoints;
    }
    public void setCalibPoints(List<CalibPoints> CalibPoints){
        this.CalibPoints = CalibPoints;
    }
    public List<CalibPoints> getCalibPoints(){
        return this.CalibPoints;
    }
	public String getVoltageRelayId() {
		return VoltageRelayId;
	}
	public void setVoltageRelayId(String voltageRelayId) {
		VoltageRelayId = voltageRelayId;
	}
}
