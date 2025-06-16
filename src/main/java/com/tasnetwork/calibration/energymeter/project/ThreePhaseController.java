package com.tasnetwork.calibration.energymeter.project;

import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantArduinoCommands;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.device.DutSerialDataManager;
import com.tasnetwork.spring.orm.model.DutCommand;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

public class ThreePhaseController implements Initializable {

	@FXML private Button btn3PhaseCurrentAvg;
	@FXML private Button btn3PhasePFAvg;
	@FXML private Button btn3PhaseVATotal;
	@FXML private Button btn3PhaseVBR;
	@FXML private Button btn3PhaseVLL;
	@FXML private Button btn3PhaseVLN;
	@FXML private Button btn3PhaseVRY;
	@FXML private Button btn3PhaseVYB;
	@FXML private Button btn3PhaseWattsTotal;

	@FXML private ProgressIndicator pi_Auto3Phase;
	@FXML private ProgressIndicator pi_AutoRPhase;

	@FXML private ToggleButton toggle_Auto3Phase;
	@FXML private ToggleButton toggle_AutoRPhase;

	@FXML private TextField txt3PhaseCurrentAvg;
	@FXML private TextField txt3PhasePFAvg;
	@FXML private TextField txt3PhaseVATotal;
	@FXML private TextField txt3PhaseVBR;
	@FXML private TextField txt3PhaseVLL;
	@FXML private TextField txt3PhaseVLN;
	@FXML private TextField txt3PhaseVRY;
	@FXML private TextField txt3PhaseVYB;
	@FXML private TextField txt3PhaseWattsTotal;

	// Static references for global access
	public static Button ref_btn3PhaseCurrentAvg;
	public static Button ref_btn3PhasePFAvg;
	public static Button ref_btn3PhaseVATotal;
	public static Button ref_btn3PhaseVBR;
	public static Button ref_btn3PhaseVLL;
	public static Button ref_btn3PhaseVLN;
	public static Button ref_btn3PhaseVRY;
	public static Button ref_btn3PhaseVYB;
	public static Button ref_btn3PhaseWattsTotal;

	public static ProgressIndicator ref_pi_Auto3Phase;
	public static ProgressIndicator ref_pi_AutoRPhase;

	public static ToggleButton ref_toggle_Auto3Phase;
	public static ToggleButton ref_toggle_AutoRPhase;

	public static TextField ref_txt3PhaseCurrentAvg;
	public static TextField ref_txt3PhasePFAvg;
	public static TextField ref_txt3PhaseVATotal;
	public static TextField ref_txt3PhaseVBR;
	public static TextField ref_txt3PhaseVLL;
	public static TextField ref_txt3PhaseVLN;
	public static TextField ref_txt3PhaseVRY;
	public static TextField ref_txt3PhaseVYB;
	public static TextField ref_txt3PhaseWattsTotal;
    
	private Timer phase3CurrentAvgTimer;
	private Timer phase3PFAvgTimer;
	private Timer phase3VATotalTimer;
	private Timer phase3VBRTimer;
	private Timer phase3VLLTimer;
	private Timer phase3VLNTimer;
	private Timer phase3VRYTimer;
	private Timer phase3VYBTimer;
	private Timer phase3WattsTotalTimer;
	
