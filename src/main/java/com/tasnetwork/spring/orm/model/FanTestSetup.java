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

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "FanTestSetup")
public class FanTestSetup {
	
	
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
		@Transient
		private SimpleBooleanProperty activeProperty = new SimpleBooleanProperty();

		@ManyToOne
		@JoinColumn(name = "dut_master_data_id")
		private DutMasterData dutMasterData;
		
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
	    

	    @Column(columnDefinition = "VARCHAR(45)", nullable = false) 
		private String rpmLowerLimit = "";
	    @Transient
		private SimpleStringProperty rpmLowerLimitProperty = new SimpleStringProperty();
	    
	    @Column(columnDefinition = "VARCHAR(45)", nullable = false) 
		private String rpmUpperLimit = "";
	    @Transient
		private SimpleStringProperty rpmUpperLimitProperty = new SimpleStringProperty();
	    
	    @Column(columnDefinition = "VARCHAR(45)", nullable = false) 
		private String rpmActual = "";
	    @Transient
		private SimpleStringProperty rpmActualProperty = new SimpleStringProperty();	    
	    
	    @Column(columnDefinition = "VARCHAR(45)", nullable = false) 
		private String windSpeedLowerLimit = "";
	    @Transient
		private SimpleStringProperty windSpeedLowerLimitProperty = new SimpleStringProperty();
	    
	    @Column(columnDefinition = "VARCHAR(45)", nullable = false) 
		private String windSpeedUpperLimit = "";
	    @Transient
		private SimpleStringProperty windSpeedUpperLimitProperty = new SimpleStringProperty();
	    
	    @Column(columnDefinition = "VARCHAR(45)", nullable = false) 
		private String windSpeedActual = "";
	    @Transient
		private SimpleStringProperty windSpeedActualProperty = new SimpleStringProperty();
	    
	    @Column(columnDefinition = "VARCHAR(45)", nullable = false) 
		private String vibrationLowerLimit = "";
	    @Transient
		private SimpleStringProperty vibrationLowerLimitProperty = new SimpleStringProperty();
	    
	    @Column(columnDefinition = "VARCHAR(45)", nullable = false) 
		private String vibrationUpperLimit = "";
	    @Transient
		private SimpleStringProperty vibrationUpperLimitProperty = new SimpleStringProperty();
	    
	    @Column(columnDefinition = "VARCHAR(45)", nullable = false) 
		private String vibrationActual = "";
	    @Transient
		private SimpleStringProperty vibrationActualProperty = new SimpleStringProperty();
	    
	    @Column(columnDefinition = "VARCHAR(45)", nullable = false) 
		private String currentLowerLimit = "";
	    @Transient
		private SimpleStringProperty currentLowerLimitProperty = new SimpleStringProperty();
	    
	    @Column(columnDefinition = "VARCHAR(45)", nullable = false) 
		private String currentUpperLimit = "";
	    @Transient
		private SimpleStringProperty currentUpperLimitProperty = new SimpleStringProperty();
	    
	    @Column(columnDefinition = "VARCHAR(45)", nullable = false) 
		private String currentActual = "";
	    @Transient
		private SimpleStringProperty currentActualProperty = new SimpleStringProperty();
	    
	    @Column(columnDefinition = "VARCHAR(45)", nullable = false) 
		private String wattsLowerLimit = "";
	    @Transient
		private SimpleStringProperty wattsLowerLimitProperty = new SimpleStringProperty();
	    
	    @Column(columnDefinition = "VARCHAR(45)", nullable = false) 
		private String wattsUpperLimit = "";
	    @Transient
		private SimpleStringProperty wattsUpperLimitProperty = new SimpleStringProperty();
	    
	    @Column(columnDefinition = "VARCHAR(45)", nullable = false) 
		private String wattsActual = "";
	    @Transient
		private SimpleStringProperty wattsActualProperty = new SimpleStringProperty();
	    
	    public String getVibrationLowerLimit() {
			return vibrationLowerLimit;
		}

		public void setVibrationLowerLimit(String vibrationLowerLimit) {
			this.vibrationLowerLimit = vibrationLowerLimit;
		}

		public SimpleStringProperty getVibrationLowerLimitProperty() {
			return vibrationLowerLimitProperty;
		}

