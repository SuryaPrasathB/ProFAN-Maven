package com.tasnetwork.calibration.energymeter.deployment;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.StringBuilder;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import com.tasnetwork.calibration.energymeter.ApplicationHomeController;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.calib.LscsCalibrationConfigLoader;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantConveyor;
import com.tasnetwork.calibration.energymeter.constant.ConstantLduCcube;
import com.tasnetwork.calibration.energymeter.constant.ConstantLscsHarmonicsSourceSlave;
import com.tasnetwork.calibration.energymeter.constant.ConstantPowerSourceBofa;
import com.tasnetwork.calibration.energymeter.constant.ConstantLduLscs;
import com.tasnetwork.calibration.energymeter.constant.ConstantPowerSourceLscs;
import com.tasnetwork.calibration.energymeter.constant.ConstantPowerSourceMte;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdRadiant;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdKre;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;
import com.tasnetwork.calibration.energymeter.constant.ProCalCustomerConfiguration;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.database.MySQL_Controller;
import com.tasnetwork.calibration.energymeter.device.Communicator;
import com.tasnetwork.calibration.energymeter.device.Data_LduBofa;
import com.tasnetwork.calibration.energymeter.device.Data_PowerSourceBofa;
import com.tasnetwork.calibration.energymeter.device.Data_PowerSourceFeedBack;
import com.tasnetwork.calibration.energymeter.device.Data_PowerSourceSetTarget;
import com.tasnetwork.calibration.energymeter.device.Data_RefStdBofa;
import com.tasnetwork.calibration.energymeter.device.Data_RefStdKre;
import com.tasnetwork.calibration.energymeter.device.Data_RefStdSands;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.device.DutSerialDataManager;
import com.tasnetwork.calibration.energymeter.device.SerialDataBofaRefStd;
import com.tasnetwork.calibration.energymeter.device.SerialDataLDU;
import com.tasnetwork.calibration.energymeter.device.SerialDataManager;
import com.tasnetwork.calibration.energymeter.device.SerialDataMteRefStd;
import com.tasnetwork.calibration.energymeter.device.SerialDataRadiantRefStd;
import com.tasnetwork.calibration.energymeter.device.SerialDataRefStdKiggs;
import com.tasnetwork.calibration.energymeter.device.SerialDataRefStdKre;
import com.tasnetwork.calibration.energymeter.director.PowerSourceDirector;
import com.tasnetwork.calibration.energymeter.director.RefStdDirector;
import com.tasnetwork.calibration.energymeter.message.lscsPowerSourceHarmonicsMessage;
import com.tasnetwork.calibration.energymeter.messenger.BofaRequestProcessor;
import com.tasnetwork.calibration.energymeter.remote.TestPointStatus;
import com.tasnetwork.calibration.energymeter.remote.TestResult;
import com.tasnetwork.calibration.energymeter.setting.BusyLoadingController;
import com.tasnetwork.calibration.energymeter.setting.DevicePortSetupController;
import com.tasnetwork.calibration.energymeter.testprofiles.TestProfileType;
import com.tasnetwork.calibration.energymeter.testreport.ResultDataModel;
import com.tasnetwork.calibration.energymeter.uac.UacDataModel;
import com.tasnetwork.calibration.energymeter.util.ErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.util.GuiUtils;
import com.tasnetwork.spring.orm.model.DutCommand;
//import com.tasnetwork.graph.HarmonicGeneration;
import com.tasnetwork.spring.orm.model.ProjectRun;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;;


public class ProjectExecutionController implements Initializable {
	
	
	private static volatile boolean  dutCalibrationVoltageTargetSet = false; 
	private static volatile boolean  dutCalibrationCurrentTargetSet = false; 
	private static volatile boolean  dutCalibrationCurrentZeroSet = false; 
	private static volatile boolean  dutCalibrationVoltCurrentSetZero = false; 
	
	private Timeline ExecuteTimeLineObj = null;
	
	PowerSourceDirector powerSourceDirector = new PowerSourceDirector();
	
	private static String presentProjectRunId = "";
	
	private static TestPointStatus presentTestPointStatus = new TestPointStatus();

	private boolean lastExecutedTestPointContainedHarmonics_Slave = false;
	static volatile boolean firstExecutionTestPointWithHarmonics = false;
	static volatile boolean secondthExecutionTestPointWithBreakWithHarmonics = false;///created for second continous test execution with Stop and start

	private static boolean lastExecutedTestPointContainedHarmonics_Master = false;
	static boolean uacExecuteAuthorized = true;
	static volatile boolean manualModeExecution = false;

	private String presentMeterType = "";

	Timer refStdFeedBackControlTimer;

	private float lastRefStdRead_R_PhaseDegreePhaseAngle = 0.0f;
	private boolean lastPowerSource_R_PhaseAngleIncrement = false;
	private boolean lastPowerSource_R_PhaseAngleDecrement= false;

	private boolean lastPowerSource_Y_PhaseAngleIncrement = false;
	private boolean lastPowerSource_Y_PhaseAngleDecrement= false;

	private boolean lastPowerSource_B_PhaseAngleIncrement = false;
	private boolean lastPowerSource_B_PhaseAngleDecrement= false;

	private float lastRefStdFeedBackRead_R_PhaseCurrent = 0.0f;
	private boolean lastPowerSource_R_CurrentIncrement = false;
	private boolean lastPowerSource_R_CurrentDecrement= false;


	private float lastRefStdFeedBackRead_Y_PhaseCurrent = 0.0f;
	private boolean lastPowerSource_Y_CurrentIncrement = false;
	private boolean lastPowerSource_Y_CurrentDecrement= false;

	private float lastRefStdFeedBackRead_B_PhaseCurrent = 0.0f;
	private boolean lastPowerSource_B_CurrentIncrement = false;
	private boolean lastPowerSource_B_CurrentDecrement= false;


	private float lastRefStdFeedBackRead_R_PhaseVolt = 0.0f;
	private boolean lastPowerSource_R_VoltIncrement = false;
	private boolean lastPowerSource_R_VoltDecrement= false;

	private float lastRefStdFeedBackRead_Y_PhaseVolt = 0.0f;
	private boolean lastPowerSource_Y_VoltIncrement = false;
	private boolean lastPowerSource_Y_VoltDecrement= false;

	private float lastRefStdFeedBackRead_B_PhaseVolt = 0.0f;
	private boolean lastPowerSource_B_VoltIncrement = false;
	private boolean lastPowerSource_B_VoltDecrement= false;

	private boolean feedbackAdjustmentDoubleConfirmed_R_VoltDecrement = false;
	private boolean feedbackAdjustmentDoubleConfirmed_R_VoltIncrement = false;
	private boolean feedbackAdjustmentDoubleConfirmed_R_CurrentDecrement = true;
	private boolean feedbackAdjustmentDoubleConfirmed_R_CurrentIncrement = true;
	private int feedbackAdjustmentDoubleConfirmed_R_CurrentIncrementCounter = 2;
	private int feedbackAdjustmentDoubleConfirmed_R_CurrentDecrementCounter = 2;

	private boolean feedbackAdjustmentDoubleConfirmed_R_PfDecrement = false;
	private boolean feedbackAdjustmentDoubleConfirmed_R_PfIncrement = false;
	private int feedbackAdjustmentDoubleConfirmed_R_PfIncrementCounter = 0;
	private int feedbackAdjustmentDoubleConfirmed_R_PfDecrementCounter = 0;

	DecimalFormat voltageFormatter = new DecimalFormat("000.000");
	DecimalFormat currentFormatterLessThan1Amps = new DecimalFormat (".000000");
	DecimalFormat currentFormatterLessThan10Amps = new DecimalFormat("0.00000");
	DecimalFormat currentFormatterMoreThan10Amps = new DecimalFormat("000.000");
	DecimalFormat degreeFormatter = new DecimalFormat("000.000");

	private volatile boolean refStdFeedBackControlFlagEnabled = false;
	private volatile boolean refStdFeedBackControlSuspended = false;
	private volatile static boolean refStdFeedBackControlAlreadyReceived = false;

	int ABORT_MANUAL_TIMER_MODE = -2;

	Timer loadNextTimer;
	Timer calibConfigRefreshTimer;
	Timer sourceRefreshTimer;
	Timer lscsPowerSourceManualModeTimerInputTimer;

	Timer ExecuteStopTimer;
	Timer ShutDownAllComPortTimer;
	Timer TCStopConfirmationTimer;
	Timer ViewLogsTimer;


	Timer closeProjectTimer;
	Timer stepRunTimer;
	Timer startRunTimer;
	Timer resumeRunTimer;
	Timer selectProjectOnchangeTimer;
	Timer sourcePromptTimer;

	Timer manualModeBtnStartTimer;
	Timer manualModeBtnUpdateTimer;
	Timer manualModeBtnStopTimer;

	public static volatile boolean semLockExecutionInprogress = false;
	public volatile boolean userRespondedForPromptDisplay = false;

	private int CoolOffTimeInMSec = (int)ConstantAppConfig.COOL_OFF_TIME_IN_MSEC;
	private Integer BufferTimeToReadLDU_DataInSec = 0;
	private Integer STA_BufferTimeToReadLDU_DataInSec =ConstantAppConfig.LDU_STA_READING_WAIT_TIME_IN_SEC;//600;
	private Integer Creep_BufferTimeToReadLDU_DataInSec = ConstantAppConfig.LDU_CREEP_READING_WAIT_TIME_IN_SEC;//120;//0;//60;
	private Integer ConstTestWaitTimeToReadLDU_DataInSec = ConstantAppConfig.LDU_DIAL_TEST_READING_WAIT_TIME_IN_SEC ;//120;
	private Integer Rep_SelfHeating_BufferTimeToReadLDU_DataInSec = ConstantAppConfig.LDU_REPEAT_SELFHEATING_READING_WAIT_TIME_IN_SEC;//120;//25;
	static int ExecuteTimeCounter = 60;
	private int ConstTestRefComInstantMetricsRefreshTimeInMsec = 1000;
	private int Const_BufferTime_InSec = 10;
	private int ExtendTimeForStepRunInSec = 600;
	private volatile int manualModeTimerInputSetValueInSec = 5;
	private volatile int manualModeTimerInputTargetValueInSec = 0;

	private int Skip_TP_Time_InSec = ConstantAppConfig.SKIP_TP_TIME_INSEC;
	private static int PowerOnWaitCounter = ConstantAppConfig.POWERONWAITCOUNTER; 
	private static int PowerSrcOnContinuousFailureCounter = ConstantAppConfig.POWER_SRC_ACCEPTED_CONTINUOUS_FAILURE_COUNTER;
	private static int RefStdFeedBackContinuousFailureCounter = ConstantRefStdRadiant.REF_STD_FEEDBACK_CONTINUOUS_FAILURE_COUNTER;
	private boolean PowerSrcContinuousFailureStatus = false;
	private boolean RefStdFeedBackContinuousFailureStatus = false;

	public static volatile boolean guiRefreshRequested = true;

	public static boolean Test_running = false;
	public static volatile boolean UserAbortedFlag = false;

	public static boolean lscsZeroCurrentPowerTurnOn = false;// with lscsPowersource when current is made zero, firmware respond with I1F, to override the response added the flag

	public static SerialDataManager SerialDM_Obj = new SerialDataManager();
	private static boolean  UpdateRowColorFlag = false;
	private static volatile boolean ExecuteStopStatus = false;

	public static String selectedDeployment_ID = "";
	public static boolean mainCtMode = false;
	public static boolean neutralCtMode = true;

	public static int selectedProjectIndex = -1;
	public static int lastSelectedProjectIndex = -1;

	Timer TC_ExecutionTimer;
	Timer CheckForSerialCommandTimer;

	public static String currentProjectName;
	public static boolean currentProjectAutoDeployEnabled = false;
	public static List<String> projectNameList = new ArrayList<String>();
	public static List<String> deploymentIdList = new ArrayList<String>();
	public static List<String> energyFlowModeList = new ArrayList<String>();
	public static List<Boolean> autoDeployEnabledList = new ArrayList<Boolean>();
	//public static List<String> equipmentSerialNoList = new ArrayList<String>();
	//public static List<String> deployModeList = new ArrayList<String>();

	Timer pwrSrcDataTransmitTimer  ;

	@FXML private Button btnLoadNext;
	@FXML private Button btnCalibConfigRefresh;
	@FXML private Button btnSourceRefresh;

	private static Button ref_btnLoadNext;
	private static Button ref_btnCalibConfigRefresh;
	private static Button ref_btnSourceRefresh;

	@FXML TextField txtRefStdDisplayR_PhaseVolt;
	@FXML TextField txtRefStdDisplayR_PhaseCurrent;
	@FXML TextField txtRefStdDisplayR_PhasePowerFactor;
	@FXML TextField txtRefStdDisplayR_PhaseDegreePhase;
	@FXML TextField txtRefStdDisplayR_PhaseFreq;
	@FXML TextField txtRefStdDisplayR_PhaseWatt;
	@FXML TextField txtRefStdDisplayR_PhaseVA;
	@FXML TextField txtRefStdDisplayR_PhaseWh;
	@FXML TextField txtRefStdDisplayR_PhaseVAR;

	@FXML Label labelMode;
	public static Label ref_labelMode;

	@FXML TabPane tabPaneRunMode;
	@FXML Tab tabManualMode;
	@FXML Tab tabSequenceMode;


	@FXML RadioButton rdBtnManualModeSinglePhase;
	@FXML RadioButton rdBtnManualModeThreePhase;
	@FXML RadioButton rdBtnManualModeImport;
	@FXML RadioButton rdBtnManualModeExport;

	@FXML RadioButton rdBtnManualModeRphasePfLag;
	@FXML RadioButton rdBtnManualModeYphasePfLag;
	@FXML RadioButton rdBtnManualModeBphasePfLag;

	@FXML RadioButton rdBtnManualModeRphasePfLead;
	@FXML RadioButton rdBtnManualModeYphasePfLead;
	@FXML RadioButton rdBtnManualModeBphasePfLead;


	@FXML Label lbl_ManualModeRphase;
	@FXML Label lbl_ManualModeYphase;
	@FXML Label lbl_ManualModeBphase;
	@FXML TextField txt_ManualModeRphaseVoltage;
	@FXML TextField txt_ManualModeYphaseVoltage;
	@FXML TextField txt_ManualModeBphaseVoltage;
	@FXML TextField txt_ManualModeRphaseCurrent;
	@FXML TextField txt_ManualModeYphaseCurrent;
	@FXML TextField txt_ManualModeBphaseCurrent;

	@FXML TextField txt_ManualModeRphasePfValue;
	@FXML TextField txt_ManualModeYphasePfValue;
	@FXML TextField txt_ManualModeBphasePfValue;
	@FXML TextField txt_ManualModeFrequencyValue;

	@FXML CheckBox chkBxManualModeBalanced;
	@FXML CheckBox chBxManualTimerMode;
	@FXML TextField txtManualSetTime;
	@FXML TextField txtManualTargetTime;

	@FXML Label lbl_setTime;
	@FXML Label lbl_targetTime;
	@FXML Label lbl_setTimeSec;
	@FXML Label lbl_targetTimeSec;;

	@FXML Button btnManualModeStart;
	@FXML Button btnManualModeUpdate;
	@FXML Button btnManualModeStop;

	public static RadioButton ref_rdBtnManualModeSinglePhase;
	public static RadioButton ref_rdBtnManualModeThreePhase;
	public static RadioButton ref_rdBtnManualModeImport;
	public static RadioButton ref_rdBtnManualModeExport;

	public static RadioButton ref_rdBtnManualModeRphasePfLag;
	public static RadioButton ref_rdBtnManualModeYphasePfLag;
	public static RadioButton ref_rdBtnManualModeBphasePfLag;

	public static RadioButton ref_rdBtnManualModeRphasePfLead;
	public static RadioButton ref_rdBtnManualModeYphasePfLead;
	public static RadioButton ref_rdBtnManualModeBphasePfLead;


	public static Label ref_lbl_ManualModeRphase;
	public static Label ref_lbl_ManualModeYphase;
	public static Label ref_lbl_ManualModeBphase;
	public static TextField ref_txt_ManualModeRphaseVoltage;
	public static TextField ref_txt_ManualModeYphaseVoltage;
	public static TextField ref_txt_ManualModeBphaseVoltage;
	public static TextField ref_txt_ManualModeRphaseCurrent;
	public static TextField ref_txt_ManualModeYphaseCurrent;
	public static TextField ref_txt_ManualModeBphaseCurrent;

	public static TextField ref_txt_ManualModeRphasePfValue;
	public static TextField ref_txt_ManualModeYphasePfValue;
	public static TextField ref_txt_ManualModeBphasePfValue;
	public static TextField ref_txt_ManualModeFrequencyValue;

	public static CheckBox ref_chkBxManualModeBalanced;
	public static CheckBox ref_chBxManualTimerMode;
	public static TextField ref_txtManualSetTime;
	public static TextField ref_txtManualTargetTime;

	public static  Label ref_lbl_setTime;
	public static  Label ref_lbl_targetTime;
	public static  Label ref_lbl_setTimeSec;
	public static  Label ref_lbl_targetTimeSec;

	public static Button ref_btnManualModeStart;
	public static Button ref_btnManualModeUpdate;
	public static Button ref_btnManualModeStop;


	public static TabPane ref_tabPaneRunMode;
	public static Tab ref_tabManualMode;
	public static Tab ref_tabSequenceMode;

	public static TextField ref_txtRefStdDisplayR_PhaseVolt;
	public static TextField ref_txtRefStdDisplayR_PhaseCurrent;
	public static TextField ref_txtRefStdDisplayR_PhasePowerFactor;
	public static TextField ref_txtRefStdDisplayR_PhaseDegreePhase;
	public static TextField ref_txtRefStdDisplayR_PhaseFreq;
	public static TextField ref_txtRefStdDisplayR_PhaseWatt;
	public static TextField ref_txtRefStdDisplayR_PhaseVA;
	public static TextField ref_txtRefStdDisplayR_PhaseWh;
	public static TextField ref_txtRefStdDisplayR_PhaseVAR;

	private static StringProperty refStdDisplayR_PhaseVolt = new SimpleStringProperty();
	private static StringProperty refStdDisplayR_PhaseCurrent = new SimpleStringProperty();
	private static StringProperty refStdDisplayR_PhasePowerFactor = new SimpleStringProperty();
	private static StringProperty refStdDisplayR_PhaseDegreePhase = new SimpleStringProperty();
	private static StringProperty refStdDisplayR_PhaseFreq = new SimpleStringProperty();
	public static StringProperty refStdDisplayR_PhaseWatt = new SimpleStringProperty();
	private static StringProperty refStdDisplayR_PhaseVA = new SimpleStringProperty();
	private static StringProperty refStdDisplayR_PhaseVAR = new SimpleStringProperty();
	private static StringProperty refStdDisplayR_PhaseWh = new SimpleStringProperty();

	@FXML Label labelRefStdDisplayActiveReactiveUnit_R;


	public static Label ref_labelRefStdDisplayActiveReactiveUnit_R;

	@FXML
	private TableView<List<Object>> tbl_liveStatus;

	@FXML
	private VBox vBoxRunningStatus;

	@FXML
	private AnchorPane anchorProgressStatusPane;

	@FXML
	private HBox hBoxControlsDisplay;

	@FXML
	private Label labelStatusGap;
	@FXML
	private Label labelModeSelectionGap;
	@FXML
	private Label labelControlSelectionGap;


	@FXML
	private GridPane gridPaneInstantMetrics;
	@FXML
	private GridPane gridPaneControls;
	@FXML
	private GridPane gridPaneProjectSelect;
	@FXML
	private GridPane gridPaneModeSelect;
	@FXML
	private GridPane gridPaneTpStatus;
	@FXML
	private GridPane gridPaneProjectStatus;



	@FXML
	private TitledPane titledPaneStatus;
	public static TitledPane ref_titledPaneStatus;
	@FXML
	private TitledPane titledPaneModeSelection;
	@FXML
	private TitledPane titledPaneControls;
	@FXML
	private TitledPane titledPaneInstantMetrics;



	@FXML
	private ComboBox<String> cmbBoxSelectProject;
	public static ComboBox<String> ref_cmbBoxSelectProject;


	@FXML
	private AnchorPane AnchorPaneRunningStatus;

	@FXML
	private AnchorPane anchorPanePhaseA;
	@FXML
	private AnchorPane anchorPanePhaseB;
	@FXML
	private AnchorPane anchorPanePhaseC;

	@FXML
	private GridPane gridPanePhaseA ;
	@FXML
	private GridPane gridPanePhaseB ;
	@FXML
	private GridPane gridPanePhaseC ;

	@FXML
	private HBox hboxRunningStatus;

	@FXML
	private VBox vBoxPhaseDisplay;

	public static TableView<List<Object>> ref_tbl_liveStatus;

	public static TableViewExtra<List<Object>> tvX = null;

	public static boolean devicesDataTableSemLock = false;

	public static boolean bExecutionInProgress = false;

	@FXML
	private TableColumn<?, ?> devicesSelectedColumn;

	@FXML
	private Button btn_Resume;
	private static Button ref_btn_Resume;

	@FXML
	private Button btnCloseProject;

	static Button ref_btnCloseProject;

	@FXML
	private Button btn_Restart;

	//private static Button ref_btn_Restart;

	@FXML
	private Button btn_Stop;
	private static Button ref_btn_Stop;

	@FXML
	private Button btn_step_run;
	private static Button ref_btn_step_run;

	@FXML
	private Button btnViewLogs;
	private static Button ref_btnViewLogs;

	@FXML
	private Button btn_execution_start;
	private static Button ref_btn_execution_start;

	@FXML
	private TitledPane titlePaneLiveStatus;

	@FXML
	private TitledPane titledPaneA;
	@FXML
	private TitledPane titledPaneB;
	@FXML
	private TitledPane titledPaneC;


	@FXML
	private ProgressBar  barTC_TimeProgressBar;
	public static  ProgressBar  ref_barTC_TimeProgressBar;

	@FXML
	private RadioButton  radioBtnMainCt;
	public static  RadioButton  ref_radioBtnMainCt;

	@FXML
	private RadioButton  radioBtnNeutralCt;
	public static  RadioButton  ref_radioBtnNeutralCt;

	@FXML
	private ProgressBar  barProjectStatus;
	public static  ProgressBar  ref_barProjectStatus;

	@FXML
	private ProgressIndicator  barPinProjectStatus;
	public static  ProgressIndicator  ref_barPinProjectStatus;


	@FXML
	private DatePicker datepicker_projectdate;

	@FXML
	private TableView<ScheduledProjectModel> scheduled_project;

	@FXML
	private TableColumn<ScheduledProjectModel, String> projectnamecolumn;

	@FXML
	private TableColumn<ScheduledProjectModel, String> scheduledtimecolumn;

	private ObservableList<ScheduledProjectModel> project_time_data = FXCollections.observableArrayList();

	public static StringProperty DisplayTestPointExecutionTimeFormat = new SimpleStringProperty();

	public static StringProperty DisplayExecuteCounterTimeFormat = new SimpleStringProperty();


	private Integer ProcessTC_ExecutionRefreshTimeInMSec = 2000;
	public static JSONArray  CurrentProjectTestPoint_List = null;
	private static boolean All_TestPoint_Completed = false;
	private static boolean CurrentTestPoint_ExecutionCompleted = false;
	private static Integer CurrentProject_TotalNoOfTestPoint=0;
	private static Integer CurrentTestPoint_Index=0;
	private static ArrayList<String> RunningStatusTableTP_ColumnList = new ArrayList<String>();
	
	private static Map<String, String> remoteTestPointStatusMap = new HashMap<>();

	public String projectEM_ModelType = "";

	static DeviceDataManagerController DisplayDataObj =  new DeviceDataManagerController();


	@FXML TextField txtTimeLeft;



	public static TextField ref_txtTimeLeft;




	@FXML CheckBox ckBxAddress1;
	@FXML CheckBox ckBxAddress2;

	public static CheckBox ref_ckBxAddress1;
	public static CheckBox ref_ckBxAddress2;



	public static JSONObject DeviceMounted = new JSONObject();
	public static volatile String CurrentTestPointName = "";
	
	public static Map<String,Boolean> deviceMountedMap = new LinkedHashMap<String,Boolean>();
	
	
	public static String CurrentTestAliasID;
	//public static int error_count = 1;
	public static long ProjectStartTime;

	public static boolean UI_TableRefreshFlag = false;
	public static boolean Is_DialogBox_Active = false;

	public static boolean TwoLevelValidationOnFlag = ProcalFeatureEnable.STABILIZATION_PWR_SRC_ENABLE_FEATURE;

	public static String FeedbackR_phaseVolt = "-1.01";
	public static String FeedbackR_phaseCurrent = "-1.02";
	public static String FeedbackR_phaseDegree = "370.1";
	public static String FeedbackR_powerFactor = "0.123";
	public static String FeedbackR_activePower = "123.4";
	public static String FeedbackY_phaseVolt = "-1.03";
	public static String FeedbackY_phaseCurrent = "-1.04";
	public static String FeedbackY_phaseDegree = "370.2";
	public static String FeedbackB_phaseVolt = "-1.05";
	public static String FeedbackB_phaseCurrent = "-1.06";
	public static String FeedbackB_phaseDegree = "370.3";
	public static String FeedbackR_Frequency = "-1.0";
	public static String FeedbackY_Frequency = "-2.0";
	public static String FeedbackB_Frequency = "-3.0";

	public static String feedbackR_ActiveEnergy = "";
	public static String feedbackR_ReactiveEnergy = "";
	public static String feedbackR_ApparentEnergy = "";
	public static String feedbackY_ActiveEnergy = "";
	public static String feedbackY_ReactiveEnergy = "";
	public static String feedbackY_ApparentEnergy = "";
	public static String feedbackB_ActiveEnergy = "";
	public static String feedbackB_ReactiveEnergy = "";
	public static String feedbackB_ApparentEnergy = "";

	public static boolean SelfHeatingTestWMPFlag = false;
	public static boolean RepTestFlag = false;

	public static boolean StepRunFlag = false;
	public static boolean ResumeFlag = false;
	public static boolean OneTimeExecuted = false;

	public static String CurrentTestType = "";


	private String FirstPhaseDisplayName = "A";
	private String SecondPhaseDisplayName = "B";
	private String ThirdPhaseDisplayName = "C";
	private String FirstAndSecondPhase = 	"AB";
	private String SecondAndThirdPhase = 	"BC";
	private String FirstAndThirdPhase = 	"AC";


	public static boolean RefStdRelaySettingFlag = ConstantRefStdRadiant.REF_STD_RELAY_SETTING;


	//=============================================================================================================
	/*	//Added by Pradeep
	static ArrayList<Boolean> includeHarmonicsOrder_V1 = new ArrayList<Boolean>();
	static ArrayList<Integer> harmonicsOrderNumber_V1 = new ArrayList<Integer>();
	static ArrayList<Integer> phaseAngleDegreeHarmonicsOrder_V1 = new ArrayList<Integer>();
	static ArrayList<Integer> amplitudePercentageHarmonicsOrder_V1 = new ArrayList<Integer>();

	static ArrayList<Boolean> includeHarmonicsOrder_V2 = new ArrayList<Boolean>();
	static ArrayList<Integer> harmonicsOrderNumber_V2 = new ArrayList<Integer>();
	static ArrayList<Integer> phaseAngleDegreeHarmonicsOrder_V2 = new ArrayList<Integer>();
	static ArrayList<Integer> amplitudePercentageHarmonicsOrder_V2 = new ArrayList<Integer>();

	static ArrayList<Boolean> includeHarmonicsOrder_V3 = new ArrayList<Boolean>();
	static ArrayList<Integer> harmonicsOrderNumber_V3 = new ArrayList<Integer>();
	static ArrayList<Integer> phaseAngleDegreeHarmonicsOrder_V3 = new ArrayList<Integer>();
	static ArrayList<Integer> amplitudePercentageHarmonicsOrder_V3 = new ArrayList<Integer>();

	static ArrayList<Boolean> includeHarmonicsOrder_I1 = new ArrayList<Boolean>();
	static ArrayList<Integer> harmonicsOrderNumber_I1 = new ArrayList<Integer>();
	static ArrayList<Integer> phaseAngleDegreeHarmonicsOrder_I1 = new ArrayList<Integer>();
	static ArrayList<Integer> amplitudePercentageHarmonicsOrder_I1 = new ArrayList<Integer>();

	static ArrayList<Boolean> includeHarmonicsOrder_I2 = new ArrayList<Boolean>();
	static ArrayList<Integer> harmonicsOrderNumber_I2 = new ArrayList<Integer>();
	static ArrayList<Integer> phaseAngleDegreeHarmonicsOrder_I2 = new ArrayList<Integer>();
	static ArrayList<Integer> amplitudePercentageHarmonicsOrder_I2 = new ArrayList<Integer>();

	static ArrayList<Boolean> includeHarmonicsOrder_I3 = new ArrayList<Boolean>();
	static ArrayList<Integer> harmonicsOrderNumber_I3 = new ArrayList<Integer>();
	static ArrayList<Integer> phaseAngleDegreeHarmonicsOrder_I3 = new ArrayList<Integer>();
	static ArrayList<Integer> amplitudePercentageHarmonicsOrder_I3 = new ArrayList<Integer>();

	public static final String HEADER_VOLTAGE = "V";
	public static final String HEADER_CURRENT = "I";
	public static final String CMD_SEPERATOR  = ","; 

	public static final String R_PHASE  = "R"; 
	public static final String Y_PHASE  = "Y"; 
	public static final String B_PHASE  = "B"; */


	//=============================================================================================================

	@Override
	public void initialize(URL url, ResourceBundle rb) {
	
		ApplicationLauncher.logger.info("ProjectExecutionController :initialize: Entry");
		ref_tbl_liveStatus = tbl_liveStatus;
		tvX = new TableViewExtra<List<Object>>(ref_tbl_liveStatus);


		ref_btn_Resume = btn_Resume;
		ref_btn_step_run = btn_step_run;
		ref_btnViewLogs = btnViewLogs;
		ref_btn_Stop = btn_Stop;
		ref_btn_execution_start = btn_execution_start;
		ref_txtTimeLeft = txtTimeLeft;

		ref_ckBxAddress1 = ckBxAddress1;
		ref_ckBxAddress2 = ckBxAddress2;
		ref_barTC_TimeProgressBar = barTC_TimeProgressBar;
		ref_barProjectStatus =barProjectStatus;
		ref_barPinProjectStatus = barPinProjectStatus;
		ref_radioBtnMainCt = radioBtnMainCt;
		ref_radioBtnNeutralCt = radioBtnNeutralCt;
		ref_labelMode = labelMode;
		ref_cmbBoxSelectProject = cmbBoxSelectProject;
		ref_btnCloseProject = btnCloseProject;

		ref_txtRefStdDisplayR_PhaseVolt = txtRefStdDisplayR_PhaseVolt;
		ref_txtRefStdDisplayR_PhaseCurrent = txtRefStdDisplayR_PhaseCurrent;
		ref_txtRefStdDisplayR_PhasePowerFactor= txtRefStdDisplayR_PhasePowerFactor;
		ref_txtRefStdDisplayR_PhaseDegreePhase= txtRefStdDisplayR_PhaseDegreePhase;
		ref_txtRefStdDisplayR_PhaseFreq = txtRefStdDisplayR_PhaseFreq;
		ref_txtRefStdDisplayR_PhaseWatt = txtRefStdDisplayR_PhaseWatt;
		ref_txtRefStdDisplayR_PhaseVA = txtRefStdDisplayR_PhaseVA;
		ref_txtRefStdDisplayR_PhaseVAR = txtRefStdDisplayR_PhaseVAR;
		ref_txtRefStdDisplayR_PhaseWh = txtRefStdDisplayR_PhaseWh;
		ref_labelRefStdDisplayActiveReactiveUnit_R = labelRefStdDisplayActiveReactiveUnit_R;
		ref_titledPaneStatus = titledPaneStatus;

		ref_btnLoadNext = btnLoadNext;
		ref_btnCalibConfigRefresh = btnCalibConfigRefresh;
		ref_btnSourceRefresh = btnSourceRefresh;
		
		
		if(ProcalFeatureEnable.PROPOWER_SRC_ONLY ){

			ref_tabPaneRunMode = tabPaneRunMode;
			ref_tabSequenceMode  = tabSequenceMode;
			ref_tabManualMode = tabManualMode;
			ref_rdBtnManualModeSinglePhase = rdBtnManualModeSinglePhase;
			ref_rdBtnManualModeThreePhase = rdBtnManualModeThreePhase;
			ref_rdBtnManualModeImport = rdBtnManualModeImport;
			ref_rdBtnManualModeExport = rdBtnManualModeExport;

			ref_rdBtnManualModeRphasePfLag = rdBtnManualModeRphasePfLag;
			ref_rdBtnManualModeYphasePfLag = rdBtnManualModeYphasePfLag;
			ref_rdBtnManualModeBphasePfLag = rdBtnManualModeBphasePfLag;

			ref_rdBtnManualModeRphasePfLead = rdBtnManualModeRphasePfLead;
			ref_rdBtnManualModeYphasePfLead = rdBtnManualModeYphasePfLead;
			ref_rdBtnManualModeBphasePfLead = rdBtnManualModeBphasePfLead;


			ref_lbl_ManualModeRphase = lbl_ManualModeRphase;
			ref_lbl_ManualModeYphase = lbl_ManualModeYphase;
			ref_lbl_ManualModeBphase = lbl_ManualModeBphase;
			ref_txt_ManualModeRphaseVoltage = txt_ManualModeRphaseVoltage;
			ref_txt_ManualModeYphaseVoltage = txt_ManualModeYphaseVoltage;
			ref_txt_ManualModeBphaseVoltage = txt_ManualModeBphaseVoltage;
			ref_txt_ManualModeRphaseCurrent = txt_ManualModeRphaseCurrent;
			ref_txt_ManualModeYphaseCurrent = txt_ManualModeYphaseCurrent;
			ref_txt_ManualModeBphaseCurrent = txt_ManualModeBphaseCurrent;

			ref_txt_ManualModeRphasePfValue = txt_ManualModeRphasePfValue;
			ref_txt_ManualModeYphasePfValue = txt_ManualModeYphasePfValue;
			ref_txt_ManualModeBphasePfValue = txt_ManualModeBphasePfValue;
			ref_txt_ManualModeFrequencyValue = txt_ManualModeFrequencyValue;

			ref_btnManualModeStart = btnManualModeStart;
			ref_btnManualModeUpdate = btnManualModeUpdate;
			ref_btnManualModeStop = btnManualModeStop;

			ref_chkBxManualModeBalanced = chkBxManualModeBalanced;
			ref_chBxManualTimerMode = chBxManualTimerMode;
			ref_txtManualSetTime = txtManualSetTime;
			ref_txtManualTargetTime = txtManualTargetTime;

			ref_lbl_setTime = lbl_setTime;
			ref_lbl_targetTime = lbl_targetTime;
			ref_lbl_setTimeSec = lbl_setTimeSec;
			ref_lbl_targetTimeSec = lbl_targetTimeSec;

		}


		Platform.runLater(() -> {
			DisplayTestPointExecutionTimeFormat.setValue(""); 
			DisplayExecuteCounterTimeFormat.setValue("");
		});
		ref_txtTimeLeft.textProperty().bind(DisplayTestPointExecutionTimeFormat);
		ApplicationHomeController.ref_statusLabel.textProperty().bind(DisplayExecuteCounterTimeFormat);



		RunningStatusAdjustScreen();

		if(!ProcalFeatureEnable.RACK_MCT_NCT_ENABLED){
			ref_radioBtnMainCt.setVisible(false);
			ref_radioBtnNeutralCt.setVisible(false);
			ref_labelMode.setVisible(false);
		}else{
			ref_radioBtnMainCt.setSelected(true);
			ref_radioBtnNeutralCt.setSelected(false);
		}
		DisableStopButton();
		DisableStartButton();
		DisableViewLogsButton();
		DisableResumeButton();
		DisableStepRunButton();


		/*		if(DeploymentManagerController.getDeployedFlag()){
			loadDevices();
			EnableStartButton();
			EnableViewLogsButton();
			EnableResumeButton();
			EnableStepRunButton();
			setProjectNameAsAppTitle();
		}*/

		try {
			if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
				if(!ApplicationHomeController.GetInstantMetricsGUI_Displayed()){
					LoadInstantMetricsGUI();
				}
			}
		} catch (JSONException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("ProjectExecutionController: initialize: JSONException: "+e.getMessage());
		}

		try {
			if(ProcalFeatureEnable.MAINTENANCE_MODE_ENABLED){
				if(!ApplicationHomeController.getMaintenanceGUI_Displayed()){
					loadMaintenanceGUI();
				}
			}
		} catch (JSONException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("ProjectExecutionController: initialize: JSONException2: "+e.getMessage());
		}


		try {
			if(!ApplicationHomeController.getLduAllDataViewGUI_Displayed()){
				if(ProcalFeatureEnable.SECONDARY_LDU_DISPLAY_ENABLED){
					loadSecondaryLduDisplayGUI();
				}
			}
		} catch (JSONException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("ProjectExecutionController: initialize: JSONException2: "+e.getMessage());
		}
		//DisplayAppropriateLabelForInstantMetricsGUI();


		if(ProcalFeatureEnable.PHASE_DISPLAY_ENABLE_FEATURE){
			FirstPhaseDisplayName = ConstantApp.FIRST_PHASE_DISPLAY_NAME;
			SecondPhaseDisplayName = ConstantApp.SECOND_PHASE_DISPLAY_NAME;
			ThirdPhaseDisplayName = ConstantApp.THIRD_PHASE_DISPLAY_NAME;
			FirstAndSecondPhase = ConstantApp.FIRST_PHASE_DISPLAY_NAME	+ConstantApp.SECOND_PHASE_DISPLAY_NAME;
			SecondAndThirdPhase = ConstantApp.SECOND_PHASE_DISPLAY_NAME + ConstantApp.THIRD_PHASE_DISPLAY_NAME;
			FirstAndThirdPhase = 	ConstantApp.FIRST_PHASE_DISPLAY_NAME + ConstantApp.THIRD_PHASE_DISPLAY_NAME;

		}
		setMainCtMode(true);
		setNeutralCtMode(false);
		// loadDeployManageDataFromDB();
		getManageDeployData();
		updateProjectListinGUI();
		
		refreshAllDataInGUI();
		if (getLastSelectedProjectIndex() != -1) {
			ref_cmbBoxSelectProject.getSelectionModel().select(getLastSelectedProjectIndex());

		}

		bindPropertytoTextField();
		if(!ProcalFeatureEnable.REF_STD_DATA_DISPLAYED_IN_RUN_SCREEN_ENABLED){
			Platform.runLater(() -> {
				titledPaneInstantMetrics.setVisible(false);
			});
		}

		if(ProcalFeatureEnable.USER_ACCESS_CONTROL_ENABLED){
			applyUacSettings();
		}


		//ApplicationLauncher.logger.debug("ProjectExecutionController: Test1");
		hideGuiObject();
		//ApplicationLauncher.logger.debug("ProjectExecutionController: Test2");
		disableGuiObject();
		//ApplicationLauncher.logger.debug("ProjectExecutionController: Test3");
		if(ProcalFeatureEnable.PROPOWER_SRC_ONLY ){
			//ApplicationLauncher.logger.debug("ProjectExecutionController: Test4");
			Platform.runLater(() -> {
				ref_btnLoadNext.setVisible(true);
				//ref_btnLoadNext.setDisable(true);
				ref_btn_execution_start.setVisible(false);
				ref_btn_Resume.setVisible(false);
				ref_txtManualSetTime.setText(String.valueOf(ConstantAppConfig.PROPOWER_MANUAL_MODE_DEFAULT_TIMER_INPUT));


			});
			disableBtnLoadNext();
			//ref_tabManualMode.setDisable(true);
			enableTabManualMode();
			disableTxtManualSetTimeBlock();

			ref_rdBtnManualModeSinglePhase.setSelected(false);
			ref_rdBtnManualModeThreePhase.setSelected(true);
			ref_rdBtnManualModeImport.setSelected(true);
			ref_rdBtnManualModeExport.setSelected(false);
			ref_rdBtnManualModeRphasePfLag.setSelected(true);
			ref_rdBtnManualModeYphasePfLag.setSelected(true);
			ref_rdBtnManualModeBphasePfLag.setSelected(true);

			ref_rdBtnManualModeRphasePfLead.setSelected(false);
			ref_rdBtnManualModeYphasePfLead.setSelected(false);
			ref_rdBtnManualModeBphasePfLead.setSelected(false);
			disableBtnManualModeStop();
			disableBtnManualModeUpdate();
			AutoFill_Voltage();
			AutoFill_Current();
			AutoFill_Phase();
		}
		
		if(ProcalFeatureEnable.PROCAL_LAB_MODE ){
			hideTabSequenceMode();
			if(!ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED) {
				Platform.runLater(() -> {

					ref_rdBtnManualModeSinglePhase.setSelected(true);
					ref_rdBtnManualModeThreePhase.setSelected(false);
					ref_chkBxManualModeBalanced.setSelected(false);
					ref_chkBxManualModeBalanced.setDisable(true);
					ref_rdBtnManualModeThreePhase.setDisable(true);

				});
			}
				
		}
		
		if(ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED){
			DeviceDataManagerController.setReadProPowerAllDataNoOfVariables(6);
			
		}else{
			DeviceDataManagerController.setReadProPowerAllDataNoOfVariables(4);
		}
		
		if(ConstantAppConfig.INSTANT_METRICS_ENERGY_DISPLAY_IN_KWH){
			Platform.runLater(()->{
				ref_labelRefStdDisplayActiveReactiveUnit_R.setText("kWh");
				
			});
		}
		//ApplicationLauncher.logger.debug("ProjectExecutionController: TestX");
	}  

	public void AutoFill_Voltage(){
		ref_txt_ManualModeRphaseVoltage.textProperty().addListener((observable, oldValue, newValue) -> {
			if(ref_chkBxManualModeBalanced.isSelected()){
				if(rdBtnManualModeThreePhase.isSelected()){
					ref_txt_ManualModeYphaseVoltage.setText(ref_txt_ManualModeRphaseVoltage.getText());
					ref_txt_ManualModeBphaseVoltage.setText(ref_txt_ManualModeRphaseVoltage.getText());
				}
			}
		});
	}

	public void AutoFill_Current(){
		ref_txt_ManualModeRphaseCurrent.textProperty().addListener((observable, oldValue, newValue) -> {
			if(ref_chkBxManualModeBalanced.isSelected()){
				if(rdBtnManualModeThreePhase.isSelected()){
					ref_txt_ManualModeYphaseCurrent.setText(ref_txt_ManualModeRphaseCurrent.getText());
					ref_txt_ManualModeBphaseCurrent.setText(ref_txt_ManualModeRphaseCurrent.getText());
				}
			}
		});
	}

	public void AutoFill_Phase(){
		ref_txt_ManualModeRphasePfValue.textProperty().addListener((observable, oldValue, newValue) -> {
			if(ref_chkBxManualModeBalanced.isSelected()){
				if(rdBtnManualModeThreePhase.isSelected()){
					ref_txt_ManualModeYphasePfValue.setText(ref_txt_ManualModeRphasePfValue.getText());
					ref_txt_ManualModeBphasePfValue.setText(ref_txt_ManualModeRphasePfValue.getText());
				}
			}

		});

	}

	public void disableChkBxManualModeBalanced(){
		Platform.runLater(() -> {
			try{
				ref_chkBxManualModeBalanced.setDisable(true);
			}catch (Exception e) {
					
					e.printStackTrace();
					ApplicationLauncher.logger.error("Init : disableChkBxManualModeBalanced: Exception :" +e.getMessage());
			}

		});
	}

	public void enableChkBxManualModeBalanced(){
		Platform.runLater(() -> {
			if(ref_rdBtnManualModeThreePhase.isSelected()){
				ref_chkBxManualModeBalanced.setDisable(false);
			}

		});
	}


	public void disableTabSequenceMode(){
		Platform.runLater(() -> {

			ref_tabSequenceMode.setDisable(true);

		});
	}

	public void enableTabSequenceMode(){
		Platform.runLater(() -> {

			ref_tabSequenceMode.setDisable(false);

		});
	}

	public void disableBtnManualModeStart(){
		Platform.runLater(() -> {

			ref_btnManualModeStart.setDisable(true);

		});
	}

	public void enableBtnManualModeStart(){
		Platform.runLater(() -> {

			ref_btnManualModeStart.setDisable(false);

		});
	}

	public void disableBtnManualModeStop(){
		Platform.runLater(() -> {

			ref_btnManualModeStop.setDisable(true);

		});
	}

	public void enableBtnManualModeStop(){
		Platform.runLater(() -> {

			ref_btnManualModeStop.setDisable(false);

		});
	}

	public void disableBtnManualModeUpdate(){
		Platform.runLater(() -> {

			ref_btnManualModeUpdate.setDisable(true);

		});
	}

	public void enableBtnManualModeUpdate(){
		Platform.runLater(() -> {

			ref_btnManualModeUpdate.setDisable(false);

		});
	}

	public void disableTabManualMode(){
		Platform.runLater(() -> {

			ref_tabManualMode.setDisable(true);

		});
	}

	public void enableTabManualMode(){
		Platform.runLater(() -> {

			ref_tabManualMode.setDisable(false);

		});
	}
	


	public void hideTabSequenceMode(){
		Platform.runLater(() -> {

			ref_tabPaneRunMode.getTabs().remove(0);

		});
	}



	public void disableTxtManualSetTimeBlock(){
		Platform.runLater(() -> {

			ref_txtManualSetTime.setDisable(true);
			ref_lbl_setTime.setDisable(true);
			ref_lbl_targetTime.setDisable(true);
			ref_lbl_setTimeSec.setDisable(true);
			ref_lbl_targetTimeSec.setDisable(true);

		});
	}

	public void enableTxtManualSetTimeBlock(){
		Platform.runLater(() -> {

			ref_txtManualSetTime.setDisable(false);
			ref_lbl_setTime.setDisable(false);
			ref_lbl_targetTime.setDisable(false);
			ref_lbl_setTimeSec.setDisable(false);
			ref_lbl_targetTimeSec.setDisable(false);

		});
	}

	public static void disableBtnLoadNext(){
		Platform.runLater(() -> {
			//ApplicationLauncher.logger.debug("disableBtnLoadNext: 1"); 
			ref_btnLoadNext.setDisable(true);
			//ApplicationLauncher.logger.debug("disableBtnLoadNext: 2");
		});
	}

	public void enableBtnLoadNext(){
		Platform.runLater(() -> {
			//ApplicationLauncher.logger.debug("enableBtnLoadNext: 1"); 
			ref_btnLoadNext.setDisable(false);
			//ApplicationLauncher.logger.debug("enableBtnLoadNext: 2"); 
		});
	}

	public void disableGuiObject(){

		if(!ProcalFeatureEnable.MAIN_CT_FEATURE_ENABLED){
			disableMainCtModeRadioButton();
		}

		if(!ProcalFeatureEnable.NEUTRAL_CT_FEATURE_ENABLED){
			disableNeutralCtModeRadioButton();
		}
		
		if(!ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED){
			if(ProcalFeatureEnable.PROPOWER_SRC_ONLY){
				Platform.runLater(()->{
					
					try{
						ref_rdBtnManualModeThreePhase.setDisable(true);
					}catch (Exception e) {
						
						e.printStackTrace();
						ApplicationLauncher.logger.error("Init : ref_rdBtnManualModeThreePhase: Exception :" +e.getMessage());
					}
					try{
						ref_rdBtnManualModeThreePhase.setSelected(false);
					}catch (Exception e) {
						
						e.printStackTrace();
						ApplicationLauncher.logger.error("Init : ref_rdBtnManualModeThreePhase: Exception :" +e.getMessage());
					}
					try{
						ref_rdBtnManualModeSinglePhase.setSelected(true);
					}catch (Exception e) {
						
						e.printStackTrace();
						ApplicationLauncher.logger.error("Init : ref_rdBtnManualModeSinglePhase: Exception :" +e.getMessage());
					}
					try{
						disableManualModeYandBphaseInput();
					}catch (Exception e) {
						
						e.printStackTrace();
						ApplicationLauncher.logger.error("Init : disableManualModeYandBphaseInput: Exception :" +e.getMessage());
					}
					try{
						disableChkBxManualModeBalanced();
					}catch (Exception e) {
						
						e.printStackTrace();
						ApplicationLauncher.logger.error("Init : disableChkBxManualModeBalanced: Exception :" +e.getMessage());
					}
				});
			}
		}

	}	

	public void hideGuiObject(){
		Platform.runLater(() -> {
			if(ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED){
				ref_btn_Resume.setVisible(false);
				ref_btn_execution_start.setVisible(false);
			}else{
				ref_btnLoadNext.setVisible(false);
				ref_btnCalibConfigRefresh.setVisible(false);
				ref_btnSourceRefresh.setVisible(false);
			}
			ref_btnCalibConfigRefresh.setVisible(false);
		});
	}

	public void bindPropertytoTextField(){

		ref_txtRefStdDisplayR_PhaseVolt.textProperty().bind(refStdDisplayR_PhaseVolt);
		ref_txtRefStdDisplayR_PhaseCurrent.textProperty().bind(refStdDisplayR_PhaseCurrent);
		ref_txtRefStdDisplayR_PhasePowerFactor.textProperty().bind(refStdDisplayR_PhasePowerFactor);
		ref_txtRefStdDisplayR_PhaseDegreePhase.textProperty().bind(refStdDisplayR_PhaseDegreePhase);
		ref_txtRefStdDisplayR_PhaseFreq.textProperty().bind(refStdDisplayR_PhaseFreq);
		ref_txtRefStdDisplayR_PhaseWatt.textProperty().bind(refStdDisplayR_PhaseWatt);
		ref_txtRefStdDisplayR_PhaseVA.textProperty().bind(refStdDisplayR_PhaseVA);
		ref_txtRefStdDisplayR_PhaseVAR.textProperty().bind(refStdDisplayR_PhaseVAR);
		ref_txtRefStdDisplayR_PhaseWh.textProperty().bind(refStdDisplayR_PhaseWh);




	}

	public static String getSelectedDeployment_ID() {
		return selectedDeployment_ID;
	}

	public static void setSelectedDeployment_ID(String selectedDeploymentID) {
		selectedDeployment_ID = selectedDeploymentID;
	}

	public void setupProjectData(){

		//ApplicationLauncher.setCursor(Cursor.WAIT);
		Platform.runLater(() -> {
			setProjectNameAsAppTitle();
		});
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());;
		calcEpoch(timeStamp);

		//ProjectExecutionController.setProjectStartTime(starttime);
		String project_name = getCurrentProjectName();
		JSONObject modelparams = getModelParameters(project_name);
		String model_type="";
		try {
			model_type = modelparams.getString("model_type");
		} catch (JSONException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("startDeploy : Exception2 pn setting model_type:" +e.getMessage());
		}
		DeviceDataManagerController.setDeployedEM_ModelType(model_type);	
		String CT_type="";
		try {
			CT_type = modelparams.getString("ct_type");
		} catch (JSONException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("startDeploy : Exception3: setting ct_type:" +e.getMessage());
		}
		DeviceDataManagerController.setDeployedEM_CT_Type(CT_type);
		/*		String project_name = ProjectExecutionController.getCurrentProjectName();
		String deploymentId = ProjectExecutionController.getSelectedDeployment_ID();*/
		DisplayDataObj.setDeployedDevicesJson(getCurrentProjectName(),getSelectedDeployment_ID());
		/*		if(!ProcalFeatureEnable.TEST_ENABLE_FEATURE){
			DisplayDataObj.setEnergyFlowMode(ConstantMtePowerSource.IMPORT_MODE);
		}else{
			DisplayDataObj.setEnergyFlowMode(ConstantMtePowerSource.EXPORT_MODE);
		}*/

		DisplayDataObj.PowerSourceEnergyFlowModeDataInit();

		if(isMainCtMode()){
			DeviceDataManagerController.setExecutionMctNctMode(ConstantReport.RESULT_EXECUTION_MODE_MAIN_CT);
		}else if(isNeutralCtMode()){
			DeviceDataManagerController.setExecutionMctNctMode(ConstantReport.RESULT_EXECUTION_MODE_NEUTRAL_CT);
		}

		List<String> CurrentActiveRackList = ProjectExecutionController.getColNamesFromDB(getCurrentProjectName(),getSelectedDeployment_ID());
		if(ProcalFeatureEnable.PROCON_INTERFACE_ENABLED) {
			CurrentActiveRackList = DisplayDataObj.manipulateConveyorDutSerialNumber(CurrentActiveRackList);
		}
		updateDutSerialNo(new ArrayList<String>(CurrentActiveRackList));
		//DB_EndTime = LocalDateTime.now();
		//ApplicationLauncher.logger.debug("startDeployTask: End Time3: "+dtf.format(DB_EndTime));

		//ApplicationLauncher.logger.debug("startDeployTask: Difference Time3: "+ TestReportController.DiffTime(dtf.format(DB_StartTime),dtf.format(DB_EndTime)));

		LiveTableDataManager.ResetliveTableData();
		LiveTableDataManager.setActiveRackList(CurrentActiveRackList);
		LiveTableDataManager.InitLiveTableData();
		//DB_EndTime = LocalDateTime.now();
		//ApplicationLauncher.logger.debug("startDeployTask: End Time4: "+dtf.format(DB_EndTime));

		//ApplicationLauncher.logger.debug("startDeployTask: Difference Time4: "+ TestReportController.DiffTime(dtf.format(DB_StartTime),dtf.format(DB_EndTime)));

		//LoadDeviceDataToDB_Result();
		//DB_EndTime = LocalDateTime.now();
		//ApplicationLauncher.logger.debug("startDeployTask: End Time5: "+dtf.format(DB_EndTime));

		//ApplicationLauncher.logger.debug("startDeployTask: Difference Time5: "+ TestReportController.DiffTime(dtf.format(DB_StartTime),dtf.format(DB_EndTime)));

		//SaveProjectRunStartTime(project_name);
		DeviceDataManagerController.setError_count(1);
		DisplayDataObj.CheckAllMeterConstSame();
		//ApplicationLauncher.setCursor(Cursor.DEFAULT);
	}

	public void setProjectEM_ModelType(String type){

		ApplicationLauncher.logger.debug("setProjectEM_ModelType:"+type);
		projectEM_ModelType = type;

	}	


	public  String getProjectEM_ModelType(){
		return projectEM_ModelType;

	}




	public void updateSelectedProjectSetting() {
		ApplicationLauncher.logger.debug("updateSelectedProjectSetting: Entry");
		try {
			if (getSelectedProjectIndex() > 0) {

				// int selectedIndex = getSelectedProjectIndex();
				int selectedIndex = getSelectedProjectIndex() - 1;// updated due to change in the insertion of text
				// "Select" in serial number
				ApplicationLauncher.logger
				.debug("updateSelectedProjectSetting: getProjectNameList:" + getProjectNameList());
				setCurrentProjectName(getProjectNameList().get(selectedIndex));
				ApplicationLauncher.logger
				.debug("updateSelectedProjectSetting: getCurrentProjectName:" + getCurrentProjectName());

				//String deployMode = getDeployModeList().get(selectedIndex);

				setSelectedDeployment_ID(getDeploymentIdList().get(selectedIndex));
				if(getEnergyFlowModeList().get(selectedIndex).equals(ConstantPowerSourceMte.IMPORT_MODE)){
					ApplicationLauncher.logger.debug("setEnergyFlowMode: setting to Import mode");
					DeviceDataManagerController.setEnergyFlowMode(ConstantPowerSourceMte.IMPORT_MODE);
					ApplicationLauncher.logger.debug("setEnergyFlowMode: setting to Export mode");
				}else if(getEnergyFlowModeList().get(selectedIndex).equals(ConstantPowerSourceMte.EXPORT_MODE)){
					DeviceDataManagerController.setEnergyFlowMode(ConstantPowerSourceMte.EXPORT_MODE);
				}else{
					ApplicationLauncher.logger.debug("setEnergyFlowMode: defaulting to Import mode");
					DeviceDataManagerController.setEnergyFlowMode(ConstantPowerSourceMte.IMPORT_MODE);
				}
				ApplicationLauncher.logger.debug("updateSelectedProjectSetting: : getSelectedDeployment_ID: " + getSelectedDeployment_ID());
				ApplicationLauncher.logger.debug("updateSelectedProjectSetting: getSelectedDeployment_ID: " + getCurrentProjectName());
				ApplicationLauncher.logger.debug("updateSelectedProjectSetting: selectedIndex: " + selectedIndex);
				//ApplicationLauncher.logger.debug("updateSelectedProjectSetting: Entry");
				setupProjectData();
				ApplicationLauncher.logger.debug("updateSelectedProjectSetting: Test2:");
				displayAppropriateLabelForInstantMetricsGUI();
				ApplicationLauncher.logger.debug("updateSelectedProjectSetting: Test3:");
				setProjectEM_ModelType(getModelType(getCurrentProjectName()));
				ApplicationLauncher.logger.debug("updateSelectedProjectSetting: Test4:");
				ApplicationLauncher.logger.debug("updateSelectedProjectSetting: getProjectEM_ModelType:" + getProjectEM_ModelType());

				if(getAutoDeployEnabledList().get(selectedIndex)){
					ApplicationHomeController.updateBottomSecondaryStatus("Auto Deploy Enabled",ConstantApp.LEFT_STATUS_INFO);
					setCurrentProjectAutoDeployEnabled(true);
				}else {
					ApplicationHomeController.updateBottomSecondaryStatus("",ConstantApp.LEFT_STATUS_INFO);
					setCurrentProjectAutoDeployEnabled(false);
				}
				if(ProcalFeatureEnable.PROCON_INTERFACE_ENABLED ) {
					updateProjectRunId();
				}
				//updateMetricLabelGUI();
				/*				if (deployMode.equals(ConstantApp.DEPLOYMENT_CT_MODE)) {
					setSelectedTestExecutionMode(ConstantApp.DEPLOYMENT_CT_MODE);
					setCtMode(true);
					setPtMode(false);
					ref_radioBtn_CT_Mode.setSelected(true);
					ref_radioBtn_PT_Mode.setSelected(false);
					ref_radioBtn_CT_Mode.setDisable(false);
					ref_radioBtn_PT_Mode.setDisable(true);
				} else if (deployMode.equals(ConstantApp.DEPLOYMENT_PT_MODE)) {
					setSelectedTestExecutionMode(ConstantApp.DEPLOYMENT_PT_MODE);
					setCtMode(false);
					setPtMode(true);
					ref_radioBtn_CT_Mode.setSelected(false);
					ref_radioBtn_PT_Mode.setSelected(true);
					ref_radioBtn_CT_Mode.setDisable(true);
					ref_radioBtn_PT_Mode.setDisable(false);
				} else if (deployMode.equals(ConstantApp.DEPLOYMENT_PT_AND_CT_MODE)) {
					setSelectedTestExecutionMode(ConstantApp.DEPLOYMENT_CT_MODE);
					setCtMode(true);
					setPtMode(false);
					ref_radioBtn_CT_Mode.setSelected(true);
					ref_radioBtn_PT_Mode.setSelected(false);
					ref_radioBtn_CT_Mode.setDisable(false);
					ref_radioBtn_PT_Mode.setDisable(false);
				}*/

			} else {

				ApplicationLauncher.logger
				.debug("updateSelectedProjectSetting: Clearing projectName and deplymentId" );
				setCurrentProjectName("");
				setSelectedDeployment_ID("");
				//LiveTableDataManager.ResetliveTableData();
			}
		} catch (Exception e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("updateSelectedProjectSetting: Exception: " + e.getMessage());
		}

	}

	private void updateProjectRunId() {
		
		String deploymentId = getSelectedDeployment_ID();
		ProjectRun projectRun = DisplayDataObj.getProjectRunService().findActiveByDeploymentId(deploymentId);
		if(projectRun!=null) {
			setPresentProjectRunId(String.valueOf(projectRun.getTestRunId()));
			ApplicationLauncher.logger.debug("updateProjectRunId: ProjectRunId: " + getPresentProjectRunId());
		}
		
	}
	
	private void closeProjectRunId(String deploymentId,String projectName) {
		
		ApplicationLauncher.logger.debug("closeProjectRunId: deploymentId : " + deploymentId);
		ApplicationLauncher.logger.debug("closeProjectRunId: projectName : " + projectName);
		try {
		ProjectRun projectRun = DisplayDataObj.getProjectRunService().findActiveByDeploymentId(deploymentId);
		if(projectRun!=null) {
			projectRun.setExecutionStatus(ConstantApp.EXECUTION_STATUS_COMPLETED);
			long epochEndTime = Instant.now().toEpochMilli()/1000;		
			
			projectRun.setEpochEndTime(epochEndTime);
			
			Date date1 = new Date(epochEndTime * 1000 );

			// format of the date  

			SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String stopTime = jdf.format(date1);
			projectRun.setEndTime(stopTime);
			
			DisplayDataObj.getProjectRunService().saveToDb(projectRun);
			setPresentProjectRunId("");
			ApplicationLauncher.logger.debug("closeProjectRunId: closed ProjectRunId: " + projectRun.getTestRunId());
			
			
			ProjectRun newProjectRun = new ProjectRun();
			newProjectRun.setDeploymentId(deploymentId);
			
			long epochStartTime = (Instant.now().toEpochMilli()/1000)+5;		
			
			newProjectRun.setEpochStartTime(epochStartTime);
			
			Date date = new Date(epochStartTime * 1000 );

			// format of the date  

			SimpleDateFormat jdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			String startTime = jdf2.format(date);
			newProjectRun.setStartTime(startTime);
			newProjectRun.setExecutionStatus(ConstantApp.EXECUTION_STATUS_NOT_STARTED);
			newProjectRun.setProjectName(projectName);
			long runId = DisplayDataObj.getProjectRunService().saveToDb(newProjectRun);
			
			
			ApplicationLauncher.logger.debug("closeProjectRunId: created new ProjectRunId: " + runId);
		}
		}catch (Exception e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("closeProjectRunId : Exception:" +e.getMessage());
		}
		
		
	}

	public static String getModelType(String project_name){
		int model_id = MySQL_Controller.sp_getProjectModel_ID(project_name);
		ApplicationLauncher.logger.debug("getModelType : project_name:" +project_name);
		ApplicationLauncher.logger.debug("getModelType : model_id:" +model_id);
		JSONObject model_data = MySQL_Controller.sp_getem_model_data(model_id);



		String model_type=null;
		try {
			model_type = model_data.getString("model_type");
		} catch (JSONException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("getModelType : JSONException:" +e.getMessage());
		}
		return model_type;
	}

	public JSONObject GetResultsFromDB() {
		ApplicationLauncher.logger.debug("GetResultsFromDB: Entry");

		JSONObject result = new JSONObject();
		// String project_name =
		// get_cmbBoxProjectListCurrentValue();//cmbBoxProjectList.getValue()+".0";

		long from_epoch = 100;
		long to_epoch = 200;
		/*		  String  mctNctMode = ConstantReport.RESULT_EXECUTION_MODE_MAIN_CT;;//isMainCtMode();
		  //String  energyMode = DisplayDataObj.getEnergyFlowMode();//ConstantReport.RESULT_EXECUTION_MODE_ENERYGY_IMPORT;
		  if(DisplayDataObj.getEnergyFlowMode().equals(ConstantMtePowerSource.IMPORT_MODE)){
			  energyMode = ConstantReport.RESULT_EXECUTION_MODE_ENERYGY_IMPORT;
		  }else if(DisplayDataObj.getEnergyFlowMode().equals(ConstantMtePowerSource.EXPORT_MODE)){
			  energyMode = ConstantReport.RESULT_EXECUTION_MODE_ENERYGY_EXPORT;

		  }
		  if(isMainCtMode()){
			  mctNctMode = ConstantReport.RESULT_EXECUTION_MODE_MAIN_CT;
		  }else if(isNeutralCtMode()){
			  mctNctMode = ConstantReport.RESULT_EXECUTION_MODE_NEUTRAL_CT;
		  }*/
		ApplicationLauncher.logger.debug("GetResultsFromDB: getCurrentProjectName(): " + getCurrentProjectName());
		ApplicationLauncher.logger.debug("GetResultsFromDB: RESULT_DATA_TYPE_ERROR_VALUE:" + ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
		ApplicationLauncher.logger.debug("GetResultsFromDB: getSelectedDeployment_ID():" + getSelectedDeployment_ID());
		ApplicationLauncher.logger.debug("GetResultsFromDB: getExecutionMctNctMode: "+DeviceDataManagerController.getExecutionMctNctMode());
		ApplicationLauncher.logger.debug("GetResultsFromDB: getEnergyFlowMode: "+DeviceDataManagerController.getEnergyFlowMode());
		
		if(ProcalFeatureEnable.PROCON_INTERFACE_ENABLED) {
			result = MySQL_Controller.sp_getresult_dataWithRunId(from_epoch, to_epoch, getCurrentProjectName(),
					ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE, getSelectedDeployment_ID(),
					DeviceDataManagerController.getExecutionMctNctMode(),
					DeviceDataManagerController.getEnergyFlowMode(),getPresentProjectRunId());
			
		}else {
			result = MySQL_Controller.sp_getresult_data(from_epoch, to_epoch, getCurrentProjectName(),
					ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE, getSelectedDeployment_ID(),
					DeviceDataManagerController.getExecutionMctNctMode(),
					DeviceDataManagerController.getEnergyFlowMode());
		}
		// ApplicationLauncher.logger.debug("GetResultsFromDB:" + result);
		return result;
	}

	public void refreshDataFromResultDB() throws JSONException {
		// (int LDU_ReadAddress,String Resultstatus,String ErrorValue);
		// setCurrentProject_TotalNoOfTestPoint(getCurrentProjectTestPointList().length());
		/*
		 * setCurrentTestPoint_Index(0); DeviceErrorDisplayUpdate( 1,"P","0.00");
		 * setCurrentTestPoint_Index(1); DeviceErrorDisplayUpdate( 1,"P","0.01");
		 * setCurrentTestPoint_Index(2); DeviceErrorDisplayUpdate( 1,"F","0.02");
		 * setCurrentTestPoint_Index(0); DeviceErrorDisplayUpdate( 2,"P","0.05");
		 */
		// setSelectedDeployment_ID("12345");
		JSONObject result = GetResultsFromDB();
		ApplicationLauncher.logger
		.info("refreshDataFromResultDB: CurrentProjectTestPoint_List: " + getCurrentProjectTestPointList());
		//ApplicationLauncher.logger.info("refreshDataFromResultDB: result: " + result);

		JSONArray results = result.getJSONArray("Results");
		JSONObject result_ = new JSONObject();
		JSONObject currentprojecttestpoint_ = new JSONObject();
		String completed_test_case = "";
		String current_project_test_case = "";
		String errorValue = "";
		String resultStatus = "";
		String lduAddress = "";
		//int totalTestInCompletedStatus = 0;
		try {
			if (result.getInt("No_of_results") != 0) {
				for (int i = 0; i < results.length(); i++) {
					// ApplicationLauncher.logger.info("refreshDataFromResultDB: i = "+i);
					result_ = (JSONObject) results.get(i);
					// ApplicationLauncher.logger.info("refreshDataFromResultDB: result_2:
					// "+result_);
					completed_test_case = result_.getString("test_case_name");
					ApplicationLauncher.logger.debug("refreshDataFromResultDB: completed_test_case: "+completed_test_case);
					for (int j = 0; j < CurrentProjectTestPoint_List.length(); j++) {
						// ApplicationLauncher.logger.info("refreshDataFromResultDB: j = "+j);
						currentprojecttestpoint_ = (JSONObject) CurrentProjectTestPoint_List.get(j);
						current_project_test_case = currentprojecttestpoint_.getString("test_case");
						ApplicationLauncher.logger.debug("refreshDataFromResultDB: current_project_test_case: "+current_project_test_case);
						if (current_project_test_case.equals(completed_test_case)) {
							setCurrentTestPoint_Index(j);
							ApplicationLauncher.logger.debug("refreshDataFromResultDB: lduAddress: "+lduAddress);
							//lduAddress = result_.getString("device_name");
							lduAddress = String.valueOf(result_.getInt("device_name"));
							ApplicationLauncher.logger.debug("refreshDataFromResultDB: resultStatus: "+resultStatus);
							resultStatus = result_.getString("test_status") + " ";
							ApplicationLauncher.logger.debug("refreshDataFromResultDB: errorValue: "+errorValue);
							errorValue = result_.getString("error_value");
							if(lduAddress.equals(ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID)){
								deviceExecutionStatusDisplayUpdate(ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID, resultStatus,errorValue);
								
							}else {
								if(current_project_test_case.startsWith(ConstantApp.EXPORT_MODE_ALIAS_NAME)){
									lduAddress = String.valueOf(Integer.parseInt(lduAddress) - ConstantApp.EXPORT_MODE_DEVICE_ID_THRESHOLD );
									ApplicationLauncher.logger.debug("refreshDataFromResultDB: lduAddress2: "+lduAddress);
								}
								DeviceErrorDisplayUpdate(Integer.parseInt(lduAddress), resultStatus,errorValue);
							}
							//		actualSecPf);
							//executionStatus = result_.getString("execution_status");
							/*							  ratio_error = result_.getString("ratio_error");
							  phase_error = result_.getString("phase_error");
							  ratio_error_status = result_.getString("ratio_error_status");
							  phase_error_status = result_.getString("phase_error_status");
							  actualSecBurdenPower = result_.getString("actual_sec_burden");
							  actualSecPf = result_.getString("actual_sec_pf");
							  actualPriValue = result_.getString("actual_pri_value");
							  resultStatus = result_.getString("test_result");
							  actualPrimaryLoadInPercentage =  result_.getString("actual_load_percent");

							  DeviceErrorDisplayUpdate(ConstantProGEN_App.LIVE_TABLE_RATIO_ERROR_COLUMN_ID,
									  ratio_error_status, ratio_error);
							  DeviceErrorDisplayUpdate(ConstantProGEN_App.LIVE_TABLE_PHASE_ERROR_COLUMN_ID,
									  phase_error_status, phase_error);
							  DeviceErrorDisplayUpdate(ConstantProGEN_App.LIVE_TABLE_EXECUTION_STATUS_COLUMN_ID, "",
									  executionStatus);*/


							//DeviceErrorDisplayUpdate(ConstantProGEN_App.LIVE_TABLE_ACTUAL_BURDEN_COLUMN_ID, "",
							//		actualSecBurdenPower);
							//updateActualBurdenOnLiveTable(actualSecBurdenPower,"");
							//updateActualSecPfOnLiveTable(actualSecPf,"");
							//UpdateMeasuredValueOnLiveTable(actualPriValue,"");
							//updateTestStatusOnLiveTable(resultStatus,updateResultData);
							//updateActualLoadOnLiveTable(actualPrimaryLoadInPercentage,"");
							/*ApplicationLauncher.logger.info("refreshDataFromResultDB: executionStatus: "+executionStatus);
							if(executionStatus.equals(ConstantProGEN_App.EXECUTION_STATUS_COMPLETED)){
								totalTestInCompletedStatus++;
							}*/
							//DeviceErrorDisplayUpdate(ConstantProGEN_App.LIVE_TABLE_ACTUAL_COS_BETA_COLUMN_ID, "",
							//		actualSecPf);
							//DeviceErrorDisplayUpdate(ConstantProGEN_App.LIVE_TABLE_ACTUAL_VALUE_COLUMN_ID, "",
							//		actualPriValue);
							//DeviceErrorDisplayUpdate(ConstantProGEN_App.LIVE_TABLE_TEST_STATUS_COLUMN_ID, "",
							//		resultStatus);


							break;
						}
					}
					// FilterResultByTestType(result,CurrentProjectTestPoint_List.get(0));
					// setCurrentTestPoint_Index(i);
				}
				//manipulateProjectStatus(totalTestInCompletedStatus);
				// setCurrentTestPoint_Index(0);
			} else {
				//manipulateProjectStatus(totalTestInCompletedStatus);
				ApplicationLauncher.logger.info("refreshDataFromResultDB: Result not found in DB, for deployment id: " + getSelectedDeployment_ID() + " : runid: " +getPresentProjectRunId());
				ApplicationHomeController.update_left_status("Result not found", ConstantApp.LEFT_STATUS_DEBUG);

				// ApplicationLauncher.InformUser("Export Failed", "Result not found!!",
				// AlertType.ERROR);
			}
			refreshProjectStatusGraphGUI();
		} catch (JSONException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.info("refreshDataFromResultDB: JSONException:" + e.getMessage());
		}
		setCurrentTestPoint_Index(0);
	}

	public void refreshProjectStatusGraphGUI(){// noOfTestPointCompleted){
		int noOfTestPointCompleted = LiveTableDataManager.getCountOfNoOfTestPointCompleted();
		ApplicationLauncher.logger.debug("refreshProjectStatusGraphGUI: noOfTestPointCompleted: " + noOfTestPointCompleted);

		//int totoalNoOfTestPoint = getCurrentProject_TotalNoOfTestPoint();
		ProjectStatusRefresh.ProjectProgressBarRefresh(ref_barProjectStatus, ref_barPinProjectStatus,
				noOfTestPointCompleted);


	}

	public void updateLiveTable() {
		LiveTableDataManager.ResetliveTableData();
		// LiveTableDataManager.setActiveRackList(CurrentActiveRackList);
		List<String> CurrentActiveRackList = getColNamesFromDB(getCurrentProjectName(),getSelectedDeployment_ID());
		if(ProcalFeatureEnable.PROCON_INTERFACE_ENABLED) {
			CurrentActiveRackList = DisplayDataObj.manipulateConveyorDutSerialNumber(CurrentActiveRackList);
		}
		updateDutSerialNo(new ArrayList<String>(CurrentActiveRackList));
		LiveTableDataManager.setActiveRackList(CurrentActiveRackList);//getColNamesForErrorDisplay());
		LiveTableDataManager.InitLiveTableData();
		// loadDevices();
		setup_live_table();
		setCurrentProject_TotalNoOfTestPoint(getCurrentProjectTestPointList().length());
		setProjectBarMax();
		//updateTargetValueOnLiveTable();
		try {
			refreshDataFromResultDB();
		} catch (JSONException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("updateLiveTable : JSONException:" + e.getMessage());
		}
	}

	public void refreshLduSecDisplayTestPointList(){

		List<String> testPointList = new ArrayList<String>();
		JSONObject currentprojecttestpoint_ = new JSONObject();
		String eachTestPoint = "";
		try {
			for (int j = 0; j < CurrentProjectTestPoint_List.length(); j++) {
				// ApplicationLauncher.logger.info("refreshDataFromResultDB: j = "+j);

				currentprojecttestpoint_ = (JSONObject) CurrentProjectTestPoint_List.get(j);

				eachTestPoint = currentprojecttestpoint_.getString("test_case");
				ApplicationLauncher.logger.info("refreshLduSecDisplayTestPointList: eachTestPoint: "+eachTestPoint);
				testPointList.add(eachTestPoint);
			}
			if(eachTestPoint.length()>0){
				SecondaryLduDisplayController.refreshLduSecondarydisplayTestPointData(testPointList);
			}
		} catch (JSONException e) {
			
			e.printStackTrace();
		}


	}

	public void updateLduSecondaryDisplay(){

		if (getSelectedProjectIndex() > 0) {
			refreshLduSecDisplayTestPointList();
			lscsClearLduSecondaryDisplay();
		}else{
			lscsClearLduSecondaryDisplayTestPoint();
			lscsClearLduSecondaryDisplay();
		}
	}

	public void refreshAllDataInGUI() {

		int projectIndex = ref_cmbBoxSelectProject.getSelectionModel().getSelectedIndex();
		setSelectedProjectIndex(projectIndex);
		updateSelectedProjectSetting();
		//updateSelectedPTCT_ModelData();
		updateLiveTable();
		setProjectNameAsAppTitle();
		if(ProcalFeatureEnable.SECONDARY_LDU_DISPLAY_ENABLED){
			updateLduSecondaryDisplay();
		}
		/*		  updateProfileDisplay();
		  updateLvdProfileData();
		  if(isPtMode()){
			  enablePT_ProfileGUI_Objects();
		  }else if(isCtMode()){
			  enableCT_ProfileGUI_Objects();
		  }else{
			  enableCT_ProfileGUI_Objects();
		  }*/
		//updateProfileDisplay();
		if (getCurrentProject_TotalNoOfTestPoint() > 0) {
			EnableStartButton();
			EnableViewLogsButton();
			EnableCloseProjectButton();
			EnableResumeButton();
			EnableStepRunButton();
		} else {
			DisableStartButton();
			DisableViewLogsButton();
			DisableCloseProjectButton();
			DisableResumeButton();
			DisableStepRunButton();
		}
		setLastSelectedProjectIndex(projectIndex);
		updateGui_MCT_NCT();
	}

	public void updateGui_MCT_NCT() {
		//Platform.runLater(() -> {
		String metertype = DeviceDataManagerController.getDeployedEM_ModelType();
		if(metertype.contains(ConstantApp.METERTYPE_SINGLEPHASE)){
			enableMainCtModeRadioButton();
			enableNeutralCtModeRadioButton();
		}else if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
			disableMainCtModeRadioButton();
			disableNeutralCtModeRadioButton();
		}

		//});
	}

	public void DisableCloseProjectButton() {
		Platform.runLater(() -> {
			ref_btnCloseProject.setDisable(true);
		});
	}

	public static void EnableCloseProjectButton() {

		if(isUacExecuteAuthorized()){
			Platform.runLater(() -> {

				ref_btnCloseProject.setDisable(false);

			});
		}
	}

	public static int getSelectedProjectIndex() {
		return selectedProjectIndex;
	}

	public static void setSelectedProjectIndex(int selectProjectIndex) {
		selectedProjectIndex = selectProjectIndex;
	}
	public static int getLastSelectedProjectIndex() {
		return lastSelectedProjectIndex;
	}

	public static void setLastSelectedProjectIndex(int lastselectProjectIndex) {
		lastSelectedProjectIndex = lastselectProjectIndex;
	}

	public static void set_labelActiveReactiveUnit_R(String InputData){
		ref_labelRefStdDisplayActiveReactiveUnit_R.setText(InputData);
	}

	public void displayAppropriateLabelForInstantMetricsGUI(){

		if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){

			if(DeviceDataManagerController.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)){
				if(ConstantAppConfig.INSTANT_METRICS_ENERGY_DISPLAY_IN_KWH){
					DisplayInstantMetricsController.set_labelActiveRectiveUnit_R("kWh");
					DisplayInstantMetricsController.set_labelActiveRectiveUnit_Y("kWh");
					DisplayInstantMetricsController.set_labelActiveRectiveUnit_B("kWh");
					set_labelActiveReactiveUnit_R("kWh");
				}else{
					DisplayInstantMetricsController.set_labelActiveRectiveUnit_R("Wh");
					DisplayInstantMetricsController.set_labelActiveRectiveUnit_Y("Wh");
					DisplayInstantMetricsController.set_labelActiveRectiveUnit_B("Wh");
					set_labelActiveReactiveUnit_R("Wh");
				}

			}else if(DeviceDataManagerController.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)){
				
				if(ConstantAppConfig.INSTANT_METRICS_ENERGY_DISPLAY_IN_KWH){
					DisplayInstantMetricsController.set_labelActiveRectiveUnit_R("kVARh");
					DisplayInstantMetricsController.set_labelActiveRectiveUnit_Y("kVARh");
					DisplayInstantMetricsController.set_labelActiveRectiveUnit_B("kVARh");
					set_labelActiveReactiveUnit_R("kVARh");
				}else{
					DisplayInstantMetricsController.set_labelActiveRectiveUnit_R("VARh");
					DisplayInstantMetricsController.set_labelActiveRectiveUnit_Y("VARh");
					DisplayInstantMetricsController.set_labelActiveRectiveUnit_B("VARh");
					set_labelActiveReactiveUnit_R("VARh");
				}
			}
		}
	}

	public void updateProjectListinGUI() {
		ApplicationLauncher.logger.debug("updateProjectListinGUI: Entry");
		// for(int i=0; i<getEquipmentSerialNoList().size();i++) {
		if (getProjectNameList().size() > 0) {
			ref_cmbBoxSelectProject.getItems().add("Select");
			ref_cmbBoxSelectProject.getItems().addAll(getProjectNameList());
			ref_cmbBoxSelectProject.getSelectionModel().select(0);
			if (getSelectedProjectIndex() == -1) {
				setSelectedProjectIndex(0);

			} else {
				ref_cmbBoxSelectProject.getSelectionModel().select(getSelectedProjectIndex());
			}

		}

	}

	public static void enableSelectProjectComboBox() {
		Platform.runLater(() -> {
			ref_cmbBoxSelectProject.setDisable(false);
		});
	}

	public static void disableSelectProjectComboBox() {
		Platform.runLater(() -> {
			ref_cmbBoxSelectProject.setDisable(true);
		});
	}

	public static void enableMainCtModeRadioButton() {
		if(ProcalFeatureEnable.MAIN_CT_FEATURE_ENABLED){
			Platform.runLater(() -> {
				ref_radioBtnMainCt.setDisable(false);
			});
		}
	}

	public static void enableNeutralCtModeRadioButton() {
		if(ProcalFeatureEnable.NEUTRAL_CT_FEATURE_ENABLED){
			Platform.runLater(() -> {
				ref_radioBtnNeutralCt.setDisable(false);
			});
		}
	}

	public static void disableMainCtModeRadioButton() {
		Platform.runLater(() -> {
			ref_radioBtnMainCt.setDisable(true);
		});
	}

	public static void disableNeutralCtModeRadioButton() {
		Platform.runLater(() -> {
			ref_radioBtnNeutralCt.setDisable(true); 
		});
	}

	@FXML
	public void closeProjectTrigger() {
		ApplicationLauncher.logger.info("closeProjectTrigger: Entry");
		closeProjectTimer = new Timer();
		closeProjectTimer.schedule(new closeProjectTaskClick(), 100);

	}

	class closeProjectTaskClick extends TimerTask {
		public void run() {
			//ApplicationLauncher.setCursor(Cursor.WAIT);
			//boolean response = ShowMeterPopupReading(bridge_balance_constant_time_in_seconds);
			Platform.runLater(() -> {
				boolean status = false;
				status = closeProjectConfirmation();
				if (status) {
					closeProjectTask();
				}
				closeProjectTimer.cancel();
			});

			//ApplicationLauncher.setCursor(Cursor.DEFAULT);

		}
	}

	public boolean closeProjectConfirmation() {

		String metertype = DeviceDataManagerController.getDeployedEM_ModelType();
		String message = "";
		if(metertype.contains(ConstantApp.METERTYPE_SINGLEPHASE)){
			message = "Once project is closed, cannot be retrieved for execution, "
					+ "kindly ensure all test points on the <Main CT type> and <Neutral CT type> are executed."
					+ "\n\nAre you sure to close the Project?";
		}else if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){

			message = "Once project is closed, cannot be retrieved for execution, "
					+ "\n\nAre you sure to close the Project?";
		}
		Alert alert = new Alert(AlertType.CONFIRMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
		alert.setTitle("Close Project");
		alert.setHeaderText("Confirmation");
		alert.setContentText(message);

		alert.getButtonTypes().clear();
		alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

		// Deactivate Defaultbehavior for yes-Button:
		Button yesButton = (Button) alert.getDialogPane().lookupButton(ButtonType.YES);
		yesButton.setDefaultButton(false);

		// Activate Defaultbehavior for no-Button:
		Button noButton = (Button) alert.getDialogPane().lookupButton(ButtonType.NO);
		noButton.setDefaultButton(true);

		final Optional<ButtonType> result = alert.showAndWait();
		return result.get() == ButtonType.YES;
	}

	public static void removeProjectNameList(String projectName) {
		ApplicationLauncher.logger.debug("removeProjectNameList: projectName: "+ projectName);
		projectNameList.remove(projectName);
		ApplicationLauncher.logger.debug("removeProjectNameList: projectNameList: "+ projectNameList);
	}


	/*public static List<String> getColNamesForErrorDisplay() {
			ApplicationLauncher.logger.debug("getColNamesForErrorDisplay : Entry");
			// JSONObject resultjson = new JSONObject();
			// ArrayList<List<String>> result = new ArrayList<List<String>>();
			List<String> col = new ArrayList<String>();
			// ApplicationLauncher.logger.debug("getColNamesForErrorDisplay:
			// ERROR_DISPLAY_COLUMN_LIST:"+ConstantProGEN_App.ERROR_DISPLAY_COLUMN_LIST);
			// col = ConstantProGEN_App.ERROR_DISPLAY_COLUMN_LIST;
			for (int i = 0; i < ConstantProGEN_App.EXECUTION_TABLE_DISPLAY_COLUMN_LIST.length; i++) {
				col.add(ConstantProGEN_App.EXECUTION_TABLE_DISPLAY_COLUMN_LIST[i]);
				// col.add(ConstantProGEN_App.ERROR_DISPLAY_COLUMN_LIST[1]);
			}

	 * resultjson = DisplayDataObj.getDeployedDevicesJson();//
	 * MySQL_Controller.sp_getdeploy_devices(project_name);
	 * 
	 * 
	 * try { int no_of_devices = resultjson.getInt("No_of_devices");
	 * 
	 * JSONArray arr = resultjson.getJSONArray("Devices"); String device_name="";
	 * String StrippedDeviceName=""; String rack_id = ""; for (int i = 0; i <
	 * arr.length(); i++) { device_name =
	 * arr.getJSONObject(i).getString("Device_name"); rack_id =
	 * arr.getJSONObject(i).getString("Rack_ID"); StrippedDeviceName =
	 * FetchLastEightCharacter(device_name); col.add(StrippedDeviceName + "/" +
	 * rack_id);
	 * 
	 * } } catch (JSONException e) { 
	 * e.printStackTrace(); ApplicationLauncher.logger.
	 * error("getColNamesForErrorDisplay: JSONException: "+e.getMessage()); }

			ApplicationLauncher.logger.debug("getColNamesForErrorDisplay: col:" + col);

			return col;
		}*/
	
	
	public String closeProcalRemoteRunProject(String projectName) {

		ApplicationLauncher.logger.debug("closeProcalRemoteRunProject: Entry");

		if(ProcalFeatureEnable.PROCON_INTERFACE_ENABLED) {
			if (projectName.equals(getCurrentProjectName())) {
				closeProjectRunId(getSelectedDeployment_ID(),getCurrentProjectName());
				ref_cmbBoxSelectProject.getItems();
				Platform.runLater(()->{
					LiveTableDataManager.ResetliveTableData();
					ref_tbl_liveStatus.getColumns().clear();
					ref_tbl_liveStatus.getItems().clear();
					/*if(projectList.contains(projectName)) {
						Platform.runLater(()->{*/
							ref_cmbBoxSelectProject.getSelectionModel().select("Select");
						//});
						
						//return projectName;
					//}
				});
			}
			else {
				ApplicationLauncher.logger.debug("closeProcalRemoteRunProject: Project Name : " + projectName);
				ApplicationLauncher.logger.debug("closeProcalRemoteRunProject: Current Project Name : " + getCurrentProjectName());
				return "ProjectNotMatched";
			} 
		}
		else {
			return "InterfaceNotEnabled";
		}
		
		return "RunProjectClosed";
		
	}

	public void closeProjectTask() {

		ApplicationLauncher.logger.info("closeProjectTask: Entry");
		//humidityTemperatureReadingStageDisplay();
		//ApplicationLauncher.logger.info("closeProjectTask: While Entry");
		//while(HumidityTemperatureReadingController.isHumidityTemperatureScreenDisplayed());
		//ApplicationLauncher.logger.info("closeProjectTask: While Exit");
		String ptModeCompletedStatus = "Y";
		String ctModeCompletedStatus = "Y";
		//if(HumidityTemperatureReadingController.isDbUpdated()) {
		boolean status = false;
/*		if(ProcalFeatureEnable.PROCON_INTERFACE_ENABLED) {
			closeProjectRunId(getSelectedDeployment_ID(),getCurrentProjectName());
			LiveTableDataManager.ResetliveTableData();
			ref_tbl_liveStatus.getColumns().clear();
			ref_tbl_liveStatus.getItems().clear();
		}
		else {*/
			status = DisplayDataObj.updateExecutionStatusInDeployManageDB( getCurrentProjectName(),
					getSelectedDeployment_ID(), ConstantApp.EXECUTION_STATUS_COMPLETED,
					ptModeCompletedStatus,ctModeCompletedStatus); 
		//}
		if(status) {
			ApplicationLauncher.logger.debug("closeProjectTask: Test1");
			LiveTableDataManager.ResetliveTableData();
			//ApplicationLauncher.logger.debug("closeProjectTask: Test2");
			// LiveTableDataManager.setActiveRackList(getColNamesFromDB(getCurrentProjectName(),getSelectedDeployment_ID()));//getColNamesForErrorDisplay()); //getColNamesFromDB()
			// ApplicationLauncher.logger.debug("closeProjectTask: Test3");
			// LiveTableDataManager.InitLiveTableData();
			ref_tbl_liveStatus.getColumns().clear();
			ref_tbl_liveStatus.getItems().clear();
			ApplicationLauncher.logger.debug("closeProjectTask: Test4");
			/*if(ProcalFeatureEnable.PROCON_INTERFACE_ENABLED) {
				closeProjectRunId(getSelectedDeployment_ID(),getCurrentProjectName());
			}*/
			setGuiRefreshRequested(false);
			removeProjectNameList(ref_cmbBoxSelectProject.getSelectionModel().getSelectedItem().toString());
			ApplicationLauncher.logger.debug("closeProjectTask: Test5");
			//Platform.runLater(() -> {
			ref_cmbBoxSelectProject.getItems().clear();
			//updateProjectListinGUI();

			//});
			ApplicationLauncher.logger.debug("closeProjectTask: Test6");
			setSelectedProjectIndex(-1);
			ApplicationLauncher.logger.debug("closeProjectTask: Test7");
			setCurrentProjectName(null);
			ApplicationLauncher.logger.debug("closeProjectTask: Test8");
			//Sleep(6000); // added for scanDeviceTimer cancel

			updateProjectListinGUI();
			Sleep(200);
			setGuiRefreshRequested(true);
			ApplicationLauncher.logger.debug("closeProjectTask: Test9");


			ApplicationLauncher.logger.info("closeProjectTrigger: user prompted: Closure-Success , Project closed succesfully");
			ApplicationLauncher.InformUser("Closure-Success","Project closed succesfully", AlertType.INFORMATION);
		}else {
			
			//if(!ProcalFeatureEnable.PROCON_INTERFACE_ENABLED) {
				ApplicationLauncher.logger.info("closeProjectTrigger: user prompted: Closure-Failed , Unable to close the project. Kindly retry again! - prompted");
				ApplicationLauncher.InformUser("Closure-Failed","Unable to close the project. Kindly retry again!", AlertType.ERROR);
		
			//}
		 }

	}

	public static String getCurrentProjectName() {
		return currentProjectName;
	}

	public void setCurrentProjectName(String project_name) {
		currentProjectName = project_name;
	}
	public static List<String> getManageDeployData() {

		ApplicationLauncher.logger.debug("getManageDeployData : Entry");
		JSONObject resultjson = new JSONObject();
		new ArrayList<List<String>>();
		List<String> col = new ArrayList<String>();
		// ApplicationLauncher.logger.debug("getColNamesForErrorDisplay:
		// ERROR_DISPLAY_COLUMN_LIST:"+ConstantProGEN_App.ERROR_DISPLAY_COLUMN_LIST);
		// col = ConstantProGEN_App.ERROR_DISPLAY_COLUMN_LIST;

		// resultjson = DisplayDataObj.getDeployedDevicesJson();//
		// MySQL_Controller.sp_getdeploy_devices(project_name);
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());;
		long currentTime = calcEpoch(timeStamp);
		long deployedTimeMaxSearchLimit = currentTime- (ConstantApp.NUMBER_OF_SECONDS_IN_A_DAY*ConstantAppConfig.DEPLOYMENT_DB_SEARCH_MAX_TIME_LIMIT_IN_DAYS);
		resultjson = MySQL_Controller.sp_getdeploy_manage_active(deployedTimeMaxSearchLimit);
		ApplicationLauncher.logger.debug("getManageDeployData : resultjson: " + resultjson);
		try {
			resultjson.getInt("No_of_deployment");

			JSONArray arr = resultjson.getJSONArray("Deployment");
			//String equipmentSerialNumber = "";
			String deploymentID = "";
			String project_name = "";
			String energyFlowMode = "";
			String autoDeplyEnabled = "";
			//String deploy_mode = "";

			List<String> project_NameList = new ArrayList<String>();
			List<String> deployment_IdList = new ArrayList<String>();
			List<String> localEnergyFlowModeList = new ArrayList<String>();
			List<Boolean> localAutoDeployEnabledList = new ArrayList<Boolean>();
			//List<String> equipment_SerialNoList = new ArrayList<String>();
			//List<String> deployModeList = new ArrayList<String>();
			//clearEnergyFlowModeList();
			for (int i = 0; i < arr.length(); i++) {

				deploymentID = arr.getJSONObject(i).getString("deployment_id");
				arr.getJSONObject(i).getString("main_ct_mode");
				arr.getJSONObject(i).getString("neutral_ct_mode");
				project_name = arr.getJSONObject(i).getString("project_name");
				energyFlowMode = arr.getJSONObject(i).getString("energy_flow_mode"); 
				autoDeplyEnabled = arr.getJSONObject(i).getString("auto_deploy_enabled"); 
				/*				if ((ct_mode.equals("Y")) && (pt_mode.equals("Y"))) {
					deploy_mode = ConstantApp.DEPLOYMENT_PT_AND_CT_MODE;
				} else if ((ct_mode.equals("N")) && (pt_mode.equals("Y"))) {
					deploy_mode = ConstantApp.DEPLOYMENT_PT_MODE;
				} else if ((ct_mode.equals("Y")) && (pt_mode.equals("N"))) {
					deploy_mode = ConstantApp.DEPLOYMENT_CT_MODE;
				}*/

				deployment_IdList.add(deploymentID);
				//equipment_SerialNoList.add(equipmentSerialNumber);
				project_NameList.add(project_name);
				localEnergyFlowModeList.add(energyFlowMode);
				if(autoDeplyEnabled.equals("Y")) {
					localAutoDeployEnabledList.add(true);
				}else {
					localAutoDeployEnabledList.add(false);
				}
				//deployModeList.add(deploy_mode);

			}

			setProjectNameList(project_NameList);
			setDeploymentIdList(deployment_IdList);
			setEnergyFlowModeList(localEnergyFlowModeList);
			setAutoDeployEnabledList(localAutoDeployEnabledList);
			//setEquipmentSerialNoList(equipment_SerialNoList);
			//setDeployModeList(deployModeList);

		} catch (JSONException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("getManageDeployData: JSONException: " + e.getMessage());
		}
		ApplicationLauncher.logger.debug("getManageDeployData: col:" + col);

		return col;
	}

	public static List<String> getProjectNameList() {
		return projectNameList;
	}

	public static void setProjectNameList(List<String> prjNameList) {
		projectNameList = prjNameList;
	}

	public static List<String> getDeploymentIdList() {
		return deploymentIdList;
	}

	public static void setDeploymentIdList(List<String> deployIdList) {
		deploymentIdList = deployIdList;
	}


	public void setProjectNameAsAppTitle() {
		String project_name = getCurrentProjectName();//MeterParamsController.getCurrentProjectName();
		Stage app = ApplicationLauncher.getPrimaryStage();
		app.setTitle(ConstantVersion.APPLICATION_NAME +" - " + project_name);
	}





	@FXML
	public void radioBtnManualModeSinglePhaseOnChange() {
		if (ref_rdBtnManualModeSinglePhase.isSelected()) {
			ref_rdBtnManualModeThreePhase.setSelected(false);
			disableManualModeYandBphaseInput();
			disableChkBxManualModeBalanced();

		} else {
			ref_rdBtnManualModeSinglePhase.setSelected(true);
			enableChkBxManualModeBalanced();
		}


	}

	private void disableManualModeYandBphaseInput() {
		
		ref_txt_ManualModeYphaseVoltage.setDisable(true);
		ref_txt_ManualModeYphaseCurrent.setDisable(true);
		ref_txt_ManualModeYphasePfValue.setDisable(true);
		ref_rdBtnManualModeYphasePfLead.setDisable(true);
		ref_rdBtnManualModeYphasePfLag.setDisable(true);

		ref_txt_ManualModeBphaseVoltage.setDisable(true);
		ref_txt_ManualModeBphaseCurrent.setDisable(true);
		ref_txt_ManualModeBphasePfValue.setDisable(true);
		ref_rdBtnManualModeBphasePfLead.setDisable(true);
		ref_rdBtnManualModeBphasePfLag.setDisable(true);
	}

	@FXML
	public void radioBtnManualModeThreePhaseOnChange() {
		if (ref_rdBtnManualModeThreePhase.isSelected()) {
			ref_rdBtnManualModeSinglePhase.setSelected(false);
			if(ref_chkBxManualModeBalanced.isSelected()){
				disableManualModeYandBphaseInput();
				if(ref_rdBtnManualModeRphasePfLag.isSelected()){
					ref_rdBtnManualModeYphasePfLag.setSelected(true);
					ref_rdBtnManualModeBphasePfLag.setSelected(true);
					ref_rdBtnManualModeYphasePfLead.setSelected(false);
					ref_rdBtnManualModeBphasePfLead.setSelected(false);

				}else{
					ref_rdBtnManualModeYphasePfLag.setSelected(false);
					ref_rdBtnManualModeBphasePfLag.setSelected(false);
					ref_rdBtnManualModeYphasePfLead.setSelected(true);
					ref_rdBtnManualModeBphasePfLead.setSelected(true);
				}
				ref_txt_ManualModeYphaseVoltage.setText(ref_txt_ManualModeRphaseVoltage.getText());
				ref_txt_ManualModeYphaseCurrent.setText(ref_txt_ManualModeRphaseCurrent.getText());
				ref_txt_ManualModeYphasePfValue.setText(ref_txt_ManualModeRphasePfValue.getText());
				ref_txt_ManualModeBphaseVoltage.setText(ref_txt_ManualModeRphaseVoltage.getText());
				ref_txt_ManualModeBphaseCurrent.setText(ref_txt_ManualModeRphaseCurrent.getText());
				ref_txt_ManualModeBphasePfValue.setText(ref_txt_ManualModeRphasePfValue.getText());
			}else{

				enableManualModeYandBphaseInput();
			}
			enableChkBxManualModeBalanced();
		} else {
			ref_rdBtnManualModeThreePhase.setSelected(true);
			disableManualModeYandBphaseInput();
			disableChkBxManualModeBalanced();
		}


	}



	@FXML
	public void chkBxManualModeBalancedOnChange() {
		if (ref_chkBxManualModeBalanced.isSelected()) {
			disableManualModeYandBphaseInput();
			if(ref_rdBtnManualModeRphasePfLag.isSelected()){
				ref_rdBtnManualModeYphasePfLag.setSelected(true);
				ref_rdBtnManualModeBphasePfLag.setSelected(true);
				ref_rdBtnManualModeYphasePfLead.setSelected(false);
				ref_rdBtnManualModeBphasePfLead.setSelected(false);

			}else{
				ref_rdBtnManualModeYphasePfLag.setSelected(false);
				ref_rdBtnManualModeBphasePfLag.setSelected(false);
				ref_rdBtnManualModeYphasePfLead.setSelected(true);
				ref_rdBtnManualModeBphasePfLead.setSelected(true);
			}
			ref_txt_ManualModeYphaseVoltage.setText(ref_txt_ManualModeRphaseVoltage.getText());
			ref_txt_ManualModeYphaseCurrent.setText(ref_txt_ManualModeRphaseCurrent.getText());
			ref_txt_ManualModeYphasePfValue.setText(ref_txt_ManualModeRphasePfValue.getText());
			ref_txt_ManualModeBphaseVoltage.setText(ref_txt_ManualModeRphaseVoltage.getText());
			ref_txt_ManualModeBphaseCurrent.setText(ref_txt_ManualModeRphaseCurrent.getText());
			ref_txt_ManualModeBphasePfValue.setText(ref_txt_ManualModeRphasePfValue.getText());
		} else {
			enableManualModeYandBphaseInput();
		}


	}


	private void enableManualModeYandBphaseInput() {
		
		ref_txt_ManualModeYphaseVoltage.setDisable(false);
		ref_txt_ManualModeYphaseCurrent.setDisable(false);
		ref_txt_ManualModeYphasePfValue.setDisable(false);
		ref_rdBtnManualModeYphasePfLead.setDisable(false);
		ref_rdBtnManualModeYphasePfLag.setDisable(false);

		ref_txt_ManualModeBphaseVoltage.setDisable(false);
		ref_txt_ManualModeBphaseCurrent.setDisable(false);
		ref_txt_ManualModeBphasePfValue.setDisable(false);
		ref_rdBtnManualModeBphasePfLead.setDisable(false);
		ref_rdBtnManualModeBphasePfLag.setDisable(false);
	}

	@FXML
	public void radioBtnManualModeImportOnChange() {
		if (ref_rdBtnManualModeImport.isSelected()) {
			ref_rdBtnManualModeExport.setSelected(false);

		} else {
			ref_rdBtnManualModeImport.setSelected(true);
		}


	}

	@FXML
	public void radioBtnManualModeExportOnChange() {
		if (ref_rdBtnManualModeExport.isSelected()) {
			ref_rdBtnManualModeImport.setSelected(false);

		} else {
			ref_rdBtnManualModeExport.setSelected(true);
		}


	}



	@FXML
	public void radioBtnManualModeRphasePfLagOnChange() {
		if (ref_rdBtnManualModeRphasePfLag.isSelected()) {
			ref_rdBtnManualModeRphasePfLead.setSelected(false);
			if(ref_chkBxManualModeBalanced.isSelected()){
				if(ref_rdBtnManualModeThreePhase.isSelected()){
					ref_rdBtnManualModeYphasePfLag.setSelected(true);
					ref_rdBtnManualModeBphasePfLag.setSelected(true);
					ref_rdBtnManualModeYphasePfLead.setSelected(false);
					ref_rdBtnManualModeBphasePfLead.setSelected(false);
				}
			}

		} else {
			ref_rdBtnManualModeRphasePfLag.setSelected(true);
		}


	}

	@FXML
	public void radioBtnManualModeRphasePfLeadOnChange() {
		if (ref_rdBtnManualModeRphasePfLead.isSelected()) {
			ref_rdBtnManualModeRphasePfLag.setSelected(false);
			if(ref_chkBxManualModeBalanced.isSelected()){
				if(ref_rdBtnManualModeThreePhase.isSelected()){
					ref_rdBtnManualModeYphasePfLag.setSelected(false);
					ref_rdBtnManualModeBphasePfLag.setSelected(false);
					ref_rdBtnManualModeYphasePfLead.setSelected(true);
					ref_rdBtnManualModeBphasePfLead.setSelected(true);
				}
			}
		} else {
			ref_rdBtnManualModeRphasePfLead.setSelected(true);
		}


	}



	@FXML
	public void radioBtnManualModeYphasePfLagOnChange() {
		if (ref_rdBtnManualModeYphasePfLag.isSelected()) {
			ref_rdBtnManualModeYphasePfLead.setSelected(false);

		} else {
			ref_rdBtnManualModeYphasePfLag.setSelected(true);
		}


	}

	@FXML
	public void radioBtnManualModeYphasePfLeadOnChange() {
		if (ref_rdBtnManualModeYphasePfLead.isSelected()) {
			ref_rdBtnManualModeYphasePfLag.setSelected(false);

		} else {
			ref_rdBtnManualModeYphasePfLead.setSelected(true);
		}


	}




	@FXML
	public void radioBtnManualModeBphasePfLagOnChange() {
		if (ref_rdBtnManualModeBphasePfLag.isSelected()) {
			ref_rdBtnManualModeBphasePfLead.setSelected(false);

		} else {
			ref_rdBtnManualModeBphasePfLag.setSelected(true);
		}


	}

	@FXML
	public void radioBtnManualModeBphasePfLeadOnChange() {
		if (ref_rdBtnManualModeBphasePfLead.isSelected()) {
			ref_rdBtnManualModeBphasePfLag.setSelected(false);

		} else {
			ref_rdBtnManualModeBphasePfLead.setSelected(true);
		}


	}




	@FXML
	public void radioBtnMainCtOnChange() {
		if (ref_radioBtnMainCt.isSelected()) {
			ref_radioBtnNeutralCt.setSelected(false);
			Platform.runLater(() -> {
				enableBusyLoadingScreen();
			});

			//setSelectedTestExecutionMode(ConstantApp.DEPLOYMENT_PT_MODE);
			setMainCtMode(true);
			setNeutralCtMode(false);
			//updateSelectedProjectSetting();
			//updateLiveTable();
			refreshAllDataInGUI();
			if (getCurrentProject_TotalNoOfTestPoint() > 0) {
				EnableStartButton();
				EnableViewLogsButton();
				EnableResumeButton();
				EnableStepRunButton();
			} else {
				DisableStartButton();
				DisableViewLogsButton();
				DisableResumeButton();
				DisableStepRunButton();
			}
			//enablePT_ProfileGUI_Objects();
			//updateProfileDisplay();
			//updateLvdProfileData();
			Platform.runLater(() -> {
				disableBusyLoadingScreen();
			});
		} else {
			ref_radioBtnMainCt.setSelected(true);
		}


	}

	@FXML
	public void radioBtnNeutralCtOnChange() {
		if (ref_radioBtnNeutralCt.isSelected()) {
			ref_radioBtnMainCt.setSelected(false);
			Platform.runLater(() -> {
				enableBusyLoadingScreen();
			});

			//setSelectedTestExecutionMode(ConstantApp.DEPLOYMENT_CT_MODE);
			setMainCtMode(false);
			setNeutralCtMode(true);
			//updateSelectedProjectSetting();
			//updateLiveTable();
			refreshAllDataInGUI();
			if (getCurrentProject_TotalNoOfTestPoint() > 0) {
				EnableStartButton();
				EnableViewLogsButton();
				EnableResumeButton();
				EnableStepRunButton();
			} else {
				DisableStartButton();
				DisableViewLogsButton();
				DisableResumeButton();
				DisableStepRunButton();
			}
			//enableCT_ProfileGUI_Objects();
			Platform.runLater(() -> {
				disableBusyLoadingScreen();
			});
		} else {
			ref_radioBtnNeutralCt.setSelected(true);
		}


	}




	@FXML
	public void chBxManualTimerModeOnChange() {
		if (ref_chBxManualTimerMode.isSelected()) {
			enableTxtManualSetTimeBlock();
		} else {
			disableTxtManualSetTimeBlock();
		}


	}

	@FXML
	public void selectProjectOnChangeTrigger() {
		ApplicationLauncher.logger.info("selectProjectOnChangeTrigger : Entry");
		selectProjectOnchangeTimer = new Timer();
		selectProjectOnchangeTimer.schedule(new selectProjectOnChangeTask(), 100);
	}
	
	public String getProcalRemoteProjectRunId() {
		
		String projectRunId = "";
		projectRunId = getPresentProjectRunId();
		ApplicationLauncher.logger.info("getProcalRemoteProjectRunId : projectRunId: " + projectRunId);
		return projectRunId;
	}
	
	public String getProcalRemoteSelectRunProject(String projectName) {
		
		ObservableList<String> projectList = ref_cmbBoxSelectProject.getItems();
		
		ApplicationLauncher.logger.info("getProcalRemoteSelectRunProject : ref_cmbBoxSelectProject: " + projectList);
		ApplicationLauncher.logger.info("getProcalRemoteSelectRunProject : projectName: " + projectName);
		
	
		
		if (ProcalFeatureEnable.PROCON_INTERFACE_ENABLED) {
			if (projectList.contains(projectName)) {
				ApplicationLauncher.logger.info("getProcalRemoteSelectRunProject : matched hit ");
				Platform.runLater(() -> {
					ref_cmbBoxSelectProject.getSelectionModel().select(projectName);
				});
			} else {
				return "ProjectNotFound";
			} 
		}
		else {
			return "InterfaceNotEnabled";
		}
		
		
		/*getPresentTestPointStatus().setAllTestExecutionCompletedCheck(false);
		getPresentTestPointStatus().setAllTestExecutionCompleted(false);*/
		
		/*for (String project : projectList) {
		    if (project.equals(projectName)) {
		    	ApplicationLauncher.logger.info("getProcalRemoteSelectRunProject : matched hit ");
				Platform.runLater(()->{
					ref_cmbBoxSelectProject.getSelectionModel().select(projectName);
				});	
		    }
		}*/
		
		return "ProjectSelected";
		//return "";
	}
	
	
	public String getProcalRemoteCloseProject(String projectName) {
		
/*		ObservableList<String> projectList = ref_cmbBoxSelectProject.getItems();
		if(projectList.contains(projectName)) {
			Platform.runLater(()->{
				ref_cmbBoxSelectProject.getSelectionModel().select(projectName);
			});
			fxb
			return projectName;
		}*/
		return "";
	}

	class selectProjectOnChangeTask extends TimerTask {

		@Override
		public void run() {
			ApplicationLauncher.logger.debug("selectProjectOnChangeTask : Entry");
			if(isGuiRefreshRequested()){
				ApplicationLauncher.logger.debug("selectProjectOnChangeTask : Entry2");
				Platform.runLater(() -> {

					enableBusyLoadingScreen();
					refreshAllDataInGUI();
					//});

					//Sleep(7000); 
					disableBusyLoadingScreen();

					/*if(ProGEN_FeatureEnable.CI_REF_STD_ALL_TAP_CONNECTED_FEATURE_ENABLED){

					  }else{
						  if(isCtMode()){ // SCR-01
							  if(ConstantConfigProGEN.refStdTerminalSwitchManualEnabled){
								  PTCT_Model profileData = getSelectedPTCT_ModelData();
								  float ratedCurrent = Float.parseFloat(profileData.getPrimaryCurrent());
								  if(ratedCurrent>10.f && ratedCurrent <=25.0f){
									  ApplicationLauncher.logger.info("projectChangeOnChangeTask: user prompted: Kindly ensure reference standard terminal wire is disconnected from 50 Amps and connected to 25 Amps terminal");
									  ApplicationLauncher.InformUser("Reference Standard wiring","Kindly ensure reference standard terminal wire is disconnected from 50 Amps and connected to 25 Amps terminal", AlertType.INFORMATION);
								  }else if(ratedCurrent > 25.0f && ratedCurrent <=50.0f){
									  ApplicationLauncher.logger.info("projectChangeOnChangeTask: user prompted: Kindly ensure reference standard terminal wire is disconnected from 25 Amps and connected to 50 Amps terminal");
									  ApplicationLauncher.InformUser("Reference Standard wiring","Kindly ensure reference standard terminal wire is disconnected from 25 Amps and connected to 50 Amps terminal", AlertType.INFORMATION);

								  }
							  }
						  }
					  }*/
					// updateCurrentPTCT_ModelData();

					//selectProjectOnchangeTimer.cancel();
				});
			}
			/*			  if(ProcalFeatureEnable.USER_ACCESS_CONTROL_ENABLED){
				  applyUacSettings();
			  }*/
			selectProjectOnchangeTimer.cancel();
		}

	}


	public void loadSecondaryLduDisplayGUI() throws JSONException{
		ApplicationLauncher.logger.debug("loadSecondaryLduDisplayGUI: entry");		
		ApplicationHomeController.setLduAllDataViewGUI_Displayed(true);
		String fxmlFilePath = "/fxml/deployment/SecondaryLduDisplay";
		if(ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK == 40){
			fxmlFilePath = "/fxml/deployment/SecondaryLduDisplay40";
			ApplicationLauncher.logger.info("loadSecondaryLduDisplayGUI: Loading 40 LDU Sec display");
		}
		FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFilePath + ConstantApp.THEME_FXML));
		Scene newScene;
		try {
			newScene = new Scene(loader.load());
		} catch (IOException ex) {
			
			ex.printStackTrace();
			ApplicationLauncher.logger.error("loadSecondaryLduDisplayGUI: IOException:"+ex.getMessage());
			return;
		}

		Stage InstantMetricsStage = new Stage();
		InstantMetricsStage.initModality(Modality.NONE);
		//https://stackoverflow.com/questions/38481914/disable-background-stage-javafx?rq=1
		InstantMetricsStage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
		InstantMetricsStage.setScene(newScene);
		InstantMetricsStage.setTitle(ConstantVersion.APPLICATION_NAME +"-LDU");
		//InstantMetricsStage.setResizable(false);
		InstantMetricsStage.setResizable(true);
		Screen.getPrimary().getVisualBounds();
		int width = 1213+10;
		int height = 636+35;
		//InstantMetricsStage.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth() - width);
		//InstantMetricsStage.setY(primaryScreenBounds.getMinY() + primaryScreenBounds.getHeight() - height);
		InstantMetricsStage.centerOnScreen();
		InstantMetricsStage.setWidth(width);
		InstantMetricsStage.setHeight(height);

		InstantMetricsStage.setAlwaysOnTop(false);
		InstantMetricsStage.show();
		InstantMetricsStage.toBack();

		InstantMetricsStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				we.consume();
			}

		}); 



	}

	public static boolean isGuiRefreshRequested() {
		return guiRefreshRequested;
	}

	public static void setGuiRefreshRequested(boolean guiRefreshRequested) {
		ProjectExecutionController.guiRefreshRequested = guiRefreshRequested;
	}

	public void LoadInstantMetricsGUI() throws JSONException{
		ApplicationLauncher.logger.info("LoadInstantMetricsGUI: entry");		
		ApplicationHomeController.SetInstantMetricsGUI_Displayed(true);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/deployment/DisplayInstantMetrics" + ConstantApp.THEME_FXML));
		if(ProcalFeatureEnable.PROPOWER_SRC_ONLY ){

			ApplicationLauncher.logger.info("LoadInstantMetricsGUI: loading DisplayInstantMetricsProPower");
			loader = new FXMLLoader(getClass().getResource("/fxml/deployment/DisplayInstantMetricsProPower" + ConstantApp.THEME_FXML));
		}
		Scene newScene;
		try {
			newScene = new Scene(loader.load());
		} catch (IOException ex) {
			
			ex.printStackTrace();
			ApplicationLauncher.logger.error("LoadInstantMetricsGUI: IOException:"+ex.getMessage());
			return;
		}

		Stage InstantMetricsStage = new Stage();
		InstantMetricsStage.initModality(Modality.NONE);//Modality.WINDOW_MODAL
		//InstantMetricsStage.initModality(Modality.WINDOW_MODAL);
		//https://stackoverflow.com/questions/38481914/disable-background-stage-javafx?rq=1
		//InstantMetricsStage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
		InstantMetricsStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/" + ConstantVersion.APP_ICON_FILENAME)));
		InstantMetricsStage.setScene(newScene);
		InstantMetricsStage.setTitle(ConstantVersion.APPLICATION_NAME +"-Instant Metrics");
		InstantMetricsStage.setResizable(false);
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		int width = 413+10;
		int height = 314+35;
		InstantMetricsStage.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth() - width);
		InstantMetricsStage.setY(primaryScreenBounds.getMinY() + primaryScreenBounds.getHeight() - height);
		InstantMetricsStage.setWidth(width);
		InstantMetricsStage.setHeight(height);

		if(ConstantAppConfig.INSTANT_METRICS_DISPLAY_ALWAYS_ON_TOP){
			InstantMetricsStage.setAlwaysOnTop(true );
			InstantMetricsStage.show();
			//InstantMetricsStage.toBack();
		}else{

			InstantMetricsStage.setAlwaysOnTop(false);			
			InstantMetricsStage.show();
			InstantMetricsStage.toBack();
		}

		InstantMetricsStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				we.consume();
			}

		}); 



	}



	public void loadMaintenanceGUI() throws JSONException{
		ApplicationLauncher.logger.info("loadMaintenanceGUI: entry");		
		ApplicationHomeController.setMaintenanceGUI_Displayed(true);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/deployment/MaintenanceModeExec" + ConstantApp.THEME_FXML));
		Scene newScene;
		try {
			newScene = new Scene(loader.load());
		} catch (IOException ex) {
			
			ex.printStackTrace();
			ApplicationLauncher.logger.error("loadMaintenanceGUI: IOException:"+ex.getMessage());
			return;
		}

		Stage InstantMetricsStage = new Stage();
		InstantMetricsStage.initModality(Modality.NONE);
		//https://stackoverflow.com/questions/38481914/disable-background-stage-javafx?rq=1
		InstantMetricsStage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
		InstantMetricsStage.setScene(newScene);
		InstantMetricsStage.setTitle(ConstantVersion.APPLICATION_NAME +"-Maintenence");
		InstantMetricsStage.setResizable(false);
		Screen.getPrimary().getVisualBounds();
		InstantMetricsStage.setAlwaysOnTop(false);
		InstantMetricsStage.show();
		//InstantMetricsStage.toBack();

		InstantMetricsStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				we.consume();
			}

		}); 



	}


	public void setProcessTC_ExecutionRefreshTimeInMSec(int value){
		ProcessTC_ExecutionRefreshTimeInMSec = value;
	}

	public int getProcessTC_ExecutionRefreshTimeInMSec(){
		return ProcessTC_ExecutionRefreshTimeInMSec;
	}

	public static boolean getExecutionInProgress(){
		return bExecutionInProgress;
	}

	public static void setExecutionInProgress(Boolean value){
		bExecutionInProgress = value;
	}



	public static  void setUpdateRowColorFlag(Boolean value){
		UpdateRowColorFlag = value;
	}

	public static Boolean getUpdateRowColorFlag(){
		return UpdateRowColorFlag;
	}



	public static  void setExecuteStopStatus(Boolean value){

		ApplicationLauncher.logger.debug("setExecuteStopStatus: value: " + value );
		ExecuteStopStatus = value;
	}

	public static Boolean getExecuteStopStatus(){
		return ExecuteStopStatus;
	}

	public static void reset_PowerOnWaitCounter(){
		PowerOnWaitCounter = ConstantAppConfig.POWERONWAITCOUNTER;

	}

	public static void UpdateProjectProgressBar(){
		ProjectStatusRefresh.ProjectProgressBarRefresh(ref_barProjectStatus,ref_barPinProjectStatus,getCurrentTestPoint_Index());
	}



	public static void UpdateRefStdProgressBar(){
		ProjectStatusRefresh.RefStdProgressBarRefresh(DisplayInstantMetricsController.ref_barRefStdRefresh);
	}

	public static void RefStdProgressBarStart(){

		ProjectStatusRefresh.startRefStdProgress(DisplayInstantMetricsController.ref_barRefStdRefresh);
	}

	public static void RefStdProgressBarStop(){

		ProjectStatusRefresh.stopRefStdProgress(DisplayInstantMetricsController.ref_barRefStdRefresh);

	}

	public static void ResetRefStdProgressCounter(){
		ProjectStatusRefresh.ResetRefStdProgressCounter();

	}


	public void RunningStatusAdjustScreen(){

		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		double ScreenHeight = primaryScreenBounds.getHeight();
		double ScreenWidth= primaryScreenBounds.getWidth();
		double SplitPaneOffset = 149;
		double ProgressStatusPaneHeight = 192;
		double Monitor_RefData_Width = 0;//250;
		double RunningStatusHeaderOffset = 60;
		double controlDisplayHeight = 80;

		long ScreenWidthThreshold = 1500;
		long ScreenHeightThreshold = 700;

		ScreenWidthThreshold = ConstantAppConfig.ScreenWidthThreshold;
		ScreenHeightThreshold = ConstantAppConfig.ScreenHeightThreshold;

		ApplicationLauncher.logger.info("RunningStatusAdjustScreen :Current Screen Height:"+ScreenHeight);
		ApplicationLauncher.logger.info("RunningStatusAdjustScreen: Current Screen Width:"+ScreenWidth);
		ApplicationLauncher.logger.info("RunningStatusAdjustScreen :ScreenHeightThreshold:"+ScreenHeightThreshold);
		ApplicationLauncher.logger.info("RunningStatusAdjustScreen: ScreenWidthThreshold:"+ScreenWidthThreshold);
		if (ScreenHeight> ScreenHeightThreshold){
			titlePaneLiveStatus.setMinHeight(ScreenHeight - ProgressStatusPaneHeight - RunningStatusHeaderOffset - controlDisplayHeight+100);
			if(ProcalFeatureEnable.PROPOWER_SRC_ONLY ){
				//ref_tabSequenceMode.setMinHeight(ScreenHeight - ProgressStatusPaneHeight - RunningStatusHeaderOffset - controlDisplayHeight+300);
				ref_tabPaneRunMode.setMinHeight(ScreenHeight - ProgressStatusPaneHeight - RunningStatusHeaderOffset - controlDisplayHeight+300);
				titlePaneLiveStatus.setMinHeight(ScreenHeight - ProgressStatusPaneHeight - RunningStatusHeaderOffset - controlDisplayHeight+50);
			}

		}else{
			ApplicationLauncher.logger.info("ProjectExecutionController :ScreenHeightThreshold else: Entry");
			//titlePaneLiveStatus.setMinHeight(ScreenHeight - ProgressStatusPaneHeight - RunningStatusHeaderOffset - controlDisplayHeight+100);
			if(ProcalFeatureEnable.PROPOWER_SRC_ONLY ){
				//ref_tabSequenceMode.setMinHeight(ScreenHeight - ProgressStatusPaneHeight - RunningStatusHeaderOffset - controlDisplayHeight+300);
				ref_tabPaneRunMode.setMaxHeight(ScreenHeight - ProgressStatusPaneHeight - RunningStatusHeaderOffset - controlDisplayHeight+300);
				titlePaneLiveStatus.setMaxHeight(ScreenHeight - ProgressStatusPaneHeight - RunningStatusHeaderOffset - controlDisplayHeight+75);
			}
		}
		double RunningStatusWidth = ScreenWidth-SplitPaneOffset-Monitor_RefData_Width+100;//-25;

		if(ScreenWidth >ScreenWidthThreshold){
			vBoxRunningStatus.setPrefWidth(RunningStatusWidth-70);
			ref_tbl_liveStatus.setPrefWidth(RunningStatusWidth-70);//-10);
			AnchorPaneRunningStatus.setPrefWidth(RunningStatusWidth+20);
			hboxRunningStatus.setPrefWidth(ScreenWidth);

			titledPaneStatus.setPrefWidth(425);

			titledPaneModeSelection.setPrefWidth(325);

			titledPaneControls.setPrefWidth(425);

			titledPaneInstantMetrics.setPrefWidth(500);

			double statusWidth = titledPaneStatus.getPrefWidth();
			double modeSelectionWidth = titledPaneModeSelection.getPrefWidth();;//286;
			double controlsWidth = titledPaneControls.getPrefWidth();//378;
			double instantMetricsWidth = titledPaneInstantMetrics.getPrefWidth();//452;
			int numberOfGapLabels = 3;
			double totalBottomControlWidth = statusWidth + modeSelectionWidth + controlsWidth + instantMetricsWidth;
			anchorProgressStatusPane.setPrefWidth(RunningStatusWidth-70);
			hBoxControlsDisplay.setPrefWidth(RunningStatusWidth-70);
			labelStatusGap.setPrefWidth((RunningStatusWidth-totalBottomControlWidth)/numberOfGapLabels);
			labelModeSelectionGap.setPrefWidth((RunningStatusWidth-totalBottomControlWidth)/numberOfGapLabels);
			labelControlSelectionGap.setPrefWidth((RunningStatusWidth-totalBottomControlWidth)/numberOfGapLabels);

			gridPaneTpStatus.setLayoutX(35);
			gridPaneProjectStatus.setLayoutX(35);
			gridPaneProjectSelect.setLayoutX(25);
			gridPaneModeSelect.setLayoutX(25);
			gridPaneControls.setLayoutX(25);
			gridPaneInstantMetrics.setLayoutX(35);

			//gridPaneTpStatus.setPrefWidth(statusWidth-10);
			//gridPaneProjectStatus.setPrefWidth(statusWidth-40);
			/*			gridPaneProjectSelect.setPrefWidth(modeSelectionWidth);
			gridPaneModeSelect.setPrefWidth(modeSelectionWidth-40);
			gridPaneControls.setPrefWidth(controlsWidth-40);
			gridPaneInstantMetrics.setPrefWidth(instantMetricsWidth-40);*/

			if(ProcalFeatureEnable.PROPOWER_SRC_ONLY ){
				//ref_tabSequenceMode.setPrefWidth(RunningStatusWidth+20);
				ref_tabPaneRunMode.setPrefWidth(RunningStatusWidth+20);
			}



		}else{
			//titlePaneLiveStatus
			ApplicationLauncher.logger.info("ProjectExecutionController :ScreenWidthThreshold else: Entry");

			if(ProcalFeatureEnable.PROPOWER_SRC_ONLY ){
				//ref_tabSequenceMode.setPrefWidth(RunningStatusWidth+20);
				ref_tabPaneRunMode.setPrefWidth(RunningStatusWidth+20);
			}
		}
	}

	public void loadDevices(){
		ApplicationLauncher.logger.info("loadDevices : Entry");

		getCurrentProjectName();

		setup_live_table();


	}

	/*	public void initResultTable(){
		ApplicationLauncher.logger.info("initResultTable : Entry");
		ref_tbl_liveStatus.getColumns().clear();
		ref_tbl_liveStatus.getItems().clear();	

		List<String> columnNames = new ArrayList<String>();
		List<List<Object>> rowValues = new ArrayList<List<Object>>();

		columnNames.add("Test Point");
		columnNames.add("Device 1");
		columnNames.add("Device 2");
		columnNames.add("Device 3");
		columnNames.add("Device 4");


		for(int i=0; i<4; i++){
			List<Object> row = new ArrayList<Object>();
			row.add("test_name"+ i);
			row.add("Pass");
			row.add("Fail");
			row.add("Pass");
			row.add("Incomplete");
			rowValues.add(row);
		}


		ApplicationLauncher.logger.info("columnNames: " + columnNames);
		ApplicationLauncher.logger.info("rowValues: " + rowValues);
		TableColumn<List<Object>, Object> column = new TableColumn<List<Object>, Object>("Test Point");
		column.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get(0)));
		ref_tbl_liveStatus.getColumns().add(column);
		ApplicationLauncher.logger.info("test1");

		for (int i = 1; i < columnNames.size(); i++) {
			TableColumn<List<Object>, Object> col = new TableColumn<List<Object>, Object>(columnNames.get(i));
			int j = i;
			col.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get(j)));
			ref_tbl_liveStatus.getColumns().add(col);

		}
		Platform.runLater(() -> {
			ref_tbl_liveStatus.getItems().setAll(FXCollections.observableList(rowValues));
		});

	}*/


	public static List<String> getColNamesFromDB(String project_name, String getSelectedDeployment_ID){
		ApplicationLauncher.logger.debug("getColNamesFromDB : Entry");
		JSONObject resultjson = new JSONObject();
		new ArrayList<List<String>>();
		List<String> col = new ArrayList<String>();
		if(ProcalFeatureEnable.LIVE_TABLE_EXECUTION_STATUS_DISPLAY){
			col.add(ConstantApp.LIVE_TABLE_EXECUTION_STATUS_DISPLAY_HEADER);
		}
		ApplicationLauncher.logger.debug("getColNamesFromDB : project_name: "+ project_name);
		ApplicationLauncher.logger.debug("getColNamesFromDB : getSelectedDeployment_ID: "+ getSelectedDeployment_ID);
		//resultjson = DisplayDataObj.getDeployedDevicesJson();// MySQL_Controller.sp_getdeploy_devices(project_name);
		resultjson = MySQL_Controller.sp_getdeploy_devices(project_name,getSelectedDeployment_ID());

		try {
			resultjson.getInt("No_of_devices");

			JSONArray arr = resultjson.getJSONArray("Devices");
			String device_name="";
			String StrippedDeviceName="";
			String rack_id = "";
			ApplicationLauncher.logger.debug("getColNamesFromDB : arr length: " + arr.length());
			for (int i = 0; i < arr.length(); i++)
			{
				device_name = arr.getJSONObject(i).getString("Device_name");
				//rack_id = arr.getJSONObject(i).getString("Rack_ID");
				rack_id = String.valueOf(arr.getJSONObject(i).getInt("Rack_ID"));
				StrippedDeviceName = FetchLastEightCharacter(device_name);
				col.add(StrippedDeviceName + "/" + rack_id);

			}
		} catch (JSONException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("getColNamesFromDB: JSONException: "+e.getMessage());
		}
		ApplicationLauncher.logger.debug("getColNamesFromDB: col:"+col);

		return col;
	}

	public static String FetchLastEightCharacter(String input){

		//ApplicationLauncher.logger.debug("FetchLastEightCharacter: : Entry");
		String Output= "";
		int length_of_input=input.length();
		//ApplicationLauncher.logger.debug("length_of_input:"+length_of_input);	
		if(length_of_input<=8){
			//ApplicationLauncher.logger.debug("input:"+input);
			Output = input;
		}
		else{
			int strip_input=length_of_input-8;
			String strip_input_string=input.substring(strip_input, length_of_input);
			//ApplicationLauncher.logger.debug("stripped_input_string"+strip_input_string);
			Output= strip_input_string;

		}
		return Output;
	}

	/*	public List<List<Object>> getRowValues(String project_name, int col_size) throws JSONException{
		ApplicationLauncher.logger.info("getRowValues : Entry");
		List<List<Object>> rowValues = new ArrayList<List<Object>>();
		JSONObject testcaselist = MySQL_Controller.sp_getdeploy_test_cases(project_name);
		JSONArray testcases = testcaselist.getJSONArray("Test_cases");
		ApplicationLauncher.logger.info("testcaselist: " + testcaselist);
		for(int i= 0; i<testcases.length(); i++){
			List<Object> row = new ArrayList<Object>();
			JSONObject jobj = testcases.getJSONObject(i);
			row.add(jobj.getString("test_case"));

			for(int j=0; j<col_size; j++){
				row.add("pass");
			}
			rowValues.add(row);
		}
		return rowValues;
	}*/

	public void setSelectedTestPointAsStartIndex(){
		int index = ref_tbl_liveStatus.getSelectionModel().getSelectedIndex();
		setCurrentTestPoint_Index(index);
	}

	@FXML
	public void ResumeRunTrigger() {
		ApplicationLauncher.logger.info("ResumeRunTrigger: Entry");
		resumeRunTimer = new Timer();
		resumeRunTimer.schedule(new ResumeRunTaskClick(), 50);

	}

	class ResumeRunTaskClick extends TimerTask {
		public void run() {
			ResumeOnClick();
			resumeRunTimer.cancel();


		}
	}

	public void ResumeOnClick(){
		ApplicationLauncher.logger.info("ResumeOnClick: Entry");
		setStepRunFlag(false);
		setResumeFlag(true);
		try {
			StartTestExecution();
		} catch (JSONException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("ResumeOnClick: JSONException: "+e.getMessage());
		}  	catch (ParseException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("ResumeOnClick: ParseException: "+e.getMessage());
		} 
	}

	public void RestartOnClick(){


	}


	@FXML
	public void StopOnClick(){

		Trigger_Stop_Confirmation();
	}
	
	public ArrayList<String>  getProcalRemotePresentTestPointIdList() {
		ApplicationLauncher.logger.info("getProcalRemotePresentTestPointIdList : Entry");
		
		//getRemoteTestPointStatusMap().clear();
		
		ArrayList<String> testPointIdList = new ArrayList<String> ();
		JSONObject currentprojecttestpoint_ = new JSONObject();
		String current_project_test_case = "";
		//getRemoteTestPointStatusMap().clear();
		for (int j = 0; j < CurrentProjectTestPoint_List.length(); j++) {
			// ApplicationLauncher.logger.info("refreshDataFromResultDB: j = "+j);
			currentprojecttestpoint_ = (JSONObject) CurrentProjectTestPoint_List.get(j);
			current_project_test_case = currentprojecttestpoint_.getString("test_case");
			testPointIdList.add(current_project_test_case);
			
			//getRemoteTestPointStatusMap().put(current_project_test_case, "");
		}
		
		//ApplicationLauncher.logger.info("testPointStatusMap: " + getRemoteTestPointStatusMap());
		
		
		///get
		//testPointIdList
		return testPointIdList;
		
	}
	
	
	
	
	
	public TestPointStatus getProcalRemotePresentTestPointStatus() {
		ApplicationLauncher.logger.info("getProcalRemotePresentTestPointStatus : Entry");
		ApplicationLauncher.logger.info("getProcalRemotePresentTestPointStatus : getPresentTestPointStatus : " + getPresentTestPointStatus());
		
		return getPresentTestPointStatus();
		
	}
	
	public JSONObject getProcalRemoteAllTestPointStatus() {
		ApplicationLauncher.logger.info("getProcalRemoteAllTestPointStatus : Entry");
		
		JSONObject result = GetResultsFromDB();
		
		ApplicationLauncher.logger.info("getProcalRemoteAllTestPointStatus : getAllTestPointStatus : " + result);
		
		return result;
		
	}
	
	
	public void procalRemoteStopTrigger() {
		ApplicationLauncher.logger.info("procalRemoteStopTrigger : Entry");
		TCStopConfirmationTimer = new Timer();
		TCStopConfirmationTimer.schedule(new RemoteExecuteStopConfirmationTC(), 100);
	}
	
	class RemoteExecuteStopConfirmationTC extends TimerTask{


		@Override
		public void run() {
			Platform.runLater(() -> {
				//boolean status=false;
				//status=stop_confirmation();
				//if(status){
					StopOnClickSuccess();
				//}
				TCStopConfirmationTimer.cancel();
			});
		}

	}
	
	

	public void StopOnClickSuccess(){
		ApplicationLauncher.logger.info("StopOnClickSuccess : Entry - User Aborted");
		setUserAbortedFlag(true);
		if(!getStepRunFlag()){
			updateAbortedInDisplay();
		}else{
			if(ProcalFeatureEnable.PROPOWER_SRC_ONLY ){
				LiveTableDataManager.UpdateliveTableData(1, "N",ConstantApp.EXECUTION_STATUS_COMPLETED);//
				
				Sleep(2);
			}
			//if(ProcalFeatureEnable.LIVE_TABLE_EXECUTION_STATUS_DISPLAY ){
			//	LiveTableDataManager.UpdateliveTableData(ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID, "N",ConstantApp.EXECUTION_STATUS_COMPLETED);//
				
				//Sleep(2);
			//}
			updateExecutionStatusOnDB();
		}
		ProjectExitProcess();

	}

	public void ProjectExitProcess(){
		ApplicationLauncher.logger.info("ProjectExitProcess : Entry");
		DisableStopButton();
		SaveProjectRunEndTime();
		//		DisableStopButton();
		DisplayShutDownAllCompPortsTrigger();

	}

	public void SaveProjectRunEndTime(){
		String project_name = getCurrentProjectName();//MeterParamsController.getCurrentProjectName();
		long start_time = getProjectStartTime();
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		long endtime = calcEpoch(timeStamp);
		MySQL_Controller.sp_update_endtime_project_run(project_name, start_time, endtime);
	}

	public void ShowOnClick() throws ParseException, JSONException{
		ApplicationLauncher.logger.info("ShowOnClick : Entry");
		LocalDate selected_date =  datepicker_projectdate.getValue();
		ApplicationLauncher.logger.info("selected_date" + selected_date.toString());
		String from_time = selected_date + " 00:00:00"; 
		String to_time = selected_date + " 24:00:00"; 
		long from_timestamp = calcEpoch(from_time);
		long to_timestamp = calcEpoch(to_time);
		ApplicationLauncher.logger.info("from_timestamp : "+ from_timestamp + " to_timestamp: "+ to_timestamp);
		JSONObject result = MySQL_Controller.sp_getproject_scheduled_time(from_timestamp, to_timestamp);
		ApplicationLauncher.logger.info("scheduled_data" + result);
		JSONArray scheduled_data = result.getJSONArray("Project_sche_time");
		JSONObject sche_project =  new JSONObject();
		for(int i=0; i< (scheduled_data.length()); i = i+2){
			sche_project =  scheduled_data.getJSONObject(i);
			project_time_data.add(new ScheduledProjectModel(sche_project.getString("Project_name"), sche_project.getString("Scheduled_time")));    		
		}
		ApplicationLauncher.logger.info("project_time_data: " + project_time_data);
		scheduled_project.getItems().setAll(project_time_data);
	}

	@SuppressWarnings("null")
	public static long calcEpoch(String Date_time){


		long epoch = 0;
		//String str = "2014-07-04 04:05:10";   // UTC
		String str = Date_time;   // UTC

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date datenew = null;
		try {
			datenew = df.parse(str);
		} catch (ParseException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("calcEpoch: ParseException: "+e.getMessage());
		}
		epoch = datenew.getTime() /1000;

		return epoch;
	}

	public static long getCurrentEpoch() throws ParseException{

		java.time.LocalDate.now().toString();
		String current_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());;

		long epoch = 0;


		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date datenew = df.parse(current_time);
		epoch = datenew.getTime() /1000;


		return epoch;
	}




	public static void setRunningStatusTable_DeviceList(ArrayList<String> columnNames){

		RunningStatusTableTP_ColumnList = columnNames;

	}

	public static ArrayList<String>  getRunningStatusTable_DeviceList(){
		return RunningStatusTableTP_ColumnList;
	}



	public static void setup_live_table() {

		ApplicationLauncher.logger.info("setup_live_table : Entry");
		String Projectname = getCurrentProjectName();//MeterParamsController.getCurrentProjectName();
		String deploymentId = getSelectedDeployment_ID();
		//Platform.runLater(() -> {
		ref_tbl_liveStatus.getColumns().clear();
		ref_tbl_liveStatus.getItems().clear();
		//});
		ArrayList<String> columnNames = new ArrayList<String>();
		columnNames.add("S.No");
		columnNames.add("Test Point");
		//columnNames.add(ConstantApp.LIVE_TABLE_EXECUTION_STATUS_DISPLAY_HEADER);
		List<String> CurrentActiveRackList = ProjectExecutionController.getColNamesFromDB(Projectname,deploymentId);
		if(ProcalFeatureEnable.PROCON_INTERFACE_ENABLED) {
			CurrentActiveRackList = DisplayDataObj.manipulateConveyorDutSerialNumber(CurrentActiveRackList);
		}
		updateDutSerialNo(new ArrayList<String>(CurrentActiveRackList));
		columnNames.addAll(CurrentActiveRackList);
		/*		ApplicationLauncher.logger.info("setup_live_table : columnNames Size:"+columnNames.size());
		for (int i = 0; i<columnNames.size();i++){
			ApplicationLauncher.logger.info("setup_live_table : columnNames:"+i+":"+columnNames.get(i).toString());
		}*/
		
		setRunningStatusTable_DeviceList(columnNames);
		//updateDutSerialNo(columnNames);
		JSONArray testcaselist;
		try {
			testcaselist = getListOfTestPoints(Projectname,deploymentId);
			setCurrentProjectTestPointList(testcaselist);
		} catch (JSONException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("setup_live_table : JSONException:"+e.getMessage());
		}
		//ApplicationLauncher.logger.debug("setup_live_table : Entry: columnNames:"+columnNames);
		//ApplicationLauncher.logger.debug("setup_live_table : Entry: getAllRowData:"+LiveTableDataManager.getAllRowData());
		insertTableValues(columnNames, LiveTableDataManager.getAllRowData());
		//Platform.runLater(() -> {
		ref_tbl_liveStatus.setVisible(true);
		ref_tbl_liveStatus.setColumnResizePolicy((param) -> true );
		//});

	}

	public static void updateDutSerialNo(ArrayList<String> columnNames) {
		
		DeviceDataManagerController.clearDutSerialNumberMap();
		String serialNo = "";
		int dutAddress = 0;
		String [] splittedData;
		for(int i=0; i< columnNames.size(); i++) {
			try {
				serialNo = "";
				ApplicationLauncher.logger.debug("updateDutSerialNo: index : "+ i + " -> " + columnNames.get(i));
				splittedData = columnNames.get(i).split("/");
				serialNo = splittedData[0];
				dutAddress = Integer.parseInt(splittedData[1]);
				ApplicationLauncher.logger.debug("updateDutSerialNo: dutAddress : "+ dutAddress + " -> " + serialNo);
				DeviceDataManagerController.setDutSerialNumberMap(serialNo,dutAddress);
			}catch (Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("updateDutSerialNo: Exception:"+e.getMessage());

			}
		}
		
		
		
	}

	public static void update_live_table(final int TableRowIndex,final ArrayList<Object> rowValues) {
		ApplicationLauncher.logger.debug("update_live_table : Entry: TableRowIndex:"+TableRowIndex);
		if(TableRowIndex>=0){
			//getPresentTestPointStatus().setPresentTestPointId(getPresentTestPointStatus.get());
			Platform.runLater(() -> {
				ref_tbl_liveStatus.getItems().set(TableRowIndex, rowValues);
				ref_tbl_liveStatus.getSelectionModel().select(TableRowIndex);
				GuiUtils.autoResizeColumns(ref_tbl_liveStatus,true);
				ApplicationLauncher.logger.debug("update_live_table : Exit :TableRowIndex:"+TableRowIndex);
				//ref_tbl_liveStatus.refresh();
			});
		}

	}



	public static void insertTableValues(ArrayList<String> columnNames, ArrayList<ArrayList<Object>> rowValues){
		ApplicationLauncher.logger.debug("insertTableValues : Entry");


		try{
			for (int i = 0; i < columnNames.size(); i++) {
				TableColumn<List<Object>, Object> col = new TableColumn<List<Object>, Object>(columnNames.get(i));
				//ApplicationLauncher.logger.debug("insertTableValues : col:"+columnNames.get(i));
				int j = i;
				try{
					col.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().get(j)));
					col.setSortable(false);//Sorting disabled
					ref_tbl_liveStatus.getColumns().add(col);

					col.setCellFactory(cellData -> new TableCell<List<Object>, Object>() {


						@Override
						public void updateItem(Object item, boolean empty) {

							super.updateItem(item, empty);

							if (item == null || empty) {
								setText(null);
							} else {
								String CurrentValue = (String)item;



								try{        


									if ( CurrentValue.contains("WFR") ) {	
										if (!ProcalFeatureEnable.RESULT_STATUS_DISPLAY_ENABLE_FEATURE){
											CurrentValue = CurrentValue.substring(2,CurrentValue.length());
										}
										//this.setStyle("-fx-background-color: yellow;");
										this.setStyle("-fx-text-fill: blue;");
										//ApplicationLauncher.logger.debug("insertTableValues :updateItem: Background color yellow");
									} else  if (CurrentValue.startsWith(ConstantReport.RESULT_STATUS_PASS)) {//if (CurrentValue.startsWith("P ")) {	fhgf
										//this.setStyle("-fx-background-color: green;");
										this.setStyle("-fx-text-fill: green;");
										if (!ProcalFeatureEnable.RESULT_STATUS_DISPLAY_ENABLE_FEATURE){
											CurrentValue = CurrentValue.substring(2,CurrentValue.length());
										}

										//ApplicationLauncher.logger.debug("insertTableValues :updateItem: Background color green");
									}  else  if (CurrentValue.startsWith(ConstantReport.RESULT_STATUS_FAIL)) {;//if (CurrentValue.startsWith("F ")) {	hgf
									//this.setStyle("-fx-background-color: red;");
									this.setStyle("-fx-text-fill: red;");
									if (!ProcalFeatureEnable.RESULT_STATUS_DISPLAY_ENABLE_FEATURE){
										CurrentValue = CurrentValue.substring(2,CurrentValue.length());
									}
									//ApplicationLauncher.logger.debug("insertTableValues :updateItem: Background color red");
									}else  if (CurrentValue.startsWith(ConstantReport.RESULT_STATUS_UNDEFINED)) {;//if (CurrentValue.startsWith("F ")) {	hgf
									//this.setStyle("-fx-background-color: red;");
									this.setStyle("-fx-text-fill: black;");
									if (!ProcalFeatureEnable.RESULT_STATUS_DISPLAY_ENABLE_FEATURE){
										CurrentValue = CurrentValue.substring(2,CurrentValue.length());
									}
									//ApplicationLauncher.logger.debug("insertTableValues :updateItem: Background color red");
									}	else  if (CurrentValue.isEmpty()){	
										this.setStyle("-fx-text-fill: black;");

									}

								} catch (Exception e){
									e.printStackTrace();
									ApplicationLauncher.logger.error("updateItem: Exception:"+e.getMessage());

								}
								setText(CurrentValue);

							}

						}

					});

				} catch (Exception e){
					e.printStackTrace();
					ApplicationLauncher.logger.error("insertTableValues :Exception1:"+ e.getMessage());
				}


			}

			//Platform.runLater(() -> {
			ref_tbl_liveStatus.getItems().setAll(FXCollections.observableList(rowValues));
			//});

		} catch (Exception e2){
			e2.printStackTrace();
			ApplicationLauncher.logger.error("insertTableValues :Exception2:"+ e2.getMessage());
		}
		//ApplicationLauncher.logger.debug("insertTableValues : Exit");
	}



	public ArrayList<String> gettestCases(ArrayList<Object> result){
		ArrayList<String> testCaseList = new ArrayList<String>();

		String testcase_name =  "";
		for(int i = 0; i < result.size(); i++){
			ResultDataModel row = (ResultDataModel) result.get(i);
			testcase_name =  row.gettestCaseName() + row.getaliasID();
			AddToTestList(testcase_name, testCaseList);
		}		
		ApplicationLauncher.logger.info(testCaseList);
		return testCaseList;
	}

	public void AddToTestList(String testcase_name, ArrayList<String> testCaseList){
		boolean isExists = false;
		if(testCaseList.size() != 0){
			for(int i = 0; i<testCaseList.size(); i++){
				if(testCaseList.get(i).equals(testcase_name)){
					isExists = true;
				}
			}
		}
		if(!isExists){
			testCaseList.add(testcase_name);
		}
	}

	public ArrayList<String> getDeviceList(ArrayList<Object> result){
		ArrayList<String> deviceList = new ArrayList<String>();
		String deviceName =  "";
		for(int i = 0; i < result.size(); i++){
			ResultDataModel row = (ResultDataModel) result.get(i);
			deviceName =  row.getdeviceName();
			AddToDeviceList(deviceName, deviceList);
		}		
		ApplicationLauncher.logger.info(deviceList);
		return deviceList;
	}

	public void AddToDeviceList(String deivceName, ArrayList<String> device_list){
		boolean isExists = false;
		if(device_list.size() != 0){
			for(int i = 0; i<device_list.size(); i++){
				if(device_list.get(i).equals(deivceName)){
					isExists = true;
				}
			}
		}
		if(!isExists){
			device_list.add(deivceName);
		}

	}
	public void DisableStartButton(){
		ref_btn_execution_start.setDisable(true);
	}



	public void DisableViewLogsButton(){
		ref_btnViewLogs.setDisable(true);
	}
	public static void EnableStopButton(){

		ref_btn_Stop.setDisable(false);
	}

	public void EnableStartButton(){
		if(isUacExecuteAuthorized()){
			ref_btn_execution_start.setDisable(false);
		}
		ApplicationHomeController.enableLeftMenuButtonsForTestRun();
	}
	public static void DisableStopButton(){

		ref_btn_Stop.setDisable(true);
	}

	public void DisableStepRunButton(){
		ref_btn_step_run.setDisable(true);
	}

	public void EnableStepRunButton(){
		if(isUacExecuteAuthorized()){
			ref_btn_step_run.setDisable(false);
		}
	}

	public void EnableViewLogsButton(){
		ref_btnViewLogs.setDisable(false);
	}

	public void DisableResumeButton(){
		ref_btn_Resume.setDisable(true);
	}

	public void EnableResumeButton(){
		if(isUacExecuteAuthorized()){
			ref_btn_Resume.setDisable(false);
		}
	}

	public void setProjectBarMax(){
		ProjectStatusRefresh.setProjectBarMax(getCurrentProject_TotalNoOfTestPoint());
	}
	public void ScanForSerialPorts(){
		ApplicationLauncher.logger.info("ScanForSerialPorts: Entry");
		ApplicationHomeController.update_left_status("Scanning serial ports",ConstantApp.LEFT_STATUS_DEBUG);
		SerialDM_Obj.ScanForSerialCommPort(); 
		/*		if(!ProcalFeatureEnable.POWERSOURCE_CONNECTED_NONE){
			if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
				BofaManager.scanForSerialCommPort();
			}
		}*/
	}

	public void setStepRunFlag(boolean value){
		StepRunFlag = value;
	}

	public static boolean getStepRunFlag(){
		return StepRunFlag;
	}

	public void setResumeFlag(boolean value){
		ResumeFlag = value;
	}

	public boolean getResumeFlag(){
		return ResumeFlag;
	}




	@FXML
	public void StartRunTrigger() {
		ApplicationLauncher.logger.info("StartRunTrigger: Entry");
		startRunTimer = new Timer();
		startRunTimer.schedule(new StartRunTaskClick(), 50);

	}
	
	public void procalRemoteStartTrigger() {
		ApplicationLauncher.logger.info("procalRemoteStartTrigger: Entry");
		startRunTimer = new Timer();
		startRunTimer.schedule(new StartRunTaskClick(), 50);

	}

	class StartRunTaskClick extends TimerTask {
		public void run() {
			
			getPresentTestPointStatus().setAllTestExecutionCompletedCheck(false);
			getPresentTestPointStatus().setAllTestExecutionCompleted(false);
			
			if(isCurrentProjectAutoDeployEnabled()) {
				executeConveyorMode();
			}else {
				execution_start_on_click();
			}

			startRunTimer.cancel();


		}


	}

	public void executeConveyorMode() {
		

		boolean status = false;
		DisableTestRunScreenButton();
		ApplicationHomeController.disableLeftMenuButtonsForTestRun();
		SerialDM_Obj.ScanForHarmonicsSerialCommPort(); 
		status = DisplayDataObj.validateConveyorComPortAccessible();
		if(status) {
			EnableStopButton();
			if(ProcalFeatureEnable.CONVEYOR_FEATURE_ENABLED){
				status = SerialDM_Obj.sendDataToConveyor(ConstantConveyor.CMD_START_CONVEYOR,"");
			}
			status = awaitingForStartCommandFromConveyor();
			if(status) {
				execution_start_on_click();

			}
		}else {
			EnableTestRunScreenButton();
			DisableStopButton();
			ApplicationHomeController.enableLeftMenuButtonsForTestRun();
		}

	}


	public boolean awaitingForStartCommandFromConveyor() {

		
		boolean status = SerialDM_Obj.awaitForStartCommandFromConveyor();

		return status;
	}

	public void execution_start_on_click(){
		ApplicationLauncher.logger.info("execution_start_on_click: Entry");
		setStepRunFlag(false);
		setResumeFlag(false);
		try {
			StartTestExecution();
		} catch (JSONException  e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("execution_start_on_click :JSONException:"+ e.getMessage());
		}catch (ParseException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("execution_start_on_click :ParseException:"+ e.getMessage());
		}
	}

	public void SetActiveReactivePulseConstant(){

		ApplicationLauncher.logger.debug("SetActiveReactivePulseConstant: Entry");
		DisplayDataObj.setRSSPulseRate(ConstantAppConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_DEFAULT);//setting default value

		if(DeviceDataManagerController.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)){
			//ApplicationLauncher.logger.debug("SetActiveReactivePulseConstant: Setting Active Pulse Constant:"+DisplayDataObj.RSS_ActivePulseConstant);
			//DisplayDataObj.setRSSPulseRate(DisplayDataObj.RSS_ActivePulseConstant);
			if(DeviceDataManagerController.getDeployedEM_CT_Type().equals(ConstantApp.METER_CT_TYPE_LTCT)){
				DisplayDataObj.setRSSPulseRate(ConstantAppConfig.RSS_LTCT_ACTIVE_PULSE_CONSTANT_DEFAULT);
			} else if(DeviceDataManagerController.getDeployedEM_CT_Type().equals(ConstantApp.METER_CT_TYPE_HTCT)){
				DisplayDataObj.setRSSPulseRate(ConstantAppConfig.RSS_HTCT_ACTIVE_PULSE_CONSTANT_DEFAULT);
			}
		}else if(DeviceDataManagerController.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)){
			//ApplicationLauncher.logger.debug("SetActiveReactivePulseConstant: Setting reactive Pulse Constant:"+DisplayDataObj.RSS_ReactivePulseConstant);
			//DisplayDataObj.setRSSPulseRate(DisplayDataObj.RSS_ReactivePulseConstant);
			if(DeviceDataManagerController.getDeployedEM_CT_Type().equals(ConstantApp.METER_CT_TYPE_LTCT)){
				DisplayDataObj.setRSSPulseRate(ConstantAppConfig.RSS_LTCT_REACTIVE_PULSE_CONSTANT_DEFAULT);
			} else if(DeviceDataManagerController.getDeployedEM_CT_Type().equals(ConstantApp.METER_CT_TYPE_HTCT)){
				DisplayDataObj.setRSSPulseRate(ConstantAppConfig.RSS_HTCT_REACTIVE_PULSE_CONSTANT_DEFAULT);
			}
		}
	}



	public void DisableTestRunScreenButton(){

		Platform.runLater(() -> {
			DisableStartButton();
			DisableViewLogsButton();

			DisableStepRunButton();
			DisableResumeButton();

		});
		DisableCloseProjectButton();
		disableSelectProjectComboBox();
		disableMainCtModeRadioButton();
		disableNeutralCtModeRadioButton();

	}

	public void StartTestExecution() throws JSONException, ParseException{
		ApplicationLauncher.logger.info("StartTestExecution: Entry");
		ApplicationHomeController.updateBottomSecondaryStatus("",ConstantApp.LEFT_STATUS_INFO);
		DisableTestRunScreenButton();

		if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
			try{
				
				/*BofaManager.comPortSemaphore.release();
				BofaManager.comPortSemaphore.release();
				BofaManager.comPortSemaphore.release();
				ApplicationLauncher.logger.debug("StartTestExecution : Bofa : Semaphore :released");
				ApplicationLauncher.logger.debug("StartTestExecution : availablePermits():" + BofaManager.comPortSemaphore.availablePermits());
				*/
				ApplicationLauncher.logger.debug("StartTestExecution : comPortSemaphore : Semaphore drainPermits");
				BofaManager.comPortSemaphore.drainPermits();
				ApplicationLauncher.logger.debug("StartTestExecution : comPortSemaphore : A-availablePermits : " + BofaManager.comPortSemaphore.availablePermits());
				ApplicationLauncher.logger.debug("StartTestExecution : comPortSemaphore : Semaphore : releasing...");
				BofaManager.comPortSemaphore.release();
				ApplicationLauncher.logger.debug("StartTestExecution : comPortSemaphore : Semaphore : released");
				ApplicationLauncher.logger.debug("StartTestExecution : comPortSemaphore : B-availablePermits : " + BofaManager.comPortSemaphore.availablePermits());
				
				
			}catch (Exception e){
				ApplicationLauncher.logger.error("StartTestExecution : comPortSemaphore : Semaphore Exception: " + e.getMessage());
			}
			
		}
		boolean status = true;

		if( (getStepRunFlag()) || getResumeFlag()){
			List<Object> row = ref_tbl_liveStatus.getSelectionModel().getSelectedItem();
			/*			try{
				String selected_testcase_name = (String) row.get(0);

			}catch(Exception e){
				//ApplicationLauncher.logger.info("getSelectedTestPoint : Exception");
				e.printStackTrace();
				ApplicationLauncher.logger.error("StartTestExecution :Exception:"+ e.getMessage());
				status = false;
				ApplicationLauncher.logger.debug("StartTestExecution: Test point not selected : ");
			}*/
			if(row == null){
				status = false;
				ApplicationLauncher.logger.debug("StartTestExecution: Test point not selected : ");
			}

		}

		if(status){
			ScanForSerialPorts();
			if(ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED) {
				status = DisplayDataObj.validateLscsSourceComPortAccessible();
			}else {
				status = DisplayDataObj.ValidateAllComPortAccessible();
			}
			//if(DisplayDataObj.ValidateAllComPortAccessible()){


			if(status) {

				if(ProcalFeatureEnable.METRICS_EXCEL_LOG_ENABLE_FEATURE){
					DeviceDataManagerController.setMetricsLogTestPointStartingEpochTimeInMSec(0);
					DeviceDataManagerController.setMetricsLogTestPointStartingAlreadyInitated(false);
					//String fileName = "MetricsLog.xlsx";//ConstantReport.ALL_PROJECT_REPORT_FILENAME;
					String projectName = getCurrentProjectName();//ConstantReport.ALL_PROJECT_REPORT_FILENAME;
					String presentTimeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());;
					String fileName = "MetricLog_"+projectName + "_" + presentTimeStamp+".xlsx";
					String file_path = "c:\\Reports\\Metrics\\";
					DisplayDataObj.setMetricsLogAcceptedLowerLimit(-0.25f);
					DisplayDataObj.setMetricsLogAcceptedUpperLimit(0.25f);
					DeviceDataManagerController.setPresentMetricLogFileName(fileName);
					DeviceDataManagerController.createMetricsExcelLogFile(file_path,fileName);
					
					//createMetricsExcelLogFile();
				}
				setExecutionInProgress(true);
				//DisableTestRunScreenButton();
				//if(!ProcalFeatureEnable.PROPOWER_SRC_ONLY ){
				if(ProcalFeatureEnable.PROPOWER_SRC_ONLY ){
					//EnableStopButton();
					disableTabManualMode();
					if((!ProcalFeatureEnable.REFSTD_CONNECTED_NONE)){
						ApplicationLauncher.logger.debug("StartTestExecution: EnableStopButton hit");
						EnableStopButton();
					}
				}else{
					EnableStopButton();
				}

				setUserAbortedFlag(false);
				SetActiveReactivePulseConstant();
				ApplicationLauncher.logger.debug("StartTestExecution: get_EM_Model_type:" + DeviceDataManagerController.getDeployedEM_ModelType());

				setPresentMeterType(DeviceDataManagerController.getDeployedEM_ModelType());
				//if((ProCalCustomerConfiguration.ELECTROBYTE_HYBRID_2NO_3PHASE_10NO_1PHASE_POSITION_2022) 
				if((ProcalFeatureEnable.RACK_HYBRID_MODE_ENABLED) 	
						&& (getPresentMeterType().startsWith(ConstantApp.METERTYPE_THREEPHASE))){
					//int electroByte3PhaseNoOfSupportedRacks  = ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_POSITIONS;
					int noOfSupportedRacks3Phase  = ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_POSITIONS;
					ApplicationLauncher.logger.debug("StartTestExecution: noOfSupportedRacks3Phase : " + noOfSupportedRacks3Phase);
					DeploymentManagerController.set_no_of_racks(noOfSupportedRacks3Phase);
				}else if((ProcalFeatureEnable.RACK_HYBRID_MODE_ENABLED) 	
						&& (getPresentMeterType().startsWith(ConstantApp.METERTYPE_SINGLEPHASE))){
					int noOfSupportedRacks3Phase  = ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_POSITIONS;
					ApplicationLauncher.logger.debug("StartTestExecution: noOfSupportedRacks3Phase : " + noOfSupportedRacks3Phase);
					DeploymentManagerController.set_no_of_racks(noOfSupportedRacks3Phase);
				}
				ApplicationHomeController.disableLeftMenuButtonsForTestRun();
				setPowerSrcContinuousFailureStatus(false);
				setRefStdFeedBackContinuousFailureStatus(false);
				resetRefStdFeedBackContinuousFailureCounter();
				setProcessTC_ExecutionRefreshTimeInMSec(ProcessTC_ExecutionRefreshTimeInMSec);
				SerialDM_Obj.ClearStdRefSerialData();
				DeviceDataManagerController.resetLastSetPowerSourceFrequency();

				SerialDataRadiantRefStd.ClearRefStd_ReadSerialData();
				String project_name = getCurrentProjectName();//MeterParamsController.getCurrentProjectName();
				String deploymentId = getSelectedDeployment_ID();
				if(getStepRunFlag()){

					getSelectedTestPoint(project_name,deploymentId);
					setOneTimeExecuted(false);
				}
				else if(getResumeFlag()){
					setSelectedTestPointAsStartIndex();  
				}
				else{
					setCurrentTestPoint_Index(0);
				}
				ApplicationLauncher.logger.debug("StartTestExecution: getCurrentTestPoint_Index : " + getCurrentTestPoint_Index());
				setListOfDevices();
				updateDeviceMountedMap();/// created replacement for setListOfDevices for optimisation
				setAll_TestPoint_CompletedStatus(false);
				setCurrentProject_TotalNoOfTestPoint(CurrentProjectTestPoint_List.length());
				
//				HashMap<String, String> testPointStatusMap = new HashMap<>();
				getRemoteTestPointStatusMap().clear();
				
				JSONObject currentprojecttestpoint_ = new JSONObject();
				String current_project_test_case = "";
				ArrayList<String> testPointIdList = new ArrayList<String> ();
				
				for (int j = 0; j < CurrentProjectTestPoint_List.length(); j++) {
					// ApplicationLauncher.logger.info("refreshDataFromResultDB: j = "+j);
					currentprojecttestpoint_ = (JSONObject) CurrentProjectTestPoint_List.get(j);
					current_project_test_case = currentprojecttestpoint_.getString("test_case");
					testPointIdList.add(current_project_test_case);
					
					getRemoteTestPointStatusMap().put(current_project_test_case, "");
				}
				
				setProjectBarMax();
				ResetRefStdProgressCounter();
				resetPowerSrcContinuousFailureCounter();
				resetRefStdFeedBackContinuousFailureCounter();
				setCurrentTestPoint_ExecutionCompletedStatus(true);
				semLockExecutionInprogress = false;
				ApplicationLauncher.logger.info("StartTestExecution: semLockExecutionInprogress: released-reset");
				/*				if(ProcalFeatureEnable.PROPOWER_SRC_ONLY ){

					DisableStepRunButton();
					enableBtnLoadNext();
				}*/
				if(ProcalFeatureEnable.BOFA_LDU_CONNECTED){
					Data_LduBofa.resetNthOfErrors();
					Data_LduBofa.resetErrorValue();
					Data_LduBofa.resetDialTestPulseCount();
					Data_LduBofa.resetStaCreepTestPulseCount();
					//Data_LduBofa.resetStepRunModeAtleastOneResultReadCompleted();
				}
				
				DeviceDataManagerController.resetStepRunModeAtleastOneResultReadCompleted();
				CheckForInitAndProcessAllTestCaseTrigger();
				ApplicationLauncher.logger.info("StartTestExecution: exit");

			}else {
				ApplicationLauncher.logger.info("StartTestExecution: EnableTestRunScreenButton");
				if(ProcalFeatureEnable.REFSTD_PORT_MANAGER_V2_ENABLED) {
					DisplayDataObj.disconnectRefStdSerialCommIfConnected();
				}
				EnableTestRunScreenButton();

			}

		}else {
			ApplicationLauncher.logger.info("StartTestExecution: Test Point not selected - Kindly select atleast one test point to proceed the execution! - Prompted ");
			ApplicationLauncher.InformUser("Test Point not selected","Kindly select atleast one test point to proceed the execution!",AlertType.ERROR);
			EnableTestRunScreenButton();

		}
	}

	

	public JSONArray getSelectedTestPoint(String project_name, String deploymentId) throws JSONException{
		ApplicationLauncher.logger.info("getSelectedTestPoint : Entry");
		JSONArray deployed_testpoints = getListOfTestPoints(project_name,deploymentId);
		List<Object> row = ref_tbl_liveStatus.getSelectionModel().getSelectedItem();
		String selected_testcase_name = (String) row.get(0);
		int index = ref_tbl_liveStatus.getSelectionModel().getSelectedIndex();
		setCurrentTestPoint_Index(index);
		JSONArray selectedTestPoint = new JSONArray();
		JSONObject jobj = new JSONObject();
		String test_point = "";
		try{
			for(int i=0; i<deployed_testpoints.length(); i++){
				jobj = getCurrentProjectTestPointList().getJSONObject(i);
				test_point = jobj.getString("test_case");
				if(test_point.equals(selected_testcase_name)){
					selectedTestPoint.put(jobj); 
				}

			}
		}
		catch(JSONException e){
			//ApplicationLauncher.logger.info("getSelectedTestPoint : Exception");
			e.printStackTrace();
			ApplicationLauncher.logger.error("getSelectedTestPoint :JSONException:"+ e.getMessage());
		}
		return selectedTestPoint;
	}

	public static boolean isMainCtMode() {
		return mainCtMode;
	}

	public static void setMainCtMode(boolean mainCtMode) {
		ProjectExecutionController.mainCtMode = mainCtMode;
	}

	public static boolean isNeutralCtMode() {
		return neutralCtMode;
	}

	public static void setNeutralCtMode(boolean neutralCtMode) {
		ProjectExecutionController.neutralCtMode = neutralCtMode;
	}

	public void cancelProcessAllTestCasesTimer(){
		try{
			TC_ExecutionTimer.cancel();
			ApplicationLauncher.logger.info("cancelProcessAllTestCasesTimer :  TC_ExecutionTimer cancelled");

		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("cancelProcessAllTestCasesTimer: Exception : "+e.getMessage());
		}
	}

	public void ProcessAllTestCasesTimerTrigger() {
		if(!getUserAbortedFlag()){
			TC_ExecutionTimer = new Timer();
			TC_ExecutionTimer.schedule(new ProcessAllTestCases(),100);// 1000);
			ApplicationLauncher.logger.info("ProcessAllTestCasesTimerTrigger Invoked:");
		}else{
			ApplicationLauncher.logger.info("ProcessAllTestCasesTimerTrigger: User Aborted:");
		}
	}

	public void MessageDisplay(String Message, AlertType AlertValue  ){

		Platform.runLater(new Runnable(){

			@Override
			public void run() {
				Alert alert = new Alert(AlertValue, Message, ButtonType.OK);
				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
				alert.showAndWait();
			}
		});

	}

	public boolean refStdValidateSerialPortCommand(){
		ApplicationLauncher.logger.info("refStdValidateSerialPortCommand: Entry");

		boolean status = false;
		try{
			if (ProcalFeatureEnable.REFSTD_CONNECTED_NONE){
				status = true;
			}else if(ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED){
				status = SerialDM_Obj.RefStd_ValidateNOP_CMD();
			}else if(ProcalFeatureEnable.SANDS_REFSTD_CONNECTED){
				status = SerialDM_Obj.sandsRefStdGetConfigTask();
				if(status){

					DeviceDataManagerController.setSandsRefStdLastSetVoltageMode(Data_RefStdSands.getReadConfigModeVoltage());
					DeviceDataManagerController.setSandsRefStdLastSetCurrentMode(Data_RefStdSands.getReadConfigModeCurrent());
					DeviceDataManagerController.setSandsRefStdLastSetPulseOutputMode(Data_RefStdSands.getReadConfigModePulseOutputUnit());

				}
			}else if(ProcalFeatureEnable.KRE_REFSTD_CONNECTED){
				//status = SerialDM_Obj.kreRefStdReadSettingTask();
				Data_RefStdKre.setWriteSettingIstall(ConstantRefStdKre.CURRENT_MAX);
				if(ProcalFeatureEnable.REFSTD_PORT_MANAGER_V2_ENABLED) {
					//displayDataObj.refStdEnableSerialMonitoring();
					//DisplayDataObj.refStdEnableSerialMonitoring_V2();

					RefStdDirector refStdKreDirector = new RefStdDirector();
					status = refStdKreDirector.refStdWriteSettingTask();
					ApplicationLauncher.logger.debug("refStdValidateSerialPortCommand : status: " + status);
					if(status){
						SerialDataManager.setRefComSerialStatusConnected(true);
					}
				}else {
					status = SerialDM_Obj.kreRefStdWriteSettingTask();
				}


			}else if(ProcalFeatureEnable.KIGGS_REFSTD_CONNECTED){ // #KIGGSREFSTD
				//status = SerialDM_Obj.kreRefStdReadSettingTask();
				//Data_RefStdKre.setWriteSettingIstall(ConstantRefStdKre.CURRENT_MAX);
				//status = SerialDM_Obj.kreRefStdWriteSettingTask();
				status = SerialDM_Obj.kiggsReadRefStdVoltAndCurrentTapRange() ;
				//status = true;

			}else if (ProcalFeatureEnable.BOFA_REFSTD_CONNECTED){
				status = true;
			}
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("refStdValidateSerialPortCommand : UnsupportedEncodingException: " + e.getMessage());
			setExecutionInProgress(false);
		}
		ApplicationLauncher.logger.info("refStdValidateSerialPortCommand: Exit");

		return status;

	}

	public boolean ValidateAllSerialPortCommand(){
		ApplicationLauncher.logger.info("ValidateAllSerialPortCommand: Invoked");
		boolean status = false;
		try {

			status = refStdValidateSerialPortCommand();
			if (status){
				ApplicationLauncher.logger.info("ValidateAllSerialPortCommand : RefStd Serial Port command validation: Success");
				if (ProcalFeatureEnable.POWERSOURCE_CONNECTED_NONE){

					ApplicationLauncher.logger.info("ValidateAllSerialPortCommand: power source configured as none connected");
					status =true;

				} else if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
					//status = SerialDM_Obj.SetPowerSourceOffWithInit();//SetPowerSourceOff();
					status = SerialDM_Obj.SetPowerSourceOffWithInitV2();
				} else if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
					status = true;//SerialDM_Obj.SetPowerSourceOffWithInit();//SetPowerSourceOff();
				}else{
					if(!SerialDataManager.isPowerSourceTurnedOff()){
						status = SerialDM_Obj.SetPowerSourceOff();
					}else{
						status = true;
					}
				}
				if (status){
					DisplayDataObj.set_Error_min("-1.00");
					DisplayDataObj.set_Error_max("+1.00");
					DeviceDataManagerController.setDutImpulsesPerUnit("2500");
					DisplayDataObj.setNoOfPulses("10");
					ApplicationLauncher.logger.info("ValidateAllSerialPortCommand : Power Source Serial Port command validation: Success");
					DevicePortSetupController.setPortValidationTurnedON(true);
					if(ProcalFeatureEnable.LDU_CONNECTED_NONE) {
						ApplicationLauncher.logger.info("ValidateAllSerialPortCommand : LDU_CONNECTED_NONE");
						status = true;
					}else if(ProcalFeatureEnable.CCUBE_LDU_CONNECTED) {
						status = SerialDM_Obj.LDU_ResetSetting();
					}else if(ProcalFeatureEnable.LSCS_LDU_CONNECTED){
						status = SerialDM_Obj.lscsLDU_CheckCom();
					}

					if(ProcalFeatureEnable.MTE_REFSTD_CONNECTED){
						mteStartRefStdReading();
						//Sleep(5000);
						//status = powerUpDeviceUnderTest();
						//status= true;
					}

					if(ProcalFeatureEnable.KRE_REFSTD_CONNECTED){
						kreStartRefStdReading();
						//Sleep(5000);
						//status = powerUpDeviceUnderTest();
						//status= true;


					}

					if(ProcalFeatureEnable.BOFA_REFSTD_CONNECTED){
						// Since already serial port ic validated....not required to validate the command again
						//bofaStartRefStdReading();
						//Sleep(5000);
						//status = powerUpDeviceUnderTest();
						//status= true;


					}



					if(ProcalFeatureEnable.KIGGS_REFSTD_CONNECTED){
						status = SerialDM_Obj.kiggsRefStd_ConfigureBNC_Output();
						//Sleep(1000);
						//status = SerialDM_Obj.kiggsRefStdStateSettingBasicMeasurement();

					}



					if(ProcalFeatureEnable.BOFA_REFSTD_CONNECTED){

						bofaStartRefStdReading();
						//status = SerialDM_Obj.kiggsRefStd_ConfigureBNC_Output();
						//Sleep(1000);
						//status = SerialDM_Obj.kiggsRefStdStateSettingBasicMeasurement();

					}

					DevicePortSetupController.setPortValidationTurnedON(false);
					if (status){
						ApplicationLauncher.logger.info("ValidateAllSerialPortCommand : LDU Serial Port command validation: Success");
						ApplicationLauncher.logger.info("ValidateAllSerialPortCommand : All Serial Port Command Validation: Success");
					}else{
						SerialDM_Obj.DisconnectLDU_SerialComm();
						SerialDM_Obj.DisconnectPwrSrc();
						if(!ProcalFeatureEnable.REFSTD_CONNECTED_NONE){
							SerialDM_Obj.DisconnectRefSerialComm();
						}
						ApplicationLauncher.logger.info("ValidateAllSerialPortCommand : LDU Serial Port command validation: Failed");
						MessageDisplay("Serial Port Command failed for LDU Serial Port.",AlertType.ERROR);

					}
				}else{
					SerialDM_Obj.DisconnectPwrSrc();
					if(!ProcalFeatureEnable.REFSTD_CONNECTED_NONE){
						if(ProcalFeatureEnable.REFSTD_PORT_MANAGER_V2_ENABLED) {
							DisplayDataObj.disconnectRefStdSerialCommIfConnected();
						}else{
							SerialDM_Obj.DisconnectRefSerialComm();
						}
					}
					if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
						if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_FEATURE_ENABLED){
							SerialDM_Obj.DisConnectHarmonicsSrc();
						}
					}
					if(!getUserAbortedFlag()){
						ApplicationLauncher.logger.info("ValidateAllSerialPortCommand : Power Source Serial Port command validation: Failed");
						MessageDisplay("Serial Port Command failed for Power Source Serial Port.",AlertType.ERROR);
					}
					setExecutionInProgress(false);
				}
			}else{


				if(!ProcalFeatureEnable.REFSTD_CONNECTED_NONE){
					ApplicationLauncher.logger.info("ValidateAllSerialPortCommand : RefStd Serial Port disconnect");
					if(ProcalFeatureEnable.REFSTD_PORT_MANAGER_V2_ENABLED) {
						DisplayDataObj.disconnectRefStdSerialCommIfConnected();
					}else{
						SerialDM_Obj.DisconnectRefSerialComm();
					}
				}
				// Fix for #RefStdSerialPortCrash_2022_09_24
				if(!ProcalFeatureEnable.LDU_CONNECTED_NONE) {
					ApplicationLauncher.logger.info("ValidateAllSerialPortCommand : LDU Serial Port disconnect");
					SerialDM_Obj.DisconnectLDU_SerialComm();
				}
				// Fix for #RefStdSerialPortCrash_2022_09_24
				if (!ProcalFeatureEnable.POWERSOURCE_CONNECTED_NONE){
					ApplicationLauncher.logger.info("ValidateAllSerialPortCommand : PowerSource Serial Port disconnect");
					SerialDM_Obj.DisconnectPwrSrc();
				}
				ApplicationLauncher.logger.info("ValidateAllSerialPortCommand : RefStd Serial Port command validation: Failed");
				MessageDisplay("Serial Port Command failed for Reference Standard Serial Port.",AlertType.ERROR);
				setExecutionInProgress(false);
			}
		} catch (Exception e) {
			//} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("ValidateAllSerialPortCommand : Exception: " + e.getMessage());
			setExecutionInProgress(false);
		}
		ApplicationLauncher.logger.debug("ValidateAllSerialPortCommand: Exit");

		return status;	
	}



	public void CheckForInitAndProcessAllTestCaseTrigger() {
		CheckForSerialCommandTimer = new Timer();
		CheckForSerialCommandTimer.schedule(new ValidateSerialPortCommandTimer(),100);// 1000);
		ApplicationLauncher.logger.info("CheckForInitAndProcessAllTestCaseTrigger Invoked:");

	}
	public void StartRefStdReading(){
		boolean status = true;

		if(ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED){
			status = RefStdRelaySetting();
		}
		if(ProcalFeatureEnable.KRE_REFSTD_CONNECTED){
			status = false;
		}
		if(ProcalFeatureEnable.KIGGS_REFSTD_CONNECTED){
			status = false;  //#DEBUG_2022_09_08_REF_STD_KIGG #REFSTD_READ_DATA_REFRESH_DELAY_FIX added this to fix the delay in refstd data delay
		}
		if(status){
			RefStd_PreRequisite();
		}
	}
	public void mteStartRefStdReading(){
		//if(RefStdRelaySetting()){
		mteRefStd_PreRequisite();
		//}
	}	

	public void kreStartRefStdReading(){
		//if(RefStdRelaySetting()){
		kreRefStd_PreRequisite();
		//}
	}	

	public void bofaStartRefStdReading(){
		//if(RefStdRelaySetting()){
		bofaRefStd_PreRequisite();
		//}
	}

	public void kiggsStartRefStdReading(){
		//if(RefStdRelaySetting()){
		kiggsRefStd_PreRequisite();
		//}
	}


	class ValidateSerialPortCommandTimer extends TimerTask {
		public void run() {
			ApplicationLauncher.logger.info("ValidateSerialPortCommandTimer: Invoked");
			boolean status = false;
			String metertype = DeviceDataManagerController.getDeployedEM_ModelType();
			if(ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED) {
				if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
					status = SerialDM_Obj.SetPowerSourceOffWithInit();
				}else{
					if(!SerialDataManager.isPowerSourceTurnedOff()){
						status = SerialDM_Obj.SetPowerSourceOff();
					}else{
						status = true;
					}
				}
				ApplicationLauncher.logger.info("ValidateSerialPortCommandTimer : test1"  );
				if(status) {
					ApplicationLauncher.logger.info("ValidateSerialPortCommandTimer : test2"  );
					if(ProcalFeatureEnable.KRE_REFSTD_CONNECTED){
						ApplicationLauncher.logger.info("ValidateSerialPortCommandTimer : test3"  );
						status = refStdValidateSerialPortCommand();
						if(status){
							ApplicationLauncher.logger.info("ValidateSerialPortCommandTimer : test4"  );
							kreStartRefStdReading();
						}
						
						
					}

					if(ProcalFeatureEnable.BOFA_REFSTD_CONNECTED){
						//ApplicationLauncher.logger.info("ValidateSerialPortCommandTimer : test3"  );
						if(ProcalFeatureEnable.BOFA_LDU_CONNECTED){
							Data_LduBofa.bofaSendLduResetErrorAll();
						}
						bofaStartRefStdReading();
					}
					if(status) {
						if(metertype.contains(ConstantApp.METERTYPE_SINGLEPHASE)){
							ApplicationLauncher.logger.info("ValidateSerialPortCommandTimer : testX1"  );
							if(ProcalFeatureEnable.RACK_MCT_NCT_ENABLED){
								ApplicationLauncher.logger.info("ValidateSerialPortCommandTimer : testX2"  );
								//Sleep(5000);
								Sleep(1000);
								status = SerialDM_Obj.lscsSetPowerSourceMctNctMode(DeviceDataManagerController.getExecutionMctNctMode(),false);
							}
						}else if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
							if(ProcalFeatureEnable.ICT_INTERFACE_ENABLED){
								SerialDM_Obj.lscsSetPowerSourceIctMode(true);
							}else if(ProcalFeatureEnable.ICT_KRE_KE6323_CONNECTED){
								SerialDM_Obj.lscsSetPowerSourceIctMode(true);
							}
						}
						//Sleep(5000);//required some time delay for the next commmand 
						SleepForSecondsWithAbortMonitoring(5);
						if(!getUserAbortedFlag()) {
							ProcessAllTestCasesTimerTrigger();
						}else {
							ApplicationLauncher.logger.debug("ValidateSerialPortCommandTimer : ProcessAllTestCasesTimerTrigger not triggered");
						}
					}
				}
			}else {

				status = ValidateAllSerialPortCommand();
				if (status){
					ApplicationLauncher.logger.info("ValidateSerialPortCommandTimer: Status    : " + status);
					ApplicationLauncher.logger.info("ValidateSerialPortCommandTimer: MeterType : " + metertype);

					SerialDM_Obj.PowerSrcSoftwareReboot();
					//String metertype = DisplayDataObj.getDeployedEM_ModelType();

					if(metertype.contains(ConstantApp.METERTYPE_SINGLEPHASE)){
						ApplicationLauncher.logger.info("ValidateSerialPortCommandTimer: Test1" );
						ApplicationLauncher.logger.info("ProcalFeatureEnable.RACK_MCT_NCT_ENABLED: " + ProcalFeatureEnable.RACK_MCT_NCT_ENABLED);

						if(ProcalFeatureEnable.RACK_MCT_NCT_ENABLED){
							ApplicationLauncher.logger.info("ValidateSerialPortCommandTimer: Test2" );

							//Sleep(5000);
							Sleep(1000);
							
							if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
								status =  getPowerSourceDirector().setPowerSourceMctNctMode(DeviceDataManagerController.getExecutionMctNctMode(),false);
						
							}else{
								status = SerialDM_Obj.lscsSetPowerSourceMctNctMode(DeviceDataManagerController.getExecutionMctNctMode(),false);
								
							}
						}
					}else if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
						if(ProcalFeatureEnable.ICT_INTERFACE_ENABLED){
							SerialDM_Obj.lscsSetPowerSourceIctMode(true);
						}else if(ProcalFeatureEnable.ICT_KRE_KE6323_CONNECTED){
							SerialDM_Obj.lscsSetPowerSourceIctMode(true);
						}
					}
					if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
						ApplicationLauncher.logger.debug("ValidateSerialPortCommandTimer : Sleep 1000 delay");
						Sleep(1000);
						ApplicationLauncher.logger.debug("ValidateSerialPortCommandTimer : Sleep 1000 delay awake");
					}
					if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_FEATURE_ENABLED){
						//lscsSetDisableHarmonicsAtSlave();  
					}
					//status = SerialDM_Obj.lscsSetPowerSourceMctNctMode(DisplayDataObj.getExecutionMctNctMode());
					if(status){
						//if(ConstantFeatureEnable.POWER_SOURCE_3PHASE_ENABLED){
						if(ProcalFeatureEnable.RADIANT_REFSTD_CONNECTED){
							status = SerialDM_Obj.RefStd_ConfigureBNC_Output();
						}else if(ProcalFeatureEnable.SANDS_REFSTD_CONNECTED){

						}else if(ProcalFeatureEnable.KRE_REFSTD_CONNECTED){

						}else if(ProcalFeatureEnable.KIGGS_REFSTD_CONNECTED){
							//ApplicationLauncher.logger.info("ValidateSerialPortCommandTimer : test3"  );
							kiggsStartRefStdReading();
						}

						//}
						//if(SerialDM_Obj.RefStd_ConfigureBNC_Output()){bmhv
						if(status){
							Sleep(1000);//required some time delay for the next commmand processing for reference standard device
							//SerialDM_Obj.RefStd_ConfigureBNC_Constant1();
							//Sleep(1000);//required some time delay for the next commmand processing for reference standard device
							StartRefStdReading();
							if(ProcalFeatureEnable.ICT_INTERFACE_ENABLED){
								if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
									if(ProcalFeatureEnable.ICT_KRE_KE6323_CONNECTED){
										SerialDM_Obj.kreIctResetAllState();
									}
								}
							}

							Sleep(2000);//5000 required some time delay for the next commmand 
							
							ProcessAllTestCasesTimerTrigger();
							//StartRefStdReading();

						} else{
							ApplicationLauncher.logger.info("ValidateSerialPortCommandTimer : Unable to set the BNC output. Try after rebooting Reference standard device");
							//Alert alert = new Alert(AlertType.ERROR, "Unable to set the BNC output. Try after rebooting Reference standard device", ButtonType.OK);
							//alert.showAndWait();
							ApplicationLauncher.InformUser("RefStd-BNC setting","Reference Standard device: Unable to set the BNC output. Try after rebooting Reference standard device",AlertType.ERROR);

						}

					} else{

						if(!getUserAbortedFlag()){
							
							
							
							ApplicationLauncher.logger.info("ValidateSerialPortCommandTimer : Unable to set the Main Ct/Neutral CT mode. Kindly retry again");
							//Alert alert = new Alert(AlertType.ERROR, "Unable to set the BNC output. Try after rebooting Reference standard device", ButtonType.OK);
							//alert.showAndWait();
							ApplicationLauncher.InformUser("Power Source mode setting","Power Source: Unable to set the Main Ct/Neutral CT mode. Kindly retry again",AlertType.ERROR);
							if(ProcalFeatureEnable.PWRSRC_PORT_MANAGER_V2_ENABLED) {
								DisplayDataObj.pwrSrcDisconnectPort_V2();
							}
							EnableStartButton();
							EnableViewLogsButton();
							DisableStopButton();
							EnableResumeButton();
							EnableStepRunButton();
							setExecutionInProgress(false);
							
							
							//ApplicationLauncher.logger.info("ValidateSerialPortCommandTimer :setRefStdReadDataFlag-2 to false");
							//DisplayDataObj.setRefStdReadDataFlag( false);
							
						}
					}

				}else{
					ApplicationLauncher.logger.info("ValidateSerialPortCommandTimer :ValidateAllSerialPort validation failed");
					EnableStartButton();
					EnableViewLogsButton();
					DisableStopButton();
					EnableResumeButton();
					EnableStepRunButton();
					setExecutionInProgress(false);
					ApplicationLauncher.logger.info("ValidateSerialPortCommandTimer :setRefStdReadDataFlag to false");
					DeviceDataManagerController.setRefStdReadDataFlag( false);

				}
			}

			CheckForSerialCommandTimer.cancel();

		}
	}

	class TestPointExecution extends TimerTask {
		public void run() {
			ApplicationLauncher.logger.info("TestPointExecution: Invoked");
			JSONObject jobj;
			try {
				ApplicationLauncher.logger.info("TestPointExecution: semLockExecutionInprogress: awaiting...");
				if(!semLockExecutionInprogress){
					semLockExecutionInprogress = true;
					ApplicationLauncher.logger.info("TestPointExecution: semLockExecutionInprogress: acquired");
					jobj = getCurrentProjectTestPointList().getJSONObject(getCurrentTestPoint_Index());
					if(!getUserAbortedFlag()){

						execute_test(jobj);
					}else{
						ApplicationLauncher.logger.info("TestPointExecution: User Aborted");
					}
				}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("TestPointExecution :JSONException:"+ e.getMessage());
			}


		}
	}

	public static boolean getOneTimeExecuted(){
		return OneTimeExecuted;
	}

	public static void setOneTimeExecuted(boolean value){
		OneTimeExecuted = value;
	}


	class ProcessAllTestCases extends TimerTask {
		public void run() {
			ApplicationLauncher.logger.info("ProcessAllTestCases: Invoked" );
			if (!getAll_TestPoint_CompletedStatus()){
				//ApplicationLauncher.logger.debug("ProcessAllTestCases: Test1" );
				if(getStepRunFlag()){
					//ApplicationLauncher.logger.debug("ProcessAllTestCases: Test2" );
					if(!getOneTimeExecuted()){
						//ApplicationLauncher.logger.debug("ProcessAllTestCases: Test3" );
						if(getCurrentTestPoint_ExecutionCompletedStatus()){
							//ApplicationLauncher.logger.debug("ProcessAllTestCases: Test4" );
							Timer TestPointExecutionTimer = new Timer();
							TestPointExecutionTimer.schedule(new TestPointExecution(),100);// 1000);

						}
					}else if (getCurrentTestPoint_ExecutionCompletedStatus()){
						//ApplicationLauncher.logger.debug("ProcessAllTestCases: Test5" );
						if(ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED){
							////ApplicationLauncher.logger.debug("ProcessAllTestCases: Test6" );
							ApplicationLauncher.logger.info("ProcessAllTestCases: calibration mode" );
							Timer TestPointExecutionTimer = new Timer();
							TestPointExecutionTimer.schedule(new TestPointExecution(),100);// 1000);
						}else if(ProcalFeatureEnable.PROPOWER_SRC_ONLY){
							//ApplicationLauncher.logger.debug("ProcessAllTestCases: Test7" );
							ApplicationLauncher.logger.info("ProcessAllTestCases: ProPower mode" );
							Timer TestPointExecutionTimer = new Timer();
							TestPointExecutionTimer.schedule(new TestPointExecution(),100);// 1000);
						}else if(ProcalFeatureEnable.CONVEYOR_CALIB_BOFA_FEATURE_ENABLED){
							//ApplicationLauncher.logger.debug("ProcessAllTestCases: Test7" );
							ApplicationLauncher.logger.info("ProcessAllTestCases: Conveyor Calibration mode" );
							Timer TestPointExecutionTimer = new Timer();
							TestPointExecutionTimer.schedule(new TestPointExecution(),100);// 1000);
						}else{
							//ApplicationLauncher.logger.debug("ProcessAllTestCases: Test8" );
							ApplicationLauncher.logger.info("ProcessAllTestCases1: All TestPoint Completed" );
							setAll_TestPoint_CompletedStatus(true);
							ProjectExitProcess();
						}
					}
				}
				else{
					//ApplicationLauncher.logger.debug("ProcessAllTestCases: Test9" );
					if(getCurrentTestPoint_Index()<getCurrentProject_TotalNoOfTestPoint()){
						//ApplicationLauncher.logger.debug("ProcessAllTestCases: Test10" );
						if(getCurrentTestPoint_ExecutionCompletedStatus()){
							//ApplicationLauncher.logger.debug("ProcessAllTestCases: Test11" );
							Timer TestPointExecutionTimer = new Timer();
							TestPointExecutionTimer.schedule(new TestPointExecution(),100);// 1000);
						}
					}else if (getCurrentTestPoint_ExecutionCompletedStatus()){
						//ApplicationLauncher.logger.debug("ProcessAllTestCases: Test12" );
						ApplicationLauncher.logger.info("ProcessAllTestCases2: All TestPoint Completed" );
						setAll_TestPoint_CompletedStatus(true);
						ProjectExitProcess();

					}
				}

				if(!getUserAbortedFlag()){
					TC_ExecutionTimer.schedule(new ProcessAllTestCases(), ProcessTC_ExecutionRefreshTimeInMSec);
				}

			}
			else{
				ApplicationLauncher.logger.debug("ProcessAllTestCases: Test12" );
				ApplicationLauncher.logger.info("ProcessAllTestCases :Timer Exit!%n");
				TC_ExecutionTimer.cancel(); //Terminate the timer thread
			}
		}
	}

	public static JSONArray getListOfTestPoints(String project_name, String deploymentID) throws JSONException{
		ApplicationLauncher.logger.debug("getListOfTestPoints : Entry");
		JSONObject testcaselist = MySQL_Controller.sp_getdeploy_test_cases(project_name,deploymentID);
		JSONArray testcases = testcaselist.getJSONArray("Test_cases");
		//ApplicationLauncher.logger.debug("testcases JSONArray: " + testcases);

		return testcases;
	}

	public void UpdateDevicesDataTableRowHighlight(){
		ApplicationLauncher.logger.info("UpdateDevicesDataTableRowHighlight : Entry");
		Platform.runLater(() -> {
			ref_tbl_liveStatus.getSelectionModel().select(getCurrentTestPoint_Index());
			ApplicationLauncher.logger.info("UpdateDevicesDataTableRowHighlight : Row Higlighted");
		});
		tvX.getFirstVisibleIndex();
		tvX.getLastVisibleIndex();
		tvX.scrollToIndex(getCurrentTestPoint_Index());

		ApplicationLauncher.logger.info("UpdateDevicesDataTableRowHighlight : Exit");
	}

	public void updateLduSecondaryDisplayTestPoint(){

		SecondaryLduDisplayController.updateLduSecondarydisplayTestPoint(getCurrentTestPoint_Index());
	}


	public void ClearDataOnCurrentRow(){
		ApplicationLauncher.logger.info("ClearDataOnCurrentRow : Entry");
		getCurrentProjectName();
		JSONObject devices_json = DisplayDataObj.getDeployedDevicesJson();//MySQL_Controller.sp_getdeploy_devices(project_name);

		String rack_id = "";
		JSONArray devices;
		try {
			devices = devices_json.getJSONArray("Devices");
			JSONObject jobj = new JSONObject();
			for(int i=0;i<devices.length(); i++){
				jobj = devices.getJSONObject(i);
				//rack_id = jobj.getString("Rack_ID");
				rack_id = String.valueOf(jobj.getInt("Rack_ID"));
				LiveTableDataManager.UpdateliveTableData(Integer.parseInt(rack_id), "","");
			}

		} catch (JSONException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("ClearDataOnCurrentRow :JSONException:"+ e.getMessage());
		}

		ApplicationLauncher.logger.info("ClearDataOnCurrentRow : Exit");
	}

	public boolean waitForFinalMeterReading(){
		ApplicationLauncher.logger.info("waitForFinalMeterReading :Entry");
		boolean ReadingReceived = false;
		int no_of_devices_connected = DisplayDataObj.get_no_of_devices_connected();
		while((no_of_devices_connected != DeviceDataManagerController.getFinalMeterValues().length()) && (!getUserAbortedFlag()) ){
			ApplicationLauncher.logger.info("Waiting for final meter input");
			Sleep(1000);

		}
		ApplicationLauncher.logger.info("waitForFinalMeterReading :No of Devices:"+ no_of_devices_connected);
		ApplicationLauncher.logger.info("waitForFinalMeterReading :Final Readings:"+ DeviceDataManagerController.getFinalMeterValues().length());
		return ReadingReceived;
	}

	public void getFinalMeterValuesFromUI(){
		ApplicationLauncher.logger.info("getFinalMeterValuesFromUI :Entry");
		TextBoxDialog TextBoxDialogobj = new TextBoxDialog();
		TextBoxDialogobj.TriggerFinalValueTDPlatFormLater();
	}

	public void getInitMeterValuesFromUI(){
		ApplicationLauncher.logger.info("getInitMeterValuesFromUI :Entry");
		TextBoxDialog TextBoxDialogobj = new TextBoxDialog();
		TextBoxDialogobj.TriggerInitValueTDPlatFormLater();

	}

	public boolean waitForInitMeterReading(){
		ApplicationLauncher.logger.info("waitForInitMeterReading :Entry");
		boolean ReadingReceived = false;

		int no_of_devices_connected = DisplayDataObj.get_no_of_devices_connected();
		while((no_of_devices_connected != DeviceDataManagerController.getInitMeterValues().length()) && (!getUserAbortedFlag())){
			ApplicationLauncher.logger.info("Waiting for initial meter input");
			Sleep(1000);

		}
		return ReadingReceived;
	}

	public void runZeroCurrent(){
		ApplicationLauncher.logger.info("runZeroCurrent  Started"  );
		DeviceDataManagerController.get_Error_min();
		DeviceDataManagerController.get_Error_max();
		DeviceDataManagerController.getNoOfPulses();
		ApplicationLauncher.logger.info("getPwrSrcR_PhaseVolt: " + DeviceDataManagerController.getPwrSrcR_PhaseVoltInFloat());

		float rated_volt = DeviceDataManagerController.getPwrSrcR_PhaseVoltInFloat();
		float r_percent = DeviceDataManagerController.getPwrSrc_Percent_VoltU1();
		float y_percent = DeviceDataManagerController.getPwrSrc_Percent_VoltU2();
		float b_percent = DeviceDataManagerController.getPwrSrc_Percent_VoltU3();
		rated_volt = Float.parseFloat(DisplayDataObj.manipulateRatedVoltageFor3PhaseDeltaFromL_L_TO_L_N(String.valueOf(rated_volt)));
		float final_volt1_value = DisplayDataObj.CalculateVoltage(rated_volt, r_percent);
		float final_volt2_value = DisplayDataObj.CalculateVoltage(rated_volt, y_percent);
		float final_volt3_value = DisplayDataObj.CalculateVoltage(rated_volt, b_percent);

		ArrayList<String> phasedegree = DeviceDataManagerController.getPhaseDegreeOutput();
		DisplayDataObj.setAllPhaseParameters(final_volt1_value, final_volt2_value, final_volt3_value, 
				0F, phasedegree);

		DeviceDataManagerController.setR_PhaseOutputVoltage(final_volt1_value);
		DeviceDataManagerController.setR_PhaseOutputCurrent(0F);
		DeviceDataManagerController.setY_PhaseOutputVoltage(final_volt2_value);
		DeviceDataManagerController.setY_PhaseOutputCurrent(0F);
		DeviceDataManagerController.setB_PhaseOutputVoltage(final_volt3_value);
		DeviceDataManagerController.setB_PhaseOutputCurrent(0F);
		if(ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED) {
			DeviceDataManagerController.manipulateTargetRmsValue();
		}
		DeviceDataManagerController.setPowerSrcOnTimerValue(300);

		DisplayDataObj.setVoltageResetRequired(false);
		DisplayPwrSrc_TurnOn();
		ApplicationLauncher.logger.info("Power On");

	}


	public void runZeroCurrentManualSource(){
		ApplicationLauncher.logger.info("runZeroCurrentManualSource  Started"  );
		DeviceDataManagerController.get_Error_min();
		DeviceDataManagerController.get_Error_max();
		DeviceDataManagerController.getNoOfPulses();
		ApplicationLauncher.logger.info("runZeroCurrentManualSource: getPwrSrcR_PhaseVolt: " + DeviceDataManagerController.getPwrSrcR_PhaseVoltInFloat());

		float rated_volt = DeviceDataManagerController.getPwrSrcR_PhaseVoltInFloat();
		float r_percent = DeviceDataManagerController.getPwrSrc_Percent_VoltU1();
		float y_percent = DeviceDataManagerController.getPwrSrc_Percent_VoltU2();
		float b_percent = DeviceDataManagerController.getPwrSrc_Percent_VoltU3();
		rated_volt = Float.parseFloat(DisplayDataObj.manipulateRatedVoltageFor3PhaseDeltaFromL_L_TO_L_N(String.valueOf(rated_volt)));
		float final_volt1_value = DisplayDataObj.CalculateVoltage(rated_volt, r_percent);
		float final_volt2_value = DisplayDataObj.CalculateVoltage(rated_volt, y_percent);
		float final_volt3_value = DisplayDataObj.CalculateVoltage(rated_volt, b_percent);

		ArrayList<String> phasedegree = DeviceDataManagerController.getPhaseDegreeOutput();
		DisplayDataObj.setAllPhaseParameters(final_volt1_value, final_volt2_value, final_volt3_value, 
				0F, phasedegree);

		DeviceDataManagerController.setR_PhaseOutputVoltage(final_volt1_value);
		DeviceDataManagerController.setR_PhaseOutputCurrent(0F);
		DeviceDataManagerController.setY_PhaseOutputVoltage(final_volt2_value);
		DeviceDataManagerController.setY_PhaseOutputCurrent(0F);
		DeviceDataManagerController.setB_PhaseOutputVoltage(final_volt3_value);
		DeviceDataManagerController.setB_PhaseOutputCurrent(0F);
		if(ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED) {
			DeviceDataManagerController.manipulateTargetRmsValue();
		}
		DeviceDataManagerController.setPowerSrcOnTimerValue(300);

		DisplayDataObj.setVoltageResetRequired(false);
		//DisplayPwrSrc_TurnOn();
		ApplicationLauncher.logger.info("runZeroCurrentManualSource: Power On");

	}

	public boolean refAccumulateStart(){
		ApplicationLauncher.logger.info("refAccumulateStart :Entry");
		if(DeviceDataManagerController.getAllPortInitSuccess()){
			SerialDM_Obj.setRefAccumlateStartStatus(false);
			if(!getUserAbortedFlag()) {
				SerialDM_Obj.Ref_AccumlateStartTrigger();
			}
			Integer AccStartStatusWaitTimeCounter = 20;
			while (AccStartStatusWaitTimeCounter !=0 && !SerialDM_Obj.getRefAccumlateStartStatus() && (!getUserAbortedFlag()) ){

				AccStartStatusWaitTimeCounter--;
				Sleep(250);
			}
		} else {
			ApplicationLauncher.logger.info("Error Code REFSTD_01: refAccumulateStart: Unable to access ref Serial Port");

		}
		return SerialDM_Obj.getLDU_ResetErrorStatus();
	}

	public boolean resetRefMeterAccumulateSetting(){
		ApplicationLauncher.logger.info("resetRefMeterAccumulateSetting :Entry");
		if(DeviceDataManagerController.getAllPortInitSuccess()){
			SerialDM_Obj.setRefAccumulateSettingStatus(false);
			if(!getUserAbortedFlag()) {
				SerialDM_Obj.Ref_AccumulateSettingTrigger();
			}
			Integer ResetSettingStatusWaitTimeCounter = 20;
			while (ResetSettingStatusWaitTimeCounter !=0 && !SerialDM_Obj.getRefAccumulateSettingStatus() && (!getUserAbortedFlag())){

				ResetSettingStatusWaitTimeCounter--;
				Sleep(250);
			}
		} else {

			ApplicationLauncher.logger.info("Error Code REFSTD_02: resetRefMeterAccumulateSetting: Unable to access ref Serial Port");
		}
		ApplicationLauncher.logger.info("resetRefMeterAccumulateSetting :Exit");
		return SerialDM_Obj.getLDU_ResetErrorStatus();
	}


	public boolean readRefMeterAccumulationReading(){
		ApplicationLauncher.logger.info("readRefMeterReading :Entry");
		if(DeviceDataManagerController.getAllPortInitSuccess()){
			SerialDM_Obj.setReadRefReadingStatus(false);
			if(!getUserAbortedFlag()) {
				SerialDM_Obj.refStdAccuReadingTrigger();
			}
			Integer readRefReadingStatusWaitTimeCounter = 20;
			while (readRefReadingStatusWaitTimeCounter !=0 && !SerialDataManager.getReadRefReadingStatus() && (!getUserAbortedFlag()) ){

				readRefReadingStatusWaitTimeCounter--;
				Sleep(250);
			}
		} else {

			ApplicationLauncher.logger.info("Error Code REFSTD_03: readRefMeterReading: Unable to access ref Serial Port");
		}
		return SerialDM_Obj.getLDU_ResetErrorStatus();
	}

	public void runRatedCurrent(){

		ApplicationLauncher.logger.info("runRatedCurrent  Started"  );
		DeviceDataManagerController.get_Error_min();
		DeviceDataManagerController.get_Error_max();
		DeviceDataManagerController.getNoOfPulses();
		float SelectedCurrentValue = DisplayDataObj.CalculateInfCurrent();

		float rated_volt = DeviceDataManagerController.getPwrSrcR_PhaseVoltInFloat();
		float r_percent = DeviceDataManagerController.getPwrSrc_Percent_VoltU1();
		float y_percent = DeviceDataManagerController.getPwrSrc_Percent_VoltU2();
		float b_percent = DeviceDataManagerController.getPwrSrc_Percent_VoltU3();
		rated_volt = Float.parseFloat(DisplayDataObj.manipulateRatedVoltageFor3PhaseDeltaFromL_L_TO_L_N(String.valueOf(rated_volt)));
		float final_volt1_value = DisplayDataObj.CalculateVoltage(rated_volt, r_percent);
		float final_volt2_value = DisplayDataObj.CalculateVoltage(rated_volt, y_percent);
		float final_volt3_value = DisplayDataObj.CalculateVoltage(rated_volt, b_percent);

		ArrayList<String> phasedegree = DeviceDataManagerController.getPhaseDegreeOutput();
		DisplayDataObj.setAllPhaseParameters(final_volt1_value, final_volt2_value, final_volt3_value, 
				SelectedCurrentValue, phasedegree);

		DeviceDataManagerController.setR_PhaseOutputVoltage(final_volt1_value);
		DeviceDataManagerController.setR_PhaseOutputCurrent(SelectedCurrentValue);
		DeviceDataManagerController.setY_PhaseOutputVoltage(final_volt2_value);
		DeviceDataManagerController.setY_PhaseOutputCurrent(SelectedCurrentValue);
		DisplayDataObj.manipulateY_PhaseCurrentFor3PhaseDelta();
		DeviceDataManagerController.setB_PhaseOutputVoltage(final_volt3_value);
		DeviceDataManagerController.setB_PhaseOutputCurrent(SelectedCurrentValue);
		if(ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED) {
			DeviceDataManagerController.manipulateTargetRmsValue();
		}
		DeviceDataManagerController.setPowerSrcOnTimerValue(300);
		DisplayDataObj.setVoltageResetRequired(false);
		DisplayPwrSrc_TurnOn();
		ApplicationLauncher.logger.info("runRatedCurrent:Power On");


	}



	public void runRatedCurrentWithPercentage(float inputPercentage){

		ApplicationLauncher.logger.info("runRatedCurrentWithPercentage  Started"  );
		DeviceDataManagerController.get_Error_min();
		DeviceDataManagerController.get_Error_max();
		DeviceDataManagerController.getNoOfPulses();
		//float SelectedCurrentValue = DisplayDataObj.CalculateInfCurrent();
		float SelectedCurrentValue = DisplayDataObj.calculateInfCurrentWithPercentage(inputPercentage);
		ApplicationLauncher.logger.info("runRatedCurrentWithPercentage : " + SelectedCurrentValue);
		float rated_volt = DeviceDataManagerController.getPwrSrcR_PhaseVoltInFloat();
		float r_percent = DeviceDataManagerController.getPwrSrc_Percent_VoltU1();
		float y_percent = DeviceDataManagerController.getPwrSrc_Percent_VoltU2();
		float b_percent = DeviceDataManagerController.getPwrSrc_Percent_VoltU3();
		rated_volt = Float.parseFloat(DisplayDataObj.manipulateRatedVoltageFor3PhaseDeltaFromL_L_TO_L_N(String.valueOf(rated_volt)));
		float final_volt1_value = DisplayDataObj.CalculateVoltage(rated_volt, r_percent);
		float final_volt2_value = DisplayDataObj.CalculateVoltage(rated_volt, y_percent);
		float final_volt3_value = DisplayDataObj.CalculateVoltage(rated_volt, b_percent);

		ArrayList<String> phasedegree = DeviceDataManagerController.getPhaseDegreeOutput();
		DisplayDataObj.setAllPhaseParameters(final_volt1_value, final_volt2_value, final_volt3_value, 
				SelectedCurrentValue, phasedegree);

		DeviceDataManagerController.setR_PhaseOutputVoltage(final_volt1_value);
		DeviceDataManagerController.setR_PhaseOutputCurrent(SelectedCurrentValue);
		DeviceDataManagerController.setY_PhaseOutputVoltage(final_volt2_value);
		DeviceDataManagerController.setY_PhaseOutputCurrent(SelectedCurrentValue);
		DisplayDataObj.manipulateY_PhaseCurrentFor3PhaseDelta();
		DeviceDataManagerController.setB_PhaseOutputVoltage(final_volt3_value);
		DeviceDataManagerController.setB_PhaseOutputCurrent(SelectedCurrentValue);
		if(ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED) {
			DeviceDataManagerController.manipulateTargetRmsValue();
		}
		DeviceDataManagerController.setPowerSrcOnTimerValue(300);
		DisplayDataObj.setVoltageResetRequired(false);
		DisplayPwrSrc_TurnOn();
		ApplicationLauncher.logger.info("runRatedCurrentWithPercentage : Power On");


	}


	public void runRatedCurrentManualSource(){

		ApplicationLauncher.logger.info("runRatedCurrentManualSource  Started"  );
		DeviceDataManagerController.get_Error_min();
		DeviceDataManagerController.get_Error_max();
		DeviceDataManagerController.getNoOfPulses();
		//ApplicationLauncher.logger.info("runRatedCurrentManualSource  no_of_pulses: " + no_of_pulses  );
		//String rssPulseRate = DisplayDataObj.getRSSPulseRate();
		//ApplicationLauncher.logger.info("runRatedCurrentManualSource  rssPulseRate: " + rssPulseRate  );
		float SelectedCurrentValue = DisplayDataObj.CalculateInfCurrent();

		float rated_volt = DeviceDataManagerController.getPwrSrcR_PhaseVoltInFloat();
		float r_percent = DeviceDataManagerController.getPwrSrc_Percent_VoltU1();
		float y_percent = DeviceDataManagerController.getPwrSrc_Percent_VoltU2();
		float b_percent = DeviceDataManagerController.getPwrSrc_Percent_VoltU3();
		rated_volt = Float.parseFloat(DisplayDataObj.manipulateRatedVoltageFor3PhaseDeltaFromL_L_TO_L_N(String.valueOf(rated_volt)));
		float final_volt1_value = DisplayDataObj.CalculateVoltage(rated_volt, r_percent);
		float final_volt2_value = DisplayDataObj.CalculateVoltage(rated_volt, y_percent);
		float final_volt3_value = DisplayDataObj.CalculateVoltage(rated_volt, b_percent);

		ArrayList<String> phasedegree = DeviceDataManagerController.getPhaseDegreeOutput();
		DisplayDataObj.setAllPhaseParameters(final_volt1_value, final_volt2_value, final_volt3_value, 
				SelectedCurrentValue, phasedegree);

		DeviceDataManagerController.setR_PhaseOutputVoltage(final_volt1_value);
		DeviceDataManagerController.setR_PhaseOutputCurrent(SelectedCurrentValue);
		DeviceDataManagerController.setY_PhaseOutputVoltage(final_volt2_value);
		DeviceDataManagerController.setY_PhaseOutputCurrent(SelectedCurrentValue);
		DisplayDataObj.manipulateY_PhaseCurrentFor3PhaseDelta();
		DeviceDataManagerController.setB_PhaseOutputVoltage(final_volt3_value);
		DeviceDataManagerController.setB_PhaseOutputCurrent(SelectedCurrentValue);
		if(ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED) {
			DeviceDataManagerController.manipulateTargetRmsValue();
		}
		DeviceDataManagerController.setPowerSrcOnTimerValue(300);
		DisplayDataObj.setVoltageResetRequired(false);
		//DisplayPwrSrc_TurnOn();
		ApplicationLauncher.logger.info("runRatedCurrentManualSource :Power On");


	}

	@SuppressWarnings("static-access")
	public void runConstTest(){
		ApplicationLauncher.logger.info("runConstTest:  Entry");
		//float given_powervalue = Float.parseFloat(DisplayDataObj.getConstTestPower())*1000;
		float given_powervalue = Float.parseFloat(DeviceDataManagerController.getConstTestPower());
		float init_ref_reading = Float.parseFloat(DeviceDataManagerController.getRefInitPhaseAReading());
		if(ProcalFeatureEnable.KRE_REFSTD_CONNECTED){
			init_ref_reading = 0.0f;
		}

		float final_calc_value = init_ref_reading + given_powervalue;
		float current_reading = 0;
		float current_A_reading= 0;
		float current_B_reading= 0;
		float current_C_reading= 0;
		String metertype = DeviceDataManagerController.getDeployedEM_ModelType();
		ApplicationLauncher.logger.info("final_calc_value: " + final_calc_value);
		ApplicationLauncher.logger.info("given_powervalue: " + given_powervalue);
		ApplicationLauncher.logger.info("init_ref_reading: " + init_ref_reading);

		while( !CompareKWhValues(current_reading, final_calc_value) && (!getUserAbortedFlag()) &&
				(DisplayDataObj.getRefStdReadDataFlag())){
			readRefMeterAccumulationReading();
			if(metertype.contains(ConstantApp.METERTYPE_SINGLEPHASE)){
				current_reading = Float.parseFloat(DeviceDataManagerController.getCurrentPhaseAReading());
				ApplicationLauncher.logger.debug("runConstTest: 1 Phase: A Reading: " + current_A_reading);
			}
			else if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
				current_A_reading = Float.parseFloat(DeviceDataManagerController.getCurrentPhaseAReading());
				current_B_reading = Float.parseFloat(DeviceDataManagerController.getCurrentPhaseBReading());
				current_C_reading = Float.parseFloat(DeviceDataManagerController.getCurrentPhaseCReading());
				current_reading = (current_A_reading + current_B_reading + current_C_reading);
				ApplicationLauncher.logger.debug("A Reading: " + current_A_reading);
				ApplicationLauncher.logger.debug("B Reading: " + current_B_reading);
				ApplicationLauncher.logger.debug("C Reading: " + current_C_reading);
				ApplicationLauncher.logger.debug("Total Energy: " + current_reading);
			}
			else{
				ApplicationLauncher.logger.info("runConstTest: Else Case");
			}	
			ApplicationLauncher.logger.info("current_reading: " + current_reading);
			Sleep(250);
		}

		ApplicationLauncher.logger.info("runConstTest:  Exit");
	}

	/*	public void runConstTestV2(){
		ApplicationLauncher.logger.info("runConstTestV2:  Entry");
		//float given_powervalue = Float.parseFloat(DisplayDataObj.getConstTestPower())*1000;
		float given_powervalue = Float.parseFloat(DisplayDataObj.getConstTestPower());
		float init_ref_reading = Float.parseFloat(DisplayDataObj.getRefInitPhaseAReading());
		if(ProcalFeatureEnable.KRE_REFSTD_CONNECTED){
			init_ref_reading = 0.0f;
		}

		float final_calc_value = init_ref_reading + given_powervalue;
		float current_reading = 0;
		float current_A_reading= 0;
		float current_B_reading= 0;
		float current_C_reading= 0;
		String metertype = DisplayDataObj.getDeployedEM_ModelType();
		ApplicationLauncher.logger.info("final_calc_value: " + final_calc_value);
		ApplicationLauncher.logger.info("given_powervalue: " + given_powervalue);
		ApplicationLauncher.logger.info("init_ref_reading: " + init_ref_reading);
		float nearingTargetPercent = 90.0f;
		float nearingTargetCurrentReductionPercentage = 10.0f;
		float nearingTargetPercentEnergy = final_calc_value * nearingTargetPercent /100;
		ApplicationLauncher.logger.info("nearingTargetPercent: " + nearingTargetPercent);
		ApplicationLauncher.logger.info("nearingTargetPercentEnergy: " + nearingTargetPercentEnergy);
		boolean nearingTargetSetPowerSourceExecutedOnce = false;
		while( !CompareKWhValues(current_reading, final_calc_value) && (!getUserAbortedFlag()) &&
				(DisplayDataObj.getRefStdReadDataFlag())){
			readRefMeterAccumulationReading();
			if(metertype.contains(ConstantApp.METERTYPE_SINGLEPHASE)){
				current_reading = Float.parseFloat(DisplayDataObj.getCurrentPhaseAReading());
				ApplicationLauncher.logger.debug("runConstTestV2: 1 Phase: A Reading: " + current_A_reading);
			}
			else if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
				current_A_reading = Float.parseFloat(DisplayDataObj.getCurrentPhaseAReading());
				current_B_reading = Float.parseFloat(DisplayDataObj.getCurrentPhaseBReading());
				current_C_reading = Float.parseFloat(DisplayDataObj.getCurrentPhaseCReading());
				current_reading = (current_A_reading + current_B_reading + current_C_reading);
				ApplicationLauncher.logger.debug("A Reading: " + current_A_reading);
				ApplicationLauncher.logger.debug("B Reading: " + current_B_reading);
				ApplicationLauncher.logger.debug("C Reading: " + current_C_reading);
				ApplicationLauncher.logger.debug("Total Energy: " + current_reading);
			}
			else{
				ApplicationLauncher.logger.info("runConstTestV2: Else Case");
			}	
			if(current_reading >=nearingTargetPercentEnergy) {
				if(!nearingTargetSetPowerSourceExecutedOnce) {
					ApplicationLauncher.logger.info("runConstTestV2: runRatedCurrentWithPercentage : Hit");
					runRatedCurrentWithPercentage(nearingTargetCurrentReductionPercentage);
					nearingTargetSetPowerSourceExecutedOnce = true;
				}
			}
			ApplicationLauncher.logger.info("current_reading: " + current_reading);
			Sleep(250);
		}

		ApplicationLauncher.logger.info("runConstTestV2:  Exit");
	}
	 */



	@SuppressWarnings("static-access")
	public void runConstTestV2(){
		ApplicationLauncher.logger.info("runConstTestV2:  Entry");
		//float given_powervalue = Float.parseFloat(DisplayDataObj.getConstTestPower())*1000;
		float given_powervalue = Float.parseFloat(DeviceDataManagerController.getConstTestPower());
		float init_ref_reading = Float.parseFloat(DeviceDataManagerController.getRefInitPhaseAReading());
		if(ProcalFeatureEnable.KRE_REFSTD_CONNECTED){
			init_ref_reading = 0.0f;
		}

		float final_calc_value = init_ref_reading + given_powervalue;
		float current_reading = 0;
		float current_A_reading= 0;
		float current_B_reading= 0;
		float current_C_reading= 0;
		String metertype = DeviceDataManagerController.getDeployedEM_ModelType();
		ApplicationLauncher.logger.info("final_calc_value: " + final_calc_value);
		ApplicationLauncher.logger.info("given_powervalue: " + given_powervalue);
		ApplicationLauncher.logger.info("init_ref_reading: " + init_ref_reading);
		float nearingTarget1Percent = ConstantAppConfig.DIAL_TEST_SINGLE_PHASE_NEARING_TARGET1_PERCENT;//90.0f;
		float nearingTarget1CurrentReductionPercentage = ConstantAppConfig.DIAL_TEST_SINGLE_PHASE_NEARING_TARGET1_CURRENT_REDUCTION_PERCENT;//10.0f;
		float nearingTarget1PercentEnergy = (final_calc_value * nearingTarget1Percent) /100.0f;
		boolean nearingTarget1SetPowerSourceExecutedOnce = false;
		ApplicationLauncher.logger.info("nearingTarget1Percent: " + nearingTarget1Percent);
		ApplicationLauncher.logger.info("nearingTarget1PercentEnergy: " + nearingTarget1PercentEnergy);


		float nearingTarget2Percent = ConstantAppConfig.DIAL_TEST_SINGLE_PHASE_NEARING_TARGET2_PERCENT;//98.0f;
		float nearingTarget2CurrentReductionPercentage = ConstantAppConfig.DIAL_TEST_SINGLE_PHASE_NEARING_TARGET2_CURRENT_REDUCTION_PERCENT;//5.0f;
		float nearingTarget2PercentEnergy = (final_calc_value * nearingTarget2Percent) /100.0f;
		boolean nearingTarget2SetPowerSourceExecutedOnce = false;
		ApplicationLauncher.logger.info("nearingTarget2Percent: " + nearingTarget2Percent);
		ApplicationLauncher.logger.info("nearingTarget2PercentEnergy: " + nearingTarget2PercentEnergy);

		while( !CompareKWhValues(current_reading, final_calc_value) && (!getUserAbortedFlag()) &&
				(DisplayDataObj.getRefStdReadDataFlag())){
			readRefMeterAccumulationReading();
			if(metertype.contains(ConstantApp.METERTYPE_SINGLEPHASE)){
				current_reading = Float.parseFloat(DeviceDataManagerController.getCurrentPhaseAReading());
				ApplicationLauncher.logger.debug("runConstTestV2: 1 Phase: A Reading: " + current_A_reading);
			}
			else if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
				current_A_reading = Float.parseFloat(DeviceDataManagerController.getCurrentPhaseAReading());
				current_B_reading = Float.parseFloat(DeviceDataManagerController.getCurrentPhaseBReading());
				current_C_reading = Float.parseFloat(DeviceDataManagerController.getCurrentPhaseCReading());
				current_reading = (current_A_reading + current_B_reading + current_C_reading);
				ApplicationLauncher.logger.debug("A Reading: " + current_A_reading);
				ApplicationLauncher.logger.debug("B Reading: " + current_B_reading);
				ApplicationLauncher.logger.debug("C Reading: " + current_C_reading);
				ApplicationLauncher.logger.debug("Total Energy: " + current_reading);
			}
			else{
				ApplicationLauncher.logger.info("runConstTestV2: Else Case");
			}	
			if(current_reading >=nearingTarget1PercentEnergy) {
				if(!nearingTarget1SetPowerSourceExecutedOnce) {
					ApplicationLauncher.logger.info("runConstTestV2: runRatedCurrentWithPercentage : Hit");
					runRatedCurrentWithPercentage(nearingTarget1CurrentReductionPercentage);
					nearingTarget1SetPowerSourceExecutedOnce = true;
				}
			}
			if(current_reading >=nearingTarget2PercentEnergy) {
				if(!nearingTarget2SetPowerSourceExecutedOnce) {
					ApplicationLauncher.logger.info("runConstTestV2: runRatedCurrentWithPercentage : Hit2");
					runRatedCurrentWithPercentage(nearingTarget2CurrentReductionPercentage);
					nearingTarget2SetPowerSourceExecutedOnce = true;
				}
			}
			ApplicationLauncher.logger.info("current_reading: " + current_reading);
			Sleep(250);
		}

		ApplicationLauncher.logger.info("runConstTestV2:  Exit");
	}

	@SuppressWarnings("static-access")
	public void runConstTestV3(){
		ApplicationLauncher.logger.info("runConstTestV3:  Entry");
		//float given_powervalue = Float.parseFloat(DisplayDataObj.getConstTestPower())*1000;
		float given_powervalue = Float.parseFloat(DeviceDataManagerController.getConstTestPower());
		float init_ref_reading = Float.parseFloat(DeviceDataManagerController.getRefInitPhaseAReading());
		if(ProcalFeatureEnable.KRE_REFSTD_CONNECTED){
			init_ref_reading = 0.0f;
		}

		float final_calc_value = init_ref_reading + given_powervalue;
		float current_reading = 0;
		float current_A_reading= 0;
		float current_B_reading= 0;
		float current_C_reading= 0;
		String metertype = DeviceDataManagerController.getDeployedEM_ModelType();
		ApplicationLauncher.logger.info("final_calc_value: " + final_calc_value);
		ApplicationLauncher.logger.info("given_powervalue: " + given_powervalue);
		ApplicationLauncher.logger.info("init_ref_reading: " + init_ref_reading);
		float nearingTarget1Percent = 90.0f;
		nearingTarget1Percent = ConstantAppConfig.DIAL_TEST_THREE_PHASE_NEARING_TARGET1_PERCENT;//50.0f;//35.0f;//50.0f;
		float nearingTarget1CurrentReductionPercentage = 10.0f;
		nearingTarget1CurrentReductionPercentage = ConstantAppConfig.DIAL_TEST_THREE_PHASE_NEARING_TARGET1_CURRENT_REDUCTION_PERCENT;//10.0f;//5.0f;
		float nearingTarget1PercentEnergy = (final_calc_value * nearingTarget1Percent) /100.0f;
		boolean nearingTarget1SetPowerSourceExecutedOnce = false;
		ApplicationLauncher.logger.info("nearingTarget1Percent: " + nearingTarget1Percent);
		ApplicationLauncher.logger.info("nearingTarget1PercentEnergy: " + nearingTarget1PercentEnergy);


		float nearingTarget2Percent = 98.0f;
		nearingTarget2Percent = ConstantAppConfig.DIAL_TEST_THREE_PHASE_NEARING_TARGET2_PERCENT;;//90.0f;//75.0f;//50.0f;//70.0f;//90.0f;
		float nearingTarget2CurrentReductionPercentage = 5.0f;
		nearingTarget2CurrentReductionPercentage = ConstantAppConfig.DIAL_TEST_THREE_PHASE_NEARING_TARGET2_CURRENT_REDUCTION_PERCENT;;//5.0f;//2.0f;
		float nearingTarget2PercentEnergy = (final_calc_value * nearingTarget2Percent) / 100.0f;
		boolean nearingTarget2SetPowerSourceExecutedOnce = false;
		ApplicationLauncher.logger.info("nearingTarget2Percent: " + nearingTarget2Percent);
		ApplicationLauncher.logger.info("nearingTarget2PercentEnergy: " + nearingTarget2PercentEnergy);

		while( !CompareKWhValues(current_reading, final_calc_value) && (!getUserAbortedFlag()) &&
				(DisplayDataObj.getRefStdReadDataFlag())){
			readRefMeterAccumulationReading();
			if(metertype.contains(ConstantApp.METERTYPE_SINGLEPHASE)){
				current_reading = Float.parseFloat(DeviceDataManagerController.getCurrentPhaseAReading());
				ApplicationLauncher.logger.debug("runConstTestV3: 1 Phase: A Reading: " + current_A_reading);
			}
			else if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
				current_A_reading = Float.parseFloat(DeviceDataManagerController.getCurrentPhaseAReading());
				current_B_reading = Float.parseFloat(DeviceDataManagerController.getCurrentPhaseBReading());
				current_C_reading = Float.parseFloat(DeviceDataManagerController.getCurrentPhaseCReading());
				current_reading = (current_A_reading + current_B_reading + current_C_reading);
				ApplicationLauncher.logger.debug("A Reading: " + current_A_reading);
				ApplicationLauncher.logger.debug("B Reading: " + current_B_reading);
				ApplicationLauncher.logger.debug("C Reading: " + current_C_reading);
				ApplicationLauncher.logger.debug("Total Energy: " + current_reading);
			}
			else{
				ApplicationLauncher.logger.info("runConstTestV3: Else Case");
			}	
			if(current_reading >=nearingTarget1PercentEnergy) {
				if(!nearingTarget1SetPowerSourceExecutedOnce) {
					ApplicationLauncher.logger.info("runConstTestV3: runRatedCurrentWithPercentage : Hit");
					runRatedCurrentWithPercentage(nearingTarget1CurrentReductionPercentage);
					nearingTarget1SetPowerSourceExecutedOnce = true;
				}
			}
			if(current_reading >=nearingTarget2PercentEnergy) {
				if(!nearingTarget2SetPowerSourceExecutedOnce) {
					ApplicationLauncher.logger.info("runConstTestV3: runRatedCurrentWithPercentage : Hit2");
					runRatedCurrentWithPercentage(nearingTarget2CurrentReductionPercentage);
					nearingTarget2SetPowerSourceExecutedOnce = true;
				}
			}
			ApplicationLauncher.logger.info("current_reading: " + current_reading);
			Sleep(250);
		}

		ApplicationLauncher.logger.info("runConstTestV3:  Exit");
	}




	public void runConstTestBofa(){
		ApplicationLauncher.logger.info("runConstTestBofa:  Entry");
		//float given_powervalue = Float.parseFloat(DisplayDataObj.getConstTestPower())*1000;
		float given_powervalue = Float.parseFloat(DeviceDataManagerController.getConstTestPower());
		float init_ref_reading = 0.0f;//Float.parseFloat(DisplayDataObj.getRefInitPhaseAReading());
		//if(ProcalFeatureEnable.KRE_REFSTD_CONNECTED){
		//	init_ref_reading = 0.0f;
		//}

		float final_calc_value = init_ref_reading + given_powervalue;
		float current_reading = 0;
		float current_A_reading= 0;
		float current_B_reading= 0;
		float current_C_reading= 0;
		String metertype = DeviceDataManagerController.getDeployedEM_ModelType();
		ApplicationLauncher.logger.info("runConstTestBofa: final_calc_value: " + final_calc_value);
		ApplicationLauncher.logger.info("runConstTestBofa: given_powervalue: " + given_powervalue);
		ApplicationLauncher.logger.info("runConstTestBofa: init_ref_reading: " + init_ref_reading);
		float nearingTarget1Percent = 90.0f;
		float nearingTarget1PercentEnergy = final_calc_value * nearingTarget1Percent /100;
		ApplicationLauncher.logger.info("runConstTestBofa : nearingTarget1Percent: " + nearingTarget1Percent);
		ApplicationLauncher.logger.info("runConstTestBofa : nearingTarget1PercentEnergy: " + nearingTarget1PercentEnergy);


		float nearingTarget2Percent = 98.0f;
		float nearingTarget2PercentEnergy = final_calc_value * nearingTarget2Percent /100;
		ApplicationLauncher.logger.info("runConstTestBofa : nearingTarget2Percent: " + nearingTarget2Percent);
		ApplicationLauncher.logger.info("runConstTestBofa : nearingTarget2PercentEnergy: " + nearingTarget2PercentEnergy);

		while( !CompareKWhValues(current_reading, final_calc_value) && (!getUserAbortedFlag()) ){// &&
			//(DisplayDataObj.getRefStdReadDataFlag())){
			readRefMeterAccumulationReading();
			if(metertype.contains(ConstantApp.METERTYPE_SINGLEPHASE)){
				current_reading = Float.parseFloat(DeviceDataManagerController.getCurrentPhaseAReading());
				ApplicationLauncher.logger.debug("runConstTestBofa: 1 Phase: A Reading: " + current_reading);
			}
			else if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
				current_A_reading = Float.parseFloat(DeviceDataManagerController.getCurrentPhaseAReading());
				current_B_reading = Float.parseFloat(DeviceDataManagerController.getCurrentPhaseBReading());
				current_C_reading = Float.parseFloat(DeviceDataManagerController.getCurrentPhaseCReading());
				current_reading = (current_A_reading + current_B_reading + current_C_reading);
				ApplicationLauncher.logger.debug("runConstTestBofa : A Reading: " + current_A_reading);
				ApplicationLauncher.logger.debug("runConstTestBofa : B Reading: " + current_B_reading);
				ApplicationLauncher.logger.debug("runConstTestBofa : C Reading: " + current_C_reading);
				ApplicationLauncher.logger.debug("runConstTestBofa : Total Energy: " + current_reading);
			}
			else{
				ApplicationLauncher.logger.info("runConstTestBofa: Else Case");
			}	
			/*			if(current_reading >=nearingTarget1PercentEnergy) {
				if(!nearingTarget1SetPowerSourceExecutedOnce) {
					ApplicationLauncher.logger.info("runConstTestBofa: runRatedCurrentWithPercentage : Hit");
					runRatedCurrentWithPercentage(nearingTarget1CurrentReductionPercentage);
					nearingTarget1SetPowerSourceExecutedOnce = true;
				}
			}
			if(current_reading >=nearingTarget2PercentEnergy) {
				if(!nearingTarget2SetPowerSourceExecutedOnce) {
					ApplicationLauncher.logger.info("runConstTestBofa: runRatedCurrentWithPercentage : Hit2");
					runRatedCurrentWithPercentage(nearingTarget2CurrentReductionPercentage);
					nearingTarget2SetPowerSourceExecutedOnce = true;
				}
			}*/
			ApplicationLauncher.logger.info("runConstTestBofa: current_reading: " + current_reading);
			Sleep(250);
		}

	}



	@SuppressWarnings("static-access")
	public void runConstTestManualSource(){
		ApplicationLauncher.logger.info("runConstTestManualSource:  Entry");
		//float given_powervalue = Float.parseFloat(DisplayDataObj.getConstTestPower())*1000;
		float given_powervalue = Float.parseFloat(DeviceDataManagerController.getConstTestPower());
		float init_ref_reading = Float.parseFloat(DeviceDataManagerController.getRefInitPhaseAReading());
		if(ProcalFeatureEnable.KIGGS_REFSTD_CONNECTED){
			init_ref_reading = 0.0f;
		}

		float final_calc_value = init_ref_reading + given_powervalue;
		float current_reading = 0;
		float current_A_reading= 0;
		float current_B_reading= 0;
		float current_C_reading= 0;
		String metertype = DeviceDataManagerController.getDeployedEM_ModelType();
		ApplicationLauncher.logger.info("runConstTestManualSource: final_calc_value: " + final_calc_value);
		ApplicationLauncher.logger.info("runConstTestManualSource: given_powervalue: " + given_powervalue);
		ApplicationLauncher.logger.info("runConstTestManualSource: init_ref_reading: " + init_ref_reading);

		while( !CompareKWhValues(current_reading, final_calc_value) && (!getUserAbortedFlag()) &&
				(DisplayDataObj.getRefStdReadDataFlag())){
			//readRefMeterAccumulationReading();
			if(metertype.contains(ConstantApp.METERTYPE_SINGLEPHASE)){
				current_reading = Float.parseFloat(DeviceDataManagerController.getCurrentPhaseAReading());
				ApplicationLauncher.logger.debug("runConstTestManualSource: 1 Phase: A Reading: " + current_A_reading);
			}
			else if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
				current_A_reading = Float.parseFloat(DeviceDataManagerController.getCurrentPhaseAReading());
				current_B_reading = Float.parseFloat(DeviceDataManagerController.getCurrentPhaseBReading());
				current_C_reading = Float.parseFloat(DeviceDataManagerController.getCurrentPhaseCReading());
				current_reading = (current_A_reading + current_B_reading + current_C_reading);
				ApplicationLauncher.logger.debug("A Reading: " + current_A_reading);
				ApplicationLauncher.logger.debug("B Reading: " + current_B_reading);
				ApplicationLauncher.logger.debug("C Reading: " + current_C_reading);
				ApplicationLauncher.logger.debug("Total Energy: " + current_reading);
			}
			else{
				ApplicationLauncher.logger.info("runConstTestManualSource: Else Case");
			}	
			ApplicationLauncher.logger.info("runConstTestManualSource: current_reading: " + current_reading);
			Sleep(250);
		}

		ApplicationLauncher.logger.info("runConstTest:  Exit");
	}


	public boolean CompareKWhValues(float current_reading, float reading_limit){

		ApplicationLauncher.logger.debug("CompareKWhValues : Math.abs : current_reading:"+ Math.abs(current_reading));
		ApplicationLauncher.logger.debug("CompareKWhValues :reading_limit:"+ reading_limit);
		/*		if(DisplayDataObj.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)){
			if(current_reading<0){
				current_reading = -(current_reading);
				ApplicationLauncher.logger.debug("CompareKWhValues :Hit1:");
			}
		}*/
		if((Math.abs(current_reading) > reading_limit)){
			ApplicationLauncher.logger.debug("CompareKWhValues :Hit2:");
			return true;
		}
		else{
			ApplicationLauncher.logger.debug("CompareKWhValues :Hit3:");
			return false;
		}
	}


	public boolean makeAllCurrentZero(){
		ApplicationLauncher.logger.info("makeAllCurrentZero :Entry");
		boolean status = false;
		if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
			status = SerialDM_Obj.lscsPowerSrcMakeAllCurrentZero();
		}
		return status;
	}

	public boolean refAccumulateStop(){
		ApplicationLauncher.logger.info("refAccumulateStop :Entry");
		if(DeviceDataManagerController.getAllPortInitSuccess()){
			SerialDM_Obj.setRefAccumlateStopStatus(false);
			if(!getUserAbortedFlag()) {
				SerialDM_Obj.Ref_AccumlateStopTrigger();
			}
			Integer AccStartStatusWaitTimeCounter = 20;
			while (AccStartStatusWaitTimeCounter !=0 && !SerialDM_Obj.getRefAccumlateStopStatus() && (!getUserAbortedFlag())){

				AccStartStatusWaitTimeCounter--;
				Sleep(250);
			}
		} else {
			ApplicationLauncher.logger.info("Error Code REFSTD_04: refAccumulateStop: Unable to access ref Serial Port");

		}
		return SerialDM_Obj.getLDU_ResetErrorStatus();
	}

	public void InformUserToCutNuetral(){
		TextBoxDialog TextBoxDialogobj = new TextBoxDialog();
		TextBoxDialogobj.TriggerCutInfoPlatFormLater();
	}
	@SuppressWarnings("static-access")
	public void WaitForUserInput(){
		DeviceDataManagerController.setcutnuetral_flag(false);
		DeviceDataManagerController.setcutnuetral_wait_flag(true);
		while((DisplayDataObj.getcutnuetral_wait_flag()) && (!getUserAbortedFlag())){
			ApplicationLauncher.logger.info("Waiting for user input");
			Sleep(1000);

		}

	}

	public void setTwoLevelValidationOnFlag(boolean value){
		TwoLevelValidationOnFlag = value;
	}

	public boolean getTwoLevelValidationOnFlag(){
		return TwoLevelValidationOnFlag;
	}


	@SuppressWarnings("static-access")
	public boolean FirstLevelValidation(){
		ApplicationLauncher.logger.debug("FirstLevelValidation: Entry");
		ApplicationHomeController.updateBottomSecondaryStatus("Power source stability time - awaiting",ConstantApp.LEFT_STATUS_INFO);
		if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
			ApplicationLauncher.logger.debug("FirstLevelValidation: Bofa Source connected");
			return true;
		}
		reset_PowerOnWaitCounter();
		if(getTwoLevelValidationOnFlag()){
			ApplicationLauncher.logger.debug(getCurrentTestPointName()+":"+ getCurrentTestPoint_Index()+" :FirstLevelValidation");
			while((!SerialDataManager.getPowerSrcOnFlag()) && (!SerialDataManager.getPowerSrcErrorResponseReceivedStatus()) 
					&& (PowerOnWaitCounter > 0) && !getUserAbortedFlag() ){


				ApplicationLauncher.logger.info(getCurrentTestPointName()+" :FirstLevelValidation: Waiting for Power Source on:" + SerialDataManager.getPowerSrcOnFlag());

				Sleep(1000);
				PowerOnWaitCounter--;

			}
			if(getUserAbortedFlag()){
				ApplicationLauncher.logger.info(getCurrentTestPointName()+":"+ getCurrentTestPoint_Index()+" :FirstLevelValidation: User aborted, exiting validation" );
				return false;
			}
			if((!SerialDataManager.getPowerSrcOnFlag()) || PowerOnWaitCounter <=0 || (SerialDM_Obj.getPowerSrcErrorResponseReceivedStatus())){
				ApplicationLauncher.logger.info("FirstLevelValidation: *************************");
				ApplicationLauncher.logger.info(getCurrentTestPointName()+":"+ getCurrentTestPoint_Index()+" :FirstLevelValidation: Skipping Test Point:");
				SerialDataManager.setSkipCurrentTP_Execution(true);
				if(!SerialDataManager.getPowerSrcOnFlag()){
					ApplicationLauncher.logger.info(getCurrentTestPointName()+":"+ getCurrentTestPoint_Index()+" :FirstLevelValidation: Skipping Reason: PowerSrcOnFlag:"+SerialDataManager.getPowerSrcOnFlag());
				}
				if(PowerOnWaitCounter <=0){
					ApplicationLauncher.logger.info(getCurrentTestPointName()+":"+ getCurrentTestPoint_Index()+" :FirstLevelValidation: Skipping Reason: PowerOnWaitCounter:"+PowerOnWaitCounter);
					FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_105);
				}
				if(SerialDataManager.getPowerSrcErrorResponseReceivedStatus()){
					ApplicationLauncher.logger.info(getCurrentTestPointName()+":"+ getCurrentTestPoint_Index()+" FirstLevelValidation: Skipping Reason: PowerSrcErrorResponseReceived:"+SerialDataManager.getPowerSrcErrorResponseReceivedStatus());
				}
				ApplicationLauncher.logger.info("FirstLevelValidation: *************************");
				updateSkippedInDisplay();

			}
			if(!SerialDataManager.getPowerSrcOnFlag()){
				checkPowerSrcOnContinuousFailureCounter();
				ApplicationLauncher.logger.info("*******************************************");
				ApplicationLauncher.logger.info("*******************************************");
				ApplicationLauncher.logger.info("*******************************************");
				ApplicationLauncher.logger.info(getCurrentTestPointName()+":"+ getCurrentTestPoint_Index()+" :StabilityValidation:PowerSource:FirstLevelValidation: Failed");
				ApplicationLauncher.logger.info("Reason5: "+FailureManager.getPowerSrcReasonForFailure());
				ApplicationLauncher.logger.info("*******************************************");
				ApplicationLauncher.logger.info("*******************************************");
				ApplicationLauncher.logger.info("*******************************************");
			}
			else{
				FailureManager.ResetPowerSrcReasonForFailure();
				ApplicationLauncher.logger.info("**************************");
				ApplicationLauncher.logger.info(getCurrentTestPointName()+":"+ getCurrentTestPoint_Index()+" :FirstLevelValidation: Success");
				ApplicationLauncher.logger.info("**************************");
				ApplicationLauncher.logger.debug("FirstLevelValidation:resetPowerSrcContinuousFailureCounter");
				resetPowerSrcContinuousFailureCounter();
			}

			return SerialDataManager.getPowerSrcOnFlag();
		}else{
			/*userRespondedForPromptDisplay = false;
			sourceParameterSetPromptDisplayTrigger();
			ApplicationLauncher.logger.info("sourceParameterSetPromptDisplayTrigger: Awaiting for user prompt");
			while ( (!userRespondedForPromptDisplay) && (!getUserAbortedFlag())  ) {
				ApplicationLauncher.logger.info("sourceParameterSetPromptDisplayTrigger: Awaiting for user prompt");
				Sleep(1000);
			}
			ApplicationLauncher.logger.info("sourceParameterSetPromptDisplayTrigger: user responded or aborted");*/
			manualSourceSetPromptDisplay();
			return true;
		}

	}

	public void manualSourceSetPromptDisplay(){
		if(!getUserAbortedFlag()){
			userRespondedForPromptDisplay = false;
			sourceParameterSetPromptDisplayTrigger();
			ApplicationLauncher.logger.info("sourceParameterSetPromptDisplayTrigger: Awaiting for user prompt triggered");
			while ( (!userRespondedForPromptDisplay) && (!getUserAbortedFlag())  ) {
				ApplicationLauncher.logger.info("sourceParameterSetPromptDisplayTrigger: Awaiting for user response");
				Sleep(1000);
			}
			ApplicationLauncher.logger.info("sourceParameterSetPromptDisplayTrigger: user responded or aborted");
		}

	}

	public void manualSourceSetCurrentPromptDisplay(){
		if(!getUserAbortedFlag()){
			userRespondedForPromptDisplay = false;
			sourceParameterSetCurrentPromptDisplayTrigger();
			ApplicationLauncher.logger.info("manualSourceSetCurrentPromptDisplay: Awaiting for user prompt");
			while ( (!userRespondedForPromptDisplay) && (!getUserAbortedFlag())  ) {
				ApplicationLauncher.logger.info("manualSourceSetCurrentPromptDisplay: Awaiting for user prompt");
				Sleep(1000);
			}
			ApplicationLauncher.logger.info("manualSourceSetCurrentPromptDisplay: user responded or aborted");
		}

	}

	public void sourceParameterSetPromptDisplayTrigger() {
		ApplicationLauncher.logger.info("sourceParameterSetPromptDisplayTrigger: Entry");
		sourcePromptTimer = new Timer();
		sourcePromptTimer.schedule(new sourceParameterSetPromptDisplayTask(), 50);
	}



	public void sourceParameterSetCurrentPromptDisplayTrigger() {
		ApplicationLauncher.logger.info("sourceParameterSetCurrentPromptDisplayTrigger: Entry");
		sourcePromptTimer = new Timer();
		sourcePromptTimer.schedule(new sourceParameterSetCurrentPromptDisplayTask(), 50);
	}


	class sourceParameterSetPromptDisplayTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				sourceParameterSetPromptDisplay();
				userRespondedForPromptDisplay = true;
			});
			sourcePromptTimer.cancel();


		}
	}

	class sourceParameterSetCurrentPromptDisplayTask extends TimerTask {
		public void run() {
			Platform.runLater(() -> {
				sourceParameterSetCurrentPromptDisplay();
				userRespondedForPromptDisplay = true;
			});
			sourcePromptTimer.cancel();


		}
	}

	public void kiggsRefStdCurrentTapSetting(){
		ApplicationLauncher.logger.debug("kiggsRefStdCurrentTapSetting: Entry");
		float selectedUserInputCurrent = Float.parseFloat(DeviceDataManagerController.getR_PhaseOutputCurrent());
		ApplicationLauncher.logger.debug("kiggsRefStdCurrentTapSetting: selectedUserInputCurrent: " + selectedUserInputCurrent);
		ApplicationLauncher.logger.debug("kiggsRefStdCurrentTapSetting: DisplayDataObj.getR_PhaseOutputCurrent(): " + DeviceDataManagerController.getR_PhaseOutputCurrent());
		SerialDM_Obj.kiggsRefStdSetManualModeCurrentTapRange(selectedUserInputCurrent);
		Sleep(1000);
		SerialDM_Obj.kiggsReadRefStdVoltAndCurrentTapRange();
		ApplicationLauncher.logger.debug("kiggsRefStdCurrentTapSetting: DisplayDataObj.getRefStdSelectedVoltageTap: " + DeviceDataManagerController.getRefStdSelectedVoltageTap());
		ApplicationLauncher.logger.debug("kiggsRefStdCurrentTapSetting: DisplayDataObj.getRefStdSelectedCurrentTap: " + DeviceDataManagerController.getRefStdSelectedCurrentTap());

	}


	public void sourceParameterSetPromptDisplay(){
		ApplicationLauncher.logger.debug("sourceParameterSetPromptDisplay: Entry");

		sourceParameterRphaseVoltSetPromptDisplay();
		sourceParameterRphaseCurrentSetPromptDisplay();
		if(!getUserAbortedFlag()){
			if(ProcalFeatureEnable.KIGGS_REFSTD_CONNECTED){
				kiggsRefStdCurrentTapSetting();
				DisplayDataObj.manipulateKiggsRefStRssConstant();
				/*if(ProcalFeatureEnable.KIGGS_REFSTD_AUTO_CALCULATION){

					String manipulatedRSS_PulseConstantInWattHour = SerialDataRefStdKiggs.manipulateRssPulseConstantInWattHour(getRefStdSelectedVoltageTap(),getRefStdSelectedCurrentTap());
					setRSSPulseRate(manipulatedRSS_PulseConstantInWattHour);
					ApplicationLauncher.logger.debug("sourceParameterSetPromptDisplay: manipulatedRSS_PulseConstantInWattHour: "+getRSSPulseRate());

				}*/

			}
			float rPhaseCurrent = Float.parseFloat(DeviceDataManagerController.getR_PhaseOutputCurrent());
			if(rPhaseCurrent!=0.0f){
				if(ProcalFeatureEnable.POWERSOURCE_MANUAL_MODE_CURRENT_SET_RECONFIRMATION_REQUIRED){
					sourceParameterRphaseCurrentSetReconfirmationPromptDisplay();// this is required, the refStd relay may be switching the TAP
				}
				sourceParameterRphasePhaseAngleSetPromptDisplay();
			}


			String metertype = getProjectEM_ModelType();
			ApplicationLauncher.logger.debug("sourceParameterSetPromptDisplay: metertype : " + metertype);
			if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){

				sourceParameterYphaseVoltSetPromptDisplay();
				sourceParameterYphaseCurrentSetPromptDisplay();
				sourceParameterYphasePhaseAngleSetPromptDisplay();

				sourceParameterBphaseVoltSetPromptDisplay();
				sourceParameterBphaseCurrentSetPromptDisplay();
				sourceParameterBphasePhaseAngleSetPromptDisplay();
			}
		}

		ApplicationLauncher.logger.debug("sourceParameterSetPromptDisplay: Exit");
	}



	public void sourceParameterSetCurrentPromptDisplay(){
		ApplicationLauncher.logger.debug("sourceParameterSetCurrentPromptDisplay: Entry");

		//sourceParameterRphaseVoltSetPromptDisplay();
		sourceParameterRphaseCurrentSetPromptDisplay();
		if(!getUserAbortedFlag()){
			if(ProcalFeatureEnable.KIGGS_REFSTD_CONNECTED){
				kiggsRefStdCurrentTapSetting();
				DisplayDataObj.manipulateKiggsRefStRssConstant();
				/*if(ProcalFeatureEnable.KIGGS_REFSTD_AUTO_CALCULATION){

					String manipulatedRSS_PulseConstantInWattHour = SerialDataRefStdKiggs.manipulateRssPulseConstantInWattHour(getRefStdSelectedVoltageTap(),getRefStdSelectedCurrentTap());
					setRSSPulseRate(manipulatedRSS_PulseConstantInWattHour);
					ApplicationLauncher.logger.debug("sourceParameterSetPromptDisplay: manipulatedRSS_PulseConstantInWattHour: "+getRSSPulseRate());

				}*/

			}

			float rPhaseCurrent = Float.parseFloat(DeviceDataManagerController.getR_PhaseOutputCurrent());
			if(rPhaseCurrent!=0.0f){
				if(ProcalFeatureEnable.POWERSOURCE_MANUAL_MODE_CURRENT_SET_RECONFIRMATION_REQUIRED){
					sourceParameterRphaseCurrentSetReconfirmationPromptDisplay();// this is required, the refStd relay may be switching the TAP
				}
				//sourceParameterRphasePhaseAngleSetPromptDisplay();
			}

			String metertype = getProjectEM_ModelType();
			ApplicationLauncher.logger.debug("sourceParameterSetCurrentPromptDisplay: metertype : " + metertype);
			if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){

				//sourceParameterYphaseVoltSetPromptDisplay();
				sourceParameterYphaseCurrentSetPromptDisplay();
				//sourceParameterYphasePhaseAngleSetPromptDisplay();

				//sourceParameterBphaseVoltSetPromptDisplay();
				sourceParameterBphaseCurrentSetPromptDisplay();
				//sourceParameterBphasePhaseAngleSetPromptDisplay();
			}

		}
		ApplicationLauncher.logger.debug("sourceParameterSetCurrentPromptDisplay: Exit");
	}

	public void sourceParameterRphasePhaseAngleSetPromptDisplay(){

		ApplicationLauncher.logger.debug("sourceParameterRphasePhaseAngleSetPromptDisplay: Entry");
		if(!getUserAbortedFlag()){
			String terminationMessage = " Degree \nAfter parameter is set, press <Ok> button to continue";

			/*		String R_Voltage = DisplayDataObj.getR_PhaseOutputVoltage();
		String R_Current = DisplayDataObj.getR_PhaseOutputCurrent();*/
			String R_Phase = String.valueOf(DeviceDataManagerController.get_PwrSrcR_PhaseDegreePhase());
			/*		String Y_Voltage = DisplayDataObj.getY_PhaseOutputVoltage();
		String Y_Current = DisplayDataObj.getY_PhaseOutputCurrent();
		String Y_Phase = String.valueOf(DeviceDataManagerController.get_PwrSrcY_PhaseDegreePhase());
		String B_Voltage = DisplayDataObj.getB_PhaseOutputVoltage();
		String B_Current = DisplayDataObj.getB_PhaseOutputCurrent();
		String B_Phase = String.valueOf(DeviceDataManagerController.get_PwrSrcB_PhaseDegreePhase());*/

			Alert alert = new Alert(AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
			alert.setTitle("Set Phase angle parameter");		
			String targetValue = R_Phase;
			String s = "Kindly set the R Phase, Phase angle to " + targetValue + terminationMessage;
			alert.setContentText(s);
			alert.showAndWait();
		}
		ApplicationLauncher.logger.debug("sourceParameterRphasePhaseAngleSetPromptDisplay: Exit");


	}

	public void sourceParameterRphaseCurrentSetPromptDisplay(){

		ApplicationLauncher.logger.debug("sourceParameterRphaseCurrentSetPromptDisplay: Entry");
		if(!getUserAbortedFlag()){
			String terminationMessage = " Amps \nAfter parameter is set, press <Ok> button to continue";

			/*		String R_Voltage = DisplayDataObj.getR_PhaseOutputVoltage();*/
			String R_Current = DeviceDataManagerController.getR_PhaseOutputCurrent();
			/*		String R_Phase = String.valueOf(DeviceDataManagerController.get_PwrSrcR_PhaseDegreePhase());
		String Y_Voltage = DisplayDataObj.getY_PhaseOutputVoltage();
		String Y_Current = DisplayDataObj.getY_PhaseOutputCurrent();
		String Y_Phase = String.valueOf(DeviceDataManagerController.get_PwrSrcY_PhaseDegreePhase());
		String B_Voltage = DisplayDataObj.getB_PhaseOutputVoltage();
		String B_Current = DisplayDataObj.getB_PhaseOutputCurrent();
		String B_Phase = String.valueOf(DeviceDataManagerController.get_PwrSrcB_PhaseDegreePhase());*/

			Alert alert = new Alert(AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
			alert.setTitle("Set Current parameter");		
			String targetValue = R_Current;
			String s = "Kindly set the R Phase, current to " + targetValue + terminationMessage;
			alert.setContentText(s);
			alert.showAndWait();
		}
		ApplicationLauncher.logger.debug("sourceParameterRphaseCurrentSetPromptDisplay: Exit");


	}





	public void sourceParameterRphaseCurrentSetReconfirmationPromptDisplay(){

		ApplicationLauncher.logger.debug("sourceParameterRphaseCurrentSetReconfirmationPromptDisplay: Entry");
		if(!getUserAbortedFlag()){
			String terminationMessage = " Amps \nif the value is altered, kindly retune and press <Ok> button to continue";

			/*		String R_Voltage = DisplayDataObj.getR_PhaseOutputVoltage();*/
			String R_Current = DeviceDataManagerController.getR_PhaseOutputCurrent();

			Alert alert = new Alert(AlertType.WARNING);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
			alert.setTitle("Set Current parameter-Reconfirm");		
			String targetValue = R_Current;
			String s = "Kindly re-verify the set current " + targetValue + terminationMessage;
			alert.setContentText(s);
			alert.showAndWait();
		}
		ApplicationLauncher.logger.debug("sourceParameterRphaseCurrentSetReconfirmationPromptDisplay: Exit");


	}

	public void sourceParameterRphaseVoltSetPromptDisplay(){

		ApplicationLauncher.logger.debug("sourceParameterRphaseVoltSetPromptDisplay: Entry");
		if(!getUserAbortedFlag()){
			String terminationMessage = " Volt \nAfter parameter is set, press <Ok> button to continue";

			String R_Voltage = DeviceDataManagerController.getR_PhaseOutputVoltage();
			/*		String R_Current = DisplayDataObj.getR_PhaseOutputCurrent();
		String R_Phase = String.valueOf(DeviceDataManagerController.get_PwrSrcR_PhaseDegreePhase());
		String Y_Voltage = DisplayDataObj.getY_PhaseOutputVoltage();
		String Y_Current = DisplayDataObj.getY_PhaseOutputCurrent();
		String Y_Phase = String.valueOf(DeviceDataManagerController.get_PwrSrcY_PhaseDegreePhase());
		String B_Voltage = DisplayDataObj.getB_PhaseOutputVoltage();
		String B_Current = DisplayDataObj.getB_PhaseOutputCurrent();
		String B_Phase = String.valueOf(DeviceDataManagerController.get_PwrSrcB_PhaseDegreePhase());*/

			Alert alert = new Alert(AlertType.INFORMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
			alert.setTitle("Set Voltage parameter");		
			String targetValue = R_Voltage;
			String s = "Kindly set the R Phase, voltage to " + targetValue + terminationMessage;
			alert.setContentText(s);
			alert.showAndWait();
		}
		ApplicationLauncher.logger.debug("sourceParameterRphaseVoltSetPromptDisplay: Exit");


	}



	public void sourceParameterBphasePhaseAngleSetPromptDisplay(){

		ApplicationLauncher.logger.debug("sourceParameterBphasePhaseAngleSetPromptDisplay: Entry");
		String terminationMessage = " Degree \nAfter parameter is set, press <Ok> button to continue";

		/*		String R_Voltage = DisplayDataObj.getR_PhaseOutputVoltage();
		String R_Current = DisplayDataObj.getR_PhaseOutputCurrent();
		String R_Phase = String.valueOf(DeviceDataManagerController.get_PwrSrcR_PhaseDegreePhase());
		String Y_Voltage = DisplayDataObj.getY_PhaseOutputVoltage();
		String Y_Current = DisplayDataObj.getY_PhaseOutputCurrent();
		String Y_Phase = String.valueOf(DeviceDataManagerController.get_PwrSrcY_PhaseDegreePhase());
		String B_Voltage = DisplayDataObj.getB_PhaseOutputVoltage();
		String B_Current = DisplayDataObj.getB_PhaseOutputCurrent();*/
		String B_Phase = String.valueOf(DeviceDataManagerController.get_PwrSrcB_PhaseDegreePhase());

		Alert alert = new Alert(AlertType.INFORMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
		alert.setTitle("Set Phase angle parameter");		
		String targetValue = B_Phase;
		String s = "Kindly set the B Phase, Phase angle to " + targetValue + terminationMessage;
		alert.setContentText(s);
		alert.showAndWait();

		ApplicationLauncher.logger.debug("sourceParameterBphasePhaseAngleSetPromptDisplay: Exit");


	}

	public void sourceParameterBphaseCurrentSetPromptDisplay(){

		ApplicationLauncher.logger.debug("sourceParameterBphaseCurrentSetPromptDisplay: Entry");
		String terminationMessage = " Amps \nAfter parameter is set, press <Ok> button to continue";

		/*		String R_Voltage = DisplayDataObj.getR_PhaseOutputVoltage();
		String R_Current = DisplayDataObj.getR_PhaseOutputCurrent();
		String R_Phase = String.valueOf(DeviceDataManagerController.get_PwrSrcR_PhaseDegreePhase());
		String Y_Voltage = DisplayDataObj.getY_PhaseOutputVoltage();
		String Y_Current = DisplayDataObj.getY_PhaseOutputCurrent();
		String Y_Phase = String.valueOf(DeviceDataManagerController.get_PwrSrcY_PhaseDegreePhase());
		String B_Voltage = DisplayDataObj.getB_PhaseOutputVoltage();*/
		String B_Current = DeviceDataManagerController.getB_PhaseOutputCurrent();
		/*		String B_Phase = String.valueOf(DeviceDataManagerController.get_PwrSrcB_PhaseDegreePhase());*/

		Alert alert = new Alert(AlertType.INFORMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
		alert.setTitle("Set Current parameter");		
		String targetValue = B_Current;
		String s = "Kindly set the B Phase, current to " + targetValue + terminationMessage;
		alert.setContentText(s);
		alert.showAndWait();

		ApplicationLauncher.logger.debug("sourceParameterBphaseCurrentSetPromptDisplay: Exit");


	}

	public void sourceParameterBphaseVoltSetPromptDisplay(){

		ApplicationLauncher.logger.debug("sourceParameterBphaseVoltSetPromptDisplay: Entry");
		String terminationMessage = " Volt \nAfter parameter is set, press <Ok> button to continue";

		/*		String R_Voltage = DisplayDataObj.getR_PhaseOutputVoltage();
		String R_Current = DisplayDataObj.getR_PhaseOutputCurrent();
		String R_Phase = String.valueOf(DeviceDataManagerController.get_PwrSrcR_PhaseDegreePhase());
		String Y_Voltage = DisplayDataObj.getY_PhaseOutputVoltage();
		String Y_Current = DisplayDataObj.getY_PhaseOutputCurrent();
		String Y_Phase = String.valueOf(DeviceDataManagerController.get_PwrSrcY_PhaseDegreePhase());*/
		String B_Voltage = DeviceDataManagerController.getB_PhaseOutputVoltage();
		/*		String B_Current = DisplayDataObj.getB_PhaseOutputCurrent();
		String B_Phase = String.valueOf(DeviceDataManagerController.get_PwrSrcB_PhaseDegreePhase());*/

		Alert alert = new Alert(AlertType.INFORMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
		alert.setTitle("Set Voltage parameter");		
		String targetValue = B_Voltage;
		String s = "Kindly set the B Phase, voltage to " + targetValue + terminationMessage;
		alert.setContentText(s);
		alert.showAndWait();

		ApplicationLauncher.logger.debug("sourceParameterBphaseVoltSetPromptDisplay: Exit");


	}




	public void sourceParameterYphasePhaseAngleSetPromptDisplay(){

		ApplicationLauncher.logger.debug("sourceParameterYphasePhaseAngleSetPromptDisplay: Entry");
		String terminationMessage = " Degree \nAfter parameter is set, press <Ok> button to continue";

		/*		String R_Voltage = DisplayDataObj.getR_PhaseOutputVoltage();
		String R_Current = DisplayDataObj.getR_PhaseOutputCurrent();
		String R_Phase = String.valueOf(DeviceDataManagerController.get_PwrSrcR_PhaseDegreePhase());
		String Y_Voltage = DisplayDataObj.getY_PhaseOutputVoltage();
		String Y_Current = DisplayDataObj.getY_PhaseOutputCurrent();*/
		String Y_Phase = String.valueOf(DeviceDataManagerController.get_PwrSrcY_PhaseDegreePhase());
		/*		String B_Voltage = DisplayDataObj.getB_PhaseOutputVoltage();
		String B_Current = DisplayDataObj.getB_PhaseOutputCurrent();
		String B_Phase = String.valueOf(DeviceDataManagerController.get_PwrSrcB_PhaseDegreePhase());*/

		Alert alert = new Alert(AlertType.INFORMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
		alert.setTitle("Set Phase angle parameter");		
		String targetValue = Y_Phase;
		String s = "Kindly set the Y Phase, Phase angle to " + targetValue + terminationMessage;
		alert.setContentText(s);
		alert.showAndWait();

		ApplicationLauncher.logger.debug("sourceParameterYphasePhaseAngleSetPromptDisplay: Exit");


	}

	public void sourceParameterYphaseCurrentSetPromptDisplay(){

		ApplicationLauncher.logger.debug("sourceParameterYphaseCurrentSetPromptDisplay: Entry");
		String terminationMessage = " Amps \nAfter parameter is set, press <Ok> button to continue";

		/*		String R_Voltage = DisplayDataObj.getR_PhaseOutputVoltage();
		String R_Current = DisplayDataObj.getR_PhaseOutputCurrent();
		String R_Phase = String.valueOf(DeviceDataManagerController.get_PwrSrcR_PhaseDegreePhase());
		String Y_Voltage = DisplayDataObj.getY_PhaseOutputVoltage();*/
		String Y_Current = DeviceDataManagerController.getY_PhaseOutputCurrent();
		/*		String Y_Phase = String.valueOf(DeviceDataManagerController.get_PwrSrcY_PhaseDegreePhase());
		String B_Voltage = DisplayDataObj.getB_PhaseOutputVoltage();
		String B_Current = DisplayDataObj.getB_PhaseOutputCurrent();
		String B_Phase = String.valueOf(DeviceDataManagerController.get_PwrSrcB_PhaseDegreePhase());*/

		Alert alert = new Alert(AlertType.INFORMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
		alert.setTitle("Set Current parameter");		
		String targetValue = Y_Current;
		String s = "Kindly set the Y Phase, current to " + targetValue + terminationMessage;
		alert.setContentText(s);
		alert.showAndWait();

		ApplicationLauncher.logger.debug("sourceParameterYphaseCurrentSetPromptDisplay: Exit");


	}

	public void sourceParameterYphaseVoltSetPromptDisplay(){

		ApplicationLauncher.logger.debug("sourceParameterYphaseVoltSetPromptDisplay: Entry");
		String terminationMessage = " Volt \nAfter parameter is set, press <Ok> button to continue";

		/*		String R_Voltage = DisplayDataObj.getR_PhaseOutputVoltage();
		String R_Current = DisplayDataObj.getR_PhaseOutputCurrent();
		String R_Phase = String.valueOf(DeviceDataManagerController.get_PwrSrcR_PhaseDegreePhase());*/
		String Y_Voltage = DeviceDataManagerController.getY_PhaseOutputVoltage();
		/*		String Y_Current = DisplayDataObj.getY_PhaseOutputCurrent();
		String Y_Phase = String.valueOf(DeviceDataManagerController.get_PwrSrcY_PhaseDegreePhase());
		String B_Voltage = DisplayDataObj.getB_PhaseOutputVoltage();
		String B_Current = DisplayDataObj.getB_PhaseOutputCurrent();
		String B_Phase = String.valueOf(DeviceDataManagerController.get_PwrSrcB_PhaseDegreePhase());*/

		Alert alert = new Alert(AlertType.INFORMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
		alert.setTitle("Set Voltage parameter");		
		String targetValue = Y_Voltage;
		String s = "Kindly set the Y Phase, voltage to " + targetValue + terminationMessage;
		alert.setContentText(s);
		alert.showAndWait();

		ApplicationLauncher.logger.debug("sourceParameterYphaseVoltSetPromptDisplay: Exit");


	}



	public boolean SecondLevelValidation(){
		ApplicationLauncher.logger.debug("SecondLevelValidation: Entry");

		if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_FEATURE_ENABLED){
			ApplicationLauncher.logger.debug("SecondLevelValidation: harmonics exit");
			return true;
		}/*else if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
			ApplicationLauncher.logger.debug("SecondLevelValidation: Bofa Source connected");
			return true;
		}*/
		ApplicationHomeController.updateBottomSecondaryStatus("Reference std validation - awaiting",ConstantApp.LEFT_STATUS_INFO);
		if(getTwoLevelValidationOnFlag())  {
			boolean status = false;
			int retry_count = ConstantAppConfig.SECOND_VALIDATION_RETRY_COUNT;
			long sleep_time = (ConstantAppConfig.SECOND_VALIDATION_WAIT_TIME /retry_count)*1000;
			ApplicationLauncher.logger.info("SecondLevelValidation: sleep_time: " + sleep_time);
			ApplicationLauncher.logger.info(getCurrentTestPointName()+":"+ getCurrentTestPoint_Index()+"SecondLevelValidation: While Entry");
			String metertype = DeviceDataManagerController.getDeployedEM_ModelType();
			while((!status) && (retry_count > 0) && !getUserAbortedFlag()){
				FailureManager.ResetRefStdFeedBackReasonForFailure();
				//ApplicationLauncher.logger.info(getCurrentTestPointName()+":"+ getCurrentTestPoint_Index()+"SecondLevelValidation: While ...");
				status  = CheckPhaseRReading();
				if(status && metertype.contains(ConstantApp.METERTYPE_THREEPHASE)  && !getUserAbortedFlag()){
					status  = CheckPhaseYReading();
					if(status && !getUserAbortedFlag()){
						status  = CheckPhaseBReading();
						if(status && !getUserAbortedFlag()){
							status = CheckPhaseRFrequencyReading();
							if(status){

								FailureManager.ResetRefStdFeedBackReasonForFailure();
								ApplicationLauncher.logger.info("**************************");
								ApplicationLauncher.logger.info("3Phase: "+getCurrentTestPointName()+":"+ getCurrentTestPoint_Index()+" :StabilityValidation:RefStd:SecondLevelValidation: Success");
								ApplicationLauncher.logger.info("**************************");
							}	
						}	
					}
				} 
				if(status && metertype.contains(ConstantApp.METERTYPE_SINGLEPHASE)  && !getUserAbortedFlag()){

					status = CheckPhaseRFrequencyReading();
					if(status){
						ApplicationHomeController.updateBottomSecondaryStatus("RefStd: target achieved",ConstantApp.LEFT_STATUS_INFO);
						FailureManager.ResetRefStdFeedBackReasonForFailure();
						ApplicationLauncher.logger.info("**************************");
						ApplicationLauncher.logger.info("Single Phase: "+getCurrentTestPointName()+":"+ getCurrentTestPoint_Index()+" :StabilityValidation:RefStd:SecondLevelValidation: Success");
						ApplicationLauncher.logger.info("**************************");
					}else {
						ApplicationLauncher.logger.info("1Phase: "+getCurrentTestPointName()+":"+ getCurrentTestPoint_Index()+" :StabilityValidation:RefStd:SecondLevelValidation: Freq Failed");

					}

				} else {

				}
				if(!status  && !getUserAbortedFlag()){
					retry_count--;

					ApplicationLauncher.logger.info(getCurrentTestPointName()+":"+ getCurrentTestPoint_Index()+" :SecondLevelValidation: Waiting...");

					Sleep((int)sleep_time);

				}
			}
			if(getUserAbortedFlag()){
				ApplicationLauncher.logger.info(getCurrentTestPointName()+":"+ getCurrentTestPoint_Index()+" :SecondLevelValidation: User aborted, exiting validation" );
				ApplicationHomeController.updateBottomSecondaryStatus("RefStd: Aborted",ConstantApp.LEFT_STATUS_INFO);

				return false;
			}

			if(status){
				SerialDataManager.setSkipCurrentTP_Execution(false);
				ApplicationLauncher.logger.debug(getCurrentTestPointName()+":"+ getCurrentTestPoint_Index()+" :SecondLevelValidation : resetContinuousFailureCounter");
				resetRefStdFeedBackContinuousFailureCounter();
			}
			else{
				SerialDataManager.setSkipCurrentTP_Execution(true);
				updateSkippedInDisplay();
				checkRefStdFeedBackcontinuousFailureCounter();
				ApplicationLauncher.logger.info("*******************************************");
				ApplicationLauncher.logger.info("*******************************************");
				ApplicationLauncher.logger.info("*******************************************");
				ApplicationLauncher.logger.info("**************************");
				ApplicationLauncher.logger.info(getCurrentTestPointName()+":"+ getCurrentTestPoint_Index()+" :SecondLevelValidation: Failed");
				ApplicationLauncher.logger.info("**************************");
				ApplicationLauncher.logger.info("StabilityValidation:RefStd:SecondLevelValidation: Failed");
				ApplicationLauncher.logger.info("Reason:"+FailureManager.getRefStdFeedBackReasonForFailure());
				ApplicationLauncher.logger.info("*******************************************");
				ApplicationLauncher.logger.info("*******************************************");
				ApplicationLauncher.logger.info("*******************************************");
				ApplicationHomeController.updateBottomSecondaryStatus("RefStd: validation failed",ConstantApp.LEFT_STATUS_INFO);

			}
			ApplicationLauncher.logger.info(getCurrentTestPointName()+":"+ getCurrentTestPoint_Index()+" :SecondLevelValidation: exit: " + status);

			return status;
		}else{

			return true;
		}
	}


	public void checkPowerSrcOnContinuousFailureCounter(){
		PowerSrcOnContinuousFailureCounter--;
		ApplicationLauncher.logger.info("checkPowerSrcOnContinuousFailureCounter: PowerSrcOnContinuousFailureCounter:  " + PowerSrcOnContinuousFailureCounter);
		if(PowerSrcOnContinuousFailureCounter <= 0){
			setProcessTC_ExecutionRefreshTimeInMSec(200);
			setPowerSrcContinuousFailureStatus(true);
			ApplicationLauncher.logger.info("******************************************");
			ApplicationLauncher.logger.info("******************************************");
			ApplicationLauncher.logger.info("Aborting all subsequent Test point due to continous power source set failure");
			ApplicationLauncher.logger.info("******************************************");
			ApplicationLauncher.logger.info("******************************************");
		}
	}


	public void checkRefStdFeedBackcontinuousFailureCounter(){
		RefStdFeedBackContinuousFailureCounter--;
		ApplicationLauncher.logger.info("checkRefStdFeedBackContinuousFailureCounter: RefStdFeedBackContinuousFailureCounter:  " + RefStdFeedBackContinuousFailureCounter);
		if(RefStdFeedBackContinuousFailureCounter <= 0){
			setProcessTC_ExecutionRefreshTimeInMSec(200);
			setRefStdFeedBackContinuousFailureStatus(true);
			ApplicationLauncher.logger.info("******************************************");
			ApplicationLauncher.logger.info("******************************************");
			ApplicationLauncher.logger.info("Aborting all subsequent Test point due to Reference Standard Feedback validation failure");
			ApplicationLauncher.logger.info("******************************************");
			ApplicationLauncher.logger.info("******************************************");
		}
	}



	public void setPowerSrcContinuousFailureStatus( boolean value){
		PowerSrcContinuousFailureStatus = value;
	}

	public boolean getPowerSrcContinuousFailureStatus(){
		return PowerSrcContinuousFailureStatus;
	}

	public void resetPowerSrcContinuousFailureCounter(){
		//PowerSrcOnContinuousFailureCounter = ConstantPowerSource.POWER_SRC_ACCEPTABLE_CONTINUOUS_FAILURE_COUNTER;
		PowerSrcOnContinuousFailureCounter = ConstantAppConfig.POWER_SRC_ACCEPTED_CONTINUOUS_FAILURE_COUNTER;
	}

	public void setRefStdFeedBackContinuousFailureStatus( boolean value){
		RefStdFeedBackContinuousFailureStatus = value;
	}

	public boolean getRefStdFeedBackContinuousFailureStatus(){
		return RefStdFeedBackContinuousFailureStatus;
	}

	public void resetRefStdFeedBackContinuousFailureCounter(){
		RefStdFeedBackContinuousFailureCounter = ConstantRefStdRadiant.REF_STD_FEEDBACK_CONTINUOUS_FAILURE_COUNTER;
	}

	public void updateSkippedInDisplay2(String failureMessage){
		ApplicationLauncher.logger.debug("updateSkippedInDisplay2: Entry");
		String project_name = getCurrentProjectName();//MeterParamsController.getCurrentProjectName();
		String test_case_name = getCurrentTestPointName();
		String alias_id = getCurrentTestAliasID();
		JSONObject devices_json = DisplayDataObj.getDeployedDevicesJson();//MySQL_Controller.sp_getdeploy_devices(project_name);
		JSONArray devices;
		String FailureReason = failureMessage;
		String rack_id="";
		try {
			devices = devices_json.getJSONArray("Devices");
			JSONObject jobj = new JSONObject();
			for(int i=0;i<devices.length(); i++){
				jobj = devices.getJSONObject(i);
				//rack_id = jobj.getString("Rack_ID");
				rack_id = String.valueOf(jobj.getInt("Rack_ID"));
				LiveTableDataManager.UpdateliveTableData(Integer.parseInt(rack_id), "N","Skipped");
			}
			rack_id ="0";
			ApplicationHomeController.update_left_status("Updating skipped TC-DB",ConstantApp.LEFT_STATUS_DEBUG);
			int seqNumber = getCurrentTestPoint_Index()+1;
			if(ProcalFeatureEnable.PROCON_INTERFACE_ENABLED) {
				String dutSerialNo = DeviceDataManagerController.getConveyorDutSerialNumberMap(0);
				MySQL_Controller.sp_add_resultWithProjectRunId(project_name, test_case_name, alias_id, rack_id, "N",DeviceDataManagerController.getError_countAndIncrement(), "Skipped",FailureReason,ConstantReport.RESULT_DATA_TYPE_FAILURE_REASON,
						DeviceDataManagerController.getExecutionMctNctMode(),DeviceDataManagerController.getEnergyFlowMode(),
						getSelectedDeployment_ID(),seqNumber,getPresentProjectRunId(),
						DeviceDataManagerController.get_Error_min(),DeviceDataManagerController.get_Error_max(),dutSerialNo);
				
			}else {
				MySQL_Controller.sp_add_result(project_name, test_case_name, alias_id, rack_id, "N",DeviceDataManagerController.getError_countAndIncrement(), "Skipped",FailureReason,ConstantReport.RESULT_DATA_TYPE_FAILURE_REASON,
						DeviceDataManagerController.getExecutionMctNctMode(),DeviceDataManagerController.getEnergyFlowMode(),
						getSelectedDeployment_ID(),seqNumber);
			}
			ApplicationHomeController.update_left_status("Finished updating skipped TC-DB",ConstantApp.LEFT_STATUS_DEBUG);

		} catch (JSONException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("updateSkippedInDisplay2: JSONException:"+e.getMessage());
		}
	}


	public void updateSkippedInDisplay(){
		ApplicationLauncher.logger.debug("updateSkippedInDisplay: Entry");
		String project_name = getCurrentProjectName();//MeterParamsController.getCurrentProjectName();
		String test_case_name = getCurrentTestPointName();
		String alias_id = getCurrentTestAliasID();
		JSONObject devices_json = DisplayDataObj.getDeployedDevicesJson();//MySQL_Controller.sp_getdeploy_devices(project_name);
		JSONArray devices;
		String FailureReason = FailureManager.getPowerSrcReasonForFailure()+"," + FailureManager.getRefStdFeedBackReasonForFailure();
		ApplicationLauncher.logger.debug("updateSkippedInDisplay: FailureReason: " + FailureReason);
		ApplicationLauncher.logger.debug("updateSkippedInDisplay: FailureReason length: " + FailureReason.length());
		String rack_id="";
		try {
			devices = devices_json.getJSONArray("Devices");
			JSONObject jobj = new JSONObject();
			for(int i=0;i<devices.length(); i++){
				jobj = devices.getJSONObject(i);
				//rack_id = jobj.getString("Rack_ID");
				rack_id = String.valueOf(jobj.getInt("Rack_ID"));
				LiveTableDataManager.UpdateliveTableData(Integer.parseInt(rack_id), "N","Skipped");
			}
			rack_id ="0";
			ApplicationHomeController.update_left_status("Updating skipped TC-DB",ConstantApp.LEFT_STATUS_DEBUG);
			int seqNumber = getCurrentTestPoint_Index()+1;
			
			if(ProcalFeatureEnable.PROCON_INTERFACE_ENABLED) {
				String dutSerialNo = DeviceDataManagerController.getConveyorDutSerialNumberMap(0);
				MySQL_Controller.sp_add_resultWithProjectRunId(project_name, test_case_name, alias_id, rack_id, "N",DeviceDataManagerController.getError_countAndIncrement(), "Skipped",FailureReason,ConstantReport.RESULT_DATA_TYPE_FAILURE_REASON,
						DeviceDataManagerController.getExecutionMctNctMode(),DeviceDataManagerController.getEnergyFlowMode(),
						getSelectedDeployment_ID(),seqNumber,getPresentProjectRunId(),
						DeviceDataManagerController.get_Error_min(),DeviceDataManagerController.get_Error_max(),dutSerialNo);
				
			}else {
				MySQL_Controller.sp_add_result(project_name, test_case_name, alias_id, rack_id, "N",DeviceDataManagerController.getError_countAndIncrement(), "Skipped",FailureReason,ConstantReport.RESULT_DATA_TYPE_FAILURE_REASON,
						DeviceDataManagerController.getExecutionMctNctMode(),DeviceDataManagerController.getEnergyFlowMode(),
						getSelectedDeployment_ID(),seqNumber);
			}
			ApplicationHomeController.update_left_status("Finished updating skipped TC-DB",ConstantApp.LEFT_STATUS_DEBUG);

		} catch (JSONException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("updateSkippedInDisplay: JSONException:"+e.getMessage());
		}
	}

	public void updateAbortedInDisplay(){
		String project_name = getCurrentProjectName();//MeterParamsController.getCurrentProjectName();
		String test_case_name = getCurrentTestPointName();
		String alias_id = getCurrentTestAliasID();
		JSONObject devices_json = DisplayDataObj.getDeployedDevicesJson();//MySQL_Controller.sp_getdeploy_devices(project_name);
		JSONArray devices;
		String FailureReason = ErrorCodeMapping.ERROR_CODE_3000 + "," + FailureManager.getPowerSrcReasonForFailure()+"," + FailureManager.getRefStdFeedBackReasonForFailure();
		String rack_id ="";

		try {
			devices = devices_json.getJSONArray("Devices");
			JSONObject jobj = new JSONObject();
			for(int i=0;i<devices.length(); i++){
				jobj = devices.getJSONObject(i);
				//rack_id = jobj.getString("Rack_ID");
				rack_id = String.valueOf(jobj.getInt("Rack_ID"));
				LiveTableDataManager.UpdateliveTableData(Integer.parseInt(rack_id), "N","Aborted");
			}
			rack_id= "0";
			ApplicationHomeController.update_left_status("Updating aborted results-DB",ConstantApp.LEFT_STATUS_DEBUG);
			int seqNumber = getCurrentTestPoint_Index()+1;
			if(ProcalFeatureEnable.PROCON_INTERFACE_ENABLED) {
				String dutSerialNo = DeviceDataManagerController.getConveyorDutSerialNumberMap(0);
				MySQL_Controller.sp_add_resultWithProjectRunId(project_name, test_case_name, alias_id, rack_id, "N",DeviceDataManagerController.getError_countAndIncrement(), "Aborted",FailureReason,
						ConstantReport.RESULT_DATA_TYPE_FAILURE_REASON,DeviceDataManagerController.getExecutionMctNctMode(),DeviceDataManagerController.getEnergyFlowMode(),
						getSelectedDeployment_ID(),seqNumber,getPresentProjectRunId(),
						DeviceDataManagerController.get_Error_min(),DeviceDataManagerController.get_Error_max(),dutSerialNo);
				
			}else {
				MySQL_Controller.sp_add_result(project_name, test_case_name, alias_id, rack_id, "N",DeviceDataManagerController.getError_countAndIncrement(), "Aborted",FailureReason,
						ConstantReport.RESULT_DATA_TYPE_FAILURE_REASON,DeviceDataManagerController.getExecutionMctNctMode(),DeviceDataManagerController.getEnergyFlowMode(),
						getSelectedDeployment_ID(),seqNumber);
			}
			ApplicationHomeController.update_left_status("Finished updating aborted results-DB",ConstantApp.LEFT_STATUS_DEBUG);

		} catch (JSONException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("updateAbortedInDisplay: JSONException:"+e.getMessage());
		}
	}

	public void updateNotExecutedInDisplay(){
		String project_name = getCurrentProjectName();//MeterParamsController.getCurrentProjectName();
		String test_case_name = getCurrentTestPointName();
		String alias_id = getCurrentTestAliasID();
		JSONObject devices_json = DisplayDataObj.getDeployedDevicesJson();//MySQL_Controller.sp_getdeploy_devices(project_name);
		FailureManager.AppendPowerSrcReasonForFailure(ErrorCodeMapping.ERROR_CODE_301);
		String rack_id = "";
		JSONArray devices;
		try {
			devices = devices_json.getJSONArray("Devices");
			JSONObject jobj = new JSONObject();
			for(int i=0;i<devices.length(); i++){
				jobj = devices.getJSONObject(i);
				//rack_id = jobj.getString("Rack_ID");
				rack_id = String.valueOf(jobj.getInt("Rack_ID"));
				LiveTableDataManager.UpdateliveTableData(Integer.parseInt(rack_id), "N","Not Executed");
			}
			rack_id="0";
			ApplicationHomeController.update_left_status("updating Not Executed TC In DB",ConstantApp.LEFT_STATUS_DEBUG);
			int seqNumber = getCurrentTestPoint_Index()+1;
			
			if(ProcalFeatureEnable.PROCON_INTERFACE_ENABLED) {
				String dutSerialNo = DeviceDataManagerController.getConveyorDutSerialNumberMap(0);
				MySQL_Controller.sp_add_resultWithProjectRunId(project_name, test_case_name, alias_id, rack_id, "N",DeviceDataManagerController.getError_countAndIncrement(), "Not Executed",ErrorCodeMapping.ERROR_CODE_301,ConstantReport.RESULT_DATA_TYPE_FAILURE_REASON,
						DeviceDataManagerController.getExecutionMctNctMode(),DeviceDataManagerController.getEnergyFlowMode(),
						getSelectedDeployment_ID(),seqNumber,getPresentProjectRunId(),
						DeviceDataManagerController.get_Error_min(),DeviceDataManagerController.get_Error_max(),dutSerialNo);
				
			}else {
				MySQL_Controller.sp_add_result(project_name, test_case_name, alias_id, rack_id, "N",DeviceDataManagerController.getError_countAndIncrement(), "Not Executed",ErrorCodeMapping.ERROR_CODE_301,ConstantReport.RESULT_DATA_TYPE_FAILURE_REASON,
						DeviceDataManagerController.getExecutionMctNctMode(),DeviceDataManagerController.getEnergyFlowMode(),
						getSelectedDeployment_ID(),seqNumber);
			}
			ApplicationHomeController.update_left_status("Finished updating Not Executed TC In DB",ConstantApp.LEFT_STATUS_DEBUG);

		} catch (JSONException e) {
			
			e.printStackTrace();
			ApplicationLauncher.logger.error("updateNotExecutedInDisplay: JSONException:"+e.getMessage());
		}
	}



	public boolean CheckPhaseRReading(){
		boolean status = false;
		ApplicationLauncher.logger.info("CheckPhaseRVoltReading: Reading...");
		status = CheckPhaseRVoltReading();
		ApplicationLauncher.logger.info("CheckPhaseRVoltReading: status: "+ status);
		if(status && !getUserAbortedFlag()){
			ApplicationLauncher.logger.debug("CheckPhaseRVoltReading: voltage: result Good");
			ApplicationHomeController.updateBottomSecondaryStatus("RefStd: target voltage achieved",ConstantApp.LEFT_STATUS_INFO);
			ApplicationLauncher.logger.info("CheckPhaseRCurrentReading: Reading...");
			status = CheckPhaseRCurrentReading();
			ApplicationLauncher.logger.info("CheckPhaseRCurrentReading: status: "+ status);
			if(status && !getUserAbortedFlag()){
				ApplicationLauncher.logger.debug("CheckPhaseRVoltReading: current: result Good");
				ApplicationHomeController.updateBottomSecondaryStatus("RefStd: target current achieved",ConstantApp.LEFT_STATUS_INFO);
				ApplicationLauncher.logger.info("CheckPhaseRDegreeReading: Reading...");
				status = CheckPhaseRDegreeReading();
				ApplicationLauncher.logger.info("CheckPhaseRDegreeReading: status: "+ status);
				if(!status){
					FailureManager.AppendRefStdFeedBackReasonForFailure(ErrorCodeMapping.ERROR_CODE_213+":Expected:"+FailureManager.getRefStdSetDegree()+" :Actual:"+FailureManager.getRefStdFeedBackDegree());
				}else {
					ApplicationLauncher.logger.debug("CheckPhaseRVoltReading: phase angle: result Good");
					ApplicationHomeController.updateBottomSecondaryStatus("RefStd: target phase angle achieved",ConstantApp.LEFT_STATUS_INFO);
				}
			}else{
				FailureManager.AppendRefStdFeedBackReasonForFailure(ErrorCodeMapping.ERROR_CODE_212+":Expected:"+FailureManager.getRefStdSetCurrent()+" :Actual:"+FailureManager.getRefStdFeedBackCurrent());
			}
		} else{
			FailureManager.AppendRefStdFeedBackReasonForFailure(ErrorCodeMapping.ERROR_CODE_211+":Expected:"+FailureManager.getRefStdSetVolt()+" :Actual:"+FailureManager.getRefStdFeedBackVolt());
		}

		return status;
	}

	public boolean CheckPhaseYReading(){
		boolean status = false;
		ApplicationLauncher.logger.info("CheckPhaseYVoltReading: Reading...");
		status = CheckPhaseYVoltReading();
		ApplicationLauncher.logger.info("CheckPhaseYVoltReading: status: "+ status);
		if(status && !getUserAbortedFlag()){
			ApplicationLauncher.logger.info("CheckPhaseYCurrentReading: Reading...");
			status = CheckPhaseYCurrentReading();
			ApplicationLauncher.logger.info("CheckPhaseYCurrentReading: status: "+ status);
			if(status && !getUserAbortedFlag()){
				ApplicationLauncher.logger.info("CheckPhaseYDegreeReading: Reading...");
				status = CheckPhaseYDegreeReading();
				ApplicationLauncher.logger.info("CheckPhaseYDegreeReading: status: "+ status);

				if(!status){
					FailureManager.AppendRefStdFeedBackReasonForFailure(ErrorCodeMapping.ERROR_CODE_223+":Expected:"+FailureManager.getRefStdSetDegree()+" :Actual:"+FailureManager.getRefStdFeedBackDegree());
				}
			}else{
				FailureManager.AppendRefStdFeedBackReasonForFailure(ErrorCodeMapping.ERROR_CODE_222+":Expected:"+FailureManager.getRefStdSetCurrent()+" :Actual:"+FailureManager.getRefStdFeedBackCurrent());
			}
		} else{
			FailureManager.AppendRefStdFeedBackReasonForFailure(ErrorCodeMapping.ERROR_CODE_221+":Expected:"+FailureManager.getRefStdSetVolt()+" :Actual:"+FailureManager.getRefStdFeedBackVolt());
		}

		return status;
	}

	public boolean CheckPhaseBReading(){
		boolean status = false;
		ApplicationLauncher.logger.info("CheckPhaseBVoltReading: Reading...");
		status = CheckPhaseBVoltReading();
		ApplicationLauncher.logger.info("CheckPhaseBVoltReading: status: "+ status);
		if(status && !getUserAbortedFlag()){
			ApplicationLauncher.logger.info("CheckPhaseBCurrentReading: Reading...");
			status = CheckPhaseBCurrentReading();
			ApplicationLauncher.logger.info("CheckPhaseBCurrentReading: status: "+ status);
			if(status && !getUserAbortedFlag()){
				ApplicationLauncher.logger.info("CheckPhaseBDegreeReading: Reading...");
				status = CheckPhaseBDegreeReading();
				ApplicationLauncher.logger.info("CheckPhaseBDegreeReading: status: "+ status);

				if(!status){
					FailureManager.AppendRefStdFeedBackReasonForFailure(ErrorCodeMapping.ERROR_CODE_233+":Expected:"+FailureManager.getRefStdSetDegree()+" :Actual:"+FailureManager.getRefStdFeedBackDegree());
				}
			}else{
				FailureManager.AppendRefStdFeedBackReasonForFailure(ErrorCodeMapping.ERROR_CODE_232+":Expected:"+FailureManager.getRefStdSetCurrent()+" :Actual:"+FailureManager.getRefStdFeedBackCurrent());
			}
		} else{
			FailureManager.AppendRefStdFeedBackReasonForFailure(ErrorCodeMapping.ERROR_CODE_231+":Expected:"+FailureManager.getRefStdSetVolt()+" :Actual:"+FailureManager.getRefStdFeedBackVolt());
		}

		return status;
	}

	public boolean CheckPhaseRVoltReading(){
		boolean status = false;
		try{
			float phaseR_volt = Float.parseFloat(getFeedbackR_phaseVolt());
			float phaseR_given_volt = Float.parseFloat(DeviceDataManagerController.getR_PhaseOutputVoltage());

			//if(getCurrentTestType().equals("InfluenceHarmonic")){
			if(getCurrentTestType().equals(TestProfileType.InfluenceHarmonic.toString())){
				status = IsValueInRange(phaseR_given_volt, phaseR_volt, ConstantAppConfig.HAR_VOLT_ACCEPTED_PERCENTAGE,ConstantApp.IS_VALUE_IN_RANGE_FOR_VOLTAGE);
			}
			else{
				status = IsValueInRange(phaseR_given_volt, phaseR_volt, ConstantAppConfig.VOLT_ACCEPTED_PERCENTAGE,ConstantApp.IS_VALUE_IN_RANGE_FOR_VOLTAGE);
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("CheckPhaseRVoltReading :Exception:"+ e.getMessage());
		}
		return status;
	}

	public boolean CheckPhaseRCurrentReading(){
		boolean status = false;
		try{
			float phaseR_current = Float.parseFloat(getFeedbackR_phaseCurrent());
			float phaseR_given_current = Float.parseFloat(DeviceDataManagerController.getR_PhaseOutputCurrent());

			//if(getCurrentTestType().equals("InfluenceHarmonic")){
			if(getCurrentTestType().equals(TestProfileType.InfluenceHarmonic.toString())){
				status = IsValueInRange(phaseR_given_current, phaseR_current, ConstantAppConfig.HAR_CURRENT_ACCEPTED_PERCENTAGE,ConstantApp.IS_VALUE_IN_RANGE_FOR_CURRENT);
			}
			else{
				status = IsValueInRange(phaseR_given_current, phaseR_current, ConstantAppConfig.CURRENT_ACCEPTED_PERCENTAGE,ConstantApp.IS_VALUE_IN_RANGE_FOR_CURRENT);
			}

		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("CheckPhaseRCurrentReading :Exception:"+ e.getMessage());
		}
		return status;
	}

	public boolean CheckPhaseRDegreeReading(){
		boolean status = false;
		try{
			if((Float.parseFloat(DeviceDataManagerController.getR_PhaseOutputCurrent()) != 0f) &&
					(Float.parseFloat(DeviceDataManagerController.getR_PhaseOutputVoltage()) != 0f) ){
				float phaseR_degree = Float.parseFloat(getFeedbackR_phaseDegree());
				float phaseR_given_degree = DeviceDataManagerController.get_PwrSrcR_PhaseDegreePhase();
				ApplicationLauncher.logger.info("CheckPhaseRDegreeReading : DEGREE_ACCEPTED_PERCENTAGE:"+ ConstantAppConfig.DEGREE_ACCEPTED_PERCENTAGE);
				status = IsDegreeInRange(phaseR_given_degree, phaseR_degree, ConstantAppConfig.DEGREE_ACCEPTED_PERCENTAGE);
			}
			else{
				status = true;
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("CheckPhaseRDegreeReading :Exception:"+ e.getMessage());
		}
		return status;
	}

	public boolean CheckPhaseRFrequencyReading(){
		boolean status = false;
		String LocalLogFailureWhichPhase = "";


		try{
			ArrayList<String> phase_enabled = DeviceDataManagerController.getPhaseDegreeOutput();
			float phaseR_freq =0;
			//float phaseY_freq =0;
			//float phaseB_freq =0;

			ApplicationLauncher.logger.info("CheckPhaseRFrequencyReading :phase_enabled:"+ phase_enabled);
			if(phase_enabled.size() != 0){

				if(phase_enabled.get(0).equals(FirstPhaseDisplayName)){
					ApplicationLauncher.logger.info("CheckPhaseRFrequencyReading :getFeedbackR_Frequency():"+ getFeedbackR_Frequency());
					phaseR_freq = Float.parseFloat(getFeedbackR_Frequency());
					LocalLogFailureWhichPhase = "R";
				}else if(phase_enabled.get(0).equals(SecondPhaseDisplayName)){
					ApplicationLauncher.logger.info("CheckPhaseRFrequencyReading :getFeedbackY_Frequency():"+ getFeedbackY_Frequency());
					phaseR_freq = Float.parseFloat(getFeedbackY_Frequency());
					LocalLogFailureWhichPhase = "Y";
				}else if(phase_enabled.get(0).equals(ThirdPhaseDisplayName)){
					ApplicationLauncher.logger.info("CheckPhaseRFrequencyReading :getFeedbackB_Frequency() :"+ getFeedbackB_Frequency());
					phaseR_freq = Float.parseFloat(getFeedbackB_Frequency());
					LocalLogFailureWhichPhase = "B";
				}else if(phase_enabled.get(0).equals(FirstAndSecondPhase)){	
					ApplicationLauncher.logger.info("CheckPhaseRFrequencyReading :getFeedbackR_Frequency():"+ getFeedbackR_Frequency());
					phaseR_freq = Float.parseFloat(getFeedbackR_Frequency());
					ApplicationLauncher.logger.info("CheckPhaseRFrequencyReading :getFeedbackY_Frequency():"+ getFeedbackY_Frequency());
					phaseR_freq = Float.parseFloat(getFeedbackY_Frequency());
					LocalLogFailureWhichPhase = "RY";
				}else if(phase_enabled.get(0).equals(SecondAndThirdPhase)){
					ApplicationLauncher.logger.info("CheckPhaseRFrequencyReading :getFeedbackY_Frequency():"+ getFeedbackY_Frequency());
					phaseR_freq = Float.parseFloat(getFeedbackY_Frequency());
					ApplicationLauncher.logger.info("CheckPhaseRFrequencyReading :getFeedbackB_Frequency() :"+ getFeedbackB_Frequency());
					phaseR_freq = Float.parseFloat(getFeedbackB_Frequency());
					LocalLogFailureWhichPhase = "YB";
				}else if(phase_enabled.get(0).equals(FirstAndThirdPhase)){
					ApplicationLauncher.logger.info("CheckPhaseRFrequencyReading :getFeedbackR_Frequency():"+ getFeedbackR_Frequency());
					phaseR_freq = Float.parseFloat(getFeedbackR_Frequency());
					ApplicationLauncher.logger.info("CheckPhaseRFrequencyReading :getFeedbackB_Frequency() :"+ getFeedbackB_Frequency());
					phaseR_freq = Float.parseFloat(getFeedbackB_Frequency());
					LocalLogFailureWhichPhase = "RB";
				}else{
					ApplicationLauncher.logger.info("CheckPhaseRFrequencyReading :getFeedbackR_Frequency()-1:"+ getFeedbackR_Frequency());
					phaseR_freq = Float.parseFloat(getFeedbackR_Frequency());
					LocalLogFailureWhichPhase = "RRR";
				}
				float phaseR_given_freq = DeviceDataManagerController.get_PwrSrc_Freq();
				status = IsValueInRange(phaseR_given_freq, phaseR_freq, ConstantAppConfig.FREQUENCY_ACCEPTED_PERCENTAGE,ConstantApp.IS_VALUE_IN_RANGE_FOR_FREQUENCY);

				if(!status){
					phaseR_freq = Float.parseFloat(getFeedbackY_Frequency());
					status = IsValueInRange(phaseR_given_freq, phaseR_freq, ConstantAppConfig.FREQUENCY_ACCEPTED_PERCENTAGE,ConstantApp.IS_VALUE_IN_RANGE_FOR_FREQUENCY);

					if(!status){
						phaseR_freq = Float.parseFloat(getFeedbackB_Frequency());
						status = IsValueInRange(phaseR_given_freq, phaseR_freq, ConstantAppConfig.FREQUENCY_ACCEPTED_PERCENTAGE,ConstantApp.IS_VALUE_IN_RANGE_FOR_FREQUENCY);



						if(!status){
							if (LocalLogFailureWhichPhase.equals(FirstPhaseDisplayName)){
								FailureManager.AppendRefStdFeedBackReasonForFailure(ErrorCodeMapping.ERROR_CODE_214+":Expected:"+FailureManager.getRefStdSetFrequency()+" :Actual:"+FailureManager.getRefStdFeedBackFrequency()+ LocalLogFailureWhichPhase);

							}
							if (LocalLogFailureWhichPhase.equals(SecondPhaseDisplayName)){
								FailureManager.AppendRefStdFeedBackReasonForFailure(ErrorCodeMapping.ERROR_CODE_224+":Expected:"+FailureManager.getRefStdSetFrequency()+" :Actual:"+FailureManager.getRefStdFeedBackFrequency()+ LocalLogFailureWhichPhase);

							}
							if (LocalLogFailureWhichPhase.equals(ThirdPhaseDisplayName)){
								FailureManager.AppendRefStdFeedBackReasonForFailure(ErrorCodeMapping.ERROR_CODE_234+":Expected:"+FailureManager.getRefStdSetFrequency()+" :Actual:"+FailureManager.getRefStdFeedBackFrequency()+ LocalLogFailureWhichPhase);

							}
							if (LocalLogFailureWhichPhase.equals("RRR")){
								FailureManager.AppendRefStdFeedBackReasonForFailure(ErrorCodeMapping.ERROR_CODE_2041+":Expected:"+FailureManager.getRefStdSetFrequency()+" :Actual:"+FailureManager.getRefStdFeedBackFrequency()+ LocalLogFailureWhichPhase);

							}
						}
					}
				}
			}
			else{
				phaseR_freq = Float.parseFloat(getFeedbackR_Frequency());
				float phaseR_given_freq = DeviceDataManagerController.get_PwrSrc_Freq();
				LocalLogFailureWhichPhase = "RYB";
				status = IsValueInRange(phaseR_given_freq, phaseR_freq, ConstantAppConfig.FREQUENCY_ACCEPTED_PERCENTAGE,ConstantApp.IS_VALUE_IN_RANGE_FOR_FREQUENCY);
				if(!status){
					if (LocalLogFailureWhichPhase.equals("RYB")){
						FailureManager.AppendRefStdFeedBackReasonForFailure(ErrorCodeMapping.ERROR_CODE_2040+":Expected:"+FailureManager.getRefStdSetFrequency()+" :Actual:"+FailureManager.getRefStdFeedBackFrequency()+ LocalLogFailureWhichPhase);

					}
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("CheckPhaseRFrequencyReading :Exception:"+ e.getMessage());
			ApplicationLauncher.logger.info("CheckPhaseRFrequencyReading :getFeedbackR_Frequency():"+ getFeedbackB_Frequency());

			float phaseR_freq = Float.parseFloat(getFeedbackR_Frequency());
			float phaseR_given_freq = DeviceDataManagerController.get_PwrSrc_Freq();
			LocalLogFailureWhichPhase = "RYB";
			status = IsValueInRange(phaseR_given_freq, phaseR_freq, ConstantAppConfig.FREQUENCY_ACCEPTED_PERCENTAGE,ConstantApp.IS_VALUE_IN_RANGE_FOR_FREQUENCY);
			if(!status){
				if (LocalLogFailureWhichPhase.equals("RYB")){
					FailureManager.AppendRefStdFeedBackReasonForFailure(ErrorCodeMapping.ERROR_CODE_2042+":Expected:"+FailureManager.getRefStdSetFrequency()+" :Actual:"+FailureManager.getRefStdFeedBackFrequency()+ LocalLogFailureWhichPhase);

				}
			}
			return status;
		}
		return status;
	}

	public boolean CheckPhaseYVoltReading(){
		boolean status = false;
		try{
			float phaseY_volt =  Float.parseFloat(getFeedbackY_phaseVolt());
			float phaseY_given_volt = Float.parseFloat(DeviceDataManagerController.getY_PhaseOutputVoltage());

			//if(getCurrentTestType().equals("InfluenceHarmonic")){
			if(getCurrentTestType().equals(TestProfileType.InfluenceHarmonic.toString())){
				status = IsValueInRange(phaseY_given_volt, phaseY_volt, ConstantAppConfig.HAR_VOLT_ACCEPTED_PERCENTAGE,ConstantApp.IS_VALUE_IN_RANGE_FOR_VOLTAGE);
			}
			else{
				status = IsValueInRange(phaseY_given_volt, phaseY_volt, ConstantAppConfig.VOLT_ACCEPTED_PERCENTAGE,ConstantApp.IS_VALUE_IN_RANGE_FOR_VOLTAGE);
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("CheckPhaseYVoltReading :Exception:"+ e.getMessage());
		}
		return status;
	}

	public boolean CheckPhaseYCurrentReading(){
		boolean status = false;
		try{
			float phaseY_current = Float.parseFloat(getFeedbackY_phaseCurrent());
			float phaseY_given_current = Float.parseFloat(DeviceDataManagerController.getY_PhaseOutputCurrent());
			//if(getCurrentTestType().equals("InfluenceHarmonic")){
			if(getCurrentTestType().equals(TestProfileType.InfluenceHarmonic.toString())){
				status = IsValueInRange(phaseY_given_current, phaseY_current, ConstantAppConfig.HAR_CURRENT_ACCEPTED_PERCENTAGE,ConstantApp.IS_VALUE_IN_RANGE_FOR_CURRENT);
			}
			else{
				status = IsValueInRange(phaseY_given_current, phaseY_current, ConstantAppConfig.CURRENT_ACCEPTED_PERCENTAGE,ConstantApp.IS_VALUE_IN_RANGE_FOR_CURRENT);
			}

		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("CheckPhaseYCurrentReading :Exception:"+ e.getMessage());
		}
		return status;
	}

	public boolean CheckPhaseYDegreeReading(){
		boolean status = false;
		try{
			if((Float.parseFloat(DeviceDataManagerController.getY_PhaseOutputCurrent()) != 0f) &&
					(Float.parseFloat(DeviceDataManagerController.getY_PhaseOutputVoltage()) != 0f)){
				float phaseY_degree = Float.parseFloat(getFeedbackY_phaseDegree());
				float phaseY_given_degree = DeviceDataManagerController.get_PwrSrcY_PhaseDegreePhase();
				status = IsDegreeInRange(phaseY_given_degree, phaseY_degree, ConstantAppConfig.DEGREE_ACCEPTED_PERCENTAGE);
			}
			else{
				status = true;
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("CheckPhaseYDegreeReading :Exception:"+ e.getMessage());
		}
		return status;
	}



	public boolean CheckPhaseBVoltReading(){
		boolean status = false;
		try{
			float phaseB_volt = Float.parseFloat(getFeedbackB_phaseVolt());
			float phaseB_given_volt = Float.parseFloat(DeviceDataManagerController.getB_PhaseOutputVoltage());
			//if(getCurrentTestType().equals("InfluenceHarmonic")){
			if(getCurrentTestType().equals(TestProfileType.InfluenceHarmonic.toString())){
				status = IsValueInRange(phaseB_given_volt, phaseB_volt, ConstantAppConfig.HAR_VOLT_ACCEPTED_PERCENTAGE,ConstantApp.IS_VALUE_IN_RANGE_FOR_VOLTAGE);
			}
			else{
				status = IsValueInRange(phaseB_given_volt, phaseB_volt, ConstantAppConfig.VOLT_ACCEPTED_PERCENTAGE,ConstantApp.IS_VALUE_IN_RANGE_FOR_VOLTAGE);
			}

		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("CheckPhaseBVoltReading :Exception:"+ e.getMessage());
		}
		return status;
	}

	public boolean CheckPhaseBCurrentReading(){
		boolean status = false;
		try{
			float phaseB_current = Float.parseFloat(getFeedbackB_phaseCurrent());
			float phaseB_given_current = Float.parseFloat(DeviceDataManagerController.getB_PhaseOutputCurrent());

			//if(getCurrentTestType().equals("InfluenceHarmonic")){
			if(getCurrentTestType().equals(TestProfileType.InfluenceHarmonic.toString())){
				status = IsValueInRange(phaseB_given_current, phaseB_current, ConstantAppConfig.HAR_CURRENT_ACCEPTED_PERCENTAGE,ConstantApp.IS_VALUE_IN_RANGE_FOR_CURRENT);
			}
			else{
				status = IsValueInRange(phaseB_given_current, phaseB_current, ConstantAppConfig.CURRENT_ACCEPTED_PERCENTAGE,ConstantApp.IS_VALUE_IN_RANGE_FOR_CURRENT);
			}

		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("CheckPhaseBCurrentReading :Exception:"+ e.getMessage());
		}
		return status;
	}

	public boolean CheckPhaseBDegreeReading(){
		boolean status = false;
		try{
			if((Float.parseFloat(DeviceDataManagerController.getB_PhaseOutputCurrent()) != 0f)&&
					(Float.parseFloat(DeviceDataManagerController.getB_PhaseOutputVoltage()) != 0f)){
				float phaseB_degree = Float.parseFloat(getFeedbackB_phaseDegree());
				float phaseB_given_degree = DeviceDataManagerController.get_PwrSrcB_PhaseDegreePhase();
				status = IsDegreeInRange(phaseB_given_degree, phaseB_degree, ConstantAppConfig.DEGREE_ACCEPTED_PERCENTAGE);
			}
			else{
				status = true;
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("CheckPhaseBDegreeReading :Exception:"+ e.getMessage());
		}

		return status;
	}



	public boolean IsValueInRange(float input_value, float feedback_value, float acc_percent,String ValidationParameter){
		boolean status = false; 
		float ZeroAcceptedValue = ConstantAppConfig.VOLT_ZERO_ACCEPTED_VALUE;
		ApplicationLauncher.logger.info("IsValueInRange: input_value: " + input_value);
		ApplicationLauncher.logger.info("IsValueInRange: feedback_value: " + feedback_value);
		if (ValidationParameter.equals(ConstantApp.IS_VALUE_IN_RANGE_FOR_VOLTAGE)){
			FailureManager.setRefStdSetVolt(Float.toString(input_value));			
			FailureManager.setRefStdFeedBackVolt(Float.toString(feedback_value));
		} else if (ValidationParameter.equals(ConstantApp.IS_VALUE_IN_RANGE_FOR_CURRENT)){
			FailureManager.setRefStdSetCurrent(Float.toString(input_value));			
			FailureManager.setRefStdFeedBackCurrent(Float.toString(feedback_value));
			ZeroAcceptedValue = ConstantAppConfig.CURRENT_ZERO_ACCEPTED_VALUE;
		} else if (ValidationParameter.equals(ConstantApp.IS_VALUE_IN_RANGE_FOR_FREQUENCY)){
			FailureManager.setRefStdSetFrequency(Float.toString(input_value));			
			FailureManager.setRefStdFeedBackFrequency(Float.toString(feedback_value));
		}
		float expected_pos_value = 0;
		float expected_neg_value = 0;
		if(!(input_value == 0)){
			expected_pos_value = input_value *((100 + acc_percent)/100);
			expected_neg_value = input_value * ((100 - acc_percent)/100);
		}
		else{
			expected_pos_value = ZeroAcceptedValue;
			expected_neg_value = -(ZeroAcceptedValue);
		}
		if((feedback_value <= expected_pos_value)&&(feedback_value >= expected_neg_value)){
			status = true;
		}
		else{
			status = false;
		}
		return status;
	}


	public boolean IsDegreeInRange(float targetValue, float feedback_value, float acc_percent){
		boolean status = false; 
		if(ProCalCustomerConfiguration.ADYA_HYBRID_3NO_3PHASE_6NO_1PHASE_POSITION_2024){
			ApplicationLauncher.logger.info("IsDegreeInRange: Skip Entry: ");
			return true;
		}

		if(ConstantAppConfig.REF_STD_PHASE_180_FLAG){
			feedback_value = 180 - feedback_value;
		}
		else{
			if((ProcalFeatureEnable.SANDS_REFSTD_CONNECTED) || (ProcalFeatureEnable.KRE_REFSTD_CONNECTED || (ProcalFeatureEnable.BOFA_REFSTD_CONNECTED))){
				if(feedback_value> 180.0f){
					//feedback_value = 360.0f-feedback_value;
					feedback_value = feedback_value -360.0f;
				}
			}else{
				feedback_value = feedback_value *(-1);
			}
			if(ProcalFeatureEnable.KRE_REFSTD_CONNECTED){

			}
		}

		// Ref Std Device gives -60 degree as reading  when 60 degree is set...
		// After reset, Ref Std Device gives 120 degree as reading  when 60 degree is set...

		FailureManager.setRefStdSetDegree(Float.toString(targetValue));			
		FailureManager.setRefStdFeedBackDegree(Float.toString(feedback_value));

		ApplicationLauncher.logger.info("IsDegreeInRange: targetValue: " + targetValue);
		ApplicationLauncher.logger.info("IsDegreeInRange: feedback_value: " + feedback_value);

		float expected_pos_value = 0;
		float expected_neg_value = 0;

		if(!(targetValue == 0) && !(targetValue == -180.0) ){
			expected_pos_value = targetValue *((100 + acc_percent)/100);
			expected_neg_value = targetValue * ((100 - acc_percent)/100);
		}else if(targetValue == -180.0) {
			//ApplicationLauncher.logger.debug("IsValueInRange: -180 degree: Entry ");
			expected_pos_value = ConstantAppConfig.DEGREE_180_ACCEPTED_VALUE;
			expected_neg_value = -(ConstantAppConfig.DEGREE_180_ACCEPTED_VALUE);
		} else if(targetValue == 180.0) {
			//ApplicationLauncher.logger.debug("IsValueInRange: 180 degree: Entry ");
			expected_pos_value = ConstantAppConfig.DEGREE_180_ACCEPTED_VALUE;
			expected_neg_value = -(ConstantAppConfig.DEGREE_180_ACCEPTED_VALUE);
		} else{
			//ApplicationLauncher.logger.debug("IsValueInRange: zero degree: Entry ");
			expected_pos_value = ConstantAppConfig.DEGREE_ZERO_ACCEPTED_VALUE;
			expected_neg_value = -(ConstantAppConfig.DEGREE_ZERO_ACCEPTED_VALUE);
		}
		ApplicationLauncher.logger.debug("IsValueInRange: expected_pos_value0: "+expected_pos_value);
		ApplicationLauncher.logger.debug("IsValueInRange: expected_neg_value0: "+expected_neg_value);
		if(targetValue < 0){
			//ApplicationLauncher.logger.debug("IsValueInRange: feedback_value: "+feedback_value);
			//ApplicationLauncher.logger.debug("IsValueInRange: expected_pos_value: "+expected_pos_value);
			//ApplicationLauncher.logger.debug("IsValueInRange: expected_neg_value: "+expected_neg_value);
			if((feedback_value > expected_pos_value)&&(feedback_value < expected_neg_value)){
				status = true;
				return status;
			}
			else{
				status = false;
			}
			if(targetValue == -180.0) {
				if(feedback_value>0){
					if((feedback_value > expected_pos_value)&&(feedback_value < 180.0)){
						status = true;
						return status;
						//ApplicationLauncher.logger.debug("IsValueInRange: 180 Entry1");
					}
				}else{
					if((feedback_value < expected_neg_value)&&(feedback_value > -180.0)){
						status = true;
						return status;
						//ApplicationLauncher.logger.debug("IsValueInRange: 180 Entry2");
					}
				}
			}

		}
		else{
			if((feedback_value < expected_pos_value)&&(feedback_value > expected_neg_value)){
				status = true;
				return status;
			}
			else{
				status = false;
			}
			if(!status && (targetValue == 180.0)) {
				//ApplicationLauncher.logger.info("IsValueInRange: feedback_value: "+feedback_value);
				ApplicationLauncher.logger.debug("IsDegreeInRange1: expected_pos_value: "+expected_pos_value);
				ApplicationLauncher.logger.debug("IsDegreeInRange1: expected_neg_value: "+expected_neg_value);
				if(feedback_value>0){
					if((feedback_value > expected_pos_value)&&(feedback_value < 180.0)){
						status = true;
						return status;
						//ApplicationLauncher.logger.debug("IsValueInRange: 180 Entry1");
					}
				}else{
					if((feedback_value < expected_neg_value)&&(feedback_value > -180.0)){
						status = true;
						return status;
						//ApplicationLauncher.logger.debug("IsValueInRange: 180 Entry2");
					}
				}
			}
		}

		// Logic for expected [-240] failed for input [120] 
		//ApplicationLauncher.logger.info("IsDegreeInRange :input_value1: "+input_value);
		if (!status && (targetValue < 0)){
			targetValue = 360+targetValue;
			//ApplicationLauncher.logger.debug("IsDegreeInRange :input_value2: "+input_value);
			//ApplicationLauncher.logger.debug("IsDegreeInRange : feedback_value: "+feedback_value);
			expected_pos_value = targetValue *((100 + acc_percent)/100);
			expected_neg_value = targetValue * ((100 - acc_percent)/100);
			ApplicationLauncher.logger.debug("IsDegreeInRange2 : expected_pos_value: "+expected_pos_value);
			ApplicationLauncher.logger.debug("IsDegreeInRange2 : expected_neg_value: "+expected_neg_value);
			if((feedback_value < expected_pos_value)&&(feedback_value > expected_neg_value)){
				status = true;
				return status;
			}
		}

		return status;
	}

	/*	public boolean IsDegreeInRange(float input_value, float feedback_value, float acc_percent){
		boolean status = false; 

		String degreephasevalue = "";
		if(ConstantConfig.REF_STD_PHASE_180_FLAG){
			feedback_value = 180 - feedback_value;
		}
		else{
			feedback_value = feedback_value *(-1);
		}

		// Ref Std Device gives -60 degree as reading  when 60 degree is set...
		// After reset, Ref Std Device gives 120 degree as reading  when 60 degree is set...

		FailureManager.setRefStdSetDegree(Float.toString(input_value));			
		FailureManager.setRefStdFeedBackDegree(Float.toString(feedback_value));

		ApplicationLauncher.logger.info("IsDegreeInRange: input_value: " + input_value);
		ApplicationLauncher.logger.info("IsDegreeInRange: feedback_value: " + feedback_value);

		float expected_pos_value = 0;
		float expected_neg_value = 0;

		if(!(input_value == 0) && !(input_value == -180.0) ){
			expected_pos_value = input_value *((100 + acc_percent)/100);
			expected_neg_value = input_value * ((100 - acc_percent)/100);
		}else if(input_value == -180.0) {
			//ApplicationLauncher.logger.debug("IsValueInRange: -180 degree: Entry ");
			expected_pos_value = ConstantConfig.DEGREE_180_ACCEPTED_VALUE;
			expected_neg_value = -(ConstantConfig.DEGREE_180_ACCEPTED_VALUE);
		}
		else{
			//ApplicationLauncher.logger.debug("IsValueInRange: zero degree: Entry ");
			expected_pos_value = ConstantConfig.DEGREE_ZERO_ACCEPTED_VALUE;
			expected_neg_value = -(ConstantConfig.DEGREE_ZERO_ACCEPTED_VALUE);
		}

		if(input_value < 0){
			//ApplicationLauncher.logger.debug("IsValueInRange: feedback_value: "+feedback_value);
			//ApplicationLauncher.logger.debug("IsValueInRange: expected_pos_value: "+expected_pos_value);
			//ApplicationLauncher.logger.debug("IsValueInRange: expected_neg_value: "+expected_neg_value);
			if((feedback_value > expected_pos_value)&&(feedback_value < expected_neg_value)){
				status = true;
				return status;
			}
			else{
				status = false;
			}
			if(input_value == -180.0) {
				if(feedback_value>0){
					if((feedback_value > expected_pos_value)&&(feedback_value < 180.0)){
						status = true;
						return status;
						//ApplicationLauncher.logger.debug("IsValueInRange: 180 Entry1");
					}
				}else{
					if((feedback_value < expected_neg_value)&&(feedback_value > -180.0)){
						status = true;
						return status;
						//ApplicationLauncher.logger.debug("IsValueInRange: 180 Entry2");
					}
				}
			}
		}
		else{
			if((feedback_value < expected_pos_value)&&(feedback_value > expected_neg_value)){
				status = true;
				return status;
			}
			else{
				status = false;
			}
		}

		// Logic for expected [-240] failed for input [120] 
		//ApplicationLauncher.logger.info("IsDegreeInRange :input_value1: "+input_value);
		if (!status && input_value < 0){
			input_value = 360+input_value;
			//ApplicationLauncher.logger.debug("IsDegreeInRange :input_value2: "+input_value);
			//ApplicationLauncher.logger.debug("IsDegreeInRange : feedback_value: "+feedback_value);
			expected_pos_value = input_value *((100 + acc_percent)/100);
			expected_neg_value = input_value * ((100 - acc_percent)/100);
			ApplicationLauncher.logger.debug("IsDegreeInRange : expected_pos_value: "+expected_pos_value);
			ApplicationLauncher.logger.debug("IsDegreeInRange : expected_neg_value: "+expected_neg_value);
			if((feedback_value < expected_pos_value)&&(feedback_value > expected_neg_value)){
				status = true;
				return status;
			}
		}

		return status;
	}
	 */
	public void STAExecuteStart(){
		ApplicationLauncher.logger.info("STA Test Started"  );
		FailureManager.ResetPowerSrcReasonForFailure();
		if(ProcalFeatureEnable.LSCS_LDU_CONNECTED) {
			lscsClearLDU_LastStoredResults();
			lscsClearLduSecondaryDisplay();
			lscsLDU_ResetError();
		}else if(ProcalFeatureEnable.BOFA_LDU_CONNECTED){
			Data_LduBofa.bofaSendLduExitDialTest();//bofaSendLduResetErrorAll();
		}
		if(getCurrentTestPoint_Index() == 0){ // when executed after Constant test voltage not powered up
			ZeroCurrentPowerOn();
			//Sleep(5000);
			if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
				WaitForPowerSrcTurnOn();
			}else if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){ 
				waitBufferTimeForBofaPowerSourceOn();
			}
			if(SerialDataManager.getPowerSrcOnFlag()){
				if(ProcalFeatureEnable.STA_KWH_READING_PROMPT_ENABLED){
					getInitMeterValuesFromUI();// Get Initial value
					waitForInitMeterReading();//User enter readings
				}
			}
		}
		else{
			if(ProcalFeatureEnable.STA_KWH_READING_PROMPT_ENABLED){
				getInitMeterValuesFromUI();// Get Initial value
				waitForInitMeterReading();//User enter readings
			}
		}



		STA_RatedCurrentPowerOn();
		//ApplicationLauncher.logger.info("STA Test Started"  );
		int STADuration = DisplayDataObj.get_STADuration();


		DisplayDataObj.setVoltageResetRequired(false);
		if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
			WaitForPowerSrcTurnOn();
		}
		////FailureManager.ResetPowerSrcReasonForFailure();
		//DisplayPwrSrc_TurnOn();
		//WaitForPowerSrcTurnOn();
		//LDU_PreRequisiteForSTATest();
		if(FirstLevelValidation()){
			if((!ProcalFeatureEnable.REFSTD_CONNECTED_NONE)){
				if(SecondLevelValidation()){
	
					if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_ENABLED){
						refStdFeedBackControl();
						setRefStdFeedBackControlSuspended(false);
					}
	
					if(ProcalFeatureEnable.CCUBE_LDU_CONNECTED) {
						LDU_PreRequisiteForSTATestV2();
						//DisplayLDU_ReadSTATimerInit();
						DisplayLDU_ReadSTATimerInitV2();
					}else if(ProcalFeatureEnable.LSCS_LDU_CONNECTED) {
						//lscsClearLDU_LastStoredResults();
						//lscsClearLduSecondaryDisplay();
						//lscsLDU_ResetError();
						lscsLDU_PreRequisiteForSTA_Test();	
						/*					int lduTimeDuration = DisplayDataObj.get_STADuration();
						while ( (!getUserAbortedFlag()) && (lduTimeDuration!=0) ){
							Sleep(1000);
							lduTimeDuration--;
							ApplicationHomeController.update_left_status("STA: " + lduTimeDuration + " Sec",ConstantApp.LEFT_STATUS_DEBUG);
	
						}
						lscsLDU_StopSTA_Test();
						if(!getUserAbortedFlag()){
							displayLscsLDU_ReadSTA_TimerInit();
						}*/
	
					}else if(ProcalFeatureEnable.BOFA_LDU_CONNECTED) {
	
						Data_LduBofa.bofaLduSendStartSTA();	// send the ldu starting current command to bofa ldu here
	
					}
					setExecuteTimeCounter(STADuration+STA_BufferTimeToReadLDU_DataInSec);
					ExecuteTimerDisplay();
					UI_TableRefreshTrigger(STADuration);
					if(ProcalFeatureEnable.LSCS_LDU_CONNECTED) {
	
						int lduTimeDuration = STADuration;//DisplayDataObj.get_STADuration();
						while ( (!getUserAbortedFlag()) && (lduTimeDuration!=0) ){
							Sleep(1000);
							lduTimeDuration--;
							ApplicationHomeController.update_left_status("STA: " + lduTimeDuration + " Sec",ConstantApp.LEFT_STATUS_DEBUG);
	
						}
						lscsLDU_StopSTA_Test();
						if(!getUserAbortedFlag()){
							displayLscsLDU_ReadSTA_TimerInit();
						}
	
					}else if(ProcalFeatureEnable.BOFA_LDU_CONNECTED) {
	
	
						int lduTimeDuration = STADuration;//DisplayDataObj.get_STADuration();
						while ( (!getUserAbortedFlag()) && (lduTimeDuration!=0) ){
							Sleep(1000);
							lduTimeDuration--;
							ApplicationHomeController.update_left_status("STA: " + lduTimeDuration + " Sec",ConstantApp.LEFT_STATUS_DEBUG);
	
						}
						//lscsLDU_StopSTA_Test();
						boolean status = false;
						status = Data_PowerSourceBofa.sendCurrentStoppingCommand(); // send the stop current command to bofa source here
						if (status) {
							if(!getUserAbortedFlag()){
	
								//displayLscsLDU_ReadSTA_TimerInit(); //updated by gopi - for the refresh to read the device before exiting the LDUreadbuffertime 
								Data_LduBofa lduBofaObj = new Data_LduBofa();
								lduBofaObj.bofaLduTriggerReadSTAPulseCount(); // trigger the command to read the pulse count from LDU
	
	
							}
						}
						else{
						}
					}
					else{
						ApplicationLauncher.logger.debug("STAExecuteStart : Skipped1 ");
						SkipCurrentTestPointGUI_Update();
					}
				} else{
					ApplicationLauncher.logger.debug("STAExecuteStart : Skipped2 ");
					SkipCurrentTestPointGUI_Update();
				}
			}else{
				ApplicationLauncher.logger.debug("STAExecuteStart : REFSTD_CONNECTED_NONE" );
				if(ProcalFeatureEnable.LSCS_LDU_CONNECTED) {

					lscsLDU_PreRequisiteForSTA_Test();	
					

				}
				setExecuteTimeCounter(STADuration+STA_BufferTimeToReadLDU_DataInSec);
				
				ExecuteTimerDisplay();
				UI_TableRefreshTrigger(STADuration);
				if(ProcalFeatureEnable.LSCS_LDU_CONNECTED) {
					
					int lduTimeDuration = STADuration;//DisplayDataObj.get_STADuration();
					while ( (!getUserAbortedFlag()) && (lduTimeDuration!=0) ){
						Sleep(1000);
						lduTimeDuration--;
						ApplicationHomeController.update_left_status("STA: " + lduTimeDuration + " Sec",ConstantApp.LEFT_STATUS_DEBUG);

					}
					lscsLDU_StopSTA_Test();
					if(!getUserAbortedFlag()){
						displayLscsLDU_ReadSTA_TimerInit();
					}

				}
				if(ProcalFeatureEnable.PROPOWER_SRC_FEEDBACK_DISPLAY ){

					ApplicationLauncher.logger.debug("STAExecuteStart : ProPower Source Feedback display" );
					manipulateGainOffsetValueForFeedBackProcess();
					SerialDM_Obj.lscsPowerSourceReadFeedBackTimerInit();
				}

				if(ProcalFeatureEnable.PROPOWER_SRC_ONLY ){

					//int noOfTestPointCompleted = LiveTableDataManager.getCountOfNoOfTestPointCompleted();
					int totalNoOfTestPointIndex  = getCurrentProjectTestPointList().length()-1;
					ApplicationLauncher.logger.debug("STAExecuteStart : totalNoOfTestPointIndex: " + totalNoOfTestPointIndex );
					ApplicationLauncher.logger.debug("STAExecuteStart : CurrentTestPoint_Index: " + CurrentTestPoint_Index );

					EnableStopButton();
					if(CurrentTestPoint_Index < totalNoOfTestPointIndex){
						enableBtnLoadNext();
					}
				}
			}



		}
		
	}

		public void SkipCurrentTestPointGUI_Update(){
			ApplicationLauncher.logger.info(getCurrentTestPointName() +":"+ getCurrentTestPoint_Index()+": Skiping test");
			setExecuteTimeCounter(Skip_TP_Time_InSec);
			ExecuteTimerDisplay();
			UI_TableRefreshTrigger(Skip_TP_Time_InSec);
			setUI_TableRefreshFlag(false);

		}

		public void WarmupExecuteStart(){

			ApplicationLauncher.logger.info("Warmup Test Started"  );
			Integer WarmupDuration  = DisplayDataObj.get_WarmupDuration();

			DisplayDataObj.setVoltageResetRequired(false);
			FailureManager.ResetPowerSrcReasonForFailure();
			if ( (getSelfHeatingTestFlag()) && (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED) ) {
				setLscsZeroCurrentPowerTurnOn(true);

			}
			DisplayPwrSrc_TurnOn();
			if(ProcalFeatureEnable.LSCS_LDU_CONNECTED) {
				lscsClearLDU_LastStoredResults();
				lscsClearLduSecondaryDisplay();
				lscsLDU_ResetError();
			}else if(ProcalFeatureEnable.BOFA_LDU_CONNECTED){
				Data_LduBofa.bofaSendLduExitDialTest();//bofaSendLduResetErrorAll();
			}
			if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
				WaitForPowerSrcTurnOn();
			}else if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){ 
				waitBufferTimeForBofaPowerSourceOn();
			}

			ApplicationLauncher.logger.info("getCurrentTestType: " + getCurrentTestType());
			ApplicationLauncher.logger.info("SelfHeating : " + TestProfileType.SelfHeating.toString());
			/*if(!getCurrentTestType().equals(TestProfileType.SelfHeating.toString())){
			LDU_PreRequisiteForReadError();
		}*/
			ApplicationLauncher.logger.info("WarmupExecuteStart: WarmupDuration: " + WarmupDuration);

			if(FirstLevelValidation()){
				if(SecondLevelValidation()){
					/*if(!getCurrentTestType().equals(TestProfileType.SelfHeating.toString())){
					LDU_PreRequisiteForReadErrorV2();
				}*/
					if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_ENABLED){
						refStdFeedBackControl();
						setRefStdFeedBackControlSuspended(false);
					}

					if(ProcalFeatureEnable.CCUBE_LDU_CONNECTED) {
						if(!getCurrentTestType().equals(TestProfileType.SelfHeating.toString())){
							LDU_PreRequisiteForReadErrorV2();
							DisplayLDU_ReadErrorTimerInitV2();
						}
					}else if(ProcalFeatureEnable.LSCS_LDU_CONNECTED){
						//lscsClearLDU_LastStoredResults();
						//lscsClearLduSecondaryDisplay();
						if(!getCurrentTestType().equals(TestProfileType.SelfHeating.toString())){
							//lscsClearLDU_LastStoredResults();
							//lscsClearLduSecondaryDisplay();
							lscsLDU_PreRequisiteForReadError();
							lscsDisplayLDU_ReadErrorTimerInit();
						}

					}else if(ProcalFeatureEnable.BOFA_LDU_CONNECTED){
						Data_RefStdBofa.setRsmFreqReadingRequired(false);
						if(Float.parseFloat(DeviceDataManagerController.getR_PhaseOutputCurrent())> 0.0f ){
							
							Data_RefStdBofa.sendReadConstOfLiveRefStdCmd();  
						}
						Data_LduBofa.bofaLDU_PreRequisiteForReadError();
						Data_LduBofa dataLduBofa = new Data_LduBofa();
						dataLduBofa.bofaDisplayLDU_ReadErrorTimerInit();

					}
					ApplicationLauncher.logger.info("WarmupExecuteStart: Executing test");
					setExecuteTimeCounter(WarmupDuration);
					ExecuteTimerDisplay();
					UI_TableRefreshTrigger(WarmupDuration);
				}
				else{
					SkipCurrentTestPointGUI_Update();

				}
			} else{
				SkipCurrentTestPointGUI_Update();
			}


		}

		public void lscsClearLDU_LastStoredResults() {
			ApplicationLauncher.logger.debug("lscsClearLDU_LastStoredResults: Entry"  );
			SerialDM_Obj.lscsClearLDU_Result();

		}



		public void lscsClearLduSecondaryDisplay() {
			ApplicationLauncher.logger.debug("lscsClearLduSecondaryDisplay: Entry"  );
			SecondaryLduDisplayController.resetLduAllSecondarydisplay();

		}

		public void lscsClearLduSecondaryDisplayTestPoint() {
			ApplicationLauncher.logger.debug("lscsClearLduSecondaryDisplayTestPoint: Entry"  );
			SecondaryLduDisplayController.clearLduSecondarydisplayTestPoint();

		}

		public void VoltageExecuteStart(){
			ApplicationLauncher.logger.info("Voltage Test Started"  );
			int timeduration = DeviceDataManagerController.getInfTimeDuration();
			ApplicationLauncher.logger.info("VoltageExecuteStart :timeduration: " + timeduration);

			DisplayDataObj.setVoltageResetRequired(true);
			FailureManager.ResetPowerSrcReasonForFailure();
			if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
				DisplayDataObj.setVoltageResetRequired(false);
			}
			DisplayPwrSrc_TurnOn();
			if(ProcalFeatureEnable.LSCS_LDU_CONNECTED) {
				if(!ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED) {
					lscsClearLDU_LastStoredResults();
					lscsClearLduSecondaryDisplay();
					lscsLDU_ResetError();
				}
			}else if(ProcalFeatureEnable.BOFA_LDU_CONNECTED){
				Data_LduBofa.bofaSendLduExitDialTest();//bofaSendLduResetErrorAll();
			}
			//WaitForPowerSrcTurnOn();
			//LDU_PreRequisiteForReadError();
			if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
				WaitForPowerSrcTurnOn();
			}else if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){ 
				waitBufferTimeForBofaPowerSourceOn();
			}
			if(!ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED) {
				if(FirstLevelValidation()){
					if(SecondLevelValidation()){

						if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_ENABLED){
							refStdFeedBackControl();
							setRefStdFeedBackControlSuspended(false);
						}

						if(ProcalFeatureEnable.CCUBE_LDU_CONNECTED) {
							LDU_PreRequisiteForReadErrorV2();
							DisplayLDU_ReadErrorTimerInitV2();
						}else if(ProcalFeatureEnable.LSCS_LDU_CONNECTED){
							//lscsClearLDU_LastStoredResults();
							//lscsClearLduSecondaryDisplay();
							lscsLDU_PreRequisiteForReadError();
							lscsDisplayLDU_ReadErrorTimerInit();

						}else if(ProcalFeatureEnable.BOFA_LDU_CONNECTED){
							Data_RefStdBofa.setRsmFreqReadingRequired(false);
							if(Float.parseFloat(DeviceDataManagerController.getR_PhaseOutputCurrent())> 0.0f ){
								
								Data_RefStdBofa.sendReadConstOfLiveRefStdCmd();  
							}
							Data_LduBofa.bofaLDU_PreRequisiteForReadError();
							Data_LduBofa dataLduBofa = new Data_LduBofa();
							dataLduBofa.bofaDisplayLDU_ReadErrorTimerInit();

						}
						setExecuteTimeCounter(timeduration +BufferTimeToReadLDU_DataInSec);
						ExecuteTimerDisplay();
						UI_TableRefreshTrigger(timeduration);
					}
					else{
						SkipCurrentTestPointGUI_Update();
					}
				}
				else{
					SkipCurrentTestPointGUI_Update();
				}
			}else {
				setExecuteTimeCounter(timeduration +BufferTimeToReadLDU_DataInSec);
				ExecuteTimerDisplay();
				UI_TableRefreshTrigger(timeduration);
			}

		}

		public void VoltUnbalanceExecuteStart(){


			ApplicationLauncher.logger.info("VoltUnbalance Test Started"  );


			int timeduration = DeviceDataManagerController.getInfTimeDuration();

			DisplayDataObj.setVoltageResetRequired(true);
			//DisplayDataObj.setVoltageResetRequired(false);
			FailureManager.ResetPowerSrcReasonForFailure();
			DisplayPwrSrc_TurnOn();
			//WaitForPowerSrcTurnOn();
			//LDU_PreRequisiteForReadError();
			if(ProcalFeatureEnable.LSCS_LDU_CONNECTED) {
				lscsClearLDU_LastStoredResults();
				lscsClearLduSecondaryDisplay();
				lscsLDU_ResetError();
			}else if(ProcalFeatureEnable.BOFA_LDU_CONNECTED){
				Data_LduBofa.bofaSendLduExitDialTest();//bofaSendLduResetErrorAll();
			}
			if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
				WaitForPowerSrcTurnOn();
			}else if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){ 
				waitBufferTimeForBofaPowerSourceOn();
			}

			if(FirstLevelValidation()){
				if(SecondLevelValidation()){
					//LDU_PreRequisiteForReadErrorV2();
					//DisplayLDU_ReadErrorTimerInitV2();
					setExecuteStopStatus(false);
					if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_ENABLED){
						refStdFeedBackControl();
						setRefStdFeedBackControlSuspended(false);
					}

					if(ProcalFeatureEnable.CCUBE_LDU_CONNECTED) {
						LDU_PreRequisiteForReadErrorV2();
						DisplayLDU_ReadErrorTimerInitV2();
					}else if(ProcalFeatureEnable.LSCS_LDU_CONNECTED){
						//lscsClearLDU_LastStoredResults();
						//lscsClearLduSecondaryDisplay();
						lscsLDU_PreRequisiteForReadError();
						lscsDisplayLDU_ReadErrorTimerInit();

					}else if(ProcalFeatureEnable.BOFA_LDU_CONNECTED){
						Data_RefStdBofa.setRsmFreqReadingRequired(false);
						if(Float.parseFloat(DeviceDataManagerController.getR_PhaseOutputCurrent())> 0.0f ){
							
							Data_RefStdBofa.sendReadConstOfLiveRefStdCmd();  
						}
						Data_LduBofa.bofaLDU_PreRequisiteForReadError();
						Data_LduBofa dataLduBofa = new Data_LduBofa();
						dataLduBofa.bofaDisplayLDU_ReadErrorTimerInit();

					}

					setExecuteTimeCounter(timeduration+BufferTimeToReadLDU_DataInSec);
					ExecuteTimerDisplay();
					UI_TableRefreshTrigger(timeduration);
				}
				else{
					SkipCurrentTestPointGUI_Update();
				}
			}
			else{
				SkipCurrentTestPointGUI_Update();
			}


		}

		public void CutNuetralExecuteStart(){


			ApplicationLauncher.logger.info("CutNuetral Test Started"  );
			InformUserToCutNuetral();
			WaitForUserInput();


			if(DeviceDataManagerController.getcutnuetral_flag()){
				int timeduration = DeviceDataManagerController.getInfTimeDuration();

				DisplayDataObj.setVoltageResetRequired(false);
				FailureManager.ResetPowerSrcReasonForFailure();
				DisplayPwrSrc_TurnOn();
				//WaitForPowerSrcTurnOn();
				//LDU_PreRequisiteForReadError();


				if(FirstLevelValidation()){
					//LDU_PreRequisiteForReadErrorV2();
					if(SecondLevelValidation()){

						if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_ENABLED){
							refStdFeedBackControl();
							setRefStdFeedBackControlSuspended(false);
						}

						LDU_PreRequisiteForReadErrorV2();
						DisplayLDU_ReadErrorTimerInitV2();
						setExecuteTimeCounter(timeduration+BufferTimeToReadLDU_DataInSec);
						ExecuteTimerDisplay();
						UI_TableRefreshTrigger(timeduration);
					}
					else{
						SkipCurrentTestPointGUI_Update();
					}
				}
				else{
					SkipCurrentTestPointGUI_Update();
				}


			}
			else{
				updateAbortedInDisplay();
				setExecuteTimeCounter(Skip_TP_Time_InSec);
				ExecuteTimerDisplay();
				UI_TableRefreshTrigger(Skip_TP_Time_InSec);
			}
		}
		//
		public void HarmonicExecuteStart(){
			ApplicationLauncher.logger.info("Harmonic Test Started"  );
			//ApplicationLauncher.logger.info("Harmonic Test : LSCS_APP_CONTROL_MODE_ENABLED : "+ ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED );

			int timeduration = DeviceDataManagerController.getInfTimeDuration();

			DisplayDataObj.setVoltageResetRequired(true);
			FailureManager.ResetPowerSrcReasonForFailure();

			if(ProcalFeatureEnable.LSCS_LDU_CONNECTED) {
				if(!ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED) {
					lscsClearLDU_LastStoredResults();
					lscsClearLduSecondaryDisplay();
					lscsLDU_ResetError();
				}
			}else if(ProcalFeatureEnable.BOFA_LDU_CONNECTED){
				Data_LduBofa.bofaSendLduExitDialTest();//bofaSendLduResetErrorAll();
			}

			if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
				//Display_Har_PwrSrc_TurnOn(); 
				if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_FEATURE_ENABLED){
					ApplicationLauncher.logger.info("Harmonic Test : getOneTimeExecuted: " + getOneTimeExecuted()  );
					setLastExecutedTestPointContainedHarmonics_Slave(true);    // rename slave 
					//setLastExecutedTestPointContainedHarmonics_Master(true);
					if(getOneTimeExecuted()){
						//if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_DSP_SLAVE_SERIAL_CONNECTED){
						//	Display_Har_PwrSrc_TurnOn(); 
						//}else{ 
						lscsSendHarmonicsDataToSlave();
						//}
						boolean status = SerialDM_Obj.SetPowerSourceOff();
						if(!status){
							ApplicationLauncher.logger.info("Harmonic Test : getOneTimeExecuted:  retrying SetPowerSourceOff 2nd Time"  );
							status = SerialDM_Obj.SetPowerSourceOff();
						}
						DisplayPwrSrc_TurnOn();
						WaitForPowerSrcTurnOn();
					}else{		

						/*boolean status = SerialDM_Obj.SetPowerSourceOff();
					if(!status){
						ApplicationLauncher.logger.info("Harmonic Test : retrying SetPowerSourceOff 2nd Time"  );
						status = SerialDM_Obj.SetPowerSourceOff();
					}

					//if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_DSP_SLAVE_SERIAL_CONNECTED){

						//setFirstExecutionTestPointWithHarmonics(true);
						//DisplayPwrSrc_TurnOn();
						//WaitForPowerSrcTurnOn();
						//ApplicationLauncher.logger.info("Harmonic Test : trying for source off"  );
						//status = SerialDM_Obj.SetPowerSourceOff();
					//}
					if(!status){
						ApplicationLauncher.logger.info("Harmonic Test : retrying SetPowerSourceOff 3nd Time"  );
						status = SerialDM_Obj.SetPowerSourceOff();
					}
						 */


						//if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_DSP_SLAVE_SERIAL_CONNECTED){
						//	Display_Har_PwrSrc_TurnOn();
						//}else{
						if(!isSecondthExecutionTestPointWithBreakWithHarmonics()){
							setFirstExecutionTestPointWithHarmonics(true);
							DisplayPwrSrc_TurnOn();
							WaitForPowerSrcTurnOn();
							ApplicationLauncher.logger.info("Harmonic Test : 2ndTest1"  );
						}
						//setFirstExecutionTestPointWithHarmonics(true);
						//DisplayPwrSrc_TurnOn();
						//WaitForPowerSrcTurnOn();
						ApplicationLauncher.logger.info("Harmonic Test : 2ndTest2"  );
						lscsSendHarmonicsDataToSlave();
						//}
						//Sleep(5000);

						DisplayPwrSrc_TurnOn();
						WaitForPowerSrcTurnOn();

					}
				}else{
					ApplicationLauncher.logger.info("Harmonic Test : Non-LSCS"  );
					Display_Har_PwrSrc_TurnOn();
				}

				//Display_Har_PwrSrc_TurnOn();
				//SerialDM_Obj.SetPowerSourceOff();
				//DisplayPwrSrc_TurnOn();
				//Display_Har_PwrSrc_TurnOn(); 
			}else if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){ 
				DisplayPwrSrc_TurnOn();
				waitBufferTimeForBofaPowerSourceOn();

			}else{

				Display_Har_PwrSrc_TurnOn();
				DisplayPwrSrc_TurnOn();
			}


			//WaitForPowerSrcTurnOn();
			//LDU_PreRequisiteForReadError();


			if(FirstLevelValidation()){
				if((!ProcalFeatureEnable.REFSTD_CONNECTED_NONE)){
					if(SecondLevelValidation()){

						if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
							//if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_FEATURE_ENABLED){
							//	setLastExecutedTestPointContainedHarmonics_Master(true);
							//}
							//Display_Har_PwrSrc_TurnOn(); 
							/*					if(!getOneTimeExecuted()){
							Display_Har_PwrSrc_TurnOn(); 
						}*/
						}
						setExecuteStopStatus(false);
						if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_ENABLED){
							refStdFeedBackControl();
							setRefStdFeedBackControlSuspended(false);
						}

						if(ProcalFeatureEnable.CCUBE_LDU_CONNECTED) {
							LDU_PreRequisiteForReadErrorV2();
							DisplayLDU_ReadErrorTimerInitV2();
						}else if(ProcalFeatureEnable.LSCS_LDU_CONNECTED){
							lscsClearLDU_LastStoredResults();
							lscsClearLduSecondaryDisplay();
							lscsLDU_PreRequisiteForReadError();
							lscsDisplayLDU_ReadErrorTimerInit();

						}else if(ProcalFeatureEnable.BOFA_LDU_CONNECTED){

							Data_RefStdBofa.setRsmFreqReadingRequired(false);
							if(Float.parseFloat(DeviceDataManagerController.getR_PhaseOutputCurrent())> 0.0f ){
								
								Data_RefStdBofa.sendReadConstOfLiveRefStdCmd();  
							}
							Data_LduBofa.bofaLDU_PreRequisiteForReadError();
							Data_LduBofa dataLduBofa = new Data_LduBofa();
							dataLduBofa.bofaDisplayLDU_ReadErrorTimerInit();

						}
						setExecuteTimeCounter(timeduration+BufferTimeToReadLDU_DataInSec);
						ExecuteTimerDisplay();
						UI_TableRefreshTrigger(timeduration);
					}
					else{
						SkipCurrentTestPointGUI_Update();
					}
				}else{
					ApplicationLauncher.logger.debug("HarmonicTest : REFSTD_CONNECTED_NONE" );
					setExecuteTimeCounter(timeduration+BufferTimeToReadLDU_DataInSec);
					ExecuteTimerDisplay();
					UI_TableRefreshTrigger(timeduration);
					if(ProcalFeatureEnable.PROPOWER_SRC_FEEDBACK_DISPLAY ){

						ApplicationLauncher.logger.debug("HarmonicTest : ProPower Source Feedback display" );
						manipulateGainOffsetValueForFeedBackProcess();
						SerialDM_Obj.lscsPowerSourceReadFeedBackTimerInit();
					}

					if(ProcalFeatureEnable.PROPOWER_SRC_ONLY ){

						//int noOfTestPointCompleted = LiveTableDataManager.getCountOfNoOfTestPointCompleted();
						int totalNoOfTestPointIndex  = getCurrentProjectTestPointList().length()-1;
						ApplicationLauncher.logger.debug("HarmonicTest : totalNoOfTestPointIndex: " + totalNoOfTestPointIndex );
						ApplicationLauncher.logger.debug("HarmonicTest : CurrentTestPoint_Index: " + CurrentTestPoint_Index );

						EnableStopButton();
						if(CurrentTestPoint_Index < totalNoOfTestPointIndex){
							enableBtnLoadNext();
						}
					}
				}
			}
			else{
				SkipCurrentTestPointGUI_Update();
			}

		}

		public void FrequencyExecuteStart(){
			ApplicationLauncher.logger.info("Frequency Test Started"  );


			int timeduration = DeviceDataManagerController.getInfTimeDuration();

			DisplayDataObj.setVoltageResetRequired(true);
			FailureManager.ResetPowerSrcReasonForFailure();
			DisplayPwrSrc_TurnOn();
			if(ProcalFeatureEnable.LSCS_LDU_CONNECTED) {
				lscsClearLDU_LastStoredResults();
				lscsClearLduSecondaryDisplay();
				lscsLDU_ResetError();
			}else if(ProcalFeatureEnable.BOFA_LDU_CONNECTED){
				Data_LduBofa.bofaSendLduExitDialTest();//bofaSendLduResetErrorAll();
			}
			if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
				WaitForPowerSrcTurnOn();
			}else if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){ 
				waitBufferTimeForBofaPowerSourceOn();
			}
			//WaitForPowerSrcTurnOn();
			//LDU_PreRequisiteForReadError();

			if(!ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED) {
				if(FirstLevelValidation()){
					if((!ProcalFeatureEnable.REFSTD_CONNECTED_NONE)){
						if(SecondLevelValidation()){
							setExecuteStopStatus(false);
							if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_ENABLED){
								refStdFeedBackControl();
								setRefStdFeedBackControlSuspended(false);
							}

							if(ProcalFeatureEnable.CCUBE_LDU_CONNECTED) {
								LDU_PreRequisiteForReadErrorV2();
								DisplayLDU_ReadErrorTimerInitV2();
							}else if(ProcalFeatureEnable.LSCS_LDU_CONNECTED){
								//lscsClearLDU_LastStoredResults();
								//lscsClearLduSecondaryDisplay();
								lscsLDU_PreRequisiteForReadError();
								lscsDisplayLDU_ReadErrorTimerInit();

							}else if(ProcalFeatureEnable.BOFA_LDU_CONNECTED){
								Data_RefStdBofa.setRsmFreqReadingRequired(false);
								if(Float.parseFloat(DeviceDataManagerController.getR_PhaseOutputCurrent())> 0.0f ){
									
									Data_RefStdBofa.sendReadConstOfLiveRefStdCmd();  
								}
								Data_LduBofa.bofaLDU_PreRequisiteForReadError();
								Data_LduBofa dataLduBofa = new Data_LduBofa();
								dataLduBofa.bofaDisplayLDU_ReadErrorTimerInit();

							}
							setExecuteTimeCounter(timeduration +BufferTimeToReadLDU_DataInSec);
							ExecuteTimerDisplay();
							UI_TableRefreshTrigger(timeduration);
						}
						else{
							SkipCurrentTestPointGUI_Update();
						}
					} else{
						ApplicationLauncher.logger.debug("FrequencyExecuteStart : REFSTD_CONNECTED_NONE" );
						setExecuteTimeCounter(timeduration+BufferTimeToReadLDU_DataInSec);
						ExecuteTimerDisplay();
						UI_TableRefreshTrigger(timeduration);
						if(ProcalFeatureEnable.PROPOWER_SRC_FEEDBACK_DISPLAY ){

							ApplicationLauncher.logger.debug("FrequencyExecuteStart : ProPower Source Feedback display" );
							manipulateGainOffsetValueForFeedBackProcess();
							SerialDM_Obj.lscsPowerSourceReadFeedBackTimerInit();
						}

						if(ProcalFeatureEnable.PROPOWER_SRC_ONLY ){

							//int noOfTestPointCompleted = LiveTableDataManager.getCountOfNoOfTestPointCompleted();
							int totalNoOfTestPointIndex  = getCurrentProjectTestPointList().length()-1;
							ApplicationLauncher.logger.debug("FrequencyExecuteStart : totalNoOfTestPointIndex: " + totalNoOfTestPointIndex );
							ApplicationLauncher.logger.debug("FrequencyExecuteStart : CurrentTestPoint_Index: " + CurrentTestPoint_Index );

							EnableStopButton();
							if(CurrentTestPoint_Index < totalNoOfTestPointIndex){
								enableBtnLoadNext();
							}
						}
					}  
				}
				else{
					SkipCurrentTestPointGUI_Update();
				}

			}else {
				setExecuteTimeCounter(timeduration+BufferTimeToReadLDU_DataInSec);
				ExecuteTimerDisplay();
				UI_TableRefreshTrigger(timeduration);
				int totalNoOfTestPointIndex  = getCurrentProjectTestPointList().length()-1;
				ApplicationLauncher.logger.debug("FrequencyExecuteStart: totalNoOfTestPointIndex-2: " + totalNoOfTestPointIndex );
				ApplicationLauncher.logger.debug("FrequencyExecuteStart: CurrentTestPoint_Index-2: " + CurrentTestPoint_Index );

				EnableStopButton();
				if(CurrentTestPoint_Index < totalNoOfTestPointIndex){
					enableBtnLoadNext();
				}
			}



		}

		public void PhaseReversalExecuteStart(){
			ApplicationLauncher.logger.info("PhaseReversal Test Started"  );


			int timeduration = DeviceDataManagerController.getInfTimeDuration();

			DeviceDataManagerController.setPhaseRevPowerOn(true);
			DeviceDataManagerController.setSkipPhaseRev(true);
			DisplayDataObj.setVoltageResetRequired(true);
			FailureManager.ResetPowerSrcReasonForFailure();
			DisplayPwrSrc_TurnOn();
			//WaitForPowerSrcTurnOn();
			//LDU_PreRequisiteForReadError();
			if(ProcalFeatureEnable.LSCS_LDU_CONNECTED) {
				if(!ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED) {
					lscsClearLDU_LastStoredResults();
					lscsClearLduSecondaryDisplay();
					lscsLDU_ResetError();
				}
			}else if(ProcalFeatureEnable.BOFA_LDU_CONNECTED){
				Data_LduBofa.bofaSendLduExitDialTest();//bofaSendLduResetErrorAll();
			}
			if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
				WaitForPowerSrcTurnOn();
			}else if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){ 
				waitBufferTimeForBofaPowerSourceOn();
			}

			if(FirstLevelValidation()){
				if(SecondLevelValidation()){
					//LDU_PreRequisiteForReadErrorV2();
					//DisplayLDU_ReadErrorTimerInitV2();
					setExecuteStopStatus(false);
					if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_ENABLED){
						refStdFeedBackControl();
						setRefStdFeedBackControlSuspended(false);
					}

					if(ProcalFeatureEnable.CCUBE_LDU_CONNECTED) {
						LDU_PreRequisiteForReadErrorV2();
						DisplayLDU_ReadErrorTimerInitV2();
					}else if(ProcalFeatureEnable.LSCS_LDU_CONNECTED){

						lscsLDU_PreRequisiteForReadError();
						lscsDisplayLDU_ReadErrorTimerInit();				

					}else if(ProcalFeatureEnable.BOFA_LDU_CONNECTED){
						Data_RefStdBofa.setRsmFreqReadingRequired(false);
						if(Float.parseFloat(DeviceDataManagerController.getR_PhaseOutputCurrent())> 0.0f ){
							
							Data_RefStdBofa.sendReadConstOfLiveRefStdCmd();  
						}
						Data_LduBofa.bofaLDU_PreRequisiteForReadError();
						Data_LduBofa dataLduBofa = new Data_LduBofa();
						dataLduBofa.bofaDisplayLDU_ReadErrorTimerInit();

					}


					setExecuteTimeCounter(timeduration+BufferTimeToReadLDU_DataInSec);
					ExecuteTimerDisplay();
					UI_TableRefreshTrigger(timeduration);
				}
				else{
					SkipCurrentTestPointGUI_Update();
				}
			}
			else{
				SkipCurrentTestPointGUI_Update();
			}


		}

		public void waitBufferTimeForBofaPowerSourceOn(){
			ApplicationLauncher.logger.info("waitBufferTimeForBofaPowerSourceOn :Entry"  );
			Integer WaitTimeCounter = 5;//;
			while (  WaitTimeCounter!=0 && (!getUserAbortedFlag())){
				Sleep(1000);
				WaitTimeCounter--; 
			}
			ApplicationLauncher.logger.info("waitBufferTimeForBofaPowerSourceOn :Exit"  );
		}

		public void waitForAll_LDU_DeviceDataPulseReadData(){
			ApplicationLauncher.logger.info("waitForAll_LDU_DeviceDataPulseReadData :Entry"  );
			Integer WaitTimeCounter = ConstTestWaitTimeToReadLDU_DataInSec;
			while (DeviceDataManagerController.getDevicesToBeRead().size()!=0 && WaitTimeCounter!=0 && (!getUserAbortedFlag())){
				Sleep(1000);
				WaitTimeCounter--; 
			}
			ApplicationLauncher.logger.info("waitForAll_LDU_DeviceDataPulseReadData :Exit"  );
		}

		@SuppressWarnings("static-access")
		public void ConstTestExecuteStart(){
			ApplicationLauncher.logger.info("ConstTestExecuteStart : Started"  );
			if(ProcalFeatureEnable.BOFA_REFSTD_CONNECTED){
				DeviceDataManagerController.setRefStdReadDataFlag(true);
				//Data_RefStdBofa.setLiveRefMeterConstant(18000000);
				
				//Data_RefStdBofa.setSerialRefComInstantMetricsRefreshTimeInMsec(ConstTestRefComInstantMetricsRefreshTimeInMsec);
			}else{
				SerialDataManager.setSerialRefComInstantMetricsRefreshTimeInMsec(ConstTestRefComInstantMetricsRefreshTimeInMsec);
			}

			ApplicationHomeController.updateBottomSecondaryStatus("1-Awaiting for Ref Standard...",ConstantApp.LEFT_STATUS_INFO);
			if(ProcalFeatureEnable.LSCS_LDU_CONNECTED) {
				lscsClearLDU_LastStoredResults();
				lscsClearLduSecondaryDisplay();
				lscsLDU_ResetError();
			}else if(ProcalFeatureEnable.BOFA_LDU_CONNECTED){
				Data_LduBofa.bofaSendLduResetErrorAll();
				//Data_LduBofa.bofaSendLduExitDialTest();//bofaSendLduResetErrorAll();
			}
			if(!DeviceDataManagerController.getRefStdReadDataFlag()){
				ApplicationLauncher.logger.info("ConstTestExecuteStart : Awaiting for getRefStdReadDataFlag..."  );
				int RetryCount = 30;
				while(RetryCount>0 && !DeviceDataManagerController.getRefStdReadDataFlag() && (!getUserAbortedFlag())){
					Sleep(1000);
					RetryCount--;
				}
				ApplicationLauncher.logger.info("ConstTestExecuteStart : Awaiting for getRefStdReadDataFlag Exit"  );
			}


			if((DisplayDataObj.getRefStdReadDataFlag()) && (!getUserAbortedFlag()) ){
				ApplicationHomeController.updateBottomSecondaryStatus("2-ZeroCurrent-Init",ConstantApp.LEFT_STATUS_INFO);
				if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED)  {
					setLscsZeroCurrentPowerTurnOn(true);
				}
				runZeroCurrent();//Turn OFF Current Only Voltage
				ApplicationHomeController.updateBottomSecondaryStatus("3-ZeroCurrent-Init power wait",ConstantApp.LEFT_STATUS_INFO);
				//Sleep(((ConstantConfig.POWERONWAITCOUNTER*1000)));
				if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
					WaitForPowerSrcTurnOn();
					if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_ENABLED){
						//refStdFeedBackControl();
						//setRefStdFeedBackControlSuspended(false);
					}
				}
				ApplicationHomeController.updateBottomSecondaryStatus("4-Setting LDU...",ConstantApp.LEFT_STATUS_INFO);
				if(ProcalFeatureEnable.CCUBE_LDU_CONNECTED) {
					LDU_PreRequisiteForConstTest();
				}else if(ProcalFeatureEnable.LSCS_LDU_CONNECTED){
					//lscsClearLDU_LastStoredResults();
					//lscsClearLduSecondaryDisplay();
					lscsLDU_PreRequisiteForConstantTest();
				}else if(ProcalFeatureEnable.BOFA_LDU_CONNECTED){
					Data_LduBofa.bofaSendLduDialTestStartAll();
					DeviceDataManagerController.setRefStdReadDataFlag(true);
				}


				ApplicationHomeController.updateBottomSecondaryStatus("5-Initial kwh value awaiting",ConstantApp.LEFT_STATUS_INFO);
				if((DisplayDataObj.getRefStdReadDataFlag()) && (!getUserAbortedFlag()) ){
					getInitMeterValuesFromUI();// Get Initial value
					waitForInitMeterReading();//User enter readings
				}
				ApplicationHomeController.updateBottomSecondaryStatus("6-Initial kwh value exit",ConstantApp.LEFT_STATUS_INFO);
				if((DisplayDataObj.getRefStdReadDataFlag()) && (!getUserAbortedFlag()) ){
					ApplicationHomeController.updateBottomSecondaryStatus("7-Resetting Ref-Standard",ConstantApp.LEFT_STATUS_INFO);
					resetRefMeterAccumulateSetting();
					ApplicationHomeController.updateBottomSecondaryStatus("8-Resetting Ref-Standard exit",ConstantApp.LEFT_STATUS_INFO);
					if((DisplayDataObj.getRefStdReadDataFlag()) && (!getUserAbortedFlag()) ){
						ApplicationHomeController.updateBottomSecondaryStatus("8-Accumulating Ref-Standard",ConstantApp.LEFT_STATUS_INFO);
						refAccumulateStart();
						if((DisplayDataObj.getRefStdReadDataFlag()) && (!getUserAbortedFlag()) ){
							ApplicationHomeController.updateBottomSecondaryStatus("9-RefStandard Read Accu data",ConstantApp.LEFT_STATUS_INFO);
							if(ProcalFeatureEnable.BOFA_REFSTD_CONNECTED){
								DeviceDataManagerController.setRefInitPhaseAReading("0.0");
								Data_RefStdBofa.setRefStdActiveEnergyAccumulatedInKwh(0.0f);
								ProjectExecutionController.setFeedbackR_ActiveEnergy("0.0");
								//DisplayDataObj.setRefInitPhaseAReading("0.0");
							}else{
								readRefMeterAccumulationReading();
							}
							if((DisplayDataObj.getRefStdReadDataFlag()) && (!getUserAbortedFlag()) ){
								ApplicationLauncher.logger.info("getCurrentPhaseAReading: " + DeviceDataManagerController.getCurrentPhaseAReading());
								DisplayDataObj.setRefInitValues();//set init ref reading
								ApplicationHomeController.updateBottomSecondaryStatus("10-RatedCurrent setting...",ConstantApp.LEFT_STATUS_INFO);
								if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED)  {
									setLscsZeroCurrentPowerTurnOn(false);
								}
								runRatedCurrent();
								if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
									WaitForPowerSrcTurnOn();
								}else if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
									ApplicationLauncher.logger.info("ConstTestExecuteStart : Awaiting for BOFA current set rating"  );
									WaitForPowerSrcTurnOn();
									ApplicationLauncher.logger.info("ConstTestExecuteStart : BOFA current set-exit"  );
								}
								if((DisplayDataObj.getRefStdReadDataFlag()) && (!getUserAbortedFlag()) ){
									ApplicationHomeController.updateBottomSecondaryStatus("11-Constant test running...",ConstantApp.LEFT_STATUS_INFO);
									//runConstTest();
									if(getPresentMeterType().contains(ConstantApp.METERTYPE_SINGLEPHASE)){
										if(ProcalFeatureEnable.BOFA_REFSTD_CONNECTED){
											if(Float.parseFloat(DeviceDataManagerController.getR_PhaseOutputCurrent())> 0.0f ){
												Data_RefStdBofa.sendReadConstOfLiveRefStdCmd();  
											}
											runConstTestBofa();
											DeviceDataManagerController.setRefStdReadDataFlag(true);
										}else {
											//runConstTest();
											runConstTestV2();
										}
										//runConstTestV2();
									}else { //if(getPresentMeterType().contains(ConstantApp.METERTYPE_THREEPHASE)){
										runConstTestV3();
										//runConstTest();
									}
									//

									if((DisplayDataObj.getRefStdReadDataFlag()) && (!getUserAbortedFlag()) ){
										ApplicationHomeController.updateBottomSecondaryStatus("12-ZeroCurrent-Setting...",ConstantApp.LEFT_STATUS_INFO);
										if(ProcalFeatureEnable.POWERSOURCE_DOSAGE_CURRENT_RELAY_OFF_ENABLED){
											//stopManualSourceCurrentRelay();	
											if((DisplayDataObj.getRefStdReadDataFlag()) && (!getUserAbortedFlag()) ){
												makeAllCurrentZero();
											}
										}else{
											if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
												Data_PowerSourceBofa.sendCurrentStoppingCommand();
											}else{
												runZeroCurrent();//Turn OFF Current
											}
										}
										ApplicationHomeController.updateBottomSecondaryStatus("13-ZeroCurrent-awaiting...",ConstantApp.LEFT_STATUS_INFO);
										//Sleep(((ConstantConfig.POWERONWAITCOUNTER*1000)));
										if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
											WaitForPowerSrcTurnOn();
										}
										if((DisplayDataObj.getRefStdReadDataFlag()) && (!getUserAbortedFlag()) ){
											ApplicationHomeController.updateBottomSecondaryStatus("14-RefStd-Stop Accumulation",ConstantApp.LEFT_STATUS_INFO);
											refAccumulateStop();
											ApplicationHomeController.updateBottomSecondaryStatus("15-RefStd-Stop Accu reading data",ConstantApp.LEFT_STATUS_INFO);
											readRefMeterAccumulationReading();
										}
										if((DisplayDataObj.getRefStdReadDataFlag()) && (!getUserAbortedFlag()) ){
											DisplayDataObj.setRefFinalValues();
											ApplicationHomeController.updateBottomSecondaryStatus("16-Reading LDU...",ConstantApp.LEFT_STATUS_INFO);
											if(ProcalFeatureEnable.CCUBE_LDU_CONNECTED) {
												DisplayLDU_ReadConstTimerInit();
											}else if(ProcalFeatureEnable.LSCS_LDU_CONNECTED){

												lscsLDU_StopConstantTest();
												displayLscsLDU_ReadConstantTimerInit();

											}else if(ProcalFeatureEnable.BOFA_REFSTD_CONNECTED){

												Data_LduBofa lduBofaObj = new Data_LduBofa();
												lduBofaObj.bofaLduTriggerReadConstantTestPulseCount(); // trigger the command to read the pulse count from LDU



											}
										}
										if((DisplayDataObj.getRefStdReadDataFlag()) && (!getUserAbortedFlag()) ){
											//if(ProcalFeatureEnable.CCUBE_LDU_CONNECTED) {
											waitForAll_LDU_DeviceDataPulseReadData();
											ApplicationHomeController.updateBottomSecondaryStatus("17-Reading LDU done",ConstantApp.LEFT_STATUS_INFO);
											//}else if(ProcalFeatureEnable.LSCS_LDU_CONNECTED){

											//}

										}
										if((DisplayDataObj.getRefStdReadDataFlag()) && (!getUserAbortedFlag()) ){
											ApplicationHomeController.updateBottomSecondaryStatus("18-Final kWH entry awaiting...",ConstantApp.LEFT_STATUS_INFO);
											getFinalMeterValuesFromUI();//Get Final Value
											waitForFinalMeterReading();
										}
										ApplicationHomeController.updateBottomSecondaryStatus("19-ZeroCurrentPowerOn",ConstantApp.LEFT_STATUS_INFO);
										if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED)  {
											setLscsZeroCurrentPowerTurnOn(true);
										}
										ZeroCurrentPowerOn();
										//DisplayPwrSrc_TurnOff();
										if(ProcalFeatureEnable.BOFA_REFSTD_CONNECTED){
											//Data_RefStdBofa.resetSerialRefComInstantMetricsRefreshTimeInMsec();
										}else{
											SerialDataManager.resetSerialRefComInstantMetricsRefreshTimeInMsec();
										}
										ApplicationHomeController.updateBottomSecondaryStatus("20-Constant test done",ConstantApp.LEFT_STATUS_INFO);
										if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
											WaitForPowerSrcTurnOn();
										}
										if((DisplayDataObj.getRefStdReadDataFlag()) && (!getUserAbortedFlag()) ){

											ApplicationHomeController.update_left_status("Updating KWh to DB",ConstantApp.LEFT_STATUS_DEBUG);
											DisplayDataObj.SaveKWhToDB();
											ApplicationHomeController.update_left_status("Finished updating KWh to DB",ConstantApp.LEFT_STATUS_DEBUG);
											getCurrentProjectName();

											try {
												ApplicationLauncher.logger.debug("ConstTestExecuteStart: setUI_TableRefreshFlag-true");
												setUI_TableRefreshFlag(true);
											} catch (Exception e) {
												ApplicationLauncher.logger.error("ConstTestExecuteStart :Exception : " + e.getMessage());
												e.printStackTrace();
											}	
										}

									}else{
										//DisplayPwrSrc_TurnOff();
										ApplicationHomeController.updateBottomSecondaryStatus("12A-ZeroCurrent Exit",ConstantApp.LEFT_STATUS_INFO);
										if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED)  {
											setLscsZeroCurrentPowerTurnOn(true);
										}
										ZeroCurrentPowerOn();
										if(ProcalFeatureEnable.BOFA_REFSTD_CONNECTED){
											Data_RefStdBofa.resetSerialRefComInstantMetricsRefreshTimeInMsec();
										}else{
											SerialDataManager.resetSerialRefComInstantMetricsRefreshTimeInMsec();
										}
										if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
											WaitForPowerSrcTurnOn();
										}
										ApplicationLauncher.logger.info("ConstTestExecuteStart : Exit1 ");

									}
								}else{
									//DisplayPwrSrc_TurnOff();
									ApplicationHomeController.updateBottomSecondaryStatus("11A-ZeroCurrent Exit",ConstantApp.LEFT_STATUS_INFO);
									if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED)  {
										setLscsZeroCurrentPowerTurnOn(true);
									}
									ZeroCurrentPowerOn();
									if(ProcalFeatureEnable.BOFA_REFSTD_CONNECTED){
										Data_RefStdBofa.resetSerialRefComInstantMetricsRefreshTimeInMsec();
									}else{
										SerialDataManager.resetSerialRefComInstantMetricsRefreshTimeInMsec();
									}
									if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
										WaitForPowerSrcTurnOn();
									}
									ApplicationLauncher.logger.info("ConstTestExecuteStart : Exit2 ");
								}
							}else{
								ApplicationLauncher.logger.info("ConstTestExecuteStart : ExitK1 ");
							}
						}else{
							ApplicationLauncher.logger.info("ConstTestExecuteStart : ExitK2 ");
						}
					}else{
						ApplicationLauncher.logger.info("ConstTestExecuteStart : ExitK3 ");
					}
				}else{
					ApplicationLauncher.logger.info("ConstTestExecuteStart : ExitK4 ");
				}
			}	else{
				ApplicationLauncher.logger.info("ConstTestExecuteStart : ExitK5 ");
			}
			if(getStepRunFlag()){
				refreshProjectStatusGraphGUI();
			}
			ApplicationHomeController.updateBottomSecondaryStatus("Constant Test Exit",ConstantApp.LEFT_STATUS_INFO);
			setExecuteTimeCounter(Const_BufferTime_InSec);

			ExecuteTimerDisplay();
			ApplicationHomeController.updateBottomSecondaryStatus("",ConstantApp.LEFT_STATUS_INFO);


		}





		@SuppressWarnings("static-access")
		public void ConstTestManualSourceExecuteStart(){
			ApplicationLauncher.logger.info("ConstTestManualSourceExecuteStart : Started"  );
			boolean lscsConstantTestGracefulExitExecuted = false;


			if(ProcalFeatureEnable.KIGGS_REFSTD_CONNECTED){

			}else{
				SerialDataManager.setSerialRefComInstantMetricsRefreshTimeInMsec(ConstTestRefComInstantMetricsRefreshTimeInMsec);
			}

			ApplicationHomeController.updateBottomSecondaryStatus("1-Awaiting for Ref Standard...",ConstantApp.LEFT_STATUS_INFO);
			if(ProcalFeatureEnable.LSCS_LDU_CONNECTED) {
				lscsClearLDU_LastStoredResults();
				lscsClearLduSecondaryDisplay();
				lscsLDU_ResetError();
			}
			if(!DeviceDataManagerController.getRefStdReadDataFlag()){
				ApplicationLauncher.logger.info("ConstTestManualSourceExecuteStart : Awaiting for getRefStdReadDataFlag..."  );
				int RetryCount = 30;
				while(RetryCount>0 && !DeviceDataManagerController.getRefStdReadDataFlag() && (!getUserAbortedFlag())){
					Sleep(1000);
					RetryCount--;
				}
				ApplicationLauncher.logger.info("ConstTestManualSourceExecuteStart : Awaiting for getRefStdReadDataFlag Exit"  );
			}


			if((DisplayDataObj.getRefStdReadDataFlag()) && (!getUserAbortedFlag()) ){
				ApplicationHomeController.updateBottomSecondaryStatus("2-ZeroCurrent-Init",ConstantApp.LEFT_STATUS_INFO);
				if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED)  {
					setLscsZeroCurrentPowerTurnOn(true);
				}
				//runZeroCurrent();//Turn OFF Current Only Voltage

				runRatedCurrentManualSource();
				ApplicationLauncher.logger.info("ConstTestManualSourceExecuteStart : test0A");
				manualSourceSetPromptDisplay();

				runZeroCurrentManualSource();
				ApplicationLauncher.logger.info("ConstTestManualSourceExecuteStart : test0B");
				manualSourceSetCurrentPromptDisplay();


				ApplicationHomeController.updateBottomSecondaryStatus("3-ZeroCurrent-Init power wait",ConstantApp.LEFT_STATUS_INFO);
				//Sleep(((ConstantConfig.POWERONWAITCOUNTER*1000)));
				if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
					WaitForPowerSrcTurnOn();
					if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_ENABLED){
						//refStdFeedBackControl();
						//setRefStdFeedBackControlSuspended(false);
					}
				}
				ApplicationHomeController.updateBottomSecondaryStatus("4-Setting LDU...",ConstantApp.LEFT_STATUS_INFO);
				if(ProcalFeatureEnable.CCUBE_LDU_CONNECTED) {
					LDU_PreRequisiteForConstTest();
				}else if(ProcalFeatureEnable.LSCS_LDU_CONNECTED){
					//lscsClearLDU_LastStoredResults();
					//lscsClearLduSecondaryDisplay();
					lscsLDU_PreRequisiteForConstantTest();
				}
				ApplicationHomeController.updateBottomSecondaryStatus("5-Initial kwh value awaiting",ConstantApp.LEFT_STATUS_INFO);
				if((DisplayDataObj.getRefStdReadDataFlag()) && (!getUserAbortedFlag()) ){
					getInitMeterValuesFromUI();// Get Initial value
					waitForInitMeterReading();//User enter readings
				}
				ApplicationHomeController.updateBottomSecondaryStatus("6-Initial kwh value exit",ConstantApp.LEFT_STATUS_INFO);
				if((DisplayDataObj.getRefStdReadDataFlag()) && (!getUserAbortedFlag()) ){
					ApplicationHomeController.updateBottomSecondaryStatus("7-Resetting Ref-Standard",ConstantApp.LEFT_STATUS_INFO);
					resetRefMeterAccumulateSetting();
					ApplicationHomeController.updateBottomSecondaryStatus("8-Resetting Ref-Standard exit",ConstantApp.LEFT_STATUS_INFO);
					if((DisplayDataObj.getRefStdReadDataFlag()) && (!getUserAbortedFlag()) ){
						ApplicationHomeController.updateBottomSecondaryStatus("8-Accumulating Ref-Standard",ConstantApp.LEFT_STATUS_INFO);
						refAccumulateStart();
						if((DisplayDataObj.getRefStdReadDataFlag()) && (!getUserAbortedFlag()) ){
							ApplicationHomeController.updateBottomSecondaryStatus("9-RefStandard Read Accu data",ConstantApp.LEFT_STATUS_INFO);

							if(ProcalFeatureEnable.KIGGS_REFSTD_CONNECTED){
								DisplayDataObj.setReadRefStdAccuDataFlag(true);
							}else{
								readRefMeterAccumulationReading();
							}
							if((DisplayDataObj.getRefStdReadDataFlag()) && (!getUserAbortedFlag()) ){
								ApplicationLauncher.logger.info("getCurrentPhaseAReading: " + DeviceDataManagerController.getCurrentPhaseAReading());
								DisplayDataObj.setRefInitValues();//set init ref reading
								ApplicationHomeController.updateBottomSecondaryStatus("10-RatedCurrent setting...",ConstantApp.LEFT_STATUS_INFO);
								if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED)  {
									setLscsZeroCurrentPowerTurnOn(false);
								}
								//runRatedCurrent();
								runRatedCurrentManualSource();
								ApplicationLauncher.logger.info("ConstTestManualSourceExecuteStart : test1");
								manualSourceSetCurrentPromptDisplay();
								if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
									WaitForPowerSrcTurnOn();
								}
								if((DisplayDataObj.getRefStdReadDataFlag()) && (!getUserAbortedFlag()) ){
									ApplicationHomeController.updateBottomSecondaryStatus("11-Constant test running...",ConstantApp.LEFT_STATUS_INFO);
									//runConstTest();
									runConstTestManualSource();
									if((DisplayDataObj.getRefStdReadDataFlag()) && (!getUserAbortedFlag()) ){
										ApplicationHomeController.updateBottomSecondaryStatus("12-ZeroCurrent-Setting...",ConstantApp.LEFT_STATUS_INFO);
										//runZeroCurrent();//Turn OFF Current
										runZeroCurrentManualSource();
										ApplicationLauncher.logger.info("ConstTestManualSourceExecuteStart : test2");
										if(ProcalFeatureEnable.POWERSOURCE_DOSAGE_CURRENT_RELAY_OFF_ENABLED){
											stopManualSourceCurrentRelay();
										}else{
											manualSourceSetCurrentPromptDisplay();
										}
										//manualSourceSetPromptDisplay();
										ApplicationHomeController.updateBottomSecondaryStatus("13-ZeroCurrent-awaiting...",ConstantApp.LEFT_STATUS_INFO);
										//Sleep(((ConstantConfig.POWERONWAITCOUNTER*1000)));
										if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
											WaitForPowerSrcTurnOn();
										}
										if((DisplayDataObj.getRefStdReadDataFlag()) && (!getUserAbortedFlag()) ){
											ApplicationHomeController.updateBottomSecondaryStatus("14-RefStd-Stop Accumulation",ConstantApp.LEFT_STATUS_INFO);
											//stopManualSourceCurrentRelay();
											refAccumulateStop();
											ApplicationHomeController.updateBottomSecondaryStatus("15-RefStd-Stop Accu reading data",ConstantApp.LEFT_STATUS_INFO);
											if(ProcalFeatureEnable.KIGGS_REFSTD_CONNECTED){
												DisplayDataObj.setReadRefStdAccuDataFlag(false);
												ApplicationLauncher.logger.info("ConstTestManualSourceExecuteStart : getCurrentPhaseAReading: " + DeviceDataManagerController.getCurrentPhaseAReading());
											}else{
												readRefMeterAccumulationReading();
											}
										}
										if((DisplayDataObj.getRefStdReadDataFlag()) && (!getUserAbortedFlag()) ){
											DisplayDataObj.setRefFinalValues();
											ApplicationHomeController.updateBottomSecondaryStatus("16-Reading LDU...",ConstantApp.LEFT_STATUS_INFO);
											if(ProcalFeatureEnable.CCUBE_LDU_CONNECTED) {
												DisplayLDU_ReadConstTimerInit();
											}else if(ProcalFeatureEnable.LSCS_LDU_CONNECTED){

												lscsLDU_StopConstantTest();
												if(ProcalFeatureEnable.POWERSOURCE_DOSAGE_CURRENT_RELAY_OFF_ENABLED){
													Sleep(500);
													lscsLDU_StopConstantTest();/// required additionally, some times some ldus are not stooped
													Sleep(500);
												}
												displayLscsLDU_ReadConstantTimerInit();
												lscsConstantTestGracefulExitExecuted = true;

											}

										}
										if((DisplayDataObj.getRefStdReadDataFlag()) && (!getUserAbortedFlag()) ){
											//if(ProcalFeatureEnable.CCUBE_LDU_CONNECTED) {
											waitForAll_LDU_DeviceDataPulseReadData();
											ApplicationHomeController.updateBottomSecondaryStatus("17-Reading LDU done",ConstantApp.LEFT_STATUS_INFO);
											//}else if(ProcalFeatureEnable.LSCS_LDU_CONNECTED){

											//}

										}
										if((DisplayDataObj.getRefStdReadDataFlag()) && (!getUserAbortedFlag()) ){
											ApplicationHomeController.updateBottomSecondaryStatus("18-Final kWH entry awaiting...",ConstantApp.LEFT_STATUS_INFO);
											getFinalMeterValuesFromUI();//Get Final Value
											waitForFinalMeterReading();
										}
										ApplicationHomeController.updateBottomSecondaryStatus("19-ZeroCurrentPowerOn",ConstantApp.LEFT_STATUS_INFO);
										if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED)  {
											setLscsZeroCurrentPowerTurnOn(true);
										}
										//ZeroCurrentPowerOn();
										runZeroCurrentManualSource();
										ApplicationLauncher.logger.info("ConstTestManualSourceExecuteStart : test3");
										//manualSourceSetPromptDisplay();
										//manualSourceSetCurrentPromptDisplay();
										//DisplayPwrSrc_TurnOff();
										SerialDataManager.resetSerialRefComInstantMetricsRefreshTimeInMsec();
										ApplicationHomeController.updateBottomSecondaryStatus("20-Constant test done",ConstantApp.LEFT_STATUS_INFO);
										if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
											WaitForPowerSrcTurnOn();
										}
										if((DisplayDataObj.getRefStdReadDataFlag()) && (!getUserAbortedFlag()) ){

											ApplicationHomeController.update_left_status("Updating KWh to DB",ConstantApp.LEFT_STATUS_DEBUG);
											DisplayDataObj.SaveKWhToDB();
											ApplicationHomeController.update_left_status("Finished updating KWh to DB",ConstantApp.LEFT_STATUS_DEBUG);
											getCurrentProjectName();

											try {
												ApplicationLauncher.logger.debug("ConstTestManualSourceExecuteStart: setUI_TableRefreshFlag-true");
												setUI_TableRefreshFlag(true);
											} catch (Exception e) {
												ApplicationLauncher.logger.error("ConstTestManualSourceExecuteStart :Exception : " + e.getMessage());
												e.printStackTrace();
											}	
										}

									}else{
										//DisplayPwrSrc_TurnOff();
										ApplicationHomeController.updateBottomSecondaryStatus("12A-ZeroCurrent Exit",ConstantApp.LEFT_STATUS_INFO);
										if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED)  {
											setLscsZeroCurrentPowerTurnOn(true);
										}
										//ZeroCurrentPowerOn();
										runZeroCurrentManualSource();
										ApplicationLauncher.logger.info("ConstTestManualSourceExecuteStart : test4");
										//manualSourceSetPromptDisplay();
										manualSourceSetCurrentPromptDisplay();
										SerialDataManager.resetSerialRefComInstantMetricsRefreshTimeInMsec();
										if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
											WaitForPowerSrcTurnOn();
										}
										ApplicationLauncher.logger.info("ConstTestManualSourceExecuteStart : Exit1 ");

									}
								}else{
									//DisplayPwrSrc_TurnOff();
									ApplicationHomeController.updateBottomSecondaryStatus("11A-ZeroCurrent Exit",ConstantApp.LEFT_STATUS_INFO);
									if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED)  {
										setLscsZeroCurrentPowerTurnOn(true);
									}
									//ZeroCurrentPowerOn();
									runZeroCurrentManualSource();
									ApplicationLauncher.logger.info("ConstTestManualSourceExecuteStart : test5");
									manualSourceSetCurrentPromptDisplay();
									//manualSourceSetPromptDisplay();
									SerialDataManager.resetSerialRefComInstantMetricsRefreshTimeInMsec();
									if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
										WaitForPowerSrcTurnOn();
									}
									ApplicationLauncher.logger.info("ConstTestManualSourceExecuteStart : Exit2 ");
								}
							}
						}
					}
				}
				if(ProcalFeatureEnable.LSCS_LDU_CONNECTED){
					if(!lscsConstantTestGracefulExitExecuted){
						ApplicationLauncher.logger.info("ConstTestManualSourceExecuteStart : lscsConstantTestGracefulExitExecuted ");
						lscsLDU_StopConstantTest();
						Sleep(500);
						lscsLDU_SendDataRefreshCmd();
						//if(ProcalFeatureEnable.POWERSOURCE_DOSAGE_CURRENT_RELAY_OFF_ENABLED){
						//Sleep(500);
						//lscsLDU_StopConstantTest();// required additionally, some times some ldus are not stooped
						//}
						//displayLscsLDU_ReadConstantTimerInit();
						lscsConstantTestGracefulExitExecuted = true;
					}
					/*				if(ProcalFeatureEnable.POWERSOURCE_DOSAGE_CURRENT_RELAY_OFF_ENABLED){
					lscsLDU_StopConstantTest();
					//lscsLDU_SendDataRefreshCmd();
					Sleep(1000);
					lscsLDU_SendDataRefreshCmd();
					Sleep(1000);
				}*/
				}
				//if(!getUserAbortedFlag()){

				if(ProcalFeatureEnable.KIGGS_REFSTD_CONNECTED){
					DisplayDataObj.setReadRefStdAccuDataFlag(false);
					Sleep(1000);
					SerialDM_Obj.kiggsRefStdSetModeBasicMeasurement();
					Sleep(1000);
					SerialDM_Obj.kiggsRefStdSetModeSettingDefault();

				}

				//}
				//Sleep(1000);
				//SerialDM_Obj.kiggsRefStdSetCurrentRangeSettingMaxTap();
			}	
			ApplicationHomeController.updateBottomSecondaryStatus("Constant Test Exit",ConstantApp.LEFT_STATUS_INFO);
			ApplicationLauncher.logger.debug("ConstTestManualSourceExecuteStart : setExecuteTimeCounter:  " + Const_BufferTime_InSec);
			setExecuteTimeCounter(Const_BufferTime_InSec);

			ExecuteTimerDisplay();
			ApplicationHomeController.updateBottomSecondaryStatus("",ConstantApp.LEFT_STATUS_INFO);
			ApplicationLauncher.logger.info("ConstTestManualSourceExecuteStart : Exit3 ");

		}

		private void stopManualSourceCurrentRelay() {
			
			ApplicationLauncher.logger.debug("stopManualSourceCurrentRelay: Entry");
			//if(ProcalFeatureEnable.POWERSOURCE_DOSAGE_CURRENT_RELAY_OFF_ENABLED){
			if(ProcalFeatureEnable.LSCS_LDU_CONNECTED){
				ApplicationLauncher.logger.debug("stopManualSourceCurrentRelay: Turning Off power source current relay");
				lscsLDU_PowerSourceCurrentRelayCutOff();
				Sleep(1000);
			}
			//}
		}

		public void CustomRatingExecuteStart(){


			ApplicationLauncher.logger.info("CustomRating Test Started"  );


			int timeduration = DeviceDataManagerController.getInfTimeDuration();

			String presentSelectedFrequency = "49.0";

			presentSelectedFrequency = String.valueOf(DeviceDataManagerController.get_PwrSrc_Freq());
			if(ProcalFeatureEnable.PROPOWER_SRC_ONLY ){

				LiveTableDataManager.UpdateliveTableData(1, "N",ConstantApp.EXECUTION_STATUS_INPROGRESS);//EXECUTION_STATUS_COMPLETED
			}

			DisplayDataObj.setVoltageResetRequired(true);
			FailureManager.ResetPowerSrcReasonForFailure();
			if(ProcalFeatureEnable.LSCS_LDU_CONNECTED) {
				if(!ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED) {
					lscsClearLDU_LastStoredResults();
					lscsClearLduSecondaryDisplay();
					lscsLDU_ResetError();
				}
			}else if(ProcalFeatureEnable.BOFA_LDU_CONNECTED){
				Data_LduBofa.bofaSendLduExitDialTest();//bofaSendLduResetErrorAll();
			}
			if (ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){
				Display_Cus_PwrSrc_TurnOn();
			}else if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
				ApplicationLauncher.logger.debug("CustomRating : DisplayDataObj.getLastSetPowerSourceFrequency: " + DeviceDataManagerController.getLastSetPowerSourceFrequency()  );
				ApplicationLauncher.logger.debug("CustomRating : presentSelectedFrequency : " +presentSelectedFrequency  );
				if(DeviceDataManagerController.getLastSetPowerSourceFrequency().equals(presentSelectedFrequency)){
					ApplicationLauncher.logger.debug("CustomRating voltage reset not required-1"  );
					DisplayDataObj.setVoltageResetRequired(false);

				}else{
					if(getOneTimeExecuted()){
						if(ProcalFeatureEnable.PROPOWER_SRC_ONLY){//// version #s4.2.1.2.1.4 ,when there is freq change , switching to third test point failed. This fixed the issue
							if(DeviceDataManagerController.getLastSetPowerSourceFrequency().equals(presentSelectedFrequency)){
								ApplicationLauncher.logger.debug("CustomRating voltage reset not required-1A"  );
								DisplayDataObj.setVoltageResetRequired(false);	
							}else{
								ApplicationLauncher.logger.debug("CustomRating voltage reset required-A"  );
								DisplayDataObj.setVoltageResetRequired(true);
							}
							
						}else {
							ApplicationLauncher.logger.debug("CustomRating voltage reset required-B"  );
							DisplayDataObj.setVoltageResetRequired(true);
						}
					}else{
						ApplicationLauncher.logger.debug("CustomRating voltage reset not required-2"  );
						DisplayDataObj.setVoltageResetRequired(false);

					}

				}
				DisplayPwrSrc_TurnOn();
				if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
					WaitForPowerSrcTurnOn();
					
				}
				
				//Sleep(5000);
			}else if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){ 
				if(DeviceDataManagerController.getLastSetPowerSourceFrequency().equals(presentSelectedFrequency)){
					ApplicationLauncher.logger.debug("CustomRating voltage reset not required-3"  );
					DisplayDataObj.setVoltageResetRequired(false);

				}else{
					if(getOneTimeExecuted()){
						if(ProcalFeatureEnable.CONVEYOR_CALIB_BOFA_FEATURE_ENABLED) {
							ApplicationLauncher.logger.debug("CustomRating voltage reset not required-3a"  );
							DisplayDataObj.setVoltageResetRequired(false);

						}else {
							ApplicationLauncher.logger.debug("CustomRating voltage reset required-4"  );
							DisplayDataObj.setVoltageResetRequired(true);
						}
					}else{
						ApplicationLauncher.logger.debug("CustomRating voltage reset not required-5"  );
						DisplayDataObj.setVoltageResetRequired(false);

					}

				}
				DisplayPwrSrc_TurnOn();
				waitBufferTimeForBofaPowerSourceOn();
			}


			//WaitForPowerSrcTurnOn();
			//LDU_PreRequisiteForReadError();
			if(!ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED) {

				if(FirstLevelValidation()){
					//LDU_PreRequisiteForReadErrorV2();
					if((!ProcalFeatureEnable.REFSTD_CONNECTED_NONE)){
						if(SecondLevelValidation()){
							//setExecuteStopStatus(false);//RAMversion s4.0.4.9.0.9
							if (ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
								DeviceDataManagerController.setLastSetPowerSourceFrequency(presentSelectedFrequency);

							}

							if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_ENABLED){
								refStdFeedBackControl();
								setRefStdFeedBackControlSuspended(false);
							}

							if(ProcalFeatureEnable.CCUBE_LDU_CONNECTED) {
								LDU_PreRequisiteForReadErrorV2();
								DisplayLDU_ReadErrorTimerInitV2();
							}else if(ProcalFeatureEnable.LSCS_LDU_CONNECTED){
								//lscsClearLDU_LastStoredResults();
								//lscsClearLduSecondaryDisplay();
								lscsLDU_PreRequisiteForReadError();
								lscsDisplayLDU_ReadErrorTimerInit();

							}else if(ProcalFeatureEnable.BOFA_LDU_CONNECTED){
								Data_RefStdBofa.setRsmFreqReadingRequired(false);
								if(Float.parseFloat(DeviceDataManagerController.getR_PhaseOutputCurrent())> 0.0f ){
									
									Data_RefStdBofa.sendReadConstOfLiveRefStdCmd();  
								}
								Data_LduBofa.bofaLDU_PreRequisiteForReadError();
								Data_LduBofa dataLduBofa = new Data_LduBofa();
								dataLduBofa.bofaDisplayLDU_ReadErrorTimerInit();

							}
							
							if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){ 
								
								
/*								private static boolean  dutCalibrationVoltageTargetSet = false; 
								private static boolean  dutCalibrationCurrentTargetSet = false; 
								private static boolean  dutCalibrationCurrentZeroSet = false; 
								private static boolean  dutCalibrationVoltCurrentSetZero = false; */
								
								if(ProcalFeatureEnable.CONVEYOR_CALIB_BOFA_FEATURE_ENABLED) {
									//if targetvoltage -- getPwrSrcR_PhaseVoltInFloat -- is not zero 
									if(DeviceDataManagerController.getPwrSrcR_PhaseVoltInFloat()!=0.0f) {
										setDutCalibrationVoltageTargetSet(true);
									}
									
									ApplicationLauncher.logger.debug("CustomRatingExecuteStart : getPwrSrcCustomInFloat_CurrentI1 : " + DeviceDataManagerController.getPwrSrcCustomInFloat_CurrentI1());
									if(DeviceDataManagerController.getPwrSrcCustomInFloat_CurrentI1()==0.0f) {
										setDutCalibrationCurrentTargetSet(true);
										setDutCalibrationCurrentZeroSet(true);
									}else {
										setDutCalibrationCurrentTargetSet(true);
									}
									
									if(DeviceDataManagerController.getPwrSrcCustomInFloat_CurrentI1()==0.0f && DeviceDataManagerController.getPwrSrcCustomInFloat_VoltU1()==0.0f) {
										setDutCalibrationVoltCurrentSetZero(true);
										setDutCalibrationCurrentZeroSet(true);
									}else {
										setDutCalibrationCurrentTargetSet(true);
									}
									
									//if targetCurrentis -- getPwrSrcR_PhaseCurrentInFloat -- not zero 
									
									//setDutCalibrationCurrentTargetSet(true);
								
								
								}
								
							}

							setExecuteTimeCounter(timeduration+BufferTimeToReadLDU_DataInSec);
							ExecuteTimerDisplay();
							UI_TableRefreshTrigger(timeduration);
							
							
						}
						else{
							SkipCurrentTestPointGUI_Update();
						}
					}else{
						ApplicationLauncher.logger.debug("CustomRating: REFSTD_CONNECTED_NONE" );
						setExecuteTimeCounter(timeduration+BufferTimeToReadLDU_DataInSec);
						
						ExecuteTimerDisplay();
						UI_TableRefreshTrigger(timeduration);
						if(ProcalFeatureEnable.PROPOWER_SRC_FEEDBACK_DISPLAY ){

							ApplicationLauncher.logger.debug("CustomRating: ProPower Source Feedback display" );
							manipulateGainOffsetValueForFeedBackProcess();
							SerialDM_Obj.lscsPowerSourceReadFeedBackTimerInit();


						}
						if(ProcalFeatureEnable.PROPOWER_SRC_ONLY ){

							//int noOfTestPointCompleted = LiveTableDataManager.getCountOfNoOfTestPointCompleted();
							int totalNoOfTestPointIndex  = getCurrentProjectTestPointList().length()-1;
							ApplicationLauncher.logger.debug("CustomRating: totalNoOfTestPointIndex: " + totalNoOfTestPointIndex );
							ApplicationLauncher.logger.debug("CustomRating: CurrentTestPoint_Index: " + CurrentTestPoint_Index );

							EnableStopButton();
							if(CurrentTestPoint_Index < totalNoOfTestPointIndex){
								enableBtnLoadNext();
							}

						}
					}
				}
				else{
					SkipCurrentTestPointGUI_Update();
				}
			}else {
				setExecuteTimeCounter(timeduration+BufferTimeToReadLDU_DataInSec);
				ExecuteTimerDisplay();
				UI_TableRefreshTrigger(timeduration);
				int totalNoOfTestPointIndex  = getCurrentProjectTestPointList().length()-1;
				ApplicationLauncher.logger.debug("CustomRating: totalNoOfTestPointIndex-2: " + totalNoOfTestPointIndex );
				ApplicationLauncher.logger.debug("CustomRating: CurrentTestPoint_Index-2: " + CurrentTestPoint_Index );

				EnableStopButton();
				if(CurrentTestPoint_Index < totalNoOfTestPointIndex){
					enableBtnLoadNext();
				}
			}



		}
		
		


		public void manipulateGainOffsetValueForFeedBackProcess(){

			ApplicationLauncher.logger.debug("manipulateGainOffsetValueForFeedBackProcess: Entry" );

			Double rVoltGainValue = DeviceDataManagerController.manipulateVoltageGainValue(ConstantApp.FIRST_PHASE_DISPLAY_NAME,
					DeviceDataManagerController.getR_PhaseOutputVoltage());
			Double rVoltOffsetValue = DeviceDataManagerController.manipulateVoltageOffsetValue(ConstantApp.FIRST_PHASE_DISPLAY_NAME,
					DeviceDataManagerController.getR_PhaseOutputVoltage());		
			DeviceDataManagerController.setR_PhaseFeedBackProcessVoltageGain(rVoltGainValue);
			DeviceDataManagerController.setR_PhaseFeedBackProcessVoltageOffset(rVoltOffsetValue);
			
			ApplicationLauncher.logger.debug("manipulateGainOffsetValueForFeedBackProcess: rVoltGainValue: " + rVoltGainValue );
			ApplicationLauncher.logger.debug("manipulateGainOffsetValueForFeedBackProcess: rVoltOffsetValue: " + rVoltOffsetValue);

			Double rCurrentGainValue = DeviceDataManagerController.manipulateCurrentGainValue(ConstantApp.FIRST_PHASE_DISPLAY_NAME,
					DeviceDataManagerController.getR_PhaseOutputCurrent());
			Double rCurrentOffsetValue = DeviceDataManagerController.manipulateCurrentOffsetValue(ConstantApp.FIRST_PHASE_DISPLAY_NAME,
					DeviceDataManagerController.getR_PhaseOutputCurrent());		
			DeviceDataManagerController.setR_PhaseFeedBackProcessCurrentGain(rCurrentGainValue);
			DeviceDataManagerController.setR_PhaseFeedBackProcessCurrentOffset(rCurrentOffsetValue);
			
			ApplicationLauncher.logger.debug("manipulateGainOffsetValueForFeedBackProcess: rCurrentGainValue: " + rCurrentGainValue );
			ApplicationLauncher.logger.debug("manipulateGainOffsetValueForFeedBackProcess: rCurrentOffsetValue: " + rCurrentOffsetValue);

			
			
			if(ProcalFeatureEnable.POWER_SOURCE_3PHASE_ENABLED){

				Double yVoltGainValue = DeviceDataManagerController.manipulateVoltageGainValue(ConstantApp.SECOND_PHASE_DISPLAY_NAME,
						DeviceDataManagerController.getY_PhaseOutputVoltage());
				Double yVoltOffsetValue = DeviceDataManagerController.manipulateVoltageOffsetValue(ConstantApp.SECOND_PHASE_DISPLAY_NAME,
						DeviceDataManagerController.getY_PhaseOutputVoltage());
				DeviceDataManagerController.setY_PhaseFeedBackProcessVoltageGain(yVoltGainValue);
				DeviceDataManagerController.setY_PhaseFeedBackProcessVoltageOffset(yVoltOffsetValue);
	
	
				Double bVoltGainValue = DeviceDataManagerController.manipulateVoltageGainValue(ConstantApp.THIRD_PHASE_DISPLAY_NAME,
						DeviceDataManagerController.getB_PhaseOutputVoltage());
				Double bVoltOffsetValue = DeviceDataManagerController.manipulateVoltageOffsetValue(ConstantApp.THIRD_PHASE_DISPLAY_NAME,
						DeviceDataManagerController.getB_PhaseOutputVoltage());
				DeviceDataManagerController.setB_PhaseFeedBackProcessVoltageGain(bVoltGainValue);
				DeviceDataManagerController.setB_PhaseFeedBackProcessVoltageOffset(bVoltOffsetValue);
	
	
	
	
				/*Double rCurrentGainValue = DisplayDataObj.manipulateCurrentGainValue(ConstantApp.FIRST_PHASE_DISPLAY_NAME,
						DisplayDataObj.getR_PhaseOutputCurrent());
				Double rCurrentOffsetValue = DisplayDataObj.manipulateCurrentOffsetValue(ConstantApp.FIRST_PHASE_DISPLAY_NAME,
						DisplayDataObj.getR_PhaseOutputCurrent());		
				DisplayDataObj.setR_PhaseFeedBackProcessCurrentGain(rCurrentGainValue);
				DisplayDataObj.setR_PhaseFeedBackProcessCurrentOffset(rCurrentOffsetValue);*/
	
	
	
				Double yCurrentGainValue = DeviceDataManagerController.manipulateCurrentGainValue(ConstantApp.SECOND_PHASE_DISPLAY_NAME,
						DeviceDataManagerController.getY_PhaseOutputCurrent());
				Double yCurrentOffsetValue = DeviceDataManagerController.manipulateCurrentOffsetValue(ConstantApp.SECOND_PHASE_DISPLAY_NAME,
						DeviceDataManagerController.getY_PhaseOutputCurrent());
				DeviceDataManagerController.setY_PhaseFeedBackProcessCurrentGain(yCurrentGainValue);
				DeviceDataManagerController.setY_PhaseFeedBackProcessCurrentOffset(yCurrentOffsetValue);
	
	
				Double bCurrentGainValue = DeviceDataManagerController.manipulateCurrentGainValue(ConstantApp.THIRD_PHASE_DISPLAY_NAME,
						DeviceDataManagerController.getB_PhaseOutputCurrent());
				Double bCurrentOffsetValue = DeviceDataManagerController.manipulateCurrentOffsetValue(ConstantApp.THIRD_PHASE_DISPLAY_NAME,
						DeviceDataManagerController.getB_PhaseOutputCurrent());
				DeviceDataManagerController.setB_PhaseFeedBackProcessCurrentGain(bCurrentGainValue);
				DeviceDataManagerController.setB_PhaseFeedBackProcessCurrentOffset(bCurrentOffsetValue);

			}

		}

		public void WaitForPowerSrcTurnOn(){
			int SleepCounter = (ConstantAppConfig.POWERONWAITCOUNTER*1000)/3;

			while (!SerialDataManager.getPowerSrcTurnedOnStatus() && SleepCounter > 0 && (!getUserAbortedFlag())){
				Sleep(1000);
				SleepCounter --;
				if(SerialDataManager.getPowerSrcOnFlag()) {
					break;
				}
			}

			ApplicationLauncher.logger.debug("WaitForPowerSrcTurnOn: SleepCounter2: "  + SleepCounter);
			if(SleepCounter>0){

				//Sleep(5000);
				SleepForSecondsWithAbortMonitoring(5);
			}

			if(DeviceDataManagerController.getDeployedEM_CT_Type().equals(ConstantApp.METER_CT_TYPE_HTCT)){
				//Sleep(10000);
				//Sleep(30000);//0.5A
				SleepForSecondsWithAbortMonitoring(30);
				//Sleep(50000);
			}
			if(ProcalFeatureEnable.PROPOWER_SRC_ONLY){// version #s4.2.1.2.1.4 ,when there is freq change , switching to next test point failed. This fixed the issue
				if(!getUserAbortedFlag()){
					setExecuteStopStatus(false);
				}
			}
		}

		/*	public void lscsFineTuneRphaseCurrent(){

		ApplicationLauncher.logger.debug("lscsFineTuneRphaseCurrent : Entry"  );

		//float currentFinetuneAcceptableMinPercent = -0.02f;
		//float currentFinetuneAcceptableMaxPercent = 0.02f;

		float expectedTargetCurrent = Float.parseFloat(DisplayDataObj.getR_PhaseOutputCurrent());//5.0f;//Float.parseFloat(Data_CI_Src.getWritePrimaryTargetCurrent());
		float actualCurrent = Float.parseFloat(getFeedbackR_phaseCurrent());//4.9f;
		float expectedCurrentLowerLimit = 0;
		float expectedCurrentUpperLimit = 0;
		float currentAcceptedFineTunePercentage = 0.2f;
		float previousReadActualCurrent = -1.0f;

		boolean rphaseCurrentFineTuneCompleted = false;

		int maxRetryCount = 5;//ConstantConfigProGEN.FineTuneI_MaxRetryCount;//10;zvxc
		int retryCount = maxRetryCount;

		expectedCurrentUpperLimit = expectedTargetCurrent * ((100 + currentAcceptedFineTunePercentage) / 100);
        expectedCurrentLowerLimit = expectedTargetCurrent * ((100 - currentAcceptedFineTunePercentage) / 100);
		ApplicationLauncher.logger.info("lscsFineTuneRphaseCurrent: expectedTargetCurrent: " + expectedTargetCurrent);
		ApplicationLauncher.logger.info("lscsFineTuneRphaseCurrent: currentAcceptedFineTunePercentage: " + currentAcceptedFineTunePercentage);
		ApplicationLauncher.logger.info("lscsFineTuneRphaseCurrent: expectedCurrentLowerLimit: " + expectedCurrentLowerLimit);
		ApplicationLauncher.logger.info("lscsFineTuneRphaseCurrent: expectedCurrentUpperLimit: " + expectedCurrentUpperLimit);

		if ((expectedCurrentLowerLimit >= actualCurrent) && (actualCurrent <= expectedCurrentUpperLimit) ){
			ApplicationLauncher.logger.info("lscsFineTuneRphaseCurrent: already in range");
			rphaseCurrentFineTuneCompleted = true;
		}else{
			rphaseCurrentFineTuneCompleted = false;
		}
		int timeOutInSec = 10;
		while((!getUserAbortedFlag()) && (retryCount!=0) && (!rphaseCurrentFineTuneCompleted) && (timeOutInSec>0)){
			actualCurrent = Float.parseFloat(getFeedbackR_phaseCurrent());
			if(previousReadActualCurrent != actualCurrent){
				previousReadActualCurrent = actualCurrent;
				if ((expectedCurrentLowerLimit >= actualCurrent) && (actualCurrent <= expectedCurrentUpperLimit) ){
					ApplicationLauncher.logger.info("lscsFineTuneRphaseCurrent: target achieved");
					rphaseCurrentFineTuneCompleted = true;
				}else{

					if (expectedCurrentLowerLimit < actualCurrent){
						SerialDM_Obj.lscsPowerSourceRphaseCurrentIncrementLevel1();
					}else{
						SerialDM_Obj.lscsPowerSourceRphaseCurrentDecrementLevel1();
					}
				}
				retryCount--;
			}
			Sleep(2000);
			timeOutInSec -=2;

		}

		if(timeOutInSec<=0){
			ApplicationLauncher.logger.info("lscsFineTuneRphaseCurrent: timeOut");
		}

		if(getUserAbortedFlag()){
			ApplicationLauncher.logger.info("lscsFineTuneRphaseCurrent: user aborted flag");
		}
		if(retryCount ==0){
			ApplicationLauncher.logger.info("lscsFineTuneRphaseCurrent: exceeded retry count");
		}

		if(rphaseCurrentFineTuneCompleted){
			ApplicationLauncher.logger.info("lscsFineTuneRphaseCurrent: rphaseCurrentFineTuneCompleted");
		}

	}
		 */


		public void lscsFineTuneRphaseVolt(){

			ApplicationLauncher.logger.debug("lscsFineTuneRphaseVolt : Entry"  );

			/*		float VoltFinetuneAcceptableMinPercent = -0.02f;
		float voltFinetuneAcceptableMaxPercent = 0.02f;*/

			float expectedTargetVolt = Float.parseFloat(DeviceDataManagerController.getR_PhaseOutputVoltage());//240.0f;//Float.parseFloat(Data_CI_Src.getWritePrimaryTargetVolt());
			float actualVolt = Float.parseFloat(getFeedbackR_phaseVolt());//239.0f;
			float expectedVoltLowerLimit = 0;
			float expectedVoltUpperLimit = 0;
			float voltAcceptedFineTunePercentage = 0.2f;
			float previousReadActualVolt = -1.0f;

			boolean rphaseVoltFineTuneCompleted = false;

			int maxRetryCount = 5;//ConstantConfigProGEN.FineTuneI_MaxRetryCount;//10;zvxc
			int retryCount = maxRetryCount;

			expectedVoltUpperLimit = expectedTargetVolt * ((100 + voltAcceptedFineTunePercentage) / 100);
			expectedVoltLowerLimit = expectedTargetVolt * ((100 - voltAcceptedFineTunePercentage) / 100);
			ApplicationLauncher.logger.info("lscsFineTuneRphaseVolt: expectedTargetVolt: " + expectedTargetVolt);
			ApplicationLauncher.logger.info("lscsFineTuneRphaseVolt: voltAcceptedFineTunePercentage: " + voltAcceptedFineTunePercentage);
			ApplicationLauncher.logger.info("lscsFineTuneRphaseVolt: expectedVoltLowerLimit: " + expectedVoltLowerLimit);
			ApplicationLauncher.logger.info("lscsFineTuneRphaseVolt: expectedVoltUpperLimit: " + expectedVoltUpperLimit);

			if ((expectedVoltLowerLimit >= actualVolt) && (actualVolt <= expectedVoltUpperLimit) ){
				ApplicationLauncher.logger.info("lscsFineTuneRphaseVolt: already in range");
				rphaseVoltFineTuneCompleted = true;
			}else{
				rphaseVoltFineTuneCompleted = false;
			}
			int timeOutInSec = 10;
			while((!getUserAbortedFlag()) && (retryCount!=0) && (!rphaseVoltFineTuneCompleted) && (timeOutInSec>0)){
				actualVolt = Float.parseFloat(getFeedbackR_phaseVolt());
				if(previousReadActualVolt != actualVolt){
					previousReadActualVolt = actualVolt;
					if ((expectedVoltLowerLimit >= actualVolt) && (actualVolt <= expectedVoltUpperLimit) ){
						ApplicationLauncher.logger.info("lscsFineTuneRphaseVolt: target achieved");
						rphaseVoltFineTuneCompleted = true;
					}else{

						if (expectedVoltLowerLimit < actualVolt){
							SerialDM_Obj.lscsPowerSourceRphaseVoltIncrementLevel1();
						}else{
							SerialDM_Obj.lscsPowerSourceRphaseVoltDecrementLevel1();
						}
					}
					retryCount--;
				}
				//Sleep(2000);
				SleepForSecondsWithAbortMonitoring(2);
				timeOutInSec -=2;

			}

			if(timeOutInSec<=0){
				ApplicationLauncher.logger.info("lscsFineTuneRphaseVolt: timeOut");
			}

			if(getUserAbortedFlag()){
				ApplicationLauncher.logger.info("lscsFineTuneRphaseVolt: user aborted flag");
			}
			if(retryCount ==0){
				ApplicationLauncher.logger.info("lscsFineTuneRphaseVolt: exceeded retry count");
			}

			if(rphaseVoltFineTuneCompleted){
				ApplicationLauncher.logger.info("lscsFineTuneRphaseVolt: rphaseVoltFineTuneCompleted");
			}

		}

		public boolean lscsSetDisableHarmonicsAtSlave(){
			boolean status = false;
			ApplicationLauncher.logger.debug("lscsSetDisableHarmonicsAtSlave : Entry"  );
			status = SerialDM_Obj.lscsHarmonicsSourceSlaveSetHarmonicsNotRequired();
			return status;
		}

		/*	public boolean lscsSetDisableHarmonicsAtStm32Master(){
		boolean status = false;
		ApplicationLauncher.logger.debug("lscsSetDisableHarmonicsAtStm32Master : Entry"  );
		status = SerialDM_Obj.lscsHarmonicsSourceStm32SetHarmonicsNotRequired();
		return status;
	}*/

		public boolean lscsSetEnableHarmonicsAtSlave(){

			ApplicationLauncher.logger.debug("lscsSetEnableHarmonicsAtSlave : Entry"  );
			boolean status = SerialDM_Obj.lscsHarmonicsSourceSlaveSetHarmonicsRequired();
			return status;
		}

		/*	public boolean lscsSetEnableHarmonicsAtStm32Master(){

		ApplicationLauncher.logger.debug("lscsSetEnableHarmonicsAtStm32Master : Entry"  );
		boolean status = SerialDM_Obj.lscsHarmonicsSourceStm32SetHarmonicsRequired();//lscsHarmonicsSourceSlaveSetHarmonicsRequired();dvfdx
		return status;
	}*/

		public boolean lscsSendEnabledHarmonicsOrderDataToSlave(){

			ApplicationLauncher.logger.debug("lscsSendEnabledHarmonicsOrderDataToSlave : Entry"  );
			boolean status = SerialDM_Obj.lscsHarmonicsSourceSlaveSendHarmonicsData();
			return status;
		}


		public boolean lscsSendFundamentalFrequencyToSlave(){

			ApplicationLauncher.logger.debug("lscsSendFundamentalFrequencyToSlave : Entry"  );
			boolean status = SerialDM_Obj.lscsHarmonicsSourceSlaveSendFundFrequency();
			return status;
		}

		public void lscsSendHarmonicsDataTransmissionEndCmdToSlave(){ 

			ApplicationLauncher.logger.debug("lscsSendHarmonicsDataTransmissionEndCmdToSlave : Entry"  );
			SerialDM_Obj.lscsHarmonicsSourceSlaveSendCmdTransmissionEnd();
		}

		/*	public void lscsSendHarmonicsDataTransmissionEndCmdToStm32Master(){ 

		ApplicationLauncher.logger.debug("lscsSendHarmonicsDataTransmissionEndCmdToStm32Master : Entry"  );
		SerialDM_Obj.lscsHarmonicsSourceStm32SendCmdTransmissionEnd();
	}*/


		public void lscsFineTuneWithRefStd(){

			ApplicationLauncher.logger.info("lscsFineTuneWithRefStd : Entry"  );
			String metertype = DeviceDataManagerController.getDeployedEM_ModelType();

			if(metertype.contains(ConstantApp.METERTYPE_SINGLEPHASE)){
				lscsFineTuneRphaseVolt();
				//lscsFineTuneRphaseCurrent();
				//lscsFineTuneRphaseDegree();

			}else if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){

			}
		}


		public void sendRefStdDataToLscsPowerSourceTask(){

			ApplicationLauncher.logger.debug("sendRefStdDataToLscsPowerSourceTask :Entry");
			boolean status = false;
			if(isRefStdFeedBackControlAlreadyReceived()){
				status = true;
				ApplicationLauncher.logger.debug("sendRefStdDataToLscsPowerSourceTask : isRefStdFeedBackControlAlreadyReceived");
				setRefStdFeedBackControlAlreadyReceived(false);
			}else{
				status = waitForPowerSrcFeedBackControlReady();
			}
			if(status){
				SerialDM_Obj.clearPowerSourceSerialData();
				//Sleep(1500);sds
				Sleep(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_SEND_DATA_TO_POWER_SRC_REFRESH_TIME_IN_MILLI_SEC);
				if(!getUserAbortedFlag()) {
					sendRefStdDataToLscsPowerSource();
				}
				//Sleep(1000);//1000//1500
			}else{
				ApplicationLauncher.logger.debug("sendRefStdDataToLscsPowerSourceTask : PowerSrcFeedBackControl not Ready");
			}
		}

		public boolean waitForPowerSrcFeedBackControlReady(){
			boolean status = false;

			ApplicationLauncher.logger.debug("waitForPowerSrcFeedBackControlReady: Entry"  );
			int waitTimeOutInSec = 65;
			status = SerialDM_Obj.lscsWaitForPowerSrcFeedBackControl(waitTimeOutInSec);


			return status;		
		}
		
		public void dutCommandExecuteStart(){


			ApplicationLauncher.logger.info("dut Command execution Started"  );
			//deviceExecutionStatusDisplayUpdate(ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID, "N",ConstantApp.EXECUTION_STATUS_INPROGRESS);//EXECUTION_STATUS_COMPLETED
			
			int timeduration = DeviceDataManagerController.getDutCommandData().getTotalDutExecutionTimeInSec() ;// 60;//DisplayDataObj.getInfTimeDuration();
			DutSerialDataManager dutSerialDataManager =  new DutSerialDataManager();
			dutSerialDataManager.dutExecuteCommandTrigger();
			setExecuteTimeCounter(timeduration);//+BufferTimeToReadLDU_DataInSec);
			ExecuteTimerDisplay();
			UI_TableRefreshTrigger(timeduration);
			//Sleep(5000);
			//deviceExecutionStatusDisplayUpdate(ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID, "N",ConstantApp.EXECUTION_STATUS_COMPLETED);//EXECUTION_STATUS_COMPLETED
			
		}
		
		

		public void AccuracyExecuteStart(){


			ApplicationLauncher.logger.info("Accuracy Test Started"  );


			int timeduration = DeviceDataManagerController.getInfTimeDuration();

			if(ProcalFeatureEnable.PROPOWER_SRC_ONLY ){

				//LiveTableDataManager.UpdateliveTableData(ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID, "N",ConstantApp.EXECUTION_STATUS_INPROGRESS);//EXECUTION_STATUS_COMPLETED
				deviceExecutionStatusDisplayUpdate(ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID, "N",ConstantApp.EXECUTION_STATUS_INPROGRESS);//EXECUTION_STATUS_COMPLETED
				
			
			}

			DisplayDataObj.setVoltageResetRequired(false);
			FailureManager.ResetPowerSrcReasonForFailure();
			DisplayPwrSrc_TurnOn();
			//WaitForPowerSrcTurnOn();
			//LDU_PreRequisiteForReadError();

			if(ProcalFeatureEnable.LSCS_LDU_CONNECTED) {
				if(!ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED) {
					lscsClearLDU_LastStoredResults();
					lscsClearLduSecondaryDisplay();
					lscsLDU_ResetError();
				}
			}else if(ProcalFeatureEnable.BOFA_LDU_CONNECTED){
				//Sleep(25000);
				Data_LduBofa.bofaSendLduExitDialTest();//bofaSendLduResetErrorAll();
				//Data_LduBofa.bofaSendLduResetErrorAll();
				
			}
			if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
				ApplicationHomeController.updateBottomSecondaryStatus("Awaiting For power source ack..",ConstantApp.LEFT_STATUS_INFO);
				/*			if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_ENABLED){
				setRefStdFeedBackControlAlreadyReceived(false);
			}*/
				WaitForPowerSrcTurnOn();
				if(ProcalFeatureEnable.METRICS_EXCEL_LOG_ENABLE_FEATURE){
					
					long now = Instant.now().toEpochMilli();
					
					DeviceDataManagerController.setMetricsLogTestPointStartingEpochTimeInMSec(now);
					ApplicationLauncher.logger.debug("AccuracyExecuteStart: getPreviousMetricLogEpochTimeInMSec: " + DeviceDataManagerController.getMetricsLogTestPointStartingEpochTimeInMSec());
					
				}
			}else if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){ 
				//DisplayPwrSrc_TurnOn();
				waitBufferTimeForBofaPowerSourceOn();
			}

			if(!ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED) {

				if(FirstLevelValidation()){
					if((!ProcalFeatureEnable.REFSTD_CONNECTED_NONE)){
						if(SecondLevelValidation()){
							//if(ProcalFeatureEnable.LSCS_PWR_SRC_FINE_TUNE_WITH_REF_STD_ENABLED){
							//lscsFineTuneWithRefStd();
							//}

							if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_ENABLED){
								//setRefStdFeedBackControlAlreadyReceived(false);
								//status = refStdFeedBackControl();
								refStdFeedBackControl();
								//waitForTargetCurrentFineTune();
								setRefStdFeedBackControlSuspended(false);
							}

							ApplicationLauncher.logger.info("getTestRunType: " + DeviceDataManagerController.getTestRunType());
							if(ProcalFeatureEnable.CCUBE_LDU_CONNECTED) {
								LDU_PreRequisiteForReadErrorV2();
								//Sleep(30000);

								//DisplayLDU_ReadErrorTimerInit();
								DisplayLDU_ReadErrorTimerInitV2();
							}else if(ProcalFeatureEnable.LSCS_LDU_CONNECTED){
								//lscsClearLDU_LastStoredResults();
								//lscsClearLduSecondaryDisplay();
								

								lscsLDU_PreRequisiteForReadError();
								lscsDisplayLDU_ReadErrorTimerInit();
							}else if(ProcalFeatureEnable.BOFA_LDU_CONNECTED){
								Data_RefStdBofa.setRsmFreqReadingRequired(false);
								
								if(Float.parseFloat(DeviceDataManagerController.getR_PhaseOutputCurrent())> 0.0f ){
									
									Data_RefStdBofa.sendReadConstOfLiveRefStdCmd();  
								}
								
								Data_LduBofa.bofaLDU_PreRequisiteForReadError();
								Data_LduBofa dataLduBofa = new Data_LduBofa();
								dataLduBofa.bofaDisplayLDU_ReadErrorTimerInit();

							}

							setExecuteTimeCounter(timeduration+BufferTimeToReadLDU_DataInSec);
							ExecuteTimerDisplay();
							UI_TableRefreshTrigger(timeduration);
						}
						else{
							SkipCurrentTestPointGUI_Update();
						}

					}else{
						ApplicationLauncher.logger.debug("AccuracyExecuteStart : REFSTD_CONNECTED_NONE" );
						setExecuteTimeCounter(timeduration+BufferTimeToReadLDU_DataInSec);
						ExecuteTimerDisplay();
						UI_TableRefreshTrigger(timeduration);
						if(ProcalFeatureEnable.PROPOWER_SRC_FEEDBACK_DISPLAY ){

							ApplicationLauncher.logger.debug("AccuracyExecuteStart : ProPower Source Feedback display" );
							manipulateGainOffsetValueForFeedBackProcess();
							SerialDM_Obj.lscsPowerSourceReadFeedBackTimerInit();
						}

						if(ProcalFeatureEnable.PROPOWER_SRC_ONLY ){

							//int noOfTestPointCompleted = LiveTableDataManager.getCountOfNoOfTestPointCompleted();
							int totalNoOfTestPointIndex  = getCurrentProjectTestPointList().length()-1;
							ApplicationLauncher.logger.debug("AccuracyExecuteStart : totalNoOfTestPointIndex: " + totalNoOfTestPointIndex );
							ApplicationLauncher.logger.debug("AccuracyExecuteStart : CurrentTestPoint_Index: " + CurrentTestPoint_Index );

							EnableStopButton();
							if(CurrentTestPoint_Index < totalNoOfTestPointIndex){
								enableBtnLoadNext();
							}
						}
					}
				}
				else{
					SkipCurrentTestPointGUI_Update();

					//added on 30-Dec-23 by Gopi for Testing		// testing not jumping to next test point		
					//================================================
					setCurrentTestPoint_ExecutionCompletedStatus(true);
					//semLockExecutionInprogress = false;
					//ApplicationLauncher.logger.info("execute_test: semLockExecutionInprogress: released1");
					IncrementCurrentTestPoint_Index();
					//=====================================================
				}
			}else {
				setExecuteTimeCounter(timeduration+BufferTimeToReadLDU_DataInSec);
				ExecuteTimerDisplay();
				UI_TableRefreshTrigger(timeduration);
				int totalNoOfTestPointIndex  = getCurrentProjectTestPointList().length()-1;
				ApplicationLauncher.logger.debug("AccuracyExecuteStart: totalNoOfTestPointIndex-2: " + totalNoOfTestPointIndex );
				ApplicationLauncher.logger.debug("AccuracyExecuteStart: CurrentTestPoint_Index-2: " + CurrentTestPoint_Index );

				EnableStopButton();
				if(CurrentTestPoint_Index < totalNoOfTestPointIndex){
					enableBtnLoadNext();
				}
			}


		}




		public boolean refStdFeedBackPhaseAngleControl(){
			boolean status = false;
			ApplicationLauncher.logger.debug("refStdFeedBackPhaseAngleControl: Entry"  );
			String userConfiguredSelectedPhaseName = ConstantAppConfig.POWER_SOURCE_SINGLE_PHASE_OUTPUT_SELECTED;
			boolean incrementMode = true;
			boolean decrementMode = false;
			//boolean mode = decrementMode;
			int levelAdjustment = 1;
			if (userConfiguredSelectedPhaseName.equals(ConstantApp.FIRST_PHASE_DISPLAY_NAME)){
				if((Float.parseFloat(DeviceDataManagerController.getR_PhaseOutputCurrent()) != 0f) &&
						(Float.parseFloat(DeviceDataManagerController.getR_PhaseOutputVoltage()) != 0f) ){

					//float degreePercentageAccepted = 1.0f;
					//ApplicationLauncher.logger.info("refStdFeedBackPhaseAngleControl : degreePercentageAccepted:"+ degreePercentageAccepted);

					float expected_pos_value = 0;
					float expected_neg_value = 0;

					float phaseR_Actualdegree = Float.parseFloat(getFeedbackR_phaseDegree());


					float phaseR_TargetDegree = DeviceDataManagerController.get_PwrSrcR_PhaseDegreePhase();
					ApplicationLauncher.logger.debug("refStdFeedBackPhaseAngleControl: phaseR_TargetDegree:" + phaseR_TargetDegree);
					if(phaseR_TargetDegree < 0.0f) {	
						//degreeStr = String.format(ConstantLscsPowerSource.PHASE_RESOLUTION,(360.0f + DeviceDataManagerController.get_PwrSrcR_PhaseDegreePhase()));
						phaseR_TargetDegree = 360.0f + phaseR_TargetDegree;

						ApplicationLauncher.logger.debug("refStdFeedBackPhaseAngleControl: phaseR_TargetDegree2:" + phaseR_TargetDegree);
					}
					//expected_pos_value = phaseR_TargetDegree *((100 + degreePercentageAccepted)/100);
					//expected_neg_value = phaseR_TargetDegree * ((100 - degreePercentageAccepted)/100);

					expected_pos_value = phaseR_TargetDegree + ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_DEGREE_ALLOWED_UPPER_LIMIT;//0.2f;
					expected_neg_value = phaseR_TargetDegree - ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_DEGREE_ALLOWED_LOWER_LIMIT;//0.4f;
					/*if(phaseR_TargetDegree == 0.0f)
				{
					expected_neg_value = phaseR_TargetDegree - 0.3f;
				}else{
					expected_neg_value = phaseR_TargetDegree - 0.3f;
				}*/

					int retryCount = ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_DEGREE_RETRY_MAX_COUNT;//10;//7;
					boolean targetAchieved= false;
					while( (!getUserAbortedFlag()) && (retryCount > 0) && (!targetAchieved) ){

						phaseR_Actualdegree = - Float.parseFloat(getFeedbackR_phaseDegree());
						ApplicationLauncher.logger.info("refStdFeedBackPhaseAngleControl : expected_pos_value:"+ expected_pos_value);
						ApplicationLauncher.logger.info("refStdFeedBackPhaseAngleControl : expected_neg_value:"+ expected_neg_value);
						ApplicationLauncher.logger.debug("refStdFeedBackPhaseAngleControl: phaseR_Actualdegree:" + phaseR_Actualdegree);
						ApplicationLauncher.logger.debug("refStdFeedBackPhaseAngleControl: phaseR_TargetDegree3:" + phaseR_TargetDegree);
						if(phaseR_Actualdegree > 300.0f){
							phaseR_Actualdegree =  phaseR_Actualdegree - 360.0f;
							ApplicationLauncher.logger.debug("refStdFeedBackPhaseAngleControl: phaseR_Actualdegree2:" + phaseR_Actualdegree);

						}
						if((phaseR_Actualdegree <= expected_pos_value)&&(phaseR_Actualdegree >= expected_neg_value)){
							ApplicationLauncher.logger.info("refStdFeedBackPhaseAngleControl : degree phase angle in range");
							targetAchieved = true;
							status = true;
						}else if(phaseR_Actualdegree > expected_pos_value){
							//mode = decrementMode;
							ApplicationLauncher.logger.info("refStdFeedBackPhaseAngleControl : decrementMode");
							SerialDM_Obj.lscsSetPowerSourcePhaseAngleFineTune(userConfiguredSelectedPhaseName,levelAdjustment,decrementMode);
							//Sleep(4500);
							SleepForSecondsWithAbortMonitoring(5);
						}else{
							//mode = incrementMode;
							ApplicationLauncher.logger.info("refStdFeedBackPhaseAngleControl : incrementMode");
							SerialDM_Obj.lscsSetPowerSourcePhaseAngleFineTune(userConfiguredSelectedPhaseName,levelAdjustment,incrementMode);
							//Sleep(4500);
							SleepForSecondsWithAbortMonitoring(5);
						}

						retryCount--;
					}

				}else{
					status = true;
				}
			}else{
				status = true;
			}

			return status;		
		}

		class refStdFeedBackVoltCurrentControl extends TimerTask{


			@Override
			public void run() {

				ApplicationLauncher.logger.debug("refStdFeedBackVoltCurrentControl: Entry");
				if(isRefStdFeedBackControlFlagEnabled()){
					//if(ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED) {
					refStdFeedBackVoltCurrentControlTask();
					//}else{
					//	if(!isRefStdFeedBackControlSuspended()){
					//		sendRefStdDataToLscsPowerSourceTask();
					//	}
					//}
					refStdFeedBackControlTimer.schedule(new refStdFeedBackVoltCurrentControl(), 10);

				}else{
					ApplicationLauncher.logger.debug("refStdFeedBackVoltCurrentControl: refStdFeedBackControlTimer cancelled");
					refStdFeedBackControlTimer.cancel();
				}


			}

		}


		public void refStdFeedBackVoltCurrentControlTask(){

			ApplicationLauncher.logger.debug("refStdFeedBackVoltCurrentControlTask :Entry");
			//ApplicationLauncher.logger.debug("refStdFeedBackVoltCurrentControlTask :getPresentMeterType(): " + getPresentMeterType());
			//ApplicationLauncher.logger.debug("refStdFeedBackVoltCurrentControlTask :getDeployedEM_ModelType(): " + DeviceDataManagerController.getDeployedEM_ModelType());

			;
			boolean status = false;
			status = waitForPowerSrcFeedBackControlReady();
			if(status){


				refStdFeedBackVoltControlV2(ConstantApp.FIRST_PHASE_DISPLAY_NAME);
				if(getPresentMeterType().contains(ConstantApp.METERTYPE_SINGLEPHASE)){
					SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
					SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
				}else{

					refStdFeedBackVoltControlV2(ConstantApp.SECOND_PHASE_DISPLAY_NAME);
					refStdFeedBackVoltControlV2(ConstantApp.THIRD_PHASE_DISPLAY_NAME);
				}
				//int levelAdjustment =1;
				//boolean noChange = true;


				// for current
				refStdFeedBackCurrentControlV2(ConstantApp.FIRST_PHASE_DISPLAY_NAME);
				if(getPresentMeterType().contains(ConstantApp.METERTYPE_SINGLEPHASE)){
					SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
					SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
				}else{
					//SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
					//SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
					//SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
					refStdFeedBackCurrentControlV2(ConstantApp.SECOND_PHASE_DISPLAY_NAME);
					refStdFeedBackCurrentControlV2(ConstantApp.THIRD_PHASE_DISPLAY_NAME);
				}


				// for pf change

				//SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange(); // for R phase 
				if((ProCalCustomerConfiguration.SANDS_HYBRID_40_POSITION_2021)  
						|| (ProCalCustomerConfiguration.DEVSYS_CONVEYOR_VERIFIC_1PHASE_POSITION_2024)
						){
					//||  (ProCalCustomerConfiguration.ADYA_HYBRID_3NO_3PHASE_6NO_1PHASE_POSITION_2024)){
					SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
					SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
					SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
				}else{

					if(((Float.parseFloat(DeviceDataManagerController.getR_PhaseOutputVoltage()) != 0f)  && (Float.parseFloat(DeviceDataManagerController.getR_PhaseOutputCurrent()) != 0f) ) ){
						refStdFeedBackR_PhaseDegreePhaseAngleControl();
					}else {
						SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
					}

					if(getPresentMeterType().contains(ConstantApp.METERTYPE_SINGLEPHASE)){
						SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange(); // for Y phase 
						SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange(); // for B phase 
					}else{
						//SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange(); // for Y phase 
						//SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange(); // for B phase 
						if(((Float.parseFloat(DeviceDataManagerController.getY_PhaseOutputVoltage()) != 0f)  && (Float.parseFloat(DeviceDataManagerController.getY_PhaseOutputCurrent()) != 0f) ) ){
							refStdFeedBackY_PhaseDegreePhaseAngleControl();
						}else {
							SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
						}
						if(((Float.parseFloat(DeviceDataManagerController.getB_PhaseOutputVoltage()) != 0f)  && (Float.parseFloat(DeviceDataManagerController.getB_PhaseOutputCurrent()) != 0f) ) ){
							refStdFeedBackB_PhaseDegreePhaseAngleControl();
						}else {
							SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
						}
					}

				}

				/*			refStdFeedBackR_PhaseDegreePhaseAngleControl();

			if(getPresentMeterType().equals(ConstantApp.METERTYPE_SINGLEPHASE)){
				SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange(); // for Y phase 
				SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange(); // for B phase 
			}else{
				//SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange(); // for Y phase 
				//SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange(); // for B phase 
				refStdFeedBackY_PhaseDegreePhaseAngleControl();
				refStdFeedBackB_PhaseDegreePhaseAngleControl();
			}*/
				//SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange(ConstantApp.FIRST_PHASE_DISPLAY_NAME,levelAdjustment,noChange);
				//SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange(ConstantApp.FIRST_PHASE_DISPLAY_NAME,levelAdjustment,noChange);
				//SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange(ConstantApp.FIRST_PHASE_DISPLAY_NAME,levelAdjustment,noChange);

				//refStdFeedBackCurrentControl();//ConstantApp.FIRST_PHASE_DISPLAY_NAME);
				//refStdFeedBackCurrentControl();//(ConstantApp.SECOND_PHASE_DISPLAY_NAME);
				//refStdFeedBackCurrentControl();//(ConstantApp.THIRD_PHASE_DISPLAY_NAME);
				//Sleep(4000);
				//Sleep(5000);
				int retryCount = 5;//10;//5;
				while( (!getUserAbortedFlag()) && (retryCount > 0) ){
					ApplicationLauncher.logger.debug("refStdFeedBackVoltCurrentControlTask : retryCount: " + retryCount);

					Sleep(1000);
					retryCount--;
				}
				ApplicationLauncher.logger.debug("refStdFeedBackVoltCurrentControlTask : while exit: " );

			}else{
				ApplicationLauncher.logger.debug("refStdFeedBackVoltCurrentControlTask : PowerSrcFeedBackControl not Ready");
			}
			ApplicationLauncher.logger.debug("refStdFeedBackVoltCurrentControlTask : Exit" );
			ApplicationLauncher.logger.debug("refStdFeedBackVoltCurrentControlTask : ======================================" );
			ApplicationLauncher.logger.debug("refStdFeedBackVoltCurrentControlTask : ======================================" );

		}

		public boolean refStdFeedBackVoltControl(String selectedPhaseName){
			boolean status = false;
			ApplicationLauncher.logger.debug("refStdFeedBackVoltControl: Entry"  );
			//String userConfiguredSelectedPhaseName = ConstantAppConfig.POWER_SOURCE_SINGLE_PHASE_OUTPUT_SELECTED;
			//String selectedPhaseName = ConstantAppConfig.POWER_SOURCE_SINGLE_PHASE_OUTPUT_SELECTED;
			boolean incrementMode = true;
			boolean decrementMode = false;
			//boolean mode = decrementMode;
			float expected_pos_value = 0;
			float expected_neg_value = 0;
			int levelAdjustment = 1;
			if (selectedPhaseName.equals(ConstantApp.FIRST_PHASE_DISPLAY_NAME)){
				if((Float.parseFloat(DeviceDataManagerController.getR_PhaseOutputVoltage()) != 0f) ){

					//float degreePercentageAccepted = 1.0f;
					//ApplicationLauncher.logger.info("refStdFeedBackPhaseAngleControl : degreePercentageAccepted:"+ degreePercentageAccepted);

					//float expected_pos_value = 0;
					//float expected_neg_value = 0;

					float phaseR_ActualVolt = Float.parseFloat(getFeedbackR_phaseVolt());


					float phaseR_TargetVolt = Float.parseFloat(DeviceDataManagerController.getR_PhaseOutputVoltage());

					expected_pos_value = phaseR_TargetVolt + ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_UPPER_LIMIT_PERCENT;//0.2f;
					expected_neg_value = phaseR_TargetVolt - ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_LOWER_LIMIT_PERCENT;//0.4f;

					//while( (!getUserAbortedFlag()) && (retryCount > 0) && (!targetAchieved) ){
					if(!getUserAbortedFlag()){
						phaseR_ActualVolt =  Float.parseFloat(getFeedbackR_phaseVolt());
						ApplicationLauncher.logger.debug("refStdFeedBackVoltControl: phaseR_TargetVolt:" + phaseR_TargetVolt);
						ApplicationLauncher.logger.info("refStdFeedBackVoltControl : expected_pos_value:"+ expected_pos_value);
						ApplicationLauncher.logger.info("refStdFeedBackVoltControl : expected_neg_value:"+ expected_neg_value);
						ApplicationLauncher.logger.debug("refStdFeedBackVoltControl: phaseR_ActualVolt:" + phaseR_ActualVolt);

						/*if(phaseR_Actualdegree > 300.0f){
						phaseR_Actualdegree =  phaseR_Actualdegree - 360.0f;
						ApplicationLauncher.logger.debug("refStdFeedBackVoltControl: phaseR_Actualdegree2:" + phaseR_Actualdegree);

					}*/
						if((phaseR_ActualVolt <= expected_pos_value)&&(phaseR_ActualVolt >= expected_neg_value)){
							ApplicationLauncher.logger.info("refStdFeedBackVoltControl : volt in range");
							status = true;
						}else if(phaseR_ActualVolt > expected_pos_value){
							//mode = decrementMode;
							ApplicationLauncher.logger.info("refStdFeedBackVoltControl : decrementMode");
							SerialDM_Obj.lscsSetPowerSourceVoltFineTune(selectedPhaseName,levelAdjustment,decrementMode);
							//Sleep(4500);
						}else{
							//mode = incrementMode;
							ApplicationLauncher.logger.info("refStdFeedBackVoltControl : incrementMode");
							SerialDM_Obj.lscsSetPowerSourceVoltFineTune(selectedPhaseName,levelAdjustment,incrementMode);
							//Sleep(4500);
						}


						//retryCount--;
					}

				}else{
					status = true;
				}
			}else if (selectedPhaseName.equals(ConstantApp.SECOND_PHASE_DISPLAY_NAME)){
				if((Float.parseFloat(DeviceDataManagerController.getY_PhaseOutputVoltage()) != 0f) ){

					//float degreePercentageAccepted = 1.0f;
					//ApplicationLauncher.logger.info("refStdFeedBackPhaseAngleControl : degreePercentageAccepted:"+ degreePercentageAccepted);

					//float expected_pos_value = 0;
					//float expected_neg_value = 0;

					float phaseY_ActualVolt = Float.parseFloat(getFeedbackY_phaseVolt());


					float phaseY_TargetVolt = Float.parseFloat(DeviceDataManagerController.getY_PhaseOutputVoltage());

					expected_pos_value = phaseY_TargetVolt + ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_UPPER_LIMIT_PERCENT;//0.2f;
					expected_neg_value = phaseY_TargetVolt - ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_LOWER_LIMIT_PERCENT;//0.4f;

					//while( (!getUserAbortedFlag()) && (retryCount > 0) && (!targetAchieved) ){
					if(!getUserAbortedFlag()){
						phaseY_ActualVolt =  Float.parseFloat(getFeedbackY_phaseVolt());
						ApplicationLauncher.logger.debug("refStdFeedBackVoltControl: phaseY_TargetVolt:" + phaseY_TargetVolt);
						ApplicationLauncher.logger.info("refStdFeedBackVoltControl : expected_pos_value:"+ expected_pos_value);
						ApplicationLauncher.logger.info("refStdFeedBackVoltControl : expected_neg_value:"+ expected_neg_value);
						ApplicationLauncher.logger.debug("refStdFeedBackVoltControl: phaseY_ActualVolt:" + phaseY_ActualVolt);

						/*if(phaseR_Actualdegree > 300.0f){
						phaseR_Actualdegree =  phaseR_Actualdegree - 360.0f;
						ApplicationLauncher.logger.debug("refStdFeedBackVoltControl: phaseR_Actualdegree2:" + phaseR_Actualdegree);

					}*/
						if((phaseY_ActualVolt <= expected_pos_value)&&(phaseY_ActualVolt >= expected_neg_value)){
							ApplicationLauncher.logger.info("refStdFeedBackVoltControl : volt in range");
							status = true;
						}else if(phaseY_ActualVolt > expected_pos_value){
							//mode = decrementMode;
							ApplicationLauncher.logger.info("refStdFeedBackVoltControl : decrementMode");
							SerialDM_Obj.lscsSetPowerSourceVoltFineTune(selectedPhaseName,levelAdjustment,decrementMode);
							//Sleep(4500);
						}else{
							//mode = incrementMode;
							ApplicationLauncher.logger.info("refStdFeedBackVoltControl : incrementMode");
							SerialDM_Obj.lscsSetPowerSourceVoltFineTune(selectedPhaseName,levelAdjustment,incrementMode);
							//Sleep(4500);
						}


						//retryCount--;
					}

				}else{
					status = true;
				}
			}else if (selectedPhaseName.equals(ConstantApp.THIRD_PHASE_DISPLAY_NAME)){
				if((Float.parseFloat(DeviceDataManagerController.getB_PhaseOutputVoltage()) != 0f) ){

					//float degreePercentageAccepted = 1.0f;
					//ApplicationLauncher.logger.info("refStdFeedBackPhaseAngleControl : degreePercentageAccepted:"+ degreePercentageAccepted);

					//float expected_pos_value = 0;
					//float expected_neg_value = 0;

					float phaseB_ActualVolt = Float.parseFloat(getFeedbackB_phaseVolt());


					float phaseB_TargetVolt = Float.parseFloat(DeviceDataManagerController.getB_PhaseOutputVoltage());

					expected_pos_value = phaseB_TargetVolt + ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_UPPER_LIMIT_PERCENT;//0.2f;
					expected_neg_value = phaseB_TargetVolt - ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_LOWER_LIMIT_PERCENT;//0.4f;

					//while( (!getUserAbortedFlag()) && (retryCount > 0) && (!targetAchieved) ){
					if(!getUserAbortedFlag()){
						phaseB_ActualVolt =  Float.parseFloat(getFeedbackB_phaseVolt());
						ApplicationLauncher.logger.debug("refStdFeedBackVoltControl: phaseB_TargetVolt:" + phaseB_TargetVolt);
						ApplicationLauncher.logger.info("refStdFeedBackVoltControl : expected_pos_value:"+ expected_pos_value);
						ApplicationLauncher.logger.info("refStdFeedBackVoltControl : expected_neg_value:"+ expected_neg_value);
						ApplicationLauncher.logger.debug("refStdFeedBackVoltControl: phaseB_ActualVolt:" + phaseB_ActualVolt);

						/*if(phaseR_Actualdegree > 300.0f){
						phaseR_Actualdegree =  phaseR_Actualdegree - 360.0f;
						ApplicationLauncher.logger.debug("refStdFeedBackVoltControl: phaseR_Actualdegree2:" + phaseR_Actualdegree);

					}*/
						if((phaseB_ActualVolt <= expected_pos_value)&&(phaseB_ActualVolt >= expected_neg_value)){
							ApplicationLauncher.logger.info("refStdFeedBackVoltControl : volt in range");
							status = true;
						}else if(phaseB_ActualVolt > expected_pos_value){
							//mode = decrementMode;
							ApplicationLauncher.logger.info("refStdFeedBackVoltControl : decrementMode");
							SerialDM_Obj.lscsSetPowerSourceVoltFineTune(selectedPhaseName,levelAdjustment,decrementMode);
							//Sleep(4500);
						}else{
							//mode = incrementMode;
							ApplicationLauncher.logger.info("refStdFeedBackVoltControl : incrementMode");
							SerialDM_Obj.lscsSetPowerSourceVoltFineTune(selectedPhaseName,levelAdjustment,incrementMode);
							//Sleep(4500);
						}


						//retryCount--;
					}

				}else{
					status = true;
				}
			}else{
				status = true;
			}

			return status;		
		}


		public boolean refStdFeedBackVoltControlV2(String selectedPhaseName){
			boolean status = false;
			ApplicationLauncher.logger.debug("refStdFeedBackVoltControlV2: Entry"  );
			//boolean mode = decrementMode;
			float expectedUpperLimitValue = 0;
			float expectedLowerLimitValue = 0;
			if (selectedPhaseName.equals(ConstantApp.FIRST_PHASE_DISPLAY_NAME)){
				String voltFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_DEFAULT_MULTIPLIER;

				if((Float.parseFloat(DeviceDataManagerController.getR_PhaseOutputVoltage()) != 0f) ){

					//float degreePercentageAccepted = 1.0f;
					//ApplicationLauncher.logger.info("refStdFeedBackPhaseAngleControl : degreePercentageAccepted:"+ degreePercentageAccepted);

					//float expected_pos_value = 0;
					//float expected_neg_value = 0;

					float phaseR_ActualVolt = Float.parseFloat(getFeedbackR_phaseVolt());


					float phaseR_TargetVolt = Float.parseFloat(DeviceDataManagerController.getR_PhaseOutputVoltage());
					//expected_pos_value = input_value * ((100 + acc_percent) / 100);
					//expectedUpperLimitValue = phaseR_TargetVolt + ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_UPPER_LIMIT_PERCENT;//0.2f;
					//expectedLowerLimitValue = phaseR_TargetVolt - ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_LOWER_LIMIT_PERCENT;//0.4f;
					//expectedUpperLimitValue = phaseR_TargetVolt * ((100 + ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_UPPER_LIMIT_PERCENT) / 100); //+ ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_UPPER_LIMIT_PERCENT;//0.2f;
					//expectedLowerLimitValue = phaseR_TargetVolt * ((100 - ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_LOWER_LIMIT_PERCENT) / 100); //- ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_LOWER_LIMIT_PERCENT;//0.4f;

					//expected_pos_value = phaseR_TargetCurrent + (phaseR_TargetCurrent*(currentFineTunePercentageAccepted/100));//0.2f;
					//expected_neg_value = phaseR_TargetCurrent - (phaseR_TargetCurrent*(currentFineTunePercentageAccepted/100));//0.4f;
					expectedUpperLimitValue = phaseR_TargetVolt + (phaseR_TargetVolt * (ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_UPPER_LIMIT_PERCENT / 100)); //+ ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_UPPER_LIMIT_PERCENT;//0.2f;
					expectedLowerLimitValue = phaseR_TargetVolt - (phaseR_TargetVolt * (ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_LOWER_LIMIT_PERCENT / 100)); //- ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_LOWER_LIMIT_PERCENT;//0.4f;

					ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 : expectedUpperLimitValue1: "+ expectedUpperLimitValue);
					ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 : expectedLowerLimitValue1: "+ expectedLowerLimitValue);

					//expectedUpperLimitValue = expectedUpperLimitValue - 0.05f;
					//expectedLowerLimitValue = expectedLowerLimitValue + 0.03f;

					//expectedUpperLimitValue = Float.parseFloat(String.format(ConstantApp.DISPLAY_VOLTAGE_RESOLUTION, expectedUpperLimitValue));
					//expectedLowerLimitValue = Float.parseFloat(String.format(ConstantApp.DISPLAY_VOLTAGE_RESOLUTION, expectedLowerLimitValue));



					float diffValue = 0;
					//while( (!getUserAbortedFlag()) && (retryCount > 0) && (!targetAchieved) ){
					if(!getUserAbortedFlag()){

						phaseR_ActualVolt =  Float.parseFloat(getFeedbackR_phaseVolt());
						ApplicationLauncher.logger.debug("refStdFeedBackVoltControlV2: phaseR_TargetVolt:" + phaseR_TargetVolt);
						ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 : expectedUpperLimitValue2:"+ expectedUpperLimitValue);
						ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 : expectedLowerLimitValue2:"+ expectedLowerLimitValue);
						ApplicationLauncher.logger.debug("refStdFeedBackVoltControlV2: phaseR_ActualVolt:" + phaseR_ActualVolt);

						
						if((phaseR_ActualVolt <= expectedUpperLimitValue)&&(phaseR_ActualVolt >= expectedLowerLimitValue)){
							ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 : volt in range");
							status = true;
							lastPowerSource_R_VoltDecrement = false;
							lastPowerSource_R_VoltIncrement = false;
							feedbackAdjustmentDoubleConfirmed_R_VoltIncrement = false;
							feedbackAdjustmentDoubleConfirmed_R_VoltDecrement = false;
							SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
						}else if(phaseR_ActualVolt > expectedUpperLimitValue){
							//mode = decrementMode;
							ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 : decrementMode");
							//SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantLscsPowerSource.CMD_PWR_SRC_FINE_CONTROL_V1_DEC_LEVEL1);

							if(lastPowerSource_R_VoltDecrement){
								lastPowerSource_R_VoltDecrement = false;
								ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 Rphase: Volt sent NoChange-D1");
								SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
							}else {
								if(feedbackAdjustmentDoubleConfirmed_R_VoltDecrement){
									ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 Rphase: decrementMode-B");
									if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_MULTIPLIER_ENABLED) {
										diffValue = phaseR_ActualVolt  - expectedUpperLimitValue;
										if(diffValue>0.2){
											voltFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_4;//2;

										}else if(diffValue>0.1){
											voltFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_3;
										}else{
											voltFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_2;
										}
										//voltFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_1;//2;
										SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_V1_DEC_LEVEL1 + String.valueOf(voltFeedBackRphaseMultiplierValue));
									}else {
										SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_V1_DEC_LEVEL1);
									}
									lastPowerSource_R_VoltDecrement = true;
									feedbackAdjustmentDoubleConfirmed_R_VoltDecrement = false;
								}else {
									ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 Rphase: Volt sent NoChange-D2");
									SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
									feedbackAdjustmentDoubleConfirmed_R_VoltDecrement = true;
								}
							}


						}else{
							//mode = incrementMode;
							ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 : incrementMode");
							//SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantLscsPowerSource.CMD_PWR_SRC_FINE_CONTROL_V1_INC_LEVEL1);

							if(lastPowerSource_R_VoltIncrement){
								lastPowerSource_R_VoltIncrement = false;
								ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 Rphase: Volt sent NoChange-F1");

								SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
							}else {

								if(feedbackAdjustmentDoubleConfirmed_R_VoltIncrement){


									ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 Rphase: incrementMode-B");
									if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_MULTIPLIER_ENABLED) {
										diffValue = expectedUpperLimitValue - phaseR_ActualVolt  ;
										if(diffValue>0.2){
											voltFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_4;//2;

										}else if(diffValue>0.1){
											voltFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_3;
										}else{
											voltFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_2;
										}
										//voltFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_1;
										SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_V1_INC_LEVEL1 + String.valueOf(voltFeedBackRphaseMultiplierValue));
									}else {
										SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_V1_INC_LEVEL1);
									}

									lastPowerSource_R_VoltIncrement = true;
									feedbackAdjustmentDoubleConfirmed_R_VoltIncrement = false;
								}else {
									ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 Rphase: Volt sent NoChange-F2");
									SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
									feedbackAdjustmentDoubleConfirmed_R_VoltIncrement = true;
								}
							}

						}
						setLastRefStdFeedBackRead_R_PhaseVolt(phaseR_ActualVolt);

						//retryCount--;
					}

				}else{
					//status = true;
					ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 : Rphase volt zero");
					status = true;
					SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
				}
			}else if (selectedPhaseName.equals(ConstantApp.SECOND_PHASE_DISPLAY_NAME)){
				if((Float.parseFloat(DeviceDataManagerController.getY_PhaseOutputVoltage()) != 0f) ){
					int voltFeedBackYphaseMultiplierValue = Integer.parseInt(ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_DEFAULT_MULTIPLIER);


					//float degreePercentageAccepted = 1.0f;
					//ApplicationLauncher.logger.info("refStdFeedBackPhaseAngleControl : degreePercentageAccepted:"+ degreePercentageAccepted);

					//float expectedUpperLimitValue = 0;
					//float expectedLowerLimitValue = 0;

					float phaseY_ActualVolt = Float.parseFloat(getFeedbackY_phaseVolt());


					float phaseY_TargetVolt = Float.parseFloat(DeviceDataManagerController.getY_PhaseOutputVoltage());

					//expectedUpperLimitValue = phaseY_TargetVolt + ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_UPPER_LIMIT_PERCENT;//0.2f;
					//expectedLowerLimitValue = phaseY_TargetVolt - ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_LOWER_LIMIT_PERCENT;//0.4f;

					expectedUpperLimitValue = phaseY_TargetVolt + (phaseY_TargetVolt * (ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_UPPER_LIMIT_PERCENT / 100)); //+ ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_UPPER_LIMIT_PERCENT;//0.2f;
					expectedLowerLimitValue = phaseY_TargetVolt - (phaseY_TargetVolt * (ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_LOWER_LIMIT_PERCENT / 100)); //- ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_LOWER_LIMIT_PERCENT;//0.4f;


					//while( (!getUserAbortedFlag()) && (retryCount > 0) && (!targetAchieved) ){
					if(!getUserAbortedFlag()){
						phaseY_ActualVolt =  Float.parseFloat(getFeedbackY_phaseVolt());
						ApplicationLauncher.logger.debug("refStdFeedBackVoltControlV2: phaseY_TargetVolt:" + phaseY_TargetVolt);
						ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 : expectedUpperLimitValue:"+ expectedUpperLimitValue);
						ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 : expectedLowerLimitValue:"+ expectedLowerLimitValue);
						ApplicationLauncher.logger.debug("refStdFeedBackVoltControlV2: phaseY_ActualVolt:" + phaseY_ActualVolt);

						
						if((phaseY_ActualVolt <= expectedUpperLimitValue)&&(phaseY_ActualVolt >= expectedLowerLimitValue)){
							ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 : volt in range");
							status = true;
							lastPowerSource_Y_VoltDecrement = false;
							lastPowerSource_Y_VoltIncrement = false;
							SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
						}else if(phaseY_ActualVolt > expectedUpperLimitValue){
							//mode = decrementMode;
							ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 : decrementMode");

							//SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantLscsPowerSource.CMD_PWR_SRC_FINE_CONTROL_V2_DEC_LEVEL1);






							if(lastPowerSource_Y_VoltDecrement){
								lastPowerSource_Y_VoltDecrement = false;
								ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 Yphase: Volt sent NoChange-D1");
								SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
							}else {
								ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 Yphase: decrementMode-B");
								if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_MULTIPLIER_ENABLED) {

									SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_V2_DEC_LEVEL1 + String.valueOf(voltFeedBackYphaseMultiplierValue));
								}else {
									SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_V2_DEC_LEVEL1);
								}
								lastPowerSource_Y_VoltDecrement = true;
							}



						}else{
							//mode = incrementMode;
							ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 : incrementMode");
							//SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantLscsPowerSource.CMD_PWR_SRC_FINE_CONTROL_V2_INC_LEVEL1);


							if(lastPowerSource_Y_VoltIncrement){
								lastPowerSource_Y_VoltIncrement = false;
								ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 Yphase: Volt sent NoChange-F1");

								SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
							}else {
								ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 Yphase: incrementMode-B");
								if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_MULTIPLIER_ENABLED) {

									SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_V2_INC_LEVEL1 + String.valueOf(voltFeedBackYphaseMultiplierValue));
								}else {
									SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_V2_INC_LEVEL1);
								}
								lastPowerSource_Y_VoltIncrement = true;
							}


						}

						setLastRefStdFeedBackRead_Y_PhaseVolt(phaseY_ActualVolt);
						//retryCount--;
					}

				}else{
					//status = true;
					ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 : Yphase volt zero");
					status = true;
					SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
				}
			}else if (selectedPhaseName.equals(ConstantApp.THIRD_PHASE_DISPLAY_NAME)){
				if((Float.parseFloat(DeviceDataManagerController.getB_PhaseOutputVoltage()) != 0f) ){
					int voltFeedBackBphaseMultiplierValue = Integer.parseInt(ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_DEFAULT_MULTIPLIER);

					//float degreePercentageAccepted = 1.0f;
					//ApplicationLauncher.logger.info("refStdFeedBackPhaseAngleControl : degreePercentageAccepted:"+ degreePercentageAccepted);

					//float expectedUpperLimitValue = 0;
					//float expectedLowerLimitValue = 0;

					float phaseB_ActualVolt = Float.parseFloat(getFeedbackB_phaseVolt());


					float phaseB_TargetVolt = Float.parseFloat(DeviceDataManagerController.getB_PhaseOutputVoltage());

					//expectedUpperLimitValue = phaseB_TargetVolt + ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_UPPER_LIMIT_PERCENT;//0.2f;
					//expectedLowerLimitValue = phaseB_TargetVolt - ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_LOWER_LIMIT_PERCENT;//0.4f;

					expectedUpperLimitValue = phaseB_TargetVolt + (phaseB_TargetVolt * (ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_UPPER_LIMIT_PERCENT / 100)); //+ ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_UPPER_LIMIT_PERCENT;//0.2f;
					expectedLowerLimitValue = phaseB_TargetVolt - (phaseB_TargetVolt * (ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_LOWER_LIMIT_PERCENT / 100)); //- ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_LOWER_LIMIT_PERCENT;//0.4f;


					//while( (!getUserAbortedFlag()) && (retryCount > 0) && (!targetAchieved) ){
					if(!getUserAbortedFlag()){
						phaseB_ActualVolt =  Float.parseFloat(getFeedbackB_phaseVolt());
						ApplicationLauncher.logger.debug("refStdFeedBackVoltControlV2: phaseB_TargetVolt:" + phaseB_TargetVolt);
						ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 : expectedUpperLimitValue:"+ expectedUpperLimitValue);
						ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 : expectedLowerLimitValue:"+ expectedLowerLimitValue);
						ApplicationLauncher.logger.debug("refStdFeedBackVoltControlV2: phaseB_ActualVolt:" + phaseB_ActualVolt);

						
						if((phaseB_ActualVolt <= expectedUpperLimitValue)&&(phaseB_ActualVolt >= expectedLowerLimitValue)){
							ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 : volt in range");
							status = true;

							lastPowerSource_B_VoltDecrement = false;
							lastPowerSource_B_VoltIncrement = false;
							SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
						}else if(phaseB_ActualVolt > expectedUpperLimitValue){
							//mode = decrementMode;
							ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 : decrementMode");
							//SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantLscsPowerSource.CMD_PWR_SRC_FINE_CONTROL_V3_DEC_LEVEL1);
							if(lastPowerSource_B_VoltDecrement){
								lastPowerSource_B_VoltDecrement = false;
								ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 Bphase: Volt sent NoChange-D1");
								SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
							}else {
								ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 Bphase: decrementMode-B");
								if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_MULTIPLIER_ENABLED) {

									SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_V3_DEC_LEVEL1 + String.valueOf(voltFeedBackBphaseMultiplierValue));
								}else {
									SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_V3_DEC_LEVEL1);
								}
								lastPowerSource_B_VoltDecrement = true;
							}
						}else{
							//mode = incrementMode;
							ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 : incrementMode");
							//SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantLscsPowerSource.CMD_PWR_SRC_FINE_CONTROL_V3_INC_LEVEL1);

							if(lastPowerSource_B_VoltIncrement){
								lastPowerSource_B_VoltIncrement = false;
								ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 Bphase: Volt sent NoChange-F1");

								SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
							}else {
								ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 Bphase: incrementMode-B");
								if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_MULTIPLIER_ENABLED) {

									SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_V3_INC_LEVEL1 + String.valueOf(voltFeedBackBphaseMultiplierValue));
								}else {
									SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_V3_INC_LEVEL1);
								}
								lastPowerSource_B_VoltIncrement = true;
							}
						}

						setLastRefStdFeedBackRead_B_PhaseVolt(phaseB_ActualVolt);


						//retryCount--;
					}

				}else{
					//status = true;
					ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 : Rphase volt zero");
					status = true;
					SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
				}
			}else{
				status = true;
			}

			return status;		
		}

		
		/*
		public boolean refStdFeedBackVoltControlV2(String selectedPhaseName){
			boolean status = false;
			ApplicationLauncher.logger.debug("refStdFeedBackVoltControlV2: Entry"  );
			//String userConfiguredSelectedPhaseName = ConstantAppConfig.POWER_SOURCE_SINGLE_PHASE_OUTPUT_SELECTED;
			//String selectedPhaseName = ConstantAppConfig.POWER_SOURCE_SINGLE_PHASE_OUTPUT_SELECTED;
			boolean incrementMode = true;
			boolean decrementMode = false;
			//boolean mode = decrementMode;
			float expectedUpperLimitValue = 0;
			float expectedLowerLimitValue = 0;
			int levelAdjustment = 1;
			if (selectedPhaseName.equals(ConstantApp.FIRST_PHASE_DISPLAY_NAME)){
				if((Float.parseFloat(DisplayDataObj.getR_PhaseOutputVoltage()) != 0f) ){

					//float degreePercentageAccepted = 1.0f;
					//ApplicationLauncher.logger.info("refStdFeedBackPhaseAngleControl : degreePercentageAccepted:"+ degreePercentageAccepted);

					//float expected_pos_value = 0;
					//float expected_neg_value = 0;

					float phaseR_ActualVolt = Float.parseFloat(getFeedbackR_phaseVolt());


					float phaseR_TargetVolt = Float.parseFloat(DisplayDataObj.getR_PhaseOutputVoltage());
					//expected_pos_value = input_value * ((100 + acc_percent) / 100);
					//expectedUpperLimitValue = phaseR_TargetVolt + ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_UPPER_LIMIT_PERCENT;//0.2f;
					//expectedLowerLimitValue = phaseR_TargetVolt - ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_LOWER_LIMIT_PERCENT;//0.4f;
					//expectedUpperLimitValue = phaseR_TargetVolt * ((100 + ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_UPPER_LIMIT_PERCENT) / 100); //+ ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_UPPER_LIMIT_PERCENT;//0.2f;
					//expectedLowerLimitValue = phaseR_TargetVolt * ((100 - ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_LOWER_LIMIT_PERCENT) / 100); //- ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_LOWER_LIMIT_PERCENT;//0.4f;

					//expected_pos_value = phaseR_TargetCurrent + (phaseR_TargetCurrent*(currentFineTunePercentageAccepted/100));//0.2f;
					//expected_neg_value = phaseR_TargetCurrent - (phaseR_TargetCurrent*(currentFineTunePercentageAccepted/100));//0.4f;
					expectedUpperLimitValue = phaseR_TargetVolt + (phaseR_TargetVolt * (ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_UPPER_LIMIT_PERCENT / 100)); //+ ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_UPPER_LIMIT_PERCENT;//0.2f;
					expectedLowerLimitValue = phaseR_TargetVolt - (phaseR_TargetVolt * (ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_LOWER_LIMIT_PERCENT / 100)); //- ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_LOWER_LIMIT_PERCENT;//0.4f;


					//expectedUpperLimitValue = Float.parseFloat(String.format(ConstantApp.DISPLAY_VOLTAGE_RESOLUTION, expectedUpperLimitValue));
					//expectedLowerLimitValue = Float.parseFloat(String.format(ConstantApp.DISPLAY_VOLTAGE_RESOLUTION, expectedLowerLimitValue));



					boolean targetAchieved= false;
					//while( (!getUserAbortedFlag()) && (retryCount > 0) && (!targetAchieved) ){
					if(!getUserAbortedFlag()){
						phaseR_ActualVolt =  Float.parseFloat(getFeedbackR_phaseVolt());
						ApplicationLauncher.logger.debug("refStdFeedBackVoltControlV2: phaseR_TargetVolt:" + phaseR_TargetVolt);
						ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 : expectedUpperLimitValue:"+ expectedUpperLimitValue);
						ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 : expectedLowerLimitValue:"+ expectedLowerLimitValue);
						ApplicationLauncher.logger.debug("refStdFeedBackVoltControlV2: phaseR_ActualVolt:" + phaseR_ActualVolt);

						
						if((phaseR_ActualVolt <= expectedUpperLimitValue)&&(phaseR_ActualVolt >= expectedLowerLimitValue)){
							ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 : volt in range");
							targetAchieved = true;
							status = true;
							SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
						}else if(phaseR_ActualVolt > expectedUpperLimitValue){
							//mode = decrementMode;
							ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 : decrementMode");
							//SerialDM_Obj.lscsSetPowerSourceVoltFineTune(selectedPhaseName,levelAdjustment,decrementMode);
							SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_V1_DEC_LEVEL1);
							//Sleep(4500);
						}else{
							//mode = incrementMode;
							ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 : incrementMode");
							//SerialDM_Obj.lscsSetPowerSourceVoltFineTune(selectedPhaseName,levelAdjustment,incrementMode);
							SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_V1_INC_LEVEL1);

							//Sleep(4500);
						}


						//retryCount--;
					}

				}else{
					//status = true;
					ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 : Rphase volt zero");
					status = true;
					SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
				}
			}else if (selectedPhaseName.equals(ConstantApp.SECOND_PHASE_DISPLAY_NAME)){
				if((Float.parseFloat(DisplayDataObj.getY_PhaseOutputVoltage()) != 0f) ){

					//float degreePercentageAccepted = 1.0f;
					//ApplicationLauncher.logger.info("refStdFeedBackPhaseAngleControl : degreePercentageAccepted:"+ degreePercentageAccepted);

					//float expectedUpperLimitValue = 0;
					//float expectedLowerLimitValue = 0;

					float phaseY_ActualVolt = Float.parseFloat(getFeedbackY_phaseVolt());


					float phaseY_TargetVolt = Float.parseFloat(DisplayDataObj.getY_PhaseOutputVoltage());

					//expectedUpperLimitValue = phaseY_TargetVolt + ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_UPPER_LIMIT_PERCENT;//0.2f;
					//expectedLowerLimitValue = phaseY_TargetVolt - ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_LOWER_LIMIT_PERCENT;//0.4f;

					expectedUpperLimitValue = phaseY_TargetVolt + (phaseY_TargetVolt * (ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_UPPER_LIMIT_PERCENT / 100)); //+ ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_UPPER_LIMIT_PERCENT;//0.2f;
					expectedLowerLimitValue = phaseY_TargetVolt - (phaseY_TargetVolt * (ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_LOWER_LIMIT_PERCENT / 100)); //- ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_LOWER_LIMIT_PERCENT;//0.4f;


					boolean targetAchieved= false;
					//while( (!getUserAbortedFlag()) && (retryCount > 0) && (!targetAchieved) ){
					if(!getUserAbortedFlag()){
						phaseY_ActualVolt =  Float.parseFloat(getFeedbackY_phaseVolt());
						ApplicationLauncher.logger.debug("refStdFeedBackVoltControlV2: phaseY_TargetVolt:" + phaseY_TargetVolt);
						ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 : expectedUpperLimitValue:"+ expectedUpperLimitValue);
						ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 : expectedLowerLimitValue:"+ expectedLowerLimitValue);
						ApplicationLauncher.logger.debug("refStdFeedBackVoltControlV2: phaseY_ActualVolt:" + phaseY_ActualVolt);

						
						if((phaseY_ActualVolt <= expectedUpperLimitValue)&&(phaseY_ActualVolt >= expectedLowerLimitValue)){
							ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 : volt in range");
							targetAchieved = true;
							status = true;
							SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
						}else if(phaseY_ActualVolt > expectedUpperLimitValue){
							//mode = decrementMode;
							ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 : decrementMode");
							//SerialDM_Obj.lscsSetPowerSourceVoltFineTune(selectedPhaseName,levelAdjustment,decrementMode);
							SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_V2_DEC_LEVEL1);
							//Sleep(4500);
						}else{
							//mode = incrementMode;
							ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 : incrementMode");
							//SerialDM_Obj.lscsSetPowerSourceVoltFineTune(selectedPhaseName,levelAdjustment,incrementMode);
							SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_V2_INC_LEVEL1);
							//Sleep(4500);
						}


						//retryCount--;
					}

				}else{
					//status = true;
					ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 : Yphase volt zero");
					status = true;
					SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
				}
			}else if (selectedPhaseName.equals(ConstantApp.THIRD_PHASE_DISPLAY_NAME)){
				if((Float.parseFloat(DisplayDataObj.getB_PhaseOutputVoltage()) != 0f) ){

					//float degreePercentageAccepted = 1.0f;
					//ApplicationLauncher.logger.info("refStdFeedBackPhaseAngleControl : degreePercentageAccepted:"+ degreePercentageAccepted);

					//float expectedUpperLimitValue = 0;
					//float expectedLowerLimitValue = 0;

					float phaseB_ActualVolt = Float.parseFloat(getFeedbackB_phaseVolt());


					float phaseB_TargetVolt = Float.parseFloat(DisplayDataObj.getB_PhaseOutputVoltage());

					//expectedUpperLimitValue = phaseB_TargetVolt + ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_UPPER_LIMIT_PERCENT;//0.2f;
					//expectedLowerLimitValue = phaseB_TargetVolt - ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_LOWER_LIMIT_PERCENT;//0.4f;

					expectedUpperLimitValue = phaseB_TargetVolt + (phaseB_TargetVolt * (ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_UPPER_LIMIT_PERCENT / 100)); //+ ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_UPPER_LIMIT_PERCENT;//0.2f;
					expectedLowerLimitValue = phaseB_TargetVolt - (phaseB_TargetVolt * (ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_LOWER_LIMIT_PERCENT / 100)); //- ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_LOWER_LIMIT_PERCENT;//0.4f;


					boolean targetAchieved= false;
					//while( (!getUserAbortedFlag()) && (retryCount > 0) && (!targetAchieved) ){
					if(!getUserAbortedFlag()){
						phaseB_ActualVolt =  Float.parseFloat(getFeedbackB_phaseVolt());
						ApplicationLauncher.logger.debug("refStdFeedBackVoltControlV2: phaseB_TargetVolt:" + phaseB_TargetVolt);
						ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 : expectedUpperLimitValue:"+ expectedUpperLimitValue);
						ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 : expectedLowerLimitValue:"+ expectedLowerLimitValue);
						ApplicationLauncher.logger.debug("refStdFeedBackVoltControlV2: phaseB_ActualVolt:" + phaseB_ActualVolt);

						
						if((phaseB_ActualVolt <= expectedUpperLimitValue)&&(phaseB_ActualVolt >= expectedLowerLimitValue)){
							ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 : volt in range");
							targetAchieved = true;
							status = true;
							SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
						}else if(phaseB_ActualVolt > expectedUpperLimitValue){
							//mode = decrementMode;
							ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 : decrementMode");
							//SerialDM_Obj.lscsSetPowerSourceVoltFineTune(selectedPhaseName,levelAdjustment,decrementMode);
							SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_V3_DEC_LEVEL1);
							//Sleep(4500);
						}else{
							//mode = incrementMode;
							ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 : incrementMode");
							//SerialDM_Obj.lscsSetPowerSourceVoltFineTune(selectedPhaseName,levelAdjustment,incrementMode);
							SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_V3_INC_LEVEL1);
							//Sleep(4500);
						}


						//retryCount--;
					}

				}else{
					//status = true;
					ApplicationLauncher.logger.info("refStdFeedBackVoltControlV2 : Rphase volt zero");
					status = true;
					SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
				}
			}else{
				status = true;
			}

			return status;		
		}

*/

		public boolean refStdFeedBackCurrentControl(){ 
			boolean status = false;
			ApplicationLauncher.logger.debug("refStdFeedBackCurrentControl: Entry"  );
			//String userConfiguredSelectedPhaseName = ConstantAppConfig.POWER_SOURCE_SINGLE_PHASE_OUTPUT_SELECTED;
			String selectedPhaseName = ConstantAppConfig.POWER_SOURCE_SINGLE_PHASE_OUTPUT_SELECTED;
			boolean incrementMode = true;
			boolean decrementMode = false;
			//boolean mode = decrementMode;
			int levelAdjustment = 1;
			if (selectedPhaseName.equals(ConstantApp.FIRST_PHASE_DISPLAY_NAME)){
				if((Float.parseFloat(DeviceDataManagerController.getR_PhaseOutputCurrent()) != 0f) ){

					//float degreePercentageAccepted = 1.0f;
					//ApplicationLauncher.logger.info("refStdFeedBackPhaseAngleControl : degreePercentageAccepted:"+ degreePercentageAccepted);

					float expected_pos_value = 0;
					float expected_neg_value = 0;

					float phaseR_ActualCurrent = Float.parseFloat(getFeedbackR_phaseCurrent());


					float phaseR_TargetCurrent = Float.parseFloat(DeviceDataManagerController.getR_PhaseOutputCurrent());
					//ApplicationLauncher.logger.debug("refStdFeedBackCurrentControl: phaseR_TargetCurrent:" + phaseR_TargetCurrent);
					/*					if(phaseR_TargetDegree < 0.0f) {	
					//degreeStr = String.format(ConstantLscsPowerSource.PHASE_RESOLUTION,(360.0f + DeviceDataManagerController.get_PwrSrcR_PhaseDegreePhase()));
					phaseR_TargetDegree = 360.0f + phaseR_TargetDegree;

					ApplicationLauncher.logger.debug("refStdFeedBackCurrentControl: phaseR_TargetDegree2:" + phaseR_TargetDegree);
				}*/
					//expected_pos_value = phaseR_TargetDegree *((100 + degreePercentageAccepted)/100);
					//expected_neg_value = phaseR_TargetDegree * ((100 - degreePercentageAccepted)/100);

					expected_pos_value = phaseR_TargetCurrent + ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_CURRENT_ALLOWED_UPPER_LIMIT_PERCENT;//0.2f;
					expected_neg_value = phaseR_TargetCurrent - ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_CURRENT_ALLOWED_LOWER_LIMIT_PERCENT;//0.4f;
					/*if(phaseR_TargetDegree == 0.0f)
				{
					expected_neg_value = phaseR_TargetDegree - 0.3f;
				}else{
					expected_neg_value = phaseR_TargetDegree - 0.3f;
				}*/

					//while( (!getUserAbortedFlag()) && (retryCount > 0) && (!targetAchieved) ){
					if(!getUserAbortedFlag()){
						phaseR_ActualCurrent =  Float.parseFloat(getFeedbackR_phaseCurrent());
						ApplicationLauncher.logger.debug("refStdFeedBackCurrentControl: phaseR_TargetCurrent:" + phaseR_TargetCurrent);
						ApplicationLauncher.logger.info("refStdFeedBackCurrentControl : expected_pos_value:"+ expected_pos_value);
						ApplicationLauncher.logger.info("refStdFeedBackCurrentControl : expected_neg_value:"+ expected_neg_value);
						ApplicationLauncher.logger.debug("refStdFeedBackCurrentControl: phaseR_ActualCurrent:" + phaseR_ActualCurrent);

						/*if(phaseR_Actualdegree > 300.0f){
						phaseR_Actualdegree =  phaseR_Actualdegree - 360.0f;
						ApplicationLauncher.logger.debug("refStdFeedBackCurrentControl: phaseR_Actualdegree2:" + phaseR_Actualdegree);

					}*/
						if((phaseR_ActualCurrent <= expected_pos_value)&&(phaseR_ActualCurrent >= expected_neg_value)){
							ApplicationLauncher.logger.info("refStdFeedBackCurrentControl : current in range");
							status = true;
						}else if(phaseR_ActualCurrent > expected_pos_value){
							//mode = decrementMode;
							ApplicationLauncher.logger.info("refStdFeedBackCurrentControl : decrementMode");
							SerialDM_Obj.lscsSetPowerSourceCurrentFineTune(selectedPhaseName,levelAdjustment,decrementMode);
							//Sleep(4500);
						}else{
							//mode = incrementMode;
							ApplicationLauncher.logger.info("refStdFeedBackCurrentControl : incrementMode");
							SerialDM_Obj.lscsSetPowerSourceCurrentFineTune(selectedPhaseName,levelAdjustment,incrementMode);
							//Sleep(4500);
						}


						//retryCount--;
					}

				}else{
					status = true;
				}
			}else{
				status = true;
			}

			return status;		
		}

		public boolean refStdFeedBackCurrentControlV2(String selectedPhaseName){
			boolean status = false;
			ApplicationLauncher.logger.debug("refStdFeedBackCurrentControlV2: Entry"  );
			float expectedUpperLimitValue = 0;
			float expectedLowerLimitValue = 0;
			BigDecimal bigValue = new BigDecimal(expectedUpperLimitValue);

			int setScaleAfterDecimal = 4;
			float diffValue =0;

			if (selectedPhaseName.equals(ConstantApp.FIRST_PHASE_DISPLAY_NAME)){


				if((Float.parseFloat(DeviceDataManagerController.getR_PhaseOutputCurrent()) != 0f) ){
					String currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_DEFAULT_MULTIPLIER;
					//float degreePercentageAccepted = 1.0f;
					//ApplicationLauncher.logger.info("refStdFeedBackPhaseAngleControl : degreePercentageAccepted:"+ degreePercentageAccepted);



					float phaseR_ActualCurrent = Float.parseFloat(getFeedbackR_phaseCurrent());


					float phaseR_TargetCurrent = Float.parseFloat(DeviceDataManagerController.getR_PhaseOutputCurrent());
					//ApplicationLauncher.logger.debug("refStdFeedBackCurrentControlV2: phaseR_TargetCurrent:" + phaseR_TargetCurrent);

					//expectedUpperLimitValue = phaseR_TargetCurrent + ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_CURRENT_ALLOWED_UPPER_LIMIT;//0.2f;
					//expectedLowerLimitValue = phaseR_TargetCurrent - ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_CURRENT_ALLOWED_LOWER_LIMIT;//0.4f;

					expectedUpperLimitValue = phaseR_TargetCurrent + (phaseR_TargetCurrent * (ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_CURRENT_ALLOWED_UPPER_LIMIT_PERCENT / 100)); //+ ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_UPPER_LIMIT_PERCENT;//0.2f;
					expectedLowerLimitValue = phaseR_TargetCurrent - (phaseR_TargetCurrent * (ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_CURRENT_ALLOWED_LOWER_LIMIT_PERCENT / 100)); //- ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_LOWER_LIMIT_PERCENT;//0.4f;



					/*if(phaseR_TargetDegree == 0.0f)
				{
					expected_neg_value = phaseR_TargetDegree - 0.3f;
				}else{
					expected_neg_value = phaseR_TargetDegree - 0.3f;
				}*/
					ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 : expectedUpperLimitValue1:"+ expectedUpperLimitValue);
					ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 : expectedLowerLimitValue1:"+ expectedLowerLimitValue);

					setScaleAfterDecimal = 4;
					if(phaseR_TargetCurrent< 1.0){
						ApplicationLauncher.logger.debug("refStdFeedBackCurrentControlV2: test1"  );
						setScaleAfterDecimal = 6;//4;
					}
					bigValue = new BigDecimal(expectedUpperLimitValue);
					bigValue = bigValue.setScale(setScaleAfterDecimal, RoundingMode.FLOOR);
					expectedUpperLimitValue = bigValue.floatValue();

					bigValue = new BigDecimal(expectedLowerLimitValue);
					bigValue = bigValue.setScale(setScaleAfterDecimal, RoundingMode.FLOOR);
					expectedLowerLimitValue = bigValue.floatValue();
					if(phaseR_TargetCurrent< 0.100){//added on version s4.2.0.9.0.1

					}else if(phaseR_TargetCurrent< 1.0){
						//expectedUpperLimitValue = expectedUpperLimitValue + 0.0005f;//commented on version s4.2.0.9.0.1
						//expectedLowerLimitValue = expectedLowerLimitValue - 0.0004f;//commented on version s4.2.0.9.0.1
					}else{
						//expectedUpperLimitValue = expectedUpperLimitValue + 0.005f;//commented on version s4.2.0.9.0.3
						//expectedLowerLimitValue = expectedLowerLimitValue - 0.002f;//commented on version s4.2.0.9.0.3
					}
					//while( (!getUserAbortedFlag()) && (retryCount > 0) && (!targetAchieved) ){
					if(!getUserAbortedFlag()){
						phaseR_ActualCurrent =  Float.parseFloat(getFeedbackR_phaseCurrent());
						/*if(phaseR_TargetCurrent< 1.0){
						setScaleAfterDecimal = 6;
						bigValue = new BigDecimal(phaseR_ActualCurrent);
						bigValue = bigValue.setScale(setScaleAfterDecimal, RoundingMode.FLOOR);
						phaseR_ActualCurrent = bigValue.floatValue();
					}*/
						ApplicationLauncher.logger.debug("refStdFeedBackCurrentControlV2: phaseR_TargetCurrent:" + phaseR_TargetCurrent);
						ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 : expectedUpperLimitValue2:"+ expectedUpperLimitValue);
						ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 : expectedLowerLimitValue2:"+ expectedLowerLimitValue);
						ApplicationLauncher.logger.debug("refStdFeedBackCurrentControlV2: phaseR_ActualCurrent:" + phaseR_ActualCurrent);

						/*if(phaseR_Actualdegree > 300.0f){
						phaseR_Actualdegree =  phaseR_Actualdegree - 360.0f;
						ApplicationLauncher.logger.debug("refStdFeedBackCurrentControlV2: phaseR_Actualdegree2:" + phaseR_Actualdegree);

					}*/
						if((phaseR_ActualCurrent <= expectedUpperLimitValue)&&(phaseR_ActualCurrent >= expectedLowerLimitValue)){
							ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 : current in range");
							status = true;
							lastPowerSource_R_CurrentDecrement = false;
							lastPowerSource_R_CurrentIncrement = false;
							feedbackAdjustmentDoubleConfirmed_R_CurrentIncrement = false;
							feedbackAdjustmentDoubleConfirmed_R_CurrentDecrement = false;
							feedbackAdjustmentDoubleConfirmed_R_CurrentIncrementCounter = 0;
							feedbackAdjustmentDoubleConfirmed_R_CurrentDecrementCounter =0;
							SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
						}else if(phaseR_ActualCurrent > expectedUpperLimitValue){
							//mode = decrementMode;
							ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 : decrementMode");
							//SerialDM_Obj.lscsSetPowerSourceCurrentFineTune(selectedPhaseName,levelAdjustment,decrementMode);
							//SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantLscsPowerSource.CMD_PWR_SRC_FINE_CONTROL_I1_DEC_LEVEL1);

							if(isLastPowerSource_R_CurrentDecrement()){
								setLastPowerSource_R_CurrentIncrement(false);
								setLastPowerSource_R_CurrentDecrement(false);
								ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 Rphase: current sent NoChange-D1");
								//data = ConstantLscsPowerSource.CMD_PWR_SRC_FINE_CONTROL_PF1_NO_CHANGE;
								SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
							}else {
								if(feedbackAdjustmentDoubleConfirmed_R_CurrentDecrement){
									ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 Rphase: decrementMode-B");
									if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_MULTIPLIER_ENABLED) {
										diffValue = phaseR_ActualCurrent  - expectedUpperLimitValue;
										ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 : dec : diffValue: " + String.format("%03.05f",diffValue));
										if(DeviceDataManagerController.getSourceCurrentR_PhaseTapSelection().equals(ConstantPowerSourceLscs.CMD_PWR_SRC_SELECT_CUR_RELAY_60_AMPS)) {
											if((diffValue)> 0.3f){
												currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_12;//9;//6;//"9";

											}else if((diffValue)> 0.2f){
												currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_11;//9;//6;//"9";

											}else{

												currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_5;//9;//6;//"9";
											}									
										}else if(DeviceDataManagerController.getSourceCurrentR_PhaseTapSelection().equals(ConstantPowerSourceLscs.CMD_PWR_SRC_SELECT_CUR_RELAY_10A)) {
											/*										if((diffValue)> 3.0f){

											currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_11;//"a";
										}else if((diffValue)> 2.0f){
											currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_9;//"9";

										}*/
											
											if(diffValue>0.022f){
												currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_7;
											}else if(diffValue>0.012f){
												currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_6;
											}else if(diffValue>0.010f){
												currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_5;//6;//"9";
											}else if(diffValue>0.008f){
												currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_4;
											}else if(diffValue>0.006f){
												currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_3;
											}else if(diffValue>0.002f){
												currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_2;
											}else {
												currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_1;//6;//"9";
											}
											//currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_4;
										}else if(DeviceDataManagerController.getSourceCurrentR_PhaseTapSelection().equals(ConstantPowerSourceLscs.CMD_PWR_SRC_SELECT_CUR_RELAY_1_AMPS)) {
											ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 : dec : 1 Amps tap" );
											/*if((diffValue)> 3.0f){

											currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_5;//"a";
										}else if((diffValue)> 2.0f){
											currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_3;//"9";

										}*/
											currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_HALF;//CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_1;//9;//"9";

										}else if(DeviceDataManagerController.getSourceCurrentR_PhaseTapSelection().equals(ConstantPowerSourceLscs.CMD_PWR_SRC_SELECT_CUR_RELAY_120_MILLI_AMPS)) {
											ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 : dec : 120mA tap" );
											currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_1;//9;//"9";


											/*										if((diffValue)> 3.0f){

											currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_5;//"a";
										}else if((diffValue)> 2.0f){
											currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_3;//"9";

										}*/
										}else if(DeviceDataManagerController.getSourceCurrentR_PhaseTapSelection().equals(ConstantPowerSourceLscs.CMD_PWR_SRC_SELECT_CUR_RELAY_50_MILLI_AMPS)) {
											ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 : dec : 50mA tap" );

											/*										if((diffValue)> 3.0f){

											currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_5;//"a";
										}else if((diffValue)> 2.0f){
											currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_3;//"9";

										}*/

											currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_1;//"9";

										}

										SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_I1_DEC_LEVEL1 + currentFeedBackRphaseMultiplierValue);
									}else {
										SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_I1_DEC_LEVEL1);

									}
									setLastPowerSource_R_CurrentIncrement(false);
									setLastPowerSource_R_CurrentDecrement(true);
									feedbackAdjustmentDoubleConfirmed_R_CurrentDecrement = false;
								}else {
									ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 Rphase: Current sent NoChange-D2");
									SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
									feedbackAdjustmentDoubleConfirmed_R_CurrentDecrementCounter++;
									if(feedbackAdjustmentDoubleConfirmed_R_CurrentDecrementCounter==1){
										feedbackAdjustmentDoubleConfirmed_R_CurrentDecrement = true;
										feedbackAdjustmentDoubleConfirmed_R_CurrentDecrementCounter =0;
										ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 Rphase: decrement Confirmed");
									}
								}
							}
							//Sleep(4500);
						}else{
							//mode = incrementMode;
							ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 : incrementMode");
							//SerialDM_Obj.lscsSetPowerSourceCurrentFineTune(selectedPhaseName,levelAdjustment,incrementMode);
							//SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantLscsPowerSource.CMD_PWR_SRC_FINE_CONTROL_I1_INC_LEVEL1);
							//Sleep(4500);

							if(isLastPowerSource_R_CurrentIncrement()){
								setLastPowerSource_R_CurrentIncrement (false);
								setLastPowerSource_R_CurrentDecrement (false);
								ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 Rphase: current sent NoChange-F1");

								SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
							}else {
								if(feedbackAdjustmentDoubleConfirmed_R_CurrentIncrement){
									ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 Rphase: incrementMode-B");
									if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_MULTIPLIER_ENABLED) {
										diffValue = expectedUpperLimitValue - phaseR_ActualCurrent  ;
										ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 : inc : diffValue: " +  String.format("%03.05f",diffValue));
										if(DeviceDataManagerController.getSourceCurrentR_PhaseTapSelection().equals(ConstantPowerSourceLscs.CMD_PWR_SRC_SELECT_CUR_RELAY_60_AMPS)) {
											if((diffValue)> 0.3f){
												currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_9;//9;//6;//"9";

											}else if((diffValue)> 0.2f){
												currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_8;//9;//6;//"9";

											}else{
												currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_7;//11;//6;//"9";
											}								
										}else if(DeviceDataManagerController.getSourceCurrentR_PhaseTapSelection().equals(ConstantPowerSourceLscs.CMD_PWR_SRC_SELECT_CUR_RELAY_10A)) {
											/*			if((diffValue)> 3.0f){

											currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_11;//"a";
										}else if((diffValue)> 2.0f){
											currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_9;//"9";

										}*/
											//currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_6;//6;//"9";
											if(diffValue>0.02f){
												currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_6;
											}else if(diffValue>0.012f){
												currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_5;
											}else if(diffValue>0.010f){
												currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_4;//6;//"9";
											}else if(diffValue>0.008f){
												currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_3;
											}else if(diffValue>0.006f){
												currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_2;
											}else if(diffValue>0.002f){
												currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_1;
											}else {
												currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_1;//6;//"9";
											}
											//currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_6;
										}else if(DeviceDataManagerController.getSourceCurrentR_PhaseTapSelection().equals(ConstantPowerSourceLscs.CMD_PWR_SRC_SELECT_CUR_RELAY_1_AMPS)) {
											ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 : inc : 1 Amps tap" );
											/*										if((diffValue)> 3.0f){

											currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_5;//"a";
										}else if((diffValue)> 2.0f){
											currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_3;//"9";

										}*/
											currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_HALF;//1;//6;//"9";

										}else if(DeviceDataManagerController.getSourceCurrentR_PhaseTapSelection().equals(ConstantPowerSourceLscs.CMD_PWR_SRC_SELECT_CUR_RELAY_120_MILLI_AMPS)) {
											ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 : inc : 120mA tap" );
											currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_1;//9;//"9";

											/*if((diffValue)> 3.0f){

											currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_5;//"a";
										}else if((diffValue)> 2.0f){
											currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_3;//"9";

										}*/
										}else if(DeviceDataManagerController.getSourceCurrentR_PhaseTapSelection().equals(ConstantPowerSourceLscs.CMD_PWR_SRC_SELECT_CUR_RELAY_50_MILLI_AMPS)) {
											ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 : inc : 50mA tap" );

											/*if((diffValue)> 3.0f){

											currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_5;//"a";
										}else if((diffValue)> 2.0f){
											currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_3;//"9";

										}*/

											currentFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_1;//6;
										}


										SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_I1_INC_LEVEL1 + String.valueOf(currentFeedBackRphaseMultiplierValue));
									}else {
										SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_I1_INC_LEVEL1);
									}
									setLastPowerSource_R_CurrentIncrement (true);
									setLastPowerSource_R_CurrentDecrement(false);
									feedbackAdjustmentDoubleConfirmed_R_CurrentIncrement = false;
								}else{
									ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 Rphase: Current sent NoChange-F2");
									SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
									feedbackAdjustmentDoubleConfirmed_R_CurrentIncrementCounter++;
									if(feedbackAdjustmentDoubleConfirmed_R_CurrentIncrementCounter==1){
										feedbackAdjustmentDoubleConfirmed_R_CurrentIncrement = true;
										feedbackAdjustmentDoubleConfirmed_R_CurrentIncrementCounter =0;
										ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 Rphase: increment Confirmed");
									}
								}
							}
						}
						setLastRefStdFeedBackRead_R_PhaseCurrent(phaseR_ActualCurrent);

						//retryCount--;
					}

				}else{
					//status = true;
					ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 : Rphase volt zero");
					status = true;
					SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
				}
			}else if (selectedPhaseName.equals(ConstantApp.SECOND_PHASE_DISPLAY_NAME)){


				if((Float.parseFloat(DeviceDataManagerController.getY_PhaseOutputCurrent()) != 0f) ){
					int currentFeedBackYphaseMultiplierValue = Integer.parseInt(ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_DEFAULT_MULTIPLIER);

					//float degreePercentageAccepted = 1.0f;
					//ApplicationLauncher.logger.info("refStdFeedBackPhaseAngleControl : degreePercentageAccepted:"+ degreePercentageAccepted);



					float phaseY_ActualCurrent = Float.parseFloat(getFeedbackY_phaseCurrent());


					float phaseY_TargetCurrent = Float.parseFloat(DeviceDataManagerController.getY_PhaseOutputCurrent());
					//ApplicationLauncher.logger.debug("refStdFeedBackCurrentControlV2: phaseR_TargetCurrent:" + phaseR_TargetCurrent);

					//expectedUpperLimitValue = phaseR_TargetCurrent + ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_CURRENT_ALLOWED_UPPER_LIMIT;//0.2f;
					//expectedLowerLimitValue = phaseR_TargetCurrent - ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_CURRENT_ALLOWED_LOWER_LIMIT;//0.4f;

					expectedUpperLimitValue = phaseY_TargetCurrent + (phaseY_TargetCurrent * (ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_CURRENT_ALLOWED_UPPER_LIMIT_PERCENT / 100)); //+ ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_UPPER_LIMIT_PERCENT;//0.2f;
					expectedLowerLimitValue = phaseY_TargetCurrent - (phaseY_TargetCurrent * (ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_CURRENT_ALLOWED_LOWER_LIMIT_PERCENT / 100)); //- ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_LOWER_LIMIT_PERCENT;//0.4f;

					setScaleAfterDecimal = 3;
					if(phaseY_TargetCurrent< 1.0){
						ApplicationLauncher.logger.debug("refStdFeedBackCurrentControlV2: test1"  );
						setScaleAfterDecimal = 4;
					}
					bigValue = new BigDecimal(expectedUpperLimitValue);
					bigValue = bigValue.setScale(setScaleAfterDecimal, RoundingMode.FLOOR);
					expectedUpperLimitValue = bigValue.floatValue();

					bigValue = new BigDecimal(expectedLowerLimitValue);
					bigValue = bigValue.setScale(setScaleAfterDecimal, RoundingMode.FLOOR);
					expectedLowerLimitValue = bigValue.floatValue();

					if(phaseY_TargetCurrent< 1.0){
						expectedUpperLimitValue = expectedUpperLimitValue + 0.0005f;
						expectedLowerLimitValue = expectedLowerLimitValue - 0.0004f;
					}else{
						expectedUpperLimitValue = expectedUpperLimitValue + 0.005f;
						expectedLowerLimitValue = expectedLowerLimitValue - 0.002f;
					}
					/*if(phaseR_TargetDegree == 0.0f)
				{
					expected_neg_value = phaseR_TargetDegree - 0.3f;
				}else{
					expected_neg_value = phaseR_TargetDegree - 0.3f;
				}*/

					//while( (!getUserAbortedFlag()) && (retryCount > 0) && (!targetAchieved) ){
					if(!getUserAbortedFlag()){
						phaseY_ActualCurrent =  Float.parseFloat(getFeedbackY_phaseCurrent());
						ApplicationLauncher.logger.debug("refStdFeedBackCurrentControlV2: phaseY_TargetCurrent:" + phaseY_TargetCurrent);
						ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 : expectedUpperLimitValue:"+ expectedUpperLimitValue);
						ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 : expectedLowerLimitValue:"+ expectedLowerLimitValue);
						ApplicationLauncher.logger.debug("refStdFeedBackCurrentControlV2: phaseY_ActualCurrent:" + phaseY_ActualCurrent);

						/*if(phaseR_Actualdegree > 300.0f){
						phaseR_Actualdegree =  phaseR_Actualdegree - 360.0f;
						ApplicationLauncher.logger.debug("refStdFeedBackCurrentControlV2: phaseR_Actualdegree2:" + phaseR_Actualdegree);

					}*/
						if((phaseY_ActualCurrent <= expectedUpperLimitValue)&&(phaseY_ActualCurrent >= expectedLowerLimitValue)){
							ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 : current in range");
							status = true;
							SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
						}else if(phaseY_ActualCurrent > expectedUpperLimitValue){

							ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 : decrementMode");

							if(isLastPowerSource_Y_CurrentDecrement()){
								//setLastPowerSource_R_CurrentIncrement(false);
								setLastPowerSource_Y_CurrentDecrement(false);
								ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 Yphase: current sent NoChange-D1");
								//data = ConstantLscsPowerSource.CMD_PWR_SRC_FINE_CONTROL_PF1_NO_CHANGE;
								SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
							}else {
								ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 Yphase: decrementMode-B");
								if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_MULTIPLIER_ENABLED) {

									SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_I2_DEC_LEVEL1 + String.valueOf(currentFeedBackYphaseMultiplierValue));
								}else {

									SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_I2_DEC_LEVEL1);
								}
								//setLastPowerSource_R_CurrentIncrement(false);
								setLastPowerSource_Y_CurrentDecrement(true);
							}
							//Sleep(4500);
						}else{
							//mode = incrementMode;
							ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 : incrementMode");

							if(isLastPowerSource_Y_CurrentIncrement()){
								setLastPowerSource_Y_CurrentIncrement (false);
								//setLastPowerSource_R_CurrentDecrement (false);
								ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 Yphase: phase angle sent NoChange-F1");

								SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
							}else {
								ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 Yphase: incrementMode-B");
								if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_MULTIPLIER_ENABLED) {

									SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_I2_INC_LEVEL1 + String.valueOf(currentFeedBackYphaseMultiplierValue));
								}else {
									SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_I2_INC_LEVEL1);
								}
								setLastPowerSource_Y_CurrentIncrement (true);
								//setLastPowerSource_R_CurrentDecrement(false);
							}
						}
						setLastRefStdFeedBackRead_Y_PhaseCurrent(phaseY_ActualCurrent);


						/*else if(phaseY_ActualCurrent > expectedUpperLimitValue){
						//mode = decrementMode;
						ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 : decrementMode");
						//SerialDM_Obj.lscsSetPowerSourceCurrentFineTune(selectedPhaseName,levelAdjustment,decrementMode);
						SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantLscsPowerSource.CMD_PWR_SRC_FINE_CONTROL_I2_DEC_LEVEL1);
						//Sleep(4500);
					}else{
						//mode = incrementMode;
						ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 : incrementMode");
						//SerialDM_Obj.lscsSetPowerSourceCurrentFineTune(selectedPhaseName,levelAdjustment,incrementMode);
						SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantLscsPowerSource.CMD_PWR_SRC_FINE_CONTROL_I2_INC_LEVEL1);
						//Sleep(4500);
					}*/


						//retryCount--;
					}

				}else{
					ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 : Rphase volt zero");
					status = true;
					SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
				}
			}else if (selectedPhaseName.equals(ConstantApp.THIRD_PHASE_DISPLAY_NAME)){


				if((Float.parseFloat(DeviceDataManagerController.getB_PhaseOutputCurrent()) != 0f) ){
					int currentFeedBackBphaseMultiplierValue = Integer.parseInt(ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_DEFAULT_MULTIPLIER);

					//float degreePercentageAccepted = 1.0f;
					//ApplicationLauncher.logger.info("refStdFeedBackPhaseAngleControl : degreePercentageAccepted:"+ degreePercentageAccepted);



					float phaseB_ActualCurrent = Float.parseFloat(getFeedbackB_phaseCurrent());


					float phaseB_TargetCurrent = Float.parseFloat(DeviceDataManagerController.getB_PhaseOutputCurrent());
					//ApplicationLauncher.logger.debug("refStdFeedBackCurrentControlV2: phaseR_TargetCurrent:" + phaseR_TargetCurrent);

					//expectedUpperLimitValue = phaseR_TargetCurrent + ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_CURRENT_ALLOWED_UPPER_LIMIT;//0.2f;
					//expectedLowerLimitValue = phaseR_TargetCurrent - ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_CURRENT_ALLOWED_LOWER_LIMIT;//0.4f;

					expectedUpperLimitValue = phaseB_TargetCurrent + (phaseB_TargetCurrent * (ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_CURRENT_ALLOWED_UPPER_LIMIT_PERCENT / 100)); //+ ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_UPPER_LIMIT_PERCENT;//0.2f;
					expectedLowerLimitValue = phaseB_TargetCurrent - (phaseB_TargetCurrent * (ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_CURRENT_ALLOWED_LOWER_LIMIT_PERCENT / 100)); //- ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_VOLT_ALLOWED_LOWER_LIMIT_PERCENT;//0.4f;

					setScaleAfterDecimal = 3;
					if(phaseB_TargetCurrent< 1.0){
						ApplicationLauncher.logger.debug("refStdFeedBackCurrentControlV2: test1"  );
						setScaleAfterDecimal = 4;
					}
					bigValue = new BigDecimal(expectedUpperLimitValue);
					bigValue = bigValue.setScale(setScaleAfterDecimal, RoundingMode.FLOOR);
					expectedUpperLimitValue = bigValue.floatValue();

					bigValue = new BigDecimal(expectedLowerLimitValue);
					bigValue = bigValue.setScale(setScaleAfterDecimal, RoundingMode.FLOOR);
					expectedLowerLimitValue = bigValue.floatValue();
					if(phaseB_TargetCurrent< 1.0){
						expectedUpperLimitValue = expectedUpperLimitValue + 0.0005f;
						expectedLowerLimitValue = expectedLowerLimitValue - 0.0004f;
					}else{
						expectedUpperLimitValue = expectedUpperLimitValue + 0.005f;
						expectedLowerLimitValue = expectedLowerLimitValue - 0.002f;
					}
					/*if(phaseR_TargetDegree == 0.0f)
				{
					expected_neg_value = phaseR_TargetDegree - 0.3f;
				}else{
					expected_neg_value = phaseR_TargetDegree - 0.3f;
				}*/

					//while( (!getUserAbortedFlag()) && (retryCount > 0) && (!targetAchieved) ){
					if(!getUserAbortedFlag()){
						phaseB_ActualCurrent =  Float.parseFloat(getFeedbackB_phaseCurrent());
						ApplicationLauncher.logger.debug("refStdFeedBackCurrentControlV2: phaseB_TargetCurrent:" + phaseB_TargetCurrent);
						ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 : expectedUpperLimitValue:"+ expectedUpperLimitValue);
						ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 : expectedLowerLimitValue:"+ expectedLowerLimitValue);
						ApplicationLauncher.logger.debug("refStdFeedBackCurrentControlV2: phaseB_ActualCurrent:" + phaseB_ActualCurrent);

						/*if(phaseR_Actualdegree > 300.0f){
						phaseR_Actualdegree =  phaseR_Actualdegree - 360.0f;
						ApplicationLauncher.logger.debug("refStdFeedBackCurrentControlV2: phaseR_Actualdegree2:" + phaseR_Actualdegree);

					}*/
						if((phaseB_ActualCurrent <= expectedUpperLimitValue)&&(phaseB_ActualCurrent >= expectedLowerLimitValue)){
							ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 : current in range");
							status = true;
							SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
						}else if(phaseB_ActualCurrent > expectedUpperLimitValue){

							ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 : decrementMode");

							if(isLastPowerSource_B_CurrentDecrement()){
								//setLastPowerSource_R_CurrentIncrement(false);
								setLastPowerSource_B_CurrentDecrement(false);
								ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 Bphase: current sent NoChange-D1");
								//data = ConstantLscsPowerSource.CMD_PWR_SRC_FINE_CONTROL_PF1_NO_CHANGE;
								SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
							}else {
								ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 Bphase: decrementMode-B");
								if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_MULTIPLIER_ENABLED) {

									SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_I3_DEC_LEVEL1 + String.valueOf(currentFeedBackBphaseMultiplierValue));
								}else {

									SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_I3_DEC_LEVEL1);
								}
								//setLastPowerSource_R_CurrentIncrement(false);
								setLastPowerSource_B_CurrentDecrement(true);
							}
							//Sleep(4500);
						}else{
							//mode = incrementMode;
							ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 : incrementMode");

							if(isLastPowerSource_B_CurrentIncrement()){
								setLastPowerSource_B_CurrentIncrement (false);
								//setLastPowerSource_R_CurrentDecrement (false);
								ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 Bphase: phase angle sent NoChange-F1");

								SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
							}else {
								ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 Bphase: incrementMode-B");
								if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_MULTIPLIER_ENABLED) {

									SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_I3_INC_LEVEL1 + String.valueOf(currentFeedBackBphaseMultiplierValue));
								}else {

									SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_I3_INC_LEVEL1);
								}
								setLastPowerSource_B_CurrentIncrement (true);
								//setLastPowerSource_R_CurrentDecrement(false);
							}
						}
						setLastRefStdFeedBackRead_B_PhaseCurrent(phaseB_ActualCurrent);


						/*else if(phaseB_ActualCurrent > expectedUpperLimitValue){
						//mode = decrementMode;
						ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 : decrementMode");
						//SerialDM_Obj.lscsSetPowerSourceCurrentFineTune(selectedPhaseName,levelAdjustment,decrementMode);
						SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantLscsPowerSource.CMD_PWR_SRC_FINE_CONTROL_I3_DEC_LEVEL1);
						//Sleep(4500);
					}else{
						//mode = incrementMode;
						ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 : incrementMode");
						//SerialDM_Obj.lscsSetPowerSourceCurrentFineTune(selectedPhaseName,levelAdjustment,incrementMode);
						SerialDM_Obj.lscsSetPowerSourceFineTuneData( ConstantLscsPowerSource.CMD_PWR_SRC_FINE_CONTROL_I3_INC_LEVEL1);
						//Sleep(4500);
					}*/


						//retryCount--;
					}

				}else{
					ApplicationLauncher.logger.info("refStdFeedBackCurrentControlV2 : Rphase volt zero");
					status = true;
					SerialDM_Obj.lscsSetPowerSourceFineTuneNoChange();
				}
			}else{
				status = true;
			}

			return status;		
		}


		public boolean refStdFeedBackR_PhaseDegreePhaseAngleControl(){
			boolean status = false;

			ApplicationLauncher.logger.debug("refStdFeedBackR_PhaseDegreePhaseAngleControl: Entry"  );

			if(isRefStdFeedBackControlFlagEnabled()){
				float phaseR_Actualdegree =  Float.parseFloat(getFeedbackR_phaseDegree());
				if(ProcalFeatureEnable.KRE_REFSTD_CONNECTED ){
					//phaseR_Actualdegree = - phaseR_Actualdegree;
				}else{
					phaseR_Actualdegree = - phaseR_Actualdegree;
				}
				//float phaseR_Actualdegree = - Float.parseFloat(getFeedbackR_phaseDegree());
				ApplicationLauncher.logger.debug("refStdFeedBackR_PhaseDegreePhaseAngleControl: phaseR_Actualdegree:" + phaseR_Actualdegree);
				String phaseR_ActualdegreeStr = "000.000";

				phaseR_ActualdegreeStr = degreeFormatter.format( phaseR_Actualdegree);
				ApplicationLauncher.logger.debug("refStdFeedBackR_PhaseDegreePhaseAngleControl: phaseR_ActualdegreeStr:" + phaseR_ActualdegreeStr);
				String data =  ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR;
				String pfFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_DEFAULT_MULTIPLIER;

				//data = " ";
				//SerialDM_Obj.WriteToSerialCommPwrSrcV2(ConstantLscsPowerSource.CMD_PWR_SRC_SEPERATOR);
				float expected_pos_value = 0;
				float expected_neg_value = 0;
				float diffValue =0;

				float phaseR_TargetDegree = DeviceDataManagerController.get_PwrSrcR_PhaseDegreePhase();
				if(getCurrentTestPointName().startsWith(ConstantApp.EXPORT_MODE_ALIAS_NAME)){
					//phaseR_TargetDegree = -phaseR_TargetDegree;
					phaseR_TargetDegree = 360.0f + phaseR_TargetDegree;
					ApplicationLauncher.logger.debug("refStdFeedBackR_PhaseDegreePhaseAngleControl: phaseR_TargetDegree0:" + phaseR_TargetDegree);
				}
				expected_pos_value = phaseR_TargetDegree + ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_DEGREE_ALLOWED_UPPER_LIMIT;//0.2f;
				expected_neg_value = phaseR_TargetDegree - ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_DEGREE_ALLOWED_LOWER_LIMIT;//0.4f;

				if(getPresentMeterType().contains(ConstantApp.METERTYPE_SINGLEPHASE)){
					expected_pos_value = phaseR_TargetDegree + ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_DEGREE_ALLOWED_UPPER_LIMIT_SINGLE_PHASE;//0.2f;
					expected_neg_value = phaseR_TargetDegree - ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_DEGREE_ALLOWED_LOWER_LIMIT_SINGLE_PHASE;//0.4f;

				}


				/*				if( (getCurrentTestPointName().contains("0.5L"))){// && (getCurrentTestPointName().startsWith(ConstantApp.CALIBRATION_ALIAS_NAME))) {
					//expected_pos_value = phaseR_TargetDegree + 0.08f;//0.2f; //#18Feb2022
					//expected_neg_value = phaseR_TargetDegree - 0.4f;//0.4f;//#18Feb2022
					lastRefStdRead_R_PhaseCurrent = phaseR_Actualdegree;
				}*/
				//data = ConstantLscsPowerSource.CMD_PWR_SRC_SEPERATOR;//v2
				//SerialDM_Obj.WriteToSerialCommPwrSrcV2(data);
				ApplicationLauncher.logger.info("refStdFeedBackR_PhaseDegreePhaseAngleControl : expected_pos_value:"+ expected_pos_value);
				ApplicationLauncher.logger.info("refStdFeedBackR_PhaseDegreePhaseAngleControl : expected_neg_value:"+ expected_neg_value);
				ApplicationLauncher.logger.debug("refStdFeedBackR_PhaseDegreePhaseAngleControl: phaseR_Actualdegree:" + phaseR_Actualdegree);
				ApplicationLauncher.logger.debug("refStdFeedBackR_PhaseDegreePhaseAngleControl: phaseR_TargetDegree1:" + phaseR_TargetDegree);
				/*				if(getCurrentTestPointName().startsWith(ConstantApp.EXPORT_MODE_ALIAS_NAME)){
					//phaseR_Actualdegree =  360.0f - phaseR_Actualdegree;
					phaseR_Actualdegree =  - phaseR_Actualdegree;
					ApplicationLauncher.logger.debug("refStdFeedBackR_PhaseDegreePhaseAngleControl: phaseR_Actualdegree2:" + phaseR_Actualdegree);
				}else */

				if(phaseR_Actualdegree > 300.0f){
					phaseR_Actualdegree =  phaseR_Actualdegree - 360.0f;
					ApplicationLauncher.logger.debug("refStdFeedBackR_PhaseDegreePhaseAngleControl: phaseR_Actualdegree3:" + phaseR_Actualdegree);

				}

				/*				float differenceWithLastReading = Math.abs(lastRefStdRead_R_PhaseDegreePhaseAngle - phaseR_Actualdegree);
				ApplicationLauncher.logger.info("refStdFeedBackR_PhaseDegreePhaseAngleControl : lastRefStdRead_R_PhaseCurrent: " + lastRefStdRead_R_PhaseDegreePhaseAngle);
				ApplicationLauncher.logger.info("refStdFeedBackR_PhaseDegreePhaseAngleControl : differenceWithLastReading: " + differenceWithLastReading);
				ApplicationLauncher.logger.info("refStdFeedBackR_PhaseDegreePhaseAngleControl : phaseR_TargetDegree+0.5f: " + (phaseR_TargetDegree+0.5f));
				boolean differenceMoreThanAllowedLimits = false;
				if ((differenceWithLastReading > 0.2f)  ){
					lastPowerSource_R_PhaseAngleIncrement = false;
					lastPowerSource_R_PhaseAngleDecrement = false;
					differenceMoreThanAllowedLimits = true;
					ApplicationLauncher.logger.info("refStdFeedBackR_PhaseDegreePhaseAngleControl : Inc/Dec allowed1");

				}else if ( (phaseR_Actualdegree > (phaseR_TargetDegree+0.5f)) ){

					lastPowerSource_R_PhaseAngleIncrement = false;
					lastPowerSource_R_PhaseAngleDecrement = false;
					differenceMoreThanAllowedLimits = true;
					ApplicationLauncher.logger.info("refStdFeedBackR_PhaseDegreePhaseAngleControl : Inc/Dec allowed2");
				}else if ( (phaseR_Actualdegree < (phaseR_TargetDegree-0.5f)) ){
					lastPowerSource_R_PhaseAngleIncrement = false;
					lastPowerSource_R_PhaseAngleDecrement = false;
					differenceMoreThanAllowedLimits = true;
					ApplicationLauncher.logger.info("refStdFeedBackR_PhaseDegreePhaseAngleControl : Inc/Dec allowed3");


				}
				ApplicationLauncher.logger.info("refStdFeedBackR_PhaseDegreePhaseAngleControl : phaseR_Actualdegree4: " + phaseR_Actualdegree);
				ApplicationLauncher.logger.info("refStdFeedBackR_PhaseDegreePhaseAngleControl : expected_pos_value: " + expected_pos_value);
				ApplicationLauncher.logger.info("refStdFeedBackR_PhaseDegreePhaseAngleControl : expected_neg_value: " + expected_neg_value);*/

				if((phaseR_Actualdegree <= expected_pos_value)&&(phaseR_Actualdegree >= expected_neg_value)){
					ApplicationLauncher.logger.info("refStdFeedBackR_PhaseDegreePhaseAngleControl : degree phase angle in range");
					//targetAchieved = true;
					//status = true;
					ApplicationLauncher.logger.info("refStdFeedBackR_PhaseDegreePhaseAngleControl : phase angle sent NoChange-1");

					data = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_PF1_NO_CHANGE;
					lastPowerSource_R_PhaseAngleIncrement = false;
					lastPowerSource_R_PhaseAngleDecrement = false;
					feedbackAdjustmentDoubleConfirmed_R_PfDecrement = false;
					feedbackAdjustmentDoubleConfirmed_R_PfIncrement = false;
					feedbackAdjustmentDoubleConfirmed_R_PfIncrementCounter = 0;
					feedbackAdjustmentDoubleConfirmed_R_PfDecrementCounter = 0;
					ApplicationLauncher.logger.info("refStdFeedBackR_PhaseDegreePhaseAngleControl Rphase: Hit-reset");
				}else if(phaseR_Actualdegree > expected_pos_value){
					//mode = decrementMode;
					ApplicationLauncher.logger.info("refStdFeedBackR_PhaseDegreePhaseAngleControl : actual greater than upper limit");
					/*					if( (getCurrentTestPointName().contains("0.5L"))){
						if(lastPowerSource_R_PhaseAngleDecrement){
							lastPowerSource_R_PhaseAngleIncrement = false;
							lastPowerSource_R_PhaseAngleDecrement = false;
							ApplicationLauncher.logger.info("refStdFeedBackR_PhaseDegreePhaseAngleControl : phase angle sent NoChange-D1");
							data = ConstantLscsPowerSource.CMD_PWR_SRC_FINE_CONTROL_PF1_NO_CHANGE;
						}else{
							ApplicationLauncher.logger.info("refStdFeedBackR_PhaseDegreePhaseAngleControl : decrementMode-A");
							data = ConstantLscsPowerSource.CMD_PWR_SRC_FINE_CONTROL_PF1_DEC_LEVEL1;
							lastPowerSource_R_PhaseAngleIncrement = false;
							lastPowerSource_R_PhaseAngleDecrement= true;
						}

					}else{*/
					if(lastPowerSource_R_PhaseAngleDecrement){
						lastPowerSource_R_PhaseAngleIncrement = false;
						lastPowerSource_R_PhaseAngleDecrement = false;
						ApplicationLauncher.logger.info("refStdFeedBackR_PhaseDegreePhaseAngleControl : phase angle sent NoChange-D1");
						data = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_PF1_NO_CHANGE;
					}else {//if(differenceMoreThanAllowedLimits){
						ApplicationLauncher.logger.info("refStdFeedBackR_PhaseDegreePhaseAngleControl : decrementMode-B");
						if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_MULTIPLIER_ENABLED) {
							diffValue = phaseR_Actualdegree  - expected_pos_value;
							ApplicationLauncher.logger.info("refStdFeedBackR_PhaseDegreePhaseAngleControl : dec : diffValue: " + diffValue);
							if (getPresentMeterType().startsWith(ConstantApp.METERTYPE_SINGLEPHASE)){
								if((diffValue)> 3.0f){

									//pfFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_11;//"a";
									pfFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_12;//11;//"a";
								}else if((diffValue)> 2.0f){
									pfFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_11;//9;//"9";

								}else if((diffValue)> 1.0f){
									pfFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_11;//9;//"9";
								}else if((diffValue)> 0.8f){
									pfFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_9;//"8";
								}else if((diffValue)> 0.7f){
									pfFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_8;//"7";
								}else if((diffValue)> 0.6f){
									pfFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_7;//"6";
								}else if((diffValue)> 0.5f){
									pfFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_6;//"5";
								}else if((diffValue)> 0.4f){
									pfFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_5;//"4";
								}else if((diffValue)> 0.3f){
									pfFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_4;//"3";
								}else if((diffValue)> 0.2f){
									pfFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_3;//"2";
								}else if((diffValue)> 0.1f){
									pfFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_2;//"2";
								}else if((diffValue)> 0.02f){
									pfFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_1;//"2";
								}
							}
						}
						ApplicationLauncher.logger.info("refStdFeedBackR_PhaseDegreePhaseAngleControl Rphase: decCounter: " +feedbackAdjustmentDoubleConfirmed_R_PfDecrementCounter);
						if(feedbackAdjustmentDoubleConfirmed_R_PfDecrement){
							ApplicationLauncher.logger.info("refStdFeedBackR_PhaseDegreePhaseAngleControl Rphase: Hit1");
							data = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_PF1_DEC_LEVEL1;
							feedbackAdjustmentDoubleConfirmed_R_PfDecrement = false;
							feedbackAdjustmentDoubleConfirmed_R_PfIncrement = false;
							lastPowerSource_R_PhaseAngleIncrement = false;
							lastPowerSource_R_PhaseAngleDecrement= false;
						}else{
							data = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_PF1_NO_CHANGE;
							ApplicationLauncher.logger.info("refStdFeedBackR_PhaseDegreePhaseAngleControl Rphase: Hit2");
							feedbackAdjustmentDoubleConfirmed_R_PfDecrementCounter++;
							if(feedbackAdjustmentDoubleConfirmed_R_PfDecrementCounter==2){
								ApplicationLauncher.logger.info("refStdFeedBackR_PhaseDegreePhaseAngleControl Rphase: Hit3");
								feedbackAdjustmentDoubleConfirmed_R_PfDecrement = true;
								feedbackAdjustmentDoubleConfirmed_R_PfDecrementCounter =0;
								ApplicationLauncher.logger.info("refStdFeedBackR_PhaseDegreePhaseAngleControl Rphase: decrement Confirmed");
							}
						}


						/*lastPowerSource_R_PhaseAngleIncrement = false;
					lastPowerSource_R_PhaseAngleDecrement= true;*/
					}

					/*				
				if(lastPowerSource_R_PhaseAngleDecrement){

					ApplicationLauncher.logger.info("refStdFeedBackR_PhaseDegreePhaseAngleControl : decrementMode-B");
					data = ConstantLscsPowerSource.CMD_PWR_SRC_FINE_CONTROL_PF1_DEC_LEVEL1;
					lastPowerSource_R_PhaseAngleIncrement = false;
					lastPowerSource_R_PhaseAngleDecrement= true;
				}else{
					lastPowerSource_R_PhaseAngleIncrement = false;
					lastPowerSource_R_PhaseAngleDecrement= true;
				}*/



					/*else{
							lastPowerSource_R_PhaseAngleIncrement = false;
							lastPowerSource_R_PhaseAngleDecrement = false;
							ApplicationLauncher.logger.info("refStdFeedBackR_PhaseDegreePhaseAngleControl : phase angle sent NoChange-D2");
							data = ConstantLscsPowerSource.CMD_PWR_SRC_FINE_CONTROL_PF1_NO_CHANGE;
						}*/
					//}
					//SerialDM_Obj.lscsSetPowerSourcePhaseAngleFineTune(userConfiguredSelectedPhaseName,levelAdjustment,decrementMode);
					//Sleep(4500);
				}else{
					ApplicationLauncher.logger.info("refStdFeedBackR_PhaseDegreePhaseAngleControl : actual lesser than lower limit");
					//mode = incrementMode;
					/*					if( (getCurrentTestPointName().contains("0.5L"))){
						if(lastPowerSource_R_PhaseAngleIncrement){
							lastPowerSource_R_PhaseAngleIncrement = false;
							lastPowerSource_R_PhaseAngleDecrement = false;
							ApplicationLauncher.logger.info("refStdFeedBackR_PhaseDegreePhaseAngleControl : phase angle sent NoChange-F1");

							data = ConstantLscsPowerSource.CMD_PWR_SRC_FINE_CONTROL_PF1_NO_CHANGE;
						}else{
							ApplicationLauncher.logger.info("refStdFeedBackR_PhaseDegreePhaseAngleControl : incrementMode-A");
							data = ConstantLscsPowerSource.CMD_PWR_SRC_FINE_CONTROL_PF1_INC_LEVEL1;
							lastPowerSource_R_PhaseAngleIncrement = true;
							lastPowerSource_R_PhaseAngleDecrement= false;
						}
					}else{*/
					if(lastPowerSource_R_PhaseAngleIncrement){
						lastPowerSource_R_PhaseAngleIncrement = false;
						lastPowerSource_R_PhaseAngleDecrement = false;
						ApplicationLauncher.logger.info("refStdFeedBackR_PhaseDegreePhaseAngleControl : phase angle sent NoChange-F1");

						data = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_PF1_NO_CHANGE;
					}else {//if(differenceMoreThanAllowedLimits){
						ApplicationLauncher.logger.info("refStdFeedBackR_PhaseDegreePhaseAngleControl : incrementMode-B");
						if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_MULTIPLIER_ENABLED) {
							diffValue = expected_pos_value - phaseR_Actualdegree   ;
							ApplicationLauncher.logger.info("refStdFeedBackR_PhaseDegreePhaseAngleControl: inc : diffValue: " + diffValue);
							if (getPresentMeterType().startsWith(ConstantApp.METERTYPE_SINGLEPHASE)){
								if((diffValue)> 3.0f){

									pfFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_11;//"a";
								}else if((diffValue)> 2.0f){
									pfFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_13;//9;//"9";

								}else if((diffValue)> 1.0f){
									pfFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_12;//9;//"9";
								}else if((diffValue)> 0.8f){
									pfFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_11;///9;//"8";
								}else if((diffValue)> 0.7f){
									pfFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_8;//"7";
								}else if((diffValue)> 0.6f){
									pfFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_7;//"6";
								}else if((diffValue)> 0.5f){
									pfFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_6;//"5";
								}else if((diffValue)> 0.4f){
									pfFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_5;//"4";
								}else if((diffValue)> 0.3f){
									pfFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_4;//"3";
								}else if((diffValue)> 0.2f){
									pfFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_3;//"2";
								}else if((diffValue)> 0.1f){
									pfFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_2;//"2";
								}else if((diffValue)> 0.02f){
									pfFeedBackRphaseMultiplierValue = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_MULTIPLIER_LEVEL_1;//"2";
								}
							}
						}
						//data = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_PF1_INC_LEVEL1;

						if(feedbackAdjustmentDoubleConfirmed_R_PfIncrement){
							data = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_PF1_INC_LEVEL1;
							feedbackAdjustmentDoubleConfirmed_R_PfIncrement = false;
							feedbackAdjustmentDoubleConfirmed_R_PfDecrement = false;
							lastPowerSource_R_PhaseAngleIncrement = false;
							lastPowerSource_R_PhaseAngleDecrement= false;
						}else{
							data = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_PF1_NO_CHANGE;
							feedbackAdjustmentDoubleConfirmed_R_PfIncrementCounter++;
							if(feedbackAdjustmentDoubleConfirmed_R_PfIncrementCounter==2){
								feedbackAdjustmentDoubleConfirmed_R_PfIncrement = true;
								feedbackAdjustmentDoubleConfirmed_R_PfIncrementCounter =0;
								ApplicationLauncher.logger.info("refStdFeedBackR_PhaseDegreePhaseAngleControl Rphase: increment Confirmed");
							}
						}
						//lastPowerSource_R_PhaseAngleIncrement = true;
						//lastPowerSource_R_PhaseAngleDecrement= false;
					}/*else{
							lastPowerSource_R_PhaseAngleIncrement = false;
							lastPowerSource_R_PhaseAngleDecrement = false;
							ApplicationLauncher.logger.info("refStdFeedBackR_PhaseDegreePhaseAngleControl : phase angle sent NoChange-F2");

							data = ConstantLscsPowerSource.CMD_PWR_SRC_FINE_CONTROL_PF1_NO_CHANGE;
						}*/
					//}
					//SerialDM_Obj.lscsSetPowerSourcePhaseAngleFineTune(userConfiguredSelectedPhaseName,levelAdjustment,incrementMode);
					//Sleep(4500);
				}
				data = ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR + data;
				if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_MULTIPLIER_ENABLED) {
					data = data + pfFeedBackRphaseMultiplierValue;////CMD_PWR_SRC_FINE_CONTROL_V1_INC_LEVEL1;

				}
				SerialDM_Obj.WriteToSerialCommPwrSrcV2(data);
				lastRefStdRead_R_PhaseDegreePhaseAngle = phaseR_Actualdegree;


			}





			return status;
		}



		public boolean refStdFeedBackY_PhaseDegreePhaseAngleControl(){
			boolean status = false;

			ApplicationLauncher.logger.debug("refStdFeedBackY_PhaseDegreePhaseAngleControl: Entry"  );

			if(isRefStdFeedBackControlFlagEnabled()){
				float phaseY_Actualdegree =  Float.parseFloat(getFeedbackY_phaseDegree());
				if(ProcalFeatureEnable.KRE_REFSTD_CONNECTED ){
					//phaseR_Actualdegree = - phaseR_Actualdegree;
				}else{
					phaseY_Actualdegree = - phaseY_Actualdegree;
				}
				//float phaseR_Actualdegree = - Float.parseFloat(getFeedbackR_phaseDegree());
				ApplicationLauncher.logger.debug("refStdFeedBackY_PhaseDegreePhaseAngleControl: phaseY_Actualdegree:" + phaseY_Actualdegree);
				String phaseY_ActualdegreeStr = "000.000";

				phaseY_ActualdegreeStr = degreeFormatter.format( phaseY_Actualdegree);
				ApplicationLauncher.logger.debug("refStdFeedBackY_PhaseDegreePhaseAngleControl: phaseY_ActualdegreeStr:" + phaseY_ActualdegreeStr);
				String data =  ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR;


				float expected_pos_value = 0;
				float expected_neg_value = 0;

				int pfFeedBackYphaseMultiplierValue = Integer.parseInt(ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_DEFAULT_MULTIPLIER);

				float phaseY_TargetDegree = DeviceDataManagerController.get_PwrSrcY_PhaseDegreePhase();
				if(getCurrentTestPointName().startsWith(ConstantApp.EXPORT_MODE_ALIAS_NAME)){
					//phaseY_TargetDegree = -phaseY_TargetDegree;
					phaseY_TargetDegree = 360.0f + phaseY_TargetDegree;
					ApplicationLauncher.logger.debug("refStdFeedBackY_PhaseDegreePhaseAngleControl: phaseY_TargetDegree0:" + phaseY_TargetDegree);
				}
				expected_pos_value = phaseY_TargetDegree + ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_DEGREE_ALLOWED_UPPER_LIMIT;//0.2f;
				expected_neg_value = phaseY_TargetDegree - ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_DEGREE_ALLOWED_LOWER_LIMIT;//0.4f;


				ApplicationLauncher.logger.info("refStdFeedBackY_PhaseDegreePhaseAngleControl : expected_pos_value:"+ expected_pos_value);
				ApplicationLauncher.logger.info("refStdFeedBackY_PhaseDegreePhaseAngleControl : expected_neg_value:"+ expected_neg_value);
				ApplicationLauncher.logger.debug("refStdFeedBackY_PhaseDegreePhaseAngleControl: phaseY_Actualdegree:" + phaseY_Actualdegree);
				ApplicationLauncher.logger.debug("refStdFeedBackY_PhaseDegreePhaseAngleControl: phaseY_TargetDegree1:" + phaseY_TargetDegree);
				/*				if(getCurrentTestPointName().startsWith(ConstantApp.EXPORT_MODE_ALIAS_NAME)){
					//phaseY_Actualdegree =  360.0f - phaseY_Actualdegree;
					phaseY_Actualdegree =  - phaseY_Actualdegree;
					ApplicationLauncher.logger.debug("refStdFeedBackY_PhaseDegreePhaseAngleControl: phaseY_Actualdegree2:" + phaseY_Actualdegree);
				}else */

				if(phaseY_Actualdegree > 300.0f){
					phaseY_Actualdegree =  phaseY_Actualdegree - 360.0f;
					ApplicationLauncher.logger.debug("refStdFeedBackY_PhaseDegreePhaseAngleControl: phaseY_Actualdegree3:" + phaseY_Actualdegree);

				}


				if((phaseY_Actualdegree <= expected_pos_value)&&(phaseY_Actualdegree >= expected_neg_value)){
					ApplicationLauncher.logger.info("refStdFeedBackY_PhaseDegreePhaseAngleControl : degree phase angle in range");
					ApplicationLauncher.logger.info("refStdFeedBackY_PhaseDegreePhaseAngleControl : phase angle sent NoChange-1");

					data = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_PF1_NO_CHANGE;
					lastPowerSource_Y_PhaseAngleIncrement = false;
					lastPowerSource_Y_PhaseAngleDecrement = false;
				}else if(phaseY_Actualdegree > expected_pos_value){

					ApplicationLauncher.logger.info("refStdFeedBackY_PhaseDegreePhaseAngleControl : actual greater than upper limit");

					if(lastPowerSource_Y_PhaseAngleDecrement){
						lastPowerSource_Y_PhaseAngleIncrement = false;
						lastPowerSource_Y_PhaseAngleDecrement = false;
						ApplicationLauncher.logger.info("refStdFeedBackY_PhaseDegreePhaseAngleControl : phase angle sent NoChange-D1");
						data = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_PF1_NO_CHANGE;
					}else {
						ApplicationLauncher.logger.info("refStdFeedBackY_PhaseDegreePhaseAngleControl : decrementMode-B");
						data = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_PF2_DEC_LEVEL1;
						lastPowerSource_Y_PhaseAngleIncrement = false;
						lastPowerSource_Y_PhaseAngleDecrement= true;
					}
				}else{
					ApplicationLauncher.logger.info("refStdFeedBackY_PhaseDegreePhaseAngleControl : actual lesser than lower limit");

					if(lastPowerSource_Y_PhaseAngleIncrement){
						lastPowerSource_Y_PhaseAngleIncrement = false;
						lastPowerSource_Y_PhaseAngleDecrement = false;
						ApplicationLauncher.logger.info("refStdFeedBackY_PhaseDegreePhaseAngleControl : phase angle sent NoChange-F1");

						data = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_PF1_NO_CHANGE;
					}else {
						ApplicationLauncher.logger.info("refStdFeedBackY_PhaseDegreePhaseAngleControl : incrementMode-B");
						data = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_PF2_INC_LEVEL1;
						lastPowerSource_Y_PhaseAngleIncrement = true;
						lastPowerSource_Y_PhaseAngleDecrement= false;
					}
				}
				data = ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR + data;

				if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_MULTIPLIER_ENABLED) {
					data = data + String.valueOf(pfFeedBackYphaseMultiplierValue);

				}

				SerialDM_Obj.WriteToSerialCommPwrSrcV2(data);


			}

			return status;
		}


		public boolean refStdFeedBackB_PhaseDegreePhaseAngleControl(){
			boolean status = false;

			ApplicationLauncher.logger.debug("refStdFeedBackB_PhaseDegreePhaseAngleControl: Entry"  );

			if(isRefStdFeedBackControlFlagEnabled()){
				float phaseB_Actualdegree =  Float.parseFloat(getFeedbackB_phaseDegree());
				if(ProcalFeatureEnable.KRE_REFSTD_CONNECTED ){
					//phaseR_Actualdegree = - phaseR_Actualdegree;
				}else{
					phaseB_Actualdegree = - phaseB_Actualdegree;
				}
				//float phaseR_Actualdegree = - Float.parseFloat(getFeedbackR_phaseDegree());
				ApplicationLauncher.logger.debug("refStdFeedBackB_PhaseDegreePhaseAngleControl: phaseB_Actualdegree:" + phaseB_Actualdegree);
				String phaseB_ActualdegreeStr = "000.000";

				phaseB_ActualdegreeStr = degreeFormatter.format( phaseB_Actualdegree);
				ApplicationLauncher.logger.debug("refStdFeedBackB_PhaseDegreePhaseAngleControl: phaseB_ActualdegreeStr:" + phaseB_ActualdegreeStr);
				String data =  ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR;


				float expected_pos_value = 0;
				float expected_neg_value = 0;
				int pfFeedBackBphaseMultiplierValue = Integer.parseInt(ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_DEFAULT_MULTIPLIER);



				float phaseB_TargetDegree = DeviceDataManagerController.get_PwrSrcB_PhaseDegreePhase();
				if(getCurrentTestPointName().startsWith(ConstantApp.EXPORT_MODE_ALIAS_NAME)){
					//phaseB_TargetDegree = - phaseB_TargetDegree;
					phaseB_TargetDegree = 360.0f + phaseB_TargetDegree;
					ApplicationLauncher.logger.debug("refStdFeedBackB_PhaseDegreePhaseAngleControl: phaseB_TargetDegree0:" + phaseB_TargetDegree);
				}
				expected_pos_value = phaseB_TargetDegree + ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_DEGREE_ALLOWED_UPPER_LIMIT;//0.2f;
				expected_neg_value = phaseB_TargetDegree - ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_DEGREE_ALLOWED_LOWER_LIMIT;//0.4f;


				ApplicationLauncher.logger.info("refStdFeedBackB_PhaseDegreePhaseAngleControl : expected_pos_value:"+ expected_pos_value);
				ApplicationLauncher.logger.info("refStdFeedBackB_PhaseDegreePhaseAngleControl : expected_neg_value:"+ expected_neg_value);
				ApplicationLauncher.logger.debug("refStdFeedBackB_PhaseDegreePhaseAngleControl: phaseB_Actualdegree:" + phaseB_Actualdegree);
				ApplicationLauncher.logger.debug("refStdFeedBackB_PhaseDegreePhaseAngleControl: phaseB_TargetDegree1:" + phaseB_TargetDegree);
				/*				if(getCurrentTestPointName().startsWith(ConstantApp.EXPORT_MODE_ALIAS_NAME)){
					//phaseB_Actualdegree =  360.0f - phaseB_Actualdegree;
					phaseB_Actualdegree =  - phaseB_Actualdegree;
					ApplicationLauncher.logger.debug("refStdFeedBackY_PhaseDegreePhaseAngleControl: phaseB_Actualdegree2:" + phaseB_Actualdegree);
				}else */

				if(phaseB_Actualdegree > 300.0f){
					phaseB_Actualdegree =  phaseB_Actualdegree - 360.0f;
					ApplicationLauncher.logger.debug("refStdFeedBackB_PhaseDegreePhaseAngleControl: phaseB_Actualdegree3:" + phaseB_Actualdegree);

				}


				if((phaseB_Actualdegree <= expected_pos_value)&&(phaseB_Actualdegree >= expected_neg_value)){
					ApplicationLauncher.logger.info("refStdFeedBackB_PhaseDegreePhaseAngleControl : degree phase angle in range");
					ApplicationLauncher.logger.info("refStdFeedBackB_PhaseDegreePhaseAngleControl : phase angle sent NoChange-1");

					data = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_PF1_NO_CHANGE;
					lastPowerSource_B_PhaseAngleIncrement = false;
					lastPowerSource_B_PhaseAngleDecrement = false;
				}else if(phaseB_Actualdegree > expected_pos_value){

					ApplicationLauncher.logger.info("refStdFeedBackB_PhaseDegreePhaseAngleControl : actual greater than upper limit");

					if(lastPowerSource_B_PhaseAngleDecrement){
						lastPowerSource_B_PhaseAngleIncrement = false;
						lastPowerSource_B_PhaseAngleDecrement = false;
						ApplicationLauncher.logger.info("refStdFeedBackB_PhaseDegreePhaseAngleControl : phase angle sent NoChange-D1");
						data = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_PF1_NO_CHANGE;
					}else {
						ApplicationLauncher.logger.info("refStdFeedBackB_PhaseDegreePhaseAngleControl : decrementMode-B");
						data = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_PF3_DEC_LEVEL1;
						lastPowerSource_B_PhaseAngleIncrement = false;
						lastPowerSource_B_PhaseAngleDecrement= true;
					}
				}else{
					ApplicationLauncher.logger.info("refStdFeedBackB_PhaseDegreePhaseAngleControl : actual lesser than lower limit");

					if(lastPowerSource_B_PhaseAngleIncrement){
						lastPowerSource_B_PhaseAngleIncrement = false;
						lastPowerSource_B_PhaseAngleDecrement = false;
						ApplicationLauncher.logger.info("refStdFeedBackB_PhaseDegreePhaseAngleControl : phase angle sent NoChange-F1");

						data = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_PF1_NO_CHANGE;
					}else {
						ApplicationLauncher.logger.info("refStdFeedBackB_PhaseDegreePhaseAngleControl : incrementMode-B");
						data = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_PF3_INC_LEVEL1;
						lastPowerSource_B_PhaseAngleIncrement = true;
						lastPowerSource_B_PhaseAngleDecrement= false;
					}
				}
				data = ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR + data;
				if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_MULTIPLIER_ENABLED) {
					data = data + String.valueOf(pfFeedBackBphaseMultiplierValue);

				}
				SerialDM_Obj.WriteToSerialCommPwrSrcV2(data);


			}

			return status;
		}



		public void sendRefStdDataToLscsPowerSource(){
			boolean sendPfIncrementDecrement = true;//false;//false;
			ApplicationLauncher.logger.debug("sendRefStdDataToLscsPowerSource: Entry"  );
			if(isRefStdFeedBackControlFlagEnabled()){
				if(GuiUtils.is_float(getFeedbackR_phaseVolt())){
					if(GuiUtils.is_float(getFeedbackR_phaseCurrent())){
						float phaseR_Actualdegree = - Float.parseFloat(getFeedbackR_phaseDegree());
						//if(GUIUtils.is_float(getFeedbackR_phaseDegree())){

						float phaseR_ActualVolt = Float.parseFloat(getFeedbackR_phaseVolt());
						float phaseR_ActualCurrent = Float.parseFloat(getFeedbackR_phaseCurrent());


						ApplicationLauncher.logger.debug("sendRefStdDataToLscsPowerSource: phaseR_Actualdegree:" + phaseR_Actualdegree);
						//if(phaseR_Actualdegree > 300.0f){
						//phaseR_Actualdegree =  phaseR_Actualdegree - 360.0f;
						//ApplicationLauncher.logger.debug("sendRefStdDataToLscsPowerSource: phaseR_Actualdegree2:" + phaseR_Actualdegree);

						//}
						String phaseR_ActualVoltStr = "000.000";
						String phaseR_ActualCurrentStr = ".000000";
						String phaseR_ActualdegreeStr = "000.000";

						phaseR_ActualVoltStr = voltageFormatter.format( phaseR_ActualVolt);
						if(phaseR_ActualCurrent < 1.0){
							phaseR_ActualCurrentStr = currentFormatterLessThan1Amps.format( phaseR_ActualCurrent);
						}else if(phaseR_ActualCurrent < 10.0){
							phaseR_ActualCurrentStr = currentFormatterLessThan10Amps.format( phaseR_ActualCurrent);
						}else {
							phaseR_ActualCurrentStr = currentFormatterMoreThan10Amps.format( phaseR_ActualCurrent);
						}

						phaseR_ActualdegreeStr = degreeFormatter.format( phaseR_Actualdegree);

						ApplicationLauncher.logger.debug("sendRefStdDataToLscsPowerSource: phaseR_ActualVoltStr:" + phaseR_ActualVoltStr);
						ApplicationLauncher.logger.debug("sendRefStdDataToLscsPowerSource: phaseR_ActualCurrentStr:" + phaseR_ActualCurrentStr);
						ApplicationLauncher.logger.debug("sendRefStdDataToLscsPowerSource: phaseR_ActualdegreeStr:" + phaseR_ActualdegreeStr);


						String data =  phaseR_ActualVoltStr;//"v1";
						SerialDM_Obj.WriteToSerialCommPwrSrcV2(data);
						//data = ConstantLscsPowerSource.CMD_PWR_SRC_SEPERATOR2;//",";
						SerialDM_Obj.WriteToSerialCommPwrSrcV2(ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR2);
						//data = "000.000";//v2
						//data = "." + ConstantLscsPowerSource.CMD_PWR_SRC_SEPERATOR;//v2
						data = ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR;//v2
						SerialDM_Obj.WriteToSerialCommPwrSrcV2(data);
						//data = ConstantLscsPowerSource.CMD_PWR_SRC_SEPERATOR2;//",";
						SerialDM_Obj.WriteToSerialCommPwrSrcV2(ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR2);
						//data = "000.000";//"v3";
						//data = "." + ConstantLscsPowerSource.CMD_PWR_SRC_SEPERATOR;//v3
						data = ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR;//v3
						SerialDM_Obj.WriteToSerialCommPwrSrcV2(data);
						//data = ConstantLscsPowerSource.CMD_PWR_SRC_SEPERATOR2;//",";
						SerialDM_Obj.WriteToSerialCommPwrSrcV2(ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR2);
						data = phaseR_ActualCurrentStr;//"i1";
						SerialDM_Obj.WriteToSerialCommPwrSrcV2(data);
						//data = ConstantLscsPowerSource.CMD_PWR_SRC_SEPERATOR2;//",";
						SerialDM_Obj.WriteToSerialCommPwrSrcV2(ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR2);
						//data = ".000000";//"i2";
						//data = "." + ConstantLscsPowerSource.CMD_PWR_SRC_SEPERATOR;//i2
						data = ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR;//i2
						SerialDM_Obj.WriteToSerialCommPwrSrcV2(data);
						//data = ConstantLscsPowerSource.CMD_PWR_SRC_SEPERATOR2;//",";
						SerialDM_Obj.WriteToSerialCommPwrSrcV2(ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR2);
						//data = ".000000";//"i3";
						//data = "." + ConstantLscsPowerSource.CMD_PWR_SRC_SEPERATOR;//i3
						data = ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR;//i3
						SerialDM_Obj.WriteToSerialCommPwrSrcV2(data);
						//data = ConstantLscsPowerSource.CMD_PWR_SRC_SEPERATOR2;//",";
						SerialDM_Obj.WriteToSerialCommPwrSrcV2(ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR2);




						if(sendPfIncrementDecrement){
							//data = " ";
							SerialDM_Obj.WriteToSerialCommPwrSrcV2(ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR);
							float expected_pos_value = 0;
							float expected_neg_value = 0;


							float phaseR_TargetDegree = DeviceDataManagerController.get_PwrSrcR_PhaseDegreePhase();
							expected_pos_value = phaseR_TargetDegree + ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_DEGREE_ALLOWED_UPPER_LIMIT;//0.2f;
							expected_neg_value = phaseR_TargetDegree - ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_DEGREE_ALLOWED_LOWER_LIMIT;//0.4f;

							if( (getCurrentTestPointName().contains("0.5L"))){// && (getCurrentTestPointName().startsWith(ConstantApp.CALIBRATION_ALIAS_NAME))) {
								//expected_pos_value = phaseR_TargetDegree + 0.08f;//0.2f; //#18Feb2022
								//expected_neg_value = phaseR_TargetDegree - 0.4f;//0.4f;//#18Feb2022
								lastRefStdRead_R_PhaseDegreePhaseAngle = phaseR_Actualdegree;
							}

							ApplicationLauncher.logger.info("sendRefStdDataToLscsPowerSource : expected_pos_value:"+ expected_pos_value);
							ApplicationLauncher.logger.info("sendRefStdDataToLscsPowerSource : expected_neg_value:"+ expected_neg_value);
							ApplicationLauncher.logger.debug("sendRefStdDataToLscsPowerSource: phaseR_Actualdegree:" + phaseR_Actualdegree);
							ApplicationLauncher.logger.debug("sendRefStdDataToLscsPowerSource: phaseR_TargetDegree3:" + phaseR_TargetDegree);
							if(phaseR_Actualdegree > 300.0f){
								phaseR_Actualdegree =  phaseR_Actualdegree - 360.0f;
								ApplicationLauncher.logger.debug("sendRefStdDataToLscsPowerSource: phaseR_Actualdegree2:" + phaseR_Actualdegree);

							}

							float differenceWithLastReading = Math.abs(lastRefStdRead_R_PhaseDegreePhaseAngle - phaseR_Actualdegree);
							ApplicationLauncher.logger.info("sendRefStdDataToLscsPowerSource : lastRefStdRead_R_PhaseCurrent: " + lastRefStdRead_R_PhaseDegreePhaseAngle);
							ApplicationLauncher.logger.info("sendRefStdDataToLscsPowerSource : differenceWithLastReading: " + differenceWithLastReading);
							boolean differenceMoreThanAllowedLimits = false;
							if ((differenceWithLastReading > 0.2f) || (phaseR_Actualdegree > (phaseR_TargetDegree+0.5f))){
								lastPowerSource_R_PhaseAngleIncrement = false;
								lastPowerSource_R_PhaseAngleDecrement = false;
								differenceMoreThanAllowedLimits = true;
								ApplicationLauncher.logger.info("sendRefStdDataToLscsPowerSource : Inc/Dec allowed");

							}

							if((phaseR_Actualdegree <= expected_pos_value)&&(phaseR_Actualdegree >= expected_neg_value)){
								ApplicationLauncher.logger.info("sendRefStdDataToLscsPowerSource : degree phase angle in range");
								//targetAchieved = true;
								//status = true;
								ApplicationLauncher.logger.info("sendRefStdDataToLscsPowerSource : phase angle sent NoChange-1");

								data = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_PF1_NO_CHANGE;
								lastPowerSource_R_PhaseAngleIncrement = false;
								lastPowerSource_R_PhaseAngleDecrement = false;
							}else if(phaseR_Actualdegree > expected_pos_value){
								//mode = decrementMode;

								if( (getCurrentTestPointName().contains("0.5L"))){
									if(lastPowerSource_R_PhaseAngleDecrement){
										lastPowerSource_R_PhaseAngleIncrement = false;
										lastPowerSource_R_PhaseAngleDecrement = false;
										ApplicationLauncher.logger.info("sendRefStdDataToLscsPowerSource : phase angle sent NoChange-D1");
										data = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_PF1_NO_CHANGE;
									}else{
										ApplicationLauncher.logger.info("sendRefStdDataToLscsPowerSource : decrementMode-A");
										data = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_PF1_DEC_LEVEL1;
										lastPowerSource_R_PhaseAngleIncrement = false;
										lastPowerSource_R_PhaseAngleDecrement= true;
									}

								}else{
									if(differenceMoreThanAllowedLimits){
										ApplicationLauncher.logger.info("sendRefStdDataToLscsPowerSource : decrementMode-B");
										data = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_PF1_DEC_LEVEL1;
										lastPowerSource_R_PhaseAngleIncrement = false;
										lastPowerSource_R_PhaseAngleDecrement= true;
									}else{
										lastPowerSource_R_PhaseAngleIncrement = false;
										lastPowerSource_R_PhaseAngleDecrement = false;
										ApplicationLauncher.logger.info("sendRefStdDataToLscsPowerSource : phase angle sent NoChange-D2");
										data = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_PF1_NO_CHANGE;
									}
								}
								//SerialDM_Obj.lscsSetPowerSourcePhaseAngleFineTune(userConfiguredSelectedPhaseName,levelAdjustment,decrementMode);
								//Sleep(4500);
							}else{
								//mode = incrementMode;
								if( (getCurrentTestPointName().contains("0.5L"))){
									if(lastPowerSource_R_PhaseAngleIncrement){
										lastPowerSource_R_PhaseAngleIncrement = false;
										lastPowerSource_R_PhaseAngleDecrement = false;
										ApplicationLauncher.logger.info("sendRefStdDataToLscsPowerSource : phase angle sent NoChange-F1");

										data = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_PF1_NO_CHANGE;
									}else{
										ApplicationLauncher.logger.info("sendRefStdDataToLscsPowerSource : incrementMode-A");
										data = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_PF1_INC_LEVEL1;
										lastPowerSource_R_PhaseAngleIncrement = true;
										lastPowerSource_R_PhaseAngleDecrement= false;
									}
								}else{
									if(differenceMoreThanAllowedLimits){
										ApplicationLauncher.logger.info("sendRefStdDataToLscsPowerSource : incrementMode-B");
										data = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_PF1_INC_LEVEL1;
										lastPowerSource_R_PhaseAngleIncrement = true;
										lastPowerSource_R_PhaseAngleDecrement= false;
									}else{
										lastPowerSource_R_PhaseAngleIncrement = false;
										lastPowerSource_R_PhaseAngleDecrement = false;
										ApplicationLauncher.logger.info("sendRefStdDataToLscsPowerSource : phase angle sent NoChange-F2");

										data = ConstantPowerSourceLscs.CMD_PWR_SRC_FINE_CONTROL_PF1_NO_CHANGE;
									}
								}
								//SerialDM_Obj.lscsSetPowerSourcePhaseAngleFineTune(userConfiguredSelectedPhaseName,levelAdjustment,incrementMode);
								//Sleep(4500);
							}
							SerialDM_Obj.WriteToSerialCommPwrSrcV2(data);
						}else{
							data = phaseR_ActualdegreeStr;//"p1";
							SerialDM_Obj.WriteToSerialCommPwrSrcV2(data);
							//data = ",";
							SerialDM_Obj.WriteToSerialCommPwrSrcV2(ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR2);
						}


						//data = "000.000";//"p2";
						//data = "." + ConstantLscsPowerSource.CMD_PWR_SRC_SEPERATOR;//p2
						data = ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR;//p2
						SerialDM_Obj.WriteToSerialCommPwrSrcV2(data);
						//data = ",";
						SerialDM_Obj.WriteToSerialCommPwrSrcV2(ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR2);
						//data = "000.000";//"p3";
						//data = "." + ConstantLscsPowerSource.CMD_PWR_SRC_SEPERATOR;//p3
						data = ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR;//p3
						SerialDM_Obj.WriteToSerialCommPwrSrcV2(data);
						//data = ",";
						SerialDM_Obj.WriteToSerialCommPwrSrcV2(ConstantPowerSourceLscs.CMD_PWR_SRC_SEPERATOR2);

						//}else{
						//	ApplicationLauncher.logger.debug("sendRefStdDataToLscsPowerSource: getFeedbackR_phaseDegree: "  + getFeedbackR_phaseDegree());
						//}
					}else{
						ApplicationLauncher.logger.debug("sendRefStdDataToLscsPowerSource: getFeedbackR_phaseCurrent: "  + getFeedbackR_phaseCurrent());
					}
				}else{
					ApplicationLauncher.logger.debug("sendRefStdDataToLscsPowerSource: getFeedbackR_phaseVolt: "  + getFeedbackR_phaseVolt());
				}
			}else{
				ApplicationLauncher.logger.debug("sendRefStdDataToLscsPowerSource: isRefStdFeedBackControlFlagEnabled: "  + isRefStdFeedBackControlFlagEnabled());
			}
			ApplicationLauncher.logger.debug("sendRefStdDataToLscsPowerSource: Exit"  );
		}


		public boolean refStdFeedBackControl(){
			boolean status = false;
			ApplicationLauncher.logger.debug("refStdFeedBackControl: Entry"  );

			/*		if(ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED) {
			status = waitForPowerSrcFeedBackControlReady();
			refStdFeedBackPhaseAngleControl();
		}*/
			lastRefStdRead_R_PhaseDegreePhaseAngle = 0.0f;
			lastPowerSource_R_PhaseAngleIncrement = false;
			lastPowerSource_R_PhaseAngleDecrement= false;

			lastPowerSource_Y_PhaseAngleIncrement = false;
			lastPowerSource_Y_PhaseAngleDecrement= false;

			lastPowerSource_B_PhaseAngleIncrement = false;
			lastPowerSource_B_PhaseAngleDecrement= false;

			setLastRefStdFeedBackRead_R_PhaseCurrent(0.0f);
			setLastPowerSource_R_CurrentIncrement(false);
			setLastPowerSource_R_CurrentDecrement(false);

			setLastRefStdFeedBackRead_Y_PhaseCurrent(0.0f);
			setLastPowerSource_Y_CurrentIncrement(false);
			setLastPowerSource_Y_CurrentDecrement(false);

			setLastRefStdFeedBackRead_B_PhaseCurrent(0.0f);
			setLastPowerSource_B_CurrentIncrement(false);
			setLastPowerSource_B_CurrentDecrement(false);



			lastRefStdFeedBackRead_R_PhaseVolt = 0.0f;
			lastPowerSource_R_VoltIncrement = false;
			lastPowerSource_R_CurrentDecrement = false;

			lastRefStdFeedBackRead_Y_PhaseVolt = 0.0f;
			lastPowerSource_Y_VoltIncrement = false;
			lastPowerSource_Y_VoltDecrement = false;

			lastRefStdFeedBackRead_B_PhaseVolt = 0.0f;
			lastPowerSource_B_VoltIncrement = false;
			lastPowerSource_B_VoltDecrement = false;
			feedbackAdjustmentDoubleConfirmed_R_VoltDecrement = true;
			feedbackAdjustmentDoubleConfirmed_R_VoltIncrement = false;
			feedbackAdjustmentDoubleConfirmed_R_CurrentDecrement = true;
			feedbackAdjustmentDoubleConfirmed_R_CurrentIncrement = false;
			feedbackAdjustmentDoubleConfirmed_R_CurrentIncrementCounter = 0;
			feedbackAdjustmentDoubleConfirmed_R_CurrentDecrementCounter =0;

			feedbackAdjustmentDoubleConfirmed_R_PfDecrement = true;
			feedbackAdjustmentDoubleConfirmed_R_PfIncrement = false;
			feedbackAdjustmentDoubleConfirmed_R_PfIncrementCounter = 0;
			feedbackAdjustmentDoubleConfirmed_R_PfDecrementCounter = 0;


			refStdFeedBackControlTrigger();
			int retryCount = 20;
			while( (!getUserAbortedFlag()) && (retryCount > 0) ){
				ApplicationLauncher.logger.debug("refStdFeedBackControl: retryCount: " + retryCount  );
				Sleep(1000);
				retryCount--;
			}
			//Sleep(20000);//10000
			ApplicationLauncher.logger.debug("refStdFeedBackControl: Exit"  );//#18Feb2022
			ApplicationLauncher.logger.debug("refStdFeedBackControl : +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++" );
			ApplicationLauncher.logger.debug("refStdFeedBackControl : +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++" );
			return status;		
		}

		public void RepeatabilityExecuteStart(){


			ApplicationLauncher.logger.info("Repeatability Test Started"  );


			int timeduration = DeviceDataManagerController.getInfTimeDuration();

			DisplayDataObj.setVoltageResetRequired(false);
			FailureManager.ResetPowerSrcReasonForFailure();
			DisplayPwrSrc_TurnOn();
			if(ProcalFeatureEnable.LSCS_LDU_CONNECTED) {
				lscsClearLDU_LastStoredResults();
				lscsClearLduSecondaryDisplay();
				lscsLDU_ResetError();
			}else if(ProcalFeatureEnable.BOFA_LDU_CONNECTED){
				Data_LduBofa.bofaSendLduExitDialTest();//bofaSendLduResetErrorAll();
			}
			if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
				WaitForPowerSrcTurnOn();
			}else if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){ 
				waitBufferTimeForBofaPowerSourceOn();
			}
			/*if( getCurrentTestPointName().endsWith("-1")){


			Sleep((ConstantConfig.POWERONWAITCOUNTER*1000)/2); //Added sleep ...bcoz there was huge error(+2%) from switching from 1.0ib to 0.05ib first test case

		}
		LDU_PreRequisiteForReadError();*/


			if(FirstLevelValidation()){
				if(SecondLevelValidation()){

					if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_ENABLED){
						refStdFeedBackControl();
						setRefStdFeedBackControlSuspended(false);
					}

					if(ProcalFeatureEnable.CCUBE_LDU_CONNECTED) {
						LDU_PreRequisiteForReadErrorV2();
						DisplayLDU_ReadErrorTimerInitV2();
					}else if(ProcalFeatureEnable.LSCS_LDU_CONNECTED){
						//lscsClearLDU_LastStoredResults();
						//lscsClearLduSecondaryDisplay();
						lscsLDU_PreRequisiteForReadError();
						lscsDisplayLDU_ReadErrorTimerInit();

					}else if(ProcalFeatureEnable.BOFA_LDU_CONNECTED){
						Data_RefStdBofa.setRsmFreqReadingRequired(false);
						if(Float.parseFloat(DeviceDataManagerController.getR_PhaseOutputCurrent())> 0.0f ){
							
							Data_RefStdBofa.sendReadConstOfLiveRefStdCmd();  
						}
						Data_LduBofa.bofaLDU_PreRequisiteForReadError();
						Data_LduBofa dataLduBofa = new Data_LduBofa();
						dataLduBofa.bofaDisplayLDU_ReadErrorTimerInit();

					}
					setExecuteTimeCounter(timeduration+Rep_SelfHeating_BufferTimeToReadLDU_DataInSec);
					ExecuteTimerDisplay();
					UI_TableRefreshTrigger(timeduration);
				}
				else{
					SkipCurrentTestPointGUI_Update();
				}
			}
			else{
				SkipCurrentTestPointGUI_Update();
			}

		}

		public void CreepExecuteStart(){

			ApplicationLauncher.logger.info("Creep Test Started"  );
			setLscsZeroCurrentPowerTurnOn(true);
			//boolean meterAlreadyTurnedOnToCaptureReading = false;
			FailureManager.ResetPowerSrcReasonForFailure();
			if(ProcalFeatureEnable.LSCS_LDU_CONNECTED) {
				lscsClearLDU_LastStoredResults();
				lscsClearLduSecondaryDisplay();
				lscsLDU_ResetError();
			}
			else if(ProcalFeatureEnable.BOFA_LDU_CONNECTED){
				Data_LduBofa.bofaSendLduExitDialTest();//bofaSendLduResetErrorAll();
			}

			if( (getCurrentTestPoint_Index() == 0) || (getStepRunFlag()) ){// when executed after Constant test voltage not powered up
				ZeroCurrentPowerOn();
				//meterAlreadyTurnedOnToCaptureReading = true;
				//Sleep(5000);
				if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
					WaitForPowerSrcTurnOn();
				}
				if(SerialDataManager.getPowerSrcOnFlag()){
					if(ProcalFeatureEnable.CREEP_KWH_READING_PROMPT_ENABLED) {
						getInitMeterValuesFromUI();// Get Initial value
						waitForInitMeterReading();//User enter readings
					}

				}else if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){ 
					waitBufferTimeForBofaPowerSourceOn();
				}
				DisplayDataObj.setVoltageResetRequired(false);
			}
			else{
				DisplayDataObj.setVoltageResetRequired(false);
				ApplicationLauncher.logger.debug("CreepExecuteStart : Test1");
				DisplayPwrSrc_TurnOn();
				ApplicationLauncher.logger.debug("CreepExecuteStart : Test2");
				//Sleep(15000);
				if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
					WaitForPowerSrcTurnOn();
				}
				ApplicationLauncher.logger.debug("CreepExecuteStart : Test3");
				if(SerialDataManager.getPowerSrcOnFlag()){
					ApplicationLauncher.logger.debug("CreepExecuteStart : Test4");
					if(ProcalFeatureEnable.CREEP_KWH_READING_PROMPT_ENABLED) {
						getInitMeterValuesFromUI();// Get Initial value
						waitForInitMeterReading();//User enter readings
					}
				}
				ApplicationLauncher.logger.debug("CreepExecuteStart : Test5");

			}



			Integer CreepDuration = DisplayDataObj.get_CreepDuration();

			//DisplayDataObj.setVoltageResetRequired(false);
			//FailureManager.ResetPowerSrcReasonForFailure();
			//if(!meterAlreadyTurnedOnToCaptureReading){
			//	DisplayPwrSrc_TurnOn();
			//}
			//WaitForPowerSrcTurnOn();
			//LDU_PreRequisiteForCreepTest();


			if(FirstLevelValidation()){
				if((!ProcalFeatureEnable.REFSTD_CONNECTED_NONE)){
					if(SecondLevelValidation()){
	
						if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_ENABLED){
							refStdFeedBackControl();
							setRefStdFeedBackControlSuspended(false);
						}
	
						if(ProcalFeatureEnable.CCUBE_LDU_CONNECTED) {
							LDU_PreRequisiteForCreepTestV2();
							DisplayLDU_ReadCreepTimerInitV2();
						} else if(ProcalFeatureEnable.LSCS_LDU_CONNECTED) { 
							//LDU_PreRequisiteForCreepTestV2();
							//DisplayLDU_ReadCreepTimerInitV2();
							//lscsClearLDU_LastStoredResults();
							//lscsClearLduSecondaryDisplay();
							lscsLDU_PreRequisiteForCreepTest();
							/*Sleep(60000);
						lscsLDU_StopCreepTest();
						displayLscsLDU_ReadCreepTimerInit();
							 */					
						}else if(ProcalFeatureEnable.BOFA_LDU_CONNECTED) {
	
							Data_LduBofa.bofaLduSendStartCRP();	// send the ldu creep test command to bofa ldu here
	
						}
						//DisplayLDU_ReadCreepTimerInit();
						setExecuteTimeCounter(CreepDuration+Creep_BufferTimeToReadLDU_DataInSec);
						ExecuteTimerDisplay();
						UI_TableRefreshTrigger(CreepDuration);
	
						if(ProcalFeatureEnable.LSCS_LDU_CONNECTED) {
	
							int lduTimeDuration = CreepDuration;
							while ( (!getUserAbortedFlag()) && (lduTimeDuration!=0) && (!getUserAbortedFlag())){
								Sleep(1000);
								lduTimeDuration--;
								ApplicationHomeController.update_left_status("Creep: " + lduTimeDuration + " Sec",ConstantApp.LEFT_STATUS_DEBUG);
	
							}
							lscsLDU_StopCreepTest();
							if(!getUserAbortedFlag()){
								displayLscsLDU_ReadCreepTimerInit();
							}
	
						}
						else if(ProcalFeatureEnable.BOFA_LDU_CONNECTED) {
	
	
							int lduTimeDuration = CreepDuration;//DisplayDataObj.get_STADuration();
							while ( (!getUserAbortedFlag()) && (lduTimeDuration!=0) ){
								Sleep(1000);
								lduTimeDuration--;
								ApplicationHomeController.update_left_status("CRP: " + lduTimeDuration + " Sec",ConstantApp.LEFT_STATUS_DEBUG);
	
							}
							//lscsLDU_StopSTA_Test();
							boolean status = false;
							status = Data_PowerSourceBofa.sendCurrentStoppingCommand(); // send the stop current command to bofa source here
							if (status) {
								if(!getUserAbortedFlag()){
	
									//displayLscsLDU_ReadSTA_TimerInit(); //updated by gopi - for the refresh to read the device before exiting the LDUreadbuffertime 
									Data_LduBofa lduBofaObj = new Data_LduBofa();
									lduBofaObj.bofaLduTriggerReadCRPPulseCount(); // trigger the command to read the pulse count from LDU
	
	
								}
							}
							else{
							}
						}
					}
					else{
						SkipCurrentTestPointGUI_Update();
					}
				}else{
					ApplicationLauncher.logger.debug("CreepExecuteStart : REFSTD_CONNECTED_NONE" );
					
					if(ProcalFeatureEnable.LSCS_LDU_CONNECTED) { 

						lscsLDU_PreRequisiteForCreepTest();				
					}
					//setExecuteTimeCounter(CreepDuration+BufferTimeToReadLDU_DataInSec);
					setExecuteTimeCounter(CreepDuration+Creep_BufferTimeToReadLDU_DataInSec);
					ExecuteTimerDisplay();
					UI_TableRefreshTrigger(CreepDuration);
					if(ProcalFeatureEnable.LSCS_LDU_CONNECTED) {
						
						int lduTimeDuration = CreepDuration;
						while ( (!getUserAbortedFlag()) && (lduTimeDuration!=0) && (!getUserAbortedFlag())){
							Sleep(1000);
							lduTimeDuration--;
							ApplicationHomeController.update_left_status("Creep: " + lduTimeDuration + " Sec",ConstantApp.LEFT_STATUS_DEBUG);

						}
						lscsLDU_StopCreepTest();
						if(!getUserAbortedFlag()){
							displayLscsLDU_ReadCreepTimerInit();
						}

					}
					if(ProcalFeatureEnable.PROPOWER_SRC_FEEDBACK_DISPLAY ){
	
						ApplicationLauncher.logger.debug("CreepExecuteStart : ProPower Source Feedback display" );
						manipulateGainOffsetValueForFeedBackProcess();
						SerialDM_Obj.lscsPowerSourceReadFeedBackTimerInit();
					}
	
					if(ProcalFeatureEnable.PROPOWER_SRC_ONLY ){
	
						//int noOfTestPointCompleted = LiveTableDataManager.getCountOfNoOfTestPointCompleted();
						int totalNoOfTestPointIndex  = getCurrentProjectTestPointList().length()-1;
						ApplicationLauncher.logger.debug("CreepExecuteStart : totalNoOfTestPointIndex: " + totalNoOfTestPointIndex );
						ApplicationLauncher.logger.debug("CreepExecuteStart : CurrentTestPoint_Index: " + CurrentTestPoint_Index );
	
						EnableStopButton();
						if(CurrentTestPoint_Index < totalNoOfTestPointIndex){
							enableBtnLoadNext();
						}
					}
				}
			}
			else{
				SkipCurrentTestPointGUI_Update();
			}


		}

		public boolean RefStdRelaySetting(){
			boolean status = false;
			ApplicationLauncher.logger.info("RefStdRelaySetting : Entry");
			status = UnlockRelays();
			ApplicationLauncher.logger.info("RefStdRelaySetting : UnlockRelays: " + status);
			if(status){
				status = true;
				//status=ChangeToCorrectTap();

				ApplicationLauncher.logger.info("RefStdRelaySetting : ChangeToCorrectTap: " + status);
			}

			if(RefStdRelaySettingFlag){
				if(!status){
					updateSkippedInDisplay();
				}
				return status;
			}
			else{
				return true;
			}

		}

		public boolean UnlockRelays(){
			boolean status = false;
			try {
				status = SerialDM_Obj.RefStd_UnlockVoltageRelay();
				ApplicationLauncher.logger.info("UnlockRelays : RefStd_UnlockVoltageRelay: " + status);

			} catch (UnsupportedEncodingException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("UnlockRelays : UnsupportedEncodingException:"+e.getMessage());
			}
			return status;
		}

		public boolean ChangeToCorrectTap(){
			boolean status = false;
			String volt = DeviceDataManagerController.getR_PhaseOutputVoltage();
			DeviceDataManagerController.getR_PhaseOutputCurrent();
			try {
				status = SerialDM_Obj.RefStd_SetVoltageRelay(volt);
				ApplicationLauncher.logger.info("ChangeToCorrectTap : RefStd_SetVoltageRelay: "  + status);
				/*			if(status){
				status = SerialDM_Obj.RefStd_SetCurrentRelay(current);
				ApplicationLauncher.logger.info("ChangeToCorrectTap : RefStd_SetCurrentRelay: "  + status);
			}*/
			} catch (UnsupportedEncodingException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("ChangeToCorrectTap : UnsupportedEncodingException:"+e.getMessage());
			}

			//String pulsevalue = SerialDM_Obj.RefStd_GetRSSPulseRate(current);
			//DisplayDataObj.setRSSPulseRate(pulsevalue);
			return status;
		}





		@FXML
		public void StepRunTrigger() {
			ApplicationLauncher.logger.info("StepRunTrigger: Entry");
			stepRunTimer = new Timer();
			stepRunTimer.schedule(new StepRunTaskClick(), 50);

		}
		
		public void procalRemoteStepRunTrigger() {
			ApplicationLauncher.logger.info("procalRemoteStepRunTrigger: Entry");
			stepRunTimer = new Timer();
			stepRunTimer.schedule(new RemoteStepRunTaskClick(), 50);

		}
		
		
		
		class RemoteStepRunTaskClick extends TimerTask {
			public void run() {
				remoteStepRunOnClick();
				stepRunTimer.cancel();


			}
		}
		
		public void remoteStepRunOnClick(){
			ApplicationLauncher.logger.info("remoteStepRunOnClick: Entry");
			setStepRunFlag(true);
			setResumeFlag(false);
			try {
				
				setDutCalibrationVoltageTargetSet(false);
				setDutCalibrationCurrentTargetSet(false);
				setDutCalibrationCurrentZeroSet(false);
				setDutCalibrationVoltCurrentSetZero(false);
				setCurrentTestPoint_Index(0);
				Sleep(1000);
				UpdateDevicesDataTableRowHighlight();
				Sleep(1000);
				StartTestExecution();
			} catch (JSONException  e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("remoteStepRunOnClick : JSONException:"+e.getMessage());
			}catch (ParseException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("remoteStepRunOnClick : ParseException:"+e.getMessage());
			}
		}
		
		

		class StepRunTaskClick extends TimerTask {
			public void run() {
				StepRunOnClick();
				stepRunTimer.cancel();


			}
		}
		public void StepRunOnClick(){
			ApplicationLauncher.logger.info("StepRunOnClick: Entry");
			setStepRunFlag(true);
			setResumeFlag(false);
			try {
				StartTestExecution();
			} catch (JSONException  e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("StepRunOnClick : JSONException:"+e.getMessage());
			}catch (ParseException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("StepRunOnClick : ParseException:"+e.getMessage());
			}
		}

		public void setCurrentTestType(String value){
			CurrentTestType = value;
		}

		public static String getCurrentTestType(){
			return CurrentTestType;
		}




		public void execute_test(JSONObject testcasedetails) throws JSONException{
			ApplicationLauncher.logger.info("execute_test : Entry");
			ApplicationLauncher.LoadConfigProperty();
			ApplicationHomeController.update_left_status("Execute test init",ConstantApp.LEFT_STATUS_DEBUG);
			System.gc();
			setExecuteTimeCounter(0);
			FailureManager.ResetRefStdFeedBackReasonForFailure();
			FailureManager.ResetPowerSrcReasonForFailure();
			setCurrentTestPoint_ExecutionCompletedStatus(false);
			String test_type = testcasedetails.getString("test_type");
			setCurrentTestType(test_type);
			ApplicationLauncher.logger.info("execute_test : test_type: " + test_type);
			String project_name = getCurrentProjectName();//MeterParamsController.getCurrentProjectName();
			setCurrentTestPointName(testcasedetails.getString("test_case"));
			ApplicationLauncher.logger.info("execute_test : getCurrentTestPointName: " + getCurrentTestPointName());
			getPresentTestPointStatus().setPresentTestPointId(getCurrentTestPointName());
			setCurrentTestAliasID(testcasedetails.getString("alias_id"));
			ApplicationLauncher.logger.info("execute_test : getPresentTestPointId: " + getPresentTestPointStatus().getPresentTestPointId());
			UpdateDevicesDataTableRowHighlight();
			deviceExecutionStatusDisplayUpdate(ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID, "N",ConstantApp.EXECUTION_STATUS_INPROGRESS);//EXECUTION_STATUS_COMPLETED
			getPresentTestPointStatus().setAllTestExecutionCompleted(false);
			if(ProcalFeatureEnable.SECONDARY_LDU_DISPLAY_ENABLED){
				updateLduSecondaryDisplayTestPoint();
			}
			ClearDataOnCurrentRow();
			SerialDataLDU.lscsLduResetReadingIndex();
			DisplayDataObj.resetLduErrorDataHashMap2d();
			SerialDataManager.setSkipCurrentTP_Execution(false);
			SerialDM_Obj.ClearSerialDataInAllPorts();

			setRepTestFlag(false);//Only Repeatability Test //Prasanth
			setSelfHeatingTestFlag(false);

			DeviceDataManagerController.resetInitMeterValues();//Const Test //Prasanth
			DeviceDataManagerController.resetFinalMeterValues();

			JSONObject model_parameters = getModelParameters(project_name);
			if(ProcalFeatureEnable.EXPORT_MODE_ENABLED){
				if(getCurrentTestPointName().contains(ConstantApp.EXPORT_MODE_ALIAS_NAME)){
					DeviceDataManagerController.setEnergyFlowMode(ConstantPowerSourceMte.EXPORT_MODE);
				}else{
					DeviceDataManagerController.setEnergyFlowMode(ConstantPowerSourceMte.IMPORT_MODE);
				}
				DisplayDataObj.PowerSourceEnergyFlowModeDataInit();
				ApplicationLauncher.logger.info("execute_test:Current Test Point: EnergyFlowMode:" + DeviceDataManagerController.getEnergyFlowMode());
				ApplicationLauncher.logger.info("execute_test:Current Test Point: EM_ModelType:" + DeviceDataManagerController.getDeployedEM_ModelType());

			}

			if(isMainCtMode()){
				DeviceDataManagerController.setExecutionMctNctMode(ConstantReport.RESULT_EXECUTION_MODE_MAIN_CT);
			}else if(isNeutralCtMode()){
				DeviceDataManagerController.setExecutionMctNctMode(ConstantReport.RESULT_EXECUTION_MODE_NEUTRAL_CT);
			}
			setLscsZeroCurrentPowerTurnOn(false);
			//ApplicationLauncher.logger.info("execute_test:test_type: " + test_type);
			ApplicationLauncher.logger.info("execute_test:Current Test Point: " + getCurrentTestPoint_Index()+": "+getCurrentTestPointName());
			ApplicationHomeController.updateBottomSecondaryStatus("",ConstantApp.LEFT_STATUS_INFO);

			if(!getUserAbortedFlag()){
				if((!getPowerSrcContinuousFailureStatus())&&(!getRefStdFeedBackContinuousFailureStatus())){
					try{
						
						String presentTestPointName = getCurrentTestPointName();
						if(presentTestPointName.contains(ConstantApp.HARMONIC_INPHASE_ALIAS_NAME)) {
							DeviceDataManagerController.setPresentTestPointContainsHarmonics(true);
						}else if(presentTestPointName.contains(ConstantApp.HARMONIC_OUTOFPHASE_ALIAS_NAME)) {
							DeviceDataManagerController.setPresentTestPointContainsHarmonics(true);
						}else {
							DeviceDataManagerController.setPresentTestPointContainsHarmonics(false);
						}
						
						
						if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_FEATURE_ENABLED){
							ApplicationLauncher.logger.info("execute_test: harmonic test1");
							if(isLastExecutedTestPointContainedHarmonics_Slave()){//pradeep  last executed test point 
								// send slave
								ApplicationLauncher.logger.info("execute_test: harmonic test2");
								//String presentTestPointName = getCurrentTestPointName();
								

								//if(!presentTestPointName.contains(ConstantApp.HARMONIC_INPHASE_ALIAS_NAME)){
								if(!DeviceDataManagerController.isPresentTestPointContainsHarmonics()){	
									ApplicationLauncher.logger.info("execute_test: harmonic test3");
									boolean slaveStatus = lscsSetDisableHarmonicsAtSlave(); 
									if(slaveStatus){
										ApplicationLauncher.logger.info("execute_test: harmonic test4");
										lscsSendHarmonicsDataTransmissionEndCmdToSlave(); 	
										setLastExecutedTestPointContainedHarmonics_Slave(false);
									}else{
										ApplicationLauncher.logger.info("execute_test: ***********************************************" );
										ApplicationLauncher.logger.info("execute_test: ***********************************************" );
										ApplicationLauncher.logger.info("execute_test: ***********************************************" );
										ApplicationLauncher.logger.info("execute_test: Slave harmonics reset communication failed: " );
										ApplicationLauncher.logger.info("execute_test: ***********************************************" );
										ApplicationLauncher.logger.info("execute_test: ***********************************************" );
										ApplicationLauncher.logger.info("execute_test: ***********************************************" );
									}

								}
							}else if(!getOneTimeExecuted()){
								ApplicationLauncher.logger.info("execute_test: harmonic test5");
								boolean slaveStatus = lscsSetDisableHarmonicsAtSlave(); 
								if(slaveStatus){
									ApplicationLauncher.logger.info("execute_test: harmonic test6");
									lscsSendHarmonicsDataTransmissionEndCmdToSlave(); 	
									setLastExecutedTestPointContainedHarmonics_Slave(false);
								}else{
									ApplicationLauncher.logger.info("execute_test: -----------------------------------------" );
									ApplicationLauncher.logger.info("execute_test: -----------------------------------------" );
									ApplicationLauncher.logger.info("execute_test: -----------------------------------------" );
									ApplicationLauncher.logger.info("execute_test: Slave harmonics reset communication failed2: " );
									ApplicationLauncher.logger.info("execute_test: -----------------------------------------" );
									ApplicationLauncher.logger.info("execute_test: -----------------------------------------" );
									ApplicationLauncher.logger.info("execute_test: -----------------------------------------" );
								}
							}
						}
						switch (test_type) {

						//case "STA":
						case ConstantApp.TEST_PROFILE_STA:
							JSONObject STA_parameters = getParameters(testcasedetails);
							DisplayDataObj.setSTAParameters(STA_parameters, model_parameters);
							STAExecuteStart();

							/*					JSONArray sta_testdetails = STA_parameters.getJSONArray("test_details");
						for(int i=0; i<sta_testdetails.length(); i++){
							String currenttestcasename = getCurrentTestPointName();
							JSONObject jobj = sta_testdetails.getJSONObject(i);
							String testname = jobj.getString("test_case_name");// + "-" + getCurrentTestAliasID();
							ApplicationLauncher.logger.info("execute_test: sta :currenttestcasename1: " + currenttestcasename);
							ApplicationLauncher.logger.info("execute_test: sta: testname: " + testname);
							if(testname.equals(currenttestcasename)){
								DisplayDataObj.setSTAParameters(STA_parameters, model_parameters);
								ApplicationLauncher.logger.info("execute_test: sta: currenttestcasename2: " + currenttestcasename+ " started");

								STAExecuteStart();
								break;

							}
						}	*/
							break;

							//case "Warmup":
						case ConstantApp.TEST_PROFILE_WARMUP:
							JSONObject warmup_parameters = getParameters(testcasedetails);
							JSONArray params = warmup_parameters.getJSONArray("test_details");
							if(params.length() != 0){
								JSONObject jobj = params.getJSONObject(0);
								DisplayDataObj.setWarmupParameters(jobj, model_parameters);
								WarmupExecuteStart();
							}
							break;

							//case "NoLoad":
						case ConstantApp.TEST_PROFILE_NOLOAD:
							JSONObject creep_parameters = getParameters(testcasedetails);
							DisplayDataObj.setCreepParameters(creep_parameters, model_parameters);
							CreepExecuteStart();
							/*					JSONArray cr_testdetails = creep_parameters.getJSONArray("test_details");
						for(int i=0; i<cr_testdetails.length(); i++){
							String currenttestcasename = getCurrentTestPointName();
							JSONObject jobj = cr_testdetails.getJSONObject(i);
							String testname = jobj.getString("test_case_name");// + "-" + getCurrentTestAliasID();
							ApplicationLauncher.logger.info("execute_test: creep :currenttestcasename1: " + currenttestcasename);
							ApplicationLauncher.logger.info("execute_test: creep: testname: " + testname);
							if(testname.equals(currenttestcasename)){
								DisplayDataObj.setCreepParameters(creep_parameters, model_parameters);
								ApplicationLauncher.logger.info("execute_test: creep: currenttestcasename2: " + currenttestcasename+ " started");
								CreepExecuteStart();
								break;

							}
						}	*/

							break;

							//case "Accuracy":
						case ConstantApp.TEST_PROFILE_DUT_COMMAND:
							ApplicationLauncher.logger.info("execute_test: dutCommand test: Entry : " );
							JSONObject dut_parameters = getDutTestParameters(testcasedetails);
							JSONArray dut_testdetails = dut_parameters.getJSONArray("test_details");

							for(int i=0; i<dut_testdetails.length(); i++){
								String currenttestcasename = getCurrentTestPointName();
								JSONObject jobj = dut_testdetails.getJSONObject(i);
								String testname = jobj.getString("test_case_name");
								ApplicationLauncher.logger.info("dutCommand: currenttestcasename: " + currenttestcasename);
								ApplicationLauncher.logger.info("testname: " + testname);
								if(testname.equals(currenttestcasename)){
									//DisplayDataObj.setDutCommandParameters(jobj, model_parameters);
									
									String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
							    	String testCaseName = "DimmerForward_";
							    	Optional<DutCommand> dutCommandDataOpt = DeviceDataManagerController.getDutCommandService().findFirstByProjectNameAndTestCaseNameStartingWith(projectName,testCaseName);
							    	
							    	if(dutCommandDataOpt.isPresent()) {
							    		DutCommand dutCommand = dutCommandDataOpt.get();
							    		//displayDataObj.setDutCommandParameters(jobj, modelParameters);
							    		DeviceDataManagerController.setDutCommandData(dutCommand);
							    		dutCommandExecuteStart();
										ApplicationLauncher.logger.info("dutCommand: currenttestcasename: " + currenttestcasename + " started");
										//dutCommandExecuteStart();
										
							    	}
									
									break;

								}/*else if( currenttestcasename.equals(ConstantApp.EXPORT_MODE_ALIAS_NAME +testname)){
									DisplayDataObj.setAccuracyParameters(jobj, model_parameters);
									ApplicationLauncher.logger.info("dutCommand: currenttestcasename: Export Mode " + currenttestcasename + " started");
									AccuracyExecuteStart();
									break;
								}*/
							}

							break;
							
						case ConstantApp.TEST_PROFILE_ACCURACY:

							JSONObject accuracy_parameters = getParameters(testcasedetails);
							JSONArray acc_testdetails = accuracy_parameters.getJSONArray("test_details");

							for(int i=0; i<acc_testdetails.length(); i++){
								String currenttestcasename = getCurrentTestPointName();
								JSONObject jobj = acc_testdetails.getJSONObject(i);
								String testname = jobj.getString("test_case_name");
								ApplicationLauncher.logger.info("Accuracy: currenttestcasename: " + currenttestcasename);
								ApplicationLauncher.logger.info("testname: " + testname);
								if(testname.equals(currenttestcasename)){
									DisplayDataObj.setAccuracyParameters(jobj, model_parameters);
									ApplicationLauncher.logger.info("Accuracy: currenttestcasename: " + currenttestcasename + " started");
									AccuracyExecuteStart();
									break;

								}else if( currenttestcasename.equals(ConstantApp.EXPORT_MODE_ALIAS_NAME +testname)){
									DisplayDataObj.setAccuracyParameters(jobj, model_parameters);
									ApplicationLauncher.logger.info("Accuracy: currenttestcasename: Export Mode " + currenttestcasename + " started");
									AccuracyExecuteStart();
									break;
								}
							}

							break;


							//case "InfluenceVolt":
						case ConstantApp.TEST_PROFILE_INFLUENCE_VOLT:
							ApplicationLauncher.logger.info("InfluenceVolt: entry");
							JSONObject voltage_parameters = getParameters(testcasedetails);
							JSONArray volt_testdetails = voltage_parameters.getJSONArray("test_details");
							for(int i=0; i<volt_testdetails.length(); i++){
								String currenttestcasename = getCurrentTestPointName();
								JSONObject jobj = volt_testdetails.getJSONObject(i);
								String testname = jobj.getString("test_case_name") ;//+ "-" + getCurrentTestAliasID();
								ApplicationLauncher.logger.info("execute_test:InfluenceVolt:currenttestcasename1: " + currenttestcasename);
								ApplicationLauncher.logger.info("execute_test: InfluenceVolt: testname: " + testname);
								if(testname.equals(currenttestcasename)){
									DisplayDataObj.setVoltageParameters(jobj, model_parameters);
									ApplicationLauncher.logger.info("InfluenceVolt: InfluenceVolt: currenttestcasename2: " + currenttestcasename+ " started");
									VoltageExecuteStart();
									break;

								}else if( currenttestcasename.equals(ConstantApp.EXPORT_MODE_ALIAS_NAME +testname)){
									DisplayDataObj.setVoltageParameters(jobj, model_parameters);
									ApplicationLauncher.logger.info("InfluenceVolt: InfluenceVolt: Export: currenttestcasename2: " + currenttestcasename+ " started");
									VoltageExecuteStart();
									break;
								}
							}	

							break;

							//case "InfluenceFreq":
						case ConstantApp.TEST_PROFILE_INFLUENCE_FREQ:
							JSONObject frequency_parameters = getParameters(testcasedetails);
							JSONArray f_testdetails = frequency_parameters.getJSONArray("test_details");
							for(int i=0; i<f_testdetails.length(); i++){
								String currenttestcasename = getCurrentTestPointName();
								JSONObject jobj = f_testdetails.getJSONObject(i);
								String testname = jobj.getString("test_case_name");// + "-" + getCurrentTestAliasID();
								ApplicationLauncher.logger.info("execute_test:InfluenceFreq:currenttestcasename1: " + currenttestcasename);
								ApplicationLauncher.logger.info("execute_test: InfluenceFreq: testname: " + testname);
								if(testname.equals(currenttestcasename)){
									DisplayDataObj.setFrequencyParameters(jobj, model_parameters);
									ApplicationLauncher.logger.info("InfluenceFreq: InfluenceFreq: currenttestcasename2: " + currenttestcasename+ " started");
									FrequencyExecuteStart();
									break;

								}else if( currenttestcasename.equals(ConstantApp.EXPORT_MODE_ALIAS_NAME +testname)){
									DisplayDataObj.setFrequencyParameters(jobj, model_parameters);
									ApplicationLauncher.logger.info("InfluenceFreq: InfluenceFreq: Export: currenttestcasename2: " + currenttestcasename+ " started");
									FrequencyExecuteStart();
									break;
								}
							}	

							break;

							//case "InfluenceHarmonic":
						case ConstantApp.TEST_PROFILE_INFLUENCE_HARMONIC:
							JSONObject harmonic_parameters = getParameters(testcasedetails);
							JSONArray har_testdetails = harmonic_parameters.getJSONArray("test_details");
							for(int i=0; i<har_testdetails.length(); i++){
								String currenttestcasename = getCurrentTestPointName();
								JSONObject jobj = har_testdetails.getJSONObject(i);
								String testname = jobj.getString("test_case_name");// + "-" + getCurrentTestAliasID();
								ApplicationLauncher.logger.info("currenttestcasename: " + currenttestcasename);
								ApplicationLauncher.logger.info("testname: " + testname);
								ApplicationLauncher.logger.info("InfluenceHarmonic: " + jobj.toString());

								if(testname.contains(ConstantApp.HARMONIC_INPHASE_ALIAS_NAME)){
									if(testname.equals(currenttestcasename)){
										ApplicationLauncher.logger.info("testname: " + testname + " Started");
										JSONArray harmonic_data = getHarmonicParameters(testcasedetails, ConstantApp.HARMONIC_INPHASE_ALIAS_NAME);
										DisplayDataObj.setHarmonicParameters(jobj, model_parameters, harmonic_data);
										ApplicationLauncher.logger.info("InfluenceHarmonic :HARMONIC_INPHASE: currenttestcasename: " + currenttestcasename+ " started");
										HarmonicExecuteStart();//To Be Tested
										break;

									}else if( currenttestcasename.equals(ConstantApp.EXPORT_MODE_ALIAS_NAME +testname)){
										ApplicationLauncher.logger.info("testname: Export: " + testname + " Started");
										JSONArray harmonic_data = getHarmonicParameters(testcasedetails, ConstantApp.HARMONIC_INPHASE_ALIAS_NAME);
										DisplayDataObj.setHarmonicParameters(jobj, model_parameters, harmonic_data);
										ApplicationLauncher.logger.info("InfluenceHarmonic : Export: HARMONIC_INPHASE: currenttestcasename: " + currenttestcasename+ " started");
										HarmonicExecuteStart();//To Be Tested
										break;
									}
								}
								else if(testname.contains(ConstantApp.HARMONIC_OUTOFPHASE_ALIAS_NAME)){
									if(testname.equals(currenttestcasename)){
										ApplicationLauncher.logger.info("testname: " + testname + " Started");
										JSONArray harmonic_data = getHarmonicParameters(testcasedetails, ConstantApp.HARMONIC_OUTOFPHASE_ALIAS_NAME);
										DisplayDataObj.setHarmonicParameters(jobj, model_parameters, harmonic_data);
										ApplicationLauncher.logger.info("InfluenceHarmonic :HARMONIC_OUTOFPHASE :  currenttestcasename: " + currenttestcasename+ " started");
										HarmonicExecuteStart();//To Be Tested
										break;

									}else if( currenttestcasename.equals(ConstantApp.EXPORT_MODE_ALIAS_NAME +testname)){
										ApplicationLauncher.logger.info("testname: Export " + testname + "Started");
										JSONArray harmonic_data = getHarmonicParameters(testcasedetails, ConstantApp.HARMONIC_OUTOFPHASE_ALIAS_NAME);
										DisplayDataObj.setHarmonicParameters(jobj, model_parameters, harmonic_data);
										ApplicationLauncher.logger.info("InfluenceHarmonic : Export: HARMONIC_OUTOFPHASE :  currenttestcasename: " + currenttestcasename+ " started");
										HarmonicExecuteStart();//To Be Tested
										break;
									}
								}
								else if(testname.contains(ConstantApp.HARMONIC_WITHOUT_ALIAS_NAME)){
									if(testname.equals(currenttestcasename)){
										ApplicationLauncher.logger.info("testname: " + testname + "Started");
										DisplayDataObj.setAccuracyParameters(jobj, model_parameters);
										ApplicationLauncher.logger.info("InfluenceHarmonic:HARMONIC_WOUT: currenttestcasename: " + currenttestcasename + " started");
										AccuracyExecuteStart();//To Be Tested
										break;

									}else if( currenttestcasename.equals(ConstantApp.EXPORT_MODE_ALIAS_NAME +testname)){
										ApplicationLauncher.logger.info("testname: Export: " + testname + "Started");
										DisplayDataObj.setAccuracyParameters(jobj, model_parameters);
										ApplicationLauncher.logger.info("InfluenceHarmonic: Export: HARMONIC_WOUT: currenttestcasename: " + currenttestcasename + " started");
										AccuracyExecuteStart();//To Be Tested
										break;
									}
								}
							}	

							break;

							//case "CuttingNuetral":
						case ConstantApp.TEST_PROFILE_CUT_NUETRAL:
							JSONObject cut_nuetral_parameters = getParameters(testcasedetails);
							JSONArray cutnuetral_testdetails = cut_nuetral_parameters.getJSONArray("test_details");
							for(int i=0; i<cutnuetral_testdetails.length(); i++){
								String currenttestcasename = getCurrentTestPointName();
								JSONObject jobj = cutnuetral_testdetails.getJSONObject(i);
								String testname = jobj.getString("test_case_name");// + "-" + getCurrentTestAliasID();
								if(testname.equals(currenttestcasename)){
									DisplayDataObj.setAccuracyParameters(jobj, model_parameters);
									ApplicationLauncher.logger.info("cutnuetral : currenttestcasename: " + currenttestcasename + " started");
									CutNuetralExecuteStart(); //To Be Tested
									break;

								}
							}		

							break;

							//case "VoltageUnbalance":
						case ConstantApp.TEST_PROFILE_VOLTAGE_UNBALANCE:
							JSONObject volt_unbalance_parameters = getParameters(testcasedetails);
							JSONArray volt_unb_testdetails = volt_unbalance_parameters.getJSONArray("test_details");
							for(int i=0; i<volt_unb_testdetails.length(); i++){
								String currenttestcasename = getCurrentTestPointName();
								JSONObject jobj = volt_unb_testdetails.getJSONObject(i);
								String testname = jobj.getString("test_case_name");// + "-" + getCurrentTestAliasID();
								if(testname.equals(currenttestcasename)){
									DisplayDataObj.setVoltUnbalanceParameters(jobj, model_parameters);
									ApplicationLauncher.logger.info("Volt unbalance : currenttestcasename: " + currenttestcasename + " started");
									VoltUnbalanceExecuteStart(); //To Be Tested
									break;

								}else if( currenttestcasename.equals(ConstantApp.EXPORT_MODE_ALIAS_NAME +testname)){
									DisplayDataObj.setVoltUnbalanceParameters(jobj, model_parameters);
									ApplicationLauncher.logger.info("Volt unbalance: currenttestcasename: Export: " + currenttestcasename + " started");
									VoltUnbalanceExecuteStart(); //To Be Tested
									break;
								}
							}	

							break;

							//case "PhaseReversal":
						case ConstantApp.TEST_PROFILE_PHASE_REVERSAL:
							JSONObject phaserev_parameters = getParameters(testcasedetails);
							JSONArray phase_testdetails = phaserev_parameters.getJSONArray("test_details");
							for(int i=0; i<phase_testdetails.length(); i++){
								String currenttestcasename = getCurrentTestPointName();
								JSONObject jobj = phase_testdetails.getJSONObject(i);
								String testname = jobj.getString("test_case_name") ;//+ "-" + getCurrentTestAliasID();
								//	if(testname.equals(currenttestcasename)){
								if( (testname.equals(currenttestcasename)) || ( currenttestcasename.equals(ConstantApp.EXPORT_MODE_ALIAS_NAME +testname)) ){
									DisplayDataObj.setAccuracyParameters(jobj, model_parameters);
									ApplicationLauncher.logger.info("PhaseReversal: currenttestcasename: " + currenttestcasename + " started");
									if(testname.contains(ConstantApp.PHASEREVERSAL_REV_ALIAS_NAME)){
										if(testname.equals(currenttestcasename)){
											ApplicationLauncher.logger.info("PhaseReversal: Rev started");
											PhaseReversalExecuteStart(); //To Be Tested
											break;

										}else if( currenttestcasename.equals(ConstantApp.EXPORT_MODE_ALIAS_NAME +testname)){
											ApplicationLauncher.logger.info("PhaseReversal: Rev started : Export1:");
											PhaseReversalExecuteStart(); //To Be Tested
											break;
										}
									}else if( currenttestcasename.equals(ConstantApp.EXPORT_MODE_ALIAS_NAME +testname)){
										ApplicationLauncher.logger.info("PhaseReversal: Norm started: Export2:");
										AccuracyExecuteStart();
										break;
									}else{
										ApplicationLauncher.logger.info("PhaseReversal: Norm started");
										AccuracyExecuteStart();
										break;
									}
								}
							}	

							break;


							//case "ConstantTest":
						case ConstantApp.TEST_PROFILE_CONSTANT_TEST:
							JSONObject const_parameters = getParameters(testcasedetails);
							JSONArray c_testdetails = const_parameters.getJSONArray("test_details");
							for(int i=0; i<c_testdetails.length(); i++){
								String currenttestcasename = getCurrentTestPointName();
								JSONObject jobj = c_testdetails.getJSONObject(i);
								String testname = jobj.getString("test_case_name");// + "-" + getCurrentTestAliasID();

								if(testname.equals(currenttestcasename)){
									DisplayDataObj.setConstTestParameters(jobj, model_parameters);
									ApplicationLauncher.logger.info("ConstantTest : currenttestcasename: " + currenttestcasename+ " started");
									if(ProcalFeatureEnable.POWERSOURCE_MANUAL_MODE){
										ConstTestManualSourceExecuteStart();
									}else{
										ConstTestExecuteStart();
									}
									break;

								}else if( currenttestcasename.equals(ConstantApp.EXPORT_MODE_ALIAS_NAME +testname)){
									DisplayDataObj.setConstTestParameters(jobj, model_parameters);
									ApplicationLauncher.logger.info("ConstantTest : currenttestcasename: Export: " + currenttestcasename+ " started");
									if(ProcalFeatureEnable.POWERSOURCE_MANUAL_MODE){
										ConstTestManualSourceExecuteStart();
									}else{
										ConstTestExecuteStart();
									}
									break;
								}
							}	

							break;

							//case "CustomTest":
						case ConstantApp.TEST_PROFILE_CUSTOM_TEST:

							JSONObject custom_rating_parameters = getParameters(testcasedetails);


							JSONArray customTestDetails = custom_rating_parameters.getJSONArray("test_details");

							for(int i=0; i<customTestDetails.length(); i++){
								String currenttestcasename = getCurrentTestPointName();
								JSONObject jobj = customTestDetails.getJSONObject(i);
								String testname = jobj.getString("test_case_name");
								ApplicationLauncher.logger.info("CustomTest: currenttestcasename: " + currenttestcasename);
								ApplicationLauncher.logger.info("testname: " + testname);
								if(testname.equals(currenttestcasename)){
									DisplayDataObj.setCustomRatingParameters(custom_rating_parameters, model_parameters);
									ApplicationLauncher.logger.info("CustomTest: currenttestcasename: " + currenttestcasename + " started");
									CustomRatingExecuteStart(); // To Be Tested
									break;

								}else if( currenttestcasename.equals(ConstantApp.EXPORT_MODE_ALIAS_NAME +testname)){
									DisplayDataObj.setCustomRatingParameters(custom_rating_parameters, model_parameters);
									ApplicationLauncher.logger.info("CustomTest: Export mode currenttestcasename: " + currenttestcasename + " started");
									CustomRatingExecuteStart(); // To Be Tested
									break; 

								}
							}

							break;

							//case "Repeatability":
						case ConstantApp.TEST_PROFILE_REPEATABILITY:
							JSONObject repeatability_parameters = getParameters(testcasedetails);
							JSONArray rep_testdetails = repeatability_parameters.getJSONArray("test_details");
							for(int i=0; i<rep_testdetails.length(); i++){
								String currenttestcasename = getCurrentTestPointName();
								JSONObject jobj = rep_testdetails.getJSONObject(i);
								String testname = jobj.getString("test_case_name");
								ApplicationLauncher.logger.info("Repeatability: currenttestcasename: " + currenttestcasename);
								ApplicationLauncher.logger.info("testname: " + testname);


								if(testname.contains(ConstantApp.REPEATABILITY_ALIAS_NAME)){
									if(testname.equals(currenttestcasename)){
										setRepTestFlag(true);
										DisplayDataObj.setRepeatabilityParameters(jobj, model_parameters);
										ApplicationLauncher.logger.info("Repeatability: currenttestcasename: " + currenttestcasename + " started");
										RepeatabilityExecuteStart();
										break; // added for issue, when time-based equal to 30 sec logic breaks

									}else if( currenttestcasename.equals(ConstantApp.EXPORT_MODE_ALIAS_NAME +testname)){
										setRepTestFlag(true);
										DisplayDataObj.setRepeatabilityParameters(jobj, model_parameters);
										ApplicationLauncher.logger.info("Repeatability: currenttestcasename: Export: " + currenttestcasename + " started");
										RepeatabilityExecuteStart();
										break;
									}
								}
							}

							break;

							//case "SelfHeating":
						case ConstantApp.TEST_PROFILE_SELF_HEATING:
							JSONObject selfheating_parameters = getParameters(testcasedetails);
							JSONArray self_testdetails = selfheating_parameters.getJSONArray("test_details");
							for(int i=0; i<self_testdetails.length(); i++){
								String currenttestcasename = getCurrentTestPointName();
								JSONObject jobj = self_testdetails.getJSONObject(i);
								String testname = jobj.getString("test_case_name");
								ApplicationLauncher.logger.info("SelfHeating: currenttestcasename: " + currenttestcasename);
								ApplicationLauncher.logger.info("testname: " + testname);

								if(testname.contains(ConstantApp.SELF_HEATING_START_TEST_ALIAS_NAME)){
									if(testname.equals(currenttestcasename)){
										setSelfHeatingTestFlag(true);
										ApplicationLauncher.logger.info("Self heat warmup execution");
										DisplayDataObj.setSelfHeatingWarmupParameters(jobj, model_parameters);
										WarmupExecuteStart();
										break;// added for issue, when time-based equal to 30 sec logic breaks
									}else if( currenttestcasename.equals(ConstantApp.EXPORT_MODE_ALIAS_NAME +testname)){
										setSelfHeatingTestFlag(true);
										ApplicationLauncher.logger.info("Self heat warmup execution : Export");
										DisplayDataObj.setSelfHeatingWarmupParameters(jobj, model_parameters);
										WarmupExecuteStart();
										break;
									}
								}

								else{
									if(testname.equals(currenttestcasename)){
										setSelfHeatingTestFlag(true);
										ApplicationLauncher.logger.info("Self Heating Test Execution");
										DisplayDataObj.setRepeatabilityParameters(jobj, model_parameters);
										ApplicationLauncher.logger.info("SelfHeating: currenttestcasename: " + currenttestcasename + " started");
										RepeatabilityExecuteStart();
										break;// added for issue, when time-based equal to 30 sec logic breaks
										//TestPointMatched = true;
									}else if( currenttestcasename.equals(ConstantApp.EXPORT_MODE_ALIAS_NAME +testname)){
										setSelfHeatingTestFlag(true);
										ApplicationLauncher.logger.info("Self Heating Test Execution");
										DisplayDataObj.setRepeatabilityParameters(jobj, model_parameters);
										ApplicationLauncher.logger.info("SelfHeating: currenttestcasename: Export: " + currenttestcasename + " started");
										RepeatabilityExecuteStart();
										break;
									}
								}
							}

							break;


						default:
							break;
						}
					}catch (Exception E) {
						ApplicationLauncher.logger.debug("execute_test: exception: "+ E.getMessage());
						String failureMessage = ErrorCodeMapping.ERROR_CODE_105 ;
						updateSkippedInDisplay2(failureMessage);

						ApplicationLauncher.logger.debug("execute_test: test Point: " + getCurrentTestPointName());
						ApplicationLauncher.logger.info("execute_test: failureMessage: " + failureMessage + " : " + ErrorCodeMapping.ERROR_CODE_105_MSG);
						setCurrentTestPoint_ExecutionCompletedStatus(true);
						semLockExecutionInprogress = false;
						ApplicationLauncher.logger.info("execute_test: semLockExecutionInprogress: released1");
						IncrementCurrentTestPoint_Index();
					}

				}else{
					updateNotExecutedInDisplay();

					ApplicationLauncher.logger.debug("execute_test: test Point2: " + getCurrentTestPointName());

					setCurrentTestPoint_ExecutionCompletedStatus(true);
					semLockExecutionInprogress = false;
					ApplicationLauncher.logger.info("execute_test: semLockExecutionInprogress: released2");
					IncrementCurrentTestPoint_Index();
					return;
				}
			}else{

				ApplicationLauncher.logger.info("execute_test: User aborted ");
				semLockExecutionInprogress = false;
				ApplicationLauncher.logger.info("execute_test: semLockExecutionInprogress: released3");
				return;
			}

		}

		public JSONObject getParameters(JSONObject testcasedetails) throws JSONException{
			String project_name = getCurrentProjectName();//MeterParamsController.getCurrentProjectName();
			String test_type = testcasedetails.getString("test_type");
			String alias_id = testcasedetails.getString("alias_id");
			ApplicationLauncher.logger.info("getParameters: test_type: " + test_type);
			JSONObject test_parameter = MySQL_Controller.sp_getproject_components(project_name, test_type, alias_id);
			//ApplicationLauncher.logger.info("getParameters: test_parameter: " + test_parameter);

			return test_parameter;
		}
		
		public JSONObject getDutTestParameters(JSONObject testcasedetails) throws JSONException{
			String project_name = getCurrentProjectName();//MeterParamsController.getCurrentProjectName();
			String test_type = testcasedetails.getString("test_type");
			String alias_id = testcasedetails.getString("alias_id");
			ApplicationLauncher.logger.info("getDutTestParameters: test_type: " + test_type);
			JSONObject test_parameter = MySQL_Controller.sp_get_dut_commands(project_name, test_type, alias_id);
			ApplicationLauncher.logger.info("getDutTestParameters: test_parameter: " + test_parameter);

			return test_parameter;
		}

		public JSONObject getModelParameters(String project_name){
			int model_id = MySQL_Controller.sp_getProjectModel_ID(project_name);
			JSONObject model_data = MySQL_Controller.sp_getem_model_data(model_id);

			return model_data;
		}

		public JSONArray getHarmonicParameters(JSONObject testcasedetails, String r_harmonics) throws JSONException{
			ApplicationLauncher.logger.info("getHarmonicParameters : Entry");
			String project_name = getCurrentProjectName();//MeterParamsController.getCurrentProjectName();
			String test_type = testcasedetails.getString("test_type");
			String alias_id = testcasedetails.getString("alias_id");
			JSONObject harmonic_data = MySQL_Controller.sp_getharmonic_data(project_name, test_type, alias_id);

			JSONArray harmonics = harmonic_data.getJSONArray("Harmonic_data");
			JSONArray req_harmonics = new JSONArray();
			JSONObject jobj = new JSONObject();
			for(int i =0; i < harmonics.length(); i++){
				jobj = harmonics.getJSONObject(i);
				String testname = jobj.getString("test_case_name");
				if(testname.equals(r_harmonics)){
					req_harmonics.put(jobj);
				}
			}
			return req_harmonics;
		}




		public static String getFeedbackR_phaseVolt(){
			return FeedbackR_phaseVolt;
		}

		public static void setFeedbackR_phaseVolt(String value){
			FeedbackR_phaseVolt = value;
		}

		public static String getFeedbackR_phaseCurrent(){
			return FeedbackR_phaseCurrent;
		}

		public static boolean isLscsZeroCurrentPowerTurnOn() {
			return lscsZeroCurrentPowerTurnOn;
		}

		public static void setLscsZeroCurrentPowerTurnOn(boolean creepTestUnderExecution) {
			ProjectExecutionController.lscsZeroCurrentPowerTurnOn = creepTestUnderExecution;
		}

		public static void setFeedbackR_phaseCurrent(String value){
			FeedbackR_phaseCurrent = value;
		}

		public static String getFeedbackR_phaseDegree(){
			return FeedbackR_phaseDegree;
		}

		public static void setFeedbackR_phaseDegree(String value){
			if(ProcalFeatureEnable.MTE_REFSTD_CONNECTED){
				try {
					float phaseValue  =Float.parseFloat(value);
					phaseValue = - phaseValue;
					value = String.format("%#.02f", phaseValue);
				}catch (Exception E) {
					ApplicationLauncher.logger.debug("setFeedbackR_phaseDegree:"+ E.getMessage());
				}
			}
			FeedbackR_phaseDegree = value;
		}

		public static String getFeedbackY_phaseVolt(){
			return FeedbackY_phaseVolt;
		}

		public static void setFeedbackY_phaseVolt(String value){
			FeedbackY_phaseVolt = value;
		}

		public static String getFeedbackY_phaseCurrent(){
			return FeedbackY_phaseCurrent;
		}

		public static void setFeedbackY_phaseCurrent(String value){

			FeedbackY_phaseCurrent = value;
		}

		public static String getFeedbackY_phaseDegree(){
			return FeedbackY_phaseDegree;
		}

		public static void setFeedbackY_phaseDegree(String value){
			if(ProcalFeatureEnable.MTE_REFSTD_CONNECTED){
				try {
					float phaseValue  =Float.parseFloat(value);
					phaseValue = - phaseValue;
					value = String.format("%#.02f", phaseValue);
				}catch (Exception E) {
					ApplicationLauncher.logger.debug("setFeedbackY_phaseDegree:"+ E.getMessage());
				}
			}
			FeedbackY_phaseDegree = value;
		}

		public static String getFeedbackB_phaseVolt(){
			return FeedbackB_phaseVolt;
		}

		public static void setFeedbackB_phaseVolt(String value){
			FeedbackB_phaseVolt = value;
		}

		public static String getFeedbackB_phaseCurrent(){
			return FeedbackB_phaseCurrent;
		}

		public static void setFeedbackB_phaseCurrent(String value){
			FeedbackB_phaseCurrent = value;
		}

		public static String getFeedbackB_phaseDegree(){
			return FeedbackB_phaseDegree;
		}

		public static void setFeedbackB_phaseDegree(String value){
			if(ProcalFeatureEnable.MTE_REFSTD_CONNECTED){
				try {
					float phaseValue  =Float.parseFloat(value);
					phaseValue = - phaseValue;
					value = String.format("%#.02f", phaseValue);
				}catch (Exception E) {
					ApplicationLauncher.logger.debug("setFeedbackB_phaseDegree:"+ E.getMessage());
				}
			}
			FeedbackB_phaseDegree = value;
		}

		public static String getFeedbackR_Frequency(){
			return FeedbackR_Frequency;
		}

		public static void setFeedbackR_Frequency(String value){
			FeedbackR_Frequency = value;
		}

		public static String getFeedbackY_Frequency(){
			return FeedbackY_Frequency;
		}

		public static void setFeedbackY_Frequency(String value){
			FeedbackY_Frequency = value;
		}

		public static String getFeedbackB_Frequency(){
			return FeedbackB_Frequency;
		}

		public static void setFeedbackB_Frequency(String value){
			FeedbackB_Frequency = value;
		}




		public static ArrayList<Boolean> getDeviceMountedList() {
			ApplicationLauncher.logger.info("getDeviceMountedList : Entry");
			/* stub function to load the devicemountedlist*/
			ArrayList<Boolean> IsDeviceMounted = new ArrayList<Boolean>();

			IsDeviceMounted.add(ref_ckBxAddress1.isSelected());
			IsDeviceMounted.add(ref_ckBxAddress2.isSelected());

			return IsDeviceMounted;
		}


		public static void UpdateDB_DeviceLDU_ErrorData(int LDU_ReadAddress,String Resultstatus,String ErrorValue,String DataType){
			ApplicationLauncher.logger.info("UpdateDB_DeviceLDU_ErrorData : Entry: "+SerialDM_Obj.getSkipCurrentTP_Execution());
			if(!SerialDM_Obj.getSkipCurrentTP_Execution()){

				ApplicationLauncher.logger.info("UpdateDB_DeviceLDU_ErrorData : Entry2");

				if(ProcalFeatureEnable.CCUBE_LDU_CONNECTED) {
					if(ErrorValue.equals(ConstantLduCcube.CMD_LDU_ERROR_DATA_ER_WFR) || ErrorValue.equals(ConstantLduCcube.CMD_LDU_CREEP_DATA_ER_WFR)){
						ErrorValue = "WFR";
					}
				}else if(ProcalFeatureEnable.LSCS_LDU_CONNECTED){
					if(ErrorValue.contains(ConstantLduLscs.CMD_LDU_ERROR_DATA_ER_WFR) || ErrorValue.equals(ConstantLduCcube.CMD_LDU_CREEP_DATA_ER_WFR)){
						ErrorValue = "WFR"; 
					}
				}



				String FailureReason = "";

				String rack_id = Integer.toString(LDU_ReadAddress);
				
				ApplicationHomeController.update_left_status("Updating DB LDU ErrorData",ConstantApp.LEFT_STATUS_DEBUG);
				ApplicationLauncher.logger.info("UpdateDB_DeviceLDU_ErrorData: LDU_ReadAddress: "  + LDU_ReadAddress);
				int seqNumber = getCurrentTestPoint_Index()+1;
				if(ProcalFeatureEnable.PROCON_INTERFACE_ENABLED) {
					
					String dutSerialNo = DeviceDataManagerController.getConveyorDutSerialNumberMap(LDU_ReadAddress);
					MySQL_Controller.sp_add_resultWithProjectRunId(getCurrentProjectName(), 
							getCurrentTestPointName(), getCurrentTestAliasID(), 
							rack_id, Resultstatus,DeviceDataManagerController.getError_countAndIncrement(), ErrorValue,
							FailureReason,DataType,DeviceDataManagerController.getExecutionMctNctMode(),DeviceDataManagerController.getEnergyFlowMode(),
							getSelectedDeployment_ID(),seqNumber,getPresentProjectRunId(),
							DeviceDataManagerController.get_Error_min(),DeviceDataManagerController.get_Error_max(),dutSerialNo);
					
					
					
				}else {
				
					MySQL_Controller.sp_add_result(getCurrentProjectName(), 
							getCurrentTestPointName(), getCurrentTestAliasID(), 
							rack_id, Resultstatus,DeviceDataManagerController.getError_countAndIncrement(), ErrorValue,
							FailureReason,DataType,DeviceDataManagerController.getExecutionMctNctMode(),DeviceDataManagerController.getEnergyFlowMode(),
							getSelectedDeployment_ID(),seqNumber);
				}
				ApplicationHomeController.update_left_status("Finished Updating DB DeviceLDU ErrorData",ConstantApp.LEFT_STATUS_DEBUG);


				FailureReason = null;//garbagecollector
				rack_id= null;//garbagecollector
			}
		}
		
		public static void updateDB_DeviceExecutionStatus(int LDU_ReadAddress,String Resultstatus,String ErrorValue,String DataType){
			ApplicationLauncher.logger.info("updateDB_DeviceExecutionStatus : Entry: "+SerialDM_Obj.getSkipCurrentTP_Execution());
			//if(!SerialDM_Obj.getSkipCurrentTP_Execution()){

				ApplicationLauncher.logger.info("updateDB_DeviceExecutionStatus : Entry2");

/*				if(ProcalFeatureEnable.CCUBE_LDU_CONNECTED) {
					if(ErrorValue.equals(ConstantLduCcube.CMD_LDU_ERROR_DATA_ER_WFR) || ErrorValue.equals(ConstantLduCcube.CMD_LDU_CREEP_DATA_ER_WFR)){
						ErrorValue = "WFR";
					}
				}else if(ProcalFeatureEnable.LSCS_LDU_CONNECTED){
					if(ErrorValue.contains(ConstantLduLscs.CMD_LDU_ERROR_DATA_ER_WFR) || ErrorValue.equals(ConstantLduCcube.CMD_LDU_CREEP_DATA_ER_WFR)){
						ErrorValue = "WFR"; 
					}
				}*/



				String FailureReason = "";

				String rack_id = Integer.toString(LDU_ReadAddress);
				ApplicationHomeController.update_left_status("Updating Execution status",ConstantApp.LEFT_STATUS_DEBUG);
				//ApplicationLauncher.logger.info("UpdateDB_DeviceLDU_ErrorData: LDU_ReadAddress: "  + LDU_ReadAddress);
				int seqNumber = getCurrentTestPoint_Index()+1;
				
				if(ProcalFeatureEnable.PROCON_INTERFACE_ENABLED) {
					String dutSerialNo = DeviceDataManagerController.getConveyorDutSerialNumberMap(LDU_ReadAddress);
					MySQL_Controller.sp_add_resultWithProjectRunId(getCurrentProjectName(), 
							getCurrentTestPointName(), getCurrentTestAliasID(), 
							rack_id, Resultstatus,DeviceDataManagerController.getError_countAndIncrement(), ErrorValue,
							FailureReason,DataType,DeviceDataManagerController.getExecutionMctNctMode(),DeviceDataManagerController.getEnergyFlowMode(),
							getSelectedDeployment_ID(),seqNumber,getPresentProjectRunId(),
							DeviceDataManagerController.get_Error_min(),DeviceDataManagerController.get_Error_max(),dutSerialNo
							);
					
				}else {
					MySQL_Controller.sp_add_result(getCurrentProjectName(), 
						getCurrentTestPointName(), getCurrentTestAliasID(), 
						rack_id, Resultstatus,DeviceDataManagerController.getError_countAndIncrement(), ErrorValue,
						FailureReason,DataType,DeviceDataManagerController.getExecutionMctNctMode(),DeviceDataManagerController.getEnergyFlowMode(),
						getSelectedDeployment_ID(),seqNumber);
				}
				ApplicationHomeController.update_left_status("Finished updating Execution status",ConstantApp.LEFT_STATUS_DEBUG);


				FailureReason = null;//garbagecollector
				rack_id= null;//garbagecollector
			//}
		}


		public static void DeviceErrorDisplayUpdate(int LDU_ReadAddress,String Resultstatus,String ErrorValue){
			if(!SerialDM_Obj.getSkipCurrentTP_Execution()){

				ApplicationLauncher.logger.info("DeviceErrorDisplayUpdate : Entry");
				if(ProcalFeatureEnable.CCUBE_LDU_CONNECTED) {
					if(ErrorValue.equals(ConstantLduCcube.CMD_LDU_ERROR_DATA_ER_WFR) || ErrorValue.equals(ConstantLduCcube.CMD_LDU_CREEP_DATA_ER_WFR)){
						ErrorValue = "WFR";
					}
				}else if(ProcalFeatureEnable.LSCS_LDU_CONNECTED){
					if(ErrorValue.contains(ConstantLduLscs.CMD_LDU_ERROR_DATA_ER_WFR) || ErrorValue.equals(ConstantLduCcube.CMD_LDU_CREEP_DATA_ER_WFR)){
						ErrorValue = "WFR";
					}

				}

				ApplicationLauncher.logger.info("DeviceErrorDisplayUpdate:LDU_ReadAddress: "  + LDU_ReadAddress);
				LiveTableDataManager.UpdateliveTableData(LDU_ReadAddress, Resultstatus,ErrorValue);
				//if(getPresentTestPointStatus().getPresentTpResult().size()>0){
					boolean remoteResultAlreadyExist = 	getPresentTestPointStatus().getPresentTpResult().stream().anyMatch(e-> e.getPositionId()==LDU_ReadAddress);//.anyMatch(e-> e.getPositionId()==2)
																	
					if(remoteResultAlreadyExist){
						String errorValueF = ErrorValue;
						getPresentTestPointStatus().getPresentTpResult().stream().filter(e-> e.getPositionId()==LDU_ReadAddress)
						.forEach(e->{
							e.setResultStatus(Resultstatus);
							e.setResultValue(errorValueF);
						});
					}else{
						
						
						TestResult testResult = new TestResult();
						testResult.setPositionId(LDU_ReadAddress);
						testResult.setResultStatus(Resultstatus);
						testResult.setResultValue(ErrorValue);	
						getPresentTestPointStatus().getPresentTpResult().add(testResult);
					}
				//}
				
				if(ProcalFeatureEnable.SECONDARY_LDU_DISPLAY_ENABLED){
					SecondaryLduDisplayController.updateLduSecondaryDisplay(LDU_ReadAddress, Resultstatus,ErrorValue);
				}

			}
		}
		
		
		public static void deviceExecutionStatusDisplayUpdate(int LDU_ReadAddress,String Resultstatus,String ErrorValue){
			if(!SerialDM_Obj.getSkipCurrentTP_Execution()){

				ApplicationLauncher.logger.debug("deviceExecutionStatusDisplayUpdate : Entry");
/*				if(ProcalFeatureEnable.CCUBE_LDU_CONNECTED) {
					if(ErrorValue.equals(ConstantLduCcube.CMD_LDU_ERROR_DATA_ER_WFR) || ErrorValue.equals(ConstantLduCcube.CMD_LDU_CREEP_DATA_ER_WFR)){
						ErrorValue = "WFR";
					}
				}else if(ProcalFeatureEnable.LSCS_LDU_CONNECTED){
					if(ErrorValue.contains(ConstantLduLscs.CMD_LDU_ERROR_DATA_ER_WFR) || ErrorValue.equals(ConstantLduCcube.CMD_LDU_CREEP_DATA_ER_WFR)){
						ErrorValue = "WFR";
					}

				}*/

				//ApplicationLauncher.logger.info("DeviceErrorDisplayUpdate:LDU_ReadAddress: "  + LDU_ReadAddress);
				
				if ( (LDU_ReadAddress == ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID) && (ErrorValue.equals(ConstantApp.EXECUTION_STATUS_COMPLETED))  ) {
					getRemoteTestPointStatusMap().put(getCurrentTestPointName(), ConstantApp.EXECUTION_STATUS_COMPLETED);
				}
				
				boolean allTestPointCompleted = false;
				
				allTestPointCompleted = getRemoteTestPointStatusMap().values().stream().allMatch(value -> ConstantApp.EXECUTION_STATUS_COMPLETED.equals(value));
				
				ApplicationLauncher.logger.debug("deviceExecutionStatusDisplayUpdate : allTestPointCompleted : " + allTestPointCompleted);
				
				if (allTestPointCompleted) {
					//getPresentTestPointStatus().setAllTestExecutionCompleted(true); 
					getPresentTestPointStatus().setAllTestExecutionCompletedCheck(true); 
				}
				
				ApplicationLauncher.logger.debug("deviceExecutionStatusDisplayUpdate : TestPointStatus : " + getPresentTestPointStatus().isAllTestExecutionCompletedCheck());
				
				//ErrorValue == EXECUTION_STATUS_COMPLETED
						//getCurrentTestPointName()
						
						///update
						//iterated all completed
						//getPresentTestPointStatus().setTestExecutionStatus(ConstantApp.EXECUTION_STATUS_COMPLETED);
				LiveTableDataManager.UpdateliveTableData(LDU_ReadAddress, Resultstatus,ErrorValue);
				boolean remoteResultAlreadyExist = 	getPresentTestPointStatus().getPresentTpResult().stream().anyMatch(e-> e.getPositionId()==LDU_ReadAddress);//.anyMatch(e-> e.getPositionId()==2)
				
				if(remoteResultAlreadyExist){
					String errorValueF = ErrorValue;
					getPresentTestPointStatus().getPresentTpResult().stream().filter(e-> e.getPositionId()==LDU_ReadAddress)
					.forEach(e->{
						e.setResultStatus(Resultstatus);
						e.setResultValue(errorValueF);
					});
				}else{
					
					
					TestResult testResult = new TestResult();
					testResult.setPositionId(LDU_ReadAddress);
					testResult.setResultStatus(Resultstatus);
					testResult.setResultValue(ErrorValue);	
					getPresentTestPointStatus().getPresentTpResult().add(testResult);
				}
				
				
				//if(ProcalFeatureEnable.SECONDARY_LDU_DISPLAY_ENABLED){
				//	SecondaryLduDisplayController.updateLduSecondaryDisplay(LDU_ReadAddress, Resultstatus,ErrorValue);
				//}

			}
		}





		public JSONObject FetchDevicesFromDB(){
			ApplicationLauncher.logger.info("FetchDevicesFromDB : Entry");
			JSONObject IsDeviceMounted1 = new JSONObject();
			ArrayList<Integer> SelectedRacks = new ArrayList<Integer>();


			try {
				getCurrentProjectName();
				JSONObject devices_json = DisplayDataObj.getDeployedDevicesJson();//MySQL_Controller.sp_getdeploy_devices(project_name);
				JSONArray devices  = devices_json.getJSONArray("Devices");
				ApplicationLauncher.logger.debug("FetchDevicesFromDB : devices:" + devices);
				JSONObject device = new JSONObject();
				for(int i=0; i<devices.length(); i++){
					device = devices.getJSONObject(i);
					SelectedRacks.add(device.getInt("Rack_ID"));
				}
				ApplicationLauncher.logger.info("SelectedRacks:" + SelectedRacks);
				int no_of_racks = DeploymentManagerController.get_no_of_racks();
				ApplicationLauncher.logger.info("no_of_racks:" + no_of_racks);
				int rackstartPosition = 1;
				if(ProcalFeatureEnable.RACK_HYBRID_MODE_ENABLED){
					rackstartPosition = ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_START_POSITION;
					no_of_racks = rackstartPosition +  no_of_racks - 1 ;
					//if((ProCalCustomerConfiguration.ELECTROBYTE_HYBRID_2NO_3PHASE_10NO_1PHASE_POSITION_2022) 
					//		&& (getPresentMeterType().startsWith(ConstantApp.METERTYPE_THREEPHASE))){
					if (getPresentMeterType().startsWith(ConstantApp.METERTYPE_THREEPHASE)){
						rackstartPosition = ProcalFeatureEnable.HYBRID_TOTAL_3PHASE_SUPPORTED_RACK_START_POSITION;
						no_of_racks = rackstartPosition +  no_of_racks - 1 ;
						ApplicationLauncher.logger.info("FetchDevicesFromDB : 3phase: rackstartPosition: " + rackstartPosition);
						ApplicationLauncher.logger.info("FetchDevicesFromDB : 3phase: no_of_racks: " + no_of_racks);
						//no_of_racks = noOfHybridSupportedRacks;sdgdsf

						//DeploymentManagerController.set_no_of_racks(electroByte3PhaseNoOfSupportedRacks);
					}else if (getPresentMeterType().startsWith(ConstantApp.METERTYPE_SINGLEPHASE)){
						//noOfSupportedRacks = ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_POSITIONS;
						//rackstartPosition = ProcalFeatureEnable.HYBRID_TOTAL_1PHASE_SUPPORTED_RACK_START_POSITION;
						ApplicationLauncher.logger.info("FetchDevicesFromDB : 1phase: rackstartPosition: " + rackstartPosition);
						ApplicationLauncher.logger.info("FetchDevicesFromDB : 1phase: no_of_racks: " + no_of_racks);
					}
				}

				String rack_id = "";
				//for(int i=1; i<= no_of_racks ; i++){
				//ApplicationLauncher.logger.debug("SelectedRacks: SelectedRacks.size() = " + SelectedRacks.size());
				for(int i=rackstartPosition; i<= no_of_racks ; i++){
					//ApplicationLauncher.logger.debug("SelectedRacks: rackstartPosition: i = " + i);

					for(int j=0; j<SelectedRacks.size(); j++){
						//ApplicationLauncher.logger.debug("SelectedRacks: rackstartPosition: j = " + j);
						//ApplicationLauncher.logger.debug("SelectedRacks: SelectedRacks.get(j) = " + SelectedRacks.get(j));
						if(SelectedRacks.get(j) != i){
							rack_id = String.format("%02d", i);
							IsDeviceMounted1.put(rack_id, false);
							//getDeviceMountedMap().put(rack_id, true);
							ApplicationLauncher.logger.debug("SelectedRacks: not appending Data : rack_id : " + rack_id);
							continue;
						}
						//ApplicationLauncher.logger.debug("SelectedRacks: appending - Good ");
						rack_id = String.format("%02d", i);
						IsDeviceMounted1.put(rack_id, true);
						getDeviceMountedMap().put(rack_id, true);
						break;
					}
				}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("FetchDevicesFromDB :JSONException :"+ e.getMessage());
			}
			ApplicationLauncher.logger.debug("SelectedRacks: IsDeviceMounted1: " + IsDeviceMounted1.toString());
			return IsDeviceMounted1;
		}

		public void setListOfDevices(){
			getDeviceMountedMap().clear();
			JSONObject IsDeviceMounted1 = FetchDevicesFromDB();
			ApplicationLauncher.logger.debug("setListOfDevices: getDeviceMountedMap: " + getDeviceMountedMap());
			DeviceMounted = IsDeviceMounted1;
		}
		
		
		public void updateDeviceMountedMap(){
			//getDeviceMountedMap().clear();
			
		}

		public static JSONObject getListOfDevices(){
			return DeviceMounted;
		}

		public void setCurrentTestPointName(String test_case_name){
			CurrentTestPointName = test_case_name;
		}

		public static String getCurrentTestPointName(){
			return CurrentTestPointName;
		}

		public void setCurrentTestAliasID(String alias_id){
			CurrentTestAliasID = alias_id;
		}

		public static String getCurrentTestAliasID(){
			return CurrentTestAliasID;
		}



		public static void IncrementCurrentTestPoint_Index(){
			CurrentTestPoint_Index++;

		}

		public static  Integer getCurrentTestPoint_Index(){
			return CurrentTestPoint_Index;
		}

		public void setCurrentTestPoint_Index(Integer Index){
			CurrentTestPoint_Index = Index;
		}



		public static  Integer getCurrentProject_TotalNoOfTestPoint(){
			return CurrentProject_TotalNoOfTestPoint;
		}

		public void setCurrentProject_TotalNoOfTestPoint(Integer value){
			CurrentProject_TotalNoOfTestPoint = value;
		}

		public static void setCurrentTestPoint_ExecutionCompletedStatus(boolean status){
			CurrentTestPoint_ExecutionCompleted = status;
		}

		public static boolean getCurrentTestPoint_ExecutionCompletedStatus(){
			return CurrentTestPoint_ExecutionCompleted;
		}



		public static void setAll_TestPoint_CompletedStatus(boolean status){
			All_TestPoint_Completed = status;
		}

		public static boolean getAll_TestPoint_CompletedStatus(){
			return All_TestPoint_Completed;
		}


		public static  JSONArray getCurrentProjectTestPointList(){
			return CurrentProjectTestPoint_List;
		}


		public static void setCurrentProjectTestPointList(JSONArray value){
			CurrentProjectTestPoint_List = value;
		}

		public static  long getProjectStartTime(){
			return ProjectStartTime;
		}

		public static void setProjectStartTime(long value){
			ProjectStartTime = value;
		}

		public static  boolean getUI_TableRefreshFlag(){
			return UI_TableRefreshFlag;
		}

		public static void setUI_TableRefreshFlag(boolean value){
			ApplicationLauncher.logger.info("setUIRefreshFlag : " + value);
			UI_TableRefreshFlag = value;
		}

		public static  boolean getSelfHeatingTestFlag(){
			return SelfHeatingTestWMPFlag;
		}

		public static void setSelfHeatingTestFlag(boolean value){
			SelfHeatingTestWMPFlag = value;
		}


		public static  boolean getRepTestFlag(){
			return RepTestFlag;
		}

		public static void setRepTestFlag(boolean value){
			RepTestFlag = value;
		}






		public static void getInitValueFromUI(){
			ApplicationLauncher.logger.info("getInitValueFromUI : Entry");
			if(!Is_DialogBox_Active){
				Is_DialogBox_Active = true;
				TextInputDialog dialog = new TextInputDialog("");
				dialog.setTitle("Enter Meter Inital Value");
				if(DeviceDataManagerController.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)){
					dialog.setTitle("Enter Meter kWh Inital Value");
				}else if(DeviceDataManagerController.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)){
					dialog.setTitle("Enter Meter kVARh Inital Value");
				}
				dialog.setHeaderText("Enter some text, or use default value.");
				ApplicationLauncher.logger.info("Waiting");
				Optional<String> result = dialog.showAndWait();
				ApplicationLauncher.logger.info("Wait over");
				String value = "0";
				if (result.isPresent()) {
					value = result.get();
				}
				addInitMeterValue(value);
				Is_DialogBox_Active =false;
			}

		}

		public static void addInitMeterValue(String value){//Gopi why this function?
			ApplicationLauncher.logger.info("addInitMeterValue : Entry");
			/*ArrayList<String> InitMeterValues = DisplayDeviceDataController.getInitMeterValues();
		ApplicationLauncher.logger.info("InitMeterValues" + InitMeterValues);
		InitMeterValues.add(value);
		DisplayDeviceDataController.setInitMeterValues(InitMeterValues);
		ApplicationLauncher.logger.info("InitMeterValues" + InitMeterValues);*/
		}

		public static void getFinalValueFromUI(){
			ApplicationLauncher.logger.info("getFinalValueFromUI : Entry");
			if(!Is_DialogBox_Active){
				Is_DialogBox_Active = true;
				TextInputDialog dialog = new TextInputDialog("");
				dialog.setTitle("Enter Meter Final Value");
				if(DeviceDataManagerController.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_ACTIVE)){
					dialog.setTitle("Enter Meter kWh Final Value");
				}else if(DeviceDataManagerController.getDeployedEM_ModelType().contains(ConstantApp.METERTYPE_REACTIVE)){
					dialog.setTitle("Enter Meter kVARh Final Value");
				}
				dialog.setHeaderText("Enter some text, or use default value.");
				ApplicationLauncher.logger.info("Waiting");
				Optional<String> result = dialog.showAndWait();
				ApplicationLauncher.logger.info("Wait over");
				String value = "0";
				if (result.isPresent()) {
					value = result.get();
				}
				addFinalMeterValue(value);
				Is_DialogBox_Active =false;
			}
		}

		public static void addFinalMeterValue(String value){//Gopi why this function?
			/*ArrayList<String> FinalMeterValues = DisplayDeviceDataController.getFinalMeterValues();
		ApplicationLauncher.logger.info("FinalMeterValues" + FinalMeterValues);
		FinalMeterValues.add(value);
		DisplayDeviceDataController.setFinalMeterValues(FinalMeterValues);
		ApplicationLauncher.logger.info("FinalMeterValues" + FinalMeterValues);*/
		}



		public void UI_TableRefreshTrigger(int CurrentTP_TimeInSec) {

			ApplicationLauncher.logger.info("UI_TableRefreshTrigger Invoked:");
			ApplicationLauncher.logger.debug("UI_TableRefreshTrigger: setUI_TableRefreshFlag: true");
			setUI_TableRefreshFlag(true);
			String test_type = ProjectExecutionController.getCurrentTestType();
			if(DeviceDataManagerController.getTestRunType().equals( ConstantApp.TESTPOINT_RUNTYPE_TIMEBASED) || 
					test_type.equals(TestProfileType.Warmup.toString()) ||
					test_type.equals(TestProfileType.NoLoad.toString()) ||
					test_type.equals(TestProfileType.STA.toString()) ||
					test_type.equals(TestProfileType.Repeatability.toString()) ||
					test_type.equals(TestProfileType.SelfHeating.toString()) ||
					test_type.equals(TestProfileType.DutCommand.toString())
					){

				ProjectStatusRefresh.setCurrentTP_ProgressBarMax(CurrentTP_TimeInSec);
				ProjectStatusRefresh.TriggerProgressBarTimerTask(ref_barTC_TimeProgressBar);
			}

		} 

		public void LDU_PreRequisiteForSTATest(){
			ApplicationLauncher.logger.info("LDU_PreRequisiteForSTATest :Entry"  );
			if(!getUserAbortedFlag()) {
				DisplayLDU_ResetError();
				Sleep(1000);
			}
			if(!getUserAbortedFlag()) {
				DisplayLDU_STASetting();
				Sleep(1000);
			}

		}

		public void DisplayLDU_ReadSTATimerInit() {
			ApplicationLauncher.logger.info("DisplayLDU_ReadSTATimerInit :Entry");
			if(SerialDM_Obj.getLDU_STASettingStatus()){
				SerialDM_Obj.LDU_ReadSTATimerTrigger();
			}
			else{
				ApplicationLauncher.logger.info("Error Code LDU09: DisplayLDU_ReadSTATimerInit: LDU_STASettingStatus not invoked");

			}



		}

		public void LDU_PreRequisiteForSTATestV2(){
			ApplicationLauncher.logger.info("LDU_PreRequisiteForSTATestV2 :Entry"  );
			//DisplayLDU_ResetError();
			//DisplayLDU_STASetting();

			if(!getUserAbortedFlag()) {
				SerialDM_Obj.LDU_ResetError();
				Sleep(1000);
			}
			if(!getUserAbortedFlag()) {
				SerialDM_Obj.LDU_STASetting();
				Sleep(1000);
			}


		}

		public void lscsLDU_ResetError(){
			ApplicationLauncher.logger.info("lscsLDU_ResetError :Entry"  );
			//DisplayLDU_ResetError();
			//DisplayLDU_STASetting();

			//if(!ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED) {
			SerialDM_Obj.lscsLDU_ResetError();
			//}
			//Sleep(1000);



		}

		public void lscsLDU_PreRequisiteForSTA_Test(){
			ApplicationLauncher.logger.info("lscsLDU_PreRequisiteForSTA_Test :Entry"  );
			//DisplayLDU_ResetError();
			//DisplayLDU_STASetting();


			//SerialDM_Obj.LDU_ResetError();
			//Sleep(1000);
			if(!getUserAbortedFlag()) {
				SerialDM_Obj.lscsLDU_STASetting();
				Sleep(1000);
			}


		}



		public void lscsLDU_PreRequisiteForConstantTest(){
			ApplicationLauncher.logger.info("lscsLDU_PreRequisiteForConstantTest :Entry"  );
			if(!getUserAbortedFlag()) {
				SerialDM_Obj.lscsLDU_ConstTestSetting();
				Sleep(1000);
			}


		}

		public void DisplayLDU_ReadSTATimerInitV2() {
			ApplicationLauncher.logger.info("DisplayLDU_ReadSTATimerInitV2 :Entry");

			SerialDM_Obj.LDU_ReadSTATimerTrigger();

		}



		public void displayLscsLDU_ReadSTA_TimerInit() {
			ApplicationLauncher.logger.info("displayLscsLDU_ReadSTA_TimerInit :Entry");

			SerialDM_Obj.lscsLDU_ReadSTA_TimerTrigger();

		}

		public boolean DisplayLDU_ResetError() {


			ApplicationLauncher.logger.info("DisplayLDU_ResetError :Entry");
			if(DeviceDataManagerController.getAllPortInitSuccess()){
				SerialDM_Obj.setLDU_ResetErrorStatus(false);
				SerialDM_Obj.LDU_ResetErrorTrigger();
				Integer ResetErrorStatusWaitTimeCounter = 20;
				while (ResetErrorStatusWaitTimeCounter !=0 && !SerialDM_Obj.getLDU_ResetErrorStatus() && (!getUserAbortedFlag()) ){

					ResetErrorStatusWaitTimeCounter--;
					Sleep(250);
				}
			} else {
				ApplicationLauncher.logger.info("Error Code LDU08: DisplayLDU_ResetError: Unable to access LDU Serial Port");

			}
			return SerialDM_Obj.getLDU_ResetErrorStatus();
		}

		public boolean DisplayLDU_STASetting() {
			ApplicationLauncher.logger.info("DisplayLDU_STASetting :Entry");
			if(SerialDM_Obj.getLDU_ResetErrorStatus()){
				SerialDM_Obj.setLDU_STASettingStatus(false);
				SerialDM_Obj.LDU_STASettingTrigger();
				Integer STASettingStatusWaitTimeCounter = 40;
				while (STASettingStatusWaitTimeCounter !=0 && !SerialDM_Obj.getLDU_STASettingStatus() && (!getUserAbortedFlag()) ){
					STASettingStatusWaitTimeCounter--;
					Sleep(250);
				}

			}
			else {
				ApplicationLauncher.logger.info("Error Code LDU01: DisplayLDU_STASetting: LDU_ResetErrorStatus not invoked");

			}

			return SerialDM_Obj.getLDU_STASettingStatus();

		}

		public boolean DisplayLDU_ResetSetting() {
			ApplicationLauncher.logger.info("DisplayLDU_ResetSetting :Entry");
			if(SerialDM_Obj.getLDU_ResetErrorStatus()){
				SerialDM_Obj.setLDU_ResetSettingStatus(false);
				SerialDM_Obj.LDU_ResetSettingTrigger();
				Integer ResetSettingStatusWaitTimeCounter = 60;
				while (ResetSettingStatusWaitTimeCounter !=0 && !SerialDM_Obj.getLDU_ResetSettingStatus() && (!getUserAbortedFlag()) ){
					ResetSettingStatusWaitTimeCounter--;
					Sleep(250);
				}

			}
			else {
				ApplicationLauncher.logger.info("DisplayLDU_ResetSetting: Error Code LDU02: LDU_ResetError not invoked");

			}

			return SerialDM_Obj.getLDU_ResetSettingStatus();


		}

		public void DisplayPwrSrc_TurnOn(){
			ApplicationLauncher.logger.info("DisplayPwrSrc_TurnOn :Entry");

			SerialDataManager.setPowerSrcTurnedOnStatus(false);
			SerialDataManager.setPowerSrcOnFlag(false);
			SerialDataManager.setPowerSrcErrorResponseReceivedStatus(false);
			SerialDM_Obj.PwrSrcON_Trigger();

		}

		public void ExecuteTimerDisplay(){
			ApplicationLauncher.logger.info("ExecuteTimerDisplay :Entry");
			if(!getExecuteStopStatus()){
				ApplicationLauncher.logger.debug("ExecuteTimerDisplay :Entry2");
				ExecuteTimeLineObj = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						int CurrentTimeInSec = getExecuteTimeCounter();
						//if (CurrentTimeInSec >= 0){
						int sec = (CurrentTimeInSec % 60);
						int min = ((CurrentTimeInSec / 60)%60);
						int hours = ((CurrentTimeInSec/60)/60);
						final String  PrintTime = String.format("%02d", hours) + ":" + String.format("%02d", min) + ":" + String.format("%02d", sec);

						Platform.runLater(() -> {
							DisplayExecuteCounterTimeFormat.setValue(PrintTime); 
						});

						ApplicationLauncher.logger.info("ExecuteTimerDisplay: ref_txtTimeLeft: " +PrintTime);
						if(DecrementExecuteTimeCounter()<0){
							//if(DecrementExecuteTimeCounter() == -1){
							ExecuteTimeLineObj.stop();
							Trigger_ExecuteStop();
							

						}else if(getStepRunFlag()){
							if(getExecuteTimeCounter() < 30){
								int extend_executetime = getExecuteTimeCounter() + ExtendTimeForStepRunInSec;
								setExecuteTimeCounter(extend_executetime);
							}
						}
						/*				}else{
						ApplicationLauncher.logger.info("ExecuteTimerDisplay :getExecuteTimeCounter() less than zero:"+ CurrentTimeInSec);
					}*/
					}
				}));
				ExecuteTimeLineObj.setCycleCount(Timeline.INDEFINITE);
				ExecuteTimeLineObj.play();
				ApplicationLauncher.logger.debug("ExecuteTimerDisplay : Exit1");
			}
			ApplicationLauncher.logger.debug("ExecuteTimerDisplay : Exit2");
		}

		public void Trigger_ExecuteStop() {
			ApplicationLauncher.logger.info("Trigger_ExecuteStop : Entry");
			if(!getExecuteStopStatus()){
				ExecuteStopTimer = new Timer();
				ExecuteStopTimer.schedule(new ExecuteStopTask(), 10);//100);
			}else{
				ApplicationLauncher.logger.info("Trigger_ExecuteStop : ExecuteStopTask already in progress");
			}
		}

		public void RefStd_PreRequisite(){

			ApplicationLauncher.logger.info("RefStd_PreRequisite :Entry"  );
			SerialDM_Obj.ClearStdRefSerialData();
			SerialDataRadiantRefStd.ClearRefStd_ReadSerialData();
			DisplayRefTimerInit();
		}

		public void mteRefStd_PreRequisite(){

			ApplicationLauncher.logger.info("mteRefStd_PreRequisite :Entry"  );
			SerialDM_Obj.ClearStdRefSerialData();
			//SerialDataRadiantRefStd.ClearRefStd_ReadSerialData();
			SerialDataMteRefStd.ClearRefStd_ReadSerialData();
			mteDisplayRefTimerInit();
		}

		public void kreRefStd_PreRequisite(){

			ApplicationLauncher.logger.info("kreRefStd_PreRequisite :Entry"  );
			SerialDM_Obj.ClearStdRefSerialData();
			SerialDataRefStdKre.ClearRefStd_ReadSerialData();
			kreDisplayRefTimerInit();
		}

		public void bofaRefStd_PreRequisite(){

			ApplicationLauncher.logger.info("bofaRefStd_PreRequisite :Entry"  );
			//SerialDM_Obj.ClearStdRefSerialData();
			//SerialDataRefStdKre.ClearRefStd_ReadSerialData();
			bofaDisplayRefTimerInit();
		}

		public void kiggsRefStd_PreRequisite(){

			ApplicationLauncher.logger.info("kiggsRefStd_PreRequisite :Entry"  );
			SerialDM_Obj.ClearStdRefSerialData();
			SerialDataRefStdKiggs.ClearRefStd_ReadSerialData();
			kiggsDisplayRefTimerInit();


		}

		public void DisplayRefTimerInit() {
			ApplicationLauncher.logger.info("DisplayRefTimerInit :Entry"  );
			if(DeviceDataManagerController.getAllPortInitSuccess()){
				SerialDM_Obj.RefStdTimerInit();
			} else {

				ApplicationLauncher.logger.info("Error Code LDU10: DisplayRefTimerInit: Unable to access Ref Standard Serial Port");

			}


		}
		public void mteDisplayRefTimerInit() {
			ApplicationLauncher.logger.info("mteDisplayRefTimerInit :Entry"  );
			if(DeviceDataManagerController.getAllPortInitSuccess()){
				SerialDM_Obj.mteRefStdTimerInit();
			} else {

				ApplicationLauncher.logger.info("Error Code LDU10: mteDisplayRefTimerInit: Unable to access Ref Standard Serial Port");

			}


		}

		public void kreDisplayRefTimerInit() {
			ApplicationLauncher.logger.info("kreDisplayRefTimerInit :Entry"  );
			ApplicationLauncher.logger.info("kreDisplayRefTimerInit :getAllPortInitSuccess: " + DeviceDataManagerController.getAllPortInitSuccess()  );
			if(DeviceDataManagerController.getAllPortInitSuccess()){
				SerialDM_Obj.kreRefStdTimerInit();
			} else {

				ApplicationLauncher.logger.info("Error Code LDU10: kreDisplayRefTimerInit: Unable to access Ref Standard Serial Port");

			}


		}


		public void bofaDisplayRefTimerInit() {
			ApplicationLauncher.logger.info("bofaDisplayRefTimerInit :Entry"  );
			if(DeviceDataManagerController.getAllPortInitSuccess()){
				//SerialDM_Obj.kreRefStdTimerInit();cdscd
				//if(ProcalFeatureEnable.BOFA_REFSTD_CONNECTED){
				Data_RefStdBofa.setRsmFreqReadingRequired(true);
				//}
				Data_RefStdBofa refStdBofa = new Data_RefStdBofa(); 
				refStdBofa.bofaRefStdTimerInit();
			} else {

				ApplicationLauncher.logger.info("Error Code LDU10: bofaDisplayRefTimerInit: Unable to access Ref Standard Serial Port");

			}


		}

		public void kiggsDisplayRefTimerInit() {
			ApplicationLauncher.logger.info("kiggsDisplayRefTimerInit :Entry"  );
			if(DeviceDataManagerController.getAllPortInitSuccess()){
				SerialDM_Obj.kiggsRefStdTimerInit();
				//SerialDM_Obj.kiggsRefStdTimerInitDebug();//#DEBUG_2022_09_08_REF_STD_KIGG

			} else {

				ApplicationLauncher.logger.info("Error Code LDU10: kiggsDisplayRefTimerInit: Unable to access Ref Standard Serial Port");

			}


		}

		public void exitCurrentTestPointExecution(){
			ApplicationLauncher.logger.info("exitCurrentTestPointExecution :Entry");
			setExecuteStopStatus(true);
			if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_ENABLED){
				setRefStdFeedBackControlFlagEnabled(false);
				setRefStdFeedBackControlSuspended(false);

			}

			//if(ProcalFeatureEnable.BOFA_LDU_CONNECTED){
			//	Data_LduBofa.bofaSendLduExitDialTest();//bofaSendLduResetErrorAll();
			//}
			if(ProcalFeatureEnable.BOFA_REFSTD_CONNECTED){

				Data_RefStdBofa.setRsmFreqReadingRequired(true);
			}
			ApplicationHomeController.update_left_status("Exit TP execution initiated",ConstantApp.LEFT_STATUS_DEBUG);
			DeviceDataManagerController.setLDU_ReadDataFlag(false);
			setUI_TableRefreshFlag(false);
			//Sleep(200);
			//SerialDM_Obj.LDU_ResetError();
			if (SerialDataManager.getPowerSrcTurnedOnStatus() || SerialDataManager.getPowerSrcOnFlag()){

				if(DisplayDataObj.getVoltageResetRequired()){
					if(!SerialDataManager.isPowerSourceTurnedOff()){
						SerialDM_Obj.SetPowerSourceOff();
					}
					SerialDM_Obj.PerformPowerSrcReboot();
				}
				else{
					if(!getRepTestFlag() && !getSelfHeatingTestFlag()){ // updated the logic to meet the condition //Gopi
						if(!getStepRunFlag()){
							if(ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED)
							{
								ZeroCurrentPowerOn();
							}
							if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
								WaitForPowerSrcTurnOn();
							}
							
							if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
								//ZeroCurrentPowerOn();
								Data_PowerSourceBofa.sendCurrentStoppingCommand();
							}
						}
					}
					if(getCurrentTestType().equals(TestProfileType.STA.toString()) ||
							getCurrentTestType().equals(TestProfileType.NoLoad.toString())){
						//Sleep(5000);
						SleepForSecondsWithAbortMonitoring(5);
						if(!getUserAbortedFlag()) {
							if ( (ProcalFeatureEnable.STA_KWH_READING_PROMPT_ENABLED) && (getCurrentTestType().equals(TestProfileType.STA.toString()) )) { 
								getFinalMeterValuesFromUI();//Get Final Value
								waitForFinalMeterReading();
							}else if ( (ProcalFeatureEnable.CREEP_KWH_READING_PROMPT_ENABLED) && (getCurrentTestType().equals(TestProfileType.NoLoad.toString()) )) { 
								getFinalMeterValuesFromUI();//Get Final Value
								waitForFinalMeterReading();
							}

							ApplicationHomeController.update_left_status("Updating KWh to DB",ConstantApp.LEFT_STATUS_DEBUG);
							DisplayDataObj.SaveKWhToDB();
							ApplicationHomeController.update_left_status("Finished updating KWh to DB",ConstantApp.LEFT_STATUS_DEBUG);
						}
					}
				}
			}


			DeviceDataManagerController.setVolt_Unbalanced_PowerOn(false);
			SerialDataManager.resetSerialLDU_ComRefreshTimeInMsec();
			Platform.runLater(() -> {
				DisplayTestPointExecutionTimeFormat.setValue("");
			});
			DisplayInstantMetricsController.ResetInstantMetrics_Wh_Display();
			if(ProcalFeatureEnable.KIGGS_REFSTD_CONNECTED){
				SerialDM_Obj.kiggsRefStdSetCurrentRangeSettingMaxTap();
			}

			ApplicationLauncher.logger.info("exitCurrentTestPointExecution :Sleeping for CoolOffTimeInMSec:"+CoolOffTimeInMSec);
			Sleep(CoolOffTimeInMSec);
			ApplicationLauncher.logger.info("exitCurrentTestPointExecution :Sleeping awake after CoolOffTimeInMSec:"+CoolOffTimeInMSec);
			updateExecutionStatusOnDB();
			
			if(ProcalFeatureEnable.BOFA_LDU_CONNECTED){
				Data_LduBofa.resetNthOfErrors();
				Data_LduBofa.resetErrorValue();
				Data_LduBofa.resetDialTestPulseCount();
				Data_LduBofa.resetStaCreepTestPulseCount();
				
			}
			
			DeviceDataManagerController.resetStepRunModeAtleastOneResultReadCompleted();
			try{
				IncrementCurrentTestPoint_Index();
				setCurrentTestPoint_ExecutionCompletedStatus(true);
				UpdateProjectProgressBar();
				refreshProjectStatusGraphGUI();
				setOneTimeExecuted(true);
				ProjectStatusRefresh.ResetTestPointProgressBar(ref_barTC_TimeProgressBar);
				SaveProjectRunEndTime();
			} catch(Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("exitCurrentTestPointExecution: Exception: " + e.getMessage());
			}
			setExecuteStopStatus(false);
			DeviceDataManagerController.setPhaseRevPowerOn(false);
			/*if(ProcalFeatureEnable.BOFA_LDU_CONNECTED){
				Data_LduBofa.resetNthOfErrors();
				Data_LduBofa.resetErrorValue();
				Data_LduBofa.resetDialTestPulseCount();
				Data_LduBofa.resetStaCreepTestPulseCount();
			}*/
			
			/*if(DisplayDataObj.getDevicesToBeRead().size() == 0){
				
				ApplicationLauncher.logger.info("exitCurrentTestPointExecution : Execution Status completed-1");
				updateDB_DeviceExecutionStatus( ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID, ConstantReport.RESULT_STATUS_PASS.trim(),ConstantApp.EXECUTION_STATUS_COMPLETED, 
						ConstantReport.RESULT_DATA_TYPE_EXECUTION_STATUS);
				updateDB_DeviceExecutionStatus( ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID, ConstantReport.RESULT_STATUS_PASS.trim(),ConstantApp.EXECUTION_STATUS_COMPLETED, 
						ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
			}else if(SerialDM_Obj.getSkipCurrentTP_Execution()){
				ApplicationLauncher.logger.info("exitCurrentTestPointExecution : Execution Status Skipped-1");
				updateDB_DeviceExecutionStatus( ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID, ConstantReport.RESULT_STATUS_UNDEFINED.trim(),ConstantApp.EXECUTION_STATUS_SKIPPED, 
						ConstantReport.RESULT_DATA_TYPE_EXECUTION_STATUS);
				updateDB_DeviceExecutionStatus( ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID, ConstantReport.RESULT_STATUS_UNDEFINED.trim(),ConstantApp.EXECUTION_STATUS_SKIPPED, 
						ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
			}else{
				updateDB_DeviceExecutionStatus( ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID, ConstantReport.RESULT_STATUS_UNDEFINED.trim(),ConstantApp.EXECUTION_STATUS_ABORTED, 
						ConstantReport.RESULT_DATA_TYPE_EXECUTION_STATUS);
				updateDB_DeviceExecutionStatus( ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID, ConstantReport.RESULT_STATUS_UNDEFINED.trim(),ConstantApp.EXECUTION_STATUS_ABORTED, 
						ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
				if(getStepRunFlag()){
					boolean stepRunModeAllDeviceReadAtleastOnce = true;
					for(String lduPosition : ProjectExecutionController.getDeviceMountedMap().keySet()){
						
						if(DisplayDataObj.getStepRunModeAtleastOneResultReadCompleted().get(Integer.parseInt(lduPosition))== false){
							stepRunModeAllDeviceReadAtleastOnce = false;
							break;
						}
						
					}
					
					if(stepRunModeAllDeviceReadAtleastOnce){
						ApplicationLauncher.logger.info("exitCurrentTestPointExecution : Execution Status completed-2");
						updateDB_DeviceExecutionStatus( ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID, ConstantReport.RESULT_STATUS_PASS.trim(),ConstantApp.EXECUTION_STATUS_COMPLETED, 
								ConstantReport.RESULT_DATA_TYPE_EXECUTION_STATUS);
						updateDB_DeviceExecutionStatus( ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID, ConstantReport.RESULT_STATUS_PASS.trim(),ConstantApp.EXECUTION_STATUS_COMPLETED, 
								ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
					
					}else{
						ApplicationLauncher.logger.info("exitCurrentTestPointExecution : Execution Status Aborted-1");
						updateDB_DeviceExecutionStatus( ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID, ConstantReport.RESULT_STATUS_UNDEFINED.trim(),ConstantApp.EXECUTION_STATUS_ABORTED, 
								ConstantReport.RESULT_DATA_TYPE_EXECUTION_STATUS);
						updateDB_DeviceExecutionStatus( ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID, ConstantReport.RESULT_STATUS_UNDEFINED.trim(),ConstantApp.EXECUTION_STATUS_ABORTED, 
								ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
					}
				}
			}*/
			
			/*updateExecutionStatusOnDB();
			
			if(ProcalFeatureEnable.BOFA_LDU_CONNECTED){
				Data_LduBofa.resetNthOfErrors();
				Data_LduBofa.resetErrorValue();
				Data_LduBofa.resetDialTestPulseCount();
				Data_LduBofa.resetStaCreepTestPulseCount();
				
			}
			
			DisplayDataObj.resetStepRunModeAtleastOneResultReadCompleted();*/
			
			ApplicationHomeController.update_left_status("Exit TP execution done",ConstantApp.LEFT_STATUS_DEBUG);
			semLockExecutionInprogress = false;
			ApplicationLauncher.logger.info("exitCurrentTestPointExecution: semLockExecutionInprogress: released");

		}
		
		public void updateExecutionStatusOnDB(){
			
			if(DeviceDataManagerController.getDevicesToBeRead().size() == 0){
				
				ApplicationLauncher.logger.info("updateExecutionStatusOnDB : Execution Status completed-1");
				LiveTableDataManager.UpdateliveTableData(ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID, ConstantReport.RESULT_STATUS_PASS.trim(),ConstantApp.EXECUTION_STATUS_COMPLETED);//
				
				updateDB_DeviceExecutionStatus( ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID, ConstantReport.RESULT_STATUS_PASS.trim(),ConstantApp.EXECUTION_STATUS_COMPLETED, 
						ConstantReport.RESULT_DATA_TYPE_EXECUTION_STATUS);
				updateDB_DeviceExecutionStatus( ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID, ConstantReport.RESULT_STATUS_PASS.trim(),ConstantApp.EXECUTION_STATUS_COMPLETED, 
						ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
			}else if(SerialDM_Obj.getSkipCurrentTP_Execution()){
				ApplicationLauncher.logger.info("updateExecutionStatusOnDB : Execution Status Skipped-1");
				LiveTableDataManager.UpdateliveTableData(ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID, ConstantReport.RESULT_STATUS_UNDEFINED.trim(),ConstantApp.EXECUTION_STATUS_SKIPPED);
				updateDB_DeviceExecutionStatus( ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID, ConstantReport.RESULT_STATUS_UNDEFINED.trim(),ConstantApp.EXECUTION_STATUS_SKIPPED, 
						ConstantReport.RESULT_DATA_TYPE_EXECUTION_STATUS);
				updateDB_DeviceExecutionStatus( ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID, ConstantReport.RESULT_STATUS_UNDEFINED.trim(),ConstantApp.EXECUTION_STATUS_SKIPPED, 
						ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
			}else{
				/*updateDB_DeviceExecutionStatus( ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID, ConstantReport.RESULT_STATUS_UNDEFINED.trim(),ConstantApp.EXECUTION_STATUS_ABORTED, 
						ConstantReport.RESULT_DATA_TYPE_EXECUTION_STATUS);
				updateDB_DeviceExecutionStatus( ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID, ConstantReport.RESULT_STATUS_UNDEFINED.trim(),ConstantApp.EXECUTION_STATUS_ABORTED, 
						ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);*/
				if(getStepRunFlag()){
					boolean stepRunModeAllDeviceReadAtleastOnce = true;
					for(String lduPosition : ProjectExecutionController.getDeviceMountedMap().keySet()){
						
						if(DeviceDataManagerController.getStepRunModeAtleastOneResultReadCompleted().get(Integer.parseInt(lduPosition))== false){
							stepRunModeAllDeviceReadAtleastOnce = false;
							break;
						}
						
					}
					
					if(stepRunModeAllDeviceReadAtleastOnce){
						ApplicationLauncher.logger.info("updateExecutionStatusOnDB : Execution Status completed-2");
						LiveTableDataManager.UpdateliveTableData(ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID, ConstantReport.RESULT_STATUS_PASS.trim(),ConstantApp.EXECUTION_STATUS_COMPLETED);
						
						updateDB_DeviceExecutionStatus( ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID, ConstantReport.RESULT_STATUS_PASS.trim(),ConstantApp.EXECUTION_STATUS_COMPLETED, 
								ConstantReport.RESULT_DATA_TYPE_EXECUTION_STATUS);
						updateDB_DeviceExecutionStatus( ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID, ConstantReport.RESULT_STATUS_PASS.trim(),ConstantApp.EXECUTION_STATUS_COMPLETED, 
								ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
					
					}else{
						ApplicationLauncher.logger.info("updateExecutionStatusOnDB : Execution Status Aborted-1");
						LiveTableDataManager.UpdateliveTableData(ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID, ConstantReport.RESULT_STATUS_UNDEFINED.trim(),ConstantApp.EXECUTION_STATUS_ABORTED);
						
						updateDB_DeviceExecutionStatus( ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID, ConstantReport.RESULT_STATUS_UNDEFINED.trim(),ConstantApp.EXECUTION_STATUS_ABORTED, 
								ConstantReport.RESULT_DATA_TYPE_EXECUTION_STATUS);
						updateDB_DeviceExecutionStatus( ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID, ConstantReport.RESULT_STATUS_UNDEFINED.trim(),ConstantApp.EXECUTION_STATUS_ABORTED, 
								ConstantReport.RESULT_DATA_TYPE_ERROR_VALUE);
					}
				}
			}
		}

		public void Sleep(int timeInMsec) {

			try {
				Thread.sleep(timeInMsec);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("Sleep :InterruptedException:"+ e.getMessage());
			}

		}

		public void SleepForSecondsWithAbortMonitoring(int timeInSec) {

			while((timeInSec>0) && (!getUserAbortedFlag())) {

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					
					e.printStackTrace();
					ApplicationLauncher.logger.error("Sleep :InterruptedException:"+ e.getMessage());
				}
				timeInSec--;
			}

		}

		public void LDU_PreRequisiteForReadError(){
			ApplicationLauncher.logger.info("LDU_PreRequisiteForReadError :Entry"  );
			DisplayLDU_ResetError();
			DisplayLDU_ResetSetting();

			//SerialDM_Obj.LDU_ResetError();
			//SerialDM_Obj.LDU_ResetSetting();

		}

		public void LDU_PreRequisiteForReadErrorV2(){
			ApplicationLauncher.logger.info("LDU_PreRequisiteForReadErrorV2 :Entry"  );
			// version s3.9.5 -  added below flag - because Test point was running and not moving forward
			DeviceDataManagerController.setLDU_ReadDataFlag( true);
			if(!getUserAbortedFlag()) {
				SerialDM_Obj.LDU_ResetError();
				Sleep(1000);//1000
			}
			if(!getUserAbortedFlag()) {
				SerialDM_Obj.LDU_ResetSetting();
				Sleep(1000);//1000
			}

		}

		public void lscsLDU_PreRequisiteForReadError(){
			ApplicationLauncher.logger.info("lscsLDU_PreRequisiteForReadError :Entry"  );
			// version s3.9.5 -  added below flag - because Test point was running and not moving forward
			DeviceDataManagerController.setLDU_ReadDataFlag( true);
			Sleep(1000);//1000
			if(!getUserAbortedFlag()) {
				SerialDM_Obj.lscsUploadLOE_Setting();
				Sleep(1000);
			}
			//SerialDM_Obj.LDU_ResetError();
			//Sleep(1000);//1000
			//SerialDM_Obj.LDU_ResetSetting();
			//Sleep(1000);//1000



		}

		public void DisplayLDU_ReadErrorTimerInit() {
			ApplicationLauncher.logger.info("DisplayLDU_ReadErrorTimerInit :Entry");
			ApplicationLauncher.logger.info("DisplayLDU_ReadErrorTimerInit :getLDU_ResetSettingStatus():" +SerialDM_Obj.getLDU_ResetSettingStatus());
			if(SerialDM_Obj.getLDU_ResetSettingStatus()){
				ApplicationLauncher.logger.info("DisplayLDU_ReadErrorTimerInit :Triggering: LDU_ReadErrorTimerTrigger" );

				SerialDM_Obj.LDU_ReadErrorTimerTrigger();
			}
			else{
				ApplicationLauncher.logger.info("DisplayLDU_ReadErrorTimerInit :Error Code LDU03: LDU_ResetSetting not invoked");
			}
		}

		public void DisplayLDU_ReadErrorTimerInitV2() {
			ApplicationLauncher.logger.info("DisplayLDU_ReadErrorTimerInitV2 :Entry");
			SerialDM_Obj.LDU_ReadErrorTimerTrigger();
		}

		public void lscsDisplayLDU_ReadErrorTimerInit() {
			ApplicationLauncher.logger.info("lscsDisplayLDU_ReadErrorTimerInit :Entry");
			SerialDM_Obj.lscsLDU_ReadErrorTimerTrigger();

		}

		public void LDU_PreRequisiteForCreepTest(){
			ApplicationLauncher.logger.info("LDU_PreRequisiteForCreepTest :Entry"  );
			DisplayLDU_ResetError();
			DisplayLDU_CreepSetting();


		}

		public void DisplayLDU_ReadCreepTimerInit() {
			ApplicationLauncher.logger.info("DisplayLDU_ReadCreepTimerInit :Entry");
			if(SerialDM_Obj.getLDU_CreepSettingStatus()){
				SerialDM_Obj.LDU_ReadCreepTimerTrigger();
			}
			else{
				ApplicationLauncher.logger.info("DisplayLDU_ReadCreepTimerInit :Error Code LDU05 : LDU_CreepSettingStatus not invoked");
			}
		}

		public void LDU_PreRequisiteForCreepTestV2(){
			ApplicationLauncher.logger.info("LDU_PreRequisiteForCreepTestV2 :Entry"  );
			//DisplayLDU_ResetError();
			//DisplayLDU_CreepSetting();
			if(!getUserAbortedFlag()) {
				SerialDM_Obj.LDU_ResetError();
				Sleep(1000);
			}
			if(!getUserAbortedFlag()) {
				SerialDM_Obj.LDU_CreepSetting();
				Sleep(1000);
			}


		}

		public void lscsLDU_PreRequisiteForCreepTest(){
			ApplicationLauncher.logger.info("lscsLDU_PreRequisiteForCreepTest :Entry"  );
			//DisplayLDU_ResetError();
			//DisplayLDU_CreepSetting();
			//SerialDM_Obj.LDU_ResetError();
			//Sleep(1000);
			if(!getUserAbortedFlag()) {
				SerialDM_Obj.lscsLDU_CreepSetting();
				Sleep(1000);
			}


		}

		public void lscsLDU_StopCreepTest(){
			ApplicationLauncher.logger.info("lscsLDU_SendCommndToStopCreepTest :Entry"  );
			//DisplayLDU_ResetError();
			//DisplayLDU_CreepSetting();
			//SerialDM_Obj.LDU_ResetError();
			//Sleep(1000);
			SerialDM_Obj.lscsLDU_SendStopCreep();
			//Sleep(1000);


		}

		public void lscsLDU_StopSTA_Test(){
			ApplicationLauncher.logger.info("lscsLDU_StopSTA_Test :Entry"  );
			//DisplayLDU_ResetError();
			//DisplayLDU_CreepSetting();
			//SerialDM_Obj.LDU_ResetError();
			//Sleep(1000);
			SerialDM_Obj.lscsLDU_SendStopSTA();
			//Sleep(1000);


		}



		public void lscsLDU_StopConstantTest(){
			ApplicationLauncher.logger.info("lscsLDU_StopConstantTest :Entry"  );
			//DisplayLDU_ResetError();
			//DisplayLDU_CreepSetting();
			//SerialDM_Obj.LDU_ResetError();
			//Sleep(1000);
			SerialDM_Obj.lscsLDU_SendStopConstantTest();
			//Sleep(1000);


		}

		public void lscsLDU_SendDataRefreshCmd(){
			ApplicationLauncher.logger.info("lscsLDU_StopConstantTest :Entry"  );

			SerialDM_Obj.lscsLDU_SendDataRefreshCmd();
			//Sleep(1000);


		}

		public void lscsLDU_PowerSourceCurrentRelayCutOff(){
			ApplicationLauncher.logger.info("lscsLDU_PowerSourceCurrentRelayCutOff :Entry"  );
			//DisplayLDU_ResetError();
			//DisplayLDU_CreepSetting();
			//SerialDM_Obj.LDU_ResetError();
			//Sleep(1000);
			SerialDM_Obj.lscsLDU_SendPowerSourceCurrentRelayCutOff();
			//Sleep(1000);


		}

		public void DisplayLDU_ReadCreepTimerInitV2() {
			ApplicationLauncher.logger.info("DisplayLDU_ReadCreepTimerInitV2 :Entry");

			SerialDM_Obj.LDU_ReadCreepTimerTrigger();

		}

		public void displayLscsLDU_ReadCreepTimerInit() {
			ApplicationLauncher.logger.info("displayLscsLDU_ReadCreepTimerInit :Entry");

			SerialDM_Obj.lscsLDU_ReadCreepTimerTrigger();

		}

		public void LDU_PreRequisiteForConstTest(){
			ApplicationLauncher.logger.info("LDU_PreRequisiteForConstTest :Entry"  );
			DisplayLDU_ResetError();
			DisplayLDU_ConstSetting();

		}



		public void DisplayLDU_ReadConstTimerInit() {
			ApplicationLauncher.logger.info("DisplayLDU_ReadConstTimerInit :Entry");
			if(SerialDM_Obj.getLDU_ConstSettingStatus()){
				SerialDM_Obj.LDU_ReadConstTimerTrigger();
			}
			else{
				ApplicationLauncher.logger.info("DisplayLDU_ReadConstTimerInit :Error Code LDU12 : LDU_CreepSettingStatus not invoked");
			}

		}



		public void displayLscsLDU_ReadConstantTimerInit() {
			ApplicationLauncher.logger.info("displayLscsLDU_ReadConstantTimerInit :Entry");
			//if(SerialDM_Obj.getLDU_ConstSettingStatus()){
			SerialDM_Obj.lscsLDU_ReadConstTimerTrigger();
			//}
			//else{
			//	ApplicationLauncher.logger.info("displayLscsLDU_ReadConstantTimerInit :Error Code LDU12 : LDU_ConstantSettingStatus not invoked");
			//}

		}

		public static void setExecuteTimeCounter(Integer Counter){
			ApplicationLauncher.logger.info("setExecuteTimeCounter :Entry : " + Counter);
			ExecuteTimeCounter = Counter;

		}

		public static Integer getExecuteTimeCounter(){
			return ExecuteTimeCounter ;

		}

		public int DecrementExecuteTimeCounter(){
			/*		if (ExecuteTimeCounter >= 0){
			ExecuteTimeCounter--;
		}
		return ExecuteTimeCounter;*/
			return --ExecuteTimeCounter;

		}

		public boolean  CloseAllComPort(){
			boolean status = false;
			ApplicationLauncher.logger.info("CloseAllComPort: Entry");
			if(DeviceDataManagerController.getAllPortInitSuccess()){
				if(!ProcalFeatureEnable.LSCS_CALIBRATION_MODE_ENABLED)  {
					//DisplayDisconnectRefStd();
					DisplayDisconnectLDU();
				}
				DisplayDisconnectRefStd();
				DisplayDisconnectPwrSrc();
				if(ProcalFeatureEnable.ICT_INTERFACE_ENABLED){
					String metertype = DeviceDataManagerController.getDeployedEM_ModelType();
					if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
						DisplayDisconnectICT();
					}
				}

				if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_FEATURE_ENABLED){

					DisplayDisconnectHarmonicsSrc();

				}

				//ApplicationLauncher.logger.info("CloseAllComPort: Closed all serial port");
			}
			
			if(ProcalFeatureEnable.DUT_GUI_SEUP_CALIBATION_MODE_ENABLED ) {
				DutSerialDataManager dutSerialDataManager = new DutSerialDataManager();
				for(int positionId = 1; positionId<=48; positionId++ ) {
					dutSerialDataManager.disconnectDutX(positionId);
				}
				
			}
			
			ApplicationLauncher.logger.info("CloseAllComPort: Closed all serial port");
			/*		
		try{
			ExecuteTimeLineObj.stop();
		}
		catch(Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("CloseAllComPort: Exception: " + e.toString());
		}*/

			return status;
		}

		public boolean  CancelExecuteTimeLine(){
			boolean status = false;

			try{
				ExecuteTimeLineObj.stop();
			}
			catch(Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("CancelExecuteTimeLine: Exception: " + e.toString());
			}
			return status;
		}

		public void  DisplayDisconnectPwrSrc (){
			SerialDM_Obj.DisconnectPwrSrc();
		}

		public void  DisplayDisconnectLDU(){
			SerialDM_Obj.DisconnectLDU();
			DeviceDataManagerController.setLDU_ReadDataFlag( false);
		}

		public void  DisplayDisconnectICT(){
			SerialDM_Obj.DisconnectICT();
			DeviceDataManagerController.setIctReadData( false);
		}

		public void  DisplayDisconnectHarmonicsSrc(){
			SerialDM_Obj.DisConnectHarmonicsSrc();
			//DisplayDataObj.setIctReadData( false);
		}


		public void  DisplayDisconnectRefStd(){

			if(!ProcalFeatureEnable.REFSTD_CONNECTED_NONE){
				if(ProcalFeatureEnable.REFSTD_PORT_MANAGER_V2_ENABLED) {

					DisplayDataObj.refStdDisconnectPort_V2();
				}else {

					if(!ProcalFeatureEnable.BOFA_REFSTD_CONNECTED){
						SerialDM_Obj.DisconnectRefStd();
					}
				}
			}
			DeviceDataManagerController.setRefStdReadDataFlag(false);
		}

		public void Display_Cus_PwrSrc_TurnOn(){
			ApplicationLauncher.logger.info("Display_Cus_PwrSrc_TurnOn :Entry");
			SerialDataManager.setPowerSrcTurnedOnStatus(false);
			SerialDataManager.setPowerSrcOnFlag(false);
			SerialDataManager.setPowerSrcErrorResponseReceivedStatus(false);
			SerialDM_Obj.Cus_PwrSrcON_Trigger();
		}

		public void DisplayPwrSrc_TurnOff(){

			SerialDM_Obj.PwrSrcOFF_Trigger();
		}


		public void DisplayShutDownAllCompPortsTrigger(){

			ApplicationLauncher.logger.info("DisplayShutDownAllCompPortsTrigger : Entry");
			ShutDownAllComPortTimer = new Timer();
			ShutDownAllComPortTimer.schedule(new ShutDownAllCompPorts(), 100);
		}

		public void Display_Har_PwrSrc_TurnOn(){

			ApplicationLauncher.logger.info("Display_Har_PwrSrc_TurnOn : Entry");



			SerialDataManager.setPowerSrcTurnedOnStatus(false);
			SerialDataManager.setPowerSrcOnFlag(false);
			SerialDataManager.setPowerSrcErrorResponseReceivedStatus(false);
			//SerialDM_Obj.Har_PwrSrcON_Trigger();

			if(ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){
				SerialDM_Obj.Har_PwrSrcON_Trigger();
			}
			else if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
				//initialiseHarmonicsData();
				if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_FEATURE_ENABLED){
					if(!ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_DSP_SLAVE_SERIAL_CONNECTED){
						lscsSendHarmonicsDataToSlave();
					}else{
						Har_PwrSrcDataTransmitON_Trigger();
					}
				}else{
					Har_PwrSrcDataTransmitON_Trigger();
				}

			}	
			ApplicationLauncher.logger.info("Display_Har_PwrSrc_TurnOn :Exit");
		}
		//========================================================================================================================================================	
		public void Har_PwrSrcDataTransmitON_Trigger() {
			ApplicationLauncher.logger.debug("Har_PwrSrcDataTransmitON_Trigger :Entry");
			Sleep(200);
			try{
				//if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_DSP_SLAVE_SERIAL_CONNECTED ){
				lscsSendHarmonicsDataToSlave();
				/*}else{
				lscsSendHarmonicsDataToStm32Master();
			}*/
			} catch(Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("Har_PwrSrcDataTransmitON_Trigger: Exception: " + e.getMessage());
			}
			//fetchHarmonicsDataFromDBandStore(); 
			//pwrSrcDataTransmitTimer = new Timer();
			//pwrSrcDataTransmitTimer.schedule(new har_PwrSrcDataTransmitTask(), 100); 
			ApplicationLauncher.logger.debug("Har_PwrSrcDataTransmitON_Trigger : Exit");
		}

		class har_PwrSrcDataTransmitTask extends TimerTask {
			public void run() {
				ApplicationLauncher.logger.debug("har_PwrSrcDataTransmitTask : Entry:" );
				//fetchHarmonicsDataFromDBandStore();  
				ApplicationLauncher.logger.debug("har_PwrSrcDataTransmitTask : Exit:" );
			}
		}

		//========================================================================================================================================================
		public boolean DisplayLDU_CreepSetting() {
			ApplicationLauncher.logger.info("DisplayLDU_CreepSetting :Entry");
			if(SerialDM_Obj.getLDU_ResetErrorStatus()){
				SerialDM_Obj.setLDU_CreepSettingStatus(false);
				SerialDM_Obj.LDU_CreepSettingTrigger();
				Integer CreepSettingStatusWaitTimeCounter = 40;
				while (CreepSettingStatusWaitTimeCounter !=0 && !SerialDM_Obj.getLDU_CreepSettingStatus() && (!getUserAbortedFlag()) ){
					CreepSettingStatusWaitTimeCounter--;
					Sleep(250);
				}

			}
			else {
				ApplicationLauncher.logger.info("DisplayLDU_CreepSetting :Error Code LDU06 : LDU_ResetError not invoked");

			}

			return SerialDM_Obj.getLDU_CreepSettingStatus();

		}

		public boolean DisplayLDU_ConstSetting() {
			ApplicationLauncher.logger.info("DisplayLDU_ConstSetting :Entry");
			if(SerialDM_Obj.getLDU_ResetErrorStatus()){
				SerialDM_Obj.setLDU_ConstSettingStatus(false);
				SerialDM_Obj.LDU_ConstSettingTrigger();
				Integer ConstSettingStatusWaitTimeCounter = 40;
				while (ConstSettingStatusWaitTimeCounter !=0 && !SerialDM_Obj.getLDU_ConstSettingStatus() && (!getUserAbortedFlag()) ){
					ConstSettingStatusWaitTimeCounter--;
					Sleep(250);
				}

			}
			else {
				ApplicationLauncher.logger.info("DisplayLDU_ConstSetting :Error Code LDU07 : LDU_ResetError not invoked");

			}

			return SerialDM_Obj.getLDU_ConstSettingStatus();

		}

		public void refStdFeedBackControlTrigger(){

			ApplicationLauncher.logger.info("refStdFeedBackControlTrigger :Entry");
			setRefStdFeedBackControlFlagEnabled(true);
			setRefStdFeedBackControlSuspended(false);
			refStdFeedBackControlTimer = new Timer();
			refStdFeedBackControlTimer.schedule(new refStdFeedBackVoltCurrentControl(), 10);
		}

		public void waitForTargetCurrentFineTune(){
			int waitTimeOutInSecMax = ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_FINETUNE_CURRENT_WAIT_TIME_IN_SEC;//60;
			float currentFineTunePercentageAccepted = 0;// ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_FINETUNE_CURRENT_ALLOWED_PERCENTAGE;//0.25f;

			int fineTuneConfirmationMax = ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_FINETUNE_CURRENT_CONFIRMATION_COUNT;//3;

			int counter = 1;
			boolean fineTuneCurrentAchieved = false;
			ApplicationLauncher.logger.debug("waitForTargetCurrentFineTune: Entry"  );
			float phaseR_ActualCurrent = Float.parseFloat(getFeedbackR_phaseCurrent());
			float expected_pos_value = 0;
			float expected_neg_value = 0;

			float phaseR_TargetCurrent = Float.parseFloat(DeviceDataManagerController.getR_PhaseOutputCurrent());

			//expected_pos_value = phaseR_TargetCurrent + (phaseR_TargetCurrent*0.0025f);//0.2f;
			//expected_neg_value = phaseR_TargetCurrent - (phaseR_TargetCurrent*0.0025f);//0.4f;

			expected_pos_value = phaseR_TargetCurrent + (phaseR_TargetCurrent*(currentFineTunePercentageAccepted/100));//0.2f;
			expected_neg_value = phaseR_TargetCurrent - (phaseR_TargetCurrent*(currentFineTunePercentageAccepted/100));//0.4f;

			if(phaseR_TargetCurrent > 0.0f){

				int fineTuneConfirmationCounter = 0;

				while( (!getUserAbortedFlag()) && (counter <= waitTimeOutInSecMax) && (!fineTuneCurrentAchieved) ){

					phaseR_ActualCurrent = Float.parseFloat(getFeedbackR_phaseCurrent());
					ApplicationLauncher.logger.debug("waitForTargetCurrentFineTune: expected_pos_value : " +expected_pos_value );
					ApplicationLauncher.logger.debug("waitForTargetCurrentFineTune: expected_neg_value : " +expected_neg_value );
					ApplicationLauncher.logger.debug("waitForTargetCurrentFineTune: phaseR_ActualCurrent : " +phaseR_ActualCurrent );
					ApplicationLauncher.logger.debug("waitForTargetCurrentFineTune: retry: " +  counter + "/" + waitTimeOutInSecMax);
					ApplicationHomeController.update_left_status("FineTune: retry: " +  counter + "/" + waitTimeOutInSecMax,	ConstantApp.LEFT_STATUS_DEBUG);

					if ( (phaseR_ActualCurrent >= expected_neg_value) && (phaseR_ActualCurrent <= expected_pos_value)){
						if(fineTuneConfirmationCounter >= fineTuneConfirmationMax){
							fineTuneCurrentAchieved = true;
							setRefStdFeedBackControlSuspended(true);
							ApplicationLauncher.logger.debug("waitForTargetCurrentFineTune: finetune current achieved : " );

							ApplicationHomeController.update_left_status("FineTune: Achieved:",	ConstantApp.LEFT_STATUS_DEBUG);

						}else{
							fineTuneConfirmationCounter++;
							ApplicationLauncher.logger.debug("waitForTargetCurrentFineTune: fineTuneConfirmationCounter: " + fineTuneConfirmationCounter  );
						}
					}else{
						fineTuneConfirmationCounter = 0;
						ApplicationLauncher.logger.debug("waitForTargetCurrentFineTune: fineTuneConfirmationCounter - Reset"  );
					}
					Sleep(1000);
					counter++;
				}
			}else{
				ApplicationLauncher.logger.debug("waitForTargetCurrentFineTune: target current zero"  );
			}

		}

		class ShutDownAllCompPorts extends TimerTask{


			@Override
			public void run() {

				ApplicationLauncher.logger.debug("ShutDownAllCompPorts: setUI_TableRefreshFlag- 1: false");

				if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_ENABLED){
					setRefStdFeedBackControlFlagEnabled(false);
					setRefStdFeedBackControlSuspended(false);

				}
				setUI_TableRefreshFlag(false);
				DeviceDataManagerController.setLDU_ReadDataFlag(false);
				DeviceDataManagerController.setRefStdReadDataFlag(false);
				DeviceDataManagerController.setPowerSrcReadFeedbackData( false);



				//semLockExecutionInprogress = false;
				disableBtnLoadNext();
				if(ProcalFeatureEnable.PROPOWER_SRC_ONLY ){
					//LiveTableDataManager.UpdateliveTableData(1, "N",ConstantApp.EXECUTION_STATUS_COMPLETED);//
					//LiveTableDataManager.UpdateliveTableData(ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID, ConstantReport.RESULT_STATUS_UNDEFINED.trim(),ConstantApp.EXECUTION_STATUS_COMPLETED);
					deviceExecutionStatusDisplayUpdate(ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID, ConstantReport.RESULT_STATUS_UNDEFINED.trim(),ConstantApp.EXECUTION_STATUS_COMPLETED);
					
					Sleep(2);
				}
				if(!isManualModeExecution()){
					cancelProcessAllTestCasesTimer();
				}
				if(ConstantAppConfig.REF_STD_FEEDBACK_CONTROL_ENABLED){
					ApplicationLauncher.logger.info("ShutDownAllCompPorts :RefStd FB Control Await: Sleeping for 10 sec");
					ApplicationHomeController.update_left_status("RefStd FB Control: sleep 10 sec...",ConstantApp.LEFT_STATUS_DEBUG);
					if(ProcalFeatureEnable.SHUTDOWN_OPTIMISATION_ENABLED){
						Sleep(3000);
					}else {
						Sleep(10000);///reverting the optimisation on S4.2.0.7.0.5 on 08-Apr-2024
					}
					//Sleep(3000);
					ApplicationLauncher.logger.info("ShutDownAllCompPorts :RefStd FB Control Await: Sleeping awake after 10 sec");

				}
				ApplicationLauncher.logger.debug("ShutDownAllCompPorts: setUI_TableRefreshFlag- 2: false");
				setUI_TableRefreshFlag(false);
				//When user stops aborted

				ApplicationLauncher.logger.debug("ShutDownAllCompPorts: getExecuteStopStatus: " + getExecuteStopStatus());
				if (SerialDataManager.getPowerSrcTurnedOnStatus()|| SerialDataManager.getPowerSrcOnFlag() || 
						getPowerSrcContinuousFailureStatus()|| getRefStdFeedBackContinuousFailureStatus()||
						getExecuteStopStatus()){
					//SerialDM_Obj.SetPowerSourceOff();

					if(getExecuteStopStatus()){
						Integer StatusWaitTimeCounter = 40;
						ApplicationLauncher.logger.info("ShutDownAllCompPorts : ExecuteStop Await - "+StatusWaitTimeCounter+" Sec to complete");
						ApplicationHomeController.update_left_status("ExecuteStop Await - "+StatusWaitTimeCounter+" Sec",ConstantApp.LEFT_STATUS_DEBUG);
						while (StatusWaitTimeCounter !=0 && getExecuteStopStatus()){
							StatusWaitTimeCounter--;
							Sleep(1000);
							ApplicationLauncher.logger.info("ShutDownAllCompPorts : awaiting ExecuteStop StatusWaitTimeCounter:"+StatusWaitTimeCounter);
						}
						ApplicationLauncher.logger.info("ShutDownAllCompPorts :ExecuteStop Await: Sleeping for 10 sec");
						ApplicationHomeController.update_left_status("ExecuteStop Await: sleep 10 sec...",ConstantApp.LEFT_STATUS_DEBUG);

						if(ProcalFeatureEnable.SHUTDOWN_OPTIMISATION_ENABLED){
							Sleep(1000);
						}else {
							Sleep(10000);///reverting the optimisation on S4.2.0.7.0.5 on 08-Apr-2024
						}
						//Sleep(1000);
						ApplicationLauncher.logger.info("ShutDownAllCompPorts :ExecuteStop Await: Sleeping awake after 10 sec");
						ApplicationHomeController.update_left_status("ExecuteStop Await: sleep 10 sec awake",ConstantApp.LEFT_STATUS_DEBUG);
					}
					ApplicationLauncher.logger.info("ShutDownAllCompPorts :PwrSrcOFF_Trigger: Sleeping for 10 sec");
					ApplicationHomeController.update_left_status("PwrSrcOFF_Trigger: sleep 10 sec...",ConstantApp.LEFT_STATUS_DEBUG);

					if (ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){
						SerialDM_Obj.PwrSrcOFF_Trigger();

						Sleep(10000);
						ApplicationLauncher.logger.info("ShutDownAllCompPorts :PwrSrcOFF_Trigger: Sleeping awake after 10 sec");
						ApplicationHomeController.update_left_status("PwrSrcOFF_Trigger: sleep 10 sec awake",ConstantApp.LEFT_STATUS_DEBUG);

					}else if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
						/*					if(!ProcalFeatureEnable.CONVEYOR_FEATURE_ENABLED){
						setUserAbortedFlag(false);
					}*/
						if(ProcalFeatureEnable.SHUTDOWN_OPTIMISATION_ENABLED){

							Sleep(5000);
							ApplicationLauncher.logger.info("ShutDownAllCompPorts :PwrSrcOFF_Trigger2: Sleeping awake after 5 sec");
							ApplicationHomeController.update_left_status("PwrSrcOFF_Trigger: sleep 5 sec awake",ConstantApp.LEFT_STATUS_DEBUG);
						}
						ApplicationLauncher.logger.info("ShutDownAllCompPorts :PwrSrcOFF_Trigger: awaiting PowerSrcOff");
						ApplicationHomeController.update_left_status("PwrSrcOFF_Trigger: awaiting PowerSrcOff",ConstantApp.LEFT_STATUS_DEBUG);

						//boolean offStatus = true;
						boolean offStatus = false;
						int retryCountForStopProcess = 2;
						while ( (retryCountForStopProcess!=0) && (!offStatus) ){
							ApplicationLauncher.logger.info("ShutDownAllCompPorts :retryCountForStopProcess: " + retryCountForStopProcess);
							if(!SerialDataManager.isPowerSourceTurnedOff()){
								offStatus = SerialDM_Obj.SetPowerSourceOffV2();
							}else{
								offStatus = true;
							}
							if(offStatus) {
								SerialDataManager.setPowerSrcTurnedOnStatus(false);
								ApplicationLauncher.logger.info("ShutDownAllCompPorts : MCT/NCT mode reset initiated");
								ApplicationHomeController.update_left_status("MCT/NCT: mode Off",ConstantApp.LEFT_STATUS_DEBUG);
								//SerialDM_Obj.lscsSetPowerSourceMctNctMode(ConstantReport.RESULT_EXECUTION_MODE_MCT_NCT_OFF);
								String metertype = DeviceDataManagerController.getDeployedEM_ModelType();

								if(metertype.contains(ConstantApp.METERTYPE_SINGLEPHASE)){
									if(ProcalFeatureEnable.RACK_MCT_NCT_ENABLED){
										//ApplicationLauncher.logger.debug("ShutDownAllCompPorts : MCT/NCT reset Sleep 5000 delay");
										//if(ProcalFeatureEnable.SHUTDOWN_OPTIMISATION_ENABLED){
										//Sleep(3000);
										//}else {
										//Sleep(5000);///reverting the optimisation on S4.2.0.7.0.5 on 08-Apr-2024
										Sleep(1000);
										//}
										SerialDM_Obj.lscsSetPowerSourceMctNctMode(ConstantReport.RESULT_EXECUTION_MODE_MCT_NCT_OFF,true);
									}
								}else if(metertype.contains(ConstantApp.METERTYPE_THREEPHASE)){
									if(ProcalFeatureEnable.ICT_INTERFACE_ENABLED){
										SerialDM_Obj.lscsSetPowerSourceIctMode(false);
									}else if(ProcalFeatureEnable.ICT_KRE_KE6323_CONNECTED){
										SerialDM_Obj.lscsSetPowerSourceIctMode(false);
									}
								}
								if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
									ApplicationLauncher.logger.debug("ShutDownAllCompPorts : Sleep 1000 delay");
									Sleep(1000);
									ApplicationLauncher.logger.debug("ShutDownAllCompPorts : Sleep 1000 delay awake");
								}
								//setUserAbortedFlag(true);
								ApplicationLauncher.logger.info("ShutDownAllCompPorts : MCT/NCT mode reset done");
							}
							Sleep(1000);
							retryCountForStopProcess--;
						}
					}else if (ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
						ApplicationLauncher.logger.info("ShutDownAllCompPorts :Bofa PwrSrcOFF_Trigger: Sleeping awake after 5 sec");
						SerialDM_Obj.PwrSrcOFF_Trigger();
						//Sleep(5000);
						int retryCountForStopProcess = 5;
						boolean offStatus = false;
						while ( (retryCountForStopProcess!=0) && (!offStatus) ){
							ApplicationLauncher.logger.info("ShutDownAllCompPorts : bofa : retryCountForStopProcess: " + retryCountForStopProcess);
							if(!SerialDataManager.isPowerSourceTurnedOff()){
								//offStatus = SerialDM_Obj.SetPowerSourceOff();
							}else{
								offStatus = true;
							}
							if(offStatus) {
								SerialDataManager.setPowerSrcTurnedOnStatus(false);
							}
							Sleep(1000);
							retryCountForStopProcess--;
						}
						ApplicationHomeController.update_left_status("PwrSrcOFF_Trigger: sleep 5 sec awake",ConstantApp.LEFT_STATUS_DEBUG);

					}


				}
				if(getCurrentTestType().equals(TestProfileType.ConstantTest.toString())){

					ApplicationLauncher.logger.info("ShutDownAllCompPorts :Const Test buffer time: Sleeping for 30 sec");
					ApplicationHomeController.update_left_status("Const Buffer Time sleep 30 sec...",ConstantApp.LEFT_STATUS_DEBUG);
					if(ProcalFeatureEnable.SHUTDOWN_OPTIMISATION_ENABLED){
						Sleep(3000);
					}else {
						Sleep(30000);///reverting the optimisation on S4.2.0.7.0.5 on 08-Apr-2024
					}
					//Sleep(3000);
					ApplicationHomeController.update_left_status("Const Buffer Time sleep 30 sec awake",ConstantApp.LEFT_STATUS_DEBUG);

					ApplicationLauncher.logger.info("ShutDownAllCompPorts  :Const Test buffer time::Sleeping awake after 30 sec");
				}
				if(ProcalFeatureEnable.SHUTDOWN_OPTIMISATION_ENABLED){
					Sleep(1000);
				}else {
					Sleep(5000);///reverting the optimisation on S4.2.0.7.0.5 on 08-Apr-2024
				}
				//Sleep(1000);

				if(ProcalFeatureEnable.LSCS_LDU_CONNECTED) {
					if(getCurrentTestType().equals(TestProfileType.ConstantTest.toString())){
						SerialDM_Obj.lscsLDU_SendStopStaCreepConstant();
					}else if(getCurrentTestType().equals(TestProfileType.STA.toString())){
						SerialDM_Obj.lscsLDU_SendStopStaCreepConstant();

					}else if(getCurrentTestType().equals(TestProfileType.NoLoad.toString())){
						SerialDM_Obj.lscsLDU_SendStopStaCreepConstant();

					}
				}
				DisplayDataObj.setVoltageResetRequired(true);
				setExecuteTimeCounter(0);
				//Sleep(15000);

				if(ProcalFeatureEnable.CONVEYOR_FEATURE_ENABLED){
					if(getUserAbortedFlag()){
						ApplicationLauncher.logger.debug("ShutDownAllCompPorts  :Stop Conveyor");
						SerialDM_Obj.sendDataToConveyor(ConstantConveyor.CMD_STOP_CONVEYOR,"");
					}else {
						ApplicationLauncher.logger.debug("ShutDownAllCompPorts  :CMD_TEST_COMPLETED Conveyor");
						SerialDM_Obj.sendDataToConveyor(ConstantConveyor.CMD_TEST_COMPLETED,"");
					}
				}
				if(isCurrentProjectAutoDeployEnabled()) {
					SerialDM_Obj.DisConnectHarmonicsSrc();
				}
				//setUserAbortedFlag(false);

				Sleep(1000);

				CloseAllComPort();
				if(!isManualModeExecution()){
					CancelExecuteTimeLine();
				}
				if(ProcalFeatureEnable.DISPLAY_3PHASE_INSTANT_METRICS){
					DisplayInstantMetricsController.ResetInstantMetrics();
					resetInstantMetrics();
				}
				ApplicationHomeController.update_left_status("ShutDownAllCompPorts: sleep 30 sec",ConstantApp.LEFT_STATUS_DEBUG);
				ApplicationLauncher.logger.info("ShutDownAllCompPorts :Sleeping for CoolOffTimeInMSec:"+CoolOffTimeInMSec);
				
				Sleep(CoolOffTimeInMSec);			
				ApplicationHomeController.update_left_status("ShutDownAllCompPorts: sleep 30 sec awake",ConstantApp.LEFT_STATUS_DEBUG);
				ApplicationLauncher.logger.info("ShutDownAllCompPorts :Sleeping awake after CoolOffTimeInMSec:"+CoolOffTimeInMSec);
				ShutDownAllComPortTimer.cancel();
				try{
					EnableTestRunScreenButton();
					if(ProcalFeatureEnable.PROPOWER_SRC_ONLY ){
						if(isManualModeExecution()){
							enableBtnManualModeStart();
							enableTabSequenceMode();
							enableManualModeScreenButton();

						}else{
							enableTabManualMode();

						}
						if(ref_chBxManualTimerMode.isSelected()){
							ApplicationLauncher.logger.info("ShutDownAllCompPorts : Manual-Timer mode: Manual timer mode completed - prompted");
							ApplicationLauncher.InformUser("Manual-Timer mode","Manual timer mode completed!",AlertType.INFORMATION);
						}
					}
				}catch(Exception e){
					e.printStackTrace();
					ApplicationLauncher.logger.error("ShutDownAllCompPorts : Exception:"+e.getMessage());
				}
				ApplicationHomeController.update_left_status("Live status",ConstantApp.LEFT_STATUS_DEBUG);
				setExecutionInProgress(false);
				setManualModeExecution(false);
				setExecuteStopStatus(false);
				String presentTestPointName = getCurrentTestPointName();
				ApplicationLauncher.logger.info("ShutDownAllCompPorts : presentTestPointName: " + presentTestPointName);

				//if(presentTestPointName.contains(ConstantApp.HARMONIC_INPHASE_ALIAS_NAME)){
				if(DeviceDataManagerController.isPresentTestPointContainsHarmonics()){		
					ApplicationLauncher.logger.debug("ShutDownAllCompPorts : Harmonics Test1" );
					if(isLastExecutedTestPointContainedHarmonics_Master()){
						setSecondthExecutionTestPointWithBreakWithHarmonics(true);
						ApplicationLauncher.logger.debug("ShutDownAllCompPorts : Harmonics Test2" );
					}else{
						setSecondthExecutionTestPointWithBreakWithHarmonics(false);
						ApplicationLauncher.logger.debug("ShutDownAllCompPorts : Harmonics Test3" );
					}
				}else{
					setLastExecutedTestPointContainedHarmonics_Slave(false);
					setLastExecutedTestPointContainedHarmonics_Master(false);
					setSecondthExecutionTestPointWithBreakWithHarmonics(false);
					//ApplicationLauncher.logger.debug("ShutDownAllCompPorts : Harmonics Test4" );
				}

				setFirstExecutionTestPointWithHarmonics(false);
				setOneTimeExecuted(false);
				DeviceDataManagerController.resetLastSetPowerSourceData();
				DeviceDataManagerController.resetLastSetPowerSourceFrequency();// added on version #s4.2.1.2.1.4 for customtest issue on PowerSource only
				
/*				if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
					try{
						BofaManager.comPortSemaphore.drainPermits();
					}catch (Exception e){
						ApplicationLauncher.logger.error("ShutDownAllCompPorts : Semaphore Exception: " + e.getMessage());
					}
					
				}*/
				/*if(getAll_TestPoint_CompletedStatus()){
					ApplicationLauncher.InformUser("Execution Completed","Testing Completed!",AlertType.INFORMATION);
				}else{
					ApplicationLauncher.InformUser("Execution Stopped","Testing Stopped!",AlertType.INFORMATION);
				}*/
				setAll_TestPoint_CompletedStatus(false);
				
				if (getPresentTestPointStatus().isAllTestExecutionCompletedCheck()) {
					getPresentTestPointStatus().setAllTestExecutionCompleted(true);
				}
				
				if(ProcalFeatureEnable.CONVEYOR_CALIB_BOFA_FEATURE_ENABLED) {
					setDutCalibrationVoltCurrentSetZero(true);
				}
				if(ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED) {
					BofaRequestProcessor.stopServerProcessor(ConstantPowerSourceBofa.BOFA_COM_PORT_KEY);
				}
				ApplicationLauncher.logger.info("ShutDownAllCompPorts : Exit:");
			}

		}

		public static void resetInstantMetrics(){
			Communicator SerialPortObj = null;
			SerialDataRadiantRefStd Reset_Value = new SerialDataRadiantRefStd(SerialPortObj);
			Reset_Value.VoltageDisplayData = "0";
			Reset_Value.CurrentDisplayData = "0";
			Reset_Value.PowerFactorData = "0";
			Reset_Value.FreqDisplayData = "0";
			Reset_Value.WattDisplayData = "0";
			Reset_Value.VA_DisplayData = "0";
			Reset_Value.VAR_DisplayData = "0";
			Reset_Value.DegreePhaseData = "0";
			refStdDataDisplayUpdate_R_Phase(Reset_Value);
			Platform.runLater(() -> {
				refStdDisplayR_PhaseWh.setValue("");
			});
		}

		public static void refStdDataDisplayUpdateVoltageData(final SerialDataMteRefStd voltageData){

			//ApplicationLauncher.logger.info("refStdDataDisplayUpdateVoltageData : Entry");
			try{
				Platform.runLater(() -> {
					refStdDisplayR_PhaseVolt.setValue(voltageData.rPhaseVoltageDisplayData);

				});

				if(!voltageData.rPhaseVoltageDisplayData.isEmpty()){
					setFeedbackR_phaseVolt(voltageData.rPhaseVoltageDisplayData);
				}
			}catch(Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("refStdDataDisplayUpdateVoltageData :Exception:"+ e.getMessage());
			}

		}


		public static void refStdDataDisplayUpdateVoltageData(final SerialDataRefStdKiggs voltageData){

			//ApplicationLauncher.logger.info("refStdDataDisplayUpdateVoltageData : Entry");
			try{
				Platform.runLater(() -> {
					refStdDisplayR_PhaseVolt.setValue(voltageData.rPhaseVoltageDisplayData);

				});

				if(!voltageData.rPhaseVoltageDisplayData.isEmpty()){
					setFeedbackR_phaseVolt(voltageData.rPhaseVoltageDisplayData);
				}
			}catch(Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("refStdDataDisplayUpdateVoltageData :Exception:"+ e.getMessage());
			}

		}

		public static void refStdDataDisplayUpdateVoltageData(final SerialDataRefStdKre voltageData){

			//ApplicationLauncher.logger.info("refStdDataDisplayUpdateVoltageData : Entry");
			try{
				Platform.runLater(() -> {
					refStdDisplayR_PhaseVolt.setValue(voltageData.rPhaseVoltageDisplayData);

				});

				if(!voltageData.rPhaseVoltageDisplayData.isEmpty()){
					setFeedbackR_phaseVolt(voltageData.rPhaseVoltageDisplayData);
				}
			}catch(Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("refStdDataDisplayUpdateVoltageData :Exception:"+ e.getMessage());
			}

		}
		public static void refStdDataDisplayUpdateCurrentData(final SerialDataMteRefStd currentData){

			//ApplicationLauncher.logger.info("refStdDataDisplayUpdateVoltageData : Entry");
			try{
				Platform.runLater(() -> {
					refStdDisplayR_PhaseCurrent.setValue(currentData.rPhaseCurrentDisplayData);

				});

				if(!currentData.rPhaseCurrentDisplayData.isEmpty()){
					setFeedbackR_phaseCurrent(currentData.rPhaseCurrentDisplayData);
				}
			}catch(Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("refStdDataDisplayUpdateCurrentData :Exception:"+ e.getMessage());
			}

		}

		public static void refStdDataDisplayUpdateCurrentData(final SerialDataRefStdKre currentData){

			//ApplicationLauncher.logger.info("refStdDataDisplayUpdateVoltageData : Entry");
			try{
				Platform.runLater(() -> {
					refStdDisplayR_PhaseCurrent.setValue(currentData.rPhaseCurrentDisplayData);

				});

				if(!currentData.rPhaseCurrentDisplayData.isEmpty()){
					setFeedbackR_phaseCurrent(currentData.rPhaseCurrentDisplayData);
				}
			}catch(Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("refStdDataDisplayUpdateCurrentData :Exception:"+ e.getMessage());
			}

		}

		public static void refStdDataDisplayUpdateWattData(final SerialDataMteRefStd wattData){

			ApplicationLauncher.logger.info("refStdDataDisplayUpdateWattData : Entry");
			try{
				Platform.runLater(() -> {
					refStdDisplayR_PhaseWatt.setValue(wattData.rPhaseWattDisplayData);

				});

				/*    		if(!wattData.rPhaseWattDisplayData.isEmpty()){
    			setFeedbackR_phaseWatt(wattData.rPhaseWattDisplayData);
    		}*/
			}catch(Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("refStdDataDisplayUpdateWattData :Exception:"+ e.getMessage());
			}

		}
		public static void refStdDataDisplayUpdateWattData(final SerialDataRefStdKre wattData){

			ApplicationLauncher.logger.info("refStdDataDisplayUpdateWattData : Entry");
			try{
				Platform.runLater(() -> {
					refStdDisplayR_PhaseWatt.setValue(wattData.rPhaseActivePowerDisplayData);

				});

				if(!wattData.rPhaseActivePowerDisplayData.isEmpty()){
					setFeedbackR_activePower(wattData.rPhaseActivePowerDisplayData);
				}
			}catch(Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("refStdDataDisplayUpdateWattData :Exception:"+ e.getMessage());
			}

		}	
		public static void refStdDataDisplayUpdatePowerFactorData(final SerialDataMteRefStd pfData){

			ApplicationLauncher.logger.info("refStdDataDisplayUpdateWattData : Entry");
			try{
				Platform.runLater(() -> {
					refStdDisplayR_PhasePowerFactor.setValue(pfData.rPhasePowerFactorData);

				});

				/*    		if(!wattData.rPhaseWattDisplayData.isEmpty()){
    			setFeedbackR_phaseWatt(wattData.rPhaseWattDisplayData);
    		}*/
			}catch(Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("refStdDataDisplayUpdatePowerFactorData :Exception:"+ e.getMessage());
			}

		}

		public static void refStdDataDisplayUpdatePowerFactorData(final SerialDataRefStdKre pfData){

			ApplicationLauncher.logger.info("refStdDataDisplayUpdateWattData : Entry");
			try{
				Platform.runLater(() -> {
					refStdDisplayR_PhasePowerFactor.setValue(pfData.rPhasePowerFactorData);

				});

				  if(!pfData.rPhasePowerFactorData.isEmpty()){
					  setFeedbackR_powerFactor(pfData.rPhasePowerFactorData);
				}
			}catch(Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("refStdDataDisplayUpdatePowerFactorData :Exception:"+ e.getMessage());
			}

		}
		public static void refStdDataDisplayUpdateDegreePhaseData(final SerialDataMteRefStd degreePhaseData){

			//ApplicationLauncher.logger.info("refStdDataDisplayUpdateVoltageData : Entry");
			try{
				Platform.runLater(() -> {
					refStdDisplayR_PhaseDegreePhase.setValue(degreePhaseData.rPhaseDegreePhaseData);

				});

				if(!degreePhaseData.rPhaseDegreePhaseData.isEmpty()){
					setFeedbackR_phaseDegree(degreePhaseData.rPhaseDegreePhaseData);
				}
			}catch(Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("refStdDataDisplayUpdateCurrentData :Exception:"+ e.getMessage());
			}

		}

		public static void refStdDataDisplayUpdateDegreePhaseData(final SerialDataRefStdKre degreePhaseData){

			//ApplicationLauncher.logger.info("refStdDataDisplayUpdateVoltageData : Entry");
			try{
				Platform.runLater(() -> {
					refStdDisplayR_PhaseDegreePhase.setValue(degreePhaseData.rPhaseDegreePhaseData);

				});

				if(!degreePhaseData.rPhaseDegreePhaseData.isEmpty()){
					setFeedbackR_phaseDegree(degreePhaseData.rPhaseDegreePhaseData);
				}
			}catch(Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("refStdDataDisplayUpdateCurrentData :Exception:"+ e.getMessage());
			}

		}	
		public static void refStdDataDisplayUpdateFreqData(final SerialDataMteRefStd freqData){

			//ApplicationLauncher.logger.info("refStdDataDisplayUpdateFreqData : Entry");
			try{
				Platform.runLater(() -> {
					refStdDisplayR_PhaseFreq.setValue(freqData.rPhaseFreqDisplayData);

				});

				if(!freqData.rPhaseFreqDisplayData.isEmpty()){
					setFeedbackR_Frequency(freqData.rPhaseFreqDisplayData);
				}
			}catch(Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("refStdDataDisplayUpdateFreqData :Exception:"+ e.getMessage());
			}

		}

		public static void refStdDataDisplayUpdateFreqData(final SerialDataRefStdKre freqData){

			//ApplicationLauncher.logger.info("refStdDataDisplayUpdateFreqData : Entry");
			try{
				Platform.runLater(() -> {
					refStdDisplayR_PhaseFreq.setValue(freqData.rPhaseFreqDisplayData);

				});

				if(!freqData.rPhaseFreqDisplayData.isEmpty()){
					setFeedbackR_Frequency(freqData.rPhaseFreqDisplayData);
				}
			}catch(Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("refStdDataDisplayUpdateFreqData :Exception:"+ e.getMessage());
			}

		}
		public static void refStdDataDisplayUpdate_R_Phase(final SerialDataRadiantRefStd R_PhaseData){

			ApplicationLauncher.logger.info("refStdDataDisplayUpdate_R_Phase : Entry");
			try{
				Platform.runLater(() -> {
					refStdDisplayR_PhaseVolt.setValue(R_PhaseData.VoltageDisplayData);
					refStdDisplayR_PhaseCurrent.setValue(R_PhaseData.CurrentDisplayData);
					refStdDisplayR_PhasePowerFactor.setValue(R_PhaseData.PowerFactorData);
					refStdDisplayR_PhaseFreq.setValue(R_PhaseData.FreqDisplayData);
					refStdDisplayR_PhaseWatt.setValue(R_PhaseData.WattDisplayData);
					refStdDisplayR_PhaseVA.setValue(R_PhaseData.VA_DisplayData);
					refStdDisplayR_PhaseVAR.setValue(R_PhaseData.VAR_DisplayData);
					refStdDisplayR_PhaseDegreePhase.setValue(R_PhaseData.DegreePhaseData);
				});

				if(!R_PhaseData.VoltageDisplayData.isEmpty()){
					setFeedbackR_phaseVolt(R_PhaseData.VoltageDisplayData);
				}
				if(!R_PhaseData.CurrentDisplayData.isEmpty()){
					setFeedbackR_phaseCurrent(R_PhaseData.CurrentDisplayData);
				}
				if(!R_PhaseData.DegreePhaseData.isEmpty()){
					setFeedbackR_phaseDegree(R_PhaseData.DegreePhaseData);
				}
				if(!R_PhaseData.FreqDisplayData.isEmpty()){
					setFeedbackR_Frequency(R_PhaseData.FreqDisplayData);
				}
			}catch(Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdate_R_Phase :Exception:"+ e.getMessage());
			}

		}
		
		
		
		
		
		public static void refStdDataDisplayUpdate_R_Phase(final Data_PowerSourceFeedBack R_PhaseData){

			ApplicationLauncher.logger.info("refStdDataDisplayUpdate_R_Phase : Entry");
			try{
				Platform.runLater(() -> {
					refStdDisplayR_PhaseVolt.setValue(R_PhaseData.getR_PhaseVoltageDisplayData());
					refStdDisplayR_PhaseCurrent.setValue(R_PhaseData.getR_PhaseCurrentDisplayData());
					refStdDisplayR_PhasePowerFactor.setValue(R_PhaseData.getR_PhasePowerFactorData());
					refStdDisplayR_PhaseFreq.setValue(R_PhaseData.getFreqData());
					/*refStdDisplayR_PhaseWatt.setValue(R_PhaseData.WattDisplayData);
					refStdDisplayR_PhaseVA.setValue(R_PhaseData.VA_DisplayData);
					refStdDisplayR_PhaseVAR.setValue(R_PhaseData.VAR_DisplayData);*/
					refStdDisplayR_PhaseDegreePhase.setValue(R_PhaseData.getR_PhaseDegreePhaseData());
				});

				if(!R_PhaseData.getR_PhaseVoltageDisplayData().isEmpty()){
					setFeedbackR_phaseVolt(R_PhaseData.getR_PhaseVoltageDisplayData());
				}
				if(!R_PhaseData.getR_PhaseCurrentDisplayData().isEmpty()){
					setFeedbackR_phaseCurrent(R_PhaseData.getR_PhaseCurrentDisplayData());
				}
				if(!R_PhaseData.getR_PhaseDegreePhaseData().isEmpty()){
					setFeedbackR_phaseDegree(R_PhaseData.getR_PhaseDegreePhaseData());
				}
				if(!R_PhaseData.getFreqData().isEmpty()){
					setFeedbackR_Frequency(R_PhaseData.getFreqData());
				}
			}catch(Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdate_R_Phase : Data_PowerSourceFeedBack  :Exception:"+ e.getMessage());
			}

		}
		
		
		public static void refStdDataDisplayUpdate_R_Phase(final SerialDataBofaRefStd R_PhaseData){

			//ApplicationLauncher.logger.info("refStdDataDisplayUpdate_R_Phase : Entry");
			try{
				Platform.runLater(() -> {
					refStdDisplayR_PhaseVolt.setValue(R_PhaseData.getVoltageDisplayData());
					refStdDisplayR_PhaseCurrent.setValue(R_PhaseData.getCurrentDisplayData());
					refStdDisplayR_PhasePowerFactor.setValue(R_PhaseData.getPowerFactorData());
					refStdDisplayR_PhaseFreq.setValue(R_PhaseData.getFreqDisplayData());
					refStdDisplayR_PhaseWatt.setValue(R_PhaseData.getWattDisplayData());
					refStdDisplayR_PhaseVA.setValue(R_PhaseData.getVA_DisplayData());
					refStdDisplayR_PhaseVAR.setValue(R_PhaseData.getVAR_DisplayData());
					refStdDisplayR_PhaseDegreePhase.setValue(R_PhaseData.getDegreePhaseData());
					refStdDisplayR_PhaseWh.setValue(R_PhaseData.getActiveEnergyInKwhDisplayData());
					
				});

				if(!R_PhaseData.getVoltageDisplayData().isEmpty()){
					setFeedbackR_phaseVolt(R_PhaseData.getVoltageDisplayData());
				}
				if(!R_PhaseData.getCurrentDisplayData().isEmpty()){
					setFeedbackR_phaseCurrent(R_PhaseData.getCurrentDisplayData());
				}
				if(!R_PhaseData.getDegreePhaseData().isEmpty()){
					setFeedbackR_phaseDegree(R_PhaseData.getDegreePhaseData());
				}
				if(!R_PhaseData.getFreqDisplayData().isEmpty()){
					setFeedbackR_Frequency(R_PhaseData.getFreqDisplayData());
				}
			}catch(Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdate_R_Phase Bofa :Exception:"+ e.getMessage());
			}

		}

		/*    public static void refStdDataDisplayUpdate_R_Phase(final SerialDataSandsRefStd R_PhaseData){

    	ApplicationLauncher.logger.info("refStdDataDisplayUpdate_R_Phase1 : Entry");
    	try{
    		Platform.runLater(() -> {
    			refStdDisplayR_PhaseVolt.setValue(R_PhaseData.VoltageDisplayData);
    			refStdDisplayR_PhaseCurrent.setValue(R_PhaseData.CurrentDisplayData);
    			refStdDisplayR_PhasePowerFactor.setValue(R_PhaseData.PowerFactorData);
    			refStdDisplayR_PhaseFreq.setValue(R_PhaseData.FreqDisplayData);
    			refStdDisplayR_PhaseWatt.setValue(R_PhaseData.WattDisplayData);
    			refStdDisplayR_PhaseVA.setValue(R_PhaseData.VA_DisplayData);
    			refStdDisplayR_PhaseVAR.setValue(R_PhaseData.VAR_DisplayData);
    			//refStdDisplayR_PhaseDegreePhase.setValue(R_PhaseData.DegreePhaseData);
    		});

    		if(!R_PhaseData.VoltageDisplayData.isEmpty()){
    			setFeedbackR_phaseVolt(R_PhaseData.VoltageDisplayData);
    		}
    		if(!R_PhaseData.CurrentDisplayData.isEmpty()){
    			setFeedbackR_phaseCurrent(R_PhaseData.CurrentDisplayData);
    		}
    		if(!R_PhaseData.DegreePhaseData.isEmpty()){
    			setFeedbackR_phaseDegree(R_PhaseData.DegreePhaseData);
    		}
    		if(!R_PhaseData.FreqDisplayData.isEmpty()){
    			setFeedbackR_Frequency(R_PhaseData.FreqDisplayData);
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdate_R_Phase1 :Exception:"+ e.getMessage());
    	}

	}*/


		/*    public static void refStdDataDisplayUpdate_Y_Phase(final SerialDataSandsRefStd Y_PhaseData){

    	ApplicationLauncher.logger.info("refStdDataDisplayUpdate_Y_Phase1 : Entry");
    	try{
    		Platform.runLater(() -> {
    			refStdDisplayY_PhaseVolt.setValue(Y_PhaseData.VoltageDisplayData);
    			refStdDisplayY_PhaseCurrent.setValue(Y_PhaseData.CurrentDisplayData);
    			refStdDisplayY_PhasePowerFactor.setValue(Y_PhaseData.PowerFactorData);
    			refStdDisplayY_PhaseFreq.setValue(Y_PhaseData.FreqDisplayData);
    			refStdDisplayY_PhaseWatt.setValue(Y_PhaseData.WattDisplayData);
    			refStdDisplayY_PhaseVA.setValue(Y_PhaseData.VA_DisplayData);
    			refStdDisplayY_PhaseVAR.setValue(Y_PhaseData.VAR_DisplayData);
    			//refStdDisplayY_PhaseDegreePhase.setValue(Y_PhaseData.DegreePhaseData);
    		});

    		if(!Y_PhaseData.VoltageDisplayData.isEmpty()){
    			setFeedbackY_phaseVolt(Y_PhaseData.VoltageDisplayData);
    		}
    		if(!Y_PhaseData.CurrentDisplayData.isEmpty()){
    			setFeedbackY_phaseCurrent(Y_PhaseData.CurrentDisplayData);
    		}
    		if(!Y_PhaseData.DegreePhaseData.isEmpty()){
    			setFeedbackY_phaseDegree(Y_PhaseData.DegreePhaseData);
    		}
    		if(!Y_PhaseData.FreqDisplayData.isEmpty()){
    			setFeedbackY_Frequency(Y_PhaseData.FreqDisplayData);
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdate_Y_Phase1 :Exception:"+ e.getMessage());
    	}

	}*/


		/*    public static void refStdDataDisplayUpdate_B_Phase(final SerialDataSandsRefStd B_PhaseData){

    	ApplicationLauncher.logger.info("refStdDataDisplayUpdate_B_Phase1 : Entry");
    	try{
    		Platform.runLater(() -> {
    			refStdDisplayB_PhaseVolt.setValue(B_PhaseData.VoltageDisplayData);
    			refStdDisplayB_PhaseCurrent.setValue(B_PhaseData.CurrentDisplayData);
    			refStdDisplayB_PhasePowerFactor.setValue(B_PhaseData.PowerFactorData);
    			refStdDisplayB_PhaseFreq.setValue(B_PhaseData.FreqDisplayData);
    			refStdDisplayB_PhaseWatt.setValue(B_PhaseData.WattDisplayData);
    			refStdDisplayB_PhaseVA.setValue(B_PhaseData.VA_DisplayData);
    			refStdDisplayB_PhaseVAR.setValue(B_PhaseData.VAR_DisplayData);
    			//refStdDisplayB_PhaseDegreePhase.setValue(B_PhaseData.DegreePhaseData);
    		});

    		if(!B_PhaseData.VoltageDisplayData.isEmpty()){
    			setFeedbackB_phaseVolt(B_PhaseData.VoltageDisplayData);
    		}
    		if(!B_PhaseData.CurrentDisplayData.isEmpty()){
    			setFeedbackB_phaseCurrent(B_PhaseData.CurrentDisplayData);
    		}
    		if(!B_PhaseData.DegreePhaseData.isEmpty()){
    			setFeedbackB_phaseDegree(B_PhaseData.DegreePhaseData);
    		}
    		if(!B_PhaseData.FreqDisplayData.isEmpty()){
    			setFeedbackB_Frequency(B_PhaseData.FreqDisplayData);
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdate_B_Phase1 :Exception:"+ e.getMessage());
    	}

	}*/


		public void EnableTestRunScreenButton(){

			Platform.runLater(() -> {
				EnableStartButton();
				EnableViewLogsButton();

				EnableStepRunButton();
				EnableResumeButton();

			});
			EnableCloseProjectButton();
			enableSelectProjectComboBox();
			enableMainCtModeRadioButton();
			enableNeutralCtModeRadioButton();
		}

		class ExecuteStopTask extends TimerTask{


			@Override
			public void run() {
				exitCurrentTestPointExecution();
				ExecuteStopTimer.cancel();

			}

		}



		public String getCurrentHighlightedTestCase(){
			int index = ref_tbl_liveStatus.getSelectionModel().getSelectedIndex();
			if(index != -1){
				List<Object> current_test = ref_tbl_liveStatus.getItems().get(index);
				String current_testcase = (String) current_test.get(1);
				return current_testcase;
			}else{
				ApplicationLauncher.logger.info("getCurrentHighlightedTestCase: index:"+index);
				return null;
			}
		}


		public void ViewLogsTrigger() {
			ApplicationLauncher.logger.info("ViewLogsTrigger: Invoked:");

			ViewLogsTimer = new Timer();
			ViewLogsTimer.schedule(new ViewLogsTask(),100);// 1000);

		}

		public void ViewLogsDataFetchFromDB(){
			if(btn_Stop.isDisabled()){
				long fromtime = getProjectStartTime();
				long totime = 0;
				try {
					totime = getCurrentEpoch();
				} catch (ParseException e) {
					
					e.printStackTrace();
					ApplicationLauncher.logger.error("ViewLogsDataFetchFromDB : ParseException:"+e.getMessage());
				}
				String project_name = getCurrentProjectName();//MeterParamsController.getCurrentProjectName();
				String CurrentTP_Name = getCurrentHighlightedTestCase();
				if (CurrentTP_Name != null){
					JSONObject TestPointData = MySQL_Controller.sp_getresult_testpoint_data(fromtime, totime, project_name,CurrentTP_Name,ConstantReport.RESULT_DATA_TYPE_FAILURE_REASON);
					try {
						JSONArray TestPointArr = TestPointData.getJSONArray("Results");
						if(TestPointArr.length() != 0){
							JSONObject jobj = TestPointArr.getJSONObject(0);
							String failure_reason = jobj.getString("failure_reason");
							AssertViewLogsOnClick(failure_reason);


						}
						else{
							ApplicationLauncher.InformUser("No Logs","Failure logs not found",AlertType.INFORMATION);
						}
					} catch (JSONException e) {
						
						e.printStackTrace();
						ApplicationLauncher.logger.error("ViewLogsDataFetchFromDB : JSONException:"+e.getMessage());
					}
				}else{
					ApplicationLauncher.logger.info("ViewLogsDataFetchFromDB: Test point not selected. Prompted user to select the test point");
					ApplicationLauncher.InformUser("Test Point not selected","Kindly select test point to publish the logs",AlertType.INFORMATION);
				}
			}    	
		}

		/*	public void InformUser(String title, String info,AlertType Alert_type){
		TextBoxDialog TextBoxDialogobj = new TextBoxDialog();
		TextBoxDialogobj.TriggerUserInfoPlatFormLater(title, info,Alert_type);
	}*/

		public static  void setUserAbortedFlag( boolean value){
			UserAbortedFlag = value;
		}

		public static boolean getUserAbortedFlag(){
			return UserAbortedFlag;
		}



		public void AssertViewLogsOnClick(String failure_reason){

			try {	
				if(!failure_reason.equals("")){
					String[] items = failure_reason.split(",");
					ApplicationLauncher.logger.info("error_log:"+(Arrays.toString(items)));

					int number_of_error_log = items.length;
					ApplicationLauncher.logger.info("number_of_error_log:"+number_of_error_log);

					String[] real_items =new String[number_of_error_log];;
					System.arraycopy(items, 0, real_items, 0, number_of_error_log);
					ApplicationLauncher.logger.info("real_items:error_log:"+(Arrays.toString(real_items)));


					int j=0;
					String[] expected_items = new String[number_of_error_log];

					for(int i=0;i<number_of_error_log;i++){
						if(items[i].contains("Expected")){
							expected_items[j] =items[i];
							expected_items[j] = expected_items[j].substring(0, expected_items[j].indexOf(":"));
							items[i]=expected_items[j];
							j++;

						}

					}
					ApplicationLauncher.logger.info("expected_items:"+(Arrays.toString(expected_items)));
					ApplicationLauncher.logger.info("error_log:"+(Arrays.toString(items)));
					new ErrorCodeMapping();
					JSONObject error_code_json=new JSONObject();
					error_code_json=ErrorCodeMapping.getERROR_CODE_MSG();
					String skip_reasons = null;
					StringBuilder stringBuilder = new StringBuilder();
					String skip_reason1 = null;
					int length_of_items=0;
					for(int i=0;i<number_of_error_log;i++){
						length_of_items=items[i].length();
						if(!(length_of_items<=8)){//.equals("")||equals(" ")||equals("   ")
							ApplicationLauncher.logger.info("Failure Reason if case length items[i]"+items[i].length());
							items[i]=items[i].replace(" ", "");
							ApplicationLauncher.logger.info("Failure Reason if case items[i] " + items[i]);
							skip_reasons = error_code_json.getString(items[i]);//.getJSONObject(items[i]);//.getString("slogan");

							skip_reasons = skip_reasons.replace("[", "");
							skip_reasons = skip_reasons.replace("]", "");
							stringBuilder.append(real_items[i]);

							stringBuilder.append(": ");
							stringBuilder.append(skip_reasons);
							stringBuilder.append("\n");

						}
						else{
							skip_reasons = null;
							ApplicationLauncher.logger.info("Failure Reason else case  items[i] " +Arrays.toString(items));
							ApplicationLauncher.logger.info("Failure Reason else case length items[i]"+items[i].length());
						}
					}
					skip_reason1 = stringBuilder.toString();
					ApplicationLauncher.InformUser("Failure Reason",skip_reason1,AlertType.INFORMATION);
				}
				else{
					ApplicationLauncher.InformUser("No Logs"," No Logs Found",AlertType.INFORMATION);
				}

			}
			catch(JSONException e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("AssertViewLogsOnClick: JSONException: " + e.toString());
				ApplicationLauncher.InformUser("Failure Reason",failure_reason,AlertType.INFORMATION);


			}

		}

		public void ZeroCurrentPowerOn(){
			ApplicationLauncher.logger.debug("ZeroCurrentPowerOn  Entry"  );

			DeviceDataManagerController.setR_PhaseOutputVoltage(Float.parseFloat(DeviceDataManagerController.getR_PhaseOutputVoltage()));
			DeviceDataManagerController.setR_PhaseOutputCurrent(0F);
			DeviceDataManagerController.setY_PhaseOutputVoltage(Float.parseFloat(DeviceDataManagerController.getY_PhaseOutputVoltage()));
			DeviceDataManagerController.setY_PhaseOutputCurrent(0F);
			DeviceDataManagerController.setB_PhaseOutputVoltage(Float.parseFloat(DeviceDataManagerController.getB_PhaseOutputVoltage()));
			DeviceDataManagerController.setB_PhaseOutputCurrent(0F);
			if(ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED) {
				DeviceDataManagerController.manipulateTargetRmsValue();
			}
			//setZeroCurrentPowerTurnedOnRequested(true);
			DisplayPwrSrc_TurnOn();
			ApplicationLauncher.logger.debug("ZeroCurrentPowerOn: Power On");

		}

		public void STA_RatedCurrentPowerOn(){

			ApplicationLauncher.logger.info("STA_RatedCurrentPowerOn  Started"  );
			float STACurrent = DisplayDataObj.CalculateSTACurrent();
			DeviceDataManagerController.setR_PhaseOutputCurrent(STACurrent);
			DeviceDataManagerController.setY_PhaseOutputCurrent(STACurrent);
			DisplayDataObj.manipulateY_PhaseCurrentFor3PhaseDelta();
			DeviceDataManagerController.setB_PhaseOutputCurrent(STACurrent);
			DeviceDataManagerController.setPowerSrcOnTimerValue(300);
			DisplayDataObj.setVoltageResetRequired(false);
			if(ProcalFeatureEnable.LSCS_APP_CONTROL_MODE_ENABLED) {
				DeviceDataManagerController.manipulateTargetRmsValue();
			}
			DisplayPwrSrc_TurnOn();
			ApplicationLauncher.logger.info("runRatedCurrent:Power On");

		}



		public boolean stop_confirmation(){

			Alert alert = new Alert(AlertType.CONFIRMATION);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
			alert.setTitle(ConstantVersion.APPLICATION_NAME +" Exit");
			alert.setHeaderText("Confirmation");
			alert.setContentText("Are you sure, you want to stop?");

			alert.getButtonTypes().clear();
			alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

			//Deactivate Defaultbehavior for yes-Button:
			Button yesButton = (Button) alert.getDialogPane().lookupButton( ButtonType.YES );
			yesButton.setDefaultButton( false );

			//Activate Defaultbehavior for no-Button:
			Button noButton = (Button) alert.getDialogPane().lookupButton( ButtonType.NO );
			noButton.setDefaultButton( true );

			final Optional<ButtonType> result = alert.showAndWait();
			return result.get() == ButtonType.YES;
		}

		public void Trigger_Stop_Confirmation() {
			ApplicationLauncher.logger.info("Trigger_Stop_Confirmation : Entry");
			TCStopConfirmationTimer = new Timer();
			TCStopConfirmationTimer.schedule(new ExecuteStopConfirmationTC(), 100);
		}

		class ExecuteStopConfirmationTC extends TimerTask{


			@Override
			public void run() {
				Platform.runLater(() -> {
					boolean status=false;
					status=stop_confirmation();
					if(status){
						StopOnClickSuccess();
					}
					TCStopConfirmationTimer.cancel();
				});
			}

		}


		class ViewLogsTask extends TimerTask {
			public void run() {
				ApplicationLauncher.setCursor(Cursor.WAIT);

				ViewLogsDataFetchFromDB();
				ApplicationLauncher.setCursor(Cursor.DEFAULT);
				ViewLogsTimer.cancel();
			}
		}


		public void enableBusyLoadingScreen() {//long time_in_seconds) {
			ApplicationLauncher.logger.info("ProjectAutoModeExecutionController: enableBusyLoadingScreen: entry");

			//FXMLLoader loader = new FXMLLoader(
			//		getClass().getResource("/fxml/setting/ScanDevice" + ConstantApp.THEME_FXML));
			Parent nodeFromFXML = null;
			try {
				nodeFromFXML = getNodeFromFXML("/fxml/setting/BusyLoading" + ConstantApp.THEME_FXML);
				ApplicationHomeController.displayBusyLoadingScreen(nodeFromFXML);
			}catch(Exception e) {
				e.printStackTrace();
				ApplicationLauncher.logger.error("ProjectExecutionController: enableBusyLoadingScreen: Exception: "+e.getMessage());
			}
		}

		public void disableBusyLoadingScreen(){
			BusyLoadingController.removeBusyLoadingScreenOverlay();
		}

		private Parent getNodeFromFXML(String url) throws IOException {

			FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
			Parent parentNode = loader.load();

			ApplicationLauncher.logger.info("ProjectExecutionController: Loaded property UI: " + parentNode);
			//testPropertyController = loader.getController();
			return parentNode;
		}

		public void SleepAndUpdateBottomLeftStatus(int timeInMsec,String message) {

			int timeInSec = timeInMsec/1000;
			while((timeInSec!=0) && (!getUserAbortedFlag()) ){
				int displayTime = timeInSec;
				Platform.runLater(() -> {
					ApplicationHomeController.update_left_status(message + displayTime + " Sec",	ConstantApp.LEFT_STATUS_DEBUG);
				});

				try {
					Thread.sleep(1000);
					timeInSec--;
				} catch (InterruptedException e) {
					
					timeInSec = 0;
					e.printStackTrace();
					ApplicationLauncher.logger.error("SleepAndUpdateBottomLeftStatus: Auto :InterruptedException:" + e.getMessage());
				}
				//if (!getUserAbortedFlag()) {


			}
			ApplicationHomeController.update_left_status(message + " Done",	ConstantApp.LEFT_STATUS_DEBUG);

		}
		
		
		public void procalRemoteLoadNextTrigger() {
			ApplicationLauncher.logger.info("procalRemoteLoadNextTrigger : Entry");
			loadNextTimer = new Timer();
			loadNextTimer.schedule(new LoadNextTrigger(), 10);
		}


		@FXML
		public void loadNextClick() {
			ApplicationLauncher.logger.info("loadNextClick : Entry");
			loadNextTimer = new Timer();
			loadNextTimer.schedule(new LoadNextTrigger(), 10);
		}

		class LoadNextTrigger extends TimerTask{


			@Override
			public void run() {
				//Platform.runLater(() -> {
				loadNextTask();
				loadNextTimer.cancel();
				//});
			}

		}

		@FXML
		public void calibConfigRefreshClick() {
			ApplicationLauncher.logger.info("calibConfigRefreshClick : Entry");
			calibConfigRefreshTimer = new Timer();
			calibConfigRefreshTimer.schedule(new CalibConfigRefreshTrigger(), 100);
		}

		class CalibConfigRefreshTrigger extends TimerTask{


			@Override
			public void run() {
				Platform.runLater(() -> {
					calibConfigRefreshTask();
					calibConfigRefreshTimer.cancel();
				});
			}

		}


		@FXML
		public void sourceRefreshClick() {
			ApplicationLauncher.logger.info("sourceRefreshClick : Entry");
			sourceRefreshTimer = new Timer();
			sourceRefreshTimer.schedule(new SourceRefreshClickTrigger(), 10);
		}

		class SourceRefreshClickTrigger extends TimerTask{


			@Override
			public void run() {
				Platform.runLater(() -> {
					sourceRefreshClickTask();
					sourceRefreshTimer.cancel();
				});
			}

		}

		
		public void loadNextTask(){
			ApplicationLauncher.logger.info("loadNextTask : Entry");
		  /*IncrementCurrentTestPoint_Index();
			setOneTimeExecuted(false);
			setCurrentTestPoint_ExecutionCompletedStatus(true);
			UpdateProjectProgressBar();
			refreshProjectStatusGraphGUI();
			//setOneTimeExecuted(true);
			ProjectStatusRefresh.ResetTestPointProgressBar(ref_barTC_TimeProgressBar);
			//SaveProjectRunEndTime();
			setExecuteStopStatus(false);*/
			ApplicationHomeController.updateBottomSecondaryStatus("Loading next Test point",ConstantApp.LEFT_STATUS_INFO);
			if(ProcalFeatureEnable.PROPOWER_SRC_ONLY ){
				LiveTableDataManager.UpdateliveTableData(1, "N",ConstantApp.EXECUTION_STATUS_COMPLETED);//
				DisableStopButton();
				Sleep(2);
			}else if(ProcalFeatureEnable.CONVEYOR_CALIB_BOFA_FEATURE_ENABLED) {
				//LiveTableDataManager.UpdateliveTableData(1, "N",ConstantApp.EXECUTION_STATUS_COMPLETED);//
				//updateDB_DeviceExecutionStatus( ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID, ConstantReport.RESULT_STATUS_PASS.trim(),ConstantApp.EXECUTION_STATUS_COMPLETED, 
				//		ConstantReport.RESULT_DATA_TYPE_EXECUTION_STATUS);
				LiveTableDataManager.UpdateliveTableData(ConstantApp.LIVE_TABLE_EXECUTION_STATUS_ID, ConstantReport.RESULT_STATUS_PASS.trim(),ConstantApp.EXECUTION_STATUS_COMPLETED);//
				
				//DisableStopButton();
				Sleep(2);
			}
			setExecuteTimeCounter(0);
			ref_btnLoadNext.setDisable(true);
			DeviceDataManagerController.setPowerSrcReadFeedbackData( false);
			disableBtnLoadNext();

			//Sleep(2000);
			SleepForSecondsWithAbortMonitoring(2);
			semLockExecutionInprogress = false;
			ApplicationLauncher.logger.info("loadNextTask: semLockExecutionInprogress: released-reset");
			//ref_btnLoadNext.setDisable(false);
			if(ProcalFeatureEnable.PROPOWER_SRC_ONLY ){

			}else {
				enableBtnLoadNext();
			}
			ApplicationLauncher.logger.info("loadNextTask: Exit");
		}
		
		public static void loadNextTestPointTask(){
			ApplicationLauncher.logger.info("loadNextTestPointTask : Entry");
		  /*IncrementCurrentTestPoint_Index();
			setOneTimeExecuted(false);
			setCurrentTestPoint_ExecutionCompletedStatus(true);
			UpdateProjectProgressBar();
			refreshProjectStatusGraphGUI();
			//setOneTimeExecuted(true);
			ProjectStatusRefresh.ResetTestPointProgressBar(ref_barTC_TimeProgressBar);
			//SaveProjectRunEndTime();
			setExecuteStopStatus(false);*/
		int totalNoOfTestPointIndex  = getCurrentProjectTestPointList().length()-1;
		ApplicationLauncher.logger.debug("loadNextTestPointTask: totalNoOfTestPointIndex: " + totalNoOfTestPointIndex );
		ApplicationLauncher.logger.debug("loadNextTestPointTask: CurrentTestPoint_Index: " + CurrentTestPoint_Index );

		
		if(CurrentTestPoint_Index < totalNoOfTestPointIndex){
			ApplicationHomeController.updateBottomSecondaryStatus("Loading next Test point",ConstantApp.LEFT_STATUS_INFO);
			if(ProcalFeatureEnable.PROPOWER_SRC_ONLY ){
				LiveTableDataManager.UpdateliveTableData(1, "N",ConstantApp.EXECUTION_STATUS_COMPLETED);//
				DisableStopButton();
				SleepV2(2);
			}
			setExecuteTimeCounter(0);
			ref_btnLoadNext.setDisable(true);
			DeviceDataManagerController.setPowerSrcReadFeedbackData( false);
			disableBtnLoadNext();

			//Sleep(2000);
			SleepForSecondsWithAbortMonitoringV2(2);
			semLockExecutionInprogress = false;
			ApplicationLauncher.logger.info("loadNextTestPointTask: semLockExecutionInprogress: released-reset");
			//ref_btnLoadNext.setDisable(false);
			if(ProcalFeatureEnable.PROPOWER_SRC_ONLY ){

			}else {
				//enableBtnLoadNext();
			}
			EnableStopButton();
		}else {
			//EnableStopButton();
			
		}
			
			ApplicationLauncher.logger.info("loadNextTestPointTask: Exit");
		}
		
		public static void SleepV2(int timeInMsec) {

			try {
				Thread.sleep(timeInMsec);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("Sleep :InterruptedException:"+ e.getMessage());
			}

		}
		
		public static void SleepForSecondsWithAbortMonitoringV2(int timeInSec) {

			while((timeInSec>0) && (!getUserAbortedFlag())) {

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					
					e.printStackTrace();
					ApplicationLauncher.logger.error("Sleep :InterruptedException:"+ e.getMessage());
				}
				timeInSec--;
			}

		}

		public void calibConfigRefreshTask(){
			ApplicationLauncher.logger.info("calibConfigRefreshTask : Entry");
			ref_btnCalibConfigRefresh.setDisable(true);

			LscsCalibrationConfigLoader.init();
			DeviceDataManagerController.manipulateTargetRmsValue();
			Sleep(2000);
			ref_btnCalibConfigRefresh.setDisable(false);
		}

		public void sourceRefreshClickTask(){
			ApplicationLauncher.logger.info("sourceRefreshClickTask : Entry");
			Platform.runLater(() -> {
				ref_btnSourceRefresh.setDisable(true);
			});

			LscsCalibrationConfigLoader.init();
			DeviceDataManagerController.manipulateTargetRmsValue();
			//Sleep(2000);

			DisplayPwrSrc_TurnOn();
			Sleep(2000);
			Platform.runLater(() -> {
				ref_btnSourceRefresh.setDisable(false);
			});
		}

		private static void applyUacSettings() {
			
			ApplicationLauncher.logger.info("ProjectExecutionController : applyUacSettings :  Entry");
			ArrayList<UacDataModel> uacSelectProfileScreenList = DeviceDataManagerController.getUacSelectProfileScreenList();
			String screenName = "";
			for (int i = 0; i < uacSelectProfileScreenList.size(); i++){

				screenName = uacSelectProfileScreenList.get(i).getScreenName();
				ApplicationLauncher.logger.info("ProjectExecutionController : screenName :  "+screenName);
				switch (screenName) {
				case ConstantApp.UAC_TEST_RUN_SCREEN:


					if(!uacSelectProfileScreenList.get(i).getExecutePossible()){
						ApplicationLauncher.logger.info("ProjectExecutionController : Test1 :  ");
						ref_btn_execution_start.setDisable(true);
						ref_btn_Resume.setDisable(true);
						ref_btn_step_run.setDisable(true);
						ref_btnCloseProject.setDisable(true);
						setUacExecuteAuthorized(false);
					}

					if(!uacSelectProfileScreenList.get(i).getAddPossible()){
						//ref_btn_Create.setDisable(true);

					}

					if(!uacSelectProfileScreenList.get(i).getUpdatePossible()){
						//ref_vbox_testscript.setDisable(true);sdvsc
						//setChildPropertySaveEnabled(false);
						//ref_btn_Save.setDisable(true);

					}

					if(!uacSelectProfileScreenList.get(i).getDeletePossible()){
						//ref_btn_Delete.setDisable(true);

					}
					break;



				default:
					break;
				}



			}
			//ApplicationLauncher.logger.debug("ProjectExecutionController : applyUacSettings :  Exit");
		}

		public static boolean isUacExecuteAuthorized() {
			return uacExecuteAuthorized;
		}

		public static void setUacExecuteAuthorized(boolean executeAuthorized) {
			uacExecuteAuthorized = executeAuthorized;
		}

		public static String getFeedbackR_ActiveEnergy() {
			return feedbackR_ActiveEnergy;
		}

		public static void setFeedbackR_ActiveEnergy(String feedbackR_ActiveEnergy) {
			ProjectExecutionController.feedbackR_ActiveEnergy = feedbackR_ActiveEnergy;
		}

		public static String getFeedbackR_ReactiveEnergy() {
			return feedbackR_ReactiveEnergy;
		}

		public static void setFeedbackR_ReactiveEnergy(String feedbackR_ReactiveEnergy) {
			ProjectExecutionController.feedbackR_ReactiveEnergy = feedbackR_ReactiveEnergy;
		}

		public static String getFeedbackR_ApparentEnergy() {
			return feedbackR_ApparentEnergy;
		}

		public static void setFeedbackR_ApparentEnergy(String feedbackR_ApparentEnergy) {
			ProjectExecutionController.feedbackR_ApparentEnergy = feedbackR_ApparentEnergy;
		}

		public static String getFeedbackY_ActiveEnergy() {
			return feedbackY_ActiveEnergy;
		}

		public static void setFeedbackY_ActiveEnergy(String feedbackY_ActiveEnergy) {
			ProjectExecutionController.feedbackY_ActiveEnergy = feedbackY_ActiveEnergy;
		}

		public static String getFeedbackY_ReactiveEnergy() {
			return feedbackY_ReactiveEnergy;
		}

		public static void setFeedbackY_ReactiveEnergy(String feedbackY_ReactiveEnergy) {
			ProjectExecutionController.feedbackY_ReactiveEnergy = feedbackY_ReactiveEnergy;
		}

		public static String getFeedbackY_ApparentEnergy() {
			return feedbackY_ApparentEnergy;
		}

		public static void setFeedbackY_ApparentEnergy(String feedbackY_ApparentEnergy) {
			ProjectExecutionController.feedbackY_ApparentEnergy = feedbackY_ApparentEnergy;
		}

		public static String getFeedbackB_ActiveEnergy() {
			return feedbackB_ActiveEnergy;
		}

		public static void setFeedbackB_ActiveEnergy(String feedbackB_ActiveEnergy) {
			ProjectExecutionController.feedbackB_ActiveEnergy = feedbackB_ActiveEnergy;
		}

		public static String getFeedbackB_ReactiveEnergy() {
			return feedbackB_ReactiveEnergy;
		}

		public static void setFeedbackB_ReactiveEnergy(String feedbackB_ReactiveEnergy) {
			ProjectExecutionController.feedbackB_ReactiveEnergy = feedbackB_ReactiveEnergy;
		}

		public static String getFeedbackB_ApparentEnergy() {
			return feedbackB_ApparentEnergy;
		}

		public static void setFeedbackB_ApparentEnergy(String feedbackB_ApparentEnergy) {
			ProjectExecutionController.feedbackB_ApparentEnergy = feedbackB_ApparentEnergy;
		}


		@FXML
		public void manualModeBtnStartOnClick() {

			ApplicationLauncher.logger.info("manualModeBtnStartOnClick: Entry");
			manualModeBtnStartTimer = new Timer();
			manualModeBtnStartTimer.schedule(new manualModeStartTaskClick(), 50);


		}

		class manualModeStartTaskClick extends TimerTask {
			public void run() {
				manualModeStartTask();
				manualModeBtnStartTimer.cancel();


			}


		}

		public void manualModeStartTask() {
			
			ApplicationLauncher.logger.debug("manualModeStartTask: Entry");
			boolean status = false;


			status = isManualModeDataInputValid();
			if(status){

				status = isManualTimerModeDataInputValid();
				if(status){
					disableManualModeScreenButton();
					ApplicationHomeController.disableLeftMenuButtonsForTestRun();
					setManualModeExecution(true);
					disableBtnManualModeStart();
					disableTabSequenceMode();
					ScanForSerialPorts();
					status = DisplayDataObj.validateLscsSourceComPortAccessible();
					if(status){
						setUserAbortedFlag(false);
						//semLockExecutionInprogress = false;
						//DisableStepRunButton();
						if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
							status = SerialDM_Obj.SetPowerSourceOffWithInit();
						}else{
							if(!SerialDataManager.isPowerSourceTurnedOff()){
								status = SerialDM_Obj.SetPowerSourceOff();
							}else{
								status = true;
							}
						}
						if(status){
							ApplicationLauncher.logger.debug("manualModeStartTask: Source comm command success");
							if(ProcalFeatureEnable.EXPORT_MODE_ENABLED){
								//if(getCurrentTestPointName().contains(ConstantApp.EXPORT_MODE_ALIAS_NAME)){
								if(ref_rdBtnManualModeExport.isSelected()){	
									DeviceDataManagerController.setEnergyFlowMode(ConstantPowerSourceMte.EXPORT_MODE);
								}else{
									DeviceDataManagerController.setEnergyFlowMode(ConstantPowerSourceMte.IMPORT_MODE);
								}					


							}else{
								DeviceDataManagerController.setEnergyFlowMode(ConstantPowerSourceMte.IMPORT_MODE);
							}
							DeviceDataManagerController.setDeployedEM_ModelType(ConstantApp.METERTYPE_ACTIVE);
							DisplayDataObj.PowerSourceEnergyFlowModeDataInit();
							ApplicationLauncher.logger.info("manualModeStartTask: Current Test Point: EnergyFlowMode:" + DeviceDataManagerController.getEnergyFlowMode());
							ApplicationLauncher.logger.info("manualModeStartTask:  Current Test Point: EM_ModelType:" + DeviceDataManagerController.getDeployedEM_ModelType());


							Data_PowerSourceSetTarget powerSrcTargetData = new Data_PowerSourceSetTarget();
							powerSrcTargetData.setR_PhaseVoltageDisplayData(ref_txt_ManualModeRphaseVoltage.getText());
							powerSrcTargetData.setY_PhaseVoltageDisplayData(ref_txt_ManualModeYphaseVoltage.getText());
							powerSrcTargetData.setB_PhaseVoltageDisplayData(ref_txt_ManualModeBphaseVoltage.getText());

							powerSrcTargetData.setR_PhaseCurrentDisplayData(ref_txt_ManualModeRphaseCurrent.getText());
							powerSrcTargetData.setY_PhaseCurrentDisplayData(ref_txt_ManualModeYphaseCurrent.getText());
							powerSrcTargetData.setB_PhaseCurrentDisplayData(ref_txt_ManualModeBphaseCurrent.getText());

							String ph1 = getPhaseValue2(ref_txt_ManualModeRphasePfValue.getText(), getLagLeadValueFromRadiobtns(ref_rdBtnManualModeRphasePfLag));
							String ph2 = getPhaseValue2(ref_txt_ManualModeYphasePfValue.getText(), getLagLeadValueFromRadiobtns(ref_rdBtnManualModeYphasePfLag));
							String ph3 = getPhaseValue2(ref_txt_ManualModeBphasePfValue.getText(), getLagLeadValueFromRadiobtns(ref_rdBtnManualModeBphasePfLag));

							ApplicationLauncher.logger.debug("manualModeStartTask: rPhasePf: " + ph1);
							ApplicationLauncher.logger.debug("manualModeStartTask: yPhasePf: " + ph2);
							ApplicationLauncher.logger.debug("manualModeStartTask: bPhasePf: " + ph3);

							powerSrcTargetData.setR_PhasePowerFactorData(ph1);
							powerSrcTargetData.setY_PhasePowerFactorData(ph2);
							powerSrcTargetData.setB_PhasePowerFactorData(ph3);
							String freq = ref_txt_ManualModeFrequencyValue.getText();
							powerSrcTargetData.setFreqData(freq);

							boolean threePhaseSelected = ref_rdBtnManualModeThreePhase.isSelected();
							if(threePhaseSelected){
								DeviceDataManagerController.setDeployedEM_ModelType(ConstantApp.METER_TYPE_THREE_PHASE_STAR_ACTIVE);
							}else{
								DeviceDataManagerController.setDeployedEM_ModelType(ConstantApp.METER_TYPE_SINGLE_PHASE_ACTIVE);
							}

							DisplayDataObj.setManualModeParameters(powerSrcTargetData,threePhaseSelected);


							//String command = SerialDM_Obj.propowerComputeCommandV2_process(threePhaseSelected);
							//SerialDM_Obj.sendManualModeCommandProcessTask(command);
							//Sleep(5000);
							SleepForSecondsWithAbortMonitoring(5);
							boolean srcStatus= false;
							if(threePhaseSelected){
								srcStatus = SerialDM_Obj.lscsThreePhaseBalancedPwrSrcOn(freq);


								if(srcStatus) {
									//isDataErrorAckRcvdFromLscsPowerSource();
									SerialDataManager.setPowerSrcOnFlag(true);
									SerialDataManager.setPowerSourceTurnedOff(false);
								}else {
									ApplicationLauncher.logger.debug("manualModeStartTask: setting error failure");
									SerialDataManager.setPowerSrcErrorResponseReceivedStatus(true);
								}
							}else{
								srcStatus = SerialDM_Obj.lscsSinglePhasePwrSrcOn(freq);

								if(srcStatus) {
									//isDataErrorAckRcvdFromLscsPowerSource();
									SerialDataManager.setPowerSrcOnFlag(true);
									SerialDataManager.setPowerSourceTurnedOff(false);
								}else {
									ApplicationLauncher.logger.debug("manualModeStartTask: setting error2 failure");
									SerialDataManager.setPowerSrcErrorResponseReceivedStatus(true);
								}
							}
							if(!SerialDataManager.getPowerSrcErrorResponseReceivedStatus()){

								if(ProcalFeatureEnable.PROPOWER_SRC_FEEDBACK_DISPLAY ){

									ApplicationLauncher.logger.debug("manualModeStartTask: ProPower Source Feedback display" );
									manipulateGainOffsetValueForFeedBackProcess();
									SerialDM_Obj.lscsPowerSourceReadFeedBackTimerInit();


								}
								//Sleep(5000);
								SleepForSecondsWithAbortMonitoring(5);
								//enableBtnManualModeUpdate();
								enableBtnManualModeStop();
								enableManualModeDataEntryField();
								//ApplicationLauncher.logger.debug("manualModeStartTask: Test1");
								if(ref_chBxManualTimerMode.isSelected()){
									//ApplicationLauncher.logger.debug("manualModeStartTask: Test2");

									lscsPowerSourceManualmodeTimerTimerInit();
									//ApplicationLauncher.logger.debug("manualModeStartTask: Test3");
								}else{
									enableBtnManualModeUpdate();
								}
								//ApplicationLauncher.logger.debug("manualModeStartTask: Test4");
							}else{
								ApplicationLauncher.logger.debug("manualModeStartTask: Source response failed");
								enableManualModeScreenButton();
								ApplicationHomeController.enableLeftMenuButtonsForTestRun();
								enableBtnManualModeStart();
								enableTabSequenceMode();
							}
						}else{
							ApplicationLauncher.logger.debug("manualModeStartTask: Source comm command check failed");
							enableManualModeScreenButton();
							ApplicationHomeController.enableLeftMenuButtonsForTestRun();
							enableBtnManualModeStart();
							enableTabSequenceMode();
						}



					}else{
						ApplicationLauncher.logger.debug("manualModeStartTask: Source comm access failed");
						enableManualModeScreenButton();
						ApplicationHomeController.enableLeftMenuButtonsForTestRun();
						enableBtnManualModeStart();
						enableTabSequenceMode();
					}
				}
			}

		}


		private void enableManualModeDataEntryField() {
			
			Platform.runLater(() -> {

				ref_txt_ManualModeRphaseVoltage.setDisable(false);

				ref_txt_ManualModeRphaseCurrent.setDisable(false);

				ref_txt_ManualModeRphasePfValue.setDisable(false);

				ref_rdBtnManualModeRphasePfLag.setDisable(false);

				ref_rdBtnManualModeRphasePfLead.setDisable(false);

				//ref_txt_ManualModeFrequencyValue.setDisable(false);
				if(ref_rdBtnManualModeThreePhase.isSelected()){
					if(ref_chkBxManualModeBalanced.isSelected()){
						ref_txt_ManualModeYphaseVoltage.setDisable(true);
						ref_txt_ManualModeBphaseVoltage.setDisable(true);
						ref_txt_ManualModeYphaseCurrent.setDisable(true);
						ref_txt_ManualModeBphaseCurrent.setDisable(true);
						ref_txt_ManualModeYphasePfValue.setDisable(true);
						ref_txt_ManualModeBphasePfValue.setDisable(true);
						ref_rdBtnManualModeYphasePfLag.setDisable(true);
						ref_rdBtnManualModeBphasePfLag.setDisable(true);
						ref_rdBtnManualModeYphasePfLead.setDisable(true);
						ref_rdBtnManualModeBphasePfLead.setDisable(true);
					}else{
						ref_txt_ManualModeYphaseVoltage.setDisable(false);
						ref_txt_ManualModeBphaseVoltage.setDisable(false);
						ref_txt_ManualModeYphaseCurrent.setDisable(false);
						ref_txt_ManualModeBphaseCurrent.setDisable(false);
						ref_txt_ManualModeYphasePfValue.setDisable(false);
						ref_txt_ManualModeBphasePfValue.setDisable(false);
						ref_rdBtnManualModeYphasePfLag.setDisable(false);
						ref_rdBtnManualModeBphasePfLag.setDisable(false);
						ref_rdBtnManualModeYphasePfLead.setDisable(false);
						ref_rdBtnManualModeBphasePfLead.setDisable(false);
					}
				}

			});
			enableChkBxManualModeBalanced();

		}

		private void disableManualModeDataEntryField() {
			
			Platform.runLater(() -> {

				ref_txt_ManualModeRphaseVoltage.setDisable(true);

				ref_txt_ManualModeRphaseCurrent.setDisable(true);

				ref_txt_ManualModeRphasePfValue.setDisable(true);

				ref_rdBtnManualModeRphasePfLag.setDisable(true);

				ref_rdBtnManualModeRphasePfLead.setDisable(true);

				//ref_txt_ManualModeFrequencyValue.setDisable(true);
				if(ref_rdBtnManualModeThreePhase.isSelected()){
					ref_txt_ManualModeYphaseVoltage.setDisable(true);
					ref_txt_ManualModeBphaseVoltage.setDisable(true);
					ref_txt_ManualModeYphaseCurrent.setDisable(true);
					ref_txt_ManualModeBphaseCurrent.setDisable(true);
					ref_txt_ManualModeYphasePfValue.setDisable(true);
					ref_txt_ManualModeBphasePfValue.setDisable(true);
					ref_rdBtnManualModeYphasePfLag.setDisable(true);
					ref_rdBtnManualModeBphasePfLag.setDisable(true);
					ref_rdBtnManualModeYphasePfLead.setDisable(true);
					ref_rdBtnManualModeBphasePfLead.setDisable(true);
				}

			});
			try{
				disableChkBxManualModeBalanced();
			}catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("disableManualModeDataEntryField: Exception :" +e.getMessage());
			}

		}


		private void enableManualModeScreenButton() {
			
			Platform.runLater(() -> {
				ref_rdBtnManualModeSinglePhase.setDisable(false);
				ref_rdBtnManualModeThreePhase.setDisable(false);
				ref_rdBtnManualModeImport.setDisable(false);
				ref_rdBtnManualModeExport.setDisable(false);
				ref_txt_ManualModeRphaseVoltage.setDisable(false);

				ref_txt_ManualModeRphaseCurrent.setDisable(false);

				ref_txt_ManualModeRphasePfValue.setDisable(false);

				ref_rdBtnManualModeRphasePfLag.setDisable(false);

				ref_rdBtnManualModeRphasePfLead.setDisable(false);

				ref_txt_ManualModeFrequencyValue.setDisable(false);
				if(ref_rdBtnManualModeThreePhase.isSelected()){
					if(ref_chkBxManualModeBalanced.isSelected()){
						ref_txt_ManualModeYphaseVoltage.setDisable(true);
						ref_txt_ManualModeBphaseVoltage.setDisable(true);
						ref_txt_ManualModeYphaseCurrent.setDisable(true);
						ref_txt_ManualModeBphaseCurrent.setDisable(true);
						ref_txt_ManualModeYphasePfValue.setDisable(true);
						ref_txt_ManualModeBphasePfValue.setDisable(true);
						ref_rdBtnManualModeYphasePfLag.setDisable(true);
						ref_rdBtnManualModeBphasePfLag.setDisable(true);
						ref_rdBtnManualModeYphasePfLead.setDisable(true);
						ref_rdBtnManualModeBphasePfLead.setDisable(true);
					}else{
						ref_txt_ManualModeYphaseVoltage.setDisable(false);
						ref_txt_ManualModeBphaseVoltage.setDisable(false);
						ref_txt_ManualModeYphaseCurrent.setDisable(false);
						ref_txt_ManualModeBphaseCurrent.setDisable(false);
						ref_txt_ManualModeYphasePfValue.setDisable(false);
						ref_txt_ManualModeBphasePfValue.setDisable(false);
						ref_rdBtnManualModeYphasePfLag.setDisable(false);
						ref_rdBtnManualModeBphasePfLag.setDisable(false);
						ref_rdBtnManualModeYphasePfLead.setDisable(false);
						ref_rdBtnManualModeBphasePfLead.setDisable(false);
					}
				}
				ref_chBxManualTimerMode.setDisable(false);
				ref_txtManualSetTime.setDisable(false);

			});

			enableChkBxManualModeBalanced();
		}


		private void disableManualModeScreenButton() {
			
			Platform.runLater(() -> {
				ref_rdBtnManualModeSinglePhase.setDisable(true);
				ref_rdBtnManualModeThreePhase.setDisable(true);
				ref_rdBtnManualModeImport.setDisable(true);
				ref_rdBtnManualModeExport.setDisable(true);
				ref_txt_ManualModeRphaseVoltage.setDisable(true);

				ref_txt_ManualModeRphaseCurrent.setDisable(true);

				ref_txt_ManualModeRphasePfValue.setDisable(true);

				ref_rdBtnManualModeRphasePfLag.setDisable(true);

				ref_rdBtnManualModeRphasePfLead.setDisable(true);

				ref_txt_ManualModeFrequencyValue.setDisable(true);
				if(ref_rdBtnManualModeThreePhase.isSelected()){
					ref_txt_ManualModeYphaseVoltage.setDisable(true);
					ref_txt_ManualModeBphaseVoltage.setDisable(true);
					ref_txt_ManualModeYphaseCurrent.setDisable(true);
					ref_txt_ManualModeBphaseCurrent.setDisable(true);
					ref_txt_ManualModeYphasePfValue.setDisable(true);
					ref_txt_ManualModeBphasePfValue.setDisable(true);
					ref_rdBtnManualModeYphasePfLag.setDisable(true);
					ref_rdBtnManualModeBphasePfLag.setDisable(true);
					ref_rdBtnManualModeYphasePfLead.setDisable(true);
					ref_rdBtnManualModeBphasePfLead.setDisable(true);

				}

				ref_chBxManualTimerMode.setDisable(true);
				ref_txtManualSetTime.setDisable(true);
			});

			if(ref_rdBtnManualModeThreePhase.isSelected()){
				disableChkBxManualModeBalanced();
			}
		}

		public static String getLagLeadValueFromRadiobtns(RadioButton lag){
			if(lag.isSelected()){
				return ConstantApp.PF_LAG;
			}
			else{
				return ConstantApp.PF_LEAD;
			}
		}

		public String getPhaseValue2(String PF_Value,String Lag_Lead){
			String phase = "0.0";
			float f_PF_Value = Float.parseFloat(PF_Value);

			if(f_PF_Value== 1.0f){
				phase =  "1";
			}
			else{
				phase =  PF_Value + Lag_Lead;
			}

			ApplicationLauncher.logger.info("getPhaseValue2: phase" + phase);
			return phase;
		}

		public void updateTxtManualTargetTime(String value){
			Platform.runLater(() -> {
				ref_txtManualTargetTime.setText(value);

			});
		}

		public boolean isManualTimerModeDataInputValid() {
			
			ApplicationLauncher.logger.debug("isManualTimerModeDataInputValid: Entry");
			boolean status = false;

			if(ref_chBxManualTimerMode.isSelected()){
				try{
					String value = ref_txtManualSetTime.getText();
					if(!value.isEmpty()){
						if(GuiUtils.isNumber(value)){

							if( (Integer.parseInt(value) >= ConstantAppConfig.PROPOWER_MANUAL_MODE_TIMER_INPUT_MIN_ACCEPTED)  &&  (Integer.parseInt(value) <= ConstantAppConfig.PROPOWER_MANUAL_MODE_TIMER_INPUT_MAX_ACCEPTED) ){
								setManualModeTimerInputSetValueInSec(Integer.parseInt(value));
								setManualModeTimerInputTargetValueInSec(Integer.parseInt(value));
								updateTxtManualTargetTime(String.valueOf(getManualModeTimerInputTargetValueInSec()));
								status = true;
							}else{
								ApplicationLauncher.logger.info("isManualTimerModeDataInputValid: " + "Manual Mode: Timer input - failed: Frequency entered not in acceptable range\n\nPresent value: " + value + " - Prompted");
								ApplicationLauncher.logger.info("isManualTimerModeDataInputValid: " + "Manual Mode: Timer input - failed: Frequency entered not in acceptable range\n\nPROPOWER_MANUAL_MODE_TIMER_INPUT_MIN_ACCEPTED: " + ConstantAppConfig.PROPOWER_MANUAL_MODE_TIMER_INPUT_MIN_ACCEPTED );
								ApplicationLauncher.logger.info("isManualTimerModeDataInputValid: " + "Manual Mode: Timer input - failed: Frequency entered not in acceptable range\n\nPROPOWER_MANUAL_MODE_TIMER_INPUT_MAX_ACCEPTED: " + ConstantAppConfig.PROPOWER_MANUAL_MODE_TIMER_INPUT_MAX_ACCEPTED );
								ApplicationLauncher.InformUser("Manual Mode: Timer input - failed","Timer input entered not in acceptable range\n\nPresent value: " + value,AlertType.ERROR);

							}
						}else{
							ApplicationLauncher.logger.info("isManualTimerModeDataInputValid: " + "Manual Mode: Timer input - failed: Kindly enter valid number for timer input is seconds\n\nPresent value: " +value +" - Prompted");
							ApplicationLauncher.InformUser("Manual Mode: Timer input - failed","Kindly enter valid number for timer input is seconds\n\nPresent value: " + value,AlertType.ERROR);

						}
					}else{
						ApplicationLauncher.logger.info("isManualTimerModeDataInputValid: " + "Manual Mode: Timer input - failed: Kindly enter valid number for timer input is seconds\n\nPresent value: Empty - Prompted");
						ApplicationLauncher.InformUser("Manual Mode: Timer input - failed","Kindly enter valid number for timer input is seconds\n\nPresent value: Empty",AlertType.ERROR);

					}
				}catch(Exception e) {
					e.printStackTrace();
					ApplicationLauncher.logger.error("isManualTimerModeDataInputValid: : Exception: "+e.getMessage());
				}
			}else{
				status = true;
			}

			return status;
		}

		public boolean isManualModeDataInputValid() {
			
			ApplicationLauncher.logger.debug("isManualModeDataInputValid: Entry");
			boolean status = false;

			float acceptedPfMaxValue = 1.0f;
			float acceptedPfMinValue = 0.0f;
			//if(ref_rdBtnManualModeThreePhase.isSelected()){

			/*String ph1 = getPhaseValue2(ref_txt_ManualModeRphasePfValue.getText(), getLagLeadValueFromRadiobtns(ref_rdBtnManualModeRphasePfLag));
		String ph2 = getPhaseValue2(ref_txt_ManualModeYphasePfValue.getText(), getLagLeadValueFromRadiobtns(ref_rdBtnManualModeYphasePfLag));
		String ph3 = getPhaseValue2(ref_txt_ManualModeBphasePfValue.getText(), getLagLeadValueFromRadiobtns(ref_rdBtnManualModeBphasePfLag));

		ApplicationLauncher.logger.debug("isManualModeDataInputValid: ph1: " + ph1);
		ApplicationLauncher.logger.debug("isManualModeDataInputValid: ph2: " + ph2);
		ApplicationLauncher.logger.debug("isManualModeDataInputValid: ph3: " + ph3);
			 */
			try{
				String value = ref_txt_ManualModeRphaseVoltage.getText();
				if(!value.isEmpty()){
					if(GuiUtils.is_float(value)){
						if( (Float.parseFloat(value) >= ConstantAppConfig.LTCT_VOLT_MIN)  &&  (Float.parseFloat(value) <= ConstantAppConfig.LTCT_VOLT_MAX) ){
							ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: R phase voltage good: value: " + value );

							value = ref_txt_ManualModeRphaseCurrent.getText();
							if(!value.isEmpty()){
								if(GuiUtils.is_float(value)){
									if(( (Float.parseFloat(value) >= ConstantAppConfig.LTCT_CURRENT_MIN)  &&  (Float.parseFloat(value) <= ConstantAppConfig.LTCT_CURRENT_MAX) )
									|| (Float.parseFloat(value)==0.0f) )	{
										ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: R phase current good: value: " + value );

										value = ref_txt_ManualModeRphasePfValue.getText();
										if(!value.isEmpty()){
											if(GuiUtils.is_float(value)){
												if( (Float.parseFloat(value) >= acceptedPfMinValue)  &&  (Float.parseFloat(value) <= acceptedPfMaxValue) ){
													ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: R phase pf good: value: " + value );
													value = ref_txt_ManualModeFrequencyValue.getText();

													if(!value.isEmpty()){
														if(GuiUtils.is_float(value)){
															float acceptedFreqMinValue = ConstantAppConfig.FREQUENCY_MIN;
															float acceptedFreqMaxValue = ConstantAppConfig.FREQUENCY_MAX;
															if( (Float.parseFloat(value) >= acceptedFreqMinValue)  &&  (Float.parseFloat(value) <= acceptedFreqMaxValue) ){
																ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: Frequency good: value: " + value );
																if(ref_rdBtnManualModeThreePhase.isSelected()){
																	ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: Y phase validation entry" );

																	value = ref_txt_ManualModeYphaseVoltage.getText();
																	if(!value.isEmpty()){
																		if(GuiUtils.is_float(value)){
																			if( (Float.parseFloat(value) >= ConstantAppConfig.LTCT_VOLT_MIN)  &&  (Float.parseFloat(value) <= ConstantAppConfig.LTCT_VOLT_MAX) ){
																				ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: Y phase voltage good: value: " + value );

																				value = ref_txt_ManualModeYphaseCurrent.getText();
																				if(!value.isEmpty()){
																					if(GuiUtils.is_float(value)){
																						if( (Float.parseFloat(value) >= ConstantAppConfig.LTCT_CURRENT_MIN)  &&  (Float.parseFloat(value) <= ConstantAppConfig.LTCT_CURRENT_MAX) ){
																							ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: Y phase current good: value: " + value );

																							value = ref_txt_ManualModeYphasePfValue.getText();
																							if(!value.isEmpty()){
																								if(GuiUtils.is_float(value)){
																									if( (Float.parseFloat(value) >= acceptedPfMinValue)  &&  (Float.parseFloat(value) <= acceptedPfMaxValue) ){
																										ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: Y phase pf good: value: " + value );
																										ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: B phase validation entry" );

																										value = ref_txt_ManualModeBphaseVoltage.getText();
																										if(!value.isEmpty()){
																											if(GuiUtils.is_float(value)){
																												if( (Float.parseFloat(value) >= ConstantAppConfig.LTCT_VOLT_MIN)  &&  (Float.parseFloat(value) <= ConstantAppConfig.LTCT_VOLT_MAX) ){
																													ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: B phase voltage good: value: " + value );
																													value = ref_txt_ManualModeBphaseCurrent.getText();
																													if(!value.isEmpty()){
																														if(GuiUtils.is_float(value)){
																															if( (Float.parseFloat(value) >= ConstantAppConfig.LTCT_CURRENT_MIN)  &&  (Float.parseFloat(value) <= ConstantAppConfig.LTCT_CURRENT_MAX) ){
																																ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: B phase current good: value: " + value );
																																value = ref_txt_ManualModeBphasePfValue.getText();
																																if(!value.isEmpty()){
																																	if(GuiUtils.is_float(value)){
																																		if( (Float.parseFloat(value) >= acceptedPfMinValue)  &&  (Float.parseFloat(value) <= acceptedPfMaxValue) ){

																																			ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: B phase pf good: value: " + value );
																																			ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: Three Phase all data input good " );
																																			status = true;
																																			return status;																																

																																		}else{
																																			ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: B phase pf - failed: B phase pf entered not in acceptable range\n\nPresent value: " + value + " - Prompted");
																																			ApplicationLauncher.InformUser("Manual Mode: B phase pf - failed","B phase pf entered not in acceptable range\n\nPresent value: " + value,AlertType.ERROR);
																																			return status;
																																		}

																																	}else{
																																		ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: B phase pf - failed: Kindly enter valid input for B phase pf\n\nPresent value: " + value + " - Prompted");
																																		ApplicationLauncher.InformUser("Manual Mode: B phase pf - failed","Kindly enter valid input for B phase pf\n\nPresent value: " + value,AlertType.ERROR);
																																		return status;
																																	}
																																}else{
																																	ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: B phase pf - failed: Kindly enter valid input for B phase pf\n\nPresent value: Empty - Prompted");
																																	ApplicationLauncher.InformUser("Manual Mode: B phase pf - failed","Kindly enter valid input for B phase pf\n\nPresent value: Empty",AlertType.ERROR);

																																	return status;
																																}
																															}else{
																																ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: B phase current - failed: B phase current entered not in acceptable range\n\nPresent value: " + value + " - Prompted");
																																ApplicationLauncher.InformUser("Manual Mode: B phase current - failed","B phase current entered not in acceptable range\n\nPresent value: " + value,AlertType.ERROR);
																																return status;
																															}

																														}else{
																															ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: B phase current - failed: Kindly enter valid input for B phase current\n\nPresent value: " + value + " - Prompted");
																															ApplicationLauncher.InformUser("Manual Mode: B phase current - failed","Kindly enter valid input for B phase current\n\nPresent value: " + value,AlertType.ERROR);
																															return status;
																														}
																													}else{
																														ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: B phase current - failed: Kindly enter valid input for B phase current\n\nPresent value: Empty - Prompted");
																														ApplicationLauncher.InformUser("Manual Mode: B phase current - failed","Kindly enter valid input for B phase current\n\nPresent value: Empty",AlertType.ERROR);

																														return status;
																													}


																												}else{
																													ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: B phase voltage - failed: B phase voltage entered not in acceptable range\n\nPresent value: " + value + " - Prompted");
																													ApplicationLauncher.InformUser("Manual Mode: B phase voltage - failed","B phase voltage entered not in acceptable range\n\nPresent value: " + value,AlertType.ERROR);
																													return status;
																												}

																											}else{
																												ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: B phase voltage - failed: Kindly enter valid input for B phase voltage\n\nPresent value: " + value + " - Prompted");
																												ApplicationLauncher.InformUser("Manual Mode: B phase voltage - failed","Kindly enter valid input for B phase voltage\n\nPresent value: " + value,AlertType.ERROR);
																												return status;
																											}
																										}else{
																											ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: B phase voltage - failed: Kindly enter valid input for B phase voltage\n\nPresent value: Empty - Prompted");
																											ApplicationLauncher.InformUser("Manual Mode: B phase voltage - failed","Kindly enter valid input for B phase voltage\n\nPresent value: Empty",AlertType.ERROR);

																											return status;
																										}
																									}else{
																										ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: Y phase pf - failed: Y phase pf entered not in acceptable range\n\nPresent value: " + value + " - Prompted");
																										ApplicationLauncher.InformUser("Manual Mode: Y phase pf - failed","Y phase pf entered not in acceptable range\n\nPresent value: " + value,AlertType.ERROR);
																										return status;
																									}

																								}else{
																									ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: Y phase pf - failed: Kindly enter valid input for Y phase pf\n\nPresent value: " + value + " - Prompted");
																									ApplicationLauncher.InformUser("Manual Mode: Y phase pf - failed","Kindly enter valid input for Y phase pf\n\nPresent value: " + value,AlertType.ERROR);
																									return status;
																								}
																							}else{
																								ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: Y phase pf - failed: Kindly enter valid input for Y phase pf\n\nPresent value: Empty - Prompted");
																								ApplicationLauncher.InformUser("Manual Mode: Y phase pf - failed","Kindly enter valid input for Y phase pf\n\nPresent value: Empty",AlertType.ERROR);

																								return status;
																							}
																						}else{
																							ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: Y phase current - failed: Y phase current entered not in acceptable range\n\nPresent value: " + value + " - Prompted");
																							ApplicationLauncher.InformUser("Manual Mode: Y phase current - failed","Y phase current entered not in acceptable range\n\nPresent value: " + value,AlertType.ERROR);
																							return status;
																						}

																					}else{
																						ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: Y phase current - failed: Kindly enter valid input for Y phase current\n\nPresent value: " + value + " - Prompted");
																						ApplicationLauncher.InformUser("Manual Mode: Y phase current - failed","Kindly enter valid input for Y phase current\n\nPresent value: " + value,AlertType.ERROR);
																						return status;
																					}
																				}else{
																					ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: Y phase current - failed: Kindly enter valid input for Y phase current\n\nPresent value: Empty - Prompted");
																					ApplicationLauncher.InformUser("Manual Mode: Y phase current - failed","Kindly enter valid input for Y phase current\n\nPresent value: Empty",AlertType.ERROR);

																					return status;
																				}


																			}else{
																				ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: Y phase voltage - failed: Y phase voltage entered not in acceptable range\n\nPresent value: " + value + " - Prompted");
																				ApplicationLauncher.InformUser("Manual Mode: Y phase voltage - failed","Y phase voltage entered not in acceptable range\n\nPresent value: " + value,AlertType.ERROR);
																				return status;
																			}

																		}else{
																			ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: Y phase voltage - failed: Kindly enter valid input for Y phase voltage\n\nPresent value: " + value + " - Prompted");
																			ApplicationLauncher.InformUser("Manual Mode: Y phase voltage - failed","Kindly enter valid input for Y phase voltage\n\nPresent value: " + value,AlertType.ERROR);
																			return status;
																		}
																	}else{
																		ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: Y phase voltage - failed: Kindly enter valid input for Y phase voltage\n\nPresent value: Empty - Prompted");
																		ApplicationLauncher.InformUser("Manual Mode: Y phase voltage - failed","Kindly enter valid input for Y phase voltage\n\nPresent value: Empty",AlertType.ERROR);

																		return status;
																	}

																}else{
																	ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: Single Phase all data input good " );
																	status = true;
																	return status;
																}

															}else{
																ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: Frequency - failed: Frequency entered not in acceptable range\n\nPresent value: " + value + " - Prompted");
																ApplicationLauncher.InformUser("Manual Mode: Frequency - failed","Frequency entered not in acceptable range\n\nPresent value: " + value,AlertType.ERROR);
																return status;
															}

														}else{
															ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: Frequency - failed: Kindly enter valid input for Frequency\n\nPresent value: " + value + " - Prompted");
															ApplicationLauncher.InformUser("Manual Mode: Frequency - failed","Kindly enter valid input for Frequency\n\nPresent value: " + value,AlertType.ERROR);
															return status;
														}
													}else{
														ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: Frequency - failed: Kindly enter valid input for Frequency\n\nPresent value: Empty - Prompted");
														ApplicationLauncher.InformUser("Manual Mode: Frequency - failed","Kindly enter valid input for Frequency\n\nPresent value: Empty",AlertType.ERROR);

														return status;
													}

												}else{
													ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: R phase pf - failed: R phase pf entered not in acceptable range\n\nPresent value: " + value + " - Prompted");
													ApplicationLauncher.InformUser("Manual Mode: R phase pf - failed","R phase pf entered not in acceptable range\n\nPresent value: " + value,AlertType.ERROR);
													return status;
												}

											}else{
												ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: R phase pf - failed: Kindly enter valid input for R phase pf\n\nPresent value: " + value + " - Prompted");
												ApplicationLauncher.InformUser("Manual Mode: R phase pf - failed","Kindly enter valid input for R phase pf\n\nPresent value: " + value,AlertType.ERROR);
												return status;
											}
										}else{
											ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: R phase pf - failed: Kindly enter valid input for R phase pf\n\nPresent value: Empty - Prompted");
											ApplicationLauncher.InformUser("Manual Mode: R phase pf - failed","Kindly enter valid input for R phase pf\n\nPresent value: Empty",AlertType.ERROR);

											return status;
										}
									}else{
										ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: R phase current - failed: R phase current entered not in acceptable range\n\nPresent value: " + value + " - Prompted");
										ApplicationLauncher.InformUser("Manual Mode: R phase current - failed","R phase current entered not in acceptable range\n\nPresent value: " + value,AlertType.ERROR);
										return status;
									}

								}else{
									ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: R phase current - failed: Kindly enter valid input for R phase current\n\nPresent value: " + value + " - Prompted");
									ApplicationLauncher.InformUser("Manual Mode: R phase current - failed","Kindly enter valid input for R phase current\n\nPresent value: " + value,AlertType.ERROR);
									return status;
								}
							}else{
								ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: R phase current - failed: Kindly enter valid input for R phase current\n\nPresent value: Empty - Prompted");
								ApplicationLauncher.InformUser("Manual Mode: R phase current - failed","Kindly enter valid input for R phase current\n\nPresent value: Empty",AlertType.ERROR);

								return status;
							}


						}else{
							ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: R phase voltage - failed: R phase voltage entered not in acceptable range\n\nPresent value: " + value + " - Prompted");
							ApplicationLauncher.InformUser("Manual Mode: R phase voltage - failed","R phase voltage entered not in acceptable range\n\nPresent value: " + value,AlertType.ERROR);
							return status;
						}

					}else{
						ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: R phase voltage - failed: Kindly enter valid input for R phase voltage\n\nPresent value: " + value + " - Prompted");
						ApplicationLauncher.InformUser("Manual Mode: R phase voltage - failed","Kindly enter valid input for R phase voltage\n\nPresent value: " + value,AlertType.ERROR);
						return status;
					}
				}else{
					ApplicationLauncher.logger.info("isManualModeDataInputValid: " + "Manual Mode: R phase voltage - failed: Kindly enter valid input for R phase voltage\n\nPresent value: Empty - Prompted");
					ApplicationLauncher.InformUser("Manual Mode: R phase voltage - failed","Kindly enter valid input for R phase voltage\n\nPresent value: Empty",AlertType.ERROR);

					return status;
				}
			}catch(Exception e) {
				e.printStackTrace();
				ApplicationLauncher.logger.error("isManualModeDataInputValid: : Exception: "+e.getMessage());
			}

			return status;
		}

		@FXML
		public void manualModeBtnUpdateOnClick() {

			ApplicationLauncher.logger.info("manualModeBtnUpdateOnClick: Entry");
			manualModeBtnUpdateTimer = new Timer();
			manualModeBtnUpdateTimer.schedule(new manualModeUpdateTaskClick(), 50);


		}

		class manualModeUpdateTaskClick extends TimerTask {
			public void run() {
				manualModeUpdateTask();
				manualModeBtnUpdateTimer.cancel();


			}
		}

		public void manualModeUpdateTask() {
			
			ApplicationLauncher.logger.debug("manualModeUpdateTask: Entry");
			boolean status = false;		
			status = isManualModeDataInputValid();
			if(status){
				status = isManualTimerModeDataInputValid();
				if(status){
					DeviceDataManagerController.setPowerSrcReadFeedbackData( false);
					disableBtnManualModeUpdate();
					disableBtnManualModeStop();
					disableManualModeDataEntryField();
					//Sleep(5000);
					ApplicationLauncher.logger.debug("manualModeUpdateTask: Sleep Entry 20 sec....");
					//Sleep(20000);
					SleepForSecondsWithAbortMonitoring(20);
					ApplicationLauncher.logger.debug("manualModeUpdateTask: Sleep Exit 20 sec");
					Data_PowerSourceSetTarget powerSrcTargetData = new Data_PowerSourceSetTarget();
					powerSrcTargetData.setR_PhaseVoltageDisplayData(ref_txt_ManualModeRphaseVoltage.getText());
					powerSrcTargetData.setY_PhaseVoltageDisplayData(ref_txt_ManualModeYphaseVoltage.getText());
					powerSrcTargetData.setB_PhaseVoltageDisplayData(ref_txt_ManualModeBphaseVoltage.getText());

					powerSrcTargetData.setR_PhaseCurrentDisplayData(ref_txt_ManualModeRphaseCurrent.getText());
					powerSrcTargetData.setY_PhaseCurrentDisplayData(ref_txt_ManualModeYphaseCurrent.getText());
					powerSrcTargetData.setB_PhaseCurrentDisplayData(ref_txt_ManualModeBphaseCurrent.getText());

					String ph1 = getPhaseValue2(ref_txt_ManualModeRphasePfValue.getText(), getLagLeadValueFromRadiobtns(ref_rdBtnManualModeRphasePfLag));
					String ph2 = getPhaseValue2(ref_txt_ManualModeYphasePfValue.getText(), getLagLeadValueFromRadiobtns(ref_rdBtnManualModeYphasePfLag));
					String ph3 = getPhaseValue2(ref_txt_ManualModeBphasePfValue.getText(), getLagLeadValueFromRadiobtns(ref_rdBtnManualModeBphasePfLag));

					powerSrcTargetData.setR_PhasePowerFactorData(ph1);
					powerSrcTargetData.setY_PhasePowerFactorData(ph2);
					powerSrcTargetData.setB_PhasePowerFactorData(ph3);
					String freq = ref_txt_ManualModeFrequencyValue.getText();
					powerSrcTargetData.setFreqData(freq);

					boolean threePhaseSelected = ref_rdBtnManualModeThreePhase.isSelected();
					if(threePhaseSelected){
						DeviceDataManagerController.setDeployedEM_ModelType(ConstantApp.METER_TYPE_THREE_PHASE_STAR_ACTIVE);
					}else{
						DeviceDataManagerController.setDeployedEM_ModelType(ConstantApp.METER_TYPE_SINGLE_PHASE_ACTIVE);
					}

					DisplayDataObj.setManualModeParameters(powerSrcTargetData,threePhaseSelected);


					//String command = SerialDM_Obj.propowerComputeCommandV2_process(threePhaseSelected);
					//SerialDM_Obj.sendManualModeCommandProcessTask(command);

					//SerialDM_Obj.lscsThreePhaseBalancedPwrSrcOn(freq);zvxxc
					boolean srcStatus = false;
					if(threePhaseSelected){
						srcStatus = SerialDM_Obj.lscsThreePhaseBalancedPwrSrcOn(freq);
						if(srcStatus) {
							//isDataErrorAckRcvdFromLscsPowerSource();
							SerialDataManager.setPowerSrcOnFlag(true);
							SerialDataManager.setPowerSourceTurnedOff(false);
						}else {
							ApplicationLauncher.logger.debug("manualModeUpdateTask: setting error failure");
							SerialDataManager.setPowerSrcErrorResponseReceivedStatus(true);
						}
					}else{
						srcStatus = SerialDM_Obj.lscsSinglePhasePwrSrcOn(freq);

						if(srcStatus) {
							//isDataErrorAckRcvdFromLscsPowerSource();
							SerialDataManager.setPowerSrcOnFlag(true);
							SerialDataManager.setPowerSourceTurnedOff(false);
						}else {
							ApplicationLauncher.logger.debug("manualModeUpdateTask: setting error2 failure");
							SerialDataManager.setPowerSrcErrorResponseReceivedStatus(true);
						}
					}
					if(!SerialDataManager.getPowerSrcErrorResponseReceivedStatus()){

						if(ProcalFeatureEnable.PROPOWER_SRC_FEEDBACK_DISPLAY ){

							ApplicationLauncher.logger.debug("manualModeUpdateTask: ProPower Source Feedback display" );
							manipulateGainOffsetValueForFeedBackProcess();
							SerialDM_Obj.lscsPowerSourceReadFeedBackTimerInit();


						}
						//Sleep(5000);
						SleepForSecondsWithAbortMonitoring(5);
						enableBtnManualModeUpdate();
						enableBtnManualModeStop();
						enableManualModeDataEntryField();

					}else{
						ApplicationLauncher.logger.debug("manualModeUpdateTask: Source response failed");
						enableManualModeScreenButton();
						ApplicationHomeController.enableLeftMenuButtonsForTestRun();
						enableBtnManualModeStart();
						enableTabSequenceMode();
					}
				}

			}

		}

		public void lscsPowerSourceManualmodeTimerTimerInit() {
			ApplicationLauncher.logger.debug("lscsPowerSourceManualmodeTimerTimerInit: Entry");
			lscsPowerSourceManualModeTimerInputTimer = new Timer();

			//powerSrcComSemlock = true;
			lscsPowerSourceManualModeTimerInputTimer.schedule(new lscsPowerSourceManualmodeTimerTimerTask(),1000);// 2000);
			//if(pwrSrcComSerialStatusConnected){

			//	ApplicationLauncher.logger.info("lscsPowerSourceReadFeedBackTimerInit :setRefStdReadDataFlag to true");
			//DisplayDataObj.setPowerSrcReadFeedbackData( true);
			//}

		}

		class lscsPowerSourceManualmodeTimerTimerTask extends TimerTask {
			public void run() {
				ApplicationLauncher.logger.info("lscsPowerSourceManualmodeTimerTimerTask: Entry : " );
				//ApplicationLauncher.logger.info("RefComRemindTask: getRefStdReadDataFlag:" + DisplayDataObj.getRefStdReadDataFlag());
				//ApplicationLauncher.logger.info("RefComRemindTask: refComSerialStatusConnected:" + refComSerialStatusConnected);
				/*			if (DisplayDataObj.isPowerSrcReadFeedbackData() && pwrSrcComSerialStatusConnected){
				try {
					ApplicationLauncher.logger.info("lscsPowerSourceReadFeedBackTimerTask: Entry2");
					//ApplicationLauncher.logger.info("RefComRemindTask: RefStdComSemlock:" + RefStdComSemlock);
					//if(RefStdComSemlock){
						powerSrcComSemlock = false;


						lscsPowerSourceReadFeedBackAllData();

						powerSrcComSemlock = true;
						Sleep(200);// added delay to allow the Ref std BNC Constant task 
						//ApplicationLauncher.logger.info("RefComRemindTask:ReadRefStdData: RefComSemlock: Released");
					//}
				} catch (Exception e1){
					e1.printStackTrace();
					ApplicationLauncher.logger.error("lscsPowerSourceReadFeedBackTimerTask :Exception1:"+ e1.getMessage());

				}
				lscsPowerSourceManualModeTimerInputTimer.schedule(new lscsPowerSourceReadFeedBackTimerTask(), SerialLscsPowerSrcInstantMetricsRefreshTimeInMsec);


			}*/
				decrementManualModeTimerInputTargetValueInSec();
				updateTxtManualTargetTime(String.valueOf(getManualModeTimerInputTargetValueInSec()));
				if(getManualModeTimerInputTargetValueInSec()==0){
					setUserAbortedFlag(true);

					disableBtnManualModeStart();
					disableBtnManualModeUpdate();
					disableBtnManualModeStop();
					//setExecuteStopStatus(true);
					DisplayShutDownAllCompPortsTrigger();
					ApplicationLauncher.logger.debug("lscsPowerSourceManualmodeTimerTimerTask :Timer Exit1!");
					lscsPowerSourceManualModeTimerInputTimer.cancel(); //Terminate the timer thread



				}else if(getManualModeTimerInputTargetValueInSec()== ABORT_MANUAL_TIMER_MODE){
					ApplicationLauncher.logger.debug("lscsPowerSourceManualmodeTimerTimerTask :Timer Exit1!");
					lscsPowerSourceManualModeTimerInputTimer.cancel(); 

				}else if(getManualModeTimerInputTargetValueInSec()>0){

					ApplicationLauncher.logger.debug("lscsPowerSourceManualmodeTimerTimerTask :renewed!");
					if(getManualModeTimerInputTargetValueInSec()<ConstantAppConfig.PROPOWER_MANUAL_MODE_TIMER_INPUT_MIN_ACCEPTED){
						ApplicationLauncher.logger.debug("lscsPowerSourceManualmodeTimerTimerTask :disabling update and stop button!");
						disableBtnManualModeUpdate();
						disableBtnManualModeStop();
					}
					lscsPowerSourceManualModeTimerInputTimer.schedule(new lscsPowerSourceManualmodeTimerTimerTask(), 1000);
				}
			}
		}

		@FXML
		public void manualModeBtnStopOnClick() {

			ApplicationLauncher.logger.info("manualModeBtnStopOnClick: Entry");
			manualModeBtnStopTimer = new Timer();
			manualModeBtnStopTimer.schedule(new manualModeStopTaskClick(), 50);


		}

		class manualModeStopTaskClick extends TimerTask {
			public void run() {
				manualModeStopTask();
				manualModeBtnStopTimer.cancel();


			}
		}

		public void manualModeStopTask() {
			
			ApplicationLauncher.logger.debug("manualModeStopTask: Entry");
			//setExecuteStopStatus(true);
			Platform.runLater(() -> {
				boolean status=false;
				status=stop_confirmation();
				if(status){
					//StopOnClickSuccess();
					setUserAbortedFlag(true);
					setManualModeTimerInputTargetValueInSec(ABORT_MANUAL_TIMER_MODE);
					//if(!getStepRunFlag()){
					//	updateAbortedInDisplay();
					//}
					//ProjectExitProcess();
					//DisableStopButton();
					disableBtnManualModeStart();
					disableBtnManualModeUpdate();
					disableBtnManualModeStop();
					//SaveProjectRunEndTime();
					//		DisableStopButton();
					DisplayShutDownAllCompPortsTrigger();

				}

			});

		}

		//==================================================================================================	
		/*	private void initialiseHarmonicsData(){

		ApplicationLauncher.logger.debug("initialiseHarmonicsData : Entry");

		for(int i=0; i<= ConstantLscsHarmonicsSourceSlave.TOTAL_NO_OF_ORDER_HARMONICS ; i++){
			try {
				ApplicationLauncher.logger.debug("initialiseHarmonicsData : i : " + i);

				lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_V1.add(i, false);	
				lscsPowerSourceHarmonicsMessage.harmonicsOrderNumber_V1.add(i, 0);
				lscsPowerSourceHarmonicsMessage.amplitudePercentageHarmonicsOrder_V1.add(i, 0);
				lscsPowerSourceHarmonicsMessage.phaseAngleDegreeHarmonicsOrder_V1.add(i, 0);

				lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_V2.add(i, false);	
				lscsPowerSourceHarmonicsMessage.harmonicsOrderNumber_V2.add(i, 0);
				lscsPowerSourceHarmonicsMessage.amplitudePercentageHarmonicsOrder_V2.add(i, 0);
				lscsPowerSourceHarmonicsMessage.phaseAngleDegreeHarmonicsOrder_V2.add(i, 0);

				lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_V3.add(i, false);	
				lscsPowerSourceHarmonicsMessage.harmonicsOrderNumber_V3.add(i, 0);
				lscsPowerSourceHarmonicsMessage.amplitudePercentageHarmonicsOrder_V3.add(i, 0);
				lscsPowerSourceHarmonicsMessage.phaseAngleDegreeHarmonicsOrder_V3.add(i, 0);

				lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_I1.add(i, false);	
				lscsPowerSourceHarmonicsMessage.harmonicsOrderNumber_I1.add(i, 0);
				lscsPowerSourceHarmonicsMessage.amplitudePercentageHarmonicsOrder_I1.add(i, 0);
				lscsPowerSourceHarmonicsMessage.phaseAngleDegreeHarmonicsOrder_I1.add(i, 0);

				lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_I2.add(i, false);	
				lscsPowerSourceHarmonicsMessage.harmonicsOrderNumber_I2.add(i, 0);
				lscsPowerSourceHarmonicsMessage.amplitudePercentageHarmonicsOrder_I2.add(i, 0);
				lscsPowerSourceHarmonicsMessage.phaseAngleDegreeHarmonicsOrder_I2.add(i, 0);

				lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_I3.add(i, false);	
				lscsPowerSourceHarmonicsMessage.harmonicsOrderNumber_I3.add(i, 0);
				lscsPowerSourceHarmonicsMessage.amplitudePercentageHarmonicsOrder_I3.add(i, 0);
				lscsPowerSourceHarmonicsMessage.phaseAngleDegreeHarmonicsOrder_I3.add(i, 0);

				//dataFrames.add(i, 0);

			}
			catch(Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("initialiseHarmonicsData: Exception: " + e.getMessage());
			}	
		}

		//testing
		for(int j=0; j <= ConstantLscsHarmonicsSourceSlave.TOTAL_NO_OF_ORDER_HARMONICS ; j++){
			try {
				ApplicationLauncher.logger.debug("initialiseHarmonicsData : j : " + j);

				lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_V1.get(j);	
				ApplicationLauncher.logger.debug("lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_V1.get(j): " + lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_V1.get(j));

				lscsPowerSourceHarmonicsMessage.harmonicsOrderNumber_V1.add(j);
				ApplicationLauncher.logger.debug("lscsPowerSourceHarmonicsMessage.harmonicsOrderNumber_V1.get(j): " + lscsPowerSourceHarmonicsMessage.harmonicsOrderNumber_V1.get(j));

				lscsPowerSourceHarmonicsMessage.amplitudePercentageHarmonicsOrder_V1.add(j);
				ApplicationLauncher.logger.debug("lscsPowerSourceHarmonicsMessage.amplitudePercentageHarmonicsOrder_V1.get(j): " + lscsPowerSourceHarmonicsMessage.amplitudePercentageHarmonicsOrder_V1.get(j));

				lscsPowerSourceHarmonicsMessage.phaseAngleDegreeHarmonicsOrder_V1.add(j);
				ApplicationLauncher.logger.debug("lscsPowerSourceHarmonicsMessage.phaseAngleDegreeHarmonicsOrder_V1.get(j): " + lscsPowerSourceHarmonicsMessage.phaseAngleDegreeHarmonicsOrder_V1.get(j));

			}
			catch(Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("initialiseHarmonicsData: Exception: " + e.getMessage());
			}
		}


		ApplicationLauncher.logger.debug("initialiseHarmonicsData : Exit");
	}
		 */

		//=================================================================================================================================	

		/*	private void fetchHarmonicsDataFromDBandStore(){

		try{
        	ApplicationLauncher.logger.info("fetchHarmonicsDataFromDBandStore : Entry");
			JSONArray harmonic_db_data = DisplayDataObj.getHarmonic_data(); 
			JSONObject harmonics_row_data = new JSONObject();

			String harmonic_no = "";
			String phase_selected   = "";       // R = 1 ; Y = 2 ; B = 3 
			String harmonic_order   = "";       // eg: 03
			String harmonic_voltage = "";       // 100  Voltage Amplitude
			String harmonic_current = "";       // 100  Current Amplitude
			String harmonic_volt_phase = "";    // 180  Voltage Phase Shift
			String harmonic_current_phase = ""; // 180 Current Phase Shift

			// therefore we getting V1,03,100,180,I1,03,100,180 in a single row 

			// =============== fetching data from database and storing it into arrayList to access later ============================//

			ApplicationLauncher.logger.info("Display_Har_PwrSrc_TurnOn : harmonic_db_data: " + harmonic_db_data);
			for(int i=0; i<harmonic_db_data.length(); i++){       // harmonic_db_data.length == no. of rows

				ApplicationLauncher.logger.debug("Display_Har_PwrSrc_TurnOn :  i  : " + i);

				harmonics_row_data = harmonic_db_data.getJSONObject(i);

				phase_selected         = harmonics_row_data.getString("phase_selected"); // R = 1 ; Y = 2 ; B = 3 
				harmonic_order         = harmonics_row_data.getString("harmonic_order");  // 3 
				harmonic_voltage       = harmonics_row_data.getString("harmonic_volt");   //  100
				harmonic_volt_phase    = harmonics_row_data.getString("harmonic_volt_phase");     // 180
				harmonic_current       = harmonics_row_data.getString("harmonic_current");        // 100
				harmonic_current_phase = harmonics_row_data.getString("harmonic_current_phase");  // 180
				ApplicationLauncher.logger.info("Display_Har_PwrSrc_TurnOn : phase_selected         : " + phase_selected);
				ApplicationLauncher.logger.info("Display_Har_PwrSrc_TurnOn : harmonic_order         : " + harmonic_order);
				ApplicationLauncher.logger.info("Display_Har_PwrSrc_TurnOn : harmonic_voltage       : " + harmonic_voltage);
				ApplicationLauncher.logger.info("Display_Har_PwrSrc_TurnOn : harmonic_volt_phase    : " + harmonic_volt_phase);
				ApplicationLauncher.logger.info("Display_Har_PwrSrc_TurnOn : harmonic_current       : " + harmonic_current);
				ApplicationLauncher.logger.info("Display_Har_PwrSrc_TurnOn : harmonic_current_phase : " + harmonic_current_phase);

				if(phase_selected.equals(lscsPowerSourceHarmonicsMessage.R_PHASE)){

					ApplicationLauncher.logger.debug("Display_Har_PwrSrc_TurnOn : Dealing R Phase " );

					if(Integer.parseInt(harmonics_row_data.getString("harmonic_volt")) > 0 ){
					lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_V1.set(Integer.parseInt(harmonic_order), true);	
					} else {
						lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_V1.set(Integer.parseInt(harmonic_order), false);

				    }

					lscsPowerSourceHarmonicsMessage.harmonicsOrderNumber_V1.set(Integer.parseInt(harmonic_order), Integer.parseInt(harmonic_order));
					lscsPowerSourceHarmonicsMessage.amplitudePercentageHarmonicsOrder_V1.set(Integer.parseInt(harmonic_order), Integer.parseInt(harmonic_voltage));
					lscsPowerSourceHarmonicsMessage.phaseAngleDegreeHarmonicsOrder_V1.set(Integer.parseInt(harmonic_order), Integer.parseInt(harmonic_volt_phase));	

					if(Integer.parseInt(harmonics_row_data.getString("harmonic_current")) > 0 ){
						lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_I1.set(Integer.parseInt(harmonic_order), true);
						} else {
							lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_I1.set(Integer.parseInt(harmonic_order), false);

					    }	
					lscsPowerSourceHarmonicsMessage.harmonicsOrderNumber_I1.set(Integer.parseInt(harmonic_order), Integer.parseInt(harmonic_order));
					lscsPowerSourceHarmonicsMessage.amplitudePercentageHarmonicsOrder_I1.set(Integer.parseInt(harmonic_order), Integer.parseInt(harmonic_current));
					lscsPowerSourceHarmonicsMessage.phaseAngleDegreeHarmonicsOrder_I1.set(Integer.parseInt(harmonic_order), Integer.parseInt(harmonic_current_phase));	
				}
				else if(phase_selected.equals(lscsPowerSourceHarmonicsMessage.Y_PHASE)){
					ApplicationLauncher.logger.debug("Display_Har_PwrSrc_TurnOn : Dealing Y Phase " );

					if(Integer.parseInt(harmonics_row_data.getString("harmonic_volt")) > 0 ){
					lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_V2.set(Integer.parseInt(harmonic_order), true);
					} else {
						lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_V2.set(Integer.parseInt(harmonic_order), false);

				    }
					lscsPowerSourceHarmonicsMessage.harmonicsOrderNumber_V2.set(Integer.parseInt(harmonic_order), Integer.parseInt(harmonic_order));
					lscsPowerSourceHarmonicsMessage.amplitudePercentageHarmonicsOrder_V2.set(Integer.parseInt(harmonic_order), Integer.parseInt(harmonic_voltage));
					lscsPowerSourceHarmonicsMessage.phaseAngleDegreeHarmonicsOrder_V2.set(Integer.parseInt(harmonic_order), Integer.parseInt(harmonic_volt_phase));	

					if(Integer.parseInt(harmonics_row_data.getString("harmonic_current")) > 0 ){
						lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_I2.set(Integer.parseInt(harmonic_order), true);
						} else {
							lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_I2.set(Integer.parseInt(harmonic_order), false);

					    }		
					lscsPowerSourceHarmonicsMessage.harmonicsOrderNumber_I2.set(Integer.parseInt(harmonic_order), Integer.parseInt(harmonic_order));
					lscsPowerSourceHarmonicsMessage.amplitudePercentageHarmonicsOrder_I2.set(Integer.parseInt(harmonic_order), Integer.parseInt(harmonic_current));
					lscsPowerSourceHarmonicsMessage.phaseAngleDegreeHarmonicsOrder_I2.set(Integer.parseInt(harmonic_order), Integer.parseInt(harmonic_current_phase));	
				}
				else if(phase_selected.equals(lscsPowerSourceHarmonicsMessage.B_PHASE)){
					ApplicationLauncher.logger.debug("Display_Har_PwrSrc_TurnOn : Dealing B Phase " );

					if(Integer.parseInt(harmonics_row_data.getString("harmonic_volt")) > 0 ){
					lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_V3.set(Integer.parseInt(harmonic_order), true);
				    } else {
						lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_V3.set(Integer.parseInt(harmonic_order), false);

				    }
					lscsPowerSourceHarmonicsMessage.harmonicsOrderNumber_V3.set(Integer.parseInt(harmonic_order), Integer.parseInt(harmonic_order));
					lscsPowerSourceHarmonicsMessage.amplitudePercentageHarmonicsOrder_V3.set(Integer.parseInt(harmonic_order), Integer.parseInt(harmonic_voltage));
					lscsPowerSourceHarmonicsMessage.phaseAngleDegreeHarmonicsOrder_V3.set(Integer.parseInt(harmonic_order), Integer.parseInt(harmonic_volt_phase));	

					if(Integer.parseInt(harmonics_row_data.getString("harmonic_current")) > 0 ){
						lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_I3.set(Integer.parseInt(harmonic_order), true);
						} else {
							lscsPowerSourceHarmonicsMessage.includeHarmonicsOrder_I3.set(Integer.parseInt(harmonic_order), false);

					    }			
					lscsPowerSourceHarmonicsMessage.harmonicsOrderNumber_I3.set(Integer.parseInt(harmonic_order), Integer.parseInt(harmonic_order));
					lscsPowerSourceHarmonicsMessage.amplitudePercentageHarmonicsOrder_I3.set(Integer.parseInt(harmonic_order), Integer.parseInt(harmonic_current));
					lscsPowerSourceHarmonicsMessage.phaseAngleDegreeHarmonicsOrder_I3.set(Integer.parseInt(harmonic_order), Integer.parseInt(harmonic_current_phase));	
				}
			} // for loop end

			ApplicationLauncher.logger.info("fetchHarmonicsDataFromDBandStore :try end");

			lscsPowerSourceHarmonicsMessage.formDataFrames();   //form Data frames 

			lscsSendHarmonicsDataToSlave();

         }
         catch(Exception e){
 			e.printStackTrace();
 			ApplicationLauncher.logger.error("Display_Har_PwrSrc_TurnOn: Exception: " + e.getMessage());
 		}

	}*/

		//=====================================================================================================================================
		private void lscsSendHarmonicsDataToSlave(){
			ApplicationLauncher.logger.debug("lscsSendHarmonicsDataToSlave : Entry ");

			if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_FEATURE_ENABLED){
				ApplicationLauncher.logger.info("lscsSendHarmonicsDataToSlave : LSCS_POWER_SOURCE_HARMONICS_FEATURE_ENABLED ");
				// Temporary  
				boolean harmonicsEnabledInTest = true ;
				int retryCount =3;
				//	

				/*boolean harmonicsEnabledInSignal_V1 = true ;
			boolean harmonicsEnabledInSignal_V2 = true ;
			boolean harmonicsEnabledInSignal_V3 = true ;
			boolean harmonicsEnabledInSignal_I1 = true ;
			boolean harmonicsEnabledInSignal_I2 = true ;
			boolean harmonicsEnabledInSignal_I3 = true ;*/
				//		
				char harmonicsStatus_V1 ;
				char harmonicsStatus_V2 ;
				char harmonicsStatus_V3 ;
				char harmonicsStatus_I1 ;
				char harmonicsStatus_I2 ;
				char harmonicsStatus_I3 ;
				// 

				if(!harmonicsEnabledInTest){
					ApplicationLauncher.logger.debug("lscsSendHarmonicsDataToSlave : harmonicsEnabledInTest: " + harmonicsEnabledInTest);
					boolean status = false;
					status = lscsSetDisableHarmonicsAtSlave(); 
					if(status){
						lscsSendHarmonicsDataTransmissionEndCmdToSlave(); 	
					}
				}
				else if(harmonicsEnabledInTest){
					ApplicationLauncher.logger.debug("lscsSendHarmonicsDataToSlave : harmonicsEnabledInTest: " + harmonicsEnabledInTest);

					lscsPowerSourceHarmonicsMessage.setHarmonicsEnabledFrame(ConstantLscsHarmonicsSourceSlave.CMD_PWR_HRM_SRC_SLAVE_ENABLE_HARMONICS_HDR)  ; //+ "0,0,0,0,0,0";  //"HE,1,";//1,0,0,0,0,0"; 

					harmonicsStatus_V1 = lscsPowerSourceHarmonicsMessage.isHarmonicsEnabledInSignal_V1() ? '1' : '0' ;
					harmonicsStatus_V2 = lscsPowerSourceHarmonicsMessage.isHarmonicsEnabledInSignal_V2() ? '1' : '0' ;
					harmonicsStatus_V3 = lscsPowerSourceHarmonicsMessage.isHarmonicsEnabledInSignal_V3() ? '1' : '0' ;
					harmonicsStatus_I1 = lscsPowerSourceHarmonicsMessage.isHarmonicsEnabledInSignal_I1() ? '1' : '0' ;
					harmonicsStatus_I2 = lscsPowerSourceHarmonicsMessage.isHarmonicsEnabledInSignal_I2() ? '1' : '0' ;
					harmonicsStatus_I3 = lscsPowerSourceHarmonicsMessage.isHarmonicsEnabledInSignal_I3() ? '1' : '0' ;

					lscsPowerSourceHarmonicsMessage.setHarmonicsEnabledFrame(  lscsPowerSourceHarmonicsMessage.getHarmonicsEnabledFrame() + 
							harmonicsStatus_V1 + ConstantLscsHarmonicsSourceSlave.CMD_SEPERATOR +
							harmonicsStatus_I1 + ConstantLscsHarmonicsSourceSlave.CMD_SEPERATOR +
							harmonicsStatus_V2 + ConstantLscsHarmonicsSourceSlave.CMD_SEPERATOR +
							harmonicsStatus_I2 + ConstantLscsHarmonicsSourceSlave.CMD_SEPERATOR +
							harmonicsStatus_V3 + ConstantLscsHarmonicsSourceSlave.CMD_SEPERATOR +																		
							harmonicsStatus_I3   

							);

					boolean status = false;
					while((retryCount>=0) && (!status)){
						ApplicationLauncher.logger.debug("lscsSendHarmonicsDataToSlave : retryCount: " + retryCount);
						status = lscsSetEnableHarmonicsAtSlave();
						retryCount--;
					}
					ApplicationLauncher.logger.debug("lscsSendHarmonicsDataToSlave : retryCount Exit: ");
					if(status){
						status = lscsSendEnabledHarmonicsOrderDataToSlave();
						if(status){
							lscsSendFundamentalFrequencyToSlave();
							if(status){
								lscsSendHarmonicsDataTransmissionEndCmdToSlave(); 	
							}
						}
					}
				}			
			}

			ApplicationLauncher.logger.debug("lscsSendHarmonicsDataToSlave : Exit ");

		}





		/*	private void lscsSendHarmonicsDataToStm32Master(){
		ApplicationLauncher.logger.debug("lscsSendHarmonicsDataToStm32Master : Entry ");

		if(ProcalFeatureEnable.LSCS_POWER_SOURCE_HARMONICS_FEATURE_ENABLED){
			ApplicationLauncher.logger.info("lscsSendHarmonicsDataToStm32Master : LSCS_POWER_SOURCE_HARMONICS_FEATURE_ENABLED ");
			// Temporary  
			boolean harmonicsEnabledInTest = true ;

	//	

			boolean harmonicsEnabledInSignal_V1 = true ;
			boolean harmonicsEnabledInSignal_V2 = true ;
			boolean harmonicsEnabledInSignal_V3 = true ;
			boolean harmonicsEnabledInSignal_I1 = true ;
			boolean harmonicsEnabledInSignal_I2 = true ;
			boolean harmonicsEnabledInSignal_I3 = true ;
	//		
			char harmonicsStatus_V1 ;
			char harmonicsStatus_V2 ;
			char harmonicsStatus_V3 ;
			char harmonicsStatus_I1 ;
			char harmonicsStatus_I2 ;
			char harmonicsStatus_I3 ;
			// 

			if(!harmonicsEnabledInTest){
				ApplicationLauncher.logger.debug("lscsSendHarmonicsDataToStm32Master : harmonicsEnabledInTest: " + harmonicsEnabledInTest);
				boolean status = false;
				status = lscsSetDisableHarmonicsAtStm32Master();//lscsSetDisableHarmonicsAtSlave(); 
				if(status){
					lscsSendHarmonicsDataTransmissionEndCmdToStm32Master();//lscsSendHarmonicsDataTransmissionEndCmdToSlave(); 	fdhggf
				}
			}
			else if(harmonicsEnabledInTest){
				ApplicationLauncher.logger.debug("lscsSendHarmonicsDataToStm32Master : harmonicsEnabledInTest: " + harmonicsEnabledInTest);

				String harmonicsEnabledFrame = "" ;          //byte no. to alter -> 6,8,10,12,14  // move this to fetchDataFromDBandStore function

				lscsPowerSourceHarmonicsMessage.setHarmonicsEnabledFrame(ConstantLscsHarmonicsSourceSlave.CMD_PWR_HRM_SRC_SLAVE_ENABLE_HARMONICS_HDR)  ; //+ "0,0,0,0,0,0";  //"HE,1,";//1,0,0,0,0,0"; 

				harmonicsStatus_V1 = lscsPowerSourceHarmonicsMessage.isHarmonicsEnabledInSignal_V1() ? '1' : '0' ;
				harmonicsStatus_V2 = lscsPowerSourceHarmonicsMessage.isHarmonicsEnabledInSignal_V2() ? '1' : '0' ;
				harmonicsStatus_V3 = lscsPowerSourceHarmonicsMessage.isHarmonicsEnabledInSignal_V3() ? '1' : '0' ;
				harmonicsStatus_I1 = lscsPowerSourceHarmonicsMessage.isHarmonicsEnabledInSignal_I1() ? '1' : '0' ;
				harmonicsStatus_I2 = lscsPowerSourceHarmonicsMessage.isHarmonicsEnabledInSignal_I2() ? '1' : '0' ;
				harmonicsStatus_I3 = lscsPowerSourceHarmonicsMessage.isHarmonicsEnabledInSignal_I3() ? '1' : '0' ;

				 lscsPowerSourceHarmonicsMessage.setHarmonicsEnabledFrame(  lscsPowerSourceHarmonicsMessage.getHarmonicsEnabledFrame() + 
						                                                    harmonicsStatus_V1 + ConstantLscsHarmonicsSourceSlave.CMD_SEPERATOR +
						                                                    harmonicsStatus_I1 + ConstantLscsHarmonicsSourceSlave.CMD_SEPERATOR +
																		    harmonicsStatus_V2 + ConstantLscsHarmonicsSourceSlave.CMD_SEPERATOR +
																		    harmonicsStatus_I2 + ConstantLscsHarmonicsSourceSlave.CMD_SEPERATOR +
																			harmonicsStatus_V3 + ConstantLscsHarmonicsSourceSlave.CMD_SEPERATOR +																		
																			harmonicsStatus_I3   

																			);
				 boolean status = lscsSetEnableHarmonicsAtStm32Master();//lscsSetEnableHarmonicsAtSlave();egrdgf


				 if(status){
					 status = lscsSendEnabledHarmonicsOrderDataToSlave();fsadsf
					 if(status){
						 lscsSendFundamentalFrequencyToSlave();esdfs
						 if(status){
							 lscsSendHarmonicsDataTransmissionEndCmdToStm32Master();//lscsSendHarmonicsDataTransmissionEndCmdToSlave(); 	fsedgfsd
						 }
					 }
				 }
			}			
		}

		ApplicationLauncher.logger.debug("lscsSendHarmonicsDataToStm32Master : Exit ");

	}*/


		//===================================================================================================================================

		public static boolean isManualModeExecution() {
			return manualModeExecution;
		}

		public static void setManualModeExecution(boolean manualModeExecution) {
			ProjectExecutionController.manualModeExecution = manualModeExecution;
		}

		public int getManualModeTimerInputSetValueInSec() {
			return manualModeTimerInputSetValueInSec;
		}

		public void setManualModeTimerInputSetValueInSec(int manualModeTimerInputSetValueInSec) {
			this.manualModeTimerInputSetValueInSec = manualModeTimerInputSetValueInSec;
		}

		public int getManualModeTimerInputTargetValueInSec() {
			return manualModeTimerInputTargetValueInSec;
		}

		public void setManualModeTimerInputTargetValueInSec(int manualModeTimerInputTargetValueInSec) {
			this.manualModeTimerInputTargetValueInSec = manualModeTimerInputTargetValueInSec;
		}

		public void decrementManualModeTimerInputTargetValueInSec() {
			ApplicationLauncher.logger.debug("decrementManualModeTimerInputTargetValueInSec: Entry");
			if(manualModeTimerInputTargetValueInSec!= ABORT_MANUAL_TIMER_MODE){
				ApplicationLauncher.logger.debug("decrementManualModeTimerInputTargetValueInSec: decrementing");
				manualModeTimerInputTargetValueInSec--;

			}
		}

		public boolean isRefStdFeedBackControlFlagEnabled() {
			return refStdFeedBackControlFlagEnabled;
		}


		public void setRefStdFeedBackControlFlagEnabled(boolean refStdFeedBackControlFlagEnabled) {
			this.refStdFeedBackControlFlagEnabled = refStdFeedBackControlFlagEnabled;
		}


		public boolean isRefStdFeedBackControlSuspended() {
			return refStdFeedBackControlSuspended;
		}


		public void setRefStdFeedBackControlSuspended(boolean refStdFeedBackControlSuspended) {
			this.refStdFeedBackControlSuspended = refStdFeedBackControlSuspended;
		}


		public static boolean isRefStdFeedBackControlAlreadyReceived() {
			return refStdFeedBackControlAlreadyReceived;
		}


		public static void setRefStdFeedBackControlAlreadyReceived(boolean feedBackControlAlreadyReceived) {
			refStdFeedBackControlAlreadyReceived = feedBackControlAlreadyReceived;
		}

		public String getPresentMeterType() {
			return presentMeterType;
		}

		public void setPresentMeterType(String presentMeterType) {
			this.presentMeterType = presentMeterType;
		}

		public float getLastRefStdFeedBackRead_R_PhaseCurrent() {
			return lastRefStdFeedBackRead_R_PhaseCurrent;
		}

		public void setLastRefStdFeedBackRead_R_PhaseCurrent(float lastRefStdFeedBackRead_R_PhaseCurrent) {
			this.lastRefStdFeedBackRead_R_PhaseCurrent = lastRefStdFeedBackRead_R_PhaseCurrent;
		}

		public boolean isLastPowerSource_R_CurrentIncrement() {
			return lastPowerSource_R_CurrentIncrement;
		}

		public void setLastPowerSource_R_CurrentIncrement(boolean lastPowerSource_R_CurrentIncrement) {
			this.lastPowerSource_R_CurrentIncrement = lastPowerSource_R_CurrentIncrement;
		}

		public boolean isLastPowerSource_R_CurrentDecrement() {
			return lastPowerSource_R_CurrentDecrement;
		}

		public void setLastPowerSource_R_CurrentDecrement(boolean lastPowerSource_R_CurrentDecrement) {
			this.lastPowerSource_R_CurrentDecrement = lastPowerSource_R_CurrentDecrement;
		}

		public float getLastRefStdFeedBackRead_Y_PhaseCurrent() {
			return lastRefStdFeedBackRead_Y_PhaseCurrent;
		}

		public void setLastRefStdFeedBackRead_Y_PhaseCurrent(float lastRefStdFeedBackRead_Y_PhaseCurrent) {
			this.lastRefStdFeedBackRead_Y_PhaseCurrent = lastRefStdFeedBackRead_Y_PhaseCurrent;
		}

		public boolean isLastPowerSource_Y_CurrentIncrement() {
			return lastPowerSource_Y_CurrentIncrement;
		}

		public void setLastPowerSource_Y_CurrentIncrement(boolean lastPowerSource_Y_CurrentIncrement) {
			this.lastPowerSource_Y_CurrentIncrement = lastPowerSource_Y_CurrentIncrement;
		}

		public boolean isLastPowerSource_Y_CurrentDecrement() {
			return lastPowerSource_Y_CurrentDecrement;
		}

		public void setLastPowerSource_Y_CurrentDecrement(boolean lastPowerSource_Y_CurrentDecrement) {
			this.lastPowerSource_Y_CurrentDecrement = lastPowerSource_Y_CurrentDecrement;
		}

		public float getLastRefStdFeedBackRead_B_PhaseCurrent() {
			return lastRefStdFeedBackRead_B_PhaseCurrent;
		}

		public void setLastRefStdFeedBackRead_B_PhaseCurrent(float lastRefStdFeedBackRead_B_PhaseCurrent) {
			this.lastRefStdFeedBackRead_B_PhaseCurrent = lastRefStdFeedBackRead_B_PhaseCurrent;
		}

		public boolean isLastPowerSource_B_CurrentIncrement() {
			return lastPowerSource_B_CurrentIncrement;
		}

		public void setLastPowerSource_B_CurrentIncrement(boolean lastPowerSource_B_CurrentIncrement) {
			this.lastPowerSource_B_CurrentIncrement = lastPowerSource_B_CurrentIncrement;
		}

		public boolean isLastPowerSource_B_CurrentDecrement() {
			return lastPowerSource_B_CurrentDecrement;
		}

		public void setLastPowerSource_B_CurrentDecrement(boolean lastPowerSource_B_CurrentDecrement) {
			this.lastPowerSource_B_CurrentDecrement = lastPowerSource_B_CurrentDecrement;
		}


		public static void clearEnergyFlowModeList() {
			energyFlowModeList.clear();;
		}

		public static List<String> getEnergyFlowModeList() {
			return energyFlowModeList;
		}

		public static void setEnergyFlowModeList(List<String> energyFlowModeList) {
			ProjectExecutionController.energyFlowModeList = energyFlowModeList;
		}

		public boolean isLastExecutedTestPointContainedHarmonics_Slave() {
			return lastExecutedTestPointContainedHarmonics_Slave;
		}

		public void setLastExecutedTestPointContainedHarmonics_Slave(boolean lastExecutedTestPointContainedHarmonics_Slave) {
			this.lastExecutedTestPointContainedHarmonics_Slave = lastExecutedTestPointContainedHarmonics_Slave;
		}

		public static boolean isLastExecutedTestPointContainedHarmonics_Master() {
			return lastExecutedTestPointContainedHarmonics_Master;
		}

		public static void setLastExecutedTestPointContainedHarmonics_Master(boolean lastExecutedTestPointContainedHarmonics) {
			ProjectExecutionController.lastExecutedTestPointContainedHarmonics_Master = lastExecutedTestPointContainedHarmonics;
		}

		public static boolean isFirstExecutionTestPointWithHarmonics() {
			return firstExecutionTestPointWithHarmonics;
		}

		public static void setFirstExecutionTestPointWithHarmonics(boolean firstExecutionTestPointWithHarmonics) {
			ProjectExecutionController.firstExecutionTestPointWithHarmonics = firstExecutionTestPointWithHarmonics;
		}

		public static boolean isSecondthExecutionTestPointWithBreakWithHarmonics() {
			return secondthExecutionTestPointWithBreakWithHarmonics;
		}

		public static void setSecondthExecutionTestPointWithBreakWithHarmonics(
				boolean secondthExecutionTestPointWithBreakWithHarmonics) {
			ProjectExecutionController.secondthExecutionTestPointWithBreakWithHarmonics = secondthExecutionTestPointWithBreakWithHarmonics;
		}

		public float getLastRefStdFeedBackRead_Y_PhaseVolt() {
			return lastRefStdFeedBackRead_Y_PhaseVolt;
		}

		public void setLastRefStdFeedBackRead_Y_PhaseVolt(float lastRefStdFeedBackRead_Y_PhaseVolt) {
			this.lastRefStdFeedBackRead_Y_PhaseVolt = lastRefStdFeedBackRead_Y_PhaseVolt;
		}

		public float getLastRefStdFeedBackRead_R_PhaseVolt() {
			return lastRefStdFeedBackRead_R_PhaseVolt;
		}

		public void setLastRefStdFeedBackRead_R_PhaseVolt(float lastRefStdFeedBackRead_R_PhaseVolt) {
			this.lastRefStdFeedBackRead_R_PhaseVolt = lastRefStdFeedBackRead_R_PhaseVolt;
		}

		public float getLastRefStdFeedBackRead_B_PhaseVolt() {
			return lastRefStdFeedBackRead_B_PhaseVolt;
		}

		public void setLastRefStdFeedBackRead_B_PhaseVolt(float lastRefStdFeedBackRead_B_PhaseVolt) {
			this.lastRefStdFeedBackRead_B_PhaseVolt = lastRefStdFeedBackRead_B_PhaseVolt;
		}

		public static List<Boolean> getAutoDeployEnabledList() {
			return autoDeployEnabledList;
		}

		public static void setAutoDeployEnabledList(List<Boolean> autoDeployEnabledList) {
			ProjectExecutionController.autoDeployEnabledList = autoDeployEnabledList;
		}

		public static boolean isCurrentProjectAutoDeployEnabled() {
			return currentProjectAutoDeployEnabled;
		}

		public static void setCurrentProjectAutoDeployEnabled(boolean currentProjectAutoDeployEnabled) {
			ProjectExecutionController.currentProjectAutoDeployEnabled = currentProjectAutoDeployEnabled;
		}

		public PowerSourceDirector getPowerSourceDirector() {
			return powerSourceDirector;
		}

		public void setPowerSourceDirector(PowerSourceDirector powerSourceDirector) {
			this.powerSourceDirector = powerSourceDirector;
		}

		public static Map<String, Boolean> getDeviceMountedMap() {
			return deviceMountedMap;
		}

		public static void setDeviceMountedMap(Map<String, Boolean> deviceMountedMap) {
			ProjectExecutionController.deviceMountedMap = deviceMountedMap;
		}

		public static String getFeedbackR_powerFactor() {
			return FeedbackR_powerFactor;
		}

		public static void setFeedbackR_powerFactor(String feedbackR_powerFactor) {
			FeedbackR_powerFactor = feedbackR_powerFactor;
		}

		public static String getFeedbackR_activePower() {
			return FeedbackR_activePower;
		}

		public static void setFeedbackR_activePower(String feedbackR_activePower) {
			FeedbackR_activePower = feedbackR_activePower;
		}

		public static TestPointStatus getPresentTestPointStatus() {
			return presentTestPointStatus;
		}

		public static void setPresentTestPointStatus(TestPointStatus presentTestPointStatus) {
			ProjectExecutionController.presentTestPointStatus = presentTestPointStatus;
		}		
		
		public static void resetPresentTestPointStatus() {
			presentTestPointStatus = new TestPointStatus();
		}
		
		public static Map<String, String> getRemoteTestPointStatusMap() {
			return remoteTestPointStatusMap;
		}

		public static void setRemoteTestPointStatusMap(Map<String, String> remoteTestPointStatusMap) {
			ProjectExecutionController.remoteTestPointStatusMap = remoteTestPointStatusMap;
		}

		public static String getPresentProjectRunId() {
			return presentProjectRunId;
		}

		public void setPresentProjectRunId(String presentProjectRunId) {
			ProjectExecutionController.presentProjectRunId = presentProjectRunId;
		}
		
		
		public static boolean isDutCalibrationVoltageTargetSet() {
			return dutCalibrationVoltageTargetSet;
		}

		public static void setDutCalibrationVoltageTargetSet(boolean dutCalibrationVoltageTargetSet) {
			ProjectExecutionController.dutCalibrationVoltageTargetSet = dutCalibrationVoltageTargetSet;
		}

		public static boolean isDutCalibrationCurrentTargetSet() {
			return dutCalibrationCurrentTargetSet;
		}

		public static void setDutCalibrationCurrentTargetSet(boolean dutCalibrationCurrentTargetSet) {
			ProjectExecutionController.dutCalibrationCurrentTargetSet = dutCalibrationCurrentTargetSet;
		}

		public static boolean isDutCalibrationVoltCurrentSetZero() {
			return dutCalibrationVoltCurrentSetZero;
		}

		public static void setDutCalibrationVoltCurrentSetZero(boolean dutCalibrationVoltCurrentSetZero) {
			ProjectExecutionController.dutCalibrationVoltCurrentSetZero = dutCalibrationVoltCurrentSetZero;
		}

		public static boolean isDutCalibrationCurrentZeroSet() {
			return dutCalibrationCurrentZeroSet;
		}

		public static void setDutCalibrationCurrentZeroSet(boolean dutCalibrationCurrentZeroSet) {
			ProjectExecutionController.dutCalibrationCurrentZeroSet = dutCalibrationCurrentZeroSet;
		}



	}

