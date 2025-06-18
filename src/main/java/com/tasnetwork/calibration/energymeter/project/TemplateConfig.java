package com.tasnetwork.calibration.energymeter.project;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Configuration class for mapping Result properties to Excel cell ranges.
 * This class is used to serialize and deserialize template configurations to/from JSON.
 */
public class TemplateConfig {
    // Map where key = Result property name (camelCase), value = Excel cell range string (e.g., "A2:A10")
    private Map<String, String> propertyToCellRange;

    public TemplateConfig() {
        // Initialize with an empty map or default values if desired
        this.propertyToCellRange = new HashMap<>();
    }

    public Map<String, String> getPropertyToCellRange() {
        return propertyToCellRange;
    }

    public void setPropertyToCellRange(Map<String, String> propertyToCellRange) {
        this.propertyToCellRange = propertyToCellRange;
    }

    /**
     * Helper method to get the start column index (0-indexed) from a cell range string.
     * Assumes single-column range for now (e.g., "A2:A10" refers to column A).
     * @param cellRange The cell range string (e.g., "A2:A10", "B5").
     * @return The 0-indexed column number, or -1 if invalid.
     */
    public static int getColumnIndexFromCellRange(String cellRange) {
        if (cellRange == null || cellRange.trim().isEmpty()) {
            return -1;
        }
        // Extract the column part (e.g., "A", "AA")
        Pattern columnPattern = Pattern.compile("^([A-Z]+)");
        Matcher matcher = columnPattern.matcher(cellRange.trim().toUpperCase());
        if (matcher.find()) {
            String columnLetters = matcher.group(1);
            int col = 0;
            for (char c : columnLetters.toCharArray()) {
                col = col * 26 + (c - 'A' + 1);
            }
            return col - 1; // Convert to 0-indexed
        }
        return -1;
    }

    /**
     * Helper method to get the start row index (0-indexed) from a cell range string.
     * @param cellRange The cell range string (e.g., "A2:A10", "B5").
     * @return The 0-indexed row number, or -1 if invalid.
     */
    public static int getStartRowIndexFromCellRange(String cellRange) {
        if (cellRange == null || cellRange.trim().isEmpty()) {
            return -1;
        }
        Pattern rowPattern = Pattern.compile("([0-9]+)"); // Get first number
        Matcher matcher = rowPattern.matcher(cellRange.trim());
        if (matcher.find()) {
            try {
                return Integer.parseInt(matcher.group(1)) - 1; // Convert to 0-indexed
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return -1;
    }

    /**
     * Helper method to get the end row index (0-indexed) from a cell range string.
     * Handles both single cell (e.g., "A5" -> returns 4) and range (e.g., "A2:A10" -> returns 9).
     * @param cellRange The cell range string (e.g., "A2:A10", "B5").
     * @return The 0-indexed end row number, or -1 if invalid.
     */
    public static int getEndRowIndexFromCellRange(String cellRange) {
        if (cellRange == null || cellRange.trim().isEmpty()) {
            return -1;
        }
        String[] parts = cellRange.trim().split(":");
        String endCellPart = (parts.length > 1) ? parts[1] : parts[0];

        Pattern rowPattern = Pattern.compile("([0-9]+)$"); // Get number at the end
        Matcher matcher = rowPattern.matcher(endCellPart.toUpperCase());
        if (matcher.find()) {
            try {
                return Integer.parseInt(matcher.group(1)) - 1; // Convert to 0-indexed
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return -1;
    }
}