		public void setVibrationLowerLimitProperty(SimpleStringProperty vibrationLowerLimitProperty) {
			this.vibrationLowerLimitProperty = vibrationLowerLimitProperty;
		}

		public String getVibrationUpperLimit() {
			return vibrationUpperLimit;
		}

		public void setVibrationUpperLimit(String vibrationUpperLimit) {
			this.vibrationUpperLimit = vibrationUpperLimit;
		}

		public SimpleStringProperty getVibrationUpperLimitProperty() {
			return vibrationUpperLimitProperty;
		}

		public void setVibrationUpperLimitProperty(SimpleStringProperty vibrationUpperLimitProperty) {
			this.vibrationUpperLimitProperty = vibrationUpperLimitProperty;
		}

		public String getVibrationActual() {
			return vibrationActual;
		}

		public void setVibrationActual(String vibrationActual) {
			this.vibrationActual = vibrationActual;
		}

		public SimpleStringProperty getVibrationActualProperty() {
			return vibrationActualProperty;
		}

		public void setVibrationActualProperty(SimpleStringProperty vibrationActualProperty) {
			this.vibrationActualProperty = vibrationActualProperty;
		}
		@Column(columnDefinition = "VARCHAR(45)", nullable = false) 
		private String activePowerLowerLimit = "";
	    @Transient
		private SimpleStringProperty activePowerLowerLimitProperty = new SimpleStringProperty();
	    
	    @Column(columnDefinition = "VARCHAR(45)", nullable = false) 
		private String activePowerUpperLimit = "";
	    @Transient
		private SimpleStringProperty activePowerUpperLimitProperty = new SimpleStringProperty();
	    
	    @Column(columnDefinition = "VARCHAR(45)", nullable = false) 
		private String activePowerActual = "";
	    @Transient
		private SimpleStringProperty activePowerActualProperty = new SimpleStringProperty();
	    
	    @Column(columnDefinition = "VARCHAR(45)", nullable = false) 
		private String powerFactorLowerLimit = "";
	    @Transient
		private SimpleStringProperty powerFactorLowerLimitProperty = new SimpleStringProperty();
	    
	    @Column(columnDefinition = "VARCHAR(45)", nullable = false) 
		private String powerFactorUpperLimit = "";
	    @Transient
		private SimpleStringProperty powerFactorUpperLimitProperty = new SimpleStringProperty();
	    
	    @Column(columnDefinition = "VARCHAR(45)", nullable = false) 
		private String powerFactorActual = "";
	    @Transient
		private SimpleStringProperty powerFactorActualProperty = new SimpleStringProperty();
	    
	    @Column(columnDefinition = "INT(45)", nullable = false) 
	    private int serialNo = 0;
	    @Transient
		private SimpleIntegerProperty serialNoProperty = new SimpleIntegerProperty();
	    
	    @Transient
	    private final BooleanProperty isRunning = new SimpleBooleanProperty(false);
	    
	    @Transient
	    private final SimpleStringProperty statusProperty = new SimpleStringProperty("Pending");
	    
	    @Transient
	    private final DoubleProperty progress = new SimpleDoubleProperty(0.0);
	    
	    @Transient
	    private final BooleanProperty rpmBlink = new SimpleBooleanProperty(false);
	    @Transient
	    private final BooleanProperty windSpeedBlink = new SimpleBooleanProperty(false);
	    @Transient
	    private final BooleanProperty currentBlink = new SimpleBooleanProperty(false);
	    @Transient
	    private final BooleanProperty wattsBlink = new SimpleBooleanProperty(false);
	    @Transient
	    private final BooleanProperty vaBlink = new SimpleBooleanProperty(false);
	    @Transient
	    private final BooleanProperty pfBlink = new SimpleBooleanProperty(false);
	    
	    @Transient
	    private SimpleBooleanProperty rpmValid = new SimpleBooleanProperty(true);
	    @Transient
	    private SimpleBooleanProperty windSpeedValid = new SimpleBooleanProperty(true);
	    @Transient
	    private SimpleBooleanProperty vibrationValid = new SimpleBooleanProperty(true);
	    @Transient
	    private SimpleBooleanProperty currentValid = new SimpleBooleanProperty(true);
	    @Transient
	    private SimpleBooleanProperty wattsValid = new SimpleBooleanProperty(true);
	    @Transient
	    private SimpleBooleanProperty activePowerValid = new SimpleBooleanProperty(true);
	    @Transient
	    private SimpleBooleanProperty powerFactorValid = new SimpleBooleanProperty(true);
	    
