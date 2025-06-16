package com.tasnetwork.spring.orm.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "DutCommand")
public class DutCommand {

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
	
	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
	private Boolean active = false;
	
	@Column(columnDefinition = "VARCHAR(45)", nullable = false) 
	private String projectName = ""; // energy meter or Fan
	
	@Column(columnDefinition = "VARCHAR(45)", nullable = false) 
	private String testCaseName = "";
	
	@Column(columnDefinition = "VARCHAR(45)", nullable = false) 
	private String testType = "";	
	
	@Column(columnDefinition = "VARCHAR(45)", nullable = false) 
	private String testAliasId = "";	
	
	@Column(columnDefinition = "VARCHAR(45)", nullable = false) 
	private String testPositionId = "";
	
	@Column(columnDefinition = "VARCHAR(45)", nullable = false) 
	private String targetCmd = "";
	
	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
	private Boolean  targetCmdInHex = false;
	
	@Column(columnDefinition = "VARCHAR(45)", nullable = false) 
	private String targetCmdTerminator = "";
	
	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
	private Boolean  targetCmdTerminatorInHex = false;
	
	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
	private Boolean  responseMandatory = false;
	
	
	@Column(columnDefinition = "VARCHAR(45)", nullable = false) 
	private String responseExpectedData = "";
	
	@Column(columnDefinition = "VARCHAR(45)", nullable = false) 
	private String responseTerminator = "";	
	
	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
	private Boolean  responseTerminatorInHex = false;
	
	
	@Column(columnDefinition = "INT(45)", nullable = false) 
	private int responseTimeOutInSec = 10;	
	
	@Column(columnDefinition = "INT(45)", nullable = false) 
	private int responseAsciiLength = 10;	
	
	@Column(columnDefinition = "INT(45)", nullable = false) 
	private int haltTimeInSec = 10;	

	@Column(columnDefinition = "INT(45)", nullable = false) 
	private int totalDutExecutionTimeInSec = 70;	
	
	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
	private Boolean  writeSerialNoToDut = false;
	
	@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
	@Type(type = "yes_no")
	private Boolean  readSerialNoFromDut = false;
	
	
	@Column(columnDefinition = "VARCHAR(45)", nullable = false) 
	private String serialNoSourceType = "";


	public Long getId() {
		return id;
	}


	public Boolean getActive() {
		return active;
	}


	public String getProjectName() {
		return projectName;
	}


	public String getTestCaseName() {
		return testCaseName;
	}


	public String getTestType() {
		return testType;
	}


	public String getTestAliasId() {
		return testAliasId;
	}


	public String getTestPositionId() {
		return testPositionId;
	}


	public String getTargetCmd() {
		return targetCmd;
	}


	public Boolean isTargetCmdInHex() {
		return targetCmdInHex;
	}


	public String getTargetCmdTerminator() {
		return targetCmdTerminator;
	}


	public Boolean isTargetCmdTerminatorInHex() {
		return targetCmdTerminatorInHex;
	}


	public Boolean isResponseMandatory() {
		return responseMandatory;
	}


	public String getResponseExpectedData() {
		return responseExpectedData;
	}


	public String getResponseTerminator() {
		return responseTerminator;
	}


	public Boolean getResponseTerminatorInHex() {
		return responseTerminatorInHex;
	}


	public int getResponseTimeOutInSec() {
		return responseTimeOutInSec;
	}


	public int getResponseAsciiLength() {
		return responseAsciiLength;
	}


	public int getHaltTimeInSec() {
		return haltTimeInSec;
	}


	public int getTotalDutExecutionTimeInSec() {
		return totalDutExecutionTimeInSec;
	}


	public Boolean isWriteSerialNoToDut() {
		return writeSerialNoToDut;
	}


	public Boolean isReadSerialNoFromDut() {
		return readSerialNoFromDut;
	}


