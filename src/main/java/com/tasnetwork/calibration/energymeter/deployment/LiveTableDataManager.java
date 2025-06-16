package com.tasnetwork.calibration.energymeter.deployment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.util.Callback;

public class LiveTableDataManager {
	private static int Index=0;
	private static ArrayList<ArrayList<Object>> AllRowData = new ArrayList<ArrayList<Object>>();
	private static  List<String> ActiveRackList = new ArrayList<String>();
	final static ObservableList<liveTableData> liveTableDataListObj = FXCollections.observableArrayList(
			new Callback<liveTableData, Observable[]>() {
				@Override
				public Observable[] call(liveTableData param) {
					return new Observable[]{
							param.rack1_DataProperty(),
							param.rack2_DataProperty(),
							param.rack3_DataProperty(),
							param.rack4_DataProperty(),
							param.rack5_DataProperty(),
							param.rack6_DataProperty(),
							param.rack7_DataProperty(),
							param.rack8_DataProperty(),
							param.rack9_DataProperty(),
							param.rack10_DataProperty(),
							param.rack11_DataProperty(),
							param.rack12_DataProperty(),
							param.rack13_DataProperty(),
							
							param.rack14_DataProperty(),
							param.rack15_DataProperty(),
							param.rack16_DataProperty(),
							param.rack17_DataProperty(),
							param.rack18_DataProperty(),
							param.rack19_DataProperty(),
							param.rack20_DataProperty(),
							param.rack21_DataProperty(),
							param.rack22_DataProperty(),
							param.rack23_DataProperty(),
							param.rack24_DataProperty(),
							param.rack25_DataProperty(),
							param.rack26_DataProperty(),
							param.rack27_DataProperty(),
							param.rack28_DataProperty(),
							param.rack29_DataProperty(),
							param.rack30_DataProperty(),
							param.rack31_DataProperty(),
							param.rack32_DataProperty(),
							param.rack33_DataProperty(),
							param.rack34_DataProperty(),
							param.rack35_DataProperty(),
							param.rack36_DataProperty(),
							param.rack37_DataProperty(),
							param.rack38_DataProperty(),
							param.rack39_DataProperty(),
							param.rack40_DataProperty(),
							param.rack41_DataProperty(),
							param.rack42_DataProperty(),
							param.rack43_DataProperty(),
							param.rack44_DataProperty(),
							param.rack45_DataProperty(),
							param.rack46_DataProperty(),
							param.rack47_DataProperty(),
							param.rack48_DataProperty(),
							param.getExecutionStatusProperty()
					};
				}
			}
			);

	public static void ListLiveDataInitListener(){

		liveTableDataListObj.addListener(new ListChangeListener<liveTableData>() {
			@Override
			public void onChanged(Change<? extends liveTableData> c) {
				while (c.next()) {
					if (c.wasPermutated()) {
						for (int i = c.getFrom(); i < c.getTo(); ++i) {
							ApplicationLauncher.logger.info("ListLiveDataInitListener: permutated" + i + " " + liveTableDataListObj.get(i));
						}
					} else if (c.wasUpdated()) {
						ArrayList<Object> CurrentTestPointRowData = new ArrayList<Object>();
						for (int i = c.getFrom(); i < c.getTo(); ++i) {

							ApplicationLauncher.logger.info("ListLiveDataInitListener: Update Index: " + i + ":getTestPointName: " + liveTableDataListObj.get(i).getTestPointName());
							CurrentTestPointRowData = ProcessDataForliveTableData(liveTableDataListObj.get(i));

							ProjectExecutionController.update_live_table(i,CurrentTestPointRowData);
							AllRowData.set(i,CurrentTestPointRowData);

						}
					} else {
						for (@SuppressWarnings("unused") liveTableData removedItem : c.getRemoved()) {
							//ApplicationLauncher.logger.info("ListLiveDataInitListener:Removed: " + removedItem);
						}
						new ArrayList<Object>();
						for (liveTableData addedItem : c.getAddedSubList()) {
							//ApplicationLauncher.logger.debug("ListLiveDataInitListener:Added: " );

							AllRowData.add(Index++,ProcessDataForliveTableData(addedItem));
						}
					}
				}
			}
		});
	}

/*	public static void AppendDataToliveTableData(){

		liveTableDataListObj.add(new liveTableData("1","TP1","P -0.101","F 0.001","N WFR","Rack4-1",
				"Rack5-1","Rack6-1","Rack7-1","Rack8-1",
				"Rack9-1","Rack10-1","Rack11-1","Rack12-1"));
		liveTableDataListObj.add(new liveTableData("2","TP2","Rack1-2","Rack2-2","Rack3-2","Rack4-2",
				"Rack5-2","Rack6-2","Rack7-2","Rack8-2",
				"Rack9-2","Rack10-2","Rack11-2","Rack12-2"));
	}*/

