package com.tasnetwork.calibration.energymeter.deployment;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ConstantLduLscs;
import com.tasnetwork.calibration.energymeter.constant.ConstantReport;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController.StartRunTaskClick;
import com.tasnetwork.calibration.energymeter.device.Communicator;
import com.tasnetwork.calibration.energymeter.device.SerialDataRadiantRefStd;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.shape.Rectangle;

public class SecondaryLduDisplayController implements Initializable{
	
	Timer assertLduTimer;

/*	public String FAIL_STYLE1 = "-fx-background-color: red;-fx-text-fill: black;";
	public String FAIL_STYLE2 = "-fx-control-inner-background: red; -fx-text-fill: white;";
	public String FAIL_STYLE3 = "-fx-background-color: tomato;-fx-text-fill: black;";/// good
	public String FAIL_STYLE4 = "-fx-background-color: tomato;-fx-text-fill: white;";
	public String PASS_STYLE1 = "-fx-control-inner-background: lightgreen;-fx-text-fill: black;";
	public String PASS_STYLE2 = "-fx-background-color: greenyellow;-fx-text-fill: black;"; // good
	public String PASS_STYLE3 = "-fx-background-color: limegreen;-fx-text-fill: black;";
	public String PASS_STYLE4 = "-fx-background-color: limegreen;-fx-text-fill: white;";*/
	
	
	@FXML ComboBox cmbBxLduSecDisplayTP;
	@FXML Rectangle cmbBxLduSecDisplayOverlay;
	@FXML TextField txtLduSecDisplayStatus;
	@FXML Button btnLduSecDisplayPrevious;
	@FXML Button btnLduSecDisplayNext;
	
	static  ComboBox ref_cmbBxLduSecDisplayTP;
	static  Rectangle ref_cmbBxLduSecDisplayOverlay;
	static TextField ref_txtLduSecDisplayStatus;
	static Button ref_btnLduSecDisplayPrevious;
	static Button ref_btnLduSecDisplayNext;
	
	@FXML Label labelStatus;
	
	static  Label ref_labelStatus;
	
	@FXML TextField txtLduPosition01;
	@FXML TextField txtLduPosition02;
	@FXML TextField txtLduPosition03;
	@FXML TextField txtLduPosition08;
	@FXML TextField txtLduPosition04;
	@FXML TextField txtLduPosition05;
	@FXML TextField txtLduPosition07;
	@FXML TextField txtLduPosition09;
	@FXML TextField txtLduPosition06;
	
	
	public static TextField ref_txtLduPosition01;
	public static TextField ref_txtLduPosition02;
	public static TextField ref_txtLduPosition03;
	public static TextField ref_txtLduPosition08;
	public static TextField ref_txtLduPosition04;
	public static TextField ref_txtLduPosition05;
	public static TextField ref_txtLduPosition07;
	public static TextField ref_txtLduPosition09;
	public static TextField ref_txtLduPosition06;
	
	@FXML TextField txtLduPosition10;
	@FXML TextField txtLduPosition11;
	@FXML TextField txtLduPosition12;
	@FXML TextField txtLduPosition17;
	@FXML TextField txtLduPosition13;
	@FXML TextField txtLduPosition14;
	@FXML TextField txtLduPosition16;
	@FXML TextField txtLduPosition18;
	@FXML TextField txtLduPosition15;
	
	public static TextField ref_txtLduPosition10;
	public static TextField ref_txtLduPosition11;
	public static TextField ref_txtLduPosition12;
	public static TextField ref_txtLduPosition17;
	public static TextField ref_txtLduPosition13;
	public static TextField ref_txtLduPosition14;
	public static TextField ref_txtLduPosition16;
	public static TextField ref_txtLduPosition18;
	public static TextField ref_txtLduPosition15;
	//public static TextField ref_txtTimeLeft;
	
	
	@FXML TextField txtLduPosition19;
	@FXML TextField txtLduPosition20;
	@FXML TextField txtLduPosition21;
	@FXML TextField txtLduPosition27;
	@FXML TextField txtLduPosition22;
	@FXML TextField txtLduPosition23;
	@FXML TextField txtLduPosition25;
	@FXML TextField txtLduPosition26;
	@FXML TextField txtLduPosition24;
	
	@FXML TextField txtLduPosition28;
	@FXML TextField txtLduPosition29;
	@FXML TextField txtLduPosition30;
	@FXML TextField txtLduPosition31;
	@FXML TextField txtLduPosition32;
	@FXML TextField txtLduPosition33;
	@FXML TextField txtLduPosition34;
	@FXML TextField txtLduPosition35;
	@FXML TextField txtLduPosition36;
	@FXML TextField txtLduPosition37;
	@FXML TextField txtLduPosition38;
	@FXML TextField txtLduPosition39;
	@FXML TextField txtLduPosition40;
	@FXML TextField txtLduPosition41;
	@FXML TextField txtLduPosition42;
	@FXML TextField txtLduPosition43;
	@FXML TextField txtLduPosition44;
	@FXML TextField txtLduPosition45;
	@FXML TextField txtLduPosition46;
	@FXML TextField txtLduPosition47;
	@FXML TextField txtLduPosition48;
	
	
	
	@FXML TitledPane A_PhasePane;
	@FXML TitledPane B_PhasePane;
	//@FXML TitledPane C_PhasePane;
	
/*	@FXML Label labelActiveRectiveUnit_R;
	@FXML Label labelActiveRectiveUnit_Y;
	@FXML Label labelActiveRectiveUnit_B;

	public static Label ref_labelActiveRectiveUnit_R;
	public static Label ref_labelActiveRectiveUnit_Y;
	public static Label ref_labelActiveRectiveUnit_B;*/
	public static TextField ref_txtLduPosition19;
	public static TextField ref_txtLduPosition20;
	public static TextField ref_txtLduPosition21;
	public static TextField ref_txtLduPosition27;
	public static TextField ref_txtLduPosition22;
	public static TextField ref_txtLduPosition23;
	public static TextField ref_txtLduPosition25;
	public static TextField ref_txtLduPosition26;
	public static TextField ref_txtLduPosition24;
	
