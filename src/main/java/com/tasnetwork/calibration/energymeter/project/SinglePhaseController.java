package com.tasnetwork.calibration.energymeter.project;

import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import com.sun.java.accessibility.util.GUIInitializedListener;
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
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

public class SinglePhaseController implements Initializable{

	@FXML private Button btnRPhaseCurrent;
	@FXML private Button btnRPhasePF;
	@FXML private Button btnRPhaseVA;
	@FXML private Button btnRPhaseVoltage;
	@FXML private Button btnRPhaseWatts;

	@FXML private ProgressIndicator pi_AutoRPhase;

	@FXML private ToggleButton toggle_AutoRPhase;

	@FXML private TextField txtRPhaseCurrent;
	@FXML private TextField txtRPhasePF;
	@FXML private TextField txtRPhaseVA;
	@FXML private TextField txtRPhaseVoltage;
	@FXML private TextField txtRPhaseWatts;
	
	@FXML private LineChart<Number, Number> lineChartRPhaseVoltage;
	@FXML private NumberAxis xAxisRPhase;

	private static final int WINDOW_SIZE = 20; // show last 20 seconds
	private XYChart.Series<Number, Number> voltageSeries;
	private Timer autoRPhaseTimer;
	private int voltageTimeCounter = 0;

	public static Button ref_btnRPhaseCurrent;
	public static Button ref_btnRPhasePF;
	public static Button ref_btnRPhaseVA;
	public static Button ref_btnRPhaseVoltage;
	public static Button ref_btnRPhaseWatts;

	public static ProgressIndicator ref_pi_AutoRPhase;

	public static ToggleButton ref_toggle_AutoRPhase;

	public static TextField ref_txtRPhaseCurrent;
	public static TextField ref_txtRPhasePF;
	public static TextField ref_txtRPhaseVA;
	public static TextField ref_txtRPhaseVoltage;
	public static TextField ref_txtRPhaseWatts;
	
	private Timer rPhaseCurrentTimer;
	private Timer rPhasePFTimer;
	private Timer rPhaseVATimer;
	private Timer rPhaseVoltageTimer;
	private Timer rPhaseWattsTimer;
	
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
		GUIInit();
		listenersAssignment();
	}
	
	/**
	 * Initialize method to assign instance controls to static references.
	 * Call this inside the @FXML initialize() method.
	 */
	private void refAssignment() {
	    ref_btnRPhaseCurrent = btnRPhaseCurrent;
	    ref_btnRPhasePF = btnRPhasePF;
	    ref_btnRPhaseVA = btnRPhaseVA;
	    ref_btnRPhaseVoltage = btnRPhaseVoltage;
	    ref_btnRPhaseWatts = btnRPhaseWatts;

	    ref_pi_AutoRPhase = pi_AutoRPhase;

	    ref_toggle_AutoRPhase = toggle_AutoRPhase;

	    ref_txtRPhaseCurrent = txtRPhaseCurrent;
	    ref_txtRPhasePF = txtRPhasePF;
	    ref_txtRPhaseVA = txtRPhaseVA;
	    ref_txtRPhaseVoltage = txtRPhaseVoltage;
	    ref_txtRPhaseWatts = txtRPhaseWatts;
	}
	
	private void GUIInit() {
		voltageSeries = new XYChart.Series<>();
		voltageSeries.setName("R-Phase Voltage");
		lineChartRPhaseVoltage.getData().add(voltageSeries);
		lineChartRPhaseVoltage.setCreateSymbols(false);
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
	}
	
	/**
	 * Starts the automatic R-phase voltage reading and updates the line chart.
	 * A new value is fetched from the Arduino every second to avoid overload.
	 */
	public void startAutoRPhaseUpdate() {
	    ref_pi_AutoRPhase.setVisible(true);
	    voltageSeries.getData().clear();
	    voltageTimeCounter = 0;

	    autoRPhaseTimer = new Timer();
	    autoRPhaseTimer.scheduleAtFixedRate(new TimerTask() {
	        @Override
	        public void run() {
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
	                String value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;

	                if (status) {
	                    try {
	                        double voltage = Double.parseDouble(value.trim());
	                        Platform.runLater(() -> {
	                        	int currentTime = voltageTimeCounter++;
	                        	voltageSeries.getData().add(new XYChart.Data<>(currentTime, voltage));

	                        	if (currentTime > WINDOW_SIZE) {
	                        	    xAxisRPhase.setLowerBound(currentTime - WINDOW_SIZE);
	                        	    xAxisRPhase.setUpperBound(currentTime);
	                        	} else {
	                        	    xAxisRPhase.setLowerBound(0);
	                        	    xAxisRPhase.setUpperBound(WINDOW_SIZE);
	                        	}

	                            ref_txtRPhaseVoltage.setText(value);
	                        });
	                    } catch (NumberFormatException e) {
	                        Platform.runLater(() -> ref_txtRPhaseVoltage.setText("ParseError"));
	                    }
	                } else {
	                    Platform.runLater(() -> ref_txtRPhaseVoltage.setText(ConstantArduinoCommands.CMD_FAILED));
	                }
	            }
	        }
	    }, 0, 1000); // every 1000 ms (1 second)
	}

	/**
	 * Stops the automatic R-phase voltage reading and hides the progress indicator.
	 */
	public void stopAutoRPhaseUpdate() {
	    if (autoRPhaseTimer != null) {
	        autoRPhaseTimer.cancel();
	        autoRPhaseTimer = null;
	    }
	    ref_pi_AutoRPhase.setVisible(false);
	}



    // ON CLICK 
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
	
	// TASKS
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
	
	// FUNCTIONS
	public void rPhaseVoltageTask() {
        Platform.runLater(() -> ref_btnRPhaseVoltage.setDisable(true));

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
            String value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;

            Platform.runLater(() -> ref_txtRPhaseVoltage.setText(value));
        }

        Platform.runLater(() -> ref_btnRPhaseVoltage.setDisable(false));
    }

    public void rPhaseCurrentTask() {
        Platform.runLater(() -> ref_btnRPhaseCurrent.setDisable(true));

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
            String value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;

            Platform.runLater(() -> ref_txtRPhaseCurrent.setText(value));
        } else {
            Platform.runLater(() -> ref_txtRPhaseCurrent.setText(ConstantArduinoCommands.CMD_FAILED));
        }

        Platform.runLater(() -> ref_btnRPhaseCurrent.setDisable(false));
    }

    public void rPhaseWattsTask() {
        Platform.runLater(() -> ref_btnRPhaseWatts.setDisable(true));

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
            String value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;

            Platform.runLater(() -> ref_txtRPhaseWatts.setText(value));
        }

        Platform.runLater(() -> ref_btnRPhaseWatts.setDisable(false));
    }

    public void rPhaseVATask() {
        Platform.runLater(() -> ref_btnRPhaseVA.setDisable(true));

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
            String value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;

            Platform.runLater(() -> ref_txtRPhaseVA.setText(value));
        }

        Platform.runLater(() -> ref_btnRPhaseVA.setDisable(false));
    }

    public void rPhasePFTask() {
        Platform.runLater(() -> ref_btnRPhasePF.setDisable(true));

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
            String value = status ? (String) resp.get("result") : ConstantArduinoCommands.CMD_FAILED;

            Platform.runLater(() -> ref_txtRPhasePF.setText(value));
        }

        Platform.runLater(() -> ref_btnRPhasePF.setDisable(false));
    }

}

