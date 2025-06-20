package com.tasnetwork.spring.orm.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.tasnetwork.calibration.energymeter.project.ReportGeneratorController.SearchQuery;
import com.tasnetwork.spring.orm.model.ProjectRun;
import com.tasnetwork.spring.orm.model.Result;


public interface ResultRepo extends JpaRepository<Result, Long>{

	Result findByProjectRunAndFanSerialNumberAndTestPointName(ProjectRun projectRun, String fanSerialNumber, String testPointName);
	
	Result findByFanSerialNumberAndTestPointName(String fanSerialNumber, String testPointName);

	Result findByFanSerialNumberAndTestPointNameAndProjectRunAndTestStatus(String fanSerialNumber, String testPointName,
			ProjectRun currentProjectRun, String status);

	List<Result> findByFanSerialNumber(String fanSerialNumber);
	
	/**
     * Finds Results by a given epoch time range (inclusive) and test status.
     * This directly maps to the 'epochTime' and 'testStatus' fields in the Result entity.
     *
     * @param fromEpochTime The start epoch time (inclusive, in milliseconds).
     * @param toEpochTime   The end epoch time (inclusive, in milliseconds).
     * @param testStatus    The test status to filter by (e.g., "COMPLETED", "PASSED", "FAILED").
     * @return A list of Results matching the criteria.
     */
    List<Result> findByEpochTimeBetweenAndFanSerialNumber(Long fromEpochTime, Long toEpochTime, String fanSerialNumber);
    
    List<Result> findByEpochTimeBetween(Long fromEpochTime, Long toEpochTime);
}