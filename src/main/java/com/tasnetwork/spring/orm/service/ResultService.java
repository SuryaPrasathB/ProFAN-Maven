package com.tasnetwork.spring.orm.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tasnetwork.calibration.energymeter.project.ReportGeneratorController.SearchQuery;
import com.tasnetwork.spring.orm.model.ProjectRun;
import com.tasnetwork.spring.orm.model.Result;
import com.tasnetwork.spring.orm.repository.ResultRepo;

@Service
public class ResultService {

	@Autowired
	private ResultRepo resultRepo;

	@Transactional
	public Long saveToDb (Result result) {
		Result resultData = resultRepo.save(result);
		return resultData.getId();
	}

	public Result findByProjectRunAndFanSerialNumberAndTestPointName(ProjectRun projectRun, String fanSerialNumber,
			String testPointName) {
		return resultRepo.findByProjectRunAndFanSerialNumberAndTestPointName(projectRun, fanSerialNumber, testPointName);
	}
	
	public Result findByFanSerialNumberAndTestPointName(String fanSerialNumber, String testPointName) {
		return resultRepo.findByFanSerialNumberAndTestPointName(fanSerialNumber, testPointName);
	}

	public Result findByFanSerialNumberAndTestPointNameAndProjectRunAndTestStatus(
			String fanSerialNumber,
			String testPointName, 
			ProjectRun currentProjectRun, 
			String status) {
		return resultRepo.findByFanSerialNumberAndTestPointNameAndProjectRunAndTestStatus(fanSerialNumber, testPointName, currentProjectRun, status);
	}

	public List<Result> findByFanSerialNumber(String fanSerialNumber) {
		return resultRepo.findByFanSerialNumber(fanSerialNumber);
	}

	public List<Result> findall() {
		return resultRepo.findAll();
	}
	
	/**
     * Finds Results by a given epoch time range (inclusive) and test status.
     * This method directly calls the corresponding repository method.
     *
     * @param fromEpochTime 	The start epoch time (inclusive, in milliseconds).
     * @param toEpochTime   	The end epoch time (inclusive, in milliseconds).
     * @param fanSerialNumber   The fan serial number     
     * @return A list of Results matching the criteria.
     */
    public List<Result> findByEpochTimeBetweenAndFanSerialNumber(Long fromEpochTime, Long toEpochTime, String fanSerialNumber) {
        return resultRepo.findByEpochTimeBetweenAndFanSerialNumber(fromEpochTime, toEpochTime, fanSerialNumber);
    }
    
    public List<Result> findByEpochTimeBetween(Long fromEpochTime, Long toEpochTime) {
    	return resultRepo.findByEpochTimeBetween(fromEpochTime, toEpochTime);
    }


}