	public static TextField ref_txtLduPosition28;
	public static TextField ref_txtLduPosition29;
	public static TextField ref_txtLduPosition30;
	public static TextField ref_txtLduPosition31;
	public static TextField ref_txtLduPosition32;
	public static TextField ref_txtLduPosition33;
	public static TextField ref_txtLduPosition34;
	public static TextField ref_txtLduPosition35;
	public static TextField ref_txtLduPosition36;
	public static TextField ref_txtLduPosition37;
	public static TextField ref_txtLduPosition38;
	public static TextField ref_txtLduPosition39;
	public static TextField ref_txtLduPosition40;
	public static TextField ref_txtLduPosition41;
	public static TextField ref_txtLduPosition42;
	public static TextField ref_txtLduPosition43;
	public static TextField ref_txtLduPosition44;
	public static TextField ref_txtLduPosition45;
	public static TextField ref_txtLduPosition46;
	public static TextField ref_txtLduPosition47;
	public static TextField ref_txtLduPosition48;
	
	@FXML
	private ProgressBar  barRefStdRefresh;
	public static  ProgressBar  ref_barRefStdRefresh;
	

	
	private static StringProperty positionData01 = new SimpleStringProperty();
	private static StringProperty positionData02 = new SimpleStringProperty();
	private static StringProperty positionData03 = new SimpleStringProperty();
	private static StringProperty positionData08 = new SimpleStringProperty();
	private static StringProperty positionData04 = new SimpleStringProperty();
	private static StringProperty positionData05 = new SimpleStringProperty();
	private static StringProperty positionData07 = new SimpleStringProperty();
	private static StringProperty positionData06 = new SimpleStringProperty();
	private static StringProperty positionData09 = new SimpleStringProperty();
	
	private static StringProperty positionData10 = new SimpleStringProperty();
	private static StringProperty positionData11 = new SimpleStringProperty();
	private static StringProperty positionData12 = new SimpleStringProperty();
	private static StringProperty positionData17 = new SimpleStringProperty();
	private static StringProperty positionData13 = new SimpleStringProperty();
	private static StringProperty positionData14 = new SimpleStringProperty();
	private static StringProperty positionData16 = new SimpleStringProperty();
	private static StringProperty positionData15 = new SimpleStringProperty();
	private static StringProperty positionData18 = new SimpleStringProperty();
	
	private static StringProperty positionData19 = new SimpleStringProperty();
	private static StringProperty positionData20 = new SimpleStringProperty();
	private static StringProperty positionData21 = new SimpleStringProperty();
	private static StringProperty positionData27 = new SimpleStringProperty();
	private static StringProperty positionData22 = new SimpleStringProperty();
	private static StringProperty positionData23 = new SimpleStringProperty();
	private static StringProperty positionData25 = new SimpleStringProperty();
	private static StringProperty positionData24 = new SimpleStringProperty();
	private static StringProperty positionData26 = new SimpleStringProperty();
	private static StringProperty positionData28 = new SimpleStringProperty();
	private static StringProperty positionData29 = new SimpleStringProperty();
	private static StringProperty positionData30 = new SimpleStringProperty();
	private static StringProperty positionData31 = new SimpleStringProperty();
	private static StringProperty positionData32 = new SimpleStringProperty();
	private static StringProperty positionData33 = new SimpleStringProperty();
	private static StringProperty positionData34 = new SimpleStringProperty();
	private static StringProperty positionData35 = new SimpleStringProperty();
	private static StringProperty positionData36 = new SimpleStringProperty();
	private static StringProperty positionData37 = new SimpleStringProperty();
	private static StringProperty positionData38 = new SimpleStringProperty();
	private static StringProperty positionData39 = new SimpleStringProperty();
	private static StringProperty positionData40 = new SimpleStringProperty();
	private static StringProperty positionData41 = new SimpleStringProperty();
	private static StringProperty positionData42 = new SimpleStringProperty();
	private static StringProperty positionData43 = new SimpleStringProperty();
	private static StringProperty positionData44 = new SimpleStringProperty();
	private static StringProperty positionData45 = new SimpleStringProperty();
	private static StringProperty positionData46 = new SimpleStringProperty();
	private static StringProperty positionData47 = new SimpleStringProperty();
	private static StringProperty positionData48 = new SimpleStringProperty();
	

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
/*		if(ConstantFeatureEnable.PHASE_DISPLAY_ENABLE_FEATURE){
			A_PhasePane.setText(ConstantApp.FIRST_PHASE_DISPLAY_NAME );
			B_PhasePane.setText(ConstantApp.SECOND_PHASE_DISPLAY_NAME);
			C_PhasePane.setText(ConstantApp.THIRD_PHASE_DISPLAY_NAME );
		}*/
    	
		ref_txtLduPosition01 = txtLduPosition01;
		ref_txtLduPosition02 = txtLduPosition02;
		ref_txtLduPosition03= txtLduPosition03;
		ref_txtLduPosition04 = txtLduPosition04;


		ref_txtLduPosition05 = txtLduPosition05;
		ref_txtLduPosition06 = txtLduPosition06;
		ref_txtLduPosition07 = txtLduPosition07;
		ref_txtLduPosition08= txtLduPosition08;
		ref_txtLduPosition09 = txtLduPosition09;
		
		ref_txtLduPosition10 = txtLduPosition10;
		ref_txtLduPosition11 = txtLduPosition11;
		ref_txtLduPosition12= txtLduPosition12;
		ref_txtLduPosition17= txtLduPosition17;
		ref_txtLduPosition13 = txtLduPosition13;
		ref_txtLduPosition14 = txtLduPosition14;
		ref_txtLduPosition16 = txtLduPosition16;
		ref_txtLduPosition15 = txtLduPosition15;
		ref_txtLduPosition18 = txtLduPosition18;
		
