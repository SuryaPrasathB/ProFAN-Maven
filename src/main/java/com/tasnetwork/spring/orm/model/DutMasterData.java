package com.tasnetwork.spring.orm.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "DutMasterData")
public class DutMasterData {
	
	
	   @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY )
	    private Long id;

		@Column( columnDefinition = "VARCHAR(3)", nullable = false) 
		@Type(type = "yes_no")
		private Boolean active = false;
		
		@Column(columnDefinition = "VARCHAR(45)", nullable = false) 
		private String dutBaseName = ""; // energy meter or Fan
		
		@Column(columnDefinition = "VARCHAR(45)", nullable = false) 
		private String dutType = "";
		
	    @Column(columnDefinition = "VARCHAR(45)", nullable = false) 
		private String ratedVoltage = "";
	    @Column(columnDefinition = "VARCHAR(45)", nullable = false) 
		private String ratedCurrent = "";
	    
	    @Column(columnDefinition = "VARCHAR(45)", nullable = false) 
		private String windSpeedConfig = "";
	    
	    @Column(columnDefinition = "VARCHAR(45)", nullable = false) 
		private String areaOfOpening = "";
	    
	    @Column(columnDefinition = "VARCHAR(45)", nullable = false) 
		private String phase = "";
	    
	    @Column(unique = true, columnDefinition = "VARCHAR(45)", nullable = false) 
		private String modelName = "";
	    
	    
	    
	    
	    @OneToMany(mappedBy = "dutMasterData", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<FanTestSetup> fanTestSetupList = new ArrayList<>();
	    
	    
	    
	    
		public Long getId() {
			return id;
		}
		public Boolean getActive() {
			return active;
		}
		public String getDutBaseName() {
			return dutBaseName;
		}
		public String getDutType() {
			return dutType;
		}
		public String getRatedVoltage() {
			return ratedVoltage;
		}
		public String getRatedCurrent() {
			return ratedCurrent;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public void setActive(Boolean active) {
			this.active = active;
		}
		public void setDutBaseName(String dutBaseName) {
			this.dutBaseName = dutBaseName;
		}
		public void setDutType(String dutType) {
			this.dutType = dutType;
		}
		public void setRatedVoltage(String ratedVoltage) {
			this.ratedVoltage = ratedVoltage;
		}
		public void setRatedCurrent(String ratedCurrent) {
			this.ratedCurrent = ratedCurrent;
		}
		public String getModelName() {
			return modelName;
		}
		public void setModelName(String modelName) {
			this.modelName = modelName;
		}
		
		public List<FanTestSetup> getFanTestSetupList() {
		    return fanTestSetupList;
		}

		public void setFanTestSetupList(List<FanTestSetup> fanTestSetupList) {
		    this.fanTestSetupList = fanTestSetupList;
		}

		public void addFanTestSetup(FanTestSetup setup) {
		    fanTestSetupList.add(setup);
		    setup.setDutMasterData(this);
		}

		public void removeFanTestSetup(FanTestSetup setup) {
		    fanTestSetupList.remove(setup);
		    setup.setDutMasterData(null);
		}
		public String getWindSpeedConfig() {
			return windSpeedConfig;
		}
		public void setWindSpeedConfig(String windSpeedConfig) {
			this.windSpeedConfig = windSpeedConfig;
		}
		public String getAreaOfOpening() {
			return areaOfOpening;
		}
		public void setAreaOfOpening(String areaOfOpening) {
			this.areaOfOpening = areaOfOpening;
		}
		public String getPhase() {
			return phase;
		}
		public void setPhase(String phase) {
			this.phase = phase;
		}

}
