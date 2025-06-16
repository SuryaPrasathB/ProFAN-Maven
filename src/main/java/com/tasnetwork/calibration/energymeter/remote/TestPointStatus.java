package com.tasnetwork.calibration.energymeter.remote;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
//Main class
import java.util.function.Predicate;
public class TestPointStatus {
	    @JsonProperty("presentTestPointId")
	    private String presentTestPointId="";
	    
	    @JsonProperty("allTestExecutionCompleted")
	    private boolean allTestExecutionCompleted=false;
	    
	    private boolean allTestExecutionCompletedCheck=false;
	    
	    /*
	     * DOUBTS
	     * Need JSON Property ?
	     */
	    /*private boolean dutCalibrationVoltageTargetSet   = false;
	    private boolean dutCalibrationCurrentTargetSet   = false; 
		private boolean dutCalibrationCurrentZeroSet     = false; 
		private boolean dutCalibrationVoltCurrentSetZero = false; */
	    
	    @JsonProperty("presentTpResult")
	    private ArrayList<TestResult> presentTpResult = new ArrayList<TestResult> ();

	    // Getters and Setters
	    public String getPresentTestPointId() {
	        return presentTestPointId;
	    }

	    public void setPresentTestPointId(String presentTestPointId) {
	        this.presentTestPointId = presentTestPointId;
	    }

	    public boolean isAllTestExecutionCompleted() {
	        return allTestExecutionCompleted;
	    }

	    public void setAllTestExecutionCompleted(boolean testExecutionStatus) {
	        this.allTestExecutionCompleted = testExecutionStatus;
	    }

	    public ArrayList<TestResult> getPresentTpResult() {
	        return presentTpResult;
	    }

	    public void setPresentTpResult(ArrayList<TestResult> presentTpResult) {
	        this.presentTpResult = presentTpResult;
	    }

		public boolean isAllTestExecutionCompletedCheck() {
			return allTestExecutionCompletedCheck;
		}

		public void setAllTestExecutionCompletedCheck(boolean allTestExecutionCompletedCheck) {
			this.allTestExecutionCompletedCheck = allTestExecutionCompletedCheck;
		}
		
		
	}

	// Sub-class for test results
	