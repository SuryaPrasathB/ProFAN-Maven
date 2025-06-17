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

    /**
     * Retrieves the last error message set by any operation in this utility class.
     *
     * @return The last error message.
     */
    public static String getLastErrorMessage() {
        return lastErrorMessage;
    }

    /**
     * Sets the voltage on the Device Under Test (DUT).
     *
     * @param voltage The voltage value to set.
     * @return true if the voltage was successfully set, false otherwise.
     */
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
                ApplicationLauncher.logger.error(lastErrorMessage);
                return false;
            }
        } catch (Exception e) {
            // Log exception details for debugging
            lastErrorMessage = "Error setting voltage: " + e.getMessage();
            ApplicationLauncher.logger.error("Error in setVoltage method", e); // Log the error
            return false;
        }
    }

    /**
     * Sends a command to the DUT to turn on its main power supply.
     * Updates lastErrorMessage on failure.
     *
     * @return true if mains were successfully turned on, false otherwise.
     */
    public static boolean mainsOn() {
        return executeDutCommandBoolean("MainsOn_");
    }

    /**
     * Sends a command to the DUT to turn off its main power supply.
     * Updates lastErrorMessage on failure.
     *
     * @return true if mains were successfully turned off, false otherwise.
     */
    public static boolean mainsOff() {
        return executeDutCommandBoolean("MainsOff_");
    }

    /**
     * Reads the R-Phase voltage from the DUT.
     * Updates lastErrorMessage on failure.
     *
     * @return The R-Phase voltage as a String, "CMD FAIL" if command execution fails, or "NO CMD" if no matching command is found.
     */
    public static String readRPhaseVoltage() {
        return executeDutCommand("RPhaseVoltage_");
    }

    /**
     * Reads the RY voltage from the DUT (for 3-Phase systems).
     * Updates lastErrorMessage on failure.
     *
     * @return The RY voltage as a String, "CMD FAIL" if command execution fails, or "NO CMD" if no matching command is found.
     */
    public static String readRYVoltage() {
        return executeDutCommand("3PhaseVRY_");
    }

    /**
     * Reads the fan RPM from the DUT.
     * Updates lastErrorMessage on failure.
     *
     * @return The fan RPM as a String, "CMD FAIL" if command execution fails, or "NO CMD" if no matching command is found.
     */
    public static String readRpm() {
        return executeDutCommand("FanRpm_");
    }

    /**
     * Reads the fan wind speed from the DUT.
     * Updates lastErrorMessage on failure.
     *
     * @return The fan wind speed as a String, "CMD FAIL" if command execution fails, or "NO CMD" if no matching command is found.
     */
    public static String readWindSpeed() {
        return executeDutCommand("FanWindspeed_");
    }

    /**
     * Reads the current from the DUT (e.g., 3-Phase Average Current).
     * Updates lastErrorMessage on failure.
     *
     * @return The current as a String, "CMD FAIL" if command execution fails, or "NO CMD" if no matching command is found.
     */
    public static String readCurrent() {
        return executeDutCommand("3PhaseCurrentAvg_");
    }

    /**
     * Reads the total watts from the DUT (e.g., 3-Phase Total Watts).
     * Updates lastErrorMessage on failure.
     *
     * @return The watts as a String, "CMD FAIL" if command execution fails, or "NO CMD" if no matching command is found.
     */
    public static String readWatts() {
        return executeDutCommand("3PhaseWattsTotal_");
    }

    /**
     * Reads the active power (VA) from the DUT.
     * Updates lastErrorMessage on failure.
     *
     * @return The active power as a String, "CMD FAIL" if command execution fails, or "NO CMD" if no matching command is found.
     */
    public static String readActivePower() {
        return executeDutCommand("3PhaseVATotal_");
    }

    /**
     * Reads the power factor from the DUT (e.g., 3-Phase Average Power Factor).
     * Updates lastErrorMessage on failure.
     *
     * @return The power factor as a String, "CMD FAIL" if command execution fails, or "NO CMD" if no matching command is found.
     */
    public static String readPowerFactor() {
        return executeDutCommand("3PhasePFAvg_");
    }

    /**
     * Helper method to execute a DUT command that returns a String result.
     * This method handles finding the command, executing it, and interpreting the response.
     * It also updates the lastErrorMessage in case of failure.
     *
     * @param testCasePrefix The prefix of the test case name to search for.
     * @return The result string from the DUT, "CMD FAIL" if command execution status is false, or "NO CMD" if the command is not found.
     */
    private static String executeDutCommand(String testCasePrefix) {
        String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;

        Optional<DutCommand> opt = DeviceDataManagerController.getDutCommandService()
                .findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCasePrefix);

        if (opt.isPresent()) {
            try {
                DeviceDataManagerController.setDutCommandData(opt.get());

                Map<String, Object> resp = new DutSerialDataManager()
                        .dutExecuteCommand(ConstantAppConfig.DUT_COMMAND_INTERFACE_ID, "", false);

                boolean status = resp.get("status") instanceof Boolean && (Boolean) resp.get("status");
                if (status) {
                    lastErrorMessage = ""; // Clear error on success
                    return resp.get("result") instanceof String ? (String) resp.get("result") : "Invalid Result Type";
                } else {
                    lastErrorMessage = "DUT command '" + testCasePrefix + "' failed: " + resp.getOrDefault("message", "Unknown reason").toString();
                    ApplicationLauncher.logger.error(lastErrorMessage);
                    return "CMD FAIL";
                }
            } catch (Exception e) {
                lastErrorMessage = "Error executing DUT command '" + testCasePrefix + "': " + e.getMessage();
                ApplicationLauncher.logger.error("Error in executeDutCommand for " + testCasePrefix, e);
                return "CMD FAIL";
            }
        } else {
            lastErrorMessage = "DutCommand not found for project: " + projectName + " and test case: " + testCasePrefix;
            ApplicationLauncher.logger.error(lastErrorMessage);
            return "NO CMD";
        }
    }

    /**
     * Helper method to execute a DUT command that returns a Boolean result.
     * This method handles finding the command, executing it, and interpreting the response.
     * It also updates the lastErrorMessage in case of failure.
     *
     * @param testCasePrefix The prefix of the test case name to search for.
     * @return true if the command was successfully executed and returned true, false otherwise (including command not found, execution failure, or false result).
     */
    private static Boolean executeDutCommandBoolean(String testCasePrefix) {
        String projectName = ConstantAppConfig.DUT_COMMAND_PROJECT_NAME;

        Optional<DutCommand> opt = DeviceDataManagerController.getDutCommandService()
                .findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCasePrefix);

        if (opt.isPresent()) {
            try {
                DeviceDataManagerController.setDutCommandData(opt.get());

                Map<String, Object> resp = new DutSerialDataManager()
                        .dutExecuteCommand(ConstantAppConfig.DUT_COMMAND_INTERFACE_ID, "", false);

                boolean status = resp.get("status") instanceof Boolean && (Boolean) resp.get("status");
                if (status) {
                    lastErrorMessage = ""; // Clear error on success
                    // Ensure the 'result' is indeed a Boolean before casting
                    return resp.get("status") instanceof Boolean ? (Boolean) resp.get("status") : false;
                } else {
                    lastErrorMessage = "DUT command '" + testCasePrefix + "' failed: " + resp.getOrDefault("message", "Unknown reason").toString();
                    ApplicationLauncher.logger.error(lastErrorMessage);
                    return false;
                }
            } catch (Exception e) {
                lastErrorMessage = "Error executing DUT command '" + testCasePrefix + "': " + e.getMessage();
                ApplicationLauncher.logger.error("Error in executeDutCommandBoolean for " + testCasePrefix, e);
                return false;
            }
        } else {
            lastErrorMessage = "DutCommand not found for project: " + projectName + " and test case: " + testCasePrefix;
            ApplicationLauncher.logger.error(lastErrorMessage);
            return false;
        }
    }
}
