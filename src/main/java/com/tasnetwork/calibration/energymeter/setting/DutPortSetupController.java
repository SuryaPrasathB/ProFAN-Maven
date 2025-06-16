package com.tasnetwork.calibration.energymeter.setting;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import com.tasnetwork.calibration.energymeter.ApplicationHomeController;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantDut;
import com.tasnetwork.calibration.energymeter.constant.ConstantPowerSourceLscs;
import com.tasnetwork.calibration.energymeter.constant.ConstantPowerSourceMte;
import com.tasnetwork.calibration.energymeter.constant.ConstantRefStdRadiant;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.database.MySQL_Controller;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.device.DutSerialDataManager;
import com.tasnetwork.calibration.energymeter.device.SerialDataManager;
import com.tasnetwork.calibration.energymeter.uac.UacDataModel;

import gnu.io.CommPortIdentifier;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import org.json.simple.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

public class DutPortSetupController implements Initializable{

/*    @FXML
    private ComboBox<Integer> cmbBxPowerSrcBaudRate;
    @FXML
    private ComboBox<Integer> cmbBxRefStdBaudRate;*/
    
    @FXML
    private Button btn_Save;
    private static Button ref_btn_Save;
    
    //@FXML
    //private Button btnValidatePwrSrcCmd;
    //private static Button ref_btnValidatePwrSrcCmd;
    
    //@FXML
    //private Button btnValidateRefStdCmd;
    //private static Button ref_btnValidateRefStdCmd;
    
    
    
    
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate1;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate2;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate3;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate4;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate5;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate6;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate7; 
    
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate8;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate9;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate10;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate11;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate12;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate13;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate14;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate15;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate16;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate17;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate18;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate19;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate20;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate21;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate22;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate23;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate24;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate25;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate26;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate27;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate28;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate29;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate30;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate31;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate32;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate33;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate34;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate35;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate36;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate37;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate38;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate39;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate40;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate41;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate42;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate43;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate44;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate45;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate46;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate47;
    @FXML    private ComboBox<Integer> cmbBxDUT_BaudRate48;
    
    /*@FXML
    private ComboBox<String> cmbBxPowerSrcPortSelection;
    @FXML
    private ComboBox<String> cmbBxRefStdPortSelection;*/
    
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection1;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection2;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection3;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection4;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection5;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection6;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection7; 
    
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection8;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection9;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection10;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection11;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection12;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection13;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection14;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection15;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection16;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection17;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection18;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection19;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection20;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection21;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection22;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection23;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection24;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection25;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection26;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection27;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection28;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection29;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection30;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection31;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection32;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection33;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection34;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection35;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection36;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection37;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection38;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection39;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection40;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection41;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection42;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection43;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection44;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection45;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection46;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection47;
    @FXML    private ComboBox<String> cmbBxDUT_PortSelection48;
    
    //@FXML
	// private ComboBox<String> cmbBxPowerSource_ModelName;
    // @FXML
    //private ComboBox<String> cmbBxReferanceStd_ModelName;
    
    @FXML    private ComboBox<String> cmbBxDUT_ModelName1;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName2;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName3;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName4;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName5;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName6;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName7;
    
    @FXML    private ComboBox<String> cmbBxDUT_ModelName8;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName9;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName10;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName11;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName12;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName13;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName14;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName15;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName16;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName17;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName18;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName19;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName20;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName21;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName22;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName23;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName24;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName25;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName26;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName27;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName28;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName29;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName30;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName31;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName32;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName33;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName34;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName35;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName36;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName37;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName38;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName39;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName40;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName41;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName42;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName43;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName44;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName45;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName46;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName47;
    @FXML    private ComboBox<String> cmbBxDUT_ModelName48;




    
/*    @FXML
    private TextField txtValidatePwrSrcCmdStatus;
    @FXML
    private TextField txtValidateRefStdCmdStatus;*/
    
    @FXML    private TextField txtValidateDUT_CmdStatus1;
    @FXML    private TextField txtValidateDUT_CmdStatus2;
    @FXML    private TextField txtValidateDUT_CmdStatus3;
    @FXML    private TextField txtValidateDUT_CmdStatus4;
    @FXML    private TextField txtValidateDUT_CmdStatus5;
    @FXML    private TextField txtValidateDUT_CmdStatus6;
    @FXML    private TextField txtValidateDUT_CmdStatus7;
    
    @FXML    private TextField txtValidateDUT_CmdStatus8;
    @FXML    private TextField txtValidateDUT_CmdStatus9;
    @FXML    private TextField txtValidateDUT_CmdStatus10;
    @FXML    private TextField txtValidateDUT_CmdStatus11;
    @FXML    private TextField txtValidateDUT_CmdStatus12;
    @FXML    private TextField txtValidateDUT_CmdStatus13;
    @FXML    private TextField txtValidateDUT_CmdStatus14;
    @FXML    private TextField txtValidateDUT_CmdStatus15;
    @FXML    private TextField txtValidateDUT_CmdStatus16;
    @FXML    private TextField txtValidateDUT_CmdStatus17;
    @FXML    private TextField txtValidateDUT_CmdStatus18;
    @FXML    private TextField txtValidateDUT_CmdStatus19;
    @FXML    private TextField txtValidateDUT_CmdStatus20;
    @FXML    private TextField txtValidateDUT_CmdStatus21;
    @FXML    private TextField txtValidateDUT_CmdStatus22;
    @FXML    private TextField txtValidateDUT_CmdStatus23;
    @FXML    private TextField txtValidateDUT_CmdStatus24;
    @FXML    private TextField txtValidateDUT_CmdStatus25;
    @FXML    private TextField txtValidateDUT_CmdStatus26;
    @FXML    private TextField txtValidateDUT_CmdStatus27;
    @FXML    private TextField txtValidateDUT_CmdStatus28;
    @FXML    private TextField txtValidateDUT_CmdStatus29;
    @FXML    private TextField txtValidateDUT_CmdStatus30;
    @FXML    private TextField txtValidateDUT_CmdStatus31;
    @FXML    private TextField txtValidateDUT_CmdStatus32;
    @FXML    private TextField txtValidateDUT_CmdStatus33;
    @FXML    private TextField txtValidateDUT_CmdStatus34;
    @FXML    private TextField txtValidateDUT_CmdStatus35;
    @FXML    private TextField txtValidateDUT_CmdStatus36;
    @FXML    private TextField txtValidateDUT_CmdStatus37;
    @FXML    private TextField txtValidateDUT_CmdStatus38;
    @FXML    private TextField txtValidateDUT_CmdStatus39;
    @FXML    private TextField txtValidateDUT_CmdStatus40;
    @FXML    private TextField txtValidateDUT_CmdStatus41;
    @FXML    private TextField txtValidateDUT_CmdStatus42;
    @FXML    private TextField txtValidateDUT_CmdStatus43;
    @FXML    private TextField txtValidateDUT_CmdStatus44;
    @FXML    private TextField txtValidateDUT_CmdStatus45;
    @FXML    private TextField txtValidateDUT_CmdStatus46;
    @FXML    private TextField txtValidateDUT_CmdStatus47;
    @FXML    private TextField txtValidateDUT_CmdStatus48;
    
/*    @FXML
    private Button btnValidatePwrSrcCmd;
    @FXML
    private Button btnValidateRefStdCmd;*/
    

    
    @FXML    private Button btnValidateDUT_Cmd1;
    @FXML    private Button btnValidateDUT_Cmd2;
    @FXML    private Button btnValidateDUT_Cmd3;
    @FXML    private Button btnValidateDUT_Cmd4;
    @FXML    private Button btnValidateDUT_Cmd5;
    @FXML    private Button btnValidateDUT_Cmd6;
    @FXML    private Button btnValidateDUT_Cmd7;
    @FXML    private Button btnValidateDUT_Cmd8;
    @FXML    private Button btnValidateDUT_Cmd9;
    @FXML    private Button btnValidateDUT_Cmd10;
    @FXML    private Button btnValidateDUT_Cmd11;
    @FXML    private Button btnValidateDUT_Cmd12;
    @FXML    private Button btnValidateDUT_Cmd13;
    @FXML    private Button btnValidateDUT_Cmd14;
    @FXML    private Button btnValidateDUT_Cmd15;
    @FXML    private Button btnValidateDUT_Cmd16;
    @FXML    private Button btnValidateDUT_Cmd17;
    @FXML    private Button btnValidateDUT_Cmd18;
    @FXML    private Button btnValidateDUT_Cmd19;
    @FXML    private Button btnValidateDUT_Cmd20;
    @FXML    private Button btnValidateDUT_Cmd21;
    @FXML    private Button btnValidateDUT_Cmd22;
    @FXML    private Button btnValidateDUT_Cmd23;
    @FXML    private Button btnValidateDUT_Cmd24;
    @FXML    private Button btnValidateDUT_Cmd25;
    @FXML    private Button btnValidateDUT_Cmd26;
    @FXML    private Button btnValidateDUT_Cmd27;
    @FXML    private Button btnValidateDUT_Cmd28;
    @FXML    private Button btnValidateDUT_Cmd29;
    @FXML    private Button btnValidateDUT_Cmd30;
    @FXML    private Button btnValidateDUT_Cmd31;
    @FXML    private Button btnValidateDUT_Cmd32;
    @FXML    private Button btnValidateDUT_Cmd33;
    @FXML    private Button btnValidateDUT_Cmd34;
    @FXML    private Button btnValidateDUT_Cmd35;
    @FXML    private Button btnValidateDUT_Cmd36;
    @FXML    private Button btnValidateDUT_Cmd37;
    @FXML    private Button btnValidateDUT_Cmd38;
    @FXML    private Button btnValidateDUT_Cmd39;
    @FXML    private Button btnValidateDUT_Cmd40;
    @FXML    private Button btnValidateDUT_Cmd41;
    @FXML    private Button btnValidateDUT_Cmd42;
    @FXML    private Button btnValidateDUT_Cmd43;
    @FXML    private Button btnValidateDUT_Cmd44;
    @FXML    private Button btnValidateDUT_Cmd45;
    @FXML    private Button btnValidateDUT_Cmd46;
    @FXML    private Button btnValidateDUT_Cmd47;
    @FXML    private Button btnValidateDUT_Cmd48;
    
    private static Button ref_btnValidateDUT_Cmd1;
    private static Button ref_btnValidateDUT_Cmd2;
    private static Button ref_btnValidateDUT_Cmd3;
    private static Button ref_btnValidateDUT_Cmd4;
    private static Button ref_btnValidateDUT_Cmd5;
    private static Button ref_btnValidateDUT_Cmd6;
    private static Button ref_btnValidateDUT_Cmd7;
    private static Button ref_btnValidateDUT_Cmd8;
    private static Button ref_btnValidateDUT_Cmd9;
    private static Button ref_btnValidateDUT_Cmd10;
    private static Button ref_btnValidateDUT_Cmd11;
    private static Button ref_btnValidateDUT_Cmd12;
    private static Button ref_btnValidateDUT_Cmd13;
    private static Button ref_btnValidateDUT_Cmd14;
    private static Button ref_btnValidateDUT_Cmd15;
    private static Button ref_btnValidateDUT_Cmd16;
    private static Button ref_btnValidateDUT_Cmd17;
    private static Button ref_btnValidateDUT_Cmd18;
    private static Button ref_btnValidateDUT_Cmd19;
    private static Button ref_btnValidateDUT_Cmd20;
    private static Button ref_btnValidateDUT_Cmd21;
    private static Button ref_btnValidateDUT_Cmd22;
    private static Button ref_btnValidateDUT_Cmd23;
    private static Button ref_btnValidateDUT_Cmd24;
    private static Button ref_btnValidateDUT_Cmd25;
    private static Button ref_btnValidateDUT_Cmd26;
    private static Button ref_btnValidateDUT_Cmd27;
    private static Button ref_btnValidateDUT_Cmd28;
    private static Button ref_btnValidateDUT_Cmd29;
    private static Button ref_btnValidateDUT_Cmd30;
    private static Button ref_btnValidateDUT_Cmd31;
    private static Button ref_btnValidateDUT_Cmd32;
    private static Button ref_btnValidateDUT_Cmd33;
    private static Button ref_btnValidateDUT_Cmd34;
    private static Button ref_btnValidateDUT_Cmd35;
    private static Button ref_btnValidateDUT_Cmd36;
    private static Button ref_btnValidateDUT_Cmd37;
    private static Button ref_btnValidateDUT_Cmd38;
    private static Button ref_btnValidateDUT_Cmd39;
    private static Button ref_btnValidateDUT_Cmd40;    
    private static Button ref_btnValidateDUT_Cmd41;
    private static Button ref_btnValidateDUT_Cmd42;
    private static Button ref_btnValidateDUT_Cmd43;
    private static Button ref_btnValidateDUT_Cmd44;
    private static Button ref_btnValidateDUT_Cmd45;
    private static Button ref_btnValidateDUT_Cmd46;
    private static Button ref_btnValidateDUT_Cmd47;
    private static Button ref_btnValidateDUT_Cmd48;
    
    Timer PwrSrcValidateTimer;
    Timer RefStdValidateTimer;
    Timer DUT1_ValidateTimer;
    Timer DUT2_ValidateTimer;
    Timer DUT3_ValidateTimer;
    Timer DUT4_ValidateTimer;
    Timer DUT5_ValidateTimer;
    Timer DUT6_ValidateTimer;
    Timer DUT7_ValidateTimer;
    Timer DUT8_ValidateTimer;
    Timer DUT9_ValidateTimer;
    Timer DUT10_ValidateTimer;
    
    Timer DUT11_ValidateTimer;
    Timer DUT12_ValidateTimer;
    Timer DUT13_ValidateTimer;
    Timer DUT14_ValidateTimer;
    Timer DUT15_ValidateTimer;
    Timer DUT16_ValidateTimer;
    Timer DUT17_ValidateTimer;
    Timer DUT18_ValidateTimer;
    Timer DUT19_ValidateTimer;
    Timer DUT20_ValidateTimer;
    
    Timer DUT21_ValidateTimer;
    Timer DUT22_ValidateTimer;
    Timer DUT23_ValidateTimer;
    Timer DUT24_ValidateTimer;
    Timer DUT25_ValidateTimer;
    Timer DUT26_ValidateTimer;
    Timer DUT27_ValidateTimer;
    Timer DUT28_ValidateTimer;
    Timer DUT29_ValidateTimer;
    Timer DUT30_ValidateTimer;
    
    Timer DUT31_ValidateTimer;
    Timer DUT32_ValidateTimer;
    Timer DUT33_ValidateTimer;
    Timer DUT34_ValidateTimer;
    Timer DUT35_ValidateTimer;
    Timer DUT36_ValidateTimer;
    Timer DUT37_ValidateTimer;
    Timer DUT38_ValidateTimer;
    Timer DUT39_ValidateTimer;
    Timer DUT40_ValidateTimer;
    
    Timer DUT41_ValidateTimer;
    Timer DUT42_ValidateTimer;
    Timer DUT43_ValidateTimer;
    Timer DUT44_ValidateTimer;
    Timer DUT45_ValidateTimer;
    Timer DUT46_ValidateTimer;
    Timer DUT47_ValidateTimer;
    Timer DUT48_ValidateTimer;
    
    @FXML private GridPane gridDut ;
    static private GridPane ref_gridDut ;
    
    private static HashMap FXML_PortMap = new HashMap();
    
    private static  boolean PortValidationTurnedON = false;
    
    Timer UI_DisplayTimer = new Timer();
    UI_DisplayTimerTask UI_DisplayTimerTaskObj;
    
    MyRunnable myRunnable;
    Thread myRunnableThread;
    
    public DutSerialDataManager dutSerialDM_Obj = new DutSerialDataManager();
   // public SerialDataManager DisplayDataObj = new SerialDataManager();
    DeviceDataManagerController DisplayDataObj =  new DeviceDataManagerController();
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	
    	ref_assignment();
/*    	Platform.runLater(() -> {
		enableBusyLoadingScreen();
		});*/
    	initializeComPorts( );

/*    	UI_DisplayTimerTaskObj = new UI_DisplayTimerTask(txtValidateRefStdCmdStatus);
    	myRunnable = new MyRunnable(txtValidateRefStdCmdStatus);*/
    	disableGuiObjects();
    	SerialDataManager.setSkipCurrentTP_Execution(false);
    	//disableBusyLoadingScreen();
    	if(ProcalFeatureEnable.USER_ACCESS_CONTROL_ENABLED){
			applyUacSettings();
		}
    	
    	Platform.runLater(() -> {
			for(int i= ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK ; i < 48; i++) {
	    		//gridPaneDutSettings.getChildren().remove(i);
	    		//gridPaneDutSettings.getRowConstraints().remove(i);
				hideRow(ref_gridDut,i);
	    	}
		});
    }
    
    