	    // GETTERS & SETTERS ======================================================================================
	    // ACTUAL VALUES 
	    // Updated setRpmActual method
	    public void setRpmActual(String rpmActual) {
	        this.rpmActual = rpmActual;  // Set the value in the database field
	        this.rpmActualProperty.set(rpmActual);  // Set the value in the StringProperty for JavaFX updates
	    }

	    // Updated setWindSpeedActual method
	    public void setWindSpeedActual(String windSpeedActual) {
	        this.windSpeedActual = windSpeedActual;  // Set the value in the database field
	        this.windSpeedActualProperty.set(windSpeedActual);  // Set the value in the StringProperty for JavaFX updates
	    }
	    
	 // Updated setWindSpeedActual method
	    public void setCurrentActual(String currentActual) {
	        this.currentActual = currentActual;  // Set the value in the database field
	        this.currentActualProperty.set(currentActual);  // Set the value in the StringProperty for JavaFX updates
	    }
	    
	 // Updated setWindSpeedActual method
	    public void setWattsActual(String wattsActual) {
	        this.wattsActual = wattsActual;  // Set the value in the database field
	        this.wattsActualProperty.set(wattsActual);  // Set the value in the StringProperty for JavaFX updates
	    }
	    
	 // Updated setWindSpeedActual method
	    public void setActivePowerActual(String activePowerActual) {
	        this.activePowerActual = activePowerActual;  // Set the value in the database field
	        this.activePowerActualProperty.set(activePowerActual);  // Set the value in the StringProperty for JavaFX updates
	    }
	    
