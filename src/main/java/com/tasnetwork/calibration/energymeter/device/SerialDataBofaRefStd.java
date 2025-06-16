package com.tasnetwork.calibration.energymeter.device;

public class SerialDataBofaRefStd {

	private String VoltageDisplayData="";
	private String CurrentDisplayData="";
	private String DegreePhaseData="";
	//private String yPhaseDegreePhaseData="";
	//private String bPhaseDegreePhaseData="";
	private String PowerFactorData="";
	private String FreqDisplayData="";
	private String WattDisplayData="";
	private String VA_DisplayData="";
	private String VAR_DisplayData="";
	
	private String activeEnergyInKwhDisplayData="";
	private String reactiveEnergyInKvarhDisplayData="";
	//private String activeEnergyInWhDisplayData="";
	
	
	
	public String getVoltageDisplayData() {
		return VoltageDisplayData;
	}
	public String getCurrentDisplayData() {
		return CurrentDisplayData;
	}
	public String getDegreePhaseData() {
		return DegreePhaseData;
	}
/*	public String getyPhaseDegreePhaseData() {
		return yPhaseDegreePhaseData;
	}
	public String getbPhaseDegreePhaseData() {
		return bPhaseDegreePhaseData;
	}*/
	public String getPowerFactorData() {
		return PowerFactorData;
	}
	public String getFreqDisplayData() {
		return FreqDisplayData;
	}
	public String getWattDisplayData() {
		return WattDisplayData;
	}
	public String getVA_DisplayData() {
		return VA_DisplayData;
	}
	public String getVAR_DisplayData() {
		return VAR_DisplayData;
	}
	public void setVoltageDisplayData(String voltageDisplayData) {
		VoltageDisplayData = voltageDisplayData;
	}
	public void setCurrentDisplayData(String currentDisplayData) {
		CurrentDisplayData = currentDisplayData;
	}
	public void setDegreePhaseData(String rPhaseDegreePhaseData) {
		this.DegreePhaseData = rPhaseDegreePhaseData;
	}
/*	public void setyPhaseDegreePhaseData(String yPhaseDegreePhaseData) {
		this.yPhaseDegreePhaseData = yPhaseDegreePhaseData;
	}
	public void setbPhaseDegreePhaseData(String bPhaseDegreePhaseData) {
		this.bPhaseDegreePhaseData = bPhaseDegreePhaseData;
	}*/
	public void setPowerFactorData(String powerFactorData) {
		PowerFactorData = powerFactorData;
	}
	public void setFreqDisplayData(String freqDisplayData) {
		FreqDisplayData = freqDisplayData;
	}
	public void setWattDisplayData(String wattDisplayData) {
		WattDisplayData = wattDisplayData;
	}
	public void setVA_DisplayData(String vA_DisplayData) {
		VA_DisplayData = vA_DisplayData;
	}
	public void setVAR_DisplayData(String vAR_DisplayData) {
		VAR_DisplayData = vAR_DisplayData;
	}
	public String getActiveEnergyInKwhDisplayData() {
		return activeEnergyInKwhDisplayData;
	}
	
	/*public void setActiveEnergyInWhDisplayData(String activeEnergyInWhDisplayData) {
		this.activeEnergyInWhDisplayData = activeEnergyInWhDisplayData;
	}
	public String getActiveEnergyInWhDisplayData() {
		
		return activeEnergyInWhDisplayData;
	}*/
	public String getReactiveEnergyInKvarhDisplayData() {
		return reactiveEnergyInKvarhDisplayData;
	}
	public void setActiveEnergyInKwhDisplayData(String activeEnergyInKwhDisplayData) {
		this.activeEnergyInKwhDisplayData = activeEnergyInKwhDisplayData;
	}
	public void setReactiveEnergyInKvarhDisplayData(String reactiveEnergyInKvarhDisplayData) {
		this.reactiveEnergyInKvarhDisplayData = reactiveEnergyInKvarhDisplayData;
	}
}
