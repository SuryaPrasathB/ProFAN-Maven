package com.tasnetwork.calibration.energymeter.util;

import java.io.*;
import java.util.Properties;

public class Settings {

    public static double voltageTolerance = 5.0;
    public static int setupDelay = 5000;
    public static int maxRetryAttempts = 10;
    public static int retryDelay = 1000;
    public static int loopDelay = 2000;
    public static int loggingFrequency = 1000;

    private static final String SETTINGS_FILE = "settings.properties";

    static {
        load();
    }

    public static void save() {
        try (FileOutputStream out = new FileOutputStream(SETTINGS_FILE)) {
            Properties props = new Properties();
            props.setProperty("voltageTolerance", String.valueOf(voltageTolerance));
            props.setProperty("setupDelay", String.valueOf(setupDelay));
            props.setProperty("maxRetryAttempts", String.valueOf(maxRetryAttempts));
            props.setProperty("retryDelay", String.valueOf(retryDelay));
            props.setProperty("loopDelay", String.valueOf(loopDelay));
            props.setProperty("loggingFrequency", String.valueOf(loggingFrequency));
            props.store(out, "User Settings");
        } catch (IOException e) {
            System.out.println("Failed to save settings: " + e.getMessage());
        }
    }

    public static void load() {
        try (FileInputStream in = new FileInputStream(SETTINGS_FILE)) {
            Properties props = new Properties();
            props.load(in);
            voltageTolerance = Double.parseDouble(props.getProperty("voltageTolerance", "5.0"));
            setupDelay = Integer.parseInt(props.getProperty("setupDelay", "5000"));
            maxRetryAttempts = Integer.parseInt(props.getProperty("maxRetryAttempts", "10"));
            retryDelay = Integer.parseInt(props.getProperty("retryDelay", "1000"));
            loopDelay = Integer.parseInt(props.getProperty("loopDelay", "2000"));
            loggingFrequency = Integer.parseInt(props.getProperty("loggingFrequency", "1000"));
        } catch (IOException e) {
            System.out.println("No settings file found, using defaults.");
        }
    }
}
