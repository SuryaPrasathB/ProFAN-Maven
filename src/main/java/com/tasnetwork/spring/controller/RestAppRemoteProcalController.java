package com.tasnetwork.spring.controller;

import java.util.ArrayList;
import java.util.Map;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;
import com.tasnetwork.calibration.energymeter.deployment.ProjectExecutionController;
import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.calibration.energymeter.remote.ProcalRemoteResponse;
import com.tasnetwork.calibration.energymeter.remote.TestPointStatus;

@RestController
@RequestMapping("/procal")
public class RestAppRemoteProcalController {


	@GetMapping(value = "/")
	public String home() {

		//System.out.println("RestAppRemoteProcalController: home : Entry");
		ApplicationLauncher.logger.info("RestAppRemoteProcalController: home path hit");
		String data = "AppName = " + ConstantVersion.APPLICATION_NAME + " : " +ConstantVersion.APPLICATION_VERSION ;
		ApplicationLauncher.logger.info("RestAppRemoteProcalController: home: " + data);
		return data;
	}

	@RequestMapping(value = {"/calib/steprun" , "/verific/steprun", "/sta1/steprun","/sta2/steprun" })
	public ResponseEntity<ProcalRemoteResponse>  stepRunTest() {
		ApplicationLauncher.logger.info("RestAppRemoteProcalController: stepRunTest hit");
		
		ProjectExecutionController.setDutCalibrationVoltageTargetSet(false);
		ProjectExecutionController.setDutCalibrationCurrentTargetSet(false);
		ProjectExecutionController.setDutCalibrationVoltCurrentSetZero(false);
		ProjectExecutionController.setDutCalibrationCurrentZeroSet(false);
		
		//System.out.println("RestAppRemoteProcalController: home : Entry");
		
		ProjectExecutionController projectExecutionController = new ProjectExecutionController();
		projectExecutionController.procalRemoteStepRunTrigger();
		//String data = "AppName = " + ConstantVersion.APPLICATION_NAME + " : " +ConstantVersion.APPLICATION_VERSION ;
		//ApplicationLauncher.logger.info("RestAppRemoteProcalController: home: " + data);

		return ResponseEntity.ok(new ProcalRemoteResponse("stepRunTestExecuteInitiated"));
	}


	@RequestMapping(value = {"/calib/start", "/verific/start", "/sta1/start","/sta2/start" })
	public ResponseEntity<ProcalRemoteResponse> startTest() {

		//System.out.println("RestAppRemoteProcalController: home : Entry");
		ApplicationLauncher.logger.info("RestAppRemoteProcalController: startTest hit");
		ProjectExecutionController projectExecutionController = new ProjectExecutionController();
		projectExecutionController.procalRemoteStartTrigger();
		//String data = "AppName = " + ConstantVersion.APPLICATION_NAME + " : " +ConstantVersion.APPLICATION_VERSION ;
		//ApplicationLauncher.logger.info("RestAppRemoteProcalController: home: " + data);


		return ResponseEntity.ok(new ProcalRemoteResponse("startTestExecuteInitiated"));
		//return "startTestExecuteInitiated";	
	}
	
	
	@RequestMapping(value = {"/calib/updateDutSerialNo", "/verific/updateDutSerialNo", "/sta1/updateDutSerialNo","/sta2/updateDutSerialNo" },
			method = RequestMethod.POST,
		    consumes = "application/json",
		    produces = "application/json"
			)
	public ResponseEntity<ProcalRemoteResponse> updateConveyorDutSerialNo(@RequestBody Map<Integer, String> dutSerialMap) {

		//System.out.println("RestAppRemoteProcalController: home : Entry");
		ApplicationLauncher.logger.info("RestAppRemoteProcalController: updateConveyorDutSerialNo hit");
		DeviceDataManagerController.clearConveyorDutSerialNumberMap();
		
		DeviceDataManagerController.setConveyorDutSerialNumberMap(dutSerialMap);
	    /*for (Map.Entry<Integer, String> entry : dutSerialMap.entrySet()) {
	        DeviceDataManagerController.setConveyorDutSerialNumberMap(entry.getValue(), entry.getKey());
	    }*/
		DeviceDataManagerController.getConveyorDutSerialNumberMap().entrySet().forEach(e->{
			ApplicationLauncher.logger.info("RestAppRemoteProcalController: updateConveyorDutSerialNo: " + e.getKey() +" -> " +e.getValue());
		});
		
		return ResponseEntity.ok(new ProcalRemoteResponse("updateDutSerialNoDone"));
		//return "startTestExecuteInitiated";	
	}