		ref_txtLduPosition19 = txtLduPosition19;
		ref_txtLduPosition20 = txtLduPosition20;
		ref_txtLduPosition21= txtLduPosition21;
		ref_txtLduPosition27= txtLduPosition27;
		ref_txtLduPosition22 = txtLduPosition22;
		ref_txtLduPosition23 = txtLduPosition23;
		ref_txtLduPosition25 = txtLduPosition25;
		ref_txtLduPosition24 = txtLduPosition24;
		ref_txtLduPosition26 = txtLduPosition26;
		
		ref_txtLduPosition28 = txtLduPosition28;
		ref_txtLduPosition29 = txtLduPosition29;
		ref_txtLduPosition30 = txtLduPosition30;
		ref_txtLduPosition31 = txtLduPosition31;
		ref_txtLduPosition32 = txtLduPosition32;
		ref_txtLduPosition33 = txtLduPosition33;
		ref_txtLduPosition34 = txtLduPosition34;
		ref_txtLduPosition35 = txtLduPosition35;
		ref_txtLduPosition36 = txtLduPosition36;
		ref_txtLduPosition37 = txtLduPosition37;
		ref_txtLduPosition38 = txtLduPosition38;
		ref_txtLduPosition39 = txtLduPosition39;
		ref_txtLduPosition40 = txtLduPosition40;
		ref_txtLduPosition41 = txtLduPosition41;
		ref_txtLduPosition42 = txtLduPosition42;
		ref_txtLduPosition43 = txtLduPosition43;
		ref_txtLduPosition44 = txtLduPosition44;
		ref_txtLduPosition45 = txtLduPosition45;
		ref_txtLduPosition46 = txtLduPosition46;
		ref_txtLduPosition47 = txtLduPosition47;
		ref_txtLduPosition48 = txtLduPosition48;

		ref_barRefStdRefresh = barRefStdRefresh;
		
		ref_cmbBxLduSecDisplayTP = cmbBxLduSecDisplayTP;
		ref_cmbBxLduSecDisplayOverlay = cmbBxLduSecDisplayOverlay;
		ref_txtLduSecDisplayStatus = txtLduSecDisplayStatus;
		ref_btnLduSecDisplayPrevious = btnLduSecDisplayPrevious;
		ref_btnLduSecDisplayNext = btnLduSecDisplayNext;
		ref_labelStatus = labelStatus;
/*		ref_labelActiveRectiveUnit_R = labelActiveRectiveUnit_R;
		ref_labelActiveRectiveUnit_Y = labelActiveRectiveUnit_Y;
		ref_labelActiveRectiveUnit_B = labelActiveRectiveUnit_B;*/
		BindPropertytoTextField();
		Platform.runLater(() -> {
			ref_cmbBxLduSecDisplayTP.getItems().clear();
		});
		
