package com.tasnetwork.calibration.energymeter.setting;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.tasnetwork.calibration.energymeter.ApplicationHomeController;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantAppConfig;
import com.tasnetwork.calibration.energymeter.constant.ProcalFeatureEnable;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.uac.UacDataModel;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;

public class SystemSettingController implements Initializable  {

	@FXML
	private AnchorPane DeviceSubMenupane;

	@FXML
	private AnchorPane device_childpane;
	
	@FXML
	private AnchorPane dut_childpane;

	@FXML
	private AnchorPane stability_childpane;

	@FXML
	private AnchorPane about_childpane;

	@FXML
	private AnchorPane admin_childpane;

	@FXML
	private AnchorPane backup_results_childpane;

	@FXML
	private AnchorPane ref_std_const_childpane;

	@FXML
	private AnchorPane reportconfig_childpane;

	@FXML
	private AnchorPane reportexcelconfig_childpane;

	@FXML
	private AnchorPane reportfilelocation_childpane;

	@FXML
	private Accordion deviceAccordian;

	@FXML
	private TitledPane deviceSettingTitlePane;
	
	@FXML
	private TitledPane dutSettingsTitledPane;

	@FXML
	private TitledPane titledPaneRefConst;

	@FXML
	private TitledPane titlepane_admin;

	@FXML
	private TitledPane titledpane_backupresults;
	
	@FXML
	private TitledPane titledPaneStability;	
	
	
	@FXML
	private TitledPane titledPaneReportConfiguration;	

	public void LoadAllChild_FXML(){
		ApplicationLauncher.logger.info("LoadAllChild_FXML:Entry");
		try {
			try{
				device_childpane.getChildren().add(getNodeFromFXML("/fxml/setting/DevicePortSetup" + ConstantApp.THEME_FXML));

			} catch (Exception e){
				e.printStackTrace();
				ApplicationLauncher.logger.error("LoadAllChild_FXML: Exception:" + e.getMessage());
			}
			
			if(ConstantAppConfig.DUT_COMM_FEATURE_ENABLED){
				dut_childpane.getChildren().add(getNodeFromFXML("/fxml/setting/DutPortSetup" + ConstantApp.THEME_FXML));
				
			}

			stability_childpane.getChildren().add(getNodeFromFXML("/fxml/setting/StabilizationValidation" + ConstantApp.THEME_FXML));
			about_childpane.getChildren().add(getNodeFromFXML("/fxml/setting/About" + ConstantApp.THEME_FXML));
			admin_childpane.getChildren().add(getNodeFromFXML("/fxml/setting/Admin_page" + ConstantApp.THEME_FXML));
			backup_results_childpane.getChildren().add(getNodeFromFXML("/fxml/setting/Backup_Results" + ConstantApp.THEME_FXML));
			if(!ProcalFeatureEnable.PROPOWER_SRC_ONLY) {
				reportconfig_childpane.getChildren().add(getNodeFromFXML("/fxml/testreport/ReportSubMenu" + ConstantApp.THEME_FXML));
			}
			ref_std_const_childpane.getChildren().add(getNodeFromFXML("/fxml/setting/RefStdConst" + ConstantApp.THEME_FXML));
		}catch(Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.info("LoadAllChild_FXML: Exception:" + e.getMessage());
		}
	}



	private Parent getNodeFromFXML(String url) throws IOException{
		return FXMLLoader.load(getClass().getResource(url));
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		deviceAccordian.setExpandedPane(deviceSettingTitlePane);
		ApplicationLauncher.logger.info("SystemSettingController: initialize: REF_STD_CONSTANT_CONFIG_ENABLE: " + ConstantAppConfig.REF_STD_CONSTANT_CONFIG_ENABLE);
		try{
			if(ConstantAppConfig.REF_STD_CONSTANT_CONFIG_ENABLE){
				titledPaneRefConst.setVisible(true);
			}
			else{
				titledPaneRefConst.setVisible(false);
			}
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("SystemSettingController: initialize: Exception:" + e.getMessage());
			titledPaneRefConst.setVisible(false);
		}
/*		if((ConstantApp.USER_ACCESS_LEVEL.equals(ConstantApp.TESTER_ACCESS_LEVEL))
				|| (ConstantApp.USER_ACCESS_LEVEL.equals(ConstantApp.READONLY_ACCESS_LEVEL))){
			titlepane_admin.setVisible(false);
		}*/
		if(!ConstantAppConfig.DUT_COMM_FEATURE_ENABLED){
			
			//dutSettingsTitledPane.setDisable(true);
			dutSettingsTitledPane.setVisible(false);
		}
		
		if(ProcalFeatureEnable.USER_ACCESS_CONTROL_ENABLED){
			if(!ApplicationHomeController.isUacAdminScreenDisplayEnabled()){
				
				titlepane_admin.setDisable(true);
			}
			
			
			
			if(!ApplicationHomeController.isUacDeviceSettingsScreenDisplayEnabled()){

				deviceSettingTitlePane.setDisable(true);
			}

			if(!ApplicationHomeController.isUacStabilitySettingsScreenDisplayEnabled()){

				titledPaneStability.setDisable(true);
			}

			if(!ApplicationHomeController.isUacReportConfigurationScreenDisplayEnabled()){

				titledPaneReportConfiguration.setDisable(true);
			}

			if(!ApplicationHomeController.isUacBackupSettingsScreenDisplayEnabled()){

				titledpane_backupresults.setDisable(true);
			}
			
			applyUacSettings();
			
		}
		
		if(ProcalFeatureEnable.PROPOWER_SRC_ONLY ){
			
			titledPaneStability.setVisible(false);
			titledPaneReportConfiguration.setVisible(false);
			titledpane_backupresults.setVisible(false);
			
		}

		LoadAllChild_FXML();
	}
	
	private static void applyUacSettings() {
		// TODO Auto-generated method stub
		ApplicationLauncher.logger.info("SystemSettingController : applyUacSettings :  Entry");
		ArrayList<UacDataModel> uacSelectProfileScreenList = DeviceDataManagerController.getUacSelectProfileScreenList();
		String screenName = "";
		for (int i = 0; i < uacSelectProfileScreenList.size(); i++){

			screenName = uacSelectProfileScreenList.get(i).getScreenName();
			switch (screenName) {
				case ConstantApp.UAC_SETTINGS_SCREEN:
					
					
					if(!uacSelectProfileScreenList.get(i).getExecutePossible()){
						//ref_btn_get_results.setDisable(true);
						
					}
					
					if(!uacSelectProfileScreenList.get(i).getAddPossible()){
						//ref_btn_add.setDisable(true);
						
					}
					
					if(!uacSelectProfileScreenList.get(i).getUpdatePossible()){
						//ref_btn_save.setDisable(true);
						
					}
					
					if(!uacSelectProfileScreenList.get(i).getDeletePossible()){
						//ref_btn_remove.setDisable(true);
						//ref_btn_reset.setDisable(true);
						
					}
					break;
					
								
	
				default:
					break;
			}
			
				
				
		}
	}
}
