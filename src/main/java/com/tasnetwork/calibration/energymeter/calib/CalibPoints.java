package com.tasnetwork.calibration.energymeter.calib;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CalibPoints {

	@SerializedName("CalibPointId")
	@Expose
    private int CalibPointId;

	@SerializedName("CalibPointName")
	@Expose
    private String CalibPointName;
	
	@SerializedName("CalibPointValue")
	@Expose
    private float CalibPointValue;

	@SerializedName("rmsValue")
	@Expose
    private long rmsValue;
	
	
	@SerializedName("gainValue")
	@Expose
    private double gainValue;
	
	@SerializedName("offsetValue")
	@Expose
    private double offsetValue;

    public void setCalibPointId(int CalibPointId){
        this.CalibPointId = CalibPointId;
    }
    public int getCalibPointId(){
        return this.CalibPointId;
    }
    public void setCalibPointName(String CalibPointName){
        this.CalibPointName = CalibPointName;
    }
    public String getCalibPointName(){
        return this.CalibPointName;
    }
    public void setRmsValue(long rmsValue){
        this.rmsValue = rmsValue;
    }
    public long getRmsValue(){
        return this.rmsValue;
    }
	public float getCalibPointValue() {
		return CalibPointValue;
	}
	public void setCalibPointValue(float calibPointValue) {
		CalibPointValue = calibPointValue;
	}
	public double getGainValue() {
		return gainValue;
	}
	public void setGainValue(double gainValue) {
		this.gainValue = gainValue;
	}
	public double getOffsetValue() {
		return offsetValue;
	}
	public void setOffsetValue(double offsetValue) {
		this.offsetValue = offsetValue;
	}
}
