package com.tasnetwork.calibration.energymeter.device;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.StringUtils;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantPowerSourceMte;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdRadiant;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdKre;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.deployment.FailureManager;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
import com.tasnetwork.calibration.energymeter.message.RefStdKreMessage;
import com.tasnetwork.calibration.energymeter.util.ErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;
import com.tasnetwork.calibration.energymeter.util.TMS_FloatConversion;

public class SerialDataRefStdKre {

	
	Timer timer;
	int responseWaitCount =5;
	volatile static Communicator SerialPortObj= null;
	volatile Boolean  ExpectedLengthRecieved = false;
	volatile Boolean ExpectedResponseRecieved = false;
	int RetryIntervalInMsec=50;//400;//200;//100;//1000;
	int numberOfReadingRequired = 1;
	int numberOfReadingObtained = 0;
	
	volatile Boolean ErrorResponseRecieved = false;

/*	DataLogParse refStdDataRphase = new DataLogParse();
	DataLogParse refStdDataYphase = new DataLogParse();
	DataLogParse refStdDataBphase = new DataLogParse();*/

	static DeviceDataManagerController DisplayDataObj =  new DeviceDataManagerController();

	public String rPhaseVoltageDisplayData="";
	public String yPhaseVoltageDisplayData="";
	public String bPhaseVoltageDisplayData="";
	public String rPhaseCurrentDisplayData="";
	public String yPhaseCurrentDisplayData="";
	public String bPhaseCurrentDisplayData="";
	public String rPhaseDegreePhaseData="";
	public String yPhaseDegreePhaseData="";
	public String bPhaseDegreePhaseData="";
	public String rPhasePowerFactorData="";
	public String yPhasePowerFactorData="";
	public String bPhasePowerFactorData="";
	public String rPhaseFreqDisplayData="";
	public String yPhaseFreqDisplayData="";
	public String bPhaseFreqDisplayData="";
	public String rPhaseActivePowerDisplayData="";
	public String yPhaseActivePowerDisplayData="";
	public String bPhaseActivePowerDisplayData="";
	
	public String rPhaseReactivePowerDisplayData="";
	public String yPhaseReactivePowerDisplayData="";
	public String bPhaseReactivePowerDisplayData="";
	
	public String rPhaseApparentPowerDisplayData="";
	public String yPhaseApparentPowerDisplayData="";
	public String bPhaseApparentPowerDisplayData="";
	public String VA_DisplayData="";
	public String VAR_DisplayData="";
	
	public String rPhaseActiveEnergyAccumulatedDisplayData="";
	public String yPhaseActiveEnergyAccumulatedDisplayData="";
	public String bPhaseActiveEnergyAccumulatedDisplayData="";
	
	public String rPhaseReactiveEnergyAccumulatedDisplayData="";	
	public String yPhaseReactiveEnergyAccumulatedDisplayData="";
	public String bPhaseReactiveEnergyAccumulatedDisplayData="";
	
	public String rPhaseApparentEnergyAccumulatedDisplayData="";	
	public String yPhaseApparentEnergyAccumulatedDisplayData="";
	public String bPhaseApparentEnergyAccumulatedDisplayData="";
	
	static String RefStd_ReadSerialData = "";
	public int ReceivedLength = 0;
	String PhaseAReading = "";
	String PhaseBReading = "";
	String PhaseCReading = "";


	public SerialDataRefStdKre( ){
		
	}

	public SerialDataRefStdKre(Communicator inpSerialPortObj ){
		SerialPortObj = inpSerialPortObj;
		
	}

	public static void setRefStd_ReadSerialData(String currentSerialData) {
		// TODO Auto-generated method stub

		RefStd_ReadSerialData = currentSerialData;

	}
	
	public int getNumberOfReadingRequired() {
		return numberOfReadingRequired;
	}

	public void setNumberOfReadingRequired(int numberOfReadings) {
		this.numberOfReadingRequired = numberOfReadings;
	}

	public int getNumberOfReadingObtained() {
		return numberOfReadingObtained;
	}