	public static void AppendRowDataToliveTableData(liveTableData newRowData){
		//ApplicationLauncher.logger.debug("AppendRowDataToliveTableData : getSerialNo: " + newRowData.getSerialNo());
		//ApplicationLauncher.logger.debug("AppendRowDataToliveTableData : getExecutionStatus: " + newRowData.getExecutionStatus());
		liveTableDataListObj.add(newRowData);

	}
	
	public static int getCountOfNoOfTestPointCompleted(){
		
		int totalNoOfTestPoint = ProjectExecutionController.getCurrentProjectTestPointList().length();
		int totalTestPointCompleted = 0;
		ApplicationLauncher.logger.debug("getCountOfNoOfTestPointCompleted : totalNoOfTestPoint: " + totalNoOfTestPoint);
		String executionStatus = "";
		for (int i = 0; i<totalNoOfTestPoint; i++){
			executionStatus = liveTableDataListObj.get(i).getExecutionStatus();//.getRack11_Data();//ConstantProGEN_App.LIVE_TABLE_EXECUTION_STATUS_COLUMN_ID
			//ApplicationLauncher.logger.info("getCountOfNoOfTestPointCompleted : executionStatus: " + executionStatus);
			if (executionStatus.contains(ConstantApp.EXECUTION_STATUS_COMPLETED)){
				totalTestPointCompleted++;
			}
		}
		return totalTestPointCompleted;
	}

	public static void ResetliveTableData(){
		liveTableDataListObj.clear();
		AllRowData.clear();
		ActiveRackList.clear();
		Index = 0;

	}

	public static void setActiveRackList(List<String> CurrentActiveRackList ){
		//ApplicationLauncher.logger.debug("setActiveRackList : getCurrentProjectName:"+MeterParamsController.getCurrentProjectName());
		//ActiveRackList= ProjectExecutionController.getColNamesFromDB(MeterParamsController.getCurrentProjectName());
		//ApplicationLauncher.logger.debug("setActiveRackList : ActiveRackList:"+ActiveRackList.toString());
		ActiveRackList= CurrentActiveRackList;


	}

	public static List<String> getActiveRackList(){
		return  ActiveRackList;

	}

	public static ArrayList<ArrayList<Object>> getAllRowData(){
		return  AllRowData;

	}


