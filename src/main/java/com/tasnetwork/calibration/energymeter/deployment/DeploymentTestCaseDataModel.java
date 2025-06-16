package com.tasnetwork.calibration.energymeter.deployment;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DeploymentTestCaseDataModel {
	
	private final BooleanProperty isSelected;
	private final StringProperty testCase;
	private final StringProperty testtype;
	private final StringProperty aliasid;
	private final StringProperty sequence_no;
	
	
	
	private int  testPeriodInSec = 0;
	private int  warmupPeriodInSec = 0;
	private String  targetFreq = ""; 
	private String  targetEnergy = ""; 
	private String	target_RYB_Voltage = ""; 
	private String	target_RYB_Current = ""; 
	private String	target_RYB_Pf = ""; 
	private String	target_R_Voltage = ""; 
	private String 	target_Y_Voltage = ""; 
	private String 	target_B_Voltage = ""; 
	private String 	target_R_Current = ""; 
	private String 	target_Y_Current = ""; 
	private String 	target_B_Current = ""; 
	private String 	target_R_Pf = ""; 
	private String 	target_Y_Pf = ""; 
	private String 	target_B_Pf = ""; 
	private String 	runType = ""; 
	private String 	maxErrorAllowed = ""; 
	private String 	minErrorAllowed = "";	
	private int 	targetNoOfPulses = 0;	
	private int 	readingId = 0;	
	private String 	testSubType = "";	
	private int 	targetAverageCount = 0;	

    
	public DeploymentTestCaseDataModel(String testCase, Boolean isSelected, String testtype,String aliasid, String sequence_no) {
		
		this.isSelected = new SimpleBooleanProperty(isSelected);
		
		this.testCase = new SimpleStringProperty(testCase);
		this.testtype = new SimpleStringProperty(testtype);
		this.aliasid = new SimpleStringProperty(aliasid);
		this.sequence_no = new SimpleStringProperty(sequence_no);
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
		
	public StringProperty testCaseProperty() {
		//return this.testCase;
		return testCase;
	}
	

	public String getTestCase() {
		//return this.testCaseProperty().get();
		return testCase.get();
		//testData.get();
	}
	

	public void setTestCase(final String inptestCase) {
		//this.testCaseProperty().set(testCase);
		testCase.set(inptestCase);
	}
	
	public StringProperty testtypeProperty() {
		return this.testtype;
	}
	

	public String getTesttype() {
		return this.testtypeProperty().get();
	}
	

	public void setTesttype(final String testtype) {
		this.sequencenoProperty().set(testtype);
	}
	
	public StringProperty aliasidProperty() {
		return this.aliasid;
	}
	

	public String getAliasid() {
		return this.aliasidProperty().get();
	}
	

	public void setAliasid(final String aliasid) {
		this.sequencenoProperty().set(aliasid);
	}
	
	public StringProperty sequencenoProperty() {
		return this.sequence_no;
	}
	

	public String getSequence_no() {
		return this.sequencenoProperty().get();
	}
	

	public void setSequence_no(final String sequence_no) {
		this.sequencenoProperty().set(sequence_no);
	}
	
	@Override
	public String toString() {
		return "DeploymentTestCaseDataModel [isSelected=" + isSelected + ", testcase=" + testCase + ", toString()=" + super.toString()
				+ "]";
	}

	public int getTestPeriodInSec() {
		return testPeriodInSec;
	}

	public int getWarmupPeriodInSec() {
		return warmupPeriodInSec;
	}

	public String getTargetFreq() {
		return targetFreq;
	}

	public String getTargetEnergy() {
		return targetEnergy;
	}

	public String getTarget_RYB_Voltage() {
		return target_RYB_Voltage;
	}

	public String getTarget_RYB_Current() {
		return target_RYB_Current;
	}

	public String getTarget_RYB_Pf() {
		return target_RYB_Pf;
	}

	public String getTarget_R_Voltage() {
		return target_R_Voltage;
	}

	public String getTarget_Y_Voltage() {
		return target_Y_Voltage;
	}

	public String getTarget_B_Voltage() {
		return target_B_Voltage;
	}

	public String getTarget_R_Current() {
		return target_R_Current;
	}

	public String getTarget_Y_Current() {
		return target_Y_Current;
	}

	public String getTarget_B_Current() {
		return target_B_Current;
	}

	public String getTarget_R_Pf() {
		return target_R_Pf;
	}

	public String getTarget_Y_Pf() {
		return target_Y_Pf;
	}

	public String getTarget_B_Pf() {
		return target_B_Pf;
	}

	public String getRunType() {
		return runType;
	}

	public String getMaxErrorAllowed() {
		return maxErrorAllowed;
	}

	public String getMinErrorAllowed() {
		return minErrorAllowed;
	}

	public int getTargetNoOfPulses() {
		return targetNoOfPulses;
	}

	public int getReadingId() {
		return readingId;
	}

	public String getTestSubType() {
		return testSubType;
	}

	public int getTargetAverageCount() {
		return targetAverageCount;
	}

	public void setTestPeriodInSec(int testPeriodInSec) {
		this.testPeriodInSec = testPeriodInSec;
	}

	public void setWarmupPeriodInSec(int warmupPeriodInSec) {
		this.warmupPeriodInSec = warmupPeriodInSec;
	}

	public void setTargetFreq(String targetFreq) {
		this.targetFreq = targetFreq;
	}

	public void setTargetEnergy(String targetEnergy) {
		this.targetEnergy = targetEnergy;
	}

	public void setTarget_RYB_Voltage(String target_RYB_Voltage) {
		this.target_RYB_Voltage = target_RYB_Voltage;
	}

	public void setTarget_RYB_Current(String target_RYB_Current) {
		this.target_RYB_Current = target_RYB_Current;
	}

	public void setTarget_RYB_Pf(String target_RYB_Pf) {
		this.target_RYB_Pf = target_RYB_Pf;
	}

	public void setTarget_R_Voltage(String target_R_Voltage) {
		this.target_R_Voltage = target_R_Voltage;
	}

	public void setTarget_Y_Voltage(String target_Y_Voltage) {
		this.target_Y_Voltage = target_Y_Voltage;
	}

	public void setTarget_B_Voltage(String target_B_Voltage) {
		this.target_B_Voltage = target_B_Voltage;
	}

	public void setTarget_R_Current(String target_R_Current) {
		this.target_R_Current = target_R_Current;
	}

	public void setTarget_Y_Current(String target_Y_Current) {
		this.target_Y_Current = target_Y_Current;
	}

	public void setTarget_B_Current(String target_B_Current) {
		this.target_B_Current = target_B_Current;
	}

	public void setTarget_R_Pf(String target_R_Pf) {
		this.target_R_Pf = target_R_Pf;
	}

	public void setTarget_Y_Pf(String target_Y_Pf) {
		this.target_Y_Pf = target_Y_Pf;
	}

	public void setTarget_B_Pf(String target_B_Pf) {
		this.target_B_Pf = target_B_Pf;
	}

	public void setRunType(String runType) {
		this.runType = runType;
	}

	public void setMaxErrorAllowed(String maxErrorAllowed) {
		this.maxErrorAllowed = maxErrorAllowed;
	}

	public void setMinErrorAllowed(String minErrorAllowed) {
		this.minErrorAllowed = minErrorAllowed;
	}

	public void setTargetNoOfPulses(int targetNoOfPulses) {
		this.targetNoOfPulses = targetNoOfPulses;
	}

	public void setReadingId(int readingId) {
		this.readingId = readingId;
	}

	public void setTestSubType(String testSubType) {
		this.testSubType = testSubType;
	}

	public void setTargetAverageCount(int targetAverageCount) {
		this.targetAverageCount = targetAverageCount;
	}
	
}