	public void setNumberOfReadingObtained(int numberOfReadingObtained) {
		this.numberOfReadingObtained = numberOfReadingObtained;
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
	
	public Boolean IsExpectedErrorResponseReceived(){
		return this.ErrorResponseRecieved;

	}



	public String ParseVoltageDatafromRefStd(String SerialInputData ){


		//ApplicationLauncher.logger.debug("ParseVoltageDatafromRefStd: Entry");
		//ApplicationLauncher.logger.debug("ParseVoltageDatafromRefStd: SerialInputData1: " + SerialInputData);
		SerialInputData = SerialInputData.replace("0D","");
		SerialInputData = GuiUtils.hexToAscii(SerialInputData);
		//ApplicationLauncher.logger.debug("ParseVoltageDatafromRefStd: SerialInputData2: " + SerialInputData);
		String VoltageInHex = null;
		rPhaseVoltageDisplayData="";
		yPhaseVoltageDisplayData="";
		bPhaseVoltageDisplayData="";
		try{
			
			String[] parsedData = SerialInputData.split(ConstantRefStdKre.ER_REF_STD_SEPERATOR);//",");
			//ApplicationLauncher.logger.debug("ParseVoltageDatafromRefStd: no of payload: "+ parsedData.length);
			if(parsedData.length>3){
				for(int i = 0 ; i < parsedData.length; i++){
					//ApplicationLauncher.logger.debug("ParseVoltageDatafromRefStd: Index: " + i + " : "+ parsedData[i]);
				}
				rPhaseVoltageDisplayData = parsedData[1];
				yPhaseVoltageDisplayData = parsedData[2];
				bPhaseVoltageDisplayData = parsedData[3];
			}
			
			//rPhaseVoltageDisplayData="20.1";
			//yPhaseVoltageDisplayData="21.2";
			//bPhaseVoltageDisplayData="22.3";
			//if(SerialInputData.length()>=(ConstantRadiantRefStd.REF_STD_INSTANT_METRICS_VOLTAGE_POSITION+8)){
				//VoltageInHex =  SerialInputData.substring(48, 48+8);gjghk
				//VoltageInHex =  SerialInputData.substring(ConstantRadiantRefStd.REF_STD_INSTANT_METRICS_VOLTAGE_POSITION, (ConstantRadiantRefStd.REF_STD_INSTANT_METRICS_VOLTAGE_POSITION+8));
				//rPhaseVoltageDisplayData=TMS_FloatConversion.hextofloat(VoltageInHex);
			//}
		}catch (Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("ParseVoltageDatafromRefStd: Exception :"+E.toString());

		}
/*		refStdDataRphase.setVoltage(rPhaseVoltageDisplayData);
		refStdDataYphase.setVoltage(yPhaseVoltageDisplayData);
		refStdDataBphase.setVoltage(bPhaseVoltageDisplayData);*/
		
		return rPhaseVoltageDisplayData;
	}
	
	public String ParseFreqDatafromRefStd(String SerialInputData ){


		//ApplicationLauncher.logger.debug("ParseFreqDatafromRefStd: Entry");
		//ApplicationLauncher.logger.debug("ParseFreqDatafromRefStd: SerialInputData1: " + SerialInputData);
		SerialInputData = SerialInputData.replace("0D","");
		SerialInputData = GuiUtils.hexToAscii(SerialInputData);
		//ApplicationLauncher.logger.debug("ParseFreqDatafromRefStd: SerialInputData2: " + SerialInputData);
		String VoltageInHex = null;
		rPhaseFreqDisplayData="";
		yPhaseFreqDisplayData="";
		bPhaseFreqDisplayData="";
		try{
			
			String[] parsedData = SerialInputData.split(ConstantRefStdKre.ER_REF_STD_SEPERATOR);//",");
			//ApplicationLauncher.logger.debug("ParseFreqDatafromRefStd: no of payload: "+ parsedData.length);
			if(parsedData.length>1){
				for(int i = 0 ; i < parsedData.length; i++){
					//ApplicationLauncher.logger.debug("ParseFreqDatafromRefStd: Index: " + i + " : "+ parsedData[i]);
				}
				rPhaseFreqDisplayData = parsedData[1];
				yPhaseFreqDisplayData = parsedData[1];
				bPhaseFreqDisplayData = parsedData[1];
			}
			
			//rPhaseVoltageDisplayData="20.1";
			//yPhaseVoltageDisplayData="21.2";
			//bPhaseVoltageDisplayData="22.3";
			//if(SerialInputData.length()>=(ConstantRadiantRefStd.REF_STD_INSTANT_METRICS_VOLTAGE_POSITION+8)){
				//VoltageInHex =  SerialInputData.substring(48, 48+8);gjghk
				//VoltageInHex =  SerialInputData.substring(ConstantRadiantRefStd.REF_STD_INSTANT_METRICS_VOLTAGE_POSITION, (ConstantRadiantRefStd.REF_STD_INSTANT_METRICS_VOLTAGE_POSITION+8));
				//rPhaseVoltageDisplayData=TMS_FloatConversion.hextofloat(VoltageInHex);
			//}
		}catch (Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("ParseFreqDatafromRefStd: Exception :"+E.toString());

		}
/*		refStdDataRphase.setFreqency(rPhaseFreqDisplayData);
		refStdDataYphase.setFreqency(yPhaseFreqDisplayData);
		refStdDataBphase.setFreqency(bPhaseFreqDisplayData);*/
		return rPhaseFreqDisplayData;
	}
	
	

	public String ParseCurrentDatafromRefStd(String SerialInputData ){


		ApplicationLauncher.logger.debug("ParseCurrentDatafromRefStd: Entry");
		//ApplicationLauncher.logger.debug("ParseCurrentDatafromRefStd: SerialInputData1: " + SerialInputData);
		SerialInputData = SerialInputData.replace("0D","");
		SerialInputData = GuiUtils.hexToAscii(SerialInputData);
		//ApplicationLauncher.logger.debug("ParseCurrentDatafromRefStd: SerialInputData2: " + SerialInputData);
		String VoltageInHex = null;
		rPhaseCurrentDisplayData="";
		yPhaseCurrentDisplayData="";
		bPhaseCurrentDisplayData="";
		try{
			
			String[] parsedData = SerialInputData.split(ConstantRefStdKre.ER_REF_STD_SEPERATOR);//",");
			//ApplicationLauncher.logger.debug("ParseCurrentDatafromRefStd: no of payload: "+ parsedData.length);
			if(parsedData.length>3){
				for(int i = 0 ; i < parsedData.length; i++){
					//ApplicationLauncher.logger.debug("ParseCurrentDatafromRefStd: Index: " + i + " : "+ parsedData[i]);
				}
				rPhaseCurrentDisplayData = parsedData[1];
				yPhaseCurrentDisplayData = parsedData[2];
				bPhaseCurrentDisplayData = parsedData[3];
			}

		}catch (Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("ParseCurrentDatafromRefStd: Exception :"+E.toString());

		}
		
/*		refStdDataRphase.setCurrent(rPhaseCurrentDisplayData);
		refStdDataYphase.setCurrent(yPhaseCurrentDisplayData);
		refStdDataBphase.setCurrent(bPhaseCurrentDisplayData);*/
		return rPhaseCurrentDisplayData;
	}
	
	
	// power factor data not available from the Mte refstandard, hence calculated with formula pf = ActivePower/ ApparentPower suggested by MTE, Mr VinayGupa
	public String ParsePowerFactorDatafromRefStd(String SerialInputData ){


		ApplicationLauncher.logger.debug("ParsePowerFactorDatafromRefStd: Entry");
		//ApplicationLauncher.logger.debug("ParseCurrentDatafromRefStd: SerialInputData1: " + SerialInputData);
		SerialInputData = SerialInputData.replace("0D","");
		SerialInputData = GuiUtils.hexToAscii(SerialInputData);
		//ApplicationLauncher.logger.debug("ParseCurrentDatafromRefStd: SerialInputData2: " + SerialInputData);
		String VoltageInHex = null;
		rPhasePowerFactorData="";
		yPhasePowerFactorData="";
		bPhasePowerFactorData="";
		try{
			if(!SerialInputData.isEmpty()) {
				//if(!SerialInputData.contains("---")) {
					String[] parsedData = SerialInputData.split(ConstantRefStdKre.ER_REF_STD_SEPERATOR);//",");
					//ApplicationLauncher.logger.debug("ParseCurrentDatafromRefStd: no of payload: "+ parsedData.length);
					if(parsedData.length>3){
						for(int i = 0 ; i < parsedData.length; i++){
							//ApplicationLauncher.logger.debug("ParseCurrentDatafromRefStd: Index: " + i + " : "+ parsedData[i]);
						}
						// power factor data not available from the Mte refstandard, hence calculated with formula pf = ActivePower/ ApparentPower suggested by MTE, Mr VinayGupa
						if(!parsedData[1].contains("---")) {
							rPhasePowerFactorData = String.format("%#.04f",(Float.parseFloat(ProjectExecutionController.refStdDisplayR_PhaseWatt.getValue())/Float.parseFloat(parsedData[1])));
						}
						if(!parsedData[2].contains("---")) {
							//yPhasePowerFactorData = String.format("%#.04f",(Float.parseFloat(yPhaseWattDisplayData)/Float.parseFloat(parsedData[2])));
						}
						if(!parsedData[3].contains("---")) {
							//bPhasePowerFactorData = String.format("%#.04f",(Float.parseFloat(bPhaseWattDisplayData)/Float.parseFloat(parsedData[3])));
					
						}
					}
				/*}else {
					ApplicationLauncher.logger.debug("ParsePowerFactorDatafromRefStd: data contains hyphen");
				}*/
			}else {
				ApplicationLauncher.logger.debug("ParsePowerFactorDatafromRefStd: data empty");
			}

		}catch (Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("ParsePowerFactorDatafromRefStd: Exception :"+E.toString());

		}
		return rPhasePowerFactorData;
	}
	
	// power factor data not available from the Mte refstandard, hence calculated with formula pf = ActivePower/ ApparentPower suggested by MTE, Mr VinayGupa
	public String ParsePowerFactorDatafromRefStdV2(String SerialInputData ){


		ApplicationLauncher.logger.debug("ParsePowerFactorDatafromRefStdV2: Entry");
		//ApplicationLauncher.logger.debug("ParseCurrentDatafromRefStd: SerialInputData1: " + SerialInputData);
		SerialInputData = SerialInputData.replace("0D","");
		SerialInputData = GuiUtils.hexToAscii(SerialInputData);
		//ApplicationLauncher.logger.debug("ParseCurrentDatafromRefStd: SerialInputData2: " + SerialInputData);
		String VoltageInHex = null;
		rPhasePowerFactorData="";
		yPhasePowerFactorData="";
		bPhasePowerFactorData="";
		try{
			if(!SerialInputData.isEmpty()) {
				//if(!SerialInputData.contains("---")) {
					String[] parsedData = SerialInputData.split(ConstantRefStdKre.ER_REF_STD_SEPERATOR);//",");
					//ApplicationLauncher.logger.debug("ParseCurrentDatafromRefStd: no of payload: "+ parsedData.length);
					if(parsedData.length>3){
						for(int i = 0 ; i < parsedData.length; i++){
							//ApplicationLauncher.logger.debug("ParseCurrentDatafromRefStd: Index: " + i + " : "+ parsedData[i]);
						}
						// power factor data not available from the Mte refstandard, hence calculated with formula pf = ActivePower/ ApparentPower suggested by MTE, Mr VinayGupa
						try{
							if( (!parsedData[1].contains("---")) && (!rPhaseActivePowerDisplayData.contains("---")) && (!rPhaseActivePowerDisplayData.isEmpty()) ) {
								if(Float.parseFloat(parsedData[1])!=0.0f){
									rPhasePowerFactorData = String.format("%#.04f",(Float.parseFloat(rPhaseActivePowerDisplayData)/Float.parseFloat(parsedData[1])));
									ApplicationLauncher.logger.debug("ParsePowerFactorDatafromRefStdV2 : rPhasePowerFactorData : "+ rPhasePowerFactorData);
								
								}
							}
							
						}catch (Exception e){
							e.printStackTrace();
							ApplicationLauncher.logger.error("ParsePowerFactorDatafromRefStdV2: B Phase Exception :"+e.toString());
	
						}
						try{	
							if ( (!parsedData[2].contains("---"))  && (!yPhaseActivePowerDisplayData.contains("---")) && (!yPhaseActivePowerDisplayData.isEmpty()) ){
								if(Float.parseFloat(parsedData[2])!=0.0f){
									yPhasePowerFactorData = String.format("%#.04f",(Float.parseFloat(yPhaseActivePowerDisplayData)/Float.parseFloat(parsedData[2])));
									ApplicationLauncher.logger.debug("ParsePowerFactorDatafromRefStdV2 : yPhasePowerFactorData : "+ yPhasePowerFactorData);
								}
							}
						
						}catch (Exception e){
							e.printStackTrace();
							ApplicationLauncher.logger.error("ParsePowerFactorDatafromRefStdV2: Y Phase Exception :"+e.toString());
	
						}
						try{
							
							if( (!parsedData[3].contains("---")) && (!bPhaseActivePowerDisplayData.contains("---")) && (!bPhaseActivePowerDisplayData.isEmpty()) ){
								if(Float.parseFloat(parsedData[3])!=0.0f){
									bPhasePowerFactorData = String.format("%#.04f",(Float.parseFloat(bPhaseActivePowerDisplayData)/Float.parseFloat(parsedData[3])));
									ApplicationLauncher.logger.debug("ParsePowerFactorDatafromRefStdV2 : bPhasePowerFactorData : "+ bPhasePowerFactorData);
								}
							}
						}catch (Exception e){
							e.printStackTrace();
							ApplicationLauncher.logger.error("ParsePowerFactorDatafromRefStdV2: B Phase Exception :"+e.toString());
	
						}
					}
				/*}else {
					ApplicationLauncher.logger.debug("ParsePowerFactorDatafromRefStdV2: data contains hyphen");
				}*/
			}else {
				ApplicationLauncher.logger.debug("ParsePowerFactorDatafromRefStdV2: data empty");
			}

		}catch (Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("ParsePowerFactorDatafromRefStdV2: Exception :"+E.toString());

		}
		
/*		refStdDataRphase.setPf(rPhasePowerFactorData);
		refStdDataYphase.setPf(yPhasePowerFactorData);
		refStdDataBphase.setPf(bPhasePowerFactorData);*/
		
		return rPhasePowerFactorData;
	}
	
	
	public String ParseWattDatafromRefStd(String SerialInputData ){


		//ApplicationLauncher.logger.debug("ParseWattDatafromRefStd: Entry");
		//ApplicationLauncher.logger.debug("ParseWattDatafromRefStd: SerialInputData1: " + SerialInputData);
		SerialInputData = SerialInputData.replace("0D","");
		SerialInputData = GuiUtils.hexToAscii(SerialInputData);
		//ApplicationLauncher.logger.debug("ParseWattDatafromRefStd: SerialInputData2: " + SerialInputData);
		String VoltageInHex = null;
		rPhaseActivePowerDisplayData="";
		yPhaseActivePowerDisplayData="";
		bPhaseActivePowerDisplayData="";
		try{
			
			String[] parsedData = SerialInputData.split(ConstantRefStdKre.ER_REF_STD_SEPERATOR);//",");
			//ApplicationLauncher.logger.debug("ParseWattDatafromRefStd: no of payload: "+ parsedData.length);
			if(parsedData.length>3){
				for(int i = 0 ; i < parsedData.length; i++){
					//ApplicationLauncher.logger.debug("ParseWattDatafromRefStd: Index: " + i + " : "+ parsedData[i]);
				}
				rPhaseActivePowerDisplayData = parsedData[1];
				yPhaseActivePowerDisplayData = parsedData[2];
				bPhaseActivePowerDisplayData = parsedData[3];
			}

		}catch (Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("ParseWattDatafromRefStd: Exception :"+E.toString());

		}
		
/*		refStdDataRphase.setActivePowerWatt(rPhaseWattDisplayData);
		refStdDataYphase.setActivePowerWatt(yPhaseWattDisplayData);
		refStdDataBphase.setActivePowerWatt(bPhaseWattDisplayData);*/
		
		return rPhaseActivePowerDisplayData;
	}
	
	

	public String ParseDegreePhaseDatafromRefStd(String SerialInputData ){


		//ApplicationLauncher.logger.debug("ParseDegreePhaseDatafromRefStd: Entry");
		//ApplicationLauncher.logger.debug("ParseDegreePhaseDatafromRefStd: SerialInputData1: " + SerialInputData);
		SerialInputData = SerialInputData.replace("0D","");
		SerialInputData = GuiUtils.hexToAscii(SerialInputData);
		//ApplicationLauncher.logger.debug("ParseDegreePhaseDatafromRefStd: SerialInputData2: " + SerialInputData);
		String VoltageInHex = null;
		rPhaseDegreePhaseData="";
		yPhaseDegreePhaseData="";
		bPhaseDegreePhaseData="";
		try{
			
			String[] parsedData = SerialInputData.split(ConstantRefStdKre.ER_REF_STD_SEPERATOR);//",");
			//ApplicationLauncher.logger.debug("ParseDegreePhaseDatafromRefStd: no of payload: "+ parsedData.length);
			if(parsedData.length>3){
				for(int i = 0 ; i < parsedData.length; i++){
					//ApplicationLauncher.logger.debug("ParseDegreePhaseDatafromRefStd: Index: " + i + " : "+ parsedData[i]);
				}
				rPhaseDegreePhaseData = parsedData[1];
				yPhaseDegreePhaseData = parsedData[2];
				bPhaseDegreePhaseData = parsedData[3];
			}

		}catch (Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("ParseDegreePhaseDatafromRefStd: Exception :"+E.toString());

		}
		
/*		refStdDataRphase.setDegreePhaseAngle(rPhaseDegreePhaseData);
		refStdDataYphase.setDegreePhaseAngle(rPhaseDegreePhaseData);
		refStdDataBphase.setDegreePhaseAngle(rPhaseDegreePhaseData);*/
		
		return rPhaseDegreePhaseData;
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



/*	public String  ParseCurrentDatafromRefStd(String SerialInputData){

		ApplicationLauncher.logger.debug("ParseCurrentDatafromRefStd: Entry");
		String CurrentInHex = null;
		CurrentDisplayData="";
		try{
			if(SerialInputData.length()>=(ConstantRadiantRefStd.REF_STD_INSTANT_METRICS_CURRENT_POSITION+8)){
				//CurrentInHex =  SerialInputData.substring(56, 56+8);hgkhj
				CurrentInHex =  SerialInputData.substring(ConstantRadiantRefStd.REF_STD_INSTANT_METRICS_CURRENT_POSITION, (ConstantRadiantRefStd.REF_STD_INSTANT_METRICS_CURRENT_POSITION+8));
				CurrentDisplayData=TMS_FloatConversion.hextofloat(CurrentInHex);
			}
		}catch (Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("ParseCurrentDatafromRefStd: Exception :"+E.getMessage());

		}
		return CurrentDisplayData;
	}
*/
/*	public String ParseWattDatafromRefStd(String SerialInputData){

		ApplicationLauncher.logger.debug("ParseWattDatafromRefStd: Entry");
		String WattInHex = null;
		WattDisplayData ="";
		try{
			if(SerialInputData.length()>=(ConstantRadiantRefStd.REF_STD_INSTANT_METRICS_WATT_POSITION+8)){
				//WattInHex =  SerialInputData.substring(64, 64+8);ghkhjk
				WattInHex =  SerialInputData.substring(ConstantRadiantRefStd.REF_STD_INSTANT_METRICS_WATT_POSITION, (ConstantRadiantRefStd.REF_STD_INSTANT_METRICS_WATT_POSITION+8));
				WattDisplayData=TMS_FloatConversion.hextofloat(WattInHex);
			}
		}catch (Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("ParseWattDatafromRefStd: Exception :"+E.getMessage());

		}
		return WattDisplayData;
	}*/

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
	
	

/*	public String ParseFreqDatafromRefStd(String SerialInputData){

		ApplicationLauncher.logger.debug("ParseFreqDatafromRefStd: Entry");
		String FreqInHex = null;
		FreqDisplayData="";
		try{
			if(SerialInputData.length()>=(ConstantRadiantRefStd.REF_STD_INSTANT_METRICS_FREQ_POSITION+8)){
				//FreqInHex =  SerialInputData.substring(88, 88+8);gyhkhgk
				FreqInHex =  SerialInputData.substring(ConstantRadiantRefStd.REF_STD_INSTANT_METRICS_FREQ_POSITION, (ConstantRadiantRefStd.REF_STD_INSTANT_METRICS_FREQ_POSITION+8));
				FreqDisplayData=TMS_FloatConversion.hextofloat(FreqInHex);
			}
		}catch (Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("ParseFreqDatafromRefStd: Exception :"+E.getMessage());

		}
		return FreqDisplayData;
	}*/

	/*public String  ParseDegreePhaseDatafromRefStd(String SerialInputData){

		ApplicationLauncher.logger.debug("ParseDegreePhaseDatafromRefStd: Entry");
		String PhaseInHex = null;
		DegreePhaseData="";
		try{
			if(SerialInputData.length()>=(ConstantRadiantRefStd.REF_STD_INSTANT_METRICS_DEGREE_POSITION+8)){
				//PhaseInHex =  SerialInputData.substring(96, 96+8);gykhfjk
				PhaseInHex =  SerialInputData.substring(ConstantRadiantRefStd.REF_STD_INSTANT_METRICS_DEGREE_POSITION, (ConstantRadiantRefStd.REF_STD_INSTANT_METRICS_DEGREE_POSITION+8));
				DegreePhaseData=TMS_FloatConversion.hextofloat(PhaseInHex);
			}
		}catch (Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("ParseDegreePhaseDatafromRefStd: Exception :"+E.getMessage());

		}
		return DegreePhaseData;
	}
*/
/*	public String  ParsePowerFactorDatafromRefStd(String SerialInputData){

		ApplicationLauncher.logger.debug("ParsePowerFactorDatafromRefStd: Entry");
		String PhaseInHex = null;
		PowerFactorData="";
		try{
			if(SerialInputData.length()>=(ConstantRadiantRefStd.REF_STD_INSTANT_METRICS_PF_POSITION+8)){
				//PhaseInHex = SerialInputData.substring(104, (104+8));gkhjk
				PhaseInHex = SerialInputData.substring(ConstantRadiantRefStd.REF_STD_INSTANT_METRICS_PF_POSITION, (ConstantRadiantRefStd.REF_STD_INSTANT_METRICS_PF_POSITION+8));
				PowerFactorData=TMS_FloatConversion.hextofloat(PhaseInHex);
			}
		}catch (Exception E){
			E.printStackTrace();
			ApplicationLauncher.logger.error("ParsePowerFactorDatafromRefStd: Exception :"+E.getMessage());

		}
		return PowerFactorData;
	}*/




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
		timer.schedule(new KreRefStdTask(), RetryIntervalInMsec);
		responseWaitCount = waitTimeCount;
		//setNumberOfReadingRequired( noOfReadingRequired);
		

	}
	
	public void SerialReponseTimerStartV2(int waitTimeCount, int noOfReadingRequired) {
		timer = new Timer();
		ApplicationLauncher.logger.debug("SerialReponseTimerStartV2: RetryIntervalInMsec: " +RetryIntervalInMsec);
		//timer.schedule(new KreRefStdTaskV2(), RetryIntervalInMsec);
		timer.schedule(new KreRefStdTaskV2(), 1);
		responseWaitCount = waitTimeCount;
		ApplicationLauncher.logger.debug("SerialReponseTimerStartV2: responseWaitCount: " +responseWaitCount);
		setNumberOfReadingRequired( noOfReadingRequired);
		

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



	
	class KreRefStdTaskV2 extends TimerTask {
		public void run() {
			String CurrentSerialData = SerialPortObj.getSerialData();
			//ApplicationLauncher.logger.debug("PowerSrcTask: responseWaitCount: "+responseWaitCount);
			//ApplicationLauncher.logger.debug("KreRefStdTaskV2: SerialData:"+CurrentSerialData);
			String ExpectedResult = SerialPortObj.getExpectedResult();
			//ApplicationLauncher.logger.debug("KreRefStdTaskV2: ExpectedResult:"+ExpectedResult);
			if(CurrentSerialData.equals(ExpectedResult) || CurrentSerialData.contains(ExpectedResult))
			{
				//if(CurrentSerialData.endsWith(ConstantRefStdKre.ER_TERMINATOR)) {
				if(CurrentSerialData.endsWith(GuiUtils.hexToAscii(ConstantRefStdKre.ER_TERMINATOR))) {	
					//int terminatorCount = StringUtils.countMatches(CurrentSerialData, ConstantRefStdKre.ER_TERMINATOR);
					//ApplicationLauncher.logger.debug("KreRefStdTaskV2 : terminatorCount1 :" + terminatorCount);
					//ApplicationLauncher.logger.debug("KreRefStdTaskV2 : CurrentSerialData :" + CurrentSerialData);
					//if(terminatorCount >= getNumberOfReadingRequired()){
				
						ApplicationLauncher.logger.info("SerialDataRefStdKre Expected received:");
						//ApplicationLauncher.logger.debug("SerialDataRefStdKre: Expected recieved:Current Data:"+CurrentSerialData+"\n");
						//SerialPortObj.StripLength(CurrentSerialData.length());
						ExpectedResponseRecieved=true; 
					//}
				}
			}
			else if (CurrentSerialData.length()>0){
				if( (CurrentSerialData.equals(SerialPortObj.getExpectedDataErrorResult())) || (CurrentSerialData.equals(SerialPortObj.getExpectedSetErrorResult())) ){
					
					ApplicationLauncher.logger.info("SerialDataRefStdKre: Unable to set the parameter:Current Data:"+CurrentSerialData+":Expected Data:"+SerialPortObj.getExpectedResult());
					FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_100+":Expected:"+SerialPortObj.getExpectedResult()+" :Actual:"+CurrentSerialData);
					SerialPortObj.StripLength(CurrentSerialData.length());
					ErrorResponseRecieved = true;
				}/*else if(CurrentSerialData.length()>ExpectedResult.length()){
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("SerialDataRefStdKre unExpected received1:Current Data:"+CurrentSerialData+":Expected Data:"+SerialPortObj.getExpectedResult());
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					if( (CurrentSerialData.contains(SerialPortObj.getExpectedDataErrorResult()) ) || ((CurrentSerialData.contains(SerialPortObj.getExpectedSetErrorResult()))) ){
						ApplicationLauncher.logger.info("SerialDataRefStdKre: Expected error response contained in received response");
						FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_100A+":Expected:"+SerialPortObj.getExpectedResult()+" :Actual:"+CurrentSerialData);
						ErrorResponseRecieved = true;
					}
					if(CurrentSerialData.contains(ExpectedResult)){
						if(CurrentSerialData.endsWith(ConstantRefStdKre.ER_TERMINATOR)) {
							int terminatorCount = StringUtils.countMatches(CurrentSerialData, ConstantRefStdKre.ER_TERMINATOR);
							ApplicationLauncher.logger.debug("KreRefStdTaskV2 : terminatorCount2 :" + terminatorCount);
							
							if(terminatorCount >= getNumberOfReadingRequired()){
								ApplicationLauncher.logger.info("SerialDataRefStdKre: Expected response contained in received response");
								ExpectedResponseRecieved=true;
							}
						}
					}

					SerialPortObj.StripLength(CurrentSerialData.length());
				}*/else if( (CurrentSerialData.contains(SerialPortObj.getExpectedDataErrorResult()) ) || ((CurrentSerialData.contains(SerialPortObj.getExpectedSetErrorResult()))) ){
					ApplicationLauncher.logger.info("SerialDataRefStdKre: Expected error response contained in received response2");
					FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_100A+":Expected:"+SerialPortObj.getExpectedResult()+" :Actual:"+CurrentSerialData);
					ErrorResponseRecieved = true;
					
				}else if (CurrentSerialData.endsWith(ConstantRefStdKre.ER_TERMINATOR)) {
					ExpectedResponseRecieved=true;
					ApplicationLauncher.logger.debug("SerialDataRefStdKre: Expected recieved2");

					//FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_100B+":Expected:"+SerialPortObj.getExpectedResult()+" :Actual:"+CurrentSerialData);
					//SerialPortObj.StripLength(CurrentSerialData.length());
				}else {
				
/*					ApplicationLauncher.logger.debug("SerialDataRefStdKre: Expected recieved: getExpectedDataErrorResult :"+SerialPortObj.getExpectedDataErrorResult()+"\n");
					ApplicationLauncher.logger.debug("SerialDataRefStdKre: Expected recieved: getExpectedSetErrorResult :"+SerialPortObj.getExpectedSetErrorResult()+"\n");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("SerialDataRefStdKre unExpected received2:Current Data:"+CurrentSerialData+":Expected Data:"+SerialPortObj.getExpectedResult());
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_100B+":Expected:"+SerialPortObj.getExpectedResult()+" :Actual:"+CurrentSerialData);
					*/
					
					//SerialPortObj.StripLength(CurrentSerialData.length());
				}
			}
			responseWaitCount --;
			//ApplicationLauncher.logger.debug("KreRefStdTaskV2: responseWaitCount: "+responseWaitCount);
			//ApplicationLauncher.logger.debug("KreRefStdTaskV2: ExpectedResponseRecieved: "+ExpectedResponseRecieved);
			//ApplicationLauncher.logger.debug("KreRefStdTaskV2: ErrorResponseRecieved: "+ErrorResponseRecieved);
			//ApplicationLauncher.logger.debug("KreRefStdTaskV2: getUserAbortedFlag(): "+ProjectExecutionController.getUserAbortedFlag());
			if (responseWaitCount == 0 || ExpectedResponseRecieved ||
					ErrorResponseRecieved ||  
					ProjectExecutionController.getUserAbortedFlag()){
				responseWaitCount=0;
				ApplicationLauncher.logger.info("SerialDataRefStdKre :Timer Exit!");
				timer.cancel(); //Terminate the timer thread
			}
			else{
				//ApplicationLauncher.logger.debug("SerialDataRefStdKre: Renewed: RetryIntervalInMsec: " +RetryIntervalInMsec);
				//ApplicationLauncher.logger.debug("SerialDataRefStdKre: RetryIntervalInMsec: " +RetryIntervalInMsec);
				timer.schedule(new KreRefStdTaskV2(), RetryIntervalInMsec);
			}
		}



	}
	
