package com.tasnetwork.calibration.energymeter.project;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;    // For reading JSON
import java.io.FileWriter;    // For writing JSON
import java.io.IOException;
// Reflection imports for dynamic property access
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime; // Required for converting LocalDate to epoch
import java.time.ZoneOffset; // Required for converting LocalDate to epoch
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

// Apache POI imports for Excel manipulation
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

// Gson imports for JSON serialization/deserialization
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.spring.orm.model.Result;

import javafx.application.Platform; // For Platform.runLater
import javafx.beans.property.ReadOnlyStringWrapper;
/**
 * AUTHOR : TriftyTexas
 */
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets; // For dialog layout padding
import javafx.geometry.Pos; // For centering cells
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode; // For key event handling
import javafx.scene.input.KeyEvent; // For key event handling
import javafx.scene.layout.GridPane; // For the config dialog layout
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality; // For dialog window
import javafx.stage.Stage;    // For dialog window
import javafx.stage.Window;
import javafx.util.Callback;

/**
 * Controller class for the Report Generator UI.
 * Handles interactions and data display for template selection, fan search, and report generation.
 */
public class ReportGeneratorController_V2 implements Initializable {

    // FXML UI elements
    @FXML private ListView<String> templateListView;
    @FXML private ImageView templatePreviewImageView; // Changed from WebView
    @FXML private Label noPreviewLabel;
    @FXML private TextField serialInputTextField;
    @FXML private TableView<Result> fanTableView;
    @FXML private TableColumn<Result, Boolean> selectColumn;
    @FXML private TableColumn<Result, String> serialColumn;
    @FXML private TableColumn<Result, String> serialNoColumn;
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
    @FXML private CheckBox selectAllCheckbox; // Keeping it a CheckBox, but it behaves like a button
    @FXML private Button generateReportButton;
    @FXML private Label errorMessageLabel;
    @FXML private Label noResultsLabel;
    @FXML private ComboBox<String> filterComboBox; 

    // Date pickers for filtering
    @FXML private DatePicker fromDatePicker;
    @FXML private DatePicker toDatePicker;

    // Default template directory path (used if no config file or invalid config)
    private static String TEMPLATES_DIR_PATH = "D:\\tasworkspace\\ProFAN\\ProFAN-Maven-s0.0.0.7\\src\\main\\resources\\reportTemplates";
    // Configuration file for storing the template directory path
    private static final String TEMPLATES_CONFIG_FILE_NAME = "C:\\Users\\Surya\\git\\ProFAN-Maven\\src\\main\\resources\\config\\templates\\templates_config.json";

    private ObservableList<Template> availableTemplates = FXCollections.observableArrayList();
    private Template selectedTemplate = null;
    
    // Stores the maximum number of records that can be selected for the currently active template.
    // Defaults to Integer.MAX_VALUE (no limit) if no template is selected or config is invalid.
    private int currentTemplateMaxRecords = 0 ; // Corrected default value

    // Flag to prevent recursive calls/multiple warnings when programmatic selection occurs.
    // This flag is ONLY for individual row checkboxes. The selectAllCheckbox now uses setOnAction.
    private volatile boolean isProgrammaticSelection = false; // Use volatile for thread visibility

    // List of Result properties that can be mapped to Excel (excluding 'id' and 'selected')
    private static final List<String> RESULT_PROPERTIES_TO_MAP = Arrays.asList(
        "fanSerialNumber", "testPointName", "voltage", "rpm", "windSpeed",
        "vibration", "current", "watts", "va", "powerFactor", "testStatus"
    );

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create(); // Gson for JSON operations
 

