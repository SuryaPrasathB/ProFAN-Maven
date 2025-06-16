package com.tasnetwork.calibration.energymeter.device;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.binary.Hex;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdRadiant;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
import com.tasnetwork.calibration.energymeter.util.TMS_FloatConversion;



public class SerialDataRadiantRefStd {
	Timer timer;
	int responseWaitCount =5;
	static Communicator SerialPortObj= null;
	Boolean ExpectedLengthRecieved = false;
	Boolean ExpectedResponseRecieved = false;
	int RetryIntervalInMsec=200;//100;//1000;

	static DeviceDataManagerController DisplayDataObj =  new DeviceDataManagerController();

	public String VoltageDisplayData="";
	public String CurrentDisplayData="";
	public String DegreePhaseData="";
	public String PowerFactorData="";
	public String FreqDisplayData="";
	public String WattDisplayData="";
	public String VA_DisplayData="";
	public String VAR_DisplayData="";
	static String RefStd_ReadSerialData = "";
	public int ReceivedLength = 0;
	String PhaseAReading = "";
	String PhaseBReading = "";
	String PhaseCReading = "";



	public SerialDataRadiantRefStd(Communicator inpSerialPortObj){
		SerialPortObj = inpSerialPortObj;
	}

	public static void setRefStd_ReadSerialData(String currentSerialData) {
		// TODO Auto-generated method stub

		RefStd_ReadSerialData = currentSerialData;

	}

	public static void ClearRefStd_ReadSerialData() {
		// TODO Auto-generated method stub
		ApplicationLauncher.logger.debug("ClearRefStd_ReadSerialData : Entry");

		RefStd_ReadSerialData = "";

	}

	public static String getRefStd_ReadSerialData() {
		// TODO Auto-generated method stub

		return RefStd_ReadSerialData = SerialPortObj.getSerialData();

	}

	public void setReceivedLength(Integer length){

		this.ReceivedLength = length;
	}

	public Integer getReceivedLength(){

		return this.ReceivedLength;
	}

	public void setPhaseAReading(String Value) {
		// TODO Auto-generated method stub

		this.PhaseAReading = Value;

	}

	public String getPhaseAReading() {
		// TODO Auto-generated method stub

		return this.PhaseAReading;

	} 

	public void setPhaseBReading(String Value) {
		// TODO Auto-generated method stub

		this.PhaseBReading = Value;

	}

	public String getPhaseBReading() {
		// TODO Auto-generated method stub

		return this.PhaseBReading;

	} 

	public void setPhaseCReading(String Value) {
		// TODO Auto-generated method stub

		this.PhaseCReading = Value;

	}

	public String getPhaseCReading() {
		// TODO Auto-generated method stub

		return this.PhaseBReading;

	}

	public String AssertValidateForHeaderAndStripFirstByteIfInvalid(String InputData){

		try{
			if (InputData.substring(2, 18).equals(ConstantRefStdRadiant.REF_STD_R_PHASE_RESPONSE_HEADER)){
				ApplicationLauncher.logger.debug("ValidateForHeaderAndStripFirstByteIfInvalid-B6: First Byte Data Stripped");
				return (InputData.substring(2, 180)+ConstantRefStdRadiant.UNKNOWN_PHASE_FILLER);
			}	else if (InputData.substring(2, 18).equals(ConstantRefStdRadiant.REF_STD_Y_PHASE_RESPONSE_HEADER)){
				ApplicationLauncher.logger.debug("ValidateForHeaderAndStripFirstByteIfInvalid-C6: First Byte Data Stripped");
				return (InputData.substring(2, 180)+ConstantRefStdRadiant.UNKNOWN_PHASE_FILLER);
			}	else if (InputData.substring(2, 18).equals(ConstantRefStdRadiant.REF_STD_B_PHASE_RESPONSE_HEADER)){
				ApplicationLauncher.logger.debug("ValidateForHeaderAndStripFirstByteIfInvalid-D6: First Byte Data Stripped");
				return (InputData.substring(2, 180)+ConstantRefStdRadiant.UNKNOWN_PHASE_FILLER);
			} else if (InputData.substring(0, 14).equals(ConstantRefStdRadiant.REF_STD_PHASE_WITHOUT_RESPONSE_HEADER)){
				ApplicationLauncher.logger.debug("ValidateForHeaderAndStripFirstByteIfInvalid-0x0E: First Byte Data Added");
				return InputData= ConstantRefStdRadiant.UNKNOWN_PHASE_FILLER+InputData;

			}	else{
				if(InputData.length() == 180){
					if(InputData.contains(ConstantRefStdRadiant.REF_STD_PHASE_WITHOUT_RESPONSE_HEADER2)){
						String Strip_InputData = InputData;
						Strip_InputData = Strip_InputData.substring(0, Strip_InputData.indexOf(ConstantRefStdRadiant.REF_STD_PHASE_WITHOUT_RESPONSE_HEADER2));
						int length_of_stripped_inputdata = Strip_InputData.length();
						String Remove_Striped_InputData =InputData.substring(Strip_InputData.length(), InputData.length());
						if(length_of_stripped_inputdata!=8){
							ApplicationLauncher.logger.debug("input data:"+InputData);
							InputData=(ConstantRefStdRadiant.UNKNOWN_PHASE_FILLER1+Remove_Striped_InputData);
							ApplicationLauncher.logger.debug("changed output data:"+InputData);
						}
					}
				}

				return InputData;

			}
		}catch(Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("AssertValidateForHeaderAndStripFirstByteIfInvalid Exception:"+e.getMessage());

			return null;
		}
	}


