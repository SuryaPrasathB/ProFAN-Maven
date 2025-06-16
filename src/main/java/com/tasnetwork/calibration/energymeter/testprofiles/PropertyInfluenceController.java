package com.tasnetwork.calibration.energymeter.testprofiles;

//PFMappingToDO
//PFMappingDone

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.database.MySQL_Controller;
import com.tasnetwork.calibration.energymeter.project.ProjectController;
import com.tasnetwork.calibration.energymeter.util.EditCell;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;
import com.tasnetwork.calibration.energymeter.util.MyFloatStringConverter;
import com.tasnetwork.calibration.energymeter.util.MyIntegerStringConverter;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;

public class PropertyInfluenceController implements Initializable {


	@FXML
	private TableView<TestSetupDataModel> imaxDataTable;

	@FXML
	private TableView<TestSetupDataModel> ibaseDataTable;
	@FXML
	private TableView<TestSetupDataModel> pfAbcDataTable;
	@FXML
	private TableView<TestSetupDataModel> pfAOrBOrCDataTable;

	@FXML
	private TableColumn<TestSetupDataModel, String> imaxDataColumn;
	@SuppressWarnings("rawtypes")
	@FXML
	private TableColumn imaxSelectedColumn;
	@SuppressWarnings("rawtypes")
	@FXML
	private TableColumn ibSelectedColumn;

	@FXML
	private ScrollPane scrollPaneInfProperty;

	@FXML
	private TableColumn<TestSetupDataModel, String> ibaseDataColumn;
	@SuppressWarnings("rawtypes")
	@FXML
	private TableColumn ibaseSelectedColumn;

	@FXML
	private TableColumn<TestSetupDataModel, String> pfAbcDataColumn;
	@SuppressWarnings("rawtypes")
	@FXML
	private TableColumn pfAbcSelectedColumn;
	@SuppressWarnings("rawtypes")
	@FXML
	private TableColumn pfA_b_cSelectedColumn;

	@FXML
	private TableColumn<TestSetupDataModel, String> pfAOrBOrCDataColumn;
	@SuppressWarnings("rawtypes")
	@FXML
	private TableColumn pfAOrBOrCSelectedColumn;

	@FXML
	private AnchorPane AnchorPropertyInfluence;

	@FXML
	private AnchorPane AnchorPaneTC_Setup;

	@FXML
	private AnchorPane InnerAnchorPropertyInfluence;

	@FXML
	private AnchorPane anchorPaneCurrentMapping;

	private ObservableList<TestSetupDataModel> imaxData = FXCollections.observableArrayList();
	private ObservableList<TestSetupDataModel> pfAbcData = FXCollections.observableArrayList();
	@FXML
	private Accordion testDataMatrixAccordian;
	@FXML
	private TitledPane testCaseSetupPane;
	@FXML
	private TitledPane testCaseSelectionPane;
	@FXML
	private TitledPane testCaseInputsPane;
	@FXML
	private TitledPane testCasePropertiesPane;
	@FXML
	private TitledPane testCaseSetupIMappingPane;

	@FXML
	private TitledPane TitledPaneTestPointSetup;

	@FXML
	private TableView<List<Object>> testCaseSelectionTable;
	@FXML
	private TableView<TestCaseData> testCaseInputsTable;

	@FXML
	private TableColumn<TestCaseData, String> testCaseNameColumn;
	@FXML
	private TableColumn<TestCaseData, Float> testCaseEminColumn;
	@FXML
	private TableColumn<TestCaseData, Float> testCaseEmaxColumn;
	@FXML
	private TableColumn<TestCaseData, Integer> testCasePulseColumn;
	@FXML
	private TableColumn<TestCaseData, Integer> testCasePulseAverageColumn;
	
	
	@FXML
	private TableColumn<TestCaseData, Integer> testCaseTimeColumn;
	
	static TableColumn<TestCaseData, Integer> ref_testCaseTimeColumn;
	
	@FXML
	private TableColumn<TestCaseData, Integer> testCaseSkipReadingCountColumn;
	@FXML
	private TableColumn<TestCaseData, Float> testCaseDeviationColumn;
	@SuppressWarnings("rawtypes")
	@FXML
	private TableColumn testCaseRunTypeColumn;

	@SuppressWarnings("rawtypes")
	@FXML
	private TableView testSetupPreviewTable;

	@FXML
	private TitledPane  TitledPaneTestPointGridPrvw;

	@FXML
	private TextField  txt_current_input;
	public static TextField  ref_txt_current_input;


	@FXML
	private TextField txt_pf_input;
	public static TextField  ref_txt_pf_input;

	@FXML	private Button btn_Save;
	@FXML	private Button btnSetupNext;
	@FXML	private Button btnSelectionNext;

	@FXML
	private Button  btn_tc_input_save;
	
	@FXML
	private Button btn_SelectAll;

	@FXML
	private Button  btn_I_mapping_Save;

	@FXML
	private Button  btn_tp_setup_Save;

	@FXML
	private TitledPane  pfUserMapping;

	@FXML
	private RadioButton radioBtn_Lag;
	@FXML
	private RadioButton radioBtn_Lead;

	@FXML
	private ListView<String>  listview_currentlist;
	public static ListView<String>  ref_listview_currentlist;

	@FXML
	private ListView<String>  listview_pf_list;
	public static ListView<String>  ref_listview_pf_list;
	
	public static Button ref_btn_Save;
	public static Button ref_btnSetupNext;
	public static Button ref_btnSelectionNext;
	public static Button ref_btn_tc_input_save;

	private String Test_type;
	private String Alias_ID;
	public static DraggableTestNode mCurrentNode=null;

	ArrayList<String> column_names = new ArrayList<String>();
	List<List<Object>> row_values = new ArrayList<List<Object>>();
	ArrayList<String> I_mapped_values = new ArrayList<String>();
	ArrayList<String> PF_mapped_values = new ArrayList<String>();
	ArrayList<String> PreviousTP_setup = new ArrayList<String>();
	ArrayList<String> TPS_column_names = new ArrayList<String>();
	ArrayList<ArrayList<Object>> TPS_row_data = new ArrayList<ArrayList<Object>>();

	public ArrayList<String>  getColumn_names() {
		return column_names;
	}
	public void setColumn_names(ArrayList<String> columns) {
		column_names = columns;

	}
	public List<List<Object>> getRow_names() {
		return row_values;
	}
	public void setRow_names(List<List<Object>> rows) {
		row_values = rows;

	}

	public ArrayList<String> get_I_Mappped_Values() {
		return I_mapped_values;
	}
	public void set_I_Mappped_Values(ArrayList<String> i_values) {
		I_mapped_values = i_values;

	}

	public ArrayList<String> get_PF_Mappped_Values() {
		return PF_mapped_values;
	}
	public void set_PF_Mappped_Values(ArrayList<String> pf_values) {
		PF_mapped_values = pf_values;

	}

	public ArrayList<String> getPreviousTP_setup() {
		return PreviousTP_setup;
	}
	public void setPreviousTP_setup(ArrayList<String> TP_setup) {
		PreviousTP_setup = TP_setup;

	}

	public void initialize(URL url, ResourceBundle rb) {
		ApplicationLauncher.logger.info("Test case matrix controller");
		ref_testCaseTimeColumn = testCaseTimeColumn;
		if(ProcalFeatureEnable.PHASE_DISPLAY_ENABLE_FEATURE){
			pfAbcSelectedColumn.setText(ConstantApp.FIRST_PHASE_DISPLAY_NAME+ConstantApp.SECOND_PHASE_DISPLAY_NAME+ConstantApp.THIRD_PHASE_DISPLAY_NAME);
			pfA_b_cSelectedColumn.setText(ConstantApp.FIRST_PHASE_DISPLAY_NAME+"/"+ConstantApp.SECOND_PHASE_DISPLAY_NAME+"/"+ConstantApp.THIRD_PHASE_DISPLAY_NAME);
		}
		if(!ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED){
			pfAbcSelectedColumn.setText(ConstantApp.FIRST_PHASE_DISPLAY_NAME);
			pfA_b_cSelectedColumn.setVisible(false);
		}


		ref_listview_currentlist = listview_currentlist;
		ref_listview_pf_list = listview_pf_list; // PFMappingDone
		ref_txt_current_input = txt_current_input;
		ref_txt_pf_input = txt_pf_input;
		
		ref_btn_Save = btn_Save;
		ref_btnSetupNext = btnSetupNext;
		ref_btnSelectionNext = btnSelectionNext;
		ref_btn_tc_input_save = btn_tc_input_save;
		
		initTestCaseSelectionData();
		initTestCaseInputsData();
		ApplicationLauncher.logger.info("Test setup Init");
		PropertyInfluenceAdjustScreen();
/*		if((ConstantApp.USER_ACCESS_LEVEL.equals(ConstantApp.TESTER_ACCESS_LEVEL)) || 
				(ConstantApp.USER_ACCESS_LEVEL.equals(ConstantApp.READONLY_ACCESS_LEVEL))){
			btn_I_mapping_Save.setDisable(true);
			btn_tp_setup_Save.setDisable(true);
		}*/

		radioBtn_Lag.setSelected(true);
		radioBtn_Lead.setSelected(false);
		btn_SelectAll.setText("Select all");
		Platform.runLater(() -> {
			if(!ProcalFeatureEnable.RUN_TYPE_TIME_BASED_ENABLED){
				ApplicationLauncher.logger.debug("influence: Disabled Time Column");
				ref_testCaseTimeColumn.setVisible(false);
				//testCaseInputsTable
			}
		});
		
		if(ProcalFeatureEnable.USER_ACCESS_CONTROL_ENABLED){
			applyUacSettings();
		}
	}
	
	private void applyUacSettings() {
		
		ApplicationLauncher.logger.debug("PropertyCalibInfluenceController :  applyUacSettings: Entry");
		if(!ProjectController.isChildPropertySaveEnabled()){
			//btn_Save.setDisable(true);
			ref_btn_Save.setDisable(true);
			ref_btnSetupNext.setDisable(true);
			ref_btnSelectionNext.setDisable(true);
			ref_btn_tc_input_save.setDisable(true);
		}
		
	}
	
	public void btn_SelectAllOnClick(){
		ApplicationLauncher.logger.debug("SetAllTestPoint: btn_SelectAll.getText():"+btn_SelectAll.getText());
		if(btn_SelectAll.getText().equals("Select all")){
			btn_SelectAll.setText("Unselect all");
			SetAllTestPoint(true);
		}else if (btn_SelectAll.getText().equals("Unselect all")){
			btn_SelectAll.setText("Select all");
			SetAllTestPoint(false);
		}

	}
	
	public void SetAllTestPoint(Boolean bValue){


		ApplicationLauncher.logger.debug("SetAllTestPoint: Entry");
		//ApplicationLauncher.logger.info("selected_col_list" + selected_col_list);
		ObservableList<List<Object>> testCaseSelectionList =  testCaseSelectionTable.getItems();
		int NoOfColumns= testCaseSelectionTable.getColumns().size();
		for (List<Object> testCaseRow : testCaseSelectionList) {
			ApplicationLauncher.logger.debug("Current Row : testCaseRow1: " + testCaseRow.get(0).toString());
			
			//for(int j =1; j <getColumn_names().size(); j++){
			for(int j =1; j <NoOfColumns; j++){	

				try{
					ApplicationLauncher.logger.debug("Current Row : Column: " + j);
					testCaseRow.set(j, bValue);
				}catch (Exception e){
					ApplicationLauncher.logger.error("SetAllTestPoint: Exception: " + e.getMessage());
				}

			}
		}

		testCaseSelectionTable.refresh();

	}

	public void radioBtn_LagOnChange(){

		if(radioBtn_Lag.isSelected()){
			radioBtn_Lead.setSelected(false);
		}else{
			radioBtn_Lead.setSelected(true);
		}

	}
	public void radioBtn_LeadOnChange(){
		if(radioBtn_Lead.isSelected()){
			radioBtn_Lag.setSelected(false);
		}else{
			radioBtn_Lag.setSelected(true);
		}
	}

