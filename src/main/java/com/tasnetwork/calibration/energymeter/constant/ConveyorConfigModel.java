package com.tasnetwork.calibration.energymeter.constant;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConveyorConfigModel {


		@SerializedName("MaxDutSupported")
		@Expose
	    private int maxNoOfDutSupported;
		
		@SerializedName("OpticalReadingInterfaceEnabled")
		@Expose
	    private boolean opticalReadingInterfaceEnabled;
		
		@SerializedName("DutSerialInterfaceEnabled")
		@Expose
	    private boolean dutSerialInterfaceEnabled;

		public int getMaxNoOfDutSupported() {
			return maxNoOfDutSupported;
		}

		public void setMaxNoOfDutSupported(int maxNoOfDutSupported) {
			this.maxNoOfDutSupported = maxNoOfDutSupported;
		}

		public boolean isOpticalReadingInterfaceEnabled() {
			return opticalReadingInterfaceEnabled;
		}

		public void setOpticalReadingInterfaceEnabled(boolean opticalReadingInterfaceEnabled) {
			this.opticalReadingInterfaceEnabled = opticalReadingInterfaceEnabled;
		}

		public boolean isDutSerialInterfaceEnabled() {
			return dutSerialInterfaceEnabled;
		}

		public void setDutSerialInterfaceEnabled(boolean dutSerialInterfaceEnabled) {
			this.dutSerialInterfaceEnabled = dutSerialInterfaceEnabled;
		}
		

	
}
