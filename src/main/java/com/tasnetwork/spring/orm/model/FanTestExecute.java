package com.tasnetwork.spring.orm.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "FanTestExecute")
public class FanTestExecute {
	
	
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

		@ManyToOne
		@JoinColumn(name = "dut_master_data_id")
		private DutMasterData dutMasterData;
		
		@Column(columnDefinition = "VARCHAR(45)", nullable = false) 
		private String status = "";
	    @Transient
		private SimpleStringProperty statusProperty = new SimpleStringProperty();

	    @Column(columnDefinition = "VARCHAR(45)", nullable = false) 
		private String testPointName = "";
	    @Transient
		private SimpleStringProperty testPointNameProperty = new SimpleStringProperty();
	    

	    @Column(columnDefinition = "VARCHAR(45)", nullable = false) 
		private String targetVoltage = "";
	    @Transient
		private SimpleStringProperty targetVoltageProperty = new SimpleStringProperty();
	    

	    @Column(columnDefinition = "INT(45)", nullable = false) 
		private int setupTimeInSec = 60;
	    @Transient
		private SimpleIntegerProperty setupTimeInSecProperty = new SimpleIntegerProperty();
	    
	    @Column(columnDefinition = "INT(45)", nullable = false) 
	    private int serialNo = 0;
	    @Transient
		private SimpleIntegerProperty serialNoProperty = new SimpleIntegerProperty();
	    
	    @Column(columnDefinition = "VARCHAR(45)", nullable = false) 
		private String rpmActual = "";
	    @Transient
		private SimpleStringProperty rpmActualProperty = new SimpleStringProperty();
	    
	    @Column(columnDefinition = "VARCHAR(45)", nullable = false) 
		private String windSpeedActual = "";
	    @Transient
		private SimpleStringProperty windSpeedActualProperty = new SimpleStringProperty();
	    
	    @Column(columnDefinition = "VARCHAR(45)", nullable = false) 
		private String currentActual = "";
	    @Transient
		private SimpleStringProperty currentActualProperty = new SimpleStringProperty();
	    
	    @Column(columnDefinition = "VARCHAR(45)", nullable = false) 
		private String wattsActual = "";
	    @Transient
		private SimpleStringProperty wattsActualProperty = new SimpleStringProperty();
	    
	    @Column(columnDefinition = "VARCHAR(45)", nullable = false) 
		private String vaActual = "";
	    @Transient
		private SimpleStringProperty vaActualProperty = new SimpleStringProperty();
	    
	    @Column(columnDefinition = "VARCHAR(45)", nullable = false) 
		private String pfActual = "";
	    @Transient
		private SimpleStringProperty pfActualProperty = new SimpleStringProperty();
	    
