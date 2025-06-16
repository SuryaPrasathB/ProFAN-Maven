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
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdSands;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.deployment.FailureManager;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
import com.tasnetwork.calibration.energymeter.util.ErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.util.IEEE754_Format;



public class SerialDataSandsRefStd {
	Timer timer;
	int responseWaitCount =5;
	static Communicator SerialPortObj= null;
	Boolean ExpectedLengthRecieved = false;
	Boolean ExpectedResponseRecieved = false;
	int RetryIntervalInMsec=200;//100;//1000;
	
	Boolean ErrorResponseRecieved = false;

	static DeviceDataManagerController displayDataObj =  new DeviceDataManagerController();
	public static SerialDataManager serialDM_Obj = new SerialDataManager();

	public String VoltageDisplayData="";
	public String CurrentDisplayData="";
	public String rPhaseDegreePhaseData="";
	public String yPhaseDegreePhaseData="";
	public String bPhaseDegreePhaseData="";
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
	
	String phaseA_ActivePowerReading = "";
	String phaseB_ActivePowerReading = "";
	String phaseC_ActivePowerReading = "";
	String totalActivePowerReading = "";

	String phaseA_ReactivePowerReading = "";
	String phaseB_ReactivePowerReading = "";
	String phaseC_ReactivePowerReading = "";
	String totalReactivePowerReading = "";

	String phaseA_ApparentPowerReading = "";
	String phaseB_ApparentPowerReading = "";
	String phaseC_ApparentPowerReading = "";
	String totalApparentPowerReading = "";