	public void PropertyInfluenceAdjustScreen(){

		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		double ScreenHeight = primaryScreenBounds.getHeight();
		double ScreenWidth= primaryScreenBounds.getWidth();
		double SplitPaneOffset = 149;
		double BottomStatusPaneOffset = 56;
		double HeaderOffset = 30;
		double TemplateWidthOffset = 232;
		double TC_WidthOffset = 225;
		double SummaryWidthOffset = 230;


		long ScreenWidthThreshold = 1500;
		long ScreenHeightThreshold = 700;
		new ConstantApp();

		ScreenWidthThreshold = ConstantAppConfig.ScreenWidthThreshold;
		ScreenHeightThreshold = ConstantAppConfig.ScreenHeightThreshold;

		ApplicationLauncher.logger.info("PropertyInfluenceAdjustScreen : Current Screen Height:"+ScreenHeight);
		ApplicationLauncher.logger.info("PropertyInfluenceAdjustScreen : Current Screen Width:"+ScreenWidth);
		ApplicationLauncher.logger.info("PropertyInfluenceAdjustScreen : ScreenHeightThreshold:"+ScreenHeightThreshold);
		ApplicationLauncher.logger.info("PropertyInfluenceAdjustScreen : ScreenWidthThreshold:"+ScreenWidthThreshold);

		Double TemplateHeight = ScreenHeight -BottomStatusPaneOffset;
		if(ScreenHeight >ScreenHeightThreshold){ // ScreenHeightThreshold Minimum to be set to 1000 for better UI adjustment
			AnchorPropertyInfluence.setMinHeight(TemplateHeight-SplitPaneOffset-HeaderOffset);
			scrollPaneInfProperty.setMinHeight(TemplateHeight-SplitPaneOffset-HeaderOffset);
			InnerAnchorPropertyInfluence.setMinHeight(TemplateHeight-SplitPaneOffset-HeaderOffset-10);
			testDataMatrixAccordian.setMinHeight(TemplateHeight-SplitPaneOffset-HeaderOffset-10);
			anchorPaneCurrentMapping.setMinHeight(TemplateHeight-SplitPaneOffset-HeaderOffset-500);
			testCaseSelectionTable.setMinHeight(TemplateHeight-SplitPaneOffset-HeaderOffset-235);
			testCaseInputsTable.setMinHeight(TemplateHeight-SplitPaneOffset-HeaderOffset-225);//225);

			TitledPaneTestPointGridPrvw.setMinHeight(TemplateHeight-SplitPaneOffset-HeaderOffset-550);
		}



		if(ScreenWidth >ScreenWidthThreshold){
			SummaryWidthOffset = 400;
			Double PropertyWidth = ScreenWidth-TemplateWidthOffset-TC_WidthOffset-SummaryWidthOffset-105;
			AnchorPropertyInfluence.setPrefWidth(PropertyWidth);
			scrollPaneInfProperty.setPrefWidth(PropertyWidth);
			InnerAnchorPropertyInfluence.setPrefWidth(PropertyWidth-20);
			testDataMatrixAccordian.setPrefWidth(PropertyWidth-20);
			TitledPaneTestPointGridPrvw.setPrefWidth(PropertyWidth-40);
			testCaseSelectionTable.setPrefWidth(PropertyWidth-40);
			testCaseInputsTable.setPrefWidth(PropertyWidth-40);
			testCaseTimeColumn.setPrefWidth(100);
			testCaseSkipReadingCountColumn.setPrefWidth(100);
			testCaseRunTypeColumn.setPrefWidth(150);

		}





	}

	public void initTestSetup(){
		setupImaxData();
		setupPfAbcData();
	}

	@SuppressWarnings("unchecked")
	private void setupImaxData() {
		imaxDataColumn.setCellValueFactory(cellData -> cellData.getValue().testDataProperty());
		imaxSelectedColumn.setCellValueFactory(new TestSetupCheckBoxValueFactory());
		ibSelectedColumn.setCellValueFactory(new TestSetupCheckBoxValueFactory1());
		//ibSelectedColumn.setCellValueFactory(new TestSetupCheckBoxValueFactory());
		imaxDataTable.getItems().clear();

		String project_name = ProjectController.getProjectName();
		JSONObject I_mapping_values = MySQL_Controller.sp_gettp_setup_i_user_data_mapping(project_name);
		ApplicationLauncher.logger.info("setupImaxData: I_mapping_values: " + I_mapping_values);
		ArrayList<String> I_values = new ArrayList<String>();
		try {
			ApplicationLauncher.logger.info("setupImaxData: I_mapping_values: " + I_mapping_values.getInt("No_of_I_mappings"));

			if(I_mapping_values.getInt("No_of_I_mappings") != 0){
				JSONArray I_mapping_values_arr = I_mapping_values.getJSONArray("I_mapping_values");
				JSONObject jobj = new JSONObject();
				String i_value = "";
				for(int i=0; i<I_mapping_values_arr.length(); i++){
					jobj = I_mapping_values_arr.getJSONObject(i);
					jobj.getInt("i_serial_no");
					i_value = jobj.getString("current_mapping_value");
					I_values.add(i_value);
					imaxData.add(new TestSetupDataModel(i_value,Boolean.FALSE, TestDataTypeEnum.IMAX,Boolean.FALSE, TestDataTypeEnum.IBASE));
				}
				set_I_Mappped_Values(I_values);
			}

		} catch (JSONException e) {
			
			ApplicationLauncher.logger.error("setupImaxData: JSONException" + e.getMessage());
			e.printStackTrace();
		}




		imaxDataTable.setItems(imaxData);
	}





	@SuppressWarnings("unchecked")
	private void setupPfAbcData() {
		pfAbcDataColumn.setCellValueFactory(cellData -> cellData.getValue().testDataProperty());
		pfAbcSelectedColumn.setCellValueFactory(new TestSetupCheckBoxValueFactory());
		pfA_b_cSelectedColumn.setCellValueFactory(new TestSetupCheckBoxValueFactory1());
		//pfA_b_cSelectedColumn.setCellValueFactory(new TestSetupCheckBoxValueFactory());
		pfAbcDataTable.getItems().clear();

		String project_name = ProjectController.getProjectName();
		JSONObject pf_mapping_values = MySQL_Controller.sp_gettp_setup_pf_user_data_mapping(project_name);
		ApplicationLauncher.logger.info("setupPfAbcData: pf_mapping_values: " + pf_mapping_values);
		ArrayList<String> PF_values = new ArrayList<String>();
		try {
			ApplicationLauncher.logger.info("setupPfAbcData: pf_mapping_values: " + pf_mapping_values.getInt("No_of_PF_mappings"));

			if(pf_mapping_values.getInt("No_of_PF_mappings") != 0){
				JSONArray PF_mapping_values_arr = pf_mapping_values.getJSONArray("PF_mapping_values");
				JSONObject jobj = new JSONObject();
				String pf_value = "";
				for(int i=0; i<PF_mapping_values_arr.length(); i++){
					jobj = PF_mapping_values_arr.getJSONObject(i);
					jobj.getInt("pf_serial_no");
					pf_value = jobj.getString("pf_mapping_value");
					PF_values.add(pf_value);
					pfAbcData.add(new TestSetupDataModel(pf_value,Boolean.FALSE, TestDataTypeEnum.PFABC,Boolean.FALSE, TestDataTypeEnum.PFAORBORC));
				}

				set_PF_Mappped_Values(PF_values);
			}

		} catch (JSONException e) {
			
			ApplicationLauncher.logger.error("setupPfAbcData2: JSONException" + e.getMessage());
			e.printStackTrace();
		}




		pfAbcDataTable.setItems(pfAbcData);
	}


	public void onTestSetupPreviewClick() {
		ApplicationLauncher.logger.info("Preview the test setup");
		onTestSetupSaveClick(false);
	}

	public void onTestSetupSaveClick() {
		onTestSetupSaveClick(true);
		initTestCaseSelectionData();
		initTestCaseInputsData();
		if(mCurrentNode.IsNewNode()){
			initTestCaseSelectionData();
			initTestCaseInputsData();
		}
		else{
			if(!IsDataChanged()){
				initTestCaseSelectionData();
				initTestCaseInputsData();
				Load_previous_saved_selection_table();
			}
			else{
				initTestCaseSelectionData();
				initTestCaseInputsData();
				delete_previous_saved_data();
			}
		}
		ProjectController.SaveProject();
		testDataMatrixAccordian.setExpandedPane(testCaseSelectionPane);

	}

	public static void delete_previous_saved_data(/*boolean RefreshSummaryTable*/){
		String currentProject = mCurrentNode.getProjectName();
		String currentTestType = mCurrentNode.getType().toString();
		String currentAliasId = mCurrentNode.getAliasId();
		MySQL_Controller.sp_delete_project_components(currentProject, currentTestType, currentAliasId);
		MySQL_Controller.sp_delete_summary_data(currentProject, currentTestType, currentAliasId);
		//if(currentTestType.equals("InfluenceHarmonic")){
		if(currentTestType.equals(TestProfileType.InfluenceHarmonic.toString())){
			MySQL_Controller.sp_delete_harmonic_data(currentProject, currentTestType, currentAliasId);
		}
		ProjectController.RefreshSummaryDataFromDB();
		//if(RefreshSummaryTable){
			//ProjectController.refreshSummaryPaneData("", currentAliasId);
		//}
	}

	public void Load_previous_saved_selection_table(){
		ArrayList<String> selected_row_list = new ArrayList<String>(); 
		ArrayList<String> selected_col_list = new ArrayList<String>(); 
		ObservableList<TestCaseData> testdetails = testCaseInputsTable.getItems();
		try {
			selected_checkbox(testdetails, selected_row_list, selected_col_list);
		} catch (JSONException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("Load_previous_saved_selection_table: JSONException" + e.getMessage());
		}
		setSelectionTable(selected_row_list, selected_col_list);
	}