		public Long getId() {
			return id;
		}
		public Date getCreatedAt() {
			return createdAt;
		}
		public Date getUpdatedAt() {
			return updatedAt;
		}
		public String getTestPointName() {
			return testPointName;
		}
		public SimpleStringProperty getTestPointNameProperty() {
			testPointNameProperty.set(testPointName);
			return testPointNameProperty;
		}
		public String getTargetVoltage() {
			return targetVoltage;
		}
		public SimpleStringProperty getTargetVoltageProperty() {
			targetVoltageProperty.set(targetVoltage);
			return targetVoltageProperty;
		}
		public int getExecuteTimeInSec() {
			return setupTimeInSec;
		}
		public SimpleIntegerProperty getExecuteTimeInSecProperty() {
			setupTimeInSecProperty.set(setupTimeInSec);
			return setupTimeInSecProperty;
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
		public void setTestPointName(String testPointName) {
			this.testPointName = testPointName;
		}
		public void setTestPointNameProperty(SimpleStringProperty testPointNameProperty) {
			this.testPointNameProperty = testPointNameProperty;
		}
		public void setTargetVoltage(String targetVoltage) {
			this.targetVoltage = targetVoltage;
		}
		public void setTargetVoltageProperty(SimpleStringProperty targetVoltageProperty) {
			this.targetVoltageProperty = targetVoltageProperty;
		}
		public void setExecuteTimeInSec(int setupTimeInSec) {
			this.setupTimeInSec = setupTimeInSec;
		}
		public void setExecuteTimeInSecProperty(SimpleIntegerProperty setupTimeInSecProperty) {
			this.setupTimeInSecProperty = setupTimeInSecProperty;
		}
		public int getSerialNo() {
			return serialNo;
		}
		public SimpleIntegerProperty getSerialNoProperty() {
			serialNoProperty.set(serialNo);
			return serialNoProperty;
		}
		public void setSerialNo(int serialNo) {
			this.serialNo = serialNo;
		}
		public void setSerialNoProperty(SimpleIntegerProperty serialNoProperty) {
			this.serialNoProperty = serialNoProperty;
		}
		
		public DutMasterData getDutMasterData() {
		    return dutMasterData;
		}

		public void setDutMasterData(DutMasterData dutMasterData) {
		    this.dutMasterData = dutMasterData;
		}
		public int getSetupTimeInSec() {
			return setupTimeInSec;
		}
		public void setSetupTimeInSec(int setupTimeInSec) {
			this.setupTimeInSec = setupTimeInSec;
		}
		public SimpleIntegerProperty getSetupTimeInSecProperty() {
			return setupTimeInSecProperty;
		}
		public void setSetupTimeInSecProperty(SimpleIntegerProperty setupTimeInSecProperty) {
			this.setupTimeInSecProperty = setupTimeInSecProperty;
		}
		public String getRpmActual() {
			return rpmActual;
		}
		public void setRpmActual(String rpmActual) {
			this.rpmActual = rpmActual;
		}
		public SimpleStringProperty getRpmActualProperty() {
			return rpmActualProperty;
		}
		public void setRpmActualProperty(SimpleStringProperty rpmActualProperty) {
			this.rpmActualProperty = rpmActualProperty;
		}
		public String getWindSpeedActual() {
			return windSpeedActual;
		}
		public void setWindSpeedActual(String windSpeedActual) {
			this.windSpeedActual = windSpeedActual;
		}
		public SimpleStringProperty getWindSpeedActualProperty() {
			return windSpeedActualProperty;
		}
		public void setWindSpeedActualProperty(SimpleStringProperty windSpeedActualProperty) {
			this.windSpeedActualProperty = windSpeedActualProperty;
		}
		public String getCurrentActual() {
			return currentActual;
		}
		public void setCurrentActual(String currentActual) {
			this.currentActual = currentActual;
		}
		public SimpleStringProperty getCurrentActualProperty() {
			return currentActualProperty;
		}
		public void setCurrentActualProperty(SimpleStringProperty currentActualProperty) {
			this.currentActualProperty = currentActualProperty;
		}
		public String getWattsActual() {
			return wattsActual;
		}
		public void setWattsActual(String wattsActual) {
			this.wattsActual = wattsActual;
		}
		public SimpleStringProperty getWattsActualProperty() {
			return wattsActualProperty;
		}
		public void setWattsActualProperty(SimpleStringProperty wattsActualProperty) {
			this.wattsActualProperty = wattsActualProperty;
		}
		public String getVaActual() {
			return vaActual;
		}
		public void setVaActual(String vaActual) {
			this.vaActual = vaActual;
		}
		public SimpleStringProperty getVaActualProperty() {
			return vaActualProperty;
		}
		public void setVaActualProperty(SimpleStringProperty vaActualProperty) {
			this.vaActualProperty = vaActualProperty;
		}
		public String getPfActual() {
			return pfActual;
		}
		public void setPfActual(String pfActual) {
			this.pfActual = pfActual;
		}
		public SimpleStringProperty getPfActualProperty() {
			return pfActualProperty;
		}
		public void setPfActualProperty(SimpleStringProperty pfActualProperty) {
			this.pfActualProperty = pfActualProperty;
		}
		
}
