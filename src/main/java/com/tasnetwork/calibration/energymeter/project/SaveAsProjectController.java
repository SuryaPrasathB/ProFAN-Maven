package com.tasnetwork.calibration.energymeter.project;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.database.MySQL_Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class SaveAsProjectController implements Initializable {
    @FXML
    private TextField txtNewProjectName;
    
    @FXML
    private ComboBox cmbBoxModel_List;
    static public ComboBox ref_cmbBoxSaveAsModel_List;
    static public TextField ref_txtNewProjectName;

    
    private boolean SubmitHit = false;


    @FXML
    private void submit() {
        SubmitHit = true;
        txtNewProjectName.getScene().getWindow().hide();
    }
    @FXML
    private void cancel() {
        SubmitHit = false;
        txtNewProjectName.getScene().getWindow().hide();
    }

    public String getNewProjectName() {
        return SubmitHit ? txtNewProjectName.getText() : null;

    }
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		ref_cmbBoxSaveAsModel_List = cmbBoxModel_List;
		ref_txtNewProjectName = txtNewProjectName;
		//txtNewProjectName = txtNewProjectName;
		guiInit();
		try {
			ProjectController.LoadModelList(ref_cmbBoxSaveAsModel_List);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.error("SaveAsProjectController : initialize : JSONException:"+e.getMessage());
		}
	}
	private void guiInit() {
		// TODO Auto-generated method stub
		
		
		Pattern pattern = Pattern.compile("[a-zA-Z0-9-]*");
		UnaryOperator<TextFormatter.Change> filter = c -> {
		    if (pattern.matcher(c.getControlNewText()).matches()) {
		        return c ;
		    } else {
		        return null ;
		    }
		};
		TextFormatter<String> formatter = new TextFormatter<>(filter);
		ref_txtNewProjectName.setTextFormatter(formatter);
		
	}
	

}
