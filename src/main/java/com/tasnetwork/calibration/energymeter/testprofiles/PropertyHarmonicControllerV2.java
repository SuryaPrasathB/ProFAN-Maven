package com.tasnetwork.calibration.energymeter.testprofiles;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
//import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
//import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantLscsHarmonicsSourceSlave;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.database.MySQL_Controller;
import com.tasnetwork.calibration.energymeter.project.ProjectController;
//import com.tasnetwork.calibration.energymeter.testprofiles.PropertyHarmonicController.SaveHarmonicDataTask;
//import com.tasnetwork.calibration.energymeter.util.ErrorCodeMapping;
//import com.tasnetwork.calibration.energymeter.project.TemperatureDisplayModel;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;

//import javafx.beans.value.ChangeListener;
//import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
//import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
//import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;


//  Not Tested - GUI - for Single Phase Test

public class PropertyHarmonicControllerV2 extends AnchorPane{     	
	
	//============================New things added by Pradeep - UnApproved ========================================
	
	@FXML CheckBox checkBoxPhaseR ;
	@FXML CheckBox checkBoxPhaseY ;
	@FXML CheckBox checkBoxPhaseB ;
	
	@FXML Tab tab_R_PhaseData ;
	@FXML Tab tab_Y_PhaseData ;
	@FXML Tab tab_B_PhaseData ;
	
	@FXML ComboBox<String> cmbBox_Un;
	public static ComboBox<String>  ref_cmbBox_Un;
	
	//@FXML TableView tv_R_PhaseData = new TableView();
	@FXML private TableView<HarmonicsDataModel> tv_R_PhaseData ;
	@FXML private TableView<HarmonicsDataModel> tv_Y_PhaseData ;
	@FXML private TableView<HarmonicsDataModel> tv_B_PhaseData ;
	
	public static TableView<HarmonicsDataModel> ref_tv_R_PhaseData ;
	public static TableView<HarmonicsDataModel> ref_tv_Y_PhaseData ;
	public static TableView<HarmonicsDataModel> ref_tv_B_PhaseData ;

	@FXML private TableColumn<HarmonicsDataModel, Number> colSerialNum_R_Phase;  
	
	@FXML private TableColumn colEnableStatus_R_Phase_V;  
	@FXML private TableColumn<HarmonicsDataModel, Number> colHarmonicsOrder_R_Phase_V;
	@FXML private TableColumn<HarmonicsDataModel, String> colAmplitude_R_Phase_V;
	@FXML private TableColumn<HarmonicsDataModel, String> colPhaseShift_R_Phase_V;
	
	@FXML private TableColumn colEnableStatus_R_Phase_I;  
	@FXML private TableColumn<HarmonicsDataModel, Number> colHarmonicsOrder_R_Phase_I;
	@FXML private TableColumn<HarmonicsDataModel, String> colAmplitude_R_Phase_I;
	@FXML private TableColumn<HarmonicsDataModel, String> colPhaseShift_R_Phase_I;
	
	@FXML private TableColumn<HarmonicsDataModel, Number> colSerialNum_B_Phase;
	
	@FXML private TableColumn colEnableStatus_B_Phase_V;  
	@FXML private TableColumn<HarmonicsDataModel, Number> colHarmonicsOrder_B_Phase_V;
	@FXML private TableColumn<HarmonicsDataModel, String> colAmplitude_B_Phase_V;
	@FXML private TableColumn<HarmonicsDataModel, String> colPhaseShift_B_Phase_V;
	
	@FXML private TableColumn colEnableStatus_B_Phase_I;  
	@FXML private TableColumn<HarmonicsDataModel, Number> colHarmonicsOrder_B_Phase_I;
	@FXML private TableColumn<HarmonicsDataModel, String> colAmplitude_B_Phase_I;
	@FXML private TableColumn<HarmonicsDataModel, String> colPhaseShift_B_Phase_I;
	
	@FXML private TableColumn<HarmonicsDataModel, Number> colSerialNum_Y_Phase;   
	
	@FXML private TableColumn colEnableStatus_Y_Phase_V;  
	@FXML private TableColumn<HarmonicsDataModel, Number> colHarmonicsOrder_Y_Phase_V;
	@FXML private TableColumn<HarmonicsDataModel, String> colAmplitude_Y_Phase_V;
	@FXML private TableColumn<HarmonicsDataModel, String> colPhaseShift_Y_Phase_V;
	
	@FXML private TableColumn colEnableStatus_Y_Phase_I;  
	@FXML private TableColumn<HarmonicsDataModel, Number> colHarmonicsOrder_Y_Phase_I;
	@FXML private TableColumn<HarmonicsDataModel, String> colAmplitude_Y_Phase_I;
	@FXML private TableColumn<HarmonicsDataModel, String> colPhaseShift_Y_Phase_I;
	
	@FXML private TableColumn<HarmonicsDataModel, Number> colHarmonicsOrder_R_Phase;
	@FXML private TableColumn<HarmonicsDataModel, Number> colHarmonicsOrder_Y_Phase;
	@FXML private TableColumn<HarmonicsDataModel, Number> colHarmonicsOrder_B_Phase;

	@FXML private ComboBox comboBoxFrequency = new ComboBox();
	public static ComboBox<String>  ref_comboBoxFrequency;
	
	@FXML TextField txtTestType;
	public static TextField ref_txtTestType;

	@FXML TextField txtAlias_ID;
	public static TextField ref_txtAlias_ID;   
	@FXML private Button btn_Save ; 
	
	Timer SaveDataTimer;
	@FXML CheckBox checkbox_inphase;
	public static CheckBox ref_checkbox_inphase;
	@FXML CheckBox checkbox_outphase;
	public static CheckBox ref_checkbox_outphase;
	
	public static boolean phase_R_Selected = false ;
	public static boolean phase_Y_Selected = false ;
	public static boolean phase_B_Selected = false ;

	public static DraggableTestNode mCurrentNode=null;
	
	static int saved_successfully_count =0;
	private String saveFailedReason = "";
	
	
	public String harmonicsTestPointNamePart = "" ;
	
	public static CheckBox ref_checkBoxPhaseR ;
	public static CheckBox ref_checkBoxPhaseY ;
	public static CheckBox ref_checkBoxPhaseB ;
	
	public static Tab ref_tab_R_PhaseData ;
	public static Tab ref_tab_Y_PhaseData ;
	public static Tab ref_tab_B_PhaseData ;
	