/*    public void hideRow(GridPane gridPane, int rowIndex) {
        for (Node child : gridPane.getChildren()) {
            Integer row = GridPane.getRowIndex(child);
            if (row != null && row == rowIndex) {
                child.setVisible(false);
                child.setManaged(false); // prevents layout space reservation
            }
        }
    }*/
    
    public void hideRow(GridPane gridPane, int rowIndex) {
        if (gridPane == null) {
            System.err.println("GridPane is null");
            return;
        }

        for (Node child : gridPane.getChildren()) {
            Integer row = GridPane.getRowIndex(child);
            if (row == null) row = 0; // Default to row 0 if not explicitly set

            if (row == rowIndex) {
                child.setVisible(false);
                child.setManaged(false);
            }
        }
    }
    
    private static void applyUacSettings() {
		// TODO Auto-generated method stub
		ApplicationLauncher.logger.info("DutPortSetupController : applyUacSettings :  Entry");
		ArrayList<UacDataModel> uacSelectProfileScreenList = DeviceDataManagerController.getUacSelectProfileScreenList();
		String screenName = "";
		for (int i = 0; i < uacSelectProfileScreenList.size(); i++){

			screenName = uacSelectProfileScreenList.get(i).getScreenName();
			switch (screenName) {
				case ConstantApp.UAC_DEVICE_SETTINGS_SCREEN:
					
					
					if(!uacSelectProfileScreenList.get(i).getExecutePossible()){
						//ref_btn_deploy.setDisable(true);
						ref_btnValidateDUT_Cmd1.setDisable(true);
						ref_btnValidateDUT_Cmd2.setDisable(true);
						ref_btnValidateDUT_Cmd3.setDisable(true);
						ref_btnValidateDUT_Cmd4.setDisable(true);
						ref_btnValidateDUT_Cmd5.setDisable(true);
						ref_btnValidateDUT_Cmd6.setDisable(true);
						ref_btnValidateDUT_Cmd7.setDisable(true);
						ref_btnValidateDUT_Cmd8.setDisable(true);
						ref_btnValidateDUT_Cmd9.setDisable(true);
						ref_btnValidateDUT_Cmd10.setDisable(true);
						ref_btnValidateDUT_Cmd11.setDisable(true);
						ref_btnValidateDUT_Cmd12.setDisable(true);
						ref_btnValidateDUT_Cmd13.setDisable(true);
						ref_btnValidateDUT_Cmd14.setDisable(true);
						ref_btnValidateDUT_Cmd15.setDisable(true);
						ref_btnValidateDUT_Cmd16.setDisable(true);
						ref_btnValidateDUT_Cmd17.setDisable(true);
						ref_btnValidateDUT_Cmd18.setDisable(true);
						ref_btnValidateDUT_Cmd19.setDisable(true);
						ref_btnValidateDUT_Cmd20.setDisable(true);
						ref_btnValidateDUT_Cmd21.setDisable(true);
						ref_btnValidateDUT_Cmd22.setDisable(true);
						ref_btnValidateDUT_Cmd23.setDisable(true);
						ref_btnValidateDUT_Cmd24.setDisable(true);
						ref_btnValidateDUT_Cmd25.setDisable(true);
						ref_btnValidateDUT_Cmd26.setDisable(true);
						ref_btnValidateDUT_Cmd27.setDisable(true);
						ref_btnValidateDUT_Cmd28.setDisable(true);
						ref_btnValidateDUT_Cmd29.setDisable(true);
						ref_btnValidateDUT_Cmd30.setDisable(true);
						ref_btnValidateDUT_Cmd31.setDisable(true);
						ref_btnValidateDUT_Cmd32.setDisable(true);
						ref_btnValidateDUT_Cmd33.setDisable(true);
						ref_btnValidateDUT_Cmd34.setDisable(true);
						ref_btnValidateDUT_Cmd35.setDisable(true);
						ref_btnValidateDUT_Cmd36.setDisable(true);
						ref_btnValidateDUT_Cmd37.setDisable(true);
						ref_btnValidateDUT_Cmd38.setDisable(true);
						ref_btnValidateDUT_Cmd39.setDisable(true);
						ref_btnValidateDUT_Cmd40.setDisable(true);
						ref_btnValidateDUT_Cmd41.setDisable(true);
						ref_btnValidateDUT_Cmd42.setDisable(true);
						ref_btnValidateDUT_Cmd43.setDisable(true);
						ref_btnValidateDUT_Cmd44.setDisable(true);
						ref_btnValidateDUT_Cmd45.setDisable(true);
						ref_btnValidateDUT_Cmd46.setDisable(true);
						ref_btnValidateDUT_Cmd47.setDisable(true);
						ref_btnValidateDUT_Cmd48.setDisable(true);
						
					}
					
					if(!uacSelectProfileScreenList.get(i).getAddPossible()){
						//ref_btn_Create.setDisable(true);
						
					}
					
					if(!uacSelectProfileScreenList.get(i).getUpdatePossible()){
						//ref_vbox_testscript.setDisable(true);sdvsc
						//setChildPropertySaveEnabled(false);
						ref_btn_Save.setDisable(true);
						
						
					}
					
					if(!uacSelectProfileScreenList.get(i).getDeletePossible()){
						//ref_btn_Delete.setDisable(true);
						
					}
					break;
					
								
	
				default:
					break;
			}
			
				
				
		}
	}
    
    public void disableGuiObjects() {
    	
    	for(int i = (ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK +1); i <= ProcalFeatureEnable.RACK_MAX_POSITION; i++){
    		switch (i){
    		
	    		case 1:
	    			ref_btnValidateDUT_Cmd1.setDisable(true);
	    			break;
	    		case 2:
	    			ref_btnValidateDUT_Cmd2.setDisable(true);
	    			break;
	    		case 3:
	    			ref_btnValidateDUT_Cmd3.setDisable(true);
	    			break;
	    		case 4:
	    			ref_btnValidateDUT_Cmd4.setDisable(true);
	    			break;
	    		case 5:
	    			ref_btnValidateDUT_Cmd5.setDisable(true);
	    			break;
	    		case 6:
	    			ref_btnValidateDUT_Cmd6.setDisable(true);
	    			break;	    			
	    		case 7:
	    			ref_btnValidateDUT_Cmd7.setDisable(true);
	    			break;
	    		case 8:
	    			ref_btnValidateDUT_Cmd8.setDisable(true);
	    			break;
	    		case 9:
	    			ref_btnValidateDUT_Cmd9.setDisable(true);
	    			break;
	    		case 10:
	    			ref_btnValidateDUT_Cmd10.setDisable(true);
	    			break;
	    			
	    		case 11:
	    			ref_btnValidateDUT_Cmd11.setDisable(true);
	    			break;
	    		case 12:
	    			ref_btnValidateDUT_Cmd12.setDisable(true);
	    			break;
	    		case 13:
	    			ref_btnValidateDUT_Cmd13.setDisable(true);
	    			break;
	    		case 14:
	    			ref_btnValidateDUT_Cmd14.setDisable(true);
	    			break;
	    		case 15:
	    			ref_btnValidateDUT_Cmd15.setDisable(true);
	    			break;
	    		case 16:
	    			ref_btnValidateDUT_Cmd16.setDisable(true);
	    			break;	    			
	    		case 17:
	    			ref_btnValidateDUT_Cmd17.setDisable(true);
	    			break;
	    		case 18:
	    			ref_btnValidateDUT_Cmd18.setDisable(true);
	    			break;
	    		case 19:
	    			ref_btnValidateDUT_Cmd19.setDisable(true);
	    			break;
	    		case 20:
	    			ref_btnValidateDUT_Cmd20.setDisable(true);
	    			break;
	    			
	    		case 21:
	    			ref_btnValidateDUT_Cmd21.setDisable(true);
	    			break;
	    		case 22:
	    			ref_btnValidateDUT_Cmd22.setDisable(true);
	    			break;
	    		case 23:
	    			ref_btnValidateDUT_Cmd23.setDisable(true);
	    			break;
	    		case 24:
	    			ref_btnValidateDUT_Cmd24.setDisable(true);
	    			break;
	    		case 25:
	    			ref_btnValidateDUT_Cmd25.setDisable(true);
	    			break;
	    		case 26:
	    			ref_btnValidateDUT_Cmd26.setDisable(true);
	    			break;	    			
	    		case 27:
	    			ref_btnValidateDUT_Cmd27.setDisable(true);
	    			break;
	    		case 28:
	    			ref_btnValidateDUT_Cmd28.setDisable(true);
	    			break;
	    		case 29:
	    			ref_btnValidateDUT_Cmd29.setDisable(true);
	    			break;
	    		case 30:
	    			ref_btnValidateDUT_Cmd30.setDisable(true);
	    			break;
	    			
	    		case 31:
	    			ref_btnValidateDUT_Cmd31.setDisable(true);
	    			break;
	    		case 32:
	    			ref_btnValidateDUT_Cmd32.setDisable(true);
	    			break;
	    		case 33:
	    			ref_btnValidateDUT_Cmd33.setDisable(true);
	    			break;
	    		case 34:
	    			ref_btnValidateDUT_Cmd34.setDisable(true);
	    			break;
	    		case 35:
	    			ref_btnValidateDUT_Cmd35.setDisable(true);
	    			break;
	    		case 36:
	    			ref_btnValidateDUT_Cmd36.setDisable(true);
	    			break;	    			
	    		case 37:
	    			ref_btnValidateDUT_Cmd37.setDisable(true);
	    			break;
	    		case 38:
	    			ref_btnValidateDUT_Cmd38.setDisable(true);
	    			break;
	    		case 39:
	    			ref_btnValidateDUT_Cmd39.setDisable(true);
	    			break;
	    		case 40:
	    			ref_btnValidateDUT_Cmd40.setDisable(true);
	    			break;
	    		
	    		default:
	    			break;
    		}
    		
    	}
    	
    }
    public void ref_assignment() {
    	ref_btnValidateDUT_Cmd1	 = 	btnValidateDUT_Cmd1;
    	ref_btnValidateDUT_Cmd2	 = 	btnValidateDUT_Cmd2;
    	ref_btnValidateDUT_Cmd3	 = 	btnValidateDUT_Cmd3;
    	ref_btnValidateDUT_Cmd4	 = 	btnValidateDUT_Cmd4;
    	ref_btnValidateDUT_Cmd5	 = 	btnValidateDUT_Cmd5;
    	ref_btnValidateDUT_Cmd6	 = 	btnValidateDUT_Cmd6;
    	ref_btnValidateDUT_Cmd7	 = 	btnValidateDUT_Cmd7;
    	ref_btnValidateDUT_Cmd8	 = 	btnValidateDUT_Cmd8;
    	ref_btnValidateDUT_Cmd9	 = 	btnValidateDUT_Cmd9;
    	ref_btnValidateDUT_Cmd10	 = 	btnValidateDUT_Cmd10;
    	ref_btnValidateDUT_Cmd11	 = 	btnValidateDUT_Cmd11;
    	ref_btnValidateDUT_Cmd12	 = 	btnValidateDUT_Cmd12;
    	ref_btnValidateDUT_Cmd13	 = 	btnValidateDUT_Cmd13;
    	ref_btnValidateDUT_Cmd14	 = 	btnValidateDUT_Cmd14;
    	ref_btnValidateDUT_Cmd15	 = 	btnValidateDUT_Cmd15;
    	ref_btnValidateDUT_Cmd16	 = 	btnValidateDUT_Cmd16;
    	ref_btnValidateDUT_Cmd17	 = 	btnValidateDUT_Cmd17;
    	ref_btnValidateDUT_Cmd18	 = 	btnValidateDUT_Cmd18;
    	ref_btnValidateDUT_Cmd19	 = 	btnValidateDUT_Cmd19;
    	ref_btnValidateDUT_Cmd20	 = 	btnValidateDUT_Cmd20;
    	ref_btnValidateDUT_Cmd21	 = 	btnValidateDUT_Cmd21;
    	ref_btnValidateDUT_Cmd22	 = 	btnValidateDUT_Cmd22;
    	ref_btnValidateDUT_Cmd23	 = 	btnValidateDUT_Cmd23;
    	ref_btnValidateDUT_Cmd24	 = 	btnValidateDUT_Cmd24;
    	ref_btnValidateDUT_Cmd25	 = 	btnValidateDUT_Cmd25;
    	ref_btnValidateDUT_Cmd26	 = 	btnValidateDUT_Cmd26;
    	ref_btnValidateDUT_Cmd27	 = 	btnValidateDUT_Cmd27;
    	ref_btnValidateDUT_Cmd28	 = 	btnValidateDUT_Cmd28;
    	ref_btnValidateDUT_Cmd29	 = 	btnValidateDUT_Cmd29;
    	ref_btnValidateDUT_Cmd30	 = 	btnValidateDUT_Cmd30;
    	ref_btnValidateDUT_Cmd31	 = 	btnValidateDUT_Cmd31;
    	ref_btnValidateDUT_Cmd32	 = 	btnValidateDUT_Cmd32;
    	ref_btnValidateDUT_Cmd33	 = 	btnValidateDUT_Cmd33;
    	ref_btnValidateDUT_Cmd34	 = 	btnValidateDUT_Cmd34;
    	ref_btnValidateDUT_Cmd35	 = 	btnValidateDUT_Cmd35;
    	ref_btnValidateDUT_Cmd36	 = 	btnValidateDUT_Cmd36;
    	ref_btnValidateDUT_Cmd37	 = 	btnValidateDUT_Cmd37;
    	ref_btnValidateDUT_Cmd38	 = 	btnValidateDUT_Cmd38;
    	ref_btnValidateDUT_Cmd39	 = 	btnValidateDUT_Cmd39;
    	ref_btnValidateDUT_Cmd40	 = 	btnValidateDUT_Cmd40;
    	
    	ref_btnValidateDUT_Cmd41	 = 	btnValidateDUT_Cmd41;
    	ref_btnValidateDUT_Cmd42	 = 	btnValidateDUT_Cmd42;
    	ref_btnValidateDUT_Cmd43	 = 	btnValidateDUT_Cmd43;
    	ref_btnValidateDUT_Cmd44	 = 	btnValidateDUT_Cmd44;
    	ref_btnValidateDUT_Cmd45	 = 	btnValidateDUT_Cmd45;
    	ref_btnValidateDUT_Cmd46	 = 	btnValidateDUT_Cmd46;
    	ref_btnValidateDUT_Cmd47	 = 	btnValidateDUT_Cmd47;
    	ref_btnValidateDUT_Cmd48	 = 	btnValidateDUT_Cmd48;
    	ref_btn_Save = btn_Save;
    	ref_gridDut = gridDut;
/*    	ref_btnValidatePwrSrcCmd = btnValidatePwrSrcCmd;
    	ref_btnValidateRefStdCmd = btnValidateRefStdCmd;*/
    }
    
    public void initializeComPorts() {
    	
        setupComPortsBaudRate();
        loadAvailableComPorts();
        
        //updatePowerSourceModel();
        //updateReferenceMeterModel();
        updateDUTModel();
        load_saved_device_settings();
    }
    
    public void load_saved_device_settings(){

/*    	JSONObject saved_pwr_setting = MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_POWER_SOURCE);
    	JSONObject saved_ref_setting = MySQL_Controller.sp_getdevice_setting(ConstantApp.SOURCE_TYPE_REF_STD);*/
    	JSONObject saved_dut1_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT1);
    	JSONObject saved_dut2_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT2);
    	JSONObject saved_dut3_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT3);
    	JSONObject saved_dut4_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT4);
    	JSONObject saved_dut5_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT5);
    	JSONObject saved_dut6_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT6);
    	JSONObject saved_dut7_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT7);
    	
    	
    	JSONObject saved_dut8_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT8);
    	JSONObject saved_dut9_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT9);
    	JSONObject saved_dut10_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT10);
    	JSONObject saved_dut11_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT11);
    	JSONObject saved_dut12_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT12);
    	JSONObject saved_dut13_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT13);
    	JSONObject saved_dut14_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT14);
    	JSONObject saved_dut15_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT15);
    	JSONObject saved_dut16_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT16);
    	JSONObject saved_dut17_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT17);
    	JSONObject saved_dut18_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT18);
    	JSONObject saved_dut19_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT19);
    	JSONObject saved_dut20_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT20);
    	JSONObject saved_dut21_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT21);
    	JSONObject saved_dut22_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT22);
    	JSONObject saved_dut23_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT23);
    	JSONObject saved_dut24_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT24);
    	JSONObject saved_dut25_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT25);
    	JSONObject saved_dut26_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT26);
    	JSONObject saved_dut27_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT27);
    	JSONObject saved_dut28_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT28);
    	JSONObject saved_dut29_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT29);
    	JSONObject saved_dut30_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT30);
    	JSONObject saved_dut31_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT31);
    	JSONObject saved_dut32_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT32);
    	JSONObject saved_dut33_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT33);
    	JSONObject saved_dut34_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT34);
    	JSONObject saved_dut35_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT35);
    	JSONObject saved_dut36_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT36);
    	JSONObject saved_dut37_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT37);
    	JSONObject saved_dut38_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT38);
    	JSONObject saved_dut39_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT39);
    	JSONObject saved_dut40_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT40);
    	JSONObject saved_dut41_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT41);
    	JSONObject saved_dut42_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT42);
    	JSONObject saved_dut43_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT43);
    	JSONObject saved_dut44_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT44);
    	JSONObject saved_dut45_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT45);
    	JSONObject saved_dut46_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT46);
    	JSONObject saved_dut47_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT47);
    	JSONObject saved_dut48_setting = MySQL_Controller.sp_getdevice_setting(ConstantDut.SOURCE_TYPE_DUT48);
    	
    	//if(true){//Condition TO BE DEFINED