    /**
     * Initializes the controller after its root element has been completely processed.
     * Sets up initial UI states and listeners.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load the configured template directory path and reload templates
        // Using Platform.runLater to ensure the scene is available
        Platform.runLater(() -> {
            loadConfiguredTemplatesDirPath();
            reloadTemplates();

            // Add global key event filter to the scene
            if (templateListView.getScene() != null) {
                templateListView.getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                    // Detect CTRL + SHIFT + ` (back_quote)
                    if (event.isControlDown() && event.isShiftDown() && event.getCode() == KeyCode.BACK_QUOTE) {
                        openPathConfigurationDialog(); 
                        event.consume(); // Consume the event so it doesn't propagate
                    }
                });
            } else {
                ApplicationLauncher.logger.error("ReportGeneratorController: initialize: Scene not available, cannot set global key listener.");
            }
        });

        // Listener for template selection (single click to select, double click to configure)
        templateListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            ApplicationLauncher.logger.info("ReportGeneratorController: initialize: Template selection changed to '" + newVal + "'");
            if (newVal != null) {
                selectedTemplate = availableTemplates.stream()
                        .filter(t -> t.getDisplayName().equals(newVal))
                        .findFirst()
                        .orElse(null);
                
                // Clear all existing selections when a new template is selected
                // This is crucial for correctly applying new selection limits.
                ApplicationLauncher.logger.info("ReportGeneratorController: initialize: Clearing previous selections due to new template selection.");
                fanTableView.getItems().forEach(fan -> fan.setSelected(false));
                fanTableView.refresh(); // Important to refresh to visually clear checkboxes
                updateGenerateReportButtonState();
                updateSelectAllCheckboxState(); // Update the selectAll checkbox state

                // Load config for the newly selected template to get numRecords
                currentTemplateMaxRecords = Integer.MAX_VALUE; // Default to no limit
                if (selectedTemplate != null) {
                    File configFile = getConfigFile(selectedTemplate); // This will now point to reportTemplates/json
                    if (configFile.exists()) {
                        try (FileReader reader = new FileReader(configFile)) {
                            TemplateConfig config = GSON.fromJson(reader, TemplateConfig.class);
                            if (config != null && config.getNumRecords() > 0) {
                                currentTemplateMaxRecords = config.getNumRecords();
                                selectAllCheckbox.setText("Select First " + currentTemplateMaxRecords + " Records");
                                ApplicationLauncher.logger.info("ReportGeneratorController: initialize: Loaded template config. Max records for selection: " + currentTemplateMaxRecords);
                            } else {
                                ApplicationLauncher.logger.info("ReportGeneratorController: initialize: Config for " + selectedTemplate.getName() + " is invalid or missing numRecords. No selection limit enforced.");
                                showAlert(Alert.AlertType.INFORMATION, "Template Configuration Info", 
                                          "The selected template '" + selectedTemplate.getDisplayName() + "' does not have a valid 'Number of Records' configured. No selection limit will be enforced for this template.");
                            }
                        } catch (IOException | JsonSyntaxException e) {
                            ApplicationLauncher.logger.error("ReportGeneratorController: initialize: Error loading config for " + selectedTemplate.getName() + ": " + e.getMessage());
                            showAlert(Alert.AlertType.ERROR, "Config Load Error", 
                                      "Error loading configuration for template '" + selectedTemplate.getDisplayName() + "'. No selection limit will be enforced.");
                            // currentTemplateMaxRecords remains Integer.MAX_VALUE
                        }
                    } else {
                        ApplicationLauncher.logger.info("ReportGeneratorController: initialize: No config file found for " + selectedTemplate.getName() + ". No selection limit enforced.");
                        showAlert(Alert.AlertType.INFORMATION, "Template Configuration Info", 
                                  "No configuration file found for the selected template '" + selectedTemplate.getDisplayName() + "'. No selection limit will be enforced. Double-click the template to configure.");
                    }
                }
                updateTemplatePreview(selectedTemplate);
            } else {
                ApplicationLauncher.logger.info("ReportGeneratorController: initialize: No template selected. Clearing preview and resetting max records.");
                selectedTemplate = null;
                updateTemplatePreview(null); // Clear preview
                currentTemplateMaxRecords = Integer.MAX_VALUE; // No template selected, no limit
                // Clear all selections when no template is selected
                fanTableView.getItems().forEach(fan -> fan.setSelected(false)); 
                fanTableView.refresh();
                updateGenerateReportButtonState();
                updateSelectAllCheckboxState();
            }
        });

        // Handle double-click to open configuration dialog
        templateListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && selectedTemplate != null) {
                ApplicationLauncher.logger.info("ReportGeneratorController: initialize: Double-click detected on template: " + selectedTemplate.getDisplayName());
                openTemplateConfigurationDialog(selectedTemplate);
            }
        });


        // Setup Result TableView columns
        selectColumn		.setCellValueFactory(new PropertyValueFactory<>("selected"));
        serialColumn		.setCellValueFactory(new PropertyValueFactory<>("fanSerialNumber")); // Now correctly binds to fanSerialNumber
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

        // Center the checkboxes in the selectColumn
        selectColumn.setCellFactory(new Callback<TableColumn<Result, Boolean>, TableCell<Result, Boolean>>() {
            @Override
            public TableCell<Result, Boolean> call(TableColumn<Result, Boolean> param) {
                return new TableCell<Result, Boolean>() {
                    private final CheckBox checkBox = new CheckBox();
                    private Boolean lastProcessedState = null;
                    private boolean isUpdating = false; // Guard to prevent recursive updates

                    {
                        // Set alignment for the cell containing the checkbox
                        setAlignment(Pos.CENTER);

                        checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                            if (isUpdating) {
                                ApplicationLauncher.logger.debug("ReportGeneratorController: initialize: Ignoring recursive checkbox update.");
                                return; // Prevent recursive updates
                            }
                            if (newVal.equals(lastProcessedState)) {
                                ApplicationLauncher.logger.debug("ReportGeneratorController: initialize: Ignoring redundant checkbox event for state: " + newVal);
                                return; // Skip redundant events
                            }
                            lastProcessedState = newVal;

                            Result fan = (Result) getTableRow().getItem();
                            if (fan == null) {
                                ApplicationLauncher.logger.warn("ReportGeneratorController: initialize: Checkbox listener triggered for null fan. Ignoring.");
                                return; // Prevent processing null rows
                            }
                            ApplicationLauncher.logger.info("ReportGeneratorController: initialize: Individual checkbox changed for fan: " + fan.getFanSerialNumber() + ", oldVal: " + oldVal + ", newVal: " + newVal + ", isProgrammaticSelection: " + isProgrammaticSelection);

                            if (isProgrammaticSelection) {
                                isUpdating = true;
                                try {
                                    fan.setSelected(newVal);
                                    ApplicationLauncher.logger.debug("ReportGeneratorController: initialize: Programmatic change, skipping limit check.");
                                } finally {
                                    isUpdating = false;
                                }
                                return;
                            }

                            // For manual selections, check limit considering this change
                            long currentSelectedCount = fanTableView.getItems().stream().filter(Result::isSelected).count();
                            if (newVal) { // Attempting to select
                                // Recalculate based on future state: current selected + 1 (if selecting this one)
                                if (!fan.isSelected() && newVal && currentSelectedCount >= currentTemplateMaxRecords) { // If not already selected, but trying to select and hitting limit
                                    ApplicationLauncher.logger.warn("ReportGeneratorController: initialize: Selection limit exceeded. Reverting checkbox.");
                                    isUpdating = true;
                                    try {
                                        checkBox.setSelected(false);
                                        showAlert(Alert.AlertType.WARNING, "Selection Limit Exceeded",
                                                "Number of records for selected template is '" + currentTemplateMaxRecords + "'. Cannot select more.");
                                    } finally {
                                        isUpdating = false;
                                    }
                                    return;
                                }
                            }
                            // Allow the selection/deselection
                            ApplicationLauncher.logger.info("ReportGeneratorController: initialize: Manual selection allowed. Setting fan selected: " + newVal);
                            isUpdating = true;
                            try {
                                fan.setSelected(newVal);
                                Platform.runLater(() -> {
                                    updateGenerateReportButtonState();
                                    updateSelectAllCheckboxState();
                                });
                            } finally {
                                isUpdating = false;
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Boolean item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null || getTableRow().getItem() == null) {
                            setGraphic(null);
                            lastProcessedState = null; // Reset state for empty cells
                        } else {
                            isUpdating = true;
                            try {
                                checkBox.setSelected(item);
                                setGraphic(checkBox);
                            } finally {
                                isUpdating = false;
                            }
                        }
                    }
                };
            }
        });

        serialColumn.setCellFactory(column -> new TableCell<Result, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);
                setAlignment(Pos.CENTER); 
            }
        });

        serialNoColumn.setCellValueFactory(cellData -> {
            // Get the index of the row and convert it to a string for display
            // This will ensure the serial number column is always populated sequentially (1, 2, 3...).
            // Note: getTableView().getItems().indexOf(cellData.getValue()) returns 0-based index.
            return new ReadOnlyStringWrapper(String.valueOf(cellData.getTableView().getItems().indexOf(cellData.getValue()) + 1));
        });
        serialNoColumn.setCellFactory(column -> new TableCell<Result, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);
                setAlignment(Pos.CENTER); 
            }
        });

        // Custom cell factory for rpmColumn to color based on rpmValid
        rpmColumn.setCellFactory(column -> new TableCell<Result, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow().getItem() == null) {
                    setText(null);
                    setTextFill(Color.BLACK); // Reset to default
                } else {
                    setText(item);
                    Result result = (Result) getTableRow().getItem();
                    if (result != null && !result.isRpmValid()) {
                        setTextFill(Color.RED);
                    } else {
                        setTextFill(Color.GREEN); // Default to green if valid or no item
                    }
                }
            }
        });

        // Custom cell factory for windspeedColumn to color based on windSpeedValid
        windspeedColumn.setCellFactory(column -> new TableCell<Result, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow().getItem() == null) {
                    setText(null);
                    setTextFill(Color.BLACK);
                } else {
                    setText(item);
                    Result result = (Result) getTableRow().getItem();
                    if (result != null && !result.isWindSpeedValid()) {
                        setTextFill(Color.RED);
                    } else {
                        setTextFill(Color.GREEN);
                    }
                }
            }
        });

        // Custom cell factory for vibrationColumn to color based on vibrationValid
        vibrationColumn.setCellFactory(column -> new TableCell<Result, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow().getItem() == null) {
                    setText(null);
                    setTextFill(Color.BLACK);
                } else {
                    setText(item);
                    Result result = (Result) getTableRow().getItem();
                    if (result != null && !result.isVibrationValid()) {
                        setTextFill(Color.RED);
                    } else {
                        setTextFill(Color.GREEN);
                    }
                }
            }
        });

        // Custom cell factory for currentColumn to color based on currentValid
        currentColumn.setCellFactory(column -> new TableCell<Result, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow().getItem() == null) {
                    setText(null);
                    setTextFill(Color.BLACK);
                } else {
                    setText(item);
                    Result result = (Result) getTableRow().getItem();
                    if (result != null && !result.isCurrentValid()) {
                        setTextFill(Color.RED);
                    } else {
                        setTextFill(Color.GREEN);
                    }
                }
            }
        });

        // Custom cell factory for wattsColumn to color based on wattsValid
        wattsColumn.setCellFactory(column -> new TableCell<Result, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow().getItem() == null) {
                    setText(null);
                    setTextFill(Color.BLACK);
                } else {
                    setText(item);
                    Result result = (Result) getTableRow().getItem();
                    if (result != null && !result.isWattsValid()) {
                        setTextFill(Color.RED);
                    } else {
                        setTextFill(Color.GREEN);
                    }
                }
            }
        });

        // Custom cell factory for vaColumn to color based on activePowerValid
        vaColumn.setCellFactory(column -> new TableCell<Result, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow().getItem() == null) {
                    setText(null);
                    setTextFill(Color.BLACK);
                } else {
                    setText(item);
                    Result result = (Result) getTableRow().getItem();
                    if (result != null && !result.isActivePowerValid()) { // Assuming activePowerValid for VA
                        setTextFill(Color.RED);
                    } else {
                        setTextFill(Color.GREEN);
                    }
                }
            }
        });

        // Custom cell factory for pfColumn to color based on powerFactorValid
        pfColumn.setCellFactory(column -> new TableCell<Result, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow().getItem() == null) {
                    setText(null);
                    setTextFill(Color.BLACK);
                } else {
                    setText(item);
                    Result result = (Result) getTableRow().getItem();
                    if (result != null && !result.isPowerFactorValid()) {
                        setTextFill(Color.RED);
                    } else {
                        setTextFill(Color.GREEN);
                    }
                }
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

        // Initialize filter ComboBox
        filterComboBox.setItems(FXCollections.observableArrayList("NONE", "PASSED", "FAILED"));
        filterComboBox.getSelectionModel().select("NONE"); // Set default filter
        // Add listener to filter ComboBox
        filterComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            ApplicationLauncher.logger.info("ReportGeneratorController: initialize: Filter ComboBox changed to '" + newVal + "'");
            applyStatusFilter(newVal);
        });

        // IMPORTANT: Use setOnAction for the selectAllCheckbox to treat it as a button
        // This avoids the recursive listener issue with selectedProperty().
        selectAllCheckbox.setOnAction(event -> handleSelectAllAction());

        // Add action listener to serialInputTextField to trigger search on Enter key press
        serialInputTextField.setOnAction(event -> {
            ApplicationLauncher.logger.info("ReportGeneratorController: initialize: Enter key pressed in serial input field. Triggering search.");
            handleSearchFans();
        });

        // Add listeners to DatePickers to trigger search when value changes
        if (fromDatePicker != null) {
            fromDatePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
                ApplicationLauncher.logger.info("ReportGeneratorController: initialize: From DatePicker changed to: " + newDate);
                handleSearchFans();
            });
        }
        if (toDatePicker != null) {
            toDatePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
                ApplicationLauncher.logger.info("ReportGeneratorController: initialize: To DatePicker changed to: " + newDate);
                handleSearchFans();
            });
        }


        // Initialize button state
        updateGenerateReportButtonState();
        errorMessageLabel.setText(""); // Clear any initial error message

        // Initial state for preview area
        templatePreviewImageView.setVisible(false);
        noPreviewLabel.setVisible(true);
        noPreviewLabel.setText("Select a template to see its preview.");
    }

    /**
     * Handles the action for the "Select All" checkbox, behaving like a toggle button.
     * Selects the first 'N' records if none or some are selected, or clears all if fully selected.
     */
    private void handleSelectAllAction() {
        ApplicationLauncher.logger.info("ReportGeneratorController: handleSelectAllAction: Called.");
        long selectedCount = fanTableView.getItems().stream().filter(Result::isSelected).count();
        long totalItems = fanTableView.getItems().size();

        // Perform the core logic within a Platform.runLater to ensure all UI updates
        // and flag changes are on the FX Application Thread and sequenced correctly.
        Platform.runLater(() -> {
            isProgrammaticSelection = true; // Set flag when starting programmatic changes
            ApplicationLauncher.logger.debug("ReportGeneratorController: handleSelectAllAction: isProgrammaticSelection set to TRUE.");
            try {
                // If nothing or some are selected, intent is to select the first N (or all if no limit)
                if (selectedCount == 0 || (selectedCount == totalItems && selectAllCheckbox.isSelected())) { // Checkbox might be "selected" visually but mean "clear"
                    ApplicationLauncher.logger.info("ReportGeneratorController: handleSelectAllAction: Attempting to select first " + currentTemplateMaxRecords + " records.");
                    
                    // Clear all current selections first to ensure a clean "select first N" operation
                    fanTableView.getItems().forEach(fan -> fan.setSelected(false));

                    int countSelected = 0;
                    for (Result fan : fanTableView.getItems()) {
                        if (countSelected < currentTemplateMaxRecords) {
                            fan.setSelected(true);
                            countSelected++;
                        } else {
                            // Ensure any items beyond limit are deselected if they somehow were selected
                            if (fan.isSelected()) {
                                fan.setSelected(false);
                            }
                        }
                    }

                    // Show informational alert only if actual limiting occurred
                    if (totalItems > currentTemplateMaxRecords && currentTemplateMaxRecords != Integer.MAX_VALUE) {
                        showAlert(Alert.AlertType.INFORMATION, "Selection Limited",
                                "Only the first " + currentTemplateMaxRecords + " records were selected based on the template configuration.");
                    }

                } else { // If all items are currently selected (meaning the button shows "Clear Selection")
                    ApplicationLauncher.logger.info("ReportGeneratorController: handleSelectAllAction: Clearing all selections.");
                    fanTableView.getItems().forEach(fan -> fan.setSelected(false));
                }
            } finally {
                isProgrammaticSelection = false; // Reset flag after programmatic changes are done
                ApplicationLauncher.logger.debug("ReportGeneratorController: handleSelectAllAction: isProgrammaticSelection set to FALSE.");
                
                // Now refresh UI elements AFTER all data model changes are complete and flag is reset
                fanTableView.refresh();
                updateGenerateReportButtonState();
                updateSelectAllCheckboxState();
            }
        });
    }


