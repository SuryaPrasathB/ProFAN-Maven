package com.tasnetwork.calibration.energymeter.deployment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.constant.ConstantReportV2;
import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;
import com.tasnetwork.calibration.energymeter.constant.ProCalCustomerConfiguration;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.database.MySQL_Controller;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.testreport.TestReportController;
import com.tasnetwork.calibration.energymeter.uac.UacDataModel;
import com.tasnetwork.calibration.energymeter.util.EditCell;
import com.tasnetwork.calibration.energymeter.util.EditCellDeploymentManage;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;
import com.tasnetwork.calibration.energymeter.util.MyFloatStringConverter;
import com.tasnetwork.calibration.energymeter.util.MyIntegerStringConverter;
import com.tasnetwork.spring.orm.model.ProjectRun;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class DeploymentManagerController implements Initializable {

	Timer DeployTaskTimer;

	Timer importDataTimer;

	private static String lastVisitedDirectory = System.getProperty("user.home");

	public static long deploymentStartTime;

	@FXML
	private TextField txtBatchNumber;

	public static TextField ref_txtBatchNumber;

	@FXML
	private Label lbl_DeploymentReferenceNumber;

	public static Label ref_lbl_DeploymentReferenceNumber;

	@FXML
	private CheckBox chkBxAutoDeploy;

	public static CheckBox ref_chkBxAutoDeploy;

	@FXML
	private Button btnImportDeploymentData;

	public static Button ref_btnImportDeploymentData;

	@FXML
	private TableView<DeploymentDataModel> devicesDataTable;

	@FXML
	private TableView<DeploymentTestCaseDataModel> testCasesDataTable;

	public static TableView<DeploymentTestCaseDataModel> ref_testCasesDataTable;

	@FXML
	private TableColumn<DeploymentDataModel, String> rackidcolumn, typeColumn, emColumn, imaxDataColumn,
			ibaseDataColumn, voltageColumn;

	@FXML
	private TableColumn<DeploymentDataModel, Float> ctr_Column, ptr_Column;

	@FXML
	private TableColumn<DeploymentDataModel, Integer> meterConstColumn;

	@FXML
	private TableColumn<DeploymentDataModel, String> meterMakeColumn;

	@FXML
	private TableColumn<DeploymentDataModel, String> meterModelNoColumn;

	@FXML
	private TableColumn<DeploymentDataModel, String> serialnoColumn;

	@FXML
	private TableColumn<DeploymentTestCaseDataModel, String> testCaseColumn;

	@FXML
	private TableColumn<DeploymentTestCaseDataModel, String> DeploytestCaseSerialNo;

	@SuppressWarnings("rawtypes")
	@FXML
	private TableColumn devicesSelectedColumn;

	@SuppressWarnings("rawtypes")
	@FXML
	private TableColumn testCaseSelectedColumn;

	@FXML
	private Button btn_deploy;

	private static Button ref_btn_deploy;

	@FXML
	private RadioButton radio_sche_later;

	@FXML
	private RadioButton radioBtn_ImportAndExport;

	@FXML
	private RadioButton radioBtn_Import;

	@FXML
	private RadioButton radioBtn_Export;

	@FXML
	private RadioButton radio_sche_now;

	@FXML
	private AnchorPane devicelistparentnode;

	@FXML
	public static AnchorPane ref_devicelistparentnode;

	@FXML
	private DatePicker date_Picker;

	@FXML
	private Label lbl_leftTableButtonAdjustment;
	@FXML
	private Label lbl_RightTableButtonAdjustment;

	@FXML
	private Label lbl_RightTableDeplolBtnAdjustment;

	@FXML
	private TextField txt_serialno;

	@FXML
	private Button btn_SelectAllRack;

	@FXML
	private Button btn_SelectAllTC;

	@FXML
	private VBox vBoxDeviceList;
	@FXML
	private TitledPane titledPaneDeviceList;
	@FXML
	private VBox vBoxTitledInternal;
	@FXML
	private HBox hBoxTable;

	@FXML
	private VBox vBoxTPSelection;

	@FXML
	private HBox hBoxUpDown;

	@FXML
	private Button btnUpMove;

	@FXML
	private Button btnDownMove;
	/*
	 * @FXML private Button btn_summary_save;
	 */

	static DeviceDataManagerController DisplayDataObj = new DeviceDataManagerController();

	public static int no_of_racks = ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK;// 12;
	public static boolean deployed_flag = false;
	public static boolean selectAll_RackActive = false;
	public static boolean selectAll_TC_Active = false;

	public static boolean AllMeterConstSame = false;
	public static int MeterConst = 0;

	public static boolean testcaseselected = false;
	public static boolean deviceselected = false;
	public static TableView<DeploymentDataModel> ref_devicesDataTable;

	private String deploymentMode = ConstantApp.DEPLOYMENT_IMPORT_MODE;
	private ObservableList<DeploymentDataModel> devicesData = FXCollections.observableArrayList();

	private ObservableList<DeploymentTestCaseDataModel> testCasesData = FXCollections.observableArrayList();

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		ref_devicelistparentnode = devicelistparentnode;
		ref_testCasesDataTable = testCasesDataTable;
		ref_txtBatchNumber = txtBatchNumber;
		ref_btn_deploy = btn_deploy;
		ref_btnImportDeploymentData = btnImportDeploymentData;
		ref_lbl_DeploymentReferenceNumber = lbl_DeploymentReferenceNumber;
		ref_devicesDataTable = devicesDataTable;
		ref_chkBxAutoDeploy = chkBxAutoDeploy;

		DeployDeviceListAdjustScreen();
		selectAll_RackActive = false;
		selectAll_TC_Active = false;
		loadDevices();
		loadTestCases(ConstantApp.IMPORT_MODE_ALIAS_NAME);
		if (ConstantApp.USER_ACCESS_LEVEL.equals(ConstantApp.READONLY_ACCESS_LEVEL)) {
			btn_deploy.setDisable(true);
		}
		GuiUtils.autoResizeColumns(ref_testCasesDataTable, false);
		radioBtn_Import.setSelected(true);
		deploymentMode = ConstantApp.DEPLOYMENT_IMPORT_MODE;
		if (ProcalFeatureEnable.EXPORT_MODE_ENABLED) {
			radioBtn_Import.setVisible(true);
			radioBtn_Export.setVisible(true);
			radioBtn_ImportAndExport.setVisible(true);
		} else {
			radioBtn_Import.setVisible(false);
			radioBtn_Export.setVisible(false);
			radioBtn_ImportAndExport.setVisible(false);
		}

		if (!ProcalFeatureEnable.TEST_EXECUTION_DIFFERENT_METER_CONSTANT_FEATURE_ENABLED) {
			// meterConstColumn.setEditable(false);
			meterConstColumn.setVisible(false);
		}

		if (!ProcalFeatureEnable.DEPLOY_MAKE_FIELD_ENABLED) {
			meterMakeColumn.setVisible(false);
		}

		if (!ProcalFeatureEnable.DEPLOY_MODEL_NO_FIELD_ENABLED) {
			meterModelNoColumn.setVisible(false);
		}

		if (ProcalFeatureEnable.USER_ACCESS_CONTROL_ENABLED) {
			applyUacSettings();
		}

		if (!ConstantAppConfig.IMPORT_DEPLOYMENT_DATA_FEATURE_ENABLED) {
			ref_btnImportDeploymentData.setVisible(false);
		}
		ref_lbl_DeploymentReferenceNumber.setText(ConstantAppConfig.DEPLOYMENT_REFERENCE_NUMBER_LABEL_NAME);

		if (!ProcalFeatureEnable.CONVEYOR_FEATURE_ENABLED) {
			ref_chkBxAutoDeploy.setVisible(false);
		}
		setupDeploymentTabFocusTableView();
		guiObjectsHide();

	}

	private void setupDeploymentTabFocusTableView() {
		
		// ref_tableViewVoltageAndCurrentMapping.setEditable(true);
		ApplicationLauncher.logger.debug("setupDeploymentTabFocusTableView : Entry");
		devicesDataTable.getSelectionModel().cellSelectionEnabledProperty().set(true);
		EditCellDeploymentManage.setPresentActiveTable(devicesDataTable);
		// when character or numbers pressed it will start edit in editable
		// fields

		devicesDataTable.setOnKeyPressed(event -> {
			if (event.getCode().isLetterKey() || event.getCode().isDigitKey()) {
				ApplicationLauncher.logger.debug("setupDeploymentTabFocusTableView: editFocusedCell");
				editFocusedCellOnTableViewDeploymentManage();
			} else if (event.getCode() == KeyCode.RIGHT
					|| ((event.getCode() == KeyCode.TAB) && (!event.isShiftDown()))) {
				ApplicationLauncher.logger.debug("setupDeploymentTabFocusTableView: Tab");
				devicesDataTable.getSelectionModel().selectNext();
				editFocusedCellOnTableViewDeploymentManage();
				event.consume();
				// } else if ( (event.getCode() == KeyCode.LEFT ) || (
				// (event.getCode() == ( KeyCode.SHIFT )) || (event.getCode() ==
				// KeyCode.TAB) ) ) {
			} else if ((event.getCode() == KeyCode.LEFT) || (event.getCode() == KeyCode.TAB && (event.isShiftDown()))) {
				// work around due to
				// TableView.getSelectionModel().selectPrevious() due to a bug
				// stopping it from working on
				// the first column in the last row of the table
				ApplicationLauncher.logger.debug("setupDeploymentTabFocusTableView: Left");
				selectPreviousOnTableViewDeploymentManage();
				editFocusedCellOnTableViewDeploymentManage();
				event.consume();
			} else if (event.getCode() == KeyCode.UP) {
				ApplicationLauncher.logger.debug("setupDeploymentTabFocusTableView: UP");
				selectAboveCellOnTableViewDeploymentManage();
				// ref_tableViewVoltageAndCurrentMapping.getSelectionModel().getTableView().getSelectionModel().selectAboveCell();
				editFocusedCellOnTableViewDeploymentManage();
				event.consume();
			} else if (event.getCode() == KeyCode.DOWN) {
				ApplicationLauncher.logger.debug("setupDeploymentTabFocusTableView: Down");
				selectBelowCellOnTableViewDeploymentManage();
				// ref_tableViewVoltageAndCurrentMapping.getSelectionModel().getTableView().getSelectionModel().selectBelowCell();
				editFocusedCellOnTableViewDeploymentManage();
				event.consume();
			}
		});
	}

	@SuppressWarnings("unchecked")
	public static void editFocusedCellOnTableViewDeploymentManage() {
		ApplicationLauncher.logger.debug("editFocusedCellOnTableViewDeploymentManage: Entry");
		final TablePosition<DeploymentDataModel, ?> focusedCell = ref_devicesDataTable.focusModelProperty().get()
				.focusedCellProperty().get();
		if (focusedCell.getTableColumn().isEditable()) {
			ApplicationLauncher.logger.debug("editFocusedCellOnTableViewDeploymentManage: Edit Hit");
			ref_devicesDataTable.edit(focusedCell.getRow(), focusedCell.getTableColumn());
		}
	}

	private TableColumn<DeploymentDataModel, ?> getTableColumnOnTableViewDeploymentManage(
			final TableColumn<DeploymentDataModel, ?> column, int offset) {
		ApplicationLauncher.logger.debug("getTableColumnOnTableViewDeploymentManage: Entry");
		int columnIndex = devicesDataTable.getVisibleLeafIndex(column);
		int newColumnIndex = columnIndex + offset;
		return devicesDataTable.getVisibleLeafColumn(newColumnIndex);
	}

	@SuppressWarnings("unchecked")
	private void selectPreviousOnTableViewDeploymentManage() {
		ApplicationLauncher.logger.debug("selectPreviousOnTableViewDeploymentManage: Entry");
		if (devicesDataTable.getSelectionModel().isCellSelectionEnabled()) {
			// in cell selection mode, we have to wrap around, going from
			// right-to-left, and then wrapping to the end of the previous line
			TablePosition<DeploymentDataModel, ?> pos = devicesDataTable.getFocusModel().getFocusedCell();
			if (pos.getColumn() - 1 >= 0) {
				// go to previous row
				devicesDataTable.getSelectionModel().select(pos.getRow(),
						getTableColumnOnTableViewDeploymentManage(pos.getTableColumn(), -1));
			} else if (pos.getRow() < devicesDataTable.getItems().size()) {
				// wrap to end of previous row
				devicesDataTable.getSelectionModel().select(pos.getRow() - 1,
						devicesDataTable.getVisibleLeafColumn(devicesDataTable.getVisibleLeafColumns().size() - 1));
			}
		} else {
			int focusIndex = devicesDataTable.getFocusModel().getFocusedIndex();
			if (focusIndex == -1) {
				devicesDataTable.getSelectionModel().select(devicesDataTable.getItems().size() - 1);
			} else if (focusIndex > 0) {
				devicesDataTable.getSelectionModel().select(focusIndex - 1);
			}
		}
	}

	private void selectAboveCellOnTableViewDeploymentManage() {
		ApplicationLauncher.logger.debug("selectAboveCellOnTableViewDeploymentManage: Entry");
		devicesDataTable.getSelectionModel().getTableView().getSelectionModel().selectAboveCell();
	}

	private void selectBelowCellOnTableViewDeploymentManage() {
		ApplicationLauncher.logger.debug("selectBelowCellOnTableViewDeploymentManage: Entry");
		devicesDataTable.getSelectionModel().getTableView().getSelectionModel().selectBelowCell();
	}

	private void guiObjectsHide() {
		
		radioBtn_ImportAndExport.setVisible(false);
	}

	public void radioBtn_ImportOnChange() {
		if (radioBtn_Import.isSelected()) {
			radioBtn_Export.setSelected(false);
			radioBtn_ImportAndExport.setSelected(false);
			deploymentMode = ConstantApp.DEPLOYMENT_IMPORT_MODE;
			loadTestCases(ConstantApp.IMPORT_MODE_ALIAS_NAME);
		} else {
			radioBtn_Import.setSelected(true);
		}

	}

	public void radioBtn_ExportOnChange() {
		if (radioBtn_Export.isSelected()) {
			radioBtn_Import.setSelected(false);
			radioBtn_ImportAndExport.setSelected(false);
			deploymentMode = ConstantApp.DEPLOYMENT_EXPORT_MODE;
			// UpdateSummaryWithExportTestPoint();
			loadTestCases(ConstantApp.EXPORT_MODE_ALIAS_NAME);
		} else {
			radioBtn_Export.setSelected(true);
		}

	}

	public void radioBtn_ImportAndExportOnChange() {
		if (radioBtn_ImportAndExport.isSelected()) {
			radioBtn_Export.setSelected(false);
			radioBtn_Import.setSelected(false);
			deploymentMode = ConstantApp.DEPLOYMENT_IMPORT_AND_EXPORT_MODE;
			UpdateSummaryWithImportExportTestPoint();
		} else {
			radioBtn_ImportAndExport.setSelected(true);
		}

	}

	public void validateAtleastOneDeviceSelected() {
		ApplicationLauncher.logger.info("validateAtleastOneDeviceSelected: Entry");
		// String project_name = MeterParamsController.getCurrentProjectName();
		Iterator<DeploymentDataModel> itr = devicesData.iterator();
		// DeploymentTestCaseDataModel dataElement = new
		// DeploymentTestCaseDataModel("", true, "", "", "");
		/*
		 * String testcase= ""; String testtype= ""; String aliasid= "";
		 */
		// int DeploymentSequenceNo = 1;
		try {
			while (itr.hasNext()) {
				// DeploymentTestCaseDataModel dataElement = itr.next();
				DeploymentDataModel dataElement = itr.next();
				// ApplicationLauncher.logger.info("LoadTestCaseToDB:"+DeploymentSequenceNo);
				// ApplicationLauncher.logger.debug("selected: " +
				// dataElement.getIsSelected());
				/*
				 * testcase= dataElement.getTestCase(); testtype=
				 * dataElement.getTesttype(); aliasid= dataElement.getAliasid();
				 */
				// String sequence_no = dataElement.getSequence_no();
				// ApplicationLauncher.logger.debug("testcase: " + testcase);
				// ApplicationLauncher.logger.debug("testtype: " + testtype);
				// ApplicationLauncher.logger.debug("dataElement.getIsSelected():
				// " + dataElement.getIsSelected());
				if (dataElement.getIsSelected()) {
					setDeviceSelected(true);
					break;
					/*
					 * testcase= dataElement.getTestCase(); testtype=
					 * dataElement.getTesttype(); aliasid=
					 * dataElement.getAliasid();
					 */
					// MySQL_Controller.sp_add_deploy_test_cases(project_name,
					// dataElement.getTestCase(),dataElement.getTesttype(),
					// dataElement.getAliasid(), String.valueOf(
					// DeploymentSequenceNo++), "Y");

					// MySQL_Controller.sp_add_deploy_test_cases(project_name,
					// testcase,testtype, aliasid, String.valueOf(
					// DeploymentSequenceNo++), "Y");
					// DeploymentSequenceNo++;
				}
				// DeploymentSequenceNo++;
				/*
				 * else{// PerformanceIssue
				 * MySQL_Controller.sp_add_deploy_test_cases(project_name,
				 * testcase,testtype, aliasid, sequence_no, "N"); }
				 */
			}
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("validateAtleastOneDeviceSelected: Exception: " + e.getMessage());
		}
	}

	public void validateAtleastOneTestPointSelected() {
		ApplicationLauncher.logger.info("validateAtleastOneTestPointSelected: Entry");
		// String project_name = MeterParamsController.getCurrentProjectName();
		Iterator<DeploymentTestCaseDataModel> itr = testCasesData.iterator();
		DeploymentTestCaseDataModel dataElement = new DeploymentTestCaseDataModel("", true, "", "", "");
		/*
		 * String testcase= ""; String testtype= ""; String aliasid= "";
		 */
		// int DeploymentSequenceNo = 1;
		try {
			while (itr.hasNext()) {
				// DeploymentTestCaseDataModel dataElement = itr.next();
				dataElement = itr.next();
				// ApplicationLauncher.logger.info("LoadTestCaseToDB:"+DeploymentSequenceNo);
				// ApplicationLauncher.logger.debug("selected: " +
				// dataElement.getIsSelected());
				/*
				 * testcase= dataElement.getTestCase(); testtype=
				 * dataElement.getTesttype(); aliasid= dataElement.getAliasid();
				 */
				// String sequence_no = dataElement.getSequence_no();
				// ApplicationLauncher.logger.debug("testcase: " + testcase);
				// ApplicationLauncher.logger.debug("testtype: " + testtype);
				// ApplicationLauncher.logger.debug("dataElement.getIsSelected():
				// " + dataElement.getIsSelected());
				if (dataElement.getIsSelected()) {
					setTestCaseSelected(true);
					break;
					/*
					 * testcase= dataElement.getTestCase(); testtype=
					 * dataElement.getTesttype(); aliasid=
					 * dataElement.getAliasid();
					 */
					// MySQL_Controller.sp_add_deploy_test_cases(project_name,
					// dataElement.getTestCase(),dataElement.getTesttype(),
					// dataElement.getAliasid(), String.valueOf(
					// DeploymentSequenceNo++), "Y");

					// MySQL_Controller.sp_add_deploy_test_cases(project_name,
					// testcase,testtype, aliasid, String.valueOf(
					// DeploymentSequenceNo++), "Y");
					// DeploymentSequenceNo++;
				}
				// DeploymentSequenceNo++;
				/*
				 * else{// PerformanceIssue
				 * MySQL_Controller.sp_add_deploy_test_cases(project_name,
				 * testcase,testtype, aliasid, sequence_no, "N"); }
				 */
			}
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("validateAtleastOneTestPointSelected: Exception: " + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void UpdateSummaryWithImportExportTestPoint() {
		testCasesData.clear();
		testCaseColumn.setCellValueFactory(cellData -> cellData.getValue().testCaseProperty());
		testCaseSelectedColumn.setCellValueFactory(new DeploymentTestCaseCheckBoxValueFactory());
		DeploytestCaseSerialNo.setCellValueFactory(cellData -> cellData.getValue().sequencenoProperty());

		String project_name = MeterParamsController.getCurrentProjectName();
		new ArrayList<String>();
		try {
			JSONObject projectdatalist = MySQL_Controller.sp_getsummary_data(project_name);
			JSONArray summary_data = projectdatalist.getJSONArray("Summary_data");
			// ApplicationLauncher.logger.info(projectdatalist);
			JSONObject jobj = new JSONObject();
			String testcase = "";
			String alias_id = "";
			// String sequence_no = "";
			String testtype = "";
			int sequence_no = 1;
			for (int i = 0; i < summary_data.length(); i++) {
				jobj = summary_data.getJSONObject(i);
				testcase = ConstantApp.IMPORT_MODE_ALIAS_NAME + jobj.getString("test_case_name");
				alias_id = jobj.getString("test_alias_id");
				// sequence_no = jobj.getString("sequence_no");
				testtype = jobj.getString("test_type");
				testCasesData.add(new DeploymentTestCaseDataModel(testcase, Boolean.TRUE, testtype, alias_id,
						String.valueOf(sequence_no++)));
			}
			for (int i = 0; i < summary_data.length(); i++) {
				jobj = summary_data.getJSONObject(i);
				testcase = ConstantApp.EXPORT_MODE_ALIAS_NAME + jobj.getString("test_case_name");
				alias_id = jobj.getString("test_alias_id");
				// sequence_no = jobj.getString("sequence_no");
				testtype = jobj.getString("test_type");
				testCasesData.add(new DeploymentTestCaseDataModel(testcase, Boolean.TRUE, testtype, alias_id,
						String.valueOf(sequence_no++)));
			}
		} catch (JSONException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("loadTestCases: JSONException: " + e.getMessage());
		}

		testCasesDataTable.setItems(testCasesData);
		testCasesDataTable.setColumnResizePolicy((param) -> true);
		GuiUtils.autoResizeColumns(testCasesDataTable, true);

	}

	public ObservableList<DeploymentTestCaseDataModel> SwapTables(
			ObservableList<DeploymentTestCaseDataModel> summary_data, int swap_pos, int current_pos) {
		DeploymentTestCaseDataModel current_testdata = summary_data.get(current_pos);
		DeploymentTestCaseDataModel swap_testdata = summary_data.get(swap_pos);
		summary_data.set(current_pos, swap_testdata);
		summary_data.set(swap_pos, current_testdata);
		return summary_data;
	}

	public void SummaryMoveUpOnClick() {
		ApplicationLauncher.logger.debug("SummaryMoveUpOnClick: Entry");
		ObservableList<DeploymentTestCaseDataModel> summary_data = ref_testCasesDataTable.getItems();
		int current_pos = 0;
		int swap_pos = 0;
		boolean swapped_flag = false;
		String testCaseName = "";
		String current_row = "";
		for (DeploymentTestCaseDataModel testCaseRow : summary_data) {
			if (testCaseRow.getIsSelected()) {
				swapped_flag = false;
				for (int i = 0; i < summary_data.size(); i++) {
					testCaseName = summary_data.get(i).getTestCase();
					current_row = testCaseRow.getTestCase();
					if (testCaseName.equals(current_row) && !swapped_flag) {
						current_pos = i;
						swap_pos = i - 1;
						// ApplicationLauncher.logger.info("SummaryMoveUpOnClick:
						// current_pos: " + current_pos);
						// ApplicationLauncher.logger.info("SummaryMoveUpOnClick:
						// swap_pos: " + swap_pos);
						if (current_pos != 0) {
							summary_data = SwapTables(summary_data, swap_pos, current_pos);
							ref_testCasesDataTable.setItems(summary_data);
							ApplicationLauncher.logger.info("SummaryMoveUpOnClick: current_pos: " + current_pos);
							ApplicationLauncher.logger.info("SummaryMoveUpOnClick: swap_pos: " + swap_pos);
							swapped_flag = true;
						}
					}
				}

			}
		}
	}

	public void SummaryMoveDownOnClick() {
		ApplicationLauncher.logger.debug("SummaryMoveDownOnClick: Entry");
		ObservableList<DeploymentTestCaseDataModel> summary_data = ref_testCasesDataTable.getItems();
		int current_pos = 0;
		int swap_pos = 0;
		boolean swapped_flag = false;
		int LastUpdatedPosition = summary_data.size() - 1;
		;
		// for (DeploymentTestCaseDataModel testCaseRow : summary_data) {
		for (int i = summary_data.size() - 1; i >= 0; i--) {
			DeploymentTestCaseDataModel testCaseRow = summary_data.get(i);
			if (testCaseRow.getIsSelected()) {
				swapped_flag = false;
				// for(int i=LastUpdatedPosition; i<summary_data.size(); i++){
				// for(int i=summary_data.size()-1; i>=0; i--){
				for (int j = LastUpdatedPosition; j >= 0; j--) {
					if (swapped_flag) {
						ApplicationLauncher.logger.info("SummaryMoveDownOnClick: break: ");
						break;
					} else {
						String testCaseName = summary_data.get(j).getTestCase();
						String current_row = testCaseRow.getTestCase();
						// ApplicationLauncher.logger.info("SummaryMoveDownOnClick:
						// testCaseName: " + testCaseName);
						// ApplicationLauncher.logger.info("SummaryMoveDownOnClick:
						// current_row: " + current_row);
						// ApplicationLauncher.logger.info("SummaryMoveDownOnClick:
						// swapped_flag: " + swapped_flag);
						if (testCaseName.equals(current_row) && !swapped_flag) {
							// ApplicationLauncher.logger.info("SummaryMoveDownOnClick:
							// testCaseName: " + testCaseName);
							// ApplicationLauncher.logger.info("SummaryMoveDownOnClick:
							// current_row: " + current_row);
							current_pos = j;
							// swap_pos = i+1;
							swap_pos = j + 1;
							// ApplicationLauncher.logger.info("SummaryMoveDownOnClick:
							// current_pos: " + current_pos);
							// ApplicationLauncher.logger.info("SummaryMoveDownOnClick:
							// swap_pos: " + swap_pos);
							// if(current_pos != summary_data.size()-1){
							if (current_pos >= 0) {
								if (swap_pos < summary_data.size()) {
									summary_data = SwapTables(summary_data, swap_pos, current_pos);
									ref_testCasesDataTable.setItems(summary_data);
									swapped_flag = true;
									LastUpdatedPosition = current_pos - 1;
									ApplicationLauncher.logger
											.info("SummaryMoveDownOnClick: current_pos: " + current_pos);
									ApplicationLauncher.logger.info("SummaryMoveDownOnClick: swap_pos: " + swap_pos);
									// ApplicationLauncher.logger.info("SummaryMoveDownOnClick:
									// swapped_flag: " + swapped_flag);
								}
							}
						}
					}
				}
			}
		}
	}

	/*
	 * public void SummaryMoveDownOnClick(){ ref_testCasesDataTable.refresh();
	 * ObservableList<DeploymentTestCaseDataModel> summary_data =
	 * ref_testCasesDataTable.getItems(); int current_pos = 0; int swap_pos = 0;
	 * boolean swapped_flag = false; int LastUpdatedPosition = 0; for
	 * (DeploymentTestCaseDataModel testCaseRow : summary_data) {
	 * if(testCaseRow.getIsSelected()){ swapped_flag = false; for(int
	 * i=LastUpdatedPosition; i<summary_data.size(); i++){ //for(int
	 * i=summary_data.size()-1; i>=0; i--){ if(swapped_flag){
	 * ApplicationLauncher.logger.info("SummaryMoveDownOnClick: break: ");
	 * break; }else{ String testCaseName = summary_data.get(i).getTestCase();
	 * String current_row = testCaseRow.getTestCase();
	 * ApplicationLauncher.logger.info("SummaryMoveDownOnClick: testCaseName: "
	 * + testCaseName); ApplicationLauncher.logger.info(
	 * "SummaryMoveDownOnClick: current_row: " + current_row);
	 * ApplicationLauncher.logger.info("SummaryMoveDownOnClick: swapped_flag: "
	 * + swapped_flag); if(testCaseName.equals(current_row) && !swapped_flag){
	 * //ApplicationLauncher.logger.info(
	 * "SummaryMoveDownOnClick: testCaseName: " + testCaseName);
	 * //ApplicationLauncher.logger.info("SummaryMoveDownOnClick: current_row: "
	 * + current_row); current_pos = i; swap_pos = i+1; //swap_pos = i+1;
	 * ApplicationLauncher.logger.info("SummaryMoveDownOnClick: current_pos: " +
	 * current_pos); ApplicationLauncher.logger.info(
	 * "SummaryMoveDownOnClick: swap_pos: " + swap_pos); if(current_pos !=
	 * summary_data.size()-1){ summary_data = SwapTables(summary_data, swap_pos,
	 * current_pos); ref_testCasesDataTable.setItems(summary_data); swapped_flag
	 * = true; LastUpdatedPosition = swap_pos+1;
	 * ApplicationLauncher.logger.info("SummaryMoveDownOnClick: swapped_flag: "
	 * + swapped_flag); } } } } } } }
	 */

	/*
	 * public void SaveSummaryDataOnClick(){
	 * ObservableList<TestSetupSummaryDataModel> summary_data =
	 * ref_SummaryTable.getItems(); String projectname = getProjectName();
	 * for(int i=0; i<summary_data.size(); i++){ TestSetupSummaryDataModel data
	 * = summary_data.get(i); String testdata = data.getTestData();
	 * ArrayList<String> test_details = parseTestDetails(testdata); String
	 * test_type = test_details.get(0); String alias_id = test_details.get(1);
	 * int sequence_no = i+1; MySQL_Controller.sp_add_summary_data(projectname,
	 * testdata, test_type, alias_id, sequence_no); }
	 * 
	 * 
	 * 
	 * }
	 */

	public void DeployDeviceListAdjustScreen() {

		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		double ScreenHeight = primaryScreenBounds.getHeight();
		double ScreenWidth = primaryScreenBounds.getWidth();
		double SplitPaneOffset = 149;
		double MeterParaMeterHeightOffset = 300;
		double UpperHBoxOffset = 120;
		double hBoxUpDownHeight = 47;

		long ScreenWidthThreshold = 1500;
		long ScreenHeightThreshold = 700;
		new ConstantApp();
		ScreenWidthThreshold = ConstantAppConfig.ScreenWidthThreshold;
		ScreenHeightThreshold = ConstantAppConfig.ScreenHeightThreshold;
		ApplicationLauncher.logger.info("DeployDeviceListAdjustScreen: ScreenWidthThreshold:" + ScreenWidthThreshold);
		ApplicationLauncher.logger.info("DeployDeviceListAdjustScreen: ScreenHeightThreshold:" + ScreenHeightThreshold);

		ApplicationLauncher.logger.info("DepolyDeviceListAdjustScreen :Current Screen Height:" + ScreenHeight);
		ApplicationLauncher.logger.info("DepolyDeviceListAdjustScreen: Current Screen Width:" + ScreenWidth);
		ApplicationLauncher.logger.info("DepolyDeviceListAdjustScreen :ScreenHeightThreshold:" + ScreenHeightThreshold);
		ApplicationLauncher.logger.info("DepolyDeviceListAdjustScreen: ScreenWidthThreshold:" + ScreenWidthThreshold);

		// btnUpMove.setLayoutY(AnchorInnerPaneSummary.getMinHeight()-45);
		// btnDownMove.setLayoutY(AnchorInnerPaneSummary.getMinHeight()-45);
		// btn_summary_save.setLayoutY(AnchorInnerPaneSummary.getMinHeight()-45);
		// vBoxTPSelection;
		// hBoxUpDown;

		if (ScreenHeight > ScreenHeightThreshold) {
			ApplicationLauncher.logger.info("DepolyDeviceListAdjustScreen :ScreenHeightThreshold: Entry");
			devicelistparentnode.setMinHeight(ScreenHeight - MeterParaMeterHeightOffset);
			vBoxDeviceList.setMinHeight(ScreenHeight - MeterParaMeterHeightOffset);
			titledPaneDeviceList.setMinHeight(ScreenHeight - MeterParaMeterHeightOffset - 10);
			vBoxTitledInternal.setMinHeight(ScreenHeight - MeterParaMeterHeightOffset - 50);
			hBoxTable.setMinHeight(ScreenHeight - MeterParaMeterHeightOffset - UpperHBoxOffset);
			vBoxTPSelection.setMinHeight(ScreenHeight - MeterParaMeterHeightOffset - UpperHBoxOffset);// -hBoxUpDown.getMinHeight());
			testCasesDataTable
					.setMinHeight(ScreenHeight - MeterParaMeterHeightOffset - UpperHBoxOffset - hBoxUpDownHeight);
			// hBoxUpDown;

		} else {
			ApplicationLauncher.logger.info("DepolyDeviceListAdjustScreen :ScreenHeightThreshold else: Entry");
		}
		double DeployTestPointTableWidth = ScreenWidth - SplitPaneOffset - 75;
		double testCasesDataTableWidth = 400;// 550;
		testCaseSelectedColumn.setMaxWidth(50.0);
		if (ScreenWidth > ScreenWidthThreshold) {
			ApplicationLauncher.logger.info("DepolyDeviceListAdjustScreen :ScreenWidthThreshold : Entry");
			devicesSelectedColumn.setMaxWidth(50.0);
			devicelistparentnode.setMinWidth(DeployTestPointTableWidth);
			vBoxDeviceList.setMinWidth(DeployTestPointTableWidth);
			titledPaneDeviceList.setMinWidth(DeployTestPointTableWidth);
			vBoxTitledInternal.setPrefWidth(DeployTestPointTableWidth);
			hBoxTable.setPrefWidth(DeployTestPointTableWidth);
			testCasesDataTable.setPrefWidth(testCasesDataTableWidth);
			devicesDataTable.setPrefWidth(DeployTestPointTableWidth - testCasesDataTableWidth);
			lbl_leftTableButtonAdjustment.setMinWidth(575);
			lbl_RightTableButtonAdjustment.setMinWidth(65);
			lbl_RightTableDeplolBtnAdjustment.setMinWidth(165);

			vBoxTPSelection.setPrefWidth(testCasesDataTableWidth);

		} else {
			ApplicationLauncher.logger.info("DepolyDeviceListAdjustScreen :ScreenWidthThreshold else: Entry");
			// titledPaneDeviceList.setMinWidth( DeployTestPointTableWidth
			// -300);
			titledPaneDeviceList.setMaxWidth(DeployTestPointTableWidth + 100);
			hBoxTable.setMaxWidth(DeployTestPointTableWidth + 100);
			// hBoxTable.setPrefWidth( DeployTestPointTableWidth -200 );
			vBoxTPSelection.setPrefWidth(testCasesDataTableWidth);
			devicesDataTable.setPrefWidth(DeployTestPointTableWidth + 100 - testCasesDataTableWidth);
			// vBoxTPSelection.setMaxWidth( testCasesDataTableWidth );
		}
	}

	public static int get_no_of_racks() {
		return no_of_racks;
	}

	public static void set_no_of_racks(int supportedNoOfRacks) {
		no_of_racks = supportedNoOfRacks;
	}

	public static void setDeployedFlag(boolean value) {
		deployed_flag = value;
	}

	public static boolean getDeployedFlag() {
		return deployed_flag;
	}

	public void setTestCaseSelected(boolean value) {
		testcaseselected = value;
	}

	public boolean IsAtleastOneTestCaseSelected() {
		return testcaseselected;
	}

	public void setDeviceSelected(boolean value) {
		deviceselected = value;
	}

	public boolean IsAtleastOneDeviceSelected() {
		return deviceselected;
	}

	@SuppressWarnings("unchecked")
	public void loadDevices() {
		devicesDataTable.setEditable(true);

		rackidcolumn.setCellValueFactory(cellData -> cellData.getValue().rackidProperty());
		typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
		imaxDataColumn.setCellValueFactory(cellData -> cellData.getValue().iMaxProperty());
		ibaseDataColumn.setCellValueFactory(cellData -> cellData.getValue().lbProperty());
		voltageColumn.setCellValueFactory(cellData -> cellData.getValue().vdProperty());
		devicesSelectedColumn.setCellValueFactory(new DeploymentDevicesCheckBoxValueFactory());
		serialnoColumn.setCellValueFactory(new PropertyValueFactory<DeploymentDataModel, String>("serialno"));
		// serialnoColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		serialnoColumn.setCellFactory(EditCellDeploymentManage.forTableColumn());
		// EditCellDeploymentManage
		serialnoColumn.setOnEditCommit(new EventHandler<CellEditEvent<DeploymentDataModel, String>>() {
			public void handle(CellEditEvent<DeploymentDataModel, String> t) {
				ApplicationLauncher.logger.info("loadDevices: setSerialno: Entry");
				DeploymentDataModel rowData = ((DeploymentDataModel) t.getTableView().getItems()
						.get(t.getTablePosition().getRow()));
				ApplicationLauncher.logger.info("loadDevices: t.getNewValue(): " + t.getNewValue());
				String serial_no = t.getNewValue();
				if (!ProcalFeatureEnable.EXPORT_MODE_ENABLED) {
					rowData.setSerialno(t.getNewValue());
				} else if (ProcalFeatureEnable.EXPORT_MODE_ENABLED) {

					if ((!serial_no.toUpperCase().contains(ConstantApp.EXPORT_MODE_ALIAS_NAME))
							&& (!serial_no.toUpperCase().contains(ConstantApp.EXPORT_MODE_ALIAS_NAME.trim()))) {
						rowData.setSerialno(t.getNewValue());
					} else {
						// rowData.setSerialno(t.getOldValue());
						// rowData.getSerialno().
						ApplicationLauncher.logger.info("DeploymentManagerController : loadDevices: handle: serial_no:"
								+ serial_no + ". Data input not accepted prompt -ErrorCode - U002");
						ApplicationLauncher.InformUser(
								"ErrorCode - U002", "Data input contains unaccepted value <"
										+ ConstantApp.EXPORT_MODE_ALIAS_NAME + ">  , kindly rephrase!!",
								AlertType.ERROR);

						devicesDataTable.refresh();
					}
				}
				// rowData.setSerialno(t.getNewValue());

			}
		});

		meterMakeColumn.setCellValueFactory(new PropertyValueFactory<DeploymentDataModel, String>("meterMake"));
		// meterMakeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		meterMakeColumn.setCellFactory(EditCellDeploymentManage.forTableColumn());
		// EditCellDeploymentManage
		/*
		 * meterMakeColumn.setCellFactory(param -> new
		 * TextFieldTableCell<DeploymentDataModel, String>() {
		 * 
		 * @Override public void updateItem(String item, boolean empty) {
		 * super.updateItem(item, empty);
		 * 
		 * ApplicationLauncher.logger.info("loadDevices: meterMakeColumn: Hit1"
		 * ); if (item != null && !empty) { DeploymentDataModel data =
		 * (DeploymentDataModel) getTableView().getItems().get(getIndex());
		 * ApplicationLauncher.logger.info("loadDevices: meterMakeColumn: Hit2"
		 * ); if(data.getmeterconst()==2000){
		 * disableProperty().bind(Bindings.createBooleanBinding(() ->
		 * data.getmeterconst()==2000, data.meterMakeProperty()));
		 * ApplicationLauncher.logger.info("loadDevices: meterMakeColumn: Hit3"
		 * ); }else if(data.getmeterconst()==3000){
		 * ApplicationLauncher.logger.info("loadDevices: meterMakeColumn: Hit4"
		 * ); } } else { // disableProperty().unbind();
		 * ApplicationLauncher.logger.info("loadDevices: meterMakeColumn: Hit5"
		 * ); } } });
		 */
		meterMakeColumn.setOnEditCommit(new EventHandler<CellEditEvent<DeploymentDataModel, String>>() {
			public void handle(CellEditEvent<DeploymentDataModel, String> t) {
				ApplicationLauncher.logger.info("loadDevices: meterMake: Entry");
				DeploymentDataModel rowData = ((DeploymentDataModel) t.getTableView().getItems()
						.get(t.getTablePosition().getRow()));
				ApplicationLauncher.logger.info("loadDevices: t.getNewValue(): " + t.getNewValue());
				String meterMake = t.getNewValue();
				if (!ProcalFeatureEnable.EXPORT_MODE_ENABLED) {
					rowData.setMeterMake(t.getNewValue());
				} else if (ProcalFeatureEnable.EXPORT_MODE_ENABLED) {

					if ((!meterMake.toUpperCase().contains(ConstantApp.EXPORT_MODE_ALIAS_NAME))
							&& (!meterMake.toUpperCase().contains(ConstantApp.EXPORT_MODE_ALIAS_NAME.trim()))) {
						rowData.setMeterMake(t.getNewValue());
					} else {
						// rowData.setSerialno(t.getOldValue());
						// rowData.getSerialno().
						ApplicationLauncher.logger.info("DeploymentManagerController : loadDevices: handle: meterMake:"
								+ meterMake + ". Data input not accepted prompt -ErrorCode - U003");
						ApplicationLauncher.InformUser(
								"ErrorCode - U003", "Data input contains unaccepted value <"
										+ ConstantApp.EXPORT_MODE_ALIAS_NAME + ">  , kindly rephrase!!",
								AlertType.ERROR);

						devicesDataTable.refresh();
					}
				}

				// rowData.setSerialno(t.getNewValue());

			}
		});

		meterModelNoColumn.setCellValueFactory(new PropertyValueFactory<DeploymentDataModel, String>("meterModelNo"));
		// meterModelNoColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		meterModelNoColumn.setCellFactory(EditCellDeploymentManage.forTableColumn());
		// EditCellDeploymentManage
		meterModelNoColumn.setOnEditCommit(new EventHandler<CellEditEvent<DeploymentDataModel, String>>() {
			public void handle(CellEditEvent<DeploymentDataModel, String> t) {
				ApplicationLauncher.logger.info("loadDevices: meterModelNo: Entry");
				DeploymentDataModel rowData = ((DeploymentDataModel) t.getTableView().getItems()
						.get(t.getTablePosition().getRow()));
				ApplicationLauncher.logger.info("loadDevices: t.getNewValue(): " + t.getNewValue());
				t.getNewValue();
				rowData.setMeterModelNo(t.getNewValue());
				devicesDataTable.refresh();
				/*
				 * if(!ProcalFeatureEnable.EXPORT_MODE_ENABLED){
				 * rowData.setMeterMake(t.getNewValue()); }else
				 * if(ProcalFeatureEnable.EXPORT_MODE_ENABLED){
				 * 
				 * 
				 * if((!meterModelNo.toUpperCase().contains(ConstantApp.
				 * EXPORT_MODE_ALIAS_NAME)) &&
				 * (!meterModelNo.toUpperCase().contains(ConstantApp.
				 * EXPORT_MODE_ALIAS_NAME.trim())) ){
				 * rowData.setMeterMake(t.getNewValue()); }else{
				 * //rowData.setSerialno(t.getOldValue());
				 * //rowData.getSerialno(). ApplicationLauncher.logger.info(
				 * "DeploymentManagerController : loadDevices: handle: meterModelNo:"
				 * +meterModelNo+
				 * ". Data input not accepted prompt -ErrorCode - U003");
				 * ApplicationLauncher.InformUser("ErrorCode - U003A",
				 * "Data input contains unaccepted value <"
				 * +ConstantApp.EXPORT_MODE_ALIAS_NAME+">  , kindly rephrase!!"
				 * ,AlertType.ERROR);
				 * 
				 * devicesDataTable.refresh(); } }
				 */

				// rowData.setSerialno(t.getNewValue());

			}
		});

		ctr_Column.setCellValueFactory(new PropertyValueFactory<DeploymentDataModel, Float>("ctrratio"));
		// ctr_Column.setCellFactory(EditCell.<DeploymentDataModel,
		// Float>forTableColumn(new MyFloatStringConverter()));
		ctr_Column.setCellFactory(
				EditCellDeploymentManage.<DeploymentDataModel, Float> forTableColumn(new MyFloatStringConverter()));
		ctr_Column.setOnEditCommit(new EventHandler<CellEditEvent<DeploymentDataModel, Float>>() {
			public void handle(CellEditEvent<DeploymentDataModel, Float> t) {
				ApplicationLauncher.logger.info("setctrratio: Entry");
				DeploymentDataModel rowData = ((DeploymentDataModel) t.getTableView().getItems()
						.get(t.getTablePosition().getRow()));
				if (t.getNewValue() != null) {
					ApplicationLauncher.logger.info("setctrratio: " + t.getNewValue());
					rowData.setctrratio(t.getNewValue());
				}
			}
		});
		ptr_Column.setCellValueFactory(new PropertyValueFactory<DeploymentDataModel, Float>("ptrratio"));
		// ptr_Column.setCellFactory(EditCell.<DeploymentDataModel,
		// Float>forTableColumn(new MyFloatStringConverter()));
		ptr_Column.setCellFactory(
				EditCellDeploymentManage.<DeploymentDataModel, Float> forTableColumn(new MyFloatStringConverter()));
		ptr_Column.setOnEditCommit(new EventHandler<CellEditEvent<DeploymentDataModel, Float>>() {
			public void handle(CellEditEvent<DeploymentDataModel, Float> t) {
				DeploymentDataModel rowData = ((DeploymentDataModel) t.getTableView().getItems()
						.get(t.getTablePosition().getRow()));
				if (t.getNewValue() != null) {
					rowData.setptrratio(t.getNewValue());
				}
			}
		});
		meterConstColumn.setCellValueFactory(new PropertyValueFactory<DeploymentDataModel, Integer>("meterconst"));
		// meterConstColumn.setCellFactory(EditCell.<DeploymentDataModel,
		// Integer>forTableColumn(new MyIntegerStringConverter()));
		meterConstColumn.setCellFactory(
				EditCellDeploymentManage.<DeploymentDataModel, Integer> forTableColumn(new MyIntegerStringConverter()));
		meterConstColumn.setOnEditCommit(new EventHandler<CellEditEvent<DeploymentDataModel, Integer>>() {
			public void handle(CellEditEvent<DeploymentDataModel, Integer> t) {
				DeploymentDataModel rowData = ((DeploymentDataModel) t.getTableView().getItems()
						.get(t.getTablePosition().getRow()));
				if (t.getNewValue() != null) {
					rowData.setmeterconst(t.getNewValue());
				}
				/*
				 * if(t.getNewValue()== 2000){ ApplicationLauncher.logger.info(
				 * "DeploymentManagerController : hit1");
				 * rowData.setMeterMake("Enabled");
				 * //devicesDataTable.refresh(); }else if(t.getNewValue()==
				 * 3000){ ApplicationLauncher.logger.info(
				 * "DeploymentManagerController : hit2");
				 * rowData.setMeterMake("Disabled");
				 * 
				 * //devicesDataTable.refresh(); }
				 */
			}
		});

		String project_name = MeterParamsController.getCurrentProjectName();
		JSONObject modeldata = FetchModelDataFromDB(project_name);
		// if (true){//Condition TO BE DEFINED

		String customer_name = "";
		String model_name = "";
		String model_type = "";
		String ib = "";
		String imax = "";
		String vd = "";
		String ctr_ratio = "";
		String ptr_ratio = "";
		String meter_const = "";
		try {
			customer_name = modeldata.getString("customer_name");
			model_name = modeldata.getString("model_name");
			model_type = modeldata.getString("model_type");
			ib = modeldata.getString("basic_current_ib");
			imax = modeldata.getString("max_current_imax");
			vd = modeldata.getString("rated_voltage_vd");
			ctr_ratio = modeldata.getString("ctr_ratio");
			ptr_ratio = modeldata.getString("ptr_ratio");
			meter_const = modeldata.getString("impulses_per_unit");
		} catch (JSONException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("loadDevices: JSONException: " + e.getMessage());
		}

		String rackid = "";
		String serialno = "";
		String meterMake = "";
		String meterModelNo = "";
		float ctrratio = 1;
		float ptrratio = 1;
		int meterconst = 0;
		int noOfSupportedRacks = ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK;
		int rackstartPosition = 1;
		if (ProcalFeatureEnable.RACK_HYBRID_MODE_ENABLED) {
			if (model_type.startsWith(ConstantApp.METERTYPE_THREEPHASE)) {
				noOfSupportedRacks = ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_POSITIONS;
				rackstartPosition = ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_START_POSITION;
				if ((ProCalCustomerConfiguration.ELECTROBYTE_HYBRID_2NO_3PHASE_10NO_1PHASE_POSITION_2022)
						&& (model_type.startsWith(ConstantApp.METERTYPE_THREEPHASE))) {
					ApplicationLauncher.logger
							.info("loadDevices: ELECTROBYTE_HYBRID_2NO_3PHASE_10NO_1PHASE_POSITION_2022 max: ");
					int electroByte3PhaseNoOfSupportedRacks = 2;
					noOfSupportedRacks = electroByte3PhaseNoOfSupportedRacks;// ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_POSITIONS;
				}
			} else if (model_type.startsWith(ConstantApp.METERTYPE_SINGLEPHASE)) {
				noOfSupportedRacks = ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_POSITIONS;
				rackstartPosition = ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_START_POSITION;
			}
		}
		set_no_of_racks(noOfSupportedRacks);
		int maxRackPosition = get_no_of_racks() + (rackstartPosition - 1);

		ApplicationLauncher.logger.info("loadDevices: get_no_of_racks: " + get_no_of_racks());
		ApplicationLauncher.logger.info("loadDevices: rackstartPosition: " + rackstartPosition);
		ApplicationLauncher.logger.info("loadDevices: maxRackPosition: " + maxRackPosition);

		// for(int i=0; i < get_no_of_racks(); i++){
		// if((ProCalCustomerConfiguration.ELECTROBYTE_HYBRID_2NO_3PHASE_10NO_1PHASE_POSITION_2022)
		if ((ProcalFeatureEnable.RACK_HYBRID_MODE_ENABLED)
				&& (model_type.startsWith(ConstantApp.METERTYPE_THREEPHASE))) {
			ApplicationLauncher.logger
					.info("loadDevices: ELECTROBYTE_HYBRID_2NO_3PHASE_10NO_1PHASE_POSITION_2022 custom: ");
			for (int i = rackstartPosition; i <= maxRackPosition; i++) {
				if (i == rackstartPosition) {
					ApplicationLauncher.logger
							.info("loadDevices: ELECTROBYTE_HYBRID_2NO_3PHASE_10NO_1PHASE_POSITION_2022: Test1");
					rackid = Integer.toString(i);// +1);
					serialno = "U" + Integer.toString(i);// +1);
					ctrratio = Float.parseFloat(ctr_ratio);// Integer.parseInt(ctr_ratio);
					ptrratio = Float.parseFloat(ptr_ratio);// Integer.parseInt(ptr_ratio);
					meterconst = Integer.parseInt(meter_const);

					devicesData.add(new DeploymentDataModel(rackid, null, customer_name, null, model_type, model_name,
							serialno, imax, ib, vd, null, Boolean.TRUE, ctrratio, ptrratio, meterconst, meterMake,
							meterModelNo));
				} else {
					ApplicationLauncher.logger
							.info("loadDevices: ELECTROBYTE_HYBRID_2NO_3PHASE_10NO_1PHASE_POSITION_2022: Test1");
					// int electroByte3Phase2ndPosition = 6;

					rackid = Integer.toString(i);// +1);
					serialno = "U" + Integer.toString(i);// +1);
					if (ProCalCustomerConfiguration.ELECTROBYTE_HYBRID_2NO_3PHASE_10NO_1PHASE_POSITION_2022) {
						int electroByte3Phase2ndPosition = 6;
						rackid = Integer.toString(electroByte3Phase2ndPosition);// +1);
						serialno = "U" + Integer.toString(electroByte3Phase2ndPosition);// +1);
					}
					// ctrratio = Integer.parseInt(ctr_ratio);
					// ptrratio = Integer.parseInt(ptr_ratio);
					ctrratio = Float.parseFloat(ctr_ratio);// Integer.parseInt(ctr_ratio);
					ptrratio = Float.parseFloat(ptr_ratio);// Integer.parseInt(ptr_ratio);
					meterconst = Integer.parseInt(meter_const);

					devicesData.add(new DeploymentDataModel(rackid, null, customer_name, null, model_type, model_name,
							serialno, imax, ib, vd, null, Boolean.TRUE, ctrratio, ptrratio, meterconst, meterMake,
							meterModelNo));
				}
			}
		} else {
			for (int i = rackstartPosition; i <= maxRackPosition; i++) {
				rackid = Integer.toString(i);// +1);
				serialno = "U" + Integer.toString(i);// +1);
				// ctrratio = Integer.parseInt(ctr_ratio);
				// ptrratio = Integer.parseInt(ptr_ratio);
				ctrratio = Float.parseFloat(ctr_ratio);// Integer.parseInt(ctr_ratio);
				ptrratio = Float.parseFloat(ptr_ratio);// Integer.parseInt(ptr_ratio);
				meterconst = Integer.parseInt(meter_const);

				devicesData.add(new DeploymentDataModel(rackid, null, customer_name, null, model_type, model_name,
						serialno, imax, ib, vd, null, Boolean.TRUE, ctrratio, ptrratio, meterconst, meterMake,
						meterModelNo));
			}
		}
		/*
		 * } else{ ApplicationLauncher.logger.info(
		 * "loadDevices:Model not mapped or it may be deleted"); }
		 */

		devicesDataTable.setItems(devicesData);

	}

	@SuppressWarnings("unchecked")
	public void loadTestCases(String DeployMode) {
		testCasesData.clear();
		testCaseColumn.setCellValueFactory(cellData -> cellData.getValue().testCaseProperty());
		testCaseSelectedColumn.setCellValueFactory(new DeploymentTestCaseCheckBoxValueFactory());
		DeploytestCaseSerialNo.setCellValueFactory(cellData -> cellData.getValue().sequencenoProperty());

		String project_name = MeterParamsController.getCurrentProjectName();
		new ArrayList<String>();
		try {
			JSONObject projectdatalist = MySQL_Controller.sp_getsummary_data(project_name);
			JSONArray summary_data = projectdatalist.getJSONArray("Summary_data");
			// ApplicationLauncher.logger.info(projectdatalist);
			JSONObject jobj = new JSONObject();
			String testcase = "";
			String alias_id = "";
			String testtype = "";
			// int sequence_no =0;
			for (int i = 0; i < summary_data.length(); i++) {
				jobj = summary_data.getJSONObject(i);
				testcase = DeployMode + jobj.getString("test_case_name");
				alias_id = jobj.getString("test_alias_id");
				// sequence_no = jobj.getString("sequence_no");
				testtype = jobj.getString("test_type");
				testCasesData.add(new DeploymentTestCaseDataModel(testcase, Boolean.TRUE, testtype, alias_id,
						String.valueOf(i + 1)));
			}
		} catch (JSONException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("loadTestCases: JSONException: " + e.getMessage());
		}

		testCasesDataTable.setItems(testCasesData);
		testCasesDataTable.setColumnResizePolicy((param) -> true);
		GuiUtils.autoResizeColumns(testCasesDataTable, true);

	}

	public static ArrayList<String> FetchTestCaselistFromDB(String projectname) throws JSONException {
		JSONObject projectdatalist = MySQL_Controller.sp_getsummary_data(projectname);
		JSONArray summary_data = projectdatalist.getJSONArray("Summary_data");
		ArrayList<String> testcaselist = new ArrayList<String>();
		ApplicationLauncher.logger.info(projectdatalist);
		JSONObject jobj = new JSONObject();
		String testcase = "";
		for (int i = 0; i < summary_data.length(); i++) {
			jobj = summary_data.getJSONObject(i);
			testcase = jobj.getString("test_case_name");
			jobj.getString("test_alias_id");
			testcaselist.add(testcase);
		}
		return testcaselist;
	}

	public JSONObject FetchModelDataFromDB(String project_name) {
		int model_id = MySQL_Controller.sp_getProjectModel_ID(project_name);
		JSONObject model_data = MySQL_Controller.sp_getem_model_data(model_id);
		return model_data;
	}

	public void ScheduleNowOnClick() {
		radio_sche_later.setSelected(false);
		date_Picker.setVisible(false);
	}

	public void ScheduleLaterOnClick() {
		radio_sche_now.setSelected(false);
		date_Picker.setVisible(true);
	}

	public JSONObject getModelParameters(String project_name) {
		int model_id = MySQL_Controller.sp_getProjectModel_ID(project_name);
		JSONObject model_data = MySQL_Controller.sp_getem_model_data(model_id);

		return model_data;
	}

	public void LoadDeviceDataToDB_Result(String deploymentID) {
		String project_name = MeterParamsController.getCurrentProjectName();
		JSONObject devices_json = MySQL_Controller.sp_getdeploy_devices(project_name, deploymentID);
		String test_case_name = "";
		String alias_id = "";
		String device_name = "";
		String FailureReason = "";
		int rack_id = 0;
		int ctr = 0;
		int ptr = 0;
		String test_result = "";
		int error_id = 0;
		String device_rack_id = "";
		String mctNctMode = ConstantReport.RESULT_EXECUTION_MODE_MCT_NCT_UNDEFINED;
		String energyFlowMode = ConstantApp.DEPLOYMENT_IMPORT_EXPORT_UNDEFINED;
		int seqNumber = 0;
		try {
			JSONArray devices = devices_json.getJSONArray("Devices");
			JSONObject jobj = new JSONObject();
			for (int i = 0; i < devices.length(); i++) {
				jobj = devices.getJSONObject(i);
				device_name = jobj.getString("Device_name");
				rack_id = jobj.getInt("Rack_ID");
				ctr = jobj.getInt("ctr_ratio");
				ptr = jobj.getInt("ptr_ratio");
				device_rack_id = device_name + "/" + Integer.toString(rack_id);

				MySQL_Controller.sp_add_result(project_name, test_case_name, alias_id, Integer.toString(rack_id),
						test_result, error_id, device_rack_id, FailureReason,
						ConstantReport.RESULT_DATA_TYPE_DEVICE_NAME, mctNctMode, energyFlowMode, deploymentID,
						seqNumber);

				/*
				 * sp_add_result (String project_name, String test_case_name,
				 * String alias_id, String rack_id, String test_result, int
				 * error_id, String error_value, String FailureReason,String
				 * data_type,String executionMctNctMode,String energyFlowMode,
				 * String deploymentId,int seqNumber
				 */

				if ((deploymentMode.equals(ConstantApp.DEPLOYMENT_IMPORT_MODE)
						|| deploymentMode.equals(ConstantApp.DEPLOYMENT_IMPORT_AND_EXPORT_MODE))) {

					MySQL_Controller.sp_add_result(project_name, test_case_name, alias_id, Integer.toString(rack_id),
							test_result, error_id, Integer.toString(ctr), FailureReason,
							ConstantReport.RESULT_DATA_TYPE_CTR, mctNctMode, energyFlowMode, deploymentID, seqNumber);
					MySQL_Controller.sp_add_result(project_name, test_case_name, alias_id, Integer.toString(rack_id),
							test_result, error_id, Integer.toString(ptr), FailureReason,
							ConstantReport.RESULT_DATA_TYPE_PTR, mctNctMode, energyFlowMode, deploymentID, seqNumber);
				}
				if (ProcalFeatureEnable.EXPORT_MODE_ENABLED) {
					if (!(deploymentMode.equals(ConstantApp.DEPLOYMENT_IMPORT_MODE))) {

						rack_id = Integer.valueOf(rack_id) + ConstantApp.EXPORT_MODE_DEVICE_ID_THRESHOLD;
						MySQL_Controller.sp_add_result(project_name, test_case_name, alias_id,
								Integer.toString(rack_id), test_result, error_id, Integer.toString(ctr), FailureReason,
								ConstantReport.RESULT_DATA_TYPE_CTR, mctNctMode, energyFlowMode, deploymentID,
								seqNumber);
						MySQL_Controller.sp_add_result(project_name, test_case_name, alias_id,
								Integer.toString(rack_id), test_result, error_id, Integer.toString(ptr), FailureReason,
								ConstantReport.RESULT_DATA_TYPE_PTR, mctNctMode, energyFlowMode, deploymentID,
								seqNumber);

					}
				}
			}

		} catch (JSONException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("LoadDeviceDataToDB: JSONException: " + e.getMessage());
		}
	}

	public void SaveProjectRunStartTime(String project_name) {
		long start_time = ProjectExecutionController.getProjectStartTime();
		MySQL_Controller.sp_add_project_run(project_name, start_time);
	}

	public void DeletePreviousDeployedTestPointData() {
		String project_name = MeterParamsController.getCurrentProjectName();
		MySQL_Controller.sp_delete_deploy_test_cases(project_name);
	}

	public void setProjectNameAsAppTitle() {
		String project_name = MeterParamsController.getCurrentProjectName();
		Stage app = ApplicationLauncher.getPrimaryStage();
		app.setTitle(ConstantVersion.APPLICATION_NAME + " - " + project_name);
	}

	public void InformUserTestCaseDeployed(String message, AlertType alertType) {
		Alert alert = new Alert(alertType);// AlertType.INFORMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("file:images/" + ConstantVersion.APP_ICON_FILENAME));
		alert.setTitle("Info");
		alert.setHeaderText("Deployment status");
		String s = message;// "Project successfully deployed ";
		alert.setContentText(s);
		alert.showAndWait();
	}

	public void LoadDevicesToDB(String lastUpdatedDeploymentID) {
		ApplicationLauncher.logger.info("LoadDevicesToDB: Entry");
		String project_name = MeterParamsController.getCurrentProjectName();
		Iterator<DeploymentDataModel> itr = devicesData.iterator();
		ApplicationLauncher.logger.info("LoadDevicesToDB: deploymentMode:" + deploymentMode);
		ApplicationLauncher.logger
				.info("LoadDevicesToDB: deploymentMode:" + deploymentMode.equals(ConstantApp.DEPLOYMENT_IMPORT_MODE));
		ApplicationLauncher.logger.info("LoadDevicesToDB: deploymentMode:"
				+ deploymentMode.equals(ConstantApp.DEPLOYMENT_IMPORT_AND_EXPORT_MODE));
		ApplicationLauncher.logger
				.info("LoadDevicesToDB: deploymentMode:" + (deploymentMode.equals(ConstantApp.DEPLOYMENT_IMPORT_MODE)
						|| deploymentMode.equals(ConstantApp.DEPLOYMENT_IMPORT_AND_EXPORT_MODE)));

		String device = "";
		String rack = "";
		String meterMake = "";
		String meterModelNo = "";
		float ctr_ratio = 1;
		float ptr_ratio = 1;
		int meter_const = 0;
		int rack_id = 0;

		ArrayList<Integer> meter_const_arr = new ArrayList<Integer>();
		try {
			// if((deploymentMode.equals(ConstantApp.DEPLOYMENT_IMPORT_MODE) ||
			// deploymentMode.equals(ConstantApp.DEPLOYMENT_IMPORT_AND_EXPORT_MODE))){
			while (itr.hasNext()) {
				DeploymentDataModel dataElement = itr.next();
				// ApplicationLauncher.logger.debug("LoadDevicesToDB: selected:
				// " + dataElement.getIsSelected());
				device = dataElement.getSerialno();
				rack = dataElement.getrackid();
				ctr_ratio = dataElement.getctrratio();
				ptr_ratio = dataElement.getptrratio();
				meter_const = dataElement.getmeterconst();
				meterMake = dataElement.getMeterMake();
				meterModelNo = dataElement.getMeterModelNo();
				rack_id = Integer.valueOf(rack);
				ApplicationLauncher.logger.debug("LoadDevicesToDB: rack_id: " + rack_id);
				if (dataElement.getIsSelected()) {
					setDeviceSelected(true);
					MySQL_Controller.sp_add_deploy_devices(lastUpdatedDeploymentID, project_name, device, rack_id,
							ctr_ratio, ptr_ratio, meter_const, "Y", meterMake, meterModelNo);

					meter_const_arr.add(meter_const);
					ApplicationLauncher.logger.debug("LoadDevicesToDB: isSelected Yes: ");
				} else {
					MySQL_Controller.sp_add_deploy_devices(lastUpdatedDeploymentID, project_name, device, rack_id,
							ctr_ratio, ptr_ratio, meter_const, "N", meterMake, meterModelNo);
					ApplicationLauncher.logger.debug("LoadDevicesToDB: isSelected No: ");
				}
			}
			/*
			 * } if(ConstantFeatureEnable.EXPORT_MODE_ENABLED){
			 * if(!(deploymentMode.equals(ConstantApp.DEPLOYMENT_IMPORT_MODE))){
			 * itr = devicesData.iterator(); ApplicationLauncher.logger.info(
			 * "LoadDevicesToDB: Export Mode");
			 * 
			 * while (itr.hasNext()) { DeploymentDataModel dataElement =
			 * itr.next(); //ApplicationLauncher.logger.info(
			 * "LoadDevicesToDB: selected: " + dataElement.getIsSelected());
			 * device= ConstantApp.EXPORT_MODE_ALIAS_NAME+
			 * dataElement.getSerialno(); ApplicationLauncher.logger.info(
			 * "LoadDevicesToDB: device:"+device); rack =
			 * dataElement.getrackid(); ctr_ratio = dataElement.getctrratio();
			 * ptr_ratio = dataElement.getptrratio(); meter_const =
			 * dataElement.getmeterconst(); rack_id =
			 * Integer.valueOf(rack)+ConstantApp.
			 * EXPORT_MODE_DEVICE_ID_THRESHOLD; if (dataElement.getIsSelected())
			 * { setDeviceSelected(true);
			 * MySQL_Controller.sp_add_deploy_devices(project_name, device,
			 * rack_id, ctr_ratio, ptr_ratio, meter_const, "Y");
			 * meter_const_arr.add(meter_const); } else{
			 * MySQL_Controller.sp_add_deploy_devices(project_name, device,
			 * rack_id, ctr_ratio, ptr_ratio, meter_const,"N"); } } } }
			 */
			CheckAllMeterConstSame(meter_const_arr);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("LoadDevicesToDB: Exception: " + e.getMessage());
		}
	}

	public void setupTargetValueForTestPoint() {
		ApplicationLauncher.logger.info("setupTargetValueForTestPoint: Entry");

		Iterator<DeploymentTestCaseDataModel> itr = testCasesData.iterator();
		DeploymentTestCaseDataModel testCaseDataElement = new DeploymentTestCaseDataModel("", true, "", "", "");

		try {
			String project_name = MeterParamsController.getCurrentProjectName();
			JSONObject modelparams = getModelParameters(project_name);

			modelparams.getString("ct_type");
			String modelType = modelparams.getString("model_type");
			float ratedVolt = Float.parseFloat(modelparams.getString("rated_voltage_vd"));
			float ratedIbCurrent = Float.parseFloat(modelparams.getString("basic_current_ib"));
			// rated_volt =
			// manipulateRatedVoltageFor3PhaseDeltaFromL_L_TO_L_N(rated_volt);
			float ratedImaxCurrent = Float.parseFloat(modelparams.getString("max_current_imax"));

			float voltPercentage = 0.0f;
			float outputTargetVolt = 0.0f;
			String voltPercentStr = "";

			float currentPercentage = 0.0f;
			float outputTargetIbCurrent = 0.0f;
			float outputTargetImaxCurrent = 0.0f;
			String currentPercentStr = "";
			// String currentStr = "";

			String[] testCaseSplitData;
			int setScaleCurrentAfterDecimal = 3;
			int setScaleVoltageAfterDecimal = 2;
			BigDecimal bigValue;

			String rybOutputTargetVoltValue = "0.0";
			String rPhaseOutputTargetVoltValue = "0.0";
			String yPhaseOutputTargetVoltValue = "0.0";
			String bPhaseOutputTargetVoltValue = "0.0";

			String rybOutputTargetCurrentValue = "0.0";
			String rPhaseOutputTargetCurrentValue = "0.0";
			String yPhaseOutputTargetCurrentValue = "0.0";
			String bPhaseOutputTargetCurrentValue = "0.0";

			String rybOutputTargetPfValue = "0.0";
			String rPhaseOutputTargetPfValue = "0.0";
			String yPhaseOutputTargetPfValue = "0.0";
			String bPhaseOutputTargetPfValue = "0.0";
			String targetFreq = "";
			String targetEnergy = "";

			String runType = "";
			int testPeriodInSec = 0;
			int warmupPeriodInSec = 0;
			String minErrorAllowed = "";
			String maxErrorAllowed = "";
			int targetNoOfPulses = 0;
			String testSubType = "";
			int readingId = 0;
			int targetAverageCount = 0;

			while (itr.hasNext()) {
				testCaseDataElement = itr.next();

				if (testCaseDataElement.getIsSelected()) {

					rybOutputTargetVoltValue = "0.0";
					rPhaseOutputTargetVoltValue = "0.0";
					yPhaseOutputTargetVoltValue = "0.0";
					bPhaseOutputTargetVoltValue = "0.0";

					rybOutputTargetCurrentValue = "0.0";
					rPhaseOutputTargetCurrentValue = "0.0";
					yPhaseOutputTargetCurrentValue = "0.0";
					bPhaseOutputTargetCurrentValue = "0.0";

					rybOutputTargetPfValue = "0.0";
					rPhaseOutputTargetPfValue = "0.0";
					yPhaseOutputTargetPfValue = "0.0";
					bPhaseOutputTargetPfValue = "0.0";

					targetFreq = "";
					targetEnergy = "";

					runType = "";
					testPeriodInSec = 0;
					warmupPeriodInSec = 0;
					minErrorAllowed = "";
					maxErrorAllowed = "";
					targetNoOfPulses = 0;
					testSubType = "";
					readingId = 0;
					targetAverageCount = 0;
					ApplicationLauncher.logger
							.debug("setupTargetValueForTestPoint: getTesttype(): " + testCaseDataElement.getTesttype());
					if (!testCaseDataElement.getTesttype().equals(ConstantApp.TEST_PROFILE_DUT_COMMAND)) {
						try {
							ApplicationLauncher.logger.debug("setupTargetValueForTestPoint: getTestCase(): "
									+ testCaseDataElement.getTestCase());
							ApplicationLauncher.logger.debug("setupTargetValueForTestPoint: getTesttype2(): "
									+ testCaseDataElement.getTesttype());

							testCaseSplitData = testCaseDataElement.getTestCase()
									.split(ConstantReportV2.TEST_POINT_FILTER_SEPERATOR);

							String testType = testCaseDataElement.getTesttype();// testcasedetails.getString("test_type");
							String aliasId = testCaseDataElement.getAliasid();// modelparams.getString("alias_id");//testcasedetails.getString("alias_id");
							ApplicationLauncher.logger
									.debug("setupTargetValueForTestPoint: CustomTest: aliasId: " + aliasId);
							ApplicationLauncher.logger
									.debug("setupTargetValueForTestPoint: CustomTest: testType: " + testType);
							JSONObject testPointComponentObj = MySQL_Controller.sp_getproject_components(project_name,
									testType, aliasId);
							JSONArray testPointComponentArray = testPointComponentObj.getJSONArray("test_details");
							JSONObject testPointComponentData = testPointComponentArray.getJSONObject(0);

							if (testCaseDataElement.getTesttype().equals(ConstantApp.TEST_PROFILE_CUSTOM_TEST)) {
								ApplicationLauncher.logger.debug("setupTargetValueForTestPoint: CustomTest: ");
								/*
								 * String testType =
								 * dataElement.getTesttype();//testcasedetails.
								 * getString("test_type"); String aliasId =
								 * dataElement.getAliasid();//modelparams.
								 * getString("alias_id");//testcasedetails.
								 * getString("alias_id");
								 * ApplicationLauncher.logger.debug(
								 * "setupTargetValueForTestPoint: CustomTest: aliasId: "
								 * + aliasId);
								 * 
								 * JSONObject customTestParameter =
								 * MySQL_Controller.sp_getproject_components(
								 * project_name, testType, aliasId); JSONArray
								 * cus_param_array =
								 * customTestParameter.getJSONArray(
								 * "test_details"); JSONObject param =
								 * cus_param_array.getJSONObject(0);
								 */

								String volt_u1 = testPointComponentData.getString("cus_voltage_u1");
								String current_i1 = testPointComponentData.getString("cus_current_i1");
								ApplicationLauncher.logger
										.debug("setupTargetValueForTestPoint: CustomTest: volt_u1: " + volt_u1);
								ApplicationLauncher.logger
										.debug("setupTargetValueForTestPoint: CustomTest: current_i1: " + current_i1);

								outputTargetVolt = Float.parseFloat(volt_u1);
								ApplicationLauncher.logger
										.debug("setupTargetValueForTestPoint: ratedVolt: " + ratedVolt);
								// ApplicationLauncher.logger.debug("setupTargetValueForTestPoint:
								// maxAcceptedVoltagePercent: " +
								// maxAcceptedVoltagePercent);
								// maxDutPercentAndVoltRated =
								// (maxAcceptedVoltagePercent/100) * ratedVolt;
								// ApplicationLauncher.logger.debug("setupTargetValueForTestPoint:
								// maxDutPercentAndVoltRated: " +
								// maxDutPercentAndVoltRated);
								if (outputTargetVolt != 0.0f) {
									bigValue = new BigDecimal(outputTargetVolt);
									bigValue = bigValue.setScale(setScaleVoltageAfterDecimal, RoundingMode.FLOOR);
									outputTargetVolt = bigValue.floatValue();
									rybOutputTargetVoltValue = String.valueOf(outputTargetVolt);
									rPhaseOutputTargetVoltValue = String.valueOf(outputTargetVolt);

									// if(outputTargetVolt <=
									// maxDutPercentAndVoltRated){

								}
								ApplicationLauncher.logger
										.debug("setupTargetValueForTestPoint: ratedImaxCurrent: " + ratedImaxCurrent);
								// ApplicationLauncher.logger.debug("setupTargetValueForTestPoint:
								// maxAcceptedImaxCurrentPercent: " +
								// maxAcceptedImaxCurrentPercent);
								// ApplicationLauncher.logger.debug("setupTargetValueForTestPoint:
								// minAcceptedCurrentValue: " +
								// minAcceptedCurrentValue);
								// maxDutPercentAndImaxRated =
								// (maxAcceptedImaxCurrentPercent/100) *
								// ratedImaxCurrent;
								// ApplicationLauncher.logger.debug("setupTargetValueForTestPoint:
								// maxDutPercentAndImaxRated: " +
								// maxDutPercentAndImaxRated);
								outputTargetImaxCurrent = Float.parseFloat(current_i1);
								if (outputTargetImaxCurrent != 0.0f) {
									bigValue = new BigDecimal(outputTargetImaxCurrent);
									bigValue = bigValue.setScale(setScaleCurrentAfterDecimal, RoundingMode.FLOOR);
									outputTargetImaxCurrent = bigValue.floatValue();
									rybOutputTargetCurrentValue = String.valueOf(outputTargetImaxCurrent);
									rPhaseOutputTargetCurrentValue = String.valueOf(outputTargetImaxCurrent);
									// ApplicationLauncher.logger.debug("setupTargetValueForTestPoint:
									// outputTargetImaxCurrent: " +
									// outputTargetImaxCurrent);

								}

								String pf1 = testPointComponentData.getString("cus_phase_ph1");
								if ((pf1.equals("1")) || (pf1.equals("1.0"))) {
									pf1 = ConstantApp.PF_UPF;
								}
								rybOutputTargetPfValue = pf1;
								targetFreq = testPointComponentData.getString("cus_frequency");
								if (modelType.contains(ConstantApp.METERTYPE_THREEPHASE)) {

									String volt_u2 = testPointComponentData.getString("cus_voltage_u2");
									String volt_u3 = testPointComponentData.getString("cus_voltage_u3");
									String current_i2 = testPointComponentData.getString("cus_current_i2");
									String current_i3 = testPointComponentData.getString("cus_current_i3");

									ApplicationLauncher.logger
											.debug("setupTargetValueForTestPoint: CustomTest: volt_u2: " + volt_u2);
									ApplicationLauncher.logger
											.debug("setupTargetValueForTestPoint: CustomTest: volt_u3: " + volt_u3);

									ApplicationLauncher.logger.debug(
											"setupTargetValueForTestPoint: CustomTest: current_i2: " + current_i2);
									ApplicationLauncher.logger.debug(
											"setupTargetValueForTestPoint: CustomTest: current_i3: " + current_i3);

									outputTargetVolt = Float.parseFloat(volt_u2);
									if (outputTargetVolt != 0.0f) {
										bigValue = new BigDecimal(outputTargetVolt);
										bigValue = bigValue.setScale(setScaleVoltageAfterDecimal, RoundingMode.FLOOR);
										outputTargetVolt = bigValue.floatValue();
										yPhaseOutputTargetVoltValue = String.valueOf(outputTargetVolt);
									}

									outputTargetVolt = Float.parseFloat(volt_u3);
									if (outputTargetVolt != 0.0f) {
										bigValue = new BigDecimal(outputTargetVolt);
										bigValue = bigValue.setScale(setScaleVoltageAfterDecimal, RoundingMode.FLOOR);
										outputTargetVolt = bigValue.floatValue();
										bPhaseOutputTargetVoltValue = String.valueOf(outputTargetVolt);
										// if(outputTargetVolt <=
										// maxDutPercentAndVoltRated){

										/*
										 * }else {
										 * ApplicationLauncher.logger.info(
										 * "setupTargetValueForTestPoint : ERROR_CODE_7011: "
										 * +
										 * ErrorCodeMapping.ERROR_CODE_7011_MSG
										 * + "\n\nS.No "
										 * +dataElement.getSequence_no() + " : "
										 * +dataElement.getTestCase() +
										 * "\n\nVrated and percentage accepted Voltage : "
										 * + maxDutPercentAndVoltRated +
										 * "\nPresent Target Voltage : "
										 * +outputTargetVolt +" - prompted!");
										 * ApplicationLauncher.InformUser(
										 * ErrorCodeMapping.ERROR_CODE_7011,
										 * ErrorCodeMapping.ERROR_CODE_7011_MSG
										 * + "\n\nS.No "
										 * +dataElement.getSequence_no() + " : "
										 * +dataElement.getTestCase() +
										 * "\n\nVrated and percentage accepted Voltage : "
										 * + maxDutPercentAndVoltRated+
										 * "\nPresent Target Voltage : "
										 * +outputTargetVolt,AlertType.ERROR);
										 * 
										 * return false; }
										 */
									}

									outputTargetImaxCurrent = Float.parseFloat(current_i2);
									if (outputTargetImaxCurrent != 0.0f) {
										bigValue = new BigDecimal(outputTargetImaxCurrent);
										bigValue = bigValue.setScale(setScaleCurrentAfterDecimal, RoundingMode.FLOOR);
										outputTargetImaxCurrent = bigValue.floatValue();
										yPhaseOutputTargetCurrentValue = String.valueOf(outputTargetImaxCurrent);

									}

									outputTargetImaxCurrent = Float.parseFloat(current_i3);
									if (outputTargetImaxCurrent != 0.0f) {
										bigValue = new BigDecimal(outputTargetImaxCurrent);
										bigValue = bigValue.setScale(setScaleCurrentAfterDecimal, RoundingMode.FLOOR);
										outputTargetImaxCurrent = bigValue.floatValue();
										bPhaseOutputTargetCurrentValue = String.valueOf(outputTargetImaxCurrent);
										// if(outputTargetImaxCurrent <=
										// maxDutPercentAndImaxRated){

									}

									rPhaseOutputTargetPfValue = pf1;
									String pf2 = testPointComponentData.getString("cus_phase_ph2");
									if ((pf2.equals("1")) || (pf2.equals("1.0"))) {
										pf2 = ConstantApp.PF_UPF;
									}
									yPhaseOutputTargetPfValue = pf2;
									String pf3 = testPointComponentData.getString("cus_phase_ph3");
									if ((pf3.equals("1")) || (pf3.equals("1.0"))) {
										pf3 = ConstantApp.PF_UPF;
									}
									bPhaseOutputTargetPfValue = pf3;
								}

								String testPeriodInMin = testPointComponentData.getString("time_duration");
								testPeriodInSec = Integer.parseInt(testPeriodInMin) * 60;
								testCaseDataElement.setTestPeriodInSec(testPeriodInSec);

								minErrorAllowed = testPointComponentData.getString("inf_emin");
								testCaseDataElement.setMinErrorAllowed(minErrorAllowed);
								maxErrorAllowed = testPointComponentData.getString("inf_emax");
								testCaseDataElement.setMaxErrorAllowed(maxErrorAllowed);

							} else {
								ApplicationLauncher.logger
										.debug("setupTargetValueForTestPoint: voltage: " + testCaseSplitData[1]);

								voltPercentStr = testCaseSplitData[1].replace(ConstantReport.EXTENSION_TYPE_VOLTAGE_U,
										"");
								voltPercentStr = voltPercentStr.replace(ConstantApp.VOLTAGE_PHASE_SPLITTER, "");
								voltPercentStr = voltPercentStr.replace(ConstantApp.FIRST_PHASE_DISPLAY_NAME, "");
								voltPercentStr = voltPercentStr.replace(ConstantApp.SECOND_PHASE_DISPLAY_NAME, "");
								voltPercentStr = voltPercentStr.replace(ConstantApp.THIRD_PHASE_DISPLAY_NAME, "");
								ApplicationLauncher.logger
										.debug("setupTargetValueForTestPoint: voltPercentStr2: " + voltPercentStr);
								voltPercentage = Float.parseFloat(voltPercentStr);
								outputTargetVolt = ratedVolt * voltPercentage / 100;
								rybOutputTargetVoltValue = String.valueOf(outputTargetVolt);
								rPhaseOutputTargetVoltValue = String.valueOf(outputTargetVolt);
								ApplicationLauncher.logger
										.debug("setupTargetValueForTestPoint: outputTargetVolt2: " + outputTargetVolt);

								if (!testCaseDataElement.getTesttype().equals(ConstantApp.TEST_PROFILE_WARMUP)) {
									if (!testCaseDataElement.getTesttype().equals(ConstantApp.TEST_PROFILE_NOLOAD)) {
										boolean iMaxHit = false;
										boolean ibHit = false;
										if (testCaseDataElement.getTesttype().equals(ConstantApp.TEST_PROFILE_STA)) {
											ApplicationLauncher.logger
													.debug("setupTargetValueForTestPoint: STA Test: ");
											currentPercentStr = testCaseSplitData[2];
											ApplicationLauncher.logger.debug(
													"setupTargetValueForTestPoint: STA: Current: " + currentPercentStr);
											if (currentPercentStr.contains(ConstantReport.EXTENSION_TYPE_CURRENT_IB)) {
												currentPercentStr = currentPercentStr
														.replace(ConstantReport.EXTENSION_TYPE_CURRENT_IB, "");
												currentPercentage = Float.parseFloat(currentPercentStr)
														* ConstantApp.CURRENT_PERCENT_CONVERTION_FACTOR;// 100;
												outputTargetIbCurrent = ratedIbCurrent * (currentPercentage) / 100;
												ApplicationLauncher.logger
														.debug("setupTargetValueForTestPoint: STA: ratedIbCurrent: "
																+ ratedIbCurrent);
												ApplicationLauncher.logger
														.debug("setupTargetValueForTestPoint: STA: outputTargetIbCurrent: "
																+ outputTargetIbCurrent);
												ibHit = true;
											} else if (currentPercentStr
													.contains(ConstantReport.EXTENSION_TYPE_CURRENT_IMAX)) {
												currentPercentStr = currentPercentStr
														.replace(ConstantReport.EXTENSION_TYPE_CURRENT_IMAX, "");
												currentPercentage = Float.parseFloat(currentPercentStr)
														* ConstantApp.CURRENT_PERCENT_CONVERTION_FACTOR;// 100;
												outputTargetImaxCurrent = ratedImaxCurrent * (currentPercentage) / 100;
												ApplicationLauncher.logger
														.debug("setupTargetValueForTestPoint: STA: ratedImaxCurrent: "
																+ ratedImaxCurrent);
												ApplicationLauncher.logger
														.debug("setupTargetValueForTestPoint: STA: outputTargetImaxCurrent: "
																+ outputTargetImaxCurrent);
												iMaxHit = true;
											}

											String testPeriodInMin = testPointComponentData.getString("time_duration");
											testPeriodInSec = Integer.parseInt(testPeriodInMin) * 60;
											testCaseDataElement.setTestPeriodInSec(testPeriodInSec);

											targetNoOfPulses = Integer
													.parseInt(testPointComponentData.getString("sta_test_pulse_no"));
											testCaseDataElement.setTargetNoOfPulses(targetNoOfPulses);

										} else {
											ApplicationLauncher.logger
													.debug("setupTargetValueForTestPoint: non-STA Test: ");
											ApplicationLauncher.logger.debug(
													"setupTargetValueForTestPoint: pf : " + testCaseSplitData[2]);
											ApplicationLauncher.logger.debug(
													"setupTargetValueForTestPoint: Current: " + testCaseSplitData[3]);
											currentPercentStr = testCaseSplitData[3];
											if (currentPercentStr.contains(ConstantReport.EXTENSION_TYPE_CURRENT_IB)) {
												currentPercentStr = currentPercentStr
														.replace(ConstantReport.EXTENSION_TYPE_CURRENT_IB, "");
												currentPercentage = Float.parseFloat(currentPercentStr)
														* ConstantApp.CURRENT_PERCENT_CONVERTION_FACTOR;// 100;
												outputTargetIbCurrent = ratedIbCurrent * (currentPercentage) / 100;
												ApplicationLauncher.logger
														.debug("setupTargetValueForTestPoint: ratedIbCurrent: "
																+ ratedIbCurrent);
												ApplicationLauncher.logger
														.debug("setupTargetValueForTestPoint: outputTargetIbCurrent: "
																+ outputTargetIbCurrent);
												ibHit = true;
											} else if (currentPercentStr
													.contains(ConstantReport.EXTENSION_TYPE_CURRENT_IMAX)) {
												currentPercentStr = currentPercentStr
														.replace(ConstantReport.EXTENSION_TYPE_CURRENT_IMAX, "");
												currentPercentage = Float.parseFloat(currentPercentStr)
														* ConstantApp.CURRENT_PERCENT_CONVERTION_FACTOR;// 100;
												outputTargetImaxCurrent = ratedImaxCurrent * (currentPercentage) / 100;
												ApplicationLauncher.logger
														.debug("setupTargetValueForTestPoint: ratedImaxCurrent: "
																+ ratedImaxCurrent);
												ApplicationLauncher.logger
														.debug("setupTargetValueForTestPoint: outputTargetImaxCurrent: "
																+ outputTargetImaxCurrent);
												iMaxHit = true;
											}

											minErrorAllowed = testPointComponentData.getString("inf_emin");
											testCaseDataElement.setMinErrorAllowed(minErrorAllowed);
											maxErrorAllowed = testPointComponentData.getString("inf_emax");
											testCaseDataElement.setMaxErrorAllowed(maxErrorAllowed);

											// String testPeriodInMin =
											// testPointComponentData.getString("time_duration");
											testPeriodInSec = Integer
													.parseInt(testPointComponentData.getString("time_duration"));
											testCaseDataElement.setTestPeriodInSec(testPeriodInSec);

										}
										// bigValue = new
										// BigDecimal(currentPercentage);
										// bigValue =
										// bigValue.setScale(setScaleCurrentAfterDecimal,
										// RoundingMode.FLOOR);
										// currentPercentage =
										// bigValue.floatValue();
										// ApplicationLauncher.logger.debug("setupTargetValueForTestPoint:
										// currentPercentage: " +
										// currentPercentage);
										if (iMaxHit) {
											if (outputTargetImaxCurrent != 0.0f) {
												bigValue = new BigDecimal(outputTargetImaxCurrent);
												bigValue = bigValue.setScale(setScaleCurrentAfterDecimal,
														RoundingMode.FLOOR);
												outputTargetImaxCurrent = bigValue.floatValue();
												rybOutputTargetCurrentValue = String.valueOf(outputTargetImaxCurrent);
											}

										} else if (ibHit) {
											if (outputTargetIbCurrent != 0.0f) {
												bigValue = new BigDecimal(outputTargetIbCurrent);
												bigValue = bigValue.setScale(setScaleCurrentAfterDecimal,
														RoundingMode.FLOOR);
												outputTargetIbCurrent = bigValue.floatValue();
												rybOutputTargetCurrentValue = String.valueOf(outputTargetIbCurrent);

											}

										}
										rybOutputTargetPfValue = GuiUtils
												.getPfDataFromTestPoint(testCaseDataElement.getTestCase());
									} else {
										ApplicationLauncher.logger.debug("setupTargetValueForTestPoint: NoLoad Test: ");

										String testPeriodInMin = testPointComponentData.getString("time_duration");
										testPeriodInSec = Integer.parseInt(testPeriodInMin) * 60;
										testCaseDataElement.setTestPeriodInSec(testPeriodInSec);

										targetNoOfPulses = Integer
												.parseInt(testPointComponentData.getString("creep_pulses"));
										testCaseDataElement.setTargetNoOfPulses(targetNoOfPulses);

									}

								} else {
									ApplicationLauncher.logger.debug("setupTargetValueForTestPoint: Warmup Test: ");
									// ApplicationLauncher.logger.debug("setupTargetValueForTestPoint:
									// pf : " + testCaseSplitData[2]);
									// ApplicationLauncher.logger.debug("setupTargetValueForTestPoint:
									// Current: " + testCaseSplitData[3]);
									// currentPercentStr = testCaseSplitData[3];
									// currentPercentStr =
									// currentPercentStr.replace(ConstantReport.EXTENSION_TYPE_CURRENT_IB,
									// "");
									currentPercentage = 100.0f;// Float.parseFloat(currentPercentStr)
																// *
																// ConstantApp.CURRENT_PERCENT_CONVERTION_FACTOR;//100;
									outputTargetIbCurrent = ratedIbCurrent * (currentPercentage) / 100;
									ApplicationLauncher.logger.debug(
											"setupTargetValueForTestPoint: Warmup: ratedIbCurrent: " + ratedIbCurrent);
									ApplicationLauncher.logger
											.debug("setupTargetValueForTestPoint: Warmup: outputTargetIbCurrent: "
													+ outputTargetIbCurrent);
									bigValue = new BigDecimal(outputTargetIbCurrent);
									bigValue = bigValue.setScale(setScaleCurrentAfterDecimal, RoundingMode.FLOOR);
									outputTargetIbCurrent = bigValue.floatValue();
									rybOutputTargetCurrentValue = String.valueOf(outputTargetIbCurrent);
									rybOutputTargetPfValue = GuiUtils
											.getPfDataFromTestPoint(testCaseDataElement.getTestCase());

									String warmupPeriodInMin = testPointComponentData.getString("time_duration");
									warmupPeriodInSec = Integer.parseInt(warmupPeriodInMin) * 60;
									testCaseDataElement.setWarmupPeriodInSec(warmupPeriodInSec);
									minErrorAllowed = testPointComponentData.getString("inf_emin");
									testCaseDataElement.setMinErrorAllowed(minErrorAllowed);
									maxErrorAllowed = testPointComponentData.getString("inf_emax");
									testCaseDataElement.setMaxErrorAllowed(maxErrorAllowed);

								}
								if (modelType.contains(ConstantApp.METERTYPE_THREEPHASE)) {
									// rybOutputTargetVoltValue =
									// String.valueOf(outputTargetVolt);
								} else {
									// rPhaseOutputTargetVoltValue =
									// String.valueOf(outputTargetVolt);
								}

								if (testCaseDataElement.getTestCase().startsWith(ConstantApp.FREQUENCY_ALIAS_NAME
										+ ConstantReportV2.TEST_POINT_NAME_SEPERATOR)) {
									targetFreq = testPointComponentData.getString("frequency");
								} else if (testCaseDataElement.getTestCase().contains(ConstantApp.CONST_TEST_ALIAS_NAME
										+ ConstantReportV2.TEST_POINT_NAME_SEPERATOR)) {
									targetEnergy = testPointComponentData.getString("energy");
								}

							}

							testCaseDataElement.setTarget_RYB_Voltage(rybOutputTargetVoltValue);
							testCaseDataElement.setTarget_R_Voltage(rPhaseOutputTargetVoltValue);
							testCaseDataElement.setTarget_Y_Voltage(yPhaseOutputTargetVoltValue);
							testCaseDataElement.setTarget_B_Voltage(bPhaseOutputTargetVoltValue);

							testCaseDataElement.setTarget_RYB_Current(rybOutputTargetCurrentValue);
							testCaseDataElement.setTarget_R_Current(rPhaseOutputTargetCurrentValue);
							testCaseDataElement.setTarget_Y_Current(yPhaseOutputTargetCurrentValue);
							testCaseDataElement.setTarget_B_Current(bPhaseOutputTargetCurrentValue);

							testCaseDataElement.setTarget_RYB_Pf(rybOutputTargetPfValue);
							testCaseDataElement.setTarget_R_Pf(rPhaseOutputTargetPfValue);
							testCaseDataElement.setTarget_Y_Pf(yPhaseOutputTargetPfValue);
							testCaseDataElement.setTarget_B_Pf(bPhaseOutputTargetPfValue);

							testCaseDataElement.setTargetFreq(targetFreq);
							testCaseDataElement.setTargetEnergy(targetEnergy);

							runType = testPointComponentData.getString("testruntype");
							testCaseDataElement.setRunType(runType);

							/*
							 * try{
							 * if(testPointComponentData.getString("reading_id")
							 * !=null){
							 * if(GuiUtils.isNumber(testPointComponentData.
							 * getString("reading_id"))){ readingId =
							 * Integer.parseInt(testPointComponentData.getString
							 * ("reading_id")); } } }catch (Exception e){
							 * e.printStackTrace();
							 * ApplicationLauncher.logger.error(
							 * "setupTargetValueForTestPoint: Exception reading_id: "
							 * +e.getMessage()); }
							 */
							// readingId = 20;

							testCaseDataElement.setReadingId(readingId);

							/*
							 * try{ if(testPointComponentData.getString(
							 * "target_average_count")!=null){
							 * if(GuiUtils.isNumber(testPointComponentData.
							 * getString("target_average_count"))){
							 * targetAverageCount =
							 * Integer.parseInt(testPointComponentData.getString
							 * ("target_average_count")); } } }catch (Exception
							 * e){ e.printStackTrace();
							 * ApplicationLauncher.logger.error(
							 * "setupTargetValueForTestPoint: Exception target_average_count: "
							 * +e.getMessage()); }
							 */
							// targetAverageCount = 15;

							testCaseDataElement.setTargetAverageCount(targetAverageCount);

							/* testSubType = ???; */

							/*
							 * try{ if(testPointComponentData.getString(
							 * "test_sub_type")!=null){ testSubType =
							 * testPointComponentData.getString("test_sub_type")
							 * ;
							 * 
							 * } }catch (Exception e){ e.printStackTrace();
							 * ApplicationLauncher.logger.error(
							 * "setupTargetValueForTestPoint: Exception test_sub_type: "
							 * +e.getMessage()); }
							 */

							// testSubType="SubType1";

							testCaseDataElement.setTestSubType(testSubType);

						} catch (Exception e) {
							e.printStackTrace();
							ApplicationLauncher.logger
									.error("setupTargetValueForTestPoint: Exception2: " + e.getMessage());
						}

					}

					// MySQL_Controller.sp_add_deploy_test_cases(lastUpdatedDeploymentID,project_name,
					// dataElement.getTestCase(),dataElement.getTesttype(),
					// dataElement.getAliasid(), String.valueOf(
					// DeploymentSequenceNo++), "Y");

				}
				// DeploymentSequenceNo++;
				/*
				 * else{// PerformanceIssue
				 * MySQL_Controller.sp_add_deploy_test_cases(project_name,
				 * testcase,testtype, aliasid, sequence_no, "N"); }
				 */
			}
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("setupTargetValueForTestPoint: Exception: " + e.getMessage());
		}

	}

	public void LoadTestPointToDB(String lastUpdatedDeploymentID) {
		ApplicationLauncher.logger.info("LoadTestPointToDB: Entry");
		String project_name = MeterParamsController.getCurrentProjectName();
		Iterator<DeploymentTestCaseDataModel> itr = testCasesData.iterator();
		DeploymentTestCaseDataModel dataElement = new DeploymentTestCaseDataModel("", true, "", "", "");
		/*
		 * String testcase= ""; String testtype= ""; String aliasid= "";
		 */
		int DeploymentSequenceNo = 1;
		try {
			while (itr.hasNext()) {
				dataElement = itr.next();

				if (dataElement.getIsSelected()) {
					setTestCaseSelected(true);

					MySQL_Controller.sp_add_deploy_test_cases(lastUpdatedDeploymentID, project_name,
							dataElement.getTestCase(), dataElement.getTesttype(), dataElement.getAliasid(),
							String.valueOf(DeploymentSequenceNo++), "Y");

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("LoadTestPointToDB: Exception: " + e.getMessage());
		}
	}

	public boolean LoadTestPointToDB_V2(String lastUpdatedDeploymentID) {
		ApplicationLauncher.logger.info("LoadTestPointToDB_V2: Entry");
		String project_name = MeterParamsController.getCurrentProjectName();
		Iterator<DeploymentTestCaseDataModel> itr = testCasesData.iterator();
		DeploymentTestCaseDataModel dataElement = new DeploymentTestCaseDataModel("", true, "", "", "");
		boolean status = true;
		/*
		 * String testcase= ""; String testtype= ""; String aliasid= "";
		 */
		int DeploymentSequenceNo = 1;
		try {
			while (itr.hasNext()) {
				dataElement = itr.next();

				if (dataElement.getIsSelected()) {
					setTestCaseSelected(true);

					// MySQL_Controller.sp_add_deploy_test_cases(lastUpdatedDeploymentID,project_name,
					// dataElement.getTestCase(),dataElement.getTesttype(),
					// dataElement.getAliasid(), String.valueOf(
					// DeploymentSequenceNo++), "Y");
					status = MySQL_Controller.sp_add_deploy_test_cases_v2(lastUpdatedDeploymentID, project_name,
							String.valueOf(DeploymentSequenceNo++), "Y", dataElement);
					if (!status) {
						ApplicationLauncher.logger.info("LoadTestPointToDB_V2: deployment failed exiting loop");
						break;
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("LoadTestPointToDB: Exception: " + e.getMessage());
		}

		return status;
	}

	@SuppressWarnings("null")
	public long calcEpoch(String Date_time) {
		long epoch = 0;
		// String str = "2014-07-04 04:05:10"; // UTC
		String str = Date_time; // UTC

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date datenew = null;
		try {
			datenew = df.parse(str);
		} catch (ParseException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("calcEpoch: ParseException:" + e.getMessage());
		}
		epoch = datenew.getTime() / 1000;

		// ApplicationLauncher.logger.info(epoch);
		return epoch;
	}

	public void ProcessAutoFill(String serial_no) {
		ArrayList<Object> ser_no_data = GetData_From_serialno(serial_no);
		String ser_prefix = (String) ser_no_data.get(0);
		int serial_number = (int) ser_no_data.get(1);
		ApplicationLauncher.logger.debug("ProcessAutoFill: ser_prefix:" + ser_prefix);
		ArrayList<String> serial_no_list = new ArrayList<String>();
		for (int i = 0; i < ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK; i++) {
			/*
			 * String serial = ser_prefix + Integer.toString(serial_number);
			 * serial_number++; serial_no_list.add(serial);
			 */

			String serial = "";

			if (ConstantAppConfig.DUT_CALIB_METER_SERIAL_NO_FIXED_LENGTH == 0) {
				// Use the serial number as-is
				serial = ser_prefix + serial_number;
			} else {
				// Pad with leading zeros to match the desired fixed length
				String paddedSerial = String
						.format("%0" + ConstantAppConfig.DUT_CALIB_METER_SERIAL_NO_FIXED_LENGTH + "d", serial_number);
				serial = ser_prefix + paddedSerial;
			}

			serial_number++;
			serial_no_list.add(serial);
		}
		ApplicationLauncher.logger.debug("ProcessAutoFill: serial_no_list:" + serial_no_list);
		LoadToTable(serial_no_list);

	}

	@FXML
	public void autoFillOnClick() {
		String serial_no = txt_serialno.getText();
		if (!serial_no.isEmpty()) {
			if (!ProcalFeatureEnable.EXPORT_MODE_ENABLED) {
				ProcessAutoFill(serial_no);
				/*
				 * ArrayList<Object> ser_no_data =
				 * GetData_From_serialno(serial_no); String ser_prefix =
				 * (String) ser_no_data.get(0); int serial_number = (int)
				 * ser_no_data.get(1); ApplicationLauncher.logger.info(
				 * "ser_prefix: " + ser_prefix); ArrayList<String>
				 * serial_no_list = new ArrayList<String>(); for(int i=0;
				 * i<ConstantApp.TOTAL_NO_OF_SUPPORTED_RACK; i++){ String serial
				 * = ser_prefix + Integer.toString(serial_number);
				 * serial_number++; serial_no_list.add(serial); }
				 * ApplicationLauncher.logger.info(serial_no_list);
				 * LoadToTable(serial_no_list);
				 */
			} else {
				if ((!serial_no.toUpperCase().contains(ConstantApp.EXPORT_MODE_ALIAS_NAME))
						&& (!serial_no.toUpperCase().contains(ConstantApp.EXPORT_MODE_ALIAS_NAME.trim()))) {
					ProcessAutoFill(serial_no);
				} else {
					ApplicationLauncher.logger.info("autoFillOnClick: serial_no:" + serial_no
							+ ". autofill not accepted prompt -ErrorCode - U003");
					ApplicationLauncher.InformUser("ErrorCode - U003", "Auto fill input contains unaccepted data <"
							+ ConstantApp.EXPORT_MODE_ALIAS_NAME + ">  , kindly rephrase!!", AlertType.ERROR);
				}
			}
		} else {
			ApplicationLauncher.logger.info(
					"autoFillOnClick : ErrorCode - DU001: <Serial No> is Empty. kindly fill the same and try again - user prompted");
			ApplicationLauncher.InformUser("ErrorCode - DU001",
					"<Serial No> is Empty. kindly fill the same and try again", AlertType.ERROR);

		}
	}

	public ArrayList<Object> GetData_From_serialno(String serial_no) {
		ArrayList<Object> result = new ArrayList<Object>();
		String lastchar = serial_no.substring(serial_no.length() - 1);
		String serial_no_int = "";
		int len = serial_no.length();
		ApplicationLauncher.logger.info("GetData_From_serialno : lastchar: " + lastchar);
		while (len > 0) {
			if (CheckInt(lastchar)) {
				serial_no_int = lastchar + serial_no_int;
				ApplicationLauncher.logger.info("GetData_From_serialno: serial_no_int" + serial_no_int);
				serial_no = serial_no.substring(0, serial_no.length() - 1);
				ApplicationLauncher.logger.info("GetData_From_serialno: serial_no: " + serial_no);
				if (serial_no.length() > 0) {
					lastchar = serial_no.substring(serial_no.length() - 1);
					ApplicationLauncher.logger.info("GetData_From_serialno: lastchar: " + lastchar);
				}
				len--;
			} else {
				len = 0;
			}
		}
		int s_no = 1;
		try {
			if (!serial_no_int.isEmpty()) {
				if (GuiUtils.isNumber(serial_no_int)) {
					s_no = Integer.parseInt(serial_no_int);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("GetData_From_serialno: Exception: " + e.getMessage());
		}
		ApplicationLauncher.logger.info("GetData_From_serialno: s_no: " + s_no);
		ApplicationLauncher.logger.info("GetData_From_serialno: serial_no:" + serial_no);
		result.add(serial_no);
		result.add(s_no);
		return result;
	}

	public boolean CheckInt(String lastchar) {
		try {
			if (GuiUtils.isNumber(lastchar)) {
				Integer.parseInt(lastchar);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("CheckInt: Exception:" + e.getMessage());
			return false;
		}

	}

	public void LoadToTable(ArrayList<String> serialnolist) {
		loadSerialNo(serialnolist);
	}

	@SuppressWarnings("unchecked")
	public void loadSerialNo(ArrayList<String> serialnolist) {

		typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
		imaxDataColumn.setCellValueFactory(cellData -> cellData.getValue().iMaxProperty());
		ibaseDataColumn.setCellValueFactory(cellData -> cellData.getValue().lbProperty());
		voltageColumn.setCellValueFactory(cellData -> cellData.getValue().vdProperty());
		devicesSelectedColumn.setCellValueFactory(new DeploymentDevicesCheckBoxValueFactory());

		serialnoColumn.setCellValueFactory(new PropertyValueFactory<DeploymentDataModel, String>("serialno"));
		serialnoColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		serialnoColumn.setOnEditCommit(new EventHandler<CellEditEvent<DeploymentDataModel, String>>() {
			public void handle(CellEditEvent<DeploymentDataModel, String> t) {
				ApplicationLauncher.logger.info("loadSerialNo: setSerialno: Entry");
				DeploymentDataModel rowData = ((DeploymentDataModel) t.getTableView().getItems()
						.get(t.getTablePosition().getRow()));

				// rowData.setSerialno(t.getNewValue());

				String serial_no = t.getNewValue();
				if (!ProcalFeatureEnable.EXPORT_MODE_ENABLED) {
					rowData.setSerialno(t.getNewValue());
				} else if (ProcalFeatureEnable.EXPORT_MODE_ENABLED) {

					if ((!serial_no.toUpperCase().contains(ConstantApp.EXPORT_MODE_ALIAS_NAME))
							&& (!serial_no.toUpperCase().contains(ConstantApp.EXPORT_MODE_ALIAS_NAME.trim()))) {
						rowData.setSerialno(t.getNewValue());
					} else {
						ApplicationLauncher.logger.info("DeploymentManagerController : loadSerialNo: handle: serial_no:"
								+ serial_no + ". Modified data input not accepted prompt-ErrorCode - U001");
						ApplicationLauncher.InformUser(
								"ErrorCode - U001", "Modified data input contains unaccepted value <"
										+ ConstantApp.EXPORT_MODE_ALIAS_NAME + ">  , kindly rephrase!!",
								AlertType.ERROR);

						devicesDataTable.refresh();
					}
				}

			}
		});

		meterMakeColumn.setCellValueFactory(new PropertyValueFactory<DeploymentDataModel, String>("meterMake"));
		meterMakeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		meterMakeColumn.setOnEditCommit(new EventHandler<CellEditEvent<DeploymentDataModel, String>>() {
			public void handle(CellEditEvent<DeploymentDataModel, String> t) {
				ApplicationLauncher.logger.info("loadSerialNo: meterMake: Entry");
				DeploymentDataModel rowData = ((DeploymentDataModel) t.getTableView().getItems()
						.get(t.getTablePosition().getRow()));

				// rowData.setSerialno(t.getNewValue());

				String meterMake = t.getNewValue();
				if (!ProcalFeatureEnable.EXPORT_MODE_ENABLED) {
					rowData.setMeterMake(t.getNewValue());
				} else if (ProcalFeatureEnable.EXPORT_MODE_ENABLED) {

					if ((!meterMake.toUpperCase().contains(ConstantApp.EXPORT_MODE_ALIAS_NAME))
							&& (!meterMake.toUpperCase().contains(ConstantApp.EXPORT_MODE_ALIAS_NAME.trim()))) {
						rowData.setMeterMake(t.getNewValue());
					} else {
						ApplicationLauncher.logger.info("DeploymentManagerController : loadSerialNo: handle: meterMake:"
								+ meterMake + ". Modified data input not accepted prompt-ErrorCode - U004");
						ApplicationLauncher.InformUser(
								"ErrorCode - U004", "Modified data input contains unaccepted value <"
										+ ConstantApp.EXPORT_MODE_ALIAS_NAME + ">  , kindly rephrase!!",
								AlertType.ERROR);

						devicesDataTable.refresh();
					}
				}

			}
		});

		ctr_Column.setCellValueFactory(new PropertyValueFactory<DeploymentDataModel, Float>("ctrratio"));
		ctr_Column.setCellFactory(EditCell.<DeploymentDataModel, Float> forTableColumn(new MyFloatStringConverter()));
		ctr_Column.setOnEditCommit(new EventHandler<CellEditEvent<DeploymentDataModel, Float>>() {
			public void handle(CellEditEvent<DeploymentDataModel, Float> t) {
				DeploymentDataModel rowData = ((DeploymentDataModel) t.getTableView().getItems()
						.get(t.getTablePosition().getRow()));
				if (t.getNewValue() != null) {
					rowData.setctrratio(t.getNewValue());
				}
			}
		});
		ptr_Column.setCellValueFactory(new PropertyValueFactory<DeploymentDataModel, Float>("ptrratio"));
		ptr_Column.setCellFactory(EditCell.<DeploymentDataModel, Float> forTableColumn(new MyFloatStringConverter()));
		ptr_Column.setOnEditCommit(new EventHandler<CellEditEvent<DeploymentDataModel, Float>>() {
			public void handle(CellEditEvent<DeploymentDataModel, Float> t) {
				DeploymentDataModel rowData = ((DeploymentDataModel) t.getTableView().getItems()
						.get(t.getTablePosition().getRow()));
				if (t.getNewValue() != null) {
					rowData.setptrratio(t.getNewValue());
				}
			}
		});
		meterConstColumn.setCellValueFactory(new PropertyValueFactory<DeploymentDataModel, Integer>("meterconst"));
		meterConstColumn
				.setCellFactory(EditCell.<DeploymentDataModel, Integer> forTableColumn(new MyIntegerStringConverter()));
		meterConstColumn.setOnEditCommit(new EventHandler<CellEditEvent<DeploymentDataModel, Integer>>() {
			public void handle(CellEditEvent<DeploymentDataModel, Integer> t) {
				DeploymentDataModel rowData = ((DeploymentDataModel) t.getTableView().getItems()
						.get(t.getTablePosition().getRow()));
				if (t.getNewValue() != null) {
					rowData.setmeterconst(t.getNewValue());
				}
			}
		});

		devicesDataTable.refresh();
		ObservableList<DeploymentDataModel> dep_device_data = devicesDataTable.getItems();
		ObservableList<DeploymentDataModel> refreshed_row_data = FXCollections.observableArrayList();
		for (int i = 0; i < dep_device_data.size(); i++) {
			DeploymentDataModel device = devicesData.get(i);
			device.setSerialno(serialnolist.get(i));
			refreshed_row_data.add(device);
		}
		devicesDataTable.setItems(refreshed_row_data);
		// devicesDataTable.refresh();
	}

	public void selectAllRackOnClick() {
		ApplicationLauncher.logger.info("selectAllRackOnClick: Entry");
		ObservableList<DeploymentDataModel> ref_devicesData = FXCollections.observableArrayList();

		Iterator<DeploymentDataModel> itr = devicesData.iterator();
		try {
			while (itr.hasNext()) {
				DeploymentDataModel dataElement = itr.next();
				if (selectAll_RackActive) {
					dataElement.setIsSelected(true);
				} else {
					dataElement.setIsSelected(false);
				}

			}
			if (selectAll_RackActive) {
				btn_SelectAllRack.setText("Unselect All Rack");
				selectAll_RackActive = false;
			} else {
				btn_SelectAllRack.setText("Select All Rack");
				selectAll_RackActive = true;
			}
			ref_devicesData.addAll(devicesData);
			devicesDataTable.getItems().clear();
			devicesData.addAll(ref_devicesData);
			devicesDataTable.setItems(devicesData);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("selectAllRackOnClick: Exception: " + e.getMessage());
		}
	}

	public void selectAllTCOnClick() {
		ApplicationLauncher.logger.info("selectAllTCOnClick: Entry");
		ObservableList<DeploymentTestCaseDataModel> ref_testCasesData = FXCollections.observableArrayList();

		Iterator<DeploymentTestCaseDataModel> itr = testCasesData.iterator();
		try {
			while (itr.hasNext()) {

				DeploymentTestCaseDataModel dataElement = itr.next();
				if (selectAll_TC_Active) {

					dataElement.setIsSelected(true);
				} else {

					dataElement.setIsSelected(false);
				}

			}
			if (selectAll_TC_Active) {
				btn_SelectAllTC.setText("Unselect All TC");
				selectAll_TC_Active = false;
			} else {
				btn_SelectAllTC.setText("Select All TC");
				selectAll_TC_Active = true;
			}
			ref_testCasesData.addAll(testCasesData);
			testCasesDataTable.getItems().clear();
			testCasesData.addAll(ref_testCasesData);
			testCasesDataTable.setItems(testCasesData);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("selectAllRackOnClick: Exception: " + e.getMessage());
		}
	}

	public static boolean IsAllMeterConstSame() {
		return AllMeterConstSame;
	}

	public static void setAllMeterConstSame(boolean value) {
		AllMeterConstSame = value;
	}

	public void CheckAllMeterConstSame(ArrayList<Integer> meter_const_arr) {
		int ref_meter_const_value = meter_const_arr.get(0);
		setAllMeterConstSame(true);
		for (int i = 1; i < meter_const_arr.size(); i++) {
			int value = meter_const_arr.get(i);
			if (!(ref_meter_const_value == value)) {
				setAllMeterConstSame(false);
			}
		}
	}

	/*
	 * public void InformUser(String title, String info,AlertType Alert_type){
	 * TextBoxDialog TextBoxDialogobj = new TextBoxDialog();
	 * TextBoxDialogobj.TriggerUserInfoPlatFormLater(title, info, Alert_type); }
	 */
	public static long getDeploymentStartTime() {
		return deploymentStartTime;
	}

	public static void setDeploymentStartTime(long value) {
		deploymentStartTime = value;
	}

	public void startDeployTrigger() {
		ApplicationLauncher.logger.debug("startDeployTrigger Invoked:");
		DeployTaskTimer = new Timer();
		DeployTaskTimer.schedule(new startDeployTask(), 100);
	}

	@FXML
	public void importDeploymentDataOnClick() {
		ApplicationLauncher.logger.debug("importDeploymentDataOnClick Invoked:");

		FileChooser fileChooser = new FileChooser();

		fileChooser.setInitialDirectory(new File(lastVisitedDirectory));
		fileChooser.getExtensionFilters().add(new ExtensionFilter("CSV Files", "*.csv"));
		fileChooser.setTitle("Open CSV File");
		File file = fileChooser.showOpenDialog(ApplicationLauncher.getPrimaryStage().getScene().getWindow());
		// lastVisitedDirectory=(file!=null &&
		// file.size()>=1)?file.get(0).getParent():System.getProperty("user.home");
		lastVisitedDirectory = (file != null) ? file.getParent() : System.getProperty("user.home");
		if (file != null) {
			ApplicationLauncher.logger.debug("importDeploymentDataOnClick : file: " + file.toString());
			importDataTimer = new Timer();
			importDataTimer.schedule(new importDeploymentDataTask(file.toString()), 100);
		}

	}

	class importDeploymentDataTask extends TimerTask {
		String fileNameWithPath = "";// "C:\\TAS_Network\\Projects\\LS_Controls\\ProCAL\\DeploymentImport-V0_1.csv";

		importDeploymentDataTask(String inputFileName) {
			fileNameWithPath = inputFileName;
		}

		public void run() {

			processCsvFile(fileNameWithPath);

			importDataTimer.cancel();
		}
	}

	public void processCsvFile(String fileNameWithPath) {

		try {
			// graph.getData().remove(0);
			// FileReader fr = new FileReader("C:\\temp\\harmonics-v1.csv");

			// String fileNameWithPath =
			// "C:\\TAS_Network\\Projects\\LS_Controls\\ProCAL\\DeploymentImport-V0_1.csv";
			FileReader fr = new FileReader(fileNameWithPath);
			BufferedReader br = new BufferedReader(fr);
			String line;
			boolean headerReadCompleted = false;
			int noOfHeader = 0;
			ArrayList<String> headerNameList = new ArrayList<String>();

			ArrayList<String> rackIdList = new ArrayList<String>();
			ArrayList<String> rackIdPositionSelectedList = new ArrayList<String>();
			ArrayList<String> dutSerialNoList = new ArrayList<String>();
			ArrayList<String> meterMakeList = new ArrayList<String>();
			ArrayList<String> ctRatioList = new ArrayList<String>();
			ArrayList<String> ptRatioList = new ArrayList<String>();
			ArrayList<String> meterConstantList = new ArrayList<String>();
			ArrayList<String> meterModelNoList = new ArrayList<String>();
			while ((line = br.readLine()) != null) {
				String[] s = line.split(",");
				if (headerReadCompleted) {
					// ApplicationLauncher.logger.debug("plotLine: s[0]: "
					// +s[0]);
					// ApplicationLauncher.logger.debug("plotLine: s[1]: "
					// +s[1]);
					// plotPoint(Integer.parseInt(s[0]), Integer.parseInt(s[1]),
					// series);
					for (int headerIndex = 0; headerIndex < noOfHeader; headerIndex++) {

						if (headerNameList.get(headerIndex)
								.equals(ConstantAppConfig.IMPORT_DEPLOYMENT_DATA_RACK_POSITION_ID_HEADER)) {
							// ApplicationLauncher.logger.debug("processCsvFile:
							// headerIndex: test1: " );
							try {
								if (!s[headerIndex].isEmpty()) {
									// ApplicationLauncher.logger.debug("processCsvFile:
									// headerIndex: test2: " );
									rackIdList.add(s[headerIndex]);
									// ApplicationLauncher.logger.debug("importDeploymentDataOnClick:
									// headerIndex: " + headerIndex);
									ApplicationLauncher.logger
											.debug("processCsvFile: headerIndex: rackId: " + s[headerIndex]);
									// ApplicationLauncher.logger.debug("processCsvFile:
									// headerIndex: rackIdList: " +
									// rackIdList.get(headerIndex));
									// ApplicationLauncher.logger.debug("processCsvFile:
									// headerIndex: rackIdList2: " +
									// rackIdList);
									for (int dataIndex = 0; dataIndex < noOfHeader; dataIndex++) {
										ApplicationLauncher.logger.debug("processCsvFile: dataIndex: headerNameList: "
												+ headerNameList.get(dataIndex));
										if (headerNameList.get(dataIndex)
												.equals(ConstantAppConfig.IMPORT_DEPLOYMENT_DATA_SERIAL_NO_HEADER)) {
											try {
												ApplicationLauncher.logger.debug(
														"processCsvFile: dataIndex1: s[dataIndex]: " + s[dataIndex]);
												dutSerialNoList.add(s[dataIndex]);
												ApplicationLauncher.logger
														.debug("processCsvFile: dutSerialNoList: " + dutSerialNoList);
											} catch (Exception e) {
												ApplicationLauncher.logger
														.error("processCsvFile: dutSerialNoList: Exception: "
																+ e.getMessage());
												ApplicationLauncher.logger
														.debug("processCsvFile: dutSerialNoList: Exception: "
																+ ConstantAppConfig.IMPORT_DEPLOYMENT_DATA_SERIAL_NO_HEADER);
												ApplicationLauncher.logger
														.debug("processCsvFile: dutSerialNoList: Empty");
												dutSerialNoList.add("");
											}

										} else if (headerNameList.get(dataIndex)
												.equals(ConstantAppConfig.IMPORT_DEPLOYMENT_DATA_CT_RATIO_HEADER)) {
											try {
												ApplicationLauncher.logger.debug(
														"processCsvFile: dataIndex2: s[dataIndex]: " + s[dataIndex]);
												ctRatioList.add(s[dataIndex]);
												// ApplicationLauncher.logger.debug("processCsvFile:
												// ctRatioList: " +
												// ctRatioList.get(dataIndex));
												ApplicationLauncher.logger
														.debug("processCsvFile: ctRatioList2: " + ctRatioList);

											} catch (Exception e) {
												ApplicationLauncher.logger.error(
														"processCsvFile: ctRatioList: Exception: " + e.getMessage());
												ApplicationLauncher.logger
														.debug("processCsvFile: ctRatioList: Exception: "
																+ ConstantAppConfig.IMPORT_DEPLOYMENT_DATA_CT_RATIO_HEADER);
												ApplicationLauncher.logger.debug("processCsvFile: ctRatioList: Empty");
												ctRatioList.add("");
											}

										} else if (headerNameList.get(dataIndex)
												.equals(ConstantAppConfig.IMPORT_DEPLOYMENT_DATA_PT_RATIO_HEADER)) {
											try {
												ApplicationLauncher.logger.debug(
														"processCsvFile: dataIndex3: s[dataIndex]: " + s[dataIndex]);
												ptRatioList.add(s[dataIndex]);
												ApplicationLauncher.logger
														.debug("processCsvFile: ptRatioList: " + ptRatioList);

											} catch (Exception e) {
												ApplicationLauncher.logger.error(
														"processCsvFile: ptRatioList: Exception: " + e.getMessage());
												ApplicationLauncher.logger
														.debug("processCsvFile: ptRatioList: Exception: "
																+ ConstantAppConfig.IMPORT_DEPLOYMENT_DATA_PT_RATIO_HEADER);
												ApplicationLauncher.logger.debug("processCsvFile: ptRatioList: Empty");
												ptRatioList.add("");
											}

										} else if (headerNameList.get(dataIndex).equals(
												ConstantAppConfig.IMPORT_DEPLOYMENT_DATA_METER_CONSTANT_HEADER)) {
											try {
												ApplicationLauncher.logger.debug(
														"processCsvFile: dataIndex4: s[dataIndex]: " + s[dataIndex]);

												// meterConstantList.add(Integer.parseInt(s[dataIndex]));
												meterConstantList.add(s[dataIndex]);
												ApplicationLauncher.logger.debug(
														"processCsvFile: meterConstantList: " + meterConstantList);
											} catch (Exception e) {
												ApplicationLauncher.logger
														.error("processCsvFile: meterConstantList: Exception: "
																+ e.getMessage());
												ApplicationLauncher.logger
														.debug("processCsvFile: meterConstantList: Exception: "
																+ ConstantAppConfig.IMPORT_DEPLOYMENT_DATA_METER_CONSTANT_HEADER);
												ApplicationLauncher.logger
														.debug("processCsvFile: meterConstantList: Empty");
												meterConstantList.add("");
											}

										} else if (headerNameList.get(dataIndex)
												.equals(ConstantAppConfig.IMPORT_DEPLOYMENT_DATA_MAKE_HEADER)) {
											try {
												ApplicationLauncher.logger.debug(
														"processCsvFile: dataIndex5: s[dataIndex]: " + s[dataIndex]);
												meterMakeList.add(s[dataIndex]);
												ApplicationLauncher.logger
														.debug("processCsvFile: meterMakeList: " + meterMakeList);

											} catch (Exception e) {
												ApplicationLauncher.logger.error(
														"processCsvFile: meterMakeList: Exception: " + e.getMessage());
												ApplicationLauncher.logger
														.debug("processCsvFile: meterMakeList: Exception: "
																+ ConstantAppConfig.IMPORT_DEPLOYMENT_DATA_MAKE_HEADER);
												ApplicationLauncher.logger
														.debug("processCsvFile: meterMakeList: Empty");
												meterMakeList.add("");
											}

										} else if (headerNameList.get(dataIndex).equals(
												ConstantAppConfig.IMPORT_DEPLOYMENT_DATA_RACK_POSITION_SELECTED_HEADER)) {
											try {
												ApplicationLauncher.logger.debug(
														"processCsvFile: dataIndex5: s[dataIndex]: " + s[dataIndex]);
												if ((s[dataIndex].toLowerCase().equals("y"))
														|| (s[dataIndex].toLowerCase().equals("yes"))) {
													// rackIdPositionSelectedList.add(true);
													rackIdPositionSelectedList.add("Y");
												} else {
													// rackIdPositionSelectedList.add(false);
													rackIdPositionSelectedList.add("N");
												}
											} catch (Exception e) {
												ApplicationLauncher.logger
														.error("processCsvFile: rackIdPositionSelectedList: Exception: "
																+ e.getMessage());
												ApplicationLauncher.logger
														.debug("processCsvFile: rackIdPositionSelectedList: Exception: "
																+ ConstantAppConfig.IMPORT_DEPLOYMENT_DATA_RACK_POSITION_SELECTED_HEADER);
												ApplicationLauncher.logger
														.debug("processCsvFile: rackIdPositionSelectedList: UNDEFINED");
												rackIdPositionSelectedList.add("UNDEFINED");
											}

											ApplicationLauncher.logger
													.debug("processCsvFile: rackIdPositionSelectedList: "
															+ rackIdPositionSelectedList);

										} else if (headerNameList.get(dataIndex)
												.equals(ConstantAppConfig.IMPORT_DEPLOYMENT_DATA_MODEL_NO_HEADER)) {
											try {
												ApplicationLauncher.logger.debug(
														"processCsvFile: dataIndex5: s[dataIndex]: " + s[dataIndex]);
												meterModelNoList.add(s[dataIndex]);
												ApplicationLauncher.logger
														.debug("processCsvFile: meterModelNoList: " + meterModelNoList);

											} catch (Exception e) {
												ApplicationLauncher.logger
														.error("processCsvFile: meterModelNoList: Exception: "
																+ e.getMessage());
												ApplicationLauncher.logger
														.debug("processCsvFile: meterModelNoList: Exception: "
																+ ConstantAppConfig.IMPORT_DEPLOYMENT_DATA_MODEL_NO_HEADER);
												ApplicationLauncher.logger
														.debug("processCsvFile: meterModelNoList: Empty");
												meterModelNoList.add("");
											}

										}
									}

								}
							} catch (Exception e) {
								ApplicationLauncher.logger
										.error("processCsvFile: rackIdList: Exception: " + e.getMessage());
								ApplicationLauncher.logger.debug("processCsvFile: rackIdList: Exception: "
										+ ConstantAppConfig.IMPORT_DEPLOYMENT_DATA_RACK_POSITION_ID_HEADER);
								// ApplicationLauncher.logger.debug("processCsvFile:
								// rackIdList: Empty");
								// rackIdList.add("");
							}
						}

					}

					// }
					// }
				} else {
					headerReadCompleted = true;
					noOfHeader = s.length;
					ApplicationLauncher.logger.debug("processCsvFile noOfHeader: " + noOfHeader);
					for (int i = 0; i < noOfHeader; i++) {
						headerNameList.add(s[i]);
						ApplicationLauncher.logger.debug("processCsvFile headerNameList: " + headerNameList.get(i));
					}
				}

				/*
				 * ZonedDateTime zdt = ZonedDateTime.parse(s[0]); Second second
				 * = new Second(Date.from(zdt.toInstant())); series.add(second,
				 * Double.valueOf(s[2]));
				 */
			}

			devicesDataTable.refresh();
			// ObservableList<DeploymentDataModel> dep_device_data =
			// devicesDataTable.getItems();
			// ObservableList<DeploymentDataModel> refreshed_row_data =
			// FXCollections.observableArrayList();

			for (int headerIndex = 0; headerIndex < rackIdList.size(); headerIndex++) {
				for (int i = 0; i < devicesData.size(); i++) {
					DeploymentDataModel device = devicesData.get(i);
					// device.setSerialno(serialnolist.get(i));
					// ApplicationLauncher.logger.debug("processCsvFile
					// rackIdList.get(headerIndex): " +
					// rackIdList.get(headerIndex));
					// ApplicationLauncher.logger.debug("processCsvFile
					// device.getrackid(): " + device.getrackid());
					if (device.getrackid().equals(rackIdList.get(headerIndex))) {
						if (rackIdPositionSelectedList.size() > 0) { // if the
																		// rack
																		// id
																		// header
																		// is
																		// not
																		// present
							if (!rackIdPositionSelectedList.get(headerIndex).equals("UNDEFINED")) {
								if (!rackIdPositionSelectedList.get(headerIndex).equals("UNDEFINED")) {
									// device.setIsSelected(rackIdPositionSelectedList.get(headerIndex));
									if (rackIdPositionSelectedList.get(headerIndex).equals("Y")) {
										device.setIsSelected(true);
									} else {
										device.setIsSelected(false);
									}
								} else {
									device.setIsSelected(false);
								}
							}
						}
						if (meterMakeList.size() > 0) {
							if (!meterMakeList.get(headerIndex).isEmpty()) {
								device.setMeterMake(meterMakeList.get(headerIndex));
							}
						}
						if (meterModelNoList.size() > 0) {
							if (!meterModelNoList.get(headerIndex).isEmpty()) {
								device.setMeterModelNo(meterModelNoList.get(headerIndex));
							}
						}
						if (dutSerialNoList.size() > 0) {
							if (!dutSerialNoList.get(headerIndex).isEmpty()) {
								device.setSerialno(dutSerialNoList.get(headerIndex));
							}
						}
						if (meterConstantList.size() > 0) {
							if (!meterConstantList.get(headerIndex).isEmpty()) {
								if (GuiUtils.isNumber(meterConstantList.get(headerIndex))) {
									device.setmeterconst(Integer.parseInt(meterConstantList.get(headerIndex)));
								}
							}
						}
						// ApplicationLauncher.logger.debug("processCsvFile:
						// headerIndex: test1: " );
						if (ctRatioList.size() > 0) {
							if (!ctRatioList.get(headerIndex).isEmpty()) {
								if (GuiUtils.isNumber(ctRatioList.get(headerIndex))) {
									device.setctrratio(Integer.parseInt(ctRatioList.get(headerIndex)));
								}
							}
						}
						// ApplicationLauncher.logger.debug("processCsvFile:
						// headerIndex: test2: " );
						if (ptRatioList.size() > 0) {
							if (!ptRatioList.get(headerIndex).isEmpty()) {
								if (GuiUtils.isNumber(ptRatioList.get(headerIndex))) {
									device.setptrratio(Integer.parseInt(ptRatioList.get(headerIndex)));
								}
							}
						}
					}

				}
			}

			devicesDataTable.refresh();

			fr.close();
		} catch (Exception e) {
			ApplicationLauncher.logger.error("processCsvFile: Exception: " + e.getMessage());
		}
	}

	class startDeployTask extends TimerTask {
		public void run() {

			ApplicationLauncher.logger.debug("startDeployTask : Entry");
			ApplicationLauncher.setCursor(Cursor.WAIT);
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
			LocalDateTime DB_StartTime = LocalDateTime.now();
			ApplicationLauncher.logger.debug("startDeployTask: Start Time: " + dtf.format(DB_StartTime));
			setDeviceSelected(false);
			setTestCaseSelected(false);
			validateAtleastOneTestPointSelected();
			validateAtleastOneDeviceSelected();
			// DeletePreviousDeployedTestPointData();
			LocalDateTime DB_EndTime = LocalDateTime.now();
			// ApplicationLauncher.logger.debug("startDeployTask: End Time0:
			// "+dtf.format(DB_EndTime));
			// ApplicationLauncher.logger.debug("startDeployTask: Difference
			// Time0: "+
			// TestReportController.DiffTime(dtf.format(DB_StartTime),dtf.format(DB_EndTime)));

			// LoadTestPointToDB();
			// DB_EndTime = LocalDateTime.now();
			// ApplicationLauncher.logger.debug("startDeployTask: End Time1:
			// "+dtf.format(DB_EndTime));
			// ApplicationLauncher.logger.debug("startDeployTask: Difference
			// Time1: "+
			// TestReportController.DiffTime(dtf.format(DB_StartTime),dtf.format(DB_EndTime)));

			// LoadDevicesToDB();

			// DB_EndTime = LocalDateTime.now();
			// ApplicationLauncher.logger.debug("startDeployTask: End Time2:
			// "+dtf.format(DB_EndTime));
			// ApplicationLauncher.logger.debug("startDeployTask: Difference
			// Time2: "+
			// TestReportController.DiffTime(dtf.format(DB_StartTime),dtf.format(DB_EndTime)));

			if (IsAtleastOneTestCaseSelected()) {
				if (IsAtleastOneDeviceSelected()) {

					if (!DisplayDataObj.isDutBlackListedOrAlreadyTested(devicesData.iterator())) {
						setDeployedFlag(true);

						Platform.runLater(() -> {
							setProjectNameAsAppTitle();
						});
						String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
								.format(Calendar.getInstance().getTime());
						;
						long starttime = 0;
						starttime = calcEpoch(timeStamp);

						// ProjectExecutionController.setProjectStartTime(starttime);

						/*
						 * String project_name =
						 * MeterParamsController.getCurrentProjectName();
						 * JSONObject modelparams =
						 * getModelParameters(project_name); String
						 * model_type=""; try { model_type =
						 * modelparams.getString("model_type"); } catch
						 * (JSONException e) { // 
						 * block e.printStackTrace();
						 * ApplicationLauncher.logger.error(
						 * "startDeploy : Exception2 pn setting model_type:"
						 * +e.getMessage()); }
						 * DisplayDataObj.setDeployedEM_ModelType(model_type);
						 * String CT_type=""; try { CT_type =
						 * modelparams.getString("ct_type"); } catch
						 * (JSONException e) { // 
						 * block e.printStackTrace();
						 * ApplicationLauncher.logger.error(
						 * "startDeploy : Exception3: setting ct_type:"
						 * +e.getMessage()); }
						 * DisplayDataObj.setDeployedEM_CT_Type(CT_type);
						 * DisplayDataObj.setDeployedDevicesJson();
						 * if(!ProcalFeatureEnable.TEST_ENABLE_FEATURE){
						 * DisplayDataObj.setEnergyFlowMode(
						 * ConstantMtePowerSource.IMPORT_MODE); }else{
						 * DisplayDataObj.setEnergyFlowMode(
						 * ConstantMtePowerSource.EXPORT_MODE); }
						 * DisplayDataObj.PowerSourceEnergyFlowModeDataInit();
						 * List<String> CurrentActiveRackList =
						 * ProjectExecutionController.getColNamesFromDB(
						 * MeterParamsController.getCurrentProjectName());
						 * DB_EndTime = LocalDateTime.now();
						 * ApplicationLauncher.logger.debug(
						 * "startDeployTask: End Time3: "
						 * +dtf.format(DB_EndTime));
						 * 
						 * ApplicationLauncher.logger.debug(
						 * "startDeployTask: Difference Time3: "+
						 * TestReportController.DiffTime(dtf.format(DB_StartTime
						 * ),dtf.format(DB_EndTime)));
						 * 
						 * LiveTableDataManager.ResetliveTableData();
						 * LiveTableDataManager.setActiveRackList(
						 * CurrentActiveRackList);
						 * LiveTableDataManager.InitLiveTableData(); DB_EndTime
						 * = LocalDateTime.now();
						 * ApplicationLauncher.logger.debug(
						 * "startDeployTask: End Time4: "
						 * +dtf.format(DB_EndTime));
						 * 
						 * ApplicationLauncher.logger.debug(
						 * "startDeployTask: Difference Time4: "+
						 * TestReportController.DiffTime(dtf.format(DB_StartTime
						 * ),dtf.format(DB_EndTime)));
						 * 
						 * LoadDeviceDataToDB_Result(); DB_EndTime =
						 * LocalDateTime.now();
						 * ApplicationLauncher.logger.debug(
						 * "startDeployTask: End Time5: "
						 * +dtf.format(DB_EndTime));
						 * 
						 * ApplicationLauncher.logger.debug(
						 * "startDeployTask: Difference Time5: "+
						 * TestReportController.DiffTime(dtf.format(DB_StartTime
						 * ),dtf.format(DB_EndTime)));
						 * 
						 * SaveProjectRunStartTime(project_name);
						 */

						setDeploymentStartTime(starttime);
						boolean status = true;// isAllSetParametersInRange();
						if (status) {
							String lastUpdatedDeploymentID = loadManageDeployDataToDB();
							DeletePreviousDeployedTestPointData(lastUpdatedDeploymentID);

							setupTargetValueForTestPoint();

							if (ProcalFeatureEnable.REPORT_GENERATION_V2_ENABLED) {
								status = LoadTestPointToDB_V2(lastUpdatedDeploymentID);
							} else {
								LoadTestPointToDB(lastUpdatedDeploymentID);
							}
							if (status) {
								LoadDevicesToDB(lastUpdatedDeploymentID);
								LoadDeviceDataToDB_Result(lastUpdatedDeploymentID);
								if (ProcalFeatureEnable.PROCON_INTERFACE_ENABLED) {
									String projectName = MeterParamsController.getCurrentProjectName();
									createProjectRun(lastUpdatedDeploymentID, projectName);
								}
								DeviceDataManagerController.setError_count(1);
								ApplicationLauncher.setCursor(Cursor.DEFAULT);
								ApplicationLauncher.logger.debug("startDeployTask: project deployment success");
								Platform.runLater(() -> {
									String message = "Project successfully deployed";
									InformUserTestCaseDeployed(message, AlertType.INFORMATION);
								});
							} else {
								DeviceDataManagerController.setError_count(1);
								ApplicationLauncher.setCursor(Cursor.DEFAULT);
								ApplicationLauncher.logger.debug("startDeployTask: project deployment failed");
								Platform.runLater(() -> {
									String message = "Project deployment failed";
									InformUserTestCaseDeployed(message, AlertType.ERROR);
								});
							}
						}
					}
				} else {
					ApplicationLauncher.InformUser("Select Device", "Select at least one device", AlertType.WARNING);
				}
			} else {
				ApplicationLauncher.InformUser("Select Test Case", "Select at least one test point", AlertType.WARNING);
			}
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			DB_EndTime = LocalDateTime.now();
			ApplicationLauncher.logger.debug("startDeployTask: End Time6: " + dtf.format(DB_EndTime));

			ApplicationLauncher.logger.debug("startDeployTask: Difference Time6: "
					+ TestReportController.DiffTime(dtf.format(DB_StartTime), dtf.format(DB_EndTime)));

			DeployTaskTimer.cancel();
		}
	}

	public void DeletePreviousDeployedTestPointData(String deploymentID) {
		String project_name = MeterParamsController.getCurrentProjectName();
		// String stdProfileName =
		// DeploymentLoaderController.getSelectedISI_StdProfileName();
		ApplicationLauncher.logger.info("DeletePreviousDeployedTestPointData: deploymentMode:" + deploymentMode);
		/*
		 * if(deploymentMode.equals(ConstantApp.DEPLOYMENT_PT_AND_CT_MODE)) {
		 * MySQL_Controller.sp_delete_deploy_test_cases(project_name,
		 * deploymentID,ConstantApp.TEST_PROFILE_CURRENT_INJECTION);
		 * MySQL_Controller.sp_delete_deploy_test_cases(project_name,
		 * deploymentID,ConstantApp.TEST_PROFILE_HIGH_VOLTAGE); }else
		 * if(deploymentMode.equals(ConstantApp.DEPLOYMENT_PT_MODE)){
		 * MySQL_Controller.sp_delete_deploy_test_cases(project_name,
		 * deploymentID,ConstantApp.TEST_PROFILE_HIGH_VOLTAGE); }else
		 * if(deploymentMode.equals(ConstantApp.DEPLOYMENT_CT_MODE)){
		 * MySQL_Controller.sp_delete_deploy_test_cases(project_name,
		 * deploymentID,ConstantApp.TEST_PROFILE_CURRENT_INJECTION); }
		 */
		MySQL_Controller.sp_delete_deploy_test_cases(project_name, deploymentID);
	}

	public String loadManageDeployDataToDB() {
		ApplicationLauncher.logger.info("loadManageDeployDataToDB: Entry");
		String project_name = MeterParamsController.getCurrentProjectName();
		ApplicationLauncher.logger.info("loadManageDeployDataToDB: project_name:" + project_name);
		devicesData.iterator();
		ApplicationLauncher.logger.info("loadManageDeployDataToDB: deploymentMode:" + deploymentMode);
		// ApplicationLauncher.logger.info("LoadDevicesToDB:
		// deploymentMode:"+deploymentMode.equals(ConstantApp.DEPLOYMENT_PT_MODE));
		// ApplicationLauncher.logger.info("LoadDevicesToDB:
		// deploymentMode:"+deploymentMode.equals(ConstantApp.DEPLOYMENT_PT_AND_CT_MODE));
		// ApplicationLauncher.logger.info("LoadDevicesToDB:
		// deploymentMode:"+(deploymentMode.equals(ConstantApp.DEPLOYMENT_PT_MODE)
		// || deploymentMode.equals(ConstantApp.DEPLOYMENT_PT_AND_CT_MODE)));

		String lastUpdatedDeploymentID = "";
		new ArrayList<Integer>();

		try {
			// String[] splitData =
			// project_name.split(ConstantApp.PROJECT_NAME_SEPERATOR);
			String customerName = "";// splitData[0];
			String equipmentSerialNo = "";// splitData[2];
			/*
			 * for(int i = 0; i< splitData.length;i++) {
			 * ApplicationLauncher.logger.info(
			 * "loadManageDeployDataToDB: splitData-i:"+splitData[i]); }
			 */
			ApplicationLauncher.logger.info("loadManageDeployDataToDB: project_name:" + project_name);
			// ApplicationLauncher.logger.info("loadManageDeployDataToDB:
			// splitData:"+splitData.toString());
			ApplicationLauncher.logger.info("loadManageDeployDataToDB: customerName:" + customerName);
			ApplicationLauncher.logger.info("loadManageDeployDataToDB: equipmentSerialNo:" + equipmentSerialNo);
			String IsMCT_Type = "Y";// "N";
			String IsNCT_Type = "Y";// "N";
			String IsMCT_TestingCompleted = "N";
			String IsNCT_TestingCompleted = "N";
			/*
			 * if(deploymentMode.equals(ConstantApp.DEPLOYMENT_PT_AND_CT_MODE))
			 * { IsPT_Type="Y"; IsCT_Type="Y"; }else
			 * if(deploymentMode.equals(ConstantApp.DEPLOYMENT_PT_MODE)){
			 * IsPT_Type="Y"; }else
			 * if(deploymentMode.equals(ConstantApp.DEPLOYMENT_CT_MODE)){
			 * IsCT_Type="Y"; }
			 */
			String customerRefNo = ref_txtBatchNumber.getText();// "";//ref_txt_CustomerRefNumber.getText();
			String ulrNumber = "";// ref_txt_UlrNumber.getText();
			String testerName = DeviceDataManagerController.getUserName();
			String energyFlowModeSelected = ConstantApp.DEPLOYMENT_IMPORT_MODE;
			if (radioBtn_Export.isSelected()) {
				energyFlowModeSelected = ConstantApp.DEPLOYMENT_EXPORT_MODE;
				ApplicationLauncher.logger
						.info("loadManageDeployDataToDB: energyFlowModeSelected:" + energyFlowModeSelected);

			}
			String autoDeployEnabled = "N";

			if (ProcalFeatureEnable.CONVEYOR_FEATURE_ENABLED) {
				if (ref_chkBxAutoDeploy.isSelected()) {
					autoDeployEnabled = "Y";
				}
			}

			long deployedTime = getDeploymentStartTime();
			long executionCompletedTime = deployedTime;
			long deployedTimeMaxSearchLimit = deployedTime - (ConstantApp.NUMBER_OF_SECONDS_IN_A_DAY
					* ConstantAppConfig.DEPLOYMENT_DB_SEARCH_MAX_TIME_LIMIT_IN_DAYS);
			String executionStatus = ConstantApp.EXECUTION_STATUS_NOT_STARTED;
			JSONObject resultjson = null;

			if (ProcalFeatureEnable.CONVEYOR_FEATURE_ENABLED) {
				resultjson = MySQL_Controller.sp_add_deploy_manage_v1_1(project_name, customerName, equipmentSerialNo,
						customerRefNo, ulrNumber, IsMCT_Type, IsNCT_Type, IsMCT_TestingCompleted,
						IsNCT_TestingCompleted, executionStatus, deployedTime, deployedTimeMaxSearchLimit,
						executionCompletedTime, testerName, energyFlowModeSelected, autoDeployEnabled);

			} else {
				resultjson = MySQL_Controller.sp_add_deploy_manage(project_name, customerName, equipmentSerialNo,
						customerRefNo, ulrNumber, IsMCT_Type, IsNCT_Type, IsMCT_TestingCompleted,
						IsNCT_TestingCompleted, executionStatus, deployedTime, deployedTimeMaxSearchLimit,
						executionCompletedTime, testerName, energyFlowModeSelected);
			}

			lastUpdatedDeploymentID = resultjson.getString("deployment_id");
			// MySQL_Controller.sp_add_deploy_devices(project_name, device,
			// rack_id,
			// ctr_ratio, ptr_ratio, meter_const,"N");
			// if((deploymentMode.equals(ConstantApp.DEPLOYMENT_IMPORT_,MODE) ||
			// deploymentMode.equals(ConstantApp.DEPLOYMENT_IMPORT_AND_EXPORT_MODE))){
			/*
			 * while (itr.hasNext()) { DeploymentDataModel dataElement =
			 * itr.next(); //ApplicationLauncher.logger.debug(
			 * "LoadDevicesToDB: selected: " + dataElement.getIsSelected());
			 * device= dataElement.getSerialno(); rack =
			 * dataElement.getrackid(); ctr_ratio = dataElement.getctrratio();
			 * ptr_ratio = dataElement.getptrratio(); meter_const =
			 * dataElement.getmeterconst(); rack_id = Integer.valueOf(rack); if
			 * (dataElement.getIsSelected()) { setDeviceSelected(true);
			 * MySQL_Controller.sp_add_deploy_devices(project_name, device,
			 * rack_id, ctr_ratio, ptr_ratio, meter_const, "Y");
			 * meter_const_arr.add(meter_const); } else{
			 * MySQL_Controller.sp_add_deploy_devices(project_name, device,
			 * rack_id, ctr_ratio, ptr_ratio, meter_const,"N"); } }
			 */
			/*
			 * } if(ConstantFeatureEnable.EXPORT_MODE_ENABLED){
			 * if(!(deploymentMode.equals(ConstantApp.DEPLOYMENT_IMPORT_MODE))){
			 * itr = devicesData.iterator(); ApplicationLauncher.logger.info(
			 * "LoadDevicesToDB: Export Mode");
			 * 
			 * while (itr.hasNext()) { DeploymentDataModel dataElement =
			 * itr.next(); //ApplicationLauncher.logger.info(
			 * "LoadDevicesToDB: selected: " + dataElement.getIsSelected());
			 * device= ConstantApp.EXPORT_MODE_ALIAS_NAME+
			 * dataElement.getSerialno(); ApplicationLauncher.logger.info(
			 * "LoadDevicesToDB: device:"+device); rack =
			 * dataElement.getrackid(); ctr_ratio = dataElement.getctrratio();
			 * ptr_ratio = dataElement.getptrratio(); meter_const =
			 * dataElement.getmeterconst(); rack_id =
			 * Integer.valueOf(rack)+ConstantApp.
			 * EXPORT_MODE_DEVICE_ID_THRESHOLD; if (dataElement.getIsSelected())
			 * { setDeviceSelected(true);
			 * MySQL_Controller.sp_add_deploy_devices(project_name, device,
			 * rack_id, ctr_ratio, ptr_ratio, meter_const, "Y");
			 * meter_const_arr.add(meter_const); } else{
			 * MySQL_Controller.sp_add_deploy_devices(project_name, device,
			 * rack_id, ctr_ratio, ptr_ratio, meter_const,"N"); } } } }
			 */
			// CheckAllMeterConstSame(meter_const_arr);
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("loadManageDeployDataToDB: Exception: " + e.getMessage());
		}

		return lastUpdatedDeploymentID;
	}

	private static void applyUacSettings() {
		
		ApplicationLauncher.logger.info("DeploymentManagerController : applyUacSettings :  Entry");
		ArrayList<UacDataModel> uacSelectProfileScreenList = DeviceDataManagerController
				.getUacSelectProfileScreenList();
		String screenName = "";
		for (int i = 0; i < uacSelectProfileScreenList.size(); i++) {

			screenName = uacSelectProfileScreenList.get(i).getScreenName();
			switch (screenName) {
			case ConstantApp.UAC_DEPLOY_SCREEN:

				if (!uacSelectProfileScreenList.get(i).getExecutePossible()) {
					ref_btn_deploy.setDisable(true);

				}

				if (!uacSelectProfileScreenList.get(i).getAddPossible()) {
					// ref_btn_Create.setDisable(true);

				}

				if (!uacSelectProfileScreenList.get(i).getUpdatePossible()) {
					// ref_vbox_testscript.setDisable(true);sdvsc
					// setChildPropertySaveEnabled(false);
					// ref_btn_Save.setDisable(true);

				}

				if (!uacSelectProfileScreenList.get(i).getDeletePossible()) {
					// ref_btn_Delete.setDisable(true);

				}
				break;

			default:
				break;
			}

		}
	}

	public void createProjectRun(String deploymentId, String projectName) {
		

		ProjectRun projectRun = new ProjectRun();
		projectRun.setDeploymentId(deploymentId);

		long epochStartTime = Instant.now().toEpochMilli() / 1000;

		projectRun.setEpochStartTime(epochStartTime);

		Date date = new Date(epochStartTime * 1000);

		// format of the date

		SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String startTime = jdf.format(date);
		projectRun.setStartTime(startTime);
		projectRun.setExecutionStatus(ConstantApp.EXECUTION_STATUS_NOT_STARTED);
		projectRun.setProjectName(projectName);
		DisplayDataObj.getProjectRunService().saveToDb(projectRun);
	}

}