	/*ref_tv_R_PhaseData ;
	ref_tv_Y_PhaseData ;
	ref_tv_B_PhaseData ;

	ref_colSerialNum_R_Phase ;
	ref_colEnable_R_Phase ;
	ref_colHarmonicsOrder_R_Phase ;
	ref_colAmp_R_Phase ;
	ref_colPhaseShift_R ;

	ref_colSerialNum_Y_Phase ;
	ref_colEnable_Y_Phase ;
	ref_colHarmonicsOrder_Y_Phase ;
	ref_colAmp_Y_Phase ;
	ref_colPhaseShift_Y ;

	ref_colSerialNum_B_Phase ;
	ref_colEnable_B_Phase ;
	ref_colHarmonicsOrder_B_Phase ;
	ref_colAmp_B_Phase ;
	ref_colPhaseShift_B ;*/
	
	
//==============================================================================================================

	

@FXML
private void initialize() throws IOException {

	ApplicationLauncher.logger.debug("initialize: Entry");
	
	refAssignment();
	loadTableProperty();
	initialiseHarmonicsDataOnTable();
	
/*	ref_colSerialNum_R_Phase = colSerialNum_R_Phase;
	ref_colEnable_R_Phase = colEnable_R_Phase;
	ref_colHarmonicsOrder_R_Phase =  colHarmonicsOrder_R_Phase;
	ref_colAmp_R_Phase = colAmp_R_Phase;
	ref_colPhaseShift_R = colPhaseShift_R;

	ref_colSerialNum_Y_Phase = colSerialNum_Y_Phase;
	ref_colEnable_Y_Phase =  colEnable_Y_Phase;
	ref_colHarmonicsOrder_Y_Phase = colHarmonicsOrder_Y_Phase;
	ref_colAmp_Y_Phase = colAmp_Y_Phase;
	ref_colPhaseShift_Y = colPhaseShift_Y;

	ref_colSerialNum_B_Phase = colSerialNum_B_Phase;
	ref_colEnable_B_Phase = colEnable_B_Phase;
	ref_colHarmonicsOrder_B_Phase = colHarmonicsOrder_B_Phase;
	ref_colAmp_B_Phase = colAmp_B_Phase;
	ref_colPhaseShift_B = colPhaseShift_B;
	*/
	
	
	/*if(ProcalFeatureEnable.USER_ACCESS_CONTROL_ENABLED){
		applyUacSettings();
	}
	
	DisableSecondRowHarmonicInputOnGUI();//Customer de
*/	
	
	ApplicationLauncher.logger.debug("initialize: Exit");
}
	
	
private void refAssignment() {
	// TODO Auto-generated method stub
	ref_txtTestType    = txtTestType;
	ref_txtAlias_ID    = txtAlias_ID;
	
	ref_checkBoxPhaseR = checkBoxPhaseR ;
	ref_checkBoxPhaseY = checkBoxPhaseY ;
	ref_checkBoxPhaseB = checkBoxPhaseB ;
	
	ref_cmbBox_Un      = cmbBox_Un;
	ref_comboBoxFrequency = comboBoxFrequency ;
	
	ref_tv_R_PhaseData = tv_R_PhaseData ;
	ref_tv_Y_PhaseData = tv_Y_PhaseData ;
	ref_tv_B_PhaseData = tv_B_PhaseData ;
	
	 ref_tab_R_PhaseData = tab_R_PhaseData;
	 ref_tab_Y_PhaseData = tab_Y_PhaseData;
	 ref_tab_B_PhaseData = tab_B_PhaseData;
	 
	 ref_checkbox_inphase = checkbox_inphase;
	 ref_checkbox_outphase = checkbox_outphase;

/*	
	if(ref_checkBoxPhaseR.isSelected()){
		tab_R_PhaseData.setDisable(false);
	}
	else{
		tab_R_PhaseData.setDisable(true);
	}

	if(ref_checkBoxPhaseY.isSelected()){
		tab_Y_PhaseData.setDisable(false);
	}
	else{
		tab_Y_PhaseData.setDisable(true);
	}

	if(ref_checkBoxPhaseB.isSelected()){
		tab_B_PhaseData.setDisable(false);
	}
	else{
		tab_B_PhaseData.setDisable(true);
	}*/

}

public static void LoadVolt(String project_name, String testType, String aliasId){
	JSONObject testdetailslist = MySQL_Controller.sp_getproject_components(project_name, testType, aliasId);
	ApplicationLauncher.logger.info("testdetailslist: " + testdetailslist);
	try {
		JSONArray testcase = testdetailslist.getJSONArray("test_details");
		JSONObject test = new JSONObject();
		String volt = "";
		for(int i=0; i<testcase.length();i++){
			test = testcase.getJSONObject(i);
			volt = test.getString("voltage");
			ref_cmbBox_Un.setValue(volt);
		}



	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		ApplicationLauncher.logger.error("LoadVolt: JSONException:"+e.getMessage());
		
	}
}


public static  void PropertyDisplayUpdate(String testType, String aliasId,DraggableTestNode SelectedNode){
	
	ApplicationLauncher.logger.debug("PropertyHarmonicControllerV2: PropertyDisplayUpdate: Entry");

	mCurrentNode = SelectedNode;  // jhkjh
	ref_txtTestType.setText(testType);
	ref_txtAlias_ID.setText(aliasId);
	ArrayList<String> ModelList = new ArrayList<String>();

	ModelList.add("Select");
	for(int i=ConstantApp.HARMONIC_COMPONENT_MIN; i<ConstantApp.HARMONIC_COMPONENT_MAX; i++){
		ModelList.add(Integer.toString(i));
	}
/*
	ref_cmbBox_harmonic1.getItems().clear();
	ref_cmbBox_harmonic1.getItems().addAll(ModelList);
	ref_cmbBox_harmonic1.getSelectionModel().select(0);

	ref_cmbBox_harmonic2.getItems().clear();
	ref_cmbBox_harmonic2.getItems().addAll(ModelList);
	ref_cmbBox_harmonic2.getSelectionModel().select(0);
*/
	ArrayList<String> volts = new ArrayList<String>();

	volts.add("100");
	volts.add("125");
	volts.add("150");

	ref_cmbBox_Un.getItems().clear();
	ref_cmbBox_Un.getItems().addAll(volts);
	ref_cmbBox_Un.getSelectionModel().select(0) ;
/*
	ref_cmbBox_harmonic3.getItems().clear();
	ref_cmbBox_harmonic3.getItems().addAll(ModelList);
	ref_cmbBox_harmonic3.getSelectionModel().select(0);

	ref_cmbBox_harmonic4.getItems().clear();
	ref_cmbBox_harmonic4.getItems().addAll(ModelList);
	ref_cmbBox_harmonic4.getSelectionModel().select(0);*/
	
	try {
		ApplicationLauncher.logger.debug("PropertyDisplayUpdate: getModelType: " + ProjectController.getModelType(mCurrentNode.getProjectName()));
		  
        if(ProjectController.getModelType(mCurrentNode.getProjectName()).startsWith(ConstantApp.METERTYPE_SINGLEPHASE)){       //Not Tested
        	ref_tab_Y_PhaseData.setDisable(true);
        	ref_checkBoxPhaseY.setDisable(true);
        	ref_checkBoxPhaseY.setSelected(false);
			setPhase_Y_Selected(false);
        	
        	ref_tab_B_PhaseData.setDisable(true);
        	ref_checkBoxPhaseB.setDisable(true);
        	ref_checkBoxPhaseB.setSelected(false);
			setPhase_B_Selected(false);
			
			ref_checkBoxPhaseR.setSelected(true);
			ref_checkBoxPhaseR.setDisable(true);
			ref_tab_R_PhaseData.setDisable(false);
			setPhase_R_Selected(true);
		}
		
		
		if(mCurrentNode.IsNewNode() != true){
			String project_name = mCurrentNode.getProjectName();
			
			LoadVolt(project_name, testType, aliasId);
			JSONObject harmonics = MySQL_Controller.sp_getharmonic_data(project_name, testType, aliasId);

			JSONArray harmonic_data = harmonics.getJSONArray("Harmonic_data");
			//System.out.println("PropertyDisplayUpdate: harmonic_data: " + harmonic_data);
			ApplicationLauncher.logger.debug("PropertyDisplayUpdate: harmonic_data: " + harmonic_data);
			JSONObject jobj = new JSONObject();
			
			int harmonic_no = 0;
			String harmonic_times = "";
			String harmonic_volt = "";
			String harmonic_current = "";
			String harmonic_phase = "";
			String harmonic_volt_phase = "";
			String harmonic_current_phase = "";
			String phase_selected = "";
			int harmonic_order = 0;
			String fund_freq = ""; 
			//project_name  
			String test_case_name = ""; 
			//test_type  
			//test_alias_id  
 	
			for(int i=0; i<harmonic_data.length(); i++){
				
				 harmonic_no = 0;
				 harmonic_times = "";
				 harmonic_volt = "";
				 harmonic_current = "";
				 harmonic_phase = "";
				 harmonic_volt_phase = "";
				 harmonic_current_phase = "";
				 phase_selected = "";
				 harmonic_order = 0;
				 fund_freq = ""; 
				try{
					jobj = harmonic_data.getJSONObject(i);
					
					harmonic_no            = jobj.getInt("harmonic_no");
					harmonic_times         = jobj.getString("harmonic_times");
					harmonic_volt          = jobj.getString("harmonic_volt");
					harmonic_current       = jobj.getString("harmonic_current");
					harmonic_phase         = jobj.getString("harmonic_phase");
					harmonic_volt_phase    = jobj.getString("harmonic_volt_phase");
					harmonic_current_phase = jobj.getString("harmonic_current_phase");
					phase_selected         = jobj.getString("phase_selected");
					harmonic_order         = jobj.getInt("harmonic_order");
					fund_freq              = jobj.getString("fund_freq"); 
					test_case_name         = jobj.getString("test_case_name"); 
				} catch(Exception e){
					e.printStackTrace();
					ApplicationLauncher.logger.debug("PropertyDisplayUpdate: Exception: Index: "  + i + " : "+ e.getMessage());
				}
				boolean orderSelectedStatus_V = false ;
				boolean orderSelectedStatus_I = false ;
				
//================				
				
				ref_comboBoxFrequency.setValue(fund_freq);         // fundamental freq from DB
				if(test_case_name.equals(ConstantApp.HARMONIC_INPHASE_ALIAS_NAME)){
					ref_checkbox_inphase.setSelected(true);
				}
				
				if(test_case_name.equals(ConstantApp.HARMONIC_OUTOFPHASE_ALIAS_NAME)){
					ref_checkbox_outphase.setSelected(true);
				}
//================					
				if(phase_selected.equals(ConstantLscsHarmonicsSourceSlave.R_PHASE )){
					 
					ApplicationLauncher.logger.debug("PropertyDisplayUpdate:  R Phase");
					ApplicationLauncher.logger.debug("PropertyDisplayUpdate:  harmonic_order: " + harmonic_order);
					
					ref_checkBoxPhaseR.setSelected(true);
					setPhase_R_Selected(true);
					
					ref_tab_R_PhaseData.setDisable(false);
					
					//for(int j=2; j<= ConstantLscsHarmonicsSourceSlave.TOTAL_NO_OF_ORDER_HARMONICS; j++){
					//if(harmonic_order == j){
							
					 if(Integer.parseInt(harmonic_volt) > 0){
						 orderSelectedStatus_V = true ;						
					 }else{
						 orderSelectedStatus_V = false ;						 
					 }
					 
					 if(Integer.parseInt(harmonic_current) > 0){
						 orderSelectedStatus_I = true ;
					 }else{
						 orderSelectedStatus_I = false ;
					 }
							
						//	HarmonicsDataModel harmonicsData_R_Phase = new HarmonicsDataModel(phase_selected, harmonic_order, 
						//										                              orderSelectedStatus_V, harmonic_volt, harmonic_volt_phase, 
						//										                              orderSelectedStatus_I, harmonic_current, harmonic_current_phase);
														
						/*	harmonicsData_R_Phase.setPhaseSelected(phase_selected);
							harmonicsData_R_Phase.setHarmonicsOrder(harmonic_order);
							
							harmonicsData_R_Phase.setHarmonicOrder_V_Selected(orderSelectedStatus_I);
							harmonicsData_R_Phase.setAmplitude_V(harmonic_volt);
							harmonicsData_R_Phase.setPhaseShift_V(harmonic_volt_phase);
							
							harmonicsData_R_Phase.setHarmonicOrder_I_Selected(orderSelectedStatus_I);
							harmonicsData_R_Phase.setAmplitude_I(harmonic_current);
							harmonicsData_R_Phase.setPhaseShift_I(harmonic_current_phase); */       
							
							//ref_tv_R_PhaseData.getItems().add(harmonicsData_R_Phase);
							
							String localPhase_selected = phase_selected ;
							int localHarmonicOrder = harmonic_order;
							
							boolean localOrderSelectedStatus_V = orderSelectedStatus_V;
							String localHarmonic_volt = harmonic_volt ;
                            String localHarmonic_volt_phase = harmonic_volt_phase ;
                            
                            boolean localOrderSelectedStatus_I = orderSelectedStatus_I;
							String localHarmonic_current = harmonic_current ;
                            String localharmonic_current_phase = harmonic_current_phase ;								
							
							ref_tv_R_PhaseData.getItems().stream()
														.filter(e -> e.getHarmonicsOrder() == localHarmonicOrder )
														.forEachOrdered(e -> {
																			  e.setPhaseSelected(localPhase_selected);
																			  e.setHarmonicsOrder(localHarmonicOrder);
																			
																			  e.setHarmonicOrder_V_Selected(localOrderSelectedStatus_V);
																			  e.setAmplitude_V(localHarmonic_volt);
																			  e.setPhaseShift_V(localHarmonic_volt_phase);
																			
																			  e.setHarmonicOrder_I_Selected(localOrderSelectedStatus_I);
																			  e.setAmplitude_I(localHarmonic_current);
																			  e.setPhaseShift_I(localharmonic_current_phase);
																		});
					//	}
				//	}
					
				}
				
				else if(phase_selected.equals(ConstantLscsHarmonicsSourceSlave.Y_PHASE)){
					
					ref_checkBoxPhaseY.setSelected(true);
					setPhase_Y_Selected(true);
					ref_tab_Y_PhaseData.setDisable(false);

					//for(int j=2; j<= ConstantLscsHarmonicsSourceSlave.TOTAL_NO_OF_ORDER_HARMONICS; j++){
					//	if(harmonic_order == j){
					 if(Integer.parseInt(harmonic_volt) > 0){
						 orderSelectedStatus_V = true ;
					 }else{
						 orderSelectedStatus_V = false ;
					 }
					 if(Integer.parseInt(harmonic_current) > 0){
						 orderSelectedStatus_I = true ;
					 }else{
						 orderSelectedStatus_I = false ;
					 }
							
//							HarmonicsDataModel harmonicsData_Y_Phase = new HarmonicsDataModel(phase_selected, harmonic_order, 
//																                              orderSelectedStatus_V, harmonic_volt, harmonic_volt_phase, 
//																                              orderSelectedStatus_I, harmonic_current, harmonic_current_phase);

						//	ref_tv_Y_PhaseData.getItems().add(harmonicsData_Y_Phase);
							
							String localPhase_selected = phase_selected ;
							int localHarmonicOrder = harmonic_order;
							
							boolean localOrderSelectedStatus_V = orderSelectedStatus_V;
							String localHarmonic_volt = harmonic_volt ;
                            String localHarmonic_volt_phase = harmonic_volt_phase ;
                            
                            boolean localOrderSelectedStatus_I = orderSelectedStatus_I;
							String localHarmonic_current = harmonic_current ;
                            String localharmonic_current_phase = harmonic_current_phase ;								
							
							ref_tv_Y_PhaseData.getItems().stream()
														.filter(e -> e.getHarmonicsOrder() == localHarmonicOrder )
														.forEachOrdered(e -> {
															e.setPhaseSelected(localPhase_selected);
															e.setHarmonicsOrder(localHarmonicOrder);
															
															e.setHarmonicOrder_V_Selected(localOrderSelectedStatus_V);
															e.setAmplitude_V(localHarmonic_volt);
															e.setPhaseShift_V(localHarmonic_volt_phase);
															
															e.setHarmonicOrder_I_Selected(localOrderSelectedStatus_I);
															e.setAmplitude_I(localHarmonic_current);
															e.setPhaseShift_I(localharmonic_current_phase);
														
														});
					//	}
				//	}
				}
				
				else if(phase_selected.equals(ConstantLscsHarmonicsSourceSlave.B_PHASE)){

					ref_checkBoxPhaseB.setSelected(true);
						setPhase_B_Selected(true);

					ref_tab_B_PhaseData.setDisable(false);
					
				//	for(int j=2; j<= ConstantLscsHarmonicsSourceSlave.TOTAL_NO_OF_ORDER_HARMONICS; j++){
				//		if(harmonic_order == j){
					 if(Integer.parseInt(harmonic_volt) > 0){
						 orderSelectedStatus_V = true ;
					 }else{
						 orderSelectedStatus_V = false ;
					 }
					 if(Integer.parseInt(harmonic_current) > 0){
						 orderSelectedStatus_I = true ;
					 }else{
						 orderSelectedStatus_I = false ;
					 }
							
//							HarmonicsDataModel harmonicsData_B_Phase = new HarmonicsDataModel(phase_selected, harmonic_order, 
//																                              orderSelectedStatus_V, harmonic_volt, harmonic_volt_phase, 
//																                              orderSelectedStatus_I, harmonic_current, harmonic_current_phase);

						//	ref_tv_B_PhaseData.getItems().add(harmonicsData_B_Phase);
							String localPhase_selected = phase_selected ;
							int localHarmonicOrder = harmonic_order;
							
							boolean localOrderSelectedStatus_V = orderSelectedStatus_V;
							String localHarmonic_volt = harmonic_volt ;
                            String localHarmonic_volt_phase = harmonic_volt_phase ;
                            
                            boolean localOrderSelectedStatus_I = orderSelectedStatus_I;
							String localHarmonic_current = harmonic_current ;
                            String localharmonic_current_phase = harmonic_current_phase ;								
							
							ref_tv_B_PhaseData.getItems().stream()
														.filter(e -> e.getHarmonicsOrder() == localHarmonicOrder )
														.forEachOrdered(e -> {
															e.setPhaseSelected(localPhase_selected);
															e.setHarmonicsOrder(localHarmonicOrder);
															
															e.setHarmonicOrder_V_Selected(localOrderSelectedStatus_V);
															e.setAmplitude_V(localHarmonic_volt);
															e.setPhaseShift_V(localHarmonic_volt_phase);
															
															e.setHarmonicOrder_I_Selected(localOrderSelectedStatus_I);
															e.setAmplitude_I(localHarmonic_current);
															e.setPhaseShift_I(localharmonic_current_phase);														
														});
					//	}
					//}
				}
				
//================				

			}


		}
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		ApplicationLauncher.logger.error("PropertyHarmonicController: PropertyDisplayUpdate: JSONException:"+e.getMessage());
	}
}
	


private void initialiseHarmonicsDataOnTable() {
	
	ApplicationLauncher.logger.debug("initialiseHarmonicsDataOnTable: Entry");
	
	for(float frequency = 45;frequency<=55; frequency += 0.1 ){
		//ApplicationLauncher.logger.debug("initialiseHarmonicsDataOnTable: frequency : " + frequency);
	    comboBoxFrequency.getItems().add(String.format("%.1f", frequency));
	}
	   // Set the default item in the combo box
	   comboBoxFrequency.setValue("50");

	comboBoxFrequency.getValue();
	
	initialiseHarmonicsDataOnTable_R_Phase() ;
	initialiseHarmonicsDataOnTable_Y_Phase() ;
	initialiseHarmonicsDataOnTable_B_Phase() ;
	
	ApplicationLauncher.logger.debug("initialiseHarmonicsDataOnTable: Exit");
}


//============================New functions added by Pradeep - UnApproved ========================================

	@FXML private void checkBoxPhaseR_OnClick(){
		if( checkBoxPhaseR.isSelected()){
			tab_R_PhaseData.setDisable(false);
			setPhase_R_Selected(true) ;           
		}else{
			tab_R_PhaseData.setDisable(true);
			setPhase_R_Selected(false) ;          
		}
	}

	@FXML private void checkBoxPhaseY_OnClick(){
		if(checkBoxPhaseY.isSelected()){
			tab_Y_PhaseData.setDisable(false);
			setPhase_Y_Selected(true) ;
		}else{
			tab_Y_PhaseData.setDisable(true);
			setPhase_Y_Selected(false) ;

		}
	}

	@FXML private void checkBoxPhaseB_OnClick(){
		if(checkBoxPhaseB.isSelected()){
			tab_B_PhaseData.setDisable(false);
			setPhase_B_Selected(true) ;
		}else{
			tab_B_PhaseData.setDisable(true);
			setPhase_B_Selected(false) ;
		}
	}
	
	@FXML private void tab_R_PhaseDataOnClick(){
		/*if(){
			tab_R_PhaseData.set
		}
		else{
			
		}*/
	}

	@FXML private void tab_Y_PhaseDataOnClick(){
			
		}
	
