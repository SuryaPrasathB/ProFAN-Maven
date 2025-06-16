package com.tasnetwork.calibration.energymeter.deployment;

import javafx.beans.property.SimpleStringProperty;

public class ScheduledProjectModel {
	
	private SimpleStringProperty projectName;
	private SimpleStringProperty scheduledTime;


	public ScheduledProjectModel(final String projectName,final String scheduledTime) {
		this.projectName = new SimpleStringProperty(projectName);
		this.scheduledTime = new SimpleStringProperty(scheduledTime);
	}
	public String getProjectName() {
		return projectName.get();
	}

	public void setProjectName(String ProjectName) {
		this.projectName.set(ProjectName);
	}
	
	public String getScheduledTime() {
		return scheduledTime.get();
	}

	public void setScheduledTime(String ScheduledTime) {
		this.scheduledTime.set(ScheduledTime);
	}
	public SimpleStringProperty projectNameProperty() {
		return projectName;
	}
	public SimpleStringProperty scheduledTimeProperty() {
		return scheduledTime;
	}
}