	class KreRefStdTask extends TimerTask {
		public void run() {
			String CurrentSerialData = SerialPortObj.getSerialData();
			//ApplicationLauncher.logger.debug("PowerSrcTask: responseWaitCount: "+responseWaitCount);
			ApplicationLauncher.logger.debug("KreRefStdTask: SerialData:"+CurrentSerialData+"\n");
			String ExpectedResult = SerialPortObj.getExpectedResult();
			ApplicationLauncher.logger.debug("KreRefStdTask: ExpectedResult:"+ExpectedResult+"\n");
			if(CurrentSerialData.equals(ExpectedResult) || CurrentSerialData.contains(ExpectedResult))
			{
				if(CurrentSerialData.endsWith(ConstantRefStdKre.ER_TERMINATOR)) {
					
				
					ApplicationLauncher.logger.info("KreRefStdTask Expected received:");
					//ApplicationLauncher.logger.debug("SerialDataRefStdKre: Expected recieved:Current Data:"+CurrentSerialData+"\n");
					//SerialPortObj.StripLength(CurrentSerialData.length());
					ExpectedResponseRecieved=true; 
				}
			}
			else if (CurrentSerialData.length()>0){
				if( (CurrentSerialData.equals(SerialPortObj.getExpectedDataErrorResult())) || (CurrentSerialData.equals(SerialPortObj.getExpectedSetErrorResult())) ){
					ApplicationLauncher.logger.info("KreRefStdTask: Unable to set the parameter:Current Data:"+CurrentSerialData+":Expected Data:"+SerialPortObj.getExpectedResult());
					FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_100+":Expected:"+SerialPortObj.getExpectedResult()+" :Actual:"+CurrentSerialData);
					SerialPortObj.StripLength(CurrentSerialData.length());
					ErrorResponseRecieved = true;
				}else if(CurrentSerialData.length()>ExpectedResult.length()){
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("KreRefStdTask unExpected received1:Current Data:"+CurrentSerialData+":Expected Data:"+SerialPortObj.getExpectedResult());
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					if( (CurrentSerialData.contains(SerialPortObj.getExpectedDataErrorResult()) ) || ((CurrentSerialData.contains(SerialPortObj.getExpectedSetErrorResult()))) ){
						ApplicationLauncher.logger.info("KreRefStdTask: Expected error response contained in received response");
						FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_100A+":Expected:"+SerialPortObj.getExpectedResult()+" :Actual:"+CurrentSerialData);
						ErrorResponseRecieved = true;
					}
					if(CurrentSerialData.contains(ExpectedResult)){
						if(CurrentSerialData.endsWith(ConstantRefStdKre.ER_TERMINATOR)) {
							ApplicationLauncher.logger.info("KreRefStdTask: Expected response contained in received response");
							ExpectedResponseRecieved=true;
						}
					}

					SerialPortObj.StripLength(CurrentSerialData.length());
				}else if( (CurrentSerialData.contains(SerialPortObj.getExpectedDataErrorResult()) ) || ((CurrentSerialData.contains(SerialPortObj.getExpectedSetErrorResult()))) ){
					ApplicationLauncher.logger.info("KreRefStdTask: Expected error response contained in received response2");
					FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_100A+":Expected:"+SerialPortObj.getExpectedResult()+" :Actual:"+CurrentSerialData);
					ErrorResponseRecieved = true;
					
				}else {
				
					ApplicationLauncher.logger.debug("KreRefStdTask: Expected recieved: getExpectedDataErrorResult :"+SerialPortObj.getExpectedDataErrorResult()+"\n");
					ApplicationLauncher.logger.debug("KreRefStdTask: Expected recieved: getExpectedSetErrorResult :"+SerialPortObj.getExpectedSetErrorResult()+"\n");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("KreRefStdTask unExpected received2:Current Data:"+CurrentSerialData+":Expected Data:"+SerialPortObj.getExpectedResult());
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					ApplicationLauncher.logger.info("**********************************************************");
					FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_100B+":Expected:"+SerialPortObj.getExpectedResult()+" :Actual:"+CurrentSerialData);
					