	@FXML private void tab_B_PhaseDataOnClick(){
		
	}
//=========================
	private void initialiseHarmonicsDataOnTable_R_Phase(){
		
		ApplicationLauncher.logger.debug("initialiseHarmonicsDataOnTable_R_Phase: Entry");
		
		tv_R_PhaseData.getItems().clear();
		
		String selectedPhase = "" ;       
		int serialNum = 1;
		int harmonicsOrder = 2 ;
		
		boolean enableStatus_V = false;
		//int harmonicsOrder_V = 2 ;
		String amplitude_V = "0" ;
		String phaseShift_V = "0" ;
		
		boolean enableStatus_I = false;
		//int harmonicsOrder_I = 2 ;
		String amplitude_I = "0" ;
		String phaseShift_I = "0" ;
		
		for(harmonicsOrder = 2; harmonicsOrder <= ConstantLscsHarmonicsSourceSlave.TOTAL_NO_OF_ORDER_HARMONICS_SUPPORTED ; harmonicsOrder ++){
			
			//	ApplicationLauncher.logger.debug("initialiseHarmonicsDataOnTable: SerialNum        : " + serialNum);
				ApplicationLauncher.logger.debug("initialiseHarmonicsDataOnTable: harmonicsOrder   : "+ harmonicsOrder);

				ApplicationLauncher.logger.debug("initialiseHarmonicsDataOnTable: enableStatus_V  : "+ enableStatus_V);
			//	ApplicationLauncher.logger.debug("initialiseHarmonicsDataOnTable: harmonicsOrder_V : "+ harmonicsOrder_V);
				ApplicationLauncher.logger.debug("initialiseHarmonicsDataOnTable: amplitude_V      : "+ amplitude_V);
				ApplicationLauncher.logger.debug("initialiseHarmonicsDataOnTable: phaseShift_V     : "+ phaseShift_V);
				
				ApplicationLauncher.logger.debug("initialiseHarmonicsDataOnTable: enableStatus_I  : "+ enableStatus_I);
			//	ApplicationLauncher.logger.debug("initialiseHarmonicsDataOnTable: harmonicsOrder_I : "+ harmonicsOrder_I);
				ApplicationLauncher.logger.debug("initialiseHarmonicsDataOnTable: amplitude_I      : "+ amplitude_I);
				ApplicationLauncher.logger.debug("initialiseHarmonicsDataOnTable: phaseShift_I     : "+ phaseShift_I);
				
				
				selectedPhase = ConstantApp.FIRST_PHASE_DISPLAY_NAME;
				HarmonicsDataModel clearHarmonicsData_R_Phase = new HarmonicsDataModel(selectedPhase, harmonicsOrder, 
						                                                  enableStatus_V, amplitude_V, phaseShift_V, 
						                                                  enableStatus_I, amplitude_I, phaseShift_I);
				
				tv_R_PhaseData.getItems().add(clearHarmonicsData_R_Phase);
				
				ref_tv_R_PhaseData = tv_R_PhaseData ;
	}
	
	ApplicationLauncher.logger.debug("initialiseHarmonicsDataOnTable_R_Phase: Entry");
	}
//=====	
	private void initialiseHarmonicsDataOnTable_Y_Phase(){
		ApplicationLauncher.logger.debug("initialiseHarmonicsDataOnTable_Y_Phase: Entry");
		tv_Y_PhaseData.getItems().clear();
		
		String selectedPhase = "" ;       
		int serialNum = 1;
		int harmonicsOrder = 2 ;
		
		boolean enableStatus_V = false;
		//int harmonicsOrder_V = 2 ;
		String amplitude_V = "0" ;
		String phaseShift_V = "0" ;
		
		boolean enableStatus_I = false;
		//int harmonicsOrder_I = 2 ;
		String amplitude_I = "0" ;
		String phaseShift_I = "0" ;
		
		for(harmonicsOrder = 2; harmonicsOrder <= ConstantLscsHarmonicsSourceSlave.TOTAL_NO_OF_ORDER_HARMONICS_SUPPORTED ; harmonicsOrder ++){
			
			//	ApplicationLauncher.logger.debug("initialiseHarmonicsDataOnTable: SerialNum        : " + serialNum);
				ApplicationLauncher.logger.debug("initialiseHarmonicsDataOnTable: harmonicsOrder   : "+ harmonicsOrder);

				ApplicationLauncher.logger.debug("initialiseHarmonicsDataOnTable: enableStatus_V  : "+ enableStatus_V);
			//	ApplicationLauncher.logger.debug("initialiseHarmonicsDataOnTable: harmonicsOrder_V : "+ harmonicsOrder_V);
				ApplicationLauncher.logger.debug("initialiseHarmonicsDataOnTable: amplitude_V      : "+ amplitude_V);
				ApplicationLauncher.logger.debug("initialiseHarmonicsDataOnTable: phaseShift_V     : "+ phaseShift_V);
				
				ApplicationLauncher.logger.debug("initialiseHarmonicsDataOnTable: enableStatus_I  : "+ enableStatus_I);
			//	ApplicationLauncher.logger.debug("initialiseHarmonicsDataOnTable: harmonicsOrder_I : "+ harmonicsOrder_I);
				ApplicationLauncher.logger.debug("initialiseHarmonicsDataOnTable: amplitude_I      : "+ amplitude_I);
				ApplicationLauncher.logger.debug("initialiseHarmonicsDataOnTable: phaseShift_I     : "+ phaseShift_I);

				
				selectedPhase = ConstantApp.SECOND_PHASE_DISPLAY_NAME;
				HarmonicsDataModel clearHarmonicsData_Y_Phase = new HarmonicsDataModel(selectedPhase, harmonicsOrder, 
		                                                                  enableStatus_V, amplitude_V, phaseShift_V, 
		                                                                  enableStatus_I, amplitude_I, phaseShift_I);
				
				tv_Y_PhaseData.getItems().add(clearHarmonicsData_Y_Phase);
				
				ref_tv_Y_PhaseData = tv_Y_PhaseData ;

			}
		ApplicationLauncher.logger.debug("initialiseHarmonicsDataOnTable_Y_Phase: Entry");
	}
//===========	
	private void initialiseHarmonicsDataOnTable_B_Phase(){
		ApplicationLauncher.logger.debug("initialiseHarmonicsDataOnTable_B_Phase: Entry");
		tv_B_PhaseData.getItems().clear();
		
		String selectedPhase = "" ;       
		int serialNum = 1;
		int harmonicsOrder = 2 ;
		
		boolean enableStatus_V = false;
		//int harmonicsOrder_V = 2 ;
		String amplitude_V = "0" ;
		String phaseShift_V = "0" ;
		
		boolean enableStatus_I = false;
		//int harmonicsOrder_I = 2 ;
		String amplitude_I = "0" ;
		String phaseShift_I = "0" ;
		
		
		for(harmonicsOrder = 2; harmonicsOrder <= ConstantLscsHarmonicsSourceSlave.TOTAL_NO_OF_ORDER_HARMONICS_SUPPORTED ; harmonicsOrder ++){
			
			//	ApplicationLauncher.logger.debug("initialiseHarmonicsDataOnTable: SerialNum        : " + serialNum);
				ApplicationLauncher.logger.debug("initialiseHarmonicsDataOnTable: harmonicsOrder   : "+ harmonicsOrder);

				ApplicationLauncher.logger.debug("initialiseHarmonicsDataOnTable: enableStatus_V  : "+ enableStatus_V);
			//	ApplicationLauncher.logger.debug("initialiseHarmonicsDataOnTable: harmonicsOrder_V : "+ harmonicsOrder_V);
				ApplicationLauncher.logger.debug("initialiseHarmonicsDataOnTable: amplitude_V      : "+ amplitude_V);
				ApplicationLauncher.logger.debug("initialiseHarmonicsDataOnTable: phaseShift_V     : "+ phaseShift_V);
				
				ApplicationLauncher.logger.debug("initialiseHarmonicsDataOnTable: enableStatus_I  : "+ enableStatus_I);
			//	ApplicationLauncher.logger.debug("initialiseHarmonicsDataOnTable: harmonicsOrder_I : "+ harmonicsOrder_I);
				ApplicationLauncher.logger.debug("initialiseHarmonicsDataOnTable: amplitude_I      : "+ amplitude_I);
				ApplicationLauncher.logger.debug("initialiseHarmonicsDataOnTable: phaseShift_I     : "+ phaseShift_I);
			
				selectedPhase = ConstantApp.THIRD_PHASE_DISPLAY_NAME;
				HarmonicsDataModel clearHarmonicsData_B_Phase = new HarmonicsDataModel(selectedPhase, harmonicsOrder, 
		                                                                   enableStatus_V, amplitude_V, phaseShift_V, 
		                                                                   enableStatus_I, amplitude_I, phaseShift_I);
				
				tv_B_PhaseData.getItems().add(clearHarmonicsData_B_Phase);
				

				ref_tv_B_PhaseData = tv_B_PhaseData ;

			}	
		ApplicationLauncher.logger.debug("initialiseHarmonicsDataOnTable_B_Phase: Entry");
	}
		

//==============================================================================================================
		private void loadTableProperty() {
		//	colHarmonicsOrder_R_Phase
			
			colSerialNum_R_Phase.setVisible(false);
			colHarmonicsOrder_R_Phase_V.setVisible(false);
			colHarmonicsOrder_R_Phase_I.setVisible(false);
			
			colSerialNum_Y_Phase.setVisible(false);
			colHarmonicsOrder_Y_Phase_V.setVisible(false);
			colHarmonicsOrder_Y_Phase_I.setVisible(false);
			
			colSerialNum_B_Phase.setVisible(false);
			colHarmonicsOrder_B_Phase_V.setVisible(false);
			colHarmonicsOrder_B_Phase_I.setVisible(false);

			tab_R_PhaseData.setDisable(true);
			tab_Y_PhaseData.setDisable(true);
			tab_B_PhaseData.setDisable(true);
			
			tv_R_PhaseData.setEditable(true); 
			tv_Y_PhaseData.setEditable(true); 
			tv_B_PhaseData.setEditable(true); 

//========			
			colSerialNum_R_Phase.setCellValueFactory( cellData -> cellData.getValue().getSerialNumProperty());
			colSerialNum_R_Phase.setStyle( "-fx-alignment: CENTER;");
			
			colHarmonicsOrder_R_Phase.setCellValueFactory( cellData -> cellData.getValue().getHarmonicsOrderProperty());
			colHarmonicsOrder_R_Phase.setStyle( "-fx-alignment: CENTER;");
			
			colEnableStatus_R_Phase_V.setCellValueFactory(new HarmonicsSelectOrder_R_Phase_V_CheckBoxValueFactory());
			colEnableStatus_R_Phase_V.setStyle( "-fx-alignment: CENTER;");
			
			colAmplitude_R_Phase_V.setCellValueFactory( cellData -> cellData.getValue().getAmplitude_V_Property());
			colAmplitude_R_Phase_V.setStyle( "-fx-alignment: CENTER;");
			colAmplitude_R_Phase_V.setCellValueFactory( cellData -> cellData.getValue().getAmplitude_V_Property());
			colAmplitude_R_Phase_V.setCellFactory(TextFieldTableCell.forTableColumn());
			colAmplitude_R_Phase_V.setOnEditCommit(new EventHandler<CellEditEvent<HarmonicsDataModel, String>>() {
				public void handle(CellEditEvent<HarmonicsDataModel, String> t) {
					ApplicationLauncher.logger.info("loadTableProperty:  Entry");
					HarmonicsDataModel rowData = ((HarmonicsDataModel) t.getTableView().getItems().get(t.getTablePosition().getRow()));
					ApplicationLauncher.logger.info("loadTableProperty: t.getNewValue(): "  +t.getNewValue());
					//String meterMake = t.getNewValue();
					if(GuiUtils.isNumber(t.getNewValue())){
						if(  Integer.parseInt(t.getNewValue()) <= 100 && Integer.parseInt(t.getNewValue()) >= 0){
							rowData.setAmplitude_V(t.getNewValue());
						}
						else{
							ApplicationLauncher.InformUser("Invalid input", "Please enter the values between 0 to 100 ", AlertType.INFORMATION);
						}
					}else{
						ApplicationLauncher.InformUser("Invalid input", "Please enter only numbers", AlertType.INFORMATION);
					}
					

					tv_R_PhaseData.refresh();


					//rowData.setSerialno(t.getNewValue());

				}
			}); 
			
			//colPhaseShift_R_Phase_V.setCellValueFactory( cellData -> cellData.getValue().getPhaseShift_V_Property());
			colPhaseShift_R_Phase_V.setStyle( "-fx-alignment: CENTER;");
			colPhaseShift_R_Phase_V.setCellValueFactory( cellData -> cellData.getValue().getPhaseShift_V_Property());
			colPhaseShift_R_Phase_V.setCellFactory(TextFieldTableCell.forTableColumn());
			colPhaseShift_R_Phase_V.setOnEditCommit(new EventHandler<CellEditEvent<HarmonicsDataModel, String>>() {
				public void handle(CellEditEvent<HarmonicsDataModel, String> t) {
					ApplicationLauncher.logger.info("loadTableProperty:  Entry");
					HarmonicsDataModel rowData = ((HarmonicsDataModel) t.getTableView().getItems().get(t.getTablePosition().getRow()));
					ApplicationLauncher.logger.info("loadTableProperty: t.getNewValue(): "  +t.getNewValue());
					//String meterMake = t.getNewValue();
					if(GuiUtils.isNumber(t.getNewValue())){
						if(  Integer.parseInt(t.getNewValue()) <= 180 && Integer.parseInt(t.getNewValue()) >= 0){
							rowData.setPhaseShift_V(t.getNewValue());
						}
						else{
							ApplicationLauncher.InformUser("Invalid input", "Please enter the values between 0 to 180 ", AlertType.INFORMATION);
						}
					}else{
						ApplicationLauncher.InformUser("Invalid input", "Please enter only numbers", AlertType.INFORMATION);
					}
					

					tv_R_PhaseData.refresh();


					//rowData.setSerialno(t.getNewValue());

				}
			}); 
			
		//	colHarmonicsOrder_R_Phase.setCellValueFactory( cellData -> cellData.getValue().getHarmonicsOrderProperty());
		//	colHarmonicsOrder_R_Phase_I.setStyle( "-fx-alignment: CENTER;");
			
			colEnableStatus_R_Phase_I.setCellValueFactory(new HarmonicsSelectOrder_R_Phase_I_CheckBoxValueFactory());
			colEnableStatus_R_Phase_I.setStyle( "-fx-alignment: CENTER;");
			
			
			colAmplitude_R_Phase_I.setCellValueFactory( cellData -> cellData.getValue().getAmplitude_I_Property());
			colAmplitude_R_Phase_I.setStyle( "-fx-alignment: CENTER;");
			colAmplitude_R_Phase_I.setCellValueFactory( cellData -> cellData.getValue().getAmplitude_I_Property());
			colAmplitude_R_Phase_I.setCellFactory(TextFieldTableCell.forTableColumn());
			colAmplitude_R_Phase_I.setOnEditCommit(new EventHandler<CellEditEvent<HarmonicsDataModel, String>>() {
				public void handle(CellEditEvent<HarmonicsDataModel, String> t) {
					ApplicationLauncher.logger.info("loadTableProperty:  Entry");
					HarmonicsDataModel rowData = ((HarmonicsDataModel) t.getTableView().getItems().get(t.getTablePosition().getRow()));
					ApplicationLauncher.logger.info("loadTableProperty: t.getNewValue(): "  +t.getNewValue());
					//String meterMake = t.getNewValue();
					if(GuiUtils.isNumber(t.getNewValue())){
						if(  Integer.parseInt(t.getNewValue()) <= 100 && Integer.parseInt(t.getNewValue()) >= 0){
							rowData.setAmplitude_I(t.getNewValue());
						}
						else{
							ApplicationLauncher.InformUser("Invalid input", "Please enter the values between 0 to 100 ", AlertType.INFORMATION);
						}
					}else{
						ApplicationLauncher.InformUser("Invalid input", "Please enter only numbers", AlertType.INFORMATION);
					}

					tv_R_PhaseData.refresh();


					//rowData.setSerialno(t.getNewValue());

				}
			}); 
			
			colPhaseShift_R_Phase_I.setCellValueFactory( cellData -> cellData.getValue().getPhaseShift_I_Property());
			colPhaseShift_R_Phase_I.setStyle( "-fx-alignment: CENTER;");
			colPhaseShift_R_Phase_I.setCellValueFactory( cellData -> cellData.getValue().getPhaseShift_I_Property());
			colPhaseShift_R_Phase_I.setCellFactory(TextFieldTableCell.forTableColumn());
			colPhaseShift_R_Phase_I.setOnEditCommit(new EventHandler<CellEditEvent<HarmonicsDataModel, String>>() {
				public void handle(CellEditEvent<HarmonicsDataModel, String> t) {
					ApplicationLauncher.logger.info("loadTableProperty:  Entry");
					HarmonicsDataModel rowData = ((HarmonicsDataModel) t.getTableView().getItems().get(t.getTablePosition().getRow()));
					ApplicationLauncher.logger.info("loadTableProperty: t.getNewValue(): "  +t.getNewValue());
					//String meterMake = t.getNewValue();
					if(GuiUtils.isNumber(t.getNewValue())){
						if(  Integer.parseInt(t.getNewValue()) <= 180 && Integer.parseInt(t.getNewValue()) >= 0){
							rowData.setPhaseShift_I(t.getNewValue());
						}
						else{
							ApplicationLauncher.InformUser("Invalid input", "Please enter the values between 0 to 180 ", AlertType.INFORMATION);
						}
					}else{
						ApplicationLauncher.InformUser("Invalid input", "Please enter only numbers", AlertType.INFORMATION);
					}
					

					tv_R_PhaseData.refresh();


					//rowData.setSerialno(t.getNewValue());

				}
			}); 
			
//============
			colSerialNum_Y_Phase.setCellValueFactory( cellData -> cellData.getValue().getSerialNumProperty());
			colSerialNum_Y_Phase.setStyle( "-fx-alignment: CENTER;");
			
			colHarmonicsOrder_Y_Phase.setCellValueFactory( cellData -> cellData.getValue().getHarmonicsOrderProperty());
			colHarmonicsOrder_Y_Phase.setStyle( "-fx-alignment: CENTER;");
			
			colEnableStatus_Y_Phase_V.setCellValueFactory(new HarmonicsSelectOrder_Y_Phase_V_CheckBoxValueFactory());
			colEnableStatus_Y_Phase_V.setStyle( "-fx-alignment: CENTER;");
			
			colAmplitude_Y_Phase_V.setCellValueFactory( cellData -> cellData.getValue().getAmplitude_V_Property());
			colAmplitude_Y_Phase_V.setStyle( "-fx-alignment: CENTER;");
			colAmplitude_Y_Phase_V.setCellValueFactory( cellData -> cellData.getValue().getAmplitude_V_Property());
			colAmplitude_Y_Phase_V.setCellFactory(TextFieldTableCell.forTableColumn());
			colAmplitude_Y_Phase_V.setOnEditCommit(new EventHandler<CellEditEvent<HarmonicsDataModel, String>>() {
				public void handle(CellEditEvent<HarmonicsDataModel, String> t) {
					ApplicationLauncher.logger.info("loadTableProperty:  Entry");
					HarmonicsDataModel rowData = ((HarmonicsDataModel) t.getTableView().getItems().get(t.getTablePosition().getRow()));
					ApplicationLauncher.logger.info("loadTableProperty: t.getNewValue(): "  +t.getNewValue());
					//String meterMake = t.getNewValue();
					if(GuiUtils.isNumber(t.getNewValue())){
						if(  Integer.parseInt(t.getNewValue()) <= 100 && Integer.parseInt(t.getNewValue()) >= 0){
							rowData.setAmplitude_V(t.getNewValue());
						}
						else{
							ApplicationLauncher.InformUser("Invalid input", "Please enter the values between 0 to 100 ", AlertType.INFORMATION);
						}
					}else{
						ApplicationLauncher.InformUser("Invalid input", "Please enter only numbers", AlertType.INFORMATION);
					}
					

					tv_Y_PhaseData.refresh();


					//rowData.setSerialno(t.getNewValue());

				}
			}); 
			
			colPhaseShift_Y_Phase_V.setCellValueFactory( cellData -> cellData.getValue().getPhaseShift_V_Property());
			colPhaseShift_Y_Phase_V.setStyle( "-fx-alignment: CENTER;");
			colPhaseShift_Y_Phase_V.setCellValueFactory( cellData -> cellData.getValue().getPhaseShift_V_Property());
			colPhaseShift_Y_Phase_V.setCellFactory(TextFieldTableCell.forTableColumn());
			colPhaseShift_Y_Phase_V.setOnEditCommit(new EventHandler<CellEditEvent<HarmonicsDataModel, String>>() {
				public void handle(CellEditEvent<HarmonicsDataModel, String> t) {
					ApplicationLauncher.logger.info("loadTableProperty:  Entry");
					HarmonicsDataModel rowData = ((HarmonicsDataModel) t.getTableView().getItems().get(t.getTablePosition().getRow()));
					ApplicationLauncher.logger.info("loadTableProperty: t.getNewValue(): "  +t.getNewValue());
					//String meterMake = t.getNewValue();
					if(GuiUtils.isNumber(t.getNewValue())){
						if(  Integer.parseInt(t.getNewValue()) <= 180 && Integer.parseInt(t.getNewValue()) >= 0){
					 	rowData.setPhaseShift_V(t.getNewValue());
						}
						else{
							ApplicationLauncher.InformUser("Invalid input", "Please enter the values between 0 to 180 ", AlertType.INFORMATION);
						}
					}else{
						ApplicationLauncher.InformUser("Invalid input", "Please enter only numbers", AlertType.INFORMATION);
					}

					tv_Y_PhaseData.refresh();


					//rowData.setSerialno(t.getNewValue());

				}
			}); 
			
		//	colHarmonicsOrder_Y_Phase.setCellValueFactory( cellData -> cellData.getValue().getHarmonicsOrderProperty());
		//	colHarmonicsOrder_Y_Phase.setStyle( "-fx-alignment: CENTER;");
			
			colEnableStatus_Y_Phase_I.setCellValueFactory(new HarmonicsSelectOrder_Y_Phase_I_CheckBoxValueFactory());
			colEnableStatus_Y_Phase_I.setStyle( "-fx-alignment: CENTER;");
			
			
			colAmplitude_Y_Phase_I.setCellValueFactory( cellData -> cellData.getValue().getAmplitude_I_Property());
			colAmplitude_Y_Phase_I.setStyle( "-fx-alignment: CENTER;");
			colAmplitude_Y_Phase_I.setCellValueFactory( cellData -> cellData.getValue().getAmplitude_I_Property());
			colAmplitude_Y_Phase_I.setCellFactory(TextFieldTableCell.forTableColumn());
			colAmplitude_Y_Phase_I.setOnEditCommit(new EventHandler<CellEditEvent<HarmonicsDataModel, String>>() {
				public void handle(CellEditEvent<HarmonicsDataModel, String> t) {
					ApplicationLauncher.logger.info("loadTableProperty:  Entry");
					HarmonicsDataModel rowData = ((HarmonicsDataModel) t.getTableView().getItems().get(t.getTablePosition().getRow()));
					ApplicationLauncher.logger.info("loadTableProperty: t.getNewValue(): "  +t.getNewValue());
					//String meterMake = t.getNewValue();
					if(GuiUtils.isNumber(t.getNewValue())){
						if(  Integer.parseInt(t.getNewValue()) <= 100 && Integer.parseInt(t.getNewValue()) >= 0){
					 	rowData.setAmplitude_I(t.getNewValue());
						}
						else{
							ApplicationLauncher.InformUser("Invalid input", "Please enter the values between 0 to 100 ", AlertType.INFORMATION);
						}
					}else{
						ApplicationLauncher.InformUser("Invalid input", "Please enter only numbers", AlertType.INFORMATION);
					}

					tv_Y_PhaseData.refresh();


					//rowData.setSerialno(t.getNewValue());

				}
			}); 
			
			colPhaseShift_Y_Phase_I.setCellValueFactory( cellData -> cellData.getValue().getPhaseShift_I_Property());
			colPhaseShift_Y_Phase_I.setStyle( "-fx-alignment: CENTER;");
			colPhaseShift_Y_Phase_I.setCellValueFactory( cellData -> cellData.getValue().getPhaseShift_I_Property());
			colPhaseShift_Y_Phase_I.setCellFactory(TextFieldTableCell.forTableColumn());
			colPhaseShift_Y_Phase_I.setOnEditCommit(new EventHandler<CellEditEvent<HarmonicsDataModel, String>>() {
				public void handle(CellEditEvent<HarmonicsDataModel, String> t) {
					ApplicationLauncher.logger.info("loadTableProperty:  Entry");
					HarmonicsDataModel rowData = ((HarmonicsDataModel) t.getTableView().getItems().get(t.getTablePosition().getRow()));
					ApplicationLauncher.logger.info("loadTableProperty: t.getNewValue(): "  +t.getNewValue());
					//String meterMake = t.getNewValue();
					if(GuiUtils.isNumber(t.getNewValue())){
						if(  Integer.parseInt(t.getNewValue()) <= 180 && Integer.parseInt(t.getNewValue()) >= 0){
					 		  rowData.setPhaseShift_I(t.getNewValue());
						}
						else{
							ApplicationLauncher.InformUser("Invalid input", "Please enter the values between 0 to 180 ", AlertType.INFORMATION);
						}
					}else{
						ApplicationLauncher.InformUser("Invalid input", "Please enter only numbers", AlertType.INFORMATION);
					}

					tv_Y_PhaseData.refresh();


					//rowData.setSerialno(t.getNewValue());

				}
			}); 
			
//============
			colSerialNum_B_Phase.setCellValueFactory( cellData -> cellData.getValue().getSerialNumProperty());
			colSerialNum_B_Phase.setStyle( "-fx-alignment: CENTER;");
			
			colHarmonicsOrder_B_Phase.setCellValueFactory( cellData -> cellData.getValue().getHarmonicsOrderProperty());
			colHarmonicsOrder_B_Phase.setStyle( "-fx-alignment: CENTER;");
			
			colEnableStatus_B_Phase_V.setCellValueFactory(new HarmonicsSelectOrder_B_Phase_V_CheckBoxValueFactory());
			colEnableStatus_B_Phase_V.setStyle( "-fx-alignment: CENTER;");
			
			colAmplitude_B_Phase_V.setCellValueFactory( cellData -> cellData.getValue().getAmplitude_V_Property());
			colAmplitude_B_Phase_V.setStyle( "-fx-alignment: CENTER;");
			colAmplitude_B_Phase_V.setCellValueFactory( cellData -> cellData.getValue().getAmplitude_V_Property());
			colAmplitude_B_Phase_V.setCellFactory(TextFieldTableCell.forTableColumn());
			colAmplitude_B_Phase_V.setOnEditCommit(new EventHandler<CellEditEvent<HarmonicsDataModel, String>>() {
				public void handle(CellEditEvent<HarmonicsDataModel, String> t) {
					ApplicationLauncher.logger.info("loadTableProperty:  Entry");
					HarmonicsDataModel rowData = ((HarmonicsDataModel) t.getTableView().getItems().get(t.getTablePosition().getRow()));
					ApplicationLauncher.logger.info("loadTableProperty: t.getNewValue(): "  +t.getNewValue());
					//String meterMake = t.getNewValue();
					if(GuiUtils.isNumber(t.getNewValue())){
						if(  Integer.parseInt(t.getNewValue()) <= 100 && Integer.parseInt(t.getNewValue()) >= 0){
					 		rowData.setAmplitude_V(t.getNewValue());
						}
						else{
							ApplicationLauncher.InformUser("Invalid input", "Please enter the values between 0 to 100 ", AlertType.INFORMATION);
						}
					}else{
						ApplicationLauncher.InformUser("Invalid input", "Please enter only numbers", AlertType.INFORMATION);
					}

					tv_B_PhaseData.refresh();


					//rowData.setSerialno(t.getNewValue());

				}
			}); 
			
			colPhaseShift_B_Phase_V.setCellValueFactory( cellData -> cellData.getValue().getPhaseShift_V_Property());
			colPhaseShift_B_Phase_V.setStyle( "-fx-alignment: CENTER;");
			colPhaseShift_B_Phase_V.setCellValueFactory( cellData -> cellData.getValue().getPhaseShift_V_Property());
			colPhaseShift_B_Phase_V.setCellFactory(TextFieldTableCell.forTableColumn());
			colPhaseShift_B_Phase_V.setOnEditCommit(new EventHandler<CellEditEvent<HarmonicsDataModel, String>>() {
				public void handle(CellEditEvent<HarmonicsDataModel, String> t) {
					ApplicationLauncher.logger.info("loadTableProperty:  Entry");
					HarmonicsDataModel rowData = ((HarmonicsDataModel) t.getTableView().getItems().get(t.getTablePosition().getRow()));
					ApplicationLauncher.logger.info("loadTableProperty: t.getNewValue(): "  +t.getNewValue());
					//String meterMake = t.getNewValue();
					if(GuiUtils.isNumber(t.getNewValue())){
						if(  Integer.parseInt(t.getNewValue()) <= 180 && Integer.parseInt(t.getNewValue()) >= 0){
				    	rowData.setPhaseShift_V(t.getNewValue());
						}
						else{
							ApplicationLauncher.InformUser("Invalid input", "Please enter the values between 0 to 180 ", AlertType.INFORMATION);
						}
					}else{
						ApplicationLauncher.InformUser("Invalid input", "Please enter only numbers", AlertType.INFORMATION);
					}

					tv_B_PhaseData.refresh();


					//rowData.setSerialno(t.getNewValue());

				}
			}); 
			
			colHarmonicsOrder_B_Phase.setCellValueFactory( cellData -> cellData.getValue().getHarmonicsOrderProperty());
			colHarmonicsOrder_B_Phase.setStyle( "-fx-alignment: CENTER;");
			
			colEnableStatus_B_Phase_I.setCellValueFactory(new HarmonicsSelectOrder_B_Phase_I_CheckBoxValueFactory());
			colEnableStatus_B_Phase_I.setStyle( "-fx-alignment: CENTER;");
			
			
			colAmplitude_B_Phase_I.setCellValueFactory( cellData -> cellData.getValue().getAmplitude_I_Property());
			colAmplitude_B_Phase_I.setStyle( "-fx-alignment: CENTER;");
			colAmplitude_B_Phase_I.setCellValueFactory( cellData -> cellData.getValue().getAmplitude_I_Property());
			colAmplitude_B_Phase_I.setCellFactory(TextFieldTableCell.forTableColumn());
			colAmplitude_B_Phase_I.setOnEditCommit(new EventHandler<CellEditEvent<HarmonicsDataModel, String>>() {
				public void handle(CellEditEvent<HarmonicsDataModel, String> t) {
					ApplicationLauncher.logger.info("loadTableProperty:  Entry");
					HarmonicsDataModel rowData = ((HarmonicsDataModel) t.getTableView().getItems().get(t.getTablePosition().getRow()));
					ApplicationLauncher.logger.info("loadTableProperty: t.getNewValue(): "  +t.getNewValue());
					//String meterMake = t.getNewValue();
					if(GuiUtils.isNumber(t.getNewValue())){
						if(  Integer.parseInt(t.getNewValue()) <= 100 && Integer.parseInt(t.getNewValue()) >= 0){
					 	rowData.setAmplitude_I(t.getNewValue());
						}
						else{
							ApplicationLauncher.InformUser("Invalid input", "Please enter the values between 0 to 100 ", AlertType.INFORMATION);
						}
					}else{
						ApplicationLauncher.InformUser("Invalid input", "Please enter only numbers", AlertType.INFORMATION);
					}

					tv_B_PhaseData.refresh();


					//rowData.setSerialno(t.getNewValue());

				}
			}); 
			
			colPhaseShift_B_Phase_I.setCellValueFactory( cellData -> cellData.getValue().getPhaseShift_I_Property());
			colPhaseShift_B_Phase_I.setStyle( "-fx-alignment: CENTER;");
			colPhaseShift_B_Phase_I.setCellValueFactory( cellData -> cellData.getValue().getPhaseShift_I_Property());
			colPhaseShift_B_Phase_I.setCellFactory(TextFieldTableCell.forTableColumn());
			colPhaseShift_B_Phase_I.setOnEditCommit(new EventHandler<CellEditEvent<HarmonicsDataModel, String>>() {
				public void handle(CellEditEvent<HarmonicsDataModel, String> t) {
					ApplicationLauncher.logger.info("loadTableProperty:  Entry");
					HarmonicsDataModel rowData = ((HarmonicsDataModel) t.getTableView().getItems().get(t.getTablePosition().getRow()));
					ApplicationLauncher.logger.info("loadTableProperty: t.getNewValue(): "  +t.getNewValue());
					//String meterMake = t.getNewValue();
					if(GuiUtils.isNumber(t.getNewValue())){
						if(  Integer.parseInt(t.getNewValue()) <= 180 && Integer.parseInt(t.getNewValue()) >= 0){
					  	rowData.setPhaseShift_I(t.getNewValue());
						}
						else{
							ApplicationLauncher.InformUser("Invalid input", "Please enter the values between 0 to 180 ", AlertType.INFORMATION);
						}
					}else{
						ApplicationLauncher.InformUser("Invalid input", "Please enter only numbers", AlertType.INFORMATION);
					}

					tv_B_PhaseData.refresh();


					//rowData.setSerialno(t.getNewValue());

				}
			}); 
//==============					
			
		}

//================================================================================================//
	