	public static ArrayList<Object> ProcessDataForliveTableData(liveTableData NewItem){
		//ApplicationLauncher.logger.debug("ProcessDataForliveTableData : Entry");
		ArrayList<Object> EachRowData = new ArrayList<Object>();
		EachRowData.add(NewItem.getSerialNo());
		EachRowData.add(NewItem.getTestPointName());
		for(int i=0; i <getActiveRackList().size();i++){
			String CurrentRack = ActiveRackList.get(i);
			if (CurrentRack.endsWith("/1")){
				EachRowData.add(NewItem.getRack1_Data());
			} else if (CurrentRack.endsWith("/2")){
				EachRowData.add(NewItem.getRack2_Data());
			}else if (CurrentRack.endsWith("/3")){
				EachRowData.add(NewItem.getRack3_Data());
			}else if (CurrentRack.endsWith("/4")){
				EachRowData.add(NewItem.getRack4_Data());
			}else if (CurrentRack.endsWith("/5")){
				EachRowData.add(NewItem.getRack5_Data());
			}else if (CurrentRack.endsWith("/6")){
				EachRowData.add( NewItem.getRack6_Data());
			}else if (CurrentRack.endsWith("/7")){
				EachRowData.add(NewItem.getRack7_Data());
			}else if (CurrentRack.endsWith("/8")){
				EachRowData.add( NewItem.getRack8_Data());
			}else if (CurrentRack.endsWith("/9")){
				EachRowData.add( NewItem.getRack9_Data());
			}else if (CurrentRack.endsWith("/10")){
				EachRowData.add( NewItem.getRack10_Data());
			}else if (CurrentRack.endsWith("/11")){
				EachRowData.add( NewItem.getRack11_Data());
			}else if (CurrentRack.endsWith("/12")){
				EachRowData.add(NewItem.getRack12_Data());
			}else if (CurrentRack.endsWith("/13")){
				EachRowData.add(NewItem.getRack13_Data());
			}
			
			else if (CurrentRack.endsWith("/14")){
				EachRowData.add(NewItem.getRack14_Data());
			}else if (CurrentRack.endsWith("/15")){
				EachRowData.add(NewItem.getRack15_Data());
			}else if (CurrentRack.endsWith("/16")){
				EachRowData.add( NewItem.getRack16_Data());
			}else if (CurrentRack.endsWith("/17")){
				EachRowData.add(NewItem.getRack17_Data());
			}else if (CurrentRack.endsWith("/18")){
				EachRowData.add( NewItem.getRack18_Data());
			}else if (CurrentRack.endsWith("/19")){
				EachRowData.add( NewItem.getRack19_Data());
			}else if (CurrentRack.endsWith("/20")){
				EachRowData.add( NewItem.getRack20_Data());
			}else if (CurrentRack.endsWith("/21")){
				EachRowData.add(NewItem.getRack21_Data());
			} else if (CurrentRack.endsWith("/22")){
				EachRowData.add(NewItem.getRack22_Data());
			}else if (CurrentRack.endsWith("/23")){
				EachRowData.add(NewItem.getRack23_Data());
			}else if (CurrentRack.endsWith("/24")){
				EachRowData.add(NewItem.getRack24_Data());
			}else if (CurrentRack.endsWith("/25")){
				EachRowData.add(NewItem.getRack25_Data());
			}else if (CurrentRack.endsWith("/26")){
				EachRowData.add( NewItem.getRack26_Data());
			}else if (CurrentRack.endsWith("/27")){
				EachRowData.add(NewItem.getRack27_Data());
			}else if (CurrentRack.endsWith("/28")){
				EachRowData.add( NewItem.getRack28_Data());
			}else if (CurrentRack.endsWith("/29")){
				EachRowData.add( NewItem.getRack29_Data());
			}else if (CurrentRack.endsWith("/30")){
				EachRowData.add( NewItem.getRack30_Data());
			}else if (CurrentRack.endsWith("/31")){
				EachRowData.add(NewItem.getRack31_Data());
			} else if (CurrentRack.endsWith("/32")){
				EachRowData.add(NewItem.getRack32_Data());
			}else if (CurrentRack.endsWith("/33")){
				EachRowData.add(NewItem.getRack33_Data());
			}else if (CurrentRack.endsWith("/34")){
				EachRowData.add(NewItem.getRack34_Data());
			}else if (CurrentRack.endsWith("/35")){
				EachRowData.add(NewItem.getRack35_Data());
			}else if (CurrentRack.endsWith("/36")){
				EachRowData.add( NewItem.getRack36_Data());
			}else if (CurrentRack.endsWith("/37")){
				EachRowData.add(NewItem.getRack37_Data());
			}else if (CurrentRack.endsWith("/38")){
				EachRowData.add( NewItem.getRack38_Data());
			}else if (CurrentRack.endsWith("/39")){
				EachRowData.add( NewItem.getRack39_Data());
			}else if (CurrentRack.endsWith("/40")){
				EachRowData.add( NewItem.getRack40_Data());
			} else if (CurrentRack.endsWith("/41")){
				EachRowData.add(NewItem.getRack41_Data());
			} else if (CurrentRack.endsWith("/42")){
				EachRowData.add(NewItem.getRack42_Data());
			}else if (CurrentRack.endsWith("/43")){
				EachRowData.add(NewItem.getRack43_Data());
			}else if (CurrentRack.endsWith("/44")){
				EachRowData.add(NewItem.getRack44_Data());
			}else if (CurrentRack.endsWith("/45")){
				EachRowData.add(NewItem.getRack45_Data());
			}else if (CurrentRack.endsWith("/46")){
				EachRowData.add( NewItem.getRack46_Data());
			}else if (CurrentRack.endsWith("/47")){
				EachRowData.add(NewItem.getRack47_Data());
			}else if (CurrentRack.endsWith("/48")){
				EachRowData.add( NewItem.getRack48_Data());
			}else if (CurrentRack.endsWith(ConstantApp.LIVE_TABLE_EXECUTION_STATUS_DISPLAY_HEADER)){
				EachRowData.add( NewItem.getExecutionStatus());
			}
			
			
		}



		//ApplicationLauncher.logger.debug("ProcessDataForliveTableData : Exit");
		return EachRowData;
	}

