package com.tasnetwork.calibration.energymeter.project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.scene.web.WebView; // New import for WebView
import javafx.scene.web.WebEngine; // New import for WebEngine

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

// Apache POI imports for XLSX handling
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Controller class for the Report Generator UI.
 * Handles interactions and data display for template selection, fan search, and report generation.
 */
public class ReportGeneratorController implements Initializable {

    // FXML UI elements
    @FXML private ListView<String> templateListView;
    // Changed from TextArea to WebView for displaying rich HTML content (e.g., Excel preview)
    @FXML private WebView templatePreviewWebView;
    @FXML private Label noPreviewLabel; // Label to show when no template is selected
    @FXML private TextField serialInputTextField;
    @FXML private TableView<Fan> fanTableView;
    @FXML private TableColumn<Fan, Boolean> selectColumn;
    @FXML private TableColumn<Fan, String> serialColumn;
    @FXML private TableColumn<Fan, String> modelColumn;
    @FXML private TableColumn<Fan, String> statusColumn;
    @FXML private TableColumn<Fan, String> lastServiceColumn;
    @FXML private CheckBox selectAllCheckbox;
    @FXML private Button generateReportButton;
    @FXML private Label errorMessageLabel;
    @FXML private Label noResultsLabel; // Label to show when no fan results are displayed


    // The path to your report templates folder
    // IMPORTANT: In a production environment, avoid hardcoding absolute paths like this.
    // Consider using relative paths based on application launch location, user settings,
    // or resource loading mechanisms appropriate for your deployment strategy.
    private static final String TEMPLATES_DIR_PATH = "C:\\Users\\Surya\\git\\ProFAN-Maven-s0.0.0.7\\src\\main\\resources\\reportTemplates";

    // This will now be populated dynamically
    private ObservableList<Template> availableTemplates = FXCollections.observableArrayList();
    private Template selectedTemplate = null;

    private final ObservableList<Fan> mockFanData = FXCollections.observableArrayList(
            new Fan(100, "SN-00100", "X-Series", "Operational", "2023-01-15", false),
            new Fan(101, "SN-00101", "Y-Series", "Maintenance", "2023-02-20", false),
            new Fan(102, "SN-00102", "X-Series", "Operational", "2023-03-10", false),
            new Fan(103, "SN-00103", "Z-Series", "Operational", "2023-04-01", false),
            new Fan(104, "SN-00104", "Y-Series", "Faulty", "2023-05-05", false),
            new Fan(105, "SN-00105", "X-Series", "Operational", "2023-06-12", false),
            new Fan(106, "SN-00106", "Z-Series", "Maintenance", "2023-07-20", false),
            new Fan(107, "SN-0107", "Y-Series", "Operational", "2023-08-01", false),
            new Fan(108, "SN-0108", "X-Series", "Operational", "2023-09-15", false),
            new Fan(109, "SN-0109", "Z-Series", "Faulty", "2023-10-10", false),
            new Fan(110, "SN-0110", "Y-Series", "Operational", "2023-11-20", false),
            new Fan(111, "SN-0111", "X-Series", "Operational", "2023-12-01", false),
            new Fan(112, "SN-0112", "Z-Series", "Maintenance", "2024-01-05", false),
            new Fan(113, "SN-0113", "Y-Series", "Operational", "2024-02-10", false),
            new Fan(114, "SN-0114", "X-Series", "Operational", "2024-03-15", false),
            new Fan(115, "SN-0115", "Z-Series", "Operational", "2024-04-20", false),
            new Fan(116, "SN-0116", "Y-Series", "Operational", "2024-05-01", false),
            new Fan(117, "SN-0117", "X-Series", "Operational", "2024-06-05", false),
            new Fan(118, "SN-0118", "Z-Series", "Maintenance", "2024-07-10", false),
            new Fan(119, "SN-0119", "Y-Series", "Operational", "2024-08-15", false),
            new Fan(120, "SN-0120", "X-Series", "Faulty", "2024-09-20", false)
    );

