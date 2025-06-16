package com.tasnetwork.calibration.energymeter.project;

import com.tasnetwork.calibration.energymeter.constant.LogLevel;

public class LogEntry {
    private final String message;
    private final LogLevel level;

    public LogEntry(String message, LogLevel level) {
        String timestamp = java.time.LocalTime.now().withNano(0).toString();
        this.message = "[" + timestamp + "] " + message;
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public LogLevel getLevel() {
        return level;
    }
}