	public static void InitLiveTableData(){
		ApplicationLauncher.logger.debug("InitLiveTableData : Entry");
		String Projectname = ProjectExecutionController.getCurrentProjectName();
		String deploymentID = ProjectExecutionController.getSelectedDeployment_ID();
		ArrayList<String> columnNames = new ArrayList<String>();
		columnNames.add("S.No");
		columnNames.add("Test Point");
		//columnNames.add(ConstantApp.LIVE_TABLE_EXECUTION_STATUS_DISPLAY_HEADER);
		columnNames.addAll(getActiveRackList());
		//columnNames.addAll(ProjectExecutionController.getColNamesFromDB(Projectname));
		JSONArray testcaselist;
		try {
			testcaselist = ProjectExecutionController.getListOfTestPoints(Projectname,deploymentID);
			initRowValues(testcaselist, columnNames);
		} catch (JSONException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("InitLiveTableData : JSONException:"+e.getMessage());
		}


	}

	public static ArrayList<ArrayList<Object>> initRowValues(JSONArray testcaselist,List<String>  devicesList) throws JSONException{
		ArrayList<ArrayList<Object>> AllRowValues = new ArrayList<ArrayList<Object>>() ;
		new ArrayList<Object>();
		JSONObject jobj = new JSONObject();
		for(int i=0; i<testcaselist.length(); i++){
			//ArrayList<Object> EachRowValues = new ArrayList<Object>();
			jobj = testcaselist.getJSONObject(i);
			/*EachRowValues.clear();
			EachRowValues.add(Integer.toString(i+1));
			EachRowValues.add(jobj.getString("test_case"));
			for(int DeviceColumn=2; DeviceColumn<(devicesList.size()); DeviceColumn++){
				EachRowValues.add(ConstantApp.TableRowFiller);
			}*/
			LiveTableDataManager.AppendRowDataToliveTableData(new liveTableData(Integer.toString(i+1),jobj.getString("test_case"),
					ConstantApp.EXECUTION_STATUS_NOT_EXECUTED,
					ConstantApp.TableRowFiller,ConstantApp.TableRowFiller,ConstantApp.TableRowFiller,ConstantApp.TableRowFiller,
					ConstantApp.TableRowFiller,ConstantApp.TableRowFiller,ConstantApp.TableRowFiller,ConstantApp.TableRowFiller,
					ConstantApp.TableRowFiller,ConstantApp.TableRowFiller,ConstantApp.TableRowFiller,ConstantApp.TableRowFiller,
					ConstantApp.TableRowFiller,ConstantApp.TableRowFiller,ConstantApp.TableRowFiller,ConstantApp.TableRowFiller,
					ConstantApp.TableRowFiller,ConstantApp.TableRowFiller,ConstantApp.TableRowFiller,ConstantApp.TableRowFiller,
					ConstantApp.TableRowFiller,ConstantApp.TableRowFiller,ConstantApp.TableRowFiller,ConstantApp.TableRowFiller,
					ConstantApp.TableRowFiller,ConstantApp.TableRowFiller,ConstantApp.TableRowFiller,ConstantApp.TableRowFiller,
					ConstantApp.TableRowFiller,ConstantApp.TableRowFiller,ConstantApp.TableRowFiller,ConstantApp.TableRowFiller,
					ConstantApp.TableRowFiller,ConstantApp.TableRowFiller,ConstantApp.TableRowFiller,ConstantApp.TableRowFiller,
					ConstantApp.TableRowFiller,ConstantApp.TableRowFiller,ConstantApp.TableRowFiller,ConstantApp.TableRowFiller,
					ConstantApp.TableRowFiller,ConstantApp.TableRowFiller,ConstantApp.TableRowFiller,ConstantApp.TableRowFiller,
					ConstantApp.TableRowFiller,ConstantApp.TableRowFiller,ConstantApp.TableRowFiller,ConstantApp.TableRowFiller
					
					));

			//AllRowValues.add(EachRowValues);
			//ApplicationLauncher.logger.debug("row: " + EachRowValues);

		}

		//ApplicationLauncher.logger.debug("initRowValues: AllRowValues: " + AllRowValues);

		return AllRowValues;
	}



