package com.tasnetwork.calibration.energymeter.remote;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TestResult {
    @JsonProperty("positionId")
    private int positionId=0;

    @JsonProperty("resultValue")
    private String resultValue="";
    
    @JsonProperty("resultStatus")
    private String resultStatus="";

    // Getters and Setters
    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    public String getResultValue() {
        return resultValue;
    }

    public void setResultValue(String resultValue) {
        this.resultValue = resultValue;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }
} 