	public String ParseVoltageDatafromRefStd(String SerialInputData ){


		ApplicationLauncher.logger.debug("ParseVoltageDatafromRefStd: Entry");
		String VoltageInHex = null;
		VoltageDisplayData="";
		try{
			if(SerialInputData.length()>=(ConstantRefStdRadiant.REF_STD_INSTANT_METRICS_VOLTAGE_POSITION+8)){
				//VoltageInHex =  SerialInputData.substring(48, 48+8);gjghk
				VoltageInHex =  SerialInputData.substring(ConstantRefStdRadiant.REF_STD_INSTANT_METRICS_VOLTAGE_POSITION, (ConstantRefStdRadiant.REF_STD_INSTANT_METRICS_VOLTAGE_POSITION+8));
				VoltageDisplayData=TMS_FloatConversion.hextofloat(VoltageInHex);
			}
		}catch (Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("ParseVoltageDatafromRefStd: Exception :"+E.toString());

		}
		return VoltageDisplayData;
	}

	public Boolean IsExpectedResponseReceived(){

		while((!ExpectedResponseRecieved)&&(responseWaitCount!=0)){
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("Ref: IsExpectedResponseReceived: InterruptedException:" + e.getMessage());
			}
		};
		return this.ExpectedResponseRecieved;
	}



	public String  ParseCurrentDatafromRefStd(String SerialInputData){

		ApplicationLauncher.logger.debug("ParseCurrentDatafromRefStd: Entry");
		String CurrentInHex = null;
		CurrentDisplayData="";
		try{
			if(SerialInputData.length()>=(ConstantRefStdRadiant.REF_STD_INSTANT_METRICS_CURRENT_POSITION+8)){
				//CurrentInHex =  SerialInputData.substring(56, 56+8);hgkhj
				CurrentInHex =  SerialInputData.substring(ConstantRefStdRadiant.REF_STD_INSTANT_METRICS_CURRENT_POSITION, (ConstantRefStdRadiant.REF_STD_INSTANT_METRICS_CURRENT_POSITION+8));
				CurrentDisplayData=TMS_FloatConversion.hextofloat(CurrentInHex);
			}
		}catch (Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("ParseCurrentDatafromRefStd: Exception :"+E.getMessage());

		}
		return CurrentDisplayData;
	}

	public String ParseWattDatafromRefStd(String SerialInputData){

		ApplicationLauncher.logger.debug("ParseWattDatafromRefStd: Entry");
		String WattInHex = null;
		WattDisplayData ="";
		try{
			if(SerialInputData.length()>=(ConstantRefStdRadiant.REF_STD_INSTANT_METRICS_WATT_POSITION+8)){
				//WattInHex =  SerialInputData.substring(64, 64+8);ghkhjk
				WattInHex =  SerialInputData.substring(ConstantRefStdRadiant.REF_STD_INSTANT_METRICS_WATT_POSITION, (ConstantRefStdRadiant.REF_STD_INSTANT_METRICS_WATT_POSITION+8));
				WattDisplayData=TMS_FloatConversion.hextofloat(WattInHex);
			}
		}catch (Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("ParseWattDatafromRefStd: Exception :"+E.getMessage());

		}
		return WattDisplayData;
	}

	public String ParseVA_DatafromRefStd(String SerialInputData){

		ApplicationLauncher.logger.debug("ParseVA_DatafromRefStd: Entry");
		String VA_InHex = null;
		VA_DisplayData="";
		try{
			if(SerialInputData.length()>=(ConstantRefStdRadiant.REF_STD_INSTANT_METRICS_VA_POSITION+8)){
				//VA_InHex =  SerialInputData.substring(72, 72+8);ghkhjk
				VA_InHex =  SerialInputData.substring(ConstantRefStdRadiant.REF_STD_INSTANT_METRICS_VA_POSITION, (ConstantRefStdRadiant.REF_STD_INSTANT_METRICS_VA_POSITION+8));
				VA_DisplayData=TMS_FloatConversion.hextofloat(VA_InHex);
			}
		}catch (Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("ParseVA_DatafromRefStd: Exception :"+E.getMessage());

		}
		return VA_DisplayData;
	}
	
	public String ParseVAR_DatafromRefStd(String SerialInputData){

		ApplicationLauncher.logger.debug("ParseVAR_DatafromRefStd: Entry");
		String VAR_InHex = null;
		VAR_DisplayData="";
		try{
			if(SerialInputData.length()>=(ConstantRefStdRadiant.REF_STD_INSTANT_METRICS_VAR_POSITION+8)){
				//VAR_InHex =  SerialInputData.substring(80, 80+8);gkhfjk
				VAR_InHex =  SerialInputData.substring(ConstantRefStdRadiant.REF_STD_INSTANT_METRICS_VAR_POSITION, (ConstantRefStdRadiant.REF_STD_INSTANT_METRICS_VAR_POSITION+8));
				VAR_DisplayData=TMS_FloatConversion.hextofloat(VAR_InHex);
			}
		}catch (Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("ParseVAR_DatafromRefStd: Exception :"+E.getMessage());

		}
		return VAR_DisplayData;
	}

	public String ParseFreqDatafromRefStd(String SerialInputData){

		ApplicationLauncher.logger.debug("ParseFreqDatafromRefStd: Entry");
		String FreqInHex = null;
		FreqDisplayData="";
		try{
			if(SerialInputData.length()>=(ConstantRefStdRadiant.REF_STD_INSTANT_METRICS_FREQ_POSITION+8)){
				//FreqInHex =  SerialInputData.substring(88, 88+8);gyhkhgk
				FreqInHex =  SerialInputData.substring(ConstantRefStdRadiant.REF_STD_INSTANT_METRICS_FREQ_POSITION, (ConstantRefStdRadiant.REF_STD_INSTANT_METRICS_FREQ_POSITION+8));
				FreqDisplayData=TMS_FloatConversion.hextofloat(FreqInHex);
			}
		}catch (Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("ParseFreqDatafromRefStd: Exception :"+E.getMessage());

		}
		return FreqDisplayData;
	}

	public String  ParseDegreePhaseDatafromRefStd(String SerialInputData){

		ApplicationLauncher.logger.debug("ParseDegreePhaseDatafromRefStd: Entry");
		String PhaseInHex = null;
		DegreePhaseData="";
		try{
			if(SerialInputData.length()>=(ConstantRefStdRadiant.REF_STD_INSTANT_METRICS_DEGREE_POSITION+8)){
				//PhaseInHex =  SerialInputData.substring(96, 96+8);gykhfjk
				PhaseInHex =  SerialInputData.substring(ConstantRefStdRadiant.REF_STD_INSTANT_METRICS_DEGREE_POSITION, (ConstantRefStdRadiant.REF_STD_INSTANT_METRICS_DEGREE_POSITION+8));
				DegreePhaseData=TMS_FloatConversion.hextofloat(PhaseInHex);
			}
		}catch (Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("ParseDegreePhaseDatafromRefStd: Exception :"+E.getMessage());

		}
		return DegreePhaseData;
	}

	public String  ParsePowerFactorDatafromRefStd(String SerialInputData){

		ApplicationLauncher.logger.debug("ParsePowerFactorDatafromRefStd: Entry");
		String PhaseInHex = null;
		PowerFactorData="";
		try{
			if(SerialInputData.length()>=(ConstantRefStdRadiant.REF_STD_INSTANT_METRICS_PF_POSITION+8)){
				//PhaseInHex = SerialInputData.substring(104, (104+8));gkhjk
				PhaseInHex = SerialInputData.substring(ConstantRefStdRadiant.REF_STD_INSTANT_METRICS_PF_POSITION, (ConstantRefStdRadiant.REF_STD_INSTANT_METRICS_PF_POSITION+8));
				PowerFactorData=TMS_FloatConversion.hextofloat(PhaseInHex);
			}
		}catch (Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("ParsePowerFactorDatafromRefStd: Exception :"+E.getMessage());

		}
		return PowerFactorData;
	}




	/*	public static String hextofloat(String  hexNumber){
		//int hex = 0x4B196DAF;
		//int raw = Integer.parseInt(hexNumber, 16);


		//long hex = Long.parseLong(hexNumber, 16);
		//float f = Float.intBitsToFloat(hex);
		//float f = Float.intBitsToFloat(hex.intValue());
		double output=0.0f;
		try{
			int raw = Integer.parseUnsignedInt(hexNumber, 16);
			int s = (raw >> 23) & 1;
				//ApplicationLauncher.logger.info("s:"+s);
			int mantissa = (raw & 0x007fffff);
				//ApplicationLauncher.logger.info("mantissa:"+mantissa);
			int exponent = raw >> 24;
				//ApplicationLauncher.logger.info("exponent1:"+exponent);
			if (exponent >= 128){
				exponent -= 256;
				//ApplicationLauncher.logger.info("exponent2:"+exponent);
			}
			if (exponent == -128){
				output= 0.0f;
				//ApplicationLauncher.logger.info("exponent3:"+exponent);
			}
			else{
				//ApplicationLauncher.logger.info("output Value1:"+((-2) ^ s));
						ApplicationLauncher.logger.info("output Value1:"+(Math.pow(-2, s)));
		ApplicationLauncher.logger.info("output Value2:"+( (float)(mantissa)));
		ApplicationLauncher.logger.info("output Value3:"+((float)(1 << 23)));
		ApplicationLauncher.logger.info("output Value4:"+( ( (float)(mantissa) / (float)(1 << 23))));
		ApplicationLauncher.logger.info("output Value5:"+(((Math.pow(-2, s)) + (float)(mantissa) / (float)(1 << 23)))   );
		ApplicationLauncher.logger.info("output Value6:"+(Math.pow(2, exponent)));
				//output= (((-2) ^ s) + ((float)(mantissa)) / ((float)(1 << 23))) * (2 ^ exponent);
				output= ((Math.pow(-2, s)) + ((float)(mantissa)) / ((float)(1 << 23))) * (Math.pow(2, exponent));
			}
		} catch (Exception e){
			System.out.printf("Exception1 on hextofloat:", e.toString());
		}

		//ApplicationLauncher.logger.info(output);
		//ApplicationLauncher.logger.info("output:"+ output);
		String Data ="";
		try{
			Data =String.format("%.4f",output);
		} catch (Exception e){
			System.out.printf("Exception2 on hextofloat:", e.toString());
		}
		return Data;
		//System.out.printf("%f", f);
	}	*/
	//from Float to Hex
	//https://stackoverflow.com/questions/17833469/convert-ieee-float-to-ti-tms320c30-32bits-float-in-python
	//https://www.ibm.com/developerworks/library/j-math2/index.html
	//https://www.doc.ic.ac.uk/~eedwards/compsys/float/
	//https://electronics.stackexchange.com/questions/223832/tms320-floating-point-texas-instruments-dsp-from-98
	//http://etd.fcla.edu/UF/UFE0005060/tmsieeeconv.c
	//http://read.pudn.com/downloads97/sourcecode/embed/397645/%E7%BB%8F%E5%85%B8%E7%9A%84dsp%E7%9A%84c%E7%A8%8B%E5%BA%8F%E5%92%8C%E6%B1%87%E7%BC%96%E7%A8%8B%E5%BA%8F%E5%BA%93%E5%B8%B8%E7%94%A8%E4%BE%8B/%E7%A8%8B%E5%BA%8F/TI%E5%B8%B8%E7%94%A8%E4%BE%8B%E7%A8%8B/ieeeConv/IEEECONV.C__.htm
	//http://etd.fcla.edu/UF/UFE0005060/tmsieeeconv.c	


	//http://etd.fcla.edu/UF/UFE0005060/tmsieeeconv.c

	/*	uint32 ieee_to_tms(const float floatIn)        CONVERSION FROM IEEE FORMAT TO TMS320C30 FORMAT 
	{

		ieee.flt = floatIn;

		 Refer to page 5-15 in the TMS320C3x User's Guide for the 
		 following case numbers of IEEE-->C3X conversion. 

		if (ieee.str.exponent == 0)
		{
			 Case 6 -- ZERO 
			return(0x80000000);
		}
		else if (ieee.str.exponent == 255 && ieee.str.sign == 1)
		{
			 Case 1 -- Maximum Negative Infinity 
			return(0x7F800000);
		}
		else if (ieee.str.exponent == 255 && ieee.str.sign == 0)
		{
			 Case 2 -- Maximum Positive Infinity 
			return(0x7F7FFFFF);
		}
		else
		{
			if (ieee.str.sign == 0)
			{
				 Case 3 
				c30.str.exponent = ieee.str.exponent - 127;
				c30.str.sign = 0;
				c30.str.mantissa = ieee.str.mantissa;
				return(c30.hex);
			}
			else if (ieee.str.mantissa != 0)
			{
				 Case 4 
				c30.str.exponent = ieee.str.exponent - 127;
				c30.str.sign = 1;
				c30.str.mantissa = ~(ieee.str.mantissa)+1;
				return(c30.hex);
			}
			else
			{
				 Case 5 
				c30.str.exponent = ieee.str.exponent - 128;
				c30.str.sign = 1;
				c30.str.mantissa = 0;
				return(c30.hex);
			}
		}
	}*/


	public void SerialReponseTimerStart(int waitTimeCount) {
		timer = new Timer();
		timer.schedule(new RefStdTask(), RetryIntervalInMsec);
		responseWaitCount = waitTimeCount;

	}

	public Boolean IsExpectedLengthReceived(){

		while((!ExpectedLengthRecieved)&&(responseWaitCount!=0)){
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ApplicationLauncher.logger.error("IsExpectedLengthReceived: InterruptedException :"+e.getMessage());
			}
		};
		return this.ExpectedLengthRecieved;
	}



	class RefStdTask extends TimerTask {
		public void run() {
			String CurrentSerialData = SerialPortObj.getSerialData();
			Integer ExpectedLength = SerialPortObj.getExpectedLength();

			if(CurrentSerialData.length()==ExpectedLength)
			{
				ApplicationLauncher.logger.info("RefStdTask: Expected length received:");

				ExpectedLengthRecieved=true;
				String ExpectedResult = SerialPortObj.getExpectedResult();
				ApplicationLauncher.logger.debug("RefStdTask: ExpectedResultX: " + ExpectedResult);
				ApplicationLauncher.logger.debug("RefStdTask: CurrentSerialDataX: " + CurrentSerialData);
				if (CurrentSerialData.contains(ExpectedResult)){
					ExpectedResponseRecieved=true;
					setRefStd_ReadSerialData(CurrentSerialData);
					setReceivedLength(CurrentSerialData.length());
					ApplicationLauncher.logger.debug("RefStdTask: Expected Data received:Length:"+CurrentSerialData.length()+":CurrentData:"+CurrentSerialData);
				}

			}
			else if (CurrentSerialData.length()>ExpectedLength){
				ApplicationLauncher.logger.info("**********************************************************");
				ApplicationLauncher.logger.info("**********************************************************");
				ApplicationLauncher.logger.info("**********************************************************");
				ApplicationLauncher.logger.info("**********************************************************");
				ApplicationLauncher.logger.info("RefStdTask: unExpected received:CurrentData:"+CurrentSerialData+":CurrentLength:"+CurrentSerialData.length()+":ExpectedLength:"+SerialPortObj.getExpectedLength());
				ApplicationLauncher.logger.info("**********************************************************");
				ApplicationLauncher.logger.info("**********************************************************");
				ApplicationLauncher.logger.info("**********************************************************");
				ApplicationLauncher.logger.info("**********************************************************");
				SerialPortObj.StripLength(CurrentSerialData.length());
				ApplicationLauncher.logger.info("RefStdTask :Stripped: Exceeded length data");

			}
			responseWaitCount --;
			if (responseWaitCount == 0 || ExpectedLengthRecieved ||  ProjectExecutionController.getUserAbortedFlag()){
				ApplicationLauncher.logger.info("RefStdTask :Timer Exit!");
				timer.cancel(); //Terminate the timer thread
			}
			else{
				timer.schedule(new RefStdTask(), RetryIntervalInMsec);
			}
		}	
	}




	public String RefStd_DecodeSerialDataForConstTest() {
		// TODO Auto-generated method stub

		String DecodeData=this.RefStd_ReadSerialData;
		String metertype = DisplayDataObj.getDeployedEM_ModelType();
		try{
			if(DecodeData.length()>=8){
				if(DecodeData.substring(0, 8).equals(ConstantRefStdRadiant.REF_STD_ACCUMULATIVE_ER)) {
				//if(DecodeData.substring(0, 8).equals("A62F0014")) {
					//ApplicationLauncher.logger.info("LDU_DecodeSerialDataForSTA: Decoded Serial Data2:"+DecodeData);
					DecodeData = DecodeData.substring(8);
					//ApplicationLauncher.logger.info("LDU_DecodeSerialDataForSTA: Decoded Serial Data3:"+DecodeData);
					//ApplicationLauncher.logger.info("DecodeData.length(): "  + DecodeData.length());

					if(DecodeData.length() == 44){
						//String PhaseA = DecodeData.substring(0, Math.min(DecodeData.length(), 8));
						String PhaseA = DecodeData.substring(ConstantRefStdRadiant.REF_STD_ACCUMULATIVE_R_PHASE_POSITION, Math.min(DecodeData.length(), (ConstantRefStdRadiant.REF_STD_ACCUMULATIVE_R_PHASE_POSITION+8)));
						PhaseA = TMS_FloatConversion.hextofloat(PhaseA);
						String PhaseB = "";
						String PhaseC = "";
						if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){

							//PhaseB = DecodeData.substring(8, Math.min(DecodeData.length(), 16));
							//PhaseC = DecodeData.substring(16, Math.min(DecodeData.length(), 24));
							PhaseB = DecodeData.substring(ConstantRefStdRadiant.REF_STD_ACCUMULATIVE_Y_PHASE_POSITION, Math.min(DecodeData.length(), (ConstantRefStdRadiant.REF_STD_ACCUMULATIVE_Y_PHASE_POSITION+8)));
							PhaseC = DecodeData.substring(ConstantRefStdRadiant.REF_STD_ACCUMULATIVE_B_PHASE_POSITION, Math.min(DecodeData.length(), (ConstantRefStdRadiant.REF_STD_ACCUMULATIVE_B_PHASE_POSITION+8)));
							PhaseB = TMS_FloatConversion.hextofloat(PhaseB);
							PhaseC = TMS_FloatConversion.hextofloat(PhaseC);
						}
						//ApplicationLauncher.logger.info("LDU_DecodeSerialDataForSTA: Decoded Serial Data4:"+DecodeData);
						//DecodeData = MyProperty.DecodeHextoString(DecodeData);
						//ApplicationLauncher.logger.info("SerialDataLDU:Decoded Serial Data5:"+DecodeData);
						//setLDU_ResultStatus(DecodeData.substring(0, 1));
						//ApplicationLauncher.logger.info("LDU_DecodeSerialDataForSTA: Decoded Serial Data6:"+getLDU_ResultStatus());
						//setLDU_ErrorValue(DecodeData.substring(1, 5));
						//ApplicationLauncher.logger.info("LDU_DecodeSerialDataForSTA: Decoded Serial Data7:"+getLDU_ErrorValue());

						//ApplicationLauncher.logger.info("PhaseA: " + PhaseA);
						//ApplicationLauncher.logger.info("PhaseB: " + PhaseB);
						//ApplicationLauncher.logger.info("PhaseC: " + PhaseC);
						/*					PhaseA = TMS_FloatConversion.hextofloat(PhaseA);
					PhaseB = TMS_FloatConversion.hextofloat(PhaseB);
					PhaseC = TMS_FloatConversion.hextofloat(PhaseC);*/

						ApplicationLauncher.logger.info("PhaseA: " + PhaseA);
						ApplicationLauncher.logger.info("PhaseB: " + PhaseB);
						ApplicationLauncher.logger.info("PhaseC: " + PhaseC);
						//ApplicationLauncher.logger.info("DecodeData: " + DecodeData);
						setPhaseAReading(PhaseA);
						setPhaseBReading(PhaseB);
						setPhaseCReading(PhaseC);
					}
				}else if(DecodeData.length()<12){

					ApplicationLauncher.logger.error("RefStd_DecodeSerialDataForConstTest :Error 2003");

				}
			}
		} catch (Exception e){
			e.printStackTrace();

			ApplicationLauncher.logger.error("RefStd_DecodeSerialDataForConstTest : Exception: "+ e.getMessage());
		}
		return DecodeData;

	}



}
