package com.tasnetwork.calibration.energymeter.deployment;

public class FailureManager {

	public static String PowerSrcReasonForFailure = "";
	public static String RefStdFeedBackReasonForFailure = "";
	public static String RefStdFeedBackDegree = "";
	public static String RefStdSetDegree = "";
	public static String RefStdSetVolt= "";
	public static String RefStdFeedBackVolt  = "";
	public static String RefStdSetCurrent = "";
	public static String RefStdFeedBackCurrent  = "";
	public static String RefStdSetFrequency = "";
	public static String RefStdFeedBackFrequency  = "";
	
	public static void AppendPowerSrcReasonForFailure(String Failure){
		PowerSrcReasonForFailure = PowerSrcReasonForFailure+Failure+",";
		
	}

	public static String getPowerSrcReasonForFailure(){
		return PowerSrcReasonForFailure ;
		
	}
	
	public static void ResetPowerSrcReasonForFailure(){
		PowerSrcReasonForFailure = "";
		
	}

	public static void AppendRefStdFeedBackReasonForFailure(String Failure){
		RefStdFeedBackReasonForFailure = RefStdFeedBackReasonForFailure+Failure+",";
		
	}
	
	public static void ResetRefStdFeedBackReasonForFailure(){
		RefStdFeedBackReasonForFailure = "";
		
	}
	
	public static String getRefStdFeedBackReasonForFailure(){
		return RefStdFeedBackReasonForFailure ;
		
	}
	
	public static void setRefStdSetDegree(String Degree){
		RefStdSetDegree = Degree;
		
	}
	
	public static String getRefStdSetDegree(){
		return RefStdSetDegree ;
		
	}
	
	public static void setRefStdFeedBackDegree(String FB_Degree){
		RefStdFeedBackDegree = FB_Degree;
		
	}
	
	public static String getRefStdFeedBackDegree(){
		return RefStdFeedBackDegree ;
		
	}
	
	public static void setRefStdSetVolt(String VoltValue){
		RefStdSetVolt = VoltValue;
		
	}
	
	public static String getRefStdSetVolt(){
		return RefStdSetVolt ;
		
	}
	
	public static void setRefStdFeedBackVolt(String FeedBackVoltValue){
		RefStdFeedBackVolt = FeedBackVoltValue;
		
	}
	
	public static String getRefStdFeedBackVolt(){
		return RefStdFeedBackVolt ;
		
	}
	
	public static void setRefStdSetCurrent(String SetCurrent){
		RefStdSetCurrent = SetCurrent;
		
	}
	
	public static String getRefStdSetCurrent(){
		return RefStdSetCurrent ;
		
	}

	public static void setRefStdFeedBackCurrent(String FeedBackCurrentValue){
		RefStdFeedBackCurrent = FeedBackCurrentValue;
		
	}
	
	public static String getRefStdFeedBackCurrent(){
		return RefStdFeedBackCurrent ;
		
	}
	
	public static void setRefStdSetFrequency(String SetFreq){
		RefStdSetFrequency = SetFreq;
		
	}
	
	public static String getRefStdSetFrequency(){
		return RefStdSetFrequency ;
		
	}

	public static void setRefStdFeedBackFrequency(String FeedBackFreq){
		RefStdFeedBackFrequency = FeedBackFreq;
		
	}
	
	public static String getRefStdFeedBackFrequency(){
		return RefStdFeedBackFrequency ;
		
	}




}
