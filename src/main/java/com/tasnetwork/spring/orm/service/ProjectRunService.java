package com.tasnetwork.spring.orm.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.spring.orm.model.ProjectRun;
import com.tasnetwork.spring.orm.model.Result;
import com.tasnetwork.spring.orm.repository.ProjectRunRepo;
import com.tasnetwork.spring.orm.repository.ResultRepo;

@Service
public class ProjectRunService {
	
	@Autowired
	private ProjectRunRepo projectRunRepo;
	
	@Autowired
	private ResultRepo resultRepo;
	
	public ProjectRun createProjectRunWithResults(ProjectRun projectRun, List<Result> results) {
		// Save ProjectRun First
		ProjectRun savedRun = projectRunRepo.save(projectRun);
		
		// Assign ProjectRun reference to each Result and Save
		for (Result result : results) {
			result.setProjectRun(savedRun);
			resultRepo.save(result);
		}
		
		return savedRun;
	}
	
	@Transactional
	public Long saveToDb (ProjectRun data) {
		ProjectRun projectRunData = projectRunRepo.save(data);
		return projectRunData.getTestRunId();
	}
	
	@Transactional
	public ProjectRun saveProjectRun(ProjectRun projectRun) {
	    return projectRunRepo.save(projectRun);
	}
	
	@Transactional
	public void deleteByTestrunId(int testrunId) {
		projectRunRepo.deleteByTestRunId(testrunId);
	}
	
	@Transactional
	public List<ProjectRun> findAll() {
		return projectRunRepo.findAll();
	}
	
	@Transactional
	public ProjectRun findActiveByDeploymentId(String deploymentId) {
		
		String executionStatus = ConstantApp.EXECUTION_STATUS_NOT_STARTED;
		return projectRunRepo.findByDeploymentIdAndExecutionStatus(deploymentId,executionStatus);
	}

	public String findTopByProjectNameOrderByDeploymentIdDesc(String projectName) {
		 ProjectRun lastRun = projectRunRepo.findTopByProjectNameOrderByDeploymentIdDesc(projectName);
		 return (lastRun != null) ? lastRun.getDeploymentId() : "0";
	}

	public ProjectRun findTopByProjectNameAndExecutionStatusOrderByDeploymentIdDesc(String projectName, String testStatus) {
		return projectRunRepo.findTopByProjectNameAndExecutionStatusOrderByDeploymentIdDesc(projectName, testStatus);
	}


	
	
}
