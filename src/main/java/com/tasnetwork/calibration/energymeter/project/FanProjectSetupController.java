package com.tasnetwork.calibration.energymeter.project;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import com.sun.glass.ui.EventLoop.State;
import com.sun.xml.bind.v2.runtime.unmarshaller.Loader;
import com.tasnetwork.calibration.energymeter.ApplicationHomeController;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.device.Data_PowerSourceSetTarget;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.testprofiles.TestCaseData;
import com.tasnetwork.calibration.energymeter.testreport.ExcelCellValueModel;
import com.tasnetwork.calibration.energymeter.util.EditCell;
import com.tasnetwork.calibration.energymeter.util.ErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.util.MyIntegerStringConverter;
import com.tasnetwork.calibration.energymeter.util.TextFieldValidation;
import com.tasnetwork.spring.orm.model.DutMasterData;
import com.tasnetwork.spring.orm.model.FanTestSetup;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FanProjectSetupController implements Initializable {//extends AnchorPane{
	
	Timer saveOnClickTimer;
	DeviceDataManagerController displayDataObj = new DeviceDataManagerController();
	private TextFieldValidation textFieldValidation = new TextFieldValidation();
	
	private static AtomicInteger serialNoAtomic = new AtomicInteger(1);

    @FXML
    private Button addTestPointButton;

    @FXML
    private ComboBox<String> cmbBxModelName;
    static public ComboBox<String> ref_cmbBxModelName;
    
    @FXML
    private RadioButton radioBtn1Phase;

    @FXML
    private RadioButton radioBtn3Phase;
    
    @FXML
    private TextField txtDisplayAreaOfOpening;

    @FXML
    private TextField txtDisplayWindSpeedConfig;
    
    private String modelPhase = "";
    private String modelWindSpeedConfig = "";
    private String modelAreaOfOpening = "";
    
    /*@FXML
    private TextField txtWindSpeedConfig;
    static private TextField ref_txtWindSpeedConfig;
    
    @FXML
    private TextField txtAreaOfOpening;
    static private TextField ref_txtAreaOfOpening;*/

    @FXML
    private TableColumn colTestSetupActive;
    
    TableColumn<FanTestSetup, Void> dragCol = new TableColumn<>("");
  
    @FXML
    private TableColumn<FanTestSetup, String> colTestSetupCurrentLowerLimit;

    @FXML
    private TableColumn<FanTestSetup, String> colTestSetupCurrentUpperLimit;

    @FXML
    private TableColumn<FanTestSetup, String> colTestSetupPfLowerLimit;

    @FXML
    private TableColumn<FanTestSetup, String> colTestSetupPfUpperLimit;

    @FXML
    private TableColumn<FanTestSetup, String> colTestSetupRpmLowerLimit;

    @FXML
    private TableColumn<FanTestSetup, String> colTestSetupRpmUpperLimit;

    @FXML
    private TableColumn<FanTestSetup, Integer> colTestSetupSerialNo;

    @FXML
    private TableColumn<FanTestSetup, String> colTestSetupTargetVoltage;

    @FXML
    private TableColumn<FanTestSetup, String> colTestSetupTestPointName;

    @FXML
    private TableColumn<FanTestSetup, Integer> colTestSetupTimeInSec;

    @FXML
    private TableColumn<FanTestSetup, String> colTestSetupActivePowerLowerLimit;

    @FXML
    private TableColumn<FanTestSetup, String> colTestSetupActivePowerUpperLimit;

    @FXML
    private TableColumn<FanTestSetup, String> colTestSetupWattsLowerLimit;

    @FXML
    private TableColumn<FanTestSetup, String> colTestSetupWattsUpperLimit;

    @FXML
    private TableColumn<FanTestSetup, String> colTestSetupWindSpeedLowerLimit;

    @FXML
    private TableColumn<FanTestSetup, String> colTestSetupWindSpeedUpperLimit;


    @FXML
    private TableView<FanTestSetup> tvTestSetup;
    static private TableView<FanTestSetup> ref_tvTestSetup;

    @FXML
    private Button saveButton;



    @FXML
    private TextField txtNewModelName;
    static private TextField ref_txtNewModelName;


    private final Popup validationPopup = new Popup();
    private final Text validationText = new Text();

/*    public FanProjectSetupController() {

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/project/FanProjectSetup" + ConstantApp.THEME_FXML));

		fxmlLoader.setRoot(this); 
		fxmlLoader.setController(this);

		try { 
			fxmlLoader.load();

		} catch (IOException exception) {
			exception.printStackTrace();
			ApplicationLauncher.logger.error("FanProjectSetupController: IOException:"+exception.getMessage());
			throw new RuntimeException(exception);
		}
	}*/
    
/*    //@FXML
    public void initialize() {
    	ApplicationLauncher.logger.debug("FanProjectSetupController: initialize: " );
		// TODO Auto-generated method stub
		refAssignment();
		guiInit();
		loadDataFromDb();
	}*/
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
    	ApplicationLauncher.logger.debug("FanProjectSetupController: initialize: " );
		// TODO Auto-generated method stub
		refAssignment();
		guiInit();
		loadDataFromDb();
	}

    private void guiInit() {
    	
    	dragCol.setPrefWidth(30); // Set width for the grip
        dragCol.setCellFactory(col -> new TableCell<FanTestSetup, Void>() {
            private final Label dragHandle = new Label("☰"); // You can also use ⋮⋮ or a small icon

            {
                dragHandle.setStyle("-fx-font-size: 14px; -fx-text-fill: gray; -fx-cursor: move;");
                dragHandle.setOnDragDetected(event -> {
                    TableRow<FanTestSetup> row = getTableRow();
                    if (!row.isEmpty()) {
                        Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                        ClipboardContent cc = new ClipboardContent();
                        cc.putString(Integer.toString(row.getIndex()));
                        db.setContent(cc);
                        event.consume();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(dragHandle);
                }
            }
        });
        
        ref_tvTestSetup.getColumns().add(0, dragCol);
        
    	ref_tvTestSetup.setRowFactory(tv -> {
            TableRow<FanTestSetup> row = new TableRow<>();

            MenuItem addItem = new MenuItem("Add Test Point");
            addItem.setOnAction(e -> addTestPoint(row.getIndex()));

            MenuItem deleteItem = new MenuItem("Delete Test Point");
            deleteItem.setOnAction(e -> deleteTestPoint(row.getItem()));

            // Add separator
            SeparatorMenuItem separator = new SeparatorMenuItem();

            MenuItem clearItem = new MenuItem("Clear Limits");
            clearItem.setOnAction(e -> clearLimits(row.getItem()));

            MenuItem pasteAll = new MenuItem("Paste to All Limits");
            pasteAll.setOnAction(e -> pasteToLimits(row.getItem(), true, true));

            MenuItem pasteLower = new MenuItem("Paste to Lower Limits");
            pasteLower.setOnAction(e -> pasteToLimits(row.getItem(), true, false));

            MenuItem pasteUpper = new MenuItem("Paste to Upper Limits");
            pasteUpper.setOnAction(e -> pasteToLimits(row.getItem(), false, true));

            ContextMenu contextMenu = new ContextMenu(
            	    addItem,
            	    deleteItem,
            	    separator,
            	    clearItem,
            	    pasteAll,
            	    pasteLower,
            	    pasteUpper
            	);
            
            row.setContextMenu(contextMenu);

         // --- Drag and Drop Support ---
            row.setOnDragDetected(event -> {
                if (!row.isEmpty()) {
                    Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent cc = new ClipboardContent();
                    cc.putString(Integer.toString(row.getIndex()));
                    db.setContent(cc);
                    event.consume();
                }
            });

            row.setOnDragOver(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasString()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                    event.consume();
                }
            });

            row.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasString()) {
                    int draggedIndex = Integer.parseInt(db.getString());
                    FanTestSetup draggedItem = ref_tvTestSetup.getItems().remove(draggedIndex);

                    int dropIndex = row.isEmpty() ? ref_tvTestSetup.getItems().size() : row.getIndex();

                    ref_tvTestSetup.getItems().add(dropIndex, draggedItem);
                    refreshSerialNumbers(); // optional: reassign serials
                    ref_tvTestSetup.getSelectionModel().select(dropIndex);
                    event.setDropCompleted(true);
                    event.consume();
                }
            });

            return row;
        });
    	
    	// Style the validation popup
        StackPane popupContent = new StackPane(validationText);
        popupContent.setBackground(new Background(new BackgroundFill(Color.web("#ffefef"), new CornerRadii(8), Insets.EMPTY)));
        popupContent.setPadding(new Insets(6));
        popupContent.setStyle("-fx-border-color: red; -fx-border-width: 1px;");

        validationText.setFill(Color.RED);
        validationText.setFont(Font.font("Arial", 12));

        validationPopup.getContent().add(popupContent);
        validationPopup.setAutoHide(true); // hide if clicked elsewhere
        
        textFieldValidation.addModelNameValidation(txtNewModelName, "Model name");

    	
    	ref_cmbBxModelName.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
    	    if (newVal != null) {
    	        updateTestSetupTableForModel(newVal);
    	    }
    	    
    	    modelPhase = displayDataObj.getDutMasterDataService().findByModelName(newVal).getPhase();
		    modelAreaOfOpening = displayDataObj.getDutMasterDataService().findByModelName(newVal).getAreaOfOpening();
		    modelWindSpeedConfig = displayDataObj.getDutMasterDataService().findByModelName(newVal).getWindSpeedConfig();
		    
		    if (radioBtn1Phase != null && radioBtn3Phase != null && modelPhase != null) {
		        radioBtn1Phase.setSelected("1".equals(modelPhase));
		        radioBtn3Phase.setSelected("3".equals(modelPhase));
		    } else {
		        System.out.println("Null check failed: radioBtn1Phase=" + radioBtn1Phase +
		                           ", radioBtn3Phase=" + radioBtn3Phase + ", modelPhase=" + modelPhase);
		    }

		    
		    txtDisplayAreaOfOpening.setText(modelAreaOfOpening);
		    txtDisplayWindSpeedConfig.setText(modelWindSpeedConfig);
    	});
    	
    	// To be implemented :
    	// Add input validation for 
    	// Wind Speed Config - Number cannot be zero
    	// Area of Opening - number cannot be zero
    	FanProjectExecuteController fanProjectExecuteController  = new FanProjectExecuteController();
    	/*fanProjectExecuteController.addNumericRangeValidation(txtWindSpeedConfig, "Wind Speed Config");
    	fanProjectExecuteController.addNumericRangeValidation(txtAreaOfOpening, "Area of Opening");*/
    	
    	ref_tvTestSetup.setEditable(true);
    	
