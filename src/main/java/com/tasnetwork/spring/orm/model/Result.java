package com.tasnetwork.spring.orm.model;

import javax.persistence.*;

@Entity
@Table(name = "Result")
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(45)")
    private String testPointName;
    
    @Column(columnDefinition = "VARCHAR(45)")
    private String fanSerialNumber;

    @Column(columnDefinition = "VARCHAR(45)")
    private String rpm;

    @Column(columnDefinition = "VARCHAR(45)")
    private String windSpeed;
    
    @Column(columnDefinition = "VARCHAR(45)")
    private String vibration;

    @Column(columnDefinition = "VARCHAR(45)")
    private String current;

    @Column(columnDefinition = "VARCHAR(45)")
    private String watts;

    @Column(columnDefinition = "VARCHAR(45)")
    private String va;

    @Column(columnDefinition = "VARCHAR(45)")
    private String powerFactor;

    @Column(columnDefinition = "VARCHAR(45)")
    private String testStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_run_id")
    private ProjectRun projectRun;

    public Result() {
    }

    public Result(String fanSerialNo, String testPointName, String rpm, String windSpeed, String vibration, String current,
                  String watts, String va, String powerFactor, String testStatus,
                  ProjectRun projectRun) {
    	this.fanSerialNumber = fanSerialNo;
        this.testPointName = testPointName;
        this.rpm = rpm;
        this.windSpeed = windSpeed;
        this.vibration = vibration;
        this.current = current;
        this.watts = watts;
        this.va = va;
        this.powerFactor = powerFactor;
        this.testStatus = testStatus;
        this.projectRun = projectRun;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getTestPointName() {
        return testPointName;
    }

    public void setTestPointName(String testPointName) {
        this.testPointName = testPointName;
    }

    public String getFanSerialNumber() {
		return fanSerialNumber;
	}

	public void setFanSerialNumber(String fanSerialNumber) {
		this.fanSerialNumber = fanSerialNumber;
	}

	public String getRpm() {
        return rpm;
    }

    public void setRpm(String rpm) {
        this.rpm = rpm;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getVibration() {
		return vibration;
	}

	public void setVibration(String vibration) {
		this.vibration = vibration;
	}

	public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getWatts() {
        return watts;
    }

    public void setWatts(String watts) {
        this.watts = watts;
    }

    public String getVa() {
        return va;
    }

    public void setVa(String va) {
        this.va = va;
    }

    public String getPowerFactor() {
        return powerFactor;
    }

    public void setPowerFactor(String powerFactor) {
        this.powerFactor = powerFactor;
    }

    public String getTestStatus() {
        return testStatus;
    }

    public void setTestStatus(String testStatus) {
        this.testStatus = testStatus;
    }

    public ProjectRun getProjectRun() {
        return projectRun;
    }

    public void setProjectRun(ProjectRun projectRun) {
        this.projectRun = projectRun;
    }

    @Override
    public String toString() {
        return "Result{" +
                "id=" + id +
                ", fanSerialNo='" + fanSerialNumber + '\'' +
                ", testPointName='" + testPointName + '\'' +
                ", rpm='" + rpm + '\'' +
                ", windSpeed='" + windSpeed + '\'' +
                ", vibration='" + vibration + '\'' + 
                ", current='" + current + '\'' +
                ", watts='" + watts + '\'' +
                ", va='" + va + '\'' +
                ", powerFactor='" + powerFactor + '\'' +
                ", testStatus='" + testStatus + '\'' +
                ", projectRun=" + (projectRun != null ? projectRun.getTestRunId() : null) +
                '}';
    }
}
