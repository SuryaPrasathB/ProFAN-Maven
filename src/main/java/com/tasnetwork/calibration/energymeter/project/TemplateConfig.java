package com.tasnetwork.calibration.energymeter.project;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Configuration class for mapping Result properties to Excel cell ranges.
 * This class is used to serialize and deserialize template configurations to/from JSON.
 * It now includes a 'numRecords' field and handles single start cell references.
 */
public class TemplateConfig {
    // Number of records to be written into the Excel template. This determines the range size.
    private int numRecords;

    // Map where key = Result property name (camelCase), value = Excel STARTING cell string (e.g., "A2", "B5")
    private Map<String, String> propertyToCellRange;

    public TemplateConfig() {
        this.numRecords = 0; // Default value
        this.propertyToCellRange = new HashMap<>();
    }

    public int getNumRecords() {
        return numRecords;
    }

    public void setNumRecords(int numRecords) {
        this.numRecords = numRecords;
    }

    public Map<String, String> getPropertyToCellRange() {
        return propertyToCellRange;
    }

    public void setPropertyToCellRange(Map<String, String> propertyToCellRange) {
        this.propertyToCellRange = propertyToCellRange;
    }

    /**
     * Helper method to get the column index (0-indexed) from a single cell reference string.
     * e.g., "A2" -> 0, "B5" -> 1
     * @param cellReference The single cell reference string (e.g., "A2", "B5").
     * @return The 0-indexed column number, or -1 if invalid.
     */
    public static int getColumnIndexFromCellReference(String cellReference) {
        if (cellReference == null || cellReference.trim().isEmpty()) {
            return -1;
        }
        // Extract the column part (e.g., "A", "AA")
        Pattern columnPattern = Pattern.compile("^([A-Z]+)");
        Matcher matcher = columnPattern.matcher(cellReference.trim().toUpperCase());
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
     * Helper method to get the row index (0-indexed) from a single cell reference string.
     * e.g., "A2" -> 1, "B5" -> 4
     * @param cellReference The single cell reference string (e.g., "A2", "B5").
     * @return The 0-indexed row number, or -1 if invalid.
     */
    public static int getRowIndexFromCellReference(String cellReference) {
        if (cellReference == null || cellReference.trim().isEmpty()) {
            return -1;
        }
        Pattern rowPattern = Pattern.compile("([0-9]+)$"); // Get number at the end
        Matcher matcher = rowPattern.matcher(cellReference.trim());
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
     * Calculates the end row index (0-indexed) for a given starting row and number of records.
     * @param startRowIndex The 0-indexed starting row.
     * @param numRecords The total number of records that will occupy rows starting from startRowIndex.
     * @return The 0-indexed end row number.
     */
    public static int getEndRowIndex(int startRowIndex, int numRecords) {
        if (numRecords <= 0) {
            return startRowIndex; // If no records, or invalid count, end at start row.
        }
        return startRowIndex + numRecords - 1;
    }
}