/*    	colPmPalletBatchNo.setCellValueFactory(data -> data.getValue().getPalletBatchNoProperty().asObject());
		colPmPalletActive.setStyle( "-fx-alignment: CENTER;");
		colPmPalletActive.setCellValueFactory(new PalletBayTestPalletActive_CheckBoxValueFactory());
		colPmPalletQrId.setCellValueFactory(data -> data.getValue().getPalletQrIdProperty());*/
		
		
		//colTestSetupRpmLowerLimit.setCellValueFactory(new PropertyValueFactory<ExcelCellValueModel, String>("cell_value"));FanTestSetup
		
    	colTestSetupSerialNo.setCellValueFactory(data -> data.getValue().getSerialNoProperty().asObject());
    	


    	colTestSetupActive.setStyle( "-fx-alignment: CENTER;");
    	colTestSetupActive.setCellValueFactory(new FanTestSetupActive_CheckBoxValueFactory());


    	colTestSetupTimeInSec.setCellValueFactory(data -> data.getValue().getSetupTimeInSecProperty().asObject());    	
    	colTestSetupTimeInSec.setCellFactory(EditCell.<FanTestSetup, Integer>forTableColumn(new MyIntegerStringConverter()));
    	colTestSetupTimeInSec.setOnEditCommit(new EventHandler<CellEditEvent<FanTestSetup, Integer>>() {
    		public void handle(CellEditEvent<FanTestSetup, Integer> t) {
    			FanTestSetup rowData = ((FanTestSetup) t.getTableView().getItems().get(t.getTablePosition().getRow()));
    			try{
    				rowData.setSetupTimeInSec(t.getNewValue());
    			} catch (Exception e) {
    				
    				e.printStackTrace();
    				ApplicationLauncher.logger.error("FanTestSetup : Init : setSetupTimeInSec: Exception: " + e.getMessage());
    				ApplicationLauncher.InformUser("Incorrect number", "Kindly enter valid input number for setup time" ,AlertType.ERROR);
    				rowData.setSetupTimeInSec(t.getOldValue());
    			}
    		}
    	});


    	colTestSetupTestPointName.setCellValueFactory(data -> data.getValue().getTestPointNameProperty());
    	colTestSetupTestPointName.setCellFactory(TextFieldTableCell.forTableColumn());
    	colTestSetupTestPointName.setOnEditCommit(new EventHandler<CellEditEvent<FanTestSetup, String>>() {
    		public void handle(CellEditEvent<FanTestSetup, String> t) {
    			FanTestSetup rowData = ((FanTestSetup) t.getTableView().getItems().get(t.getTablePosition().getRow()));
    			if(t.getNewValue() != null){

    				rowData.setTestPointName(t.getNewValue());
    				ref_tvTestSetup.refresh();

    			}
    		}
    	});

    	colTestSetupTargetVoltage.setCellValueFactory(data -> data.getValue().getTargetVoltageProperty());
    	colTestSetupTargetVoltage.setCellFactory(TextFieldTableCell.forTableColumn());
    	colTestSetupTargetVoltage.setOnEditCommit(new EventHandler<CellEditEvent<FanTestSetup, String>>() {
    		public void handle(CellEditEvent<FanTestSetup, String> t) {
    			FanTestSetup rowData = ((FanTestSetup) t.getTableView().getItems().get(t.getTablePosition().getRow()));
    			if(t.getNewValue() != null){

    				rowData.setTargetVoltage(t.getNewValue());
    				ref_tvTestSetup.refresh();

    			}
    		}
    	});

    	colTestSetupRpmLowerLimit.setCellValueFactory(data -> data.getValue().getRpmLowerLimitProperty());
    	colTestSetupRpmLowerLimit.setCellFactory(TextFieldTableCell.forTableColumn());
    	colTestSetupRpmLowerLimit.setOnEditCommit(new EventHandler<CellEditEvent<FanTestSetup, String>>() {
    		public void handle(CellEditEvent<FanTestSetup, String> t) {
    			FanTestSetup rowData = ((FanTestSetup) t.getTableView().getItems().get(t.getTablePosition().getRow()));
    			if(t.getNewValue() != null){

    				rowData.setRpmLowerLimit(t.getNewValue());
    				ref_tvTestSetup.refresh();

    			}
			}
		});
		
		
		colTestSetupRpmUpperLimit.setCellValueFactory(data -> data.getValue().getRpmUpperLimitProperty());
		colTestSetupRpmUpperLimit.setCellFactory(TextFieldTableCell.forTableColumn());
		colTestSetupRpmUpperLimit.setOnEditCommit(new EventHandler<CellEditEvent<FanTestSetup, String>>() {
			public void handle(CellEditEvent<FanTestSetup, String> t) {
				FanTestSetup rowData = ((FanTestSetup) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				if(t.getNewValue() != null){
					
					rowData.setRpmUpperLimit(t.getNewValue());
					ref_tvTestSetup.refresh();

				}
			}
		});
		
		
		colTestSetupWindSpeedLowerLimit.setCellValueFactory(data -> data.getValue().getWindSpeedLowerLimitProperty());
		colTestSetupWindSpeedLowerLimit.setCellFactory(TextFieldTableCell.forTableColumn());
		colTestSetupWindSpeedLowerLimit.setOnEditCommit(new EventHandler<CellEditEvent<FanTestSetup, String>>() {
			public void handle(CellEditEvent<FanTestSetup, String> t) {
				FanTestSetup rowData = ((FanTestSetup) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				if(t.getNewValue() != null){
					
					rowData.setWindSpeedLowerLimit(t.getNewValue());
					ref_tvTestSetup.refresh();

				}
			}
		});
		
		
		colTestSetupWindSpeedUpperLimit.setCellValueFactory(data -> data.getValue().getWindSpeedUpperLimitProperty());
		colTestSetupWindSpeedUpperLimit.setCellFactory(TextFieldTableCell.forTableColumn());
		colTestSetupWindSpeedUpperLimit.setOnEditCommit(new EventHandler<CellEditEvent<FanTestSetup, String>>() {
			public void handle(CellEditEvent<FanTestSetup, String> t) {
				FanTestSetup rowData = ((FanTestSetup) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				if(t.getNewValue() != null){
					
					rowData.setWindSpeedUpperLimit(t.getNewValue());
					ref_tvTestSetup.refresh();

				}
			}
		});
		
		
		colTestSetupWattsLowerLimit.setCellValueFactory(data -> data.getValue().getWattsLowerLimitProperty());
		colTestSetupWattsLowerLimit.setCellFactory(TextFieldTableCell.forTableColumn());
		colTestSetupWattsLowerLimit.setOnEditCommit(new EventHandler<CellEditEvent<FanTestSetup, String>>() {
			public void handle(CellEditEvent<FanTestSetup, String> t) {
				FanTestSetup rowData = ((FanTestSetup) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				if(t.getNewValue() != null){
					
					rowData.setWattsLowerLimit(t.getNewValue());
					ref_tvTestSetup.refresh();

				}
			}
		});
		
		
		colTestSetupWattsUpperLimit.setCellValueFactory(data -> data.getValue().getWattsUpperLimitProperty());
		colTestSetupWattsUpperLimit.setCellFactory(TextFieldTableCell.forTableColumn());
		colTestSetupWattsUpperLimit.setOnEditCommit(new EventHandler<CellEditEvent<FanTestSetup, String>>() {
			public void handle(CellEditEvent<FanTestSetup, String> t) {
				FanTestSetup rowData = ((FanTestSetup) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				if(t.getNewValue() != null){
					
					rowData.setWattsUpperLimit(t.getNewValue());
					ref_tvTestSetup.refresh();

				}
			}
		});
		
		
		colTestSetupActivePowerLowerLimit.setCellValueFactory(data -> data.getValue().getActivePowerLowerLimitProperty());
		colTestSetupActivePowerLowerLimit.setCellFactory(TextFieldTableCell.forTableColumn());
		colTestSetupActivePowerLowerLimit.setOnEditCommit(new EventHandler<CellEditEvent<FanTestSetup, String>>() {
			public void handle(CellEditEvent<FanTestSetup, String> t) {
				FanTestSetup rowData = ((FanTestSetup) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				if(t.getNewValue() != null){
					
					rowData.setActivePowerLowerLimit(t.getNewValue());
					ref_tvTestSetup.refresh();

				}
			}
		});
		
		
		colTestSetupActivePowerUpperLimit.setCellValueFactory(data -> data.getValue().getActivePowerUpperLimitProperty());
		colTestSetupActivePowerUpperLimit.setCellFactory(TextFieldTableCell.forTableColumn());
		colTestSetupActivePowerUpperLimit.setOnEditCommit(new EventHandler<CellEditEvent<FanTestSetup, String>>() {
			public void handle(CellEditEvent<FanTestSetup, String> t) {
				FanTestSetup rowData = ((FanTestSetup) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				if(t.getNewValue() != null){
					
					rowData.setActivePowerUpperLimit(t.getNewValue());
					ref_tvTestSetup.refresh();

				}
			}
		});

		
		colTestSetupPfLowerLimit.setCellValueFactory(data -> data.getValue().getPowerFactorLowerLimitProperty());
		colTestSetupPfLowerLimit.setCellFactory(TextFieldTableCell.forTableColumn());
		colTestSetupPfLowerLimit.setOnEditCommit(new EventHandler<CellEditEvent<FanTestSetup, String>>() {
			public void handle(CellEditEvent<FanTestSetup, String> t) {
				FanTestSetup rowData = ((FanTestSetup) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				if(t.getNewValue() != null){
					
					rowData.setPowerFactorLowerLimit(t.getNewValue());
					ref_tvTestSetup.refresh();

				}
			}
		});
		
		
		colTestSetupPfUpperLimit.setCellValueFactory(data -> data.getValue().getPowerFactorUpperLimitProperty());
		colTestSetupPfUpperLimit.setCellFactory(TextFieldTableCell.forTableColumn());
		colTestSetupPfUpperLimit.setOnEditCommit(new EventHandler<CellEditEvent<FanTestSetup, String>>() {
			public void handle(CellEditEvent<FanTestSetup, String> t) {
				FanTestSetup rowData = ((FanTestSetup) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				if(t.getNewValue() != null){
					
					rowData.setPowerFactorUpperLimit(t.getNewValue());
					ref_tvTestSetup.refresh();

				}
			}
		});
		
		
		colTestSetupCurrentLowerLimit.setCellValueFactory(data -> data.getValue().getCurrentLowerLimitProperty());
		colTestSetupCurrentLowerLimit.setCellFactory(TextFieldTableCell.forTableColumn());
		colTestSetupCurrentLowerLimit.setOnEditCommit(new EventHandler<CellEditEvent<FanTestSetup, String>>() {
			public void handle(CellEditEvent<FanTestSetup, String> t) {
				FanTestSetup rowData = ((FanTestSetup) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				if(t.getNewValue() != null){
					
					rowData.setCurrentLowerLimit(t.getNewValue());
					ref_tvTestSetup.refresh();

				}
			}
		});
		
		
		colTestSetupCurrentUpperLimit.setCellValueFactory(data -> data.getValue().getCurrentUpperLimitProperty());
		colTestSetupCurrentUpperLimit.setCellFactory(TextFieldTableCell.forTableColumn());
		colTestSetupCurrentUpperLimit.setOnEditCommit(new EventHandler<CellEditEvent<FanTestSetup, String>>() {
			public void handle(CellEditEvent<FanTestSetup, String> t) {
				FanTestSetup rowData = ((FanTestSetup) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				if(t.getNewValue() != null){
					
					rowData.setCurrentUpperLimit(t.getNewValue());
					ref_tvTestSetup.refresh();

				}
			}
		});
		
	}
    
    
    /*private void updateTestSetupTableForModel(String selectedModelName) {
        List<DutMasterData> dutMasterDataList = displayDataObj.getDutMasterDataService().findAll();

        Optional<DutMasterData> selectedDutOpt = dutMasterDataList.stream()
                .filter(d -> d.getModelName().equals(selectedModelName))
                .findFirst();

        selectedDutOpt.ifPresent(dut -> {
            List<FanTestSetup> fanList = dut.getFanTestSetupList();
            if (fanList != null) {
                ref_tvTestSetup.getItems().clear();
                ref_tvTestSetup.getItems().addAll(fanList);
                serialNoAtomic.set(fanList.size() + 1);
            }
        });
    }*/

    // Sort by serial number
    private void updateTestSetupTableForModel(String selectedModelName) {
        List<DutMasterData> dutMasterDataList = displayDataObj.getDutMasterDataService().findAll();

        Optional<DutMasterData> selectedDutOpt = dutMasterDataList.stream()
                .filter(d -> d.getModelName().equals(selectedModelName))
                .findFirst();

        selectedDutOpt.ifPresent(dut -> {
            List<FanTestSetup> fanList = dut.getFanTestSetupList();
            if (fanList != null) {
                // Sort the list by serial number
                fanList.sort(Comparator.comparingInt(FanTestSetup::getSerialNo)); // Assuming getSerialNo() returns int

                ref_tvTestSetup.getItems().clear();
                ref_tvTestSetup.getItems().addAll(fanList);
                serialNoAtomic.set(fanList.size() + 1);
            }
        });
    }


	private void loadDataFromDb() {
		// TODO Auto-generated method stub
    	List<DutMasterData> dutMasterDataList = displayDataObj.getDutMasterDataService().findAll();
    	List<String> modelList = new ArrayList<String>();
    	modelList = dutMasterDataList.stream().map(e->e.getModelName()).collect(Collectors.toList());
    	if (!modelList.isEmpty()) {
            ref_cmbBxModelName.getItems().clear();
            ref_cmbBxModelName.getItems().addAll(modelList);
            ref_cmbBxModelName.getSelectionModel().clearSelection();

            String selectedModel = ref_cmbBxModelName.getSelectionModel().getSelectedItem();

            Optional<DutMasterData> selectedDutOpt = dutMasterDataList.stream()
                    .filter(d -> d.getModelName().equals(selectedModel))
                    .findFirst();

            selectedDutOpt.ifPresent(dut -> {
                List<FanTestSetup> fanList = dut.getFanTestSetupList();
                if (fanList != null) {
                    ref_tvTestSetup.getItems().clear();
                    ref_tvTestSetup.getItems().addAll(fanList);
                    serialNoAtomic.set(fanList.size()+1);
                }else {
                	serialNoAtomic.set(1);
                }
            });
        }
	}

	private void refAssignment() {
		// TODO Auto-generated method stub
    	ref_txtNewModelName = txtNewModelName;
    	ref_cmbBxModelName = cmbBxModelName;
    	/*ref_txtWindSpeedConfig = txtWindSpeedConfig;
    	ref_txtAreaOfOpening = txtAreaOfOpening;*/
    	ref_tvTestSetup = tvTestSetup;
	}

	@FXML
	void btnAddModelOnClick(ActionEvent event) {
	    String modelName = ref_txtNewModelName.getText();
	    ApplicationLauncher.logger.debug("displayDataObj: modelName: " + modelName);

	    // Perform model name validation using TextFieldValidation
	    String validationMessage = validateModelName(modelName);
	    if (validationMessage != null) {
	        // Show validation message and stop further processing
	        Alert alert = new Alert(Alert.AlertType.WARNING);
	        alert.setTitle("Invalid Model Name");
	        alert.setHeaderText(null);
	        alert.setContentText(validationMessage);
	        alert.showAndWait();
	        return; // Exit the method and do not proceed further
	    }

	    // Check if the model name already exists in the database
	    DutMasterData existingModel = displayDataObj.getDutMasterDataService().findByModelName(modelName);
	    if (existingModel != null) {
	        // Model already exists, show an alert
	        Alert alert = new Alert(Alert.AlertType.WARNING);
	        alert.setTitle("Model Already Exists");
	        alert.setHeaderText(null);
	        alert.setContentText("The model name \"" + modelName + "\" already exists. Please choose a different name.");
	        alert.showAndWait();
	        return; // Exit the method to prevent adding the model
	    }

	    // If model doesn't exist, create and save the new model
	    DutMasterData dutMasterDataObj = new DutMasterData();
	    dutMasterDataObj.setActive(true);
	    dutMasterDataObj.setModelName(modelName);
	    dutMasterDataObj.setDutBaseName("");
	    dutMasterDataObj.setDutType("");
	    dutMasterDataObj.setRatedCurrent("");
	    dutMasterDataObj.setRatedVoltage("");

	    // Open the ConfigurationPanel in a non-blocking popup
	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/project/ConfigurationPanel" + ConstantApp.THEME_FXML));
	        Parent root = loader.load();

	        ConfigurationPanelController controller = loader.getController();
	        controller.setInitialModelName(modelName);
	        controller.setDisplayDataObj(displayDataObj); // To access DB for saving

	        Stage stage = new Stage();
	        stage.setTitle("Configuration Panel");
	        stage.setScene(new Scene(root));
	        stage.initModality(Modality.NONE); // NON - BLOCKING
	        stage.initStyle(StageStyle.DECORATED); // Optional Styling
	        stage.show();
	    } catch (IOException e) {
	        ApplicationLauncher.logger.error("Failed to load ConfigurationPanel_W.fxml", e);
	    }
	    
	    /*// Save the new model to the database
	    displayDataObj.getDutMasterDataService().saveToDb(dutMasterDataObj);

	    // Add model to the ComboBox
	    ref_cmbBxModelName.getItems().add(modelName);
	    ref_cmbBxModelName.getSelectionModel().select(modelName);*/
	}

	private String validateModelName(String modelName) {
	    // Implement validation logic for the model name
	    if (modelName.isEmpty()) {
	        return "Model name cannot be empty.";
	    } else if (!modelName.matches("^[A-Za-z_][A-Za-z0-9_]*$")) {
	        if (!Character.isLetter(modelName.charAt(0)) && modelName.charAt(0) != '_') {
	            return "First character must be a letter or underscore.";
	        } else if (modelName.contains(" ")) {
	            return "Spaces are not allowed.";
	        } else if (!modelName.matches("[A-Za-z0-9_]*")) {
	            return "Only letters, digits, and underscores are allowed.";
	        }
	    }
	    return null; // No validation error
	}
	
	@FXML
	void btnDeleteModelOnClick(ActionEvent event) {
	    String selectedModel = ref_cmbBxModelName.getSelectionModel().getSelectedItem();

	    if (selectedModel == null || selectedModel.isEmpty()) {
	        Alert alert = new Alert(Alert.AlertType.WARNING);
	        alert.setTitle("No Model Selected");
	        alert.setHeaderText(null);
	        alert.setContentText("Please select a model to delete.");
	        alert.showAndWait();
	        return;
	    }

	    // Confirm before deleting
	    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
	    confirm.setTitle("Delete Confirmation");
	    confirm.setHeaderText("Are you sure you want to delete this model?");
	    confirm.setContentText("Model: " + selectedModel);
	    
	    Optional<ButtonType> result = confirm.showAndWait();
	    if (result.isPresent() && result.get() == ButtonType.OK) {
	        try {
	            // Delete from database
	            displayDataObj.getDutMasterDataService().deleteByModelName(selectedModel);

	            // Remove from ComboBox
	            ref_cmbBxModelName.getItems().remove(selectedModel);

	            // Optional: Clear selection
	            ref_cmbBxModelName.getSelectionModel().clearSelection();

	            ApplicationLauncher.logger.debug("Deleted model: " + selectedModel);

	            Alert info = new Alert(Alert.AlertType.INFORMATION);
	            info.setTitle("Model Deleted");
	            info.setHeaderText(null);
	            info.setContentText("Model \"" + selectedModel + "\" deleted successfully.");
	            info.showAndWait();

	        } catch (Exception e) {
	            ApplicationLauncher.logger.error("Error deleting model: " + selectedModel, e);
	            Alert error = new Alert(Alert.AlertType.ERROR);
	            error.setTitle("Error");
	            error.setHeaderText("Could not delete the model.");
	            error.setContentText("Please check logs for more details.");
	            error.showAndWait();
	        }
	    }
	}

	
	@FXML
	void btnAddTestPointOnClick(ActionEvent event) {
		
		FanTestSetup fanTestSetup = new FanTestSetup();
		fanTestSetup.setTestPointName("TestName"+serialNoAtomic.get());
		fanTestSetup.setSerialNo(serialNoAtomic.getAndIncrement());
		fanTestSetup.setActive(true);
		fanTestSetup.setRpmLowerLimit("RpmLow");
		fanTestSetup.setRpmUpperLimit("RpmHigh");
		
		fanTestSetup.setSetupTimeInSec(20);
		fanTestSetup.setTargetVoltage("240.5");
		fanTestSetup.setWindSpeedLowerLimit("WsLower");
		fanTestSetup.setWattsLowerLimit("WattLower");
		fanTestSetup.setPowerFactorLowerLimit("PfLower");
		fanTestSetup.setActivePowerLowerLimit("ApLower");
		fanTestSetup.setCurrentLowerLimit("I-Lower");
		
		fanTestSetup.setWindSpeedUpperLimit("WsUpper");
		fanTestSetup.setWattsUpperLimit("WattsUpper");
		fanTestSetup.setPowerFactorUpperLimit("PfUpper");
		fanTestSetup.setActivePowerUpperLimit("ApUpper");
		fanTestSetup.setCurrentUpperLimit("I-Upper");
		
		ref_tvTestSetup.getItems().add(fanTestSetup);
	}
	
	@FXML
	void btnDeleteTestPointOnClick (ActionEvent event) {
		TableRow<FanTestSetup> row = new TableRow<>();
		ref_tvTestSetup.getItems().remove(row.getItem());
	}
	

	
	
	@FXML
	public void btnSaveOnClick() {

		ApplicationLauncher.logger.info("btnSaveOnClick: Entry");
		saveOnClickTimer = new Timer();
		saveOnClickTimer.schedule(new SaveTaskClick(), 50);


	}

	class SaveTaskClick extends TimerTask {
		public void run() {
			saveToDbTask();
			saveOnClickTimer.cancel();


		}
	}

	public void saveToDbTask() {
		
		ApplicationLauncher.logger.debug("saveToDbTask: Entry");
		boolean status = false;		
		String selectedModel = ref_cmbBxModelName.getSelectionModel().getSelectedItem();
		//String windSpeedConfig = 

		DutMasterData selectedDut = displayDataObj.getDutMasterDataService()
		    .findAll()
		    .stream()
		    .filter(d -> d.getModelName().equals(selectedModel))
		    .findFirst()
		    .orElse(null);

		if (selectedDut == null) {
		    ApplicationLauncher.logger.warn("No DutMasterData found for model: " + selectedModel);
		    return;
		}
		
		/*for (FanTestSetup setup : ref_tvTestSetup.getItems()) {
		    selectedDut.addFanTestSetup(setup);
		}*/
		
		// Clear old test setups
	    selectedDut.getFanTestSetupList().clear();

	    // Add updated ones
	    for (FanTestSetup setup : ref_tvTestSetup.getItems()) {
	        setup.setDutMasterData(selectedDut); // Important!
	        selectedDut.getFanTestSetupList().add(setup);
	    }
		
		displayDataObj.getDutMasterDataService().saveToDb(selectedDut);
	    ApplicationLauncher.logger.info("Saved FanTestSetup list to model: " + selectedModel);
	    ApplicationLauncher.InformUser("Setup Saved","Test Setup saved succesfully", AlertType.INFORMATION);
	}
	
	// =================== RIGHT - CLICK MENU HELPER  =======================================
	private void addTestPoint(int index) {
	    FanTestSetup previous = index >= 0 && index < ref_tvTestSetup.getItems().size()
	        ? ref_tvTestSetup.getItems().get(index)
	        : null;

	    FanTestSetup fanTestSetup = new FanTestSetup();
	    fanTestSetup.setTestPointName("TestName" + serialNoAtomic.get());
	    fanTestSetup.setSerialNo(serialNoAtomic.getAndIncrement());
	    fanTestSetup.setActive(true);
	    fanTestSetup.setSetupTimeInSec(20);
	    fanTestSetup.setTargetVoltage("240.5");

	    if (previous != null) {
	        fanTestSetup.setRpmLowerLimit(previous.getRpmLowerLimit());
	        fanTestSetup.setRpmUpperLimit(previous.getRpmUpperLimit());
	        fanTestSetup.setWindSpeedLowerLimit(previous.getWindSpeedLowerLimit());
	        fanTestSetup.setWattsLowerLimit(previous.getWattsLowerLimit());
	        fanTestSetup.setPowerFactorLowerLimit(previous.getPowerFactorLowerLimit());
	        fanTestSetup.setActivePowerLowerLimit(previous.getActivePowerLowerLimit());
	        fanTestSetup.setCurrentLowerLimit(previous.getCurrentLowerLimit());

	        fanTestSetup.setWindSpeedUpperLimit(previous.getWindSpeedUpperLimit());
	        fanTestSetup.setWattsUpperLimit(previous.getWattsUpperLimit());
	        fanTestSetup.setPowerFactorUpperLimit(previous.getPowerFactorUpperLimit());
	        fanTestSetup.setActivePowerUpperLimit(previous.getActivePowerUpperLimit());
	        fanTestSetup.setCurrentUpperLimit(previous.getCurrentUpperLimit());
	    }

	    ref_tvTestSetup.getItems().add(index + 1, fanTestSetup);
	}

	private void deleteTestPoint(FanTestSetup point) {
	    ref_tvTestSetup.getItems().remove(point);
	}

	private void clearLimits(FanTestSetup point) {
	    point.setRpmLowerLimit("");
	    point.setRpmUpperLimit("");
	    point.setWindSpeedLowerLimit("");
	    point.setWattsLowerLimit("");
	    point.setPowerFactorLowerLimit("");
	    point.setActivePowerLowerLimit("");
	    point.setCurrentLowerLimit("");

	    point.setWindSpeedUpperLimit("");
	    point.setWattsUpperLimit("");
	    point.setPowerFactorUpperLimit("");
	    point.setActivePowerUpperLimit("");
	    point.setCurrentUpperLimit("");
	    
	    ref_tvTestSetup.refresh();
	}

	private void pasteToLimits(FanTestSetup point, boolean lower, boolean upper) {
	    Clipboard clipboard = Clipboard.getSystemClipboard();
	    if (clipboard.hasString()) {
	        String val = clipboard.getString();
	        if (lower) {
	            point.setRpmLowerLimit(val);
	            point.setWindSpeedLowerLimit(val);
	            point.setWattsLowerLimit(val);
	            point.setPowerFactorLowerLimit(val);
	            point.setActivePowerLowerLimit(val);
	            point.setCurrentLowerLimit(val);
	        }
	        if (upper) {
	            point.setRpmUpperLimit(val);
	            point.setWindSpeedUpperLimit(val);
	            point.setWattsUpperLimit(val);
	            point.setPowerFactorUpperLimit(val);
	            point.setActivePowerUpperLimit(val);
	            point.setCurrentUpperLimit(val);
	        }
	    }
	    
	    ref_tvTestSetup.refresh();
	}
	
	private void refreshSerialNumbers() {
	    int serial = 1;
	    for (FanTestSetup setup : ref_tvTestSetup.getItems()) {
	        setup.setSerialNo(serial++);
	    }
	}

	public static ComboBox<String> getRef_cmbBxModelName() {
		return ref_cmbBxModelName;
	}

	public static void setRef_cmbBxModelName(ComboBox<String> ref_cmbBxModelName) {
		FanProjectSetupController.ref_cmbBxModelName = ref_cmbBxModelName;
	}

}