	public static void UpdateliveTableData1(){
		liveTableDataListObj.get(0).setRack1_Data("Rack1-1c");

	}

	public static void UpdateliveTableData2(){

		liveTableDataListObj.get(1).setRack2_Data("Rack1-2d");
	}
	public static void UpdateliveTableData(int LDU_ReadAddress,String Resultstatus,String ErrorValue){
		ApplicationLauncher.logger.info("UpdateliveTableData : Entry");
		Integer CurrentTestPointIndex = ProjectExecutionController.getCurrentTestPoint_Index();
		String currentTestPointName = ProjectExecutionController.getCurrentTestPointName();
		if (CurrentTestPointIndex != ProjectExecutionController.getCurrentProject_TotalNoOfTestPoint()){
			ApplicationLauncher.logger.info("UpdateliveTableData : currentTestPointName: "+currentTestPointName+" : Index:"+CurrentTestPointIndex+" :Rack"+LDU_ReadAddress+":<"+Resultstatus + " " + ErrorValue+">");
			
			switch (LDU_ReadAddress) {

			case 1:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack1_Data(Resultstatus + " " + ErrorValue);
				//ApplicationLauncher.logger.info("UpdateliveTableData :"+CurrentProjectName+" Index:"+CurrentTestPointIndex+" :Rack"+LDU_ReadAddress+":<"+Resultstatus + " " + ErrorValue+">");
				break;
			case 2:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack2_Data(Resultstatus + " " + ErrorValue);
				//ApplicationLauncher.logger.info("UpdateliveTableData :"+CurrentProjectName+" Index:"+CurrentTestPointIndex+" :Rack"+LDU_ReadAddress+":<"+Resultstatus + " " + ErrorValue+">");
				break;
			case 3:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack3_Data(Resultstatus + " " + ErrorValue);
				//ApplicationLauncher.logger.info("UpdateliveTableData :"+CurrentProjectName+" Index:"+CurrentTestPointIndex+" :Rack"+LDU_ReadAddress+":<"+Resultstatus + " " + ErrorValue+">");
				break;
			case 4:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack4_Data(Resultstatus + " " + ErrorValue);
				//ApplicationLauncher.logger.info("UpdateliveTableData :"+CurrentProjectName+" Index:"+CurrentTestPointIndex+" :Rack"+LDU_ReadAddress+":<"+Resultstatus + " " + ErrorValue+">");
				break;
			case 5:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack5_Data(Resultstatus + " " + ErrorValue);
				//ApplicationLauncher.logger.info("UpdateliveTableData :"+CurrentProjectName+" Index:"+CurrentTestPointIndex+" :Rack"+LDU_ReadAddress+":<"+Resultstatus + " " + ErrorValue+">");
				break;
			case 6:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack6_Data(Resultstatus + " " + ErrorValue);
				//ApplicationLauncher.logger.info("UpdateliveTableData :"+CurrentProjectName+" Index:"+CurrentTestPointIndex+" :Rack"+LDU_ReadAddress+":<"+Resultstatus + " " + ErrorValue+">");
				break;
			case 7:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack7_Data(Resultstatus + " " + ErrorValue);
				//ApplicationLauncher.logger.info("UpdateliveTableData :"+CurrentProjectName+" Index:"+CurrentTestPointIndex+" :Rack"+LDU_ReadAddress+":<"+Resultstatus + " " + ErrorValue+">");
				break;
			case 8:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack8_Data(Resultstatus + " " + ErrorValue);
				//ApplicationLauncher.logger.info("UpdateliveTableData :"+CurrentProjectName+" Index:"+CurrentTestPointIndex+" :Rack"+LDU_ReadAddress+":<"+Resultstatus + " " + ErrorValue+">");
				break;
			case 9:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack9_Data(Resultstatus + " " + ErrorValue);
				//ApplicationLauncher.logger.info("UpdateliveTableData :"+CurrentProjectName+" Index:"+CurrentTestPointIndex+" :Rack"+LDU_ReadAddress+":<"+Resultstatus + " " + ErrorValue+">");
				break;
			case 10:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack10_Data(Resultstatus + " " + ErrorValue);
				//ApplicationLauncher.logger.info("UpdateliveTableData :"+CurrentProjectName+" Index:"+CurrentTestPointIndex+" :Rack"+LDU_ReadAddress+":<"+Resultstatus + " " + ErrorValue+">");
				break;
			case 11:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack11_Data(Resultstatus + " " + ErrorValue);
				//ApplicationLauncher.logger.info("UpdateliveTableData :"+CurrentProjectName+" Index:"+CurrentTestPointIndex+" :Rack"+LDU_ReadAddress+":<"+Resultstatus + " " + ErrorValue+">");
				break;
			case 12:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack12_Data(Resultstatus + " " + ErrorValue);
				//ApplicationLauncher.logger.info("UpdateliveTableData :"+CurrentProjectName+" Index:"+CurrentTestPointIndex+" :Rack"+LDU_ReadAddress+":<"+Resultstatus + " " + ErrorValue+">");
				break;
			case 13:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack13_Data(Resultstatus + " " + ErrorValue);
				//ApplicationLauncher.logger.info("UpdateliveTableData :"+CurrentProjectName+" Index:"+CurrentTestPointIndex+" :Rack"+LDU_ReadAddress+":<"+Resultstatus + " " + ErrorValue+">");
				break;
				
				
				
			case 14:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack14_Data(Resultstatus + " " + ErrorValue);
				break;
			case 15:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack15_Data(Resultstatus + " " + ErrorValue);
				break;
			case 16:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack16_Data(Resultstatus + " " + ErrorValue);
				break;
			case 17:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack17_Data(Resultstatus + " " + ErrorValue);
				break;
			case 18:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack18_Data(Resultstatus + " " + ErrorValue);
				break;
			case 19:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack19_Data(Resultstatus + " " + ErrorValue);
				break;
			case 20:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack20_Data(Resultstatus + " " + ErrorValue);
				break;

			
				
			case 21:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack21_Data(Resultstatus + " " + ErrorValue);
				break;
			case 22:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack22_Data(Resultstatus + " " + ErrorValue);
				break;
			case 23:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack23_Data(Resultstatus + " " + ErrorValue);
				break;
			case 24:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack24_Data(Resultstatus + " " + ErrorValue);
				break;
			case 25:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack25_Data(Resultstatus + " " + ErrorValue);
				break;
			case 26:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack26_Data(Resultstatus + " " + ErrorValue);
				break;
			case 27:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack27_Data(Resultstatus + " " + ErrorValue);
				break;
			case 28:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack28_Data(Resultstatus + " " + ErrorValue);
				break;
			case 29:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack29_Data(Resultstatus + " " + ErrorValue);
				break;
			case 30:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack30_Data(Resultstatus + " " + ErrorValue);
				break;
				
			case 31:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack31_Data(Resultstatus + " " + ErrorValue);
				break;
			case 32:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack32_Data(Resultstatus + " " + ErrorValue);
				break;
			case 33:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack33_Data(Resultstatus + " " + ErrorValue);
				break;
			case 34:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack34_Data(Resultstatus + " " + ErrorValue);
				break;
			case 35:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack35_Data(Resultstatus + " " + ErrorValue);
				break;
			case 36:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack36_Data(Resultstatus + " " + ErrorValue);
				break;
			case 37:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack37_Data(Resultstatus + " " + ErrorValue);
				break;
			case 38:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack38_Data(Resultstatus + " " + ErrorValue);
				break;
			case 39:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack39_Data(Resultstatus + " " + ErrorValue);
				break;
			case 40:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack40_Data(Resultstatus + " " + ErrorValue);
				break;
				
			case 41:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack41_Data(Resultstatus + " " + ErrorValue);
				break;
			case 42:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack42_Data(Resultstatus + " " + ErrorValue);
				break;
			case 43:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack43_Data(Resultstatus + " " + ErrorValue);
				break;
			case 44:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack44_Data(Resultstatus + " " + ErrorValue);
				break;
			case 45:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack45_Data(Resultstatus + " " + ErrorValue);
				break;
			case 46:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack46_Data(Resultstatus + " " + ErrorValue);
				break;
			case 47:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack47_Data(Resultstatus + " " + ErrorValue);
				break;
			case 48:
				liveTableDataListObj.get(CurrentTestPointIndex).setRack48_Data(Resultstatus + " " + ErrorValue);
				break;
				
			case ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID:
				liveTableDataListObj.get(CurrentTestPointIndex).setExecutionStatus(Resultstatus + " "+ErrorValue);
				break;

				
				
				
			default:
				break;
			}
		}


	}

