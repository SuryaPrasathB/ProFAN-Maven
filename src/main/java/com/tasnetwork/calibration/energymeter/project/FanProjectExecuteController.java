package com.tasnetwork.calibration.energymeter.project;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.json.JSONException;
import org.json.JSONObject;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantArduinoCommands;
import com.tasnetwork.calibration.energymeter.constant.ConstantCSS;
import com.tasnetwork.calibration.energymeter.constant.ConstantStatus;
import com.tasnetwork.calibration.energymeter.constant.LogLevel;
import com.tasnetwork.calibration.energymeter.database.MySQL_Controller;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.device.DutSerialDataManager;
import com.tasnetwork.calibration.energymeter.util.EditCell;
import com.tasnetwork.calibration.energymeter.util.MyIntegerStringConverter;
import com.tasnetwork.calibration.energymeter.util.ManualReadingsInputPopup;
import com.tasnetwork.spring.orm.model.DutCommand;
import com.tasnetwork.spring.orm.model.DutMasterData;
import com.tasnetwork.spring.orm.model.FanTestSetup;
import com.tasnetwork.spring.orm.model.ProjectRun;
import com.tasnetwork.spring.orm.model.Result;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TableCell;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode; // Import KeyCode
import javafx.scene.input.KeyEvent; // Import KeyEvent
import javafx.scene.control.CheckBox; // Import CheckBox
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane; // Import GridPane
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;


/**
 * Controller for the Fan Project Execution UI, managing fan testing operations,
 * including voltage control, phase measurements, and test point execution.
 */
public class FanProjectExecuteController implements Initializable {

	private ProjectRun currentProjectRun;

	// It MUST be set to 'true' at the beginning of a new overall test run (e.g., when the user clicks 'Start Test').
	private boolean isFirstTestPointInSequence = true; // Initialize to true for the very first test point in a new sequence

	// Flag for simulation mode
	private boolean SIMULATION_MODE = true; // Set to true to bypass hardware calls

	// Flag to enable/disable the secret shortcut for simulation mode configuration
	private boolean isSimulationShortcutEnabled = true;

	// FXML Buttons ==============================================================================================================================
	@FXML private Button btnStart;
	static private Button ref_btnStart;

	@FXML private Button btnStop;
	static private Button ref_btnStop;

	// Dimmer Controls
	@FXML private Button btnDimmerIsMin;
	static private Button ref_btnDimmerIsMin;

	@FXML private Button btnDimmerSetMin;
	@FXML private Button btnDimmerForward;
	static private Button ref_btnDimmerForward;

	@FXML private Button btnDimmerReverse;
	static private Button ref_btnDimmerReverse;

	@FXML private Button btnDimmerIsMinExecute;
	static private Button ref_btnDimmerIsMinExecute;

	@FXML private Button btnDimmerSetMinExecute;

	@FXML private Button btnDimmerForwardExecute;
	static private Button ref_btnDimmerForwardExecute;

	@FXML private Button btnDimmerReverseExecute;
	static private Button ref_btnDimmerReverseExecute;

	// 3-Phase Measurements
	@FXML private Button btn3PhaseCurrentAvg;
	static private Button ref_btn3PhaseCurrentAvg;

	@FXML private Button btn3PhasePFAvg;
	static private Button ref_btn3PhasePFAvg;

	@FXML private Button btn3PhaseVATotal;
	static private Button ref_btn3PhaseVATotal;

	@FXML private Button btn3PhaseVBR;
	static private Button ref_btn3PhaseVBR;

	@FXML private Button btn3PhaseVLL;
	static private Button ref_btn3PhaseVLL;

	@FXML private Button btn3PhaseVLN;
	static private Button ref_btn3PhaseVLN;

	@FXML private Button btn3PhaseVRY;
	static private Button ref_btn3PhaseVRY;

	@FXML private Button btn3PhaseVYB;
	static private Button ref_btn3PhaseVYB;

	@FXML private Button btn3PhaseWattsTotal;
	static private Button ref_btn3PhaseWattsTotal;

	// B Phase
	@FXML private Button btnBPhaseCurrent;
	static private Button ref_btnBPhaseCurrent;

	@FXML private Button btnBPhasePF;
	static private Button ref_btnBPhasePF;

	@FXML private Button btnBPhaseVA;
	static private Button ref_btnBPhaseVA;

	@FXML private Button btnBPhaseVoltage;
	static private Button ref_btnBPhaseVoltage;

	@FXML private Button btnBPhaseWatts;
	static private Button ref_btnBPhaseWatts;

	// Fan Controls
	@FXML private Button btnFanRpm;
	static private Button ref_btnFanRpm;

	@FXML private Button btnFanWindspeed;
	static private Button ref_btnFanWindspeed;

	@FXML private Button btnFanRpmExecute;
	static private Button ref_btnFanRpmExecute;

	@FXML private Button btnFanWindspeedExecute;
	static private Button ref_btnFanWindspeedExecute;

	// Mains Control
	@FXML private Button btnVoltageMainOn;
	static private Button ref_btnVoltageMainOn;

	@FXML private Button btnVoltageMainOff;
	static private Button ref_btnVoltageMainOff;

	// R Phase
	@FXML private Button btnRPhaseCurrent;
	static private Button ref_btnRPhaseCurrent;

	@FXML private Button btnRPhasePF;
	static private Button ref_btnRPhasePF;

	@FXML private Button btnRPhaseVA;
	static private Button ref_btnRPhaseVA;

	@FXML private Button btnRPhaseVoltage;
	static private Button ref_btnRPhaseVoltage;

	@FXML private Button btnRPhaseWatts;
	static private Button ref_btnRPhaseWatts;

	// Voltage Controls
	@FXML private Button btnSetVoltage;
	static private Button ref_btnSetVoltage;

	@FXML private Button btnTestVoltage;
	static private Button ref_btnTestVoltage;

	@FXML private Button btnMaintainVoltage;
	@FXML private Button btnSetVoltageExecute;
	static private Button ref_btnSetVoltageExecute;

	@FXML private Button btnTestVoltageExecute;
	static private Button ref_btnTestVoltageExecute;

	@FXML private Button btnMaintainVoltageExecute;
	// Y Phase
	@FXML private Button btnYPhaseCurrent;
	static private Button ref_btnYPhaseCurrent;

	@FXML private Button btnYPhasePF;
	static private Button ref_btnYPhasePF;

	@FXML private Button btnYPhaseVA;
	static private Button ref_btnYPhaseVA;

	@FXML private Button btnYPhaseVoltage;
	static private Button ref_btnYPhaseVoltage;

	@FXML private Button btnYPhaseWatts;
	static private Button ref_btnYPhaseWatts;

	// Auto Toggle Buttons and Progress Indicators
	@FXML private ToggleButton toggle_Auto3Phase;
	@FXML private ProgressIndicator pi_Auto3Phase;

	@FXML private ToggleButton toggle_AutoBPhase;
	@FXML private ProgressIndicator pi_AutoBPhase;

	@FXML private ToggleButton toggle_AutoFan;
	@FXML private ProgressIndicator pi_AutoFan;

	@FXML private ToggleButton toggle_AutoRPhase;
	@FXML private ProgressIndicator pi_AutoRPhase;

	@FXML private ToggleButton toggle_AutoYPhase;
	@FXML private ProgressIndicator pi_AutoYPhase;

	private Button clickedButtonRef; // track which button was clicked

	// ────────────────────────────────────────────────────────────────────────
	// Executors
	// ────────────────────────────────────────────────────────────────────────

	private ScheduledExecutorService autoRPhaseExecutor;
	private ScheduledExecutorService autoYPhaseExecutor;
	private ScheduledExecutorService autoBPhaseExecutor;
	private ScheduledExecutorService auto3PhaseExecutor;
	private ScheduledExecutorService autoFanExecutor;

	// FXML TextFields ===============================================================================================================================
	// Forward/Reverse Timing
	@FXML private TextField txtForwardInMsec;
	static private TextField ref_txtForwardInMsec;

	@FXML private TextField txtReverseInMsec;
	static private TextField ref_txtReverseInMsec;

	@FXML private TextField txtForwardInMsecExecute;
	static private TextField ref_txtForwardInMsecExecute;

	@FXML private TextField txtReverseInMsecExecute;
	static private TextField ref_txtReverseInMsecExecute;

	// 3-Phase Readings
	@FXML private TextField txt3PhaseCurrentAvg;
	static private TextField ref_txt3PhaseCurrentAvg;

	@FXML private TextField txt3PhasePFAvg;
	static private TextField ref_txt3PhasePFAvg;

	@FXML private TextField txt3PhaseVATotal;
	static private TextField ref_txt3PhaseVATotal;

	@FXML private TextField txt3PhaseVBR;
	static private TextField ref_txt3PhaseVBR;

	@FXML private TextField txt3PhaseVLL;
	static private TextField ref_txt3PhaseVLL;

	@FXML private TextField txt3PhaseVLN;
	static private TextField ref_txt3PhaseVLN;

	@FXML private TextField txt3PhaseVRY;
	static private TextField ref_txt3PhaseVRY;

	@FXML private TextField txt3PhaseVYB;
	static private TextField ref_txt3PhaseVYB;

	@FXML private TextField txt3PhaseWattsTotal;
	static private TextField ref_txt3PhaseWattsTotal;

	// B Phase
	@FXML private TextField txtBPhaseCurrent;
	static private TextField ref_txtBPhaseCurrent;

	@FXML private TextField txtBPhasePF;
	static private TextField ref_txtBPhasePF;

	@FXML private TextField txtBPhaseVA;
	static private TextField ref_txtBPhaseVA;

	@FXML private TextField txtBPhaseVoltage;
	static private TextField ref_txtBPhaseVoltage;

	@FXML private TextField txtBPhaseWatts;
	static private TextField ref_txtBPhaseWatts;

	// R Phase
	@FXML private TextField txtRPhaseCurrent;
	static private TextField ref_txtRPhaseCurrent;

	@FXML private TextField txtRPhasePF;
	static private TextField ref_txtRPhasePF;

	@FXML private TextField txtRPhaseVA;
	static private TextField ref_txtRPhaseVA;

	@FXML private TextField txtRPhaseVoltage;
	static private TextField ref_txtRPhaseVoltage;

	@FXML private TextField txtRPhaseWatts;
	static private TextField ref_txtRPhaseWatts;

	// Y Phase
	@FXML private TextField txtYPhaseCurrent;
	static private TextField ref_txtYPhaseCurrent;

	@FXML private TextField txtYPhasePF;
	static private TextField ref_txtYPhasePF;

	@FXML private TextField txtYPhaseVA;
	static private TextField ref_txtYPhaseVA;

	@FXML private TextField txtYPhaseVoltage;
	static private TextField ref_txtYPhaseVoltage;

	@FXML private TextField txtYPhaseWatts;
	static private TextField ref_txtYPhaseWatts;

	// Voltage Controls
	@FXML private TextField txtSetVoltage;
	static private TextField ref_txtSetVoltage;

	@FXML private TextField txtTestVoltage;
	static private TextField ref_txtTestVoltage;

	@FXML private TextField txtSetVoltageExecute;
	static private TextField ref_txtSetVoltageExecute;

	@FXML private TextField txtTestVoltageExecute;
	static private TextField ref_txtTestVoltageExecute;

	// Fan
	@FXML private TextField txtFanRpm;
	static private TextField ref_txtFanRpm;

	@FXML private TextField txtFanWindSpeed;
	static private TextField ref_txtFanWindSpeed;

	@FXML private TextField txtFanRpmExecute;
	static private TextField ref_txtFanRpmExecute;

	@FXML private TextField txtFanWindSpeedExecute;
	static private TextField ref_txtFanWindSpeedExecute;
	// TIMERS =====================================================================================================================================
	private Timer startTimer;
	private Timer stopTimer;

	// =========================== Dimmer Timers ===================================
	private Timer dimmerForwardTimer;
	private Timer dimmerReverseTimer;
	private Timer dimmerIsMinTimer;
	private Timer dimmerSetMinTimer;

	// =========================== 3-Phase Timers ==================================
	private Timer phase3CurrentAvgTimer;
	private Timer phase3PFAvgTimer;
	private Timer phase3VATotalTimer;
	private Timer phase3VBRTimer;
	private Timer phase3VLLTimer;
	private Timer phase3VLNTimer;
	private Timer phase3VRYTimer;
	private Timer phase3VYBTimer;
	private Timer phase3WattsTotalTimer;

	// =========================== B Phase Timers ==================================
	private Timer bPhaseCurrentTimer;
	private Timer bPhasePFTimer;
	private Timer bPhaseVATimer;
	private Timer bPhaseVoltageTimer;
	private Timer bPhaseWattsTimer;

	// =========================== R Phase Timers ==================================
	private Timer rPhaseCurrentTimer;
	private Timer rPhasePFTimer;
	private Timer rPhaseVATimer;
	private Timer rPhaseVoltageTimer;
	private Timer rPhaseWattsTimer;

	// =========================== Y Phase Timers ==================================
	private Timer yPhaseCurrentTimer;
	private Timer yPhasePFTimer;
	private Timer yPhaseVATimer;
	private Timer yPhaseVoltageTimer;
	private Timer yPhaseWattsTimer;

	// =========================== Fan Timers ======================================
	private Timer fanRpmTimer;
	private Timer fanWindspeedTimer;

	// =========================== Mains Timers ======================================
	private Timer mainsOnTimer;
	private Timer mainsOffTimer;

	// =========================== Voltage Control Timers ===========================
	private Timer setVoltageTimer;
	private Timer testVoltageTimer;
	private Timer maintainVoltageTimer;

	// =========================== Utility Timers ==================================
	private Timer saveOnClickTimer;

	// ===============================================================================================================


	DeviceDataManagerController displayDataObj = new DeviceDataManagerController();

	private static AtomicInteger serialNoAtomic = new AtomicInteger(1);

	@FXML
	private Button addTestPointButton;

	@FXML
	private Button btnSettings;

	@FXML
	private ComboBox<String> cmbBxModelName;
	static private ComboBox<String> ref_cmbBxModelName;

	@FXML
	private TableColumn<FanTestSetup, String> colTestSetupStatus;

	@FXML
	private TableColumn<FanTestSetup, Double> colTestSetupProgress;

	@FXML
	private TableColumn<FanTestSetup, String> colTestSetupTestPointName;

	@FXML
	private TableColumn<FanTestSetup, Integer> colTestSetupSerialNo;

	@FXML private TableColumn<FanTestSetup, String> colTestSetupTargetVoltage;

	@FXML private TableColumn<FanTestSetup, Integer> colTestSetupTimeInSec;

	@FXML private TableColumn<FanTestSetup, String> colTestSetupRpmActual;

	@FXML private TableColumn<FanTestSetup, String> colTestSetupWindSpeedActual;

	@FXML private TableColumn<FanTestSetup, String> colTestSetupVibActual;

	@FXML private TableColumn<FanTestSetup, String> colTestSetupCurrentActual;

	@FXML private TableColumn<FanTestSetup, String> colTestSetupWattsActual;

	@FXML private TableColumn<FanTestSetup, String> colTestSetupActivePowerActual;

	@FXML private TableColumn<FanTestSetup, String> colTestSetupPfActual;


	// TEST EXECUTION ===================================================================================
	@FXML private Button btnTestPointCloseProject;
	@FXML private Button btnTestPointResume;
	@FXML private Button btnTestPointStart;
	@FXML private Button btnTestPointStepRun;
	@FXML private Button btnTestPointStop;

	/*@FXML
    private ProgressBar progressBarTestPoint;*/	

	@FXML
	private TableView<FanTestSetup> tvTestSetup;
	static private TableView<FanTestSetup> ref_tvTestSetup;

	private ObservableList<FanTestSetup> testPoints;

	private int currentIndex = 0;
	private boolean isRunning = false;
	private boolean isStopped = false;
	private boolean isStepRun = false;

	private String fanSerialNumber = "";
	private String modelPhase = "";
	private volatile Integer requestedResumeIndex = null;

	@FXML
	private TextField txtNewFanSerialNo;
	static private TextField ref_txtNewFanSerialNo;

	@FXML
	private TextArea textAreaLogs;

	@FXML
	private ListView<LogEntry> listViewLogs;

	private final ObservableList<LogEntry> logItems = FXCollections.observableArrayList();


	// RESULTS
	private Result currentResult;
	private boolean allValid ;

	// DYNAMIC CONTROLS PANEL
	@FXML
	private AnchorPane dynamicContentPane;

	// ============================================================================================================

	/**
	 * Initializes the controller and sets up the GUI components.
	 * - Sets up table columns and their properties
	 * - Initializes UI controls and their event handlers
	 * - Loads initial data from database
	 * - Sets up validation rules for input fields
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		ApplicationLauncher.logger.debug("FanProjectExecuteController: initialize: " );

		refAssignment();
		listenersAssignment();
		guiInit();
		loadDataFromDb();
	}

	/**
	 * Initializes the GUI components and their properties.
	 * - Sets up table columns with custom cell factories
	 * - Configures progress indicators and status displays
	 * - Sets up editable columns and their validation
	 * - Initializes button states and event handlers
	 */
	private void guiInit() {

		listViewLogs.setItems(logItems);
		listViewLogs.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		listViewLogs.setCellFactory(lv -> new ListCell<LogEntry>() {
			@Override
			protected void updateItem(LogEntry item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setStyle("");
				} else {
					setText(item.getMessage());
					switch (item.getLevel()) {
					case ERROR:
						setTextFill(Color.RED);
						break;
					case DEBUG:
						setTextFill(Color.ORANGE);
						break;
					default:
						setTextFill(Color.BLACK);
						break;
					}
				}
			}
		});

