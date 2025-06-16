package com.tasnetwork.calibration.energymeter.testprofiles;

import com.tasnetwork.calibration.energymeter.constant.ConstantApp;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
 
public class HarmonicsDataModel {
	
	  SimpleStringProperty phaseSelected;
	
	  IntegerProperty serialNum;
	  IntegerProperty harmonicsOrder;

	//BooleanProperty enableStatus;
	  BooleanProperty harmonicOrderSelected_V;
	//private IntegerProperty harmonicsOrder_V;
	  SimpleStringProperty amplitude_V;
	  SimpleStringProperty phaseShift_V;
	
	  BooleanProperty harmonicOrderSelected_I;
	//private IntegerProperty harmonicsOrder_I;
	  SimpleStringProperty amplitude_I;
	  SimpleStringProperty phaseShift_I;
	
	//phaseSelected = ConstantApp.FIRST_PHASE_DISPLAY_NAME;
	//private CheckBox chkBxV1OrderHarmonics;
	
	
	public HarmonicsDataModel (String selectedPhase, int OrderOfHarmonics, 
			                   Boolean harmonicOrderSelected_V, String Amp_V, String PhaseDegree_V,
			                   Boolean harmonicOrderSelected_I, String Amp_I, String PhaseDegree_I){
		
	//	this.serialNum      = new SimpleIntegerProperty(SerialNo);
		this.phaseSelected = new SimpleStringProperty(selectedPhase);
		this.harmonicsOrder = new SimpleIntegerProperty(OrderOfHarmonics);

		this.harmonicOrderSelected_V   = new SimpleBooleanProperty(harmonicOrderSelected_V);
		this.amplitude_V      = new SimpleStringProperty(Amp_V);
		this.phaseShift_V     = new SimpleStringProperty(PhaseDegree_V) ;
		
		this.harmonicOrderSelected_I = new SimpleBooleanProperty(harmonicOrderSelected_I);
		this.amplitude_I      = new SimpleStringProperty(Amp_I);
		this.phaseShift_I     = new SimpleStringProperty(PhaseDegree_I);
	}
	
	public HarmonicsDataModel (){
		/*this.serialNum      = new SimpleIntegerProperty(0);
		this.harmonicOrderSelected   = new SimpleBooleanProperty(true);
		
		this.harmonicsOrder_V = new SimpleIntegerProperty(2);
		this.amplitude_V      = new SimpleStringProperty("");
		this.phaseShift_V     = new SimpleStringProperty("") ;
		
		this.harmonicsOrder_I = new SimpleIntegerProperty(2);
		this.amplitude_I      = new SimpleStringProperty("");
		this.phaseShift_I     = new SimpleStringProperty("");*/
		}

 
	//=====================
		public IntegerProperty getSerialNumProperty() {
			return serialNum;
		}

		public void setSerialNum(int serialNum) {
			this.serialNum.setValue(serialNum);
		}
		
		public int getSerialNum() {
			return this.serialNum.getValue();
		}

	//===================
		/*
		public BooleanProperty getEnableStatusProperty() {
			return enableStatus;
		}


		public void setEnableStatus(boolean enableStatus) {
			this.enableStatus.setValue(enableStatus);
		}
		
	 
		public boolean getEnableStatus() {
			return this.enableStatus.getValue();
		}*/

//===================
		

		
	public BooleanProperty getHarmonicOrderSelected_V_Property() {
		return harmonicOrderSelected_V;
	}
	
	public boolean isHarmonicOrder_V_Selected() {
		return harmonicOrderSelected_V.getValue();
	}

	public void setHarmonicOrder_V_Selected(Boolean harmonicOrderSelected_V) {
		this.harmonicOrderSelected_V.setValue(harmonicOrderSelected_V);
	}
		
//===================
	
	public IntegerProperty getHarmonicsOrderProperty() {
		return harmonicsOrder;
	}


	public void setHarmonicsOrder(int harmonicsOrder) {
		this.harmonicsOrder.setValue(harmonicsOrder);
	}
	
	public int getHarmonicsOrder() {
		return this.harmonicsOrder.getValue();
	}

//===================
		/*public IntegerProperty getHarmonicsOrder_V_Property() {
			return harmonicsOrder_V;
		}


		public void setHarmonicsOrder_V(int harmonicsOrder_V) {
			this.harmonicsOrder_V.setValue(harmonicsOrder_V);
		}
		
		public int getHarmonicsOrder_V() {
			return this.harmonicsOrder_V.getValue();
		}
*/
	//===================
		
		public SimpleStringProperty getAmplitude_V_Property() {
			return amplitude_V;
		}


		public void setAmplitude_V(String amplitude_V) {
			this.amplitude_V.setValue(amplitude_V);
		}
		
		public String getAmplitude_V() {
			return this.amplitude_V.getValue();
		}

	//===================
		
		public SimpleStringProperty getPhaseShift_V_Property() {
			return phaseShift_V;
		}


		public void setPhaseShift_V(String phaseShift_V) {
			this.phaseShift_V.setValue(phaseShift_V);
		}
		
		public String getPhaseShift_V() {
			return this.phaseShift_V.getValue();
		}
//===================
		
		public BooleanProperty getHarmonicOrderSelected_I_Property() {
			return harmonicOrderSelected_I;
		}
		
		public boolean isHarmonicOrder_I_Selected() {
			return harmonicOrderSelected_I.getValue();
		}

		public void setHarmonicOrder_I_Selected(Boolean harmonicOrderSelected_I) {
			this.harmonicOrderSelected_I.setValue(harmonicOrderSelected_I);
		}
			
	//===================
			/*public IntegerProperty getHarmonicsOrder_I_Property() {
				return harmonicsOrder_I;
			}


			public void setHarmonicsOrder_I(int harmonicsOrder_I) {
				this.harmonicsOrder_I.setValue(harmonicsOrder_I);
			}
			
			public int getHarmonicsOrder_I() {
				return this.harmonicsOrder_I.getValue();
			}*/

		//===================
			
			public SimpleStringProperty getAmplitude_I_Property() {
				return amplitude_I;
			}


			public void setAmplitude_I(String amplitude_I) {
				this.amplitude_I.setValue(amplitude_I);
			}
			
			public String getAmplitude_I() {
				return this.amplitude_I.getValue();
			}

		//===================
			
			public SimpleStringProperty getPhaseShift_I_Property() {
				return phaseShift_I;
			}


			public void setPhaseShift_I(String phaseShift_I) {
				this.phaseShift_I.setValue(phaseShift_I);
			}
			
			public String getPhaseShift_I() {
				return this.phaseShift_I.getValue();
			}
//=======================================================================================

			//public SimpleStringProperty getPhaseSelectedProperty() {
			//	return phaseShift_I;
			//}

			public void setPhaseSelected(String phaseSelected) {
				this.phaseSelected.setValue(phaseSelected);
			}
			
			public String getPhaseSelected() {
				return this.phaseSelected.getValue();
			}
//=======================================================================================
			
			
		//	public String getPhaseSelected() {
		//		return phaseSelected;
		//	}

		//	public void setPhaseSelected(String phaseSelected) {
		//		this.phaseSelected = phaseSelected;
		//	}

	/*	public BooleanProperty getHarmonicOrderSelectedProperty() {
			return harmonicOrderSelected;
		}
		
		public boolean isHarmonicOrderSelected() {
			return harmonicOrderSelected.getValue();
		}

		public void setHarmonicOrderSelected(Boolean harmonicOrderSelected) {
			this.harmonicOrderSelected.setValue(harmonicOrderSelected);
		}*/
		
//=======================================================================================
	
	
	
	
	
}