		public void SaveHarmonicDataTrigger(){
			ApplicationLauncher.logger.info("SaveHarmonicDataTriggerV2 Invoked:");
			SaveDataTimer = new Timer();
			SaveDataTimer.schedule(new SaveHarmonicDataTask(),100);

		}
		
		
		public boolean IsFieldEmpty(){
			if(ref_checkbox_inphase.isSelected() || ref_checkbox_outphase.isSelected()){
				/*if (!ref_cmbBox_harmonic1.getSelectionModel().getSelectedItem().equals("Select")){
					return false;
				}else{
					ApplicationLauncher.logger.info ("IsFieldEmpty: Kindly select the Harmonic Component: Prompt:");
					//Platform.runLater(() -> {
					ApplicationLauncher.InformUser("Empty Field", "Kindly select the Harmonic Component",AlertType.ERROR);
					//});
					return true;

				}*/	
				
				return false;
			}else{
				ApplicationLauncher.logger.info ("IsFieldEmpty :  Neithor inphase nor out phase check box selected");
				//Platform.runLater(() -> {

				ApplicationLauncher.InformUser("Not Saved","Please select <In Phase> or <Out Phase> check box",AlertType.ERROR);
				//});
				return true;
			}


		}

//=============================================================================================================//		
		class SaveHarmonicDataTask extends TimerTask {
			public void run() {

				ApplicationLauncher.logger.info ("SaveHarmonicDataTask V2 : Entry");
				
				if (!IsFieldEmpty()){
					
					
				
	
					//=========================================================
					if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED ){
						ApplicationLauncher.logger.info ("SaveHarmonicDataTask: R Phase Selected : " +  isPhase_R_Selected());
						if(isPhase_R_Selected()){
							
							HarmonicsDataModel harmonicsData_R_Phase = new HarmonicsDataModel();
	
							ArrayList<HarmonicsDataModel> harmonicsOrdersData_R_phaseList = (ArrayList<HarmonicsDataModel>) tv_R_PhaseData.getItems().stream()
									.filter(e -> ( (e.isHarmonicOrder_V_Selected() == true) || (e.isHarmonicOrder_I_Selected() == true)  ) )
									.collect(Collectors.toList());
	
							ApplicationLauncher.logger.debug( "SaveHarmonicDataTask: harmonicsOrdersData_R_phaseList Size : " +  harmonicsOrdersData_R_phaseList.size());
	
							if(harmonicsOrdersData_R_phaseList.size() > 0){
								ApplicationLauncher.logger.debug("SaveHarmonicDataTask: Size " + harmonicsOrdersData_R_phaseList.size());
								
								for(int i = 0 ; i< harmonicsOrdersData_R_phaseList.size(); i++){
									harmonicsData_R_Phase = harmonicsOrdersData_R_phaseList.get(i);
									ApplicationLauncher.logger.debug ("SaveHarmonicDataTask: harmonicsData_R_Phase.isHarmonicOrder_I_Selected() " + harmonicsData_R_Phase.isHarmonicOrder_I_Selected());
									if(harmonicsData_R_Phase.isHarmonicOrder_I_Selected()){
										ApplicationLauncher.logger.debug ("SaveHarmonicDataTask: Test 03 ");
										harmonicsData_R_Phase.setHarmonicOrder_I_Selected(false);		
										ApplicationLauncher.logger.debug ("SaveHarmonicDataTask: Test 04 ");
									}
								}
								
								for(int i = 0 ; i< harmonicsOrdersData_R_phaseList.size();i++){ 
									harmonicsData_R_Phase = harmonicsOrdersData_R_phaseList.get(i);							
									if(harmonicsData_R_Phase.isHarmonicOrder_V_Selected()){
										ApplicationLauncher.logger.debug ("SaveHarmonicDataTask: Test 01 ");
										harmonicsData_R_Phase.setHarmonicOrder_I_Selected(true);		
										ApplicationLauncher.logger.debug ("SaveHarmonicDataTask: Test 02 ");
									}
									
								}
							
							}
						}
					}
	
					//=====================================================		
					if (validation()){
	
						ApplicationLauncher.setCursor(Cursor.WAIT);	
						mCurrentNode.NewNode(false);
	
						// ======== R Phase ==================		
						if(isPhase_R_Selected()){
							if (ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED) {
								ArrayList<HarmonicsDataModel> selectedHarmonicOrdersList_R_Phase = new ArrayList<HarmonicsDataModel>();
	
								selectedHarmonicOrdersList_R_Phase = (ArrayList<HarmonicsDataModel>) tv_R_PhaseData.getItems().stream()
										.filter(e -> ( (e.isHarmonicOrder_V_Selected() == true) ))
										.collect( Collectors.toList());					  			
	
								setUserSelectedHarmonicOrdersList_R_Phase(selectedHarmonicOrdersList_R_Phase); 	
							}
							else{
							ArrayList<HarmonicsDataModel> selectedHarmonicOrdersList_R_Phase = new ArrayList<HarmonicsDataModel>();
	
							selectedHarmonicOrdersList_R_Phase = (ArrayList<HarmonicsDataModel>) tv_R_PhaseData.getItems().stream()
									.filter(e -> ( (e.isHarmonicOrder_V_Selected() == true) || (e.isHarmonicOrder_I_Selected() == true)))
									.collect( Collectors.toList());					  			
	
							setUserSelectedHarmonicOrdersList_R_Phase(selectedHarmonicOrdersList_R_Phase); 	
							}
						}
						else{
							initialiseHarmonicsDataOnTable_R_Phase() ;      // clearing all data in the table
						}
	
						// ======== Y Phase ==================	
						if(isPhase_Y_Selected()){
							ArrayList<HarmonicsDataModel> selectedHarmonicOrdersList_Y_Phase = new ArrayList<HarmonicsDataModel>();
	
							selectedHarmonicOrdersList_Y_Phase = (ArrayList<HarmonicsDataModel>) tv_Y_PhaseData.getItems().stream()
									.filter(e -> ( (e.isHarmonicOrder_V_Selected() == true) || (e.isHarmonicOrder_I_Selected() == true) ) )
									.collect( Collectors.toList());
	
							setUserSelectedHarmonicOrdersList_Y_Phase(selectedHarmonicOrdersList_Y_Phase); 
						}else{
							initialiseHarmonicsDataOnTable_Y_Phase() ;   	// clearing all data in the table
						}
						// ======== Y Phase ==================		
						if(isPhase_B_Selected()){
							ArrayList<HarmonicsDataModel> selectedHarmonicOrdersList_B_Phase = new ArrayList<HarmonicsDataModel>();
	
							selectedHarmonicOrdersList_B_Phase = (ArrayList<HarmonicsDataModel>) tv_B_PhaseData.getItems().stream()
									.filter(e -> ( (e.isHarmonicOrder_V_Selected() == true) || (e.isHarmonicOrder_I_Selected() == true) ) )
									.collect( Collectors.toList());
	
							setUserSelectedHarmonicOrdersList_B_Phase(selectedHarmonicOrdersList_B_Phase); 
						}else{
							initialiseHarmonicsDataOnTable_B_Phase() ;      // clearing all data in the table
						}
						//=========================================		
	
						/*
						String phase_selected = "0";
						String harmonic_order = "0";
						String harmonic_volt = "0";
						String harmonic_volt_phase = "0";
						String harmonic_current = "0";
						String harmonic_current_phase = "0";
						 */
						boolean status = false;
						PropertyInfluenceController.delete_previous_saved_data();
	
						status = SaveHarmonics();
						if(status){
							status = SaveToDB();
							if(status){
								ApplicationLauncher.logger.info ("SaveHarmonicDataTask: Successfully data saved in database ");
								ApplicationLauncher.InformUser("Success", "Successfully Data saved in database", AlertType.INFORMATION);
							}
						}else {
							ApplicationLauncher.logger.info ("SaveHarmonicDataTask: Data not saved in database");
							ApplicationLauncher.InformUser("Failed to Save", "Data not saved in database", AlertType.ERROR);
						}
	
	
	
						//project_name, test_case_name, test_type, test_alias_id, 
						//	SaveHarmonics(phase_selected, harmonic_order, harmonic_volt, harmonic_volt_phase, harmonic_current, harmonic_current_phase);
	
	
						ProjectController.SaveProject();
						ProjectController.LoadSummaryDataToGUI();
	
						ApplicationLauncher.setCursor(Cursor.DEFAULT);
					}
				
				}
				SaveDataTimer.cancel();