	public String getSerialNoSourceType() {
		return serialNoSourceType;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public void setActive(Boolean active) {
		this.active = active;
	}


	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}


	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}


	public void setTestType(String testType) {
		this.testType = testType;
	}


	public void setTestAliasId(String testAliasId) {
		this.testAliasId = testAliasId;
	}


	public void setTestPositionId(String testPositionId) {
		this.testPositionId = testPositionId;
	}


	public void setTargetCmd(String targetCmd) {
		this.targetCmd = targetCmd;
	}


	public void setTargetCmdInHex(Boolean targetCmdInHex) {
		this.targetCmdInHex = targetCmdInHex;
	}


	public void setTargetCmdTerminator(String targetCmdTerminator) {
		this.targetCmdTerminator = targetCmdTerminator;
	}


	public void setTargetCmdTerminatorInHex(Boolean targetCmdTerminatorInHex) {
		this.targetCmdTerminatorInHex = targetCmdTerminatorInHex;
	}


	public void setResponseMandatory(Boolean responseMandatory) {
		this.responseMandatory = responseMandatory;
	}


	public void setResponseExpectedData(String responseExpectedData) {
		this.responseExpectedData = responseExpectedData;
	}


	public void setResponseTerminator(String responseTerminator) {
		this.responseTerminator = responseTerminator;
	}


	public void setResponseTerminatorInHex(Boolean responseTerminatorInHex) {
		this.responseTerminatorInHex = responseTerminatorInHex;
	}


	public void setResponseTimeOutInSec(int responseTimeOutInSec) {
		this.responseTimeOutInSec = responseTimeOutInSec;
	}


	public void setResponseAsciiLength(int responseAsciiLength) {
		this.responseAsciiLength = responseAsciiLength;
	}


	public void setHaltTimeInSec(int haltTimeInSec) {
		this.haltTimeInSec = haltTimeInSec;
	}


	public void setTotalDutExecutionTimeInSec(int totalDutExecutionTimeInSec) {
		this.totalDutExecutionTimeInSec = totalDutExecutionTimeInSec;
	}


	public void setWriteSerialNoToDut(Boolean writeSerialNoToDut) {
		this.writeSerialNoToDut = writeSerialNoToDut;
	}


	public void setReadSerialNoFromDut(Boolean readSerialNoFromDut) {
		this.readSerialNoFromDut = readSerialNoFromDut;
	}


	public void setSerialNoSourceType(String serialNoSourceType) {
		this.serialNoSourceType = serialNoSourceType;
	}	

	@Override
	public String toString() {
	    return "DutCommand{" +
	            "id=" + id +
	            ", createdAt=" + createdAt +
	            ", updatedAt=" + updatedAt +
	            ", active=" + active +
	            ", projectName='" + projectName + '\'' +
	            ", testCaseName='" + testCaseName + '\'' +
	            ", testType='" + testType + '\'' +
	            ", testAliasId='" + testAliasId + '\'' +
	            ", testPositionId='" + testPositionId + '\'' +
	            ", targetCmd='" + targetCmd + '\'' +
	            ", targetCmdInHex=" + targetCmdInHex +
	            ", targetCmdTerminator='" + targetCmdTerminator + '\'' +
	            ", targetCmdTerminatorInHex=" + targetCmdTerminatorInHex +
	            ", responseMandatory=" + responseMandatory +
	            ", responseExpectedData='" + responseExpectedData + '\'' +
	            ", responseTerminator='" + responseTerminator + '\'' +
	            ", responseTerminatorInHex=" + responseTerminatorInHex +
	            ", responseTimeOutInSec=" + responseTimeOutInSec +
	            ", responseAsciiLength=" + responseAsciiLength +
	            ", haltTimeInSec=" + haltTimeInSec +
	            ", totalDutExecutionTimeInSec=" + totalDutExecutionTimeInSec +
	            ", writeSerialNoToDut=" + writeSerialNoToDut +
	            ", readSerialNoFromDut=" + readSerialNoFromDut +
	            ", serialNoSourceType='" + serialNoSourceType + '\'' +
	            '}';
	}

}
