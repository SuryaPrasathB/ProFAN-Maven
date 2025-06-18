package com.tasnetwork.calibration.energymeter.project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane; // For the config dialog layout
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.concurrent.Worker;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality; // For dialog window
import javafx.stage.Stage;    // For dialog window
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.geometry.Insets; // For dialog layout padding

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;    // For reading JSON
import java.io.FileWriter;    // For writing JSON
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Apache POI imports for Excel manipulation
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

// Reflection imports for dynamic property access
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

// Gson imports for JSON serialization/deserialization
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.spring.orm.model.Result;

/**
 * Controller class for the Report Generator UI.
 * Handles interactions and data display for template selection, fan search, and report generation.
 */
public class ReportGeneratorController implements Initializable {

    // FXML UI elements
    @FXML private ListView<String> templateListView;
    @FXML private WebView templatePreviewWebView;
    @FXML private Label noPreviewLabel;
    @FXML private TextField serialInputTextField;
    @FXML private TableView<Result> fanTableView;
    @FXML private TableColumn<Result, Boolean> selectColumn;
    @FXML private TableColumn<Result, String> serialColumn;
    @FXML private TableColumn<Result, String> testpointNameColumn;
    @FXML private TableColumn<Result, String> voltageColumn;
    @FXML private TableColumn<Result, String> rpmColumn;
    @FXML private TableColumn<Result, String> windspeedColumn;
    @FXML private TableColumn<Result, String> currentColumn;
    @FXML private TableColumn<Result, String> wattsColumn;
    @FXML private TableColumn<Result, String> vaColumn;
    @FXML private TableColumn<Result, String> pfColumn;
    @FXML private TableColumn<Result, String> vibrationColumn;
    @FXML private TableColumn<Result, String> statusColumn;
    @FXML private CheckBox selectAllCheckbox;
    @FXML private Button generateReportButton;
    @FXML private Label errorMessageLabel;
    @FXML private Label noResultsLabel;


    // The path to your report templates folder
    private static final String TEMPLATES_DIR_PATH = "D:\\tasworkspace\\ProFAN\\ProFAN-Maven-s0.0.0.7\\src\\main\\resources\\reportTemplates";

    private ObservableList<Template> availableTemplates = FXCollections.observableArrayList();
    private Template selectedTemplate = null;
    
    // Fetch all results from the service once at startup.
    // In a large-scale application, you would typically fetch data
    // on-demand based on search criteria rather than loading all at once.
    List<Result> allResults = DeviceDataManagerController.getResultService().findall();

    // List of Result properties that can be mapped to Excel (excluding 'id' and 'selected')
    // This list will define the fields shown in the configuration dialog.
    private static final List<String> RESULT_PROPERTIES_TO_MAP = Arrays.asList(
        "fanSerialNumber", "testPointName", "rpm", "windSpeed",
        "vibration", "current", "watts", "va", "powerFactor", "testStatus"
    );

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create(); // Gson for JSON operations