	public boolean IsDataChanged(){
		ApplicationLauncher.logger.debug("IsDataChanged: Entry");
		ArrayList<String> previous_tp_setup = getPreviousTP_setup();
		ArrayList<String> tp_setup = FetchData();
		boolean is_data_changed = false;
		for(int i=0; i<tp_setup.size(); i++){
			if(!previous_tp_setup.get(i).equals(tp_setup.get(i))){
				is_data_changed = true;
				ApplicationLauncher.logger.debug("IsDataChanged: True");
				break;
			}
		}
		return is_data_changed;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onTestSetupSaveClick(boolean saveData) {
		ApplicationLauncher.logger.info("Saving the test setup");

		testSetupPreviewTable.getItems().clear();
		testSetupPreviewTable.getColumns().clear();

		ObservableList existingRows = FXCollections.observableList(new ArrayList());
		ObservableList existingColumns = FXCollections.observableList(new ArrayList());

		getPreviewColumns(existingColumns);
		testSetupPreviewTable.getColumns().setAll(existingColumns);

		getPreviewRows(existingRows);
		testSetupPreviewTable.getItems().setAll(existingRows);

		if (saveData) {
			saveTestSetupData();
			ArrayList<String> imax_sel_data = FetchImaxData();
			ArrayList<String> ib_sel_data = FetchIbData();
			ArrayList<String> abc_sel_data = FetchAbcData();
			ArrayList<String> a_b_c_sel_data = FetchA_b_cData();
			SaveTP_setup_ToDB(imax_sel_data, ib_sel_data, abc_sel_data, a_b_c_sel_data);
		}
	}

	public ArrayList<String> FetchData(){
		ApplicationLauncher.logger.info("FetchData: Entry");
		ArrayList<String> test_setup_data = new ArrayList<String>();
		ArrayList<String> imax_sel_data = new ArrayList<String>();
		ArrayList<String> ib_sel_data = new ArrayList<String>();
		try {
			Iterator<TestSetupDataModel> itr = imaxData.iterator();
			while (itr.hasNext()) {
				TestSetupDataModel dataElement = itr.next();
				if (!dataElement.getIsSelected()) {
					imax_sel_data.add("F");
				}
				else{
					imax_sel_data.add("T");
				}

				if (!dataElement.getIsSelected1()) {
					ib_sel_data.add("F");
				}
				else{
					ib_sel_data.add("T");
				}
			}
			while(imax_sel_data.size()<ConstantAppConfig.I_MAPPING_SIZE){// Gopi: why limited to 10?
				imax_sel_data.add("F");
			}
			while(ib_sel_data.size()<ConstantAppConfig.I_MAPPING_SIZE){// Gopi: why limited to 10?
				ib_sel_data.add("F");
			}
			ApplicationLauncher.logger.info("FetchData: imax_sel_data: " + imax_sel_data);
			ApplicationLauncher.logger.info("FetchData: ib_sel_data: " + ib_sel_data);
			test_setup_data.addAll(imax_sel_data);
			test_setup_data.addAll(ib_sel_data);

			ApplicationLauncher.logger.info("FetchData: test_setup_data_current: " + test_setup_data);

			ArrayList<String> abc_sel_data = new ArrayList<String>();
			ArrayList<String> a_b_c_sel_data = new ArrayList<String>();
			itr = pfAbcData.iterator();
			while (itr.hasNext()) {
				TestSetupDataModel dataElement = itr.next();
				ApplicationLauncher.logger.info("FetchData: dataElement:" + dataElement.getTestData());
				ApplicationLauncher.logger.info("FetchData: dataElement abc selected:" + dataElement.getIsSelected());
				ApplicationLauncher.logger.info("FetchData: dataElement a_b_c selected:" + dataElement.getIsSelected1());
				if (!dataElement.getIsSelected()) {
					abc_sel_data.add("F");
				}
				else{
					abc_sel_data.add("T");
				}

				if (!dataElement.getIsSelected1()) {
					a_b_c_sel_data.add("F");
				}
				else{
					a_b_c_sel_data.add("T");
				}
				ApplicationLauncher.logger.info("FetchData: abc_sel_data: " + abc_sel_data);
				ApplicationLauncher.logger.info("FetchData: a_b_c_sel_data: " + a_b_c_sel_data);

			}

			while(abc_sel_data.size()<ConstantAppConfig.PF_MAPPING_SIZE){
				abc_sel_data.add("F");
			}

			while(a_b_c_sel_data.size()<ConstantAppConfig.PF_MAPPING_SIZE){
				a_b_c_sel_data.add("F");
			}
			test_setup_data.addAll(abc_sel_data);
			test_setup_data.addAll(a_b_c_sel_data);


			ApplicationLauncher.logger.info("FetchData: test_setup_data" + test_setup_data);
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("FetchData: Exception: "+e.getMessage());
		}
		return test_setup_data;

	}

	public ArrayList<String> FetchImaxData(){
		ApplicationLauncher.logger.info("FetchImaxData: Entry");
		ArrayList<String> imax_sel_data = new ArrayList<String>();
		try {
			Iterator<TestSetupDataModel> itr = imaxData.iterator();
			while (itr.hasNext()) {
				TestSetupDataModel dataElement = itr.next();
				if (!dataElement.getIsSelected()) {
					imax_sel_data.add("F");
				}
				else{
					imax_sel_data.add("T");
				}
			}
			while(imax_sel_data.size()< ConstantAppConfig.I_MAPPING_SIZE){
				imax_sel_data.add("F");
			}
			ApplicationLauncher.logger.info("FetchImaxData: imax_sel_data: " + imax_sel_data);
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("FetchImaxData: Exception: "+e.getMessage());
		}
		return imax_sel_data;

	}

	public ArrayList<String> FetchIbData(){
		ApplicationLauncher.logger.info("FetchIbData: Entry");
		ArrayList<String> ib_sel_data = new ArrayList<String>();
		try {
			Iterator<TestSetupDataModel> itr = imaxData.iterator();
			while (itr.hasNext()) {
				TestSetupDataModel dataElement = itr.next();
				if (!dataElement.getIsSelected1()) {
					ib_sel_data.add("F");
				}
				else{
					ib_sel_data.add("T");
				}
			}
			while(ib_sel_data.size()< ConstantAppConfig.I_MAPPING_SIZE){
				ib_sel_data.add("F");
			}
			ApplicationLauncher.logger.info("FetchIbData: ib_sel_data: " + ib_sel_data);
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("FetchIbData: Exception: "+e.getMessage());
		}
		return ib_sel_data;

	}

	public ArrayList<String> FetchAbcData(){
		ApplicationLauncher.logger.info("FetchAbcData: Entry");
		ArrayList<String> abc_sel_data = new ArrayList<String>();
		try {
			Iterator<TestSetupDataModel> itr = pfAbcData.iterator();
			while (itr.hasNext()) {
				TestSetupDataModel dataElement = itr.next();
				ApplicationLauncher.logger.info("FetchAbcData: dataElement:" + dataElement.getTestData());
				ApplicationLauncher.logger.info("FetchAbcData: dataElement abc selected:" + dataElement.getIsSelected());
				if (!dataElement.getIsSelected()) {
					abc_sel_data.add("F");
				}
				else{
					abc_sel_data.add("T");
				}

				ApplicationLauncher.logger.info("FetchAbcData: abc_sel_data: " + abc_sel_data);

			}
			while(abc_sel_data.size()< ConstantAppConfig.PF_MAPPING_SIZE){
				abc_sel_data.add("F");
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("FetchAbcData: Exception: "+e.getMessage());
		}
		return abc_sel_data;
	}

	public ArrayList<String> FetchA_b_cData(){
		ApplicationLauncher.logger.info("FetchA_b_cData: Entry");
		ArrayList<String> a_b_c_sel_data = new ArrayList<String>();
		try {
			Iterator<TestSetupDataModel> itr = pfAbcData.iterator();
			while (itr.hasNext()) {
				TestSetupDataModel dataElement = itr.next();
				ApplicationLauncher.logger.info("FetchA_b_cData: dataElement:" + dataElement.getTestData());
				ApplicationLauncher.logger.info("FetchA_b_cData: dataElement a_b_c selected:" + dataElement.getIsSelected1());
				if (!dataElement.getIsSelected1()) {
					a_b_c_sel_data.add("F");
				}
				else{
					a_b_c_sel_data.add("T");
				}
				ApplicationLauncher.logger.info("FetchA_b_cData: a_b_c_sel_data: " + a_b_c_sel_data);

			}
			while(a_b_c_sel_data.size()< ConstantAppConfig.PF_MAPPING_SIZE){
				a_b_c_sel_data.add("F");
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("FetchA_b_cData: Exception: "+e.getMessage());
		}
		return a_b_c_sel_data;

	}



	public void SaveTP_setup_ToDB(ArrayList<String> imax_sel_data,
			ArrayList<String> ib_sel_data, ArrayList<String> abc_sel_data, 
			ArrayList<String> a_b_c_sel_data){
		String project_name = getCurrentNode().getProjectName();
		ApplicationLauncher.logger.debug("SaveTP_setup_ToDB : imax_sel_data: " + imax_sel_data);
		String i_name = "";
		for(int i=0; i < ConstantAppConfig.I_MAPPING_SIZE; i++){
			if(imax_sel_data.get(i).equals("T")){
				i_name = "imax_" + Integer.toString(i+1);
				MySQL_Controller.sp_add_test_point_setup(project_name,i_name);
			}
		}
		ApplicationLauncher.logger.debug("SaveTP_setup_ToDB : ib_sel_data: " + ib_sel_data);
		for(int i=0; i < ConstantAppConfig.I_MAPPING_SIZE; i++){
			if(ib_sel_data.get(i).equals("T")){
				i_name = "ib_" + Integer.toString(i+1);
				MySQL_Controller.sp_add_test_point_setup(project_name,i_name);
			}
		}
		ApplicationLauncher.logger.debug("SaveTP_setup_ToDB : abc_sel_data: " + abc_sel_data);
		String pf_name = "";;
		for(int i=0; i < ConstantAppConfig.PF_MAPPING_SIZE; i++){
			if(abc_sel_data.get(i).equals("T")){
				pf_name = "abc_" + Integer.toString(i+1);
				MySQL_Controller.sp_add_test_point_setup(project_name,pf_name);
			}
		}

		ApplicationLauncher.logger.debug("SaveTP_setup_ToDB : a_b_c_sel_data: " + a_b_c_sel_data);
		for(int i=0; i < ConstantAppConfig.PF_MAPPING_SIZE; i++){
			if(a_b_c_sel_data.get(i).equals("T")){
				pf_name = "a_b_c_" + Integer.toString(i+1);;
				MySQL_Controller.sp_add_test_point_setup(project_name,pf_name);
			}
		}

	}

	private float getAbsoluteCurrentValue(String percentageCurrent) {
		float retValue = 0.0F;
		try {
			retValue = (Float.parseFloat(percentageCurrent))/100;

		} catch(Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("getAbsoluteCurrentValue: Exception: " + e.getMessage());
		}
		return retValue;
	}

	public void onTestSetupCancelClick() {
		ApplicationLauncher.logger.info("Cancelling the test setup");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private int getPreviewColumns(ObservableList previewColumns) {
		int previewColumnCount = 1;
		try{
			TableColumn<List<Object>, Object> column = new TableColumn("PF/I");
			column.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get(0)));
			previewColumns.add(column);


/*			Iterator<TestSetupDataModel> itrI_Max = imaxData.iterator();
			while (itrI_Max.hasNext()) {
				TestSetupDataModel dataElement = itrI_Max.next();
				if (!dataElement.getIsSelected()) {
					continue;
				}
				String colName = "" + getAbsoluteCurrentValue(dataElement.getTestData()) + "Imax";
				column = new TableColumn<>(colName);
				column.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get(previewColumns.size())));
				previewColumns.add(column);
				previewColumnCount += 1;
			}*/

			Iterator<TestSetupDataModel> itrI_B = imaxData.iterator();
			while (itrI_B.hasNext()) {
				TestSetupDataModel dataElement = itrI_B.next();
				if (!dataElement.getIsSelected1()) {
					continue;
				}
				String colName = "" + getAbsoluteCurrentValue(dataElement.getTestData()) + "Ib";
				column = new TableColumn(colName);
				column.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get(previewColumns.size())));
				previewColumns.add(column);
				previewColumnCount += 1;
			}
			
			Iterator<TestSetupDataModel> itrI_Max = imaxData.iterator();
			while (itrI_Max.hasNext()) {
				TestSetupDataModel dataElement = itrI_Max.next();
				if (!dataElement.getIsSelected()) {
					continue;
				}
				String colName = "" + getAbsoluteCurrentValue(dataElement.getTestData()) + "Imax";
				column = new TableColumn<>(colName);
				column.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get(previewColumns.size())));
				previewColumns.add(column);
				previewColumnCount += 1;
			}
		} catch(Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("getPreviewColumns: Exception :" + e.getMessage());

		}

		return previewColumnCount;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private int getPreviewRows(List previewRows) {
		int previewRowCount = 0;
		try{
			Iterator<TestSetupDataModel> itr_pfABC = pfAbcData.iterator();
			String ABC_Prefix = "ABC " ;
			if(ProcalFeatureEnable.PHASE_DISPLAY_ENABLE_FEATURE){
				ABC_Prefix=ConstantApp.FIRST_PHASE_DISPLAY_NAME+ConstantApp.SECOND_PHASE_DISPLAY_NAME+ConstantApp.THIRD_PHASE_DISPLAY_NAME+" ";
			}
			while (itr_pfABC.hasNext()) {
				TestSetupDataModel dataElement = itr_pfABC.next();
				if (!dataElement.getIsSelected()) {
					continue;
				}
				List<Object> rowData = new ArrayList<Object>();

				rowData.add(ABC_Prefix + dataElement.getTestData());
				for (int i = 1; i <= testSetupPreviewTable.getColumns().size(); i++) {
					rowData.add("");
				}
				previewRows.add(rowData);
				previewRowCount += 1;
			}

			Iterator<TestSetupDataModel> itr_AorBorC = pfAbcData.iterator();
			String A_B_C_Prefix = "A/B/C " ;
			if(ProcalFeatureEnable.PHASE_DISPLAY_ENABLE_FEATURE){
				A_B_C_Prefix = ConstantApp.FIRST_PHASE_DISPLAY_NAME+"/"+ConstantApp.SECOND_PHASE_DISPLAY_NAME+"/"+ConstantApp.THIRD_PHASE_DISPLAY_NAME+" " ;

			}
			while (itr_AorBorC.hasNext()) {
				TestSetupDataModel dataElement = itr_AorBorC.next();
				if (!dataElement.getIsSelected1()) {
					continue;
				}
				List<Object> rowData = new ArrayList<Object>();

				rowData.add(A_B_C_Prefix + dataElement.getTestData());
				for (int i = 1; i <= testSetupPreviewTable.getColumns().size(); i++) {
					rowData.add("");
				}
				previewRows.add(rowData);
				previewRowCount += 1;
			}
		} catch(Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("getPreviewRows: Exception :" + e.getMessage());

		}

		return previewRowCount;
	}



	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void saveTestSetupData() {
		ApplicationLauncher.logger.info("saveToFileSystem:Entry: Storing the test setup column & row data in to file");
		ArrayList<String> tps_column_names = new ArrayList<String>();
		ArrayList<ArrayList<Object>> tps_row_data = new ArrayList<ArrayList<Object>>();
		try {
			Iterator itrColumn = testSetupPreviewTable.getColumns().iterator();
			while (itrColumn.hasNext()) {
				TableColumn obj = (TableColumn) itrColumn.next();
				tps_column_names.add(obj.getText());
			}
			setTPS_column_names(tps_column_names);
			Iterator itrItems = testSetupPreviewTable.getItems().iterator();
			while (itrItems.hasNext()) {
				Object obj = itrItems.next();
				ArrayList<Object> tps_row_values = (ArrayList<Object>) obj;
				tps_row_data.add(tps_row_values);
			}
			ApplicationLauncher.logger.info("tps_column_names: " + tps_column_names);
			ApplicationLauncher.logger.info("tps_row_data: " + tps_row_data);
			setTPS_row_data(tps_row_data);

		} catch(Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("saveToFileSystem: Exception1 :" + e.getMessage());

		}
	}

	public void setTPS_row_data(ArrayList<ArrayList<Object>> row_data){
		TPS_row_data = row_data;
	}

	public ArrayList<ArrayList<Object>> getTPS_row_data(){
		return TPS_row_data;
	}

	public void setTPS_column_names(ArrayList<String> column_values){
		TPS_column_names = column_values;
	}

	public ArrayList<String>  getTPS_column_names(){
		return TPS_column_names;
	}

	private void initTestCaseSelectionData() {
		testCaseSelectionTable.getColumns().clear();
		testCaseSelectionTable.getItems().clear();

		List<String> columnNames = new ArrayList<String>();
		List<List<Object>> rowValues = new ArrayList<List<Object>>();
		//ApplicationLauncher.logger.info("test1");
		readTestSetup(columnNames, rowValues);

		ApplicationLauncher.logger.info("columnNames: " + columnNames);
		ApplicationLauncher.logger.info("rowValues: " + rowValues);
		TableColumn<List<Object>, Object> column = new TableColumn<List<Object>, Object>("Test Point");
		column.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get(0)));
		testCaseSelectionTable.getColumns().add(column);
		//ApplicationLauncher.logger.info("test2");

		for (int i = 1; i < columnNames.size(); i++) {
			TableColumn<List<Object>, CheckBox> checkBoxColumn = new TableColumn<List<Object>, CheckBox>(columnNames.get(i));
			checkBoxColumn.setCellValueFactory(new TestCaseSelectionCheckBoxFactory(i));
			testCaseSelectionTable.getColumns().add(checkBoxColumn);
		}

		testCaseSelectionTable.getItems().setAll(FXCollections.observableList(rowValues));

	}


	@SuppressWarnings("unchecked")
	public void initTestCaseInputsData() {
		testCaseInputsTable.setEditable(true);
		testCaseNameColumn.setSortable(false);
		testCaseEminColumn.setSortable(false);
		testCaseEmaxColumn.setSortable(false);
		testCasePulseColumn.setSortable(false);
		testCasePulseAverageColumn.setSortable(false);
		testCaseTimeColumn.setSortable(false);
		testCaseSkipReadingCountColumn.setSortable(false);
		testCaseDeviationColumn.setSortable(false);

		testCaseNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

		testCaseEminColumn.setCellValueFactory(new PropertyValueFactory<TestCaseData, Float>("errorMin"));
		testCaseEminColumn.setCellFactory(EditCell.<TestCaseData, Float>forTableColumn(new MyFloatStringConverter()));
		testCaseEminColumn.setOnEditCommit(new EventHandler<CellEditEvent<TestCaseData, Float>>() {
			public void handle(CellEditEvent<TestCaseData, Float> t) {
				TestCaseData rowData = ((TestCaseData) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				try{	
					rowData.setErrorMin(t.getNewValue());
				} catch (Exception e) {
					
					e.printStackTrace();
					ApplicationLauncher.logger.error("initTestCaseInputsData: setErrorMin: Exception: " + e.getMessage());
					ApplicationLauncher.InformUser("Incorrect decimal Value", "Kindly enter valid input decimal for Emin" ,AlertType.ERROR);
					rowData.setErrorMin(t.getOldValue());
				}
			}
		});

		testCaseEmaxColumn.setCellValueFactory(new PropertyValueFactory<TestCaseData, Float>("errorMax"));
		testCaseEmaxColumn.setCellFactory(EditCell.<TestCaseData, Float>forTableColumn(new MyFloatStringConverter()));
		testCaseEmaxColumn.setOnEditCommit(new EventHandler<CellEditEvent<TestCaseData, Float>>() {
			public void handle(CellEditEvent<TestCaseData, Float> t) {
				TestCaseData rowData = ((TestCaseData) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				try{
					rowData.setErrorMax(t.getNewValue());
				} catch (Exception e) {
					
					e.printStackTrace();
					ApplicationLauncher.logger.error("initTestCaseInputsData: setErrorMax: Exception: " + e.getMessage());
					ApplicationLauncher.InformUser("Incorrect decimal Value", "Kindly enter valid input decimal for Emax" ,AlertType.ERROR);
					rowData.setErrorMax(t.getOldValue());
				}
			}
		});

		testCasePulseColumn.setCellValueFactory(new PropertyValueFactory<TestCaseData, Integer>("pulse"));
		testCasePulseColumn.setCellFactory(EditCell.<TestCaseData, Integer>forTableColumn(new MyIntegerStringConverter()));
		testCasePulseColumn.setOnEditCommit(new EventHandler<CellEditEvent<TestCaseData, Integer>>() {
			public void handle(CellEditEvent<TestCaseData, Integer> t) {
				TestCaseData rowData = ((TestCaseData) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				try{
					rowData.setPulse(t.getNewValue());
				} catch (Exception e) {
					
					e.printStackTrace();
					ApplicationLauncher.logger.error("initTestCaseInputsData: setPulse: Exception: " + e.getMessage());
					ApplicationLauncher.InformUser("Incorrect number", "Kindly enter valid input number for Pulse" ,AlertType.ERROR);
					rowData.setPulse(t.getOldValue());
				}
			}
		});

		
		testCasePulseAverageColumn.setCellValueFactory(new PropertyValueFactory<TestCaseData, Integer>("average"));
		testCasePulseAverageColumn.setCellFactory(EditCell.<TestCaseData, Integer>forTableColumn(new MyIntegerStringConverter()));
		testCasePulseAverageColumn.setOnEditCommit(new EventHandler<CellEditEvent<TestCaseData, Integer>>() {
			public void handle(CellEditEvent<TestCaseData, Integer> t) {
				TestCaseData rowData = ((TestCaseData) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				try{
					rowData.setAverage(t.getNewValue());
				} catch (Exception e) {
					
					e.printStackTrace();
					ApplicationLauncher.logger.error("initTestCaseInputsData: setAverage: Exception: " + e.getMessage());
					ApplicationLauncher.InformUser("Incorrect number", "Kindly enter valid input number for Average" ,AlertType.ERROR);
					rowData.setAverage(t.getOldValue());
				}
			}
		});
		
		
		testCaseTimeColumn.setCellValueFactory(new PropertyValueFactory<TestCaseData, Integer>("times"));
		testCaseTimeColumn.setCellFactory(EditCell.<TestCaseData, Integer>forTableColumn(new MyIntegerStringConverter()));
		testCaseTimeColumn.setOnEditCommit(new EventHandler<CellEditEvent<TestCaseData, Integer>>() {
			public void handle(CellEditEvent<TestCaseData, Integer> t) {
				TestCaseData rowData = ((TestCaseData) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				try{
					rowData.setTimes(t.getNewValue());
				} catch (Exception e) {
					
					e.printStackTrace();
					ApplicationLauncher.logger.error("initTestCaseInputsData: setTime: Exception: " + e.getMessage());
					ApplicationLauncher.InformUser("Incorrect number", "Kindly enter valid input number for Time" ,AlertType.ERROR);
					rowData.setTimes(t.getOldValue());
				}
			}
		});

		testCaseSkipReadingCountColumn.setCellValueFactory(new PropertyValueFactory<TestCaseData, Integer>("skipreadingcount"));
		testCaseSkipReadingCountColumn.setCellFactory(EditCell.<TestCaseData, Integer>forTableColumn(new MyIntegerStringConverter()));
		testCaseSkipReadingCountColumn.setOnEditCommit(new EventHandler<CellEditEvent<TestCaseData, Integer>>() {
			public void handle(CellEditEvent<TestCaseData, Integer> t) {
				TestCaseData rowData = ((TestCaseData) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				try{
					rowData.setSkipreadingcount(t.getNewValue());
				} catch (Exception e) {
					
					e.printStackTrace();
					ApplicationLauncher.logger.error("initTestCaseInputsData: setSkipreadingcount: Exception: " + e.getMessage());
					ApplicationLauncher.InformUser("Incorrect number", "Kindly enter valid input number for Skip Reading" ,AlertType.ERROR);
					rowData.setSkipreadingcount(t.getOldValue());
				}
			}
		});

		testCaseDeviationColumn.setCellValueFactory(new PropertyValueFactory<TestCaseData, Float>("deviation"));
		testCaseDeviationColumn.setCellFactory(EditCell.<TestCaseData, Float>forTableColumn(new MyFloatStringConverter()));
		testCaseDeviationColumn.setOnEditCommit(new EventHandler<CellEditEvent<TestCaseData, Float>>() {
			public void handle(CellEditEvent<TestCaseData, Float> t) {
				TestCaseData rowData = ((TestCaseData) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				try{
					rowData.setDeviation(t.getNewValue());
				} catch (Exception e) {
					
					e.printStackTrace();
					ApplicationLauncher.logger.error("initTestCaseInputsData: setDeviation: Exception: " + e.getMessage());
					ApplicationLauncher.InformUser("Incorrect number", "Kindly enter valid input number for Deviation" ,AlertType.ERROR);
					rowData.setDeviation(t.getOldValue());
				}
			}
		});

		testCaseRunTypeColumn.setCellValueFactory(new TestCaseDataComboBoxValueFactory());

	}


	@SuppressWarnings("rawtypes")
	public void onTestSelectionSaveClick() {
		ApplicationLauncher.logger.info("Saving the test case selection");
		List<String> processedTestCases = new ArrayList<String>();

		ObservableList<List<Object>> testCaseSelectionList =  testCaseSelectionTable.getItems();
		String testCaseName = "";
		Boolean testCaseSelected = false;
/*		for (List<Object> testCaseRow : testCaseSelectionList) {
			testCaseName = testCaseRow.get(0).toString();

			for (int i = 1; i < testCaseRow.size(); i++) {
				testCaseSelected = (Boolean)testCaseRow.get(i);
				if (testCaseSelected == false) {
					continue;
				}
				TableColumn column = testCaseSelectionTable.getColumns().get(i);
				String testData = column.getText();

				ApplicationLauncher.logger.info("Processing test data: " + testCaseName + "-" + testData);
				processedTestCases.add(testCaseName + "-" + testData);
			}
		}*/
		
		for (int i = 1; i < testCaseSelectionTable.getColumns().size(); i++) {
			TableColumn column = testCaseSelectionTable.getColumns().get(i);
			String testData = column.getText();
			for (List<Object> testCaseRow : testCaseSelectionList) {
				testCaseName = testCaseRow.get(0).toString();
				testCaseSelected = (Boolean)testCaseRow.get(i);
				if (testCaseSelected == false) {
					continue;
				}
				ApplicationLauncher.logger.info("Processing test data: " + testCaseName + "-" + testData);
				processedTestCases.add(testCaseName + "-" + testData);

			}

		}

		updateTestCaseInputData(processedTestCases);
		ProjectController.SaveProject();
		testDataMatrixAccordian.setExpandedPane(testCaseInputsPane);
	}

	private void updateTestCaseInputData(List<String> processedTestCases) {
		ObservableList<TestCaseData> testCaseData = FXCollections.observableArrayList();
		String runType = ConstantApp.TESTPOINT_RUNTYPE_PULSEBASED;
		
		if(ProcalFeatureEnable.RUN_TYPE_TIME_BASED_ENABLED){
			//runType = ConstantApp.TESTPOINT_RUNTYPE_TIMEBASED;
		}
		for (String testCase : processedTestCases) {
			testCaseData.add(new TestCaseData(testCase, 
					ConstantAppConfig.ERROR_MIN_DEFAULT_VALUE,
					ConstantAppConfig.ERROR_MAX_DEFAULT_VALUE,
					ConstantAppConfig.PULSES_DEFAULT_VALUE,
					ConstantAppConfig.AVERAGE_DEFAULT_VALUE,
					ConstantAppConfig.TIME_DEFAULT_VALUE,
					ConstantAppConfig.SKIP_READING_DEFAULT_VALUE, 
					ConstantAppConfig.DEVIATION_DEFAULT_VALUE,
					runType));
		}
		testCaseInputsTable.getItems().setAll(testCaseData);
		if(mCurrentNode.getType() == TestProfileType.ConstantTest){
			ApplicationLauncher.logger.info("PropertyValueUpdate : testCaseInputsTable: Not Editable");
			//testCaseInputsTable.setDisable(true); commented on proCAL-Version s4.2.0.3.1.3 , emin,emax data is required for error result calculation on ReportGenV2
		}
	}

	public void onTestDataInputSaveClick() {
		ApplicationLauncher.logger.info("Saving the test case inputs");
		SaveToNode();
		testDataMatrixAccordian.setExpandedPane(testCasePropertiesPane);
		ProjectController.SaveProject();
	}

	public void SaveToDB(){
		List<String> columnData = new ArrayList<>();
		List<String> EminData = new ArrayList<>();
		List<String> EmaxData = new ArrayList<>();
		List<String> PulsesData = new ArrayList<>();
		List<String> averageData = new ArrayList<>();
		
		List<String> TimeData = new ArrayList<>();
		List<String> SkipReadingCountData = new ArrayList<>();
		List<String> DeviationData = new ArrayList<>();
		List<String> TestRunTypeData = new ArrayList<>();
		for (TestCaseData item : testCaseInputsTable.getItems()) {
			columnData.add(testCaseNameColumn.getCellObservableValue(item).getValue());
			EminData.add(testCaseEminColumn.getCellObservableValue(item).getValue().toString());
			EmaxData.add(testCaseEmaxColumn.getCellObservableValue(item).getValue().toString());
			PulsesData.add(testCasePulseColumn.getCellObservableValue(item).getValue().toString());
			averageData.add(testCasePulseAverageColumn.getCellObservableValue(item).getValue().toString());
			
			
			TimeData.add(testCaseTimeColumn.getCellObservableValue(item).getValue().toString());
			SkipReadingCountData.add(testCaseSkipReadingCountColumn.getCellObservableValue(item).getValue().toString());
			DeviationData.add(testCaseDeviationColumn.getCellObservableValue(item).getValue().toString());
			TestRunTypeData.add(item.getTestRunType());
		}
		ApplicationLauncher.logger.info("Name Column data: " + columnData);
		String TestType = getTestType();
		String AliasID = getAliasID();
		DraggableTestNode node = getCurrentNode();
		node = setNodeProperties(node,columnData, EminData, EmaxData, PulsesData, TimeData, SkipReadingCountData, DeviationData, TestRunTypeData,averageData);


		String alias_name = "";
		String summary_display_tp_name = "";
		String aliasID = "";
		String positionID = "";
		String test_type = "";
		String project_name = "";
		String Emin = "";
		String Emax = "";
		String Pulses = "";
		String average = "";
		String Time = "";
		String SkipReadingCount = "";
		String Deviation = "";
		String testruntype = "";	
		
		
		for(int i = 0; i< columnData.size(); i++){
			ApplicationLauncher.logger.info("SaveToDB : TestType: " + TestType);
			ApplicationLauncher.logger.info("SaveToDB : AliasID: " + AliasID);
			ApplicationLauncher.logger.info("SaveToDB: Input table data: " + columnData.get(i));

			alias_name = mCurrentNode.getAliasName();
			summary_display_tp_name = alias_name+ "_" + AliasID + "-" + columnData.get(i);
			ApplicationLauncher.logger.info("SaveToDB : summary_display_tp_name: " + summary_display_tp_name);
			ApplicationLauncher.logger.info("Node Title: " + TestType + "-" + columnData.get(i));
			ApplicationLauncher.logger.info("Node AliasID: " + AliasID);
			aliasID = AliasID;
			positionID = Integer.toString(node.getPositionId());
			test_type = getTestType();
			project_name = node.getProjectName();
			Emin = EminData.get(i);
			Emax = EmaxData.get(i);
			Pulses = PulsesData.get(i);
			average = averageData.get(i);
			Time = TimeData.get(i);
			SkipReadingCount = SkipReadingCountData.get(i);
			Deviation = DeviationData.get(i);
			testruntype = TestRunTypeData.get(i);
			ApplicationLauncher.logger.info("Node test_type: " + test_type);

			if(GuiUtils.FormatErrorInput(Emin)!=null){
				if(GuiUtils.FormatErrorInput(Emax)!=null){
					if(GuiUtils.is_number(Pulses)){
						if(GuiUtils.validateAvgPulses(average)){
						if(GuiUtils.is_number(Time)){
							if(GuiUtils.is_number(SkipReadingCount)){
								if(GuiUtils.is_float(Deviation)){
									//if(test_type.equals("PhaseReversal")){
									if(test_type.equals(TestProfileType.PhaseReversal.toString())){
										ApplicationLauncher.logger.info("PhaseReversal Entry");
										String phaserev_norm = ConstantApp.PHASEREVERSAL_NORMAL_ALIAS_NAME;
										String normal_phaserev_name = phaserev_norm+ "_" + AliasID + "-" + columnData.get(i);

										MySQL_Controller.sp_add_project_components(project_name,normal_phaserev_name,test_type, aliasID, positionID, 
												Time , "", "", "","","","", 
												Emin, Emax, Pulses, SkipReadingCount,Deviation,testruntype,"","","",
												"","","","","",
												"","","","","",
												"","","",average);
										ProjectController.UpdateNewTestPointSummaryDataToDB(node.getProjectName(), normal_phaserev_name , node.getType().toString(),  AliasID);

										MySQL_Controller.sp_add_project_components(project_name,summary_display_tp_name,test_type, aliasID, positionID, 
												Time , "", "", "","","","", 
												Emin, Emax, Pulses, SkipReadingCount,Deviation,testruntype,"","","",
												"","","","","",
												"","","","","",
												"","","",average);
										ProjectController.UpdateNewTestPointSummaryDataToDB(node.getProjectName(), summary_display_tp_name , node.getType().toString(),  AliasID);
									}
									else{
										MySQL_Controller.sp_add_project_components(project_name,summary_display_tp_name,test_type, aliasID, positionID, 
												Time , "", "", "","","","", 
												Emin, Emax, Pulses, SkipReadingCount,Deviation,testruntype,"","","",
												"","","","","",
												"","","","","",
												"","","",average);
										ProjectController.UpdateNewTestPointSummaryDataToDB(node.getProjectName(), summary_display_tp_name , node.getType().toString(),  AliasID);
									}
								}
							}
						}
					}
					}
				}
			}
		}	
	}

	public void SaveToNode(){
		List<String> columnData = new ArrayList<>();
		List<String> EminData = new ArrayList<>();
		List<String> EmaxData = new ArrayList<>();
		List<String> PulsesData = new ArrayList<>();
		List<String> averageData = new ArrayList<>();
		List<String> TimeData = new ArrayList<>();
		List<String> SkipReadingCountData = new ArrayList<>();
		List<String> DeviationData = new ArrayList<>();
		List<String> TestRunTypeData = new ArrayList<>();
		for (TestCaseData item : testCaseInputsTable.getItems()) {
			columnData.add(testCaseNameColumn.getCellObservableValue(item).getValue());
			EminData.add(testCaseEminColumn.getCellObservableValue(item).getValue().toString());
			EmaxData.add(testCaseEmaxColumn.getCellObservableValue(item).getValue().toString());
			PulsesData.add(testCasePulseColumn.getCellObservableValue(item).getValue().toString());
			averageData.add(testCasePulseAverageColumn.getCellObservableValue(item).getValue().toString());
			
			
			TimeData.add(testCaseTimeColumn.getCellObservableValue(item).getValue().toString());
			SkipReadingCountData.add(testCaseSkipReadingCountColumn.getCellObservableValue(item).getValue().toString());
			DeviationData.add(testCaseDeviationColumn.getCellObservableValue(item).getValue().toString());
			TestRunTypeData.add(item.getTestRunType());
		}
		ApplicationLauncher.logger.info("Name Column data: " + columnData);
		getTestType();
		getAliasID();
		DraggableTestNode node = getCurrentNode();
		node = setNodeProperties(node,columnData, EminData, EmaxData, PulsesData, TimeData, SkipReadingCountData, DeviationData, TestRunTypeData,averageData);
	}

	public DraggableTestNode setNodeProperties(DraggableTestNode node, List<String> columnData, 
			List<String> eminData, List<String> emaxData, 
			List<String> pulsesData, List<String> timeData, 
			List<String> SkipReadingCountData, List<String> deviationData,
			List<String> TestRunTypeData, List<String> averageData){
		node.NewNode(false);
		node.setTestCaseNames(columnData);
		node.setEmin_list(eminData);
		node.setEmax_list(emaxData);
		node.setPulses_list(pulsesData);
		node.setTime_list(timeData);
		node.setSkipReadingCount_list(SkipReadingCountData);
		node.setDeviation_list(deviationData);
		node.setTestRunType_list(TestRunTypeData);
		node.setAverage_list(averageData);
		return node;
	}



	public void PropertyValueUpdate(String TestType, String AliasID, DraggableTestNode SelectedNode){
		ApplicationLauncher.logger.info("PropertyValueUpdate entry");
		Test_type = TestType;
		Alias_ID = AliasID;
		mCurrentNode = SelectedNode;
		ApplicationLauncher.logger.info("Test_type:" + Test_type);

		String project_name = mCurrentNode.getProjectName();
		JSONObject test_setup_data = MySQL_Controller.sp_gettest_point_setup(project_name);
		ApplicationLauncher.logger.info("test_setup_data: " + test_setup_data);
		ApplicationLauncher.logger.info("condition: "+ test_setup_data.isNull("imax"));
		try {
			if(test_setup_data.isNull("imax")){//Gopi...Why only Imax?
				initTestSetup();
				loadIMappingDefaultData();
				loadPF_MappingDefaultData();//PFMappingToDO
			}
			else{
				load_saved_data();
				initTestSetup();
				SetTestSetupData(test_setup_data);
				onTestSetupPreviewClick();
				ArrayList<String> previous_test_setup_data = FetchData();
				setPreviousTP_setup(previous_test_setup_data);
			}
		} catch (JSONException e1) {
			
			e1.printStackTrace();
			ApplicationLauncher.logger.error("PropertyValueUpdate: JSONException1: " + e1.getMessage());
		}

		ApplicationLauncher.logger.info("PropertyValueUpdate: mCurrentNode.IsNewNode(): " + mCurrentNode.IsNewNode());
		if(!mCurrentNode.IsNewNode()){
			String test_name = mCurrentNode.getTitle();
			String aliasId = mCurrentNode.getAliasId();
			ApplicationLauncher.logger.info("properties: " + project_name + " " +test_name +" " + aliasId);

			JSONObject testdetailslist = MySQL_Controller.sp_getproject_components(project_name,TestType , aliasId);
			ApplicationLauncher.logger.info("testdetailslist: " + testdetailslist);
			try {
				settestcaseInputTable(testdetailslist);
				ApplicationLauncher.logger.info("project_name: " + project_name);
				ArrayList<String> selected_row_list = new ArrayList<String>(); 
				ArrayList<String> selected_col_list = new ArrayList<String>(); 
				ObservableList<TestCaseData> testdetails = testCaseInputsTable.getItems();
				selected_checkbox(testdetails, selected_row_list, selected_col_list);
				setSelectionTable(selected_row_list, selected_col_list);
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("PropertyValueUpdate: JSONException2: " + e.getMessage());
			}

		}

		if(mCurrentNode.getType() == TestProfileType.ConstantTest){
			ApplicationLauncher.logger.info("PropertyValueUpdate : testCaseInputsTable: Not Editable");
			testCaseInputsTable.setDisable(true);
		}

	}

	private void loadIMappingSavedData() {
		ArrayList<String> savedvalues = new ArrayList<String>();
		ref_listview_currentlist.getItems().clear();


		String project_name = ProjectController.getProjectName();
		JSONObject I_mapping_values = MySQL_Controller.sp_gettp_setup_i_user_data_mapping(project_name);
		ApplicationLauncher.logger.debug("loadIMappingSavedData: I_mapping_values: " + I_mapping_values);
		try {
			ApplicationLauncher.logger.debug("loadIMappingSavedData: I_mapping_values: " + I_mapping_values.getInt("No_of_I_mappings"));

			if(I_mapping_values.getInt("No_of_I_mappings") != 0){
				JSONArray I_mapping_values_arr = I_mapping_values.getJSONArray("I_mapping_values");
				JSONObject jobj = new JSONObject();
				String i_value = "";
				for(int i=0; i<I_mapping_values_arr.length(); i++){
					jobj = I_mapping_values_arr.getJSONObject(i);
					jobj.getInt("i_serial_no");
					i_value = jobj.getString("current_mapping_value");
					savedvalues.add(i_value);
				}
			}
			else{
				loadIMappingDefaultData();

			}

		} catch (JSONException e) {
			ApplicationLauncher.logger.error("loadIMappingSavedData: JSONException1" + e.getMessage());
			e.printStackTrace();
		}


		ref_listview_currentlist.getItems().addAll(savedvalues);
	}

	private void  loadPF_MappingSavedData(){
		ArrayList<String> savedvalues = new ArrayList<String>();
		ref_listview_pf_list.getItems().clear();


		String project_name = ProjectController.getProjectName();
		JSONObject PF_mapping_values = MySQL_Controller.sp_gettp_setup_pf_user_data_mapping(project_name);
		ApplicationLauncher.logger.info("loadPF_MappingSavedData: PF_mapping_values: " + PF_mapping_values);
		try {
			ApplicationLauncher.logger.info("loadPF_MappingSavedData: PF_mapping_values: " + PF_mapping_values.getInt("No_of_PF_mappings"));

			if(PF_mapping_values.getInt("No_of_PF_mappings") != 0){
				JSONArray PF_mapping_values_arr = PF_mapping_values.getJSONArray("PF_mapping_values");
				JSONObject jobj = new JSONObject();
				String pf_value = "";
				for(int i=0; i<PF_mapping_values_arr.length(); i++){
					jobj = PF_mapping_values_arr.getJSONObject(i);
					jobj.getInt("pf_serial_no");
					pf_value = jobj.getString("pf_mapping_value");
					savedvalues.add(pf_value);
				}
			}
			else{

				loadPF_MappingDefaultData();
			}

		} catch (JSONException e) {
			ApplicationLauncher.logger.error("loadPF_MappingSavedData: JSONException" + e.getMessage());
			e.printStackTrace();
		}


		ref_listview_pf_list.getItems().addAll(savedvalues);
	}


	public void loadIMappingDefaultData(){
		ArrayList<String> defaultvalues = new ArrayList<String>();
		ref_listview_currentlist.getItems().clear();
		defaultvalues.addAll(ConstantAppConfig.I_MAPPING_DEFAULT_VALUES);
		ref_listview_currentlist.getItems().addAll(defaultvalues);
	}

	public void loadPF_MappingDefaultData(){
		ApplicationLauncher.logger.debug("loadPF_MappingDefaultData: Entry" );
		ArrayList<String> defaultvalues = new ArrayList<String>();
		ref_listview_pf_list.getItems().clear();
		defaultvalues.addAll(ConstantAppConfig.PF_MAPPING_DEFAULT_VALUES);
		ref_listview_pf_list.getItems().addAll(defaultvalues);
		ApplicationLauncher.logger.debug("loadPF_MappingDefaultData: PF_MAPPING_DEFAULT_VALUES:"+ConstantAppConfig.PF_MAPPING_DEFAULT_VALUES );
	}

	public void settestcaseInputTable(JSONObject testdetailslist) throws JSONException{
		ObservableList<TestCaseData> testCaseData = FXCollections.observableArrayList();
		JSONArray testcases = testdetailslist.getJSONArray("test_details");
		ApplicationLauncher.logger.info("testcases: " + testcases);
		String testtype = mCurrentNode.getType().toString();
		ArrayList<String> existingTests = new ArrayList<String>();
		
		JSONObject test = new JSONObject();
		String testcaseName = "";
		Float emin = 0f;
		Float emax = 0f;
		int pulse = 0;
		int average = 0;
		int time = 0 ;
		int SkipReadingCount = 0;
		Float deviation = 0f;
		String testruntype = "";
		
		for(int i = 0; i < testcases.length();i++){
			boolean testexists = false;
			test = testcases.getJSONObject(i);
			testcaseName = test.getString("test_case_name");
			if(!testcaseName.contains(ConstantApp.REPEATABILITY_START_TEST_ALIAS_NAME) &&
					!testcaseName.contains(ConstantApp.REPEATABILITY_END_TEST_ALIAS_NAME) &&
					!testcaseName.contains(ConstantApp.SELF_HEATING_START_TEST_ALIAS_NAME) &&
					!testcaseName.contains(ConstantApp.SELF_HEATING_END_TEST_ALIAS_NAME)){

				ApplicationLauncher.logger.info("testcaseName: " + testcaseName);
				emin = Float.parseFloat(test.getString("inf_emin"));
				emax = Float.parseFloat(test.getString("inf_emax"));
				pulse = Integer.parseInt(test.getString("inf_pulses"));
				average = Integer.parseInt(test.getString("inf_average"));				
				time = Integer.parseInt(test.getString("time_duration"));
				SkipReadingCount = Integer.parseInt(test.getString("skip_reading_count"));
				deviation = Float.parseFloat(test.getString("inf_deviation"));
				testruntype = test.getString("testruntype");
				testcaseName = testcaseName.substring(testcaseName.indexOf("-")+1);
				if(testcaseName.contains("U")){
					testcaseName = testcaseName.substring(testcaseName.indexOf("-")+1);
				}
/*				if((testtype.equals("InfluenceFreq")) ||  
						(testtype.equals("ConstantTest")) ||
						(testtype.equals("Repeatability")) ||
						(testtype.equals("SelfHeating") ||*/
				if((testtype.equals(TestProfileType.InfluenceFreq.toString())) ||  
						(testtype.equals(TestProfileType.ConstantTest.toString())) ||
						(testtype.equals(TestProfileType.Repeatability.toString())) ||
						(testtype.equals(TestProfileType.SelfHeating.toString()) ||				
								(testtype.equals(TestProfileType.InfluenceHarmonic.toString()))) ){
					int pos = testcaseName.lastIndexOf("-");
					String testname_without_freq = testcaseName.substring(0,pos);
					ApplicationLauncher.logger.info("settestcaseInputTable: testname_without_freq: " + testname_without_freq);
					ApplicationLauncher.logger.info("existingTests: " + existingTests);
					if(existingTests.size() == 0){
						testexists = false;
					}
					else{
						for(int j=0; j<existingTests.size(); j++){
							if(existingTests.get(j).equals(testname_without_freq)){
								ApplicationLauncher.logger.info("settestcaseInputTable: testexists true: testname_without_freq: " + testname_without_freq);
								testexists = true;
								break;
							}
							else{
								ApplicationLauncher.logger.info("settestcaseInputTable: testexists false: testname_without_freq: " + testname_without_freq);
								testexists = false;
							}
						}
					}
					if(!testexists){
						existingTests.add(testname_without_freq);
						testCaseData.add(new TestCaseData(testname_without_freq, emin,emax , pulse,average, time, SkipReadingCount, deviation,testruntype));
					}
				}
				//else if((testtype.equals("InfluenceHarmonic"))){
				else if((testtype.equals(TestProfileType.InfluenceHarmonic.toString()))){	
					
					int pos = testcaseName.indexOf("_");
					String testname_without_aliasname = testcaseName.substring(pos+1);
					ApplicationLauncher.logger.info("settestcaseInputTable: testname_without_aliasname: " + testname_without_aliasname);
					ApplicationLauncher.logger.info("existingTests: " + existingTests);
					if(existingTests.size() == 0){
						testexists = false;
					}
					else{
						for(int j=0; j<existingTests.size(); j++){
							if(existingTests.get(j).equals(testname_without_aliasname)){
								ApplicationLauncher.logger.info("settestcaseInputTable: testexists true: testname_without_freq: " + testname_without_aliasname);
								testexists = true;
								break;
							}
							else{
								ApplicationLauncher.logger.info("settestcaseInputTable: testexists false: testname_without_freq: " + testname_without_aliasname);
								testexists = false;
							}
						}
					}
					if(!testexists){
						existingTests.add(testname_without_aliasname);
						testCaseData.add(new TestCaseData(testcaseName, emin,emax , pulse,average, time, SkipReadingCount, deviation,testruntype));
					}
				}
				else{
					testCaseData.add(new TestCaseData(testcaseName, emin,emax , pulse,average, time, SkipReadingCount, deviation,testruntype));
				}	
			}	
		}
		ApplicationLauncher.logger.info("testCaseData: " + testCaseData);
		testCaseInputsTable.getItems().setAll(testCaseData);
	}

	public void selected_checkbox(ObservableList<TestCaseData> testdetails, ArrayList<String> selected_row_list,
			ArrayList<String> selected_col_list) throws JSONException{

		//TestCaseData test = new TestCaseData();
		String testcaseName = "";
		//String[] split_test_case = [];
		String row = "";
		String col = "";
		
		for(int i = 0; i < testdetails.size(); i++){
			TestCaseData test = testdetails.get(i);
			testcaseName = test.getName();
			ApplicationLauncher.logger.info("testcaseName: " + testcaseName );
			String[] split_test_case = testcaseName.split("-");
			row = split_test_case[0];
			col = split_test_case[1];
			ApplicationLauncher.logger.info("row:" + row);
			ApplicationLauncher.logger.info("col:" + col);
			selected_row_list.add(row);
			selected_col_list.add(col);
			ApplicationLauncher.logger.info("selected values: " + selected_row_list + selected_col_list);
		}

	}

	public void SetTestSetupData(JSONObject test_setup_data) throws JSONException{
		ArrayList<String> imax_selection_data = new ArrayList<String>();
		ArrayList<String> ib_selection_data = new ArrayList<String>();
		ArrayList<String> abc_selection_data = new ArrayList<String>();
		ArrayList<String> a_b_c_selection_data = new ArrayList<String>();
		ApplicationLauncher.logger.info("test_setup_data: " +test_setup_data);

		JSONObject imax_json = test_setup_data.getJSONObject("imax");
		JSONObject ib_json = test_setup_data.getJSONObject("ib");
		JSONObject abc_json = test_setup_data.getJSONObject("abc");
		JSONObject a_b_c_json = test_setup_data.getJSONObject("a_b_c");
		String i_name ="";
		for(int i=0; i< ConstantAppConfig.I_MAPPING_SIZE; i++){
			i_name = "imax_" + Integer.toString(i+1);
			imax_selection_data.add(imax_json.getString(i_name));
		}

		for(int i=0; i< ConstantAppConfig.I_MAPPING_SIZE; i++){
			i_name = "ib_" + Integer.toString(i+1);
			ib_selection_data.add(ib_json.getString(i_name));
		}
		String pf_name="";
		for(int i=0; i< ConstantAppConfig.PF_MAPPING_SIZE; i++){
			pf_name = "abc_" + Integer.toString(i+1);
			abc_selection_data.add(abc_json.getString(pf_name));
		}


		for(int i=0; i< ConstantAppConfig.PF_MAPPING_SIZE; i++){
			pf_name = "a_b_c_" + Integer.toString(i+1);
			a_b_c_selection_data.add(a_b_c_json.getString(pf_name));
		}




		ApplicationLauncher.logger.info("imax_selection_data :" +imax_selection_data);
		ApplicationLauncher.logger.info("ib_selection_data :" +ib_selection_data);
		ApplicationLauncher.logger.info("abc_selection_data :" +abc_selection_data);
		ApplicationLauncher.logger.info("a_b_c_selection_data :" +a_b_c_selection_data);

		ArrayList<String> columns = new ArrayList<String>();
		List<List<Object>> rows = new ArrayList<List<Object>>();
		try{
			columns.add("PF/I");
			int index = 0;
			Iterator<TestSetupDataModel> itrImax1 = imaxData.iterator();
			/*int index = 0;
			while (itrImax1.hasNext()) {
				TestSetupDataModel dataElement = itrImax1.next();
				if(imax_selection_data.get(index).equals("T")){
					ApplicationLauncher.logger.info("Selected case entry");
					dataElement.setIsSelected(true);
					add_imax_col_names(index, columns);
				}			
				else{
					dataElement.setIsSelected(false);
				}

				index = index + 1;
			}*/


			Iterator<TestSetupDataModel> itrIb = imaxData.iterator();
			index = 0;
			while (itrIb.hasNext()) {
				TestSetupDataModel dataElement = itrIb.next();
				if(ib_selection_data.get(index).equals("T")){
					dataElement.setIsSelected1(true);
					add_ibase_col_names(index, columns);
				}			
				else{
					dataElement.setIsSelected1(false);
				}
				index = index + 1;
			}
			index = 0;
			while (itrImax1.hasNext()) {
				TestSetupDataModel dataElement = itrImax1.next();
				if(imax_selection_data.get(index).equals("T")){
					ApplicationLauncher.logger.info("Selected case entry");
					dataElement.setIsSelected(true);
					add_imax_col_names(index, columns);
				}			
				else{
					dataElement.setIsSelected(false);
				}

				index = index + 1;
			}
			setColumn_names(columns);
			Iterator<TestSetupDataModel> itr_ABC = pfAbcData.iterator();
			index = 0;
			while (itr_ABC.hasNext()) {
				TestSetupDataModel dataElement = itr_ABC.next();
				if(abc_selection_data.get(index).equals("T")){
					dataElement.setIsSelected(true);
					ApplicationLauncher.logger.info("SetTestSetupData1: index:" + index);
					add_abc_row_values(index, rows);
				}			
				else{
					dataElement.setIsSelected(false);
				}

				index = index + 1;
			}

			Iterator<TestSetupDataModel> itr_AorBorC = pfAbcData.iterator();
			index = 0;
			while (itr_AorBorC.hasNext()) {
				TestSetupDataModel dataElement = itr_AorBorC.next();
				if(a_b_c_selection_data.get(index).equals("T")){
					dataElement.setIsSelected1(true);
					ApplicationLauncher.logger.info("SetTestSetupData2: index:" + index);
					add_a_b_c_row_values(index, rows);
				}			
				else{
					dataElement.setIsSelected1(false);
				}
				index = index + 1;
			}
			setRow_names(rows);
		} catch(Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("SetTestSetupData: Exception :" + e.getMessage());

		} 

	}

	public void setSelectionTable(ArrayList<String> selected_row_list, ArrayList<String>  selected_col_list){
		testCaseSelectionTable.getColumns().clear();
		testCaseSelectionTable.getItems().clear();

		List<String> columnNames = new ArrayList<String>();
		List<List<Object>> rowValues = new ArrayList<List<Object>>();
		columnNames = getColumn_names();
		rowValues = getRow_names();

		ApplicationLauncher.logger.info("setSelectionTable: Selection Table>>>columnNames: " + columnNames);
		ApplicationLauncher.logger.info("setSelectionTable: Selection Table>>>rowValues: " + rowValues);

		TableColumn<List<Object>, Object> column = new TableColumn<List<Object>, Object>("Test Point");
		column.setSortable(false);
		column.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get(0)));
		testCaseSelectionTable.getColumns().add(column);

		for (int i = 1; i < columnNames.size(); i++) {
			TableColumn<List<Object>, CheckBox> checkBoxColumn = new TableColumn<List<Object>, CheckBox>(columnNames.get(i));
			checkBoxColumn.setSortable(false);
			checkBoxColumn.setCellValueFactory(new TestCaseSelectionCheckBoxFactory(i));
			testCaseSelectionTable.getColumns().add(checkBoxColumn);
		}

		testCaseSelectionTable.getItems().setAll(FXCollections.observableList(rowValues));
		set_Selected_values(selected_row_list, selected_col_list);
	}

	@SuppressWarnings("rawtypes")
	public void set_Selected_values(ArrayList<String> selected_row_list, ArrayList<String>  selected_col_list){
		new ArrayList<String>();

		ApplicationLauncher.logger.info("selected_row_list: " + selected_row_list);
		ApplicationLauncher.logger.info("selected_col_list" + selected_col_list);
		ObservableList<List<Object>> testCaseSelectionList =  testCaseSelectionTable.getItems();
		for (List<Object> testCaseRow : testCaseSelectionList) {
			ApplicationLauncher.logger.info("selected_row_list.size(): " + selected_row_list.size());
			ApplicationLauncher.logger.info("Current Row : testCaseRow: " + testCaseRow.get(0).toString());
			String testCaseName = "";
			String testData = "";
			for(int i = 0; i<selected_row_list.size(); i++){
				testCaseName = testCaseRow.get(0).toString();
				ApplicationLauncher.logger.info("testCaseName: " + testCaseName);				
				if(testCaseName.equals(selected_row_list.get(i))){
					ApplicationLauncher.logger.info("selected_row_list: " + selected_row_list.get(i) );
					for(int j =1; j <getColumn_names().size(); j++){
						TableColumn column = testCaseSelectionTable.getColumns().get(j);
						testData = column.getText();
						ApplicationLauncher.logger.info("testData: " + testData );
						ApplicationLauncher.logger.info("selected_col_list: "+ selected_col_list.get(i));
						if(testData.equals(selected_col_list.get(i))){
							testCaseRow.set(j, true);
						}
					}
				}
			}
		}


	}

	public ArrayList<String> add_imax_col_names(int index, ArrayList<String> columnnames){



		ArrayList<String> i_mapped_names = get_I_Mappped_Values();
		float mapped_value = 0f;
		String imax_value = "";
		for(int i=0; i<i_mapped_names.size() ; i++){
			if(index == i){
				mapped_value = (Float.parseFloat(i_mapped_names.get(i)))/100;
				imax_value = mapped_value + "Imax";
				columnnames.add(imax_value);
			}
		}


		return columnnames;
	}

	public ArrayList<String> add_ibase_col_names(int index, ArrayList<String> columnnames){


		ArrayList<String> i_mapped_names = get_I_Mappped_Values();
		float mapped_value = 0f;
		String ib_value = "";
		for(int i=0; i<i_mapped_names.size() ; i++){
			if(index == i){
				mapped_value = (Float.parseFloat(i_mapped_names.get(i)))/100;
				ib_value = mapped_value + "Ib";
				columnnames.add(ib_value);
			}
		}


		return columnnames;
	}

	public List<List<Object>> add_abc_row_values(int index, List<List<Object>> rows){
		List<Object> row_value = new ArrayList<Object>();

		ArrayList<String> row_names = get_PF_Mappped_Values();
		for (int i = 0 ; i<row_names.size() ; i++){
			if(index == i){
				row_names.add(row_names.get(i));
			}
		}

		int count = getColumn_names().size() -1;
		for(int i=0; i<(ConstantAppConfig.PF_MAPPING_SIZE-1); i++){ //Gopi: why this is 6?

			if(index == i){
				row_value.add(row_names.get(i));
				for(int j=0; j<count ; j++){
					row_value.add(false);
				}
				rows.add(row_value);
			}
		}


		return rows;
	}

	public List<List<Object>> add_a_b_c_row_values(int index, List<List<Object>> rows){
		List<Object> row_value0 = new ArrayList<Object>();
		List<Object> row_value1 = new ArrayList<Object>();
		List<Object> row_value2 = new ArrayList<Object>();
		List<List<String>> row_names = new ArrayList<List<String>>();
		List<List<String>> rowHeaderData = new ArrayList<List<String>>();
		String FirstPhaseDisplayName = "A";
		String SecondPhaseDisplayName = "B";
		String ThirdPhaseDisplayName = "C";
		if(ProcalFeatureEnable.PHASE_DISPLAY_ENABLE_FEATURE){
			FirstPhaseDisplayName = ConstantApp.FIRST_PHASE_DISPLAY_NAME;
			SecondPhaseDisplayName = ConstantApp.SECOND_PHASE_DISPLAY_NAME;
			ThirdPhaseDisplayName = ConstantApp.THIRD_PHASE_DISPLAY_NAME;
		}


		ArrayList<String> row_namesHeader = get_PF_Mappped_Values();
		for (int i = 0 ; i<row_namesHeader.size() ; i++){

			rowHeaderData.add(Arrays.asList(FirstPhaseDisplayName+":"+row_namesHeader.get(i), 
					SecondPhaseDisplayName+":"+row_namesHeader.get(i),
					ThirdPhaseDisplayName+":"+row_namesHeader.get(i)));

		}


		row_names.addAll( rowHeaderData);

		int count = getColumn_names().size() - 1;
		for(int i=0; i<ConstantAppConfig.PF_MAPPING_SIZE; i++){ 
			if(index == i){
				row_value0.add(row_names.get(i).get(0));
				row_value1.add(row_names.get(i).get(1));
				row_value2.add(row_names.get(i).get(2));
				for(int j=0; j<count ; j++){
					row_value0.add(false);
					row_value1.add(false);
					row_value2.add(false);
				}
				rows.add(row_value0);
				rows.add(row_value1);
				rows.add(row_value2);
			}
		}



		ApplicationLauncher.logger.info("add_a_b_c_row_values: rows: "  + rows);
		return rows;
	}

	public String getTestType(){
		return Test_type;
	}

	public String getAliasID(){
		return Alias_ID;
	}

	public DraggableTestNode getCurrentNode(){
		return mCurrentNode;
	}


	private void readTestSetup(List<String> columns, List<List<Object>> rows) {
		ApplicationLauncher.logger.info("Reading the test setup column & row data fromfile");

		try {
			String tps_columns = getTPS_column_names().toString();
			fillColumnNames(columns, tps_columns);
			String tps_rows = "";
			for(int i=0; i<getTPS_row_data().size();i++){
				tps_rows = getTPS_row_data().get(i).toString();
				fillRows(rows, tps_rows);
			}
		} catch(Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("readTestSetup: Exception1: " + e.getMessage());

		}
	}

	private void fillColumnNames(List<String> columns, String dataLine) {
		dataLine = dataLine.substring(dataLine.indexOf("[")+1, dataLine.lastIndexOf("]")).trim();
		String[] columnNames = dataLine.split(",");
		for (String colName : columnNames) {
			columns.add(colName.trim());
		}
		ApplicationLauncher.logger.info("Processed column data = " + columns);
	}

	private void fillRows(List<List<Object>> rows, String dataLine) {
		dataLine = dataLine.substring(dataLine.indexOf("[")+1, dataLine.lastIndexOf("]")).trim();
		ApplicationLauncher.logger.info("fillRows :dataLine :" + dataLine);
		String[] rowValueArr = dataLine.split(",");


		String testCaseName = rowValueArr[0].trim();
		String beginValidationABC = "ABC";
		String beginValidationA_B_C = "A/B/C";

		if (ProcalFeatureEnable.PHASE_DISPLAY_ENABLE_FEATURE){
			beginValidationABC  = ConstantApp.FIRST_PHASE_DISPLAY_NAME+ConstantApp.SECOND_PHASE_DISPLAY_NAME+ConstantApp.THIRD_PHASE_DISPLAY_NAME;
			beginValidationA_B_C = ConstantApp.FIRST_PHASE_DISPLAY_NAME+"/"+ConstantApp.SECOND_PHASE_DISPLAY_NAME+"/"+ConstantApp.THIRD_PHASE_DISPLAY_NAME;
		}
		if (testCaseName.startsWith(beginValidationABC)) {
			List<Object> rowValues = new ArrayList<Object>();
			testCaseName = testCaseName.substring(3).trim();

			rowValues.add(testCaseName);
			for (int i = 1; i < rowValueArr.length; i++) {
				rowValues.add(Boolean.FALSE);
			}

			ApplicationLauncher.logger.info("Processed row data 1 = " + rowValues);
			rows.add(rowValues);
		}else if (testCaseName.startsWith(beginValidationA_B_C)) {
			String testCaseOrigName = testCaseName.substring(5).trim();
			String[] testCasePrefixArr = testCaseName.substring(0, 5).trim().split("/");

			for (int i = 0; i < testCasePrefixArr.length; i++) {
				List<Object> rowValues = new ArrayList<Object>();

				rowValues.add(testCasePrefixArr[i]+ ":" + testCaseOrigName);
				for (int k = 1; k < rowValueArr.length; k++) {
					rowValues.add(Boolean.FALSE);
				}

				ApplicationLauncher.logger.info("Processed row data = " + rowValues);
				rows.add(rowValues);
			}
		}

	}


	public void expandPropertiesPane(Parent propertiesFxmlNode, Boolean allpane) {

		if ((propertiesFxmlNode != null) && (!allpane)) {
			((AnchorPane) testCasePropertiesPane.getContent()).getChildren().clear();
			((AnchorPane) testCasePropertiesPane.getContent()).getChildren().add(propertiesFxmlNode);

			testCaseSetupIMappingPane.setDisable(true);
			testCaseSetupPane.setDisable(true);
			testCaseSelectionPane.setDisable(true);
			testCaseInputsPane.setDisable(true);

			testCasePropertiesPane.setDisable(false);
			testDataMatrixAccordian.setExpandedPane(testCasePropertiesPane);
		}
		else if((propertiesFxmlNode != null) && (allpane)){
			((AnchorPane) testCasePropertiesPane.getContent()).getChildren().clear();
			((AnchorPane) testCasePropertiesPane.getContent()).getChildren().add(propertiesFxmlNode);
			testCasePropertiesPane.setDisable(false);

			testCaseSetupPane.setDisable(false);
			testCaseSelectionPane.setDisable(false);
			testCaseInputsPane.setDisable(false);
			loadIMappingSavedData();
			loadPF_MappingSavedData();
			SaveI_AndPF_MappingOnClick();
			testDataMatrixAccordian.setExpandedPane(testCaseSetupPane);
		}
		else if((propertiesFxmlNode == null) && (allpane)){
			((AnchorPane) testCasePropertiesPane.getContent()).getChildren().clear();
			testCaseSetupIMappingPane.setDisable(true);
			testCaseSetupPane.setDisable(true);
			testCaseSelectionPane.setDisable(true);
			testCaseInputsPane.setDisable(true);

			testCasePropertiesPane.setDisable(true);
			//testDataMatrixAccordian.setExpandedPane(testCasePropertiesPane);
		}
		else {
			((AnchorPane) testCasePropertiesPane.getContent()).getChildren().clear();
			testCasePropertiesPane.setDisable(true);

			testCaseSetupPane.setDisable(false);
			testCaseSelectionPane.setDisable(false);
			testCaseInputsPane.setDisable(false);
			loadIMappingDefaultData();
			loadPF_MappingDefaultData();
			SaveI_AndPF_MappingOnClick();
			testDataMatrixAccordian.setExpandedPane(testCaseSetupPane);
		}
	}

	public void AddCurrentOnClick(){
		ObservableList<String> current =  ref_listview_currentlist.getItems();
		if(current.size()< ConstantAppConfig.I_MAPPING_SIZE){
			String i_current = ref_txt_current_input.getText();
			if(!checkdataexists(i_current) && (!i_current.isEmpty())){
				if(GuiUtils.is_number(i_current)){
					ref_listview_currentlist.getItems().add(i_current);
					ref_txt_current_input.clear();
					ref_txt_current_input.requestFocus();
				}
			}
		}
	}

	public void AddPF_MappingOnClick(){
		ObservableList<String> current =  ref_listview_pf_list.getItems();
		if(current.size()< ConstantAppConfig.PF_MAPPING_SIZE){
			String pf_value = ref_txt_pf_input.getText();
			if(!checkPF_MAppingdataexists(pf_value) && (!pf_value.isEmpty())){
				if(GuiUtils.is_float(pf_value)){
					if(Float.parseFloat(pf_value) > ConstantApp.PF_MIN && 
							Float.parseFloat(pf_value) <= ConstantApp.PF_MAX){
						if(!(Float.parseFloat(pf_value)==1.0f) ){
							if(radioBtn_Lag.isSelected() && (!checkPF_MAppingdataexists(pf_value+ConstantApp.PF_LAG))){
								pf_value= pf_value + ConstantApp.PF_LAG;
								ref_listview_pf_list.getItems().add(pf_value);
								ref_txt_pf_input.clear();
								ref_txt_pf_input.requestFocus();
							}else if(radioBtn_Lead.isSelected() && (!checkPF_MAppingdataexists(pf_value+ConstantApp.PF_LEAD))){
								pf_value= pf_value + ConstantApp.PF_LEAD;
								ref_listview_pf_list.getItems().add(pf_value);
								ref_txt_pf_input.clear();
								ref_txt_pf_input.requestFocus();
							}
						}else{
							ref_listview_pf_list.getItems().add("1.0");
							ref_txt_pf_input.clear();
							ref_txt_pf_input.requestFocus();
						}

					}

				}else{
					ApplicationLauncher.logger.info ("AddPF_MappingOnClick:  Test point data save failed. Data entered is not a valid float: "+ pf_value);
					ApplicationLauncher.InformUser("Incorrect float Value", "Test point data save failed: Kindly enter valid float input for PF" ,AlertType.ERROR);
				
				}

			}
		}
	}

	public static boolean checkdataexists(String value){
		boolean dataexists = false;
		ObservableList<String> existing_data = ref_listview_currentlist.getItems();
		if(existing_data.size()!=0){
			String data = "";
			for(int i=0; i<existing_data.size(); i++){
				data = existing_data.get(i);
				if(value.equals(data)){
					dataexists = true;
					break;
				}
				else{
					dataexists = false;
				}
			}
		}
		else{
			dataexists = false;
		}
		return dataexists;
	}

	public static boolean checkPF_MAppingdataexists(String value){
		boolean dataexists = false;
		ObservableList<String> existing_data = ref_listview_pf_list.getItems();
		if(existing_data.size()!=0){
			String data = "";
			for(int i=0; i<existing_data.size(); i++){
				data = existing_data.get(i);
				if(value.equals(data)){
					dataexists = true;
					break;
				}
				else{
					dataexists = false;
				}
			}
		}
		else{
			dataexists = false;
		}
		return dataexists;
	}

	public void RemoveCurrentOnClick(){
		ObservableList<String> current =  ref_listview_currentlist.getItems();
		String rem_current = ref_listview_currentlist.getSelectionModel().getSelectedItem();
		current.remove(rem_current);
	}

	public void RemovePF_MappingtOnClick(){
		ObservableList<String> pfObj =  ref_listview_pf_list.getItems();
		String rem_pfValue = ref_listview_pf_list.getSelectionModel().getSelectedItem();
		pfObj.remove(rem_pfValue);
	}

	public ObservableList<String> getSelectedCurrent(){
		ObservableList<String> current =  ref_listview_currentlist.getItems();
		ApplicationLauncher.logger.info("current: " + current);
		return current;
	}

	public ObservableList<String> getSelectedPF(){
		ObservableList<String> PF_List =  ref_listview_pf_list.getItems();
		ApplicationLauncher.logger.info("PF_List: " + PF_List);
		return PF_List;
	}

	public void SaveI_Mapping_CurrentOnClick(){
		String project_name = ProjectController.getProjectName();
		ObservableList<String> current = getSelectedCurrent();
		MySQL_Controller.sp_delete_tp_setup_i_user_data_mapping(project_name);
		for(int i=0; i<current.size(); i++){
			MySQL_Controller.sp_add_tp_setup_i_user_data_mapping(project_name, i+1, current.get(i));
		}
		setupImaxData();
		testDataMatrixAccordian.setExpandedPane(testCaseSetupPane);	
	}

	public void SaveI_AndPF_MappingOnClick(){
		String project_name = ProjectController.getProjectName();

		ObservableList<String> PF_List = getSelectedPF();
		MySQL_Controller.sp_delete_tp_setup_pf_user_data_mapping(project_name);


		for(int i=0; i<PF_List.size(); i++){
			MySQL_Controller.sp_add_tp_setup_pf_user_data_mapping(project_name, i+1, PF_List.get(i));
		}
		setupPfAbcData();
		ObservableList<String> current = getSelectedCurrent();
		MySQL_Controller.sp_delete_tp_setup_i_user_data_mapping(project_name);
		for(int i=0; i<current.size(); i++){
			MySQL_Controller.sp_add_tp_setup_i_user_data_mapping(project_name, i+1, current.get(i));
		}
		setupImaxData();
		testDataMatrixAccordian.setExpandedPane(testCaseSetupPane);
	}

	public void load_saved_test_point_setup(){
		String project_name = ProjectController.getProjectName();
		JSONObject test_setup_data = MySQL_Controller.sp_gettest_point_setup(project_name);
		ApplicationLauncher.logger.info("load_saved_test_point_setup: " + test_setup_data);
		ApplicationLauncher.logger.info("condition: "+ test_setup_data.isNull("imax"));
		try {
			if(!test_setup_data.isNull("imax")){
				SetTestSetupData(test_setup_data);
				onTestSetupPreviewClick();
			}
		} catch (JSONException e1) {
			
			e1.printStackTrace();
			ApplicationLauncher.logger.error("load_saved_test_point_setup: JSONException: " + e1.getMessage());
		}
	}


	public void load_saved_data(){
		String project_name = ProjectController.getProjectName();
		JSONObject i_mapping_list = MySQL_Controller.sp_gettp_setup_i_user_data_mapping(project_name);
		ApplicationLauncher.logger.info("load_saved_data: i_mapping_list: " + i_mapping_list);
		ref_listview_currentlist.getItems().clear();
		try {
			JSONArray I_mapping_values_arr = i_mapping_list.getJSONArray("I_mapping_values");
			JSONObject jobj = new JSONObject();
			String i_value = "";
			for(int i=0; i<I_mapping_values_arr.length(); i++){
				jobj = I_mapping_values_arr.getJSONObject(i);
				i_value = jobj.getString("current_mapping_value");
				ref_listview_currentlist.getItems().add(i_value);
			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("PropertyInfluenceController: load_saved_data: JSONException1: " + e.getMessage());
		}

		JSONObject pf_mapping_list = MySQL_Controller.sp_gettp_setup_pf_user_data_mapping(project_name);
		ApplicationLauncher.logger.info("load_saved_data: pf_mapping_list: " + pf_mapping_list);
		ref_listview_pf_list.getItems().clear();
		try {
			JSONArray PF_mapping_values_arr = pf_mapping_list.getJSONArray("PF_mapping_values");
			JSONObject jobj = new JSONObject();
			String pf_value = "";
			for(int i=0; i<PF_mapping_values_arr.length(); i++){
				jobj = PF_mapping_values_arr.getJSONObject(i);
				pf_value = jobj.getString("pf_mapping_value");
				ref_listview_pf_list.getItems().add(pf_value);
			}

		} catch (JSONException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("PropertyInfluenceController: load_saved_data: JSONException2: " + e.getMessage());
		}
	}

	public void MoveUpIOnClick(){
		ObservableList<String> currentList_data = ref_listview_currentlist.getItems();
		int current_pos = 0;
		int swap_pos = 0;
		boolean swapped_flag = false;
		String selected_value = "";
		String i_value = "";
		String current_row = "";
		for (String testCaseRow : currentList_data) {
			selected_value = ref_listview_currentlist.getSelectionModel().getSelectedItem();
			ApplicationLauncher.logger.info("MoveUpIOnClick: selected_value: " + selected_value);

			if(testCaseRow.equals(selected_value)){
				for(int i=0; i<currentList_data.size(); i++){
					i_value = currentList_data.get(i);
					current_row = testCaseRow;
					ApplicationLauncher.logger.info("MoveUpIOnClick: i_value: " + i_value);
					ApplicationLauncher.logger.info("MoveUpIOnClick: current_row: " + current_row);

					if(i_value.equals(current_row) &&!swapped_flag){
						current_pos = i;
						swap_pos = i-1;
						ApplicationLauncher.logger.info("MoveUpIOnClick: current_pos: " + current_pos);
						ApplicationLauncher.logger.info("MoveUpIOnClick: swap_pos: " + swap_pos);
						if(current_pos != 0){
							currentList_data = SwapValues(currentList_data, swap_pos, current_pos);
							ref_listview_currentlist.setItems(currentList_data);
							ref_listview_currentlist.getSelectionModel().select(swap_pos);
							swapped_flag = true;
						}
					}
				}

			}
		}
	}


	public void MoveUpPF_MappingOnClick(){
		ObservableList<String> PF_List_data = ref_listview_pf_list.getItems();
		int current_pos = 0;
		int swap_pos = 0;
		boolean swapped_flag = false;
		String selected_value = "";
		String pf_value = "";
		String current_row = "";
		for (String testCaseRow : PF_List_data) {
			selected_value = ref_listview_pf_list.getSelectionModel().getSelectedItem();
			ApplicationLauncher.logger.info("MoveUpPF_MappingOnClick: selected_value: " + selected_value);

			if(testCaseRow.equals(selected_value)){
				for(int i=0; i<PF_List_data.size(); i++){
					pf_value = PF_List_data.get(i);
					current_row = testCaseRow;
					ApplicationLauncher.logger.info("MoveUpPF_MappingOnClick: pf_value: " + pf_value);
					ApplicationLauncher.logger.info("MoveUpPF_MappingOnClick: current_row: " + current_row);

					if(pf_value.equals(current_row) &&!swapped_flag){
						current_pos = i;
						swap_pos = i-1;
						ApplicationLauncher.logger.info("MoveUpPF_MappingOnClick: current_pos: " + current_pos);
						ApplicationLauncher.logger.info("MoveUpPF_MappingOnClick: swap_pos: " + swap_pos);
						if(current_pos != 0){
							PF_List_data = SwapValues(PF_List_data, swap_pos, current_pos);
							ref_listview_pf_list.setItems(PF_List_data);
							ref_listview_pf_list.getSelectionModel().select(swap_pos);
							swapped_flag = true;
						}
					}
				}

			}
		}
	}

	public void MoveDownIOnClick(){
		ObservableList<String> currentList_data = ref_listview_currentlist.getItems();
		int current_pos = 0;
		int swap_pos = 0;
		boolean swapped_flag = false;
		String selected_value = "";
		String i_value = "";
		String current_row = "";
		for (String testCaseRow : currentList_data) {
			selected_value = ref_listview_currentlist.getSelectionModel().getSelectedItem();
			if(testCaseRow.equals(selected_value)){
				for(int i=0; i<currentList_data.size(); i++){
					i_value = currentList_data.get(i);
					current_row = testCaseRow;
					ApplicationLauncher.logger.info("MoveDownIOnClick: i_value: " + i_value);
					ApplicationLauncher.logger.info("MoveDownIOnClick: current_row: " + current_row);

					if(i_value.equals(current_row) &&!swapped_flag){
						current_pos = i;
						swap_pos = i+1;
						ApplicationLauncher.logger.info("MoveDownIOnClick: current_pos: " + current_pos);
						ApplicationLauncher.logger.info("MoveDownIOnClick: swap_pos: " + swap_pos);
						if(current_pos != currentList_data.size()-1){
							currentList_data = SwapValues(currentList_data, swap_pos, current_pos);
							ref_listview_currentlist.setItems(currentList_data);
							ref_listview_currentlist.getSelectionModel().select(swap_pos);
							swapped_flag = true;
						}
					}
				}

			}
		}
	}


	public void MoveDownPF_MappingOnClick(){
		ObservableList<String> pfList_data = ref_listview_pf_list.getItems();
		int current_pos = 0;
		int swap_pos = 0;
		boolean swapped_flag = false;
		String selected_value = "";
		String pf_value = "";
		String current_row = "";
		for (String testCaseRow : pfList_data) {
			selected_value = ref_listview_pf_list.getSelectionModel().getSelectedItem();
			if(testCaseRow.equals(selected_value)){
				for(int i=0; i<pfList_data.size(); i++){
					pf_value = pfList_data.get(i);
					current_row = testCaseRow;
					ApplicationLauncher.logger.info("MoveDownPF_MappingOnClick: pf_value: " + pf_value);
					ApplicationLauncher.logger.info("MoveDownPF_MappingOnClick: current_row: " + current_row);

					if(pf_value.equals(current_row) &&!swapped_flag){
						current_pos = i;
						swap_pos = i+1;
						ApplicationLauncher.logger.info("MoveDownPF_MappingOnClick: current_pos: " + current_pos);
						ApplicationLauncher.logger.info("MoveDownPF_MappingOnClick: swap_pos: " + swap_pos);
						if(current_pos != pfList_data.size()-1){
							pfList_data = SwapValues(pfList_data, swap_pos, current_pos);
							ref_listview_pf_list.setItems(pfList_data);
							ref_listview_pf_list.getSelectionModel().select(swap_pos);
							swapped_flag = true;
						}
					}
				}

			}
		}
	}


	public ObservableList<String> SwapValues(ObservableList<String> currentList_data, int swap_pos, int current_pos) {
		String current_value = currentList_data.get(current_pos);
		String swap_value = currentList_data.get(swap_pos);
		currentList_data.set(current_pos, swap_value);
		currentList_data.set(swap_pos, current_value);
		return currentList_data;
	} 

	public void CopyDataToAllRows(){
		TestCaseData row_data = testCaseInputsTable.getItems().get(0);
		ObservableList<TestCaseData> copied_row_data = FXCollections.observableArrayList();
		copied_row_data.add(row_data);
		float e_min = row_data.getErrorMin();
		float e_max = row_data.getErrorMax();
		int times = row_data.getTimes();
		int pulses = row_data.getPulse();
		int SkipReadingCount = row_data.getSkipreadingcount();
		float deviation = row_data.getDeviation();
		String runtype = row_data.getTestRunType();
		ApplicationLauncher.logger.info("CopyDataToAllRows: e_min: " + e_min);
		ApplicationLauncher.logger.info("CopyDataToAllRows: e_max: " + e_max);
		ApplicationLauncher.logger.info("CopyDataToAllRows: time: " + times);
		ApplicationLauncher.logger.info("CopyDataToAllRows: pulses: " + pulses);
		ApplicationLauncher.logger.info("CopyDataToAllRows: SkipReadingCount: " + SkipReadingCount);
		ApplicationLauncher.logger.info("CopyDataToAllRows: deviation: " + deviation);
		ApplicationLauncher.logger.info("CopyDataToAllRows: run_type: " + runtype);
		for (int i=1; i<testCaseInputsTable.getItems().size(); i++) {
			TestCaseData copy_row = testCaseInputsTable.getItems().get(i);
			copy_row.setErrorMin(e_min);
			copy_row.setErrorMax(e_max);
			ApplicationLauncher.logger.info("CopyDataToAllRows: time: " + times);
			copy_row.setTimes(times);
			copy_row.setPulse(pulses);
			copy_row.setSkipreadingcount(SkipReadingCount);
			copy_row.setDeviation(deviation);
			copy_row.setTestRunType(runtype);
			copied_row_data.add(copy_row);
		}
		testCaseInputsTable.getItems().clear();
		testCaseInputsTable.getItems().addAll(copied_row_data);
	}

	public void OnIMappingEnterClick(){
		ApplicationLauncher.logger.info("OnEnterClick: Entry ");
		ref_txt_current_input.setOnKeyPressed(new EventHandler<KeyEvent>(){
			@Override
			public void handle(KeyEvent ke){
				if (ke.getCode().equals(KeyCode.ENTER)){
					ApplicationLauncher.logger.info("OnEnterClick: Enter Key Pressed ");
					AddCurrentOnClick();
				}
			}
		});
	}
}

