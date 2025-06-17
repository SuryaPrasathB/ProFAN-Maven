package com.tasnetwork.spring.orm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tasnetwork.spring.orm.model.ProjectRun;
import com.tasnetwork.spring.orm.model.Result;

public interface ResultRepo extends JpaRepository<Result, Long>{

	Result findByProjectRunAndFanSerialNumberAndTestPointName(ProjectRun projectRun, String fanSerialNumber, String testPointName);
	
	Result findByFanSerialNumberAndTestPointName(String fanSerialNumber, String testPointName);

	Result findByFanSerialNumberAndTestPointNameAndProjectRunAndTestStatus(String fanSerialNumber, String testPointName,
			ProjectRun currentProjectRun, String status);

	List<Result> findByFanSerialNumber(String fanSerialNumber);
	
}