				//========================		

			}
		}
//===============================================================================================================================================		
		ArrayList<HarmonicsDataModel> UserSelectedHarmonicOrdersList_R_Phase = new ArrayList<HarmonicsDataModel>();
		ArrayList<HarmonicsDataModel> UserSelectedHarmonicOrdersList_Y_Phase = new ArrayList<HarmonicsDataModel>();
		ArrayList<HarmonicsDataModel> UserSelectedHarmonicOrdersList_B_Phase = new ArrayList<HarmonicsDataModel>();

		
		public ArrayList<HarmonicsDataModel> getUserSelectedHarmonicOrdersList_R_Phase() {
			return UserSelectedHarmonicOrdersList_R_Phase;
		}

		public void setUserSelectedHarmonicOrdersList_R_Phase(ArrayList<HarmonicsDataModel> UserSelectedHarmonicOrdersList_R_Phase) {
			this.UserSelectedHarmonicOrdersList_R_Phase = UserSelectedHarmonicOrdersList_R_Phase;
		}	
		
		public ArrayList<HarmonicsDataModel> getUserSelectedHarmonicOrdersList_Y_Phase() {
			return UserSelectedHarmonicOrdersList_Y_Phase;
		}

		public void setUserSelectedHarmonicOrdersList_Y_Phase(ArrayList<HarmonicsDataModel> UserSelectedHarmonicOrdersList_Y_Phase) {
			this.UserSelectedHarmonicOrdersList_Y_Phase = UserSelectedHarmonicOrdersList_Y_Phase;
		}	
		
		public ArrayList<HarmonicsDataModel> getUserSelectedHarmonicOrdersList_B_Phase() {
			return UserSelectedHarmonicOrdersList_B_Phase;
		}

		public void setUserSelectedHarmonicOrdersList_B_Phase(ArrayList<HarmonicsDataModel> UserSelectedHarmonicOrdersList_B_Phase) {
			this.UserSelectedHarmonicOrdersList_B_Phase = UserSelectedHarmonicOrdersList_B_Phase;
		}	