	public static String getliveTableData(int LDU_ReadAddress){
		ApplicationLauncher.logger.info("getliveTableData : Entry");
		String ResultData=null;
		Integer Current_TestPointIndex = ProjectExecutionController.getCurrentTestPoint_Index();
		if (Current_TestPointIndex != ProjectExecutionController.getCurrentProject_TotalNoOfTestPoint()){
			switch (LDU_ReadAddress) {

			case 1:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack1_Data();
				break;
			case 2:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack2_Data();
				break;
			case 3:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack3_Data();
				break;
			case 4:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack4_Data();
				break;
			case 5:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack5_Data();
				break;
			case 6:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack6_Data();
				break;
			case 7:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack7_Data();
				break;
			case 8:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack8_Data();
				break;
			case 9:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack9_Data();
				break;
			case 10:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack10_Data();
				break;
			case 11:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack11_Data();
				break;
			case 12:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack12_Data();
				break;
			case 13:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack13_Data();
				break;
				
			case 14:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack14_Data();
				break;
			case 15:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack15_Data();
				break;
			case 16:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack16_Data();
				break;
			case 17:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack17_Data();
				break;
			case 18:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack18_Data();
				break;
			case 19:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack19_Data();
				break;
			case 20:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack20_Data();
				break;
				
			case 21:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack21_Data();
				break;
			case 22:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack22_Data();
				break;
			case 23:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack23_Data();
				break;
			case 24:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack24_Data();
				break;
			case 25:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack25_Data();
				break;
			case 26:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack26_Data();
				break;
			case 27:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack27_Data();
				break;
			case 28:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack28_Data();
				break;
			case 29:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack29_Data();
				break;
			case 30:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack30_Data();
				break;
				
			case 31:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack31_Data();
				break;
			case 32:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack32_Data();
				break;
			case 33:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack33_Data();
				break;
			case 34:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack34_Data();
				break;
			case 35:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack35_Data();
				break;
			case 36:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack36_Data();
				break;
			case 37:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack37_Data();
				break;
			case 38:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack38_Data();
				break;
			case 39:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack39_Data();
				break;
			case 40:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack40_Data();
				break;
				
			case 41:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack41_Data();
				break;
			case 42:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack42_Data();
				break;
			case 43:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack43_Data();
				break;
			case 44:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack44_Data();
				break;
			case 45:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack45_Data();
				break;
			case 46:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack46_Data();
				break;
			case 47:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack47_Data();
				break;
			case 48:
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getRack48_Data();
				break;
				
			case ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID:
				
				ResultData = liveTableDataListObj.get(Current_TestPointIndex).getExecutionStatus();
				break;

				
			default:
				break;
			}
		}
		return ResultData;
	}
}