		// Add global key event filter to the scene for the simulation mode shortcut
		Platform.runLater(() -> {
			if (tvTestSetup.getScene() != null) {
				tvTestSetup.getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
					// Detect CTRL + SHIFT + ` (back_quote)
					if (isSimulationShortcutEnabled && event.isAltDown() && event.isShiftDown() && event.getCode() == KeyCode.BACK_QUOTE) {
						openSimulationConfigurationDialog();
						event.consume(); // Consume the event so it doesn't propagate
					}
					/*
					 * if (isSimulationShortcutEnabled && event.isControlDown() &&
					 * event.isShiftDown() && event.getCode() == KeyCode.BACK_QUOTE) {
					 * ReportGeneratorController reportGeneratorController = new
					 * ReportGeneratorController();
					 * reportGeneratorController.openPathConfigurationDialog(); event.consume(); //
					 * Consume the event so it doesn't propagate }
					 */
				});
			} else {
				System.err.println("Warning: Scene not available during initialize, cannot set global key listener for simulation config.");
			}
		});


		// TEXT BOX VALIDATION ===============================================================================
		addNumericRangeValidation(txtForwardInMsec, "Forward time (ms)");
		addNumericRangeValidation(txtReverseInMsec, "Reverse time (ms)");
		addNumericRangeValidationAllowZero(txtSetVoltage, "Set Voltage (V)");
		addNumericRangeValidationAllowZero(txtTestVoltage, "Test Voltage (V)");

		addTextValidation(txtNewFanSerialNo, "Fan Serial No");


		// Table row factory for highlighting running test points
		tvTestSetup.setRowFactory(tv -> {
			TableRow<FanTestSetup> row = new TableRow<FanTestSetup>() { // Specify the type explicitly
				@Override
				protected void updateItem(FanTestSetup item, boolean empty) {
					super.updateItem(item, empty);
					if (item == null || empty) {
						setStyle("");
					} else {
						if (item.isRunning()) {
							setStyle(ConstantCSS.FX_BACKGROUND_COLOR + ConstantCSS.COLOR_LIGHT_GREEN_SEMI_COLON);
						} else {
							setStyle("");
						}
					}
				}
			};
			// Double-click to jump to test point
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && !row.isEmpty()) {
					FanTestSetup selected = row.getItem();
					int selectedIndex = tvTestSetup.getItems().indexOf(selected);
					ApplicationLauncher.logger.debug("User requested to jump to Test Point Index: " + selectedIndex);

					if (isRunning && selectedIndex != currentIndex) {
						ApplicationLauncher.logger.info("Resume requested to Test Point Index: " + selectedIndex);
						requestedResumeIndex = selectedIndex;
					}
				}
			});

			return row;
		});

		/*// RIGHT CLICK MENU FOR LOG AREA ======================================================================

		ContextMenu contextMenu = new ContextMenu();

	    MenuItem copyItem = new MenuItem("Copy");
	    copyItem.setOnAction(e -> textAreaLogs.copy());

	    MenuItem selectAllItem = new MenuItem("Select All");
	    selectAllItem.setOnAction(e -> textAreaLogs.selectAll());

	    SeparatorMenuItem separator = new SeparatorMenuItem();

	    MenuItem clearItem = new MenuItem("Clear");
	    clearItem.setOnAction(e -> textAreaLogs.clear());

	    contextMenu.getItems().addAll(copyItem, selectAllItem, separator, clearItem);

	    // Enable/Disable Copy based on selection
	    textAreaLogs.setContextMenu(contextMenu);
	    textAreaLogs.selectionProperty().addListener((obs, oldSel, newSel) -> {
	        copyItem.setDisable(textAreaLogs.getSelectedText().isEmpty());
	    });

	    // Also initialize the correct state on startup
	    copyItem.setDisable(true);*/

		// RIGHT CLICK MENU FOR LIST LOG AREA ======================================================================
		ContextMenu contextMenuList = new ContextMenu();

		MenuItem copyItemList = new MenuItem("Copy");
		copyItemList.setOnAction(e -> {
			LogEntry selected = listViewLogs.getSelectionModel().getSelectedItem();
			if (selected != null) {
				Clipboard clipboard = Clipboard.getSystemClipboard();
				ClipboardContent content = new ClipboardContent();
				content.putString(selected.getMessage());  // ✅ Fixed
				clipboard.setContent(content);
			}
		});

		MenuItem previewAndCopyItem = new MenuItem("Preview & Copy");
		previewAndCopyItem.setOnAction(e -> {
			ObservableList<LogEntry> selectedItems = listViewLogs.getSelectionModel().getSelectedItems();
			if (!selectedItems.isEmpty()) {
				String preview = selectedItems.stream()
						.map(LogEntry::getMessage)
						.collect(Collectors.joining("\n"));

				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setTitle("Copy Preview");
				alert.setHeaderText("Do you want to copy the selected log(s)?");
				TextArea textArea = new TextArea(preview);
				textArea.setEditable(false);
				textArea.setWrapText(true);
				textArea.setMaxWidth(Double.MAX_VALUE);
				textArea.setMaxHeight(Double.MAX_VALUE);
				alert.getDialogPane().setContent(textArea);

				Optional<ButtonType> result = alert.showAndWait();
				if (result.isPresent() && result.get() == ButtonType.OK) {
					ClipboardContent content = new ClipboardContent();
					content.putString(preview);
					Clipboard.getSystemClipboard().setContent(content);
				}
			}
		});

		MenuItem selectAllItemList = new MenuItem("Select All");
		selectAllItemList.setOnAction(e -> listViewLogs.getSelectionModel().selectAll());

		SeparatorMenuItem separatorList = new SeparatorMenuItem();

		MenuItem clearItemList = new MenuItem("Clear");
		clearItemList.setOnAction(e -> logItems.clear());

		contextMenuList.getItems().addAll(
				previewAndCopyItem,  // New
				selectAllItemList,
				separatorList,
				clearItemList
				);

		// Attach context menu
		listViewLogs.setContextMenu(contextMenuList);

		// Enable/Disable Copy based on selection
		listViewLogs.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			copyItemList.setDisable(newVal == null);
		});

		copyItemList.setDisable(true);  // Initially disabled



		// ====================================================================================================

		// Model selection listener		
		ref_cmbBxModelName.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal != null) {
				updateTestSetupTableForModel(newVal);
				testPoints = ref_tvTestSetup.getItems();
				modelPhase = DeviceDataManagerController.getDutMasterDataService().findByModelName(newVal).getPhase();
				//appendLog("Model Phase = " + modelPhase);

				loadPhaseContent(modelPhase);
			}
		});

		ref_tvTestSetup.setEditable(false);

		colTestSetupRpmActual.setCellValueFactory(cellData -> cellData.getValue().rpmActualProperty());
		colTestSetupRpmActual.setCellFactory(col -> new TableCell<FanTestSetup, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				setText(null);
				setStyle("");
				getStyleClass().remove("blinking-cell");

				if (!empty && item != null) {
					setText(item);
					FanTestSetup tp = getTableView().getItems().get(getIndex());
					setStyle(ConstantCSS.FX_TEST_FILL + (tp.rpmValidProperty().get() ? ConstantCSS.COLOR_GREEN_SEMI_COLON : ConstantCSS.COLOR_RED_SEMI_COLON));
					if (tp.rpmBlinkProperty().get() && !getStyleClass().contains("blinking-cell")) {
						getStyleClass().add("blinking-cell");
					}
				}
			}
		});

		colTestSetupWindSpeedActual.setCellValueFactory(cellData -> cellData.getValue().windSpeedActualProperty());
		colTestSetupWindSpeedActual.setCellFactory(col -> new TableCell<FanTestSetup, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				setText(null);
				setStyle("");
				getStyleClass().remove("blinking-cell");

				if (!empty && item != null) {
					setText(item);
					FanTestSetup tp = getTableView().getItems().get(getIndex());
					setStyle(ConstantCSS.FX_TEST_FILL + (tp.windSpeedValidProperty().get() ? ConstantCSS.COLOR_GREEN_SEMI_COLON : ConstantCSS.COLOR_RED_SEMI_COLON));
					if (tp.windSpeedBlinkProperty().get() && !getStyleClass().contains("blinking-cell")) {
						getStyleClass().add("blinking-cell");
					}
				}
			}
		});

		colTestSetupVibActual.setCellValueFactory(cellData -> cellData.getValue().vibrationActualProperty());
		colTestSetupVibActual.setCellFactory(col -> new TableCell<FanTestSetup, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				setText(null);
				setStyle("");
				getStyleClass().remove("blinking-cell");

				if (!empty && item != null) {
					setText(item);
					FanTestSetup tp = getTableView().getItems().get(getIndex());
					setStyle(ConstantCSS.FX_TEST_FILL + (tp.vibValidProperty().get() ? ConstantCSS.COLOR_GREEN_SEMI_COLON : ConstantCSS.COLOR_RED_SEMI_COLON));
					if (tp.windSpeedBlinkProperty().get() && !getStyleClass().contains("blinking-cell")) {
						getStyleClass().add("blinking-cell");
					}
				}
			}
		});

		colTestSetupCurrentActual.setCellValueFactory(cellData -> cellData.getValue().currentActualProperty());
		colTestSetupCurrentActual.setCellFactory(col -> new TableCell<FanTestSetup, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				setText(null);
				setStyle("");
				getStyleClass().remove("blinking-cell");

				if (!empty && item != null) {
					setText(item);
					FanTestSetup tp = getTableView().getItems().get(getIndex());
					setStyle(ConstantCSS.FX_TEST_FILL + (tp.currentValidProperty().get() ? ConstantCSS.COLOR_GREEN_SEMI_COLON : ConstantCSS.COLOR_RED_SEMI_COLON));
					if (tp.currentBlinkProperty().get() && !getStyleClass().contains("blinking-cell")) {
						getStyleClass().add("blinking-cell");
					}
				}
			}
		});

		colTestSetupWattsActual.setCellValueFactory(cellData -> cellData.getValue().wattsActualProperty());
		colTestSetupWattsActual.setCellFactory(col -> new TableCell<FanTestSetup, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				setText(null);
				setStyle("");
				getStyleClass().remove("blinking-cell");

				if (!empty && item != null) {
					setText(item);
					FanTestSetup tp = getTableView().getItems().get(getIndex());
					setStyle(ConstantCSS.FX_TEST_FILL + (tp.wattsValidProperty().get() ? ConstantCSS.COLOR_GREEN_SEMI_COLON : ConstantCSS.COLOR_RED_SEMI_COLON));
					if (tp.wattsBlinkProperty().get() && !getStyleClass().contains("blinking-cell")) {
						getStyleClass().add("blinking-cell");
					}
				}
			}
		});

		colTestSetupActivePowerActual.setCellValueFactory(cellData -> cellData.getValue().activePowerActualProperty());
		colTestSetupActivePowerActual.setCellFactory(col -> new TableCell<FanTestSetup, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				setText(null);
				setStyle("");
				getStyleClass().remove("blinking-cell");

				if (!empty && item != null) {
					setText(item);
					FanTestSetup tp = getTableView().getItems().get(getIndex());
					setStyle(ConstantCSS.FX_TEST_FILL + (tp.vaValidProperty().get() ? ConstantCSS.COLOR_GREEN_SEMI_COLON : ConstantCSS.COLOR_RED_SEMI_COLON));
					if (tp.vaBlinkProperty().get() && !getStyleClass().contains("blinking-cell")) {
						getStyleClass().add("blinking-cell");
					}
				}
			}
		});

		colTestSetupPfActual.setCellValueFactory(cellData -> cellData.getValue().powerFactorActualProperty());
		colTestSetupPfActual.setCellFactory(col -> new TableCell<FanTestSetup, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				setText(null);
				setStyle("");
				getStyleClass().remove("blinking-cell");

				if (!empty && item != null) {
					setText(item);
					FanTestSetup tp = getTableView().getItems().get(getIndex());
					setStyle(ConstantCSS.FX_TEST_FILL + (tp.pfValidProperty().get() ? ConstantCSS.COLOR_GREEN_SEMI_COLON : ConstantCSS.COLOR_RED_SEMI_COLON));
					if (tp.pfBlinkProperty().get() && !getStyleClass().contains("blinking-cell")) {
						getStyleClass().add("blinking-cell");
					}
				}
			}
		});


		ref_cmbBxModelName.setPromptText("Select Model");


		/*ObservableList<FanTestSetup> dummyData = FXCollections.observableArrayList();

		for (int i = 1; i <= 3; i++) {
		    FanTestSetup point = new FanTestSetup();
		    point.setStatus("In Progress");
		    point.setTestPointName("Test Point " + i);
		    point.setTargetVoltage(String.valueOf(10*i + 200));
		    point.setSetupTimeInSec(60);

		    point.setRpmActual(generateRandomRpm());
	        point.setWindSpeedActual(generateRandomWindSpeed());
	        point.setCurrentActual(generateRandomCurrent());
	        point.setWattsActual(generateRandomWatts());
	        point.setActivePowerActual(generateRandomActivePower());	
	        point.setPowerFactorActual(generateRandomPowerFactor());



		    point.setIsRunning(false);
		    dummyData.add(point);
		}

		tvTestSetup.setItems(dummyData);*/

		/* colPmPalletBatchNo.setCellValueFactory(data -> data.getValue().getPalletBatchNoProperty().asObject());
		colPmPalletActive.setStyle( "-fx-alignment: CENTER;");
		colPmPalletActive.setCellValueFactory(new PalletBayTestPalletActive_CheckBoxValueFactory());
		colPmPalletQrId.setCellValueFactory(data -> data.getValue().getPalletQrIdProperty());*/


		//colTestExecuteRpmLowerLimit.setCellValueFactory(new PropertyValueFactory<ExcelCellValueModel, String>("cell_value"));FanTestSetup

		colTestSetupSerialNo.setCellValueFactory(data -> data.getValue().getSerialNoProperty().asObject());
		colTestSetupStatus.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

		colTestSetupStatus.setCellFactory(column -> new TableCell<FanTestSetup, String>() {
			@Override
			protected void updateItem(String status, boolean empty) {
				super.updateItem(status, empty);

				if (empty || status == null) {
					setText(null);
					setStyle("");
				} else {
					setText(status);
					if (ConstantStatus.COMPLETED.equalsIgnoreCase(status)) {
						setTextFill(Color.GREEN);
					} else if (ConstantStatus.IN_PROG.equalsIgnoreCase(status)) {
						setTextFill(Color.ORANGE);
					} else if (ConstantStatus.FAILED.equalsIgnoreCase(status)) {
						setTextFill(Color.RED);
					}else {
						setTextFill(Color.BLACK);
					}
				}
			}
		});

		colTestSetupProgress.setCellValueFactory(cellData -> cellData.getValue().progressProperty().asObject());

		colTestSetupProgress.setCellFactory(column -> new TableCell<FanTestSetup, Double>() {
			private final ProgressBar progressBar = new ProgressBar();

			@Override
			protected void updateItem(Double progress, boolean empty) {
				super.updateItem(progress, empty);

				if (empty || progress == null) {
					setGraphic(null);
				} else {
					progressBar.setProgress(progress);

					FanTestSetup testPoint = getTableView().getItems().get(getIndex());
					if (ConstantStatus.COMPLETED.equalsIgnoreCase(testPoint.getStatus())) {
						progressBar.setStyle(ConstantCSS.FX_ACCENT + ConstantCSS.COLOR_GREEN_SEMI_COLON);
					} else if ("Failed".equalsIgnoreCase(testPoint.getStatus())) {
						progressBar.setStyle(ConstantCSS.FX_ACCENT + ConstantCSS.COLOR_RED_SEMI_COLON);
					} else {
						progressBar.setStyle(""); // Default style
					}

					setGraphic(progressBar);
				}
			}
		});




		/*colTestSetupActive.setStyle( "-fx-alignment: CENTER;");
		colTestSetupActive.setCellValueFactory(new FanTestSetupActive_CheckBoxValueFactory());*/


		colTestSetupTimeInSec.setCellValueFactory(data -> data.getValue().getSetupTimeInSecProperty().asObject());    	
		colTestSetupTimeInSec.setCellFactory(EditCell.<FanTestSetup, Integer>forTableColumn(new MyIntegerStringConverter()));
		colTestSetupTimeInSec.setOnEditCommit(new EventHandler<CellEditEvent<FanTestSetup, Integer>>() {
			public void handle(CellEditEvent<FanTestSetup, Integer> t) {
				FanTestSetup rowData = ((FanTestSetup) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				try{
					rowData.setSetupTimeInSec(t.getNewValue());
				} catch (Exception e) {
					e.printStackTrace();
					ApplicationLauncher.logger.error("FanTestSetup : Init : setSetupTimeInSec: Exception: " + e.getMessage());
					ApplicationLauncher.InformUser("Incorrect number", "Kindly enter valid input number for setup time" ,AlertType.ERROR);
					rowData.setSetupTimeInSec(t.getOldValue());
				}
			}
		});


		colTestSetupTestPointName.setCellValueFactory(data -> data.getValue().getTestPointNameProperty());
		colTestSetupTestPointName.setCellFactory(TextFieldTableCell.forTableColumn());
		colTestSetupTestPointName.setOnEditCommit(new EventHandler<CellEditEvent<FanTestSetup, String>>() {
			public void handle(CellEditEvent<FanTestSetup, String> t) {
				FanTestSetup rowData = ((FanTestSetup) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				if(t.getNewValue() != null){

					rowData.setTestPointName(t.getNewValue());
					ref_tvTestSetup.refresh();

				}
			}
		});

		colTestSetupTargetVoltage.setCellValueFactory(data -> data.getValue().getTargetVoltageProperty());
		colTestSetupTargetVoltage.setCellFactory(TextFieldTableCell.forTableColumn());
		colTestSetupTargetVoltage.setOnEditCommit(new EventHandler<CellEditEvent<FanTestSetup, String>>() {
			public void handle(CellEditEvent<FanTestSetup, String> t) {
				FanTestSetup rowData = ((FanTestSetup) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				if(t.getNewValue() != null){

					rowData.setTargetVoltage(t.getNewValue());
					ref_tvTestSetup.refresh();

				}
			}
		});

		/*colTestSetupRpmLowerLimit.setCellValueFactory(data -> data.getValue().getRpmLowerLimitProperty());
		colTestSetupRpmLowerLimit.setCellFactory(TextFieldTableCell.forTableColumn());
		colTestSetupRpmLowerLimit.setOnEditCommit(new EventHandler<CellEditEvent<FanTestSetup, String>>() {
			public void handle(CellEditEvent<FanTestSetup, String> t) {
				FanTestSetup rowData = ((FanTestSetup) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				if(t.getNewValue() != null){

					rowData.setRpmLowerLimit(t.getNewValue());
					ref_tvTestSetup.refresh();

				}
			}
		});*/


		/*		colTestSetupRpmUpperLimit.setCellValueFactory(data -> data.getValue().getRpmUpperLimitProperty());
		colTestSetupRpmUpperLimit.setCellFactory(TextFieldTableCell.forTableColumn());
		colTestSetupRpmUpperLimit.setOnEditCommit(new EventHandler<CellEditEvent<FanTestSetup, String>>() {
			public void handle(CellEditEvent<FanTestSetup, String> t) {
				FanTestSetup rowData = ((FanTestSetup) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				if(t.getNewValue() != null){

					rowData.setRpmUpperLimit(t.getNewValue());
					ref_tvTestSetup.refresh();

				}
			}
		});*/


		/*colTestSetupWindSpeedLowerLimit.setCellValueFactory(data -> data.getValue().getWindSpeedLowerLimitProperty());
		colTestSetupWindSpeedLowerLimit.setCellFactory(TextFieldTableCell.forTableColumn());
		colTestSetupWindSpeedLowerLimit.setOnEditCommit(new EventHandler<CellEditEvent<FanTestSetup, String>>() {
			public void handle(CellEditEvent<FanTestSetup, String> t) {
				FanTestSetup rowData = ((FanTestSetup) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				if(t.getNewValue() != null){

					rowData.setWindSpeedLowerLimit(t.getNewValue());
					ref_tvTestSetup.refresh();

				}
			}
		});*/


		/*		colTestSetupWindSpeedUpperLimit.setCellValueFactory(data -> data.getValue().getWindSpeedUpperLimitProperty());
		colTestSetupWindSpeedUpperLimit.setCellFactory(TextFieldTableCell.forTableColumn());
		colTestSetupWindSpeedUpperLimit.setOnEditCommit(new EventHandler<CellEditEvent<FanTestSetup, String>>() {
			public void handle(CellEditEvent<FanTestSetup, String> t) {
				FanTestSetup rowData = ((FanTestSetup) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				if(t.getNewValue() != null){

					rowData.setWindSpeedUpperLimit(t.getNewValue());
					ref_tvTestSetup.refresh();

				}
			}
		});*/


		/*colTestSetupWattsLowerLimit.setCellValueFactory(data -> data.getValue().getWattsLowerLimitProperty());
		colTestSetupWattsLowerLimit.setCellFactory(TextFieldTableCell.forTableColumn());
		colTestSetupWattsLowerLimit.setOnEditCommit(new EventHandler<CellEditEvent<FanTestSetup, String>>() {
			public void handle(CellEditEvent<FanTestSetup, String> t) {
				FanTestSetup rowData = ((FanTestSetup) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				if(t.getNewValue() != null){

					rowData.setWattsLowerLimit(t.getNewValue());
					ref_tvTestSetup.refresh();

				}
			}
		});*/


		/*		colTestSetupWattsUpperLimit.setCellValueFactory(data -> data.getValue().getWattsUpperLimitProperty());
		colTestSetupWattsUpperLimit.setCellFactory(TextFieldTableCell.forTableColumn());
		colTestSetupWattsUpperLimit.setOnEditCommit(new EventHandler<CellEditEvent<FanTestSetup, String>>() {
			public void handle(CellEditEvent<FanTestSetup, String> t) {
				FanTestSetup rowData = ((FanTestSetup) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				if(t.getNewValue() != null){

					rowData.setWattsUpperLimit(t.getNewValue());
					ref_tvTestSetup.refresh();

				}
			}
		});*/


		/*colTestSetupActivePowerLowerLimit.setCellValueFactory(data -> data.getValue().getActivePowerLowerLimitProperty());
		colTestSetupActivePowerLowerLimit.setCellFactory(TextFieldTableCell.forTableColumn());
		colTestSetupActivePowerLowerLimit.setOnEditCommit(new EventHandler<CellEditEvent<FanTestSetup, String>>() {
			public void handle(CellEditEvent<FanTestSetup, String> t) {
				FanTestSetup rowData = ((FanTestSetup) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				if(t.getNewValue() != null){

					rowData.setActivePowerLowerLimit(t.getNewValue());
					ref_tvTestSetup.refresh();

				}
			}
		});*/



		/*		colTestSetupActivePowerUpperLimit.setCellValueFactory(data -> data.getValue().getActivePowerUpperLimitProperty());
		colTestSetupActivePowerUpperLimit.setCellFactory(TextFieldTableCell.forTableColumn());
		colTestSetupActivePowerUpperLimit.setOnEditCommit(new EventHandler<CellEditEvent<FanTestSetup, String>>() {
			public void handle(CellEditEvent<FanTestSetup, String> t) {
				FanTestSetup rowData = ((FanTestSetup) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				if(t.getNewValue() != null){

					rowData.setActivePowerUpperLimit(t.getNewValue());
					ref_tvTestSetup.refresh();

				}
			}
		});*/


		/*colTestSetupPfLowerLimit.setCellValueFactory(data -> data.getValue().getPowerFactorLowerLimitProperty());
		colTestSetupPfLowerLimit.setCellFactory(TextFieldTableCell.forTableColumn());
		colTestSetupPfLowerLimit.setOnEditCommit(new EventHandler<CellEditEvent<FanTestSetup, String>>() {
			public void handle(CellEditEvent<FanTestSetup, String> t) {
				FanTestSetup rowData = ((FanTestSetup) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				if(t.getNewValue() != null){

					rowData.setPowerFactorLowerLimit(t.getNewValue());
					ref_tvTestSetup.refresh();

				}
			}
		});*/


		/*		colTestSetupPfUpperLimit.setCellValueFactory(data -> data.getValue().getPowerFactorUpperLimitProperty());
		colTestSetupPfUpperLimit.setCellFactory(TextFieldTableCell.forTableColumn());
		colTestSetupPfUpperLimit.setOnEditCommit(new EventHandler<CellEditEvent<FanTestSetup, String>>() {
			public void handle(CellEditEvent<FanTestSetup, String> t) {
				FanTestSetup rowData = ((FanTestSetup) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				if(t.getNewValue() != null){

					rowData.setPowerFactorUpperLimit(t.getNewValue());
					ref_tvTestSetup.refresh();

				}
			}
		});*/


		/*colTestSetupCurrentLowerLimit.setCellValueFactory(data -> data.getValue().getCurrentLowerLimitProperty());
		colTestSetupCurrentLowerLimit.setCellFactory(TextFieldTableCell.forTableColumn());
		colTestSetupCurrentLowerLimit.setOnEditCommit(new EventHandler<CellEditEvent<FanTestSetup, String>>() {
			public void handle(CellEditEvent<FanTestSetup, String> t) {
				FanTestSetup rowData = ((FanTestSetup) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				if(t.getNewValue() != null){

					rowData.setCurrentLowerLimit(t.getNewValue());
					ref_tvTestSetup.refresh();

				}
			}
		});*/


		/*		colTestSetupCurrentUpperLimit.setCellValueFactory(data -> data.getValue().getCurrentUpperLimitProperty());
		colTestSetupCurrentUpperLimit.setCellFactory(TextFieldTableCell.forTableColumn());
		colTestSetupCurrentUpperLimit.setOnEditCommit(new EventHandler<CellEditEvent<FanTestSetup, String>>() {
			public void handle(CellEditEvent<FanTestSetup, String> t) {
				FanTestSetup rowData = ((FanTestSetup) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				if(t.getNewValue() != null){

					rowData.setCurrentUpperLimit(t.getNewValue());
					ref_tvTestSetup.refresh();

				}
			}
		});*/

	}

	/**
	 * Configures table columns with cell factories and value factories.
	 */
	/*private void updateTestSetupTableForModel(String selectedModelName) {
		List<DutMasterData> dutMasterDataList = DeviceDataManagerController.getDutMasterDataService().findAll();

		Optional<DutMasterData> selectedDutOpt = dutMasterDataList.stream()
				.filter(d -> d.getModelName().equals(selectedModelName))
				.findFirst();

		selectedDutOpt.ifPresent(dut -> {
			List<FanTestSetup> fanList = dut.getFanTestSetupList();
			if (fanList != null) {
				ref_tvTestSetup.getItems().clear();
				ref_tvTestSetup.getItems().addAll(fanList);
				serialNoAtomic.set(fanList.size() + 1);
			}
		});
	}*/
	// Sort by serial number
	private void updateTestSetupTableForModel(String selectedModelName) {
		List<DutMasterData> dutMasterDataList = DeviceDataManagerController.getDutMasterDataService().findAll();

		Optional<DutMasterData> selectedDutOpt = dutMasterDataList.stream()
				.filter(d -> d.getModelName().equals(selectedModelName))
				.findFirst();

		selectedDutOpt.ifPresent(dut -> {
			List<FanTestSetup> fanList = dut.getFanTestSetupList();
			if (fanList != null) {
				// Sort the list by serial number
				fanList.sort(Comparator.comparingInt(FanTestSetup::getSerialNo)); // Assuming getSerialNo() returns int

				ref_tvTestSetup.getItems().clear();
				ref_tvTestSetup.getItems().addAll(fanList);
				serialNoAtomic.set(fanList.size() + 1);
			}
		});
	}

	/**
	 * Loads model data from the database and populates the UI.
	 */
	private void loadDataFromDb() {
		List<DutMasterData> dutMasterDataList = DeviceDataManagerController.getDutMasterDataService().findAll();
		List<String> modelList = new ArrayList<String>();
		modelList = dutMasterDataList.stream().map(e->e.getModelName()).collect(Collectors.toList());
		if (!modelList.isEmpty()) {
			ref_cmbBxModelName.getItems().clear();
			ref_cmbBxModelName.getItems().addAll(modelList);
			ref_cmbBxModelName.getSelectionModel().clearSelection();

			String selectedModel = ref_cmbBxModelName.getSelectionModel().getSelectedItem();

			Optional<DutMasterData> selectedDutOpt = dutMasterDataList.stream()
					.filter(d -> d.getModelName().equals(selectedModel))
					.findFirst();

			selectedDutOpt.ifPresent(dut -> {
				List<FanTestSetup> fanList = dut.getFanTestSetupList();
				if (fanList != null) {
					ref_tvTestSetup.getItems().clear();
					ref_tvTestSetup.getItems().addAll(fanList);
					serialNoAtomic.set(fanList.size()+1);
				}else {
					serialNoAtomic.set(1);
				}
			});
		}
	}

	/**
	 * Assigns references to static fields for UI components.
	 */
	private void refAssignment() {
		ref_txtNewFanSerialNo = txtNewFanSerialNo;
		ref_cmbBxModelName = cmbBxModelName;
		ref_tvTestSetup = tvTestSetup;

		ref_btnStart			 = btnStart;
		ref_btnStop				 = btnStop;

		ref_btnDimmerForward     = btnDimmerForward;
		ref_btnDimmerReverse     = btnDimmerReverse;

		ref_btnDimmerForwardExecute     = btnDimmerForwardExecute;
		ref_btnDimmerReverseExecute     = btnDimmerReverseExecute;

		ref_btn3PhaseCurrentAvg  = btn3PhaseCurrentAvg;
		ref_btn3PhasePFAvg       = btn3PhasePFAvg;
		ref_btn3PhaseVATotal     = btn3PhaseVATotal;
		ref_btn3PhaseVBR         = btn3PhaseVBR;
		ref_btn3PhaseVLL         = btn3PhaseVLL;
		ref_btn3PhaseVLN         = btn3PhaseVLN;
		ref_btn3PhaseVRY         = btn3PhaseVRY;
		ref_btn3PhaseVYB         = btn3PhaseVYB;
		ref_btn3PhaseWattsTotal  = btn3PhaseWattsTotal;

		ref_btnBPhaseCurrent     = btnBPhaseCurrent;
		ref_btnBPhasePF          = btnBPhasePF;
		ref_btnBPhaseVA          = btnBPhaseVA;
		ref_btnBPhaseVoltage     = btnBPhaseVoltage;
		ref_btnBPhaseWatts       = btnBPhaseWatts;

		ref_btnDimmerIsMin       = btnDimmerIsMin;
		ref_btnDimmerIsMinExecute        = btnDimmerIsMinExecute;
		ref_btnFanRpm            = btnFanRpm;
		ref_btnFanWindspeed      = btnFanWindspeed;

		ref_btnFanRpmExecute            = btnFanRpmExecute;
		ref_btnFanWindspeedExecute      = btnFanWindspeedExecute;

		ref_btnVoltageMainOn	= btnVoltageMainOn;
		ref_btnVoltageMainOff	= btnVoltageMainOff;


		ref_btnRPhaseCurrent     = btnRPhaseCurrent;
		ref_btnRPhasePF          = btnRPhasePF;
		ref_btnRPhaseVA          = btnRPhaseVA;
		ref_btnRPhaseVoltage     = btnRPhaseVoltage;
		ref_btnRPhaseWatts       = btnRPhaseWatts;

		ref_btnSetVoltage        = btnSetVoltage;
		ref_btnTestVoltage       = btnTestVoltage;

		ref_btnSetVoltageExecute        = btnSetVoltageExecute;
		ref_btnTestVoltageExecute       = btnTestVoltageExecute;

		ref_btnYPhaseCurrent     = btnYPhaseCurrent;
		ref_btnYPhasePF          = btnYPhasePF;
		ref_btnYPhaseVA          = btnYPhaseVA;
		ref_btnYPhaseVoltage     = btnYPhaseVoltage;
		ref_btnYPhaseWatts       = btnYPhaseWatts;	

		pi_Auto3Phase		.setVisible(false);
		pi_AutoBPhase		.setVisible(false);
		pi_AutoFan			.setVisible(false);
		pi_AutoRPhase		.setVisible(false);
		pi_AutoYPhase		.setVisible(false);

		ref_txtForwardInMsec     = txtForwardInMsec;
		ref_txtReverseInMsec     = txtReverseInMsec;
		ref_txtForwardInMsecExecute     = txtForwardInMsecExecute;
		ref_txtReverseInMsecExecute     = txtReverseInMsecExecute;

		ref_txt3PhaseCurrentAvg  = txt3PhaseCurrentAvg;
		ref_txt3PhasePFAvg       = txt3PhasePFAvg;
		ref_txt3PhaseVATotal     = txt3PhaseVATotal;
		ref_txt3PhaseVBR         = txt3PhaseVBR;
		ref_txt3PhaseVLL         = txt3PhaseVLL;
		ref_txt3PhaseVLN         = txt3PhaseVLN;
		ref_txt3PhaseVRY         = txt3PhaseVRY;
		ref_txt3PhaseVYB         = txt3PhaseVYB;
		ref_txt3PhaseWattsTotal  = txt3PhaseWattsTotal;

		ref_txtBPhaseCurrent     = txtBPhaseCurrent;
		ref_txtBPhasePF          = txtBPhasePF;
		ref_txtBPhaseVA          = txtBPhaseVA;
		ref_txtBPhaseVoltage     = txtBPhaseVoltage;
		ref_txtBPhaseWatts       = txtBPhaseWatts;

		ref_txtRPhaseCurrent     = txtRPhaseCurrent;
		ref_txtRPhasePF          = txtRPhasePF;
		ref_txtRPhaseVA          = txtRPhaseVA;
		ref_txtRPhaseVoltage     = txtRPhaseVoltage;
		ref_txtRPhaseWatts       = txtRPhaseWatts;

		ref_txtYPhaseCurrent     = txtYPhaseCurrent;
		ref_txtYPhasePF          = txtYPhasePF;
		ref_txtYPhaseVA          = txtYPhaseVA;
		ref_txtYPhaseVoltage     = txtYPhaseVoltage;
		ref_txtYPhaseWatts       = txtYPhaseWatts;

		ref_txtSetVoltage        = txtSetVoltage;
		ref_txtTestVoltage       = txtTestVoltage;
		ref_txtSetVoltageExecute        = txtSetVoltageExecute;
		ref_txtTestVoltageExecute       = txtTestVoltageExecute;

		ref_txtFanRpm			 = txtFanRpm;
		ref_txtFanWindSpeed		 = txtFanWindSpeed;

		ref_txtFanRpmExecute			 = txtFanRpmExecute;
		ref_txtFanWindSpeedExecute		 = txtFanWindSpeedExecute;
		// FAN TESTING DEBUG
		// Disable STOP button
		//ref_btnStop.setDisable(true);

	}

	/**
	 * Sets up listeners for UI component events.
	 */
	public void listenersAssignment() {
		toggle_AutoRPhase.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
			if (isSelected) {
				startAutoRPhaseUpdate();
			} else {
				stopAutoRPhaseUpdate();
			}
		});

		toggle_AutoYPhase.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
			if (isSelected) {
				startAutoYPhaseUpdate();
			} else {
				stopAutoYPhaseUpdate();
			}
		});

		toggle_AutoBPhase.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
			if (isSelected) {
				startAutoBPhaseUpdate();
			} else {
				stopAutoBPhaseUpdate();
			}
		});

		toggle_Auto3Phase.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
			if (isSelected) {
				startAuto3PhaseUpdate();
			} else {
				stopAuto3PhaseUpdate();
			}
		});

		toggle_AutoFan.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
			if (isSelected) {
				startAutoFanUpdate();
			} else {
				stopAutoFanUpdate();
			}
		});


	}

	/**
	 * Adds a new model to the database and updates the combo box.
	 */
	@FXML
	void btnAddModelOnClick(ActionEvent event) {

		String modelName = ref_txtNewFanSerialNo.getText();
		ApplicationLauncher.logger.debug("displayDataObj: modelName: " + modelName);
		DutMasterData dutMasterDataObj = new DutMasterData();
		dutMasterDataObj.setActive(true);
		dutMasterDataObj.setModelName(modelName);
		dutMasterDataObj.setDutBaseName("");
		dutMasterDataObj.setDutType("");
		dutMasterDataObj.setRatedCurrent("");
		dutMasterDataObj.setRatedVoltage("");

		DeviceDataManagerController.getDutMasterDataService().saveToDb(dutMasterDataObj);

		ref_cmbBxModelName.getItems().add(modelName);
		ref_cmbBxModelName.getSelectionModel().select(modelName);
	}

	/*@FXML
	void btnAddTestPointOnClick(ActionEvent event) {

		FanTestSetup fanTestSetup = new FanTestSetup();
		fanTestSetup.setTestPointName("TestName"+serialNoAtomic.get());
		fanTestSetup.setSerialNo(serialNoAtomic.getAndIncrement());
		fanTestSetup.setActive(true);
		fanTestSetup.setRpmLowerLimit("RpmLow");
		fanTestSetup.setRpmUpperLimit("RpmHigh");

		fanTestSetup.setSetupTimeInSec(20);
		fanTestSetup.setTargetVoltage("240.5");
		fanTestSetup.setWindSpeedLowerLimit("WsLower");
		fanTestSetup.setWattsLowerLimit("WattLower");
		fanTestSetup.setPowerFactorLowerLimit("PfLower");
		fanTestSetup.setActivePowerLowerLimit("ApLower");
		fanTestSetup.setCurrentLowerLimit("I-Lower");

		fanTestSetup.setWindSpeedUpperLimit("WsUpper");
		fanTestSetup.setWattsUpperLimit("WattsUpper");
		fanTestSetup.setPowerFactorUpperLimit("PfUpper");
		fanTestSetup.setActivePowerUpperLimit("ApUpper");
		fanTestSetup.setCurrentUpperLimit("I-Upper");

		ref_tvTestSetup.getItems().add(fanTestSetup);
	}
	 */


	/**
	 * Saves test setup data to the database.
	 */
	@FXML
	public void btnSaveOnClick() {

		ApplicationLauncher.logger.info("btnSaveOnClick: Entry");
		saveOnClickTimer = new Timer();
		saveOnClickTimer.schedule(new SaveTaskClick(), 50);


	}

	class SaveTaskClick extends TimerTask {
		public void run() {
			saveToDbTask();
			saveOnClickTimer.cancel();


		}
	}

	// ================================== TEST EXECUTION ================================

	/**
	 * Handles the "Start" button click event.
	 * - Logs the start of execution
	 * - Disables controls except the stop button
	 * - Initializes the execution environment
	 * - Creates a new ProjectRun entry
	 * - Begins running test points sequentially
	 */
	@FXML
	private void btnTestPointStartOnClick() {
		appendLog("Start Execution", LogLevel.INFO);
		setControlsDisabled(true); // Disable all except stop

		isRunning = true;
		isStopped = false;
		currentIndex = 0;

		// To be implemented:
		// Create ProjectRun entry	  
		String projectName = cmbBxModelName.getSelectionModel().getSelectedItem();

		// Check if model selected
		if (projectName == null || projectName.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Missing Project Name");
			alert.setHeaderText(null);
			alert.setContentText("Please select model.");
			alert.showAndWait();
			setControlsDisabled(false); // Re-enable buttons if previously disabled
			return; // Exit early
		}

		String fanSerialNumber = ref_txtNewFanSerialNo.getText();

		// Check if serial number is empty
		if (fanSerialNumber.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Missing Serial Number");
			alert.setHeaderText(null);
			alert.setContentText("Please enter a valid fan serial number before starting the test.");
			alert.showAndWait();
			setControlsDisabled(false); // Re-enable buttons if previously disabled
			return; // Exit early
		}

		// Check if serial number already exists
		// If exist prompt user and ask consent
		List<Result> existingResults = DeviceDataManagerController.getResultService().findByFanSerialNumber(fanSerialNumber);

		if (existingResults != null && !existingResults.isEmpty()) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Serial Number Exists");
			alert.setHeaderText("The fan serial number '" + fanSerialNumber + "' already has test results.");
			alert.setContentText("Do you want to continue with this serial number? This may overwrite or append to existing results.");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.isPresent() && result.get() == ButtonType.OK) {
				appendLog("User consented to use existing serial number: " + fanSerialNumber, LogLevel.INFO);
			} else {
				appendLog("User cancelled due to existing serial number: " + fanSerialNumber, LogLevel.ERROR);
				setControlsDisabled(false); // Re-enable buttons
				return; // Exit early
			}
		}	    

		// Check for existing INPROGRESS project run
		ProjectRun existingInProgressRun = displayDataObj.getProjectRunService()
				.findTopByProjectNameAndExecutionStatusOrderByDeploymentIdDesc(projectName, "Not Started");

		if (existingInProgressRun != null) {
			// Use existing project run
			currentProjectRun = existingInProgressRun;
			appendLog("Project Run Exists: " + projectName, LogLevel.DEBUG);
		} else {
			// Create new project run
			String lastDeploymentIdStr = displayDataObj.getProjectRunService()
					.findTopByProjectNameOrderByDeploymentIdDesc(projectName);
			int nextDeploymentId = Integer.parseInt(lastDeploymentIdStr) + 1;

			createProjectRun(String.valueOf(nextDeploymentId), projectName);
			appendLog("Deployed new project run for model: " + projectName, LogLevel.DEBUG);
		}

		String defaultStatus = ConstantStatus.IN_PROG;

		for (FanTestSetup tp : ref_tvTestSetup.getItems()) {
			String testPointName = tp.getTestPointName();

			// Check if result already exists with INPROGRESS
			/*
			 * Result existingResult = DeviceDataManagerController.getResultService()
			 * .findByFanSerialNumberAndTestPointNameAndProjectRunAndTestStatus(
			 * fanSerialNumber, testPointName, currentProjectRun, ConstantStatus.IN_PROG );
			 */

			/*
			 * Result existingResult = DeviceDataManagerController.getResultService()
			 * .findByFanSerialNumberAndTestPointNameAndProjectRunAndTestStatus(
			 * fanSerialNumber, testPointName, currentProjectRun, ConstantStatus.IN_PROG );
			 */

			if (existingResults == null || existingResults.isEmpty()) {
				createResult(fanSerialNumber, testPointName, defaultStatus, currentProjectRun);
			} else {
				appendLog("Skipping creation for test point " + testPointName + " (already INPROGRESS)", LogLevel.DEBUG);
			}
		}


		// Proceed with test execution
		runTestPointsSequentially();
	}

	public void createProjectRun(String deploymentId,String projectName) {

		ProjectRun projectRun = new ProjectRun();
		projectRun.setDeploymentId(deploymentId);
		long epochStartTime = Instant.now().toEpochMilli()/1000;				
		projectRun.setEpochStartTime(epochStartTime);
		Date date = new Date(epochStartTime * 1000 );

		// format of the date  
		SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String startTime = jdf.format(date);
		projectRun.setStartTime(startTime);
		projectRun.setExecutionStatus(ConstantApp.EXECUTION_STATUS_NOT_STARTED);
		projectRun.setProjectName(projectName);

		currentProjectRun = projectRun;

		displayDataObj.getProjectRunService().saveToDb(projectRun);
	}

	/*public String getLastDeploymentId(String projectName) {
	    // Fetch the ProjectRun with the highest deployment ID for the given project name
	    // This could be a custom query like: 
	    // SELECT MAX(deploymentId) FROM project_run WHERE project_name = :projectName

	    ProjectRun lastRun = displayDataObj.getProjectRunService().findTopByProjectNameOrderByDeploymentIdDesc(projectName);
	    if (lastRun != null) {
	        return lastRun.getDeploymentId();
	    } else {
	        return "0"; // or some default
	    }
	}*/

	/**
	 * Handles the "Step Run" button click event.
	 * - Executes a single test point
	 * - Updates progress and status
	 * - Validates test results
	 * - Moves to next test point if successful
	 */
	@FXML
	private void btnTestPointStepRunOnClick() {
		appendLog("Step Run Execution", LogLevel.INFO);
		setControlsDisabled(true);
		isStepRun = true; // Indicate we're in Step Run mode

		FanTestSetup selectedTestPoint = ref_tvTestSetup.getSelectionModel().getSelectedItem();

		if (selectedTestPoint != null) {
			Task<Void> task = new Task<Void>() {
				@Override
				protected Void call() {
					runSingleTestPoint(selectedTestPoint);
					return null;
				}
			};
			new Thread(task).start();
		} else {
			appendLog("No test point selected.", LogLevel.ERROR);
			setControlsDisabled(false);
		}
	}

	/**
	 * Handles the "Resume" button click event.
	 * - Resumes test execution from the last stopped point
	 * - Restores execution state
	 * - Continues sequential test execution
	 */
	@FXML
	private void btnTestPointResumeOnClick() {
		appendLog("Resume Execution", LogLevel.INFO);
		setControlsDisabled(true);

		int selectedIndex = ref_tvTestSetup.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0 && selectedIndex < testPoints.size()) {
			currentIndex = selectedIndex;
		}

		isRunning = true;
		isStopped = false;
		runTestPointsSequentially();
	}

	/**
	 * Handles the "Stop" button click event.
	 * - Stops the current test execution
	 * - Updates test point status
	 * - Enables control buttons
	 * - Saves current progress
	 */
	@FXML
	private void btnTestPointStopOnClick() {
		appendLog("Stop Execution", LogLevel.INFO);

		isRunning = false;
		isStopped = true;

		// Reset voltage to 0 only if not in simulation mode
		if (!SIMULATION_MODE) {
			appendLog("Set Voltage : 0", LogLevel.INFO);
			TestPointUtils.setVoltage("0");
			ApplicationLauncher.logger.info("Execution stopped manually. Voltage set to 0.");
		} else {
			appendLog("SIMULATION: Voltage reset skipped.", LogLevel.INFO);
		}

		setControlsDisabled(false); // Re-enable all buttons
	}

	/**
	 * Handles the "Close Project" button click event.
	 * - Placeholder for future implementation to close the current project session.
	 */
	@FXML
	void btnTestPointCloseProjectOnClick(ActionEvent event) {
		// To be implemented: logic to handle closing the project
		appendLog("Start Execution", LogLevel.INFO);
		setControlsDisabled(true); // Disable all except stop

		isRunning = true;
		isStopped = false;
		currentIndex = 0;

		// To be implemented:
		// Create ProjectRun entry	  
		String projectName = cmbBxModelName.getSelectionModel().getSelectedItem();

		// Check for existing INPROGRESS project run
		ProjectRun existingInProgressRun = displayDataObj.getProjectRunService()
				.findTopByProjectNameAndExecutionStatusOrderByDeploymentIdDesc(projectName, "Not Started");

		if (existingInProgressRun != null) {
			// Use existing project run
			currentProjectRun = existingInProgressRun;
			appendLog("Project Run Exists " + projectName, LogLevel.DEBUG);
		} else {
			// Create new project run
			String lastDeploymentIdStr = displayDataObj.getProjectRunService()
					.findTopByProjectNameOrderByDeploymentIdDesc(projectName);
			int nextDeploymentId = Integer.parseInt(lastDeploymentIdStr) + 1;

			createProjectRun(String.valueOf(nextDeploymentId), projectName);
			appendLog("Deployed new project run for model: " + projectName, LogLevel.DEBUG);
		}

		String fanSerialNumber = ref_txtNewFanSerialNo.getText();

		// Check if serial number is empty
		if (fanSerialNumber.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Missing Serial Number");
			alert.setHeaderText(null);
			alert.setContentText("Please enter a valid fan serial number before starting the test.");
			alert.showAndWait();
			setControlsDisabled(false); // Re-enable buttons if previously disabled
			return; // Exit early
		}

		String defaultStatus = ConstantStatus.IN_PROG;

		for (FanTestSetup tp : ref_tvTestSetup.getItems()) {
			String testPointName = tp.getTestPointName();

			// Check if result already exists with INPROGRESS
			Result existingResult = DeviceDataManagerController.getResultService()
					.findByFanSerialNumberAndTestPointNameAndProjectRunAndTestStatus(
							fanSerialNumber, testPointName, currentProjectRun, ConstantStatus.IN_PROG
							);

			if (existingResult == null) {
				createResult(fanSerialNumber, testPointName, defaultStatus, currentProjectRun);
			} else {
				appendLog("Skipping creation for test point " + testPointName + " (already INPROGRESS)", LogLevel.DEBUG);
			}
		}

		fanSerialNumber = ref_txtNewFanSerialNo.getText();

		FanTestSetup testPoint = testPoints.get(currentIndex);
		// Update results to DB
		updateResultMeasurements(
			    fanSerialNumber,
			    LocalDateTime.now(),
			    testPoint.getTestPointName(),
			    currentResult.getVoltage(),
			    currentResult.getRpm(),
			    currentResult.isRpmValid(),
			    currentResult.getWindSpeed(),
			    currentResult.isWindSpeedValid(),
			    currentResult.getVibration(),
			    currentResult.isVibrationValid(),
			    currentResult.getWatts(),
			    currentResult.isWattsValid(),
			    currentResult.getVa(),
			    currentResult.getCurrent(),
			    currentResult.isCurrentValid(),
			    currentResult.getPowerFactor(),
			    currentResult.isPowerFactorValid(),
			    currentResult.getTestStatus(),
			    currentResult.isActivePowerValid()
			);


		ApplicationLauncher.logger.debug("Test Point Name : " + testPoint.getTestPointName());
		appendLog("Result Updated : Test point : TestName1 ", LogLevel.INFO);
	}	

	/**
	 * Creates and saves a new Result entry in the database for the specified test point.
	 *
	 * @param fanSerialNumber The serial number of the fan under test.
	 * @param testPointName   The name or identifier of the test point.
	 * @param testStatus      The current status of the test (e.g., "In Progress", "Completed", "Failed").
	 * @param projectRun      The ProjectRun instance this result is associated with.
	 */
	public void createResult(String fanSerialNumber, String testPointName, String testStatus, ProjectRun projectRun) {
		Result result = new Result();

		result.setFanSerialNumber(fanSerialNumber);
		result.setTestPointName(testPointName);
		result.setTestStatus(testStatus);
		result.setProjectRun(projectRun);

		DeviceDataManagerController.getResultService().saveToDb(result);
	}

	/**
	 * Updates measurement values for a specific Result entry.
	 *
	 * @param projectRun      The associated ProjectRun instance.
	 * @param fanSerialNumber The fan's serial number.
	 * @param testPointName   The test point name to identify the result.
	 * @param rpm             Measured RPM.
	 * @param windspeed       Measured windspeed.
	 * @param watts           Measured watts.
	 * @param va              Measured VA.
	 * @param current         Measured current.
	 * @param powerFactor     Measured power factor.
	 * @param status          Final test status ("Completed", "Failed", etc.).
	 */
	public void updateResultMeasurements(ProjectRun projectRun, String fanSerialNumber, String testPointName,
			String rpm, String windspeed, String watts, String va, String current,
			String powerFactor, String status) {


		/*Result test1 = displayDataObj.getResultService()
	            .findByProjectRunAndTestPointName(projectRun,testPointName);

		if (test1 != null) {
			ApplicationLauncher.logger.debug("Test 1 Found : By Project Run and Test Point Name") ;
		}*/

		Result result = DeviceDataManagerController.getResultService()
				.findByProjectRunAndFanSerialNumberAndTestPointName(projectRun, fanSerialNumber, testPointName);

		if (result != null) {
			result.setRpm(rpm);
			result.setWindSpeed(windspeed);
			result.setWatts(watts);
			result.setVa(va);
			result.setCurrent(current);
			result.setPowerFactor(powerFactor);
			result.setTestStatus(status);

			DeviceDataManagerController.getResultService().saveToDb(result);
		} else {
			ApplicationLauncher.logger.debug("Project Run : " + projectRun);
			ApplicationLauncher.logger.debug("Fan Serial Number : " + fanSerialNumber);
			ApplicationLauncher.logger.debug("Test Point Name : " + testPointName);
			appendLog("Result not found for test point: " + testPointName, LogLevel.INFO);
			ApplicationLauncher.logger.debug("Result not found for test point: " + testPointName);
		}
	}

	/**
	 * Updates measurement values for a specific Result entry.
	 *
	 * @param projectRun      The associated ProjectRun instance.
	 * @param fanSerialNumber The fan's serial number.
	 * @param testPointName   The test point name to identify the result.
	 * @param rpm             Measured RPM.
	 * @param windspeed       Measured windspeed.
	 * @param watts           Measured watts.
	 * @param va              Measured VA.
	 * @param current         Measured current.
	 * @param powerFactor     Measured power factor.
	 * @param status          Final test status ("Completed", "Failed", etc.).
	 */
	public void updateResultMeasurements(
	        String fanSerialNumber,
	        LocalDateTime dateTime,
	        String testPointName,
	        String voltage,
	        String rpm,
	        boolean rpmValid,
	        String windspeed,
	        boolean windSpeedValid,
	        String vibration,
	        boolean vibrationValid,
	        String watts,
	        boolean wattsValid,
	        String va,
	        String current,
	        boolean currentValid,
	        String powerFactor,
	        boolean powerFactorValid,
	        String status,
	        boolean activePowerValid
	)
	{

		Result result = DeviceDataManagerController.getResultService()
				.findByFanSerialNumberAndTestPointName(fanSerialNumber, testPointName);

		if (result != null) {
		    result.setDateTime(dateTime);
		    result.setEpochTime(convertToEpoch(dateTime));

		    //result.setTestPointName(testPointName); // Optional, in case you need to update it
		    //result.setFanSerialNumber(fanSerialNumber); // Optional

		    result.setVoltage(voltage);
		    result.setRpm(rpm);
		    result.setRpmValid(rpmValid);

		    result.setWindSpeed(windspeed);
		    result.setWindSpeedValid(windSpeedValid);

		    result.setVibration(vibration);
		    result.setVibrationValid(vibrationValid);

		    result.setCurrent(current);
		    result.setCurrentValid(currentValid);

		    result.setWatts(watts);
		    result.setWattsValid(wattsValid);

		    result.setVa(va);

		    result.setPowerFactor(powerFactor);
		    result.setPowerFactorValid(powerFactorValid);

		    result.setTestStatus(status);
		    result.setActivePowerValid(activePowerValid);

		    DeviceDataManagerController.getResultService().saveToDb(result);
		}
 else {
			ApplicationLauncher.logger.debug("Fan Serial Number : " + fanSerialNumber);
			ApplicationLauncher.logger.debug("Test Point Name : " + testPointName);
			appendLog("Result not found for test point: " + testPointName, LogLevel.INFO);
			ApplicationLauncher.logger.debug("Result not found for test point: " + testPointName);
		}
	}
	
	/**
	 * Converts a LocalDateTime to epoch milliseconds in UTC.
	 * Returns null if input is null.
	 */
	private Long convertToEpoch(LocalDateTime dateTime) {
	    if (dateTime == null) {
	        return null;
	    }
	    return dateTime.atZone(ZoneOffset.UTC)
	                   .toInstant()
	                   .toEpochMilli();
	}


	// Call this method to disable or enable test control buttons
	private void setControlsDisabled(boolean disabled) {
		btnTestPointStart.setDisable(disabled);
		btnTestPointStepRun.setDisable(disabled);
		btnTestPointResume.setDisable(disabled);
		btnTestPointStop.setDisable(!disabled); // Enable stop only during execution
	}

	// ===================================== RUNNING METHODS ====================================================
	/**
	 * Runs test points one after the other in a new thread.
	 * - Skips if stopped.
	 * - Supports jumping to a requested index (if set).
	 * - Executes each point using runSingleTestPoint().
	 */
	private void runTestPointsSequentially() {		
		new Thread(() -> {
			for (; currentIndex < testPoints.size(); currentIndex++) {
				if (!isRunning || isStopped) break;

				FanTestSetup testPoint = testPoints.get(currentIndex);
				Platform.runLater(() -> {
					testPoint.setStatus(ConstantStatus.IN_PROG);
					testPoint.setRpmActual("");
					testPoint.setCurrentActual("");
					testPoint.setWattsActual("");
					testPoint.setActivePowerActual("");
					testPoint.setPowerFactorActual("");
					testPoint.setWindSpeedActual("");
					testPoint.setVibrationActual("");

				});
				appendLog("Running : Testpoint : " + (currentIndex + 1), LogLevel.INFO);
				runSingleTestPoint(testPoint);

				// After completing a test point, check if resume was requested
				if (requestedResumeIndex != null) {
					if (requestedResumeIndex >= 0 && requestedResumeIndex < testPoints.size()) {
						currentIndex = requestedResumeIndex - 1; // -1 because loop will increment
						ApplicationLauncher.logger.info("Jumping to requested index: " + requestedResumeIndex);
						appendLog("Jumping to Index : " + requestedResumeIndex, LogLevel.INFO);
					}
					requestedResumeIndex = null; // Reset flag
				}

				try {
					Thread.sleep(500);  // small delay before next
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// After all test points are completed and not stopped
			if (!isStopped && currentIndex == testPoints.size()) {
				Platform.runLater(() -> {
					appendLog("All test points completed. Updating project status and serial number.", LogLevel.INFO);
					updateProjectRunStatus(ConstantStatus.COMPLETED); // Mark project run as completed
					updateNextSerialNumber(); // Increment serial number after completion
					setControlsDisabled(false); // Re-enable controls
				});
			}
		}).start();
	}

	// updateProjectRunStatus function
	/**
	 * Updates the status of the current ProjectRun.
	 */
	private void updateProjectRunStatus(String status) {
		if (currentProjectRun != null) {
			currentProjectRun.setExecutionStatus(status);
			currentProjectRun.setEndTime(String.valueOf(LocalDateTime.now())); // Set end time when status changes to completed/stopped/failed
			displayDataObj.getProjectRunService().saveToDb(currentProjectRun);
			appendLog("Project Run status updated to: " + status, LogLevel.INFO);
		} else {
			appendLog("No active Project Run to update status.", LogLevel.DEBUG);
		}
	}

	/**
	 * Executes a single test point:
	 * - Sets voltage.
	 * - Verifies voltage.
	 * - Waits for setup time.
	 * - Reads and validates measurements.
	 * - Updates UI and logs throughout.
	 * - Handles both Step Run (looped reading) and full run (single read).
	 */
	private void runSingleTestPoint(FanTestSetup testPoint) {

		Platform.runLater(() -> {
			testPoint.setIsRunning(true);
			tvTestSetup.refresh();
		});

		// Step 0: Mains ON
		appendLog("Turning Mains ON : ", LogLevel.INFO);
		Platform.runLater(() -> testPoint.setProgress(0.1));

		boolean mainsOn;
		if (SIMULATION_MODE) {
			appendLog("SIMULATION: Mains ON (no actual hardware call): ", LogLevel.INFO);
			mainsOn = true; // Simulate success
		} else {
			mainsOn = TestPointUtils.mainsOn();
		}

		if (!mainsOn || (!SIMULATION_MODE && (TestPointUtils.getLastErrorMessage().contains("COM") || TestPointUtils.getLastErrorMessage().contains("not found")))) {
			handleExecutionFailure(testPoint, "COM PORT NOT FOUND. Stopping execution." + (SIMULATION_MODE ? " (Simulated)" : ""));
			return;
		}

		appendLog("MAINS ON", LogLevel.INFO);

		String targetVoltage = testPoint.getTargetVoltage();
		int setupTime = testPoint.getSetupTimeInSec();

		// --- Conditional Execution for Voltage Set and Initial Wait ---
		// These steps are executed ONLY for the first test point in a sequence.
		if (isFirstTestPointInSequence) {
			// Step 1: Set Voltage
			appendLog("Setting Voltage : " + targetVoltage, LogLevel.INFO);
			Platform.runLater(() -> testPoint.setProgress(0.1));

			boolean voltageSet;
			if (SIMULATION_MODE) {
				appendLog("SIMULATION: Setting Voltage (no actual hardware call): " + targetVoltage, LogLevel.INFO);
				voltageSet = true; // Simulate success
			} else {
				voltageSet = TestPointUtils.setVoltage(targetVoltage);
			}

			if (!voltageSet || (!SIMULATION_MODE && (TestPointUtils.getLastErrorMessage().contains("COM") || TestPointUtils.getLastErrorMessage().contains("not found")))) {
				handleExecutionFailure(testPoint, "COM PORT NOT FOUND. Stopping execution." + (SIMULATION_MODE ? " (Simulated)" : ""));
				return;
			}

			// Step 2: Wait for voltage to stabilize
			appendLog("Waiting for Voltage Set", LogLevel.INFO);
			try {
				Thread.sleep(15000);
			} catch (InterruptedException ignored) {
				// Restore the interrupted status
				Thread.currentThread().interrupt();
				appendLog("Voltage stabilization wait interrupted.", LogLevel.DEBUG);
				handleExecutionFailure(testPoint, "Voltage stabilization interrupted.");
				return;
			}

			// After the first test point successfully completes these steps,
			// set the flag to false so subsequent test points in this run skip them.
			isFirstTestPointInSequence = false;
			appendLog("Voltage set and initial wait complete. Subsequent test points will skip this step.", LogLevel.INFO);
		} else {
			appendLog("Skipping Voltage Set and initial wait for subsequent test points in this run.", LogLevel.INFO);
		}

		// Step 3: Verify Voltage
		final int MAX_ATTEMPTS = 10;
		boolean voltageVerified = false;
		String actualVoltage = "";

		for (int attempt = 1; attempt <= MAX_ATTEMPTS && !isStopped; attempt++) {
			if (SIMULATION_MODE) {
				// Simulate voltage near target
				try {
					double target = Double.parseDouble(targetVoltage.trim());
					double simulatedVoltage = target + (random.nextDouble() * 10 - 5); // +/- 5V simulation
					actualVoltage = String.format("%.2f", simulatedVoltage);
				} catch (NumberFormatException e) {
					actualVoltage = "230.0"; // Default simulated voltage
				}
				appendLog("SIMULATION: Read Voltage: " + actualVoltage, LogLevel.INFO);
			} else {
				if (modelPhase.equals("1")) {
					appendLog("Reading R phase Voltage", LogLevel.INFO);
					actualVoltage = TestPointUtils.readRPhaseVoltage();
				} else if (modelPhase.equals("3")) {
					appendLog("Reading 3 phase Voltage", LogLevel.INFO);
					actualVoltage = TestPointUtils.readRYVoltage();
				}
			}

			try {
				double actual = Double.parseDouble(actualVoltage.trim());
				double target = Double.parseDouble(targetVoltage.trim());
				if (Math.abs(actual - target) <= 5.0) {
					appendLog("Voltage : " + actual, LogLevel.INFO);
					appendLog("Voltage Verified", LogLevel.INFO);
					Platform.runLater(() -> testPoint.setProgress(0.3));
					voltageVerified = true;					
					break;
				} else {
					appendLog("Attempt " + attempt + ": Voltage mismatch. Got: " + actual, LogLevel.INFO);

				}
			} catch (NumberFormatException e) {
				appendLog("Attempt " + attempt + ": Invalid voltage format. Got: " + actualVoltage, LogLevel.INFO);
			}

			try {
				Thread.sleep(2000);
				appendLog("Sleep", LogLevel.INFO);
			} catch (InterruptedException e) {}
		}

		if (!voltageVerified || isStopped) {
			handleExecutionFailure(testPoint, "Failed to verify voltage after " + MAX_ATTEMPTS + " attempts.");
			return;
		}

		// Step 4: Wait setup duration
		appendLog("Waiting for Setup Time : " + setupTime, LogLevel.INFO);
		for (int i = 0; i < setupTime && !isStopped; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
			final double progress = 0.3 + (i + 1) * (0.4 / setupTime);
			Platform.runLater(() -> testPoint.setProgress(progress));
		}

		// Step 5: Loop measurement update until stop
		currentResult = new Result();
		currentResult.setFanSerialNumber(ref_txtNewFanSerialNo.getText());
		currentResult.setVoltage(targetVoltage);
		currentResult.setTestPointName(testPoint.getTestPointName());
		currentResult.setProjectRun(currentProjectRun); // Pass the current ProjectRun entity
		allValid = true; // Assume all passed initially
		// It works this way
		// Set false later if any one fails

		executeTestPoint(testPoint, isStepRun);

		fanSerialNumber = ref_txtNewFanSerialNo.getText();

		// Update results to DB
		updateResultMeasurements(
			    fanSerialNumber,
			    LocalDateTime.now(),
			    testPoint.getTestPointName(),
			    currentResult.getVoltage(),
			    currentResult.getRpm(),
			    currentResult.isRpmValid(),
			    currentResult.getWindSpeed(),
			    currentResult.isWindSpeedValid(),
			    currentResult.getVibration(),
			    currentResult.isVibrationValid(),
			    currentResult.getWatts(),
			    currentResult.isWattsValid(),
			    currentResult.getVa(),
			    currentResult.getCurrent(),
			    currentResult.isCurrentValid(),
			    currentResult.getPowerFactor(),
			    currentResult.isPowerFactorValid(),
			    currentResult.getTestStatus(),
			    currentResult.isActivePowerValid()
			);

		appendLog("Result Updated : Test point : " + testPoint.getTestPointName(), LogLevel.INFO);

		// Step 6: Clean-up
		Platform.runLater(() -> {
			testPoint.setIsRunning(false);
			if (!isStopped) testPoint.setStatus(ConstantStatus.COMPLETED);
			else testPoint.setStatus(ConstantStatus.STOPPED);
			tvTestSetup.refresh();
		});

		// Reset voltage only if not step run (optional logic)
		// This part is moved to runTestPointsSequentially after all tests are done
		/* if (!isStepRun && currentIndex >= testPoints.size() - 1) {
	        TestPointUtils.setVoltage("0");
	        appendLog("All test points completed. Voltage set to 0.", LogLevel.INFO);
	        setControlsDisabled(false);
	    } */	        

		// Step 7 : All test points completed — Mains OFF
		appendLog("All test points completed. Turning Mains OFF...", LogLevel.INFO);
		Platform.runLater(() -> testPoint.setProgress(1.0)); // Optional visual update

		if (!SIMULATION_MODE) {
		    TestPointUtils.mainsOff();
		    appendLog("Mains OFF executed (hardware). Voltage set to 0. Dimmer not set to min.", LogLevel.INFO);
		} else {
		    appendLog("SIMULATION: Mains OFF (no actual hardware call). Voltage set to 0. Dimmer not set to min.", LogLevel.INFO);
		}

		// Re-enable UI controls
		setControlsDisabled(false);

	}

	/**
	 * Retrieves the wind-speed reading count (as integer) from the configuration
	 * stored against the currently selected model in the ComboBox.
	 *
	 * @return the number of wind-speed readings the user must enter
	 */
	private int fetchWindSpeedConfigForCurrentModel() {
		String modelName = cmbBxModelName.getSelectionModel().getSelectedItem();
		DutMasterData model = DeviceDataManagerController.getDutMasterDataService().findByModelName(modelName);
		// assume getWindSpeedConfig() returns a String like "3"
		try {
			return Integer.parseInt(model.getWindSpeedConfig());
		} catch (NumberFormatException e) {
			// fallback to a default of 1 reading
			return 1;
		}
	}

	/**
	 * Opens a non-modal popup containing N labeled TextFields ("Reading 1", "Reading 2", …),
	 * a Save button and a Next/Confirm button. Once the user has entered all values and clicks Save,
	 * the average of those values is returned.
	 *
	 * @param count the number of individual wind-speed readings to collect
	 * @return the average of the entered wind-speed values
	 */
	private double promptForWindSpeedReadings(int count) {
		final FutureTask<Double> task = new FutureTask<>(() -> {
			ManualReadingsInputPopup popup = new ManualReadingsInputPopup(count, "Wind Speed Readings");
			return popup.showAndWaitAndReturnAverage(); // Should be blocking on FX thread
		});

		Platform.runLater(task); // Run UI interaction on FX thread

		try {
			return task.get(); // Wait for result from FX thread
		} catch (Exception e) {
			e.printStackTrace();
			return 0.0; // Or some other fallback/default
		}
	}

	/**
	 * Opens a non-modal popup containing N labeled TextFields ("Vibration Reading 1", "Vibration Reading 2", …),
	 * a Save button and a Next/Confirm button. Once the user has entered all values and clicks Save,
	 * the average of those values is returned.
	 *
	 * @param count the number of individual vibration readings to collect
	 * @return the average of the entered vibration values
	 */
	private double promptForVibrationReadings(int count) {
		final FutureTask<Double> task = new FutureTask<>(() -> {
			// Reusing WindSpeedPopup for simplicity. Ideally, you might create a dedicated
			// 'VibrationPopup' class in 'com.tasnetwork.calibration.energymeter.util;'
			// if you need different UI or validation logic for vibration.
			ManualReadingsInputPopup popup = new ManualReadingsInputPopup(count, "Vibration Readings");
			return popup.showAndWaitAndReturnAverage(); // Should be blocking on FX thread
		});

		Platform.runLater(task); // Run UI interaction on FX thread

		try {
			return task.get(); // Wait for result from FX thread
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("Failed to get vibration readings from popup.", e);
			return 0.0; // Or some other fallback/default
		}
	}

	/**
	 * Executes a single test point by performing measurements such as RPM, Current, Watts,
	 * Active Power, Power Factor, and Wind Speed. The execution path is controlled by the
	 * `isStepRun` flag. If true, the function loops with a delay between each set of measurements
	 * until stopped. If false, it executes the measurements once. Each measurement is validated
	 * against configured limits using `readValidateAndUpdate`. Wind speed is handled as a special
	 * averaged input through a popup UI. At the end of execution, the test result is marked as
	 * PASSED or FAILED based on all validation flags.
	 *
	 * @param testPoint       the current test point containing expected limits and setters
	 * @param isStepRun       boolean flag to control step-run looping mode
	 * @throws InterruptedException if the loop is interrupted during sleep
	 */
	private void executeTestPoint(FanTestSetup testPoint, boolean isStepRun) {
		Runnable runAllValidations = () -> {
			try {
				// Determine the correct data source (simulation or actual hardware) based on SIMULATION_MODE
				Supplier<String> rpmReadFunc = SIMULATION_MODE ? FanProjectExecuteController::generateRandomRpm : TestPointUtils::readRpm;
				Supplier<String> currentReadFunc = SIMULATION_MODE ? FanProjectExecuteController::generateRandomCurrent : TestPointUtils::readCurrent;
				Supplier<String> wattsReadFunc = SIMULATION_MODE ? FanProjectExecuteController::generateRandomWatts : TestPointUtils::readWatts;
				Supplier<String> activePowerReadFunc = SIMULATION_MODE ? FanProjectExecuteController::generateRandomActivePower : TestPointUtils::readActivePower;
				Supplier<String> powerFactorReadFunc = SIMULATION_MODE ? FanProjectExecuteController::generateRandomPowerFactor : TestPointUtils::readPowerFactor;

				readValidateAndUpdate(rpmReadFunc, FanTestSetup::getRpmLowerLimit, FanTestSetup::getRpmUpperLimit,
						FanTestSetup::setRpmActual, FanTestSetup::getRpmValid, testPoint, 0.7, "RPM");

				readValidateAndUpdate(currentReadFunc, FanTestSetup::getCurrentLowerLimit, FanTestSetup::getCurrentUpperLimit,
						FanTestSetup::setCurrentActual, FanTestSetup::getCurrentValid, testPoint, 0.75, "Current");

				readValidateAndUpdate(wattsReadFunc, FanTestSetup::getWattsLowerLimit, FanTestSetup::getWattsUpperLimit,
						FanTestSetup::setWattsActual, FanTestSetup::getWattsValid, testPoint, 0.80, "Watts");

				readValidateAndUpdate(activePowerReadFunc, FanTestSetup::getActivePowerLowerLimit, FanTestSetup::getActivePowerUpperLimit,
						FanTestSetup::setActivePowerActual, FanTestSetup::getActivePowerValid, testPoint, 0.85, "ActivePower");

				readValidateAndUpdate(powerFactorReadFunc, FanTestSetup::getPowerFactorLowerLimit, FanTestSetup::getPowerFactorUpperLimit,
						FanTestSetup::setPowerFactorActual, FanTestSetup::getPowerFactorValid, testPoint, 0.90, "PowerFactor");

				// Wind speed logic
				int windSpeedCount = fetchWindSpeedConfigForCurrentModel();
				double avgWindSpeed;
				if (SIMULATION_MODE) {
					//promptForWindSpeedReadings(windSpeedCount); // Simulating pop up
					avgWindSpeed = Double.parseDouble(generateRandomWindSpeed());
					appendLog("SIMULATION: Windspeed: " + String.format("%.1f", avgWindSpeed), LogLevel.INFO);
				} else {
					avgWindSpeed = promptForWindSpeedReadings(windSpeedCount);
				}
				Supplier<String> windSpeedSupplier = () -> String.valueOf(avgWindSpeed);

				readValidateAndUpdate(
						windSpeedSupplier,
						FanTestSetup::getWindSpeedLowerLimit,
						FanTestSetup::getWindSpeedUpperLimit,
						FanTestSetup::setWindSpeedActual,
						FanTestSetup::getWindSpeedValid,
						testPoint,
						1.0,
						"WindSpeed"
						);

				// Vibration Parameter Logic similar to Wind speed
				int vibrationCount = fetchWindSpeedConfigForCurrentModel();
				double avgVibration;
				if (SIMULATION_MODE) {
					//promptForVibrationReadings(vibrationCount); // Simulating pop up for vibration
					avgVibration = Double.parseDouble(generateRandomVibration());
					appendLog("SIMULATION: Vibration: " + String.format("%.1f", avgVibration), LogLevel.INFO);
				} else {
					avgVibration = promptForVibrationReadings(vibrationCount);
				}
				Supplier<String> vibrationSupplier = () -> String.valueOf(avgVibration);

				readValidateAndUpdate(
						vibrationSupplier,
						FanTestSetup::getVibrationLowerLimit, // Assumes this method exists
						FanTestSetup::getVibrationUpperLimit, // Assumes this method exists
						FanTestSetup::setVibrationActual,     // Assumes this method exists
						FanTestSetup::getVibrationValid,      // Assumes this method exists
						testPoint,
						1.0, // Adjust progress if other steps follow
						"Vibration"
						);

			} catch (InterruptedException e) {
				appendLog("Measurement loop interrupted.", LogLevel.INFO);
				Thread.currentThread().interrupt();
			}
		};

		if (isStepRun) {
			while (!isStopped) {
				runAllValidations.run();
				currentResult.setTestStatus(allValid ? ConstantStatus.PASSED : ConstantStatus.FAILED);

				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				}
			}
		} else {
			runAllValidations.run();
			currentResult.setTestStatus(allValid ? ConstantStatus.PASSED : ConstantStatus.FAILED);

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	/**
	 * Handles failures during execution.
	 * - Updates UI with error.
	 * - Shows alert with message.
	 * - Stops the execution safely.
	 */
	private void handleExecutionFailure(FanTestSetup testPoint, String errorMsg) {
		Platform.runLater(() -> {
			testPoint.setStatus("Failed");
			testPoint.setIsRunning(false);
			tvTestSetup.refresh();

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Execution Error");
			alert.setHeaderText(null);
			alert.setContentText(errorMsg);
			alert.showAndWait();
		});

		appendLog(errorMsg, LogLevel.INFO);
		ApplicationLauncher.logger.error(errorMsg);
		isRunning = false;
		isStopped = true;
	}

	/**
	 * Sends a voltage value using predefined DUT command logic.
	 * - Retrieves the command from the project settings.
	 * - Appends voltage and executes the command.
	 */
	public void setVoltage(String voltage) {

		String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
		String testCaseName = ConstantArduinoCommands.SET_VOLTAGE;
		Optional<DutCommand> dutCommandDataOpt = DeviceDataManagerController.getDutCommandService()
				.findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);

		if (dutCommandDataOpt.isPresent()) {
			DutCommand dutCommand = dutCommandDataOpt.get();
			DeviceDataManagerController.setDutCommandData(dutCommand);

			String appendData = voltage;
			boolean isDataAppend = true;
			dutCommandExecuteStart(appendData, isDataAppend);
		}

	}

	/**
	 * Automatically updates the fan serial number in the text field to the next sequential number.
	 * Assumes serial numbers are in the format "SN" followed by a number (e.g., "SN100").
	 */
	private void updateNextSerialNumber() {
		String currentSerialNumber = ref_txtNewFanSerialNo.getText();
		if (currentSerialNumber != null && currentSerialNumber.startsWith("SN") && currentSerialNumber.length() > 2) {
			try {
				// Extract the numeric part of the serial number
				String numericPartStr = currentSerialNumber.substring(2);
				int numericPart = Integer.parseInt(numericPartStr);

				// Increment the numeric part
				int nextNumericPart = numericPart + 1;

				// Format the new serial number back to "SN" + incremented number (with leading zeros if necessary)
				// This assumes a fixed width for the numeric part, or you can adjust formatting as needed.
				// For example, if "SN100" becomes "SN101", "SN001" becomes "SN002"
				String formatString = "SN%0" + numericPartStr.length() + "d";
				String newSerialNumber = String.format(formatString, nextNumericPart);

				// Set the new serial number to the text field on the JavaFX Application Thread
				Platform.runLater(() -> {
					ref_txtNewFanSerialNo.setText(newSerialNumber);
					appendLog("Fan serial number updated to: " + newSerialNumber, LogLevel.INFO);
				});

			} catch (NumberFormatException e) {
				appendLog("Failed to parse serial number for increment: " + currentSerialNumber + ". Error: " + e.getMessage(), LogLevel.ERROR);
			} catch (Exception e) {
				appendLog("An unexpected error occurred while updating serial number: " + e.getMessage(), LogLevel.ERROR);
			}
		} else {
			appendLog("Current serial number format is not 'SNXXX' or is empty. Cannot automatically increment: " + currentSerialNumber, LogLevel.DEBUG);
		}
	}

	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	public void saveToDbTask() {

		ApplicationLauncher.logger.debug("saveToDbTask: Entry");
		String selectedModel = ref_cmbBxModelName.getSelectionModel().getSelectedItem();

		DutMasterData selectedDut = DeviceDataManagerController.getDutMasterDataService()
				.findAll()
				.stream()
				.filter(d -> d.getModelName().equals(selectedModel))
				.findFirst()
				.orElse(null);

		if (selectedDut == null) {
			ApplicationLauncher.logger.warn("No DutMasterData found for model: " + selectedModel);
			return;
		}

		/*for (FanTestSetup setup : ref_tvTestSetup.getItems()) {
		    selectedDut.addFanTestSetup(setup);
		}*/

		// Clear old test setups
		selectedDut.getFanTestSetupList().clear();

		// Add updated ones
		for (FanTestSetup setup : ref_tvTestSetup.getItems()) {
			setup.setDutMasterData(selectedDut); // Important!
			selectedDut.getFanTestSetupList().add(setup);
		}

		DeviceDataManagerController.getDutMasterDataService().saveToDb(selectedDut);
		ApplicationLauncher.logger.info("Saved FanTestSetup list to model: " + selectedModel);
		ApplicationLauncher.InformUser("Setup Saved","Test Setup saved succesfully", AlertType.INFORMATION);
	}

	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	// FXML On Click =============================================================================================================================

	// =========================== Settings ====================================================
	@FXML
	private void openSettingsWindow() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/project/SettingsPanel.fxml"));
			Parent root = loader.load();

			Stage settingsStage = new Stage();
			settingsStage.setTitle("Settings");
			settingsStage.setScene(new Scene(root));
			settingsStage.initOwner(btnSettings.getScene().getWindow()); // tie to main window
			settingsStage.initModality(Modality.NONE); // non-blocking
			settingsStage.setResizable(false);
			settingsStage.setAlwaysOnTop(true);
			settingsStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	// =========================== Start & Stop ================================================

	@FXML
	void btnStartOnClick(ActionEvent event) {
		ApplicationLauncher.logger.info("btnStartOnClick: Entry");

		startTimer = new Timer();
		startTimer.schedule(new startOnTaskClick(), 50);
	}

	@FXML
	void btnStopOnClick(ActionEvent event) {
		ApplicationLauncher.logger.info("btnStopOnClick: Entry");

		isFirstTestPointInSequence = true;

		stopTimer = new Timer();
		stopTimer.schedule(new stopOnTaskClick(), 50);
	}

	// =========================== Dimmer & Voltage Controls ====================================

	@FXML 
	void btnDimmerForwardOnClick(ActionEvent event) {	
		ApplicationLauncher.logger.info("btnDimmerForwardOnClick: Entry");
		clickedButtonRef = (Button) event.getSource(); // store the reference to clicked button
		dimmerForwardTimer = new Timer();
		dimmerForwardTimer.schedule(new DimmerForwardOnTaskClick(), 50);
	}

	@FXML
	void btnDimmerReverseOnClick(ActionEvent event) {
		ApplicationLauncher.logger.info("btnDimmerReverseOnClick: Entry");
		clickedButtonRef = (Button) event.getSource(); // store the reference to clicked button
		dimmerReverseTimer = new Timer();
		dimmerReverseTimer.schedule(new DimmerReverseOnTaskClick(), 50);
	}

	@FXML
	void btnDimmerIsMinOnClick(ActionEvent event) {
		ApplicationLauncher.logger.info("btnDimmerIsMinOnClick: Entry");
		clickedButtonRef = (Button) event.getSource(); // store the reference to clicked button
		dimmerIsMinTimer = new Timer();
		dimmerIsMinTimer.schedule(new DimmerIsMinOnTaskClick(), 50);
	}

	@FXML
	void btnDimmerSetMinOnClick(ActionEvent event) {
		ApplicationLauncher.logger.info("btnDimmerSetMinOnClick: Entry");
		clickedButtonRef = (Button) event.getSource(); // store the reference to clicked button
		dimmerSetMinTimer = new Timer();
		dimmerSetMinTimer.schedule(new DimmerSetMinOnTaskClick(), 50);
	}

	// =========================== Voltage Buttons ===============================================

	@FXML
	void btnSetVoltageOnClick(ActionEvent event) {
		ApplicationLauncher.logger.info("btnSetVoltageOnClick: Entry");
		clickedButtonRef = (Button) event.getSource(); // store the reference to clicked button
		setVoltageTimer = new Timer();
		setVoltageTimer.schedule(new SetVoltageTask(), 50);
	}

	@FXML
	void btnTestVoltageOnClick(ActionEvent event) {
		ApplicationLauncher.logger.info("btnTestVoltageOnClick: Entry");
		clickedButtonRef = (Button) event.getSource(); // store the reference to clicked button
		testVoltageTimer = new Timer();
		testVoltageTimer.schedule(new TestVoltageTask(), 50);
	}

	@FXML
	void btnMaintainVoltageOnClick(ActionEvent event) {
		ApplicationLauncher.logger.info("btnMaintainVoltageOnClick: Entry");
		maintainVoltageTimer = new Timer();
		maintainVoltageTimer.schedule(new MaintainVoltageTask(), 50);
	}

	// =========================== R Phase Measurement Buttons ==================================
	@FXML
	void btnRPhaseCurrentOnClick(ActionEvent event) {
		ApplicationLauncher.logger.info("btnRPhaseCurrentOnClick: Entry");
		rPhaseCurrentTimer = new Timer();
		rPhaseCurrentTimer.schedule(new RPhaseCurrentTask(), 50);
	}

	@FXML
	void btnRPhasePFOnClick(ActionEvent event) {
		ApplicationLauncher.logger.info("btnRPhasePFOnClick: Entry");
		rPhasePFTimer = new Timer();
		rPhasePFTimer.schedule(new RPhasePFTask(), 50);
	}

	@FXML
	void btnRPhaseVAOnClick(ActionEvent event) {
		ApplicationLauncher.logger.info("btnRPhaseVAOnClick: Entry");
		rPhaseVATimer = new Timer();
		rPhaseVATimer.schedule(new RPhaseVATask(), 50);
	}

	@FXML
	void btnRPhaseVoltageOnClick(ActionEvent event) {
		ApplicationLauncher.logger.info("btnRPhaseVoltageOnClick: Entry");
		rPhaseVoltageTimer = new Timer();
		rPhaseVoltageTimer.schedule(new RPhaseVoltageTask(), 50);
	}

	@FXML
	void btnRPhaseWattsOnClick(ActionEvent event) {
		ApplicationLauncher.logger.info("btnRPhaseWattsOnClick: Entry");
		rPhaseWattsTimer = new Timer();
		rPhaseWattsTimer.schedule(new RPhaseWattsTask(), 50);
	}

	// =========================== Y Phase Measurement Buttons ==================================
	@FXML
	void btnYPhaseCurrentOnClick(ActionEvent event) {
		ApplicationLauncher.logger.info("btnYPhaseCurrentOnClick: Entry");
		yPhaseCurrentTimer = new Timer();
		yPhaseCurrentTimer.schedule(new YPhaseCurrentTask(), 50);
	}

	@FXML
	void btnYPhasePFOnClick(ActionEvent event) {
		ApplicationLauncher.logger.info("btnYPhasePFOnClick: Entry");
		yPhasePFTimer = new Timer();
		yPhasePFTimer.schedule(new YPhasePFTask(), 50);
	}

	@FXML
	void btnYPhaseVAOnClick(ActionEvent event) {
		ApplicationLauncher.logger.info("btnYPhaseVAOnClick: Entry");
		yPhaseVATimer = new Timer();
		yPhaseVATimer.schedule(new YPhaseVATask(), 50);
	}

	@FXML
	void btnYPhaseVoltageOnClick(ActionEvent event) {
		ApplicationLauncher.logger.info("btnYPhaseVoltageOnClick: Entry");
		yPhaseVoltageTimer = new Timer();
		yPhaseVoltageTimer.schedule(new YPhaseVoltageTask(), 50);
	}

	@FXML
	void btnYPhaseWattsOnClick(ActionEvent event) {
		ApplicationLauncher.logger.info("btnYPhaseWattsOnClick: Entry");
		yPhaseWattsTimer = new Timer();
		yPhaseWattsTimer.schedule(new YPhaseWattsTask(), 50);
	}

	// =========================== B Phase Measurement Buttons ==================================
	@FXML
	void btnBPhaseVoltageOnClick(ActionEvent event) {
		ApplicationLauncher.logger.info("btnBPhaseVoltageOnClick: Entry");
		bPhaseVoltageTimer = new Timer();
		bPhaseVoltageTimer.schedule(new BPhaseVoltageTask(), 50);
	}

	@FXML
	void btnBPhaseCurrentOnClick(ActionEvent event) {
		ApplicationLauncher.logger.info("btnBPhaseCurrentOnClick: Entry");
		bPhaseCurrentTimer = new Timer();
		bPhaseCurrentTimer.schedule(new BPhaseCurrentTask(), 50);
	}

	@FXML
	void btnBPhaseWattsOnClick(ActionEvent event) {
		ApplicationLauncher.logger.info("btnBPhaseWattsOnClick: Entry");
		bPhaseWattsTimer = new Timer();
		bPhaseWattsTimer.schedule(new BPhaseWattsTask(), 50);
	}

	@FXML
	void btnBPhaseVAOnClick(ActionEvent event) {
		ApplicationLauncher.logger.info("btnBPhaseVAOnClick: Entry");
		bPhaseVATimer = new Timer();
		bPhaseVATimer.schedule(new BPhaseVATask(), 50);
	}

	@FXML
	void btnBPhasePFOnClick(ActionEvent event) {
		ApplicationLauncher.logger.info("btnBPhasePFOnClick: Entry");
		bPhasePFTimer = new Timer();
		bPhasePFTimer.schedule(new BPhasePFTask(), 50);
	}	

	// =========================== 3-Phase Measurement Buttons ==================================
	@FXML
	void btn3PhaseCurrentAvgOnClick(ActionEvent event) {
		ApplicationLauncher.logger.info("btn3PhaseCurrentAvgOnClick: Entry");
		phase3CurrentAvgTimer = new Timer();
		phase3CurrentAvgTimer.schedule(new Phase3CurrentAvgTask(), 50);
	}

	@FXML
	void btn3PhasePFAvgOnClick(ActionEvent event) {
		ApplicationLauncher.logger.info("btn3PhasePFAvgOnClick: Entry");
		phase3PFAvgTimer = new Timer();
		phase3PFAvgTimer.schedule(new Phase3PFAvgTask(), 50);
	}

	@FXML
	void btn3PhaseVATotalOnClick(ActionEvent event) {
		ApplicationLauncher.logger.info("btn3PhaseVATotalOnClick: Entry");
		phase3VATotalTimer = new Timer();
		phase3VATotalTimer.schedule(new Phase3VATotalTask(), 50);
	}

	@FXML
	void btn3PhaseVBROnClick(ActionEvent event) {
		ApplicationLauncher.logger.info("btn3PhaseVBROnClick: Entry");
		phase3VBRTimer = new Timer();
		phase3VBRTimer.schedule(new Phase3VBRTask(), 50);
	}

	@FXML
	void btn3PhaseVLLOnClick(ActionEvent event) {
		ApplicationLauncher.logger.info("btn3PhaseVLLOnClick: Entry");
		phase3VLLTimer = new Timer();
		phase3VLLTimer.schedule(new Phase3VLLTask(), 50);
	}

	@FXML
	void btn3PhaseVLNOnClick(ActionEvent event) {
		ApplicationLauncher.logger.info("btn3PhaseVLNOnClick: Entry");
		phase3VLNTimer = new Timer();
		phase3VLNTimer.schedule(new Phase3VLNTask(), 50);
	}

	@FXML
	void btn3PhaseVRYOnChange(ActionEvent event) {
		ApplicationLauncher.logger.info("btn3PhaseVRYOnChange: Entry");
		phase3VRYTimer = new Timer();
		phase3VRYTimer.schedule(new Phase3VRYTask(), 50);
	}

	@FXML
	void btn3PhaseVYBOnClick(ActionEvent event) {
		ApplicationLauncher.logger.info("btn3PhaseVYBOnClick: Entry");
		phase3VYBTimer = new Timer();
		phase3VYBTimer.schedule(new Phase3VYBTask(), 50);
	}

	@FXML
	void btn3PhaseWattsTotalOnClick(ActionEvent event) {
		ApplicationLauncher.logger.info("btn3PhaseWattsTotalOnClick: Entry");
		phase3WattsTotalTimer = new Timer();
		phase3WattsTotalTimer.schedule(new Phase3WattsTotalTask(), 50);
	}


	// =========================== Fan Monitoring ===============================================
	@FXML
	void btnFanRpmOnClick(ActionEvent event) {
		ApplicationLauncher.logger.info("btnFanRpmOnClick: Entry");
		clickedButtonRef = (Button) event.getSource(); // store the reference to clicked button
		fanRpmTimer = new Timer();
		fanRpmTimer.schedule(new FanRpmTask(), 50);
	}

	@FXML
	void btnFanWindspeedOnClick(ActionEvent event) {
		ApplicationLauncher.logger.info("btnFanWindspeedOnClick: Entry");
		clickedButtonRef = (Button) event.getSource(); // store the reference to clicked button
		fanWindspeedTimer = new Timer();
		fanWindspeedTimer.schedule(new FanWindspeedTask(), 50);
	}


	// ========================== Mains Controls ===============================================

	@FXML
	void btnVoltageMainsOnOnClick(ActionEvent event) {
		ApplicationLauncher.logger.info("btnVoltageMainsOnOnClick: Entry");
		clickedButtonRef = (Button) event.getSource(); // store the reference to clicked button
		mainsOnTimer = new Timer();
		mainsOnTimer.schedule(new MainsOnTask(), 50);
	}

	@FXML
	void btnVoltageMainsOffOnClick(ActionEvent event) {
		ApplicationLauncher.logger.info("btnVoltageMainsOffOnClick: Entry");
		clickedButtonRef = (Button) event.getSource(); // store the reference to clicked button
		mainsOffTimer = new Timer();
		mainsOffTimer.schedule(new MainsOffTask(), 50);
	}

	// =========================== Utility Buttons ==============================================
	//@FXML void btnSaveOnClick(ActionEvent event) { }


	// On Click Tasks =============================================================================================================================

	class startOnTaskClick extends TimerTask {
		public void run() {
			startOnTask();
			startTimer.cancel();
		}
	}

	class stopOnTaskClick extends TimerTask {
		public void run() {
			stopOnTask();
			stopTimer.cancel();
		}
	}

	class DimmerForwardOnTaskClick extends TimerTask {
		public void run() {
			dimmerForwardOnTask();
			dimmerForwardTimer.cancel();
		}
	}

	class DimmerReverseOnTaskClick extends TimerTask {
		@Override
		public void run() {
			dimmerReverseOnTask();
			dimmerReverseTimer.cancel();
		}
	}

	class DimmerIsMinOnTaskClick extends TimerTask {
		@Override
		public void run() {
			dimmerIsMinOnTask();
			dimmerIsMinTimer.cancel();
		}
	}

	class DimmerSetMinOnTaskClick extends TimerTask {
		@Override
		public void run() {
			dimmerSetMinOnTask();
			dimmerSetMinTimer.cancel();
		}
	}

	class SetVoltageTask extends TimerTask {
		public void run() {
			setVoltageTask();
			setVoltageTimer.cancel();
		}
	}

	class TestVoltageTask extends TimerTask {
		public void run() {
			testVoltageTask();
			testVoltageTimer.cancel();
		}
	}

	class MaintainVoltageTask extends TimerTask {
		public void run() {
			maintainVoltageTask();
			maintainVoltageTimer.cancel();
		}
	}

	class RPhaseCurrentTask extends TimerTask {
		@Override public void run() {
			rPhaseCurrentTask();
			rPhaseCurrentTimer.cancel();
		}
	}

	class RPhasePFTask extends TimerTask {
		@Override public void run() {
			rPhasePFTask();
			rPhasePFTimer.cancel();
		}
	}

	class RPhaseVATask extends TimerTask {
		@Override public void run() {
			rPhaseVATask();
			rPhaseVATimer.cancel();
		}
	}

	class RPhaseVoltageTask extends TimerTask {
		@Override public void run() {
			rPhaseVoltageTask();
			rPhaseVoltageTimer.cancel();
		}
	}

	class RPhaseWattsTask extends TimerTask {
		@Override public void run() {
			rPhaseWattsTask();
			rPhaseWattsTimer.cancel();
		}
	}

	class YPhaseCurrentTask extends TimerTask {
		@Override public void run() { yPhaseCurrentTask(); yPhaseCurrentTimer.cancel(); }
	}

	class YPhasePFTask extends TimerTask {
		@Override public void run() { yPhasePFTask(); yPhasePFTimer.cancel(); }
	}

	class YPhaseVATask extends TimerTask {
		@Override public void run() { yPhaseVATask(); yPhaseVATimer.cancel(); }
	}

	class YPhaseVoltageTask extends TimerTask {
		@Override public void run() { yPhaseVoltageTask(); yPhaseVoltageTimer.cancel(); }
	}

	class YPhaseWattsTask extends TimerTask {
		@Override public void run() { yPhaseWattsTask(); yPhaseWattsTimer.cancel(); }
	}

	class BPhaseVoltageTask extends TimerTask {
		@Override public void run() { bPhaseVoltageTask(); bPhaseVoltageTimer.cancel(); }
	}

	class BPhaseCurrentTask extends TimerTask {
		@Override public void run() { bPhaseCurrentTask(); bPhaseCurrentTimer.cancel(); }
	}

	class BPhaseWattsTask extends TimerTask {
		@Override public void run() { bPhaseWattsTask(); bPhaseWattsTimer.cancel(); }
	}

	class BPhaseVATask extends TimerTask {
		@Override public void run() { bPhaseVATask(); bPhaseVATimer.cancel(); }
	}

	class BPhasePFTask extends TimerTask {
		@Override public void run() { bPhasePFTask(); bPhasePFTimer.cancel(); }
	}

	class Phase3CurrentAvgTask extends TimerTask {
		@Override public void run() { phase3CurrentAvgTask(); phase3CurrentAvgTimer.cancel(); }
	}
	class Phase3PFAvgTask extends TimerTask {
		@Override public void run() { phase3PFAvgTask(); phase3PFAvgTimer.cancel(); }
	}
	class Phase3VATotalTask extends TimerTask {
		@Override public void run() { phase3VATotalTask(); phase3VATotalTimer.cancel(); }
	}
	class Phase3VBRTask extends TimerTask {
		@Override public void run() { phase3VBRTask(); phase3VBRTimer.cancel(); }
	}
	class Phase3VLLTask extends TimerTask {
		@Override public void run() { phase3VLLTask(); phase3VLLTimer.cancel(); }
	}
	class Phase3VLNTask extends TimerTask {
		@Override public void run() { phase3VLNTask(); phase3VLNTimer.cancel(); }
	}
	class Phase3VRYTask extends TimerTask {
		@Override public void run() { phase3VRYTask(); phase3VRYTimer.cancel(); }
	}
	class Phase3VYBTask extends TimerTask {
		@Override public void run() { phase3VYBTask(); phase3VYBTimer.cancel(); }
	}
	class Phase3WattsTotalTask extends TimerTask {
		@Override public void run() { phase3WattsTotalTask(); phase3WattsTotalTimer.cancel(); }
	}

	class FanRpmTask extends TimerTask {
		@Override public void run() {
			fanRpmTask();
			fanRpmTimer.cancel();
		}
	}

	class FanWindspeedTask extends TimerTask {
		@Override public void run() {
			fanWindspeedTask();
			fanWindspeedTimer.cancel();
		}
	}

	class MainsOnTask extends TimerTask {
		@Override public void run() {
			ApplicationLauncher.logger.info("MainsOnTask: Entry");
			mainsOnTask();
			mainsOnTimer.cancel();
		}
	}

	class MainsOffTask extends TimerTask {
		@Override public void run() {
			mainsOffTask();
			mainsOffTimer.cancel();
		}
	}
	// Task =======================================================================================================================================

	public void startOnTask() {
		Platform.runLater(() -> {
			ref_btnStart.setDisable(true);
			ref_btnStop.setDisable(false);
		});

		String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
		String testCaseName = ConstantArduinoCommands.START;
		Optional<DutCommand> dutCommandDataOpt = DeviceDataManagerController.getDutCommandService()
				.findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);

		if (dutCommandDataOpt.isPresent()) {
			DutCommand dutCommand = dutCommandDataOpt.get();
			DeviceDataManagerController.setDutCommandData(dutCommand);

			boolean isDataAppend = false;
			// Only call hardware command if not in simulation mode
			if (!SIMULATION_MODE) {
				dutCommandExecuteStart("", isDataAppend);
			} else {
				appendLog("SIMULATION: Start command skipped.", LogLevel.INFO);
			}
		}
	}

	public void stopOnTask() {
		Platform.runLater(() -> {
			ref_btnStop.setDisable(true);
			ref_btnStart.setDisable(false);
		});

		String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
		String testCaseName = ConstantArduinoCommands.STOP;
		Optional<DutCommand> dutCommandDataOpt = DeviceDataManagerController.getDutCommandService()
				.findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);

		if (dutCommandDataOpt.isPresent()) {
			DutCommand dutCommand = dutCommandDataOpt.get();
			DeviceDataManagerController.setDutCommandData(dutCommand);

			boolean isDataAppend = false;
			// Only call hardware command if not in simulation mode
			if (!SIMULATION_MODE) {
				dutCommandExecuteStart("", isDataAppend);
			} else {
				appendLog("SIMULATION: Stop command skipped.", LogLevel.INFO);
			}
		}
	}

	public void dimmerForwardOnTask() {
		Platform.runLater(() -> {
			clickedButtonRef.setDisable(true);
		});

		String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
		String testCaseName = ConstantArduinoCommands.DIMMER_FORWARD_VOLT;
		Optional<DutCommand> dutCommandDataOpt = DeviceDataManagerController.getDutCommandService()
				.findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);

		if (dutCommandDataOpt.isPresent()) {
			DutCommand dutCommand = dutCommandDataOpt.get();
			DeviceDataManagerController.setDutCommandData(dutCommand);
			String appendData = "";

			if (clickedButtonRef == ref_btnDimmerForward) {
				appendData = ref_txtForwardInMsec.getText();
			} else if (clickedButtonRef == ref_btnDimmerForwardExecute) {
				appendData = ref_txtForwardInMsecExecute.getText();
			}

			boolean isDataAppend = true;
			// Only call hardware command if not in simulation mode
			if (!SIMULATION_MODE) {
				dutCommandExecuteStart(appendData, isDataAppend);
			} else {
				appendLog("SIMULATION: Dimmer Forward skipped (data: " + appendData + ").", LogLevel.INFO);
			}
		}

		Platform.runLater(() -> {
			clickedButtonRef.setDisable(false);
		});
	}

	public void dimmerReverseOnTask() {
		Platform.runLater(() -> {
			clickedButtonRef.setDisable(true);
		});

		String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
		String testCaseName = ConstantArduinoCommands.DIMMER_REVERSE_VOLT;
		Optional<DutCommand> dutCommandDataOpt = DeviceDataManagerController.getDutCommandService()
				.findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);

		if (dutCommandDataOpt.isPresent()) {
			DutCommand dutCommand = dutCommandDataOpt.get();
			DeviceDataManagerController.setDutCommandData(dutCommand);

			String appendData = "";

			if (clickedButtonRef == ref_btnDimmerReverse) {
				appendData = ref_txtReverseInMsec.getText();
			} else if (clickedButtonRef == ref_btnDimmerReverseExecute) {
				appendData = ref_txtReverseInMsecExecute.getText();
			}

			boolean isDataAppend = true;
			// Only call hardware command if not in simulation mode
			if (!SIMULATION_MODE) {
				dutCommandExecuteStart(appendData, isDataAppend);
			} else {
				appendLog("SIMULATION: Dimmer Reverse skipped (data: " + appendData + ").", LogLevel.INFO);
			}
		}

		Platform.runLater(() -> {
			clickedButtonRef.setDisable(false);
		});
	}

	public void dimmerIsMinOnTask() {
		Platform.runLater(() -> clickedButtonRef.setDisable(true));

		String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
		String testCaseName = ConstantArduinoCommands.DIMMER_IS_MIN;
		Optional<DutCommand> dutCommandDataOpt = DeviceDataManagerController.getDutCommandService()
				.findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);

		if (dutCommandDataOpt.isPresent()) {
			DutCommand dutCommand = dutCommandDataOpt.get();
			DeviceDataManagerController.setDutCommandData(dutCommand);

			boolean isDataAppend = false;
			boolean result;
			if (SIMULATION_MODE) {
				result = random.nextBoolean(); // Simulate random true/false
				appendLog("SIMULATION: Dimmer Is Min returned " + result, LogLevel.INFO);
			} else {
				result = dutCommandExecuteStart("", isDataAppend);
			}

			Platform.runLater(() -> {
				if (clickedButtonRef == ref_btnDimmerIsMin) {
					ref_btnDimmerIsMin.setStyle(result 
							? ConstantCSS.FX_BACKGROUND_COLOR + ConstantCSS.COLOR_GREEN_SEMI_COLON
									: ConstantCSS.FX_BACKGROUND_COLOR + ConstantCSS.COLOR_RED_SEMI_COLON);
				} else if (clickedButtonRef == ref_btnDimmerIsMinExecute) {
					ref_btnDimmerIsMinExecute.setStyle(result 
							? ConstantCSS.FX_BACKGROUND_COLOR + ConstantCSS.COLOR_GREEN_SEMI_COLON
									: ConstantCSS.FX_BACKGROUND_COLOR + ConstantCSS.COLOR_RED_SEMI_COLON);
				}
			});
		}

		Platform.runLater(() -> clickedButtonRef.setDisable(false));
	}


	public void dimmerSetMinOnTask() {
		Platform.runLater(() -> clickedButtonRef.setDisable(true));

		String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
		String testCaseName = ConstantArduinoCommands.DIMMER_SET_MIN;
		Optional<DutCommand> dutCommandDataOpt = DeviceDataManagerController.getDutCommandService()
				.findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);

		if (dutCommandDataOpt.isPresent()) {
			DutCommand dutCommand = dutCommandDataOpt.get();
			DeviceDataManagerController.setDutCommandData(dutCommand);

			boolean isDataAppend = false;
			boolean result;
			if (SIMULATION_MODE) {
				result = true; // Simulate success
				appendLog("SIMULATION: Dimmer Set Min successful.", LogLevel.INFO);
			} else {
				result = dutCommandExecuteStart("", isDataAppend);
			}

			Platform.runLater(() -> {
				if (clickedButtonRef == ref_btnDimmerIsMin) { // Note: this might be a copy-paste error, should be SetMin related buttons
					ref_btnDimmerIsMin.setStyle(result 
							? ConstantCSS.FX_BACKGROUND_COLOR + ConstantCSS.COLOR_GREEN_SEMI_COLON
									: ConstantCSS.FX_BACKGROUND_COLOR + ConstantCSS.COLOR_RED_SEMI_COLON);
				} else if (clickedButtonRef == ref_btnDimmerIsMinExecute) { // Same here
					ref_btnDimmerIsMinExecute.setStyle(result 
							? ConstantCSS.FX_BACKGROUND_COLOR + ConstantCSS.COLOR_GREEN_SEMI_COLON
									: ConstantCSS.FX_BACKGROUND_COLOR + ConstantCSS.COLOR_RED_SEMI_COLON);
				}
				// Potentially, add specific styles for btnDimmerSetMin and btnDimmerSetMinExecute if they exist
			});
		}

		Platform.runLater(() -> clickedButtonRef.setDisable(false));
	}


	public void setVoltageTask() {
		Platform.runLater(() -> clickedButtonRef.setDisable(true));

		String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
		String testCaseName = ConstantArduinoCommands.SET_VOLTAGE;
		Optional<DutCommand> dutCommandDataOpt = DeviceDataManagerController.getDutCommandService()
				.findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);

		if (dutCommandDataOpt.isPresent()) {
			DutCommand dutCommand = dutCommandDataOpt.get();
			DeviceDataManagerController.setDutCommandData(dutCommand);

			String appendData = "";

			if (clickedButtonRef == ref_btnSetVoltage) {
				appendData = ref_txtSetVoltage.getText();
			} else if (clickedButtonRef == ref_btnSetVoltageExecute) {
				appendData = ref_txtSetVoltageExecute.getText();
			}

			boolean isDataAppend = true;
			// Only call hardware command if not in simulation mode
			if (!SIMULATION_MODE) {
				dutCommandExecuteStart(appendData, isDataAppend);
			} else {
				appendLog("SIMULATION: Set Voltage skipped (data: " + appendData + ").", LogLevel.INFO);
			}
		}

		Platform.runLater(() -> clickedButtonRef.setDisable(false));
	}

	public void testVoltageTask() {
		Platform.runLater(() -> clickedButtonRef.setDisable(true));

		String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
		String testCaseName = ConstantArduinoCommands.TEST_VOLTAGE;
		Optional<DutCommand> dutCommandDataOpt = DeviceDataManagerController.getDutCommandService()
				.findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);

		if (dutCommandDataOpt.isPresent()) {
			DutCommand dutCommand = dutCommandDataOpt.get();
			DeviceDataManagerController.setDutCommandData(dutCommand);

			String appendData = "";

			if (clickedButtonRef == ref_btnTestVoltage) {
				appendData = ref_txtTestVoltage.getText();
			} else if (clickedButtonRef == ref_btnTestVoltageExecute) {
				appendData = ref_txtTestVoltageExecute.getText();
			}

			boolean isDataAppend = true;
			// Only call hardware command if not in simulation mode
			if (!SIMULATION_MODE) {
				dutCommandExecuteStart(appendData, isDataAppend);
			} else {
				appendLog("SIMULATION: Test Voltage skipped (data: " + appendData + ").", LogLevel.INFO);
			}
		}

		Platform.runLater(() -> clickedButtonRef.setDisable(false));
	}

	public void maintainVoltageTask() {
		Platform.runLater(() -> clickedButtonRef.setDisable(true));

		String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
		String testCaseName = ConstantArduinoCommands.MAINTAIN_VOLTAGE;
		Optional<DutCommand> dutCommandDataOpt = DeviceDataManagerController.getDutCommandService()
				.findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);

		if (dutCommandDataOpt.isPresent()) {
			DutCommand dutCommand = dutCommandDataOpt.get();
			DeviceDataManagerController.setDutCommandData(dutCommand);

			boolean isDataAppend = false;
			// Only call hardware command if not in simulation mode
			if (!SIMULATION_MODE) {
				dutCommandExecuteStart("", isDataAppend);
			} else {
				appendLog("SIMULATION: Maintain Voltage skipped.", LogLevel.INFO);
			}
		}

		Platform.runLater(() -> clickedButtonRef.setDisable(false));
	}

	public void rPhaseVoltageTask() {
		Platform.runLater(() -> ref_btnRPhaseVoltage.setDisable(true));

		String value;
		if (SIMULATION_MODE) {
			value = generateRandomVoltage(); // Simulate a random voltage
			appendLog("SIMULATION: R Phase Voltage: " + value, LogLevel.INFO);
		} else {
			String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
			String testCaseName = ConstantArduinoCommands.R_PHASE_VOLTAGE;
			Optional<DutCommand> opt = DeviceDataManagerController
					.getDutCommandService()
					.findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);

			if (opt.isPresent()) {
				DeviceDataManagerController.setDutCommandData(opt.get());

				Map<String, Object> resp = new DutSerialDataManager()
						.dutExecuteCommand(ConstantAppConfig.DUT_COMMAND_INTERFACE_ID, "", false);

				boolean status = resp.get("status") instanceof Boolean && (Boolean) resp.get("status");
				value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;
			} else {
				value = ConstantArduinoCommands.CMD_FAILED;
			}
		}

		Platform.runLater(() -> ref_txtRPhaseVoltage.setText(value));
		Platform.runLater(() -> ref_btnRPhaseVoltage.setDisable(false));
	}

	public void rPhaseCurrentTask() {
		Platform.runLater(() -> ref_btnRPhaseCurrent.setDisable(true));

		String value;
		if (SIMULATION_MODE) {
			value = generateRandomCurrent(); // Simulate random current
			appendLog("SIMULATION: R Phase Current: " + value, LogLevel.INFO);
		} else {
			String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
			String testCaseName = ConstantArduinoCommands.R_PHASE_CURRENT;
			Optional<DutCommand> opt = DeviceDataManagerController
					.getDutCommandService()
					.findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);

			if (opt.isPresent()) {
				DeviceDataManagerController.setDutCommandData(opt.get());

				Map<String, Object> resp = new DutSerialDataManager()
						.dutExecuteCommand(ConstantAppConfig.DUT_COMMAND_INTERFACE_ID, "", false);

				boolean status = resp.get("status") instanceof Boolean && (Boolean) resp.get("status");
				value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;
			} else {
				value = ConstantArduinoCommands.CMD_FAILED;
			}
		}

		Platform.runLater(() -> ref_txtRPhaseCurrent.setText(value));
		Platform.runLater(() -> ref_btnRPhaseCurrent.setDisable(false));
	}

	public void rPhaseWattsTask() {
		Platform.runLater(() -> ref_btnRPhaseWatts.setDisable(true));

		String value;
		if (SIMULATION_MODE) {
			value = generateRandomWatts(); // Simulate random watts
			appendLog("SIMULATION: R Phase Watts: " + value, LogLevel.INFO);
		} else {
			String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
			String testCaseName = ConstantArduinoCommands.R_PHASE_WATTS;
			Optional<DutCommand> opt = DeviceDataManagerController
					.getDutCommandService()
					.findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);

			if (opt.isPresent()) {
				DeviceDataManagerController.setDutCommandData(opt.get());

				Map<String, Object> resp = new DutSerialDataManager()
						.dutExecuteCommand(ConstantAppConfig.DUT_COMMAND_INTERFACE_ID, "", false);

				boolean status = resp.get("status") instanceof Boolean && (Boolean) resp.get("status");
				value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;
			} else {
				value = ConstantArduinoCommands.CMD_FAILED;
			}
		}

		Platform.runLater(() -> ref_txtRPhaseWatts.setText(value));
		Platform.runLater(() -> ref_btnRPhaseWatts.setDisable(false));
	}

	public void rPhaseVATask() {
		Platform.runLater(() -> ref_btnRPhaseVA.setDisable(true));

		String value;
		if (SIMULATION_MODE) {
			value = generateRandomActivePower(); // Simulate random VA
			appendLog("SIMULATION: R Phase VA: " + value, LogLevel.INFO);
		} else {
			String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
			String testCaseName = ConstantArduinoCommands.R_PHASE_VA;
			Optional<DutCommand> opt = DeviceDataManagerController
					.getDutCommandService()
					.findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);

			if (opt.isPresent()) {
				DeviceDataManagerController.setDutCommandData(opt.get());

				Map<String, Object> resp = new DutSerialDataManager()
						.dutExecuteCommand(ConstantAppConfig.DUT_COMMAND_INTERFACE_ID, "", false);

				boolean status = resp.get("status") instanceof Boolean && (Boolean) resp.get("status");
				value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;
			} else {
				value = ConstantArduinoCommands.CMD_FAILED;
			}
		}

		Platform.runLater(() -> ref_txtRPhaseVA.setText(value));
		Platform.runLater(() -> ref_btnRPhaseVA.setDisable(false));
	}

	public void rPhasePFTask() {
		Platform.runLater(() -> ref_btnRPhasePF.setDisable(true));

		String value;
		if (SIMULATION_MODE) {
			value = generateRandomPowerFactor(); // Simulate random power factor
			appendLog("SIMULATION: R Phase PF: " + value, LogLevel.INFO);
		} else {
			String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
			String testCaseName = ConstantArduinoCommands.R_PHASE_PF;
			Optional<DutCommand> opt = DeviceDataManagerController
					.getDutCommandService()
					.findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);

			if (opt.isPresent()) {
				DeviceDataManagerController.setDutCommandData(opt.get());

				Map<String, Object> resp = new DutSerialDataManager()
						.dutExecuteCommand(ConstantAppConfig.DUT_COMMAND_INTERFACE_ID, "", false);

				boolean status = resp.get("status") instanceof Boolean && (Boolean) resp.get("status");
				value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;
			} else {
				value = ConstantArduinoCommands.CMD_FAILED;
			}
		}

		Platform.runLater(() -> ref_txtRPhasePF.setText(value));
		Platform.runLater(() -> ref_btnRPhasePF.setDisable(false));
	}

	public void yPhaseVoltageTask() {
		Platform.runLater(() -> ref_btnYPhaseVoltage.setDisable(true));

		String value;
		if (SIMULATION_MODE) {
			value = generateRandomVoltage(); // Simulate random voltage
			appendLog("SIMULATION: Y Phase Voltage: " + value, LogLevel.INFO);
		} else {
			String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
			String testCaseName = ConstantArduinoCommands.Y_PHASE_VOLTAGE;
			Optional<DutCommand> opt = DeviceDataManagerController
					.getDutCommandService()
					.findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);

			if (opt.isPresent()) {
				DeviceDataManagerController.setDutCommandData(opt.get());

				Map<String, Object> resp = new DutSerialDataManager()
						.dutExecuteCommand(ConstantAppConfig.DUT_COMMAND_INTERFACE_ID, "", false);

				boolean status = resp.get("status") instanceof Boolean && (Boolean) resp.get("status");
				value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;
			} else {
				value = ConstantArduinoCommands.CMD_FAILED;
			}
		}

		Platform.runLater(() -> ref_txtYPhaseVoltage.setText(value));
		Platform.runLater(() -> ref_btnYPhaseVoltage.setDisable(false));
	}

	public void yPhaseCurrentTask() {
		Platform.runLater(() -> ref_btnYPhaseCurrent.setDisable(true));

		String value;
		if (SIMULATION_MODE) {
			value = generateRandomCurrent(); // Simulate random current
			appendLog("SIMULATION: Y Phase Current: " + value, LogLevel.INFO);
		} else {
			String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
			String testCaseName = ConstantArduinoCommands.Y_PHASE_CURRENT;
			Optional<DutCommand> opt = DeviceDataManagerController
					.getDutCommandService()
					.findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);

			if (opt.isPresent()) {
				DeviceDataManagerController.setDutCommandData(opt.get());

				Map<String, Object> resp = new DutSerialDataManager()
						.dutExecuteCommand(ConstantAppConfig.DUT_COMMAND_INTERFACE_ID, "", false);

				boolean status = resp.get("status") instanceof Boolean && (Boolean) resp.get("status");
				value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;
			} else {
				value = ConstantArduinoCommands.CMD_FAILED;
			}
		}

		Platform.runLater(() -> ref_txtYPhaseCurrent.setText(value));
		Platform.runLater(() -> ref_btnYPhaseCurrent.setDisable(false));
	}

	public void yPhaseWattsTask() {
		Platform.runLater(() -> ref_btnYPhaseWatts.setDisable(true));

		String value;
		if (SIMULATION_MODE) {
			value = generateRandomWatts(); // Simulate random watts
			appendLog("SIMULATION: Y Phase Watts: " + value, LogLevel.INFO);
		} else {
			String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
			String testCaseName = ConstantArduinoCommands.Y_PHASE_WATTS;
			Optional<DutCommand> opt = DeviceDataManagerController
					.getDutCommandService()
					.findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);

			if (opt.isPresent()) {
				DeviceDataManagerController.setDutCommandData(opt.get());

				Map<String, Object> resp = new DutSerialDataManager()
						.dutExecuteCommand(ConstantAppConfig.DUT_COMMAND_INTERFACE_ID, "", false);

				boolean status = resp.get("status") instanceof Boolean && (Boolean) resp.get("status");
				value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;
			} else {
				value = ConstantArduinoCommands.CMD_FAILED;
			}
		}

		Platform.runLater(() -> ref_txtYPhaseWatts.setText(value));
		Platform.runLater(() -> ref_btnYPhaseWatts.setDisable(false));
	}

	public void yPhaseVATask() {
		Platform.runLater(() -> ref_btnYPhaseVA.setDisable(true));

		String value;
		if (SIMULATION_MODE) {
			value = generateRandomActivePower(); // Simulate random VA
			appendLog("SIMULATION: Y Phase VA: " + value, LogLevel.INFO);
		} else {
			String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
			String testCaseName = ConstantArduinoCommands.Y_PHASE_VA;
			Optional<DutCommand> opt = DeviceDataManagerController
					.getDutCommandService()
					.findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);

			if (opt.isPresent()) {
				DeviceDataManagerController.setDutCommandData(opt.get());

				Map<String, Object> resp = new DutSerialDataManager()
						.dutExecuteCommand(ConstantAppConfig.DUT_COMMAND_INTERFACE_ID, "", false);

				boolean status = resp.get("status") instanceof Boolean && (Boolean) resp.get("status");
				value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;
			} else {
				value = ConstantArduinoCommands.CMD_FAILED;
			}
		}

		Platform.runLater(() -> ref_txtYPhaseVA.setText(value));
		Platform.runLater(() -> ref_btnYPhaseVA.setDisable(false));
	}

	public void yPhasePFTask() {
		Platform.runLater(() -> ref_btnYPhasePF.setDisable(true));

		String value;
		if (SIMULATION_MODE) {
			value = generateRandomPowerFactor(); // Simulate random power factor
			appendLog("SIMULATION: Y Phase PF: " + value, LogLevel.INFO);
		} else {
			String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
			String testCaseName = ConstantArduinoCommands.Y_PHASE_PF;
			Optional<DutCommand> opt = DeviceDataManagerController
					.getDutCommandService()
					.findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);

			if (opt.isPresent()) {
				DeviceDataManagerController.setDutCommandData(opt.get());

				Map<String, Object> resp = new DutSerialDataManager()
						.dutExecuteCommand(ConstantAppConfig.DUT_COMMAND_INTERFACE_ID, "", false);

				boolean status = resp.get("status") instanceof Boolean && (Boolean) resp.get("status");
				value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;
			} else {
				value = ConstantArduinoCommands.CMD_FAILED;
			}
		}

		Platform.runLater(() -> ref_txtYPhasePF.setText(value));
		Platform.runLater(() -> ref_btnYPhasePF.setDisable(false));
	}

	public void bPhaseVoltageTask() {
		Platform.runLater(() -> ref_btnBPhaseVoltage.setDisable(true));

		String value;
		if (SIMULATION_MODE) {
			value = generateRandomVoltage(); // Simulate random voltage
			appendLog("SIMULATION: B Phase Voltage: " + value, LogLevel.INFO);
		} else {
			String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
			String testCaseName = ConstantArduinoCommands.B_PHASE_VOLTAGE;
			Optional<DutCommand> opt = DeviceDataManagerController
					.getDutCommandService()
					.findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);

			if (opt.isPresent()) {
				DeviceDataManagerController.setDutCommandData(opt.get());

				Map<String, Object> resp = new DutSerialDataManager()
						.dutExecuteCommand(ConstantAppConfig.DUT_COMMAND_INTERFACE_ID, "", false);

				boolean status = resp.get("status") instanceof Boolean && (Boolean) resp.get("status");
				value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;
			} else {
				value = ConstantArduinoCommands.CMD_FAILED;
			}
		}

		Platform.runLater(() -> ref_txtBPhaseVoltage.setText(value));
		Platform.runLater(() -> ref_btnBPhaseVoltage.setDisable(false));
	}

	public void bPhaseCurrentTask() {
		Platform.runLater(() -> ref_btnBPhaseCurrent.setDisable(true));

		String value;
		if (SIMULATION_MODE) {
			value = generateRandomCurrent(); // Simulate random current
			appendLog("SIMULATION: B Phase Current: " + value, LogLevel.INFO);
		} else {
			String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
			String testCaseName = ConstantArduinoCommands.B_PHASE_CURRENT;
			Optional<DutCommand> opt = DeviceDataManagerController
					.getDutCommandService()
					.findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);

			if (opt.isPresent()) {
				DeviceDataManagerController.setDutCommandData(opt.get());

				Map<String, Object> resp = new DutSerialDataManager()
						.dutExecuteCommand(ConstantAppConfig.DUT_COMMAND_INTERFACE_ID, "", false);

				boolean status = resp.get("status") instanceof Boolean && (Boolean) resp.get("status");
				value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;
			} else {
				value = ConstantArduinoCommands.CMD_FAILED;
			}
		}

		Platform.runLater(() -> ref_txtBPhaseCurrent.setText(value));
		Platform.runLater(() -> ref_btnBPhaseCurrent.setDisable(false));
	}

	public void bPhaseWattsTask() {
		Platform.runLater(() -> ref_btnBPhaseWatts.setDisable(true));

		String value;
		if (SIMULATION_MODE) {
			value = generateRandomWatts(); // Simulate random watts
			appendLog("SIMULATION: B Phase Watts: " + value, LogLevel.INFO);
		} else {
			String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
			String testCaseName = ConstantArduinoCommands.B_PHASE_WATTS;
			Optional<DutCommand> opt = DeviceDataManagerController
					.getDutCommandService()
					.findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);

			if (opt.isPresent()) {
				DeviceDataManagerController.setDutCommandData(opt.get());

				Map<String, Object> resp = new DutSerialDataManager()
						.dutExecuteCommand(ConstantAppConfig.DUT_COMMAND_INTERFACE_ID, "", false);

				boolean status = resp.get("status") instanceof Boolean && (Boolean) resp.get("status");
				value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;
			} else {
				value = ConstantArduinoCommands.CMD_FAILED;
			}
		}

		Platform.runLater(() -> ref_txtBPhaseWatts.setText(value));
		Platform.runLater(() -> ref_btnBPhaseWatts.setDisable(false));
	}

	public void bPhaseVATask() {
		Platform.runLater(() -> ref_btnBPhaseVA.setDisable(true));

		String value;
		if (SIMULATION_MODE) {
			value = generateRandomActivePower(); // Simulate random VA
			appendLog("SIMULATION: B Phase VA: " + value, LogLevel.INFO);
		} else {
			String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
			String testCaseName = ConstantArduinoCommands.B_PHASE_VA;
			Optional<DutCommand> opt = DeviceDataManagerController
					.getDutCommandService()
					.findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);

			if (opt.isPresent()) {
				DeviceDataManagerController.setDutCommandData(opt.get());

				Map<String, Object> resp = new DutSerialDataManager()
						.dutExecuteCommand(ConstantAppConfig.DUT_COMMAND_INTERFACE_ID, "", false);

				boolean status = resp.get("status") instanceof Boolean && (Boolean) resp.get("status");
				value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;
			} else {
				value = ConstantArduinoCommands.CMD_FAILED;
			}
		}

		Platform.runLater(() -> ref_txtBPhaseVA.setText(value));
		Platform.runLater(() -> ref_btnBPhaseVA.setDisable(false));
	}

	public void bPhasePFTask() {
		Platform.runLater(() -> ref_btnBPhasePF.setDisable(true));

		String value;
		if (SIMULATION_MODE) {
			value = generateRandomPowerFactor(); // Simulate random power factor
			appendLog("SIMULATION: B Phase PF: " + value, LogLevel.INFO);
		} else {
			String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
			String testCaseName = ConstantArduinoCommands.B_PHASE_PF;
			Optional<DutCommand> opt = DeviceDataManagerController
					.getDutCommandService()
					.findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);

			if (opt.isPresent()) {
				DeviceDataManagerController.setDutCommandData(opt.get());

				Map<String, Object> resp = new DutSerialDataManager()
						.dutExecuteCommand(ConstantAppConfig.DUT_COMMAND_INTERFACE_ID, "", false);

				boolean status = resp.get("status") instanceof Boolean && (Boolean) resp.get("status");
				value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;
			} else {
				value = ConstantArduinoCommands.CMD_FAILED;
			}
		}

		Platform.runLater(() -> ref_txtBPhasePF.setText(value));
		Platform.runLater(() -> ref_btnBPhasePF.setDisable(false));
	}

	public void phase3CurrentAvgTask() {
		Platform.runLater(() -> ref_btn3PhaseCurrentAvg.setDisable(true));

		String value;
		if (SIMULATION_MODE) {
			value = generateRandomCurrent(); // Simulate random current
			appendLog("SIMULATION: 3 Phase Current Avg: " + value, LogLevel.INFO);
		} else {
			String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
			String testCaseName = ConstantArduinoCommands.PHASE_3_CURRENT_AVG;
			Optional<DutCommand> opt = DeviceDataManagerController
					.getDutCommandService()
					.findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);

			if (opt.isPresent()) {
				DeviceDataManagerController.setDutCommandData(opt.get());

				Map<String, Object> resp = new DutSerialDataManager()
						.dutExecuteCommand(ConstantAppConfig.DUT_COMMAND_INTERFACE_ID, "", false);

				boolean status = resp.get("status") instanceof Boolean && (Boolean) resp.get("status");
				value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;
			} else {
				value = ConstantArduinoCommands.CMD_FAILED;
			}
		}

		Platform.runLater(() -> ref_txt3PhaseCurrentAvg.setText(value));
		Platform.runLater(() -> ref_btn3PhaseCurrentAvg.setDisable(false));
	}

	public void phase3PFAvgTask() {
		Platform.runLater(() -> ref_btn3PhasePFAvg.setDisable(true));

		String value;
		if (SIMULATION_MODE) {
			value = generateRandomPowerFactor(); // Simulate random power factor
			appendLog("SIMULATION: 3 Phase PF Avg: " + value, LogLevel.INFO);
		} else {
			String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
			String testCaseName = ConstantArduinoCommands.PHASE_3_PF_AVG;
			Optional<DutCommand> opt = DeviceDataManagerController
					.getDutCommandService()
					.findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);

			if (opt.isPresent()) {
				DeviceDataManagerController.setDutCommandData(opt.get());

				Map<String, Object> resp = new DutSerialDataManager()
						.dutExecuteCommand(ConstantAppConfig.DUT_COMMAND_INTERFACE_ID, "", false);

				boolean status = resp.get("status") instanceof Boolean && (Boolean) resp.get("status");
				value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;
			} else {
				value = ConstantArduinoCommands.CMD_FAILED;
			}
		}

		Platform.runLater(() -> ref_txt3PhasePFAvg.setText(value));
		Platform.runLater(() -> ref_btn3PhasePFAvg.setDisable(false));
	}

	public void phase3VATotalTask() {
		Platform.runLater(() -> ref_btn3PhaseVATotal.setDisable(true));

		String value;
		if (SIMULATION_MODE) {
			value = generateRandomActivePower(); // Simulate random VA
			appendLog("SIMULATION: 3 Phase VA Total: " + value, LogLevel.INFO);
		} else {
			String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
			String testCaseName = ConstantArduinoCommands.PHASE_3_VA_TOTAL;
			Optional<DutCommand> opt = DeviceDataManagerController
					.getDutCommandService()
					.findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);

			if (opt.isPresent()) {
				DeviceDataManagerController.setDutCommandData(opt.get());

				Map<String, Object> resp = new DutSerialDataManager()
						.dutExecuteCommand(ConstantAppConfig.DUT_COMMAND_INTERFACE_ID, "", false);

				boolean status = resp.get("status") instanceof Boolean && (Boolean) resp.get("status");
				value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;
			} else {
				value = ConstantArduinoCommands.CMD_FAILED;
			}
		}

		Platform.runLater(() -> ref_txt3PhaseVATotal.setText(value));
		Platform.runLater(() -> ref_btn3PhaseVATotal.setDisable(false));
	}

	public void phase3VBRTask() {
		Platform.runLater(() -> ref_btn3PhaseVBR.setDisable(true));

		String value;
		if (SIMULATION_MODE) {
			value = generateRandomVoltage(); // Simulate random voltage
			appendLog("SIMULATION: 3 Phase VBR: " + value, LogLevel.INFO);
		} else {
			String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
			String testCaseName = ConstantArduinoCommands.PHASE_3_VBR;
			Optional<DutCommand> opt = DeviceDataManagerController
					.getDutCommandService()
					.findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);

			if (opt.isPresent()) {
				DeviceDataManagerController.setDutCommandData(opt.get());

				Map<String, Object> resp = new DutSerialDataManager()
						.dutExecuteCommand(ConstantAppConfig.DUT_COMMAND_INTERFACE_ID, "", false);

				boolean status = resp.get("status") instanceof Boolean && (Boolean) resp.get("status");
				value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;
			} else {
				value = ConstantArduinoCommands.CMD_FAILED;
			}
		}

		Platform.runLater(() -> ref_txt3PhaseVBR.setText(value));
		Platform.runLater(() -> ref_btn3PhaseVBR.setDisable(false));
	}

	public void phase3VLLTask() {
		Platform.runLater(() -> ref_btn3PhaseVLL.setDisable(true));

		String value;
		if (SIMULATION_MODE) {
			value = generateRandomVoltage(); // Simulate random voltage
			appendLog("SIMULATION: 3 Phase VLL: " + value, LogLevel.INFO);
		} else {
			String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
			String testCaseName = ConstantArduinoCommands.PHASE_3_VLL;
			Optional<DutCommand> opt = DeviceDataManagerController
					.getDutCommandService()
					.findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);

			if (opt.isPresent()) {
				DeviceDataManagerController.setDutCommandData(opt.get());

				Map<String, Object> resp = new DutSerialDataManager()
						.dutExecuteCommand(ConstantAppConfig.DUT_COMMAND_INTERFACE_ID, "", false);

				boolean status = resp.get("status") instanceof Boolean && (Boolean) resp.get("status");
				value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;
			} else {
				value = ConstantArduinoCommands.CMD_FAILED;
			}
		}

		Platform.runLater(() -> ref_txt3PhaseVLL.setText(value));
		Platform.runLater(() -> ref_btn3PhaseVLL.setDisable(false));
	}

	public void phase3VLNTask() {
		Platform.runLater(() -> ref_btn3PhaseVLN.setDisable(true));

		String value;
		if (SIMULATION_MODE) {
			value = generateRandomVoltage(); // Simulate random voltage
			appendLog("SIMULATION: 3 Phase VLN: " + value, LogLevel.INFO);
		} else {
			String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
			String testCaseName = ConstantArduinoCommands.PHASE_3_VLN;
			Optional<DutCommand> opt = DeviceDataManagerController
					.getDutCommandService()
					.findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);

			if (opt.isPresent()) {
				DeviceDataManagerController.setDutCommandData(opt.get());

				Map<String, Object> resp = new DutSerialDataManager()
						.dutExecuteCommand(ConstantAppConfig.DUT_COMMAND_INTERFACE_ID, "", false);

				boolean status = resp.get("status") instanceof Boolean && (Boolean) resp.get("status");
				value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;
			} else {
				value = ConstantArduinoCommands.CMD_FAILED;
			}
		}

		Platform.runLater(() -> ref_txt3PhaseVLN.setText(value));
		Platform.runLater(() -> ref_btn3PhaseVLN.setDisable(false));
	}

	public void phase3VRYTask() {
		Platform.runLater(() -> ref_btn3PhaseVRY.setDisable(true));

		String value;
		if (SIMULATION_MODE) {
			value = generateRandomVoltage(); // Simulate random voltage
			appendLog("SIMULATION: 3 Phase VRY: " + value, LogLevel.INFO);
		} else {
			String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
			String testCaseName = ConstantArduinoCommands.PHASE_3_VRY;
			Optional<DutCommand> opt = DeviceDataManagerController
					.getDutCommandService()
					.findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);

			if (opt.isPresent()) {
				DeviceDataManagerController.setDutCommandData(opt.get());

				Map<String, Object> resp = new DutSerialDataManager()
						.dutExecuteCommand(ConstantAppConfig.DUT_COMMAND_INTERFACE_ID, "", false);

				boolean status = resp.get("status") instanceof Boolean && (Boolean) resp.get("status");
				value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;
			} else {
				value = ConstantArduinoCommands.CMD_FAILED;
			}
		}

		Platform.runLater(() -> ref_txt3PhaseVRY.setText(value));
		Platform.runLater(() -> ref_btn3PhaseVRY.setDisable(false));
	}

	public void phase3VYBTask() {
		Platform.runLater(() -> ref_btn3PhaseVYB.setDisable(true));

		String value;
		if (SIMULATION_MODE) {
			value = generateRandomVoltage(); // Simulate random voltage
			appendLog("SIMULATION: 3 Phase VYB: " + value, LogLevel.INFO);
		} else {
			String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
			String testCaseName = ConstantArduinoCommands.PHASE_3_VYB;
			Optional<DutCommand> opt = DeviceDataManagerController
					.getDutCommandService()
					.findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);

			if (opt.isPresent()) {
				DeviceDataManagerController.setDutCommandData(opt.get());

				Map<String, Object> resp = new DutSerialDataManager()
						.dutExecuteCommand(ConstantAppConfig.DUT_COMMAND_INTERFACE_ID, "", false);

				boolean status = resp.get("status") instanceof Boolean && (Boolean) resp.get("status");
				value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;
			} else {
				value = ConstantArduinoCommands.CMD_FAILED;
			}
		}

		Platform.runLater(() -> ref_txt3PhaseVYB.setText(value));
		Platform.runLater(() -> ref_btn3PhaseVYB.setDisable(false));
	}

	public void phase3WattsTotalTask() {
		Platform.runLater(() -> ref_btn3PhaseWattsTotal.setDisable(true));

		String value;
		if (SIMULATION_MODE) {
			value = generateRandomWatts(); // Simulate random watts
			appendLog("SIMULATION: 3 Phase Watts Total: " + value, LogLevel.INFO);
		} else {
			String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
			String testCaseName = ConstantArduinoCommands.PHASE_3_WATTS_TOTAL;
			Optional<DutCommand> opt = DeviceDataManagerController
					.getDutCommandService()
					.findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);

			if (opt.isPresent()) {
				DeviceDataManagerController.setDutCommandData(opt.get());

				Map<String, Object> resp = new DutSerialDataManager()
						.dutExecuteCommand(ConstantAppConfig.DUT_COMMAND_INTERFACE_ID, "", false);

				boolean status = resp.get("status") instanceof Boolean && (Boolean) resp.get("status");
				value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;
			} else {
				value = ConstantArduinoCommands.CMD_FAILED;
			}
		}

		Platform.runLater(() -> ref_txt3PhaseWattsTotal.setText(value));
		Platform.runLater(() -> ref_btn3PhaseWattsTotal.setDisable(false));
	}

	public void fanRpmTask() {
		Platform.runLater(() -> clickedButtonRef.setDisable(true));

		String value;
		if (SIMULATION_MODE) {
			value = generateRandomRpm(); // Simulate random RPM
			appendLog("SIMULATION: Fan RPM: " + value, LogLevel.INFO);
		} else {
			String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
			String testCaseName = ConstantArduinoCommands.FAN_RPM;
			Optional<DutCommand> opt = DeviceDataManagerController
					.getDutCommandService()
					.findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);

			if (opt.isPresent()) {
				DeviceDataManagerController.setDutCommandData(opt.get());

				Map<String, Object> resp = new DutSerialDataManager()
						.dutExecuteCommand(ConstantAppConfig.DUT_COMMAND_INTERFACE_ID, "", false);

				boolean status = resp.get("status") instanceof Boolean && (Boolean) resp.get("status");
				value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;
			} else {
				value = ConstantArduinoCommands.CMD_FAILED;
			}
		}

		Platform.runLater(() -> {
			if (clickedButtonRef == ref_btnFanRpm) {
				ref_txtFanRpm.setText(value);
			} else if (clickedButtonRef == ref_btnFanRpmExecute) {
				ref_txtFanRpmExecute.setText(value);
			}
		});

		Platform.runLater(() -> clickedButtonRef.setDisable(false));
	}

	public void fanWindspeedTask() {
		Platform.runLater(() -> clickedButtonRef.setDisable(true));

		String value;
		if (SIMULATION_MODE) {
			value = generateRandomWindSpeed(); // Simulate random windspeed
			appendLog("SIMULATION: Fan Windspeed: " + value, LogLevel.INFO);
		} else {
			String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
			String testCaseName = ConstantArduinoCommands.FAN_WINDSPEED;
			Optional<DutCommand> opt = DeviceDataManagerController
					.getDutCommandService()
					.findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);

			if (opt.isPresent()) {
				DeviceDataManagerController.setDutCommandData(opt.get());

				Map<String, Object> resp = new DutSerialDataManager()
						.dutExecuteCommand(ConstantAppConfig.DUT_COMMAND_INTERFACE_ID, "", false);

				boolean status = resp.get("status") instanceof Boolean && (Boolean) resp.get("status");
				value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;
			} else {
				value = ConstantArduinoCommands.CMD_FAILED;
			}
		}

		Platform.runLater(() -> {
			if (clickedButtonRef == ref_btnFanWindspeed) {
				ref_txtFanWindSpeed.setText(value);
			} else if (clickedButtonRef == ref_btnFanWindspeedExecute) {
				ref_txtFanWindSpeedExecute.setText(value);
			}
		});

		Platform.runLater(() -> clickedButtonRef.setDisable(false));
	}

	public void mainsOnTask() {
		Platform.runLater(() -> clickedButtonRef.setDisable(true));

		ApplicationLauncher.logger.info("MainsOnTask: Entry");

		String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
		String testCaseName = ConstantArduinoCommands.MAINS_ON;
		Optional<DutCommand> dutCommandDataOpt = DeviceDataManagerController.getDutCommandService()
				.findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);

		if (dutCommandDataOpt.isPresent()) {
			DutCommand dutCommand = dutCommandDataOpt.get();
			DeviceDataManagerController.setDutCommandData(dutCommand);

			boolean isDataAppend = false;
			// Only call hardware command if not in simulation mode
			if (!SIMULATION_MODE) {
				dutCommandExecuteStart("", isDataAppend);
			} else {
				appendLog("SIMULATION: Mains On skipped.", LogLevel.INFO);
			}
		}

		Platform.runLater(() -> clickedButtonRef.setDisable(false));
	}

	public void mainsOffTask() {
		Platform.runLater(() -> clickedButtonRef.setDisable(true));

		String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
		String testCaseName = ConstantArduinoCommands.MAINS_OFF;
		Optional<DutCommand> dutCommandDataOpt = DeviceDataManagerController.getDutCommandService()
				.findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);

		if (dutCommandDataOpt.isPresent()) {
			DutCommand dutCommand = dutCommandDataOpt.get();
			DeviceDataManagerController.setDutCommandData(dutCommand);

			boolean isDataAppend = false;
			// Only call hardware command if not in simulation mode
			if (!SIMULATION_MODE) {
				dutCommandExecuteStart("", isDataAppend);
			} else {
				appendLog("SIMULATION: Mains On skipped.", LogLevel.INFO);
			}
		}

		Platform.runLater(() -> clickedButtonRef.setDisable(false));
	}


	// Auto Updates ===============================================================================

	/**
	 * Auto-update control methods:
	 *
	 * These methods manage periodic data fetching for the various measurement
	 * phases (R, Y, B), the combined 3-phase metrics, and the fan metrics.
	 * * In normal operation these would run in parallel (each sub-task scheduled
	 * independently on a pool of threads), but for testing they have been
	 * shifted into a single serial sequence.
	 *
	 * startAutoXUpdate():
	 * 1. Makes the corresponding progress indicator visible.
	 * 2. Creates a ScheduledExecutorService (single-threaded for series testing,
	 * or a pool for true parallel runs).
	 * 3. Schedules its metric-specific tasks (voltage, current, watts, VA, PF,
	 * etc.) at a fixed rate (every 3 seconds). In series mode, all subtasks
	 * are invoked one after another inside one Runnable; in parallel mode,
	 * each Runnable is scheduled separately.
	 *
	 * stopAutoXUpdate():
	 * 1. Hides the corresponding progress indicator.
	 * 2. Uses Platform.runLater() to re-enable each metric button on the
	 * JavaFX Application Thread.
	 * 3. Calls stopAutoUpdateGracefully() to shut down the executor:
	 * • Attempts a graceful shutdown (awaits termination up to 3 seconds).
	 * • If still running, forces shutdownNow().
	 * • Logs the outcome under the given phase/fan tag.
	 *
	 * stopAutoUpdateGracefully(executor, tag):
	 * - Initiates executor.shutdown().
	 * - Spawns a background thread to await termination.
	 * - If not terminated within 3 seconds, calls executor.shutdownNow().
	 * - Catches InterruptedException to force shutdown and resets thread interrupt.
	 */

	/*private void startAutoRPhaseUpdate() {
        pi_AutoRPhase.setVisible(true);
        autoRPhaseExecutor = Executors.newScheduledThreadPool(5);

        autoRPhaseExecutor.scheduleAtFixedRate(this::rPhaseVoltageTask, 0, 3, TimeUnit.SECONDS);
        autoRPhaseExecutor.scheduleAtFixedRate(this::rPhaseCurrentTask, 0, 3, TimeUnit.SECONDS);
        autoRPhaseExecutor.scheduleAtFixedRate(this::rPhaseWattsTask, 0, 3, TimeUnit.SECONDS);
        autoRPhaseExecutor.scheduleAtFixedRate(this::rPhaseVATask, 0, 3, TimeUnit.SECONDS);
        autoRPhaseExecutor.scheduleAtFixedRate(this::rPhasePFTask, 0, 3, TimeUnit.SECONDS);
    }*/

	private void startAutoRPhaseUpdate() {
		pi_AutoRPhase.setVisible(true);
		// Single-threaded so tasks execute one by one
		autoRPhaseExecutor = Executors.newSingleThreadScheduledExecutor();

		autoRPhaseExecutor.scheduleAtFixedRate(() -> {
			// each of these runs serially on the same thread
			rPhaseVoltageTask();
			rPhaseCurrentTask();
			rPhaseWattsTask();
			rPhaseVATask();
			rPhasePFTask();
		}, 0, 3, TimeUnit.SECONDS);
	}

	private void stopAutoRPhaseUpdate() {
		pi_AutoRPhase.setVisible(false);
		if (autoRPhaseExecutor != null && !autoRPhaseExecutor.isShutdown()) {

			// re-enable buttons on JavaFX application thread
			Platform.runLater(() -> {
				ref_btnRPhaseVoltage.setDisable(false);
				ref_btnRPhaseCurrent.setDisable(false);
				ref_btnRPhaseWatts.setDisable(false);
				ref_btnRPhaseVA.setDisable(false);
				ref_btnRPhasePF.setDisable(false);
			});

			stopAutoUpdateGracefully(autoRPhaseExecutor, "R Phase");
		}
	}

	private void startAutoYPhaseUpdate() {
		pi_AutoYPhase.setVisible(true);
		autoYPhaseExecutor = Executors.newScheduledThreadPool(5);

		autoYPhaseExecutor.scheduleAtFixedRate(this::yPhaseVoltageTask, 0, 3, TimeUnit.SECONDS);
		autoYPhaseExecutor.scheduleAtFixedRate(this::yPhaseCurrentTask, 0, 3, TimeUnit.SECONDS);
		autoYPhaseExecutor.scheduleAtFixedRate(this::yPhaseWattsTask, 0, 3, TimeUnit.SECONDS);
		autoYPhaseExecutor.scheduleAtFixedRate(this::yPhaseVATask, 0, 3, TimeUnit.SECONDS);
		autoYPhaseExecutor.scheduleAtFixedRate(this::yPhasePFTask, 0, 3, TimeUnit.SECONDS);
	}

	private void stopAutoYPhaseUpdate() {
		pi_AutoYPhase.setVisible(false);
		if (autoYPhaseExecutor != null && !autoYPhaseExecutor.isShutdown()) {

			Platform.runLater(() -> ref_btnYPhaseVoltage.setDisable(false));
			Platform.runLater(() -> ref_btnYPhaseCurrent.setDisable(false));
			Platform.runLater(() -> ref_btnYPhaseWatts.setDisable(false));
			Platform.runLater(() -> ref_btnYPhaseVA.setDisable(false));
			Platform.runLater(() -> ref_btnYPhasePF.setDisable(false));

			stopAutoUpdateGracefully(autoYPhaseExecutor, "Y Phase");
		}
	}

	private void startAutoBPhaseUpdate() {
		pi_AutoBPhase.setVisible(true);
		autoBPhaseExecutor = Executors.newScheduledThreadPool(5);

		autoBPhaseExecutor.scheduleAtFixedRate(this::bPhaseVoltageTask, 0, 3, TimeUnit.SECONDS);
		autoBPhaseExecutor.scheduleAtFixedRate(this::bPhaseCurrentTask, 0, 3, TimeUnit.SECONDS);
		autoBPhaseExecutor.scheduleAtFixedRate(this::bPhaseWattsTask, 0, 3, TimeUnit.SECONDS);
		autoBPhaseExecutor.scheduleAtFixedRate(this::bPhaseVATask, 0, 3, TimeUnit.SECONDS);
		autoBPhaseExecutor.scheduleAtFixedRate(this::bPhasePFTask, 0, 3, TimeUnit.SECONDS);
	}

	private void stopAutoBPhaseUpdate() {
		pi_AutoBPhase.setVisible(false);
		if (autoBPhaseExecutor != null && !autoBPhaseExecutor.isShutdown()) {

			Platform.runLater(() -> ref_btnBPhaseVoltage.setDisable(false));
			Platform.runLater(() -> ref_btnBPhaseCurrent.setDisable(false));
			Platform.runLater(() -> ref_btnBPhaseWatts.setDisable(false));
			Platform.runLater(() -> ref_btnBPhaseVA.setDisable(false));
			Platform.runLater(() -> ref_btnBPhasePF.setDisable(false));

			stopAutoUpdateGracefully(autoBPhaseExecutor, "B Phase");
		}
	}

	private void startAuto3PhaseUpdate() {
		pi_Auto3Phase.setVisible(true);
		auto3PhaseExecutor = Executors.newScheduledThreadPool(5);

		auto3PhaseExecutor.scheduleAtFixedRate(this::phase3WattsTotalTask, 0, 3, TimeUnit.SECONDS);
		auto3PhaseExecutor.scheduleAtFixedRate(this::phase3VATotalTask, 0, 3, TimeUnit.SECONDS);
		auto3PhaseExecutor.scheduleAtFixedRate(this::phase3PFAvgTask, 0, 3, TimeUnit.SECONDS);
		auto3PhaseExecutor.scheduleAtFixedRate(this::phase3CurrentAvgTask, 0, 3, TimeUnit.SECONDS);

		auto3PhaseExecutor.scheduleAtFixedRate(this::phase3VLLTask, 0, 3, TimeUnit.SECONDS);
		auto3PhaseExecutor.scheduleAtFixedRate(this::phase3VLNTask, 0, 3, TimeUnit.SECONDS);
		auto3PhaseExecutor.scheduleAtFixedRate(this::phase3VRYTask, 0, 3, TimeUnit.SECONDS);
		auto3PhaseExecutor.scheduleAtFixedRate(this::phase3VYBTask, 0, 3, TimeUnit.SECONDS);
		auto3PhaseExecutor.scheduleAtFixedRate(this::phase3VBRTask, 0, 3, TimeUnit.SECONDS);

	}

	private void stopAuto3PhaseUpdate() {
		pi_Auto3Phase.setVisible(false);
		if (auto3PhaseExecutor != null && !auto3PhaseExecutor.isShutdown()) {

			Platform.runLater(() -> ref_btn3PhaseWattsTotal.setDisable(false));
			Platform.runLater(() -> ref_btn3PhaseVATotal.setDisable(false));
			Platform.runLater(() -> ref_btn3PhasePFAvg.setDisable(false));
			Platform.runLater(() -> ref_btn3PhaseCurrentAvg.setDisable(false));

			Platform.runLater(() -> ref_btn3PhaseVLL.setDisable(false));
			Platform.runLater(() -> ref_btn3PhaseVLN.setDisable(false));
			Platform.runLater(() -> ref_btn3PhaseVRY.setDisable(false));
			Platform.runLater(() -> ref_btn3PhaseVYB.setDisable(false));
			Platform.runLater(() -> ref_btn3PhaseVBR.setDisable(false));

			stopAutoUpdateGracefully(auto3PhaseExecutor, "3 Phase");
		}
	}

	private void startAutoFanUpdate() {
		pi_AutoFan.setVisible(true);
		autoBPhaseExecutor = Executors.newScheduledThreadPool(5);

		autoFanExecutor.scheduleAtFixedRate(this::fanRpmTask, 0, 3, TimeUnit.SECONDS);
		autoFanExecutor.scheduleAtFixedRate(this::fanWindspeedTask, 0, 3, TimeUnit.SECONDS);

	}

	private void stopAutoFanUpdate() {
		pi_AutoFan.setVisible(false);
		if (autoFanExecutor != null && !autoFanExecutor.isShutdown()) {

			Platform.runLater(() -> ref_btnFanRpm.setDisable(false));
			Platform.runLater(() -> ref_btnFanWindspeed.setDisable(false));

			stopAutoUpdateGracefully(autoFanExecutor, "Fan");
		}
	}

	private void stopAutoUpdateGracefully(ExecutorService executor, String tag) {
		if (executor != null && !executor.isShutdown()) {
			executor.shutdown(); // Graceful stop
			new Thread(() -> {
				try {
					if (!executor.awaitTermination(3, TimeUnit.SECONDS)) {
						ApplicationLauncher.logger.debug(tag + " executor not terminated in time, forcing shutdown...");
						executor.shutdownNow(); // Force if not done
					} else {
						ApplicationLauncher.logger.debug(tag + " executor terminated gracefully.");
					}
				} catch (InterruptedException e) {
					executor.shutdownNow();
					Thread.currentThread().interrupt();
					ApplicationLauncher.logger.debug(tag + " executor interrupted during shutdown.");
				}
			}).start(); // Run in background thread
		}
	}


	// ==============================================================================================================================================

	/**
	 * Reads a parameter value using the provided function, validates it against limits,
	 * updates the actual value and validity in the test point, updates UI progress,
	 * and stores the result. Used for test point measurements like RPM, current, etc.
	 *
	 * @param readFunc            Function to read the parameter value (from hardware or simulation)
	 * @param lowerLimitGetter    Function to get the lower limit for the parameter
	 * @param upperLimitGetter    Function to get the upper limit for the parameter
	 * @param actualSetter        Consumer to update the actual value in the test point
	 * @param validPropertyGetter Function to get the validity flag for the parameter
	 * @param testPoint           The test point being evaluated
	 * @param progress            Progress value (0.0 to 1.0) to update UI
	 * @param label               Label of the parameter (for logging and result mapping)
	 * @throws InterruptedException if the thread is interrupted during sleep
	 */
	private void readValidateAndUpdate(
	        Supplier<String> readFunc,
	        Function<FanTestSetup, String> lowerLimitGetter,
	        Function<FanTestSetup, String> upperLimitGetter,
	        BiConsumer<FanTestSetup, String> actualSetter,
	        Function<FanTestSetup, BooleanProperty> validPropertyGetter,
	        FanTestSetup testPoint,
	        double progress,
	        String label
	) throws InterruptedException {
	    // Step 1: Read the parameter value
	    String value = readFunc.get();
	    String finalValue = value;
	    appendLog("Read and Update : " + label + " : " + finalValue, LogLevel.INFO);
	    ApplicationLauncher.logger.debug("runSingleTestPoint : Read and Update : " + label + " : " + finalValue);

	    // Step 2: Fetch limit values for validation
	    String lowerLimit = lowerLimitGetter.apply(testPoint);
	    String upperLimit = upperLimitGetter.apply(testPoint);
	    boolean isValid = false;

	    // Step 3: Validate the value within lower and upper bounds
	    try {
	        if (finalValue != null && !finalValue.trim().isEmpty() &&
	            lowerLimit != null && !lowerLimit.trim().isEmpty() &&
	            upperLimit != null && !upperLimit.trim().isEmpty()) {

	            double val = Double.parseDouble(finalValue.trim());
	            double lower = Double.parseDouble(lowerLimit.trim());
	            double upper = Double.parseDouble(upperLimit.trim());

	            isValid = val >= lower && val <= upper;
	        }
	    } catch (NumberFormatException e) {
	        isValid = true; // fallback if parsing fails
	    }

	    // Step 4: Update validity status in UI and result
	    validPropertyGetter.apply(testPoint).set(isValid);
	    if (!isValid) {
	        allValid = false; // Mark test point result as failed if any parameter is invalid
	    }

	    // Set the validity flag in currentResult
	    switch (label) {
	        case "RPM"          : currentResult.setRpmValid(isValid); break;
	        case "WindSpeed"    : currentResult.setWindSpeedValid(isValid); break;
	        case "Current"      : currentResult.setCurrentValid(isValid); break;
	        case "Watts"        : currentResult.setWattsValid(isValid); break;
	        case "Vibration"    : currentResult.setVibrationValid(isValid); break;
	        case "ActivePower"  : currentResult.setActivePowerValid(isValid); break;
	        case "PowerFactor"  : currentResult.setPowerFactorValid(isValid); break;
	    }

	    // Step 5: Update the actual value and progress in the UI
	    Platform.runLater(() -> actualSetter.accept(testPoint, finalValue));
	    Platform.runLater(() -> testPoint.setProgress(progress));

	    // Step 6: Update actual measurement in currentResult
	    switch (label) {
	        case "RPM"          : currentResult.setRpm(finalValue); break;
	        case "WindSpeed"    : currentResult.setWindSpeed(finalValue); break;
	        case "Current"      : currentResult.setCurrent(finalValue); break;
	        case "Watts"        : currentResult.setWatts(finalValue); break;
	        case "Vibration"    : currentResult.setVibration(finalValue); break;
	        case "ActivePower"  : currentResult.setVa(finalValue); break;
	        case "PowerFactor"  : currentResult.setPowerFactor(finalValue); break;
	    }

	    // Step 7: Small delay between updates
	    Thread.sleep(500);
	}


	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	/**
	 * appendLog:
	 * Safely appends a log message to the JavaFX TextArea on the UI thread.
	 *
	 * 1. Wraps updates in Platform.runLater() to ensure execution on the
	 * JavaFX Application Thread.
	 * 2. Checks that the textAreaLogs control is non-null before use.
	 * 3. Generates a timestamp (HH:mm:ss, without nanoseconds) via LocalTime.
	 * 4. Prepends the timestamp to the message in square brackets and
	 * adds a newline, then appends it to the TextArea.
	 *//*
	public void appendLog(String message) {
	    Platform.runLater(() -> {
	        if (textAreaLogs != null) {
	            String timestamp = java.time.LocalTime.now().withNano(0).toString();
	            textAreaLogs.appendText("[" + timestamp + "] " + message + "\n");
	        }
	    });
	}*/

	/**
	 * appendLog:
	 * Adds a timestamped log message to the top of a ListView.
	 * Maintains fast updates and avoids UI thread bloat.
	 */
	public void appendLog(String message, LogLevel level) {
		LogEntry entry = new LogEntry(message, level);

		Platform.runLater(() -> {
			if (listViewLogs != null) {
				logItems.add(0, entry);  // Prepend
			}
		});
	}



	/**
	 * Adds numeric input validation to a TextField that only allows values between 1 and 240.
	 * If the field is empty, not numeric, or out of range, a pop-up error message is shown.
	 *
	 * @param field The TextField to apply validation on.
	 * @param label The label used in the validation message for user clarity.
	 */
	public void addNumericRangeValidation(TextField field, String label) {
		// Create a pop-up to show validation error
		Popup validationPopup = new Popup();
		Text validationText = new Text();

		// Style and configure the pop-up content
		StackPane popupContent = new StackPane(validationText);
		popupContent.setBackground(new Background(new BackgroundFill(Color.web("#ffefef"), new CornerRadii(8), Insets.EMPTY)));
		popupContent.setPadding(new Insets(6));
		popupContent.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
		validationText.setFill(Color.RED);
		validationText.setFont(Font.font("Arial", 12));
		validationPopup.getContent().add(popupContent);
		validationPopup.setAutoHide(true);

		// Listener for text changes in the field
		field.textProperty().addListener((obs, oldVal, newVal) -> {
			String message = null;

			// Validation rules
			if (newVal.isEmpty()) {
				message = label + " cannot be empty.";
			} else if (!newVal.matches("\\d+")) {
				message = label + " must be a number.";
			} else {
				int value = Integer.parseInt(newVal);
				if (value < 1 || value > 240) {
					message = label + " must be between 1 and 240.";
				}
			}

			if (message != null) {
				// Set red border and show pop-up
				field.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
				validationText.setText(message);

				if (!validationPopup.isShowing()) {
					double x = field.localToScreen(field.getBoundsInLocal()).getMinX();
					double y = field.localToScreen(field.getBoundsInLocal()).getMinY() - 35;
					validationPopup.show(field, x, y);
				}
			} else {
				// Clear styles and hide pop-up on valid input
				field.setStyle("");
				validationPopup.hide();
			}
		});
	}

	/**
	 * Adds numeric input validation to a TextField that allows values from 0 to 240.
	 * Similar to the above method, but includes 0 as a valid entry.
	 *
	 * @param field The TextField to apply validation on.
	 * @param label The label used in the validation message for user clarity.
	 */
	public void addNumericRangeValidationAllowZero(TextField field, String label) {
		Popup validationPopup = new Popup();
		Text validationText = new Text();

		StackPane popupContent = new StackPane(validationText);
		popupContent.setBackground(new Background(new BackgroundFill(Color.web("#ffefef"), new CornerRadii(8), Insets.EMPTY)));
		popupContent.setPadding(new Insets(6));
		popupContent.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
		validationText.setFill(Color.RED);
		validationText.setFont(Font.font("Arial", 12));
		validationPopup.getContent().add(popupContent);
		validationPopup.setAutoHide(true);

		field.textProperty().addListener((obs, oldVal, newVal) -> {
			String message = null;

			if (newVal.isEmpty()) {
				message = label + " cannot be empty.";
			} else if (!newVal.matches("\\d+")) {
				message = label + " must be a number.";
			} else {
				int value = Integer.parseInt(newVal);
				if (value < 0 || value > 415) {
					message = label + " must be between 0 and 415.";
				}
			}

			if (message != null) {
				field.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
				validationText.setText(message);

				if (!validationPopup.isShowing()) {
					double x = field.localToScreen(field.getBoundsInLocal()).getMinX();
					double y = field.localToScreen(field.getBoundsInLocal()).getMinY() - 35;
					validationPopup.show(field, x, y);
				}
			} else {
				field.setStyle("");
				validationPopup.hide();
			}
		});
	}

	/**
	 * Adds validation to ensure a TextField contains a valid identifier-like name.
	 * Rules: must start with a letter or underscore, followed by letters, digits, or underscores.
	 * Invalid input shows a styled tool-tip.
	 *
	 * @param field The TextField to validate.
	 * @param label The label used in error messages.
	 */
	public void addTextValidation(TextField field, String label) {
		Tooltip tooltip = new Tooltip();
		tooltip.setStyle("-fx-background-color: #ffdddd; -fx-text-fill: red; -fx-font-size: 12;");
		tooltip.setAutoHide(true);

		field.textProperty().addListener((obs, oldText, newText) -> {
			// Validation: Only letters, digits, and underscore; no spaces; no number at the start
			boolean isValid = newText.matches("^[A-Za-z_][A-Za-z0-9_]*$");

			if (newText.isEmpty()) {
				// Reset style if empty
				field.setStyle(""); // Clear styles if empty
				tooltip.hide();
			} else if (!isValid) {
				// Invalid input styling and tooltip message
				field.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
				tooltip.setText(label + " must start with a letter or underscore and contain only letters, numbers, or underscores.");
				if (!tooltip.isShowing()) {
					Bounds bounds = field.localToScreen(field.getBoundsInLocal());
					tooltip.show(field, bounds.getMinX(), bounds.getMinY() - 30);
				}
			} else {
				// Valid input
				field.setStyle(""); // Valid input: clear error styles
				tooltip.hide();
			}
		});
	}



	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++



	public JSONObject getModelParameters(String project_name){
		int model_id = MySQL_Controller.sp_getProjectModel_ID(project_name);
		JSONObject model_data = MySQL_Controller.sp_getem_model_data(model_id);

		return model_data;
	}

	/*public void dutCommandExecuteStart(String appendData,  boolean isDataAppend) {
		int timeduration = displayDataObj.getDutCommandData().getTotalDutExecutionTimeInSec() ;
		DutSerialDataManager dutSerialDataManager =  new DutSerialDataManager();

		int dutInterfaceId = ConstantAppConfig.DUT_COMMAND_INTERFACE_ID;
		dutSerialDataManager.dutExecuteCommand(dutInterfaceId,appendData,  isDataAppend);
	}*/

	// dutCommandExecuteStart function (Fixed for Java 1.8)
	public boolean dutCommandExecuteStart(String appendData, boolean isDataAppend) {
		ApplicationLauncher.logger.info("dutCommandExecuteStart : ENTRY");

		DeviceDataManagerController.getDutCommandData().getTotalDutExecutionTimeInSec();
		DutSerialDataManager dutSerialDataManager = new DutSerialDataManager();

		int dutInterfaceId = ConstantAppConfig.DUT_COMMAND_INTERFACE_ID;
		Map<String, Object> resultMap;

		if (SIMULATION_MODE) {
			appendLog("SIMULATION: dutCommandExecuteStart skipped (data: " + appendData + ").", LogLevel.INFO);
			// Fix for Java 1.8: Use HashMap instead of Map.of()
			Map<String, Object> simulatedResponse = new HashMap<>();
			simulatedResponse.put("status", true);
			simulatedResponse.put("result", "SIM_OK");
			resultMap = simulatedResponse;
		} else {
			resultMap = dutSerialDataManager.dutExecuteCommand(dutInterfaceId, appendData, isDataAppend);
		}

		Object status = resultMap.get("status");
		return status instanceof Boolean && (Boolean) status;
	}


	public JSONObject getDutTestParameters(String projectName) throws JSONException{
		//String project_name = getCurrentProjectName();//MeterParamsController.getCurrentProjectName();
		String test_type = ConstantApp.TEST_PROFILE_DUT_COMMAND;// testcasedetails.getString("test_type");
		String alias_id = "01";
		ApplicationLauncher.logger.info("getDutTestParameters: test_type: " + test_type);
		JSONObject test_parameter = MySQL_Controller.sp_get_dut_commands(projectName, test_type, alias_id);
		ApplicationLauncher.logger.info("getDutTestParameters: test_parameter: " + test_parameter);

		return test_parameter;
	}

	// RANDOM GENERATOR
	private static final Random random = new Random();
	// Method to generate random RPM value (e.g., in the range of 0 to 1150)
	public static String generateRandomRpm() {
		int rpm = random.nextInt(1151); // Random number between 0 and 1150
		return String.valueOf(rpm);
	}

	// Method to generate random wind speed value (e.g., in the range of 0 to 20)
	public static String generateRandomWindSpeed() {
		double windSpeed = 1 + (random.nextDouble() * 19); // Random value between 1 and 20
		return String.format("%.1f", windSpeed);  // Format to one decimal place
	}

	// Method to generate random Vibration value (e.g., in the range of 0.1 to 5.0 g)
	public static String generateRandomVibration() {
		double vibration = 0.1 + (random.nextDouble() * 4.9); // Random value between 0.1 and 5.0
		return String.format("%.1f", vibration);  // Format to one decimal place
	}

	// Method to generate random voltage value (e.g., in the range of 0 to 415)
	public static String generateRandomVoltage() {
		int voltage = random.nextInt(416); // Random number between 0 and 415
		return String.valueOf(voltage);
	}

	// Method to generate random current value (e.g., in the range of 0 to 5 amps)
	public static String generateRandomCurrent() {
		double current = 0.1 + (random.nextDouble() * 4.9); // Random value between 0.1 and 5
		return String.format("%.1f", current);
	}

	// Method to generate random watts value (e.g., in the range of 0 to 2000 watts)
	public static String generateRandomWatts() {
		int watts = random.nextInt(2001); // Random value between 0 and 2000
		return String.valueOf(watts);
	}

	// Method to generate random active power value (e.g., in the range of 0 to 2000 VA)
	public static String generateRandomActivePower() {
		int activePower = random.nextInt(2001); // Random value between 0 and 2000
		return String.valueOf(activePower);
	}

	// Method to generate random power factor value (e.g., between 0.1 and 1.0)
	public static String generateRandomPowerFactor() {
		double powerFactor = 0.1 + (random.nextDouble() * 0.9); // Random value between 0.1 and 1.0
		return String.format("%.2f", powerFactor);  // Format to two decimal places
	}



	// DYNAMIC CONTROLS PANEL
	/**
	 * Loads an FXML into the dynamic content area based on the model phase.
	 * @param modelPhase the current model phase determining which screen to show
	 */
	public void loadPhaseContent(String modelPhase) {
		String fxmlPath;

		switch (modelPhase) {
		case "1":
			fxmlPath = "/fxml/project/SinglePhase" + ConstantApp.THEME_FXML	;
			break;
		case "3":
			fxmlPath = "/fxml/project/ThreePhase" + ConstantApp.THEME_FXML	;
			break;
		default:
			return;
		}

		try {
			Parent content = FXMLLoader.load(getClass().getResource(fxmlPath));
			dynamicContentPane.getChildren().setAll(content);
			// Optional: anchor content to all sides
			AnchorPane.setTopAnchor(content, 0.0);
			AnchorPane.setBottomAnchor(content, 0.0);
			AnchorPane.setLeftAnchor(content, 0.0);
			AnchorPane.setRightAnchor(content, 0.0);
		} catch (IOException e) {
			e.printStackTrace();
			//log or display a UI alert here
		}
	}

	public boolean isFirstTestPointInSequence() {
		return isFirstTestPointInSequence;
	}

	public void setFirstTestPointInSequence(boolean isFirstTestPointInSequence) {
		this.isFirstTestPointInSequence = isFirstTestPointInSequence;
	}

	private void openSimulationConfigurationDialog() {
		ApplicationLauncher.logger.info("Opening Simulation Configuration Dialog.");

		Stage dialogStage = new Stage();
		dialogStage.setTitle("Simulation Configuration");

		try {
			Window owner = btnSettings != null && btnSettings.getScene() != null
					? btnSettings.getScene().getWindow()
							: null;
			if (owner != null) {
				dialogStage.initOwner(owner);
			} else {
				ApplicationLauncher.logger.warn("btnSettings or its scene is null. Opening dialog without owner.");
			}
		} catch (Exception e) {
			ApplicationLauncher.logger.warn("Failed to set dialog owner: " + e.getMessage());
		}

		dialogStage.initModality(Modality.APPLICATION_MODAL);

		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10));
		grid.setHgap(10);
		grid.setVgap(10);

		Label label = new Label("Enable Simulation Mode:");
		CheckBox simulationCheckBox = new CheckBox();
		simulationCheckBox.setSelected(SIMULATION_MODE);

		Button saveButton = new Button("Save");
		Button cancelButton = new Button("Cancel");
		Label statusLabel = new Label();
		statusLabel.setWrapText(true);

		grid.add(label, 0, 0);
		grid.add(simulationCheckBox, 1, 0);
		grid.add(saveButton, 0, 1);
		grid.add(cancelButton, 1, 1);
		GridPane.setColumnSpan(statusLabel, 2);
		grid.add(statusLabel, 0, 2);

		saveButton.setOnAction(event -> {
			boolean newMode = simulationCheckBox.isSelected();
			if (SIMULATION_MODE != newMode) {
				SIMULATION_MODE = newMode;
				appendLog("Simulation Mode set to: " + SIMULATION_MODE, LogLevel.INFO);
				statusLabel.setText("Simulation mode updated successfully.");
				statusLabel.setTextFill(Color.GREEN);
				ApplicationLauncher.logger.info("Simulation mode updated: " + SIMULATION_MODE);
			} else {
				statusLabel.setText("No change detected.");
				statusLabel.setTextFill(Color.GRAY);
				ApplicationLauncher.logger.info("Simulation mode unchanged.");
			}
			dialogStage.close();
		});

		cancelButton.setOnAction(event -> {
			ApplicationLauncher.logger.info("Simulation Configuration Dialog cancelled.");
			dialogStage.close();
		});

		dialogStage.setScene(new Scene(grid));
		dialogStage.setResizable(false);
		dialogStage.showAndWait();
	}

}