/*    		try {
    			if(saved_pwr_setting.has("port_name")){
    				cmbBxPowerSrcPortSelection.setValue(saved_pwr_setting.getString("port_name"));
    			} else{
    				
    				cmbBxPowerSrcPortSelection.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: PowerSrcPortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException1:"+e.getMessage());
				cmbBxPowerSrcPortSelection.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: PowerSrcPortSelection: Data not retrieved from database");
			} 	
    		try {
    			if(saved_pwr_setting.has("baud_rate")){
	        	cmbBxPowerSrcBaudRate.setValue(Integer.parseInt(saved_pwr_setting.getString("baud_rate"))); 	
    			} else{
    				cmbBxPowerSrcBaudRate.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: PowerSrcBaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException2:"+e.getMessage());
				cmbBxPowerSrcBaudRate.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: PowerSrcBaudRate: Data not retrieved from database");
			
			} 
    		try {
    			if(saved_ref_setting.has("port_name")){
	        	cmbBxRefStdPortSelection.setValue(saved_ref_setting.getString("port_name")); 	
    			} else{
    				cmbBxRefStdPortSelection.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: RefStdPortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException3:"+e.getMessage());
				cmbBxRefStdPortSelection.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: RefStdPortSelection: Data not retrieved from database");
			
			} 
    		try {
    			if(saved_ref_setting.has("baud_rate")){
	        	cmbBxRefStdBaudRate.setValue(Integer.parseInt(saved_ref_setting.getString("baud_rate"))); 	
    			} else{
    				cmbBxRefStdBaudRate.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: RefStdBaudRate: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException4:"+e.getMessage());
				cmbBxRefStdBaudRate.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: RefStdBaudRate: Data not retrieved from database");
			
			} */
    		try {
    			if(saved_dut1_setting.has("port_name")){
	        	cmbBxDUT_PortSelection1.setValue(saved_dut1_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection1.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT1_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-1:"+e.getMessage());
				cmbBxDUT_PortSelection1.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT1_PortSelection: Data not retrieved from database");
			
			} 
    		
    		
    		    		
    		try {
    			if(saved_dut2_setting.has("port_name")){
	        	cmbBxDUT_PortSelection2.setValue(saved_dut2_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection2.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT2_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-2:"+e.getMessage());
				cmbBxDUT_PortSelection2.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT2_PortSelection: Data not retrieved from database");
			
			} 
    		
    		try {
    			if(saved_dut3_setting.has("port_name")){
	        	cmbBxDUT_PortSelection3.setValue(saved_dut3_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection3.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT3_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-3:"+e.getMessage());
				cmbBxDUT_PortSelection3.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT3_PortSelection: Data not retrieved from database");
			
			} 
    		
    		try {
    			if(saved_dut4_setting.has("port_name")){
	        	cmbBxDUT_PortSelection4.setValue(saved_dut4_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection4.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT4_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-4:"+e.getMessage());
				cmbBxDUT_PortSelection4.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT4_PortSelection: Data not retrieved from database");
			
			} 
    		
    		try {
    			if(saved_dut5_setting.has("port_name")){
	        	cmbBxDUT_PortSelection5.setValue(saved_dut5_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection5.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT5_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-5:"+e.getMessage());
				cmbBxDUT_PortSelection5.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT5_PortSelection: Data not retrieved from database");
			
			} 
    		
    		try {
    			if(saved_dut6_setting.has("port_name")){
	        	cmbBxDUT_PortSelection6.setValue(saved_dut6_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection6.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT6_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-6:"+e.getMessage());
				cmbBxDUT_PortSelection6.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT6_PortSelection: Data not retrieved from database");
			
			} 
    		
    		try {
    			if(saved_dut7_setting.has("port_name")){
	        	cmbBxDUT_PortSelection7.setValue(saved_dut7_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection7.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT7_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-7:"+e.getMessage());
				cmbBxDUT_PortSelection7.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT7_PortSelection: Data not retrieved from database");
			
			} 
    		
    		try {
    			if(saved_dut8_setting.has("port_name")){
	        	cmbBxDUT_PortSelection8.setValue(saved_dut8_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection8.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT8_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-8:"+e.getMessage());
				cmbBxDUT_PortSelection8.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT8_PortSelection: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut9_setting.has("port_name")){
	        	cmbBxDUT_PortSelection9.setValue(saved_dut9_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection9.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT9_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-9:"+e.getMessage());
				cmbBxDUT_PortSelection9.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT9_PortSelection: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut10_setting.has("port_name")){
	        	cmbBxDUT_PortSelection10.setValue(saved_dut10_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection10.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT10_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-10:"+e.getMessage());
				cmbBxDUT_PortSelection10.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT10_PortSelection: Data not retrieved from database");
			
			} 
			
			
			
    		try {
    			if(saved_dut11_setting.has("port_name")){
	        	cmbBxDUT_PortSelection11.setValue(saved_dut11_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection11.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT11_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-11:"+e.getMessage());
				cmbBxDUT_PortSelection11.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT11_PortSelection: Data not retrieved from database");
			
			} 
    		
    		
    		    		
    		try {
    			if(saved_dut12_setting.has("port_name")){
	        	cmbBxDUT_PortSelection12.setValue(saved_dut12_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection12.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT12_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-12:"+e.getMessage());
				cmbBxDUT_PortSelection12.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT12_PortSelection: Data not retrieved from database");
			
			} 
    		
    		try {
    			if(saved_dut13_setting.has("port_name")){
	        	cmbBxDUT_PortSelection13.setValue(saved_dut13_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection13.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT13_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-13:"+e.getMessage());
				cmbBxDUT_PortSelection13.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT13_PortSelection: Data not retrieved from database");
			
			} 
    		
    		try {
    			if(saved_dut14_setting.has("port_name")){
	        	cmbBxDUT_PortSelection14.setValue(saved_dut14_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection14.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT14_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-14:"+e.getMessage());
				cmbBxDUT_PortSelection14.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT14_PortSelection: Data not retrieved from database");
			
			} 
    		
    		try {
    			if(saved_dut15_setting.has("port_name")){
	        	cmbBxDUT_PortSelection15.setValue(saved_dut15_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection15.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT15_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-15:"+e.getMessage());
				cmbBxDUT_PortSelection15.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT15_PortSelection: Data not retrieved from database");
			
			} 
    		
    		try {
    			if(saved_dut16_setting.has("port_name")){
	        	cmbBxDUT_PortSelection16.setValue(saved_dut16_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection16.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT16_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-16:"+e.getMessage());
				cmbBxDUT_PortSelection16.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT16_PortSelection: Data not retrieved from database");
			
			} 
    		
    		try {
    			if(saved_dut17_setting.has("port_name")){
	        	cmbBxDUT_PortSelection17.setValue(saved_dut17_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection17.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT17_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-17:"+e.getMessage());
				cmbBxDUT_PortSelection17.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT17_PortSelection: Data not retrieved from database");
			
			} 
    		
    		try {
    			if(saved_dut18_setting.has("port_name")){
	        	cmbBxDUT_PortSelection18.setValue(saved_dut18_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection18.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT18_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-18:"+e.getMessage());
				cmbBxDUT_PortSelection18.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT18_PortSelection: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut19_setting.has("port_name")){
	        	cmbBxDUT_PortSelection19.setValue(saved_dut19_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection19.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT19_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-19:"+e.getMessage());
				cmbBxDUT_PortSelection19.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT19_PortSelection: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut20_setting.has("port_name")){
	        	cmbBxDUT_PortSelection20.setValue(saved_dut20_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection20.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT20_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-20:"+e.getMessage());
				cmbBxDUT_PortSelection20.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT20_PortSelection: Data not retrieved from database");
			
			} 
			
			
    		try {
    			if(saved_dut21_setting.has("port_name")){
	        	cmbBxDUT_PortSelection21.setValue(saved_dut21_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection21.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT21_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-21:"+e.getMessage());
				cmbBxDUT_PortSelection21.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT21_PortSelection: Data not retrieved from database");
			
			} 
    		
    		
    		    		
    		try {
    			if(saved_dut22_setting.has("port_name")){
	        	cmbBxDUT_PortSelection22.setValue(saved_dut22_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection22.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT22_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-22:"+e.getMessage());
				cmbBxDUT_PortSelection22.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT22_PortSelection: Data not retrieved from database");
			
			} 
    		
    		try {
    			if(saved_dut23_setting.has("port_name")){
	        	cmbBxDUT_PortSelection23.setValue(saved_dut23_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection23.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT23_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-23:"+e.getMessage());
				cmbBxDUT_PortSelection23.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT23_PortSelection: Data not retrieved from database");
			
			} 
    		
    		try {
    			if(saved_dut24_setting.has("port_name")){
	        	cmbBxDUT_PortSelection24.setValue(saved_dut24_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection24.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT24_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-24:"+e.getMessage());
				cmbBxDUT_PortSelection24.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT24_PortSelection: Data not retrieved from database");
			
			} 
    		
    		try {
    			if(saved_dut25_setting.has("port_name")){
	        	cmbBxDUT_PortSelection25.setValue(saved_dut25_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection25.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT25_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-25:"+e.getMessage());
				cmbBxDUT_PortSelection25.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT25_PortSelection: Data not retrieved from database");
			
			} 
    		
    		try {
    			if(saved_dut26_setting.has("port_name")){
	        	cmbBxDUT_PortSelection26.setValue(saved_dut26_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection26.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT26_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-26:"+e.getMessage());
				cmbBxDUT_PortSelection26.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT26_PortSelection: Data not retrieved from database");
			
			} 
    		
    		try {
    			if(saved_dut27_setting.has("port_name")){
	        	cmbBxDUT_PortSelection27.setValue(saved_dut27_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection27.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT27_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-27:"+e.getMessage());
				cmbBxDUT_PortSelection27.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT27_PortSelection: Data not retrieved from database");
			
			} 
    		
    		try {
    			if(saved_dut28_setting.has("port_name")){
	        	cmbBxDUT_PortSelection28.setValue(saved_dut28_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection28.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT28_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-28:"+e.getMessage());
				cmbBxDUT_PortSelection28.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT28_PortSelection: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut29_setting.has("port_name")){
	        	cmbBxDUT_PortSelection29.setValue(saved_dut29_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection29.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT29_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-29:"+e.getMessage());
				cmbBxDUT_PortSelection29.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT29_PortSelection: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut30_setting.has("port_name")){
	        	cmbBxDUT_PortSelection30.setValue(saved_dut30_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection30.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT30_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-30:"+e.getMessage());
				cmbBxDUT_PortSelection30.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT30_PortSelection: Data not retrieved from database");
			
			} 
			
			
    		try {
    			if(saved_dut31_setting.has("port_name")){
	        	cmbBxDUT_PortSelection31.setValue(saved_dut31_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection31.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT31_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-31:"+e.getMessage());
				cmbBxDUT_PortSelection31.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT31_PortSelection: Data not retrieved from database");
			
			} 
    		
    		
    		    		
    		try {
    			if(saved_dut32_setting.has("port_name")){
	        	cmbBxDUT_PortSelection32.setValue(saved_dut32_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection32.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT32_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-32:"+e.getMessage());
				cmbBxDUT_PortSelection32.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT32_PortSelection: Data not retrieved from database");
			
			} 
    		
    		try {
    			if(saved_dut33_setting.has("port_name")){
	        	cmbBxDUT_PortSelection33.setValue(saved_dut33_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection33.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT33_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-33:"+e.getMessage());
				cmbBxDUT_PortSelection33.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT33_PortSelection: Data not retrieved from database");
			
			} 
    		
    		try {
    			if(saved_dut34_setting.has("port_name")){
	        	cmbBxDUT_PortSelection34.setValue(saved_dut34_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection34.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT34_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-34:"+e.getMessage());
				cmbBxDUT_PortSelection34.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT34_PortSelection: Data not retrieved from database");
			
			} 
    		
    		try {
    			if(saved_dut35_setting.has("port_name")){
	        	cmbBxDUT_PortSelection35.setValue(saved_dut35_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection35.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT35_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-35:"+e.getMessage());
				cmbBxDUT_PortSelection35.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT35_PortSelection: Data not retrieved from database");
			
			} 
    		
    		try {
    			if(saved_dut36_setting.has("port_name")){
	        	cmbBxDUT_PortSelection36.setValue(saved_dut36_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection36.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT36_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-36:"+e.getMessage());
				cmbBxDUT_PortSelection36.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT36_PortSelection: Data not retrieved from database");
			
			} 
    		
    		try {
    			if(saved_dut37_setting.has("port_name")){
	        	cmbBxDUT_PortSelection37.setValue(saved_dut37_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection37.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT37_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-37:"+e.getMessage());
				cmbBxDUT_PortSelection37.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT37_PortSelection: Data not retrieved from database");
			
			} 
    		
    		try {
    			if(saved_dut38_setting.has("port_name")){
	        	cmbBxDUT_PortSelection38.setValue(saved_dut38_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection38.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT38_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-38:"+e.getMessage());
				cmbBxDUT_PortSelection38.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT38_PortSelection: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut39_setting.has("port_name")){
	        	cmbBxDUT_PortSelection39.setValue(saved_dut39_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection39.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT39_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-39:"+e.getMessage());
				cmbBxDUT_PortSelection39.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT39_PortSelection: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut40_setting.has("port_name")){
	        	cmbBxDUT_PortSelection40.setValue(saved_dut40_setting.getString("port_name")); 	
    			} else {
    				cmbBxDUT_PortSelection40.setValue("");
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT40_PortSelection: Data not retrieved from DB");
    			
    			}

			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-40:"+e.getMessage());
				cmbBxDUT_PortSelection40.setValue("");
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT40_PortSelection: Data not retrieved from database");
			
			} 
			if((ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK > 40) && (ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK == ProcalFeatureEnable.RACK_MAX_POSITION)){
			    
			
				try {
	    			if(saved_dut41_setting.has("port_name")){
		        	cmbBxDUT_PortSelection41.setValue(saved_dut41_setting.getString("port_name")); 	
	    			} else {
	    				cmbBxDUT_PortSelection41.setValue("");
	    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT41_PortSelection: Data not retrieved from DB");
	    			
	    			}
	
				} catch (JSONException e) {
					
					e.printStackTrace();
					ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-41:"+e.getMessage());
					cmbBxDUT_PortSelection41.setValue("");
					ApplicationLauncher.logger.info("load_saved_device_settings: DUT41_PortSelection: Data not retrieved from database");
				
				} 
	    		
	    		
	    		    		
	    		try {
	    			if(saved_dut42_setting.has("port_name")){
		        	cmbBxDUT_PortSelection42.setValue(saved_dut42_setting.getString("port_name")); 	
	    			} else {
	    				cmbBxDUT_PortSelection42.setValue("");
	    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT42_PortSelection: Data not retrieved from DB");
	    			
	    			}
	
				} catch (JSONException e) {
					
					e.printStackTrace();
					ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-42:"+e.getMessage());
					cmbBxDUT_PortSelection42.setValue("");
					ApplicationLauncher.logger.info("load_saved_device_settings: DUT42_PortSelection: Data not retrieved from database");
				
				} 
	    		
	    		try {
	    			if(saved_dut43_setting.has("port_name")){
		        	cmbBxDUT_PortSelection43.setValue(saved_dut43_setting.getString("port_name")); 	
	    			} else {
	    				cmbBxDUT_PortSelection43.setValue("");
	    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT43_PortSelection: Data not retrieved from DB");
	    			
	    			}
	
				} catch (JSONException e) {
					
					e.printStackTrace();
					ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-43:"+e.getMessage());
					cmbBxDUT_PortSelection43.setValue("");
					ApplicationLauncher.logger.info("load_saved_device_settings: DUT43_PortSelection: Data not retrieved from database");
				
				} 
	    		
	    		try {
	    			if(saved_dut44_setting.has("port_name")){
		        	cmbBxDUT_PortSelection44.setValue(saved_dut44_setting.getString("port_name")); 	
	    			} else {
	    				cmbBxDUT_PortSelection44.setValue("");
	    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT44_PortSelection: Data not retrieved from DB");
	    			
	    			}
	
				} catch (JSONException e) {
					
					e.printStackTrace();
					ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-44:"+e.getMessage());
					cmbBxDUT_PortSelection44.setValue("");
					ApplicationLauncher.logger.info("load_saved_device_settings: DUT44_PortSelection: Data not retrieved from database");
				
				} 
	    		
	    		try {
	    			if(saved_dut45_setting.has("port_name")){
		        	cmbBxDUT_PortSelection45.setValue(saved_dut45_setting.getString("port_name")); 	
	    			} else {
	    				cmbBxDUT_PortSelection45.setValue("");
	    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT45_PortSelection: Data not retrieved from DB");
	    			
	    			}
	
				} catch (JSONException e) {
					
					e.printStackTrace();
					ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-45:"+e.getMessage());
					cmbBxDUT_PortSelection45.setValue("");
					ApplicationLauncher.logger.info("load_saved_device_settings: DUT45_PortSelection: Data not retrieved from database");
				
				} 
	    		
	    		try {
	    			if(saved_dut46_setting.has("port_name")){
		        	cmbBxDUT_PortSelection46.setValue(saved_dut46_setting.getString("port_name")); 	
	    			} else {
	    				cmbBxDUT_PortSelection46.setValue("");
	    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT46_PortSelection: Data not retrieved from DB");
	    			
	    			}
	
				} catch (JSONException e) {
					
					e.printStackTrace();
					ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-46:"+e.getMessage());
					cmbBxDUT_PortSelection46.setValue("");
					ApplicationLauncher.logger.info("load_saved_device_settings: DUT46_PortSelection: Data not retrieved from database");
				
				} 
	    		
	    		try {
	    			if(saved_dut47_setting.has("port_name")){
		        	cmbBxDUT_PortSelection47.setValue(saved_dut47_setting.getString("port_name")); 	
	    			} else {
	    				cmbBxDUT_PortSelection47.setValue("");
	    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT47_PortSelection: Data not retrieved from DB");
	    			
	    			}
	
				} catch (JSONException e) {
					
					e.printStackTrace();
					ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-47:"+e.getMessage());
					cmbBxDUT_PortSelection47.setValue("");
					ApplicationLauncher.logger.info("load_saved_device_settings: DUT47_PortSelection: Data not retrieved from database");
				
				} 
	    		
	    		try {
	    			if(saved_dut48_setting.has("port_name")){
		        	cmbBxDUT_PortSelection48.setValue(saved_dut48_setting.getString("port_name")); 	
	    			} else {
	    				cmbBxDUT_PortSelection48.setValue("");
	    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT48_PortSelection: Data not retrieved from DB");
	    			
	    			}
	
				} catch (JSONException e) {
					
					e.printStackTrace();
					ApplicationLauncher.logger.error("load_saved_device_settings: JSONException5-48:"+e.getMessage());
					cmbBxDUT_PortSelection48.setValue("");
					ApplicationLauncher.logger.info("load_saved_device_settings: DUT48_PortSelection: Data not retrieved from database");
				
				} 
			}
			
			
    		
    		try {
    			if(saved_dut1_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate1.setValue(Integer.parseInt(saved_dut1_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate1.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-1:"+e.getMessage());
				cmbBxDUT_BaudRate1.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT_BaudRate: Data not retrieved from database");
			
			} 
    		
    		
    		
    		try {
    			if(saved_dut2_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate2.setValue(Integer.parseInt(saved_dut2_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate2.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT2_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-2:"+e.getMessage());
				cmbBxDUT_BaudRate2.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT2_BaudRate: Data not retrieved from database");
			
			} 
    		
    		try {
    			if(saved_dut3_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate3.setValue(Integer.parseInt(saved_dut3_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate3.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT3_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-3:"+e.getMessage());
				cmbBxDUT_BaudRate3.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT3_BaudRate: Data not retrieved from database");
			
			} 
    		
    		try {
    			if(saved_dut4_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate4.setValue(Integer.parseInt(saved_dut4_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate4.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT4_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-4:"+e.getMessage());
				cmbBxDUT_BaudRate4.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT4_BaudRate: Data not retrieved from database");
			
			} 
    		
    		try {
    			if(saved_dut5_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate5.setValue(Integer.parseInt(saved_dut5_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate5.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT5_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-5:"+e.getMessage());
				cmbBxDUT_BaudRate5.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT5_BaudRate: Data not retrieved from database");
			
			} 
    		
    		try {
    			if(saved_dut6_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate6.setValue(Integer.parseInt(saved_dut6_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate6.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT6_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-6:"+e.getMessage());
				cmbBxDUT_BaudRate6.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT6_BaudRate: Data not retrieved from database");
			
			} 
    		
    		try {
    			if(saved_dut7_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate7.setValue(Integer.parseInt(saved_dut7_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate7.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT7_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-7:"+e.getMessage());
				cmbBxDUT_BaudRate7.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT7_BaudRate: Data not retrieved from database");
			
			} 
    		
    		try {
    			if(saved_dut8_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate8.setValue(Integer.parseInt(saved_dut8_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate8.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT8_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-8:"+e.getMessage());
				cmbBxDUT_BaudRate8.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT8_BaudRate: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut9_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate9.setValue(Integer.parseInt(saved_dut9_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate9.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT9_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-9:"+e.getMessage());
				cmbBxDUT_BaudRate9.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT9_BaudRate: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut10_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate10.setValue(Integer.parseInt(saved_dut10_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate10.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT10_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-10:"+e.getMessage());
				cmbBxDUT_BaudRate10.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT10_BaudRate: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut11_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate11.setValue(Integer.parseInt(saved_dut11_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate11.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT11_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-11:"+e.getMessage());
				cmbBxDUT_BaudRate11.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT11_BaudRate: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut12_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate12.setValue(Integer.parseInt(saved_dut12_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate12.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT12_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-12:"+e.getMessage());
				cmbBxDUT_BaudRate12.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT12_BaudRate: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut13_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate13.setValue(Integer.parseInt(saved_dut13_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate13.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT13_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-13:"+e.getMessage());
				cmbBxDUT_BaudRate13.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT13_BaudRate: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut14_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate14.setValue(Integer.parseInt(saved_dut14_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate14.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT14_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-14:"+e.getMessage());
				cmbBxDUT_BaudRate14.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT14_BaudRate: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut15_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate15.setValue(Integer.parseInt(saved_dut15_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate15.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT15_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-15:"+e.getMessage());
				cmbBxDUT_BaudRate15.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT15_BaudRate: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut16_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate16.setValue(Integer.parseInt(saved_dut16_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate16.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT16_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-16:"+e.getMessage());
				cmbBxDUT_BaudRate16.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT16_BaudRate: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut17_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate17.setValue(Integer.parseInt(saved_dut17_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate17.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT17_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-17:"+e.getMessage());
				cmbBxDUT_BaudRate17.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT17_BaudRate: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut18_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate18.setValue(Integer.parseInt(saved_dut18_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate18.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT18_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-18:"+e.getMessage());
				cmbBxDUT_BaudRate18.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT18_BaudRate: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut19_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate19.setValue(Integer.parseInt(saved_dut19_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate19.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT19_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-19:"+e.getMessage());
				cmbBxDUT_BaudRate19.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT19_BaudRate: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut20_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate20.setValue(Integer.parseInt(saved_dut20_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate20.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT20_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-20:"+e.getMessage());
				cmbBxDUT_BaudRate20.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT20_BaudRate: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut21_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate21.setValue(Integer.parseInt(saved_dut21_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate21.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT21_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-21:"+e.getMessage());
				cmbBxDUT_BaudRate21.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT21_BaudRate: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut22_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate22.setValue(Integer.parseInt(saved_dut22_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate22.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT22_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-22:"+e.getMessage());
				cmbBxDUT_BaudRate22.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT22_BaudRate: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut23_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate23.setValue(Integer.parseInt(saved_dut23_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate23.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT23_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-23:"+e.getMessage());
				cmbBxDUT_BaudRate23.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT23_BaudRate: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut24_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate24.setValue(Integer.parseInt(saved_dut24_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate24.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT24_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-24:"+e.getMessage());
				cmbBxDUT_BaudRate24.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT24_BaudRate: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut25_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate25.setValue(Integer.parseInt(saved_dut25_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate25.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT25_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-25:"+e.getMessage());
				cmbBxDUT_BaudRate25.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT25_BaudRate: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut26_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate26.setValue(Integer.parseInt(saved_dut26_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate26.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT26_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-26:"+e.getMessage());
				cmbBxDUT_BaudRate26.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT26_BaudRate: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut27_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate27.setValue(Integer.parseInt(saved_dut27_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate27.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT27_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-27:"+e.getMessage());
				cmbBxDUT_BaudRate27.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT27_BaudRate: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut28_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate28.setValue(Integer.parseInt(saved_dut28_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate28.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT28_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-28:"+e.getMessage());
				cmbBxDUT_BaudRate28.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT28_BaudRate: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut29_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate29.setValue(Integer.parseInt(saved_dut29_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate29.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT29_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-29:"+e.getMessage());
				cmbBxDUT_BaudRate29.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT29_BaudRate: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut30_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate30.setValue(Integer.parseInt(saved_dut30_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate30.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT30_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-30:"+e.getMessage());
				cmbBxDUT_BaudRate30.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT30_BaudRate: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut31_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate31.setValue(Integer.parseInt(saved_dut31_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate31.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT31_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-31:"+e.getMessage());
				cmbBxDUT_BaudRate31.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT31_BaudRate: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut32_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate32.setValue(Integer.parseInt(saved_dut32_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate32.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT32_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-32:"+e.getMessage());
				cmbBxDUT_BaudRate32.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT32_BaudRate: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut33_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate33.setValue(Integer.parseInt(saved_dut33_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate33.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT33_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-33:"+e.getMessage());
				cmbBxDUT_BaudRate33.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT33_BaudRate: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut34_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate34.setValue(Integer.parseInt(saved_dut34_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate34.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT34_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-34:"+e.getMessage());
				cmbBxDUT_BaudRate34.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT34_BaudRate: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut35_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate35.setValue(Integer.parseInt(saved_dut35_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate35.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT35_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-35:"+e.getMessage());
				cmbBxDUT_BaudRate35.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT35_BaudRate: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut36_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate36.setValue(Integer.parseInt(saved_dut36_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate36.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT36_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-36:"+e.getMessage());
				cmbBxDUT_BaudRate36.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT36_BaudRate: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut37_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate37.setValue(Integer.parseInt(saved_dut37_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate37.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT37_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-37:"+e.getMessage());
				cmbBxDUT_BaudRate37.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT37_BaudRate: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut38_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate38.setValue(Integer.parseInt(saved_dut38_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate38.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT38_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-38:"+e.getMessage());
				cmbBxDUT_BaudRate38.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT38_BaudRate: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut39_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate39.setValue(Integer.parseInt(saved_dut39_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate39.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT39_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-39:"+e.getMessage());
				cmbBxDUT_BaudRate39.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT39_BaudRate: Data not retrieved from database");
			
			} 
			
			try {
    			if(saved_dut40_setting.has("baud_rate")){
	        	cmbBxDUT_BaudRate40.setValue(Integer.parseInt(saved_dut40_setting.getString("baud_rate"))); 
    			} else {
    				cmbBxDUT_BaudRate40.setValue(9600);
    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT40_BaudRate: Data not retrieved from DB");
    			
    			}
			} catch (JSONException e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-40:"+e.getMessage());
				cmbBxDUT_BaudRate40.setValue(9600);
				ApplicationLauncher.logger.info("load_saved_device_settings: DUT40_BaudRate: Data not retrieved from database");
			
			} 
			if((ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK > 40) && (ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK == ProcalFeatureEnable.RACK_MAX_POSITION)){
			    
				try {
	    			if(saved_dut41_setting.has("baud_rate")){
		        	cmbBxDUT_BaudRate41.setValue(Integer.parseInt(saved_dut41_setting.getString("baud_rate"))); 
	    			} else {
	    				cmbBxDUT_BaudRate41.setValue(9600);
	    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT41_BaudRate: Data not retrieved from DB");
	    			
	    			}
				} catch (JSONException e) {
					
					e.printStackTrace();
					ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-41:"+e.getMessage());
					cmbBxDUT_BaudRate41.setValue(9600);
					ApplicationLauncher.logger.info("load_saved_device_settings: DUT41_BaudRate: Data not retrieved from database");
				
				} 
				
				try {
	    			if(saved_dut42_setting.has("baud_rate")){
		        	cmbBxDUT_BaudRate42.setValue(Integer.parseInt(saved_dut42_setting.getString("baud_rate"))); 
	    			} else {
	    				cmbBxDUT_BaudRate42.setValue(9600);
	    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT42_BaudRate: Data not retrieved from DB");
	    			
	    			}
				} catch (JSONException e) {
					
					e.printStackTrace();
					ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-42:"+e.getMessage());
					cmbBxDUT_BaudRate42.setValue(9600);
					ApplicationLauncher.logger.info("load_saved_device_settings: DUT42_BaudRate: Data not retrieved from database");
				
				} 
				
				try {
	    			if(saved_dut43_setting.has("baud_rate")){
		        	cmbBxDUT_BaudRate43.setValue(Integer.parseInt(saved_dut43_setting.getString("baud_rate"))); 
	    			} else {
	    				cmbBxDUT_BaudRate43.setValue(9600);
	    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT43_BaudRate: Data not retrieved from DB");
	    			
	    			}
				} catch (JSONException e) {
					
					e.printStackTrace();
					ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-43:"+e.getMessage());
					cmbBxDUT_BaudRate43.setValue(9600);
					ApplicationLauncher.logger.info("load_saved_device_settings: DUT43_BaudRate: Data not retrieved from database");
				
				} 
				
				try {
	    			if(saved_dut44_setting.has("baud_rate")){
		        	cmbBxDUT_BaudRate44.setValue(Integer.parseInt(saved_dut44_setting.getString("baud_rate"))); 
	    			} else {
	    				cmbBxDUT_BaudRate44.setValue(9600);
	    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT44_BaudRate: Data not retrieved from DB");
	    			
	    			}
				} catch (JSONException e) {
					
					e.printStackTrace();
					ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-44:"+e.getMessage());
					cmbBxDUT_BaudRate44.setValue(9600);
					ApplicationLauncher.logger.info("load_saved_device_settings: DUT44_BaudRate: Data not retrieved from database");
				
				} 
				
				try {
	    			if(saved_dut45_setting.has("baud_rate")){
		        	cmbBxDUT_BaudRate45.setValue(Integer.parseInt(saved_dut45_setting.getString("baud_rate"))); 
	    			} else {
	    				cmbBxDUT_BaudRate45.setValue(9600);
	    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT45_BaudRate: Data not retrieved from DB");
	    			
	    			}
				} catch (JSONException e) {
					
					e.printStackTrace();
					ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-45:"+e.getMessage());
					cmbBxDUT_BaudRate45.setValue(9600);
					ApplicationLauncher.logger.info("load_saved_device_settings: DUT45_BaudRate: Data not retrieved from database");
				
				} 
				
				try {
	    			if(saved_dut46_setting.has("baud_rate")){
		        	cmbBxDUT_BaudRate46.setValue(Integer.parseInt(saved_dut46_setting.getString("baud_rate"))); 
	    			} else {
	    				cmbBxDUT_BaudRate46.setValue(9600);
	    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT46_BaudRate: Data not retrieved from DB");
	    			
	    			}
				} catch (JSONException e) {
					
					e.printStackTrace();
					ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-46:"+e.getMessage());
					cmbBxDUT_BaudRate46.setValue(9600);
					ApplicationLauncher.logger.info("load_saved_device_settings: DUT46_BaudRate: Data not retrieved from database");
				
				} 
				
				try {
	    			if(saved_dut47_setting.has("baud_rate")){
		        	cmbBxDUT_BaudRate47.setValue(Integer.parseInt(saved_dut47_setting.getString("baud_rate"))); 
	    			} else {
	    				cmbBxDUT_BaudRate47.setValue(9600);
	    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT47_BaudRate: Data not retrieved from DB");
	    			
	    			}
				} catch (JSONException e) {
					
					e.printStackTrace();
					ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-47:"+e.getMessage());
					cmbBxDUT_BaudRate47.setValue(9600);
					ApplicationLauncher.logger.info("load_saved_device_settings: DUT47_BaudRate: Data not retrieved from database");
				
				} 
				
				try {
	    			if(saved_dut48_setting.has("baud_rate")){
		        	cmbBxDUT_BaudRate48.setValue(Integer.parseInt(saved_dut48_setting.getString("baud_rate"))); 
	    			} else {
	    				cmbBxDUT_BaudRate48.setValue(9600);
	    				ApplicationLauncher.logger.info("load_saved_device_settings: DUT48_BaudRate: Data not retrieved from DB");
	    			
	    			}
				} catch (JSONException e) {
					
					e.printStackTrace();
					ApplicationLauncher.logger.error("load_saved_device_settings: JSONException6-48:"+e.getMessage());
					cmbBxDUT_BaudRate48.setValue(9600);
					ApplicationLauncher.logger.info("load_saved_device_settings: DUT48_BaudRate: Data not retrieved from database");
				
				} 
			}
    	//}	
    	
    }
    
    public static JSONObject get_device_settings(String InputSourceType){

    	JSONObject saved_pwr_setting = MySQL_Controller.sp_getdevice_setting(InputSourceType);
    	return saved_pwr_setting;
    }
    
    
    public void setupComPortsBaudRate() {
    	
/*    	cmbBxPowerSrcBaudRate.getItems().clear();
    	cmbBxPowerSrcBaudRate.getItems().addAll(ConstantApp.BaudRateConstant);
    	if(ProcalFeatureEnable.MTE_POWER_SOURCE_CONNECTED){
    		cmbBxPowerSrcBaudRate.getSelectionModel().select(ConstantMtePowerSource.PowerSrcDefaultBaudRate);
    	}else if(ProcalFeatureEnable.LSCS_POWER_SOURCE_CONNECTED){
    		cmbBxPowerSrcBaudRate.getSelectionModel().select(ConstantLscsPowerSource.PowerSrcDefaultBaudRate);
    	}
    	cmbBxRefStdBaudRate.getItems().clear();
    	cmbBxRefStdBaudRate.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxRefStdBaudRate.getSelectionModel().select(ConstantRadiantRefStd.RefStdDefaultBaudRate);*/
    	cmbBxDUT_BaudRate1.getItems().clear();
    	cmbBxDUT_BaudRate1.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxDUT_BaudRate1.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	
    	    	
    	cmbBxDUT_BaudRate2.getItems().clear();
    	cmbBxDUT_BaudRate2.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxDUT_BaudRate2.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	
    	cmbBxDUT_BaudRate3.getItems().clear();
    	cmbBxDUT_BaudRate3.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxDUT_BaudRate3.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	
    	cmbBxDUT_BaudRate4.getItems().clear();
    	cmbBxDUT_BaudRate4.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxDUT_BaudRate4.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	
    	cmbBxDUT_BaudRate5.getItems().clear();
    	cmbBxDUT_BaudRate5.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxDUT_BaudRate5.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	
    	cmbBxDUT_BaudRate6.getItems().clear();
    	cmbBxDUT_BaudRate6.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxDUT_BaudRate6.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	
    	cmbBxDUT_BaudRate7.getItems().clear();
    	cmbBxDUT_BaudRate7.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxDUT_BaudRate7.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	
    	
    	cmbBxDUT_BaudRate8.getItems().clear();
    	cmbBxDUT_BaudRate9.getItems().clear();
    	cmbBxDUT_BaudRate10.getItems().clear();
    	cmbBxDUT_BaudRate11.getItems().clear();
    	cmbBxDUT_BaudRate12.getItems().clear();
    	cmbBxDUT_BaudRate13.getItems().clear();
    	cmbBxDUT_BaudRate14.getItems().clear();
    	cmbBxDUT_BaudRate15.getItems().clear();
    	cmbBxDUT_BaudRate16.getItems().clear();
    	cmbBxDUT_BaudRate17.getItems().clear();
    	cmbBxDUT_BaudRate18.getItems().clear();
    	cmbBxDUT_BaudRate19.getItems().clear();
    	cmbBxDUT_BaudRate20.getItems().clear();
    	cmbBxDUT_BaudRate21.getItems().clear();
    	cmbBxDUT_BaudRate22.getItems().clear();
    	cmbBxDUT_BaudRate23.getItems().clear();
    	cmbBxDUT_BaudRate24.getItems().clear();
    	cmbBxDUT_BaudRate25.getItems().clear();
    	cmbBxDUT_BaudRate26.getItems().clear();
    	cmbBxDUT_BaudRate27.getItems().clear();
    	cmbBxDUT_BaudRate28.getItems().clear();
    	cmbBxDUT_BaudRate29.getItems().clear();
    	cmbBxDUT_BaudRate30.getItems().clear();
    	cmbBxDUT_BaudRate31.getItems().clear();
    	cmbBxDUT_BaudRate32.getItems().clear();
    	cmbBxDUT_BaudRate33.getItems().clear();
    	cmbBxDUT_BaudRate34.getItems().clear();
    	cmbBxDUT_BaudRate35.getItems().clear();
    	cmbBxDUT_BaudRate36.getItems().clear();
    	cmbBxDUT_BaudRate37.getItems().clear();
    	cmbBxDUT_BaudRate38.getItems().clear();
    	cmbBxDUT_BaudRate39.getItems().clear();
    	cmbBxDUT_BaudRate40.getItems().clear();
    	
    	if((ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK > 40) && (ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK == ProcalFeatureEnable.RACK_MAX_POSITION)){
	    	cmbBxDUT_BaudRate41.getItems().clear();
	    	cmbBxDUT_BaudRate42.getItems().clear();
	    	cmbBxDUT_BaudRate43.getItems().clear();
	    	cmbBxDUT_BaudRate44.getItems().clear();
	    	cmbBxDUT_BaudRate45.getItems().clear();
	    	cmbBxDUT_BaudRate46.getItems().clear();
	    	cmbBxDUT_BaudRate47.getItems().clear();
	    	cmbBxDUT_BaudRate48.getItems().clear();
    	}

    	cmbBxDUT_BaudRate8.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxDUT_BaudRate9.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxDUT_BaudRate10.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxDUT_BaudRate11.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxDUT_BaudRate12.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxDUT_BaudRate13.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxDUT_BaudRate14.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxDUT_BaudRate15.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxDUT_BaudRate16.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxDUT_BaudRate17.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxDUT_BaudRate18.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxDUT_BaudRate19.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxDUT_BaudRate20.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxDUT_BaudRate21.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxDUT_BaudRate22.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxDUT_BaudRate23.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxDUT_BaudRate24.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxDUT_BaudRate25.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxDUT_BaudRate26.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxDUT_BaudRate27.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxDUT_BaudRate28.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxDUT_BaudRate29.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxDUT_BaudRate30.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxDUT_BaudRate31.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxDUT_BaudRate32.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxDUT_BaudRate33.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxDUT_BaudRate34.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxDUT_BaudRate35.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxDUT_BaudRate36.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxDUT_BaudRate37.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxDUT_BaudRate38.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxDUT_BaudRate39.getItems().addAll(ConstantApp.BaudRateConstant);
    	cmbBxDUT_BaudRate40.getItems().addAll(ConstantApp.BaudRateConstant);
    	
    	if((ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK > 40) && (ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK == ProcalFeatureEnable.RACK_MAX_POSITION)){
    	    
	    	cmbBxDUT_BaudRate41.getItems().addAll(ConstantApp.BaudRateConstant);
	    	cmbBxDUT_BaudRate42.getItems().addAll(ConstantApp.BaudRateConstant);
	    	cmbBxDUT_BaudRate43.getItems().addAll(ConstantApp.BaudRateConstant);
	    	cmbBxDUT_BaudRate44.getItems().addAll(ConstantApp.BaudRateConstant);
	    	cmbBxDUT_BaudRate45.getItems().addAll(ConstantApp.BaudRateConstant);
	    	cmbBxDUT_BaudRate46.getItems().addAll(ConstantApp.BaudRateConstant);
	    	cmbBxDUT_BaudRate47.getItems().addAll(ConstantApp.BaudRateConstant);
	    	cmbBxDUT_BaudRate48.getItems().addAll(ConstantApp.BaudRateConstant);
    	}

    	cmbBxDUT_BaudRate8.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	cmbBxDUT_BaudRate9.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	cmbBxDUT_BaudRate10.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	cmbBxDUT_BaudRate11.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	cmbBxDUT_BaudRate12.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	cmbBxDUT_BaudRate13.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	cmbBxDUT_BaudRate14.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	cmbBxDUT_BaudRate15.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	cmbBxDUT_BaudRate16.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	cmbBxDUT_BaudRate17.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	cmbBxDUT_BaudRate18.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	cmbBxDUT_BaudRate19.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	cmbBxDUT_BaudRate20.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	cmbBxDUT_BaudRate21.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	cmbBxDUT_BaudRate22.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	cmbBxDUT_BaudRate23.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	cmbBxDUT_BaudRate24.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	cmbBxDUT_BaudRate25.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	cmbBxDUT_BaudRate26.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	cmbBxDUT_BaudRate27.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	cmbBxDUT_BaudRate28.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	cmbBxDUT_BaudRate29.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	cmbBxDUT_BaudRate30.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	cmbBxDUT_BaudRate31.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	cmbBxDUT_BaudRate32.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	cmbBxDUT_BaudRate33.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	cmbBxDUT_BaudRate34.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	cmbBxDUT_BaudRate35.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	cmbBxDUT_BaudRate36.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	cmbBxDUT_BaudRate37.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	cmbBxDUT_BaudRate38.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	cmbBxDUT_BaudRate39.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	cmbBxDUT_BaudRate40.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	if((ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK > 40) && (ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK == ProcalFeatureEnable.RACK_MAX_POSITION)){
    	    
	    	cmbBxDUT_BaudRate41.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
	    	cmbBxDUT_BaudRate42.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
	    	cmbBxDUT_BaudRate43.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
	    	cmbBxDUT_BaudRate44.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
	    	cmbBxDUT_BaudRate45.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
	    	cmbBxDUT_BaudRate46.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
	    	cmbBxDUT_BaudRate47.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
	    	cmbBxDUT_BaudRate48.getSelectionModel().select(ConstantDut.DUT_DefaultBaudRate);
    	}

    	
    }
    
    public static boolean  getPortValidationTurnedON(){
    	return PortValidationTurnedON;
    }
    
    public static void setPortValidationTurnedON(boolean status){
    	PortValidationTurnedON = status;
    	
    }
    
    
    public void loadAvailableComPorts() {
    	
    	
    	//Stub for testing
    	
		 ArrayList<String> ModelList = new ArrayList<String>();
		 String ModelName = "";
		 ConstantApp MyPropertyObj= new ConstantApp();
		 ModelName = ConstantAppConfig.REFSTD;
		 ModelList.add("Com1");
		 ModelList.add("Com2");
		 ModelList.add("Com3");
		 
    	scanSerialPortAndUpdateDisplay();//dramesh
    }
    
/*    public void updatePowerSourceModel() {
		 ArrayList<String> ModelList = new ArrayList<String>();
		 String ModelName = "";
		 ConstantApp MyPropertyObj= new ConstantApp();
		 ModelName = ConstantConfig.POWERSRC;
		 ModelList.add(ModelName);
		 cmbBxPowerSource_ModelName.getItems().clear();
	     cmbBxPowerSource_ModelName.getItems().addAll(ModelList);
	     cmbBxPowerSource_ModelName.getSelectionModel().select(0);
	 }
    
    public void updateReferenceMeterModel() {
		 ArrayList<String> ModelList = new ArrayList<String>();
		 String ModelName = "";
		 ConstantApp MyPropertyObj= new ConstantApp();
		 ModelName = ConstantConfig.REFSTD;
		 ModelList.add(ModelName);
		 cmbBxReferanceStd_ModelName.getItems().clear();
		 cmbBxReferanceStd_ModelName.getItems().addAll(ModelList);
		 cmbBxReferanceStd_ModelName.getSelectionModel().select(0);
	 }*/
    
    public void updateDUTModel() {
		 ArrayList<String> ModelList = new ArrayList<String>();
		 String ModelName = "";
		 ConstantApp MyPropertyObj= new ConstantApp();
		 ModelName = ConstantAppConfig.DUT;
		 ModelList.add(ModelName);
		 cmbBxDUT_ModelName1.getItems().clear();
		 cmbBxDUT_ModelName1.getItems().addAll(ModelList);
		 cmbBxDUT_ModelName1.getSelectionModel().select(0);
		 
		 	 
		 cmbBxDUT_ModelName2.getItems().clear();
		 cmbBxDUT_ModelName2.getItems().addAll(ModelList);
		 cmbBxDUT_ModelName2.getSelectionModel().select(0);
		 
		 cmbBxDUT_ModelName3.getItems().clear();
		 cmbBxDUT_ModelName3.getItems().addAll(ModelList);
		 cmbBxDUT_ModelName3.getSelectionModel().select(0);
		 
		 cmbBxDUT_ModelName4.getItems().clear();
		 cmbBxDUT_ModelName4.getItems().addAll(ModelList);
		 cmbBxDUT_ModelName4.getSelectionModel().select(0);
		 
		 cmbBxDUT_ModelName5.getItems().clear();
		 cmbBxDUT_ModelName5.getItems().addAll(ModelList);
		 cmbBxDUT_ModelName5.getSelectionModel().select(0);
		 
		 cmbBxDUT_ModelName6.getItems().clear();
		 cmbBxDUT_ModelName6.getItems().addAll(ModelList);
		 cmbBxDUT_ModelName6.getSelectionModel().select(0);
		 
		 cmbBxDUT_ModelName7.getItems().clear();
		 cmbBxDUT_ModelName7.getItems().addAll(ModelList);
		 cmbBxDUT_ModelName7.getSelectionModel().select(0);
		 
		 
	    	cmbBxDUT_ModelName8.getItems().clear();
	    	cmbBxDUT_ModelName9.getItems().clear();
	    	cmbBxDUT_ModelName10.getItems().clear();
	    	cmbBxDUT_ModelName11.getItems().clear();
	    	cmbBxDUT_ModelName12.getItems().clear();
	    	cmbBxDUT_ModelName13.getItems().clear();
	    	cmbBxDUT_ModelName14.getItems().clear();
	    	cmbBxDUT_ModelName15.getItems().clear();
	    	cmbBxDUT_ModelName16.getItems().clear();
	    	cmbBxDUT_ModelName17.getItems().clear();
	    	cmbBxDUT_ModelName18.getItems().clear();
	    	cmbBxDUT_ModelName19.getItems().clear();
	    	cmbBxDUT_ModelName20.getItems().clear();
	    	cmbBxDUT_ModelName21.getItems().clear();
	    	cmbBxDUT_ModelName22.getItems().clear();
	    	cmbBxDUT_ModelName23.getItems().clear();
	    	cmbBxDUT_ModelName24.getItems().clear();
	    	cmbBxDUT_ModelName25.getItems().clear();
	    	cmbBxDUT_ModelName26.getItems().clear();
	    	cmbBxDUT_ModelName27.getItems().clear();
	    	cmbBxDUT_ModelName28.getItems().clear();
	    	cmbBxDUT_ModelName29.getItems().clear();
	    	cmbBxDUT_ModelName30.getItems().clear();
	    	cmbBxDUT_ModelName31.getItems().clear();
	    	cmbBxDUT_ModelName32.getItems().clear();
	    	cmbBxDUT_ModelName33.getItems().clear();
	    	cmbBxDUT_ModelName34.getItems().clear();
	    	cmbBxDUT_ModelName35.getItems().clear();
	    	cmbBxDUT_ModelName36.getItems().clear();
	    	cmbBxDUT_ModelName37.getItems().clear();
	    	cmbBxDUT_ModelName38.getItems().clear();
	    	cmbBxDUT_ModelName39.getItems().clear();
	    	cmbBxDUT_ModelName40.getItems().clear();
	    	if((ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK > 40) && (ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK == ProcalFeatureEnable.RACK_MAX_POSITION)){
	    	    
		    	cmbBxDUT_ModelName41.getItems().clear();
		    	cmbBxDUT_ModelName42.getItems().clear();
		    	cmbBxDUT_ModelName43.getItems().clear();
		    	cmbBxDUT_ModelName44.getItems().clear();
		    	cmbBxDUT_ModelName45.getItems().clear();
		    	cmbBxDUT_ModelName46.getItems().clear();
		    	cmbBxDUT_ModelName47.getItems().clear();
		    	cmbBxDUT_ModelName48.getItems().clear();
	    	}

	    	cmbBxDUT_ModelName8.getItems().addAll(ModelList);
	    	cmbBxDUT_ModelName9.getItems().addAll(ModelList);
	    	cmbBxDUT_ModelName10.getItems().addAll(ModelList);
	    	cmbBxDUT_ModelName11.getItems().addAll(ModelList);
	    	cmbBxDUT_ModelName12.getItems().addAll(ModelList);
	    	cmbBxDUT_ModelName13.getItems().addAll(ModelList);
	    	cmbBxDUT_ModelName14.getItems().addAll(ModelList);
	    	cmbBxDUT_ModelName15.getItems().addAll(ModelList);
	    	cmbBxDUT_ModelName16.getItems().addAll(ModelList);
	    	cmbBxDUT_ModelName17.getItems().addAll(ModelList);
	    	cmbBxDUT_ModelName18.getItems().addAll(ModelList);
	    	cmbBxDUT_ModelName19.getItems().addAll(ModelList);
	    	cmbBxDUT_ModelName20.getItems().addAll(ModelList);
	    	cmbBxDUT_ModelName21.getItems().addAll(ModelList);
	    	cmbBxDUT_ModelName22.getItems().addAll(ModelList);
	    	cmbBxDUT_ModelName23.getItems().addAll(ModelList);
	    	cmbBxDUT_ModelName24.getItems().addAll(ModelList);
	    	cmbBxDUT_ModelName25.getItems().addAll(ModelList);
	    	cmbBxDUT_ModelName26.getItems().addAll(ModelList);
	    	cmbBxDUT_ModelName27.getItems().addAll(ModelList);
	    	cmbBxDUT_ModelName28.getItems().addAll(ModelList);
	    	cmbBxDUT_ModelName29.getItems().addAll(ModelList);
	    	cmbBxDUT_ModelName30.getItems().addAll(ModelList);
	    	cmbBxDUT_ModelName31.getItems().addAll(ModelList);
	    	cmbBxDUT_ModelName32.getItems().addAll(ModelList);
	    	cmbBxDUT_ModelName33.getItems().addAll(ModelList);
	    	cmbBxDUT_ModelName34.getItems().addAll(ModelList);
	    	cmbBxDUT_ModelName35.getItems().addAll(ModelList);
	    	cmbBxDUT_ModelName36.getItems().addAll(ModelList);
	    	cmbBxDUT_ModelName37.getItems().addAll(ModelList);
	    	cmbBxDUT_ModelName38.getItems().addAll(ModelList);
	    	cmbBxDUT_ModelName39.getItems().addAll(ModelList);
	    	cmbBxDUT_ModelName40.getItems().addAll(ModelList);
	    	if((ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK > 40) && (ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK == ProcalFeatureEnable.RACK_MAX_POSITION)){
	    	    
		    	cmbBxDUT_ModelName41.getItems().addAll(ModelList);
		    	cmbBxDUT_ModelName42.getItems().addAll(ModelList);
		    	cmbBxDUT_ModelName43.getItems().addAll(ModelList);
		    	cmbBxDUT_ModelName44.getItems().addAll(ModelList);
		    	cmbBxDUT_ModelName45.getItems().addAll(ModelList);
		    	cmbBxDUT_ModelName46.getItems().addAll(ModelList);
		    	cmbBxDUT_ModelName47.getItems().addAll(ModelList);
		    	cmbBxDUT_ModelName48.getItems().addAll(ModelList);
	    	}

	    	cmbBxDUT_ModelName8.getSelectionModel().select(0);
	    	cmbBxDUT_ModelName9.getSelectionModel().select(0);
	    	cmbBxDUT_ModelName10.getSelectionModel().select(0);
	    	cmbBxDUT_ModelName11.getSelectionModel().select(0);
	    	cmbBxDUT_ModelName12.getSelectionModel().select(0);
	    	cmbBxDUT_ModelName13.getSelectionModel().select(0);
	    	cmbBxDUT_ModelName14.getSelectionModel().select(0);
	    	cmbBxDUT_ModelName15.getSelectionModel().select(0);
	    	cmbBxDUT_ModelName16.getSelectionModel().select(0);
	    	cmbBxDUT_ModelName17.getSelectionModel().select(0);
	    	cmbBxDUT_ModelName18.getSelectionModel().select(0);
	    	cmbBxDUT_ModelName19.getSelectionModel().select(0);
	    	cmbBxDUT_ModelName20.getSelectionModel().select(0);
	    	cmbBxDUT_ModelName21.getSelectionModel().select(0);
	    	cmbBxDUT_ModelName22.getSelectionModel().select(0);
	    	cmbBxDUT_ModelName23.getSelectionModel().select(0);
	    	cmbBxDUT_ModelName24.getSelectionModel().select(0);
	    	cmbBxDUT_ModelName25.getSelectionModel().select(0);
	    	cmbBxDUT_ModelName26.getSelectionModel().select(0);
	    	cmbBxDUT_ModelName27.getSelectionModel().select(0);
	    	cmbBxDUT_ModelName28.getSelectionModel().select(0);
	    	cmbBxDUT_ModelName29.getSelectionModel().select(0);
	    	cmbBxDUT_ModelName30.getSelectionModel().select(0);
	    	cmbBxDUT_ModelName31.getSelectionModel().select(0);
	    	cmbBxDUT_ModelName32.getSelectionModel().select(0);
	    	cmbBxDUT_ModelName33.getSelectionModel().select(0);
	    	cmbBxDUT_ModelName34.getSelectionModel().select(0);
	    	cmbBxDUT_ModelName35.getSelectionModel().select(0);
	    	cmbBxDUT_ModelName36.getSelectionModel().select(0);
	    	cmbBxDUT_ModelName37.getSelectionModel().select(0);
	    	cmbBxDUT_ModelName38.getSelectionModel().select(0);
	    	cmbBxDUT_ModelName39.getSelectionModel().select(0);
	    	cmbBxDUT_ModelName40.getSelectionModel().select(0);
	    	if((ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK > 40) && (ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK == ProcalFeatureEnable.RACK_MAX_POSITION)){
	    	    
		    	cmbBxDUT_ModelName41.getSelectionModel().select(0);
		    	cmbBxDUT_ModelName42.getSelectionModel().select(0);
		    	cmbBxDUT_ModelName43.getSelectionModel().select(0);
		    	cmbBxDUT_ModelName44.getSelectionModel().select(0);
		    	cmbBxDUT_ModelName45.getSelectionModel().select(0);
		    	cmbBxDUT_ModelName46.getSelectionModel().select(0);
		    	cmbBxDUT_ModelName47.getSelectionModel().select(0);
		    	cmbBxDUT_ModelName48.getSelectionModel().select(0);
	    	}
		 
	    	
		 
	 }
    

    
    
    
    public void scanSerialPortAndUpdateDisplay() {

/*     	cmbBxPowerSrcPortSelection.getItems().clear();
     	cmbBxRefStdPortSelection.getItems().clear();*/
     	cmbBxDUT_PortSelection1.getItems().clear();
     	cmbBxDUT_PortSelection2.getItems().clear();
     	cmbBxDUT_PortSelection3.getItems().clear();
     	cmbBxDUT_PortSelection4.getItems().clear();
     	cmbBxDUT_PortSelection5.getItems().clear();
     	cmbBxDUT_PortSelection6.getItems().clear();
     	cmbBxDUT_PortSelection7.getItems().clear();
     	cmbBxDUT_PortSelection8.getItems().clear();
     	cmbBxDUT_PortSelection9.getItems().clear();
     	cmbBxDUT_PortSelection10.getItems().clear();
     	
     	cmbBxDUT_PortSelection11.getItems().clear();
     	cmbBxDUT_PortSelection12.getItems().clear();
     	cmbBxDUT_PortSelection13.getItems().clear();
     	cmbBxDUT_PortSelection14.getItems().clear();
     	cmbBxDUT_PortSelection15.getItems().clear();
     	cmbBxDUT_PortSelection16.getItems().clear();
     	cmbBxDUT_PortSelection17.getItems().clear();
     	cmbBxDUT_PortSelection18.getItems().clear();
     	cmbBxDUT_PortSelection19.getItems().clear();
     	cmbBxDUT_PortSelection20.getItems().clear();
     	
     	cmbBxDUT_PortSelection21.getItems().clear();
     	cmbBxDUT_PortSelection22.getItems().clear();
     	cmbBxDUT_PortSelection23.getItems().clear();
     	cmbBxDUT_PortSelection24.getItems().clear();
     	cmbBxDUT_PortSelection25.getItems().clear();
     	cmbBxDUT_PortSelection26.getItems().clear();
     	cmbBxDUT_PortSelection27.getItems().clear();
     	cmbBxDUT_PortSelection28.getItems().clear();
     	cmbBxDUT_PortSelection29.getItems().clear();
     	cmbBxDUT_PortSelection30.getItems().clear();
     	
     	cmbBxDUT_PortSelection31.getItems().clear();
     	cmbBxDUT_PortSelection32.getItems().clear();
     	cmbBxDUT_PortSelection33.getItems().clear();
     	cmbBxDUT_PortSelection34.getItems().clear();
     	cmbBxDUT_PortSelection35.getItems().clear();
     	cmbBxDUT_PortSelection36.getItems().clear();
     	cmbBxDUT_PortSelection37.getItems().clear();
     	cmbBxDUT_PortSelection38.getItems().clear();
     	cmbBxDUT_PortSelection39.getItems().clear();
     	cmbBxDUT_PortSelection40.getItems().clear();
     	if((ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK > 40) && (ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK == ProcalFeatureEnable.RACK_MAX_POSITION)){
    	    
	     	cmbBxDUT_PortSelection41.getItems().clear();
	     	cmbBxDUT_PortSelection42.getItems().clear();
	     	cmbBxDUT_PortSelection43.getItems().clear();
	     	cmbBxDUT_PortSelection44.getItems().clear();
	     	cmbBxDUT_PortSelection45.getItems().clear();
	     	cmbBxDUT_PortSelection46.getItems().clear();
	     	cmbBxDUT_PortSelection47.getItems().clear();
	     	cmbBxDUT_PortSelection48.getItems().clear();
     	}

     	Enumeration ports = CommPortIdentifier.getPortIdentifiers();

        while (ports.hasMoreElements()) {
            CommPortIdentifier curPort = (CommPortIdentifier)ports.nextElement();

            if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL) {
/*             	cmbBxPowerSrcPortSelection.getItems().add(curPort.getName());
            	cmbBxRefStdPortSelection.getItems().add(curPort.getName());*/
            	cmbBxDUT_PortSelection1.getItems().add(curPort.getName());
            	cmbBxDUT_PortSelection2.getItems().add(curPort.getName());
            	cmbBxDUT_PortSelection3.getItems().add(curPort.getName());
            	cmbBxDUT_PortSelection4.getItems().add(curPort.getName());
            	cmbBxDUT_PortSelection5.getItems().add(curPort.getName());
            	cmbBxDUT_PortSelection6.getItems().add(curPort.getName());
            	cmbBxDUT_PortSelection7.getItems().add(curPort.getName());
            	cmbBxDUT_PortSelection8.getItems().add(curPort.getName());
            	cmbBxDUT_PortSelection9.getItems().add(curPort.getName());
            	cmbBxDUT_PortSelection10.getItems().add(curPort.getName());
            	
            	cmbBxDUT_PortSelection11.getItems().add(curPort.getName());
            	cmbBxDUT_PortSelection12.getItems().add(curPort.getName());
            	cmbBxDUT_PortSelection13.getItems().add(curPort.getName());
            	cmbBxDUT_PortSelection14.getItems().add(curPort.getName());
            	cmbBxDUT_PortSelection15.getItems().add(curPort.getName());
            	cmbBxDUT_PortSelection16.getItems().add(curPort.getName());
            	cmbBxDUT_PortSelection17.getItems().add(curPort.getName());
            	cmbBxDUT_PortSelection18.getItems().add(curPort.getName());
            	cmbBxDUT_PortSelection19.getItems().add(curPort.getName());
            	cmbBxDUT_PortSelection20.getItems().add(curPort.getName());
            	
            	cmbBxDUT_PortSelection21.getItems().add(curPort.getName());
            	cmbBxDUT_PortSelection22.getItems().add(curPort.getName());
            	cmbBxDUT_PortSelection23.getItems().add(curPort.getName());
            	cmbBxDUT_PortSelection24.getItems().add(curPort.getName());
            	cmbBxDUT_PortSelection25.getItems().add(curPort.getName());
            	cmbBxDUT_PortSelection26.getItems().add(curPort.getName());
            	cmbBxDUT_PortSelection27.getItems().add(curPort.getName());
            	cmbBxDUT_PortSelection28.getItems().add(curPort.getName());
            	cmbBxDUT_PortSelection29.getItems().add(curPort.getName());
            	cmbBxDUT_PortSelection30.getItems().add(curPort.getName());
            	
            	cmbBxDUT_PortSelection31.getItems().add(curPort.getName());
            	cmbBxDUT_PortSelection32.getItems().add(curPort.getName());
            	cmbBxDUT_PortSelection33.getItems().add(curPort.getName());
            	cmbBxDUT_PortSelection34.getItems().add(curPort.getName());
            	cmbBxDUT_PortSelection35.getItems().add(curPort.getName());
            	cmbBxDUT_PortSelection36.getItems().add(curPort.getName());
            	cmbBxDUT_PortSelection37.getItems().add(curPort.getName());
            	cmbBxDUT_PortSelection38.getItems().add(curPort.getName());
            	cmbBxDUT_PortSelection39.getItems().add(curPort.getName());
            	cmbBxDUT_PortSelection40.getItems().add(curPort.getName());
            	if((ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK > 40) && (ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK == ProcalFeatureEnable.RACK_MAX_POSITION)){
            	    
	            	cmbBxDUT_PortSelection41.getItems().add(curPort.getName());
	            	cmbBxDUT_PortSelection42.getItems().add(curPort.getName());
	            	cmbBxDUT_PortSelection43.getItems().add(curPort.getName());
	            	cmbBxDUT_PortSelection44.getItems().add(curPort.getName());
	            	cmbBxDUT_PortSelection45.getItems().add(curPort.getName());
	            	cmbBxDUT_PortSelection46.getItems().add(curPort.getName());
	            	cmbBxDUT_PortSelection47.getItems().add(curPort.getName());
	            	cmbBxDUT_PortSelection48.getItems().add(curPort.getName());
            	}
            }
        }
        
/*        try {
        	cmbBxPowerSrcPortSelection.getSelectionModel().select(0);
        } catch(Exception e) {
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception1:"+e.getMessage());
        }
        try {
        	cmbBxRefStdPortSelection.getSelectionModel().select(0);
        } catch(Exception e) {
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception2:"+e.getMessage());
        }*/
        
        try{
        	cmbBxDUT_PortSelection1.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-1:"+e.getMessage());
        }
        
        
        
        try{
        	cmbBxDUT_PortSelection2.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-2:"+e.getMessage());
        }
        try{
        	cmbBxDUT_PortSelection3.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-3:"+e.getMessage());
        }
        try{
        	cmbBxDUT_PortSelection4.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-4:"+e.getMessage());
        }
        try{
        	cmbBxDUT_PortSelection5.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-5:"+e.getMessage());
        }
        try{
        	cmbBxDUT_PortSelection6.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-6:"+e.getMessage());
        }
        try{
        	cmbBxDUT_PortSelection7.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-7:"+e.getMessage());
        }
        
        
        
        try{ 
        	cmbBxDUT_PortSelection8.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-8:"+e.getMessage());
        }
	
		try{ 
        	cmbBxDUT_PortSelection9.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-9:"+e.getMessage());
        }
		try{ 
        	cmbBxDUT_PortSelection10.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-10:"+e.getMessage());
        }	
		try{ 
        	cmbBxDUT_PortSelection11.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-11:"+e.getMessage());
        }	
	
		try{ 
        	cmbBxDUT_PortSelection12.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-12:"+e.getMessage());
        }
		
		try{ 
        	cmbBxDUT_PortSelection13.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-13:"+e.getMessage());
        }

		try{ 
        	cmbBxDUT_PortSelection14.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-14:"+e.getMessage());
        }
		try{ 
        	cmbBxDUT_PortSelection15.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-15:"+e.getMessage());
        }
		try{ 
        	cmbBxDUT_PortSelection16.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-16:"+e.getMessage());
        }
		try{ 
        	cmbBxDUT_PortSelection17.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-17:"+e.getMessage());
        }
		try{ 
        	cmbBxDUT_PortSelection18.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-18:"+e.getMessage());
        }
		try{ 
        	cmbBxDUT_PortSelection19.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-19:"+e.getMessage());
        }
		try{ 
        	cmbBxDUT_PortSelection20.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-20:"+e.getMessage());
        }
		try{ 
        	cmbBxDUT_PortSelection21.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-21:"+e.getMessage());
        }	
		try{ 
        	cmbBxDUT_PortSelection22.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-22:"+e.getMessage());
        }	
		try{ 
        	cmbBxDUT_PortSelection23.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-23:"+e.getMessage());
        }
		try{ 
        	cmbBxDUT_PortSelection24.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-24:"+e.getMessage());
        }
		try{ 
        	cmbBxDUT_PortSelection25.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-25:"+e.getMessage());
        }
		try{ 
        	cmbBxDUT_PortSelection26.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-26:"+e.getMessage());
        }
		try{ 
        	cmbBxDUT_PortSelection27.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-27:"+e.getMessage());
        }
		try{ 
        	cmbBxDUT_PortSelection28.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-28:"+e.getMessage());
        }
			try{ 
        	cmbBxDUT_PortSelection29.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-29:"+e.getMessage());
        }
		try{ 
        	cmbBxDUT_PortSelection30.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-30:"+e.getMessage());
        }
		try{ 
        	cmbBxDUT_PortSelection31.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-31:"+e.getMessage());
        }
		try{ 
        	cmbBxDUT_PortSelection32.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-32:"+e.getMessage());
        }
		
		try{ 
        	cmbBxDUT_PortSelection33.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-33:"+e.getMessage());
        }
		try{ 
        	cmbBxDUT_PortSelection34.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-34:"+e.getMessage());
        }
		try{ 
        	cmbBxDUT_PortSelection35.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-35:"+e.getMessage());
        }
			try{ 
        	cmbBxDUT_PortSelection36.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-36:"+e.getMessage());
        }
		try{ 
        	cmbBxDUT_PortSelection37.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-37:"+e.getMessage());
        }
		try{ 
        	cmbBxDUT_PortSelection38.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-38:"+e.getMessage());
        }
		try{ 
        	cmbBxDUT_PortSelection39.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-39:"+e.getMessage());
        }
		try{ 
        	cmbBxDUT_PortSelection40.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-40:"+e.getMessage());
        }
		
		if((ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK > 40) && (ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK == ProcalFeatureEnable.RACK_MAX_POSITION)){
		    
		try{ 
        	cmbBxDUT_PortSelection41.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-41:"+e.getMessage());
        }
		try{ 
        	cmbBxDUT_PortSelection42.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-42:"+e.getMessage());
        }
		try{ 
        	cmbBxDUT_PortSelection43.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-43:"+e.getMessage());
        }
		try{ 
        	cmbBxDUT_PortSelection44.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-44:"+e.getMessage());
        }
		try{ 
        	cmbBxDUT_PortSelection45.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-45:"+e.getMessage());
        }
		try{ 
        	cmbBxDUT_PortSelection46.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-46:"+e.getMessage());
        }
		try{ 
        	cmbBxDUT_PortSelection47.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-47:"+e.getMessage());
        }
		try{ 
        	cmbBxDUT_PortSelection48.getSelectionModel().select(0);
        } catch(Exception e){
        	e.printStackTrace();
        	ApplicationLauncher.logger.error("scanSerialPortAndUpdateDisplay: Exception3-48:"+e.getMessage());
        }
		}
        
        
        
    }
    
    public void SaveOnClick(){
/*    	String pwr_type = ConstantApp.SOURCE_TYPE_POWER_SOURCE;
    	String pwr_model_name = cmbBxPowerSource_ModelName.getSelectionModel().getSelectedItem();
    	String pwr_port_name = cmbBxPowerSrcPortSelection.getSelectionModel().getSelectedItem();
    	String pwr_baud_rate = cmbBxPowerSrcBaudRate.getSelectionModel().getSelectedItem().toString();
    	String ref_type = ConstantApp.SOURCE_TYPE_REF_STD;
    	String ref_model_name = cmbBxReferanceStd_ModelName.getSelectionModel().getSelectedItem();
    	String ref_port_name = cmbBxRefStdPortSelection.getSelectionModel().getSelectedItem();
    	String ref_baud_rate = cmbBxRefStdBaudRate.getSelectionModel().getSelectedItem().toString();*/
    	String dut1_type = ConstantDut.SOURCE_TYPE_DUT1;
    	String dut2_type = ConstantDut.SOURCE_TYPE_DUT2;
    	String dut3_type = ConstantDut.SOURCE_TYPE_DUT3;
    	String dut4_type = ConstantDut.SOURCE_TYPE_DUT4;
    	String dut5_type = ConstantDut.SOURCE_TYPE_DUT5;
    	String dut6_type = ConstantDut.SOURCE_TYPE_DUT6;
    	String dut7_type = ConstantDut.SOURCE_TYPE_DUT7;
    	
    	String dut8_type = ConstantDut.SOURCE_TYPE_DUT8;
    	String dut9_type = ConstantDut.SOURCE_TYPE_DUT9;
    	String dut10_type = ConstantDut.SOURCE_TYPE_DUT10;
    	String dut11_type = ConstantDut.SOURCE_TYPE_DUT11;
    	String dut12_type = ConstantDut.SOURCE_TYPE_DUT12;
    	String dut13_type = ConstantDut.SOURCE_TYPE_DUT13;
    	String dut14_type = ConstantDut.SOURCE_TYPE_DUT14;
    	String dut15_type = ConstantDut.SOURCE_TYPE_DUT15;
    	String dut16_type = ConstantDut.SOURCE_TYPE_DUT16;
    	String dut17_type = ConstantDut.SOURCE_TYPE_DUT17;
    	String dut18_type = ConstantDut.SOURCE_TYPE_DUT18;
    	String dut19_type = ConstantDut.SOURCE_TYPE_DUT19;
    	String dut20_type = ConstantDut.SOURCE_TYPE_DUT20;
    	String dut21_type = ConstantDut.SOURCE_TYPE_DUT21;
    	String dut22_type = ConstantDut.SOURCE_TYPE_DUT22;
    	String dut23_type = ConstantDut.SOURCE_TYPE_DUT23;
    	String dut24_type = ConstantDut.SOURCE_TYPE_DUT24;
    	String dut25_type = ConstantDut.SOURCE_TYPE_DUT25;
    	String dut26_type = ConstantDut.SOURCE_TYPE_DUT26;
    	String dut27_type = ConstantDut.SOURCE_TYPE_DUT27;
    	String dut28_type = ConstantDut.SOURCE_TYPE_DUT28;
    	String dut29_type = ConstantDut.SOURCE_TYPE_DUT29;
    	String dut30_type = ConstantDut.SOURCE_TYPE_DUT30;
    	String dut31_type = ConstantDut.SOURCE_TYPE_DUT31;
    	String dut32_type = ConstantDut.SOURCE_TYPE_DUT32;
    	String dut33_type = ConstantDut.SOURCE_TYPE_DUT33;
    	String dut34_type = ConstantDut.SOURCE_TYPE_DUT34;
    	String dut35_type = ConstantDut.SOURCE_TYPE_DUT35;
    	String dut36_type = ConstantDut.SOURCE_TYPE_DUT36;
    	String dut37_type = ConstantDut.SOURCE_TYPE_DUT37;
    	String dut38_type = ConstantDut.SOURCE_TYPE_DUT38;
    	String dut39_type = ConstantDut.SOURCE_TYPE_DUT39;
    	String dut40_type = ConstantDut.SOURCE_TYPE_DUT40;
    	String dut41_type = ConstantDut.SOURCE_TYPE_DUT41;
    	String dut42_type = ConstantDut.SOURCE_TYPE_DUT42;
    	String dut43_type = ConstantDut.SOURCE_TYPE_DUT43;
    	String dut44_type = ConstantDut.SOURCE_TYPE_DUT44;
    	String dut45_type = ConstantDut.SOURCE_TYPE_DUT45;
    	String dut46_type = ConstantDut.SOURCE_TYPE_DUT46;
    	String dut47_type = ConstantDut.SOURCE_TYPE_DUT47;
    	String dut48_type = ConstantDut.SOURCE_TYPE_DUT48;
    	
    	String dut1_model_name = cmbBxDUT_ModelName1.getSelectionModel().getSelectedItem();
    	String dut2_model_name = cmbBxDUT_ModelName2.getSelectionModel().getSelectedItem();
    	String dut3_model_name = cmbBxDUT_ModelName3.getSelectionModel().getSelectedItem();
    	String dut4_model_name = cmbBxDUT_ModelName4.getSelectionModel().getSelectedItem();
    	String dut5_model_name = cmbBxDUT_ModelName5.getSelectionModel().getSelectedItem();
    	String dut6_model_name = cmbBxDUT_ModelName6.getSelectionModel().getSelectedItem();
    	String dut7_model_name = cmbBxDUT_ModelName7.getSelectionModel().getSelectedItem();
    	
    	String dut8_model_name = cmbBxDUT_ModelName8.getSelectionModel().getSelectedItem();
    	String dut9_model_name = cmbBxDUT_ModelName9.getSelectionModel().getSelectedItem();
    	String dut10_model_name = cmbBxDUT_ModelName10.getSelectionModel().getSelectedItem();
    	String dut11_model_name = cmbBxDUT_ModelName11.getSelectionModel().getSelectedItem();
    	String dut12_model_name = cmbBxDUT_ModelName12.getSelectionModel().getSelectedItem();
    	String dut13_model_name = cmbBxDUT_ModelName13.getSelectionModel().getSelectedItem();
    	String dut14_model_name = cmbBxDUT_ModelName14.getSelectionModel().getSelectedItem();
    	String dut15_model_name = cmbBxDUT_ModelName15.getSelectionModel().getSelectedItem();
    	String dut16_model_name = cmbBxDUT_ModelName16.getSelectionModel().getSelectedItem();
    	String dut17_model_name = cmbBxDUT_ModelName17.getSelectionModel().getSelectedItem();
    	String dut18_model_name = cmbBxDUT_ModelName18.getSelectionModel().getSelectedItem();
    	String dut19_model_name = cmbBxDUT_ModelName19.getSelectionModel().getSelectedItem();
    	String dut20_model_name = cmbBxDUT_ModelName20.getSelectionModel().getSelectedItem();
    	String dut21_model_name = cmbBxDUT_ModelName21.getSelectionModel().getSelectedItem();
    	String dut22_model_name = cmbBxDUT_ModelName22.getSelectionModel().getSelectedItem();
    	String dut23_model_name = cmbBxDUT_ModelName23.getSelectionModel().getSelectedItem();
    	String dut24_model_name = cmbBxDUT_ModelName24.getSelectionModel().getSelectedItem();
    	String dut25_model_name = cmbBxDUT_ModelName25.getSelectionModel().getSelectedItem();
    	String dut26_model_name = cmbBxDUT_ModelName26.getSelectionModel().getSelectedItem();
    	String dut27_model_name = cmbBxDUT_ModelName27.getSelectionModel().getSelectedItem();
    	String dut28_model_name = cmbBxDUT_ModelName28.getSelectionModel().getSelectedItem();
    	String dut29_model_name = cmbBxDUT_ModelName29.getSelectionModel().getSelectedItem();
    	String dut30_model_name = cmbBxDUT_ModelName30.getSelectionModel().getSelectedItem();
    	String dut31_model_name = cmbBxDUT_ModelName31.getSelectionModel().getSelectedItem();
    	String dut32_model_name = cmbBxDUT_ModelName32.getSelectionModel().getSelectedItem();
    	String dut33_model_name = cmbBxDUT_ModelName33.getSelectionModel().getSelectedItem();
    	String dut34_model_name = cmbBxDUT_ModelName34.getSelectionModel().getSelectedItem();
    	String dut35_model_name = cmbBxDUT_ModelName35.getSelectionModel().getSelectedItem();
    	String dut36_model_name = cmbBxDUT_ModelName36.getSelectionModel().getSelectedItem();
    	String dut37_model_name = cmbBxDUT_ModelName37.getSelectionModel().getSelectedItem();
    	String dut38_model_name = cmbBxDUT_ModelName38.getSelectionModel().getSelectedItem();
    	String dut39_model_name = cmbBxDUT_ModelName39.getSelectionModel().getSelectedItem();
    	String dut40_model_name = cmbBxDUT_ModelName40.getSelectionModel().getSelectedItem();


    	
    	String dut_port_name1 = cmbBxDUT_PortSelection1.getSelectionModel().getSelectedItem();
    	String dut_port_name2 = cmbBxDUT_PortSelection2.getSelectionModel().getSelectedItem();
    	String dut_port_name3 = cmbBxDUT_PortSelection3.getSelectionModel().getSelectedItem();
    	String dut_port_name4 = cmbBxDUT_PortSelection4.getSelectionModel().getSelectedItem();
    	String dut_port_name5 = cmbBxDUT_PortSelection5.getSelectionModel().getSelectedItem();
    	String dut_port_name6 = cmbBxDUT_PortSelection6.getSelectionModel().getSelectedItem();
    	String dut_port_name7 = cmbBxDUT_PortSelection7.getSelectionModel().getSelectedItem();
    	
    	String dut_port_name8 = cmbBxDUT_PortSelection8.getSelectionModel().getSelectedItem();
    	String dut_port_name9 = cmbBxDUT_PortSelection9.getSelectionModel().getSelectedItem();
    	String dut_port_name10 = cmbBxDUT_PortSelection10.getSelectionModel().getSelectedItem();
    	String dut_port_name11 = cmbBxDUT_PortSelection11.getSelectionModel().getSelectedItem();
    	String dut_port_name12 = cmbBxDUT_PortSelection12.getSelectionModel().getSelectedItem();
    	String dut_port_name13 = cmbBxDUT_PortSelection13.getSelectionModel().getSelectedItem();
    	String dut_port_name14 = cmbBxDUT_PortSelection14.getSelectionModel().getSelectedItem();
    	String dut_port_name15 = cmbBxDUT_PortSelection15.getSelectionModel().getSelectedItem();
    	String dut_port_name16 = cmbBxDUT_PortSelection16.getSelectionModel().getSelectedItem();
    	String dut_port_name17 = cmbBxDUT_PortSelection17.getSelectionModel().getSelectedItem();
    	String dut_port_name18 = cmbBxDUT_PortSelection18.getSelectionModel().getSelectedItem();
    	String dut_port_name19 = cmbBxDUT_PortSelection19.getSelectionModel().getSelectedItem();
    	String dut_port_name20 = cmbBxDUT_PortSelection20.getSelectionModel().getSelectedItem();
    	String dut_port_name21 = cmbBxDUT_PortSelection21.getSelectionModel().getSelectedItem();
    	String dut_port_name22 = cmbBxDUT_PortSelection22.getSelectionModel().getSelectedItem();
    	String dut_port_name23 = cmbBxDUT_PortSelection23.getSelectionModel().getSelectedItem();
    	String dut_port_name24 = cmbBxDUT_PortSelection24.getSelectionModel().getSelectedItem();
    	String dut_port_name25 = cmbBxDUT_PortSelection25.getSelectionModel().getSelectedItem();
    	String dut_port_name26 = cmbBxDUT_PortSelection26.getSelectionModel().getSelectedItem();
    	String dut_port_name27 = cmbBxDUT_PortSelection27.getSelectionModel().getSelectedItem();
    	String dut_port_name28 = cmbBxDUT_PortSelection28.getSelectionModel().getSelectedItem();
    	String dut_port_name29 = cmbBxDUT_PortSelection29.getSelectionModel().getSelectedItem();
    	String dut_port_name30 = cmbBxDUT_PortSelection30.getSelectionModel().getSelectedItem();
    	String dut_port_name31 = cmbBxDUT_PortSelection31.getSelectionModel().getSelectedItem();
    	String dut_port_name32 = cmbBxDUT_PortSelection32.getSelectionModel().getSelectedItem();
    	String dut_port_name33 = cmbBxDUT_PortSelection33.getSelectionModel().getSelectedItem();
    	String dut_port_name34 = cmbBxDUT_PortSelection34.getSelectionModel().getSelectedItem();
    	String dut_port_name35 = cmbBxDUT_PortSelection35.getSelectionModel().getSelectedItem();
    	String dut_port_name36 = cmbBxDUT_PortSelection36.getSelectionModel().getSelectedItem();
    	String dut_port_name37 = cmbBxDUT_PortSelection37.getSelectionModel().getSelectedItem();
    	String dut_port_name38 = cmbBxDUT_PortSelection38.getSelectionModel().getSelectedItem();
    	String dut_port_name39 = cmbBxDUT_PortSelection39.getSelectionModel().getSelectedItem();
    	String dut_port_name40 = cmbBxDUT_PortSelection40.getSelectionModel().getSelectedItem();
    	

    	
    	String dut1_baud_rate = cmbBxDUT_BaudRate1.getSelectionModel().getSelectedItem().toString();
    	String dut2_baud_rate = cmbBxDUT_BaudRate2.getSelectionModel().getSelectedItem().toString();
    	String dut3_baud_rate = cmbBxDUT_BaudRate3.getSelectionModel().getSelectedItem().toString();
    	String dut4_baud_rate = cmbBxDUT_BaudRate4.getSelectionModel().getSelectedItem().toString();
    	String dut5_baud_rate = cmbBxDUT_BaudRate5.getSelectionModel().getSelectedItem().toString();
    	String dut6_baud_rate = cmbBxDUT_BaudRate6.getSelectionModel().getSelectedItem().toString();
    	String dut7_baud_rate = cmbBxDUT_BaudRate7.getSelectionModel().getSelectedItem().toString();
    	
    	String dut8_baud_rate = cmbBxDUT_BaudRate8.getSelectionModel().getSelectedItem().toString();
    	String dut9_baud_rate = cmbBxDUT_BaudRate9.getSelectionModel().getSelectedItem().toString();
    	String dut10_baud_rate = cmbBxDUT_BaudRate10.getSelectionModel().getSelectedItem().toString();
    	String dut11_baud_rate = cmbBxDUT_BaudRate11.getSelectionModel().getSelectedItem().toString();
    	String dut12_baud_rate = cmbBxDUT_BaudRate12.getSelectionModel().getSelectedItem().toString();
    	String dut13_baud_rate = cmbBxDUT_BaudRate13.getSelectionModel().getSelectedItem().toString();
    	String dut14_baud_rate = cmbBxDUT_BaudRate14.getSelectionModel().getSelectedItem().toString();
    	String dut15_baud_rate = cmbBxDUT_BaudRate15.getSelectionModel().getSelectedItem().toString();
    	String dut16_baud_rate = cmbBxDUT_BaudRate16.getSelectionModel().getSelectedItem().toString();
    	String dut17_baud_rate = cmbBxDUT_BaudRate17.getSelectionModel().getSelectedItem().toString();
    	String dut18_baud_rate = cmbBxDUT_BaudRate18.getSelectionModel().getSelectedItem().toString();
    	String dut19_baud_rate = cmbBxDUT_BaudRate19.getSelectionModel().getSelectedItem().toString();
    	String dut20_baud_rate = cmbBxDUT_BaudRate20.getSelectionModel().getSelectedItem().toString();
    	String dut21_baud_rate = cmbBxDUT_BaudRate21.getSelectionModel().getSelectedItem().toString();
    	String dut22_baud_rate = cmbBxDUT_BaudRate22.getSelectionModel().getSelectedItem().toString();
    	String dut23_baud_rate = cmbBxDUT_BaudRate23.getSelectionModel().getSelectedItem().toString();
    	String dut24_baud_rate = cmbBxDUT_BaudRate24.getSelectionModel().getSelectedItem().toString();
    	String dut25_baud_rate = cmbBxDUT_BaudRate25.getSelectionModel().getSelectedItem().toString();
    	String dut26_baud_rate = cmbBxDUT_BaudRate26.getSelectionModel().getSelectedItem().toString();
    	String dut27_baud_rate = cmbBxDUT_BaudRate27.getSelectionModel().getSelectedItem().toString();
    	String dut28_baud_rate = cmbBxDUT_BaudRate28.getSelectionModel().getSelectedItem().toString();
    	String dut29_baud_rate = cmbBxDUT_BaudRate29.getSelectionModel().getSelectedItem().toString();
    	String dut30_baud_rate = cmbBxDUT_BaudRate30.getSelectionModel().getSelectedItem().toString();
    	String dut31_baud_rate = cmbBxDUT_BaudRate31.getSelectionModel().getSelectedItem().toString();
    	String dut32_baud_rate = cmbBxDUT_BaudRate32.getSelectionModel().getSelectedItem().toString();
    	String dut33_baud_rate = cmbBxDUT_BaudRate33.getSelectionModel().getSelectedItem().toString();
    	String dut34_baud_rate = cmbBxDUT_BaudRate34.getSelectionModel().getSelectedItem().toString();
    	String dut35_baud_rate = cmbBxDUT_BaudRate35.getSelectionModel().getSelectedItem().toString();
    	String dut36_baud_rate = cmbBxDUT_BaudRate36.getSelectionModel().getSelectedItem().toString();
    	String dut37_baud_rate = cmbBxDUT_BaudRate37.getSelectionModel().getSelectedItem().toString();
    	String dut38_baud_rate = cmbBxDUT_BaudRate38.getSelectionModel().getSelectedItem().toString();
    	String dut39_baud_rate = cmbBxDUT_BaudRate39.getSelectionModel().getSelectedItem().toString();
    	String dut40_baud_rate = cmbBxDUT_BaudRate40.getSelectionModel().getSelectedItem().toString();

    	
    	
/*    	MySQL_Controller.sp_add_device_settings(1, pwr_type, pwr_model_name, pwr_port_name, pwr_baud_rate);
    	MySQL_Controller.sp_add_device_settings(2, ref_type, ref_model_name, ref_port_name, ref_baud_rate);*/
    	if( (!dut1_model_name.isEmpty()) && (!dut_port_name1.isEmpty())  && (!dut1_baud_rate.isEmpty())){
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT01_ID, dut1_type, dut1_model_name, dut_port_name1, dut1_baud_rate);
    	}   
    	if( (!dut2_model_name.isEmpty()) && (!dut_port_name2.isEmpty())  && (!dut2_baud_rate.isEmpty())){   
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT02_ID, dut2_type, dut2_model_name, dut_port_name2, dut2_baud_rate);
    	}   
    	if( (!dut3_model_name.isEmpty()) && (!dut_port_name3.isEmpty())  && (!dut3_baud_rate.isEmpty())){   
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT03_ID, dut3_type, dut3_model_name, dut_port_name3, dut3_baud_rate);
    	}   
    	if( (!dut4_model_name.isEmpty()) && (!dut_port_name4.isEmpty())  && (!dut4_baud_rate.isEmpty())){   
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT04_ID, dut4_type, dut4_model_name, dut_port_name4, dut4_baud_rate);
    	}   
    	if( (!dut5_model_name.isEmpty()) && (!dut_port_name5.isEmpty())  && (!dut5_baud_rate.isEmpty())){   
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT05_ID, dut5_type, dut5_model_name, dut_port_name5, dut5_baud_rate);
    	}   
    	if( (!dut6_model_name.isEmpty()) && (!dut_port_name6.isEmpty())  && (!dut6_baud_rate.isEmpty())){   
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT06_ID, dut6_type, dut6_model_name, dut_port_name6, dut6_baud_rate);
    	}   
    	if( (!dut7_model_name.isEmpty()) && (!dut_port_name7.isEmpty())  && (!dut7_baud_rate.isEmpty())){   
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT07_ID, dut7_type, dut7_model_name, dut_port_name7, dut7_baud_rate);
    	
    	}   
    	if( (!dut8_model_name.isEmpty()) && (!dut_port_name8.isEmpty())  && (!dut8_baud_rate.isEmpty())){   
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT08_ID, dut8_type, dut8_model_name, dut_port_name8, dut8_baud_rate);
    	}   
    	if( (!dut9_model_name.isEmpty()) && (!dut_port_name9.isEmpty())  && (!dut9_baud_rate.isEmpty())){   
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT09_ID, dut9_type, dut9_model_name, dut_port_name9, dut9_baud_rate);
    	}   
    	if( (!dut10_model_name.isEmpty()) && (!dut_port_name10.isEmpty())  && (!dut10_baud_rate.isEmpty())){   
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT10_ID, dut10_type, dut10_model_name, dut_port_name10, dut10_baud_rate);
    	}   
    	if( (!dut11_model_name.isEmpty()) && (!dut_port_name11.isEmpty())  && (!dut11_baud_rate.isEmpty())){   
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT11_ID, dut11_type, dut11_model_name, dut_port_name11, dut11_baud_rate);
    	}   
    	if( (!dut12_model_name.isEmpty()) && (!dut_port_name12.isEmpty())  && (!dut12_baud_rate.isEmpty())){   
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT12_ID, dut12_type, dut12_model_name, dut_port_name12, dut12_baud_rate);
    	}   
    	if( (!dut13_model_name.isEmpty()) && (!dut_port_name13.isEmpty())  && (!dut13_baud_rate.isEmpty())){   
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT13_ID, dut13_type, dut13_model_name, dut_port_name13, dut13_baud_rate);
    	}   
    	if( (!dut14_model_name.isEmpty()) && (!dut_port_name14.isEmpty())  && (!dut14_baud_rate.isEmpty())){   
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT14_ID, dut14_type, dut14_model_name, dut_port_name14, dut14_baud_rate);
    	}   
    	if( (!dut15_model_name.isEmpty()) && (!dut_port_name15.isEmpty())  && (!dut15_baud_rate.isEmpty())){   
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT15_ID, dut15_type, dut15_model_name, dut_port_name15, dut15_baud_rate);
    	}   
    	if( (!dut16_model_name.isEmpty()) && (!dut_port_name16.isEmpty())  && (!dut16_baud_rate.isEmpty())){   
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT16_ID, dut16_type, dut16_model_name, dut_port_name16, dut16_baud_rate);
    	}   
    	if( (!dut17_model_name.isEmpty()) && (!dut_port_name17.isEmpty())  && (!dut17_baud_rate.isEmpty())){   
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT17_ID, dut17_type, dut17_model_name, dut_port_name17, dut17_baud_rate);
    	}   
    	if( (!dut18_model_name.isEmpty()) && (!dut_port_name18.isEmpty())  && (!dut18_baud_rate.isEmpty())){   
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT18_ID, dut18_type, dut18_model_name, dut_port_name18, dut18_baud_rate);
    	}   
    	if( (!dut19_model_name.isEmpty()) && (!dut_port_name19.isEmpty())  && (!dut19_baud_rate.isEmpty())){   
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT19_ID, dut19_type, dut19_model_name, dut_port_name19, dut19_baud_rate);
    	}   
    	if( (!dut20_model_name.isEmpty()) && (!dut_port_name20.isEmpty())  && (!dut20_baud_rate.isEmpty())){   
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT20_ID, dut20_type, dut20_model_name, dut_port_name20, dut20_baud_rate);
    	}   
    	if( (!dut21_model_name.isEmpty()) && (!dut_port_name21.isEmpty())  && (!dut21_baud_rate.isEmpty())){   
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT21_ID, dut21_type, dut21_model_name, dut_port_name21, dut21_baud_rate);
    	}   
    	if( (!dut22_model_name.isEmpty()) && (!dut_port_name22.isEmpty())  && (!dut22_baud_rate.isEmpty())){   
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT22_ID, dut22_type, dut22_model_name, dut_port_name22, dut22_baud_rate);
    	}   
    	if( (!dut23_model_name.isEmpty()) && (!dut_port_name23.isEmpty())  && (!dut23_baud_rate.isEmpty())){   
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT23_ID, dut23_type, dut23_model_name, dut_port_name23, dut23_baud_rate);
    	}   
    	if( (!dut24_model_name.isEmpty()) && (!dut_port_name24.isEmpty())  && (!dut24_baud_rate.isEmpty())){   
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT24_ID, dut24_type, dut24_model_name, dut_port_name24, dut24_baud_rate);
    	}   
    	if( (!dut25_model_name.isEmpty()) && (!dut_port_name25.isEmpty())  && (!dut25_baud_rate.isEmpty())){   
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT25_ID, dut25_type, dut25_model_name, dut_port_name25, dut25_baud_rate);
    	}   
    	if( (!dut26_model_name.isEmpty()) && (!dut_port_name26.isEmpty())  && (!dut26_baud_rate.isEmpty())){   
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT26_ID, dut26_type, dut26_model_name, dut_port_name26, dut26_baud_rate);
    	}   
    	if( (!dut27_model_name.isEmpty()) && (!dut_port_name27.isEmpty())  && (!dut27_baud_rate.isEmpty())){   
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT27_ID, dut27_type, dut27_model_name, dut_port_name27, dut27_baud_rate);
    	}   
    	
    	if( (!dut28_model_name.isEmpty()) && (!dut_port_name28.isEmpty())  && (!dut28_baud_rate.isEmpty())){   
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT28_ID, dut28_type, dut28_model_name, dut_port_name28, dut28_baud_rate);
    	}   
    	if( (!dut29_model_name.isEmpty()) && (!dut_port_name29.isEmpty())  && (!dut29_baud_rate.isEmpty())){   
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT29_ID, dut29_type, dut29_model_name, dut_port_name29, dut29_baud_rate);
    	}   
    	if( (!dut30_model_name.isEmpty()) && (!dut_port_name30.isEmpty())  && (!dut30_baud_rate.isEmpty())){   
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT30_ID, dut30_type, dut30_model_name, dut_port_name30, dut30_baud_rate);
    	}   
    	if( (!dut31_model_name.isEmpty()) && (!dut_port_name31.isEmpty())  && (!dut31_baud_rate.isEmpty())){   
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT31_ID, dut31_type, dut31_model_name, dut_port_name31, dut31_baud_rate);
    	}   
    	if( (!dut32_model_name.isEmpty()) && (!dut_port_name32.isEmpty())  && (!dut32_baud_rate.isEmpty())){   
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT32_ID, dut32_type, dut32_model_name, dut_port_name32, dut32_baud_rate);
    	}   
    	if( (!dut33_model_name.isEmpty()) && (!dut_port_name33.isEmpty())  && (!dut33_baud_rate.isEmpty())){   
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT33_ID, dut33_type, dut33_model_name, dut_port_name33, dut33_baud_rate);
    	}   
    	if( (!dut34_model_name.isEmpty()) && (!dut_port_name34.isEmpty())  && (!dut34_baud_rate.isEmpty())){   
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT34_ID, dut34_type, dut34_model_name, dut_port_name34, dut34_baud_rate);
    	}   
    	if( (!dut35_model_name.isEmpty()) && (!dut_port_name35.isEmpty())  && (!dut35_baud_rate.isEmpty())){   
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT35_ID, dut35_type, dut35_model_name, dut_port_name35, dut35_baud_rate);
    	}   
    	if( (!dut36_model_name.isEmpty()) && (!dut_port_name36.isEmpty())  && (!dut36_baud_rate.isEmpty())){   
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT36_ID, dut36_type, dut36_model_name, dut_port_name36, dut36_baud_rate);
    	}   
    	if( (!dut37_model_name.isEmpty()) && (!dut_port_name37.isEmpty())  && (!dut37_baud_rate.isEmpty())){   
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT37_ID, dut37_type, dut37_model_name, dut_port_name37, dut37_baud_rate);
    	}   
    	if( (!dut38_model_name.isEmpty()) && (!dut_port_name38.isEmpty())  && (!dut38_baud_rate.isEmpty())){   
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT38_ID, dut38_type, dut38_model_name, dut_port_name38, dut38_baud_rate);
    	}   
    	if( (!dut39_model_name.isEmpty()) && (!dut_port_name39.isEmpty())  && (!dut39_baud_rate.isEmpty())){   
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT39_ID, dut39_type, dut39_model_name, dut_port_name39, dut39_baud_rate);
    	}   
    	if( (!dut40_model_name.isEmpty()) && (!dut_port_name40.isEmpty())  && (!dut40_baud_rate.isEmpty())){   
    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT40_ID, dut40_type, dut40_model_name, dut_port_name40, dut40_baud_rate);
    	}
    	
    	if((ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK > 40) && (ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK == ProcalFeatureEnable.RACK_MAX_POSITION)){
    	    
	    	String dut41_model_name = cmbBxDUT_ModelName41.getSelectionModel().getSelectedItem();
	    	String dut42_model_name = cmbBxDUT_ModelName42.getSelectionModel().getSelectedItem();
	    	String dut43_model_name = cmbBxDUT_ModelName43.getSelectionModel().getSelectedItem();
	    	String dut44_model_name = cmbBxDUT_ModelName44.getSelectionModel().getSelectedItem();
	    	String dut45_model_name = cmbBxDUT_ModelName45.getSelectionModel().getSelectedItem();
	    	String dut46_model_name = cmbBxDUT_ModelName46.getSelectionModel().getSelectedItem();
	    	String dut47_model_name = cmbBxDUT_ModelName47.getSelectionModel().getSelectedItem();
	    	String dut48_model_name = cmbBxDUT_ModelName48.getSelectionModel().getSelectedItem();
    		
	    	String dut_port_name41 = cmbBxDUT_PortSelection41.getSelectionModel().getSelectedItem();
	    	String dut_port_name42 = cmbBxDUT_PortSelection42.getSelectionModel().getSelectedItem();
	    	String dut_port_name43 = cmbBxDUT_PortSelection43.getSelectionModel().getSelectedItem();
	    	String dut_port_name44 = cmbBxDUT_PortSelection44.getSelectionModel().getSelectedItem();
	    	String dut_port_name45 = cmbBxDUT_PortSelection45.getSelectionModel().getSelectedItem();
	    	String dut_port_name46 = cmbBxDUT_PortSelection46.getSelectionModel().getSelectedItem();
	    	String dut_port_name47 = cmbBxDUT_PortSelection47.getSelectionModel().getSelectedItem();
	    	String dut_port_name48 = cmbBxDUT_PortSelection48.getSelectionModel().getSelectedItem();
    		
    		String dut41_baud_rate = cmbBxDUT_BaudRate41.getSelectionModel().getSelectedItem().toString();
        	String dut42_baud_rate = cmbBxDUT_BaudRate42.getSelectionModel().getSelectedItem().toString();
        	String dut43_baud_rate = cmbBxDUT_BaudRate43.getSelectionModel().getSelectedItem().toString();
        	String dut44_baud_rate = cmbBxDUT_BaudRate44.getSelectionModel().getSelectedItem().toString();
        	String dut45_baud_rate = cmbBxDUT_BaudRate45.getSelectionModel().getSelectedItem().toString();
        	String dut46_baud_rate = cmbBxDUT_BaudRate46.getSelectionModel().getSelectedItem().toString();
        	String dut47_baud_rate = cmbBxDUT_BaudRate47.getSelectionModel().getSelectedItem().toString();
        	String dut48_baud_rate = cmbBxDUT_BaudRate48.getSelectionModel().getSelectedItem().toString();
        	
	        if( (!dut41_model_name.isEmpty()) && (!dut_port_name41.isEmpty()) && (!dut41_baud_rate.isEmpty()) ){   
	        	MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT41_ID, dut41_type, dut41_model_name, dut_port_name41, dut41_baud_rate);
	    	}   
	        if( (!dut42_model_name.isEmpty()) && (!dut_port_name42.isEmpty()) && (!dut42_baud_rate.isEmpty()) ){   
	        	MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT42_ID, dut42_type, dut42_model_name, dut_port_name42, dut42_baud_rate);
	    	}   
	        if( (!dut43_model_name.isEmpty()) && (!dut_port_name43.isEmpty()) && (!dut43_baud_rate.isEmpty()) ){   
	        	MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT43_ID, dut43_type, dut43_model_name, dut_port_name43, dut43_baud_rate);
	    	}   
	        if( (!dut44_model_name.isEmpty()) && (!dut_port_name44.isEmpty()) && (!dut44_baud_rate.isEmpty()) ){   
	        	MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT44_ID, dut44_type, dut44_model_name, dut_port_name44, dut44_baud_rate);
	    	}   
	        if( (!dut45_model_name.isEmpty()) && (!dut_port_name45.isEmpty()) && (!dut45_baud_rate.isEmpty()) ){   
	        	MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT45_ID, dut45_type, dut45_model_name, dut_port_name45, dut45_baud_rate);
	    	}   
	        if( (!dut46_model_name.isEmpty()) && (!dut_port_name46.isEmpty()) && (!dut46_baud_rate.isEmpty()) ){   
	        	MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT46_ID, dut46_type, dut46_model_name, dut_port_name46, dut46_baud_rate);
	        }
	    	
	    	if( (!dut47_model_name.isEmpty()) && (!dut_port_name47.isEmpty()) && (!dut47_baud_rate.isEmpty()) ){   
	    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT47_ID, dut47_type, dut47_model_name, dut_port_name47, dut47_baud_rate);
	    	}
	    	
	    	if( (!dut48_model_name.isEmpty()) && (!dut_port_name48.isEmpty()) && (!dut48_baud_rate.isEmpty()) ){   
	    		MySQL_Controller.sp_add_device_settings(ConstantAppConfig.DEVICE_SETTINGS_DUT48_ID, dut48_type, dut48_model_name, dut_port_name48, dut48_baud_rate);
	    	}
    	
    	}

    	
    	ApplicationLauncher.InformUser("Saved Successfully", "Saved data successfully", AlertType.INFORMATION);
    	
    }
    

    

    
    
/*    public void PwrSrcValidateSerialCmd(){
    	
    	String PowerSrcCommPortID= null;
    	String PwrSrcCommBaudRate = null;
    	txtValidatePwrSrcCmdStatus.clear();
    	try{
	    	dutSerialDM_Obj.commPowerSrc.searchForPorts(); 
	    	PowerSrcCommPortID = getCurrentPwrSrcComPortID();
	    	PwrSrcCommBaudRate = getCurrentPwrSrcComBaudRate();
	    	boolean status = dutSerialDM_Obj.pwrSrc_CommInit(PowerSrcCommPortID,PwrSrcCommBaudRate);
	    	
	    	if (!status){
	    		
	    		txtValidatePwrSrcCmdStatus.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		status = dutSerialDM_Obj.SetPowerSourceOff();
	    		if (!status){
	    			txtValidatePwrSrcCmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidatePwrSrcCmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		
	    	}
	    	ApplicationLauncher.logger.info("PwrSrcValidateSerialCmd: testD:"+dutSerialDM_Obj.commPowerSrc.getPortDeviceMapping());
	    	dutSerialDM_Obj.DisconnectPwrSrc();
	    	
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("PwrSrcValidateSerialCmd: Exception1:"+e.getMessage());
    		//ApplicationLauncher.logger.info("PwrSrcValidateSerialCmd: Exception:"+e.toString());
    		txtValidatePwrSrcCmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
    	}
    }
    
    public void RefStdValidateSerialCmd(){
    	
    	String RefStdCommPortID= null;
    	String RefStdCommBaudRate = null;
    	txtValidateRefStdCmdStatus.clear();
    	try{
	    	dutSerialDM_Obj.commRefStandard.searchForPorts(); 
	    	RefStdCommPortID = getCurrentRefStdComPortID();
	    	RefStdCommBaudRate = getCurrentRefStdComBaudRate();
	    	boolean status = dutSerialDM_Obj.RefStdComInit(RefStdCommPortID,RefStdCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateRefStdCmdStatus.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {

	    		status = dutSerialDM_Obj.mteRefStd_ValidateVersionCMD();
	    		if (!status){
	    			txtValidateRefStdCmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateRefStdCmdStatus.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		
	    	}
	    	dutSerialDM_Obj.DisconnectRefStd();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("RefStdValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }*/
    
    class MyRunnable implements Runnable{
        

        double count ;
    	TextField l_txtValidateRefStdCmdStatus;
 
        public MyRunnable(TextField ValidateRefStdCmdStatus) {
        	count = 0;
        	l_txtValidateRefStdCmdStatus= ValidateRefStdCmdStatus;
        }
 
        @Override
        public void run() {
            for (int i = 0; i <= count; i++) {
                 
                final double update_i = count;
                 
                
            	ApplicationLauncher.logger.info("RefStdValidateSerialCmd:runLater1:test2");
            	l_txtValidateRefStdCmdStatus.setText("Sending CMD"+update_i);
            	ApplicationLauncher.logger.info("RefStdValidateSerialCmd:runLater1:test3");
                 
                //Update JavaFX UI with runLater() in UI thread
                Platform.runLater(new Runnable(){
 
                    @Override
                    public void run() {
                        for (int j = 0; j <= update_i; j++) {
	                    	ApplicationLauncher.logger.info("RefStdValidateSerialCmd:runLater2:test2");
	                    	ApplicationLauncher.logger.info("RefStdValidateSerialCmd:runLater2:test3");
	                    		ApplicationLauncher.logger.info("RefStdValidateSerialCmd:runLater2:SleepEntry");
	                        	ApplicationLauncher.logger.info("RefStdValidateSerialCmd:runLater2:Exception");
	                            
	                        
	                    	ApplicationLauncher.logger.info("RefStdValidateSerialCmd:runLater2:NextForloop");
                            
                    	}
                    }
                });
                 

            }
        }
         
    }
    
    
    
    class UI_DisplayTimerTask extends TimerTask{
    	 

        double count =10;
    	TextField l_txtValidateRefStdCmdStatus;
 
        public UI_DisplayTimerTask(TextField ValidateRefStdCmdStatus) {

        	l_txtValidateRefStdCmdStatus= ValidateRefStdCmdStatus;
        	
        }
 
        @Override
        public void run() {
        	for(int i=0;i<count;i++){
	        	ApplicationLauncher.logger.info("RefStdValidateSerialCmd:test2");
	        	l_txtValidateRefStdCmdStatus.setText("Sending CMD"+i);
	        	ApplicationLauncher.logger.info("RefStdValidateSerialCmd:test3");

	        	try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					
					e.printStackTrace();
					ApplicationLauncher.logger.error("UI_DisplayTimerTask: InterruptedException: "+e.getMessage());
				}
        	}
        	
        	UI_DisplayTimer.cancel();

             
        }
         
    }
    
    
/*    public void DUT_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus1.clear();
    	try{
	    	dutSerialDM_Obj.commDUT1.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID1();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate1();
	    	boolean status = dutSerialDM_Obj.DUT1_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus1.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
				DisplayDataObj.set_Error_min("-1.00");
				DisplayDataObj.set_Error_max("+1.00");
				DisplayDataObj.setNoOfPulses("10");
				
				serialDM_Obj.setDUT1_ReadDataFlag(true);
	    		status = dutSerialDM_Obj.DUT_ResetSetting();
	    		if (!status){
	    			txtValidateDUT_CmdStatus1.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus1.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		serialDM_Obj.setDUT1_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT1();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }*/
    
    public void lscsDUT1_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus1.clear();
    	try{
	    	dutSerialDM_Obj.commDUT1.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID1();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate1();
	    	boolean status = dutSerialDM_Obj.DUT1_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus1.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
	    		status = dutSerialDM_Obj.lscsDUT1_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus1.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus1.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT1_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT1();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT1_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
    
    public void lscsDUT2_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus2.clear();
    	try{
	    	dutSerialDM_Obj.commDUT2.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID2();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate2();
	    	boolean status = dutSerialDM_Obj.DUT2_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus2.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-2.00");
				DisplayDataObj.set_Error_max("+2.00");
				DisplayDataObj.setNoOfPulses("20");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT2_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus2.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus2.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT2_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT2();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT2_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
    
    public void lscsDUT3_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus3.clear();
    	try{
	    	dutSerialDM_Obj.commDUT3.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID3();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate3();
	    	boolean status = dutSerialDM_Obj.DUT3_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus3.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-3.00");
				DisplayDataObj.set_Error_max("+3.00");
				DisplayDataObj.setNoOfPulses("30");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT3_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus3.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus3.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT3_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT3();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT3_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
    
    public void lscsDUT4_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus4.clear();
    	try{
	    	dutSerialDM_Obj.commDUT4.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID4();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate4();
	    	boolean status = dutSerialDM_Obj.DUT4_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus4.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-4.00");
				DisplayDataObj.set_Error_max("+4.00");
				DisplayDataObj.setNoOfPulses("40");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT4_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus4.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus4.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT4_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT4();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT4_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
    
    public void lscsDUT5_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus5.clear();
    	try{
	    	dutSerialDM_Obj.commDUT5.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID5();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate5();
	    	boolean status = dutSerialDM_Obj.DUT5_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus5.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-5.00");
				DisplayDataObj.set_Error_max("+5.00");
				DisplayDataObj.setNoOfPulses("50");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT5_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus5.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus5.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT5_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT5();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT5_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
    
    public void lscsDUT6_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus6.clear();
    	try{
	    	dutSerialDM_Obj.commDUT6.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID6();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate6();
	    	boolean status = dutSerialDM_Obj.DUT6_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus6.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-6.00");
				DisplayDataObj.set_Error_max("+6.00");
				DisplayDataObj.setNoOfPulses("60");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT6_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus6.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus6.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT6_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT6();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT6_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
    
    public void lscsDUT7_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus7.clear();
    	try{
	    	dutSerialDM_Obj.commDUT7.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID7();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate7();
	    	boolean status = dutSerialDM_Obj.DUT7_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus7.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-7.00");
				DisplayDataObj.set_Error_max("+7.00");
				DisplayDataObj.setNoOfPulses("70");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT7_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus7.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus7.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT7_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT7();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT7_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
    
	public void lscsDUT8_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus8.clear();
    	try{
	    	dutSerialDM_Obj.commDUT8.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID8();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate8();
	    	boolean status = dutSerialDM_Obj.DUT8_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus8.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-8.00");
				DisplayDataObj.set_Error_max("+8.00");
				DisplayDataObj.setNoOfPulses("80");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT8_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus8.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus8.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT8_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT8();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT8_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
	

	

	



		
		
	public void lscsDUT9_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus9.clear();
    	try{
	    	dutSerialDM_Obj.commDUT9.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID9();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate9();
	    	boolean status = dutSerialDM_Obj.DUT9_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus9.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-9.00");
				DisplayDataObj.set_Error_max("+9.00");
				DisplayDataObj.setNoOfPulses("90");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT9_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus9.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus9.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT9_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT9();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT9_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
	


		
		
	public void lscsDUT10_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus10.clear();
    	try{
	    	dutSerialDM_Obj.commDUT10.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID10();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate10();
	    	boolean status = dutSerialDM_Obj.DUT10_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus10.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-10.00");
				DisplayDataObj.set_Error_max("+10.00");
				DisplayDataObj.setNoOfPulses("100");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT10_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus10.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus10.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT10_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT10();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT10_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
	


		
		
	public void lscsDUT11_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus11.clear();
    	try{
	    	dutSerialDM_Obj.commDUT11.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID11();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate11();
	    	boolean status = dutSerialDM_Obj.DUT11_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus11.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-11.00");
				DisplayDataObj.set_Error_max("+11.00");
				DisplayDataObj.setNoOfPulses("110");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT11_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus11.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus11.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT11_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT11();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT11_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
	

	

	

		
	public void lscsDUT12_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus12.clear();
    	try{
	    	dutSerialDM_Obj.commDUT12.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID12();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate12();
	    	boolean status = dutSerialDM_Obj.DUT12_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus12.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-12.00");
				DisplayDataObj.set_Error_max("+12.00");
				DisplayDataObj.setNoOfPulses("120");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT12_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus12.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus12.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT12_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT12();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT12_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
	

	


	public void lscsDUT13_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus13.clear();
    	try{
	    	dutSerialDM_Obj.commDUT13.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID13();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate13();
	    	boolean status = dutSerialDM_Obj.DUT13_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus13.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-13.00");
				DisplayDataObj.set_Error_max("+13.00");
				DisplayDataObj.setNoOfPulses("130");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT13_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus13.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus13.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT13_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT13();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT13_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
	

	
	 
		
		
	public void lscsDUT14_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus14.clear();
    	try{
	    	dutSerialDM_Obj.commDUT14.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID14();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate14();
	    	boolean status = dutSerialDM_Obj.DUT14_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus14.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-14.00");
				DisplayDataObj.set_Error_max("+14.00");
				DisplayDataObj.setNoOfPulses("140");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT14_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus14.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus14.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT14_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT14();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT14_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
	


		
		
	public void lscsDUT15_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus15.clear();
    	try{
	    	dutSerialDM_Obj.commDUT15.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID15();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate15();
	    	boolean status = dutSerialDM_Obj.DUT15_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus15.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-15.00");
				DisplayDataObj.set_Error_max("+15.00");
				DisplayDataObj.setNoOfPulses("150");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT15_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus15.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus15.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT15_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT15();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT15_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
	

	

	


		
		
	public void lscsDUT16_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus16.clear();
    	try{
	    	dutSerialDM_Obj.commDUT16.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID16();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate16();
	    	boolean status = dutSerialDM_Obj.DUT16_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus16.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-16.00");
				DisplayDataObj.set_Error_max("+16.00");
				DisplayDataObj.setNoOfPulses("160");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT16_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus16.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus16.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT16_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT16();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT16_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
	



		
		
	public void lscsDUT17_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus17.clear();
    	try{
	    	dutSerialDM_Obj.commDUT17.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID17();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate17();
	    	boolean status = dutSerialDM_Obj.DUT17_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus17.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-17.00");
				DisplayDataObj.set_Error_max("+17.00");
				DisplayDataObj.setNoOfPulses("170");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT17_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus17.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus17.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT17_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT17();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT17_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
	


		
		
	public void lscsDUT18_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus18.clear();
    	try{
	    	dutSerialDM_Obj.commDUT18.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID18();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate18();
	    	boolean status = dutSerialDM_Obj.DUT18_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus18.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-18.00");
				DisplayDataObj.set_Error_max("+18.00");
				DisplayDataObj.setNoOfPulses("180");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT18_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus18.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus18.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT18_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT18();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT18_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
	



		
		
	public void lscsDUT19_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus19.clear();
    	try{
	    	dutSerialDM_Obj.commDUT19.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID19();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate19();
	    	boolean status = dutSerialDM_Obj.DUT19_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus19.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-19.00");
				DisplayDataObj.set_Error_max("+19.00");
				DisplayDataObj.setNoOfPulses("190");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT19_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus19.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus19.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT19_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT19();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT19_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
	

	

						
		
		
	public void lscsDUT20_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus20.clear();
    	try{
	    	dutSerialDM_Obj.commDUT20.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID20();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate20();
	    	boolean status = dutSerialDM_Obj.DUT20_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus20.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-20.00");
				DisplayDataObj.set_Error_max("+20.00");
				DisplayDataObj.setNoOfPulses("200");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT20_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus20.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus20.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT20_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT20();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT20_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
	

	

		
		
	public void lscsDUT21_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus21.clear();
    	try{
	    	dutSerialDM_Obj.commDUT21.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID21();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate21();
	    	boolean status = dutSerialDM_Obj.DUT21_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus21.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-21.00");
				DisplayDataObj.set_Error_max("+21.00");
				DisplayDataObj.setNoOfPulses("210");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT21_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus21.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus21.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT21_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT21();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT21_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }


		
		
	public void lscsDUT22_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus22.clear();
    	try{
	    	dutSerialDM_Obj.commDUT22.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID22();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate22();
	    	boolean status = dutSerialDM_Obj.DUT22_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus22.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-22.00");
				DisplayDataObj.set_Error_max("+22.00");
				DisplayDataObj.setNoOfPulses("220");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT22_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus22.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus22.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT22_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT22();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT22_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
	

	

	


		
		
	public void lscsDUT23_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus23.clear();
    	try{
	    	dutSerialDM_Obj.commDUT23.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID23();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate23();
	    	boolean status = dutSerialDM_Obj.DUT23_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus23.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-23.00");
				DisplayDataObj.set_Error_max("+23.00");
				DisplayDataObj.setNoOfPulses("230");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT23_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus23.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus23.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT23_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT23();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT23_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
	


		
		
	public void lscsDUT24_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus24.clear();
    	try{
	    	dutSerialDM_Obj.commDUT24.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID24();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate24();
	    	boolean status = dutSerialDM_Obj.DUT24_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus24.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-24.00");
				DisplayDataObj.set_Error_max("+24.00");
				DisplayDataObj.setNoOfPulses("240");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT24_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus24.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus24.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT24_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT24();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT24_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
	


		
		
	public void lscsDUT25_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus25.clear();
    	try{
	    	dutSerialDM_Obj.commDUT25.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID25();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate25();
	    	boolean status = dutSerialDM_Obj.DUT25_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus25.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-25.00");
				DisplayDataObj.set_Error_max("+25.00");
				DisplayDataObj.setNoOfPulses("250");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT25_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus25.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus25.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT25_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT25();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT25_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
	

	


		
		
	public void lscsDUT26_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus26.clear();
    	try{
	    	dutSerialDM_Obj.commDUT26.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID26();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate26();
	    	boolean status = dutSerialDM_Obj.DUT26_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus26.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-26.00");
				DisplayDataObj.set_Error_max("+26.00");
				DisplayDataObj.setNoOfPulses("260");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT26_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus26.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus26.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT26_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT26();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT26_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
	


		
		
	public void lscsDUT27_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus27.clear();
    	try{
	    	dutSerialDM_Obj.commDUT27.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID27();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate27();
	    	boolean status = dutSerialDM_Obj.DUT27_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus27.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-27.00");
				DisplayDataObj.set_Error_max("+27.00");
				DisplayDataObj.setNoOfPulses("270");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT27_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus27.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus27.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT27_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT27();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT27_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
	



		
		
	public void lscsDUT28_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus28.clear();
    	try{
	    	dutSerialDM_Obj.commDUT28.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID28();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate28();
	    	boolean status = dutSerialDM_Obj.DUT28_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus28.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-28.00");
				DisplayDataObj.set_Error_max("+28.00");
				DisplayDataObj.setNoOfPulses("280");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT28_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus28.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus28.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT28_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT28();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT28_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
	

	

	

		
		
	public void lscsDUT29_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus29.clear();
    	try{
	    	dutSerialDM_Obj.commDUT29.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID29();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate29();
	    	boolean status = dutSerialDM_Obj.DUT29_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus29.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-29.00");
				DisplayDataObj.set_Error_max("+29.00");
				DisplayDataObj.setNoOfPulses("290");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT29_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus29.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus29.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT29_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT29();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT29_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }

		
		
	public void lscsDUT30_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus30.clear();
    	try{
	    	dutSerialDM_Obj.commDUT30.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID30();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate30();
	    	boolean status = dutSerialDM_Obj.DUT30_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus30.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-30.00");
				DisplayDataObj.set_Error_max("+30.00");
				DisplayDataObj.setNoOfPulses("300");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT30_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus30.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus30.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT30_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT30();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT30_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
	

	
	

			 
		
		
	public void lscsDUT31_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus31.clear();
    	try{
	    	dutSerialDM_Obj.commDUT31.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID31();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate31();
	    	boolean status = dutSerialDM_Obj.DUT31_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus31.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-31.00");
				DisplayDataObj.set_Error_max("+31.00");
				DisplayDataObj.setNoOfPulses("310");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT31_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus31.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus31.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT31_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT31();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT31_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
	

	


		
	public void lscsDUT32_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus32.clear();
    	try{
	    	dutSerialDM_Obj.commDUT32.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID32();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate32();
	    	boolean status = dutSerialDM_Obj.DUT32_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus32.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-32.00");
				DisplayDataObj.set_Error_max("+32.00");
				DisplayDataObj.setNoOfPulses("320");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT32_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus32.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus32.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT32_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT32();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT32_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
	


		
		
		
	public void lscsDUT33_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus33.clear();
    	try{
	    	dutSerialDM_Obj.commDUT33.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID33();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate33();
	    	boolean status = dutSerialDM_Obj.DUT33_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus33.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-33.00");
				DisplayDataObj.set_Error_max("+33.00");
				DisplayDataObj.setNoOfPulses("330");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT33_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus33.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus33.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT33_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT33();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT33_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
	

	
		
		
	public void lscsDUT34_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus34.clear();
    	try{
	    	dutSerialDM_Obj.commDUT34.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID34();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate34();
	    	boolean status = dutSerialDM_Obj.DUT34_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus34.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-34.00");
				DisplayDataObj.set_Error_max("+34.00");
				DisplayDataObj.setNoOfPulses("340");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT34_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus34.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus34.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT34_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT34();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT34_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
	


		
		
	public void lscsDUT35_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus35.clear();
    	try{
	    	dutSerialDM_Obj.commDUT35.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID35();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate35();
	    	boolean status = dutSerialDM_Obj.DUT35_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus35.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-35.00");
				DisplayDataObj.set_Error_max("+35.00");
				DisplayDataObj.setNoOfPulses("350");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT35_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus35.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus35.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT35_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT35();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT35_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
	

	

	

	
	 
		
		
	public void lscsDUT36_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus36.clear();
    	try{
	    	dutSerialDM_Obj.commDUT36.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID36();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate36();
	    	boolean status = dutSerialDM_Obj.DUT36_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus36.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-36.00");
				DisplayDataObj.set_Error_max("+36.00");
				DisplayDataObj.setNoOfPulses("360");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT36_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus36.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus36.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT36_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT36();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT36_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
	



		
		
	public void lscsDUT37_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus37.clear();
    	try{
	    	dutSerialDM_Obj.commDUT37.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID37();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate37();
	    	boolean status = dutSerialDM_Obj.DUT37_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus37.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-37.00");
				DisplayDataObj.set_Error_max("+37.00");
				DisplayDataObj.setNoOfPulses("370");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT37_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus37.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus37.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT37_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT37();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT37_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
	

		
		
		
	public void lscsDUT38_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus38.clear();
    	try{
	    	dutSerialDM_Obj.commDUT38.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID38();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate38();
	    	boolean status = dutSerialDM_Obj.DUT38_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus38.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-38.00");
				DisplayDataObj.set_Error_max("+38.00");
				DisplayDataObj.setNoOfPulses("380");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT38_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus38.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus38.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT38_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT38();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT38_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
	

	
		
		
	public void lscsDUT39_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus39.clear();
    	try{
	    	dutSerialDM_Obj.commDUT39.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID39();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate39();
	    	boolean status = dutSerialDM_Obj.DUT39_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus39.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-39.00");
				DisplayDataObj.set_Error_max("+39.00");
				DisplayDataObj.setNoOfPulses("390");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT39_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus39.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus39.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT39_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT39();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT39_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
	

	

		
		
	public void lscsDUT40_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus40.clear();
    	try{
	    	dutSerialDM_Obj.commDUT40.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID40();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate40();
	    	boolean status = dutSerialDM_Obj.DUT40_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus40.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-40.00");
				DisplayDataObj.set_Error_max("+40.00");
				DisplayDataObj.setNoOfPulses("400");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT40_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus40.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus40.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT40_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT40();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT40_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
	

	
	

		
		
		
	public void lscsDUT41_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus41.clear();
    	try{
	    	dutSerialDM_Obj.commDUT41.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID41();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate41();
	    	boolean status = dutSerialDM_Obj.DUT41_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus41.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-41.00");
				DisplayDataObj.set_Error_max("+41.00");
				DisplayDataObj.setNoOfPulses("410");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT41_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus41.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus41.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT41_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT41();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT41_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
	

	


		
		
	public void lscsDUT42_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus42.clear();
    	try{
	    	dutSerialDM_Obj.commDUT42.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID42();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate42();
	    	boolean status = dutSerialDM_Obj.DUT42_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus42.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-42.00");
				DisplayDataObj.set_Error_max("+42.00");
				DisplayDataObj.setNoOfPulses("420");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT42_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus42.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus42.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT42_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT42();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT42_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
	

	


	 
		
		
	public void lscsDUT43_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus43.clear();
    	try{
	    	dutSerialDM_Obj.commDUT43.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID43();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate43();
	    	boolean status = dutSerialDM_Obj.DUT43_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus43.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-43.00");
				DisplayDataObj.set_Error_max("+43.00");
				DisplayDataObj.setNoOfPulses("430");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT43_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus43.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus43.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT43_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT43();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT43_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
	

	

	
		
		
	public void lscsDUT44_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus44.clear();
    	try{
	    	dutSerialDM_Obj.commDUT44.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID44();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate44();
	    	boolean status = dutSerialDM_Obj.DUT44_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus44.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-44.00");
				DisplayDataObj.set_Error_max("+44.00");
				DisplayDataObj.setNoOfPulses("440");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT44_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus44.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus44.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT44_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT44();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT44_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
	

		
		
	public void lscsDUT45_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus45.clear();
    	try{
	    	dutSerialDM_Obj.commDUT45.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID45();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate45();
	    	boolean status = dutSerialDM_Obj.DUT45_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus45.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-45.00");
				DisplayDataObj.set_Error_max("+45.00");
				DisplayDataObj.setNoOfPulses("450");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT45_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus45.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus45.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT45_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT45();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT45_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
	

	


	
		
		
	public void lscsDUT46_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus46.clear();
    	try{
	    	dutSerialDM_Obj.commDUT46.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID46();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate46();
	    	boolean status = dutSerialDM_Obj.DUT46_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus46.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-46.00");
				DisplayDataObj.set_Error_max("+46.00");
				DisplayDataObj.setNoOfPulses("460");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT46_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus46.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus46.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT46_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT46();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT46_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
	

	


		
		
	public void lscsDUT47_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus47.clear();
    	try{
	    	dutSerialDM_Obj.commDUT47.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID47();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate47();
	    	boolean status = dutSerialDM_Obj.DUT47_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus47.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-47.00");
				DisplayDataObj.set_Error_max("+47.00");
				DisplayDataObj.setNoOfPulses("470");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT47_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus47.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus47.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT47_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT47();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT47_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }
	


		
		
		
	public void lscsDUT48_ValidateSerialCmd(){
    	String DUT_CommPortID= null;
    	String DUTCommBaudRate = null;
    	txtValidateDUT_CmdStatus48.clear();
    	try{
	    	dutSerialDM_Obj.commDUT48.searchForPorts(); 
	    	DUT_CommPortID = getCurrentDUT_ComPortID48();
	    	DUTCommBaudRate = getCurrentDUT_ComBaudRate48();
	    	boolean status = dutSerialDM_Obj.DUT48_Init(DUT_CommPortID,DUTCommBaudRate);
	    	if (!status){
	    		
	    		txtValidateDUT_CmdStatus48.setText(ConstantApp.SERIAL_PORT_ACCESS_FAILED);
	    		
	    	} else {
	    		setPortValidationTurnedON(true);
/*				DisplayDataObj.set_Error_min("-48.00");
				DisplayDataObj.set_Error_max("+48.00");
				DisplayDataObj.setNoOfPulses("480");
				
				DisplayDataObj.setDUT_ReadDataFlag(true);*/
	    		//status = dutSerialDM_Obj.DUT_ResetSetting();
	    		status = dutSerialDM_Obj.lscsDUT48_CheckCom();
	    		
	    		if (!status){
	    			txtValidateDUT_CmdStatus48.setText(ConstantApp.SERIAL_PORT_COMMAND_FAILED);
	    		}else{
	    			txtValidateDUT_CmdStatus48.setText(ConstantApp.SERIAL_PORT_COMMAND_Success);
	    		}
	    		setPortValidationTurnedON(false);
	    		DisplayDataObj.setDUT48_ReadDataFlag(false);
	    	}
	    	dutSerialDM_Obj.DisconnectDUT48();
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DUT48_ValidateSerialCmd: Exception"+e.getMessage());
    	}
    	
    	
    }

/*	private String getCurrentPwrSrcComBaudRate() {
		// TODO Auto-generated method stub
		return cmbBxPowerSrcBaudRate.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentPwrSrcComPortID() {
		// TODO Auto-generated method stub
		
		return cmbBxPowerSrcPortSelection.getSelectionModel().getSelectedItem();
	}
	
	private String getCurrentRefStdComBaudRate() {
		// TODO Auto-generated method stub
		return cmbBxRefStdBaudRate.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentRefStdComPortID() {
		// TODO Auto-generated method stub
		
		return cmbBxRefStdPortSelection.getSelectionModel().getSelectedItem();
	}*/
	
	private String getCurrentDUT_ComBaudRate1() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate1.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID1() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection1.getSelectionModel().getSelectedItem();
	}
	
	
	
	private String getCurrentDUT_ComBaudRate2() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate2.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID2() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection2.getSelectionModel().getSelectedItem();
	}
	
	private String getCurrentDUT_ComBaudRate3() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate3.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID3() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection3.getSelectionModel().getSelectedItem();
	}
	
	private String getCurrentDUT_ComBaudRate4() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate4.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID4() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection4.getSelectionModel().getSelectedItem();
	}
	
	private String getCurrentDUT_ComBaudRate5() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate5.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID5() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection5.getSelectionModel().getSelectedItem();
	}
	
	private String getCurrentDUT_ComBaudRate6() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate6.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID6() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection6.getSelectionModel().getSelectedItem();
	}
	
	private String getCurrentDUT_ComBaudRate7() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate7.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID7() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection7.getSelectionModel().getSelectedItem();
	}
	
	
	private String getCurrentDUT_ComBaudRate8() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate8.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID8() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection8.getSelectionModel().getSelectedItem();
	}
	
	private String getCurrentDUT_ComBaudRate9() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate9.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID9() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection9.getSelectionModel().getSelectedItem();
	}
	
	private String getCurrentDUT_ComBaudRate10() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate10.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID10() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection10.getSelectionModel().getSelectedItem();
	}
	
	private String getCurrentDUT_ComBaudRate11() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate11.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID11() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection11.getSelectionModel().getSelectedItem();
	}
	
	private String getCurrentDUT_ComBaudRate12() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate12.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID12() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection12.getSelectionModel().getSelectedItem();
	}
	
	private String getCurrentDUT_ComBaudRate13() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate13.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID13() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection13.getSelectionModel().getSelectedItem();
	}
	
	private String getCurrentDUT_ComBaudRate14() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate14.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID14() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection14.getSelectionModel().getSelectedItem();
	}
	private String getCurrentDUT_ComBaudRate15() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate15.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID15() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection15.getSelectionModel().getSelectedItem();
	}

	
	private String getCurrentDUT_ComBaudRate16() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate16.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID16() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection16.getSelectionModel().getSelectedItem();
	}
	private String getCurrentDUT_ComBaudRate17() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate17.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID17() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection17.getSelectionModel().getSelectedItem();
	}
	
	private String getCurrentDUT_ComBaudRate18() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate18.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID18() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection18.getSelectionModel().getSelectedItem();
	}
	
	private String getCurrentDUT_ComBaudRate19() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate19.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID19() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection19.getSelectionModel().getSelectedItem();
	}
	
	
	private String getCurrentDUT_ComBaudRate20() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate20.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID20() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection20.getSelectionModel().getSelectedItem();
	}
	
	private String getCurrentDUT_ComBaudRate21() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate21.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID21() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection21.getSelectionModel().getSelectedItem();
	}
	
	
	private String getCurrentDUT_ComBaudRate22() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate22.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID22() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection22.getSelectionModel().getSelectedItem();
	}
	
	private String getCurrentDUT_ComBaudRate23() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate23.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID23() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection23.getSelectionModel().getSelectedItem();
	}

	
	private String getCurrentDUT_ComBaudRate24() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate24.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID24() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection24.getSelectionModel().getSelectedItem();
	}
	
	private String getCurrentDUT_ComBaudRate25() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate25.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID25() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection25.getSelectionModel().getSelectedItem();
	}
	
	private String getCurrentDUT_ComBaudRate26() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate26.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID26() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection26.getSelectionModel().getSelectedItem();
	}
	
	private String getCurrentDUT_ComBaudRate27() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate27.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID27() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection27.getSelectionModel().getSelectedItem();
	}
	
	private String getCurrentDUT_ComBaudRate28() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate28.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID28() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection28.getSelectionModel().getSelectedItem();
	}
	
	private String getCurrentDUT_ComBaudRate29() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate29.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID29() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection29.getSelectionModel().getSelectedItem();
	}
	
	private String getCurrentDUT_ComBaudRate30() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate30.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID30() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection30.getSelectionModel().getSelectedItem();
	}

	
	private String getCurrentDUT_ComBaudRate31() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate31.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID31() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection31.getSelectionModel().getSelectedItem();
	}
	
	
	private String getCurrentDUT_ComBaudRate32() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate32.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID32() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection32.getSelectionModel().getSelectedItem();
	}
	
	private String getCurrentDUT_ComBaudRate33() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate33.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID33() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection33.getSelectionModel().getSelectedItem();
	}

	
	private String getCurrentDUT_ComBaudRate34() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate34.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID34() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection34.getSelectionModel().getSelectedItem();
	}
	
	private String getCurrentDUT_ComBaudRate35() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate35.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID35() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection35.getSelectionModel().getSelectedItem();
	}
	
	private String getCurrentDUT_ComBaudRate36() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate36.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID36() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection36.getSelectionModel().getSelectedItem();
	}
	
	private String getCurrentDUT_ComBaudRate37() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate37.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID37() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection37.getSelectionModel().getSelectedItem();
	}
	
	private String getCurrentDUT_ComBaudRate38() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate38.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID38() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection38.getSelectionModel().getSelectedItem();
	}
	
	private String getCurrentDUT_ComBaudRate39() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate39.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID39() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection39.getSelectionModel().getSelectedItem();
	}
	
	private String getCurrentDUT_ComBaudRate40() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate40.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID40() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection40.getSelectionModel().getSelectedItem();
	}
	private String getCurrentDUT_ComBaudRate41() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate41.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID41() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection41.getSelectionModel().getSelectedItem();
	}
	
	private String getCurrentDUT_ComBaudRate42() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate42.getSelectionModel().getSelectedItem().toString();
	}
	
	private String getCurrentDUT_ComPortID42() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection42.getSelectionModel().getSelectedItem();
	}
	
	private String getCurrentDUT_ComBaudRate43() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate43.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID43() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection43.getSelectionModel().getSelectedItem();
	}
		
	private String getCurrentDUT_ComBaudRate44() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate44.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID44() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection44.getSelectionModel().getSelectedItem();
	}
	

	private String getCurrentDUT_ComBaudRate45() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate45.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID45() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection45.getSelectionModel().getSelectedItem();
	}
	
	private String getCurrentDUT_ComBaudRate46() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate46.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID46() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection46.getSelectionModel().getSelectedItem();
	}
	
	private String getCurrentDUT_ComBaudRate47() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate47.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID47() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection47.getSelectionModel().getSelectedItem();
	}	

	
	private String getCurrentDUT_ComBaudRate48() {
		// TODO Auto-generated method stub
		return cmbBxDUT_BaudRate48.getSelectionModel().getSelectedItem().toString();
	}

	private String getCurrentDUT_ComPortID48() {
		// TODO Auto-generated method stub
		
		return cmbBxDUT_PortSelection48.getSelectionModel().getSelectedItem();
	}
	
/*    public void  PwrSrcValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("PwrSrcValidateSerialCmdTrigger: Invoked:");
		PwrSrcValidateTimer = new Timer();
		PwrSrcValidateTimer.schedule(new PwrSrcValidateTimerTask(),100);// 1000);
		
    }
    
    public void  RefStdValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("RefStdValidateSerialCmdTrigger: Invoked:");
    	RefStdValidateTimer = new Timer();
    	RefStdValidateTimer.schedule(new RefStdValidateTimerTask(),100);// 1000);
		
    }*/
    
    @FXML
    public void  DUT1_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT1_ValidateSerialCmdTrigger: Invoked:");
    	DUT1_ValidateTimer = new Timer();
    	DUT1_ValidateTimer.schedule(new DUT1_ValidateTimerTask(),100);// 1000);
		
    }
    
    @FXML
    public void  DUT2_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT2_ValidateSerialCmdTrigger: Invoked:");
    	DUT2_ValidateTimer = new Timer();
    	DUT2_ValidateTimer.schedule(new DUT2_ValidateTimerTask(),200);// 2000);
    }
    
    @FXML
    public void  DUT3_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT3_ValidateSerialCmdTrigger: Invoked:");
    	DUT3_ValidateTimer = new Timer();
    	DUT3_ValidateTimer.schedule(new DUT3_ValidateTimerTask(),100);// 1000);
    }
    
    @FXML
    public void  DUT4_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT4_ValidateSerialCmdTrigger: Invoked:");
    	DUT4_ValidateTimer = new Timer();
    	DUT4_ValidateTimer.schedule(new DUT4_ValidateTimerTask(),100);// 1000);
    }
    
    @FXML
    public void  DUT5_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT5_ValidateSerialCmdTrigger: Invoked:");
    	DUT5_ValidateTimer = new Timer();
    	DUT5_ValidateTimer.schedule(new DUT5_ValidateTimerTask(),100);// 1000);
    }
    
    @FXML
    public void  DUT6_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT6_ValidateSerialCmdTrigger: Invoked:");
    	DUT6_ValidateTimer = new Timer();
    	DUT6_ValidateTimer.schedule(new DUT6_ValidateTimerTask(),100);// 1000);
    }
    
    @FXML
    public void  DUT7_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT7_ValidateSerialCmdTrigger: Invoked:");
    	DUT7_ValidateTimer = new Timer();
    	DUT7_ValidateTimer.schedule(new DUT7_ValidateTimerTask(),100);// 1000);
    }
    
    @FXML
    public void  DUT8_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT8_ValidateSerialCmdTrigger: Invoked:");
    	DUT8_ValidateTimer = new Timer();
    	DUT8_ValidateTimer.schedule(new DUT8_ValidateTimerTask(),100);// 1000);
    }
    
	@FXML
    public void  DUT9_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT9_ValidateSerialCmdTrigger: Invoked:");
    	DUT9_ValidateTimer = new Timer();
    	DUT9_ValidateTimer.schedule(new DUT9_ValidateTimerTask(),100);// 1000);
    }
	
	@FXML
    public void  DUT10_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT10_ValidateSerialCmdTrigger: Invoked:");
    	DUT10_ValidateTimer = new Timer();
    	DUT10_ValidateTimer.schedule(new DUT10_ValidateTimerTask(),100);// 1000);
    }
	
	@FXML
    public void  DUT11_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT11_ValidateSerialCmdTrigger: Invoked:");
    	DUT11_ValidateTimer = new Timer();
    	DUT11_ValidateTimer.schedule(new DUT11_ValidateTimerTask(),100);// 1000);
    }
	
	@FXML
    public void  DUT12_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT12_ValidateSerialCmdTrigger: Invoked:");
    	DUT12_ValidateTimer = new Timer();
    	DUT12_ValidateTimer.schedule(new DUT12_ValidateTimerTask(),100);// 1000);
    }
	
	@FXML
    public void  DUT13_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT13_ValidateSerialCmdTrigger: Invoked:");
    	DUT13_ValidateTimer = new Timer();
    	DUT13_ValidateTimer.schedule(new DUT13_ValidateTimerTask(),100);// 1000);
    }
	
	@FXML
    public void  DUT14_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT14_ValidateSerialCmdTrigger: Invoked:");
    	DUT14_ValidateTimer = new Timer();
    	DUT14_ValidateTimer.schedule(new DUT14_ValidateTimerTask(),100);// 1000);
    }
	
	@FXML
    public void  DUT15_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT15_ValidateSerialCmdTrigger: Invoked:");
    	DUT15_ValidateTimer = new Timer();
    	DUT15_ValidateTimer.schedule(new DUT15_ValidateTimerTask(),100);// 1000);
    }
	
	@FXML
    public void  DUT16_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT16_ValidateSerialCmdTrigger: Invoked:");
    	DUT16_ValidateTimer = new Timer();
    	DUT16_ValidateTimer.schedule(new DUT16_ValidateTimerTask(),100);// 1000);
    }
	@FXML
    public void  DUT17_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT17_ValidateSerialCmdTrigger: Invoked:");
    	DUT17_ValidateTimer = new Timer();
    	DUT17_ValidateTimer.schedule(new DUT17_ValidateTimerTask(),100);// 1000);
    }
	
	@FXML
    public void  DUT18_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT18_ValidateSerialCmdTrigger: Invoked:");
    	DUT18_ValidateTimer = new Timer();
    	DUT18_ValidateTimer.schedule(new DUT18_ValidateTimerTask(),100);// 1000);
    }
	
	@FXML
    public void  DUT19_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT19_ValidateSerialCmdTrigger: Invoked:");
    	DUT19_ValidateTimer = new Timer();
    	DUT19_ValidateTimer.schedule(new DUT19_ValidateTimerTask(),100);// 1000);
    }
	
	@FXML
    public void  DUT20_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT20_ValidateSerialCmdTrigger: Invoked:");
    	DUT20_ValidateTimer = new Timer();
    	DUT20_ValidateTimer.schedule(new DUT20_ValidateTimerTask(),100);// 1000);
    }	@FXML
    public void  DUT21_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT21_ValidateSerialCmdTrigger: Invoked:");
    	DUT21_ValidateTimer = new Timer();
    	DUT21_ValidateTimer.schedule(new DUT21_ValidateTimerTask(),100);// 1000);
    }
	
	@FXML
    public void  DUT22_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT22_ValidateSerialCmdTrigger: Invoked:");
    	DUT22_ValidateTimer = new Timer();
    	DUT22_ValidateTimer.schedule(new DUT22_ValidateTimerTask(),100);// 1000);
    }
	
	
	@FXML
    public void  DUT23_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT23_ValidateSerialCmdTrigger: Invoked:");
    	DUT23_ValidateTimer = new Timer();
    	DUT23_ValidateTimer.schedule(new DUT23_ValidateTimerTask(),100);// 1000);
    }
	
	@FXML
    public void  DUT24_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT24_ValidateSerialCmdTrigger: Invoked:");
    	DUT24_ValidateTimer = new Timer();
    	DUT24_ValidateTimer.schedule(new DUT24_ValidateTimerTask(),100);// 1000);
    }
	
	@FXML
    public void  DUT25_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT25_ValidateSerialCmdTrigger: Invoked:");
    	DUT25_ValidateTimer = new Timer();
    	DUT25_ValidateTimer.schedule(new DUT25_ValidateTimerTask(),100);// 1000);
    }
	
	@FXML
    public void  DUT26_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT26_ValidateSerialCmdTrigger: Invoked:");
    	DUT26_ValidateTimer = new Timer();
    	DUT26_ValidateTimer.schedule(new DUT26_ValidateTimerTask(),100);// 1000);
    }
	
	@FXML
    public void  DUT27_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT27_ValidateSerialCmdTrigger: Invoked:");
    	DUT27_ValidateTimer = new Timer();
    	DUT27_ValidateTimer.schedule(new DUT27_ValidateTimerTask(),100);// 1000);
    }
	@FXML
    public void  DUT28_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT28_ValidateSerialCmdTrigger: Invoked:");
    	DUT28_ValidateTimer = new Timer();
    	DUT28_ValidateTimer.schedule(new DUT28_ValidateTimerTask(),100);// 1000);
    }
	
	@FXML
    public void  DUT29_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT29_ValidateSerialCmdTrigger: Invoked:");
    	DUT29_ValidateTimer = new Timer();
    	DUT29_ValidateTimer.schedule(new DUT29_ValidateTimerTask(),100);// 1000);
    }
	
	@FXML
    public void  DUT30_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT30_ValidateSerialCmdTrigger: Invoked:");
    	DUT30_ValidateTimer = new Timer();
    	DUT30_ValidateTimer.schedule(new DUT30_ValidateTimerTask(),100);// 1000);
    }
	
	@FXML
    public void  DUT31_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT31_ValidateSerialCmdTrigger: Invoked:");
    	DUT31_ValidateTimer = new Timer();
    	DUT31_ValidateTimer.schedule(new DUT31_ValidateTimerTask(),100);// 1000);
    }	@FXML
    public void  DUT32_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT32_ValidateSerialCmdTrigger: Invoked:");
    	DUT32_ValidateTimer = new Timer();
    	DUT32_ValidateTimer.schedule(new DUT32_ValidateTimerTask(),100);// 1000);
    }
	
	@FXML
    public void  DUT33_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT33_ValidateSerialCmdTrigger: Invoked:");
    	DUT33_ValidateTimer = new Timer();
    	DUT33_ValidateTimer.schedule(new DUT33_ValidateTimerTask(),100);// 1000);
    }
	
	@FXML
    public void  DUT34_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT34_ValidateSerialCmdTrigger: Invoked:");
    	DUT34_ValidateTimer = new Timer();
    	DUT34_ValidateTimer.schedule(new DUT34_ValidateTimerTask(),100);// 1000);
    }
	
	@FXML
    public void  DUT35_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT35_ValidateSerialCmdTrigger: Invoked:");
    	DUT35_ValidateTimer = new Timer();
    	DUT35_ValidateTimer.schedule(new DUT35_ValidateTimerTask(),100);// 1000);
    }
	
	@FXML
    public void  DUT36_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT36_ValidateSerialCmdTrigger: Invoked:");
    	DUT36_ValidateTimer = new Timer();
    	DUT36_ValidateTimer.schedule(new DUT36_ValidateTimerTask(),100);// 1000);
    }
	
	@FXML
    public void  DUT37_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT37_ValidateSerialCmdTrigger: Invoked:");
    	DUT37_ValidateTimer = new Timer();
    	DUT37_ValidateTimer.schedule(new DUT37_ValidateTimerTask(),100);// 1000);
    }
	
	@FXML
    public void  DUT38_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT38_ValidateSerialCmdTrigger: Invoked:");
    	DUT38_ValidateTimer = new Timer();
    	DUT38_ValidateTimer.schedule(new DUT38_ValidateTimerTask(),100);// 1000);
    }
	
	@FXML
    public void  DUT39_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT39_ValidateSerialCmdTrigger: Invoked:");
    	DUT39_ValidateTimer = new Timer();
    	DUT39_ValidateTimer.schedule(new DUT39_ValidateTimerTask(),100);// 1000);
    }
	@FXML
    public void  DUT40_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT40_ValidateSerialCmdTrigger: Invoked:");
    	DUT40_ValidateTimer = new Timer();
    	DUT40_ValidateTimer.schedule(new DUT40_ValidateTimerTask(),100);// 1000);
    }
	
	@FXML
    public void  DUT41_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT41_ValidateSerialCmdTrigger: Invoked:");
    	DUT41_ValidateTimer = new Timer();
    	DUT41_ValidateTimer.schedule(new DUT41_ValidateTimerTask(),100);// 1000);
    }
	
	@FXML
    public void  DUT42_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT42_ValidateSerialCmdTrigger: Invoked:");
    	DUT42_ValidateTimer = new Timer();
    	DUT42_ValidateTimer.schedule(new DUT42_ValidateTimerTask(),100);// 1000);
    }
	
	@FXML
    public void  DUT43_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT43_ValidateSerialCmdTrigger: Invoked:");
    	DUT43_ValidateTimer = new Timer();
    	DUT43_ValidateTimer.schedule(new DUT43_ValidateTimerTask(),100);// 1000);
    }	@FXML
    public void  DUT44_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT44_ValidateSerialCmdTrigger: Invoked:");
    	DUT44_ValidateTimer = new Timer();
    	DUT44_ValidateTimer.schedule(new DUT44_ValidateTimerTask(),100);// 1000);
    }
	
	@FXML
    public void  DUT45_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT45_ValidateSerialCmdTrigger: Invoked:");
    	DUT45_ValidateTimer = new Timer();
    	DUT45_ValidateTimer.schedule(new DUT45_ValidateTimerTask(),100);// 1000);
    }
	
	@FXML
    public void  DUT46_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT46_ValidateSerialCmdTrigger: Invoked:");
    	DUT46_ValidateTimer = new Timer();
    	DUT46_ValidateTimer.schedule(new DUT46_ValidateTimerTask(),100);// 1000);
    }
	
	@FXML
    public void  DUT47_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT47_ValidateSerialCmdTrigger: Invoked:");
    	DUT47_ValidateTimer = new Timer();
    	DUT47_ValidateTimer.schedule(new DUT47_ValidateTimerTask(),100);// 1000);
    }
	
	@FXML
    public void  DUT48_ValidateSerialCmdTrigger(){
    	ApplicationLauncher.logger.info("DUT48_ValidateSerialCmdTrigger: Invoked:");
    	DUT48_ValidateTimer = new Timer();
    	DUT48_ValidateTimer.schedule(new DUT48_ValidateTimerTask(),100);// 1000);
    }
	
	

/*	
	class PwrSrcValidateTimerTask extends TimerTask {
		public void run() {
			btnValidatePwrSrcCmd.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("PwrSrcValidateTimerTask: WAIT");
			try {
				PwrSrcValidateSerialCmd();
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("PwrSrcValidateTimerTask: Exception:"+e.getMessage());
			}
			PwrSrcValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("PwrSrcValidateTimerTask: DEFAULT");
			btnValidatePwrSrcCmd.setDisable(false);
		}
	}
	
	class RefStdValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateRefStdCmd.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("RefStdValidateTimerTask: WAIT");
			try {
				
				RefStdValidateSerialCmd();
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("RefStdValidateTimerTask: Exception:"+e.getMessage());
			}
			RefStdValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("RefStdValidateTimerTask: DEFAULT");
			btnValidateRefStdCmd.setDisable(false);
		}
	}*/
	
	class DUT1_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd1.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT_ValidateTimerTask: WAIT");
			try {
				/*if(ProcalFeatureEnable.CCUBE_DUT_CONNECTED){
					DUT_ValidateSerialCmd();
				} else if (ProcalFeatureEnable.LSCS_DUT_CONNECTED){
					lscsDUT1_ValidateSerialCmd();
				}*/
				lscsDUT1_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT1_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd1.setDisable(false);
		}
	}


	class DUT2_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd2.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT2_ValidateTimerTask: WAIT");
			try {
				lscsDUT2_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT2_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT2_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT2_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd2.setDisable(false);
		}
	}
	
	class DUT3_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd3.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT3_ValidateTimerTask: WAIT");
			try {
				lscsDUT3_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT3_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT3_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT3_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd3.setDisable(false);
		}
	}
	
	class DUT4_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd4.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT4_ValidateTimerTask: WAIT");
			try {
				lscsDUT4_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT4_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT4_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT4_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd4.setDisable(false);
		}
	}
	
	class DUT5_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd5.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT5_ValidateTimerTask: WAIT");
			try {
				lscsDUT5_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT5_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT5_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT5_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd5.setDisable(false);
		}
	}
	
	class DUT6_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd6.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT6_ValidateTimerTask: WAIT");
			try {
				lscsDUT6_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT6_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT6_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT6_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd6.setDisable(false);
		}
	}
	
	class DUT7_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd7.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT7_ValidateTimerTask: WAIT");
			try {
				lscsDUT7_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT7_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT7_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT7_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd7.setDisable(false);
		}
	}
	
	class DUT8_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd8.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT8_ValidateTimerTask: WAIT");
			try {
				lscsDUT8_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT8_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT8_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT8_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd8.setDisable(false);
		}
	}

	class DUT9_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd9.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT9_ValidateTimerTask: WAIT");
			try {
				lscsDUT9_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT9_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT9_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT9_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd9.setDisable(false);
		}
	}
	
	class DUT10_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd10.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT10_ValidateTimerTask: WAIT");
			try {
				lscsDUT10_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT10_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT10_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT10_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd10.setDisable(false);
		}
	}
	
	class DUT11_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd11.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT11_ValidateTimerTask: WAIT");
			try {
				lscsDUT11_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT11_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT11_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT11_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd11.setDisable(false);
		}
	}
	
	class DUT12_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd12.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT12_ValidateTimerTask: WAIT");
			try {
				lscsDUT12_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT12_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT12_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT12_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd12.setDisable(false);
		}
	}
	
	class DUT13_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd13.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT13_ValidateTimerTask: WAIT");
			try {
				lscsDUT13_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT13_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT13_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT13_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd13.setDisable(false);
		}
	}
	
	class DUT14_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd14.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT14_ValidateTimerTask: WAIT");
			try {
				lscsDUT14_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT14_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT14_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT14_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd14.setDisable(false);
		}
	}
	
	class DUT15_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd15.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT15_ValidateTimerTask: WAIT");
			try {
				lscsDUT15_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT15_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT15_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT15_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd15.setDisable(false);
		}
	}
	
	class DUT16_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd16.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT16_ValidateTimerTask: WAIT");
			try {
				lscsDUT16_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT16_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT16_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT16_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd16.setDisable(false);
		}
	}	
	
	class DUT17_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd17.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT17_ValidateTimerTask: WAIT");
			try {
				lscsDUT17_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT17_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT17_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT17_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd17.setDisable(false);
		}
	}
	
	class DUT18_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd18.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT18_ValidateTimerTask: WAIT");
			try {
				lscsDUT18_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT18_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT18_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT18_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd18.setDisable(false);
		}
	}
	
	class DUT19_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd19.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT19_ValidateTimerTask: WAIT");
			try {
				lscsDUT19_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT19_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT19_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT19_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd19.setDisable(false);
		}
	}
	
	class DUT20_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd20.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT20_ValidateTimerTask: WAIT");
			try {
				lscsDUT20_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT20_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT20_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT20_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd20.setDisable(false);
		}
	}	
	
	class DUT21_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd21.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT21_ValidateTimerTask: WAIT");
			try {
				lscsDUT21_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT21_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT21_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT21_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd21.setDisable(false);
		}
	}
	
	class DUT22_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd22.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT22_ValidateTimerTask: WAIT");
			try {
				lscsDUT22_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT22_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT22_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT22_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd22.setDisable(false);
		}
	}
	
	class DUT23_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd23.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT23_ValidateTimerTask: WAIT");
			try {
				lscsDUT23_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT23_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT23_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT23_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd23.setDisable(false);
		}
	}
	
	class DUT24_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd24.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT24_ValidateTimerTask: WAIT");
			try {
				lscsDUT24_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT24_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT24_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT24_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd24.setDisable(false);
		}
	}

	class DUT25_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd25.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT25_ValidateTimerTask: WAIT");
			try {
				lscsDUT25_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT25_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT25_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT25_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd25.setDisable(false);
		}
	}
	
	class DUT26_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd26.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT26_ValidateTimerTask: WAIT");
			try {
				lscsDUT26_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT26_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT26_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT26_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd26.setDisable(false);
		}
	}
	
	class DUT27_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd27.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT27_ValidateTimerTask: WAIT");
			try {
				lscsDUT27_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT27_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT27_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT27_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd27.setDisable(false);
		}
	}
	
	class DUT28_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd28.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT28_ValidateTimerTask: WAIT");
			try {
				lscsDUT28_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT28_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT28_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT28_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd28.setDisable(false);
		}
	}
	
	class DUT29_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd29.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT29_ValidateTimerTask: WAIT");
			try {
				lscsDUT29_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT29_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT29_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT29_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd29.setDisable(false);
		}
	}
	
	class DUT30_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd30.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT30_ValidateTimerTask: WAIT");
			try {
				lscsDUT30_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT30_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT30_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT30_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd30.setDisable(false);
		}
	}
	
	class DUT31_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd31.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT31_ValidateTimerTask: WAIT");
			try {
				lscsDUT31_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT31_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT31_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT31_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd31.setDisable(false);
		}
	}
	
	class DUT32_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd32.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT32_ValidateTimerTask: WAIT");
			try {
				lscsDUT32_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT32_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT32_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT32_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd32.setDisable(false);
		}
	}	
	
	class DUT33_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd33.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT33_ValidateTimerTask: WAIT");
			try {
				lscsDUT33_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT33_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT33_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT33_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd33.setDisable(false);
		}
	}
	
	class DUT34_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd34.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT34_ValidateTimerTask: WAIT");
			try {
				lscsDUT34_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT34_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT34_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT34_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd34.setDisable(false);
		}
	}
	
	class DUT35_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd35.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT35_ValidateTimerTask: WAIT");
			try {
				lscsDUT35_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT35_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT35_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT35_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd35.setDisable(false);
		}
	}
	
	class DUT36_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd36.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT36_ValidateTimerTask: WAIT");
			try {
				lscsDUT36_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT36_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT36_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT36_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd36.setDisable(false);
		}
	}	
	
	class DUT37_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd37.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT37_ValidateTimerTask: WAIT");
			try {
				lscsDUT37_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT37_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT37_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT37_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd37.setDisable(false);
		}
	}
	
	class DUT38_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd38.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT38_ValidateTimerTask: WAIT");
			try {
				lscsDUT38_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT38_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT38_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT38_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd38.setDisable(false);
		}
	}
	
	class DUT39_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd39.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT39_ValidateTimerTask: WAIT");
			try {
				lscsDUT39_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT39_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT39_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT39_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd39.setDisable(false);
		}
	}
	
	class DUT40_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd40.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT40_ValidateTimerTask: WAIT");
			try {
				lscsDUT40_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT40_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT40_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT40_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd40.setDisable(false);
		}
	}

	class DUT41_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd41.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT41_ValidateTimerTask: WAIT");
			try {
				lscsDUT41_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT41_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT41_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT41_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd41.setDisable(false);
		}
	}
	
	class DUT42_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd42.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT42_ValidateTimerTask: WAIT");
			try {
				lscsDUT42_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT42_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT42_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT42_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd42.setDisable(false);
		}
	}
	
	class DUT43_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd43.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT43_ValidateTimerTask: WAIT");
			try {
				lscsDUT43_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT43_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT43_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT43_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd43.setDisable(false);
		}
	}
	
	class DUT44_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd44.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT44_ValidateTimerTask: WAIT");
			try {
				lscsDUT44_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT44_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT44_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT44_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd44.setDisable(false);
		}
	}
	
	class DUT45_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd45.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT45_ValidateTimerTask: WAIT");
			try {
				lscsDUT45_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT45_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT45_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT45_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd45.setDisable(false);
		}
	}
	
	class DUT46_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd46.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT46_ValidateTimerTask: WAIT");
			try {
				lscsDUT46_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT46_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT46_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT46_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd46.setDisable(false);
		}
	}
	
	class DUT47_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd47.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT47_ValidateTimerTask: WAIT");
			try {
				lscsDUT47_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT47_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT47_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT47_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd47.setDisable(false);
		}
	}
	
	class DUT48_ValidateTimerTask extends TimerTask {
		public void run() {
			btnValidateDUT_Cmd48.setDisable(true);
			ApplicationLauncher.setCursor(Cursor.WAIT);
			ApplicationLauncher.logger.info("DUT48_ValidateTimerTask: WAIT");
			try {
				lscsDUT48_ValidateSerialCmd();
				
			} catch (Exception e) {
				
				e.printStackTrace();
				ApplicationLauncher.logger.error("DUT48_ValidateTimerTask: Exception:"+e.getMessage());
			}
			DUT48_ValidateTimer.cancel();
			ApplicationLauncher.setCursor(Cursor.DEFAULT);
			ApplicationLauncher.logger.info("DUT48_ValidateTimerTask: DEFAULT");
			btnValidateDUT_Cmd48.setDisable(false);
		}
	}	
	
	 public void enableBusyLoadingScreen() {//long time_in_seconds) {
		 ApplicationLauncher.logger.info("SystemSettingController: enableBusyLoadingScreen: entry");

		 //FXMLLoader loader = new FXMLLoader(
		 //		getClass().getResource("/fxml/setting/ScanDevice" + ConstantApp.THEME_FXML));
		 Parent nodeFromFXML = null;
		 try {
			 nodeFromFXML = getNodeFromFXML("/fxml/setting/BusyLoading" + ConstantApp.THEME_FXML);
			 ApplicationHomeController.displayBusyLoadingScreen(nodeFromFXML);
		 }catch(Exception e) {
			 e.printStackTrace();
			 ApplicationLauncher.logger.error("SystemSettingController: enableBusyLoadingScreen: Exception: "+e.getMessage());
		 }
	 }

	 public void disableBusyLoadingScreen(){
		 BusyLoadingController.removeBusyLoadingScreenOverlay();
	 }
	 
	 private Parent getNodeFromFXML(String url) throws IOException{
			return FXMLLoader.load(getClass().getResource(url));
	 }
	
}
