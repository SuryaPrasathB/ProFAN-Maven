package com.tasnetwork.calibration.energymeter.calib;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrentCalibration {

	@SerializedName("CurrentPhase")
	@Expose
	private String CurrentPhase;

	@SerializedName("NoOfCurrentTaps")
	@Expose
    private int NoOfCurrentTaps;

	@SerializedName("CurrentTap")
	@Expose
    private List<CurrentTap> CurrentTap;

    public void setCurrentPhase(String CurrentPhase){
        this.CurrentPhase = CurrentPhase;
    }
    public String getCurrentPhase(){
        return this.CurrentPhase;
    }
    public void setNoOfCurrentTaps(int NoOfCurrentTaps){
        this.NoOfCurrentTaps = NoOfCurrentTaps;
    }
    public int getNoOfCurrentTaps(){
        return this.NoOfCurrentTaps;
    }
    public void setCurrentTap(List<CurrentTap> CurrentTap){
        this.CurrentTap = CurrentTap;
    }
    public List<CurrentTap> getCurrentTap(){
        return this.CurrentTap;
    }
}