	 // Updated setWindSpeedActual method
	    public void setPowerFactorActual(String powerFactorActual) {
	        this.powerFactorActual = powerFactorActual;  // Set the value in the database field
	        this.powerFactorActualProperty.set(powerFactorActual);  // Set the value in the StringProperty for JavaFX updates
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
		public Boolean getActive() {
			return active;
		}
		public SimpleBooleanProperty getActiveProperty() {
			return activeProperty;
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
		public int getSetupTimeInSec() {
			return setupTimeInSec;
		}
		public SimpleIntegerProperty getSetupTimeInSecProperty() {
			setupTimeInSecProperty.set(setupTimeInSec);
			return setupTimeInSecProperty;
		}
		public String getRpmLowerLimit() {
			return rpmLowerLimit;
		}
		public SimpleStringProperty getRpmLowerLimitProperty() {
			rpmLowerLimitProperty.set(rpmLowerLimit);
			return rpmLowerLimitProperty;
		}
		public String getRpmUpperLimit() {
			return rpmUpperLimit;
		}
		public SimpleStringProperty getRpmUpperLimitProperty() {
			rpmUpperLimitProperty.set(rpmUpperLimit);
			return rpmUpperLimitProperty;
		}
		public String getWindSpeedLowerLimit() {
			return windSpeedLowerLimit;
		}
		public SimpleStringProperty getWindSpeedLowerLimitProperty() {
			windSpeedLowerLimitProperty.set(windSpeedLowerLimit);
			return windSpeedLowerLimitProperty;
		}
		public String getWindSpeedUpperLimit() {
			return windSpeedUpperLimit;
		}
		public SimpleStringProperty getWindSpeedUpperLimitProperty() {
			windSpeedUpperLimitProperty.set(windSpeedUpperLimit);
			return windSpeedUpperLimitProperty;
		}
		public String getCurrentLowerLimit() {
			return currentLowerLimit;
		}
		public SimpleStringProperty getCurrentLowerLimitProperty() {
			currentLowerLimitProperty.set(currentLowerLimit);
			return currentLowerLimitProperty;
		}
		public String getCurrentUpperLimit() {
			return currentUpperLimit;
		}
		public SimpleStringProperty getCurrentUpperLimitProperty() {
			currentUpperLimitProperty.set(currentUpperLimit);
			return currentUpperLimitProperty;
		}
		public String getWattsLowerLimit() {
			return wattsLowerLimit;
		}
		public SimpleStringProperty getWattsLowerLimitProperty() {
			wattsLowerLimitProperty.set(wattsLowerLimit);
			return wattsLowerLimitProperty;
		}
		public String getWattsUpperLimit() {
			return wattsUpperLimit;
		}
		public SimpleStringProperty getWattsUpperLimitProperty() {
			wattsUpperLimitProperty.set(wattsUpperLimit);
			return wattsUpperLimitProperty;
		}
		public String getActivePowerLowerLimit() {
			return activePowerLowerLimit;
		}
		public SimpleStringProperty getActivePowerLowerLimitProperty() {
			activePowerLowerLimitProperty.set(activePowerLowerLimit);
			return activePowerLowerLimitProperty;
		}
		public String getActivePowerUpperLimit() {
			return activePowerUpperLimit;
		}
		public SimpleStringProperty getActivePowerUpperLimitProperty() {
			activePowerUpperLimitProperty.set(activePowerUpperLimit);
			return activePowerUpperLimitProperty;
		}
		public String getPowerFactorLowerLimit() {
			return powerFactorLowerLimit;
		}
		public SimpleStringProperty getPowerFactorLowerLimitProperty() {
			powerFactorLowerLimitProperty.set(powerFactorLowerLimit);
			return powerFactorLowerLimitProperty;
		}
		public String getPowerFactorUpperLimit() {
			return powerFactorUpperLimit;
		}
		public SimpleStringProperty getPowerFactorUpperLimitProperty() {
			powerFactorUpperLimitProperty.set(powerFactorUpperLimit);
			return powerFactorUpperLimitProperty;
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
		public void setActive(Boolean active) {
			this.active = active;
		}
		public void setActiveProperty(SimpleBooleanProperty activeProperty) {
			this.activeProperty = activeProperty;
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
		public void setSetupTimeInSec(int setupTimeInSec) {
			this.setupTimeInSec = setupTimeInSec;
		}
		public void setSetupTimeInSecProperty(SimpleIntegerProperty setupTimeInSecProperty) {
			this.setupTimeInSecProperty = setupTimeInSecProperty;
		}
		public void setRpmLowerLimit(String rpmLowerLimit) {
			this.rpmLowerLimit = rpmLowerLimit;
		}
		public void setRpmLowerLimitProperty(SimpleStringProperty rpmLowerLimitProperty) {
			this.rpmLowerLimitProperty = rpmLowerLimitProperty;
		}
		public void setRpmUpperLimit(String rpmUpperLimit) {
			this.rpmUpperLimit = rpmUpperLimit;
		}
		public void setRpmUpperLimitProperty(SimpleStringProperty rpmUpperLimitProperty) {
			this.rpmUpperLimitProperty = rpmUpperLimitProperty;
		}
		public void setWindSpeedLowerLimit(String windSpeedLowerLimit) {
			this.windSpeedLowerLimit = windSpeedLowerLimit;
		}
		public void setWindSpeedLowerLimitProperty(SimpleStringProperty windSpeedLowerLimitProperty) {
			this.windSpeedLowerLimitProperty = windSpeedLowerLimitProperty;
		}
		public void setWindSpeedUpperLimit(String windSpeedUpperLimit) {
			this.windSpeedUpperLimit = windSpeedUpperLimit;
		}
		public void setWindSpeedUpperLimitProperty(SimpleStringProperty windSpeedUpperLimitProperty) {
			this.windSpeedUpperLimitProperty = windSpeedUpperLimitProperty;
		}
		public void setCurrentLowerLimit(String currentLowerLimit) {
			this.currentLowerLimit = currentLowerLimit;
		}
		public void setCurrentLowerLimitProperty(SimpleStringProperty currentLowerLimitProperty) {
			this.currentLowerLimitProperty = currentLowerLimitProperty;
		}
		public void setCurrentUpperLimit(String currentUpperLimit) {
			this.currentUpperLimit = currentUpperLimit;
		}
		public void setCurrentUpperLimitProperty(SimpleStringProperty currentUpperLimitProperty) {
			this.currentUpperLimitProperty = currentUpperLimitProperty;
		}
		public void setWattsLowerLimit(String wattsLowerLimit) {
			this.wattsLowerLimit = wattsLowerLimit;
		}
		public void setWattsLowerLimitProperty(SimpleStringProperty wattsLowerLimitProperty) {
			this.wattsLowerLimitProperty = wattsLowerLimitProperty;
		}
		public void setWattsUpperLimit(String wattsUpperLimit) {
			this.wattsUpperLimit = wattsUpperLimit;
		}
		public void setWattsUpperLimitProperty(SimpleStringProperty wattsUpperLimitProperty) {
			this.wattsUpperLimitProperty = wattsUpperLimitProperty;
		}
		public void setActivePowerLowerLimit(String activePowerLowerLimit) {
			this.activePowerLowerLimit = activePowerLowerLimit;
		}
		public void setActivePowerLowerLimitProperty(SimpleStringProperty activePowerLowerLimitProperty) {
			this.activePowerLowerLimitProperty = activePowerLowerLimitProperty;
		}
		public void setActivePowerUpperLimit(String activePowerUpperLimit) {
			this.activePowerUpperLimit = activePowerUpperLimit;
		}
		public void setActivePowerUpperLimitProperty(SimpleStringProperty activePowerUpperLimitProperty) {
			this.activePowerUpperLimitProperty = activePowerUpperLimitProperty;
		}
		public void setPowerFactorLowerLimit(String powerFactorLowerLimit) {
			this.powerFactorLowerLimit = powerFactorLowerLimit;
		}
		public void setPowerFactorLowerLimitProperty(SimpleStringProperty powerFactorLowerLimitProperty) {
			this.powerFactorLowerLimitProperty = powerFactorLowerLimitProperty;
		}
		public void setPowerFactorUpperLimit(String powerFactorUpperLimit) {
			this.powerFactorUpperLimit = powerFactorUpperLimit;
		}
		public void setPowerFactorUpperLimitProperty(SimpleStringProperty powerFactorUpperLimitProperty) {
			this.powerFactorUpperLimitProperty = powerFactorUpperLimitProperty;
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
		public String getRpmActual() {
		    return rpmActualProperty.get();
		}
		public StringProperty rpmActualProperty() {
		    return rpmActualProperty;
		}
		public BooleanProperty rpmValidProperty() {
		    return rpmValid;
		}
		public String getWindSpeedActual() {
		    return windSpeedActual;
		}
		public StringProperty windSpeedActualProperty() {
		    return windSpeedActualProperty;
		}

		public String getCurrentActual() {
		    return currentActual;
		}
		public StringProperty currentActualProperty() {
		    return currentActualProperty;
		}

		public String getWattsActual() {
		    return wattsActual;
		}
		public StringProperty wattsActualProperty() {
		    return wattsActualProperty;
		}

		public String getActivePowerActual() {
		    return activePowerActual;
		}
		public StringProperty activePowerActualProperty() {
		    return activePowerActualProperty;
		}

		public String getPowerFactorActual() {
		    return powerFactorActual;
		}
		public StringProperty powerFactorActualProperty() {
		    return powerFactorActualProperty;
		}

		public boolean isRunning() {
		    return isRunning.get();
		}

		public void setIsRunning(boolean value) {
		    isRunning.set(value);
		}

		public BooleanProperty isRunningProperty() {
		    return isRunning;
		}
		
		public String getStatus() {
		    return statusProperty.get();
		}

		public void setStatus(String status) {
		    this.statusProperty.set(status);
		}

		public SimpleStringProperty statusProperty() {
		    return statusProperty;
		}
		
		public double getProgress() {
		    return progress.get();
		}

		public void setProgress(double value) {
		    this.progress.set(value);
		}

		public DoubleProperty progressProperty() {
		    return progress;
		}
		
		public BooleanProperty rpmBlinkProperty() { return rpmBlink; }
		public BooleanProperty windSpeedBlinkProperty() { return windSpeedBlink; }
		public BooleanProperty currentBlinkProperty() { return currentBlink; }
		public BooleanProperty wattsBlinkProperty() { return wattsBlink; }
		public BooleanProperty vaBlinkProperty() { return vaBlink; }
		public BooleanProperty pfBlinkProperty() { return pfBlink; }

		public void setRpmBlink(boolean value) { rpmBlink.set(value); }
		public void setWindSpeedBlink(boolean value) { windSpeedBlink.set(value); }
		public void setCurrentBlink(boolean value) { currentBlink.set(value); }
		public void setWattsBlink(boolean value) { wattsBlink.set(value); }
		public void setVaBlink(boolean value) { vaBlink.set(value); }
		public void setPfBlink(boolean value) { pfBlink.set(value); }
		
		public SimpleBooleanProperty getRpmValid() {
			return rpmValid;
		}
		public SimpleBooleanProperty getActivePowerValid() {
			return activePowerValid;
		}

		public void setActivePowerValid(SimpleBooleanProperty activePowerValid) {
			this.activePowerValid = activePowerValid;
		}

		public SimpleBooleanProperty getPowerFactorValid() {
			return powerFactorValid;
		}

		public void setPowerFactorValid(SimpleBooleanProperty powerFactorValid) {
			this.powerFactorValid = powerFactorValid;
		}

		public SimpleBooleanProperty getWindSpeedValid() {
			return windSpeedValid;
		}

		public SimpleBooleanProperty getVibrationValid() {
			return vibrationValid;
		}

		public void setVibrationValid(SimpleBooleanProperty vibrationValid) {
			this.vibrationValid = vibrationValid;
		}

		public SimpleBooleanProperty getCurrentValid() {
			return currentValid;
		}

		public SimpleBooleanProperty getWattsValid() {
			return wattsValid;
		}

		public void setRpmValid(SimpleBooleanProperty rpmValid) {
			this.rpmValid = rpmValid;
		}
		public SimpleBooleanProperty windSpeedValidProperty() {
			return windSpeedValid;
		}
		public void setWindSpeedValid(SimpleBooleanProperty windSpeedValid) {
			this.windSpeedValid = windSpeedValid;
		}
		public SimpleBooleanProperty currentValidProperty() {
			return currentValid;
		}
		public void setCurrentValid(SimpleBooleanProperty currentValid) {
			this.currentValid = currentValid;
		}
		public SimpleBooleanProperty wattsValidProperty() {
			return wattsValid;
		}
		public void setWattsValid(SimpleBooleanProperty wattsValid) {
			this.wattsValid = wattsValid;
		}
		public SimpleBooleanProperty vaValidProperty() {
			return activePowerValid;
		}
		public void setVaValid(SimpleBooleanProperty vaValid) {
			this.activePowerValid = vaValid;
		}
		public SimpleBooleanProperty pfValidProperty() {
			return powerFactorValid;
		}
		public void setPfValid(SimpleBooleanProperty pfValid) {
			this.powerFactorValid = pfValid;
		}
		
		public SimpleStringProperty getRpmActualProperty() {
			return rpmActualProperty;
		}
		public void setRpmActualProperty(SimpleStringProperty rpmActualProperty) {
			this.rpmActualProperty = rpmActualProperty;
		}
		public SimpleStringProperty getWindSpeedActualProperty() {
			return windSpeedActualProperty;
		}
		public void setWindSpeedActualProperty(SimpleStringProperty windSpeedActualProperty) {
			this.windSpeedActualProperty = windSpeedActualProperty;
		}
		public SimpleStringProperty getCurrentActualProperty() {
			return currentActualProperty;
		}
		public void setCurrentActualProperty(SimpleStringProperty currentActualProperty) {
			this.currentActualProperty = currentActualProperty;
		}
		public SimpleStringProperty getWattsActualProperty() {
			return wattsActualProperty;
		}
		public void setWattsActualProperty(SimpleStringProperty wattsActualProperty) {
			this.wattsActualProperty = wattsActualProperty;
		}
		public SimpleStringProperty getActivePowerActualProperty() {
			return activePowerActualProperty;
		}
		public void setActivePowerActualProperty(SimpleStringProperty activePowerActualProperty) {
			this.activePowerActualProperty = activePowerActualProperty;
		}
		public SimpleStringProperty getPowerFactorActualProperty() {
			return powerFactorActualProperty;
		}
		public void setPowerFactorActualProperty(SimpleStringProperty powerFactorActualProperty) {
			this.powerFactorActualProperty = powerFactorActualProperty;
		}
		public BooleanProperty getIsRunning() {
			return isRunning;
		}
		public SimpleStringProperty getStatusProperty() {
			return statusProperty;
		}
		public BooleanProperty getRpmBlink() {
			return rpmBlink;
		}
		public BooleanProperty getWindSpeedBlink() {
			return windSpeedBlink;
		}
		public BooleanProperty getCurrentBlink() {
			return currentBlink;
		}
		public BooleanProperty getWattsBlink() {
			return wattsBlink;
		}
		public BooleanProperty getVaBlink() {
			return vaBlink;
		}
		public BooleanProperty getPfBlink() {
			return pfBlink;
		}
}