		hideGUI_Objects();
		//demoStyleDisplay();
		//assertLduDisplayTrigger();
		
    }
    
    
    public void hideGUI_Objects() {
    	
    	Platform.runLater(() -> {
			ref_txtLduSecDisplayStatus.setVisible(false);
			ref_btnLduSecDisplayPrevious.setVisible(false);
			ref_btnLduSecDisplayNext.setVisible(false);
			ref_labelStatus.setVisible(false);
		});
    	
    }
    public void assertLduDisplayTrigger() {
		  ApplicationLauncher.logger.info("assertLduDisplayTrigger: Entry");
		  assertLduTimer = new Timer();
		  assertLduTimer.schedule(new AssertLduDisplayTask(), 50);

	  }
    
	  class AssertLduDisplayTask extends TimerTask {
		  public void run() {
			  assertLduDisplay();
			  assertLduTimer.cancel();


		  }
	  }
    
    public void assertLduDisplay(){
    	
    	Sleep(10000);
    	for(int address=1; address <=48; address++){
    		for(int i =1; i <= 6; i++){
    			if(i==1){
    				updateLduSecondaryDisplay(address,"P", "+1.234");
    			}else if(i==2){
    				updateLduSecondaryDisplay(address,"N", "");
    			}else if(i==3){
    				updateLduSecondaryDisplay(address,"F", "+2.000");
    			}else if(i==4){
    				updateLduSecondaryDisplay(address,"", "");
    			}else if(i==5){
    				updateLduSecondaryDisplay(address,"N", "WFR");
    			}else if(i==6){
    				updateLduSecondaryDisplay(address,"", String.valueOf(address));
    			}
    			Sleep(2000);
    		}
    		Sleep(1000);
    	}
    }
    
	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {
			 
			e.printStackTrace();
			ApplicationLauncher.logger.error("Sleep :InterruptedException:"+ e.getMessage());
		}

	}
    
    public void demoStyleDisplay(){
    	
    	/*	public String FAIL_STYLE1 = "-fx-background-color: red;-fx-text-fill: black;";
    	public String FAIL_STYLE2 = "-fx-control-inner-background: red; -fx-text-fill: white;";
    	public String FAIL_STYLE3 = "-fx-background-color: tomato;-fx-text-fill: black;";/// good
    	public String FAIL_STYLE4 = "-fx-background-color: tomato;-fx-text-fill: white;";
    	public String PASS_STYLE1 = "-fx-control-inner-background: lightgreen;-fx-text-fill: black;";
    	public String PASS_STYLE2 = "-fx-background-color: greenyellow;-fx-text-fill: black;"; // good
    	public String PASS_STYLE3 = "-fx-background-color: limegreen;-fx-text-fill: black;";
    	public String PASS_STYLE4 = "-fx-background-color: limegreen;-fx-text-fill: white;";*/
    	
    	positionData01.setValue("+1.123");
		positionData02.setValue("+2.123");
		positionData03.setValue("+3.123");
		positionData04.setValue("+4.123");
		positionData05.setValue("+5.123");
		positionData06.setValue("+6.123");
		positionData07.setValue("+7.123");
		positionData08.setValue("+8.123");
		positionData09.setValue("+9.123");
		positionData10.setValue("+9.223");
		positionData11.setValue("+9.323");
		positionData12.setValue("+9.423");
		/*//ref_txtLduPosition01.setStyle(arg0);
		ref_txtLduPosition01.setStyle(FAIL_STYLE1);
		ref_txtLduPosition02.setStyle("-fx-control-inner-background: red; -fx-text-fill: white;");
		ref_txtLduPosition03.setStyle("-fx-background-color: tomato;-fx-text-fill: black;");
		ref_txtLduPosition04.setStyle(FAIL_STYLE4);
		ref_txtLduPosition05.setStyle("-fx-control-inner-background: lightgreen;-fx-text-fill: black;");
		//ref_txtLduPosition03.setStyle("-fx-background-color: lightgreen;");
		//ref_txtLduPosition04.setStyle("-fx-background-color: red; -fx-text-fill: white;");
		
		
		ref_txtLduPosition06.setStyle("-fx-background-color: greenyellow;-fx-text-fill: black;");
		ref_txtLduPosition07.setStyle("-fx-background-color: limegreen;-fx-text-fill: black;");
		ref_txtLduPosition08.setStyle("-fx-background-color: limegreen;-fx-text-fill: white;");
		ref_txtLduPosition09.getStyleClass().add("custom");// loaded from lduDisplay.css file // .custom {  -fx-control-inner-background: orange;}
		//ref_txtLduPosition04.setStyle();
*/		
		ref_txtLduPosition01.getStyleClass().add("failStyle1");
		ref_txtLduPosition02.getStyleClass().add("failStyle2");
		ref_txtLduPosition03.getStyleClass().add("failStyle3");
		ref_txtLduPosition04.getStyleClass().add("failStyle4");
		ref_txtLduPosition05.getStyleClass().add("passStyle1");
	
		ref_txtLduPosition06.getStyleClass().add("passStyle2");
		ref_txtLduPosition07.getStyleClass().add("passStyle3");
		ref_txtLduPosition08.getStyleClass().add("passStyle4");
		ref_txtLduPosition09.getStyleClass().add("custom");// loaded from lduDisplay.css file // .custom {  -fx-control-inner-background: orange;}
		ref_txtLduPosition10.getStyleClass().add("passSelected");
		ref_txtLduPosition11.getStyleClass().add("failSelected");
		ref_txtLduPosition12.getStyleClass().add("failSelected");
		ref_txtLduPosition12.getStyleClass().add("defaultStyle");
    }
    
