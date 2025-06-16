package com.tasnetwork.calibration.energymeter.calib;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VoltageCalibration {

	@SerializedName("VoltagePhase")
	@Expose
    private String VoltagePhase;

	@SerializedName("NoOfVoltageTaps")
	@Expose
    private int NoOfVoltageTaps;

	@SerializedName("VoltageTap")
	@Expose
    private List<VoltageTap> VoltageTap;

    public void setVoltagePhase(String VoltagePhase){
        this.VoltagePhase = VoltagePhase;
    }
    public String getVoltagePhase(){
        return this.VoltagePhase;
    }
    public void setNoOfVoltageTaps(int NoOfVoltageTaps){
        this.NoOfVoltageTaps = NoOfVoltageTaps;
    }
    public int getNoOfVoltageTaps(){
        return this.NoOfVoltageTaps;
    }
    public void setVoltageTap(List<VoltageTap> VoltageTap){
        this.VoltageTap = VoltageTap;
    }
    public List<VoltageTap> getVoltageTap(){
        return this.VoltageTap;
    }
}