    /**
     * Initializes the controller after its root element has been completely processed.
     * Sets up initial UI states and listeners.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load templates from the specified directory
        availableTemplates = loadTemplatesFromDirectory();
        templateListView.setItems(availableTemplates.stream().map(Template::getName).collect(Collectors.toCollection(FXCollections::observableArrayList)));

        // --- WEBVIEW DEBUGGING SETUP (Optional, but good for troubleshooting) ---
        WebEngine engine = templatePreviewWebView.getEngine();
        engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                System.out.println("WebView finished loading (SUCCEEDED). Current URL: " + engine.getLocation());
            } else if (newState == Worker.State.FAILED) {
                System.err.println("WebView failed to load URL: " + engine.getLocation());
                System.err.println("Error: " + engine.getLoadWorker().getException());
            }
        });

        engine.setOnAlert(event -> System.out.println("WebView Alert: " + event.getData()));
        engine.setOnStatusChanged(event -> System.out.println("WebView Status: " + event.getData()));
        // --- END WEBVIEW DEBUGGING SETUP ---

        // Listener for template selection (single click to select, double click to configure)
        templateListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedTemplate = availableTemplates.stream()
                        .filter(t -> t.getName().equals(newVal))
                        .findFirst()
                        .orElse(null);
                
                // Update preview on single selection
                updateTemplatePreview(selectedTemplate);
            } else {
                selectedTemplate = null;
                updateTemplatePreview(null); // Clear preview
            }
        });

        // Handle double-click to open configuration dialog
        templateListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && selectedTemplate != null) {
                openTemplateConfigurationDialog(selectedTemplate);
            }
        });


        // Setup Result TableView columns
        selectColumn		.setCellValueFactory(new PropertyValueFactory<>("selected"));
        serialColumn		.setCellValueFactory(new PropertyValueFactory<>("fanSerialNumber"));
        testpointNameColumn .setCellValueFactory(new PropertyValueFactory<>("testPointName"));
        voltageColumn		.setCellValueFactory(new PropertyValueFactory<>("voltage"));
        rpmColumn           .setCellValueFactory(new PropertyValueFactory<>("rpm"));  
        windspeedColumn     .setCellValueFactory(new PropertyValueFactory<>("windSpeed"));
        currentColumn       .setCellValueFactory(new PropertyValueFactory<>("current"));  
        wattsColumn         .setCellValueFactory(new PropertyValueFactory<>("watts"));
        vaColumn            .setCellValueFactory(new PropertyValueFactory<>("va"));  
        pfColumn            .setCellValueFactory(new PropertyValueFactory<>("powerFactor"));
        vibrationColumn     .setCellValueFactory(new PropertyValueFactory<>("vibration"));
        statusColumn		.setCellValueFactory(new PropertyValueFactory<>("testStatus"));

        // Custom cell factory for the select checkbox column
        selectColumn.setCellFactory(new Callback<TableColumn<Result, Boolean>, TableCell<Result, Boolean>>() {
            @Override
            public TableCell<Result, Boolean> call(TableColumn<Result, Boolean> param) {
                return new TableCell<Result, Boolean>() {
                    private final CheckBox checkBox = new CheckBox();
                    {
                        checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                            Result fan = (Result) getTableRow().getItem();
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

        // Custom cell factory for the status column to color the text
        statusColumn.setCellFactory(new Callback<TableColumn<Result, String>, TableCell<Result, String>>() {
            @Override
            public TableCell<Result, String> call(TableColumn<Result, String> param) {
                return new TableCell<Result, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                            setTextFill(Color.BLACK); // Reset to default color
                        } else {
                            setText(item);
                            if (item.equalsIgnoreCase("PASSED")) {
                                setTextFill(Color.GREEN);
                            } else if (item.equalsIgnoreCase("FAILED")) {
                                setTextFill(Color.RED);
                            } else {
                                setTextFill(Color.BLACK); // Default color for other statuses
                            }
                        }
                    }
                };
            }
        });

        // Set initial fan data by converting the List to an ObservableList
        fanTableView.setItems(FXCollections.observableArrayList(allResults));
        updateTableViewVisibility();

        // Add listener to selectAllCheckbox
        selectAllCheckbox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            for (Result fan : fanTableView.getItems()) {
                fan.setSelected(newVal);
            }
            fanTableView.refresh(); // Refresh table to update checkboxes and status colors
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
     * Updates the WebView to show the preview of the selected template.
     * @param template The template whose preview to show, or null to clear.
     */
    private void updateTemplatePreview(Template template) {
        WebEngine engine = templatePreviewWebView.getEngine();
        if (template != null) {
            File imageFile = template.getImageFile();
            if (imageFile != null && imageFile.exists() && imageFile.isFile()) {
                try {
                    engine.load(imageFile.toURI().toURL().toExternalForm());
                    templatePreviewWebView.setVisible(true);
                    noPreviewLabel.setVisible(false);
                    System.out.println("Attempting to load JPG: " + imageFile.toURI().toURL().toExternalForm());
                } catch (Exception e) {
                    System.err.println("Failed to load JPG preview for: " + template.getName() + " - " + e.getMessage());
                    engine.loadContent("<h1>Error</h1><p>Could not load image preview. Error: " + escapeHtml(e.getMessage()) + "</p>");
                    templatePreviewWebView.setVisible(true);
                    noPreviewLabel.setVisible(false);
                }
            } else {
                engine.loadContent("");
                templatePreviewWebView.setVisible(false);
                noPreviewLabel.setText("No JPG preview available for " + template.getName() + ".");
                noPreviewLabel.setVisible(true);
                System.out.println("No JPG file found for: " + template.getName() + " at " + (imageFile != null ? imageFile.getAbsolutePath() : "null path"));
            }
        } else {
            engine.loadContent("");
            templatePreviewWebView.setVisible(false);
            noPreviewLabel.setText("Select a template to see its preview.");
            noPreviewLabel.setVisible(true);
        }
    }