/*    public static void set_labelActiveRectiveUnit_R(String InputData){
    	ref_labelActiveRectiveUnit_R.setText(InputData);
    }
    public static void set_labelActiveRectiveUnit_Y(String InputData){
    	ref_labelActiveRectiveUnit_Y.setText(InputData);
    }
    public static void set_labelActiveRectiveUnit_B(String InputData){
    	ref_labelActiveRectiveUnit_B.setText(InputData);
    }*/
    
    public void BindPropertytoTextField(){
    	
    	ref_txtLduPosition01.textProperty().bind(positionData01);
    	ref_txtLduPosition02.textProperty().bind(positionData02);
    	ref_txtLduPosition03.textProperty().bind(positionData03);
    	ref_txtLduPosition04.textProperty().bind(positionData04);
    	ref_txtLduPosition05.textProperty().bind(positionData05);
    	ref_txtLduPosition06.textProperty().bind(positionData06);
    	ref_txtLduPosition07.textProperty().bind(positionData07);
    	ref_txtLduPosition08.textProperty().bind(positionData08);
    	ref_txtLduPosition09.textProperty().bind(positionData09);
    	
    	
    	
    	
 
    	
    	ref_txtLduPosition10.textProperty().bind(positionData10);
    	ref_txtLduPosition11.textProperty().bind(positionData11);
    	ref_txtLduPosition12.textProperty().bind(positionData12);
    	ref_txtLduPosition13.textProperty().bind(positionData13);
    	ref_txtLduPosition14.textProperty().bind(positionData14);
    	ref_txtLduPosition15.textProperty().bind(positionData15);
    	ref_txtLduPosition16.textProperty().bind(positionData16);
    	ref_txtLduPosition17.textProperty().bind(positionData17);
    	ref_txtLduPosition18.textProperty().bind(positionData18);
    	
    	ref_txtLduPosition19.textProperty().bind(positionData19);
    	ref_txtLduPosition20.textProperty().bind(positionData20);
    	ref_txtLduPosition21.textProperty().bind(positionData21);
    	ref_txtLduPosition22.textProperty().bind(positionData22);
    	ref_txtLduPosition23.textProperty().bind(positionData23);
    	ref_txtLduPosition24.textProperty().bind(positionData24);
    	ref_txtLduPosition25.textProperty().bind(positionData25);
    	ref_txtLduPosition26.textProperty().bind(positionData26);
    	ref_txtLduPosition27.textProperty().bind(positionData27);
    	
    	
    	ref_txtLduPosition28.textProperty().bind(positionData28);
    	
    	ref_txtLduPosition29.textProperty().bind(positionData29);
    	ref_txtLduPosition30.textProperty().bind(positionData30);
    	ref_txtLduPosition31.textProperty().bind(positionData31);
    	ref_txtLduPosition32.textProperty().bind(positionData32);
    	ref_txtLduPosition33.textProperty().bind(positionData33);
    	ref_txtLduPosition34.textProperty().bind(positionData34);
    	ref_txtLduPosition35.textProperty().bind(positionData35);
    	ref_txtLduPosition36.textProperty().bind(positionData36);
    	ref_txtLduPosition37.textProperty().bind(positionData37);
    	
    	ref_txtLduPosition38.textProperty().bind(positionData38);
    	
    	ref_txtLduPosition39.textProperty().bind(positionData39);
    	ref_txtLduPosition40.textProperty().bind(positionData40);
    	ref_txtLduPosition41.textProperty().bind(positionData41);
    	ref_txtLduPosition42.textProperty().bind(positionData42);
    	ref_txtLduPosition43.textProperty().bind(positionData43);
    	ref_txtLduPosition44.textProperty().bind(positionData44);
    	ref_txtLduPosition45.textProperty().bind(positionData45);
    	ref_txtLduPosition46.textProperty().bind(positionData46);
    	ref_txtLduPosition47.textProperty().bind(positionData47);
    	ref_txtLduPosition48.textProperty().bind(positionData48);

    }
    

    
    public void progressDemoStart(){
    	//ProjectExecutionController.RefStdProgressBarStart();
    }
    public void progressDemoStop(){
    	//ProjectExecutionController.RefStdProgressBarStop();
    }
    public static void resetLduAllSecondarydisplay(){
    	ApplicationLauncher.logger.debug("resetLduAllSecondarydisplay : Entry");
    	
    	for(int address=1; address <=ProcalFeatureEnable.TOTAL_NO_OF_SUPPORTED_RACK; address++){
    		updateLduSecondaryDisplay(address,"", "");

    	}
    }
    
    public static void updateLduSecondarydisplayTestPoint(final int index){
    	ApplicationLauncher.logger.debug("updateLduSecondarydisplayTestPoint : Entry");
    	Platform.runLater(() -> {
	    	try{
	    		ref_cmbBxLduSecDisplayTP.getSelectionModel().select(index);
	    	} catch (Exception e) {
				 
				e.printStackTrace();
				ApplicationLauncher.logger.error("updateLduSecondarydisplayTestPoint :Exception:"+ e.getMessage());
			}
    	});

    }
    
    public static void refreshLduSecondarydisplayTestPointData(List<String> testPointList){
    	ApplicationLauncher.logger.debug("refreshLduSecondarydisplayTestPointData : Entry");
    	Platform.runLater(() -> {
	    	try{
	    		ref_cmbBxLduSecDisplayTP.getItems().clear();
	    		ref_cmbBxLduSecDisplayTP.getItems().addAll(testPointList);
	    		ref_cmbBxLduSecDisplayTP.getSelectionModel().select(0);
	    	} catch (Exception e) {
				 
				e.printStackTrace();
				ApplicationLauncher.logger.error("refreshLduSecondarydisplayTestPointData :Exception:"+ e.getMessage());
			}
    	});

    }
    
    public static void clearLduSecondarydisplayTestPoint(){
    	ApplicationLauncher.logger.debug("clearLduSecondarydisplayTestPoint : entry");
    	Platform.runLater(() -> {
    		ref_cmbBxLduSecDisplayTP.getItems().clear();
    	});
    }
    