    /**
     * Loads the configured template directory path from a JSON file.
     * If the file doesn't exist or is invalid, it falls back to the default path.
     */
    private void loadConfiguredTemplatesDirPath() {
        File configFile = new File(TEMPLATES_CONFIG_FILE_NAME);
        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                Properties configProps = GSON.fromJson(reader, Properties.class);
                if (configProps != null && configProps.containsKey("templatesDirPath")) {
                    TEMPLATES_DIR_PATH = configProps.getProperty("templatesDirPath");
                    ApplicationLauncher.logger.info("ReportGeneratorController: loadConfiguredTemplatesDirPath: Loaded TEMPLATES_DIR_PATH from config: " + TEMPLATES_DIR_PATH);
                } else {
                    ApplicationLauncher.logger.error("ReportGeneratorController: loadConfiguredTemplatesDirPath: Config file exists but is empty or missing 'templatesDirPath'. Using default path.");
                }
            } catch (IOException | JsonSyntaxException e) {
                ApplicationLauncher.logger.error("ReportGeneratorController: loadConfiguredTemplatesDirPath: Error reading templates config file: " + e.getMessage());
                // Fallback to default path
            }
        } else {
            ApplicationLauncher.logger.info("ReportGeneratorController: loadConfiguredTemplatesDirPath: Templates config file not found. Using default path: " + TEMPLATES_DIR_PATH);
        }
        
    }

    /**
     * Saves the provided template directory path to a JSON file.
     * @param newPath The new path to save.
     */
    private void saveConfiguredTemplatesDirPath(String newPath) {
        File configFile = new File(TEMPLATES_CONFIG_FILE_NAME);
        try (FileWriter writer = new FileWriter(configFile)) {
            Properties configProps = new Properties();
            configProps.setProperty("templatesDirPath", newPath);
            GSON.toJson(configProps, writer);
            TEMPLATES_DIR_PATH = newPath; // Update the static variable
            ApplicationLauncher.logger.info("ReportGeneratorController: saveConfiguredTemplatesDirPath: TEMPLATES_DIR_PATH saved to config: " + newPath);
        } catch (IOException e) {
            ApplicationLauncher.logger.error("ReportGeneratorController: saveConfiguredTemplatesDirPath: Error saving templates config file: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Save Error", "Failed to save template path: " + e.getMessage());
        }
    }

    /**
     * Reloads the templates from the current TEMPLATES_DIR_PATH.
     * This method re-scans the directory, updates the ListView, and clears selection/preview.
     */
    private void reloadTemplates() {
        ApplicationLauncher.logger.info("ReportGeneratorController: reloadTemplates: Reloading templates from: " + TEMPLATES_DIR_PATH);
        availableTemplates.clear();
        availableTemplates.addAll(loadTemplatesFromDirectory());
        templateListView.setItems(availableTemplates.stream().map(Template::getDisplayName).collect(Collectors.toCollection(FXCollections::observableArrayList)));
        
        selectedTemplate = null; // Clear selected template
        updateTemplatePreview(null); // Clear preview area
        
        // Also re-apply filters and search to ensure the table reflects the data from the new path
        handleSearchFans(); // This will trigger applyStatusFilter as well
    }

    private long lastOpenedTimeMillis = 0; // cooldown state (instance variable)

    /**
     * Opens a dialog for the user to configure the TEMPLATES_DIR_PATH.
     */
    void openPathConfigurationDialog() {
        long currentTime = System.currentTimeMillis();
        long timeSinceLastOpen = currentTime - lastOpenedTimeMillis;

        if (timeSinceLastOpen < 10_000) { // less than 10 seconds
            ApplicationLauncher.logger.info("ReportGeneratorController: openPathConfigurationDialog: Dialog already opened recently. Wait before reopening.");
            return;
        }

        lastOpenedTimeMillis = currentTime; // update timestamp

        ApplicationLauncher.logger.info("ReportGeneratorController: openPathConfigurationDialog: Opening Path Configuration Dialog.");
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Configure Template Directory");
        dialogStage.initModality(Modality.APPLICATION_MODAL);

        try {
            Window owner = templateListView != null && templateListView.getScene() != null
                           ? templateListView.getScene().getWindow()
                           : null;
            if (owner != null) {
                dialogStage.initOwner(owner);
            } else {
                ApplicationLauncher.logger.warn("ReportGeneratorController: openPathConfigurationDialog: templateListView or its scene is null. Dialog will open without an owner.");
            }
        } catch (Exception e) {
            ApplicationLauncher.logger.warn("ReportGeneratorController: openPathConfigurationDialog: Failed to set dialog owner: " + e.getMessage());
        }

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(5);

        Label pathLabel = new Label("New Template Directory Path:");
        TextField pathTextField = new TextField(TEMPLATES_DIR_PATH);
        pathTextField.setPrefWidth(400);

        Button saveButton = new Button("Save and Reload");
        Button cancelButton = new Button("Cancel");
        Label statusLabel = new Label();
        statusLabel.setWrapText(true);

        grid.addRow(0, pathLabel, pathTextField);
        grid.addRow(1, saveButton, cancelButton);
        GridPane.setColumnSpan(statusLabel, 2);
        grid.addRow(2, statusLabel);

        saveButton.setOnAction(event -> {
            String newPath = pathTextField.getText().trim();
            File newDir = new File(newPath);
            if (newPath.isEmpty() || !newDir.exists() || !newDir.isDirectory()) {
                statusLabel.setText("Invalid path. Please enter a valid existing directory.");
                statusLabel.setTextFill(Color.RED);
                ApplicationLauncher.logger.info("ReportGeneratorController: openPathConfigurationDialog: Invalid path entered in config dialog: " + newPath);
            } else {
                saveConfiguredTemplatesDirPath(newPath);
                reloadTemplates();
                statusLabel.setText("Path saved and templates reloaded successfully.");
                statusLabel.setTextFill(Color.GREEN);
                ApplicationLauncher.logger.info("ReportGeneratorController: openPathConfigurationDialog: Path saved and reloaded: " + newPath);
                dialogStage.close();
            }
        });

        cancelButton.setOnAction(event -> {
            ApplicationLauncher.logger.info("ReportGeneratorController: openPathConfigurationDialog: Path Configuration Dialog cancelled.");
            dialogStage.close();
        });

        dialogStage.setScene(new javafx.scene.Scene(grid));
        dialogStage.showAndWait();
    }




    /**
     * Updates the ImageView to show the preview of the selected template.
     * @param template The template whose preview to show, or null to clear.
     */
    private void updateTemplatePreview(Template template) {
        if (template != null) {
            File imageFile = template.getImageFile();
            if (imageFile != null && imageFile.exists() && imageFile.isFile()) {
                try {
                    Image image = new Image(imageFile.toURI().toString());
                    templatePreviewImageView.setImage(image);
                    templatePreviewImageView.setVisible(true);
                    noPreviewLabel.setVisible(false);
                    ApplicationLauncher.logger.info("ReportGeneratorController: updateTemplatePreview: Successfully loaded JPG: " + imageFile.getAbsolutePath());
                } catch (Exception e) {
                    ApplicationLauncher.logger.error("ReportGeneratorController: updateTemplatePreview: Failed to load JPG preview for: " + template.getName() + " - " + e.getMessage());
                    templatePreviewImageView.setImage(null); // Clear image on error
                    templatePreviewImageView.setVisible(false);
                    noPreviewLabel.setText("Could not load image preview. Error: " + e.getMessage());
                    noPreviewLabel.setVisible(true);
                }
            } else {
                templatePreviewImageView.setImage(null); // Clear image if no file or invalid
                templatePreviewImageView.setVisible(false);
                noPreviewLabel.setText("No JPG preview available for " + template.getName() + ".");
                noPreviewLabel.setVisible(true);
                ApplicationLauncher.logger.info("ReportGeneratorController: updateTemplatePreview: No JPG file found for: " + template.getName() + " at " + (imageFile != null ? imageFile.getAbsolutePath() : "null path"));
            }
        } else {
            templatePreviewImageView.setImage(null); // Clear image when no template selected
            templatePreviewImageView.setVisible(false);
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
        File templateXlsxDir = new File(TEMPLATES_DIR_PATH, "xlsx"); // Point to the xlsx subfolder

        if (!templateXlsxDir.exists()) {
            ApplicationLauncher.logger.error("ReportGeneratorController: loadTemplatesFromDirectory: Template XLSX directory does not exist: " + templateXlsxDir.getAbsolutePath());
            errorMessageLabel.setText("Error: Template XLSX directory not found.");
            return templates;
        }
        if (!templateXlsxDir.isDirectory()) {
            ApplicationLauncher.logger.error("ReportGeneratorController: loadTemplatesFromDirectory: Path is not a directory: " + templateXlsxDir.getAbsolutePath());
            errorMessageLabel.setText("Error: Template XLSX path is not a directory.");
            return templates;
        }

        // Filter for ONLY .xlsx files for listing
        File[] xlsxFiles = templateXlsxDir.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".xlsx"));

        if (xlsxFiles != null) {
            for (File xlsxFile : xlsxFiles) {
                // Determine the corresponding JPG file name in the 'jpg' subfolder
                String baseName = getFileNameWithoutExtension(xlsxFile.getName());
                File imageFile = new File(new File(TEMPLATES_DIR_PATH, "jpg"), baseName + ".jpg"); // Check for JPG in 'jpg' subfolder
                templates.add(new Template(xlsxFile.getName(), xlsxFile, imageFile));
            }
        } else {
            ApplicationLauncher.logger.error("ReportGeneratorController: loadTemplatesFromDirectory: Could not list files in directory: " + templateXlsxDir.getAbsolutePath());
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
     * Inner class representing a parsed search query for fan serial numbers.
     * Can be either an exact serial number or a range with a common prefix.
     */
    public static class SearchQuery {
        private String exactSerialNumber; // For single searches like "SN-001"
        private String prefix;            // For range searches, e.g., "SN-"
        private int startNumeric;         // Start of numeric range, e.g., 1
        private int endNumeric;           // End of numeric range, e.g., 100
        private int numericPartLengthForRange; // New field for padding, used during range query generation

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
         * @param numericPartLengthForRange The detected length of the numeric part for padding.
         */
        public SearchQuery(String prefix, int startNumeric, int endNumeric, int numericPartLengthForRange) {
            this.prefix = prefix;
            this.startNumeric = startNumeric;
            this.endNumeric = endNumeric;
            this.numericPartLengthForRange = numericPartLengthForRange;
        }

        /**
         * Checks if this query represents a range search.
         * @return true if it's a range search, false otherwise (meaning it's an exact match).
         */
        public boolean isRange() {
            return prefix != null;
        }

        // Getters for use in handleSearchFans
        public String getPrefix() { return prefix; }
        public int getStartNumeric() { return startNumeric; }
        public int getEndNumeric() { return endNumeric; }
        public int getNumericPartLengthForRange() { return numericPartLengthForRange; }
        public String getExactSerialNumber() { return exactSerialNumber; }

        /**
         * Determines if a given fan serial number matches this search query (used for in-memory filtering if applicable).
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
                    
                    // Optional: enforce padding consistency if required for stricter matching
                    // if (numericPartLengthForRange > 0 && fanNumericPartStr.length() != numericPartLengthForRange) {
                    //     return false; // Mismatch in padding
                    // }

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

        ApplicationLauncher.logger.info("ReportGeneratorController: parseSerialQueries: Parsing serial input: '" + input + "'");

        for (String part : parts) {
            String trimmedPart = part.trim();
            if (trimmedPart.isEmpty()) continue;

            if (trimmedPart.contains("-")) {
                // This is a potential range query
                String[] rangeParts = trimmedPart.split("-");
                
                int firstHyphenIndex = trimmedPart.indexOf('-');
                if (firstHyphenIndex == -1 || trimmedPart.lastIndexOf('-') == firstHyphenIndex) {
                    rangeParts = trimmedPart.split("-");
                } else {
                    rangeParts = trimmedPart.split("-", 2); // Split only on the first hyphen
                }


                if (rangeParts.length != 2) {
                    errorMessageLabel.setText(String.format("Invalid range format: '%s'. Expected 'START-END'.", trimmedPart));
                    ApplicationLauncher.logger.error("ReportGeneratorController: parseSerialQueries: Invalid range format: '" + trimmedPart + "'. Expected 'START-END'.");
                    return new ArrayList<>(); // Return empty list on error
                }

                String startStr = rangeParts[0].trim();
                String endStr = rangeParts[1].trim();

                // Parse the start part of the range
                Matcher startMatcher = serialPattern.matcher(startStr);
                if (!startMatcher.matches()) {
                    errorMessageLabel.setText(String.format("Invalid start serial format in range: '%s'. Expected 'PREFIXNUMBER'.", startStr));
                    ApplicationLauncher.logger.error("ReportGeneratorController: parseSerialQueries: Invalid start serial format in range: '" + startStr + "'. Expected 'PREFIXNUMBER'.");
                    return new ArrayList<>();
                }
                String startPrefix = startMatcher.group(1);
                String startNumericPartStr = startMatcher.group(2); // Get original numeric string for padding length
                int startNumeric;
                try {
                    startNumeric = Integer.parseInt(startNumericPartStr);
                } catch (NumberFormatException e) {
                     errorMessageLabel.setText(String.format("Invalid numeric part in start serial: '%s'.", startStr));
                     ApplicationLauncher.logger.error("ReportGeneratorController: parseSerialQueries: Invalid numeric part in start serial: '" + startStr + "'. " + e.getMessage());
                     return new ArrayList<>();
                }
                int detectedPaddingLength = startNumericPartStr.length(); // Capture padding length here


                // Parse the end part of the range
                Matcher endMatcher = serialPattern.matcher(endStr);
                String commonPrefix;
                int endNumeric;

                if (endMatcher.matches()) {
                    // The end part also has a prefix and numeric part (e.g., "SN-100")
                    String endPrefix = endMatcher.group(1);
                    try {
                        endNumeric = Integer.parseInt(endMatcher.group(2));
                    }  catch (NumberFormatException e) {
                         errorMessageLabel.setText(String.format("Invalid numeric part in end serial: '%s'.", endStr));
                         ApplicationLauncher.logger.error("ReportGeneratorController: parseSerialQueries: Invalid numeric part in end serial: '" + endStr + "'. " + e.getMessage());
                         return new ArrayList<>();
                    }
                    // Crucial: prefixes must match for a valid alphanumeric range
                    if (!startPrefix.equalsIgnoreCase(endPrefix)) {
                        errorMessageLabel.setText(String.format("Prefix mismatch in range: '%s' and '%s'. Prefixes must be the same.", startStr, endStr));
                        ApplicationLauncher.logger.error("ReportGeneratorController: parseSerialQueries: Prefix mismatch in range: '" + startStr + "' and '" + endStr + "'.");
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
                        ApplicationLauncher.logger.error("ReportGeneratorController: parseSerialQueries: Invalid end serial format in range: '" + endStr + "'. " + e.getMessage());
                        return new ArrayList<>();
                    }
                }

                // Ensure startNumeric is not greater than endNumeric, swap if necessary
                if (startNumeric > endNumeric) {
                    int temp = startNumeric;
                    startNumeric = endNumeric;
                    endNumeric = temp;
                }
                SearchQuery newQuery = new SearchQuery(commonPrefix, startNumeric, endNumeric, detectedPaddingLength);
                queries.add(newQuery);
                ApplicationLauncher.logger.info("ReportGeneratorController: parseSerialQueries: Parsed range query: " + newQuery.getPrefix() + String.format("%0" + newQuery.getNumericPartLengthForRange() + "d", newQuery.getStartNumeric()) + "-" + String.format("%0" + newQuery.getNumericPartLengthForRange() + "d", newQuery.getEndNumeric()));

            } else {
                // This is a single serial number for exact matching
                SearchQuery newQuery = new SearchQuery(trimmedPart);
                queries.add(newQuery);
                ApplicationLauncher.logger.info("ReportGeneratorController: parseSerialQueries: Parsed exact query: " + newQuery.getExactSerialNumber());
            }
        }
        return queries;
    }

    /**
     * Handles the search button click event.
     * Parses the serial input and filters the fan data based on fanSerialNumber.
     * This method is triggered by the search button or by pressing Enter in the serialInputTextField.
     */
    @FXML
    private void handleSearchFans() {
        ApplicationLauncher.logger.info("ReportGeneratorController: handleSearchFans: Called.");
        errorMessageLabel.setText(""); // Clear previous error messages
        String serialInput = serialInputTextField.getText().trim();
        LocalDate fromDate = fromDatePicker != null ? fromDatePicker.getValue() : null;
        LocalDate toDate = toDatePicker != null ? toDatePicker.getValue() : null;

        List<SearchQuery> serialSearchQueries = new ArrayList<>();
        if (!serialInput.isEmpty()) {
            serialSearchQueries = parseSerialQueries(serialInput);
            if (serialSearchQueries.isEmpty() && !errorMessageLabel.getText().isEmpty()) {
                ApplicationLauncher.logger.info("ReportGeneratorController: handleSearchFans: Search query parsing failed. Clearing table.");
                fanTableView.setItems(FXCollections.emptyObservableList());
                updateTableViewVisibility();
                updateSelectAllCheckboxState();
                updateGenerateReportButtonState();
                return;
            }
        }

        // Convert LocalDate to epoch milliseconds
        Long fromEpochTime = null;
        if (fromDate != null) {
            fromEpochTime = fromDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
            ApplicationLauncher.logger.info("ReportGeneratorController: handleSearchFans: From Date: " + fromDate + " -> From Epoch: " + fromEpochTime);
        }

        Long toEpochTime = null;
        if (toDate != null) {
            toEpochTime = toDate.atTime(LocalTime.MAX).atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
            ApplicationLauncher.logger.info("ReportGeneratorController: handleSearchFans: To Date: " + toDate + " -> To Epoch: " + toEpochTime);
        }

        boolean isSerialInputProvided = !serialInput.isEmpty();
        boolean isDateFilterActive = (fromEpochTime != null || toEpochTime != null);

        List<Result> preliminaryFilteredData = new ArrayList<>();

        if (isSerialInputProvided && isDateFilterActive) {
            if (serialSearchQueries.size() == 1 && !serialSearchQueries.get(0).isRange()) {
                String singleSerialNumber = serialSearchQueries.get(0).getExactSerialNumber();
                preliminaryFilteredData = DeviceDataManagerController.getResultService()
                    .findByEpochTimeBetweenAndFanSerialNumber(fromEpochTime, toEpochTime, singleSerialNumber);
                ApplicationLauncher.logger.info("ReportGeneratorController: handleSearchFans: Single serial ('" + singleSerialNumber + "') and date range filter applied. Found " + preliminaryFilteredData.size() + " fans.");
            } else {
                Set<Result> uniqueResults = new HashSet<>();
                for (SearchQuery query : serialSearchQueries) {
                    if (query.isRange()) {
                        int paddingLength = query.getNumericPartLengthForRange();
                        for (int i = query.getStartNumeric(); i <= query.getEndNumeric(); i++) {
                            String formattedSerial = String.format("%s%0" + paddingLength + "d", query.getPrefix(), i);
                            List<Result> found = DeviceDataManagerController.getResultService()
                                .findByEpochTimeBetweenAndFanSerialNumber(fromEpochTime, toEpochTime, formattedSerial);
                            uniqueResults.addAll(found);
                        }
                    } else {
                        List<Result> found = DeviceDataManagerController.getResultService()
                            .findByEpochTimeBetweenAndFanSerialNumber(fromEpochTime, toEpochTime, query.getExactSerialNumber());
                        uniqueResults.addAll(found);
                    }
                }
                preliminaryFilteredData.addAll(uniqueResults);
                ApplicationLauncher.logger.info("ReportGeneratorController: handleSearchFans: Multiple serials/range and date range filter applied. Found " + preliminaryFilteredData.size() + " fans.");
            }
        } else if (isSerialInputProvided) {
            Set<Result> uniqueResults = new HashSet<>();
            for (SearchQuery query : serialSearchQueries) {
                if (query.isRange()) {
                    int paddingLength = query.getNumericPartLengthForRange();
                    for (int i = query.getStartNumeric(); i <= query.getEndNumeric(); i++) {
                        String formattedSerial = String.format("%s%0" + paddingLength + "d", query.getPrefix(), i);
                        List<Result> found = DeviceDataManagerController.getResultService().findByFanSerialNumber(formattedSerial);
                        uniqueResults.addAll(found);
                    }
                } else {
                    List<Result> found = DeviceDataManagerController.getResultService().findByFanSerialNumber(query.getExactSerialNumber());
                    uniqueResults.addAll(found);
                }
            }
            preliminaryFilteredData.addAll(uniqueResults);
            ApplicationLauncher.logger.info("ReportGeneratorController: handleSearchFans: Only serial filter applied. Found " + preliminaryFilteredData.size() + " fans.");
        } else if (isDateFilterActive) {
            preliminaryFilteredData = DeviceDataManagerController.getResultService()
                .findByEpochTimeBetween(fromEpochTime, toEpochTime);
            ApplicationLauncher.logger.info("ReportGeneratorController: handleSearchFans: Only date filter applied. Found " + preliminaryFilteredData.size() + " fans.");
        } else {
            ApplicationLauncher.logger.info("ReportGeneratorController: handleSearchFans: No filters active. No results shown.");
        }

        // 🔽 Sort by numeric suffix of the fan serial number
        preliminaryFilteredData.sort(Comparator.comparingInt(r -> extractNumericSuffix(r.getFanSerialNumber())));

        // ✅ Final filter and display
        applyStatusFilter(filterComboBox.getSelectionModel().getSelectedItem(), preliminaryFilteredData);
    }
    
    private int extractNumericSuffix(String serial) {
        if (serial == null || serial.isEmpty()) return Integer.MAX_VALUE;

        Matcher matcher = Pattern.compile("(\\d+)$").matcher(serial);
        if (matcher.find()) {
            try {
                return Integer.parseInt(matcher.group(1));
            } catch (NumberFormatException e) {
                // Parsing failed; place at the end
            }
        }
        return Integer.MAX_VALUE;
    }

    /**
     * Applies a status filter to the fanTableView.
     * @param selectedFilter The selected filter string ("NONE", "PASSED", "FAILED").
     * @param sourceList The list of Result objects to filter from.
     */
    private void applyStatusFilter(String selectedFilter, List<Result> sourceList) {
        ApplicationLauncher.logger.info("ReportGeneratorController: applyStatusFilter: Called with filter: " + selectedFilter);
        List<Result> listToFilter = (sourceList != null) ? sourceList : new ArrayList<>(); // Ensure sourceList is not null
        ObservableList<Result> filteredData = FXCollections.observableArrayList();

        if ("PASSED".equalsIgnoreCase(selectedFilter)) {
            filteredData.addAll(listToFilter.stream()
                                    .filter(r -> "PASSED".equalsIgnoreCase(r.getTestStatus()))
                                    .collect(Collectors.toList()));
        } else if ("FAILED".equalsIgnoreCase(selectedFilter)) {
            filteredData.addAll(listToFilter.stream()
                                    .filter(r -> "FAILED".equalsIgnoreCase(r.getTestStatus()))
                                    .collect(Collectors.toList()));
        } else { // "NONE" or any other value
            filteredData.addAll(listToFilter);
        }

        ApplicationLauncher.logger.info("ReportGeneratorController: applyStatusFilter: Filtered data size: " + filteredData.size());
        fanTableView.setItems(filteredData);
        fanTableView.refresh(); // Refresh table to ensure colors and selection states are correct
        updateTableViewVisibility();
        updateSelectAllCheckboxState(); // Update the Select All checkbox's label (itself)
        updateGenerateReportButtonState();
    }

    /**
     * Overload for applyStatusFilter to simplify calls when filtering based on UI changes.
     * @param selectedFilter The selected filter string ("NONE", "PASSED", "FAILED").
     */
    private void applyStatusFilter(String selectedFilter) {
        // When triggered by ComboBox change, re-run the main search logic to re-fetch from DB.
        ApplicationLauncher.logger.info("ReportGeneratorController: applyStatusFilter: Overload called, re-invoking handleSearchFans().");
        handleSearchFans(); 
    }


    /**
     * Handles the generate report button click event.
     * Collects selected fan IDs and the chosen template, then simulates report generation.
     */
    @FXML
    private void handleGenerateReport() {
        ApplicationLauncher.logger.info("ReportGeneratorController: handleGenerateReport: Button clicked.");
        List<Result> fansToReport = fanTableView.getItems().stream()
                .filter(Result::isSelected)
                .collect(Collectors.toList());

        if (fansToReport.isEmpty()) {
            ApplicationLauncher.logger.warn("ReportGeneratorController: handleGenerateReport: No fans selected for report.");
            showAlert(Alert.AlertType.WARNING, "No Fans Selected", "Please select at least one fan to generate a report.");
            return;
        }

        if (selectedTemplate == null) {
            ApplicationLauncher.logger.warn("ReportGeneratorController: handleGenerateReport: No template selected for report.");
            showAlert(Alert.AlertType.WARNING, "No Template Selected", "Please select an Excel template to generate the report.");
            return;
        }

        try {
            ApplicationLauncher.logger.info("ReportGeneratorController: handleGenerateReport: Initiating Excel report generation for " + fansToReport.size() + " fans.");
            generateExcelReport(selectedTemplate, fansToReport);
            ApplicationLauncher.logger.info("ReportGeneratorController: handleGenerateReport: Report generation complete.");
            showAlert(Alert.AlertType.INFORMATION, "Report Generation Complete",
                    "Report successfully generated and saved to your chosen location.");
        }
        catch (IOException e) {
            ApplicationLauncher.logger.error("ReportGeneratorController: handleGenerateReport: Report generation failed due to I/O error: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Report Generation Failed",
                    "Failed to generate report due to an I/O error: " + e.getMessage());
            e.printStackTrace();
        }
        catch (Exception e) {
            ApplicationLauncher.logger.error("ReportGeneratorController: handleGenerateReport: Unexpected error during report generation: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Report Generation Failed",
                    "An unexpected error occurred during report generation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Opens a dialog for the user to configure the Excel mapping for the selected template	.
     * @param template The template to configure.
     */
    private void openTemplateConfigurationDialog(Template template) {
        ApplicationLauncher.logger.info("ReportGeneratorController: openTemplateConfigurationDialog: Opening dialog for template: " + template.getName());
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Configure Template: " + template.getName());
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(templateListView.getScene().getWindow());

        // === Layout containers ===
        HBox root = new HBox(20);
        root.setPadding(new Insets(10));
        root.setAlignment(Pos.TOP_LEFT);

        // === Nominal Data ===
        GridPane nominalGrid = new GridPane();
        nominalGrid.setHgap(10);
        nominalGrid.setVgap(5);
        nominalGrid.setPadding(new Insets(10));

        TitledPane nominalPane = new TitledPane("Nominal Data", nominalGrid);
        nominalPane.setCollapsible(false);

        Map<String, TextField> nominalTextFields = new LinkedHashMap<>();
        String[] nominalFields = {"modelCell", "phaseCell", "currentCell", "powerCell", "speedCell", "airVolumeCell"};
        int nRow = 0;
        for (String field : nominalFields) {
            Label label = new Label(toTitleCase(field.replace("Cell", "")) + " Cell:");
            TextField textField = new TextField();
            textField.setPromptText("e.g., A1");
            nominalTextFields.put(field, textField);
            nominalGrid.addRow(nRow++, label, textField);
        }

        // === Mapping Grid ===
        GridPane mappingGrid = new GridPane();
        mappingGrid.setHgap(10);
        mappingGrid.setVgap(5);
        mappingGrid.setPadding(new Insets(10));

        TextField recordsTextField = new TextField();
        recordsTextField.setPromptText("e.g., 10");
        recordsTextField.setTextFormatter(new TextFormatter<>(change -> {
            return change.getControlNewText().matches("\\d*") ? change : null;
        }));
        mappingGrid.addRow(0, new Label("Number of Records:"), recordsTextField);

        int mRow = 1;
        Map<String, TextField> propertyTextFields = new LinkedHashMap<>();
        for (String propertyName : RESULT_PROPERTIES_TO_MAP) {
            Label label = new Label(camelCaseToTitleCase(propertyName) + " Start Cell:");
            TextField textField = new TextField();
            textField.setPromptText("e.g., B2");
            propertyTextFields.put(propertyName, textField);
            mappingGrid.addRow(mRow++, label, textField);
        }

        Button loadButton = new Button("Load Configuration");
        Button saveButton = new Button("Save Configuration");
        Label statusLabel = new Label();
        statusLabel.setWrapText(true);

        mappingGrid.addRow(mRow++, loadButton, saveButton);
        GridPane.setColumnSpan(statusLabel, 2);
        mappingGrid.addRow(mRow, statusLabel);

        // Combine and set scene
        root.getChildren().addAll(nominalPane, mappingGrid);
        dialogStage.setScene(new Scene(root));
        dialogStage.show();

        // === Config file location ===
        File configFile = getConfigFile(template);

        // === Load button logic ===
        loadButton.setOnAction(event -> {
            ApplicationLauncher.logger.info("Loading config for template: " + template.getName());
            if (configFile.exists()) {
                try (FileReader reader = new FileReader(configFile)) {
                    TemplateConfig config = GSON.fromJson(reader, TemplateConfig.class);
                    if (config != null) {
                        recordsTextField.setText(String.valueOf(config.getNumRecords()));
                        if (config.getPropertyToCellRange() != null) {
                            for (Map.Entry<String, String> entry : config.getPropertyToCellRange().entrySet()) {
                                TextField tf = propertyTextFields.get(entry.getKey());
                                if (tf != null) tf.setText(entry.getValue());
                            }
                        }
                        if (config.getNominalFieldToCell() != null) {
                            for (Map.Entry<String, String> entry : config.getNominalFieldToCell().entrySet()) {
                                TextField tf = nominalTextFields.get(entry.getKey());
                                if (tf != null) tf.setText(entry.getValue());
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
                    e.printStackTrace();
                }
            } else {
                statusLabel.setText("No configuration file found.");
                statusLabel.setTextFill(Color.BLUE);
            }
        });

        loadButton.fire(); // Auto-load config on open

        // === Save button logic ===
        saveButton.setOnAction(event -> {
            TemplateConfig config = new TemplateConfig();
            Map<String, String> newMapping = new HashMap<>();
            Map<String, String> nominalMapping = new HashMap<>();
            boolean hasInvalidInput = false;

            // Validate number of records
            try {
                int numRecords = Integer.parseInt(recordsTextField.getText().trim());
                if (numRecords <= 0) throw new NumberFormatException("Non-positive");
                config.setNumRecords(numRecords);
            } catch (NumberFormatException e) {
                statusLabel.setText("Invalid number of records. Must be a positive integer.");
                statusLabel.setTextFill(Color.RED);
                return;
            }

            // Validate result property mappings
            for (Map.Entry<String, TextField> entry : propertyTextFields.entrySet()) {
                String prop = entry.getKey();
                String cell = entry.getValue().getText().trim().toUpperCase();
                if (!cell.isEmpty()) {
                    if (!cell.matches("^[A-Z]+[0-9]+$")) {
                        statusLabel.setText("Invalid cell format for " + prop + ": " + cell);
                        statusLabel.setTextFill(Color.RED);
                        hasInvalidInput = true;
                        break;
                    }
                    newMapping.put(prop, cell);
                }
            }

            // Validate nominal field mappings
            for (Map.Entry<String, TextField> entry : nominalTextFields.entrySet()) {
                String prop = entry.getKey();
                String cell = entry.getValue().getText().trim().toUpperCase();
                if (!cell.isEmpty()) {
                    if (!cell.matches("^[A-Z]+[0-9]+$")) {
                        statusLabel.setText("Invalid cell format for " + prop + ": " + cell);
                        statusLabel.setTextFill(Color.RED);
                        hasInvalidInput = true;
                        break;
                    }
                    nominalMapping.put(prop, cell);
                }
            }

            if (hasInvalidInput) return;

            config.setPropertyToCellRange(newMapping);
            config.setNominalFieldToCell(nominalMapping);

            try (FileWriter writer = new FileWriter(configFile)) {
                GSON.toJson(config, writer);
                statusLabel.setText("Configuration saved successfully.");
                statusLabel.setTextFill(Color.GREEN);
            } catch (IOException e) {
                statusLabel.setText("Error saving configuration: " + e.getMessage());
                statusLabel.setTextFill(Color.RED);
                e.printStackTrace();
            }
        });
    }

    /**
     * Helper to get the JSON configuration file for a given template.
     */
    private File getConfigFile(Template template) {
        String baseName = getFileNameWithoutExtension(template.getName());
        // Point to the 'json' subfolder for template-specific configurations
        return new File(new File(TEMPLATES_DIR_PATH, "json"), baseName + ".json");
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
        ApplicationLauncher.logger.info("ReportGeneratorController: generateExcelReport: Called for template: " + template.getDisplayName() + ", fans count: " + fansToReport.size());

        File configFile = getConfigFile(template); // This will now point to reportTemplates/json
        if (!configFile.exists()) {
            ApplicationLauncher.logger.warn("ReportGeneratorController: generateExcelReport: Config file not found for template: " + template.getName());
            showAlert(Alert.AlertType.ERROR, "Configuration Missing",
                    "Configuration file not found for template '" + template.getDisplayName() + "'. Please double-click the template to configure cell mappings.");
            return; // Exit method
        }

        TemplateConfig config;
        try (FileReader reader = new FileReader(configFile)) {
            config = GSON.fromJson(reader, TemplateConfig.class);
            // Changed validation for numRecords to be > 0 AND mappings not empty
            if (config == null || config.getNumRecords() <= 0 || config.getPropertyToCellRange() == null || config.getPropertyToCellRange().isEmpty()) {
                ApplicationLauncher.logger.warn("ReportGeneratorController: generateExcelReport: Config is empty, invalid, or missing numRecords/mappings for template: " + template.getName() + ".");
                showAlert(Alert.AlertType.ERROR, "Configuration Empty/Invalid",
                        "Configuration for template '" + template.getDisplayName() + "' is incomplete or invalid. Please configure 'Number of Records' and cell mappings.");
                return; // Exit method
            }
        } catch (JsonSyntaxException e) {
            ApplicationLauncher.logger.error("ReportGeneratorController: generateExcelReport: Error parsing config for template: " + template.getName() + " - " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Configuration Error",
                    "Error parsing configuration for template '" + template.getDisplayName() + "'. It might be malformed JSON. Error: " + e.getMessage());
            e.printStackTrace();
            return; // Exit method
        }


        // Use try-with-resources to ensure workbook and file streams are closed
        File xlsxTemplateFile = new File(new File(TEMPLATES_DIR_PATH, "xlsx"), template.getName()); // Point to xlsx subfolder
        try (FileInputStream fis = new FileInputStream(xlsxTemplateFile);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // Get the first sheet of the workbook
            ApplicationLauncher.logger.info("ReportGeneratorController: generateExcelReport: Processing sheet: " + sheet.getSheetName());

            int numRecordsToProcess = config.getNumRecords();
            ApplicationLauncher.logger.info("ReportGeneratorController: generateExcelReport: Number of records configured in template: " + numRecordsToProcess);

            // Prepare a map to keep track of the current row index for each configured column.
            // This ensures data is appended sequentially within each column's defined range.
            Map<Integer, Integer> currentRowIndexForColumn = new HashMap<>();
            
            // Initialize the current row index for each configured column based on its start row
            // and determine the max end row for any column based on numRecords.
            Map<Integer, Integer> endRowIndexForColumn = new HashMap<>();


            for (Map.Entry<String, String> entry : config.getPropertyToCellRange().entrySet()) {
                String cellRefStr = entry.getValue(); // This is now a single cell like "A2"
                int startColIdx = TemplateConfig.getColumnIndexFromCellReference(cellRefStr);
                int startRowIdx = TemplateConfig.getRowIndexFromCellReference(cellRefStr);
                
                if (startColIdx != -1 && startRowIdx != -1) {
                    currentRowIndexForColumn.put(startColIdx, startRowIdx);
                    // Calculate the end row for this specific column based on numRecords
                    int calculatedEndRowIdx = TemplateConfig.getEndRowIndex(startRowIdx, numRecordsToProcess);
                    endRowIndexForColumn.put(startColIdx, calculatedEndRowIdx);
                    ApplicationLauncher.logger.debug("ReportGeneratorController: generateExcelReport: Mapping: " + entry.getKey() + " to Start Cell: " + cellRefStr + " (Col: " + startColIdx + ", Row: " + startRowIdx + ") End Row: " + calculatedEndRowIdx);
                } else {
                    ApplicationLauncher.logger.warn("ReportGeneratorController: generateExcelReport: Invalid starting cell reference '" + cellRefStr + "' for property '" + entry.getKey() + "'. This mapping will be skipped.");
                }
            }


            // 1. Populate data rows based on configuration
            // Iterate through selected fans and populate the cells
            for (int i = 0; i < fansToReport.size(); i++) {
                Result fan = fansToReport.get(i);
                ApplicationLauncher.logger.info("ReportGeneratorController: generateExcelReport: Populating data for fan: " + fan.getFanSerialNumber() + " (Record " + (i + 1) + "/" + fansToReport.size() + ")");

                for (Map.Entry<String, String> mapping : config.getPropertyToCellRange().entrySet()) {
                    String resultPropertyName = mapping.getKey(); 
                    String cellRefStr = mapping.getValue(); // This is the starting cell like "A2"

                    int targetColIdx = TemplateConfig.getColumnIndexFromCellReference(cellRefStr);
                    int startRowIdx = TemplateConfig.getRowIndexFromCellReference(cellRefStr);
                    
                    if (targetColIdx == -1 || startRowIdx == -1) {
                        // Error already logged during initialization of currentRowIndexForColumn
                        continue; 
                    }

                    // Get the current row index for this specific column
                    int currentRowForThisColumn = currentRowIndexForColumn.getOrDefault(targetColIdx, startRowIdx);
                    int maxEndRowForThisColumn = endRowIndexForColumn.getOrDefault(targetColIdx, startRowIdx); // Max row for this column

                    // Only write if there's space left in the configured range for this column
                    if (currentRowForThisColumn <= maxEndRowForThisColumn) {
                        Object value = null;
                        try {
                            String getterMethodName = "get" + resultPropertyName.substring(0, 1).toUpperCase() + resultPropertyName.substring(1);
                            Method getter = Result.class.getMethod(getterMethodName);
                            value = getter.invoke(fan);
                            ApplicationLauncher.logger.debug("ReportGeneratorController: generateExcelReport: Property: " + resultPropertyName + ", Value: " + value + ", Target Cell: R" + (currentRowForThisColumn + 1) + "C" + (targetColIdx + 1));
                        } catch (NoSuchMethodException e) {
                            ApplicationLauncher.logger.warn("ReportGeneratorController: generateExcelReport: Getter method not found for property '" + resultPropertyName + "'. Skipping this data for fan " + fan.getFanSerialNumber());
                            continue;
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            ApplicationLauncher.logger.error("ReportGeneratorController: generateExcelReport: Error invoking getter for property '" + resultPropertyName + ": " + e.getMessage());
                            continue;
                        }
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
                    	ApplicationLauncher.logger.warn("ReportGeneratorController: generateExcelReport: Configured range for property '" + resultPropertyName + 
                    		    "' (starting at " + cellRefStr + " for " + numRecordsToProcess + 
                    		    " records) is full. Data for fan " + fan.getFanSerialNumber() + 
                    		    " (record #" + (i + 1) + ") will not fit.");
                    	
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
                ApplicationLauncher.logger.info("ReportGeneratorController: generateExcelReport: Saving generated report to: " + outputFile.getAbsolutePath());
                try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                    workbook.write(fos);
                }
            } else {
                ApplicationLauncher.logger.info("ReportGeneratorController: generateExcelReport: Report save cancelled by user.");
                throw new IOException("Report save cancelled by user.");
            }

        } // End of try-with-resources
    }

    /**
     * Updates the enabled state and text of the generate report button
     * based on the number of selected fans.
     */
    private void updateGenerateReportButtonState() {
        long selectedCount = fanTableView.getItems().stream().filter(Result::isSelected).count();
        ApplicationLauncher.logger.debug("ReportGeneratorController: updateGenerateReportButtonState: Selected count: " + selectedCount);

        boolean shouldBeEnabled = selectedCount > 0;
        
        // Update button text
        generateReportButton.setText("Generate Report (" + selectedCount + " selected)");

        // Update disabled state
        if (generateReportButton.isDisabled() == !shouldBeEnabled) {
            ApplicationLauncher.logger.debug("ReportGeneratorController: updateGenerateReportButtonState: No change in button disabled state. Skipping update.");
            return;
        }

        generateReportButton.setDisable(!shouldBeEnabled);
    }

    /**
     * Updates the text of the selectAll checkbox based on the current selection status
     * of individual fans in the table.
     * This method does NOT set `setSelected` or `setIndeterminate` on the selectAllCheckbox.
     */
    private void updateSelectAllCheckboxState() {
        ApplicationLauncher.logger.debug("ReportGeneratorController: updateSelectAllCheckboxState: Called.");
        if (fanTableView.getItems().isEmpty()) {
            ApplicationLauncher.logger.info("ReportGeneratorController: updateSelectAllCheckboxState: Table is empty. Disabling selectAll checkbox.");
            selectAllCheckbox.setDisable(true);
            selectAllCheckbox.setText("Select All");
            selectAllCheckbox.setSelected(false);
            selectAllCheckbox.setIndeterminate(false);
            return;
        }

        selectAllCheckbox.setDisable(false);
        long selectedCount = fanTableView.getItems().stream().filter(Result::isSelected).count();
        long totalItems = fanTableView.getItems().size();

        // Check if state has changed to avoid redundant updates
        String expectedText = selectedCount == 0 ? "Select First " + currentTemplateMaxRecords + " Records" : "Clear Selection";
        boolean expectedSelected = selectedCount > 0;
        boolean expectedIndeterminate = selectedCount > 0 && selectedCount < totalItems;

        if (selectAllCheckbox.getText().equals(expectedText) &&
            selectAllCheckbox.isSelected() == expectedSelected &&
            selectAllCheckbox.isIndeterminate() == expectedIndeterminate) {
            ApplicationLauncher.logger.debug("ReportGeneratorController: updateSelectAllCheckboxState: No change in checkbox state. Skipping update.");
            return;
        }

        ApplicationLauncher.logger.info("ReportGeneratorController: updateSelectAllCheckboxState: Selected count: " + selectedCount + ", Total items: " + totalItems);

        if (selectedCount == 0) {
            selectAllCheckbox.setText("Select First " + currentTemplateMaxRecords + " Records");
            // Although it behaves like a button, the internal state might influence visual style
            selectAllCheckbox.setSelected(false); 
            selectAllCheckbox.setIndeterminate(false);
        } else {
            selectAllCheckbox.setText("Clear Selection");
            // Set to true when selectedCount > 0 to visually indicate "something is selected"
            selectAllCheckbox.setSelected(true); 
            // Set indeterminate if some, but not all, are selected
            selectAllCheckbox.setIndeterminate(selectedCount > 0 && selectedCount < totalItems);
        }
    }

    /**
     * Shows or hides the fan table and a "no results" label based on data presence.
     */
    private void updateTableViewVisibility() {
        ApplicationLauncher.logger.debug("ReportGeneratorController: updateTableViewVisibility: Called.");
        boolean hasResults = !fanTableView.getItems().isEmpty();
        fanTableView.setVisible(hasResults);
        noResultsLabel.setVisible(!hasResults);
        selectAllCheckbox.setDisable(!hasResults); // Disable select all if no results
        ApplicationLauncher.logger.info("ReportGeneratorController: updateTableViewVisibility: Table visible: " + hasResults + ", No Results Label visible: " + !hasResults);
    }

    /**
     * Helper method to show a JavaFX Alert dialog.
     * @param type The type of the alert (e.g., WARNING, INFORMATION).
     * @param title The title of the alert dialog.
     * @param message The message content of the alert.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        ApplicationLauncher.logger.info("ReportGeneratorController: showAlert: Showing Alert: [" + type + "] " + title + " - " + message);
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

        /**
         * Returns the display name of the template (filename without .xlsx extension).
         */
        public String getDisplayName() {
            String fileName = getName();
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex > 0) {
                return fileName.substring(0, dotIndex);
            }
            return fileName;
        }

        public File getXlsxFile() {
            return xlsxFile;
        }

        public File getImageFile() {
            return imageFile;
        }
    }
    
    /**
     * Converts camelCase or any string into a title-cased string with spaces.
     * Example: "modelCell" → "Model", "airVolumeCell" → "Air Volume"
     */
    private String toTitleCase(String input) {
        if (input == null || input.isEmpty()) return input;

        return Arrays.stream(input.replace("Cell", "").split("(?=[A-Z])"))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

}