    /**
     * Scans the predefined directory for .xlsx files and checks for corresponding .jpg files.
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

        // Filter for ONLY .xlsx files for listing
        File[] xlsxFiles = templateDir.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".xlsx"));

        if (xlsxFiles != null) {
            for (File xlsxFile : xlsxFiles) {
                // Determine the corresponding JPG file name
                String baseName = getFileNameWithoutExtension(xlsxFile.getName());
                File imageFile = new File(templateDir, baseName + ".jpg"); // Check for JPG
                templates.add(new Template(xlsxFile.getName(), xlsxFile, imageFile));
            }
        } else {
            System.err.println("Could not list files in directory: " + TEMPLATES_DIR_PATH);
            errorMessageLabel.setText("Error: Could not access template files.");
        }
        return templates;
    }

    /**
     * Helper method to get file name without extension.
     */
    private String getFileNameWithoutExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            return fileName.substring(0, dotIndex);
        }
        return fileName;
    }

    /**
     * Helper method to escape HTML special characters.
     * Used to ensure error messages are safely displayed in WebView.
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
     * Inner class representing a parsed search query for fan serial numbers.
     * Can be either an exact serial number or a range with a common prefix.
     */
    public static class SearchQuery {
        private String exactSerialNumber; // For single searches like "SN-001"
        private String prefix;            // For range searches, e.g., "SN-"
        private int startNumeric;         // Start of numeric range, e.g., 1
        private int endNumeric;           // End of numeric range, e.g., 100

        /**
         * Constructor for an exact serial number match.
         * @param exactSerialNumber The exact serial number to match (case-insensitive).
         */
        public SearchQuery(String exactSerialNumber) {
            this.exactSerialNumber = exactSerialNumber;
        }

        /**
         * Constructor for a serial number range match.
         * @param prefix The common alphanumeric prefix (e.g., "SN-", "LSCS-").
         * @param startNumeric The starting numeric value in the range.
         * @param endNumeric The ending numeric value in the range.
         */
        public SearchQuery(String prefix, int startNumeric, int endNumeric) {
            this.prefix = prefix;
            this.startNumeric = startNumeric;
            this.endNumeric = endNumeric;
        }

        /**
         * Checks if this query represents a range search.
         * @return true if it's a range search, false otherwise (meaning it's an exact match).
         */
        public boolean isRange() {
            return prefix != null;
        }

        /**
         * Determines if a given fan serial number matches this search query.
         * For range queries, it checks if the prefix matches and the numeric part falls within the range.
         * For exact queries, it performs a case-insensitive exact match.
         * @param fanSerialNumber The serial number of a fan to check.
         * @return true if the fan serial number matches the query, false otherwise.
         */
        public boolean matches(String fanSerialNumber) {
            if (fanSerialNumber == null) {
                return false;
            }

            if (isRange()) {
                String lowerFanSerial = fanSerialNumber.toLowerCase();
                String lowerPrefix = this.prefix.toLowerCase();

                // Check if the fan's serial number starts with the range prefix
                if (!lowerFanSerial.startsWith(lowerPrefix)) {
                    return false;
                }

                try {
                    // Extract the numeric part of the fan's serial number
                    String fanNumericPartStr = fanSerialNumber.substring(this.prefix.length());
                    int fanNumericPart = Integer.parseInt(fanNumericPartStr);

                    // Check if the numeric part is within the specified range
                    return fanNumericPart >= this.startNumeric && fanNumericPart <= this.endNumeric;
                } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                    // If parsing fails (e.g., non-numeric suffix, prefix mismatch length), it doesn't match
                    return false;
                }
            } else {
                // Exact match (case-insensitive)
                return fanSerialNumber.equalsIgnoreCase(exactSerialNumber);
            }
        }
    }

    /**
     * Parses the input string from the serial input field into a list of SearchQuery objects.
     * Supports single serial numbers (e.g., "SN-001") and alphanumeric ranges (e.g., "SN-001-SN-100", "SN-001-100").
     * Sets an error message if parsing fails.
     * @param input The raw input string from the search field.
     * @returns A List of parsed SearchQuery objects.
     */
    private List<SearchQuery> parseSerialQueries(String input) {
        List<SearchQuery> queries = new ArrayList<>();
        errorMessageLabel.setText(""); // Clear previous errors
        String[] parts = input.split(",");
        
        // Regex to split a serial number into its alphanumeric prefix and numeric suffix
        // Group 1: prefix (e.g., "SN-", "LSCS-") - non-digit characters at the start, including spaces and hyphens
        // Group 2: numeric part (e.g., "001", "1273") - one or more digits at the end
        Pattern serialPattern = Pattern.compile("^([^0-9]*)([0-9]+)$");

        for (String part : parts) {
            String trimmedPart = part.trim();
            if (trimmedPart.isEmpty()) continue;

            if (trimmedPart.contains("-")) {
                // This is a potential range query
                String[] rangeParts = trimmedPart.split("-");
                // A more robust split for ranges: check if there's exactly one hyphen for a simple "start-end"
                // or if it's a more complex serial that happens to have hyphens in the prefix.
                // For "SN-001-SN-100" or "SN-001-100", simple split on first hyphen is okay if it produces 2 parts.
                
                int firstHyphenIndex = trimmedPart.indexOf('-');
                if (firstHyphenIndex == -1 || trimmedPart.lastIndexOf('-') == firstHyphenIndex) {
                    // Only one hyphen, or no hyphen (handled by 'else' branch below)
                    // If one hyphen, it's a simple X-Y or PREFIXNUM-NUM
                    rangeParts = trimmedPart.split("-");
                } else {
                    // Multiple hyphens (e.g., "LSCS-123-LSCS-145") or "SN-A-10-SN-B-20"
                    // We need to be careful to parse only the main range.
                    // Assuming "START_SERIAL-END_SERIAL" where START_SERIAL and END_SERIAL can contain hyphens,
                    // but the *main* range separator is the first or last hyphen if there are multiple.
                    // For now, let's stick to simple "PART1-PART2" structure and improve if needed for more complex cases.
                    // A simple split with limit 2 ensures we get two parts if multiple hyphens are present
                    rangeParts = trimmedPart.split("-", 2); // Split only on the first hyphen
                }


                if (rangeParts.length != 2) {
                    errorMessageLabel.setText(String.format("Invalid range format: '%s'. Expected 'START-END'.", trimmedPart));
                    return new ArrayList<>(); // Return empty list on error
                }

                String startStr = rangeParts[0].trim();
                String endStr = rangeParts[1].trim();

                // Parse the start part of the range
                Matcher startMatcher = serialPattern.matcher(startStr);
                if (!startMatcher.matches()) {
                    errorMessageLabel.setText(String.format("Invalid start serial format in range: '%s'. Expected 'PREFIXNUMBER'.", startStr));
                    return new ArrayList<>();
                }
                String startPrefix = startMatcher.group(1);
                int startNumeric;
                try {
                    startNumeric = Integer.parseInt(startMatcher.group(2));
                } catch (NumberFormatException e) {
                     errorMessageLabel.setText(String.format("Invalid numeric part in start serial: '%s'.", startStr));
                     return new ArrayList<>();
                }

                // Parse the end part of the range
                Matcher endMatcher = serialPattern.matcher(endStr);
                String commonPrefix;
                int endNumeric;

                if (endMatcher.matches()) {
                    // The end part also has a prefix and numeric part (e.g., "SN-100")
                    String endPrefix = endMatcher.group(1);
                    try {
                        endNumeric = Integer.parseInt(endMatcher.group(2));
                    } catch (NumberFormatException e) {
                         errorMessageLabel.setText(String.format("Invalid numeric part in end serial: '%s'.", endStr));
                         return new ArrayList<>();
                    }
                    // Crucial: prefixes must match for a valid alphanumeric range
                    if (!startPrefix.equalsIgnoreCase(endPrefix)) {
                        errorMessageLabel.setText(String.format("Prefix mismatch in range: '%s' and '%s'. Prefixes must be the same.", startStr, endStr));
                        return new ArrayList<>();
                    }
                    commonPrefix = startPrefix; // Use the matched prefix
                } else {
                    // The end part is just a numeric value (e.g., "100" in "SN-001-100")
                    try {
                        endNumeric = Integer.parseInt(endStr);
                        commonPrefix = startPrefix; // Assume the same prefix as the start of the range
                    } catch (NumberFormatException e) {
                        errorMessageLabel.setText(String.format("Invalid end serial format in range: '%s'. Expected 'NUMBER' or 'PREFIXNUMBER'.", endStr));
                        return new ArrayList<>();
                    }
                }

                // Ensure startNumeric is not greater than endNumeric, swap if necessary
                if (startNumeric > endNumeric) {
                    int temp = startNumeric;
                    startNumeric = endNumeric;
                    endNumeric = temp;
                }
                queries.add(new SearchQuery(commonPrefix, startNumeric, endNumeric));

            } else {
                // This is a single serial number for exact matching
                queries.add(new SearchQuery(trimmedPart));
            }
        }
        return queries;
    }

    /**
     * Handles the search button click event.
     * Parses the serial input and filters the fan data based on fanSerialNumber.
     */
    @FXML
    private void handleSearchFans() {
        errorMessageLabel.setText(""); // Clear previous error messages
        String input = serialInputTextField.getText().trim();

        if (input.isEmpty()) {
            // If input is empty, display all results fetched initially.
            fanTableView.setItems(FXCollections.observableArrayList(allResults)); 
            updateTableViewVisibility();
            updateSelectAllCheckboxState();
            updateGenerateReportButtonState();
            return;
        }

        List<SearchQuery> searchQueries = parseSerialQueries(input);

        // If there was an error during parsing, searchQueries will be empty
        // and errorMessageLabel would have been set. In this case, clear table.
        if (searchQueries.isEmpty() && !errorMessageLabel.getText().isEmpty()) {
             fanTableView.setItems(FXCollections.emptyObservableList());
             updateTableViewVisibility();
             updateSelectAllCheckboxState();
             updateGenerateReportButtonState();
             return;
        }

        // --- IMPORTANT NOTE ON DATABASE FETCHING ---
        // In a real-world scenario, to "fetch from the database and display" each time
        // a search is performed, you would typically call a service method that
        // queries the database directly with the search terms/ranges.
        // For example, if your ResultService had methods like:
        // List<Result> findByFanSerialNumberExact(String serial);
        // List<Result> findByFanSerialNumberRange(String prefix, int startNum, int endNum);
        //
        // You would dynamically build your database query here based on the 'searchQueries' list.
        // For this example, we filter the 'allResults' list already loaded in memory,
        // which simulates the filtering but operates on in-memory data.
        // ------------------------------------------

        ObservableList<Result> filteredFans = FXCollections.observableArrayList(
                allResults.stream() // Filtering the already loaded 'allResults' for demonstration
                    .filter(fan -> {
                        // A fan matches if its serial number matches ANY of the parsed search queries
                        return searchQueries.stream()
                                .anyMatch(query -> query.matches(fan.getFanSerialNumber()));
                    })
                    .collect(Collectors.toList())
        );

        fanTableView.setItems(filteredFans);
        fanTableView.refresh(); // Refresh table to ensure colors are applied on filtered data
        updateTableViewVisibility();
        updateSelectAllCheckboxState(); // Update select all checkbox based on filtered results
        updateGenerateReportButtonState(); // Update generate button state
    }

    /**
     * Handles the generate report button click event.
     * Collects selected fan IDs and the chosen template, then simulates report generation.
     */
    @FXML
    private void handleGenerateReport() {
        List<Result> fansToReport = fanTableView.getItems().stream()
                .filter(Result::isSelected)
                .collect(Collectors.toList());

        if (fansToReport.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Fans Selected", "Please select at least one fan to generate a report.");
            return;
        }

        if (selectedTemplate == null) {
            showAlert(Alert.AlertType.WARNING, "No Template Selected", "Please select an Excel template to generate the report.");
            return;
        }

        try {
            generateExcelReport(selectedTemplate, fansToReport);
            showAlert(Alert.AlertType.INFORMATION, "Report Generation Complete",
                    "Report successfully generated and saved to your chosen location.");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Report Generation Failed",
                    "Failed to generate report due to an I/O error: " + e.getMessage());
            System.err.println("Error generating report: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Report Generation Failed",
                    "An unexpected error occurred during report generation: " + e.getMessage());
            System.err.println("Unexpected error during report generation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Opens a dialog for the user to configure the Excel mapping for the selected template.
     * @param template The template to configure.
     */
    private void openTemplateConfigurationDialog(Template template) {
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Configure Template: " + template.getName());
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(templateListView.getScene().getWindow());

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(5);

        Map<String, TextField> propertyTextFields = new LinkedHashMap<>(); // To maintain order and access fields

        int rowIdx = 0;
        // Dynamically create input fields for each mappable Result property
        for (String propertyName : RESULT_PROPERTIES_TO_MAP) {
            Label label = new Label(camelCaseToTitleCase(propertyName) + " Cell Range:");
            TextField textField = new TextField();
            textField.setPromptText("e.g., A2:A10 or C5");
            propertyTextFields.put(propertyName, textField);
            grid.addRow(rowIdx++, label, textField);
        }

        Button loadButton = new Button("Load Configuration");
        Button saveButton = new Button("Save Configuration");
        Label statusLabel = new Label();
        statusLabel.setWrapText(true); // Allow text to wrap

        grid.addRow(rowIdx++, loadButton, saveButton);
        GridPane.setColumnSpan(statusLabel, 2); // Span across two columns
        grid.addRow(rowIdx++, statusLabel);

        // --- Load Configuration Logic ---
        File configFile = getConfigFile(template);
        loadButton.setOnAction(event -> {
            if (configFile.exists()) {
                try (FileReader reader = new FileReader(configFile)) {
                    TemplateConfig config = GSON.fromJson(reader, TemplateConfig.class);
                    if (config != null && config.getPropertyToCellRange() != null) {
                        for (Map.Entry<String, String> entry : config.getPropertyToCellRange().entrySet()) {
                            TextField tf = propertyTextFields.get(entry.getKey());
                            if (tf != null) {
                                tf.setText(entry.getValue());
                            }
                        }
                        statusLabel.setText("Configuration loaded successfully from " + configFile.getName());
                        statusLabel.setTextFill(Color.GREEN);
                    } else {
                        statusLabel.setText("Configuration file empty or invalid.");
                        statusLabel.setTextFill(Color.ORANGE);
                    }
                } catch (IOException | JsonSyntaxException e) {
                    statusLabel.setText("Error loading configuration: " + e.getMessage());
                    statusLabel.setTextFill(Color.RED);
                    System.err.println("Error loading config for " + template.getName() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                statusLabel.setText("No existing configuration found for this template.");
                statusLabel.setTextFill(Color.BLUE);
            }
        });
        // Automatically load on dialog open if config exists
        loadButton.fire(); // Simulate a click to load on opening

        // --- Save Configuration Logic ---
        saveButton.setOnAction(event -> {
            TemplateConfig config = new TemplateConfig();
            Map<String, String> newMapping = new HashMap<>();
            boolean hasInvalidInput = false;
            for (Map.Entry<String, TextField> entry : propertyTextFields.entrySet()) {
                String propertyName = entry.getKey();
                String cellRange = entry.getValue().getText().trim();
                if (!cellRange.isEmpty()) {
                    // Basic validation: ensure it looks like a cell reference
                    // Updated regex to allow single cell (e.g., A1) or range (e.g., A1:B2)
                    if (!Pattern.matches("^[A-Z]+[0-9]+(:[A-Z]+[0-9]+)?$", cellRange.toUpperCase())) {
                        statusLabel.setText("Invalid cell range format for " + camelCaseToTitleCase(propertyName) + ": " + cellRange);
                        statusLabel.setTextFill(Color.RED);
                        hasInvalidInput = true;
                        break;
                    }
                    newMapping.put(propertyName, cellRange);
                }
            }

            if (hasInvalidInput) {
                return; // Stop saving if input is invalid
            }

            if (newMapping.isEmpty()) {
                statusLabel.setText("No mappings entered. Configuration will be empty.");
                statusLabel.setTextFill(Color.ORANGE);
            }

            config.setPropertyToCellRange(newMapping);

            try (FileWriter writer = new FileWriter(configFile)) {
                GSON.toJson(config, writer);
                statusLabel.setText("Configuration saved successfully to " + configFile.getName());
                statusLabel.setTextFill(Color.GREEN);
            } catch (IOException e) {
                statusLabel.setText("Error saving configuration: " + e.getMessage());
                statusLabel.setTextFill(Color.RED);
                System.err.println("Error saving config for " + template.getName() + ": " + e.getMessage());
                e.printStackTrace();
            }
        });

        // Set the scene and show the dialog
        dialogStage.setScene(new javafx.scene.Scene(grid));
        dialogStage.showAndWait();
    }

    /**
     * Helper to get the JSON configuration file for a given template.
     */
    private File getConfigFile(Template template) {
        String baseName = getFileNameWithoutExtension(template.getName());
        return new File(TEMPLATES_DIR_PATH, baseName + ".json");
    }

    /**
     * Converts camelCase string to Title Case with spaces.
     * e.g., "fanSerialNumber" -> "Fan Serial Number"
     */
    private String camelCaseToTitleCase(String camelCaseString) {
        if (camelCaseString == null || camelCaseString.isEmpty()) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        result.append(Character.toUpperCase(camelCaseString.charAt(0)));
        for (int i = 1; i < camelCaseString.length(); i++) {
            char c = camelCaseString.charAt(i);
            if (Character.isUpperCase(c)) {
                result.append(" ");
            }
            result.append(c);
        }
        return result.toString();
    }


    /**
     * Generates an Excel report using the selected template and fan data.
     * This method dynamically identifies columns based on headers in the template
     * and populates them with corresponding data from the Result objects.
     *
     * @param template The selected report template containing the XLSX file.
     * @param fansToReport A list of Result objects to be included in the report.
     * @throws IOException If there's an error reading the template or writing the report.
     * @throws NoSuchMethodException If a getter method for a property cannot be found via reflection.
     * @throws IllegalAccessException If access to a getter method is denied.
     * @throws InvocationTargetException If an exception occurs during getter method invocation.
     */
    private void generateExcelReport(Template template, List<Result> fansToReport)
            throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        File configFile = getConfigFile(template);
        if (!configFile.exists()) {
            showAlert(Alert.AlertType.ERROR, "Configuration Missing",
                    "Configuration file not found for template '" + template.getName() + "'. Please double-click the template to configure cell mappings.");
            return; // Exit method
        }

        TemplateConfig config;
        try (FileReader reader = new FileReader(configFile)) {
            config = GSON.fromJson(reader, TemplateConfig.class);
            if (config == null || config.getPropertyToCellRange() == null || config.getPropertyToCellRange().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Configuration Empty/Invalid",
                        "Configuration for template '" + template.getName() + "' is empty or invalid. Please configure cell mappings.");
                return; // Exit method
            }
        } catch (JsonSyntaxException e) {
            showAlert(Alert.AlertType.ERROR, "Configuration Error",
                    "Error parsing configuration for template '" + template.getName() + "'. It might be malformed JSON. Error: " + e.getMessage());
            System.err.println("Error parsing config for " + template.getName() + ": " + e.getMessage());
            e.printStackTrace();
            return; // Exit method
        }


        // Use try-with-resources to ensure workbook and file streams are closed
        try (FileInputStream fis = new FileInputStream(template.getXlsxFile());
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // Get the first sheet of the workbook

            // Prepare a map to keep track of the current row index for each configured column
            // This ensures data is appended correctly if multiple properties map to the same column.
            Map<Integer, Integer> currentRowIndexForColumn = new HashMap<>();
            
            // Initialize the current row index for each configured column based on its start row
            for (Map.Entry<String, String> entry : config.getPropertyToCellRange().entrySet()) {
                String cellRangeStr = entry.getValue();
                int startColIdx = TemplateConfig.getColumnIndexFromCellRange(cellRangeStr);
                int startRowIdx = TemplateConfig.getStartRowIndexFromCellRange(cellRangeStr);
                
                if (startColIdx != -1 && startRowIdx != -1) {
                    currentRowIndexForColumn.put(startColIdx, startRowIdx);
                }
            }


            // 1. Populate data rows based on configuration
            for (Result fan : fansToReport) {
                for (Map.Entry<String, String> mapping : config.getPropertyToCellRange().entrySet()) {
                    String resultPropertyName = mapping.getKey(); // e.g., "fanSerialNumber"
                    String cellRangeStr = mapping.getValue();     // e.g., "A2:A10"

                    // Get value from Result object using reflection
                    Object value = null;
                    try {
                        String getterMethodName = "get" + resultPropertyName.substring(0, 1).toUpperCase() + resultPropertyName.substring(1);
                        Method getter = Result.class.getMethod(getterMethodName);
                        value = getter.invoke(fan);
                    } catch (NoSuchMethodException e) {
                        System.err.println("Warning: Getter method not found for property '" + resultPropertyName + "'. Skipping this data for fan " + fan.getFanSerialNumber());
                        continue; // Skip this property if getter not found
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        System.err.println("Error invoking getter for property '" + resultPropertyName + "': " + e.getMessage());
                        // Consider throwing this as a higher-level error or logging
                        continue;
                    }

                    // Parse cell range and place value
                    int targetColIdx = TemplateConfig.getColumnIndexFromCellRange(cellRangeStr);
                    int startRowIdx = TemplateConfig.getStartRowIndexFromCellRange(cellRangeStr);
                    int endRowIdx = TemplateConfig.getEndRowIndexFromCellRange(cellRangeStr);

                    if (targetColIdx == -1 || startRowIdx == -1 || endRowIdx == -1) {
                        System.err.println("Invalid cell range for property '" + resultPropertyName + "': " + cellRangeStr + ". Skipping.");
                        continue; // Skip if cell range is invalid
                    }

                    // Get the current row index for this specific column
                    int currentRowForThisColumn = currentRowIndexForColumn.getOrDefault(targetColIdx, startRowIdx);

                    if (currentRowForThisColumn <= endRowIdx) {
                        Row dataRow = sheet.getRow(currentRowForThisColumn);
                        if (dataRow == null) {
                            dataRow = sheet.createRow(currentRowForThisColumn);
                        }
                        Cell dataCell = dataRow.createCell(targetColIdx);
                        if (value != null) {
                            if (value instanceof String) {
                                dataCell.setCellValue((String) value);
                            } else if (value instanceof Number) {
                                dataCell.setCellValue(((Number) value).doubleValue());
                            } else if (value instanceof Boolean) {
                                dataCell.setCellValue((Boolean) value);
                            } else {
                                dataCell.setCellValue(value.toString());
                            }
                        } else {
                            dataCell.setCellValue("");
                        }
                        // Increment the current row index for this column for the next fan
                        currentRowIndexForColumn.put(targetColIdx, currentRowForThisColumn + 1);
                    } else {
                        System.err.println("Warning: No more space in configured range '" + cellRangeStr + "' for property '" + resultPropertyName + "'. Data for fan " + fan.getFanSerialNumber() + " will not fit.");
                    }
                }
            }

            // 3. Prompt user to save the new file
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Report As");
            fileChooser.setInitialFileName("Report_" + template.getName().replace(".xlsx", "") + "_" + System.currentTimeMillis() + ".xlsx");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Workbook", "*.xlsx"));

            Window stage = generateReportButton.getScene().getWindow();
            File outputFile = fileChooser.showSaveDialog(stage);

            if (outputFile != null) {
                try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                    workbook.write(fos);
                }
            } else {
                throw new IOException("Report save cancelled by user.");
            }

        } // End of try-with-resources
    }

    /**
     * Scans the specified header row of an Excel sheet to create a mapping
     * from Result property names to Excel column indices.
     *
     * @param sheet The Excel sheet to scan.
     * @param headerRowIndex The 0-indexed row number where headers are located.
     * @return A Map where keys are Result property names (e.g., "fanSerialNumber")
     * and values are their corresponding 0-indexed Excel column numbers.
     * @deprecated No longer used for data population; mapping is now configured via dialog.
     */
    @Deprecated // This method is no longer used for data population as mapping is user-defined
    private Map<String, Integer> findColumnMappings(Sheet sheet, int headerRowIndex) {
        // This method is now deprecated as the mapping is user-configured.
        // It's kept for reference in case any other part of the code relies on it,
        // but it will not be called for generateExcelReport.
        return new HashMap<>(); // Return empty map as it's not used
    }


    /**
     * Updates the enabled state and text of the generate report button
     * based on the number of selected fans.
     */
    private void updateGenerateReportButtonState() {
        long selectedCount = fanTableView.getItems().stream().filter(Result::isSelected).count();
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
        long selectedCount = fanTableView.getItems().stream().filter(Result::isSelected).count();
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
     * Now stores the XLSX file and its corresponding image file for preview.
     */
    public static class Template {
        private final String name;     // Name of the XLSX file (e.g., "MyReport.xlsx")
        private final File xlsxFile;   // Reference to the actual XLSX file
        private final File imageFile;  // Reference to the corresponding JPG/image file for preview

        public Template(String name, File xlsxFile, File imageFile) {
            this.name = name;
            this.xlsxFile = xlsxFile;
            this.imageFile = imageFile;
        }

        public String getName() {
            return name;
        }

        public File getXlsxFile() {
            return xlsxFile;
        }

        public File getImageFile() {
            return imageFile;
        }
    }

}
