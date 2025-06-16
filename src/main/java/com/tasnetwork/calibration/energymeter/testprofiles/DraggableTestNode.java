
package com.tasnetwork.calibration.energymeter.testprofiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.database.MySQL_Controller;
import com.tasnetwork.calibration.energymeter.project.ProjectController;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DraggableTestNode extends AnchorPane {

	@FXML AnchorPane draggableTestNodePane;

	@FXML AnchorPane testScriptPropertyChildPane;

	private EventHandler <DragEvent> mContextDragOver;
	private EventHandler <DragEvent> mContextDragDropped;

	@FXML private Label draggableNodeTitle;
	@FXML private Label draggableNodeCloseButton;

	private TestProfileType mType = null;
	private Point2D mDragOffset = new Point2D (0.0, 0.0);
	private String aliasId;
	private String aliasName;
	private int positionId;
	private int nodeIndex;
	private Boolean NewNode;
	private String ProjectName = null;
	private final DraggableTestNode self;

	private String WarmupTimeDuration= null;
	private String CreepUn = null;
	private String CreepIb = null;
	private String STAIb = null;
	private String STATestTime = null;
	private String STATestPulseNo = null;
	private String StdInput = null;
	private String StdLoad = null;
	private List<String> TestCaseNames = new ArrayList<String>();
	private List<String> Emin_list  = new ArrayList<String>();
	private List<String> Emax_list  = new ArrayList<String>();
	private List<String> Pulses_list  = new ArrayList<String>();
	private List<String> Time_list = new ArrayList<String>();
	private List<String> SkipReadingCount_list = new ArrayList<String>();
	private List<String> Deviation_list = new ArrayList<String>();
	private List<String> TestRunType_list = new ArrayList<String>();
	private List<String> Average_list  = new ArrayList<String>();

	private Object testPropertyController;
	private Object testPropertyController2;
	private ProjectController parentScriptController;

	public DraggableTestNode(AnchorPane propertyChildPane) {

		testScriptPropertyChildPane = propertyChildPane;
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/testprofile/DraggableTestNode" + ConstantApp.THEME_FXML));

		fxmlLoader.setRoot(this); 
		fxmlLoader.setController(this);

		self = this;

		try { 
			fxmlLoader.load();

		} catch (IOException exception) {
			exception.printStackTrace();
			ApplicationLauncher.logger.error(" DraggableTestNode : IOException: " + exception.getMessage());
			throw new RuntimeException(exception);
		}
	}

	@FXML
	private void initialize() {
		buildNodeDragHandlers();
		if((ConstantApp.USER_ACCESS_LEVEL.equals(ConstantApp.TESTER_ACCESS_LEVEL)) ||
				(ConstantApp.USER_ACCESS_LEVEL.equals(ConstantApp.READONLY_ACCESS_LEVEL))){
			draggableNodeCloseButton.setDisable(true);
		}

	}

	public String  getTitle() {
		return this.draggableNodeTitle.getText();
	}
	public void setTitle(String Input) {
		this.draggableNodeTitle.setText(Input);
		//this.draggableNodeTitle.setFont(Font  );

	}

	public void setParentScriptController(ProjectController Input){
		parentScriptController = Input;
	}

	public void setColor() {
		getStyleClass().clear();
		getStyleClass().add("dragicon");
		getStyleClass().add("icon-paleblue");
	}

	public void setHighLightColorToCurrentNode() {
		getStyleClass().clear();
		getStyleClass().add("dragicon");
		getStyleClass().add("icon-palegreen");
	}

	public String  getAliasId() {
		return this.aliasId;
	}
	public void setAliasId(String ID) {
		this.aliasId = ID;
	}

	public String  getAliasName() {
		return this.aliasName;
	}
	public void setAliasName(String name) {
		this.aliasName = name;
	}

	public int  getPositionId() {
		return this.positionId;
	}
	public void setPositionId(int posID) {
		this.positionId = posID;
	}

	public int  getNodeIndex() {
		return this.nodeIndex;
	}
	public void setNodeIndex(int index) {
		this.nodeIndex = index;
	}

	public Boolean  IsNewNode() {
		return this.NewNode;
	}
	public void NewNode(Boolean value) {
		this.NewNode = value;
	}

	public String getProjectName() {
		return ProjectName;
	}

	public void setProjectName(String Pro_name) {
		ProjectName = Pro_name;
	}

	public String getWarmupTimeDuration() {
		return WarmupTimeDuration;
	}

	public void setWarmupTimeDuration(String warmupTimeDuration) {
		WarmupTimeDuration = warmupTimeDuration;
	}

	public String getCreepUn() {
		return CreepUn;
	}

	public void setCreepUn(String CreepUnValue) {
		CreepUn = CreepUnValue;
	}

	public String getCreepIb() {
		return CreepIb;
	}

	public void setCreepIb(String CreepIbValue) {
		CreepIb = CreepIbValue;
	}

	public void setSTAIb(String STAIbValue) {
		STAIb = STAIbValue;
	}

	public String getSTAIb() {
		return STAIb;
	}

	public void setSTATestTime(String STATestTimeValue) {
		STATestTime = STATestTimeValue;
	}

	public String getSTATestTime() {
		return STATestTime;
	}

	public void setSTATestPulseNo(String STATestPulseNoValue) {
		STATestPulseNo = STATestPulseNoValue;
	}

	public String getSTATestPulseNo() {
		return STATestPulseNo;
	}

	public void setStdInput(String StdInputValue) {
		StdInput = StdInputValue;
	}

	public String getStdInput() {
		return StdInput;
	}

	public void setStdLoad(String StdLoadValue) {
		StdLoad = StdLoadValue;
	}

	public String getStdLoad() {
		return StdLoad;
	}

	public void setTestCaseNames(List<String> testcase){
		TestCaseNames = testcase;
	}

	public List<String> getTestCaseNames(){
		return TestCaseNames;
	}

	public void setEmin_list(List<String> emin){
		Emin_list = emin;
	}

	public List<String> getEmin_list(){
		return Emin_list;
	}

	public void setEmax_list(List<String> emax){
		Emax_list = emax;
	}

	public List<String> getEmax_list(){
		return Emax_list;
	}

	public void setPulses_list(List<String> pulses){
		Pulses_list = pulses;
	}

	public List<String> getPulses_list(){
		return Pulses_list;
	}

	public void setAverage_list(List<String> Average){
		Average_list = Average;
	}

	public List<String> getAverage_list(){
		return Average_list;
	}
	
	
	public void setTime_list(List<String> time){
		Time_list = time;
	}

	public List<String> getTime_list(){
		return Time_list;
	}

	public void setSkipReadingCount_list(List<String> SkipReadingCount){
		SkipReadingCount_list = SkipReadingCount;
	}

	public List<String> getSkipReadingCount_list(){
		return SkipReadingCount_list;
	}

	public void setDeviation_list(List<String> deviation){
		Deviation_list = deviation;
	}

	public List<String> getDeviation_list(){
		return Deviation_list;
	}

	public void setTestRunType_list(List<String> runtype){
		TestRunType_list = runtype;
	}

	public List<String> getTestRunType_list(){
		return TestRunType_list;
	}

	public void relocateToPoint (Point2D p) {

		//relocates the object to a point that has been converted to
		//scene coordinates
		Point2D localCoords = getParent().sceneToLocal(p);
		relocate (localCoords.getX(), localCoords.getY());
	}


	public TestProfileType getType () { 
		return mType; 
	}


	public void setType (TestProfileType type) {
		mType = type;
		ApplicationLauncher.logger.info ("setType: type: " + type);
		switch (mType) {

		case STA:
			//getStyleClass().clear();
			//getStyleClass().add("icon-lightblue");

			setTitle(ConstantApp.DISPLAY_TC_TITLE_STARTING_CURRENT);
			break;

		case Warmup:
			//getStyleClass().add("icon-red");	
			setTitle(ConstantApp.DISPLAY_TC_TITLE_WARMUP);
			break;

		case NoLoad:
			//getStyleClass().add("icon-lightgreen");
			setTitle(ConstantApp.DISPLAY_TC_TITLE_NOLOADTEST);
			break;

		case Accuracy:
			//getStyleClass().add("icon-grey");
			setTitle(ConstantApp.DISPLAY_TC_TITLE_ACCURACY);
			break;

		case InfluenceVolt:
			//getStyleClass().add("icon-purple");
			setTitle(ConstantApp.DISPLAY_TC_TITLE_INF_VOLTAGE);
			break;

		case InfluenceFreq:
			//getStyleClass().add("icon-yellow");
			setTitle(ConstantApp.DISPLAY_TC_TITLE_INF_FREQUENCY);
			break;


		case InfluenceHarmonic:
			//getStyleClass().add("icon-orange");
			setTitle(ConstantApp.DISPLAY_TC_TITLE_INF_HARMONICS);
			break;

		case CuttingNuetral:
			//getStyleClass().add("icon-white");
			setTitle(ConstantApp.DISPLAY_TC_TITLE_CUT_NUETRAL);
			break;

		case VoltageUnbalance:
			//getStyleClass().add("icon-cyan");
			setTitle(ConstantApp.DISPLAY_TC_TITLE_INF_VOLT_UNBALANCE);
			break;

		case PhaseReversal:
			//getStyleClass().add("icon-brown");
			setTitle(ConstantApp.DISPLAY_TC_TITLE_PHASE_REVERSAL);
			break;


		case Repeatability:
			//getStyleClass().add("icon-blue");
			setTitle(ConstantApp.DISPLAY_TC_TITLE_REPEATABLITY);
			break;

		case SelfHeating:
			//getStyleClass().add("icon-blue");
			setTitle(ConstantApp.DISPLAY_TC_TITLE_SELF_HEATING);
			break;

		case ConstantTest:
			//getStyleClass().add("icon-lightgrey");
			setTitle(ConstantApp.DISPLAY_TC_TITLE_CONST_TEST);
			break;

		case CustomTest:
			//getStyleClass().add("icon-lightgrey");
			setTitle(ConstantApp.DISPLAY_TC_TITLE_CUSTOM_TEST);
			break;
			
		case DutCommand:
			//getStyleClass().add("icon-lightgrey");
			setTitle(ConstantApp.DISPLAY_TC_TITLE_DUT_COMMAND);
			break;


		default:
			break;
		}
	}

	public void onDragNodeClick() {
		ApplicationLauncher.logger.info ("Draggable node -> on click event recieved...");

		String currentAliasId = getAliasId();
		TestProfileType nodetype = getType(); 
		ApplicationLauncher.logger.info("currentAliasId: "  + currentAliasId);
		ApplicationLauncher.logger.info("nodetype: "  + nodetype);

		if (!currentAliasId.equals("00")) {
			SetDefaultColorToAllNode();
			setHighLightColorToCurrentNode();

			try {
				unloadCurrentTestPropertyFXML();
				loadTestPropertyFXML(nodetype, currentAliasId, self);
				ApplicationLauncher.logger.info ("Draggable node -> on click event -> Loading FXML...");
			}
			catch (Exception e) {
				e.printStackTrace();
				ApplicationLauncher.logger.error(" onDragNodeClick : Exception: " + e.getMessage());
			}
		}
	}

	public void SetDefaultColorToAllNode(){
		List<DraggableTestNode> mNodeList = new ArrayList<DraggableTestNode>();
		mNodeList = ProjectController.getNodeList();
		for (int i=0;i<ProjectController.getNodeList().size();i++){
			mNodeList.get(i).getStyleClass().clear();
			mNodeList.get(i).getStyleClass().add("dragicon");
			mNodeList.get(i).getStyleClass().add("icon-paleblue");
		}
	}


	private Parent getNodeFromFXML(String url) throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
		Parent parentNode = loader.load();

		ApplicationLauncher.logger.info("Loaded property UI: " + parentNode);
		testPropertyController = loader.getController();
		return parentNode;
	}

	private Parent getNodeFromFXML2(String url) throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
		Parent parentNode = loader.load();

		ApplicationLauncher.logger.info("Loaded property UI: " + parentNode);
		testPropertyController2 = loader.getController();
		return parentNode;
	}



	public void loadTestPropertyFXML(TestProfileType nodeType, String currentAliasId, DraggableTestNode SelectedNode) throws IOException {
		ApplicationLauncher.logger.info("Draggable node -> loading child property for " + nodeType);

		Parent nodeFromFXML = null;

		nodeFromFXML = getNodeFromFXML("/fxml/testprofile/PropertyInfluence" + ConstantApp.THEME_FXML);
		testScriptPropertyChildPane.getChildren().add(nodeFromFXML);
		PropertyInfluenceController testPaneController = ((PropertyInfluenceController) testPropertyController);

		System.out.println("loadTestPropertyFXML: nodeType: " + nodeType);
		switch (nodeType) {

		case STA:
			nodeFromFXML = getNodeFromFXML("/fxml/testprofile/PropertySTA" + ConstantApp.THEME_FXML);
			testPaneController.expandPropertiesPane(nodeFromFXML, false);

			if (testPropertyController != null && testPropertyController instanceof PropertySTAController) {
				((PropertySTAController) testPropertyController).PropertyDisplayUpdate(nodeType.toString(), currentAliasId, SelectedNode);
			}
			break;

		case Warmup:
			nodeFromFXML = getNodeFromFXML("/fxml/testprofile/PropertyWarmup" + ConstantApp.THEME_FXML);
			testPaneController.expandPropertiesPane(nodeFromFXML, false);

			if (testPropertyController != null && testPropertyController instanceof PropertyWarmupController) {
				((PropertyWarmupController) testPropertyController).updateTestPropertyDisplay(nodeType.toString(), currentAliasId, SelectedNode);
			}
			break;

		case NoLoad:
			nodeFromFXML = getNodeFromFXML("/fxml/testprofile/PropertyCreep" + ConstantApp.THEME_FXML);
			testPaneController.expandPropertiesPane(nodeFromFXML,false);
			if (testPropertyController != null && testPropertyController instanceof PropertyCreepController) {
				((PropertyCreepController) testPropertyController).PropertyDisplayUpdate(nodeType.toString(), currentAliasId, SelectedNode);
			}
			break;


		case Repeatability:
			nodeFromFXML = getNodeFromFXML2("/fxml/testprofile/PropertyRepeatability" + ConstantApp.THEME_FXML);
			testPaneController.expandPropertiesPane(nodeFromFXML, true);
			if (testPropertyController != null) {
				((PropertyInfluenceController) testPropertyController).PropertyValueUpdate(nodeType.toString(), currentAliasId, SelectedNode);
				((PropertyRepeatabilityController) testPropertyController2).PropertyDisplayUpdate(nodeType.toString(), currentAliasId, SelectedNode);
			}
			break;


		case SelfHeating:
			nodeFromFXML = getNodeFromFXML2("/fxml/testprofile/PropertySelfHeating" + ConstantApp.THEME_FXML);
			testPaneController.expandPropertiesPane(nodeFromFXML, true);
			if (testPropertyController != null) {
				((PropertyInfluenceController) testPropertyController).PropertyValueUpdate(nodeType.toString(), currentAliasId, SelectedNode);
				((PropertySelfHeatingController) testPropertyController2).PropertyDisplayUpdate(nodeType.toString(), currentAliasId, SelectedNode);
			}
			break;

		case ConstantTest:
			nodeFromFXML = getNodeFromFXML2("/fxml/testprofile/PropertyConstant" + ConstantApp.THEME_FXML);
			testPaneController.expandPropertiesPane(nodeFromFXML, true);
			if (testPropertyController != null){
				((PropertyInfluenceController) testPropertyController).PropertyValueUpdate(nodeType.toString(), currentAliasId, SelectedNode);
				((PropertyConstTestController) testPropertyController2).PropertyDisplayUpdate(nodeType.toString(), currentAliasId, SelectedNode);
			}
			break;

		case Accuracy:
		case CuttingNuetral:
		case PhaseReversal:
			nodeFromFXML = getNodeFromFXML2("/fxml/testprofile/PropertyAccuracy" + ConstantApp.THEME_FXML);
			testPaneController.expandPropertiesPane(nodeFromFXML, true);
			if (testPropertyController != null) {
				((PropertyInfluenceController) testPropertyController).PropertyValueUpdate(nodeType.toString(), currentAliasId, SelectedNode);
				((PropertyAccuracyController) testPropertyController2).PropertyDisplayUpdate(nodeType.toString(), currentAliasId, SelectedNode);
			}
			break;


		case InfluenceVolt:
			ApplicationLauncher.logger.info("InfluenceVolt: test1");

			nodeFromFXML = getNodeFromFXML2("/fxml/testprofile/PropertyVoltage" + ConstantApp.THEME_FXML);
			testPaneController.expandPropertiesPane(nodeFromFXML, true);
			if (testPropertyController != null){
				ApplicationLauncher.logger.info("InfluenceVolt: test2");
				((PropertyInfluenceController) testPropertyController).PropertyValueUpdate(nodeType.toString(), currentAliasId, SelectedNode);
				((PropertyVoltageController) testPropertyController2).PropertyDisplayUpdate(nodeType.toString(), currentAliasId, SelectedNode);
			}
			break;


		case InfluenceFreq:
			ApplicationLauncher.logger.info("InfluenceFreq: test1");

			nodeFromFXML = getNodeFromFXML2("/fxml/testprofile/PropertyFrequency" + ConstantApp.THEME_FXML);
			testPaneController.expandPropertiesPane(nodeFromFXML, true);
			if (testPropertyController != null){
				ApplicationLauncher.logger.info("InfluenceFreq: test2");
				((PropertyInfluenceController) testPropertyController).PropertyValueUpdate(nodeType.toString(), currentAliasId, SelectedNode);
				((PropertyFrequencyController) testPropertyController2).PropertyDisplayUpdate(nodeType.toString(), currentAliasId, SelectedNode);
			}
			break;

		case InfluenceHarmonic:
			ApplicationLauncher.logger.info("InfluenceHarmonic: test1");

			nodeFromFXML = getNodeFromFXML2("/fxml/testprofile/PropertyHarmonic" + ConstantApp.THEME_FXML);
			
			if (ProcalFeatureEnable.HARMONICS_FEATURE_V2_ENABLED){
				ApplicationLauncher.logger.info("InfluenceHarmonic: loaded PropertyHarmonicV2 fxml");
				nodeFromFXML = getNodeFromFXML2("/fxml/testprofile/PropertyHarmonicV2" + ConstantApp.THEME_FXML);
			}
			
			if (ProcalFeatureEnable.BOFA_POWER_SOURCE_CONNECTED){
				ApplicationLauncher.logger.info("InfluenceHarmonic: loaded PropertyHarmonicBofa fxml");
				nodeFromFXML = getNodeFromFXML2("/fxml/testprofile/PropertyHarmonicBofa" + ConstantApp.THEME_FXML);
			}
			
		 //   nodeFromFXML = getNodeFromFXML2("/fxml/testprofile/PropertyHarmonicV2" + ConstantApp.THEME_FXML);
		//	nodeFromFXML = getNodeFromFXML2("/fxml/testprofile/PropertyHarmonicBofa" + ConstantApp.THEME_FXML);

			testPaneController.expandPropertiesPane(nodeFromFXML, true);
			if (testPropertyController != null){
				ApplicationLauncher.logger.info("InfluenceHarmonic: test2");
				((PropertyInfluenceController) testPropertyController).PropertyValueUpdate(nodeType.toString(), currentAliasId, SelectedNode);
				
				//((PropertyHarmonicController) testPropertyController2).PropertyDisplayUpdate(nodeType.toString(), currentAliasId, SelectedNode);
				if (ProcalFeatureEnable.HARMONICS_FEATURE_V2_ENABLED){
					((PropertyHarmonicControllerV2) testPropertyController2).PropertyDisplayUpdate(nodeType.toString(), currentAliasId, SelectedNode);
			
				}else {
					((PropertyHarmonicController) testPropertyController2).PropertyDisplayUpdate(nodeType.toString(), currentAliasId, SelectedNode);
					
				}
			}
			break;

		case VoltageUnbalance:
			ApplicationLauncher.logger.info("VoltageUnbalance: test1");

			nodeFromFXML = getNodeFromFXML2("/fxml/testprofile/PropertyVoltageUnbalance" + ConstantApp.THEME_FXML);
			testPaneController.expandPropertiesPane(nodeFromFXML, true);
			if (testPropertyController != null){
				ApplicationLauncher.logger.info("VoltageUnbalance: test2");
				((PropertyInfluenceController) testPropertyController).PropertyValueUpdate(nodeType.toString(), currentAliasId, SelectedNode);
				((PropertyVoltageUnbalanceController) testPropertyController2).PropertyDisplayUpdate(nodeType.toString(), currentAliasId, SelectedNode);
			}
			break;

		case CustomTest:
			nodeFromFXML = getNodeFromFXML("/fxml/testprofile/PropertyCustomRating" + ConstantApp.THEME_FXML);
			testPaneController.expandPropertiesPane(nodeFromFXML, false);

			if (testPropertyController != null && testPropertyController instanceof PropertyCustomRatingController) {
				((PropertyCustomRatingController) testPropertyController).PropertyDisplayUpdate(nodeType.toString(), currentAliasId, SelectedNode);
			}
			break;
			
			
		case DutCommand:
			nodeFromFXML = getNodeFromFXML("/fxml/testprofile/PropertyDutCommand" + ConstantApp.THEME_FXML);
			testPaneController.expandPropertiesPane(nodeFromFXML, false);

			if (testPropertyController != null && testPropertyController instanceof PropertyDutCommandController) {
				((PropertyDutCommandController) testPropertyController).PropertyDisplayUpdate(nodeType.toString(), currentAliasId, SelectedNode);
			}
			break;

		default:
			testPaneController.expandPropertiesPane(null, true);
			PopupErrorLoadingTP();
			break;
		}
	}

	public void PopupErrorLoadingTP(){
		Alert alert = new Alert(AlertType.INFORMATION);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
		alert.setTitle("Info");
		alert.setHeaderText("Error Loading Test Point");
		String s ="Error Code: 401";
		alert.setContentText(s);
		Optional<ButtonType> result = alert.showAndWait();
		ApplicationLauncher.logger.info("result.get(): "  + result.get());
	}


	public void buildNodeDragHandlers() {

		mContextDragOver = new EventHandler <DragEvent>() {
			//dragover to handle node dragging in the right pane view
			@Override
			public void handle(DragEvent event) {		
				ApplicationLauncher.logger.info ("Draggable node -> drag over event recieved...");
				event.acceptTransferModes(TransferMode.ANY);				
				relocateToPoint(new Point2D( event.getSceneX(), event.getSceneY()));

				event.consume();
			}
		};

		//dragdrop for node dragging
		mContextDragDropped = new EventHandler <DragEvent> () {
			@Override
			public void handle(DragEvent event) {
				ApplicationLauncher.logger.info ("Draggable node -> drag dropped event recieved...");

				event.setDropCompleted(true);
				event.consume();
			}
		};

		draggableNodeCloseButton.setOnMouseClicked( new EventHandler <MouseEvent> () {

			@Override
			public void handle(MouseEvent event) {
				ApplicationLauncher.logger.info ("Draggable node -> close  event recieved...");
				String currentTitle = getTitle();
				String currentAliasId = getAliasId();
				String currentProject = getProjectName();
				String currentTestType = getType().toString();

				if (currentAliasId.equals("00")) {
					event.consume();//dramesh
					return;
				}
				ApplicationLauncher.logger.info ("Deleting the node: " + currentAliasId);
				VBox parent  = (VBox) self.getParent();
				ApplicationLauncher.logger.info ("Deleting the node: Node size:" + parent.getChildren().size());
				try {
					if (parent.getChildren().size() == 0) {
						unloadCurrentTestPropertyFXML();
						ProjectController.refreshSummaryPaneData("all", "all");
					}
					else {
						ProjectController.removeTestPoint(currentTestType,currentAliasId );
						ApplicationLauncher.logger.info ("Deleting the node: IsNewNode:" + IsNewNode());
						if(!IsNewNode()){
							ApplicationLauncher.logger.info ("Deleting the node: currentProject:" + currentProject);
							ApplicationLauncher.logger.info ("Deleting the node: currentTestType: " + currentTestType);
							ApplicationLauncher.logger.info ("Deleting the node: currentAliasId: " + currentAliasId);
							ProjectController.DeleteNode(self);
							MySQL_Controller.sp_delete_project_node(currentProject, currentTestType, currentAliasId);
							MySQL_Controller.sp_delete_project_components(currentProject, currentTestType, currentAliasId);
							MySQL_Controller.sp_delete_summary_data(currentProject, currentTestType, currentAliasId);
							//if(currentTestType.equals("InfluenceHarmonic")){
							if(currentTestType.equals(TestProfileType.InfluenceHarmonic.toString())){
								MySQL_Controller.sp_delete_harmonic_data(currentProject, currentTestType, currentAliasId);
							}
						}
						//ProjectController.refreshSummaryPaneData(currentTitle, currentAliasId);
						ProjectController.RefreshSummaryDataFromDB();
						ProjectController.LoadSummaryDataToGUI();
					}
					parent.getChildren().remove(self);
				} catch (IOException e) {
					
					e.printStackTrace();
					ApplicationLauncher.logger.error(" setOnMouseClicked : IOException: " + e.getMessage());
					
				}
				rebuildNodeIndex(parent);
				event.consume();
			}
		});

		//drag detection for node dragging
		self.setOnDragDetected ( new EventHandler <MouseEvent> () {

			@Override
			public void handle(MouseEvent event) {
				ApplicationLauncher.logger.info ("Draggable node -> drag detected event recieved...");

				relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
				ApplicationLauncher.logger.info ("Draggable node -> source parent: " + self.getParent());
				ApplicationLauncher.logger.info ("Draggable node -> source location: " + new Point2D(event.getSceneX(), event.getSceneY()));

				ClipboardContent content = new ClipboardContent();
				DragContainer container = new DragContainer();

				container.addData ("type", mType.toString());
				content.put(DragContainer.AddNode, container);
				container.addData ("nodeParent", self.getParent().getId());

				startDragAndDrop (TransferMode.ANY).setContent(content);                
				ApplicationLauncher.logger.info ("Draggable node -> " + container.getValue("type"));
				event.consume();					
			}
		});	

		self.setOnMouseClicked(new EventHandler <MouseEvent> () {

			@Override
			public void handle(MouseEvent event) {
				System.out.println ("Draggable node -> click location: " + new Point2D(event.getSceneX(), event.getSceneY()));
				if (event.getClickCount() >=2) {
					System.out.println ("Draggable node -> double click handler invoked...");
					String parentContainer = self.getParent().getId();
					if (!parentContainer.equals("testScriptLeftPane")) {
						System.out.println ("Draggable node -> ignoring right pane node double click...");
						return;
					}
					if (parentScriptController == null) {
						System.out.println ("Draggable node ->parent script controller is null....");
						return;
					}
					parentScriptController.onNodeDoubleClicked(event);
				}
				else {
					System.out.println ("Draggable node -> single click handler invoked...");
					onDragNodeClick();
				}
				event.consume();
			}
		});
	}


	public void unloadCurrentTestPropertyFXML() throws IOException{
		testScriptPropertyChildPane.getChildren().clear();
	}


	public Point2D getDragOffSet() {
		return mDragOffset;
	}

	public void setDragOffSet(Point2D newOffset) {
		mDragOffset = newOffset;
	}

	public void rebuildNodeIndex(VBox parent) {
		List<Node> nodeList = parent.getChildren();
		int i = 0;
		for (Node node : nodeList) {
			((DraggableTestNode) node).setNodeIndex(++i);
			ApplicationLauncher.logger.info("Updating the node " + ((DraggableTestNode) node).getTitle() + " index to " + i);
		}
	}

	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("DraggableTestNode[id=").append(getId()).append(", aliasId=").append(aliasId).append(", ");
		str.append("type=").append(mType).append(", location=").append(mDragOffset).append("]");
		return str.toString();
	}

}
