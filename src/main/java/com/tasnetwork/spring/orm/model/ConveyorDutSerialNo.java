package com.tasnetwork.spring.orm.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "ConveyorDutSerialNumber")
public class ConveyorDutSerialNo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private Long id;
	
	@Column( updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date createdAt;

	@Column()
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date updatedAt;
	
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String testRunId="";

	@Column(columnDefinition = "VARCHAR(45)") 
	private String deploymentId="";
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String dutSerialNo="";
	
	@Column(columnDefinition = "VARCHAR(45)") 
	private String positionId="";
	
	/*@Transient
	private SimpleStringProperty deploymentIdProperty = new SimpleStringProperty();

	@Column(columnDefinition = "VARCHAR(45)") 
	private String projectName;
	@Transient
	private SimpleStringProperty projectNameProperty = new SimpleStringProperty();

	@Column(columnDefinition = "DATETIME") 
	private String startTime;
	@Transient
	private SimpleStringProperty startTimeProperty = new SimpleStringProperty();

	@Column(columnDefinition = "DATETIME") 
	private String endTime;
	@Transient
	private SimpleStringProperty endTimeProperty = new SimpleStringProperty();

	@Column(columnDefinition = "VARCHAR(45)") 
	private String executionStatus;
	@Transient
	private SimpleStringProperty executionStatusProperty = new SimpleStringProperty();

	@Column(columnDefinition = "INT") 
	private long epochStartTime;
	@Transient
	private SimpleStringProperty epochStartTimeProperty = new SimpleStringProperty();

	@Column(columnDefinition = "INT") 
	private long epochEndTime;
	@Transient
	private SimpleStringProperty epochEndTimeProperty = new SimpleStringProperty();
*/
	public ConveyorDutSerialNo () {

	}

	public Long getId() {
		return id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public String getTestRunId() {
		return testRunId;
	}

	public String getDeploymentId() {
		return deploymentId;
	}

	public String getDutSerialNo() {
		return dutSerialNo;
	}

	public String getPositionId() {
		return positionId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public void setTestRunId(String testRunId) {
		this.testRunId = testRunId;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}

	public void setDutSerialNo(String dutSerialNo) {
		this.dutSerialNo = dutSerialNo;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	/*public ConveyorDutSerialNumber (
			Long testrunId,
			String deploymentId,
			String projectName,
			String startTime,
			String endTime,
			String executionStatus,
			Integer epochStartTime,
			Integer epochEndTime
			) 
	{
		this.testRunId = testrunId;
		this.deploymentId = deploymentId;
		this.projectName = projectName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.executionStatus = executionStatus;
		this.epochStartTime = epochStartTime;
		this.epochEndTime = epochEndTime;
	}

	@Override
	public String toString() {
		return "ProjectRun {" +
				"testrunId=" + testRunId +
				", deploymentId='" + deploymentId + '\'' +
				", projectName='" + projectName + '\'' +
				", startTime='" + startTime + '\'' +
				", endTime='" + endTime + '\'' +
				", executionStatus='" + executionStatus + '\'' +
				", epochStartTime=" + epochStartTime +
				", epochEndTime=" + epochEndTime +
				'}';
	}

	public Long getTestRunId() {
		return testRunId;
	}



	public String getDeploymentId() {
		return deploymentId;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}

	public SimpleStringProperty getDeploymentIdProperty() {
		return deploymentIdProperty;
	}

	public void setDeploymentIdProperty(SimpleStringProperty deploymentIdProperty) {
		this.deploymentIdProperty = deploymentIdProperty;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public SimpleStringProperty getProjectNameProperty() {
		return projectNameProperty;
	}

	public void setProjectNameProperty(SimpleStringProperty projectNameProperty) {
		this.projectNameProperty = projectNameProperty;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public SimpleStringProperty getStartTimeProperty() {
		return startTimeProperty;
	}

	public void setStartTimeProperty(SimpleStringProperty startTimeProperty) {
		this.startTimeProperty = startTimeProperty;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public SimpleStringProperty getEndTimeProperty() {
		return endTimeProperty;
	}

	public void setEndTimeProperty(SimpleStringProperty endTimeProperty) {
		this.endTimeProperty = endTimeProperty;
	}

	public String getExecutionStatus() {
		return executionStatus;
	}

	public void setExecutionStatus(String executionStatus) {
		this.executionStatus = executionStatus;
	}

	public SimpleStringProperty getExecutionStatusProperty() {
		return executionStatusProperty;
	}

	public void setExecutionStatusProperty(SimpleStringProperty executionStatusProperty) {
		this.executionStatusProperty = executionStatusProperty;
	}

	public long getEpochStartTime() {
		return epochStartTime;
	}

	public void setEpochStartTime(long epochStartTime) {
		this.epochStartTime = epochStartTime;
	}

	public SimpleStringProperty getEpochStartTimeProperty() {
		return epochStartTimeProperty;
	}

	public void setEpochStartTimeProperty(SimpleStringProperty epochStartTimeProperty) {
		this.epochStartTimeProperty = epochStartTimeProperty;
	}

	public long getEpochEndTime() {
		return epochEndTime;
	}

	public void setEpochEndTime(long epochEndTime) {
		this.epochEndTime = epochEndTime;
	}

	public SimpleStringProperty getEpochEndTimeProperty() {
		return epochEndTimeProperty;
	}

	public void setEpochEndTimeProperty(SimpleStringProperty epochEndTimeProperty) {
		this.epochEndTimeProperty = epochEndTimeProperty;
	}

	public void setTestRunId(Long testRunId) {
		this.testRunId = testRunId;
	}
*/

}
