package com.tasnetwork.calibration.energymeter.project;

import java.util.Map;
import java.util.Optional;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.device.DutSerialDataManager;
import com.tasnetwork.spring.orm.model.DutCommand;

public class TestPointUtils {

	private static String lastErrorMessage = "";

	public static String getLastErrorMessage() {
	    return lastErrorMessage;
	}

	public static boolean setVoltage(String voltage) {
	    try {
	        String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;
	        String testCaseName = "SetVoltage_";

	        // Find the DutCommand based on the project name and test case name
	        Optional<DutCommand> dutCommandDataOpt = DeviceDataManagerController.getDutCommandService()
	                .findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);

	        // If DutCommand is found, execute the command
	        if (dutCommandDataOpt.isPresent()) {
	            DutCommand dutCommand = dutCommandDataOpt.get();
	            DeviceDataManagerController.setDutCommandData(dutCommand);

	            // Execute the command to set the voltage
	            new DutSerialDataManager().dutExecuteCommand(ConstantAppConfig.DUT_COMMAND_INTERFACE_ID, voltage, true);
	            
	            // Reset error message on success
	            lastErrorMessage = ""; 
	            return true;
	        } else {
	            // Log error if DutCommand was not found
	            lastErrorMessage = "DutCommand not found for project: " + projectName + " and test case: " + testCaseName;
	            return false;
	        }
	    } catch (Exception e) {
	        // Log exception details for debugging
	        lastErrorMessage = "Error setting voltage: " + e.getMessage();
	        ApplicationLauncher.logger.error("Error in setVoltage method", e);  // Log the error
	        return false;
	    }
	}


    public static String readRPhaseVoltage() {
        return executeDutCommand("RPhaseVoltage_");
    }

    public static String readRYVoltage() {
        return executeDutCommand("3PhaseVRY_");
    }
    
    public static String readRpm() {
        return executeDutCommand("FanRpm_");
    }

    public static String readWindSpeed() {
        return executeDutCommand("FanWindspeed_");
    }

    public static String readCurrent() {
        return executeDutCommand("3PhaseCurrentAvg_");
    }

    public static String readWatts() {
        return executeDutCommand("3PhaseWattsTotal_");
    }

    public static String readActivePower() {
        return executeDutCommand("3PhaseVATotal_");
    }

    public static String readPowerFactor() {
        return executeDutCommand("3PhasePFAvg_");
    }

    private static String executeDutCommand(String testCasePrefix) {
        String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;

        Optional<DutCommand> opt = DeviceDataManagerController.getDutCommandService()
                .findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCasePrefix);

        if (opt.isPresent()) {
            DeviceDataManagerController.setDutCommandData(opt.get());

            Map<String, Object> resp = new DutSerialDataManager()
                    .dutExecuteCommand(ConstantAppConfig.DUT_COMMAND_INTERFACE_ID, "", false);

            boolean status = resp.get("status") instanceof Boolean && (Boolean) resp.get("status");
            return status ? (String) resp.get("result") : "CMD FAIL";
        }

        return "NO CMD";
    }
    
    
}