//============================================================================================================================================		
		public boolean validation(){
			
			boolean validation = false ;

//================			
			if(ProjectController.getModelType(mCurrentNode.getProjectName()).startsWith(ConstantApp.METERTYPE_THREEPHASE)){  
			
				if( (isPhase_R_Selected() == true) || 
					(isPhase_Y_Selected() == true) || 
					(isPhase_B_Selected() == true) ){
					validation = true ;
				}
				else {
					validation = false;
					ApplicationLauncher.InformUser("No phase selected", "Atleast one phase should be selected", AlertType.ERROR);
					return validation ;
				}
			}
			else if(ProjectController.getModelType(mCurrentNode.getProjectName()).startsWith(ConstantApp.METERTYPE_SINGLEPHASE)){  
				if(isPhase_R_Selected()){ 
					validation = true ;
				}
				else {
					validation = false ;
					ApplicationLauncher.InformUser("No phase selected", "Phase CheckBox should be selected", AlertType.ERROR);
					return validation ;
				}
			}
//====================			
				
			String localAmplitude_V = "" ;
			String localAmplitude_I = "" ;
			String localPhaseDifference_V = "" ;
			String localPhaseDifference_I = "" ;
			int totalHarmonicsVoltageContent = 0;
			int totalHarmonicsCurrentContent = 0;
//====			
			if(isPhase_R_Selected()){
				
			ArrayList<HarmonicsDataModel> selectedHarmonicOrdersList_R_Phase = new ArrayList<HarmonicsDataModel>();
			
			selectedHarmonicOrdersList_R_Phase = (ArrayList<HarmonicsDataModel>) tv_R_PhaseData.getItems().stream()
					.filter(e -> ( (e.isHarmonicOrder_V_Selected() == true) || (e.isHarmonicOrder_I_Selected() == true) ) )
					.collect( Collectors.toList());

			
//======	ATLEAST ONE ORDER SHOULD BE SELECTED
			
		//	if(isPhase_R_Selected()){
				if(selectedHarmonicOrdersList_R_Phase.size()>0){
					validation = true ;
				}
				else {
					validation = false ;
					ApplicationLauncher.InformUser("No order selected", "Atleast one order should be selected", AlertType.ERROR);
					return validation ;
				}
		//	}
//======	MORE THAN ALLOWED ORDERS SELECTED	
	
		if(selectedHarmonicOrdersList_R_Phase.size()<= ConstantLscsHarmonicsSourceSlave.NUM_OF_ORDERS_ALLOWED_IN_ONE_TEST_CASE){
			validation = true ;
		}
		else {
			validation = false ;
			ApplicationLauncher.InformUser("More than allowed orders selected", 
					                       "Maximum allowed number of orders is "+ ConstantLscsHarmonicsSourceSlave.NUM_OF_ORDERS_ALLOWED_IN_ONE_TEST_CASE, 
					                       AlertType.ERROR);
			return validation ;
		}		
						
//======  AMPLITUDE IS ZERO IN SELECTED ORDER
			
			HarmonicsDataModel harmonicsData_R_Phase_ = new HarmonicsDataModel();
			
			if(selectedHarmonicOrdersList_R_Phase.size()>0){
				boolean voltage_validation = false ;
				boolean current_validation = false;
				
			for(int i = 0 ; i< selectedHarmonicOrdersList_R_Phase.size();i++){ 
				harmonicsData_R_Phase_ = selectedHarmonicOrdersList_R_Phase.get(i);
				 localAmplitude_V = harmonicsData_R_Phase_.getAmplitude_V() ;
				 ApplicationLauncher.logger.debug("validation: localAmplitude_V: " + localAmplitude_V);	
 
				 localAmplitude_I = harmonicsData_R_Phase_.getAmplitude_I() ;
				 ApplicationLauncher.logger.debug("validation: localAmplitude_I: " + localAmplitude_I);	
				 
				 
				if(harmonicsData_R_Phase_.isHarmonicOrder_V_Selected()){

					totalHarmonicsVoltageContent = totalHarmonicsVoltageContent + Integer.parseInt(localAmplitude_V);

					if((Integer.parseInt(localAmplitude_V) > 0)){
						voltage_validation = true ;
					}
					else {
						voltage_validation = false ;
						//ApplicationLauncher.InformUser("Error with R Phase V Amp of Harm order " + harmonicsData_R_Phase_.getHarmonicsOrder(), "Amplitude percentage should be greater than zero", AlertType.INFORMATION);
						//return validation;
					}
				}

				if(harmonicsData_R_Phase_.isHarmonicOrder_I_Selected()){
					
					 ApplicationLauncher.logger.debug("Test11"); 

					totalHarmonicsCurrentContent = totalHarmonicsCurrentContent + Integer.parseInt(localAmplitude_I);

					if((Integer.parseInt(localAmplitude_I) > 0)){
						current_validation = true ;
					}
					else {
						current_validation = false ;
						//ApplicationLauncher.InformUser("Error with R Phase I Amp of Harm order " + harmonicsData_R_Phase_.getHarmonicsOrder(), "Amplitude percentage should be greater than zero", AlertType.INFORMATION);
						//return validation;
					}
				}	
 
				
				 if (voltage_validation || current_validation) {
					 validation = true ;
				 }
				 else{
					 validation = false ;
					 if (!voltage_validation) {
						 ApplicationLauncher.InformUser("Error with R Phase V Amp of Harm order " + harmonicsData_R_Phase_.getHarmonicsOrder(), "Amplitude percentage should be greater than zero", AlertType.INFORMATION);
						 return validation;
					 } else if(!current_validation) {
						 ApplicationLauncher.InformUser("Error with R Phase I Amp of Harm order " + harmonicsData_R_Phase_.getHarmonicsOrder(), "Amplitude percentage should be greater than zero", AlertType.INFORMATION);
						 return validation;
					 }
				 }
				
				
			} // for loop end
			
			 ApplicationLauncher.logger.debug("Voltage Validation : " + voltage_validation); 
			 ApplicationLauncher.logger.debug("Current Validation : " + current_validation);
			 
			
			ApplicationLauncher.logger.debug("Total harmonics voltage content : " + totalHarmonicsVoltageContent); 
			ApplicationLauncher.logger.debug("Total harmonics current content : " + totalHarmonicsCurrentContent); 
	
			//===========================		
			// MORE THAN ALLOWED VOLTAGE CONTENT 		
			if(totalHarmonicsVoltageContent <= ConstantLscsHarmonicsSourceSlave.BOFA_MAXIMUM_ALLOWED_HARM_VOLTAGE_CONTENT){  
				validation = true ;
			}
			else{
				validation = false ;
				ApplicationLauncher.InformUser("More than allowed voltage content", 
						"Maximum allowed voltage content is "+ ConstantLscsHarmonicsSourceSlave.BOFA_MAXIMUM_ALLOWED_HARM_VOLTAGE_CONTENT, 
						AlertType.ERROR);
				return validation ;
			}
			
			
			// MORE THAN ALLOWED CURRENT CONTENT 
			if(totalHarmonicsCurrentContent <= ConstantLscsHarmonicsSourceSlave.BOFA_MAXIMUM_ALLOWED_HARM_CURRENT_CONTENT){
				validation = true ;
			}
			else{
				validation = false ;
				ApplicationLauncher.InformUser("More than allowed current content", 
						"Maximum allowed current content is "+ ConstantLscsHarmonicsSourceSlave.BOFA_MAXIMUM_ALLOWED_HARM_CURRENT_CONTENT, 
						AlertType.ERROR);
				return validation ;
			}	
			
			}
			}
//=======
			
			if(isPhase_Y_Selected()){
				

			ArrayList<HarmonicsDataModel> selectedHarmonicOrdersList_Y_Phase = new ArrayList<HarmonicsDataModel>();

			selectedHarmonicOrdersList_Y_Phase = (ArrayList<HarmonicsDataModel>) tv_Y_PhaseData.getItems().stream()
					.filter(e -> ( (e.isHarmonicOrder_V_Selected() == true) || (e.isHarmonicOrder_I_Selected() == true) ) )
					.collect( Collectors.toList());
			
//======	ATLEAST ONE ORDER SHOULD BE SELECTED		
			
		//	if(isPhase_Y_Selected()){
			if(selectedHarmonicOrdersList_Y_Phase.size()>0){
				validation = true ;
			}
			else {
				validation = false ;
				ApplicationLauncher.InformUser("No order selected", "Atleast one order should be selected", AlertType.ERROR);
				return validation ;
			}
		//	}
			
			
//======	MORE THAN ALLOWED ORDERS SELECTED	

			if(selectedHarmonicOrdersList_Y_Phase.size()<= ConstantLscsHarmonicsSourceSlave.NUM_OF_ORDERS_ALLOWED_IN_ONE_TEST_CASE){
				validation = true ;
			}
			else {
				validation = false ;
				ApplicationLauncher.InformUser("More than allowed orders selected", 
						                       "Maximum allowed number of orders is "+ ConstantLscsHarmonicsSourceSlave.NUM_OF_ORDERS_ALLOWED_IN_ONE_TEST_CASE, 
						                        AlertType.ERROR);
				return validation ;
			}


//======  AMPLITUDE IS ZERO IN SELECTED ORDER
			HarmonicsDataModel harmonicsData_Y_Phase_ = new HarmonicsDataModel();
			if(selectedHarmonicOrdersList_Y_Phase.size()>0){
			for(int i = 0 ; i< selectedHarmonicOrdersList_Y_Phase.size();i++){ 
				harmonicsData_Y_Phase_ = selectedHarmonicOrdersList_Y_Phase.get(i);
				 localAmplitude_V = harmonicsData_Y_Phase_.getAmplitude_V() ;
				 localAmplitude_I = harmonicsData_Y_Phase_.getAmplitude_I() ;
				 if(harmonicsData_Y_Phase_.isHarmonicOrder_V_Selected()){
					 
					 totalHarmonicsVoltageContent = totalHarmonicsVoltageContent + Integer.parseInt(localAmplitude_V);
					 
						if((Integer.parseInt(localAmplitude_V) > 0)){
							validation = true ;
						}
						else {
							validation = false ;
							ApplicationLauncher.InformUser("Error with Y Phase V Amp of Harm order " + harmonicsData_Y_Phase_.getHarmonicsOrder(), "Amplitude percentage should be greater than zero", AlertType.INFORMATION);
						}
					}
				 if(harmonicsData_Y_Phase_.isHarmonicOrder_I_Selected()){
					 
					 totalHarmonicsCurrentContent = totalHarmonicsCurrentContent + Integer.parseInt(localAmplitude_I);
					 
						if((Integer.parseInt(localAmplitude_I) > 0)){
							validation = true ;
						}
						else {
							validation = false ;
							ApplicationLauncher.InformUser("Error with Y Phase I Amp of Harm order " + harmonicsData_Y_Phase_.getHarmonicsOrder(), "Amplitude percentage should be greater than zero", AlertType.INFORMATION);
							return validation;
						}
					}
				 
				 
					// MORE THAN ALLOWED VOLTAGE CONTENT 		
					if(totalHarmonicsVoltageContent < ConstantLscsHarmonicsSourceSlave.BOFA_MAXIMUM_ALLOWED_HARM_VOLTAGE_CONTENT){
						validation = true ;
					}
					else{
						validation = false ;
						ApplicationLauncher.InformUser("More than allowed voltage content", 
								"Maximum allowed voltage content is "+ ConstantLscsHarmonicsSourceSlave.BOFA_MAXIMUM_ALLOWED_HARM_VOLTAGE_CONTENT, 
								AlertType.ERROR);
						return validation ;
					}
					
					
					// MORE THAN ALLOWED CURRENT CONTENT 
					if(totalHarmonicsCurrentContent < ConstantLscsHarmonicsSourceSlave.BOFA_MAXIMUM_ALLOWED_HARM_CURRENT_CONTENT){
						validation = true ;
					}
					else{
						validation = false ;
						ApplicationLauncher.InformUser("More than allowed current content", 
								"Maximum allowed current content is "+ ConstantLscsHarmonicsSourceSlave.BOFA_MAXIMUM_ALLOWED_HARM_CURRENT_CONTENT, 
								AlertType.ERROR);
						return validation ;
					}  
			}
			}
			}
//===========	
			if(isPhase_B_Selected()){
			ArrayList<HarmonicsDataModel> selectedHarmonicOrdersList_B_Phase = new ArrayList<HarmonicsDataModel>();

			selectedHarmonicOrdersList_B_Phase = (ArrayList<HarmonicsDataModel>) tv_B_PhaseData.getItems().stream()
					.filter(e -> ( (e.isHarmonicOrder_V_Selected() == true) || (e.isHarmonicOrder_I_Selected() == true) ) )
					.collect( Collectors.toList());

//======	ATLEAST ONE ORDER SHOULD BE SELECTED	
			
		//	if(isPhase_B_Selected()){
			if(selectedHarmonicOrdersList_B_Phase.size()>0){
				validation = true ;
			}
			else {
				validation = false ;
				ApplicationLauncher.InformUser("No order selected", "Atleast one order should be selected", AlertType.ERROR);
				return validation ;
			}
		//	}
			
//	======	MORE THAN ALLOWED ORDERS SELECTED	

			if(selectedHarmonicOrdersList_B_Phase.size()<= ConstantLscsHarmonicsSourceSlave.NUM_OF_ORDERS_ALLOWED_IN_ONE_TEST_CASE){
				validation = true ;
			}
			else {
				validation = false ;
				ApplicationLauncher.InformUser("More than allowed orders selected", 
						"Maximum allowed number of orders is "+ ConstantLscsHarmonicsSourceSlave.NUM_OF_ORDERS_ALLOWED_IN_ONE_TEST_CASE, 
						AlertType.ERROR);
				return validation ;
			}


//======  AMPLITUDE IS ZERO IN SELECTED ORDER
			
			HarmonicsDataModel harmonicsData_B_Phase_ = new HarmonicsDataModel();
			if(selectedHarmonicOrdersList_B_Phase.size()>0){
			for(int i = 0 ; i< selectedHarmonicOrdersList_B_Phase.size();i++){ 
				harmonicsData_B_Phase_ = selectedHarmonicOrdersList_B_Phase.get(i);
				 localAmplitude_V = harmonicsData_B_Phase_.getAmplitude_V() ;
				 localAmplitude_I = harmonicsData_B_Phase_.getAmplitude_I() ;
				 if(harmonicsData_B_Phase_.isHarmonicOrder_V_Selected()){
					 
					 totalHarmonicsVoltageContent = totalHarmonicsVoltageContent + Integer.parseInt(localAmplitude_V);
					 
						if((Integer.parseInt(localAmplitude_V) > 0)){
							validation = true ;
						}
						else {
							validation = false ;
							ApplicationLauncher.InformUser("Error with B Phase V Amp of Harm order " + harmonicsData_B_Phase_.getHarmonicsOrder(), "Amplitude percentage should be greater than zero", AlertType.INFORMATION);
							return validation;
						}
					}
				 if(harmonicsData_B_Phase_.isHarmonicOrder_I_Selected()){
					 
					 totalHarmonicsCurrentContent = totalHarmonicsCurrentContent + Integer.parseInt(localAmplitude_I);
					 
						if((Integer.parseInt(localAmplitude_I) > 0)){
							validation = true ;
						}
						else {
							validation = false ;
							ApplicationLauncher.InformUser("Error with B Phase I Amp of Harm order " + harmonicsData_B_Phase_.getHarmonicsOrder(), "Amplitude percentage should be greater than zero", AlertType.INFORMATION);
							return validation;
						}
					}
			}
			
			// MORE THAN ALLOWED VOLTAGE CONTENT 		
			if(totalHarmonicsVoltageContent < ConstantLscsHarmonicsSourceSlave.BOFA_MAXIMUM_ALLOWED_HARM_VOLTAGE_CONTENT){
				validation = true ;
			}
			else{
				validation = false ;
				ApplicationLauncher.InformUser("More than allowed voltage content", 
						"Maximum allowed voltage content is "+ ConstantLscsHarmonicsSourceSlave.BOFA_MAXIMUM_ALLOWED_HARM_VOLTAGE_CONTENT, 
						AlertType.ERROR);
				return validation ;
			}
			
			
			// MORE THAN ALLOWED CURRENT CONTENT 
			if(totalHarmonicsCurrentContent < ConstantLscsHarmonicsSourceSlave.BOFA_MAXIMUM_ALLOWED_HARM_CURRENT_CONTENT){
				validation = true ;
			}
			else{
				validation = false ;
				ApplicationLauncher.InformUser("More than allowed current content", 
						"Maximum allowed current content is "+ ConstantLscsHarmonicsSourceSlave.BOFA_MAXIMUM_ALLOWED_HARM_CURRENT_CONTENT, 
						AlertType.ERROR);
				return validation ;
			}
			
			}
			}
//===========			
			ApplicationLauncher.logger.debug("validation: validation: " + validation);		
			return validation;

}
	
//==========================================================================================================================================		
		
		public boolean SaveToDB(){
			
			boolean status  = false ;
			List<String> columnData = mCurrentNode.getTestCaseNames();
			List<String> EminData = mCurrentNode.getEmin_list();
			List<String> EmaxData = mCurrentNode.getEmax_list();
			List<String> PulsesData = mCurrentNode.getPulses_list();
			List<String> AverageData = mCurrentNode.getAverage_list();
			List<String> TimeData = mCurrentNode.getTime_list();
			List<String> SkipReadingCountData = mCurrentNode.getSkipReadingCount_list();
			List<String> DeviationData = mCurrentNode.getDeviation_list();
			List<String> TestRunTypeData = mCurrentNode.getTestRunType_list();
			ApplicationLauncher.logger.info("SaveToDB: columnData: " + columnData);
			if(columnData.size() !=0){
				saved_successfully_count = 0;
				String testname = "";
				String emin = "";
				String emax = "";
				String pulses = "";
				String average = "";
				String time = "";
				String SkipReadingCount = "";
				String deviation = "";
				String runtype = "";
				String harmonicOrder = "";
				for(int i =0; i< columnData.size(); i++){
					testname = columnData.get(i);
					emin = EminData.get(i);
					emax = EmaxData.get(i);
					pulses = PulsesData.get(i);
					average = AverageData.get(i);
					time = TimeData.get(i);
					SkipReadingCount = SkipReadingCountData.get(i);
					deviation = DeviationData.get(i);
					runtype = TestRunTypeData.get(i);
					ApplicationLauncher.logger.debug("SaveToDB:  harmonicsTestPointNamePart : " + harmonicsTestPointNamePart  );
				    harmonicOrder = harmonicsTestPointNamePart;  //pradeep    harmonicsTestPointNamePart 
				    
					status = SaveHarmonicToDB(testname, emin, emax, pulses,average, time, SkipReadingCount, deviation, runtype, harmonicOrder);
				}
				/*ArrayList<String> selectedpointnames = getSelectedTestPointsNames();
				int no_of_test_points = selectedpointnames.size()*columnData.size();
				ApplicationLauncher.logger.info("SaveToDB: no_of_test_points: " + no_of_test_points);
				ApplicationLauncher.logger.info("SaveToDB: saved_successfully_count: " + saved_successfully_count);
				if(saved_successfully_count == no_of_test_points){
					ApplicationLauncher.logger.info ("PropertyHarmonicController: SaveToDB: Test point saved successfully");
					ApplicationLauncher.InformUser("Saved", "Test point saved successfully",AlertType.INFORMATION);
				}
				else{
					ApplicationLauncher.logger.info ("PropertyHarmonicController: SaveToDB: Test point save failed. Test point save failed:" +saveFailedReason);
					ApplicationLauncher.InformUser("Incorrect Value", "Test point save failed: " + saveFailedReason, AlertType.ERROR);
				}
				
				*/
			}
			else{
				ApplicationLauncher.logger.info ("PropertyHarmonicController: SaveToDB:  Save the test point inputs - warning triggered");
				ApplicationLauncher.InformUser("Not Saved","Save the test point inputs",AlertType.WARNING);
			     status  = false ;
			}
				return status ;
		}