/*    public static void clearLduSecondaryDisplayStatus(){
    	ApplicationLauncher.logger.debug("clearLduSecondaryDisplayStatus : entry");
    	Platform.runLater(() -> {
    		ref_txtLduSecDisplayStatus.setText("");
    	});
    }*/
    
    public static void changeLduPositionBackground(TextField txtLduPosition, String resultStatus){
    	try{
    		txtLduPosition.getStyleClass().remove(ConstantLduLscs.LDU_FAIL_STYLE_CLASS_NAME);
	    	txtLduPosition.getStyleClass().remove(ConstantLduLscs.LDU_DEFAULT_STYLE_CLASS_NAME);
	    	txtLduPosition.getStyleClass().remove(ConstantLduLscs.LDU_FAIL_STYLE_CLASS_NAME);
	    	if(resultStatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){
		    	
		    	txtLduPosition.getStyleClass().add(ConstantLduLscs.LDU_PASS_STYLE_CLASS_NAME);
	    	}else if(resultStatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){
				//ApplicationLauncher.logger.debug("UpdateLduDisplay1 : fail");
				//ref_txtLduPosition01.getStyleClass().clear();
				//txtLduPosition.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
				//txtLduPosition.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
				txtLduPosition.getStyleClass().add(ConstantLduLscs.LDU_FAIL_STYLE_CLASS_NAME);
			}else{
				//ApplicationLauncher.logger.debug("UpdateLduDisplay1 : default");
				//ref_txtLduPosition01.getStyleClass().clear();
				//txtLduPosition.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
				//txtLduPosition.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
				txtLduPosition.getStyleClass().add(ConstantLduLscs.LDU_DEFAULT_STYLE_CLASS_NAME);
			}
    	} catch (Exception e) {
			 
			//e.printStackTrace();
			ApplicationLauncher.logger.error("changeLduPositionBackground :Exception:"+ e.getMessage());
		}
    	
    }
    
    public static void updateLduSecondaryDisplay(final int LDU_ReadAddress,final String Resultstatus,final String ErrorValue){


    	//ApplicationLauncher.logger.debug("updateLduSecondaryDisplay : Entry");
    	//ApplicationLauncher.logger.debug("updateLduSecondaryDisplay : LDU_ReadAddress: "+LDU_ReadAddress);
    	//ApplicationLauncher.logger.debug("updateLduSecondaryDisplay : Resultstatus: "+Resultstatus);
    	//ApplicationLauncher.logger.debug("updateLduSecondaryDisplay : ErrorValue: "+ErrorValue);
    	try{
    		Platform.runLater(() -> {
	    		
				switch (LDU_ReadAddress) {

				case 1:
					positionData01.setValue(ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition01,Resultstatus);
/*					if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){
						ApplicationLauncher.logger.debug("UpdateLduDisplay1 : pass");
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);vxcv
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition01.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){
						ApplicationLauncher.logger.debug("UpdateLduDisplay1 : fail");
						//ref_txtLduPosition01.getStyleClass().clear();
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);xcvc
						ref_txtLduPosition01.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{
						ApplicationLauncher.logger.debug("UpdateLduDisplay1 : default");
						//ref_txtLduPosition01.getStyleClass().clear();
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);erytr
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);fhggj
						ref_txtLduPosition01.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}*/
					//liveTableDataListObj.get(CurrentTestPointIndex).setRack1_Data(Resultstatus + " " + ErrorValue);
					//ApplicationLauncher.logger.info("UpdateliveTableData :"+CurrentProjectName+" Index:"+CurrentTestPointIndex+" :Rack"+LDU_ReadAddress+":<"+Resultstatus + " " + ErrorValue+">");
					break;
				case 2:
					positionData02.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack2_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition02,Resultstatus);
					/*					if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition02.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition02.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition02.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}*/
					//ApplicationLauncher.logger.info("UpdateliveTableData :"+CurrentProjectName+" Index:"+CurrentTestPointIndex+" :Rack"+LDU_ReadAddress+":<"+Resultstatus + " " + ErrorValue+">");
					break;
				case 3:
					positionData03.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack3_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition03,Resultstatus); 
					/* if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition03.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition03.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition03.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					//ApplicationLauncher.logger.info("UpdateliveTableData :"+CurrentProjectName+" Index:"+CurrentTestPointIndex+" :Rack"+LDU_ReadAddress+":<"+Resultstatus + " " + ErrorValue+">");
					*/ 
					break;
				case 4:
					positionData04.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack4_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition04, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition04.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition04.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition04.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					//ApplicationLauncher.logger.info("UpdateliveTableData :"+CurrentProjectName+" Index:"+CurrentTestPointIndex+" :Rack"+LDU_ReadAddress+":<"+Resultstatus + " " + ErrorValue+">");
					*/ 
					break;
				case 5:
					positionData05.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack5_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition05, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition05.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition05.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition05.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					//ApplicationLauncher.logger.info("UpdateliveTableData :"+CurrentProjectName+" Index:"+CurrentTestPointIndex+" :Rack"+LDU_ReadAddress+":<"+Resultstatus + " " + ErrorValue+">");
					*/ 
					break;
				case 6:
					positionData06.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack6_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition06,Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition06.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition06.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition06.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					//ApplicationLauncher.logger.info("UpdateliveTableData :"+CurrentProjectName+" Index:"+CurrentTestPointIndex+" :Rack"+LDU_ReadAddress+":<"+Resultstatus + " " + ErrorValue+">");
					*/ 
					break;
				case 7:
					positionData07.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack7_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition07, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition07.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition07.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition07.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					//ApplicationLauncher.logger.info("UpdateliveTableData :"+CurrentProjectName+" Index:"+CurrentTestPointIndex+" :Rack"+LDU_ReadAddress+":<"+Resultstatus + " " + ErrorValue+">");
					*/ 
					break;
				case 8:
					positionData08.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack8_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition08, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition08.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition08.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition08.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					//ApplicationLauncher.logger.info("UpdateliveTableData :"+CurrentProjectName+" Index:"+CurrentTestPointIndex+" :Rack"+LDU_ReadAddress+":<"+Resultstatus + " " + ErrorValue+">");
					*/ 
					break;
				case 9:
					positionData09.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack9_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition09, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition09.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition09.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition09.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					//ApplicationLauncher.logger.info("UpdateliveTableData :"+CurrentProjectName+" Index:"+CurrentTestPointIndex+" :Rack"+LDU_ReadAddress+":<"+Resultstatus + " " + ErrorValue+">");
					*/ 
					break;
				case 10:
					positionData10.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack10_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition10, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition10.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition10.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition10.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					//ApplicationLauncher.logger.info("UpdateliveTableData :"+CurrentProjectName+" Index:"+CurrentTestPointIndex+" :Rack"+LDU_ReadAddress+":<"+Resultstatus + " " + ErrorValue+">");
					*/
					break;
				case 11:
					positionData11.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack11_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition11, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition11.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition11.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition11.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					//ApplicationLauncher.logger.info("UpdateliveTableData :"+CurrentProjectName+" Index:"+CurrentTestPointIndex+" :Rack"+LDU_ReadAddress+":<"+Resultstatus + " " + ErrorValue+">");
					*/
					break;
				case 12:
					positionData12.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack12_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition12, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition12.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition12.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition12.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					//ApplicationLauncher.logger.info("UpdateliveTableData :"+CurrentProjectName+" Index:"+CurrentTestPointIndex+" :Rack"+LDU_ReadAddress+":<"+Resultstatus + " " + ErrorValue+">");
					*/ 
					break;
				case 13:
					positionData13.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack13_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition13, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition13.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition13.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition13.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					//ApplicationLauncher.logger.info("UpdateliveTableData :"+CurrentProjectName+" Index:"+CurrentTestPointIndex+" :Rack"+LDU_ReadAddress+":<"+Resultstatus + " " + ErrorValue+">");
					*/ 
					break;
					
					
					
				case 14:
					positionData14.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack14_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition14, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition14.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition14.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition14.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					*/ 
					break;
				case 15:
					positionData15.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack15_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition15, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition15.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition15.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition15.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					*/ 
					break;
				case 16:
					positionData16.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack16_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition16, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition16.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition16.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition16.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					*/ 
					break;
				case 17:
					positionData17.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack17_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition17, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition17.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition17.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition17.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					*/ 
					break;
				case 18:
					positionData18.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack18_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition18, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition18.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition18.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition18.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					*/ 
					break;
				case 19:
					positionData19.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack19_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition19, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition19.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition19.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition19.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					*/ 
					break;
				case 20:
					positionData20.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack20_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition20, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition20.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition20.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition20.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					*/ 
					break;

				
					
				case 21:
					positionData21.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack21_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition21, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition21.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition21.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition21.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					*/ 
					break;
				case 22:
					positionData22.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack22_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition22, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition22.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition22.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition22.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					*/ 
					break;
				case 23:
					positionData23.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack23_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition23, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition23.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition23.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition23.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					*/ 
					break;
				case 24:
					positionData24.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack24_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition24, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition24.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition24.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition24.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					*/ 
					break;
				case 25:
					positionData25.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack25_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition25, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition25.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition25.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition25.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					*/ 
					break;
				case 26:
					positionData26.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack26_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition26, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition26.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition26.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition26.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					*/ 
					break;
				case 27:
					positionData27.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack27_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition27, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition27.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition27.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition27.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					*/ 
					break;
				case 28:
					positionData28.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack28_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition28, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition28.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition28.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition28.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					*/ 
					break;
				case 29:
					positionData29.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack29_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition29, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition29.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition29.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition29.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					*/ 
					break;
				case 30:
					positionData30.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack30_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition30, Resultstatus);
					/* if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition30.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition30.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition30.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					*/ 
					
					break;
					
				case 31:
					positionData31.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack31_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition31, Resultstatus);
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition31.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition31.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition31.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					*/ 
					break;
				case 32:
					positionData32.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack32_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition32, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition32.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition32.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition32.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					*/ 
					break;
				case 33:
					positionData33.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack33_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition33, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition33.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition33.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition33.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					*/ 
					break;
				case 34:
					positionData34.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack34_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition34, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition34.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition34.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition34.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					*/ 
					break;
				case 35:
					positionData35.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack35_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition35, Resultstatus);
					/* if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition35.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition35.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition35.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					*/ 
					break;
				case 36:
					positionData36.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack36_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition36, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition36.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition36.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition36.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					*/ 
					break;
				case 37:
					positionData37.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack37_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition37, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition37.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition37.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition37.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					*/ 
					break;
				case 38:
					positionData38.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack38_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition38, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition38.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition38.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition38.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					*/ 
					break;
				case 39:
					positionData39.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack39_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition39, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition39.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition39.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition39.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					*/ 
					break;
				case 40:
					positionData40.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack40_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition40, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition40.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition40.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition40.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					*/ 
					break;
					
				case 41:
					positionData41.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack41_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition41, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition41.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition41.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition41.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					*/ 
					break;
				case 42:
					positionData42.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack42_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition42, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition42.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition42.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition42.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					*/ 
					break;
				case 43:
					positionData43.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack43_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition43, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition43.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition43.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition43.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					*/ 
					break;
				case 44:
					positionData44.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack44_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition44, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition44.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition44.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition44.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					*/ 
					break;
				case 45:
					positionData45.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack45_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition45, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition45.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition45.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition45.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					*/ 
					break;
				case 46:
					positionData46.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack46_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition46, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition46.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition46.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition46.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					*/ 
					break;
				case 47:
					positionData47.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack47_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition47, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition47.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition47.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition47.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					*/ 
					break;
				case 48:
					positionData48.setValue(ErrorValue); //liveTableDataListObj.get(CurrentTestPointIndex).setRack48_Data(Resultstatus + " " + ErrorValue);
					changeLduPositionBackground(ref_txtLduPosition48, Resultstatus); 
					/*  if(Resultstatus.equals(ConstantReport.RESULT_STATUS_PASS.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);cvcb
						ref_txtLduPosition48.getStyleClass().add(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME);
					}else if(Resultstatus.equals(ConstantReport.RESULT_STATUS_FAIL.trim())){ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
						ref_txtLduPosition48.getStyleClass().add(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
					}else{ 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_PASS_STYLE_CLASS_NAME); 
						ref_txtLduPosition01.getStyleClass().remove(ConstantLscsLDU.LDU_FAIL_STYLE_CLASS_NAME);
						ref_txtLduPosition48.getStyleClass().add(ConstantLscsLDU.LDU_DEFAULT_STYLE_CLASS_NAME);
					}
					*/ 
					break;

					
					
					
				default:
					break;
				}
    		});
    		


    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdate_Y_Phase :Exception:"+ e.getMessage());
    	}


	}
    
    @FXML 
    public void cmbBxLduSecDisplayTpOnChange(){
    	
    }
    
    @FXML 
    public void lduSecDisplayPreviousTrigger(){
    	
    }
    
    @FXML 
    public void lduSecDisplayNextTrigger(){
    	
    }
