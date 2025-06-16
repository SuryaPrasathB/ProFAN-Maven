package com.tasnetwork.calibration.energymeter.device;

import java.util.ArrayList;

import com.tasnetwork.calibration.energymeter.constant.Constant_ICT;

public class Data_IctKre {

	private static ArrayList<ArrayList<String>> readShortIctList  = new ArrayList<ArrayList<String>>();
	
	private static ArrayList<String> readShortIctStatusList  = new ArrayList<String>();
	
	static{
		for(int i =1 ; i<= Constant_ICT.ICT_MAX_POSITION; i++){
			ArrayList<String> statusList = new ArrayList<String>();
			statusList.add(Constant_ICT.ICT_STATUS_NORMAL);
			statusList.add(Constant_ICT.ICT_STATUS_NORMAL);
			statusList.add(Constant_ICT.ICT_STATUS_NORMAL);
			readShortIctList.add(statusList);
			
		}
	}

	public static ArrayList<ArrayList<String>> getReadShortIctList() {
		return readShortIctList;
	}


	public static void setReadShortIctList(ArrayList<ArrayList<String>> readShortIctList) {
		Data_IctKre.readShortIctList = readShortIctList;
	}
	
	public static ArrayList<String> getReadShortIct(String IctId) {
		return readShortIctList.get(Integer.valueOf(IctId)-1);
	}
	
	public static String getReadShortIctRphase(String IctId) {
		return readShortIctList.get(Integer.valueOf(IctId)-1).get(0);
	}
	
	public static String getReadShortIctYphase(String IctId) {
		return readShortIctList.get(Integer.valueOf(IctId)-1).get(1);
	}
	
	public static String getReadShortIctBphase(String IctId) {
		return readShortIctList.get(Integer.valueOf(IctId)-1).get(1);
	}
	



	public static void setReadShortIct(String IctId, ArrayList<String> readIctStatusList) {
		Data_IctKre.readShortIctList.set(((Integer.valueOf(IctId)-1)), readIctStatusList);
	}


	public static ArrayList<String> getReadShortIctStatusList() {
		return readShortIctStatusList;
	}
	
	public static void clearReadShortIctStatusList() {
		readShortIctStatusList.clear();
	}


	public static void setReadShortIctStatusList(String rPhaseStatus,String yPhaseStatus,String bPhaseStatus) {
		Data_IctKre.readShortIctStatusList.add(rPhaseStatus);
		Data_IctKre.readShortIctStatusList.add(yPhaseStatus);
		Data_IctKre.readShortIctStatusList.add(bPhaseStatus);
	}
}