//===================================================================================================================
		
		public boolean SaveHarmonicToDB(String testname, String emin, String emax, String pulses,String average ,
				String time, String SkipReadingCount, String deviation, String runtype,
				String harmonicOrder){
			
			boolean status = false ;
			
			ArrayList<String> selectedpointnames = getSelectedTestPointsNames();
			ApplicationLauncher.logger.debug ("PropertyHarmonicControllerV2: SaveHarmonicToDB: selectedpointnames: " + selectedpointnames);
			String project_name = mCurrentNode.getProjectName();
			ApplicationLauncher.logger.debug ("PropertyHarmonicControllerV2: SaveHarmonicToDB: project_name: " + project_name);
			String test_type = mCurrentNode.getType().toString();
			ApplicationLauncher.logger.debug ("PropertyHarmonicControllerV2: SaveHarmonicToDB: test_type: " + test_type);
			String aliasID = txtAlias_ID.getText();
			ApplicationLauncher.logger.debug ("PropertyHarmonicControllerV2: SaveHarmonicToDB: aliasID: " + aliasID);
			String positionID = Integer.toString(mCurrentNode.getPositionId());
			String volt = cmbBox_Un.getValue();
			
			/*String harmonicOrder = "";//ref_cmbBox_harmonic1.getSelectionModel().getSelectedItem();
			if(harmonicOrder.equals("Select")){
				harmonicOrder = "0";
			}*/
			for(int i =0;i<selectedpointnames.size(); i++){
				ApplicationLauncher.logger.debug ("PropertyHarmonicControllerV2: SaveHarmonicToDB: selectedpointnames.get(" + i + "): " + selectedpointnames.get(i));
				
				if(GuiUtils.FormatErrorInput(emin)!=null){
					if(GuiUtils.FormatErrorInput(emax)!=null){
						if(GuiUtils.is_number(pulses)){
							if(GuiUtils.validateAvgPulses(average)){
							if(GuiUtils.is_number(time)){
								if(GuiUtils.is_number(SkipReadingCount)){
									if(GuiUtils.is_float(deviation)){	
										if(selectedpointnames.size() > 1){ 
											if(GuiUtils.is_number(volt)){
												Boolean validation_status = GuiUtils.ValidateVoltagePercentageInVoltageValue(project_name, volt);
												if (validation_status){
													ProjectController.removeTestPoint(mCurrentNode.getType().toString(), aliasID);
													String alias_name = selectedpointnames.get(i);
													String volt_display_name = GuiUtils.FormatUnForDisplay(volt);
													String summary_display_tp_name = alias_name+ "_" + aliasID + "-" +volt_display_name+ "-" + testname + "-" + harmonicOrder;
													status = MySQL_Controller.sp_add_project_components(project_name,summary_display_tp_name,test_type, aliasID, positionID,
															time , "",  "", "","","","",
															emin, emax, pulses, SkipReadingCount,deviation,runtype, "","",volt,
															"","","","","",
															"","","","","",
															"","","",average);

													ProjectController.UpdateNewTestPointSummaryDataToDB(mCurrentNode.getProjectName(), summary_display_tp_name , 
															mCurrentNode.getType().toString(),  aliasID);
													saved_successfully_count = saved_successfully_count + 1;
												}else{
													saveFailedReason = testname +": Voltage validation failed due to configured acceptable limit";
													status = false ;
												}
											}else{

												saveFailedReason = testname +": voltage is not a valid number";
												status = false ;
											}
										}else{

											saveFailedReason = testname +": Atleast one Test point should be selected!!";
											status = false ;
										}
									}else{

										saveFailedReason = testname +": deviation is not a valid float value";
										status = false ;
									}
								}else{

									saveFailedReason = testname +": skip reading count is not a valid number";
									status = false ;
								}
							}else{

								saveFailedReason = testname +": time is not a valid number";
								status = false ;
							}
						}else{

							saveFailedReason = testname +": average is not a valid number";
							status = false ;
						}
					}else{

							saveFailedReason = testname +": pulse is not a valid number";
							status = false ;
						}
				}else{

						saveFailedReason = testname +": Error max is not a valid format";
						status = false ;
					}
			}else{

					saveFailedReason = testname +": Error min is not a valid format";
					status = false ;
				}
			}
			ApplicationLauncher.logger.debug ("PropertyHarmonicControllerV2: SaveHarmonicToDB: saveFailedReason: " + saveFailedReason);
			
			if(!status){
				ApplicationLauncher.InformUser("Failed to save harmonics data", " Reason: + saveFailedReason", AlertType.ERROR);
			}
			
			return status ;
		}	
		
		