/*    
    public static void DeviceRefStdDataDisplayUpdate_R_Phase(final SerialDataRefStd R_PhaseData){

    	ApplicationLauncher.logger.info("DeviceRefStdDataDisplayUpdate_R_Phase : Entry");
    	try{
    		Platform.runLater(() -> {
	    		positionData01.setValue(R_PhaseData.VoltageDisplayData);
	    		positionData02.setValue(R_PhaseData.CurrentDisplayData);
	    		positionData03.setValue(R_PhaseData.PowerFactorData);
	    		positionData04.setValue(R_PhaseData.FreqDisplayData);
	    		positionData05.setValue(R_PhaseData.WattDisplayData);
	    		positionData07.setValue(R_PhaseData.VA_DisplayData);
	    		positionData06.setValue(R_PhaseData.VAR_DisplayData);
	    		positionData08.setValue(R_PhaseData.DegreePhaseData);
    		});

    		if(!R_PhaseData.VoltageDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_phaseVolt(R_PhaseData.VoltageDisplayData);
    		}
    		if(!R_PhaseData.CurrentDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_phaseCurrent(R_PhaseData.CurrentDisplayData);
    		}
    		if(!R_PhaseData.DegreePhaseData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_phaseDegree(R_PhaseData.DegreePhaseData);
    		}
    		if(!R_PhaseData.FreqDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackR_Frequency(R_PhaseData.FreqDisplayData);
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdate_R_Phase :Exception:"+ e.getMessage());
    	}

	}
    
    public static void DeviceRefStdDataDisplayUpdate_Y_Phase(final SerialDataRefStd Y_PhaseData){


    	ApplicationLauncher.logger.info("DeviceRefStdDataDisplayUpdate_Y_Phase : Entry");
    	try{
    		Platform.runLater(() -> {
	    		positionData10.setValue(Y_PhaseData.VoltageDisplayData);
	    		positionData11.setValue(Y_PhaseData.CurrentDisplayData);
	    		positionData12.setValue(Y_PhaseData.PowerFactorData);
	    		positionData13.setValue(Y_PhaseData.FreqDisplayData);
	    		positionData14.setValue(Y_PhaseData.WattDisplayData);
	    		positionData16.setValue(Y_PhaseData.VA_DisplayData);
	    		positionData15.setValue(Y_PhaseData.VAR_DisplayData);
	    		positionData17.setValue(Y_PhaseData.DegreePhaseData);
    		});
    		

    		if(!Y_PhaseData.VoltageDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackY_phaseVolt(Y_PhaseData.VoltageDisplayData);
    		}
    		if(!Y_PhaseData.CurrentDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackY_phaseCurrent(Y_PhaseData.CurrentDisplayData);
    		}
    		if(!Y_PhaseData.DegreePhaseData.isEmpty()){
    			ProjectExecutionController.setFeedbackY_phaseDegree(Y_PhaseData.DegreePhaseData);
    		}
    		if(!Y_PhaseData.FreqDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackY_Frequency(Y_PhaseData.FreqDisplayData);
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    		ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdate_Y_Phase :Exception:"+ e.getMessage());
    	}


	}
	
	public static void DeviceRefStdDataDisplayUpdate_B_Phase(final SerialDataRefStd B_PhaseData){

		
		ApplicationLauncher.logger.info("DeviceRefStdDataDisplayUpdate_B_Phase : Entry");
		try{
			Platform.runLater(() -> {
	    		positionData19.setValue(B_PhaseData.VoltageDisplayData);
	    		positionData20.setValue(B_PhaseData.CurrentDisplayData);
	    		positionData21.setValue(B_PhaseData.PowerFactorData);
	    		positionData22.setValue(B_PhaseData.FreqDisplayData);
	    		positionData23.setValue(B_PhaseData.WattDisplayData);
	    		positionData25.setValue(B_PhaseData.VA_DisplayData);
	    		positionData24.setValue(B_PhaseData.VAR_DisplayData);
	    		positionData27.setValue(B_PhaseData.DegreePhaseData);
			});

			
			if(!B_PhaseData.VoltageDisplayData.isEmpty()){
				ProjectExecutionController.setFeedbackB_phaseVolt(B_PhaseData.VoltageDisplayData);
			}
			if(!B_PhaseData.CurrentDisplayData.isEmpty()){
				ProjectExecutionController.setFeedbackB_phaseCurrent(B_PhaseData.CurrentDisplayData);
			}
			if(!B_PhaseData.DegreePhaseData.isEmpty()){
				ProjectExecutionController.setFeedbackB_phaseDegree(B_PhaseData.DegreePhaseData);
			}
    		if(!B_PhaseData.FreqDisplayData.isEmpty()){
    			ProjectExecutionController.setFeedbackB_Frequency(B_PhaseData.FreqDisplayData);
    		}
		}catch(Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("DeviceRefStdDataDisplayUpdate_B_Phase :Exception:"+ e.getMessage());
		}

	}
	
	public static void DeviceRefStdDataDisplayUpdateActiveEnergy(String R_phase, String Y_phase, String B_phase){
		Platform.runLater(() -> {
			positionData09.setValue(R_phase);
			positionData18.setValue(Y_phase);
			positionData26.setValue(B_phase);
		});
	}*/
/*
	public static void ResetInstantMetrics(){
    	Communicator SerialPortObj = null;
		SerialDataRefStd Reset_Value = new SerialDataRefStd(SerialPortObj);
		Reset_Value.VoltageDisplayData = "0";
		Reset_Value.CurrentDisplayData = "0";
		Reset_Value.PowerFactorData = "0";
		Reset_Value.FreqDisplayData = "0";
		Reset_Value.WattDisplayData = "0";
		Reset_Value.VA_DisplayData = "0";
		Reset_Value.VAR_DisplayData = "0";
		Reset_Value.DegreePhaseData = "0";
		DeviceRefStdDataDisplayUpdate_R_Phase(Reset_Value);
		DeviceRefStdDataDisplayUpdate_Y_Phase(Reset_Value);
		DeviceRefStdDataDisplayUpdate_B_Phase(Reset_Value);
		Platform.runLater(() -> {
			positionData09.setValue("");
			positionData18.setValue("");
			positionData26.setValue("");
		});
	}
	public static void ResetInstantMetrics_Wh_Display(){

		Platform.runLater(() -> {
			positionData09.setValue("");
			positionData18.setValue("");
			positionData26.setValue("");
		});
	}
*/
}