	public SerialDataSandsRefStd(Communicator inpSerialPortObj){
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
			if(SerialInputData.length()>=(ConstantRefStdSands.REF_STD_INSTANT_METRICS_VOLTAGE_INDEX_POSITION+8)){
				//VoltageInHex =  SerialInputData.substring(48, 48+8);gjghk
				VoltageInHex =  SerialInputData.substring(ConstantRefStdSands.REF_STD_INSTANT_METRICS_VOLTAGE_INDEX_POSITION, (ConstantRefStdSands.REF_STD_INSTANT_METRICS_VOLTAGE_INDEX_POSITION+8));
				VoltageDisplayData=IEEE754_Format.hexToFloat(VoltageInHex);
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
			if(SerialInputData.length()>=(ConstantRefStdSands.REF_STD_INSTANT_METRICS_CURRENT_INDEX_POSITION+8)){
				//CurrentInHex =  SerialInputData.substring(56, 56+8);hgkhj
				CurrentInHex =  SerialInputData.substring(ConstantRefStdSands.REF_STD_INSTANT_METRICS_CURRENT_INDEX_POSITION, (ConstantRefStdSands.REF_STD_INSTANT_METRICS_CURRENT_INDEX_POSITION+8));
				CurrentDisplayData=IEEE754_Format.hexToFloat(CurrentInHex);
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
			if(SerialInputData.length()>=(ConstantRefStdSands.REF_STD_INSTANT_METRICS_WATT_INDEX_POSITION+8)){
				//WattInHex =  SerialInputData.substring(64, 64+8);ghkhjk
				WattInHex =  SerialInputData.substring(ConstantRefStdSands.REF_STD_INSTANT_METRICS_WATT_INDEX_POSITION, (ConstantRefStdSands.REF_STD_INSTANT_METRICS_WATT_INDEX_POSITION+8));
				WattDisplayData=IEEE754_Format.hexToFloat(WattInHex);
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
			if(SerialInputData.length()>=(ConstantRefStdSands.REF_STD_INSTANT_METRICS_VA_INDEX_POSITION+8)){
				//VA_InHex =  SerialInputData.substring(72, 72+8);ghkhjk
				VA_InHex =  SerialInputData.substring(ConstantRefStdSands.REF_STD_INSTANT_METRICS_VA_INDEX_POSITION, (ConstantRefStdSands.REF_STD_INSTANT_METRICS_VA_INDEX_POSITION+8));
				VA_DisplayData=IEEE754_Format.hexToFloat(VA_InHex);
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
			if(SerialInputData.length()>=(ConstantRefStdSands.REF_STD_INSTANT_METRICS_VAR_INDEX_POSITION+8)){
				//VAR_InHex =  SerialInputData.substring(80, 80+8);gkhfjk
				VAR_InHex =  SerialInputData.substring(ConstantRefStdSands.REF_STD_INSTANT_METRICS_VAR_INDEX_POSITION, (ConstantRefStdSands.REF_STD_INSTANT_METRICS_VAR_INDEX_POSITION+8));
				VAR_DisplayData=IEEE754_Format.hexToFloat(VAR_InHex);
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
			if(SerialInputData.length()>=(ConstantRefStdSands.REF_STD_INSTANT_METRICS_FREQ_INDEX_POSITION+8)){
				//FreqInHex =  SerialInputData.substring(88, 88+8);gyhkhgk
				FreqInHex =  SerialInputData.substring(ConstantRefStdSands.REF_STD_INSTANT_METRICS_FREQ_INDEX_POSITION, (ConstantRefStdSands.REF_STD_INSTANT_METRICS_FREQ_INDEX_POSITION+8));
				FreqDisplayData=IEEE754_Format.hexToFloat(FreqInHex);
			}
		}catch (Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("ParseFreqDatafromRefStd: Exception :"+E.getMessage());

		}
		return FreqDisplayData;
	}

/*	public String  ParseDegreePhaseDatafromRefStd(String SerialInputData){

		ApplicationLauncher.logger.debug("ParseDegreePhaseDatafromRefStd: Entry");
		String PhaseInHex = null;
		DegreePhaseData="";
		try{
			if(SerialInputData.length()>=(ConstantSandsRefStd.REF_STD_INSTANT_METRICS_DEGREE_INDEX_POSITION+8)){
				//PhaseInHex =  SerialInputData.substring(96, 96+8);gykhfjk
				PhaseInHex =  SerialInputData.substring(ConstantSandsRefStd.REF_STD_INSTANT_METRICS_DEGREE_INDEX_POSITION, (ConstantSandsRefStd.REF_STD_INSTANT_METRICS_DEGREE_INDEX_POSITION+8));
				DegreePhaseData=IEEE754_Format.hexToFloat(PhaseInHex);
			}
		}catch (Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("ParseDegreePhaseDatafromRefStd: Exception :"+E.getMessage());

		}
		return DegreePhaseData;
	}*/
	
	
	
	public String  ParseDegreeR_PhaseDatafromRefStd(String SerialInputData){

		ApplicationLauncher.logger.debug("ParseDegreeR_PhaseDatafromRefStd: Entry");
		String PhaseInHex = null;
		rPhaseDegreePhaseData="";
		try{
			if(SerialInputData.length()>=(ConstantRefStdSands.REF_STD_VI_R_PHASE_ANGLE_INDEX_POSITION+8)){
				//PhaseInHex =  SerialInputData.substring(96, 96+8);
				PhaseInHex =  SerialInputData.substring(ConstantRefStdSands.REF_STD_VI_R_PHASE_ANGLE_INDEX_POSITION, (ConstantRefStdSands.REF_STD_VI_R_PHASE_ANGLE_INDEX_POSITION+8));
				rPhaseDegreePhaseData=IEEE754_Format.hexToFloat(PhaseInHex);
			}
		}catch (Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("ParseDegreeR_PhaseDatafromRefStd: Exception :"+E.getMessage());

		}
		return rPhaseDegreePhaseData;
	}
	
	public String  ParseDegreeY_PhaseDatafromRefStd(String SerialInputData){

		ApplicationLauncher.logger.debug("ParseDegreeY_PhaseDatafromRefStd: Entry");
		String PhaseInHex = null;
		yPhaseDegreePhaseData="";
		try{
			if(SerialInputData.length()>=(ConstantRefStdSands.REF_STD_VI_Y_PHASE_ANGLE_INDEX_POSITION+8)){
				//PhaseInHex =  SerialInputData.substring(96, 96+8);
				PhaseInHex =  SerialInputData.substring(ConstantRefStdSands.REF_STD_VI_Y_PHASE_ANGLE_INDEX_POSITION, (ConstantRefStdSands.REF_STD_VI_Y_PHASE_ANGLE_INDEX_POSITION+8));
				yPhaseDegreePhaseData=IEEE754_Format.hexToFloat(PhaseInHex);
			}
		}catch (Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("ParseDegreeY_PhaseDatafromRefStd: Exception :"+E.getMessage());

		}
		return yPhaseDegreePhaseData;
	}
	
	public String  ParseDegreeB_PhaseDatafromRefStd(String SerialInputData){

		ApplicationLauncher.logger.debug("ParseDegreeB_PhaseDatafromRefStd: Entry");
		String PhaseInHex = null;
		bPhaseDegreePhaseData="";
		try{
			if(SerialInputData.length()>=(ConstantRefStdSands.REF_STD_VI_B_PHASE_ANGLE_INDEX_POSITION+8)){
				//PhaseInHex =  SerialInputData.substring(96, 96+8);gykhfjk
				PhaseInHex =  SerialInputData.substring(ConstantRefStdSands.REF_STD_VI_B_PHASE_ANGLE_INDEX_POSITION, (ConstantRefStdSands.REF_STD_VI_B_PHASE_ANGLE_INDEX_POSITION+8));
				bPhaseDegreePhaseData=IEEE754_Format.hexToFloat(PhaseInHex);
			}
		}catch (Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("ParseDegreeB_PhaseDatafromRefStd: Exception :"+E.getMessage());

		}
		return bPhaseDegreePhaseData;
	}
	
	

	public String  ParsePowerFactorDatafromRefStd(String SerialInputData){

		ApplicationLauncher.logger.debug("ParsePowerFactorDatafromRefStd: Entry");
		String PhaseInHex = null;
		PowerFactorData="";
		try{
			if(SerialInputData.length()>=(ConstantRefStdSands.REF_STD_INSTANT_METRICS_PF_INDEX_POSITION+8)){
				//PhaseInHex = SerialInputData.substring(104, (104+8));gkhjk
				PhaseInHex = SerialInputData.substring(ConstantRefStdSands.REF_STD_INSTANT_METRICS_PF_INDEX_POSITION, (ConstantRefStdSands.REF_STD_INSTANT_METRICS_PF_INDEX_POSITION+8));
				PowerFactorData=IEEE754_Format.hexToFloat(PhaseInHex);
				if(ProcalFeatureEnable.SANDS_REFSTD_NEGATIVE_PF_DISPLAY_ISSUE_EXIST){
					if(PowerFactorData.startsWith("-")){
						PowerFactorData = PowerFactorData.replace("-", "");
					}
				}
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
		timer.schedule(new SandsRefStdTask(), RetryIntervalInMsec);
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



	class SandsRefStdTask extends TimerTask {
		public void run() {
			String CurrentSerialData = SerialPortObj.getSerialData();
			Integer ExpectedLength = SerialPortObj.getExpectedLength();

			if(CurrentSerialData.length()==ExpectedLength)
			{
				ApplicationLauncher.logger.info("SandsRefStdTask: Expected length received:");

				ExpectedLengthRecieved=true;
				String ExpectedResult = SerialPortObj.getExpectedResult();
				ApplicationLauncher.logger.debug("SandsRefStdTask: ExpectedResultX: " + ExpectedResult);
				ApplicationLauncher.logger.debug("SandsRefStdTask: CurrentSerialDataX: " + CurrentSerialData);
				if (CurrentSerialData.contains(ExpectedResult)){
					ExpectedResponseRecieved=true;
					setRefStd_ReadSerialData(CurrentSerialData);
					setReceivedLength(CurrentSerialData.length());
					ApplicationLauncher.logger.debug("SandsRefStdTask: Expected Data received:Length:"+CurrentSerialData.length()+":CurrentData:"+CurrentSerialData);
				}

			/*}else if (CurrentSerialData.length()>ExpectedLength){
				ApplicationLauncher.logger.info("**********************************************************");
				ApplicationLauncher.logger.info("**********************************************************");
				ApplicationLauncher.logger.info("**********************************************************");
				ApplicationLauncher.logger.info("**********************************************************");
				ApplicationLauncher.logger.info("SandsRefStdTask: unExpected received:CurrentData:"+CurrentSerialData+":CurrentLength:"+CurrentSerialData.length()+":ExpectedLength:"+SerialPortObj.getExpectedLength());
				ApplicationLauncher.logger.info("**********************************************************");
				ApplicationLauncher.logger.info("**********************************************************");
				ApplicationLauncher.logger.info("**********************************************************");
				ApplicationLauncher.logger.info("**********************************************************");
				SerialPortObj.StripLength(CurrentSerialData.length());
				ApplicationLauncher.logger.info("SandsRefStdTask :Stripped: Exceeded length data");

			}*/
			}else if( (CurrentSerialData.equals(SerialPortObj.getExpectedDataErrorResult())) || (CurrentSerialData.equals(SerialPortObj.getExpectedSetErrorResult())) ){
				ApplicationLauncher.logger.info("SerialDataMteRefStd: Unable to set the parameter:Current Data:"+CurrentSerialData+":Expected Data:"+SerialPortObj.getExpectedResult());
				FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_100+":Expected:"+SerialPortObj.getExpectedResult()+" :Actual:"+CurrentSerialData);
				SerialPortObj.StripLength(CurrentSerialData.length());
				ErrorResponseRecieved = true;
			}
			responseWaitCount --;
			if (responseWaitCount == 0 || ExpectedLengthRecieved ||
					ErrorResponseRecieved ||  ProjectExecutionController.getUserAbortedFlag()){
				responseWaitCount=0;
				ApplicationLauncher.logger.info("SandsRefStdTask :Timer Exit!");
				timer.cancel(); //Terminate the timer thread
			}
			else{
				timer.schedule(new SandsRefStdTask(), RetryIntervalInMsec);
			}
		}	
	}




	public String RefStd_DecodeSerialDataForConstTest(String DecodeData) {
		ApplicationLauncher.logger.debug("RefStd_DecodeSerialDataForConstTest : Entry");
		// TODO Auto-generated method stub

		//String DecodeData=this.RefStd_ReadSerialData;
		String metertype = displayDataObj.getDeployedEM_ModelType();
		//ApplicationLauncher.logger.info("DecodeData.length()1: "  + DecodeData.length());
		try{
			//if(DecodeData.length()>=8){
			if(DecodeData.length()>= ConstantRefStdSands.ER_READ_ACCU_ENERGY_SEC_LENGTH){	
				if(DecodeData.substring(0, 2).equals(ConstantRefStdSands.REF_STD_ACCUMULATIVE_ER)) {

					//DecodeData = DecodeData.substring(8);
					ApplicationLauncher.logger.info("DecodeData.length(): "  + DecodeData.length());

					if(DecodeData.length() == ConstantRefStdSands.ER_READ_ACCU_ENERGY_SEC_LENGTH){
						//String PhaseA = DecodeData.substring(0, Math.min(DecodeData.length(), 8));
						String epochInSec = DecodeData.substring(ConstantRefStdSands.REF_STD_ACCU_EPOCH_INDEX_POSITION, Math.min(DecodeData.length(), (ConstantRefStdSands.REF_STD_ACCU_EPOCH_INDEX_POSITION+8)));
						epochInSec = IEEE754_Format.hexToFloat(epochInSec);
						ApplicationLauncher.logger.info("RefStd_DecodeSerialDataForConstTest: epochInSec: " + epochInSec);
						
						String totalActiveEnergy = DecodeData.substring(ConstantRefStdSands.REF_STD_ACCU_TOTAL_ACTIVE_ENERGY_INDEX_POSITION, Math.min(DecodeData.length(), (ConstantRefStdSands.REF_STD_ACCU_TOTAL_ACTIVE_ENERGY_INDEX_POSITION+8)));
						totalActiveEnergy = IEEE754_Format.hexToFloat(totalActiveEnergy);
						ApplicationLauncher.logger.info("RefStd_DecodeSerialDataForConstTest: totalActiveEnergy: " + totalActiveEnergy);
						
						
						String totalReactiveEnergy = DecodeData.substring(ConstantRefStdSands.REF_STD_ACCU_TOTAL_REACTIVE_ENERGY_INDEX_POSITION, Math.min(DecodeData.length(), (ConstantRefStdSands.REF_STD_ACCU_TOTAL_REACTIVE_ENERGY_INDEX_POSITION+8)));
						totalReactiveEnergy = IEEE754_Format.hexToFloat(totalReactiveEnergy);
						ApplicationLauncher.logger.info("RefStd_DecodeSerialDataForConstTest: totalReactiveEnergy: " + totalReactiveEnergy);
						
						
						String totalApparentEnergy = DecodeData.substring(ConstantRefStdSands.REF_STD_ACCU_TOTAL_APPARENT_ENERGY_INDEX_POSITION, Math.min(DecodeData.length(), (ConstantRefStdSands.REF_STD_ACCU_TOTAL_APPARENT_ENERGY_INDEX_POSITION+8)));
						totalApparentEnergy = IEEE754_Format.hexToFloat(totalApparentEnergy);
						ApplicationLauncher.logger.info("RefStd_DecodeSerialDataForConstTest: totalApparentEnergy: " + totalApparentEnergy);
						
						
						
						String activeEnergyPhaseA = DecodeData.substring(ConstantRefStdSands.REF_STD_ACCU_R_PHASE_ACTIVE_ENERGY_INDEX_POSITION, Math.min(DecodeData.length(), (ConstantRefStdSands.REF_STD_ACCU_R_PHASE_ACTIVE_ENERGY_INDEX_POSITION+8)));
						activeEnergyPhaseA = IEEE754_Format.hexToFloat(activeEnergyPhaseA);
						
						String reactiveEnergyPhaseA = DecodeData.substring(ConstantRefStdSands.REF_STD_ACCU_R_PHASE_REACTIVE_ENERGY_INDEX_POSITION, Math.min(DecodeData.length(), (ConstantRefStdSands.REF_STD_ACCU_R_PHASE_REACTIVE_ENERGY_INDEX_POSITION+8)));
						reactiveEnergyPhaseA = IEEE754_Format.hexToFloat(reactiveEnergyPhaseA);
						
						String apparentEnergyPhaseA = DecodeData.substring(ConstantRefStdSands.REF_STD_ACCU_R_PHASE_APPARENT_ENERGY_INDEX_POSITION, Math.min(DecodeData.length(), (ConstantRefStdSands.REF_STD_ACCU_R_PHASE_APPARENT_ENERGY_INDEX_POSITION+8)));
						apparentEnergyPhaseA = IEEE754_Format.hexToFloat(apparentEnergyPhaseA);
						
						String activeEnergyPhaseB = "";
						String activeEnergyPhaseC = "";
						
						String reactiveEnergyPhaseB = "";
						String reactiveEnergyPhaseC = "";
						
						String apparentEnergyPhaseB = "";
						String apparentEnergyPhaseC = "";
						if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){

							//PhaseB = DecodeData.substring(8, Math.min(DecodeData.length(), 16));
							//PhaseC = DecodeData.substring(16, Math.min(DecodeData.length(), 24));
							activeEnergyPhaseB = DecodeData.substring(ConstantRefStdSands.REF_STD_ACCU_Y_PHASE_ACTIVE_ENERGY_INDEX_POSITION, Math.min(DecodeData.length(), (ConstantRefStdSands.REF_STD_ACCU_Y_PHASE_ACTIVE_ENERGY_INDEX_POSITION+8)));
							activeEnergyPhaseC = DecodeData.substring(ConstantRefStdSands.REF_STD_ACCU_B_PHASE_ACTIVE_ENERGY_INDEX_POSITION, Math.min(DecodeData.length(), (ConstantRefStdSands.REF_STD_ACCU_B_PHASE_ACTIVE_ENERGY_INDEX_POSITION+8)));
							activeEnergyPhaseB = IEEE754_Format.hexToFloat(activeEnergyPhaseB);
							activeEnergyPhaseC = IEEE754_Format.hexToFloat(activeEnergyPhaseC);
							
							reactiveEnergyPhaseB = DecodeData.substring(ConstantRefStdSands.REF_STD_ACCU_Y_PHASE_REACTIVE_ENERGY_INDEX_POSITION, Math.min(DecodeData.length(), (ConstantRefStdSands.REF_STD_ACCU_Y_PHASE_REACTIVE_ENERGY_INDEX_POSITION+8)));
							reactiveEnergyPhaseC = DecodeData.substring(ConstantRefStdSands.REF_STD_ACCU_B_PHASE_REACTIVE_ENERGY_INDEX_POSITION, Math.min(DecodeData.length(), (ConstantRefStdSands.REF_STD_ACCU_B_PHASE_REACTIVE_ENERGY_INDEX_POSITION+8)));
							reactiveEnergyPhaseB = IEEE754_Format.hexToFloat(reactiveEnergyPhaseB);
							reactiveEnergyPhaseC = IEEE754_Format.hexToFloat(reactiveEnergyPhaseC);
							
							apparentEnergyPhaseB = DecodeData.substring(ConstantRefStdSands.REF_STD_ACCU_Y_PHASE_APPARENT_ENERGY_INDEX_POSITION, Math.min(DecodeData.length(), (ConstantRefStdSands.REF_STD_ACCU_Y_PHASE_APPARENT_ENERGY_INDEX_POSITION+8)));
							apparentEnergyPhaseC = DecodeData.substring(ConstantRefStdSands.REF_STD_ACCU_B_PHASE_APPARENT_ENERGY_INDEX_POSITION, Math.min(DecodeData.length(), (ConstantRefStdSands.REF_STD_ACCU_B_PHASE_APPARENT_ENERGY_INDEX_POSITION+8)));
							apparentEnergyPhaseB = IEEE754_Format.hexToFloat(apparentEnergyPhaseB);
							apparentEnergyPhaseC = IEEE754_Format.hexToFloat(apparentEnergyPhaseC);
						}

						ApplicationLauncher.logger.info("activeEnergyPhaseA: " + activeEnergyPhaseA);
						ApplicationLauncher.logger.info("reactiveEnergyPhaseA: " + reactiveEnergyPhaseA);
						ApplicationLauncher.logger.info("apparentEnergyPhaseA: " + apparentEnergyPhaseA);
						ApplicationLauncher.logger.info("activeEnergyPhaseB: " + activeEnergyPhaseB);
						ApplicationLauncher.logger.info("reactiveEnergyPhaseB: " + reactiveEnergyPhaseB);
						ApplicationLauncher.logger.info("apparentEnergyPhaseB: " + apparentEnergyPhaseB);
						ApplicationLauncher.logger.info("activeEnergyPhaseC: " + activeEnergyPhaseC);
						ApplicationLauncher.logger.info("reactiveEnergyPhaseC: " + reactiveEnergyPhaseC);
						ApplicationLauncher.logger.info("apparentEnergyPhaseC: " + apparentEnergyPhaseC);
						//ApplicationLauncher.logger.info("DecodeData: " + DecodeData);
						setPhaseAReading(activeEnergyPhaseA);
						setPhaseBReading(activeEnergyPhaseB);
						setPhaseCReading(activeEnergyPhaseC);
						
						setPhaseA_ActivePowerReading(activeEnergyPhaseA);
						setPhaseB_ActivePowerReading(activeEnergyPhaseB);
						setPhaseC_ActivePowerReading(activeEnergyPhaseC);
						
						
						
						setPhaseA_ReactivePowerReading(reactiveEnergyPhaseA);
						setPhaseB_ReactivePowerReading(reactiveEnergyPhaseB);
						setPhaseC_ReactivePowerReading(reactiveEnergyPhaseC);
						
						setPhaseA_ApparentPowerReading(apparentEnergyPhaseA);
						setPhaseB_ApparentPowerReading(apparentEnergyPhaseB);
						setPhaseC_ApparentPowerReading(apparentEnergyPhaseC);
						
						setTotalActivePowerReading(totalActiveEnergy);
						setTotalReactivePowerReading(totalReactiveEnergy);
						setTotalApparentPowerReading(totalApparentEnergy);
						
						
						
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
	
	public static void parseReadModeData(String DecodeData) {
		ApplicationLauncher.logger.debug("parseReadModeData : Entry");
		// TODO Auto-generated method stub

		//String DecodeData=this.RefStd_ReadSerialData;
		//String metertype = DisplayDataObj.getDeployedEM_ModelType();
		//ApplicationLauncher.logger.info("DecodeData.length()1: "  + DecodeData.length());
		try{
			if(DecodeData.length()>=ConstantRefStdSands.ER_GET_CONFIG_SEC_LENGTH){
				if(DecodeData.substring(0, 2).equals(ConstantRefStdSands.ER_GET_CONFIG_SEC_ACK)) {

					//DecodeData = DecodeData.substring(8);
					ApplicationLauncher.logger.info("DecodeData.length(): "  + DecodeData.length());

					if(DecodeData.length() == ConstantRefStdSands.ER_GET_CONFIG_SEC_LENGTH){
						//String PhaseA = DecodeData.substring(0, Math.min(DecodeData.length(), 8));
						String m1VoltageData = DecodeData.substring(ConstantRefStdSands.GET_CONFIG_MODE_M1_INDEX_POSITION, Math.min(DecodeData.length(), (ConstantRefStdSands.GET_CONFIG_MODE_M1_INDEX_POSITION+2)));
						Data_RefStdSands.setReadConfigModeVoltage(m1VoltageData);

						String m2CurrentData = DecodeData.substring(ConstantRefStdSands.GET_CONFIG_MODE_M2_INDEX_POSITION, Math.min(DecodeData.length(), (ConstantRefStdSands.GET_CONFIG_MODE_M2_INDEX_POSITION+2)));
						Data_RefStdSands.setReadConfigModeCurrent(m2CurrentData);
						
						String m3PulseOuptPutModeData = DecodeData.substring(ConstantRefStdSands.GET_CONFIG_MODE_M3_INDEX_POSITION, Math.min(DecodeData.length(), (ConstantRefStdSands.GET_CONFIG_MODE_M3_INDEX_POSITION+2)));
						Data_RefStdSands.setReadConfigModePulseOutputUnit(m3PulseOuptPutModeData);

						ApplicationLauncher.logger.info("parseReadModeData: m1VoltageData: " + m1VoltageData);
						ApplicationLauncher.logger.info("parseReadModeData: m2CurrentData: " + m2CurrentData);
						ApplicationLauncher.logger.info("parseReadModeData: m3PulseOuptPutModeData: " + m3PulseOuptPutModeData);
					}
				}else if(DecodeData.length()<ConstantRefStdSands.ER_GET_CONFIG_SEC_LENGTH){

					ApplicationLauncher.logger.error("parseReadModeData :Error 2003");

				}
			}
		} catch (Exception e){
			e.printStackTrace();

			ApplicationLauncher.logger.error("parseReadModeData : Exception: "+ e.getMessage());
		}
		//return DecodeData;

	}
	
	public Boolean IsExpectedErrorResponseReceived(){
		return this.ErrorResponseRecieved;

	}
	
	public static boolean isLastSetModeConfigurationSame(String targetMaxVoltage, String targetMaxCurrent ){
		
		ApplicationLauncher.logger.info("isLastSetModeConfigurationSame :Entry");
		boolean status = true;
		try{
			float voltage = Float.parseFloat(targetMaxVoltage);
			float current = Float.parseFloat(targetMaxCurrent);
			
			String voltageTargetMode = ConstantRefStdSands.DATA_CONFIG_MODE_M1_VOLT_LT_240V;
			if(voltage <= ConstantRefStdSands.MODE_M1_VOLT_LEVEl1_MAX){
				voltageTargetMode = ConstantRefStdSands.DATA_CONFIG_MODE_M1_VOLT_HT_63DOT5V;
			}else if(voltage <= ConstantRefStdSands.MODE_M1_VOLT_LEVEl2_MAX){
				voltageTargetMode = ConstantRefStdSands.DATA_CONFIG_MODE_M1_VOLT_LT_240V;
			}
			ApplicationLauncher.logger.info("isLastSetModeConfigurationSame : Last Set Voltage Mode in Hex: " + displayDataObj.getSandsRefStdLastSetVoltageMode());
			ApplicationLauncher.logger.info("isLastSetModeConfigurationSame : target Voltage mode in Hex: " + voltageTargetMode);
			if(!voltageTargetMode.equals(displayDataObj.getSandsRefStdLastSetVoltageMode())){
				status = false;
				Data_RefStdSands.setWriteConfigModeVoltage(voltageTargetMode);
				ApplicationLauncher.logger.info("isLastSetModeConfigurationSame : Change required for Voltage Mode");
			}
			
			//ApplicationLauncher.logger.info("isLastSetModeConfigurationSame : target Current: " + targetMaxCurrent);
			//ApplicationLauncher.logger.info("isLastSetModeConfigurationSame : target Current float : " + current);
			
			
			
			String currentTargetMode = ConstantRefStdSands.DATA_CONFIG_MODE_M2_CURRENT_MAX_100A;
			if(current <= ConstantRefStdSands.MODE_M2_CURRENT_LEVEl1_MAX){
				//ApplicationLauncher.logger.info("isLastSetModeConfigurationSame : target Current: Hit1" );
				currentTargetMode = ConstantRefStdSands.DATA_CONFIG_MODE_M2_CURRENT_MAX_1A;
			}else if(current <= ConstantRefStdSands.MODE_M2_CURRENT_LEVEl2_MAX){
				//ApplicationLauncher.logger.info("isLastSetModeConfigurationSame : target Current: Hit2" );
				currentTargetMode = ConstantRefStdSands.DATA_CONFIG_MODE_M2_CURRENT_MAX_10A;
			}else if(current <= ConstantRefStdSands.MODE_M2_CURRENT_LEVEl3_MAX){
				//ApplicationLauncher.logger.info("isLastSetModeConfigurationSame : target Current: Hit3" );
				currentTargetMode = ConstantRefStdSands.DATA_CONFIG_MODE_M2_CURRENT_MAX_100A;
			}
			
			ApplicationLauncher.logger.info("isLastSetModeConfigurationSame : Last Set Current Mode in Hex: " + displayDataObj.getSandsRefStdLastSetCurrentMode());
			ApplicationLauncher.logger.info("isLastSetModeConfigurationSame : target Current mode in Hex: " + currentTargetMode);
			if(!currentTargetMode.equals(displayDataObj.getSandsRefStdLastSetCurrentMode())){
				status = false;
				Data_RefStdSands.setWriteConfigModeCurrent(currentTargetMode);
				ApplicationLauncher.logger.info("isLastSetModeConfigurationSame : Change required for Current Mode");
			}
						
			if(displayDataObj.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)){
				//if(getEnergyFlowMode().equals(ConstantMtePowerSource.IMPORT_MODE)){
				ApplicationLauncher.logger.info("isLastSetModeConfigurationSame : Active : Last Set Pulse Output Mode in Hex: " + displayDataObj.getSandsRefStdLastSetPulseOutputMode());
				ApplicationLauncher.logger.info("isLastSetModeConfigurationSame : Active : target Set Pulse Output Mode in Hex: " + ConstantRefStdSands.DATA_CONFIG_MODE_M3_PULSE_OUTPUT_ACTIVE_ENERGY);
				if(!displayDataObj.getSandsRefStdLastSetPulseOutputMode().equals(ConstantRefStdSands.DATA_CONFIG_MODE_M3_PULSE_OUTPUT_ACTIVE_ENERGY)){
					status = false;
					Data_RefStdSands.setWriteConfigModePulseOutputUnit(ConstantRefStdSands.DATA_CONFIG_MODE_M3_PULSE_OUTPUT_ACTIVE_ENERGY);
					ApplicationLauncher.logger.info("isLastSetModeConfigurationSame : Change required for Pulse output Active Mode");
				}
				
				//}
			}else if(displayDataObj.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)){
				//if(getEnergyFlowMode().equals(ConstantMtePowerSource.IMPORT_MODE)){
				ApplicationLauncher.logger.info("isLastSetModeConfigurationSame : Reactive : Last Set Pulse Output Mode in Hex: " + displayDataObj.getSandsRefStdLastSetPulseOutputMode());
				ApplicationLauncher.logger.info("isLastSetModeConfigurationSame : Reactive : target Set Pulse Output Mode in Hex: " + ConstantRefStdSands.DATA_CONFIG_MODE_M3_PULSE_OUTPUT_REACTIVE_ENERGY);
				
				if(!displayDataObj.getSandsRefStdLastSetPulseOutputMode().equals(ConstantRefStdSands.DATA_CONFIG_MODE_M3_PULSE_OUTPUT_REACTIVE_ENERGY)){
					status = false;
					Data_RefStdSands.setWriteConfigModePulseOutputUnit(ConstantRefStdSands.DATA_CONFIG_MODE_M3_PULSE_OUTPUT_REACTIVE_ENERGY);
					ApplicationLauncher.logger.info("isLastSetModeConfigurationSame : Change required for Pulse output Reactive Mode");
				}
				//}
			}
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("isLastSetModeConfigurationSame : Exception :"+ e.getMessage());
		}
		
		return status;
	}

	public String getPhaseA_ActivePowerReading() {
		return phaseA_ActivePowerReading;
	}

	public void setPhaseA_ActivePowerReading(String phaseA_ActivePowerReading) {
		this.phaseA_ActivePowerReading = phaseA_ActivePowerReading;
	}

	public String getPhaseB_ActivePowerReading() {
		return phaseB_ActivePowerReading;
	}

	public void setPhaseB_ActivePowerReading(String phaseB_ActivePowerReading) {
		this.phaseB_ActivePowerReading = phaseB_ActivePowerReading;
	}

	public String getPhaseC_ActivePowerReading() {
		return phaseC_ActivePowerReading;
	}

	public void setPhaseC_ActivePowerReading(String phaseC_ActivePowerReading) {
		this.phaseC_ActivePowerReading = phaseC_ActivePowerReading;
	}

	public String getTotalActivePowerReading() {
		return totalActivePowerReading;
	}

	public void setTotalActivePowerReading(String totalActivePowerReading) {
		this.totalActivePowerReading = totalActivePowerReading;
	}

	public String getPhaseA_ReactivePowerReading() {
		return phaseA_ReactivePowerReading;
	}

	public void setPhaseA_ReactivePowerReading(String phaseA_RectivePowerReading) {
		this.phaseA_ReactivePowerReading = phaseA_RectivePowerReading;
	}

	public String getPhaseB_ReactivePowerReading() {
		return phaseB_ReactivePowerReading;
	}

	public void setPhaseB_ReactivePowerReading(String phaseB_ReactivePowerReading) {
		this.phaseB_ReactivePowerReading = phaseB_ReactivePowerReading;
	}

	public String getPhaseC_ReactivePowerReading() {
		return phaseC_ReactivePowerReading;
	}

	public void setPhaseC_ReactivePowerReading(String phaseC_ReactivePowerReading) {
		this.phaseC_ReactivePowerReading = phaseC_ReactivePowerReading;
	}

	public String getTotalReactivePowerReading() {
		return totalReactivePowerReading;
	}

	public void setTotalReactivePowerReading(String totalReactivePowerReading) {
		this.totalReactivePowerReading = totalReactivePowerReading;
	}

	public String getPhaseA_ApparentPowerReading() {
		return phaseA_ApparentPowerReading;
	}

	public void setPhaseA_ApparentPowerReading(String phaseA_ApparentPowerReading) {
		this.phaseA_ApparentPowerReading = phaseA_ApparentPowerReading;
	}

	public String getPhaseB_ApparentPowerReading() {
		return phaseB_ApparentPowerReading;
	}

	public void setPhaseB_ApparentPowerReading(String phaseB_ApparentPowerReading) {
		this.phaseB_ApparentPowerReading = phaseB_ApparentPowerReading;
	}

	public String getPhaseC_ApparentPowerReading() {
		return phaseC_ApparentPowerReading;
	}

	public void setPhaseC_ApparentPowerReading(String phaseC_ApparentPowerReading) {
		this.phaseC_ApparentPowerReading = phaseC_ApparentPowerReading;
	}

	public String getTotalApparentPowerReading() {
		return totalApparentPowerReading;
	}

	public void setTotalApparentPowerReading(String totalApparentPowerReading) {
		this.totalApparentPowerReading = totalApparentPowerReading;
	}
	




}