	@RequestMapping(value = {"/calib/stop", "/verific/stop", "/sta1/stop","/sta2/stop" })
	public ResponseEntity<ProcalRemoteResponse> stopTest() {

		//System.out.println("RestAppRemoteProcalController: home : Entry");
		ApplicationLauncher.logger.info("RestAppRemoteProcalController: stopTest hit");
		ProjectExecutionController projectExecutionController = new ProjectExecutionController();
		projectExecutionController.procalRemoteStopTrigger();
		//String data = "AppName = " + ConstantVersion.APPLICATION_NAME + " : " +ConstantVersion.APPLICATION_VERSION ;
		//ApplicationLauncher.logger.info("RestAppRemoteProcalController: home: " + data);

		return ResponseEntity.ok(new ProcalRemoteResponse("testStopInitiated"));
		//return "testStopInitiated";
	}


	@RequestMapping(value = {"/calib/tpList", "/verific/tpList", "/sta1/tpList","/sta2/tpList" })
	public ArrayList<String> getTestPointIdList() {

		//System.out.println("RestAppRemoteProcalController: home : Entry");
		ApplicationLauncher.logger.info("RestAppRemoteProcalController: getTestPointIdList hit");
		ProjectExecutionController projectExecutionController = new ProjectExecutionController();
		ArrayList<String> testPointIdList = projectExecutionController.getProcalRemotePresentTestPointIdList();
		//String data = "AppName = " + ConstantVersion.APPLICATION_NAME + " : " +ConstantVersion.APPLICATION_VERSION ;
		ApplicationLauncher.logger.info("RestAppRemoteProcalController: testPointIdList: " + testPointIdList.toString());
		return testPointIdList;
	}

	@RequestMapping(value = {"/calib/getTpIdResult", "/verific/getTpIdResult", "/sta1/getTpIdResult","/sta2/getTpIdResult" })
	public ResponseEntity<Object>  getPresentTestPointIdResult() {

		//System.out.println("RestAppRemoteProcalController: home : Entry");
		ApplicationLauncher.logger.info("RestAppRemoteProcalController: getPresentTestPointIdResult hit");
		ProjectExecutionController projectExecutionController = new ProjectExecutionController();
		TestPointStatus testPointStatus = projectExecutionController.getProcalRemotePresentTestPointStatus();

		ProcalRemoteResponse procalRemoteResponse = new ProcalRemoteResponse();
		procalRemoteResponse.setTestPointStatus(testPointStatus);
		procalRemoteResponse.setMessage("tpStatus");

		//String data = "AppName = " + ConstantVersion.APPLICATION_NAME + " : " +ConstantVersion.APPLICATION_VERSION ;
		//ApplicationLauncher.logger.info("RestAppRemoteProcalController: testPointIdList: " + testPointIdList.toString());
		return ResponseEntity.ok(procalRemoteResponse);
	}
	
