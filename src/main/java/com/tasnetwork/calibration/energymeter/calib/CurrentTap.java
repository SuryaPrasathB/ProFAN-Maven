package com.tasnetwork.calibration.energymeter.calib;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrentTap {

	@SerializedName("CurrentTapId")
	@Expose
	private int CurrentTapId;

	@SerializedName("CurrentTapMax")
	@Expose
    private float CurrentTapMax;
	
	@SerializedName("CurrentRelayId")
	@Expose
    private String CurrentRelayId;

	@SerializedName("NoOfCalibrationPoints")
	@Expose
    private int NoOfCalibrationPoints;

	@SerializedName("CalibPoints")
	@Expose
    private List<CalibPoints> CalibPoints;

    public void setCurrentTapId(int CurrentTapId){
        this.CurrentTapId = CurrentTapId;
    }
    public int getCurrentTapId(){
        return this.CurrentTapId;
    }
    public void setCurrentTapMax(float CurrentTapMax){
        this.CurrentTapMax = CurrentTapMax;
    }
    public float getCurrentTapMax(){
        return this.CurrentTapMax;
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
	public String getCurrentRelayId() {
		return CurrentRelayId;
	}
	public void setCurrentRelayId(String currentRelayId) {
		CurrentRelayId = currentRelayId;
	}
}