    /**
     * Initializes the controller after its root element has been completely processed.
     * Sets up initial UI states and listeners.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load templates from the specified directory
        availableTemplates = loadTemplatesFromDirectory();
        templateListView.setItems(availableTemplates.stream().map(Template::getName).collect(Collectors.toCollection(FXCollections::observableArrayList)));

        // Listener for template selection
        templateListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                // Find the selected template object by name
                selectedTemplate = availableTemplates.stream()
                        .filter(t -> t.getName().equals(newVal))
                        .findFirst()
                        .orElse(null);

                if (selectedTemplate != null && selectedTemplate.getFile() != null) {
                    try {
                        String fileContent = readFileContent(selectedTemplate.getFile());
                        templatePreviewWebView.getEngine().loadContent(fileContent); // Load content into WebView
                        templatePreviewWebView.setVisible(true);
                        noPreviewLabel.setVisible(false); // Hide "No Preview" label
                    } catch (IOException e) {
                        System.err.println("Failed to read file content for: " + selectedTemplate.getName() + " - " + e.getMessage());
                        templatePreviewWebView.getEngine().loadContent("<h1>Error</h1><p>Could not load preview content: " + escapeHtml(e.getMessage()) + "</p>");
                        templatePreviewWebView.setVisible(true); // Show error in WebView
                        noPreviewLabel.setVisible(false); // Hide normal "No Preview" label
                    }
                } else {
                    templatePreviewWebView.getEngine().loadContent(""); // Clear WebView
                    templatePreviewWebView.setVisible(false);
                    noPreviewLabel.setText("No preview available.");
                    noPreviewLabel.setVisible(true);
                }
            } else {
                selectedTemplate = null;
                templatePreviewWebView.getEngine().loadContent(""); // Clear WebView
                templatePreviewWebView.setVisible(false);
                noPreviewLabel.setText("Select a template to see its preview.");
                noPreviewLabel.setVisible(true);
            }
        });

        // Setup Fan TableView columns
        selectColumn.setCellValueFactory(new PropertyValueFactory<>("selected"));
        serialColumn.setCellValueFactory(new PropertyValueFactory<>("serial"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        lastServiceColumn.setCellValueFactory(new PropertyValueFactory<>("lastService"));

        // Custom cell factory for the select checkbox column
        selectColumn.setCellFactory(new Callback<TableColumn<Fan, Boolean>, TableCell<Fan, Boolean>>() {
            @Override
            public TableCell<Fan, Boolean> call(TableColumn<Fan, Boolean> param) {
                return new TableCell<Fan, Boolean>() {
                    private final CheckBox checkBox = new CheckBox();
                    {
                        checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                            Fan fan = (Fan) getTableRow().getItem();
                            if (fan != null) {
                                fan.setSelected(newVal);
                                updateGenerateReportButtonState();
                                updateSelectAllCheckboxState();
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Boolean item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || getItem() == null) {
                            setGraphic(null);
                        } else {
                            checkBox.setSelected(item);
                            setGraphic(checkBox);
                        }
                    }
                };
            }
        });

        // Set initial fan data (show all mock data initially)
        fanTableView.setItems(mockFanData);
        updateTableViewVisibility();

        // Add listener to selectAllCheckbox
        selectAllCheckbox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            for (Fan fan : fanTableView.getItems()) {
                fan.setSelected(newVal);
            }
            fanTableView.refresh(); // Refresh table to update checkboxes
            updateGenerateReportButtonState();
        });

        // Initialize button state
        updateGenerateReportButtonState();
        errorMessageLabel.setText(""); // Clear any initial error message

        // Initial state for preview area
        templatePreviewWebView.setVisible(false);
        noPreviewLabel.setVisible(true);
        noPreviewLabel.setText("Select a template to see its preview.");
    }

    /**
     * Scans the predefined directory for .xlsx, .csv, and .txt files and creates Template objects.
     * @return An ObservableList of Template objects found in the directory.
     */
    private ObservableList<Template> loadTemplatesFromDirectory() {
        ObservableList<Template> templates = FXCollections.observableArrayList();
        File templateDir = new File(TEMPLATES_DIR_PATH);

        if (!templateDir.exists()) {
            System.err.println("Template directory does not exist: " + TEMPLATES_DIR_PATH);
            errorMessageLabel.setText("Error: Template directory not found.");
            return templates;
        }
        if (!templateDir.isDirectory()) {
            System.err.println("Path is not a directory: " + TEMPLATES_DIR_PATH);
            errorMessageLabel.setText("Error: Template path is not a directory.");
            return templates;
        }

        // Filter for .xlsx, .csv, and .txt files
        File[] files = templateDir.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".xlsx") ||
                name.toLowerCase().endsWith(".csv") ||
                name.toLowerCase().endsWith(".txt"));

        if (files != null) {
            for (File file : files) {
                templates.add(new Template(file.getName(), file));
            }
        } else {
            System.err.println("Could not list files in directory: " + TEMPLATES_DIR_PATH);
            errorMessageLabel.setText("Error: Could not access template files.");
        }
        return templates;
    }

    /**
     * Reads the content of a given file into a String for preview.
     * For XLSX files, it generates an HTML representation.
     * For CSV/TXT files, it reads them as plain text (HTML encoded for display in WebView).
     *
     * @param file The file to read.
     * @return The content of the file as an HTML String.
     * @throws IOException If an I/O error occurs reading the file.
     */
    private String readFileContent(File file) throws IOException {
        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(".xlsx")) {
            return generateHtmlPreviewFromXlsx(file);
        } else if (fileName.endsWith(".csv") || fileName.endsWith(".txt")) {
            // Read text content and then HTML escape it for safe display in WebView
            String rawContent = Files.readAllLines(file.toPath()).stream().collect(Collectors.joining(System.lineSeparator()));
            return "<html><body><pre>" + escapeHtml(rawContent) + "</pre></body></html>";
        } else {
            return "<html><body><p>Preview not supported for this file type.</p></body></html>";
        }
    }

    /**
     * Reads the content of the first sheet of an XLSX file using Apache POI
     * and returns an HTML string representation of its cell values,
     * resembling an Excel grid with row numbers and column letters.
     *
     * @param file The XLSX file to read.
     * @return An HTML string containing the table representation of the XLSX content.
     * @throws IOException If an error occurs while reading the file.
     */
    private String generateHtmlPreviewFromXlsx(File file) throws IOException {
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<!DOCTYPE html>\n<html>\n<head>\n<style>\n");
        htmlContent.append("body { margin: 0; padding: 0; }\n");
        htmlContent.append("table { border-collapse: collapse; width: 100%; font-family: 'Monospaced', Courier, monospace; font-size: 12px; }\n");
        htmlContent.append("th, td { border: 1px solid #ccc; padding: 4px 8px; text-align: left; white-space: pre-wrap; vertical-align: top; }\n"); // pre-wrap to handle newlines within cells
        htmlContent.append("th { background-color: #e0e0e0; font-weight: bold; }\n"); // Grey background for headers
        htmlContent.append(".row-header { background-color: #e0e0e0; text-align: right; font-weight: bold; }\n"); // Grey background for row headers
        htmlContent.append("</style>\n</head>\n<body>\n");
        htmlContent.append("<table>\n");

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

            // Determine maximum column index to ensure all columns are displayed
            int maxColIndex = -1;
            for (Row row : sheet) {
                if (row.getLastCellNum() > maxColIndex) {
                    maxColIndex = row.getLastCellNum() -1; // getLastCellNum is 1-based
                }
            }
            if (maxColIndex < 0) maxColIndex = 0; // Ensure at least one column if sheet is empty

            // Generate column headers (A, B, C...)
            htmlContent.append("<thead><tr>\n");
            htmlContent.append("<th class=\"row-header\">&nbsp;</th>"); // Top-left blank cell for corner
            for (int i = 0; i <= maxColIndex; i++) {
                char columnChar = (char) ('A' + i);
                htmlContent.append("<th>").append(columnChar).append("</th>");
            }
            htmlContent.append("</tr></thead>\n");

            htmlContent.append("<tbody>\n");
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            DataFormatter formatter = new DataFormatter();

            // Iterate over each row in the sheet
            for (int r = sheet.getFirstRowNum(); r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                htmlContent.append("<tr>\n");
                htmlContent.append("<td class=\"row-header\">").append(r + 1).append("</td>"); // Row number

                // Iterate over each cell in the row
                for (int i = 0; i <= maxColIndex; i++) {
                    Cell cell = (row == null) ? null : row.getCell(i); // Handle empty rows
                    String cellValue = getCellValueAsString(cell, evaluator, formatter);
                    htmlContent.append("<td>").append(escapeHtml(cellValue)).append("</td>");
                }
                htmlContent.append("</tr>\n");
            }
            htmlContent.append("</tbody>\n");

        } catch (Exception e) {
            System.err.println("Error generating HTML preview for XLSX file: " + file.getName() + " - " + e.getMessage());
            return "<html><body><h1>Error</h1><p>Error reading XLSX file: " + escapeHtml(e.getMessage()) + ". Please ensure it's a valid XLSX format.</p></body></html>";
        }
        htmlContent.append("</table>\n</body>\n</html>");
        return htmlContent.toString();
    }

    /**
     * Helper method to get cell value as a String, handling different cell types.
     * This method now uses DataFormatter.formatCellValue(Cell cell, FormulaEvaluator evaluator)
     * which correctly handles all cell types, including formulas, and applies formatting.
     *
     * @param cell The cell to get the value from.
     * @param evaluator FormulaEvaluator for evaluating formula cells.
     * @param formatter DataFormatter for general numeric and date formatting.
     * @return The string representation of the cell's value.
     */
    private String getCellValueAsString(Cell cell, FormulaEvaluator evaluator, DataFormatter formatter) {
        if (cell == null) {
            return "";
        }
        // Use DataFormatter.formatCellValue with the cell and evaluator.
        // This single call handles STRING, NUMERIC (including dates), BOOLEAN,
        // BLANK, ERROR, and FORMULA cells correctly by evaluating formulas
        // and applying the cell's formatting.
        return formatter.formatCellValue(cell, evaluator);
    }

    /**
     * Helper method to escape HTML special characters.
     * Used to ensure cell values are safely displayed in WebView.
     * @param text The text to escape.
     * @return The HTML-escaped string.
     */
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#x27;"); // For single quotes
    }


    /**
     * Handles the search button click event.
     * Parses the serial input and filters the fan data.
     */
    @FXML
    private void handleSearchFans() {
        errorMessageLabel.setText(""); // Clear previous error messages
        String input = serialInputTextField.getText().trim();
        if (input.isEmpty()) {
            fanTableView.setItems(mockFanData); // Show all if input is empty
            updateTableViewVisibility();
            return;
        }

        Set<Integer> desiredIds = parseSerialInput(input);

        if (!errorMessageLabel.getText().isEmpty()) { // If an error was set during parsing
            fanTableView.setItems(FXCollections.emptyObservableList());
            updateTableViewVisibility();
            return;
        }

        ObservableList<Fan> filteredFans = FXCollections.observableArrayList(
                mockFanData.stream()
                        .filter(fan -> desiredIds.contains(fan.getId()))
                        .collect(Collectors.toList())
        );
        fanTableView.setItems(filteredFans);
        updateTableViewVisibility();
        updateSelectAllCheckboxState(); // Update select all checkbox based on filtered results
        updateGenerateReportButtonState(); // Update generate button state
    }

    /**
     * Parses the input string for serial numbers and returns a set of fan IDs.
     * Supports single numbers, ranges (e.g., 100-120), and comma-separated values.
     * Sets an error message if parsing fails.
     * @param input The raw input string from the search field.
     * @returns A Set of unique fan IDs.
     */
    private Set<Integer> parseSerialInput(String input) {
        Set<Integer> ids = new HashSet<>();
        String[] parts = input.split(",");

        for (String part : parts) {
            String trimmedPart = part.trim();
            if (trimmedPart.isEmpty()) continue;

            if (trimmedPart.contains("-")) {
                String[] rangeParts = trimmedPart.split("-");
                if (rangeParts.length != 2) {
                    errorMessageLabel.setText(String.format("Invalid range format: '%s'. Use 'start-end'.", trimmedPart));
                    return new HashSet<>();
                }
                try {
                    int start = Integer.parseInt(rangeParts[0].trim());
                    int end = Integer.parseInt(rangeParts[1].trim());

                    if (start > end) { // Swap if start is greater than end
                        int temp = start;
                        start = end;
                        end = temp;
                    }

                    for (int i = start; i <= end; i++) {
                        ids.add(i);
                    }
                } catch (NumberFormatException e) {
                    errorMessageLabel.setText(String.format("Invalid numbers in range: '%s'. Please use numeric values.", trimmedPart));
                    return new HashSet<>();
                }
            } else {
                try {
                    ids.add(Integer.parseInt(trimmedPart));
                } catch (NumberFormatException e) {
                    errorMessageLabel.setText(String.format("Invalid serial number: '%s'. Please use numeric values.", trimmedPart));
                    return new HashSet<>();
                }
            }
        }
        return ids;
    }

    /**
     * Handles the generate report button click event.
     * Collects selected fan IDs and the chosen template, then simulates report generation.
     */
    @FXML
    private void handleGenerateReport() {
        List<Fan> fansToReport = fanTableView.getItems().stream()
                .filter(Fan::isSelected)
                .collect(Collectors.toList());

        if (fansToReport.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Fans Selected", "Please select at least one fan to generate a report.");
            return;
        }

        String templateName = (selectedTemplate != null) ? selectedTemplate.getName() : "No Template Selected";
        List<Integer> selectedFanIds = fansToReport.stream().map(Fan::getId).collect(Collectors.toList());

        // In a real application, you would send this data to a backend service or
        // a report generation library (e.g., JasperReports, Apache POI for Excel, iText for PDF).
        System.out.println("Generating report with template: " + templateName);
        System.out.println("For selected fan IDs: " + selectedFanIds);
        System.out.println("Selected fans details: " + fansToReport);

        showAlert(Alert.AlertType.INFORMATION, "Report Generation Initiated",
                String.format("Report will be generated for %d fans using template: %s.\n(Check console for details)",
                        fansToReport.size(), templateName));
    }

    /**
     * Updates the enabled state and text of the generate report button
     * based on the number of selected fans.
     */
    private void updateGenerateReportButtonState() {
        long selectedCount = fanTableView.getItems().stream().filter(Fan::isSelected).count();
        generateReportButton.setDisable(selectedCount == 0);
        generateReportButton.setText("Generate Report (" + selectedCount + " selected)");
    }

    /**
     * Updates the state of the selectAll checkbox based on the selection status
     * of individual fans in the table.
     */
    private void updateSelectAllCheckboxState() {
        if (fanTableView.getItems().isEmpty()) {
            selectAllCheckbox.setSelected(false);
            selectAllCheckbox.setDisable(true);
            return;
        }
        selectAllCheckbox.setDisable(false);
        long selectedCount = fanTableView.getItems().stream().filter(Fan::isSelected).count();
        selectAllCheckbox.setSelected(selectedCount == fanTableView.getItems().size());
        selectAllCheckbox.setIndeterminate(selectedCount > 0 && selectedCount < fanTableView.getItems().size());
    }

    /**
     * Shows or hides the fan table and a "no results" label based on data presence.
     */
    private void updateTableViewVisibility() {
        boolean hasResults = !fanTableView.getItems().isEmpty();
        fanTableView.setVisible(hasResults);
        noResultsLabel.setVisible(!hasResults);
        selectAllCheckbox.setDisable(!hasResults); // Disable select all if no results
    }

    /**
     * Helper method to show a JavaFX Alert dialog.
     * @param type The type of the alert (e.g., WARNING, INFORMATION).
     * @param title The title of the alert dialog.
     * @param message The message content of the alert.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null); // No header text
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Inner class representing a Report Template.
     */
    public static class Template {
        private final String name;
        private final File file; // Storing the File object directly

        public Template(String name, File file) {
            this.name = name;
            this.file = file;
        }

        public String getName() {
            return name;
        }

        public File getFile() {
            return file;
        }
    }

    /**
     * Inner class representing a Fan with properties for TableView.
     */
    public static class Fan {
        private final int id;
        private final String serial;
        private final String model;
        private final String status;
        private final String lastService;
        private boolean selected; // For the checkbox

        public Fan(int id, String serial, String model, String status, String lastService, boolean selected) {
            this.id = id;
            this.serial = serial;
            this.model = model;
            this.status = status;
            this.lastService = lastService;
            this.selected = selected;
        }

        // Getters
        public int getId() { return id; }
        public String getSerial() { return serial; }
        public String getModel() { return model; }
        public String getStatus() { return status; }
        public String getLastService() { return lastService; }
        public boolean isSelected() { return selected; }

        // Setter for selected property (important for TableView checkbox)
        public void setSelected(boolean selected) { this.selected = selected; }

        @Override
        public String toString() {
            return "Fan{" +
                   "id=" + id +
                   ", serial='" + serial + '\'' +
                   ", model='" + model + '\'' +
                   ", status='" + status + '\'' +
                   ", lastService='" + lastService + '\'' +
                   ", selected=" + selected +
                   '}';
        }
    }
}