	/**
     * Initializes the controller and sets up the GUI components.
     * - Sets up table columns and their properties
     * - Initializes UI controls and their event handlers
     * - Loads initial data from database
     * - Sets up validation rules for input fields
     */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		ApplicationLauncher.logger.debug("ThreePhaseController : initialize: " );
		refAssignment();
	}

	/**
	 * Initialize method to assign instance controls to static references.
	 * Call this inside the @FXML initialize() method.
	 */
	private void refAssignment() {
	    ref_btn3PhaseCurrentAvg = btn3PhaseCurrentAvg;
	    ref_btn3PhasePFAvg = btn3PhasePFAvg;
	    ref_btn3PhaseVATotal = btn3PhaseVATotal;
	    ref_btn3PhaseVBR = btn3PhaseVBR;
	    ref_btn3PhaseVLL = btn3PhaseVLL;
	    ref_btn3PhaseVLN = btn3PhaseVLN;
	    ref_btn3PhaseVRY = btn3PhaseVRY;
	    ref_btn3PhaseVYB = btn3PhaseVYB;
	    ref_btn3PhaseWattsTotal = btn3PhaseWattsTotal;

	    ref_pi_Auto3Phase = pi_Auto3Phase;
	    ref_pi_AutoRPhase = pi_AutoRPhase;

	    ref_toggle_Auto3Phase = toggle_Auto3Phase;
	    ref_toggle_AutoRPhase = toggle_AutoRPhase;

	    ref_txt3PhaseCurrentAvg = txt3PhaseCurrentAvg;
	    ref_txt3PhasePFAvg = txt3PhasePFAvg;
	    ref_txt3PhaseVATotal = txt3PhaseVATotal;
	    ref_txt3PhaseVBR = txt3PhaseVBR;
	    ref_txt3PhaseVLL = txt3PhaseVLL;
	    ref_txt3PhaseVLN = txt3PhaseVLN;
	    ref_txt3PhaseVRY = txt3PhaseVRY;
	    ref_txt3PhaseVYB = txt3PhaseVYB;
	    ref_txt3PhaseWattsTotal = txt3PhaseWattsTotal;
	}

	// ON CLICKS
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
	
	// TASKS
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
	
	// FUNCTIONS
	public void phase3CurrentAvgTask() {
        Platform.runLater(() -> ref_btn3PhaseCurrentAvg.setDisable(true));

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
            String value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;

            Platform.runLater(() -> ref_txt3PhaseCurrentAvg.setText(value));
        }

        Platform.runLater(() -> ref_btn3PhaseCurrentAvg.setDisable(false));
    }

    public void phase3PFAvgTask() {
        Platform.runLater(() -> ref_btn3PhasePFAvg.setDisable(true));

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
            String value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;

            Platform.runLater(() -> ref_txt3PhasePFAvg.setText(value));
        }

        Platform.runLater(() -> ref_btn3PhasePFAvg.setDisable(false));
    }

    public void phase3VATotalTask() {
        Platform.runLater(() -> ref_btn3PhaseVATotal.setDisable(true));

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
            String value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;

            Platform.runLater(() -> ref_txt3PhaseVATotal.setText(value));
        }

        Platform.runLater(() -> ref_btn3PhaseVATotal.setDisable(false));
    }

    public void phase3VBRTask() {
        Platform.runLater(() -> ref_btn3PhaseVBR.setDisable(true));

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
            String value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;

            Platform.runLater(() -> ref_txt3PhaseVBR.setText(value));
        }

        Platform.runLater(() -> ref_btn3PhaseVBR.setDisable(false));
    }

    public void phase3VLLTask() {
        Platform.runLater(() -> ref_btn3PhaseVLL.setDisable(true));

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
            String value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;

            Platform.runLater(() -> ref_txt3PhaseVLL.setText(value));
        }

        Platform.runLater(() -> ref_btn3PhaseVLL.setDisable(false));
    }

    public void phase3VLNTask() {
        Platform.runLater(() -> ref_btn3PhaseVLN.setDisable(true));

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
            String value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;

            Platform.runLater(() -> ref_txt3PhaseVLN.setText(value));
        }

        Platform.runLater(() -> ref_btn3PhaseVLN.setDisable(false));
    }

    public void phase3VRYTask() {
        Platform.runLater(() -> ref_btn3PhaseVRY.setDisable(true));

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
            String value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;

            Platform.runLater(() -> ref_txt3PhaseVRY.setText(value));
        }

        Platform.runLater(() -> ref_btn3PhaseVRY.setDisable(false));
    }

    public void phase3VYBTask() {
        Platform.runLater(() -> ref_btn3PhaseVYB.setDisable(true));

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
            String value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;

            Platform.runLater(() -> ref_txt3PhaseVYB.setText(value));
        }

        Platform.runLater(() -> ref_btn3PhaseVYB.setDisable(false));
    }

    public void phase3WattsTotalTask() {
        Platform.runLater(() -> ref_btn3PhaseWattsTotal.setDisable(true));

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
            String value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;

            Platform.runLater(() -> ref_txt3PhaseWattsTotal.setText(value));
        }

        Platform.runLater(() -> ref_btn3PhaseWattsTotal.setDisable(false));
    }

}