					//SerialPortObj.StripLength(CurrentSerialData.length());
				}
			}
			responseWaitCount --;
			//ApplicationLauncher.logger.debug("KreRefStdTask: responseWaitCount: "+responseWaitCount);
			//ApplicationLauncher.logger.debug("KreRefStdTask: ExpectedResponseRecieved: "+ExpectedResponseRecieved);
			//ApplicationLauncher.logger.debug("KreRefStdTask: ErrorResponseRecieved: "+ErrorResponseRecieved);
			//ApplicationLauncher.logger.debug("KreRefStdTask: getUserAbortedFlag(): "+ProjectExecutionController.getUserAbortedFlag());
			if (responseWaitCount == 0 || ExpectedResponseRecieved ||
					ErrorResponseRecieved ||  
					ProjectExecutionController.getUserAbortedFlag()){
				responseWaitCount=0;
				ApplicationLauncher.logger.info("KreRefStdTask :Timer Exit!%n");
				timer.cancel(); //Terminate the timer thread
			}
			else{
				timer.schedule(new KreRefStdTask(), RetryIntervalInMsec);
			}
		}



	}

	public void parseSettingsSerialData(String decodeDataHex) {
		
		//String DecodeData=this.RefStd_ReadSerialData;
		//String decodeDataHex = "4B52453A2F2F434D433D302C495354414C4C3D302C434F55545F313D31652B3030372C434F55545F323D31303030302C434F55545F333D31303030302C50575F313D302C50575F323D302C50575F333D302C4552525F5741593D302C4552525F504C5553455F543D322C4552525F504C5553455F4E3D3130302C43494E5F313D3130303030302C43494E5F323D3130303030302C43494E5F333D3130303030302C43494E5F343D3130303030302C50575F494E313D302C50575F494E323D302C50575F494E333D302C50575F494E343D302C534E3D393332313034382C45563D312E322E322E322C44563D312E302E322E322C4441544554494D453D323032312D31322D32312031333A303A302C4352433D34392C2F21";
		ApplicationLauncher.logger.debug("parseSettingsSerialData: decodeDataHex: " + decodeDataHex);
		if(decodeDataHex.startsWith(ConstantRefStdKre.CMD_RESPONSE_HDR)){
			decodeDataHex = decodeDataHex.replace(ConstantRefStdKre.CMD_RESPONSE_HDR, "");
			String decodeDataString = GuiUtils.hexToAscii(decodeDataHex);
			ApplicationLauncher.logger.debug("parseSettingsSerialData: decodeDataString2: " + decodeDataString);
			String[] parsedDataWithHeader = decodeDataString.split(GuiUtils.hexToAscii(ConstantRefStdKre.ER_REF_STD_SEPERATOR));
			//ApplicationLauncher.logger.debug("parseSettingsSerialData: parsedDataWithHeader: " + parsedDataWithHeader[0]);
			if(parsedDataWithHeader.length >0){
				ApplicationLauncher.logger.debug("parseSettingsSerialData: parsedDataWithHeader[1]: " + parsedDataWithHeader[1]);
				String currentSetting = parsedDataWithHeader[1].replace(ConstantRefStdKre.SETTING_ISTALL_PARSED_DATA_HEADER, "");
				if(ConstantAppConfig.REF_STD_ENABLE_PARSING_LOGS){ 
					ApplicationLauncher.logger.debug("parseSettingsSerialData: currentSetting: " + currentSetting);
				}
			}
		}
	}
	
	
	public void parseBasicsSerialData(String decodeDataHex) {
		
		//String DecodeData=this.RefStd_ReadSerialData;
		//String decodeDataHex = "4B52453A2F2F465245513D35302E3030303030302C55313D302E3031373836363438372C5531323D302E303038363031303330372C49313D392E35313139383937652D3030352C50313D2D322E31393230343638652D3030372C51313D2D382E38393538373931652D3030372C53313D392E39363930353733652D3030372C434F53313D2D302E32313938383530362C61313D3235312E37363533342C6131323D3334352E38323636372C616C313D3132332E37323738372C616C31323D3334352E38323636372C4550313D302C4551313D302C4553313D302C55323D302E3031353833343434382C5532333D302E303036333332393337372C49323D362E35393333383631652D3030352C50323D2D342E353834363939652D3030382C51323D2D372E33353439333236652D3030372C53323D372E37343830303135652D3030372C434F53323D2D302E3035393137323636352C61323D3236362E37333338312C6132333D3335362E39363931352C616C323D33302E3935323532352C616C32333D3335362E39363931352C4550323D302C4551323D302C4553323D302C55333D302E3031373439333531322C5533313D302E3030393839313538312C49333D372E33303435323138652D3030352C50333D2D332E33323032353735652D3030382C51333D2D382E33353433353631652D3030372C53333D382E39363035303932652D3030372C434F53333D2D302E30333730353433342C61333D3236392E36353634332C6133313D31372E3230343137312C616C333D3330382E35353933332C616C33313D31372E3230343137312C4550333D302C4551333D302C4553333D302C5053554D3D2D322E39383235343234652D3030372C5153554D3D2D322E34363035313638652D3030362C5353554D3D322E36363737353638652D3030362C53434F533D2D302E31323033333532362C5455313D322E303236373433322C5449313D302E32383735313136332C5455323D312E313934333538312C5449323D302E33333537353736312C5455333D312E323638303732362C5449333D302E33343634303836342C4E463D312C4352433D33352C2F21";
		if(decodeDataHex.startsWith(ConstantRefStdKre.CMD_RESPONSE_HDR)){
			decodeDataHex = decodeDataHex.replace(ConstantRefStdKre.CMD_RESPONSE_HDR, "");
			String decodeDataString = GuiUtils.hexToAscii(decodeDataHex);
			ApplicationLauncher.logger.debug("parseBasicsSerialData: decodeDataString: " + decodeDataString);
			String[] parsedDataWithHeader = decodeDataString.split(GuiUtils.hexToAscii(ConstantRefStdKre.ER_REF_STD_SEPERATOR));
			//ApplicationLauncher.logger.debug("parseSettingsSerialData: parsedDataWithHeader: " + parsedDataWithHeader[0]);
			if(parsedDataWithHeader.length >0){
				for(int i = 0 ; i < parsedDataWithHeader.length; i ++){
					if(ConstantAppConfig.REF_STD_ENABLE_PARSING_LOGS){
						ApplicationLauncher.logger.debug("parseBasicsSerialData: parsedDataWithHeader: " + parsedDataWithHeader[i]);
					}
					if(parsedDataWithHeader[i].startsWith(ConstantRefStdKre.FREQ_PARSED_DATA_HEADER)){
						
						rPhaseFreqDisplayData = parsedDataWithHeader[i].replace(ConstantRefStdKre.FREQ_PARSED_DATA_HEADER, "");
						//ApplicationLauncher.logger.debug("parseBasicsSerialData: rPhaseFreqDisplayData1: " + rPhaseFreqDisplayData);
						if ((!rPhaseFreqDisplayData.isEmpty()) && (GuiUtils.is_float(rPhaseFreqDisplayData)) ){
							rPhaseFreqDisplayData = String.format(ConstantApp.DISPLAY_FREQ_RESOLUTION, Float.parseFloat(rPhaseFreqDisplayData));
						}
						yPhaseFreqDisplayData = parsedDataWithHeader[i].replace(ConstantRefStdKre.FREQ_PARSED_DATA_HEADER, "");
						if ((!yPhaseFreqDisplayData.isEmpty()) && (GuiUtils.is_float(yPhaseFreqDisplayData)) ){
							yPhaseFreqDisplayData = String.format(ConstantApp.DISPLAY_FREQ_RESOLUTION, Float.parseFloat(yPhaseFreqDisplayData));
						}
						bPhaseFreqDisplayData = parsedDataWithHeader[i].replace(ConstantRefStdKre.FREQ_PARSED_DATA_HEADER, "");
						if ((!bPhaseFreqDisplayData.isEmpty()) && (GuiUtils.is_float(bPhaseFreqDisplayData)) ){
							bPhaseFreqDisplayData = String.format(ConstantApp.DISPLAY_FREQ_RESOLUTION, Float.parseFloat(bPhaseFreqDisplayData));
						}
						if(ConstantAppConfig.REF_STD_ENABLE_PARSING_LOGS){
							ApplicationLauncher.logger.debug("parseBasicsSerialData: rPhaseFreqDisplayData: " + rPhaseFreqDisplayData);
						}
					}else if(parsedDataWithHeader[i].startsWith(ConstantRefStdKre.RPHASE_VOLTAGE_PARSED_DATA_HEADER)){
						rPhaseVoltageDisplayData = parsedDataWithHeader[i].replace(ConstantRefStdKre.RPHASE_VOLTAGE_PARSED_DATA_HEADER, "");
						if ((!rPhaseVoltageDisplayData.isEmpty()) && (GuiUtils.is_float(rPhaseVoltageDisplayData)) ){
							if(ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED){
								rPhaseVoltageDisplayData = String.format(ConstantApp.DISPLAY_CALIBRATION_VOLTAGE_RESOLUTION, Float.parseFloat(rPhaseVoltageDisplayData));
							}else{
								rPhaseVoltageDisplayData = String.format(ConstantApp.DISPLAY_VOLTAGE_RESOLUTION, Float.parseFloat(rPhaseVoltageDisplayData));
						
							}
						}
						if(ConstantAppConfig.REF_STD_ENABLE_PARSING_LOGS){ 
							ApplicationLauncher.logger.debug("parseBasicsSerialData: rPhaseVoltageDisplayData: " + rPhaseVoltageDisplayData);
						}
					}else if(parsedDataWithHeader[i].startsWith(ConstantRefStdKre.RPHASE_CURRENT_PARSED_DATA_HEADER)){
						rPhaseCurrentDisplayData = parsedDataWithHeader[i].replace(ConstantRefStdKre.RPHASE_CURRENT_PARSED_DATA_HEADER, "");
						if ((!rPhaseCurrentDisplayData.isEmpty()) && (GuiUtils.is_float(rPhaseCurrentDisplayData)) ){
							if(ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED){
								rPhaseCurrentDisplayData = String.format(ConstantApp.DISPLAY_CALIBRATION_CURRENT_RESOLUTION, Float.parseFloat(rPhaseCurrentDisplayData));
							}
							
/*							else if(Float.parseFloat(rPhaseCurrentDisplayData)> 1.0f){
								
								rPhaseCurrentDisplayData = String.format(ConstantApp.DISPLAY_CURRENT_RESOLUTION_HIGH, Float.parseFloat(rPhaseCurrentDisplayData));	
								
							}else{
								rPhaseCurrentDisplayData = String.format(ConstantApp.DISPLAY_CURRENT_RESOLUTION_LOW, Float.parseFloat(rPhaseCurrentDisplayData));
							}*/
						}
						if(ConstantAppConfig.REF_STD_ENABLE_PARSING_LOGS){ 
							ApplicationLauncher.logger.debug("parseBasicsSerialData: rPhaseCurrentDisplayData: " + rPhaseCurrentDisplayData);
						}
					}else if(parsedDataWithHeader[i].startsWith(ConstantRefStdKre.RPHASE_PF_PARSED_DATA_HEADER)){
						rPhasePowerFactorData = parsedDataWithHeader[i].replace(ConstantRefStdKre.RPHASE_PF_PARSED_DATA_HEADER, "");
						if ((!rPhasePowerFactorData.isEmpty()) && (GuiUtils.is_float(rPhasePowerFactorData)) ){
							rPhasePowerFactorData = String.format(ConstantApp.DISPLAY_PHASE_ANGLE_PF_RESOLUTION, Float.parseFloat(rPhasePowerFactorData));
						}
						if(ConstantAppConfig.REF_STD_ENABLE_PARSING_LOGS){ 
							ApplicationLauncher.logger.debug("parseBasicsSerialData: rPhasePowerFactorData: " + rPhasePowerFactorData);
						}
					}else if(parsedDataWithHeader[i].startsWith(ConstantRefStdKre.RPHASE_ACTIVE_POWER_PARSED_DATA_HEADER)){
						rPhaseActivePowerDisplayData = parsedDataWithHeader[i].replace(ConstantRefStdKre.RPHASE_ACTIVE_POWER_PARSED_DATA_HEADER, "");
						if ((!rPhaseActivePowerDisplayData.isEmpty()) && (GuiUtils.is_float(rPhaseActivePowerDisplayData)) ){
							rPhaseActivePowerDisplayData = String.format(ConstantApp.DISPLAY_POWER_RESOLUTION, Float.parseFloat(rPhaseActivePowerDisplayData));
						}
						if(ConstantAppConfig.REF_STD_ENABLE_PARSING_LOGS){ 
							ApplicationLauncher.logger.debug("parseBasicsSerialData: rPhaseActivePowerDisplayData: " + rPhaseActivePowerDisplayData);
						}
					}else if(parsedDataWithHeader[i].startsWith(ConstantRefStdKre.RPHASE_PHASE_ANGLE_PARSED_DATA_HEADER)){
						rPhaseDegreePhaseData = parsedDataWithHeader[i].replace(ConstantRefStdKre.RPHASE_PHASE_ANGLE_PARSED_DATA_HEADER, "");
						if ((!rPhaseDegreePhaseData.isEmpty()) && (GuiUtils.is_float(rPhaseDegreePhaseData)) ){
							rPhaseDegreePhaseData = String.format(ConstantApp.DISPLAY_PHASE_ANGLE_DEGREE_RESOLUTION, Float.parseFloat(rPhaseDegreePhaseData));
						}
						if(ConstantAppConfig.REF_STD_ENABLE_PARSING_LOGS){ 
							ApplicationLauncher.logger.debug("parseBasicsSerialData: rPhaseDegreePhaseData: " + rPhaseDegreePhaseData);
						}
					}else if(parsedDataWithHeader[i].startsWith(ConstantRefStdKre.RPHASE_REACTIVE_POWER_PARSED_DATA_HEADER)){
						rPhaseReactivePowerDisplayData = parsedDataWithHeader[i].replace(ConstantRefStdKre.RPHASE_REACTIVE_POWER_PARSED_DATA_HEADER, "");
						if ((!rPhaseReactivePowerDisplayData.isEmpty()) && (GuiUtils.is_float(rPhaseReactivePowerDisplayData)) ){
							rPhaseReactivePowerDisplayData = String.format(ConstantApp.DISPLAY_POWER_RESOLUTION, Float.parseFloat(rPhaseReactivePowerDisplayData));
						}
						if(ConstantAppConfig.REF_STD_ENABLE_PARSING_LOGS){ 
							ApplicationLauncher.logger.debug("parseBasicsSerialData: rPhaseReactivePowerDisplayData: " + rPhaseReactivePowerDisplayData);
						}
					}else if(parsedDataWithHeader[i].startsWith(ConstantRefStdKre.RPHASE_APPARENT_POWER_PARSED_DATA_HEADER)){
						rPhaseApparentPowerDisplayData = parsedDataWithHeader[i].replace(ConstantRefStdKre.RPHASE_APPARENT_POWER_PARSED_DATA_HEADER, "");
						if ((!rPhaseApparentPowerDisplayData.isEmpty()) && (GuiUtils.is_float(rPhaseApparentPowerDisplayData)) ){
							rPhaseApparentPowerDisplayData = String.format(ConstantApp.DISPLAY_POWER_RESOLUTION, Float.parseFloat(rPhaseApparentPowerDisplayData));
						}
						if(ConstantAppConfig.REF_STD_ENABLE_PARSING_LOGS){ 
							ApplicationLauncher.logger.debug("parseBasicsSerialData: rPhaseApparentPowerDisplayData: " + rPhaseApparentPowerDisplayData);
						}
					}else if(parsedDataWithHeader[i].startsWith(ConstantRefStdKre.RPHASE_ACTIVE_ENERGY_PARSED_DATA_HEADER)){
						rPhaseActiveEnergyAccumulatedDisplayData = parsedDataWithHeader[i].replace(ConstantRefStdKre.RPHASE_ACTIVE_ENERGY_PARSED_DATA_HEADER, "");
						if ((!rPhaseActiveEnergyAccumulatedDisplayData.isEmpty()) && (!rPhaseActiveEnergyAccumulatedDisplayData.equals(ConstantRefStdKre.INVALID_ENERGY_DATA)) ){
							if(GuiUtils.is_float(rPhaseActiveEnergyAccumulatedDisplayData)){
								rPhaseActiveEnergyAccumulatedDisplayData = String.format(ConstantApp.DISPLAY_ENERGY_RESOLUTION, Float.parseFloat(rPhaseActiveEnergyAccumulatedDisplayData));
							
							}
						}
						if(ConstantAppConfig.REF_STD_ENABLE_PARSING_LOGS){ 
							ApplicationLauncher.logger.debug("parseBasicsSerialData: rPhaseActiveEnergyAccu: " + rPhaseActiveEnergyAccumulatedDisplayData);
						}
					}else if(parsedDataWithHeader[i].startsWith(ConstantRefStdKre.RPHASE_REACTIVE_ENERGY_PARSED_DATA_HEADER)){
						rPhaseReactiveEnergyAccumulatedDisplayData = parsedDataWithHeader[i].replace(ConstantRefStdKre.RPHASE_REACTIVE_ENERGY_PARSED_DATA_HEADER, "");
						//if ((!rPhaseReactiveEnergyAccumulatedDisplayData.isEmpty()) && (GUIUtils.is_float(rPhaseReactiveEnergyAccumulatedDisplayData)) ){
						if ((!rPhaseReactiveEnergyAccumulatedDisplayData.isEmpty()) && (!rPhaseReactiveEnergyAccumulatedDisplayData.equals(ConstantRefStdKre.INVALID_ENERGY_DATA)) ){
							if(GuiUtils.is_float(rPhaseReactiveEnergyAccumulatedDisplayData)){
						
								rPhaseReactiveEnergyAccumulatedDisplayData = String.format(ConstantApp.DISPLAY_ENERGY_RESOLUTION, Float.parseFloat(rPhaseReactiveEnergyAccumulatedDisplayData));
							}
						}
						if(ConstantAppConfig.REF_STD_ENABLE_PARSING_LOGS){ 
							ApplicationLauncher.logger.debug("parseBasicsSerialData: rPhaseReactiveEnergyAccu: " + rPhaseReactiveEnergyAccumulatedDisplayData);
						}
					}else if(parsedDataWithHeader[i].startsWith(ConstantRefStdKre.RPHASE_APPARENT_ENERGY_PARSED_DATA_HEADER)){
						rPhaseApparentEnergyAccumulatedDisplayData = parsedDataWithHeader[i].replace(ConstantRefStdKre.RPHASE_APPARENT_ENERGY_PARSED_DATA_HEADER, "");
						if ((!rPhaseApparentEnergyAccumulatedDisplayData.isEmpty()) && (GuiUtils.is_float(rPhaseApparentEnergyAccumulatedDisplayData)) ){
							rPhaseApparentEnergyAccumulatedDisplayData = String.format(ConstantApp.DISPLAY_ENERGY_RESOLUTION, Float.parseFloat(rPhaseApparentEnergyAccumulatedDisplayData));
						}
						if(ConstantAppConfig.REF_STD_ENABLE_PARSING_LOGS){ 
							ApplicationLauncher.logger.debug("parseBasicsSerialData: rPhaseApparentEnergyAccu: " + rPhaseApparentEnergyAccumulatedDisplayData);
						}
					}else if(parsedDataWithHeader[i].startsWith(ConstantRefStdKre.YPHASE_VOLTAGE_PARSED_DATA_HEADER)){
						yPhaseVoltageDisplayData = parsedDataWithHeader[i].replace(ConstantRefStdKre.YPHASE_VOLTAGE_PARSED_DATA_HEADER, "");
						if ((!yPhaseVoltageDisplayData.isEmpty()) && (GuiUtils.is_float(yPhaseVoltageDisplayData)) ){
							if(ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED){
								yPhaseVoltageDisplayData = String.format(ConstantApp.DISPLAY_CALIBRATION_VOLTAGE_RESOLUTION, Float.parseFloat(yPhaseVoltageDisplayData));
						
							}else{
								yPhaseVoltageDisplayData = String.format(ConstantApp.DISPLAY_VOLTAGE_RESOLUTION, Float.parseFloat(yPhaseVoltageDisplayData));
							}
						}
						if(ConstantAppConfig.REF_STD_ENABLE_PARSING_LOGS){ 
							ApplicationLauncher.logger.debug("parseBasicsSerialData: yPhaseVoltageDisplayData: " + yPhaseVoltageDisplayData);
						}
					}else if(parsedDataWithHeader[i].startsWith(ConstantRefStdKre.YPHASE_CURRENT_PARSED_DATA_HEADER)){
						yPhaseCurrentDisplayData = parsedDataWithHeader[i].replace(ConstantRefStdKre.YPHASE_CURRENT_PARSED_DATA_HEADER, "");
						if ((!yPhaseCurrentDisplayData.isEmpty()) && (GuiUtils.is_float(yPhaseCurrentDisplayData)) ){
							if(ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED){
								yPhaseCurrentDisplayData = String.format(ConstantApp.DISPLAY_CALIBRATION_CURRENT_RESOLUTION, Float.parseFloat(yPhaseCurrentDisplayData));
							}/*else if(Float.parseFloat(yPhaseCurrentDisplayData)> 1.0f){
								yPhaseCurrentDisplayData = String.format(ConstantApp.DISPLAY_CURRENT_RESOLUTION_HIGH, Float.parseFloat(yPhaseCurrentDisplayData));
									
							}else{
								yPhaseCurrentDisplayData = String.format(ConstantApp.DISPLAY_CURRENT_RESOLUTION_LOW, Float.parseFloat(yPhaseCurrentDisplayData));
							}*/
						}
						if(ConstantAppConfig.REF_STD_ENABLE_PARSING_LOGS){ 
							ApplicationLauncher.logger.debug("parseBasicsSerialData: yPhaseCurrentDisplayData: " + yPhaseCurrentDisplayData);
						}
					}else if(parsedDataWithHeader[i].startsWith(ConstantRefStdKre.YPHASE_PF_PARSED_DATA_HEADER)){
						yPhasePowerFactorData = parsedDataWithHeader[i].replace(ConstantRefStdKre.YPHASE_PF_PARSED_DATA_HEADER, "");
						if ((!yPhasePowerFactorData.isEmpty()) && (GuiUtils.is_float(yPhasePowerFactorData)) ){
							yPhasePowerFactorData = String.format(ConstantApp.DISPLAY_PHASE_ANGLE_PF_RESOLUTION, Float.parseFloat(yPhasePowerFactorData));
						}
						if(ConstantAppConfig.REF_STD_ENABLE_PARSING_LOGS){ 
							ApplicationLauncher.logger.debug("parseBasicsSerialData: yPhasePowerFactorData: " + yPhasePowerFactorData);
						}
					}else if(parsedDataWithHeader[i].startsWith(ConstantRefStdKre.YPHASE_PHASE_ANGLE_PARSED_DATA_HEADER)){
						yPhaseDegreePhaseData = parsedDataWithHeader[i].replace(ConstantRefStdKre.YPHASE_PHASE_ANGLE_PARSED_DATA_HEADER, "");
						if ((!yPhaseDegreePhaseData.isEmpty()) && (GuiUtils.is_float(yPhaseDegreePhaseData)) ){
							yPhaseDegreePhaseData = String.format(ConstantApp.DISPLAY_PHASE_ANGLE_DEGREE_RESOLUTION, Float.parseFloat(yPhaseDegreePhaseData));
						}
						if(ConstantAppConfig.REF_STD_ENABLE_PARSING_LOGS){ 
							ApplicationLauncher.logger.debug("parseBasicsSerialData: yPhaseDegreePhaseData: " + yPhaseDegreePhaseData);
						}
					}else if(parsedDataWithHeader[i].startsWith(ConstantRefStdKre.YPHASE_ACTIVE_POWER_PARSED_DATA_HEADER)){
						yPhaseActivePowerDisplayData = parsedDataWithHeader[i].replace(ConstantRefStdKre.YPHASE_ACTIVE_POWER_PARSED_DATA_HEADER, "");
						if ((!yPhaseActivePowerDisplayData.isEmpty()) && (GuiUtils.is_float(yPhaseActivePowerDisplayData)) ){
							yPhaseActivePowerDisplayData = String.format(ConstantApp.DISPLAY_POWER_RESOLUTION, Float.parseFloat(yPhaseActivePowerDisplayData));
						}
						if(ConstantAppConfig.REF_STD_ENABLE_PARSING_LOGS){ 
							ApplicationLauncher.logger.debug("parseBasicsSerialData: yPhaseWattDisplayData: " + yPhaseActivePowerDisplayData);
						}
					}else if(parsedDataWithHeader[i].startsWith(ConstantRefStdKre.YPHASE_REACTIVE_POWER_PARSED_DATA_HEADER)){
						yPhaseReactivePowerDisplayData = parsedDataWithHeader[i].replace(ConstantRefStdKre.YPHASE_REACTIVE_POWER_PARSED_DATA_HEADER, "");
						if ((!yPhaseReactivePowerDisplayData.isEmpty()) && (GuiUtils.is_float(yPhaseReactivePowerDisplayData)) ){
							yPhaseReactivePowerDisplayData = String.format(ConstantApp.DISPLAY_POWER_RESOLUTION, Float.parseFloat(yPhaseReactivePowerDisplayData));
						}
						if(ConstantAppConfig.REF_STD_ENABLE_PARSING_LOGS){ 
							ApplicationLauncher.logger.debug("parseBasicsSerialData: yPhaseReactivePowerDisplayData: " + yPhaseReactivePowerDisplayData);
						}
					}else if(parsedDataWithHeader[i].startsWith(ConstantRefStdKre.YPHASE_APPARENT_POWER_PARSED_DATA_HEADER)){
						yPhaseApparentPowerDisplayData = parsedDataWithHeader[i].replace(ConstantRefStdKre.YPHASE_APPARENT_POWER_PARSED_DATA_HEADER, "");
						if ((!yPhaseApparentPowerDisplayData.isEmpty()) && (GuiUtils.is_float(yPhaseApparentPowerDisplayData)) ){
							yPhaseApparentPowerDisplayData = String.format(ConstantApp.DISPLAY_POWER_RESOLUTION, Float.parseFloat(yPhaseApparentPowerDisplayData));
						}
						if(ConstantAppConfig.REF_STD_ENABLE_PARSING_LOGS){ 
							ApplicationLauncher.logger.debug("parseBasicsSerialData: yPhaseApparentPowerDisplayData: " + yPhaseApparentPowerDisplayData);
						}
					}else if(parsedDataWithHeader[i].startsWith(ConstantRefStdKre.YPHASE_ACTIVE_ENERGY_PARSED_DATA_HEADER)){
						yPhaseActiveEnergyAccumulatedDisplayData = parsedDataWithHeader[i].replace(ConstantRefStdKre.YPHASE_ACTIVE_ENERGY_PARSED_DATA_HEADER, "");
						//if ((!yPhaseActiveEnergyAccumulatedDisplayData.isEmpty()) && (GUIUtils.is_float(yPhaseActiveEnergyAccumulatedDisplayData)) ){
						if ((!yPhaseActiveEnergyAccumulatedDisplayData.isEmpty()) && (!yPhaseActiveEnergyAccumulatedDisplayData.equals(ConstantRefStdKre.INVALID_ENERGY_DATA)) ){
							if(GuiUtils.is_float(yPhaseActiveEnergyAccumulatedDisplayData)){
								yPhaseActiveEnergyAccumulatedDisplayData = String.format(ConstantApp.DISPLAY_ENERGY_RESOLUTION, Float.parseFloat(yPhaseActiveEnergyAccumulatedDisplayData));
						
							}
						}
						if(ConstantAppConfig.REF_STD_ENABLE_PARSING_LOGS){ 
							ApplicationLauncher.logger.debug("parseBasicsSerialData: yPhaseActiveEnergyAccu: " + yPhaseActiveEnergyAccumulatedDisplayData);
						}
					}else if(parsedDataWithHeader[i].startsWith(ConstantRefStdKre.YPHASE_REACTIVE_ENERGY_PARSED_DATA_HEADER)){
						yPhaseReactiveEnergyAccumulatedDisplayData = parsedDataWithHeader[i].replace(ConstantRefStdKre.YPHASE_REACTIVE_ENERGY_PARSED_DATA_HEADER, "");
						//if ((!yPhaseReactiveEnergyAccumulatedDisplayData.isEmpty()) && (GUIUtils.is_float(yPhaseReactiveEnergyAccumulatedDisplayData)) ){
						if ((!yPhaseReactiveEnergyAccumulatedDisplayData.isEmpty()) && (!yPhaseReactiveEnergyAccumulatedDisplayData.equals(ConstantRefStdKre.INVALID_ENERGY_DATA)) ){
							if(GuiUtils.is_float(yPhaseReactiveEnergyAccumulatedDisplayData)){
					
								yPhaseReactiveEnergyAccumulatedDisplayData = String.format(ConstantApp.DISPLAY_ENERGY_RESOLUTION, Float.parseFloat(yPhaseReactiveEnergyAccumulatedDisplayData));
							}
						}
						if(ConstantAppConfig.REF_STD_ENABLE_PARSING_LOGS){ 
							ApplicationLauncher.logger.debug("parseBasicsSerialData: yPhaseReactiveEnergyAccu: " + yPhaseReactiveEnergyAccumulatedDisplayData);
						}
					}else if(parsedDataWithHeader[i].startsWith(ConstantRefStdKre.YPHASE_APPARENT_ENERGY_PARSED_DATA_HEADER)){
						yPhaseApparentEnergyAccumulatedDisplayData = parsedDataWithHeader[i].replace(ConstantRefStdKre.YPHASE_APPARENT_ENERGY_PARSED_DATA_HEADER, "");
						if ((!yPhaseApparentEnergyAccumulatedDisplayData.isEmpty()) && (GuiUtils.is_float(yPhaseApparentEnergyAccumulatedDisplayData)) ){
							yPhaseApparentEnergyAccumulatedDisplayData = String.format(ConstantApp.DISPLAY_ENERGY_RESOLUTION, Float.parseFloat(yPhaseApparentEnergyAccumulatedDisplayData));
						}
						if(ConstantAppConfig.REF_STD_ENABLE_PARSING_LOGS){ 
							ApplicationLauncher.logger.debug("parseBasicsSerialData: yPhaseApparentEnergyAccu: " + yPhaseApparentEnergyAccumulatedDisplayData);
						}
					}else if(parsedDataWithHeader[i].startsWith(ConstantRefStdKre.BPHASE_VOLTAGE_PARSED_DATA_HEADER)){
						bPhaseVoltageDisplayData = parsedDataWithHeader[i].replace(ConstantRefStdKre.BPHASE_VOLTAGE_PARSED_DATA_HEADER, "");
						if ((!bPhaseVoltageDisplayData.isEmpty()) && (GuiUtils.is_float(bPhaseVoltageDisplayData)) ){
							if(ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED){
								bPhaseVoltageDisplayData = String.format(ConstantApp.DISPLAY_CALIBRATION_VOLTAGE_RESOLUTION, Float.parseFloat(bPhaseVoltageDisplayData));
							}else{
								bPhaseVoltageDisplayData = String.format(ConstantApp.DISPLAY_VOLTAGE_RESOLUTION, Float.parseFloat(bPhaseVoltageDisplayData));
							}
						}
						if(ConstantAppConfig.REF_STD_ENABLE_PARSING_LOGS){ 
							ApplicationLauncher.logger.debug("parseBasicsSerialData: bPhaseVoltageDisplayData: " + bPhaseVoltageDisplayData);
						}
					}else if(parsedDataWithHeader[i].startsWith(ConstantRefStdKre.BPHASE_CURRENT_PARSED_DATA_HEADER)){
						bPhaseCurrentDisplayData = parsedDataWithHeader[i].replace(ConstantRefStdKre.BPHASE_CURRENT_PARSED_DATA_HEADER, "");
						if ((!bPhaseCurrentDisplayData.isEmpty()) && (GuiUtils.is_float(bPhaseCurrentDisplayData)) ){
							if(ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED){
								bPhaseCurrentDisplayData = String.format(ConstantApp.DISPLAY_CALIBRATION_CURRENT_RESOLUTION, Float.parseFloat(bPhaseCurrentDisplayData));
							}/*else if(Float.parseFloat(bPhaseCurrentDisplayData)> 1.0f){
								 
								bPhaseCurrentDisplayData = String.format(ConstantApp.DISPLAY_CURRENT_RESOLUTION_HIGH, Float.parseFloat(bPhaseCurrentDisplayData));
								
							}else{
								bPhaseCurrentDisplayData = String.format(ConstantApp.DISPLAY_CURRENT_RESOLUTION_LOW, Float.parseFloat(bPhaseCurrentDisplayData));
							}*/
						}
						if(ConstantAppConfig.REF_STD_ENABLE_PARSING_LOGS){ 
							ApplicationLauncher.logger.debug("parseBasicsSerialData: bPhaseCurrentDisplayData: " + bPhaseCurrentDisplayData);
						}
					}else if(parsedDataWithHeader[i].startsWith(ConstantRefStdKre.BPHASE_PF_PARSED_DATA_HEADER)){
						bPhasePowerFactorData = parsedDataWithHeader[i].replace(ConstantRefStdKre.BPHASE_PF_PARSED_DATA_HEADER, "");
						if ((!bPhasePowerFactorData.isEmpty()) && (GuiUtils.is_float(bPhasePowerFactorData)) ){
							bPhasePowerFactorData = String.format(ConstantApp.DISPLAY_PHASE_ANGLE_PF_RESOLUTION, Float.parseFloat(bPhasePowerFactorData));
						}
						if(ConstantAppConfig.REF_STD_ENABLE_PARSING_LOGS){ 
							ApplicationLauncher.logger.debug("parseBasicsSerialData: bPhasePowerFactorData: " + bPhasePowerFactorData);
						}
					}else if(parsedDataWithHeader[i].startsWith(ConstantRefStdKre.BPHASE_PHASE_ANGLE_PARSED_DATA_HEADER)){
						bPhaseDegreePhaseData = parsedDataWithHeader[i].replace(ConstantRefStdKre.BPHASE_PHASE_ANGLE_PARSED_DATA_HEADER, "");
						if ((!bPhaseDegreePhaseData.isEmpty()) && (GuiUtils.is_float(bPhaseDegreePhaseData)) ){
							bPhaseDegreePhaseData = String.format(ConstantApp.DISPLAY_PHASE_ANGLE_DEGREE_RESOLUTION, Float.parseFloat(bPhaseDegreePhaseData));
						}
						if(ConstantAppConfig.REF_STD_ENABLE_PARSING_LOGS){ 
							ApplicationLauncher.logger.debug("parseBasicsSerialData: bPhaseDegreePhaseData: " + bPhaseDegreePhaseData);
						}
					}else if(parsedDataWithHeader[i].startsWith(ConstantRefStdKre.BPHASE_ACTIVE_POWER_PARSED_DATA_HEADER)){
						bPhaseActivePowerDisplayData = parsedDataWithHeader[i].replace(ConstantRefStdKre.BPHASE_ACTIVE_POWER_PARSED_DATA_HEADER, "");
						if ((!bPhaseActivePowerDisplayData.isEmpty()) && (GuiUtils.is_float(bPhaseActivePowerDisplayData)) ){
							bPhaseActivePowerDisplayData = String.format(ConstantApp.DISPLAY_POWER_RESOLUTION, Float.parseFloat(bPhaseActivePowerDisplayData));
						}
						if(ConstantAppConfig.REF_STD_ENABLE_PARSING_LOGS){ 
							ApplicationLauncher.logger.debug("parseBasicsSerialData: bPhaseWattDisplayData: " + bPhaseActivePowerDisplayData);
						}
					}else if(parsedDataWithHeader[i].startsWith(ConstantRefStdKre.BPHASE_REACTIVE_POWER_PARSED_DATA_HEADER)){
						bPhaseReactivePowerDisplayData = parsedDataWithHeader[i].replace(ConstantRefStdKre.BPHASE_REACTIVE_POWER_PARSED_DATA_HEADER, "");
						if ((!bPhaseReactivePowerDisplayData.isEmpty()) && (GuiUtils.is_float(bPhaseReactivePowerDisplayData)) ){
							bPhaseReactivePowerDisplayData = String.format(ConstantApp.DISPLAY_POWER_RESOLUTION, Float.parseFloat(bPhaseReactivePowerDisplayData));
						}
						if(ConstantAppConfig.REF_STD_ENABLE_PARSING_LOGS){ 
							ApplicationLauncher.logger.debug("parseBasicsSerialData: bPhaseReactivePowerDisplayData: " + bPhaseReactivePowerDisplayData);
						}
					}else if(parsedDataWithHeader[i].startsWith(ConstantRefStdKre.BPHASE_APPARENT_POWER_PARSED_DATA_HEADER)){
						bPhaseApparentPowerDisplayData = parsedDataWithHeader[i].replace(ConstantRefStdKre.BPHASE_APPARENT_POWER_PARSED_DATA_HEADER, "");
						if ((!bPhaseApparentPowerDisplayData.isEmpty()) && (GuiUtils.is_float(bPhaseApparentPowerDisplayData)) ){
							bPhaseApparentPowerDisplayData = String.format(ConstantApp.DISPLAY_POWER_RESOLUTION, Float.parseFloat(bPhaseApparentPowerDisplayData));
						}
						if(ConstantAppConfig.REF_STD_ENABLE_PARSING_LOGS){ 
							ApplicationLauncher.logger.debug("parseBasicsSerialData: bPhaseApparentPowerDisplayData: " + bPhaseApparentPowerDisplayData);
						}
					}else if(parsedDataWithHeader[i].startsWith(ConstantRefStdKre.BPHASE_ACTIVE_ENERGY_PARSED_DATA_HEADER)){
						bPhaseActiveEnergyAccumulatedDisplayData = parsedDataWithHeader[i].replace(ConstantRefStdKre.BPHASE_ACTIVE_ENERGY_PARSED_DATA_HEADER, "");
						//if ((!bPhaseActiveEnergyAccumulatedDisplayData.isEmpty()) && (GUIUtils.is_float(bPhaseActiveEnergyAccumulatedDisplayData)) ){
						if ((!bPhaseActiveEnergyAccumulatedDisplayData.isEmpty()) && (!bPhaseActiveEnergyAccumulatedDisplayData.equals(ConstantRefStdKre.INVALID_ENERGY_DATA)) ){
							if(GuiUtils.is_float(bPhaseActiveEnergyAccumulatedDisplayData)){
						
								bPhaseActiveEnergyAccumulatedDisplayData = String.format(ConstantApp.DISPLAY_ENERGY_RESOLUTION, Float.parseFloat(bPhaseActiveEnergyAccumulatedDisplayData));
							}
						}
						if(ConstantAppConfig.REF_STD_ENABLE_PARSING_LOGS){ 
							ApplicationLauncher.logger.debug("parseBasicsSerialData: bPhaseActiveEnergyAccu: " + bPhaseActiveEnergyAccumulatedDisplayData);
						}
					}else if(parsedDataWithHeader[i].startsWith(ConstantRefStdKre.BPHASE_REACTIVE_ENERGY_PARSED_DATA_HEADER)){
						bPhaseReactiveEnergyAccumulatedDisplayData = parsedDataWithHeader[i].replace(ConstantRefStdKre.BPHASE_REACTIVE_ENERGY_PARSED_DATA_HEADER, "");
						
						//if ((!bPhaseReactiveEnergyAccumulatedDisplayData.isEmpty()) && (GUIUtils.is_float(bPhaseReactiveEnergyAccumulatedDisplayData)) ){
						if ((!bPhaseReactiveEnergyAccumulatedDisplayData.isEmpty()) && (!bPhaseReactiveEnergyAccumulatedDisplayData.equals(ConstantRefStdKre.INVALID_ENERGY_DATA)) ){
							if(GuiUtils.is_float(bPhaseReactiveEnergyAccumulatedDisplayData)){
						
								bPhaseReactiveEnergyAccumulatedDisplayData = String.format(ConstantApp.DISPLAY_ENERGY_RESOLUTION, Float.parseFloat(bPhaseReactiveEnergyAccumulatedDisplayData));
							}
						}				
						if(ConstantAppConfig.REF_STD_ENABLE_PARSING_LOGS){ 
							ApplicationLauncher.logger.debug("parseBasicsSerialData: bPhaseReactiveEnergyAccu: " + bPhaseReactiveEnergyAccumulatedDisplayData);
						}
					}else if(parsedDataWithHeader[i].startsWith(ConstantRefStdKre.BPHASE_APPARENT_ENERGY_PARSED_DATA_HEADER)){
						bPhaseApparentEnergyAccumulatedDisplayData = parsedDataWithHeader[i].replace(ConstantRefStdKre.BPHASE_APPARENT_ENERGY_PARSED_DATA_HEADER, "");
						if ((!bPhaseApparentEnergyAccumulatedDisplayData.isEmpty()) && (GuiUtils.is_float(bPhaseApparentEnergyAccumulatedDisplayData)) ){
							bPhaseApparentEnergyAccumulatedDisplayData = String.format(ConstantApp.DISPLAY_ENERGY_RESOLUTION, Float.parseFloat(bPhaseApparentEnergyAccumulatedDisplayData));
						}
						if(ConstantAppConfig.REF_STD_ENABLE_PARSING_LOGS){ 
							ApplicationLauncher.logger.debug("parseBasicsSerialData: bPhaseApparentEnergyAccu: " + bPhaseApparentEnergyAccumulatedDisplayData);
						}
					}
					
/*		*/
				}
				//ApplicationLauncher.logger.debug("parseBasicsSerialData: parsedDataWithHeader[1]: " + parsedDataWithHeader[1]);
				//String currentSetting = parsedDataWithHeader[1].replace(ConstantRefStdKre.ISTALL_PARSED_DATA_HEADER, "");
				//ApplicationLauncher.logger.debug("parseBasicsSerialData: currentSetting: " + currentSetting);
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


						ApplicationLauncher.logger.info("PhaseA: " + PhaseA);
						ApplicationLauncher.logger.info("PhaseB: " + PhaseB);
						ApplicationLauncher.logger.info("PhaseC: " + PhaseC);
						//ApplicationLauncher.logger.info("DecodeData: " + DecodeData);
						setPhaseAReading(PhaseA);
						setPhaseBReading(PhaseB);
						setPhaseCReading(PhaseC);
					}
				}else if(DecodeData.length()<12){

					ApplicationLauncher.logger.error("RefStd_DecodeSerialDataForConstTest:Error 2003");

				}
			}
		} catch (Exception e){
			e.printStackTrace();

			ApplicationLauncher.logger.error("LDU_DecodeSerialDataForConstTest: Exception: "+ e.getMessage());
		}
		return DecodeData;

	}




}
