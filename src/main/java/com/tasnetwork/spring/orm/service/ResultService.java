package com.tasnetwork.spring.orm.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