	@RequestMapping(value = {"/calib/parameterStatus"})
	public ResponseEntity<Object> getParameterStatus() {
		
		ApplicationLauncher.logger.info("RestAppRemoteProcalController : getParameterStatus : hit");
		
		/*
		 * DOUBTS
		 * Separate call back to just get param stauts ?
		 * JSON object ? Or Procal Remote response ?
		 * 
		 */
		
		//ApplicationLauncher.logger.info("RestAppRemoteProcalController: getPresentTestPointIdResult hit");
		//ProjectExecutionController projectExecutionController = new ProjectExecutionController();
		//TestPointStatus testPointStatus = projectExecutionController.getProcalRemotePresentTestPointStatus();

		ProcalRemoteResponse procalRemoteResponse = new ProcalRemoteResponse();
		procalRemoteResponse.setDutCalibrationVoltageTargetSet(ProjectExecutionController.isDutCalibrationVoltageTargetSet());
		procalRemoteResponse.setDutCalibrationCurrentTargetSet(ProjectExecutionController.isDutCalibrationCurrentTargetSet());
		procalRemoteResponse.setDutCalibrationVoltCurrentSetZero(ProjectExecutionController.isDutCalibrationVoltCurrentSetZero());
		procalRemoteResponse.setDutCalibrationCurrentZeroSet(ProjectExecutionController.isDutCalibrationCurrentZeroSet());
		procalRemoteResponse.setMessage("tpParamStatus");

		//String data = "AppName = " + ConstantVersion.APPLICATION_NAME + " : " +ConstantVersion.APPLICATION_VERSION ;
		//ApplicationLauncher.logger.info("RestAppRemoteProcalController: testPointIdList: " + testPointIdList.toString());
		return ResponseEntity.ok(procalRemoteResponse);
		
		//return ResponseEntity.ok("PARAM STATUS");
	}

	@RequestMapping(value = {"/calib/switchToNext" , "/verific/switchToNext", "/sta1/switchToNext","/sta2/switchToNext" })
	public ResponseEntity<ProcalRemoteResponse> switchToNext() {

		/*
		 * Switch to next test point logic
		 */
		
		ProjectExecutionController.setDutCalibrationVoltageTargetSet(false);
		ProjectExecutionController.setDutCalibrationCurrentTargetSet(false);
		ProjectExecutionController.setDutCalibrationVoltCurrentSetZero(false);
		ProjectExecutionController.setDutCalibrationCurrentZeroSet(false);
			
		ProjectExecutionController projectExecutionController = new ProjectExecutionController();
		projectExecutionController.procalRemoteLoadNextTrigger();
		//String data = "AppName = " + ConstantVersion.APPLICATION_NAME + " : " +ConstantVersion.APPLICATION_VERSION ;
		//ApplicationLauncher.logger.info("RestAppRemoteProcalController: home: " + data);


		return ResponseEntity.ok(new ProcalRemoteResponse("switchToNextExecuteInitiated"));

		//return "switchToNextExecuteInitiated";
	}
	
	
	@RequestMapping(value = {"/calib/getAllTpResult", "/verific/getAllTpResult", "/sta1/getAllTpResult","/sta2/getAllTpResult" })
	public ResponseEntity<Object>  getAllTestPointResult() {

		/*//System.out.println("RestAppRemoteProcalController: home : Entry");
		ApplicationLauncher.logger.info("RestAppRemoteProcalController: getAllTestPointResult hit");
		ProjectExecutionController projectExecutionController = new ProjectExecutionController();
		JSONObject result = projectExecutionController.getProcalRemoteAllTestPointStatus();

		ProcalRemoteResponse procalRemoteResponse = new ProcalRemoteResponse();
		procalRemoteResponse.setResult(result); //setTestPointStatus(testPointStatus);
		procalRemoteResponse.setMessage("tpStatus");

		//String data = "AppName = " + ConstantVersion.APPLICATION_NAME + " : " +ConstantVersion.APPLICATION_VERSION ;
		//ApplicationLauncher.logger.info("RestAppRemoteProcalController: testPointIdList: " + testPointIdList.toString());
		return ResponseEntity.ok(procalRemoteResponse);*/

		ApplicationLauncher.logger.info("RestAppRemoteProcalController: getProcalRemoteAllTestPointStatus hit");
		
		ProjectExecutionController projectExecutionController = new ProjectExecutionController();
		JSONObject result = projectExecutionController.getProcalRemoteAllTestPointStatus();

		//return ResponseEntity.ok("DUMMY");
		return ResponseEntity.ok(result.toString());

	}

