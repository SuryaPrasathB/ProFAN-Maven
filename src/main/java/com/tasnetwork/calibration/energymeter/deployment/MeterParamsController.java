package com.tasnetwork.calibration.energymeter.deployment;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.database.MySQL_Controller;
import com.tasnetwork.calibration.energymeter.util.ComboBoxAutoComplete;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;


public class MeterParamsController implements Initializable {

	@FXML
	private AnchorPane meterparamsparentnode;

	@FXML ComboBox<String> cmbBox_projectName;

	@FXML
	private TextField txt_customerName;

	@FXML
	private TextField txt_modelName; 

	@FXML
	private TextField txt_modelType;

	@FXML
	private TextField txt_Ib;

	@FXML
	private TextField txt_Imax;

	@FXML
	private TextField txt_Vd;

	@FXML
	private TextField txt_impulses_per_unit;

	@FXML
	private TextField txt_class;


	@FXML
	private AnchorPane deploy_childnode;



	public static String currentProjectName;

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		MeterParamsAdjustScreen();
		
		try {
			LoadProjectNames();
			new ComboBoxAutoComplete<String>(cmbBox_projectName);
		} catch (JSONException e) {
			 
			e.printStackTrace();
			ApplicationLauncher.logger.info("MeterParamsController: initialize: JSONException:"+e.getMessage());
			
		}

	}  


	public void MeterParamsAdjustScreen(){

		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		double ScreenHeight = primaryScreenBounds.getHeight();
		double ScreenWidth= primaryScreenBounds.getWidth();
		double SplitPaneOffset = 149;
		double BottomStatusPaneOffset = 56;
		double MeterParaMeterHeightOffset = 300;

		long ScreenWidthThreshold = 1500;
		long ScreenHeightThreshold = 700;
		ConstantApp MyPropertyObj= new ConstantApp();
		ScreenWidthThreshold = ConstantAppConfig.ScreenWidthThreshold;
		ScreenHeightThreshold = ConstantAppConfig.ScreenHeightThreshold;

		ApplicationLauncher.logger.info("MeterParamsAdjustScreen :Current Screen Height:"+ScreenHeight);
		ApplicationLauncher.logger.info("MeterParamsAdjustScreen: Current Screen Width:"+ScreenWidth);
		ApplicationLauncher.logger.info("MeterParamsAdjustScreen :ScreenHeightThreshold:"+ScreenHeightThreshold);
		ApplicationLauncher.logger.info("MeterParamsAdjustScreen: ScreenWidthThreshold:"+ScreenWidthThreshold);
		if (ScreenHeight> ScreenHeightThreshold){
			deploy_childnode.setMinHeight(ScreenHeight - MeterParaMeterHeightOffset );


		}

		double DeployTestPointTableWidth = ScreenWidth-SplitPaneOffset-25;
		if(ScreenWidth >ScreenWidthThreshold){

			deploy_childnode.setMinWidth(DeployTestPointTableWidth );
		}else{
			ApplicationLauncher.logger.info("MeterParamsAdjustScreen : ScreenWidthThreshold else: entry ");
			deploy_childnode.setMinWidth(DeployTestPointTableWidth - 400 );
		}

	}


	public static String getCurrentProjectName(){
		return currentProjectName;
	}

	public void setCurrentProjectName(String project_name){
		currentProjectName = project_name;
	}

	public void LoadProjectNames() throws JSONException{
		ArrayList<String> projectList =new ArrayList<String>();
		JSONObject result = MySQL_Controller.sp_getproject_list();
		JSONArray project_arr = result.getJSONArray("Projects");
		for(int i =0; i<project_arr.length(); i++ ){
			projectList.add(project_arr.getString(i));
		}
		cmbBox_projectName.getItems().clear();
		cmbBox_projectName.getItems().addAll(projectList);
		cmbBox_projectName.getSelectionModel().select(0);
	}
	public void LoadModelDetails(){
		String projectName = cmbBox_projectName.getValue();
		//String currentSelectedCustomerName = ref_cmbBox_CustomerName.getSelectionModel().getSelectedItem().toString();
		//String meterType = txt_modelName.getText();//.getSelectionModel().getSelectedItem().toString();
		//String projectName = project_name+ConstantApp.PROJECT_NAME_SEPERATOR+meterType;
		int model_id = MySQL_Controller.sp_getProjectModel_ID(projectName);
		//bkbjh
		setCurrentProjectName(projectName);
		JSONObject modeldata = MySQL_Controller.sp_getem_model_data(model_id);
		if (true){

			//ApplicationLauncher.logger.debug("loadManageDeployDataToDB: modeldata:"+modeldata);
			try {
				txt_customerName.setText(modeldata.getString("customer_name"));
				txt_modelName.setText(modeldata.getString("model_name"));
				txt_modelType.setText(modeldata.getString("model_type")); 
				txt_class.setText(modeldata.getString("model_class"));
				txt_Ib.setText(modeldata.getString("basic_current_ib")); 
				txt_Imax.setText(modeldata.getString("max_current_imax")); 
				txt_Vd.setText(modeldata.getString("rated_voltage_vd")); 
				txt_impulses_per_unit.setText(modeldata.getString("impulses_per_unit"));
				//String meterType = txt_modelName.getText();
				//projectName = projectName+ConstantApp.PROJECT_NAME_SEPERATOR+meterType;
				//ApplicationLauncher.logger.debug("loadManageDeployDataToDB: projectName:"+projectName);
				//setCurrentProjectName(projectName);
			} catch (JSONException e) {
				 
				e.printStackTrace();
				ApplicationLauncher.logger.info("LoadModelDetails: JSONException:"+e.getMessage());
			}

		}
	}

	public void resetDevices() throws IOException{
		txt_customerName.clear();
		txt_modelName.clear();
		txt_modelType.clear();
		txt_Ib.clear();
		txt_Imax.clear();
		txt_Vd.clear();
		txt_impulses_per_unit.clear();
		unloadCurrentTestPropertyFXML();
	}

	public void loadDevices(){
		ResetPane();
		ApplicationLauncher.logger.info("You clicked LOAD Button!");
		LoadModelDetails();
		AnchorPane childpane = (AnchorPane)meterparamsparentnode.getParent().getScene().lookup("#deploy_childnode");
		ApplicationLauncher.logger.info(childpane);
		try {
			childpane.getChildren().add(getNodeFromFXML("/fxml/deployment/DeploymentManager" + ConstantApp.THEME_FXML));
		}catch(Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.info("loadDevices: Exception:"+e.getMessage());
		}
	}

	public void ResetPane(){
		AnchorPane childpane = (AnchorPane)meterparamsparentnode.getParent().getScene().lookup("#deploy_childnode");
		childpane.getChildren().clear();
	}

	private Parent getNodeFromFXML(String url) throws IOException{
		return FXMLLoader.load(getClass().getResource(url));
	}

	public void unloadCurrentTestPropertyFXML() throws IOException{
		AnchorPane childpane = (AnchorPane)meterparamsparentnode.getParent().getScene().lookup("#deploy_childnode");
		childpane.getChildren().clear();
	}        
}

