package com.tasnetwork.calibration.energymeter.deployment;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DeploymentDataModel {
	
	private final StringProperty rackid;
	private final BooleanProperty isSelected;
	private final StringProperty manufacturer;
	private final StringProperty testProject;
	private final StringProperty type;
	private final StringProperty em;
	private final StringProperty serialno;	
	private final StringProperty iMax;
	private final StringProperty lb;
	private final StringProperty vd;
	private final StringProperty status;
	private final StringProperty testCase;
	private final FloatProperty ctr_ratio;
	private final FloatProperty ptr_ratio;
	private final IntegerProperty meter_const;
	private final StringProperty meterMake;	
	
	private final StringProperty meterModelNo;	
	//private final StringProperty batchNumber;	

    
	public DeploymentDataModel(String rackid, String testCase,
			String manufacturer, String testProject, String type, 
			String em, String serialno, String iMax, String lb, String vd,
			String status,  Boolean isSelected, float ctr_ratio, float ptr_ratio,
			int meter_const, String make, String modelNo) {
		
		this.rackid = new SimpleStringProperty(rackid);
		this.manufacturer = new SimpleStringProperty(manufacturer);
		this.testProject = new SimpleStringProperty(testProject);
		this.type = new SimpleStringProperty(type);
		this.em = new SimpleStringProperty(em);
		this.serialno = new SimpleStringProperty(serialno);
		this.iMax = new SimpleStringProperty(iMax);
		this.lb = new SimpleStringProperty(lb);
		this.vd = new SimpleStringProperty(vd);
		this.status = new SimpleStringProperty(status);
		this.testCase = new SimpleStringProperty(testCase);
		this.isSelected = new SimpleBooleanProperty(isSelected);
		this.ctr_ratio = new SimpleFloatProperty(ctr_ratio);
		this.ptr_ratio = new SimpleFloatProperty(ptr_ratio);
		this.meter_const = new SimpleIntegerProperty(meter_const);
		this.meterMake = new SimpleStringProperty(make);
		this.meterModelNo = new SimpleStringProperty(modelNo);
	}
	
	public StringProperty rackidProperty() {
		return rackid;
	}
	
	public String getrackid() {
		return rackid.get();
	}
	
	public void setrackid(String rackid) {
		this.rackid.set(rackid);
	}
	
	public StringProperty manufacturerProperty() {
		return manufacturer;
	}
	
	public String getmanufacturer() {
		return manufacturer.get();
	}

	public void setmanufacturer(String manufacturer) {
		this.manufacturer.set(manufacturer);
	}
	
	
	public BooleanProperty isSelectedProperty() {
		return isSelected;
	}
	public Boolean IsIsSelected() {
		return isSelected.get();
	}
	
	public Boolean getIsSelected() {
		return isSelected.get();
	}

	public void setIsSelected(Boolean selected) {
		this.isSelected.set(selected);
	}
		
	public StringProperty typeProperty() {
		return this.type;
	}
	

	public String getType() {
		return this.typeProperty().get();
	}
	

	public void setType(final String type) {
		this.typeProperty().set(type);
	}
	

	public StringProperty emProperty() {
		return this.em;
	}
	

	public String getEm() {
		return this.emProperty().get();
	}
	

	public void setEm(final String em) {
		this.emProperty().set(em);
	}
	
	public StringProperty serialnoProperty() {
		return this.serialno;
	}
	

	public String getSerialno() {
		return this.serialno.get();
	}
	

	public void setSerialno(String serialno) {
		this.serialno.set(serialno);
	}
	
	
	
	public StringProperty meterMakeProperty() {
		return this.meterMake;
	}
	

	public String getMeterMake() {
		return this.meterMake.get();
	}
	

	public void setMeterMake(String make) {
		this.meterMake.set(make);
	}
	
	
	
	
	
	public StringProperty getMeterModelNoProperty() {
		return this.meterModelNo;
	}
	

	public String getMeterModelNo() {
		return this.meterModelNo.get();
	}
	

	public void setMeterModelNo(String modelNo) {
		this.meterModelNo.set(modelNo);
	}
	
	
	

	public StringProperty iMaxProperty() {
		return this.iMax;
	}
	

	public String getIMax() {
		return this.iMaxProperty().get();
	}
	

	public void setIMax(final String iMax) {
		this.iMaxProperty().set(iMax);
	}
	

	public StringProperty lbProperty() {
		return this.lb;
	}
	

	public String getLb() {
		return this.lbProperty().get();
	}
	

	public void setLb(final String lb) {
		this.lbProperty().set(lb);
	}
	

	public StringProperty vdProperty() {
		return this.vd;
	}
	

	public String getVd() {
		return this.vdProperty().get();
	}
	

	public void setVd(final String vd) {
		this.vdProperty().set(vd);
	}
	
	public StringProperty statusProperty() {
		return this.status;
	}
	

	public String getStatus() {
		return this.statusProperty().get();
	}
	

	public void setStatus(final String status) {
		this.statusProperty().set(status);
	}

	public StringProperty testProjectProperty() {
		return this.testProject;
	}
	

	public String getTestProject() {
		return this.testProjectProperty().get();
	}
	

	public void setTestProject(final String testProject) {
		this.testProjectProperty().set(testProject);
	}


	public StringProperty testCaseProperty() {
		return this.testCase;
	}
	

	public String getTestCase() {
		return this.testCaseProperty().get();
	}
	

	public void setTestCase(final String testCase) {
		this.testCaseProperty().set(testCase);
	}
	
	public FloatProperty ctrratioProperty() {
		return ctr_ratio;
	}
	
	public float getctrratio() {
		return ctr_ratio.get();
	}
	
	public void setctrratio(float ctrratio) {
		this.ctr_ratio.set(ctrratio);
	}
	
	public FloatProperty ptrratioProperty() {
		return ptr_ratio;
	}
	
	public float getptrratio() {
		return ptr_ratio.get();
	}
	
	public void setptrratio(float ptrratio) {
		this.ptr_ratio.set(ptrratio);
	}	
	
	public IntegerProperty meterconstProperty() {
		return meter_const;
	}
	
	public int getmeterconst() {
		return meter_const.get();
	}
	
	public void setmeterconst(int meterconst) {
		this.meter_const.set(meterconst);
	}	
	
}