	@RequestMapping(value = {"/calib/getRunId", "/verific/getRunId", "/sta1/getRunId","/sta2/getRunId" })
	public ResponseEntity<Object>  getPresentRunId() {

		//System.out.println("RestAppRemoteProcalController: home : Entry");
		ApplicationLauncher.logger.info("RestAppRemoteProcalController: getPresentRunId hit");
		ProjectExecutionController projectExecutionController = new ProjectExecutionController();
		String presentRunId = projectExecutionController.getProcalRemoteProjectRunId();
		ApplicationLauncher.logger.info("RestAppRemoteProcalController: presentRunId: " + presentRunId);
		//ProcalRemoteResponse procalRemoteResponse = new ProcalRemoteResponse();
		//procalRemoteResponse.setTestPointStatus(testPointStatus);
		//procalRemoteResponse.setMessage("testPointStatus");

		//String data = "AppName = " + ConstantVersion.APPLICATION_NAME + " : " +ConstantVersion.APPLICATION_VERSION ;
		//ApplicationLauncher.logger.info("RestAppRemoteProcalController: testPointIdList: " + testPointIdList.toString());
		return ResponseEntity.ok(new ProcalRemoteResponse(presentRunId));
	}


	@RequestMapping(value = {"/calib/selectRunProject", "/verific/selectRunProject", "/sta1/selectRunProject","/sta2/selectRunProject" })
	public ResponseEntity<Object>  selectProject(@RequestParam String projectName ) {

		//System.out.println("RestAppRemoteProcalController: home : Entry");
		ApplicationLauncher.logger.info("RestAppRemoteProcalController: getPresentRunId hit : " + projectName);
		ProjectExecutionController projectExecutionController = new ProjectExecutionController();
		//String projectName = "DevSysVerific01";//"Fuji-Routine";//DevSysVerificO1
		String remoteResponseMessage = projectExecutionController.getProcalRemoteSelectRunProject(projectName);
		ApplicationLauncher.logger.info("RestAppRemoteProcalController: remoteResponseMessage: " + remoteResponseMessage);
		//ProcalRemoteResponse procalRemoteResponse = new ProcalRemoteResponse();
		//procalRemoteResponse.setTestPointStatus(testPointStatus);
		//procalRemoteResponse.setMessage("testPointStatus");

		//String data = "AppName = " + ConstantVersion.APPLICATION_NAME + " : " +ConstantVersion.APPLICATION_VERSION ;
		//ApplicationLauncher.logger.info("RestAppRemoteProcalController: testPointIdList: " + testPointIdList.toString());
		//return ResponseEntity.ok(new ProcalRemoteResponse(remoteResponseMessage));
		return ResponseEntity.ok(new ProcalRemoteResponse(remoteResponseMessage));
	}


	@RequestMapping(value = {"/calib/closeRunProject", "/verific/closeRunProject", "/sta1/closeRunProject","/sta2/closeRunProject" })
	public ResponseEntity<Object>  closeProject(@RequestParam String projectName ){//@PathVariable String projectName){/

		//System.out.println("RestAppRemoteProcalController: home : Entry");
		ApplicationLauncher.logger.info("RestAppRemoteProcalController: getPresentRunId hit : " +projectName);
		ProjectExecutionController projectExecutionController = new ProjectExecutionController();
		//String projectName = reqParam.get("projectName");//"DevSysVerific01";//"Fuji-Routine";
		String remoteResponseMessage = projectExecutionController.closeProcalRemoteRunProject(projectName);
		ApplicationLauncher.logger.info("RestAppRemoteProcalController: remoteResponseMessage: " + remoteResponseMessage);
		//ProcalRemoteResponse procalRemoteResponse = new ProcalRemoteResponse();
		//procalRemoteResponse.setTestPointStatus(testPointStatus);
		//procalRemoteResponse.setMessage("testPointStatus");

		//String data = "AppName = " + ConstantVersion.APPLICATION_NAME + " : " +ConstantVersion.APPLICATION_VERSION ;
		//ApplicationLauncher.logger.info("RestAppRemoteProcalController: testPointIdList: " + testPointIdList.toString());
		//return ResponseEntity.ok(new ProcalRemoteResponse(remoteResponseMessage));
		return ResponseEntity.ok(new ProcalRemoteResponse(remoteResponseMessage));
	}
	
	

}