//=======================================================================
		
		  //public void SaveHarmonics(String phase_selected, String harmonic_order, String harmonic_volt,
		  //		                  String harmonic_volt_phase,String harmonic_current,String harmonic_current_phase){
			public boolean SaveHarmonics(){
				
			ApplicationLauncher.logger.debug("SaveHarmonics:  Entry");

			boolean status = false ;
				
			String project_name = mCurrentNode.getProjectName();
			String test_type = mCurrentNode.getType().toString();
			String aliasID = txtAlias_ID.getText();
			
			String harmonicsFrequency = (String) comboBoxFrequency.getValue();
			
			ApplicationLauncher.logger.info("SaveHarmonics: project_name : " + txtTestType.getText());
			ApplicationLauncher.logger.info("SaveHarmonics: test_type    : " + txtTestType.getText());
			ApplicationLauncher.logger.info("SaveHarmonics: aliasID      : " + txtAlias_ID.getText());
			
			ApplicationLauncher.logger.info("SaveHarmonics: isPhase_R_Selected()      : " + isPhase_R_Selected());
			ApplicationLauncher.logger.info("SaveHarmonics: isPhase_Y_Selected()      : " + isPhase_Y_Selected());
			ApplicationLauncher.logger.info("SaveHarmonics: isPhase_B_Selected()      : " + isPhase_B_Selected());
			
			ApplicationLauncher.logger.info("SaveHarmonics: harmonicsFrequency      : " + harmonicsFrequency);
			
			String testCaseName = ConstantApp.HARMONIC_INPHASE_ALIAS_NAME;// pradeep
			
			harmonicsTestPointNamePart = "" ;
/*			String harmonicOrder = "3";
			//String currentHarmonicOrder = "3";
			String voltPercent = "30";
			String currentPercent = "40";
			String voltPhaseShiftDegree = "90";
		
			ApplicationLauncher.logger.info("SaveHarmonics: testCaseName: " + testCaseName);
			MySQL_Controller.sp_add_harmonic_data(project_name, 
					testCaseName, test_type, aliasID, 1, 
				Integer.parseInt(harmonicOrder),
				voltPercent, 
				currentPercent, voltPhaseShiftDegree);*/
			/*			harmonicsData.setHarmonicsOrder_V(5);	
			harmonicsData.setHarmonicOrder_V_Selected(true);
			harmonicsData.setAmplitude_V("35");
			harmonicsData.setAmplitude_I("45");
			harmonicsData.setPhaseShift_V("90");
			harmonicsData.setPhaseShift_I("180");
			harmonicsData.setPhaseSelected(ConstantApp.SECOND_PHASE_DISPLAY_NAME);*/
			

			//if ( (harmonicsData.isHarmonicOrder_V_Selected()) || (harmonicsData.isHarmonicOrder_I_Selected()) ){
	
//=============== R Phase  ==========================================================
			
			if(isPhase_R_Selected()){ 
				
			ApplicationLauncher.logger.debug("SaveHarmonics:  isPhase_R_Selected() : " + isPhase_R_Selected() );

			
			HarmonicsDataModel harmonicsData_R_Phase = new HarmonicsDataModel();

			ArrayList<HarmonicsDataModel> harmonicsOrdersData_R_phaseList = (ArrayList<HarmonicsDataModel>) getUserSelectedHarmonicOrdersList_R_Phase().stream()
                    						.filter(e -> ( (e.isHarmonicOrder_V_Selected() == true) || (e.isHarmonicOrder_I_Selected() == true) ) )
                    						.collect(Collectors.toList());
			ApplicationLauncher.logger.debug("SaveHarmonics:  harmonicsOrdersData_R_phaseList.size() : " + harmonicsOrdersData_R_phaseList.size()  );
			
			if(harmonicsOrdersData_R_phaseList.size()>0){				
				harmonicsTestPointNamePart = ConstantLscsHarmonicsSourceSlave.R_PHASE ;// + ConstantLscsHarmonicsSourceSlave.HYPHEN ;
				ApplicationLauncher.logger.debug("SaveHarmonics:  harmonicsTestPointNamePart : " + harmonicsTestPointNamePart  );

            String harmonicsSelectedStatus = "" ;
			for(int i = 0 ; i< harmonicsOrdersData_R_phaseList.size();i++){ 
				harmonicsData_R_Phase = harmonicsOrdersData_R_phaseList.get(i);	
				
				if(!harmonicsData_R_Phase.isHarmonicOrder_V_Selected()){
					harmonicsData_R_Phase.setAmplitude_V(ConstantLscsHarmonicsSourceSlave.ZERO);
					harmonicsData_R_Phase.setPhaseShift_V(ConstantLscsHarmonicsSourceSlave.ZERO);
				}
				
				if(!harmonicsData_R_Phase.isHarmonicOrder_I_Selected()){
					harmonicsData_R_Phase.setAmplitude_I(ConstantLscsHarmonicsSourceSlave.ZERO);
					harmonicsData_R_Phase.setPhaseShift_I(ConstantLscsHarmonicsSourceSlave.ZERO);
				}
				
				    ApplicationLauncher.logger.debug("SaveHarmonics: harmonicsData_R_Phase.getPhaseSelected()          : " + harmonicsData_R_Phase.getPhaseSelected());
	                ApplicationLauncher.logger.debug("SaveHarmonics: harmonicsData_R_Phase.getPhaseSelected()          : " + harmonicsData_R_Phase.getHarmonicsOrder());
	                
					ApplicationLauncher.logger.debug("SaveHarmonics: harmonicsData_R_Phase.isHarmonicOrder_V_Selected(): " + harmonicsData_R_Phase.isHarmonicOrder_V_Selected());
					ApplicationLauncher.logger.debug("SaveHarmonics: harmonicsData_R_Phase.getAmplitude_V()            : " + harmonicsData_R_Phase.getAmplitude_V());
					ApplicationLauncher.logger.debug("SaveHarmonics: harmonicsData_R_Phase.getPhaseShift_V()           : " + harmonicsData_R_Phase.getPhaseShift_V());
					
					ApplicationLauncher.logger.debug("SaveHarmonics: harmonicsData_R_Phase.isHarmonicOrder_I_Selected(): " + harmonicsData_R_Phase.isHarmonicOrder_I_Selected());
					ApplicationLauncher.logger.debug("SaveHarmonics: harmonicsData_R_Phase.getAmplitude_I()            : " + harmonicsData_R_Phase.getAmplitude_I());
					ApplicationLauncher.logger.debug("SaveHarmonics: harmonicsData_R_Phase.getPhaseShift_I()           : " + harmonicsData_R_Phase.getPhaseShift_I());
				
				
				testCaseName = ConstantApp.HARMONIC_INPHASE_ALIAS_NAME;//"";
				
//===========				  
				harmonicsTestPointNamePart = harmonicsTestPointNamePart + harmonicsData_R_Phase.getHarmonicsOrder();
				ApplicationLauncher.logger.debug("SaveHarmonics:  harmonicsTestPointNamePart : " + harmonicsTestPointNamePart  );

				if(harmonicsData_R_Phase.isHarmonicOrder_V_Selected()){
					harmonicsSelectedStatus = ConstantLscsHarmonicsSourceSlave.HARM_IN_ONLY_V ;
					if(harmonicsData_R_Phase.isHarmonicOrder_I_Selected()){
						harmonicsSelectedStatus = ConstantLscsHarmonicsSourceSlave.HARM_IN_V_AND_I ;
					}
				}
				else if(harmonicsData_R_Phase.isHarmonicOrder_I_Selected()){
					harmonicsSelectedStatus = ConstantLscsHarmonicsSourceSlave.HARM_IN_ONLY_I ;
				}
				
				harmonicsTestPointNamePart = harmonicsTestPointNamePart + harmonicsSelectedStatus ;
				ApplicationLauncher.logger.debug("SaveHarmonics:  harmonicsTestPointNamePart : " + harmonicsTestPointNamePart  );
          
//============
				
/*				if(harmonicsData_R_Phase.getPhaseShift_V()=="0"){
					if(harmonicsData_R_Phase.getPhaseShift_I()=="0"){
						testCaseName = ConstantApp.HARMONIC_INPHASE_ALIAS_NAME;
					}
				}
				
				if(harmonicsData_R_Phase.getPhaseShift_V()=="180"){
					if(harmonicsData_R_Phase.getPhaseShift_I()=="180"){
						testCaseName = ConstantApp.HARMONIC_OUTOFPHASE_ALIAS_NAME;
					}
				}	*/
				
				harmonicsData_R_Phase.setPhaseSelected( ConstantApp.FIRST_PHASE_DISPLAY_NAME);  
				if(ref_checkbox_inphase.isSelected()) {
					testCaseName = ConstantApp.HARMONIC_INPHASE_ALIAS_NAME;
					status = MySQL_Controller.sp_add_harmonic_dataV2(project_name, testCaseName, test_type, aliasID, harmonicsFrequency, harmonicsData_R_Phase);	
					
				}
				
				if(ref_checkbox_outphase.isSelected()) {
					testCaseName = ConstantApp.HARMONIC_OUTOFPHASE_ALIAS_NAME;
					status = MySQL_Controller.sp_add_harmonic_dataV2(project_name, testCaseName, test_type, aliasID, harmonicsFrequency, harmonicsData_R_Phase);	
					
				}
							
				//harmonicsData_R_Phase.setPhaseSelected( ConstantApp.FIRST_PHASE_DISPLAY_NAME);  
				//status = MySQL_Controller.sp_add_harmonic_dataV2(project_name, testCaseName, test_type, aliasID, harmonicsFrequency, harmonicsData_R_Phase);	
			}
			}
			}
			
//=============== Y Phase  ==========================================================
			if(isPhase_Y_Selected()){ 
				ApplicationLauncher.logger.debug("SaveHarmonics:  isPhase_Y_Selected() : " + isPhase_Y_Selected() );

			HarmonicsDataModel harmonicsData_Y_Phase = new HarmonicsDataModel();

			ArrayList<HarmonicsDataModel> harmonicsOrdersData_Y_phaseList = (ArrayList<HarmonicsDataModel>) getUserSelectedHarmonicOrdersList_Y_Phase().stream()
                    						.filter(e -> ( (e.isHarmonicOrder_V_Selected() == true) || (e.isHarmonicOrder_I_Selected() == true) ) )
                    						.collect(Collectors.toList());
			
			ApplicationLauncher.logger.debug("SaveHarmonics:  harmonicsOrdersData_Y_phaseList.size() : " + harmonicsOrdersData_Y_phaseList.size()  );

			
			if(harmonicsOrdersData_Y_phaseList.size()>0){
						   				harmonicsTestPointNamePart = harmonicsTestPointNamePart + //ConstantLscsHarmonicsSourceSlave.HYPHEN + 
                                               ConstantLscsHarmonicsSourceSlave.Y_PHASE ; //ConstantLscsHarmonicsSourceSlave.HYPHEN ;
				ApplicationLauncher.logger.debug("SaveHarmonics:  harmonicsTestPointNamePart : " + harmonicsTestPointNamePart  );

				String harmonicsSelectedStatus = "" ;
			for(int i =0 ; i< harmonicsOrdersData_Y_phaseList.size();i++){
				harmonicsData_Y_Phase = harmonicsOrdersData_Y_phaseList.get(i);
				
				if(!harmonicsData_Y_Phase.isHarmonicOrder_V_Selected()){
					harmonicsData_Y_Phase.setAmplitude_V(ConstantLscsHarmonicsSourceSlave.ZERO);
					harmonicsData_Y_Phase.setPhaseShift_V(ConstantLscsHarmonicsSourceSlave.ZERO);
				}
				
				if(!harmonicsData_Y_Phase.isHarmonicOrder_I_Selected()){
					harmonicsData_Y_Phase.setAmplitude_I(ConstantLscsHarmonicsSourceSlave.ZERO);
					harmonicsData_Y_Phase.setPhaseShift_I(ConstantLscsHarmonicsSourceSlave.ZERO);
				}
				
				testCaseName = ConstantApp.HARMONIC_INPHASE_ALIAS_NAME;//"";
				
 //=====               
				harmonicsTestPointNamePart = harmonicsTestPointNamePart + harmonicsData_Y_Phase.getHarmonicsOrder();
				ApplicationLauncher.logger.debug("SaveHarmonics:  harmonicsTestPointNamePart : " + harmonicsTestPointNamePart  );

				if(harmonicsData_Y_Phase.isHarmonicOrder_V_Selected()){
					harmonicsSelectedStatus = ConstantLscsHarmonicsSourceSlave.HARM_IN_ONLY_V ;
					if(harmonicsData_Y_Phase.isHarmonicOrder_I_Selected()){
						harmonicsSelectedStatus = ConstantLscsHarmonicsSourceSlave.HARM_IN_V_AND_I ;
					}
				}
				else if(harmonicsData_Y_Phase.isHarmonicOrder_I_Selected()){
					harmonicsSelectedStatus = ConstantLscsHarmonicsSourceSlave.HARM_IN_ONLY_I ;
				}
				
				harmonicsTestPointNamePart = harmonicsTestPointNamePart + harmonicsSelectedStatus ;	
				ApplicationLauncher.logger.debug("SaveHarmonics:  harmonicsTestPointNamePart : " + harmonicsTestPointNamePart  );

//=======				
/*				if(harmonicsData_Y_Phase.getPhaseShift_V()=="0"){
					if(harmonicsData_Y_Phase.getPhaseShift_I()=="0"){
						testCaseName = ConstantApp.HARMONIC_INPHASE_ALIAS_NAME;
					}
				}
				
				if(harmonicsData_Y_Phase.getPhaseShift_V()=="180"){
					if(harmonicsData_Y_Phase.getPhaseShift_I()=="180"){
						testCaseName = ConstantApp.HARMONIC_OUTOFPHASE_ALIAS_NAME;
					}
				}*/
				
				
				harmonicsData_Y_Phase.setPhaseSelected(ConstantApp.SECOND_PHASE_DISPLAY_NAME);
				
				if(ref_checkbox_inphase.isSelected()) {
					testCaseName = ConstantApp.HARMONIC_INPHASE_ALIAS_NAME;
					status = MySQL_Controller.sp_add_harmonic_dataV2(project_name, testCaseName, test_type, aliasID, harmonicsFrequency, harmonicsData_Y_Phase);
					
				}
				
				if(ref_checkbox_outphase.isSelected()) {
					testCaseName = ConstantApp.HARMONIC_OUTOFPHASE_ALIAS_NAME;
					status = MySQL_Controller.sp_add_harmonic_dataV2(project_name, testCaseName, test_type, aliasID, harmonicsFrequency, harmonicsData_Y_Phase);
					
				}
				
				//status = MySQL_Controller.sp_add_harmonic_dataV2(project_name, testCaseName, test_type, aliasID, harmonicsFrequency, harmonicsData_Y_Phase);
				
			}
			}
			}
			
//=============== B Phase  ==========================================================
			
			if(isPhase_B_Selected()){ 
				ApplicationLauncher.logger.debug("SaveHarmonics:  isPhase_B_Selected() : " + isPhase_B_Selected() );

			HarmonicsDataModel harmonicsData_B_Phase = new HarmonicsDataModel();

			ArrayList<HarmonicsDataModel> harmonicsOrdersData_B_phaseList = (ArrayList<HarmonicsDataModel>) getUserSelectedHarmonicOrdersList_B_Phase().stream()
                    						.filter(e -> ( (e.isHarmonicOrder_V_Selected() == true) || (e.isHarmonicOrder_I_Selected() == true) ) )
                    						.collect(Collectors.toList());
			
			ApplicationLauncher.logger.debug("SaveHarmonics:  harmonicsOrdersData_B_phaseList.size() : " + harmonicsOrdersData_B_phaseList.size()  );

			
			if(harmonicsOrdersData_B_phaseList.size()>0){
				harmonicsTestPointNamePart = harmonicsTestPointNamePart + //ConstantLscsHarmonicsSourceSlave.HYPHEN + 
                                                                          ConstantLscsHarmonicsSourceSlave.B_PHASE;// + ConstantLscsHarmonicsSourceSlave.HYPHEN ;
				ApplicationLauncher.logger.debug("SaveHarmonics:  harmonicsTestPointNamePart : " + harmonicsTestPointNamePart  );

				String harmonicsSelectedStatus = "" ;
			for(int i =0 ; i< harmonicsOrdersData_B_phaseList.size();i++){
				harmonicsData_B_Phase = harmonicsOrdersData_B_phaseList.get(i);
				
				if(!harmonicsData_B_Phase.isHarmonicOrder_V_Selected()){
					harmonicsData_B_Phase.setAmplitude_V(ConstantLscsHarmonicsSourceSlave.ZERO);
					harmonicsData_B_Phase.setPhaseShift_V(ConstantLscsHarmonicsSourceSlave.ZERO);
				}
				
				if(!harmonicsData_B_Phase.isHarmonicOrder_I_Selected()){
					harmonicsData_B_Phase.setAmplitude_I(ConstantLscsHarmonicsSourceSlave.ZERO);
					harmonicsData_B_Phase.setPhaseShift_I(ConstantLscsHarmonicsSourceSlave.ZERO);
				}
				
				testCaseName = ConstantApp.HARMONIC_INPHASE_ALIAS_NAME;//"";
				
 //=====               
				harmonicsTestPointNamePart = harmonicsTestPointNamePart + harmonicsData_B_Phase.getHarmonicsOrder();
				ApplicationLauncher.logger.debug("SaveHarmonics:  harmonicsTestPointNamePart : " + harmonicsTestPointNamePart  );

				if(harmonicsData_B_Phase.isHarmonicOrder_V_Selected()){
					harmonicsSelectedStatus = ConstantLscsHarmonicsSourceSlave.HARM_IN_ONLY_V ;
					if(harmonicsData_B_Phase.isHarmonicOrder_I_Selected()){
						harmonicsSelectedStatus = ConstantLscsHarmonicsSourceSlave.HARM_IN_V_AND_I ;
					}
				}
				else if(harmonicsData_B_Phase.isHarmonicOrder_I_Selected()){
					harmonicsSelectedStatus = ConstantLscsHarmonicsSourceSlave.HARM_IN_ONLY_I ;
				}
				
				harmonicsTestPointNamePart = harmonicsTestPointNamePart + harmonicsSelectedStatus ;	
				ApplicationLauncher.logger.debug("SaveHarmonics:  harmonicsTestPointNamePart : " + harmonicsTestPointNamePart  );

//=======
				
/*				if(harmonicsData_B_Phase.getPhaseShift_V()=="0"){
					if(harmonicsData_B_Phase.getPhaseShift_I()=="0"){
						testCaseName = ConstantApp.HARMONIC_INPHASE_ALIAS_NAME;
					}
				}
				
				if(harmonicsData_B_Phase.getPhaseShift_V()=="180"){
					if(harmonicsData_B_Phase.getPhaseShift_I()=="180"){
						testCaseName = ConstantApp.HARMONIC_OUTOFPHASE_ALIAS_NAME;
					}
				}*/	
				harmonicsData_B_Phase.setPhaseSelected( ConstantApp.THIRD_PHASE_DISPLAY_NAME);  
				
				
				if(ref_checkbox_inphase.isSelected()) {
					testCaseName = ConstantApp.HARMONIC_OUTOFPHASE_ALIAS_NAME;
					status = MySQL_Controller.sp_add_harmonic_dataV2(project_name, testCaseName, test_type, aliasID, harmonicsFrequency, harmonicsData_B_Phase);		
					
				}
				
				if(ref_checkbox_outphase.isSelected()) {
					testCaseName = ConstantApp.HARMONIC_INPHASE_ALIAS_NAME;
					status = MySQL_Controller.sp_add_harmonic_dataV2(project_name, testCaseName, test_type, aliasID, harmonicsFrequency, harmonicsData_B_Phase);		
					
				}
			
				//status = MySQL_Controller.sp_add_harmonic_dataV2(project_name, testCaseName, test_type, aliasID, harmonicsFrequency, harmonicsData_B_Phase);		
			
			}
			}
			}
			//}
/*
			if(!(harmonic_1_times.equals("Select"))){
				ApplicationLauncher.logger.info("SaveHarmonics: harmonic_1_times: " + harmonic_1_times);
				if(GuiUtils.is_number(harmonic_1_volt) && !harmonic_1_volt.isEmpty()){
					ApplicationLauncher.logger.info("SaveHarmonics: harmonic_1_volt: " + harmonic_1_volt);
					if(GuiUtils.is_number(harmonic_1_current) && !harmonic_1_current.isEmpty()){
						ApplicationLauncher.logger.info("SaveHarmonics: harmonic_1_current: " + harmonic_1_current);
						if(harmonic_phases.size()!=0){
							ApplicationLauncher.logger.info("SaveHarmonics: harmonic_phases: " + harmonic_phases);
							for(int i=0; i<harmonic_phases.size();i++){
								String testcasename = "";
								if(harmonic_phases.get(i).equals("0")){
									testcasename = ConstantApp.HARMONIC_INPHASE_ALIAS_NAME;
								}
								else{
									testcasename = ConstantApp.HARMONIC_OUTOFPHASE_ALIAS_NAME;
								}
								ApplicationLauncher.logger.info("SaveHarmonics: testcasename: " + testcasename);
								MySQL_Controller.sp_add_harmonic_data(project_name, 
										testcasename, test_type, aliasID, 1, 
										Integer.parseInt(harmonic_1_times),
										harmonic_1_volt, 
										harmonic_1_current, harmonic_phases.get(i));
							}
						}
					}
				}
			}	


			if(!(harmonic_2_times.equals("Select"))){
				if(GuiUtils.is_number(harmonic_2_volt) && !harmonic_2_volt.isEmpty()){
					if(GuiUtils.is_number(harmonic_2_current) && !harmonic_2_current.isEmpty()){
						if(harmonic_phases.size()!=0){
							for(int i=0; i<harmonic_phases.size();i++){
								String testcasename = "";
								if(harmonic_phases.get(i).equals("0")){
									testcasename = ConstantApp.HARMONIC_INPHASE_ALIAS_NAME;
								}
								else{
									testcasename = ConstantApp.HARMONIC_OUTOFPHASE_ALIAS_NAME;
								}
								MySQL_Controller.sp_add_harmonic_data(project_name, 
										testcasename, test_type, aliasID, 2, 
										Integer.parseInt(harmonic_2_times),
										harmonic_2_volt, 
										harmonic_2_current, harmonic_phases.get(i));
							}
						}
					}
				}
			}*/
	/*		ProjectController.SaveProject();
			ProjectController.LoadSummaryDataToGUI();*/
			
			ApplicationLauncher.logger.debug("SaveHarmonics:  Exit");
			
			
			if(!status){
				ApplicationLauncher.logger.error("SaveHarmonics: Harmonics data not added ");
			}
			
			return status ;
}
		
//================================
			
		public ArrayList<String> getSelectedTestPointsNames(){
			ArrayList<String>  testpointnames =  new ArrayList<String>();
			testpointnames.add(ConstantApp.HARMONIC_WITHOUT_ALIAS_NAME);
			if(ref_checkbox_inphase.isSelected()){
				testpointnames.add(ConstantApp.HARMONIC_INPHASE_ALIAS_NAME);
			}
			if(ref_checkbox_outphase.isSelected()){
				testpointnames.add(ConstantApp.HARMONIC_OUTOFPHASE_ALIAS_NAME);
			}
			
			//testpointnames.add(ConstantApp.HARMONIC_INPHASE_ALIAS_NAME);//pradeep
			return testpointnames;
		}


		public static boolean isPhase_R_Selected() {
			return phase_R_Selected;
		}


		public static void setPhase_R_Selected(boolean phase_R_Selected) {
			PropertyHarmonicControllerV2.phase_R_Selected = phase_R_Selected;
		}


		public static boolean isPhase_Y_Selected() {
			return phase_Y_Selected;
		}


		public static void setPhase_Y_Selected(boolean phase_Y_Selected) {
			PropertyHarmonicControllerV2.phase_Y_Selected = phase_Y_Selected;
		}

		public static boolean isPhase_B_Selected() {
			return phase_B_Selected;
		}


		public static void setPhase_B_Selected(boolean phase_B_Selected) {
			PropertyHarmonicControllerV2.phase_B_Selected = phase_B_Selected;
		}

//==============================================================================================================
	
	
}

